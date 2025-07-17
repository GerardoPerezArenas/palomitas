package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.RelacionEntradasSalidasValueObject;
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
public class RelacionEntradasSalidasForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(RelacionEntradasSalidasForm.class.getName());

    //Reutilizamos
    RelacionEntradasSalidasValueObject relESVO = new RelacionEntradasSalidasValueObject();

    public RelacionEntradasSalidasValueObject getRelacionEntradasSalidas() {
        return relESVO;
    }

    public void setRelacionEntradasSalidas(RelacionEntradasSalidasValueObject relESVO) {
        this.relESVO = relESVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getAsunto() {
        return relESVO.getAsunto();
      }
      public void setAsunto(String asunto) {
        relESVO.setAsunto(asunto);
      }
      public String getTxtDocumento() {
        return relESVO.getTxtDocumento();
      }
      public void setTxtDocumento(String txtDocumento) {
        relESVO.setTxtDocumento(txtDocumento);
      }
      public String getTxtFechaES() {
        return relESVO.getTxtFechaES();
      }
      public void setTxtFechaES(String txtFechaES) {
        relESVO.setTxtFechaES(txtFechaES);
      }
      public String getTxtNombre() {
        return relESVO.getTxtNombre();
      }
      public void setTxtNombre(String txtNombre) {
        relESVO.setTxtNombre(txtNombre);
      }
      public String getTxtNumeroRegistro() {
        return relESVO.getTxtNumeroRegistro();
      }
      public void setTxtNumeroRegistro(String txtNumeroRegistro) {
        relESVO.setTxtNumeroRegistro(txtNumeroRegistro);
      }
      public String getTxtRemitente() {
        return relESVO.getTxtRemitente();
      }
      public void setTxtRemitente(String txtRemitente) {
        relESVO.setTxtRemitente(txtRemitente);
      }
      public String getTxtTipoDocumento() {
        return relESVO.getTxtTipoDocumento();
      }
      public void setTxtTipoDocumento(String txtTipoDocumento) {
        relESVO.setTxtTipoDocumento(txtTipoDocumento);
      }
      public String getTxtTipoES() {
        return relESVO.getTxtTipoES();
      }
      public void setTxtTipoES(String txtTipoES) {
        relESVO.setTxtTipoES(txtTipoES);
  }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            relESVO.validate(idioma);
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
