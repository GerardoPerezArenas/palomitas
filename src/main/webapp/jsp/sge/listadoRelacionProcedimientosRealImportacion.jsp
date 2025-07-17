<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm" %>
<%@page import="java.util.Vector"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES - LISTADO RELACION PROCEDIMIENTOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=0;
  int apl=0;
  String funcion = "";

  if ((session.getAttribute("usuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<style type="text/css">
   TR.gris TD {background-color:#C0C0C0;color:white;}
</style>
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
   Vector relacionProcedimientos = (Vector) session.getAttribute("RelacionProcedimientos");
   int numRelacionProcedimientos = 0;
   if ( relacionProcedimientos != null ) numRelacionProcedimientos = relacionProcedimientos.size();

   DefinicionProcedimientosForm f= (DefinicionProcedimientosForm)  session.getAttribute("DefinicionProcedimientosForm");
   DefinicionProcedimientosValueObject arVO = f.getDefinicionProcedimientos();
   String codMunicipio= arVO.getCodMunicipio();
   String codProcedimiento= arVO.getTxtCodigo();
   int pos=0;
   if ( (codMunicipio !=null) && (codProcedimiento !=null) ){
    DefinicionProcedimientosValueObject procedimiento = new DefinicionProcedimientosValueObject();
    boolean encontrada=false;
    while (!encontrada && (pos<relacionProcedimientos.size()))
    {
      procedimiento = (DefinicionProcedimientosValueObject) relacionProcedimientos.elementAt(pos);
      String m = procedimiento.getCodMunicipio();
      String t = procedimiento.getTxtCodigo();
      if ( m.equals(codMunicipio) && t.equals(codProcedimiento) ) {
        encontrada=true;
      }
      else pos++;
    }
    if (!encontrada) pos=0;
   }
   String importar = arVO.getImportar();
%>

/* Para navegacion */
var lineasPagina   = 15;
var paginaActual   = 1;
var numeroPaginas= Math.ceil(<%=numRelacionProcedimientos%>/lineasPagina);

function cargarInicio()
{
   listaSel = lista;
   numAnotacion = <%= pos%>;
   document.forms[0].importar.value = '<%= importar %>';
   if (numAnotacion != 0){
     pag = buscarAnotacion(numAnotacion);
   }
   else pag = 1;
   cargaPagina(pag);
}
function buscarAnotacion(i) {
  paginaActual=1;
  i = i+1;
  paginaActual = Math.ceil(i/lineasPagina);
  return paginaActual;
}
function enlaces() {
    numeroPaginas = Math.ceil(<%=numRelacionProcedimientos%> /lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPagina');
}
function cargaPagina(numeroPagina){    
  window.focus();
  lista = new Array();
  listaOriginal= new Array();
  paginaActual = numeroPagina;
  document.forms[0].paginaListado.value = numeroPagina;
  document.forms[0].numLineasPaginaListado.value = lineasPagina;
  document.forms[0].opcion.value="cargar_pagina";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
  document.forms[0].submit();
}
function inicializaLista(numeroPagina){
   var j = 0;
   paginaActual = numeroPagina;
   listaP = new Array();
   inicio =0;
   fin = lineasPagina;
   listaP = listaSel;
   tab.lineas=listaP;
   refresh();
   domlay('enlace',0,0,0,'');
   domlay('enlace',1,0,0,enlaces());
}
function seleccionRegistro(indice) { 
       document.forms[0].codMunicipio.value = listaOriginal[indice][3];
       document.forms[0].codProcedimiento.value = listaOriginal[indice][1];
       document.forms[0].opcion.value="recargaConsultaProcReal";
       document.forms[0].target="mainFrame";
       document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
       document.forms[0].submit();
}
function pulsarSalirConsultar() {

    document.forms[0].opcion.value="inicio";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
    document.forms[0].submit();
 }
function checkKeysLocal(evento,tecla) {
   var teclaAuxiliar = "";
   if(window.event){
       evento = window.event;
       teclaAuxiliar = evento.keyCode;
   }else
       teclaAuxiliar = evento.which;

   if ('Alt+C' == tecla) pulsarSalirConsultar();

    //if (event.keyCode == 38 || event.keyCode == 40){
   if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
       upDownTable(tab,lista,teclaAuxiliar);
  }
  //if(event.keyCode == 13){
  if(teclaAuxiliar == 13){
    if((tab.selectedIndex>-1)&&(!tab.ultimoTable)){
      callFromTableTo(tab.selectedIndex,tab.id);
    }
    }
    keyDel(evento);
  }
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{
              
            cargarInicio();}">
<html:form action="/MantAnotacionRegistro.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codMunicipio" value=""/>
<html:hidden  property="codProcedimiento" value=""/>
<html:hidden property="ejercicioAnotacion" />
<html:hidden property="numeroAnotacion" />
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="10"/>
<input type="hidden" name="importar" value="">
<div class="txttitblanco"><%=descriptor.getDescripcion("gEtiqProc")%></div>
<div class="contenidoPantalla">
    <TABLE width="100%" >
        <TR>
            <TD id="tabla"></TD>
        </TR>
        <TR>
            <TD><div id="enlace"  class="dataTables_wrapper"></div></TD>
        </TR>
    </TABLE>
    <DIV id="capaBotonesConsulta" name="capaBotonesConsulta" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdSalir" onclick="pulsarSalirConsultar();return false;">
    </DIV>
</div>
</html:form>
<script type="text/javascript">
var tab = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
    '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
    '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
    '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
    '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
tab.addColumna('200','center',"<%=descriptor.getDescripcion("etiqCodProc")%>");
tab.addColumna('700','left',"<%=descriptor.getDescripcion("etiqDescProc")%>");
tab.displayCabecera=true;
tab.displayTabla();
tab.colorLinea=function(rowID) {
  var fueraDePlazo = listaOriginal[rowID][4];
  if(fueraDePlazo=="si")
    return 'gris';
}
function refresh(){
  tab.displayTabla();
}
  function callFromTableTo(rowID,tableName){
    if(tab.id == tableName){
      //var p = paginaActual - 1;
      //fila = (lineasPagina*p + parseInt(rowID));
      fila=parseInt(rowID);
      seleccionRegistro(fila);
    }
  }
</script>
</BODY>
</html:html>

