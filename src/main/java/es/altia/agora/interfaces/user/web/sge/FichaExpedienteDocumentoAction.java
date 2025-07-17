package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import com.google.gson.Gson;
import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.ExpedienteOtroDocumentoManager;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.interfaces.user.web.util.ResultadoAjax;
import es.altia.agora.technical.ConstantesAjax;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.struts.StrutsFileValidation;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import java.util.*;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.DocumentConversionException;
import es.altia.common.exception.FormatNotSupportedException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.ResultadoTratamientoCargaParcialExpedienteVO;
import es.altia.util.commons.JODConverterHelper;
import es.altia.util.commons.MimeTypes;
import es.altia.util.commons.WebOperations;
import java.net.MalformedURLException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author manuel.bahamonde
 */
public class FichaExpedienteDocumentoAction extends ActionSession {

    protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");
    private ExpedienteOtroDocumentoManager expedienteOtroDocumentoManager = null;
    private DocumentoManager documentoManager = null;
    
    public ActionForward performSession(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            m_Log.debug("perform");

            Date fecha = new Date();

            m_Log.debug("================= FichaExpedienteDocumentoAction ======================>");
            ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
            HttpSession session = request.getSession();
            expedienteOtroDocumentoManager = ExpedienteOtroDocumentoManager.getInstance();
            documentoManager = DocumentoManager.getInstance();

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
            
            FichaExpedienteDocumentoForm fichaForm = (FichaExpedienteDocumentoForm) form;
            FormFile fichero = null;
            if (fichaForm != null && fichaForm.getFichero() != null) {
                if (!"".equals(fichaForm.getFichero().getFileName())) {
                    fichero = fichaForm.getFichero();
                }
            }

            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            m_Log.debug("FichaExpedienteDocumentoAction - FichaExpedienteDocumentoForm es: " + fichaForm);

            if ("documentoNuevo".equals(opcion)) {
                fichaForm.setTituloFichero("");
                fichaForm.setFechaDocumento(Fecha.obtenerString(fecha));
                opcion = "documentoNuevo";

            } else if ("documentoAlta".equals(opcion)) {
                boolean isExtensionCorrect = false;
                boolean isSizeCorrect = false;
                boolean datosModificados = fichero != null;
                String navegador = request.getParameter("navegador");
                m_Log.debug(" =========================> Alta de un documento otro navegador web utlizado: " + navegador);

                // Si se ha sube el documento por POST
                if (fichero != null && fichero.getFileSize() > 0) {
                    // Si se ha sube el documento por POST
                    // Comprobación del tamaño del fichero y de extensión correcta
                    isExtensionCorrect = StrutsFileValidation.isExtensionValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);
                    isSizeCorrect = StrutsFileValidation.isSizeValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);


                    // Comprobacion de la extension del fichero
                    if (!isExtensionCorrect) {
                        m_Log.debug("La extensión del fichero no es válida");
                        request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "si");
                        String ext = (String) StrutsFileValidation.getLimite(StrutsFileValidation.EXTENSION_DOC_PREFFIX, StrutsFileValidation.SUFFIX_EXPEDIENTE);
                        request.setAttribute(ConstantesDatos.EXTENSION_PERMITED, ext);
                        request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                        opcion = "documentoNuevo"; // Volvemos al formulario de entrada
                    } else {
                        request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "no");
                    }

                    // Comprobacion del tamaño del fichero
                    if (!isSizeCorrect) {
                        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "si");
                        Integer tam = Math.round((Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES)/ConstantesDatos.DIVISOR_BYTES);
                        //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);
                        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE, tam);
                        request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                        opcion = "documentoNuevo"; // Volvemos al formulario de entrada
                    } else {
                        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "no");
                    }


                    if(isExtensionCorrect && isSizeCorrect){
                        
                        String codMunicipio = (String)fichaExpForm.getExpedienteVO().getAtributo("codMunicipio");
                        String ejercicio = (String)fichaExpForm.getExpedienteVO().getAtributo("ejercicio");
                        String numExpediente = (String)fichaExpForm.getExpedienteVO().getAtributo("numero");
                        
                        String[] datosExpediente = numExpediente.split("/");
                        String codProcedimiento = datosExpediente[1];
                        // Se obtiene la implementación del plugin correspondiente                        
                        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
                        
                        Hashtable<String,Object> datos = new Hashtable<String,Object>();
                        datos.put("codMunicipio",codMunicipio);
                        datos.put("ejercicio",ejercicio);
                        datos.put("numeroExpediente",numExpediente);
                        datos.put("nombreDocumento",fichaForm.getTituloFichero());
                        datos.put("perteneceRelacion","false");
                        datos.put("params",params);
                        datos.put("fichero",fichero.getFileData());
                        datos.put("tipoMime",fichero.getContentType());
                        datos.put("codUsuario",Integer.toString(usuario.getIdUsuario()));
                        String fileName = fichero.getFileName();
                        String[] datosFichero = fileName.split("[.]");
                        datos.put("extension",datosFichero[datosFichero.length-1]); //Cogemos como extension el texto despues del ultimo punto (el nombre podria ser que tuviera puntos)
                        datos.put("origen", almacen.getNombreServicio());
                        
                        int tipoDocumento = -1;
                        if(!almacen.isPluginGestor())
                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                        else{
                            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);                            
                            datos.put("codProcedimiento",codProcedimiento);                            
                            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                            ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
                            //String tipoPlugin = bundleDocumentos.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);
                            //String tipoPlugin      = almacen.getTipoPlugin();
                            //String nombreGestor    = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + tipoPlugin + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR);
                            //String carpetaRaiz     = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                            String carpetaRaiz     = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                            String descripcionOrganizacion = usuario.getOrg();
                            ArrayList<String> listaCarpetas = new ArrayList<String>();
                            listaCarpetas.add(carpetaRaiz);
                            listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
                            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                            listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                            datos.put("listaCarpetas",listaCarpetas);
                            /** FIN **/
                           tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                        }


                        Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                        try{
                            almacen.setDocumentoExterno(doc);
                            
                            OperacionesExpedienteManager.getInstance().registrarAltaDocumentoExpediente(doc, usuario.getNombreUsu(), true, params);
                            
                            ArrayList<ExpedienteOtroDocumentoVO> docExternos = FichaExpedienteManager.getInstance().obtenerOtrosDocumentosExpediente(codMunicipio,ejercicio,numExpediente, params);
                             // Se convierte la respuesta a un String JSON                        
                            String jsonDocumentosExternos = new Gson().toJson(docExternos);
                            request.setAttribute("jsonDocumentosExternos",jsonDocumentosExternos);
                            opcion = "documentoAlta";
                            
                        }catch(AlmacenDocumentoTramitacionException e){
                            e.printStackTrace();
                        }
                    }// if todo correcto
                }

            } else if ("documentoEliminar".equals(opcion)) {

                String codInternoDocumento = request.getParameter("codInternoDoc");  // Código interno del documento externo a eliminar
                String codigo = request.getParameter("codigoDocumento"); // Posición del documento en la fila de documentos
                
                String extension = request.getParameter("extension"); // Extensión del documento a eliminar
                String nombreDocumento = request.getParameter("nombre"); // Nombre del documento a eliminar

                nombreDocumento = StringEscapeUtils.unescapeHtml(nombreDocumento);
                fichaForm.setCodigo(codigo);
                String codMunicipio      = (String)fichaExpForm.getExpedienteVO().getAtributo("codMunicipio");
                String ejercicio            = (String)fichaExpForm.getExpedienteVO().getAtributo("ejercicio");
                String numExpediente  = (String)fichaExpForm.getExpedienteVO().getAtributo("numero");

                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codMunicipio);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numExpediente);                                                            
                datos.put("perteneceRelacion","false");
                datos.put("params",params);
                datos.put("numeroDocumento",codInternoDocumento);
                datos.put("extension",extension);
                datos.put("tipoMime",MimeTypes.guessMimeTypeFromExtension(extension));
                datos.put("nombreDocumento",nombreDocumento);
                String codProcedimiento = numExpediente.split("/")[1];                 
                
                // Se obtiene la implementación del plugin correspondiente                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
                datos.put("origen", almacen.getNombreServicio());
                
                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                else{                    
                    
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                    datos.put("nombreOrganizacion",usuario.getOrg());
                    datos.put("codProcedimiento",codProcedimiento);
                    datos.put("nombreProcedimiento",nombreProcedimiento);
                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                    ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
                    //String tipoPlugin = bundleDocumentos.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);
                    //String tipoPlugin   = almacen.getTipoPlugin();
                    //String nombreGestor = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + tipoPlugin + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR);
                    //String carpetaRaiz     = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                    String carpetaRaiz  = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                    
                    String descripcionOrganizacion = usuario.getOrg();
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                    datos.put("listaCarpetas",listaCarpetas);
                    

                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }

                ResultadoTratamientoCargaParcialExpedienteVO respuesta = new ResultadoTratamientoCargaParcialExpedienteVO();
                try{
                    Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                    boolean eliminado = almacen.eliminarDocumentoExterno(doc);
                    
                    OperacionesExpedienteManager.getInstance().registrarEliminacionDocumentoExpediente(doc, usuario.getNombreUsu(), true, params);
                    
                    respuesta.setStatus(0);
                    respuesta.setDescStatus("OK");                    
                    if(!eliminado){
                        respuesta.setStatus(1);
                        respuesta.setDescStatus("ERROR AL ELIMINAR EL DOCUMENTO EXTERNO ");                    
                    }
                }catch(Exception e){
                    m_Log.error("Error al eliminar el documento externo: " + e.getMessage());
                    e.printStackTrace();
                    respuesta.setStatus(1);
                    respuesta.setDescStatus("ERROR AL ELIMINAR EL DOCUMENTO EXTERNO ");                                        
                }finally{
                    // Se convierte la respuesta a un String JSON                        
                    retornarJSON(new Gson().toJson(respuesta),response);
                }
                
                return null;
            } else if ("documentoCrearCSV".equals(opcion)) {
                ByteArrayInputStream inputStream = null;
                ByteArrayOutputStream outputStream = null;
                ExpedienteOtroDocumentoVO expediente = null;
                ResultadoAjax<ExpedienteOtroDocumentoVO> respuesta = new ResultadoAjax<ExpedienteOtroDocumentoVO>();
                respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
                respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_OK);
                
                try {
                    String codInternoDocumento = request.getParameter("codInternoDoc");  // Codigo interno del documento externo
                    
                    // Se comprueba si ya existe el metadato CSV
                    if (!expedienteOtroDocumentoManager.existeDocumentoMetadatoCSV(codInternoDocumento, params)) {
                        // Se obtiene el fichero del documento
                        //Se indica si al convertir el documento se desea que sea PDF o PDFA y ese valor se almacena en el fichero documentos.properties
                        ResourceBundle configDocuemntos = ResourceBundle.getBundle("documentos");
                        String propiedad = usuario.getOrgCod()+"/FICHA_EXPEDIENTE_DOCUMENTO/FORMATO_PDFA";
                        String pdfA = configDocuemntos.getString(propiedad);
                        byte[] ficheroDocExterno = obtieneFicheroPDFCSV(request, fichaExpForm, pdfA);

                        if (ficheroDocExterno != null && ficheroDocExterno.length > 0) {
                            outputStream = new ByteArrayOutputStream();
                            inputStream = new ByteArrayInputStream(ficheroDocExterno);

                            // Incrustar el CSV en el pdf
                            String cabeceraCSV = ""; // TODO implementar cabecera
                            String codigoCSV = "";
                            boolean generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(usuario.getOrgCod(), registroConf);
                            
                            if (generarCSV) {
                                codigoCSV = documentoManager.incrustarCSVenPDF(inputStream, outputStream, cabeceraCSV, usuario.getNombreUsu(), String.valueOf(usuario.getOrgCod()));
                            }
                            // Almacenar el documento e insertar los datos del CSV en las tablas correspondientes
                            expediente = almacenarDocumentoCSV(request, fichaExpForm, usuario, outputStream, codigoCSV);
                        } else {
                            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_NO_EXISTE_FICHERO);
                            respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_NO_EXISTE_FICHERO);
            } 
                    } else {
                        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_YA_EXISTE_CSV);
                        respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_YA_EXISTE_CSV);

                    }
                } catch (FormatNotSupportedException fnse) {
                    m_Log.error("Ha ocurrido un error al intentar generar el CSV", fnse);
                    respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_FORMATO_FICHERO_NO_SOPORTADO);
                    respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_FORMATO_FICHERO_NO_SOPORTADO);
                } catch (Exception e) {
                    m_Log.error("Ha ocurrido un error al intentar generar el CSV", e);
                    respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
                    respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_INTERNO);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                }

                respuesta.setResultado(expediente);
                retornarJSON(new Gson().toJson(respuesta), response);
                return null;
            } else {
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
                opcion = "documentoNuevo";
            }

            m_Log.debug("<================= FichaExpedienteDocumentoAction ======================");
            return (mapping.findForward(opcion));

        } catch (Exception e) {
            // Si ocurre algún error
            e.printStackTrace();
            m_Log.error("Se ha producido el siguiente error: " + e.getMessage());
            return null;
        }
    }
    
    private byte[] obtieneFicheroPDFCSV(HttpServletRequest request, FichaExpedienteForm fichaExpForm, String pdfA) throws TechnicalException, FormatNotSupportedException, Exception {
        byte[] documentoConvertido = null;
        
        // Descargar fichero
        byte[] ficheroDocExterno = descargarFicheroOtroDocumento(request, fichaExpForm);
        
        if (ficheroDocExterno != null && ficheroDocExterno.length > 0) {
            try {
                String tipoMimeOriginal = request.getParameter("tipoMime");
                String rutaTemporalCSV = getRutaTemporalFicherosCSV(request);
                
                if (!MimeTypes.PDF[0].equals(tipoMimeOriginal)) {
                    // Convertir fichero a PDF
                    documentoConvertido = JODConverterHelper.convertirDocumentoAPdf(rutaTemporalCSV, ficheroDocExterno, tipoMimeOriginal, pdfA);
                } else {
                    documentoConvertido = ficheroDocExterno;
                }
            } catch (DocumentConversionException dce) {
                throw new TechnicalException("Error al intentar convertir el fichero a pdf", dce);
            }
        } else {
            throw new TechnicalException("El fichero esta vacio");
        }
        
        return documentoConvertido;
    }
    
    private byte[] descargarFicheroOtroDocumento(HttpServletRequest request, FichaExpedienteForm fichaExpForm) throws TechnicalException {
        byte[] fichero = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            Config configCommon = ConfigServiceHelper.getConfig("common");
            Config configDoc = ConfigServiceHelper.getConfig("documentos");
            String url = String.format("%s%s%s", 
                    configCommon.getString("hostVirtual"),
                    request.getContextPath(),
                    configDoc.getString("CSV/URI/SERVLET_VER_DOCUMENTO"));
            String sessionId = request.getSession().getId();
            String codInternoDocumento = request.getParameter("codInternoDoc"); // Código interno del documento externo
            String extension = request.getParameter("extension");
            String tipoMime = request.getParameter("tipoMime");
            String nombreDocumento = StringEscapeUtils.unescapeHtml(request.getParameter("nombre")); // Nombre del documento a eliminar
            String codMunicipio = (String) fichaExpForm.getExpedienteVO().getAtributo("codMunicipio");
            String ejercicio = (String) fichaExpForm.getExpedienteVO().getAtributo("ejercicio");
            String numExpediente = (String) fichaExpForm.getExpedienteVO().getAtributo("numero");
            String expHistorico = (String) fichaExpForm.getExpedienteVO().getAtributo("expHistorico");
            
            Map<String, Object> paramsDescargarFichero = new LinkedHashMap<String, Object>();
            paramsDescargarFichero.put("codigo", codInternoDocumento);
            paramsDescargarFichero.put("ejercicio", ejercicio);
            paramsDescargarFichero.put("numero", numExpediente);
            paramsDescargarFichero.put("codMunicipio", codMunicipio);
            paramsDescargarFichero.put("extension", extension);
            paramsDescargarFichero.put("tipoMime", tipoMime);
            paramsDescargarFichero.put("nombreFich", nombreDocumento);
            paramsDescargarFichero.put("opcion", "0");
            paramsDescargarFichero.put("otroDocExp", "si");
            paramsDescargarFichero.put("expHistorico", expHistorico);

            // Descarga del fichero
            WebOperations.descargarUrlPost(url, sessionId, paramsDescargarFichero, out);
            
            fichero = out.toByteArray();
        } catch (MalformedURLException mue) {
            throw new TechnicalException("Error al intentar descargar el fichero del documento", mue);
        } catch (IOException ioe) {
            throw new TechnicalException("Error al intentar descargar el fichero del documento", ioe);
        } finally {
            IOUtils.closeQuietly(out);
        }
                
        return fichero;
    }

    private String getRutaTemporalFicherosCSV(HttpServletRequest request) throws TechnicalException {
        StringBuilder path = new StringBuilder();

        try {
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            String rutaBase = config.getString("RUTA_DISCO_DOCUMENTOS");
            String sessionId = request.getSession().getId();

            path.append(rutaBase)
                    .append(File.separator)
                    .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE)
                    .append(File.separator)
                    .append(sessionId)
                    .append(File.separator)
                    .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_CSV);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("Ruta temporal de ficheros CSV: %s", path.toString()));
            }
        } catch (Exception e) {
            throw new TechnicalException("Error al obtener la ruta temporal para almacenar los CSV ", e);
        }

        return path.toString();
    }
    
    private ExpedienteOtroDocumentoVO almacenarDocumentoCSV(HttpServletRequest request, FichaExpedienteForm fichaExpForm,
            UsuarioValueObject usuario, ByteArrayOutputStream outputStream, String codigoCSV)
            throws TechnicalException {
        
        ExpedienteOtroDocumentoVO documento;
        
        try {
            String[] params = usuario.getParamsCon();
            String codMunicipio = (String) fichaExpForm.getExpedienteVO().getAtributo("codMunicipio");
            String ejercicio = (String) fichaExpForm.getExpedienteVO().getAtributo("ejercicio");
            String numExpediente = (String) fichaExpForm.getExpedienteVO().getAtributo("numero");
            Boolean expHistorico = Boolean.valueOf((String)fichaExpForm.getExpedienteVO().getAtributo("expHistorico"));
            
            String[] datosExpediente = numExpediente.split("/");
            String codProcedimiento = datosExpediente[1];
            String codInternoDocumento = request.getParameter("codInternoDoc"); // Código interno del documento externo
            String nombreDoc = StringEscapeUtils.unescapeHtml(request.getParameter("nombre"));
            byte[] fichero = outputStream.toByteArray();
            String tipoMime = MimeTypes.PDF[0]; // Siempre va a ser formato PDF
            String extension = MimeTypes.FILEEXTENSION_PDF; // Siempre va a ser formato PDF
            
            Long idMetadato = expedienteOtroDocumentoManager.getDocumentoIdMetadato(codMunicipio, ejercicio, numExpediente, codInternoDocumento, params);
            String csv = codigoCSV;
            String csvAplicacion = ConstantesDatos.APLICACION_ORIGEN_DOCUMENTO_FLEXIA;
            
            // Generar URL de descarga del archivo CSV
            Map<String, Object> paramUrlCsv = new HashMap<String, Object>();
            paramUrlCsv.put("codigo", codInternoDocumento);
            paramUrlCsv.put("ejercicio", ejercicio);
            paramUrlCsv.put("numero", numExpediente);
            paramUrlCsv.put("codMunicipio", codMunicipio);
            paramUrlCsv.put("extension", extension);
            paramUrlCsv.put("tipoMime", tipoMime);
            paramUrlCsv.put("nombreFich", nombreDoc);
            paramUrlCsv.put("expHistorico", expHistorico);
            paramUrlCsv.put("otroDocExp", "si");
            paramUrlCsv.put("opcion", "0");
            String csvUri = documentoManager.crearURLCodigoSeguroVerificacion(paramUrlCsv);

            // Se obtiene la implementación del plugin correspondiente                        
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()), codProcedimiento);

            Hashtable<String, Object> datos = new Hashtable<String, Object>();
            datos.put("params", params);
            datos.put("codMunicipio", codMunicipio);
            datos.put("ejercicio", ejercicio);
            datos.put("numeroExpediente", numExpediente);
            datos.put("codDocumento", codInternoDocumento);
            datos.put("nombreDocumento", nombreDoc);
            datos.put("fichero", fichero);
            datos.put("tipoMime", tipoMime);
            datos.put("extension", extension);
            if (idMetadato != null) {
                datos.put("idMetadatoDocumento", idMetadato);
            }
            datos.put("metadatoDocumentoCsv", csv);
            datos.put("metadatoDocumentoCsvAplicacion", csvAplicacion);
            datos.put("metadatoDocumentoCsvUri", csvUri);

            int tipoDocumento = -1;
            if (!almacen.isPluginGestor()) {
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            } else { // TODO: De momento solo se va a implementar el acceso mediante base de datos
//                String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
//
//                datos.put("perteneceRelacion", "false");
//                datos.put("codProcedimiento", codProcedimiento);
//                datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
//
//                /**
//                 * SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE
//                 * ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL *
//                 */
//                ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
//                String carpetaRaiz = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
//
//                String descripcionOrganizacion = usuario.getOrg();
//                ArrayList<String> listaCarpetas = new ArrayList<String>();
//                listaCarpetas.add(carpetaRaiz);
//                listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
//                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
//                listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
//                datos.put("listaCarpetas", listaCarpetas);
//                /**
//                 * FIN *
//                 */
//                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            }

            Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);

            // Almacenar el fichero en bbdd/gestor documental
            if (!almacen.setDocumentoExternoFicheroCSV(doc)) {
                throw new TechnicalException("Error al intentar almacenar el documento");
            }
            
            // Obtenemos el fichero para actualizar la pantalla
            documento = expedienteOtroDocumentoManager.getDocumentoSinFichero(codInternoDocumento, ejercicio, codMunicipio, numExpediente, expHistorico, params);
        } catch (AlmacenDocumentoTramitacionException e) {
            throw new TechnicalException("Error al intentar almacenar el documento", e);
        }
        
        return documento;
    }
    
     /**
     * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición 
     * a alguna de las operaciones de este action
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json,HttpServletResponse response){
        
        try{
            if(json!=null){
                response.setCharacterEncoding("UTF-8");                
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
}
