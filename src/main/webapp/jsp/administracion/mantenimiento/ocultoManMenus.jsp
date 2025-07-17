
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Menus</title>
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
		var datosMenu = new Array();
		var datosProcesosOriginal = new Array();
		var datosProcesos = new Array();
		var procesosAsignados = new Array();
		var posDesXDefecto
		var desXDefecto;
		
		// CARGA DE VALORES (DESDE JSP);
		
				<% 
      	MantenimientosAdminForm bForm = (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
		GeneralValueObject od = bForm.getOtrosDatos();
      	Vector listaMenus = bForm.getListaMenus();
		if (listaMenus == null) listaMenus = new Vector();
      	int lengthMenus = listaMenus.size();
    %>
    <%for(int i=0;i<lengthMenus;i++){
        GeneralValueObject menu = (GeneralValueObject)listaMenus.get(i);
		Vector ld = (Vector) menu.getAtributo("descripcionesIdioma");
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
	     	datosMenu[<%=i%>] = ['<%=(String)od.getAtributo("codMenu")%>',
								  '<%=(String)menu.getAtributo("elemento")%>',
										'<%=(String)menu.getAtributo("padre")%>',
										'<%=(String)menu.getAtributo("proceso")%>',
        				  				'<%=(String)menu.getAtributo("formulario")%>',
						  posDesXDefecto, desXDefecto, datosDescripciones];
			procesosAsignados[procesosAsignados.length]=datosMenu[<%=i%>][3];						 
    <%}%>
			var codMenu = '<%=(String)od.getAtributo("codMenu")%>';
			var descMenu = '<%=(String)od.getAtributo("descMenu")%>';

	// PROCESOS
	<%  Vector listaProcesos = bForm.getListaProcesos();
      	int lengthProcesos = listaProcesos.size();
		for(int i=0;i<lengthProcesos;i++){
        	GeneralValueObject procesos = (GeneralValueObject)listaProcesos.get(i);
			Vector ld = (Vector) procesos.getAtributo("descripcionesIdioma");
			if (ld == null) ld = new Vector();
	%>
		datosDescripciones = new Array();
	<%     for(int j=0;j<ld.size();j++){
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
	<% 		} %>							
	     	datosProcesosOriginal[<%=i%>] = ['<%=(String)procesos.getAtributo("codigo")%>',
        				  					'<%=(String)procesos.getAtributo("formulario")%>',
						  					posDesXDefecto, desXDefecto, datosDescripciones
											,buscarProcesoAsignado('<%=(String)procesos.getAtributo("codigo")%>')];
			datosProcesos[<%=i%>]= [desXDefecto,'<%=(String)procesos.getAtributo("formulario")%>']
    <%}%>

		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			parent.mainFrame.cargarMenu(codMenu,descMenu,datosMenu, datosProcesosOriginal,datosProcesos);
		}
		
		function buscarProcesoAsignado(codigo){
			for (var j=0; j<procesosAsignados.length;j++){
				if (procesosAsignados[j]==codigo) return 1;
			}
			return 0;
		}

		
		
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
