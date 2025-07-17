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
 * <p>Descripción: CodPostalesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class CodPostalesManager  {
  private static CodPostalesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(CodPostalesManager.class.getName());


  protected CodPostalesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static CodPostalesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(CodPostalesManager.class) {
        if (instance == null) {
          instance = new CodPostalesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaCodPostales(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaCodPostalesValue");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = codPostalesDAO.getListaCodPostales(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaCodPostalesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
 public Vector getListaCodPostalesDesdeMantenimiento(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaCodPostalesValue");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = codPostalesDAO.getListaCodPostalesDesdeMantenimiento(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaCodPostalesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarCodPostal(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarCodPostal");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = codPostalesDAO.eliminarCodPostal(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarCodPostal");
    return resultado;
  }

   public Vector eliminarCodPostalDesdeMantenimiento(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarCodPostal");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = codPostalesDAO.eliminarCodPostalDesdeMantenimiento(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarCodPostal");
    return resultado;
  }
  public Vector modificarCodPostal(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarCodPostal");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = codPostalesDAO.modificarCodPostal(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarCodPostal");
    return resultado;
  }

  public Vector modificarCodPostalDesdeMantenimiento(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarCodPostal");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = codPostalesDAO.modificarCodPostalDesdeMantenimiento(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarCodPostal");
    return resultado;
  }
  public Vector altaCodPostal(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaCodPostal");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = codPostalesDAO.altaCodPostal(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaCodPostal");
    return resultado;
  }
  
  public Vector altaCodPostalDesdeMantenimiento(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaCodPostal");
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = codPostalesDAO.altaCodPostalDesdeMantenimiento(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaCodPostal");
    return resultado;
  }
   public boolean existeCodPostal(GeneralValueObject gVO, String[] params){
      //queremos estar informados de cuando este metodo es ejecutado
    log.debug("existeCodigoPostal");
    boolean resultado=false;
    CodPostalesDAO codPostalesDAO = CodPostalesDAO.getInstance();
    String codigo =(String) gVO.getAtributo("descPostal");
    
    
    
    try{
        resultado = codPostalesDAO.existeCodPostal(params,codigo);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("existeCodPostal");
    return resultado;
  }
}