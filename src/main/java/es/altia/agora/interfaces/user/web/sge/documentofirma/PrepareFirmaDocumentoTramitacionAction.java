/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.sge.documentofirma;

import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoPK;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoVO;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.documentos.DocumentOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version $\Date$ $\Revision$
 */
public class PrepareFirmaDocumentoTramitacionAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "PrepareFirmaDocumentoTramitacionAction";
    private static final String MAPPING_SUCCESS = "FirmaDocumentoTramitacionForm";
    private static final Log _log =
            LogFactory.getLog(PrepareFirmaDocumentoTramitacionAction.class.getName());

    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");
        /* Cast form */
        DocumentoFirmaActionForm concreteForm = (DocumentoFirmaActionForm) form;

        /* Retrieve DataSource key */
        this.pDataSourceKey = SessionManager.getDataSourceKey(request);

        String tipoDocumento = request.getParameter("tipoDocumento");
        String nombreDocumento = request.getParameter("nombreDocumento");
        request.setAttribute("tipoDocumentoFirmar",tipoDocumento);
        request.setAttribute("nombreDocumentoFirmar",nombreDocumento);
        
        if (SessionManager.isUserAuthenticated(request)) {
            /* Averiguar nif usuario firmante */
            int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
            DelegacionFirmaFacadeDelegate facade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
            facade.setDsKey(this.pDataSourceKey);
            UsuarioDelegadoVO vo = facade.retrieveUsuarioDelegable(new UsuarioDelegadoPK(idUsuario));
            concreteForm.setNifUsuarioFirmante(vo.getNif());
            concreteForm.setTipoDocumento(Integer.parseInt(tipoDocumento));

            String [] params = SessionManager.getAuthenticatedUser(request).getParamsCon();
            String editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(concreteForm.getIdMunicipio(),
                       concreteForm.getIdNumeroExpediente(),concreteForm.getIdTramite(),
                       concreteForm.getIdOcurrenciaTramite(),concreteForm.getIdNumeroDocumento(),"1".equals(tipoDocumento),params);
            concreteForm.setEditorTexto(editorTexto);

            if (nombreDocumento == null || nombreDocumento.isEmpty()) {
                nombreDocumento = DocumentosAplicacionDAO.getInstance().getNombreDocumento(concreteForm.getIdMunicipio(),
                       concreteForm.getIdNumeroExpediente(),concreteForm.getIdTramite(),
                       concreteForm.getIdOcurrenciaTramite(),concreteForm.getIdNumeroDocumento(),"1".equals(tipoDocumento),params);
            }
            
            String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                    editorTexto,
                    nombreDocumento);
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

}//class
/*______________________________EOF_________________________________*/