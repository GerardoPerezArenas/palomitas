package es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento;

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
import java.util.Collection;

public class MantenimientoGradosProteccionForm extends ActionForm {

    private Collection gradosProteccion;
    private String codigo;
    private String descripcion;

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(MantenimientoGradosProteccionForm.class.getName());

    public Collection getGradosProteccion() {
        return gradosProteccion;
    }

    public void setGradosProteccion(Collection gradosProteccion) {
        this.gradosProteccion = gradosProteccion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public MantenimientoGradosProteccionForm() {
        reset();
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    /**
     *  Resetea la lista de cuentas
     */
    private void reset() {
        gradosProteccion = null;
        codigo = null;
        descripcion = null;
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
