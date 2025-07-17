<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@ page import="es.altia.arboles.impl.ArbolImpl"%>
<%@ page import="java.util.Vector"%>

<html>
    <head>
        <!-- mantenimientoUORs.jsp -->
<jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Cargos</title>
        
        <!-- Estilos -->
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
            }
        %>
        <!-- Aplicación <%= apl%>-->
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/WebFXImgRadioButtonTreeItem.js"></script>
        
        
        <script type="text/javascript">
         
            // VARIABLES GLOBALES
            //=====================================
            var cod_del_seleccionado = '';
            var fecha_hoy = fechaHoy();
            var uors = new Array();
            var uorcods = new Array();
            var icoUsuario = 'fa fa-folder-open'; // de alta
            var icoUsuarioBaW ='fa fa-folder-open-o';
            var icoUsuarioNV = 'fa fa-folder-open';// no visible en registro y de alta
            var icoUsuarioNVBaW = 'fa fa-folder-open-o';// no visible en registro y de baja
            var treeUORs = new WebFXTree('Cargos');
            treeUORs.icon=icoUsuario;
            treeUORs.openIcon=icoUsuario;
            treeUORs.estilo='webfx-tree-item-area';
            treeUORs.action='javascript:{hacerAlgo();}';
            
            
            // rellenar Array javascript con objetos UORs y otro para indexar con los uor_cod
            <%
            MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");

            Vector listaUORDTOs = bForm.getListaNuevasUORs();
            ArbolImpl arbol = bForm.getArbol();
            for (int j = 0; j < listaUORDTOs.size(); j++) {
                UORDTO dto = (UORDTO) listaUORDTOs.get(j);
%>
    // array con los objetos tipo uor mapeados por el array de arriba
    uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
        // array con los códigos visibles
        uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
            <%
            }
%>
    <%= arbol.toJavascript("treeUORs", "icoUsuario", "icoUsuarioBaW", "icoUsuarioNV", "icoUsuarioNVBaW",
                        "hacerAlgo()", "webfx-tree-item-area")%>
                        
                        // las nuevas
                        // =================================================================
                        // Llamada cdo seleccionas un nodo del arbol
                        function hacerAlgo() {
                            // si se ha pinchado en la raíz, no hacer nada
                            if(treeUORs.getSelected() == treeUORs) {
                                desactivarBotones("MEL");
                                pulsarLimpiar();
                                document.forms[0].estado[0].checked = true;
                                return;
                            }
                            
                            pulsarLimpiar();
                            
                            // sabemos si está de alta o baja por el icono
                            var estado;
                            if((treeUORs.getSelected().icon == icoUsuarioNV)||(treeUORs.getSelected().icon == icoUsuario)) {
                                estado = 'A';
                            }
                            else {
                                estado = 'B';
                            }
                            // obtener el nodo del array de uors
                            var uor_en_array = buscarUorPorCodVisibleEstadoDesc(
                                uors, nodo2CodVis(treeUORs.getSelected().text), estado,
                                nodo2Descripcion(treeUORs.getSelected().text));
                            
                            if(uor_en_array != null) {
                                activarBotones("MEL");
                                
                                cod_del_seleccionado = uor_en_array.uor_cod;
                                
                                // rellenar los elementos del form
                                document.campos.codigo_visible.value = uor_en_array.uor_cod_vis;
                                document.campos.nombre.value = uor_en_array.uor_nom;
                                
                                // código visible de la Unidad padre si la hay
                                if(uor_en_array.uor_pad != '') {
                                    var uor_padre = buscarUorPorCod(uor_en_array.uor_pad);
                                    document.campos.unidad_sup.value = uor_padre.uor_cod_vis;
                                }
                                // estado
                                if(uor_en_array.uor_estado == 'A') {
                                    document.campos.estado[0].checked = true;
                                }
                                else {
                                    document.campos.estado[1].checked = true;
                                }
                                // fechas
                                document.campos.fecha_alta.value = uor_en_array.uor_fecha_alta;
                                document.campos.fecha_baja.value = uor_en_array.uor_fecha_baja;
                                
                                activarBotones("AMELS");
                            }
                            else {
                                alert('Error de datos de Cargos');
                            }
                        }
                        
                        /*
                        Devuelve true si hay alguna UOR de alta descendiente del nodo seleccionado
                        */
                        function buscaDescenAlta() {
                            var tiene = false;
                            
                            // si el nodo no tiene hijos
                            if(treeUORs.getSelected().childNodes.length == 0) {
                                // en principio no usamos el estado del seleccionado
                            }
                            else {
                                tiene = true;
                                /*for(var k=0; k<treeUORs.getSelected().childNodes.length; k++) {
                                    var bool_temp = tieneDescenAlta(treeUORs.getSelected().childNodes[k]);
                                    //alert('iter: ' + k + ', valor: ' + bool_temp);
                                    tiene = tiene || bool_temp;
                                }*/
                            }
                            return tiene;
                        }
                        
                        /*
                        Recurre un nodo y todos sus descendientes buscando un nodo de alta, en cuyo caso devuelve true
                        */
                        function tieneDescenAlta(nodo) {
                            var tiene_desc_alta = false;
                            
                            // si no hay más descendientes
                            if(nodo.childNodes.length == 0) {
                                // comprobar si el hijo esta de alta
                                tiene_desc_alta = tiene_desc_alta || estaDeAlta(nodo);
                            }
                            // si hay más descendientes
                            else {
                                // visitar padre
                                tiene_desc_alta = tiene_desc_alta || estaDeAlta(nodo);
                                // visitar hijos
                                for(var i=0; i<nodo.childNodes.length; i++) {
                                    var bool_temp = tieneDescenAlta(nodo.childNodes[i]);
                                    tiene_desc_alta = tiene_desc_alta || bool_temp;
                                }
                            }
                            
                            return tiene_desc_alta;
                        }
                        
                        /*
                        Devuelve true si el nodo argumento esta de alta
                        */
                        function estaDeAlta(nodo) {
                            if(nodo.icon == icoUsuario) {
                                return true;
                            }
                            else
                                return false;
                        }
                        
                        
                        // =================================================================
                        // FUNCIONES MANEJO DE BOTONES
                        /* Función desactivarBotones(String)
                        El String contiene un conjunto de letras de entre las asignadas a los botones
                        */
                        function desactivarBotones(cuales) {
                            for(var i=0; i<cuales.length; i++) {
                                // alta
                                if(cuales.charAt(i) == 'A') {
                                    document.getElementById("cmdAlta").disabled=true;
                                    document.getElementById("cmdAlta").className = "botonGeneralDeshabilitado";
                                }
                                // limpiar campos
                                else if(cuales.charAt(i) == 'L') {
                                    document.getElementById("cmdLimpiar").disabled=true;
                                    document.getElementById("cmdLimpiar").className = "botonGeneralDeshabilitado";
                                }
                                // eliminar
                                else if(cuales.charAt(i) == 'E') {
                                    document.getElementById("cmdEliminar").disabled=true;
                                    document.getElementById("cmdEliminar").className = "botonGeneralDeshabilitado";
                                }
                                // modificar
                                else if(cuales.charAt(i) == 'M') {
                                    document.getElementById("cmdModificar").disabled=true;
                                    document.getElementById("cmdModificar").className = "botonGeneralDeshabilitado";
                                }
                                // salir
                                else if(cuales.charAt(i) == 'S') {
                                    document.getElementById("cmdSalir").disabled=true;
                                    document.getElementById("cmdSalir").className = "botonGeneralDeshabilitado";
                                }
                            }
                        }
                        
                        /* Función activarBotones(String)
                        El String contiene un conjunto de letras de entre las asignadas a los botones
                        */
                        function activarBotones(cuales) {
                            for(var i=0; i<cuales.length; i++) {
                                // alta
                                if(cuales.charAt(i) == 'A') {
                                    document.getElementById("cmdAlta").disabled=false;
                                    document.getElementById("cmdAlta").className = "botonGeneral";
                                }
                                // limpiar campos
                                else if(cuales.charAt(i) == 'L') {
                                    document.getElementById("cmdLimpiar").disabled=false;
                                    document.getElementById("cmdLimpiar").className = "botonGeneral";
                                }
                                // eliminar
                                else if(cuales.charAt(i) == 'E') {
                                    document.getElementById("cmdEliminar").disabled=false;
                                    document.getElementById("cmdEliminar").className = "botonGeneral";
                                }
                                // modificar
                                else if(cuales.charAt(i) == 'M') {
                                    document.getElementById("cmdModificar").disabled=false;
                                    document.getElementById("cmdModificar").className = "botonGeneral";
                                }
                                // salir
                                else if(cuales.charAt(i) == 'S') {
                                    document.getElementById("cmdSalir").disabled=false;
                                    document.getElementById("cmdSalir").className = "botonGeneral";
                                }
                            }
                        }
                        
                        
                        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
                        function inicializar() {
                            treeUORs.action='javascript:{hacerAlgo();}';
                            window.focus();
                            desactivarBotones("MEL");
                        }
                        
                        
                        function montarSubarbol(vieja, nueva) {
                            if(vieja.childNodes.length > 0) {
                                for(var i=0; i<vieja.childNodes.length; i++) {
                                    var nodo = new WebFXTreeItem(vieja.childNodes[i].text,'javascript:{hacerAlgo();}',
                                        nueva, icoUsuario,icoUsuario,'webfx-tree-item-area');
                                    montarSubarbol(vieja.childNodes[i], nodo);
                                }
                            }
                            else {
                                /*var nodo = new WebFXTreeItem(vieja.text,'javascript:{hacerAlgo();}',
                                nueva, icoUsuario,icoUsuario,'webfx-tree-item-area');*/
                            }
                        }
                        
                        // FUNCIONES DE PULSACION DE BOTONES
                        function pulsarSalir(){
                            document.forms[0].target = "mainFrame";
                            document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                            document.forms[0].submit();
                        }
                        
                        function pulsarEliminar() {
                            if(treeUORs.getSelected() == null) {
                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                                }
                                else {
                                    // msjTieneDescAlta
                                    if(buscaDescenAlta() == true) {
                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjCargoTieneDescAlta")%>');                                        
                                            pulsarLimpiar();
                                            return;
                                        }
                                    
                                    // sabemos si está de alta o baja por el icono
                                    var estado;
                                    if((treeUORs.getSelected().icon == icoUsuarioNV)||(treeUORs.getSelected().icon == icoUsuario)) {
                                        estado = 'A';
                                    }
                                    else {
                                        estado = 'B';
                                    }
                                    var nodo = buscarUorPorCodVisibleEstadoDesc(
                                        uors, nodo2CodVis(treeUORs.getSelected().text), estado,
                                        nodo2Descripcion(treeUORs.getSelected().text));
                                    
                                    cod_visible_seleccionado = nodo2CodVis(treeUORs.getSelected().text);
                                    if(nodo == null || cod_visible_seleccionado == null) {
                                        return;
                                    }
                                    if(jsp_alerta("C",'<%=descriptor.getDescripcion("desEliminarCargo")%>') ==1) {
                                        document.forms[0].codUOR.value = nodo.uor_cod;
                                        document.forms[0].opcion.value = "eliminar";
                                        document.forms[0].target = "oculto";
                                        
                                        var padre = treeUORs.getSelected().parentNode;
                                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/cargos.do';
                                        document.forms[0].submit();
                                    }
                                }
                                pulsarLimpiar();
                            }
                            
                            // actúa según el resultado de borrar en el action
                            // resultado puede ser: realizada, hayRegistros y noEliminada
                            function postPulsarEliminar(resultado) {
                                if(resultado == 'realizada') {
                                    treeUORs.getSelected().remove();
                                    
                                    document.forms[0].opcion.value = 'cargar';
                                    document.forms[0].target = "mainFrame";
                                    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/cargos.do';
                                    document.forms[0].submit();
                                }
                                else if(resultado == 'hayRegistros') {
                                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjCargoTieneReg")%>');
                                    }
                                    else if(resultado == 'noEliminada') {
                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjCargoNoEliminada")%>');
                                        }
                                        else if(resultado == 'formularios') {
                                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjCargoForm")%>');
                                            }
                                        }
                                        
                                        function pulsarLimpiar() {
                                        desactivarBotones("MEL");
                                            // codigo
                                            document.campos.codUOR.value = '';
                                            // codigo visible
                                            document.campos.codigo_visible.value = '';
                                            // nombre o descripcion de la UOR
                                            document.campos.nombre.value = '';
                                            // codigo visible de la unidad padre
                                            document.campos.unidad_sup.value = '';
                                            // estado
                                            document.campos.estado[0].checked = false;
                                            document.campos.estado[1].checked = false;
                                            // fechas
                                            document.campos.fecha_alta.value = '';
                                            document.campos.fecha_baja.value = '';
                                        }
                                        
                                        function postPulsarModificar(resultado) {
                                            if(resultado == 'realizada') {
                                                treeUORs.getSelected().remove();
                                                
                                                document.forms[0].opcion.value = 'cargar';
                                                document.forms[0].target = "mainFrame";
                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/cargos.do';
                                                document.forms[0].submit();
                                            }
                                            else if(resultado == 'formularios') {
                                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjCargoFormMod")%>');
                                                    pulsarLimpiar();
                                                }
                                                
                                            }
                                            
                                            
                                            
                                            function pulsarModificar() {
                                                document.forms[0].opcion.value = 'modificar';
                                                document.forms[0].target = "mainFrame";
                                                
                                                // obtener datos del UOR del arbol
                                                // sabemos si el seleccionado del arbol está de alta o baja por el icono
                                                var estado_selected;
                                                if((treeUORs.getSelected().icon == icoUsuarioNV)||(treeUORs.getSelected().icon == icoUsuario)) {
                                                    estado_selected = 'A';
                                                }
                                                else {
                                                    estado_selected = 'B';
                                                }
                                                var cod_visible_seleccionado = nodo2CodVis(treeUORs.getSelected().text);
                                                var uor_en_array = buscarUorPorCodVisibleEstadoDesc(
                                                    uors, cod_visible_seleccionado, estado_selected,
                                                    nodo2Descripcion(treeUORs.getSelected().text));
                                                if(uor_en_array == null || cod_visible_seleccionado == null) {
                                                    jsp_alert('A', '<%=descriptor.getDescripcion("msjErrorInterno")%>');
                                                        return
                                                    }
                                                    
                                                    // obtener información campos
                                                    var input_uor_nom = document.forms[0].nombre.value.trim();
                                                    var input_uor_estado;
                                                    if(document.forms[0].estado[0].checked == true) {
                                                        input_uor_estado = 'A';
                                                    }
                                                    else {
                                                        input_uor_estado = 'B';
                                                    }
                                                    
                                                    var input_uor_cod_vis = document.forms[0].codigo_visible.value.trim();
                                                    var input_uor_unidad_sup = document.campos.unidad_sup.value;
                                                    
                                                    
                                                    ////////////////////
                                                    // COMPROBACIONES///
                                                    ////////////////////
                                                    // si no hay nada seleccionado
                                                    if(treeUORs.getSelected() == null) {
                                                        jsp_alert('A', '<%=descriptor.getDescripcion("msjProbTecnico")%>');
                                                            return;
                                                        }
                                                        
                                                        
                                                        // procesar campos obligatorios
                                                        // nombre
                                                        if(input_uor_nom == '') {
                                                            jsp_alerta("A",'<%=descriptor.getDescripcion("faltaDescripCargo")%>');
                                                                return;
                                                            }
                                                            // codigo visible
                                                            if(input_uor_cod_vis == '') {
                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("faltaCodVisCargo")%>');
                                                                    return;
                                                                }
                                                                
                                                                // obtener codigo visible anterior del padre, si existe
                                                                var cod_vis_pad_anterior = '';
                                                                var uor_pad_anterior = buscarUorPorCod(uor_en_array.uor_pad);
                                                                if(uor_pad_anterior != null) {
                                                                    cod_vis_pad_anterior = uor_pad_anterior.uor_cod_vis;
                                                                }
                                                                
                                                                // no puede haber 2 semántica/ idénticas
                                                                if((input_uor_nom == uor_en_array.uor_nom)&&
                                                                    (input_uor_estado == uor_en_array.uor_estado)&&
                                                                    (input_uor_cod_vis == uor_en_array.uor_cod_vis)&&
                                                                    (input_uor_unidad_sup == cod_vis_pad_anterior)
                                                                ) {
                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoModifCargo")%>');
                                                                    pulsarLimpiar();
                                                                    return;
                                                                }
                                                                
                                                                // si el estado cambia de baja a alta, modificar fecha de alta y eliminar la de baja
                                                                if((input_uor_estado == 'A')&&(uor_en_array.uor_estado == "B")) {
                                                                    document.forms[0].fecha_alta.value = fecha_hoy;
                                                                    document.forms[0].fecha_baja.value = '';
                                                                    uor_en_array.uor_fecha_alta = fecha_hoy;
                                                                    uor_en_array.uor_fecha_baja = null;

                                                                }
                                                                // si al revés, cambia de alta a baja
                                                                else if((input_uor_estado == 'B')&&(uor_en_array.uor_estado == "A")) {

                                                                        if(buscaDescenAlta() == true) {
                                                                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjCargoTieneDescAlta")%>');
                                                                                pulsarLimpiar();
                                                                                return;
                                                                        }

                                                                    document.forms[0].fecha_baja.value = fecha_hoy;
                                                                    uor_en_array.uor_fecha_baja = fecha_hoy;
                                                                }
                                                                
                                                                
                                                                var uor_pad_cambiado = false;
                                                                // si hay algo en el campo de Unidad superior distinto a lo que había
                                                                if(input_uor_unidad_sup != cod_vis_pad_anterior) {
                                                                    uor_pad_cambiado = true;
                                                                    // comprobar que no se intenta poner como padre a uno mismo
                                                                    if(input_uor_unidad_sup == uor_en_array.uor_cod_vis) {
                                                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorCargoSuperior")%>');
                                                                            pulsarLimpiar();
                                                                            return;
                                                                        }
                                                                        
                                                                        // comprobar q la nueva existe (si es q no es vacía)
                                                                        // 27/04/2006 sólo permitir cambio a UOR padre de alta
                                                                        if(input_uor_unidad_sup != '' && buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'A') == null) {
                                                                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorCargoSuperior")%>'  + '<%=descriptor.getDescripcion("msjMotivo")%>');
                                                                                pulsarLimpiar();
                                                                                return;
                                                                            }
                                                                        }
                                                                        
                                                                        // ver si ha cambiado alguno de los campos: nombre, email, tipo
                                                                        var info_no_cambia = (input_uor_nom == uor_en_array.uor_nom);
                                                                        
                                                                        
                                                                        // no puede haber 2 con el mismo estado + cod_vis + descripcion a menos q sea pq estamos cambiando
                                                                        //la unidad superior o modificando otro campo
                                                                        if((uor_pad_cambiado == false)&&(buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, input_uor_estado, input_uor_nom) != null)&&
                                                                            (info_no_cambia == true)) {
                                                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjExCargo")%>');
                                                                            pulsarLimpiar();
                                                                            return;
                                                                        }
                                                                        
                                                                        //////////////////////////////////////////
                                                                        // continuar modificación normalmente   //
                                                                        //////////////////////////////////////////
                                                                        if(jsp_alerta("C",'<%=descriptor.getDescripcion("desModificarCargo")%>', "") ==1) {
                                                                            // rellenar los input
                                                                            document.forms[0].codUOR.value = uor_en_array.uor_cod;
                                                                            
                                                                            // uor padre
                                                                            if(uor_pad_cambiado == true) {
                                                                                // si han borrado el campo = cambiar a hijo de la raiz
                                                                                if(input_uor_unidad_sup == '') {
                                                                                    
                                                                                }
                                                                                else {
                                                                                    // buscar cod del padre segun el cod visible del campo y descripcion, preferencia uno de alta
                                                                                    var uor_pad = buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'A');
                                                                                    if(uor_pad == null) { // si es null, debe estar de baja
                                                                                        uor_pad = buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'B');
                                                                                    }
                                                                                    if(uor_pad == null) { // si tampoco hay de baja, hay un error grave
                                                                                        jsp_alert('A', '<%=descriptor.getDescripcion("msjErrorDatosCargos")%>');
                                                                                        }
                                                                                        document.forms[0].codPad.value = uor_pad.uor_cod;
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    document.forms[0].codPad.value = uor_en_array.uor_pad;
                                                                                }
                                                                                
                                                                                document.forms[0].opcion.value = "modificar";
                                                                                document.forms[0].target = "oculto";
                                                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/cargos.do';
                                                                                document.forms[0].submit();
                                                                                desactivarBotones("AMEL");
                                                                            }
                                                                            
                                                                        }
                                                                        
                                                                        function pulsarAlta() {
                                                                            /////////////////////////////
                                                                            // obtener información campos
                                                                            /////////////////////////////
                                                                            var input_uor_nom = document.forms[0].nombre.value.trim();
                                                                            var input_uor_estado;
                                                                            if(document.forms[0].estado[0].checked == true) {
                                                                                input_uor_estado = 'A';
                                                                            }
                                                                            else if(document.forms[0].estado[1].checked == true) {
                                                                                input_uor_estado = 'B';
                                                                            }
                                                                            
                                                                            var input_uor_cod_vis = document.forms[0].codigo_visible.value.trim();
                                                                            
                                                                            // ver si hay un UOR en el array de UORs con el mismo estado ,cod_vis y descripcion
                                                                            var uor_en_array = buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, input_uor_estado, input_uor_nom);
                                                                            
                                                                            ////////////////////
                                                                            // COMPROBACIONES
                                                                            ////////////////////
                                                                            // procesar campos obligatorios
                                                                            // nombre
                                                                            if(input_uor_nom == '') {
                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("faltaDescripCargo")%>');
                                                                                    return;
                                                                                }
                                                                                // codigo visible
                                                                                if(input_uor_cod_vis == '') {
                                                                                    jsp_alerta("A",'<%=descriptor.getDescripcion("faltaCodVisCargo")%>');
                                                                                        return;
                                                                                    }
                                                                                    
                                                                                    // 01/05/2006: ahora no se puede dar de alta una UOR cuyo nombre + cod visible exista
                                                                                    if(buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, 'A', input_uor_nom) != null ||
                                                                                        buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, 'B', input_uor_nom) != null) {
                                                                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjExCargo")%>');
                                                                                        pulsarLimpiar();
                                                                                        return;
                                                                                    }
                                                                                    
                                                                                    // no puede haber 2 semántica/ idénticas: mismo estado + cod_vis + descripcion, ya sea de alta o de baja
                                                                                    if(uor_en_array != null) {
                                                                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjExCargo")%>');
                                                                                            pulsarLimpiar();
                                                                                            return;
                                                                                        }
                                                                                        
                                                                                        // si esta de alta, no puede haber otro con el mismo cod visible y tambien de alta
                                                                                        if((input_uor_estado == 'A')&&(buscarUorPorCodVisibleEstado(uors, input_uor_cod_vis, 'A') != null)) {
                                                                                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjExCargo")%>');
                                                                                                pulsarLimpiar();
                                                                                                return;
                                                                                            }
                                                                                            
                                                                                            
                                                                                            // procesar estado, y fechas de alta y baja
                                                                                            if(input_uor_estado == 'A') {
                                                                                                document.forms[0].estado.value = "A";
                                                                                                document.forms[0].fecha_alta.value = fecha_hoy;
                                                                                                document.forms[0].fecha_baja.value = '';
                                                                                            }
                                                                                            else if(input_uor_estado == 'B') {
                                                                                                document.forms[0].estado.value = "B";
                                                                                                document.forms[0].fecha_alta.value = '';
                                                                                                document.forms[0].fecha_baja.value = fecha_hoy;
                                                                                            }
                                                                                            else { // no hay casilla de estado marcada
                                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("faltaEstadoCargo")%>');
                                                                                                    return;
                                                                                                }
                                                                                                
                                                                                                // inputs ocultos
                                                                                                // cod del nodo padre
                                                                                                if (document.campos.unidad_sup.value==''){
                                                                                                    if(treeUORs.getSelected() == treeUORs) { // si el padre es la raiz
                                                                                                    document.forms[0].codPad.value = '';
                                                                                                    }
                                                                                                else document.forms[0].codPad.value = cod_del_seleccionado;
                                                                                                }
                                                                                                else {
                                                                                                     var uor_pad = buscarUorPorCodVisibleEstado(uors, document.campos.unidad_sup.value, 'A');
                                                                                                    document.forms[0].codPad.value = uor_pad.uor_cod;
                                                                                                }

                                                                                                
                                                                                                document.forms[0].codUOR.value = ''; // no es necesario; se obtiene de la BD
                                                                                                document.forms[0].opcion.value = 'alta';
                                                                                                document.forms[0].target.value = "mainFrame";
                                                                                                
                                                                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/cargos.do';
                                                                                                document.forms[0].submit();
                                                                                                pulsarLimpiar();
                                                                                            }
                                                                                            

                      function actualizarEstilosUltimaBusqueda(){
                          
                      }
                                                                                            
        </script>
    </head>
<body class="bandaBody" onload="javascript:{inicializar();}">
<form name="campos" id="campos" action="">
    <input  type="hidden"  name="opcion" id="opcion">
    <!-- información sobre el UOR seleccionado que no se muestra en pantalla -->
    <input  type="hidden"  name="codUOR" id="codUOR">
    <input  type="hidden"  name="codPad" id="codPad">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantCargos")%></div>
    <div class="contenidoPantalla">
        <table >
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <td align="justify"  valign="top">

                    <div class="webfx-tree-div" style="height:350px;width:100%;overflow:auto">
                        <script type="text/javascript">
                            treeUORs.icon=icoUsuario;
                            treeUORs.openIcon=icoUsuario;
                            treeUORs.estilo='webfx-tree-item-area';
                            treeUORs.action='javascript:{hacerAlgo();}';
                            document.write(treeUORs);
                            treeUORs.expandAll();
                            treeUORs.select();
                        </script>
                    </div>
                </td>
            </tr>
        </table>
        <table >
            <tr height="28">
                <td class="etiqueta" style="width: 70px">
                    <%=descriptor.getDescripcion("etiqCodigoCargo")%>:
                </td>
                <td style="width: 100px">
                    <input type="text" name="codigo_visible" id="codigo_visible" size="6" maxlength="6" class="inputTextoObligatorio" onkeyup="return SoloAlfanumericos(this);"/>
                </td>
                <td style="width:10px;">&nbsp;</td>
                <td class="etiqueta" style="width:120px;">
                    <%=descriptor.getDescripcion("etiqDesCargo")%>:
                </td>
                <td colspan="3">
                    <input type="text" name="nombre" id="nombre" size="95" maxlength="80" style="width:550px" class="inputTextoObligatorio" onkeyup="return xAMayusculas(this);"/>
                </td>
            </tr>
            <tr>
                <td style="width:70px;"  class="etiqueta">
                    <%=descriptor.getDescripcion("etiqEstadoCargo")%>:
                </td>
                <td class="etiqueta" align="left" style="width: 100px">
                    <input type="radio" name="estado" id="estado_alta" value="A" checked="true"/><%=descriptor.getDescripcion("etiqEstAltaCargo")%>
                    <input type="radio" name="estado" id="estado_baja" value="B"><%=descriptor.getDescripcion("etiqEstBajaCargo")%>
                </td>
                <td style="width:10px;">&nbsp;</td>
                <td class="etiqueta" style="width:120px;" >
                    <%=descriptor.getDescripcion("etiqCargoSup")%>:
                </td>
                <td>
                    <input type="text" name="unidad_sup" id="unidad_sup" size="6" maxlength="6" class="inputTexto" onkeyup="return SoloAlfanumericos(this);"/>
                </td>
            </tr>
            <tr>
                <td class="etiqueta" style="width: 70px">
                    <%=descriptor.getDescripcion("etiqFechaAltaCargo")%>:
                </td>
                <td align="left" style="width: 100px">
                    <input type="text" name="fecha_alta" id="fecha_alta" size="10" maxlength="10" value="" readonly="true" class="inputTxtFecha"/>
                </td>
                <td style="width:10px;">&nbsp;</td>
                <td class="etiqueta" style="width:120px;">
                    <%=descriptor.getDescripcion("etiqFechaBajaCargo")%>:
                </td>
                <td align="left">
                    <input type="text" name="fecha_baja" id="fecha_baja" size="10" maxlength="10" value="" readonly="true" class="inputTxtFecha"/>
                </td>
            </tr>
        </table>
        <div id="tablaBotones" class="botoneraPrincipal">
                <form action="" name="botones" id="botones">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%>
                                   name="cmdAlta" id="cmdAlta" onclick="pulsarAlta();" accesskey="A">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%>
                                   name="cmdModificar" id="cmdModificar" onclick="pulsarModificar();" accesskey="M">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%>
                                   name="cmdEliminar" id="cmdEliminar" onclick="pulsarEliminar();" accesskey="E">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%>
                                   name="cmdLimpiar" id="cmdLimpiar" onclick="pulsarLimpiar();" accesskey="L">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%>
                                   name="cmdSalir" id="cmdSalir" onclick="pulsarSalir();" accesskey="S">
        </div>      
    </div>      
</form>
</body>
</html>
