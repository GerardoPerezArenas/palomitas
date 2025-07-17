// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.registro.DocumentoMetadatosVO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.TablaMetadatos;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.struts.StrutsFileValidation;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Vector;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import sun.misc.BASE64Decoder;
import java.util.*;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.util.commons.DateOperations;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.ByteArrayInOutStream;
import es.altia.util.commons.MimeTypes;
import es.altia.util.pdf.PdfHelper;
import es.altia.util.persistance.KeyValueObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils; 


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */
public class DocumentoRegistroAction extends ActionSession  {

    private static final ResourceBundle configRegistro = ResourceBundle.getBundle("Registro");
    private static final ResourceBundle configDocumentos = ResourceBundle.getBundle("documentos");
    
  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    try
    {  
    m_Log.debug("perform");
  
    Date fecha = new Date();
 
    m_Log.debug("================= DocumentoRegistroAction ======================>");    
    AnotacionRegistroManager anotacionRegistroManager = AnotacionRegistroManager.getInstance();
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");


    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);
            
    MantAnotacionRegistroForm regESForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");    
    Long tamAcumuladoGlobal = new Long(0);
    if(regESForm!=null)
    {   
        if(regESForm.getListaDocsAsignados()!=null && regESForm.getListaDocsAsignados().size()>0)
        {               
            for(int i=0;i<regESForm.getListaDocsAsignados().size();i++)
            {
                RegistroValueObject rvo = (RegistroValueObject)regESForm.getListaDocsAsignados().get(i);
                //byte[] file = rvo.getDoc();
                //tamAcumuladoGlobal = tamAcumuladoGlobal + file.length;                
                tamAcumuladoGlobal = tamAcumuladoGlobal + rvo.getLongitudDocumento();                
            }              
        }        
    }        


    if (form == null) {
      m_Log.debug("Rellenamos el form de fichero");
      form = new DocumentoRegistroForm();
      if ("request".equals(mapping.getScope())){          
        request.setAttribute(mapping.getAttribute(), form);
      }
      else{          
          session.setAttribute(mapping.getAttribute(), form);
      }
    }
    
    DocumentoRegistroForm fichaForm = (DocumentoRegistroForm)form;            
    FormFile fichero = null;
    if(fichaForm!=null && fichaForm.getFichero()!=null)
        if (!"".equals(fichaForm.getFichero().getFileName())) 
        fichero = fichaForm.getFichero();       
    
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario"); 
    
    //Traductor
    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
    traductor.setApl_cod(usuario.getAppCod());
    traductor.setIdi_cod(usuario.getIdioma());
    
    m_Log.debug("DocumentoRegistroAction - DocumentoRegistroForm es: " + fichaForm);
    
    if("documentoNuevo".equals(opcion)){

        String alta = request.getParameter("alta");
        
        if("true".equals(alta))
        {
            fichaForm.setModoAlta(1);
        }
        else{
            fichaForm.setModoAlta(0);
            fichaForm.setEntregado("N");
        }
        fichaForm.setTituloModificando("");
        fichaForm.setTituloFichero("");
        fichaForm.setfechaDocumento(Fecha.obtenerString(fecha));
        if (session.getAttribute("modificando")!=null) session.removeAttribute("modificando"); 
        opcion="documentoNuevo";
    } 
    else 
    if("documentoAlta".equals(opcion)){           
        boolean isExtensionCorrect = false;
        boolean isSizeCorrect = false;   
        boolean datosModificados = fichero!=null;
//        if ("si".equals(datosModificados)) fichaForm.setModificando(false);
        if(((fichaForm.getDocEscaneado()!=null && fichaForm.getDocEscaneado().length()>=1)||fichaForm.isModificando())&&!datosModificados){
            byte[] sByte;             
            String titulo = fichaForm.getTituloFichero();
            if (fichaForm.isModificando()) {
//                int codigo = Integer.parseInt(request.getParameter("codigo"));
//                titulo = request.getParameter("tituloDoc");
                int codigo = Integer.parseInt((String)session.getAttribute("codigo"));
                MantAnotacionRegistroForm mantARForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
                Vector docs = mantARForm.getListaDocsAsignados();
                RegistroValueObject registro = new RegistroValueObject();
                registro = (RegistroValueObject)docs.get(codigo);
                sByte = registro.getDoc();
                
            }else {
            // Si se ha escaneado el documento            
            // Se comprueba la longitud del documento escaneado            
                String escaner = fichaForm.getDocEscaneado();
                BASE64Decoder decoder = new BASE64Decoder();
                sByte = decoder.decodeBuffer(escaner);            
                
            }
            String extension = "jpg";
            File f = File.createTempFile(titulo,"." + extension);
            FileOutputStream fos = new FileOutputStream(f);            
            // Se guarda el array de bytes en disco para 
            fos.write(sByte);
            fos.flush();
            fos.close();
            
            isSizeCorrect = StrutsFileValidation.isSizeValid(f,StrutsFileValidation.APP_REGISTRO);
            //isExtensionCorrect = StrutsFileValidation.isExtensionValid(extension);            
            isExtensionCorrect = StrutsFileValidation.isExtensionValid(f,StrutsFileValidation.APP_REGISTRO);            
            f.delete();
            
            
            /******************************/
            // Se mueve el fichero subido a una determinada ruta en disco para alojar                     
            byte[] contenido = sByte;
            String nombreFichero = titulo + "." + extension;

            boolean continuar = false;
            String path = null;
            try{
                path = configDocumentos.getString("RUTA_DISCO_DOCUMENTOS");
                continuar = true;
            }catch(Exception e){
                m_Log.error("Error al recuperar la propiedad RUTA_DISCO_DOCUMENTOS en documentos.properties: " + e.getMessage());
                e.printStackTrace();
            }

            if(continuar){
                try{

                    String pathDir = path + File.separator + ConstantesDatos.SUBCARPETA_DOCUMENTOS_REGISTRO;                            

                    if(!FileOperations.createDirectory(pathDir)){
                      // Se redirige a la pantalla de alta para mostrar el error pertinente
                      request.setAttribute(ConstantesDatos.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO, "SI");
                      opcion = "documentoNuevo";


                    }else{                            
                        // Se crea el subdirectorio dentro del anterior con el session_id del usuario

                        String pathDirSessionId = pathDir + File.separator + session.getId();
                        if(!FileOperations.createDirectory(pathDirSessionId)){
                            // Se redirige a la pantalla de alta para mostrar el error pertinente
                            request.setAttribute(ConstantesDatos.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO, "SI");
                            opcion = "documentoNuevo";
                        }else{

                            String pathFile = pathDirSessionId + File.separator + nombreFichero;
                            FileOperations.writeFile(pathFile, contenido);
                            opcion = "documentoAlta";

                            request.setAttribute(ConstantesDatos.RUTA_FICHERO_SUBIDO_SERVIDOR,pathFile);
                        }
                    }
                }catch(Exception e){
                    m_Log.error("Error al copiar contenido del fichero adjuntado al directorio que los almacena temporalmente: " + e.getMessage());
                    e.printStackTrace();
                    // Se redirige a la pantalla de alta para mostrar el error pertinente
                    request.setAttribute(ConstantesDatos.ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO, "SI");
                    opcion = "documentoNuevo";
                }
            }else{
                // Se redirige a la pantalla de alta para mostrar el error pertinente
                request.setAttribute(ConstantesDatos.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO, "SI");
                opcion = "documentoNuevo";

            }
            
            
            /******************************/
            
            
        }        
        else // Si se ha sube el documento por POST
        if (fichero != null && fichero.getFileSize() > 0) {
            // Si se ha sube el documento por POST
            // Comprobación del tamaño del fichero y de extensión correcta    
            isExtensionCorrect = StrutsFileValidation.isExtensionValid(fichero, StrutsFileValidation.APP_REGISTRO);
            isSizeCorrect = StrutsFileValidation.isSizeValid(fichero, StrutsFileValidation.APP_REGISTRO);


            // Comprobacion de la extension del fichero
            if (!isExtensionCorrect) {
                m_Log.debug("La extensión del fichero no es válida");
                request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "si");
                String ext = (String) StrutsFileValidation.getLimite(StrutsFileValidation.EXTENSION_DOC_PREFFIX, StrutsFileValidation.SUFFIX_REGISTRO);
                request.setAttribute(ConstantesDatos.EXTENSION_PERMITED, ext);
                request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                opcion = "documentoNuevo"; // Volvemos al formulario de entrada           
            } else {
                request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "no");
            }

            // Comprobacion del tamaño del fichero
            if (!isSizeCorrect) {
                request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "si");
                Integer tam = Math.round((Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX, StrutsFileValidation.SUFFIX_REGISTRO)/ConstantesDatos.DIVISOR_BYTES);
                //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);                   
                request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE, tam);
                request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                opcion = "documentoNuevo"; // Volvemos al formulario de entrada           
            } else {
                request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "no");
            }

            // Se todo es correcto se procede a dar el documento de alta
            if (isSizeCorrect && isExtensionCorrect) {
                // Se comprueba si no se excede el límite global de tamaño para
                // los documentos subidos al registro           
                long tamGlobalRegistro = Long.parseLong((String) StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOCS_PREFFIX, StrutsFileValidation.APP_REGISTRO));
                m_Log.debug("******* Comprobación exceso limite global para documentos");
                m_Log.debug("******* LIMITE MÁXIMO GLOBAL FICHEROS REGISTRO ENTRADA: " + tamGlobalRegistro);
                m_Log.debug("******* TAMAÑO DEL DOCUMENTO SUBIDO: " + StrutsFileValidation.SIZE_FILE_VALIDATED);
                long suma = tamAcumuladoGlobal + StrutsFileValidation.SIZE_FILE_VALIDATED;
                m_Log.debug("******* TAMAÑO FICHERO + ACUMULADO GLOBAL: " + suma);

                if (suma > tamGlobalRegistro) {
                    m_Log.debug("Se excede el limite fijado para registro de " + tamGlobalRegistro);
                    // Se excede el límite global de tamaño máximo de ficheros 
                    // admitidos para el registro               
                    request.setAttribute(ConstantesDatos.EXCEED_LIMITE_GLOBAL_DOCUMENTOS_REGISTRO, "si");
                    request.setAttribute(ConstantesDatos.LIMITE_GLOBAL_DOCUMENTOS_REGISTRO, Math.round(tamGlobalRegistro / (long) ConstantesDatos.DIVISOR_BYTES));
                    opcion = "documentoNuevo";
                } else { // Se establece la suma total de los documentos subidos y se da de alta el documento
                    m_Log.debug("No se excede el limite fijado para registro de " + tamGlobalRegistro);
                    request.setAttribute(ConstantesDatos.EXCEED_LIMITE_GLOBAL_DOCUMENTOS_REGISTRO, "no");
                    //opcion = "documentoAlta";
                    
                    // Se mueve el fichero subido a una determinada ruta en disco para alojar                     
                    byte[] contenido = fichero.getFileData();
                    String nombreFichero = fichero.getFileName();
 
                    boolean continuar = false;
                    String path = null;
                    try{
                        path = configDocumentos.getString("RUTA_DISCO_DOCUMENTOS");
                        continuar = true;
                    }catch(Exception e){
                        m_Log.error("Error al recuperar la propiedad RUTA_DISCO_DOCUMENTOS en documentos.properties: " + e.getMessage());
                        e.printStackTrace();
                    }

                    if(continuar){
                        try{
                            
                            String pathDir = path + File.separator + ConstantesDatos.SUBCARPETA_DOCUMENTOS_REGISTRO;                            
                            
                            if(!FileOperations.createDirectory(pathDir)){
                              // Se redirige a la pantalla de alta para mostrar el error pertinente
                              request.setAttribute(ConstantesDatos.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO, "SI");
                              opcion = "documentoNuevo";
                                
                              
                            }else{                            
                                // Se crea el subdirectorio dentro del anterior con el session_id del usuario
                                
                                String pathDirSessionId = pathDir + File.separator + session.getId();
                                if(!FileOperations.createDirectory(pathDirSessionId)){
                                    // Se redirige a la pantalla de alta para mostrar el error pertinente
                                    request.setAttribute(ConstantesDatos.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO, "SI");
                                    opcion = "documentoNuevo";
                                }else{
                                
                                    String pathFile = pathDirSessionId + File.separator + nombreFichero;
                                    FileOperations.writeFile(pathFile, contenido);
                                    opcion = "documentoAlta";
                                    
                                    request.setAttribute(ConstantesDatos.RUTA_FICHERO_SUBIDO_SERVIDOR,pathFile);
                                }
                            }
                        }catch(Exception e){
                            m_Log.error("Error al copiar contenido del fichero adjuntado al directorio que los almacena temporalmente: " + e.getMessage());
                            e.printStackTrace();
                            // Se redirige a la pantalla de alta para mostrar el error pertinente
                            request.setAttribute(ConstantesDatos.ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO, "SI");
                            opcion = "documentoNuevo";
                        }
                    }else{
                        // Se redirige a la pantalla de alta para mostrar el error pertinente
                        request.setAttribute(ConstantesDatos.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO, "SI");
                        opcion = "documentoNuevo";
                                
                    }
                }
            }

        }


        // Comprobacion de que no este repetido el título, si no estamos modificando
        //MantAnotacionRegistroForm regESForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
        request.setAttribute(ConstantesDatos.DOCUMENT_TITLE_REPEATED,"no");
        if (regESForm != null && fichaForm.getTituloModificando().equals("")) {
            String titulo = fichaForm.getTituloFichero();
            Vector<RegistroValueObject> docs = regESForm.getListaDocsAsignados();
            for (RegistroValueObject doc : docs) {
                if (doc.getNombreDoc().equals(titulo)) {
                    request.setAttribute(ConstantesDatos.DOCUMENT_TITLE_REPEATED,"si");                    
                    opcion = "documentoNuevo"; // Volvemos al formulario de entrada 
                }
            }
            
        }                
    
    } else if("modificar".equals(opcion)){
 
         
        String tituloDoc = request.getParameter("tituloDoc");
        String codigo = request.getParameter("codigo");
        String fechaDoc = request.getParameter("fecha");
        String tipoDoc = request.getParameter("tipo");
        String entregado = request.getParameter("entr");
        String tipoDocumental = request.getParameter("tipoDocumental");
        session.setAttribute("codigo", codigo);
        String alta = request.getParameter("alta");
        if("true".equals(alta)){
            fichaForm.setModoAlta(1);
        }
        else{
            fichaForm.setModoAlta(0);
        }
        
        fichaForm.setTituloModificando(tituloDoc);
        fichaForm.setTituloFichero(tituloDoc);
        fichaForm.setEntregado(entregado);
        if ("".equals(fechaDoc) || fechaDoc == null)fichaForm.setFechaDocumento(Fecha.obtenerString(Calendar.getInstance().getTime()));        
        else fichaForm.setFechaDocumento(fechaDoc);        
        fichaForm.setTipoFichero(tipoDoc);
        fichaForm.setTipoDocumental(tipoDocumental);
        
        fichaForm.setModificando(!"".equals(tipoDoc) && tipoDoc != null);
        m_Log.debug("MODIFICANDO ES: " + true);
        session.setAttribute("modificando", fichaForm.isModificando());        

        opcion="documentoNuevo";
    } else if("documentoEliminar".equals(opcion)){
      String codigo = request.getParameter("codigoDocumento");
      fichaForm.setCodigo(codigo);
      opcion = "documentoEliminar";
    } else if("comprobarDocumentoMigrado".equals(opcion)){
        response.setContentType("text");
        try {
            Integer anoEntrada = Integer.parseInt(regESForm.getAnoAnotacion());
            Integer numeroEntrada = Integer.parseInt(regESForm.getNumeroAnotacion());
            String tituloDoc = request.getParameter("tituloDocumento");
            String respuesta = anotacionRegistroManager.getDocumentoMigrado(anoEntrada, numeroEntrada, tituloDoc, usuario.getParamsCon());
            response.getWriter().write(respuesta);
        } catch (IOException ioe) {
           m_Log.error("error escribiendo respuesta ajax comprobarDocumentoMigrado"+ ioe.getMessage());
        }
        
    
    } else if("documentoCotejar".equals(opcion)){
        String tituloDoc = request.getParameter("tituloDoc");

        // Obtencion de los listados para los desplegables
        List<KeyValueObject<Integer, String>> listaTipoDocumental = 
                anotacionRegistroManager.obtenerTiposMetadatosCotejo(null, TablaMetadatos.TIPO_DOCUMENTAL, usuario.getParamsCon());
        List<KeyValueObject<Integer, String>> listaEstadoElaboracion = 
                anotacionRegistroManager.obtenerTiposMetadatosCotejo(null, TablaMetadatos.ESTADO_ELABORACION, usuario.getParamsCon());
        
        // Obtencion de valores por defecto
        Integer valorDefectoTipoDocumental = 1;
        Integer valorDefectoEstadoElaboracion = 1;
        try{
            valorDefectoTipoDocumental = Integer.parseInt(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/DESPLEGABLE/TIPO_DOCUMENTAL")));
            valorDefectoEstadoElaboracion = Integer.parseInt(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/DESPLEGABLE/ESTADO_ELABORACION")));
        }catch(Exception e){
            e.printStackTrace();
            m_Log.error("Error al recuperar las propiedades de los valores por defecto de los desplegables de cotejo en registro.properties: " + e.getMessage());
        }
        
        request.setAttribute("listaTipoDocumental", listaTipoDocumental);
        request.setAttribute("valorDefectoTipoDocumental", valorDefectoTipoDocumental);
        request.setAttribute("listaEstadoElaboracion", listaEstadoElaboracion);
        request.setAttribute("valorDefectoEstadoElaboracion", valorDefectoEstadoElaboracion);
        request.setAttribute("tituloDoc", tituloDoc);

        opcion = "documentoCotejar";
    } else if ("documentoCotejarFirma".equals(opcion)) {
        String tituloDoc = request.getParameter("tituloDoc");

      m_Log.debug("tituloDoc "+ tituloDoc);

        if (regESForm != null) {
            RegistroValueObject doc = null;

            // Obtenemos el documento a partir del nombre seleccionado en la vista
            for (int i = 0; i < regESForm.getListaDocsAsignados().size(); i++) {
                doc = (RegistroValueObject) regESForm.getListaDocsAsignados().get(i);
                      m_Log.debug("docLista[ "+ i+"] :"+doc.getNombreDoc());
                if (tituloDoc.equals(doc.getNombreDoc())) {
                    break;
                }
            }

            // Obtenemos el contenido del fichero
            Documento docFichero = anotacionRegistroManager.getDocumentoFichero(doc, usuario);
            
             m_Log.debug("contenido Fichero "+ docFichero.getFichero()); 

            if (doc != null && docFichero != null) {
                String texto1 = null;
                String texto2 = null;
                float alto = 0;
                float ancho = 0;
                float posXCentro = 0;
                float posYCentro = 0;
                float rotacion = 0;
                float offsetTextoX = 0;
                float tamanoFuente = 0;
                float escalado = 0;
                boolean superpuesto = false;
                boolean pintarBorde = false;
                ByteArrayInputStream inputStream = null;
                ByteArrayOutputStream outputStream = null;
                ByteArrayInOutStream tempOutputStream = null;
                
                try {
                    // Obtenemos las propiedades necesarias para poder incrustar el texto en el pdf
                    texto1 = configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_TEXTO1"));
                    texto2 = configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_TEXTO2"));
                    alto = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_ALTO")));
                    ancho = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_ANCHO")));
                    posXCentro = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_POS_X_CENTRO")));
                    posYCentro = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_POS_Y_CENTRO")));
                    rotacion = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_ROTACION")));
                    offsetTextoX = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_OFFSET_TEXTO_X")));
                    tamanoFuente = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_TAMANO_FUENTE")));
                    superpuesto = Boolean.parseBoolean(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_SUPERPUESTO")));
                    pintarBorde = Boolean.parseBoolean(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/RECUADRO_PINTAR_BORDE")));
                    escalado = Float.parseFloat(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/ESCALADO")));
                            
                    // Realizamos un escalado del contenido del documento para hacer sitio
                    // para incrustar el texto lateral
                    inputStream = new ByteArrayInputStream(docFichero.getFichero());
                    tempOutputStream = new ByteArrayInOutStream();
                    PdfHelper.ajustarPaginaPDF(inputStream, tempOutputStream, escalado);
                    
                    // Se incrusta el texto de que es una copia en el lateral izquierdo del documento
                    outputStream = new ByteArrayOutputStream();
                    PdfHelper.incrustarTextoLateralPDF(
                            tempOutputStream.getInputStream(), outputStream,
                            Arrays.asList(texto1, texto2),
                            alto, ancho, posXCentro, posYCentro, rotacion,
                            offsetTextoX, tamanoFuente, superpuesto, pintarBorde);
                    
                    // Grabar en disco en el servidor
                    String nombreFicheroTemporal = String.format("%s.%s.%s", tituloDoc, ConstantesDatos.EXTENSION_FICHERO_PDF, ConstantesDatos.EXTENSION_FICHERO_TMP_COTEJO);
                    grabarFicheroTemporalServidor(outputStream.toByteArray(), nombreFicheroTemporal, session.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    m_Log.error("Error al intentar incrustar el texto lateral en el documento PDF", e);
                    request.setAttribute("MENSAJE_ERRORES_COTEJO", traductor.getDescripcion("msjErrorInternoCotejo"));
                } finally {
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                    IOUtils.closeQuietly(tempOutputStream);
                }
            }
        }

        // Obtenemos la URL con la jsp que realizara la firma del documento
        Config configPortafirmas = ConfigServiceHelper.getConfig("Portafirmas");
        String plugin = configPortafirmas.getString(String.format("%d/%s", usuario.getOrgCod(), "cotejo/PluginPortafirmas"));
        String url = configPortafirmas.getString(String.format("%s/%s/%s", "PluginPortafirmas", plugin, "urlFirmarCotejo"));

        // Variables necesarias para firmar el documento
        String algoritmoCifrado = configRegistro.getString(String.format("%d/%s", usuario.getOrgCod(), "COTEJO/FIRMA/ALGORITMO_CIFRADO"));
        String formatoFichero = configRegistro.getString(String.format("%d/%s", usuario.getOrgCod(), "COTEJO/FIRMA/FORMATO_FICHERO"));
        Config configCommon = ConfigServiceHelper.getConfig("common");
        String hostVirtual = configCommon.getString("hostVirtual");
        Integer desfaseEnvioServidor = Integer.parseInt(
                configRegistro.getString(String.format("%d/%s",
                                usuario.getOrgCod(), "COTEJO/FIRMA/DESFASE_ENVIO_SERVIDOR")));
                
        request.setAttribute("algoritmoCifrado", algoritmoCifrado);
        request.setAttribute("formatoFichero", formatoFichero);
        request.setAttribute("hostVirtual", hostVirtual);
        request.setAttribute("tituloDoc", tituloDoc);
        request.setAttribute("desfaseEnvioServidor", desfaseEnvioServidor);
    
        return new ActionForward(url);
    } else if ("obtenerFicheroTemporalCotejo".equals(opcion)) {
        BufferedOutputStream bos = null;
        
        String tituloDoc = request.getParameter("tituloDoc");
        
      try {

            // Obtenemos el fichero del servidor

            String nombreFicheroTemporal = String.format("%s.%s.%s", tituloDoc, ConstantesDatos.EXTENSION_FICHERO_PDF, ConstantesDatos.EXTENSION_FICHERO_TMP_COTEJO);
            
            m_Log.debug("nombreFicheroTemporal "+nombreFicheroTemporal);

            byte[] ficheroCotejo = obtenerFicheroTemporalServidor(nombreFicheroTemporal, session.getId());



            if (ficheroCotejo != null) {

                // Se devuelve el contenido del fichero a traves del navegador
                response.setContentType(ConstantesDatos.TIPO_MIME_DOCUMENTO_PDF);
                response.setHeader("Content-Disposition", "attachment; filename=" + nombreFicheroTemporal);
                response.setContentLength(ficheroCotejo.length);
                bos = new BufferedOutputStream(response.getOutputStream());
                bos.write(ficheroCotejo, 0, ficheroCotejo.length);
                bos.flush();
            } else {
                throw new TechnicalException("Error al obtener el fichero de cotejo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al intentar obtener el fichero de cotejo del servidor", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {

            IOUtils.closeQuietly(bos);
        }
        

        return null;
    } else if ("verificarOperacionCotejo".equals(opcion)) {
        try {
            // Solo se realiza si /COTEJO/FIRMA/VERIFICACION_AL_FIRMAR esta a SI
            String verificar = configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/FIRMA/VERIFICACION_AL_FIRMAR"));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("Se realizara la verificacion de la firma: %s", verificar));
            }

            if (ConstantesDatos.SI.equalsIgnoreCase(verificar)) {
                String firmaBase64 = request.getParameter("firmaBase64");
                
                if (StringUtils.isNotEmpty(firmaBase64)) {
                    // Obtenemos la implementacion del plugin de Portafirmas configurada en el properties
                    PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(usuario.getOrgCod())+"/cotejo");

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(String.format("Implementacion utilizada: %s", plugin.getClass().getCanonicalName()));
                    }

                    DocumentoFirmadoVO documento = new DocumentoFirmadoVO();
                    documento.setFirma(firmaBase64);
                    documento.setTipoMime(MimeTypes.PDF[0]);

                    if (plugin.verificarFirma(documento)) {
                        m_Log.debug("La firma del documento es correcta");
                    } else {
                        m_Log.warn("La firma del documento no es valida");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write(traductor.getDescripcion("errFirmaDocumentoNoValida"));
                    }
                } else {
                    throw new TechnicalException("No se ha encontrado la firma en la request");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al intentar verificar la firma", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(traductor.getDescripcion("msjErrorInternoCotejo"));
        }

        return null;
    } else if ("finalizarCotejarDocumento".equals(opcion)) {
        
        // Obtenemos parametros y atributos de la request/sesion
        String tituloDoc = request.getParameter("tituloDoc");
        String codTipoDocumental = request.getParameter("codTipoDocumental");
        String codEstadoElaboracion = request.getParameter("codEstadoElaboracion");
        String firmaBase64 = request.getParameter("firmaBase64");

        try {
            if (StringUtils.isNotEmpty(firmaBase64)) {
                // El fichero viene en base64, por lo que es necesario decodificarlo antes de grabarlo a disco
                byte[] ficheroCotejo = new Base64().decode(firmaBase64.getBytes(ConstantesDatos.CHARSET_UTF_8));                

                // Obtenemos el registro y el documento seleccionado con la informacion del fichero
                if (regESForm != null && regESForm.getRegistro() != null && regESForm.getListaDocsAsignados() != null) {
                    RegistroValueObject registro = regESForm.getRegistro();
                    RegistroValueObject documento = null;

                    for (int i = 0; i < regESForm.getListaDocsAsignados().size(); i++) {
                        documento = (RegistroValueObject) regESForm.getListaDocsAsignados().get(i);
                        if (tituloDoc.equals(documento.getNombreDoc())) {
                            break;
                        }
                    }

                    // Valor para tipo firma
                    Integer tipoFirma = Integer.parseInt(configRegistro.getString(String.format("%d%s", usuario.getOrgCod(), "/COTEJO/DESPLEGABLE/TIPO_FIRMA")));
                    // Organo
                    String codUorVisible = UORsManager.getInstance().getCodigoVisibleUorByCodUor(String.valueOf(registro.getUnidadOrgan()), usuario.getParamsCon());
                    // Fecha de firma
                    Calendar sysdate = GregorianCalendar.getInstance();
                    String fechaDoc = DateOperations.toString(sysdate, DateOperations.LATIN_DATE_FORMAT);

                    // Rellenamos los metadatos
                    DocumentoMetadatosVO metadatos = new DocumentoMetadatosVO();
                    metadatos.setDepartamento(registro.getIdentDepart());
                    metadatos.setUor(registro.getUnidadOrgan());
                    metadatos.setEjercicio(registro.getAnoReg());
                    metadatos.setNumero(registro.getNumReg());
                    metadatos.setTipoRegistro(registro.getTipoReg());
                    metadatos.setNombreDoc(documento.getNombreDoc());
                    metadatos.setVersionNTI(ConstantesDatos.METADATO_COTEJO_VERSION_NTI);
                    metadatos.setIdDocumento(null); // Autogenerado en BBDD
                    metadatos.setOrgano(codUorVisible);
                    metadatos.setFechaCaptura(sysdate);
                    metadatos.setOrigen(ConstantesDatos.ID_ORIGEN_CIUDADANO); // TODO
                    metadatos.setEstadoElaboracion(Integer.parseInt(codEstadoElaboracion));
                    metadatos.setNombreFormato(ConstantesDatos.METADATO_COTEJO_NOMBRE_FORMATO);
                    metadatos.setTipoDocumental(Integer.parseInt(codTipoDocumental));
                    metadatos.setTipoFirma(tipoFirma);

                    // Comprobar si se supera el tamano global de ficheros
                    long tamGlobalRegistro = Long.parseLong((String) StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOCS_PREFFIX, StrutsFileValidation.APP_REGISTRO));
                    long nuevoAcumuladoGlobal = tamAcumuladoGlobal - documento.getLongitudDocumento() + ficheroCotejo.length;
                    
                    if (nuevoAcumuladoGlobal <= tamGlobalRegistro) {
                        if (m_Log.isDebugEnabled()) { m_Log.debug(String.format("No se excede el limite fijado para registro de: %d", tamGlobalRegistro)); }
                        
                        // Grabamos el fichero nuevo o sobreescribimos el no cotejado en el servidor
                        String nombreFichero = String.format("%s.%s", tituloDoc, ConstantesDatos.EXTENSION_FICHERO_PDF);
                        String rutaFichero = grabarFicheroTemporalServidor(ficheroCotejo, nombreFichero, session.getId());

                        RegistroValueObject documentoInsertar = (RegistroValueObject) SerializationUtils.clone(documento);
                        documentoInsertar.setMetadatosDoc(metadatos);
                        documentoInsertar.setCotejado(ConstantesDatos.SI);
                        documentoInsertar.setFechaDoc(fechaDoc);
                        documentoInsertar.setDocNormal(true);
                        documentoInsertar.setLongitudDocumento(ficheroCotejo.length);
                        documentoInsertar.setDoc(ficheroCotejo);
                        documentoInsertar.setRutaDocumentoRegistroDisco(rutaFichero);

                        reorganizarListadoDocumentos(regESForm, documentoInsertar);
                    } else {
                        m_Log.info(String.format("Se excede el limite fijado para registro de: %d", tamGlobalRegistro));
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        String msjError = String.format("%s %d bytes", traductor.getDescripcion("tamGlobalExceed"), tamGlobalRegistro);
                        response.getWriter().write(msjError);
                    }
                }

            } else {
                m_Log.error("No se pudo obtener el documento cotejado codificado en base64");
                throw new TechnicalException("No se pudo obtener el documento cotejado codificado en base64");
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al finalizar las operaciones de cotejo de documento de registro", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(traductor.getDescripcion("msjErrorInternoCotejo"));
        }
        return null;
    } else if("refrescarListaDocumentoRE".equals(opcion)){
        opcion = "refrescarListaDocumentoRE";
    } else if("limpiarFicherosTemporalesCotejo".equals(opcion)){
        // Se borran los ficheros temporales de cotejo
        try {
            borrarFicherosTemporalesCotejo(session.getId());
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al intentar borrar los ficheros temporales de los documentos cotejados", e);
        }
        return null;
    } else if("consultarDocumentoCotejado".equals(opcion)){
        DocumentoMetadatosVO metadatos = null;
        Integer indexFichero = null;
        
        // Obtenemos parametros y atributos de la request/sesion
        String tituloDoc = request.getParameter("tituloDoc");
        String indexFicheroParam = request.getParameter("indexFichero");
        if (StringUtils.isNumeric(indexFicheroParam)) {
            indexFichero = Integer.parseInt(indexFicheroParam);
        }
        
        // Obtenemos el documento seleccionado con la informacion de los metadatos
        if (regESForm != null && regESForm.getListaDocsAsignados() != null) {
            RegistroValueObject rvo = null;

            // Si ya tenemos el indice porque venimos de otras pantallas de
            // documento cotejado buscamos directamente por el mismo
            // De lo contrario recorremos el array de ficheros y buscamos por nombre
            // de documento.
            if (indexFichero != null) {
                rvo = (RegistroValueObject)regESForm.getListaDocsAsignados().get(indexFichero);
                metadatos = rvo.getMetadatosDoc();
            } else {
                for (int i = 0; i < regESForm.getListaDocsAsignados().size(); i++) {
                    rvo = (RegistroValueObject)regESForm.getListaDocsAsignados().get(i);
                    if (tituloDoc.equals(rvo.getNombreDoc()) && rvo.getEstadoDocumentoRegistro() != ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO) {
                        metadatos = rvo.getMetadatosDoc();
                        indexFichero = i;
                        break;
                    }
                }
            }
        }
        
        if (metadatos != null) {
            String origen = null;
            String descripcion = null;
            
            if (ConstantesDatos.ID_ORIGEN_CIUDADANO  == metadatos.getOrigen()) {
                origen = ConstantesDatos.DESC_ORIGEN_CIUDADANO;
            } else if (ConstantesDatos.ID_ORIGEN_ADMINISTRACION == metadatos.getOrigen()) {
                origen = ConstantesDatos.DESC_ORIGEN_ADMINISTRACION;
            }
            
            descripcion = anotacionRegistroManager.obtenerDescripcionTipoFirmaMetadatosCotejoById(metadatos.getTipoFirma(), usuario.getParamsCon());
            
            request.setAttribute("fechaCaptura", DateOperations.toString(metadatos.getFechaCaptura(), DateOperations.LATIN_DATE_FORMAT));
            request.setAttribute("origen", StringUtils.trim(origen));
            request.setAttribute("organo", StringUtils.trim(metadatos.getOrgano()));
            request.setAttribute("tipoFirma", StringUtils.trim(descripcion));
        } else {
            m_Log.error(String.format("No se han encontrado metadatos para el documento: %s", tituloDoc));
        }
        
        request.setAttribute("indexFichero", indexFichero);
        opcion = "consultarDocumentoCotejado";
    } else if("verificacionFirmaDocumento".equals(opcion)) {
        RegistroValueObject doc = null;
        Integer indexFichero = null;
        String msjEstadoFirmaCertificado = null;
        boolean firmaValida = false;
        String datosCertificado = null;
        String idFirmante = null;
        String nombreFirmante = null;
        String emisorCertificado = null;
        String validezCertificado = null;

        // Obtenemos parametros y atributos de la request/sesion
        String indexFicheroParam = request.getParameter("indexFichero");
        if (StringUtils.isNumeric(indexFicheroParam)) {
            indexFichero = Integer.parseInt(indexFicheroParam);
            doc = (RegistroValueObject) regESForm.getListaDocsAsignados().get(indexFichero);

            // Obtenemos el contenido del fichero
            Documento docFichero = anotacionRegistroManager.getDocumentoFichero(doc, usuario);
            
            if (docFichero != null && docFichero.getFichero() != null && docFichero.getFichero().length > 0) {
                // Codificamos el fichero en base64 para la verificacion y obtencion de los datos del certificado
                String ficheroBase64 = new String(new Base64().encode(docFichero.getFichero()), ConstantesDatos.CHARSET_UTF_8);

                PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(usuario.getOrgCod()));

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(String.format("Implementacion utilizada: %s", plugin.getClass().getCanonicalName()));
                }

                DocumentoFirmadoVO documento = new DocumentoFirmadoVO();
                documento.setFirma(ficheroBase64);
                documento.setFicheroHash64(ficheroBase64);
                documento.setTipoMime(docFichero.getTipoMimeContenido());
                
                // Verificacion y extraccion de los datos de los certificados
                ArrayListFirmasVO infoCertificado = plugin.verificarFirmaInfo(documento);
                if (infoCertificado != null && StringUtils.isEmpty(infoCertificado.getResultadoVerificacion())) {
                    FirmaVO firma = infoCertificado.get(0);

                    datosCertificado = firma.getAsuntoCertificado();
                    idFirmante = firma.getNif();
                    nombreFirmante = firma.getNombrePersona();
                    emisorCertificado = firma.getEmisorCertificado();
                    validezCertificado = firma.getValidez(); 

                    if (firma.getValido()) {
                        firmaValida = true;
                        msjEstadoFirmaCertificado = traductor.getDescripcion("msjFirmaCertificadoCorrecta");
                    } else {
                        msjEstadoFirmaCertificado = traductor.getDescripcion("msjFirmaCertificadoIncorrecta");
                    }
                } else {
                    m_Log.error("Error al intentar obtener los datos del certificado");
                    msjEstadoFirmaCertificado = traductor.getDescripcion("msjErrorInterno");
                }
            } else {
                m_Log.error(String.format("Documento no encontrado: %s", doc.getNombreDoc()));
                msjEstadoFirmaCertificado = traductor.getDescripcion("msjErrorInterno");
            }
        } else {
            m_Log.error(String.format("Parametro 'indexFichero' vacio o no numerico: %s", indexFicheroParam));
            msjEstadoFirmaCertificado = traductor.getDescripcion("msjErrorInterno");
        }

        request.setAttribute("msjEstadoFirmaCertificado", msjEstadoFirmaCertificado);
        request.setAttribute("firmaValida", firmaValida);
        request.setAttribute("datosCertificado", datosCertificado);
        request.setAttribute("idFirmante", idFirmante);
        request.setAttribute("nombreFirmante", nombreFirmante);
        request.setAttribute("emisorCertificado", emisorCertificado);
        request.setAttribute("validezCertificado", validezCertificado);
        request.setAttribute("indexFichero", indexFicheroParam);
        
        opcion = "verificacionFirmaDocumento";
    } else if ("enviarMailCotejo".equals(opcion)) {
        try {
            String indexFichero = request.getParameter("indexFichero");
            String email = request.getParameter("correo");
            
            if (regESForm != null && regESForm.getRegistro() != null) {
                RegistroValueObject elRegistroESVO = regESForm.getRegistro();
                
                anotacionRegistroManager.enviarCorreoDocumentoCotejo(elRegistroESVO, usuario, Integer.parseInt(indexFichero), email);
            } else {
                throw new Exception("No existen los datos del registro");
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.debug(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(traductor.getDescripcion("errEnviarEmail"));
        }
        
        return null;
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }   
        
    // Cada vez que se excede el tamaño máximo del fichero a subir por POST el parámetro opcion
    // llega vacío
    //if(opcion==null && fichaForm!=null && (fichaForm.getFichero()==null || (fichaForm.getFichero()!=null && fichaForm.getFichero().getFileSize()==0))){
    if(opcion==null){
        // Se indica que hay error y se vuelve a la página que
        // permite dar de alta un nuevo documento        
        //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);  
        int num = (Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX,StrutsFileValidation.SUFFIX_REGISTRO);        

        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE,Math.round(num/ConstantesDatos.DIVISOR_BYTES));
        request.setAttribute("ERROR_FILESIZE_UPLOAD","si");
        opcion = "documentoNuevo";        
    }
    m_Log.debug("<================= DocumentoRegistroAction ======================");        
    return (mapping.findForward(opcion));
  }
  catch(Exception e){
      // Si ocurre algún error
      e.printStackTrace();
      m_Log.error("Se ha producido el siguiente error: " + e.getMessage());
      return null;
  }
     
  }

    /**
     * Graba el contenido del fichero temporal de cotejo al directorio temporal del servidor
     * 
     * @param fichero
     * @param nombreFichero
     * @param sessionID
     * @return 
     *   @throws IOException
     */  
    private String grabarFicheroTemporalServidor(byte[] fichero, String nombreFichero, String sessionID) throws IOException {
        String rutaCompletaFichero = null;

        String path = configDocumentos.getString("RUTA_DISCO_DOCUMENTOS");

        StringBuilder pathFile = new StringBuilder();

        pathFile.append(path)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_REGISTRO)
                .append(File.separator)
                .append(sessionID)
                .append(File.separator)
                .append(nombreFichero);

        FileUtils.writeByteArrayToFile(new File(pathFile.toString()), fichero);
        rutaCompletaFichero = pathFile.toString();


        return rutaCompletaFichero;
    }

    /**
     * Obtiene el contenido del fichero temporal de cotejo del directorio temporal del servidor
     * 
     * @param nombreFichero
     * @param sessionID
     * @return 
     * @throws IOException
     */
    private byte[] obtenerFicheroTemporalServidor(String nombreFichero, String sessionID) throws IOException {
        byte[] fichero = null;

        String path = configDocumentos.getString("RUTA_DISCO_DOCUMENTOS");

        StringBuilder pathFile = new StringBuilder();
        pathFile.append(path)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_REGISTRO)
                .append(File.separator)
                .append(sessionID)
                .append(File.separator)
                .append(nombreFichero);

        fichero = FileUtils.readFileToByteArray(new File(pathFile.toString()));

        return fichero;
    }
    
    /**
     * Borra los ficheros temporales de cotejo del directorio temporal del servidor
     * 
     * @param sessionID
     * @throws IOException
     */
    private void borrarFicherosTemporalesCotejo(String sessionID) {
        String path = configDocumentos.getString("RUTA_DISCO_DOCUMENTOS");

        StringBuilder directorio = new StringBuilder();
        directorio.append(path)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_REGISTRO)
                .append(File.separator)
                .append(sessionID)
                .append(File.separator);

        File carpeta = new File(directorio.toString());
        File listaFicheros[] = carpeta.listFiles();

        for (File fichero : listaFicheros) {
            if (fichero.getName().endsWith(String.format(".%s", ConstantesDatos.EXTENSION_FICHERO_TMP_COTEJO))) {
                fichero.delete();

            }
        }

    }
    
    private void reorganizarListadoDocumentos(MantAnotacionRegistroForm regESForm, RegistroValueObject documento) {
        Vector docs = regESForm.getListaDocsAsignados();

        int posicion = 0;
        for (int h = 0; h < docs.size(); h++) {
            RegistroValueObject doc = (RegistroValueObject) docs.get(h);
            if (doc.getNombreDoc().equals(documento.getNombreDoc())) {
                posicion = docs.indexOf(doc);
                break;
            }
        }

        // Si el archivo no es nuevo, debemos eliminar el antiguo y marcar este como nuevo
        if (documento.getEstadoDocumentoRegistro() != ConstantesDatos.ESTADO_DOCUMENTO_NUEVO) {
            if (regESForm.getNumero() != null && regESForm.getNumero().length() > 0) {
                // El objeto antiguo que se ha modificado, se marcará como eliminado y se añade al final
                // de la lista de documentos
                RegistroValueObject documentoModificado = (RegistroValueObject) docs.get(posicion);
                documentoModificado.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO);
                docs.add(documentoModificado);
            }
        }

        // Se añade el documento recien modificado y cuyo estado sera el de nuevo a la posición que
        // ocupaba el documento anterior, es decir, el modificado
        documento.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
        docs.set(posicion, documento);
    }
}
