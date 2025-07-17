<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>

<html>
<head>
<jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
<title> Ha caducado la sesion </title>
<link rel="stylesheet" href="<c:url value='/css/estilo.css'/>" type="text/css">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript">
function redirecciona(){
    var opcion ='<c:out value="${param.opcion}"/>';
    if(opcion=="irALoginCAS"){
        var urlRequest = window.top.location.href;
        var urlLogin ='<c:out value="${param.url}"/>';
        console.log("url login: "+urlLogin);
        console.log("uri antes: "+urlRequest);
        console.log("uri dp: "+encodeURIComponent(urlRequest));
        window.top.location = urlLogin + "?service=" + encodeURIComponent(urlRequest);
    } else {
        <c:if test="${requestScope.showMessage}">
            <logic:messagesPresent>
                jsp_alerta('A', '<html:messages id="msg"><c:out value="${msg}"/></html:messages>');
            </logic:messagesPresent>        
        </c:if>    
        document.forms[0].target = '_top';
        document.forms[0].error.value = "sesionCaduca";
        document.forms[0].submit();
    }
}
</script>
</head>
<body onLoad="redirecciona();" class="bandaBody">
<form action="<c:url value='/welcome.do'/>">
	<input type="hidden" name="error">
	<p>&nbsp;</p>
</form>
</body>
</html>
