// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.IdiomasDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.LabelValueTO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.ArrayList;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: IdiomasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class IdiomasManager  {
  private static IdiomasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(IdiomasManager.class.getName());

  protected IdiomasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static IdiomasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(IdiomasManager.class) {
        if (instance == null) {
          instance = new IdiomasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaIdiomas(String[] params){
    IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = idiomaDAO.getListaIdiomas(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  
  /**
   * Devuelve una colección de objetos GeneralValueTO con el código y descripción de cada idioma recuperado
   * de la base de datos
   * @param params: Array de String con los parámetros de la conexión
   * @param idIdioma: Id del idioma seleccionado por el usuario y que no deseamos
   * @return Coleccion de GeneralValueTO
   */
   public ArrayList<LabelValueTO> getListIdiomasLabel(String[] params,int idIdioma){
    IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
    ArrayList<LabelValueTO> resultado = new ArrayList<LabelValueTO>();
    
    try{     
      resultado = idiomaDAO.getListaIdiomasLabel(params,idIdioma);               
      
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());      
    }    
    
    return resultado;
  }
   
   
    /**
   * Recupera la descripción de un idioma de la bd
   * @param params: Array de String con los parámetros de la conexión
   * @param idIdioma: Id del idioma 
   * @return String
   */
   public String getDescripcionIdioma(String[] params,int idIdioma){
        IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
        String resultado = "";

        try{     
            resultado = idiomaDAO.getDescripcionIdioma(params,idIdioma);               

        }catch(Exception ce){
          log.error("JDBC Technical problem " + ce.getMessage());      
        }    

        return resultado;
  }

    /**
   * Recupera la descripción de un idioma de la bd
   * @param params: Array de String con los parámetros de la conexión
   * @param idIdioma: Id del idioma
   * @return String
   */
   public String getClaveIdioma(String[] params,int idIdioma){
        IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
        String resultado = "";

        try{
            resultado = idiomaDAO.getClaveIdioma(params,idIdioma);

        }catch(Exception ce){
          log.error("JDBC Technical problem " + ce.getMessage());
        }

        return resultado;
  }

  public Vector buscarUsuarios(String codigo, String[] params) {
	IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
	Vector resultado = new Vector();
	try{
	  resultado = idiomaDAO.buscarUsuarios(codigo,params);
	}catch (Exception e){
	  log.error("Excepción capturada: " + e.toString());
	}
	return resultado;
  }

  public Vector eliminarIdioma(String codigo, String[] params) {
    IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = idiomaDAO.eliminarIdioma(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  	public Vector eliminarIdiomaConUsuarios(String codigoViejo, String codigoNuevo, String[] params) {
  		IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
  		Vector resultado = new Vector();
  		try {
  			resultado = idiomaDAO.eliminarIdiomaConUsuarios(codigoViejo, codigoNuevo, params);
  		} catch (Exception e) {
  			log.error("Excepción capturada: " + e.toString());
	    }
	    return resultado;
  	}

  public Vector modificarIdioma(GeneralValueObject gVO, String[] params){
    IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = idiomaDAO.modificarIdioma(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaIdioma(GeneralValueObject gVO, String[] params) {
    IdiomasDAO idiomaDAO = IdiomasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = idiomaDAO.altaIdioma(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}