<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de UsoViviendas</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosUsoViviendas = new Array();
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaUsoViviendas();
		}
		
		function cargaTablaUsoViviendas(){
		<% 
      MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
      Vector listaUsoViviendas = bForm.getListaUsoViviendas();
      int lengthUsoViviendas = listaUsoViviendas.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthUsoViviendas;i++){
        GeneralValueObject usoViviendas = (GeneralValueObject)listaUsoViviendas.get(i);%>
      datosUsoViviendas[j] = ["<%=(String)usoViviendas.getAtributo("codUsoVivienda")%>",
        "<%=(String)usoViviendas.getAtributo("descUsoVivienda")%>"];
			lista[j] = datosUsoViviendas[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
