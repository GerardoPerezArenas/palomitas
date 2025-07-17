// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.DepartamentosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DepartamentosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class DepartamentosManager  {
  private static DepartamentosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(DepartamentosManager.class.getName());
    

  protected DepartamentosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static DepartamentosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(DepartamentosManager.class) {
        if (instance == null) {
          instance = new DepartamentosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaDepartamentos(String[] params){
    DepartamentosDAO departamentosDAO = DepartamentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = departamentosDAO.getListaDepartamentos(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarDepartamento(String codigo, String[] params) {
    DepartamentosDAO departamentoDAO = DepartamentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = departamentoDAO.eliminarDepartamento(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarDepartamento(GeneralValueObject gVO, String[] params){
    DepartamentosDAO departamentoDAO = DepartamentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = departamentoDAO.modificarDepartamento(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaDepartamento(GeneralValueObject gVO, String[] params) {
    DepartamentosDAO departamentoDAO = DepartamentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = departamentoDAO.altaDepartamento(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}