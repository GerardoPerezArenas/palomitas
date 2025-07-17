<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.planeamiento.form.ConvenioUrbanisticoForm"%>
<%@ page import="java.util.Collection"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html:html>
<head>
<jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<TITLE>::: Convenios Urbanísticos :::</TITLE>
<%
    UsuarioValueObject usuarioVO = null;
    int idioma = 1;

    if (session!=null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma =  usuarioVO.getIdioma();
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />

<style type="text/css">

    /* Override styles from webfxlayout */
    /* Para colocar las pestañas correctamente */

    .dynamic-tab-pane-control .tab-page {
        height:		200px;
    }

    .dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
        height:		100px;
    }

</style>


<!-- Ficheros JavaScript -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/listaBusquedaTerceros.js'/>"></script>

<script type="text/javascript">

var consultando = false;
var errorFechas = false;
var insertar = false;
var modificar = false;
var baja = false;
var numEnlaces = 10;
var registroInf;
var registroSup;
var numeroRegistro = -1;
var displayBotonesModificacion = "inline";

function borrarDatos(){
    document.forms[0].anho.value = '';
    document.forms[0].numero.value = '';
    document.forms[0].codigoSubseccion.value = '';
    document.forms[0].subseccion.value = '';
    document.forms[0].objetoConvenio.value = '';
    document.forms[0].codigoAmbito.value = '';
    document.forms[0].ambito.value = '';
    document.forms[0].parcela.value = '';
    document.forms[0].nombreFirmante.value = '';
    document.forms[0].codigoOrganoAprobacion.value = '';
    document.forms[0].organoAprobacion.value = '';
    document.forms[0].fechaAprobacion.value = '';
    document.forms[0].fechaBaja.value = '';
    document.forms[0].fechaPublicacion.value = '';
    document.forms[0].numeroPublicacion.value = '';
    document.forms[0].observaciones.value = '';
    document.forms[0].archivo.value = '';
    tabAnotacion.lineas=new Array();
    refrescaAnotaciones();
    tabRectificacion.lineas=new Array();
    refrescaRectificaciones();
    habilitarCampos();
    ocultarBotonesModificacion();
    ocultarModificacion();
}

function pulsarLimpiar() {
    document.forms[0].target = "oculto";
    document.forms[0].action = "<html:rewrite page='/planeamiento/LimpiarConvenioUrbanistico.do'/>" +
                               "?numeroRegistro=" + numeroRegistro;
    document.forms[0].submit();
}

function Inicio() {
    window.focus();
    creaCombos();
    tableObject=tabAnotacion;
    cargaTabla();
    tableObjectRectificacion=tabRectificacion;
    cargaTablaRectificacion();
    habilitarImagenCal("calFechaAprobacion",true);
    habilitarImagenCal("calFechaBaja",true);
    habilitarImagenCal("calFechaPublicacion",true);
    habilitarImagenCal("calFechaAnotacion",true);
    habilitarImagenCal("calFechaRectificacion",true);
    <%-- Establecer modo consulta o normal--%>
     <%
     String consultando = (String) session.getAttribute("modoConsulta");
     if (consultando.equals("true")) {
     %>
        displayBotonesModificacion = "none";
    <%} else {%>
        displayBotonesModificacion = "inline";
    <%}%>
    document.getElementById("tdBotonAlta").style.display = displayBotonesModificacion;
    document.getElementById("tdEspacioBotonAlta").style.display = displayBotonesModificacion;
    <%-- Si venimos de una busqueda por listado--%>
    <%
        String numeroRegistro = (String) request.getAttribute("registroActual");
        if (numeroRegistro!=null) {
            int numRegistros = ((Collection) session.getAttribute("registrosConvenio")).size();
            String tipoRegistro = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getTipoRegistro();
            String anho = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getAnho();
            String numero = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getNumero();
            String codigoSubseccion = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getCodigoSubseccion();
            String objetoConvenio = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getObjetoConvenio();
            String codigoAmbito = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getCodigoAmbito();
            String parcela = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getParcela();
            String codigoOrganoAprobacion = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getCodigoOrganoAprobacion();
            String fechaAprobacion = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getFechaAprobacion();
            String fechaBaja = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getFechaBaja();
            String fechaPublicacion = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getFechaPublicacion();
            String numeroPublicacion = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getNumeroPublicacion();
            if (numeroPublicacion==null) {
                numeroPublicacion = "";
            }
            String observaciones = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getObservaciones();
            if (observaciones==null) {
                observaciones = "";
            }
            String archivo = ((ConvenioUrbanisticoForm) session.getAttribute("ConvenioUrbanisticoForm")).getArchivo();
            if (archivo==null) {
                archivo = "";
            }
            String nombreFirmante = (String) request.getAttribute("nombreFirmante");
    %>
        document.forms[0].tipoRegistro.value = "<%=tipoRegistro%>";
        navegacionConsulta("<%=numRegistros%>", "<%=numeroRegistro%>");
        var objetoConvenio= "<%=objetoConvenio%>";
        var observaciones = "<%=observaciones%>";
        var file = "<%=archivo%>";
        var busca = /@intro@/gi;
        var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
        objetoConvenio = objetoConvenio.replace(busca, pStrReemplaza);
        observaciones = observaciones.replace(busca, pStrReemplaza);
        file = file.replace(busca, pStrReemplaza);
        rellenaDatosRegistro("<%=anho%>", "<%=numero%>", "<%=codigoSubseccion%>", objetoConvenio,
                "<%=codigoAmbito%>", "<%=parcela%>", "<%=codigoOrganoAprobacion%>", "<%=fechaAprobacion%>", "<%=fechaBaja%>",
                "<%=fechaPublicacion%>", "<%=numeroPublicacion%>", observaciones, file, "<%=nombreFirmante%>", lista,
                listaRectificacion);
        numeroRegistro = "<%=numeroRegistro%>";
        ocultarCapaBotones("capaBotonesInsertar");
        ocultarCapaBotones("capaBotonesAnotacion");
        ocultarCapaBotones("capaBotonesListaAnotacion");
        ocultarCapaBotones("capaBotonesRectificacion");
        ocultarCapaBotones("capaBotonesListaRectificacion");
        mostrarCapaBotones("capaBotonesConsulta");
        document.forms[0].anho.className = "inputTexto";
        pulsarCancelarAnotacion();
        pulsarCancelarRectificacion();
        mostrarBotonesModificacion();
        insertar = false;
        modificar = false;
        baja = false;
    <%} else {%>
        pulsarCancelar();
        ocultarBotonesModificacion();
    <%}%>
    ocultarModificacion();
}

function pulsarModificar() {
    mostrarBotonesModificacion();
    modificar = true;
    document.getElementById("capaNavegacionConsulta").style.display = "none";
    habilitarCampos();
    ocultarCapaBotones("capaBotonesConsulta");
    mostrarCapaBotones("capaBotonesInsertar");
    mostrarCapaBotones("capaBotonesAnotacion");
    mostrarCapaBotones("capaBotonesListaAnotacion");
    mostrarCapaBotones("capaBotonesRectificacion");
    mostrarCapaBotones("capaBotonesListaRectificacion");
    document.forms[0].anho.className = "inputTextoObligatorio";
    document.forms[0].numero.className = "inputTextoObligatorio";
    document.forms[0].codigoSubseccion.className = "inputTextoObligatorio";
    document.forms[0].subseccion.className = "inputTextoObligatorio";
    document.forms[0].objetoConvenio.className = "textareaTextoObligatorio";
    document.forms[0].codigoAmbito.className = "inputTextoObligatorio";
    document.forms[0].ambito.className = "inputTextoObligatorio";
    document.forms[0].parcela.className = "inputTexto";
    document.forms[0].nombreFirmante.className = "inputTextoObligatorio";
    document.forms[0].codigoOrganoAprobacion.className = "inputTextoObligatorio";
    document.forms[0].organoAprobacion.className = "inputTextoObligatorio";
    document.forms[0].fechaAprobacion.className = "inputTxtFechaObligatorio";
    document.forms[0].fechaBaja.className = "inputTxtFecha";
    document.forms[0].fechaPublicacion.className = "inputTxtFecha";
    document.forms[0].numeroPublicacion.className = "inputTexto";
    document.forms[0].observaciones.className = "inputTexto";
    document.forms[0].archivo.className = "inputTexto";
    document.getElementById("trFechaBaja").style.display = "none";
}

function pulsarGrabar() {
    if (validarFormulario()) {
        if (insertar) {
            document.forms[0].target = "oculto";
            document.forms[0].action = "<html:rewrite page='/planeamiento/InsertarConvenioUrbanistico.do'/>";
            document.forms[0].submit();
            insertar = false;
        }
        if (modificar || baja) {
            document.forms[0].target = "oculto";
            document.forms[0].action = "<html:rewrite page='/planeamiento/ModificarConvenioUrbanistico.do'/>";
            document.forms[0].submit();
            modificar = false;
            if (baja) {
                //                document.getElementById("trFechaBaja").style.display = "none";
                document.getElementById("tdBotonAlta").style.display = "none";
                document.getElementById("tdEspacioBotonAlta").style.display = "none";
                document.getElementById("tdBotonModificar").style.display = "none";
                document.getElementById("tdEspacioBotonModificar").style.display = "none";
                document.getElementById("tdBotonBaja").style.display = "none";
                document.getElementById("tdEspacioBotonBaja").style.display = "none";
            }
            baja = false;
        }
        document.forms[0].fechaBaja.className = "inputTxtFecha";
    }
}

function pulsarAlta() {
    insertar = true;
    pulsarLimpiar();
}

function pulsarPostAlta() {
//    document.getElementById("tabPage4").style.visibility = "hidden";
    if (insertar) {
        tp1.hideTabPage(3);
    //    borrarDatos();
        ocultarCapaBotones("capaBotonesConsulta");
        mostrarCapaBotones("capaBotonesInsertar");
        mostrarCapaBotones("capaBotonesAnotacion");
        mostrarCapaBotones("capaBotonesListaAnotacion");
        ocultarCapaBotones("capaBotonesRectificacion");
        ocultarCapaBotones("capaBotonesListaRectificacion");
        document.forms[0].anho.className = "inputTexto";
        document.forms[0].numero.className = "inputTexto";
        document.forms[0].codigoSubseccion.className = "inputTextoObligatorio";
        document.forms[0].subseccion.className = "inputTextoObligatorio";
        document.forms[0].objetoConvenio.className = "textareaTextoObligatorio";
        document.forms[0].codigoAmbito.className = "inputTextoObligatorio";
        document.forms[0].ambito.className = "inputTextoObligatorio";
        document.forms[0].parcela.className = "inputTexto";
        document.forms[0].nombreFirmante.className = "inputTextoObligatorio";
        document.forms[0].codigoOrganoAprobacion.className = "inputTextoObligatorio";
        document.forms[0].organoAprobacion.className = "inputTextoObligatorio";
        document.forms[0].fechaAprobacion.className = "inputTxtFechaObligatorio";
        document.forms[0].fechaBaja.className = "inputTxtFecha";
        document.forms[0].fechaPublicacion.className = "inputTxtFecha";
        document.forms[0].numeroPublicacion.className = "inputTexto";
        document.forms[0].observaciones.className = "inputTexto";
        document.forms[0].archivo.className = "inputTexto";
        document.getElementById("trFechaBaja").style.display = "none";
        document.forms[0].numero.value = "";
        document.forms[0].numero.readOnly = true;
        document.forms[0].anho.value = "";
        document.forms[0].anho.readOnly = true;
        document.getElementById("capaRadioButtons").style.display = "none";
    }
}

function pulsarCancelar() {
    tp1.setSelectedIndex(0);
    if (document.getElementById("capaNavegacionConsulta").style.display=="none") {
        deshabilitarCampos();
    }
    ocultarCapaBotones("capaBotonesInsertar");
    ocultarCapaBotones("capaBotonesAnotacion");
    ocultarCapaBotones("capaBotonesListaAnotacion");
    ocultarCapaBotones("capaBotonesRectificacion");
    ocultarCapaBotones("capaBotonesListaRectificacion");
    mostrarCapaBotones("capaBotonesConsulta");
    pulsarCancelarAnotacion();
    pulsarCancelarRectificacion();
    document.forms[0].anho.className = "inputTexto";
    document.forms[0].numero.className = "inputTexto";
    document.forms[0].codigoSubseccion.className = "inputTexto";
    document.forms[0].subseccion.className = "inputTexto";
    document.forms[0].objetoConvenio.className = "textareaTexto";
    document.forms[0].codigoAmbito.className = "inputTexto";
    document.forms[0].ambito.className = "inputTexto";
    document.forms[0].parcela.className = "inputTexto";
    document.forms[0].nombreFirmante.className = "inputTexto";
    document.forms[0].codigoOrganoAprobacion.className = "inputTexto";
    document.forms[0].organoAprobacion.className = "inputTexto";
    document.forms[0].fechaAprobacion.className = "inputTxtFecha";
    document.forms[0].numero.readOnly = false;
    document.forms[0].anho.readOnly = false;
    document.forms[0].codigoSubseccion.readOnly = false;
    insertar = false;
    modificar = false;
    baja = false;
    document.getElementById("trFechaBaja").style.display = "block";
    document.forms[0].fechaBaja.className = "inputTxtFecha";
    if (numeroRegistro!=-1) {
        determinarRegistro(numeroRegistro);
//        document.getElementById("tabPage4").style.visibility = "visible";
        document.getElementById("tdBotonListado").style.display = "inline";
        document.getElementById("tdEspacioBotonListado").style.display = "inline";
        tp1.showTabPage(3);
    } else {
        tp1.hideTabPage(3);
        document.getElementById("tdBotonListado").style.display = "none";
        document.getElementById("tdEspacioBotonListado").style.display = "none";
        document.getElementById("capaRadioButtons").style.display = "inline";
    }
}

<%--function pulsarCancelarSalir() {
    window.location = "<html:rewrite page='/planeamiento/CargarConvenioUrbanistico.do'/>?tipoRegistro=" + document.forms[0].tipoRegistro.value + "&consultando=true";
}--%>

function pulsarBaja() {
    mostrarBotonesModificacion();
    document.getElementById("capaNavegacionConsulta").style.display = "none";
    baja = true;
    habilitarCampos();
    ocultarCapaBotones("capaBotonesConsulta");
    mostrarCapaBotones("capaBotonesInsertar");
    mostrarCapaBotones("capaBotonesAnotacion");
    mostrarCapaBotones("capaBotonesListaAnotacion");
    mostrarCapaBotones("capaBotonesRectificacion");
    mostrarCapaBotones("capaBotonesListaRectificacion");
    document.forms[0].anho.className = "inputTextoObligatorio";
    document.forms[0].numero.className = "inputTextoObligatorio";
    document.forms[0].codigoSubseccion.className = "inputTextoObligatorio";
    document.forms[0].subseccion.className = "inputTextoObligatorio";
    document.forms[0].objetoConvenio.className = "textareaTextoObligatorio";
    document.forms[0].codigoAmbito.className = "inputTextoObligatorio";
    document.forms[0].ambito.className = "inputTextoObligatorio";
    document.forms[0].parcela.className = "inputTexto";
    document.forms[0].nombreFirmante.className = "inputTextoObligatorio";
    document.forms[0].codigoOrganoAprobacion.className = "inputTextoObligatorio";
    document.forms[0].organoAprobacion.className = "inputTextoObligatorio";
    document.forms[0].fechaAprobacion.className = "inputTxtFechaObligatorio";
    document.forms[0].fechaBaja.className = "inputTxtFechaObligatorio";
    document.forms[0].fechaPublicacion.className = "inputTxtFecha";
    document.forms[0].numeroPublicacion.className = "inputTexto";
    document.forms[0].observaciones.className = "inputTexto";
    document.forms[0].archivo.className = "inputTexto";
    document.getElementById("trFechaBaja").style.display = "block";
}

function pulsarBuscar() {
//    habilitarCampos();
    var action = "<html:rewrite page='/planeamiento/BuscarConvenioUrbanistico.do'/>";
    if (document.forms[0].tipoConsulta[0].checked) {
        action = action + "?listado=true";
        document.forms[0].target="mainFrame";
    } else {
        action = action + "?listado=false";
    document.forms[0].target = "oculto";
    }
    document.forms[0].action = action;
    document.forms[0].submit();
}

function pulsarAltaAnotacion() {
    document.getElementById("tabListaAnotaciones").style.display = "none";
    document.getElementById("tablaAnotaciones").style.display = "block";
    document.forms[0].fechaAnotacion.className = "inputTxtFechaObligatorio";
}

function pulsarGrabarAnotacion() {
    if (document.forms[0].fechaAnotacion.value!="") {
        document.forms[0].target = "oculto";
        document.forms[0].action = "<html:rewrite page='/planeamiento/InsertarAnotacionConvenio.do'/>";
        document.forms[0].submit();
        pulsarCancelarAnotacion();
    } else {
        jsp_alerta("A", "Debe rellenar el campo obligatorio");
    }
}

function pulsarEliminarAnotacion() {
    if(tabAnotacion.selectedIndex != -1) {
        document.forms[0].target = "oculto";
        document.forms[0].action = "<html:rewrite page='/planeamiento/EliminarAnotacionConvenio.do'/>";
        document.forms[0].submit();
        pulsarCancelarAnotacion();
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarModificarAnotacion() {
    if(tabAnotacion.selectedIndex != -1) {
        pulsarAltaAnotacion();
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarCancelarAnotacion() {
    document.getElementById("numeroAnotacion").value = "";
    document.getElementById("fechaAnotacion").value = "";
    document.getElementById("comentarioAnotacion").value = "";
    document.getElementById("tablaAnotaciones").style.display = "none";
    document.getElementById("tabListaAnotaciones").style.display = "block";
    document.forms[0].fechaAnotacion.className = "inputTxtFecha";
}

function recuperaAnotaciones(lis) {
    lista = new Array();

    lista = lis;

    tabAnotacion.lineas=lista;
    tabAnotacion.displayTabla();
}

function pulsarAltaRectificacion() {
    document.getElementById("tabListaRectificaciones").style.display = "none";
    document.getElementById("tablaRectificaciones").style.display = "block";
    document.forms[0].fechaRectificacion.className = "inputTxtFechaObligatorio";
}

function pulsarGrabarRectificacion() {
    if (document.forms[0].fechaRectificacion.value!="") {
        document.forms[0].target = "oculto";
        document.forms[0].action = "<html:rewrite page='/planeamiento/InsertarRectificacionConvenio.do'/>";
        document.forms[0].submit();
        pulsarCancelarRectificacion();
    } else {
        jsp_alerta("A", "Debe rellenar el campo obligatorio");
    }
}

function pulsarEliminarRectificacion() {
    if(tabRectificacion.selectedIndex != -1) {
        document.forms[0].target = "oculto";
        document.forms[0].action = "<html:rewrite page='/planeamiento/EliminarRectificacionConvenio.do'/>";
        document.forms[0].submit();
        pulsarCancelarRectificacion();
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarModificarRectificacion() {
    if(tabRectificacion.selectedIndex != -1) {
        pulsarAltaRectificacion();
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarCancelarRectificacion() {
    document.getElementById("numeroRectificacion").value = "";
    document.getElementById("fechaRectificacion").value = "";
    document.getElementById("comentarioRectificacion").value = "";
    document.getElementById("tablaRectificaciones").style.display = "none";
    document.getElementById("tabListaRectificaciones").style.display = "block";
    document.forms[0].fechaRectificacion.className = "inputTxtFecha";
}

function recuperaRectificaciones(lis) {
    listaRectificacion = new Array();

    listaRectificacion = lis;

    tabRectificacion.lineas=listaRectificacion;
    tabRectificacion.displayTabla();
}

function pulsarListado() {
    var action = "<html:rewrite page='/planeamiento/VerListadoConvenioUrbanistico.do'/>";
    document.forms[0].target="mainFrame";
    document.forms[0].action = action;
    document.forms[0].submit();
}

function pulsarSalir(){
    window.location = "<html:rewrite page='/planeamiento/SalirConvenioUrbanistico.do'/>";
}

function navegacionConsulta(numRegistros, registroActual) {
    var htmlString = "";
    calcularLimites(registroActual, numRegistros);
    if(registroActual != -1){numeroRegistro = registroActual;}
    if (numRegistros > 1){
        htmlString += '<table border="0" cellpadding="0" cellspacing="0" align="center"><tr>';
        if (registroInf > numEnlaces) { // botones izq
            htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
            htmlString += '<a href="javascript:determinarRegistro(1)" class="navegacion" target="_self"><b><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-fast-backward"></span></font></b></a></td>';
            htmlString += '<td class="fondo" width="5" align="center" style="background-color: #E6E6E6">&nbsp;</td>';
            htmlString += '<td width="35" align="center" valign="middle" bgcolor="#A7B996">';
            htmlString += '<a href="javascript:determinarRegistro('+eval(registroInf-numEnlaces)+')" class="navegacion" target="_self"><b><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-backward"></span></font></b></a></td>';
        } else htmlString += '<td width="75" class="fondo" align="center" style="background-color: #E6E6E6">&nbsp;';
        htmlString += '</td><td  class="fondo" align="center" width="330" style="background-color: #E6E6E6">&nbsp;&nbsp;';
        for (var i=registroInf; i <= registroSup; i++) {
            if (i == registroActual)
                htmlString += '<font style="color:#A7B996; font-family:Verdana,Arial; font-size:11px; font-weight:bold">'+ (i) + '</font>&nbsp;&nbsp;';
            else
                htmlString += '<a href="javascript:determinarRegistro('+ (i) + ')" class="navegacion" target="_self">'+ ''+(i) + '</a>&nbsp;&nbsp;';
        }
        if (registroInf + numEnlaces <= numRegistros){ // botones dcha
            htmlString += '</td><td width="35" align="center" valign="middle" bgcolor="#A7B996">';
            htmlString += '<a href="javascript:determinarRegistro('+ eval(registroSup+1) + ')" class="navegacion" target="_self"><b><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-forward"></span></font></b></a>';
            htmlString += '</td><td class="fondo" width="5" align="center" style="background-color: #E6E6E6">&nbsp;';
            htmlString += '</td><td width="35" align="center" valign="middle" bgcolor="#A7B996">';
            htmlString += '<a href="javascript:determinarRegistro('+ (eval((Math.ceil(numRegistros/numEnlaces)-1) * numEnlaces + 1)) + ')" class="navegacion" target="_self"><b><font style="color:#FFFFFF; font-family:Verdana,Arial; font-size:9px; font-weight:bold; text-decoration: none"><span class="fa fa-fast-forward"></span></font></b></a>';
        } else htmlString += '</td><td width="75" class="fondo" align="center" style="background-color: #E6E6E6">&nbsp;';
        htmlString += '</td></tr></table>';
        htmlString += '<center><font class="textoSuelto" class="fondo">'+numRegistros + '&nbsp;registros encontrados.</font></center>'
        domlay('capaNavegacionConsulta',1,0,0, htmlString);
    } else if(numRegistros == 0 || numRegistros ==1) {
        htmlString += '<center><font class="textoSuelto" class="fondo">'+numRegistros + ( (numRegistros==1)?'&nbsp;registro encontrado.':'&nbsp;registros encontrados.')+'</font></center>'
        domlay('capaNavegacionConsulta',1,0,0, htmlString);
    }
}

function showBarraNavegacion() {
    document.getElementById("capaNavegacionConsulta").style.display = "block";
}

function calcularLimites(registroSelec, numRegistros) {
    registroInf = ( (Math.ceil(registroSelec/numEnlaces)-1)) * numEnlaces + 1;
    registroSup = (eval(registroInf + numEnlaces -1) < numRegistros)?eval(registroInf + numEnlaces -1):numRegistros;
}

function determinarRegistro(numRegistro) {
//    habilitarCampos();
    numeroRegistro = numRegistro;
    document.forms[0].target = "oculto";
    document.forms[0].action = "<html:rewrite page='/planeamiento/RellenarConvenioUrbanistico.do'/>" +
                               "?numeroRegistro=" + numRegistro;
    document.forms[0].submit();
}

function ocultarModificacion() {
    if (document.forms[0].fechaBaja.value!="") {
        document.getElementById("tdBotonAlta").style.display = "none";
        document.getElementById("tdEspacioBotonAlta").style.display = "none";
        document.getElementById("tdBotonModificar").style.display = "none";
        document.getElementById("tdEspacioBotonModificar").style.display = "none";
        document.getElementById("tdBotonBaja").style.display = "none";
        document.getElementById("tdEspacioBotonBaja").style.display = "none";
    }
}

function ocultarBotonesModificacion(){
    if (document.forms[0].numero.value=="") {
        document.getElementById("tdBotonModificar").style.display = "none";
        document.getElementById("tdEspacioBotonModificar").style.display = "none";
        document.getElementById("tdBotonBaja").style.display = "none";
        document.getElementById("tdEspacioBotonBaja").style.display = "none";
<%--        document.getElementById("tdBotonCancelarSalir").style.display = "none";
        document.getElementById("tdEspacioBotonCancelarSalir").style.display = "none";--%>
        document.getElementById("tdBotonLimpiar").style.display = "inline";
        document.getElementById("tdEspacioBotonLimpiar").style.display = "inline";
        document.getElementById("tdBotonBuscar").style.display = "inline";
        document.getElementById("tdEspacioBotonBuscar").style.display = "inline";
        document.getElementById("capaRadioButtons").style.display = "inline";
    }
}

function mostrarBotonesModificacion(){
    document.getElementById("tdBotonModificar").style.display = displayBotonesModificacion;
    document.getElementById("tdEspacioBotonModificar").style.display = displayBotonesModificacion;
    document.getElementById("tdBotonBaja").style.display = displayBotonesModificacion;
    document.getElementById("tdEspacioBotonBaja").style.display = displayBotonesModificacion;
<%--    document.getElementById("tdBotonCancelarSalir").style.display = displayBotonesModificacion;
    document.getElementById("tdEspacioBotonCancelarSalir").style.display = displayBotonesModificacion;--%>
    document.getElementById("tdBotonLimpiar").style.display = "none";
    document.getElementById("tdEspacioBotonLimpiar").style.display = "none";
    document.getElementById("tdBotonBuscar").style.display = "none";
    document.getElementById("tdEspacioBotonBuscar").style.display = "none";
    document.getElementById("tdBotonListado").style.display = "inline";
    document.getElementById("tdEspacioBotonListado").style.display = "inline";
    document.getElementById("capaRadioButtons").style.display = "none";
}

function pulsarFirmantes(){
    var source = "<html:rewrite page='/jsp/planeamiento/firmantes.jsp'/>";
    var parameter;
    if (document.forms[0].subseccion.className == "inputTextoObligatorio") {
        parameter = "&modif=true";
    } else {
        parameter = "&modif=false";
    }
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/planeamiento/mainVentana.jsp?source='+source + parameter,
	'width=540,height=410,status='+ '<%=statusBar%>',function(datos){
                        if (datos) {
                            document.forms[0].nombreFirmante.value = datos;
                        } else {
                            document.forms[0].nombreFirmante.value = "";
                        }
                  });
}

function firmantesNoExistenInsercion(){
    insertar = true;
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoFirmantes")%>');
}

function firmantesNoExistenModificacion(){
    modificar = true;
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoFirmantes")%>');
}

function errorInsercion(){
    insertar = true;
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorInsercion")%>');
}

function errorModificacion(){
    modificar = true;
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorModificacion")%>');
}

<%-- No se debe quitar.Requerida por rellenarTercero en listaBusquedasTerceros--%>
function mostrarDescripcionTipoDoc(){
}

function limpiarInputs() {
    tab.selectLinea(tab.selectedIndex);
    borrarDatos();
}

function errorEliminar() {
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjManUtil")%>');
}

function mostrarCalFechaAprobacion() {
    if (document.getElementById("calFechaAprobacion").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaAprobacion',null,null,null,'','calFechaAprobacion','',null,null,null,null,null,null,null);
}

function mostrarCalFechaBaja() {
    if (document.getElementById("calFechaBaja").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaBaja',null,null,null,'','calFechaBaja','',null,null,null,null,null,null,null);
}

function mostrarCalFechaPublicacion() {
    if (document.getElementById("calFechaPublicacion").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaPublicacion',null,null,null,'','calFechaPublicacion','',null,null,null,null,null,null,null);
}

function mostrarCalFechaAnotacion() {
    if (document.getElementById("calFechaAnotacion").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaAnotacion',null,null,null,'','calFechaAnotacion','',null,null,null,null,null,null,null);
}

function mostrarCalFechaRectificacion() {
    if (document.getElementById("calFechaRectificacion").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaRectificacion',null,null,null,'','calFechaRectificacion','',null,null,null,null,null,null,null);
}

function comprobarFecha(inputFecha) {
    var formato = 'dd/mm/yyyy';
    if (Trim(inputFecha.value)!='') {
        if (consultando) {
            var validas = true;
            var fechaFormateada=inputFecha.value;
            var pos=0;
            var fechas = Trim(inputFecha.value);
            var fechas_array = fechas.split(/[:|&<>!=]/);
            for (var loop=0; loop < fechas_array.length; loop++) {
                f = fechas_array[loop];
                formato = formatoFecha(Trim(f));
                var D = ValidarFechaConFormato(f,formato);
                if (!D[0]) validas=false;
                else {
                    if (fechaFormateada.indexOf(f,pos) != -1) {
                        var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
                        var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
                        pos=fechaFormateada.indexOf(f,pos);
                        fechaFormateada = toTheLeft + D[1]+ toTheRight;
                    }
                }
            }
            if (!validas) {
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                inputFecha.focus();
                return false;
            } else {
                inputFecha.value = fechaFormateada;
                return true;
            }
        } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
            var D = ValidarFechaConFormato(inputFecha.value,formato);
            if (!D[0]){
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                inputFecha.focus();
                return false;
            } else {
                inputFecha.value = D[1];
                return true;
            }
        }
    }
    return true;
}

function ValidarFechaConFormato(fecha, formato) {
    if (formato==null) formato ="dd/mm/yyyy";
    if (formato=="mm/yyyy")
        fecha = "01/"+fecha;
    else if (formato=="yyyy")
        fecha ="01/01/"+fecha;
    else if (formato =="mmyyyy")
        fecha = "01"+fecha;

    var D = DataValida(fecha);
    if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha;
    else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
    else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
    else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
    return D;
}

function mostrarCapaBotones(nombreCapa) {
    document.getElementById(nombreCapa).style.visibility='visible';
}

function ocultarCapaBotones(nombreCapa) {
    document.getElementById(nombreCapa).style.visibility='hidden';
}

function rellenaNumero(anho, numero) {
    document.forms[0].anho.value = anho;
    document.forms[0].numero.value = numero;
}

function rellenarDatos(tableObject, rowID){
    if(rowID>-1 && !tableObject.ultimoTable && (tableObject.parent.id=="tablaListaAnotaciones")){
        document.forms[0].numeroAnotacion.value = lista[rowID][0];
        document.forms[0].fechaAnotacion.value = lista[rowID][1];
        document.forms[0].comentarioAnotacion.value = lista[rowID][2];
    } else if(rowID>-1 && !tableObject.ultimoTable && (tableObject.parent.id=="tablaListaRectificaciones")){
            document.forms[0].numeroRectificacion.value = listaRectificacion[rowID][0];
            document.forms[0].fechaRectificacion.value = listaRectificacion[rowID][1];
            document.forms[0].comentarioRectificacion.value = listaRectificacion[rowID][2];
    }else borrarDatos();
}

function rellenaDatosRegistro(anho,numero,subseccion,objeto,ambito,parc,organo,
                              aprobacion,baja,fecPubli,numPubli,obs,arch,nomFir,anotaciones,rectificaciones) {
    document.forms[0].anho.value = anho;
    document.forms[0].numero.value = numero;
    document.forms[0].codigoSubseccion.value = subseccion;
    comboSubsecciones.buscaLinea(subseccion);
    document.forms[0].objetoConvenio.value = objeto;
    document.forms[0].codigoAmbito.value = ambito;
    comboAmbitos.buscaLinea(ambito);
    document.forms[0].parcela.value = parc;
    document.forms[0].codigoOrganoAprobacion.value = organo;
    comboOrganosAprobacion.buscaLinea(organo);
    document.forms[0].fechaAprobacion.value = aprobacion;
    document.forms[0].fechaBaja.value = baja;
    document.forms[0].fechaPublicacion.value = fecPubli;
    document.forms[0].numeroPublicacion.value = numPubli;
    document.forms[0].observaciones.value = obs;
    document.forms[0].archivo.value = arch;
    document.forms[0].nombreFirmante.value = nomFir;
    recuperaAnotaciones(anotaciones);
    recuperaRectificaciones(rectificaciones);
    tp1.showTabPage(3);
    deshabilitarCampos();
}

function deshabilitarCampos() {
    document.forms[0].anho.disabled = true;
    document.forms[0].numero.disabled = true;
    comboSubsecciones.deactivate();
    document.forms[0].objetoConvenio.disabled = true;
    document.forms[0].objetoConvenio.className = "inputTextoDeshabilitado"
    comboAmbitos.deactivate();
    document.forms[0].parcela.disabled = true;
    document.forms[0].parcela.className = "inputTextoDeshabilitado"
    document.forms[0].nombreFirmante.className = "inputTextoDeshabilitado";
    document.forms[0].nombreFirmante.disabled = true;
    comboOrganosAprobacion.deactivate();
    document.forms[0].fechaAprobacion.disabled = true;
    document.forms[0].fechaAprobacion.className = "inputTxtFechaDeshabilitado"
    document.getElementById("anchorFechaAprobacion").style.display = "none";
    document.forms[0].fechaBaja.disabled = true;
    document.forms[0].fechaBaja.className = "inputTxtFechaDeshabilitado"
    document.getElementById("anchorFechaBaja").style.display = "none";
    document.forms[0].fechaPublicacion.disabled = true;
    document.forms[0].fechaPublicacion.className = "inputTxtFechaDeshabilitado"
    document.getElementById("anchorFechaPublicacion").style.display = "none";
    document.forms[0].numeroPublicacion.disabled = true;
    document.forms[0].numeroPublicacion.className = "inputTextoDeshabilitado"
    document.forms[0].observaciones.disabled = true;
    document.forms[0].observaciones.className = "inputTextoDeshabilitado"
    document.forms[0].archivo.disabled = true;
    document.forms[0].archivo.className = "inputTextoDeshabilitado"
}

function habilitarCampos() {
    if (!modificar && !baja) {
        comboSubsecciones.activate();
    } else {
        document.forms[0].anho.readOnly = true;
        document.forms[0].numero.readOnly = true;
        document.forms[0].codigoSubseccion.disabled=false;
        document.forms[0].codigoSubseccion.readOnly = true;
    }
    document.forms[0].anho.disabled = false;
    document.forms[0].numero.disabled = false;
    document.forms[0].objetoConvenio.disabled = false;
    document.forms[0].objetoConvenio.className = "inputTexto"
    comboAmbitos.activate();
    document.forms[0].parcela.disabled = false;
    document.forms[0].parcela.className = "inputTexto"
    document.forms[0].nombreFirmante.className = "inputTexto";
    document.forms[0].nombreFirmante.disabled = false;
    comboOrganosAprobacion.activate();
    document.forms[0].fechaAprobacion.disabled = false;
    document.forms[0].fechaAprobacion.className = "inputTxtFecha";
    document.getElementById("anchorFechaAprobacion").style.display = "inline";
    document.forms[0].fechaBaja.disabled = false;
    document.forms[0].fechaBaja.className = "inputTxtFecha";
    document.getElementById("anchorFechaBaja").style.display = "inline";
    document.forms[0].fechaPublicacion.disabled = false;
    document.forms[0].fechaPublicacion.className = "inputTxtFecha";
    document.getElementById("anchorFechaPublicacion").style.display = "inline";
    document.forms[0].numeroPublicacion.disabled = false;
    document.forms[0].numeroPublicacion.className = "inputTexto"
    document.forms[0].observaciones.disabled = false;
    document.forms[0].observaciones.className = "inputTexto"
    document.forms[0].archivo.disabled = false;
    document.forms[0].archivo.className = "inputTexto"
}

<%--function selecFila(des){
    var indexOld = tab.selectedIndex;
    if(des.length != 0){
        for (var x=0; x<lista.length; x++){
            var auxLis = new String(lista[x][1]);
            auxLis = auxLis.substring(0,des.length);
            if(auxLis == des){
                if (x!=indexOld)tab.selectLinea(x);
                break;
            }
        }
    }else tab.selectLinea(-1);
}

function buscar(){
    var auxCod = new String("");
    var auxDes = new String("");
    if((event.keyCode != 40)&&(event.keyCode != 38)&&(event.keyCode != 8)){
        if(event.keyCode != 13){
            auxCod = document.forms[0].codigo.value;
            auxDes = document.forms[0].descripcion.value;
            if(event.keyCode == 8 && auxDes.length == 0) borrarDatos();
            selecFila(auxDes);
        }else{
            if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)){
                document.forms[0].codigo.value = lista[tab.selectedIndex][0];
                document.forms[0].descripcion.value = lista[tab.selectedIndex][1];
                auxDes = lista[tab.selectedIndex][1];
            }
        }
    }
}
--%>
/////////////// Control teclas.

function checkKeysLocal(evento,tecla) {
     var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;


    if('Alt+A'==tecla) ejecutarAccionBoton("tdBotonAlta", "capaBotonesConsulta", "", "pulsarAlta()");
    if('Alt+M'==tecla) ejecutarAccionBoton("tdBotonModificar", "capaBotonesConsulta", "", "pulsarModificar()");
    if('Alt+B'==tecla) ejecutarAccionBoton("tdBotonBaja", "capaBotonesConsulta", "", "pulsarBaja()");
    if('Alt+B'==tecla) ejecutarAccionBoton("tdBotonBuscar", "capaBotonesConsulta", "", "pulsarBuscar()");
    if('Alt+L'==tecla) ejecutarAccionBoton("tdBotonLimpiar", "capaBotonesConsulta", "", "pulsarLimpiar()");
    if('Alt+L'==tecla) ejecutarAccionBoton("tdBotonListado", "capaBotonesConsulta", "", "pulsarListado()");
    if('Alt+S'==tecla) ejecutarAccionBoton("tdBotonSalir", "capaBotonesConsulta", "", "pulsarSalir()");

    if('Alt+G'==tecla) ejecutarAccionBoton("tdBotonGrabar", "capaBotonesInsertar", "", "pulsarGrabar()");
    if('Alt+C'==tecla) ejecutarAccionBoton("tdBotonCancelar", "capaBotonesInsertar", "", "pulsarCancelar()");

    if('Alt+R'==tecla) ejecutarAccionBoton("tdBotonGrabarAnotacion", "capaBotonesAnotacion", "tabPage3", "pulsarGrabarAnotacion()");
    if('Alt+N'==tecla) ejecutarAccionBoton("tdBotonCancelarAnotacion", "capaBotonesAnotacion", "tabPage3", "pulsarCancelarAnotacion()");

    if('Alt+A'==tecla) ejecutarAccionBoton("tdBotonAltaAnotacion", "capaBotonesListaAnotacion", "tabPage3", "pulsarAltaAnotacion()");
    if('Alt+M'==tecla) ejecutarAccionBoton("tdBotonModificarAnotacion", "capaBotonesListaAnotacion", "tabPage3", "pulsarModificarAnotacion()");
    if('Alt+E'==tecla) ejecutarAccionBoton("tdBotonEliminarAnotacion", "capaBotonesListaAnotacion", "tabPage3", "pulsarEliminarAnotacion()");

    if('Alt+R'==tecla) ejecutarAccionBoton("tdBotonGrabarRectificacion", "capaBotonesRectificacion", "tabPage4", "pulsarGrabarRectificacion()");
    if('Alt+N'==tecla) ejecutarAccionBoton("tdBotonCancelarRectificacion", "capaBotonesRectificacion", "tabPage4", "pulsarCancelarRectificacion()");

    if('Alt+A'==tecla) ejecutarAccionBoton("tdBotonAltaRectificacion", "capaBotonesListaRectificacion", "tabPage4", "pulsarAltaRectificacion()");
    if('Alt+M'==tecla) ejecutarAccionBoton("tdBotonModificarRectificacion", "capaBotonesListaRectificacion", "tabPage4", "pulsarModificarRectificacion()");
    if('Alt+E'==tecla) ejecutarAccionBoton("tdBotonEliminarRectificacion", "capaBotonesListaRectificacion", "tabPage4", "pulsarEliminarRectificacion()");

    if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
        upDownTable(tab,lista,teclaAuxiliar);
    }
    //    if (event.keyCode == 13) buscar();
    keyDel(evento);
}

function ejecutarAccionBoton(boton, capa, pestana, accion) {
    if (document.getElementById(capa) && document.getElementById(boton)) {
        if ((pestana=="" || document.getElementById(pestana).style.display != "none") &&
            (document.getElementById(capa).style.display != "none" && document.getElementById(capa).style.visibility != "hidden") &&
            document.getElementById(boton).style.display != "none") {
            eval(accion);
        }
    }
}

document.onkeydown=checkKeys;

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
                              }" >

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form method="post" target="_self">
<html:hidden name="ConvenioUrbanisticoForm" property="tipoRegistro"/>

<!-- Datos. -->
<table width="100%" height="435px" cellpadding="0px" cellspacing="0px">
<tr>
<td width="791px" height="435px">
<table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
<tr>
<td>
<table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
<tr>
    <td id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_conUrb")%></td>
</tr>
<tr>
    <td width="100%" height="1px" bgcolor="#666666"></td>
</tr>
<tr>
    <td width="100%" height="22px" bgcolor="#DCDCCC">
        <table id="tablaBuscar" width="50%" cellpadding="0px" cellspacing="0px" border="0">
            <tr>
                <td width="18%" class="etiqueta">&nbsp;&nbsp;<%=descriptor.getDescripcion("numRegistro")%></td>
                <td width="18%" class="columnP">
                    <input type="text" class='inputTextoObligatorio' size="4" maxlength="4" name="anho"
                           onkeypress = "javascript:return SoloDigitos(event);"
                           onfocus="this.select();" />&nbsp;<span style="font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11px; font-style:normal; font-weight: normal; font-variant: normal; color: #999999;">/</span>
                    <input type="text" class='inputTexto' size="7" maxlength="6" name="numero"
                           onkeypress = "javascript:return SoloDigitos(event);"
                           onfocus="this.select();"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
<td width="100%" bgcolor="#e6e6e6" align="center" valign="top">
<!-- Separador. -->
<table height="3px" cellpadding="0px" cellspacing="0px">
    <tr>
        <td></td>
    </tr>
</table>
<!-- Fin separador. -->
<div class="tab-pane" id="tab-pane-1" >
<script type="text/javascript">
    tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
</script>
<!-- CAPA 1: DATOS GENERALES
------------------------------ -->
<div class="tab-page" id="tabPage1" style="height:270px">
<h2 class="tab"><%=descriptor.getDescripcion("plan_pestana1")%></h2>
<script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
<table id ="tablaDatosGral" width="600px" cellspacing="4px" cellpadding="1px" border="0">
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_subsec")%>:</td>
    <!-- Codigo Tipo -->
    <td width="73%" align="left">
        <input name="codigoSubseccion" type="text" class="inputTexto" id="codSubseccion" size="8"/>
        <input name="subseccion" type="text" class="inputTexto" id="descSubseccion"  style="width:320;height:17" readonly/>
        <a id="anchorSubseccion" name="anchorSubseccion" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSubseccion" name="botonSubseccion"
                style="cursor:hand;"></span></a>
    </td>
</tr>
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("conUrb_objConvenio")%>:</td>
    <!-- Objeto Convenio -->
    <td width="73%" align="left">
        <textarea id="obligatorio" class="textareaTextoObligatorio" cols="64" rows="5" name="objetoConvenio"  maxlength="2" onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);"></textarea>
    </td>
</tr>
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_ambito")%>:</td>
    <td width="73%" align="left">
        <input name="codigoAmbito" type="text" class="inputTexto" id="codAmbito" size="8"/>
        <input name="ambito" type="text" class="inputTexto" id="descAmbito"  style="width:320;height:17" readonly/>
        <a id="anchorAmbito" name="anchorAmbito" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonAmbito" name="botonAmbito"
        style="cursor:hand;"></span></a>
    </td>
</tr>
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_parcela")%>:</td>
    <td width="73%" align="left">
        <input name="parcela" type="text" class="inputTexto" id="parcela" style="width:406;" maxlength="75" onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
</tr>
<tr valign="center">
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("conUrb_firmantes")%>:</td>
    <!-- Codigo Tipo -->
    <td width="73%" align="left"><table width="100%" border="0" align="left" style="position: relative; left: -3px;">
            <tr>
                <td class="textoSuelto" align="left">
                    <bean:write name="ConvenioUrbanisticoForm" property="firmante" scope="session"/>
                </td>
            </tr>
            <tr>
                <td align="left">
                    <input type="text" class="inputTextoObligatorio" size="62" name="nombreFirmante" id="obligatorio" readOnly/>
                    <span class="fa fa-search" aria-hidden="true" id="terceros"  name="botonT" alt="Buscar Firmanter" onclick="javascript:pulsarFirmantes();" style="cursor: hand;"></span>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_organo")%>:</td>
    <!-- Codigo Tipo -->
    <td width="73%" align="left">
        <input name="codigoOrganoAprobacion" type="text" class="inputTexto" id="codOrganoAprobacion" size="8"/>
        <input name="organoAprobacion" type="text" class="inputTexto" id="descOrganoAprobacion"  style="width:320;height:17" readonly/>
        <a id="anchorOrganoAprobacion" name="anchorOrganoAprobacion" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOrganoAprobacion" name="botonOrganoAprobacion"
                style="cursor:hand;"></span></a>
    </td>
</tr>
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_fecAprob")%>:</td>
    <td width="33%" align="left">
        <input name="fechaAprobacion" type="text" class="inputTxtFechaObligatorio" size="18"
               onkeypress = "javascript:if (consultando) return soloCaracteresFechaConsultando(event); else return soloCaracteresFecha(event);"
               onblur = "javascript:return comprobarFecha(this);"
               onfocus = "this.select();"/>
        &nbsp;
        <a href="javascript:calClick();return false;" id="anchorFechaAprobacion" onClick="mostrarCalFechaAprobacion();return false;" style="text-decoration:none;">
            <span class="fa fa-calendar" aria-hidden="true" name="calFechaAprobacion" alt="Data" ></span>
        </a>
    </td>
</tr>
<tr id="trFechaBaja">
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_fecBaja")%>:</td>
    <td width="33%" align="left">
        <input name="fechaBaja" type="text" class="inputTxtFecha" size="18"
               onkeypress = "javascript:if (consultando) return soloCaracteresFechaConsultando(event); else return soloCaracteresFecha(event);"
               onblur = "javascript:return comprobarFecha(this);"
               onfocus = "this.select();"/>
        &nbsp;
        <a href="javascript:calClick();return false;" id="anchorFechaBaja" onClick="mostrarCalFechaBaja();return false;" style="text-decoration:none;">
            <span class="fa fa-calendar" aria-hidden="true" name="calFechaBaja" alt="Data" ></span>
        </a>
    </td>
</tr>
</table>
</div>
<div class="tab-page" id="tabPage2" style="height:270px">
    <h2 class="tab"><%=descriptor.getDescripcion("plan_pestana2")%></h2>
    <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
    <table id ="tablaNotasMarginales" width="600px" cellspacing="4px" cellpadding="1px" border="0">
        <tr>
            <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_fecPubli")%>:</td>
            <td width="33%" align="left">
                <input name="fechaPublicacion" type="text" class="inputTxtFecha" size="18"
                       onkeypress = "javascript:if (consultando) return soloCaracteresFechaConsultando(event); else return soloCaracteresFecha(event);"
                       onblur = "javascript:return comprobarFecha(this);"
                       onfocus = "this.select();"/>
                &nbsp;
                <a href="javascript:calClick();return false;" id="anchorFechaPublicacion" onClick="mostrarCalFechaPublicacion();return false;" style="text-decoration:none;">
                    <span class="fa fa-calendar" aria-hidden="true" name="calFechaPublicacion" alt="Data" ></span>
                </a>
            </td>
        </tr>
        <tr>
            <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("numPublicacion")%></td>
            <td width="73%">
                <input type="text" class='inputTexto' style="width:320;height:17" maxlength="50" name="numeroPublicacion" onfocus="this.select();" />
            </td>
        </tr>
        <tr>
            <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_observ")%></td>
            <td width="73%">
                <textarea class="textareaTexto" cols="64" rows="5" name="observaciones"  onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);"></textarea>
            </td>
        </tr>
        <tr>
            <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_arch")%></td>
            <td width="73%">
                <textarea class="textareaTexto" cols="64" rows="5" name="archivo"  onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);"></textarea>
            </td>
        </tr>
    </table>
</div>
<div class="tab-page" id="tabPage3" style="height:270px">
<h2 class="tab"><%=descriptor.getDescripcion("plan_pestana3")%></h2>
<script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
<table id ="tablaAnotaciones" width="600px" cellspacing="4px" cellpadding="1px" border="0" style="display: none;">
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_fecAnotacion")%>:</td>
    <td width="33%" align="left">
        <input type="hidden" name="numeroAnotacion"/>
        <input name="fechaAnotacion" type="text" class="inputTxtFecha" size="18"
               onkeypress = "javascript:if (consultando) return soloCaracteresFechaConsultando(event); else return soloCaracteresFecha(event);"
               onblur = "javascript:return comprobarFecha(this);"
               onfocus = "this.select();"/>
        &nbsp;
        <a href="javascript:calClick();return false;" onClick="mostrarCalFechaAnotacion();return false;" style="text-decoration:none;" id="anchorFechaAnotacion">
            <span class="fa fa-calendar" aria-hidden="true" name="calFechaAnotacion" alt="Data" ></span>
        </a>
    </td>
</tr>
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("comAnotacion")%></td>
    <td width="73%">
        <textarea class="textareaTexto" cols="64" rows="5" name="comentarioAnotacion"  onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);" value=""></textarea>
    </td>
</tr>
<tr>
<td colspan="2">
    <div id="capaBotonesAnotacion" STYLE="position:absolute; width:567px; height:0px; visibility:hidden; ">
        <table cellpadding="0px" cellspacing="0px" align="right">
            <tr>
                <td width="2px"></td>
                <!-- Botón GRABAR. -->
                <td id="tdBotonGrabarAnotacion">
                    <table cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td width="100px" height="21px">
                                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                                    <tr>
                                        <td width="100%" height="100%">
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                                <tr>
                                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabar" onClick="pulsarGrabarAnotacion();return false;"/></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Sombra lateral. -->
                            <td width="1px" height="23px">
                                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td height="1px" bgcolor="#B0BDCD"></td>
                                    </tr>
                                    <tr>
                                        <td height="22px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Fin sombra lateral. -->
                        </tr>
                        <!-- Sombra inferior. -->
                        <tr>
                            <td colspan="2" width="101px" height="1px">
                                <table cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                                        <td width="100px" height="1px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- Fin sombra inferior. -->
                    </table>
                </td>
                <!-- Fin botón GRABAR. -->
                <td width="2px"></td>
                <!-- Botón CANCELAR. -->
                <td id="tdBotonCancelarAnotacion">
                    <table cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td width="100px" height="21px">
                                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                                    <tr>
                                        <td width="100%" height="100%">
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                                <tr>
                                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bCancelar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onClick="pulsarCancelarAnotacion();return false;"/></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Sombra lateral. -->
                            <td width="1px" height="23px">
                                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td height="1px" bgcolor="#B0BDCD"></td>
                                    </tr>
                                    <tr>
                                        <td height="22px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Fin sombra lateral. -->
                        </tr>
                        <!-- Sombra inferior. -->
                        <tr>
                            <td colspan="2" width="101px" height="1px">
                                <table cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                                        <td width="100px" height="1px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- Fin sombra inferior. -->
                    </table>
                </td>
                <!-- Fin botón CANCELAR. -->
            </tr>
        </table>
    </div>
    <!-- Fin botones CONSULTA. -->
</td>
</tr>
</table>
<table id ="tabListaAnotaciones" width="600px" cellspacing="4px" cellpadding="1px" border="0" style="display: block;">
<tr>
    <td colspan="2" id="tablaListaAnotaciones"></td>
</tr>
<tr>
<td colspan="2">
<div id="capaBotonesListaAnotacion" STYLE="position:absolute; width:567px; height:0px; visibility:hidden; ">
<table cellpadding="0px" cellspacing="0px" align="right">
<tr>
<td width="2px"></td>
<!-- Botón ALTA. -->
<td id="tdBotonAltaAnotacion">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAltaAnotacion" onClick="pulsarAltaAnotacion();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón ALTA. -->
<td width="2px"></td>
<!-- Botón MODIFICAR. -->
<td id="tdBotonModificarAnotacion">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificarAnotacion" onClick="pulsarModificarAnotacion();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón MODIFICAR. -->
<!-- Botón ELIMINAR. -->
<td id="tdBotonEliminarAnotacion">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>' name="cmdEliminarAnotacion" onClick="pulsarEliminarAnotacion();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón ELIMINAR. -->
</tr>
</table>
</div>
<!-- Fin botones CONSULTA. -->
</td>
</tr>
</table>
</div>
<div class="tab-page" id="tabPage4" style="height:270px;">
<h2 class="tab"><%=descriptor.getDescripcion("plan_pestana4")%></h2>
<script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
<table id ="tablaRectificaciones" width="600px" cellspacing="4px" cellpadding="1px" border="0" style="display: none;">
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("insPlan_fecRectific")%>:</td>
    <td width="33%" align="left">
        <input type="hidden" name="numeroRectificacion"/>
        <input name="fechaRectificacion" type="text" class="inputTxtFecha" size="18"
               onkeypress = "javascript:if (consultando) return soloCaracteresFechaConsultando(event); else return soloCaracteresFecha(event);"
               onblur = "javascript:return comprobarFecha(this);"
               onfocus = "this.select();"/>
        &nbsp;
        <a href="javascript:calClick();return false;" onClick="mostrarCalFechaRectificacion();return false;" style="text-decoration:none;" id="anchorFechaRectificacion">
            <span class="fa fa-calendar" aria-hidden="true" name="calFechaRectificacion" alt="Data" ></span>
        </a>
    </td>
</tr>
<tr>
    <td width="27%" class="etiqueta"><%=descriptor.getDescripcion("comRectificacion")%></td>
    <td width="73%">
        <textarea class="textareaTexto" cols="64" rows="5" name="comentarioRectificacion"  onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);" value=""></textarea>
    </td>
</tr>
<tr>
<td colspan="2">
    <div id="capaBotonesRectificacion" STYLE="position:absolute; width:567px; height:0px; visibility:hidden; ">
        <table cellpadding="0px" cellspacing="0px" align="right">
            <tr>
                <td width="2px"></td>
                <!-- Botón GRABAR. -->
                <td id="tdBotonGrabarRectificacion">
                    <table cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td width="100px" height="21px">
                                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                                    <tr>
                                        <td width="100%" height="100%">
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                                <tr>
                                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabar" onClick="pulsarGrabarRectificacion();return false;"/></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Sombra lateral. -->
                            <td width="1px" height="23px">
                                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td height="1px" bgcolor="#B0BDCD"></td>
                                    </tr>
                                    <tr>
                                        <td height="22px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Fin sombra lateral. -->
                        </tr>
                        <!-- Sombra inferior. -->
                        <tr>
                            <td colspan="2" width="101px" height="1px">
                                <table cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                                        <td width="100px" height="1px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- Fin sombra inferior. -->
                    </table>
                </td>
                <!-- Fin botón GRABAR. -->
                <td width="2px"></td>
                <!-- Botón CANCELAR. -->
                <td id="tdBotonCancelarRectificacion">
                    <table cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td width="100px" height="21px">
                                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                                    <tr>
                                        <td width="100%" height="100%">
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                                <tr>
                                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bCancelar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onClick="pulsarCancelarRectificacion();return false;"/></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Sombra lateral. -->
                            <td width="1px" height="23px">
                                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td height="1px" bgcolor="#B0BDCD"></td>
                                    </tr>
                                    <tr>
                                        <td height="22px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                            <!-- Fin sombra lateral. -->
                        </tr>
                        <!-- Sombra inferior. -->
                        <tr>
                            <td colspan="2" width="101px" height="1px">
                                <table cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                                        <td width="100px" height="1px" bgcolor="#333333"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- Fin sombra inferior. -->
                    </table>
                </td>
                <!-- Fin botón CANCELAR. -->
            </tr>
        </table>
    </div>
    <!-- Fin botones CONSULTA. -->
</td>
</tr>
</table>
<table id ="tabListaRectificaciones" width="600px" cellspacing="4px" cellpadding="1px" border="0" style="display: block;">
<tr>
    <td colspan="2" id="tablaListaRectificaciones"></td>
</tr>
<tr>
<td colspan="2">
<div id="capaBotonesListaRectificacion" STYLE="position:absolute; width:567px; height:0px; visibility:hidden; ">
<table cellpadding="0px" cellspacing="0px" align="right">
<tr>
<td width="2px"></td>
<!-- Botón ALTA. -->
<td id="tdBotonAltaRectificacion">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAltaRectificacion" onClick="pulsarAltaRectificacion();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón ALTA. -->
<td width="2px"></td>
<!-- Botón MODIFICAR. -->
<td id="tdBotonModificarRectificacion">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificarRectificacion" onClick="pulsarModificarRectificacion();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón MODIFICAR. -->
<!-- Botón ELIMINAR. -->
<td id="tdBotonEliminarRectificacion">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>' name="cmdEliminarRectificacion" onClick="pulsarEliminarRectificacion();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón ELIMINAR. -->
</tr>
</table>
</div>
<!-- Fin botones CONSULTA. -->
</td>
</tr>
</table>
</div>
</div>
<table id="tabla1" width="631px" cellspacing="0px" cellpadding="0px" border="0px" bgcolor="#e6e6e6">
    <tr>
        <td colspan="2" width="100%" align="left" valign="top">
            <div id="capaRadioButtons" name="capaRadioButtons" STYLE="position:absolute; width:100%; height:0px;">
                <table width="100%" height="28px" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px"></td>
                        <td align="left" valing="top">
                            <table width="200px" class="subsubtitulo" cellspacing="0px" cellpadding="0px" border="0px">
                                <tr>
                                    <td width="100%" class="textoSuelto">
                                        <input type="radio" name="tipoConsulta" class="textoSuelto" value="listado" CHECKED> <%=descriptor.getDescripcion("gEtiqListReg")%></input>
                                        <br>
                                        <input type="radio" name="tipoConsulta" class="textoSuelto" value="registro" > <%=descriptor.getDescripcion("gEtiqRegAReg")%></input>
                                      </td>
                                  </tr>
                              </table>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td width="5px"></td>
    </tr>
    <!-- ----------------------- NAVEGACIÓN ---------------------------- -->
    <tr>
        <td width="100%" align="center" valign="top">
            <div id="capaNavegacionConsulta" name="capaNavegacionConsulta" style="height:30px;"></DIV>
        </td>
    </tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>
<div id="capaBotonesConsulta" name="capaBotonesConsulta" STYLE="position:absolute; width:100%; height:0px; visibility:hidden; ">
<table cellpadding="0px" cellspacing="0px" align="right">
<tr>
<td width="2px" id="tdEspacioBotonAlta"></td>
<!-- Botón ALTA. -->
<td id="tdBotonAlta">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAlta" onClick="pulsarAlta();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón ALTA. -->
<td width="2px" id="tdEspacioBotonModificar"></td>
<!-- Botón MODIFICAR. -->
<td id="tdBotonModificar">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificar" onClick="pulsarModificar();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón MODIFICAR. -->
<td width="2px" id="tdEspacioBotonBaja"></td>
<!-- Botón BAJA. -->
<td id="tdBotonBaja">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bBaja")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbBaja")%>' name="cmdBaja" onClick="pulsarBaja();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón BAJA. -->
<td width="2px" id="tdEspacioBotonBuscar"></td>
<!-- Botón BUSCAR. -->
<td id="tdBotonBuscar">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbBuscar")%>' name="cmdBuscar" onClick="pulsarBuscar();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón BUSCAR. -->
<td width="2px" id="tdEspacioBotonLimpiar"></td>
<!-- Botón LIMPIAR. -->
<td id="tdBotonLimpiar">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="pulsarLimpiar();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón LIMPIAR. -->
<%--<td width="2px" id="tdEspacioBotonCancelarSalir"></td>
<!-- Botón CANCELARSALIR. -->
<td id="tdBotonCancelarSalir">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bCancelar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarSalir" onClick="pulsarCancelarSalir();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón CANCELARSALIR. -->
--%>
<td width="2px" id="tdEspacioBotonListado"></td>
<!-- Botón LISTADO. -->
<td id="tdBotonListado">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bListado")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbListado")%>' name="cmdListado" onClick="pulsarListado();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón LISTADO. -->
<td width="2px"></td>
<!-- Botón SALIR. -->
<td id="tdBotonSalir">
    <table cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="100px" height="21px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td width="100%" height="100%">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();return false;"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <!-- Sombra lateral. -->
            <td width="1px" height="23px">
                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td height="1px" bgcolor="#B0BDCD"></td>
                    </tr>
                    <tr>
                        <td height="22px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
            <!-- Fin sombra lateral. -->
        </tr>
        <!-- Sombra inferior. -->
        <tr>
            <td colspan="2" width="101px" height="1px">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                        <td width="100px" height="1px" bgcolor="#333333"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Fin sombra inferior. -->
    </table>
</td>
<!-- Fin botón SALIR. -->
</tr>
</table>
</div>
<!-- Fin botones CONSULTA. -->
<div id="capaBotonesInsertar" name="capaBotonesInsertar" STYLE="position:absolute; width:100%; height:0px; visibility:hidden; ">
    <table cellpadding="0px" cellspacing="0px" align="right">
        <tr>
            <td width="2px"></td>
            <!-- Botón GRABAR. -->
            <td id="tdBotonGrabar">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="100px" height="21px">
                            <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                                <tr>
                                    <td width="100%" height="100%">
                                        <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabar" onClick="pulsarGrabar();return false;"/></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <!-- Sombra lateral. -->
                        <td width="1px" height="23px">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td height="1px" bgcolor="#B0BDCD"></td>
                                </tr>
                                <tr>
                                    <td height="22px" bgcolor="#333333"></td>
                                </tr>
                            </table>
                        </td>
                        <!-- Fin sombra lateral. -->
                    </tr>
                    <!-- Sombra inferior. -->
                    <tr>
                        <td colspan="2" width="101px" height="1px">
                            <table cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                                    <td width="100px" height="1px" bgcolor="#333333"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <!-- Fin sombra inferior. -->
                </table>
            </td>
            <!-- Fin botón GRABAR. -->
            <td width="2px"></td>
            <!-- Botón CANCELAR. -->
            <td id="tdBotonCancelar">
                <table cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="100px" height="21px">
                            <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                                <tr>
                                    <td width="100%" height="100%">
                                        <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td bgcolor="#ffffff" width="100%" height="100%"><input type="button" title='<%=descriptor.getDescripcion("toolTip_bCancelar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onClick="pulsarCancelar();return false;"/></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <!-- Sombra lateral. -->
                        <td width="1px" height="23px">
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td height="1px" bgcolor="#B0BDCD"></td>
                                </tr>
                                <tr>
                                    <td height="22px" bgcolor="#333333"></td>
                                </tr>
                            </table>
                        </td>
                        <!-- Fin sombra lateral. -->
                    </tr>
                    <!-- Sombra inferior. -->
                    <tr>
                        <td colspan="2" width="101px" height="1px">
                            <table cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td width="1px" height="1px" bgcolor="#B0BDCD"></td>
                                    <td width="100px" height="1px" bgcolor="#333333"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <!-- Fin sombra inferior. -->
                </table>
            </td>
            <!-- Fin botón CANCELAR. -->
        </tr>
    </table>
</div>

</form>


</BODY>

<script type="text/javascript">

    var comboSubsecciones;
    var comboAmbitos;
    var comboOrganosAprobacion;

    function creaCombos() {
        var cont = 0;
        var listaCod = new Array();
        var listaDesc = new Array();

        comboSubsecciones = new Combo("Subseccion");
        comboAmbitos = new Combo("Ambito");
        comboOrganosAprobacion = new Combo("OrganoAprobacion");

    <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="subsecciones" scope="session">
        listaCod[cont] = ['<bean:write name="elemento" property="codigo" />'];
        listaDesc[cont] = ['<bean:write name="elemento" property="descripcion"/>'];
        cont = cont + 1;
    </logic:iterate>
        comboSubsecciones.addItems(listaCod, listaDesc);

        cont = 0;
        listaCod = new Array();
        listaDesc = new Array();
    <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="ambitos" scope="session">
        listaCod[cont] = ['<bean:write name="elemento" property="codigo" />'];
        listaDesc[cont] = ['<bean:write name="elemento" property="descripcion"/>'];
        cont = cont + 1;
    </logic:iterate>
        comboAmbitos.addItems(listaCod, listaDesc);

        cont = 0;
        listaCod = new Array();
        listaDesc = new Array();
    <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="organosAprobacion" scope="session">
        listaCod[cont] = ['<bean:write name="elemento" property="codigo" />'];
        listaDesc[cont] = ['<bean:write name="elemento" property="descripcion"/>'];
        cont = cont + 1;
    </logic:iterate>
        comboOrganosAprobacion.addItems(listaCod, listaDesc);
    }
</script>

<script type="text/javascript">
    // JAVASCRIPT DE LA TABLA ANOTACIONES

    var tabAnotacion;
    var tabRectificacion;
    if(document.all) tabAnotacion = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tablaListaAnotaciones);
    else tabAnotacion = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaListaAnotaciones'));
    if(document.all) tabRectificacion = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tablaListaRectificaciones);
    else tabRectificacion = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaListaRectificaciones'));

    tabAnotacion.addColumna('60','left','<%= descriptor.getDescripcion("etiq_numAnot")%>');
    tabAnotacion.addColumna('70','left','<%= descriptor.getDescripcion("etiq_fecha")%>');
    tabAnotacion.addColumna('440','left','<%= descriptor.getDescripcion("etiq_Comentario")%>');
    tabAnotacion.displayCabecera=true;
    tabAnotacion.height = 190;

    tabRectificacion.addColumna('60','left','<%= descriptor.getDescripcion("etiq_numAnot")%>');
    tabRectificacion.addColumna('70','left','<%= descriptor.getDescripcion("etiq_fecha")%>');
    tabRectificacion.addColumna('440','left','<%= descriptor.getDescripcion("etiq_Comentario")%>');
    tabRectificacion.displayCabecera=true;
    tabRectificacion.height = 190;

    function cargaTabla(){
        var cont = 0;

        lista = new Array();
        var comentario;
        var busca = /@intro@/gi;
        var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
    <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="anotaciones" scope="session">
        comentario = '<bean:write name="elemento" property="comentarioAnotacion"/>';
        comentario = comentario.replace(busca,pStrReemplaza);
        lista[cont] = ['<bean:write name="elemento" property="numeroAnotacion" />', '<bean:write name="elemento" property="fechaAnotacion" />', comentario];
        cont = cont + 1;
    </logic:iterate>

        tabAnotacion.lineas=lista;
        refrescaAnotaciones();
    }

    var tableObject=tabAnotacion;

    function refrescaAnotaciones() {
        tabAnotacion.displayTabla();
    }

    tabAnotacion.displayDatos = pintaDatosAnotaciones;

    function pintaDatosAnotaciones() {
        tableObject = tabAnotacion;
    }

    function cargaTablaRectificacion(){
        var cont = 0;

        listaRectificacion = new Array();
        var comentario;
        var busca = /@intro@/gi;
        var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
    <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="rectificaciones" scope="session">
        comentario = '<bean:write name="elemento" property="comentarioRectificacion"/>';
        comentario = comentario.replace(busca,pStrReemplaza);
        listaRectificacion[cont] = ['<bean:write name="elemento" property="numeroRectificacion" />', '<bean:write name="elemento" property="fechaRectificacion" />', comentario];
        cont = cont + 1;
    </logic:iterate>

        tabRectificacion.lineas=listaRectificacion;
        refrescaRectificaciones();
    }

    var tableObjectRectificacion=tabRectificacion;

    function refrescaRectificaciones() {
        tabRectificacion.displayTabla();
    }

    tabRectificacion.displayDatos = pintaDatosRectificaciones;

    function pintaDatosRectificaciones() {
        tableObjectRectificacion = tabRectificacion;
    }

</script>

<script> Inicio(); </script>

</body>

</html:html>
