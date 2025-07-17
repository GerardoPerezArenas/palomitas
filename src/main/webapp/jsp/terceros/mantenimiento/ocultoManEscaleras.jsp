<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Escaleras</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosEscaleras = new Array();
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaEscaleras();
		}
		
		function cargaTablaEscaleras(){
		<% 
      MantenimientosTercerosForm bForm =
        (MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
      Vector listaEscaleras = bForm.getListaEscaleras();
      int lengthEscaleras = listaEscaleras.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthEscaleras;i++){
        GeneralValueObject escaleras = (GeneralValueObject)listaEscaleras.get(i);%>
      datosEscaleras[j] = ["<%=(String)escaleras.getAtributo("codigo")%>",
        "<%=(String)escaleras.getAtributo("descripcion")%>"];
			lista[j] = datosEscaleras[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
