package es.altia.agora.business.gestionInformes.persistence;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.gestionInformes.persistence.manual.GestionInformesDAO;
import es.altia.agora.business.gestionInformes.InformeValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;

public class GestionInformesManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static GestionInformesManager instance = null;

  protected static Config m_ConfigTechnical;
  protected static Config m_ConfigCommon;
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(GestionInformesManager.class.getName());


  protected GestionInformesManager() {
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    m_ConfigError = ConfigServiceHelper.getConfig("error");
    m_ConfigCommon = ConfigServiceHelper.getConfig("common");
  }


  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de FichaExpedienteManager
  */
  public static GestionInformesManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    if (instance == null) {
        synchronized(GestionInformesManager.class) {
        if (instance == null)
            instance = new GestionInformesManager();
      }
    }
    return instance;
  }

    public Vector getListaInformes(String[] params) {
      Vector lista = new Vector();
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaInformes");

      try{
        lista = GestionInformesDAO.getInstance().getListaInformes(params);
      }catch (Exception e) {
        e.printStackTrace();
      }
      return lista;
    }

    public Vector getListaInformesPublicados(UsuarioValueObject usuario, String origen) {
      Vector lista = new Vector();
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaInformesPublicados");

      try{
        lista = GestionInformesDAO.getInstance().getListaInformesPublicados(usuario, origen);
      }catch (Exception e) {
        e.printStackTrace();
      }
      return lista;
    }

    public Vector getListaInformes(GeneralValueObject gVO, String[] params) {
      Vector lista = new Vector();
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaInformes");

      try{
        lista = GestionInformesDAO.getInstance().getListaInformes(gVO, params);
      }catch (Exception e) {
        e.printStackTrace();
      }
      return lista;
    }

    public Vector getTipoCamposInformes(String[] params) {
      if(m_Log.isDebugEnabled()) m_Log.debug("getTipoCamposInformes() : BEGIN");
      Vector lista = new Vector();
      try{
        lista = GestionInformesDAO.getInstance().getTipoCamposInformes(params);
      }catch (Exception e) {
        e.printStackTrace();
      }//try-catch
      if(m_Log.isDebugEnabled()) m_Log.debug("getTipoCamposInformes() : END");
      return lista;
    }//getTipoCamposInformes

    public Vector getListaAmbitos(String[] params) {
      Vector lista = new Vector();
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaAmbitos");

      try{
        lista = GestionInformesDAO.getInstance().getListaAmbitos(params);
      }catch (Exception e) {
        e.printStackTrace();
      }
      return lista;
    }
    public Vector getListaModoAmbitos(String[] params) {
        Vector lista = new Vector();
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getListaAmbitos");

        try{
          lista = GestionInformesDAO.getInstance().getListaModoAmbitos(params);
        }catch (Exception e) {
          e.printStackTrace();
        }
        return lista;
      }
    public Vector getListaCampos(String codigoAmbito,String[] params) {
        Vector lista = new Vector();
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getListaAmbitos");

        try{
          lista = GestionInformesDAO.getInstance().getListaCampos(codigoAmbito,params);
        }catch (Exception e) {
          e.printStackTrace();
        }
        return lista;
      }

    public String getOrigenPlantilla(String[] params, String idPlantilla) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getOrigenPlantilla() BEGIN");
        String origen = "";
        try{
            origen = GestionInformesDAO.getInstance().getOrigenPlantilla(params, idPlantilla);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("getOrigenPlantilla() END");
        return origen;
    }
    
    public boolean hayPermiso (String codPlantilla, String[] params){    
        boolean hay = false;
        hay = GestionInformesDAO.getInstance().hayPermiso(codPlantilla,params);
        return hay;
    }
}
