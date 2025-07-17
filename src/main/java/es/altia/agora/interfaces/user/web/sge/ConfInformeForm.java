package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.ConfInformeValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** Clase utilizada para capturar o mostrar el estado del informe */

public class ConfInformeForm extends ActionForm {

	//Queremos usar el fichero de configuración technical

    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

    //Necesitamos el servicio de log

    protected static Log m_Log =
            LogFactory.getLog(ConfInformeForm.class.getName());

    //Reutilizamos

	ConfInformeValueObject confInformeVO = new ConfInformeValueObject();

	private Vector opciones;

    public Vector getOpciones(){ 
      return opciones; 
    }

    public void setOpciones(Vector opciones){ 
      this.opciones = opciones; 
    }		

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
		m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //ConfInformeValueObject hara el trabajo para nostros ...
        try {
			confInformeVO.validate(idioma);
        } catch (ValidationException ve) {
			//Hay errores...
			//Tenemos que traducirlos a formato struts
			errors=validationException(ve,errors);
        }
        return errors;
    }

    /* Función que procesa los errores de validación a formato struts */

    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
		Iterator iter = ve.getMessages().get();
		while (iter.hasNext()) {
			Message message = (Message)iter.next();
			errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
		}
		return errors;
    }
}
    