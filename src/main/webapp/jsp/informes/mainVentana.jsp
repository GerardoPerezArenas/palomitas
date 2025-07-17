<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
  String source = request.getParameter("source");
  String ventana = request.getParameter("ventana");
  String operacion = request.getParameter("operacion");
  String tipoFichero = request.getParameter("tipoFichero");	
  String codPais = request.getParameter("codPaisHP");
  String codProvincia = request.getParameter("codProvinciaHP");
  String codMunicipio = request.getParameter("codMunicipioHP");
  String distrito = request.getParameter("distritoHP");
  String seccion = request.getParameter("seccionHP");
  String hoja = request.getParameter("hojaHP");
  String version = request.getParameter("versionHP");
  String histHab = request.getParameter("histHab");
  String familia = request.getParameter("familiaHP");
  String letraHoja = (String) request.getParameter("letraHP");
  String formato = request.getParameter("formato");
  String idTercero = request.getParameter("idTercero");
  String fechaHasta = request.getParameter("fechaHasta");
  String cadena = source+"&ventana="+ventana+"&operacion="+operacion+"&tipoFichero="+tipoFichero +"&codPaisHP="+codPais+"&codProvinciaHP="+codProvincia+"&codMunicipioHP="+codMunicipio +"&distritoHP="+distrito +"&seccionHP="+seccion +"&hojaHP="+hoja +"&versionHP="+version+"&histHab="+histHab+"&letraHP="+letraHoja+"&familiaHP="+familia+"&formato="+formato+"&idTercero="+idTercero+"&fechaHasta="+fechaHasta;

  Log m_log = LogFactory.getLog(this.getClass().getName());
  if(m_log.isDebugEnabled()) m_log.debug(cadena);  
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]-->  
  <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/reloj.js'/>"></script>
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none" onload="ocultaReloj()"></iframe>
    <iframe src="about:blank" name="menu" class="iframeMenu" style="display:none"></iframe>
    <iframe src="<%=response.encodeURL(cadena)%>" name="mainFrame" class="iframeUnica" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
