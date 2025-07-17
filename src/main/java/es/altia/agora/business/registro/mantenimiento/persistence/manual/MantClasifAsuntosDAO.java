package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MantClasifAsuntosDAO {
    
  //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
  //Para informacion de logs.
    protected static Log m_Log =
          LogFactory.getLog(MantClasifAsuntosDAO.class.getName());
  //La instancia de esta clase
    private static MantClasifAsuntosDAO instance = null;

     
    
    protected MantClasifAsuntosDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

       
    }
    
    public static MantClasifAsuntosDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(MantClasifAsuntosDAO.class){
                if (instance == null)
                    instance = new MantClasifAsuntosDAO();
                }
        }
        return instance;
    }

    /**
     * Carga todas las clasificaciones de asunto existentes
     * para la unidad de Registro, cuyo código pasamos
     * como parámetro.
     *@return Un vector de MantClasifAsuntosValueObject 
     */
    public ArrayList<MantClasifAsuntosValueObject> cargarClasifAsuntos(int unidadRegistro,String[] params) {
        
        ArrayList<MantClasifAsuntosValueObject> clasifAsuntos = new ArrayList<MantClasifAsuntosValueObject>();        
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement st = null;
        
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->cargarClasifAsuntos.BEGIN, la unidad de Registro es: "+ unidadRegistro);
        try {       
          abd = new AdaptadorSQLBD(params);        
          conexion = abd.getConnection();
          // Creamos la select
          String sql;

       if(unidadRegistro==-1){ //Si estamos desde el administrador, sólo se podrá dar de alta asuntos con asunto por defecto (que lo tendrán todas las oficinas)
               sql=" SELECT CODIGO, DESCRIPCION " +
              " FROM R_CLASIFICACION_ASUNTO " +
              " WHERE CODIGO=0"; 
           
          }else{
          sql=" SELECT CODIGO, DESCRIPCION " +
              " FROM R_CLASIFICACION_ASUNTO " +
              " WHERE UNIDAD_REGISTRO = " + unidadRegistro + 
              " ORDER BY CODIGO";
          }
         
          if(m_Log.isDebugEnabled()) m_Log.debug("cargarAsuntos. Sql"+sql);
          st = conexion.createStatement();
          rs = st.executeQuery(sql);
         

          while(rs.next()){
              MantClasifAsuntosValueObject mantClasifAsuntosVO = new MantClasifAsuntosValueObject(); 
              mantClasifAsuntosVO.setCodigo(rs.getInt("CODIGO"));
              mantClasifAsuntosVO.setDescripcion(rs.getString("DESCRIPCION"));
              mantClasifAsuntosVO.setUnidadRegistro(unidadRegistro);
              clasifAsuntos.add(mantClasifAsuntosVO);
          }
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->cargarClasifAsuntos.END");
        return clasifAsuntos;
    }
    
    
    /*
     * Graba una nueva clasificación de Asunto en la BD.  
     */
    public void grabarAlta(MantClasifAsuntosValueObject clasifAsunto, String[] params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement st = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->grabarAlta. BEGIN");
       if (clasifAsunto!=null){
        

            try {       
                abd = new AdaptadorSQLBD(params);        
                conexion = abd.getConnection();
                abd.inicioTransaccion(conexion);

                int codigo=-1;
                String descripcion="";
                int unidadRegistro=-1;
                //Comprobamos que los valores no nos llegan nulos
                if (clasifAsunto.getCodigo()!=null){
                    codigo=clasifAsunto.getCodigo();
                    if(m_Log.isDebugEnabled()) m_Log.debug("Codigo"+ codigo);
                }
                 if (clasifAsunto.getDescripcion()!=null){
                    descripcion=clasifAsunto.getDescripcion();
                    if(m_Log.isDebugEnabled()) m_Log.debug("Descipcion"+ descripcion);
                }
                if (clasifAsunto.getUnidadRegistro()!=null){
                    unidadRegistro=clasifAsunto.getUnidadRegistro();
                    if(m_Log.isDebugEnabled()) m_Log.debug("Unidad de Registro"+ unidadRegistro);
                } 
                 
                // Creamos el insert para la clasificacion del asunto
                String sql;
                sql = " INSERT INTO R_CLASIFICACION_ASUNTO ( CODIGO, DESCRIPCION, UNIDAD_REGISTRO ) " +
                       " VALUES (" +codigo + ", '" + descripcion + "' ,"+ unidadRegistro+" )";

                if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO.grabarAlta.Sql"+sql);

               st = conexion.createStatement();
               st.executeUpdate(sql);

                abd.finTransaccion(conexion);

            }  catch (Exception e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                try{
                    abd.rollBack(conexion);
                }catch (Exception x){
                    x.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(x.getMessage());
                }   
            } finally {
                try{
                    abd.devolverConexion(conexion);
                }catch (Exception e){
                    e.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }
        }
  
    }//clasifAsunto es nulo  
       else{
       if(m_Log.isDebugEnabled()) m_Log.debug("El objeto viene a Nulo, no grabamos nada en BD");  
       }
       
  
    if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO.grabarAlta.END");   
    }
  
    /**
     * Modifica una clasificacion de asunto en la BD
     */
    public void grabarModificacion(String descripcion, Integer codigo, Integer unidadRegistro,  String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->grabarModificacion.BEGIN");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement st = null;
      
        try {       
            abd = new AdaptadorSQLBD(params);        
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
       
            String sql;
            sql = " UPDATE R_CLASIFICACION_ASUNTO SET " + 
                  " DESCRIPCION= '" + descripcion +"'" +
                  " WHERE CODIGO = " + codigo+
                  " AND UNIDAD_REGISTRO = "+unidadRegistro;  
            
            
            if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->grabarModificacion.CONSULTA:"+sql);
            
              st = conexion.createStatement();
              st.executeUpdate(sql);
             abd.finTransaccion(conexion);
            
        }  catch (Exception e) {            
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            try{
                abd.rollBack(conexion);
            }catch (Exception x){
                x.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(x.getMessage());
            }           
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
    }
    

     /**
     * Carga  la clasificacion que tiene como codigo, el pasado como parametro
     * y como unidadRegistro, tb  la pasada como parámetro
     * @return Un MantAsuntosValueObject que contendrá toda la información de la clasificacion 
     */
    public MantClasifAsuntosValueObject cargarClasifAsunto(Integer codigo, Integer unidadRegistro, String[] params) {
                        
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement st = null;
        MantClasifAsuntosValueObject clasifAsuntoVO=new MantClasifAsuntosValueObject();
      
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->cargarClasifAsunto.BEGIN, el codigo pasado es: "+codigo);
        try {       
          abd = new AdaptadorSQLBD(params);        
          conexion = abd.getConnection();
          
          // Creamos la select para el asunto
          String sql;
          sql = " SELECT DESCRIPCION, CODIGO, UNIDAD_REGISTRO " +
                " FROM R_CLASIFICACION_ASUNTO " +
                " WHERE CODIGO =  " +codigo +
                " AND UNIDAD_REGISTRO= "+ unidadRegistro;
             
          if(m_Log.isDebugEnabled()) m_Log.debug("CargarClasifAsunto: La consulta es:"+sql);
          
          st = conexion.createStatement();
          rs = st.executeQuery(sql);
         
          if (rs.next()) {  
              
              String descripcion=rs.getString("DESCRIPCION");
              if(m_Log.isDebugEnabled()) m_Log.debug("Descripcion recuperada de la BD:"+descripcion );
              Integer codigoBD= rs.getInt("CODIGO");
              if(m_Log.isDebugEnabled()) m_Log.debug("Codigo recuperado de la BD:"+codigoBD );
              Integer unidadRegistroBD=rs.getInt("UNIDAD_REGISTRO");
              if(m_Log.isDebugEnabled()) m_Log.debug("Unidad de registro recuperado de la BD: "+unidadRegistroBD);
              clasifAsuntoVO.setDescripcion(descripcion);
              clasifAsuntoVO.setCodigo(codigoBD);
              clasifAsuntoVO.setUnidadRegistro(unidadRegistroBD);
           
          } else{
          
              if(m_Log.isDebugEnabled()) m_Log.debug("NO devuelvo resultados!!");
          }
          rs.close();
         
       
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
         if(m_Log.isDebugEnabled()) m_Log.debug("CargarClasifAsunto:END");
        return clasifAsuntoVO;
    }
    
    
     /**
     * Borra fisicamente de la BD  la clasificacion que tiene como codigo y unidad de Registro
     * los que van el MantClasifAsuntosValueObject
     * @return Un MantClasifAsuntosValueObject que contendrá toda la información de la clasificacion 
     */
    
 
    public  void borrarClasifAsunto(int codigo, int unidadRegistro, String [] params){ 
      if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->borrarClasifAsunto.BEGIN");
        
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement st = null;
        
      
        if(m_Log.isDebugEnabled()) m_Log.debug("Queremos borrar la clasificacion con codigo:"+codigo);
        if(m_Log.isDebugEnabled()) m_Log.debug("Queremos borrar la clasificacion con unidad De Registro:"+unidadRegistro);
      
        
      try {
          
          String sql;
           abd = new AdaptadorSQLBD(params);        
          conexion = abd.getConnection();
          
          
          
        sql = " DELETE FROM R_CLASIFICACION_ASUNTO " +
              " WHERE CODIGO= " + codigo +
              " AND UNIDAD_REGISTRO= " + unidadRegistro;
          
          
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO. borrarClasificacion, la consulta es:"+sql);            
         st = conexion.createStatement();
         st.executeUpdate(sql);
         
      } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }


}
    /**
     * Comprueba si existe algún tipo de asunto que tenga la clasificacion, con el código
     * pasado como parámetro, si es así devuelve TRUE, en caso contrario devuelve FALSE.
     * @return Boolean,
     */
    
    
 public Boolean consultarExisteAsuntoConClasificacion(Integer codigo, Integer unidadRegistro, String[] params) {
                        
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement st = null;
        //si resultado es true, no dejamos borrar, así que es lo menos malo
        Boolean resultado=true; 
        MantClasifAsuntosValueObject clasifAsuntoVO=new MantClasifAsuntosValueObject();
      
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->consultarExisteAsuntoConClasificacion, para el codigo: "+codigo);
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosDAO->consultarExisteAsuntoConClasificacion, para la unidadRegistro: "+unidadRegistro);
        try {       
          abd = new AdaptadorSQLBD(params);        
          conexion = abd.getConnection();
          
          // Creamos la select para el asunto
          String sql;
          sql = " SELECT CODIGO_CLASIFICACION, UNIDADREGISTRO " +
                " FROM R_TIPOASUNTO " +
                " WHERE CODIGO_CLASIFICACION =  " +codigo +
                " AND UNIDADREGISTRO= "+ unidadRegistro;
             
          if(m_Log.isDebugEnabled()) m_Log.debug("consultarExisteAsuntoConClasificacion: La consulta es:"+sql);
          
          st = conexion.createStatement();
          rs = st.executeQuery(sql);
         
          if (rs.next()) {  
               if(m_Log.isDebugEnabled()) m_Log.debug("Existen tipos de asuntos, con esa clasificacion, devolvemos TRUE !!");
              
               resultado= true;
           
          } else{
               if(m_Log.isDebugEnabled()) m_Log.debug("No existe tipos de asuntos con esa clasificacion, devolvemos FALSE!!");
              
               resultado= false;
          }
          
         rs.close();
       
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
         if(m_Log.isDebugEnabled()) m_Log.debug("consultarExisteAsuntoConClasificacion.END, Se devuelve:"+resultado);
        return resultado;
    }    
    
     
       
    
    
}