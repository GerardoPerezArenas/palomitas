var TIPO_ERROR_ELIMINAR_DOCUMENTO_EXTERNO = 1;
var TIPO_ERROR_CARGAR_LISTADO_DOCUMENTOS_EXTERNOS = 2;
var TIPO_ERROR_CARGAR_LISTADO_NOTIFICACIONES_ELECTRONICAS = 3;
var TIPO_ERROR_CARGAR_LISTADO_COMUNICACIONES_ELECTRONICAS = 4;
var TIPO_ERROR_CARGAR_LISTADO_OTROS_DATOS_EXPEDIENTE = 5;
var TIPO_ERROR_CARGAR_LISTADO_DATOS_SUPLEMENTARIOS = 6;
var TIPO_ERROR_ACTUALIZAR_CAMPOS_NUMERICOS_ALARMAS = 7;
var TIPO_ERROR_TRAMITE_NOTIFICADO_NO_INICIADO = 8;
var TIPO_ERROR_FILA_NO_SELECCIONADA = 9;
var TIPO_OK_CSV_GENERADO_CORRECTO = 100;
var TIPO_ERROR_CREAR_CSV_DOCUMENTO = 101;
var TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE = 110;
var TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO = 111;
var TIPO_ERROR_CSV_CODIGO_DESCONOCIDO = 112;
var ESTILO_CSS_INPUT_NORMAL      = "inputTexto";
var ESTILO_CSS_INPUT_OBLIGATORIO = "inputTextoObligatorio";
 
function validarDocumento(){ 
    var tipo = document.forms[0].codTipoDoc.value;
    var documento = document.forms[0].txtDNI.value;
    if (documento == '') return true;
    if(tipo=="4" || tipo=="5"){
        // Si se trata de un CIF 
        if(!validarCIF(documento)){
            
            if(!validarNifDocumentoTipoCif(documento)){
                
                if(!validarNieDocumentoTipoCif(documento)){
                    jsp_alerta('A',getMensajesError("msjDocIncorrecto"));
                    return false;                    
                }
            }            
        }
        return true;
    }
    if(tipo=="1") return validarNif(document.forms[0].txtDNI);
    // Validamos el pasaporte.
    if (tipo=="2") return true;
    // Validamos la tarjeta de residencia
    if (tipo=="3") {
        var nieCorrecto = validarNie(document.forms[0].txtDNI);
        if (!nieCorrecto)
            jsp_alerta('A',getMensajesError("msjDocIncorrecto"));
        return nieCorrecto;
    }
}//validarDocumento

 

function validarNif(campo) {
    var documento = campo.value;
    var LONGITUD = 9;
    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es menor de la esperada, rellenamos con ceros a la izquierda.
    var ultCaracter = documento.substring(documento.length - 1, documento.length);
    if (isNaN(ultCaracter)) while (documento.length < LONGITUD) documento = "0" + documento;
    else while (documento.length < LONGITUD - 1) documento = "0" + documento;

    if (documento.length > LONGITUD) {
        jsp_alerta('A',getMensajesError("msjDocIncorrecto"));
        campo.value = '';
        campo.focus();
        return false;
    }

    if (documento.length == LONGITUD) {
        var numDocumento = documento.substring(0, 8);
        var letDocumento = documento.substring(8, 9);
    } else {
        var numDocumento = documento;
        var letDocumento = '';
    }

    if (isNaN(numDocumento)) {
        jsp_alerta('A',getMensajesError("msjDocIncorrecto"));
        campo.value = '';
        campo.focus();
        return false;
    }

   
    var letraCorrecta = getLetraNif(numDocumento);
    if (letDocumento == '') {
        jsp_alerta('A',getMensajesError("msjLetraNif") + " " + letraCorrecta);
        campo.value = numDocumento;
        campo.select();
        return false;
    }

    letDocumento=letDocumento.toUpperCase();
    if (letDocumento != letraCorrecta) {
        var res = jsp_alerta('C',getMensajesError("msjLetraNif") + " " + letraCorrecta + "." + getMensajesError("msjPregContinuar"));
        if (!(res > 0)) {
            campo.value = numDocumento + letDocumento;
            campo.select();
            return false;
        }
    }
    campo.value = numDocumento + letDocumento;
    return true;
}//validarNif

function getLetraNif(dni) {
    var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
    return lockup.charAt(dni % 23);
}//getLetraNif

// Ver fecha vencimiento - campoFecha.jsp
function onClickVer(nameCampo, plazo, periodoPlazo, campoActivo){    
    var campo = $('#' + nameCampo).val();
    
    if (campo==null) return;
    //Si no se ha definido una fecha
    //if ((campo.value =='') || (campo.value =="")){ 
    if (campo ==''){ 
        jsp_alerta('A',getMensajesError("msgNoFecha"));
    }else{                   
        if ((plazo=='null')||(plazo=="null")||(plazo==null)){
            jsp_alerta('A',getMensajesError("msgNoPlazo"));
        }else{//se ha definido un plazo
            var parametros ="opcion=verFechaVencimiento"
            parametros += "&fecha=";
            parametros += campo;
            parametros += "&plazo=";
            parametros += plazo;
            parametros += "&periodoPlazo=";
            parametros += periodoPlazo;
            abrirXanelaAuxiliar(getContextPath() + '/jsp/sge/mainVentana.jsp?source='+getContextPath() + '/sge/TramitacionExpedientes.do?' + parametros,null,
            'width=400,height=350',function(rslt){});
        }
    }
}//onClickVer

/**
function onClickDesactivar(idCampo){
    if (idCampo==null)return;
    //var cmdActivar = document.getElementById("cmdActivar"+idCampo);
    //var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
    var cmdActivar = document.getElementById("enlaceActivar"+idCampo);
    var cmdDesactivar = document.getElementById("enlaceDesactivar"+idCampo);
    var campo =  document.getElementById("activar"+idCampo);
    //al pulsar bton desactivar, se oculta el bton activar
    cmdActivar.style.visibility='visible';
    cmdActivar.style.display='inline';
    cmdDesactivar.style.display='none';
    campo.value="desactivada"; 
}//onClickDesactivar

function onClickActivar(idCampo){ 
    if (idCampo==null)return;
    //var cmdActivar = document.getElementById("cmdActivar"+idCampo);
    //var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
    var cmdActivar = document.getElementById("enlaceActivar"+idCampo);
    var cmdDesactivar = document.getElementById("enlaceDesactivar"+idCampo);
    var campo =  document.getElementById("activar"+idCampo);
    //al pulsar bton desactivar, se oculta el bton activar
    cmdDesactivar.style.visibility='visible';
    cmdActivar.style.display='none';
    cmdDesactivar.style.display='inline';
    campo.value="activada"; 
}//onClickActivar
*/

function onClickDesactivar(idCampo){
    if(!gConsultando){
        if (idCampo==null)return;
        //var cmdActivar = document.getElementById("cmdActivar"+idCampo);
        //var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
        var cmdActivar = document.getElementById("enlaceActivar"+idCampo);
        var cmdDesactivar = document.getElementById("enlaceDesactivar"+idCampo);
        var campo =  document.getElementById("activar"+idCampo);
        //al pulsar bton desactivar, se oculta el bton activar
        cmdActivar.style.visibility='visible';
        cmdActivar.style.display='inline';
        cmdDesactivar.style.display='none';
        campo.value="desactivada"; 
    }
}//onClickDesactivar

function onClickActivar(idCampo){ 
    if(!gConsultando){
        if (idCampo==null)return;
        //var cmdActivar = document.getElementById("cmdActivar"+idCampo);
        //var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
        var cmdActivar = document.getElementById("enlaceActivar"+idCampo);
        var cmdDesactivar = document.getElementById("enlaceDesactivar"+idCampo);
        var campo =  document.getElementById("activar"+idCampo);
        //al pulsar bton desactivar, se oculta el bton activar
        cmdDesactivar.style.visibility='visible';
        cmdActivar.style.display='none';
        cmdDesactivar.style.display='inline';
        campo.value="activada"; 
    }
}//onClickActivar



function mostrarCapasBotones(nombreCapa) {
    document.getElementById('capaBotones1').style.display='none';
    document.getElementById('capaBotones11').style.display='none';
    document.getElementById('capaBotones2').style.display='none';
    document.getElementById('capaBotones3').style.display='none';
    document.getElementById('capaBotones4').style.display='none';
    document.getElementById(nombreCapa).style.display='';
}//mostrarCapasBotones

/**  Carga el tercero siguiente al actyual si es posible */
function terceroSiguiente() {
    var pos = getPosTerceroActual();
    var num = getNumInteresados();
    var separador = '§¥';

    if((pos +1)<num){
        var inter = listaInteresados[pos+1];
        var linter = new Array();
        linter = inter.split("#");
        document.forms[0].titular.value = linter[2];
        document.forms[0].domicilio.value = linter[3];
        document.forms[0].codTipoDoc.value = linter[0];
        document.forms[0].descTipoDoc.value = getDescTipoDoc(linter[0]);
        document.forms[0].txtDNI.value = linter[1];
        terceroActual = getCodTercero(pos +1);
        posInteresadoActual= (pos+1) + 1;
        updateMsgNumInteresados();

        // Se fija el rol
        var descript = new Array();
        descript = getDescRol(pos+1);
        comboRol.buscaCodigo(descript[0]);

        var terSig = getCodTerceroSiguiente();
        var terAnt = getCodTerceroAnterior();
            notificacionTercero(pos+1);

        if(terAnt==-1)
            deshabilitarImagenBoton("imgAnt",true);
        else
            deshabilitarImagenBoton("imgAnt",false);

        if(terSig==-1)
            deshabilitarImagenBoton("imgNext",true);
        else
            deshabilitarImagenBoton("imgNext",false);
    }
}//terceroSiguiente

function terceroAnterior(){
    var pos = getPosTerceroActual();

        if((pos -1)>=0){
        var inter = listaInteresados[pos-1];
        var linter = new Array();
        linter = inter.split("#");
        document.forms[0].titular.value = linter[2];
        document.forms[0].domicilio.value = linter[3];
        document.forms[0].codTipoDoc.value = linter[0];
        document.forms[0].descTipoDoc.value = getDescTipoDoc(linter[0]);
        document.forms[0].txtDNI.value = linter[1];
        terceroActual = getCodTercero(pos-1);
        posInteresadoActual= pos;
        var descript = getDescRol(pos-1);
        comboRol.buscaCodigo(descript[0]);
        updateMsgNumInteresados();

        var terSig = getCodTerceroSiguiente();
        var terAnt = getCodTerceroAnterior();
        notificacionTercero(pos-1);
        if(terAnt==-1)
            deshabilitarImagenBoton("imgAnt",true);
        else
            deshabilitarImagenBoton("imgAnt",false);


        if(terSig==-1)
            deshabilitarImagenBoton("imgNext",true);
        else
            deshabilitarImagenBoton("imgNext",false);
    }
}//terceroAnterior

function notificacionTercero(posicion){
    var valor = listaNotificacionesElectronicas[posicion];
    if(valor==0)
        document.forms[0].admiteNotificacion.checked = false;
    else
        document.forms[0].admiteNotificacion.checked = true;
}//notificacionTercero

function pulsarVerExpre(nombre){
    var ajax = getXMLHttpRequest();
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var result ="";    
    if(ajax!=null){                                                         
        var url = getContextPath() + "/sge/FichaExpediente.do";       
        var parametros = "&opcion=recuperarExpresion&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                            + "&campo=" + nombre;                    
        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        try{            
            if (ajax.readyState==4 && ajax.status==200){         
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var text = ajax.responseText;                                             
               if (text.trim() != ""){                                        
                       abrirXanelaAuxiliar(getContextPath() + '/jsp/sge/mainVentana.jsp?source='+getContextPath() + "/jsp/sge/mostrarExpresion.jsp?sql="+text,'',
                                'width=550,height=350,scrollbars=yes',function(){});
                }
            }
        }catch(Err){
            alert("Error.descripcion: " + Err.description);            
        }
    }//if(ajax!=null)
}//pulsarVerExpre


function verificarPantallaExterna(i){
    var urlExterna = null;
    var parametrosVentana = null;
    var ajax = getXMLHttpRequest();
    if(ajax!=null){

        var ocurrenciaTramite = listaTramitesOriginal[i][0];
        var codTramite       = listaTramitesOriginal[i][1];
        var fechaInicio        = listaTramitesOriginal[i][3];
        var fechaFin           = listaTramitesOriginal[i][4];
        var codProcedimiento = document.forms[0].codProcedimiento.value;
        var codMunicipio     = document.forms[0].codMunicipio.value;
        var numero           = document.forms[0].numero.value;
        var codUtr           = listaTramitesOriginal[i][10];
        var ejercicio        = document.forms[0].ejercicio.value;
        var url             = getContextPath() + "/sge/TramitacionExpedientes.do";       
        var parametros = "opcion=comprobarPluginTramitacionExterna&codTramite=" + escape(codTramite) + "&codProcedimiento=" + escape(codProcedimiento)
            + "&codOrganizacion=" + escape(codMunicipio) + "&ocurrenciaTramite=" + escape(ocurrenciaTramite) + "&numExpediente=" + escape(numero)
            + "&fechaInicioTramite=" + escape(fechaInicio) + "&fechaFinTramite=" + escape(fechaFin) + "&codUorTramitadora=" + escape(codUtr) + "&ejercicio=" + escape(ejercicio);

        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        
        try{
            if (ajax.readyState==4 && ajax.status==200){                
                
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var text = ajax.responseText;
               
               if(text!=null && text!=undefined && text!="null" && text!=""){
                    urlExterna = text.trim();
               }
            }
        }catch(Err){
            alert("Error.descripcion: " + Err.description);
        }
        // Se solicita ahora los parámetros de configuración de la ventana
		var parametros = "opcion=parametrosPluginTramitacionExterna&codTramite=" + escape(codTramite) + "&codProcedimiento=" + escape(codProcedimiento)
            + "&codOrganizacion=" + escape(codMunicipio) + "&ocurrenciaTramite=" + escape(ocurrenciaTramite) + "&numExpediente=" + escape(numero)
            + "&fechaInicioTramite=" + escape(fechaInicio) + "&fechaFinTramite=" + escape(fechaFin) + "&codUorTramitadora=" + escape(codUtr) + "&ejercicio=" + escape(ejercicio);

        var ajax = getXMLHttpRequest();
        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
		
	try{
            if (ajax.readyState==4 && ajax.status==200){                
                
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var text = ajax.responseText;               
               if(text!=null && text!=undefined && text!="null" && text!=""){
                    parametrosVentana = text.trim();
               }
            }
        }catch(Err){
            alert("Error.descripcion: " + Err.description);
        }	
    }
    var salida = new Array();
    salida[0] =  urlExterna;
    salida[1] =  parametrosVentana;    
    return salida;
}


// Visualizar un documento asociado al expediente de la pestaña 'Otros Documentos'
function pulsarVisualizarOtroDocumento(){
    var codMun = document.forms[0].codMunicipio.value;
    var ejercicio = document.forms[0].ejercicio.value;
    var numero = document.forms[0].numero.value;
    var expHistorico = document.forms[0].expHistorico.value;
	if(tabOtrosDocumentos.selectedIndex != -1) {
        var codDocumento = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][0];
        var extension        = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][4];
        var tipoMime         = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][2];

        if(listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][2] != '') {            
            window.open(getContextPath() + "/VerDocumentoDatosSuplementarios?codigo=" + codDocumento + "&ejercicio=" + ejercicio + "&numero=" + numero + "&codMunicipio=" + codMun + "&extension=" + extension + "&tipoMime=" + tipoMime +
                    "&nombreFich=" + listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][1]  +  "&opcion=0&otroDocExp=si" + "&expHistorico=" + expHistorico, "ventana1",
                    "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
		}
	} else mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_FILA_NO_SELECCIONADA);
}


// Actualiza la lista de documentos asociados al expediente despues de haber eliminado uno concreto
function actualizaEliminarDoc(){ 
    var list = new Array();
    try{
    tamIndex=tabOtrosDocumentos.selectedIndex;
    tamLength=tabOtrosDocumentos.lineas.length;
    for (i=tamIndex - 1; i < listaOtrosDocumentos.length - 1; i++){
        if (i + 1 <= listaOtrosDocumentos.length - 2){
            listaOtrosDocumentos[i + 1]=listaOtrosDocumentos[i + 2];
        }
    }
    for(j=0; j < listaOtrosDocumentos.length-1 ; j++){
        list[j] = listaOtrosDocumentos[j];
    }
    tabOtrosDocumentos.lineas=list;
    tabOtrosDocumentos.displayTabla();
    listaOtrosDocumentos=list;
    }catch(Err){
        alert("ERROR actualizaEliminarDoc(): " + Err.description);
    }
}


    function desactivarAdmiteNotif(){ 
        if(listaNotificacionesElectronicas=='null' || listaNotificacionesElectronicas.length == 0) {
            document.forms[0].admiteNotificacion.checked = false;
            document.forms[0].admiteNotificacion.disabled = true;
        }else{
            document.forms[0].admiteNotificacion.disabled = false;
        }
    }
    
    
    function mostrarAlertaNotificacions(mensaje)
    {
        jsp_alerta('A',mensaje);
    }
    
    
    function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp","Microsoft.XMLHttp"
          ];

        if (window.XMLHttpRequest){
                // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                return new XMLHttpRequest();
        }else if (window.ActiveXObject){
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                    try {
                        var oXmlHttp = new ActiveXObject(aVersions[i]);
                        return oXmlHttp;
                    }catch (error) {
                    //no necesitamos hacer nada especial
                    }
             }
        }
    }

     function mostrarCapas(nombreCapa) {
        document.getElementById(nombreCapa).style.visibility='visible';
    }
    
    

  function interesado_Obligatorio(){ 
     if(esInteresadoObligatorio=="true") 
         return "1";
     else 
         return "0";
  }

   function activarFormulario() {
        habilitarDatos(document.forms[0]);
        for(var i=0;i<listaDocumentos.length;i++) {
            eval("document.forms[0].documentoEntregado" + i + ".disabled=false");
        }
    }
    
    
     function valoresInicialesTercero() {
        var listaCodTercero = "";
        var listaVersionTercero = "";
        var listaCodDomicilios= "";
        var listaRol = "";
        for (i=0; i < terceros.length; i++) {
            listaCodTercero += terceros[i][0]+'§¥';
            listaVersionTercero += terceros[i][1]+'§¥';
            listaRol += terceros[i][7]+ '§¥';
            listaCodDomicilios += terceros[i][4]+'§¥';
        }
        document.forms[0].listaCodTercero.value = listaCodTercero;
        document.forms[0].listaVersionTercero.value = listaVersionTercero;
        document.forms[0].listaCodDomicilio.value = listaCodDomicilios;
        document.forms[0].listaRol.value = listaRol;
    }

    function pulsarCancelar() { 
        top.close();
    }    
    
    function pulsarAceptar() {
       var retorno = new Array();
       retorno[0] = document.forms[0].numExpediente.value;
       self.parent.opener.retornoXanelaAuxiliar(retorno);
    }
    
      function calcularAltoAgrupacion(codAgrupacion){
        var tamanho = 0;
        var maxY=0;
        var ultimoCampo=new Array();
        for (i=0; i < listaCampos.length; i++) {
            var campo = new Array();
                campo = listaCampos[i];           
            if(campo[4] == codAgrupacion){
                var posY = parseInt(campo[3]);
                if(posY>maxY) {
                    ultimoCampo=campo;
                    maxY = posY
                }
            }//if(campo[4] == codAgrupacion)
        }//for (i=0; i < listaCampos.length; i++) 
       
       if (listaCampos.length>0 && maxY > 0){
            if (ultimoCampo[5] == 4)               
              tamanho = maxY+ 120;
            else 
                tamanho = maxY+ 40;
       }
        if(tamanho!=0){
        try{
            document.getElementById("capaDatosSuplementarios_" + codAgrupacion).style.height = tamanho + "px";
            var alturaCapaPrincipal = document.getElementById("capaDatosSuplementarios").style.height;
            alturaCapaPrincipal = Number(alturaCapaPrincipal.replace("px",""));
            alturaCapaPrincipal = alturaCapaPrincipal + tamanho + 100;
            document.getElementById("capaDatosSuplementarios").style.height = alturaCapaPrincipal + "px";
        }catch(Err){
            // Puede dar error establecer el tamaño de la agrupación por defecto, se captura el error pero no se hace nada
        }
        
      }
    }//calcularAltoAgrupacion
   
   /**
     * Función utilizada cuando se ha indicado que un tipo de documento es un CIF, pero se va a valir
     * si se trata de un NIF. No vacía el campo de formulario con el documento como hace la función
     * @param {String} campo: Cadena de caracteres con el documento a validar
     * @returns {Boolean}
     */
    function validarNieDocumentoTipoCif(documento) {
        var LONGITUD = 9;

        // Si se trata de un NIE
        // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.    
        if (documento.length != LONGITUD) {                        
            return false;
        }

        // Comprobas que el formato se corresponde con el de un NIE
        var primeraLetra = documento.substring(0,1);
        var numero = documento.substring(1,8);
        var ultimaLetra = documento.substring(8,9);
        if (!(isNaN(primeraLetra) && !isNaN(numero) && isNaN(ultimaLetra))) {                
            return false;
        }

        // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
        if (primeraLetra == "Y") numero = parseInt(numero,10) + 10000000;
        else if (primeraLetra == "Z") numero = parseInt(numero,10) + 20000000;
        else if (primeraLetra != "X") {                
            return false;
        }

        // Validamos el caracter de control.
        var letraCorrecta = getLetraNif(numero);
        if (ultimaLetra != letraCorrecta) {                
            return false;
        }
        return true;
    }


    /**
      * Función utilizada cuando se ha indicado que un tipo de documento es un CIF, pero se va a valir
      * si se trata de un NIF. No vacía el campo de formulario con el documento como hace la función
      * validarNif de gestionTerceros.jsp
      * @param {document.forms[0].text} campo
      * @returns {Boolean
      */  
    function validarNifDocumentoTipoCif(abc) {        
        var dni=abc.substring(0,abc.length-1);
        let=abc.charAt(abc.length-1);
        if (!isNaN(let)) {
            return false;
        } else {
            cadena="TRWAGMYFPDXBNJZSQVHLCKET";
            posicion = dni % 23;
            letra = cadena.substring(posicion,posicion+1);
            if (letra!=let.toUpperCase()) return false;
        }
        return true;
    }
    
    
    
    
function marcarEliminadoCampoSuplementarioFichero(codigo,origen,msgErr1,msgErr2) {   
    var exito =  false;
    if(codigo==null || origen==null || codigo=="" || origen==""){
        jsp_alerta("A",msgErr1);        
    }else{        
        var ajax = getXMLHttpRequest();
        
        if(ajax!=null){
            
          var url =APP_CONTEXT_PATH + "/EliminarDocumentoCampoSuplementarioFichero.do";          
          var parametros = "&codigoCampo=" + codigo + "&origen=EXPEDIENTE";
          ajax.open("POST",url,false);
          ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
          ajax.setRequestHeader("Accept", "text/html, application/xml, text/plain");
          ajax.send(parametros);
          
          try{
              
              if (ajax.readyState==4 && ajax.status==200){                  									
                  var text = ajax.responseText;											
                  var datosRespuesta = text.split("=");
                  
                  if(datosRespuesta!=null && datosRespuesta.length==2 && datosRespuesta[0].trim()=="exito" && datosRespuesta[1].trim()=="1"){                        
                        exito = true;
                  }else
                      jsp_alerta("A",msgErr2);
              }
          }catch(Err){
              alert("Error: " + Err.description);
          }
      }
            
      return exito;
    }    
}

function crearCSVCampoSuplementarioFichero(codigo) {
    
    pleaseWait('on');

    if (codigo) {
        try {
             $.ajax({
                 url: getContextPath() + "/sge/DatosSuplementariosFichero.do",
                 type: 'POST',
                 async: true,
                 data: {
                     opcion: 'crearCSV',
                     codigo: codigo,
                     origen: 'EXPEDIENTE'
                 },
                 success: procesarRespuestaCrearCSVCampoSuplementarioFichero,
                 error: muestraErrorRespuestaGenerarCSVDocumento
             });
        } catch(Err){
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    } else {
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CSV_CODIGO_DESCONOCIDO);
    }
}

function procesarRespuestaCrearCSVCampoSuplementarioFichero(ajaxResult) {
    var respuesta = JSON.parse(ajaxResult);

    if (respuesta) {
        var codError = respuesta.status;

        if (codError === 0) { // Si es ok
            var resultado = respuesta.resultado;
            
            if (resultado) {
                var codigoCampo = resultado.codigoCampo;
                var nombreFichero = resultado.nombreFichero;

                modificaVariableCambios(1);
                actualizarFicheros(codigoCampo, nombreFichero);
            }

            mostrarMensajeErrorCargarParcialExpediente(TIPO_OK_CSV_GENERADO_CORRECTO);
        } else if (codError === 100) { // Ya existe el CSV       
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE);
        } else if (codError === 102) { // Formato no soportado       
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO);
        } else {
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    } else
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CREAR_CSV_DOCUMENTO);

    pleaseWait('off');
}

function habilitarBotonVerNotificacion(){    
    if(document.getElementById("btnVerNotificacion")!=null && document.getElementById("btnVerNotificacion")!=undefined){
        document.getElementById("btnVerNotificacion").disabled=false;        
        document.getElementById('btnVerNotificacion').style.color = '#ffffff';
    }
}


/// Funciones utilizadas en la cargar parcial de la ficha del expediente

function inicializarTablaOtrosDocumentos(){            
    tabOtrosDocumentos.lineas=listaOtrosDocumentos;
    tabOtrosDocumentos.displayTabla();
} 


function procesarRespuestaListadoDocumentosExternos(ajaxResult){    
    try{     
        var listaDocsExternos = JSON.parse(ajaxResult);        
        var contador = 0;        
        listaOtrosDocumentos = new Array();

        for(i = 0; i < listaDocsExternos.length; i++ ){              
            var codigo = listaDocsExternos[i].codigoDocumento;
            var nombre = listaDocsExternos[i].nombreDocumento;
            var tipo   = listaDocsExternos[i].tipoDocumento;
            var extension = listaDocsExternos[i].extension;
            var fecha = listaDocsExternos[i].fechaAltaDocuemento;

            listaOtrosDocumentos[contador] = [codigo,nombre,tipo,fecha,extension];
            contador++;
        }
        pleaseWait('off');
        tabOtrosDocumentos.lineas=listaOtrosDocumentos;
        tabOtrosDocumentos.displayTabla();
        // Se pone a true esta variable, para que al seleccionar la pestaña "Otros documentos",
        // no se recargue de nuevo el contenido de la misma.
        cargadoOtrosDocumentos = true;

    }catch(Err){
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_DOCUMENTOS_EXTERNOS);
    }
 }

 function eliminarOtroDocumentoExpediente(tamIndece,codInternoDoc,nombre,extension){     
    pleaseWait('on');
    try{        
         $.ajax({
             url: getContextPath() + "/sge/FichaExpedienteDocumento.do",
             type: 'POST',
             async: true,
             data: 'codInternoDoc=' + codInternoDoc + '&nombre=' + nombre + '&extension=' + extension + "&opcion=documentoEliminar",
             success: procesarRespuestaEliminarDocumentoExterno,
             error: muestraErrorRespuestaEliminarDocumentoExterno
         });
    }catch(Err){
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_ELIMINAR_DOCUMENTO_EXTERNO);
    } 
}

function generarCSVOtroDocumentoExpediente(codInternoDoc, nombre, extension, tipoMime){     
    pleaseWait('on');
    try{        
         $.ajax({
             url: getContextPath() + "/sge/FichaExpedienteDocumento.do",
             type: 'POST',
             async: true,
             data: {
                 opcion: 'documentoCrearCSV',
                 codInternoDoc: codInternoDoc,
                 nombre: unescape(nombre),
                 extension: extension,
                 tipoMime: tipoMime
             },
             success: procesarRespuestaGenerarCSVDocumentoExterno,
             error: muestraErrorRespuestaGenerarCSVDocumento
         });
    }catch(Err){
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CREAR_CSV_DOCUMENTO);
    } 
}

function procesarRespuestaEliminarDocumentoExterno(ajaxResult){
    pleaseWait('off');    
    var indiceDocumento = tabOtrosDocumentos.selectedIndex;    
    var respuesta = JSON.parse(ajaxResult);     
    
    if(respuesta!=undefined && respuesta!=null){
        if(respuesta.status=="0"){            
            listaOtrosDocumentos.splice(indiceDocumento,1);
            tabOtrosDocumentos.lineas=listaOtrosDocumentos;
            tabOtrosDocumentos.displayTabla();               
        }else
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_ELIMINAR_DOCUMENTO_EXTERNO);
            
    }else
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_ELIMINAR_DOCUMENTO_EXTERNO);
}

function procesarRespuestaGenerarCSVDocumentoExterno(ajaxResult) {
    var respuesta = JSON.parse(ajaxResult);     
    
    if(respuesta!=undefined && respuesta!=null){
        var codError = respuesta.status;
        
        if (codError === 0) { // Si es ok
            if (respuesta.resultado) {
                //Actualizar datos del fichero
                var indiceDocumento = tabOtrosDocumentos.selectedIndex;
                
                // Formato del listaOtrosDocumentos[x] -> ["4265", "DOCUMENTO_21042017_131143", "application/pdf", "21/04/2017", "pdf"]
                listaOtrosDocumentos[indiceDocumento][0] = respuesta.resultado.codigoDocumento;
                listaOtrosDocumentos[indiceDocumento][1] = respuesta.resultado.nombreDocumento;
                listaOtrosDocumentos[indiceDocumento][2] = respuesta.resultado.tipoDocumento;
                listaOtrosDocumentos[indiceDocumento][3] = respuesta.resultado.fechaAltaDocuemento;
                listaOtrosDocumentos[indiceDocumento][4] = respuesta.resultado.extension;
                
                tabOtrosDocumentos.lineas=listaOtrosDocumentos;
                tabOtrosDocumentos.displayTabla();
            }
            
            mostrarMensajeErrorCargarParcialExpediente(TIPO_OK_CSV_GENERADO_CORRECTO);
        } else if(codError === 100) { // Ya existe el CSV       
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE);
        } else if(codError === 102) { // Formato no soportado       
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO);
        } else {
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    }else
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CREAR_CSV_DOCUMENTO);
    
    pleaseWait('off'); 
}

function procesarRespuestaListadoNotificacionesExpediente(ajaxResult){    
    pleaseWait('off'); 
    var datos = JSON.parse(ajaxResult);       
    var notificaciones = datos.notificaciones;       
    var tramites = datos.tramitesNotificados;   
    
    cargarListadoNotificaciones(notificaciones);
    if(tabTramitesNotif != null)
        cargarListadoTramitesNotificadosNoE(tramites);
    
    cargadoNotificaciones = true;
}

function cargarListadoNotificaciones(notificaciones){
    var contador = 0;
    
    if(notificaciones!=undefined && notificaciones!=null){        
        for(var i=0;i<notificaciones.length;i++){            
            var codigoNotificacion = notificaciones[i].codigoNotificacion;            
            var textoNotificacion = notificaciones[i].textoNotificacion;
            var fechaEnvio = notificaciones[i].fechaEnvioAsString;            
            var numeroRegistroTelematico = notificaciones[i].numeroRegistroTelematico;
            var acto = '';
            if(notificaciones[i].actoNotificado!=null && notificaciones[i].actoNotificado!=undefined)
                acto = notificaciones[i].actoNotificado.toUpperCase();
            var nombreTramite = notificaciones[i].nombreTramite;
            var estadoNotificacion = descripcionEstadoNotificacion(notificaciones[i].estadoNotificacion).toUpperCase();
            var fechaAcuse = notificaciones[i].fechaAcuseAsString;            
            var resultado = notificaciones[i].resultado; 
			var interesado = '';
			var autorizados = notificaciones[i].autorizados;
            if(autorizados != null && autorizados != undefined && autorizados.length > 0)
				interesado = notificaciones[i].autorizados[0].nombreCompleto;
			var fechaSolEnvio = '';
            if(notificaciones[i].fechaSolEnvioAsString!=null && notificaciones[i].fechaSolEnvioAsString!=undefined)
                fechaSolEnvio = notificaciones[i].fechaSolEnvioAsString;
			var identificadorPlatea = '';
            if(notificaciones[i].identificadorPlatea!=null && notificaciones[i].identificadorPlatea!=undefined)
                identificadorPlatea = notificaciones[i].identificadorPlatea;
            
            if(mostrarColumnasNotificaciones){
                listaNotificacionesTabla[contador]=[fechaSolEnvio,fechaEnvio,acto,nombreTramite,estadoNotificacion,fechaAcuse,resultado, identificadorPlatea];
            } else {
                listaNotificacionesTabla[contador]=[fechaEnvio,acto,nombreTramite,estadoNotificacion,fechaAcuse,interesado];
            }
            listaNotificaciones[contador] = [codigoNotificacion,acto,textoNotificacion,estadoNotificacion,numeroRegistroTelematico];
            contador++;
        } 
        tabNotificaciones.lineas=listaNotificacionesTabla;
        tabNotificaciones.displayTabla();
    }
}

function cargarListadoTramitesNotificadosNoE(tramites){
    var listaTramitesTabla = new Array();
    var contador = 0;
    if(tramites!=undefined && tramites!=null){        
        for(var i=0;i<tramites.length;i++){            
            var nombreTramite = tramites[i].tramite;
            var codigoTramite = tramites[i].codTramite;            
            var ocuTramite = tramites[i].ocurrenciaTramite;            
            var fechaInicio = tramites[i].fechaInicio;          
            var fechaFin = tramites[i].fechaFin;
            if(fechaFin == undefined || fechaFin == null)
                fechaFin = "--";
            
            var camposSup = tratarCamposSupTramitesNotificados(tramites[i]);
            
            var enlace = "<a href='javascript:void(0);' onclick='mostrarDocumentacionTramiteNotificado(" + codigoTramite + "," + ocuTramite + ");'>";
            enlace += "<span class='fa fa-eye' id='enlaceTodo' name='enlaceTodo'></span>";
            enlace += "</a>";   
                        
            listaTramitesTabla[contador]=[
                codigoTramite + " - " + nombreTramite + " (Ocu. " + ocuTramite + ")",
                fechaInicio,fechaFin,camposSup,enlace
            ];     
            contador++;
        } 
        tabTramitesNotif.lineas=listaTramitesTabla;
        tabTramitesNotif.displayTabla();
    }
}

function tratarCamposSupTramitesNotificados(tramite){
    var camposSup = "";
    var eCamposSuplementarios = tramite.estructuraDatosSuplementarios;
    if(eCamposSuplementarios!=undefined && eCamposSuplementarios!=null){
        camposSup = "<div>";
        var vCamposSuplementarios = tramite.valoresDatosSuplementarios;
        for(var jE=0;jE<eCamposSuplementarios.length;jE++){
            var codigo = eval("eCamposSuplementarios[jE].codCampo");
            var valor = eval("vCamposSuplementarios[jE].campos." + codigo);
            if(valor == undefined || valor == null)
                valor = "";
            camposSup += "<div><span class='etiqueta-cod-cs'>" + codigo + ":</span><span class='etiqueta-val-cs'>" + valor + "</span></div>";
        }
        camposSup += "</div>";
    } 
    return camposSup;
}

function mostrarDocumentacionTramiteNotificado(codTramNotif,ocuTramNotif){
    var pos = -1;
    if(listaTramitesOriginal.length>0){
        for(var index = 0; index < listaTramitesOriginal.length; index++){
            if(listaTramitesOriginal[index][0]==ocuTramNotif && listaTramitesOriginal[index][1]==codTramNotif){
                pos = index;
                break;
            }
        }
        if(pos!=-1){
            cargarTramitacion(pos);
        } else 
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_TRAMITE_NOTIFICADO_NO_INICIADO);
    }
}

function procesarRespuestaListadoComunicacionesExpediente(ajaxResult){    
    pleaseWait('off');

    var comunicaciones = JSON.parse(ajaxResult);
    var contador = 0;
    if(comunicaciones!=null && comunicaciones!=undefined){

        for(var i=0;i<comunicaciones.length;i++){
            var id = comunicaciones[i].id;
            var numeroRegistro = comunicaciones[i].numeroRegistro;
            var asunto         = comunicaciones[i].asunto;
            var fecha          = comunicaciones[i].fechaAsString;
            var estado         = descripcionComunicacionLeida(comunicaciones[i].leida);
            listaComunicaciones[contador] = [id,numeroRegistro,asunto,estado,fecha];
            contador++;
        }

        tabComunicaciones.lineas=listaComunicaciones;
        tabComunicaciones.displayTabla();
        cargadoComunicaciones  = true;
    }
}



function procesarRespuestaOtrosDatosExpediente(ajaxResult){    
    pleaseWait('off');    
    var resultado = JSON.parse(ajaxResult);
    var contadorEnlaces = 0;
    if(resultado!=null){
        
        if(resultado.status!=0){
             mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_OTROS_DATOS_EXPEDIENTE);
        }else{
        
            // Se actualiza la sección de Enlaces, si los hubiese
            if(resultado.enlaces!=null && resultado.enlaces!=undefined){

                for(var i=0;resultado.enlaces!=null && i<resultado.enlaces.length;i++){                        
                    var descripcion = resultado.enlaces[i].descripcion;
                    var url         = resultado.enlaces[i].url;
                    listaEnlaces[contadorEnlaces] = [descripcion,url];
                    contadorEnlaces++;
                }

                if(listaEnlaces.length>0){
                    $('#filaSubtituloEnlaces').css("display","");
                    $('#filaContenidoEnlaces').css("display","");                        
                    // Se genera el contenido en la función enlaces() y se muestra en la capa "capaEnlaces"
                    domlay('capaEnlaces',1,0,0,enlaces());
                }
            }
            
            
            // Se actualiza la sección de asientos de registro, si los hubiese
            if(resultado.asientos!=null && resultado.asientos!=undefined){    
                
                // Si se ha producido algún al recuperar asientos de registro externos, se muestran los errores al usuario
                if(resultado.errores!=null && resultado.errores!=undefined){
                    mostrarMensajesErrorAsientosRegistroExternos(resultado.errores);
                }
                
                // Se obtienen los asientos de registro recuperados, que están asociados al expediente
                var contadorAsientos = 0;
                for(var i=0;i<resultado.asientos.length;i++){                                
                    var codigoDepartamento  = resultado.asientos[i].codigoDepartamento;
                    var codigoUOR           = resultado.asientos[i].codigoUOR;
                    var tipoAsiento         = resultado.asientos[i].tipoAsiento;                    
                    var fechaAsiento        = resultado.asientos[i].fechaAsiento;                    
                    var asuntoAsiento       = resultado.asientos[i].asuntoAsiento;
                    var origenAsiento       = resultado.asientos[i].origenAsiento;
                    var nombreInteresado    = resultado.asientos[i].nombreInteresado;                                        
                    var apellido1Interesado = resultado.asientos[i].apellido1Interesado;                                        
                    var apellido2Interesado = resultado.asientos[i].apellido2Interesado;                                      
                    var masInteresados      = resultado.asientos[i].masInteresados;
                    var observaciones       = resultado.asientos[i].observaciones;
                    var nombreCompleto      = getNombreCompletoInteresado(nombreInteresado!=undefined?nombreInteresado.trim():'',apellido1Interesado!=undefined?apellido1Interesado.trim():'',apellido2Interesado!=undefined?apellido2Interesado.trim():'',masInteresados);
                    var idAsiento           = resultado.asientos[i].ejercicioAsiento + "/" + resultado.asientos[i].numeroAsiento;                                        
                    var descripcionCortaAsunto = getDescripcionCortaAsuntoAsiento(asuntoAsiento);
                    var codigoUOD           = resultado.asientos[i].codigoUOD; 
                    
                    listaAsientosOriginal[contadorAsientos] = [codigoDepartamento,codigoUOR,tipoAsiento,idAsiento,fechaAsiento,nombreCompleto,asuntoAsiento,origenAsiento,codigoUOD];
                    listaAsientos[contadorAsientos] = [idAsiento,tipoAsiento,fechaAsiento,nombreCompleto,unescape(descripcionCortaAsunto)];                                        
                    (observaciones)?(listaObs[contadorAsientos]=true):(listaObs[contadorAsientos]=false);
                    contadorAsientos++;                    
                }
                
                if(listaAsientosOriginal.length>0 && listaAsientos.length>0){
                    tabAsientos.lineas=listaAsientos;
                    tabAsientos.observaciones=listaObs;
                    tabAsientos.displayTabla();
                }
            }
            
            
            if(resultado.documentos!=null && resultado.documentos!=undefined){                               
                var checkEntreg0 = "<input type='checkbox' class='check' name='documentoEntregado";
                var checkEntreg1 = "' id='documentoEntregado";
                var checkEntreg2 = "' value='SI'";
                var checkEntreg3 = " CHECKED ";
                var checkEntreg4 = " onclick='javascript:verificarDocumentosPresentados(this);'";
                var checkEntreg5 = ">";
                var entreg;
                
                for(i=0;resultado.documentos!=null && i<resultado.documentos.length;i++){
                    var codigo = resultado.documentos[i].codigo;
                    var nombreDocumento = resultado.documentos[i].nombreDocumento;
                    var condicion = resultado.documentos[i].condicion;
                    var codDocumentoAdjunto = resultado.documentos[i].codDocumentoAdjunto;
                    var tipoMimeDocumentoAdjunto = resultado.documentos[i].tipoMimeDocumentoAdjunto || '';
                    var nombreDocumentoAdjunto   = resultado.documentos[i].nombreDocumentoAdjunto || '';
                    var fechaAltaDocumentoAdjunto = resultado.documentos[i].fechaAltaDocumentoAdjunto || '';
                    var estadoFirmaDocumentoAdjunto = resultado.documentos[i].estadoFirmaDocumentoAdjunto || '';
                    var fechaEntrega = resultado.documentos[i].fechaEntrega || '';
                    var entregado = resultado.documentos[i].entregado;
                    
                    entreg = checkEntreg0 + i + checkEntreg1 + i +checkEntreg2;                    
                    if(entregado)
                        entreg += checkEntreg3;
                    
                    entreg += checkEntreg4;
                    entreg += checkEntreg5;
                    
                    var enlaceVerificarFirma = "";
                    if(estadoFirmaDocumentoAdjunto!=null && estadoFirmaDocumentoAdjunto!=undefined && estadoFirmaDocumentoAdjunto=="F")
                        enlaceVerificarFirma = getEnlaceVerificarFirmaDocumentoInicio(codDocumentoAdjunto,codigo)
                    
                    listaDocumentosOriginal[i]        = [codigo,nombreDocumento,condicion];
                    listaDocumentos[i]                = [entreg,nombreDocumento,condicion,nombreDocumentoAdjunto,tipoMimeDocumentoAdjunto,fechaAltaDocumentoAdjunto,descripcionEstadoFirmaDocumentoInicio(estadoFirmaDocumentoAdjunto),codDocumentoAdjunto,enlaceVerificarFirma];
                    listaDocumentosOriginalAuxiliar[i]= [entreg,nombreDocumento,condicion,nombreDocumentoAdjunto,tipoMimeDocumentoAdjunto,fechaAltaDocumentoAdjunto,descripcionEstadoFirmaDocumentoInicio(estadoFirmaDocumentoAdjunto),codDocumentoAdjunto,enlaceVerificarFirma];                    
                }                
                tabDocumentos.lineas=listaDocumentos;
                tabDocumentos.displayTabla();
                
                if(estadoExpediente==1 || estadoExpediente==9) { 
                    // Si el expediente está anulado o finalizado, se habilita el botón [Ver] 
                    // para poder visualizar un documento de inicio de expediente
                    document.forms[0].cmdVerDocumentoExpediente.disabled = false;
                    document.forms[0].cmdVerDocumentoExpediente.style.color = '#ffffff';                        
                    // Se deshabilitan los checkbox de cada documento de inicio, porque está habilitado y el usuario
                    // pulsa sobre ellos, se puede llegar a eliminar el documento de inicio
                    for(var i=0;listaDocumentos!=undefined && i<listaDocumentos.length;i++){                                                
                        $('#documentoEntregado' + i).prop('disabled',true);                        
                    }                    
                }// if                 
            }
            
            // Se pone a true esta variable, para indicar que la pestaña "Otros Datos" 
            // ya ha sido cargada, y evitar volver a recargarla de nuevo
            cargadoOtrosDatos = true;
            
        }
    }   
}

function getHTMLCampoTexto(campo,mostrarDescTramite){    
    try{
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var tamano        = campo.tamano;        
        var modoLectura = "";            
        var valor = ''; 
        var disabled ='';
        var tamanoVista;
	
        if(tamano<=50){	
            tamanoVista=50;	
        }else{	
            tamanoVista=campo.tamanoVista;	
        }
        
        var nombreCampo = campo.codCampo;
        if(campo.codTramite!=null && campo.codTramite!=undefined){
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;            
        }
           
        if(campo.soloLectura=="true") modoLectura='readonly';
        if(campo.bloqueado=="SI") modoLectura='readonly';
        // Si se están consultando, entonces se deshabilita el campo de texto
        if(gConsultando) disabled='disabled';            
                    
        var estiloCss = (campo.obligatorio=="1")?ESTILO_CSS_INPUT_OBLIGATORIO:ESTILO_CSS_INPUT_NORMAL;
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = campo.valorCampo.valorDatoSuplementario;
            contadorValoresCamposSuplementariosExpediente++;
        }
        
        var inputForm ='';               
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
           var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += "<input type='text' id='" + nombreCampo + "' name='" + nombreCampo + "' class='" + estiloCss + "' maxlength=" + tamano + " title='" + descCampo + "' " + modoLectura +  ' ' + disabled  + " onchange='cambiosCampoSupl();' style='width:"+tamanoVista+"%;text-transform: none' onkeyup='return xValidarCaracteres(this)'" + " value='" + valor + "'" + ">";
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
       }else{                   
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";       
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td>";
            inputForm += "<td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>"
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += "<input type='text' id='" + nombreCampo + "' name='" + nombreCampo + "' class='" + estiloCss + "' maxlength=" + tamano + " title='" + descCampo + "' " + modoLectura +  ' ' + disabled  + " onchange='cambiosCampoSupl();' style='width:"+tamanoVista+"%;text-transform: none' onkeyup='return xValidarCaracteres(this)'" + " value='" + valor + "'" + ">";
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
       }        
       return inputForm;
                
    }catch(Err){
        alert("Error al obtener el HTML de un campo suplementario de texto con código " + codCampo + ": " + Err.description);
    }
}





/**
 * Devuelve el código HTML para un campo suplementario de tipo numérico
 * @param: Objeto que contiene la estructura del campo a crear, así como su valor
 */
function getHTMLCampoNumerico(campo,mostrarDescTramite){
    try{                
        
        var estiloCss     = '';   
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var modoLectura = "";            
        var valor = ''; 
        var ob    = '';
        var id    = '';
        var disabled = '';
                        
        if(campo.soloLectura=="true") modoLectura="readonly";
        if(campo.bloqueado=="SI") modoLectura="readonly";
        // Si se están consultando, entonces se deshabilita el campo numérico
        if(gConsultando) disabled='disabled';            
        
        /********************************/
        
        if(campo.obligatorio=="1") {
            estiloCss = "inputTextoObligatorio";
            ob = "obligatorio";
        } else {
            estiloCss= "inputTexto";
        }
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = formatValorDecimal(campo.valorCampo.valorDatoSuplementario);
            contadorValoresCamposSuplementariosExpediente++;
        }

        var nombreCampo = campo.codCampo;
        if(campo.codTramite!=null && campo.codTramite!=undefined){
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;            
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;            
        }
        
        if(ob=="obligatorio") {
             id = ob;
        } else {
            id = nombreCampo;
        }
       
        var campoFormulario = "<input type='text' name='" + nombreCampo + "' value='" + valor + "' maxlength=" + campo.tamano + " id='" + id + "' class='" + estiloCss + "' " + 
                    "title='" + campo.descCampo + "' " + modoLectura + " onchange='cambiosCampoSupl();' size=20 onfocus='javascript:desFormatValorDecimal(this," + campo.soloLectura + ");' "  +                     
                    "onkeypress='javascript:if(gConsultando) return SoloDigitosConsultaCampoNumerico(event); else return SoloDigitosNumericosCampoNumerico(event);' " + 
                    "onBlur='javascript:validarLongitudNumero(this,\"" + nombreCampo + "\"); if(!gConsultando) recargarCamposCalculados();'" + ' ' + disabled + "/>";
        
        var inputForm ='';
        var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
       
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += campoFormulario;
            inputForm +="</td></tr></table>";            
            inputForm +="</div>";
       }else{                              
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += campoFormulario;
            inputForm +="</td></tr></table>";            
            inputForm +="</div>";
       }        
       return inputForm;
        
    }catch(Err){
        alert("Error al obtener el HTML de un campo suplementario de texto con código " + codCampo + ": " + Err.description);
    }
}



/**
 * Devuelve el código HTML para un campo suplementario de tipo fichero
 * @param: Objeto que contiene la estructura del campo a crear, así como su valor
 */
function getHTMLCampoFichero(campo,mostrarDescTramite){
    try{                
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var modoLectura = "";            
        var valor = ''; 
        var estiloCSS = '';
        var ob = '';     
        var id = '';
        var deshabilitado = '';
                
        if(campo.obligatorio=='1') {
            estiloCSS = "inputTextoObligatorio";
            ob = "obligatorio";
        } else {
            estiloCSS = "inputTexto";
        }
                
        if(campo.soloLectura=="true") modoLectura="readonly";
        if(campo.bloqueado=="SI") modoLectura="readonly";
        // Si se están consultando, entonces se deshabilita el campo fichero
        if(gConsultando) deshabilitado='disabled';            
        
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = campo.valorCampo.valorDatoSuplementario;
            contadorValoresCamposSuplementariosExpediente++;
        }
               
        var nombreCampo = codCampo;               
        if (campo.codTramite!=null && campo.codTramite!=undefined) {
            var digitosTramite = eval(campo.codTramite.length);	
            nombreCampo = nombreCampo.substring(0,digitosTramite+1) + "_" + nombreCampo.substring(digitosTramite+1);
            nombreCampo =  nombreCampo + "_" + campo.ocurrencia;
        }
        
        if(ob=='obligatorio'){
            id = ob;
        } else {
            id = nombreCampo;
        }
        
        var inputForm = '';
        inputForm += "<table cellpadding='0px' cellspacing='0px' border='0px'>";
        inputForm += "<tr><td>";
        inputForm += "<input style='height:17px' type='text' name='" + nombreCampo + "' id='" + id + "' class='" + estiloCSS + "' size=75 readonly='true' value='" + valor + "'" + ' ' + deshabilitado + ">";
        
        if(modoLectura!="readonly"){
            inputForm += "<A href=\"javascript:ventanaPopUpModal('inicio','" + nombreCampo + "');\" style='text-decoration:none;'>";
            inputForm += "<span class='fa fa-paperclip' id='imagenBoton" + nombreCampo + "' alt='Fichero'></span>";
            inputForm += "</A>";
        }
        
        inputForm += "</td><td style='width: 12px'></td>";
        if(valor!=undefined && valor!='')
            inputForm += "<td><input type='button' class='botonGeneral' value='Visualizar' name='cmdVisualizar" + nombreCampo + "' onclick=\"onClickDocumento('" + nombreCampo + "','" + valor + "');\"></td>";
        else
            inputForm += "<td><input type='button' class='botonGeneral' value='Visualizar' name='cmdVisualizar" + nombreCampo + "' onclick=\"onClickDocumento('" + nombreCampo + "','" + valor + "');\" disabled></td>";
        
        inputForm += "<td style='width: 2px'></td>";
        if(campo.codTramite==undefined || campo.codTramite==''){
            // Si el campo no es de trámite, se añade el botón eliminar. Pero hay que comprobar si se muestra o no deshabilitado
            
            var disabled = '';
            if(valor==undefined || valor=='' || gConsultando) disabled = "disabled";  
            if (modoLectura === "readonly") {
               inputForm += "<td><input type='button' class='botonGeneral' value='Eliminar' name='cmdEliminar" + nombreCampo + "' onclick=\"onClickEliminarDocumento('" + nombreCampo + "');\" disabled>";   
            } else {
               inputForm += "<td><input type='button' class='botonGeneral' value='Eliminar' name='cmdEliminar" + nombreCampo + "' onclick=\"onClickEliminarDocumento('" + nombreCampo + "');\"" + disabled + ">";    
            }
            inputForm += "<td><input type='button' class='botonGeneral' value='CSV' name='cmdCSV" + nombreCampo + "' onclick=\"onClickCrearCSVDocumento('" + nombreCampo + "');\"" + disabled + ">";
            
        }            
        inputForm += "</td></tr></table>";
        
        var salida ='';
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            salida +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            salida +="<table id='prueba' name='prueba' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            salida +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            salida += descCampo  + "</td>";        
            salida += "<td class='columnP' align='left'>"            
            salida += inputForm;
            salida +="</td></tr></table>";
            salida +="</div>";
        }else{                              
            salida +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            if(mostrarDescTramite)
                salida +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            salida +="<table id='prueba' name='prueba' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            
            salida +="<tr><td class='deTramite' style='width:3%;text-transform;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            salida += descCampo  + "</td>";        
            salida += "<td class='columnP' align='left'>"            
            salida += inputForm;
            salida +="</td></tr></table>";
            salida +="</div>";
        }        
       
        
        return salida;
        
    }catch(Err){
        alert("Error al obtener el HTML de un campo suplementario de fichero con código " + codCampo + ": " + Err.description);
    }
}

/**
 * Devuelve el código HTML para un campo suplementario de tipo texto largo
 * @param: Objeto que contiene la estructura del campo a crear, así como su valor
 */
function getHTMLCampoTextoLargo(campo,mostrarDescTramite){    
    try{ 
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var modoLectura   = "";            
        var valor         = ''; 
        var disabled      = '';
                
        if(campo.soloLectura=="true") modoLectura="readonly";
        if(campo.bloqueado=="SI") modoLectura="readonly";      
        // Si se están consultando, entonces se deshabilita el campo de texto largo
        if(gConsultando) disabled='disabled';            
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = campo.valorCampo.valorDatoSuplementario;
            contadorValoresCamposSuplementariosExpediente++;
        }


        var estiloCSS = '';
        if(campo.obligatorio==1)
            estiloCSS = "textareaTextoObligatorio";
        else
            estiloCSS = "textareaTexto";
        
        var nombreCampo = campo.codCampo;
        if(campo.codTramite!=null && campo.codTramite!=undefined){
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;                
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;                            
        }
   
        var inputForm ='';   
        var enlaceTodo;
        enlaceTodo = "<A href='javascript:void(0);' onclick=\"verTexto('" + nombreCampo + "','"+descCampo+"');\" style='top: 105px;position: relative;'>";
        enlaceTodo += "<span class='fa fa-expand' id='enlaceTodo' name='enlaceTodo' alt='Maximizar Campo'></span>";
        enlaceTodo += "</A>";   
      
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += "<textarea class='" + estiloCSS + "' style='width:95%;height:130px !important;text-transform: none;' name='" + nombreCampo + "' id='" + nombreCampo + "'" + 
            " title='" + descCampo + "' onchange='modificaVariableCambiosCamposSupl();' onkeyup='return xValidarCaracteres(this);' " + modoLectura + ' ' + disabled + ">" + valor + "</textarea>";
            inputForm += enlaceTodo+"</td></tr></table>";
            inputForm +="</div>";
        }else{                              
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += "<textarea class='" + estiloCSS + "' style='width:95%;height:130px !important;text-transform: none;' name='" + nombreCampo + "' id='" + nombreCampo + "'" + 
            " title='" + descCampo + "' onchange='modificaVariableCambiosCamposSupl();' onkeyup='return xValidarCaracteres(this);' " + modoLectura + ' ' + disabled + ">" + valor + "</textarea>";
            inputForm += enlaceTodo+"</td></tr></table>";
            inputForm +="</div>";
        }        
       
        return inputForm;
        
    } catch(Err){
        alert("Error al obtener el HTML de un campo suplementario de texto con código " + codCampo + ": " + Err.description);
    }
}

function verTexto(id,codigo){
    var texto = $("#"+id).val();
    if (texto!="")
    //var textoTrans = texto.replace(new RegExp("\n","g"), "<br>");
    mostrarVentana(texto,"Contenido de campo suplementario "+codigo);

}

/**
 * Devuelve el código HTML para un campo suplementario de tipo fecha
 * @param: Objeto que contiene la estructura del campo a crear, así como su valor
 */
function getHTMLCampoFecha(campo,mostrarDescTramite){        
    try{ 
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var modoLectura = "";            
        var valor = ''; 
        var id = '';
        var ob = '';
        var estiloCss = '';
        var activar = '';
        var alarmaVencida = false;        
        var valorPlazoActivo = false;
        var disabled = '';
        
        if(campo.obligatorio==1){
            estiloCss = "inputTxtFechaObligatorio";
            ob = "obligatorio";
        }
        else{
            estiloCss = "inputTxtFecha";
        }
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = campo.valorCampo.valorDatoSuplementario;      
            contadorValoresCamposSuplementariosExpediente++;
        }        
        if(campo.valorCampo.valorPlazoActivo!=null && campo.valorCampo.valorPlazoActivo=="1")
            valorPlazoActivo = true;
        
        if(campo.valorCampo.alarmaVencida!=null && campo.valorCampo.alarmaVencida!='' && campo.valorCampo.alarmaVencida)
            alarmaVencida = true;
        
        // Se obtiene la fecha de vencimiento
        if(campo.valorCampo.valorFechaVencimiento) fechaVencimiento = campo.valorCampo.valorFechaVencimiento;
                
        if(campo.soloLectura=="true" || campo.bloqueado=="SI") {
            modoLectura="readonly";
        }
        
        if(gConsultando) {
            disabled = 'disabled';
        }
        
        var nombreCampo = campo.codCampo;                
        if(campo.codTramite!=null && campo.codTramite!=undefined){
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;                
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;                            
        }
                
        if(ob=="obligatorio"){
            id = ob;
        } else {
            id = nombreCampo;
        }                
        
        (campo.campoActivo=="0")?activar="desactivada":activar ="activada";       
        
        var campoFormulario = "<input type='text' id='" + id + "' class='" + estiloCss + "' value='" + valor + "' maxlength=" + campo.tamano + " name='" + nombreCampo + "' " + 
                              "title='" + campo.descCampo + "' " + modoLectura + " " + disabled + " " + 
                              "onkeyup=\"javascript:if(gConsultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);\" " + 
                              "onblur=\"if(!gConsultando) recargarCamposCalculados();javascript:return comprobarFecha(this);\" " +     
                              "onfocus=\"this.select();\" size=10 onchange=\"this.focus();\"/>";
        
        if (!gConsultando) {
            
            if (campo.bloqueado!="SI") {
                campoFormulario += '<a onclick="showCalendar(\'forms[0]\',\'' + id + '\',null,null,null,\'\',\'cal' + id + '\',\'\',null,null,null,null,null,null,null,null,event);cambiosCampoSupl();return false;" ' + 
                    'onblur="javascript: document.getElementById("' + id + '").focus();ocultarCalendario(); return false;" ' + 
                    'style="TEXT-DECORATION: none; " >';
            }
            
            campoFormulario += '<span class=';
            
            
            if (campo.bloqueado=="SI") {
                campoFormulario += '"fa fa-calendar" style="color: grey;"';
            } else {
                campoFormulario += '"fa fa-calendar"';
            }
            
            campoFormulario += ' aria-hidden="true" id="cal" name="cal" alt="' + campo.descCampo + '" ></span>';
            
            if (campo.bloqueado!="SI") {
                campoFormulario += ' </a> ';
            }
        }
            
       // Si hay definida un plazo de fecha, entonces se debe mostrar
       if(campo.plazoFecha!='' && campo.checkPlazoFecha!='' && valorPlazoActivo && !alarmaVencida){
            campoFormulario += "<A style='text-decoration:none;'>";
            campoFormulario += "<span alt='Alarma' title='Alarma' class='fa fa-bell' style id='imagenBoton" + nombreCampo + "' name='imagenBoton" + nombreCampo + "'" + disabled + "></span>";
            campoFormulario += "</A>";            
       }else if(campo.plazoFecha!='' && campo.checkPlazoFecha!='' && valorPlazoActivo && alarmaVencida){    
            campoFormulario += "<A style='text-decoration:none;'>";
            campoFormulario += "<span class='fa fa-bell corAlarmaVencida' alt='Alarma' title='Alarma' id='imagenBoton" + nombreCampo + "' name='imagenBoton" + nombreCampo + "'" + disabled + "></span>";
            campoFormulario += "</A>";                              
       }else{
            campoFormulario += "<A style='text-decoration:none;'>";
            campoFormulario += "<span class='fa' alt='Alarma' title='Alarma' id='imagenBoton" + nombreCampo + "' name='imagenBoton" + nombreCampo + "'></span>";
            campoFormulario += "</A>";                                     
       }

       if(campo.plazoFecha!=undefined && campo.checkPlazoFecha!=undefined && campo.plazoFecha!='' && campo.checkPlazoFecha!=''){
          campoFormulario += "<input type='button' class='botonGeneral' value='" + mensajeEtiquetaAlarma + "' name='cmdVer" + nombreCampo + "' onclick=\"onClickVer('" + nombreCampo + "','" + campo.plazoFecha + "','" + campo.checkPlazoFecha + "','" + campo.campoActivo + "');\"" + disabled + ">";                                 
       }
           
       if(campo.plazoFecha!=undefined && campo.checkPlazoFecha!=undefined && campo.plazoFecha!='' && campo.checkPlazoFecha!=''){                      
           if(valorPlazoActivo=="0"){ // Está desactivada la alarma, se da la posibilidad de activarla
                                      // Si está desactivada la alarma, habrá que activarla               
               campoFormulario += "<A id='enlaceActivar" + id + "' style=\"text-decoration:none;visibility:inline;display:inline;\" onclick=\"onClickActivar('" + id + "');\">";
               campoFormulario += "<span class='fa fa-times-circle' id='cmdActivar" + id + "' name='cmdActivar" + nombreCampo + "' title='" + mensajeEtiquetaActivar + "'" + disabled + "></span>";
               campoFormulario += "</A>";      
               
               campoFormulario += "<A id='enlaceDesactivar" + id + "' style=\"text-decoration:none;visibility:none;display:none;\" onclick=\"onClickDesactivar('" + id + "');\">";
               campoFormulario += "<span class='fa fa-check' id='cmdDesactivar" + id + "' name='cmdDesactivar" + nombreCampo + "' title='" + mensajeEtiquetaDesactivar + "'" + disabled + "></span>";
               campoFormulario += "</A>"; 
               
           }else
           if(valorPlazoActivo=="1"){ // Está activada la alarma, se da la posibilidad de desactivarla               
               campoFormulario += "<A id='enlaceActivar" + id + "' style=\"text-decoration:none;visibility:none;display:none;\" onclick=\"onClickActivar('" + id + "');\">";
               campoFormulario += "<span class='fa fa-times-circle' id='cmdActivar" + id + "' name='cmdActivar" + nombreCampo + "' title='" + mensajeEtiquetaActivar + "'" + disabled + "></span>";
               campoFormulario += "</A>";
                              
               campoFormulario += "<A id='enlaceDesactivar" + id + "' style=\"text-decoration:none;visibility:inline;display:inline;\" onclick=\"onClickDesactivar('" + id + "');\">";
               campoFormulario += "<span class='fa fa-check' id='cmdDesactivar" + id + "' name='cmdDesactivar" + id + "' title='" + mensajeEtiquetaDesactivar + "'" + disabled + "></span>";
               campoFormulario += "</A>";                     
           }
       }         
  
       campoFormulario += "<input type='text' style=\"visibility:hidden;display:none\" id='activar" + id + "' name='activar" + nombreCampo + "' value='" + activar + "' size=10 maxlength=10 readonly=true/>";                
       
       
       var inputForm ='';
       var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
             
       if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";       
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += campoFormulario;
            inputForm +="</td></tr></table>";
            inputForm += "</div>";
       }else{                  
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";                                
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";       
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += campoFormulario;
            inputForm +="</td></tr></table>";
            inputForm += "</div>";
       }       
       return inputForm;
        
    } catch(Err){
        alert("Error al obtener el HTML de un campo suplementario de texto con código " + codCampo + ": " + Err.description);
    }
} 




/**
 * Devuelve el código HTML para un campo suplementario de tipo fecha calculado
 * @param: Objeto que contiene la estructura del campo a crear, así como su valor
 */
function getHTMLCampoFechaCalculado(campo,mostrarDescTramite){        
    try{ 
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var tamano        = campo.tamano;        
        var modoLectura = "";            
        var valor = ''; 
        var id = '';
        var ob = '';
        var estiloCss = '';
        var disabled = '';
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = campo.valorCampo.valorDatoSuplementario;       
            contadorValoresCamposSuplementariosExpediente++;
        }        
        
        modoLectura="readonly";          
        if(gConsultando) disabled = 'disabled';
        
        if(campo.obligatorio==1){
            estiloCss = "inputTxtFechaObligatorio";
            ob = "obligatorio";
        } else{
            estiloCss = "inputTxtFecha";
        }
              
        var nombreCampo = campo.codCampo;
        if(campo.codTramite!=null && campo.codTramite!=undefined){
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;                
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;                            
        }
        
        if(ob=="obligatorio") {
            id = ob;
        } else {
            id = nombreCampo;
        }
       
        var campoFormulario = "<input type='text' id='" + id + "' class='" + estiloCss + "' value='" + valor + "' maxlength=" + tamano + " name='" + nombreCampo + "' title='" + descCampo + "' " + modoLectura + " onkeyup=\"javascript:if(gConsultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);\"";
        campoFormulario += "onblur=\"javascript:return comprobarFecha(this);\" onfocus=\"this.select();\"" + ' ' + disabled + ">";
        
        if(campo.soloLectura!="true" && campo.bloqueado!="SI"){         
            campoFormulario += "<input type='button' class='botonGeneral' value='Ver..' name='cmdVerOpe_" + nombreCampo + "' onclick=\"pulsarVerExpre('" + nombreCampo + "');\" style=\"width:50px;height:17px;display:inline;\">";
        }
         
        var inputForm ='';
        var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
        
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>"; 
            inputForm +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += campoFormulario;
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
        }else{                              
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>"; 
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += campoFormulario;
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
        }        
        return inputForm;
        
    } catch(Err){
        alert("Error al obtener el HTML de un campo suplementario de fecha calculado con código " + codCampo + ": " + Err.description);
    }
} 


function getHTMLCampoDesplegable(campo,mostrarDescTramite){
    try{ 
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var obligatorio   = campo.obligatorio;
        var id = '';
        var estiloCSS = '';
        var disabled = '';                      
        var nombreCampo = campo.codCampo;        
        var disabled = '';
        
        var campoLargo=false;	
        var tamanoVista	
        
        
        for(propiedad in campo.listaDescDesplegable){
              var descripcion=limpiarCadena(campo.listaDescDesplegable[propiedad]);             
              if(descripcion.length>=50){	
                campoLargo=true;	
                break	
            }
         }
            
	
        if(campoLargo){	
            tamanoVista=100;	
        }else{	
            tamanoVista=50;	
        }
        
        if(campo.codTramite!=null && campo.codTramite!=undefined){            
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;
        }
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            contadorValoresCamposSuplementariosExpediente++;
        }
  
        if(campo.obligatorio==1){
            id = obligatorio;            
            estiloCSS = 'inputTextoObligatorio';
        }else{
            id = nombreCampo;
            estiloCSS = 'inputTexto';
        }
        
        if(campo.bloqueado==1 || gConsultando){
            estiloCSS="inputTexto inputTextoDeshabilitado";
            disabled = "disabled";
        }
        
        var inputCodigoCombo = "<input type='text' id='" + id + "' name='cod" + nombreCampo + "' onkeyup='return xAMayusculas(this);' class='" + estiloCSS + "' style='width: 20%' maxlength=8 " + disabled + ">";
       var inputDescCombo   = "&nbsp;<input type='text' id='desc" + nombreCampo + "' name='desc" + nombreCampo + "' " + disabled + " class='" + estiloCSS + "' style='width:70%' readonly='true'>";
        var inputAnchorCombo1 = "<A href='' id='anchor" + nombreCampo + "' name='anchor" + nombreCampo + "' style='text-decoration:none;padding-right: 15px;'" + ' ' + disabled + ">";
        var inputAnchorCombo2 = "<span class='fa fa-chevron-circle-down' aria-hidden='true' id='boton" + nombreCampo + "' name='boton" + nombreCampo + "' style='cursor:hand;'" + ' ' + disabled + "></a>";
        
        var inputForm ='';        
        var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;       
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" +tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += inputCodigoCombo + inputDescCombo + inputAnchorCombo1 + inputAnchorCombo2
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
        }else{                  
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" +tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += inputCodigoCombo + inputDescCombo + inputAnchorCombo1 + inputAnchorCombo2
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
        }    
       
       
       
        return inputForm;
                
    } catch(Err){
        alert("Error al obtener el HTML de un campo suplementario desplegable con código " + codCampo + ": " + Err.description);
    }
}


/**
 * Devuelve el código HTML para un campo suplementario de tipo numérico calculado
 * @param: Objeto que contiene la estructura del campo a crear, así como su valor
 */
function getHTMLCampoNumericoCalculado(campo,mostrarDescTramite){    
   try{          
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var tamano        = campo.tamano;        
        var activo        = campo.activo;         
        var posX          = campo.posX;
        var posY          = campo.posY;                    
        var estiloCss     = '';
        var modoLectura   = '';            
        var valor = ''; 
        var id = '';
        var ob = '';
        var disabled = '';
        
        modoLectura="readonly";        
        // Si se están consultando, entonces se deshabilita el campo numérico calculado
        if(gConsultando) disabled='disabled';            
   
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = formatValorDecimal(campo.valorCampo.valorDatoSuplementario);
            contadorValoresCamposSuplementariosExpediente++;
        }

        var nombreCampo = campo.codCampo;
        if(campo.codTramite!=null && campo.codTramite!=undefined){
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;            
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;            
        }
        
        if(campo.obligatorio=="1") {
            estiloCss = ESTILO_CSS_INPUT_OBLIGATORIO;
            ob = "obligatorio";
        } else {
            estiloCss = ESTILO_CSS_INPUT_NORMAL;
        }
                
        if(ob=="obligatorio"){
            id = ob;
        } else {
            id = nombreCampo;
        }
        
        var fcampo="<input type='text' name='" + nombreCampo + "' value='" + valor + "' maxlength=" + campo.tamano + " id='" + id + "' class='" + estiloCss + "' " + 
                 "title='" + campo.descCampo + "'"  + modoLectura + " onchange='cambiosCampoSupl();' size=20 onfocus='javascript:desFormatValorDecimal(this," + campo.soloLectura + ");' " + 
                 "onkeypress='javascript:if(gConsultando) return SoloDigitosConsultaCampoNumericoCal(event); else return SoloDigitosNumericosCampoNumericoCal(event);' " + 
                 "onBlur='javascript:validarLongitudNumeroCal(this); focus(this);'" + modoLectura + ' ' + disabled + "/>" + 
                 "<input type='button' class='botonGeneral' value='Ver..' name='cmdVerOpe_" + nombreCampo + "' style='width:50px;height:17px;' onclick='pulsarVerExpre(\"" + nombreCampo + "\");' />";        
                
        var inputForm = '';
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += fcampo;
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
       }else{                              
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += fcampo;
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
       }        
        
       return inputForm;
        
    }catch(Err){
        alert("Error al obtener el HTML de un campo suplementario numérico calculado con código " + codCampo + ": " + Err.description);
    }
}



/**
 * Devuelve el código HTML para un campo suplementario desplegable exerno
 * @param: Objeto que contiene la estructura del campo a crear, así como su valor
 */
function getHTMLCampoDesplegableExterno(campo,mostrarDescTramite){    
   try{          
        var codCampo      = campo.codCampo;
        var descCampo     = campo.descCampo;    
        var estiloCss     = '';
        var modoLectura   = '';            
        var valor = ''; 
        var codigo = '';
        var id = '';
        var ob = '';
        var disabled = '';
        
        if(gConsultando) disabled = 'disabled';        
        if(campo.soloLectura =="true"){
             modoLectura = "readonly";
        }  
        if(campo.bloqueado=="SI"){
            modoLectura = "readonly";
        }
        
        if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!=""){
            valor = campo.valorCampo.valorDatoSuplementario;
            contadorValoresCamposSuplementariosExpediente++;
        }
        
       if(campo.valorCampo.codValorDatoSuplementario!=null && campo.valorCampo.codValorDatoSuplementario!=""){
            codigo = campo.valorCampo.codValorDatoSuplementario;
        }


        var nombreCampo = campo.codCampo;        
        if(campo.codTramite!=null && campo.codTramite!=undefined){
            if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;            
            }else
                nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;            
        }

        if(campo.obligatorio=="1") {
            estiloCss = ESTILO_CSS_INPUT_OBLIGATORIO;
            ob = "obligatorio";
        } else {
            estiloCss = ESTILO_CSS_INPUT_NORMAL;
        }
                
        if(ob=="obligatorio"){
            id = ob;
        } else {
            id = nombreCampo;
        }

        // #208822 input para mostrar el codigo del valor seleccionado
        var fcampo = "<input type='text' name='" + nombreCampo + "_CODSEL' value='" + codigo + "' id='" + id + "_CODSEL' class='" + estiloCss + "'";
        fcampo += "title='" + campo.descCampo + "' style='width:110px; margin-right:4px;' size='10' readonly >";
        fcampo += "<input type='text' name='" + nombreCampo + "' value='" + valor + "' maxlength=" + campo.tamano;

        fcampo += " id=" + id + " class='" + estiloCss + "' onchange=\"cambiosCampoSupl()\" title='" + campo.descCampo + "' " + modoLectura + ' ' + disabled;
        fcampo += " style=\"width:350px;text-transform: none\" onkeyup=\"return xValidarCaracteres(this);\" onblur=\"javascript:if (!validarLongitudTexto(this)) {document.getElementById('" + nombreCampo + "').value = '';focus(this);}\">";
        if(modoLectura!="readonly"){
            fcampo += "<input type='button' class='botonGeneral' value='Seleccionar' name='cmdVerExterno_" + nombreCampo + "' onclick=\"pulsarVerExterno('" + nombreCampo + "');\" style=\"width:90px;height:17px\"" + ' ' + disabled + ">";
        }
                
        var inputForm = '';              
        if(campo.descripcionTramite==undefined || campo.descripcionTramite==''){
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += fcampo;
            inputForm +="</td></tr></table>";
            inputForm +="</div>";            
        }else{                  
            var idTabla = "T" + campo.codTramite + campo.ocurrencia + campo.codCampo;
            inputForm +="<div id='campo_" + campo.codCampo + "' style='width:" + campo.tamanoVista + "%'>";
            if(mostrarDescTramite)
                inputForm +="<div class='deTramite'> Trámite " + campo.descripcionTramite + "</div>";
            inputForm +="<table id='" + idTabla + "' name='" + idTabla + "' width='100%' border='0px' cellspacing='2px' cellpadding='1px'>";        
            inputForm +="<tr><td class='deTramite' style='width:3%;text-transform:capitalize;'>&nbsp;</td><td class='etiqueta' style='text-transform:capitalize;' width='150px' align='left'>";
            inputForm += descCampo + "</td>";        
            inputForm += "<td class='columnP' align='left'>"            
            inputForm += campo;
            inputForm +="</td></tr></table>";
            inputForm +="</div>";
        }       
        
        return inputForm;
        
    }catch(Err){
        alert("Error al obtener el HTML de un campo suplementario numérico calculado con código " + codCampo + ": " + Err.description);
    }
}
 
 
 /**
  * Crea la tabla que contendrá las agrupaciones con sus campos suplementarios correspondientes
  */ 
 function getCabeceraTablaAgrupacionesDatosSuplementarios(){     
     return "<table id='tablaDatosSuplementariosAgrupaciones' class='contenidoPestanha'>";     
 }
 
 function getDivAgrupacionDatosSuplementarios(codAgrupacion,descAgrupacion){
    var salida = ""; 
    if(codAgrupacion!="DEF"){         
        salida += "<tr><td class='sub3titulo'>&nbsp;" + descAgrupacion + "</td></tr>";    
    }          
    salida += "<TR style='padding-bottom:5px'>";
    salida += "<TD style='width: 100%' valign='top'>";
    salida += "<DIV id='capaDatosSuplementarios_" + codAgrupacion + "' name='capaDatosSuplementarios_" + codAgrupacion + "' ";
    salida += "style='border-bottom: 1px dashed #1766A7; border-right: 1px dashed #1766A7;border-left: 1px dashed #1766A7;width:99.8%;";
    salida += "position: relative'>";
 
    return salida;
 }
 
    function estanCargadosCamposSuplementarios(){
        return cargadoCamposSuplementarios;
    }
        
    function cargarCamposSuplementarios(){ 
        pleaseWait('on');
        try{            
            var codMunicipio  = $("#codMunicipio").val();            
            var ejercicio     = $("#ejercicio").val();            
            var numExpediente = $("#numero").val();
            var codProcedimiento = $("#codProcedimiento").val();
            var expHistorico = $("#expHistorico").val();
            
            $.ajax({
                 url: APP_CONTEXT_PATH + '/expediente/cargaFicha.do',
                 type: 'POST',
                 async: true,
                 data: 'codProcedimiento=' + codProcedimiento + '&codMunicipio=' + codMunicipio + '&ejercicio=' + ejercicio + 
                         '&numero=' + numExpediente + '&opcion=cargarDatosSuplementarios' + '&expHistorico=' + expHistorico,
                 success: procesarRespuestaListadoCamposSuplementarios,
                 error: muestraErrorRespuestaListadoCamposSuplementarios
             });                 
             return cargadoCamposSuplementarios;
        }catch(Err){            
            pleaseWait('off');
            mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_DATOS_SUPLEMENTARIOS);
        } 
    }
  
 function procesarRespuestaListadoCamposSuplementarios(ajaxResult){              
    try{     
        
        var listaCamposDesplegables  = new Array();        
        var resultado                = JSON.parse(ajaxResult);                        
        var agrupaciones             = resultado.agrupaciones;                
        var contadorCamposCalculados = 0;     
        var contadorCampos           = 0;
        var salida;                
           
        // Se vacía el contenido del DIV en el que se mostrarán los datos suplementarios
        $('#capaDatosSuplementarios').html('');
        // Se almacena en el campo oculto "jsonEstructuraCamposSuplementarios" la estructura de los campos 
        // suplementarios en formato JSON. De modo al grabar, la estructura podrá ser recuperada desde el Action y 
        // parseada a la colección de objetos EstructuraCampo, para proceder a grabar los valores de los campos
        $('#jsonEstructuraCamposSuplementarios').val(ajaxResult);
           
        contadorValoresCamposSuplementariosExpediente = 0;   
        nombreCamposRecargarCalculados = "";
        tiposCamposRecargarCalculados = "";
        salida = getCabeceraTablaAgrupacionesDatosSuplementarios();       
                     
        // Se recorre cada agrupación y se recupera la estructura de campos de cada de ellas.
        // Por cada campo, se recupera además su valor
        for(var i=0;agrupaciones!=null && i<agrupaciones.length;i++){        
            var codAgrupacion  = agrupaciones[i].codAgrupacion;
            var descAgrupacion = agrupaciones[i].descAgrupacion;
            var estructura     = agrupaciones[i].estructura;            
            rellenarAgrupaciones(agrupaciones[i]);  
            
            if(estructura!=undefined && estructura.length>0)
                salida += getDivAgrupacionDatosSuplementarios(codAgrupacion, descAgrupacion);
                        
            var campoFormulario ='';           
            var codTramiteActual = '';
            var codTramiteTramiteAnterior = '';
            
            
            for(var j=0;estructura!=undefined && j<estructura.length;j++){                            
                var campoInicial  = estructura[j];                                   
                var codTipoDato   = campoInicial.codTipoDato;
                contadorCampos++;       
                
                var tituloTramite = false;
                if(codAgrupacion=='DVT'){
                    codTramiteActual = campoInicial.codTramite;
                    if(codTramiteActual!=codTramiteTramiteAnterior){
                        // Si la agrupación es de trámite y el campo pertenece a un trámite distinto del anterior, se añade una 
                        // fila para indicar el nombre del trámite                        
                        codTramiteTramiteAnterior = codTramiteActual;
                        tituloTramite = true;
                    }
                }

                if (campoInicial.codTramite == null || campoInicial.codTramite==undefined){                    
                    if (nombreCamposRecargarCalculados==""){
                        nombreCamposRecargarCalculados = nombreCamposRecargarCalculados + campoInicial.codCampo ;
                        tiposCamposRecargarCalculados  = tiposCamposRecargarCalculados  + campoInicial.codTipoDato;
                    }
                    else{
                        nombreCamposRecargarCalculados = nombreCamposRecargarCalculados + "#" + campoInicial.codCampo;
                        tiposCamposRecargarCalculados  = tiposCamposRecargarCalculados + "#" + campoInicial.codTipoDato;
                    }                    
                }                
                
                rellenarPosicionesCampos(campoInicial);                
                switch(codTipoDato){
                    case "1": // Numérico                            
                        campoFormulario += getHTMLCampoNumerico(campoInicial,tituloTramite);                                                    
                        break;                        
                    case "2": // Texto                                                   
                        campoFormulario += getHTMLCampoTexto(campoInicial,tituloTramite);
                        break;                           
                    case "3": // Fecha                           
                        campoFormulario += getHTMLCampoFecha(campoInicial,tituloTramite);
                        break;           
                    case "4": // Texto largo
                        campoFormulario += getHTMLCampoTextoLargo(campoInicial,tituloTramite);
                        break;
                    case "5": // Fichero
                        campoFormulario += getHTMLCampoFichero(campoInicial,tituloTramite);
                        break;
                    case "6": // Desplegable
                         campoFormulario += getHTMLCampoDesplegable(campoInicial,tituloTramite);
                         listaCamposDesplegables.push(campoInicial);
                         break;
                    case "8": // Numérico calculado
                        campoFormulario += getHTMLCampoNumericoCalculado(campoInicial,tituloTramite);    
                        contadorCamposCalculados++;
                        break;
                    case "9": // Fecha calculado
                        campoFormulario += getHTMLCampoFechaCalculado(campoInicial,tituloTramite);
                        contadorCamposCalculados++;
                        break;                    
                    case "10": // Desplegable externo
                        campoFormulario += getHTMLCampoDesplegableExterno(campoInicial,tituloTramite);
                        break;                        
                }// switch         
             }// for
                    
             //salida += campoFormulario + "</td></tr>";             
             salida += campoFormulario + "</div></td></tr>";             
        }// for
        
        salida += "</table>";                
        if(contadorCampos>0){
            $('#capaDatosSuplementarios').append(salida);    
        
            // Se generan los combos por cada uno de los campos desplegables
            generarCombos(listaCamposDesplegables);
            // Si hay cambios calculados, se llama a la función recargarCamposCalculados para mostrar su valor
            if(contadorCamposCalculados>0) recargarCamposCalculados();
            cambiarColorFondoPestanaCampoSuplementarios();                        
            try{            
                // Se posicionan los campos suplementarios si procede            
                var posicionar = resultado.posicionarCamposSuplementariosExpediente;            
                if(posicionar==1){                          
                    posicionarCamposPestanaDatosSuplementarios();
                } 
            }catch(Err){            
                alert("Error al posicionar los campos: " + Err.description);
            }
            
        }else            
            $('#capaDatosSuplementarios').append(mostrarDIVNoHayCamposSuplementarios());    
           
        // Se pone a true la variable, para que al hacer click sobre la pestaña "Datos Suplementarios", no se 
        // vuelva a recargar la pestaña
        cargadoCamposSuplementarios = true;        
        pleaseWait('off');      
        
    }catch(Err){        
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_DATOS_SUPLEMENTARIOS);
    }
 }
 
 
 function mostrarDIVNoHayCamposSuplementarios(){     
     return "<div class='divMensajeNoCamposSuplementarios'>" + mensajeNoDatosSuplementarios + "</div>";
 }
 
  /**
     * Función que es llamada para cambiar el color de fondo de la pestaña de los datos suplementarios.
     * Para ello tiene en cuenta el que el valor de la variable global contadorValoresCamposSuplementariosExpediente, sea
     * mayor que cero, que es el número de campos suplementarios a mostrar en la pestaña "Datos Suplementarios"
     **/
  function cambiarColorFondoPestanaCampoSuplementarios(){
     
     if(contadorValoresCamposSuplementariosExpediente>0){         
        var cssClass = $("#pestana2").attr("class");
        
        if(cssClass!=undefined && cssClass!='' && (cssClass=="tab selected" || cssClass=="tab-remark selected")){
            $('#pestana2').addClass('tab-remark selected');        
        }else 
            $('#pestana2').addClass('tab-remark');
     }
 }

 /**
  * Eliminar del parámetro cadena las comillas simples que hay al principio y al final del string
  * @param cadena
  * @return cadena limpia
  */
 function limpiarCadena(cadena){
     return cadena.substring(1,cadena.length-1);
 }
 
 function generarCombos(listaCombos){
    var codigos      = new Array();
    var descripciones = new Array();
    var estados = new Array();
    var idioma = $("#idioma").val();    
    try{
    
        for(i=0;listaCombos!=undefined && i<listaCombos.length;i++){
            var campo = listaCombos[i];        
            var nombreCampo = campo.codCampo;        

            if(campo.codTramite!=null && campo.codTramite!=undefined){            
                if(campo.ocurrencia!=null && campo.ocurrencia!=undefined){
                    nombreCampo = "T_" + campo.codTramite + "_" + campo.ocurrencia + "_" + campo.codCampo;
                }else
                    nombreCampo = "T_" + campo.codTramite + "_" + campo.codCampo;
            }

            // Se obtiene el valor del desplegable
            var valor='';        
            if(campo.valorCampo.valorDatoSuplementario!=null && campo.valorCampo.valorDatoSuplementario!="")
                valor = campo.valorCampo.valorDatoSuplementario;

            // Se obtiene la lista con los códigos del desplegable
            for(propiedad in campo.listaCodDesplegable){
                codigos.push(limpiarCadena(campo.listaCodDesplegable[propiedad]));
            }

            // Se obtiene la lista con las descripciones de los elementos del desplegable
            for(propiedad in campo.listaDescDesplegable){
                descripciones.push(limpiarCadena(campo.listaDescDesplegable[propiedad]));
            }
            
            for(propiedad in campo.listaDescDesplegable){
                estados.push(limpiarCadena(campo.listaEstadoValorDesplegable[propiedad]));	
            }

             var combo;	
            if(idioma !=undefined && idioma!=null){	
                combo = new Combo(nombreCampo,idioma);	
            }else{	
                combo = new Combo(nombreCampo);	
            }      
            combo.addItems2(codigos,descripciones,estados);
            if(valor!=undefined && valor!='') combo.buscaLinea(valor);
            combo.change = modificaVariableCambiosCamposSupl;

            if(campo.soloLectura=="true" || campo.bloqueado=="SI"){         
                combo.deactivate();
            }
            
            if(gConsultando){                
                // Si se está consulntando
               combo.deactivate();
            }         
            
            // Se añade el combo en el array de combos
            nombreCombos.push(combo);
            campo = null;
            codigos = new Array();
            descripciones = new Array();
            estados= new Array();

         }// for     
     }catch(Err){
         alert(" Error en función generarCombos(): " + Err.description);
     }
 }
 

function getNombreCompletoInteresado(nombre,apellido1,apellido2,masInteresados){
    var nombreCompleto = "";
    try{
        if(apellido1!=null && apellido1!=undefined && apellido1.length>0 && apellido1!="")
            nombreCompleto +=apellido1 + nombreCompleto;
        
        if(apellido2!=null && apellido2!=undefined && apellido2.length>0 && apellido2!="")
            nombreCompleto += " " + apellido2;
        
        if(nombre!=null && nombre!=undefined && nombre.length>0){            
            if(nombreCompleto.length==0 || nombreCompleto=="")
                nombreCompleto += nombre;
            else
                nombreCompleto += ", " + nombre;
        }
        
        if(masInteresados)
            nombreCompleto += " Y OTROS";
        
    }catch(Err){
        alert("Error al generar el nombre completo del interesado de una anotación de registro: " + nombreCompleto);
    }        
    return nombreCompleto;
}



function getDescripcionCortaAsuntoAsiento(descripcion){
    var salida = descripcion;
    
    if (descripcion!=null && descripcion!="" && descripcion.length>40)
         salida = descripcion.substring(0,40) + "...";
     
    return salida;    
}

function muestraErrorRespuestaOtrosDatosExpediente(result){
     mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_OTROS_DATOS_EXPEDIENTE);
}

function muestraErrorRespuestaListadoComunicacionesExpediente(result){
    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_COMUNICACIONES_ELECTRONICAS);
}

function muestraErrorRespuestaListadoNotificacionesExpediente(result){
    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_NOTIFICACIONES_ELECTRONICAS);
}

function muestraErrorRespuestaEliminarDocumentoExterno(result){    
    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_ELIMINAR_DOCUMENTO_EXTERNO);
}
         
function muestraErrorRespuestaGenerarCSVDocumento(result){    
    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CREAR_CSV_DOCUMENTO);
}

function muestraErrorRespuestaListadoDocumentosExternos(result){  
    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_DOCUMENTOS_EXTERNOS);
}

function muestraErrorRespuestaListadoCamposSuplementarios(result){
    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_DATOS_SUPLEMENTARIOS);    
}

// Muestra un mensaje de error. Se le pasa el código del campo de formulario y el tamaño máximo para su contenido
function validarLongitudTexto(campo) {        
    var valor = campo.value;
    if (valor!=''){            
        if (!(valor.length<=tamanoMaximoCampoTexto)) {                          
            jsp_alerta("A",msjTamMaxCampoTexto);
            return false;            
        }else return true;          
    }else
        return true;
}


/**
 * FUNCIONES USADAS POR LOS CAMPOS SUPLEMENTARIOS NUMÉRICOS CALCULADOS
 */
 function SoloDigitosNumericosCampoNumericoCal(e) {
      var tecla, caracter;
      var numeros= "0123456789,";

      tecla = (document.all)?e.keyCode:e.which;
      if (tecla == null) return true;
      if ((tecla == 0)|| (tecla == 8)) return true;

      caracter = String.fromCharCode(tecla);
      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }

  function SoloDigitosConsultaCampoNumericoCal(e) {
      var tecla, caracter;
      var numeros= "0123456789&|():><!=,";

      tecla = (document.all)?e.keyCode:e.which;

      if (tecla == null) return true;
      if ((tecla == 0)|| (tecla == 8)) return true;

      caracter = String.fromCharCode(tecla);

      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }


function validarLongitudNumeroCal(campo) {
    var valor = campo.value;
    longitudvalida = longitudValidaValorCal(valor);
}

function longitudValidaValorCal(valor) {
    var devolver = true;
    var limiteMaximo = tamanoMaximoCampoNumerico -3;
    
    if (valor != '')
    {
      // si no hay comas
      if (valor.indexOf(",")==-1 && valor.indexOf(":")==-1 && valor.indexOf(">")==-1 && valor.indexOf("<")==-1) {
        // quitar los ceros a la izquierda
        valor = trimLeftZeroes(valor);
        devolver=(valor.length<=limiteMaximo);
      }
      // si hay comas
      else  if (valor.indexOf(",")!=-1){
        pos = valor.lastIndexOf(",");
        intPart = valor.substring(0,pos);
        decPart = valor.substring(pos+1,valor.lenght);              
        if (intPart==""){
            intPart = "0";
        }
        // quitar los puntos y comas
        intPart = trimPointers(intPart);
        // quitar los ceros a la izquierda
        intPart = trimLeftZeroes(intPart);
        if (decPart.length > 2){
            devolver = "";
        }else{
            devolver=(intPart.length<=limiteMaximo);
        }            
      } else if (valor.indexOf(":")!=-1){
          var los2numeros = valor.split(':');
          devolver = longitudValidaValor(los2numeros[0]) && longitudValidaValor(los2numeros[1]);
      } else if (valor.indexOf(">")==-1 || valor.indexOf("<")==-1) {
          devolver = devolver=((valor.length+1)<=limiteMaximo);
      }
      if (!devolver) {
          
       var numMaximo = "";
        for (var i=1;i<=limiteMaximo;i++) {
            numMaximo += "9";
        }
        numMaximo = numMaximo + "'99";
        
        var mensaje = mensajeTamanhoMaximoCampoNumerico.replace('{0}',numMaximo);
          
        jsp_alerta("A",mensaje);
      }
      return devolver;
    }
}

function cambiosCampoSupl(){
    if (gConsultando!=true) modificaVariableCambiosCamposSupl();
}



function SoloDigitosNumericosCampoNumerico(e) {

      var tecla, caracter;
      var numeros= "0123456789,";

      tecla = (document.all)?e.keyCode:e.which;

      if (tecla == null) return true;
      if ((tecla == 0)|| (tecla == 8)) return true;


      caracter = String.fromCharCode(tecla);

      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }

  function SoloDigitosConsultaCampoNumerico(e) {


      var tecla, caracter;
      var numeros= "0123456789&|():><!=,";

      tecla = (document.all)?e.keyCode:e.which;

      if (tecla == null) return true;
      if ((tecla == 0)|| (tecla == 8)) return true;

      caracter = String.fromCharCode(tecla);

      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }

function validarLongitudNumero(campo,nombre) {    
    var valor = campo.value;
    longitudvalida = longitudValidaValor(valor);
    if (longitudvalida == false)
    {
        document.getElementById(nombre).value = ""; 
        focus(campo)
    }
}

function longitudValidaValor(valor) {
    var devolver = true;    
    var limiteMaximo = tamanoMaximoCampoNumerico -3;
    
    
    if (valor != '')
    {
      // si no hay comas
      if (valor.indexOf(",")==-1 && valor.indexOf(":")==-1 && valor.indexOf(">")==-1 && valor.indexOf("<")==-1) {
        // quitar los ceros a la izquierda
        valor = trimLeftZeroes(valor);
        devolver=(valor.length<=limiteMaximo);
      }
      // si hay comas
      else  if (valor.indexOf(",")!=-1){
        pos = valor.lastIndexOf(",");
        intPart = valor.substring(0,pos);
        decPart = valor.substring(pos+1,valor.lenght);        
        if (intPart==""){
            intPart = "0";
        }
        // quitar los puntos y comas
        intPart = trimPointers(intPart);
        // quitar los ceros a la izquierda
        intPart = trimLeftZeroes(intPart);
        
        if (decPart.length > 2){
            devolver = false;
        }else{
            devolver=(intPart.length<=limiteMaximo);
        }
        
      } else if (valor.indexOf(":")!=-1){
          var los2numeros = valor.split(':');
          devolver = longitudValidaValor(los2numeros[0]) && longitudValidaValor(los2numeros[1]);
      } else if (valor.indexOf(">")==-1 || valor.indexOf("<")==-1) {
          devolver = devolver=((valor.length+1)<=limiteMaximo);
      }
      if (!devolver) {
            
           
        var numMaximo = "";
        for (var i=1;i<=limiteMaximo;i++) {
            numMaximo += "9";
        }
        numMaximo = numMaximo + "'99";
        
        var mensaje = mensajeTamanhoMaximoCampoNumerico.replace('{0}',numMaximo);
        jsp_alerta("A",mensaje);
      }
            
      return devolver;
    }
}

 function rellenarPosicionesCampos(objetoCampo){                
    var campo = new Array();
    campo[0] = objetoCampo.codCampo;
    campo[1] = objetoCampo.posicionar;
    campo[2] = objetoCampo.posX;
    campo[3] = objetoCampo.posY;
    campo[4] = objetoCampo.codAgrupacion;
    campo[5] = objetoCampo.codTipoDato;
    listaCampos.push(campo);        
 }
 
 
 function rellenarAgrupaciones(objetoAgrupacion){
    var agrupacion = new Array();
    agrupacion[0] = objetoAgrupacion.codAgrupacion;
    agrupacion[1] = objetoAgrupacion.ordenAgrupacion;     
    listaAgrupaciones.push(agrupacion);     
 }
       
        
function posicionarCamposPestanaDatosSuplementarios(){                             
        
    try{
        for (i=0; i < listaCampos.length; i++) {
            var campo = new Array();
            campo = listaCampos[i];
            var nomCampo = "campo_" + campo[0];
            var nomAgrupacion = "capaDatosSuplementarios_" + campo[4];
            var posX = campo[2];
            var posY = campo[3];
                
            if(posX != undefined && posX != null && posX != "" && posX !=  " "){
                if(posY != undefined && posY != null && posY != "" && posY !=  " "){
                    if(document.getElementById(nomCampo)){        
                        
                        var posParent = $('#'+nomAgrupacion).offset();
                        var posParent = $('#'+nomAgrupacion).offset();                    
                        posX = parseInt(posX) + parseInt(posParent.left);
                        posY = parseInt(posY) + parseInt(posParent.top);                            
                        $('#'+nomCampo).css('z-index', i+1);
                        if (posX<100){ 
                            $('#'+nomCampo).css('float', 'left');
                            $('#'+nomCampo).css('clear', 'left');
                        }
                        else  $('#'+nomCampo).css('float', 'right');

                        $('#'+nomCampo).offset({ top: posY, left: posX, of: nomAgrupacion});      
                        $('#'+nomCampo).css("position", "static");                       
                        
                    }                
                }
            }
        }

        for (x=0; x < listaAgrupaciones.length; x++) {
            var agrupacion = new Array();
                agrupacion = listaAgrupaciones[x];            
            calcularAltoAgrupacion(agrupacion[0]);
        } 
    
    }catch(Err){
        alert("ERROR AL POSICIONAR CAMPOS: " + Err.message);        
    }
    
}


function camposSuplementariosVistaExpediente(){    
    var numExpediente = $("#numero").val();
    try{

        if(numExpediente!=undefined && numExpediente!=null && numExpediente!=''){
            $.ajax({
                url: APP_CONTEXT_PATH + '/expediente/cargaFicha.do',
                type: 'POST',
                async: true,
                data: 'numero=' + numExpediente + '&opcion=cargarCamposExpedienteActualizarEnFicha',
                success: procesarRespuestaActualizarCamposSuplementarios,                
                error: muestraErrorRespuestaActualizarCamposSuplementarios
            });    
        }
    }catch(Err){
        pleaseWait('off');
        mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_ACTUALIZAR_CAMPOS_NUMERICOS_ALARMAS);            
    }
}


function muestraErrorRespuestaActualizarCamposSuplementarios(){
    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_ACTUALIZAR_CAMPOS_NUMERICOS_ALARMAS);    
}

function procesarRespuestaActualizarCamposSuplementarios(ajaxResult){        
    if(ajaxResult!=undefined && ajaxResult!=null){
        var lista = JSON.parse(ajaxResult);

        for(i=0;lista!=undefined && i<lista.length;i++){
            var objeto = lista[i];                
            switch(objeto.tipoDatoSuplementario){
                case 1: actualizarValorCampoNumerico(objeto.codDatoSuplementario,objeto.valorDatoSuplementario);
                        break;                 
                case 3: actualizarAlarma(objeto);
                        break;
            }
        }        
        jsp_alerta('A',mensajeExitoGrabacionExpediente);
    }
}


function actualizarValorCampoNumerico(codCampo,valor){   
    $('#' + codCampo).val(formatValorDecimal(valor));        
}

function actualizarAlarma(objeto){        
    
    try{        
        if(objeto!=undefined && objeto!=null && objeto.valorPlazoActivo!=undefined && objeto.valorPlazoActivo!=null && objeto.valorPlazoActivo=="1"){        
            if(objeto.alarmaVencida)
                $("#imagenBoton" + objeto.codDatoSuplementario).attr("class","fa fa-bell corAlarmaVencida");
            else 
                $("#imagenBoton" + objeto.codDatoSuplementario).attr("class","fa fa-bell");
        }else
            $("#imagenBoton" + objeto.codDatoSuplementario).attr("class","fa");
    }catch(Err){
        alert("actualizarAlarma error:  " + Err.description);
    }
}

function abrirListadoUORs(tipoUOR){
    var argumentos = new Array();
    argumentos[0] = document.forms[0].txtDNI.value;
    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + getContextPath() + "/sge/FichaExpediente.do?opcion=seleccionUOR",argumentos,
      'width=670,height=470',function(datos){
        // TODO: ACLARAR ESTE CASO
        if(datos != null) {
            // Se toma el terceroVO correspondiente a la UOR, es devuelto a traves
            // de recuperaBusquedaTerceros
            document.forms[0].opcion.value = "getTerceroUOR";
            document.forms[0].target="oculto";
            document.forms[0].action = getContextPath() + '/BusquedaTerceros.do?tipoDocUOR=' + tipoUOR +
                  '&docUOR=' + datos[0] + '&nombreUOR=' + datos[1];
            document.forms[0].submit();
        } else
            pleaseWait('off');
    });
}

// #253692
function validarUbicacionDocumentacion(texto,letrasValidas1,letrasValidas2){
    var tamValido = 10;
    var tamLetras = tamValido/2;
    var valida = false;
    if(texto!=""){
        if(texto.length==tamValido){
            var letras = texto.substring(0,5);
            var numeros = texto.substring(5,tamValido);
            if(numeros.substring(0,1)!="-"){ //isNan y Number.isInteger aceptan negativos, pero no son validos, lo  quitamos
                var number = parseInt(numeros);
                if (isNaN(number)!=true && isInt(number) && /^[0-9]{5}$/.test(numeros)==true) { 
                    // parte numerica valida, comprobamos parte alfabetica
                    if(letras.length==tamLetras){
                        var primerBloque = letras.substring(0,2);
                        var segundoBloque = letras.substring(2,tamLetras);
                        var grupos2validos = letrasValidas1.split(";");
                        var grupos3validos = letrasValidas2.split(";");
                        var primeroEsVal = false;
                        for(var i=0; i<grupos2validos.length; i++){
                            if(primerBloque.toUpperCase()==grupos2validos[i].toUpperCase()){
                                primeroEsVal = true;
                                break;
                            }
                        }
                        var segundoEsVal = false;
                        for(var i=0; i<grupos3validos.length; i++){
                            if(segundoBloque.toUpperCase()==grupos3validos[i].toUpperCase()){
                                segundoEsVal = true;
                                break;
                            }
                        }
                        if(primeroEsVal && segundoEsVal)
                            valida = true;
                    } 
                } 
            } 
        }
    } else valida = true;
    
    return valida;
}

function isInt(n) 
{
    return n != "" && !isNaN(n) && Math.round(n) == n;
}

function marcarExpedienteNotifTelematica(){
    //comprobarmos  si se marca el check de notificación telemática en la pestaña de Notificaciones
    if(mostrarCheckNotificaciones){
        for(i=0; i<listaNotificacionesElectronicas.length; i++){
            if(listaNotificacionesElectronicas[i]=="1"){
                $("#expNotificacionTelematica").prop('checked', true);
            }
        }
    } 
}