<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%	UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=4;
    if (session.getAttribute("usuario") != null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
    <title>Firmantes</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css'/>">
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
var vectorCamposRejilla = ['codigo', 'descripcion'];
var listaTabla = new Array();
var listaFirmantes = new Array();
var datosRejilla = new Array();

// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    cargaTabla();
    <%
    String modif = request.getParameter("modif");
    %>
    <% if (modif.equals("true")) {%>
/*        document.getElementById("alta").style.display = "inline";
        document.getElementById("eliminar").style.display = "inline";
        document.getElementById("tercero").style.display = "inline";*/
        document.getElementById("botonTer").style.display = "inline";
    <% } else {%>
/*        document.getElementById("alta").style.display = "none";
        document.getElementById("eliminar").style.display = "none";
        document.getElementById("tercero").style.display = "none";*/
        document.getElementById("botonTer").style.display = "none";
    <% } %>
}
// FUNCIONES DE LIMPIEZA DE CAMPOS
function limpiarFormulario(){
    tablaFirmantes.lineas = new Array();
    tablaFirmantes.displayTabla();
    limpiar(vectorCamposRejilla);
}

function limpiarCamposRejilla(){
    limpiar(vectorCamposRejilla);
}

function pulsarSalir() {
    var retorno = "";
    if (tablaFirmantes.lineas[0]!=null) {
        retorno = tablaFirmantes.lineas[0][1];
    }
    self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function pulsarEliminar() {
    if(tablaFirmantes.selectedIndex != -1) {
        if (validarFormulario()) {
            document.forms[0].target = "oculto";
            document.forms[0].action = "<html:rewrite page='/planeamiento/EliminarFirmante.do'/>";
            document.forms[0].submit();
            limpiarFormulario();
        }
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarAlta() {
    var yaExiste = 0;
    if(validarFormulario() && document.forms[0].descripcion.value!="") {
        if(noEsta()){
            document.forms[0].target = "oculto";
            document.forms[0].action = "<html:rewrite page='/planeamiento/InsertarFirmante.do'/>";
            document.forms[0].submit();
            limpiarFormulario();
        } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
        }
    } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
    }
}

// FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
function noEsta(){
    var cod = document.forms[0].codigo.value;
    for(i=0;(i<listaFirmantes.length);i++){
        if((listaFirmantes[i][0] == cod))
            return false;
    }
    return true;
}

function filaSeleccionada(tabla){
    var i = tabla.selectedIndex;
    if((i>=0)&&(!tabla.ultimoTable))
        return true;
    return false;
}

function pulsarBuscarTerc() {
    var argumentos = new Array();
    argumentos =[new Array(),""];
    var source = "<html:rewrite page='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true&preguntaAlta=si'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
	'width=790,height=350,status='+ '<%=statusBar%>',function(datos){
        if(datos!=undefined){
            document.forms[0].codigo.value = datos[0];
            document.forms[0].descripcion.value = datos[6];
        }
    });
}

function abrirTerceros() {
    var source = "<html:rewrite page='/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo=NoAlta&descDoc=&docu='/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
	'width=790,height=510,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            document.forms[0].codigo.value = datos[0];
                            document.forms[0].descripcion.value = datos[6];
                        }
                    });
}
</script>
</head>

<body class="bandaBody" onload="inicializar();">
<form name="formulario" method="post">
<input type="hidden" name="codigo"/>

<table id="tabla1" width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
    <tr>
        <td class="txttitblanco">&nbsp;Lista de Firmantes
        </td>
    </tr>
    <tr>
        <td width="100%" height="1px"	bgcolor="#666666"></td>
    </tr>
    <tr>
        <td bgcolor="#e6e6e6" align="center">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px">
                <tr>
                    <td align="center" id="tabla"></td>
                </tr>
                <tr id="tercero">
                    <td>
                        <table width="100%" cellpadding="1px" cellspacing="0px" border="0px">
                            <tr>
                                <td width="315px" align="left">
                                    <input type="text" class="inputTextoDeshabilitado" id="descripcion" name="descripcion" style="width:275px" readonly><span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Interesado" style="cursor:hand;" onclick="javascript:pulsarBuscarTerc();"></span><span class="fa fa-users" aria-hidden="true"  id="botonTer" name="botonTer" alt="Mantenimiento de Terceros" style="cursor:hand;" onclick="javascript:abrirTerceros();"></span>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td height="20px"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<div id="tablaBotones"  class="botoneraPrincipal">
    <input type= "button" class="botonGeneral" id="alta" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" onClick="pulsarAlta();" accesskey="A">
    <input type= "button" class="botonGeneral" id="eliminar" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar" onClick="limpiar(vectorCamposRejilla);" accesskey="L">
    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
</div>
</form>
<script type="text/javascript">
    var tablaFirmantes;
    if(document.all) tablaFirmantes = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla);
    else tablaFirmantes = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

    tablaFirmantes.addColumna('0','center','');
    tablaFirmantes.addColumna('500','center','<%=descriptor.getDescripcion("gEtiqNombre")%>');
    tablaFirmantes.displayCabecera=true;
    tablaFirmantes.height = 150;

    function cargaTabla(){
        var cont = 0;

        listaFirmantes = new Array();
        <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="partesFirmantes">
            listaFirmantes[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion"/>'];
            cont = cont + 1;
        </logic:iterate>

        tablaFirmantes.lineas=listaFirmantes;
        refresh();
    }

    function rellenarDatos(tableObject, rowID){
        if(rowID>-1 && !tableObject.ultimoTable){
            document.forms[0].codigo.value = listaFirmantes[rowID][0];
            document.forms[0].descripcion.value = listaFirmantes[rowID][1];
        }else limpiarFormulario();
    }

    function recuperaDatos(lista) {
        listaFirmantes = lista;
        tablaFirmantes.lineas=listaFirmantes;
        refresh();
    }

    function refresh() {
        tablaFirmantes.displayTabla();
    }

    document.onmouseup = checkKeys;
    function checkKeysLocal(evento,tecla){
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

        keyDel(evento);
        if (teclaAuxiliar == 38 ||teclaAuxiliar == 40) upDownTable(tablaFirmantes,listaFirmantes,teclaAuxiliar);
    }
</script></body></html>
