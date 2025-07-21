<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.util.ValidarDocumento" %>
<%@page import="java.util.Vector" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@ page import="es.altia.util.conexion.AdaptadorSQLBD"%>
<%@ page import="es.altia.util.conexion.AdaptadorSQL"%>
<%@ page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.technical.ConstantesDatos"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.ResourceBundle"%>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>

<html:html>
<head>
<TITLE>::: REXISTRO ENTRADA - Alta :::</TITLE>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
response.setHeader("Cache-control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);
MantAnotacionRegistroForm mantARForm;
if (request.getAttribute("MantAnotacionRegistroForm") != null) mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
else mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");

UsuarioValueObject usuarioVO = new UsuarioValueObject();
RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
Config m_Conf = ConfigServiceHelper.getConfig("common");
Config m_Registro = ConfigServiceHelper.getConfig("Registro");
String restriccion_horas = m_Registro.getString("restriccion_fecha_presentacion");
String permitir_contestar = m_Registro.getString("RELACIONAR_ENTRADA_SALIDA_APLICACIONES_INDIVIDUALES");
String JSP_poblacion = m_Conf.getString("JSP.Poblacion");
String JSP_autoridad = m_Conf.getString("JSP.Registro.Autoridad");
String pv = "visible";
if ("no".equals(JSP_poblacion)) pv = "hidden";
String aut = "visible";
if ("no".equals(JSP_autoridad)) aut = "hidden";


Boolean datosSga=false;	
try{	
    datosSga=m_Registro.getString("mostrar_datos_sga").toUpperCase().equals("SI");	
}catch(Exception e){	
    datosSga=false;	
}

Boolean mostrarTituloOficina = false;	
if(m_Registro.getString("mostrar_titulo_oficina_registro").toUpperCase().equals("SI")){	
 mostrarTituloOficina=true;	
}



int idioma = 2;
int apl = ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA;
int aplActual=1;
int cod_org = 1;
int cod_dep = 1;
int cod_ent = 1;
int cod_unidOrg = 1;
String css="";
boolean permisoMantenimiento = false;
boolean permisoMantenimientoSalida = false;
String desc_org = "";
String desc_ent = "";
String respOpcion = "";
String funcion = "";
String dil = "";
String idSesion = session.getId();
int codUsu = 1;
String tituloUor="";


if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)) {
    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
    regUsuarioVO = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
    codUsu=usuarioVO.getIdUsuario();
    idioma = usuarioVO.getIdioma();
    cod_org = usuarioVO.getOrgCod();
    cod_dep = regUsuarioVO.getDepCod();
    cod_ent = usuarioVO.getEntCod();
    cod_unidOrg = regUsuarioVO.getUnidadOrgCod();
    desc_org = usuarioVO.getOrg();
    desc_ent = usuarioVO.getEnt();
    css=usuarioVO.getCss();
    aplActual=usuarioVO.getAppCod();
    tituloUor=usuarioVO.getUnidadOrg();
    tituloUor = new String(tituloUor.getBytes("ISO-8859-1"), "UTF-8");
    
}

String BORRARCAMPOS  = "";
try{
    ResourceBundle portafirmas = ResourceBundle.getBundle("Registro");
    BORRARCAMPOS  = portafirmas.getString(cod_org+"/EliminarCamposTrasCambiarTEntrada");
}catch(Exception e){
  BORRARCAMPOS = "";
}
    
boolean hayBuscada = false;
boolean hayModificada = false;
boolean contestacion = false;
boolean asientoRelacionado = false;
boolean altaDesdeTramitar = false;

if (session.getAttribute("modoInicio") != null) {
    if ("recargar_buscada".equals((String) session.getAttribute("modoInicio"))) hayBuscada = true;
    else if ("contestar_entrada".equals((String) session.getAttribute("modoInicio"))) contestacion = true;
    else if ("asientoRelacionado".equals((String) session.getAttribute("modoInicio"))) asientoRelacionado = true;
    else if ("hayModificada".equals((String) session.getAttribute("modoInicio"))) hayModificada = true;
    else if ("alta_desde_tramitar".equals((String) session.getAttribute("modoInicio"))) altaDesdeTramitar = true;
    session.removeValue("modoInicio");
}

String tipoAnotacion = "S";
if (session.getAttribute("tipoAnotacion") != null) tipoAnotacion = (String) session.getAttribute("tipoAnotacion");

String titPag;
String tipo;
String fech;
String hora;
String numOrden;
String destino;
String origen;

if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
    permisoMantenimiento = usuarioVO.getMantenerEntrada();
    permisoMantenimientoSalida = usuarioVO.getMantenerSalida();
    titPag = "tit_AnotE";
    tipo = "res_tipoEntrada";
    fech = "res_fecE";
    hora = "res_HoraEnt";
    numOrden = "res_numOrdE";
    destino = "res_etiqDestino";
    origen = "res_etiqOrigen";
} else {
    permisoMantenimiento = usuarioVO.getMantenerSalida();
    titPag = "tit_AnotS";
    tipo = "res_tipoSalida";
    fech = "res_fecS";
    hora = "res_HoraSal";
    numOrden = "res_numOrdS";
    destino = "res_etiqOrigen";
    origen = "res_etiqDestino";
}

boolean desdeEntradasRechazadas=false;

if(session.getAttribute("entradasRechazadas")!=null && session.getAttribute("entradasRechazadas").equals("S")){
        desdeEntradasRechazadas=true;    
}

boolean desdePendientesFinalizar = false;
if(session.getAttribute("entradasPendientes")!=null && session.getAttribute("entradasPendientes").equals("S")){
    desdePendientesFinalizar = true;
}
if (session.getAttribute("reservas") != null) respOpcion = (String) session.getAttribute("reservas");
session.removeValue("reservas");
Config m_Config = ConfigServiceHelper.getConfig("common");
String statusBar = m_Config.getString("JSP.StatusBar");
Calendar calendar = Calendar.getInstance();
String ejercicioActual = java.lang.Integer.toString(calendar.get(Calendar.YEAR));

// Se comprueba si hay bloqueo de fecha y hora en caso de hacer una respuesta  para una anotación de salida
String bloqueo = mantARForm.getBloquearFechaHoraPresentacion();

Integer oficinasPermiso = (Integer) session.getAttribute("numPermisosOficinaRegistro");

String userAgent = request.getHeader("user-agent");

boolean permiso_contestar=true;

String directiva_salidas_uor_usuario = "NO";
if (session.getAttribute("directiva_salidas_uor_usuario") != null) directiva_salidas_uor_usuario = (String) session.getAttribute("directiva_salidas_uor_usuario");


if ((("S".equals(tipoAnotacion)) || ("Relacion_S".equals(tipoAnotacion)))&&("SI".equals(directiva_salidas_uor_usuario))) {
    permiso_contestar=false;
}else{
    if (("NO".equals(permitir_contestar))&&(aplActual!=apl)) {
        permiso_contestar=false;
    }
}

boolean mostrarDigitalizar = false;
boolean mostrarCotejo = false;
String servicioDigitalizacionActivo = (String) session.getAttribute("servicioDigitalizacionActivo");
if((tipoAnotacion.equals("E") || tipoAnotacion.equals("Relacion_E"))
    && servicioDigitalizacionActivo != null && servicioDigitalizacionActivo.equalsIgnoreCase("si")){
        mostrarDigitalizar = true;
} else if(servicioDigitalizacionActivo == null || servicioDigitalizacionActivo.equalsIgnoreCase("no")){
	mostrarCotejo = true;
}

// Integracion SIR - LANBIDE
String integracionSIRLanbide = "0";
try{
    integracionSIRLanbide = m_Registro.getString("INTEGRACION_SIR_LANBIDE");
}catch(Exception ex){
    ex.printStackTrace();
    %> <script type="text/javascript">console.log(<%=ex.getMessage()%>);</script>
<%}

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

<script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/json2.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaBusquedaTerceros.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>?<%=System.currentTimeMillis()%>">
<script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/altaRE.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/digitalizacion.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/gestionTerceros.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/uor.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/registro/gestionDocAportadosAntURL_ATASE.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/registro/gestionDocumentosRegistroDokusi.js'/>?<%=System.currentTimeMillis()%>"></script>
<% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
<script type="text/javascript" src="<html:rewrite page='/scripts/sir/gestionRegistroSalidaSIR.js'/>?<%=System.currentTimeMillis()%>"></script>
<%}else{%>
<script type="text/javascript" src="<html:rewrite page='/scripts/sir/gestionRegistroEntradaSIR.js'/>?<%=System.currentTimeMillis()%>"></script>
<%}%>
<script type="text/javascript" src="<html:rewrite page='/scripts/sir/gestionRegistroSIRGeneral.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/sir/gestionRegistroSIRBuscadorDIR3.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/sir/underscore-umd.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/sir/barraPaginacionBuscador.js'/>?<%=System.currentTimeMillis()%>"></script>

<script type="text/javascript"> 
dojo.require("dojo.io.*");
dojo.require("dojo.event.*");

var MENSAJE_RANGO_ANO_ENTRADA_RELACIONADA_NO_VALIDO = '<%=descriptor.getDescripcion("msjRangoEjeAnotRelInvalido")%>';
// Mensaje a mostrar cuando no se selecciona tipo de entrada en la consulta
var MENSAJE_TIPO_ENTRADA_REQUERIDO = 'Por favor, ingrese un tipo de entrada';
var BLOQUEO_FECHA_HORA_ANOTACION = false;

<%
	if("SI".equals(bloqueo)){
%>
	BLOQUEO_FECHA_HORA_ANOTACION = true;
<%
	}
%>
    
var NUM_OFICINAS_REGISTRO_USUARIO = <%=oficinasPermiso %>;
var estaPluginExpRelacionadosFlexiaCargado = "<%=(String)session.getAttribute("plugin_exp_relacionados_flexia")%>";
var ejercicioActual = "<%= ejercicioActual %>";
var EsquemaGenerico = "<%=es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO%>"
// almacenar en vars algunos mensajes de la app
mensajeCodigoIncorrecto ='<%=descriptor.getDescripcion("msjNoCodigo")%>'; // Idioma
var mensajeHora = '<%=descriptor.getDescripcion("horaNoVal")%>';
var mensajeFechaNoValida='<%=descriptor.getDescripcion("fechaNoVal")%>';

// === UORs
var uors = new Array();
var uorcods = new Array();
var uorcodsinternos = new Array();
var uordescs = new Array();
var uorPermiteDigitalizacion = <%=regUsuarioVO.isUnidadOrganicaDigit()%>;
<%
Vector listaUORDTOs = mantARForm.getListaNuevasUORs();
for (int j = 0; j < listaUORDTOs.size(); j++) {
    UORDTO dto = (UORDTO) listaUORDTOs.get(j);
%>
// array con los objetos tipo uor mapeados por el array de arriba
    uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
    // array con los códigos visibles
    uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
    uorcodsinternos[<%=j%>] = '<%=dto.getUor_cod()%>';
    uordescs[<%=j%>] = "<str:escape><%=dto.getUor_nom()%></str:escape>";
<%
}
%>
    
// === fin seccion UORs
// Para saber si activar o no las notificaciones al cambiar la unidad organica
var changeNotifs = false;

// Para operaciones duplicar y contestar.
var ejercicioBuscada='';
var numeroBuscada='';
var tipoBuscada='';

// Listas de valores.
var cod_EstadosSIR = new Array();
var desc_EstadosSIR = new Array();
var cod_tiposDocumentos = new Array();
var desc_tiposDocumentos = new Array();
var act_tiposDocumentos = new Array();
var cod_tiposDocumentosAlta = new Array();	
var desc_tiposDocumentosAlta = new Array();	
var act_tiposDocumentosAlta = new Array();	
var cod_tiposDocumentosAux = new Array();	
var desc_tiposDocumentosAux = new Array();
var cod_tiposTransportes = new Array();
var desc_tiposTransportes = new Array();
var act_tiposTransportes = new Array();
var cod_tiposRemitentes= new Array();
var desc_tiposRemitentes= new Array();
var act_tiposRemitentes= new Array();
var desc_temas = new Array();
var cod_temas = new Array();
var cod_tipoEntrada=new Array();
var desc_tipoEntrada=new Array();
var cod_tiposIdInteresado = new Array();
var desc_tiposIdInteresado = new Array();
var cod_departamentos= new Array();
var desc_departamentos= new Array();
var cod_procedimientos = new Array();
var desc_procedimientos = new Array();
var mun_procedimientos = new Array();
var digit_procedimientos = new Array();
var cod_roles = new Array();
var desc_roles = new Array();
var defecto_roles = new Array();
var terceros = new Array();
var terceroCargado = '';
var terceroActual = -1; // Indice en el vector 'terceros' del tercero mostrado
var listaPlantillas= new Array();
var codlistaPlantillas= new Array();
// Relaciones entre asientos, elementos: 0 = tipo, 1 = ejercicio, 2 = numero
var relaciones = new Array();
var tipoActual;
var existenAnexosFormularios='no';
var numExpedienteAux = "";
var ROL_TER_ANULAR_RES = "<%=ConstantesDatos.ROL_TER_ANULAR_RES %>";

<%
if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {
%>
    tipoActual = 'E';
<%
} else {
%>
    tipoActual = 'S';
<%
}
%>

var listaDoc = new Array();
var listaDocEntregados = new Array();
var listaAnt=new Array();	
var listaAntEntregados=new Array();
var listaUorsCorreo = new Array();
//cuando viene de cancelar_anular (pulsar boton contestar)necesitamos importar los datos de lista interesados
var cod_estadosAnotaciones= new Array(0,1,2,9,-9,3);
var desc_estadosAnotaciones= new Array('<%=descriptor.getDescripcion("etiq_pendientes")%>',
        '<%=descriptor.getDescripcion("etiq_aceptadas")%>', '<%=descriptor.getDescripcion("etiq_rechazadas")%>',
        '<%=descriptor.getDescripcion("etiq_anuladas")%>', '<%=descriptor.getDescripcion("etiq_noAnuladas")%>',
        '<%=descriptor.getDescripcion("etiq_asocExp")%>');

<% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
    cod_tipoEntrada = [0,1];
    desc_tipoEntrada = ['<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("entradaOrd"))%>'
        ,'<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("salidaSIR"))%>'];
<% } else {%>
    cod_tipoEntrada = [0,1,2];
    desc_tipoEntrada = ['<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("entradaOrd"))%>',
            '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("DestOtroReg"))%>',
            '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("procOtroReg"))%>'];
<% }%>

//Arrays para Pestanha Formularios
var listaFormularios = new Array();
var listaFormulariosOriginal = new Array();

// Funciones rejilla
var lista= new Array();
var i;
var cont = 0;
// Variable tipo de documento
var tipoDoc;
var dTipoDoc;
// Variable texto Diligencia
var textoDil;
// Variable para saber si estamos modificando una anotacion (0) o no (1).
// Se le da el valor 0 en 'pulsarModificar' y 1 en 'registroCerradoAlModificar' y 'pulsarCancelarModificar'
var mod;
var fechaHoy;
var fecha;
var modificarGrabar=0;
var estadolabelTipoDoc=1;
// Variable para saber al cambiar de procedimientoo asunto si hemos elegido uno
// diferente al que ya estaba. Se iniciliza en 'inicializar' o 'recuperaDatos'
var cod_procedimiento_anterior;
var indice_asunto_anterior = 0;
// Variable con el codigo del rol por defecto
var rolPorDefecto = '<bean:write name="MantAnotacionRegistroForm" property="codRolDefecto"/>';
var descRolPorDefecto = "<str:escape><bean:write name="MantAnotacionRegistroForm" property="descRolDefecto" filter="false"/></str:escape>";

// Diligencias anulacion.
var estadoAnot;
var diligenciaAnulacion="";

var codUsu='<%=codUsu%>';
var codOrg='<%=cod_org%>';

var aplic =<%=apl%>;
var idiom = <%=idioma%>;
var litMosPagDePags = '<%=descriptor.getDescripcion("mosPagDePags")%>';
var litAnterior = '<%=descriptor.getDescripcion("anterior")%>';
var litSiguiente = '<%=descriptor.getDescripcion("siguiente")%>';

// #278739
var responderASalida = "<%= session.getAttribute("tipoAnotacionBuscada")%>";

 
var documentosModificados=false;
var mostrarDigitalizar = <%=mostrarDigitalizar%>;

var integracionSIRLanbide = <%=integracionSIRLanbide%>;

function inicializar() {
    modificando('N');
    tp1.setSelectedIndex(0);
    compruebaModificadoRegistro();
    <% if (!contestacion && !hayBuscada && !asientoRelacionado && !altaDesdeTramitar && !("reservas".equals(respOpcion))) {%>
        activarFormularioConsulta();
        document.forms[0].enviarCorreo.disabled= true;
        document.getElementById('checkBoxNotificar').className = "etiquetaDeshabilitada";
        document.forms[0].enviarCorreo.checked = false;
        document.forms[0].fechaDoc.value="";
        cargarSoloListas();
        ocultarBotonesInteresados();
        mostrarCapasBotones("capaBotonesConsulta");
        ActivaBotonVerAnexosFormularios();
        desactivarRelaciones();
        cargarTipoDocAux("Todos");
        cod_procedimiento_anterior = document.forms[0].cod_procedimiento.value;
        // Valores por defecto que no se quieren.
        // Tipo remitente
        document.forms[0].cod_tipoRemitente.value='';
        actualizarDescripcion('cod_tipoRemitente','txtNomeTipoRemitente',cod_tiposRemitentes,desc_tiposRemitentes);
        // Tipo transporte
        document.forms[0].txtNumTransp.value=''; // Num transporte
        document.forms[0].cod_tipoTransporte.value='';
        actualizarDescripcion('cod_tipoTransporte','desc_tipoTransporte',cod_tiposTransportes,desc_tiposTransportes);
        // Tipo documento
        document.forms[0].cbTipoDoc.value="";
        document.forms[0].descTipoDoc.value="";
        document.forms[0].txtCodigoDocumento.value = '';
        actualizarDescripcion('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentos,desc_tiposDocumentos);
        // Tipo entrada
        document.forms[0].cbTipoEntrada.value="0";
        document.forms[0].txtNomeTipoEntrada.value="";
        consultando=true;
        mostrarDestino();
        actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
    <% } else if("reservas".equals(respOpcion)){%>
        
        cargarTipoDocAux("Alta");
        var obligatorioAsunto = <%=mantARForm.getObligatorioAsuntoCodificado()%>;
        if(obligatorioAsunto == true){            
            cambiarEstadoComboAsuntoCodificado(true);
        }        
        document.forms[0].ano.value = "<bean:write name="MantAnotacionRegistroForm" property="fechaAnotacion"/>".substring(6, 10);
	// Colocamos el foco en el campo de tipo de documento de interesado.
	document.forms[0].cbTipoDoc.focus();
    <% }else {%>
        cargarTipoDocAux("Alta");
         <%if(contestacion){%>	
            cargarSoloListas();	
            compruebaTipoDoc(document.forms[0].txtCodigoDocumento.value,document.forms[0].txtNomeDocumento.value);	
        <%}%>
        // TODO adaptado para contestar
        if((document.forms[0].cod_uor.value != '')&&(document.forms[0].cod_uniRegDestinoORD.value == '')) {
            document.forms[0].cod_uniRegDestinoORD.value = '<%=request.getAttribute("codigo_uor_real")%>';
        }
        consultando=false;
        var vCamposCodigo= camposCodigo();
        cambiarLongMaxInput(vCamposCodigo,longMaxInputCodigo);
        var vCamposFecha = camposFecha();
        cambiarLongMaxInput(vCamposFecha,longMaxInputFecha);

    <% }%>

	if(BLOQUEO_FECHA_HORA_ANOTACION) bloquearFechaHoraAnotacion();
        analizarDocumento();
        numExpedienteAux = document.forms[0].txtExp1.value;
        
        if(mostrarDigitalizar){
             $("#capaBotones2 [name='cmdRegistrarAlta']").hide();
        }

        if(typeof integracionSIRLanbide != undefined  && integracionSIRLanbide == "1"){
            inicializarPaginaPrincipalAltaRE();
        }

}

<% if ("reservas".equals(respOpcion)) {%>
function modificarReserva() {  
    fechaHoy = "<bean:write name="MantAnotacionRegistroForm" property="fechaHoraHoy"/>";
    activarFormulario();
    deshabilitarBuscar();
    desactivarOrigenYExpediente();
    desactivarInteresado();
    ocultarInfSoloConsulta();
    actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
    cod_procedimiento_anterior = document.forms[0].cod_procedimiento.value;
    document.forms[0].fechaDocumento.value = fechaHoy;
    var vectorFecDoc = [document.forms[0].fechaDocumento,document.forms[0].fechaAnotacion];
    deshabilitarGeneral(vectorFecDoc);
    document.forms[0].fecPresRes.value = document.forms[0].fechaAnotacion.value;
    document.forms[0].cbTipoDoc.value = "";
    document.forms[0].descTipoDoc.value = "";
    borrarInteresado();
    var vCamposCodigo= camposCodigo();
    cambiarLongMaxInput(vCamposCodigo,longMaxInputCodigo);
    var vCamposFecha = camposFecha();
    cambiarLongMaxInput(vCamposFecha,longMaxInputFecha);
    <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) { %>
        document.forms[0].horaMinDocumento.value = document.forms[0].horaMinAnotacion.value;
        $("[name='cmdRexistrarModificacion']").hide();
        $("[name='cmdDigitalizarDesdeConsulta']").hide();
        <% if ("no".equals((String) request.getAttribute("botonGuardarDuplicar"))) { %>
            $("[name='cmdGuardarDuplicar']").hide();
        <% }else{%>
            $("[name='cmdGuardarDuplicar']").show();
        <% } %>
    <% } %>
    ocultarBotonesNavegacionInteresados();
    mostrarCapasBotones('capaBotones2');
    tp1.setSelectedIndex(0);
    cargarSoloListas();
    valoresPorDefectoCombos();
    modificando('S');
    mostrarDestino();
    var m = 0;
    <%
        Vector listaRoles = mantARForm.getListaRoles();
        for(int t=0;t<listaRoles.size();t++) {
        %>
            cod_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>'];
            desc_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>'];
            defecto_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>'];
            m++;
        <% } %>
    comboRol.addItems(cod_roles, desc_roles);
    mostrarCapaDatosBusqueda();
    mostrarBotonesEdicionInteresados();
    deshabilitarImagenCal("calFechaAnotacion",true);
}
<%  } %>

function mostrarDestino() {
    // === INICIO LOG DE ENTRADA ===
    console.log("[mostrarDestino] INICIO. cbTipoEntrada:", document.forms[0].cbTipoEntrada.value);

    var borrarCampos = "<%=BORRARCAMPOS%>";
    var elementos = [
        document.forms[0].txtCodigoDocumento,
        document.forms[0].txtNomeDocumento,
        document.forms[0].cod_uor,
        document.forms[0].desc_uniRegDestinoORD
    ];

    <%-- 1 · Sólo aplica a ENTRADAS (E / Relacion_E) --%>
    <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) { %>
    var tipoEntrada = document.forms[0].cbTipoEntrada.value;
    if (document.getElementById("anoEjercicio")) {
    var campoAno = document.getElementById("anoEjercicio");
        if (tipoEntrada === '1' && !document.forms[0].opcion.value.startsWith('cancelar_anular')) {
            console.log('[mostrarDestino] ANTES de borrar. ano=', document.forms[0].ano.value,
                ' numero=', document.forms[0].numero.value);
            campoAno.value = ejercicioActual;
        } else if (campoAno.value.trim() === "") {
            // Solo establecer por defecto cuando el usuario no ha introducido
            // un ejercicio explicito.
            campoAno.value = ejercicioActual;
            console.log('[mostrarDestino] DESPUÉS. ano=', document.forms[0].ano.value,
                ' numero=', document.forms[0].numero.value);
        }
    }
    /* ---------------------------------------------------------------- */

    /* ====== 2 · Dispatcher principal según tipoEntrada =============== */
    if (tipoEntrada === '') {                /* --- SIN tipo entrada --- */
        console.log("[mostrarDestino] Caso: SIN tipo entrada");
        borrarDestinoOrdinaria();
        borrarDestinoOtroReg();
        borrarProcedenteOtroReg();
        if (document.getElementById("TEOtroReg")) document.getElementById("TEOtroReg").style.display = 'none';
        if (document.getElementById("capaOrigen")) document.getElementById("capaOrigen").style.display = 'none';
        tp1_p3.setPrimerElementoFoco(document.forms[0].cod_uor);
        habilitarTipoDocYRemitente();
        habilitarExpediente();
        if (document.getElementById('etiquetaTipoRem')) document.getElementById('etiquetaTipoRem').className = "etiqueta";
        if (document.getElementById('etiquetaTipoDoc')) document.getElementById('etiquetaTipoDoc').className = "etiqueta";
        if ($("#unidadDestinoEntradaSIR").length > 0) $("#unidadDestinoEntradaSIR").hide();
        $("#gEtiqUnidOrigenDestino").text($("#etiqUnidDestino").val());
        if (document.getElementById("divDatosRegistroSIR")) document.getElementById("divDatosRegistroSIR").style.display = 'none';
        if (document.getElementById("capaTiposEstadoAnotacion")) document.getElementById("capaTiposEstadoAnotacion").style.display = 'block';
        if (document.getElementById("capaTiposEstadoSIR")) document.getElementById("capaTiposEstadoSIR").style.visibility = 'hidden';
    } else if (tipoEntrada === '0') {  // Destino ordinario
        console.log("[mostrarDestino] Caso: DESTINO ORDINARIO");
        borrarDestinoOtroReg();
        borrarProcedenteOtroReg();
        habilitarExpediente();
        if (document.getElementById("TEOtroReg")) document.getElementById("TEOtroReg").style.display = 'none';
        if (document.getElementById("capaOrigen")) document.getElementById("capaOrigen").style.display = 'none';
        tp1_p3.setPrimerElementoFoco(document.forms[0].cod_uor);
        if (top.menu.modificando == 'S') normalAobligatorio(elementos);
        habilitarTipoDocYRemitente();
        if (document.getElementById('etiquetaTipoRem')) document.getElementById('etiquetaTipoRem').className = "etiqueta";
        if (document.getElementById('etiquetaTipoDoc')) document.getElementById('etiquetaTipoDoc').className = "etiqueta";
        if ($("#unidadDestinoEntradaSIR").length > 0) $("#unidadDestinoEntradaSIR").hide();
        $("#gEtiqUnidOrigenDestino").text($("#etiqUnidDestino").val());
        if (document.getElementById("divDatosRegistroSIR")) document.getElementById("divDatosRegistroSIR").style.display = 'none';
        if (document.getElementById("capaTiposEstadoAnotacion")) document.getElementById("capaTiposEstadoAnotacion").style.display = 'block';
        if (document.getElementById("capaTiposEstadoSIR")) document.getElementById("capaTiposEstadoSIR").style.visibility = 'hidden';
    } else if (tipoEntrada === '1') {  // Destino otro registro
        console.log("[mostrarDestino] Caso: DESTINO OTRO REGISTRO");

        /* ????????????????????????????????????????????????????????????????
           1· Guardamos la selección actual para que no se pierda
        ????????????????????????????????????????????????????????????????? */
        const _anoSel    = document.forms[0].ano    ? document.forms[0].ano.value    : "";
        const _numSel    = document.forms[0].numero ? document.forms[0].numero.value : "";

        /* ????????????????????????????????????????????????????????????????
           2· Rutinas que limpian campos (mantengo la condición original)
        ????????????????????????????????????????????????????????????????? */
        if (!borrarCampos || borrarCampos !== "0") {
            borrarDestinoOrdinaria();
            borrarProcedenteOtroReg();
            borrarYDeshabilitarExpediente();
        }

        /* ????????????????????????????????????????????????????????????????
           3· Restauramos ano / numero si se han vaciado durante el borrado
        ????????????????????????????????????????????????????????????????? */
        if (document.forms[0].ano    && document.forms[0].ano.value    === "") {
            document.forms[0].ano.value = _anoSel;
        }
        if (document.forms[0].numero && document.forms[0].numero.value === "") {
            document.forms[0].numero.value = _numSel;
        }

        /* ????????????????????????????????????????????????????????????????
           4· La lógica original continúa sin cambios
        ????????????????????????????????????????????????????????????????? */
        if ($("#TEOtroReg").length) $("#TEOtroReg").show();

        if (typeof integracionSIRLanbide !== 'undefined' && integracionSIRLanbide == "1") {
            if ($("#TEOtroReg #prueba2").length) {
                deshabilitarGeneral($("#TEOtroReg #prueba2").find('input,span,a'));
                deshabilitarIconos($("#TEOtroReg #prueba2").find('span'), true);
            }
            $("#gEtiqUnidOrigenDestino").text($("#etiqUnidOrigen").val());
            if ($("#unidadDestinoEntradaSIR").length) {
                $("#unidadDestinoEntradaSIR").show();
                $("#codigoUnidadDestinoSIR, #nombreUnidadDestinoSIR").prop("disabled", true);
                bloquearUnidadDestino(true);
                bloquearUnidadProcedimiento(true);
            }
        } else {
            if ($("#unidadDestinoEntradaSIR").length) {
                $("#unidadDestinoEntradaSIR").hide();
                desBloquearUnidadDestino();
                desBloquearUnidadProcedimiento();
            }
        }

        if ($("#TEOtroReg #prueba2").length) {
            const visible = $('#TEOtroReg #prueba2 [name="cod_orgDestino"]').val() ||
                $('#TEOtroReg #prueba2 [name="cod_uniRegDestino"]').val();
            $("#TEOtroReg").toggle(!!visible);
        }

        if ($("#capaOrigen").length) $("#capaOrigen").hide();
        tp1_p3.setPrimerElementoFoco(document.forms[0].cod_orgDestino);

        if (!borrarCampos || borrarCampos !== "0") {
            document.forms[0].txtCodigoDocumento.value   = '';
            document.forms[0].txtNomeDocumento.value     = '';
            document.forms[0].cod_tipoRemitente.value    = '';
            document.forms[0].txtNomeTipoRemitente.value = '';
        }
        if (top.menu.modificando == 'S') obligatorioAnormal(elementos);
        deshabilitarTipoDocYRemitente();
        if ($("#divDatosRegistroSIR").length) $("#divDatosRegistroSIR").show();
        $("#capaTiposEstadoAnotacion").hide();
        $("#capaTiposEstadoSIR").css("visibility", "visible");
    } else if (tipoEntrada === '2') {        /* --- DESTINO PROCEDENTE registro --- */
        console.log("[mostrarDestino] Caso: DESTINO PROCEDENTE DE OTRO REGISTRO");
        borrarDestinoOtroReg();
        if ($("#TEOtroReg").length) $("#TEOtroReg").hide();
        if ($("#capaOrigen").length) $("#capaOrigen").show();
        tp1_p3.setPrimerElementoFoco(document.forms[0].cod_uor);
        if (top.menu.modificando == 'S') normalAobligatorio(elementos);
        habilitarTipoDocYRemitente();
        if ($("#unidadDestinoEntradaSIR").length) $("#unidadDestinoEntradaSIR").hide();
        $("#gEtiqUnidOrigenDestino").text($("#etiqUnidDestino").val());
        if ($("#divDatosRegistroSIR").length) $("#divDatosRegistroSIR").hide();
        $("#capaTiposEstadoAnotacion").show();
        $("#capaTiposEstadoSIR").css("visibility", "hidden");
    }
    <%-- fin bloque ENTRADAS --%>

    <% } else { %>
    mostrarFiltrosProcOtraAdmin();
    /* ====== 3 · Caso para anotaciones de SALIDA ====== */
    console.log("[mostrarDestino] Caso: ELSE (no E ni Relacion_E)");
    if ($("#capaOrigen").length) $("#capaOrigen").hide();
    if (top.menu.modificando == 'S') normalAobligatorio(elementos);
    else                             obligatorioAnormal(elementos);
    if ($("#unidadDestinoEntradaSIR").length) $("#unidadDestinoEntradaSIR").hide();
    $("#gEtiqUnidOrigenDestino").text($("#etiqUnidDestino").val());
    if ($("#divDatosRegistroSIR").length) $("#divDatosRegistroSIR").hide();
    $("#capaTiposEstadoAnotacion").show();
    $("#capaTiposEstadoSIR").css("visibility", "hidden");
    <% } %>

    // === FIN LOG DE SALIDA ===
    console.log("[mostrarDestino] FIN.");
}

    <% if (permisoMantenimiento) { %>
function pulsarAlta() {   
    if(NUM_OFICINAS_REGISTRO_USUARIO == 1){
        vectorImg = new Array(document.getElementsByName("consultaExpediente")[0]);
        habilitarImagen(vectorImg, true);
        changeNotifs = true;
        mostrarCapaDatosBusqueda();
        cargarTipoDocAux("Alta");
                
        if (rolPorDefecto==''){
        jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjNoRol"))%>');
        } else {
            terceros = new Array();
            ocultarBotonesNavegacionInteresados();
            relaciones = new Array();
            listaDoc = new Array();
            listaDocEntregados=new Array();
            listaAnt=new Array();	
            listaAntEntregados=new Array();
            document.forms[0].codProcedimientoRoles.value='';
            document.forms[0].opcionAltaDesdeConsulta.value="NO";
            ocultarLista();
            ocultarCalendario();
            borrarPaginaBuscada();
            valoresPorDefectoCombos();
            crearListasRelacionesTxt();
            consultando=false;
            <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                document.forms[0].opcion.value="init_alta_entrada";
                 $("[name='cmdDigitalizarDesdeConsulta']").hide();
                 $("[name='cmdDigitalizarDesdeAlta']").hide();
                 $("[name='cmdFinDigitalizar']").hide();
            <% } else {%>
                document.forms[0].opcion.value="init_alta_salida";
                document.forms[0].tipoRegistro.value="S";
            <% }%>
            document.forms[0].target="oculto";
            document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
            // Datos SIR
            if($('[name="codigoUnidadDestinoSIRHidden"]') != undefined){
                $('[name="codigoUnidadDestinoSIRHidden"]').val($('[name="codigoUnidadDestinoSIR"]').val());
                $('[name="listaCodTerceroSIRHidden"]').val($('[name="listaCodTercero"]').val());
                $('[name="listaVersionTerceroSIRHidden"]').val($('[name="listaVersionTercero"]').val());
                $('[name="listaCodDomicilioSIRHidden"]').val($('[name="listaCodDomicilio"]').val());
            }
            document.forms[0].submit();
        }
    }else if(NUM_OFICINAS_REGISTRO_USUARIO == 0){
        jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_sinOficinaRegistro"))%>');
    }else if(NUM_OFICINAS_REGISTRO_USUARIO > 1){
        jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_masDeUnaOficinaRegistro"))%>');
    }//
}//pulsarAlta

function pulsarAltaDesdeConsulta() { 
    mostrarMensajeRegistroTelematico('');
    if(NUM_OFICINAS_REGISTRO_USUARIO == 1){
        mostrarCapaDatosBusqueda();
        ocultarCapaAnulacion();
        cargarTipoDocAux("Alta");
        changeNotifs = true;
        if (rolPorDefecto=='') {
            jsp_alerta("A", "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjNoRol"))%>");
        } else {
        document.forms[0].descTipoDoc.value="";
        document.forms[0].txtCodigoDocumento.value = '';
        actualizarDescripcion('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentos,desc_tiposDocumentos);
            terceros = new Array();
            mostrarBotonesEdicionInteresados();
            ocultarBotonesNavegacionInteresados();
            relaciones = new Array();
            listaDoc = new Array();
            listaDocEntregados= new Array();
            tabAnt.lineas = listaDocEntregados;
            tabAnt.displayTabla();
			agregarLinkDocumentosAportados();
            document.forms[0].codProcedimientoRoles.value='';
            document.forms[0].opcionAltaDesdeConsulta.value="SI";
            desactivarNavegacion();
            ocultarLista();
            ocultarCalendario();
            borrarPaginaBuscada();
            crearListasRelacionesTxt();
            clean();
            consultando=false;
            
            //Borramos el combo de expedientes relacionados
            limpiarComboExpedientesRelacionados();
            deshabilitarDatosSGA();
            habilitarDivMsgAsuntoBaja(false);
            document.getElementById("divAsuntoBaja").innerHTML = "";

            <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                document.forms[0].opcion.value="init_alta_entrada";
                 $("[name='cmdDigitalizarDesdeConsulta']").hide();
                 $("[name='cmdDigitalizarDesdeAlta']").hide();
                 $("[name='cmdFinDigitalizar']").hide();
            <% } else {%>
                document.forms[0].opcion.value="init_alta_salida";
            <% } %>
            document.forms[0].target="oculto";
            document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
            // Datos SIR
            if($('[name="codigoUnidadDestinoSIRHidden"]') != undefined){
                $('[name="codigoUnidadDestinoSIRHidden"]').val($('[name="codigoUnidadDestinoSIR"]').val());
                $('[name="listaCodTerceroSIRHidden"]').val($('[name="listaCodTercero"]').val());
                $('[name="listaVersionTerceroSIRHidden"]').val($('[name="listaVersionTercero"]').val());
                $('[name="listaCodDomicilioSIRHidden"]').val($('[name="listaCodDomicilio"]').val());
            }
            document.forms[0].submit();
        }
    }else if(NUM_OFICINAS_REGISTRO_USUARIO == 0){
        jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_sinOficinaRegistro"))%>');
    }else if(NUM_OFICINAS_REGISTRO_USUARIO > 1){
        jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_masDeUnaOficinaRegistro"))%>');
    }//
}//pulsarAltaDesdeConsulta
<% }%>

function recuperaDatos(datos,datosTercero,listaTemas,lista_CODtiposDocumentos, lista_DESCtiposDocumentos, lista_ACTtiposDocumentos,
        lista_CODtiposDocumentosAlta, lista_DESCtiposDocumentosAlta, lista_ACTtiposDocumentosAlta,
        lista_CODactuaciones, lista_DESCactuaciones, lista_CODtiposTransportes, lista_DESCtiposTransportes, lista_ACTtiposTransportes, lista_CODtiposRemitentes,
        lista_DESCtiposRemitentes, lista_ACTtiposRemitentes, lista_CODtemas, lista_DESCtemas,lista_CODidInteresado, lista_DESCidInteresado,
        lista_CODdpto, lista_DESCdpto, fecha, listaDocs,listaAnteriores, listFormularios, listFormulariosOriginal,listaInteresados, listaRelaciones,
        lista_uni_asuntos, lista_cod_asuntos, lista_desc_asuntos, lista_cod_roles, lista_desc_roles, lista_defecto_roles,existenAnexosForms,desdeIniciarDuplicar, bloquearDestino,
        listaExpedientesRelacionados, contIdNumsExpedientesRelacionados,registroTelematico,esRegTelemModificable, procedimientoDigitalizacion, finDigitalizacion,bloquearProcedimiento){                 
            pleaseWait('off');
            if (top.menu.modificando == 'N') {                 
                desactivarComboAsuntos();
                ocultarBotonesEdicionInteresados();
            } else {                                
                //comboAsuntos.activate();
                mostrarBotonesEdicionInteresados();
            }
            if(existenAnexosForms=='si'){
                existenAnexosFormularios='si';
            }

            cod_roles = lista_cod_roles;
            desc_roles = lista_desc_roles;
            defecto_roles = lista_defecto_roles;
            comboRol.addItems(cod_roles, desc_roles);

            terceros=listaInteresados;
            crearListas();
            relaciones=listaRelaciones;
            crearListasRelacionesTxt();
            // Si hay mas de un interesado activar navegacion
            if (terceros.length > 1) mostrarBotonesNavegacionInteresados();
            else ocultarBotonesNavegacionInteresados();
            vectorConsulta = new Array(document.getElementsByName("consultaExpediente")[0]);
            habilitarImagen(vectorConsulta, true);
            fechaHoy = fecha;
            if (unescape(datos[60])=='null') document.forms[0].observaciones.value = '';
            else document.forms[0].observaciones.value =unescape(datos[60]);
            //Borrar interesado.
            document.forms[0].cbTipoDoc.value='';
            document.forms[0].descTipoDoc.value='';
            cod_tiposIdInteresado = lista_CODidInteresado;
            desc_tiposIdInteresado = lista_DESCidInteresado;
            cod_departamentos = lista_CODdpto;
            desc_departamentos = lista_DESCdpto;
            if(datos[51] == "si") {
                document.getElementById("capaDiligencia").style.visibility = 'visible';
                document.getElementById('botonDiligencias').removeAttribute("disabled");
                habilitarImagenBoton("capaDiligencia",true);
            } else document.getElementById("capaDiligencia").style.visibility = 'hidden';

            document.forms[0].fechaAnotacion.value= datos[1] +"/"+datos[2]+"/"+datos[3];
            document.forms[0].fechaDocumento.value= datos[4] +"/"+datos[5]+"/"+datos[6];
                document.forms[0].fechaDoc.value= datos[67];
            <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>
                document.forms[0].horaMinDocumento.value= datos[7] + ":"+ datos[8];
            <% }%>
            document.forms[0].asunto.value = unescape(datos[9]);
            document.forms[0].cod_tipoTransporte.value = datos[10];
            document.forms[0].txtCodigoDocumento.value = datos[11];
    var descEntrada = datos[12];
    var idxEntrada = desc_tipoEntrada.indexOf(descEntrada);
    if (idxEntrada != -1) {
        document.forms[0].cbTipoEntrada.value = cod_tipoEntrada[idxEntrada];
    } else {
        document.forms[0].cbTipoEntrada.value = descEntrada;
    }
            actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
            mostrarDestino();
            document.forms[0].cod_tipoRemitente.value = datos[13];
            document.forms[0].txtNumTransp.value = datos[14];
            document.forms[0].cod_unidadeRexistroOrixe.value = datos[18];
            document.forms[0].cod_uniRegDestino.value = datos[16];
            document.forms[0].cod_orgDestino.value = datos[19];
            document.forms[0].cod_orgOrigen.value = datos[21];
            document.forms[0].txtNomeDocumento.value = datos[23];
            document.forms[0].txtNomeTipoRemitente.value = datos[24];
            document.forms[0].desc_tipoTransporte.value = datos[25];
            document.forms[0].cod_actuacion.value = datos[26];
            document.forms[0].txtNomeActuacion.value = datos[27];
            document.forms[0].desc_orgDestino.value = datos[28];
            document.forms[0].desc_uniRegDestino.value = datos[31];
            document.forms[0].desc_orgOrigen.value = datos[32];
            document.forms[0].desc_unidadeRexistroOrixe.value = datos[35];
            document.forms[0].tipoRegistroOrigen.value = datos[36];
            <% if (("S".equals(tipoAnotacion)) || ("Relacion_S".equals(tipoAnotacion))) {%>
                document.forms[0].tipoRegistro.value= "S";
            <% }%>
            document.forms[0].txtExp1Orixe.value = datos[38];           
            document.forms[0].txtExp2Orixe.value = datos[37];
            document.forms[0].cod_uniRegDestinoORD.value = datos[42]; // cod UOR
            document.forms[0].cod_uor.value = datos[43]; // cod visible
            document.forms[0].ano.value=datos[44];
            document.forms[0].numero.value=datos[45];
            document.forms[0].horaMinAnotacion.value=datos[46]+":"+datos[47]+":"+datos[70];
            var igual=datos[48];
            document.forms[0].desc_uniRegDestinoORD.value = datos[50];
            textoDil = datos[52];
            document.forms[0].autoridad.value = unescape(datos[61]);
            
             <% if(datosSga){%>
                document.forms[0].codigoSga.value=datos[71];
                document.forms[0].expedienteSga.value=datos[72];
            <%}%> 
                
            document.forms[0].cmdCancelarBuscada.disabled=false;
            document.getElementsByName("cmdCuneus")[0].disabled=false;
            if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=false;
            if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=false;
            if (document.forms[0].cmdContestar) document.forms[0].cmdContestar.disabled=false; 
            if (document.forms[0].cmdResponder) document.forms[0].cmdResponder.disabled=false;
            if (document.forms[0].cmdRelacionar) document.forms[0].cmdRelacionar.disabled=false;

            if(igual == "cerrado") { 
                if (document.forms[0].cmdModificar){ document.forms[0].cmdModificar.disabled=true; document.getElementById('cmdModificar').style.color = '#CCCCCC'};
                if (document.forms[0].cmdAnular){ document.forms[0].cmdAnular.disabled=true; document.getElementById('cmdAnular').style.color = '#CCCCCC'};
                //if (document.forms[0].cmdDigitalizarDesdeConsulta){ document.forms[0].cmdDigitalizarDesdeConsulta.disabled=true; document.getElementById('cmdDigitalizarDesdeConsulta').style.color = '#CCCCCC'};
            } else {
                if (document.forms[0].cmdModificar){ document.forms[0].cmdModificar.disabled=false; document.getElementById('cmdModificar').style.color = '#ffffff'};
                if (document.forms[0].cmdAnular){ document.forms[0].cmdAnular.disabled=false; document.getElementById('cmdAnular').style.color = '#ffffff'};    
                //if (document.forms[0].cmdDigitalizarDesdeConsulta){ document.forms[0].cmdDigitalizarDesdeConsulta.disabled=false; document.getElementById('cmdDigitalizarDesdeConsulta').style.color = '#ffffff'};
            }
            lista=listaTemas;
            listaDoc = listaDocs;
            tabDoc.lineas=listaDocs;
            refrescaDoc();
            listaAnt=listaAnteriores;	
            tabAnt.lineas=listaAnteriores;	
            tabAnt.displayTabla();
			agregarLinkDocumentosAportados();
            cargarFormularios(listFormularios,listFormulariosOriginal);
            ActivaBotonVerAnexosFormularios();
            cod_tiposDocumentos = lista_CODtiposDocumentos;
            desc_tiposDocumentos = lista_DESCtiposDocumentos;
            act_tiposDocumentos = lista_ACTtiposDocumentos;
            cod_tiposDocumentosAlta = lista_CODtiposDocumentosAlta;	
            desc_tiposDocumentosAlta = lista_DESCtiposDocumentosAlta;	
            act_tiposDocumentosAlta = lista_ACTtiposDocumentosAlta;
            cod_tiposTransportes = lista_CODtiposTransportes;
            desc_tiposTransportes= lista_DESCtiposTransportes;
            act_tiposTransportes = lista_ACTtiposTransportes;
            cod_tiposRemitentes= lista_CODtiposRemitentes;
            desc_tiposRemitentes= lista_DESCtiposRemitentes;
            act_tiposRemitentes = lista_ACTtiposRemitentes;
            cod_temas=lista_CODtemas;
            desc_temas=lista_DESCtemas;

            document.forms[0].codTerc.value = datos[39];
            document.forms[0].codDomTerc.value = datos[40];
            document.forms[0].numModifTerc.value = datos[41];
            terceroCargado = datos[39];
            mostrarDatosTercero();

            if(modificarGrabar != 1) {
                document.getElementById("capaBotones3").style.display = '';
                document.getElementById("capaCunho").style.visibility = 'visible';
                modificarGrabar = 0;
            }
             if (estadolabelTipoDoc == 0){	
                compruebaTipoDoc(document.forms[0].txtCodigoDocumento.value, document.forms[0].txtNomeDocumento.value);	
            }else{	
                 cargarTipoDocAux("conPreSel");
                 OcultaTipoDocLabel();	
            }
            if(mod == 1) {
                habilitarBuscar();
                document.getElementById("capaBotones3").style.display = '';
                document.getElementById("capaCunho").style.visibility = 'visible';
                document.getElementById("capaBotones4").style.display = 'none';
            } else {
                deshabilitarBuscar();
            }
            tp1.setSelectedIndex(0);
            estadoAnot = datos[53];
            diligenciaAnulacion ='';

            if (estadoAnot == 9 ) {
                diligenciaAnulacion = unescape(datos[54]);
                domlay('capaEstado',1,0,0,'');
                document.getElementById('botonDiligenciasAnulacion').disabled=false;
                if (igual != "cerrado") {
                    domlay('capaDesAnular', 1,0,0,'');
                    if (document.getElementById('botonRecuperarAnulada')) document.getElementById('botonRecuperarAnulada').disabled=false;
                } else {
                    domlay('capaDesAnular', 0,0,0,'');
                    if (document.getElementById('botonRecuperarAnulada')) document.getElementById('botonRecuperarAnulada').disabled=true;
                }
                if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true; document.getElementById('cmdModificar').style.color = '#CCCCCC';
                if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true; document.getElementById('cmdAnular').style.color = '#CCCCCC';
                //if (document.forms[0].cmdDigitalizarDesdeConsulta) document.forms[0].cmdDigitalizarDesdeConsulta.disabled=true; document.getElementById('cmdDigitalizarDesdeConsulta').style.color = '#CCCCCC';
            } else if (estadoAnot == 1) { // No se pueden modificar.         
                if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true; document.getElementById('cmdModificar').style.color = '#CCCCCC';
                if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true; document.getElementById('cmdAnular').style.color = '#CCCCCC';
                //if (document.forms[0].cmdDigitalizarDesdeConsulta) document.forms[0].cmdDigitalizarDesdeConsulta.disabled=true; document.getElementById('cmdDigitalizarDesdeConsulta').style.color = '#CCCCCC';
            } else {
                domlay('capaEstado',0,0,0,'');
                domlay('capaDesAnular',0,0,0,'');
            }

            // CONTESTADAS
            <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
                if ( (datos[55]!='') && (datos[56]!='') ) mostrarCapaAnotacionContestada(true, datos[55], datos[56], '<%= descriptor.getDescripcion("gEtiqAnotContest") %>');
                else mostrarCapaAnotacionContestada(false,' ',' ','');
            <% }%>

            //document.forms[0].txtExp1.value=datos[57];    
            document.forms[0].cod_procedimiento.value=datos[58];
            document.forms[0].codProcedimientoRoles.value=datos[66];
            document.forms[0].desc_procedimiento.value=datos[59];
            document.forms[0].mun_procedimiento.value=datos[64];

            if(datos[68]=="true"){ /**  Si el asunto de la anotación está dado de baja **/
                habilitarDivMsgAsuntoBaja(true);
                        var mensaje = '<%=descriptor.getDescripcion("etiqErrorAsuntoRegBaja1")%>' + " " + datos[65] + " " + '<%=descriptor.getDescripcion("etiqErrorAsuntoRegBaja2")%>' + " " + datos[69];		
                        document.getElementById("divAsuntoBaja").innerHTML= mensaje;
            }

            if(desdeIniciarDuplicar!=null && desdeIniciarDuplicar==true) {
                habilitarDivMsgAsuntoBaja(false);
                document.getElementById("divAsuntoBaja").innerHTML='';
            }                     
            cargarComboExpedientesRelacionados(listaExpedientesRelacionados, contIdNumsExpedientesRelacionados);                        
            if (contIdNumsExpedientesRelacionados!=null && contIdNumsExpedientesRelacionados.length > 0)
                comboExpedientesRelacionados.selectItem(1);
            
            if (top.menu.modificando == 'N') { 
                comboExpedientesRelacionados.deactivate();                         
            } else {                                
                comboExpedientesRelacionados.activate();
            }
                                    
            cargarComboAsuntos(lista_uni_asuntos, lista_cod_asuntos, lista_desc_asuntos, datos[65],datos[68]);
            rolPorDefecto = datos[62];
            descRolPorDefecto = datos[63];
            // Para saber cuando cambiamos el procedimiento
            cod_procedimiento_anterior = document.forms[0].cod_procedimiento.value;
            // Ponemos el check de notificar vacio
            desactivaNotificaciones();
            compruebaModificadoRegistro();
            if (top.menu.modificando == 'N') deshabilitarTipoDocYRemitente();
            deshabilitarTipoDocTerceroYDoc(); 
            
            // #270948: Se abre el componente de digitalización si así se indica
            var altaConDigitalizacion = document.getElementById("digitalizarConAltaPrevia").value;
            if(altaConDigitalizacion != null && altaConDigitalizacion != undefined && altaConDigitalizacion != ""){
                document.getElementById("digitalizarConAltaPrevia").value = "";
                pulsarDigitalizarDocs('alta');
            }
            
             if(registroTelematico){
               mostrarMensajeRegistroTelematico('<%=descriptor.getDescripcion("msgRegTelematico")%>');
               
                var textArea = document.getElementsByName("asunto");
                var textoAsunto = null;
                if (textArea != null) {
                    textoAsunto = textArea[0].value;
                }
                
                var textoPermiteModificacion = "SIR (REGISTRO - ERREGISTRO)";
               
                if (document.forms[0].cmdModificar && !esRegTelemModificable && textoAsunto != null && textoAsunto.toUpperCase().indexOf(textoPermiteModificacion) == -1
                        // Caso de que sea Salida enviada al SIR no se debe modificar
                        ||(("S"=="<%=tipoAnotacion%>" || "Relacion_S"=="<%=tipoAnotacion%>") && document.forms[0].cbTipoEntrada.value=="1")
                        ){
                    document.forms[0].cmdModificar.disabled=true;
                    document.getElementById('cmdModificar').style.color = '#CCCCCC';
                }
                
                 if (document.forms[0].cmdAnular && textoAsunto != null && textoAsunto.toUpperCase().indexOf(textoPermiteModificacion) == -1) {
                    document.forms[0].cmdAnular.disabled=true; 
                    document.getElementById('cmdAnular').style.color = '#CCCCCC';
                }
                	
                if (document.forms[0].cmdDuplicar){
                    document.forms[0].cmdDuplicar.disabled=true;
                    document.getElementById('cmdDuplicar').style.color = '#CCCCCC';                    
                }  
	
                if (document.forms[0].cmdContestar){
                    document.forms[0].cmdContestar.disabled=true;
                    document.getElementById('cmdContestar').style.color = '#CCCCCC'; 
                    
                }  
            }
             if (top.menu.modificando != 'N'){
				 bloquearUnidadDestino(bloquearDestino);
				 bloquearUnidadProcedimiento(bloquearProcedimiento);
			 }
            // Mostrar u ocultar el boton de digitalizar documentos de registro
            document.forms[0].finDigitalizacion.value = finDigitalizacion;
            if (mostrarDigitalizar) {
                if(finDigitalizacion == true){ // Si la digitalizacion ha finalizado no se puede digitalizar desde consulta
                        deshabilitarGeneral([document.forms[0].cmdDigitalizarDesdeConsulta]);
                        $("[name='cmdDigitalizarDesdeConsulta']").css("color", "#ccc");
                } else {
					 document.getElementById("FinDigitalizacionEtiqueta").innerHTML= "Pend. fin digit.";
                    habilitarGeneral([document.forms[0].cmdDigitalizarDesdeConsulta]);
                    $("[name='cmdDigitalizarDesdeConsulta']").css("color", "#fff");
                }
                if (top.menu.modificando == 'N') {
                    gestionarVisibilidadDigitalizacion('disabled',true);
                } else {
                    gestionarVisibilidadDigitalizacion('disabled',finDigitalizacion);
                }
                
                if  (procedimientoDigitalizacion && uorPermiteDigitalizacion) {
                    gestionarVisibilidadDigitalizacion('mostrar',finDigitalizacion);
                } else {
                    gestionarVisibilidadDigitalizacion('ocultar', finDigitalizacion);
                }
            }
}


function recuperaDatos1(datos,lista_CODtiposDocumentos, lista_DESCtiposDocumentos,lista_CODactuaciones, lista_DESCactuaciones,
        lista_CODtiposTransportes, lista_DESCtiposTransportes, lista_CODtiposRemitentes, lista_DESCtiposRemitentes,
        lista_CODtemas, lista_DESCtemas,lista_CODidInteresado, lista_DESCidInteresado,lista_CODdpto, lista_DESCdpto) {
    document.forms[0].fechaAnotacion.value = datos[1]+"/"+datos[2]+"/"+datos[3];
    document.forms[0].horaMinAnotacion.value=datos[4]+":"+datos[5];
    document.forms[0].fechaDocumento.value = datos[1]+"/"+datos[2]+"/"+datos[3];
    <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) { %>
        document.forms[0].horaMinDocumento.value=datos[4]+":"+datos[5]+":"+datos[6]; 
    <% } %>

    cod_tiposDocumentos = lista_CODtiposDocumentos;
    desc_tiposDocumentos = lista_DESCtiposDocumentos;
    cod_tiposTransportes = lista_CODtiposTransportes;
    desc_tiposTransportes= lista_DESCtiposTransportes;
    cod_tiposRemitentes= lista_CODtiposRemitentes;
    desc_tiposRemitentes= lista_DESCtiposRemitentes;
    cod_tiposIdInteresado = lista_CODidInteresado;
    desc_tiposIdInteresado = lista_DESCidInteresado;
    cod_departamentos = lista_CODdpto;
    desc_departamentos = lista_DESCdpto;
    cod_temas=lista_CODtemas;
    desc_temas=lista_DESCtemas;

    <% if ("reservas".equals(respOpcion)) { %>
        document.forms[0].cmdCancelarBuscada.disabled=false;
        document.getElementsByName("cmdCuneus")[0].disabled=false;
        if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=false; document.getElementById('cmdModificar').style.color = '#ffffff';
        if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true; document.getElementById('cmdAnular').style.color = '#CCCCCC';
        if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=true;
        if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=true;
        if (document.forms[0].cmdContestar) document.forms[0].cmdContestar.disabled=true; 
        if (document.forms[0].cmdResponder) document.forms[0].cmdResponder.disabled=true;
        if (document.forms[0].cmdRelacionar) document.forms[0].cmdRelacionar.disabled=true;
    <% } else { %>
        if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=false; document.getElementById('cmdModificar').style.color = '#ffffff';
        if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=false;
        if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=false;
    <% } %>
    desactivarOrigenYExpediente();
    if(mod == 1) {
        habilitarBuscar();
        mostrarCapasBotones('capaBotones3');
        document.getElementById("capaCunho").style.visibility = 'visible';
    }
    tp1.setSelectedIndex(0);
}

<% if (permisoMantenimiento) {%>
function pulsarModificar(idPestana) {
	   cargarTipoDocAux("Alta");
	
    if (rolPorDefecto==''){
        jsp_alerta("A", "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjErrorModRegistro"))%>");
    } else {
        changeNotifs = false;
        mod=0;
        activarFormulario();        
        //NO SE PUEDE MODIFICAR LA FECHA DE GRABACIÓN
         var vectorFecDoc = [document.forms[0].fechaDocumento];
         deshabilitarGeneral(vectorFecDoc);
         

        // Mantenemos desactivadas las notificaciones.
        var vectorEnviarCorreo = new Array(document.forms[0].enviarCorreo.disabled);
        deshabilitarGeneral(vectorEnviarCorreo);

        var vectorNotificar = new Array(document.forms[0].cmdNotificar);
        deshabilitarGeneral(vectorNotificar);
        
//Mantenemos desactivados los campos del bloque sga	
        deshabilitarDatosSGA();
        
        domlay('diligencia',0,500,70,textoDil);
        mostrarCapasBotones('capaBotones4');

        modificarGrabar=1;
        modificando('S');
        finDigitalizar = null;
        mostrarDestino();
        
        desactivarOrigenYExpediente();

        desactivarInteresado();

        ocultarInfSoloConsulta();

        desactivarCampoDocumento();

		bloquearFechaHoraAnotacion();

        <% if (!"reservas".equals(respOpcion)) {%>
            desactivarNavegacion();
        <% }%>
            
        //Marcamos en el caso de que sea obligatorio el campo de asunto
        var obligatorioAsunto = <%=mantARForm.getObligatorioAsuntoCodificado()%>;
        if(obligatorioAsunto == true){            
            cambiarEstadoComboAsuntoCodificado(true);
        }//if(obligatorioAsunto == true) 
		estadolabelTipoDoc = 0;

        var params = "?pulsarModificar=1";
        if (idPestana) {
            params += "&irAPestana=" + idPestana;
        }
        
        document.forms[0].modificar.value = "1";
        document.forms[0].opcion.value="buscar";
        document.forms[0].target="oculto";
        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>" + params;
        // Datos SIR
        if($('[name="codigoUnidadDestinoSIRHidden"]') != undefined){
            $('[name="codigoUnidadDestinoSIRHidden"]').val($('[name="codigoUnidadDestinoSIR"]').val());
            $('[name="listaCodTerceroSIRHidden"]').val($('[name="listaCodTercero"]').val());
            $('[name="listaVersionTerceroSIRHidden"]').val($('[name="listaVersionTercero"]').val());
            $('[name="listaCodDomicilioSIRHidden"]').val($('[name="listaCodDomicilio"]').val());
        }
        document.forms[0].submit();
    }
   
}

function registroCerradoAlModificar(){
    pleaseWait('off');
    jsp_alerta("A", '<%=descriptor.getDescripcion("msjRegCAnot")%>');
    mostrarCapasBotones('capaBotones3');
    <% if (!("reservas".equals(respOpcion))) {%>
        actualizaAnotacionNavegacion(anotacionActual);
    <% } else {%>
        mostrarCapasBotones('capaBotones3');
        if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=false; document.getElementById('cmdModificar').style.color = '#ffffff';
        if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=false; document.getElementById('cmdAnular').style.color = '#ffffff';
        if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=false;
        if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=false;
        if (document.forms[0].cmdContestar) document.forms[0].cmdContestar.disabled=false; 
        if (document.forms[0].cmdResponder) document.forms[0].cmdResponder.disabled=false;
        if (document.forms[0].cmdRelacionar) document.forms[0].cmdRelacionar.disabled=false;
        document.forms[0].cmdCancelarBuscada.disabled=false;
        document.getElementsByName("cmdCuneus")[0].disabled=false;
        habilitarBuscar();
        mod = 1;
        modificando('N');
        borrarPaginaBuscada();
        document.forms[0].opcion.value="buscar";
        document.forms[0].target="oculto";
        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
        document.forms[0].submit();
    <% }%>
}

function confirmarModificacion() {
    activarFormulario();
    deshabilitarBuscar();
    desactivarOrigenYExpediente();
    desactivarInteresado();
        document.forms[0].opcion.value="grabarModificacionesConfirmada";
        // TEMAS
        var l = new Array();
        for (i=0; i < lista.length; i++) l[i]=lista[i][0]+'§¥'; // Solo códigos
        document.forms[0].listaTemas.value=l;
        document.forms[0].target="oculto";
        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
        document.forms[0].submit();
}

function confirmarModificacion2() { 
    activarFormulario();
    deshabilitarBuscar();
    desactivarOrigenYExpediente();
    desactivarInteresado();

        document.forms[0].opcion.value="grabarModificacionesConfirmada2";
        // TEMAS
        var l = new Array();
        for (i=0; i < lista.length; i++) l[i]=lista[i][0]+'§¥'; // Solo códigos
        document.forms[0].listaTemas.value=l;
        document.forms[0].target="oculto";
        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
        document.forms[0].submit();
mostrarCapasBotones('capaBotones4');
}

function confirmarModificacionFechaAnteriorPosterior(mnsj) {    
    pleaseWait('off');
    activarFormulario();
    deshabilitarBuscar();
    desactivarOrigenYExpediente();
    desactivarInteresado();
    if(jsp_alerta("A",mnsj)){
        document.forms[0].opcion.value="grabarModificacionesConfirmada";
        // TEMAS
        var l = new Array();
        for (i=0; i < lista.length; i++) l[i]=lista[i][0]+'§¥'; // Solo códigos
        document.forms[0].listaTemas.value=l;
        document.forms[0].target="oculto";
        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
        document.forms[0].submit();
    } else {
        mostrarCapasBotones('capaBotones4');
    }
}
<% }%>

<% if (permisoMantenimiento) {%>
function pulsarAnular() {
    var vector = new Array(document.forms[0].ano, document.forms[0].numero);
    habilitarGeneral(vector);
    activarInteresado();
    document.forms[0].opcion.value="iniciar_anular";
    document.forms[0].target="oculto";
    document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
    document.forms[0].submit();
}

function iniciarAnular() {    
    modificando('S');
    mostrarDestino();
    window.location = "<html:rewrite page='/jsp/registro/entrada/anulacionRE.jsp'/>";
}

function pulsarRecuperarAnulada() {
    if(jsp_alerta("",'<%=descriptor.getDescripcion("pregRecuperarAnulada")%>')){
        activarFormulario();
        document.forms[0].opcion.value="desAnular";
        document.forms[0].target="oculto";
        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
        document.forms[0].submit();
    }
}

function anotacionRecuperada(mnsj) {
    jsp_alerta("A", mnsj);
    habilitarBuscar();
    document.forms[0].tipoConsulta[1].checked=true;
    document.forms[0].opcion.value="recargar_consulta";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
    document.forms[0].submit();
}
<% }%>

<% if (permisoMantenimiento) {%>
    function pulsarRegistrarModificar() { 
        if(NUM_OFICINAS_REGISTRO_USUARIO == 1){
            pleaseWait('on');
            var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);
            if(t == null) {
                pleaseWait('off');
                alert('Problema tecnico al registrar');
                return;
            } else if(t == true) {
                pleaseWait('off');
                jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
                return;
            }   
            
             documentosModificados=false;


            <%if("reservas".equals(respOpcion)) session.setAttribute("grabarDesdeReserva","1");%>
            if (validarObligatoriosAqui() && validaRetramitarDocCambioProcedimiento()) {
                if ((comprobarFecha(document.forms[0].fechaAnotacion,mensajeFechaNoValida)) && (comprobarFecha(document.forms[0].fechaDocumento,mensajeFechaNoValida))) {
                    if (comparaFecha()) {
                        var l = new Array();
                        for (i=0; i < lista.length; i++) l[i]=lista[i][0]+'§¥'; // Solo códigos
                        document.forms[0].listaTemas.value=l;
                        document.forms[0].opcion.value="grabarModificaciones";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
                        // Hay que habilitar los campos tipo doc y tipo remitente
                        // aunque esten vacíos o no se copian al form.
                        habilitarTipoDocYRemitente();
                        // Datos SIR
                        if($('[name="codigoUnidadDestinoSIRHidden"]') != undefined){
                            $('[name="codigoUnidadDestinoSIRHidden"]').val($('[name="codigoUnidadDestinoSIR"]').val());
                            $('[name="listaCodTerceroSIRHidden"]').val($('[name="listaCodTercero"]').val());
                            $('[name="listaVersionTerceroSIRHidden"]').val($('[name="listaVersionTercero"]').val());
                            $('[name="listaCodDomicilioSIRHidden"]').val($('[name="listaCodDomicilio"]').val());
                        }
                        document.forms[0].submit();
                        desactivarFormulario();
                        //Marcamos en el caso de que sea obligatorio el campo de asunto
                        var obligatorioAsunto = <%=mantARForm.getObligatorioAsuntoCodificado()%>;
                        if(obligatorioAsunto == true){             
                                cambiarEstadoComboAsuntoCodificado(false);
                        }//if(obligatorioAsunto == true)
                        modificando('N');
                    } else {
                        pleaseWait('off');
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoGrabarAnot")%>');
                    }
                }else {
                    pleaseWait('off');
                }
            }
        }else if(NUM_OFICINAS_REGISTRO_USUARIO == 0){
            jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_sinOficinaRegistro"))%>');
        }else if(NUM_OFICINAS_REGISTRO_USUARIO > 1){
            jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_masDeUnaOficinaRegistro"))%>');
        }
    }//pulsarRegistrarModificar
    
    function pulsarRegistrarModificar2() {
        if(NUM_OFICINAS_REGISTRO_USUARIO == 1){
            var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);
            if(t == null){
                pleaseWait('off');
                alert('Problema tecnico al registrar');
                return;
            }else if (t == true){
                pleaseWait('off');
                jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
                return;
            }
            if(validarObligatoriosAqui()){
                if ((comprobarFecha(document.forms[0].fechaAnotacion,mensajeFechaNoValida)) && 
                    (comprobarFecha(document.forms[0].fechaDocumento,mensajeFechaNoValida))){
                    var l = new Array();
                    for (i=0; i < lista.length; i++) l[i]=lista[i][0]+'§¥'; // Solo códigos
                    <c:choose>
                        <c:when test="${requestScope.mostrarCuneus == 'true'}">
                            pulsarImprimirCuneus('si');
                        </c:when>
                        <c:otherwise>                        
                            // Si es obligatorio el asunto codificado, se desactiva
                            var obligatorioAsunto = <%=mantARForm.getObligatorioAsuntoCodificado()%>;
                            if(obligatorioAsunto == true){
                                    cambiarEstadoComboAsuntoCodificado(false);
                            }//if(obligatorioAsunto == true)
                            duplicar();
                        </c:otherwise>
                    </c:choose>
                }
            }else{
                pleaseWait('off');
            }//if(validarObligatoriosAqui())
        }else if(NUM_OFICINAS_REGISTRO_USUARIO == 0){
            jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_sinOficinaRegistro"))%>');
        }else if(NUM_OFICINAS_REGISTRO_USUARIO > 1){
            jsp_alerta("A", '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msg_masDeUnaOficinaRegistro"))%>');
        }//if
    }//pulsarRegistrarModificar2
	
function duplicar(){
    document.forms[0].opcion.value="grabarDuplicarModificaciones";
    document.forms[0].target="oculto";
    document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
    
    // Hay que habilitar los campos tipo doc y tipo remitente
    // aunque esten vacíos o no se copian al form.
    habilitarTipoDocYRemitente();
    document.forms[0].submit();
    desactivarFormulario();
    modificando('S');
            var vectorFec = [document.forms[0].fechaAnotacion];
            deshabilitarGeneral(vectorFec);
            document.forms[0].fecPresRes.value = document.forms[0].fechaAnotacion.value;
            deshabilitarImagenCal("calFechaAnotacion",true);
}

function pulsarCancelarModificar() { 
    document.getElementById('checkBoxNotificar').className = 'etiquetaDeshabilitada';
    document.forms[0].enviarCorreo.disabled = true;
    ocultarLista();
    ocultarCalendario();
    desactivarFormulario();
    modificando('N');
    mostrarDestino();
    comboRol.addItems([],[]);
    descRolPorDefecto = '';
    codRolPorDefecto = '';
    mod = 1;
    estadolabelTipoDoc = 1;
    documentosModificados=false;
    inicializar();

    <% if ("reservas".equals(respOpcion)) { %>
        borrarPaginaBuscada();
        document.forms[0].opcion.value="buscar";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
        document.forms[0].submit();
    <% } else {  /* Modo consulta*/ %>
        mostrarCapasBotones('capaBotones3');
        actualizaAnotacionNavegacion(anotacionActual);
    <% } %>
	
	
    //Marcamos en el caso de que sea obligatorio el campo de asunto
	var obligatorioAsunto = <%=mantARForm.getObligatorioAsuntoCodificado()%>;
	if(obligatorioAsunto == true){ 		
		cambiarEstadoComboAsuntoCodificado(false);
	}
	pleaseWait("on");
}

<% }%>

<% if (permisoMantenimiento) {%>

function activarTipoDoc(){	
    document.forms[0].txtCodigoDocumento.disabled =false;	
    document.forms[0].txtNomeDocumento.disabled=false;	
}

function pulsarDuplicar() { 
    mostrarMensajeRegistroTelematico('');
    habilitarBuscar(); 
    //eliminamos los datos de expedientes asociados
     activarInteresado();	
    activarTipoDoc();	
    cargarTipoDocAux("Alta");
    limpiarComboExpedientesRelacionados();
    document.getElementById("codListaExpedientesRelacionados").value="";
    document.getElementById("descListaExpedientesRelacionados").value="";
    
    document.forms[0].opcion.value="iniciar_duplicar";
    document.forms[0].modificar.value="1";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
    document.forms[0].submit();
    estadolabelTipoDoc = 0;

}

function pulsarRegistrarDuplicar() {
    pleaseWait('on');
    var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);
    if(t == null) {
        alert('Problema tecnico al registrar');
        pleaseWait('off');
        return;
    } else if(t == true) {
        pleaseWait('off');
        jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
        return;
    }
    ocultarLista();
    ocultarCalendario();
    documentosModificados=false;
    if (validarObligatoriosAqui()) {
        if ((comprobarFecha(document.forms[0].fechaAnotacion,mensajeFechaNoValida)) && (comprobarFecha(document.forms[0].fechaDocumento,mensajeFechaNoValida))) {
            if (comparaFecha()) {
                var l = new Array();
                for (i=0; i < lista.length; i++) l[i]=lista[i][0]+'§¥'; // Solo códigos
                modificando('N');
                mostrarDestino();
                document.forms[0].listaTemas.value=l;
                <% if (contestacion) {%>
                    with (document.forms[0]) {
                        ejercicioAnotacionContestada.value=ejercicioBuscada;
                        numeroAnotacionContestada.value=numeroBuscada;
                    }
                <% }%>
                txtListaUors=document.forms[0].cod_uniRegDestinoORD.value;
                for(i=0; i<listaUorsCorreo.length; i++) txtListaUors =txtListaUors+','+ listaUorsCorreo[i];
                document.forms[0].txtListaUorsCorreo.value = txtListaUors;
                ocultarCapaDatosBusqueda();

                cambiarEstadoComboAsuntoCodificado(false);
                document.forms[0].opcion.value="duplicar";
                document.forms[0].target="oculto";
                document.forms[0].pendienteBuzon.value="no";
                document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                document.forms[0].submit();
                desactivarFormulario();
            }else{
                pleaseWait('off');
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoGrabarAnot")%>');
            }
        }
        else pleaseWait('off');
    }
}

function duplicarDenegado() {
    ocultarLista();
    ocultarCalendario();
    ocultarCapaAnulacion();
    ocultarCapaDiligencia();
    modificando('N');
    mostrarDestino();
    jsp_alerta("A", '<%=descriptor.getDescripcion("msjDuplicAnotDeneg")%>');
    <% if ("Relacion_E".equals(tipoAnotacion) || "E".equals(tipoAnotacion)) { %>
        document.forms[0].opcion.value="Relacion_E";
    <% } else {%>
        document.forms[0].opcion.value="Relacion_S";
    <% }%>
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
    document.forms[0].submit();
}



<% if (permisoMantenimientoSalida) {%>
<% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>

function pulsarContestar() {
    habilitarBuscar();
    mostrarDestino();
    activarInteresado();
     cargarTipoDocAux("Alta");
    document.forms[0].opcion.value="contestar";
    document.forms[0].modificar.value="1";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
    document.forms[0].submit();
}

function contestarDenegado() {
    jsp_alerta("A", '<%=descriptor.getDescripcion("msjContestAnotDeneg")%>');
    desactivarFormulario();
    habilitarBuscar();
    modificando('N');
    mostrarDestino();
      activarTipoDoc();	
    cargarTipoDocAux("Alta");
    mostrarCapasBotones('capaBotones3');	
    estadolabelTipoDoc = 0;
}
<% } %>
<% } %>



<% if (contestacion || altaDesdeTramitar) {%>
function cargarListas() {

    var fecha = '<bean:write name="MantAnotacionRegistroForm" property="fechaHoraHoy"/>';
    var fechaServidor = fecha.substring(0,10);
    var horaServidor = fecha.substring(11,19);
    document.forms[0].fechaAnotacion.value=fechaServidor;
    document.forms[0].fechaDocumento.value=fechaServidor;
    document.forms[0].horaMinAnotacion.value = horaServidor;

    var listaInteresados = new Array();
    <%
    BusquedaTercerosForm busquedaTercerosForm = (BusquedaTercerosForm) session.getAttribute("BusquedaTercerosForm");
    TramitacionExpedientesForm tramExpForm= (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");
    Vector listaInteresados = (Vector) busquedaTercerosForm.getListaInteresados();
    int lengthInteresados = listaInteresados.size();
    int i = 0;
    %>
    var j=0;
    <% for (i = 0; i < lengthInteresados; i++) { %>
        listaInteresados[j] = ['<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("codigoTercero")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("versionTercero")%>',
                '<%=StringEscapeUtils.escapeJavaScript((String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("titular"))%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("descRol")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("domicilio")%>',
                '<%=StringEscapeUtils.escapeJavaScript((String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("descDomicilio"))%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("porDefecto")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("rol")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("telefono")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("email")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("pais")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("provincia")%>',
                '<%=StringEscapeUtils.escapeJavaScript((String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("municipio"))%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("cp")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("tip")%>',
                '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("doc")%>',];
        j++;
    <% } %>
    terceros=listaInteresados;
    document.forms[0].codTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="codTerc"/>';
    terceroCargado = document.forms[0].codTerc.value;
    document.forms[0].codDomTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="codDomTerc"/>';
    document.forms[0].numModifTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="numModifTerc"/>';
    crearListas();
    cargarSoloListas();
    cod_roles = new Array();
    desc_roles = new Array();
    defecto_roles = new Array();
    var m = 0;
    <%
    Vector listaRoles = mantARForm.getListaRoles();
    for(int t=0;t<listaRoles.size();t++) {
    %>
        cod_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>'];
        desc_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>'];
        defecto_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>'];
        m++;
    <% } %>
    comboRol.addItems(cod_roles, desc_roles);

    mostrarDatosTercero();
    activarFormulario();
    deshabilitarBuscar();
    desactivarOrigenYExpediente();
    desactivarInteresado();
    ocultarInfSoloConsulta();

    var listaTemas = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTemasAsignados">
        listaTemas[cont]= [ '<bean:write name="elemento" property="codigoTema"/>', '<bean:write name="elemento" property="descTema"/>'];
        cont= cont +1;
    </logic:iterate>
    lista=listaTemas;

    var listaDocs = new Array();    
    cont=0;
    
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAsignados">   
	var str='';
	if('<bean:write name="elemento" property="entregado"/>'=='S') str='SI'; 
	else if('<bean:write name="elemento" property="entregado"/>'=='N') str='NO';
	else str=''; 
        if(mostrarDigitalizar){
            listaDocs[cont]= [str, '<bean:write name="elemento" property="nombreDoc"/>', '<bean:write name="elemento" property="tipoDoc"/>', '<bean:write name="elemento" property="fechaDoc"/>','<bean:write name="elemento" property="compulsado"/>','<bean:write name="elemento" property="doc"/>','<bean:write name="elemento" property="descripcionTipoDocumental"/>','<bean:write name="elemento" property="idDocumento"/>'];        
        }else{
            listaDocs[cont]= [str, '<bean:write name="elemento" property="nombreDoc"/>', '<bean:write name="elemento" property="tipoDoc"/>', '<bean:write name="elemento" property="fechaDoc"/>','<bean:write name="elemento" property="cotejado"/>','<bean:write name="elemento" property="doc"/>','<bean:write name="elemento" property="idDocumento"/>'];        
        }
        cont= cont +1;
    </logic:iterate>
    listaDoc = listaDocs;
    tabDoc.lineas=listaDocs;
    refrescaDoc();

      var listaAnteriores=new Array();	
    cont=0;	
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAnteriores">	
        listaAnteriores[cont]=['<bean:write name="elemento" property="tipoDocAnterior"/>',	
            '<bean:write name="elemento" property="nombreDocAnterior"/>',	
            '<bean:write name="elemento" property="organoDocAnterior"/>',	
            '<bean:write name="elemento" property="fechaDocAnterior"/>'];	
        cont=cont+1;	
    </logic:iterate>  	
        listaAnt=listaAnteriores;	
        tabAnt.lineas=listaAnteriores;	
        tabAnt.displayTabla();
		agregarLinkDocumentosAportados();
    
    // Relaciones entre asientos
    var listaRelaciones = new Array();
    cont=0;
    <logic:iterate id="relacion" name="MantAnotacionRegistroForm" property="relaciones">
        listaRelaciones[cont] = ['<bean:write name="relacion" property="tipo"/>', '<bean:write name="relacion" property="ejercicio"/>', '<bean:write name="relacion" property="numero"/>'];
        cont = cont + 1;
    </logic:iterate>
    relaciones = listaRelaciones;

    var codigoAsunto = '<bean:write name="MantAnotacionRegistroForm" property="codAsunto"/>';
    if (codigoAsunto == null || codigoAsunto == 'null') codigoAsunto = '';
    cargarComboAsuntos(uni_asuntos, cod_asuntos, desc_asuntos, codigoAsunto);

    crearListasRelacionesTxt();
    tp1.setSelectedIndex(0); 
    mostrarDestino();
    actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
    document.forms[0].asunto.value = unescape('<bean:write name="MantAnotacionRegistroForm" property="asunto"/>');

    var vectorFecDoc = [document.forms[0].fechaDocumento];
    deshabilitarGeneral(vectorFecDoc);
    <% if (altaDesdeTramitar) {%>
    numExpedienteAux='<%=tramExpForm.getNumeroExpediente()%>';
    <%}%>
}

<% }
} %>
 
function cargarSoloListas() {
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposDocumentos">
        cod_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        act_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
    </logic:iterate>
        
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposDocumentosAlta">	
        cod_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        act_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
    </logic:iterate> 

    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposRemitentes">
        cod_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        act_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
    </logic:iterate>

    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposTransportes">
        cod_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        act_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
    </logic:iterate>

    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTemas">
        cod_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
    </logic:iterate>

    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDepartamentos">
        cod_departamentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_departamentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
    </logic:iterate>

    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
        cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
    </logic:iterate>
    var m = 0;
     <%
    Vector listaRoles = mantARForm.getListaRoles();
    for(int t=0;t<listaRoles.size();t++) {
    %>
        cod_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>'];
        desc_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>'];
        defecto_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>'];
        m++;
    <% } %>
    comboRol.addItems(cod_roles, desc_roles);

    comboRol.selectedIndex=-1;

    // Uso
    inicializarEstadosSIR();


            }



// Función para inicializar el combo de estados SIR
function inicializarEstadosSIR() {
    // Mapa código ? descripción (todas las descripciones vienen de descriptor.getDescripcion)
    const ESTADOS_SIR = {
        '':  '',
        '0': '<%=descriptor.getDescripcion("etiq_sir_pendienteEnvio")%>',
        '1': '<%=descriptor.getDescripcion("etiq_sir_enviado")%>',
        '2': '<%=descriptor.getDescripcion("etiq_sir_enviadoAck")%>',
        '3': '<%=descriptor.getDescripcion("etiq_sir_enviadoError")%>',
        '4': '<%=descriptor.getDescripcion("etiq_sir_devuelto")%>',
        '5': '<%=descriptor.getDescripcion("etiq_sir_aceptado")%>',
        '6': '<%=descriptor.getDescripcion("etiq_sir_reenviado")%>',
        '7': '<%=descriptor.getDescripcion("etiq_sir_reenviadoAck")%>',
        '8': '<%=descriptor.getDescripcion("etiq_sir_reenviadoError")%>',
        '9': '<%=descriptor.getDescripcion("etiq_sir_anulado")%>',
        '10':'<%=descriptor.getDescripcion("etiq_sir_recibido")%>',
        '11':'<%=descriptor.getDescripcion("etiq_sir_rechazado")%>',
        '12':'<%=descriptor.getDescripcion("etiq_sir_rechazadoAck")%>',
        '13':'<%=descriptor.getDescripcion("etiq_sir_rechazadoError")%>',
        '14':'<%=descriptor.getDescripcion("etiq_sir_validado")%>',
        '15':'<%=descriptor.getDescripcion("etiq_sir_reintentarValidacion")%>'
};

// Extraemos arrays de códigos y descripciones
const codigos       = Object.keys(ESTADOS_SIR);
const descripciones = codigos.map(c => ESTADOS_SIR[c]);

// Pinta el combo (suponiendo que comboEstadoSIR está definido y tiene addItems)
if (typeof comboEstadoSIR !== 'undefined' && comboEstadoSIR.addItems) {
    comboEstadoSIR.addItems(codigos, descripciones);
}
}



function inicializarRecarga() { 
    <% if (hayBuscada) {
        if (!contestacion) {%>
            recargaConsulta();
        <% } else {%>            
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
                cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
            </logic:iterate>
        <% }
    }%>
    <% if (contestacion || altaDesdeTramitar) {%>
        modificando('S');
        mostrarCapaDatosBusqueda();
        cargarListas();
        fechaHoy = "<bean:write name="MantAnotacionRegistroForm" property="fechaHoraHoy"/>";
        ejercicioBuscada = "<%= session.getAttribute("ejercicioAnotacionBuscada")%>";
        numeroBuscada = "<%= session.getAttribute("numeroAnotacionBuscada")%>";
        tipoBuscada = "<%= session.getAttribute("tipoAnotacionBuscada")%>";
        document.forms[0].codProcedimientoRoles.value = "<bean:write name="MantAnotacionRegistroForm" property="cod_procedimiento"/>";
        cod_procedimiento_anterior = document.forms[0].codProcedimientoRoles.value;
        <% if (altaDesdeTramitar) { %> 
            mostrarCapasBotones('capaBotonesAltaDesdeTramite');
        <% } else { %>
            mostrarCapasBotones('capaBotones5');
            mostrarCapaAnotacionContestada(true,ejercicioBuscada, numeroBuscada, "<%= descriptor.getDescripcion("gEtiqAnotContest") %>");
        <% } %>
        mostrarDescripcionTipoDoc();
        <% session.removeValue("ejercicioAnotacionBuscada");
        session.removeValue("numeroAnotacionBuscada");
        session.removeValue("tipoAnotacionBuscada");
    } %>
    desactivaNotificaciones();
    changeNotifs = true;
    cambiaUnidadOrganica();
    document.getElementById('capaTiposEstadoAnotacion').style.visibility = "hidden";
    if (top.menu.modificando == 'N') ocultarBotonesEdicionInteresados();
    else mostrarBotonesEdicionInteresados();
    if (terceros.length > 1) mostrarBotonesNavegacionInteresados();
    else ocultarBotonesNavegacionInteresados();
    deshabilitarTipoDocTerceroYDoc();        
}

<% if (permisoMantenimiento) {%>
    // devuelve true si el codigo de la uor del argumento esta de baja
        function esUorBaja(uor_a_buscar) {

            var encontrado = buscarUorPorCod(uor_a_buscar);
            if(encontrado == null) {
                return false;
            }


            if(encontrado.uor_estado == 'B') {
                return true;
            }
            else if(encontrado.uor_estado == 'A') {
                return false;
            }

            return null;
        }

        function validarObligatoriosAqui() {
            desactivarCampoDocumento();
            var msjObl = '<%=descriptor.getDescripcion("msjObligTodos")%>';
            
            //Comprobamos si el asunto es obligatorio
            var obligatorioAsunto = <%=mantARForm.getObligatorioAsuntoCodificado()%>;
            if(obligatorioAsunto == true){
                if(document.getElementById("codAsunto").value == ""){
                    jsp_alerta('A',msjObl);
                    pleaseWait('off');
                    return false;
                }//if(document.getElementById("codAsunto").value == "")
            }//if(obligatorioAsunto == true)

            // Desactivado esta transformacion ya que no era necesaria.
            //document.forms[0].asunto.value = escape(document.forms[0].asunto.value);

            if (!validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                pleaseWait('off');
                return false;
            }
                //Combos.
                if (document.forms[0].cbTipoEntrada.value != 0 && document.forms[0].cbTipoEntrada.value != 1
                    && document.forms[0].cbTipoEntrada.value != 2)
                {
                    pleaseWait('off');
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjAbrirCond10")%>');
                        tp1.setSelectedIndex(0);
                        document.forms[0].cbTipoEntrada.focus();
                        return false;
                    }

                // COMPROBACIONES DE FECHAS
                var fechaDocumentoTxt = document.forms[0].fechaDoc.value;
                var fechaGrabacionTxt = document.forms[0].fechaDocumento.value;
                var fechaPresentacionTxt = document.forms[0].fechaAnotacion.value;
                
                var fechaDocumento =
                    new Date(fechaDocumentoTxt.substring(6,10),
                             fechaDocumentoTxt.substring(3,5) - 1,
                             fechaDocumentoTxt.substring(0,2));

                var fechaGrabacion =
                    new Date(fechaGrabacionTxt.substring(6,10),
                             fechaGrabacionTxt.substring(3,5) - 1,
                             fechaGrabacionTxt.substring(0,2));

                var fechaPresentacion =
                    new Date(fechaPresentacionTxt.substring(6,10),
                             fechaPresentacionTxt.substring(3,5) - 1,
                             fechaPresentacionTxt.substring(0,2));
                             
                //document.forms[0].fechaAnotacion.value = fechaPresentacionTxt;
                //document.forms[0].fechaDocumento.value = fechaDocumentoTxt;

                <% if (restriccion_horas.equalsIgnoreCase("SI")) {%>
                // Fecha presentacion <= fecha grabacion
                if (comparaFechas(fechaPresentacion,fechaGrabacion) == 1) {
                    pleaseWait('off');
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjAnotCond2")%>');
                    tp1.setSelectedIndex(0);
                    return false;
                }
                <%}%>
                // Fecha documento <= fecha presentacion
                if (comparaFechas(fechaDocumento,fechaPresentacion) == 1) {
                    pleaseWait('off');
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjAnotCond4")%>');
                    tp1.setSelectedIndex(0);
                    return false;
                }


                                        // ---> Terceros.
                                        if ( (document.forms[0].codTerc.value == "0") || (document.forms[0].codTerc.value == "") || (document.forms[0].codTerc.value == null)
                                            || (document.forms[0].codDomTerc.value == "0") || (document.forms[0].codDomTerc.value == "") || (document.forms[0].codDomTerc.value == null)
                                            || (document.forms[0].numModifTerc.value == "0") || (document.forms[0].numModifTerc.value == "") || (document.forms[0].numModifTerc.value == null) ) {

                                        jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotCondTerc")%>');
                                            tp1.setSelectedIndex(0);
                                            buscarDocTipoDoc();

                                            return false;

                                        }
                                        // ---> Rol 
                                        if((document.forms[0].codRolTercero.value=="") || (document.forms[0].codRolTercero.value=="null") ||(document.forms[0].codRolTercero.value=="0")){
                                             pleaseWait('off');
                                            jsp_alerta("A", 'Debe rellenar todos los campos obligatorios');
                                            tp1.setSelectedIndex(0);
                                           
                                            return false;

                                        }

                                        // ---> DESTINO
                                        <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                                        if (document.forms[0].cbTipoEntrada.value==0) {
                                            onchangeCod_uniRegDestinoORD();
                                            if (Trim(document.forms[0].cod_uniRegDestinoORD.value)==''){
                                                    pleaseWait('off');
                                                    jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdUni")%>');
                                                        tp1.setSelectedIndex(0);
                                                        document.forms[0].cod_uniRegDestinoORD.focus(); // TODO el otro no aceptaba el focus
                                                        return false;
                                            }
                                        }else if (document.forms[0].cbTipoEntrada.value ==1){
                                            if(typeof integracionSIRLanbide != 'undefined' && integracionSIRLanbide == "1"){
                                                if (Trim(document.forms[0].codigoUnidadDestinoSIR.value) == ''){
                                                    pleaseWait('off');
                                                    jsp_alerta('A','<%=descriptor.getDescripcion("msgSelecDestinoSalidaSIR")%>');
                                                    tp1.setSelectedIndex(0);
                                                    document.forms[0].codigoUnidadDestinoSIR.focus();
                                                    return false;
                                                }
                                            }else{
                                                if (Trim(document.forms[0].cod_orgDestino.value) == ''){
                                                    pleaseWait('off');
                                                    jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOtrRegOrg")%>');
                                                    tp1.setSelectedIndex(1);
                                                    document.forms[0].cod_orgDestino.focus();
                                                    return false;
                                                }
                                            }
                                        } else if (document.forms[0].cbTipoEntrada.value ==2){
                                            onchangeCod_uniRegDestinoORD();
                                            if (Trim(document.forms[0].cod_uniRegDestinoORD.value)==''){
                                                pleaseWait('off');
                                                jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdUni")%>');
                                                tp1.setSelectedIndex(0);
                                                document.forms[0].cod_uniRegDestinoORD.focus();
                                                return false;
                                            }
                                        }else { // No hay un tipo de entrada seleccionado
                                            pleaseWait('off');
                                                jsp_alerta('A','FALTA TIPO ENTRADA');
                                                tp1.setSelectedIndex(0);
                                                document.forms[0].cbTipoEntrada.select();
                                                return false;
                                        }
                                        <% } else {%>
                                        if(document.forms[0].cbTipoEntrada.value == 1){ // Salida SIR
                                            // Unidad Origen se carga desde properties validar solo que este rellena
                                            if (Trim(document.forms[0].cod_uniRegDestinoORD.value)==''){
                                                pleaseWait('off');
                                                jsp_alerta('A','<%=descriptor.getDescripcion("msgSelecOrigenSalidaSIR")%>');
                                                document.forms[0].cod_uniRegDestinoORD.focus();
                                                return false;
                                            }
                                            // Validar que este cumplimentado la unidad organica destino
                                            if (document.forms[0].codigoUnidadDestinoSIR != undefined && document.forms[0].codigoUnidadDestinoSIR != null && Trim(document.forms[0].codigoUnidadDestinoSIR.value)==''){
                                                pleaseWait('off');
                                                jsp_alerta('A','<%=descriptor.getDescripcion("msgSelecDestinoSalidaSIR")%>');
                                                tp1.setSelectedIndex(0);
                                                document.forms[0].codigoUnidadDestinoSIR.focus();
                                                return false;
                                            }
                                        }else{ // Salida Ordinaria (Resto de Opciones si se crean mas)
                                            onchangeCod_uniRegDestinoORD();
                                            if (Trim(document.forms[0].cod_uniRegDestinoORD.value)==''){
                                                pleaseWait('off');
                                                jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdUni")%>');
                                                tp1.setSelectedIndex(0);
                                                document.forms[0].cod_uniRegDestinoORD.focus();
                                                return false;
                                            }
                                        }
                                        <% } %>
                                        return true;
        }
<% }%>

<% if (permisoMantenimiento) {%>

/* FUNCION REGISTRAR ALTA					    */


function pulsarRegistrarAlta() {
    pleaseWait('on');
    var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);

    if(t == null) {
        pleaseWait('off');
        alert('Problema tecnico al registrar');
        return;
    } else if(t == true) {
        pleaseWait('off');
        jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
        return;
    }

    if (validarObligatoriosAqui()) {
        if ((comprobarFecha(document.forms[0].fechaAnotacion,mensajeFechaNoValida)) && (comprobarFecha(document.forms[0].fechaDocumento,mensajeFechaNoValida))) {
            if (comparaFecha()) {
		// #234108
                if(cumpleTipoDocumentoObligatorio()){
                    tratarDocumentosPresentadosAlta();		
                    document.forms[0].opcion.value="registrar_alta_confirmada";
                    <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                        document.forms[0].opcion.value="registrar_alta_entrada";
                    <% } else {%>
                        document.forms[0].opcion.value="registrar_alta_salida";
                    <% }%>
                    // TEMAS
                    var l = new Array();
                    for (i=0; i < lista.length; i++)
                        l[i]=lista[i][0]+'§¥'; // Solo códigos
                    document.forms[0].listaTemas.value=l;
                    document.forms[0].target="oculto";
                    if (document.forms[0].opcionAltaDesdeConsulta.value=="SI")
                        document.forms[0].pendienteBuzon.value="no";

                    // Construimos string con las uors a notificar por correo
                    txtListaUors=document.forms[0].cod_uniRegDestinoORD.value;
                    for(i=0; i<listaUorsCorreo.length; i++) {
                        txtListaUors =txtListaUors+','+ listaUorsCorreo[i];
                    }

                    cambiarEstadoComboAsuntoCodificado(false);                
                    documentosModificados=false;
                    crearListasRelacionesTxt();
                    ocultarCapaDatosBusqueda();
                    document.forms[0].txtListaUorsCorreo.value = txtListaUors;
                    document.forms[0].action ='<c:url value="/MantAnotacionRegistro.do"/>';
                    // Datos SIR
                    if($('[name="codigoUnidadDestinoSIRHidden"]') != undefined){
                        $('[name="codigoUnidadDestinoSIRHidden"]').val($('[name="codigoUnidadDestinoSIR"]').val());
                        $('[name="listaCodTerceroSIRHidden"]').val($('[name="listaCodTercero"]').val());
                        $('[name="listaVersionTerceroSIRHidden"]').val($('[name="listaVersionTercero"]').val());
                        $('[name="listaCodDomicilioSIRHidden"]').val($('[name="listaCodDomicilio"]').val());
                    }
                    document.forms[0].submit();
                } else {
                    pleaseWait('off');
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msj_NoCumpleTipoDocOblig")%>');
                }
            } else {
                pleaseWait('off');
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoGrabarAnot")%>');
            }
        }
        else pleaseWait('off');
    }
	alta=false;
	document.forms[0].modoAlta.value=0;
}
<% }%>
    
    function buscarDocTipoDoc() {
        var busqueda = <%=mantARForm.getBusquedaAjaxTercero()%>;
        if(!consultando){
            var tipo = document.forms[0].cbTipoDoc.value;
            if (!documentoNoValido("cbTipoDoc","txtDNI",1)){
                pleaseWait('on');
                this.pulsarBuscarTerceros = function(){};
                document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
                document.forms[0].opcion.value="buscar";
                document.forms[0].action=APP_CONTEXT_PATH+"/BusquedaTerceros.do";
                document.forms[0].target="oculto";
                document.forms[0].submit();
            }
        }else{
            if(busqueda){
                var tipo = document.forms[0].cbTipoDoc.value;
                if (!documentoNoValido("cbTipoDoc","txtDNI",1)){
                    pleaseWait('on');
                    this.pulsarBuscarTerceros = function(){};
                    document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
                    document.forms[0].opcion.value="buscar";
                    document.forms[0].action=APP_CONTEXT_PATH+"/BusquedaTerceros.do";
                    document.forms[0].target="oculto";
                    document.forms[0].submit();
                }
            }//if(busqueda)
        }//if(!consultando)
    }//buscarDocTipoDoc

     

            function actualizarComboEstadoSegunTipoEntrada() {
                var tipoEntrada = document.forms[0].cbTipoEntrada.value;
                console.log("Valor cbTipoEntrada:", tipoEntrada);

                if (tipoEntrada === '1') {
                    alert("Tipo Entrada = 1: Activando combo alternativo y divDatosRegistroSIR");

                    // Mostrar combo alternativo
                    document.getElementById("capaTiposEstadoAnotacion").style.display = "none";
                    document.getElementById("capaTiposEstadoAnotacionAlt").style.display = "block";

                    // Mostrar div SIR
                    document.getElementById("divDatosRegistroSIR").style.display = "block";

                    // Aqu? rellenamos el campo con un texto por defecto
                    document.getElementById("identificadorRegistroSIR").value = "AUTO-ID-123"; // <-- prueba de valor

                    console.log("combo ALT visible, div SIR visible y valor rellenado");
                } else {
                    alert("Tipo Entrada ? 1: Restaurando combo original y ocultando divDatosRegistroSIR");

                    document.getElementById("capaTiposEstadoAnotacion").style.display = "block";
                    document.getElementById("capaTiposEstadoAnotacionAlt").style.display = "none";
                    document.getElementById("divDatosRegistroSIR").style.display = "none";

                    // Limpiar campo al cambiar tipo
                    document.getElementById("identificadorRegistroSIR").value = "";

                    console.log("combo ORIGINAL visible, div SIR oculto y campo limpio");
                }
            }


            function generaJustificante(codlistaPlantillas,listaPlantillas,editorPlantillas){
                var urlPeticion = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=altaDocumento";
                var datosPeticion;

         var datos;
         var argumentos = new Array();

         <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
         codTip='E';
         <% } else {%>
         codTip='S';
         <% }%>
         argumentos[0] =aplic ;


         if (listaPlantillas.length == 0){
            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoPlantilla")%>");
        }else if(listaPlantillas.length == 1){
             var cod = codlistaPlantillas[0][0];	
            var desc = listaPlantillas[0][0];	
            var edit = editorPlantillas[0][0];	
            datosPeticion = {	
                'codAplicacion' : aplic, 'descripcionJus' : desc, 'aplicacion' : cod,	
              'editorJustif' : edit, 'idiomaCuneus' : idiom	
            };	
            enviarPeticionGenerarJustificante(urlPeticion,datosPeticion,edit);

         }else{
           argumentos[1] = codlistaPlantillas;	
             argumentos[2] = listaPlantillas;	
             argumentos[3] = editorPlantillas;	
            abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + "/jsp/registro/entrada/justificanteEntrada.jsp",argumentos,	
	'width=650,height=400',function(datos){	
                        if(datos!=undefined){
                        datosPeticion = {	
                            'codAplicacion' : <%=apl%>, 'descripcionJus' : datos[1], 'aplicacion' : datos[0],	
                            'editorJustif' : datos[2], 'idiomaCuneus' : <%=idioma%>	
                        };	
                        enviarPeticionGenerarJustificante(urlPeticion,datosPeticion,datos[2]);	
                        }
                  });
        }

         return false;
     }

    function mostrarErrorRespuestaJustificante(codError){	
        if(codError === 1)	
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorCargarJustificante")%>');	
        if(codError === 2)	
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');	
    }
     // Rellena los campos con los datos del tercero de la lista 'terceros'que tenga el rol por defecto(el primero de ellos)
     
     

 function pulsarBuscarTercerosImpl(){
   pleaseWait('on');
   if (document.forms[0].cbTipoDoc.value == <%=m_Conf.getString("tercero.codUOR")%> &&
   (document.forms[0].txtDNI.value == '' || document.forms[0].txtDNI.value==null)) {
      var argumentos = new Array();
      argumentos[0] = document.forms[0].txtDNI.value;
      abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + "/MantAnotacionRegistro.do?opcion=seleccionUOR",argumentos,
	'width=670,height=470',function(datos){
                        // TODO: ACLARAR ESTE CASO
                        if(datos != null) {
                            // Se toma el terceroVO correspondiente a la UOR, es devuelto a traves
                            // de recuperaBusquedaTerceros
                            document.forms[0].opcion.value = "getTerceroUOR";
                            document.forms[0].target="oculto";
                            document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do' +
                                  '?tipoDocUOR=' + <%=m_Conf.getString("tercero.codUOR")%> +
                                  '&docUOR=' + datos[0] + '&nombreUOR=' + datos[1];
                            document.forms[0].submit();
                        } else
                            pleaseWait('off');
                    });
   } else {
     if (terceros.length > 0) {
       // Se cargan los datos del tercero actual para abrir la ventana de terceros
       document.forms[0].opcion.value="buscar_por_id";
       document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
       document.forms[0].target="oculto";
       document.forms[0].submit();
     } else {
       terceroBuscado([]);
     }
   }
}

// Gestiona el tratamiento de los datos devueltos por la ventana de gestion de terceros
function tratarTerceroDevuelto(tercero){
    if (tercero != undefined) {
        if (tercero[0] == '0') { // Se trata de un tercero externo
            if(tercero[2] == 1 && (tercero[5] == "" || tercero[5] == null)){
            //jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");  
            jsp_alerta("A","No se puede dar de alta en tercero porque no dispone de primer apellido y para un NIF es obligatorio"); 
            }  else {
            var request = buildRequestInsercionDirectaTercero(tercero[22]);
            document.forms[0].opcion.value = "grabarTercDomExterno";
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?' + request;
            document.forms[0].submit();
        }
        } else {
            pleaseWait('on');
            insertarTerceroPorDefecto(tercero);
            pleaseWait('on');
            mostrarDatosTercero();
        }
    } else pleaseWait('off');
}

         function pulsarBuscarTerceros(){};

         this.pulsarBuscarTerceros = pulsarBuscarTercerosImpl;



         function pulsarVerAnexoPDF(){
             var i = tabFormularios.selectedIndex;
             if(i == -1) {
                 jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                 }else{
                 var source = "<%=request.getContextPath()%>/sge/ListaAnexos.do?formPDF=" + listaFormulariosOriginal[i][1] +
                 "&estado=" + listaFormulariosOriginal[i][5];
                 abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
                        'width=500,height=500,status='+ '<%=statusBar%>',function(mensaje){
                                if ((mensaje!=null)&&(mensaje!="null")){
                                    jsp_alerta("A",mensaje);
                                }
                        });
                 }
             }

        
         /* Fin Pestaña formularios */

function recuperaBusquedaTerceros(Terceros) {
    this.pulsarBuscarTerceros = pulsarBuscarTercerosImpl;
    if (Terceros.length>0) {
        // Se ha encontrado el tercero buscado
        if (Terceros.length == 1 && Terceros[0][0] == '0') {
            var request = buildRequestInsercionDirectaTercero(Terceros[0]);
            document.forms[0].opcion.value = "grabarTercDomExterno";
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?' + request;
            document.forms[0].submit();
        } else {

            if (Terceros.length > 1) { // Varios terceros encontrados

                var argumentos = new Array();
                argumentos['modo'] = 'seleccion';
                argumentos['terceros'] = Terceros;
                argumentos['tipodoc'] = document.forms[0].cbTipoDoc.value;
                argumentos['doc'] = document.forms[0].txtDNI.value;
                var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&ventana=true';
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
                    'width=990,height=690,status='+ '<%=statusBar%>',function(tercero){
                        tratarTerceroDevuelto(tercero);
                    });        
            } else { // Un unico tercero encontrado

                var tercero = formatearArrayTercero(Terceros[0], indiceDomPrincipal(Terceros[0]));
                insertarTerceroPorDefecto(tercero);
                mostrarDatosTercero();
                pleaseWait('off');
            }
        }
    } else {
        if (top.menu.modificando == 'N') {
            jsp_alerta("A",'<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjNoRegistros"))%>');
            document.forms[0].codTerc.value = '0';
            document.forms[0].codDomTerc.value = '0';
            document.forms[0].numModifTerc.value = '0';
            pleaseWait('off');
        } else {
            // Caso de que no exista el tercero buscado, se pregunta si se quiere dar de alta
            if(jsp_alerta("",'<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjAltaTercero"))%>')) {

                var argumentos = new Array();
                argumentos['modo'] = 'alta';
                argumentos['tipodoc'] = document.forms[0].cbTipoDoc.value;
                argumentos['doc'] = document.forms[0].txtDNI.value;
                var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&ventana=true';
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
                    'width=990,height=650,status='+ '<%=statusBar%>',function(tercero){
                        tratarTerceroDevuelto(tercero);
                    });        
            } else
                pleaseWait('off');
        }
    }

    dTipoDoc = document.forms[0].descTipoDoc.value;
    tipoDoc = document.forms[0].cbTipoDoc.value;
    cambiaFoco();

}

var cambiaElFoco = false;

function cambiaFoco() {
    if (cambiaElFoco) {
        document.forms[0].codAsunto.focus();
        cambiaElFoco = false;
    }
}

      function buscarDocRazonSocial() {
          open('<%=request.getContextPath()%>/jsp/busqRazonSocial.html', 'Sizewindow', 'width=375,height=200,scrollbars=no,toolbar=no')
          }

      /********************************************************************/
      /* FUNCION VALORES POR DEFECTO COMBOS                               */
      /********************************************************************/
      // Pone en los combos de tipo de documento, remitente, tipo entrada y
      // transporte los valores por defecto.
      function valoresPorDefectoCombos() {
          // Tipo documento.
          valor=0;
          for(i=0;i<=cod_tiposDocumentos.length;i++){
              if(act_tiposDocumentos[i]=='SI'){
                  document.forms[0].txtCodigoDocumento.value=cod_tiposDocumentos[i];
                  valor=1;
              }
          }
          //si no esta ninguno activo dejo por defecto el que tenia o en blanco
          if(valor==0){
              document.forms[0].txtCodigoDocumento.value='';
          }

          actualizarDescripcion('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentosAux,desc_tiposDocumentosAux);
          
          // Tipo entrada.
          document.forms[0].cbTipoEntrada.value=0;
          actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
          mostrarDestino();

          // Tipo remitente
          valor=0;
          for(i=0;i<=cod_tiposRemitentes.length;i++){
              if(act_tiposRemitentes[i]=='SI'){
                  document.forms[0].cod_tipoRemitente.value=cod_tiposRemitentes[i];
                  valor=1;
              }
          }
          //si no esta ninguno activo dejo por defecto el que tenia o en blanco
          if(valor==0){
              document.forms[0].cod_tipoRemitente.value='';
          }

          actualizarDescripcion('cod_tipoRemitente','txtNomeTipoRemitente',cod_tiposRemitentes,desc_tiposRemitentes);

          // Tipo transporte
          document.forms[0].txtNumTransp.value=''; // Num transporte

          valor=0;
          for(i=0;i<=cod_tiposTransportes.length;i++){
              if(act_tiposTransportes[i]=='SI'){
                  document.forms[0].cod_tipoTransporte.value=cod_tiposTransportes[i];
                  valor=1;
              }
          }
          //si no esta ninguno activo dejo por defecto el que tenia o en blanco
          if(valor==0){
              document.forms[0].cod_tipoTransporte.value='';
          }

          actualizarDescripcion('cod_tipoTransporte','desc_tipoTransporte',cod_tiposTransportes,desc_tiposTransportes);
      }

          function borrarPaginaBuscada() {
              comboAsuntos.selectItem(0);
              if(mod != 1) {
                  document.forms[0].ano.value="";
                  document.forms[0].numero.value="";
              }
              document.forms[0].fechaDocumento.value=''; // Fecha documento
              <% 	if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
              document.forms[0].horaMinDocumento.value="";
              <% }%>

              document.forms[0].fechaAnotacion.value=''; // Fecha entrada
              document.forms[0].horaMinAnotacion.value="";
              document.forms[0].asunto.value=''; // Asunto
              document.forms[0].autoridad.value='';
               document.forms[0].codigoSga.value='';	
              document.forms[0].expedienteSga.value='';
              document.forms[0].fechaDoc.value=''; // Fecha documento (3.10)
              document.forms[0].cod_actuacion.value=''; // Actuacion.
              document.forms[0].txtNomeActuacion.value='';
              // Pestaña 2.
              document.forms[0].cbTipoDoc.value=''; // Tipo documento.
              document.forms[0].descTipoDoc.value='';
              document.forms[0].txtDNI.value=''; // Documento.
              document.forms[0].txtInteresado.value=''; // Razon Social.
              document.forms[0].txtApell1.value=''; // Apellidos.
              document.forms[0].txtApell2.value='';
              document.forms[0].txtPart.value=''; // Partículas.
              document.forms[0].txtPart2.value='';
              document.forms[0].txtTelefono.value=''; // Telefono.
              document.forms[0].txtCorreo.value=''; // Email.
              document.forms[0].txtPais.value=''; // Pais.
              document.forms[0].txtProv.value=''; // Provincia.
              document.forms[0].txtMuni.value=''; // Municipio.
              document.forms[0].txtDomicilio.value=''; // Nombre vía.
              document.forms[0].txtPoblacion.value='';
              document.forms[0].txtCP.value=''; // Codigo.
              document.forms[0].codRolTercero.value='';
              document.forms[0].descRolTercero.value='';
              terceros =  new Array(); // Multi interesados
              document.forms[0].codTerc.value = '';
    borrarDestinoOtroReg();
              document.forms[0].txtExp1.value=''; // Expediente relacionado.
              document.forms[0].cod_uniRegDestinoORD.value=''; // Unidad de rexistro.
              document.forms[0].cod_uor.value=''; // Unidad de registro cod visible
              document.forms[0].desc_uniRegDestinoORD.value='';
    borrarDestinoOrdinaria();
    borrarProcedenteOtroReg();
              if (document.getElementById("capaTiposEstadoAnotacion").style.visibility =='visible') {
                  document.forms[0].cod_estadoAnotacion.value='';
                  document.forms[0].desc_estadoAnotacion.value='';
              }
              //Pestaña 5
              document.forms[0].observaciones.value = '';
          }

          <% if (permisoMantenimiento) {%>
          /********************************************************************/
          /* FUNCION INICIAR ALTA												*/
          /********************************************************************/

          function iniciarAlta(lista_CODtiposDocumentos, lista_DESCtiposDocumentos,lista_ACTtiposDocumentos,
              lista_CODtiposDocumentosAlta, lista_DESCtiposDocumentosAlta,lista_ACTtiposDocumentosAlta,
              lista_CODtiposTransportes, lista_DESCtiposTransportes,
              lista_CODtiposRemitentes, lista_DESCtiposRemitentes,
              lista_CODtemas, lista_DESCtemas,
              lista_CODidInteresado, lista_DESCidInteresado,
              lista_CODdpto, lista_DESCdpto,
              lista_uni_asuntos, lista_cod_asuntos, lista_desc_asuntos,
              lista_cod_roles, lista_desc_roles, lista_defecto_roles,
              fechaServidor, horaServidor, fecha)
          {
              alta=true;
	      document.forms[0].modoAlta.value=1;
              noObligatorioToObligatorioConsulta();
              ocultarInfSoloConsulta();
              activarFormulario();
              terceroActual = -1;

              // Desactivamos los campos de notificaciones activados por el metodo anterior.
              var vectorNotificar = new Array(document.forms[0].cmdNotificar);
              deshabilitarGeneral(vectorNotificar);
              document.forms[0].enviarCorreo.disabled="true";

              deshabilitarBuscar();
              desactivarOrigenYExpediente();
              desactivarInteresado();
              var vCamposCodigo= camposCodigo();
              cambiarLongMaxInput(vCamposCodigo,longMaxInputCodigo);
              var vCamposFecha = camposFecha();
              cambiarLongMaxInput(vCamposFecha,longMaxInputFecha);
              modificando('S');
              mostrarDestino();
              
              
              uni_asuntos = lista_uni_asuntos;
              cod_asuntos = lista_cod_asuntos;
              desc_asuntos = lista_desc_asuntos;
              
              comboAsuntos.addItems(lista_cod_asuntos,lista_desc_asuntos);
              comboAsuntos.selectItem(0);
              comboAsuntos.activate();

              listaUorsCorreo = new Array();

              document.forms[0].ano.value ='';
              document.forms[0].numero.value ='';

              cod_roles = lista_cod_roles;
              desc_roles = lista_desc_roles;
              defecto_roles = lista_defecto_roles;
              comboRol.addItems(cod_roles, desc_roles);
              for(var i=0; i<defecto_roles.length; i++) {
                  if (defecto_roles[i] == 'SI') {
                      comboRol.buscaCodigo(cod_roles[i]);
                      break;
                  }
              }
              mostrarBotonesEdicionInteresados();
                deshabilitarDatosSGA();
              cod_tiposDocumentos = lista_CODtiposDocumentos;
              desc_tiposDocumentos = lista_DESCtiposDocumentos;
              act_tiposDocumentos=lista_ACTtiposDocumentos;


              cod_tiposDocumentosAlta = lista_CODtiposDocumentosAlta;	
              desc_tiposDocumentosAlta = lista_DESCtiposDocumentosAlta;	
              act_tiposDocumentosAlta=lista_ACTtiposDocumentosAlta;
              cod_tiposTransportes = lista_CODtiposTransportes;
              desc_tiposTransportes= lista_DESCtiposTransportes;

              cod_tiposRemitentes= lista_CODtiposRemitentes;
              desc_tiposRemitentes= lista_DESCtiposRemitentes;

              cod_temas= lista_CODtemas;
              desc_temas= lista_DESCtemas;
              cod_tiposIdInteresado = lista_CODidInteresado;
              desc_tiposIdInteresado = lista_DESCidInteresado;
              cod_departamentos = lista_CODdpto;
              desc_departamentos = lista_DESCdpto;

              fechaHoy=fecha;

              document.forms[0].fechaAnotacion.value=fechaServidor;
            //fechad e grabación la da el servidor no puede ser modificada
              document.forms[0].fechaDocumento.value=fechaServidor;
              var vectorFecDoc = [document.forms[0].fechaDocumento];
              deshabilitarGeneral(vectorFecDoc);

              // Hora de presentacion tambien la da el servidor
              document.forms[0].horaMinAnotacion.value = horaServidor;

			  bloquearFechaHoraAnotacion();

           mostrarCapasBotones('capaBotones2');
           tp1.setSelectedIndex(0);
           <% 	if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
           tipoDeRemitentePorDefecto();
           <% }%>
           tipoDeDocumentoPorDefecto();
           // Para saber cuando cambiamos de procedimiento
           cod_procedimiento_anterior = document.forms[0].cod_procedimiento.value;
		   tipoDeDocumentoAnotacionPorDefecto()
           // Colocamos el foco en el campo de tipo de documento de interesado.
           document.forms[0].cbTipoDoc.focus();
           
           //Marcamos en el caso de que sea obligatorio el campo de asunto
           var obligatorioAsunto = <%=mantARForm.getObligatorioAsuntoCodificado()%>;           
           if(obligatorioAsunto) cambiarEstadoComboAsuntoCodificado(true);
           // Se vacía el código del expediente y su descripción relacionado si tuviesen algún valor           
           document.getElementById("codListaExpedientesRelacionados").value="";
           document.getElementById("descListaExpedientesRelacionados").value="";
       }

     /********************************************************************/
     /* FUNCION REGISTRO CERRADO					 */
     /********************************************************************/

     function registroCerrado() {

        pleaseWait('off');
         var mnsj='<%=descriptor.getDescripcion("msjRegCAnot")%>'
         jsp_alerta("A", mnsj);
         activarFormulario();
         if (document.getElementById('capaBotones2'))
             if (document.getElementById('capaBotones2').style.display=='') {
                 document.forms[0].cmdRegistrarAlta.disabled=false;
                 document.forms[0].cmdCancelarAlta.disabled=false;
             }
             if (document.getElementById('capaBotones5'))
                 if (document.getElementById('capaBotones5').style.display=='') {
                     document.forms[0].cmdRegistrarDuplicar.disabled=false;
                     document.forms[0].cmdCancelarDuplicar.disabled=false;
                 }

             }
/********************************************************************/
/* FUNCION CONFIRMAR ALTA					    */
/********************************************************************/

function confirmarAlta() { 
    activarFormulario();
    deshabilitarBuscar();
    desactivarOrigenYExpediente();
    desactivarInteresado();
    mostrarCapaDatosBusqueda();
    pleaseWait('off');
    jsp_alerta("A",'<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjUltAnotPosterior"))%>');
}

// Alertar cuando la fecha de anotacion es posterior a la del servidor
function alertarFechaPosterior() {
    activarFormulario();
    deshabilitarBuscar();
    desactivarOrigenYExpediente();
    desactivarInteresado();
    mostrarCapaDatosBusqueda();
    pleaseWait('off');
    jsp_alerta("A",'<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjNoGrabarAnot"))%>');
}
                 /********************************************************************/
                 /* FUNCION NO EXISTE EXPEDIENTE										*/
                 /********************************************************************/

                 function noExisteExpediente() {
                     pleaseWait('off');
                     var mnsj='<%=descriptor.getDescripcion("msjNoExistExp")%>'
                     activarFormulario();
                     deshabilitarBuscar();
                     desactivarOrigenYExpediente();
                     desactivarInteresado();
                     jsp_alerta('A',mnsj)
                 }
                 /********************************************************************/
                 /* FUNCION PROC MAL RELACIONADO 									*/
                 /********************************************************************/

                 function procMalRelacionado() {
                     pleaseWait('off');
                     var mnsj='<%=descriptor.getDescripcion("msjProcMalRel")%>'
                     activarFormulario();
                     deshabilitarBuscar();
                     desactivarOrigenYExpediente();
                     desactivarInteresado();
                     jsp_alerta('A',mnsj);
                 }
                 /********************************************************************/
                 /* FUNCION ALTA ENTRADA REGISTRADA									*/
                 /********************************************************************/

                 function altaEntradaRegistrada(ej,num) {

                     <% if (contestacion) {%>
                        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjAltaAnotRespVolver")%>') ==1) {
                            pulsarCancelarContestar();
                        }
                     <% }%>
         
                     //jsp_alerta("A", mnsj);
                     desactivarFormulario();
                     desactivarComboAsuntos();
                     modificando('N');
                     mostrarDestino();
                     borrarPaginaBuscada();
                     borrarInteresado();
                     ocultarLista();
                     ocultarCalendario();
                     borrarPaginaBuscada();
                     document.forms[0].codTerc.value="";
                     document.forms[0].codDomTerc.value="";
                     document.forms[0].numModifTerc.value="";
                     clean();
                     document.forms[0].horaMinAnotacion.value="";
                     document.forms[0].cbTipoDoc.value="";
                     document.forms[0].descTipoDoc.value="";
                     document.forms[0].cbTipoEntrada.value=-1;
                     document.forms[0].txtNomeTipoEntrada.value="";
                     document.forms[0].txtCodigoDocumento.value='';
                     document.forms[0].tipoConsulta[1].checked = true;

                     document.forms[0].ano.value = ej;
                     document.forms[0].numero.value = num;

                     document.forms[0].opcion.value="consultar";
                     document.forms[0].target="oculto";
                     document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                     document.forms[0].submit();
                 }

                 <% }%>

                 /********************************************************************/
                 /* FUNCION MOSTRAR LISTA UNID REG DESTINO							*/
                 /********************************************************************/


                 function mostrarListaUnidRegDestino(){

                     if (
                         (Trim(document.forms[0].cod_orgDestino.value) != '')
                         //&& (Trim(document.forms[0].cod_entDestino.value) != '')
                         //&& (Trim(document.forms[0].cod_dptoDestino.value) != '')
                     ){
                     var condiciones = new Array();

                     //condiciones[0]='UOR_DEP'+'§¥';
                     //condiciones[1]= document.forms[0].cod_dptoDestino.value ;
                     condiciones[0]='UOREX_ORG'+'§¥';
                     condiciones[1]=document.forms[0].cod_orgDestino.value ;
                     muestraListaTabla('UOREX_COD','UOREX_NOM',EsquemaGenerico + 'A_UOREX A_UOREX',condiciones,'cod_uniRegDestino','desc_uniRegDestino','botonUnidadeRexistro','100');
                 } else document.forms[0].cod_uniRegDestino.value=''; // Si viene del onchange permaneceria
             }
             /********************************************************************/
             /* FUNCION MOSTRAR LISTA UNID REG ORIGEN							*/
             /********************************************************************/

             function mostrarListaUnidRegOrigen(){

                 if ( (Trim(document.forms[0].cod_orgOrigen.value) != '')){
                     var condiciones = new Array();
                     condiciones[0]='UOREX_ORG'+'§¥';
                     condiciones[1]=document.forms[0].cod_orgOrigen.value ;
                     muestraListaTabla('UOREX_COD','UOREX_NOM',EsquemaGenerico + 'A_UOREX A_UOREX',condiciones,'cod_unidadeRexistroOrixe','desc_unidadeRexistroOrixe','botonUnidadeRexistroOrigen','100');
                 } else document.forms[0].cod_unidadeRexistroOrixe.value=''; // Si viene del onchange permaneceria
             }


         function horaMinDoc() {
             var today = new Date();
             var hoy;
             var hora;
             var min;

             hoy = today.toString();
             if (hoy.substring(9,10)==" ") {
                 hora = hoy.substring(10,12);
                 min = hoy.substring(13,15);
             } else {
             hora = hoy.substring(11,13);
             min = hoy.substring(14,16);
         }
         <% if ("E".equals(tipoAnotacion)) {%>
         document.forms[0].horaMinDocumento.value=hora+":"+min;
         <%}%>
     }

 var anoSeleccionada;
 var numeroSeleccionada;
 var numAnotaciones;

 function recargaConsulta() {
     determinarAnotacion("<bean:write name="MantAnotacionRegistroForm" property="posicionAnotacion"/>");
     }

function anotacionModificada() {
<%if (!("reservas".equals(respOpcion))) {%>
    mostrarCapasBotones('capaBotones3');
    habilitarBuscar();
    document.forms[0].cmdListado.disabled=false;
    document.forms[0].tipoConsulta[1].checked=true;
    document.forms[0].opcion.value="recargar_encontradas";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
    document.forms[0].submit();
<% } else {%>
	    if (document.forms[0].duplicar.value == '1'){
        jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjResRegistrada"))%>');
	    fechaHoy = "<bean:write name="MantAnotacionRegistroForm" property="fechaHoraHoy"/>";
	    activarFormulario();
	    deshabilitarBuscar();
	    desactivarOrigenYExpediente();
	    desactivarInteresado();
	    ocultarInfSoloConsulta();
	    actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
	    document.forms[0].fechaDocumento.value = fechaHoy;
	    var vectorFecDoc = [document.forms[0].fechaDocumento,document.forms[0].fechaAnotacion];
	    deshabilitarGeneral(vectorFecDoc);
	    document.forms[0].fecPresRes.value = document.forms[0].fechaAnotacion.value;
	    deshabilitarImagenCal("calFechaAnotacion",true);
    } else {
        jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjResRegistrada"))%>');
        desactivarFormulario();
        ocultarCapaDatosBusqueda();
        modificando('N');
        document.forms[0].opcion.value="consultar";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do?tipoConsulta=registroAregistro";
        document.forms[0].submit();
    }
<% }%>
}

 function pulsarConsulta(){    // ¡¡¡Recargando!!!
     <% if ("E".equals(tipoAnotacion)) {%>
     document.forms[0].opcion.value="Relacion_E";
     <% } else {%>
     document.forms[0].opcion.value="Relacion_S";
     <% }%>
     document.forms[0].target="mainFrame";
     document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
     document.forms[0].submit();
 }

 function activarFormularioConsulta() {
     activarFormulario();
     var vectorBotonesDoc = [document.forms[0].cmdVerDoc, document.forms[0].cmdAltaDoc, document.forms[0].cmdModificarDoc, document.forms[0].cmdEliminarDoc,
                             document.forms[0].cmdAltaAnterior, document.forms[0].cmdModAnterior, document.forms[0].cmdEliminarAnterior];
	 if(<%=mostrarCotejo%>) vectorBotonesDoc.push(document.forms[0].cmdCotejarDoc);
	 if(<%=mostrarDigitalizar%>) {
		 vectorBotonesDoc.push(document.forms[0].cmdDigitalizarDesdeConsulta);
		 vectorBotonesDoc.push(document.forms[0].cmdFinDigitalizar);
	 }
     deshabilitarGeneral(vectorBotonesDoc);

     var vectorNotificar = new Array(document.forms[0].cmdNotificar);
     deshabilitarGeneral(vectorNotificar);

     obligatorioToNoObligatorio(document.forms[0]);
     desactivarOrigenYExpediente();
     desactivarInteresado();
     var v2 = new Array(document.forms[0].txtInteresado);
     habilitarGeneral(v2);
     var v3 = new Array(document.forms[0].horaMinAnotacion);
     //noObligatorioToObligatorioGeneral(v3)
     deshabilitarGeneral(v3);
     <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
     var v4 = new Array(document.forms[0].horaMinDocumento);
      $("[name='cmdDigitalizarDesdeConsulta']").hide();
      $("[name='cmdFinDigitalizar']").hide();
     //noObligatorioToObligatorioGeneral(v4)
     deshabilitarGeneral(v4);
     <%}%>
     tp1.setSelectedIndex(0);
     if (document.getElementById("capaTiposEstadoAnotacion") )
         document.getElementById("capaTiposEstadoAnotacion").style.visibility='';
     // Longitud campos codigo.
     var vCamposCodigo= camposCodigo();
     cambiarLongMaxInput(vCamposCodigo,longMaxInputCodigoConsulta);
     var vCamposFecha = camposFecha();
     cambiarLongMaxInput(vCamposFecha,longMaxInputCodigoConsulta);

     vectorImg = new Array(document.getElementsByName("consultaExpediente")[0]);
     deshabilitarImagen(vectorImg, true);
 }

 function pulsarSalirConsultar() {
     if (mod == 0) { //Si estamos modificando
         pulsarCancelarModificar();
     } else {
         ocultarLista();
         ocultarCalendario();
         <% if ("Relacion_E".equals(tipoAnotacion) || "E".equals(tipoAnotacion)) {%>
         document.forms[0].opcion.value="E";
         <% } else {%>
         document.forms[0].opcion.value="S";
         <% }%>
         document.forms[0].target="mainFrame";
         document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
         document.forms[0].submit();
    }
 }

 function pulsarListado() {
      var desdeRechazadas = <%=desdeEntradasRechazadas%>; 
      var desdePendientes = <%=desdePendientesFinalizar%>;
      if(<%=desdeEntradasRechazadas%>){
           document.forms[0].opcion.value="volver_listado_rechazadas";
           document.forms[0].target="mainFrame";
           document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
           document.forms[0].submit();
       }else if (desdePendientes){
           document.forms[0].opcion.value="volver_listado_pendientes_finalizar";
           document.forms[0].target="mainFrame";
           document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
           document.forms[0].submit();
      } else {
            //document.forms[0].ano.disabled=false;
            //document.forms[0].numero.disabled=false;
            var vector = new Array(document.forms[0].ano, document.forms[0].numero);
            habilitarGeneral(vector);
            document.forms[0].opcion.value="ver_consulta_listado";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
            document.forms[0].submit();
        }
 }


 function consultarEnSIR(form) {

     var identificador =form.identificadorRegistroSIR.value;
    form.identificadorRegistroSIRValue.value = identificador;

    form.opcion.value = "consultaEnSIR";
    form.target = "mainFrame";
    form.action = "<%=request.getContextPath()%>/MantAnotacionRegistro.do";
    pleaseWailt('on');
     form.submit();
          }


function pulsarConsultar() {
    var tipoEntrada = document.forms[0].cbTipoEntrada.value;
    if (tipoEntrada === '') {
        jsp_alerta('A', MENSAJE_TIPO_ENTRADA_REQUERIDO);
        return;
    }

    if (tipoEntrada === '1'){
        consultarEnSIR(document.forms[0]);
    } else {
        var valido=true;
        if (!comprobarFecha(document.forms[0].fechaAnotacion,mensajeFechaNoValida))
            valido=false;
        else if (!comprobarFecha(document.forms[0].fechaDocumento,mensajeFechaNoValida))
            valido=false;
        //if ((document.forms[0].cbTipoEntrada.value=='') && (Trim(document.forms[0].cod_dptoDestinoORD.value)!='')) { // TODO rmved
        if (valido) {
            with(document.forms[0]){
                cmdConsultar.disabled=true;
                cmdLimpiar.disabled=true;
                <% if (permisoMantenimiento) {%>
                cmdAlta.disabled=true;
                <% }%>
             }
             // TEMAS
             var l = new Array();
             for (i=0; i < lista.length; i++)
                 {
                     l[i]=lista[i][0]+'§¥'; // Solo códigos
                 }
                 pleaseWait('on');
                 document.forms[0].listaTemas.value=l;
                 document.forms[0].opcion.value="consultar";
                 if (document.forms[0].tipoConsulta[0].checked) // Listado
                     document.forms[0].target="mainFrame";
                 else  document.forms[0].target="oculto";

                 document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                 document.forms[0].submit();
             }
             cargarTipoDocAux("Alta");
        }
     }

     function actualizaAnotacionNavegacion(anotacionSelecc) {

         if (numAnotaciones>0) {
             // Ocultar div anulacion
             ocultarCapaAnulacion();
             ocultarCapaDiligencia();
             // TODO rmved document.forms[0].cod_dptoDestinoORD,
             // TODO document.forms[0].desc_dptoDestinoORD,
             var vector1 = new Array(document.forms[0].cod_uor,
                 document.forms[0].desc_uniRegDestinoORD,
                 document.forms[0].cod_procedimiento,document.forms[0].desc_procedimiento);
             // TODO rmved document.forms[0].botonDepartamentoORD,
             var vectorBoton1 =  new Array(document.getElementsByName('botonUnidadeRexistroORD')[0],document.getElementsByName('botonProcedimiento')[0]);
             // TODO rmvd document.all.anchorDepartamentoORD,
             var vectorAnchor1;
             if(document.all)
                    vectorAnchor1 =  new Array(document.all.anchorUnidadeRexistroORD,document.all.anchorProcedimiento);
             else{
                 var anchorsUnidadeRexistroORD = document.getElementsByName("anchorUnidadeRexistroORD");
                 var anchorsProcedimiento          = document.getElementsByName("anchorProcedimiento");
                 if(anchorsUnidadeRexistroORD!=null && anchorsUnidadeRexistroORD.length>=1 && anchorsProcedimiento!=null && anchorsProcedimiento.length>=1)
                        vectorAnchor1 =  new Array(anchorsUnidadeRexistroORD[0],anchorsProcedimiento[0]);
             }
             noObligatorioToObligatorioConsulta();            
             deshabilitarGeneral(vector1);
             deshabilitarImagenBotonGeneral(vectorBoton1, true);
             document.forms[0].ano.value = anoSeleccionada;
             document.forms[0].numero.value = numeroSeleccionada;
             calcularLimites(anotacionSelecc);

             var vector = new Array(document.forms[0].ano, document.forms[0].numero);
             habilitarGeneral(vector);
             document.forms[0].modificar.value = "1";
             document.forms[0].opcion.value="buscar";
             document.forms[0].target="oculto";
             document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
             document.forms[0].submit();
             desactivarFormulario();
             <% if (permiso_contestar) {%>
                if (tipoActual=='E') {
                   habilitarGeneral(new Array(document.forms[0].cmdContestar));      
                } else
                   habilitarGeneral(new Array(document.forms[0].cmdResponder));
             <% } %>
             anotacionActual = anotacionSelecc;
             
             if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=false;

         } else { // Desactivar todos los botones si no existen anotaciones.

         document.forms[0].ano.value="", document.forms[0].numero.value="";
         document.forms[0].cbTipoDoc.value=""; document.forms[0].descTipoDoc.value="";
         document.forms[0].cbTipoEntrada.value="0"; document.forms[0].txtNomeTipoEntrada.value="";
         desactivarFormulario();
         if ( document.getElementById("capaBotones3")) { 
             mostrarCapasBotones('capaBotones3');
             document.forms[0].cmdCancelarBuscada.disabled=false;
             document.getElementsByName("cmdCuneus")[0].disabled=false;
             if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true; document.getElementById('cmdModificar').style.color = '#CCCCCC';
             if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true; document.getElementById('cmdAnular').style.color = '#CCCCCC';
             if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=true;
             if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=true;
             if (document.forms[0].cmdContestar) document.forms[0].cmdContestar.disabled=true; 
             if (document.forms[0].cmdResponder) document.forms[0].cmdResponder.disabled=true;
             if (document.forms[0].cmdRelacionar) document.forms[0].cmdRelacionar.disabled=true;
         }
     }
     deshabilitarBuscar();
     domlay('capaNavegacionConsulta',1,0,0,navegacionConsulta());
     document.forms[0].cmdListado.disabled=false;
     document.getElementById("cmdCuneus").disabled = false;    
 }

 var consultando=false;
 var alta=false;

function pulsarDocumentos(){

     var source="<%=request.getContextPath()%>/editor/DocumentosAplicacion.do?opcion=cargarAplicaciones&aplicacion=";
     source=source+'aplic';
     source=source+"&ventana=modal";
     source=source+"&modo=1";
     abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/editor/mainVentana.jsp?source="+source,'',
	'width=750,height=510,status='+ '<%=statusBar%>',function(datos){});
}

function pulsarImprimirCuneus(grabarDuplicar){
    grabarDuplicarCuneus = grabarDuplicar;
        
    <%String prev = m_Conf.getString("Registro.prev.cuneos");
            if (prev.equals("si")) {%>
                    var source = "<%=request.getContextPath()%>/jsp/registro/posicionCuneus.jsp?opcion=s";
                    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/editor/mainVentana.jsp?source="+source,'',
                           'width=570,height=450,status='+ '<%=statusBar%>',function(res){
                                if(res!=undefined){
                                    document.forms[0].idiomaCuneus.value = res[0];
                                    document.forms[0].posicionCuneus.value = res[1];
                                    document.forms[0].nCopiasCuneus.value = res[2]

                                    document.forms[0].grabarDuplicar.value=grabarDuplicarCuneus;
                                    document.forms[0].opcion.value="imprimirCuneus";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                                    document.forms[0].submit();
                                }
                            });
            <%} else {%>
                        document.forms[0].idiomaCuneus.value = idiom;
                        document.forms[0].posicionCuneus.value = '<%=m_Conf.getString("Registro.pos.cuneos")%>';
                        document.forms[0].nCopiasCuneus.value = <%=m_Conf.getString("Registro.copias")%>
                        document.forms[0].grabarDuplicar.value=grabarDuplicarCuneus;
                        document.forms[0].opcion.value="imprimirCuneus";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                        document.forms[0].submit();
            <%}%>
         }

    function abrirInforme(nombre,tipoFichero) {
        if ((tipoFichero == null) || (tipoFichero=='')) {
            tipoFichero = 'pdf'
        }
        if (!(nombre =='')) {
            var source = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre="+nombre+"&tipoFichero="+tipoFichero;

            if (tipoFichero == 'html') {
                // Es imprescindible abrir en una ventana nueva sin frames para poder imprimir bien el cuño en la Epson TM-295
                ventanaInforme = window.open(source,'ventana','width=280px,height=170px,status='+ '<%=statusBar%>' + ',toolbar=no, location=no,resizable=yes');
                } else {
                ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source="+source,'ventana','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no, location=no,resizable=yes');
                }
                if (document.forms[0].grabarDuplicar.value=='si') {
                    duplicar();
                }
            } else {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjNoPDF")%>');
        }
    }


    /* Especifica las acciones a llevar a cabo al seleccionar un tipo de asunto del combo */
    function onChangeComboAsuntos(origen) {

        if(top.menu.modificando == 'S'){
            if ((comboAsuntos.selectedIndex != indice_asunto_anterior  && (top.menu.modificando == 'S')) ||
                         (origen=="arbolClasif")){

              // Si no hay terceros ni documentos no hace falta confirmacion
              if (terceros.length == 0 && listaDoc.length == 0) {
                 confirmarCargarAsunto('confirmado');
              } else {


                 document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                 document.forms[0].uniRegAsunto.value=uni_asuntos[comboAsuntos.selectedIndex - 1];
                 document.forms[0].opcion.value="comprobarCargarAsunto";
                 document.forms[0].target="oculto";
                 document.forms[0].submit();
              }
            }
        }
        else
         
           habilitarDivMsgAsuntoBaja(true);
    }

    /* Pide confirmacion para cargar un asunto segun el resultado de la comprobacion. */
    function confirmarCargarAsunto(mensaje) {
    changeNotifs=true;
          // Seleccionar el mensaje adecuado
          var alerta;
          if (mensaje == 'rolesYDocs') {
            alerta = "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjConfRolesYDocs"))%>";
          } else if (mensaje == 'roles') {
             if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
                alerta = "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjConfRoles"))%>";
            }else mensaje="confirmado";
          } else {
            alerta = "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjConfDocs"))%>";
          }

          // Confirmacion
          if (mensaje == 'confirmado' || jsp_alerta('',alerta))
          {
            if (comboAsuntos.selectedIndex > 0) {
                    pleaseWait('on');
                    indice_asunto_anterior = comboAsuntos.selectedIndex;
                    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                    document.forms[0].uniRegAsunto.value=uni_asuntos[comboAsuntos.selectedIndex - 1];
                    document.forms[0].opcion.value="cargarAsunto";
                    document.forms[0].target="oculto";
                    document.forms[0].submit();

            } else {
                // Si se deselecciona el asunto hay que recargar roles, ademas se pierde el procedimiento
                if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
                    // Si esta activo el plugin de flexia, se borra el procedimiento y el exp.relacionado
                    document.forms[0].cod_procedimiento.value='';
                    document.forms[0].desc_procedimiento.value='';
                    document.forms[0].txtExp1.value = '';
                }
                pleaseWait('on'); 
                indice_asunto_anterior = 0;
                document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                document.forms[0].opcion.value="cargarAsunto";
                document.forms[0].target="oculto";
                document.forms[0].submit();
            }
          } else {
                comboAsuntos.selectItem(indice_asunto_anterior);
          }
    }

function confirmarCargarProcedimiento(mensaje) {
    // Seleccionar el mensaje adecuado
    var alerta;
	var modifDocs=0;
    if (mensaje == 'rolesYDocs') {
      alerta = "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjConfRolesYDocs"))%>";
	  modifDocs=1;
    } else if (mensaje == 'roles') {
      alerta = "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjConfRoles"))%>";
    } else {
      alerta = "<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjConfDocs"))%>";
	  modifDocs=1;
    }

    // Confirmacion
    if (mensaje == 'confirmado' || jsp_alerta('',alerta))
    {
        var cod = document.forms[0].cod_procedimiento.value;
        // Buscar el municipio del procedimiento
        for(i=0; i<cod_procedimientos.length; i++) {
            if (cod == cod_procedimientos[i])
                document.forms[0].mun_procedimiento.value = mun_procedimientos[i];
        }

		//Borrar el array de los documentos
	limpiarListaDocumentosSeleccionados();
        // Borrar expediente relacionado
		if (document.forms[0].expRel.value!='1'){
        document.forms[0].txtExp1.value = '';
		}
		else document.forms[0].expRel.value = '0';
        cod_procedimiento_anterior = cod;
        document.forms[0].opcion.value="cargarProcedimiento";
        document.forms[0].target="oculto";
        document.forms[0].submit();

    } else {
        // El usuario no cambia el procedimiento, volvemos al anterior
        document.forms[0].cod_procedimiento.value = cod_procedimiento_anterior;
        onchangeCod_procedimiento();
    }
}
function pulsarRelaciones() {
    var args = new Array();
    args[0]=top.menu.modificando;
    args[1]=relaciones;
    args[2]=window.self;
    args[3]=document.forms[0].ano.value;
    args[4]=document.forms[0].numero.value;
    args[5]=tipoActual;
    // En alta desde tramitar se desactiva la navegacion entre asientos
  <% if (altaDesdeTramitar) { %>
    args[6]=true;
  <% } else { %>
    args[6]=false;
  <% } %>
      
  <% if (("NO".equals(permitir_contestar))&&(aplActual!=apl)) { %>
    args[7]=false;
  <% } else { %>
    args[7]=true;
  <% } %>
<% if ("SI".equals(directiva_salidas_uor_usuario)) { %>
    args[8]=true;
  <% } else { %>
    args[8]=false;
  <% } %>

    relacionesAntiguas = relaciones;
    var source = "<html:rewrite page='/jsp/registro/entrada/listadoRelaciones.jsp?dummy='/>";
    abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source,args,
           'width=700,height=420',function(rel){	
                relaciones=rel;
                // Para el caso de que el usuario cierre la ventana con la X y se pierda la lista
                if (relaciones == null || relaciones == undefined) relaciones = relacionesAntiguas;
                crearListasRelacionesTxt();
            });
}

function pulsarActuaciones() {
    var busqueda;
    <% if (!contestacion && !hayBuscada && !asientoRelacionado && !altaDesdeTramitar && !("reservas".equals(respOpcion))) {%>
        busqueda = true;
    <% } else { %>
        busqueda = false;
    <% } %>
    temasAntiguos  = lista;
    codActAntiguo = document.forms[0].cod_actuacion.value;

    var l = new Array();
    for (i=0; i < lista.length; i++) l[i]=lista[i][0]+'§¥'; // Solo códigos

    var source = "<html:rewrite page='/MantAnotacionRegistro.do'/>" + "?opcion=cargarTemas&busqueda=" + busqueda + "&modificando=" +
            top.menu.modificando + "&fechaAnotacion=" + document.forms[0].fechaAnotacion.value +
            "&codActuacion=" + document.forms[0].cod_actuacion.value + "&listaTemas=" + l;
            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp?source='/>" + source,"ventana1",
	'width=900,height=500,status='+ '<%=statusBar%>',function(res){	
                        resultado=getArrayTemas(res);
                        if (resultado != null && resultado != undefined) {
                            document.forms[0].cod_actuacion.value = resultado[0];
                            lista = resultado[1];
                        } else {
                            document.forms[0].cod_actuacion.value = codActAntiguo;
                            lista = temasAntiguos;
                        }
                        compruebaModificadoRegistro();
                    });
}

function cargarDatosAnotacion() {
    <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) || contestacion) {%>
    var libroBusqueda = "E";
    <% } else { %>
    var libroBusqueda = "S";
    <% } %>
    if (document.forms[0].anoCarga.value == null || document.forms[0].anoCarga.value.length == 0 ||
            document.forms[0].numeroCarga.value == null || document.forms[0].numeroCarga.value.length == 0) {
        jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_introNumAnot"))%>');
    } else {
        pleaseWait('on');
        var params = new Array();
        params['anoCarga'] = '' + document.forms[0].anoCarga.value;
        params['numeroCarga'] = '' + document.forms[0].numeroCarga.value;
        params['libroRegistro'] = libroBusqueda;
        var bindArgs = {
            url: '<c:url value="/MantAnotacionRegistro.do"/>?opcion=cargaDatosBusqueda',
            error: function(type, data, evt) {
                jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_errorNoControl"))%>');
            },
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);
        dojo.event.connect(req, "load", this, "realizaCargaDatos");
    }
}

function realizaCargaDatos(type, data, evt) {
    pleaseWait('off');
    if (data) {
        if (data.codigoError) {
            if (data.codigoError == '0') jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_errorAnotNoExist"))%>');
            else if (data.codigoError == '1') jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_errorAnotAnul"))%>');
            else if (data.codigoError == '2') jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_errorAnotResv"))%>');
            document.forms[0].numeroCarga.select();
            return;
        }
        var datosGenerales = data.datosGenerales;
        var i = 0;
        document.forms[0].cod_uor.value = datosGenerales[i++];
        cambiaUnidadOrganica();
        document.forms[0].asunto.value = unescape(datosGenerales[i++]);
        document.forms[0].cbTipoEntrada.value = datosGenerales[i++];
        actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
        mostrarDestino();
        document.forms[0].txtCodigoDocumento.value = datosGenerales[i++];
        actualizarDescripcion('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentosAux,desc_tiposDocumentosAux);
        document.forms[0].cod_tipoRemitente.value = datosGenerales[i++];
        actualizarDescripcion('cod_tipoRemitente','txtNomeTipoRemitente',cod_tiposRemitentes,desc_tiposRemitentes);
        document.forms[0].codTerc.value = datosGenerales[i++];
        document.forms[0].codDomTerc.value = datosGenerales[i++];
        document.forms[0].numModifTerc.value = datosGenerales[i++];
        document.forms[0].txtNumTransp.value = datosGenerales[i++];
        document.forms[0].cod_tipoTransporte.value = datosGenerales[i++];
        actualizarDescripcion('cod_tipoTransporte','desc_tipoTransporte',cod_tiposTransportes,desc_tiposTransportes);
        document.forms[0].cod_actuacion.value = datosGenerales[i++];
        document.forms[0].autoridad.value = unescape(datosGenerales[i++]);
        document.forms[0].cod_procedimiento.value = datosGenerales[i++];
        document.forms[0].desc_procedimiento.value = datosGenerales[i++];
        document.forms[0].mun_procedimiento.value = datosGenerales[i++];
        document.forms[0].observaciones.value = datosGenerales[i++];
        document.forms[0].txtExp1.value = datosGenerales[i++];

        var datosTemas = data.datosTemas;
        lista = new Array();
        for (i = 0; i < datosTemas.length; i = i + 2) {
            lista[i/2] = [datosTemas[i], datosTemas[i + 1]];
        }

        var datosRelaciones = data.datosRelaciones;
        relaciones = new Array();
        relaciones[0] = [datosRelaciones[0], datosRelaciones[1], datosRelaciones[2]];
        var datosDocs = data.datosDocs;
        listaDoc = new Array();
		if(mostrarDigitalizar){
	        for (i = 0; i < datosDocs.length; i = i + 8) {
	           listaDoc[i/8] = [datosDocs[i], datosDocs[i + 1], datosDocs[i + 2], datosDocs[i + 3], datosDocs[i + 4], datosDocs[i + 5], datosDocs[i + 6], datosDocs[i+7]];
	        }
		} else {
		    for (i = 0; i < datosDocs.length; i = i + 7) {
	           listaDoc[i/7] = [datosDocs[i], datosDocs[i + 1], datosDocs[i + 2], datosDocs[i + 3], datosDocs[i + 4], datosDocs[i + 5], datosDocs[i + 6]];
	        }
		}
        tabDoc.lineas=listaDoc;
        refrescaDoc();
        
         var datosAnts=data.datosAnts;	
        listaAnt=new Array();	
        for(i=0; i<datosAnts.length; i=i+4){	
            listaAnt[i/4]=[datosAnts[i], datosAnts[i + 1], datosAnts[i + 2], datosAnts[i + 3]];	
        }	
        tabAnt.lineas=listaAnt;	
        tabAnt.displayTabla();
		agregarLinkDocumentosAportados();

        var datosInteresados = data.datosInteresados;
        terceros = new Array();
        terceros = transformarJSONArrayTerceros(datosInteresados);
        crearListas();
        terceroCargado = document.forms[0].codTerc.value;
        mostrarDatosTercero();

        compruebaModificadoRegistro();
    }
}

function consultarExpRelacionado() { 
   var ALMOHADILLA = "#";
	if((document.forms[0].txtExp1.value == null || document.forms[0].txtExp1.value.length == 0 &&
            (document.forms[0].descListaExpedientesRelacionados.value == null || 
            document.forms[0].descListaExpedientesRelacionados.value.length==0))){
		jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjExpRelNotFound"))%>');

	}else{
		var ajax = getXMLHttpRequest();
		if(ajax!=null){
			var url = "<%= request.getContextPath() %>" + "/ValidarExistenciaExpRelacionadoRegistro.do";

			var numero
                        if(document.forms[0].txtExp1.value != null && document.forms[0].txtExp1.value.length > 0){
                            numero = document.forms[0].txtExp1.value;
                        }else{
                            numero = document.forms[0].descListaExpedientesRelacionados.value;
                        }
			var expediente = numero.split("/");
			var ejercicio = expediente[0];
			var codProcedimiento = expediente[1];
			var modoConsulta = "si";
			var desdeAltaRE = "si";
			var opcion ="cargar";
                              
			var parametros = "&opcion=" + opcion + "&codExp="+ numero +  "&modoConsulta=" + modoConsulta + "&desdeAltaRE=" + desdeAltaRE +  "&ejercicio=" + ejercicio + "&numero=" + numero + "&codProcedimiento=" + codProcedimiento +  "&desdeConsulta=si";
			ajax.open("POST",url,false);
			ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			ajax.send(parametros);

			var res = ajax.responseText;

			if (res!=null && res.length>0 && ajax.readyState==4)
				{
					if (ajax.status==200){
						var res = ajax.responseText;
						var salida = res.split(ALMOHADILLA);

						if(salida!=null && salida[0]!=""){
							if(salida[0].trim()=="permiso" && salida[1].trim()=="noexiste"){
								jsp_alerta('A','<%=descriptor.getDescripcion("msgExpedienteNoExiste")%>');
							}else
							if(salida[0].trim()=="permiso" && salida[1].trim()=="no"){
								jsp_alerta('A','<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjExpRelNotAccess"))%>');
							}
							else
                            if(salida[0].trim()=="permiso" && salida[1].trim()=="restringido"){
								jsp_alerta('A','<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjProcRelRestringido"))%>');
							}
							else
							if(salida[0].trim()=="destino"  && salida[1].trim()!=""){
                                var source = salida[1].trim();
                                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
                                    'width=992,height=670,toolbar=no,scrollbars=yes,left=150,top=75,status='+ '<%=statusBar%>',function(datosConsulta){});
							}
						}
						else{
							jsp_alerta('A','<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjExpRelNotAccess"))%>');
						}
					 }
				 }

		}
		else{
			jsp_alerta('A','<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjExpRelNotAccess"))%>');
		}
	}
}
  //metodos de la tabla documentos de altaRE
function pulsarCotejarDoc() {
    // Si no se ha seleccionado fila
    if (tabDoc.selectedIndex != -1) {
        var tituloDoc = listaDoc[tabDoc.selectedIndex][1];		

        if (!comprobarSiJustificanteCSV(tituloDoc)) {
            var entregado = listaDoc[tabDoc.selectedIndex][0];		
            var codigo = tabDoc.selectedIndex;
            var tipo = listaDoc[tabDoc.selectedIndex][2];
            var fecha = listaDoc[tabDoc.selectedIndex][3];

            // Si no tiene documento (no tiene documento == no tiene tipo)
            if (tipo != '') {
                // Se comprueba que el fichero sea de tipo pdf
                if (tipo === 'application/pdf') {
                    var cotejado = listaDoc[tabDoc.selectedIndex][4];

                    if (cotejado === 'SI') { // Tiene copia cotejada
                        source = '<html:rewrite page='/registro/DocumentoRegistro.do?opcion=consultarDocumentoCotejado'/>';
                        source += '&tituloDoc=' + tituloDoc;
                        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1', 'width=500,height=520,status='+ '<%=statusBar%>', function(){});
                    } else if (top.menu.modificando === 'N') { // Si no se está modificando el registro no se debe permitir el cotejo.
                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoHayFicheroCotejado")%>');
                    } else if (jsp_alerta('', '<%=descriptor.getDescripcion("msjPreguntarRealizarCotejo")%>')) {
                        source = '<html:rewrite page='/registro/DocumentoRegistro.do?opcion=documentoCotejar'/>';
                        source += '&tituloDoc=' + tituloDoc;
                        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1', 'width=500,height=520,status='+ '<%=statusBar%>', function(){
                            // Limpiar ficheros temporales de cotejo
                            $.ajax({
                                url: APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do',
                                type: 'POST',
                                async: true,
                                data: { opcion: 'limpiarFicherosTemporalesCotejo' },
                                success: null,
                                error: null
                            });
                        });
                    }
                } else {
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjFormatoNoPDF")%>');
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoHayFichero")%>');
            }
        } else {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoModificarJustificanteCSV")%>');
        }
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function comprobarDocumentoMigrado() {
	var documentoMigrado;
	$.ajax({
		url: APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do',
		type: 'POST',
		async: false,
		data: {
			opcion: 'comprobarDocumentoMigrado',
                        tituloDocumento: listaDoc[tabDoc.selectedIndex][1]
		},
		dataType: 'text',
		success: function(data) {
                     documentoMigrado = data == "1" ? true : false;
		},
		error: null
	});
        return documentoMigrado;
        } 


                function pulsarEliminarDoc() {
                    if(tabDoc.selectedIndex != -1) {
						var tituloDoc = listaDoc[tabDoc.selectedIndex][1];
						var compulsado = listaDoc[tabDoc.selectedIndex][4];
						var continuar = false;
						if(!mostrarDigitalizar || compulsado=="NO"){
							if (!comprobarSiJustificanteCSV(tituloDoc)) {
                                                             if (!comprobarDocumentoMigrado()){
									if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarDocumento")%>')) {
										document.forms[0].codigoDocumento.value=tabDoc.selectedIndex;
										document.forms[0].opcion.value="documentoEliminar";
										document.forms[0].target="oculto";
										document.forms[0].action="<%=request.getContextPath()%>/registro/DocumentoRegistro.do";
										document.forms[0].submit();
									} else {
										tabDoc.selectLinea(tab.selectedIndex);
										tabDoc.selectedIndex = -1;

									}
                                                                        } else {
								jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoBorrarDocMigrado")%>');
                                                                }
							} else {
								jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoModificarJustificanteCSV")%>');
							}
						} else {
                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrEliminarDocDigit")%>');
                        }
					} else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                }

				function pulsarModificarDoc() {
					if(tabDoc.selectedIndex != -1) {
						var entregado = listaDoc[tabDoc.selectedIndex][0];		
						var tituloDoc = listaDoc[tabDoc.selectedIndex][1];
						var compulsado = tabDoc.lineas[tabDoc.selectedIndex][4];
						var tipoDocumental = tabDoc.lineas[tabDoc.selectedIndex][6];
						if(!mostrarDigitalizar || compulsado=="NO"){
							if (!comprobarSiJustificanteCSV(tituloDoc)) {
								var entregado = listaDoc[tabDoc.selectedIndex][0];		
								var codigo = tabDoc.selectedIndex;
								var tipo = listaDoc[tabDoc.selectedIndex][2];
								var fecha = listaDoc[tabDoc.selectedIndex][3];
								if(alta)source = "<html:rewrite page='/registro/DocumentoRegistro.do'/>?opcion=modificar&tituloDoc=" + tituloDoc + "&codigo=" + codigo + "&fecha=" + fecha + "&tipo=" + tipo+ "&alta=" + alta;
								else source = "<html:rewrite page='/registro/DocumentoRegistro.do'/>?opcion=modificar&tituloDoc=" + tituloDoc + "&codigo=" + codigo + "&fecha=" + fecha + "&tipo=" + tipo+ "&entr=" + entregado;
								abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
											'width=550,height=500,status='+ '<%=statusBar%>',function(ventana){
									if(ventana != undefined) {
										documentosModificados=true;
										actualizaDocs(ventana);
									}
								});
							} else {
								jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoModificarJustificanteCSV")%>');
							}
						} else {
							jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrModificarDocDigit")%>');
						}
					} else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
				}

function pulsarAltaDoc() {
    var txtListaDocEntregados=document.forms[0].txtListaDocEntregados.value;
    source = "<html:rewrite page='/registro/DocumentoRegistro.do'/>?opcion=documentoNuevo&alta=" + alta;
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
	'width=550,height=500,status='+ '<%=statusBar%>',function(ventana){
                        if(ventana!=undefined) {
                                documentosModificados=true;
                                actualizaDocs(ventana);
                        }
                    });
}
function pulsarHistoricoAnotacion() {

    var source = "<html:rewrite page='/MantAnotacionRegistro.do'/>?opcion=cargarHistorico";
    abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source,undefined,
	'width=942,height=460',function(){});
}
function onchangeNumExpediente() { 
    if (document.forms[0].txtExp1.value == '' || document.forms[0].txtExp1.value == undefined) {
        return;
    }
	if ((Trim(document.forms[0].cod_uniRegDestinoORD.value) == '')){
		jsp_alerta('A', 'Seleccione antes la unidad de destino/origen');
		document.forms[0].txtExp1.value='';
		return;
	}

	if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
		if (!consultando) {
			// Vamos a obtener el codigo de procedimiento del expediente introducido.
			var numExpediente = document.forms[0].txtExp1.value;
			var index0 = numExpediente.indexOf("/", 0);
			var index1 = numExpediente.indexOf("/", index0 + 1);
			if (index0 == -1 || index1 == -1 || index0 == 0 || index1+1 == numExpediente.length) {
				jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjExpErrorFormat"))%>');
				document.forms[0].txtExp1.value = '';
				return;
			}

			var procSelected = numExpediente.substring(index0+1, index1);
			document.forms[0].cod_procedimiento.value = procSelected;
			onchangeCod_procedimientoExpRel();			
		}
	}//if
	else{
		document.forms[0].cod_procedimiento.value = '';
		document.forms[0].desc_procedimiento.value = '';
	}
}
function onfocusNumExpediente() { 
	if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
		// Comprobamos si hay un proc seleccionado, sino borramos el valor de este campo.
		if (document.forms[0].txtExp1.value != '' && document.forms[0].txtExp1.value != undefined) {
			var numExpediente = document.forms[0].txtExp1.value;
			var index0 = numExpediente.indexOf("/", 0);
			var index1 = numExpediente.indexOf("/", index0 + 1);
			if (index0 == -1 || index1 == -1 || index0 == 0 || index1+1 == numExpediente.length) {
				jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjExpErrorFormat"))%>');
				document.forms[0].txtExp1.value = '';
				return;
			}
			var procSelected = numExpediente.substring(index0+1, index1);
			if (procSelected != document.forms[0].cod_procedimiento.value) {
				document.forms[0].txtExp1.value = '';
			}
		}
		actualizarValDiv('cod_procedimiento','desc_procedimiento');
		ocultarDivNoFocus();
		onFocus_CodProcedimiento();
	}
}

function pulsarRegistrarAltaDesdeTramite() {

   var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);
   if(t == null) {
       alert('Problema tecnico al registrar');
       return;
   }
   else if(t == true) {
       jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
           return;
   }
   if (validarObligatoriosAqui()) {
       if ( (comprobarFecha(document.forms[0].fechaAnotacion,mensajeFechaNoValida)) && (comprobarFecha(document.forms[0].fechaDocumento,mensajeFechaNoValida) ) ){
           if (comparaFecha() ) {
               var l = new Array();
               for (i=0; i < lista.length; i++) {
                       l[i]=lista[i][0]+'§¥'; // Solo códigos
               } 
               documentosModificados=false;
               <% if(altaDesdeTramitar) { %> document.forms[0].txtExp1.value = numExpedienteAux; <% } %>            
               document.forms[0].listaTemas.value=l;
               document.forms[0].opcion.value="registrar_alta_tramitar";
               document.forms[0].target="oculto";
               document.forms[0].pendienteBuzon.value="no";
               document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
               document.forms[0].submit();
           }else{
               jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoGrabarAnot")%>');
           }
       }
   }
}
function pulsarListaTerceros() {
    // Abrimos dialogo
    var source = "<%=request.getContextPath()%>/jsp/registro/listadoInteresados.jsp?opcion=";
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,terceros,
            'width=970,height=460,status='+ '<%=statusBar%>',function(indiceNuevo){
                if (indiceNuevo!=undefined) {
                    mostrarInteresado(indiceNuevo);
                }
            });
}
function pulsarListaDomicilios() {
    // Se cargan los datos del tercero actual para obtener los domicilios
    document.forms[0].opcion.value="buscar_por_id_doms";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].target="oculto";
    document.forms[0].submit();
    pleaseWait('on');
}

function validarNif(campo) {
   var documento = campo.value;
    var LONGITUD = 9;
   // var navigator=navigator.userAgent.toLowerCase().indexOf('chrome') > -1
    // Si se trata de un NIF
    // Primero comprobamos si el NIF esta vacio, en ese caso no permitirá seguir completando el formulario
    //En Chrome no lanza mensaje de alerta ya que sino entra en bucle con el evento onblur
    if(documento==''){
        campo.value="";
        if(!(navigator.userAgent.toLowerCase().indexOf('chrome') > -1)){
         jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
        }
        campo.focus();
        return false;
    }else{    
    // Se comprueba la longitud, si es menor de la esperada, rellenamos con ceros a la izquierda.
        var ultCaracter = documento.substring(documento.length - 1, documento.length);
        if (isNaN(ultCaracter)) while (documento.length < LONGITUD) documento = "0" + documento;
        else while (documento.length < LONGITUD - 1) documento = "0" + documento;

        if (documento.length > LONGITUD) {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
            campo.value = '';
            campo.focus();
            return false;
        }
        if (documento.length == LONGITUD) {
            var numDocumento = documento.substring(0, 8);
            var letDocumento = documento.substring(8, 9).toUpperCase();
        } else {
            var numDocumento = documento;
            var letDocumento = '';
        }
        if (isNaN(numDocumento)) {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
            campo.value = '';
            campo.focus();
            return false;
        }
        var letraCorrecta = getLetraNif(numDocumento);
        if (letDocumento == '') {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letraCorrecta);
            campo.value = numDocumento+letraCorrecta;
            campo.focus();
            return true;
        }
        letDocumento=letDocumento.toUpperCase();
        if (letDocumento != letraCorrecta) {
            var res = jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letraCorrecta + '. <%=descriptor.getDescripcion("msjPregContinuar")%>');
            if (!(res > 0)) {
                campo.value ='';
                campo.focus();
                return false;
        }
    }
    campo.value = numDocumento + letDocumento;
    return true;
    }
}

function pulsarCancelarAlta() {
    alta=false;
    ocultarCapaDatosBusqueda();    
    cambiarEstadoComboAsuntoCodificado(false);
    if (document.forms[0].opcionAltaDesdeConsulta.value=="SI"){ 
        desactivarComboAsuntos();
        document.forms[0].enviarCorreo.disabled= true;
        document.getElementById('checkBoxNotificar').className = "etiquetaDeshabilitada";
        document.forms[0].enviarCorreo.checked = false;
        pulsarCancelarModificar();
        //Cambiamos el valor de mod porque en pulsarCancelarModificar() se puso a 1 y necesitamos que siga siendo undefined
        mod='undefined';
    } else { 
        modificando('N');
        ocultarLista();
        ocultarCalendario();
        borrarPaginaBuscada();
        activaInfSoloConsulta();
        document.forms[0].ano.value = ejercicioActual;
        document.forms[0].codTerc.value="0";
        document.forms[0].codDomTerc.value="0";
        document.forms[0].numModifTerc.value="0";
        terceros=new Array();
        comboRol.addItems([],[]);
        codRolPorDefecto = '';
        descRolPorDefecto = '';
        clean();
        inicializar();
        mostrarDestino();
        document.forms[0].horaMinAnotacion.value="";
        documentosModificados=false;
        if (document.getElementById("capaTiposEstadoAnotacion"))
                document.getElementById("capaTiposEstadoAnotacion").style.visibility='visible';

        // hay que recargar el combo de asunto ya que se cargarán con todos los asuntos de registro existentes, incluidos
        // los que han sido dados de baja
        document.forms[0].target="oculto";
        document.forms[0].action = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=recargarComboAsuntos";
        document.forms[0].submit();

    }

}


function pulsarVerDoc() { 
    if(tabDoc.selectedIndex != -1) {
        // Si no tiene documento no se hace nada (no tiene documento == no tiene tipo)
        if(listaDoc[tabDoc.selectedIndex][2] != ''){            
            var indice = tabDoc.selectedIndex;
            window.open(APP_CONTEXT_PATH + "/VerDocumentoRegistro?nombre=" + tabDoc.selectedIndex +"&opcion=0&codigo=" + indice ,"ventana1","left=10, top=10, width=850, height=800, scrollbars=no, menubar=no, location=no, resizable=yes");
        }
        else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoHayFichero")%>');
    } else jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjNoSelecFila"))%>');
}

function pulsarEliminarAnt(){
        if(tabAnt.selectedIndex !=-1){
            if(jsp_alerta('','<%=descriptor.getDescripcion("msjBorrarDocumento")%>')){
                document.forms[0].codigoDocAnterior.value=tabAnt.selectedIndex;
                document.forms[0].opcion.value="anteriorEliminar";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/registro/EntregadosAnterior.do";
                document.forms[0].submit();
            }else{
                tabAnt.selectLinea(tab.selectedIndex);
                tabAnt.selectedIndex=-1;
            }
       }else{
            jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
       }
      }
      
      function pulsarModificarAnt(){
    if(tabAnt.selectedIndex !=-1){
        var tipoDocAnterior=listaAnt[tabAnt.selectedIndex][0];
        var nombreDocAnterior=listaAnt[tabAnt.selectedIndex][1];
        var organoDocAnterior=listaAnt[tabAnt.selectedIndex][2];
        var fechaDocAnterior=listaAnt[tabAnt.selectedIndex][3];
        var codigo=tabAnt.selectedIndex;
        source = "<html:rewrite page='/registro/EntregadosAnterior.do'/>?opcion=modificar&nombreDocAnterior=" + nombreDocAnterior+"&codigo="+codigo+ "&tipoDocAnterior=" + tipoDocAnterior + "&fechaDocAnterior=" + fechaDocAnterior + "&organoDocAnterior=" + organoDocAnterior;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
	'width=670,height=370,status='+ '<%=statusBar%>',function(ventana){
                        if(ventana != undefined){
                            actualizaAnt(ventana);
                        }
            });
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarAltaAnterior(){
    source="<html:rewrite page='/registro/EntregadosAnterior.do'/>?opcion=altaNueva";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
	'width=680,height=500,status='+ '<%=statusBar%>',function(ventana){
                        if(ventana!=undefined) {
                                actualizaAnt(ventana);
                        }
                    });
    }
    
    
    function mostrarMensajeAltaRegistroDesdeTramitar(datos)
    {
         if (datos!=null && datos!=undefined) {
                            if (datos['resultado'] != 'cancelar') {
                                jsp_alerta("A","<%=descriptor.getDescripcion("msjAltaTramNum")%> " 
                                               + datos['anotacion'] + 
                                               " <%=descriptor.getDescripcion("msjAltaTramDia")%> " 
                                               + datos['dia'] +  
                                               " <%=descriptor.getDescripcion("msjAltaTramHora")%> " 
                                               + datos['hora']);
                            }
                        }
        
    }
      
    
    
    function pulsarDigitalizarDocs(origen){
        if(documentosModificados) jsp_alerta('A', 'Existen documentos sin grabar. Grabe antes de digitalizar otros documentos');
        else
        pulsarDigitalizar(codUsu,origen);
        
}

    function pulsarFinalizar(opcion){
            <%if ("reservas".equals(respOpcion)) {%>
                    document.forms[0].desde_reserva.value = true;
              <%}else{%>
                    document.forms[0].desde_reserva.value = false;
               <%}%>
            
            document.forms[0].finDigitalizacion.value=true;
            comprobarCodProcedimientoValido(opcion);    
            
    }

    function pulsarDigitalizarConAltaPrevia(opcion){
        $("#digitalizarConAltaPrevia").val("si");
        if(!opcion) opcion = "Alta";
        comprobarCodProcedimientoValido(opcion);
    }
        
    function resfrescaTablaDocumentosDigitalizacion(){
 
     var ejercicio=document.forms[0].ano.value;
     var numero=document.forms[0].numero.value;

         $.post('<c:url value="/registro/digitalizacionDocumentosLanbide.do"/>',{'ejercicio':ejercicio,'numero':numero,'opcion':'recuperarDocumentos'},function(ajaxResult){
             
         if(ajaxResult){
                var datos = JSON.parse(ajaxResult);

                var listaDocs = new Array();    
                for(i=0; i<datos.length;i++)
                {
                    var str='';
                    if (datos[i].entregado=='S') str='SI';
                    else if (datos[i].entregado=='N') str='NO';
                    else str='';
                    var fecha='';
                 
                    if(datos[i].fechaDoc===undefined) fecha='';
                    else fecha=datos[i].fechaDoc; 
                    if(datos[i].descripcionTipoDocumental===undefined){
                        descripcionTipoDocumental = '';
                    } else {
                        descripcionTipoDocumental = datos[i].descripcionTipoDocumental;
                    }
                    
                    listaDocs[i]= [str,datos[i].nombreDoc , datos[i].tipoDoc,fecha,datos[i].compulsado,datos[i].doc, descripcionTipoDocumental];        
                }              
                listaDoc = listaDocs;
                tabDoc.lineas=listaDocs;
                refrescaDoc();
                
            } else jsp_alerta("A","Ha ocurrido un error al recuperar los datos que necesita el complemento de digitalización para ejecutarse.")
        });
       
}     

    function irAPestana(idPestana) {
        if (idPestana) {
            $('#' + idPestana).click();
        }
    }
    
    function ocultaDivCombosDesplegables (){
        $("#comboDesplegable").hide();
    }
    
</SCRIPT>
    </head>

    <BODY class="bandaBody" onload="javascript:{ pleaseWait('off');
            
     
        inicializar();
        <% if (hayBuscada || contestacion || altaDesdeTramitar) {%> 
            inicializarRecarga();
        <% } else if (hayModificada) {%>
            determinarEncontrada();

        <% } else if ("reservas".equals(respOpcion)) {%>          
            modificarReserva();

        // Para navegacion entre asientos relacionados
        <% } else if (asientoRelacionado) {
           int numRelacionAnotaciones = ((Integer)session.getAttribute("NumRelacionAnotaciones")).intValue();
           int posAnotacion = 1; %> 
           registroAregistro(<%=numRelacionAnotaciones%>, <%=posAnotacion%>);
        <% } %>
    mostrarFiltrosProcOtraAdmin();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/MantAnotacionRegistro.do" target="_self">

        <!-- código de la UOR -->
        <html:hidden  property="opcion" value=""/>
        <html:hidden  property="codigoDocumento" value=""/> <!-- Utilizado para eliminar el documento -->
         <html:hidden property="codigoDocAnterior" value=""/> <!-- Utilizado para eliminar el documento anterior aportado -->

        <html:hidden  property="tipo_select"   value=""/>
        <html:hidden  property="col_cod"   value=""/>
        <html:hidden  property="col_desc"   value=""/>
        <html:hidden  property="nom_tabla"   value=""/>
        <html:hidden  property="input_cod"   value=""/>
        <html:hidden  property="input_desc"   value=""/>
        <html:hidden  property="column_valor_where" value=""/>
        <html:hidden  property="whereComplejo" value=""/>
        <html:hidden  property="codTerc" value="0"/>
        <html:hidden  property="codDomTerc" value="0"/>
        <html:hidden  property="codRol" value="0"/>
        <html:hidden  property="descRol" value="0"/>
        <html:hidden  property="numModifTerc" value="0"/>
        <html:hidden  property="txtIdTercero" value=""/>
        <html:hidden  property="txtIdDomicilio" value=""/>
        <html:hidden  property="txtVersion" value=""/>
        <input type="hidden" name="situacion"/>
        <html:hidden  property="modificar" value="0"/>
        <html:hidden  property="duplicar" value="1"/>
        <html:hidden  property="hayTexto" value=""/>
        <!-- Unidad de registro del asunto, necesario pq puede ser -1 indicando todos los registros -->
        <html:hidden  property="uniRegAsunto"/>
        <!-- Municipio del procedimiento y codigo que deben usarse para recuperar los roles -->
        <html:hidden property="codProcedimientoRoles" value=""/>
        <html:hidden property="mun_procedimiento" value="0"/>
        <html:hidden property="codUor"/>
        <html:hidden property="codDep"/>
        <html:hidden property="tipoRegistro"/>
        <html:hidden property="anoReg"/>
        <html:hidden property="numeroReg"/>

        <html:hidden  property="listaTemas" value="" />

        <html:hidden property="acceso" value="consultar" />
        <html:hidden property="pendienteBuzon" value="no"/>
        <input type="hidden" name="ventana" value="false">
        <html:hidden property="aplicacion" value=""/>
        <html:hidden property="posicionAnotacion" value=""/>
        <input type="hidden" name="lineasPagina" value="10">
        <input type="hidden" name="pagina" value="1">

        <input type="hidden" name="posicionCuneus" >
        <input type="hidden" name="idiomaCuneus" >
        <input type="hidden" name="nCopiasCuneus" >
        <input type="hidden" name="opcionAltaDesdeConsulta">
        <input type="hidden" name="grabarDuplicar">

        <input type="hidden" name="listaCodTercero" value="">
        <input type="hidden" name="listaVersionTercero" value="">
        <input type="hidden" name="listaCodDomicilio" value="">
        <input type="hidden" name="listaRol" value="">
        <input type="hidden" name="listaDescRol" value="">
        <!--Para plantillas de justificante -->
        <input type="hidden" name="codAplicacion" value="">
        <input type="hidden" name="descripcionJus" value="">
         <input type="hidden" name="editorJustif" value="">


        <!--Para navegacion entre entradas relacionadas -->
        <input type="hidden" name="tipoAsiento" value="">
        <input type="hidden" name="ejercicioAsiento" value="">
        <input type="hidden" name="numeroAsiento" value="">

        <input type="hidden" name="expRel" value="0">
		
	<input type="hidden" name="modoAlta" value="0">
	
	<input type="hidden" name="txtListaDocEntregados" value="">
        
        <!-- #270948: Indica que se ha pulsado Digitalizar desde el formulario de alta -->
        <input type="hidden" id="digitalizarConAltaPrevia" value="">
        <!-- #326290: Se utiliza  para evitar el error de busqueda en bd de una reserva que no existe-->
        
        <input type="hidden" name="desde_reserva">
	
	

        <html:hidden property="txtPart"/>
        <html:hidden property="txtApell1"/>
        <html:hidden property="txtPart2"/>
        <html:hidden property="txtApell2"/>
        <html:hidden property="txtPais"/>
        <html:hidden property="txtPoblacion"/>
        <html:hidden property="tipoRegistroOrigen" value=""/>

        <!-- Para el envio de correo a unidades organicas -->
        <html:hidden property="txtListaUorsCorreo"/>

        <!-- Relaciones entre asientos -->
        <html:hidden property="txtTiposRelaciones" value=""/>
        <html:hidden property="txtEjerciciosRelaciones" value=""/>
        <html:hidden property="txtNumerosRelaciones" value=""/>

        <html:hidden property="cod_actuacion" value=""/>
        <html:hidden property="txtNomeActuacion" value=""/>
        <html:hidden property="horaMinDocumento" value=""/>
        
        <html:hidden property="mostrarGenerarModelo" styleId="mostrarGenerarModelo" value="0"/>
        
        <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) || contestacion) {%>
            <!-- contestacion -->
            <html:hidden property="ejercicioAnotacionContestada" value=""/>
            <html:hidden property="numeroAnotacionContestada" value=""/>
            <html:hidden property="mun_procedimiento" value=""/>
        <% }%>
        
        <input type="hidden" name="fecPresRes" value="">
        
        <%--<input type="hidden" name="valorOpcionPermanencia">--%>
        <html:hidden property="valorOpcionPermanencia" value=""/>
        <html:hidden property="registroTelematico"/>
        <html:hidden property="finDigitalizacion"/>
        
        <!-- #234108 -->
        <input type="hidden" id="asuntoConTipoDocOblig" name="asuntoConTipoDocOblig"/>
        
        <html:hidden property="retramitarDocumentosCambioProcedimiento"/>

<% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
<div class="txttitblancoder">	
    <%=descriptor.getDescripcion(titPag)%>	
    <%if(mostrarTituloOficina){%>	
        <span class="txttitsmallcabecera" title="<%=tituloUor%>"><%=tituloUor%></span>	
    <%}%>	
</div>
<% } else {%>
<div class="txttitblanco">	
    <%=descriptor.getDescripcion(titPag)%>	
    <%if(mostrarTituloOficina){%>	
        <span class="txttitsmallcabecera" title="<%=tituloUor%>"><%=tituloUor%></span>	
    <%}%>	
</div>
<% }%>
<div class="encabezadoGris" id="tablaBuscar">
    <div style="float:left;width: 21%;padding-top:10px" class="etiqueta"><%=descriptor.getDescripcion(numOrden)%></div>
    <div style="float:left;width: 18%" class="columnP">
        <%
            String valorAno = ejercicioActual;
            Object anoSes = session.getAttribute("anoEjercicioUltimaConsulta");
            if (anoSes != null && !"".equals(anoSes.toString().trim())) {
                valorAno = anoSes.toString();
            }
        %>
        <html:text  styleClass="inputTexto" size="5" maxlength="4" property="ano" styleId="anoEjercicio"
                    onkeyup="return SoloDigitosNumericos(this);"
                    onfocus="this.select();" value="<%= valorAno %>" />&nbsp;<span id="separadorAno" style="font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11px; font-style:normal; font-weight: normal; font-variant: normal; color: #999999;">/</span>
        <html:text  styleClass="inputTexto" size="9" maxlength="8" property="numero" styleId="numeroRegistro"
                    onkeyup="return SoloDigitosNumericos(this);"
                    onfocus="this.select();" />
    </div>
    <div id="divDatosRegistroSIR" style="float:left;position:relative;top:-2px;display:none;">
        <span style="padding-top:10px" class="etiqueta"><%=descriptor.getDescripcion("msgIdentificadorSIR")%></span>
        <input type="text"  class='inputTexto inputTextoDeshabilitado' disabled="disabled" name="identificadorRegistroSIR" id="identificadorRegistroSIR"/>
        <input type="hidden"  class='inputTexto inputTextoDeshabilitado' name="identificadorRegistroSIRValue" id="identificadorRegistroSIRValue"/>

    </div>
    <div style="float:left;width:60%;position:relative;top:-2px" id="divTablaBotones">
        <TABLE class="tablaBotones" width="100%" cellpadding="0px" cellspacing="0px" border="0">
            <TR>
                <TD style="width: 4%" valign="middle">	<!-- Diligencia de anulacion -->

                    <DIV id="capaDiligencia" name="capaDiligencia" STYLE="position:relative; width:100%; visibility:hidden;" >
                        <span class="fa fa-pencil-square-o" name="botonDiligencias" id="botonDiligencias" style="cursor:pointer; font-size:20px; color: red"
                              onClick="javascript:pulsarDiligencia();" alt= "<%=descriptor.getDescripcion("altAnotDil")%>" title= "<%=descriptor.getDescripcion("altAnotDil")%>"></span>
                    </DIV>
                    <div id="diligencia" style="position:absolute; z-index:10; visibility: hidden;font-size: 10px; font-family: Verdana, Arial, Helvetica, Sans-serif; font-style: normal; background-color: #FFFFFF; border-top: #666666 1px solid; border-bottom: #666666 1px solid; border-left: #666666 1px solid; border-right: #666666 1px solid;"></div>
                </TD>
                <TD style="width: 10%" valign="middle">
                    <DIV id="capaEstado" name="capaEstado" STYLE="position:relative; width:100%; visibility:hidden;">
                        <TABLE width="100%" cellpadding="0px" cellspacing="0px" border="0">
                            <TR>
                                <TD style="width: 20%" valign='middle'> 
                                    <span class="fa-fa-pencil-square-o" name="botonDiligenciasAnulacion" id="botonDiligenciasAnulacion" 
                                          style="cursor:hand;text-decoration:line-through;" onClick="javascript:pulsarDiligenciasAnulacion();" 
                                          alt="<%=descriptor.getDescripcion("altDiligAnul")%>" 
                                          title="<%=descriptor.getDescripcion("altDiligAnul")%>"></span>
                                </TD>
                                <TD valign='middle' style="width: 80%; color:red; font-size:10px;">&nbsp;<%=descriptor.getDescripcion("etiq_anulada")%> </TD>
                            </TR>
                        </TABLE>
                    </DIV>
                    <div id="capaDiligenciasAnulacion" style="position:absolute; z-index:10; visibility: hidden;font-size: 10px; font-family: Verdana, Arial, Helvetica, Sans-serif; font-style: normal; color: #006500; background-color: #FFFFFF; border-top: #666666 1px solid; border-bottom: #666666 1px solid; border-left: #666666 1px solid;border-right: #666666 1px solid; overflow-x:no; overflow-y:yes"></div>
                </TD>
                <TD style="width: 4%" valign="middle"><!-- Desanulacion -->
                    <DIV id="capaDesAnular" class="etiqueta" name="capaDesAnular" STYLE="position:relative; width:100%; visibility:hidden;">
                        <% if (permisoMantenimiento) {%>
                        <span class="fa fa-undo" name="botonRecuperarAnulada" style="cursor:hand;" 
                              onClick="javascript:pulsarRecuperarAnulada();" alt= "<%=descriptor.getDescripcion("altDesAnular")%>" 
                              title= "<%=descriptor.getDescripcion("altDesAnular")%>"></span>
                        <% }%>
                    </DIV>
                </TD>
				<td id="FinDigitalizacionEtiqueta"> </td>
                <TD style="width: 50%" valign="middle">
                    <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) || contestacion) {%>
                    <div id="capaAnotacionContestada" style="position:relative; visibility: hidden; width:100%;"></div>
                    <% }%>
                </TD>
                <TD style="height:20px;vertical-align:top"><!-- Cuño -->
                    <div id="capaCunho" name="capaCunho" STYLE="position:relative;width:100%; visibility:hidden;height:0px;cursor:pointer">
                        <a class="botonjustificante" title='<%=descriptor.getDescripcion("altHistorico")%>' alt='<%=descriptor.getDescripcion("altHistorico")%>' href="" onClick="javascript:{pulsarHistoricoAnotacion();return false;}"><%=descriptor.getDescripcion("etiqHistorico")%></a>
                        
                         <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) || contestacion) {%>
                         <a class="botonjustificante" title='<%=descriptor.getDescripcion("altJustificSalida")%>' alt='<%=descriptor.getDescripcion("altJustificSalida")%>' href="" onClick="javascript:{pulsarJustificanteEntrada();return false;}">'<%=descriptor.getDescripcion("etiq_justif")%>'</a>
                          <a class="botonjustificante" id="filaModelo"  title='<%=descriptor.getDescripcion("altModPetRpta")%>' alt='<%=descriptor.getDescripcion("altModPetRpta")%>' href="" onClick="javascript:{pulsarImprimirModelo('peticion_anotacion');return false;}">'Pet. Resp.'</a>
                         <% }else{%>
                         <a class="botonjustificante" title='<%=descriptor.getDescripcion("altJustificEntrada")%>' alt='<%=descriptor.getDescripcion("altJustificEntrada")%>' href="" onClick="javascript:{pulsarJustificanteEntrada();return false;}">'<%=descriptor.getDescripcion("etiq_justif")%>'</a>
                         <% }%>
                        <span class="fa fa-registered botonjustificante" name="cmdCuneus" id="cmdCuneus" onClick="javascript:{pulsarImprimirCuneus('no');return false;}" alt="<%=descriptor.getDescripcion("altSello")%>" title="<%=descriptor.getDescripcion("altSello")%>"></span>
                    </div>
                    <div id="cargaDatos" style="position:relative;float:left;clear:both;visibility:hidden;height:0px;cursor:pointer">
                        <a class="etiqueta" title='' alt='' href="" onClick="javascript:{mostrarOcultarCamposBusqueda(); return false;}" style="height: 20px; margin-top: 9px">
                            <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) || contestacion) {%>
                            <%=descriptor.getDescripcion("etq_cargaDatosEnt")%>:
                            <% } else { %>
                            <%=descriptor.getDescripcion("etq_cargaDatosSal")%>:
                            <% }%>
                        </a>
                        <div id="camposDatos" style="visibility: hidden;float:left;clear:both;z-index: 40">
                            <html:text  styleClass='inputTexto' size="4" maxlength="4" property="anoCarga"
                                    onkeyup = "return SoloDigitosNumericos(this);"
                                    onfocus="this.select();"/>&nbsp;<span style="font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11px; font-style:normal; font-weight: normal; font-variant: normal; color: #999999;">/</span>
                            <html:text  styleClass='inputTexto' size="8" maxlength="8" property="numeroCarga"
                                    onkeyup = "return SoloDigitosNumericos(this);"
                                    onfocus="this.select();" />
                            <span class="fa fa-search" name="botonRecuperarAnulada" style="cursor:hand"
                                  onClick="javascript:cargarDatosAnotacion();" alt= "" title= ""></span>
                        </div>
                    </div>
                </TD>
            </TR>
        </TABLE>
    </div>
</div>
<div class="contenidoPantalla">
<!-- ------------------------------------------------------------------ -->
<!--                            PESTANAS                                        -->
<!-- ------------------------------------------------------------------ -->
            <div class="tab-pane" id="tab-pane-1" >
                <script type="text/javascript">
                    tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
                </script>
                <!-- CAPA 1: DATOS GENERALES ------------------------------ -->
                <div class="tab-page" id="tabPage1">
                    <h2 class="tab" id="pestana1"><%=descriptor.getDescripcion("res_pestana1")%></h2>
                    <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
                    <TABLE id ="tablaDatosGral" class="contenidoPestanha">
                        <TR>
                            <TD colspan="2">
                                <span class="etiqueta"><%=descriptor.getDescripcion("etiq_fecGrab")%>: </span>
                                <span class="columnP">
                                   <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="7" property="fechaDocumento"
                                               onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                               onblur = "javascript:return comprobarFecha(this,mensajeFechaNoValida);"
                                               onfocus = "this.select();"/>
                                </span>
                                <span class="etiqueta" style="margin-left:0.3%"><%=descriptor.getDescripcion("res_fecPres")%>: </span>
                                <span class="columnP">
                                     <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="7" property="fechaAnotacion"
                                               onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                               onblur = "javascript:return comprobarFecha(this,mensajeFechaNoValida);"
                                               onfocus = "javascript: this.select();" />
                                    <A href="javascript:if(!BLOQUEO_FECHA_HORA_ANOTACION) calClick();return false;" onClick="if(!BLOQUEO_FECHA_HORA_ANOTACION) mostrarCalFechaAnotacion(event);return false;" onblur="if(!BLOQUEO_FECHA_HORA_ANOTACION) ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                                        <span class="fa fa-calendar" aria-hidden="true" style="border: 0px solid none" id="calFechaAnotacion" name="calFechaAnotacion" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
                                    </A>
                                </span>
                                <span class="etiqueta" style="margin-left:0.3%"><%=descriptor.getDescripcion("res_HoraPres")%>:</span>
                                <span class="columnP">
                                    <html:text styleClass="inputTxtFecha" size="6" maxlength="5" property="horaMinAnotacion"
                                               onkeyup = "return SoloCaracteresHora(this);"
                                               onblur = "javascript:return comprobarHora(this, mensajeHora);"
                                               onfocus = "javascript: this.select();" />
                                </span>
                                 <span class="etiqueta" style="margin-left:0.3%;"><%=descriptor.getDescripcion("etiq_fecDoc")%>: </span>
                                <span class="columnP">
                                    <html:text styleClass="inputTxtFecha" size="7" maxlength="10" property="fechaDoc"
                                               onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                               onblur = "javascript:return comprobarFecha(this,mensajeFechaNoValida);"
                                               onfocus = "this.select();" />
                                   <A href="javascript:calClick(event);return false;" onClick="mostrarCalDocumento(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                                        <span class="fa fa-calendar" aria-hidden="true" style="border: 0px solid none"id="calDocumento" name="calDocumento" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
                                    </A>
                                </span>
                            </TD>
                        </TR>
                        <!-- FIN Unidad Organica -->
                        <TR>
                            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("gEtiqIntDom")%></TD>
                        </TR>
                        <!--  Datos interesado -->
                        <TR>
                            <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqIdentificacion")%>:</TD>
                            <TD style="width: 90%" class="columnP">
                                 <html:text styleId="obligatorio" property="cbTipoDoc" styleClass="inputTextoObligatorio" style="width:5%"
                                            onfocus="javascript:this.select();  analizarDocumento();"
                                            onchange="javascript:{onchangeCbTipoDoc();} analizarDocumento();"
                                            onblur="javascript:onblurTipoDocumento('cbTipoDoc','descTipoDoc');  analizarDocumento();"/>
                                 <html:text styleClass="inputTextoObligatorio" styleId="obligatorio" property="descTipoDoc" style="width:20%" readonly="true"
                                            onfocus="javascript:{onFocusDescTipoDoc();}"
                                            onclick="javascript:{onClickDescTipoDoc();}"
                                            onblur="javascript:{onblurTipoDocumento('cbTipoDoc','descTipoDoc');}"/>
                                 <A href="javascript:{onClickDescTipoDoc();}" style="text-decoration:none;" id="anclaD" name="anchorTipoDoc"
                                    onclick="javascript:this.focus();">
                                     <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonTipoDoc" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                 </A>
                                 <html:text styleClass="inputTextoObligatorio" style="width:15%" maxlength="16" property="txtDNI" styleId="obligatorio" onchange="javascript:buscarDocTipoDoc();" onkeyup="return xAMayusculas(this);"/>
                               <div style="display:inline">
                                     <!-- Separador --> <span style="display:inline">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                     <span id="botonSeleccionTerceros" style="display:inline;visibility:hidden">
                                         <span class="fa fa-users" style="cursor:pointer" aria-hidden="true" name="botonTer" alt="<%=descriptor.getDescripcion("titGestionTerceros")%>" title="<%=descriptor.getDescripcion("titGestionTerceros")%>"
                                               onclick="javascript:pulsarBuscarTerceros();"></span>
                                     </span>
                                     <!-- Separador --> <span style="display:inline">&nbsp;</span>
                                     <span id="flechaMenos" style="display:inline;visibility:hidden">&nbsp;
                                         <span class="fa fa-arrow-circle-o-left" aria-hidden="true" onclick="javascript:pulsarAnteriorTercero();" id="flechaAnterior" name="flechaAnterior" style="cursor:hand" alt="<%=descriptor.getDescripcion("tipIntAnterior")%>" title="<%=descriptor.getDescripcion("tipIntAnterior")%>"></span>&nbsp
                                     </span>
                                     <span id="ordenTercero" class="etiqueta" style="display:inline; vertical-align:middle;visibility:hidden">&nbsp;</span>
                                     <span id="flechaMas" style="vertical-align:bottom;display:inline;visibility:hidden">&nbsp;
                                         <span class="fa fa-arrow-circle-o-right" aria-hidden="true" onclick="javascript:pulsarSiguienteTercero();" id="flechaSiguiente" name="flechaSiguiente" style="cursor:hand" alt="<%=descriptor.getDescripcion("tipIntSiguiente")%>" title="<%=descriptor.getDescripcion("tipIntSiguiente")%>"></span>
                                     </span>
                                     <!-- Separador --> <span style="display:inline">&nbsp;</span>
                                     <span id="botonEliminarTerceros" style="display:inline;visibility:hidden">
                                         <span class="fa fa-trash" style="cursor:pointer" name="botonDelTer" alt="<%=descriptor.getDescripcion("tipElimInter")%>" title="<%=descriptor.getDescripcion("tipElimInter")%>"
                                               onclick="javascript:pulsarEliminarTercero('<%=descriptor.getDescripcion("msjBorrarInt")%>');"></span>
                                     </span>
                               </div>
                               <div style="float:right; display:inline">
                                     <span id="botonListaTerceros" style="display:inline;visibility:hidden">
                                         <span class="fa fa-list" style="cursor:pointer" id="botonListTer" name="botonListTer" alt="<%=descriptor.getDescripcion("tipListaInter")%>" title="<%=descriptor.getDescripcion("tipListaInter")%>"
                                               onclick="javascript:pulsarListaTerceros();"></span>
                                     </span>
                                     <!-- Separador --> <span style="display:inline">&nbsp;</span>
                                     <span id="botonListaDomicilios" style="display:inline;visibility:hidden">
                                         <span class="fa fa-map-marker " style="cursor:pointer" name="botonListDom" alt="<%=descriptor.getDescripcion("tipListaDomsInter")%>" 
                                               title="<%=descriptor.getDescripcion("tipListaDomsInter")%>"
                                               onclick="javascript:pulsarListaDomicilios();"></span>
                                     </span>
                               </div>
                             </TD>
                         </TR>
                        <TR>
                            <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombr")%>/<BR><%=descriptor.getDescripcion("gEtiqRazonSoc")%>:&nbsp;</TD>
                            <TD style="width: 90%">
                                <html:text styleClass="inputTexto" style="width:70%;" maxlength="120" property="txtInteresado" readonly="false" tabindex="-1" onkeyup="return xAMayusculas(this);"/>
                                <span style="margin-left: 1%" class="etiqueta" ><%=descriptor.getDescripcion("etiqRol")%>:</span>
                                <input type="hidden" class="inputTextoObligatorio" id="codRolTercero" name="codRolTercero">
                                <input type="text" class="inputTextoObligatorio" id="descRolTercero" name="descRolTercero" style="width: 15%" readOnly="true">&nbsp;
                                <a href="" id="anchorRolTercero" name="anchorRolTercero">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonRol" name="botonRol" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor: pointer;"></span>
                                </a>
                            </td>
                        </TR>
                        <TR>
                            <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDomicilio")%>:&nbsp;</TD>
                              <TD style="width: 90%">
                                <span class="columnP">
                                    <html:text  styleClass="inputTexto" property="txtDomicilio" style="width:70%" maxlength="60" readonly="true" tabindex="-1"/>
                                </span>
                               
                                <span style="margin-left:1%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</span>
                                <span class="columnP" >
                                    <html:text  styleClass="inputTexto" style="width:15%;" maxlength="5" property="txtCP" readonly="true" tabindex="-1"/>
                                </span>
                            </td>

                            <TD style="width: 90%" class="columnP">
                               
                            </TD>
                        </TR>
                        <TR id="capaInfoAuxInteresado" name="capaInfoAuxInteresado">
                            <TD style="width: 10%" class="etiqueta">
                                <%=descriptor.getDescripcion("gEtiqProvincia")%>:
                            </TD>
                            <TD style="width: 90%">
                                <span class="columnP">
                                    <html:text property="txtProv"  styleClass="inputTexto" styleId="txtProv" style="width:22%" readonly="true" tabindex="-1"/> 
                                </span>
                                <span style="margin-left:1%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</span>
                                <span class="columnP">
                                    <html:text property="txtMuni"  styleClass="inputTexto" styleId="txtMuni" style="width:30%;" readonly="true" tabindex="-1"/>
                                </span>
                                <span style="margin-left:1%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:</span>
                                <span class="columnP" >
                                    <html:text  property="txtTelefono" styleClass="inputTexto" style="width:8%" maxlength="20" readonly="true" tabindex="-1"/>
                                </span>
                                <span class="etiqueta" style="margin-left:1%" align="left"><%=descriptor.getDescripcion("gEtiqEmail")%>:</span>
                                <span class="columnP">
                                    <html:text  property="txtCorreo" styleClass="inputTexto" style="width:15%; text-transform: none" maxlength="30" readonly="true" tabindex="-1"/>
                                </span>
                            </td>
                        </TR>
 
                        <TR>
                            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("gEtiqDatosDocumento")%></TD>
                        </TR>
                        <TR>
                            <TD colspan="2" >
                                <table style="width: 100%">
                                    <tr>
                                        <TD style="width: 50%">
                                            <div style="width: 20%;float:left" class="etiqueta"><%=descriptor.getDescripcion(tipo)%>:</div>
                                            <div style="width: 80%;float:left" class="columnP">
                                                <%String txtEventOnbluCbTipoEntrada=(("S"==tipoAnotacion || "Relacion_S"==tipoAnotacion)?"javascript:{mostrarDestino();getSetUnidadOrigenDIR3SalidaSIR();}":"javascript:{mostrarDestino();getSetCodigoAsuntoEntradaSIR()}");%>
                                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="cbTipoEntrada"  style="width:18%" maxlength="1" onkeyup="return SoloDigitosNumericos(this);"
                                                           onfocus="javascript:this.select();mostrarDestino();"
                                                           onchange="javascript:{divSegundoPlano=true;inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);mostrarDestino();mostrarFiltrosProcOtraAdmin();}"
                                                           onblur="<%=txtEventOnbluCbTipoEntrada%>"/>
                                                <%if(userAgent.indexOf("MSIE")!=-1) {%> <!--IE9, IE11 no contiene MSIE, se identifica como Mozilla-->
                                                <html:text styleClass="inputTextoObligatorio" styleId="obligatorio"  property="txtNomeTipoEntrada" style="width:71%" readonly="true"
                                                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}"
                                                           onclick="{divSegundoPlano=false; inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}"
                                                           onblur="javascript:{mostrarDestino();pasarFocoACod();}"/>
                                                <% } else { %>
                                                <html:text styleClass="inputTextoObligatorio" styleId="obligatorio"  property="txtNomeTipoEntrada" style="width:71%" readonly="true"
                                                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}"
                                                           onclick="{divSegundoPlano=false; inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}"
                                                           onblur="javascript:{mostrarDestino();}"/>
                                                <% } %>
                                                <A href="javascript:{divSegundoPlano=false;inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}" style="text-decoration:none;" id="anclaD" name="anchorTipoEntrada" onclick="javascript:pasarFocoADesc();">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonTipoEntrada" alt= "<%=descriptor.getDescripcion("altDesplegable")%>" title= "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                                </A>
                                            </div>
                                        </TD>
                                        <TD>
                                            <div style="width: 20%;float:left" id="etiquetaTipoDoc" class="etiqueta"><%=descriptor.getDescripcion("res_tipoDoc")%>:</div>
                                            <div style="width: 22%;float:right">
                                                <div id="capaTipoDocNoexiste" class="etiqueta" name="capaTipoDocNoexiste" style="visibility:hidden">
                                                    <i class="fa fa-exclamation-triangle" aria-hidden="true" id="descDocNoVal" name="descDocNoVal" title= "" border="0" style="cursor:hand" ></i>
                                                </div>
                                            </div>
                                            <div style="width: 58%;float:left" class="columnP">
                                                <html:text styleClass="inputTexto" property="txtCodigoDocumento" style="width:27%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();"
                                                           onchange="javascript:onchangeCampoCodigo('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentosAux,desc_tiposDocumentosAux,operadorConsulta);"/>
                                                <html:text styleClass="inputTexto"   property="txtNomeDocumento" style="width:61%" readonly="true"
                                                           onfocus="javascript:onFocusCampoDesc('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentosAux,desc_tiposDocumentosAux,operadorConsulta);"
                                                           onclick="javascript:onClickCampoDesc('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentosAux,desc_tiposDocumentosAux,operadorConsulta);"/>
                                                <A href="javascript:{onClickCampoDesc('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentosAux,desc_tiposDocumentosAux);}" style="text-decoration:none;" name="anchorTipoDocumento" id="anchorTipoDocumento" onclick="javascript:window.focus();">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonTipoDocumento" alt= "<%=descriptor.getDescripcion("altDesplegable")%>" title= "<%=descriptor.getDescripcion("altDesplegable")%>"  style="cursor:hand;"></span>
                                                </A>
                                            </div>
                                        </TD>
                                    </tr>
                                </table>
                            </TD>
                        </TR>
                        <jsp:include page="/jsp/registro/entrada/filtrosProcOtraAdmin.jsp" flush="true"/>
                        <TR>
                            <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("res_asunto")%>:</TD>
                            <TD style="width:90%;">
                                <html:text styleClass="inputTexto" property="codAsunto" styleId="codAsunto" style="width:8%" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                                <html:text styleClass="inputTexto" property="descAsunto" styleId="descAsunto" style="width:70%" readonly="true"/>
                                 <A href="" style="text-decoration:none;" name="anchorAsunto"/>
                                 <A href="javascript:mostrarArbolClasifAsuntos();" style="text-decoration:none;" id="anclaD" name="anclaD"
                                     accesskey="" onclick="javascript:window.focus();">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonClasifAsuntos" alt="<%=descriptor.getDescripcion("altDesplegable")%>" alt="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                </A>
                                <div id="divAsuntoBaja" name="divAsuntoBaja" class="etiquetaError" style="float:right;visibility:hidden"></div>
                            </TD>
                        </TR>
                        <TR>
                            <TD colspan="2">
                                <div style="float:left;width:50%">
                                    <span class="etiqueta"><%=descriptor.getDescripcion("etiq_Extracto")%>:</span>
                                    <span class="columnP">
                                        <html:textarea styleId="obligatorio" styleClass="textareaTextoObligatorio" cols="74" rows="5" property="asunto"  maxlength="7" style="width:99%" 
                                                       onkeydown="return textCounter(this,4000);" onblur="return xAMayusculas(this);" value=""></html:textarea>
                                    </span>
                                </div>
                                <div style="float:left;width:50%">
                                    <span class="etiqueta"><%=descriptor.getDescripcion("etiq_observaciones")%>:</span>
                                    <span class="columnP">
                                            <html:textarea styleClass="textareaTexto" property="observaciones" style="width:99%" rows="5"  maxlength="7" 
                                                           onkeydown="return textCounter(this,4000);" onblur="return xAMayusculas(this);"  value=""></html:textarea>
                                    </span>
                                </div>
                            </TD>
                        </TR>
                        <!-- Unidad Organica Origen/Destino -->
                        <%
                            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
                        %>
                            <TR>
                                <TD style="width: 10%" class="etiqueta" id="gEtiqUnidOrigenDestino"><%=descriptor.getDescripcion("gEtiqUnidDestino")%>:</TD>
                                <TD class="columnP" style="width: 90%">
                                    <html:hidden property="cod_uniRegDestinoORD" value=""/>
                                    <html:text styleClass="inputTextoObligatorio" property="cod_uor" style="width:8%"
                                               onkeyup="return xAMayusculas(this);"
                                               onfocus="javascript:{this.select();registrarCambioUor();}"
                                               onblur="javascript:{cambiaUnidadOrganica();}"/>
                                    <input type="hidden" name="cod_uor_anterior" id="cod_uor_anterior" value=""/>
                                    <html:text styleClass="inputTextoObligatorio"  property="desc_uniRegDestinoORD" style="width:86%" readonly="true"
                                               onclick="javascript:{onClickDesc_uniRegDestinoORD();}"
                                               onfocus="javascript:{registrarCambioUor();}"/>
                                    <A href="javascript:{onClickHref_uniRegDestinoORD();}" style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistroORD"
                                       onclick="javascript:window.focus();">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadeRexistroORD" alt="<%=descriptor.getDescripcion("altDesplegable")%>" alt="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                    </A>
                                </TD>
                            </TR>
                            <tr>
                                <TABLE id="unidadDestinoEntradaSIR" style="display:none;" width="100%">
                                    <TR id="filaUnidadDestinoEntrada">
                                        <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidDestino")%>:</TD>
                                        <TD class="columnP" valign="top">
                                            <html:hidden property="codigoUnidadDestinoSIRHidden" styleId="codigoUnidadDestinoSIRHidden" value=""/>
                                            <html:hidden property="listaCodTerceroSIRHidden" styleId="listaCodTerceroSIRHidden" value=""/>
                                            <html:hidden property="listaVersionTerceroSIRHidden" styleId="listaVersionTerceroSIRHidden" value=""/>
                                            <html:hidden property="listaCodDomicilioSIRHidden" styleId="listaCodDomicilioSIRHidden" value=""/>
                                            <html:text styleClass="inputTexto" property="codigoUnidadDestinoSIR" styleId="codigoUnidadDestinoSIR" style="width:8%" readonly="readonly" disabled="disabled" />
                                            <input type="hidden" name="codigoUnidadDestinoSIR_anterior" id="codigoUnidadDestinoSIR_anterior" value=""/>
                                            <html:text styleClass="inputTexto" property="nombreUnidadDestinoSIR" styleId="nombreUnidadDestinoSIR" style="width:86%" readonly="readonly" disabled="disabled" />
                                            <A href="#" style="text-decoration:none;" id="chooseUnidadDestinoSIR" name="chooseUnidadDestinoSIR" onclick="mostrarModalSeleccionUnidadDIR3();" >
                                                <span class="fa fa-plus-circle" aria-hidden="true" id="desp" name="spanChooseUnidadDestinoSIR" alt="<%=descriptor.getDescripcion("msgSelecDestinoSalidaSIR")%>" style="cursor:hand;"></span>
                                            </A>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </tr>
                        <% } else { %>
                            <TR>
                                <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidOrigen")%>:</TD>
                                <TD class="columnP" style="width: 90%">                                           
                                    <html:hidden property="cod_uniRegDestinoORD" value=""/>
                                    <html:text styleClass="inputTextoObligatorio" property="cod_uor" style="width:8%" styleId="cod_uor"
                                               onkeyup="return xAMayusculas(this);"
                                               onfocus="javascript:{this.select();registrarCambioUor();}"
                                               onblur="javascript:{cambiaUnidadOrganica();}"/>
                                    <input type="hidden" name="cod_uor_anterior" id="cod_uor_anterior" value=""/>
                                    <html:text styleClass="inputTextoObligatorio" property="desc_uniRegDestinoORD" style="width:86%" readonly="true" styleId="desc_uniRegDestinoORD"
                                               onclick="javascript:{onClickDesc_uniRegDestinoORDFiltroUsu(codOrg,codUsu);}"
                                               onfocus="javascript:{registrarCambioUor();}"/>
                                    <A href="javascript:{onClickHref_uniRegDestinoORDFiltroUsu();}" style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistroORD"
                                       onclick="javascript:window.focus();">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadeRexistroORD" alt="<%=descriptor.getDescripcion("altDesplegable")%>" alt="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                    </A>
                                </TD>
                            </TR>
                            <!-- Esta propiedad se cumplimenta en las Entradas pero en el apartado Destinios Otros registros, Otra pestana definida alrededor de linea 4634 -->
                            <TR id="filaUnidadDestinoSalida" style="display: none;">
                                <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidDestino")%>:</TD>
                                <TD class="columnP" style="width: 90%">
                                    <html:hidden property="codigoUnidadDestinoSIRHidden" styleId="codigoUnidadDestinoSIRHidden" value=""/>
                                    <html:text styleClass="inputTexto" property="codigoUnidadDestinoSIR" styleId="codigoUnidadDestinoSIR" style="width:8%" readonly="readonly"/>
                                    <input type="hidden" name="codigoUnidadDestinoSIR_anterior" id="codigoUnidadDestinoSIR_anterior" value=""/>
                                    <html:text styleClass="inputTexto" property="nombreUnidadDestinoSIR" styleId="nombreUnidadDestinoSIR" style="width:86%" readonly="readonly"/>
                                    <A href="#" style="text-decoration:none;" id="chooseUnidadDestinoSIR" name="chooseUnidadDestinoSIR" onclick="mostrarModalSeleccionUnidadDIR3();" >
                                        <span class="fa fa-plus-circle" aria-hidden="true" id="desp" name="spanChooseUnidadDestinoSIR" alt="<%=descriptor.getDescripcion("msgSelecDestinoSalidaSIR")%>" style="cursor:hand;"></span>
                                    </A>
                                </TD>
                            </TR>
                            
                        <% } %>
                        
                        <!-- Envio de correos a unidades organicas -->
                        <TR>
                            <TD colspan="2">
                                <div class="columnP" style="width:50%;float:left">
                                    <span id="checkBoxNotificar" class="etiquetaDeshabilitada">
                                        <html:checkbox disabled="true" property="enviarCorreo" styleId="enviarCorreo" value="true" onclick="javascript:{onChangeNotificar();}">
                                            <%=descriptor.getDescripcion("etiqEnviarNotif")%>
                                        </html:checkbox>
                                    </span>
                                    <!-- Botón NOTIFICAR A -->
                                    <input type="button" class="botonLargo" title="<%=descriptor.getDescripcion("altNotificar")%>" alt="<%=descriptor.getDescripcion("altNotificar")%>" 
                                           value="<%=descriptor.getDescripcion("gbNotificar")%>" style="float:right;margin-right: 2%;"
                                           name="cmdNotificar"  onClick="pulsarNotificar();return false;"/>
                                            <!-- Fin Botón NOTIFICAR A -->
                                </div>


                                        <div id="capaTiposEstadoAnotacion" name="capaTiposEstadoAnotacion" style="width:50%;float:left;visibility:visible" >
                                            <span class="etiqueta"><%=descriptor.getDescripcion("etiq_estadoAnot")%>:&nbsp;</span>
                                            <span class="columnP">
                                            <html:text styleId="noObligatorio" styleClass="inputTexto"  property="cod_estadoAnotacion"
                                                       style="width:10%" value="" maxlength="1" onkeyup = "return SoloDigitosNumericos(this);" onfocus="javascript:this.select();" onchange="javascript:{divSegundoPlano=true;inicializarValores('cod_estadoAnotacion','desc_estadoAnotacion',cod_estadosAnotaciones,desc_estadosAnotaciones);}"/>
                                            <html:text styleClass="inputTexto" styleId="noObligatorio"  property="desc_estadoAnotacion" style="width:68%" readonly="true" value="" onfocus="javascript:{divSegundoPlano=true;inicializarValores('cod_estadoAnotacion','desc_estadoAnotacion',cod_estadosAnotaciones,desc_estadosAnotaciones);}" onclick="javascript:{divSegundoPlano=false;inicializarValores('cod_estadoAnotacion','desc_estadoAnotacion',cod_estadosAnotaciones,desc_estadosAnotaciones);}"/>
                                            <A href="javascript:{divSegundoPlano=false;inicializarValores('cod_estadoAnotacion','desc_estadoAnotacion',cod_estadosAnotaciones,desc_estadosAnotaciones);}" style="text-decoration:none;" id="anclaD" name="anchorEstadoAnotacion" onclick="javascript:window.focus();">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonEstadoAnotacion" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                            </A>
                                            </span>
                                        </div>

                                        <div id="capaTiposEstadoSIR" name="capaTiposEstadoSIR" style="width:50%;float:left;visibility:visible" >
                                            <span class="etiqueta"><%=descriptor.getDescripcion("etiq_estadoSIR")%>:&nbsp;</span>
                                            <span class="columnP">
                                                <html:text styleId="codEstadoSIR" styleClass="inputTexto"  property="codEstadoSIR"
                                                    style="width:10%" value="" maxlength="1" onkeyup = "return SoloDigitosNumericos(this);" onfocus="javascript:this.select();" />
                                                <html:text styleClass="inputTextoObligatorio" styleId="descEstadoSIR"  property="descEstadoSIR" style="width:64%" readonly="true"/>
                                                <A href="" id="anchorEstadoSIR" name="anchorEstadoSIR">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEstadoSIR" name="botonEstadoSIR" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                                </A>
                                            </span>

                                        </div>


                            </TD>
                        </TR>
                        <TR>
                            <TD colspan="2">
                                <table style="width:100%">
                                    <tbody>
                                        <tr>
                                            <td style="width:50%"><div id="etiquetaTipoRem" class="etiqueta"><%=descriptor.getDescripcion("res_tipoRemit")%>:</div></td>
                                            <td style="width:25%"><div class="etiqueta"><%=descriptor.getDescripcion("res_tipoTrans")%>:</div></td>
                                            <td style="width:25%"><div class="etiqueta"><%=descriptor.getDescripcion("res_numTransp")%>:</div></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <table style="width:100%">
                                    <tbody>
                                        <tr>
                                            <td style="width:50%">
                                                <DIV id="TEOrdinaria" name="TEOrdinaria" STYLE="position:relative; visibility:visible;" >
                                                  <TABLE id="prueba1" width="100%" cellpadding="0" cellspacing="2">
                                                      <TR>
                                                          <TD valign="top" class="columnP">
                                                              <html:text styleClass="inputTexto"  property="cod_tipoRemitente" style="width:15%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();" onchange="javascript:onchangeCampoCodigo('cod_tipoRemitente','txtNomeTipoRemitente', cod_tiposRemitentes,desc_tiposRemitentes,operadorConsulta);"/>
                                                              <html:text styleClass="inputTexto" property="txtNomeTipoRemitente" style="width:75%" readonly="true" onfocus="javascript:onFocusCampoDesc('cod_tipoRemitente','txtNomeTipoRemitente',cod_tiposRemitentes,desc_tiposRemitentes,operadorConsulta);" onclick="javascript:onClickCampoDesc('cod_tipoRemitente','txtNomeTipoRemitente',cod_tiposRemitentes,desc_tiposRemitentes,operadorConsulta);"/>
                                                              <A href="javascript:onClickCampoDesc('cod_tipoRemitente','txtNomeTipoRemitente',cod_tiposRemitentes,desc_tiposRemitentes);" style="text-decoration:none;" id="anclaD" name="anchorTipoRemitente" onclick="javascript:window.focus();">
                                                                  <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonTipoRemitente" alt= "<%=descriptor.getDescripcion("altDesplegable")%>" title= "<%=descriptor.getDescripcion("altDesplegable")%>"  style="cursor:hand;"></span>
                                                              </A>
                                                          </TD>
                                                      </TR>
                                                  </TABLE>
                                              </DIV>
                                            </td>
                                            <td style="width:25%">
                                                  <div class="columnP">
                                                  <html:text styleClass="inputTexto"  property="cod_tipoTransporte" style="width:16%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();" onchange="javascript: onchangeCampoCodigo('cod_tipoTransporte','desc_tipoTransporte', cod_tiposTransportes,desc_tiposTransportes,operadorConsultaNulo);"/>
                                                  <html:text styleClass="inputTexto" property="desc_tipoTransporte" style="width:42%" readonly="true" onfocus="javascript:onFocusCampoDesc('cod_tipoTransporte','desc_tipoTransporte', cod_tiposTransportes,desc_tiposTransportes,operadorConsultaNulo);" onclick="javascript:onClickCampoDesc('cod_tipoTransporte','desc_tipoTransporte', cod_tiposTransportes,desc_tiposTransportes,operadorConsultaNulo);"/>
                                                  <A href="javascript:onClickCampoDesc('cod_tipoTransporte','desc_tipoTransporte',cod_tiposTransportes,desc_tiposTransportes);" style="text-decoration:none;" id="anclaD" name="anchorTipoTransporte" onclick="javascript:window.focus();">
                                                      <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonTipoTransporte" alt= "<%=descriptor.getDescripcion("altDesplegable")%>" title= "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                                  </A>
                                              </div>
                                            </td>
                                            <td style="width:25%">
                                                <html:text  styleClass="inputTexto"  maxlength="10" property="txtNumTransp"/>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </TD>
                        </TR>
                        <TR>
                            <TD colspan="2">
                                <div style="width: 50%;float:left">
                                    <span style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</span>
                                    <span style="width: 90%" class="columnP">
                                        <%if(userAgent.indexOf("MSIE")!=-1) {%>		 
                                            <html:text styleClass="inputTexto"  property="cod_procedimiento" style="width:16%"  onchange="javascript:{onchangeCod_procedimiento();divSegundoPlano=true}"  onfocus="javascript:{this.select();onFocus_CodProcedimiento();}" onblur="return xAMayusculas(this);" onkeypress="return xAMayusculas(this);"/>
                                        <%}else{%>
                                            <html:text styleClass="inputTexto"  property="cod_procedimiento" style="width:16%" onkeyup="return xAMayusculas(this);" onchange="javascript:{onchangeCod_procedimiento();divSegundoPlano=true}"  onfocus="javascript:{this.select();onFocus_CodProcedimiento();}"/>
                                        <%}%>
                                        <!-- #231150: se añade onBlur para que cargue siempre los documentos del procedimiento-->
                                        <html:text styleClass="inputTexto" property="desc_procedimiento" style="width:50%" readonly="true" onclick="javascript:{onClickDesc_procedimiento();}" onblur="javascript:{onFocus_CodProcedimiento();}"/>
                                        <A style="text-decoration:none;" id="anclaD" name="anchorProcedimiento" onclick="javascript:window.focus();" onfocus="javascript:{cambiaFoco();}">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonProcedimiento" onclick="javascript:{onClickDesc_procedimiento();divSegundoPlano=false}" alt= "<%=descriptor.getDescripcion("altDesplegable")%>" title= "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                        </A>
                                    </span>
                                </div>
                                <div style="width: 50%;float:left">
                                    <span style="margin-left:4%" class="etiqueta"><%=descriptor.getDescripcion("res_ExpRel")%>:</span>
                                    <span class="columnP">
                                        <%if(userAgent.indexOf("MSIE")!=-1) {%>		 
                                            <html:text  styleClass="inputTexto" style="width:40%" maxlength="18" property="txtExp1" styleId="txtExp1" onchange="javascript:{onchangeNumExpediente();}"  onfocus="javascript:{onfocusNumExpediente();}" value=""  onkeypress="javascript:PasaAMayusculas(event);"/>
                                        <%}else{%>
                                            <html:text  styleClass="inputTexto" style="width:40%" maxlength="18" property="txtExp1" styleId="txtExp1" onkeyup="return xAMayusculas(this);" onchange="javascript:{onchangeNumExpediente();}"  onfocus="javascript:{onfocusNumExpediente();}" value=""/>
                                        <%}%>
                                    </span>
                                </div>
                            </TD>
                        </TR>
                        <TR>
                            <TD colspan="2">
                                <div style="float:left;width: 50%" class="columnP">
                                    <div id="divRegistroTelematico" style="text-align:left;width:100%;font-weight:bold;margin-top:4px;font-size:12px;font-color:#0F2667;">
                                        <span  class="etiqueta"><input id="chkRegistroTelematico" type="checkbox" name="chkRegistroTelematico" onclick="javascript:elements['registroTelematico'].value=this.checked;" <c:if test="${sessionScope.MantAnotacionRegistroForm.registroTelematico eq true}">  checked  </c:if> > 
                                         <%=descriptor.getDescripcion("etiqRegTelematico")%></span>
                                    </div>
                                </div>
                                <div style="width: 50%;float:left">
                                    <span class="etiqueta"><%=descriptor.getDescripcion("res_ExpRelacionados")%>:</span>
                                    <span class="columnP">
                                        <%if(userAgent.indexOf("MSIE")!=-1) {%>		 
                                            <input type="text" class="inputTexto" style="width:1%;display:none" maxlength="18" readonly="true" id="codListaExpedientesRelacionados"/>
                                            <input type="text" class="inputTexto" style="width:40%" maxlength="18" readonly="true" id="descListaExpedientesRelacionados"/>
                                        <%}else{%>
                                            <input type="text" class="inputTexto" style="width:1%;display:none" maxlength="18" readonly="true" id="codListaExpedientesRelacionados"/>    
                                            <input type="text" class="inputTexto" style="width:40%" maxlength="18" readonly="true" id="descListaExpedientesRelacionados"/>
                                        <%}%>
                                        <A href="#" style="text-decoration:none;" id="anclaD" name="anchorListaExpedientesRelacionados">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="anclaD" name="botonExpedientesRelacionados" alt= "<%=descriptor.getDescripcion("altDesplegable")%>" title= "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                        </A>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <span class="fa fa-share" aria-hidden="true" name="consultaExpediente" onclick="javascript:consultarExpRelacionado();" style="cursor:hand;" alt= "<%=descriptor.getDescripcion("altExpRel")%>" title= "<%=descriptor.getDescripcion("altExpRel")%>"></span>
                                    </span>
                                </div>
                            </TD>
                        </TR>                                   
                    </TABLE>
                </div>

                <!-- CAPA 3: DESTINO  y ORIGEN
                ------------------------------ -->
                <div class="tab-page" id="tabPage3">
                    <h2 class="tab" id="pestana2"><%=descriptor.getDescripcion("pestanaOtrosDatos")%></h2>
                    <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
                    <TABLE id ="tablaDatosGral" class="contenidoPestanha">
                        <TR>
                            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("gEtiqDocuAportados")%></TD>
                        </TR>
                        <TR>
                            <TD style="width: 86%" id="tablaDoc"></TD>
                            <td style="vertical-align:top;text-align:right">
                                <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gEtiqVer")%>" alt="<%=descriptor.getDescripcion("altVer")%>" title="<%=descriptor.getDescripcion("altVer")%>" name="cmdVerDoc" onclick="pulsarVerDoc();">
                                 <input type= "button" class="botonGeneral" style="margin-top:5px" value='<%=descriptor.getDescripcion("gbAnexar")%>' alt="<%=descriptor.getDescripcion("altAnexar")%>" title="<%=descriptor.getDescripcion("altAnexar")%>" name="cmdAltaDoc" onclick="pulsarAltaDoc();">
                                <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbModificar")%>" alt="<%=descriptor.getDescripcion("altModDocReg")%>" title="<%=descriptor.getDescripcion("altModDocReg")%>" name="cmdModificarDoc"  id="cmdModificarDoc" onclick="pulsarModificarDoc();">
                                <input type= "button" class="botonGeneral" style="margin-top:5px" value="Eliminar" alt="<%=descriptor.getDescripcion("altElimDocReg")%>" title="<%=descriptor.getDescripcion("altElimDocReg")%>" name="cmdEliminarDoc" onclick="pulsarEliminarDoc();">
                                <% if(mostrarCotejo) { %>
									<input type= "button" class="botonGeneral" style="margin-top:5px" value='<%=descriptor.getDescripcion("gbCotejar")%>' alt="<%=descriptor.getDescripcion("altCotejar")%>" title="<%=descriptor.getDescripcion("altCotejar")%>" name="cmdCotejarDoc" onclick="pulsarCotejarDoc();">
								<% } %>
                            </td>
                        </TR>
                         <tr>
                            <td colspan="2">
                                <div id="aportAnterior" name="aportAnterior" style="width:100%;">
                                    <table id="aportadosAnterior" width="100%" >
                                        <tr>
                                            <td class="sub3titulo" colspan="2">Documentos Entregados Anteriormente</td> <!meterlo en descripccion-!>
                                        </tr>
                                        <tr>
                                            <td style="width:86%" id="tablaAnt"></td>
                                            <td style="vertical-align:top;text-align: right">
                                                <input type="button" class="botonGeneral" value='Alta' alt='Alta' title="Alta" name="cmdAltaAnterior" onclick="pulsarAltaAnterior();">
                                                <input type="button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbModificar")%>" alt="Modificar" title="Modificar" name="cmdModAnterior"onclick="pulsarModificarAnt();">
                                                <input type="button" class="botonGeneral"  style="margin-top: 5px" value="<%=descriptor.getDescripcion("altElimDocReg")%>" alt="Eliminar" title="Eliminar" name="cmdEliminarAnterior" onclick="pulsarEliminarAnt();">   
                                            </td>
                                            
                                        </tr>
                                    </table>
                                </DIV>
                            </td>
                        </tr>
                        <TR>
                            <td colspan="2">
                                <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>
                                <span style="visibility:<%=aut%>"class="etiqueta"><%=descriptor.getDescripcion("etiq_autoridad_a")%>:</span>
                                <span style="visibility:<%=aut%>" class="columnP">
                                <% } else {%>
                                <span style="width: 16%; visibility:<%=aut%>" class="etiqueta"><%=descriptor.getDescripcion("etiq_autoridad_desde")%>:</span>
                                <span style="visibility:<%=aut%>"  class="columnP">
                                <%}%>
                                    <html:text styleClass="inputTexto" property="autoridad" size="105" maxlength="105" onkeydown="return textCounter(this,4000);" onblur="return xAMayusculas(this);" value=""></html:text>
                                </span>
                            </TD>
                        </TR>
                        <TR>
                            <td colspan="2">
                                <!-- FIN tipo entrada ORDINARIA -->
                                <!-- Tipo entrada A OTRO REGISTRO -->
                                <DIV id="TEOtroReg" name="TEOtroReg" STYLE="width:100%;display:none;" >
                                    <TABLE id="prueba2" width="100%">
                                        <TR>
                                            <TD class="sub3titulo" colspan="2">&nbsp;<%=descriptor.getDescripcion("res_etiqDestino")%></TD>
                                        </TR>
                                        <TR>
                                            <TD style="width: 16%" class="etiqueta"> <%=descriptor.getDescripcion("tit_Org")%>:
                                            </TD>
                                            <td class="columnP" valign="top">
                                                <html:text styleClass="inputTextoObligatorio"  property="cod_orgDestino" style="width:15%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();" onchange="javascript:onchangeCod_orgDestino();" onblur="javascript:{onchangeCod_orgDestino();onblurOrganizacionOtroReg('cod_orgDestino','desc_orgDestino');setTimeout('ocultarDivNoFocus()',100);}"/>
                                                <html:text styleClass="inputTextoObligatorio"  property="desc_orgDestino" style="width:75%" readonly="true" onclick="javascript:{onClickDesc_orgDestino();}"/>
                                                <A href="javascript:{onClickDesc_orgDestino();}" style="text-decoration:none;" id="anclaD" name="anchorOrganizacionDestino" onclick="javascript:window.focus();">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonOrganizacionDestino" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                                </A>
                                            </TD>
                                        </TR>
                                        <TR>
                                            <TD style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidReg")%>:</TD>
                                            <TD class="columnP" valign="top">
                                                <html:text styleClass="inputTexto"  property="cod_uniRegDestino" style="width:15%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();" onblur="javascript:{onchangeCod_uniRegDestino();setTimeout('ocultarDivNoFocus()',100);}"/>
                                                <html:text styleClass="inputTexto" property="desc_uniRegDestino" style="width:75%" readonly="true" onclick="javascript:{onClickDesc_uniRegDestino();}"/>
                                                <A href="javascript: {onClickDesc_uniRegDestino();}" style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistro" onclick="javascript:window.focus();">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadeRexistro" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                                </A>
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </DIV>
                                <!-- FIN Tipo de Entrada procedente de otro registro -->
                                <!-- Tipo de Entrada procedente de Otro Registro -->
                                <DIV id="capaOrigen" class="etiqueta" name="capaOrigen" STYLE="display:none">
                                    <TABLE width="100%">
                                        <TR>
                                            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("res_etiqOrigen")%> </TD>
                                        </TR>
                                        <TR>
                                            <TD style="width: 16%" class="etiqueta"> <%=descriptor.getDescripcion("tit_Org")%>:</TD>
                                            <TD class="columnP" valign="top">
                                                <html:text styleClass="inputTexto"  property="cod_orgOrigen" style="width:15%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();" onchange="javascript:onchangeCod_orgOrigen();" onblur="javascript:{onchangeCod_orgOrigen();onblurOrganizacionOrigen('cod_orgOrigen','desc_orgOrigen');setTimeout('ocultarDivNoFocus()',100);}"/>
                                                <html:text styleClass="inputTexto"  property="desc_orgOrigen" style="width:75%" readonly="true" onclick="javascript:{onClickDesc_orgOrigen();}"/>
                                                <A href="javascript:{onClickDesc_orgOrigen();}" style="text-decoration:none;" id="anclaD" name="anchorOrganizacionOrigen" onclick="javascript:window.focus();">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonOrganizacionOrigen" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                                                </A>
                                            </TD>
                                        </TR>
                                        <TR>
                                            <TD style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidReg")%>:</TD>
                                            <TD class="columnP" valign="top">
                                                <html:text styleClass="inputTexto"  property="cod_unidadeRexistroOrixe" style="width:15%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();" onblur="javascript:{onchangeCod_uniRegOrigen();setTimeout('ocultarDivNoFocus()',100);}"/>
                                                <html:text styleClass="inputTexto" property="desc_unidadeRexistroOrixe" style="width:75%" readonly="true" onclick="javascript:{onClickDesc_uniRegOrigen();}"/>
                                                <A href="javascript: {onClickDesc_uniRegOrigen();}" style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistroOrigen" ACCESSKEY=""onclick="javascript:window.focus();">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadeRexistroOrigen" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"  style="cursor:hand;"></span>
                                                </A>
                                            </TD>
                                        </TR>
                                        <TR>
                                            <TD colspan="2">
                                                <table width="100%">
                                                    <tr>
                                                        <TD style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("res_EntRelEje")%>:</TD>
                                                        <TD style="width: 13%" class="columnP">
                                                            <html:text  styleClass='inputTexto' style="width: 40%" maxlength="4" property="txtExp1Orixe" onkeyup = "return SoloDigitosNumericos(this);" onfocus="this.select();" onblur="comprobarEntradaRelacionada(this.value,MENSAJE_RANGO_ANO_ENTRADA_RELACIONADA_NO_VALIDO);" />
                                                        </TD>
                                                        <TD style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("res_EntRelNum")%>:</TD>
                                                        <TD class="columnP">
                                                            <html:text  styleClass='inputTexto' style="width: 25%" maxlength="11" property="txtExp2Orixe" onkeyup = "return SoloDigitosNumericos(this);" onfocus="this.select();" />
                                                        </TD>
                                                    </tr>
                                                </table>
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </DIV>
                                <!-- FIN Tipo de entrada procedente de otro registro -->
                               <!-- Botón RELACIONES -->
                                <TABLE width="100%">
                                    <TR>
                                        <TD class="sub3titulo"><%=descriptor.getDescripcion("gEtiqOtrasOpciones")%></TD>
                                    </TR>
                                    <tr>
                                        <td>
                                            <input type="button"
                                                   class="botonGeneral" value="<%=descriptor.getDescripcion("tit_Rel")%>"
                                                   alt="<%=descriptor.getDescripcion("altRelaciones")%>" title="<%=descriptor.getDescripcion("altRelaciones")%>"
                                                   id="cmdRelaciones" name="cmdRelaciones" onClick="pulsarRelaciones();"/>
                                            <input type="button" title="Incluir los temas y actuaciones relativos a la anotación"
                                                   class="botonGeneral" value="Temas" style="float:right"
                                                   id="cmdActuaciones" name="cmdActuaciones" onClick="pulsarActuaciones();"/>
                                        </td>
                                    </tr>
                                    </table>
                                <!-- Fin Botón RELACIONES -->
                                 <!-- bloque Datos SGA -->	
                                <% if(datosSga){%>	
                                      <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>  <table style="width:100%" id="bloqueSga"> 
                                       <%}else{%>    <table style="display:none; width:100%" id="bloqueSga"> <%}%>  
                                    
                                        <tr style="width:100%">	
                                             <td colspan="2" class="sub3titulo" style="width:100%"><%=descriptor.getDescripcion("etiqDatosSga")%></td>	
                                        </tr>	
                                        <tr>	
                                            <td style="width:8%" class="etiqueta"><%=descriptor.getDescripcion("etiqCodigoSga")%></td>	
                                            <td style="width:92%" class="columnP">	
                                                <html:text styleClass="inputTexto" property="codigoSga" size="12" maxlength="12" readonly="true"></html:text>	
                                            </td>	
                                        </tr>	
                                        <tr>	
                                            <td style="width:8%" class="etiqueta"><%=descriptor.getDescripcion("etiqExpedienteSga")%> </td> 	
                                            <td style="width:92%" class="columnP">	
                                                <html:text styleClass="inputTexto" property="expedienteSga" size="60" maxlength="30" readonly="true"></html:text>	
                                            </td>	
                                        </tr>	
                                    </table>	
                              	 <%}else{%> 
                                      <html:hidden property="codigoSga"/>
                                        <html:hidden property="expedienteSga"/>
                                     <%}%>  
                              <!-- fin bloque SGA -->
                            </TD>
                        </TR>
                    </TABLE>
                </div>


            <!-- CAPA 5: FORMULARIOS
------------------------------ -->

<% if ("si".equals(m_Config.getString("JSP.Formularios.Registro"))) {
%>
            <div class="tab-page" id="tabPage7">
                <h2 class="tab" id="pestana7"><%=descriptor.getDescripcion("etFichaFormularios")%></h2>
                <script type="text/javascript">tp1_p7 = tp1.addTabPage( document.getElementById( "tabPage7" ) );</script>
                <TABLE width="100%" border="0">
                    <tr><td height="2px"></td></tr>
                    <tr>
                                                            <td align="center">
                                                                    <table border="0">
                                                                    <tr>
                                                                            <td id="tablaFormularios" ></td>
                                                                    </tr>
                                                                    </table>
                                                            </td>
                                                    </tr>
                    <TR><TD height="10px"></TD></TR>
                    <TR><TD width="100%" align="center" valign="top">
                            <input style="border-style: solid;  border-width: 1px 2px 2px 1px; border-color: #999999 #465736 #465736 #999999;" type="button" name="cmdVerAnexo" alt="<%=descriptor.getDescripcion("altVerAnexo")%>" title="<%=descriptor.getDescripcion("altVerAnexo")%>" onclick="pulsarVerAnexoPDF();" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbVerAnexo")%>">
                        </TD>
                    </TR>
                </TABLE>
            </div>
            <%} else {%>
            <div style="display:none">
                <table>
                    <tr><td id="tablaFormularios"></td></tr>
                </table>
            </div>
            <% }%>
   </div>

    <DIV id="capaRadioButtons" name="capaRadioButtons" STYLE="width:100%; height:0px; visibility:hidden;">
        <TABLE style="width: 60%">
            <TR>
                <TD width="70%" class="subsubtitulo textoSuelto">
                    <input type="radio" name="tipoConsulta" class="textoSuelto" value="listado" CHECKED> <%=descriptor.getDescripcion("gEtiqListReg")%></input>
                    &nbsp;
                    <input type="radio" name="tipoConsulta" class="textoSuelto" value="registro" > <%=descriptor.getDescripcion("gEtiqRegAReg")%></input>
                </TD>
                                                                                        
            </TR>

        </TABLE>
    </DIV>
    <DIV style="width:100%" id="capaNavegacionConsulta" name="capaNavegacionConsulta" class="dataTables_wrapper"></DIV>
<!-- Definimos varias capas para los botones según lo que se permite hacer. -->


<% if (permisoMantenimiento) {%>
         <!-- Botones ALTA:
        1. Registrar alta.
        2. Cancelar alta.
    -->
        <DIV id="capaBotones2" name="capaBotones2" style="display:none" class="botoneraPrincipal">
            <!-- BOTÓN DE GRABAR -->
              <% if (mostrarDigitalizar && "reservas".equals(respOpcion)) { %>
                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdRegistrarAlta" onClick="comprobarCodProcedimientoValido('Mod');return false;"/>
             <%} else {%>
             <input type="button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdRegistrarAlta" onClick="comprobarCodProcedimientoValido('Alta');return false;">
              <% } %>
              <!--FIN  BOTÓN DE GRABAR -->
                
            <!-- Botón FINALIZAR digit --> 
             <% if (mostrarDigitalizar && !"reservas".equals(respOpcion)) { %>
                <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbFinalizar")%>" alt="<%=descriptor.getDescripcion("altFinalizar")%>" title="<%=descriptor.getDescripcion("altFinalizar")%>" name="cmdFinDigitalizarAlta" onclick="pulsarFinalizar('AltaFinDigitalizar');">
              <%}%>
       
            <% if (mostrarDigitalizar && "reservas".equals(respOpcion)) { %>
                <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbFinalizar")%>" alt="<%=descriptor.getDescripcion("altFinalizar")%>" title="<%=descriptor.getDescripcion("altFinalizar")%>" name="cmdFinDigitalizarAlta" onclick="pulsarFinalizar('ModFinDigitalizar');">
              <%}%>
            <!-- Fin botón FINALIZAR -->

             <!-- BOTÓN DE CANCELAR -->
             <% if (mostrarDigitalizar && "reservas".equals(respOpcion)) { %>
            <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarAlta" onClick="pulsarSalirConsultar();return false;"/>
            <%} else {%>
            <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarAlta" onClick="pulsarCancelarAlta();return false;"/>
            <% } %>
            <!--FIN BOTÓN DE CANCELAR -->
            
            <!-- #270948: Botón Digitalizar -->
            <% if (mostrarDigitalizar && !"reservas".equals(respOpcion)) { %>
                    <input type= "button" class="botonGeneral" value="Digitalizar" alt="Digitalizar documentos" 
                           title="Digitalizar documentos" name="cmdDigitalizarDesdeAlta" id="cmdDigitalizarDesdeAlta" onclick="pulsarDigitalizarConAltaPrevia();return false;">
            <% } %>
            
                <% if (mostrarDigitalizar && "reservas".equals(respOpcion)) { %>
            <input type= "button" class="botonGeneral" value="Digitalizar" alt="Digitalizar documentos" 
                        style="display:none" title="Digitalizar documentos" name="cmdDigitalizarDesdeAlta" id="cmdDigitalizarDesdeAlta" onclick="pulsarDigitalizarDocs('consulta');">
    <% } %>
        </DIV>
        <!-- Fin botones ALTA. -->
<% }%>

<!-- Botones PÁGINA BUSCADA:
             1. Alta (desde consulta)
             2. Modificar.
             3. Anular.
             4. Duplicar.
             5. Contestar/Responder.
             6. Relacionar.
             7. Salir (de consultar)
             8. Digitalizar
-->
<DIV id="capaBotones3" name="capaBotones3" style="display:none" class="botoneraPrincipal">
    <% 	if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>

    <% if (permisoMantenimiento) {%>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAltaDesdeConsulta" onClick="pulsarAltaDesdeConsulta();return false;" accesskey='A'/>
    <!-- Fin botón Alta Desde Consulta. -->

    <!-- Botón MODIFICAR. -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bModificarAnot")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificarAnot")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificar" id="cmdModificar" onClick="divSegundoPlano=true; pulsarModificar();return false;"/>
    <!-- Fin botón MODIFICAR. -->
    <!-- Botón ANULAR. -->
        <input type="button" class="botonGeneral" title='<%=descriptor.getDescripcion("toolTip_bAnular")%>' alt='<%=descriptor.getDescripcion("toolTip_bAnular")%>' title='<%=descriptor.getDescripcion("toolTip_bAnular")%>' value='<%=descriptor.getDescripcion("gbAnular")%>' name="cmdAnular" id="cmdAnular" onClick="pulsarAnular();return false;"/>
    <!-- Botón DUPLICAR. -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bDuplicar")%>' alt='<%=descriptor.getDescripcion("toolTip_bDuplicar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbDuplicar")%>'
               name="cmdDuplicar" id="cmdDuplicar" onClick="pulsarDuplicar();return false;" style="display: none"/>
    <!-- Fin botón DUPLICAR. -->
    <!-- Botón CONTESTAR. -->
     <% if (permiso_contestar) {%>
        <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bContestar")%>' alt='<%=descriptor.getDescripcion("toolTip_bContestar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbContestar")%>'
               name="cmdContestar" id="cmdContestar" onClick="pulsarContestar();return false;"/>
    <!-- Fin botón CONTESTAR. -->
    <% }%>
    <!-- Botón RELACIONAR. -->
    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bRelacionar")%>' alt='<%=descriptor.getDescripcion("toolTip_bRelacionar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbRelacionar")%>'
               name="cmdRelacionar" onClick="pulsarRelacionar();return false;"/>
    <!-- Fin botón RELACIONAR -->
<% }%>
    <!-- Botón SALIR CONSULTAR. -->
    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'
            class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>'
            name="cmdCancelarBuscada" onClick="pulsarSalirConsultar();return false;"/>
    <!-- Fin botón SALIR CONSULTAR. -->
<% } else { // no es "E" ni "Relacion_E"%>
<% if (permisoMantenimiento) {%>
    <!-- Botón Alta Desde Consulta. -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>'
               name="cmdAltaDesdeConsulta" onClick="pulsarAltaDesdeConsulta();return false;" accesskey='A'/>
    <!-- Fin botón Alta Desde Consulta. -->
    <!-- Botón MODIFICAR. -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bModificarAnot")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>'
               name="cmdModificar" id="cmdModificar" onClick="pulsarModificar();return false;"/>
    <!-- Fin botón MODIFICAR. -->
    <!-- Botón ANULAR. -->
    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAnular")%>' alt='<%=descriptor.getDescripcion("toolTip_bAnular")%>'
           class="botonGeneral" value='<%=descriptor.getDescripcion("gbAnular")%>'
           name="cmdAnular" id="cmdAnular" onClick="pulsarAnular();return false;"/>
    <!-- Fin botón ANULAR. -->
    <!-- Botón DUPLICAR. -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bDuplicar")%>' alt='<%=descriptor.getDescripcion("toolTip_bDuplicar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbDuplicar")%>'
               name='cmdDuplicar' id='cmdDuplicar' onClick="pulsarDuplicar();return false;" style="display:none"/>
    <!-- Fin botón DUPLICAR. -->
    <% if (permiso_contestar) {%>
   <!-- Botón RESPUESTA. -->
    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bRespuesta")%>' alt='<%=descriptor.getDescripcion("toolTip_bRespuesta")%>'
           class="botonGeneral" value='<%=descriptor.getDescripcion("gbRespuesta")%>'
           name="cmdResponder" id="cmdResponder" onClick="pulsarResponder();return false;"/>
    <!-- Fin botón RESPUESTA -->
    <% }%>
    <!-- Botón RELACIONAR. -->
    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bRelacionar")%>' alt='<%=descriptor.getDescripcion("toolTip_bRelacionar")%>'
                class="botonGeneral" value='<%=descriptor.getDescripcion("gbRelacionar")%>'
                name="cmdRelacionar" id="cmdRelacionar" onClick="pulsarRelacionar();return false;"/>
    <!-- Fin botón RELACIONAR -->
<% }%>
    <!-- Botón VOLVER AL LISTADO. -->
    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'
           class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>'
           name="cmdCancelarBuscada" id="cmdCancelarBuscada" onClick="pulsarSalirConsultar();return false;"/>
    <!-- Fin botón SALIR CONSULTAR. -->
    <% }%>
    <!-- Botón NUEVA CONSULTA. -->
    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bListado")%>' alt='<%=descriptor.getDescripcion("toolTip_bListado")%>'
            class="botonGeneral" value= '<%=descriptor.getDescripcion("gbListado")%>'
            name="cmdListado"  id="cmdListado" onClick="pulsarListado();return false;"/>
	<!-- Fin botón NUEVA CONSULTA. -->
    <!-- Botón ENVIAR REGISTRO A TRAVÉS DEL CIR. -->
    <% Config registroConf = ConfigServiceHelper.getConfig("Registro");
       if (registroConf.getString("INTEGRACION_SIR").equals("SI")){%>
    <input type="button" title='<%=descriptor.getDescripcion("gbEnviarAsiento")%>a' alt='<%=descriptor.getDescripcion("gbEnviarAsiento")%>'
		   class="botonGeneral" value= '<%=descriptor.getDescripcion("gbEnviarAsiento")%>'
	   name="cmdEnviar" id="cmdEnviar" onClick="javascript:pulsarEnviar('<%=request.getContextPath()%>');"/>
    <!-- Fin botón Botón ENVIAR REGISTRO A TRAVÉS DEL CIR. -->
    <% }%>
    <!-- Campos para retramitar al modificar procedimiento o desde el modal para Usuario ADMIN  -->
    <input type="hidden" name="regRetramitarDocTipoAnotacion" value="<%=((String) session.getAttribute("tipoAnotacion")!=null ? (String) session.getAttribute("tipoAnotacion") : tipoAnotacion )%>" id="regRetramitarDocTipoAnotacion"/>
    <input type="hidden" name="regRetramitarDocCodDepPKAnotacion" value="<%=usuarioVO.getDepCod()%>" id="regRetramitarDocCodDepPKAnotacion"/>
    <input type="hidden" name="regRetramitarDocCodUnidOrgPKAnotacion" value="<%=usuarioVO.getUnidadOrgCod()%>" id="regRetramitarDocCodUnidOrgPKAnotacion"/>
    <input type="hidden" name="regRetramitarDocCodigoOrgaEsquema" value="<%=usuarioVO.getOrgCod()%>" id="regRetramitarDocCodigoOrgaEsquema"/>
    <% if(codUsu==m_Registro.getInt("IDBD_USU_ADMIN_RETRAMITAR_DOC")){ %>
    <input type="button" title='<%=descriptor.getDescripcion("msgRetramiDocCambioProTitulo")%>' alt='<%=descriptor.getDescripcion("msgRetramiDocCambioProTitulo")%>' class="botonLargo" value='<%=descriptor.getDescripcion("msgRetramiDocCambioRetramitar")%>' name="cmdRegRetramitarDoc" id="cmdRegRetramitarDoc" onClick="registroRetramitarDocsCargarPantallaSelectOwner()" />
    <jsp:include page="/jsp/registro/digitalizacion/retramitarDocsCambioProcSelectOwner.jsp" flush="true">
            <jsp:param name="idiomaUsuarioModal" value="<%=usuarioVO.getIdioma()%>" />
            <jsp:param name="codOrganizacionModal" value="<%=usuarioVO.getOrgCod()%>" />
            <jsp:param name="idUsuarioModal" value="<%=usuarioVO.getIdUsuario()%>" />
            <jsp:param name="aplModal" value="<%=usuarioVO.getAppCod()%>" />
            <jsp:param name="cssModal" value="<%=usuarioVO.getCss()%>" />
    </jsp:include>
    <%}%>
</div>

<div id="divGestionSeleccionDestinoSIR">
    <input type="hidden" name="codigoUnidadOrganicaPKRegistro" value="<%=cod_unidOrg%>" id="codigoUnidadOrganicaPKRegistro"/>
    <input type="hidden" name="codigoDepartamentoPKRegistro" value="<%=cod_dep%>" id="codigoDepartamentoPKRegistro"/>
    <input type="hidden" name="tipoAnotacionRegistro" value="<%=tipoAnotacion%>" id="tipoAnotacionRegistro"/>
    <input type="hidden" name="msgSalidaNoDadaAltaSIR" value="<%=descriptor.getDescripcion("msgSalidaNoDadaAltaSIR")%>" id="msgSalidaNoDadaAltaSIR"/>
    <input type="hidden" name="msgEntradaNoDadaAltaSIR" value="<%=descriptor.getDescripcion("msgEntradaNoDadaAltaSIR")%>" id="msgEntradaNoDadaAltaSIR"/>
    <input type="hidden" name="etiqUnidOrigen" value="<%=descriptor.getDescripcion("gEtiqUnidOrigen")%>" id="etiqUnidOrigen"/>
    <input type="hidden" name="etiqUnidDestino" value="<%=descriptor.getDescripcion("gEtiqUnidDestino")%>" id="etiqUnidDestino"/>
    <jsp:include page="/jsp/registro/sir/modal/selectUnidadDestinoDIR3-SIR_Modal.jsp" flush="true">
            <jsp:param name="idiomaUsuarioModal" value="<%=usuarioVO.getIdioma()%>" />
            <jsp:param name="codOrganizacionModal" value="<%=usuarioVO.getOrgCod()%>" />
            <jsp:param name="idUsuarioModal" value="<%=usuarioVO.getIdUsuario()%>" />
            <jsp:param name="aplModal" value="<%=usuarioVO.getAppCod()%>" />
            <jsp:param name="cssModal" value="<%=usuarioVO.getCss()%>" />
    </jsp:include>
</div>



            
<!-- Botones ALTA DESDE TRAMITAR:
         1. Registrar alta desde tramitar.
         2. Cancelar alta desde tramitar.
-->
<DIV id="capaBotonesAltaDesdeTramite" name="capaBotonesAltaDesdeTramite" style="display:none" class="botoneraPrincipal">
    <!-- Botón REGISTRAR ALTA DESDE TRAMITAR -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>'
               name="cmdRegistrarAltaTramitar" onClick="pulsarRegistrarAltaDesdeTramite();return false;"/>
    <!-- Fin botón REGISTRAR ALTA DESDE TRAMITAR -->
    <!-- Botón CANCELAR ALTA DESDE TRAMITAR -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>'
               name="cmdCancelarAltaTramitar" onClick="pulsarCancelarAltaDesdeTramite();return false;"/>
    <!-- Fin botón CANCELAR ALTA DESDE TRAMITAR -->
</DIV>
<!-- Fin botones ALTA DESDE TRAMITAR -->

<% if (permisoMantenimiento) {%>

         <!-- Botones MODIFICACIÓN:
             1. DUBPLICAR Y GRABAR (Eliminado)
             2. Registrar cambios.
             3. Cancelar cambios. -->
<DIV id="capaBotones4" name="capaBotones4" style="display:none" class="botoneraPrincipal">
    <!-- Botón REGISTRAR duplicar-guardar. -->
    <% if ("buscar".equals(request.getParameter("opcion"))) {%>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bGrabarDuplicar")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabarDuplicar")%>'
               class="botonLargo" value='<%=descriptor.getDescripcion("gbGrabarDuplicar")%>'
               name="cmdRegistrarAlta" onClick="pulsarRegistrarModificar2();return false;">
    <% }%>
    <!-- Botón REGISTRAR CAMBIOS. -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>'
               name="cmdRexistrarModificacion" onClick="comprobarCodProcedimientoValido('Mod');return false;"/>
    <!-- Fin botón REGISTRAR CAMBIOS. -->
    <!-- Botón FINALIZAR digit --> 
      <% if (mostrarDigitalizar) { %>
        <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbFinalizar")%>" alt="<%=descriptor.getDescripcion("altFinalizar")%>" title="<%=descriptor.getDescripcion("altFinalizar")%>" name="cmdFinDigitalizarMod" onclick="pulsarFinalizar('ModFinDigitalizar');">
      <% } %>
      <!-- Fin botón FINALIZAR -->
    
    <!-- Botón CANCELAR CAMBIOS. -->
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>'
               name="cmdCancelarModificar" onClick="pulsarSalirConsultar();return false;"/>
    <!-- Fin botón CANCELAR CAMBIOS. -->
    
    <!-- En #270948: Se añade el Botón Digitalizar a la capaBotones3-->
    <!-- En #326290: Este botón se elimina de la capaBotones3 y se añade a la capaBotones4-->
    <% if (mostrarDigitalizar) { %>
            <input type= "button" class="botonGeneral" value="Digitalizar" alt="Digitalizar documentos" 
                   title="Digitalizar documentos" name="cmdDigitalizarDesdeConsulta" id="cmdDigitalizarDesdeConsulta" onclick="pulsarDigitalizarDocs('consulta');">
    <% } %>
</DIV>

<DIV id="capaBotones5" name="capaBotones5" style="display:none" class="botoneraPrincipal">
    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>'
           class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>'
           name="cmdRegistrarDuplicar" onClick="comprobarCodProcedimientoValido('Dup');return false;"/>
    <!-- Botón FINALIZAR digit --> 
             <% if (mostrarDigitalizar) { %>
                <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbFinalizar")%>" alt="<%=descriptor.getDescripcion("altFinalizar")%>" title="<%=descriptor.getDescripcion("altFinalizar")%>" name="cmdFinDigitalizarAlta" onclick="pulsarFinalizar('DupFinDigitalizar');">
              <%}%>
    <!-- Fin botón FINALIZAR -->
    
    <% if (contestacion) {%>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>'
               name="cmdCancelarDuplicar" onClick="pulsarCancelarContestar();return false;"/>
    <% } else {%>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>'
               name="cmdCancelarDuplicar" onClick="pulsarCancelarDuplicar();return false;"/>
    <% } %>
    
    <!-- Botón DIGITALIZAR -->
    <% if (mostrarDigitalizar) { %>
        <input type= "button" class="botonGeneral" value="Digitalizar" alt="Digitalizar documentos" 
               title="Digitalizar documentos" name="cmdDigitalizarDesdeAlta" id="cmdDigitalizarDesdeAlta" onclick="pulsarDigitalizarConAltaPrevia('Dup');return false;">
    <% } %>
    <!-- Fin Botón DIGITALIZAR -->
</DIV>
<!-- Fin botones DUPLICADO. -->

<% }%>

<!-- Botones CONSULTA.
    1. Alta
    2. Buscar
    3. Limpiar
-->
<DIV id="capaBotonesConsulta" name="capaBotonesConsulta" style="display:none; " class="botoneraPrincipal">
    <% if (permisoMantenimiento) {%>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>'
               name="cmdAlta" onClick="pulsarAlta();return false;"/>
    <% }%>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' alt='<%=descriptor.getDescripcion("toolTip_bBuscar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbBuscar")%>'
               name="cmdConsultar" onClick="pulsarConsultar();return false;"/>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'
               class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>'
               name="cmdLimpiar" onClick="pulsarLimpiar();document.forms[0].ano.value=ejercicioActual;return false;"/>
</DIV>
 
<div id="popupcalendar" class="text" style="border:1px solid red"></div>
<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

</DIV>
</html:form>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>

<script type="text/javascript">


tp1_p1.setPrimerElementoFoco(document.forms[0].fechaAnotacion);
tp1_p3.setPrimerElementoFoco(document.forms[0].cod_uor); // TODO chged

// Combo de tipos de asunto
var comboAsuntos = new Combo("Asunto");
//Combo de expedientesRelacionados
var comboExpedientesRelacionados = new Combo("ListaExpedientesRelacionados");
var cod_asuntos = new Array();
var desc_asuntos = new Array();
var uni_asuntos = new Array();

cont=0;
<logic:present name="MantAnotacionRegistroForm" property="listaAsuntos">
    <logic:iterate id="asunto" name="MantAnotacionRegistroForm" property="listaAsuntos">
        uni_asuntos[cont] = '<bean:write name="asunto" property="unidadRegistro"/>';
        cod_asuntos[cont]  ='<bean:write name="asunto" property="codigo"/>';
        desc_asuntos[cont] ="<str:escape><bean:write name="asunto" property="descripcion" filter="false"/></str:escape>";
        cont++;
    </logic:iterate>
</logic:present>

comboAsuntos.addItems(cod_asuntos, desc_asuntos);
comboAsuntos.selectItem(0);

// Se asigna una funcion definida arriba al evento de cambio del combo
// para que cargue el tipo de asunto.
comboAsuntos.change = onChangeComboAsuntos;
// Fin de combo de tipos de asunto

// Combo de roles
var comboRol = new Combo("RolTercero");
comboRol.change = onChangeComboRol;

// Fin de combo de roles

        // Combo de estadoSIR

        var comboEstadoSIR = new Combo("EstadoSIR");

        function refresh(capa){
            if(capa =="tabDiv")  tabDiv.displayTabla();
        }

// JAVASCRIPT DE LA TABLA DE FORMULARIOS
// Igual que en fichaExpediente
var tabFormularios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaFormularios'),null,'<%=descriptor.getDescripcion("numResultados")%>');

tabFormularios.addColumna('60','left',"<%= descriptor.getDescripcion("etCodForm")%>");
tabFormularios.addColumna('400','left',"<%= descriptor.getDescripcion("etDesForm")%>");
tabFormularios.addColumna('80','left',"<%= descriptor.getDescripcion("etFechaForm")%>");
tabFormularios.addColumna('90','left',"<%= descriptor.getDescripcion("etUsuarioForm")%>");
tabFormularios.addColumna('20','center',"");
tabFormularios.displayCabecera=true;
tabFormularios.displayTabla();

tabFormularios.colorLinea=function(rowID) {
    if(listaFormulariosOriginal[rowID][0]!="0") return 'gris';
}

// JAVASCRIPT DE LA PESTAÑA DOCUMENTOS

var tabDoc = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDoc'));

tabDoc.addColumna('80','center',' <%= descriptor.getDescripcion("gEtiqAportadoAlta")%>');

if(mostrarDigitalizar) {
    tabDoc.addColumna('350','left',' <%= descriptor.getDescripcion("gEtiqRelacion")%>');
}else
    tabDoc.addColumna('500','left',' <%= descriptor.getDescripcion("gEtiqRelacion")%>');

tabDoc.addColumna('100','center','<%= descriptor.getDescripcion("gEtiq_tipo")%>');
tabDoc.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqFecha")%>');
if(mostrarDigitalizar) {
    tabDoc.addColumna('80','center','<%= descriptor.getDescripcion("etiqCompulsado")%>');
} else {
	tabDoc.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqCotejado")%>');
}
tabDoc.addColumna('0','center',''); // Columna oculta para guardar el contenido del documento
if(mostrarDigitalizar) {
    tabDoc.addColumna('230','left','<%= descriptor.getDescripcion("etiqTipoDocumental")%>');
}
tabDoc.addColumna('0','center',''); // Columna oculta para guardar el id de documento
tabDoc.displayCabecera=true;
tabDoc.displayTabla();



var tabAnt = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAnt'));

tabAnt.addColumna('80','center','Tipo de Documento');
tabAnt.addColumna('500','left','Nombre de Documento');
tabAnt.addColumna('100','center','Órgano');
tabAnt.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqFecha")%>');
tabAnt.addColumna('0','center','');
tabAnt.displayCabecera=true;
tabAnt.displayTabla();
agregarLinkDocumentosAportados();
function callFromTableTo(rowID,tableName){
    if (tabFormularios.id == tableName){
        window.open("<html:rewrite page='/AbrirPDFFormulario'/>?codigo=" + listaFormularios[rowID][0] + "&opcion=imprimir","ventanaForm");
    }
}

function callFromTableTo(rowID,tableName){
    if (tabFormularios.id == tableName){
        window.open("<html:rewrite page='/AbrirPDFFormulario'/>?codigo=" + listaFormularios[rowID][0] + "&opcion=imprimir","ventanaForm");
    }
}

function compruebaTipoDoc(codigo,desc){

    var existe = false;

    for (var i=0; i<cod_tiposDocumentosAux.length; i++) {
        if (cod_tiposDocumentosAux[i] == codigo){
            existe = true;
        }            	
    }    
    
    if(codigo=='') document.getElementById("capaTipoDocNoexiste").style.visibility = 'hidden';
    else{
        if (existe == true){
            document.getElementById("capaTipoDocNoexiste").style.visibility = 'hidden';
        }else{
            document.getElementById("capaTipoDocNoexiste").style.visibility = 'visible';
            document.getElementById("descDocNoVal").title = '<%=descriptor.getDescripcion("gEtiq_TipoDocNoExst1")%>' + " " + desc + " " + '<%=descriptor.getDescripcion("gEtiq_TipoDocNoExst2")%>'
            document.forms[0].txtCodigoDocumento.value = "";
            document.forms[0].txtNomeDocumento.value = "";
        }
    }
}
	


document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = evento.which;
    var coordx = evento.clientX;
    var coordy = evento.clientY;

    if('Alt+C'==tecla)
    {
      if(document.getElementById('capaBotones3').style.display=='')
          pulsarSalirConsultar();
      <% if (permisoMantenimiento) {%>
      else if(document.getElementById('capaBotones2').style.display=='')
          pulsarCancelarAlta();
      else if(document.getElementById('capaBotones5').style.display=='')
          pulsarCancelarDuplicar();
      else if(document.getElementById('capaBotones4').style.display=='')
          pulsarCancelarModificar();
      <% }%>
    }
    <% if (permisoMantenimiento) {%>
    else if('Alt+A'==tecla) {
      if(document.getElementById('capaBotonesConsulta').style.display=='')
          pulsarAlta();
      else  if(document.getElementById('capaBotones3').style.display=='')
          if (!document.forms[0].cmdAnular.disabled) pulsarAnular();
    }
    else if('Alt+G'==tecla) {
      if(document.getElementById('capaBotones4').style.display=='')
          comprobarCodProcedimientoValido('Mod');
      else if(document.getElementById('capaBotones2').style.display=='')
          comprobarCodProcedimientoValido('Alta');
      else  if(document.getElementById('capaBotones5').style.display=='')
          comprobarCodProcedimientoValido('Dup');
    }
    else if('Alt+M'==tecla){
      if(document.getElementById('capaBotones3').style.display=='')
          if (!document.forms[0].cmdModificar.disabled) pulsarModificar();
    }
    else if('Alt+B'==tecla){
      if(document.getElementById('capaBotonesConsulta').style.display=='')
          pulsarConsultar();
    }
    else if('Alt+D'==tecla) {
      if(document.getElementById('capaBotones3').style.display=='')
          pulsarDuplicar();
    }
    else if('Alt+O'==tecla) {
      <% if (permisoMantenimientoSalida) {
             if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>
                     if(document.getElementById('capaBotones3').style.display=='')
                         pulsarContestar();
             <% }
      } %>
     }
     <% } %>
     else if('Alt+L'==tecla) {
         if(document.getElementById('capaBotonesConsulta').style.display=='')
             pulsarLimpiar();
     }

    if((layerVisible)||(divSegundoPlano)) buscar(evento);

    keyDel(evento);

    //if (event.keyCode == 9){
    if (teclaAuxiliar == 9){//Tab


        if(layerVisible) ocultarDiv();
        if(divSegundoPlano) divSegundoPlano = false;
        comboAsuntos.ocultar();
        comboRol.ocultar();
        if(IsCalendarVisible) hideCalendar();
        return false;
    }
    if (teclaAuxiliar == 1){//Clic

      if(layerVisible) setTimeout('ocultarDivNoFocus()',40);


      if (comboRol.base.style.display !== 'none' && isClickOutCombo(comboRol,coordx,coordy)) setTimeout('comboRol.ocultar()',20);

      if(IsCalendarVisible) replegarCalendarioGeneral(evento);

      if(divSegundoPlano)	divSegundoPlano = false;

    }
    if(teclaAuxiliar == 13){//Enter	
            var elTxtDNI = $("input[name='txtDNI']");	
            if(elTxtDNI.is(":focus") && !elTxtDNI.is(":disabled") && elTxtDNI.prop("readonly")==false){	
              buscarDocTipoDoc();	
           }	
        }

    if(evento.button == 1){

        if(layerVisible) setTimeout('ocultarDiv()',40);
        if(divSegundoPlano)	divSegundoPlano = false;
    }
    if(evento.button == 9){    

        comboAsuntos.ocultar();
        comboRol.ocultar();
        return false;
    }
}
    if(window.screen.availWidth <= 1024){
          window.parent.document.body.style.zoom="99%"; 
	  onClickmostrarMenos();
          var textArea = document.getElementById ("observaciones");
          textArea.rows = 3;
          var textArea2 = document.getElementById ("asunto");
          textArea2.rows = 3;
    }
    
    function mostrarMensaje(codMensaje){
         if(codMensaje="1"){
            jsp_alerta('A','<%=descriptor.getDescripcion("noImprimirJustificante")%>');
         }
    }
$(function(){
    if($("[name=cbTipoEntrada]").val()=="" ){
        $("[name=cbTipoEntrada]").val("0");
        actualizarDescripcion("cbTipoEntrada","txtNomeTipoEntrada",cod_tipoEntrada,desc_tipoEntrada);
    }
});

</script>
    </BODY>
</html:html>