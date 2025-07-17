package es.altia.agora.interfaces.user.web.terceros;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoAmbitoForm;
import es.altia.agora.interfaces.user.web.planeamiento.form.AnotacionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.Iterator;
import java.util.Vector;

public class TerceroForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(TerceroForm.class.getName());

    private String tipoDocumento;
    private String tipoDocDesc;
    private String documento;
    private String codTercero;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private Vector domicilios;
    private String codDomPrincipal;

    public TerceroForm() {
        reset();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTipoDocDesc() {
        return tipoDocDesc;
    }

    public void setTipoDocDesc(String tipoDocDesc) {
        this.tipoDocDesc = tipoDocDesc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCodTercero() {
        return codTercero;
    }

    public void setCodTercero(String codTercero) {
        this.codTercero = codTercero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Vector getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(Vector domicilios) {
        this.domicilios = domicilios;
    }

    public String getCodDomPrincipal() {
        return codDomPrincipal;
    }

    public void setCodDomPrincipal(String codDomPrincipal) {
        this.codDomPrincipal = codDomPrincipal;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    private void reset() {
        tipoDocumento = null;
        documento = null;
        codTercero = null;
        nombre = null;
        apellido1 = null;
        apellido2 = null;
        domicilios = new Vector();
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
