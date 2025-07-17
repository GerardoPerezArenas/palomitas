<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>

	<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
		<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">	
		<title>Oculto Mantenimiento de Tipos de Idiomas</title>	
		<script type="text/javascript">
		
			// VARIABLES GLOBALES
			var lista = new Array();
		    var datosIdiomas = new Array();
				
			// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
			function inicializar() {
				parent.mainFrame.asignarIdiomaUsuarios();
			}
									
		</script>
	</head>
	
	<body onload="inicializar();"/>
	
</html>