<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<html:html>
    <head>
        <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <TITLE>::: ADMINISTRACIÓN LOCAL: Mantenimiento de usuarios :::</TITLE>
        <%
            int maxUsersLicense = 0;
            int numTotalUsuarios = 0;
            int idioma = 1;
            int apl = 1;
            String css="";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css=usuario.getCss();
                }

                numTotalUsuarios = ((Integer)session.getAttribute("NUM_TOTAL_USUARIOS")).intValue();
                maxUsersLicense  = ((Integer)session.getAttribute("MAX-USERS-LICENSE")).intValue();
            }

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<!-- Ficheros JavaScript -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<style type="text/css">
TR.gris TD {background-color:#C0C0C0;color:white;}
</style>
<script type="text/javascript"> 
<%
    /* Recupera el vector de usuarios de la sesion. */

    Vector relacionUsuarios = (Vector) session.getAttribute("RelacionUsuarios");
    int numRelacionUsuarios = 0;
    if (relacionUsuarios != null) {
        numRelacionUsuarios = relacionUsuarios.size();
    }
%>
    
var lista = new Array();
var listaOriginal = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var ultimo = false;
var pagin;

/* Para navegacion Usuarios*/
var lineasPagina   = 10;
var paginaActual   = 1;
var numUsuarios = <%= numRelacionUsuarios %>;
var maxUsersLicense = <%= maxUsersLicense %>;
var numTotalUsuarios = <%= numTotalUsuarios %>

var enl=Math.ceil(numUsuarios/lineasPagina);        
// Fin navegacion Usuarios

var tabUsuarios;
var tableObject=tabUsuarios;

function inicializar() {
    window.focus();
    listaSel = lista;
    cargaPagina(1);
    cargarComboFilasPagina();
}

function cargaPagina(numeroPagina){
    lista = new Array();
    listaOriginal= new Array();
    document.forms[0].paginaListado.value = numeroPagina;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    document.forms[0].opcion.value="cargar_paginaAdmLocal";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
    document.forms[0].submit();
}

function inicializaLista(numeroPagina){
    tableObject=tabUsuarios;
    var j = 0;

    paginaActual = numeroPagina;
    listaP = new Array();

    inicio =0;
    fin = lineasPagina;
    listaP = listaSel;
    tabUsuarios.lineas=listaP;
    refrescaUsuarios();

    domlay('enlace',1,0,0,enlaces());
}

// JAVASCRIPT DE LA TABLA DE USUARIOS ************************************************************
    
function enlaces() {
    numeroPaginas = Math.ceil(numUsuarios /lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPaginaB');
}

function cargaPaginaB(numeroPagina){
    lista = new Array();
    listaOriginal= new Array();
    document.forms[0].paginaListado.value = numeroPagina;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    document.forms[0].opcion.value="cargar_paginaAdmLocal";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
    document.forms[0].submit();
}
    
// FIN DEL JAVASCRIPT DE LA TABLA DE USUARIOS **************************************************************

function callFromTableTo(rowID,tableName){
    if(tabUsuarios.id == tableName){
        datosUsuarios(rowID);
    } 
}

function pulsarSalir() {
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
    document.forms[0].submit();
}

function datosUsuarios(rowID) {
    var variables = new Array();
    variables[0] = lista[rowID][0];
    var primero = listaOriginal[rowID][0];
    var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=datosUsuariosAdmLocal&primero="+primero;
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,variables,
    'width=998,height=540,status='+ '<%=statusBar%>',function(datos){
                    if(datos!=undefined){
                        document.forms[0].opcion.value="inicioAdmLocal";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                        document.forms[0].submit();
                    }
                });
}
            
// Comprueba el límite de usuarios de la licencia si los hubiese
function comprobarLimiteUsuarios(){
    var aux = numTotalUsuarios + 1;   
    if(maxUsersLicense>-1 && aux>maxUsersLicense){               
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjUsersLicExceed")%>' + " " + maxUsersLicense);
        return false;
    }      

    // No se puede dar de alta usuarios
    if(maxUsersLicense==-1){       
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjUsersNotPermited")%>' + " " + maxUsersLicense);
            return false;
        }
        return true;   
}

function pulsarAltaUsuarios() {
    if(comprobarLimiteUsuarios())  
        {          
            var segundo = "alta";
            var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=altaDatosUsuariosAdmLocal&segundo="+segundo;
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,null,
                'width=998,height=700,status='+ '<%=statusBar%>',function(datos){
                    if(datos!=undefined){
                        numUsuarios = numUsuarios + 1;
                        if(numUsuarios>(lineasPagina*paginaSuperior) ) {
                            numeroPaginas = numeroPaginas + 1;
                            paginaSuperior = paginaSuperior + 1;
                        }
                        document.forms[0].opcion.value="inicioAdmLocal";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                        document.forms[0].submit();
                    }
                });
            }
}

function pulsarEliminarUsuarios() {
    if(tabUsuarios.selectedIndex != -1) {
        if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarUsu")%>')) {
            pleaseWait('on');
            document.forms[0].codUsuario.value = listaOriginal[tabUsuarios.selectedIndex][0];
            document.forms[0].opcion.value="eliminarUsuario";
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
            document.forms[0].submit();
        }
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    } 
}

function pulsarModificarUsuarios() {
    if(tabUsuarios.selectedIndex != -1) {
        datosUsuarios(tabUsuarios.selectedIndex);
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    } 
}

function usuarioEliminado(eliminado) {
    numUsuarios = numUsuarios - eliminado;
    if(numUsuarios==(lineasPagina*(paginaSuperior-eliminado)) ) {
        if(numeroPaginas != 1) 	numeroPaginas = numeroPaginas - eliminado;
        if(paginaSuperior !=1) paginaSuperior = paginaSuperior - eliminado;
    }
    document.forms[0].opcion.value="inicioAdmLocal";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
    document.forms[0].submit();
}

function pulsarBuscar() {
    document.forms[0].paginaListado.value = 1;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    document.forms[0].opcion.value="inicioAdmLocal";

    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
    document.forms[0].submit();
}

function pulsarCancelarBuscar() {                               
    document.forms[0].loginT.value="";
    document.forms[0].nombreT.value="";                                
}

function cargarComboFilasPagina(){
    var selectorDeFilas = '<select name="filasPagina" id="filasPagina" class ="" onchange="cambiarFilasPagina();">' + 
                                    '<option value="10">10</option>' + 
                                    '<option value="25">25</option>' + 
                                    '<option value="50">50</option>' + 
                                    '<option value="100">100</option>' + 
                                '</select>';
    document.getElementById('contSelectPax').innerHTML = '<%=descriptor.getDescripcion("mosFilasPag")%>'.replace('_MENU_',selectorDeFilas); 
    document.getElementById('filasPagina').value= lineasPagina;
}

function cambiarFilasPagina(){ 
    lineasPagina = document.getElementById('filasPagina').value;

    cargaPaginaB(1);
}
</script>
</head>
<body class="bandaBody"  onload="javascript:{ pleaseWait('off'); inicializar()}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/administracion/UsuariosGrupos.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="12"/>
<input type="hidden" name="codUsuario" value="">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_mantUsu")%></div>
<div class="contenidoPantalla">
    <table>
        <tr>
            <td>
                <div class="dataTables_wrapper paxinacionDataTables">
                    <label id="contSelectPax"></label>
                </div>
            </td>
        </tr>            
        <tr>
            <td id="tablaUsuarios">
        </tr>
         <tr>  
            <td>
                <div id="enlace" class="dataTables_wrapper"></div>
            </td> 
        </tr>
        <tr>  
            <td class="etiqueta">
                <div style="width:14%;float:left"><%=descriptor.getDescripcion("etiq_login")%>:</div>
                <div style="float:left"><%=descriptor.getDescripcion("gEtiq_Nombre")%>:</div>
            </td> 
        </tr>
         <tr>  
            <td>
                 <input type="text" name="loginT" id="loginT"  maxlength="10" style="width:13%" class="inputTexto"  />
                 <input type="text" name="nombreT" id="nombreT"  maxlength="50" style="width:45%" class="inputTexto"  />
                <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                       value="<%=descriptor.getDescripcion("gbBuscar")%>"
                       onClick="pulsarBuscar();" accesskey="B" style="margin-left:80px">
                <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                       value='<%=descriptor.getDescripcion("gbLimpiar")%>'
                       onClick="pulsarCancelarBuscar();" accesskey="C">
            </td>       
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaUsuarios" onClick="pulsarAltaUsuarios();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificarUsuarios" onClick="pulsarModificarUsuarios();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbBaja")%> name="cmdEliminarUsuarios" onClick="pulsarEliminarUsuarios();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>      
</div>      
</html:form>
<script type="text/javascript">
// JAVASCRIPT DE LA TABLA USUARIOS

var tabUsuarios = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
        '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>',
        '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
        '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
        document.getElementById("tablaUsuarios"));

tabUsuarios.addColumna('120','left','<%= descriptor.getDescripcion("etiq_login")%>');
tabUsuarios.addColumna('680','left','<%= descriptor.getDescripcion("gEtiq_Nombre")%>');
tabUsuarios.addColumna('100','left','<%= descriptor.getDescripcion("etiq_situacion")%>');
tabUsuarios.displayCabecera=true;
tabUsuarios.colorLinea=function(rowID) {
    var eliminado = listaOriginal[rowID][3];
    if(eliminado == '<%=descriptor.getDescripcion("gbBaja")%>')
    return 'gris';
}

function refrescaUsuarios() {
    tabUsuarios.displayTabla();
}

tabUsuarios.displayDatos = pintaDatosUsuarios;

function pintaDatosUsuarios() {
    tableObject = tabUsuarios;
}

// FIN DE LOS JAVASCRIPT'S DE LA TABLA USUARIOS

document.onmouseup = checkKeys;

function checkKeysLocal(evento, tecla){

    var teclaAuxiliar="";
      if(window.event){
        evento = window.event;
        teclaAuxiliar =evento.keyCode;
      }else
            teclaAuxiliar =evento.which;


    if(teclaAuxiliar == 40){
        if(tabUsuarios==tableObject) {
            upDownTable(tabUsuarios,listaSel,teclaAuxiliar);
        } 
    }
    if (teclaAuxiliar == 38){
        if(tabUsuarios==tableObject) {
            upDownTable(tabUsuarios,listaSel,teclaAuxiliar);
        } 
    }
    if(teclaAuxiliar == 13){
        if((tabUsuarios.selectedIndex>-1)&&(tabUsuarios.selectedIndex < lista.length)&&(!ultimo)){
            if(tabUsuarios==tableObject)
                callFromTableTo(tabUsuarios.selectedIndex,tabUsuarios.id);
        }
    }
    keyDel(evento);
}
</script>
</BODY>
</html:html>