package es.altia.agora.business.gestionInformes.persistence;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.gestionInformes.persistence.manual.SolicitudesInformesDAO;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;

public class SolicitudesInformesManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static SolicitudesInformesManager instance = null;

  protected static Config m_ConfigTechnical;
  protected static Config m_ConfigCommon;
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(SolicitudesInformesManager.class.getName());


  protected SolicitudesInformesManager() {
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    m_ConfigError = ConfigServiceHelper.getConfig("error");
    m_ConfigCommon = ConfigServiceHelper.getConfig("common");
  }


  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de FichaExpedienteManager
  */
  public static SolicitudesInformesManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    if (instance == null) {
        synchronized(SolicitudesInformesManager.class) {
        if (instance == null)
            instance = new SolicitudesInformesManager();
      }
    }
    return instance;
  }

    public Vector getListaSolicitudes(UsuarioValueObject usuario, String[] params, String origen) {
      Vector lista = new Vector();
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaSolicitudes");

      try{
        lista = SolicitudesInformesDAO.getInstance().getListaSolicitudes(usuario, params, origen);
      }catch (Exception e) {
        e.printStackTrace();
      }
      return lista;
    }

    public SolicitudInformeValueObject obtenerSolicitud(String[] params, String codSolicitud) {
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("obtenerSolicitud");
      SolicitudInformeValueObject siVO = new SolicitudInformeValueObject();
      try{
        siVO = SolicitudesInformesDAO.getInstance().obtenerSolicitud(params, codSolicitud);
      }catch (Exception e) {
        e.printStackTrace();
      }
      return siVO;
    }

    public long grabarSolicitud(SolicitudInformeValueObject siVO, String[] params) throws Exception{
      long resultado = 0;
        //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("grabarSolicitud");

      try{
        resultado = SolicitudesInformesDAO.getInstance().grabarSolicitud(siVO, params);
      }catch (Exception e) {
        resultado = -1;
        throw e;
      }
      return resultado;
    }

    public void anotaInicioSolicitud(String[] params, String codSolicitud, int estado) throws Exception{
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("anotaInicioSolicitud");

      try{
        SolicitudesInformesDAO.getInstance().anotaInicioSolicitud(params,codSolicitud, estado);
      }catch (Exception e) {
        throw e;
      }
    }

    public void anotaFinSolicitud(String[] params, String codSolicitud, long tiempo, String fichero, int estado) throws Exception{
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("anotaFinSolicitud");

      try{
        SolicitudesInformesDAO.getInstance().anotaFinSolicitud(params, codSolicitud, tiempo, fichero, estado);
      }catch (Exception e) {
        throw e;
      }
    }

    public void eliminarSolicitud(String[] params, String codSolicitud) {
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("eliminarSolicitud");
      SolicitudInformeValueObject siVO = new SolicitudInformeValueObject();
      try{
        SolicitudesInformesDAO.getInstance().eliminarSolicitud(params, codSolicitud);
      }catch (Exception e) {
        e.printStackTrace();
      }
    }
}
