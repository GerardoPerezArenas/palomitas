<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
<head>
<jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<TITLE>::: ADMINISTRACION  Datos de Ejercicios:::</TITLE>
<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<SCRIPT type="text/javascript">

function inicializar() {
  var argVentana = self.parent.opener.xanelaAuxiliarArgs;
  document.forms[0].codAplicacion.value = argVentana[0];
  document.forms[0].codOrganizacion.value = argVentana[1];
  document.forms[0].codEntidad.value = argVentana[2];
  document.forms[0].baseDeDatos.value = argVentana[3];
}

function pulsarSalir() {
  self.parent.opener.retornoXanelaAuxiliar();
}



function pulsarAceptar () {
  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
    var retorno = new Array();
	retorno = [document.forms[0].baseDeDatos.value];
    self.parent.opener.retornoXanelaAuxiliar(retorno);
  }
}

function devolver () {
  var retorno = new Array();
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}


</SCRIPT>
</head>
<body class="bandaBody" onload="javascript:{ pleaseWait('off'); inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/administracion/AutorizacionesExternas.do" target="_self">
<html:hidden  property="opcion" value=""/>
<input  type="hidden" name="codAplicacion">
<input  type="hidden" name="codOrganizacion">
<input  type="hidden" name="codEntidad">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_datBD")%></div>
<div id="tabla" class="contenidoPantalla">
    <TABLE id ="tablaDatosGral" width="400px">
        <tr>
          <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("tit_datBD")%>:</td>
          <td width="80%" class="columnP">
            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="baseDeDatos" 
                                          size="40" maxlength="255" style="text-transform: none"/>
          </td>
        </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
      <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar"  onClick="pulsarAceptar();">
      <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();">
    </div>
</div>
</html:form>
<script language="JavaScript1.2">

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    if(window.event) evento = window.event;
    keyDel(evento);
}


</script>

</BODY>

</html:html>
