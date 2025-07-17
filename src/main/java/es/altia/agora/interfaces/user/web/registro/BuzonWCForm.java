package es.altia.agora.interfaces.user.web.registro;

import es.altia.agora.business.registro.BuzonWCValueObject;
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


/** Clase utilizada para capturar o mostrar el estado de una Tramitacion */
public class BuzonWCForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(BuzonWCForm.class.getName());

    //Reutilizamos
    BuzonWCValueObject buzonVO = new BuzonWCValueObject();

    public BuzonWCValueObject getBuzonWC() {
        return buzonVO;
    }

    public void setBuzonWC(BuzonWCValueObject buzonVO) {
        this.buzonVO = buzonVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public Vector getListaRegistroWC() {
		  return buzonVO.getListaRegistroWC(); 
	  }
	  public void setPaginaListado(String paginaListado) {
			buzonVO.setPaginaListado(paginaListado); 
		}
	
		public void setNumLineasPaginaListado(String numLineasPaginaListado) {
			buzonVO.setNumLineasPaginaListado(numLineasPaginaListado); 
		}
	
		public String getPaginaListado() {
			return buzonVO.getPaginaListado(); 
		}
	
		public String getNumLineasPaginaListado() {
			return buzonVO.getNumLineasPaginaListado(); 
		}
		public String getRespOpcion() {
		  return buzonVO.getRespOpcion(); 
	  }
	  
	  public void setXML(String XML) {
		  buzonVO.setXML(XML); 
	  }

	  public String getXML() {
		  return buzonVO.getXML(); 
	  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            buzonVO.validate(idioma);
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
