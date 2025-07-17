<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaRelacionExpedientesForm"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: LISTA PROCEDIMIENTOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

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

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

<SCRIPT language="JavaScript">

var datos = new Array();
var datosOriginal = new Array();
var cont = 0;

function inicializar() {
  <% FichaRelacionExpedientesForm expForm=new FichaRelacionExpedientesForm();
     expForm = (FichaRelacionExpedientesForm)session.getAttribute("FichaRelacionExpedientesForm");
	 Vector listaUnidadesUsuario = new Vector();
	 listaUnidadesUsuario = expForm.getListaUnidadesUsuario();
     for(int i=0;i<listaUnidadesUsuario.size();i++) {
	   GeneralValueObject g = new GeneralValueObject();
	   g = (GeneralValueObject) listaUnidadesUsuario.elementAt(i);
  %>
	 datos[cont] = ['<%=(String)g.getAtributo("codUnidadTramitadora")%>',
	 				'<%=(String)g.getAtributo("descUnidadTramitadora")%>'];
	 cont++;
  <%	   
	 }
  %>
  tab.lineas=datos;
  tab.displayTabla();
  top.focus();
}

function pulsarAceptar() {
  if(tab.selectedIndex != -1) {
    self.parent.opener.retornoXanelaAuxiliar(datos[tab.selectedIndex][0]);
  } else {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{inicializar();}">
<html:form action="/sge/FichaRelacionExpedientes.do" target="_self">
<table id="tabla1" style="width:100%;height:455px" cellpadding="0px" cellspacing="0px">
    <tr>
        <td id="titulo" style="width: 100%; height: 25px" class="titulo">&nbsp;<%=descriptor.getDescripcion("titListUnidIni")%></td>
    </tr>
    <tr>
        <div class="contenidoPantalla" valign="top" style="padding-top: 5px">
            <table cellpadding="0px" cellspacing="5px" border="0px" align="center">
                <tr>
                    <td id="tablaUnidades" align="left" colspan="4"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<div class="botoneraPrincipal">
    <input type="button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
    <input type="button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar"  onclick="pulsarCancelar();">
</div>
</html:form>

<script language="JavaScript1.2">
// TABLA PRIMERA
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUnidades'));

tab.addColumna('80','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('275','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tab.displayCabecera=true;

function checkKeysLocal(tecla) {
  keyDel();
}
</script>
</BODY>
</html:html>
