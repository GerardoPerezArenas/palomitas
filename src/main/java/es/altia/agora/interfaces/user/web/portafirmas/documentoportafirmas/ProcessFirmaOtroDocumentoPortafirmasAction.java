/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.portafirmas.documentofirma.vo.*;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
import es.altia.x509.certificados.validacion.ValidacionCertificado;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * @version $\Date$ $\Revision$
 */
public class ProcessFirmaOtroDocumentoPortafirmasAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessFirmaDocumentoPortafirmasAction";
    private static final String MESSAGE_CERTIFICADO_NO_VALIDO =  "Portafirmas.FirmaDocumentoPortafirmasForm.CertificadoNoValido";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaCorrectamente";
    private static final String MESSAGE_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaIncorrectamente";
    private static final Log log =
            LogFactory.getLog(ProcessFirmaDocumentoPortafirmasAction.class.getName());

    private String pDataSourceKey = null;
    
    private boolean usuarioValido=false;
    int idUsuarioLogueado=0;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");


        log.debug(" ========================> ProcessFirmaOtroDocumentoPortafirmasAction init");
        PrepareSearchDocumentoPortafirmasAction searchAction =  new PrepareSearchDocumentoPortafirmasAction();
        FirmaDocumentoPortafirmasActionForm concreteForm = (FirmaDocumentoPortafirmasActionForm) form;
        UsuarioValueObject usuarioVO = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        idUsuarioLogueado=usuarioVO.getIdUsuario();
        ActionForward result = null;
        
        log.debug("  concreteForm.getFirma() "+concreteForm.getFirma() );

        /* Aceptamos el rechazo, por lo que eliminamos de la sesion el parametro de valor seleccionado en caso de cancelar */
        SessionManager.removeSelectedIndex(request);

        FirmaVO firma = new FirmaVO();
        firma.setValido(false);
        try{
            firma = verificarFirma(concreteForm,usuarioVO);            
           result = mapping.findForward( searchAction.getDefaultMappingKey() );
        }catch(Exception e){
            e.printStackTrace();
        }


        if(!firma.getValido()){
            saveSingleMessage(request,new ActionMessage(MESSAGE_CERTIFICADO_NO_VALIDO));
            result = mapping.findForward( searchAction.getDefaultMappingKey() );
        }else{

            /* Retrieve DataSource key */
            this.pDataSourceKey = SessionManager.getDataSourceKey(request);
            /* Save Signature */
            boolean firmaAlmacenada = false;
            DocumentoOtroFirmaVO doc = new DocumentoOtroFirmaVO();
            if(concreteForm.getFirma()!=null){
                doc.setFirma(concreteForm.getFirma().getBytes());
                doc.setCodigoDocumento(concreteForm.getIdNumeroDocumento());
                doc.setCodigoUsuarioFirma(Integer.toString(usuarioVO.getIdUsuario()));
                doc.setCodOrganizacion(Integer.toString(usuarioVO.getOrgCod()));
                doc.setFechaFirma(Calendar.getInstance());
                                
                doc.setParams(usuarioVO.getParamsCon());                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuarioVO.getOrgCod())).getImplClassDocumentoExternoPortafirmas(Integer.toString(usuarioVO.getOrgCod()));
                
                try{
                    firmaAlmacenada = almacen.setFirmaDocumentoExternoPortafirmas(doc);

                }catch(AlmacenDocumentoTramitacionException e){
                    e.printStackTrace();
                }                
            }

            String mensaje =null;
            if(firmaAlmacenada){
                // La firma ha sido almacenada
               log.debug(" ===========> LA FIRMA HA SIDO ALMACENADA");
               mensaje = MESSAGE_SUCCESS;
            }else{
                // La firma no ha sido almacenada
                log.debug("============> LA FIRMA NO HA SIDO ALMACENADA");
                mensaje = MESSAGE_FAIL;
            }


            /* Do search */
            searchAction.execute(mapping,form,request,response);

           /* Save messages */
           saveSingleMessage(request,new ActionMessage(mensaje));
           /* Return ActionForward */

          if ( concreteForm.getDoPopUp() ) result = mapping.findForward( searchAction.getPopUpMappingKey() );
          else if ( concreteForm.getDoPrintPreview() ) result = mapping.findForward( searchAction.getPrintPreviewMappingKey() );
          else result = mapping.findForward( searchAction.getDefaultMappingKey() );
          if (_log.isDebugEnabled()) _log.debug(CLSNAME+ ".doPerform() END  Jumping to "+ result.getName() + "-----");


     }// else if

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

    public FirmaVO verificarFirma(FirmaDocumentoPortafirmasActionForm formulario,UsuarioValueObject usuario) throws Exception{
        
        byte[] documento = null;
        int codOrganizacion             = usuario.getOrgCod();
        String nombreOrganizacion  = usuario.getOrg();
        String[] params                  = usuario.getParamsCon();
        String firma                        = formulario.getFirma();
        _log.debug("**************** verificarFirma codOrg: " + codOrganizacion + " <> nombreOrg: " + nombreOrganizacion);
        _log.debug("**************** La firma del fichero es:  " + firma);

        FirmaVO infoFirma = new FirmaVO();
        
        try{

            DocumentoOtroFirmaVO doc = new DocumentoOtroFirmaVO();
            doc.setCodigoDocumento(formulario.getIdNumeroDocumento());
            doc.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
            doc.setParams(params);

            //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(Integer.toString(usuario.getOrgCod()));
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(codOrganizacion)).getImplClassDocumentoExternoPortafirmas(Integer.toString(codOrganizacion));

            try{
               // firmaAlmacenada = almacen.setFirmaDocumentoExternoPortafirmas(doc);
               doc = almacen.getDocumentoExternoPortafirmas(doc);
               documento = doc.getContenido();

            }catch(AlmacenDocumentoTramitacionException e){
                e.printStackTrace();
            }

            //DocumentoOtroFirmaVO doc = EDocExtPortafirmasManager.getInstance().getDocumento(Integer.toString(formulario.getIdNumeroDocumento()), usuario.getParamsCon());
            //documento = doc.getContenido();
            File f = File.createTempFile("prueba","temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(documento);
            fos.flush();
            fos.close();

            DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
            docFirmado.setFicheroFirma(f);
            docFirmado.setFirma(firma);
            docFirmado.setFicheroHash64(formulario.getHashB64());
            PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
            ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirmado);
            
            if (!datosFirma.isEmpty()){
                if(datosFirma.get(0) != null){
                    infoFirma = (FirmaVO) datosFirma.get(0);
                    if(infoFirma.getValido()){
                        infoFirma.setValido(verificaUsuarioFirmante(infoFirma));
                    }else{
                        infoFirma.setValido(false);
                    }//if(infoFirma.getValido())     
                }else{
                    infoFirma.setValido(false);
                }
            }else{
                infoFirma.setValido(false);
            }

            f.delete();

        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }

       return infoFirma;
    }


     public boolean verificarFirma(File documento,String firma){
       ValidacionCertificado validar = new ValidacionCertificado();
        return validar.verificarFirma(documento, firma);
    }
     
     public boolean verificaUsuarioFirmante(FirmaVO infoFirma){
        if(log.isDebugEnabled()) log.debug("verificaUsuarioFirmante() : BEGIN");
        boolean exito=false;
        try{
            String nifUsuarioFirmaDocumento=infoFirma.getNif();
            UsuarioEscritorioValueObject usuario=new UsuarioEscritorioValueObject();
            int codUsuarioFirmante;
            
            if(UsuarioManager.getInstance().buscaUsuario(nifUsuarioFirmaDocumento)!=null){
                usuario=UsuarioManager.getInstance().buscaUsuario(nifUsuarioFirmaDocumento);
                codUsuarioFirmante=usuario.getIdUsuario();
                if(codUsuarioFirmante==idUsuarioLogueado){
                    exito=true;
                    usuarioValido=true; 
                }else{
                    exito=false;
                    usuarioValido=false; 
                }//if(codUsuarioFirmante==idUsuarioLogueado)
            }else{
                exito=false;
                usuarioValido=false;        
            }////if(UsuarioManager.getInstance().buscaUsuario(nifUsuarioFirmaDocumento)!=null)
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        if(log.isDebugEnabled()) log.debug("verificaUsuarioFirmante() : BEGIN");
        return exito;
    }//verificaUsuarioFirmante

}//class