package es.altia.agora.business.sge.persistence;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.persistence.manual.TecnicoReferenciaDTO;
import es.altia.agora.business.sge.persistence.manual.TenicoReferenciaDAO;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase que implementa la búsqueda y actualización de técnicos de referencia.
 * @author alberto.pulpeiro
 */
public class TecnicoReferenciaManager{

   private static TecnicoReferenciaManager instance = null;
   protected static Config conf;
   protected static Log m_Log =
            LogFactory.getLog(TecnicoReferenciaManager.class.getName());

   protected TecnicoReferenciaManager() {
	//Queremos usar el fichero de configuración technical
	conf = ConfigServiceHelper.getConfig("techserver");
   }

   /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de ManActuacionesManager
    */
   public static TecnicoReferenciaManager getInstance() {
	//Si no hay una instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
	   synchronized(TecnicoReferenciaManager.class) {
		if (instance == null)
		   instance = new TecnicoReferenciaManager();
	   }
	}
	return instance;
   }

   public List<TecnicoReferenciaDTO> loadTecnicosReferencia(String[] params){
	//queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("loadTecnicosReferencia");
	 List<TecnicoReferenciaDTO> listaTecnico = new ArrayList<TecnicoReferenciaDTO>();
	try{
	   listaTecnico = TenicoReferenciaDAO.getInstance().loadTecnicosReferencia(params);
	}
	catch (Exception e){
	   m_Log.error("Excepción capturada: " + e.toString());
	   m_Log.debug("Excepción capturada: " + e.toString());
	}
	m_Log.debug("fin loadTecnicosReferencia");
	return listaTecnico;
   }
}