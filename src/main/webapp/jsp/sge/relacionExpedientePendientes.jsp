<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.RelacionExpedientesForm"%>
<%@ page import="es.altia.agora.business.sge.RelacionExpedientesValueObject"%>

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

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>::: RELACIONES  Relaciones Pendientes:::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <style type="text/css">
       TR.rojo TD {color:#d74328;}
       TR.negrita TD {font-weight: bold;}
    </style>

<SCRIPT type="text/javascript">
<%  String desdeFichaExpediente = "";
    if (session.getAttribute("desdeFichaExpediente") != null) {
      if ("si".equals((String) session.getAttribute("desdeFichaRelacion"))){
        desdeFichaExpediente="si";
      }
      session.removeAttribute("desdeFichaRelacion");
    }

   RelacionExpedientesForm f= (RelacionExpedientesForm)  session.getAttribute("RelacionExpedientesForm");
   RelacionExpedientesValueObject reVO = f.getRelacionExpedientes();

   // PARA EXPEDIENTES
   Vector relacionExpedientes = (Vector) session.getAttribute("RelacionExpedientes");
   int numRelacionExpedientes = 0;
   if ( relacionExpedientes != null ) numRelacionExpedientes = relacionExpedientes.size();

   String codMunicipio = reVO.getCodMunicipio();
   String codProcedimiento = reVO.getCodProcedimiento();
   String ejercicio = reVO.getEjercicio();
   String numero = reVO.getNumeroRelacion();
   int posE=0;
   if ( (codMunicipio !=null) && (codProcedimiento !=null) && (ejercicio !=null) && (numero !=null)){
    RelacionExpedientesValueObject RE = new RelacionExpedientesValueObject();
    boolean encontradaE=false;
    while (!encontradaE && (posE<numRelacionExpedientes))
    {
      RE = (RelacionExpedientesValueObject) relacionExpedientes.elementAt(posE);
      String m = RE.getCodMunicipio();
      String p = RE.getCodProcedimiento();
      String e = RE.getEjercicio();
      String n = RE.getNumeroRelacion();
      if ( m.equals(codMunicipio) && p.equals(codProcedimiento) && e.equals(ejercicio) && n.equals(numero)) {
        encontradaE=true;
      }
      else posE++;
    }
    if (!encontradaE) posE=0;
   }
%>


var listaE = new Array();
var listaOriginalE = new Array();
var listaPE = new Array();
var listaSelE = new Array();
var filaE;
var ultimoE = false;
var paginE;

/* Para navegacion Expedientes*/
var lineasPaginaE   = 12;
var paginaActualE   = 1;
var numeroPaginasE=Math.ceil((<%=numRelacionExpedientes%>) /lineasPaginaE);

// Fin navegacion expedientes

function inicializar() {
    window.focus();
    listaSelE = listaE;
    numAnotacionE = <%= posE%>;
    if (numAnotacionE != 0){
      pagE = buscarAnotacionE(numAnotacionE);
    } else 
        pagE = 1;

    cargaPagina(pagE);
}

function cargaPagina(numeroPaginaE){
  document.forms[0].paginaListadoE.value = numeroPaginaE;
  document.forms[0].numLineasPaginaListadoE.value = lineasPaginaE;
  document.forms[0].opcion.value="cargar_pagina_rel_pendientes";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
  document.forms[0].submit();
}

function inicializaLista(numeroPaginaE){
  tableObject=tabExp;
  var j = 0;
  var jE = 0;

  paginaActualE = numeroPaginaE;
  listaPE = new Array();

  listaPE = listaSelE;

  tabExp.lineas=listaPE;
  refrescaExpedientes();
  domlay('enlaceE',1,0,0,enlacesE());

}

// JAVASCRIPT DE LA TABLA DE EXPEDIENTES PENDIENTES *******************************************************************

function buscarAnotacionE(i) {
  paginaActualE=1;
  i = i+1;
  paginaActualE = Math.ceil(i/lineasPaginaE);
  return paginaActualE;
}

function enlacesE() {
    numeroPaginasE = Math.ceil(<%=numRelacionExpedientes%> /lineasPaginaE);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>',paginaActualE,numeroPaginasE,'cargaPaginaE');
}

function cargaPaginaE(numeroPaginaE){
  listaE = new Array();
  listaOriginalE= new Array();
  document.forms[0].paginaListadoE.value = numeroPaginaE;
  document.forms[0].numLineasPaginaListadoE.value = lineasPaginaE;
  document.forms[0].opcion.value="cargar_pagina_rel_pendientes";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
  document.forms[0].submit();
}

// FIN DEL JAVASCRIPT DE LA TABLA DE EXPEDIENTES PENDIENTES ***********************************************************

function pulsarIniciarRelacion() {
    document.forms[0].opcion.value="crear";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
    document.forms[0].submit();
}

function pulsarSalir() {
   window.location = "<%=request.getContextPath()%>/jsp/sge/presentacionExped.jsp";
}

function callFromTableTo(rowID,tableName){
  if(tabExp.id == tableName) cargarExpediente(rowID);
}

function cargarExpediente(i) {
  pleaseWait('on');
  document.forms[0].codMunicipio.value = listaOriginalE[i][0];
  document.forms[0].codProcedimiento.value = listaOriginalE[i][1];
  document.forms[0].ejercicio.value = listaOriginalE[i][3];
  document.forms[0].numero.value = listaOriginalE[i][4];
  document.forms[0].opcion.value="cargar";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/FichaRelacionExpedientes.do'/>";
  document.forms[0].submit();
}

// Busca el expediente que introduce el usuario
function pulsarBuscarRelacionExpedientes()
{
    var numeroRelacionExpedientes = document.forms[0].numeroRelacionExpedientes.value;
    if (numeroRelacionExpedientes.length != 0)
    {
        var url = "<c:url value='/jsp/sge/ocultoConsultaRelacionExpediente.jsp?numeroRelacionExpediente='/>";
        if(numeroRelacionExpedientes.substring(0,4)=="REL_") numeroRelacionExpedientes = numeroRelacionExpedientes.substring(4);
        
        url += numeroRelacionExpedientes;
        parent.oculto.location=url;
    }
}
// Cargo el expediente de forma directa a traves de la busqueda
// del numero de expediente
function cargarExpedienteDirecto()
{
  pleaseWait('on');
  document.forms[0].opcion.value="cargar";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/FichaRelacionExpedientes.do'/>";
  document.forms[0].submit();
}
// Muestra el aviso cuando el expediente no existe
function mostrarAviso() {
  jsp_alerta('A','<%=descriptor.getDescripcion("msjNoExistRelExpPend")%>');
  document.forms[0].numeroRelacionExpedientes.value = "";
}
</SCRIPT>
</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off');  inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include> 

<html:form action="/sge/RelacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>
<html:hidden  property="paginaListadoE" value="1"/>
<html:hidden  property="numLineasPaginaListadoE" value="12"/>
<html:hidden  property="codDepartamento" value=""/>
<html:hidden  property="codUnidadRegistro" value=""/>
<html:hidden  property="tipoRegistro" value=""/>
<html:hidden  property="ejerNum" value=""/>

<input type="hidden" name="codMunicipio" >
<input type="hidden" name="codProcedimiento" >
<input type="hidden" name="ejercicio" >
<input type="hidden" name="numero" >

<!-- Datos. -->
<div class="txttitblanco"><%=descriptor.getDescripcion("tit_RelPend")%></div>
<div class="contenidoPantalla">
    <table> 		                                                                                                 
        <TR>
            <td>
                <table width="100%">
                    <tr>
                        <td align="left" id="tablaExpedientes">
                        </td>
                    </tr>
                </table>
            </td>
        </TR>
        <tr>
            <td>
                <div id="enlaceE" class="dataTables_wrapper"></div>
            </td>
        </tr>
        <TR>
            <td>
                <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                    <tr>
                        <td style="width: 37%" align="left">
                            <table border="0px" width="100%" cellspacing="0px" cellpadding="0px">
                                <tr>
                                    <td style="width: 5%" class="etiqueta"><%= descriptor.getDescripcion("etiq_leyenda")%>:</td>
                                    <td style="width: 11%"></td>
                                    <td>
                                        <c:url value="/sge/RelacionExpedientes.do" var="fueraPlazo">
                                            <c:param name="opcion" value="fueraPlazo"/>
                                        </c:url>
                                        <a href="<c:out value='${fueraPlazo}'/>" class="fueraPlazo">
                                            <%= descriptor.getDescripcion("etiq_fueraPl")%>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td>
                                        <c:url value="/sge/RelacionExpedientes.do" var="pendientesEstaUnidad">
                                            <c:param name="opcion" value="pendientesEstaUnidad"/>
                                        </c:url>
                                        <a href="<c:out value='${pendientesEstaUnidad}'/>" class="pendientesEstaUnidad">
                                            <b><%= descriptor.getDescripcion("etiq_pendEstaUni")%></b>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td>
                                        <c:url value="/sge/RelacionExpedientes.do" var="pendientesOtraUnidad">
                                            <c:param name="opcion" value="pendientesOtraUnidad"/>
                                        </c:url>
                                        <a href="<c:out value='${pendientesOtraUnidad}'/>" class="pendientesOtraUnidad">
                                            <%= descriptor.getDescripcion("etiq_pendOtraUni")%>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td style="width: 16%" class="etiqueta" align="right"><%=descriptor.getDescripcion("etiq_numRel")%>:&nbsp;&nbsp;</td>
                        <td style="width: 34%" class="columnP">
                        </td>
                        <td align="left">
                        </td>
                    </tr>
                </table>                                                        
            </td>
        </TR>
    </TABLE>
    <div class="botoneraPrincipal">
        <html:text styleClass="inputTexto" property="numeroRelacionExpedientes" size="20" maxlength="30"
                   onkeypress="javascript:PasaAMayusculas(event);"/>&nbsp;
        <span class="fa fa-search" aria-hidden="true"  id="botonTerceros" name="botonTerceros" style="cursor:hand; border:0px none" onclick="javascript:pulsarBuscarRelacionExpedientes();"></span>
        <input type="button" class="botonLargo" accesskey="I" value='<%=descriptor.getDescripcion("gbIniciarRelacion")%>' name="cmdIniciar"   onclick="pulsarIniciarRelacion();" style="margin-left:15px">
    </div>
</div>
</html:form>

<script type="text/javascript">

// JAVASCRIPT DE LA TABLA EXPEDIENTES

var tabExp = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaExpedientes'));

tabExp.addColumna('200','left','<%= descriptor.getDescripcion("etiq_numRel")%>');
tabExp.addColumna('260','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tabExp.addColumna('400','center','<%= descriptor.getDescripcion("gEtiq_Asunto")%>');
tabExp.displayCabecera=true;

tabExp.colorLinea=function(rowID) {
  var fueraDePlazo = listaOriginalE[rowID][6];
  if(fueraDePlazo=="si")
    return 'rojo';
  var pendiente = listaOriginalE[rowID][7];
  if(pendiente=="si")
    return 'negrita';
}

var tableObject=tabExp;

function refrescaExpedientes() {
  tabExp.displayTabla();
}

tabExp.displayDatos = pintaDatosExpedientes;

function pintaDatosExpedientes() {
  tableObject = tabExp;
}
// FIN DE LOS JAVASCRIPT'S DE LA TABLA EXPEDIENTES

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla){
    var teclaAuxiliar = "";
    if(window.event){
       evento = window.event;
       teclaAuxiliar = evento.keyCode;
    } else
        teclaAuxiliar = evento.which;

  //if(event.keyCode == 40){
  if(teclaAuxiliar == 40){
    if(tabExp==tableObject) {
      upDownTable(tabExp,listaSelE,teclaAuxiliar);
    }
  }
  
  //if (event.keyCode == 38){
  if (teclaAuxiliar == 38){
    if(tabExp==tableObject) {
      upDownTable(tabExp,listaSelE,teclaAuxiliar);
    }
  }

  keyDel(evento);
}
</script>
<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>
</html:html>