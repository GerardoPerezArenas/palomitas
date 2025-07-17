<%@ page import="es.altia.agora.business.sge.MetadatosDocumentoVO"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DatosSuplementariosFicheroForm"%>
<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaRelacionExpedientesForm"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>

<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
  <script type="text/javascript">

	// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        <%
        String DOT = ".";
       // Config m_ConfigExpediente = ConfigServiceHelper.getConfig("Expediente");
       // String  = m_ConfigExpediente.getString();        
        DatosSuplementariosFicheroForm fichaForm = (DatosSuplementariosFicheroForm)session.getAttribute("DatosSuplementariosFicheroForm");
        Log _log = LogFactory.getLog(this.getClass());
        String path = fichaForm.getPath();
        String nombre;
        String[] matriz = path.split("/");
        nombre = matriz[matriz.length-1];               
        
        int pos = nombre.lastIndexOf(".");
        String extension = nombre.substring(pos +1,nombre.length());        
                              
        String imageType    = "image/jpeg";
        String codigo       = fichaForm.getCodigo();
        FormFile fichero    = fichaForm.getFichero();        
        String docEscaneado = fichaForm.getDocEscaneado();
        _log.debug(" ocultoDatos codigo: " + codigo);
        
        
        // Indica si lo que se pretende es guardar el fichero recien subido en el formulario TramitacionExpedientesForm
        String desdeTramitacionExpedientes = (String)request.getAttribute("desdeTramitacionExpedientes");

        _log.debug("oculto desdeTramitacionExpedientes: " + desdeTramitacionExpedientes);
        if (session.getAttribute("FichaExpedienteForm") != null){
            FichaExpedienteForm expForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");        
            GeneralValueObject listaFicheros = expForm.getListaFicheros();
            Hashtable tabla = listaFicheros.getTabla();


            // El documento se sube por post
            if(fichero!=null && fichero.getFileSize()>=1 && fichero.getFileData()!=null && (docEscaneado==null || docEscaneado.length()==0))
            {                                            
                _log.debug(">>>>>>> listaFicheros.toString() " + listaFicheros.toString());                
                _log.debug("ocultoDatosSuplementariosFichero.jsp: DOC SUBIDO POR POST");
                _log.debug("ocultoDatosSuplementariosFichero.jsp - fileData: " + fichero.getFileData());
                _log.debug("ocultoDatosSuplementariosFichero.jsp - contentType: " + fichero.getContentType());
                _log.debug("ocultoDatosSuplementariosFichero.jsp - fileName: " + fichero.getFileName());
               

                GeneralValueObject listaTiposFicheros = expForm.getListaTiposFicheros();
                listaTiposFicheros.setAtributo(codigo,fichero.getContentType());
                _log.debug("Content-type: " + fichero.getContentType());
                expForm.setListaTiposFicheros(listaTiposFicheros);                                               
                
                // El nombre del fichero será el título o en su defecto el nombre del fichero 
                // en el equipo cliente
                if(fichaForm!=null && fichaForm.getTituloFichero()!=null && fichaForm.getTituloFichero().length()>=1)
                    nombre = fichaForm.getTituloFichero() + DOT + extension;    
                else
                    nombre = fichero.getFileName();

                // Lista con los nombres de los ficheros
                GeneralValueObject listaNombreFicheros = expForm.getListaNombreFicheros();               
                listaNombreFicheros.setAtributo(codigo,nombre);
                expForm.setListaNombreFicheros(listaNombreFicheros);      
                
                
                // Ruta del fichero en disco
                String rutaFicheroDisco = (String)request.getAttribute("RUTA_FICHERO_SUBIDO_SERVIDOR");
                GeneralValueObject listaRutaFicherosDisco = expForm.getListaRutaFicherosDisco();                
                listaRutaFicherosDisco.setAtributo(codigo,rutaFicheroDisco);
                expForm.setListaRutaFicherosDisco(listaRutaFicherosDisco);      
                
                
                // Estado del documento                
                GeneralValueObject listaEstadosFicheros = expForm.getListaEstadoFicheros();                
                listaEstadosFicheros.setAtributo(codigo,ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
                expForm.setListaEstadoFicheros(listaEstadosFicheros);        

                // Metadatos del documento                
                GeneralValueObject listaMetadatosFicheros = expForm.getListaMetadatosFicheros();                
                listaMetadatosFicheros.setAtributo(codigo, new MetadatosDocumentoVO());
                
                // Se almacena la longitud del fichero recien subido al servidor
                GeneralValueObject listaLongitudesFicheros = expForm.getListaLongitudFicherosDisco();
                listaLongitudesFicheros.setAtributo(codigo,new Integer(fichero.getFileData().length));
                
            }
            else // El documento se ha escaneado
            if(fichero.getFileSize()==0 && docEscaneado!=null && docEscaneado.length()>=1)
            {               
                _log.debug("ocultoDatosSuplementariosFichero.jsp: DOC SCANNER");
                sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();       
                _log.debug("Content-type doc escaner: " + imageType);                       
                _log.debug("Resultado decode BASE64: " + decoder.decodeBuffer(docEscaneado));
                byte[] ficheroEscaneado = decoder.decodeBuffer(docEscaneado);
                //listaFicheros.setAtributo(codigo,decoder.decodeBuffer(docEscaneado));
                listaFicheros.setAtributo(codigo,ficheroEscaneado);
                expForm.setListaFicheros(listaFicheros);
                GeneralValueObject listaTiposFicheros = expForm.getListaTiposFicheros();
                listaTiposFicheros.setAtributo(codigo,imageType);
                expForm.setListaTiposFicheros(listaTiposFicheros);                
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("ddMMyyyy_HHmmss");
                
                // El nombre del fichero será el título o en su defecto se le asigna uno
                // automáticamente
                if(fichaForm!=null && fichaForm.getTituloFichero()!=null && fichaForm.getTituloFichero().length()>=1)
                    nombre = fichaForm.getTituloFichero() + DOT + "jpg"; 
                else
                    nombre = "CODIGO" + formato.format(calendar.getTime()) + ".jpg";                                
                
                // Lista con los nombres de los ficheros
                GeneralValueObject listaNombreFicheros = expForm.getListaNombreFicheros();
                listaNombreFicheros.setAtributo(codigo,nombre);
                expForm.setListaNombreFicheros(listaNombreFicheros); 
                
                
                /************************************/                
                // Ruta del fichero en disco
                String rutaFicheroDisco = (String)request.getAttribute("RUTA_FICHERO_SUBIDO_SERVIDOR");
                GeneralValueObject listaRutaFicherosDisco = expForm.getListaRutaFicherosDisco();                
                listaRutaFicherosDisco.setAtributo(codigo,rutaFicheroDisco);
                expForm.setListaRutaFicherosDisco(listaRutaFicherosDisco);      
                                
                // Estado del documento                
                GeneralValueObject listaEstadosFicheros = expForm.getListaEstadoFicheros();                
                listaEstadosFicheros.setAtributo(codigo,ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
                expForm.setListaEstadoFicheros(listaEstadosFicheros);        

                // Se almacena la longitud del fichero recien subido al servidor
                GeneralValueObject listaLongitudesFicheros = expForm.getListaLongitudFicherosDisco();
                listaLongitudesFicheros.setAtributo(codigo,new Integer(fichero.getFileData().length));
                
                // Metadatos del documento                
                GeneralValueObject listaMetadatosFicheros = expForm.getListaMetadatosFicheros();                
                listaMetadatosFicheros.setAtributo(codigo, new MetadatosDocumentoVO());
                /************************************/
            }
        }
       
        if (session.getAttribute("FichaRelacionExpedientesForm") != null){
            FichaRelacionExpedientesForm fichaRelExpForm = (FichaRelacionExpedientesForm)session.getAttribute("FichaRelacionExpedientesForm");
            if (fichaRelExpForm != null) 
            {
                GeneralValueObject listaFicheros = fichaRelExpForm.getListaFicheros();
                listaFicheros.setAtributo(codigo,fichero.getFileData());
                fichaRelExpForm.setListaFicheros(listaFicheros);
                GeneralValueObject listaTiposFicheros = fichaRelExpForm.getListaTiposFicheros();
                listaTiposFicheros.setAtributo(codigo,fichero.getContentType());
                fichaRelExpForm.setListaTiposFicheros(listaTiposFicheros);
            }
        }

        if (session.getAttribute("TramitacionExpedientesForm") != null
                && "si".equals(desdeTramitacionExpedientes)){ _log.debug("oculto TramitacionExpedientesForm!=null");
            TramitacionExpedientesForm tramExpForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
            if (tramExpForm != null) {

                if (docEscaneado!=null && docEscaneado.length()>=1){
                //El fichero es escaneado
                //Si entra aquí debemos cubriri el form de tramitaicón con los nuevos valores de ficheros.
                //Debemos comprobar si el fichero se ha subido por escaner o POST.
                GeneralValueObject listaFicheros = tramExpForm.getListaFicheros();
                              sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
                _log.debug("Content-type doc escaner: " + imageType);
                _log.debug("Resultado decode BASE64: " + decoder.decodeBuffer(docEscaneado));
                byte[] ficheroEscaneado = decoder.decodeBuffer(docEscaneado);
                listaFicheros.setAtributo(codigo,ficheroEscaneado);
                tramExpForm.setListaFicheros(listaFicheros);

                GeneralValueObject listaTiposFicheros = tramExpForm.getListaTiposFicheros();
                listaTiposFicheros.setAtributo(codigo,imageType);
                tramExpForm.setListaTiposFicheros(listaTiposFicheros);
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("ddMMyyyy_HHmmss");

             
               
                
                
                // El nombre del fichero será el título o en su defecto el nombre del fichero
                // en el equipo cliente
                if(fichaForm!=null && fichaForm.getTituloFichero()!=null && fichaForm.getTituloFichero().length()>=1)
                    nombre = fichaForm.getTituloFichero() + DOT + "jpg";
                else
                    nombre = "CODIGO" + formato.format(calendar.getTime()) + ".jpg";
                    }
                else{
                //Si entra aquí debemos cubriri el form de tramitaicón con los nuevos valores de ficheros.
                //Debemos comprobar si el fichero se ha subido por escaner o POST.
                //GeneralValueObject listaFicheros = tramExpForm.getListaFicheros();
                //listaFicheros.setAtributo(codigo,fichero.getFileData());
                //tramExpForm.setListaFicheros(listaFicheros);

                GeneralValueObject listaTiposFicheros = tramExpForm.getListaTiposFicheros();
                listaTiposFicheros.setAtributo(codigo,fichero.getContentType());
                tramExpForm.setListaTiposFicheros(listaTiposFicheros);
                
                // El nombre del fichero será el título o en su defecto el nombre del fichero 
                // en el equipo cliente
                if(fichaForm!=null && fichaForm.getTituloFichero()!=null && fichaForm.getTituloFichero().length()>=1)
                    nombre = fichaForm.getTituloFichero() + DOT + extension;    
                else
                    nombre = fichero.getFileName();
                }
                
                GeneralValueObject LISTA_LONGITUDES_FICHERO = tramExpForm.getListaLongitudFicheros();                
                GeneralValueObject LISTA_NOMBRES_FICHERO = tramExpForm.getListaNombresFicheros();
                
                // Lista con las longitudes de los archivos
                if(LISTA_LONGITUDES_FICHERO!=null){
                    LISTA_LONGITUDES_FICHERO = new GeneralValueObject();                    
                }
                LISTA_LONGITUDES_FICHERO.setAtributo(codigo,fichero.getFileData().length);                
                tramExpForm.setListaLongitudFicheros(LISTA_LONGITUDES_FICHERO);                                    
                
                
                // Lista con los nombres de los archivos
                if(LISTA_NOMBRES_FICHERO!=null)
                    LISTA_NOMBRES_FICHERO = new GeneralValueObject();
                
                LISTA_NOMBRES_FICHERO.setAtributo(codigo,nombre);
                tramExpForm.setListaNombresFicheros(LISTA_NOMBRES_FICHERO);
                                
                
                try{
                    
                    // Lista con las rutas de los ficheros en disco
                    String rutaFicheroDisco = (String)request.getAttribute("RUTA_FICHERO_SUBIDO_SERVIDOR");
                    GeneralValueObject listaRutaFicherosDisco = tramExpForm.getListaRutaFicherosDisco();                
                    listaRutaFicherosDisco.setAtributo(codigo,rutaFicheroDisco);
                    tramExpForm.setListaRutaFicherosDisco(listaRutaFicherosDisco);      

                    // Lista con el estado de los ficheros
                    GeneralValueObject listaEstadosFicheros = tramExpForm.getListaEstadoFicheros();                
                    listaEstadosFicheros.setAtributo(codigo,ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
                    tramExpForm.setListaEstadoFicheros(listaEstadosFicheros);                      
                    
                    //System.out.println("listaNombresFicheros: " + tramExpForm.getListaNombresFicheros());
                    
                    // Metadatos del documento                
                    GeneralValueObject listaMetadatosFicheros = tramExpForm.getListaMetadatosFicheros();                
                    listaMetadatosFicheros.setAtributo(codigo, new MetadatosDocumentoVO());
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }       
      
       %>
       self.parent.opener.retornoXanelaAuxiliar(["<%=codigo%>","<%=nombre%>"]);
    }
</script>
</head>
<body onload="inicializar();">
</body>
</html>
