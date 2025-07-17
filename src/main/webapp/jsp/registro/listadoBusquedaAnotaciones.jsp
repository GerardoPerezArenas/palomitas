<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html:html>

<head>

<TITLE>::: REGISTRO ENTRADA SALIDA - LISTADO RELACION ANOTACIONES :::</TITLE>

<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />


<%

    //=================================================================================================
    MantAnotacionRegistroForm mantARForm;
    if (request.getAttribute("MantAnotacionRegistroForm") != null) {
        mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
    } else {
        mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
    }
    //=================================================================================================
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
  int idioma=2;
  int apl=1;
  String css="";
  String funcion = "";
  String idSesion = session.getId();

  if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
    idioma = usuarioVO.getIdioma();
    css= usuarioVO.getCss();
  }

  String tipoAnotacion="E";
  
   if (mantARForm.getTipoReg()!= null) {
                tipoAnotacion =mantARForm.getTipoReg();
            }
          
  
  
  String titPag;
  String tipo;
  String fech;
  String numOrden;
  String tip;

  if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion)) ){
    titPag ="tit_BusquedaEntr";
    tipo ="res_tipoEntrada";
    fech="res_fecE";
    numOrden="res_numOrdE";
    tip="E";

  }
  else
  {
    titPag="tit_BusquedaSal";
    tipo ="res_tipoSalida";
    fech="res_fecS";
    numOrden="res_numOrdS";
    tip="S";
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
<script src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>


<SCRIPT type="text/javascript">

var lista = new Array();
// Array de booleanos, indica que anotaciones de 'lista' tienen observaciones.
// Se construye al igual que 'lista' en ocultoCargarPaginaRelacionAnotaciones.jsp
var listaObs = new Array();  
var listaOriginal = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var ultimo = false;
var pagin;
var asunto;
var apellido1;
var estado;

<% /* Recuperar el vector de anotaciones de la sesion. */

    int numRelacionAnotaciones = (Integer)session.getAttribute("NumRelacionAnotaciones");
    int pos=0;

%>

/* Para navegacion */
var enlacesPagina  = 10;
var lineasPagina   = 13;

var paginaActual   = 1;
var paginaInferior = 1;
var paginaSuperior = enlacesPagina;

var inicio = 0;
var fin    = 0;
var numeroPaginas;
var ventanaInforme;

function cargarInicio() {
    
    window.focus();
    listaSel = lista;
    var numAnotacion = <%=pos%>;
    var pag = 1;
    if (numAnotacion != 0) {
        pag = buscarAnotacion(numAnotacion);
    }

    cargaPagina(pag);
}

function irPrimeraPagina() {
	paginaActual   = 1;
	paginaInferior = 1;
	if (numeroPaginas <= enlacesPagina)
    paginaSuperior = numeroPaginas;
  else paginaSuperior = enlacesPagina;
	cargaPagina(paginaActual)
}

function irUltimaPagina() {
	paginaActual   = Math.ceil(<%=numRelacionAnotaciones%>/lineasPagina);
	paginaInferior = 1;
  if (numeroPaginas <= enlacesPagina)
    paginaSuperior = numeroPaginas;
  else {
    paginaSuperior = enlacesPagina;
    while (paginaActual > paginaSuperior) {
      paginaInferior = paginaSuperior +1;
      if (numeroPaginas > paginaInferior-1+enlacesPagina)
        paginaSuperior = paginaInferior-1+enlacesPagina;
      else paginaSuperior = numeroPaginas;
     }
  }
	cargaPagina(paginaActual)
}

function irNPaginasSiguientes(pagActual){
  pagActual = parseInt(pagActual);
  var incremento = paginaSuperior +1 - pagActual;
  if (pagActual + incremento > numeroPaginas)
      pagActual = Math.ceil(<%=numRelacionAnotaciones%>/lineasPagina); // Ultima
  else {
    pagActual +=  incremento;
    paginaInferior = pagActual;
    if (paginaInferior + enlacesPagina > numeroPaginas)
        paginaSuperior=numeroPaginas;
    else paginaSuperior=paginaInferior+enlacesPagina-1;
  }
  cargaPagina(pagActual);
}

function irNPaginasAnteriores(pagActual){

  pagActual = parseInt(pagActual);
  var incremento = enlacesPagina + (pagActual -paginaInferior);
  if (paginaActual - incremento <= 0)
      pagActual = 1; // Primera
  else {
    pagActual -= incremento;
    paginaInferior = pagActual;
    if (paginaInferior + enlacesPagina > numeroPaginas)
          paginaSuperior=numeroPaginas;
    else paginaSuperior= paginaInferior + enlacesPagina -1;
  }
  cargaPagina(pagActual);
}

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


function enlaces()
{
   var toolTipPrimeraPagina = " title='" + "<%=descriptor.getDescripcion("toolTip_bPriPagina")%>" + "'";
   var toolTipUltimaPagina = " title='" + "<%=descriptor.getDescripcion("toolTip_bUltPagina")%>" + "'";
   var toolTipPaginaAnterior = " title='" + "<%=descriptor.getDescripcion("toolTip_bAntPagina")%>" + "'";
   var toolTipPaginaSiguiente = " title='" + "<%=descriptor.getDescripcion("toolTip_bSigPagina")%>" + "'";

   var htmlString = " ";
   numeroPaginas = Math.ceil(<%=numRelacionAnotaciones%> /lineasPagina);

   if (numeroPaginas > 1)
   {
      htmlString += '<table border="0" cellpadding="2" cellspacing="0" align="center"><tr>'

      if (paginaActual > 1) {
		  htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
		  htmlString += '<a href="javascript:irPrimeraPagina();" class="navegacion" '+ toolTipPrimeraPagina +' target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-fast-backward"></span></font></a></td>';
		  htmlString += '<td class="fondo" width="5" align="center" style="background-color: #E6E6E6"></td>';
		  htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
		  htmlString += '<a class="navegacion" '+ toolTipPaginaAnterior +' href="javascript:irNPaginasAnteriores('+ eval(paginaActual) + ')" target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-backward"></span></font></a></td>';
      } else htmlString += '<td width="75" align="center" style="background-color: #E6E6E6"></td>';

      htmlString += '</td><td class="fondo" align="center" width="400" style="background-color: #E6E6E6">';

      for (var i=0; i < numeroPaginas; i++)
      {
         if (((i+1)>= paginaInferior) && (i<paginaSuperior))
         {
            if ((i+1) == paginaActual)
               htmlString += '<font style="color:#A7B996; font-family:Verdana,Arial; font-size:11px; font-weight:bold">'+ (i+1) + '&nbsp;&nbsp;</font>';
            else
			   htmlString += '<a href="javascript:cargaPagina('+ eval(i+1) + ')" class="navegacion" target="_self">'+ (i+1) + '</a>&nbsp;&nbsp;';
         }
      }

      if (paginaActual < numeroPaginas){
		htmlString += '</td><td width="35" align="center" valign="middle" bgcolor="#A7B996">';
		htmlString += '<a class="navegacion" '+ toolTipPaginaSiguiente +' href="javascript:irNPaginasSiguientes('+ eval(eval(paginaActual))+ ')" target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-forward"></span></font></a></td>';
		htmlString += '<td class="fondo" width="5" align="center" style="background-color: #E6E6E6"></td>';
		htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
		htmlString += '<a href="javascript:irUltimaPagina();" class="navegacion" '+ toolTipUltimaPagina +' target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-fast-forward"></span></font></a></td>';
    } else htmlString += '</td><td width="70" align="center" style="background-color: #E6E6E6"></td>';
	  htmlString += '</tr></table>';
   }



   var registroInferior = ((paginaActual - 1) * lineasPagina) + 1;
   var registroSuperior = (paginaActual * lineasPagina);
   if (paginaActual == numeroPaginas)
      registroSuperior = <%=numRelacionAnotaciones%> ;
   if (listaSel.length > 0)
    htmlString += '<center><font class="textoSuelto">Resultados&nbsp;' + registroInferior + '&nbsp;a&nbsp;' + registroSuperior + '&nbsp;de&nbsp;' + <%=numRelacionAnotaciones%> + '&nbsp;encontrados.</font></center>'
   else
    htmlString += '<center><font class="textoSuelto">&nbsp;' + <%=numRelacionAnotaciones%>  + '&nbsp;encontrados.</font></center>'

   return (htmlString);
}


function cargaPagina(numeroPagina){

	   lista = new Array();
	   listaOriginal= new Array();
	   document.forms[0].paginaListado.value = numeroPagina;
	   document.forms[0].numLineasPaginaListado.value = lineasPagina;
            document.forms[0].columna.value = "0";
            document.forms[0].tipoOrden.value ="";
       document.forms[0].opcion.value="cargar_pagina";
       document.forms[0].procedoRelaciones.value="relaciones";
       document.forms[0].target="oculto";
       document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
       document.forms[0].submit();
}


// Cambia pq ahora es todo el vector.
function inicializaLista(numeroPagina){

   var j = 0;

   paginaActual = numeroPagina;
   listaP = new Array();

   inicio =0;
   fin = lineasPagina;
   listaP = listaSel;

   tab.lineas=listaP;
   tab.observaciones=listaObs;
   refresh();

   domlay('enlace',1,0,0,enlaces());

}

function seleccionRegistro(indice) {
    document.forms[0].posicionAnotacion.value = (paginaActual - 1) * document.forms[0].numLineasPaginaListado.value + indice + 1;
    document.forms[0].ano.value = listaOriginal[indice][0];
    document.forms[0].numero.value = listaOriginal[indice][1];
    document.forms[0].tipo.value ="<%=tip%>";
    var datos= new Array();
    datos=[document.forms[0].ano.value,document.forms[0].numero.value, document.forms[0].tipo.value];
    self.parent.opener.retornoXanelaAuxiliar(datos);
}


function pulsarSalirConsultar() {

 <% if ("Relacion_E".equals(tipoAnotacion) || "E".equals(tipoAnotacion)){ %>
    document.forms[0].opcion.value="RelacionBusqueda_E";
 <% } else { %>
    document.forms[0].opcion.value="RelacionBusqueda_S";
 <% } %>
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
    document.forms[0].submit();
 }

function checkKeysLocal(evento,tecla){
     var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    if ('Alt+C' == tecla) pulsarSalirConsultar();

   	if(teclaAuxiliar == 38 || teclaAuxiliar == 40) upDownTable(tab,listaP,teclaAuxiliar);
	if(teclaAuxiliar == 13){
		if(tab.selectedIndex>-1 && !tab.ultimoTable) callFromTableTo(tab.selectedIndex,tab.id);
	}
	keyDel(evento);
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{pleaseWait('off'); 
              
            cargarInicio();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<html:form action="/MantAnotacionRegistro.do" target="_self">

<html:hidden  property="opcion" value=""/>
<html:hidden  property="ano" value=""/>
<html:hidden  property="ano" styleId="ano" value=""/>
<html:hidden  property="numero" value=""/>
<html:hidden property="ejercicioAnotacion" />
<html:hidden property="numeroAnotacion" />
<html:hidden property="posicionAnotacion" />
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="10"/>
<input type="hidden" name="tipo">
<html:hidden property="columna" value="0"/>
<html:hidden property="tipoOrden" value="0"/>
<html:hidden property="procedoRelaciones" />

<% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
<div class="txttitblancoder"><%=descriptor.getDescripcion(titPag)%></div>
<% } else {%>
<div class="txttitblanco"><%=descriptor.getDescripcion(titPag)%></div>
<% }%>
<div class="contenidoPantalla">
    <table width="100%" cellpadding="0px" cellspacing="0px" style="padding-top: 10px">
        <tr>
            <td id="tabla" width="100%" height="330px" align="left" style="padding-left: 45px" valign="top"></td>
        </tr>
        <tr>
            <td width="100%" align="left" style="padding-left: 45px" valign="top"><div id="enlace" STYLE="position: relative; width:100%;"></div></td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalirConsultar();return false;">
    </div>
</div>
</html:form>

<script language="JavaScript1.2">
  var  tab= new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
  tab.addColumna('97',null,'<%=descriptor.getDescripcion("etiqEjerNum")%>');
  tab.addColumna('107',null,'<%=descriptor.getDescripcion("etiqFecAnot")%>');
  <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion)) ){ %>
    tab.addColumna('340',null,'<%=descriptor.getDescripcion("etiqRemAsun")%>');
  <% } else { %>
    tab.addColumna('340',null,'<%=descriptor.getDescripcion("etiqDestAsun")%>');
  <% } %>
  tab.addColumna('140',null,'<%=descriptor.getDescripcion("gEtiqTiDoc")%>');
  <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion)) ){ %>
    tab.addColumna('180',null,'<%=descriptor.getDescripcion("res_etiqDestino")%>');
  <% } else { %>
    tab.addColumna('180',null,'<%=descriptor.getDescripcion("res_etiqOrigen")%>');
  <% } %>
  tab.displayCabecera=true;

function refresh(){
	tab.displayTabla();
}


function callFromTableTo(rowID,tableName){
	if(tab.id == tableName){
		fila=parseInt(rowID);
		seleccionRegistro(fila);
	}
}

</script>

</BODY>

</html:html>
