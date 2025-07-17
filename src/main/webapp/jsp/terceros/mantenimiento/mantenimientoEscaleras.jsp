<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title>Untitled</title>
</head>

<body class="bandaBody">
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
    <title>Mantenimiento de Tipos de Escaleras</title>
    <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
    
    <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
    %>
    
    
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
   

    <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
     <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript">
        // VARIABLES GLOBALES
        var lista = new Array();
        var datosEscaleras = new Array();
        var vectorCamposRejilla = ["codigo","descripcion","codigoAntiguo"];
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
        function inicializar(){
            window.focus();
            pleaseWait("off");
            
                
            cargaTablaEscaleras();
            
        }
        
        function cargaTablaEscaleras(){
                 <%
            MantenimientosTercerosForm bForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
            Vector listaEscaleras = bForm.getListaEscaleras();
            int lengthEscaleras = listaEscaleras.size();
            int i = 0;
           %>
               var j=0;
    <%for (i = 0; i < lengthEscaleras; i++) {
                GeneralValueObject escaleras = (GeneralValueObject) listaEscaleras.get(i);%>
                    datosEscaleras[j] = ["<%=(String) escaleras.getAtributo("codigo")%>",
                        "<%=(String) escaleras.getAtributo("descripcion")%>"];
                        lista[j] = datosEscaleras[j];
                        j++;
                        <%}%>
                        tablaEscaleras.lineas = lista;
                        refresca(tablaEscaleras);
                    }
                    
                    // FUNCIONES DE LIMPIEZA DE CAMPOS
                    function limpiarFormulario(){
                        tablaEscaleras.lineas = new Array();
                        refresca(tablaEscaleras);
                        limpiarCamposRejilla();
                    }
                    
                    function limpiarCamposRejilla(){
                        var vector = [document.forms[0].codigo];
                        habilitarGeneral(vector);
                        limpiar(vectorCamposRejilla);
                    }

 
                    function pulsaLimpiarTabla() {
                     tablaEscaleras.selectLinea(-1);
                     limpiarCamposRejilla();
                 }
                    
                    function pulsarLimpiar() {
                        limpiarCamposRejilla();
                       if(tablaEscaleras.selectedIndex != -1 ) {
                            tablaEscaleras.selectLinea(tablaEscaleras.selectedIndex);
                            tablaEscaleras.selectedIndex = -1;
                    }
                    }
                    
                    // FUNCIONES DE PULSACION DE BOTONES
                    function pulsarSalir(){
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
                    }
                    
                    function pulsarEliminar() {
                        if(filaSeleccionada(tablaEscaleras)) {
                                    if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimEsc")%>') ==1) {
                                        var vector = [document.forms[0].codigo];
                                        habilitarGeneral(vector);
                                        document.forms[0].identificador.value = document.forms[0].codigo.value;
                                        document.forms[0].opcion.value = "eliminar";
                                        document.forms[0].target = "oculto";
                                        document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Escaleras.do";
                                        document.forms[0].submit();
                                        deshabilitarGeneral(vector);
                                        limpiarCamposRejilla();
                                        if(tablaEscaleras.selectedIndex != -1 ) {
                                            tablaEscaleras.selectLinea(tablaEscaleras.selectedIndex);
                                            tablaEscaleras.selectedIndex = -1;
                                        }
                                    }
                                }else
                        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                        }

                        function noEliminarEsc() {
                            jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimEsc")%>");
                            }
                            
                            function pulsarModificar(){
                                if(filaSeleccionada(tablaEscaleras)){
                                    if(validarCamposRejilla()){		
                                        var vector = [document.forms[0].codigo];
                                        habilitarGeneral(vector);	
                                        document.forms[0].opcion.value = "modificar";
                                        document.forms[0].target = "oculto";
                                        document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Escaleras.do";
                                        document.forms[0].submit();
                                        deshabilitarGeneral(vector);
                                        limpiarCamposRejilla();
                                    }else
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                    }
                                    else
                                        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                                        }
                                        
                                        function pulsarAlta(){
                                            if (validarCamposRejilla()){
                                                if(noEsta()){
                                                    document.forms[0].opcion.value = "alta";
                                                    document.forms[0].target = "oculto";
                                                    document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Escaleras.do";
                                                    document.forms[0].submit();
                                                    limpiarCamposRejilla();
                                                }
                                            }else
                                            jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                            }
                                            
                                            // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
                                            function noEsta(indice){
                                                var cod = document.forms[0].codigo.value;
                                                for(i=0;(i<lista.length);i++){
                                                    if(i!=indice){
                                                        if((lista[i][0]) == cod)
                                                            return false;
                                                    }
                                                }
                                                return true;
                                            }
                                            
                                            function filaSeleccionada(tabla){
                                                var i = tabla.selectedIndex;
                                                if((i>=0)&&(!tabla.ultimoTable))
                                                    return true;
                                                return false;
                                            }
                                            
                                            function validarCamposRejilla(){
                                                var codigo = document.forms[0].codigo.value;
                                                var descrip = document.forms[0].descripcion.value;
                                                if((codigo!="")&&(descrip!=""))
                                                    return true;
                                                                                 return false;
                                            }

                                            function recuperaDatos(lista2) {
                                                limpiarCamposRejilla();
                                                lista = lista2;
                                                tablaEscaleras.lineas=lista;
                                                refresca(tablaEscaleras);
                                            }
                                            
    </script>

    
    <body onload="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <input  type="hidden"  name="codigoAntiguo" value="">
            
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantEscaleras")%></div>
    <div class="contenidoPantalla">
        <table style="width: 100%">
            <tr> 
                <td> 
                    <div id="tabla"></div>
                </td>
            </tr>
            <tr> 
                <td> 
                    <input type="text" class="inputTextoObligatorio" id="obligatorio"  
                           name="codigo" maxlength="2" 
                           style="width:13%" onkeyup="return xAMayusculas(this);">
                    <input name="descripcion" type="text" class="inputTextoObligatorio" id="obligatorio" 
                           maxlength="60" style="width:86%" onkeyup="return xAMayusculas(this);">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%>
                   name="cmdAlta" onClick="pulsarAlta();" accesskey="A"> 
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%>
                   name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%>
                   name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%>
                   name="cmdLimpiar" onClick="pulsaLimpiarTabla();" accesskey="L">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%>
                   name="cmdSalir" onClick="pulsarSalir();" accesskey="S"> 
        </div>            				            
    </div>            				            
</form>
<script type="text/javascript">
            var tablaEscaleras = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));
            tablaEscaleras.addColumna("120","center",'<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
                tablaEscaleras.addColumna("780","left",'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
                    tablaEscaleras.displayCabecera=true;
                    tablaEscaleras.displayTabla();
                    
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    function rellenarDatos(tableName,rowID){
                        var i = rowID;
                        limpiarCamposRejilla();
                        if((i>=0)&&!tableName.ultimoTable){
                                                var vector = [document.forms[0].codigo];
                                                deshabilitarGeneral(vector);
                            var datosRejilla = [lista[i][0],lista[i][1],lista[i][0]];
                            rellenar(datosRejilla,vectorCamposRejilla);
                                            }
                                        }

                    document.onmouseup = checkKeys; 
                    
                    function checkKeysLocal(evento, tecla){
                        var aux=null;
                        if(window.event)
                            aux = window.event;
                        else
                            aux = evento;

                        var tecla = 0;
                        if(aux.keyCode)
                            tecla = aux.keyCode;
                        else
                            tecla = aux.which;

                        keyDel(aux);
                        
                        if (tecla == 38) upDownTable(tablaEscaleras,lista,tecla);
                        if (tecla == 40) upDownTable(tablaEscaleras,lista,tecla);
                        if (tecla == 13) pushEnterTable(tablaEscaleras,lista);
                    }
                    
        </script>
        
        
    </body>
</html>
