<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html:html locale="true">
<head><jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
    <title><bean:message key="logon.title"/></title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script>
    function redirecciona(){
        <%
            String day = (String)session.getAttribute("day");
            String month = (String)session.getAttribute("month");
            String year = (String)session.getAttribute("year");

            if((day!=null)&&(month!=null)&&(year!=null)){

        %>
        parent.icon.location.href = "<c:url value='/jsp/escritorio/agenda.jsp?day='/>"+day+"&month="+month+"&year="+year";
        <%
            //Se eliminan de la session.
            session.removeAttribute("day");
            session.removeAttribute("month");
            session.removeAttribute("year");

            }else{
        %>
        parent.icons.location.href = "<c:url value='/jsp/escritorio/agenda.jsp'/>";
        <%
            }
        %>
    }
    </script>
    <html:base/>
</head>
<BODY onLoad="redirecciona();">
 &nbsp;&nbsp;
</body>
</html:html>

