package es.altia.flexia.notificacion.persistence;



// PAQUETES	IMPORTADOS

import es.altia.agora.business.util.GeneralValueObject;
import java.sql.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.util.ArrayList;

import es.altia.flexia.notificacion.vo.*;
import es.altia.util.conexion.BDException;


public class NotificacionManager {


    private static NotificacionManager instance =	null;
	protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
	protected static Config m_ConfigError; // Para los mensajes de error localizados
	protected static Log m_Log =
            LogFactory.getLog(NotificacionManager.class.getName());


    protected NotificacionManager() {
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");
	}

    public static NotificacionManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (NotificacionManager.class) {
            if (instance == null) {
                instance = new NotificacionManager();
            }

        }
        return instance;
    }

    /**
    //Da de alta una notificación en la base de datos. Devuelve un boolean.
    public int insertarNotificacion(NotificacionVO notificacion,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("insertarNotificacion");

	int res = -1;

	try{
            res = NotificacionDAO.getInstance().insertarNotificacion(notificacion, params);

	}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    } */
    


    //Almacena la firma de una notificación en base de datos. Devuelve un boolean
    public boolean guardarFirma(int codigoNotificacion,String firma,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("guardarFirma");

	boolean res = false;

	try{
            res = NotificacionDAO.getInstance().guardarFirma(codigoNotificacion, firma,params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }

   
    public String getTipoNotificacion(NotificacionVO notificacion,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("getTipoNotificacion");

	String ret = "";

	try{
            ret = NotificacionDAO.getInstance().getTipoNotificacion(notificacion, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return ret;
	}
    }

    public int getUsuarioFirmanteNotificacion(NotificacionVO notificacion,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("getUsuarioFirmanteNotificacion");

	int ret = -1;

	try{
            ret = NotificacionDAO.getInstance().getUsuarioFirmanteNotificacion(notificacion, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return ret;
	}
    }

     public boolean guardarEstadoNotificacionEnviada(NotificacionVO notificacion,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("guardarEstadoNotificacionEnviada");

	boolean res = false;

	try{
            res = NotificacionDAO.getInstance().guardarEstadoNotificacionEnviada(notificacion, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }


      /**
  * Almacena el xml de la notificación que posteriormente será firmado por el usuario
  * @param codNotificacion: Código de la notificacion
  * @param con: Conexión
  *
  */
  public String getXMLFirmaNotificacion(int codNotificacion,String[] params)  {
    String xml = null;
    Connection con = null;
    AdaptadorSQLBD adapt = null;

	try{
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        xml = NotificacionDAO.getInstance().getXMLFirmaNotificacion(codNotificacion, con);

	}catch (Exception e) {
        e.printStackTrace();
	}finally{
        try{
            adapt.devolverConexion(con);
        }catch(Exception e){
            m_Log.error("Error al devolver la conexión a la BBDD: " + e.getMessage());
        }
	}
    return xml;
  }



   public String getCodDepartamentoNotifTramite(int codTramite,String codProcedimiento,int codOrganizacion,String[] params)  {
        String xml = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            xml = NotificacionDAO.getInstance().getCodDepartamentoNotifTramite(codTramite,codProcedimiento,codOrganizacion, con);

        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al devolver la conexión a la BBDD: " + e.getMessage());
            }
        }
        return xml;
  }


   /**
    * Comprueba si en una determinada ocurrencia de trámite de un expediente, si hay una notificación que haya sido enviada
    * @param numExpediente: Nº expediente
    * @param codProcedimiento: 
    * @param codTramite
    * @param ocuTramite
    * @param codMunicipio
    * @param ejercicio
    * @param params
    * @return
    */
   public boolean tieneNotificacionEnviadaTramite(String numExpediente,String codProcedimiento,String codTramite,String ocuTramite,String codMunicipio,String ejercicio,String[] params)  {
       AdaptadorSQLBD adapt = null;
       Connection con = null;
       boolean exito = false;
       try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = NotificacionDAO.getInstance().tieneNotificacionEnviadaTramite(numExpediente, codProcedimiento, codTramite, ocuTramite, codMunicipio, ejercicio, con);

       }catch(Exception e){
           exito = false;
           e.printStackTrace();
       }finally{
           try{
              adapt.devolverConexion(con);
           }catch(Exception e){
              e.printStackTrace();
           }
       }
       return exito;
   }

  

    public NotificacionVO getDetalleNotificacion(String codNotificacion, boolean expedienteHistorico, String[] params) throws TechnicalException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        NotificacionVO notif = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            notif = NotificacionDAO.getInstance().getDetalleNotificacion(codNotificacion, params[0], expedienteHistorico, con);
        } catch (Exception e) {
			e.printStackTrace();
            TechnicalException exception = new TechnicalException(e.getMessage(), e);
            
            try {
                adapt.rollBack(con);
            } catch (BDException bde) {
                exception = new TechnicalException("Error al realizar rollback: " + e.getMessage(), e);
            }
            
            throw exception;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                m_Log.error("Error al devolver la conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return notif;
    }
	
	public NotificacionVO getDetalleNotificacion(String codNotificacion,String codRegTel, boolean expedienteHistorico, String[] params) throws TechnicalException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        NotificacionVO notif = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
			
			if (NotificacionDAO.getInstance().tieneNotificacionIndividual(Integer.parseInt(codNotificacion), expedienteHistorico, con)){
				notif = NotificacionDAO.getInstance().getDetalleNotificacionIndividual(codNotificacion, codRegTel, params[0], expedienteHistorico, con);
			}
			else{
				notif = NotificacionDAO.getInstance().getDetalleNotificacion(codNotificacion, params[0], expedienteHistorico, con);
			}
			
        } catch (Exception e) {
            TechnicalException exception = new TechnicalException(e.getMessage(), e);
            
            try {
                adapt.rollBack(con);
            } catch (BDException bde) {
                exception = new TechnicalException("Error al realizar rollback: " + e.getMessage(), e);
            }
            
            throw exception;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return notif;
    }

    public GeneralValueObject getUnidadTramitadoraTramite(String codTramite,String ocurrenciaTramite,String numExpediente,String[] params){
        GeneralValueObject gvo = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            gvo =  NotificacionDAO.getInstance().getUnidadTramitadoraTramite(codTramite,ocurrenciaTramite,numExpediente,con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar la conexión: " + e.getMessage());
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
    public boolean estaNotificacionPreparadaParaFirma(String codNotificacion,String numExpediente,String[] params){
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = NotificacionDAO.getInstance().estaNotificacionPreparadaParaFirma(codNotificacion, numExpediente, params[0], con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar la conexión: " + e.getMessage());
            }
        }

        return exito;

    }
	
	public boolean tieneNotificacionIndividual(int codNotificacion, boolean historificada,String[] params){
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = NotificacionDAO.getInstance().tieneNotificacionIndividual(codNotificacion, historificada, con);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar la conexión: " + e.getMessage());
            }
        }

        return exito;

    }
}
