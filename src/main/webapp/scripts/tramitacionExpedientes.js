// Codigos para los mensajes de alerta
var TRAMITE_TIPO_OK_CSV_GENERADO_CORRECTO = 100;
var TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO = 101;
var TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE = 110;
var TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO = 111;
var TRAMITE_TIPO_ERROR_CSV_CODIGO_DESCONOCIDO = 112;

function verificarPantallaExterna(datosTramite){
    var urlExterna = null;
    var parametrosVentana = null;
    var ajax = getXMLHttpRequest();
    if(ajax!=null){

        var ocurrenciaTramite = datosTramite[0];
        var codTramite       = datosTramite[1];
        var fechaInicio        = datosTramite[3];
        var fechaFin           = datosTramite[4];
        var codProcedimiento = document.forms[0].codProcedimiento.value;
        var codMunicipio     = document.forms[0].codMunicipio.value;
        var numero           = document.forms[0].numero.value;
        var codUtr           = datosTramite[5];
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

function crearCSVDocumentoTramite(codDocumento, nombreDocumento, editorTexto, successCallback) {
    pleaseWait('on');

    if (codDocumento) { 
        try {
             $.ajax({
                 url: getContextPath() + "/sge/DocumentosExpediente.do",
                 type: 'POST',
                 async: true,
                 data: {
                     opcion: 'crearCSV',
                     codDocumento: codDocumento,
                     nombreDocumento: nombreDocumento,
                     editorTexto: editorTexto
                 },
                 success: successCallback,
                 error: errorCrearCSVDocumento
             });
        } catch(Err){
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    } else {
        mostrarMensajeError(TRAMITE_TIPO_ERROR_CSV_CODIGO_DESCONOCIDO);
    }
}

function successCrearCSVDocumentoTramite(ajaxResult) {
    var respuesta = JSON.parse(ajaxResult);
    if (respuesta) {
        var codError = respuesta.status;

        if (codError === 0) { // Si es ok
            var resultado = respuesta.resultado;
            
            if (resultado) {
                var codigoCampo = resultado.codigo;
                var nombreFichero = resultado.nombre;
                var fechaModificacion = resultado.fechaModificacion;

                // Codigo Fichero
                listaDocumentosOriginal[tab.selectedIndex][0] = codigoCampo;
                
                // Nombre Fichero
                listaDocumentos[tab.selectedIndex][0] = nombreFichero;
                listaDocumentosOriginal[tab.selectedIndex][1] = nombreFichero;
                
                // Fecha Modificacion
                listaDocumentos[tab.selectedIndex][2] = fechaModificacion;
                listaDocumentosOriginal[tab.selectedIndex][3] = fechaModificacion;
                
                //EditorTexto
                listaDocumentos[tab.selectedIndex][6] = TEXTOS_I18_EDITOR_TEXTO_NULL;
                listaDocumentosOriginal[tab.selectedIndex][9] = '';
                
                tab.lineas=listaDocumentos;
                refresca()
            }

            mostrarMensajeError(TRAMITE_TIPO_OK_CSV_GENERADO_CORRECTO);
        } else if (codError === 100) { // Ya existe el CSV       
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE);
        } else if (codError === 102) { // Formato no soportado       
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO);
        } else {
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    } else
        mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);

    pleaseWait('off');
}

function crearCSVCampoSuplementarioFicheroTramite(codigo) {
    pleaseWait('on');

    if (codigo) {
        var codTramite = document.forms[0].codTramite.value;
        var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
        var nombreFichero = eval("document.forms[0]." + codigo + ".value;");
        
        try {
             $.ajax({
                 url: getContextPath() + "/sge/DatosSuplementariosFichero.do",
                 type: 'POST',
                 async: true,
                 data: {
                     opcion: 'crearCSV',
                     codigo: codigo,
                     origen: 'TRAMITE',
                     nombreFicheroTramite: nombreFichero,
                     codTramite: codTramite,
                     ocurrenciaTramite: ocurrenciaTramite
                 },
                 success: successCrearCSVCampoSuplementarioFicheroTramite,
                 error: errorCrearCSVDocumento
             });
        } catch(Err){
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    } else {
        mostrarMensajeError(TRAMITE_TIPO_ERROR_CSV_CODIGO_DESCONOCIDO);
    }
}

function successCrearCSVCampoSuplementarioFicheroTramite(ajaxResult) {
    var respuesta = JSON.parse(ajaxResult);

    if (respuesta) {
        var codError = respuesta.status;

        if (codError === 0) { // Si es ok
            var resultado = respuesta.resultado;
            
            if (resultado) {
                var codigoCampo = resultado.codigoCampo;
                var nombreFichero = resultado.nombreFichero;

                actualizarFicheros(codigoCampo, nombreFichero);
                
            }

            mostrarMensajeError(TRAMITE_TIPO_OK_CSV_GENERADO_CORRECTO);
         
        } else if (codError === 100) { // Ya existe el CSV       
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE);
        } else if (codError === 102) { // Formato no soportado       
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO);
        } else {
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    } else
        mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);

    pleaseWait('off');
}

function errorCrearCSVDocumento(result){    
    mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
}

// Obtener flujo y circuito de firmas del documento
function obtenerFlujoFirmaDefecto(codigoDocumento) {
    try {
        pleaseWait('on');

        var criterio = {};
        criterio.opcion = 'obtenerFlujoFirmaPorDefectoByDocumentoTramite';
        criterio.codMunicipio = document.forms[0].codMunicipio.value;
        criterio.codProcedimiento = document.forms[0].codProcedimiento.value;
        criterio.ejercicio = document.forms[0].ejercicio.value;
        criterio.numExpediente = document.forms[0].numeroExpediente.value;
        criterio.codTramite = document.forms[0].codTramite.value;
        criterio.codOcurrencia = document.forms[0].ocurrenciaTramite.value;
        criterio.codDocumento = codigoDocumento;

        $.ajax({
            url: APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do',
            type: 'POST',
            async: true,
            data: criterio,
            success: function(ajaxResult) {
                successAjax(ajaxResult,
                processObtenerFlujoFirmaDefecto,
                TEXTOS_I18_ERROR_INTERNO);
            },
            error: errorAjax
        });
    } catch (Err) {
        errorAjax();
    }
}

function processObtenerFlujoFirmaDefecto(resultado) {
    enviarDocumentoAFirmaFlujo(resultado);
}

// Obtenemos el usuario de firma por defecto para el documento a enviar
function obtenerUsuarioFirmaDefecto(codigoDocumento) {
    try {
        pleaseWait('on');

        var criterio = {};
        criterio.opcion = 'obtenerUsuarioFirmaPorDefectoByDocumentoTramite';
        criterio.codMunicipio = document.forms[0].codMunicipio.value;
        criterio.codProcedimiento = document.forms[0].codProcedimiento.value;
        criterio.ejercicio = document.forms[0].ejercicio.value;
        criterio.numExpediente = document.forms[0].numeroExpediente.value;
        criterio.codTramite = document.forms[0].codTramite.value;
        criterio.codOcurrencia = document.forms[0].ocurrenciaTramite.value;
        criterio.codDocumento = codigoDocumento;

        $.ajax({
            url: APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do',
            type: 'POST',
            async: true,
            data: criterio,
            success: function(ajaxResult) {
                successAjax(ajaxResult,
                processObtenerUsuarioFirmaDefecto,
                TEXTOS_I18_ERROR_INTERNO);
            },
            error: errorAjax
        });
    } catch (Err) {
        errorAjax();
    }
}

function processObtenerUsuarioFirmaDefecto(resultado) {
    enviarDocumentoAFirmaUsuario(resultado);
}

/* Preparar los datos del flujo de firma personalizada */
function prepararEnvioFlujoFirmaTramitePersonalizado(params) {
    try {
        var criterio = {};
        criterio.opcion = 'prepararEnvioFlujoFirmaTramitePersonalizado';
        criterio.codMunicipio = document.forms[0].codMunicipio.value;
        criterio.codProcedimiento = document.forms[0].codProcedimiento.value;
        criterio.ejercicio = document.forms[0].ejercicio.value;
        criterio.numExpediente = document.forms[0].numeroExpediente.value;
        criterio.codTramite = document.forms[0].codTramite.value;
        criterio.codOcurrencia = document.forms[0].ocurrenciaTramite.value;
        criterio.codDocumento = params.codDocumento;
        criterio.flujoCircuito = JSON.stringify(params.datosFlujo);

        $.ajax({
            url: APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do',
            type: 'POST',
            async: true,
            data: criterio,
            success: function(ajaxResult) {
                successAjax(ajaxResult,
                processPrepararEnvioFirmaTramitePersonalizado,
                TEXTOS_I18_ERROR_INTERNO);
            },
            error: errorAjax
        });
    } catch (Err) {
        errorAjax();
    }
}

/* Preparar los datos del usuario firmante personalizada */
function prepararEnvioUsuarioFirmaTramitePersonalizado(params) {
    try {
        var criterio = {};
        criterio.opcion = 'prepararEnvioUsuarioFirmaTramitePersonalizado';
        criterio.codMunicipio = document.forms[0].codMunicipio.value;
        criterio.codProcedimiento = document.forms[0].codProcedimiento.value;
        criterio.ejercicio = document.forms[0].ejercicio.value;
        criterio.numExpediente = document.forms[0].numeroExpediente.value;
        criterio.codTramite = document.forms[0].codTramite.value;
        criterio.codOcurrencia = document.forms[0].ocurrenciaTramite.value;
        criterio.codDocumento = params.codDocumento;
        criterio.usuarioFirmante = JSON.stringify(params.datosUsuario);
    
        $.ajax({
            url: APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do',
            type: 'POST',
            async: true,
            data: criterio,
            success: function(ajaxResult) {
                successAjax(ajaxResult,
                processPrepararEnvioFirmaTramitePersonalizado,
                TEXTOS_I18_ERROR_INTERNO);
            },
            error: errorAjax
        });
    } catch (Err) {
        errorAjax();
    }
}

// Creacion del CSV antes de el envio de la peticion de firma del documento
function processPrepararEnvioFirmaTramitePersonalizado(ajaxResult) {
    pleaseWait('off');

    var editorTexto = null;
    var codDocumento = null;
    var nombreDocumento = null;
    
    // Obtenemos los datos necesarios para la creacion del CSV
    for (var i = 0; i < listaDocumentosOriginal.length; i++) {
        if (listaDocumentosOriginal[i][0] === String(ajaxResult)) {
            editorTexto = listaDocumentosOriginal[i][9];
            codDocumento = listaDocumentosOriginal[i][0];
            nombreDocumento = listaDocumentosOriginal[i][1];
            break;
        }
    }
    
    crearCSVDocumentoTramite(codDocumento, nombreDocumento, editorTexto, processCrearCSVDesdeEnvioFirmaTramite);
}

function processCrearCSVDesdeEnvioFirmaTramite(ajaxResult) {
    var respuesta = JSON.parse(ajaxResult);
    if (respuesta) {
        var codError = respuesta.status;

        if (codError === 0 || codError === 100) { // Si es ok o si ya existe el CSV
            var resultado = respuesta.resultado;
            
            if (resultado) {
                var codigoCampo = resultado.docGen.codigo;
                
                if (codError === 0) {
                    var nombreFichero = resultado.docGen.nombre;
                    var fechaModificacion = resultado.docGen.fechaModificacion;
                    var nombreUsuarioFirmante = resultado.docGen.nombreUsuarioFirmante;
                    var duplicado = resultado.docDup;
                    var codigoDocumentoNuevo= duplicado.codDocumento;
                   
                    var row = [];
                    var rowOriginal = [];
                   
                    // Obtenemos los datos necesarios para la creacion del CSV
                    for (var i = 0; i < listaDocumentosOriginal.length; i++) {
                        if (listaDocumentosOriginal[i][0] === codigoCampo) {
                            // Codigo Fichero
                            listaDocumentosOriginal[i][0] = codigoCampo;
                          
                                                
                            // Nombre Fichero
                            listaDocumentos[i][0] = nombreFichero;
                            listaDocumentosOriginal[i][1] = nombreFichero;

                            // Fecha Modificacion
                            listaDocumentos[i][2] = fechaModificacion;
                            listaDocumentosOriginal[i][3] = fechaModificacion;
                            
                            //Nombre del usuario
                            listaDocumentosOriginal[i][5] = nombreUsuarioFirmante;
                           
                            //EditorTexto
                            var editorTexto = listaDocumentos[i][6];
                            listaDocumentosOriginal[i][6] = editorTexto;
                            
                            //Estado de firma
                            callbackCambioEstadoDocumento("cambiadoEstadoFirmaDocumentoCRDSinAlerta", codigoCampo, duplicado.estadoFirma);
                            
                            listaDocumentosOriginal[i][9] = '';
                            
                            // se añaden los datos del duplicado
                            rowOriginal.push(String(duplicado.codDocumento));
                            
                            rowOriginal.push(duplicado.nombreDocumento);
                            row.push(duplicado.nombreDocumento);
                            
                            rowOriginal.push(duplicado.fechaAltaDocumentoAsString);
                            row.push(duplicado.fechaAltaDocumentoAsString);
                            
                            rowOriginal.push(duplicado.fechaModDocumentoAsString);
                            row.push(duplicado.fechaModDocumentoAsString);
                            
                            rowOriginal.push(duplicado.fechaInformeAsString);
                            if(duplicado.fechaInformeAsString!==""){
                                row.push(duplicado.fechaInformeAsString + '&nbsp;<a href="javascript:modificarFechaInforme('+duplicado.codDocumento +','+listaDocumentos.length+');"><span class="fa fa-calendar" aria-hidden="true" /> </span></a>');
                            } else{
                                row.push('<a href="javascript:modificarFechaInforme('+duplicado.codDocumento +','+listaDocumentos.length+');"><span class="fa fa-calendar" aria-hidden="true"/></span></a>');
                            }
                            
                            rowOriginal.push(listaDocumentosOriginal[i][5]);
                            row.push(listaDocumentosOriginal[i][5]);
                            
                            rowOriginal.push(listaDocumentosOriginal[i][6]);
                            
                            rowOriginal.push(duplicado.estadoFirma);
                            row.push(getEstadoFirmaVisual(duplicado.codDocumento,duplicado.estadoFirma));
                            
                            rowOriginal.push(listaDocumentosOriginal[i][8]);
                            
                            rowOriginal.push(TEXTOS_I18_EDITOR_TEXTO_NULL);                           
                            row.push(TEXTOS_I18_EDITOR_TEXTO_NULL);
                            
                            listaDocumentosOriginal.splice(i,0,rowOriginal);
                            listaDocumentos.splice(i,0,row);
                            
                            break;
                        }
                    }

                    tab.lineas=listaDocumentos;
                    refresca();
                }
                
                pleaseWait('off');
                enviarAFirmar(codigoDocumentoNuevo, codigoCampo);
            } else {
                mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
            }
        } else if (codError === 102) { // Formato no soportado       
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO);
        } else {
            mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
        }
    } else {
        mostrarMensajeError(TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO);
    }
}

// Determina si el tipo de firma es de los antiguos (T y O) o de los nuevos (L y U)
function verDatosFirmaDocumentoSegunTipo(codigoDocumento) {
    try {
        var url = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do';
        
        var criterio = {};
        criterio.opcion = 'comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion';
        criterio.codMunicipio = document.forms[0].codMunicipio.value;
        criterio.codProcedimiento = document.forms[0].codProcedimiento.value;
        criterio.ejercicio = document.forms[0].ejercicio.value;
        criterio.numExpediente = document.forms[0].numeroExpediente.value;
        criterio.codTramite = document.forms[0].codTramite.value;
        criterio.codOcurrencia = document.forms[0].ocurrenciaTramite.value;
        criterio.codDocumento = codigoDocumento;

        $.getJSON(url, criterio, function(data) {
            if (data === true) {
                verDatosFirmaDocumentoFlujoUsuario(codigoDocumento);
            } else {
                verDatosFirmaDocumento(codigoDocumento);
            }
        }).fail(function() {
            errorAjax();
        });
    } catch (Err) {
        errorAjax();
    }
}

// Ver la firma de los tipo L y U
function verDatosFirmaDocumentoFlujoUsuario(codigoDocumento) {
    var opcion = "cargarPantallaVerEstadoCircuitoFirmas";
    var source = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do?opcion=' + opcion;
    source += '&codMunicipio=' + document.forms[0].codMunicipio.value;
    source += '&codProcedimiento=' + document.forms[0].codProcedimiento.value;
    source += '&ejercicio=' + document.forms[0].ejercicio.value;
    source += '&numExpediente=' + document.forms[0].numeroExpediente.value;
    source += '&codTramite=' + document.forms[0].codTramite.value;
    source += '&codOcurrencia=' + document.forms[0].ocurrenciaTramite.value;
    source += '&codDocumento=' + codigoDocumento;

    abrirXanelaAuxiliar(APP_CONTEXT_PATH + '/jsp/sge/mainVentana.jsp?source=' + source,
            "ventana1", 'width=850,height=650,scroll=no', function(result) {
                if (result) {
                }
            });
}

