/*______________________________BOF_________________________________*/
package es.altia.flexia.notificacion.firma;

import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoPK;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoVO;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.altia.agora.business.escritorio.UsuarioValueObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @version $\Date$ $\Revision$
 */
public class PrepareFirmaNotificacionAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "PrepareFirmaNotificacionAction";
    private static final String MAPPING_SUCCESS = "FirmaDocumentoTramitacionForm";
    private static final Log _log =
            LogFactory.getLog(PrepareFirmaNotificacionAction.class.getName());

    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");
        /* Cast form */
        es.altia.flexia.notificacion.firma.NotificacionFirmaActionForm concreteForm = (es.altia.flexia.notificacion.firma.NotificacionFirmaActionForm) form;

        UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        
        /* Retrieve DataSource key */
        this.pDataSourceKey = SessionManager.getDataSourceKey(request);

        String tipoDocumento = request.getParameter("tipoDocumento");
        String nombreDocumento = request.getParameter("nombreDocumento");
        //String textoFirmar = request.getParameter("textoFirmar");
        String textoFirmar=(String) request.getSession().getAttribute("textoFirmar");
        String codNotificacion = request.getParameter("codNotificacion");
        String tipoMime = request.getParameter("tipoMime");
       
        request.setAttribute("tipoDocumentoFirmar",tipoDocumento);
        request.setAttribute("nombreDocumentoFirmar",nombreDocumento);
        request.setAttribute("textoFirmar",textoFirmar);
        request.setAttribute("codNotificacion",codNotificacion);
        //request.setAttribute("codOrganizacion",usuario.getOrgCod();
        request.setAttribute("tipoMime",tipoMime);
        
        if (SessionManager.isUserAuthenticated(request)) {
            /* Averiguar nif usuario firmante */
            int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
            DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
            facade.setDsKey(this.pDataSourceKey);
            UsuarioDelegadoVO vo = facade.retrieveUsuarioDelegable(new UsuarioDelegadoPK(idUsuario));
            concreteForm.setNifUsuarioFirmante(vo.getNif());concreteForm.setTipoDocumento(Integer.parseInt(tipoDocumento));
            concreteForm.setCodigoNotificacion(Integer.parseInt(codNotificacion));//concreteForm.setCodigoOrganizacion(usuario.getOrgCod());
            concreteForm.setTipoMime(tipoMime);
        }//if
        final ActionForward result;
        if(tipoDocumento.equals("2")) result = mapping.findForward("FirmaNotificacionForm");

        /* Return ActionForward */
        else result = mapping.findForward(MAPPING_SUCCESS);
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() END");
        return result;
    }//doPerform

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