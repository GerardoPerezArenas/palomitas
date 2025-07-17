/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.delegacionfirma;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaPK;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaVO;
import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.commons.DateOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.struts.DefaultActionForm;
import es.altia.util.struts.actions.SimpleCrudPrepareAction;
import es.altia.util.struts.actions.SimpleCrudProcessAction;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * @version $\Date$ $\Revision$
 */
public class PrepareDelegacionFirmaAction extends SimpleCrudPrepareAction {
    /*_______Constants______________________________________________*/
    public static final String CLSNAME="PrepareDelegacionFirmaAction";
    public static final String MAPPING_SUCCESS="DelegacionFirmaForm";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/

    protected ActionForward doPrepareCreate(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        /*Cast form*/
        DelegacionFirmaActionForm concreteForm = (DelegacionFirmaActionForm) form;

        /* If the request is to allow the user to correct errors in the form, "concreteForm" must not be modified. */
        if (request.getAttribute(Globals.ERROR_KEY) == null) {
            if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPrepareCreate() There are no previous errors... prefilling form");
            /* Pre-fill static form fields */
            initScratchForm(mapping, concreteForm, request);
        } else {
            if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPrepareCreate() There are errors... do not touch form");
        }//if

        /* Return ActionForward */
        final ActionForward result = mapping.findForward(MAPPING_SUCCESS);
        return result;
    }//doPrepareCreate

    protected ActionForward doPrepareUpdate(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException, ModelException {
        /*Cast form*/
        DelegacionFirmaActionForm concreteForm = (DelegacionFirmaActionForm) form;

        /* Pre-fill static form fields */
        int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        concreteForm.setUsuario(idUsuario);

        /* If the request is to allow the user to correct errors in the form, "concreteForm" must not be modified. */
        if (request.getAttribute(Globals.ERROR_KEY) == null) {
            if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPrepareUpdate() There are no previous errors... prefilling form");

            /* Recuperar parámetros de conexión */
            //UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
            //String[] params = usuario.getParamsCon();
            //if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() PARAMS = {"+BasicTypesOperations.toString(params,",")+"}");

            /* Recuperar delegacion firma */
            DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
facade.resetDsKey();           
            //facade.setDsKey(params[params.length-1]);
            DelegacionFirmaPK pk = new DelegacionFirmaPK(idUsuario);
            try {
                DelegacionFirmaVO vo = facade.retrieveDelegacionFirma(pk);
                /* Recuperar datos usuario delegado */
                int idUsuarioDelegado = vo.getUsuarioDelegado();
                UsuarioEscritorioValueObject usuarioEscritorioVO = UsuarioManager.getInstance().buscaUsuario(idUsuarioDelegado);

                /* Guardar datos en formulario */
                concreteForm.setDelegacionFirmaVO(vo,request);
                if (usuarioEscritorioVO!=null)
                    concreteForm.setNombreUsuarioDelegado(usuarioEscritorioVO.getNombreUsu());
                else
                    concreteForm.setNombreUsuarioDelegado("USUARIO NO VALIDO");
                if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPrepareUpdate() NombreUsuario = "+usuarioEscritorioVO.getNombreUsu()+"; ConcreteForm ="+concreteForm);
            } catch (InstanceNotFoundException e) {
                /* No hay ninguna delegacion definida */
                initScratchForm(mapping, concreteForm, request);
            }//try-catch
        } else {
            if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPrepareUpdate() There are errors... do not touch form");
        }//if

        /* Return ActionForward */
        final ActionForward result = mapping.findForward(MAPPING_SUCCESS);
        return result;
    }//doPrepareUpdate

    private void initScratchForm(ActionMapping mapping, DelegacionFirmaActionForm concreteForm, HttpServletRequest request) {
        int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        concreteForm.setUsuario(idUsuario);
        concreteForm.setFechaDesde(DateOperations.toString(Calendar.getInstance(),GlobalNames.DATE_FORMAT));
        Calendar nextMonth = Calendar.getInstance(); nextMonth.add(Calendar.MONTH,1);
        concreteForm.setFechaHasta(DateOperations.toString(nextMonth,GlobalNames.DATE_FORMAT));
        concreteForm.setActionCode(SimpleCrudProcessAction.DO_CREATE);
    }//initScratchForm


    protected String getMainPageMapping() {
        return GlobalNames.MAINPAGE_GLOBAL_FORWARD;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }

}//class
/*______________________________EOF_________________________________*/
