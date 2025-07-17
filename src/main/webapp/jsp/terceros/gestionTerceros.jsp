<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="es.altia.agora.business.terceros.TercerosValueObject" %>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.agora.technical.EstructuraCampo"%>
<%@page import="es.altia.agora.technical.CamposFormulario"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<html>
<head>    
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>Gestión de terceros</title>
<%
boolean bAltaViaDirecta = false;
ParametrosTerceroValueObject ptVO = null;
int idioma = 1;
int apl = 3;
int cod_org = 1;
int cod_dep = 1;
int entCod = 1;
String css = "";
int aplicacionOrigen=3;
String funcion = "";
String inicioTercero = "";
String origen = (String) session.getAttribute("origen");
String existeTramero = "";
String inicioAlta = "";
String inicioAltaDesdeBuscar = "";
String inicioModificar = "";
String cargarTercero = "";
String seleccionDesdeInformes = "";
String soloTerceroFlexia="no";

if (session.getAttribute("usuario") != null) {
    UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
    ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
    if(ptVO == null){
        ptVO = new ParametrosTerceroValueObject();
        ptVO.setPais("108"); //Código España
        ptVO.setProvincia("99"); //codigo_provincia_desconocido
        ptVO.setMunicipio("999"); //codigo_municipio_desconocido
    }
    idioma = usuarioVO.getIdioma();
    cod_org = usuarioVO.getOrgCod();
    cod_dep = usuarioVO.getDepCod();
    entCod = usuarioVO.getEntCod();
    css = usuarioVO.getCss();
    aplicacionOrigen=usuarioVO.getAppCod();        
}

// #228743
String actualizarDom = null;
if(request.getAttribute("actualizarDom")!=null){
    actualizarDom = (String) request.getAttribute("actualizarDom");
}


if (session.getAttribute("soloTerceroFlexia")!=null)
            soloTerceroFlexia = (String) session.getAttribute("soloTerceroFlexia");

// El atributo 'inicioTercero' esta presente en la sesión cuando se carga
// la página mediante 'BusquedaTercerosAction.do?opcion=inicializarTerc'
// (desde el módulo de gestión de terceros) Desde registro o expedientes
// tendremos inicioTercero="no".
if (session.getAttribute("inicioTercero") != null) {
    inicioTercero = (String) session.getAttribute("inicioTercero");
} else {
    inicioTercero = "no";
}
session.removeAttribute("inicioTercero");
Log _log = LogFactory.getLog(this.getClass());
if (_log.isDebugEnabled()) {
    _log.debug("terceros/gestionTerceros.jsp: session->inicioTercero = " + inicioTercero);
}

// Otros atributos de la sesion
if (session.getAttribute("inicioAlta") != null) {
    inicioAlta = (String) session.getAttribute("inicioAlta");
} else {
    inicioAlta = "no";
}
session.removeAttribute("inicioAlta");

if (session.getAttribute("inicioAltaDesdeBuscar") != null) {
    inicioAltaDesdeBuscar = (String) session.getAttribute("inicioAltaDesdeBuscar");
} else {
    inicioAltaDesdeBuscar = "no";
}
session.removeAttribute("inicioAltaDesdeBuscar");

if (session.getAttribute("seleccionDesdeInformes") != null) {
    seleccionDesdeInformes = (String) session.getAttribute("seleccionDesdeInformes");
} else {
    seleccionDesdeInformes = "no";
}
session.removeAttribute("seleccionDesdeInformes");

if (session.getAttribute("inicioModificar") != null) {
    inicioModificar = (String) session.getAttribute("inicioModificar");
} else {
    inicioModificar = "no";
}
session.removeAttribute("inicioModificar");

if (session.getAttribute("cargarTercero") != null) {
    cargarTercero = (String) session.getAttribute("cargarTercero");
} else {
    cargarTercero = "no";
}
session.removeAttribute("cargarTercero");

// Parametros de configuracion
Config m_Conf = ConfigServiceHelper.getConfig("common");
String JSP_altaViaDirecta = m_Conf.getString("JSP.altaViaDirecta");
String JSP_entidadColectiva = m_Conf.getString("JSP.EntidadColectiva");
String JSP_entidadSingular = m_Conf.getString("JSP.EntidadSingular");
String JSP_emplazamiento = m_Conf.getString("JSP.Emplazamiento");
String JSP_poblacion = m_Conf.getString("JSP.Poblacion");
String CodigoSinTipoVia = m_Conf.getString("T_TVI.CodigoSinTipoVia");

Config confTerceros = ConfigServiceHelper.getConfig("Terceros");
String mostrarOrigen = confTerceros.getString("Terceros.mostrarOrigen");

// Activar/desactivar elementos del formulario segun configuracion
String ecv = "block";
String esv = "block";
String emplv = "block";
String pv = "block";
if ("no".equals(JSP_entidadColectiva)) {
    ecv = "none";
}
if ("no".equals(JSP_entidadSingular)) {
    esv = "none";
}
if ("no".equals(JSP_emplazamiento)) {
    emplv = "none";
    pv = "inline";
}
if ("no".equals(JSP_poblacion)) {
    pv = "none";
    if (!emplv.equals("none")) {
        emplv = "inline";
    }
}

StringTokenizer st = new StringTokenizer(JSP_altaViaDirecta, ",");
while (st.hasMoreTokens()) {
    if (st.nextToken().trim().equals(String.valueOf(cod_org))) {
        bAltaViaDirecta = true;
    }
}

String statusBar = m_Conf.getString("JSP.StatusBar");

// LISTAS PARA LOS COMBOS
BusquedaTercerosForm bForm = (BusquedaTercerosForm) session.getAttribute("BusquedaTercerosForm");
Vector listaDocs = bForm.getListaTipoDocs();
Vector listaProvincias = bForm.getListaProvincias();
Vector listaMunicipios = bForm.getListaMunicipios();
Vector listaUsoViviendas = bForm.getListaUsoViviendas();
Vector listaCodPostales = bForm.getListaCodPostales();
String permitirAltaTerceroSinDomicilio = bForm.getPermitirAltaTerceroSinDomicilio();
_log.debug(" ---- permitirAltaTerceroSinDomicilio:: " + permitirAltaTerceroSinDomicilio);

if (_log.isDebugEnabled()) {
    _log.debug("el tamaño de la lista de municipios es : " + listaMunicipios.size());
}

int lengthDocs = listaDocs.size();
int lengthProvs = listaProvincias.size();
int lengthMun = listaMunicipios.size();
int lengthUsoViviendas = listaUsoViviendas.size();
int lengthCodPostales = listaCodPostales.size();
int i = 0;

String lcodProv = "";
String ldescProv = "";
String lcodMun = "";
String ldescMun = "";
String lcodDocs = "";
String ldescDocs = "";
String lpersJF = "";
String lcodTOC = "";
String ldescTOC = "";
String lcodCP = "";

// Tipos de documento
if (lengthDocs > 0) {
    for (i = 0; i < lengthDocs - 1; i++) {
        GeneralValueObject doc = (GeneralValueObject) listaDocs.get(i);
        lcodDocs += "'" + (String) doc.getAtributo("codTipoDoc") + "',";
        ldescDocs += "'" + escape((String) doc.getAtributo("descTipoDoc")) + "',";
        lpersJF += "'" + (String) doc.getAtributo("persFJ") + "',";
    }
    GeneralValueObject doc = (GeneralValueObject) listaDocs.get(i);
    lcodDocs += "'" + (String) doc.getAtributo("codTipoDoc") + "'";
    ldescDocs += "'" + escape((String) doc.getAtributo("descTipoDoc")) + "'";
    lpersJF += "'" + (String) doc.getAtributo("persFJ") + "'";
}

// Provincias y municipios
if (lengthProvs > 0) {
    for (i = 0; i < lengthProvs - 1; i++) {
        GeneralValueObject prov = (GeneralValueObject) listaProvincias.get(i);
        lcodProv += "\"" + (String) prov.getAtributo("codigo") + "\",";
        ldescProv += "\"" + escape((String) prov.getAtributo("descripcion")) + "\",";
    }
    GeneralValueObject prov = (GeneralValueObject) listaProvincias.get(i);
    lcodProv += "\"" + (String) prov.getAtributo("codigo") + "\"";
    ldescProv += "\"" + escape((String) prov.getAtributo("descripcion")) + "\"";
}

if (lengthMun > 0) {
    for (i = 0; i < lengthMun - 1; i++) {
        GeneralValueObject mun = (GeneralValueObject) listaMunicipios.get(i);
        lcodMun += "\"" + (String) mun.getAtributo("codMunicipio") + "\",";
        ldescMun += "\"" + escape((String) mun.getAtributo("nombreOficial")) + "\",";
    }
    GeneralValueObject mun = (GeneralValueObject) listaMunicipios.get(i);
    lcodMun += "\"" + (String) mun.getAtributo("codMunicipio") + "\"";
    ldescMun += "\"" + escape((String) mun.getAtributo("nombreOficial")) + "\"";
}

// Codigos postales
if (lengthCodPostales > 0) {
    for (i = 0; i < lengthCodPostales - 1; i++) {
        GeneralValueObject cp = (GeneralValueObject) listaCodPostales.get(i);
        lcodCP += "'" + cp.getAtributo("codPostal") + "',";
    }
    GeneralValueObject cp = (GeneralValueObject) listaCodPostales.get(i);
    lcodCP += "'" + cp.getAtributo("codPostal") + "'";
}

// ECOs y ESIs
Vector listaECOs = bForm.getListaECOs();
Vector listaESIs = bForm.getListaESIs();
int lengthECOs = listaECOs.size();
int lengthESIs = listaESIs.size();
String lcodECOS = "";
String ldescECOS = "";
String lcodESIS = "";
String ldescESIS = "";
String lECOESIS = "";
i = 0;

if (lengthECOs > 0) {
    for (i = 0; i < lengthECOs - 1; i++) {
        GeneralValueObject eco = (GeneralValueObject) listaECOs.get(i);
        lcodECOS += "\"" + (String) eco.getAtributo("codECO") + "\",";
        ldescECOS += "\"" + escape((String) eco.getAtributo("descECO")) + "\",";
    }
    GeneralValueObject eco = (GeneralValueObject) listaECOs.get(i);
    lcodECOS += "\"" + (String) eco.getAtributo("codECO") + "\"";
    ldescECOS += "\"" + escape((String) eco.getAtributo("descECO")) + "\"";
}

if (lengthESIs > 0) {
    for (i = 0; i < lengthESIs - 1; i++) {
        GeneralValueObject esi = (GeneralValueObject) listaESIs.get(i);
        lcodESIS += "\"" + (String) esi.getAtributo("codEntidadSingular") + "\",";
        ldescESIS += "\"" + escape((String) esi.getAtributo("nombreOficial")) + "\",";
        lECOESIS += "[\"" + (String) esi.getAtributo("codEntidadColectiva") + "\",\"" + escape((String) esi.getAtributo("descEntidadColectiva")) + "\"],";
    }
    GeneralValueObject esi = (GeneralValueObject) listaESIs.get(i);
    lcodESIS += "\"" + (String) esi.getAtributo("codEntidadSingular") + "\"";
    ldescESIS += "\"" + escape((String) esi.getAtributo("nombreOficial")) + "\"";
    lECOESIS += "[\"" + (String) esi.getAtributo("codEntidadColectiva") + "\",\"" + escape((String) esi.getAtributo("descEntidadColectiva")) + "\"]";
}

String userAgent = request.getHeader("user-agent");
String sizeTxtInteresado="";
if(userAgent.indexOf("MSIE")!=-1) {
    // Internet Explorer
    sizeTxtInteresado = "123";

}else{
    // Firefox u otro navegador
    sizeTxtInteresado = "122";
}

 /************* Se comprueba cual es el navegador utilizado por el usuario ******************/

String PERMITIR_ALTATERCERO_SINDOMICILIO = bForm.getPermitirAltaTerceroSinDomicilio();

// Acceso al fichero de configuración common.properties
Config m_Config = ConfigServiceHelper.getConfig("common");
String campos = "";

String PANTALLA_DATOS_SUPLEMENTARIOS_ACTIVADA = bForm.getPantallaDatosSuplementariosTerceroActivada();
%>

<%!
// Funcion para escapar strings para javascript
private String escape(String str) {
    return StringEscapeUtils.escapeJavaScript(str);
  }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"	property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"	property="apl_cod" value="<%=apl%>"	/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js?<%=System.currentTimeMillis()%>"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>?<%=System.currentTimeMillis()%>" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/gestionTerceros.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js?<%=System.currentTimeMillis()%>"></script>

<script type="text/javascript">
// VARIABLES GLOBALES
var operadorConsulta = '|&:<>!=';
var ventana = true;
var nombreViaGeneral ="";

// Modos de operacion
var busqueda = 0;
var seleccion = 1;
var alta = 2;
var modif = 3;
var altaDom = 4;
var modoActual;
// Usada para impedir la recarga automatica de los combos cuando
// se cargan manualmente al modificar
var cargandoModificar = false;

// Para navegacion
var datosTabla = new Array();
var lineasPagina   = 10;
var paginaActual   = 1;

// Indices del tercero y domicilio seleccionados
var domicilioActual = -1;
var terceroActual = -1;

// LISTAS DE VALORES
var codTipoDocs = [<%=lcodDocs%>];
var descTipoDocs = [<%=ldescDocs%>];
var persFJ = [<%=lpersJF%>];
var codProvincias = [<%=lcodProv%>];
var descProvincias = [<%=ldescProv%>];
var codMunicipios = [<%=lcodMun%>];
var codMunicipiosDefecto = [<%=lcodMun%>];
var descMunicipios = [<%=ldescMun%>];
var descMunicipiosDefecto = [<%=ldescMun%>];
var codPostales = [<%=lcodCP%>];
var codECOs= [<%=lcodECOS%>];
var descECOs= [<%=ldescECOS%>];
var codESIs= [<%=lcodESIS%>];
var descESIs= [<%=ldescESIS%>];
var listaECOESIs = [<%=lECOESIS%>];
var nombreCombos = new Array();
var nombreCamposSupNoCombos = new Array();
var datosSuplementarios = new Array();
var CARGAR_DATOS_SUPLEMENTARIOS = "<%=PANTALLA_DATOS_SUPLEMENTARIOS_ACTIVADA%>";

// #228743: variable que indica si hay cambios en el tercero
var existenCambios = false;
        
function modificaVariableCambiosCamposSupl(){
    // Esta función no hace nada, simplemente se usa porque se llamada desde CampoFecha.jsp para cargar los capos suplementarios de tipo fecha
    
}

function inicio(){
       cargarCombos();
       pasarAModoBusqueda();

<% if (inicioTercero.equals("si")) {%>
        // Inicio en modulo de terceros
        ventana=false;
        document.forms[0].ventana.value = false;

        pasarAModoBusqueda();
        comboProvincia.selectItem(-1);
        comboMunicipio.selectItem(-1);
        desHabilitarECOESIVIA (true);        
        marcarObligatoriosCamposSuplementarios(false);
<% } else {%>
        
        // Inicio en ventana en registro o expedientes
        ventana=true;
        document.forms[0].ventana.value = true;        
        if (self.parent.opener.xanelaAuxiliarArgs['modo'] == 'seleccion') {
            Terceros = self.parent.opener.xanelaAuxiliarArgs['terceros'];
            
            if(Terceros.length>0){
                cargarListaTerceros();
            
                // Si se indica un codigo de domicilio se muestra este en el primer tercero de la lista
                if (self.parent.opener.xanelaAuxiliarArgs['domicilio']) {
                    var idDom = self.parent.opener.xanelaAuxiliarArgs['domicilio'];
                    for (var i=0; i<Terceros[0][18].length; i++) {
                        if (Terceros[0][18][i][5] == idDom) {
                            mostrarTercero(0);
                            mostrarDomicilio(Terceros[0], i);
                        }
                    }
                }
            }
        } else {  // Modo alta
            pulsarAlta();
        }
        if (self.parent.opener.xanelaAuxiliarArgs['tipodoc'] && self.parent.opener.xanelaAuxiliarArgs['doc']) {
            comboTipoDoc.buscaCodigo(self.parent.opener.xanelaAuxiliarArgs['tipodoc']);
            document.forms[0].txtDNI.value = self.parent.opener.xanelaAuxiliarArgs['doc'];
        }
<% }%>
    // TODO: falta iniciar en modo alta con el documento del tercero ya cargado
    pleaseWait1('off',top.mainFrame);
}

function valoresPorDefecto(){
    document.forms[0].codPais.value = "<%=ptVO.getPais()%>";
    document.forms[0].descPais.value = "<%=ptVO.getNomPais()%>";
    document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
    document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
    document.forms[0].codMunicipio.value = "<%=ptVO.getMunicipio()%>";
    document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
}

function cargarCombos() {
    comboTipoDoc.addItems(codTipoDocs,descTipoDocs);
    comboProvincia.addItems(codProvincias,descProvincias);
    comboMunicipio.addItems(codMunicipios,descMunicipios);
    comboPostal.addItems(codPostales,codPostales);
    comboECO.addItems(codECOs,descECOs);
    comboESI.addItems(codESIs,descESIs);
}

function cargarListas(){
    document.forms[0].opcion.value="cargarListas";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}

function cerrar(){    
    self.parent.opener.retornoXanelaAuxiliar(TerceroSel);
}

function inicializaLista(){
    pleaseWait1('on',top.mainFrame);
    document.forms[0].lineasPagina.value = lineasPagina;
    document.forms[0].pagina.value = numeroPagina;
    document.forms[0].opcion.value="recargaBusquedaTerceros";
    if(ventana){
        document.forms[0].ventana.value="true";
    }
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
    limpiarTodo();
    mostrarCapaDomicilios(false);
}

/// FUNCIONES DE LOS BOTONES Y DEL FRAME OCULTO
function pulsarBuscar(){ 
    if (!validarDocumento()) return;       
    var vectorCampos = ['codTipoDoc','txtDNI','txtInteresado','txtApell1', 'txtApell2','txtTelefono','txtCorreo',
        'txtCodVia', 'txtNumDesde', 'txtLetraDesde','txtNumHasta','txtLetraHasta', 'txtBloque', 'txtPortal',
        'txtEsc', 'txtPlta', 'txtPta', 'descPostal', 'txtDomicilio', 'txtBarriada', 'codECO', 'codESI'];
    if((document.forms[0].codTipoDoc.value!="" && document.forms[0].codTipoDoc.value!="0") && document.forms[0].txtDNI.value==""){
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjDocObligatorio"))%>');
        return;
    }

    trimTerceroBuscado();
    if((document.forms[0].codTipoDoc.value=="" || document.forms[0].codTipoDoc.value=="0") && document.forms[0].txtDNI.value!=""){
            jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjCritBusqDocum"))%>');
            return;
    }else if(algunoNoVacio(vectorCampos)){
        if (viaBuscada()) {

            document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
            paginaActual=1;
            if (ventana) {
                document.forms[0].ventana.value="true";
            }
            document.forms[0].target="oculto";

            if (document.forms[0].terExpUnidad.checked) {
                document.forms[0].selTercMiUnidad.value="si";
            }else{
                 document.forms[0].selTercMiUnidad.value="no";
            }

             document.forms[0].soloTerceroFlexia.value='<%=soloTerceroFlexia%>';             
             document.forms[0].opcion.value="buscarTerceros";
             document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
            pleaseWait1('on',top.mainFrame);
            document.forms[0].submit();
        } else {
            jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjViaNoBusc"))%>');
        }
    } else {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjCritBusqTerc"))%>');
    }
}

function recuperaBusquedaTerceros(datos) { // oculto
    pleaseWait1('off',top.mainFrame);
    Terceros = datos;    
    cargarListaTerceros();    
}

function errorDemasiadosTerceros() { // oculto
    pleaseWait1('off',top.mainFrame);
     jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjDemasiadosTerceros"))%>');
}

function pulsarBuscarVia(opcion) {
    if (!document.forms[0].descVia.disabled) {
        if (advertirBusquedaTramero()) {

            // Si no hay provincia, usamos valores por defecto
            if (document.forms[0].codProvincia.value == "") {

                document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
                document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
                comboMunicipio.addItems(codMunicipiosDefecto,descMunicipiosDefecto);
                document.forms[0].codMunicipio.value = "<%=ptVO.getMunicipio()%>";
                document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";

            // Si hay provincia pero no hay municipio...
            } else if (document.forms[0].codMunicipio.value == "") {

                // Si la provincia es la 'por defecto', usamos el municipio por defecto.
                if (document.forms[0].codProvincia.value == "<%=ptVO.getProvincia()%>") {

                    document.forms[0].codMunicipio.value = "<%=ptVO.getMunicipio()%>";
                    document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";

                // Si la provincia no es la 'por defecto', se pide al usuario que
                // introduzca municipio.
                } else {
                    jsp_alerta("A", '<%=escape(descriptor.getDescripcion("msjIntroMun"))%>');
                    return;
                }
            }

            pleaseWait1("on",top.mainFrame);
            document.forms[0].opcion.value=opcion;
            document.forms[0].target="oculto";
            var urlParams;
            urlParams="codProvincia=" + document.forms[0].codProvincia.value;
            urlParams= urlParams + "&codMunicipio=" + document.forms[0].codMunicipio.value;
            <% if (existeTramero.equals("si")) {%>
            document.forms[0].action="<html:rewrite page='/terceros/mantenimiento/Viales.do?'/>" + urlParams;
            <% } else {%>
            urlParams = urlParams + '&txtNumDesde=&txtNumHasta=';
            document.forms[0].action="<html:rewrite page='/terceros/mantenimiento/Viales.do?'/>" + urlParams;
            <% }%>
            document.forms[0].submit();
        }
    }
}

function advertirBusquedaTramero() {
    var numDesde = document.forms[0].txtNumDesde.value;
    var numHasta = document.forms[0].txtNumHasta.value;
    <% if (existeTramero.equals("si")) {%>
    if ((numDesde!="" && numDesde!=null) || (numHasta!="" && numHasta!=null)) {
        if (jsp_alerta("",'<%=escape(descriptor.getDescripcion("msjBusqTramero"))%>')) {
            return true;
        } else
        return false;
    } else
    return true;
    <% } else {%>
    return true;
    <% }%>
}

function cargarListaViasBuscadas(total, idVia, codVia, descVia, descTipoVia, codECO, codESI, codTipoVia, codProvincia, descProvincia, codMunicipio, descMunicipio, nombreVia){

         pleaseWait1("off",top.mainFrame);

         if(nombreVia!=null && nombreVia.length>0)
             nombreViaGeneral = nombreVia;
         if (total==0){
            document.forms[0].txtCodVia.value = "";
            document.forms[0].descVia.value = "";
            document.forms[0].txtDomicilio.value = "";
            document.forms[0].idVia.value = "";
            document.forms[0].codTVia.value = "";
            document.forms[0].descTVia.value = "";
            if(<%=bAltaViaDirecta%> && (modoActual != busqueda)){
                if ( jsp_alerta("C",'<%=escape(descriptor.getDescripcion("msjNoDatosPregNueva"))%>') == 1 ){
                    iniciarAltaViaDirecta();
                }
            }else{
            jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoDatos"))%>');
            }
        } else 	if (total==1){
        document.forms[0].txtCodVia.value = codVia;
        document.forms[0].descVia.value = descVia;
        document.forms[0].idVia.value = idVia;
        document.forms[0].codTVia.value = codTipoVia;
        document.forms[0].descTVia.value = codTipoVia == '<%=CodigoSinTipoVia%>'?"":descTipoVia;
    } else {
    var ver = "0,";
    var tamanoVentana=940;
    if (document.forms[0].codProvincia.value!=null && document.forms[0].codProvincia.value!="") {
        ver = "1,";
        tamanoVentana = tamanoVentana - 120;
    }
    if (document.forms[0].codMunicipio.value!=null && document.forms[0].codMunicipio.value!="") {
        ver = ver + "1";
        tamanoVentana = tamanoVentana - 120;
    } else ver = ver + "0";
    var source = "<%=request.getContextPath()%>/jsp/terceros/listaViasBuscadas.jsp?opcion="+ ver;
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,'',
	'width='+tamanoVentana+',height=600,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined && datosConsulta[0] == "altaViaDirecta"){
                            iniciarAltaViaDirecta();
                        } else if(datosConsulta!=undefined){
                            if (datosConsulta[4]=='null') datosConsulta[4] = '';
                            document.forms[0].codECO.value = datosConsulta[4];
                            if ( datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);
                            document.forms[0].txtCodVia.value = datosConsulta[1];
                           // document.forms[0].codVia.value = datosConsulta[1];
                            document.forms[0].descVia.value = datosConsulta[2];
                           // document.forms[0].txtDomicilio.value = datosConsulta[2];
                            document.forms[0].idVia.value = datosConsulta[0];
                            document.forms[0].codTVia.value = datosConsulta[6];
                            document.forms[0].descTVia.value = datosConsulta[6] == '<%=CodigoSinTipoVia%>'?"":datosConsulta[3];
                        }
                });
}
}

function pulsarNuevaBusqueda(){
    pasarAModoBusqueda();
}

function pulsarAlta() { 
    // Grabar directo se usa para que el usuario pueda confirmar la grabacion
    // de un tercero
    document.forms[0].grabarDirecto.value="no";
    pasarAModoAlta();
}


function ocultoAltaTerceroSinDomicilio(){
    document.forms[0].opcion.value="ocultoAltaTerceroSinDomicilio";
    document.forms[0].target=(ventana)?"oculto":"oculto";
    document.forms[0].ventana.value=(ventana)?"true":"false";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}

function pulsarGrabarAlta() {  
    var permitirAltaTerceroSinDomicilio = "<%=permitirAltaTerceroSinDomicilio%>";

    var tipo= document.forms[0].codTipoDoc.value;
    
    //Los campos suplementarios de tercero, solo se validan si es persona física
     if(tipo!="4" && tipo!="5"&& tipo!="0"){ 
    validarCamposSuplementariosTercero();
     }

    // Validacion
    if (!validarTercero()) {
        return; // Los mensajes de error estan en la propia funcion
    }else{
    var documento=document.forms[0].txtDNI.value;    
    document.forms[0].txtDNI.value=Trim(documento);
    }
    
    if (document.forms[0].codTipoDoc.value == <%=m_Conf.getString("tercero.codUOR")%>) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjTercUOR"))%>'); return;
    }
    
    if(permitirAltaTerceroSinDomicilio=="NO" || permitirAltaTerceroSinDomicilio==""){
        if (!validarDomicilio()) {
            return; // Los mensajes de error estan en la propia funcion
        }
    }else
    if(permitirAltaTerceroSinDomicilio=="SI"){
        if(!validarDomicilioAltaTerceroSinDomicilio())
            return; // Los mensajes de error estan en la propia funcion
    }

    if (!viaBuscada()) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoViaDom"))%>'); return;
    }



    
    //Los campos suplementarios de tercero, solo se validan si es persona física
     if(tipo!="4" && tipo!="5"&& tipo!="0"){ 
        if(!validarCamposSuplementariosTercero()){        
            jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoDatosSupTercero"))%>');return;

        }
     }
   

    ocultoAltaTerceroSinDomicilio();
    document.forms[0].txtNormalizado.value = "2";
    // Se da de alta el tercero y domicilio, por tanto siempre es principal
    document.forms[0].esDomPrincipal.value = "true";
    document.forms[0].opcion.value="grabarAltaTercero";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    pleaseWait1('on',top.mainFrame);
    document.forms[0].submit();
}

function confirmarDocDuplicado() { // oculto
    pleaseWait1('off',top.mainFrame);
    if (jsp_alerta("", "<%=escape(descriptor.getDescripcion("msjTerDocRepetido"))%>")) {
       document.forms[0].opcion.value="grabarAltaTercero";
       document.forms[0].grabarDirecto.value="si";
       document.forms[0].target="oculto";
       document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
       pleaseWait1('on',top.mainFrame);
       document.forms[0].submit();
    } else {
       document.forms[0].txtDNI.select();
    }
}

function confirmarExisteTercero() { // oculto
     pleaseWait1('off',top.mainFrame);
     if (jsp_alerta("",'<%=escape(descriptor.getDescripcion("msjExTerSinDoc"))%>')) {
         document.forms[0].opcion.value="grabarAltaTercero";
         document.forms[0].grabarDirecto.value="si";
         document.forms[0].target="oculto";
         document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
         pleaseWait1('on',top.mainFrame);
         document.forms[0].submit();
     }
}

function altaTerceroGrabada(terceros) {// oculto
    pleaseWait1('off',top.mainFrame);
    jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjTercGrab"))%>');
    Terceros = terceros;
    cargarListaTerceros(); // Solo hay un tercero en la lista, se selecciona automaticamente
}

function pulsarCancelarAlta() {
    pasarAModoBusqueda();    
}

function pulsarAltaDomicilio() {
    pasarAModoAltaDomicilio();
}

function pulsarGrabarAltaDomicilio() { 
    if (!validarDomicilio()) {
        return; // Los mensajes de error estan en la propia funcion
    }
    if (!viaBuscada()) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoViaDom"))%>'); return;
    }

    document.forms[0].txtNormalizado.value = "2";
    document.forms[0].opcion.value="grabarAltaDomicilio";

    // En el caso de no tener ningun domicilio el tercero, se marca este como principal
    if (Terceros[terceroActual][18].length == 0)
        document.forms[0].domPrincipal.checked = true;

    if (document.forms[0].domPrincipal.checked) {
        document.forms[0].esDomPrincipal.value = "true";
    } else {
        document.forms[0].esDomPrincipal.value = "false";
    }
    document.forms[0].nuevoDomPrincipal.value = Terceros[terceroActual][20];
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    pleaseWait1('on',top.mainFrame);
    document.forms[0].submit();
}

function altaDomicilioGrabada(terceros) { // oculto
    pleaseWait1('off',top.mainFrame);
    jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjDomGrab"))%>');
    var domNuevo = terceros[0][18][0];
    var domsAnteriores = Terceros[terceroActual][18];
    var indiceDomNuevo = domsAnteriores.length;
    // Añadimos nuevo domicilio a los del tercero
    domsAnteriores[indiceDomNuevo] = domNuevo;
    // Actualizamos el domicilio por defecto del tercero si es el caso (en BD ya se hace en el DAO)
    if (document.forms[0].domPrincipal.checked) {
        Terceros[terceroActual][20] = domNuevo[5];
    }
    // Mostramos resultado
    domicilioActual = indiceDomNuevo;
    recargarListaYTercero(); // Produce cambio a modo seleccion
}

function pulsarCancelarAltaDomicilio() {
    limpiarDomicilio();
    recargarListaYTercero(); // Produce cambio a modo seleccion
}

function pulsarEliminarDomicilio() {
    if(document.forms[0].txtIdDomicilio.value == "0") {
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjDatosExternos"))%>");
    } else {
        if (jsp_alerta("","<%=escape(descriptor.getDescripcion("msjBajaDomicilio"))%>")) {

            // Comprobar si es el principal, en ese caso pedir nuevo domicilio principal
            if (document.forms[0].domPrincipal.checked) {
                pedirNuevoDomPrincipal(function(nuevoDomPrincipal){
                            if (nuevoDomPrincipal!=undefined) {
                                // Ejecutar la accion
                                document.forms[0].nuevoDomPrincipal.value = nuevoDomPrincipal;
                                document.forms[0].opcion.value="borrarDomicilio";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                document.forms[0].submit();
                                pleaseWait1('on',top.mainFrame);
                            }
                        });
            }else{
                // Ejecutar la accion
                document.forms[0].nuevoDomPrincipal.value = "";
                document.forms[0].opcion.value="borrarDomicilio";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                document.forms[0].submit();
                pleaseWait1('on',top.mainFrame);
            }
        }
    }
}
function pedirNuevoDomPrincipal(despois) {

    // Construimos una lista con el resto de domicilios
    var domicilios = Terceros[terceroActual][18];
    var restoDomicilios = new Array();
    var j = 0;
    for(var i = 0; i<domicilios.length; i++) {
        if (i!=domicilioActual) restoDomicilios[j++] = domicilios[i];
    }

    // Abrimos dialogo
    var argumentos = new Array();
    argumentos['domicilios'] = restoDomicilios;
    argumentos['elegirDomPrincipal'] = true;
    var source = "<%=request.getContextPath()%>/jsp/terceros/listaDomicilios.jsp?opcion=";
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
	'width=800,height=400,status='+ '<%=statusBar%>',despois);
}


function domicilioEliminado() { //oculto
    pleaseWait1('off',top.mainFrame);

    // Construimos una lista con el resto de domicilios
    var domicilios = Terceros[terceroActual][18];
    var restoDomicilios = new Array();
    var j = 0;
    for(var i = 0; i<domicilios.length; i++) {
        if (i!=domicilioActual) restoDomicilios[j++] = domicilios[i];
    }
    Terceros[terceroActual][18] = restoDomicilios;
    
    // Actualizamos domicilio principal del tercero
    if (document.forms[0].nuevoDomPrincipal.value != "") {
        Terceros[terceroActual][20] = document.forms[0].nuevoDomPrincipal.value;
    }

    // Recargar tercero
    domicilioActual = -1;
    recargarListaYTercero(); // Produce cambio a modo seleccion
}

function pulsarListaDomicilios() {
    // Abrimos dialogo
    listaDomicilios = Terceros[terceroActual][18];
    var argumentos = new Array();
    argumentos['domicilios'] = listaDomicilios;
    var source = "<%=request.getContextPath()%>/jsp/terceros/listaDomicilios.jsp?opcion=";
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
	'width=800,height=400,status='+ '<%=statusBar%>',function(nuevoDom){
                            if (nuevoDom!=undefined) {
                                for(var i = 0; i<domicilios.length; i++) {
                                    if (listaDomicilios[i][5] == nuevoDom) {
                                        mostrarDomicilio(Terceros[terceroActual],i);
                                        break;
                                    }
                                }
                            }
                    });
}

function pulsarEliminarTercero() {
    if (tab.selectedIndex == -1) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoSelecFila"))%>'); return;
    }
    if (Terceros[terceroActual][0] == "0") {
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjBusqExterna"))%>");
    } else {
        var msjConfirmar = '<%=escape(descriptor.getDescripcion("msjDarBaja"))%>' + " " +
                           Terceros[terceroActual][4] + " " + Terceros[terceroActual][5] + " " +
                           Terceros[terceroActual][6] + " con DNI: " + Terceros[terceroActual][3];
        if(jsp_alerta("",msjConfirmar)) {
            document.forms[0].opcion.value="borrarTercero";
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
            pleaseWait1('on',top.mainFrame);
        }
    }
}

function terceroEliminado() {
    pleaseWait1('off',top.mainFrame);

    // Si hemos eliminado el unico tercero de la lista volvemos a busqueda
    if (Terceros.length == 1) {
        pasarAModoBusqueda();
    } else {
        // Construimos lista con el resto de terceros
        var nuevosTerceros = new Array();
        var j = 0;
        for (var i=0; i<Terceros.length; i++) {
            if (i!=terceroActual) nuevosTerceros[j++] = Terceros[i];
        }
        Terceros = nuevosTerceros;
        inicializaLista();
    }
}

function pulsarModificar() {
    if (tab.selectedIndex == -1) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoSelecFila"))%>'); return;
    }
    if (Terceros[terceroActual][0] == "0") {
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjBusqExternaModif"))%>");
    }else {
        if (Terceros[terceroActual][18].length == 0) {
            jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjAltaDom"))%>");
        } else {
            pasarAModoModificar();
            // Hay que cargar los combos de municipio y codigo postal
            document.forms[0].opcion.value="cargarMunicipiosYCodPostales";
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
            cargandoModificar = true;
            pleaseWait1('on',top.mainFrame);
        }
    }
}

function cargarCombosModificar(_codMunicipios, _descMunicipios, _codPostales) {
    
    codMunicipios = _codMunicipios;
    descMunicipios = _descMunicipios;
    codPostales = _codPostales;

    comboMunicipio.addItems(codMunicipios,descMunicipios);
    comboPostal.addItems(codPostales,codPostales);

    pleaseWait1('off',top.mainFrame);
    cargandoModificar = false;
}

function pulsarGrabarModificacion() { 
    var permitirAltaTerceroSinDomicilio = "<%=permitirAltaTerceroSinDomicilio%>";
    var tipo= document.forms[0].codTipoDoc.value;
    // Validacion
    if (!validarTercero()) {
        return; // Los mensajes de error estan en la propia funcion
    }
    
    if(permitirAltaTerceroSinDomicilio=="NO" || permitirAltaTerceroSinDomicilio==""){
        if (!validarDomicilio()) {
            return; // Los mensajes de error estan en la propia funcion
        }
    }else
    if(permitirAltaTerceroSinDomicilio=="SI"){
        if(!validarDomicilioAltaTerceroSinDomicilio())
            return; // Los mensajes de error estan en la propia funcion
    }

    /*
    if (!validarDomicilio()) {
        return; // Los mensajes de error estan en la propia funcion
    }*/
    if (!viaBuscada()) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoViaDom"))%>'); return;
    }

    //Los campos suplementarios de tercero, solo se validan si es persona física
     if(tipo!="4" && tipo!="5"&& tipo!="0"){ 
        if(!validarCamposSuplementariosTercero()){        
            jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoDatosSupTercero"))%>');return;

        }
     }

    existenCambios = true;
    // #228743
    var modDom = "<%=actualizarDom%>";
     //TODO: comprobar si es tercero o domicilio externo
    // Comprobar si se ha marcado como principal
    var esDomicilioPrincipal = (Terceros[terceroActual][20] == Terceros[terceroActual][18][domicilioActual][5]);
    var estaMarcadoPrincipal = document.forms[0].domPrincipal.checked;

    if (estaMarcadoPrincipal) {
        document.forms[0].nuevoDomPrincipal.value = Terceros[terceroActual][18][domicilioActual][5];
        document.forms[0].txtNormalizado.value = "2";
        document.forms[0].opcion.value="grabarModificacion";
        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?actualizarDom="+modDom;
        pleaseWait1('on',top.mainFrame);
        document.forms[0].submit();
    } else if (esDomicilioPrincipal && Terceros[terceroActual][18].length > 1) {
        pedirNuevoDomPrincipal(function(nuevoDomPrincipal){
            if (nuevoDomPrincipal!=undefined) {
                document.forms[0].nuevoDomPrincipal.value = nuevoDomPrincipal;
                document.forms[0].txtNormalizado.value = "2";
                document.forms[0].opcion.value="grabarModificacion";
                document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?actualizarDom="+modDom;
                pleaseWait1('on',top.mainFrame);
                document.forms[0].submit();
            }
        });
    } else {
        document.forms[0].nuevoDomPrincipal.value = "";
        document.forms[0].txtNormalizado.value = "2";
        document.forms[0].opcion.value="grabarModificacion";
        <%--document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";--%>
        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?actualizarDom="+modDom;
        pleaseWait1('on',top.mainFrame);
        document.forms[0].submit();
    }
}

function modificacionGrabada(terceros) {
    pleaseWait1('off',top.mainFrame);
    var terceroModificado = terceros[0];
    var domModificado = terceroModificado[18][0];
    var domsActuales = Terceros[terceroActual][18];
    var domPrincipalAnterior = Terceros[terceroActual][20];

    Terceros[terceroActual] = terceroModificado;
    Terceros[terceroActual][18] = domsActuales;
    Terceros[terceroActual][18][domicilioActual] = domModificado;

    // Comprobamos que no se pierda el codigo de domicilio principal
    if (Terceros[terceroActual][20] == "") {
        Terceros[terceroActual][20] = domPrincipalAnterior;
    }
    Terceros[terceroActual][1] = parseInt(Terceros[terceroActual][1]) +1;
    // Mostramos resultado
    recargarListaYTercero(); // Produce cambio a modo seleccion

}

function pulsarCancelarModificar() {
    limpiarDomicilio();
    limpiarInteresado();
    recargarListaYTercero(); // Produce cambio a modo seleccion
}

function recargarListaYTercero() {
    var auxDom = domicilioActual;
    var auxTer = terceroActual;
    cargarListaTerceros();
    if (tab.selectedIndex != -1) {
        pasarAModoSeleccion();
    } else {
        tab.selectLinea(auxTer); // Produce cambio a modo seleccion
    }
    if (Terceros[auxTer][18].length > 0 && auxDom != -1) {
        mostrarDomicilio(Terceros[auxTer], auxDom);
    }
}

function pulsarAnteriorDomicilio(){
    var tercero = Terceros[terceroActual];
    if (domicilioActual > 0) {
        mostrarDomicilio(tercero, domicilioActual - 1);
    } else {       
    } 
}

function pulsarSiguienteDomicilio() {
    var tercero = Terceros[terceroActual];
    if (domicilioActual < tercero[18].length - 1) {
        mostrarDomicilio(tercero, domicilioActual + 1);
    } else {       
    } 
}

function pulsarSeleccionar() {
    if (tab.selectedIndex == -1) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoSelecFila"))%>');
        return;
    }
    if (Terceros[terceroActual][18].length == 0) {
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjAltaDom"))%>");
    } else {
       
       self.parent.opener.retornoXanelaAuxiliar(formatearArrayTercero(Terceros[terceroActual], domicilioActual));
    }
}


function pulsarSeleccionar(indice) {

    if (indice == -1) {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoSelecFila"))%>');
        return;
    }
    if (Terceros[terceroActual][18].length == 0) {
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjAltaDom"))%>");
    } else {
        self.parent.opener.retornoXanelaAuxiliar(formatearArrayTercero(Terceros[terceroActual], domicilioActual));
    }
}

function pulsarSalir(){
    // #228743
    var modDom = "<%=actualizarDom%>";
    var origenRegExp = "<%=inicioTercero%>";
    
    if(origenRegExp=="no" && modDom=="no" && existenCambios){
        <%--'<%=escape(descriptor.getDescripcion("msjUsuarBaja"))%>'--%>
        if(jsp_alerta("","Ha modificado el tercero, desea seleccionar la información de interesado?")){
            self.parent.opener.retornoXanelaAuxiliar(formatearArrayTercero(Terceros[terceroActual], domicilioActual));
        } else self.parent.opener.retornoXanelaAuxiliar(undefined);
    } else self.parent.opener.retornoXanelaAuxiliar(undefined);
    
    self.parent.opener.retornoXanelaAuxiliar(undefined);
}

/// FIN FUNCIONES DE LOS BOTONES

/// FUNCIONES PARA CAMBIAR DE MODO
/// ------------------------------

function pasarAModoBusqueda() { 
    deshabilitarTodo(false);
    mostrarCapaDomicilios(false);
    mostrarDomPrincipal(false);
    
    limpiarCamposSuplementariosTercero();
    
    if (modoActual == alta) {
    
        marcarObligatoriosInteresado(false);
        marcarObligatoriosDomicilio(false);
        marcarObligatoriosCamposSuplementarios(false);
        limpiarCamposSuplementariosTercero();
    }
    
    limpiarTodo();    
    mostrarCapaBotones('capaBotonesBusqueda');
    
    document.forms[0].terExpUnidad.checked = false;
   
    modoActual = busqueda;
    actualizaPersonaFJ(); // Depende del modo actual
    
}

function pasarAModoSeleccion() {
    deshabilitarTodo(true);
    mostrarDomPrincipal(true);
    mostrarAltaDomicilio(true);
    mostrarCapaBotones('capaBotonesSeleccion');
    marcarObligatoriosDomicilio(false);
    marcarObligatoriosInteresado(false);    
    modoActual = seleccion;
}

function pasarAModoAlta() { 
    limpiarListaTerceros();
    mostrarCapaDomicilios(false);
    mostrarDomPrincipal(false);
    mostrarCapaBotones('capaBotonesAlta');
    marcarObligatoriosInteresado(true);
    <% if ("NO".equals(permitirAltaTerceroSinDomicilio)){ %>
         // Si no se permite dar de alta el tercero sin dar de alta el domicilio
        marcarObligatoriosDomicilio(true);
    <%} else{ %>
        // Si se permite el alta del tercero sin tener que introducir obligatoriamente el domicilio
        marcarObligatoriosDomicilio(false);
    <% }%>

    document.forms[0].descPostal.readOnly=false;
    modoActual = alta;
    if (document.forms[0].codTipoDoc.value != <%=m_Conf.getString("tercero.codUOR")%>)
    actualizaPersonaFJ(); // Depende del modo actual

    // Se marcan los campos suplementarios, si los hay, como obligatorios, pero solo aquellos que estén definidos como tal
    marcarObligatoriosCamposSuplementarios(true);
    habilitarCamposSuplementarios(true);
}

function pasarAModoModificar() {
    limpiarListaTerceros();
    if (document.forms[0].codTipoDoc.value == <%=m_Conf.getString("tercero.codUOR")%>) 
        deshabilitarDomicilio(false);
    else 
        deshabilitarTodo(false);
        
    mostrarCapaDomicilios(false);
    if (Terceros[terceroActual][18].length == 1) {
//        document.forms[0].domPrincipal.checked = true;
        document.forms[0].domPrincipal.disabled = true;
    }
    mostrarDomPrincipal(true);
    if (Terceros[terceroActual][18].length > 0) mostrarOrdenDomicilio(true);
    mostrarCapaBotones('capaBotonesModificar');
    if (document.forms[0].codTipoDoc.value != <%=m_Conf.getString("tercero.codUOR")%>)
        marcarObligatoriosInteresado(true);
    //marcarObligatoriosDomicilio(true);
     <% if ("NO".equals(permitirAltaTerceroSinDomicilio)){ %>
         // Si no se permite dar de alta el tercero sin dar de alta el domicilio
        marcarObligatoriosDomicilio(true);
    <%} else{ %>
        // Si se permite el alta del tercero sin tener que introducir obligatoriamente el domicilio
        marcarObligatoriosDomicilio(false);
    <% }%>

    document.forms[0].descPostal.readOnly=false;
    modoActual = modif;
    if (document.forms[0].codTipoDoc.value != <%=m_Conf.getString("tercero.codUOR")%>)
        actualizaPersonaFJ(); // Depende del modo actual
    
    marcarObligatoriosCamposSuplementarios(true);
    habilitarCamposSuplementarios(true);
}

function pasarAModoAltaDomicilio() {
    limpiarListaTerceros();
    limpiarDomicilio();
    deshabilitarDomicilio(false);
    mostrarCapaDomicilios(false);
    mostrarDomPrincipal(true);
    document.forms[0].domPrincipal.checked = false;
   
    document.forms[0].terExpUnidad.checked = false;
    
    mostrarCapaBotones('capaBotonesAltaDomicilio');
    marcarObligatoriosDomicilio(true);
    document.forms[0].descPostal.readOnly=false;
    modoActual = altaDom;
}

/// FIN FUNCIONES PARA CAMBIAR DE MODO

function cargarListaTerceros(){
    marcarObligatoriosCamposSuplementarios(false);

    if(Terceros.length>0){
        var lista = new Array();
        var nombre;
        var direccion;
        for (var i = 0; i < Terceros.length; i++) {
            var tercero = Terceros[i];
            nombre = formatearNombreCompleto(tercero);
            if (tercero[18].length == 0) {
                direccion = "<%=escape(descriptor.getDescripcion("etiqSinDomicilio"))%>";
            } else {
                direccion = formatearDireccion(tercero[18][indiceDomPrincipal(tercero)]);
            }
        <%if (mostrarOrigen.equals("si")) {%>
            lista[i] = [tercero[3],nombre,direccion,tercero[19]];
        <%} else {%>
            lista[i] = [tercero[3],nombre,direccion];
        <%}%>
        }
        datosTabla = lista;
        tab.lineas=lista;
        tab.displayTabla();

        // Si solo hay un resultado se selecciona directamente
        if (Terceros.length == 1){            
            tab.selectLinea(0);            
        }
    } else {
        jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoRegCoinc"))%>');
    <% if (((request.getParameter("preguntaAlta") == null) || (request.getParameter("preguntaAlta").equals("si")))) {%>
        if (jsp_alerta("",'<%=escape(descriptor.getDescripcion("msjNuevoInteresado"))%>')) {
             pulsarAlta();
        }
    <% }%>
    }
}

function iniciarAltaViaDirecta(){
    document.forms[0].viaDarAlta.value = nombreViaGeneral;
    pleaseWait1("on",top.mainFrame);
    document.forms[0].opcion.value="iniciarAltaDirecta";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
    document.forms[0].submit();
}

function altaViaDirectaIniciada(codTipoVia,descTipoVia,nombreViaAlta) {
    var argumentos = new Array();
    argumentos = [document.forms[0].codProvincia.value,document.forms[0].codMunicipio.value, codTipoVia, descTipoVia,nombreViaAlta];
    var source = "<%=request.getContextPath()%>/jsp/terceros/altaViaDirecta.jsp?opcion=altaViaDirecta";
    pleaseWait1("off",top.mainFrame); // Viene del buscar via
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source, argumentos,
	'width=500,height=400,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined){
                                document.forms[0].codECO.value = datosConsulta[4];
                                if (datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);
                                document.forms[0].txtCodVia.value = datosConsulta[0];
                                document.forms[0].descVia.value = datosConsulta[2];
                                document.forms[0].codTVia.value = datosConsulta[6];
                                document.forms[0].descTVia.value = datosConsulta[3];
                            }
                    });
}

function cargarListaMunicipios(){ 
    document.forms[0].opcion.value="cargarMunicipios";
    document.forms[0].target=(ventana)?"oculto":"oculto";
    document.forms[0].ventana.value=(ventana)?"true":"false";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}

function cargarListaCodPostales(){
    document.forms[0].opcion.value="cargarCodPostales";
    if(ventana){
        document.forms[0].ventana.value="true";
    }
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?codProvincia="+document.forms[0].codProvincia.value+"&codMunicipio="+document.forms[0].codMunicipio.value;
    document.forms[0].submit();
}

function cambiarSituacion(tercero) {
    var situacion = tercero[12];
    if(situacion=="B"){
        if(jsp_alerta("",'<%=escape(descriptor.getDescripcion("msjUsuarBaja"))%>')){
            document.forms[0].situacion.value="A";
            document.forms[0].opcion.value="cambiaSituacionTercero";
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
            tercero[12]="A";
        }
    }
}

function validarDomicilio(){ 
    var pais = document.forms[0].codPais;
    var prov = document.forms[0].codProvincia;
    var muni = document.forms[0].codMunicipio;
    var cpostal	= document.forms[0].descPostal;
    var domicilio = document.forms[0].txtDomicilio;
    var via	= document.forms[0].txtCodVia;
    var descVia	= document.forms[0].descVia;

    if(('<%=emplv%>' == "hidden" && Trim(via.value) == "") || (Trim(pais.value)=="")||(Trim(prov.value)=="")||(Trim(muni.value)=="")){    
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjObligTodos"))%>");
        return false;
    } else {
        if (Trim(via.value) == '') { 
            if (Trim(descVia.value) != '') {                 
                jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoViaBusc"))%>');
                return false;                
            }
            if (Trim(domicilio.value) == '' ){           
                jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjNoViaDom"))%>');
                return false;
            }
        }
    }
    /* anadir ECOESI */
    if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>")
        && ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")) {

        var ecv = '<%=ecv%>';
        var esv = '<%=esv%>';
        if (ecv == 'visible' && esv == 'visible') {
            if (( document.forms[0].codECO.value=="")&&( document.forms[0].codESI.value=="") ){
                jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjRellenarESI"))%>");
                return false;
            }
        }
    }
    /* fin anadir ECOESI */

    if(cpostal.value!=""){
        if (prov.value.length==1) prov.value="0"+prov.value;
        if (!validarCodigoPostal(prov,cpostal)) {
            document.forms[0].descPostal.select();
            return false;
        }
    }
    return true;
}


/** Esta función se llama para validar el domicilio si FLEXIA se ha configurado para
     poder  dar de alta terceros sin introducir un domicilio */
function validarDomicilioAltaTerceroSinDomicilio(){ 
    var pais = document.forms[0].codPais;
    var prov = document.forms[0].codProvincia;
    var muni = document.forms[0].codMunicipio;
    var cpostal	= document.forms[0].descPostal;
    var domicilio = document.forms[0].txtDomicilio;
    var via	= document.forms[0].txtCodVia;    
    var descPostal      = Trim(document.forms[0].descPostal.value);
    var txtBarriada     = Trim(document.forms[0].txtBarriada.value);
    var txtDomicilio     = Trim(document.forms[0].txtDomicilio.value);
    var txtNumDesde  = Trim(document.forms[0].txtNumDesde.value);
    var txtLetraDesde  =Trim(document.forms[0].txtLetraDesde.value);
    var txtNumHasta   = Trim(document.forms[0].txtNumHasta.value);
    var txtLetraHasta  = Trim(document.forms[0].txtLetraHasta.value);
    var txtBloque        = Trim(document.forms[0].txtBloque.value);
    var txtPortal         = Trim(document.forms[0].txtPortal.value);
    var txtEsc             = Trim(document.forms[0].txtEsc.value);
    var txtPlta            = Trim(document.forms[0].txtPlta.value);
    var txtPta             = Trim(document.forms[0].txtPta.value);
    var descVia          = Trim(document.forms[0].descVia.value);

    var mensaje="";
    var contador =0;        

    if(descPostal!="") contador++;
    if(txtBarriada!="") contador++;
    if(txtDomicilio!="") contador++;
    if(txtNumDesde!="") contador++;
    if(txtLetraDesde!="") contador++;
    if(txtNumHasta!="") contador++;
    if(txtLetraHasta!="") contador++;
    if(txtBloque!="") contador++;
    if(txtPortal!="") contador++;
    if(txtEsc!="") contador++;
    if(txtPlta!="") contador++;
    if(txtPta!="") contador++;
    if(txtPta!="") contador++;    
    if(descVia!="") contador++;


    if(Trim(prov.value)!="" && Trim(muni.value)!=""){
        // Hay provincia y municipio => Se comprueba si hay domicilio
        if (Trim(via.value) == '' && Trim(domicilio.value) == '' ){ 
                mensaje = "<%=escape(descriptor.getDescripcion("msjNoViaDom"))%>";
        }
    }

    if(Trim(prov.value)!="" && Trim(muni.value)==""){
        mensaje = "<%=escape(descriptor.getDescripcion("msjNoViaDom"))%>";
    }else
    if(Trim(prov.value)=="" && Trim(muni.value)=="" && contador>0){
        mensaje = "<%=escape(descriptor.getDescripcion("msjNoViaDom"))%>";
    }

    /* anadir ECOESI */
    if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>")
        && ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")) {

        var ecv = '<%=ecv%>';
        var esv = '<%=esv%>';
        if (ecv == 'visible' && esv == 'visible') {
            if (( document.forms[0].codECO.value=="")&&( document.forms[0].codESI.value=="") ){            
                mensaje="<%=escape(descriptor.getDescripcion("msjRellenarESI"))%>"
                //return false;
            }
        }
    }
    /* fin anadir ECOESI */

    if(cpostal.value!=""){
        if (prov.value.length==1) prov.value="0"+prov.value;
        if (!validarCodigoPostal(prov,cpostal)) {
            document.forms[0].descPostal.select();
            return false;
        }
    }

    if(mensaje!=null && mensaje.length>0){
        jsp_alerta("A", mensaje);
        return false;
    }
    else
        return true; 
}

function validarTercero(){ 
    var codTipoDoc = document.forms[0].codTipoDoc;
    var documento = document.forms[0].txtDNI;
    var nombre = document.forms[0].txtInteresado;
    var apel1 = document.forms[0].txtApell1.value;
    if((codTipoDoc.value=="")||((codTipoDoc.value!="0")&&(documento.value==""))||(nombre.value=="") || ((codTipoDoc.value==1) && (apel1==""))) {        
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjObligTodos"))%>");
        return false;
    }
    if (!validarDocumento()) return false;
    return true;
}

function validarNif(campo) { 
    var documento = campo.value;
    var LONGITUD = 9;	    
    
    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es menor de la esperada, rellenamos con ceros a la izquierda.
    var ultCaracter = documento.substring(documento.length - 1, documento.length);
    if (isNaN(ultCaracter)) while (documento.length < LONGITUD) documento = "0" + documento;
    else while (documento.length < LONGITUD - 1) documento = "0" + documento;
    
    if (documento.length > LONGITUD) {
        jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjDocIncorrecto"))%>');
        campo.value = '';
        campo.focus();
        return false;
    }
    
    if (documento.length == LONGITUD) {
        var numDocumento = documento.substring(0, 8);
        var letDocumento = documento.substring(8, 9);
    } else {
        var numDocumento = documento;
        var letDocumento = '';
    }
    
    if (isNaN(numDocumento)) {
        jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjDocIncorrecto"))%>');
        campo.value = '';
        campo.focus();        
        return false;
    }
    
    var letraCorrecta = getLetraNif(numDocumento);
    if (letDocumento == '') {
        jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjLetraNif"))%> ' + letraCorrecta);
        campo.value = numDocumento;    
        campo.select();
        return false;
    }
    
    if (letDocumento != letraCorrecta) {
        var res = jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjLetraNif"))%> ' + letraCorrecta + '. <%=escape(descriptor.getDescripcion("msjPregContinuar"))%>');
        if (!(res > 0)) {
            campo.value = numDocumento + letDocumento;    
            campo.select();        
            return false;
        }
    }
    campo.value = numDocumento + letDocumento;
    return true;
}


function validarDocumento(){
    var tipo = document.forms[0].codTipoDoc.value;
    var codTerc = document.forms[0].codTerc.value;

    var documento = document.forms[0].txtDNI.value;
    if (documento == '') return true;        


    if(tipo=="4" || tipo=="5"){ 
        // Si se trata de un CIF
        if(!validarCIF(documento)){
            // Si el tipo de documento es CIF, se va a admitir también un NIF o un NIE              
            if(!validarNifDocumentoTipoCif(documento)){                  
                if(!validarNieDocumentoTipoCif(documento)){
                    jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjDocIncorrecto"))%>');
                    return false;                      
                }else return true;
            }else
              return true;
        }
        return true;
    }

    if(tipo=="1") return validarNif(document.forms[0].txtDNI);

    // Validamos el pasaporte.
    if (tipo=="2") return true;

    // Validamos la tarjeta de residencia
    if (tipo=="3") {
      var nieCorrecto = validarNie(document.forms[0].txtDNI);
      if (!nieCorrecto)
          jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjDocIncorrecto"))%>');
      return nieCorrecto;
      }

    return true;
}
      
function mostrarErrorAltaTercero(msgError){        
    pleaseWait("off");
    jsp_alerta("A",msgError);    
}

function altaTerceroFlexiaNoSistemasExternos(terceros) {// oculto
    pleaseWait1('off',top.mainFrame);
    jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msgAltaTerConErr"))%>');
    Terceros = terceros;
    cargarListaTerceros(); // Solo hay un tercero en la lista, se selecciona automaticamente
}

function errorAltaTerceroTransaccionErroresSistemasExternos() {// oculto
    pleaseWait1('off',top.mainFrame);
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrTransaccionErrTerExt")%>');
}

function errorTecnicoAltaTerceroExterno() {// oculto
    pleaseWait1('off',top.mainFrame);
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrTecnicoAltaTer")%>');
}
</script>
</head>

<body class="bandaBody" onLoad="inicio();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form name="formulario" METHOD=POST target="_self">
    <input type="hidden" name="opcion">
    <input type="hidden" name="grabarDirecto" value="no">
    <input type="hidden" name="codTerc">
    <input type="hidden" name="txtIdTercero">
    <input type="hidden" name="codDomTerc">
    <input type="hidden" name="txtIdDomicilio">
    <input type="hidden" name="numModifTerc">
    <input type="hidden" name="txtVersion">
    <input type="hidden" name="situacion">
    <input type="hidden" name="perFJ">
    <input type="hidden" name="txtNormalizado" value="2">
    <input type="hidden" name="ventana"	value="false">
    <input type="hidden" name="txtPart">
    <input type="hidden" name="txtPart2">
    <input type="hidden" name="codTVia">
    <input type="hidden" name="descTVia">
    <input type="hidden" name="idVia" value="1"> <!-- Codigo INE de la via -->
    <input type="hidden" name="codPais"	size="3" value="<%=ptVO.getPais()%>">
    <input type="hidden" name="descPais" size="3">
    <input type="hidden" name="lineasPagina" value="10">
    <input type="hidden" name="pagina" value="1">
    <input type="hidden" name="posElemento">
    <input type="hidden" name="esDomPrincipal" value="false">
    <input type="hidden" name="nuevoDomPrincipal" value="">
    <input type="hidden" name="viaDarAlta" value=""/>
    <input type="hidden" name="selTercMiUnidad" value=""/>
    <input type="hidden" name="soloTerceroFlexia" value=""/>
    <input type="hidden" name="codTerceroOrigen" value=""/>
   
   <% if (aplicacionOrigen!=4) {%>
   <input type="hidden" name="terExpUnidad" value=""/>
   <%}%>
    
<div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("titGestionTerceros")%></div>
<div class="contenidoPantalla">
    <c:if test="${sessionScope.BusquedaTercerosForm.pantallaDatosSuplementariosTerceroActivada ne 'SI'}">
    <div class="sub3titulo"><%= descriptor.getDescripcion("manTer_TitDatos")%></div>
    </c:if>
    <!-- DATOS TERCERO Y DOMICILIO -->
        <!-- Cabecera pestaña -->
        <c:if test="${sessionScope.BusquedaTercerosForm.pantallaDatosSuplementariosTerceroActivada eq 'SI'}">
        <div class="tab-pane" id="tab-pane-1">
            <script type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
            </script>                                        
            <div class="tab-page" id="tabPage1">
                <h2 class="tab" id="pestana1"><%= descriptor.getDescripcion("manTer_TitDatos")%></h2>
                <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
        </c:if>
            <table>
                <tr>
                    <td valign="top">
                        <table width="100%" class="separaFilasVert">
                            <tr>
                                <td width="18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</td>
                                <td width="38%" class="columnP">
                                    <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTexto" style="width:8%"
                                           onkeypress="javascript:return SoloDigitosConsulta(event);">
                                    <input id="descTipoDoc" name="descTipoDoc" type="text" class="inputTexto" style="width:70%" readonly>
                                    <a id="anchorTipoDoc" name="anchorTipoDoc" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;"	alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                                </td>
                                <td width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                                <td width="32%" class="columnP" align="left">
                                    <input type="text" class="inputTexto" style="width:80%" maxlength="16" name="txtDNI"  id="txtDNI" onblur="return xAMayusculas(this);">
                                </td>
                            </tr>
                            <tr>
                                <td width="18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
                                <td width="82%" colspan="3">
                                    <input type="text" name="txtInteresado" id="txtInteresado" class="inputTexto" size="<%=sizeTxtInteresado%>" maxlength="120" style="width:100%" onblur="return xAMayusculas(this);">
                                </td>
                            </tr>
                            <tr>
                                <td width="18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
                                <td width="38%" class="columnP">
                                    <input type="text" name="txtApell1" id="txtApell1" class="inputTexto" maxlength="25" style="width:95%" onblur="return xAMayusculas(this);">
                                </td>
                                <td width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</td>
                                <td width="32%" class="columnP">
                                    <input type="text" name="txtApell2" id="txtApell2" class="inputTexto" maxlength="25" style="width:100%" onblur="return xAMayusculas(this);">
                                </td>
                            </tr>
                            <tr>
                                <td width="18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:</td>
                                <td width="38%" class="etiqueta">
                                    <input type="text" name="txtTelefono" id="txtTelefono" class="inputTexto" maxlength="40" style="width:95%" onkeyup="return xAMayusculas(this);">
                                </td>
                                <td width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmail")%>:</td>
                                <td width="32%" class="etiqueta">
                                    <input type="text" name="txtCorreo" id="txtCorreo" class="inputTexto" maxlength="50" style="width:100%" style="text-transform: none">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="sub3titulo">&nbsp;<%=descriptor.getDescripcion("manTer_TitDomis")%></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                                <td width="38%" class="columnP">
                                    <input class="inputTexto" type="text" id="codProvincia" name="codProvincia" style="width:8%" onkeyup="javascript:return SoloDigitosNumericos(this);">
                                    <input class="inputTexto" type="text" id="descProvincia" name="descProvincia" style="width:70%" readonly>
                                    <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                                </td>
                                <td width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
                                <td width="38%" class="columnP">
                                    <input class="inputTexto" type="text" id="codMunicipio" name="codMunicipio"
                                           style="width:8%" onkeyup="return SoloDigitosNumericos(this);">
                                    <input id="descMunicipio" name="descMunicipio" type="text" class="inputTexto" style="width:70%" readonly>
                                    <a id="anchorMunicipio" name="anchorMunicipio" href="">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" 
                                              name="botonMunicipio" style="cursor:hand;" alt ="<%=descriptor.getDescripcion("altDesplegable")%>" 
                                              title ="<%=descriptor.getDescripcion("altDesplegable")%>" 
                                              style="cursor:hand;"></span>
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- ECO/ESI -->
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td style="display:<%=ecv%>" width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqParroquia")%>:</td>
                                <td style="display:<%=ecv%>" width="38%" class="columnP" >
                                    <input type="text" id="codECO" name="codECO" class="inputTexto" size="3">
                                    <input type="text" id="descECO" name="descECO" class="inputTexto" size="40" readonly>
                                    <a href="" id="anchorECO" name="anchorECO"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonECO" name="botonECO" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                                </td>
                                <td	style="display:<%=esv%>" width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqLugar")%>:</td>
                                <td	style="display:<%=esv%>" width="38%" class="etiqueta">
                                    <input type="text" id="codESI" name="codESI" class="inputTexto" size="3">
                                    <input type="text" id="descESI" name="descESI" class="inputTexto" size="40" readonly>
                                    <a href="" id="anchorESI" name="anchorESI"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESI" name="botonESI" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- VIA -->
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td width="37%" class="etiqueta" valign="bottom"><%=descriptor.getDescripcion("etiqVia")%>:</td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_PNumero")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_UNumero")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("manTer_EtiqBLQ")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("manTer_EtiqPTL")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Escalera")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Planta")%></td>
                                <td width="7%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Puerta")%></td>
                            </tr>
                            <tr>
                                <td width="37%" class="columnP">
                                    <input type="hidden" name="txtCodVia">
                                    <input type="text"  id="descVia" name="descVia" class="inputTexto" maxlength="50" style="width:85%" 
                                           onkeypress="<%if(userAgent.indexOf("MSIE")!=-1) {%>javascript:PasaAMayusculas(event);<%}else{%>return xAMayusculas(this);<%}%>" 
                                           onchange="limpiarVia();">
                                    <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Vía" style="cursor:hand;" onclick="pulsarBuscarVia('buscarVias');"></span>
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtNumDesde" id="txtNumDesde"style="width:100%" maxlength=4 onkeyup="return SoloDigitosNumericos(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtLetraDesde" id="txtLetraDesde"style="width:100%" maxlength=1 onkeyup="return xAMayusculas(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtNumHasta" id="txtNumHasta"style="width:100%" maxlength=4 onkeyup="javascript:return SoloDigitosNumericos(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtLetraHasta" id="txtLetraHasta"style="width:100%" maxlength=1 onkeyup="return xAMayusculas(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtBloque" id="txtBloque"style="width:100%" maxlength=3 onkeyup="return xAMayusculas(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtPortal" id="txtPortal"style="width:100%" maxlength=2 onkeyup="return xAMayusculas(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtEsc" id="txtEsc"style="width:100%" maxlength=2 onkeyup="return xAMayusculas(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtPlta" id="txtPlta"style="width:100%" maxlength=3 onkeyup="return xAMayusculas(this);">
                                </td>
                                <td width="7%" class="columnP" align="center">
                                    <input type="text" class="inputTexto" name="txtPta" id="txtPta"style="width:100%" maxlength=4 onkeyup="return xAMayusculas(this);">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td height="5px"></td>
                </tr>
                <!-- CP, POBLACION, EMPLAZAMIENTO -->
                <tr>
                    <td>
                        <table border="0px"width="100%">
                            <tr>
                                <td>
                                    <span width="18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</span>
                                    <span width="32%" class="columnP" >
                                        <input type="text" class="inputTexto" size="9" maxlength=8 id="descPostal" name="descPostal"></input>
                                        <a id="anchorPostal" name="anchorCP" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCP" name="botonCP" style="cursor:pointer" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span></a>
                                    </span>
                                    <div width="50%" id="divDomPrincipal" style="display:inline; visibility: hidden">
                                        <span width="80%" class="etiqueta" id="etiqDomPrincipal">&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("etiqDirPrincipal")%>:&nbsp;</span>
                                        <span width="20%" class="columnP" style="vertical-align:bottom" id="checkDomPrincipal">
                                            <input type="checkbox" value="true" id="domPrincipal" name="domPrincipal"></input>
                                        </span>
                                    </div>
                                </td>
                                <td style="display:<%=emplv%>; padding-right:7px; text-align:right">
                                    <span width="34%" style="display:<%=emplv%>; text-align:left" class="etiqueta">&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiqEmplazamiento")%>:&nbsp;</span>
                                    <span width="66%" style="display:<%=emplv%>" class="columnP">
                                        <input type="text" name="txtDomicilio" class="inputTexto" id="txtDomicilio" size="49" maxlength="50" onblur="return xAMayusculas(this);"></input>
                                    </span>
                                </td>
                                <td style="display:<%=pv%>; padding-right:7px; text-align:right">
                                    <span width="27%" style="display:<%=pv%>; text-align:left" class="etiqueta">&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiqPoblacion")%>:&nbsp;</span>
                                    <span width="73%" style="display:<%=pv%>" class="columnP">
                                        <input name="txtBarriada" type="text" class="inputTexto" id="txtBarriada" size="38" maxlength="40" onblur="return xAMayusculas(this);"></input>
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- BOTONES DOMICILIO -->
                <tr>
                    <td>
                    <table width="100%">
                        <tr>
                            <td style="text-align:left; display:inline;width:30%">
                                <div id="navegacionDoms" style="display:inline; visibility: visible">
                                    <span id="flechaMenos">&nbsp;
                                        <span class="fa fa-arrow-circle-o-left" aria-hidden="true" onclick="javascript:pulsarAnteriorDomicilio();" id="flechaAnterior" name="flechaAnterior" style="cursor:pointer" alt="<%=descriptor.getDescripcion("tipDomAnterior")%>" title="<%=descriptor.getDescripcion("tipDomAnterior")%>"></span>&nbsp
                                    </span>
                                    <span class="etiqueta" id="ordenDomicilio">&nbsp;</span>
                                    <span id="flechaMas" style="vertical-align:bottom">&nbsp;
                                        <span class="fa fa-arrow-circle-o-right" aria-hidden="true" onclick="javascript:pulsarSiguienteDomicilio();" id="flechaSiguiente" name="flechaSiguiente" style="cursor:pointer" alt="<%=descriptor.getDescripcion("tipDomSiguiente")%>" title="<%=descriptor.getDescripcion("tipDomSiguiente")%>"></span>
                                    </span>
                                </div>
                                <div id="operacionesDoms" style="display:inline; margin-left:30px; visibility: hidden">
                                    <span id="nuevoDomicilio"><span class="fa fa-plus-square" onclick="javascript:pulsarAltaDomicilio();" style="cursor:pointer" alt="<%=descriptor.getDescripcion("tipAltaDomicilio")%>" title="<%=descriptor.getDescripcion("tipAltaDomicilio")%>"></span></span>
                                    <span id="eliminarDom"><span class="fa fa-trash" onclick="javascript:pulsarEliminarDomicilio();" style="cursor:pointer" alt="<%=descriptor.getDescripcion("toolTip_bEliminarDom")%>" title="<%=descriptor.getDescripcion("toolTip_bEliminarDom")%>"></span></span>
                                    <span style="margin-left:30px"><span class="fa fa-map-marker " onclick="javascript:pulsarListaDomicilios();" style="cursor:pointer" alt="<%=descriptor.getDescripcion("etiqVerDom")%>" title="<%=descriptor.getDescripcion("etiqVerDom")%>"></span></span>
                                </div>
                            </td>
                            <td style="text-align: left" width="70%">
                                <!-- BOTONES BUSQUEDA -->
                                <div id="capaBotonesBusqueda" style="text-align: right; display:none">
                                    <table cellpadding="0px" cellspacing="0px">
                                        <tr>
                                            <% if (aplicacionOrigen==4) {%>
                                            <td>
                                                <span class="etiqueta">&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiqTercExpUnidad")%>:</span>
                                                 <input type="checkbox" value="true" id="terExpUnidad" name="terExpUnidad"></input>
                                            </td>
                                            <td style="width:18px"></td>
                                            <% }%>
                                            <td>
                                                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' alt='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar" onClick="pulsarBuscar();return false;">
                                                 <% if (!seleccionDesdeInformes.equals("si")) {%>
                                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>' class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" onClick="pulsarAlta();return false;">
                                                 <% }%>
                                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="limpiarTodo();return false;">
                                                 <% if (!inicioTercero.equals("si")) {%>
                                                    <input type= "button"  title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' alt='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbCerrar")%>" name="cmdSalir" onClick="pulsarSalir();return false;">
                                                 <% }%>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <!-- BOTONES SELECCION -->
                                <div id="capaBotonesSeleccion" style="text-align: right; display:none">
                                    <input type="button" title='<%=descriptor.getDescripcion("tipNuevaBusqueda")%>' alt='<%=descriptor.getDescripcion("tipNuevaBusqueda")%>' class="botonLargo" value="<%=descriptor.getDescripcion("gbNuevaBusqueda")%>" name="cmdNuevaBusqueda" onClick="pulsarNuevaBusqueda();return false;">
                                    <% if (!seleccionDesdeInformes.equals("si")) {%>
                                        <input type="button" title='<%=descriptor.getDescripcion("gbModificar")%>' alt='<%=descriptor.getDescripcion("gbModificar")%>' class="botonGeneral"  value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificar" onClick="pulsarModificar();return false;">
                                        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bEliminarTer")%>' alt='<%=descriptor.getDescripcion("toolTip_bEliminarTer")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>' name="cmdEliminarTercero" onClick="pulsarEliminarTercero();return false;">
                                    <% }%>
                                    <% if (!inicioTercero.equals("si")) {%>
                                        <input type= "button"  title='<%=descriptor.getDescripcion("gbSeleccionar")%>' alt='<%=descriptor.getDescripcion("gbSeleccionar")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbSeleccionar")%>" name="cmdSalir" onClick="pulsarSeleccionar();return false;">
                                        <input type= "button"  title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' alt='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbCerrar")%>" name="cmdSalir" onClick="pulsarSalir();return false;">
                                    <% }%>
                                </div>

                                <!-- BOTONES ALTA -->
                                <div id="capaBotonesAlta" style="text-align: right; display:none">
                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabarAlta" onClick= "pulsarGrabarAlta();return false;">
                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarAlta" onClick="pulsarCancelarAlta();return false;">
                                </div>
                                 <!-- BOTONES ALTA DOMICILIO -->
                                <div id="capaBotonesAltaDomicilio" style="text-align: right; display:none">
                                    <input type= "button" title='<%=descriptor.getDescripcion("tipGrabarDom")%>' alt='<%=descriptor.getDescripcion("tipGrabarDom")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabarAltaDomicilio" onClick= "pulsarGrabarAltaDomicilio();return false;">
                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarAltaDomicilio" onClick="pulsarCancelarAltaDomicilio();return false;">
                                </div>
                                <!-- BOTONES MODIFICAR -->
                                <div id="capaBotonesModificar" style="text-align: right; display:none">
                                    <input type= "button" title='<%=descriptor.getDescripcion("tipConfirmar")%>' alt='<%=descriptor.getDescripcion("tipConfirmar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbConfirmar")%>' name="cmdGrabarModificacion" onClick= "pulsarGrabarModificacion();return false;">
                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarModificar" onClick="pulsarCancelarModificar();return false;">
                                </div>
                            </td>
                        </tr>
                    </table>
                    </td>
                </tr>
               <tr>
                    <td class="sub3titulo"><%=descriptor.getDescripcion("titSelTerceros")%></td>
               </tr>
                <tr>
                    <td id="tabla"></td>
                </tr>
            </table>

        <c:if test="${sessionScope.BusquedaTercerosForm.pantallaDatosSuplementariosTerceroActivada eq 'SI'}">
            </div>  <!-- CIERRA EL DIV DE LA PRIMERA PESTAÑA -->
        </c:if>
<!--------->
<%
Vector<EstructuraCampo> estructuraDatosSuplementarios = (Vector<EstructuraCampo>) bForm.getEstructuraCamposSuplementariosTercero();
Vector valoresDatosSuplementarios = (Vector) bForm.getValoresCamposSuplementariosTercero();

if (estructuraDatosSuplementarios != null)
{
int lengthEstructuraDatosSuplementarios = estructuraDatosSuplementarios.size();        
if (lengthEstructuraDatosSuplementarios > 0) 
{
for (int h= 0; h < lengthEstructuraDatosSuplementarios; h++) {
EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(h);
if (eC.getCodTipoDato().equals("5")) {
campos = campos + "," + eC.getCodCampo().toUpperCase();                    
}
}// for
if (campos.length() > 0) {
campos = campos.substring(1);
}            

%>
          <c:if test="${sessionScope.BusquedaTercerosForm.pantallaDatosSuplementariosTerceroActivada eq 'SI'}">
                <div class="tab-page" id="tabPage2">
                    <h2 class="tab" id="pestana2"><%=descriptor.getDescripcion("etiqDatosSuplementTer")%></h2>
                    <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
                        <%                                                            
                                for (int k = 0; k < lengthEstructuraDatosSuplementarios; k++) {
                                    request.setAttribute("CAMPO_BEAN", estructuraDatosSuplementarios.get(k));
                                    request.setAttribute("beanVO", valoresDatosSuplementarios.get(k));
                        %>
                                        <jsp:include page="/jsp/plantillas/terceros/CampoVistaTercero.jsp?desactivaFormulario=false" flush="true" />
                        <%
                                }// for

                                session.removeAttribute("tramiteCodigo"); // Borramos de la sesion lo utilizado en la JSP anterior
                        %>                                                    
                </div>
           </c:if>
<%
}// if
}// if
%>                                                     
<!-- fin del div pestañas -->
<c:if test="${sessionScope.BusquedaTercerosForm.pantallaDatosSuplementariosTerceroActivada eq 'SI'}">
    </div>
</c:if>
</div>

</form>

 <%
    Vector estructuraDatosSuplementariosAux = null;
    Vector valoresDatosSuplementariosAux = null;
    estructuraDatosSuplementariosAux = (Vector<EstructuraCampo>) bForm.getEstructuraCamposSuplementariosTercero();
    valoresDatosSuplementariosAux = (Vector) bForm.getValoresCamposSuplementariosTercero();

    if (estructuraDatosSuplementariosAux != null) {
        int lengthEstructuraDatosSuplementariosAux = estructuraDatosSuplementariosAux.size();
        int lengthValoresDatosSuplementariosAux = valoresDatosSuplementariosAux.size();
        if (lengthEstructuraDatosSuplementariosAux > 0) {
        int j=0;
        int yy=0;
            for (int hh = 0; hh < lengthEstructuraDatosSuplementariosAux; hh++) {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(hh);
                CamposFormulario cF = (CamposFormulario) valoresDatosSuplementariosAux.elementAt(hh);
                String nombre = eC.getCodCampo();
                String valor = cF.getString(nombre);


    String tipo = eC.getCodTipoDato();
    if (tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegable"))||tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegableExt"))) {%>
    <script type="text/javascript">
        if(CARGAR_DATOS_SUPLEMENTARIOS!=null && (CARGAR_DATOS_SUPLEMENTARIOS=="SI" || CARGAR_DATOS_SUPLEMENTARIOS=="si")){
             eval("var combo<%=nombre%> = new Combo('<%=nombre%>')");
                eval("combo<%=nombre%>.addItems(<%=eC.getListaCodDesplegable()%>,<%=eC.getListaDescDesplegable()%>)");
                    eval("combo<%=nombre%>.buscaLinea('<%=valor%>');");
                        <%if ((eC.getSoloLectura().equals("true"))||("SI".equals(eC.getBloqueado()))) {%>
                        eval("combo<%=nombre%>.deactivate();");
                             <%}%>
                                eval("combo<%=nombre%>.change=modificaVariableCambiosCamposSupl;");
                                nombreCombos[<%=j%>]=['<%=nombre%>'];
                                <%j++;%>
       }// if

    </script>
    <%
            }
           }
         }
      }
    %>


<script type="text/javascript">
        var contDatosSuplementarios = 0
<%
    estructuraDatosSuplementariosAux = null;
    valoresDatosSuplementariosAux = null;
    estructuraDatosSuplementariosAux = (Vector<EstructuraCampo>) bForm.getEstructuraCamposSuplementariosTercero();
    valoresDatosSuplementariosAux = (Vector) bForm.getValoresCamposSuplementariosTercero();

    if (estructuraDatosSuplementariosAux != null)
    {
        int lengthEstructuraDatosSuplementariosAux = estructuraDatosSuplementariosAux.size();
        int lengthValoresDatosSuplementariosAux = valoresDatosSuplementariosAux.size();
        if (lengthEstructuraDatosSuplementariosAux > 0)
        {
            int j=0;
            int yy=0;

            for (int hh = 0; hh < lengthEstructuraDatosSuplementariosAux; hh++)
            {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(hh);
                CamposFormulario cF = (CamposFormulario) valoresDatosSuplementariosAux.elementAt(hh);
                String nombre = eC.getCodCampo();
                String valor = cF.getString(nombre);
                String tipo = eC.getCodTipoDato();
%>
        datosSuplementarios[contDatosSuplementarios] = ['<%=nombre%>','<%=tipo%>','<%=eC.getObligatorio()%>','<%=eC.getActivo()%>'];
        contDatosSuplementarios++;
<%
            }
        }
    }
%>      

var tab;
<% if (mostrarOrigen.equals("si")) {%>
    tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'), 950,'<%=descriptor.getDescripcion("numResultados")%>');
    tab.addColumna('90', null, '<%=escape(descriptor.getDescripcion("gEtiq_Documento"))%>');
    tab.addColumna('280', 'left', '<%=escape(descriptor.getDescripcion("etiqNomCompleto"))%>');
    tab.addColumna('420', 'left', '<%=escape(descriptor.getDescripcion("etiqDirPrincipal"))%>');
    tab.addColumna('120', 'center', '<%=escape(descriptor.getDescripcion("etiqOrig"))%>');
<%} else {%>
    tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'), 950,'<%=descriptor.getDescripcion("numResultados")%>');
    tab.addColumna('90', null, '<%=escape(descriptor.getDescripcion("gEtiq_Documento"))%>');
    tab.addColumna('310', 'left', '<%=escape(descriptor.getDescripcion("etiqNomCompleto"))%>');
    tab.addColumna('500', 'left', '<%=escape(descriptor.getDescripcion("etiqDirPrincipal"))%>');
<%}%>

tab.displayCabecera=true;
tab.displayTabla();

// Funcion ejecutada al seleccionar una fila de la tabla
function mostrarFilaTabla(datos){ 
    var i = tab.selectedIndex;
    if (modoActual != seleccion) pasarAModoSeleccion();
    if((i>=0)&&(!tab.ultimoTable)){        
        mostrarTercero(i);
    } else { // Ninguna fila seleccionada        
        limpiarInteresado();
        limpiarDomicilio();
        mostrarCapaDomicilios(false);
        limpiarCamposSuplementariosTerceroSinModoActual();
    }
}

tab.displayDatos = mostrarFilaTabla;

// funcion que se llama al hacer doble clic sobre la tabla
function callFromTableTo(rowID,tableName){ 
<% if (!inicioTercero.equals("si")) {%> // Aplicaciones de Registro y expedientes        
     pulsarSeleccionar();
<% } else { // Aplicacion de Terceros y Territorio %>
     ;
<% } %>
}

 var coordx=0;
 var coordy=0;

 <%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>



document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var aux=null;
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }
    else 
        teclaAuxiliar = evento.which;
    
    keyDel(evento);    
    if ((teclaAuxiliar == 40) || (teclaAuxiliar == 38)){ // Arriba/abajo
        upDownTable(tab,datosTabla,teclaAuxiliar);    
    } else if (teclaAuxiliar == 37) {  // Izquierda
        //pulsarAnteriorDomicilio();
    } else if (teclaAuxiliar == 39) {  // Derecha
        //pulsarSiguienteDomicilio();
    }


    if (teclaAuxiliar == 1){
       
        if (comboProvincia.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboProvincia.ocultar()',20);
        if (comboMunicipio.base.style.visibility == "visible"&& isClickOutCombo(comboMunicipio,coordx,coordy)) setTimeout('comboMunicipio.ocultar()',20);
        if (comboTipoDoc.base.style.visibility == "visible"&& isClickOutCombo(comboTipoDoc,coordx,coordy)) setTimeout('comboTipoDoc.ocultar()',20);
        if (comboPostal.base.style.visibility == "visible"&& isClickOutCombo(comboPostal,coordx,coordy)) setTimeout('comboPostal.ocultar()',20);
        if (comboECO.base.style.visibility == "visible"&& isClickOutCombo(comboECO,coordx,coordy)) setTimeout('comboECO.ocultar()',20);
        if (comboESI.base.style.visibility == "visible"&& isClickOutCombo(comboESI,coordx,coordy)) setTimeout('comboESI.ocultar()',20);

        // Oculta  los combos que pueda haber en la pestaña de datos suplementarios del tercero
        for(i=0;i<nombreCombos.length;i++) {
            var nCombo=eval('combo'+nombreCombos[i]);
            eval('if (nCombo.base.style.visibility == "visible" && isClickOutCombo(nCombo,coordx,coordy)) nCombo.ocultar()');
        }
    }// if
    
    if (teclaAuxiliar == 9){
        
        if (comboECO.base.style.visibility == "visible" && isClickOutCombo(comboECO,coordx,coordy)) setTimeout('comboECO.ocultar()',20);
        if (comboESI.base.style.visibility == "visible" && isClickOutCombo(comboESI,coordx,coordy)) setTimeout('comboESI.ocultar()',20);
    }
}

// funcion que se llama al seleccionar una fila de la tabla
function rellenarDatos(tableName,listName){
    mostrarFilaTabla(undefined);
}

// COMBOS
var	comboProvincia = new Combo("Provincia");
var	comboMunicipio = new Combo("Municipio");
var	comboTipoDoc = new Combo("TipoDoc");
var comboPostal = new Combo("Postal");
var comboECO = new Combo("ECO");
var comboESI = new Combo("ESI");

var	auxCombo = 'comboMunicipio';

comboProvincia.change =
    function() {
    auxCombo='comboMunicipio';
    limpiar(['codMunicipio','descMunicipio','txtCodVia','descVia','txtDomicilio','codTVia']);
    if(comboProvincia.cod.value.length!=0){
        desHabilitarECOESIVIA(true);
        cargarListaMunicipios();
    } else{
        comboMunicipio.addItems([],[]);
    }
}

comboMunicipio.change =
    function() {
    if (!cargandoModificar && document.forms[0].codProvincia.value != "") {
        limpiar(['txtCodVia','txtDomicilio', 'codTVia','descVia']);
        if(comboMunicipio.cod.value.length!=0){
            if( (comboMunicipio.cod.value=="<%=ptVO.getMunicipio()%>") &&  ( comboProvincia.cod.value=="<%=ptVO.getProvincia()%>")) {
                desHabilitarECOESIVIA(false);
            } else {
                desHabilitarECOESIVIA(true);
            }
            cargarListaCodPostales();
        } else {
            comboMunicipio.selectItem(0);
        }
    }
}

comboTipoDoc.change = function() {

    //if(document.getElementById('codTipoDoc').value != 1){
         document.forms[0].txtDNI.value="";
        actualizaPersonaFJ();
   // }
}
function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
}

comboECO.change = function() {
    auxCombo='comboESI';
    limpiar(['codESI','descESI','descVia','txtCodVia', 'idVia', 'txtBarriada']);
    if(comboECO.cod.value.length!=0){
        var i = comboECO.selectedIndex-1;
        if(i>=0){
            document.forms[0].txtBarriada.value = descECOs[i];
        }
    }
    cargarListas();
}

comboESI.change = function() {
    limpiar(['descVia','txtCodVia']);
    if(comboESI.cod.value.length!=0){
        var i = comboESI.selectedIndex-1;
        if(i>=0){
            document.forms[0].codECO.value = listaECOESIs[i][0];
            document.forms[0].descECO.value = listaECOESIs[i][1];
            document.forms[0].txtBarriada.value = descESIs[i] +" - "+document.forms[0].descECO.value;
        }
        cargarListas();
    }
}
    </script>
    </body>
</html>
