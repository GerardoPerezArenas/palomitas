<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Enumeration" %>
<%
  String cadena = request.getParameter("source");
  Enumeration nombres = request.getParameterNames(); 
  while (nombres.hasMoreElements()){
    String nombre = (String) nombres.nextElement();
    if (!nombre.startsWith("source")){
        String valor = request.getParameter(nombre);
        cadena = cadena + "&" + nombre + "=" + valor;
    }
  }
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]-->   
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="<%=cadena%>" name="mainFrame" class="iframeUnica" scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
</body>
</html>





