package es.altia.agora.business.util.persistence;

import java.util.Vector;
import es.altia.agora.business.util.persistence.manual.ValidarDocumentoDAO;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */

public class ValidarDocumentoManager {

   private static ValidarDocumentoManager instance = null;

   private ValidarDocumentoManager(){}

   /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MenuManager
    */
   public static ValidarDocumentoManager getInstance() {
	//Si no hay una instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
	   synchronized(ValidarDocumentoManager.class) {
		if (instance == null)
		   instance = new ValidarDocumentoManager();
	   }
	}
	return instance;
   }

   public Vector load(String params[]){
	return ValidarDocumentoDAO.getInstance().load(params);
   }
}