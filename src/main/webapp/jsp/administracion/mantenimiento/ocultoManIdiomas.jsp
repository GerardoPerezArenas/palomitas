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
		function inicializar(){
			cargaTablaIdiomas();
		}
		
		function cargaTablaIdiomas(){
		<% 
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaIdiomas = bForm.getListaIdiomas();
      int lengthIdiomas = listaIdiomas.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthIdiomas;i++){
        GeneralValueObject idiomas = (GeneralValueObject)listaIdiomas.get(i);%>
      datosIdiomas[j] = ['<%=(String) idiomas.getAtributo("codigo")%>',
                    '<%=(String) idiomas.getAtributo("clave")%>', '<%=(String) idiomas.getAtributo("descripcion")%>'];
			lista[j] = datosIdiomas[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
