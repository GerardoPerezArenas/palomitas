<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Procedimientos</title>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    var lista = new Array();
    var datosProcedimientos = new Array();
    var listaOriginal = new Array();

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
      cargaTablaProcedimientos();
    }

    function cargaTablaProcedimientos(){
    <%
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaProcedimientos = bForm.getListaProcedimientos();
      int lengthProcedimientos = listaProcedimientos.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthProcedimientos;i++){
        GeneralValueObject procedimientos = (GeneralValueObject)listaProcedimientos.get(i);%>
      datosProcedimientos[j] = ['<%=(String)procedimientos.getAtributo("codigo")%>',
                       '<%=(String)procedimientos.getAtributo("descripcion")%>'];
	  listaOriginal[j] = ['<%=(String)procedimientos.getAtributo("codigo")%>',
	                   '<%=(String)procedimientos.getAtributo("codCampo")%>',
					   '<%=(String)procedimientos.getAtributo("idioma")%>',
                       '<%=(String)procedimientos.getAtributo("descripcion")%>',
					   '<%=(String)procedimientos.getAtributo("descIdioma")%>'];
      lista[j] = datosProcedimientos[j];
      j++;
    <%}%>
      parent.mainFrame.recuperaDatos(lista,listaOriginal);
    }

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
