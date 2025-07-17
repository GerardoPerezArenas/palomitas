/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.sge.documentofirma;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas.DocumentoExternoNotificacionForm;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.persistence.NotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
/**
 *
 * @author Administrador
 */
public class ProcessFirmaNotificacionAction extends DefaultAction{

     /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessFirmaDocumentoTramitacionAction";
    private static final String MAPPING_SUCCESS = "success";
    private static final String MESSAGE_SUCCESS = "Sge.FirmaDocumentoTramitacionForm.FirmaGuardadaCorrectamente";
    private static final String MESSAGE_CERTIFICATE_NOT_VALID = "Sge.FirmaDocumentoTramitacionForm.CertificadoNoValido";

    private static final Log _log =
            LogFactory.getLog(ProcessFirmaDocumentoTramitacionAction.class.getName());

    private String pDataSourceKey = null;
    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");

        /* Cast form */     
        DocumentoExternoNotificacionForm concreteForm = (DocumentoExternoNotificacionForm) form;
        UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        boolean firmaValida = false;
        try{
            firmaValida = verificarFirma(concreteForm,usuario);
            
        }catch(Exception e){
            e.printStackTrace();
        }

        if(!firmaValida){
            saveSingleMessage(request,new ActionMessage(MESSAGE_CERTIFICATE_NOT_VALID));
            request.setAttribute("FIRMA_VALIDA","NO");
        }else{

            request.setAttribute("FIRMA_VALIDA","SI");
            boolean continuar = false;
            try{
                continuar = guardarFirma(concreteForm, usuario);
            }catch(Exception e){
                e.printStackTrace();
            }

            request.setAttribute("FIRMA_ALMACENADA","NO");
            if(continuar) request.setAttribute("FIRMA_ALMACENADA","SI");

            /* Save messages */
            saveSingleMessage(request,new ActionMessage(MESSAGE_SUCCESS));
            
        }// else

        /* Return ActionForward */
        ActionForward result  = mapping.findForward("success");
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



  
    private boolean guardarFirma(DocumentoExternoNotificacionForm concreteForm,UsuarioValueObject usuario){
        boolean continuar = false;
                

        int codOrganizacion = usuario.getOrgCod();
        int codNotificacion = concreteForm.getCodNotificacion();
        int codAdjunto = concreteForm.getCodAdjunto();
        String firma = concreteForm.getFirma();

        AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
        adjunto.setFirma(firma);
        adjunto.setEstadoFirma(ConstantesDatos.ESTADO_FIRMA_FIRMADO);
        ResourceBundle config = ResourceBundle.getBundle("Portafirmas");
        String plataformaFirma = config.getString(codOrganizacion  + "/PluginPortafirmas");
        adjunto.setPlataformaFirma(plataformaFirma);
        adjunto.setCodUsuarioFirmaOtro(usuario.getIdUsuario());
        adjunto.setTipoCertificadoFirma(ConstantesDatos.TIPO_CERTIFICADO_USUARIO);
        adjunto.setIdDocExterno(codAdjunto);
        adjunto.setCodigoNotificacion(codNotificacion);

        try{

            continuar = NotificacionManager.getInstance().guardarFirma(codNotificacion, firma, usuario.getParamsCon());

        }catch(Exception e){
            continuar  = false;
            e.printStackTrace();
            _log.error("Error al guardar la firma del documento adjunto " + codAdjunto + " de la notificación " + codNotificacion);
        }
        return continuar;
    }

    public boolean verificarFirma(DocumentoExternoNotificacionForm formulario,UsuarioValueObject usuario){
        boolean exito = false;
        
        byte[] documento = null;
        int codOrganizacion             = usuario.getOrgCod();
        String nombreOrganizacion  = usuario.getOrg();
        String[] params                  = usuario.getParamsCon();
        String firma                        = formulario.getFirma();
        int codNotificacion =        formulario.getCodNotificacion();
        _log.debug("**************** verificarFirma codNotificacion: " + codNotificacion + " <> nombreOrg: " + nombreOrganizacion);
        _log.debug("**************** La firma del fichero es:  " + firma);

        try{

            String xmlFirma = NotificacionManager.getInstance().getXMLFirmaNotificacion(codNotificacion, params);

            if(xmlFirma!=null && !"".equals(xmlFirma)){
                File f = File.createTempFile("prueba",".temp");
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(xmlFirma.getBytes());
                fos.flush();
                fos.close();

                DocumentoFirmadoVO docFirma = new DocumentoFirmadoVO();
                docFirma.setFirma(firma);
                docFirma.setFicheroFirma(f);
                docFirma.setFicheroHash64(formulario.getHashB64());
                docFirma.setTipoMime(formulario.getTipoMime());

                PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
                //ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirma);
                boolean firmaCorrecta = plugin.verificarFirma(docFirma);

                if(firmaCorrecta)
                    exito = true;
                else
                    exito = false;

                /**
                FirmaVO infoFirma = new FirmaVO();

                if (!datosFirma.isEmpty()){
                    if(datosFirma.get(0) != null){
                    infoFirma = (FirmaVO) datosFirma.get(0);
                    exito = true;
                    }else{
                        infoFirma.setValido(false);
                        exito = false;
                    }
                }else{
                    infoFirma.setValido(false);
                    exito = false;
                }
                */

                f.delete();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
       return exito;        
    }

}
