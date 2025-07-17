<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        <title> Mantenimiento de CodPostales INE </title>
        <!-- Estilos -->
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ParametrosTerceroValueObject ptVO = null;
            int idioma = 1;
            int apl = 3;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
        <script type="text/javascript" src="<c:url value='/scripts/formateador.js'/>"></script>
        <script type="text/javascript">
            // VARIABLES GLOBALES
                 <%
            MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
    %>  
        var ventana = "<%=mantForm.getVentana()%>";
        var codProvincias = new Array();
        var descProvincias = new Array();
        var codMunicipios = new Array();
        var descMunicipios = new Array();
        var codMunicipiosDefecto = new Array();
        var descMunicipiosDefecto = new Array();
        var listaCodPostalesOriginal = new Array();
        var listaCodPostales = new Array();
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
        function recuperaDatosIniciales(){
    <%
            Vector listaProvincias = mantForm.getListaProvincias();
            Vector listaMunicipios = mantForm.getListaMunicipios();
            int lengthProvs = listaProvincias.size();
            int lengthMuns = listaMunicipios.size();
            int i = 0;
    %>
        var j=0;
        <%for (i = 0; i < lengthProvs; i++) {%>
        codProvincias[j] = "<%=(String) ((GeneralValueObject) listaProvincias.get(i)).getAtributo("codigo")%>";
        descProvincias[j] = "<%=(String) ((GeneralValueObject) listaProvincias.get(i)).getAtributo("descripcion")%>";
        j++;
        <%}%>
        j=0;
        <%for (i = 0; i < lengthMuns; i++) {%>
        codMunicipios[j] = "<%=(String) ((GeneralValueObject) listaMunicipios.get(i)).getAtributo("codMunicipio")%>";
        descMunicipios[j] = "<%=(String) ((GeneralValueObject) listaMunicipios.get(i)).getAtributo("nombreOficial")%>";
        j++;
        <%}%>
        codMunicipiosDefecto = codMunicipios;
        descMunicipiosDefecto = descMunicipios;
    }
    
    function inicializar(){
        window.focus();
        recuperaDatosIniciales();
        valoresPorDefecto();
        pulsarCancelarBuscar();
        if(ventana=="false"){
            pleaseWait1("off",top.mainFrame);
            
                
            
            pulsarBuscar();
        }else{
        pleaseWait1("off",top1.mainFrame1);
        var parametros = self.parent.opener.xanelaAuxiliarArgs;
        rellenarCamposBusqueda(parametros);
        pulsarBuscar();
    }
}

// FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
//var vectorCamposBusqueda = ["codProvincia","descProvincia","codMunicipio","descMunicipio"];
var vectorCamposRejilla = ["codPostal","defecto"];
//var vectorCombosBusqueda = ["comboProvincia","comboMunicipio"];
var vectorBotones = ["botonAlta","botonModificar","botonBorrar","botonLimpiar"];

/*function habilitarCamposBusqueda(habilitar){
habilitarGeneralCombos(vectorCombosBusqueda,habilitar);
}*/

function habilitarCamposRejilla(habilitar){
    habilitarGeneralInputs(vectorCamposRejilla,habilitar);
    habilitarGeneralInputs(vectorBotones,habilitar);
}

function limpiarFormulario(){
    //limpiarCamposBusqueda();
    limpiarCamposRejilla();
    tablaCodPostales.lineas = new Array();
    refresca(tablaCodPostales);
}

/*function limpiarCamposBusqueda(){
limpiar(vectorCamposBusqueda);
}*/

function limpiarCamposRejilla(){
    document.forms[0].codPostal.value = "";
    document.forms[0].defecto.checked = false;
    limpiar(vectorCamposRejilla);
}

// FUNCIONES DE CARGA DE DATOS DINAMICA
function cargarListaMunicipios(){	
    document.forms[0].opcion.value="cargarMunicipios";
    document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
    document.forms[0].submit();
}

function cargarListaCodPostales(lista){
    listaCodPostales = lista;
    tablaCodPostales.lineas = lista;
    refresca(tablaCodPostales);
}

function valoresPorDefecto(){
    document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
    document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
    document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
    document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
    document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
    codMunicipios = codMunicipiosDefecto;
    descMunicipios = descMunicipiosDefecto;
    comboProvincia.addItems(codProvincias,descProvincias);
    //comboMunicipio.addItems(codMunicipios,descMunicipios);
}

function rellenarCamposBusqueda(datos){
    rellenar(datos,vectorCamposBusqueda);
}


// FUNCIONES DE VALIDACION DE CAMPOS
function validarCamposBusqueda(){
    var pais = document.forms[0].codPais.value;
    var provincia = document.forms[0].codProvincia.value;
    var municipio = document.forms[0].codMunicipio.value;
    if((pais!="")&&(provincia!="")&&(municipio!=""))
        return true;
    return false;
}

function validarCamposRejilla(){
    var codPostal = document.forms[0].codPostal.value;
    if(codPostal!="")
        return true;
    return false;
}

function noEsta(indice){
    var cod = document.forms[0].codPostal.value;
    for(i=0;(i<listaCodPostales.length);i++){
        if(i!=indice){
            if((listaCodPostales[i][0]) == cod)
                return false;
        }
    }
    return true;
}

function soloUnoPorDefecto(){
    var i=0;
    var defecto = document.forms[0].defecto.checked;
    var numDefectos = 0;
    for(i=0;i<listaCodPostalesOriginal.length;i++){
        if(listaCodPostalesOriginal[i][1]=="1")
            numDefectos++;
    }
    if((numDefectos==1)&& defecto)
        return false;
    else
        return true;
}

// FUNCIONES DE PULSACION DE BOTONES
function pulsarBuscar(){
    //var botonBuscar = ["botonBuscar"];
    if(validarCamposBusqueda()){
        document.forms[0].opcion.value="cargarCodPostales";
        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/CodPostales.do";
        document.forms[0].submit();
        //habilitarGeneralInputs(botonBuscar,false);
        //habilitarCamposBusqueda(false);
        habilitarCamposRejilla(true);
    }else
    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
    }
    
function pulsarAlta(){
    if(validaProvinciaCP()){
        if(validarCamposRejilla()){
            if(noEsta()){
                if(soloUnoPorDefecto()){
                    //habilitarCamposBusqueda(true);
                    var defecto = document.forms[0].defecto.checked;
                    document.forms[0].defecto1.value = defecto?"1":"0";
                    document.forms[0].opcion.value="alta";
                    document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/CodPostales.do";
                    document.forms[0].submit();
                    //habilitarCamposBusqueda(false);
                    limpiarCamposRejilla();
                }else
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjUnCodxDefecto")%>");
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
    }
}// pulsarAlta

function validaProvinciaCP(){

var provincia =  document.forms[0].codProvincia;
var codigoPostal =  document.forms[0].codPostal;
if(provincia.value.length == 1)document.forms[0].codProvincia.value="0"+provincia.value;
    if (provincia.value == '66'|| provincia.value == '99'){
        return true;
    }else {
        if(codigoPostal.value.length!=5){
            jsp_alerta("A","El código postal no tiene la longitud correcta");
            return false;
        }
        if(provincia.value == codigoPostal.value.substring(0,2))
            return true;
        else{
            jsp_alerta("A","El código postal no pertenece a la provincia seleccionada");
            codigoPostal.select();

            return false;
        }
    }
}



function provinciaSelec(){
    var codProvincia = document.forms[0].codProvincia.value;
    var codigoPostal =  document.forms[0].codPostal;
    var cod;
    var codPostal;
    var longitudCuatroDigitos = false;
      if (codigoPostal.value != "" ){
        if(codigoPostal.value.substring(0,1)!= 0){
            codPostal = codigoPostal.value.substring(0,2);
        }else{
            cod = trimLeftZeroes(codigoPostal.value);
            if(cod.length == 4) longitudCuatroDigitos = true;
            codPostal = cod.substring(0,1);
        }
        comboProvincia.buscaCodigo(codPostal);
        if(longitudCuatroDigitos){
            document.forms[0].codProvincia.value="0"+codPostal;
        }
    }
}


function eliminaCero(){
    var codProvincia = document.forms[0].codProvincia.value;
    var trimCP;
    if (codProvincia.substring(0,1)== 0){ 
       trimCP = trimLeftZeroes(codProvincia) ;
       document.forms[0].codProvincia.value = trimCP ;
    }
}

            function pulsarModificar(){
                if(validaProvinciaCP()){
                    if(tablaCodPostales.selectedIndex != -1) {
                        if(validarCamposRejilla()){
                            if(noEsta(tablaCodPostales.selectedIndex)){
                                if(soloUnoPorDefecto()){
                                    //habilitarCamposBusqueda(true);
                                    var defecto = document.forms[0].defecto.checked;
                                    document.forms[0].defecto1.value = defecto?"1":"0";
                                    document.forms[0].opcion.value="modificarCodPostalDesdeMantenimiento";
                                    document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/CodPostales.do";
                                    document.forms[0].submit();
                                    //habilitarCamposBusqueda(false);
                                    limpiarCamposRejilla();
                                    if(tablaCodPostales.selectedIndex != -1 ) {
                                        tablaCodPostales.selectLinea(tablaCodPostales.selectedIndex);
                                        tablaCodPostales.selectedIndex = -1;
                                    }
                                }else
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msjUnCodxDefecto")%>");
                        }else
                            jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                    }else
                        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                } else 
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                }
             }
                            
                            function noModificarCodPostales() {
                                jsp_alerta("A","<%=descriptor.getDescripcion("msjNoModifCodPost")%>");
                                }
                                
                                function pulsarBorrar(){
                                    if(tablaCodPostales.selectedIndex != -1) {
                                        if(validarCamposBusqueda()){
                                            if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimCodPost")%>') ==1) {
                                                //habilitarCamposBusqueda(true);
                                                document.forms[0].opcion.value="eliminarCodPostalDesdeMantenimiento";
                                                document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/CodPostales.do";
                                                document.forms[0].submit();
                                                //habilitarCamposBusqueda(false);
                                                limpiarCamposRejilla();
                                                if(tablaCodPostales.selectedIndex != -1 ) {
                                                    tablaCodPostales.selectLinea(tablaCodPostales.selectedIndex);
                                                    tablaCodPostales.selectedIndex = -1;
                                                }
                                            }
                                        }else
                                        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                                        } else {
                                        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                                        }
                                    }
                                    
                                    function noEliminarCodPostales() {
                                        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimCodPost")%>");
                                        }
                                        
                                        function pulsarCancelarBuscar(){
                                            limpiarFormulario();
                                            //var botonBuscar = ["botonBuscar"];
                                            //habilitarGeneralInputs(botonBuscar,true);
                                            //habilitarCamposBusqueda(true);
                                            habilitarCamposRejilla(false);
                                            valoresPorDefecto();
                                        }
                                        
                                        function pulsarLimpiar(){
                                            limpiarCamposRejilla();
                                            if(tablaCodPostales.selectedIndex != -1 ) {
                                                tablaCodPostales.selectLinea(tablaCodPostales.selectedIndex);
                                                tablaCodPostales.selectedIndex = -1;
                                            }
                                        }
                                        
                                        function pulsarSalir(){
                                            if(ventana=="false"){
                                                document.forms[0].opcion.value="inicializarTerc";
                                                document.forms[0].target="mainFrame";
                                                document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                document.forms[0].submit();
                                            }else{
                                            var datosRetorno;
                                            if(indice>-1)
                                                datosRetorno = listaCodPostalesOriginal[indice];
                                            self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
                                        }
                                    }
    </script>
</head>
<body class="bandaBody" onLoad="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form action="" method="get" name="formulario" target="_self">
    <input type="hidden" name="opcion">
    <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
    <input type="hidden" name="codPostalAntiguo" value="">
    <input type="hidden" name="defecto1" value="">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
    <input type="hidden" name="descProvincia" value="<%=ptVO.getNomProvincia()%>">-->
    <input type="hidden" name="codMunicipio" value="<%=ptVO.getMunicipio()%>">
    <input type="hidden" name="descMunicipio" value="<%=ptVO.getNomMunicipio()%>">
            
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_ManCodPostales")%></div>
        <div class="contenidoPantalla">
            <table width="100%">
                <tr>
                    <td id="tablaCodPostales" onclick="javascript:return provinciaSelec();">
                    </td>
                </tr>
                <tr style="padding-top: 5px">
                    <td> 
                        <table width="560px" style="height: 60px">
                             <tr>
                                <td style="width:20px;"  class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                                <td style="width:350px" class="columnP">
                                    <input class="inputTexto" type="text" id="codProvincia" name="codProvincia" size="3" onkeyup="javascript:return SoloDigitosNumericos(this);" onclick="javascript:return eliminaCero();">
                                    <input class="inputTexto" type="text" id="descProvincia" name="descProvincia" size="40" onselect="javascript:return eliminaCero();" onclick="javascript:return eliminaCero();" readonly>
                                    <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                                </td>
                            </tr>
                        </table>
                        <table width="360px">
                            <tr>
                                <td style="width:140px">
                                    <input name="codPostal" type="text" class="inputTextoObligatorio" id="numero" size=10 maxlength=5 
                                           onkeyup = "return SoloDigitosNumericos(this);">
                                </td>
                                <td style="width:200px" class="etiqueta">
                                    <input name="defecto" type="checkbox" class="inputTexto" value="1">
                                    <%=descriptor.getDescripcion("gEtiq_CodxDefecto")%>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr style="padding-bottom: 10px">
                    <td></td>
                </tr>
            </table>
            <div class="botoneraPrincipal">
                <input type="button" class="botonGeneral"  name="botonAlta"	onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
                <input type="button" class="botonGeneral"  name="botonModificar" onClick="pulsarModificar();" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
                <input type="button" class="botonGeneral" name="botonBorrar" onClick="pulsarBorrar();" accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
                <input type="button" class="botonGeneral"  name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
                <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
            </div>                                     		            
        </div>                                     		            
    </form>
<script type="text/javascript">
            var indice;
            var tablaCodPostales = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaCodPostales"));
            tablaCodPostales.addColumna("180","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
            tablaCodPostales.addColumna("180","center",'<%=descriptor.getDescripcion("gEtiq_CodxDefecto")%>');
            tablaCodPostales.displayCabecera=true;
            tablaCodPostales.displayTabla();
                    
                    
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    function rellenarDatos(tableName,rowID){
                        if(tablaCodPostales==tableName){
                            var i=rowID;
                            indice = rowID;
                            limpiarCamposRejilla();
                            if(i>=0){
                                document.forms[0].codPostal.value = listaCodPostalesOriginal[i][0];
                                document.forms[0].defecto.checked=(listaCodPostalesOriginal[i][1]=="1")? true:false;
                                document.forms[0].codPostalAntiguo.value = listaCodPostalesOriginal[i][0];
                            }
                        }
                    } 
                    
                    // FUNCION DE CONTROL DE TECLAS
                    document.onmouseup = checkKeys; 
                    
                    function checkKeysLocal(evento,tecla){
                        var teclaAuxiliar = "";
                        if(window.event){
                            evento         = window.event;
                            teclaAuxiliar =  evento.keyCode;
                        }else
                            teclaAuxiliar =  evento.which;
                        
                        //** Esta funcion se debe implementar en cada JSP para particularizar  **//
                        //** las acciones a realizar de las distintas combinaciones de teclas  **//
                        keyDel(evento);
                        if(teclaAuxiliar == 40){
                            upDownTable(tablaCodPostales,listaCodPostales,teclaAuxiliar);
                        }

                        if(teclaAuxiliar == 38){
                            upDownTable(tablaCodPostales,listaCodPostales,teclaAuxiliar);
                        }  
                    }


// COMBOS
var comboProvincia = new Combo("Provincia");
                    
        </script>
    </body>
</html>
