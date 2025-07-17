package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
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
import java.sql.Connection;
import java.util.*;

/**
 *
 * @author Tiffany
 */
public class ProcessFirmaDocumentoExternoNotificacionPortafirmasAction extends DefaultAction {
    /*_______Constants______________________________________________*/

    private static final String CLSNAME = "ProcessFirmaDocumentoExternoNotificacionPortafirmasAction";
    private static final String MESSAGE_CERTIFICADO_NO_VALIDO = "Portafirmas.FirmaDocumentoPortafirmasForm.CertificadoNoValido";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaCorrectamente";
    private static final String MESSAGE_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaIncorrectamente";
    private static final Log log = LogFactory.getLog(ProcessFirmaDocumentoPortafirmasAction.class.getName());
    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) {
            _log.info(CLSNAME + ".doPerform() BEGIN");
        }


        log.debug(" ========================> ProcessFirmaDocumentoExternoNotificacionPortafirmasAction init");
        PrepareSearchDocumentoPortafirmasAction searchAction = new PrepareSearchDocumentoPortafirmasAction();
        FirmaDocumentoPortafirmasActionForm concreteForm = (FirmaDocumentoPortafirmasActionForm) form;
        UsuarioValueObject usuarioVO = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        ActionForward result = null;

        /* Aceptamos el rechazo, por lo que eliminamos de la sesion el parametro de valor seleccionado en caso de cancelar */
        SessionManager.removeSelectedIndex(request);

        FirmaVO firma = new FirmaVO();
        firma.setValido(false);
        try {
            firma = verificarFirma(concreteForm, usuarioVO);
            result = mapping.findForward(searchAction.getDefaultMappingKey());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!firma.getValido()) {
            saveSingleMessage(request, new ActionMessage(MESSAGE_CERTIFICADO_NO_VALIDO));
            result = mapping.findForward(searchAction.getDefaultMappingKey());
        } else {

            /* Retrieve DataSource key */
            this.pDataSourceKey = SessionManager.getDataSourceKey(request);
            /* Save Signature */
            boolean firmaAlmacenada = false;            
            AdjuntoNotificacionVO doc = new AdjuntoNotificacionVO();
            if (concreteForm.getFirma() != null) {
                
                /*
                try {
                    doc.setFirma(concreteForm.getFirma());
                    doc.setIdDocExterno(concreteForm.getIdNumeroDocumento());
                    doc.setParams(usuarioVO.getParamsCon());
                    doc.setCodUsuarioFirmaOtro(usuarioVO.getIdUsuario());                    
                    doc.setFechaFirma(Calendar.getInstance());
                    doc.setPlataformaFirma(concreteForm.getPlataformaFirma());
                    
                    AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().
                            getImplClass(Integer.toString(usuarioVO.getOrgCod()));

                    firmaAlmacenada = almacen.setFirmaDocumentoExternoNotificacion(doc);
                    
                } catch (AlmacenDocumentoTramitacionException ex) {
                    Logger.getLogger(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName()).log(Level.SEVERE, null, ex);
                }*/


            }

            String mensaje = null;
            if (firmaAlmacenada) {
                // La firma ha sido almacenada
                log.debug(" ===========> LA FIRMA HA SIDO ALMACENADA");
                mensaje = MESSAGE_SUCCESS;
            } else {
                // La firma no ha sido almacenada
                log.debug("============> LA FIRMA NO HA SIDO ALMACENADA");
                mensaje = MESSAGE_FAIL;
            }


            /* Do search */
            searchAction.execute(mapping, form, request, response);

            /* Save messages */
            saveSingleMessage(request, new ActionMessage(mensaje));
            /* Return ActionForward */

            if (concreteForm.getDoPopUp()) {
                result = mapping.findForward(searchAction.getPopUpMappingKey());
            } else if (concreteForm.getDoPrintPreview()) {
                result = mapping.findForward(searchAction.getPrintPreviewMappingKey());
            } else {
                result = mapping.findForward(searchAction.getDefaultMappingKey());
            }
            if (_log.isDebugEnabled()) {
                _log.debug(CLSNAME + ".doPerform() END  Jumping to " + result.getName() + "-----");
            }


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

    public FirmaVO verificarFirma(FirmaDocumentoPortafirmasActionForm formulario, UsuarioValueObject usuario) throws Exception {
        boolean exito = false;

        byte[] documento = null;
        int codOrganizacion = usuario.getOrgCod();
        String nombreOrganizacion = usuario.getOrg();
        String[] params = usuario.getParamsCon();
        String firma = formulario.getFirma();
        _log.debug("**************** verificarFirma codOrg: " + codOrganizacion + " <> nombreOrg: " + nombreOrganizacion);
        _log.debug("**************** La firma del fichero es:  " + firma);
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        FirmaVO infoFirma = new FirmaVO();
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            String codDocumento = Integer.toString(formulario.getIdNumeroDocumento());
            String numExpediente = formulario.getIdNumeroExpediente();
            String codTramite = Integer.toString(formulario.getIdTramite());
            String ocuTramite  = Integer.toString(formulario.getIdOcurrenciaTramite());
            String codMunicipio = Integer.toString(formulario.getIdMunicipio());
                                      
            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("codDocumento",codDocumento);
            datos.put("codMunicipio",codMunicipio);             
            datos.put("numeroExpediente",numExpediente);
            datos.put("codTramite",codTramite);
            datos.put("ocurrenciaTramite",ocuTramite);            
            String codProcedimiento = numExpediente.split("/")[1];            
            
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
            int TIPO_DOCUMENTO = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            if(almacen.isPluginGestor()) TIPO_DOCUMENTO = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            log.debug("TIPO_DOCUMENTO: " + TIPO_DOCUMENTO);

            Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, TIPO_DOCUMENTO);
            doc = almacen.getDocumentoExternoNotificacion(doc,con);

            documento = doc.getFichero();
                
            File f = File.createTempFile("prueba", "temp");
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
                }else{
                    infoFirma.setValido(false);
                }     
            }else{
                infoFirma.setValido(false);
            }
            
            f.delete();

            f.delete();

        } catch (BDException e) {
            e.printStackTrace();
            _log.error("Error al recuperar conexión a la BBDD: " + e.getMessage());
            throw e;
        } 
        catch (AlmacenDocumentoTramitacionException e) {
            e.printStackTrace();
            _log.error("Error al verificar la firma del documento externo de una notificación: " + e.getMessage());
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            _log.error("Error al verificar la firma del documento externo de una notificación: " + e.getMessage());
        }
        finally{
            try{
                adapt.devolverConexion(con);
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return infoFirma;
    }

    public boolean verificarFirma(File documento, String firma) {
        ValidacionCertificado validar = new ValidacionCertificado();
        return validar.verificarFirma(documento, firma);
    }

    
    /*
    private void notificarSiguienteFirmante(FirmaDocumentoPortafirmasActionForm form, String [] params) throws TechnicalException {
        Vector<UsuariosGruposManager> usuarios = new Vector<UsuariosGruposManager>();
        FirmasDocumentoProcedimientoVO firma = FirmasDocumentoProcedimientoManager.getInstance().getFirmaSiguiente(form.getIdNumFirma(),form.getIdPresentado(), params);
        if (firma!=null){ //Si no se ha llegado al final del circuito
            usuarios = UsuariosGruposManager.getInstance().getUsuariosFirmantesUnidadCargo(firma.getMunicipio(), firma.getUor(), firma.getCargo(), params);
        }
        if (!usuarios.isEmpty()) {
            String nombreUor = firma.getNomUor();
            String nombreCargo = firma.getNomCargo();
            String numeroExpediente = form.getIdNumeroExpediente();
            String nombreDocumento = form.getDescripcionDocumento();
            String procedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(form.getIdProcedimiento(), params);           
            
            Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");        
            String subject = m_ConfigTechnical.getString("mail.send.siguienteFirmante.subject");
            subject = subject.replaceAll("@documento@", nombreDocumento);            
            
            
            String content = "";
            String cargoTodos = CargosDAO.getInstance().getCargoPorCodigoVisible("TD", params).getUor_cod();
            if (firma.getUsuario()!=null && !"".equals(firma.getUsuario())) {
               content = m_ConfigTechnical.getString("mail.send.siguienteFirmante.user.content");
            } else if (firma.getCargo()!=null && !"".equals(firma.getCargo()) && !cargoTodos.equals(firma.getCargo())) {
               content = m_ConfigTechnical.getString("mail.send.siguienteFirmante.cargo.content");
            } else if (firma.getUor()!=null && !"".equals(firma.getUor())) {
               content = m_ConfigTechnical.getString("mail.send.siguienteFirmante.uor.content");
            } 
            content = content.replaceAll("@uor@", nombreUor);            
            content = content.replaceAll("@cargo@", nombreCargo);            
            content = content.replaceAll("@procedimiento@", procedimiento);            
            content = content.replaceAll("@expediente@", numeroExpediente);            
            content = content.replaceAll("@documento@", nombreDocumento);            
            
            MailHelper mailHelper = new MailHelper();     
            for (Iterator it=usuarios.iterator();it.hasNext();) {
                String email = ((UsuariosGruposValueObject)it.next()).getEmail();
                if (email==null || "".equals(email)) continue;
                try {
                    mailHelper.sendMail(email, subject, content);
                } catch (MailException ex) {
                    Logger.getLogger(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName()).log(Level.SEVERE, null, ex);
                    throw new TechnicalException("Error enviando mail para firmar", ex);
                } catch (MailServiceNotActivedException ex) {
                    Logger.getLogger(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName()).log(Level.SEVERE, null, ex);
                    throw new TechnicalException("Error enviando mail para firmar. Servicio de mail no activo", ex);
                }
            }
            
        }
    } */
}