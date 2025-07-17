<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Valores de los Campos Desplegables</title>
        
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
            
            ArrayList listaIdiomas = (ArrayList) session.getAttribute("listaIdiomas");
            String descIdioma = ((LabelValueTO) listaIdiomas.get(0)).getLabel();
          %>

        <!-- Estilos -->
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var lista = new Array();
            var datosValores = new Array();
            var datosValoresOriginales = new Array();
            var multiidioma = <%=listaIdiomas!= null && listaIdiomas.size() > 0%>;
            
            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            function inicializar(){
                window.focus();
                cargaTablaValores();
            }
            
            function cargaTablaValores() {
    <%
            MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
            Vector listaValores = bForm.getListaValoresCamposDesplegables();
            GeneralValueObject otrosDatos = bForm.getOtrosDatos();
            int lengthValores = listaValores.size();
            int i = 0;
            String nombreCampo = (String) otrosDatos.getAtributo("descCampo");
            String campo = (String) otrosDatos.getAtributo("campo");
    %>
        document.forms[0].campo.value='<%=campo%>';
        var j=0;
        var estadoValor;
    <%    for (i = 0; i < lengthValores; i++) {
                GeneralValueObject Valor = (GeneralValueObject) listaValores.get(i);%>
                
                var codigoCampo = '<%=(String) Valor.getAtributo("codigoCampo")%>';
                var codigoValor = '<%=(String) Valor.getAtributo("codigoValor")%>';
                var descripcionValor = '<%=(String) Valor.getAtributo("descripcionValor")%>';
                
				if('<%=(String) Valor.getAtributo("estadoValor")%>'=='A'){
                        estadoValor='<span class="fa fa-check 2x"></span>';
                    }else{
                        estadoValor='<span class="fa fa-close 2x"></span>';
                    } 
                if (multiidioma){
                    var descripciones = "";
                    descripciones = descripcionValor.split("|");
                    var descId1 = descripciones[0];
                    var descId2 = "";
                    if (descripcionValor.indexOf("|") != -1){
                        descId2 = descripciones[1];
                    }
                    datosValores[j] = [codigoValor,descId1,descId2,estadoValor];
                    datosValoresOriginales[j] = [codigoCampo,codigoValor,descId1,descId2, '<%=(String) Valor.getAtributo("estadoValor")%>'];
               } else {
                   datosValores[j] = [codigoValor,descripcionValor,estadoValor];
                   datosValoresOriginales[j] = [codigoCampo,codigoValor,descripcionValor, '<%=(String) Valor.getAtributo("estadoValor")%>'];
               }
               j++;
               <%}%>
                   lista = datosValores;
                   tablaValores.lineas = lista;
                   refresca(tablaValores);
               }
                        
                        
                        // FUNCIONES DE LIMPIEZA DE CAMPOS
                        function limpiarFormulario(){
                            tablaValores.lineas = new Array();
                            refresca(tablaValores);
                            limpiarInputs();
                        }
                        
                        function limpiarInputs(){
                            document.forms[0].Codigo.value = '';
                            document.forms[0].Descripcion.value = '';
                            if(multiidioma){
                                document.forms[0].descripcionIdiomaA.value="";
                            }
                            var vector = [document.forms[0].Codigo];
                            habilitarGeneral(vector);
                        }
                        
                        function limpiar() {
                            limpiarInputs();
                            tablaValores.selectLinea(-1);
                        }
                        
                        // FUNCIONES DE PULSACION DE BOTONES
                        function pulsarVolver(){
                            document.forms[0].opcion.value = 'cargar';
                            document.forms[0].target = "mainFrame";
                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
                            document.forms[0].submit();
                        }
                        
                        function pulsarEliminar() {
                            var vector = [document.forms[0].Codigo];
                            habilitarGeneral(vector);
                            if(tablaValores.selectedIndex != -1) {
                                document.forms[0].opcion.value = 'eliminarValor';
                                document.forms[0].target = "oculto";
                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
                                document.forms[0].submit();
                            }
                            else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                            }
                            
                            function pulsarModificar(){
                                var vector = [document.forms[0].Codigo];
                                habilitarGeneral(vector);
                                var cod = document.forms[0].Codigo.value;
                                var yaExiste = 0;
                                if(tablaValores.selectedIndex != -1){
                                    if(validarFormulario()){
                                        for(l=0; l < lista.length; l++){
                                            var lineaSeleccionada;
                                            lineaSeleccionada = tablaValores.selectedIndex;
                                            if(l == lineaSeleccionada) {
                                                l= l;
                                            } else {
                                            if ((lista[l][0]) == cod ){
                                                yaExiste = 1;
                                            }
                                        }
                                    }
                                    if(yaExiste == 0) {
                                        document.forms[0].identificador.value = datosValoresOriginales[tablaValores.selectedIndex][1];
                                        document.forms[0].opcion.value = 'modificarValor';
                                        document.forms[0].target = "oculto";
                                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
                                        document.forms[0].submit();
                                    } else {
                                    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                    }
                                }
                            }
                            else
                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                                }
                                
                                function pulsarAlta(){
                                    var cod = document.forms[0].Codigo.value;
                                    var existe = 0;
                                    if (validarFormulario()){
                                        for(var i=0; (i < lista.length) && (existe == 0); i++){
                                            if((lista[i][0]) == cod)
                                                existe = 1;
                                        }
                                        if(existe == 0){
                                            document.forms[0].opcion.value = 'altaValor';
                                            document.forms[0].target = "oculto";
                                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
                                            document.forms[0].submit();
                                        }
                                        else
                                            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                            }
                                        }
                                        
                                        function buscar(tabla){
                                            var auxDes = "";
                                            if((event.keyCode != 40)&&(event.keyCode != 38)){
                                                if(event.keyCode != 13){
                                                    auxDes = document.forms[0].Descripcion.value;
                                                    if(event.keyCode == 8){
                                                        if(auxDes.length == 0){
                                                            limpiarInputs();
                                                        }
                                                    }
                                                    selecFila(auxDes);
                                                }else{
                                                if((tabla.selectedIndex>-1)&&(tabla.selectedIndex < lista.length)){
                                                    pintaDatos(tabla.getLinea());
                                                }
                                            }
                                        }
                                    }
                                    
                                    function pintaDatos(datos){
                                        document.forms[0].Codigo.value = datos[0];
                                        document.forms[0].Descripcion.value = datos[1];
                                    }
                                    
                                    
                                    function selecFila(des){
                                        if(des.length != 0){
                                            for (var x=0; x<lista.length; x++){
                                                var auxLis = new String(lista[x][1]);
                                                auxLis = auxLis.substring(0,des.length);
                                                if(auxLis == des){
                                                    if(x!=tablaValores.selectedIndex) tablaValores.selectLinea(x);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    
                                    function recuperaDatos(lista2, lista2Original) {
                                        limpiarInputs();
                                        datosValores = lista2;
                                        datosValoresOriginales = lista2Original;
                                        lista = lista2;
                                        tablaValores.lineas=lista;
                                        refresca(tablaValores);
                                    }
                                    
				function pulsarRecuperar(){
                                        var vector = [document.forms[0].Codigo];
                                        habilitarGeneral(vector);
                                        if(tablaValores.selectedIndex != -1) {
                                            if(datosValoresOriginales[tablaValores.selectedIndex][datosValoresOriginales[tablaValores.selectedIndex].length -1]=='B'){
                                                document.forms[0].opcion.value = 'recuperarValor';
                                                document.forms[0].target = "oculto";
                                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
                                                document.forms[0].submit();
                                            }else{
                                                jsp_alerta('A','<%=descriptor.getDescripcion("msjNoRecuperaValor")%>');
                                            }
                                       }else {
                                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                                       }   
                            
                                    }
        </script>
    </head>
    
    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <input  type="hidden"  name="campo" id="campo">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gbValores")%> <%= nombreCampo%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <td>
                    <input type="text" class="inputTextoObligatorio" id="obligatorio"
                                          name="Codigo" maxlength="8" style="width:125px"
                                          onkeyup="return xAMayusculas(this);">
                    
                    <% if(listaIdiomas != null && listaIdiomas.size()>0){%>
                    <input name="Descripcion" type="text" class="inputTextoObligatorio" id="obligatorio" maxlength="250"
                           onblur="return xAMayusculas(this)" size="50">
                    <input name="descripcionIdiomaA" type="text" class="inputTexto" id="descripcionIdiomaA" maxlength="250"
                           onblur="return xAMayusculas(this)" size="50">
                    <%}else{%>           
                               
                    <input name="Descripcion" type="text" class="inputTextoObligatorio" id="obligatorio" 
                           maxlength="500" onblur="return xAMayusculas(this);" style="width:780px">
                    <%}%>
                </td>
            </tr>
        </TABLE>								
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>"
               name="cmdAlta" onClick="pulsarAlta();" accesskey="A"> 
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>"
               name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>"
               name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
		<input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbRecuperar")%>"
               name="cmdRecuperar" onclick="pulsarRecuperar();" accesskey="R">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>"
               name="cmdLimpiar" onClick="limpiar();" accesskey="L">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>"
               name="cmdSalir" onClick="pulsarVolver();" accesskey="S">
    </div>                        
</div>                        
</form>
<script type="text/javascript">
            var tablaValores = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tablaValores.addColumna('120','center','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
            if(multiidioma){
                 tablaValores.addColumna('390',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>'+ '<%=(idioma!=1)?(" "+descIdioma):""%>');
                 tablaValores.addColumna('390',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>'+ '<%=(idioma==1)?(" "+descIdioma):""%>');
            }else{
                tablaValores.addColumna('780',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
            }
			tablaValores.addColumna('90','center','<%= descriptor.getDescripcion("gEtiq_Estado")%>');
                    tablaValores.displayCabecera = true;
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    function rellenarDatos(tableName,rowID){
                        var i = rowID;
                        limpiarInputs();
                        var vector = [document.forms[0].Codigo];
                        deshabilitarGeneral(vector);
                        if((i>=0)&&!tableName.ultimoTable){
                            document.forms[0].Codigo.value = lista[i][0];
                            document.forms[0].Descripcion.value = lista[i][1];
                            if(multiidioma){
                                document.forms[0].descripcionIdiomaA.value=lista[i][2];
                            }
                        }
                    }
                    
                    document.onmouseup = checkKeys;
                    
                    
                    function checkKeysLocal(evento, tecla){

                        var teclaAuxiliar="";
                          if(window.event){
                            evento = window.event;
                            teclaAuxiliar =evento.keyCode;
                          }else
                                teclaAuxiliar =evento.which;

                        keyDel(evento);

                        if (teclaAuxiliar == 38) upDownTable(tablaValores,lista,teclaAuxiliar);
                        if (teclaAuxiliar == 40) upDownTable(tablaValores,lista,teclaAuxiliar);
                        if (teclaAuxiliar == 13) pushEnterTable(tablaValores,lista);
                    }
                    
        </script>
        
    </body>
</html>
