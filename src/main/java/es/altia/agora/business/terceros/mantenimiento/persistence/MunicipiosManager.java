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
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: MunicipiosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class MunicipiosManager  {
  private static MunicipiosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(MunicipiosManager.class.getName());

  protected MunicipiosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static MunicipiosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(MunicipiosManager.class) {
        if (instance == null) {
          instance = new MunicipiosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaMunicipios(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaMunicipiosValue");
    MunicipiosDAO municipiosDAO = MunicipiosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = municipiosDAO.getListaMunicipios(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaMunicipiosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarMunicipio(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarMunicipio");
    MunicipiosDAO municipiosDAO = MunicipiosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = municipiosDAO.eliminarMunicipio(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarMunicipio");
    return resultado;
  }

  public Vector modificarMunicipio(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarMunicipio");
    MunicipiosDAO municipiosDAO = MunicipiosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = municipiosDAO.modificarMunicipio(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarMunicipio");
    return resultado;
  }

  public Vector altaMunicipio(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaMunicipio");
    MunicipiosDAO municipiosDAO = MunicipiosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = municipiosDAO.altaMunicipio(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaMunicipio");
    return resultado;
  }
}