<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%String source = request.getParameter("source");
  String nombre = request.getParameter("nombre");
  String tipoFichero = request.getParameter("tipoFichero");
  String directorio = request.getParameter("dir");
  String cadena = source +"&nombre="+nombre+"&tipoFichero="+tipoFichero + "&dir=" + directorio;
  Log m_log = LogFactory.getLog(this.getClass().getName());
  m_log.debug(cadena); %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]-->  
  <script type="text/javascript">
        var APP_CONTEXT_PATH="<%=request.getContextPath()%>";
    </script>
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="about:blank" name="menu" style="display:none"></iframe>
    <iframe src="<%=cadena%>" name="mainFrame" class="iframeUnica" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <script type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
