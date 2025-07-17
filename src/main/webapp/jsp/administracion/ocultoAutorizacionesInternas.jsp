<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.AutorizacionesInternasForm"%>
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
	var datosDecripciones = new Array();
	var posDesXDefecto;
	var desXDefecto;
	var listaGrupos = new Array();
	var listaUsuarios = new Array();
	var listaPermisosUsuario;
	var listaPermisosGrupo;
				
	// CARGA DE VALORES (DESDE JSP);		
	<% 
      	AutorizacionesInternasForm bForm = (AutorizacionesInternasForm)session.getAttribute("AutorizacionesInternasForm");
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
    <%}%>
			var codMenu = '<%=(String)od.getAtributo("codMenu")%>';
			var descMenu = '<%=(String)od.getAtributo("descMenu")%>';

			
	// GRUPOS
	<%  Vector listaGrupos = bForm.getListaGrupos();
      	int lengthGrupos = listaGrupos.size();
		for(int i=0;i<lengthGrupos;i++){
        	GeneralValueObject grupo = (GeneralValueObject)listaGrupos.get(i);
			Vector permisos = new Vector();
			permisos = (Vector) grupo.getAtributo("listaPermisos");
	%>
			listaPermisosGrupo = new Array();
	<%						
			for (int j=0; j<permisos.size(); j++) {
				GeneralValueObject permiso = (GeneralValueObject) permisos.elementAt(j);
	%>
			var cp = "<%=(String)permiso.getAtributo("codProceso")%>";
			listaPermisosGrupo[cp]='<%=(String)permiso.getAtributo("tipProceso")%>';	
	<% 		} %>

	  	listaGrupos[<%=i%>] = ['<%=(String)grupo.getAtributo("codGrupo")%>',
        				  					'<%=(String)grupo.getAtributo("descGrupo")%>'
											,listaPermisosGrupo];
    <% }
	    Vector listaUsuarios = bForm.getListaUsuarios();
      	int lengthUsuarios = listaUsuarios.size();
		for(int i=0;i<lengthUsuarios;i++){
        	GeneralValueObject usuario = (GeneralValueObject)listaUsuarios.get(i);
			Vector permisos = new Vector();
			permisos = (Vector) usuario.getAtributo("listaPermisos");
	%>
			listaPermisosUsuario = new Array();
	<%						
			for (int j=0; j<permisos.size(); j++) {
				GeneralValueObject permiso = (GeneralValueObject) permisos.get(j);
	%>
			listaPermisosUsuario['<%=(String)permiso.getAtributo("codProceso")%>']='<%=(String)permiso.getAtributo("tipProceso")%>';	
	<% 		} %>
			
	  	listaUsuarios[<%=i%>] = ['<%=(String)usuario.getAtributo("codUsuario")%>',
     				  			'<%=(String)usuario.getAtributo("codGrupo")%>',
								'<%=(String)usuario.getAtributo("nomUsuario")%>',
								'<%=(String)usuario.getAtributo("logUsuario")%>',
								listaPermisosUsuario];
    <%}%>
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			parent.mainFrame.cargarBusqueda(codMenu,descMenu,datosMenu, listaGrupos, listaUsuarios);
		}				
		
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
