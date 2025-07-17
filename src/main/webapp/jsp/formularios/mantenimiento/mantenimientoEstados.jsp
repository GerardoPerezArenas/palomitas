<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.formularios.mantenimiento.MantenimientosFormulariosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Mantenimiento de Estados de Formularios</title>

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
}%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<!-- Estilos -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">
var lista = new Array();
var datosEstados = new Array();
var vectorCamposRejilla = ['codigo','descripcion','codigoAntiguo'];

function inicializar(){
    window.focus();
    cargaTablaEstados();
}

function cargaTablaEstados(){
<%
    MantenimientosFormulariosForm bForm = (MantenimientosFormulariosForm) session.getAttribute("MantenimientosFormulariosForm");
    Vector listaEstados = bForm.getListaEstadosFormularios();
    int lengthEstados = listaEstados.size();
    int i = 0;
%>
var j=0;
<%for (i = 0; i < lengthEstados; i++) {
    GeneralValueObject estados = (GeneralValueObject) listaEstados.get(i);%>
    datosEstados[j] = ['<%=(String) estados.getAtributo("codigo")%>',
        '<%=(String) estados.getAtributo("descripcion")%>'];
    lista[j] = datosEstados[j];
    j++;
<%}%>
    tablaEstados.lineas = lista;
    refresca(tablaEstados);
}

// FUNCIONES DE LIMPIEZA DE CAMPOS
function limpiarFormulario(){
    tablaEstados.lineas = new Array();
    refresca(tablaEstados);
    limpiarCamposRejilla();
}

function limpiarCamposRejilla(){
    limpiar(vectorCamposRejilla);
    var vector = [document.forms[0].codigo];
    habilitarGeneral(vector);
}

function pulsarLimpiar() {
    tablaEstados.selectLinea(-1);
    limpiarCamposRejilla();
}

// FUNCIONES DE PULSACION DE BOTONES
function pulsarSalir(){
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/jsp/formularios/presentacionADM.jsp';
    document.forms[0].submit();
}

function pulsarEliminar() {
    var vector = [document.forms[0].codigo];
    habilitarGeneral(vector);
    if(filaSeleccionada(tablaEstados)) {
        document.forms[0].identificador.value = document.forms[0].codigo.value;
        document.forms[0].opcion.value = 'eliminar';
        document.forms[0].target = "oculto";
        document.forms[0].action = '<%=request.getContextPath()%>/formularios/mantenimiento/Estados.do';
        document.forms[0].submit();
        limpiarCamposRejilla();
    }else
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarModificar(){
    var vector = [document.forms[0].codigo];
    habilitarGeneral(vector);
    var cod = document.forms[0].codigo.value;
    var yaExiste = 0;
    if(filaSeleccionada(tablaEstados)){
        if(validarCamposRejilla()){
            for(l=0; l < lista.length; l++){
                var lineaSeleccionada;
                lineaSeleccionada = tablaEstados.selectedIndex;
                if(l == lineaSeleccionada) {
                    l= l;
                } else {
                    if ((lista[l][0]) == cod ){
                        yaExiste = 1;
                    }
                }
            }
            if(yaExiste == 0) {
                document.forms[0].opcion.value = 'modificar';
                document.forms[0].target = "oculto";
                document.forms[0].action = '<%=request.getContextPath()%>/formularios/mantenimiento/Estados.do';
                document.forms[0].submit();
                limpiarCamposRejilla();
            } else {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
            }
        }else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
    }
    else
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarAlta(){
    if (validarCamposRejilla()){
        if(noEsta(-1)){
            document.forms[0].opcion.value = 'alta';
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/formularios/mantenimiento/Estados.do';
            document.forms[0].submit();
            limpiarCamposRejilla();
        }
    }else
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
}

// FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
function noEsta(indice){
    var cod = document.forms[0].codigo.value;
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
    tablaEstados.lineas=lista;
    refresca(tablaEstados);
}
</script>
</head>
<body class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>

<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <input  type="hidden"  name="codigoAntiguo" value="">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("etiq_MantEstadoForm")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <td>
                    <input type="text" class="inputTextoObligatorio" id="obligatorio" style="width: 10.5%"
                           name="codigo" size="10" maxlength="3" onkeyup="return xAMayusculas(this);">
                    <input name="descripcion" type="text" class="inputTextoObligatorio" id="obligatorio" 
                           style="width: 88%" maxlength="100" onkeyup="return xAMayusculas(this);">
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
<script>
var tablaEstados = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
tablaEstados.addColumna('100','center','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
tablaEstados.addColumna('810','left','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');

tablaEstados.displayCabecera=true;
tablaEstados.displayTabla();

function refresca(tabla){
    tabla.displayTabla();
}

function rellenarDatos(tableName,rowID){
    var i = rowID;
    limpiarCamposRejilla();
    var vector = [document.forms[0].codigo];
    deshabilitarGeneral(vector);
    if((i>=0)&&!tableName.ultimoTable){
        var datosRejilla = [lista[i][0],lista[i][1],lista[i][0]];
        rellenar(datosRejilla,vectorCamposRejilla);
    }
}

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla){
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    keyDel(evento);
    if (teclaAuxiliar == 38) upDownTable(tablaEstados,lista,teclaAuxiliar);
    if (teclaAuxiliar == 40) upDownTable(tablaEstados,lista,teclaAuxiliar);
    if (teclaAuxiliar == 13) pushEnterTable(tablaEstados,lista);
}
</script>
</body>
</html>