// NOMBRE DEL PAQUETE
package es.altia.agora.business.formularios.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.formularios.persistence.manual.FormulariosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: EntidadesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class FormulariosManager  {
  private static FormulariosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(FormulariosManager.class.getName());

  protected FormulariosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static FormulariosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(FormulariosManager.class) {
        if (instance == null) {
          instance = new FormulariosManager();
        }
      }
    }
    return instance;
  }

    public Vector getListaFormularios(GeneralValueObject gVO,String[] params){
      log.debug("getListaUORs");
      FormulariosDAO formulariosDAO = FormulariosDAO.getInstance();
      Vector resultado = new Vector();
      try{
        log.debug("Usando persistencia manual");
        resultado = formulariosDAO.getListaFormularios(gVO,params);
        return resultado;
      }catch(Exception ce){
        log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
    }

    public Vector getListaFormulariosTipo0(String[] params){
      log.debug("getListaUORs");
      FormulariosDAO formulariosDAO = FormulariosDAO.getInstance();
      Vector resultado = new Vector();
      try{
        log.debug("Usando persistencia manual");
        resultado = formulariosDAO.getListaFormulariosTipo0(params);
        return resultado;
      }catch(Exception ce){
        log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
    }

    public Vector getListaFormulariosTipo0Restricciones(String[] params, String loginUsuario, String cargo){
      log.debug("getListaUORs");
      FormulariosDAO formulariosDAO = FormulariosDAO.getInstance();
      Vector resultado = new Vector();
      try{
        log.debug("Usando persistencia manual");
        resultado = formulariosDAO.getListaFormulariosTipo0Restricciones(params, loginUsuario, cargo);
        return resultado;
      }catch(Exception ce){
        log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
    }

    public Vector getListaFormulariosSinParametros(String[] params){
      log.debug("getListaUORs");
      FormulariosDAO formulariosDAO = FormulariosDAO.getInstance();
      Vector resultado = new Vector();
      try{
        log.debug("Usando persistencia manual");
        resultado = formulariosDAO.getListaFormulariosSinParametros(params);
        return resultado;
      }catch(Exception ce){
        log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
    }

}