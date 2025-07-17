<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
<head>
<title> Relación Anotaciones</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script>

function redirecciona() {

<%
    int numRelacionAnotaciones = ((Integer)session.getAttribute("NumRelacionAnotaciones")).intValue();
    int posAnotacion = 1;
    	
%>	    
   parent.mainFrame.registroAregistro(<%=numRelacionAnotaciones%>, <%=posAnotacion%>)
}

</script>

</head>
<body onLoad="redirecciona();">



<p>&nbsp;<p>


</body>
</html>
