package es.altia.agora.business.gestionInformes.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.AmbitoListaValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.gestionInformes.persistence.manual.MantenimientoInformesDAO;
import es.altia.agora.business.gestionInformes.persistence.manual.SolicitudesInformesDAO;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

public class MantenimientoInformesManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static MantenimientoInformesManager instance = null;

  protected static Config m_ConfigTechnical;
  protected static Config m_ConfigCommon;
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(SolicitudesInformesManager.class.getName());


  protected MantenimientoInformesManager() {
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    m_ConfigError = ConfigServiceHelper.getConfig("error");
    m_ConfigCommon = ConfigServiceHelper.getConfig("common");
  }


  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de FichaExpedienteManager
  */
  public static MantenimientoInformesManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    if (instance == null) {
        synchronized(MantenimientoInformesManager.class) {
        if (instance == null)
            instance = new MantenimientoInformesManager();
      }
    }
    return instance;
  }

  
    public void modificarOrigen(ElementoListaValueObject siVO, String[] params) throws Exception{
      long resultado = 0;
        //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("modificarOrigen");

      try{
         MantenimientoInformesDAO.getInstance().modificarOrigen(siVO, params);
      }catch (Exception e) {
        resultado = -1;
        throw e;
      }
    }
    public void insertarOrigen(ElementoListaValueObject siVO, String[] params) throws Exception{
        long resultado = 0;
          //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("insertarOrigen");

        try{
           MantenimientoInformesDAO.getInstance().insertarOrigen(siVO, params);
        }catch (Exception e) {
          resultado = -1;
          throw e;
        }
      }
	public void insertarModoOrigen(ElementoListaValueObject siVO, String[] params) throws Exception{
	    long resultado = 0;
	      //queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("insertarOrigen");
	
	    try{
	       MantenimientoInformesDAO.getInstance().insertarModoOrigen(siVO, params);
	    }catch (Exception e) {
	      resultado = -1;
	      throw e;
	    }
	  }
	public Integer insertarCampo(AmbitoListaValueObject siVO, String[] params) throws Exception{
	    Integer resultado = 0;
	      //queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("insertarOrigen");
	
	    try{
	       resultado=MantenimientoInformesDAO.getInstance().insertarCampo(siVO, params);
	    }catch (Exception e) {
	      resultado = -1;
	      throw e;
	    }
	    return resultado;
	  }
   public void modificarCampo(AmbitoListaValueObject siVO, String[] params) throws Exception{
	      long resultado = 0;
	        //queremos estar informados de cuando este metodo es ejecutado
	      m_Log.debug("modificarOrigen");

	      try{
	         MantenimientoInformesDAO.getInstance().modificarCampo(siVO, params);
	      }catch (Exception e) {
	        resultado = -1;
	        throw e;
	      }
	    }
  public String eliminarCampo(String desc,String nom,Integer codigo, String[] params) throws Exception{
        long resultado = 0;
        String result=null;
          //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("insertarOrigen");

        try{
           result=MantenimientoInformesDAO.getInstance().eliminarCampo(desc,nom,codigo, params);
        }catch (Exception e) {
          resultado = -1;
          throw e;
        }
        return result; 
      }
	
    public void modificarModoOrigen(ElementoListaValueObject siVO, String[] params) throws Exception{
        long resultado = 0;
          //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("insertarOrigen");

        try{
           MantenimientoInformesDAO.getInstance().modificarModoOrigen(siVO, params);
        }catch (Exception e) {
          resultado = -1;
          throw e;
        }
      }
    public void eliminarModoOrigen(Integer codigo, String[] params) throws Exception{
        long resultado = 0;
          //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("insertarOrigen");

        try{
           MantenimientoInformesDAO.getInstance().eliminarModoOrigen(codigo, params);
        }catch (Exception e) {
          resultado = -1;
          throw e;
        }
      }
    public void eliminarOrigen(Integer codigo, String[] params) throws Exception{
        long resultado = 0;
          //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("elimiarOrigen");

        try{
           MantenimientoInformesDAO.getInstance().eliminarOrigen(codigo, params);
        }catch (Exception e) {
          resultado = -1;
          throw e;
        }
      }

}
