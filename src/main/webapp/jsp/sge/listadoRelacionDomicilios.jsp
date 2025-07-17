<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: EXPEDIENTES - LISTADO RELACION DOMICILIOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->

<link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloSge.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<%

  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  ParametrosTerceroValueObject ptVO = null;
  int idioma=0;
  int apl=0;
  String funcion = "";

  if ((session.getAttribute("usuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<SCRIPT type="text/javascript">

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

<% /* Recuperar el vector de procedimientos de la sesion. */

   Vector relacionDomicilios = (Vector) session.getAttribute("RelacionDomicilios");
   int numRelacionDomicilios = 0;
   if ( relacionDomicilios != null ) numRelacionDomicilios = relacionDomicilios.size();

   BusquedaTercerosForm f= (BusquedaTercerosForm)  session.getAttribute("BusquedaTercerosForm");
   int pos=0;
   GeneralValueObject domicilio = new GeneralValueObject();
   boolean encontrada=false;
%>

/* Para navegacion */
var enlacesPagina  = 10;
var lineasPagina   = 10;

var paginaActual   = 1;
var paginaInferior = 1;
var paginaSuperior = enlacesPagina;

var inicio = 0;
var fin    = 0;

var numeroPaginas= Math.ceil(<%=numRelacionDomicilios%>/lineasPagina);
if (numeroPaginas < enlacesPagina) paginaSuperior= numeroPaginas;


function cargarInicio()
{
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
    paginaActual   = Math.ceil(<%=numRelacionDomicilios%>/lineasPagina);
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
    var incremento = paginaSuperior + 1 - pagActual;
    if (pagActual + incremento > numeroPaginas) {
        pagActual = Math.ceil(<%=numRelacionDomicilios%>/lineasPagina); // Ultima
    }else {
        pagActual += incremento;
        paginaInferior = pagActual;
        if (paginaInferior + enlacesPagina > numeroPaginas)
          paginaSuperior=numeroPaginas;
        else paginaSuperior= paginaInferior + enlacesPagina -1;
    }
    cargaPagina(pagActual);
  }

  function irNPaginasAnteriores(pagActual){
      pagActual = parseInt(pagActual);
      var incremento = enlacesPagina + (pagActual - paginaInferior);
      if (pagActual - incremento <= 0)
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
   numeroPaginas = Math.ceil(<%=numRelacionDomicilios%> /lineasPagina);

   if (numeroPaginas > 1)
   {
      htmlString += '<table border="0" cellpadding="2" cellspacing="0" align="center"><tr>'

      if (paginaActual > 1) {
      htmlString += '<td class="cabecera" width="35" align="center">&nbsp;';
      htmlString += '<a href="javascript:irPrimeraPagina();" class="navegacion" target="_self"><b><font color="white"> ';
      htmlString += '|<< </font></b>';
      htmlString += '</a></td>';
      htmlString += '<td class="fondo" width="5" align="center" style="background-color:#E6E6E6">&nbsp;</td>';
      htmlString += '<td class="cabecera" width="35" align="center">';
      htmlString += '<a class="navegacion" href="javascript:irNPaginasAnteriores('+ eval(paginaActual) + ')" target="_self">';
      htmlString += '<b><font color="white"> << </font></b>';
      htmlString += '</a></td>';
      } else htmlString += '<td class="cabecera" width="75" align="center" style="background-color:#E6E6E6" >&nbsp;</td>';

      htmlString += '</td><td class="fondo" align="center" width="400" style="background-color:#E6E6E6">';

      //for (var i=0; i < numeroPaginas; i++)      {
      //   if (((i+1)>= paginaInferior) && (i<paginaSuperior))         {
      for(var i=paginaInferior-1; i < paginaSuperior; i++){
            if ((i+1) == paginaActual)
               htmlString += '<font style="color:#A7B996; font-family:Verdana,Arial; font-size:11px; font-weight:bold">'+ (i+1) + '</font>&nbsp;&nbsp;';
            else
                     htmlString += '<a class="navegacion" href="javascript:cargaPagina('+ eval(i+1) + ')" target="_self">'+ (i+1) + '</a>&nbsp;&nbsp;';
      }
      //}

      if (paginaActual < numeroPaginas){
       htmlString += '</td><td class="cabecera" width="35" align="center">&nbsp;';
       htmlString += '<a  class="navegacion" href="javascript:irNPaginasSiguientes('+ eval(eval(paginaActual))+ ')" target="_self">';
       htmlString += '<b><font color="white"> >> ';
       htmlString += '</a></td>';
       htmlString += '<td class="fondo" width="5" align="center" style="background-color:#E6E6E6">&nbsp;</td>';
       htmlString += '<td class="cabecera" width="35" align="center">&nbsp;';
       htmlString += '<a href="javascript:irUltimaPagina();" class="navegacion" target="_self">';
       htmlString += '<b><font color="white"> >>| </font></b>';
       htmlString += '</a></td>';
    } else htmlString += '</td><td class="cabecera" width="70" align="center" style="background-color:#E6E6E6">&nbsp;</td>';
    htmlString += '</tr></table>';
   }

   var registroInferior = ((paginaActual - 1) * lineasPagina) + 1;
   var registroSuperior = (paginaActual * lineasPagina);
   if (paginaActual == numeroPaginas)
      registroSuperior = <%=numRelacionDomicilios%> ;
   if (listaSel.length > 0)
    htmlString += '<center><font class="textoSuelto">Resultados&nbsp;' + registroInferior + '&nbsp;a&nbsp;' + registroSuperior + '&nbsp;de&nbsp;' + <%=numRelacionDomicilios%> + '&nbsp;encontrados.</font></center>'
   else
    htmlString += '<center><font class="textoSuelto">&nbsp;' + <%=numRelacionDomicilios%>  + '&nbsp;encontrados.</font></center>'

   return (htmlString);
}


function cargaPagina(numeroPagina){
     lista = new Array();
     listaOriginal= new Array();
     document.forms[0].paginaListado.value = numeroPagina;
     document.forms[0].numLineasPaginaListado.value = lineasPagina;
     document.forms[0].opcion.value="cargar_pagina";
     document.forms[0].target="oculto";
     document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
     document.forms[0].submit();
	 pleaseWait1('on',top.mainFrame);	
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
   pleaseWait1('off',top.mainFrame);	

}

function seleccionDomicilio(indice) {
	 var i = ((paginaActual-1)*10)+indice;
	 document.forms[0].idDomicilio.value = i;
     document.forms[0].opcion.value="recargaDomicilio";
     document.forms[0].target="mainFrame";
     document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
     document.forms[0].submit();
}


function pulsarSalirConsultar() {
    document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
    document.forms[0].opcion.value="abrirDomic";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
    document.forms[0].submit();
 }


function checkKeysLocal(tecla){
    if ('Alt+C' == tecla){
      pulsarSalirConsultar();
  }
    if (event.keyCode == 38){
      upDownG(tab,listaP);
    }
    if (event.keyCode == 40){
      upDownG(tab,listaP);
    }
  if(event.keyCode == 13){
    if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)&&(!ultimo)){
      callFromTableTo(tab.selectedIndex,tab.id);
    }
  }
  keyDel();
}

function upDownG(tab,lista){
  var i = tab.selectedIndex;
    var selRow = eval("document.all." + tab.id + "_Row" + i);
    selRow.bgColor = TB_Fondo;
    if(event.keyCode == 40){
      tab.selectedIndex++;
        if(tab.selectedIndex == lista.length) {
          tab.selectedIndex--;
          ultimo = true;
        }
    }
    if(event.keyCode == 38){
      if(!ultimo){
          if(tab.selectedIndex > -1) tab.selectedIndex--;
        }else{
          ultimo = false;
        }
    }
    if(!ultimo){
        selRow = eval("document.all." + tab.id + "_Row" + tab.selectedIndex);
        selRow.bgColor = TB_FondoActivo;
        scrollControlG(tab);
    }
}

function scrollControlG(tab){
  var num =  TB_RowHeight+4;
  divHeight = tab.parent.style.height;
    divHeight = divHeight.slice(0,divHeight.length-2);
    var filas = new Number(divHeight);
    filas = Math.floor(filas/num)-1;
    if((tab.selectedIndex)*num < tab.parent.scrollTop){
      tab.parent.scrollTop = (tab.selectedIndex)*num;
    }else{
        if((tab.selectedIndex)*num > (tab.parent.scrollTop + (filas*num))){
          tab.parent.scrollTop = (tab.selectedIndex-filas)*num;
        }
    }
}

</SCRIPT>

</HEAD>

<BODY class="bandaBody" onload="javascript:{ 
              
            cargarInicio();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/sge/TramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="10"/>
<html:hidden  property="codTramite" />
<html:hidden  property="codMunicipio" />
<input type="hidden" name="codPais" value="<%=ptVO.getPais()%>">
<input type="hidden" name="codProvincia" value="">
<input type="hidden" name="idDomicilio" value="">
<html:hidden  property="ejercicio" />
<html:hidden  property="numeroExpediente" />

<table width="100%" height="4px" cellpadding="0px" cellspacing="0px">
	<tr>
		<td width="100%"></td>
	</tr>
</table>

<table id ="tabla1" width="660px" height="269px" align="center" cellpadding="0px" cellspacing="0px">
<tr>
    <td width="660px" height="268px">
            <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                            <td>
                                    <table id="tabla1" width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                            <tr>
                                                    <td width="100%" height="30px" bgcolor="#A7B996" class="titulo">&nbsp;<%=descriptor.getDescripcion("tit_Domicilios")%></td>
                                            </tr>
                                            <tr>
                                                    <td width="100%" height="1px" bgcolor="#666666"></td>
                                            </tr>
                                            <tr>
                                                    <td width="100%" bgcolor="#e6e6e6" valign="top" height="237px">
                                                            <table width="100%" cellpadding="0px" cellspacing="0px">
                                                                    <tr>
                                                                            <td id="tabla" width="100%" height="197px" align="center" valign="middle"></td>
                                                                    </tr>
                                                                    <tr>
                                                                            <td width="100%" align="center" valign="bottom" height="35px">
                                                                                    <div id="enlace" STYLE="position: relative; width:100%;"></div>
                                                                            </td>
                                                                    </tr>										
                                                                    <tr>
                                                                            <td width="100%" height="5px"></td>
                                                                    </tr>
                                                            </table>
                                                    </td>
                                            </tr>
                                    </table>
                            </td>
                    </tr>
            </table>
    </td>
</tr>
</table>
<div class="botoneraPrincipal">
    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarSalirConsultar();return false;" accessKey="C">
</div>


</html:form>

<script language="JavaScript1.2">

  var tab;
  if(document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla);
  else tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

  tab.addColumna('120', 'left', '<%=descriptor.getDescripcion("gEtiqMunicipio")%>');
  tab.addColumna('230', 'left','<%=descriptor.getDescripcion("gEtiqDomicilio")%>');
  tab.addColumna('210', 'left','<%=descriptor.getDescripcion("gEtiqPoblacion")%>');
  tab.addColumna('60', 'left','<%=descriptor.getDescripcion("gEtiqCodPostal")%>');
 
 tab.height = 150;
 tab.displayCabecera=true; 

 function refresh(){
  tab.displayTabla();
 }

 function callFromTableTo(rowID,tableName){
    if(tab.id == tableName){
      fila=parseInt(rowID);
      seleccionDomicilio(fila);
    }
  }

</script>

</BODY>

</html:html>
