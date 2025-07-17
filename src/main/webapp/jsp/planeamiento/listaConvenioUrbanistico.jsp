<!-- JSP de mantenimiento de temas -->
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head>
<jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Listado de Convenios Urbanísticos </title>
<%
    UsuarioValueObject usuarioVO = null;
    int idioma = 1;

    if (session!=null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma =  usuarioVO.getIdioma();
    }
    String idSesion = session.getId();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />


<!-- ***********************		FICHERO JAVASCRIPT 	**************************    -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listasValores/listas/listas.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listasValores/listas/tiposIdioma.js'/>"></script>

<script type="text/javascript" SRC="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" SRC="<html:rewrite page='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css'/>" media="screen" >
<script type="text/javascript">

var lista= new Array();
var i;

function Inicio() {
    window.focus();
    cargaTabla();
}

function recuperaDatos(lis) {
    lista = new Array();

    lista = lis;

    tab.lineas=lista;
    refresh();
}

function pulsarImprimir() {
		pleaseWait('on');
		document.forms[0].target="oculto";
		document.forms[0].action="<html:rewrite page='/planeamiento/ImprimirConvenioUrbanistico.do'/>?idioma=" + "<%=idioma%>";
		document.forms[0].submit();
}

function abrirInforme(nombre){
	pleaseWait('off');
	var source = "<html:rewrite page='/jsp/verPdf.jsp?opcion=null&nombre='/>"+nombre;
	var nombreVentana = (top.name=='ventana'?'ventana2':'ventana');
	ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source="+source,nombreVentana,'width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no');
	ventanaInforme.focus();
}

function pulsarSalir(){
    window.location = "<html:rewrite page='/planeamiento/CargarConvenioUrbanistico.do'/>?tipoRegistro=2&consultando=true";
}

function errorEliminar() {
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjManUtil")%>');
}

function rellenarDatos(tableObject, rowID){
    if(rowID>-1 && !tableObject.ultimoTable){
        document.forms[0].numeroRegistro.value = lista[rowID][0];
        document.forms[0].codigoSubseccion.value = lista[rowID][2];
    }
}

function callFromTableTo(rowID,tableName){
    document.forms[0].target="mainFrame";
    document.forms[0].action = "<html:rewrite page='/planeamiento/RellenarConvenioUrbanisticoFromListado.do'/>";
    document.forms[0].submit();
}

function selecFila(des){
    var indexOld = tab.selectedIndex;
    if(des.length != 0){
        for (var x=0; x<lista.length; x++){
            var auxLis = new String(lista[x][1]);
            auxLis = auxLis.substring(0,des.length);
            if(auxLis == des){
                if (x!=indexOld)tab.selectLinea(x);
                break;
            }
        }
    }else tab.selectLinea(-1);
}

/////////////// Control teclas.

function checkKeysLocal(tecla) {
    if('Alt+S'==tecla) pulsarSalir();
    if('Alt+I'==tecla) pulsarImprimir();

    keyDel();
}

document.onkeydown=checkKeys;

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
                              }" >


        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form method="post">
<input type="hidden" name="numeroRegistro"/>
<input type="hidden" name="codigoSubseccion"/>

<table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
    <tr>
        <td id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_listConUrb")%></td>
    </tr>
    <tr>
        <td width="100%" height="1px" bgcolor="#666666"></td>
    </tr>
    <tr/>
    <tr>
        <td>
            <table width="100%" height="80%" align="center" cellpadding="0px" cellspacing="3px" bgcolor="#FFFFFF">
                <tr>
                    <td>
                        <table width="60%" rules="cols" align="center" cellspacing="0" cellpadding="0" class="fondoCab" border="0">
                            <tr>
                                <td id="tabla">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<div class="botoneraPrincipal" id="tabla2">
    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbImprimir")%>' name="cmdImprimir" onClick="pulsarImprimir();"/>
    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();"/>
</div>
</form>
<script type="text/javascript">
    var tab;
    if(document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla,760);
    else tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('div_tabla'),150);
    tab.addColumna('80','left','<%= descriptor.getDescripcion("gEtiq_numReg")%>');
    tab.addColumna('80','left','<%= descriptor.getDescripcion("gEtiq_fechaAprob")%>');
    tab.addColumna('0','left','');
    tab.addColumna('190','left','<%= descriptor.getDescripcion("gEtiq_subseccion")%>');
    tab.addColumna('190','left','<%= descriptor.getDescripcion("gEtiq_objConvenio")%>');
    tab.addColumna('190','left','<%= descriptor.getDescripcion("gEtiq_ambito")%>');
    tab.height = 300;
    tab.displayCabecera=true;

    function cargaTabla(){
        var cont = 0;

        lista = new Array();
        var objetoConvenio;
        var busca = /@intro@/gi;
        var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
    <logic:iterate id="elemento" name="registrosConvenio" scope="session">
        objetoConvenio = '<bean:write name="elemento" property="objetoConvenio"/>';
        objetoConvenio = objetoConvenio.replace(busca,pStrReemplaza);
        lista[cont] = ['<bean:write name="elemento" property="anho"/>/<bean:write name="elemento" property="numero"/>', '<bean:write name="elemento" property="fechaAprobacion"/>', '<bean:write name="elemento" property="codigoSubseccion"/>', '<bean:write name="elemento" property="subseccion"/>', objetoConvenio, '<bean:write name="elemento" property="ambito"/>'];
        cont = cont + 1;
    </logic:iterate>

        tab.lineas=lista;
        refresh();
    }

    function refresh(){
        tab.displayTabla();
    }
</script>

<script> Inicio(); </script>

</BODY>

</html>
