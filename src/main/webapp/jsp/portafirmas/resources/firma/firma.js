/* Constantes internacionalización */
    var ERROR_MODO_CERTIFICADO_NO_VALIDO    = "Modo de operación no válido";
    var MENSAJE_INICIANDO_PROCESO_FIRMA_URL = "Iniciando proceso de firma de fichero remoto";

/* Constantes */
    var MODO_CERTIFICADO_LISTACA = 0;
    var MODO_CERTIFICADO_NIF     = 1;
    var MODO_CERTIFICADO_NSERIE  = 2;
    var DEFAULT_DIRECTORIO_TEMPORAL = "/";
    var DEFAULT_FICHERO_TEMPORAL = "tempFile";
    var DEFAULT_CA = "FNMT Clase 2 CA";
    //  var DEFAULT_CA = "Altia Consultores CA ( PRUEBAS )";

/* Atributos */
    var caActiva = DEFAULT_CA;
  
  
/* Funciones */
    /*
     * firmarURL
     *
     * Firma en local un fichero remoto usando el applet de firma de Altia
     * Parámetros:
     *  - [url:String] la URL de donde debe descargarse el fichero
     *  - [modoCertificado:int] 0-lista certificados CA, 1-NIF, 2-Numero Serie
     *  - [idCertificadoFirmante:String] null, NIF o número de serie (según sea modoCertificado)
     *  - [formulario:Form] Ref al formulario donde están los campos requeridos en esta función
     *            - dirtFichero: (ENTRADA) campo con el nombre del directorio temporal
     *            - nameFichero: (ENTRADA) campo con el nombre del fichero temporal
     *            - fichero: (SALIDA) campo en el que se guarda el fichero firmado ???
     *            - firma: (SALIDA) campo en el que se guarda la firma en base 64
     *            - appletFirmaHayError: (SALIDA) campo en el que se guarda "S" o "N" dependiendo de si hubo errores
     *  - [ca:String] DN de la CA de los certificados de firma o null si se quiere el valor por defecto
     *
     * Devuelve:
     *  - [boolean] true si fue bien, false si algo fue mal
     */
    function firmarURL(url, modoCertificado, idCertificadoFirmante, formulario, ca) { 
        if (ca) caActiva = ca;

        /* Inicialización applet */
        var applet = document.applets.APLFIRMA;
        // if (caActiva != null) applet.setCA(caActiva);
        if (caActiva != null) applet.setListaCA(0);
        switch(modoCertificado) {
            case 0:
                applet.setModoSeleccionCertificado(applet.CERTIFICADO_LISTA, null);
                break;
            case 1:
                applet.setModoSeleccionCertificado(applet.CERTIFICADO_NIF, idCertificadoFirmante);
                break;
            case 2:
                applet.setModoSeleccionCertificado(applet.CERTIFICADO_NUMEROSERIE, idCertificadoFirmante);
                break;
            default:
                formulario.appletFirmaHayError.value = "S";
                formulario.appletFirmaCodigoError.value = 2;
                formulario.appletFirmaMensajeError.value = ERROR_MODO_CERTIFICADO_NO_VALIDO;
                return;
        }

        /* Obtener nombre fichero temporal */
        var directorio = DEFAULT_DIRECTORIO_TEMPORAL;
        var fichero = DEFAULT_FICHERO_TEMPORAL;
        
        if ( (formulario) && (formulario.dirtFichero) && (formulario.nameFichero) &&
             (formulario.dirtFichero.value!='') && (formulario.nameFichero.value!='') ) {
            directorio = formulario.dirtFichero.value;
            fichero = formulario.nameFichero.value;
        }

                
        /* Firma */
        // applet.abreVentanaEstado(MENSAJE_INICIANDO_PROCESO_FIRMA_URL);
        fichero = directorio + fichero; // Para el applet de Sun ya que no tiene en cuenta el directorio al crear el fichero temporal

        var res = applet.firmaFicheroURL(url,directorio,fichero);
        //applet.cierraVentanaEstado();

        /* Guardar resultados */
        formulario.fichero.value = res;
        if (res) {
            formulario.firma.value = applet.getFirma();
            formulario.appletFirmaHayError.value = "N";
            return true;
        } else {
            formulario.firma.value = null;
            formulario.appletFirmaHayError.value = "S";
            formulario.appletFirmaCodigoError.value = applet.getCodigoError();
            formulario.appletFirmaMensajeError.value = applet.getMensajeError();
            return false;
        }
    }

    function cargarAppletDescarga () {
        
        var applet;
	
        applet				= document.createElement("OBJECT");
	applet.id		= "APLDESCARGA";
	applet.height	= 1;
	applet.width	= 1;
        applet.hspace = 100;
        applet.vspace = 100;
        applet.align = "middle";
	applet.code		= "es.altia.flexia.applet.AppletDescargaFichero.class";
        appendParam (applet,"IDIOMA","E");
        appendParam (applet,"java_codebase", DOMAIN_NAME+ "/jsp/portafirmas/resources/firma");
        appendParam (applet,"java_archive","AppletDescargaDocumento.jar");
        appendParam (applet,"separate_jvm","true");
        appendParam (applet,"type","application/x-java-applet");alert ('applet descarga' );
	document.getElementsByTagName('body')[0].appendChild(applet);        
        
	return applet;
    }
    
    function cargarAppletFirmaSLE () {
        
         var applet;
	
        applet				= document.createElement("OBJECT");
	applet.id		= "APLFIRMANUEVO";
        applet.hspace = 100;
        applet.vspace = 100;
	applet.height	= 1;
	applet.width	= 1;
        applet.align = "middle";
	applet.code		= "es.altia.applets.firma.Applet.class";
        appendParam (applet,"IDIOMA","E");
        appendParam (applet,"scriptable","true");
        appendParam (applet,"mayscript","false");
        appendParam (applet,"separate_jvm","true");
        appendParam (applet,"java_codebase", DOMAIN_NAME+ "/jsp/portafirmas/resources/firma");
        appendParam (applet,"java_archive","AppletFirmaSLE.jar");
        appendParam (applet,"type","application/x-java-applet");        
	document.getElementsByTagName('body')[0].appendChild(applet);
	return applet;
    }
    
    
    function appendParam(parent, name, value)
{
	var param 	= document.createElement("param");
	param.name	= name;
	param.value	= value;
	parent.appendChild(param);
	
	return param;
}