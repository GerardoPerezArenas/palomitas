<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Campos Desplegables</title>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    var lista = new Array();
    var datosCamposDesplegables = new Array();

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
      cargaTablaCamposDesplegables();
    }

    function cargaTablaCamposDesplegables(){
    <%
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaCamposDesplegables = bForm.getListaCamposDesplegables();
      int lengthCamposDesplegables = listaCamposDesplegables.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthCamposDesplegables;i++){
        GeneralValueObject CampoDesplegable = (GeneralValueObject)listaCamposDesplegables.get(i);%>
      datosCamposDesplegables[j] = ['<%=(String)CampoDesplegable.getAtributo("codigo")%>',
        '<%=(String)CampoDesplegable.getAtributo("descripcion")%>'];
      lista[j] = datosCamposDesplegables[j];
      j++;
    <%}%>
      parent.mainFrame.recuperaDatos(lista);
    }

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
