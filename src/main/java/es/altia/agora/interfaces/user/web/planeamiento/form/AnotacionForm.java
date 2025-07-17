package es.altia.agora.interfaces.user.web.planeamiento.form;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoAmbitoForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.Iterator;

public class AnotacionForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(AnotacionForm.class.getName());

    private String numeroAnotacion;
    private String fechaAnotacion;
    private String comentarioAnotacion;

    public AnotacionForm() {
        reset();
    }

    public String getNumeroAnotacion() {
        return numeroAnotacion;
    }

    public void setNumeroAnotacion(String numeroAnotacion) {
        this.numeroAnotacion = numeroAnotacion;
    }

    public String getFechaAnotacion() {
        return fechaAnotacion;
    }

    public void setFechaAnotacion(String fechaAnotacion) {
        this.fechaAnotacion = fechaAnotacion;
    }

    public String getComentarioAnotacion() {
        return comentarioAnotacion;
    }

    public void setComentarioAnotacion(String comentarioAnotacion) {
        this.comentarioAnotacion = comentarioAnotacion;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public Object clone() {
        AnotacionForm anotacionForm = new AnotacionForm();
        anotacionForm.setNumeroAnotacion(numeroAnotacion);
        anotacionForm.setFechaAnotacion(fechaAnotacion);
        anotacionForm.setComentarioAnotacion(comentarioAnotacion);
        return anotacionForm;
    }

    private void reset() {
        numeroAnotacion = null;
        fechaAnotacion = null;
        comentarioAnotacion = null;
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
