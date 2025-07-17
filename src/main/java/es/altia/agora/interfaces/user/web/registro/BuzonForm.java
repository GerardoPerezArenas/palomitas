// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro;

// PAQUETES IMPORTADOS
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.*;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ManActuacionesForm</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */

public class BuzonForm extends ActionForm {

   //Queremos usar el fichero de configuración technical
   protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
   //Necesitamos el servicio de log
   protected static Log m_Log =
           LogFactory.getLog(BuzonForm.class.getName());

   private Vector buzon;
   private boolean filas;

   public Vector getBuzon(){
	return buzon;
   }

   public void setBuzon(Vector v){
	buzon = v;
   }

   public boolean getFilas(){
	return filas;
   }

   public void setFilas(boolean f){
	filas=f;
   }

  /* Función que procesa los errores de validación a formato struts */
   private ActionErrors validationException(ValidationException ve,ActionErrors errors){
	Iterator iter = ve.getMessages().get();
	while (iter.hasNext()){
	   Message message = (Message)iter.next();
	   errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
	}
	return errors;
   }
}