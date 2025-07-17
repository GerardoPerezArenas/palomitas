<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page contentType="text/html; charset=iso-8859-1"
    language="java"
    import="java.util.Map"
 %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%
  String source = request.getParameter("source");
  String primero = request.getParameter("primero");
  String segundo = request.getParameter("segundo");
  String tercero = request.getParameter("tercero");
  String cuarto = request.getParameter("cuarto");
  String quinto = request.getParameter("quinto");

  String cadena = source +"&primero="+primero+"&segundo="+segundo+"&tercero="+tercero+"&cuarto="+cuarto+"&quinto="+quinto;
  Log m_log = LogFactory.getLog(this.getClass().getName());
  if(m_log.isDebugEnabled()) m_log.debug(cadena);
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=10"/>
    <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <!--[if lte IE 9]> 
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
    <![endif]-->
  </head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="about:blank" name="menu" style="display:none"></iframe>
    <iframe src="<%=cadena%>" name="mainFrame" class="iframeUnica" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
