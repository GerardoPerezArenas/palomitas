/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var urlBaseLlamadaGestionSIRBuscadorDIR3 = APP_CONTEXT_PATH + "/registro/MantAnotacionRegistro/sir/RegistroSIRBuscadorDIR3ActionController.do";

var _sirUnidadDIR3Filtros;
var _sirUnidadDIR3FiltrosDefecto={
    codigoUnidad:null    // 	VARCHAR2 ( 100 BYTE) PRIMARY KEY
    ,nombreUnidad_ES:null    // 	 VARCHAR2(1500 BYTE)
    ,nombreUnidad_EU:null    // 	 VARCHAR2(1500 BYTE)
    ,codigoOficina:  null    // 		VARCHAR2 ( 100 BYTE)
    ,nombreOficina:  null    // 		
    ,codigoOrganismo:null    // 	 VARCHAR2 ( 100 BYTE)
    ,codigoRaiz: null    // 	 VARCHAR2( 100 BYTE)
    ,nombreRaiz: null    // 	 
    ,codigoNivelAdministrativo: null    // 	 VARCHAR2 ( 100 BYTE)
    ,codigoComunidadAutonoma:null    // 	 VARCHAR2 ( 100 BYTE)
    ,codigoProvincia:null    // 	 VARCHAR2 ( 100 BYTE)
    ,fechaActivacionDesde:null       //   date(BBDD) - JSON y parametro String DD/MM/YYYY
    ,fechaActivacionHasta:null       //   date(BBDD) - JSON y parametro String DD/MM/YYYY
    ,numPaginaRecuperar: 1
    ,numPaginasTotal:null
    ,numResultadosPorPagina: 10
    ,numResultadosTotal:null
    ,clasificarYPaginar:true
    ,totalUnidadesDIR3Sistema: null
    //,offsetQuery : 0  // Numero linea para hacer el corte en la consulta
    ,idioma : 1  // Idioma, defecto 1 espanol. 4=Euskera
    };
    
var _barraPaginacion;
var _generarResultadoBusqueda;

/*
 * Plantilla para pintar resultados busqueda.
 */
var _resultadosBusquedaTemplate = '\
        <% if (sirUnidadDIR3RespuestaBusqueda.estadoPeticion == 0) { %>\
            <% if (sirUnidadDIR3RespuestaBusqueda.response.listaUnidadesDIR3 != null && sirUnidadDIR3RespuestaBusqueda.response.listaUnidadesDIR3.length > 0) { %>\
                <div class="form-row">\
                    <div  class="col-md-6">\
                        <div id="totalUnidadesSistema" class="form-group">\
                            <span class="etiqueta"> Unidades Registradas Sistema: </span>\
                            <span class="etiqueta"> <%=sirUnidadDIR3RespuestaBusqueda.response.totalUnidadesDIR3Sistema%> </span>\
                            <span class="etiqueta"> Unidades</span>\
                        </div>\
                        <div id="resultadosObtenidos" class="form-group">\
                            <span class="etiqueta"> Resultados obtenidos: </span>\
                            <span class="etiqueta"> <%=sirUnidadDIR3RespuestaBusqueda.response.numResultadosTotal%> </span>\
                            <span class="etiqueta"> Unidades</span>\
                        </div>\
                        <div id="datosPaginacion" class="form-group">\
                            <span class="etiqueta">P\u00E1gina </span>\
                            <span class="etiqueta"> <%= sirUnidadDIR3RespuestaBusqueda.response.numPaginaRecuperar %> </span>\
                            <span class="etiqueta"> de </span>\
                            <span class="etiqueta"> <%=sirUnidadDIR3RespuestaBusqueda.response.numPaginasTotal %> </span>\
                        </div>\
                    </div>\
                    <div  class="col-md-6" style="align-self: center;">\
                        <div id="selectorPaginacion" style="text-align: right;">\
                            <span class="etiqueta form-group" style="display:inline-block;"> Resultados por p\u00E1gina: </span><br/>\
                            <select class="form-control" style="width: auto; float: right;" id="selectPaginacion" onchange="paginacionOnchage()">\
                                <option label="10" value="10"/>\
                                <option label="25" value="25"/>\
                                <option label="50" value="50"/>\
                            </select>\
                        </div>\
                    </div>\
                </div>\
                <div id="listaResultados">\
                    <table class="table">\
                        <thead class="sub3titulo textoCentrado" >\
                          <tr>\
                            <th scope="col">Codigo DIR3</th>\
                            <th scope="col">Unidad</th>\
                            <th scope="col">Organismo</th>\
                          </tr>\
                        </thead>\
                        <tbody>\
                            <%_.each(sirUnidadDIR3RespuestaBusqueda.response.listaUnidadesDIR3, function(unidadDIR3) { %>\
                                <tr data-unidadcodigo="<%=unidadDIR3.codigoUnidad%>" onclick="asignarUnidadVentanaPadre(this);" class="lineaResultadosSelect">\
                                    <td class="col-md-3"><span id="unidadCodigo_<%=unidadDIR3.codigoUnidad%>"><%=unidadDIR3.codigoUnidad%></span></td>\
                                    <td class="col-md-6">\
                                        <% if(sirUnidadDIR3RespuestaBusqueda.sirUnidadDIR3Filtros !=null && sirUnidadDIR3RespuestaBusqueda.sirUnidadDIR3Filtros.idioma==4){ %>\
                                            <span id="unidadDescripcion_<%=unidadDIR3.codigoUnidad%>"><%=(unidadDIR3.nombreUnidad_EU.length > 200 ) ? unidadDIR3.nombreUnidad_EU.substring(0,200) + "..." : unidadDIR3.nombreUnidad_EU%></span>\
                                        <% } else {%>\
                                            <span id="unidadDescripcion_<%=unidadDIR3.codigoUnidad%>"><%=(unidadDIR3.nombreUnidad_ES.length > 200 ) ? unidadDIR3.nombreUnidad_ES.substring(0,200) + "..." : unidadDIR3.nombreUnidad_ES %></span>\
                                            <%}%>\
                                    </td>\
                                    <td class="col-md-6"><span><%=unidadDIR3.organismo%></span></td>\
                                </tr>\
                            <%});%>\
                        </tbody>\
                    </table>\
                </div>\
            <%} else { %> \
                <h3>No se han encontrado resultados...</h3>\
            <%}\
        } else { %> \
            <h3>Se ha presentado un error en la busqueda... <%=listaUnidadesDIR3.descPeticion%></h3>\
        <%}%> \
        '
;
var testTemplate='<article class="alert alert-primary" role="alert" style="cursor:pointer;" id="article_<%=unidadDIR3.codigoUnidad%>" data-unidadcodigo="<%=unidadDIR3.codigoUnidad%>" onclick="asignarUnidadVentanaPadre(this);">\
                            <div class="container pl-0 pr-0">\
                                <div class="row">\
                                    <div class="col-7">\
                                        <div>\
                                            <div>\
                                                <i class="fa fa-calendar fa-fw" title="Fecha Activacion"></i>\
                                                <span class="etiqueta"><%=unidadDIR3.fechaActivacion%></span>\
                                            </div>\
                                        </div>\
                                        <div>\
                                            <div class="etiqueta">\
                                                <i class="fa fa-tag fa-fw" title="Codigo DIR3"></i>\
                                                <span id="unidadCodigo_<%=unidadDIR3.codigoUnidad%>"><%=unidadDIR3.codigoUnidad%></span>\
                                                <i class="fa fa-tags fa-fw" title="Nombre Unidad"></i>\
                                                <% if(sirUnidadDIR3RespuestaBusqueda.sirUnidadDIR3Filtros !=null && sirUnidadDIR3RespuestaBusqueda.sirUnidadDIR3Filtros.idioma==4){ %>\
                                                    <span id="unidadDescripcion_<%=unidadDIR3.codigoUnidad%>"><%=(unidadDIR3.nombreUnidad_EU.length > 200 ) ? unidadDIR3.nombreUnidad_EU.substring(0,200) + "..." : unidadDIR3.nombreUnidad_EU%></span>\
                                                <% } else {%>\
                                                    <span id="unidadDescripcion_<%=unidadDIR3.codigoUnidad%>"><%=(unidadDIR3.nombreUnidad_ES.length > 200 ) ? unidadDIR3.nombreUnidad_ES.substring(0,200) + "..." : unidadDIR3.nombreUnidad_ES %></span>\
                                                <%}%>\
                                            </div>\
                                        </div>\
                                    </div>\
                                </div>\
                            </div>\
                        </article>\
'
;

$(document).ready(function() {
    // Instarcia la variable filtros
    _sirUnidadDIR3Filtros = $.extend({},_sirUnidadDIR3FiltrosDefecto);
    // Cargamos Desplegables basicos
    cargarDesplegablesIniciales();
    // Asiganamos Eventos elementos html
    asignarEventos();
    // Inicializamos la barra de paginacion,  Se crea la barra de paginacion
    _barraPaginacion = new BarraPaginacionBuscador({
        id: 'paginacionUnidadesDIR3',
        click: _paginar
    });
    // Compilar las plantilla para pintar resumen de Unidades
    _generarResultadoBusqueda = _.template(_resultadosBusquedaTemplate);
    
});


function getListaUnidadOrganicaDestinoSIR() {

    $.ajax({
        type: 'POST',
        url: urlBaseLlamadaGestionSIRBuscadorDIR3,
        data: {'opcion': 'getListaUnidadOrganicaDestinoSIR'
            ,'_sirUnidadDIR3Filtros' : JSON.stringify(_sirUnidadDIR3Filtros)
        },
        success: function (respuesta) {
            if (respuesta !== null) {
                respuesta = JSON.parse(respuesta);
                // Se pinta la pagina de resultados de la busqueda
                _pintarResultadosBusqueda(respuesta);
                // Actualizar los datos de paginacion
                var pagTotales = (respuesta!=null && respuesta.estadoPeticion==0 && respuesta.response!=null && respuesta.response.numPaginasTotal != null
                            ?  respuesta.response.numPaginasTotal
                            : 1
                        );
                _barraPaginacion.establecerPaginacion({
                    numPaginaActual: _sirUnidadDIR3Filtros.numPaginaRecuperar,
                    numPaginasTotal: pagTotales,
                });
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
}

function mostrarModalSeleccionUnidadDIR3(){
    _sirUnidadDIR3Filtros = getParametrosActuales();
    getListaUnidadOrganicaDestinoSIR();
    $("#modalSelectDestinoDIR3SIR").modal('show');
}

function asignarEventos(){
    // Cierre Modal
    $('#modalSelectDestinoDIR3SIR').on('hidden.bs.modal', function () {
        // Limpiar Desplegable, muchas opciones
       resetParametrosSirUnidadDIR3Filtros();
       limpiarDatosFiltrosForm();
    });
    // Boton Filtrar
    $('#btnBuscarUnidadDIR3').on('click', function () {
        _sirUnidadDIR3Filtros = getParametrosActuales();
        _sirUnidadDIR3Filtros.numPaginaRecuperar = 1;
        getListaUnidadOrganicaDestinoSIR();
    });
    // Boton Limpiar Filtros
    $('#btnLimpiarFiltrosUnidadDIR3').on('click', function () {
       limpiarDatosFiltrosForm();
    });

    // Change filtro comunidad cargar provincia
    $('#filtroComunidadAutonoma').on('change', function () {
        cargarDesplegableProvinciaByComunidadAutonoma($("#filtroComunidadAutonoma").val());
    });
}

function _paginar(numPagina){
        _sirUnidadDIR3Filtros.numPaginaRecuperar = numPagina;
        _sirUnidadDIR3Filtros.clasificarYPaginar = false;
        // Lanzar la busqueda de Unidades  
        getListaUnidadOrganicaDestinoSIR();
        $('html, body').animate({ scrollTop: 0 }, 'fast');
    }


function _pintarResultadosBusqueda(sirUnidadDIR3RespuestaBusqueda) {
    $('#paginaUnidadesDIR3').html(_generarResultadoBusqueda({sirUnidadDIR3RespuestaBusqueda: sirUnidadDIR3RespuestaBusqueda}));
    if (_sirUnidadDIR3Filtros.numResultadosPorPagina != null && _sirUnidadDIR3Filtros.numResultadosPorPagina > 0) {
        $("#selectPaginacion").val(_sirUnidadDIR3Filtros.numResultadosPorPagina);
    }
}

function paginacionOnchage() {
    _sirUnidadDIR3Filtros.numPaginaRecuperar = 1;
    _sirUnidadDIR3Filtros.numResultadosPorPagina=$("#selectPaginacion").val();
    getListaUnidadOrganicaDestinoSIR();
}

function getParametrosActuales(){
    _sirUnidadDIR3Filtros.codigoUnidad=$("#filtroCodigoDIR3").val();
    _sirUnidadDIR3Filtros.nombreUnidad_ES=$("#filtroNombreDIR3").val();
    _sirUnidadDIR3Filtros.nombreUnidad_EU=$("#filtroNombreDIR3").val();
    _sirUnidadDIR3Filtros.codigoOficina=$("#filtroCodigoOficina").val();
    _sirUnidadDIR3Filtros.nombreOficina=$("#filtroNombreOficina").val();
    _sirUnidadDIR3Filtros.codigoOrganismo=$("#filtroCodigoOrganismo").val();
    _sirUnidadDIR3Filtros.codigoRaiz=$("#filtroCodigoRaiz").val();
    _sirUnidadDIR3Filtros.nombreRaiz=$("#filtroNombreRaiz").val();
    _sirUnidadDIR3Filtros.codigoNivelAdministrativo=$("#filtroNivelAdministrativo").val();
    _sirUnidadDIR3Filtros.codigoComunidadAutonoma=$("#filtroComunidadAutonoma").val();
    _sirUnidadDIR3Filtros.codigoProvincia=$("#filtroProvincia").val();
    _sirUnidadDIR3Filtros.fechaActivacionDesde=$("#filtroFechaActivacionDesde").val();
    _sirUnidadDIR3Filtros.fechaActivacionHasta=$("#filtroFechaActivacionHasta").val();
    //,offsetQuery : 0  // Numero linea para hacer el corte en la consulta
    _sirUnidadDIR3Filtros.idioma=$("#idiomaUsuarioModal").val();
    return _sirUnidadDIR3Filtros;
}

function resetParametrosSirUnidadDIR3Filtros(){
    _sirUnidadDIR3Filtros = $.extend({}, _sirUnidadDIR3FiltrosDefecto);
}

function cargarDesplegableProvinciaByComunidadAutonoma(codigoComunidadAutonoma){
    resetComboCreateSelectOptionDefault("filtroProvincia");
    if(codigoComunidadAutonoma!==null && codigoComunidadAutonoma!== undefined && codigoComunidadAutonoma!==""){
        $.ajax({
            type: 'POST',
            url: urlBaseLlamadaGestionSIRBuscadorDIR3,
            data: {'opcion': 'getProvinciasPorComunidadAutonoma'
                ,codigoComunidadAutonoma:codigoComunidadAutonoma 
            },
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(respuesta);
                    if (respuesta.estadoPeticion == 0) {
                        
                        $.each(respuesta.response, function (index, item) {
                            $('#filtroProvincia').append($('<option>', {
                                value: item.codigo,
                                text:  item.descripcion,
                                title: item.codigo + " - " + item.descripcion
                            }));
                        });                        
                    } else {
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
    }
}
    
function cargarDesplegablesIniciales(){
    cargarFiltroCodigoOrganismo();
    cargarFiltroNivelAdministrativo();
    cargarFiltroComunidadAutonoma();
    resetComboCreateSelectOptionDefault("filtroProvincia");
}

function cargarFiltroComunidadAutonoma(){
    resetComboCreateSelectOptionDefault("filtroComunidadAutonoma");
    $.ajax({
        type: 'POST',
        url: urlBaseLlamadaGestionSIRBuscadorDIR3,
        data: {'opcion': 'getListaComboComunidadAutonoma'
        },
        success: function (respuesta) {
            if (respuesta !== null) {
                respuesta = JSON.parse(respuesta);
                if (respuesta.estadoPeticion == 0) {
                    $.each(respuesta.response, function (index, item) {
                        $('#filtroComunidadAutonoma').append($('<option>', {
                            value: item.codigo,
                            text: item.descripcion,
                            title: item.codigo + " - " + item.descripcion
                        }));
                    });
                } else {
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
}

function cargarFiltroNivelAdministrativo(){
    resetComboCreateSelectOptionDefault("filtroNivelAdministrativo");
    $.ajax({
        type: 'POST',
        url: urlBaseLlamadaGestionSIRBuscadorDIR3,
        data: {'opcion': 'getListaComboNivelAdministrativo'
        },
        success: function (respuesta) {
            if (respuesta !== null) {
                respuesta = JSON.parse(respuesta);
                if (respuesta.estadoPeticion == 0) {
                    $.each(respuesta.response, function (index, item) {
                        $('#filtroNivelAdministrativo').append($('<option>', {
                            value: item.codigo,
                            text: item.descripcion,
                            title: item.codigo + " - " + item.descripcion
                        }));
                    });
                } else {
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
}

function cargarFiltroCodigoOrganismo(){
    resetComboCreateSelectOptionDefault("filtroCodigoOrganismo");
    $.ajax({
        type: 'POST',
        url: urlBaseLlamadaGestionSIRBuscadorDIR3,
        data: {'opcion': 'getListaComboOrganismos'
        },
        success: function (respuesta) {
            if (respuesta !== null) {
                respuesta = JSON.parse(respuesta);
                if (respuesta.estadoPeticion == 0) {
                    $.each(respuesta.response, function (index, item) {
                        $('#filtroCodigoOrganismo').append($('<option>', {
                            value: item.codigo,
                            text: item.descripcion,
                            title: item.codigo + " - " + item.descripcion
                        }));
                    });
                } else {
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
}

function resetComboCreateSelectOptionDefault(idElementoCombo){
    if(idElementoCombo!==null && idElementoCombo!=="" && idElementoCombo!==undefined){
        $('#'+idElementoCombo).empty();
        $('#'+idElementoCombo).append($('<option>', {
            value: "",
            text: $("#mensajeSeleccionar").val()
        }));
    }
}

function asignarUnidadVentanaPadre(elementoArticle){
    var unidadCodigo=$(elementoArticle).data("unidadcodigo");
    if(unidadCodigo!==null && unidadCodigo!==undefined && unidadCodigo!=""){
        $('[name="codigoUnidadDestinoSIRHidden"]').val($('#unidadCodigo_'+unidadCodigo).text());
        $('[name="codigoUnidadDestinoSIR"]').val($('#unidadCodigo_'+unidadCodigo).text());
        $('[name="nombreUnidadDestinoSIR"]').val($('#unidadDescripcion_'+unidadCodigo).text());
        $("#modalSelectDestinoDIR3SIR").modal('hide');
    }else{
       jsp_alerta("A", "No se ha podido seleccionar la unidad Intentelo de nuevo");
    }
}

function limpiarDatosFiltrosForm(){
    $("#filtroCodigoDIR3").val("");
    $("#filtroNombreDIR3").val("");
    $("#filtroNombreDIR3").val("");
    $("#filtroCodigoOficina").val("");
    $("#filtroNombreOficina").val("");
    $("#filtroCodigoOrganismo").val("");
    $("#filtroCodigoRaiz").val("");
    $("#filtroNombreRaiz").val("");
    $("#filtroNivelAdministrativo").val("");
    $("#filtroComunidadAutonoma").val("");
    $("#filtroProvincia").val("");
    $("#filtroFechaActivacionDesde").val("");
    $("#filtroFechaActivacionHasta").val("");     
}
