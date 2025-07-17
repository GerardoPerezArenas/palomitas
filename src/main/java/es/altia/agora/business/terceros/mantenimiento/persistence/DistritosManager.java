// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.*;
import es.altia.agora.business.util.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;

/**
 * <p>T�tulo: Proyecto @gora</p>
 * <p>Descripci�n: DistritosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DistritosManager  {
  private static DistritosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion t�cnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(DistritosManager.class.getName());   

  protected DistritosManager() {
    // Queremos usar el fichero de configuraci�n technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static DistritosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizaci�n aqu� para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(DistritosManager.class) {
        if (instance == null) {
          instance = new DistritosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaDistritos(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaDistritosValue");
    DistritosDAO distritosDAO = DistritosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = distritosDAO.getListaDistritos(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaDistritosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarDistrito(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarDistrito");
    DistritosDAO distritosDAO = DistritosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = distritosDAO.eliminarDistrito(gVO,params);
    }catch (Exception e){
      log.error("Excepci�n capturada: " + e.toString());
    }
    log.debug("eliminarDistrito");
    return resultado;
  }

  public Vector modificarDistrito(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarDistrito");
    DistritosDAO distritosDAO = DistritosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = distritosDAO.modificarDistrito(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarDistrito");
    return resultado;
  }

  public Vector altaDistrito(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaDistrito");
    DistritosDAO distritosDAO = DistritosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = distritosDAO.altaDistrito(gVO,params);
    }catch (Exception e) {
      log.error("Excepci�n capturada: " + e.toString());
    }
    log.debug("altaDistrito");
    return resultado;
  }
}