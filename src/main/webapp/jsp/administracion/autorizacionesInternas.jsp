<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.AutorizacionesInternasForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Autorizaciones Internas </title>
	<!-- Estilos -->
	
	

<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=5;
	int codOrg = 0;
	int codEnt = 0;
	String descOrg = "";
	String descEnt = "";
        String css="";
  	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    	apl = usuarioVO.getAppCod();
	    idioma = usuarioVO.getIdioma();
		codOrg = usuarioVO.getOrgCod();
		codEnt = usuarioVO.getEntCod();
		descOrg = usuarioVO.getOrg();
		descEnt = usuarioVO.getEnt();
	                css=usuarioVO.getCss();
            }%>
            
             
	<%AutorizacionesInternasForm bForm = (AutorizacionesInternasForm)session.getAttribute("AutorizacionesInternasForm");%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
             <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
             <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/WebFXImgRadioButtonTreeItem.js"></script>
<script type="text/javascript">
    var cod_Organizaciones = new Array();
    var desc_Organizaciones = new Array();
    var cod_Aplicaciones = new Array();
    var desc_Aplicaciones = new Array();
	var cod_Entidades = new Array();
    var desc_Entidades = new Array();
	var treeUsuarios;
	var grupos = new Array(); // Indexados por nodo del arbol
	var nodosGrupos = new Array(); // De nodos indexados por codigo	
  	var usuarios = new Array(); // Indexados por nodo del arbol
	var nodosUsuariosMenu = new Array(); // De nodos indexados por codigo
	var icoUsuario = 'fa fa-folder-open';
                  var icoPtoUsuario = 'fa fa-user';
	var treeMenu;
	
  	var ptosMenu = new Array(); // Indexados por nodo del arbol
	var nodosPtosMenu = new Array(); // De nodos indexados por codigo	
	var elementosMenu = new Array();
	var icoMenu = 'fa fa-folder-open';
	var icoPtoMenu = 'fa fa-cubes';
		
    var vectorBotones = new Array();
	var vectorCamposBusqueda = new Array();
            var usuariosByGrupo = new Array();
            var nodoGrupoActualID;

	/* DATOS INICIALES */
	
	<%      
	  Vector listaOrganizaciones = bForm.getListaOrganizaciones();
	  if (listaOrganizaciones == null) listaOrganizaciones = new Vector();
      Vector listaAplicaciones = bForm.getListaAplicaciones();
	  if (listaAplicaciones == null) listaAplicaciones = new Vector();
	  Vector listaEntidades = bForm.getListaEntidades();
	  if (listaEntidades == null) listaEntidades = new Vector();

	  for(int i=0;i<listaOrganizaciones.size();i++){
        GeneralValueObject org = (GeneralValueObject)listaOrganizaciones.get(i);%>
      	cod_Organizaciones[<%=i%>] = '<%=(String) org.getAtributo("codigo")%>';
      	desc_Organizaciones[<%=i%>] = '<%=(String) org.getAtributo("descripcion")%>';
    <%}
      for(int i=0;i<listaAplicaciones.size();i++){
        GeneralValueObject aplicacion = (GeneralValueObject)listaAplicaciones.get(i);%>
      	cod_Aplicaciones[<%=i%>] = '<%=(String) aplicacion.getAtributo("codigo")%>';
      	desc_Aplicaciones[<%=i%>] = '<%=(String) aplicacion.getAtributo("descripcion")%>';
    <%}
      for(int i=0;i<listaEntidades.size();i++){
        GeneralValueObject entidad = (GeneralValueObject)listaEntidades.get(i);%>
      	cod_Entidades[<%=i%>] = '<%=(String) entidad.getAtributo("codigo")%>';
      	desc_Entidades[<%=i%>] = '<%=(String) entidad.getAtributo("descripcion")%>';
    <%}%>

		
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
	function inicializar(){
			buscarValoresIniciales();
			window.focus();
      		vectorBotones = [document.forms[0].botonModificar];
			vectorCamposBusqueda = [document.forms[0].codOrganizacion,document.forms[0].descOrganizacion
									,document.forms[0].codAplicacion,document.forms[0].descAplicacion
									,document.forms[0].codEntidad,document.forms[0].descEntidad,];
			pulsarCancelarBuscar();
			//comboOrganizacion.addItems(cod_Organizaciones, desc_Organizaciones);
			comboAplicacion.addItems(cod_Aplicaciones, desc_Aplicaciones);
			//comboEntidad.addItems(cod_Entidades, desc_Entidades);
	}
	
	function buscarValoresIniciales() {
	  document.forms[0].codOrganizacion.value = '<%=codOrg%>';
	  document.forms[0].descOrganizacion.value = '<%=descOrg%>';
	  document.forms[0].codEntidad.value = '<%=codEnt%>';
	  document.forms[0].descEntidad.value = '<%=descEnt%>';
	}
		

    // FUNCIONES DE PULSACION DE BOTONES
    function pulsarBuscar(){
		var botonBuscar = [document.forms[0].botonBuscar];
		if(validarCamposBusqueda()){
		    var vector = [document.forms[0].codOrganizacion,document.forms[0].descOrganizacion,
			              document.forms[0].codEntidad,document.forms[0].descEntidad];
			deshabilitarGeneral(vector);
			//comboOrganizacion.deactivate();
			comboAplicacion.deactivate();
			//comboEntidad.deactivate();
			deshabilitarGeneral(botonBuscar);
			document.forms[0].aplicacion.value = document.forms[0].codAplicacion.value;
			document.forms[0].organizacion.value = document.forms[0].codOrganizacion.value;
			document.forms[0].entidad.value = document.forms[0].codEntidad.value;								
        	document.forms[0].opcion.value="cargarBusqueda";
	        document.forms[0].target="oculto";
    	    document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesInternas.do";
        	document.forms[0].submit();			
		}else jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
	}
    
	
    function pulsarSalir(){

	  document.forms[0].target = "mainFrame";
      document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
      document.forms[0].submit();
    }

    function pulsarCancelarBuscar(){			
			borrarMenu();
			borrarUsuarios();
			var botonBuscar = [document.forms[0].botonBuscar];
			habilitarGeneral(botonBuscar);			
			deshabilitarGeneral(vectorBotones);
			comboAplicacion.selectItem(-1);			
			//comboOrganizacion.selectItem(-1);			
			//comboEntidad.selectItem(-1);			
			comboAplicacion.activate();
			var vector = [document.forms[0].codOrganizacion,document.forms[0].descOrganizacion,
			              document.forms[0].codEntidad,document.forms[0].descEntidad];
			habilitarGeneral(vector);
			//comboOrganizacion.activate();
			//comboEntidad.activate();
		}
		
    // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
		
    function validarCamposBusqueda(){
      var codOrg = document.forms[0].codOrganizacion.value;
	  var codApl = document.forms[0].codAplicacion.value;
	  var codEnt = document.forms[0].codEntidad.value;
      if(codOrg!="" && codApl!="" && codEnt!="")
        return true;
      return false;
    }
	
	function cargarBusqueda(codMenu,descMenu, datosMenu, listaGrupos, listaUsuarios) {
		cargarUsuarios(listaGrupos, listaUsuarios);
		cargarMenu(codMenu,descMenu, datosMenu);
		document.forms[0].codMenu.value=codMenu;
		var vector = [document.forms[0].codOrganizacion,document.forms[0].descOrganizacion,
			              document.forms[0].codEntidad,document.forms[0].descEntidad];
		deshabilitarGeneral(vector);
		//comboOrganizacion.deactivate();
		comboAplicacion.deactivate();	
		//comboEntidad.deactivate();			
		habilitarGeneral(vectorBotones);
	}
	
	function cargarMenu(codMenu,descMenu, datosMenu){
		borrarMenu();
		if (codMenu != "") {
			var padre;
			var nodo;
			var nodoMenu;
			var nodoPadre = treeMenu;		
			// Raiz.
			nodoMenu = new WebFXTreeItem(descMenu,'javascript:{actualizarProceso();}',nodoPadre,icoPtoMenu,icoPtoMenu,'webfx-tree-item-area');
			nodoPadre = nodoMenu;
			elementosMenu = new Array();
			elementosMenu = datosMenu;
			for (i=0; i<datosMenu.length; i++ ) {
				padre = datosMenu[i][2];			
				if (padre==0)
					nodoPadre = nodoMenu;
				else { 				
					nodoPadre = nodosPtosMenu[padre];
					if (nodoPadre == undefined) nodoPadre = nodoMenu; // Por hacer algo
				}
				var codigo = datosMenu[i][1];
				var desc = datosMenu[i][6];
				var proceso = datosMenu[i][3];
				nodo = new WebFXImgRadioButtonTreeItem(desc,null,1,nodoPadre,icoPtoMenu,icoPtoMenu,'webfx-tree-item-area');
	      		        ptosMenu[nodo.id]=datosMenu[i];
				nodosPtosMenu[codigo]=nodo;
				nodo.setSpanGrupo(false);						
				nodo.setSpanPermiso(false);						

			}			
			treeMenu.expand();
			treeMenu.childNodes[0].expand(); //Solo va a tener uno.
		}
	}
	
	function actualizarProceso() {
	
	}
	
	function borrarMenu(){
	  	ptosMenu = new Array();
		nodosPtosMenu = new Array();		
		if (treeMenu.childNodes.length == 1) {
			var nodo = treeMenu.childNodes[0];
			nodo.remove();			
		}
	}

	function cargarUsuarios(listaGrupos, listaUsuarios){
		borrarUsuarios();
		
		var padre;
		var nodo;		
		var nodoPadre = treeUsuarios;		
		
		for (i=0; i<listaGrupos.length; i++ ) {
			var codigo = listaGrupos[i][0];
			var desc = listaGrupos[i][1];
			nodo = new WebFXTreeItem(desc,'javascript:{actualizarPermisos();}',nodoPadre,icoPtoMenu,icoPtoMenu,'webfx-tree-item-area');
	      	grupos[nodo.id]=listaGrupos[i];			
			nodosGrupos[codigo]=nodo;
		}			
				
		for (i=0; i<listaUsuarios.length; i++ ) {
			var codigo = listaUsuarios[i][0];
			var grupo = listaUsuarios[i][1];	
			var login = listaUsuarios[i][3];	
			nodoPadre = nodosGrupos[grupo];
			if (nodoPadre != undefined) {
                                    usuarios[i]=listaUsuarios[i];
			}
		}


		treeUsuarios.expand();		

	}

	function borrarUsuarios(){
	  	usuarios = new Array();
		nodosUsuarios = new Array();		
		for (var i=treeUsuarios.childNodes.length-1; i>=0; i--) {
			var nodo = treeUsuarios.childNodes[i];
			nodo.remove();			
		}
	}
	
	function listaEntidades() {
			document.forms[0].organizacion.value=document.forms[0].codOrganizacion.value;
        	document.forms[0].opcion.value="cargarEntidades";
	        document.forms[0].target="oculto";
    	    document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesInternas.do";
        	document.forms[0].submit();	
	}			
	
	function cargarEntidades(codigos, descripciones) {
		comboEntidad.addItems(codigos,descripciones);
	}
		
	function construirPermisos(){
		var cadena ="";
		for (var ii=0; ii<elementosMenu.length; ii++){									
			var proceso = elementosMenu[ii][3];
			var nodoPtoMenu = nodosPtosMenu[elementosMenu[ii][1]];						
			cadena += proceso+'§¥' +nodoPtoMenu.getRadio()+'§¥';
		}
		return cadena;
	}
	
	function actualizarPermisos(){
		if (treeUsuarios.getSelected()) { 
			var nodo = treeUsuarios.getSelected();			
			if (nodo == treeUsuarios){ // raiz	
			    if ((nodosPtosMenu != null) && (nodosPtosMenu != "")){
				for (var ii=0; ii<elementosMenu.length; ii++){							
					var nodoPtoMenu = nodosPtosMenu[elementosMenu[ii][1]];						
					nodoPtoMenu.setSpanGrupo(false);						
					nodoPtoMenu.setSpanPermiso(false);						
				}
                                    nodoGrupoActualID = "";
                                    var listaTabla = new Array();
                                    usuariosByGrupo = new Array();
                                    tabUsuarios.lineas = listaTabla;
                                    refresca(tabUsuarios);
		            }else jsp_alerta ("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
			} else {
				if (nodo.parentNode == treeUsuarios) { // Grupo
                                    nodoGrupoActualID = nodo.id;
					datosGrupo = grupos[nodo.id];										
					var listaPermisos =datosGrupo[2]; // indexada por proceso
					for (var ii=0; ii<elementosMenu.length; ii++){
						var proceso = elementosMenu[ii][3];
						var permiso = listaPermisos[proceso];
						if (permiso==undefined) permiso=0;
						var nodoPtoMenu = nodosPtosMenu[elementosMenu[ii][1]];						
						nodoPtoMenu.setSpanGrupo(false);						
						nodoPtoMenu.setSpanPermiso(true);						
						nodoPtoMenu.setRadio(permiso);
					}

                                    var listaTabla = new Array();
                                    usuariosByGrupo = new Array();
                                    tabUsuarios.lineas = listaTabla;
                                    refresca(tabUsuarios);
                                    var j = 0;
                                    for (i=0; i<usuarios.length; i++ ) {
                                        if (usuarios[i][1] == datosGrupo[0]) {
                                            usuariosByGrupo[j] = usuarios[i];
                                            listaTabla[j] = [usuarios[i][1], usuarios[i][3]];
                                            j++;
                                        }
                                    }
                                    tabUsuarios.lineas = listaTabla;
                                    refresca(tabUsuarios);					
				} else { // usuario
                                datosUsuario = usuarios[tabUsuarios.selectedIndex];
					var listaPermisos =datosUsuario[4]; // indexada por proceso
					for (var ii=0; ii<elementosMenu.length; ii++){
						var proceso = elementosMenu[ii][3];
						// Permiso del grupo
						var grupo = nodo.parentNode;
						datosGrupo = grupos[grupo.id];										
						var listaPermisosGrupos =datosGrupo[2]; // indexada por proceso
						var permisoGrupo = listaPermisosGrupos[proceso];
						var pg = false;
						if (permisoGrupo==undefined) pg = false;
						else if (permisoGrupo==1) pg=true;
						else pg=false;												
						// fin
						var permiso = listaPermisos[proceso];
						if (permiso==undefined) permiso="-1"; // El del grupo
						var nodoPtoMenu = nodosPtosMenu[elementosMenu[ii][1]];												
						nodoPtoMenu.setSpanGrupo(true);						
						nodoPtoMenu.setSpanPermiso(true);						
						nodoPtoMenu.setRadio(permiso);
						nodoPtoMenu.setGrupo(pg);
					}					
				}
			}
		}
	}
	

                function rellenarDatos(tableName,rowID){
                    datosUsuario = usuariosByGrupo[rowID];
                    var listaPermisos =datosUsuario[4]; // indexada por proceso
                    for (var ii=0; ii<elementosMenu.length; ii++){
                        var proceso = elementosMenu[ii][3];
                        // Permiso del grupo
                        datosGrupo = grupos[nodoGrupoActualID];
                        var listaPermisosGrupos =datosGrupo[2]; // indexada por proceso
                        var permisoGrupo = listaPermisosGrupos[proceso];
                        var pg = false;
                        if (permisoGrupo==undefined) pg = false;
                        else if (permisoGrupo==1) pg=true;
                        else pg=false;
                        // fin
                        var permiso = listaPermisos[proceso];
                        if (permiso==undefined) permiso="-1"; // El del grupo
                        var nodoPtoMenu = nodosPtosMenu[elementosMenu[ii][1]];
                        nodoPtoMenu.setSpanGrupo(true);
                        nodoPtoMenu.setSpanPermiso(true);
                        nodoPtoMenu.setRadio(permiso);
                        nodoPtoMenu.setGrupo(pg);
                    }
                }
                        
	function grabarPermisos(){
		if (comprobarPadreNodo(webFXTreeHandler.selected)) { 
			var nodo = treeUsuarios.getSelected();	
			if (nodo == treeUsuarios){ // raiz	
			} else {
				var codigo="";
                                if (tabUsuarios.selectedIndex!=-1) { //usuario
                                    datosUsuario = usuariosByGrupo[tabUsuarios.selectedIndex];
                                    codigo =datosUsuario[0];
                                    document.forms[0].opcion.value="actualizarPermisosUsuario";
                                } else { // Grupo
                                    datosGrupo = grupos[nodo.id];										
                                    codigo =datosGrupo[0]; 
                                    document.forms[0].opcion.value="actualizarPermisosGrupo";					
                                }
                            nodoGrupoActualID = "";
                            var listaTabla = new Array();
                            usuariosByGrupo = new Array();
                            tabUsuarios.lineas = listaTabla;
                            refresca(tabUsuarios);
				var s = construirPermisos();
				document.forms[0].nuevosPermisos.value=s;				
				document.forms[0].codigoGU.value=codigo;
				document.forms[0].target="oculto";
				//comboOrganizacion.activate();
				var vector = [document.forms[0].codOrganizacion,document.forms[0].descOrganizacion,
			                  document.forms[0].codEntidad,document.forms[0].descEntidad];
			    habilitarGeneral(vector);
			    comboAplicacion.activate();
				//comboEntidad.activate();
				document.forms[0].aplicacion.value = document.forms[0].codAplicacion.value;
				document.forms[0].organizacion.value = document.forms[0].codOrganizacion.value;
				document.forms[0].entidad.value = document.forms[0].codEntidad.value;								
				deshabilitarGeneral(vectorBotones);
    	    	document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesInternas.do";
        		document.forms[0].submit();	
			}
		} else {
		  jsp_alerta("A",'<%=descriptor.getDescripcion("msjAlgunNodoUsuar")%>');
		}
	}
	
	function comprobarPadreNodo(nodo) {
                    while(nodo.text != "GRUPOS-USUARIOS" && nodo.text != "MENÚ") {
		  var nodo2 = nodo.parentNode;
		  nodo = nodo2;
	   }
                    return ((nodo.text == "GRUPOS-USUARIOS") || (tabUsuarios.selectedIndex!=-1));
	}
	

    function actualizarEstilosUltimaBusqueda(){
    }
	
    </script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form  method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="codMenu" id="codMenu">
    <input  type="hidden"  name="nuevosPermisos" id="nuevosPermisos">
    <input  type="hidden"  name="codigoGU" id="codigoGU">
    <input  type="hidden"  name="aplicacion" id="aplicacion">
    <input  type="hidden"  name="organizacion" id="organizacion">
    <input  type="hidden"  name="entidad" id="entidad">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_usarMenus")%></div>
    <div class="contenidoPantalla">						
        <table width="100%">
            <tr>
                <td class="etiqueta" style="width:10%;"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
                <td class="etiqueta" style="width:53%;">
                    <input type="text" name="codOrganizacion" id="codOrganizacion" size="5" class="inputTextoObligatorio" value="" readonly="true">
                    <input type="text" name="descOrganizacion"  id="descOrganizacion" size="65" class="inputTextoObligatorio" style="width:380px" readonly="true" value="">
                </td> 
            </tr>			
            <tr><td colspan="2" style="width:100%;height:5px;"></td></tr>
            <tr>
                <td class="etiqueta" style="width:10%;"><%=descriptor.getDescripcion("gEtiq_Entidad")%>:</td>
                <td class="etiqueta" style="width:53%;">
                    <input type="text" name="codEntidad" id="codEntidad" size="5" class="inputTextoObligatorio" value="" readonly="true">
                    <input type="text" name="descEntidad"  id="descEntidad" size="65" class="inputTextoObligatorio" style="width:380px" readonly="true" value="">
                </td>
            </tr>	
            <tr><td colspan="2" style="width:100%;height:5px;"></td></tr>
            <tr>
                <td class="etiqueta" style="width:10%;"><%=descriptor.getDescripcion("gEtiq_Aplicacion")%>:</td>
                <td class="etiqueta" style="width:53%;">
                    <input type="text" name="codAplicacion" id="codAplicacion" size="5" class="inputTextoObligatorio" value="">
                    <input type="text" name="descAplicacion"  id="descAplicacion" size="65" class="inputTextoObligatorio" style="width:380px" readonly="true" value="">
                    <A href="" id="anchorAplicacion" name="anchorAplicacion"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                             name="botonAplicacion" style="cursor:hand;"></span>
                    </A>
                </td>
                <td align="right" style="width:30%;">
                    <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                           value="<%=descriptor.getDescripcion("gbBuscar")%>"
                           onClick="pulsarBuscar();" accesskey="B">
                    <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                           value='<%=descriptor.getDescripcion("gbCancelar")%>'
                           onClick="pulsarCancelarBuscar();" accesskey="C">
                </td>
            </tr>																
        </table>
        <table width="100%">
            <tr>
                <td style="width: 35%"><!-- Arbol -->
                    <table width="100%" cellspacing="0" cellpadding="4" class="subsubtitulo">
                        <tr>
                            <td>
                                            <div class="webfx-tree-div" style="height:100px;width:100%;overflow:auto;">
                                                <script type="text/javascript">                                  
                                        treeUsuarios = new WebFXTree('GRUPOS-USUARIOS');
                                                    treeUsuarios.icon=icoUsuario;
                                                    treeUsuarios.openIcon=icoUsuario;
                                                    treeUsuarios.estilo='webfx-tree-item-area';
                                                    treeUsuarios.action='javascript:{actualizarPermisos();}';
                                                    document.write(treeUsuarios);														
                                                </script>
                                            </div>				
                                    </td>
                                </tr>
                                </table>
                    <table width="100%" cellspacing="0" cellpadding="4" class="subsubtitulo">
                        <tr>
                            <td>
                                <div class="webfx-tree-div" style="height:292px;width:100%;overflow:auto;">
                                    <table width="100%" rules="cols" border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td id="tablaUsuarios">
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </table>								
                            </td>
                            <td style="width:65%;"> <!-- Arbol Menu  -->
                                <table width="100%" cellspacing="0" cellpadding="4" class="subsubtitulo">
                                    <tr><td>														
                                            <div class="webfx-tree-div" style="height:402px;width:100%;overflow:auto;">
                                                <script type="text/javascript">                                  
                                                    treeMenu = new WebFXTree('MENÚ');
                                                    treeMenu.icon=icoMenu;
                                                    treeMenu.openIcon=icoMenu;
                                                    treeMenu.estilo='webfx-tree-item-area';
                                                    document.write(treeMenu);														
                                                </script>
                                            </div>				
                                    </td></tr>
                                </table>								

                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal"> 
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
                                       name="botonModificar" onClick="grabarPermisos();" accesskey="M"> 
             <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                                       name="botonSalir" onClick="pulsarSalir();" accesskey="S">  
        </div>
    </div>
</form>
<script type="text/javascript">  
            var comboAplicacion = new Combo("Aplicacion");
            //var comboEntidad = new Combo("Entidad");
            //var comboOrganizacion = new Combo("Organizacion");
            
            /*comboOrganizacion.change = function() { 
            auxCombo='comboEntidad'; 
            comboEntidad.selectItem(-1);			
            if(comboOrganizacion.des.value.length != 0) {
            listaEntidades(comboAplicacion.cod.value,comboOrganizacion.cod.value);			
            }else{
            comboEntidad.addItems([],[]);
            }	
            }*/
            
            // JAVASCRIPT DE LA TABLA USUARIOS
            
            var tabUsuarios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUsuarios'));
            tabUsuarios.addColumna('0','left','cod'); //Le pongo dos columnas porque si no falla el constructor
            tabUsuarios.addColumna('305','left','<%= descriptor.getDescripcion("gEtiq_Nombre")%>');
            tabUsuarios.displayCabecera=false;
            
            document.onmouseup = checkKeys; 
        function refresca(tabla){
            tabla.displayTabla();
        }

        <%String Agent = request.getHeader("user-agent");%>

        var coordx=0;
        var coordy=0;


        <%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
            window.addEventListener('mousemove', function(e) {
                coordx = e.clientX;
                coordy = e.clientY;
            }, true);
        <%}%>

            function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	if (teclaAuxiliar == 1){
            if (comboAplicacion.base.style.visibility == "visible" && isClickOutCombo(comboAplicacion,coordx,coordy)) setTimeout('comboAplicacion.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            comboAplicacion.ocultar();
        }

   keyDel(evento);
  }
</script>
</body>
</html>
