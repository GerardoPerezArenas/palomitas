<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.DiligenciasForm" %>
<%@page import="es.altia.agora.business.registro.DiligenciasValueObject" %>
<%@page import="java.util.Vector"%>

<html:html>

<head>

<TITLE>::: LISTA DILIGENCIAS :::</TITLE>

<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

<%
  int idioma=1;
  int apl=1;
  DiligenciasForm dForm = new DiligenciasForm();
  char tipo = 'E';
  String fechaBuscada = "";
  String anotacionBuscada = "";
  if (session!=null){
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if (usuario!=null) {
      idioma = usuario.getIdioma();
      apl = usuario.getAppCod();
    }
	dForm = (DiligenciasForm) session.getAttribute("DiligenciasForm");
	DiligenciasValueObject dVO = new DiligenciasValueObject();
	dVO = dForm.getDiligencia();
	tipo = dVO.getTipo();
	fechaBuscada = dVO.getFechaBuscada();
	anotacionBuscada = dVO.getAnotacionBuscada();
  }

%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<!-- Ficheros JavaScript -->

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >


<SCRIPT language="JavaScript">

var datos = new Array();
var datosOriginal = new Array();
var cont = 0;
var tipoRegistro = "";

function inicializar() {
  <logic:iterate id="elemento" name="DiligenciasForm" property="listaDiligencias">
    var diligencia = unescape('<bean:write name="elemento" property="anotacion"/>');
	if (diligencia.length > 70 ) diligencia = diligencia.substring(0,70)+'...';
	datos[cont] = ['<bean:write name="elemento" property="fecha"/>',
	 			   diligencia];
	datosOriginal[cont] = ['<bean:write name="elemento" property="fecha"/>',
	 			   		   '<bean:write name="elemento" property="anotacion"/>'];
	cont++;
  </logic:iterate>
  tipoRegistro = '<bean:write name="DiligenciasForm" property="tipo"/>';
  document.forms[0].txtAnotacionBuscada.value = '<%= anotacionBuscada%>';
  document.forms[0].fechaAnotacionBuscada.value = '<%= fechaBuscada%>';

  tab.lineas=datos;
  tab.displayTabla();
  window.focus();
}

function pulsarAceptar() {
  if(tab.selectedIndex != -1) {
    
	document.forms[0].txtAnotacion.value = datosOriginal[tab.selectedIndex][1];
	document.forms[0].fecha.value = datosOriginal[tab.selectedIndex][0];
       
	<% 	if ('S' == tipo) { %>
	  document.forms[0].opcion.value = "recarSS";
	<% } else { %>
	  document.forms[0].opcion.value = "recarEE";
	<% } %>
	document.forms[0].target = "mainFrame";
    document.forms[0].action = "Diligencias.do";
    document.forms[0].submit();
  } else {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

function pulsarAceptar2(indice) {
 //if(tab.selectedIndex != -1) {
  if(indice!=-1){
      /*
	document.forms[0].txtAnotacion.value = datosOriginal[tab.selectedIndex][1];
	document.forms[0].fecha.value = datosOriginal[tab.selectedIndex][0];
    */
    document.forms[0].txtAnotacion.value = datosOriginal[indice][1];
	document.forms[0].fecha.value = datosOriginal[indice][0];

	<% 	if ('S' == tipo) { %>
	  document.forms[0].opcion.value = "recarSS";
	<% } else { %>
	  document.forms[0].opcion.value = "recarEE";
	<% } %>
	document.forms[0].target = "mainFrame";
    document.forms[0].action = "Diligencias.do";
    document.forms[0].submit();
  } else {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }

}

function pulsarCancelar() {
  document.forms[0].txtAnotacion.value = "";
  document.forms[0].fecha.value = "";
  if(tipoRegistro == "E") {
    document.forms[0].opcion.value = "recarEE";
	document.forms[0].target = "mainFrame";
    document.forms[0].action = "Diligencias.do";
    document.forms[0].submit();
  } else {
    document.forms[0].opcion.value = "recarSS";
	document.forms[0].target = "mainFrame";
    document.forms[0].action = "Diligencias.do";
    document.forms[0].submit();
  }
}

</SCRIPT>

</head>

<body class="bandaBody"  onload="javascript:{
 								inicializar();}">

<html:form action="/Diligencias.do" target="_self">

<input type="hidden" name="opcion" value="">
<input type="hidden" name="txtAnotacion" value="">
<input type="hidden" name="fecha" value="">
<input type="hidden" name="txtAnotacionBuscada" value ="">
<input type="hidden" name="fechaAnotacionBuscada" value ="">

<% if ('S' == tipo) { %>
    <div class="txttitblancoder"><%=descriptor.getDescripcion("tit_listDilig")%></div>
<% } else { %>
    <div class="txttitblanco"><%=descriptor.getDescripcion("tit_listDilig")%></div>
<% } %>
    <div width="100%"  valign="top" class="contenidoPantalla">
        <table style="width:100%" >
            <tr>
                <td id="tablaDiligencias" width="100%" valign="middle"></td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" accesskey="A"  value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar"  onclick="pulsarCancelar();">
        </div>
    </div>
</html:form>



<script language="JavaScript1.2">

// TABLA PRIMERA
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDiligencias'));

tab.addColumna('150','center','<%= descriptor.getDescripcion("gEtiqFecha")%>');
tab.addColumna('720','center','<%= descriptor.getDescripcion("gEtiq_dilig")%>');
tab.displayCabecera=true;

function checkKeysLocal(evento,tecla) {
    if(window.event) evento = window.event;
    keyDel(evento);
}

function callFromTableTo(rowID,tableName){
  if(tab.id == tableName){
    pulsarAceptar2(rowID);
  }

}


</script>

</BODY>

</html:html>
