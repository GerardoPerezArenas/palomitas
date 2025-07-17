<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Aplicaciones</title>
  <script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var datosAplicaciones = new Array();
    var ltitulos = new Array();
    
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaAplicaciones();
		}
		
		function cargaTablaAplicaciones(){
		<% 
      MantenimientosAdminForm bForm =
        (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaAplicaciones = bForm.getListaAplicaciones();
      int lengthAplicaciones = listaAplicaciones.size();
      int i = 0;
    %>
    	var j=0;
    <%for(i=0;i<lengthAplicaciones;i++){
    %>lTitulos = new Array();<%
        GeneralValueObject aplicaciones = (GeneralValueObject)listaAplicaciones.get(i);
		Vector datosTitulos = (Vector) aplicaciones.getAtributo("titulos");
        for (int t=0; t<datosTitulos.size();t++){
	        GeneralValueObject tit = (GeneralValueObject)datosTitulos.elementAt(t);
			String codIdioma = (String)tit.getAtributo("codIdioma");
			String desIdioma = (String)tit.getAtributo("desIdioma");
			String titulo = (String)tit.getAtributo("titulo");
		%>
			lTitulos[<%=t%>] = ['<%=codIdioma%>','<%=desIdioma%>','<%=titulo%>'];
			
		<%}%>
        datosAplicaciones[j] = ['<%=(String)aplicaciones.getAtributo("codigo")%>',
          '<%=(String)aplicaciones.getAtributo("descripcion")%>',
          '<%=(String)aplicaciones.getAtributo("ejecutable")%>',
          '<%=(String)aplicaciones.getAtributo("seguridad")%>',
          '<%=(String)aplicaciones.getAtributo("accesoDefecto")%>',
          '<%=(String)aplicaciones.getAtributo("diccionario")%>',
          '<%=(String)aplicaciones.getAtributo("informes")%>',
          '<%=(String)aplicaciones.getAtributo("version")%>',
          '<%=(String)aplicaciones.getAtributo("conEntidades")%>',
          '<%=(String)aplicaciones.getAtributo("conEjercicios")%>',
          '<%=(String)aplicaciones.getAtributo("icono")%>',lTitulos];

        lista[j] = [datosAplicaciones[j][0],datosAplicaciones[j][1]];
			  j++;
    <%}%>
      parent.mainFrame.datosAplicaciones = datosAplicaciones;
		parent.mainFrame.cargarTablaAplicaciones(lista);
		
		
		
		
		
		
		
		
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>