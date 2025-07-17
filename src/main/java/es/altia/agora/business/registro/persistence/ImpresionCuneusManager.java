package es.altia.agora.business.registro.persistence;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

public abstract class ImpresionCuneusManager {

	  private static ImpresionCuneusManager instance = null;
	  
	  protected static Config m_ConfigRegistro =  
		      ConfigServiceHelper.getConfig("Registro");
	  protected static Log m_Log =
	          LogFactory.getLog(ImpresionCuneusManager.class.getName());

	  protected ImpresionCuneusManager() {}

	  /**
	  * Factory method para el <code>Singleton</code>.
	  * @return Instancia unica de la clase indicada en Registro.properties
	  */
	  public static ImpresionCuneusManager getInstance() throws AnotacionRegistroException{	
		  try { 
		    if (instance == null) {
	          synchronized(ImpresionCuneusManager.class) {
	            if (instance == null) {
	              //Buscamos la clase a instanciar segun Registro.properties.
	    	      String tipo = m_ConfigRegistro.getString("Registro/TipoImpresion");
	    	      String implClassName = m_ConfigRegistro.getString("Registro/TipoImpresion/" + tipo + "/implClassName");
	    	      m_Log.debug("ImpresionCuneusManager: obtenido nombre de clase de Registro.properties : " + implClassName);
	    	      //Obtenemos la clase
	    	      Class theClass = Class.forName(implClassName);
	    	      instance = (ImpresionCuneusManager) theClass.newInstance();
	            }
		      }
		    } 
		    return instance;	
		    
		  } catch (ClassNotFoundException cnfe) {
			  m_Log.error("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR");
	          cnfe.printStackTrace();
	          throw new AnotacionRegistroException("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR ", cnfe);  
		  } catch (InstantiationException ie) {
			  m_Log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO");
	          ie.printStackTrace();
	          throw new AnotacionRegistroException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE " +
	                  "YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO", ie);	  	  
		  } catch (IllegalAccessException iae) {
			  m_Log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE");
	          iae.printStackTrace();
	          throw new AnotacionRegistroException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE " +
	                  "YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE", iae);
	      }
	  }
	  
	  public abstract String imprimirCuneus(RegistroValueObject elRegistroESVO, UsuarioValueObject usuVO, 
			  String posicionSello, String idiomaSello, String nCopiasSello, String sUrl, String realPath);
	  
	  public abstract String tipoFichero();
}
