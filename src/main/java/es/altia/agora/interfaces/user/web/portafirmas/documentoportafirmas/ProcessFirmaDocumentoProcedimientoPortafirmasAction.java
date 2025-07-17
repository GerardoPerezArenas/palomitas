package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.CargosDAO;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.portafirmas.documentofirma.vo.*;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasManager;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.FirmasDocumentoProcedimientoManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 *
 * @author Tiffany
 */
public class ProcessFirmaDocumentoProcedimientoPortafirmasAction extends DefaultAction {
    /*_______Constants______________________________________________*/

    private static final String CLSNAME = "ProcessFirmaDocumentoPortafirmasAction";
    private static final String MESSAGE_CERTIFICADO_NO_VALIDO = "Portafirmas.FirmaDocumentoPortafirmasForm.CertificadoNoValido";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaCorrectamente";
    private static final String MESSAGE_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.FirmaGuardadaIncorrectamente";
    private static final String MESSAGE_NO_PERMITE_SUBSANACION = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoNoPermiteSubsanacion";
    private static final Log log =
            LogFactory.getLog(ProcessFirmaDocumentoPortafirmasAction.class.getName());
    private static final int FLUJO_LISTA_TRAMITES_FAVORABLE = 1;
    private String pDataSourceKey = null;
    
    private boolean usuarioValido=false;
    int idUsuarioLogueado=0;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) {
            _log.info(CLSNAME + ".doPerform() BEGIN");
        }


        log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction init");
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
            // SOLO PRUEBAS
            firma = verificarFirma(concreteForm, usuarioVO);
            //firma.setValido(true);
            //result = mapping.findForward(searchAction.getDefaultMappingKey());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!firma.getValido()) {
            saveSingleMessage(request, new ActionMessage(MESSAGE_CERTIFICADO_NO_VALIDO));
            result = mapping.findForward(searchAction.getDefaultMappingKey());
        } else {

            log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction firma del documento correcta");
            /* Retrieve DataSource key */
            this.pDataSourceKey = SessionManager.getDataSourceKey(request);
            /* Save Signature */
            String mensaje = null;
            boolean firmaAlmacenada = false;
            boolean realizaFirma = true;
            boolean permiteSubsanacion = permiteSubsanacionFirma(concreteForm, usuarioVO);
            log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction permite subsanación ? " + permiteSubsanacion);
            
            if (permiteSubsanacion) {
                log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction se permite la tramitación por subsanación del trámite");
                GeneralValueObject tramite = permiteTramitacionFirma(concreteForm, usuarioVO);
                Vector tramites;
                if (tramite != null) {
                    
                    log.debug(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName() + "--> Se realiza el avance del trámite");
                    concreteForm.setIdTramite(Integer.parseInt((String)tramite.getAtributo("codTramite")));
                    Vector tramitesIniciar = obtenerTramitesSiguientes(concreteForm, usuarioVO);                    
                    log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction numero tramitesIniciar: " + tramitesIniciar.size());
                    
                    String[] params = usuarioVO.getParamsCon();
                    TramitacionExpedientesValueObject traExpVO = new TramitacionExpedientesValueObject();
                    traExpVO.setListaEMailsAlIniciar(new Vector());
                    traExpVO.setListaEMailsAlFinalizar(new Vector());
                    traExpVO.setCodUsuario(Integer.toString(usuarioVO.getIdUsuario()));
                    traExpVO.setNombreUsuario(usuarioVO.getNombreUsu());
                    traExpVO.setCodIdiomaUsuario(usuarioVO.getIdioma());            
                    traExpVO.setListaTramitesIniciar(tramitesIniciar);
                    traExpVO.setCodMunicipio((String)tramite.getAtributo("codMunicipio"));
                    traExpVO.setCodOrganizacion((String)tramite.getAtributo("codMunicipio"));
                    traExpVO.setCodProcedimiento((String)tramite.getAtributo("codProcedimiento"));
                    traExpVO.setEjercicio((String)tramite.getAtributo("ejercicio"));
                    traExpVO.setNumeroExpediente((String)tramite.getAtributo("numeroExpediente"));
                    traExpVO.setNumero((String)tramite.getAtributo("numeroExpediente"));
                    traExpVO.setCodTramite((String)tramite.getAtributo("codTramite"));
                    traExpVO.setOcurrenciaTramite((String)tramite.getAtributoONulo("ocurrenciaTramite"));
                    try {
                        
                        log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction antes de llamar a finalizarConSubsanacion:: ");
                        tramites = TramitacionExpedientesManager.getInstance().finalizarConSubsanacion(traExpVO, params, true);
                        log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction después de llamar a finalizarConSubsanacion:: " + tramites.size());
                        
                        if (tramites == null) {
                            realizaFirma = false;
                        } else
                            OperacionesExpedienteManager.getInstance().registrarFinalizarTramite(traExpVO, true, params);
                    } catch (WSException wse) {
                        ponerMensajeFalloSW(traExpVO, wse);
                        if (wse.isMandatoryExecution()) {
                            realizaFirma = false;
                        }
                    } catch (TramitacionException te) {
                        traExpVO.setMensajeSW(te.getMessage());
                        realizaFirma = false;
                    } catch (EjecucionSWException eswe) {
                        ponerMensajeFalloSW(traExpVO, eswe);
                        if (eswe.isStopEjecucion()) {
                            realizaFirma = false;
                        }
                    }            
                    log.debug(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName() + "<-- Se realiza el avance del trámite");
                } else {
                    log.debug(" ========================> ProcessFirmaDocumentoProcedimientoPortafirmasAction NO PERMITE SUBSANACIÓN");
                    realizaFirma = false;
                    mensaje = MESSAGE_NO_PERMITE_SUBSANACION;
                }
            }
            
            if (realizaFirma) {
                DocumentoProcedimientoFirmaVO doc = new DocumentoProcedimientoFirmaVO();            
                if (concreteForm.getFirma() != null && realizaFirma) {
                    try {
                        doc.setFirma(concreteForm.getFirma().getBytes());
                        doc.setIdPresentado(concreteForm.getIdPresentado());
                        doc.setCodigoUsuarioFirma(Integer.toString(usuarioVO.getIdUsuario()));
                        doc.setFechaFirma(Calendar.getInstance());
                        doc.setObservaciones(concreteForm.getObservaciones());
                        doc.setIdNumFirma(concreteForm.getIdNumFirma());
                        doc.setCodOrganizacion(Integer.toString(usuarioVO.getOrgCod()));
                        doc.setCodigoDocumento(concreteForm.getIdNumeroDocumento());
                        doc.setParams(usuarioVO.getParamsCon());
                         

                        int codMunicipio = usuarioVO.getOrgCod();
                        String numeroExpediente = concreteForm.getIdNumeroExpediente();
                        String[] datosExp = numeroExpediente.split("/");
                        String ejercicio = datosExp[0];
                        String codProcedimiento = datosExp[1];
                        String[] params = usuarioVO.getParamsCon();
                        String nombreOrganizacion = usuarioVO.getOrg();
                        int codDocumentoPresentado = concreteForm.getIdPresentado();
                        int numeroDocumento = concreteForm.getIdNumeroDocumento();
                        String observaciones = concreteForm.getObservaciones();
                        int idFirma = concreteForm.getIdNumFirma();
                        
                        int tipoDocumento = -1;

                        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuarioVO.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuarioVO.getOrgCod()),codProcedimiento);                        
                        Hashtable<String,Object> datos = new Hashtable<String,Object>();
                        datos.put("codDocumento",Integer.toString(codDocumentoPresentado));
                        datos.put("nombreOrganizacion", nombreOrganizacion);
                        datos.put("codProcedimiento", codProcedimiento);                        
                        datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
                        datos.put("codMunicipio", Integer.toString(codMunicipio));
                        datos.put("ejercicio", ejercicio);    
                        datos.put("params", params);
                        datos.put("numeroExpediente", numeroExpediente);
                        datos.put("codUsuario", Integer.toString(usuarioVO.getIdUsuario()));
                        datos.put("fichero",concreteForm.getFirma().getBytes());
                        datos.put("numeroDocumento",Integer.toString(numeroDocumento));
                        if(observaciones!=null) datos.put("observaciones",observaciones);
                        datos.put("idFirma",Integer.toString(idFirma));
                        
                        if (!almacen.isPluginGestor()) {
                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                            datos.put("origen", ConstantesDatos.ORIGEN_ADJUNTO_DOC_PRESENTADO_BBDD);
                        } else {                            
                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                            
                            ResourceBundle bundle = ResourceBundle.getBundle("documentos");                     
                            String carpetaRaiz = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                          
                            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);                            
                            String nombreDocumento = DocsProcedimientoPortafirmasManager.getInstance().getNombreDocumentoExpediente(tipoDocumento, numeroExpediente, codProcedimiento, params);

                            datos.put("nombreDocumento", nombreDocumento);
                            datos.put("extension", ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);
                            datos.put("nombreProcedimiento", nombreProcedimiento);
                            datos.put("origen", almacen.getNombreServicio());
                            
                            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                            traductor.setApl_cod(4);
                            traductor.setIdi_cod(usuarioVO.getIdioma());

                            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                            ArrayList<String> listaCarpetas = new ArrayList<String>();
                            listaCarpetas.add(carpetaRaiz);
                            listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + nombreOrganizacion);
                            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                            listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
                            listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
                            datos.put("listaCarpetas", listaCarpetas);
                            
                        }
                        
                        Documento documento = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                        
                        firmaAlmacenada = almacen.setFirmaDocumentoExpediente(documento);

                    } catch (AlmacenDocumentoTramitacionException ex) {
                        Logger.getLogger(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (firmaAlmacenada) {
                    Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");                        
                    if ("si".equals(m_ConfigTechnical.getString("mail.portafirmas.activo"))) {                    
                        notificarSiguienteFirmante(concreteForm, usuarioVO.getParamsCon());
                    }
                    // La firma ha sido almacenada
                    log.debug(" ===========> LA FIRMA HA SIDO ALMACENADA");
                    mensaje = MESSAGE_SUCCESS;
                } else {
                    // La firma no ha sido almacenada
                    log.debug("============> LA FIRMA NO HA SIDO ALMACENADA");
                    mensaje = MESSAGE_FAIL;
                }
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
        byte[] contenido = null;
        int codOrganizacion = usuario.getOrgCod();
        String nombreOrganizacion = usuario.getOrg();
        String[] params = usuario.getParamsCon();
        String firma = formulario.getFirma();
        _log.debug("**************** verificarFirma codOrg: " + codOrganizacion + " <> nombreOrg: " + nombreOrganizacion);
        _log.debug("**************** La firma del fichero es:  " + firma);

        FirmaVO infoFirma = new FirmaVO();
        try {
                       
            String numeroExpediente = formulario.getIdNumeroExpediente();
            int ejercicio    = formulario.getIdEjercicio();
            int codDocumento = formulario.getIdNumeroDocumento();
            String codProcedimiento = formulario.getIdProcedimiento();
            //int codMunicipio = usuario.getOrgCod();
            String codMunicipio = Integer.toString(usuario.getOrgCod());
            String tipoMime = formulario.getTipoMime();
            
            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("codDocumento",Integer.toString(codDocumento));
            datos.put("codMunicipio",codMunicipio);
            datos.put("ejercicio",Integer.toString(ejercicio));
            datos.put("numeroExpediente",numeroExpediente);
            datos.put("params",params);
            datos.put("codProcedimiento",codProcedimiento);                       
            
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
            int tipoDocumento = -1;
            
            if(!almacen.isPluginGestor())
                  tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
             else{
                ResourceBundle bundle = ResourceBundle.getBundle("documentos");
                /*
               String tipoPlugin = bundle.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);
               String nombreGestor   = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + tipoPlugin + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR);
               String carpetaRaiz       = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
               */
               String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 
                
               String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);        
               datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
               //datos.put("origen",nombreGestor);
               datos.put("origen",almacen.getNombreServicio());

               TraductorAplicacionBean traductor = new TraductorAplicacionBean();
               traductor.setApl_cod(4);
               traductor.setIdi_cod(usuario.getIdioma());

               /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
               ArrayList<String> listaCarpetas = new ArrayList<String>();
               listaCarpetas.add(carpetaRaiz);
               listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
               listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
               listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
               listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
               datos.put("listaCarpetas",listaCarpetas);
               tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;               
            }
            
            
            try {
                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                
                 if(almacen.isPluginGestor()){
                    // Se recupera la información del documento de la base de datos para extraer la extensión del fichero y componer el nombre del fichero
                    // para poder obtener su contenido del gestor
                    doc = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(doc);
                    log.debug(" ********* nombredocumento: " + doc.getNombreDocumento());
                    log.debug(" ********* extensiondocumento: " + doc.getExtension());
                    DocumentoGestor docGestor = (DocumentoGestor)doc;
                    docGestor.setNombreFicheroCompleto(codDocumento + ConstantesDatos.GUION + doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension());
                    doc = almacen.getDocumentoPresentado(docGestor);
                }else
                    doc = almacen.getDocumentoPresentado(doc);
                
                // CONTINUAR AQUI PARA RECUPERAR EL DOCUMENTO DEL GESTOR. 
                // HABRÁ QUE ELIMINAR EL METODO getDocumentoExpediente de todos los plugins y de la interfaz principal
                // uya que no se usará.                
                contenido = doc.getFichero();
                
            } catch (AlmacenDocumentoTramitacionException e) {
                e.printStackTrace();
            }

            File f = File.createTempFile("prueba", "temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(contenido);
            fos.flush();
            fos.close();

            DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
            docFirmado.setFicheroFirma(f);
            docFirmado.setFirma(firma);
            docFirmado.setFicheroHash64(formulario.getHashB64());
            docFirmado.setTipoMime(tipoMime);
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
            } else {
                infoFirma.setValido(false);
            }
            f.delete();
            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return infoFirma;
    }
    
    

    public boolean verificarFirma(File documento, String firma) {
        ValidacionCertificado validar = new ValidacionCertificado();
        return validar.verificarFirma(documento, firma);
    }

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
    }

    private boolean permiteSubsanacionFirma (FirmaDocumentoPortafirmasActionForm form, UsuarioValueObject usuario) 
            throws TechnicalException {
        _log.debug(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName() + "--> permiteSubsanacionFirma");
        String[] params = usuario.getParamsCon();
        int documento = form.getIdNumeroDocumento();
        int municipio = form.getIdMunicipio();
        String procedimiento = form.getIdProcedimiento();
        int docPresentado = form.getIdPresentado();
        int idNumFirma = form.getIdNumFirma();
        
        boolean permiteSubsanacion = FirmasDocumentoProcedimientoManager.getInstance().permiteSubsanacion(documento, 
                municipio, procedimiento, docPresentado, idNumFirma, params);
        
        _log.debug(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName() + "<-- permiteSubsanacionFirma: " + permiteSubsanacion);
        return permiteSubsanacion;
    }
    
    private GeneralValueObject permiteTramitacionFirma (FirmaDocumentoPortafirmasActionForm form, UsuarioValueObject usuario) 
            throws TechnicalException {
        _log.debug(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName() + "--> permiteTramitacionFirma");
        GeneralValueObject tramite = null;
        String[] params = usuario.getParamsCon();
        String expediente = form.getIdNumeroExpediente();
        //tramite = TramitacionExpedientesManager.getInstance().expedientePermiteSubsanacion(expediente, params);
        tramite = TramitacionExpedientesManager.getInstance().expedientePermiteSubsanacion(expediente, params);        
        _log.debug(ProcessFirmaDocumentoProcedimientoPortafirmasAction.class.getName() + "<-- permiteTramitacionFirma: " + tramite);
        return tramite;
    }

    private Vector<TramitacionExpedientesValueObject> obtenerTramitesSiguientes (FirmaDocumentoPortafirmasActionForm form, UsuarioValueObject usuario) 
            throws TechnicalException {
        _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "--> obtenerTramitesSiguientes");
        Vector<TramitacionExpedientesValueObject> listaTramites = null;
        String[] params = usuario.getParamsCon();
        String municipio = String.valueOf(form.getIdMunicipio());
        String procedimiento = form.getIdProcedimiento();
        String tramite = String.valueOf(form.getIdTramite());
        listaTramites = TramitacionExpedientesManager.getInstance().obtenerTramitesSiguientes(municipio, procedimiento, tramite, FLUJO_LISTA_TRAMITES_FAVORABLE, params);
        _log.debug(ProcessSubsanaDocumentoProcedimientoPortafirmasAction.class.getName() + "<-- obtenerTramitesSiguientes: " + listaTramites.size() + listaTramites.toString());
        return listaTramites;
    }

    /**
     * Pone en el VO el mensaje adecuado con el error que devuelve un servicio
     * web en una WSException
     * @param tramExpVO
     * @param wse
     */
    private void ponerMensajeFalloSW(TramitacionExpedientesValueObject tramExpVO, WSException wse) {
        String msg = "Fallo en ejecución de servicio web ";
	if (wse.isMandatoryExecution()) {
            msg += "obligatorio. ";
	} else {
            msg += "no obligatorio. ";
	}
	tramExpVO.setMensajeSW(msg + wse.getMessage());
    }

    private void ponerMensajeFalloSW(TramitacionExpedientesValueObject tramExpVO, EjecucionSWException eswe) {
	String msg = "Fallo en ejecución de servicio web obligatorio. ";
	tramExpVO.setMensajeSW(msg + eswe.getMensaje());
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
}