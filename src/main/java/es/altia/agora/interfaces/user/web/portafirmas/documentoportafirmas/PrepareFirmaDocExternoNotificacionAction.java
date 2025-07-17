/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoPK;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoVO;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Administrador
 */
public class PrepareFirmaDocExternoNotificacionAction extends DefaultAction{
     /*_______Constants______________________________________________*/
    private static final String CLSNAME = "PrepareFirmaDocExternoNotificacionAction";
    private static final String MAPPING_SUCCESS = "firmaDocExternoNotificacion";
    private static final Log _log =
            LogFactory.getLog(PrepareFirmaDocExternoNotificacionAction.class.getName());

    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");
        /* Cast form */
        DocumentoExternoNotificacionForm concreteForm = (DocumentoExternoNotificacionForm) form;

        /* Retrieve DataSource key */
        this.pDataSourceKey = SessionManager.getDataSourceKey(request);

        String tipoDocumento = request.getParameter("tipoDocumento");
        String tipoMime = request.getParameter("tipoMime");
        String nombreDocumento = request.getParameter("nombreDocumento");
        request.setAttribute("tipoDocumentoFirmar",tipoDocumento);
        request.setAttribute("nombreDocumentoFirmar",nombreDocumento);
        
        String codAdjunto = request.getParameter("codAdjunto");
        String codNotificacion = request.getParameter("codNotificacion");
        
        String codProcedimiento = request.getParameter("codProcedimiento");
        String numExpediente = request.getParameter("numExpediente");
        String codigoTramite = request.getParameter("codigoTramite");
        String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
        String ejercicio = request.getParameter("ejercicio");
        String codMunicipio = request.getParameter("codMunicipio");
        
                
        
        if (SessionManager.isUserAuthenticated(request)) {
            /* Averiguar nif usuario firmante */
            int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
            DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
            facade.setDsKey(this.pDataSourceKey);
            //UsuarioDelegadoVO vo = facade.retrieveUsuarioDelegable(new UsuarioDelegadoPK(idUsuario));
            //concreteForm.setNifUsuarioFirmante(vo.getNif());concreteForm.setTipoDocumento(Integer.parseInt(tipoDocumento));
            
            concreteForm.setCodNotificacion(Integer.parseInt(codNotificacion));            
            concreteForm.setCodAdjunto(Integer.parseInt(codAdjunto));
            concreteForm.setIdTramite(Integer.parseInt(codigoTramite));
            concreteForm.setIdOcurrenciaTramite(Integer.parseInt(ocurrenciaTramite));
            concreteForm.setIdNumeroExpediente(numExpediente);
            concreteForm.setIdEjercicio(Integer.parseInt(ejercicio));
            concreteForm.setIdMunicipio(Integer.parseInt(codMunicipio));
            concreteForm.setIdProcedimiento(codProcedimiento);
            concreteForm.setTipoMime(tipoMime);
        }//if

        /* Return ActionForward */
        final ActionForward result = mapping.findForward(MAPPING_SUCCESS);
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
   
   
}
