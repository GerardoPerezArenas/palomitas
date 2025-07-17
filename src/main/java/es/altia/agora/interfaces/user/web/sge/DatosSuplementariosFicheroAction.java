// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import com.google.gson.Gson;
import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.sge.CampoSuplementarioFicheroVO;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.util.*;
import es.altia.agora.interfaces.user.web.util.ResultadoAjax;
import es.altia.agora.technical.ConstantesAjax;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.DocumentConversionException;
import es.altia.common.exception.FormatNotSupportedException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.commons.JODConverterHelper;
import es.altia.util.commons.MimeTypes;
import es.altia.util.commons.WebOperations;
import es.altia.util.struts.StrutsFileValidation;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import sun.misc.BASE64Decoder;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */
public class DatosSuplementariosFicheroAction extends ActionSession  {
    
    protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("perform");
    long totalAcumulado = 0;
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("DatosSuplementariosFicheroAction-la opcion en el action es " + opcion);

    if (form == null) {
      m_Log.debug("DatosSuplementariosFicheroAction-Rellenamos el form de fichero");
      form = new DatosSuplementariosFicheroForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    DatosSuplementariosFicheroForm fichaForm = (DatosSuplementariosFicheroForm)form;
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    
    // Este atributo se utiliza para saber si se entra en este action desde tramitacion de expedientes.
    // En ese caso se pone a si este atributo
    request.setAttribute("desdeTramitacionExpedientes","no");
    
    String codTramite = request.getParameter("codTramite");
    String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
    
    FichaExpedienteForm fichaExpForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");        
    if(fichaExpForm!=null){  
        
           
        
        
        /************************************************************************/
        /****** SE OBTIENE LA SUMA DE LOS TAMAÑOS DE TODOS LOS FICHEROS DE  *****/
        /****** CAMPOS SUPLEMENTARIOS DE TIPO FICHERO                       *****/
        /***********************************************************************/
        GeneralValueObject listLongitudesFicheros = fichaExpForm.getListaLongitudFicherosDisco();
        java.util.Hashtable tabla = listLongitudesFicheros.getTabla();
             
        for(java.util.Iterator it = tabla.keySet().iterator();it.hasNext();)
        {
            String clave = (String)it.next();
            
            if(tabla.get(clave)!=null && tabla.get(clave).toString().length()>=1)
            {     
                Integer longitud = (Integer)tabla.get(clave);             
                totalAcumulado = totalAcumulado + longitud;
            }     
        } 
        m_Log.debug("totalAcumulado: " + totalAcumulado);
        
    }
        
    m_Log.debug(">>>>>>>>>>Codigo del formulario:  " + fichaForm.getCodigo());
    String codigo = null;
    if(request.getParameter("codigo")!=null && request.getParameter("codigo").length()>0)    
        codigo = request.getParameter("codigo");
    else
        codigo = fichaForm.getCodigo();
    
    if("inicio".equals(opcion)){
        
        request.setAttribute("codTramite",codTramite);
        request.setAttribute("ocurrenciaTramite",ocurrenciaTramite);
        
        m_Log.debug("CODIGO"+ codigo);
        opcion="inicio";
    }
    else if("cargar".equals(opcion)){        
        boolean isExtensionCorrect = false;
        boolean isSizeCorrect = false;     
                
        boolean documentoEscaneado = false;
        byte[] contenido = null;
        String nombreFichero = null;
        if(fichaForm.getDocEscaneado()!=null && fichaForm.getDocEscaneado().length()>=1)
        {
            // Si se ha escaneado el documento
            m_Log.debug("DatosSuplementariosFicheroAction - Hay documento escaneado => Se procede a la comprobación de su tamaño");
            // Se comprueba la longitud del documento escaneado
            String escaner = fichaForm.getDocEscaneado();
            BASE64Decoder decoder = new BASE64Decoder();
            //byte[] sByte = decoder.decodeBuffer(escaner);
            contenido = decoder.decodeBuffer(escaner);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("ddMMyyyy_HHmmss");
            //String titulo = ConstantesDatos.DOCUMENTO + "_" + formato.format(calendar.getTime());
            nombreFichero= ConstantesDatos.DOCUMENTO + "_" + formato.format(calendar.getTime());
            String extension = "jpg";            
            m_Log.debug("Nombre del fichero: " + nombreFichero + "." + extension);
            
            try{
                
                File f = File.createTempFile(nombreFichero,"." + extension);
                FileOutputStream fos = new FileOutputStream(f);            
                // Se guarda el array de bytes en disco para 
                fos.write(contenido);
                fos.flush();
                fos.close();
                m_Log.debug("DatosSuplementariosFicheroAction - fichero temporal: " + f.getPath());
                m_Log.debug("DatosSuplementariosFicheroAction - fichero temporal tamaño: " + f.length());            
                               
                isSizeCorrect = StrutsFileValidation.isSizeValid(f,StrutsFileValidation.APP_EXPEDIENTES);
                isExtensionCorrect = StrutsFileValidation.isExtensionValid(f,StrutsFileValidation.APP_EXPEDIENTES);            
                f.delete();
                documentoEscaneado = true;
            }
            catch(Exception e){
                e.printStackTrace();
                m_Log.debug(">>>>> Exception: " + e.getMessage());
            }
        }        
        else // Si se sube fichero por POST
        if(fichaForm.getFichero()!=null && fichaForm.getFichero().getFileSize()>0){
            FormFile fichero = fichaForm.getFichero();                                
            //isExtensionCorrect = true; 
            //isSizeCorrect      = true; 
            isExtensionCorrect = StrutsFileValidation.isExtensionValid(fichero,StrutsFileValidation.APP_EXPEDIENTES);
            isSizeCorrect      = StrutsFileValidation.isSizeValid(fichero,StrutsFileValidation.APP_EXPEDIENTES);                       
        }        
        
        if(!isExtensionCorrect){       
            
            m_Log.debug("La extensión del fichero no es válida");
            request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT,"si");                              
            request.setAttribute(ConstantesDatos.EXTENSION_PERMITED,StrutsFileValidation.getLimite(StrutsFileValidation.EXTENSION_DOC_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES));                    
            request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES,ConstantesDatos.DESCRIPCION_BYTES);
            opcion = "inicio"; // Volvemos al formulario de entrada           
        }else{
           m_Log.debug("La extensión del fichero es válida");
           request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT,"no");       
        }

        if(!isSizeCorrect){       
            m_Log.debug("El tamaño del fichero excede el límite máximo");
            //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);                               
            int num = Math.round((Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES)/ConstantesDatos.DIVISOR_BYTES);                   
            request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE,num);
            request.setAttribute("ERROR_FILESIZE_UPLOAD","si");                        
            request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES,ConstantesDatos.DESCRIPCION_BYTES);                
            opcion = "inicio"; // Volvemos al formulario de entrada           
            
        }else {
            m_Log.debug("El tamaño del fichero no excede el límite máximo");
           request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED,"no");              
        }       
                
                  
       if(isSizeCorrect && isExtensionCorrect)
       {
           long tamGlobalExpedientes = Long.parseLong((String)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOCS_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES));                                         
           m_Log.debug("Tamaño global expedientes: " + tamGlobalExpedientes);                       
            long suma = totalAcumulado + StrutsFileValidation.SIZE_FILE_VALIDATED;
            //long tamGlobalExpedientes = 2;
              //   long suma =        0;
            if(suma>tamGlobalExpedientes){                 
                m_Log.debug("La suma: " + suma + " excede el limite fijado para expedientes de " + tamGlobalExpedientes);                 
                m_Log.debug("redondeo exceso ficheros global " + Math.round(tamGlobalExpedientes/(long)ConstantesDatos.DIVISOR_BYTES));
                // Se excede el límite global de tamaño máximo de ficheros 
                // admitidos para el registro               
                request.setAttribute(ConstantesDatos.EXCEED_LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES,"si");          
                request.setAttribute(ConstantesDatos.LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES,Math.round(tamGlobalExpedientes/(long)ConstantesDatos.DIVISOR_BYTES));               
                opcion = "inicio";               
            }
            else{
                
                boolean directorio = false;
                String PATH_DIR = null;
                try{
                    ResourceBundle config = ResourceBundle.getBundle("documentos");
                    PATH_DIR = config.getString("RUTA_DISCO_DOCUMENTOS");
                    directorio = true;
                }catch(Exception e){
                   m_Log.error("Eror al obtener la propiedad que indica la ruta en la que se almacenan los documentos en disco: " + e.getMessage());
                   request.setAttribute(ConstantesDatos.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO,"SI");
                   opcion = "inicio"; 
                }
                
                if(directorio){
                m_Log.info("el directorio existe ");
                    if(PATH_DIR!=null){
                        String subPathDir = PATH_DIR + File.separator + ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE;
                        boolean subpathdir = false;
                        m_Log.info("el sub path es: "+subPathDir);
                        try{
                            File dir = new File(subPathDir);
                            if(!dir.exists()){
                                dir.mkdir();
                                m_Log.info("creamos en directorio porque no existe");
                            }
                            subpathdir = true;
                        }catch(Exception e){
                            m_Log.error("error creando el directorio :"+ e.getMessage());
                            request.setAttribute(ConstantesDatos.ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO,"SI");
                            opcion = "inicio"; 
                        }
                        
                        
                        if(subpathdir){                           
                            try{
                                // Se crea una carpeta con el sessionid
                                String sessionId = subPathDir + File.separator + session.getId();
                                File sessionDir = new File(sessionId);
                                if(!sessionDir.exists()){
                                    sessionDir.mkdir();
                                }
                                String numExpediente = fichaExpForm.getNumExpediente().replaceAll("/","-");
                                String dirExp = sessionId + File.separator + numExpediente;
                                
                                File fDirExp = new File(dirExp);
                                if(!fDirExp.exists()){
                                    fDirExp.mkdir();
                                }
                                
                                String pathFicheroFinal = null;
                                if(!documentoEscaneado){
                                    nombreFichero = fichaForm.getFichero().getFileName();
                                    
                                    contenido = fichaForm.getFichero().getFileData(); 
                                    
                                }
                                
                                if(codTramite==null || ocurrenciaTramite==null || "".equals(codTramite) || "".equals(ocurrenciaTramite) || "null".equals(codTramite) || "null".equals(ocurrenciaTramite))
                                    pathFicheroFinal = dirExp + File.separator + codigo + "_" + nombreFichero;
                                else
                                    pathFicheroFinal = dirExp + File.separator + codigo + "_" + codTramite + "_" + ocurrenciaTramite + "_" + nombreFichero;
                                
                                m_Log.info("el path final del fichero es :" +pathFicheroFinal);
                                FileOperations.writeFile(pathFicheroFinal,contenido);
                                request.setAttribute("RUTA_FICHERO_SUBIDO_SERVIDOR",pathFicheroFinal);
                                 // Se establece la suma total de los documentos subidos y se da de alta el documento
                                m_Log.debug("La suma: " + suma + " no excede el limite fijado para registro de " + tamGlobalExpedientes);
                                request.setAttribute(ConstantesDatos.EXCEED_LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES,"no");                
                                opcion = "cargarFicheroDatosSuplementarios";
                                
                            }catch(Exception e){
                                m_Log.error("error obtenido contenido o escribiendo el fichero :"+ e.getMessage() + ","  + e.getCause());
                                request.setAttribute(ConstantesDatos.ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO,"SI");
                                opcion = "inicio"; 
                            }                            
                        }
                   }
                }
            }
       }
       
       // Se guarda este atributo en la request para que desde ocultoDatosSuplementariosFichero.jsp sepa de donde viene
       request.setAttribute("desdeTramitacionExpedientes","si");
      
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }   
    else if("eliminar".equals(opcion))
    {        
        String cod = request.getParameter("codigo");        
        request.setAttribute("codigo_fichero_eliminado",cod);
        
        GeneralValueObject lFicheros = fichaExpForm.getListaFicheros();
        lFicheros.setAtributo(cod,null);
        GeneralValueObject lTiposFicheros = fichaExpForm.getListaTiposFicheros();
        lTiposFicheros.setAtributo(cod,null);
        GeneralValueObject lNombreFicheros = fichaExpForm.getListaNombreFicheros();
        lNombreFicheros.setAtributo(cod,null);        
        
        opcion = "eliminarFicheroDatoSuplementario";        
    } else if ("crearCSV".equals(opcion)) {
        CampoSuplementarioFicheroVO campo = null;
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        ResultadoAjax<CampoSuplementarioFicheroVO> respuesta = new ResultadoAjax<CampoSuplementarioFicheroVO>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_OK);
        
        try {
            // Obtenemos el tipo de formulario en funcion del origen EXPEDIENTE/TRAMITE
            Map<String, Object> parametros = obtenerParametrosCrearCSV(request, session);
            
            // Se comprueba si ya existe el metadato CSV
            if (!existeDocumentoMetadatoCSV(parametros)) {
                // Se obtiene el fichero del documento
                //Se indica si al convertir el documento se desea que sea PDF o PDFA y ese valor se almacena en el fichero documentos.properties
                ResourceBundle configDocuemntos = ResourceBundle.getBundle("documentos");
                String propiedad = usuario.getOrgCod()+"/DATOS_SUPLEMENTARIOS/FORMATO_PDFA";
                String pdfA = configDocuemntos.getString(propiedad);
                byte[] ficheroDoc = obtieneFicheroPdfCSV(parametros, pdfA);

                if (ficheroDoc != null && ficheroDoc.length > 0) {
                    outputStream = new ByteArrayOutputStream();
                    inputStream = new ByteArrayInputStream(ficheroDoc);

                    String codigoCSV = "";
                    boolean generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(usuario.getOrgCod(), registroConf);

                    if (generarCSV) {
                        // Incrustar el CSV en el pdf
                        String cabeceraCSV = ""; // TODO implementar cabecera
                        codigoCSV = DocumentoManager.getInstance().incrustarCSVenPDF(inputStream, outputStream, cabeceraCSV, usuario.getNombreUsu(), String.valueOf(usuario.getOrgCod()));
                    } else {
                        IOUtils.copy(inputStream,outputStream);
                    }
                    
                    // Almacenar el documento en el servidor y actualizar los datos del form
                    String nombreFichero = almacenarDocumentoCSV(parametros, outputStream, codigoCSV);
                                                            
                    campo = new CampoSuplementarioFicheroVO();
                    campo.setCodigoCampo(codigo);
                    campo.setNombreFichero(nombreFichero);
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

        respuesta.setResultado(campo);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);
        return null;
    } else{
      opcion = mapping.getInput();
    }
    
    
    m_Log.debug("perform");         
        
    // Cuando se excede el tamaño del fichero a subir por POST => 
    // el parámetro opcion está vacío
    if(opcion==null || opcion.length()==0){
        m_Log.debug("opcion nula o vacía => Se exce el tamaño del fichero");
        // Se indica que hay error y se vuelve a la página que
        // permite dar de alta un nuevo documento        
        //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);                           
        int num = Math.round((Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES)/ConstantesDatos.DIVISOR_BYTES);
        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE,num);
        request.setAttribute("ERROR_FILESIZE_UPLOAD","si");
        // Se guarda el código en la request porque cuando se sube un fichero con tamaño superior
        // al indicado por configuración, este parámetro se pierde y hay que guardarlo para que
        // desde la página ocultoDatosSuplementariosFichero.jsp se pueda recuperar y pasarlo a 
        // la jsp altaRE.jsp
        request.setAttribute("CODIGO_DOC_REGISTRO_ACTION",codigo);               
        opcion = "inicio";        
    }
    
    return (mapping.findForward(opcion));
  }
  
    private boolean existeDocumentoMetadatoCSV(Map<String, Object> parametros) throws TechnicalException {
        boolean existe = false;

        String codigoCampo = (String) parametros.get("codigo");
        String origen = (String) parametros.get("origen");
        Integer codMunicipio = (Integer) parametros.get("codMunicipio");
        Integer ejercicio = (Integer) parametros.get("ejercicio");
        String numExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = (Integer) parametros.get("codTramite");
        Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
        MetadatosDocumentoVO metadatosSinGrabar = (MetadatosDocumentoVO) parametros.get("metadatosSinGrabar");
        UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");
        
        // Primero se comprueba si existe el objecto metadato en el formulario.
        // Este objeto solo se genera cuando se sube un nuevo documento o se genera el CSV.
        // Si no existe dicho objeto hay que comprobar en base de datos, ya que se trata de un documento
        // que ya existe en base de datos
        if (metadatosSinGrabar != null) {
            if (StringUtils.isNotEmpty(metadatosSinGrabar.getCsv())) {
                existe = true;
            }
        } else {
            if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE.equalsIgnoreCase(origen)) {
                existe = DocumentoManager.getInstance().existeMetadatoCSV(usuario.getParamsCon(), ConstantesDatos.SUBCONSULTA_E_TFI_ID_METADATO_PK,
                        codigoCampo,
                        ejercicio,
                        codMunicipio,
                        numExpediente);
            } else if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE.equalsIgnoreCase(origen)) {
                String sufijoCodigoFicheroTramite = String.format("_%d", ocurrenciaTramite);
                String codigoCampoReal = StringUtils.removeEnd(codigoCampo, sufijoCodigoFicheroTramite);
                
                existe = DocumentoManager.getInstance().existeMetadatoCSV(usuario.getParamsCon(), ConstantesDatos.SUBCONSULTA_E_TFIT_ID_METADATO_PK,
                        codigoCampoReal,
                        ejercicio,
                        codMunicipio,
                        numExpediente,
                        codTramite,
                        ocurrenciaTramite);
            }
        }
        
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(String.format("Existe metadato?: %b", existe));
        }

        return existe;
    }
    
    private byte[] obtieneFicheroPdfCSV(Map<String, Object> parametros, String pdfA)
            throws TechnicalException, FormatNotSupportedException, Exception {
        byte[] documentoConvertido = null;

        m_Log.debug("obtieneFicheroPdfCSV");
        
        // Descargar fichero
        byte[] ficheroDoc = descargarFichero(parametros);

        if (ficheroDoc != null && ficheroDoc.length > 0) {
            try {
                String tipoMimeOriginal = (String) parametros.get("tipoMimeOriginal");
                if (!MimeTypes.PDF[0].equals(tipoMimeOriginal)) {
                    // Convertir fichero a PDF
                    String rutaTemporalCSV = getRutaTemporalFicherosCSV(parametros);
                    documentoConvertido = JODConverterHelper.convertirDocumentoAPdf(rutaTemporalCSV, ficheroDoc, tipoMimeOriginal, pdfA);
                } else {
                    documentoConvertido = ficheroDoc;
                }
            } catch (DocumentConversionException dce) {
                throw new TechnicalException("Error al intentar convertir el fichero a pdf", dce);
            }
        } else {
            throw new TechnicalException("El fichero esta vacio");
        }

        return documentoConvertido;
    }
    
    private byte[] descargarFichero(Map<String, Object> parametros) throws TechnicalException {
        byte[] fichero = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        m_Log.debug("descargarFichero");
        
        try {
            String codigo = (String) parametros.get("codigo");
            String origen = (String) parametros.get("origen");
            Boolean isExpHistorico = (Boolean) parametros.get("isExpHistorico");
            String nombreFichero = (String) parametros.get("nombreFichero");
            String contextPath = (String) parametros.get("contextPath");
            String sessionId = (String) parametros.get("sessionId");
            
            Config configCommon = ConfigServiceHelper.getConfig("common");
            Config configDoc = ConfigServiceHelper.getConfig("documentos");
            String url = String.format("%s%s%s",
                    configCommon.getString("hostVirtual"),
                    contextPath,
                    configDoc.getString("CSV/URI/SERVLET_VER_DOCUMENTO"));

            Map<String, Object> paramsDescargarFichero = new LinkedHashMap<String, Object>();
            paramsDescargarFichero.put("codigo", codigo);
            paramsDescargarFichero.put("nombreFich", nombreFichero);
            if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE.equalsIgnoreCase(origen)) {
                paramsDescargarFichero.put("opcion", "0");
                paramsDescargarFichero.put("expHistorico", isExpHistorico);
            } else if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE.equalsIgnoreCase(origen)) {
                paramsDescargarFichero.put("opcion", "1");
            }

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
    
    private String getRutaTemporalFicherosCSV(Map<String, Object> parametros) throws TechnicalException {
        StringBuilder path = new StringBuilder();

        m_Log.debug("getRutaTemporalFicherosCSV");
        
        try {
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            String rutaBase = config.getString("RUTA_DISCO_DOCUMENTOS");
            String sessionId = (String) parametros.get("sessionId");
            String numExpediente = (String) parametros.get("numExpediente");
            numExpediente = numExpediente.replaceAll("/", "-");

            path.append(rutaBase)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE)
                .append(File.separator)
                .append(sessionId)
                .append(File.separator)
                .append(numExpediente)
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
    
        private String almacenarDocumentoCSV(Map<String, Object> parametros,
                ByteArrayOutputStream outputStream, String codigoCSV)
            throws TechnicalException {
        
        String nombreNuevo = null;
        
        m_Log.debug("almacenarDocumentoCSVExpediente");

        try {
            String codigo = (String) parametros.get("codigo");
            byte[] fichero = outputStream.toByteArray();
            
            // Nombre del fichero
            String nombreAntiguo = (String) parametros.get("nombreFichero");
            nombreNuevo = String.format("%s.%s", FilenameUtils.removeExtension(nombreAntiguo), MimeTypes.FILEEXTENSION_PDF);
            parametros.put("nombreFicheroNuevo", nombreNuevo);
            
            // Ruta del fichero en disco
            String rutaNueva = null;
            String rutaAntigua = (String) parametros.get("rutaFicheroDisco");
            if (StringUtils.isEmpty(rutaAntigua)) {
                rutaNueva = getServerTempFileName(parametros, nombreNuevo);
            } else {
                rutaNueva = rutaAntigua;
            }

            // Estado del documento
            Integer estadoNuevo = ConstantesDatos.ESTADO_DOCUMENTO_NUEVO;

            // Tamaño del fichero
            Integer longitudNueva = new Integer(fichero.length);

            // Tipo del fichero
            String tipoMimeNuevo = MimeTypes.PDF[0];
            
            // Metadatos del documento
            MetadatosDocumentoVO metadatosNuevo = new MetadatosDocumentoVO();
            metadatosNuevo.setCsv(codigoCSV);
            metadatosNuevo.setCsvAplicacion(ConstantesDatos.APLICACION_ORIGEN_DOCUMENTO_FLEXIA);
            metadatosNuevo.setCsvUri(generarUriCsvDescargaFichero(parametros));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("nombreAntiguo: %s", nombreAntiguo));
                m_Log.debug(String.format("nombreNuevo: %s", nombreNuevo));
                m_Log.debug(String.format("rutaAntigua: %s", rutaAntigua));
                m_Log.debug(String.format("rutaNueva: %s", rutaNueva));
                m_Log.debug(String.format("estadoNuevo: %d", estadoNuevo));
                m_Log.debug(String.format("longitudNueva: %d", longitudNueva));
                m_Log.debug(String.format("tipoMimeNuevo: %s", tipoMimeNuevo));
                m_Log.debug(String.format("metadatosNuevo: %s", metadatosNuevo));
            }
            
            // Grabar en disco del servidor
             FileUtils.writeByteArrayToFile(new File(rutaNueva), fichero);
            
            // Actualizar las listas del formulario dependiendo del tipo que se pasa como parametro
            ActionForm formulario = (ActionForm) parametros.get("formularioOperacion");
            GeneralValueObject listaNombreFicheros = null;
            GeneralValueObject listaRutaFicherosDisco = null;
            GeneralValueObject listaEstadosFicheros = null;
            GeneralValueObject listaLongitudesFicheros = null;
            GeneralValueObject listaTiposFicheros = null;
            GeneralValueObject listaMetadatosFicheros = null;

            if (formulario instanceof FichaExpedienteForm) {
                FichaExpedienteForm form = (FichaExpedienteForm) formulario;
                
                listaNombreFicheros = form.getListaNombreFicheros();
                listaRutaFicherosDisco = form.getListaRutaFicherosDisco();
                listaEstadosFicheros = form.getListaEstadoFicheros();
                listaLongitudesFicheros = form.getListaLongitudFicherosDisco();
                listaTiposFicheros = form.getListaTiposFicheros();
                listaMetadatosFicheros = form.getListaMetadatosFicheros();
            } else if (formulario instanceof TramitacionExpedientesForm) {
                TramitacionExpedientesForm form = (TramitacionExpedientesForm) formulario;

                listaNombreFicheros = form.getListaNombresFicheros();
                listaRutaFicherosDisco = form.getListaRutaFicherosDisco();
                listaEstadosFicheros = form.getListaEstadoFicheros();
                listaLongitudesFicheros = form.getListaLongitudFicheros();
                listaTiposFicheros = form.getListaTiposFicheros();
                listaMetadatosFicheros = form.getListaMetadatosFicheros();
            } else {
                throw new TechnicalException("Formulario no valido");
            }
            
            listaNombreFicheros.setAtributo(codigo, nombreNuevo);
            listaRutaFicherosDisco.setAtributo(codigo, rutaNueva);
            listaEstadosFicheros.setAtributo(codigo, estadoNuevo);
            listaLongitudesFicheros.setAtributo(codigo, longitudNueva);
            listaTiposFicheros.setAtributo(codigo, tipoMimeNuevo);
            listaMetadatosFicheros.setAtributo(codigo, metadatosNuevo);
        } catch (IOException ioe) {
            throw new TechnicalException("Error al intentar grabar el fichero a disco", ioe);
        }
        
        return nombreNuevo;
    }
    
    private String getServerTempFileName(Map<String, Object> parametros, String nombreFichero)
            throws TechnicalException {
        
        m_Log.debug("getServerTempFileName");
        
        StringBuilder path = new StringBuilder();
        String codigoCampo = (String) parametros.get("codigo");
        Integer codTramite = (Integer) parametros.get("codTramite");
        Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
        String sessionId = (String) parametros.get("sessionId");
        String numExpediente = (String) parametros.get("numExpediente");
        numExpediente = numExpediente.replaceAll("/", "-");
        
        try {
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            path.append(config.getString("RUTA_DISCO_DOCUMENTOS"))
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE)
                .append(File.separator)
                .append(sessionId)
                .append(File.separator)
                .append(numExpediente)
                .append(File.separator)
                .append(codigoCampo);
            
            if (codTramite != null && ocurrenciaTramite != null) {
                path.append("_")
                    .append(codTramite)
                    .append("_")
                    .append(ocurrenciaTramite); 
            }
            
            path.append("_")
                .append(nombreFichero);
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("Ruta del fichero en servidor: %s", path.toString()));
            }
        } catch (Exception e) {
            throw new TechnicalException("Error al obtener la propiedad que indica la ruta en la que se almacenan los documentos en disco: " + e);
        }

        return path.toString();
    }
    
    private Map<String, Object> obtenerParametrosCrearCSV(HttpServletRequest request, HttpSession session) throws TechnicalException {
        Map<String, Object> parametros = new HashMap<String, Object>();

        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String sessionId = request.getSession().getId();
        String codigoCampo = request.getParameter("codigo");
        String origen = request.getParameter("origen");
        String contextPath = request.getContextPath();
        ActionForm formularioOperacion = null;
        Integer ejercicio = null;
        Integer codMunicipio = null; 
        String numExpediente = null;
        Integer codTramite = null;
        Integer ocurrenciaTramite = null;
        Boolean isExpHistorico = null;
        String tipoMimeOriginal = null;
        String nombreFichero = null;
        String rutaFicheroDisco = null;
        MetadatosDocumentoVO metadatosSinGrabar = null;
        
        FichaExpedienteForm fichaExpForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
        TramitacionExpedientesForm tramExpForm = (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");
                    
        if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE.equalsIgnoreCase(origen)) {
            m_Log.debug("Origen de tipo expediente");
            
            formularioOperacion = fichaExpForm;
            metadatosSinGrabar = (MetadatosDocumentoVO) fichaExpForm.getListaMetadatosFicheros().getAtributoONulo(codigoCampo);
            ejercicio = NumberUtils.createInteger(fichaExpForm.getEjercicio());
            codMunicipio = NumberUtils.createInteger(fichaExpForm.getCodMunicipio());
            numExpediente = fichaExpForm.getNumExpediente();
            isExpHistorico = fichaExpForm.isExpHistorico();
            nombreFichero = (String) fichaExpForm.getListaNombreFicheros().getAtributo(codigoCampo);
            tipoMimeOriginal = (String) fichaExpForm.getListaTiposFicheros().getAtributo(codigoCampo);
            rutaFicheroDisco = (String) fichaExpForm.getListaRutaFicherosDisco().getAtributo(codigoCampo);
        } else if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE.equalsIgnoreCase(origen)) {
            m_Log.debug("Origen de tipo tramite");
            
            formularioOperacion = tramExpForm;
            metadatosSinGrabar = (MetadatosDocumentoVO) tramExpForm.getListaMetadatosFicheros().getAtributoONulo(codigoCampo);
            ejercicio = NumberUtils.createInteger(tramExpForm.getEjercicio());
            codMunicipio = NumberUtils.createInteger(tramExpForm.getCodMunicipio());
            numExpediente = tramExpForm.getNumeroExpediente();
            codTramite = NumberUtils.createInteger(request.getParameter("codTramite"));
            ocurrenciaTramite = NumberUtils.createInteger(request.getParameter("ocurrenciaTramite"));
            isExpHistorico = fichaExpForm.isExpHistorico();
            
            // Si el fichero esta en base de datos el nombre no viene en el formulario, por lo que se recoge de la request
            nombreFichero = request.getParameter("nombreFicheroTramite");
            tipoMimeOriginal = (String) tramExpForm.getListaTiposFicheros().getAtributo(codigoCampo);
            rutaFicheroDisco = (String) tramExpForm.getListaRutaFicherosDisco().getAtributo(codigoCampo);
        } else {
            throw new TechnicalException(String.format("El origen no es valido: %s", origen));
        }

        // Asignamos los valores al mapa
        parametros.put("usuario", usuario);
        parametros.put("sessionId", sessionId);
        parametros.put("contextPath", contextPath);
        parametros.put("codigo", codigoCampo);
        parametros.put("origen", origen);
        parametros.put("codMunicipio", codMunicipio);
        parametros.put("ejercicio", ejercicio);
        parametros.put("numExpediente", numExpediente);
        parametros.put("codTramite", codTramite);
        parametros.put("ocurrenciaTramite", ocurrenciaTramite);
        parametros.put("isExpHistorico", isExpHistorico);
        parametros.put("nombreFichero", nombreFichero);
        parametros.put("tipoMimeOriginal", tipoMimeOriginal);
        parametros.put("rutaFicheroDisco", rutaFicheroDisco);
        parametros.put("metadatosSinGrabar", metadatosSinGrabar);
        parametros.put("formularioOperacion", formularioOperacion);

        return parametros;
    }

    private String generarUriCsvDescargaFichero(Map<String, Object> parametros) {
        DocumentoManager documentoManager = DocumentoManager.getInstance();
        Map<String, Object> paramUrlCsv = new HashMap<String, Object>();
        
        String origen = (String) parametros.get("origen");
        
        if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE.equalsIgnoreCase(origen)) {
            paramUrlCsv.put("codigo", parametros.get("codigo"));
            paramUrlCsv.put("nombreFich", parametros.get("nombreFicheroNuevo"));
            paramUrlCsv.put("opcion", "0");
            paramUrlCsv.put("expHistorico", parametros.get("isExpHistorico"));
            paramUrlCsv.put("codMunicipio", parametros.get("codMunicipio"));
            paramUrlCsv.put("ejercicio", parametros.get("ejercicio"));
            paramUrlCsv.put("numExpediente", parametros.get("numExpediente"));
        } else if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE.equalsIgnoreCase(origen)) {
            paramUrlCsv.put("codigo", parametros.get("codigo"));
            paramUrlCsv.put("nombreFich", parametros.get("nombreFicheroNuevo"));
            paramUrlCsv.put("opcion", "1");
            paramUrlCsv.put("codMunicipio", parametros.get("codMunicipio"));
            paramUrlCsv.put("ejercicio", parametros.get("ejercicio"));
            paramUrlCsv.put("numExpediente", parametros.get("numExpediente"));
            paramUrlCsv.put("codTramite", parametros.get("codTramite"));
            paramUrlCsv.put("ocurrenciaTramite", parametros.get("ocurrenciaTramite"));
        }
        
        return documentoManager.crearURLCodigoSeguroVerificacion(paramUrlCsv);
    }
}
