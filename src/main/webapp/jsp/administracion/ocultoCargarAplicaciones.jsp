<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.AutorizacionesInternasForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Cargar Entidades</title>
  <script type="text/javascript">
		// VARIABLES GLOBALES
    	var codigos = new Array();
		var descripciones = new Array();
	<% 
      AutorizacionesInternasForm bForm = (AutorizacionesInternasForm)session.getAttribute("AutorizacionesInternasForm");
	  
	  Vector listaAplicaciones = bForm.getListaAplicaciones();
	  if (listaAplicaciones == null) listaAplicaciones = new Vector();	  
      int lengthAplicaciones = listaAplicaciones.size();
	  
   	  for(int i=0;i<listaAplicaciones.size();i++){
        GeneralValueObject aplicaciones = (GeneralValueObject)listaAplicaciones.get(i); %>
      codigos[<%=i%>] = '<%=(String)aplicaciones.getAtributo("codigo")%>';
      descripciones[<%=i%>] = '<%=(String)aplicaciones.getAtributo("descripcion")%>';

    <%}%>

	// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
	function inicializar(){
			parent.mainFrame.cargarAplicaciones(codigos, descripciones);
	}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
