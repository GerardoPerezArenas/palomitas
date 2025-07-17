<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="es.altia.agora.interfaces.user.web.sge.DocumentosExpedienteForm"%>
<%
 DocumentosExpedienteForm docForm=(DocumentosExpedienteForm)session.getAttribute("DocumentosExpedienteForm");
%>
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>Oculto Documentos de Expediente</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<script>
var opcion = "<%=docForm.getResultado()%>";
function grabarDocumento(){
}
function redireccionaOpcion(){
	if(opcion=="ok") grabarDocumento();
	else  parent.mainFrame.errorAltaDocumento();	
}
</script>
</head>
<body onload="redireccionaOpcion()">
</body>
</html>
