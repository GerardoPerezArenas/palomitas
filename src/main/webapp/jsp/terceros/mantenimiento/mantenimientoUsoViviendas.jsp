<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
    <title>Mantenimiento de Tipos de UsoViviendas</title>
    <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
    
    <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css=usuarioVO.getCss();
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
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript">
        // VARIABLES GLOBALES
        var lista = new Array();
        var datosUsoViviendas = new Array();
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
        function inicializar(){
            window.focus();
            pleaseWait("off");
            
                
            cargaTablaUsoViviendas();
        }
        
        function cargaTablaUsoViviendas(){
                 <%
            MantenimientosTercerosForm bForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
            Vector listaUsoViviendas = bForm.getListaUsoViviendas();
            int lengthUsoViviendas = listaUsoViviendas.size();
            int i = 0;
    %>
        var j=0;
    <%for (i = 0; i < lengthUsoViviendas; i++) {
                GeneralValueObject usoViviendas = (GeneralValueObject) listaUsoViviendas.get(i);%>
                    datosUsoViviendas[j] = ["<%=(String) usoViviendas.getAtributo("codUsoVivienda")%>",
                        "<%=(String) usoViviendas.getAtributo("descUsoVivienda")%>"];
                        lista[j] = datosUsoViviendas[j];
                        j++;
                        <%}%>
                        tablaUsoViviendas.lineas = lista;
                        refresca(tablaUsoViviendas);
                    }
                    
                    // FUNCIONES DE LIMPIEZA DE CAMPOS
                    function limpiarFormulario(){
                        tablaUsoViviendas.lineas = new Array();
                        refresca(tablaUsoViviendas);
                        limpiarInputs();
                    }
                    
                    function limpiarInputs(){
                        var vector = [document.forms[0].txtCodigo];
                        habilitarGeneral(vector);
                        document.forms[0].txtCodigo.value = "";
                        document.forms[0].txtDescripcion.value = "";
                    }
                    
                    function pulsarLimpiar() {
                        limpiarInputs();
                        if(tablaUsoViviendas.selectedIndex != -1 ) {
                            tablaUsoViviendas.selectLinea(tablaUsoViviendas.selectedIndex);
                            tablaUsoViviendas.selectedIndex = -1;
                        }
                    }
                    
                    // FUNCIONES DE PULSACION DE BOTONES
                    function pulsarSalir(){
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
                    }
                    
                    function pulsarEliminar() {
                        if(tablaUsoViviendas.selectedIndex != -1) {
                            if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimUsoViv")%>') ==1) {
                                var vector = [document.forms[0].txtCodigo];
                                habilitarGeneral(vector);
                                document.forms[0].identificador.value = lista[tablaUsoViviendas.selectedIndex][0];
                                document.forms[0].opcion.value = "eliminar";
                                document.forms[0].target = "oculto";
                                document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/UsoViviendas.do";
                                document.forms[0].submit();
                                deshabilitarGeneral(vector);
                                limpiarInputs();
                                if(tablaUsoViviendas.selectedIndex != -1 ) {
                                    tablaUsoViviendas.selectLinea(tablaUsoViviendas.selectedIndex);
                                    tablaUsoViviendas.selectedIndex = -1;
                                }
                            }
                        }
                        else jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                        }
                        
                        function noEliminarTOcup() {
                            jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimTOcup")%>");
                            }
                            
                            function pulsarModificar(){
                                if(tablaUsoViviendas.selectedIndex != -1){
                                    if(validarFormulario()){			
                                        var vector = [document.forms[0].txtCodigo];
                                        habilitarGeneral(vector);
                                        document.forms[0].opcion.value = "modificar";
                                        document.forms[0].target = "oculto";
                                        document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/UsoViviendas.do";
                                        document.forms[0].submit();
                                        deshabilitarGeneral(vector);
                                    }
                                }
                                else
                                    jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                                    }
                                    
                                    function pulsarAlta(){
                                        var cod = document.forms[0].txtCodigo.value;
                                        var existe = 0;
                                        if (validarFormulario()){
                                            for(var i=0; (i < lista.length) && (existe == 0); i++){
                                                if((lista[i][0]) == cod)
                                                    existe = 1;
                                            }
                                            if(existe == 0){
                                                document.forms[0].opcion.value = "alta";
                                                document.forms[0].target = "oculto";
                                                document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/UsoViviendas.do";
                                                document.forms[0].submit();
                                            }
                                            else
                                                jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                                                }
                                            }
                                            
                                            function buscar(tabla){
                                                var auxDes = "";
                                                if((event.keyCode != 40)&&(event.keyCode != 38)){
                                                    if(event.keyCode != 13){
                                                        auxDes = document.forms[0].txtDescripcion.value;
                                                        if(event.keyCode == 8){
                                                            if(auxDes.length == 0){
                                                                limpiarInputs();
                                                            }
                                                        }
                                                        selecFila(auxDes);
                                                    }else{
                                                    if((tabla.selectedIndex>-1)&&(tabla.selectedIndex < lista.length)){
                                                        pintaDatos();
                                                    }
                                                }
                                            }
                                        }
                                        
                                        function selecFila(des){
                                            if(des.length != 0){
                                                for (var x=0; x<lista.length; x++){
                                                    var auxLis = new String(lista[x][1]);
                                                    auxLis = auxLis.substring(0,des.length);
                                                    if(auxLis == des){
                                                        var selRow = eval("document.all." + tablaUsoViviendas.id + "_Row" + tablaUsoViviendas.selectedIndex);
                                                        selRow.bgColor = TB_Fondo;
                                                        tablaUsoViviendas.selectedIndex = x;
                                                        selRow = eval("document.all." + tablaUsoViviendas.id + "_Row" + x);
                                                        selRow.bgColor = TB_FondoActivo;
                                                        scrollControlTable(tablaUsoViviendas);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        
                                        function recuperaDatos(lista2) {
                                            limpiarInputs();
                                            lista = lista2;
                                            tablaUsoViviendas.lineas=lista;
                                            refresca(tablaUsoViviendas);
                                        }
                                        
    </script>
</head>

<body class="bandaBody" onload="inicializar();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        

<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <input  type="hidden"  name="codigoAntiguo" value="">
    
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantUsoViviend")%></div>
    <div class="contenidoPantalla">
        <table >
            <tr> 
                <td id="tabla"> 
                 </td>
            </tr>
            <tr> 
                <td> 
                    <input type="text" class="inputTextoObligatorio" id="obligatorio"  
                           name="txtCodigo" maxlength="5" 
                           style="width:13%" onblur="return xAMayusculas(this);">
                    <input name="txtDescripcion" type="text" class="inputTextoObligatorio" id="obligatorio" 
                           style="width:86%" maxlength="100" onblur="return xAMayusculas(this);">
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
                   name="cmdLimpiar" onClick="pulsarLimpiar();" accesskey="L"> 
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                   name="cmdSalir" onClick="pulsarSalir();" accesskey="S"> 
        </div>                                               
    </div>                                               
</form>
<script type="text/javascript">
    var tablaUsoViviendas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));
    tablaUsoViviendas.addColumna("120","center",'<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
    tablaUsoViviendas.addColumna("780","left",'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');

    tablaUsoViviendas.displayCabecera=true;
    tablaUsoViviendas.displayTabla();

    function refresca(tabla){
        tabla.displayTabla();
    }

    function rellenarDatos(tableName,rowID){
        var i = rowID;
        limpiarInputs();
        if((i>=0)&&!tableName.ultimoTable){
            var vector = [document.forms[0].txtCodigo];
            deshabilitarGeneral(vector);
            document.forms[0].txtCodigo.value = lista[i][0];
            document.forms[0].txtDescripcion.value = lista[i][1];
            document.forms[0].codigoAntiguo.value = lista[i][0];
        }
    } 

    document.onmouseup = checkKeys; 

    function checkKeysLocal(evento,tecla){
        var teclaAuxiliar = "";
        if(window.event) {
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;
        keyDel(evento);
        if (teclaAuxiliar == 38) upDownTable(tablaUsoViviendas,lista,teclaAuxiliar);
        if (teclaAuxiliar == 40) upDownTable(tablaUsoViviendas,lista,teclaAuxiliar);
        if (teclaAuxiliar == 13) pushEnterTable(tablaUsoViviendas,lista);
    }
    </script>
</body>
</html>
