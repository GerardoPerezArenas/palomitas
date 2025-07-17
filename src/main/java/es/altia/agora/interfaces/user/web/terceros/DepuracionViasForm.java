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

public class DepuracionViasForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(DepuracionViasForm.class.getName());

    private String codProvincia;
    private String codMunicipio;
    private String descVia;

    public DepuracionViasForm() {
        reset();
    }

    public String getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getDescVia() {
        return descVia;
    }

    public void setDescVia(String descVia) {
        this.descVia = descVia;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    private void reset() {
        codProvincia = null;
        codMunicipio = null;
        descVia = null;
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
