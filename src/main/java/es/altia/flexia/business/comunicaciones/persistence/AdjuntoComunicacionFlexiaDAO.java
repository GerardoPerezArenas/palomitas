/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.business.comunicaciones.persistence;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.business.comunicaciones.vo.AdjuntoComunicacionVO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Clase que implementa los métodos de acceso a datos de los adjuntos de las comunicaciones
 *
 * @author Ricardo
 */
public class AdjuntoComunicacionFlexiaDAO {
    private static AdjuntoComunicacionFlexiaDAO instance =	null;
    protected static Log m_Log =
            LogFactory.getLog(AdjuntoComunicacionFlexiaDAO.class.getName());




    /** Constructor
     * 
     */
    protected AdjuntoComunicacionFlexiaDAO() {
             

	}

    /** Método que obtiene una nueva instancia del DAO si no ha sido inicializado previamente
     * 
     * @return
     */
    public static AdjuntoComunicacionFlexiaDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (AdjuntoComunicacionFlexiaDAO.class) {
            if (instance == null) {
                instance = new AdjuntoComunicacionFlexiaDAO();
            }

        }
        return instance;
    }
    
 
    /** Método que obtiene los adjuntos de una comunicación dada
     * 
     * @param idComunicacion Identificador único de la comunicación para la que se recuperan los adjuntos
     * @param conexion Datos de conexión a BBDD
     * @return Lista de objectos AdjuntoComunicacionVO con los datos de los adjuntos
     */
    public List<AdjuntoComunicacionVO> getListaAdjuntos(Long idComunicacion, Connection conexion){
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): INICIO  -> Comunicacion " + idComunicacion);}
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        List<AdjuntoComunicacionVO> salida = new ArrayList<AdjuntoComunicacionVO>();
        try{
            String sql = "Select ID, ID_COMUNICACION, NOMBRE, TIPO_MIME, FECHA, CONTENIDO, FIRMA, PLATAFORMA_FIRMA from ADJUNTO_COMUNICACION where ID_COMUNICACION = ?";

            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> SQL " + sql);}
            ps = conexion.prepareStatement(sql);
                    
            ps.setLong(1, idComunicacion);
  
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Ejecutando query....");}        
            rs = ps.executeQuery();            

            while(rs.next()){
                AdjuntoComunicacionVO adjunto = new AdjuntoComunicacionVO();
                
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Seteando valores del adjunto ");}        
                
                adjunto.setId(rs.getLong("ID"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> ID " + adjunto.getId());}        
                
                adjunto.setIdComunicacion(rs.getLong("ID_COMUNICACION"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> ID  COMUNICACION " + adjunto.getIdComunicacion());}        
                
                adjunto.setNombre(rs.getString("NOMBRE"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> NOMBRE " + adjunto.getNombre());}        
                
                adjunto.setTipoMime(rs.getString("TIPO_MIME"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Tipo " + adjunto.getTipoMime());}        
                
                Calendar cal=Calendar.getInstance();
                cal.setTime(rs.getDate("FECHA"));
                adjunto.setFecha(cal);
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Fecha " + adjunto.getFecha());}        

                adjunto.setFirma(rs.getString("FIRMA"));
                adjunto.setPlataformaFirma(rs.getString("PLATAFORMA_FIRMA"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Platforma " + adjunto.getPlataformaFirma());}        
                
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Adjunto añadido a a lista de salida");}        
                salida.add(adjunto);
               
            }
            

        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Error " + e.getMessage());}
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Error " + e.getMessage());}
            }
        }
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getListaAdjuntos(): -> Devolviendo " +  salida.size() + "adjuntos");}        
        return salida;
    }
    
    /** Método que recupera los datos de un adjunto a partir de su identificador único
     * 
     * @param idComunicacion Identificador único de la comunicación
     * @param idAdjunto Identificador único del adjunto
     * @param conexion Datos de conexión a BBDD
     * @return Objeto  AdjuntoComunicacionVO con los datos del adjunto buscado
     */
    public AdjuntoComunicacionVO getAdjunto(Long idComunicacion,Long idAdjunto,Connection conexion){
        if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): INICIO  -> Comunicacion " + idComunicacion + " Adjunto " + idAdjunto);}
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        AdjuntoComunicacionVO salida = null;
        try{
            String sql = "Select ID, ID_COMUNICACION, NOMBRE, TIPO_MIME, FECHA, CONTENIDO, FIRMA, PLATAFORMA_FIRMA from ADJUNTO_COMUNICACION where ID_COMUNICACION = ? and ID = ?";

            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> SQL " + sql);}
            ps = conexion.prepareStatement(sql);
                    
            ps.setLong(1, idComunicacion);
            ps.setLong(2, idAdjunto);
  
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Ejecutando query....");}        
            rs = ps.executeQuery();            

            while(rs.next()){
                salida = new AdjuntoComunicacionVO();
                
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Seteando valores del adjunto ");}        
                
                salida.setId(rs.getLong("ID"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> ID " + salida.getId());}        
                
                salida.setIdComunicacion(rs.getLong("ID_COMUNICACION"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> ID  COMUNICACION " + salida.getIdComunicacion());}        
                
                salida.setNombre(rs.getString("NOMBRE"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> NOMBRE " + salida.getNombre());}        
                
                salida.setTipoMime(rs.getString("TIPO_MIME"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Tipo " + salida.getTipoMime());}        
                
                Calendar cal=Calendar.getInstance();
                cal.setTime(rs.getDate("FECHA"));
                salida.setFecha(cal);
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Fecha " + salida.getFecha());}        

                salida.setContenido(new String(rs.getBytes("CONTENIDO")));
                salida.setFirma(rs.getString("FIRMA"));
                salida.setPlataformaFirma(rs.getString("PLATAFORMA_FIRMA"));
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Platforma " + salida.getPlataformaFirma());}        
                
                        
            }
            

        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Error " + e.getMessage());}
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Error " + e.getMessage());}
            }
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> Devolviendo el adjunto ");}        
            return salida;
        }
        
        
    }
}
