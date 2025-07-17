<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.formularios.mantenimiento.MantenimientosFormulariosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Estados de Formularios</title>
  <script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosEstados = new Array();
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaEstados();
		}
		
		function cargaTablaEstados(){
		<% 
      MantenimientosFormulariosForm bForm =
        (MantenimientosFormulariosForm)session.getAttribute("MantenimientosFormulariosForm");
      Vector listaEstados = bForm.getListaEstadosFormularios();
      int lengthEstados = listaEstados.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthEstados;i++){
        GeneralValueObject estados = (GeneralValueObject)listaEstados.get(i);%>
      datosEstados[j] = ['<%=(String)estados.getAtributo("codigo")%>',
        '<%=(String)estados.getAtributo("descripcion")%>'];
			lista[j] = datosEstados[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
