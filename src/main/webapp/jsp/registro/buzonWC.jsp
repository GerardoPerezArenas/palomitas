<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.BuzonWCForm" %>
<%@page import="es.altia.agora.business.registro.BuzonWCValueObject" %>
<%@ page import="java.util.Vector"%>

<html:html>
<head>
<TITLE>::: REGISTRO  Buzón Web Ciudadano:::</TITLE>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

<%
  int idioma=1;
  int apl=1;
  if (session!=null){
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if (usuario!=null) {
      idioma = usuario.getIdioma();
      apl = usuario.getAppCod();
    }
  }

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<!-- Ficheros JavaScript -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>

<SCRIPT language="JavaScript">
<%
/* Recupera el vector de registros de al web del ciudadano de la sesion. */

   Vector relacionRegistros = (Vector) session.getAttribute("RelacionRegistros");
   int numRelacionRegistros = 0;
   if ( relacionRegistros != null ) numRelacionRegistros = relacionRegistros.size();

   BuzonWCForm f= (BuzonWCForm)  session.getAttribute("BuzonWCForm");
   BuzonWCValueObject tVO = f.getBuzonWC();
   int pos=0;

%>

var lista = new Array();
var listaOriginal = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var ultimo = false;
var pagin;

/* Para navegacion Buzon*/
var lineasPagina   = 12;
var paginaActual   = 1;

var numeroRelacionRegistros = <%=numRelacionRegistros%>;
var numeroPaginas=Math.ceil(<%=numRelacionRegistros%>/lineasPagina);
// Fin navegacion Buzon

var tabBuzon;
var tabExp;

function inicializar() {
  listaSel = lista;
  numAnotacion = <%= pos%>;
  if (numAnotacion != 0){
    pag = buscarAnotacion(numAnotacion);
  } else 
      pag = 1;

  cargaPagina(pag);
}

function cargaPagina(numeroPagina){
  lista = new Array();
  listaOriginal= new Array();
  document.forms[0].paginaListado.value = numeroPagina;
  document.forms[0].numLineasPaginaListado.value = lineasPagina;
  document.forms[0].opcion.value="cargar_pagina";
  document.forms[0].target="oculto";
  document.forms[0].action="BuzonWC.do";
  document.forms[0].submit();
}

function inicializaLista(numeroPagina,numero){
  var j = 0;
  numeroRelacionRegistros = numero;

  paginaActual = numeroPagina;
  listaP = new Array();

  inicio =0;
  fin = lineasPagina;
  listaP = listaSel;

  tabBuzon.lineas=listaP;
  refrescaBuzon();

  domlay('enlace',1,0,0,enlaces());

}

// JAVASCRIPT DE LA TABLA DE BUZON ************************************************************

function buscarAnotacion(i) {
  paginaActual=1;
  i = i+1;
  paginaActual = Math.ceil(i/lineasPagina);
  while (paginaActual > paginaSuperior) {
    paginaInferior = paginaInferior +1;
    paginaSuperior = paginaSuperior +1;
  }
  return paginaActual;
}

function enlaces() {
    numeroPaginas = Math.ceil(numeroRelacionRegistros /lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPaginaB');
}

function cargaPaginaB(numeroPagina){
  lista = new Array();
  listaOriginal= new Array();
  document.forms[0].paginaListado.value = numeroPagina;
  document.forms[0].numLineasPaginaListado.value = lineasPagina;
  document.forms[0].opcion.value="cargar_pagina";
  document.forms[0].target="oculto";
  document.forms[0].action="BuzonWC.do";
  document.forms[0].submit();
}
// FIN DEL JAVASCRIPT DE LA TABLA DE BUZON **************************************************************

function callFromTableTo(rowID){
  document.forms[0].numero.value = listaOriginal[rowID][4];
  document.forms[0].ejercicio.value = listaOriginal[rowID][3];
  document.forms[0].opcion.value="verSolicitud";
  document.forms[0].target="oculto";
  document.forms[0].action="BuzonWC.do";
  document.forms[0].submit();
}

function muestraSolicitud(){
  document.forms[0].target="_blank";
  document.forms[0].action="<%=request.getContextPath()%>/jsp/registro/solicitud.jsp";
  document.forms[0].submit();
}

function pulsarHistorico() {
  document.forms[0].opcion.value="inicioHistorico";
  document.forms[0].target="mainFrame";
  document.forms[0].action="BuzonWC.do";
  document.forms[0].submit();
}

function pulsarAceptar() {
  if(tabBuzon.selectedIndex !=-1) {
    document.forms[0].numero.value = listaOriginal[tabBuzon.selectedIndex][4];
	document.forms[0].ejercicio.value = listaOriginal[tabBuzon.selectedIndex][3];
	document.forms[0].opcion.value="aceptar";
    document.forms[0].target="oculto";
    document.forms[0].action="BuzonWC.do";
    document.forms[0].submit();
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

function pulsarRechazar() {
  if(tabBuzon.selectedIndex !=-1) {
    document.forms[0].numero.value = listaOriginal[tabBuzon.selectedIndex][4];
	document.forms[0].ejercicio.value = listaOriginal[tabBuzon.selectedIndex][3];
	document.forms[0].opcion.value="rechazar";
    document.forms[0].target="oculto";
    document.forms[0].action="BuzonWC.do";
    document.forms[0].submit();
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

</SCRIPT>
</head>
<body class="bandaBody"  onload="javascript:{ pleaseWait('off'); inicializar()}">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>

<html:form action="/BuzonWC.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="12"/>

<input type="hidden" name="numero" value="">
<input type="hidden" name="ejercicio" value="">

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_buzWC")%></div>
<div class="contenidoPantalla">
    <table>
        <tr>
            <td width="15%" bgcolor="#7B9EC0" class="etiqueta" align="center">
                <a href="http://69.89.100.151:7006/WebCidadan/index.jsp" target="_blank" style="color: #FFFFFF">
                <!--<a href="http://localhost:7006/WebCidadan/index.jsp" target="_blank" style="color: #FFFFFF">-->
                  <%=descriptor.getDescripcion("gbWebCiud")%>
                </a>
            </td>
        </tr>
            <tr>
              <td width="100%" height="1px" bgcolor="#666666" colspan="2"></td>
            </tr>
            <tr>
              <td width="100%" bgcolor="#e6e6e6" align="center" valign="top" colspan="2">
                <!-- Separador. -->
                <table height="3px" cellpadding="0px" cellspacing="0px">
                  <tr>
                    <td ></td>
                  </tr>
                </table>
                <!-- Fin separador. -->


                <TABLE id ="tabla1" class="tablaP" width="600px" cellspacing="0px" cellpadding="0px">
                 <TR>
                   <TD width="100%" align="center">
                   <table width="100%" rules="cols" bordercolor="#7B9EC0" border="0" cellspacing="0" cellpadding="0" class="fondoCab">
                     <tr>
                     <td align="center" id="tablaBuzon">
                     </td>
                     </tr>
                   </table>
                   </TD>
                 </TR>
                 <tr>
                   <td height="5px" bgcolor="#e6e6e6"></td>
                 </tr>
                 <tr>
                  <td width="100%" class="columnP" height="10px" align="left" bgcolor="#e6e6e6">
                  <div id="enlace" STYLE="width:100%; top:320px;">
                  </div>
                  </td>
                 </tr>
                </TABLE>
            </td>
        </tr>
    </table>
    <div id="tabla1" class="botoneraPrincipal">
      <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onClick="pulsarAceptar();">
      <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbRechazar")%> name="cmdRechazar" onClick="pulsarRechazar();">
      <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbHistorico")%>' name="cmdHistorico" onClick="pulsarHistorico();" >
    </div>
  </div>
</html:form>
<script language="JavaScript1.2">

// JAVASCRIPT DE LA TABLA BUZON

var tabBuzon = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaBuzon'));

tabBuzon.addColumna('130','left','<%= descriptor.getDescripcion("gEtiqFecha")%>');
tabBuzon.addColumna('500','center','<%= descriptor.getDescripcion("etiq_titulo")%>');
tabBuzon.displayCabecera=true;

function refrescaBuzon() {
  tabBuzon.displayTabla();
}

// FIN DE LOS JAVASCRIPT'S DE LA TABLA BUZON

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla){
    var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

  if(teclaAuxiliar == 40){
    upDownTable(tabBuzon,listaSel,teclaAuxiliar);
  }

  if (teclaAuxiliar == 38){
    upDownTable(tabBuzon,listaSel,teclaAuxiliar);
  }

  if(teclaAuxiliar == 13){
    if((tabBuzon.selectedIndex>-1)&&(tabBuzon.selectedIndex < lista.length)&&(!ultimo)){
      callFromTableTo(tabBuzon.selectedIndex);
    }
  }
  keyDel(evento);
}

</script>



<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>

</BODY>

</html:html>
