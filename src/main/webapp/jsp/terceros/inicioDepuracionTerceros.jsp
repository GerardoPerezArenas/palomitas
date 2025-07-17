<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="java.util.Vector"%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Depuracion Vías :::</title>
<%
    int idioma=1;
    int apl=1;
    if (session.getAttribute("usuario") != null){
        UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl =	usuarioVO.getAppCod();
    }
    String userAgent = request.getHeader("user-agent");

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>"	/>

<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/gestionTerceros.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript">

var consultando = false;
var errorFechas = false;
var startIndex = 1;
var count = 10;
var numResultados = 0;
var paginaActualE = 1;
var numeroPaginasE = 1;
var nuevaBusqueda = true;

function inicio() {
     var botones = document.getElementsByName("cmdDepurar");
      if(botones!=null && botones.length==1){
            botones[0].className = "botonGeneralDeshabilitado";
    }

    <% if (request.getParameter("tipoBusqueda")==null || request.getParameter("tipoBusqueda").equals("")) { %>
    habilitarImagenCal("calFechaInicio", true);
    habilitarImagenCal("calFechaFin", true);
    deshabilitarPanel();
    <% } else if (request.getParameter("tipoBusqueda").equals("modificados")) { %>
        document.forms[0].tipoConsulta[0].checked = true;
        checkConsulta();
        document.forms[0].fechaInicio.value = "<%=request.getParameter("fechaInicio")%>";
        document.forms[0].fechaFin.value = "<%=request.getParameter("fechaFin")%>";
    <% } else if (request.getParameter("tipoBusqueda").equals("repetidos")) { %>
        document.forms[0].tipoConsulta[1].checked = true;
        checkConsulta();
    <% } else { %>
        var nombre =  "<%=request.getParameter("nombre")%>";
        var apellido1 =  "<%=request.getParameter("apellido1")%>";
        var apellido2 =  "<%=request.getParameter("apellido2")%>";
        var documento =  "<%=request.getParameter("documento")%>";
        if (nombre=='null') nombre = '';
        if (apellido1=='null') apellido1 = '';
        if (apellido2=='null') apellido2 = '';
        if (documento=='null') documento = '';
        document.forms[0].tipoConsulta[2].checked = true;
        checkConsulta();
        document.forms[0].nombre.value = nombre;
        document.forms[0].apellido1.value = apellido1;
        document.forms[0].apellido2.value = apellido2;
        document.forms[0].codTipoDocumento.value = "<%=request.getParameter("codTipoDocumento")%>";
        comboTipoDoc.buscaLinea(document.forms[0].codTipoDocumento.value);
        document.forms[0].documento.value = documento;
    <% } %>
}

function checkConsulta() {
    if (document.forms[0].tipoConsulta[0].checked) {//seleccionado modificados
        document.forms[0].fechaInicio.disabled = false;
        document.forms[0].fechaInicio.className = "inputTxtFechaObligatorio";
        document.forms[0].fechaFin.disabled = false;
        document.forms[0].fechaFin.className = "inputTxtFechaObligatorio";
        habilitarImagenCal("calFechaInicio", true);
        habilitarImagenCal("calFechaFin", true);
        limpiarPanel();
        deshabilitarPanel();
    } else if (document.forms[0].tipoConsulta[1].checked) {//seleccionado repetidos
        document.forms[0].fechaInicio.value = "";
        document.forms[0].fechaInicio.className = "inputTxtFecha";
        document.forms[0].fechaInicio.disabled = true;
        document.forms[0].fechaFin.value = "";
        document.forms[0].fechaFin.className = "inputTxtFecha";
        document.forms[0].fechaFin.disabled = true;
        habilitarImagenCal("calFechaInicio", false);
        habilitarImagenCal("calFechaFin", false);
        limpiarPanel();
        deshabilitarPanel();
    } else if (document.forms[0].tipoConsulta[2].checked) {//seleccionado busqueda extendida
        document.forms[0].fechaInicio.value = "";
        document.forms[0].fechaInicio.disabled = true;
        document.forms[0].fechaInicio.className = "inputTxtFecha";
        document.forms[0].fechaFin.value = "";
        document.forms[0].fechaFin.disabled = true;
        document.forms[0].fechaFin.className = "inputTxtFecha";
        habilitarImagenCal("calFechaInicio", false);
        habilitarImagenCal("calFechaFin", false);
        habilitarPanel();
    }
}

function habilitarPanel() {
    document.getElementById("trPanelBusqExtend").disabled=false;
    comboTipoDoc.activate();
    document.forms[0].documento.disabled = false;
    document.forms[0].nombre.disabled = false;
    document.forms[0].apellido1.disabled = false;
    document.forms[0].apellido2.disabled = false;
}


function validarNif(campo) {
    var documento = campo.value;
    var LONGITUD = 9;
    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es menor de la esperada, rellenamos con ceros a la izquierda.
    var ultCaracter = documento.substring(documento.length - 1, documento.length);
    if (isNaN(ultCaracter)) while (documento.length < LONGITUD) documento = "0" + documento;
    else while (documento.length < LONGITUD - 1) documento = "0" + documento;

    if (documento.length > LONGITUD) {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
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
        jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
        campo.value = '';
        campo.focus();
        return false;
    }

    var letraCorrecta = getLetraNif(numDocumento);
    if (letDocumento == '') {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letraCorrecta);
        campo.value = numDocumento;
        campo.select();
        return false;
    }

    if (letDocumento != letraCorrecta) {
        var res = jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' +
            letraCorrecta + '. <%=descriptor.getDescripcion("msjPregContinuar")%>');
        if (!(res > 0)) {
            campo.value = numDocumento + letDocumento;
            campo.select();
            return false;
        }
    }
    campo.value = numDocumento + letDocumento;
    return true;
}

function deshabilitarPanel() {
    document.getElementById("trPanelBusqExtend").disabled=true;
    comboTipoDoc.deactivate();
    document.forms[0].documento.disabled = true;
    document.forms[0].nombre.disabled = true;
    document.forms[0].apellido1.disabled = true;
    document.forms[0].apellido2.disabled = true;
}

function limpiarPanel() {
    document.forms[0].nombre.value = "";
    document.forms[0].apellido1.value = "";
    document.forms[0].apellido2.value = "";
    document.forms[0].codTipoDocumento.value = "";
    document.forms[0].descTipoDocumento.value = "";
    document.forms[0].documento.value = "";
}

function pulsarBuscar() {
    paginaActualE = 1;
    startIndex = 1;
    nuevaBusqueda = true;
    if (validarFormulario()) {
        if (document.forms[0].tipoConsulta[0].checked) {//seleccionada repetidas
            document.forms[0].startIndex.value = startIndex;
            document.forms[0].count.value = count;
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/terceros/BuscarTercerosRepetidosByFecDepuracion.do";
            document.forms[0].submit();
        } else {
            document.forms[0].startIndex.value = startIndex;
            document.forms[0].count.value = count;
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/terceros/BuscarTercerosRepetidosByNomAndDocDepuracion.do";
            document.forms[0].submit();
        }
    }
}

function noTerceros() {
    rows = new Array();
    tabTerceros.setLineas(rows);
    tabTerceros.displayTabla();
    jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoTercerosRep")%>");
}

function pulsarDepurar() {
    var tipoBusqueda = "";
    if (document.forms[0].tipoConsulta[0].checked) {
        tipoBusqueda = document.forms[0].tipoConsulta[0].value;
    } else if (document.forms[0].tipoConsulta[1].checked) {
        tipoBusqueda = document.forms[0].tipoConsulta[1].value;
    } else {
        tipoBusqueda = document.forms[0].tipoConsulta[2].value;
    }

    if (tabTerceros.selectedIndex>=0) {
        document.forms[0].target="mainFrame";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/BuscarTerceroDepuracion.do?codTercero=" +
                tabTerceros.getLinea(tabTerceros.selectedIndex)[2]+"&tipoBusqueda="+tipoBusqueda;
        document.forms[0].submit();
    } else {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }
}

function pulsarLimpiar(){ 
    document.forms[0].fechaInicio.value="";
    document.forms[0].fechaFin.value="";
    comboTipoDoc.buscaLinea(0);
    document.forms[0].documento.value="";
    document.forms[0].nombre.value="";
    document.forms[0].apellido1.value="";
    document.forms[0].apellido2.value="";
    document.forms[0].tipoConsulta[0].checked=true;
    checkConsulta();
    rows = new Array();
    tabTerceros.setLineas(rows);
    tabTerceros.displayTabla();
    document.forms[0].cmdDepurar.disabled=true;
    var botones = document.getElementsByName("cmdDepurar");
    if(botones!=null && botones.length==1){
        botones[0].className = "botonGeneralDeshabilitado";
    }
}

function pulsarSalir() {
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/SalirDepuracionTerceros.do";
    document.forms[0].submit();
}

function rellenarDatos(tableName,rowID) {    
    activarBotonDepurar();
}

function activarBotonDepurar(){ 
    document.forms[0].cmdDepurar.disabled=false;
    var botones = document.getElementsByName("cmdDepurar");
    if(botones!=null && botones.length==1){
        botones[0].className = "botonGeneral";
    }
}

function mostrarCalFechaInicio(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calFechaInicio").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaInicio',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
}

function mostrarCalFechaFin(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calFechaFin").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaFin',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
}

function comprobarFecha(inputFecha) {
    var formato = 'dd/mm/yyyy';
    if (Trim(inputFecha.value)!='') {
        if (consultando) {
            var validas = true;
            var fechaFormateada=inputFecha.value;
            var pos=0;
            var fechas = Trim(inputFecha.value);
            var fechas_array = fechas.split(/[:|&<>!=]/);
            for (var loop=0; loop < fechas_array.length; loop++) {
                var f = fechas_array[loop];
                formato = formatoFecha(Trim(f));
                var D = ValidarFechaConFormato(f,formato);
                if (!D[0]) validas=false;
                else {
                    if (fechaFormateada.indexOf(f,pos) != -1) {
                        var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
                        var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
                        pos=fechaFormateada.indexOf(f,pos);
                        fechaFormateada = toTheLeft + D[1]+ toTheRight;
                    }
                }
            }
            if (!validas) {
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                inputFecha.focus();
                return false;
            } else {
                inputFecha.value = fechaFormateada;
                return true;
            }
        } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
            var D = ValidarFechaConFormato(inputFecha.value,formato);
            if (!D[0]){
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                inputFecha.focus();
                return false;
            } else {
                inputFecha.value = D[1];
                return true;
            }
        }
    }
    return true;
}

function ValidarFechaConFormato(fecha, formato) {
    if (formato == null) formato = "dd/mm/yyyy";
    if (formato == "mm/yyyy")
        fecha = "01/" + fecha;
    else if (formato == "yyyy")
        fecha = "01/01/" + fecha;
    else if (formato == "mmyyyy")
        fecha = "01" + fecha;

    var D = DataValida(fecha);
    if (formato == "dd/mm/yyyy") D[1] = D[0] ? D[1].ISOlocaldateStr() : fecha;
    else if (formato == "mm/yyyy") D[1] = D[0] ? D[1].ISOlocaldateStr().substring(3) : fecha;
    else if (formato == "yyyy") D[1] = D[0] ? D[1].ISOlocaldateStr().substring(6) : fecha;
    else if (formato == "mmyyyy") D[1] = D[0] ? D[1].ISOlocaldateStr().substring(3) : fecha;
    return D;
}

function actualizarNavegacion(numTercsEncontrados) {
    numResultados = numTercsEncontrados;
    domlay('enlaceE',1,0,0,enlacesE());
}

function enlacesE() {
    numeroPaginasE = Math.ceil(numResultados / count);
    paginaActualE = Math.ceil(startIndex / count);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>',
            '<%=descriptor.getDescripcion("siguiente")%>',paginaActualE,numeroPaginasE,'cargaPaginaE');
}

function cargaPaginaE(numeroPaginaE) {
    startIndex = (numeroPaginaE - 1) * count + 1;
    document.forms[0].startIndex.value = startIndex;
    document.forms[0].count.value = count;
    if (document.forms[0].tipoConsulta[0].checked) {//seleccionada repetidas
        document.forms[0].target = "oculto";
        document.forms[0].action = "<%=request.getContextPath()%>/terceros/BuscarTercerosRepetidosByFecDepuracion.do";
        document.forms[0].submit();
    } else {
        document.forms[0].target = "oculto";
        document.forms[0].action = "<%=request.getContextPath()%>/terceros/BuscarTercerosRepetidosByNomAndDocDepuracion.do";
        document.forms[0].submit();
    }
}

function functionClick(){    
}
function cargarComboFilasPagina(){
    var selectorDeFilas = '<select name="filasPagina" id="filasPagina" class ="" onchange="cambiarFilasPagina();">' + 
                                    '<option value="10">10</option>' + 
                                    '<option value="25">25</option>' + 
                                    '<option value="50">50</option>' + 
                                    '<option value="100">100</option>' + 
                                '</select>';
    document.getElementById('contSelectPax').innerHTML = '<%=descriptor.getDescripcion("mosFilasPag")%>'.replace('_MENU_',selectorDeFilas); 
    document.getElementById('filasPagina').value= count;
}

function cambiarFilasPagina(){ 
    count = document.getElementById('filasPagina').value;

    cargaPaginaE(1);
}
</script>
<body class="bandaBody" onload="pleaseWait('off');inicio();">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<form  method="post" target="_self">
<input  type="hidden" name="opcion">
<input type="hidden" name="startIndex">
<input type="hidden" name="count">
<div class="txttitblanco"><%=descriptor.getDescripcion("titDepTerceros")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td colspan="4" class="etiqueta">
                <input type="radio" name="tipoConsulta" class="textoSuelto" value="modificados" onclick="checkConsulta()" CHECKED/> <%=descriptor.getDescripcion("gEtiqDepTercModif")%>:
                &nbsp;
                <input name="fechaInicio" type="text" class="inputTxtFechaObligatorio" size="15"
                       onkeyup = "if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                       onblur = "return comprobarFecha(this);"
                       onfocus = "this.select();"/>
                <a href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;"  style="text-decoration:none;" id="anchorFechaInicio">
                    <span class="fa fa-calendar" aria-hidden="true" name="calFechaInicio" id="calFechaInicio" alt="Data" ></span>
                </a>
                &nbsp;
                <%=descriptor.getDescripcion("gEtiqDepTercY")%>
                &nbsp;
                <input name="fechaFin" type="text" class="inputTxtFechaObligatorio" size="15"
                       onkeyup = "if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                       onblur = "return comprobarFecha(this);"
                       onfocus = "this.select();"/>
                <a href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" id="anchorFechaFin">
                    <span class="fa fa-calendar" aria-hidden="true" name="calFechaFin" id="calFechaFin" alt="Data" ></span>
                </a>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="etiqueta">
                <input type="radio" name="tipoConsulta" class="textoSuelto" value="repetidos" onclick="checkConsulta()"/> <%=descriptor.getDescripcion("gEtiqDepTercRep")%>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="etiqueta">
                <input type="radio" name="tipoConsulta" class="textoSuelto" value="extendida" onclick="checkConsulta()"/> <%=descriptor.getDescripcion("gEtiqDepTercExtend")%>
            </td>
        </tr>
        <tr id="trPanelBusqExtend" style="visibility: visible;">
            <td style="width: 18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</td>
            <td style="width: 32%" class="columnP">
                <input id="codTipoDocumento" name="codTipoDocumento" type="text" class="inputTexto" size="3"
                       onkeyup="return SoloDigitosConsulta(this);">
                <input id="descTipoDocumento" name="descTipoDocumento" type="text" class="inputTexto" style="width:169px" readonly>
                <a id="anchorTipoDocumento" name="anchorTipoDocumento" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDocumento" name="botonTipoDocumento" style="cursor:hand;"	alt=""></span></a>
            </td>
            <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
            <td class="columnP">
                <input type="text"  class="inputTexto" size="35" maxlength=16 name="documento" onkeyup="return xAMayusculas(this);"
                       onchange="documentoNoValido('codTipoDocumento','documento',1);">
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
            <td colspan="3">
                <input TYPE="text" class="inputTexto" style="width:582px;" MAXLENGTH=80  NAME="nombre" onblur="return xAMayusculas(this);">
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
            <td class="columnP">
                <input TYPE="text" name="apellido1" class="inputTexto" SIZE=35	MAXLENGTH=25 onblur="return xAMayusculas(this);">
            </td>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</td>
            <td class="columnP">
                <input name="apellido2" TYPE="text" class="inputTexto" SIZE=35	MAXLENGTH=25 onblur="return xAMayusculas(this);">
            </td>
        </tr>
        <tr>
            <td colspan="4" align="right">
                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar" id="cmdBuscar" onClick="pulsarBuscar();return false;">                                                                                                                
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <div class="dataTables_wrapper paxinacionDataTables">
                    <label id="contSelectPax"></label>
                </div>
            </td>
        </tr>            
        <tr>
            <td colspan="4" id="tabla" style="width:100%;"></td>
        </tr>
        <tr>
            <td colspan="4" id="enlaceE" class="dataTables_wrapper"></td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
            <input type="button" title='<%=descriptor.getDescripcion("toolTip_bDepurar")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbDepurar")%>" name="cmdDepurar" onClick="pulsarDepurar();return false;" disabled>
            <input type="button" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="pulsarLimpiar();return false;"/>
            <input type="button" title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();return false;"/>
    </div>                                
</div>                                
</form>
<script type="text/javascript">
var comboTipoDoc = new Combo("TipoDocumento");
var listaCod = new Array();
var listaDesc = new Array();
var cont=0;
<%
Vector tiposDoc = (Vector) request.getAttribute("DepuracionTerceros.tiposDocumento");
for(int h=0;h<tiposDoc.size();h++){
    GeneralValueObject gVO = (GeneralValueObject)tiposDoc.get(h);
%>
listaCod[cont] = "<%=gVO.getAtributo("codTipoDoc")%>";
listaDesc[cont] = "<%=gVO.getAtributo("descTipoDoc")%>";
cont = cont + 1;
<%
}
%>
comboTipoDoc.addItems(listaCod, listaDesc);

//Usado para el calendario
var coordx=0;
var coordy=0;

<%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }

    if (teclaAuxiliar == 1){
        if (comboTipoDoc.base.style.visibility == "visible" && isClickOutCombo(comboTipoDoc,coordx,coordy)) setTimeout('comboTipoDoc.ocultar()',20);
        if(IsCalendarVisible) replegarCalendario(coordx,coordy);
    }
    if (teclaAuxiliar == 9){
        comboTipoDoc.ocultar();
        if(IsCalendarVisible) hideCalendar();
    }
}

var rows = new Array();
<%
if (session.getAttribute("DepuracionTerceros.tercerosRepetidos")!=null && !((Vector)session.getAttribute("DepuracionTerceros.tercerosRepetidos")).isEmpty()) {
%>
    rows = new Array();
    <logic:iterate id="elemento" name="DepuracionTerceros.tercerosRepetidos" scope="session">
    rows[rows.length] = ["<bean:write name="elemento" property="tipoDocumento"/>",
            "<bean:write name="elemento" property="documento"/>",
            "<bean:write name="elemento" property="codTercero"/>",
            "<bean:write name="elemento" property="nombre"/>",
            "<bean:write name="elemento" property="apellido1"/>",
            "<bean:write name="elemento" property="apellido2"/>"];
    </logic:iterate>
    actualizarNavegacion(rows.length);
<%
}
%>
var tabTerceros = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
        '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
        '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
        '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
        document.getElementById("tabla"));

tabTerceros.addColumna('130','center','<%= descriptor.getDescripcion("gEtiqTiDoc")%>');
tabTerceros.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqDocumento")%>');
tabTerceros.addColumna('0','center','<%= descriptor.getDescripcion("codTercero")%>');
tabTerceros.addColumna('210','center','<%= descriptor.getDescripcion("gEtiqNombre")%>');
tabTerceros.addColumna('210','center','<%= descriptor.getDescripcion("gEtiqApellido1Part")%>');
tabTerceros.addColumna('210','center','<%= descriptor.getDescripcion("gEtiqApellido2Part")%>');
tabTerceros.displayCabecera=true;
tabTerceros.setLineas(rows);
tabTerceros.displayTabla();
cargarComboFilasPagina();
</script>
</body>
</html>
