<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<%
String cadena=null;
if ( (request.getParameter("op")!=null) && (request.getParameter("op").equals("GEP")) ) {
	cadena="editorWord2.jsp";
	} else if ( (request.getParameter("op")!=null) && ( (request.getParameter("op").equals("AE"))  || (request.getParameter("op").equals("ME"))) ) {
	cadena="MantenimientoEntidades.jsp";
	} else cadena="MantenimientoEstructuras.jsp";
%>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]-->   
  <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="about:blank" name="menu" class="iframeMenu" style="display:none"></iframe>
    <iframe src='./<%=cadena%>?op=<%=request.getParameter("op")%>&id=<%=request.getParameter("id")%>&codApliInforme=<%=request.getParameter("codApliInforme")%>' name="mainFrame" class="iframeUnica" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
    </body>
</html>


