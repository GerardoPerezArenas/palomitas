/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.portafirmas.documentofirma.vo.*;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoRelacionFirmaFacadeDelegate;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultAction;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.util.commons.MimeTypes;
import es.altia.util.documentos.DocumentOperations;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.ResourceBundle;

/**
 * @version $\Date$ $\Revision$
 */
public class ProcessFirmaDocumentoPortafirmasAction extends DefaultAction {
    /*_______Constants______________________________________________*/

    private static final String CLSNAME = "ProcessFirmaDocumentoPortafirmasAction";
    private static final String MESSAGE_CERTIFICADO_NO_VALIDO = "Portafirmas.FirmaDocumentoPortafirmasForm.CertificadoNoValido";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaCorrectamente";
    private static final String MESSAGE_SUCCESS_MAIL_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaCorrectamente.FalloMail";
     private static final String MESSAGE_USER_NOT_VALID = "Sge.FirmaDocumentoTramitacionForm.UsuarioFirmaNoValido";
    
    private static final Log _log =
            LogFactory.getLog(ProcessFirmaDocumentoPortafirmasAction.class.getName());
    private String pDataSourceKey = null;
    
    private boolean usuarioValido=false;
    int idUsuarioLogueado=0;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) {
            _log.info(CLSNAME + ".doPerform() BEGIN");
        }

        PrepareSearchDocumentoPortafirmasAction searchAction = new PrepareSearchDocumentoPortafirmasAction();
        FirmaDocumentoPortafirmasActionForm concreteForm = (FirmaDocumentoPortafirmasActionForm) form;
        UsuarioValueObject usuarioVO = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        idUsuarioLogueado=usuarioVO.getIdUsuario();
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
            if (!usuarioValido) saveSingleMessage(request,new ActionMessage(MESSAGE_USER_NOT_VALID));
            else saveSingleMessage(request, new ActionMessage(MESSAGE_CERTIFICADO_NO_VALIDO));
            result = mapping.findForward(searchAction.getDefaultMappingKey());
        } else {

            boolean continuar = false;
            try {
                continuar = guardarFirma(concreteForm, usuarioVO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (continuar) {
                /* Retrieve DataSource key */
                this.pDataSourceKey = SessionManager.getDataSourceKey(request);

                /* Save Signature */
                int idUsuarioAutenticado = SessionManager.getAuthenticatedUser(request).getIdUsuario();
                int idUsuarioResponsable = concreteForm.getUsuarioFirmante();
                int tipoDocumento = concreteForm.getTipoDocumento();
                if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_EXPEDIENTE) {
                    final DocumentoFirmaVO vo = new DocumentoFirmaVO(
                            new DocumentoFirmaPK(concreteForm.getIdMunicipio(),
                            concreteForm.getIdProcedimiento(), concreteForm.getIdEjercicio(),
                            concreteForm.getIdNumeroExpediente(), concreteForm.getIdTramite(),
                            concreteForm.getIdOcurrenciaTramite(), concreteForm.getIdNumeroDocumento(),
                            idUsuarioResponsable),
                            DocumentoFirmaVO.ESTADO_FIRMA_FIRMADO, concreteForm.getFirma().getBytes(),
                            Calendar.getInstance(), null, idUsuarioAutenticado);
                    DocumentoFirmaFacadeDelegate facade = (DocumentoFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoFirmaFacadeDelegate.class);
                    facade.setDsKey(this.pDataSourceKey);
                    facade.firmarDocumentoPortafirmas(vo);
                } else if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_RELACION) {
                    final DocumentoRelacionFirmaVO vo = new DocumentoRelacionFirmaVO(
                            new DocumentoRelacionFirmaPK(concreteForm.getIdMunicipio(),
                            concreteForm.getIdProcedimiento(), concreteForm.getIdEjercicio(),
                            concreteForm.getIdNumeroExpediente(), concreteForm.getIdTramite(),
                            concreteForm.getIdOcurrenciaTramite(), concreteForm.getIdNumeroDocumento(),
                            idUsuarioResponsable),
                            DocumentoRelacionFirmaVO.ESTADO_FIRMA_FIRMADO, concreteForm.getFirma().getBytes(),
                            Calendar.getInstance(), null, idUsuarioAutenticado);
                    DocumentoRelacionFirmaFacadeDelegate facade = (DocumentoRelacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoRelacionFirmaFacadeDelegate.class);
                    facade.setDsKey(this.pDataSourceKey);
                    facade.firmarDocumentoPortafirmas(vo);
                }

                /* Do search */
                searchAction.execute(mapping, form, request, response);

                /* ---------------------------Envio de mail de confirmacion de firma ------------------------*/
                String mensaje = MESSAGE_SUCCESS;
                try {
                    _log.debug("Enviando mail");

                    String numeroExpediente = "";
                    if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_RELACION) {
                        ResourceBundle common = ResourceBundle.getBundle("common");
                        numeroExpediente += common.getString("constante.relacion");
                    }
                    numeroExpediente += concreteForm.getIdNumeroExpediente();

                    MailHelper mailHelper = new MailHelper();
                    Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
                    String email = m_ConfigTechnical.getString("mail.send.confirm.to");
                    String subject = m_ConfigTechnical.getString("mail.send.confirm.subject");
                    String content = m_ConfigTechnical.getString("mail.send.confirm.content");
                    String usuario = null;
                    String documento = null;
                    String tramite = null;
                    /* Reemplazos de campos en el asunto y el contenido del mensaje*/
                    subject = subject.replaceAll("@municipio@", (new Integer(concreteForm.getIdMunicipio())).toString());
                    subject = subject.replaceAll("@procedimiento@", concreteForm.getIdProcedimiento());
                    subject = subject.replaceAll("@ejercicio@", (new Integer(concreteForm.getIdEjercicio())).toString());
                    subject = subject.replaceAll("@expediente@", numeroExpediente);
                    if (subject.indexOf("@tramite@") != -1) {
                        DefinicionTramitesValueObject dtVO = new DefinicionTramitesValueObject();
                        dtVO.setCodMunicipio(Integer.toString(concreteForm.getIdMunicipio()));
                        dtVO.setTxtCodigo(concreteForm.getIdProcedimiento());
                        dtVO.setNumeroTramite("");
                        Vector tramites = DefinicionTramitesManager.getInstance().getListaTramites(dtVO,
                                SessionManager.getAuthenticatedUser(request).getParamsCon());
                        for (int i = 0; i < tramites.size(); i++) {
                            ElementoListaValueObject elementoVO = (ElementoListaValueObject) tramites.get(i);
                            if (elementoVO.getIdentificador().equals(Integer.toString(concreteForm.getIdTramite()))) {
                                tramite = elementoVO.getDescripcion();
                                break;
                            }
                        }
                        subject = subject.replaceAll("@tramite@", tramite);
                    }
                    subject = subject.replaceAll("@ocurrencia@", (new Integer(concreteForm.getIdOcurrenciaTramite())).toString());
                    if (subject.indexOf("@documento@") != -1) {
                        documento = concreteForm.getDescripcionDocumento();
                        subject = subject.replaceAll("@documento@", documento);
                    }
                    if (subject.indexOf("@usuario@") != -1) {
                        usuario = SessionManager.getAuthenticatedUser(request).getNombreUsu();
                        subject = subject.replaceAll("@usuario@", usuario);
                    }
                    content = content.replaceAll("@municipio@", (new Integer(concreteForm.getIdMunicipio())).toString());
                    content = content.replaceAll("@procedimiento@", concreteForm.getIdProcedimiento());
                    content = content.replaceAll("@ejercicio@", (new Integer(concreteForm.getIdEjercicio())).toString());
                    content = content.replaceAll("@expediente@", numeroExpediente);
                    if (content.indexOf("@tramite@") != -1) {
                        if (tramite == null) {
                            DefinicionTramitesValueObject dtVO = new DefinicionTramitesValueObject();
                            dtVO.setCodMunicipio(Integer.toString(concreteForm.getIdMunicipio()));
                            dtVO.setTxtCodigo(concreteForm.getIdProcedimiento());
                            dtVO.setNumeroTramite("");
                            Vector tramites = DefinicionTramitesManager.getInstance().getListaTramites(dtVO,
                                    SessionManager.getAuthenticatedUser(request).getParamsCon());
                            for (int i = 0; i < tramites.size(); i++) {
                                ElementoListaValueObject elementoVO = (ElementoListaValueObject) tramites.get(i);
                                if (elementoVO.getIdentificador().equals(Integer.toString(concreteForm.getIdTramite()))) {
                                    tramite = elementoVO.getDescripcion();
                                    break;
                                }
                            }
                        }
                        content = content.replaceAll("@tramite@", tramite);
                    }
                    content = content.replaceAll("@ocurrencia@", (new Integer(concreteForm.getIdOcurrenciaTramite())).toString());
                    if (content.indexOf("@documento@") != -1) {
                        if (documento == null) {
                            documento = concreteForm.getDescripcionDocumento();
                        }
                        content = content.replaceAll("@documento@", documento);
                    }
                    if (content.indexOf("@usuario@") != -1) {
                        if (usuario == null) {
                            usuario = SessionManager.getAuthenticatedUser(request).getNombreUsu();
                        }
                        content = content.replaceAll("@usuario@", usuario);
                    }
                    mailHelper.sendMail(email, subject, content);
                    _log.debug("mail enviado");
                } catch (MailServiceNotActivedException e) {
                    e.printStackTrace();
                } catch (MailException e) {
                    mensaje = MESSAGE_SUCCESS_MAIL_FAIL;
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new InternalErrorException(e);
                }
                /*---------------------------------------------------------------------------------*/


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


            }//if


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

    private boolean guardarFirma(FirmaDocumentoPortafirmasActionForm concreteForm, UsuarioValueObject usuario) throws WSException, TechnicalException, AlmacenDocumentoTramitacionException {
        boolean continuar = false;
        
        String codMunicipio = Integer.toString(usuario.getOrgCod());
        /************** SE GUARDA LA FIRMA EN EL GESTOR DOCUMENTAL SI PROCEDE ******************************/
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(Integer.toString(usuario.getOrgCod()));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,concreteForm.getIdProcedimiento());
        if (almacen.isPluginGestor()) {
            Hashtable<String, Object> datos = new Hashtable<String, Object>();
            datos.put("codMunicipio",codMunicipio );
            datos.put("codProcedimiento", concreteForm.getIdProcedimiento());
            datos.put("ejercicio", Integer.toString(concreteForm.getIdEjercicio()));
            datos.put("numeroExpediente", concreteForm.getIdNumeroExpediente());
            datos.put("codTramite", Integer.toString(concreteForm.getIdTramite()));
            datos.put("ocurrenciaTramite", Integer.toString(concreteForm.getIdOcurrenciaTramite()));
            datos.put("codDocumento", Integer.toString(concreteForm.getIdNumeroDocumento()));
            datos.put("codUsuario", Integer.toString(usuario.getIdUsuario()));
            datos.put("numeroDocumento", Integer.toString(concreteForm.getIdNumeroDocumento()));
            datos.put("perteneceRelacion", "false");
            datos.put("params", usuario.getParamsCon());
            datos.put("fichero", concreteForm.getFirma().getBytes());


            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", Integer.toString(usuario.getOrgCod()));
            gVO.setAtributo("codProcedimiento", concreteForm.getIdProcedimiento());
            gVO.setAtributo("ejercicio", Integer.toString(concreteForm.getIdEjercicio()));
            gVO.setAtributo("codTramite", Integer.toString(concreteForm.getIdTramite()));
            gVO.setAtributo("ocurrenciaTramite", Integer.toString(concreteForm.getIdOcurrenciaTramite()));
            gVO.setAtributo("numeroExpediente", concreteForm.getIdNumeroExpediente());
            gVO.setAtributo("numeroDocumento", Integer.toString(concreteForm.getIdNumeroDocumento()));

            Documento doc = null;
            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(usuario.getOrgCod()), concreteForm.getIdProcedimiento(), Integer.toString(concreteForm.getIdTramite()), usuario.getParamsCon());
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(concreteForm.getIdProcedimiento(), usuario.getParamsCon());
            datos.put("nombreOrganizacion", usuario.getOrg());
            datos.put("nombreProcedimiento", nombreProcedimiento);
            datos.put("codigoVisibleTramite", codigoVisibleTramite);
            datos.put("extension", ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);

            String nombreDocumento = null;
            String nombreCarpetaExpediente = null;
            if (concreteForm.getTipoDocumento() == 0) {
                // Documento no perteneciente a una relación
                datos.put("perteneceRelacion", "false");
                nombreDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, usuario.getParamsCon());
                nombreCarpetaExpediente = ((String) gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION);
            } else {
                // Documento perteneciente a una relación
                gVO.setAtributo("numeroRelacion", concreteForm.getIdNumeroExpediente());
                datos.put("numeroRelacion", concreteForm.getIdNumeroExpediente());
                nombreDocumento = DocumentosRelacionExpedientesManager.getInstance().getNombreDocumentoRelacionGestor(gVO, usuario.getParamsCon());
                datos.put("perteneceRelacion", "true");
                nombreCarpetaExpediente = ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + ((String) gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION);
            }
            datos.put("nombreDocumento", nombreDocumento);

            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
            ResourceBundle bundle = ResourceBundle.getBundle("documentos");        
            String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
            
            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + usuario.getOrg());
            listaCarpetas.add(concreteForm.getIdProcedimiento() + ConstantesDatos.GUION + nombreProcedimiento);            
            listaCarpetas.add(nombreCarpetaExpediente);

            datos.put("listaCarpetas", listaCarpetas);

            String editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(usuario.getOrgCod(),
                    concreteForm.getIdNumeroExpediente(),concreteForm.getIdTramite(),
                    concreteForm.getIdOcurrenciaTramite(),concreteForm.getIdNumeroDocumento(),
                    concreteForm.getTipoDocumento() != 0, usuario.getParamsCon());
            
            String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                    editorTexto, nombreDocumento);
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime) + ConstantesDatos.DOT + ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);
            datos.put("tipoMime", tipoMime);

            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR);
            try {
                almacen.setFirmaDocumento(doc);
                continuar = true;
            } catch (AlmacenDocumentoTramitacionException e) {
                continuar = false;
                e.printStackTrace();
                _log.error("Error al guardar la firma en el gestor documental: " + e.getMessage());
                throw e;
            }
        } else {
            continuar = true;
        }

        /****************************************************************************************************/
        return continuar;
    }

    public FirmaVO verificarFirma(FirmaDocumentoPortafirmasActionForm formulario, UsuarioValueObject usuario) throws Exception {
        

        byte[] documento = null;
        int codOrganizacion = usuario.getOrgCod();
        String nombreOrganizacion = usuario.getOrg();
        String[] params = usuario.getParamsCon();
        String firma = formulario.getFirma();
        _log.debug("**************** verificarFirma codOrg: " + codOrganizacion + " <> nombreOrg: " + nombreOrganizacion);
        _log.debug("**************** La firma del fichero es:  " + firma);


        _log.debug("====================> " + this.getClass().getName() + ".verificaFirma codOrganizacion: " + codOrganizacion);
        _log.debug("====================> " + this.getClass().getName() + ".verificaFirma nombreOrganizacion: " + nombreOrganizacion);
        _log.debug("====================> " + this.getClass().getName() + ".verificaFirma firma: " + firma);

        FirmaVO infoFirma = new FirmaVO();
        
        try {
            documento = this.getContenidoDocumento(formulario, Integer.toString(codOrganizacion), nombreOrganizacion, params);
            File f = File.createTempFile("prueba", "temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(documento);
            fos.flush();
            fos.close();

            DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
            docFirmado.setFicheroFirma(f);
            docFirmado.setFirma(firma);
            docFirmado.setFicheroHash64(formulario.getHashB64());
            docFirmado.setTipoMime(formulario.getTipoMime());
           
            PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
            ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirmado);
          
            if (!datosFirma.isEmpty()){
                if(datosFirma.get(0) != null){
                infoFirma = (FirmaVO) datosFirma.get(0); 
                if(infoFirma.getValido()) infoFirma.setValido(verificaUsuarioFirmante(infoFirma));
                }else{
                    infoFirma.setValido(false);
                }
            }else{
                infoFirma.setValido(false);
            }
            
            f.delete();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return infoFirma;
    }

    
  public boolean verificaUsuarioFirmante(FirmaVO infoFirma)
    {
        boolean exito=false;
        try{
            
            String nifUsuarioFirmaDocumento=infoFirma.getNif();
            UsuarioEscritorioValueObject usuario=new UsuarioEscritorioValueObject();
            int codUsuarioFirmante;
            
            if(UsuarioManager.getInstance().buscaUsuario(nifUsuarioFirmaDocumento)!=null)
            {
                usuario=UsuarioManager.getInstance().buscaUsuario(nifUsuarioFirmaDocumento);
                codUsuarioFirmante=usuario.getIdUsuario();
                if(codUsuarioFirmante==idUsuarioLogueado)
                {
                    exito=true;
                    usuarioValido=true; 
                }
                else
                {
                    exito=false;
                    usuarioValido=false; 
                }
            }
            else
            {
                exito=false;
                usuarioValido=false;        
            }
                                             
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return exito;
    }
    

    /**
     * Se recupera el contenido del documento para poder verificar la firma
     */
    private byte[] getContenidoDocumento(FirmaDocumentoPortafirmasActionForm formulario, String codOrganizacion, String nombreOrganizacion, String[] params) throws TechnicalException, WSException, AlmacenDocumentoTramitacionException {
        byte[] resultado = null;

        _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento init");

        Hashtable<String, Object> datos = new Hashtable<String, Object>();

        datos.put("codMunicipio", Integer.toString(formulario.getIdMunicipio()));
        datos.put("numeroExpediente", formulario.getIdNumeroExpediente());
        datos.put("codTramite", Integer.toString(formulario.getIdTramite()));
        datos.put("ocurrenciaTramite", Integer.toString(formulario.getIdOcurrenciaTramite()));
        datos.put("numeroDocumento", Integer.toString(formulario.getIdNumeroDocumento()));
        datos.put("params", params);

        if (formulario.getTipoDocumento() == 0) {
            datos.put("perteneceRelacion", "false");
        } else {
            datos.put("numeroRelacion", formulario.getIdNumeroExpediente());
            datos.put("perteneceRelacion", "true");
        }


        _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento idMunicipio " + formulario.getIdMunicipio());
        _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento idNumeroExpediente: " + formulario.getIdNumeroExpediente());
        _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento getIdTramite: " + formulario.getIdTramite());
        _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento getIdOcurrenciaTramite: " + formulario.getIdOcurrenciaTramite());
        _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento getIdNumeroDocumento: " + formulario.getIdNumeroDocumento());

        ResourceBundle bundle = ResourceBundle.getBundle("common");

        String tipoMime, extension = null;

        String editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(formulario.getIdMunicipio(),
                formulario.getIdNumeroExpediente(),formulario.getIdTramite(),
                formulario.getIdOcurrenciaTramite(),formulario.getIdNumeroDocumento(),
                formulario.getTipoDocumento() != 0, params);

        String codMunicipio =  Integer.toString(formulario.getIdMunicipio());                
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,formulario.getIdProcedimiento());
        
        Documento doc = null;
        int tipoDocumento = -1;
        if (!almacen.isPluginGestor()) {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        } else {
            String codProcedimiento = formulario.getIdProcedimiento();
            String ejercicio = Integer.toString(formulario.getIdEjercicio());


            _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento codProcedimiento " + codProcedimiento);

            // Se obtiene el nombre del documento a mostrar porque se necesita para el caso de que se venga de firmar el documento
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", codMunicipio);
            gVO.setAtributo("codProcedimiento", codProcedimiento);
            gVO.setAtributo("ejercicio", ejercicio);
            gVO.setAtributo("codTramite", Integer.toString(formulario.getIdTramite()));
            gVO.setAtributo("ocurrenciaTramite", Integer.toString(formulario.getIdOcurrenciaTramite()));
            gVO.setAtributo("numeroExpediente", formulario.getIdNumeroExpediente());
            gVO.setAtributo("numeroRelacion", formulario.getIdNumeroExpediente());
            gVO.setAtributo("numeroDocumento", Integer.toString(formulario.getIdNumeroDocumento()));

            // Se recupera el nombre del documento para poder componer la ruta y leerlo en el gestor documental
            String nombreDocumento = "";
            if (formulario.getTipoDocumento() == 0) {
                nombreDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params);
            } else {
                gVO.setAtributo("numeroRelacion", formulario.getIdNumeroExpediente());
                nombreDocumento = DocumentosRelacionExpedientesManager.getInstance().getNombreDocumentoRelacionGestor(gVO, params);
            }
            datos.put("nombreDocumento", nombreDocumento);

            tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                    editorTexto,
                    nombreDocumento);
            extension = MimeTypes.guessExtensionFromMimeType(tipoMime);
            datos.put("extension", extension);
            datos.put("tipoMime", tipoMime);
            
            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(formulario.getIdMunicipio()), codProcedimiento, Integer.toString(formulario.getIdTramite()), params);
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
            datos.put("codProcedimiento", codProcedimiento);
            datos.put("nombreOrganizacion", nombreOrganizacion);
            datos.put("nombreProcedimiento", nombreProcedimiento);
            datos.put("codigoVisibleTramite", codigoVisibleTramite);
            datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);

            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
            ResourceBundle bundleDoc = ResourceBundle.getBundle("documentos");        
            String carpetaRaiz = bundleDoc.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + formulario.getIdMunicipio() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
            
            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(formulario.getIdMunicipio() + ConstantesDatos.GUION + nombreOrganizacion);
            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);

            if (formulario.getTipoDocumento() == 0) {
                listaCarpetas.add(((String) gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
            } else {
                String numeroRelacion = ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + (String) gVO.getAtributo("numeroRelacion");
                listaCarpetas.add(numeroRelacion.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
            }

            datos.put("listaCarpetas", listaCarpetas);

            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        }

        // Se recupera el contenido del documento
        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
        resultado = almacen.getDocumento(doc);

        if (resultado != null) {
            _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento codProcedimiento resultado!=null: " + resultado.length);
        } else {
            _log.debug("====================> " + this.getClass().getName() + ".getContenidoDocumento codProcedimiento resultado==null: ");
        }
        return resultado;
    }
}//class
