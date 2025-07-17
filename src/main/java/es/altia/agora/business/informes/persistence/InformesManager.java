package es.altia.agora.business.informes.persistence;

import es.altia.agora.business.informes.persistence.manual.InformesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class InformesManager{

   private static InformesManager instance = null;
   protected static Config conf;
   protected static Log m_Log = LogFactory.getLog(InformesManager.class.getName());

   protected InformesManager() {
	//Queremos usar el fichero de configuración technical
	conf = ConfigServiceHelper.getConfig("techserver");
	//Queremos usar el servicio de log
   }

   /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de ManActuacionesManager
    */
   public static InformesManager getInstance() {
	//Si no hay una instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
	   synchronized(InformesManager.class) {
		if (instance == null)
		   instance = new InformesManager();
	   }
	}
	return instance;
   }

   public static Vector generarInforme(String[] params, Vector filtros, Vector grupos, GeneralValueObject paramsPagina)
   {
     m_Log.info("InformesManager.GenerarInforme()");
     Vector estadisticas = new Vector();   

     try{
       estadisticas = InformesDAO.getInstance().generarInforme(params, filtros, grupos, paramsPagina);
     }
     catch (Exception e){
       if(m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
       e.printStackTrace();
     }
     m_Log.info("generarInforme END");
     return estadisticas;
   }
   
   public static String verExisteExpedientes(String[] params, Vector filtros, Vector grupos, GeneralValueObject paramsPagina)
   {
     m_Log.info("InformesManager.verExisteExpedientes()");
     String respuesta = "";
     
     try{
       respuesta = InformesDAO.getInstance().verExisteExpedientes(params, filtros, grupos, paramsPagina);
     }
     catch (Exception e){
       respuesta = null;
       if(m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
       e.printStackTrace();
     }
     m_Log.info("verExisteExpedientes END");
     return respuesta;
   }

}