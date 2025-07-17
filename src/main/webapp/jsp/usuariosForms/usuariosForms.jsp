<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.usuariosforms.UsuariosFormsValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.usuariosforms.UsuariosFormsForm" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<html:html>

<head><jsp:include page="/jsp/usuariosForms/tpls/app-constants.jsp" />

<TITLE>::: ADMINISTRACIÓN LOCAL: Mantenimiento de usuarios :::</TITLE>

<!-- Estilos -->




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

    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>

<SCRIPT language="JavaScript">

<% 
/* Recupera el vector de usuarios de la sesion. */

   Vector relacionUsuarios = (Vector) session.getAttribute("RelacionUsuarios");
   int numRelacionUsuarios = 0;
   if ( relacionUsuarios != null ) numRelacionUsuarios = relacionUsuarios.size();
   int pos=0;

%>

var lista = new Array();
var listaOriginal = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var ultimo = false;
var pagin;

/* Para navegacion Usuarios*/
var lineasPagina   = 12;
var paginaActual   = 1;
var inicio = 0;
var fin    = 0;
var numUsuarios = <%= numRelacionUsuarios %>;
var posicion = <%= pos %>;
var numeroPaginas=Math.ceil(numUsuarios/lineasPagina);

// Fin navegacion Usuarios

var tabUsuarios;
var tableObject=tabUsuarios;

function inicializar() {

window.focus();
listaSel = lista;
numAnotacion = posicion;

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
  document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
  document.forms[0].submit();
}

function inicializaLista(numeroPagina){

  tableObject=tabUsuarios;
  var j = 0;

  paginaActual = numeroPagina;
  listaP = new Array();

  inicio =0;
  fin = lineasPagina;
  listaP = listaSel;

  tabUsuarios.lineas=listaP;
  refrescaUsuarios();

  domlay('enlace',1,0,0,enlaces());

}

// JAVASCRIPT DE LA TABLA DE USUARIOS ************************************************************

function buscarAnotacion(i) {
  paginaActual=1;
  i = i+1;
  paginaActual = Math.ceil(i/lineasPagina);
  return paginaActual;
}

function enlaces() {
    numeroPaginas = Math.ceil(numUsuarios /lineasPagina);
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
  document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
  document.forms[0].submit();
}

// FIN DEL JAVASCRIPT DE LA TABLA DE USUARIOS **************************************************************

function callFromTableTo(rowID,tableName){
  if(tabUsuarios.id == tableName){
    datosUsuarios(rowID);
  } 
}

function pulsarSalir() {
  document.forms[0].target = "mainFrame";
  document.forms[0].action = '<%=request.getContextPath()%>/jsp/usuariosForms/presentacionADM.jsp';
  document.forms[0].submit();
}

function datosUsuarios(rowID) {
  var variables = new Array();
  variables[0] = lista[rowID][0];
  var primero = listaOriginal[rowID][0];
  var source = "<%=request.getContextPath()%>/UsuariosForms.do?opcion=modificar&primero="+primero;
  abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/usuariosForms/mainVentana.jsp?source="+source,variables,
	'width=805,height=550,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          document.forms[0].opcion.value="inicio";
                          document.forms[0].target="oculto";
                              document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
                          document.forms[0].submit();
                        }
                  });
}

function pulsarAltaUsuarios() {
  var segundo = "alta";
  var source = "<%=request.getContextPath()%>/UsuariosForms.do?opcion=alta&segundo="+segundo;
  abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/usuariosForms/mainVentana.jsp?source="+source,null,
	'width=805,height=550,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            numUsuarios = numUsuarios + 1;
                            if(numUsuarios>(lineasPagina*paginaSuperior) ) {
                              numeroPaginas = numeroPaginas + 1;
                              paginaSuperior = paginaSuperior + 1;
                            }
                            document.forms[0].opcion.value="inicio";
                            document.forms[0].target="";
                            document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
                            document.forms[0].submit();
                        }
                  });
}

function pulsarEliminarUsuarios() {
  if(tabUsuarios.selectedIndex != -1) {
  
    if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarUsu")%>')) {
        document.forms[0].codUsuario.value = listaOriginal[tabUsuarios.selectedIndex][0];
        document.forms[0].opcion.value="eliminarUsuario";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
        document.forms[0].submit();
    }
  } else {
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  } 
}

function pulsarModificarUsuarios() {
  if(tabUsuarios.selectedIndex != -1) {
	  datosUsuarios(tabUsuarios.selectedIndex);
  } else {
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  } 
}

function usuarioEliminado() {
  numUsuarios = numUsuarios - 1;
  if(numUsuarios==(lineasPagina*(paginaSuperior-1)) ) {
	if(numeroPaginas != 1) 	numeroPaginas = numeroPaginas - 1;
	if(paginaSuperior !=1) paginaSuperior = paginaSuperior - 1;
  }
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
  document.forms[0].submit();
}
</SCRIPT>
</head>
<body class="bandaBody"  onload="javascript:{ pleaseWait('off'); inicializar()}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>

<html:form action="/UsuariosForms.do" target="_self">

<html:hidden property="opcion" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="12"/>

<input type="hidden" name="codUsuario" value="">

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_mantUsu")%></div>
<div class="contenidoPantalla">
    <table id="tabla1" width="100%">
        <tr>
            <td id="tablaUsuarios"></td>
        </tr>
        <tr>
            <td>
                <div id="enlace"  class="dataTables_wrapper"></div>
            </td>
        </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaUsuarios" onClick="pulsarAltaUsuarios();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificarUsuarios" onClick="pulsarModificarUsuarios();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarUsuarios" onClick="pulsarEliminarUsuarios();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>				
</div>				
</html:form>
<script language="JavaScript1.2">
// JAVASCRIPT DE LA TABLA USUARIOS
var tabUsuarios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUsuarios'));

tabUsuarios.addColumna('185','left','<%= descriptor.getDescripcion("etiq_login")%>');
tabUsuarios.addColumna('500','left','<%= descriptor.getDescripcion("etiq_nombre")%>');
tabUsuarios.displayCabecera=true;

function refrescaUsuarios() {
  tabUsuarios.displayTabla();
}

tabUsuarios.displayDatos = pintaDatosUsuarios;

function pintaDatosUsuarios() {
  tableObject = tabUsuarios;
}

// FIN DE LOS JAVASCRIPT'S DE LA TABLA USUARIOS

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla){
        var teclaAuxiliar;
        if(window.event){
            evento = window.event;
            teclaAuxiliar  = evento.keyCode;
        }else
            teclaAuxiliar  = evento.which;

		keyDel(evento);

  if(teclaAuxiliar == 40){
    if(tabUsuarios==tableObject) {
      upDownTable(tabUsuarios,listaSel,teclaAuxiliar);
    } 
  }
  if (teclaAuxiliar == 38){
    if(tabUsuarios==tableObject) {
      upDownTable(tabUsuarios,listaSel,teclaAuxiliar);
    } 
  }
  if(teclaAuxiliar == 13){
    if((tabUsuarios.selectedIndex>-1)&&(tabUsuarios.selectedIndex < lista.length)&&(!ultimo)){
      if(tabUsuarios==tableObject)
	    callFromTableTo(tabUsuarios.selectedIndex,tabUsuarios.id);
    }
  }
  keyDel(evento);
}
</script>
</BODY>
</html:html>
