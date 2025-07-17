<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Entidades</title>
  <script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosEntidades = new Array();
	<% 
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
	  String operacion = bForm.getOperacion();
	%>
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			<% if("noPuedeEliminar".equals(operacion)) { %>
			  noPuedeEliminar();
			<% } else { %>
			  cargaTablaEntidades();
			<% } %>
		}
		
		function cargaTablaEntidades(){
		<% 
      Vector listaEntidades = bForm.getListaEntidades();
      int lengthEntidades = listaEntidades.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthEntidades;i++){
        GeneralValueObject entidades = (GeneralValueObject)listaEntidades.get(i);%>
      datosEntidades[j] = ['<%=(String)entidades.getAtributo("codEntidad")%>',
        '<%=(String)entidades.getAtributo("descEntidad")%>',
        '<%=(String)entidades.getAtributo("directorio")%>',
        '<%=(String)entidades.getAtributo("tipoEntidad")%>'];
			lista[j] = datosEntidades[j];
      j++;
    <%}%>
			parent.mainFrame.cargarTablaEntidades(lista);
		}
		
		function noPuedeEliminar() {
		  parent.mainFrame.noPuedeEliminar();
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
