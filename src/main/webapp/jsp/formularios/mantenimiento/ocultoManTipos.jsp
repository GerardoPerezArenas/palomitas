<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.formularios.mantenimiento.MantenimientosFormulariosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Formularios</title>
  <script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosTipos = new Array();
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaTipos();
		}
		
		function cargaTablaTipos(){
		<% 
      MantenimientosFormulariosForm bForm =
        (MantenimientosFormulariosForm)session.getAttribute("MantenimientosFormulariosForm");
      Vector listaTipos = bForm.getListaTiposFormularios();
      int lengthTipos = listaTipos.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthTipos;i++){
        GeneralValueObject tipos = (GeneralValueObject)listaTipos.get(i);%>
      datosTipos[j] = ['<%=(String)tipos.getAtributo("codigo")%>',
        '<%=(String)tipos.getAtributo("descripcion")%>'];
			lista[j] = datosTipos[j];
      j++;
    <%}%>
			parent.mainFrame.recuperaDatos(lista);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
