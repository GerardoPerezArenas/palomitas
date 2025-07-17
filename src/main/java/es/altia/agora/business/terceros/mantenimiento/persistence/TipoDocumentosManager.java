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
 * <p>Descripción: TipoDocumentosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TipoDocumentosManager  {
  private static TipoDocumentosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TipoDocumentosManager.class.getName());

  protected TipoDocumentosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TipoDocumentosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TipoDocumentosManager.class) {
        if (instance == null) {
          instance = new TipoDocumentosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTipoDocumentos(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaTipoDocumentosValue");
    TipoDocumentosDAO tipoDocumentosDAO = TipoDocumentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tipoDocumentosDAO.getListaTipoDocumentos(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaTipoDocumentosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarTipoDocumento(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarTipoDocumento");
    TipoDocumentosDAO tipoDocumentosDAO = TipoDocumentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoDocumentosDAO.eliminarTipoDocumento(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarTipoDocumento");
    return resultado;
  }

  public Vector modificarTipoDocumento(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarTipoDocumento");
    TipoDocumentosDAO tipoDocumentosDAO = TipoDocumentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoDocumentosDAO.modificarTipoDocumento(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarTipoDocumento");
    return resultado;
  }

  public Vector altaTipoDocumento(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaTipoDocumento");
    TipoDocumentosDAO tipoDocumentosDAO = TipoDocumentosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoDocumentosDAO.altaTipoDocumento(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaTipoDocumento");
    return resultado;
  }
  
  public String getByPrimaryKey(String[] params,String codigo)
    {
	//queremos estar informados de cuando este metodo es ejecutado
	log.debug("getByPrimaryKey");
	String r = "";
	try{
	   r = TipoDocumentosDAO.getInstance().getByPrimaryKey(params,codigo);
	}
	catch (Exception e){
	   log.error("Excepción capturada: " + e.toString());
	}
	log.debug("getByPrimaryKey");
	return r;
   }
    
  public Vector getListaTipoDocumentos(String[] params,String parametro,String campo)
    {
	//queremos estar informados de cuando este metodo es ejecutado
	log.debug("getListaTipoDocumentos");
	Vector r = new Vector();
	try{
	   r = TipoDocumentosDAO.getInstance().getListaTipoDocumentos(params,parametro,campo);
	}
	catch (Exception e){
	   log.error("Excepción capturada: " + e.toString());
	}
	log.debug("getListaTipoDocumentos");
	return r;
   }     
}