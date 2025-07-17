package es.altia.agora.business.registro.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.registro.persistence.manual.InformesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class InformesManager {
   private static InformesManager instance = null;
   protected static Config m_ct; // Para el fichero de configuracion técnico
   protected static Config m_ConfigError; // Para los mensajes de error localizados
   protected static Log m_Log =
           LogFactory.getLog(InformesManager.class.getName());           

   protected InformesManager() {
	super();
	// Queremos usar el fichero de configuracion techserver
	m_ct = ConfigServiceHelper.getConfig("techserver");
	// Queremos tener acceso a los mensajes de error localizados
	m_ConfigError = ConfigServiceHelper.getConfig("error");
   }

   public static InformesManager getInstance() {
	// si no hay ninguna instancia de esta clase tenemos que crear una
	if (instance == null) {
	   // Necesitamos sincronizacion para serializar (no multithread)
	   // Las invocaciones de este metodo
	   synchronized(InformesManager.class) {
		if (instance == null) {
		   instance = new InformesManager();
		}
	   }
	}
	return instance;
   }

   public Vector getDias(String[] params,
				 GeneralValueObject parametrosConsultaVO, int t){
	// queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("informeLibroRegistro");
	InformesDAO infDAO = InformesDAO.getInstance();
	Vector dias = null;
	try{
	   m_Log.debug("Usando persistencia manual");
	   dias=infDAO.getDias(params, parametrosConsultaVO,t);
	   // queremos estar informados de cuando este metodo finaliza
	   m_Log.debug("informeLibroRegistro");
	}catch(Exception ce){
	   m_Log.error("JDBC Technical problem " + ce.getMessage());
	}
	return dias;
   }
   
   /* INFORMES GESTION */
   public Vector getEstadisticas(String[] params, UsuarioValueObject usuario){

	   m_Log.debug("getEstadisticas");
	   InformesDAO infDAO = InformesDAO.getInstance();
	   Vector e = null;
	   try{		  
		  e=infDAO.getEstadisticas(params, usuario);		  
	   }catch(Exception ce){
		  m_Log.error("JDBC Technical problem " + ce.getMessage());
	   } finally {	   
	   		m_Log.debug("getEstadisticas");
	   		return e;
	  }
   }
/* Obtine los códigos de todas las unidades*/
  public Vector getTodasUnidades(String[] params,GeneralValueObject parametrosConsultaVO)
  {
      m_Log.debug("getTodasUnidades");
      InformesDAO infDAO = InformesDAO.getInstance();
      Vector e = null;
	  try{
		  e=infDAO.getTodasUnidades(params,parametrosConsultaVO);
	  }catch(Exception ce){
		  m_Log.error("JDBC Technical problem " + ce.getMessage());
	  } finally {
	  	m_Log.debug("getTodasUnidades");
	    return e;
	  }
  }


   /**
     * Devuelve los codigos de las organizaciones externas
     * @param parametrosConsultaVO parametros necesarios para filtrar las consultas.
     * @param codOrganizacion Codigo de la organizacion externa.(0: todas las organizaciones externas).
     * @param params Parametros de conexion a la BD.
     * @return lista con los codigos de las organizaciones externas.
     */
    public Vector getOrganizacionesExternas(GeneralValueObject parametrosConsultaVO, String codOrganizacion, String[] params) {
        m_Log.debug("getOrganizacionesExternas");
        InformesDAO infDAO = InformesDAO.getInstance();
        Vector e = new Vector();
        try {
            e = infDAO.getOrganizacionesExternas(parametrosConsultaVO, codOrganizacion, params);

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        } finally {
            m_Log.debug("getOrganizacionesExternas");
            return e;
        }
    }



    /**
     * Devuelve los codigos de las unidades organicas externas
     * @param parametrosConsultaVO parametros necesarios para filtrar las consultas.
     * @param codOrganizacion Codigo de la organizacion externa.
     * @param codUnidadOrganica Codigo de la unidad organica externa. (0: todas las unidades de una organizacion externa).
     * @param params Parametros de conexion a la BD.
     * @return lista con los codigos de las unidades organicas externas.
     */
    public Vector getUnidadesExternas(GeneralValueObject parametrosConsultaVO, String codOrganizacion, String codUnidadOrganica, String[] params) {
        m_Log.debug("getUnidadesExternas");
        InformesDAO infDAO = InformesDAO.getInstance();
        Vector e = new Vector();
        try {
            e = infDAO.getUnidadesExternas(parametrosConsultaVO, codOrganizacion, codUnidadOrganica, params);

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        } finally {
            m_Log.debug("getUnidadesExternas");
            return e;
        }
    }


  public String getUORPorCodigoVisual(String[] params, String codigoVisual)
   {
      InformesDAO infDAO = InformesDAO.getInstance();
      String e = null;
      try{
		  e=infDAO.getUORPorCodigoVisual(codigoVisual,params);
	  }catch(Exception ce){
		  m_Log.error("JDBC Technical problem " + ce.getMessage());
	  } finally {
	  	m_Log.debug("getTodasUnidades");
	    return e;
	  }
   }
   
  public String getClaveIdioma(String[] params, String idioma)
   {
      InformesDAO infDAO = InformesDAO.getInstance();
      String e = null;
      try{
		  e=infDAO.getClaveIdioma(idioma,params);
	  }catch(Exception ce){
		  m_Log.error("JDBC Technical problem " + ce.getMessage());
	  } finally {
	  	m_Log.debug("getTodasUnidades");
	    return e;
	  }
   }
  
  
  
  
 /**

     * Devuelve listado de las anotaciones de Libro de Registro
     * @param consulta parametros necesarios para realizar la consulta.
     * @param params Parametros de conexion a la BD.
     * @return listado de anotaciones a mostar en el libro de registro.
     */
    public Vector getListadoInformeRegistro(String consulta, String[] params){
        Vector<RegistroValueObject> listado = new Vector<RegistroValueObject>();
        InformesDAO infDAO = InformesDAO.getInstance();
 
        try{
             listado=infDAO.getListadoInformeRegistro(consulta, params);
        }catch(Exception ce){
             m_Log.error("JDBC Technical problem "+ ce.getMessage());
        } finally{
            m_Log.debug("getListadoInformeRegistro");
             return listado;
        }
    }
}