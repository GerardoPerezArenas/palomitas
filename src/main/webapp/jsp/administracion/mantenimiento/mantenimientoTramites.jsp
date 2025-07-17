<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Tipos de Tramites</title>
        
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
        %>
        
        <!-- Estilos -->
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript">
        // VARIABLES GLOBALES
        var lista = new Array();
        var listaOriginal = new Array();
        var datosTramites = new Array();
        var cod_idioma = new Array();
        var desc_idioma = new Array();

        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
        function inicializar(){
            window.focus();
            cargaTablaTramites();
        }

        function cargaTablaTramites(){
<%
        MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
        Vector listaTramites = bForm.getListaTramites();
        int lengthTramites = listaTramites.size();
        int i = 0;
%>
    var j=0;
<%for (i = 0; i < lengthTramites; i++) {
            GeneralValueObject tramites = (GeneralValueObject) listaTramites.get(i);%>
                datosTramites[j] = ['<%=(String) tramites.getAtributo("codigo")%>',
                    '<%=(String) tramites.getAtributo("descripcion")%>'];
                    listaOriginal[j] = ['<%=(String) tramites.getAtributo("codigo")%>',
                        '<%=(String) tramites.getAtributo("codCampo")%>',
                        '<%=(String) tramites.getAtributo("idioma")%>',
                        '<%=(String) tramites.getAtributo("descripcion")%>',
                        '<%=(String) tramites.getAtributo("descIdioma")%>'];
                        lista[j] = datosTramites[j];
                        j++;
                        <%}%>
                        tablaTramites.lineas = lista;
                        refresca(tablaTramites);
                    }

                    // FUNCIONES DE LIMPIEZA DE CAMPOS
                    function limpiarFormulario(){
                        tablaTramites.lineas = new Array();
                        refresca(tablaTramites);
                        limpiarInputs();
                    }

                    function limpiarInputs(){
                        document.forms[0].Codigo.value = '';
                        document.forms[0].Descripcion.value = '';
                        var vector = [document.forms[0].Codigo];
                        habilitarGeneral(vector);
                    }

                    function limpiar() {
                        limpiarInputs();
                        tablaTramites.selectLinea(-1);
                    }

                    // FUNCIONES DE PULSACION DE BOTONES
                    function pulsarSalir(){
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
                    }

                    function pulsarEliminar() {
                        if(tablaTramites.selectedIndex != -1) {
                            if(jsp_alerta("C",'<%=descriptor.getDescripcion("desEliminarTipTram")%>') ==1) {
                                var vector = [document.forms[0].Codigo];
                                habilitarGeneral(vector);
                                document.forms[0].identificador.value = lista[tablaTramites.selectedIndex][0];
                                document.forms[0].codCampoAntiguo.value = document.forms[0].codCampo.value;
                                document.forms[0].codIdiomaAntiguo.value = document.forms[0].codIdioma.value;
                                document.forms[0].opcion.value = 'eliminar';
                                document.forms[0].target = "oculto";
                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Tramites.do';
                                document.forms[0].submit();
                            }
                        }
                        else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                        }

                        function pulsarModificar(){
                            var cod = document.forms[0].Codigo.value;
                            var yaExiste = 0;
                            if(tablaTramites.selectedIndex != -1){
                                if(validarFormulario()){
                                    for(l=0; l < lista.length; l++){
                                        var lineaSeleccionada;
                                        lineaSeleccionada = tablaTramites.selectedIndex;
                                        if(l == lineaSeleccionada) {
                                            l= l;
                                        } else {
                                        if((lista[l][0]) == cod){
                                            yaExiste = 1;
                                        }
                                    }
                                }
                                if(yaExiste == 0) {
                                    var vector = [document.forms[0].Codigo];
                                    habilitarGeneral(vector);
                                    document.forms[0].identificador.value = lista[tablaTramites.selectedIndex][0]; 
                                    document.forms[0].codCampoAntiguo.value = document.forms[0].codCampo.value;
                                    document.forms[0].codIdiomaAntiguo.value = document.forms[0].codIdioma.value;
                                    document.forms[0].opcion.value = 'modificar';
                                    document.forms[0].target = "oculto";
                                    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Tramites.do';
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
                                        var vector = [document.forms[0].Codigo];
                                        habilitarGeneral(vector);
                                        document.forms[0].opcion.value = 'alta';
                                        document.forms[0].target = "oculto";
                                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Tramites.do';
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
                                                if(x != tablaTramites.selectedIndex)
                                                    tablaTramites.selectLinea(x);
                                                break;
                                            }
                                        }
                                    }
                                }

                                function recuperaDatos(lista1,lista2) {
                                    limpiarInputs();
                                    lista = lista1;
                                    listaOriginal = lista2;
                                    tablaTramites.lineas=lista;
                                    refresca(tablaTramites);
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
        <input  type="hidden"  name="codCampoAntiguo" id="codCampoAntiguo">
        <input  type="hidden"  name="codIdiomaAntiguo" id="codIdiomaAntiguo">

        <input  type="hidden"  name="codCampo" value="NOM">
        <input  type="hidden"  name="codIdioma" value="1">
        <input  type="hidden"  name="descIdioma" value="">
            
        <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantTramites")%></div>
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
                               name="Codigo" maxlength="3" style="width:13%" 
                               onkeyup="return SoloDigitosNumericos(this);">
                        <input name="Descripcion" type="text" class="inputTextoObligatorio" id="obligatorio"
                               maxlength="80" style="width:86%" 
                               onblur="return xAMayusculas(this);" onKeyUp="buscar(tablaTramites);">
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
                       name="cmdLimpiar" onClick="limpiar();" accesskey="L">
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                       name="cmdSalir" onClick="pulsarSalir();" accesskey="S"> 
            </div>
        </div>
    </form>
<script type="text/javascript">
            var tablaTramites = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tablaTramites.addColumna('120','center','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
                tablaTramites.addColumna('780','left','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
                    tablaTramites.displayCabecera=true;
                    tablaTramites.displayTabla();
                    
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

        if (teclaAuxiliar == 38) upDownTable(tablaTramites,lista,teclaAuxiliar);
        if (teclaAuxiliar == 40) upDownTable(tablaTramites,lista,teclaAuxiliar);
        if (teclaAuxiliar == 13) pushEnterTable(tablaTramites,lista);
                    }
                    
        </script>
        
    </body>
</html>
