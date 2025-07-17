/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    $('#modalGestionMensajeOperacion').on('hidden.bs.modal', function () {
        $("#textoValidacionCampos").text("");
        $('#textoValidacionCampos').hide();
    });
});

function registroRetramitarDocsCargarPantallaSelectOwner() {
    var urlBaseLlamada = APP_CONTEXT_PATH + "/registro/MantAnotacionRegistro/DocumentosRegistroRetramitacion.do";
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: {'tipoOperacion': 'retramitarDocumentosCargarPantallaSelectOwner'
            , 'codigoOrgaEsquema': $('[name="regRetramitarDocCodigoOrgaEsquema"]').val()
        },
        success: function (respuesta) {
            if (respuesta !== null) {
                $("#listaProcedimiento").empty();
                // Opcion Vacia
                /*$('#listaProcedimiento').append($('<option>', {
                    value: "",
                    text: "Selecciona Owner..."
                }));
                */
                // Opcion LANDIS 
                $('#listaProcedimiento').append($('<option>', {
                    value: "LANDIS",
                    text: "LANDIS"
                }));
                $('#listaProcedimiento').attr('readonly', true);
                // Lista Procedimiento De Momento Solo cargamos Landis
                /*$.each(respuesta, function (index, item) {
                    $('#listaProcedimiento').append($('<option>', {
                        value: item.codigo,
                        text: item.codigo,
                        title: item.descripcion
                    }));
                });
                */
                /*for (var index = 0; index < respuesta.length; index++) {
                }*/
                $("#modalGestionMensajeOperacion").modal('show');
            } else {
                var mensajeAlert = $('#idiomaUsuarioModal').val() == 4
                    ? "Ezin izan dira Prozeduren Konboa kargatzeko datuak jaso, akatsak bere horretan jarraitzen badu kontsultatu euskarrian."
                    : "No se ha podido recoger los datos para cargar el Combo de Procedimientos, si el error persiste consulte con soporte.";
                jsp_alerta("A",mensajeAlert);
            }
        },
        //dataType: "json",
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeAlert = ($('#idiomaUsuarioModal').val() == 4
                ? "ERROREA gertatu da eskaera prozesatzean. "
                : "Se ha presentado ERROR  al procesar la Peticion. ")
                + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            console.log(mensajeAlert);
            jsp_alerta("A", mensajeAlert);
        }
        //async: false
    });
}

function retramitarSeleccionandoOwner(){
    var ownerSeleccionado = $("#listaProcedimiento").val();
    //alert("Owner Seleccionado : " + ownerSeleccionado);
    $("#textoValidacionCampos").text("");
    if(ownerSeleccionado!=null && ownerSeleccionado!= undefined && ownerSeleccionado!=""){
         $('#textoValidacionCampos').hide();
         $("#modalGestionMensajeOperacion").modal('hide');
         pleaseWait('on');
         registroRetramitarDocsCambioProcedimiento(ownerSeleccionado);
         pleaseWait('off');
    }else{
        var textoMensaje = $("#msgRetramiDocCambioCampoOblig").val();
        if($("#listaProcedimiento").val()==="")
            textoMensaje=textoMensaje+" "+$("#listaProcedimiento").prop("title")+".";
        $("#textoValidacionCampos").text(textoMensaje);
        $('#textoValidacionCampos').slideDown();
    }
}

function registroRetramitarDocsCambioProcedimiento(ownerSeleccionado){
//    alert("Retramitar Documentos... Entrada: " 
//            + " " + $('[name="regRetramitarDocCodigoOrgaEsquema"]').val()
//            + " " + $('[name="regRetramitarDocCodDepPKAnotacion"]').val()
//            + " " + $('[name="regRetramitarDocCodUnidOrgPKAnotacion"]').val()
//            + " " + $('[name="regRetramitarDocTipoAnotacion"]').val()
//            + " " + $('[id="anoEjercicio"]').val() + "/" + $('[id="numeroRegistro"]').val() 
//            + " " + $('[name="cod_procedimiento"]').val()
//        );
    //ownerSeleccionado=(ownerSeleccionado!=null && ownerSeleccionado!= undefined && ownerSeleccionado != "" ? ownerSeleccionado : "LANDIS");
    var _procedimiento = $('[name="cod_procedimiento"]').val();
    var mensajeAlertTemplate = $('#idiomaUsuarioModal').val() == 4 ?
        `Ziur nago dokumentuak  ${_procedimiento}  prozedurarekin eta ${ownerSeleccionado} jabearekin atzeratu nahi dituzula DOKUSIn: Dokumentuen katalogazioari buruzko informazio guztia galduko da.`
        : `Seguro que desea retramitar los documentos con el procedimiento ${_procedimiento} y el Propietario ${ownerSeleccionado} en  DOKUSI:  Se perdera toda la informacion de catalogacion de los mismos.`
    ;
    /*
        'Seguro que desea retramitar los documentos con el procedimiento ' + $('[name="cod_procedimiento"]').val()
                    + ' y el Propietario en DOKUSI : ' + ownerSeleccionado + '. Se perdera toda la informacion de catalogacion de los mismos.' )
    */
    if(jsp_alerta("C",mensajeAlertTemplate)){
//        alert('Llamada ajax a retramitar los documentos...');
        var urlBaseLlamada = APP_CONTEXT_PATH + "/registro/MantAnotacionRegistro/DocumentosRegistroRetramitacion.do";
        var tipoRegistro=$('[name="regRetramitarDocTipoAnotacion"]').val();
        tipoRegistro=(tipoRegistro!=null && tipoRegistro!= undefined && tipoRegistro.indexOf("Relacion_")>=0 ? tipoRegistro.replace("Relacion_","") : tipoRegistro );
        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: {'tipoOperacion':'retramitarDocumentosCambioProcedimiento'
                ,'codigoOrgaEsquema':$('[name="regRetramitarDocCodigoOrgaEsquema"]').val()
                ,'codDepPKAnotacion':$('[name="regRetramitarDocCodDepPKAnotacion"]').val()
                ,'codUnidOrgPKAnotacion':$('[name="regRetramitarDocCodUnidOrgPKAnotacion"]').val()
                ,'tipoAnotacion':tipoRegistro
                ,'ejercicioAnotacion':$('[id="anoEjercicio"]').val()
                ,'numeroAnotacion':$('[id="numeroRegistro"]').val() 
                ,'codProcedimiento':$('[name="cod_procedimiento"]').val()
                ,'ownerSeleccionado':ownerSeleccionado
            },
            success: function (respuesta) {
                if (respuesta !== null) {
//                    respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
//                        return value == null ? "" : value
//                    }));
                    $("#modalGestionMensajeOperacion").modal('hide');
                    if("selectOwner"==respuesta || respuesta[0]=="selectOwner"){
                        $("#modalGestionMensajeOperacion").modal('show');
                    }else{
                        var mensaje = $('#idiomaUsuarioModal').val() == 4
                            ? "Egindako eragiketa. Emaitza: "
                            : "Operacion Ejecutada. Resultado: ";
                        for (var index = 0; index < respuesta.length; index++) {
                            mensaje = mensaje + " " + respuesta[index];
                        }
                        //$("#modalGestionMensajeOperacion").modal('hide');
                        jsp_alerta("A", mensaje);
                        resfrescaTablaDocumentosDigitalizacion();
                    }
                } else {
                    var mensajeAlert = ($('#idiomaUsuarioModal').val() == 4
                        ? "Datuak ezin izan dira eguneratu, akatsak bere horretan jarraitzen badu, kontsultatu euskarrian. "
                        : "Los datos no se ha podido actualizar, si el error persiste consulte con soporte. ");
                    jsp_alerta("A",mensajeAlert);
                }
            },
            //dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                 var mensajeAlert = ($('#idiomaUsuarioModal').val() == 4
                    ? "ERROREA gertatu da eskaera prozesatzean. "
                    : "Se ha presentado ERROR  al procesar la Peticion. ")
                    + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                console.log(mensajeAlert);
                jsp_alerta("A", mensajeAlert);
            }
            //async: false
        });
    }
}

function registrarDocumentos() {
    var oidFormat = ($('#registrarDokusiOidDocumento') != null && $('#registrarDokusiOidDocumento') != undefined
                && $('#registrarDokusiOidDocumento').val() != null && $('#registrarDokusiOidDocumento').val() != undefined
                && $('#registrarDokusiOidDocumento').val() != "" ?
                $('#registrarDokusiOidDocumento').val()
                : ""
                );
    var dniFormat = ($('#registrarDokusiDniUsuario') != null && $('#registrarDokusiDniUsuario') != undefined
                && $('#registrarDokusiDniUsuario').val() != null && $('#registrarDokusiDniUsuario').val() != undefined
                && $('#registrarDokusiDniUsuario').val() != "" ?
                $('#registrarDokusiDniUsuario').val().trim().split(" ").join("").toUpperCase()
                : ""
                );
    var nombreApellidoFormat = ($('#registrarDokusiNombreApellidosUsuario') != null && $('#registrarDokusiNombreApellidosUsuario') != undefined
            && $('#registrarDokusiNombreApellidosUsuario').val() != null && $('#registrarDokusiNombreApellidosUsuario').val() != undefined
            && $('#registrarDokusiNombreApellidosUsuario').val() != "" ?
            $('#registrarDokusiNombreApellidosUsuario').val().trim().split(" ").join("").toUpperCase()
            : ""
            );
    var loginFormat = ($('#registrarDokusiLoginUsuario') != null && $('#registrarDokusiLoginUsuario') != undefined
            && $('#registrarDokusiLoginUsuario').val() != null && $('#registrarDokusiLoginUsuario').val() != undefined
            && $('#registrarDokusiLoginUsuario').val() != "" ?
            $('#registrarDokusiLoginUsuario').val().trim().split(" ").join("").toUpperCase()
            : ""
            );
    if(validarDatosRegistrarUnDocEspe()){
        var mensajeAlert = ($('#idiomaUsuarioModal').val() == 4
            ? "Ziur adierazitako dokumentuak erregistratu nahi dituzula. "
            : "Seguro que desea registrar los documentos indicados. ");
        if (jsp_alerta("C",mensajeAlert
                + prepararTextoConfirmacionRegisto())) {
            var urlBaseLlamada = APP_CONTEXT_PATH + "/registro/MantAnotacionRegistro/DocumentosRegistroRetramitacion.do";
            var tipoRegistro = $('[name="regRetramitarDocTipoAnotacion"]').val();
            tipoRegistro = (tipoRegistro != null && tipoRegistro != undefined && tipoRegistro.indexOf("Relacion_") >= 0 ? tipoRegistro.replace("Relacion_", "") : tipoRegistro);
            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: {'tipoOperacion': 'registrarDocumentosCompulsadosDokusi'
                    , 'codigoOrgaEsquema': $('[name="regRetramitarDocCodigoOrgaEsquema"]').val()
                    , 'codDepPKAnotacion': $('[name="regRetramitarDocCodDepPKAnotacion"]').val()
                    , 'codUnidOrgPKAnotacion': $('[name="regRetramitarDocCodUnidOrgPKAnotacion"]').val()
                    , 'tipoAnotacion': tipoRegistro
                    , 'ejercicioAnotacion': $('[id="anoEjercicio"]').val()
                    , 'numeroAnotacion': $('[id="numeroRegistro"]').val()
                    , 'codProcedimiento': $('[name="cod_procedimiento"]').val()
                    , 'registrarTodos': ($('#registrarDokusiTodosDocs').prop("checked") ? "1" : "0")
                    , 'registrarDokusiOidDocumento': oidFormat
                    , 'registrarDokusiDniUsuario': dniFormat
                    , 'registrarDokusiNombreApellidosUsuario': nombreApellidoFormat
                    , 'registrarDokusiLoginUsuario': loginFormat
                },
                success: function (respuesta) {
                    if (respuesta !== null) {
                        $("#modalGestionMensajeOperacion").modal('hide');
                        if ("selectOwner" == respuesta || respuesta[0] == "selectOwner") {
                            $("#modalGestionMensajeOperacion").modal('show');
                        } else {
                             var mensaje = $('#idiomaUsuarioModal').val() == 4
                                ? "Egindako eragiketa. Emaitza: "
                                : "Operacion Ejecutada. Resultado: ";
                            for (var index = 0; index < respuesta.length; index++) {
                                mensaje = mensaje + " " + respuesta[index];
                            }
                            jsp_alerta("A", mensaje);
                        }
                    } else {
                        var mensajeAlert = $('#idiomaUsuarioModal').val() == 4
                            ? "Ezin izan da eragiketaren erantzuna lortu, akatsak bere horretan jarraitzen badu, kontsultatu euskarriarekin."
                            : "No se ha podido obtener la respuesta de la operacion, si el error persiste consulte con soporte.";
                        jsp_alerta("A",mensajeAlert);
                    }
                },
                //dataType: "json",
                error: function (jqXHR, textStatus, errorThrown) {
                    var mensajeAlert = ($('#idiomaUsuarioModal').val() == 4
                        ? "ERROREA gertatu da eskaera prozesatzean. "
                        : "Se ha presentado ERROR  al procesar la Peticion. ")
                        + jqXHR + ' - ' + textStatus + '  ' +  errorThrown;
                    console.log(mensajeAlert);
                    jsp_alerta("A", mensajeAlert);
                }
                //async: false
            });
        }
    }
}

function prepararTextoConfirmacionRegisto(){
    if($('#registrarDokusiTodosDocs').prop("checked")){
        return $('#registrarDokusiTodosDocs').prop("title");
    }else if($('#registrarDokusiUnDocsEspecifico').prop("checked")){
        return $('#registrarDokusiOidDocumento').val() + " - " + $('#registrarDokusiDniUsuario').val()+"#"+$('#registrarDokusiNombreApellidosUsuario').val()+"#"+$('#registrarDokusiLoginUsuario').val();
    }
}

function mostrarOcultarDetallesRegistrarDocEspecifico(){
    if($('#registrarDokusiTodosDocs').prop("checked")){
        $('#divRegistarDocExpecifico').hide();
    }else{
        $('#divRegistarDocExpecifico').slideDown();
    }
}

function validarDatosRegistrarUnDocEspe(){
    if($('#registrarDokusiUnDocsEspecifico').prop("checked")){
        var oidFormat = ($('#registrarDokusiOidDocumento') != null && $('#registrarDokusiOidDocumento') != undefined
                && $('#registrarDokusiOidDocumento').val() != null && $('#registrarDokusiOidDocumento').val() != undefined
                && $('#registrarDokusiOidDocumento').val() != "" ?
                $('#registrarDokusiOidDocumento').val()
                : ""
                );
        var dniFormat = ($('#registrarDokusiDniUsuario') != null && $('#registrarDokusiDniUsuario') != undefined
                && $('#registrarDokusiDniUsuario').val() != null && $('#registrarDokusiDniUsuario').val() != undefined
                && $('#registrarDokusiDniUsuario').val() != "" ?
                $('#registrarDokusiDniUsuario').val().trim().split(" ").join("").toUpperCase()
                : ""
                );
        var nombreApellidoFormat = ($('#registrarDokusiNombreApellidosUsuario') != null && $('#registrarDokusiNombreApellidosUsuario') != undefined
                && $('#registrarDokusiNombreApellidosUsuario').val() != null && $('#registrarDokusiNombreApellidosUsuario').val() != undefined
                && $('#registrarDokusiNombreApellidosUsuario').val() != "" ?
                $('#registrarDokusiNombreApellidosUsuario').val().trim().split(" ").join("").toUpperCase()
                : ""
                );
        var loginFormat = ($('#registrarDokusiLoginUsuario') != null && $('#registrarDokusiLoginUsuario') != undefined
                && $('#registrarDokusiLoginUsuario').val() != null && $('#registrarDokusiLoginUsuario').val() != undefined
                && $('#registrarDokusiLoginUsuario').val() != "" ?
                $('#registrarDokusiLoginUsuario').val().trim().split(" ").join("").toUpperCase()
                : ""
                );
        $("#textoValidacionCampos").text("");
        $('#textoValidacionCampos').hide();
        if (oidFormat != "" && dniFormat != "" && nombreApellidoFormat != "" && loginFormat) {
            return true;
        } else {
            var textoMensaje = $("#msgRetramiDocCambioCampoOblig").val();
            if ($("#registrarDokusiOidDocumento").val() === "")
                textoMensaje = textoMensaje + " " + $("#registrarDokusiOidDocumento").prop("title") + ".";
            if ($("#registrarDokusiDniUsuario").val() === "")
                textoMensaje = textoMensaje + " " + $("#registrarDokusiDniUsuario").prop("title") + ".";
            if ($("#registrarDokusiNombreApellidosUsuario").val() === "")
                textoMensaje = textoMensaje + " " + $("#registrarDokusiNombreApellidosUsuario").prop("title") + ".";
            if ($("#registrarDokusiLoginUsuario").val() === "")
                textoMensaje = textoMensaje + " " + $("#registrarDokusiLoginUsuario").prop("title") + ".";
            $("#textoValidacionCampos").text(textoMensaje);
            $('#textoValidacionCampos').slideDown();
            return false;
        }
 
    }else return true;
}

function validaRetramitarDocCambioProcedimiento(){ //codigoProcedimientoAnterior,codigoProcedimientoActual
    //if(codigoProcedimientoAnterior!=codigoProcedimientoActual
    //        || codigoProcedimientoAnterior==undefined || codigoProcedimientoActual==undefined){
        // Validar Si tiene Documentos En Dokusi para ser Retramitados.
        // Leer Datos de la entrada 
        // Reseteamos el control Retramitacion 
        var retorno = true;
        $('[name="retramitarDocumentosCambioProcedimiento"]').val("");
        var urlBaseLlamada = APP_CONTEXT_PATH + "/registro/MantAnotacionRegistro/DocumentosRegistroRetramitacion.do";
        var tipoRegistro = $('[name="regRetramitarDocTipoAnotacion"]').val();
        tipoRegistro = (tipoRegistro != null && tipoRegistro != undefined && tipoRegistro.indexOf("Relacion_") >= 0 ? tipoRegistro.replace("Relacion_", "") : tipoRegistro);
        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: {'tipoOperacion': 'getDatosEntradaRegistroValidaRetramitarCambioProc'
                , 'codigoOrgaEsquema': $('[name="regRetramitarDocCodigoOrgaEsquema"]').val()
                , 'codDepPKAnotacion': $('[name="regRetramitarDocCodDepPKAnotacion"]').val()
                , 'codUnidOrgPKAnotacion': $('[name="regRetramitarDocCodUnidOrgPKAnotacion"]').val()
                , 'tipoAnotacion': tipoRegistro
                , 'ejercicioAnotacion': $('[id="anoEjercicio"]').val()
                , 'numeroAnotacion': $('[id="numeroRegistro"]').val()
                , 'codProcedimiento': $('[name="cod_procedimiento"]').val()
            },
            success: function (respuesta) {
                if (respuesta !== null) {
                    // El procedimiento no puede estar a null Si hay que retramitar Avisamos al usuario
                    if(respuesta.codProcedimiento!=null && respuesta.codProcedimiento!==undefined && respuesta.codProcedimiento!=""){
                        if (respuesta.codProcedimiento != respuesta.codProcedimientoAnterior
                            && respuesta.tieneDocumentosEnDokusi) {
                            // Si hay documentos catalogados, mostramos cuadro de confirmacion.
                            if(respuesta.tieneDocumentosCatalogados){
                                var _codProcedimientoAnterior = (respuesta.codProcedimientoAnterior != null && respuesta.codProcedimientoAnterior != undefined && respuesta.codProcedimientoAnterior != "" ? respuesta.codProcedimientoAnterior : "(Sin Valor)");
                                var _codProcedimientoActual = (respuesta.codProcedimiento != null && respuesta.codProcedimiento != undefined && respuesta.codProcedimiento != "" ? respuesta.codProcedimiento : "(Sin Valor)");
                                var mensajeAlertTemplate = $('#idiomaUsuarioModal').val() == 4
                                    ? `${_codProcedimientoAnterior} prozedura aldatu da, dokumentuak ${_codProcedimientoActual}ra atzeratuko dira jarraitzen badu. Haien katalogazioari buruzko informazio guztia galtzen bada. Jarraitzeko asegurua??`
                                    : `Ha cambiado el procedimiento ${_codProcedimientoAnterior}, si continua se retramitaran los documentos a ${_codProcedimientoActual}. Se perdera toda la informacion de catalogacion de los mismos. Seguro de Continuar ??`;
                                /* *
                                    "Ha cambiado el procedimiento "
                                                                        + (respuesta.codProcedimientoAnterior != null && respuesta.codProcedimientoAnterior != undefined && respuesta.codProcedimientoAnterior != "" ? respuesta.codProcedimientoAnterior : "(Sin Valor)")
                                                                        + ", si continua se retramitaran los documentos a "
                                                                        + (respuesta.codProcedimiento != null && respuesta.codProcedimiento != undefined && respuesta.codProcedimiento != "" ? respuesta.codProcedimiento : "(Sin Valor)")
                                                                        + ". Se perdera toda la informacion de catalogacion de los mismos."
                                                                        + " Seguro de Continuar ??"
                                */
                                retorno = jsp_alerta_Mediana("C",mensajeAlertTemplate
                                    ,($('#idiomaUsuarioModal').val() == 4?"Aldaketa berrestea, dokumentuen erretramitazioarekin":"Confirmacion Modificacion con retramitacion Documentos")
                                    );
                            }else // retramitamos automaticamente
                                retorno=true;
                            if (retorno)
                                $('[name="retramitarDocumentosCambioProcedimiento"]').val("1");
                        }
                    }else{
                        var mensajeAlert = $('#idiomaUsuarioModal').val() == 4
                            ? "Prozedura eremua BETE GABE dago, eta Dokusin dauden dokumentu batzuk erretramitatu egin behar dira. Ezin duzu Erregistroaren Aldaketa gauzatu."
                            : "El campo procedimiento esta SIN CUMPLIMENTAR y existen documentos alojados en Dokusi que son necesarios retramitar. No puede ejecutar la Modificacion del Registro.";
                        jsp_alerta_Mediana("A",mensajeAlert
                            ,($('#idiomaUsuarioModal').val() == 4?"Bete gabeko prozeduraren eremuaren jakinarazpena":"Notificacion Campo Procedimiento sin Cumplimentar")
                        );
                        retorno=false;
                    }
                } else {
                    var mensajeAlert = $('#idiomaUsuarioModal').val() == 4
                        ? "Ezin izan da baliozkotu prozedura eta Dokusin ostatatutako dokumentuen izakinak aldatu badira. Jarraitu nahi duzu aldaketarekin??"
                        : "No se ha podido validar si se ha cambiado el procedimiento y la existencias de documentos alojados en Dokusi. Desea de Continuar de todas maneras con la modificacion??";
                    retorno=jsp_alerta_Mediana("C",mensajeAlert
                        ,($('#idiomaUsuarioModal').val() == 4?"Aldaketa berrestea, dokumentuen erretramitazioarekin":"Confirmacion Modificacion con retramitacion Documentos")
                    );
                }
            },
            //dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeAlert = ($('#idiomaUsuarioModal').val() == 4
                    ? "ERROREA gertatu da eskaera prozesatzean. "
                    : "Se ha presentado ERROR  al procesar la Peticion. ")
                    + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                console.log(mensajeAlert);
                jsp_alerta("A", mensajeAlert);
            },
            async: false
        }); 
    //}
    if(!retorno)
        pleaseWait('off');
    return retorno;
}