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
	  
	  Vector listaEntidades = bForm.getListaEntidades();
	  if (listaEntidades == null) listaEntidades = new Vector();	  
      int lengthEntidades = listaEntidades.size();
	  
   	  for(int i=0;i<lengthEntidades;i++){
        GeneralValueObject entidades = (GeneralValueObject)listaEntidades.get(i); %>
      codigos[<%=i%>] = '<%=(String)entidades.getAtributo("codEntidad")%>';
      descripciones[<%=i%>] = '<%=(String)entidades.getAtributo("descEntidad")%>';

    <%}%>

	// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
	function inicializar(){
			parent.mainFrame.cargarEntidades(codigos, descripciones);
	}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
