<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Relacion/Expedientes :::</TITLE>
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
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>

<SCRIPT language="JavaScript">

    var datos = new Array();
    var datosOriginal = new Array();
    var cont = 0;

    function inicializar() {
      window.focus();
    }

    function pulsarAceptar() {
        if (document.forms[0].group[1].checked)
            self.parent.opener.retornoXanelaAuxiliar("1");
        else
            self.parent.opener.retornoXanelaAuxiliar("0");
    }

    function pulsarCancelar() {
      self.parent.opener.retornoXanelaAuxiliar();
    }

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">

<html:form action="/sge/TramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>

<table width="100%" height="15px" cellpadding="0px" cellspacing="0px">
	<tr>
		<td width="100%" height="10px"></td>
	</tr>
</table>

<table width="400px" height="150px" align="center" cellpadding="0px" cellspacing="0px">
    <tr>
        <td width="399px" height="150px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                        <tr>
                                <td>
                                        <table id="tabla1" width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                                <tr>
                                                        <td width="100%" height="30px" bgcolor="#7B9EC0" class="titulo">&nbsp;<%=descriptor.getDescripcion("etiq_PregDocu")%></td>
                                                </tr>
                                                <tr>
                                                        <td width="100%" height="1px" bgcolor="#666666"></td>
                                                </tr>
                                                <tr>
                                                        <td width="100%" bgcolor="#e6e6e6" align="center" valign="top">
                                                                <table cellspacing="0px" cellpadding="0px" border="0px">
                                                                        <tr>
                                                                                <td height="30px"></td>
                                                                        </tr>
                                                                        <tr>
                                                                                <td class="etiqueta" height="30px">
                                            <input type="radio" name="group" value="0" checked>&nbsp;<%=descriptor.getDescripcion("etiq_PregDocu1")%>
                                    </td>
                                                                        </tr>
                                <tr>
                                    <td class="etiqueta" height="30px">
                                            <input type="radio" name="group" value="1">&nbsp;<%=descriptor.getDescripcion("etiq_PregDocu2")%>
                                    </td>
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
    <input type="button" class="botonGeneral" accesskey="A" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" onClick="pulsarAceptar();"/>
    <input type="button" class="botonGeneral" accesskey="C" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onClick="pulsarCancelar();"/>
</div>

</html:form>
</BODY>

</html:html>
