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

public class MantenimientoTiposForm extends ActionForm {

    private Collection tipos;
    private Collection subsecciones;
    private String tipoRegistro;
    private String codigoSubseccion;
    private String subseccion;
    private String codigo;
    private String descripcion;

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(MantenimientoTiposForm.class.getName());

    public Collection getTipos() {
        return tipos;
    }

    public void setTipos(Collection tipos) {
        this.tipos = tipos;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public Collection getSubsecciones() {
        return subsecciones;
    }

    public void setSubsecciones(Collection subsecciones) {
        this.subsecciones = subsecciones;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getCodigoSubseccion() {
        return codigoSubseccion;
    }

    public void setCodigoSubseccion(String codigoSubseccion) {
        this.codigoSubseccion = codigoSubseccion;
    }

    public String getSubseccion() {
        return subseccion;
    }

    public void setSubseccion(String subseccion) {
        this.subseccion = subseccion;
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

    public MantenimientoTiposForm() {
        reset();
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    /**
     *  Resetea la lista de cuentas
     */
    private void reset() {
        tipos = null;
        subsecciones = null;
        tipoRegistro = null;
        codigoSubseccion = null;
        subseccion = null;
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
