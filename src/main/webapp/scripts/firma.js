/* Constantes internacionalización */
var ERROR_MODO_CERTIFICADO_NO_VALIDO    = "Modo de operación no válido";
var MENSAJE_INICIANDO_PROCESO_FIRMA_URL = "Iniciando proceso de firma de fichero remoto";

/* Constantes */
var MODO_CERTIFICADO_LISTACA = 0;
var MODO_CERTIFICADO_NIF     = 1;
var MODO_CERTIFICADO_NSERIE  = 2;
var DEFAULT_FICHERO_TEMPORAL = "tempFile";
var DEFAULT_CA = "FNMT Clase 2 CA";
//  var DEFAULT_CA = "Altia Consultores CA ( PRUEBAS )";

/* Atributos */
var caActiva = DEFAULT_CA;


/* Funciones */
  


/**
      * Llama al applet de descarga para descargar el documento del servidor y almacenarlo al directorio
      * temporal del usuario para que pueda ser firmado
      * [Parámetros]:
      *  - [String] url de la que se descarga el documento
      * Devuelve:
      *  - [String] con la ruta temporal del fichero que será pasada al applet de firma o null si no se ha
      *    podido descargar el fichero.
      */
function descargarFichero(url) {    
        var applet = document.getElementById("APLDESCARGA");
        
    /* Obtener nombre fichero temporal */
        var directorio = applet.getDirTmp();        
    var fichero = DEFAULT_FICHERO_TEMPORAL;
                
    /** Se descarga el documento al directorio personal del usuario */
   fichero = directorio + fichero; // Para el applet de Sun ya que no tiene en cuenta el directorio al crear el fichero temporal
    var resultado = applet.descargarFichero(url,fichero);
        
        if(resultado){
        return fichero;
    }else
        return null;
}


/**
      * Permite firmar un fichero
      * [Parámetros]:
      *  - [ruta: String] Ruta al fichero a firmar en el directorio temporal del usuario
      *  - [formulario: Form] Formulario en el que se encuentran los campos necesarios en esta función
      * Devuelve:
      *  - [Boolean] Indica si la operación se ha realizado con éxito
      */
function firmarFichero(ruta,formulario){
    var applet = document.getElementsByName("APLFIRMANUEVO")[0];
    // Se firma el fichero
    var resultado = null;
    
    if (formulario.firmaBase64.value==""){
        resultado = applet.firmaFichero(ruta);  
    } else {
        resultado = applet.coFirmaFichero(ruta,formulario.firmaBase64.value);  
    }
                        
    /* Guardar resultados */
    formulario.fichero.value = resultado;
    if (resultado) {
        formulario.firma.value = applet.getFirmaBase64();            
        formulario.appletFirmaHayError.value = "N";        
        return true;
    } else {
        formulario.firma.value = null;
        formulario.appletFirmaHayError.value = "S";
        formulario.appletFirmaCodigoError.value = applet.getEstado();
        formulario.appletFirmaMensajeError.value = applet.getMensajeError();            
        return false;
    }
}

function firmarTexto(texto,formulario){
    var applet = document.getElementsByName("APLFIRMANUEVO")[0];
    // Se firma el fichero
    var resultado = applet.firmaTexto(texto);

    /* Guardar resultados */
    formulario.fichero.value = resultado;
    if (resultado) {
        formulario.firma.value = applet.getFirmaBase64();
        formulario.appletFirmaHayError.value = "N";
        return true;
    } else {
        formulario.firma.value = null;
        formulario.appletFirmaHayError.value = "S";
        formulario.appletFirmaCodigoError.value = applet.getEstado();
        formulario.appletFirmaMensajeError.value = applet.getMensajeError();
        return false;
    }
    }


function firmarFicheroAFirma(ruta,formulario){
    
    try {
        netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
    } catch (e) {
        alert('Imposible el acceso a ficheros locales debido a la configuracion de seguridad del navegador. Para solucionarlo, siga los siguentes pasos: (1) Introduzca "about:config" en el campo URL del navegador; (2) Click con boton derecho y seleccione New->Boolean; (3) Introduzca "signed.applets.codebase_principal_support" (sin las comillas) como nuevo nombre; (4) Click OK y intente cargar el fichero de nuevo.');
        return;
    }
	
    clienteFirma.initialize();
    clienteFirma.setFileuri(ruta);
    clienteFirma.setShowErrors(true);
    
    if (formulario.firmaBase64.value==""){
        clienteFirma.sign();
    } else {
        clienteFirma.setElectronicSignature(formulario.firmaBase64.value);
        clienteFirma.coSign();
    }
    
    if (!clienteFirma.isError()) {
        //clienteFirma.saveSignToFile();
        
        formulario.firma.value = clienteFirma.getSignatureBase64Encoded();
	formulario.hashB64.value = clienteFirma.getFileHashBase64Encoded(false);

        //var certB64 = document.getElementById("certFirma");
        //certB64.value = clienteFirma.getSignCertificate();
	    
        return true;
    } else {
        alert("No se ha podido firmar: " + clienteFirma.getErrorMessage());
        return false;
    }

}