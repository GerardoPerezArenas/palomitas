<!doctype html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
 <% UsuarioValueObject usuario=new UsuarioValueObject();
	String soloConsulta = "no";
	if (session!=null){
      usuario = (UsuarioValueObject)session.getAttribute("usuario");
      if (usuario!=null) {
        soloConsulta = usuario.getSoloConsultarExp();
      }
    }%>
<html>
<head>
    
    <meta http-equiv="X-UA-Compatible" content="IE=10"/>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">


  <title> Gestión de Expedientes </title>
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="<c:url value='/jsp/menuLateral.jsp'/>" name="menu" class="iframeMenu" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    
  <% if(soloConsulta.equals("no")) { %>
   <!--Cuando el cliente sea CRTVG se usará esta línea comentada en lugar de la original para que el cliente acceda
   a los expedientes pendientes solo de su unidad.
   <frame src="<c:url value='/sge/Tramitacion.do?opcion=pendientesEstaUnidad'/>" name="mainFrame" 
             noresize scrolling="NO" marginwidth="0" marginheight="0">
   -->
    <iframe src="<c:url value='/sge/Tramitacion.do?opcion=inicio'/>" name="mainFrame" class="iframeMainFrame" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
  <% } else { %>
     <iframe src="<c:url value='/sge/ConsultaExpedientes.do?opcion=inicio'/>" name="mainFrame" class="iframeMainFrame" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
  <% } %>
    <SCRIPT type="text/javascript">
        top.focus();
    </script>
    </body>
</html>
