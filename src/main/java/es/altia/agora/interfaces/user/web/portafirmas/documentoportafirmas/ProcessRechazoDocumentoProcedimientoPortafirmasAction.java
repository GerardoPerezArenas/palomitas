/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasManager;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.FirmasDocumentoProcedimientoManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.util.struts.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Tiffany
 */
public class ProcessRechazoDocumentoProcedimientoPortafirmasAction extends DefaultAction{
    
      /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessRechazoDocumentoPortafirmasAction";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoRechazado";
    private static final String MESSAGE_SUCCESS_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoNoRechazado";

    private static final Log _log =
            LogFactory.getLog(ProcessRechazoDocumentoPortafirmasAction.class.getName());

    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");

        /* Aceptamos el rechazo, por lo que eliminamos de la sesion el parametro de valor seleccionado en caso de cancelar */
        SessionManager.removeSelectedIndex(request);

        /* Cast form */
        FirmaDocumentoPortafirmasActionForm concreteForm = (FirmaDocumentoPortafirmasActionForm) form;
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPerform() ConcreteForm = "+concreteForm);

        /* Retrieve DataSource key */
        this.pDataSourceKey = SessionManager.getDataSourceKey(request);

        /* Save Signature */
        int idUsuarioAutenticado  = SessionManager.getAuthenticatedUser(request).getIdUsuario();
                
        UsuarioValueObject usuarioVO = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        boolean documentoRechazado = false;
        
        if(!"".equals(concreteForm.getObservaciones())){
            DocumentoProcedimientoFirmaVO doc = new DocumentoProcedimientoFirmaVO();
            doc.setIdPresentado(concreteForm.getIdPresentado());            
            doc.setCodigoUsuarioFirma(Integer.toString(idUsuarioAutenticado));
            doc.setFechaFirma(Calendar.getInstance());
            doc.setObservaciones(concreteForm.getObservaciones());
            doc.setIdNumFirma(concreteForm.getIdNumFirma());
            
            documentoRechazado = DocsProcedimientoPortafirmasManager.getInstance().
                    rechazarDocumento(doc, usuarioVO.getParamsCon());
            
          
        }

        String mensaje = null;
        if(documentoRechazado){
            mensaje = MESSAGE_SUCCESS;
        }else
            mensaje = MESSAGE_SUCCESS_FAIL;
      
        /* Do search */
        PrepareSearchDocumentoPortafirmasAction searchAction =  new PrepareSearchDocumentoPortafirmasAction();
        searchAction.execute(mapping,form,request,response);

          
        /* Save messages */
        saveSingleMessage(request,new ActionMessage(mensaje));
        
        /* Finalizadmos informe */
        //si se marco finalizar en rechazo
        String[] params = usuarioVO.getParamsCon();
        _log.debug("Recuperamos listaFirmas. Datos entreda: idNumDoc: "+concreteForm.getIdNumeroDocumento()+" idMunicipio: "+concreteForm.getIdMunicipio());
        _log.debug("IdProcedmientos: "+concreteForm.getIdProcedimiento()+" Params: "+params);
        ArrayList<FirmasDocumentoProcedimientoVO> listaFirmas = FirmasDocumentoProcedimientoManager.getInstance().
                 getFirmasDocumento(String.valueOf(concreteForm.getIdNumeroDocumento()), String.valueOf(concreteForm.getIdMunicipio()),
                 String.valueOf(concreteForm.getIdProcedimiento()), params);
            
        if(concreteForm.getFinalizaRechazo()!=null && concreteForm.getFinalizaRechazo().equals("1") ){
            TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
            HttpSession session = request.getSession();                 
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");   
            String justificacion= concreteForm.getObservaciones();
            tramExpVO.setJustificacion(justificacion);
            tramExpVO.setPersonaAutoriza(usuario.getNombreUsu());   
            _log.debug("** justificacion "+tramExpVO.getJustificacion());
            _log.debug("** autoriza "+tramExpVO.getPersonaAutoriza());
            tramExpVO.setListaEMailsAlIniciar(new Vector());
            tramExpVO.setListaEMailsAlFinalizar(new Vector());
            tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
            tramExpVO.setNombreUsuario(usuario.getNombreUsu());
            tramExpVO.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
            tramExpVO.setCodEntidad(Integer.toString(usuario.getEntCod()));
            _log.debug("** usuario "+Integer.toString(usuario.getIdUsuario()));
            tramExpVO.setObservaciones(null);
            tramExpVO.setNumeroExpediente(concreteForm.getIdNumeroExpediente());
       
            
            tramExpVO.setCodMunicipio(String.valueOf(concreteForm.getIdMunicipio()));
            tramExpVO.setEjercicio(String.valueOf(concreteForm.getIdEjercicio()));
            tramExpVO.setNumero(String.valueOf(concreteForm.getIdNumeroExpediente()));
            tramExpVO.setProcedimiento(concreteForm.getIdProcedimiento());
            tramExpVO.setCodProcedimiento(concreteForm.getIdProcedimiento());
            tramExpVO.setCodTramite(String.valueOf(concreteForm.getIdTramite()));
            _log.debug("codMunicipio: "+tramExpVO.getCodMunicipio()+" ejercicio: "+tramExpVO.getEjercicio()+" numero: "+tramExpVO.getNumero());
            _log.debug("codProcedimiento: "+tramExpVO.getCodProcedimiento()+" codTramite: "+tramExpVO.getCodTramite());
            
            TramitacionExpedientesManager tramitacion= TramitacionExpedientesManager.getInstance();
            int res = -1;
            res =tramitacion.finalizarExpedienteNoConvencional(tramExpVO, null,params);
            if (res > 0)
                OperacionesExpedienteManager.getInstance().registrarAnularExpediente(tramExpVO, params);
        }
        
        /* Return ActionForward */
        final ActionForward result;
        if ( concreteForm.getDoPopUp() ) result = mapping.findForward( searchAction.getPopUpMappingKey() );
        else if ( concreteForm.getDoPrintPreview() ) result = mapping.findForward( searchAction.getPrintPreviewMappingKey() );
        else result = mapping.findForward( searchAction.getDefaultMappingKey() );
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+ ".doPerform() END  Jumping to "+ result.getName() + "-----");
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
