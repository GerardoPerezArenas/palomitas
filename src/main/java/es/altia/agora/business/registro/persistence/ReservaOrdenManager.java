// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.registro.ReservaOrdenValueObject;
import es.altia.agora.business.registro.persistence.manual.ReservaOrdenDAO;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DiligenciasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ReservaOrdenManager {

  private static ReservaOrdenManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(ReservaOrdenManager.class.getName());          

  protected ReservaOrdenManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ReservaOrdenManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ReservaOrdenManager.class) {
        if (instance == null) {
          instance = new ReservaOrdenManager();
        }
      }
    }
    return instance;
  }

  public void insert(ReservaOrdenValueObject reserva,String[] params){
   // queremos estar informados de cuando este metodo es ejecutado
   m_Log.debug("insertReservaOrdenValue");
   m_Log.debug("Pasa por el insert del manager");
   ReservaOrdenDAO reservaDAO = ReservaOrdenDAO.getInstance();
   try{
     m_Log.debug("Usando persistencia manual");
     m_Log.debug("Pasa por el try del insert del manager");
     reservaDAO.insert(reserva,params);
     // queremos estar informados de cuando este metodo finaliza
     m_Log.debug("insertReservaOrdenValue");
   }catch(Exception ce){
     m_Log.error("JDBC Technical problem " + ce.getMessage());
   }
  }

  public void loadRER(ReservaOrdenValueObject reserva,String[] params) {
   m_Log.debug("loadReservaOrdenValue");
   m_Log.debug("Pasa por el load del manager");
   ReservaOrdenDAO reservaDAO = ReservaOrdenDAO.getInstance();
   try{
     m_Log.debug("Usando persistencia manual");
     m_Log.debug("Pasa por el try del load del manager");
     reservaDAO.loadRER(reserva,params);
     // queremos estar informados de cuando este metodo finaliza
     m_Log.debug("insertReservaOrdenValue");
   }catch(Exception ce){
     m_Log.error("JDBC Technical problem " + ce.getMessage());
   }
  }

  /**
   * Recupera las reservas hasta cierta fecha.
   * @param reservaVO vo con los datos del registro y la fecha
   * @param params parametros de conexion a BD
   * @return vector con las reservas recuperadas
   * @throws Exception
   */
  public Vector<ReservaOrdenValueObject> cargarReservasPorFecha(
              ReservaOrdenValueObject reservaVO,String[] params) 
              throws Exception {
      m_Log.debug("ReservaOrdenManager.cargarReservasPorFecha");
      ReservaOrdenDAO reservaDAO = ReservaOrdenDAO.getInstance();
      Vector<ReservaOrdenValueObject> reservas = new Vector<ReservaOrdenValueObject>();
      try{
          reservas = reservaDAO.cargarReservasPorFecha(reservaVO, params);
      }catch(Exception ce){
          throw ce;
      }
      return reservas;
  }
  
  /**
   * Anula las reservas del vector con la diligencia que se indica. Para cada una
   * se crea una anotación en R_RES con estado anulado y se anota la anulacion
   * en el histórico.
   * @param reservas reservas a anular
   * @param diligencia diligencia de anulación
   * @param usuario usuario que realiza la anulación
   * @param params parametros de conexion a BD
   * @throws java.lang.Exception
   */
  public void anularReservas(Vector<ReservaOrdenValueObject> reservas, String diligencia, 
          UsuarioValueObject usuario, String[] params) 
          throws Exception {
      m_Log.debug("ReservaOrdenManager.anularReservas");
      ReservaOrdenDAO reservaDAO = ReservaOrdenDAO.getInstance();
      try{
          reservaDAO.anularReservas(reservas, diligencia, usuario, params);
      }catch(Exception ce){
          throw ce;
      }
  }
}