/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var urlBaseLlamadaGestionEntradaSIR = APP_CONTEXT_PATH + "/registro/MantAnotacionRegistro/entrada/sir/RegistroEntradaActionController.do";



$(document).ready(function() {
});

function getSetUnidadOrigenDIR3EntradaSIR(){
    if($('[name="cbTipoEntrada"]').val()==1 && (top.menu.modificando == 'S')){
        $.ajax({
            type: 'POST',
            url: urlBaseLlamadaGestionEntradaSIR,
            data: {'opcion': 'getUnidadOrganicaOrigenEntradaSIR'
            },
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(respuesta);
                    if(respuesta.estadoPeticion==0){
                        // A revisar si leemos desde jsp o inlcuimos en la query de SIR
                        var uorOrigenFromArbol = buscarUorPorCodVisible(uors,respuesta.response.codVisibleUorFlexia);
                        if(uorOrigenFromArbol != null)
                            $('[name="cod_uniRegDestinoORD"]').val(uorOrigenFromArbol.uor_cod);
                        $('[name="cod_uor"]').val(respuesta.response.codVisibleUorFlexia);
                        $('[name="desc_uniRegDestinoORD"]').val(($("#idiomaUsuarioModal").val()==4?respuesta.response.nombreUnidad_EU:respuesta.response.nombreUnidad_ES));
                        $('[name="cod_uor"]').prop("readonly",true);
                        $('[name="desc_uniRegDestinoORD"]').prop("readonly",true);
                        $('[name="botonUnidadeRexistroORD"]').prop("readonly",true);
                        $("#gEtiqUnidOrigenDestino").text($("#etiqUnidOrigen").val());
                        deshabilitarIconos($('[name^="botonUnidadeRexistroORD"]'), true);
                    }else{
                        jsp_alerta("A", "Error al procesar la peticion " + respuesta.descPeticion);
                    }
                } else {
                    jsp_alerta("A", "Respuesta Null");
                }
            },
            //dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            }
            //async: false
        });
    }else{
        $('[name="cod_uor"]').val("");
        $('[name="desc_uniRegDestinoORD"]').val("");
        $('[name="botonUnidadeRexistroORD"]').prop("readonly",false);
        deshabilitarIconos($('[name^="botonUnidadeRexistroORD"]'), false);
        $("#gEtiqUnidOrigenDestino").text($("#etiqUnidDestino").val());
    }

}

function getSetDatosSirUnidadDIR3EntradaDestinoInput(conservarDatosDestinoSIRFormulario){
    if($('[name="cbTipoEntrada"]').val()==1){
        $.ajax({
            type: 'POST',
            url: urlBaseLlamadaGestionEntradaSIR,
            data: {'opcion': 'getDatosSirUnidadDIR3EntradaDestinoByFkRRes'
                ,res_dep:$('[name="codigoDepartamentoPKRegistro"]').val()
                ,res_uor:$('[name="codigoUnidadOrganicaPKRegistro"]').val()
                ,res_tip:getTipoRegistroEoS()
                ,res_eje:$('[name="ano"]').val()
                ,res_num:$('[name="numero"]').val()
            },
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(respuesta);
                    if(respuesta.estadoPeticion==0){
                        if(!((typeof conservarDatosDestinoSIRFormulario != 'undefined') && conservarDatosDestinoSIRFormulario)){
                            if(respuesta.response!=null){
                                $('[name="codigoUnidadDestinoSIR"]').val(respuesta.response.codigoUnidad);
                                $('[name="nombreUnidadDestinoSIR"]').val(respuesta.response.nombreUnidad);
                                $('[id="identificadorRegistroSIR"]').val(respuesta.response.numeroRegistroSIR);
                                $("#gEtiqUnidOrigenDestino").text($("#etiqUnidOrigen").val());
                                // Permitimos la edicion solo si no se ha dado de alta en el SIR
                                if(respuesta.response.numeroRegistroSIR != null && respuesta.response.numeroRegistroSIR!=""){
                                    gestionEdicionRegistro(false);
                                }else{
                                    gestionEdicionRegistro(true);
                                    jsp_alerta("A", $("#msgEntradaNoDadaAltaSIR").val());
                                }
                            }else{
                                jsp_alerta("A", (typeof idiom !== 'undefined' && idiom == 4 ? "Ez da berreskuratu eskatutako irteera-erregistrorako Regexlan ESIko Helburuko Unitatearen daturik." : "No se han recuperado datos Unidad Destino SIR en Regexlan para el registro de salida solicitado"));
                                limpiarDatosSirUnidadDIR3DestinoInput();
                            }
                        }
                    }else{
                        jsp_alerta("A", "Error al procesar la peticion " + respuesta.descPeticion);
                    }                
                } else {
                    jsp_alerta("A", "Respuesta Null");
                }
            },
            //dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            }
            //async: false
        });
    }else{
        $('[name="cod_uor"]').val("");
        $('[name="desc_uniRegDestinoORD"]').val("");
         $("#gEtiqUnidOrigenDestino").text($("#etiqUnidDestino").val());
        $('[name="botonUnidadeRexistroORD"]').prop("readonly",false);
    }
    
}

function getSetCodigoAsuntoEntradaSIR(){
    if($('[name="cbTipoEntrada"]').val()==1 && (top.menu.modificando == 'S')){
        $.ajax({
            type: 'POST',
            url: urlBaseLlamadaGestionEntradaSIR,
            data: {'opcion': 'getCodigoAsuntoEntradasSIR'
            },
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(respuesta);
                    if(respuesta.estadoPeticion==0){
                        $("#codAsunto").val(respuesta.response) // SIR-TRUKEA
                        var indiceSeeccionado = 0;
                        $(comboAsuntos.codigos).each(function(index,elemento){
                            if(elemento==respuesta.response){
                                indiceSeeccionado=index;
                                return;
                            }
                        });
                        comboAsuntos.selectItem(indiceSeeccionado);
                        // Deshabilitamos el combo
                        comboAsuntos.deactivate();
                        deshabilitarIconos($('[name^="botonClasifAsuntos"]'), true);
                        bloquearUnidadDestino(true);
                    }else{
                        jsp_alerta("A", "Error al procesar la peticion " + respuesta.descPeticion);
                    }
                } else {
                    jsp_alerta("A", "Respuesta Null");
                }
            },
            //dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            }
            //async: false
        });
    }else{
        comboAsuntos.activate();
        deshabilitarIconos($('[name^="botonClasifAsuntos"]'), false);
        desBloquearUnidadDestino();
    }

}
