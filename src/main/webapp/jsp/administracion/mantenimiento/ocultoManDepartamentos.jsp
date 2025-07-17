<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Departamentos</title>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    var lista = new Array();
    var datosDepartamentos = new Array();

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
      cargaTablaDepartamentos();
    }

    function cargaTablaDepartamentos(){
    <%
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaDepartamentos = bForm.getListaDepartamentos();
      int lengthDepartamentos = listaDepartamentos.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthDepartamentos;i++){
        GeneralValueObject departamentos = (GeneralValueObject)listaDepartamentos.get(i);%>
      datosDepartamentos[j] = ['<%=(String)departamentos.getAtributo("codigo")%>',
        '<%=(String)departamentos.getAtributo("descripcion")%>'];
      lista[j] = datosDepartamentos[j];
      j++;
    <%}%>
      parent.mainFrame.recuperaDatos(lista);
    }

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
