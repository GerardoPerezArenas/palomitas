package es.altia.agora.interfaces.user.web.escritorio.exception;

import org.apache.struts.action.ActionErrors;

public class CertificateLogonException extends Exception {
    
    private ActionErrors errors;

    public CertificateLogonException(ActionErrors errors) {
        this.errors = errors;
    }

    public ActionErrors getErrors() {
        return errors;
    }

    public void setErrors(ActionErrors errors) {
        this.errors = errors;
    }
        
}
