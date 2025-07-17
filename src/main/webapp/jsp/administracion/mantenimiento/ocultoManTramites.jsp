<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Tramites</title>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    var lista = new Array();
	var listaOriginal = new Array();
    var datosTramites = new Array();

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
      cargaTablaTramites();
    }

    function cargaTablaTramites(){
    <%
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaTramites = bForm.getListaTramites();
      int lengthTramites = listaTramites.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthTramites;i++){
        GeneralValueObject tramites = (GeneralValueObject)listaTramites.get(i);%>
      datosTramites[j] = ['<%=(String)tramites.getAtributo("codigo")%>',
                       '<%=(String)tramites.getAtributo("descripcion")%>'];
	  listaOriginal[j] = ['<%=(String)tramites.getAtributo("codigo")%>',
	                   '<%=(String)tramites.getAtributo("codCampo")%>',
					   '<%=(String)tramites.getAtributo("idioma")%>',
                       '<%=(String)tramites.getAtributo("descripcion")%>',
					   '<%=(String)tramites.getAtributo("descIdioma")%>'];
      lista[j] = datosTramites[j];
      j++;
    <%}%>
      parent.mainFrame.recuperaDatos(lista,listaOriginal);
    }

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
