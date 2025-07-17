<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<html>
<head><jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
<title> Oculto Vacio </title>
<%
	DocumentosAplicacionForm daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
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
