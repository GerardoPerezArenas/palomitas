<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Areas</title>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    var lista = new Array();
	var listaOriginal = new Array();
    var datosAreas = new Array();
    var opcion = "<%=request.getParameter("opcion")%>";

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
      if (opcion=="vuelveCargar")
      cargaTablaAreas();
      else
        parent.mainFrame.noEliminar();
    }

    function cargaTablaAreas(){
    <%
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaAreas = bForm.getListaAreas();
      int lengthAreas = listaAreas.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthAreas;i++){
        GeneralValueObject areas = (GeneralValueObject)listaAreas.get(i);%>
      datosAreas[j] = ['<%=(String)areas.getAtributo("codigo")%>',
                       '<%=(String)areas.getAtributo("descripcion")%>'];
	  listaOriginal[j] = ['<%=(String)areas.getAtributo("codigo")%>',
	                   '<%=(String)areas.getAtributo("codCampo")%>',
					   '<%=(String)areas.getAtributo("idioma")%>',
                       '<%=(String)areas.getAtributo("descripcion")%>',
					   '<%=(String)areas.getAtributo("descIdioma")%>'];
      lista[j] = datosAreas[j];
      j++;
    <%}%>
      parent.mainFrame.recuperaDatos(lista,listaOriginal);
    }

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
