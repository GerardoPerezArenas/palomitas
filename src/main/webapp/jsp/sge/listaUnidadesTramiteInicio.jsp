<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: LISTA PROCEDIMIENTOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%int idioma=1;
  int apl=4;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
        }
  }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<SCRIPT type="text/javascript">

var listaTabla = new Array();
var listaCodigos = new Array();

function inicializar() {

  // Creamos listado de unidades disponibles
  var i=0;
  <logic:iterate id="uor" scope="session" name="unidadesTramiteInicio">
    listaTabla[i] = [ '<bean:write name="uor" property="uor_cod_vis"/>',
                      '<bean:write name="uor" property="uor_nom"/>'];
    listaCodigos[i]='<bean:write name="uor" property="uor_cod"/>';
    i++;
  </logic:iterate>

  tab.lineas=listaTabla;
  tab.displayTabla();
  window.focus();

  var nombreTramite = self.parent.opener.xanelaAuxiliarArgs;
  document.getElementById('msjSeleccionarUnidad').innerHTML = '&nbsp;&nbsp;' +
      '<%=descriptor.getDescripcion("msjUtrTramite")%> ' + nombreTramite + ':';
}

function pulsarAceptar() {
  i = tab.selectedIndex;
  if (i >= 0) {
    self.parent.opener.retornoXanelaAuxiliar(listaCodigos[i]);
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

function callFromTableTo(rowID,tableName){
  if(tab.id == tableName){
    pulsarAceptar();
  }
}
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{inicializar();}">
<form>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titUtrTramite")%></div>
    <div class="contenidoPantalla">
        <table width="100%" cellpadding="0px" cellspacing="0px" style="padding-top: 10px">
            <tr>
                <td class="etiqueta"><span id="msjSeleccionarUnidad">&nbsp;</span></td>
            <tr>
                <td id="tabla" style="width: 100%" align="center" valign="middle"></td>
            </tr>
            <tr>
                <td style="width: 100%" align="center" valign="top"><div id="enlace" STYLE="position: relative; width:100%;"></div></td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" accesskey="A" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();">
        </div>
    </div>
</form>
<script type="text/javascript">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),488);
tab.addColumna('120','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('355','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tab.displayCabecera=true;

    function rellenarDatos(tableName,rowID){
      tableObject = tableName;
      var selRow = eval("document.all." + tableName.id + "_Row" + tableName.selectedIndex);

}
function checkKeysLocal(evento,tecla) {
   var teclaAuxiliar="";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;
    keyDel(evento);

  if ( (teclaAuxiliar == 40) || (teclaAuxiliar == 38)){
    upDownTable(tab,datos,teclaAuxiliar);
  }
}
</script>

<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>
</html:html>


