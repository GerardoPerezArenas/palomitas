// NOMBRE DEL PAQUETE
package es.altia.agora.business.formularios.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.formularios.persistence.manual.FichaFormularioDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm;
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

public class FichaFormularioManager  {
  private static FichaFormularioManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(FichaFormularioManager.class.getName());

  public FichaFormularioManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static FichaFormularioManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(FichaFormularioManager.class) {
        if (instance == null) {
          instance = new FichaFormularioManager();
        }
      }
    }
    return instance;
  }

  public Vector eliminarFormulario(String codForm, String[] params) {
    log.debug("eliminarFormulario");
    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = formularioDAO.eliminarFormulario(codForm,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public GeneralValueObject cargarFormulario(String codForm, String[] params, String organizacion) {
    log.debug("cargarFormulario");
    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance(organizacion);
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      resultado = formularioDAO.cargarFormulario(codForm,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

    public Vector cargarAreasPorDefecto(String[] params) {
      log.debug("cargarAreasPorDefecto");
      FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
      Vector resultado = new Vector();
      try{
        resultado = formularioDAO.cargarAreasPorDefecto(params);
      }catch (Exception e){
        log.error("Excepción capturada: " + e.toString());
      }
      return resultado;
    }

    public Vector cargarTramitesFormulario(String codForm, int municipio, String[] params) {
      log.debug("cargarTramitesFormulario");
      FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
      Vector resultado = new Vector();
      try{
        resultado = formularioDAO.cargarTramitesFormulario(codForm, municipio, params);
      }catch (Exception e){
        log.error("Excepción capturada: " + e.toString());
      }
      return resultado;
    }

    public Vector cargarRestriccionesFormulario(String codForm, String[] params) {
      log.debug("cargarRestriccionesFormulario");
      FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
      Vector resultado = new Vector();
      try{
        resultado = formularioDAO.cargarRestriccionesFormulario(codForm, params);
      }catch (Exception e){
        log.error("Excepción capturada: " + e.toString());
      }
      return resultado;
    }

    public byte[] getFicheroFormulario(String codigo, String tipo, String[] params){
      log.debug("cargarFichero");
      FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
      byte[] resultado = null;
      try{
        resultado = formularioDAO.getFicheroFormulario(codigo, tipo, params);
      }catch(Exception e){
        log.error("Excepcion capturada: " + e.toString());
      }
      return resultado;
    }
    
     public String validarVisualizacionFichero(String codigo, String tipo, String[] params, String organizacion){
      log.debug("cargarFichero");
      FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance(organizacion);
      String resultado = null;
      try{
        resultado = formularioDAO.validarVisualizacionFichero(codigo, tipo, params);
      }catch(Exception e){
        log.error("Excepcion capturada: " + e.toString());
      }
      return resultado;
    }

    public Vector modificarFormulario(GeneralValueObject gVO, String[] params){
      log.debug("modificarRelacion");
      FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
      Vector resultado = new Vector();
      try{
        resultado = formularioDAO.modificarFormulario(gVO, params);
      }catch(Exception e){
        log.error("Excepcion capturada: " + e.toString());
      }
      return resultado;
    }

    public Vector modificarFormularioFichero(FichaFormularioForm form, String[] params){
      log.debug("modificarRelacion");
      FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
      Vector resultado = new Vector();
      try{
        resultado = formularioDAO.modificarFormularioFichero(form, params);
      }catch(Exception e){
        log.error("Excepcion capturada: " + e.toString());
      }
      return resultado;
    }

  public Vector altaFormulario(GeneralValueObject gVO, String[] params) {
    log.debug("altaRelacion");
    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = formularioDAO.altaFormulario(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector getListaPrecargas(String organizacion) {
	    log.debug("getListaPrecargas");
	    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance(organizacion);
	    Vector resultado = new Vector();
	    try{
	      resultado = formularioDAO.getListaPrecargas();
	    }catch (Exception e) {
	      log.error("Excepción capturada: " + e.toString());
	    }
	    return resultado;
	  }

  public Vector getListaPrecargas() {
	    log.debug("getListaPrecargas");
	    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
	    Vector resultado = new Vector();
	    try{
	      resultado = formularioDAO.getListaPrecargas();
	    }catch (Exception e) {
	      log.error("Excepción capturada: " + e.toString());
	    }
	    return resultado;
	  }
  
  public String getCodNoVisible(String codigoForm, String[] params) {
	    log.debug("getCodNoVisible");
	    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
	    String resultado = "";
	    try{
	      resultado = formularioDAO.getCodNoVisible(codigoForm, params);
	    }catch (Exception e) {
	      log.error("Excepción capturada: " + e.toString());
	    }
	    return resultado;
	  }
  
  public Vector getPrecargasFormulario(String codForm, String[] params) {
	    log.debug("getPrecargasSeleccionadas");
	    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
	    Vector resultado = new Vector();
	    try{
	      resultado = formularioDAO.getPrecargasFormulario(codForm,params);
	    }catch (Exception e) {
	      log.error("Excepción capturada: " + e.toString());
	    }
	    return resultado;
	  }
  
  public int setPrecargasFormulario(String codForm, Vector listaPrecargas, String[] params) {
	    log.debug("getPrecargasSeleccionadas");
	    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
	    int resultado = 0;
	    try{
	      resultado=formularioDAO.setPrecargasFormulario(codForm,listaPrecargas,params);
	      return resultado;
	    }catch (Exception e) {
	      log.error("Excepción capturada: " + e.toString());
	    }
	    return resultado;
	  }
  
  public int eliminarPrecargas(String codForm, String[] params) {
	    log.debug("getPrecargasSeleccionadas");
	    FichaFormularioDAO formularioDAO = FichaFormularioDAO.getInstance();
	    int resultado = 0;
	    try{
	      resultado=formularioDAO.eliminarPrecargas(codForm,params);
	      return resultado;
	    }catch (Exception e) {
	      log.error("Excepción capturada: " + e.toString());
	    }
	    return resultado;
	  }

}