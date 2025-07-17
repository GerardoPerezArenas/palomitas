package es.altia.util.struts;

import es.altia.util.persistance.PersistanceContext;
import es.altia.util.persistance.impl.BasePersistanceContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides convenience methods to facilitate the implementation of concrete
 * <code>ActionForm</code>s.
 */
public abstract class DefaultActionForm extends ActionForm {
    /*_______Atributes______________________________________________*/
    private long pVersionNumber = -1;
    private String pActionCode = null;

    /*_______Operations_____________________________________________*/
    public DefaultActionForm() {
        reset();
    }//constructor

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping,request);
        pVersionNumber = -1;
        pActionCode = null;
        reset();
    }//reset

	public PersistanceContext getPersistanceContext() {
		return new BasePersistanceContext(pVersionNumber, true, true);
	}//getPersistanceContext
	public void setPersistanceContext(PersistanceContext ctx){
		pVersionNumber = ctx.getVersionNumber();
	}//setPersistanceContext

	public long getVersionNumber() {
		return pVersionNumber;
	}//getVersionNumber
	public void setVersionNumber(long newValue) {
		pVersionNumber = newValue;
	}//setVersionNumber

    public String getActionCode() {
        return pActionCode;
    }
    public void setActionCode(String actionCode) {
        pActionCode = actionCode;
    }

    protected abstract void reset();

	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		return errors;
	}//validate
}//class
