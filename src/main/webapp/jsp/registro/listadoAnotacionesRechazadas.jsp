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

<html:html>

<head>

<TITLE>::: REGISTRO ENTRADA SALIDA - LISTADO ANOTACIONES RECHAZADAS :::</TITLE>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
  int idioma=2;
  int apl=1;

  if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }

  String tipo;
  String fech;
  String numOrden;
  tipo ="res_tipoEntrada";
  fech="res_fecE";
  numOrden="res_numOrdE";

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<!-- Ficheros JavaScript -->

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script language="JavaScript1.2" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>



<SCRIPT language="JavaScript">

var lista = new Array();
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

   Vector relacionAnotaciones = (Vector) session.getAttribute("RelacionAnotaciones");
   int numRelacionAnotaciones = 0;
   if ( relacionAnotaciones != null ) numRelacionAnotaciones = relacionAnotaciones.size();

   MantAnotacionRegistroForm f= (MantAnotacionRegistroForm)  session.getAttribute("MantAnotacionRegistroForm");
   RegistroValueObject arVO = f.getRegistro();
   int ano= arVO.getAnoReg();
   int num= arVO.getNumReg();
   int pos=0;
   if ( (ano!=0) && (num!=0) ){
	   	Vector anotacion;
		boolean encontrada=false;
		while (!encontrada && (pos<relacionAnotaciones.size()))
		{
			anotacion = (Vector) relacionAnotaciones.elementAt(pos);
			String a = (String) anotacion.elementAt(3);
			String n = (String) anotacion.elementAt(4);
			if ( a.equals(String.valueOf(ano)) && n.equals(String.valueOf(num)) )
				encontrada=true;
			else pos++;
		}
		if (!encontrada) pos=0;
   }
%>

/* Para navegacion */
var enlacesPagina  = 10;
var lineasPagina   = 10;

var paginaActual   = 1;
var paginaInferior = 1;
var paginaSuperior = enlacesPagina;

var inicio = 0;
var fin    = 0;
var numeroPaginas;

function cargarInicio(){
   window.focus();
   listaSel = lista;
   numAnotacion = <%= pos%>;
   if (numAnotacion != 0){
	pag = buscarAnotacion(numAnotacion);
   }
   else pag = 1;
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
   var htmlString = " ";
   numeroPaginas = Math.ceil(<%=numRelacionAnotaciones%> /lineasPagina);

   if (numeroPaginas > 1)
   {
      htmlString += '<table border="0" cellpadding="2" cellspacing="0" align="center"><tr>'

      if (paginaActual > 1) {
		  htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
		  htmlString += '<a href="javascript:irPrimeraPagina();" class="navegacion" target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"> |<< </font></a></td>';
		  htmlString += '<td class="fondo" width="5" align="center" style="background-color: #E6E6E6"></td>';
		  htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
		  htmlString += '<a class="navegacion" href="javascript:irNPaginasAnteriores('+ eval(paginaActual) + ')" target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"> << </font></a></td>';
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
		htmlString += '<a class="navegacion" href="javascript:irNPaginasSiguientes('+ eval(eval(paginaActual))+ ')" target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"> >> </font></a></td>';
		htmlString += '<td class="fondo" width="5" align="center" style="background-color: #E6E6E6"></td>';
		htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
		htmlString += '<a href="javascript:irUltimaPagina();" class="navegacion" target="_self"><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"> >>| </font></a></td>';
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
       document.forms[0].opcion.value="cargar_paginaRechazadas";
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
   refresh();

   domlay('enlace',1,0,0,enlaces());

}

function seleccionRegistro(indice) {
	   document.forms[0].ano.value = listaOriginal[indice][0];
       document.forms[0].numero.value = listaOriginal[indice][1];
       document.forms[0].opcion.value="cancelar_anular";
       document.forms[0].target="mainFrame";
       document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
       document.forms[0].submit();
}


function pulsarSalirConsultar() {

    document.forms[0].opcion.value="Relacion_E";
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

</HEAD>

<body class="bandaBody" onload="javascript:{ 
              
            cargarInicio();}">

<html:form action="/MantAnotacionRegistro.do" target="_self">

<html:hidden  property="opcion" value=""/>
<html:hidden  property="ano" value=""/>
<html:hidden  property="numero" value=""/>
<html:hidden property="ejercicioAnotacion" />
<html:hidden property="numeroAnotacion" />
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="10"/>

<div class="txttitblanco"><%=descriptor.getDescripcion("titEntRec")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td id="tabla" width="100%" align="center" valign="top"></td>
        </tr>
        <tr>
            <td width="100%" align="center" valign="top"><div id="enlace" STYLE="position: relative; width:100%;"></div></td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdSalir" onClick="pulsarSalirConsultar();return false;">
    </div>
</div>
</html:form>

<script language="JavaScript1.2">

  var tab;
  if(document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla);
  else tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

  tab.addColumna('87',null,'<%=descriptor.getDescripcion("etiqEjerNum")%>');
  tab.addColumna('97',null,'<%=descriptor.getDescripcion("etiqFecAnot")%>');
  tab.addColumna('280',null,'<%=descriptor.getDescripcion("etiqRemAsun")%>');
  tab.addColumna('150',null,'<%=descriptor.getDescripcion("gEtiqMunicipio")%>');
  tab.addColumna('140',null,'<%=descriptor.getDescripcion("res_etiqDestino")%>');
  tab.height = 300;
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
