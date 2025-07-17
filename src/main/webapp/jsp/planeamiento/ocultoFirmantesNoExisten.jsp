<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>


<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Oculto Firmantes </title>

<script>

function redirecciona()
{
    <% if (request.getParameter("modif").equals("true")) {%>
        parent.mainFrame.firmantesNoExistenModificacion();
    <%} else {%>
        parent.mainFrame.firmantesNoExistenInsercion();
    <%}%>
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
