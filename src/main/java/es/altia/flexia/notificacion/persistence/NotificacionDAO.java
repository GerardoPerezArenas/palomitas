package es.altia.flexia.notificacion.persistence;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;


import es.altia.flexia.notificacion.plugin.FactoriaPluginNotificacion;
import es.altia.flexia.notificacion.plugin.PluginNotificacion;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import es.altia.flexia.notificacion.vo.*;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.BDException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringEscapeUtils;

public class NotificacionDAO {

    private static NotificacionDAO instance =	null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(NotificacionDAO.class.getName());




     protected NotificacionDAO() {
                m_CommonProperties = ConfigServiceHelper.getConfig("common");
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");


	}

    public static NotificacionDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (NotificacionDAO.class) {
            if (instance == null) {
                instance = new NotificacionDAO();
            }

        }
        return instance;
    }

    //Da de alta una notificación en base de datos. La notificación está asociada a un trámite y ocurrencia del mismo determinada
 public int insertarNotificacion(NotificacionVO notificacion, Connection con) throws TechnicalException {

    int resultado = 0;    
    PreparedStatement ps = null;
    String sql = "";
    ResultSet rs = null;
    int codigo=0;

    try{
      String numeroExpediente=notificacion.getNumExpediente();
      String codProcedimiento=notificacion.getCodigoProcedimiento();
      int ejercicio=notificacion.getEjercicio();
      int codMunicipio=notificacion.getCodigoMunicipio();
      int codTramite=notificacion.getCodigoTramite();
      int ocuTramite=notificacion.getOcurrenciaTramite();
      String actoNotificado=notificacion.getActoNotificado();
      int caducidadNotificacion=notificacion.getCaducidadNotificacion();
      String firma=notificacion.getFirma();
      String textoNotificacion=notificacion.getTextoNotificacion();

      
      ResourceBundle config = ResourceBundle.getBundle("techserver");
      
      if(config.getString("CON.gestor").equalsIgnoreCase(ConstantesDatos.ORACLE)) {
          
          sql = "INSERT INTO NOTIFICACION(CODIGO_NOTIFICACION,NUM_EXPEDIENTE," +
                "COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO,COD_TRAMITE,OCU_TRAMITE,ACTO_NOTIFICADO," +
                "CADUCIDAD_NOTIFICACION,FIRMA,TEXTO_NOTIFICACION)" +
                " VALUES (SEQ_NOTIFICACION.nextval,?,?,?,?,?,?,?,?,'"+firma+"',?)";   
          
      } else if(config.getString("CON.gestor").equalsIgnoreCase(ConstantesDatos.SQLSERVER)) {
          sql = "INSERT INTO NOTIFICACION(NUM_EXPEDIENTE," +
                "COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO,COD_TRAMITE,OCU_TRAMITE,ACTO_NOTIFICADO," +
                "CADUCIDAD_NOTIFICACION,FIRMA,TEXTO_NOTIFICACION)" +
                " VALUES (?,?,?,?,?,?,?,?,'"+firma+"',?)";             
      }

      int i = 1;
      ps = con.prepareStatement(sql);
      ps.setString(i++,numeroExpediente);
      ps.setString(i++,codProcedimiento);
      ps.setInt(i++,ejercicio);
      ps.setInt(i++,codMunicipio);
      ps.setInt(i++,codTramite);
      ps.setInt(i++,ocuTramite);
      ps.setString(i++,actoNotificado);
      ps.setInt(i++,caducidadNotificacion);
      ps.setString(i++,textoNotificacion);
      
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      //resultado = st.executeUpdate(sql);
      resultado = ps.executeUpdate();

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        return -1;
    }finally{
        
        try{
            if (ps!=null) ps.close();            
            
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            return -1;
        }
    }

    if (resultado==0) return -1;
    else return (codigo);

   }

  
   //Almacena la firma de una notificación en base de datos
 public boolean guardarFirma(int codigoNotificacion,String firma,String[] params) throws TechnicalException {

    int resultado = 0;
    AdaptadorSQLBD obd = null;
    Connection con = null;
    
    String sql = "";
    ResultSet rs = null;
    PreparedStatement ps = null;

    try{
      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();


     byte[] bFirma = firma.getBytes();
     


    //if(m_Log.isDebugEnabled()) m_Log.debug(bFirma);

     sql =	"UPDATE NOTIFICACION SET FIRMA=? , FIRMADA=?" +
          " WHERE CODIGO_NOTIFICACION=?";
     if(m_Log.isDebugEnabled()) m_Log.debug(sql);
     if(m_Log.isDebugEnabled()) m_Log.debug(firma);
     if(m_Log.isDebugEnabled()) m_Log.debug(codigoNotificacion);

     int i=1;
     ps = con.prepareStatement(sql);

  

     //java.io.InputStream st=new java.io.ByteArrayInputStream(bFirma);
     if(bFirma!=null && bFirma.length>0){
         java.io.InputStream st = new java.io.ByteArrayInputStream(bFirma);
         ps.setBinaryStream(i++, st, bFirma.length);
         ps.setString(i++, "F");
         
     }else{
         ps.setNull(i++, java.sql.Types.LONGVARBINARY);
         ps.setString(i++, "");
     }
         
      
     

      //ps.setString(i++,firma);
      ps.setInt(i++,codigoNotificacion);

      resultado = ps.executeUpdate();

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        return false;
    }finally{
        try{
            if (ps!=null) ps.close();
            obd.devolverConexion(con);
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            return false;
        }
    }

    if (resultado==0) return false;
    else return true;

   }

 //Almacena la firma de una notificación en base de datos
  public NotificacionVO getNotificacion(NotificacionVO notificacion, Connection con) throws TechnicalException {

        NotificacionVO notificacionVORetorno = new NotificacionVO();
        ResultSet rs = null;
        Statement st = null;
        String parteWhere;

        try{
            
            String numeroExpediente=notificacion.getNumExpediente();
            String codProcedimiento=notificacion.getCodigoProcedimiento();
            int ejercicio=notificacion.getEjercicio();
            int codMunicipio=notificacion.getCodigoMunicipio();
            int codTramite=notificacion.getCodigoTramite();
            int ocuTramite=notificacion.getOcurrenciaTramite();
            int caducidadNotificacion=notificacion.getCaducidadNotificacion();
           
            parteWhere=" NUM_EXPEDIENTE='"+numeroExpediente+"' AND COD_PROCEDIMIENTO='"+codProcedimiento+"' AND EJERCICIO="+ejercicio+" AND "+
                        "COD_MUNICIPIO="+codMunicipio+" AND COD_TRAMITE="+codTramite+" AND OCU_TRAMITE="+ocuTramite;
           
             String sql = "SELECT CODIGO_NOTIFICACION,NUM_EXPEDIENTE,COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO," +
                          "COD_TRAMITE,OCU_TRAMITE,ACTO_NOTIFICADO,CADUCIDAD_NOTIFICACION,FIRMA,TEXTO_NOTIFICACION,FIRMADA" +
                          " FROM NOTIFICACION WHERE " + parteWhere+" ORDER BY CODIGO_NOTIFICACION DESC";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            if(rs.next()){ //Puede tener varias notificaciones pero vamos a devolver la ultima

                notificacionVORetorno=notificacion;
                notificacionVORetorno.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notificacionVORetorno.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notificacionVORetorno.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notificacionVORetorno.setEjercicio(rs.getInt("EJERCICIO"));
                notificacionVORetorno.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notificacionVORetorno.setCodigoTramite(rs.getInt("COD_TRAMITE"));
                notificacionVORetorno.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                notificacionVORetorno.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notificacionVORetorno.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                //notificacionVORetorno.setFirma(rs.getString("FIRMA"));
                if(rs.getInt("CADUCIDAD_NOTIFICACION")==0) notificacionVORetorno.setCaducidadNotificacion(caducidadNotificacion);

               
                   byte[] contenido = null;
                    // Se lee el contenido binario del documento
                    java.io.InputStream stream = rs.getBinaryStream("FIRMA");
                    java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                    int c;
                    if (stream != null) {
                         while ((c = stream.read()) != -1) {
                             ot.write(c);
                         }
                    }
                    ot.flush();
                    contenido = ot.toByteArray();
                    ot.close();

                    String value = new String(contenido);

                    notificacionVORetorno.setFirma(value);
                   
                    String textoNotificacion = rs.getString("TEXTO_NOTIFICACION");
                    
                    textoNotificacion = StringEscapeUtils.escapeJavaScript(textoNotificacion);
                            
                    notificacionVORetorno.setTextoNotificacion(textoNotificacion);
                //notificacionVORetorno.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notificacionVORetorno.setEstadoNotificacion(rs.getString("FIRMADA"));

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return notificacionVORetorno;
    }



  public String getTipoNotificacion(NotificacionVO notificacion,String[] params) throws TechnicalException {

        AdaptadorSQLBD obd = null;
        Connection con = null;
        String tipoNotificacion="";
        ResultSet rs = null;
        Statement st = null;
        String parteWhere;


        try{

            obd = new AdaptadorSQLBD(params);
            con = obd.getConnection();

            String codProcedimiento=notificacion.getCodigoProcedimiento();
            int codMunicipio=notificacion.getCodigoMunicipio();
            int codTramite=notificacion.getCodigoTramite();

             String sql = "SELECT TRA_COD_TIPO_NOTIFICACION FROM E_TRA WHERE TRA_MUN="+codMunicipio+" AND TRA_PRO='"+codProcedimiento+"' AND TRA_COD="+codTramite;


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){

                tipoNotificacion=rs.getString("TRA_COD_TIPO_NOTIFICACION");


            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                obd.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return tipoNotificacion;
    }



   public int getUsuarioFirmanteNotificacion(NotificacionVO notificacion,String[] params) throws TechnicalException {

        AdaptadorSQLBD obd = null;
        Connection con = null;
        String tipoNotificacion="";
        String usuarioNotificacion="";
        String codUsuarioNotificacion="";
        int codUsu=-1;
        ResultSet rs = null;
        Statement st = null;
        String parteWhere;


        try{

            obd = new AdaptadorSQLBD(params);
            con = obd.getConnection();

            String codProcedimiento=notificacion.getCodigoProcedimiento();
            int codMunicipio=notificacion.getCodigoMunicipio();
            int codTramite=notificacion.getCodigoTramite();

             String sql = "SELECT TRA_NOTIFICACION_ELECTRONICA, TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA FROM E_TRA WHERE TRA_MUN="+codMunicipio+" AND TRA_PRO='"+codProcedimiento+"' AND TRA_COD="+codTramite;


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){

                tipoNotificacion=rs.getString("TRA_NOTIFICACION_ELECTRONICA");
                usuarioNotificacion=rs.getString("TRA_TIPO_USUARIO_FIRMA");
                codUsuarioNotificacion=rs.getString("TRA_OTRO_COD_USUARIO_FIRMA");
            }

            if((usuarioNotificacion!=null)&&(!("".equals(usuarioNotificacion))))
            {
                if("0".equals(usuarioNotificacion)) codUsu=0;
                else 
                {
                    if((codUsuarioNotificacion!=null)&&(!("".equals(codUsuarioNotificacion)))) codUsu=Integer.parseInt(codUsuarioNotificacion);
                    else codUsu=-1;
                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                obd.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return codUsu;
    }



   public boolean guardarEstadoNotificacionEnviada(NotificacionVO notificacion,String[] params) throws TechnicalException {

    int resultado = 0;
    AdaptadorSQLBD obd = null;
    Connection con = null;

    String sql = "";
    ResultSet rs = null;
    PreparedStatement ps = null;

    try{
      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();


     int codigoNotificacion=-1;

     codigoNotificacion=notificacion.getCodigoNotificacion();


    //if(m_Log.isDebugEnabled()) m_Log.debug(bFirma);

     sql =	"UPDATE NOTIFICACION SET FIRMADA=?, FECHA_ENVIO=?, REGISTRO_RT=? " +
          " WHERE CODIGO_NOTIFICACION=?";
     if(m_Log.isDebugEnabled()) m_Log.debug(sql);


     int i=1;
     ps = con.prepareStatement(sql);

     ps.setString(i++, "E");
     ps.setTimestamp(i++, DateOperations.toTimestamp(Calendar.getInstance()));
     ps.setString(i++,notificacion.getNumeroRegistroTelematico());
     ps.setInt(i++,codigoNotificacion);
     resultado = ps.executeUpdate();

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        return false;
    }finally{
        try{
            if (ps!=null) ps.close();
            obd.devolverConexion(con);
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            return false;
        }
    }

    if (resultado==0) return false;
    else return true;

   }


 /**
  * Actualiza la información de una notificación
  * @param notificacion: NotificacionVO
  * @param con: Connection 
  * @return Un boolean
  */
 public boolean updateNotificacion(NotificacionVO notificacion, Connection con) throws TechnicalException {

    boolean exito = false;
    Statement st = null;
    String sql = "";    
    
    try{
      
      String numeroExpediente=notificacion.getNumExpediente();
      String codProcedimiento=notificacion.getCodigoProcedimiento();
      int ejercicio=notificacion.getEjercicio();
      int codMunicipio=notificacion.getCodigoMunicipio();
      int codTramite=notificacion.getCodigoTramite();
      int ocuTramite=notificacion.getOcurrenciaTramite();
      String actoNotificado=notificacion.getActoNotificado();
      int caducidadNotificacion=notificacion.getCaducidadNotificacion();     
      String textoNotificacion=notificacion.getTextoNotificacion();
      int codNotificacion = notificacion.getCodigoNotificacion();

      sql = "UPDATE NOTIFICACION SET COD_PROCEDIMIENTO='" + codProcedimiento + "',EJERCICIO=" + ejercicio + ",COD_MUNICIPIO=" + codMunicipio + 
             ",COD_TRAMITE=" + codTramite + ",OCU_TRAMITE=" + ocuTramite + ",ACTO_NOTIFICADO='" + actoNotificado + "', " + 
             "CADUCIDAD_NOTIFICACION=" + caducidadNotificacion + ",TEXTO_NOTIFICACION='" + textoNotificacion + "' " + 
             "WHERE CODIGO_NOTIFICACION=" + codNotificacion + " AND NUM_EXPEDIENTE='" + numeroExpediente + "'";

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      st = con.createStatement();
      int resultado = st.executeUpdate(sql);
      m_Log.debug("NotificacionDAO.updateNotificacion notificación actualizadas: " + resultado);
      if(resultado>=1) exito = true;      

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        exito = false;
    }finally{
        try{
            if (st!=null) st.close();            
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            
        }
    }

    return exito;
   }
   
   

 
 
    private int getMaxCodNotificacion(Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        int codigo = 0;
        try{
            
            String sql = "SELECT MAX(CODIGO_NOTIFICACION) FROM NOTIFICACION";
            st = con.createStatement();            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            rs = st.executeQuery(sql);

            codigo = 0;
            while (rs.next()) {
                codigo = rs.getInt(1);
            }
            codigo++;
            
            st.close();
            rs.close();
        }catch(SQLException e){
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error(e.getMessage());
            }                
        }
        
        return codigo;
        
        
    }
 
 
    
 /**
  * Crea una notificacion electrónica por dfecto en el caso de que no exista
  * @param notificacion: Objeto de tipo NotificacionVO
  * 
  */
  public void crearNotificacionPorDefecto(NotificacionVO notificacion, Connection con) throws TechnicalException {
        
        ResultSet rs = null;
        Statement st = null;
        PreparedStatement ps = null;
        
        try{
            
            String numeroExpediente=notificacion.getNumExpediente();
            String codProcedimiento=notificacion.getCodigoProcedimiento();
            int ejercicio=notificacion.getEjercicio();
            int codMunicipio=notificacion.getCodigoMunicipio();
            int codTramite=notificacion.getCodigoTramite();
            int ocuTramite=notificacion.getOcurrenciaTramite();
           
            String sql = "SELECT FIRMADA,CODIGO_NOTIFICACION FROM NOTIFICACION ";
            sql = sql + " WHERE NUM_EXPEDIENTE='"+numeroExpediente+"' AND COD_PROCEDIMIENTO='"+codProcedimiento+"' AND EJERCICIO="+ejercicio+" AND "+
                        "COD_MUNICIPIO="+codMunicipio+" AND COD_TRAMITE="+codTramite+" AND OCU_TRAMITE="+ocuTramite + " ORDER BY CODIGO_NOTIFICACION DESC";
            m_Log.debug(sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = -1;
            String estado = null;
            String codNotif = null;
            while(rs.next()){
               estado = rs.getString("FIRMADA");
               codNotif = rs.getString("CODIGO_NOTIFICACION");
               break;
            }
            
            st.close();
            rs.close();

            m_Log.debug("CODIGO_NOTIFICACION: " + codNotif + ",estado: " + estado);
            
            boolean crear = false;
            if(codNotif!=null && !"".equals(codNotif) && estado!=null && "E".equalsIgnoreCase(estado)) // Si hay una notificación enviada para la ocurrencia de trámite
                crear = true;
            else
            if(codNotif==null && estado==null) // Si no existe una notificación para la ocurrencia del trámite, se crea
               crear = true;

            if(crear){                
                int codigo = this.getMaxCodNotificacion(con);       
                
                ResourceBundle config = ResourceBundle.getBundle("techserver");
                String gestorBD = config.getString("CON.gestor");
                
                
                if(gestorBD.equalsIgnoreCase(ConstantesDatos.ORACLE)) { 
                    // No existe una notificación para la ocurrencia del trámite que pertenece a un determinado expediente
                    sql = "INSERT INTO NOTIFICACION(CODIGO_NOTIFICACION,NUM_EXPEDIENTE,COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO,COD_TRAMITE,OCU_TRAMITE) " + 
                          "VALUES (SEQ_NOTIFICACION.nextval,?,?,?,?,?,?)";
                } else
                if(gestorBD.equalsIgnoreCase(ConstantesDatos.SQLSERVER)) { 
                    // No existe una notificación para la ocurrencia del trámite que pertenece a un determinado expediente
                    sql = "INSERT INTO NOTIFICACION(NUM_EXPEDIENTE,COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO,COD_TRAMITE,OCU_TRAMITE) " + 
                          "VALUES(?,?,?,?,?,?)";
                }     
                
                
                m_Log.debug(sql);
                
                ps = con.prepareStatement(sql);
                int i=1;
                //ps.setInt(i++,codigo);
                ps.setString(i++,numeroExpediente);
                ps.setString(i++,codProcedimiento);
                ps.setInt(i++,ejercicio);
                ps.setInt(i++,codMunicipio);
                ps.setInt(i++,codTramite);
                ps.setInt(i++,ocuTramite);

                int rowsUpdated = ps.executeUpdate();
                m_Log.debug("filas actualizadas: " + rowsUpdated);
                if(rowsUpdated!=1) throw new TechnicalException("No se ha dado de alta la notificación por defecto");                                             
            }
            

        }catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        }finally{
            try{
                if(ps!=null) ps.close();
                if(st!=null) st.close();
                if(rs!=null) rs.close();                
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }



/**
  * Almacena el xml de la notificación que posteriormente será firmado por el usuario
  * @param codNotificacion: Código de la notificacion
  * @param con: Conexión
  *
  */
  public boolean guardarXMLFirmaNotificacion(int codNotificacion,String firma, Connection con)  {

        boolean exito = false;
        PreparedStatement ps = null;

        try{

            String sql = "UPDATE NOTIFICACION SET XML_NOTIFICACION=? WHERE CODIGO_NOTIFICACION=?";
            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            int i=1;
            ps.setString(i++,firma);
            ps.setInt(i++,codNotificacion);
            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated==1) exito =true;

        }catch(SQLException e){
            e.printStackTrace();
            exito = false;
        }finally{
            try{
                if(ps!=null) ps.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return exito;

    }




  /**
  * Almacena el xml de la notificación que posteriormente será firmado por el usuario
  * @param codNotificacion: Código de la notificacion
  * @param con: Conexión
  *
  */
  public String getXMLFirmaNotificacion(int codNotificacion,Connection con)  {

        String xml = null;
        Statement st = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT XML_NOTIFICACION FROM NOTIFICACION WHERE CODIGO_NOTIFICACION=" + codNotificacion;
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                xml = rs.getString("XML_NOTIFICACION");
            }

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return xml;
    }



   public boolean tieneNotificacionEnviadaTramite(String numExpediente,String codProcedimiento,String codTramite,String ocuTramite,String codMunicipio,String ejercicio,Connection con)  {

        boolean exito =false;
        Statement st = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT COUNT(*) AS NUM FROM NOTIFICACION WHERE NUM_EXPEDIENTE='" + numExpediente + "' AND COD_PROCEDIMIENTO='" + codProcedimiento + "' " +
                         "AND COD_TRAMITE=" + codTramite + " AND OCU_TRAMITE=" + ocuTramite + " AND COD_MUNICIPIO=" + codMunicipio + " AND EJERCICIO=" + ejercicio + " AND FIRMADA='E'";
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            int num = -1;
            while(rs.next()){
                 num = rs.getInt("NUM");
            }

            if(num>=1) exito = true;

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return exito;
    }
 


    public String getCodDepartamentoNotifTramite(int codTramite,String codProcedimiento,int codOrganizacion,Connection con)  {

        String dpto = null;
        Statement st = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT COD_DEPTO_NOTIFICACION FROM E_TRA WHERE TRA_COD=" + codTramite + " AND TRA_PRO='" + codProcedimiento + "' AND TRA_MUN=" + codOrganizacion;
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                 dpto = rs.getString("COD_DEPTO_NOTIFICACION");
            }

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return dpto;
    }

    /**
	 * Recupera la propiedad 'verificar_estado_notificaciones' de notificaciones.properties
	 * de si se debe comprobar el estado de la notificacion en el SNE
	 * @param codOrganizacion
	 * @return 
	 */    
    private boolean verificarEstadoNotificaciones(int codOrganizacion){
        boolean exito = false;
        m_Log.debug(" =============================> NotificacionDAO.verificarEstadoNotificaciones ======================>");
        try{
            ResourceBundle config = ResourceBundle.getBundle("notificaciones");
            String verificarEstado = config.getString(codOrganizacion + "/verificar_estado_notificaciones");
            
            m_Log.debug("========================>Propiedad verificarEstado: " + verificarEstado);
            if(verificarEstado!=null && "SI".equalsIgnoreCase(verificarEstado))
                exito = true;
            
        }catch(Exception e){
            m_Log.debug("Error al recuperar la propiedad: " + e.getMessage());
            e.printStackTrace();
            m_Log.error("Error al recuperar la propiedad [COD_ORGANIZACION]/verificar_estado_notificaciones: " + e.getMessage());
            exito = false;            
        }
        m_Log.debug(" =============================> NotificacionDAO.verificarEstadoNotificaciones devolviendo : " + exito);
        return exito;
    }
	
    /**
	 * Se comprueba el estado de la notificación en el SNE
	 * @param codOrganizacion
	 * @return 
	 */    
    private void comprobarEstadoNotificacionesSNE(int codOrganizacion, NotificacionVO notificacion, boolean expHistorico, TraductorAplicacionBean traductor, Connection con) {
		PluginNotificacion pluginNotificacion = null;
		es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO notSNE = null;
		int estadoNotificacion = -1;

		try {
			pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(codOrganizacion));
			notSNE = pluginNotificacion.consultarNotificacionSNE(Integer.toString(codOrganizacion), notificacion.getNumeroRegistroTelematico());
			estadoNotificacion = notSNE.getEstadoNotificacion();
				//String fechaCaducidad  = notSNE.getFechaCaducidad();

			//m_Log.debug("estadoNotificacion: " + estadoNotificacion + ", fechaCaducidad: " + fechaCaducidad);
			m_Log.debug("estadoNotificacion: " + estadoNotificacion);

			if (estadoNotificacion == 0) {
				notificacion.setEstadoNotificacion("0");

			} else if (estadoNotificacion == 1) {
				notificacion.setEstadoNotificacion("1");

			} else if (estadoNotificacion == 2) {
				notificacion.setEstadoNotificacion("2");

			} else if (estadoNotificacion == 3) {
				notificacion.setEstadoNotificacion("3");

			} else if (estadoNotificacion == 4) {
				notificacion.setEstadoNotificacion("4");

			}

			String fechaAcuseNotificacion = notSNE.getFechaLeidoRechazado();
			m_Log.debug("fechaAcuseNotificacion : " + fechaAcuseNotificacion);
			String horaAcuseNotificacion = notSNE.getHoraLeidoRechazado();
			m_Log.debug("horaAcuseNotificacion : " + horaAcuseNotificacion);

			if (fechaAcuseNotificacion != null && notificacion.getFechaAcuse() == null) {
				notificacion.setFechaAcuseAsString(fechaAcuseNotificacion + " " + horaAcuseNotificacion);
				actualizarFechaAcuseNotificacion(notificacion, expHistorico, con);
			}

			if (estadoNotificacion == 4 || estadoNotificacion == 1) { //notificacion leida y leida_cancelada resultado acuse notificacion A
				notificacion.setResultado("A");
			} else if (estadoNotificacion == 3) {  //notificacion rechazada resultado acuse notificación  R
				notificacion.setResultado("R");
			}

			// si estado distinto de 0 no leida  y  2 caducada se actualiza la tabla
			if (estadoNotificacion != 0 && estadoNotificacion != 2) {
				actualizarResultadoAcuseNotificacion(notificacion, expHistorico, con);
				if ("A".equals(notificacion.getResultado())) {
					notificacion.setResultado(traductor.getDescripcion("etiqAcepNotif"));
				} else if ("R".equals(notificacion.getResultado())) {
					notificacion.setResultado(traductor.getDescripcion("etiqRechNotif"));
				}
			}

		} catch (Exception e) {
			m_Log.debug("Error al recuperar el estado de la notificación del SNE: " + e.getMessage());
			e.printStackTrace();
			notificacion.setEstadoNotificacion("0");  //==> Se indica que está pendiente sino se ha podido leer
		}
	}
    

    /**
     * Recupera las notificaciones que pertenecen a un determinado expediente y que han sido enviados al sistema de notificación de altia (SNE)
     * @param codOrganizacion: Código de la organización
     * @param idioma: Código de idioma
     * @param numExpediente: Nº expediente
     * @param params: parametros de conexión a la BBDD
     * @param pluginPropio: boolean false se realiza consulta incluyendo fechas envio null e incopora nuevas columnas, true fecha envio not null
     * @return ArrayList<NotificacionVO>
     */
    public ArrayList<NotificacionVO> getNotificacionesExpediente(int codOrganizacion,int idioma,String numExpediente,String expHistorico,Connection con, boolean pluginPropio)  {
        ArrayList<NotificacionVO> notificaciones = new ArrayList<NotificacionVO>();        
		//Almacenamos temporalmente las notificaciones en un Array para poder consultar el estado según tengan o no notificaciones individuales
		ArrayList<NotificacionVO> notificacionesAux = new ArrayList<NotificacionVO>(); 
        
        Statement st = null;
		ResultSet rs = null;
		String sql ="";
        
        Boolean expedienteHistorico = false;
        if(expHistorico.equals("true")){
            expedienteHistorico = true;
        }
        try{
            if (expedienteHistorico)
                sql= "SELECT HIST_NOTIFICACION.*  FROM HIST_NOTIFICACION WHERE NUM_EXPEDIENTE='" + numExpediente + "' AND FIRMADA='E' AND FECHA_ENVIO IS NOT NULL AND COD_MUNICIPIO ="+codOrganizacion+" ORDER BY FECHA_ENVIO DESC";
            else if(pluginPropio){
                sql= "SELECT NOTIFICACION.*  FROM NOTIFICACION WHERE NUM_EXPEDIENTE='" + numExpediente + "' AND FIRMADA='E' AND FECHA_ENVIO IS NOT NULL  AND COD_MUNICIPIO ="+codOrganizacion+" ORDER BY FECHA_ENVIO DESC";
            }else{
                sql = "SELECT NOTIFICACION.*  FROM NOTIFICACION WHERE NUM_EXPEDIENTE='"+numExpediente+"' AND COD_MUNICIPIO ="+codOrganizacion+" ORDER BY FECHA_ENVIO DESC";  
            }     
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            Hashtable<Integer,String> nombresTramites = new Hashtable<Integer,String>();    
            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setIdi_cod(idioma);
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);

            while(rs.next()){
                NotificacionVO notif = new NotificacionVO();
                String codProcedimiento = rs.getString("COD_PROCEDIMIENTO");
                int codMunicipio = rs.getInt("COD_MUNICIPIO");
                int codTramite = rs.getInt("COD_TRAMITE");
                notif.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notif.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notif.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                notif.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notif.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notif.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notif.setEjercicio(rs.getInt("EJERCICIO"));
                notif.setCodigoTramite(codTramite);
                notif.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));                               
                notif.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                
                
                
                if(nombresTramites.containsKey(codTramite))
                    notif.setNombreTramite(nombresTramites.get(codTramite));
                else{                        
                    
                    String nombreTramite = this.getNombreTramite(new Integer(codTramite),codProcedimiento,codMunicipio,con);
                    notif.setNombreTramite(nombreTramite);
                    nombresTramites.put(codTramite,nombreTramite);                    
                }
                java.sql.Timestamp fechaEnvio = rs.getTimestamp("FECHA_ENVIO");
                if(fechaEnvio!=null){
                    notif.setFechaEnvio(DateOperations.toCalendar(fechaEnvio));
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    notif.setFechaEnvioAsString(sf.format(notif.getFechaEnvio().getTime()));
                } else {
                    notif.setFechaEnvioAsString("-");
                }
                notif.setNumeroRegistroTelematico(rs.getString("REGISTRO_RT"));
           
                String fechaCaducidad=rs.getString("CADUCIDAD_NOTIFICACION");
				
				//INI -Comprobamos si tiene notificaciones individuales
				if (tieneNotificacionIndividual(notif.getCodigoNotificacion(),expedienteHistorico, con )){
					
					//si el resultado es afirmativo obtenemos las notificaciones individuales:
					notificacionesAux.addAll(getNotifIndividualByCodNotif(notif, codOrganizacion, expedienteHistorico, pluginPropio, traductor, con));
					
				} else{
				
					java.sql.Timestamp fechaAcuse = rs.getTimestamp("FECHA_ACUSE");
					if(fechaAcuse!=null){
						notif.setFechaAcuse(DateOperations.toCalendar(fechaAcuse));                    
						SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
						notif.setFechaAcuseAsString(sf.format(notif.getFechaAcuse().getTime()));
					} else {
						notif.setFechaAcuseAsString("--");
					}
					notif.setResultado(rs.getString("RESULTADO")==null?"--":rs.getString("RESULTADO"));
					if ("A".equals(notif.getResultado()))
						notif.setResultado(traductor.getDescripcion("etiqAcepNotif"));
					else if ("R".equals(notif.getResultado()))
						notif.setResultado(traductor.getDescripcion("etiqRechNotif"));

					if(verificarEstadoNotificaciones(codOrganizacion)){
						comprobarEstadoNotificacionesSNE(codOrganizacion, notif, expedienteHistorico, traductor, con);
					}
					
					ArrayList<AutorizadoNotificacionVO> terceroNotificacion = new ArrayList<AutorizadoNotificacionVO>();
					AutorizadoNotificacionVO tercero = new AutorizadoNotificacionVO();
					tercero.setNombreCompleto("--");
					terceroNotificacion.add(tercero);
					notif.setAutorizados(terceroNotificacion);
					
					// Datos que solicita Lanbide para mostrar en el listado
					if(!pluginPropio){
						java.sql.Timestamp fechaSolEnvio = rs.getTimestamp("FECHA_SOL_ENVIO");
						if(fechaSolEnvio!=null){
							notif.setFechaSolEnvio(DateOperations.toCalendar(fechaSolEnvio));
							SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							notif.setFechaSolEnvioAsString(sf.format(notif.getFechaSolEnvio().getTime()));
						} else {
							notif.setFechaSolEnvioAsString("-");
						}
						String codNotif = rs.getString("COD_NOTIFICACION_PLATEA");
						if(codNotif!= null){
							notif.setIdentificadorPlatea(codNotif);
						}else{
							notif.setIdentificadorPlatea("-");
						}
					}
					
					notificacionesAux.add(notif);
                }
				
                notificaciones.addAll(notificacionesAux);
                notificacionesAux.clear();                
            }

            m_Log.debug(notificaciones.size());
            m_Log.debug("getNotificacionesExpediente:END");
        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error("Error en la BBDD: " + e.getMessage());    
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return notificaciones;
    }

    private String getNombreTramite(int codTramite,String codProcedimiento,int codMunicipio,Connection con){
        String nombreTramite = null;
        Statement st = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT TML_VALOR FROM E_TML WHERE TML_TRA=" + codTramite + " AND TML_PRO='" + codProcedimiento + "' AND TML_MUN=" + codMunicipio;
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                nombreTramite = rs.getString("TML_VALOR");
            }

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return nombreTramite;

    }



    private String getNombreProcedimiento(String codProcedimiento,int codOrganizacion,Connection con){
        String nombreProc = null;
        Statement st = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT PML_VALOR FROM E_PML WHERE PML_COD='" + codProcedimiento + "' AND PML_MUN=" + codOrganizacion;
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                nombreProc = rs.getString("PML_VALOR");
            }

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return nombreProc;

    }



     /**
     * Recupera las notificaciones que pertenecen a un determinado expediente
     * @param codNotificacion: Código de la notificación
     * @param gestor: Nombre del gestor de BBDD
     * @param expedienteHistorico: True si el expediente está en el histórico y false en caso contrario
     * @param con: conexión a la BBDD
     * @return ArrayList<NotificacionVO>
     */
    public NotificacionVO getDetalleNotificacion(String codNotificacion,String gestor,boolean expedienteHistorico,Connection con)  throws TechnicalException {
        NotificacionVO notif = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "";
            String sql2 = "";
            
            if(!expedienteHistorico)
                sql = "SELECT NOTIFICACION.* ,(case when ((sysdate- to_date(FECHA_ENVIO))>CADUCIDAD_NOTIFICACION) THEN 1 ELSE 0 END) TTL FROM NOTIFICACION WHERE CODIGO_NOTIFICACION=?";
            else
                sql = "SELECT HISTO_NOTIFICACION.* ,(case when ((sysdate- to_date(FECHA_ENVIO))>CADUCIDAD_NOTIFICACION) THEN 1 ELSE 0 END) TTL FROM HIST_NOTIFICACION WHERE CODIGO_NOTIFICACION=?" ;
            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(codNotificacion));
            rs = ps.executeQuery();

            AdjuntoNotificacionDAO adjuntoDAO = AdjuntoNotificacionDAO.getInstance();
            
           while(rs.next()){
                notif = new NotificacionVO();
                String codProcedimiento = rs.getString("COD_PROCEDIMIENTO");
                int codMunicipio = rs.getInt("COD_MUNICIPIO");
                int codTramite = rs.getInt("COD_TRAMITE");
                notif.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notif.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notif.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                notif.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notif.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notif.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notif.setEjercicio(rs.getInt("EJERCICIO"));
                notif.setCodigoTramite(codTramite);
                notif.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                notif.setEstadoNotificacion(rs.getString("FIRMADA"));
                java.sql.Timestamp fechaAcuse = rs.getTimestamp("FECHA_ACUSE");
                if(fechaAcuse!=null){
                    notif.setFechaAcuse(DateOperations.toCalendar(fechaAcuse));
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    notif.setFechaAcuseAsString(sf.format(notif.getFechaAcuse().getTime()));
                } else {
                    notif.setFechaAcuseAsString("--");
                }
               notif.setResultado(rs.getString("RESULTADO")==null?"--":rs.getString("RESULTADO"));
               // Recuperamos el valor de acusado
               HashMap<Integer, String> acusado = getDatosAcusado(rs.getString("COD_NOTIFICACION_PLATEA"),con );
               if (acusado!=null){
                   notif.setPersonaAcuseDNI(acusado.get(0));
                   notif.setPersonaAcuseNombre(acusado.get(1));
               }

                String firma = null;
                InputStream is = rs.getBinaryStream("FIRMA");
                
                if(is!=null){
                    int BUFFER_SIZE = 1024;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    int length;
                    byte[] buffer = new byte[BUFFER_SIZE];

                    while ((length = is.read(buffer)) != -1)
                    {
                      output.write(buffer, 0, length);
                    }
                    firma = new String(output.toByteArray());
                }
                
                if(firma!=null)
                    notif.setFirma(firma);
                else
                    notif.setFirma(null);
                
                notif.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notif.setNombreTramite(this.getNombreTramite(codTramite,codProcedimiento,codMunicipio,con));
                notif.setNombreExpediente(this.getNombreProcedimiento(notif.getCodigoProcedimiento(),notif.getCodigoMunicipio(),con));
                String numRegistroRT = rs.getString("REGISTRO_RT");
                notif.setNumeroRegistroTelematico(numRegistroRT);
                
                String TiempoDesdeEnvio=rs.getString("TTL");
                String fechaCaducidad=rs.getString("CADUCIDAD_NOTIFICACION");
                if(verificarEstadoNotificaciones(codMunicipio)){
                    try{
                        // Se comprueba el estado de la notificación en el SNE
                        PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(codMunicipio));
                        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO notSNE = pluginNotificacion.consultarNotificacionSNE(Integer.toString(codMunicipio),numRegistroRT);
                        int estadoNotificacion = notSNE.getEstadoNotificacion();
                        //String fechaCaducidad  = notSNE.getFechaCaducidad();
                        m_Log.debug("estadoNotificacion: " + estadoNotificacion + ", fechaCaducidad: " + fechaCaducidad);


    //                    if(estadoNotificacion==0){
    //                        if("1".equals(caducada))
    //                        {
    //                            notif.setEstadoNotificacion("4"); //Caducada
    //                        }else{
    //                            notif.setEstadoNotificacion("0"); // Pendiente
    //                        }
    //                    }
    //                    else
    //                    if(estadoNotificacion==1)
    //                        notif.setEstadoNotificacion("1"); // Leida
    //                    else
    //                    if(estadoNotificacion==2)
    //                        notif.setEstadoNotificacion("2"); // Rechazada
    //                    else
    //                        notif.setEstadoNotificacion("3"); // cancelada

                        //nuevos estados devueltos por el SNE Noviembre 2016
                         if(estadoNotificacion==0){
                              notif.setEstadoNotificacion("0");

                         }else if(estadoNotificacion==1){
                             notif.setEstadoNotificacion("1");

                         }else if(estadoNotificacion==2){
                             notif.setEstadoNotificacion("2");

                         }else if(estadoNotificacion==3){
                             notif.setEstadoNotificacion("3");

                         }else if(estadoNotificacion==4){
                             notif.setEstadoNotificacion("4");

                         }

                        String fechaAcuseNotificacion = notSNE.getFechaLeidoRechazado();
                        m_Log.debug("fechaAcuseNotificacion : " + fechaAcuseNotificacion);
                        String horaAcuseNotificacion = notSNE.getHoraLeidoRechazado();
                        m_Log.debug("horaAcuseNotificacion : " + horaAcuseNotificacion);
                        
                        if(fechaAcuseNotificacion != null && fechaAcuse == null){
                            notif.setFechaAcuseAsString(fechaAcuseNotificacion+" "+horaAcuseNotificacion);
                            actualizarFechaAcuseNotificacion(notif, expedienteHistorico, con);
                        }
                        
                         if(estadoNotificacion==4 || estadoNotificacion==1){ //notificacion leida y leida_cancelada resultado acuse notificacion A
                                notif.setResultado("A");
                         } else if(estadoNotificacion==3){  //notificacion rechazada resultado acuse notificación  R
                                notif.setResultado("R");
                         } 
                         
                         // si estado distinto de 0 no leida  y  2 caducada se actualiza la tabla
                         if(estadoNotificacion!=0 && estadoNotificacion!=2){
                                actualizarResultadoAcuseNotificacion(notif, expedienteHistorico, con);
                         } 


                    }catch(Exception e){
                        m_Log.debug("Error al recuperar el estado de la notificación del SNE: " + e.getMessage());
                        e.printStackTrace();
                        notif.setEstadoNotificacion("0");  //==> Se indica que está pendiente sino se ha podido leer
                    }
                }

            }

            // Se recuperan los adjuntos externos de la notificación
            ArrayList<AdjuntoNotificacionVO> adjuntosExternos = AdjuntoNotificacionDAO.getInstance().getListaAdjuntosExterno(Integer.parseInt(codNotificacion),expedienteHistorico,con);
            notif.setAdjuntosExternos(adjuntosExternos);

            AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
            adjunto.setNumeroExpediente(notif.getNumExpediente());
            adjunto.setEjercicio(notif.getEjercicio());
            adjunto.setCodigoMunicipio(notif.getCodigoMunicipio());
            adjunto.setCodigoProcedimiento(notif.getCodigoProcedimiento());
            adjunto.setCodigoTramite(notif.getCodigoTramite());
            adjunto.setOcurrenciaTramite(notif.getOcurrenciaTramite());
            adjunto.setCodigoNotificacion(notif.getCodigoNotificacion());

            ArrayList<AdjuntoNotificacionVO> adjuntosTramitacion = adjuntoDAO.getDocumentosTramitacion(adjunto,expedienteHistorico,con);
            ArrayList<AdjuntoNotificacionVO> auxTramitacion = new ArrayList<AdjuntoNotificacionVO>();
            for(int i=0;adjuntosTramitacion!=null && i<adjuntosTramitacion.size();i++){
                if(adjuntosTramitacion.get(i).getSeleccionado().equalsIgnoreCase("SI")){
                    auxTramitacion.add(adjuntosTramitacion.get(i));
                }
            }
            notif.setAdjuntos(auxTramitacion);

            ArrayList<AutorizadoNotificacionVO> inter = AutorizadoNotificacionDAO.getInstance().getDetalleInteresadosExpediente(notif.getNumExpediente(),notif.getCodigoNotificacion(),expedienteHistorico,con);            
            notif.setAutorizados(inter);

        }catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException("Error al recuperar el detalla de una notificación",e);
        }catch(Exception e){
            e.printStackTrace();
            throw new TechnicalException("Error al recuperar el detalla de una notificación",e);
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return notif;
    }



    public GeneralValueObject getUnidadTramitadoraTramite(String codTramite,String ocurrenciaTramite,String numExpediente,Connection con){
        GeneralValueObject gvo = new GeneralValueObject();
        Statement st = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT UOR_NOM,UOR_COD FROM E_CRO,A_UOR WHERE CRO_NUM='" + numExpediente + "' AND CRO_TRA=" + codTramite + " AND CRO_OCU=" + ocurrenciaTramite +
                         " AND CRO_UTR=A_UOR.UOR_COD";
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);


            String nombre ="";
            String uorCod ="";
            while(rs.next()){
                nombre = rs.getString("UOR_NOM");
                uorCod = rs.getString("UOR_COD");
            }
            gvo.setAtributo("UOR_NOM",nombre);
            gvo.setAtributo("UOR_COD",uorCod);

        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return gvo;

    }



    /**
     * Comprueba si una notificación tiene cubierto todos los datos obligatorios para que pueda ser firmada
     * @param codNotificacion: Código de la notificación
     * @param nombreGestor: Nombre del gestor de BBDD (ORACLE o SQLSERVER)
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean estaNotificacionPreparadaParaFirma(String codNotificacion,String numExpediente,String nombreGestor,Connection con){
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT ACTO_NOTIFICADO,CADUCIDAD_NOTIFICACION,TEXTO_NOTIFICACION FROM NOTIFICACION WHERE CODIGO_NOTIFICACION=" + codNotificacion;
                         
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            String acto = null;
            int caducidad = -1;
            String texto = null;
            while(rs.next()){
                acto = rs.getString("ACTO_NOTIFICADO");
                texto = rs.getString("TEXTO_NOTIFICACION");
                caducidad = rs.getInt("CADUCIDAD_NOTIFICACION");
            }

            if(acto!=null && !"".equals(acto) && texto!=null && !"".equals(texto) && caducidad>=0){
                // Se comprueba ahora si tiene interesados a los que enviar la notificación electrónica

                ArrayList<AutorizadoNotificacionVO> autorizados =AutorizadoNotificacionDAO.getInstance().getInteresadosExpediente(numExpediente,Integer.parseInt(codNotificacion),nombreGestor,con);
                int contador = 0;
                for(int i=0;autorizados!=null && i<autorizados.size();i++){
                    AutorizadoNotificacionVO aut = autorizados.get(i);
                    if(aut.getSeleccionado().equalsIgnoreCase("SI")){
                        contador++;
                    }
                }

                if(contador>0) exito = true;

            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return exito;
    }

    public void actualizarFechaAcuseNotificacion(NotificacionVO notificacion, Boolean expHistorico, Connection con) throws TechnicalException {
        String sql = "";
        PreparedStatement ps = null;

        try {
            String fechaAcuse = notificacion.getFechaAcuseAsString();
            int codigoNotificacion = notificacion.getCodigoNotificacion();

            if (expHistorico) {
                sql = "UPDATE HIST_NOTIFICACION SET FECHA_ACUSE=? "
                        + " WHERE CODIGO_NOTIFICACION=?";
            } else {
                sql = "UPDATE NOTIFICACION SET FECHA_ACUSE=? "
                        + " WHERE CODIGO_NOTIFICACION=?";
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setTimestamp(i++, DateOperations.guessTimestamp(fechaAcuse));
            ps.setInt(i++, codigoNotificacion);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.debug("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                SigpGeneralOperations.closeStatement(ps);
            } catch (Exception e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.debug("Excepcion capturada en: " + getClass().getName());
                }

            }
        }
    }
   
    public void actualizarResultadoAcuseNotificacion(NotificacionVO notificacion, Boolean expHistorico, Connection con) throws TechnicalException {
        String sql = "";
        PreparedStatement ps = null;

        try {
            int codigoNotificacion = notificacion.getCodigoNotificacion();
            String resultado = notificacion.getResultado();

            if (expHistorico) {
                sql = "UPDATE HIST_NOTIFICACION SET RESULTADO=? "
                        + " WHERE CODIGO_NOTIFICACION=?";
            } else {
                sql = "UPDATE NOTIFICACION SET RESULTADO=? "
                        + " WHERE CODIGO_NOTIFICACION=?";
            }

            m_Log.debug(sql);

            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setString(i++, resultado);
            ps.setInt(i++, codigoNotificacion);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.debug("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                SigpGeneralOperations.closeStatement(ps);
            } catch (Exception e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.debug("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
    }

	
	public boolean tieneNotificacionIndividual(int codNotificacion, boolean historificada, Connection con)  {

		String sql = "";
        boolean exito =false;
        Statement st = null;
        ResultSet rs = null;

        try{

			if (!historificada){
				sql = "SELECT COUNT(*) AS NUM FROM NOTIFICACION_INDIVIDUAL WHERE CODIGO_NOTIFICACION='" + codNotificacion + "'" ;
			}
			else{
				sql = "SELECT COUNT(*) AS NUM FROM HIST_NOTIFICACION_INDIVIDUAL WHERE CODIGO_NOTIFICACION='" + codNotificacion + "'" ;
			}
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            int num = -1;
            while(rs.next()){
                 num = rs.getInt("NUM");
            }

            if(num>=1) exito = true;

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return exito;
    }
	
	public NotificacionVO getDetalleNotificacionIndividual(String codNotificacion,String codRegTel, String gestor,boolean expedienteHistorico,Connection con)  throws TechnicalException {
        NotificacionVO notif = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		PreparedStatement psIndv = null;
		ResultSet rsIndv = null;
		int codTer = 0;
        try{
            String sql = "";
            String sql2 = "";
            
            if(!expedienteHistorico)
                sql = "SELECT NOTIFICACION.* ,(case when ((sysdate- to_date(FECHA_ENVIO))>CADUCIDAD_NOTIFICACION) THEN 1 ELSE 0 END) TTL FROM NOTIFICACION WHERE CODIGO_NOTIFICACION=?";
            else
                sql = "SELECT HISTO_NOTIFICACION.* ,(case when ((sysdate- to_date(FECHA_ENVIO))>CADUCIDAD_NOTIFICACION) THEN 1 ELSE 0 END) TTL FROM HIST_NOTIFICACION WHERE CODIGO_NOTIFICACION=?" ;
            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(codNotificacion));
            rs = ps.executeQuery();

            //INI - Recuperamos los datos de la notificacion individual:
            if(expedienteHistorico)
                sql2 = "SELECT HIST_NOTIFICACION_INDIVIDUAL.* FROM HIST_NOTIFICACION_INDIVIDUAL WHERE REGISTRO_RT='" + codRegTel+ "'";
            else
                sql2 = "SELECT NOTIFICACION_INDIVIDUAL.* FROM NOTIFICACION_INDIVIDUAL WHERE REGISTRO_RT ='" + codRegTel+ "'";
            m_Log.debug(sql2);

            psIndv = con.prepareStatement(sql2);
            rsIndv = psIndv.executeQuery();
			
			//FIN - Recuperamos los datos de la notificacion individual:
			
            AdjuntoNotificacionDAO adjuntoDAO = AdjuntoNotificacionDAO.getInstance();
            
           while(rs.next()){
			   
			    while(rsIndv.next()){
					
				codTer = rsIndv.getInt("TER_COD");	
				
                notif = new NotificacionVO();
                String codProcedimiento = rs.getString("COD_PROCEDIMIENTO");
                int codMunicipio = rs.getInt("COD_MUNICIPIO");
                int codTramite = rs.getInt("COD_TRAMITE");
                notif.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notif.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notif.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                notif.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notif.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notif.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notif.setEjercicio(rs.getInt("EJERCICIO"));
                notif.setCodigoTramite(codTramite);
                notif.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                notif.setEstadoNotificacion(rs.getString("FIRMADA"));
				
                java.sql.Timestamp fechaAcuse = rsIndv.getTimestamp("FECHA_ACUSE");
				
                if(fechaAcuse!=null){
                    notif.setFechaAcuse(DateOperations.toCalendar(fechaAcuse));
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    notif.setFechaAcuseAsString(sf.format(notif.getFechaAcuse().getTime()));
                } else {
                    notif.setFechaAcuseAsString("--");
                }
                notif.setResultado(rsIndv.getString("RESULTADO")==null?"--":rsIndv.getString("RESULTADO"));
                
                String firma = null;
                InputStream is = rs.getBinaryStream("FIRMA");
                
                if(is!=null){
                    int BUFFER_SIZE = 1024;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    int length;
                    byte[] buffer = new byte[BUFFER_SIZE];

                    while ((length = is.read(buffer)) != -1)
                    {
                      output.write(buffer, 0, length);
                    }
                    firma = new String(output.toByteArray());
                }
                
                if(firma!=null)
                    notif.setFirma(firma);
                else
                    notif.setFirma(null);
                
                notif.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notif.setNombreTramite(this.getNombreTramite(codTramite,codProcedimiento,codMunicipio,con));
                notif.setNombreExpediente(this.getNombreProcedimiento(notif.getCodigoProcedimiento(),notif.getCodigoMunicipio(),con));
                
				
                notif.setNumeroRegistroTelematico(codRegTel);
                
                String TiempoDesdeEnvio=rs.getString("TTL");
                String fechaCaducidad=rs.getString("CADUCIDAD_NOTIFICACION");
                if(verificarEstadoNotificaciones(codMunicipio)){
                    try{
                        // Se comprueba el estado de la notificación en el SNE
                        PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(codMunicipio));
                        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO notSNE = pluginNotificacion.consultarNotificacionSNE(Integer.toString(codMunicipio),notif.getNumeroRegistroTelematico());
                        int estadoNotificacion = notSNE.getEstadoNotificacion();
                        //String fechaCaducidad  = notSNE.getFechaCaducidad();
                        m_Log.debug("estadoNotificacion: " + estadoNotificacion + ", fechaCaducidad: " + fechaCaducidad);

                        //nuevos estados devueltos por el SNE Noviembre 2016
                        if(estadoNotificacion==0){
                              notif.setEstadoNotificacion("0");

                         }else if(estadoNotificacion==1){
                             notif.setEstadoNotificacion("1");

                         }else if(estadoNotificacion==2){
                             notif.setEstadoNotificacion("2");

                         }else if(estadoNotificacion==3){
                             notif.setEstadoNotificacion("3");

                         }else if(estadoNotificacion==4){
                             notif.setEstadoNotificacion("4");

                         }
                        
                        String fechaAcuseNotificacion = notSNE.getFechaLeidoRechazado();
                        m_Log.debug("fechaAcuseNotificacion : " + fechaAcuseNotificacion);
                        String horaAcuseNotificacion = notSNE.getHoraLeidoRechazado();
                        m_Log.debug("horaAcuseNotificacion : " + horaAcuseNotificacion);
                        
                        if(fechaAcuseNotificacion != null && fechaAcuse == null){
                            notif.setFechaAcuseAsString(fechaAcuseNotificacion+" "+horaAcuseNotificacion);
                            actualizarFechaAcuseNotificacion(notif, expedienteHistorico, con);
                        }
                        
                         if(estadoNotificacion==4 || estadoNotificacion==1){ //notificacion leida y leida_cancelada resultado acuse notificacion A
                                notif.setResultado("A");
                         } else if(estadoNotificacion==3){  //notificacion rechazada resultado acuse notificación  R
                                notif.setResultado("R");
                         } 
                         
                         // si estado distinto de 0 no leida  y  2 caducada se actualiza la tabla
                         if(estadoNotificacion!=0 && estadoNotificacion!=2){
                                actualizarResultadoAcuseNotificacion(notif, expedienteHistorico, con);
                         } 
                         
                      
                    }catch(Exception e){
                        m_Log.debug("Error al recuperar el estado de la notificación del SNE: " + e.getMessage());
                        e.printStackTrace();
                        notif.setEstadoNotificacion("0");  //==> Se indica que está pendiente sino se ha podido leer
                    }
                }

            }
		   }

            // Se recuperan los adjuntos externos de la notificación
            ArrayList<AdjuntoNotificacionVO> adjuntosExternos = AdjuntoNotificacionDAO.getInstance().getListaAdjuntosExterno(Integer.parseInt(codNotificacion),expedienteHistorico,con);
            notif.setAdjuntosExternos(adjuntosExternos);

            AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
            adjunto.setNumeroExpediente(notif.getNumExpediente());
            adjunto.setEjercicio(notif.getEjercicio());
            adjunto.setCodigoMunicipio(notif.getCodigoMunicipio());
            adjunto.setCodigoProcedimiento(notif.getCodigoProcedimiento());
            adjunto.setCodigoTramite(notif.getCodigoTramite());
            adjunto.setOcurrenciaTramite(notif.getOcurrenciaTramite());
            adjunto.setCodigoNotificacion(notif.getCodigoNotificacion());

            ArrayList<AdjuntoNotificacionVO> adjuntosTramitacion = adjuntoDAO.getDocumentosTramitacion(adjunto,expedienteHistorico,con);
            ArrayList<AdjuntoNotificacionVO> auxTramitacion = new ArrayList<AdjuntoNotificacionVO>();
            for(int i=0;adjuntosTramitacion!=null && i<adjuntosTramitacion.size();i++){
                if(adjuntosTramitacion.get(i).getSeleccionado().equalsIgnoreCase("SI")){
                    auxTramitacion.add(adjuntosTramitacion.get(i));
                }
            }
            notif.setAdjuntos(auxTramitacion);

            ArrayList<AutorizadoNotificacionVO> inter = AutorizadoNotificacionDAO.getInstance().getDetalleInteresadosExpediente(notif.getNumExpediente(),notif.getCodigoNotificacion(),expedienteHistorico,con);            
            ArrayList<AutorizadoNotificacionVO> inter1 = new ArrayList<AutorizadoNotificacionVO>();
			for (AutorizadoNotificacionVO aut : inter){

				if ( codTer == aut.getCodigoTercero()){

					inter1.add(aut);
					break;
				}
			}
			
			notif.setAutorizados(inter1);
			
        }catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException("Error al recuperar el detalla de una notificación",e);
        }catch(Exception e){
            e.printStackTrace();
            throw new TechnicalException("Error al recuperar el detalla de una notificación",e);
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
				
				if(psIndv!=null) ps.close();
                if(rsIndv!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return notif;
    }
	
	private ArrayList<NotificacionVO> getNotifIndividualByCodNotif(NotificacionVO notificacion, int codOrganizacion, boolean expHistorico, boolean pluginPropio, TraductorAplicacionBean traductor, Connection con){
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		NotificacionVO notifIndv = null;
		ArrayList<AutorizadoNotificacionVO> inter = null;
		int codNotificacion = notificacion.getCodigoNotificacion();
		ArrayList<NotificacionVO> notificacionesAux = new ArrayList<NotificacionVO>(); 
		
		try {
			if (expHistorico)
				sql= "SELECT HIST_NOTIFICACION_INDIVIDUAL.* FROM HIST_NOTIFICACION_INDIVIDUAL WHERE CODIGO_NOTIFICACION=?";
			else
				sql= "SELECT NOTIFICACION_INDIVIDUAL.* FROM NOTIFICACION_INDIVIDUAL WHERE CODIGO_NOTIFICACION=?";
			m_Log.debug("SQL: " + sql);
			m_Log.debug("Parámetros pasados a la SQL: " + codNotificacion);
			
			ps = con.prepareStatement(sql);
			ps.setInt(1, codNotificacion);
			rs = ps.executeQuery();
			
			while(rs.next()){
				//Creamos una notifiacion auxiliar para conservar los datos previos
				notifIndv = new NotificacionVO();

				//Cargamos los datos de la notificación en notifAux
				notifIndv.setActoNotificado(notificacion.getActoNotificado());
				notifIndv.setAdjuntos(notificacion.getAdjuntos());
				notifIndv.setAdjuntosExternos(notificacion.getAdjuntosExternos());
				notifIndv.setAplicacion(notificacion.getAplicacion());
				notifIndv.setCaducidadNotificacion(notificacion.getCaducidadNotificacion());
				notifIndv.setClaseProcedimiento(notificacion.getClaseProcedimiento());
				notifIndv.setCodDepartamento(notificacion.getCodDepartamento());
				notifIndv.setCodigoMunicipio(notificacion.getCodigoMunicipio());
				notifIndv.setCodigoNotificacion(codNotificacion);
				notifIndv.setCodigoProcedimiento(notificacion.getCodigoProcedimiento());
				notifIndv.setCodigoTipoNotificacion(notificacion.getCodigoTipoNotificacion());
				notifIndv.setCodigoTramite(notificacion.getCodigoTramite());
				notifIndv.setDepartamento(notificacion.getCodDepartamento());
				notifIndv.setEjercicio(notificacion.getEjercicio());
				notifIndv.setEstadoNotificacion(notificacion.getEstadoNotificacion());
				notifIndv.setFechaEnvio(notificacion.getFechaEnvio());
				notifIndv.setFechaEnvioAsString(notificacion.getFechaEnvioAsString());
				notifIndv.setFirma(notificacion.getFirma());
				notifIndv.setIdEmisor(notificacion.getIdEmisor());
				notifIndv.setNombreEmisor(notificacion.getNombreEmisor());
				notifIndv.setNombreExpediente(notificacion.getNombreExpediente());
				notifIndv.setNumExpediente(notificacion.getNumExpediente());
				notifIndv.setNombreTramite(notificacion.getNombreTramite());
				notifIndv.setOcurrenciaTramite(notificacion.getOcurrenciaTramite());
				notifIndv.setParams(notificacion.getParams());
				notifIndv.setTextoNotificacion(notificacion.getTextoNotificacion());
				
				//Recuperamos los interesados de la notificación:
                try{
                    inter= AutorizadoNotificacionDAO.getInstance().getDetalleInteresadosExpediente(notifIndv.getNumExpediente(),notifIndv.getCodigoNotificacion(),expHistorico,con);                                                    
                }catch(Exception e){
					m_Log.debug("Error al recuperar los interesados de la notificación " + e.getMessage());
					e.printStackTrace();
                } 
				
				int codigoTercero = rs.getInt("TER_COD");
				for (AutorizadoNotificacionVO interesado : inter){

					if (interesado.getCodigoTercero() == codigoTercero){
						ArrayList<AutorizadoNotificacionVO> terceroNotificacion = new ArrayList<AutorizadoNotificacionVO>();
						terceroNotificacion.add(interesado);
						notifIndv.setAutorizados(terceroNotificacion);
					}
				}
				
				// Asignamos el numero de registro telemático correspondiente a la comunicación en curso,
				// necesario para mostrar las notificaciones individuales
				notifIndv.setNumeroRegistroTelematico(rs.getString("REGISTRO_RT"));

				java.sql.Timestamp fechaAcuse = rs.getTimestamp("FECHA_ACUSE");
				if(fechaAcuse!=null){
					notifIndv.setFechaAcuse(DateOperations.toCalendar(fechaAcuse));                    
					SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
					notifIndv.setFechaAcuseAsString(sf.format(notifIndv.getFechaAcuse().getTime()));
				} else {
					notifIndv.setFechaAcuseAsString("--");
				}

				notifIndv.setResultado(rs.getString("RESULTADO")==null?"--":rs.getString("RESULTADO"));                                              

				if ("A".equals(notifIndv.getResultado()))
				   notifIndv.setResultado(traductor.getDescripcion("etiqAcepNotif"));
				else if ("R".equals(notifIndv.getResultado()))
				   notifIndv.setResultado(traductor.getDescripcion("etiqRechNotif"));
				
				if(verificarEstadoNotificaciones(codOrganizacion)){
					comprobarEstadoNotificacionesSNE(codOrganizacion, notifIndv, expHistorico, traductor, con);							 
				}
				
				// Datos que solicita Lanbide para mostrar en el listado
				if(!pluginPropio){
					java.sql.Timestamp fechaSolEnvio = rs.getTimestamp("FECHA_SOL_ENVIO");
					if(fechaSolEnvio!=null){
						notifIndv.setFechaSolEnvio(DateOperations.toCalendar(fechaSolEnvio));
						SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						notifIndv.setFechaSolEnvioAsString(sf.format(notifIndv.getFechaSolEnvio().getTime()));
					} else {
						notifIndv.setFechaSolEnvioAsString("-");
					}
					String codNotif = rs.getString("COD_NOTIFICACION_PLATEA");
					if(codNotif!= null){
						notifIndv.setIdentificadorPlatea(codNotif);
					}else{
						notifIndv.setIdentificadorPlatea("-");
					}
				}

				notificacionesAux.add(notifIndv);
			}
			
		} catch (SQLException sqle) {
			m_Log.error("Error en la BBDD: " + sqle.getMessage());    
			sqle.printStackTrace();
		} finally {
			try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(Exception e){
                e.printStackTrace();
            }
		}
		
		return notificacionesAux;
	}

    private HashMap<Integer, String> getDatosAcusado(String oidSolicitud, Connection con){
        HashMap<Integer, String> acusado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT \n" +
                    "    REGEXP_SUBSTR(REGLAS_SUSCRIPCION, '<deliveryPersonId >(.*?)</deliveryPersonId >', 1, 1, NULL, 1) AS deliveryPersonId,\n" +
                    "    REGEXP_SUBSTR(REGLAS_SUSCRIPCION, '<deliveryPersonName>(.*?)</deliveryPersonName>', 1, 1, NULL, 1) AS deliveryPersonName\n" +
                    "FROM \n" +
                    "    ERRDIGIT.estadisticas\n" +
                    "WHERE \n" +
                    "    OID_SOLICITUD = ? \n" +
                    "    AND EVENTO LIKE 'PUBLISH_NOTIFICATION_DELIVERY -%' \n" +
                    "    AND RESULTADO = 'OK'";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setString(1, oidSolicitud);
            rs = ps.executeQuery();

            while(rs.next()){
                acusado = new HashMap<Integer, String>();
                acusado.put(0, rs.getString(1));
                acusado.put(1, rs.getString(2));
            }

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return acusado;

    }
}
