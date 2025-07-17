<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Numeraciones</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosNumeraciones = new Array();
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaNumeraciones();
		}
		
		function cargaTablaNumeraciones(){
		<% 
      MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
      Vector listaNumeraciones = bForm.getListaNumeraciones();
      int lengthNumeraciones = listaNumeraciones.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthNumeraciones;i++){
        GeneralValueObject numeraciones = (GeneralValueObject)listaNumeraciones.get(i);%>
      datosNumeraciones[j] = ["<%=(String)numeraciones.getAtributo("codigo")%>",
        "<%=(String)numeraciones.getAtributo("descripcion")%>"];
			lista[j] = datosNumeraciones[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
