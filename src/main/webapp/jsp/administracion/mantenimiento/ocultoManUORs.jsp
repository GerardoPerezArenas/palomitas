<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de UORs</title>

<script type="text/javascript">
var operacion_realizada = '<%= request.getAttribute("operacion_realizada")%>';
var resultadoOperacion = "realizada";

<%
MantenimientosAdminForm bForm = (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
if (bForm.getOtrosDatos() != null){
%>
// resultados de eliminar: hayRegistros, hayUsuarios y noEliminada/Modificada
resultadoOperacion = '<%= (String) ( (GeneralValueObject) bForm.getOtrosDatos()).getAtributo("respuesta") %>';
<%
}
%>

// según el resultado de eliminar
function inicializar() {
    if(operacion_realizada == 'eliminar')
        parent.mainFrame.postPulsarEliminar(resultadoOperacion);
    else if (operacion_realizada == 'modificar')
        parent.mainFrame.postPulsarModificar(resultadoOperacion);
}
</script>

</head>

<body onload="inicializar();">
</body>
</html>
