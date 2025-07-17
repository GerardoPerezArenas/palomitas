package es.altia.flexia.interfaces.user.web.documentospresentados.action;

import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.CargosDAO; 
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.FirmasDocumentoExpedienteVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoBBDD;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor; 
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteDocumentoForm;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
import es.altia.agora.business.sge.persistence.FirmasDocumentoProcedimientoManager;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.flexia.interfaces.user.web.documentospresentados.form.DocumentoExpedienteForm;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.commons.MimeTypes;
import es.altia.util.struts.StrutsFileValidation;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import sun.misc.BASE64Decoder;

/**
 * Action que recibe las peticiones de un usuario para dar de adjuntar un fichero a un documento de expediente o para eliminarlo
 * @author oscar.rodriguez
 */
public class FichaExpedienteDocumentosPresentadosAction extends ActionSession {

    private final String MAPPING_ACTUALIZAR_LISTA_DOCUMENTOS_ELIMINADOS = "actualizarListaDocumentosEliminados";
    private final String MAPPING_ACTUALIZAR_LISTA_DOCUMENTOS_IE = "actualizarListaDocumentosIE";
    private final String MAPPING_ACTUALIZAR_LISTA_DOCUMENTOS_FF = "actualizarListaDocumentosFF";
    private final String MAPPING_MOSTRAR_ALTA = "mostrarAlta";
    private final String MAPPING_FAIL = "fallo";
    private Logger log = Logger.getLogger(FichaExpedienteDocumentosPresentadosAction.class);

    public ActionForward performSession(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            m_Log.debug("perform");

            Date fecha = new Date();

            m_Log.debug("================= FichaExpedienteDocumentosPresentadosAction ======================>");
            HttpSession session = request.getSession();

            // Validaremos los parametros del request especificados
            ActionErrors errors = new ActionErrors();
            String opcion = request.getParameter("opcion");

            if (m_Log.isInfoEnabled()) {
                m_Log.info("la opcion en el action es " + opcion);
            }

            if (form == null) {
                m_Log.debug("Rellenamos el form de fichero");
                form = new FichaExpedienteDocumentoForm();
                if ("request".equals(mapping.getScope())) {
                    request.setAttribute(mapping.getAttribute(), form);
                } else {
                    session.setAttribute(mapping.getAttribute(), form);
                }
            }

            FichaExpedienteForm fichaExpForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
            DocumentoExpedienteForm fichaForm = (DocumentoExpedienteForm) form;
            FormFile fichero = null;
            if (fichaForm != null && fichaForm.getFichero() != null) {
                if (!"".equals(fichaForm.getFichero().getFileName())) {
                    fichero = fichaForm.getFichero();
                }
            }

            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();

            if ("mostrarAlta".equals(opcion)) {
                fichaForm.setCodMunicipio(request.getParameter("codMunicipio"));
                fichaForm.setCodDocumento(request.getParameter("codigoDocumento"));
                fichaForm.setEjercicio(request.getParameter("ejercicio"));
                fichaForm.setCodProcedimiento(request.getParameter("codProcedimiento"));
                fichaForm.setNumeroExpediente(request.getParameter("numExpediente"));
                request.setAttribute("codUnidadOrganicaExp", request.getParameter("codUnidadOrganicaExp"));
                fichaForm.setTituloFichero("");
                fichaForm.setFechaDocumento(Fecha.obtenerString(fecha));
                //Aquí tengo qu consultar para saber si tengo que consultar firmas definibles o no
                boolean firmasConfigurables = FirmasDocumentoProcedimientoManager.getInstance().tieneFirmasConfigurables(fichaForm.getCodDocumento(), fichaForm.getCodMunicipio(), fichaForm.getCodProcedimiento(),
                        params);
                fichaForm.setFirmas(FirmasDocumentoProcedimientoManager.getInstance().getFirmasDocumento(fichaForm.getCodDocumento(), fichaForm.getCodMunicipio(), fichaForm.getCodProcedimiento(),
                        params));
                fichaForm.setFirmasConfigurables(firmasConfigurables);
                opcion = MAPPING_MOSTRAR_ALTA;

            } else if ("mostrarModificacion".equals(opcion)) {
                fichaForm.setCodMunicipio(request.getParameter("codMunicipio"));
                fichaForm.setCodDocumento(request.getParameter("codigoDocumento"));
                fichaForm.setEjercicio(request.getParameter("ejercicio"));
                fichaForm.setCodProcedimiento(request.getParameter("codProcedimiento"));
                fichaForm.setNumeroExpediente(request.getParameter("numExpediente"));
                request.setAttribute("codUnidadOrganicaExp", request.getParameter("codUnidadOrganicaExp"));
                fichaForm.setTituloFichero("");
                fichaForm.setFechaDocumento(Fecha.obtenerString(fecha));
                fichaForm.setModificar("SI");
                fichaForm.setNombreOriginalAdjunto(request.getParameter("nombreOriginalAdjunto"));
                 boolean firmasConfigurables = FirmasDocumentoProcedimientoManager.getInstance().tieneFirmasConfigurables(fichaForm.getCodDocumento(), fichaForm.getCodMunicipio(), fichaForm.getCodProcedimiento(),
                        params);
                fichaForm.setFirmas(FirmasDocumentoProcedimientoManager.getInstance().getFirmasDocumento(fichaForm.getCodDocumento(), fichaForm.getCodMunicipio(), fichaForm.getCodProcedimiento(),
                        params));
                fichaForm.setFirmasConfigurables(firmasConfigurables);
                opcion = MAPPING_MOSTRAR_ALTA; 
            } else if ("documentoAlta".equals(opcion)) {
                boolean isExtensionCorrect = false;
                boolean isSizeCorrect = false;

                // Contenido del fichero
                byte[] contenidoFichero = null;
                // ContentType del fichero
                String contentType = "";
                // Nombre del fichero
                String nombreFichero = "";
                // Extensión del fichero
                String extensionFichero = "";

                if (fichaForm.getDocEscaneado() != null && fichaForm.getDocEscaneado().length() >= 1) {
                    byte[] sByte;
                    String titulo = fichaForm.getTituloFichero();
                    // Si se ha escaneado el documento
                    // Se comprueba la longitud del documento escaneado
                    String escaner = fichaForm.getDocEscaneado();
                    BASE64Decoder decoder = new BASE64Decoder();
                    sByte = decoder.decodeBuffer(escaner);

                    String extension = "jpg";
                    File f = File.createTempFile(titulo, "." + extension);
                    FileOutputStream fos = new FileOutputStream(f);
                    // Se guarda el array de bytes en disco para
                    fos.write(sByte);
                    fos.flush();
                    fos.close();

                    isSizeCorrect = StrutsFileValidation.isSizeValid(f, StrutsFileValidation.APP_REGISTRO);
                    //isExtensionCorrect = StrutsFileValidation.isExtensionValid(extension);
                    isExtensionCorrect = StrutsFileValidation.isExtensionValid(f, StrutsFileValidation.APP_REGISTRO);
                    // Contenido del fichero 
                    contenidoFichero = sByte;
                    // La imagen que procede del éscaner se trata de una JPEG
                    contentType = "image/jpeg";
                    // Nombre del documento
                    nombreFichero = titulo;
                    // Extensión del fichero
                    extensionFichero = extension;
                    f.delete();
                } else {
                    // Si se ha sube el documento por POST
                    if (fichero != null && fichero.getFileSize() > 0) {
                        // Si se ha sube el documento por POST
                        // Comprobación del tamaño del fichero y de extensión correcta
                        isExtensionCorrect = StrutsFileValidation.isExtensionValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);
                        isSizeCorrect = StrutsFileValidation.isSizeValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);

                        // Se guarda el contenido binario del fichero en la variable apropiada para pasar el contenido al plugin
                        contenidoFichero = fichero.getFileData();
                        contentType = fichero.getContentType();

                        String fileName = fichero.getFileName();
                        //String[] datosFichero = fileName.split("[.]");
                        //extensionFichero = datosFichero[1];
                        extensionFichero = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());                        
                        nombreFichero    = fichaForm.getTituloFichero();

                    }//if
                }//else

                // Comprobacion de la extension del fichero
                if (!isExtensionCorrect) {
                    m_Log.debug("La extensión del fichero no es válida");
                    request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "si");
                    String ext = (String) StrutsFileValidation.getLimite(StrutsFileValidation.EXTENSION_DOC_PREFFIX, StrutsFileValidation.SUFFIX_EXPEDIENTE);
                    request.setAttribute(ConstantesDatos.EXTENSION_PERMITED, ext);
                    request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                    opcion = MAPPING_FAIL; // Volvemos al formulario de entrada
                } else {
                    request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "no");
                }

                // Comprobacion del tamaño del fichero
                if (!isSizeCorrect) {
                    request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "si");
                    Integer tam = (Integer) StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX, StrutsFileValidation.SUFFIX_EXPEDIENTE);
                    tam = Math.round(tam/ConstantesDatos.DIVISOR_BYTES);

                    request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE, tam);
                    request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                    opcion = MAPPING_FAIL; // Volvemos al formulario de entrada
                } else {
                    request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "no");
                }

                Vector documentos = null;
                if (isExtensionCorrect && isSizeCorrect) {
                    // Se obtiene la implementación del plugin correspondiente                                        
                    String navegador = request.getParameter("navegador");
                    String codMunicipio = fichaForm.getCodMunicipio();
                    String ejercicio = fichaForm.getEjercicio();
                    String numeroExpediente = fichaForm.getNumeroExpediente();
                    String codDocumento = fichaForm.getCodDocumento();
                    String codProcedimiento = fichaForm.getCodProcedimiento();
                    // Indica si se modifica el contenido del adjunto asociado al documento de expediente
                    String modificar = fichaForm.getModificar();
                    String nombreDocumento = fichaForm.getTituloFichero();
                    String nombreOriginalAdjunto = fichaForm.getNombreOriginalAdjunto();
                    AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
                    
                    Hashtable<String, Object> datos = new Hashtable<String, Object>();
                    datos.put("codDocumento", codDocumento);
                    datos.put("codMunicipio", codMunicipio);
                    datos.put("ejercicio", ejercicio);
                    datos.put("numeroExpediente", numeroExpediente);
                    datos.put("params", params);
                    datos.put("fichero", contenidoFichero);
                    datos.put("tipoMime", contentType);
                    datos.put("nombreDocumento", nombreFichero);
                    datos.put("extension", extensionFichero);
                    datos.put("codProcedimiento", codProcedimiento);
                    datos.put("codUsuario", Integer.toString(usuario.getIdUsuario()));
                    if (modificar != null && modificar.equalsIgnoreCase("SI")) {
                        datos.put("modificarAdjuntoDocExpediente", "SI");
                    } else {
                        datos.put("modificarAdjuntoDocExpediente", "NO");
                    }
                    // SE COMPRUEBA SI SÓLO SE MARCA EL DOCUMENTO COMO PRESENTADO O SI TAMBIÉN SE DA DE ALTA UN ADJUNTO ASOCIADO AL MISMO
                    if ("documentoAlta".equals(opcion)) {
                        // Se marca el documento como presentado y se da alta el adjunto asociado al documento
                        datos.put("soloPresentar", "NO");
                    } else if ("marcarDocumentoPresentado".equals(opcion)) {
                        // Sólo se marca el documento como presentado y no se da alta ningún adjunto
                        datos.put("soloPresentar", "SI");
                    }

                    int tipoDocumento = -1;
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                    if (!almacen.isPluginGestor()) {
                        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                        datos.put("origen", ConstantesDatos.ORIGEN_ADJUNTO_DOC_PRESENTADO_BBDD);
                    } else {
                        ResourceBundle bundle = ResourceBundle.getBundle("documentos");                    
                        String carpetaRaiz = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                        datos.put("nombreOrganizacion", usuario.getOrg());
                        datos.put("codProcedimiento", codProcedimiento);
                        datos.put("nombreProcedimiento", nombreProcedimiento);
                        datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
                        datos.put("origen", almacen.getNombreServicio());
                        // TODO: HAY QUE RECUPERAR EL NOMBRE ANTIGUO DEL FICHERO

                        // NUEVO NOMBRE DEL DOCUMENTO
                        datos.put("nombreFicheroCompleto", codDocumento + ConstantesDatos.GUION + nombreDocumento + ConstantesDatos.DOT + extensionFichero);
                        // NOMBRE ORIGINAL DEL DOCUMENTO EN EL CASO DE LO QUE SE REALIZE SEA UNA MODIFICACIÓN
                        if (modificar != null && modificar.equalsIgnoreCase("SI") && nombreOriginalAdjunto != null && nombreOriginalAdjunto.length() > 0) {

                            // Se recuperan la extensión y nombre original del adjunto asociado al documento de expediente, puesto que se ha modificado.
                            Documento aux = new DocumentoBBDD();
                            aux.setCodMunicipio(Integer.parseInt(codMunicipio));
                            aux.setEjercicio(Integer.parseInt(ejercicio));
                            aux.setNumeroExpediente(numeroExpediente);
                            aux.setCodProcedimiento(codProcedimiento);
                            aux.setCodDocumento(Integer.parseInt(codDocumento));
                            aux.setParams(params);

                            aux = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(aux);
                            log.debug(" ********** aux.getNombreFichero original: " + aux.getNombreDocumento());
                            log.debug(" ********** aux.getNombreFichero original: " + aux.getExtension());
                            datos.put("nuevoNombreFicheroCompleto", codDocumento + ConstantesDatos.GUION + nombreDocumento + ConstantesDatos.DOT + extensionFichero);
                            datos.put("nombreFicheroCompleto", codDocumento + ConstantesDatos.GUION + aux.getNombreDocumento() + ConstantesDatos.DOT + aux.getExtension());
                        }

                        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                        traductor.setApl_cod(4);
                        traductor.setIdi_cod(usuario.getIdioma());

                        /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                        ArrayList<String> listaCarpetas = new ArrayList<String>();
                        listaCarpetas.add(carpetaRaiz);
                        listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                        listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                        listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
                        listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
                        datos.put("listaCarpetas", listaCarpetas);

                        // Nombre completo del fichero
                        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                    }

                    Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                    try {
                        almacen.setDocumentoPresentado(doc);
                        
                        OperacionesExpedienteManager.getInstance().registrarAltaDocumentoExpediente(doc, 
                                usuario.getNombreUsu(), false, params);
                        
                        ArrayList<FirmasDocumentoProcedimientoVO> firmas =
                                FirmasDocumentoProcedimientoManager.getInstance().getFirmasDocumento(
                                fichaForm.getCodDocumento(), fichaForm.getCodMunicipio(), fichaForm.getCodProcedimiento(), params);

                        GeneralValueObject gVO = new GeneralValueObject();
                        gVO.setAtributo("codMunicipio", codMunicipio);
                        gVO.setAtributo("codProcedimiento", codProcedimiento);
                        gVO.setAtributo("ejercicio", ejercicio);
                        gVO.setAtributo("numero", numeroExpediente);
                        documentos = FichaExpedienteManager.getInstance().cargaListaDocumentosExpediente(gVO, params);
                        int codDocPresentado = buscaCodigoDocPresentado(fichaForm.getCodDocumento(), documentos);
                        int codUnidadOrganicaExp = Integer.parseInt((String) request.getParameter("codUnidadOrganicaExp"));
                        
                        if (!firmas.isEmpty()) {

                            boolean firmasConfigurables = FirmasDocumentoProcedimientoManager.getInstance().
                                    tieneFirmasConfigurables(fichaForm.getCodDocumento(), fichaForm.getCodMunicipio(),
                                    fichaForm.getCodProcedimiento(), params);

                            /*Si tiene firmas configurables se recogen de la JSP, en caso contrario se consulta 
                            la definición del documento en BBDD y se arranca el circuito directamente.*/

                            if (firmasConfigurables) {
                                log.debug("lista firmas-->" + request.getParameter("listaFirmas"));
                                String listaFirmas = request.getParameter("listaFirmas");
                                firmas = crearListadoFirmasGuardar(listaFirmas);
                                for (int t = 0; t < firmas.size(); t++) {                                    
                                    log.debug("firmas: " + firmas.get(t).toString());
                                }
                            }

                            FichaExpedienteManager.getInstance().iniciarCircuitoFirmas(firmas, codDocPresentado, codUnidadOrganicaExp, params);

                            Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
                            if ("si".equals(m_ConfigTechnical.getString("mail.portafirmas.activo"))) {
                                FirmasDocumentoProcedimientoVO firma = firmas.get(0);
                                Vector<UsuariosGruposManager> usuarios = UsuariosGruposManager.getInstance().getUsuariosFirmantesUnidadCargo(firma.getMunicipio(), firma.getUor(), firma.getCargo(), params);
                                String nombreUor = firma.getNomUor();
                                String nombreCargo = firma.getNomCargo();

                                String subject = m_ConfigTechnical.getString("mail.send.siguienteFirmante.subject");
                                subject = subject.replaceAll("@documento@", nombreDocumento);


                                String content = "";
                                String cargoTodos = CargosDAO.getInstance().getCargoPorCodigoVisible("TD", params).getUor_cod();
                                if (firma.getUsuario() != null && !"".equals(firma.getUsuario())) {
                                    content = m_ConfigTechnical.getString("mail.send.siguienteFirmante.user.content");
                                } else if (firma.getCargo() != null && !"".equals(firma.getCargo()) && !cargoTodos.equals(firma.getCargo())) {
                                    content = m_ConfigTechnical.getString("mail.send.siguienteFirmante.cargo.content");
                                } else if (firma.getUor() != null && !"".equals(firma.getUor())) {
                                    content = m_ConfigTechnical.getString("mail.send.siguienteFirmante.uor.content");
                                }
                                content = content.replaceAll("@uor@", nombreUor);
                                content = content.replaceAll("@cargo@", nombreCargo);
                                content = content.replaceAll("@procedimiento@", nombreProcedimiento);
                                content = content.replaceAll("@expediente@", numeroExpediente);
                                content = content.replaceAll("@documento@", nombreDocumento);

                                MailHelper mailHelper = new MailHelper();
                                for (Iterator it = usuarios.iterator(); it.hasNext();) {
                                    String email = ((UsuariosGruposValueObject) it.next()).getEmail();
                                    if (email == null || "".equals(email)) {
                                        continue;
                                    }
                                    mailHelper.sendMail(email, subject, content);
                                }
                            }
                        }


                        // Se recupera la nueva lista de documentos de expediente para actualizar la vista
                        documentos = FichaExpedienteManager.getInstance().cargaListaDocumentosExpediente(gVO, params);
                        request.setAttribute("documentosExpediente", documentos);

                        if (navegador == null || (navegador != null && navegador.equals("IE"))) // Internet Explorer. Se redirige al oculto
                        {
                            opcion = MAPPING_ACTUALIZAR_LISTA_DOCUMENTOS_IE;
                        } else if (navegador != null && navegador.equals("FF")) {
                            // Firefox. Se redirige a altaDocumentoExpediente.jsp para que sea esta en firefox la que recarga la lista de documentos presentados
                            request.setAttribute("recargarListaDocumentosPresentados", "SI");
                            opcion = MAPPING_ACTUALIZAR_LISTA_DOCUMENTOS_FF;
                        }
                    } catch (AlmacenDocumentoTramitacionException e) {
                        e.printStackTrace();
                    }
                }// if todo correcto               
            } else if ("marcarDocumentoPresentado".equals(opcion)) {
                String codMunicipio = request.getParameter("codMunicipio");
                String ejercicio = request.getParameter("ejercicio");
                String numeroExpediente = request.getParameter("numExpediente");
                String codDocumento = request.getParameter("codigoDocumento");
                String codProcedimiento = request.getParameter("codProcedimiento");

                Hashtable<String, Object> datos = new Hashtable<String, Object>();
                datos.put("codDocumento", codDocumento);
                datos.put("codMunicipio", codMunicipio);
                datos.put("ejercicio", ejercicio);
                datos.put("numeroExpediente", numeroExpediente);
                datos.put("params", params);
                datos.put("codProcedimiento", codProcedimiento);

                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD);
                // Se marca el documento como presentado
                ExpedienteDocPresentadoManager.getInstance().comprobarDocumentoPresentado(doc);
                // Se recuperan los documentos de expediente para actualizar la vista
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codMunicipio", codMunicipio);
                gVO.setAtributo("codProcedimiento", codProcedimiento);
                gVO.setAtributo("ejercicio", ejercicio);
                gVO.setAtributo("numero", numeroExpediente);
                Vector documentos = FichaExpedienteManager.getInstance().cargaListaDocumentosExpediente(gVO, params);
                request.setAttribute("documentosExpediente", documentos);
                //opcion =  MAPPING_ACTUALIZAR_LISTA_DOCUMENTOS;
                opcion = "actualizarListaDocumentosIE";
            } else if ("documentoEliminar".equals(opcion) || "desmarcarDocumentoEliminar".equals(opcion)) {

                String codMunicipio = request.getParameter("codMunicipio");
                String ejercicio = request.getParameter("ejercicio");
                String numeroExpediente = request.getParameter("numExpediente");
                String codDocumento = request.getParameter("codigoDocumento");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String nombreAdjunto = request.getParameter("nombreAdjunto");
                String tipoMime = request.getParameter("tipoMime");
                String fechaAdjunto = request.getParameter("fechaAdjunto");

                Hashtable<String, Object> datos = new Hashtable<String, Object>();
                datos.put("codDocumento", codDocumento);
                datos.put("codMunicipio", codMunicipio);
                datos.put("ejercicio", ejercicio);
                datos.put("numeroExpediente", numeroExpediente);
                datos.put("params", params);
                datos.put("codProcedimiento", codProcedimiento);
                if ("documentoEliminar".equals(opcion)) {
                    // Se indica que sólo se elimina el adjunto asociado a un documento de expediente, pero no se desmarca el documento como presentado
                    datos.put("eliminarSoloAdjunto", "SI");
                } else if ("desmarcarDocumentoEliminar".equals(opcion)) {
                    // Se indica que se desmarca el documento de expediente como presentado. Esto hace que automáticamente se elimine cualquier adjunto que tuviese asociado.
                    datos.put("eliminarSoloAdjunto", "NO");
                }

                // Se obtiene la implementación del plugin correspondiente                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
                
                int tipoDocumento = -1;
                if (!almacen.isPluginGestor()) {
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                } else {
                    ResourceBundle bundle = ResourceBundle.getBundle("documentos");                   
                    String carpetaRaiz = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                    
                    
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                    datos.put("nombreOrganizacion", usuario.getOrg());
                    datos.put("codProcedimiento", codProcedimiento);
                    datos.put("nombreProcedimiento", nombreProcedimiento);
                    datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
                    datos.put("origen", almacen.getNombreServicio());

                    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                    traductor.setApl_cod(4);
                    traductor.setIdi_cod(usuario.getIdioma());

                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
                    listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
                    datos.put("listaCarpetas", listaCarpetas);

                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }

                // Se necesitan para el registro de la operacion realizada
                if(!"".equals(nombreAdjunto)){
                datos.put("nombreDocumento", nombreAdjunto);
                datos.put("tipoMime", tipoMime);
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
                datos.put("fechaDoc", fechaAdjunto);
                datos.put("origen",almacen.getNombreServicio());
                }
                
                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);

                if (almacen.isPluginGestor()) {
                    // Se recupera la información del documento de la base de datos para extraer la extensión del fichero y componer el nombre del fichero
                    // para poder obtener su contenido del gestor
                    doc = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(doc);
                    log.debug(" ********* nombredocumento: " + doc.getNombreDocumento());
                    log.debug(" ********* extensiondocumento: " + doc.getExtension());
                    DocumentoGestor docGestor = (DocumentoGestor) doc;
                    docGestor.setNombreFicheroCompleto(codDocumento + ConstantesDatos.GUION + doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension());
                    doc = docGestor;
                }

                //Si tiene adjunto hay que eliminarlo del almacen (tanto eliminacion como desmarcado con adjunto existente conllevan una eliminación en el almacen)
                if(!"".equals(nombreAdjunto)){
                    boolean eliminado = almacen.eliminarDocumentoPresentado(doc);
                }else //Si solo es desmarcado (ya no tiene el adjunto en el almacen) se desmarca en BBDD
                {
                    ExpedienteDocPresentadoManager.getInstance().eliminarDocumentoPresentado(doc);
                }
                
                OperacionesExpedienteManager.getInstance().registrarEliminacionDocumentoExpediente(doc,usuario.getNombreUsu(), false, params);
                
                // Se recupera la nueva lista de documentos de expediente para actualizar la vista
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codMunicipio", codMunicipio);
                gVO.setAtributo("codProcedimiento", codProcedimiento);
                gVO.setAtributo("ejercicio", ejercicio);
                gVO.setAtributo("numero", numeroExpediente);

                Vector documentos = FichaExpedienteManager.getInstance().cargaListaDocumentosExpediente(gVO, params);
                request.setAttribute("documentosExpediente", documentos);
                opcion = MAPPING_ACTUALIZAR_LISTA_DOCUMENTOS_ELIMINADOS;

            } else if ("verFirmas".equals(opcion)) {
                int codDocumentoAdjunto = Integer.parseInt((String) request.getParameter("codDocumentoAdjunto"));
                ArrayList<FirmasDocumentoExpedienteVO> listaFirmas = FichaExpedienteManager.getInstance().getFirmasDocumento(codDocumentoAdjunto, params);
                request.setAttribute("listaFirmas", listaFirmas);
            }else if ("verificarFirma".equals(opcion)) {
                if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpedienteAction(): Opcion verificarFirma");}

                String codDocumento =  (String)request.getParameter("codDocumentoAdjunto");
                String idDocumento =  (String)request.getParameter("idDocumento");
                if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpedienteAction(): Codigo de documento" + codDocumento);}

              
                FirmaVO firma = verificarFirma(fichaExpForm,usuario, Integer.valueOf(codDocumento), Integer.valueOf(idDocumento));
                fichaExpForm.setFirmaValida(firma.getValido());
                
                fichaExpForm.setDatosCertificado(firma.getAsuntoCertificado());
                fichaExpForm.setEmisorCertificado(firma.getEmisorCertificado());
                fichaExpForm.setNombreFirmante(firma.getNombrePersona());
                fichaExpForm.setNifFirmante(firma.getNif());
                fichaExpForm.setValidezCertificado(firma.getValidez());
                fichaExpForm.setFirmaValida(firma.getValido());

            }
            
            else {
                opcion = mapping.getInput();
            }

            // Cada vez que se excede el tamaño máximo del fichero a subir por POST el parámetro opcion
            // llega vacío
            if (opcion == null) {
                // Se indica que hay error y se vuelve a la página que
                // permite dar de alta un nuevo documento
                //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);
                int num = (Integer) StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX, StrutsFileValidation.SUFFIX_EXPEDIENTE);
                request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE, Math.round(num / ConstantesDatos.DIVISOR_BYTES));
                request.setAttribute("ERROR_FILESIZE_UPLOAD", "si");
                opcion = MAPPING_FAIL;
            }

            m_Log.debug("<================= FichaExpedienteDocumentosPresentadosAction ======================");
            return (mapping.findForward(opcion));

        } catch (Exception e) {
            // Si ocurre algún error
            e.printStackTrace();
            m_Log.error("Se ha producido el siguiente error: " + e.getMessage());
            return null;
        }
    }

    private int buscaCodigoDocPresentado(String codigo, Vector documentos) {
        int resultado = -1;
        for (Iterator it = documentos.iterator(); it.hasNext();) {
            GeneralValueObject gVO = (GeneralValueObject) it.next();
            String codDoc = (String) gVO.getAtributo("codigo");
            String codDocumentoAdjuntoExpediente = (String) gVO.getAtributo("codDocumentoAdjuntoExpediente");            
            
            if (codigo.equals(codDoc) && codDocumentoAdjuntoExpediente!=null && !"".equals(codDocumentoAdjuntoExpediente)){                
                resultado = Integer.parseInt(codDocumentoAdjuntoExpediente);
                break;
            }
        }
        return resultado;
    }

    private ArrayList<FirmasDocumentoProcedimientoVO> crearListadoFirmasGuardar(String listaFirmasStr) {
        ArrayList<FirmasDocumentoProcedimientoVO> resultado = new ArrayList<FirmasDocumentoProcedimientoVO>();
        if (listaFirmasStr==null || "".equals(listaFirmasStr)) return resultado;       
        //String[] firmas = listaFirmasStr.split("¬¬");
        String[] firmas = listaFirmasStr.split("§¥");
        
        
        //for (int i=0;i<firmas.length;i=i+5) {
        for (int i=0;i<firmas.length;i++) {
            
            String[] datosFirma = firmas[i].split("¬¬");
            if(datosFirma!=null && datosFirma.length==7){
                FirmasDocumentoProcedimientoVO auxVO = new FirmasDocumentoProcedimientoVO();
                //auxVO.setUor(firmas[i]);
                if (datosFirma[0].equals("") || datosFirma[0].equalsIgnoreCase("null"))
                     auxVO.setUor(null);
                else auxVO.setUor(datosFirma[0]);           
               
                //auxVO.setCargo(firmas[i+1]);
                if (datosFirma[1].equals("") || datosFirma[1].equalsIgnoreCase("null"))
                     auxVO.setCargo(null);
                else auxVO.setCargo(datosFirma[1]); 
                 //auxVO.setUsuario(firmas[i+2]);
                 if (datosFirma[2].equals("") || datosFirma[2].equalsIgnoreCase("null"))
                     auxVO.setUsuario(null);
                else auxVO.setUsuario(datosFirma[2]); 
            
                //auxVO.setOrden(firmas[i+3]);
                auxVO.setOrden(datosFirma[3]);
                            // revisar el orden en el que llegan			
                //auxVO.setFinalizaRechazo(firmas[i+5]);
                auxVO.setFinalizaRechazo(datosFirma[5]);
                //auxVO.setTramitar(firmas[i+6]);
                auxVO.setTramitar(datosFirma[6]);
                resultado.add(auxVO);
            }
        }
        return resultado;
    }
    
    
    
     
     public FirmaVO verificarFirma(FichaExpedienteForm formulario, UsuarioValueObject usuario, Integer codDocumento, Integer idDocumento) throws Exception {

        byte[] documento = null;
        int codOrganizacion = usuario.getOrgCod();
        String nombreOrganizacion = usuario.getOrg();
        String[] params = usuario.getParamsCon();
        String firma = new String(FichaExpedienteManager.getInstance().cargarFirmaDocumento(codDocumento,params));
        m_Log.debug("**************** verificarFirma codOrg: " + codOrganizacion + " <> nombreOrg: " + nombreOrganizacion);
        m_Log.debug("**************** La firma del fichero es:  " + firma);


        m_Log.debug("====================> " + this.getClass().getName() + ".verificaFirma codOrganizacion: " + codOrganizacion);
        m_Log.debug("====================> " + this.getClass().getName() + ".verificaFirma nombreOrganizacion: " + nombreOrganizacion);
        m_Log.debug("====================> " + this.getClass().getName() + ".verificaFirma firma: " + firma);
        
        FirmaVO infoFirma = new FirmaVO();
        
        try {
            documento = this.getContenidoDocumento(usuario, formulario.getCodMunicipio(), formulario.getEjercicio(), formulario.getNumExpediente(), idDocumento.toString(),formulario.getCodProcedimiento());
            File f = File.createTempFile("prueba", "temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(documento);
            fos.flush();
            fos.close();

            DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
            docFirmado.setFicheroFirma(f);
            docFirmado.setFirma(firma);
            docFirmado.setFicheroHash64(new String(documento));

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

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return infoFirma;
    }
    
     
     public byte[] getContenidoDocumento(UsuarioValueObject usuario, String codMunicipio, String ejercicio, String numExpediente, String codDocumento, String codProcedimiento){
         if (m_Log.isDebugEnabled()){m_Log.debug("getContenidoDocumento(): INICIO");}

      

         String[] params = usuario.getParamsCon();

        Hashtable<String,Object> datos = new Hashtable<String,Object>();
        if (m_Log.isDebugEnabled()){m_Log.debug("getContenidoDocumento(): Cod Documeto" + codDocumento);}
         datos.put("codDocumento",codDocumento);
         if (m_Log.isDebugEnabled()){m_Log.debug("getContenidoDocumento(): Municipio " + codMunicipio);}
         datos.put("codMunicipio",codMunicipio);
         if (m_Log.isDebugEnabled()){m_Log.debug("getContenidoDocumento(): Ejecrcicio" + ejercicio);}
         datos.put("ejercicio",ejercicio);
         if (m_Log.isDebugEnabled()){m_Log.debug("getContenidoDocumento(): Num Exp " + numExpediente);}
         datos.put("numeroExpediente",numExpediente);
         datos.put("params",params);
         if (m_Log.isDebugEnabled()){m_Log.debug("getContenidoDocumento(): Procedimiento " + codProcedimiento);}
         datos.put("codProcedimiento",codProcedimiento);

        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
        Documento doc = null;
        int tipoDocumento = -1;
        if(!almacen.isPluginGestor())
           tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        else{
            ResourceBundle bundle = ResourceBundle.getBundle("documentos");            
            String carpetaRaiz = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);            
            
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);        
            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
            datos.put("origen",almacen.getNombreServicio());

            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setApl_cod(4);
            traductor.setIdi_cod(usuario.getIdioma());

            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
            listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
            listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
            datos.put("listaCarpetas",listaCarpetas);
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;

     }

     byte[] fichero = null;
     String nombreFichero = null;
     String tipoContenido  = null;

      try{

            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            if(almacen.isPluginGestor()){

                // Se recupera la información del documento de la base de datos para extraer la extensión del fichero y componer el nombre del fichero
                // para poder obtener su contenido del gestor
                doc = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(doc);
                if (m_Log.isDebugEnabled()){m_Log.debug(" ********* nombredocumento: " + doc.getNombreDocumento());}
                if (m_Log.isDebugEnabled()){m_Log.debug(" ********* extensiondocumento: " + doc.getExtension());}
                DocumentoGestor docGestor = (DocumentoGestor)doc;
                docGestor.setNombreFicheroCompleto(codDocumento + ConstantesDatos.GUION + doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension());
                doc = almacen.getDocumentoPresentado(docGestor);
            }else
                doc = almacen.getDocumentoPresentado(doc);

            fichero             = doc.getFichero();
            nombreFichero = doc.getNombreDocumento() + "." + doc.getExtension();
            tipoContenido  = doc.getTipoMimeContenido();

            if (m_Log.isDebugEnabled()){m_Log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero longitud: " + fichero.length);}
            if (m_Log.isDebugEnabled()){m_Log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero nombreFichero: " + nombreFichero);}
            if (m_Log.isDebugEnabled()){m_Log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero tipoContenido: " + tipoContenido);}

      }catch(AlmacenDocumentoTramitacionException e){
          e.printStackTrace();
          fichero             = null;
          nombreFichero = null;
          tipoContenido   = null;
      }

      if(fichero == null)
      {
             log.warn("FICHERO NULO EN VerDocumentoPresentadoServlet.defaultAction");
            
      }   
      return fichero;
      
     }
   
}
