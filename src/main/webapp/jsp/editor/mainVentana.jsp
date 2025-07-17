<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%
	String source = request.getParameter("source");
	String operacion = request.getParameter("aplicacion");
	String ventana = request.getParameter("ventana");
	String modo = request.getParameter("modo");
	String cadena = source+"&aplicacion="+operacion+"&ventana="+ventana+"&modo="+modo;
    Log m_log = LogFactory.getLog(this.getClass().getName());
    if(m_log.isDebugEnabled()) m_log.debug("Cadena pasada a mainVentana (EDITOR)= " + cadena);    
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <!--[if lte IE 9]> 
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
    <![endif]-->
  </head>
<body style="margin:0">
    <iframe name="ocultoDocumentos" src="about:blank" marginwidth="0" scrolling="no" style="display:none"></iframe>
    <iframe src="about:blank" name="menuDocumentos"  style="display:none"></iframe>
    <iframe src="<%=cadena%>" marginwidth="0" name="mainFrame" class="iframeUnica" scrolling="NO"></iframe>
</body>
</html>
