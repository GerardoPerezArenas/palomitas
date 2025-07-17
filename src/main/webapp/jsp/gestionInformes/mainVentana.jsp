<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<%String source = request.getParameter("source");
  String codInforme = request.getParameter("codInforme");
  String cadena = source +"&codInforme="+codInforme;
  Log m_Log = LogFactory.getLog(this.getClass().getName());
  m_Log.debug("source EN MAINVENTANA2 "+source);
  m_Log.debug("codInforme EN MAINVENTANA2 "+codInforme);
  m_Log.debug("cadena EN MAINVENTANA2 "+cadena); 
%>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]-->   
  <jsp:include page="/jsp/gestionInformes/tpls/app-constants.jsp" />
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe name="menu" src="about:blank" style="display:none"></iframe>
    <iframe src="<%=cadena%>" name="mainFrame" class="iframeUnica" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
