package es.altia.agora.interfaces.user.web.planeamiento.form;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.Iterator;

public class RectificacionForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(RectificacionForm.class.getName());

    private String numeroRectificacion;
    private String fechaRectificacion;
    private String comentarioRectificacion;

    public RectificacionForm() {
        reset();
    }

    public String getNumeroRectificacion() {
        return numeroRectificacion;
    }

    public void setNumeroRectificacion(String numeroRectificacion) {
        this.numeroRectificacion = numeroRectificacion;
    }

    public String getFechaRectificacion() {
        return fechaRectificacion;
    }

    public void setFechaRectificacion(String fechaRectificacion) {
        this.fechaRectificacion = fechaRectificacion;
    }

    public String getComentarioRectificacion() {
        return comentarioRectificacion;
    }

    public void setComentarioRectificacion(String comentarioRectificacion) {
        this.comentarioRectificacion = comentarioRectificacion;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    private void reset() {
        numeroRectificacion = null;
        fechaRectificacion = null;
        comentarioRectificacion = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
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
