<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%String source = request.getParameter("source");
    String modif = request.getParameter("modif");
    String codigoDomicilio = request.getParameter("codigoDomicilio");
    String modoInicio = request.getParameter("modoInicio");
    source = source + "?modif=" + modif + "&codigoDomicilio=" + codigoDomicilio + "&modoInicio=" + modoInicio;
%>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]-->  
  <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="about:blank" name="menu" class="iframeMenu" style="display:none"></iframe>
    <iframe src="<%=source%>" name="mainFrame" class="iframeUnica" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
