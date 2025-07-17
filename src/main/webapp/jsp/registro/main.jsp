<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.lang.String"%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>::SIGP::Registro de Entrada / Salida</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]--> 
    
    <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
    
    <%
    String opcion = request.getParameter("opcion");
    %>
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="<c:url value='/jsp/menuLateral.jsp'/>" name="menu" class="iframeMenu" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <% if ("S".equals(opcion)) { %>
        <iframe src="<c:url value='/MantAnotacionRegistro.do?opcion=S'/>" name="mainFrame" class="iframeMainFrame" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <%} else{%>
        <iframe src="<c:url value='/jsp/registro/presentacionRegistroES.jsp'/>" name="mainFrame" class="iframeMainFrame" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <%}%>
    <script type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
