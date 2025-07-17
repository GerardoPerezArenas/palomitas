/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.business.comunicaciones.persistence;

import es.altia.flexia.business.comunicaciones.vo.ComunicacionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Clase que implementa las operaciones de acceso a datos de las comunicaciones de Flexia
 *
 * @author Ricardo
 */
public class ComunicacionesFlexiaDAO {
    private static ComunicacionesFlexiaDAO instance =	null;
   
    protected static Log m_Log =
            LogFactory.getLog(ComunicacionesFlexiaDAO.class.getName());




    /** Constructor
     * 
     */
    protected ComunicacionesFlexiaDAO() {
              

     }

     /** Método que inicializa el DAO si no lo está previamente
      * 
      * @return
      */
     public static ComunicacionesFlexiaDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (ComunicacionesFlexiaDAO.class) {
            if (instance == null) {
                instance = new ComunicacionesFlexiaDAO();
            }

        }
        return instance;
    }
     
    /** Método que obtiene la lista de comunicaciones que cumplen los parámetros dados
     * 
     * @param organizacion Identificador de la organización del usuario que creó la comunicación
     * @param ejercicio Ejercicio en el que se creó la comunicación
     * @param numExpediente Número de expediente asociado a la comunicación
     * @param conexion Coenxión de BBDD
     * @return Lista de objetos  ComunicacionVO que cumplen las condiciones
     */
   public List<ComunicacionVO> getComunicaciones(String organizacion, Integer ejercicio,String numExpediente,String expHistorico,Connection conexion){
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicaciones(): INICIO  -> Ejercicio " + ejercicio + " Expediente " + numExpediente + " Organizacion " + organizacion);}
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");                                                                        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        List<ComunicacionVO> salida = new ArrayList<ComunicacionVO>();
        try{
            //String sql = "Select ID, ASUNTO, TEXTO, TIPO_DOCUMENTO, DOCUMENTO , NOMBRE, FECHA, NUM_REGISTRO, ORIGEN_REGISTRO, XML_COMUNICACION, FIRMA, PLATAFORMA_FIRMA, COD_ORGANIZACION, EJERCICIO, NUM_EXPEDIENTE, LEIDA from COMUNICACION where COD_ORGANIZACION = ? and EJERCICIO =? and NUM_EXPEDIENTE = ?";
            String sql;
            if ("true".equals(expHistorico))
                sql = "SELECT ID,ASUNTO,TEXTO,TIPO_DOCUMENTO,DOCUMENTO,NOMBRE,FECHA,NUM_REGISTRO,EJERCICIO,NUM_EXPEDIENTE,LEIDA " + 
                             "FROM HIST_COMUNICACION WHERE COD_ORGANIZACION=? AND EJERCICIO=? AND NUM_EXPEDIENTE=?";
            else
                sql = "SELECT ID,ASUNTO,TEXTO,TIPO_DOCUMENTO,DOCUMENTO,NOMBRE,FECHA,NUM_REGISTRO,EJERCICIO,NUM_EXPEDIENTE,LEIDA " + 
                             "FROM COMUNICACION WHERE COD_ORGANIZACION=? AND EJERCICIO=? AND NUM_EXPEDIENTE=?";

            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicaciones(): -> SQL " + sql);}
            ps = conexion.prepareStatement(sql);
                    
            ps.setLong(1, Long.parseLong(organizacion));
            ps.setInt(2,ejercicio);
            ps.setString(3, numExpediente);
  
            rs = ps.executeQuery();            
            while(rs.next()){
                ComunicacionVO comunicacion = new ComunicacionVO();
                comunicacion.setId(rs.getLong("ID"));
                //comunicacion.setAsunto(StringEscapeUtils.escapeJavaScript(rs.getString("ASUNTO")));
                comunicacion.setAsunto(rs.getString("ASUNTO"));
                comunicacion.setTexto(rs.getString("TEXTO"));
                comunicacion.setTipoDocumento(rs.getInt("TIPO_DOCUMENTO"));
                comunicacion.setDocumento(rs.getString("DOCUMENTO"));
                comunicacion.setNombre(rs.getString("NOMBRE"));

                Calendar cal=Calendar.getInstance();
                cal.setTime(rs.getDate("FECHA"));
                comunicacion.setFecha(cal);
                if(comunicacion.getFecha()!=null){
                    comunicacion.setFechaAsString(sdf.format(comunicacion.getFecha().getTime()));                    
                }
                
                comunicacion.setNumeroRegistro(rs.getString("NUM_REGISTRO"));                
                //comunicacion.setOrigenRegistro(rs.getString("ORIGEN_REGISTRO"));                                
                //comunicacion.setFirma(new String(rs.getString("FIRMA")));                
                comunicacion.setEjercicio(rs.getInt("EJERCICIO"));                                
                comunicacion.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
                comunicacion.setLeida(rs.getInt("LEIDA")==1);
                salida.add(comunicacion);
            }

        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()){m_Log.error(this.getClass().getName() + ".getComunicaciones(): -> Error " + e.getMessage());}
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicaciones(): -> Error " + e.getMessage());}
            }
        }
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicaciones(): -> Devolviendo " +  salida.size() + "adjuntos");}        
        return salida;
    }
    
    
    /** Método que obtiene los datos de una comunicación a partir de su identificador único 
     * 
     * @param idComunicacion Identificador único de la comunicación
     * @param conexion Datos de la conexión a BBDD
     * @return Datos de la comunicación buscada
     */
    public ComunicacionVO getComunicacion(Long idComunicacion, Connection conexion){
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): INICIO  -> ID " + idComunicacion );}
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ComunicacionVO salida = null;
        try{
            String sql = "Select ID, ASUNTO, TEXTO, TIPO_DOCUMENTO, DOCUMENTO , NOMBRE, FECHA, NUM_REGISTRO, ORIGEN_REGISTRO, XML_COMUNICACION, FIRMA, PLATAFORMA_FIRMA, COD_ORGANIZACION, EJERCICIO, NUM_EXPEDIENTE, LEIDA from COMUNICACION where ID = ? ";

            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> SQL " + sql);}
            ps = conexion.prepareStatement(sql);
                    
            ps.setLong(1, idComunicacion);
 
  
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Ejecutando query....");}        
            rs = ps.executeQuery();            

            while(rs.next()){
                salida = new ComunicacionVO();
                                
                salida.setXmlComunicacion(rs.getString("XML_COMUNICACION"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> XML " + salida.getXmlComunicacion());}        
              
                
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Seteando valores del adjunto ");}        
                
                salida.setId(rs.getLong("ID"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> ID " + salida.getId());}        
                
                //salida.setAsunto(rs.getString("ASUNTO"));
                salida.setAsunto(StringEscapeUtils.unescapeHtml(rs.getString("ASUNTO")));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> ASUNTO " + salida.getAsunto());}        
                
                //salida.setTexto(rs.getString("TEXTO"));
                salida.setTexto(StringEscapeUtils.unescapeHtml(rs.getString("TEXTO")));
                salida.setTipoDocumento(rs.getInt("TIPO_DOCUMENTO"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> TIPO DOCUMENTO " + salida.getTipoDocumento());}        

                salida.setDocumento(rs.getString("DOCUMENTO"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> DOCUMENTO " + salida.getDocumento());}        
                
                salida.setNombre(rs.getString("NOMBRE"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> NOMBRE " + salida.getNombre());}        

                Calendar cal=Calendar.getInstance();
                cal.setTime(rs.getDate("FECHA"));
                salida.setFecha(cal);
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> FECHA " + salida.getFecha());}        

                
                salida.setNumeroRegistro(rs.getString("NUM_REGISTRO"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Num Registro " + salida.getNumeroRegistro());}        

                salida.setOrigenRegistro(rs.getString("ORIGEN_REGISTRO"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> ORigen Registro " + salida.getOrigenRegistro());}        
                
                salida.setFirma(rs.getString("FIRMA"));
                
                salida.setEjercicio(rs.getInt("EJERCICIO"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> EJERCICIO " + salida.getEjercicio());}        
                
                salida.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> NUM EXPEDIENTE " + salida.getNumeroExpediente());}                        
                
                salida.setLeida(rs.getInt("LEIDA")==1);
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> LEIDA " + salida.getLeida());}        
                
                
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Adjunto añadido a a lista de salida");}        
    
               
            }
            

        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Error " + e.getMessage());}
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Error " + e.getMessage());}
            }
        }
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Devolviendo la comunicacion");}        
        return salida;
    }
     
     /** Método que marca una comunicación como leida
      * 
      * @param idComunicacion Identificador único de la comunicación
      * @param conexion Conexión a BBDD
      * @return Indicador de si se ha ejecutado correctamente la operación
      */
     public Boolean marcarComunicacionLeida(Long idComunicacion, Connection conexion){
         if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".marcarComunicacionLeida(): INICIO  -> ID " + idComunicacion );}
         
         PreparedStatement ps = null;
         ResultSet rs = null;
         Boolean exito = null;
         try{
            String sql = "UPDATE COMUNICACION SET LEIDA=1 where ID = ? and LEIDA =0";

            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> SQL " + sql);}
            ps = conexion.prepareStatement(sql);
                    
            ps.setLong(1, idComunicacion);
  
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Ejecutando query....");}            

            int rowsUpdated = ps.executeUpdate();
            if(rowsUpdated==1) exito = true;
            

        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Error " + e.getMessage());}
            exito  = false;
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Error " + e.getMessage());}
                 exito  = false;
            }
        }
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion(): -> Devolviendo la comunicacion");}        
        return exito;
     }
}
