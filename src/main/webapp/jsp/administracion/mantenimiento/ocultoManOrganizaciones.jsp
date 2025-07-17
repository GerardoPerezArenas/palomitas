<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Organizaciones</title>
  <script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosOrganizaciones = new Array();
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
			  cargaTablaOrganizaciones();
			<% } %>
		}
		
		function cargaTablaOrganizaciones(){
		<% 
      Vector listaOrganizaciones = bForm.getListaOrganizaciones();
      int lengthOrganizaciones = listaOrganizaciones.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthOrganizaciones;i++){
        GeneralValueObject organizaciones = (GeneralValueObject)listaOrganizaciones.get(i);%>
      datosOrganizaciones[j] = ['<%=(String)organizaciones.getAtributo("codigo")%>',
        '<%=(String)organizaciones.getAtributo("descripcion")%>',
        '<%=(String)organizaciones.getAtributo("icono")%>'];
			lista[j] = datosOrganizaciones[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}
		
		function noPuedeEliminar() {
		  parent.mainFrame.noPuedeEliminar();
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
