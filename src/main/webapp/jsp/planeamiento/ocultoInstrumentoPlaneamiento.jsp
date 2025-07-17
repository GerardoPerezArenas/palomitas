<%@ page import="java.util.Collection"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>


<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Oculto Instrumento Planeamiento </title>

<script>

    <%
    int numRegistros = ((Collection) session.getAttribute("registrosPlaneamiento")).size();
    String registroActual = ((String) request.getAttribute("registroActual")!= null?(String) request.getAttribute("registroActual"):"-1");
    %>
function redirecciona()
{
    var numero = '<bean:write name="InstrumentoPlaneamientoForm" property="numero" scope="session"/>';
    var year = '<bean:write name="InstrumentoPlaneamientoForm" property="anho" scope="session"/>';
    parent.mainFrame.rellenaNumero(year, numero);
    parent.mainFrame.navegacionConsulta(<%=numRegistros%>, <%=registroActual%>);
    parent.mainFrame.pulsarCancelar();
    parent.mainFrame.deshabilitarCampos();
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
