<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@ page import="es.altia.arboles.impl.ArbolImpl"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>


<html>
    <head>
        <!-- mantenimientoUORs.jsp -->
<jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Unidades Orgánicas</title>
        
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
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            String accede = m_Conf.getString("aytos.registro");
            String opcion = request.getParameter("opcion");
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
        
        
        <!--<script type="text/javascript" src="C:\Programas\jsTrace-v1.3-drag\dom-drag.js"></script>-->
<!--<script type="text/javascript" src="C:\Programas\jsTrace-v1.3-drag\jsTrace.js"></script>-->

        
        <script type="text/javascript"> 
            
            /*function trace( msg ){
            if( typeof( jsTrace ) != 'undefined' )  {
            jsTrace.send( msg );
            }
            }*/
            
            
            // VARIABLES GLOBALES
            //=====================================
            var cod_del_seleccionado = '';
            var fecha_hoy = fechaHoy();
            var uors = new Array();
            var uorcods = new Array();
            var uorrexistroXeral = new Array();
            var icoUsuario = 'fa fa-folder-open'; // de alta
            var icoUsuarioBaW ='fa fa-folder-open-o';
            var icoUsuarioNV = 'fa fa-folder-open color-nv';// no visible en registro y de alta
            var icoUsuarioNVBaW = 'fa fa-folder-open-o color-nv';// no visible en registro y de baja
            var treeUORs = new WebFXTree('Unidades Organizativas');
            treeUORs.icon=icoUsuario;
            treeUORs.openIcon=icoUsuario;
            treeUORs.estilo='webfx-tree-item-area';
            treeUORs.action='javascript:{hacerAlgo();}';
            
            /** prueba **/
            var oficinasRegistro = new Array();
            
            /** prueba **/
            
            
            // rellenar Array javascript con objetos UORs y otro para indexar con los uor_cod
            <%
            MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");

            Vector listaUORDTOs = bForm.getListaNuevasUORs();
            ArbolImpl arbol = bForm.getArbol();
            for (int j = 0; j < listaUORDTOs.size(); j++) {
                UORDTO dto = (UORDTO) listaUORDTOs.get(j);
%>
    
        oficinasRegistro[<%=j%>] = ['<%=dto.getUor_cod()%>','<%=dto.getUor_cod_vis()%>','<%=dto.isOficinaRegistro()%>','<%=dto.getUor_tipo()%>'];
        
    
        // array con los objetos tipo uor mapeados por el array de arriba
        uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
        
        // array con los códigos visibles
        uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';   
            
            <%
            }
%>
        
    <%= arbol.toJavascript("treeUORs", "icoUsuario", "icoUsuarioBaW", "icoUsuarioNV", "icoUsuarioNVBaW",
                        "hacerAlgo()", "webfx-tree-item-area")%>
                                                        
                            
                        // Comprueba en el array oficinasRegistro, si una uor es de tipo oficina de registro
                        // param: codUorVisible: Código visible de uor
                        function isOficinaRegistro(codUorVisible){   
                            var exito = false;                            
                            for(i=0;oficinasRegistro!=null && i<oficinasRegistro.length;i++){                                
                               if(oficinasRegistro[i][1]==codUorVisible && oficinasRegistro[i][2]=="true"){
                                   exito = true;
                                   break;
                               }                                
                            }// for                            
                            return exito;
                        }
                        
                        
                        // Comprueba en el array oficinasRegistro, si una uor se trata de una uor de tipo Registro
                        // param: codUorVisible: Código visible de uor
                        function isUnidadTipoRegistro(codUorVisible){                            
                            var exito = false;
                            for(i=0;oficinasRegistro!=null && i<oficinasRegistro.length;i++){
                               if(oficinasRegistro[i][1]==codUorVisible && oficinasRegistro[i][3]=="1"){
                                   exito = true;
                                   break;
                               }                                
                            }// for                            
                            return exito;
                        }
                        
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
                            
                                                        
                            /***** PRUEBA *****/                            
                            document.forms[0].tipo.disabled = false;                            
                            if(isOficinaRegistro(nodo2CodVis(treeUORs.getSelected().text))){                                
                                // La uor es una oficina de registro, por tanto, nunca podrá ser de tipo registro, entonces se desactiva el check "Registro"                            
                                document.forms[0].tipo.disabled = true;
                            }
                            
                            /***** PRUEBA *****/
                            
                            
                            
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
                                <%if (accede.equals("si")) {%>
                                document.campos.codigo_accede.value = uor_en_array.uor_cod_accede;
                                <%}%>
                                document.campos.nombre.value = uor_en_array.uor_nom;
                                document.campos.email.value = uor_en_array.uor_email;
                                
                                // código visible de la Unidad padre si la hay
                                if(uor_en_array.uor_pad != '') {
                                    var uor_padre = buscarUorPorCod(uor_en_array.uor_pad);
                                    document.campos.unidad_sup.value = uor_padre.uor_cod_vis;
                                }
                                // estado
                                if(uor_en_array.uor_estado == 'A') {
                                    document.campos.estado[0].checked = true;
                                    $(".etiqAgachar").css("display","none");
                                } else {
                                    document.campos.estado[1].checked = true;
                                    $(".etiqAgachar").css("display","");
                                }
                                //tipo
                                
                                if(uor_en_array.uor_tipo == '1') {
                                    document.campos.tipo.checked = true;
                                    document.campos.rexistro_xeral.disabled =false;
                                    
                                }
                                else {
                                    document.campos.tipo.checked = false;
                                    document.campos.rexistro_xeral.disabled =true;
                                }
                                //REXITRO XERAL
                                if(uor_en_array.uor_rexistro_xeral == '1') {
                                    document.campos.rexistro_xeral.checked = true;
                                }
                                else {
                                    document.campos.rexistro_xeral.checked = false;
                                }
                                
                                // fechas
                                document.campos.fecha_alta.value = uor_en_array.uor_fecha_alta;
                                document.campos.fecha_baja.value = uor_en_array.uor_fecha_baja;
                                // no visible en el registro
                                if(uor_en_array.uor_no_reg == '1') {
                                    document.campos.no_visible_registro.checked = true;
                                }
                                else {
                                    document.campos.no_visible_registro.checked = false;
                                }
                                
                                if(uor_en_array.uorOculta == 'S') {
                                    document.campos.uorOculta.checked = true;
                                }
                                else {
                                    document.campos.uorOculta.checked = false;
                                }
                                
                                // oficina de registro
                                if(uor_en_array.oficina_registro == '1') {
                                    document.campos.oficina_registro.checked = true;
                                }else
                                    document.campos.oficina_registro.checked = false;
                                
                                activarBotones("AMELS");
                            }
                            else {
                                alert('Error de datos de UORs');
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
                                for(var k=0; k<treeUORs.getSelected().childNodes.length; k++) {
                                    var bool_temp = tieneDescenAlta(treeUORs.getSelected().childNodes[k]);
                                    //alert('iter: ' + k + ', valor: ' + bool_temp);
                                    tiene = tiene || bool_temp;
                                }
                            }
                            return tiene;
                        }
                        
                        /*
                        Devuelve true si hay alguna UOR de alta descendiente del nodo seleccionado
                        */
                        function buscaDescen() {
                            var tiene = false;

                            // si el nodo no tiene hijos
                            if(treeUORs.getSelected().childNodes.length == 0) {
                                // en principio no usamos el estado del seleccionado
                            }
                            else {
                                tiene=true;
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
                        
                        function pulsarNombre(){
                            
                            
                            document.forms[0].opcion.value= 'cargarNombre';
                            //document.forms[0].ordenar[0].checked=false;
                            //document.forms[0].ordenar[1].checked=true;
                            document.forms[0].target= "mainFrame";
                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                            document.forms[0].submit();
                            
                        }
                        
                        function pulsarCodigo(){
                            
                            
                            document.forms[0].opcion.value= 'cargar';
                            //document.forms[0].ordenar[0].checked=true;
                            //document.forms[0].ordenar[1].checked=false;
                            document.forms[0].target= "mainFrame";
                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                            document.forms[0].submit();
                        }
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
                                    if(buscaDescen() == true) {
                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjTieneDescAlta")%>');      
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
                                    //var nodo = buscarUorPorCodVisibleEstado(uors, nodo2CodVis(treeUORs.getSelected().text), estado);
                                    var nodo = buscarUorPorCodVisibleEstadoDesc(
                                        uors, nodo2CodVis(treeUORs.getSelected().text), estado,
                                        nodo2Descripcion(treeUORs.getSelected().text));
                                    
                                    cod_visible_seleccionado = nodo2CodVis(treeUORs.getSelected().text);
                                    if(nodo == null || cod_visible_seleccionado == null) {
                                        return;
                                    }
                                    if(jsp_alerta("C",'<%=descriptor.getDescripcion("desEliminarUOR")%>') ==1) {
                                        document.forms[0].codUOR.value = nodo.uor_cod;
                                        document.forms[0].opcion.value = "eliminar";
                                        document.forms[0].target = "oculto";
                                        
                                        var padre = treeUORs.getSelected().parentNode;
                                        //treeUORs.getSelected().remove();
                                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                                        document.forms[0].submit();
                                        //            padre.icon = icoUsuario;
                                        //            padre.openIcon = icoUsuario;
                                    }
                                }
                                pulsarLimpiar();
                            }
                            
                            // actúa según el resultado de borrar en el action
                            // resultado puede ser: realizada, hayRegistros, hayUsuarios y noEliminada
                            function postPulsarEliminar(resultado) {
                                
                                   if(resultado =='hayOficinasRegistro'){
                                       jsp_alerta('A', '<%=descriptor.getDescripcion("msgErrDeleteUorOfisReg")%>');
                                   }else
                                   if(resultado == 'hayRegistros') {
                                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneReg")%>');
                                    }
                                    else if(resultado == 'noEliminada') {
                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORNoEliminada")%>');
                                        }
                                        else if(resultado == 'hayUsuarios') {
                                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneUsu")%>');                                            
                                            }
                                            else if(resultado == 'hayTramites') {
                                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneTram")%>');
                                                }
                                                else if(resultado == 'hayInformes') {
                                                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneInf")%>');
                                                    }
                                                        treeUORs.getSelected().remove();
                                                        document.forms[0].opcion.value = 'cargar';
                                                        document.forms[0].target = "mainFrame";
                                                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                                                        document.forms[0].submit();
                                                    }
                                                
                                                // actúa según el resultado de modificar en el action
                                                // resultado puede ser: realizada, hayRegistros, hayUsuarios y noModificada
                                                function postPulsarModificar(resultado) {
                                                     if(resultado == 'hayRegistros') { 
                                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneRegModif")%>');
                                                        }
                                                        else if(resultado == 'noModificada') {
                                                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORNoModificada")%>');
                                                            }
                                                            else if(resultado == 'hayUsuarios') { 
                                                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneUsuModif")%>');
                                                                }
                                                                else if(resultado == 'hayTramites') { 
                                                                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneTramModif")%>');
                                                                    }
                                                                    else if(resultado == 'hayInformes') {
                                                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjUORTieneInfModif")%>');
                                                                        }
                                                                       treeUORs.getSelected().remove();
                                                                        document.forms[0].opcion.value = 'cargar';
                                                                        document.forms[0].target = "mainFrame";
                                                                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                                                                        document.forms[0].submit();
                                                                    }
                                                                    
                                                                    function pulsarLimpiar() { 
                                                                        // codigo
                                                                        document.campos.codUOR.value = '';
                                                                        // codigo visible
                                                                        document.campos.codigo_visible.value = '';
                                                                        <%if (accede.equals("si")) {%>
                                                                        // codigo accede
                                                                        document.campos.codigo_accede.value = '';
                                                                        <%}%>
                                                                        // nombre o descripcion de la UOR
                                                                        document.campos.nombre.value = '';
                                                                        // codigo visible de la unidad padre
                                                                        document.campos.unidad_sup.value = '';
                                                                        // tipo
                                                                        document.campos.tipo.checked = false;
                                                                        document.campos.tipo.value = '';
                                                                        // estado
                                                                        document.campos.estado[0].checked = false;
                                                                        document.campos.estado[1].checked = false;
                                                                        // fechas
                                                                        document.campos.fecha_alta.value = '';
                                                                        document.campos.fecha_baja.value = '';
                                                                        // email
                                                                        document.campos.email.value = '';
                                                                        // visible en registro?
                                                                        document.campos.no_visible_registro.checked = false;  
                                                                        
                                                                        document.campos.uorOculta.checked = false;                                                                        
                                                                        
                                                                        document.forms[0].tipo.disabled = false;
                                                                        document.forms[0].oficina_registro.checked = false;
                                                                    }
                                                                    
                                                                    
                                                                    

                                    function tieneAncestroTipoRegistro(codigoUor){                                                                   
                                            var dato;                                                                                                                                            

                                            var fin = false;
                                            var exito =false;

                                            var codigo = codigoUor;
                                            while(!fin){
                                                    dato = buscarUorPorCod(codigo);
                                                    if(dato!=null){
                                                            if(dato.uor_tipo=='1'){
                                                                    // La unidad es de tipo Registro                                                                                
                                                                    fin =true;
                                                                    exito = true;
                                                            }else
                                                            if(dato.uor_pad==null || dato.uor_pad==''){
                                                                    fin = true;
                                                                    exito = false;
                                                            }else
                                                                    codigo = dato.uor_pad;                                                                                    

                                                    }//if

                                            }//while 

                                            return exito;

                                    }//tieneAncestroTipoRegistro
                                                                    
                                                                    function pulsarModificar() { 
                                                                        document.forms[0].opcion.value = 'modificar';
                                                                        document.forms[0].target = "oculto";
                                                                        
                                                                        var input_oficina_registro=0;
                                                                        
                                                                        if(document.forms[0].oficina_registro.checked)
                                                                            input_oficina_registro = 1;
                                                                        
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
                                                                        //var uor_en_array = buscarUorPorCodVisibleEstado(uors, cod_visible_seleccionado, estado_selected);
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
                                                                            var input_uor_tipo;
                                                                            if(document.forms[0].tipo.checked == true) {
                                                                                input_uor_tipo = '1';
                                                                                document.forms[0].tipo.value = '1';
                                                                            }
                                                                            else {
                                                                                input_uor_tipo = '';
                                                                                document.forms[0].tipo.value = '0';
                                                                            }
                                                                            //rexistro xeral
                                                                            var input_rexistro_xeral;
                                                                            if(document.forms[0].rexistro_xeral.checked == true) {
                                                                                input_rexistro_xeral ='1';
                                                                                document.forms[0].rexistro_xeral.value ='1';
                                                                            } else{
                                                                            input_rexistro_xeral ='0';
                                                                            document.forms[0].rexistro_xeral.value ='0';
                                                                        }
                                                                        //no_visible_registro
                                                                        var input_no_visible_registro;
                                                                        if(document.forms[0].no_visible_registro.checked == true) {
                                                                            input_no_visible_registro = '1';
                                                                            document.forms[0].no_visible_registro.value = '1';
                                                                        }else {
                                                                        input_no_visible_registro = '0';
                                                                        document.forms[0].no_visible_registro.value = '0';
                                                                    }
                                                                    
                                                                        var inputUorOculta;
                                                                        if (document.forms[0].uorOculta.checked == true) {
                                                                            inputUorOculta = 'S';
                                                                            document.forms[0].uorOculta.value = 'S';
                                                                        }else {
                                                                            inputUorOculta = 'N';
                                                                            document.forms[0].uorOculta.value = 'N';
                                                                        }
                                                                    
                                                                    var input_uor_cod_vis = document.forms[0].codigo_visible.value.trim();
                                                                    var input_uor_cod_accede="";
                                                                    <%if (accede.equals("si")) {%>
                                                                    input_uor_cod_accede = document.forms[0].codigo_accede.value.trim();
                                                                    <%} %>
                                                                    var input_uor_email = document.forms[0].email.value;
                                                                    var input_uor_unidad_sup = document.campos.unidad_sup.value;
                                                                    
                                                                    
                                                                    ////////////////////
                                                                    // COMPROBACIONES
                                                                    ////////////////////
                                                                    // si no hay nada seleccionado
                                                                    if(treeUORs.getSelected() == null) {
                                                                        jsp_alert('A', '<%=descriptor.getDescripcion("msjProbTecnico")%>');
                                                                            return
                                                                        }
                                                                        
                                                                        // procesar campos obligatorios
                                                                        // nombre
                                                                        if(input_uor_nom == '') {
                                                                            jsp_alerta("A",'<%=descriptor.getDescripcion("faltaDescripUOR")%>');
                                                                                return;
                                                                            }
                                                                            // codigo visible
                                                                            if(input_uor_cod_vis == '') {
                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("faltaCodVisUOR")%>');
                                                                                    return;
                                                                                }
                                                                                
                                                                                // email   
                                                                                if (input_uor_email != '' && noesemail(input_uor_email)) {
                                                                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligEmail")%>');
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
                                                                                        (input_uor_tipo == uor_en_array.uor_tipo)&&
                                                                                        (input_uor_email == uor_en_array.uor_email)&&
                                                                                        (input_uor_unidad_sup == cod_vis_pad_anterior)&&
                                                                                        (input_no_visible_registro == uor_en_array.uor_no_reg)&&
                                                                                        (inputUorOculta == uor_en_array.uorOculta)&&
                                                                                        (input_rexistro_xeral == uor_en_array.uor_rexistro_xeral) &&
                                                                                        (input_oficina_registro == uor_en_array.oficina_registro)
                                                                                    ) {
                                                                                    <%if (accede.equals("si")) {%>
                                                                                    if (input_uor_cod_accede == uor_en_array.uor_cod_accede) {
                                                                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoModifUOR")%>');
                                                                                            pulsarLimpiar();
                                                                                            return;
                                                                                        }
                                                                                        <%} else {%>
                                                                                    
                                                                                        
                                                                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoModifUOR")%>');
                                                                                            pulsarLimpiar();
                                                                                            return;
                                                                                            <%}%>
                                                                                        }
                                                                                        
                                                                                        // si el estado cambia de baja a alta, modificar fecha de alta
                                                                                        if((input_uor_estado == 'A')&&(uor_en_array.uor_estado == "B")) {
                                                                                            document.forms[0].fecha_alta.value = fecha_hoy;
                                                                                            uor_en_array.uor_fecha_alta = fecha_hoy;
                                                                                            document.forms[0].fecha_baja.value = '';
                                                                                            uor_en_array.uor_fecha_baja =  null;
                                                                                        }
                                                                                        // si al revés, cambia de alta a baja
                                                                                        else if((input_uor_estado == 'B')&&(uor_en_array.uor_estado == "A")) {                                                                                            

                                                                                            if(buscaDescenAlta() == true) {
                                                                                                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjTieneDescAlta")%>');
                                                                                                     pulsarLimpiar();
                                                                                                 return;}
                                                                                            document.forms[0].fecha_baja.value = fecha_hoy;
                                                                                            uor_en_array.uor_fecha_baja = fecha_hoy;
                                                                                        }
                                                                                        
                                                                                        var uor_pad_cambiado = false;
                                                                                        // si hay algo en el campo de Unidad superior distinto a lo que había
                                                                                        if(input_uor_unidad_sup != cod_vis_pad_anterior) {
                                                                                            uor_pad_cambiado = true;
                                                                                            // comprobar que no se intenta poner como padre a uno mismo
                                                                                            if(input_uor_unidad_sup == uor_en_array.uor_cod_vis) {
                                                                                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorUORSuperior")%>');
                                                                                                    pulsarLimpiar();
                                                                                                    return;
                                                                                                }
                                                                                                
                                                                                                // comprobar q la nueva existe (si es q no es vacía)
                                                                                                /*if(input_uor_unidad_sup != '' && buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'A') == null &&
                                                                                                buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'B') == null) {*/
                                                                                                // 27/04/2006 sólo permitir cambio a UOR padre de alta
                                                                                                if(input_uor_unidad_sup != '' && buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'A') == null) {
                                                                                                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorUORSuperior")%>'  + '<%=descriptor.getDescripcion("msjMotivo")%>');
                                                                                                        pulsarLimpiar();
                                                                                                        return;
                                                                                                    }
                                                                                                }
                                                                                                
                                                                                                // ver si ha cambiado alguno de los campos: nombre, email, tipo
                                                                                                var info_no_cambia = (input_uor_nom == uor_en_array.uor_nom)&&
                                                                                                (input_uor_tipo == uor_en_array.uor_tipo)&&
                                                                                                (input_uor_email == uor_en_array.uor_email)&&
                                                                                                (input_no_visible_registro == uor_en_array.uor_no_reg)&&
                                                                                                (inputUorOculta == uor_en_array.uorOculta)&&
                                                                                                (input_rexistro_xeral == uor_en_array.uor_rexistro_xeral) &&
                                                                                                (input_oficina_registro == uor_en_array.oficina_registro);
                                                                                                <%if (accede.equals("si")) {%>
                                                                                                info_no_cambia = info_no_cambia && (input_uor_cod_accede == uor_en_array.uor_cod_accede)
                                                                                                <%}%>
                                                                                                
                                                                                                // no puede haber 2 con el mismo estado + cod_vis + descripcion a menos q sea pq estamos cambiando
                                                                                                //la unidad superior o modificando otro campo
                                                                                                if((uor_pad_cambiado == false)&&(buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, input_uor_estado, input_uor_nom) != null)&&
                                                                                                    (info_no_cambia == true)) {
                                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjExUOR")%>');
                                                                                                    pulsarLimpiar();
                                                                                                    return;
                                                                                                }
                                                                                                                                                                                                
                                                                                                if(input_uor_unidad_sup!='' && input_uor_tipo=='1'){
                                                                                                    
                                                                                                    if(isOficinaRegistro(input_uor_unidad_sup)){
                                                                                                        jsp_alerta('A','<%=descriptor.getDescripcion("msgErrUorRegHijaOfiReg")%>');
                                                                                                        return;
                                                                                                    }

                                                                                                    if(isUnidadTipoRegistro(input_uor_unidad_sup)){
                                                                                                        jsp_alerta('A','<%=descriptor.getDescripcion("msgErrUorRegHijaUorReg")%>');
                                                                                                        return;
                                                                                                    }
                                                                                                }
                                                                                                
                                                                                                
                                                                                                /*********************************/
                                                                                                if(input_oficina_registro=='1' && input_uor_tipo=='1'){
				
                                                                                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrUnidadRegistroOfi")%>');                                                                                                    
                                                                                                        return;
                                                                                                }else{
                                                                                                                    
                                                                                                        if(input_oficina_registro==1){                                                                                                        
                                                                                                                // Si es oficina de registro, se comprueba si su padre o alguno de sus ancestros es de tipo registro                                                                                                                                                                                                            
                                                                                                                if(input_uor_unidad_sup==null || input_uor_unidad_sup==''){
                                                                                                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrPadreTipoRegistro")%>');                                                                                                                                                                                                                
                                                                                                                        return;
                                                                                                                }else{                                                                                                            
                                                                                                                            
                                                                                                                        var dato = buscarUorPorCodVisible(uors, input_uor_cod_vis);
                                                                                                                        if(dato!=null){                                                                                                                
                                                                                                                                var continuar = tieneAncestroTipoRegistro(dato.uor_cod);

                                                                                                                                if(!continuar){
                                                                                                                                        jsp_alerta('A','<%=descriptor.getDescripcion("msgErrPadreTipoRegistro")%>');
                                                                                                                                        return;
                                                                                                                                }
                                                                                                                        }
                                                                                                                }// else
                                                                                                        }


                                                                                                        if(input_oficina_registro=='1' && input_uor_unidad_sup!='' && input_uor_unidad_sup!=uor_en_array.uor_pad){
                                                                                                                // Si es oficina de registro y se cambia de padre, se comprueba si el nuevo padre es de unidad de registro
                                                                                                                // o tiene alguna unidad que lo sea
                                                                                                                var dato = buscarUorPorCodVisible(uors, input_uor_unidad_sup);   
                                                                                                                var continuar = tieneAncestroTipoRegistro(dato.uor_cod);

                                                                                                                if(!continuar){
                                                                                                                        jsp_alerta('A','EL NUEVO PADRE NO ES VÁLIDO');
                                                                                                                        return;
                                                                                                                }

                                                                                                        }

                                                                                                }//else																

                                                                                                
                                                                                                /*********************************/
                                                                                                
                                                                                                //////////////////////////////////////////
                                                                                                // continuar modificación normalmente   //
                                                                                                //////////////////////////////////////////

                                                                                                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desModificarUOR")%>', "") ==1) {                                                                                                  
                                                                                                    // rellenar los input
                                                                                                    document.forms[0].codUOR.value = uor_en_array.uor_cod;
                                                                                                    
                                                                                                    // uor padre
                                                                                                    if(uor_pad_cambiado == true) {
                                                                                                        // si han borrado el campo = cambiar a hijo de la raiz
                                                                                                        if(input_uor_unidad_sup == '') {
                                                                                                            
                                                                                                            document.forms[0].codPad.value = "";
                                                                                                            
                                                                                                        }else {
                                                                                                        // buscar cod del padre segun el cod visible del campo y descripcion, preferencia uno de alta
                                                                                                        var uor_pad = buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'A');
                                                                                                        if(uor_pad == null) { // si es null, debe estar de baja
                                                                                                            uor_pad = buscarUorPorCodVisibleEstado(uors, input_uor_unidad_sup, 'B');
                                                                                                        }
                                                                                                        if(uor_pad == null) { // si tampoco hay de baja, hay un error grave
                                                                                                            alert('Error en los datos de UORs');
                                                                                                        }
                                                                                                        document.forms[0].codPad.value = uor_pad.uor_cod;
                                                                                                    }
                                                                                                }else {                                                                                             
                                                                                                document.forms[0].codPad.value = uor_en_array.uor_pad;
                                                                                            }
                                                                                            
                                                                                            
                                                                                            
                                                                                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                                                                                            document.forms[0].submit();
                                                                                            //pulsarLimpiar();
                                                                                            desactivarBotones("AMEL");
                                                                                            //treeUORs.getSelected().deSelect();
                                                                                        }
                                                                                        else{
                                                                                            // Si se pretendía dar de baja la uor y se cancela => Se vacía el campo con la fecha de baja
                                                                                            if((input_uor_estado == 'B')&&(uor_en_array.uor_estado == "A"))
                                                                                            {
                                                                                                 document.forms[0].fecha_baja.value = '';
                                                                                                 uor_en_array.uor_fecha_baja = '';
                                                                                            }
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
                                                                                                                                                                                
                                                                                        var input_uor_tipo;
                                                                                        if(document.forms[0].tipo.checked == true) {
                                                                                            input_uor_tipo = '1';
                                                                                        }
                                                                                        else {
                                                                                            input_uor_tipo = '';
                                                                                        }
                                                                                                                                                                                
                                                                                        var input_no_registro;
                                                                                        if(document.forms[0].no_visible_registro.checked == true) {
                                                                                            input_no_registro = '1';
                                                                                        }
                                                                                        else {
                                                                                            input_no_registro = '0';
                                                                                        }
                                                                                        
                                                                                        var inputUorOculta;
                                                                                        if (document.forms[0].uorOculta.checked == true) {
                                                                                            inputUorOculta = 'S';
                                                                                            document.forms[0].uorOculta.value = 'S';
                                                                                        }else {
                                                                                            inputUorOculta = 'N';
                                                                                            document.forms[0].uorOculta.value = 'N';
                                                                                        }
                                                                                        
                                                                                        var input_uor_cod_vis = document.forms[0].codigo_visible.value.trim();
                                                                                        var input_uor_email = document.forms[0].email.value;
                                                                                        
                                                                                        // ver si hay un UOR en el array de UORs con el mismo estado ,cod_vis y descripcion
                                                                                        var uor_en_array = buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, input_uor_estado, input_uor_nom);
                                                                                        
                                                                                        ////////////////////
                                                                                        // COMPROBACIONES
                                                                                        ////////////////////
                                                                                        // procesar campos obligatorios
                                                                                        // nombre
                                                                                        if(input_uor_nom == '') {
                                                                                            jsp_alerta("A",'<%=descriptor.getDescripcion("faltaDescripUOR")%>');
                                                                                                return;
                                                                                            }
                                                                                            // codigo visible
                                                                                            if(input_uor_cod_vis == '') {
                                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("faltaCodVisUOR")%>');
                                                                                                    return;
                                                                                                }
                                                                                                
                                                                                                // email
                                                                                                if (input_uor_email != '' && noesemail(input_uor_email)) {
                                                                                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligEmail")%>');
                                                                                                        return;
                                                                                                    }
                                                                                                    
                                                                                                    // 01/05/2006: ahora no se puede dar de alta una UOR cuyo nombre + cod visible exista
                                                                                                    if(buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, 'A', input_uor_nom) != null ||
                                                                                                        buscarUorPorCodVisibleEstadoDesc(uors, input_uor_cod_vis, 'B', input_uor_nom) != null) {
                                                                                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjExUOR")%>');
                                                                                                        pulsarLimpiar();
                                                                                                        return;
                                                                                                    }
                                                                                                    
                                                                                                    // no puede haber 2 semántica/ idénticas: mismo estado + cod_vis + descripcion, ya sea de alta o de baja
                                                                                                    if(uor_en_array != null) {
                                                                                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjExUOR")%>');
                                                                                                            pulsarLimpiar();
                                                                                                            return;
                                                                                                        }
                                                                                                        
                                                                                                        // si esta de alta, no puede haber otro con el mismo cod visible y tambien de alta
                                                                                                        if((input_uor_estado == 'A')&&(buscarUorPorCodVisibleEstado(uors, input_uor_cod_vis, 'A') != null)) {
                                                                                                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjExUOR")%>');
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
                                                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("faltaEstadoUOR")%>');
                                                                                                                    return;
                                                                                                                }
                                                                                                                // tipo
                                                                                                                if(input_uor_tipo == '1') {
                                                                                                                    document.forms[0].tipo.value = '1';
                                                                                                                }
                                                                                                                else {
                                                                                                                    document.forms[0].tipo.value = '';
                                                                                                                }
                                                                                                                // no visible desde registro
                                                                                                                document.forms[0].no_visible_registro.value = input_no_registro;

                                                                                                                // inputs ocultos
                                                                                                                // cod del nodo padre
                                                                                                                //CH - Buscar el codigo del padre
                                                                                                                if(document.campos.unidad_sup.value == '') {
                                                                                                                    
                                                                                                                    document.forms[0].codPad.value = '';
                                                                                                                }
                                                                                                                else {
                                                                                                                    var unidad_sup=document.campos.unidad_sup.value;
                                                                                                                    var uor_pad = buscarUorPorCodVisibleEstado(uors, unidad_sup, 'A');
                                                                                                                    
                                                                                                                    // comprobar que no se intenta poner como padre a uno mismo
                                                                                                                    if(unidad_sup ==  input_uor_cod_vis ) {
                                                                                                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorUORSuperior")%>');
                                                                                                                            pulsarLimpiar();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        
                                                                                                                        //comprueba que el padre exista y este dado de alta
                                                                                                                        if(unidad_sup != '' && buscarUorPorCodVisibleEstado(uors, unidad_sup, 'A') == null) {
                                                                                                                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorUORSuperior")%>'  + ": está de baja");
                                                                                                                                pulsarLimpiar();
                                                                                                                                return;
                                                                                                                            } 
                                                                                                                            document.forms[0].codPad.value = uor_pad.uor_cod;
                                                                                                                        }
                                                                                                                        
                                                                                                                        /*** prueba ***/
                                                                                                                        // Se comprueba que si el padre es de uor de tipo Registro o uor oficina de registro, 
                                                                                                                        // que no esté marcado el check de Registro                                                                                                                               
                                                                                                                        if(unidad_sup!='' && input_uor_tipo=='1'){
                                                                                                                            
                                                                                                                            if(isOficinaRegistro(unidad_sup)){
                                                                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrUorRegHijaOfiReg")%>');
                                                                                                                                return;
                                                                                                                            }
                                                                                                                                                                                                                                                        
                                                                                                                            if(isUnidadTipoRegistro(unidad_sup)){
                                                                                                                                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrUorRegHijaUorReg")%>');
                                                                                                                                return;
                                                                                                                            }                                                                                                                            
                                                                                                                            
                                                                                                                        }
                                                                                                                        
                                                                                                                        /*** prueba ***/
                                                                                                                        
                                                                                                                        //fin de asignar nodo padre
                                                                                                                        document.forms[0].codUOR.value = ''; // no es necesario; se obtiene de la BD
                                                                                                                        document.forms[0].opcion.value = 'alta';
                                                                                                                        document.forms[0].target.value = "mainFrame";
                                                                                                                        
                                                                                                                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                                                                                                                        document.forms[0].submit();
                                                                                                                        pulsarLimpiar();
                                                                                                                    }
                                                                                                                                                                                                                                       
                                                                                                                    
                                             function actualizarEstilosUltimaBusqueda(){
                                                 
                                             }
                                             
            function controlarEtiqAgachar(){
                if (document.getElementById('estado_alta').checked){
                    $(".etiqAgachar").css("display","none");
                } else {
                    $(".etiqAgachar").css("display","");
                }
            }

        </script>
    </head>
    
    <body class="bandaBody" onload="javascript:{/*pleaseWait('off')*/;
        inicializar();}">
        
        
        
<form name="campos" id="campos" action="">
    <input  type="hidden"  name="opcion" id="opcion">
    <!-- información sobre el UOR seleccionado que no se muestra en pantalla -->
    <input  type="hidden"  name="codUOR" id="codUOR">
    <input  type="hidden"  name="codPad" id="codPad">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantUORs")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <td align="justify"  valign="top">
                    <div class="webfx-tree-div" style="height:450px;width:100%;overflow:auto;">
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
        <table width="100%">
            <tr height="28">
                <td class="etiqueta">
                    <%=descriptor.getDescripcion("etiqCodigoUOR")%>:
                </td>
                <td>
                    <input type="text" name="codigo_visible" id="codigo_visible" size="8" maxlength="8" class="inputTextoObligatorio" onkeyup="return SoloAlfanumericos(this);"/>
                </td>
                <td style="width:20px;">&nbsp;</td>
                <td class="etiqueta">
                    <%=descriptor.getDescripcion("etiqDesUOR")%>:
                </td>
                <td colspan="8">
                    <input type="text" name="nombre" id="nombre" size="95" maxlength="120" style="width:625px" class="inputTextoObligatorio" onkeyup="return xAMayusculas(this);"/>
                </td>
            </tr>
            <tr>
                <td style="height: 29" class="etiqueta">
                    <%=descriptor.getDescripcion("etiqOrdenacionUOR")%>:

                </td>

                <% if (opcion.equals("cargarNombre")) {%>
                <td class="etiqueta" align="left">
                    <input type="radio" name="ordenar" id="ordenar_codigo" value="A"   onclick="pulsarCodigo();" /><%=descriptor.getDescripcion("etiqOrdCodUOR")%>
                </td>
                <td class="etiqueta" align="left"  style="width: 90px" >
                    <input type="radio" name="ordenar" id="ordenar_nombre" value="B"  checked="true" onclick="pulsarNombre();"/><%=descriptor.getDescripcion("etiqOrdNomUOR")%>
                </td>

                <%} else {%>
                <td class="etiqueta" align="left" >
                    <input type="radio" name="ordenar" id="ordenar_codigo" value="A" checked="true" onclick="pulsarCodigo();" /><%=descriptor.getDescripcion("etiqOrdCodUOR")%>
                </td>
                <td class="etiqueta" align="left" style="width: 90px" >
                  <input type="radio" name="ordenar" id="ordenar_nombre" value="B"  onclick="pulsarNombre();"/><%=descriptor.getDescripcion("etiqOrdNomUOR")%>
                </td>
                <%}%>  

                <td class="etiqueta" align="left"  style="width: 20px">
                    <%=descriptor.getDescripcion("etiqEstadoUOR")%>:
                </td>
                <td class="etiqueta" align="left" >
                    <input type="radio" name="estado" id="estado_alta" onclick="controlarEtiqAgachar()" value="A" checked="true"/><%=descriptor.getDescripcion("etiqEstAltaUOR")%>
                </td> 
                <td class="etiqueta" align="left" style="width: 60px">
                    <input type="radio" name="estado" id="estado_baja" onclick="controlarEtiqAgachar()" value="B"><%=descriptor.getDescripcion("etiqEstBajaUOR")%>
                </td>

                <td class="etiqueta" style="width: 30px">
                    <%=descriptor.getDescripcion("etiqUniSupUOR")%>:
                </td>
                <td>
                    <input type="text" name="unidad_sup" id="unidad_sup" size="6" maxlength="8" class="inputTexto"  onkeyup="return SoloAlfanumericos(this);"/>
                </td>
                <td class="etiqueta">
                    <%=descriptor.getDescripcion("etiqMailUOR")%>:
                </td>
                <td>
                    <input type="text" name="email" id="email" size="42" maxlength="100" value="" style="width:250px" class="inputTextoEmail" onkeyup="return SoloMail(this);"/>
                </td>
            </tr>
            <tr>
                <td class="etiqueta" style="nowrap: true">
                    <%=descriptor.getDescripcion("etiqFechaAltaUOR")%>:
                </td>
                <td align="left">
                    <input type="text" name="fecha_alta" id="fecha_alta" size="10" maxlength="10" value="" readonly="true" class="inputTxtFecha"/>
                </td>
                <td style="width:20px;">&nbsp;</td>
                <td class="etiqueta" style="nowrap: true">
                    <%=descriptor.getDescripcion("etiqFechaBajaUOR")%>:
                </td>
                <td align="left">
                    <input type="text" name="fecha_baja" id="fecha_baja" size="10" maxlength="10" value="" readonly="true" class="inputTxtFecha"/>
                </td>
                <td>
                    &nbsp;
                </td>
                <td align="left" class="etiqueta etiqAgachar"> 
                    <%=descriptor.getDescripcion("ocultar")%>:
                </td>
                <td class="etiqAgachar">
                    <input type="checkbox" name="uorOculta" id="uorOculta"/>
                </td>
            </tr>
            <tr>
                <td class="etiqueta">
                    <%=descriptor.getDescripcion("etiqRegistroUOR")%>:
                </td>
                <td align="left">
                    <input type="checkbox" name="tipo" id="tipo"/>
                </td>
                <td style="width:20px;">&nbsp;</td>
                <td class="etiqueta" style="nowrap: true">
                    <%=descriptor.getDescripcion("etiqRexXeral")%>:
                </td>
                <td align="left">
                    <input type="checkbox" name="rexistro_xeral" id="rexistro_xeral"/>
                </td>
                  <td style="width:20px;">&nbsp;</td>
                <td class="etiqueta" style="nowrap: true" colspan='3'>
                    <%=descriptor.getDescripcion("etiqNoVisibleUOR")%>:
                    <input type="checkbox" name="no_visible_registro" id="no_visible_registro"/>
                </td>

                <td class="etiqueta" style="nowrap: true" colspan='3'>
                    <%=descriptor.getDescripcion("etiqOficinaRegistro")%>:
                    <input type="checkbox" name="oficina_registro" id="oficina_registro"/>
                </td>


            </tr>   
            <%if (accede.equals("si")) {%>
            <tr>
                <td class="etiqueta" style="nowrap: true">
                    <%=descriptor.getDescripcion("etiqAccedeUOR")%>:
                </td>
                <td align="left">
                    <input type="text" name="codigo_accede" id="codigo_accede" size="10" maxlength="10" value="" class="inputTexto" onkeyup="return xAMayusculas(this);"/>

+                                                        </td>
            </tr>
            <%}%>
        </table>
        <div id="tablaBotones" class="botoneraPrincipal">
            <form action="" name="botones" id="botones">
                <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>'
                       name="cmdAlta" id="cmdAlta" onclick="pulsarAlta();" accesskey="A">
                <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>'
                       name="cmdModificar" id="cmdModificar" onclick="pulsarModificar();" accesskey="M">
                <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>'
                       name="cmdEliminar" id="cmdEliminar" onclick="pulsarEliminar();" accesskey="E">
                <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>'
                       name="cmdLimpiar" id="cmdLimpiar" onclick="pulsarLimpiar();" accesskey="L">
                <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>'
                       name="cmdSalir" id="cmdSalir" onclick="pulsarSalir();" accesskey="S">
            </div>      
        </div>      
    </form>
</body>
</html>
