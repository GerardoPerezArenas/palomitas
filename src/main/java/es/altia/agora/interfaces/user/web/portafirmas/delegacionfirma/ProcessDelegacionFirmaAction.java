/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.delegacionfirma;

import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaPK;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaVO;
import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultActionForm;
import es.altia.util.struts.actions.SimpleCrudProcessAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version $\Date$ $\Revision$
 */
public class ProcessDelegacionFirmaAction extends SimpleCrudProcessAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME="ProcessDelegacionFirmaAction";
    private static final String MAPPING_SUCCESS="DelegacionFirmaForm";
    private static final String MESSAGE_CREATE_SUCCESS="DelegacionFirmaForm.Create.Success";
    private static final String MESSAGE_UPDATE_SUCCESS="DelegacionFirmaForm.Update.Success";
    private static final String MESSAGE_DELETE_SUCCESS="DelegacionFirmaForm.Delete.Success";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    protected String getMainPageMapping() {
        return GlobalNames.MAINPAGE_GLOBAL_FORWARD;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }

    protected ActionForward doCreate(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        /*Cast form*/
        DelegacionFirmaActionForm concreteForm = (DelegacionFirmaActionForm) form;

        /* Retrieve data from form */
        DelegacionFirmaVO vo = concreteForm.getDelegacionFirmaVO(request);

        /* Recuperar parámetros de conexión */
        //UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
        //String[] params = usuario.getParamsCon();
        //if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() PARAMS = {"+BasicTypesOperations.toString(params,",")+"}");

        /* Do the job */
        DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
        //facade.setDsKey(params[params.length-1]);
        facade.resetDsKey();
        vo = facade.createDelegacionFirma(vo);

        /* Update form */
        concreteForm.setDelegacionFirmaVO(vo,request);
        concreteForm.setActionCode(SimpleCrudProcessAction.DO_UPDATE);

        /* Return ActionForward */
        final ActionForward result = saveSingleMessageAndForward(mapping,request,MESSAGE_CREATE_SUCCESS,MAPPING_SUCCESS);
        return result;
    }//doCreate

    protected ActionForward doUpdate(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        /*Cast form*/
        DelegacionFirmaActionForm concreteForm = (DelegacionFirmaActionForm) form;

        /* Retrieve data from form */
        DelegacionFirmaVO vo = concreteForm.getDelegacionFirmaVO(request);

        /* Recuperar parámetros de conexión */
        //UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
        //String[] params = usuario.getParamsCon();
        //if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() PARAMS = {"+BasicTypesOperations.toString(params,",")+"}");

        /* Do the job */
        DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
        //facade.setDsKey(params[params.length-1]);
        facade.updateDelegacionFirma(vo);

        /* Update form */
        concreteForm.setDelegacionFirmaVO(vo,request);
        concreteForm.setActionCode(SimpleCrudProcessAction.DO_UPDATE);

        /* Return ActionForward */
        final ActionForward result = saveSingleMessageAndForward(mapping,request,MESSAGE_UPDATE_SUCCESS,MAPPING_SUCCESS);
        return result;
    }//doUpdate

    protected ActionForward doDelete(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        /*Cast form*/
        DelegacionFirmaActionForm concreteForm = (DelegacionFirmaActionForm) form;

        /* Retrieve data */
        int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        DelegacionFirmaPK pk = new DelegacionFirmaPK(idUsuario);

        /* Recuperar parámetros de conexión */
        //UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
        //String[] params = usuario.getParamsCon();
        //if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() PARAMS = {"+BasicTypesOperations.toString(params,",")+"}");

        /* Do the job */
        DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
        //facade.setDsKey(params[params.length-1]);
        facade.removeDelegacionFirma(pk);

        concreteForm.reset(mapping,request);
        concreteForm.setActionCode(SimpleCrudProcessAction.DO_CREATE);

        /* Return ActionForward */
        final ActionForward result = saveSingleMessageAndForward(mapping,request,MESSAGE_DELETE_SUCCESS,MAPPING_SUCCESS);
        return result;
    }//doDelete
}//class
/*______________________________EOF_________________________________*/
