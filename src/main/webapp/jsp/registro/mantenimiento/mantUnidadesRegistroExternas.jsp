<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.mantenimiento.MantRegistroExternoForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.util.ElementoListaValueObject"%>
<%@page import="java.util.Vector"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Mantenimiento de Unidades de Registro Externas</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        
<script type="text/javascript">
// VARIABLES GLOBALES
var lista = new Array();
var listaOriginal = new Array();
var codOrganizaciones = new Array();
var descOrganizaciones = new Array();
var datosUnidadesRegistro = new Array();
var vectorCamposRejilla = ['codUnidadRegistro','desUnidadRegistro','organizacionUnidadRegistro'];
var vectorBotones = new Array();
var vectorCamposRejilla1 = new Array();
var vectorBotonesBusqueda = new Array();

// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    window.focus();
    recuperaDatosIniciales();
    vectorBotones = [document.forms[0].botonAlta,document.forms[0].botonModificar,
        document.forms[0].botonBorrar,document.forms[0].botonLimpiar];
    vectorCamposRejilla1 = [document.forms[0].codUnidadRegistro,document.forms[0].desUnidadRegistro,
        document.forms[0].organizacionUnidadRegistro];
    vectorBotonesBusqueda = document.getElementsByName("botonOrganizacion");
    pulsarCancelarBuscar();

}

function recuperaDatosIniciales(){
    <%
    MantRegistroExternoForm bForm = (MantRegistroExternoForm) session.getAttribute("MantRegistroExternoForm");
    Vector listaOrganizaciones = bForm.getListaOrganizacionesExternas();
    if (listaOrganizaciones == null) {
        listaOrganizaciones = new Vector();
    }
    int lengthOrganizaciones = listaOrganizaciones.size();
    String cods = "";
    String descs = "";
    if (lengthOrganizaciones > 0) {
        ElementoListaValueObject organizaciones;
        for (int i = 0; i < lengthOrganizaciones - 1; i++) {
            organizaciones = (ElementoListaValueObject) listaOrganizaciones.get(i);
            cods += "\"" + organizaciones.getCodigo() + "\",";
            descs += "\"" + organizaciones.getDescripcion() + "\",";
        }
        organizaciones = (ElementoListaValueObject) listaOrganizaciones.get(lengthOrganizaciones - 1);
        cods += "\"" + organizaciones.getCodigo() + "\"";
        descs += "\"" + organizaciones.getDescripcion() + "\"";
    }
             %>
    codOrganizaciones = [<%=cods%>];
    descOrganizaciones = [<%=descs%>];			    
    comboOrganizacion.addItems(codOrganizaciones,descOrganizaciones);			
}

// FUNCIONES DE LIMPIEZA DE CAMPOS
function limpiarFormulario(){
    tablaUnidadesRegistro.lineas = new Array();
    refresca(tablaUnidadesRegistro);
    limpiarCamposRejilla();
}

function limpiarCamposRejilla(){
    limpiar(vectorCamposRejilla);
    habilitarGeneral([document.forms[0].codUnidadRegistro]);
}

// FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO

function noEsta(indice){
    var cod = document.forms[0].codUnidadRegistro.value;
    for(i=0;(i<lista.length);i++){
        if(i!=indice){
            if((lista[i][0]) == cod) {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                    return false;
            }
        }
    }
    return true;
}

function validarCamposBusqueda(){
    var codOrg = document.forms[0].codOrganizacion.value;
    if(codOrg!="")
        return true;
    return false;
}

function validarCamposRejilla(){
    var codUR = document.forms[0].codUnidadRegistro.value;
    var descUR = document.forms[0].desUnidadRegistro.value;
    if((codUR!="")&&(descUR!=""))
        return true;
    return false;
}

    // FUNCIONES DE PULSACION DE BOTONES
function pulsarBuscar(){
    var botonBuscar = [document.forms[0].botonBuscar];
    if(validarCamposBusqueda()){
        document.forms[0].idOrganizacionExterna.value = document.forms[0].codOrganizacion.value;
        document.forms[0].opcion.value="cargarUnidadesRegistroExternas";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/registro/mantenimiento/MantRegistroExterno.do";
        document.forms[0].submit();
        deshabilitarGeneral(botonBuscar);
        comboOrganizacion.deactivate();
        habilitarGeneral(vectorBotones);
        habilitarGeneral(vectorCamposRejilla1);
        deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,true);
    }else
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>')
}

function pulsarCancelarBuscar(){
    limpiarFormulario();
    habilitarGeneral([document.forms[0].botonBuscar]);
    comboOrganizacion.activate();
    deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,false);
    deshabilitarGeneral(vectorCamposRejilla1);
    deshabilitarGeneral(vectorBotones);
}

function pulsarSalir(){
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
    document.forms[0].submit();
}

function pulsarAlta(){
    if (validarCamposRejilla()){
        if(noEsta()){
            document.forms[0].idOrganizacionExterna.value = document.forms[0].codOrganizacion.value;
            document.forms[0].organizacionUnidadRegistro.value = document.forms[0].codOrganizacion.value;
            document.forms[0].opcion.value = 'altaUnidadRegistroExterna';
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/registro/mantenimiento/MantRegistroExterno.do';
            document.forms[0].submit();
            limpiarCamposRejilla();
        }
    }else
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
}

function pulsarEliminar() {
    if((tablaUnidadesRegistro.selectedIndex != -1)  && !tablaUnidadesRegistro.ultimoTable ) {
        if (jsp_alerta("C",'<%=descriptor.getDescripcion("msjElimUniRegExt")%>')==1) {
            document.forms[0].idOrganizacionExterna.value = document.forms[0].codOrganizacion.value;
            document.forms[0].idUnidadRegistro.value = document.forms[0].codUnidadRegistro.value;
            document.forms[0].organizacionUnidadRegistro.value = document.forms[0].codOrganizacion.value;
            document.forms[0].opcion.value = 'eliminarUnidadRegistroExterna';
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/registro/mantenimiento/MantRegistroExterno.do';
            document.forms[0].submit();
            limpiarCamposRejilla();
        }
    }else 
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarModificar(){
    var cod = document.forms[0].codUnidadRegistro.value;
    var yaExiste = 0;
    if((tablaUnidadesRegistro.selectedIndex != -1)  && !tablaUnidadesRegistro.ultimoTable ) {
        if(validarCamposRejilla()){			
            for(l=0; l < lista.length; l++){
                var lineaSeleccionada;
                lineaSeleccionada = tablaUnidadesRegistro.selectedIndex;
                if(l == lineaSeleccionada) {
                    l= l;
                } else {
                if ((lista[l][0]) == cod ){
                    yaExiste = 1;
                }
            }
        }
    if(yaExiste == 0) {
        document.forms[0].idOrganizacionExterna.value = document.forms[0].codOrganizacion.value;
        document.forms[0].idUnidadRegistro.value = document.forms[0].codUnidadRegistro.value;
        document.forms[0].organizacionUnidadRegistro.value = document.forms[0].codOrganizacion.value;
        document.forms[0].opcion.value = 'modificarUnidadRegistroExterna';
        document.forms[0].target = "oculto";
        document.forms[0].action = '<%=request.getContextPath()%>/registro/mantenimiento/MantRegistroExterno.do';
        document.forms[0].submit();

    } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
    }
    }else
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
    } else
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarLimpiar(){
    limpiarCamposRejilla();
    tablaUnidadesRegistro.selectLinea(-1);
}


function recuperaDatos(lista1,lista2) {
    limpiarCamposRejilla();
    listaOriginal = lista1;
    lista = lista2;
    tablaUnidadesRegistro.lineas=lista;
    refresca(tablaUnidadesRegistro);
}


function recuperaDatosCompleto(resultado,lista1,lista2, codOrg, desOrg) {
    limpiarCamposRejilla();
    listaOriginal = lista1;
    lista = lista2;
    tablaUnidadesRegistro.lineas=lista;
    refresca(tablaUnidadesRegistro);
    comboOrganizacion.addItems(codOrg,desOrg);	  
    comboOrganizacion.buscaCodigo(document.forms[0].codOrganizacion.value);

    if (resultado == 'modificada' || resultado=='eliminada' || resultado=='insertada') {
        limpiarCamposRejilla();
    } else {
    if (resultado=='no insertada')
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoAltaUniRegExt")%>');
            else if (resultado=='no modificada')
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoModUniRegExt")%>');
                    else if (resultado=='no eliminada')
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoElimUniRegExt")%>');
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
    <input  type="hidden"  name="idUnidadRegistro" value="">
    <input  type="hidden"  name="idOrganizacionExterna">
    <input name="organizacionUnidadRegistro" type="hidden"> 
            
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("titMantUniRegExt")%></div>
    <div class="contenidoPantalla">
        <table>
            <TR>
                <td style="width:70%" class="etiqueta">
                    <%=descriptor.getDescripcion("gEtiq_Organizacion")%>
                    <input class="inputTextoObligatorio" type="text" id="codOrganizacion" name="codOrganizacion"
                           style="width:10%" maxlength="3" onkeyup="return SoloDigitosNumericos(this);">
                    <input id="descOrganizacion" name="descOrganizacion" type="text" class="inputTextoObligatorio" 
                           style="width:70%" readonly>
                    <a id="anchorOrganizacion" name="anchorOrganizacion" href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOrganizacion" name="botonOrganizacion"
                             style="cursor:hand;"></span>
                    </a>
                </td>
                <td style="width: 26%;text-align:right">
                                <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                       value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                       onClick="pulsarBuscar();" accesskey="B">
                                <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                       value='<%=descriptor.getDescripcion("gbCancelar")%>'
                                       onClick="pulsarCancelarBuscar();" accesskey="C">
                </td>
                </tr>
                <tr> 
                    <td id="tabla" colspan="2"></td>
                </tr>
                <tr> 
                    <td colspan="2">
                        <input type="text" class="inputTextoObligatorio" style="width:13%"
                               name="codUnidadRegistro" maxlength="3" 
                               onkeyup="return SoloDigitosNumericos(this);">
                        <input name="desUnidadRegistro"  type="text" class="inputTextoObligatorio" 
                               maxlength="100" style="width:86%" onblur="return xAMayusculas(this);">
                    </td>
                </tr>
            </TABLE>								
            <div class="botoneraPrincipal">
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> 
                                           name="botonAlta" onClick="pulsarAlta();" accesskey="A"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
                                           name="botonModificar" onClick="pulsarModificar();" accesskey="M"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> 
                                           name="botonBorrar" onClick="pulsarEliminar();" accesskey="E"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> 
                                           name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                                           name="botonSalir" onClick="pulsarSalir();" accesskey="S">  
            </div>                        
    </div>                        
</form>
        
        <script type="text/javascript">
            
            var comboOrganizacion  = new Combo("Organizacion");
            
            var tablaUnidadesRegistro = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            tablaUnidadesRegistro.addColumna('120','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
                tablaUnidadesRegistro.addColumna('780',null,'<%= descriptor.getDescripcion("gEtiq_desc")%>');
                    tablaUnidadesRegistro.displayCabecera = true;
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    function rellenarDatos(tableName,rowID){
                        if(tableName==tablaUnidadesRegistro){
                            var i = rowID;
                            limpiarCamposRejilla();
                            if((i>=0)&&!tableName.ultimoTable){
                                rellenar(listaOriginal[i],vectorCamposRejilla);
                                document.forms[0].idUnidadRegistro.value = lista[i][0];
                                deshabilitarGeneral([document.forms[0].codUnidadRegistro]);
                            }
                        }
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

                        if(tecla == 40){
                            upDownTable(tablaUnidadesRegistro,tecla);
                        }
                        if(tecla == 38){
                            upDownTable(tablaUnidadesRegistro,lista,tecla);
                        }  
                        if (tecla == 1) if (comboOrganizacion.base.style.visibility == "visible" && isClickOutCombo(comboOrganizacion,coordx,coordy)) setTimeout('comboOrganizacion.ocultar()',20);

                        if (tecla == 9) comboOrganizacion.ocultar();
                    }
                    
        </script>
        
    </body>
</html>
