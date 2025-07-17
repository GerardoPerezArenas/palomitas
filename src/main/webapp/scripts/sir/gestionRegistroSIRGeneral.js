/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var urlBaseLlamadaGestionGeneralSIR = APP_CONTEXT_PATH + "/registro/MantAnotacionRegistro/sir/RegistroSirGeneralActionController.do";



$(document).ready(function() {
    //inicializarPaginaPrincipalAltaRE();
});

function inicializarPaginaPrincipalAltaRE(conservarDatosDestinoSIRFormulario){
    //Ejecutarse al cargar todos los datos del formulario para validar si es necesario mostrar los campos Destino
    if("S" == getTipoRegistroEoS()){
        if($('[name="cbTipoEntrada"]').val()==1){
            $("#filaUnidadDestinoSalida").show();
            $("#codigoUnidadDestinoSIR").prop("readonly",true);
            $("#nombreUnidadDestinoSIR").prop("readonly",true);
            $('[id="divDatosRegistroSIR"]').show();
            $('#divTablaBotones').attr("style", "float:left;width:35%;position:relative;top:-2px");
            getSetDatosSirUnidadDIR3SalidaDestinoInput();
            // Si estamos modificando mostramos el boton de agregar
            if($('[name="modificar"]').val()=="1"
                    || $('[name="codigoUnidadDestinoSIR"]').val()==""){
                $("#chooseUnidadDestinoSIR").show();
            }else{
                $("#chooseUnidadDestinoSIR").hide();
            }
        }else{
            $("#filaUnidadDestinoSalida").hide();
            $('[id="divDatosRegistroSIR"]').hide();
            $('#divTablaBotones').attr("style", "float:left;width:60%;position:relative;top:-2px");
        }
    }else  if("E" == getTipoRegistroEoS()){
        if($('[name="cbTipoEntrada"]').val()==1){
            $('[id="divDatosRegistroSIR"]').show();
            $('#divTablaBotones').attr("style", "float:left;width:35%;position:relative;top:-2px");
            if(typeof $("#TEOtroReg #prueba2") != 'undefined'){
                $("#TEOtroReg #prueba2").find("input,span,a").prop("disabled", true);
                $("#TEOtroReg #prueba2").find("span").addClass("faDeshabilitado");
                if( ($('#TEOtroReg #prueba2 [name="cod_orgDestino"]').val() != null && $('#TEOtroReg #prueba2 [name="cod_orgDestino"]').val() != "" )
                    || ($('#TEOtroReg #prueba2 [name="cod_uniRegDestino"]').val() != null && $('#TEOtroReg #prueba2 [name="cod_uniRegDestino"]').val() != "" )
                   ){
                   $("#TEOtroReg").show();
                }else{
                    $("#TEOtroReg").hide();
                }
            }

            if(typeof $("#unidadDestinoEntradaSIR") != 'undefined'){
                $("#unidadDestinoEntradaSIR").show();
                $("#codigoUnidadDestinoSIR").prop("disabled","disabled");
                $("#nombreUnidadDestinoSIR").prop("disabled","disabled");
            }

            getSetDatosSirUnidadDIR3EntradaDestinoInput(conservarDatosDestinoSIRFormulario);
            // El procedimiento de SIR, no aparece en la carga inicial de lista de procedimientos
            // Verificamos si esta, sino lo agregamos
            if(!existeProcedimientoInLista_cod_procedimientos($('[name="cod_procedimiento"]').val())){
                cod_procedimientos[cod_procedimientos.length]=$('[name="cod_procedimiento"]').val();
            }
            if(!existeProcedimientoInLista_desc_procedimientos($('[name="desc_procedimiento"]').val())){
                desc_procedimientos[desc_procedimientos.length]=$('[name="desc_procedimiento"]').val();
            }
        }else{
            $('[id="divDatosRegistroSIR"]').hide();
            $('#divTablaBotones').attr("style", "float:left;width:60%;position:relative;top:-2px");
            if(typeof $("#unidadDestinoEntradaSIR") != 'undefined'){
                $("#unidadDestinoEntradaSIR").hide();
            }
            $("#TEOtroReg").hide();
        }
    }
}

function gestionEdicionRegistro(permitirModificar){
    var botonesEdicionNames = ["cmdModificar","cmdAnular","cmdRelacionar"];
        $("#capaBotones3 input").filter(function (indexCB3) {
            return $.inArray($(this).prop("name"), botonesEdicionNames) >= 0;
        }).each(function (indexEach, elementEach) {
                $(elementEach).attr("disabled", !permitirModificar);
                $(elementEach).attr("style", "color:#".concat((!permitirModificar?"#CCCCCC":"#fff")));
        });
}

function limpiarDatosSirUnidadDIR3DestinoInput(){
    $('[name="codigoUnidadDestinoSIR"]').val("");
    $('[name="nombreUnidadDestinoSIR"]').val("");
    $('[id="identificadorRegistroSIR"]').val("");
}


function mostrarMensajeAltaSIR(textoMesaje){
    jsp_alerta("A",textoMesaje,"Gestion SIR/ESI kudeaketa");
}

function getTipoRegistroEoS(){
    var respuesta = "";
    if( $('[name="tipoAnotacionRegistro"]') != null && $('[name="tipoAnotacionRegistro"]') != undefined){
        var tipAnotacionReg = $('[name="tipoAnotacionRegistro"]').val();
        respuesta = ("Relacion_E" == tipAnotacionReg ? "E" : "Relacion_S" == tipAnotacionReg ? "S" : tipAnotacionReg);
    }
    return respuesta;
}

function existeProcedimientoInLista_cod_procedimientos(codProcedimiento){
    if(codProcedimiento != undefined && codProcedimiento != null && codProcedimiento != ""
        && (typeof cod_procedimientos != 'undefined')){
        for(i=0; i<cod_procedimientos.length; i++) {
            if (codProcedimiento == cod_procedimientos[i])
                return true;
        }
    }
    return false;
}

function existeProcedimientoInLista_desc_procedimientos(descProcedimiento){
    if(descProcedimiento != undefined && descProcedimiento != null && descProcedimiento != ""
        && (typeof desc_procedimientos != 'undefined')){
        for(i=0; i<desc_procedimientos.length; i++) {
            if (descProcedimiento == desc_procedimientos[i])
                return true;
        }
    }
    return false;
}