package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.FormulTramitacionExpedientesValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class FormulTramitacionExpedientesForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(FormulTramitacionExpedientesForm.class.getName());

    //Reutilizamos
    FormulTramitacionExpedientesValueObject formulTramExpVO = new FormulTramitacionExpedientesValueObject();

    public FormulTramitacionExpedientesValueObject getFormulTramitacionExpedientes() {
        return formulTramExpVO;
    }

    public void setFormulTramitacionExpedientes(FormulTramitacionExpedientesValueObject formulTramExpVO) {
        this.formulTramExpVO = formulTramExpVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getTxtDescripcion() {
          return formulTramExpVO.getTxtDescripcion();
        }
        public void setTxtDescripcion(String txtDescripcion) {
          formulTramExpVO.setTxtDescripcion(txtDescripcion);
        }
        public String getTxtExpediente() {
          return formulTramExpVO.getTxtExpediente();
        }
        public void setTxtExpediente(String txtExpediente) {
          formulTramExpVO.setTxtExpediente(txtExpediente);
        }
        public String getTxtProcedimiento() {
          return formulTramExpVO.getTxtProcedimiento();
        }
        public void setTxtProcedimiento(String txtProcedimiento) {
          formulTramExpVO.setTxtProcedimiento(txtProcedimiento);
        }
        public String getTxtSolicitante() {
          return formulTramExpVO.getTxtSolicitante();
        }
        public void setTxtSolicitante(String txtSolicitante) {
          formulTramExpVO.setTxtSolicitante(txtSolicitante);
        }
        public String getTxtTramite() {
          return formulTramExpVO.getTxtTramite();
        }
        public void setTxtTramite(String txtTramite) {
          formulTramExpVO.setTxtTramite(txtTramite);
      }


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            formulTramExpVO.validate(idioma);
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
