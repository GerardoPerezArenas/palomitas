package es.altia.agora.business.sge.persistence;

import java.util.Vector;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.persistence.manual.CargosDAO;
import es.altia.agora.business.util.GeneralValueObject;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */

public class CargosManager{

   private static CargosManager instance = null;
   protected static Config conf;
   protected static Log m_Log =
            LogFactory.getLog(CargosManager.class.getName());

   protected CargosManager() {
	//Queremos usar el fichero de configuración technical
	conf = ConfigServiceHelper.getConfig("techserver");
   }

   /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de ManActuacionesManager
    */
   public static CargosManager getInstance() {
	//Si no hay una instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
	   synchronized(CargosManager.class) {
		if (instance == null)
		   instance = new CargosManager();
	   }
	}
	return instance;
   }

   public Vector loadCargos(String[] params){
	//queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("loadCargos");
	Vector r = new Vector();
	try{
	   r = CargosDAO.getInstance().loadCargos(params);
	}
	catch (Exception e){
	   m_Log.error("Excepción capturada: " + e.toString());
	   m_Log.debug("Excepción capturada: " + e.toString());
	}
	m_Log.debug("loadCargos");
	return r;
   }

   public Vector eliminarCargo(String cod, String[] params) {
	//queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("eliminarCargo");
	Vector r = new Vector();
	try{
	   r = CargosDAO.getInstance().eliminarCargo(cod,params);
	}
	catch (Exception e){
	   m_Log.error("Excepción capturada: " + e.toString());
	}
	m_Log.debug("eliminarCargo");
	return r;
   }

   public Vector modificarCargo(GeneralValueObject gVO, String[] params){
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("modificarCargo");
     Vector r = new Vector();
     try{
	  r = CargosDAO.getInstance().modificarCargo(gVO, params);
     }
     catch(Exception e){
	  m_Log.error("Excepcion capturada: " + e.toString());
     }
     m_Log.debug("modificarCargo");
     return r;
   }

   public Vector altaCargo(GeneralValueObject gVO, String[] params) {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("altaCargo");
     Vector r = new Vector();
     try{
	 r = CargosDAO.getInstance().altaCargo(gVO,params);
     }
     catch (Exception e) {
	  m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("altaCargo");
     return r;
   }

  public GeneralValueObject getCargo(GeneralValueObject gVO, String[] params)
  { 
    /* Obtiene la información de un cargo dado su identificador en el atributo "cod" del GeneralValueObject */
	 
    m_Log.debug("getCargo");
    try{
       gVO = CargosDAO.getInstance().getCargo(gVO,params);
    }
    catch (Exception e){
       m_Log.error("Excepción capturada: " + e.toString());
    }
    m_Log.debug("getCargo");
    return gVO;
  }
}