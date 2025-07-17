<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]-->   
  <jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="<html:rewrite page='/jsp/menuLateral.jsp'/>" name="menu" class="iframeMenu" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <iframe src="<html:rewrite page='/planeamiento/CargarInstrumentoPlaneamiento.do?tipoRegistro=1&consultando=true'/>" name="mainFrame" class="iframeMainFrame" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
