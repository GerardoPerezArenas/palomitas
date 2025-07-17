
<html>
<head><jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
    <%@ page import="es.altia.agora.interfaces.user.web.sge.DocumentosExpedienteForm"%>
<title> Oculto Vacio </title>
<%
	DocumentosExpedienteForm daForm =(DocumentosExpedienteForm)session.getAttribute("DocumentosExpedienteForm");
	String opcion=daForm.getOpcion();
%>
<script>
    function cerrarVentana(){
        self.parent.opener.retornoXanelaAuxiliar("recarga");
    }
</script>

</head>

<body onload="cerrarVentana()">

</body>

</html>

