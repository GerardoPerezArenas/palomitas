<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
    <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
    <title> Mantenimiento de Vias </title>
    <%UsuarioValueObject usuarioVO = new UsuarioValueObject();
        ParametrosTerceroValueObject ptVO = null;
        int idioma = 1;
        int apl = 3;
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
            ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
        }%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
<script type="text/javascript">
<%MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");%>
var ventana = "<%=mantForm.getVentana()%>";
var existePadron = "<%=mantForm.getOperacion()%>";
var codProvincias = new Array();
var descProvincias = new Array();
var codMunicipios = new Array();
var descMunicipios = new Array();
var codMunicipiosDefecto = new Array();
var descMunicipiosDefecto = new Array();
var codTipoVias = new Array();
var descTipoVias = new Array();
var listaViasOriginal = new Array();

var vectorCamposRejilla = ["idVia","codVia","descVia","nombreCorto","codTipoVia","descTipoVia"];
var vectorCombosRejilla = ["comboTipoVia"];
var vectorBotones = ["botonAlta","botonBorrar","botonLimpiar"];

var numViasBuscadas = 0;
var lineasPaginaE = 10;
var paginaActualE = 1;

function inicializar() {
    recuperaDatosIniciales();
    pulsarCancelarBuscar();
    pleaseWait1("off", top.mainFrame);
    pulsarBuscar(1);
}

function recuperaDatosIniciales(){
    <%
            Vector listaProvincias = mantForm.getListaProvincias();
            Vector listaMunicipios = mantForm.getListaMunicipios();
            Vector listaTipoVias = mantForm.getListaTipoVias();
            int lengthTVias = listaTipoVias.size();
            int lengthProvs = listaProvincias.size();
            int lengthMuns = listaMunicipios.size();
            int i;
            // Provincias
            String codProvincias = "";
            String descProvincias = "";
            for (i = 0; i < lengthProvs - 1; i++) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + provincias.getAtributo("codigo") + "\",";
                descProvincias += "\"" + provincias.getAtributo("descripcion") + "\",";
            }
            if (lengthProvs > 0) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + provincias.getAtributo("codigo") + "\"";
                descProvincias += "\"" + provincias.getAtributo("descripcion") + "\"";
            }
            // Municipios
            String codMunicipios = "";
            String descMunicipios = "";
            for (i = 0; i < lengthMuns - 1; i++) {
                GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
                codMunicipios += "\"" + municipios.getAtributo("codMunicipio") + "\",";
                descMunicipios += "\"" + municipios.getAtributo("nombreOficial") + "\",";
            }
            if (lengthMuns > 0) {
                GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
                codMunicipios += "\"" + municipios.getAtributo("codMunicipio") + "\"";
                descMunicipios += "\"" + municipios.getAtributo("nombreOficial") + "\"";
            }
            // Vias
            String codTipoVias = "";
            String descTipoVias = "";
            for (i = 0; i < lengthTVias - 1; i++) {
                GeneralValueObject tVias = (GeneralValueObject) listaTipoVias.get(i);
                codTipoVias += "\"" + tVias.getAtributo("codTipoVia") + "\",";
                descTipoVias += "\"" + tVias.getAtributo("descTipoVia") + "\",";
            }
            if (lengthTVias > 0) {
                GeneralValueObject tVias = (GeneralValueObject) listaTipoVias.get(i);
                codTipoVias += "\"" + tVias.getAtributo("codTipoVia") + "\"";
                descTipoVias += "\"" + tVias.getAtributo("descTipoVia") + "\"";
            }%>
    codProvincias = [<%=codProvincias%>];
    descProvincias = [<%=descProvincias%>];
    codMunicipios = [<%=codMunicipios%>];
    descMunicipios = [<%=descMunicipios%>];
    codMunicipiosDefecto = codMunicipios;
    descMunicipiosDefecto = descMunicipios;
    codTipoVias = [<%=codTipoVias%>];
    descTipoVias = [<%=descTipoVias%>];
    comboProvincia.addItems(codProvincias,descProvincias);
    comboMunicipio.addItems(codMunicipios,descMunicipios);
    comboProvincia.selectItem(-1);
    comboMunicipio.selectItem(-1);
}
                                
function valoresPorDefecto() {
    document.forms[0].codPais.value = "<%=ptVO.getPais()%>";
    document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
    document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
    document.forms[0].codMunicipio.value = "<%=ptVO.getMunicipio()%>";
    document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
    codMunicipios = codMunicipiosDefecto;
    descMunicipios = descMunicipiosDefecto;
    comboTipoVia.addItems(codTipoVias, descTipoVias);
}

function pulsarCancelarBuscar() {
    limpiarFormulario();
    habilitarCamposRejilla(false);
    valoresPorDefecto();
}

function limpiarFormulario(){
    limpiarCamposRejilla();
    tablaVias.lineas = new Array();
    refresca(tablaVias);
}

function limpiarCamposRejilla(){
    limpiar(vectorCamposRejilla);
}

function habilitarCamposRejilla(habilitar) {
    habilitarGeneralInputs(vectorCamposRejilla, habilitar);
    habilitarGeneralInputs(vectorBotones, habilitar);
    habilitarGeneralCombos(vectorCombosRejilla, habilitar);
}

function pulsarBuscar(numeroPaginaE) {
    if (validarCamposBusqueda()) {
        document.forms[0].paginaListadoE.value = numeroPaginaE;
        document.forms[0].numLineasPaginaListadoE.value = lineasPaginaE;
        document.forms[0].opcion.value = "cargarVias1";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
        document.forms[0].submit();
        habilitarCamposRejilla(true);
    } else
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjObligTodos")%>")
}

function validarCamposBusqueda() {
    var pais = document.forms[0].codPais.value;
    var provincia = document.forms[0].codProvincia.value;
    var municipio = document.forms[0].codMunicipio.value;
    return (pais != "") && (provincia != "") && (municipio != "");
}

function actualizarListaVias(numPaginaActual) {
    paginaActualE = numPaginaActual;
    tablaVias.lineas = listaViasOriginal;
    refresca(tablaVias);
    domlay('enlaceE',1,0,0,enlacesE());
}

function enlacesE() {
    numeroPaginasE = Math.ceil(numViasBuscadas / lineasPaginaE);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActualE,numeroPaginasE,'cargaPaginaE');
}

function cargaPaginaE(numeroPaginaE) {
    //listaE = new Array();
    //listaOriginalE = new Array();
    document.forms[0].paginaListadoE.value = numeroPaginaE;
    document.forms[0].numLineasPaginaListadoE.value = lineasPaginaE;
    document.forms[0].opcion.value = "recargarViales";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
    document.forms[0].submit();
}

function cargarListaMunicipios() {
    document.forms[0].opcion.value = "cargarMunicipios";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
    document.forms[0].submit();
}

function cargarListaVias() {
    document.forms[0].paginaListadoE.value = 1;
    document.forms[0].numLineasPaginaListadoE.value = lineasPaginaE;
    document.forms[0].opcion.value="cargarVias1";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
    document.forms[0].submit();
}

function pulsarAlta(){
    if(!filaSeleccionada()){
        if(validarCamposRejilla()){
            if(noEsta()){
                //habilitarCamposBusqueda(true);
                document.forms[0].opcion.value="alta";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                document.forms[0].submit();
                //habilitarCamposBusqueda(false);
                limpiarCamposRejilla();
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
        }
}
    function pulsarModificar(){
        if(filaSeleccionada()){
            if(validarCamposRejilla()){
                if(noEsta(tablaVias.selectedIndex)){
                    if(existePadron=="SI"){
                        var confirmar = jsp_alerta("","<%=descriptor.getDescripcion("msjModViaSinReg")%>");
                            if(confirmar){
                                pleaseWait('on');
                                document.forms[0].opcion.value="modificar";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                                //document.forms[0].submit();
                                limpiarCamposRejilla();
                            }
                        }else{
                        document.forms[0].opcion.value="modificar";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                        document.forms[0].submit();
                        limpiarCamposRejilla();
                    }
                }else
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
        } else
            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
}

function pulsarBorrar(){
    if(filaSeleccionada()){
        if(validarCamposBusqueda()){
            if(existePadron=="SI"){
                var confirmar = jsp_alerta("","<%=descriptor.getDescripcion("msjModViaInc")%>");
                    if(confirmar){
                        document.forms[0].opcion.value="eliminar";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                        document.forms[0].submit();
                        limpiarCamposRejilla();
                    }
                }else{
                document.forms[0].opcion.value="eliminar";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                document.forms[0].submit();
                limpiarCamposRejilla();
            }
        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
    } else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
}

function pulsarCancelarBuscar(){
    limpiarFormulario();
    habilitarCamposRejilla(false);
    valoresPorDefecto();
}

function pulsarLimpiar(){
    limpiarCamposRejilla();
    if(tablaVias.selectedIndex != -1 ) {
        tablaVias.selectLinea(tablaVias.selectedIndex);
        tablaVias.selectedIndex = -1;
    }
}

function pulsarSalir(){
    document.forms[0].opcion.value="inicializarTerc";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}

function filaSeleccionada() {
    var i = tablaVias.focusedIndex;
    return i >= 0 && !tablaVias.ultimoTable;
}

function validarCamposRejilla() {
    var codVia = document.forms[0].codVia.value;
    var descVia = document.forms[0].descVia.value;
    var codTipoVia = document.forms[0].codTipoVia.value;
    return (codVia != "") && (descVia != "") && (codTipoVia != "");
}

function noEsta(indice) {
    var codVia = document.forms[0].codVia.value;
    for (var i = 0; (i < listaViasOriginal.length); i++) {
        if (i != indice) {
            if (listaViasOriginal[i][1] == codVia)
                return false;
        }
    }
    return true;
}
</script>
    </head>
    <body class="bandaBody"  onLoad="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<html:form action="/terceros/mantenimiento/Viales.do" target="_self">
    <input type="hidden" name="paginaListadoE" value="1">
    <input type="hidden" name="numLineasPaginaListadoE" value="10">
    <input type="hidden" name="opcion">
    <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
    <input type="hidden" name="idVia" value="">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantVias")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td>
                    <table>
                        <tr>
                            <td	style="width: 18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                            <td	class="columnP">
                                <input class="inputTexto" type="text"	id="codProvincia" name="codProvincia" size="3" onkeyup="return SoloDigitosNumericos(this);">
                                <input class="inputTexto" type="text"	id="descProvincia" name="descProvincia" style="width:210" readonly>
                                <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:hand;"></span></a>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
                            <td	class="columnP">
                                <input class="inputTexto"	type="text"	id="codMunicipio"	name="codMunicipio"
                                       size="3" onkeyup="return SoloDigitosNumericos(this);">
                                <input id="descMunicipio"	name="descMunicipio" type="text" class="inputTexto"	style="width:210"	readonly>
                                <a id="anchorMunicipio" name="anchorMunicipio" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio" style="cursor:hand;"></span></a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <TR>
                <TD>
                    <div id="tablaVias"></div>
                </td>
            </tr>
            <tr>
                <td>
                    <input name="codVia" type="text" class="inputTextoObligatorio"
                           maxlength="6" onkeyup = "return SoloDigitosNumericos(this);" style="width:9%">
                    <input name="descVia" type="text" class="inputTextoObligatorio" 
                           maxlength=50 onblur="return xAMayusculas(this);" style="width: 34.8%">
                    <input name="nombreCorto" type="text" class="inputTexto" maxlength=35
                           onblur="return xAMayusculas(this);" style="width: 34.8%">
                    <input class="inputTextoObligatorio" type="text" id="codTipoVia" name="codTipoVia"
                           maxlength="5" onkeyup = "return SoloDigitosNumericos(this);" style="width: 3%">
                    <input id="descTipoVia" name="descTipoVia" type="text" class="inputTextoObligatorio"
                           readonly style="width: 12%">
                    <a id="anchorTipoVia" name="anchorTipoVia" href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" alt="" id="botonTipoVia" name="botonTipoVia"
                             style="cursor:'hand'"></span>
                    </a>
                </td>
            </tr>
            <tr>
                <td style="width: 100%; height: 10px" class="columnP" align="left">
                    <div id="enlaceE" class="dataTables_wrapper"></div>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral"  name="botonAlta"	onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
            <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
            <input type="button" class="botonGeneral" name="botonBorrar"	onClick="pulsarBorrar();" accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
            <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
            <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
        </div>            
    </div>            
</html:form>
<script type="text/javascript">
var comboProvincia = new Combo("Provincia");
var comboMunicipio = new Combo("Municipio");

comboProvincia.change =
function() {
    auxCombo='comboMunicipio';
    limpiar(['codMunicipio','descMunicipio']);
    if(comboProvincia.cod.value.length!=0){
        cargarListaMunicipios();
        limpiarFormulario();
    } else{
        comboMunicipio.addItems([],[]);
    }
}

comboMunicipio.change =
function() {
    if (document.forms[0].codMunicipio.value != "")
        cargarListaVias();
    else
        limpiarFormulario();
}

var indice;
var tablaVias = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tablaVias);

tablaVias.addColumna("0","center","idVia");
tablaVias.addColumna("80","center","<%=descriptor.getDescripcion("gEtiq_CodigoINE")%>");
tablaVias.addColumna("300","left","<%=descriptor.getDescripcion("gEtiq_Descrip")%>");
tablaVias.addColumna("300","left","<%=descriptor.getDescripcion("gEtiq_NomCorto")%>");
tablaVias.addColumna("0","center","codTipoVia");
tablaVias.addColumna("170","center","<%=descriptor.getDescripcion("manTer_EtiqTVI")%>");

tablaVias.displayCabecera = true;
tablaVias.displayTabla();

function refresca(tabla){
    tabla.displayTabla();
}
function rellenarDatos(tableName,rowID){
    if(tablaVias==tableName){
        var i=tablaVias.focusedIndex;
        indice = rowID;
        limpiarCamposRejilla();
        if((i>=0)&&!tableName.ultimoTable){
            rellenar(listaViasOriginal[i],vectorCamposRejilla);
        }
    }
}

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>
document.onmouseup = checkKeys;
function checkKeysLocal(evento,tecla){
    var teclaAuxiliar = "";
    if(window.event){
        evento         = window.event;
        teclaAuxiliar =  evento.keyCode;
    }else
        teclaAuxiliar =  evento.which;

    //** Esta funcion se debe implementar en cada JSP para particularizar  **//
    //** las acciones a realizar de las distintas combinaciones de teclas  **//
    keyDel(evento);
    if(teclaAuxiliar == 40){
        upDownTable(tablaVias,listaViasOriginal,teclaAuxiliar);
    }
    if(teclaAuxiliar == 38){
        upDownTable(tablaVias,listaViasOriginal,teclaAuxiliar);
    }
    if (teclaAuxiliar == 1){
        if (comboTipoVia.base.style.visibility == "visible" && isClickOutCombo(comboTipoVia,coordx,coordy)) setTimeout('comboTipoVia.ocultar()',20);
        if (comboProvincia.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboProvincia.ocultar()',20);
        if (comboMunicipio.base.style.visibility == "visible" && isClickOutCombo(comboMunicipio,coordx,coordy)) setTimeout('comboMunicipio.ocultar()',20);
    }
    if (teclaAuxiliar == 9){
        comboTipoVia.ocultar();
        comboProvincia.ocultar();
        comboMunicipio.ocultar();
    }
}
var comboTipoVia  = new Combo("TipoVia");
var auxCombo = "comboMunicipio";
function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
}
</script>
</body>
</html>
