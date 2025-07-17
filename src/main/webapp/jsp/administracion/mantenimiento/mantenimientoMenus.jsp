<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Menus por Organizacion </title>
       
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                    css=usuarioVO.getCss();
            }

            MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        
        %>
        
        
         <!-- Estilos -->
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value='${sessionScope.usuario.css}'/>"/>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
        <script type="text/javascript">
            // VARIABLES GLOBALES
            var cod_Organizaciones = new Array();
            var desc_Organizaciones = new Array();
            var cod_Aplicaciones = new Array();
            var desc_Aplicaciones = new Array();
            var datosProcesosOriginal = new Array();
            var datosProcesos = new Array();	
            
            var tree;
            var ptosMenu = new Array(); // Indexados por nodo del arbol
            var nodosPtosMenu = new Array(); // De nodos indexados por codigo
            var icoMenu = 'fa fa-folder-open';
            var icoPtoMenu = 'fa fa-cubes';
            
            var datosProcesos = new Array();
            var datosProcesosOriginal = new Array();
            var vectorBotones = new Array();
            var vectorCamposBusqueda = new Array();
            
            /* DATOS INICIALES */
            
         <%
            Vector listaOrganizaciones = bForm.getListaOrganizaciones();
            if (listaOrganizaciones == null) {
                listaOrganizaciones = new Vector();
            }
            Vector listaAplicaciones = bForm.getListaAplicaciones();
            if (listaAplicaciones == null) {
                listaAplicaciones = new Vector();
            }
            for (int i = 0; i < listaOrganizaciones.size(); i++) {
                GeneralValueObject org = (GeneralValueObject) listaOrganizaciones.get(i);%>
                    cod_Organizaciones[<%=i%>] = '<%=(String) org.getAtributo("codigo")%>';
                        desc_Organizaciones[<%=i%>] = '<%=(String) org.getAtributo("descripcion")%>';
    <%}
            for (int i = 0; i < listaAplicaciones.size(); i++) {
                GeneralValueObject aplicacion = (GeneralValueObject) listaAplicaciones.get(i);%>
                    cod_Aplicaciones[<%=i%>] = '<%=(String) aplicacion.getAtributo("codigo")%>';
                        desc_Aplicaciones[<%=i%>] = '<%=(String) aplicacion.getAtributo("descripcion")%>';
                            <%}%>
                            
                            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
                            function inicializar(){
                                window.focus();
                                vectorBotones = [document.forms[0].botonAnadir,document.forms[0].botonModificar,document.forms[0].botonEliminar];
                                vectorCamposBusqueda = [document.forms[0].codOrganizacion,document.forms[0].descOrganizacion
                                    ,document.forms[0].codAplicacion,document.forms[0].descAplicacion];
                                pulsarCancelarBuscar();
                                comboOrganizacion.addItems(cod_Organizaciones, desc_Organizaciones);
                                comboAplicacion.addItems(cod_Aplicaciones, desc_Aplicaciones);
                            }
                            
                            
                            function cargarTablaProcesos(listaOriginal, lista){
                                datosProcesosOriginal = listaOriginal;
                                datosProcesos = lista;
                                tablaProcesos.lineas = datosProcesos;
                                refresca(tablaProcesos);	  	  
                            }
                            
                            // FUNCIONES DE LIMPIEZA DE CAMPOS
                            function limpiarTablaProcesos(){
                                tablaProcesos.lineas = new Array();
                                refresca(tablaProcesos);	  
                            }
                            
                            // FUNCIONES DE PULSACION DE BOTONES
                            function pulsarBuscar(){
                                var botonBuscar = [document.forms[0].botonBuscar];
                                if(validarCamposBusqueda()){
                                    document.forms[0].opcion.value="cargarMenus";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/Menus.do";
                                    document.forms[0].submit();
                                    comboOrganizacion.deactivate();
                                    comboAplicacion.deactivate();
                                    //habilitarGeneral(vectorBotones);
                                    deshabilitarGeneral(botonBuscar);
                                }else jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                                }
                                
                                
                                function pulsarSalir(){
                                    document.forms[0].target = "mainFrame";
                                    document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                                    document.forms[0].submit();
                                }
                                
                                function pulsarEliminar() {
                                    
                                    if (tree.getSelected()) { 
                                        var nodo = tree.getSelected();
                                        if (nodo != tree) {				
                                            if (nodo.parentNode == tree) {
                                                comboOrganizacion.activate();
                                                comboAplicacion.activate();					
                                                document.forms[0].opcion.value = 'eliminarMenu';
                                                document.forms[0].target = "oculto";
                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Menus.do';
                                                document.forms[0].submit();       			  								  
                                            } else {
                                            if(jsp_alerta("C",'<%=descriptor.getDescripcion("desEliminarMen")%>') ==1) {
                                                comboOrganizacion.activate();
                                                comboAplicacion.activate();
                                                document.forms[0].codMenu.value =  ptosMenu[nodo.id][0];
                                                document.forms[0].codElemento.value =  ptosMenu[nodo.id][1];
                                                document.forms[0].opcion.value = 'eliminarPuntoMenu';
                                                document.forms[0].target = "oculto";
                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Menus.do';
                                                document.forms[0].submit();       			  
                                            }
                                        }
                                    } 
                                }
                            }
                            
                            function pulsarModificar(){
                                if (tree.getSelected()) { 
                                    var nodo = tree.getSelected();
                                    if (nodo != tree) {				
                                        if (nodo.parentNode == tree) {
                                            var source = "<%=request.getContextPath()%>/jsp/administracion/mantenimiento/nombreMenu.jsp?opcion=cargar";
                                            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,null,
                                                    'width=850,height=350,status='+ '<%=statusBar%>',function(datos){
                                                        if(datos!=undefined){
                                                            comboOrganizacion.activate();
                                                            comboAplicacion.activate();
                                                            document.forms[0].descMenu.value=datos[0];
                                                            document.forms[0].opcion.value = 'modificarMenu';
                                                            document.forms[0].target = "oculto";
                                                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Menus.do';
                                                            document.forms[0].submit();       			 				
                                                        }
                                                    });
                                        } 
                                    }
                                }
                            }	
                            
                            
                            function pulsarAlta(){
                                if (tree.getSelected()) { 
                                    nodoAlta = tree.getSelected();
                                    if (nodoAlta == tree) {								
                                        if (nodoAlta.childNodes.length < 1){
                                            var source = "<%=request.getContextPath()%>/jsp/administracion/mantenimiento/nombreMenu.jsp?opcion=cargar";
                                            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,null,
                                                    'width=850,height=350,status='+ '<%=statusBar%>',function(datos){
                                                            if(datos!=undefined){
                                                                comboOrganizacion.activate();
                                                                comboAplicacion.activate();						
                                                                document.forms[0].descMenu.value=datos[0];
                                                                document.forms[0].opcion.value = 'altaMenu';
                                                                document.forms[0].target = "oculto";
                                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Menus.do';
                                                                document.forms[0].submit();       			 				
                                                            }
                                                    });
                                            } else jsp_alerta("A",'<%=descriptor.getDescripcion("msjExisteMenu")%>');
                                            } else {
                                            var source = "<%=request.getContextPath()%>/jsp/administracion/mantenimiento/listaProcesos.jsp?opcion=cargar";
                                            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosProcesosOriginal,
                                                'width=998,height=530,status='+ '<%=statusBar%>',function(datos){
                                                        if(datos!=undefined){					
                                                            if(datos[0] == undefined) {
                                                                comboOrganizacion.activate();
                                                                comboAplicacion.activate();	
                                                                document.forms[0].opcion.value = 'altaTodosPuntosMenu';
                                                                document.forms[0].target = "oculto";
                                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Menus.do';
                                                                document.forms[0].submit();
                                                            } else {
                                                            comboOrganizacion.activate();
                                                            comboAplicacion.activate();							
                                                            document.forms[0].codProceso.value = datos[0];
                                                            if (ptosMenu[nodoAlta.id]==undefined) // No tendra padre.
                                                                document.forms[0].codPadre.value =  0;
                                                            else document.forms[0].codPadre.value =  ptosMenu[nodoAlta.id][1];		
                                                            document.forms[0].opcion.value = 'altaPuntoMenu';
                                                            document.forms[0].target = "oculto";
                                                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Menus.do';
                                                            document.forms[0].submit();
                                                        }
                                                    }
                                                });
                                        }
                                    }
                                }
                                
                                
                                function pulsarCancelarBuscar(){			
                                    limpiarTablaProcesos();
                                    borrarMenu();
                                    var botonBuscar = [document.forms[0].botonBuscar];
                                    habilitarGeneral(botonBuscar);			
                                    deshabilitarGeneral(vectorBotones);
                                    comboAplicacion.activate();
                                    comboOrganizacion.activate();
                                    comboAplicacion.selectItem(-1);			
                                    comboOrganizacion.selectItem(-1);
                                    
                                }
                                
                                // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
                                
                                function filaSeleccionada(tabla){
                                    var i = tabla.selectedIndex;
                                    if((i>=0)&&(!tabla.ultimoTable))
                                        return true;
                                    return false;
                                }
                                
                                function validarCamposBusqueda(){
                                    var codOrg = document.forms[0].codOrganizacion.value;
                                    var codApl = document.forms[0].codAplicacion.value;
                                    if(codOrg!="" && codApl!="")
                                        return true;
                                    return false;
                                }
                                
                                /*
                                function construirIdiomasDescripciones(){
                                var cadena ="";
                                for (i=0; i < datosDescripcionesOriginal.length; i++) {
                                cadena += datosDescripcionesOriginal[i][0]+'§¥' +datosDescripcionesOriginal[i][2]+'§¥';
                                }
                                return cadena;
                                }
                                
                                */
                                
                                function cargarMenu(codMenu,descMenu, datosMenu, dpO,dp){
                                    borrarMenu();
                                    if (codMenu != "") {
                                        var padre;
                                        var nodo;
                                        var nodoMenu;
                                        var nodoPadre = tree;		
                                        // Raiz.
                                        nodoMenu = new WebFXTreeItem(descMenu,'javascript:{actualizarProceso();}',nodoPadre,icoPtoMenu,icoPtoMenu,'webfx-tree-item-area');
                                        nodoPadre = nodoMenu;
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
                                            nodo = new WebFXTreeItem(desc,'javascript:{actualizarProceso();}',nodoPadre,icoPtoMenu,icoPtoMenu,'webfx-tree-item-area');
                                            ptosMenu[nodo.id]=datosMenu[i];
                                            nodosPtosMenu[codigo]=nodo;
                                        }			
                                        tree.expand();
                                        tree.childNodes[0].expand();
                                        datosProcesosOriginal = dpO;
                                        datosProcesos = dp;
                                    }
                                    tablaProcesos.lineas = datosProcesos;
                                    tablaProcesos.displayTabla();
                                    document.forms[0].codMenu.value=codMenu;
                                    comboOrganizacion.deactivate();
                                    comboAplicacion.deactivate();			
                                }
                                
                                function borrarMenu(){
                                    ptosMenu = new Array();
                                    nodosPtosMenu = new Array();		
                                    if (tree.childNodes.length == 1) {
                                        var nodo = tree.childNodes[0];
                                        nodo.remove();			
                                    }
                                    datosProcesosOriginal = new Array();
                                    datosProcesos = new Array();
                                    tablaProcesos.lineas = new Array();
                                    tablaProcesos.displayTabla();
                                    tree.select();
                                    actualizarProceso();
                                }
                                
                                function actualizarProceso(){
                                    if (tree.getSelected()) { 
                                        tablaProcesos.readOnly = false;
                                        var nodo = tree.getSelected();
                                        var idNodo = tree.getSelected().id;			
                                        if (nodo == tree) {
                                            tablaProcesos.selectLinea(tablaProcesos.selectedIndex);
                                            habilitarGeneral(vectorBotones);
                                            deshabilitarGeneral([document.forms[0].botonModificar,document.forms[0].botonEliminar]);
                                        } else if ( nodo.parentNode == tree) {		
                                        tablaProcesos.selectLinea(tablaProcesos.selectedIndex);
                                        habilitarGeneral(vectorBotones);
                                    } else {				
                                    var pos = posProceso(ptosMenu[nodo.id][3]);
                                    if (tablaProcesos.selectedIndex != pos)
                                        tablaProcesos.selectLinea(pos);
                                    habilitarGeneral(vectorBotones);
                                    deshabilitarGeneral([document.forms[0].botonModificar]);
                                }
                                tablaProcesos.readOnly = true;
                            }
                        }
                        
                        function posProceso(id){
                            for (var j=0; j<datosProcesosOriginal.length; j++)
                                if (datosProcesosOriginal[j][0]==id) return j;
                            return -1;
                        }
                        

                        function actualizarEstilosUltimaBusqueda(){
                        }
                        
        </script>
    </head>
    
    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form  method="post">
            <input  type="hidden"  name="opcion" id="opcion">
            <input  type="hidden"  name="codMenu" id="codMenu">
            <input  type="hidden"  name="descMenu" id="descMenu">
            <input  type="hidden"  name="codElemento" id="codElemento">
            <input  type="hidden"  name="codProceso" id="codProceso">
            <input  type="hidden"  name="codPadre" id="codPadre">
            
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantMenus")%></div>
            <div class="contenidoPantalla">
            <table style="width: 100%">
                <tr>
                    <td>							
                        <table width="100%" >
                            <tr style="padding-bottom:5px">
                                <td class="etiqueta" style="width: 15%"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
                                <td class="etiqueta" style="width: 55%">
                                    <input type="text" name="codOrganizacion" id="codOrganizacion" size="3" class="inputTextoObligatorio" value="">
                                    <input type="text" name="descOrganizacion"  id="descOrganizacion" size="43" class="inputTextoObligatorio" style="width:400px" readonly="true" value="">
                                    <A href="" id="anchorOrganizacion" name="anchorOrganizacion"> 
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOrganizacion"
                                             name="botonOrganizacion" style="cursor:hand;"></span>
                                    </A>
                                </td>
                                <td align="right" style="width: 30%"></td>
                            </tr>			

                            <tr>
                                <td class="etiqueta" style="width: 15%"><%=descriptor.getDescripcion("gEtiq_Aplicacion")%>:</td>
                                <td class="etiqueta" style="width: 55%">
                                    <input type="text" name="codAplicacion" id="codAplicacion" size="3" class="inputTextoObligatorio" value="">
                                    <input type="text" name="descAplicacion"  id="descAplicacion" size="43" class="inputTextoObligatorio" style="width:400px" readonly="true" value="">
                                    <A href="" id="anchorAplicacion" name="anchorAplicacion"> 
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                             name="botonAplicacion" style="cursor:hand;"></span>
                                    </A>
                                </td>
                                <td align="left" style="width: 30%">
                                    <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                           value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                           onClick="pulsarBuscar();" accesskey="B">
                                    <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                           value='<%=descriptor.getDescripcion("gbCancelar")%>'
                                           onClick="pulsarCancelarBuscar();" accesskey="C">
                                </td>
                            </tr>
                        </table>
                    </td>                                                        
                </tr>
                <tr>
                    <td>							
                        <table width="99%">
                            <tr>
                                <td style="width: 35%;vertical-align: top" class="subsubtitulo"><!-- Arbol -->
                                    <div class="webfx-tree-div" style="height:100%;width:100%;">
                                        <script type="text/javascript">                                  
                                            tree = new WebFXTree('MENÚ');
                                            tree.icon=icoMenu;
                                            tree.openIcon=icoMenu;
                                            tree.estilo='webfx-tree-item-area';
                                            tree.action ='javascript:{actualizarProceso();}';
                                            document.write(tree);														
                                        </script>
                                    </div>				
                                </td>
                                <td id="tabla" style="width: 65%;vertical-align: top" class="subsubtitulo"> <!-- Procesos -->
                                </td>
                            </tr>
                        </table>
                    </td>							      													                                                        
                </tr>							      													                                                        
            </TABLE>
            <div class="botoneraPrincipal">
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAnadir")%> 
                       name="botonAnadir" onClick="pulsarAlta();" accesskey="A"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> 
                       name="botonEliminar" onClick="pulsarEliminar();" accesskey="E"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
                                           name="botonModificar" onClick="pulsarModificar();" accesskey="M"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                                           name="botonSalir" onClick="pulsarSalir();" accesskey="S">  
            </div>                        		                        
        </div>                        		                        
    </form>

    <script type="text/javascript">  
            
            var tablaProcesos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tablaProcesos.addColumna('300',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
                tablaProcesos.addColumna('300',null,'<%= descriptor.getDescripcion("gEtiq_Formulario")%>');
                    tablaProcesos.displayCabecera = true;  
                    tablaProcesos.readOnly = true;
                    
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    tablaProcesos.colorLinea=function(rowID) {
                        if(datosProcesosOriginal[rowID][5]==1)
                            return 'gris';
                    }
                    
                    var tablaObject = tablaProcesos;
                    
                    var comboAplicacion = new Combo("Aplicacion");
                    var comboOrganizacion = new Combo("Organizacion");

                    <%String Agent = request.getHeader("user-agent");%>

                    var coordx=0;
                    var coordy=0;


                    <%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
                        window.addEventListener('mousemove', function(e) {
                            coordx = e.clientX;
                            coordy = e.clientY;
                        }, true);
                    <%}%>

                    document.onmouseup = checkKeys; 
                    
                    function checkKeysLocal(evento, tecla){

                        var teclaAuxiliar="";
                          if(window.event){
                            evento = window.event;
                            teclaAuxiliar =evento.keyCode;
                          }else
                                teclaAuxiliar =evento.which;

                        keyDel(evento);

                        if(teclaAuxiliar == 40 || teclaAuxiliar == 38){
                            if(tablaProcesos==tablaObject)
                                upDownTable(tablaProcesos,datosProcesos,teclaAuxiliar);
                        }    
                        if(teclaAuxiliar == 13){
                            if(tablaProcesos==tablaObject)
                                pushEnterTable(tablaProcesos,datosProcesos);
                        }
                        if(teclaAuxiliar == 1){
                            if (comboAplicacion.base.style.visibility == "visible" && isClickOutCombo(comboAplicacion,coordx,coordy)) setTimeout('comboAplicacion.ocultar()',20);
                            if (comboOrganizacion.base.style.visibility == "visible" && isClickOutCombo(comboOrganizacion,coordx,coordy)) setTimeout('comboOrganizacion.ocultar()',20);
                        }
                        if(teclaAuxiliar == 9){
                            if (comboAplicacion.base.style.visibility == "visible") comboAplicacion.ocultar();
                            if (comboOrganizacion.base.style.visibility == "visible") comboOrganizacion.ocultar();
                        }
                    }
                    
        </script>
        
        
    </body>
</html>

