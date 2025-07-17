
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Procesos</title>
<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=5;
  	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    	apl = usuarioVO.getAppCod();
	    idioma = usuarioVO.getIdioma();
	}
%>

  <script type="text/javascript">
		// VARIABLES GLOBALES
		var datosProcesosOriginal = new Array();
    	var datosProcesos = new Array();
		var posDesXDefecto
		var desXDefecto;
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaProcesos();
		}
		
		function cargaTablaProcesos(){
		<% 
      	MantenimientosAdminForm bForm = (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      	Vector listaProcesos = bForm.getListaProcesos();
      	int lengthProcesos = listaProcesos.size();
    %>
    	var j=0;
    <%for(int i=0;i<lengthProcesos;i++){
        GeneralValueObject procesos = (GeneralValueObject)listaProcesos.get(i);
		Vector ld = (Vector) procesos.getAtributo("descripcionesIdioma");
		if (ld == null) ld = new Vector();
	%>
		datosDescripciones = new Array();
	<% for(int j=0;j<ld.size();j++){

        GeneralValueObject d = (GeneralValueObject)ld.elementAt(j);
		String ci = (String)d.getAtributo("codigoIdioma");
		String di = (String)d.getAtributo("descripcionIdioma");
		String dp = StringEscapeUtils.escapeJavaScript((String)d.getAtributo("descripcion"));
	%>	
		datosDescripciones[<%=j%>] = ['<%=ci%>','<%=di%>','<%=dp%>'];
		if ('<%=idioma%>' == '<%=ci%>') {
			posDesXDefecto = <%=j%>;
			desXDefecto = '<%=dp%>';
		}
	<% } %>							
	     	datosProcesosOriginal[j] = ['<%=(String)procesos.getAtributo("codigo")%>',
        				  '<%=(String)procesos.getAtributo("formulario")%>',
						  posDesXDefecto, desXDefecto, datosDescripciones];

			datosProcesos[j++]= [desXDefecto,'<%=(String)procesos.getAtributo("formulario")%>']
    <%}%>
			parent.mainFrame.cargarTablaProcesos(datosProcesosOriginal,datosProcesos);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
