<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Depuracion Vías :::</title>
<%
    UsuarioValueObject usuarioVO;
    int idioma=1;
    int apl=1;
    if (session.getAttribute("usuario") != null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl =	usuarioVO.getAppCod();
    }

    Config m_Conf = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Conf.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request"
             class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>" />

<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript">
function cargarListaMunicipios(actionOrigen){
    var parametros ="";
    if (actionOrigen!=undefined && actionOrigen=="volver") {
        parametros = "?codPais=" + "<%=(String)request.getAttribute("codPais")%>" + "&codProvincia=" +  document.forms[0].codProvincia.value
            + "&actionOrigen=volver";
    } else {
        parametros = "?codPais=" + "<%=(String)request.getAttribute("codPais")%>" + "&codProvincia=" +  document.forms[0].codProvincia.value;
    }
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/CargarListaMunicipios.do" + parametros;
    document.forms[0].submit();
}

function pulsarBuscarVia(opcion) {
    if(document.forms[0].descVia.value != "") {
        document.forms[0].opcion.value=opcion;
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
        document.forms[0].submit();
    } else {
        jsp_alerta("A",'<%=descriptor.getDescripcion("gMsg_CritBus")%>');
    }
}

function limpiarVia(){
    var descVia = document.forms[0].descVia.value;
    if (descVia==""){
        document.forms[0].codVia.value = "";
    }
}

function cargarListaViasBuscadas(total, idVia, codVia, descVia, descTipoVia, codECO, codESI, codTipoVia){
    if (total==0){
        document.forms[0].codVia.value = "";
        document.forms[0].descVia.value = "";
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');
    } else if (total==1){
        document.forms[0].codVia.value = codVia;
        document.forms[0].descVia.value = descVia;

    } else {
        var source = "<%=request.getContextPath()%>/jsp/terceros/listaViasBuscadasDepuracion.jsp?opcion=null";
        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source="+source,'',
    'width=720,height=400,status='+ '<%=statusBar%>',function(datosConsulta){
                    if(datosConsulta!=undefined && datosConsulta[0] == "altaViaDirecta"){
                        iniciarAltaViaDirecta();
                    } else if(datosConsulta!=undefined){
                        document.forms[0].codVia.value = datosConsulta[1];
                        document.forms[0].descVia.value = datosConsulta[2];
                    }
                });
    }
}

function checkConsulta() {
    if (document.forms[0].tipoConsulta[0].checked) {//seleccionada la via
        document.forms[0].descVia.readOnly = false;
        document.forms[0].descVia.className = "inputTextoObligatorio";
    } else if (document.forms[0].tipoConsulta[1].checked) {//seleccionada repetidas
        document.forms[0].descVia.value = "";
        document.forms[0].codVia.value = "";
        document.forms[0].descVia.readOnly = true;
        document.forms[0].descVia.className = "inputTexto";
    }
}

function pulsarBuscar() {
    if (validarFormulario()) {
        if (document.forms[0].tipoConsulta[1].checked) {//seleccionada repetidas
            document.forms[0].descVia.value="*";
        }
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/BuscarViasRepetidasDepuracion.do";
        document.forms[0].submit();
        if (document.forms[0].tipoConsulta[1].checked) {//seleccionada repetidas
            document.forms[0].descVia.value="";
        }
    }
}

function noVias() {
    rows = new Array();
    tabVias.setLineas(rows);
    tabVias.displayTabla();
    jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoViasRep")%>");
}

function pulsarDepurar() {
    if (tabVias.selectedIndex>=0) {
        var aux = document.forms[0].descVia.value;
        var codVia = tabVias.getLinea(tabVias.selectedIndex)[2];
        document.forms[0].descVia.value= tabVias.getLinea(tabVias.selectedIndex)[3];
        document.forms[0].codProvincia.value = tabVias.getLinea(tabVias.selectedIndex)[4];
        document.forms[0].codMunicipio.value = tabVias.getLinea(tabVias.selectedIndex)[6];
        document.forms[0].target="mainFrame";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/BuscarViaDepuracion.do?codVia=" + codVia;
        document.forms[0].submit();
        document.forms[0].descVia.value = aux;
    } else {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }
}

function pulsarLimpiar(){
    document.forms[0].descVia.value="";
    document.forms[0].codVia.value="";
    document.forms[0].tipoConsulta[0].checked=true;
    checkConsulta();
    comboProvincia.buscaCodigo("<%=(String)request.getAttribute("codProvincia")%>");
    comboMunicipios.buscaCodigo("<%=(String)request.getAttribute("codMunicipio")%>");
    rows = new Array();
    tabVias.setLineas(rows);
    tabVias.displayTabla();
    document.forms[0].cmdDepurar.disabled=true;
    document.forms[0].cmdDepurar.className="botonGeneralDeshabilitado";
}

function pulsarSalir() {
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/SalirDepuracionVias.do";
    document.forms[0].submit();
}

function rellenarDatos(tableName,rowID) {    
    activarBotonDepurar();
}

function activarBotonDepurar(){
    document.forms[0].cmdDepurar.disabled=false;
    document.forms[0].cmdDepurar.className="botonGeneral";
}
</script>
<body class="bandaBody" onload="pleaseWait('off')" >
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<form  method="post" target="_self">
<input  type="hidden" name="opcion">
<div class="txttitblanco"><%=descriptor.getDescripcion("titDepVias")%></div>
<div class="contenidoPantalla">
    <table style="width: 100%">
        <tr>
            <td >
                <span class="etiqueta" style="margin-right:5px">
                    <%=descriptor.getDescripcion("gEtiqProvincia")%>: 
                </span>
                <input class="inputTextoObligatorio" type="text" id="codProvincia" name="codProvincia" style="width:3%">
                <input class="inputTextoObligatorio" type="text" id="descProvincia" name="descProvincia" style="width:20%" readonly>
                    <a id="anchorProvincia" name="anchorProvincia"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia"></span></a>
                <span class="etiqueta" style="margin-left:15px;margin-right:5px">
                    <%=descriptor.getDescripcion("gEtiqMunicipio")%>:
                </span>
                <input class="inputTextoObligatorio" type="text" id="codMunicipio" name="codMunicipio" style="width:3%">
                <input id="descMunicipio" name="descMunicipio" type="text" class="inputTextoObligatorio" style="width:35%" readonly>
                <a id="anchorMunicipio" name="anchorMunicipio"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio"></span></a>
            </td>
        </tr>
        <tr>
            <td style="padding-top: 25px;">
                <div class="etiqueta" style="width:100%">
                    <input type="radio" name="tipoConsulta" class="textoSuelto" value="via" onclick="checkConsulta()" CHECKED> <%=descriptor.getDescripcion("gEtiqSelVia")%>:
                    <input type="hidden" name="codVia">
                    <input type="text" name="descVia" class="inputTextoObligatorio" style="width:25%" maxlength="50" onkeyup="return xAMayusculas(this);" onchange="limpiarVia();">
                    <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Vía" style="cursor:pointer;" onclick="pulsarBuscarVia('buscarVias');"></span>
                </div>
                <div class="etiqueta" style="float:left;margin-top:10px">
                    <input type="radio" name="tipoConsulta" class="textoSuelto" value="repetidas" onclick="checkConsulta()"> <%=descriptor.getDescripcion("gEtiqViasRep")%>
                </div>
                <div style="float:right">
                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar" id="cmdBuscar" onClick="pulsarBuscar();return false;">                                                                                    
                </div>
            </td>
        </tr>
        <tr>
            <td id="tabla" style="width:100%;"></td>
        </tr>
    </table>                                                
    <div class="botoneraPrincipal">
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bDepurar")%>' class="botonGeneralDeshabilitado" value="<%=descriptor.getDescripcion("gbDepurar")%>" name="cmdDepurar" onClick="pulsarDepurar();return false;" disabled>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="pulsarLimpiar();return false;"/>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();return false;"/>
    </div>   
</div>   
</form>
<script type="text/javascript">
    var comboProvincia = new Combo("Provincia");
    var comboMunicipios = new Combo("Municipio");
    var listaCod = new Array();
    var listaDesc = new Array();
    var cont=0;
    <%
        Vector provincias = (Vector) request.getAttribute("provincias");
        for (int i=0;i<provincias.size();i++) {
            GeneralValueObject gVO = (GeneralValueObject) provincias.get(i);
    %>
    listaCod[cont] = "<%=(String)gVO.getAtributo("codigo")%>";
    listaDesc[cont] = "<%=(String)gVO.getAtributo("descripcion")%>";
    cont = cont + 1;
    <%
        }
    %>
    comboProvincia.addItems(listaCod, listaDesc);
    comboProvincia.buscaLinea("<%=(String)request.getAttribute("codProvincia")%>");

    cont = 0;
    listaCod = new Array();
    listaDesc = new Array();
    <%
        Vector municipios = (Vector) request.getAttribute("municipios");
        for (int i=0;i<municipios.size();i++) {
            GeneralValueObject gVO = (GeneralValueObject) municipios.get(i);
    %>
    listaCod[cont] = "<%=(String)gVO.getAtributo("codMunicipio")%>";
    listaDesc[cont] = "<%=(String)gVO.getAtributo("nombreOficial")%>";
    cont = cont + 1;
    <%
        }
    %>
    comboMunicipios.addItems(listaCod, listaDesc);
    comboMunicipios.buscaCodigo("<%=(String)request.getAttribute("codMunicipio")%>");
    comboProvincia.change = function() {
        auxCombo="comboMunicipios";
        if(comboProvincia.cod.value.length!=0){
            cargarListaMunicipios();
        }else{
            comboMunicipios.addItems([],[]);
        }
    }
    <% if (request.getAttribute("actionOrigen")!=null && request.getAttribute("actionOrigen").equals("volver")) { %>
        cargarListaMunicipios("volver");
    <% } %>


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

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


    if (teclaAuxiliar == 1){
       if (comboProvincia.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboProvincia.ocultar()',20);
       if (comboMunicipios.base.style.visibility == "visible" && isClickOutCombo(comboMunicipios,coordx,coordy)) setTimeout('comboMunicipios.ocultar()',20);
    }
    if (teclaAuxiliar == 9){
        comboProvincia.ocultar();
        comboMunicipios.ocultar();
    }
}

var rows = new Array();
<%
    if (session.getAttribute("DepuracionVias.viasRepetidas")!=null && !((Vector)session.getAttribute("DepuracionVias.viasRepetidas")).isEmpty()) {
%>
        <logic:iterate id="elemento" name="DepuracionVias.viasRepetidas" scope="session">
            rows[rows.length] = ["<bean:write name="elemento" property="codTipoVia"/>",
                    "<bean:write name="elemento" property="tipoVia"/>",
                    "<bean:write name="elemento" property="codVia"/>",
                    "<bean:write name="elemento" property="descVia"/>",
                    "<bean:write name="elemento" property="codProvincia"/>",
                    "<bean:write name="elemento" property="provincia"/>",
                    "<bean:write name="elemento" property="codMunicipio"/>",
                    "<bean:write name="elemento" property="municipio"/>"]
        </logic:iterate>
<%
    }
%>
var tabVias = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
    '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
    '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
    '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
    '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
    document.getElementById("tabla"));

tabVias.addColumna('0','left','codTipoVia');
tabVias.addColumna('150','center','<%= descriptor.getDescripcion("gEtiqTipoVia")%>');
tabVias.addColumna('0','left','codVia');
tabVias.addColumna('395','center','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
tabVias.addColumna('0','left','codProvincia');
tabVias.addColumna('150','center','<%= descriptor.getDescripcion("gEtiqProvincia")%>');
tabVias.addColumna('0','left','codMunicipio');
tabVias.addColumna('190','center','<%= descriptor.getDescripcion("gEtiqMunicipio")%>');
tabVias.displayCabecera=true;
tabVias.setLineas(rows);
tabVias.displayTabla();
</script>
</body>
</html>
