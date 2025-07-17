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

<TITLE>::: REGISTRO  Historico Buzón Web Ciudadano:::</TITLE>
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

<style type="text/css">
   TR.rojo TD {color:red;}
</style>

<style type="text/css">
   TR.gris TD {color:#808080;}
</style>
<script type="text/javascript">
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
var enlacesPagina  = 10;
var lineasPagina   = 12;
var paginaActual   = 1;
var paginaInferior = 1;
var paginaSuperior = enlacesPagina;
var inicio = 0;
var fin    = 0;

var numeroRelacionRegistros = <%=numRelacionRegistros%>;
var numeroPaginas=Math.ceil(<%=numRelacionRegistros%>/lineasPagina);
if (numeroPaginas < enlacesPagina) paginaSuperior= numeroPaginas;


// Fin navegacion Buzon

var tabBuzon;
var tabExp;

function inicializar() {
  listaSel = lista;
  numAnotacion = <%= pos%>;
  if (numAnotacion != 0){
    pag = buscarAnotacion(numAnotacion);
  }
  else pag = 1;

  cargaPagina(pag);
}

function cargaPagina(numeroPagina){
  lista = new Array();
  listaOriginal= new Array();
  document.forms[0].paginaListado.value = numeroPagina;
  document.forms[0].numLineasPaginaListado.value = lineasPagina;
  document.forms[0].opcion.value="cargar_paginaHistorico";
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
  var htmlString = " ";
  numeroPaginas = Math.ceil(numeroRelacionRegistros /lineasPagina);

  if (numeroPaginas > 1) {
    htmlString += '<table border="0" cellpadding="2" cellspacing="0" align="center"><tr>'
    if (paginaActual > 1) {
      htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">&nbsp;';
      htmlString += '<a href="javascript:irPrimeraPagina();" class="navegacion" target="_self"><b><font color="white"> ';
      htmlString += '<span class="fa fa-fast-backward"></span> </font></b>';
      htmlString += '</a></td>';
      htmlString += '<td class="fondo" width="5" align="center" style="background-color: #E6E6E6">&nbsp;</td>';
      htmlString += '<td  width="35" align="center" valign="middle" bgcolor="#A7B996">';
      htmlString += '<a class="navegacion" href="javascript:irNPaginasAnteriores('+ eval(paginaActual) + ')" target="_self">';
      htmlString += '<b><font color="white"><span class="fa fa-backward"></span></font></b>';
      htmlString += '</a></td>';
      } else htmlString += '<td  width="75" align="center" style="background-color:white">&nbsp;</td>';

    htmlString += '</td><td class="fondo" align="center" width="400" style="background-color:white">';

    //for (var i=0; i < numeroPaginas; i++) {
    //  if (((i+1)>= paginaInferior) && (i<paginaSuperior)) {
    for(var i=paginaInferior-1; i < paginaSuperior; i++){
        if ((i+1) == paginaActual)
          htmlString += '<font style="color:#A7B996; font-family:Verdana,Arial; font-size:11px; font-weight:bold">'+ (i+1) + '</font>&nbsp;&nbsp;';
        else
          htmlString += '<a class="navegacion" href="javascript:cargaPaginaB('+ eval(i+1) + ')" target="_self">'+ (i+1) + '</a>&nbsp;&nbsp;';
      }
    //}

    if (paginaActual < numeroPaginas){
      htmlString += '</td><td width="35" align="center" valign="middle" bgcolor="#A7B996">&nbsp;';
      htmlString += '<a  class="navegacion" href="javascript:irNPaginasSiguientes('+ eval(eval(paginaActual))+ ')" target="_self">';
      htmlString += '<b><font color="white"><span class="fa fa-forward"></span>';
      htmlString += '</a></td>';
      htmlString += '<td class="fondo" width="5" align="center" style="background-color:white">&nbsp;</td>';
      htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">&nbsp;';
      htmlString += '<a href="javascript:irUltimaPagina();" class="navegacion" target="_self">';
      htmlString += '<b><font color="white"><span class="fa fa-fast-forward"></span></font></b>';
      htmlString += '</a></td>';
    } else htmlString += '</td><td  align="center" style="background-color:white">&nbsp;</td>';
    htmlString += '</tr></table>';
  }

  var registroInferior = ((paginaActual - 1) * lineasPagina) + 1;
  var registroSuperior = (paginaActual * lineasPagina);
  if (paginaActual == numeroPaginas)
    registroSuperior = numeroRelacionRegistros ;
  if (listaSel.length > 0)
    htmlString += '<center><font class="textoSuelto">Resultados&nbsp;' + registroInferior + '&nbsp;a&nbsp;' + registroSuperior + '&nbsp;de&nbsp;' + numeroRelacionRegistros + '&nbsp;encontrados.</font></center>'
  else
    htmlString += '<center><font class="textoSuelto">&nbsp;' + numeroRelacionRegistros  + '&nbsp;encontrados.</font></center>'

  return (htmlString);
}

function cargaPaginaB(numeroPagina){
  lista = new Array();
  listaOriginal= new Array();
  document.forms[0].paginaListado.value = numeroPagina;
  document.forms[0].numLineasPaginaListado.value = lineasPagina;
  document.forms[0].opcion.value="cargar_paginaHistorico";
  document.forms[0].target="oculto";
  document.forms[0].action="BuzonWC.do";
  document.forms[0].submit();
}

function irNPaginasAnteriores(pagActual){

  pagActual = parseInt(pagActual);
  var incremento = enlacesPagina + (pagActual -paginaInferior+1);
  if (paginaInferior -1 <= 0)
      pagActual = 1; // Primera
  else {
    pagActual -= incremento;
    paginaInferior = pagActual;
    if (paginaInferior + enlacesPagina > numeroPaginas)
          paginaSuperior=numeroPaginas;
    else paginaSuperior= paginaInferior + enlacesPagina -1;
  }

  cargaPaginaB(pagActual);
}

function irNPaginasSiguientes(pagActual){
  pagActual = parseInt(pagActual);
  var incremento = paginaSuperior +1 - pagActual;
  if (pagActual + incremento > numeroPaginas)
      pagActual = Math.ceil(numeroRelacionRegistros/lineasPagina); // Ultima
  else {
    pagActual +=  incremento;
    pagInferior = pagActual;
    if (paginaInferior + enlacesPagina > numeroPaginas)
        paginaSuperior=numeroPaginas;
    else paginaSuperior=paginaInferior+enlacesPagina-1;
  }
  cargaPaginaB(pagActual);
}

function irPrimeraPagina() {
  paginaActual   = 1;
  paginaInferior = 1;
  if (numeroPaginas <= enlacesPagina)
      paginaSuperior = numeroPaginas;
  else paginaSuperior = enlacesPagina;
  cargaPaginaB(paginaActual)
}

function irUltimaPagina() {
  paginaActual   = Math.ceil(numeroRelacionRegistros/lineasPagina);
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
  cargaPaginaB(paginaActual)
}

// FIN DEL JAVASCRIPT DE LA TABLA DE BUZON **************************************************************


function pulsarVolver() {
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  document.forms[0].action="BuzonWC.do";
  document.forms[0].submit();
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

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_histBuzWC")%></div>
<div class="botoneraPrincipal">
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
  <div id="tabla1" class="botoneraPrincipal">
    <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbVolver")%> name="cmdVolver" onClick="pulsarVolver();">
  </div>
</div>
</html:form>



<script language="JavaScript1.2">

// JAVASCRIPT DE LA TABLA BUZON

var tabBuzon = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaBuzon'));

tabBuzon.addColumna('130','left','<%= descriptor.getDescripcion("gEtiqFecha")%>');
tabBuzon.addColumna('500','center','<%= descriptor.getDescripcion("etiq_titulo")%>');
tabBuzon.displayCabecera=true;

tabBuzon.colorLinea=function(rowID) {
  var estado = listaOriginal[rowID][2];
  if(estado == "R") {
    return 'rojo';
  } else if(estado == "A") {
    return 'gris';
  }
}

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
  
  if(event.keyCode == 13){
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
