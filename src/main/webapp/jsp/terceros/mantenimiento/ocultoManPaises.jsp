<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Paises</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosPaises = new Array();
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaPaises();
		}
		
		function cargaTablaPaises(){
		<% 
      MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
      Vector listaPaises = bForm.getListaPaises();
      int lengthPaises = listaPaises.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthPaises;i++){
        GeneralValueObject paises = (GeneralValueObject)listaPaises.get(i);%>
      datosPaises[j] = ["<%=(String)paises.getAtributo("codigo")%>",
        "<%=(String)paises.getAtributo("descripcion")%>",
        "<%=(String)paises.getAtributo("nombreLargo")%>",
        "<%=(String)paises.getAtributo("digitoControl")%>"];
			lista[j] = datosPaises[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
