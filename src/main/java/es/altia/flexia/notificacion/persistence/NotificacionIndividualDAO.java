package es.altia.flexia.notificacion.persistence;

import java.util.ArrayList;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.common.exception.TechnicalException;

import java.sql.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import es.altia.flexia.notificacion.vo.*;
import es.altia.util.commons.DateOperations;
import java.util.Calendar;

import java.util.ResourceBundle;
import es.altia.agora.technical.ConstantesDatos;

public class NotificacionIndividualDAO {
	
	private static NotificacionIndividualDAO instance =	null;
    protected static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(NotificacionIndividualDAO.class.getName());
	
	protected NotificacionIndividualDAO() {
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");
	}
	
	
	public static NotificacionIndividualDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (NotificacionIndividualDAO.class) {
            if (instance == null) {
                instance = new NotificacionIndividualDAO();
            }

        }
        return instance;
    }
	
	/**
	 * Da de alta una notificación individual en base de datos. 
	 * La notificación individual está asociada a una notificación y un interesado
	 * 
	 * @return -1 si resultado KO
	 * @return codigoNotificacionIndividual si resultado OK
	 */
	public int insertarNotificacionIndividual(NotificacionIndividualVO notificacionIndividual, Connection con) throws TechnicalException {
		
		int resultado = 0;    
		PreparedStatement ps = null;
		String sql = "";
		ResultSet rs = null;
		int codigo=0;

    try{
		
		int codigoNotificacion = notificacionIndividual.getCodigoNotificacion();
		int autorizado = notificacionIndividual.getAutorizado().getCodigoTercero();
		String enviada = notificacionIndividual.getEstadoNotificacionIndividual();
		String registroRT = notificacionIndividual.getNumeroRegistroTelematico();

		ResourceBundle config = ResourceBundle.getBundle("techserver");

		if(config.getString("CON.gestor").equalsIgnoreCase(ConstantesDatos.ORACLE)) {

		sql = "INSERT INTO NOTIFICACION_INDIVIDUAL"
				+ "(CODIGO_NOTIFICACION_INDIVIDUAL,"
				+ "CODIGO_NOTIFICACION,"
				+ "TER_COD,"
				+ "ENVIADA,"
				+ "REGISTRO_RT) "
				+" VALUES (SEQ_NOTIFICACION_INDIVIDUAL.nextval,?,?,?,?)";   
          
      } else
      if(config.getString("CON.gestor").equalsIgnoreCase(ConstantesDatos.SQLSERVER)) {
          sql = "INSERT INTO NOTIFICACION_INDIVIDUAL"
				+ "(CODIGO_NOTIFICACION_INDIVIDUAL,"
				+ "CODIGO_NOTIFICACION,"
				+ "TER_COD,"
				+ "ENVIADA,"
				+ "REGISTRO_RT) "
				+" VALUES (?,?,?,?)";             
      }
		
	  int i = 1;
      ps = con.prepareStatement(sql);
      ps.setInt(i++,codigoNotificacion);
	  ps.setInt(i++,autorizado);
      ps.setString(i++,enviada);
	  ps.setString(i++,registroRT);
      
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
	
	

	/**
	 * Actualiza la información de una notificación individual
	 * @return true - actualizada correctamente
	 */
	public boolean updateNotificacionIndividual(NotificacionIndividualVO notificacionIndividual, Connection con) throws TechnicalException {
	
		return false;
	}
	
	/**
	 * Recupera la información de una notificación individual asociada a una notifiación y un interesado
	 * @return NotificacionVO
	 */
	public NotificacionVO getNotificacionIndividual(NotificacionIndividualVO notificacionIndividual, Connection con) throws TechnicalException {
		
		return null;
	}
	
	/**
     * Recupera las notificaciones individuales asociadas a una notificación
     * @return ArrayList<NotificacionVO>
     */
    public ArrayList<NotificacionVO> getNotificacionesIndividualesNotificacion(NotificacionIndividualVO notificacionIndividual, Connection con)  {
		
		return null;
	}
	
	
	
        
}
