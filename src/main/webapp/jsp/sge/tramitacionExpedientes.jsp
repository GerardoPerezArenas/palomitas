<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject" %>
<%@page import="java.util.ResourceBundle"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.technical.EstructuraCampo"%>
<%@ page import="java.util.Collection"%>
<%@ page import="es.altia.agora.technical.CamposFormulario"%>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DatosPantallaModuloVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TareasPendientesInicioTramiteVO"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject"%>

<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>
<%Log mLog = LogFactory.getLog(this.getClass().getName());
  String fechaFinExpediente = "";
  String nombre_campo = "";
  String nombre_campo_descripcion = "";
  String nombre_campo_aux = "";
  String nombre_campo_carga = "";
  String hayInteresadosAdmiten = "0";
  boolean existenCamposCalculados=false;
  int idioma=1;
  int apl=1;
  int munic = 0;
  String[] params=null;
  TramitacionExpedientesForm tramExpForm=new TramitacionExpedientesForm();
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      tramExpForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
          params = usuario.getParamsCon();
          
          if(apl==1)
              apl=4;          
        }
        
  }
    
    String PORTAFIRMAS  = "";
    try{
        ResourceBundle portafirmas = ResourceBundle.getBundle("Portafirmas");
        PORTAFIRMAS  = portafirmas.getString(munic+"/Portafirmas");
    }catch(Exception e){
      PORTAFIRMAS = "";
    }
    
  fechaFinExpediente = tramExpForm.getFechaFin();
  mLog.debug("FECHA FIN =" + fechaFinExpediente + "-----");
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String campos="";
    
    tramExpForm.setDesdeExpediente(false);
    
     String macroPlantillas = m_Config.getString("macroPlantillas");
  String hostVirtual = m_Config.getString("hostVirtual");
     String dominioFinal = "";
    if(hostVirtual==null || hostVirtual.length()==0) {
        dominioFinal = StrutsUtilOperations.getProtocol(request) +"://" + macroPlantillas + request.getContextPath();
    } else {
        dominioFinal = hostVirtual+ request.getContextPath();
    }
    
/************* Se comprueba cual es el navegador utilizado por el usuario ******************/
String userAgent = request.getHeader("user-agent");
hayInteresadosAdmiten = (String) request.getAttribute("hayInteresadosAdmitenNotif");

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<html:html><head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES  Tramitación de Expedientes :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/ui-lightness/jquery-ui-1.10.2.custom.min.css'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tramitacionExpedientes.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/vbscript" src="<c:url value='/scripts/documentos.vbs'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-ui-1.10.2.custom.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>

<style type="text/css">
html>body .etiquetaPlazoMaximo {
  font-family: Verdana, Arial, Helvetica, Sans-serif;
  font-size: 11px;
  font-style:normal;
  font-weight: normal;
  color: #0F2667;
  nowrap:true;
  width:158px;
}

</style>
<SCRIPT type="text/javascript">    
    
var TEXTOS_I18_EDITOR_TEXTO_NULL = '<%=descriptor.getDescripcion("etiqNoEditable")%>';
var TEXTOS_I18_ERROR_INTERNO     = '<%=descriptor.getDescripcion("msjErrorInterno")%>';

var listaDocumentos = new Array();
var listaDocumentosOriginal = new Array();
var listaCodDocumentosTramite = new Array();
var listaEnlaces = new Array();
var listaCodInteresados = "";
var listaVersInteresados = "";
var listaCodUorsRegistro = new Array();
var nombreCombos = new Array();
var cont = 0;
var cont1 = 0;
var cont2 = 0;

/**** Tareas pendientes ****/
 var tieneTareasPendientesInicio = <%=tramExpForm.tieneTareasPendientesInicio()%>;
/**** Tareas pendientes ****/

var esInteresadoObligatorio = "<%=tramExpForm.isInteresadoObligatorio()%>";

// #212448
// indica si el trámite a iniciar se cargará de forma directa
var cargaDirecta = false;
// codigo de trámite   que se abren al finalizar (cuando se abre solo uno)
var codTramAbrir = null;

// #289948: Variable global que indica si la finalizacion se hace después del envio de notificación
var postNotificacionEnviada = false;

function mostrarMensajeError(codigoError) {
    switch (codigoError) {
        case TRAMITE_TIPO_ERROR_CREAR_CSV_DOCUMENTO:
            jsp_alerta('A', '<%=descriptor.getDescripcion("errorCrearCSVDoc")%>');
            break;
        case TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE:
            jsp_alerta('A', '<%=descriptor.getDescripcion("errorCSVDocYaExiste")%>');
            break;
        case TRAMITE_TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO:
            jsp_alerta('A', '<%=descriptor.getDescripcion("errorCSVFormatoNoSoportado")%>');
            break;
        case TRAMITE_TIPO_ERROR_CSV_CODIGO_DESCONOCIDO:
            jsp_alerta('A', '<%=descriptor.getDescripcion("errCodigoDesconocidoCampoSup")%>');
            break;
        case TRAMITE_TIPO_OK_CSV_GENERADO_CORRECTO:
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjCSVGeneradoOK")%>'+ ". "+'<%=descriptor.getDescripcion("msjTramGrabarAutomat")%>');
            pulsarGrabar(); 
            break;
    }

    pleaseWait('off');
}

function marcarEliminadoCampoSuplementarioFichero(codigo,origen,msgErr1,msgErr2) {   
    var exito =  false;
    if(codigo==null || origen==null || codigo=="" || origen==""){
        jsp_alerta("A",msgErr1);        
    }else{        
        var ajax = getXMLHttpRequest();
        
        if(ajax!=null){
            
          var codTramite = document.forms[0].codTramite.value;
          var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
          
          var url =APP_CONTEXT_PATH + "/EliminarDocumentoCampoSuplementarioFichero.do";          
          var parametros = "&codigoCampo=" + codigo + "&origen=TRAMITE&codTramite=" + codTramite + "&ocurrenciaTramite=" + ocurrenciaTramite;
          ajax.open("POST",url,false);
          ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
          ajax.setRequestHeader("Accept", "text/html, application/xml, text/plain");
          ajax.send(parametros);
          
          try{
              
              if (ajax.readyState==4 && ajax.status==200){                  									
                  var text = ajax.responseText;											
                  var datosRespuesta = text.split("=");
                  
                  if(datosRespuesta!=null && datosRespuesta.length==2 && datosRespuesta[0].trim()=="exito" && datosRespuesta[1].trim()=="1"){                        
                        exito = true;
                  }else
                      jsp_alerta("A",msgErr2);
              }
          }catch(Err){
              alert("Error: " + Err.description);
          }
          
      }
            
      return exito;
    }
}

<c:set var="JS_DEBUG_LEVEL" value="0" />
function getEstadoFirmaVisual( codigoDocumento, codigoEstadoFirma ) {
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.getEstadoFirmaVisual("+codigoDocumento+","+codigoEstadoFirma+") BEGIN");
    </c:if>
    var result = "";
    <% if (m_Config.getString("aytos.firmadoc").equalsIgnoreCase("si")) {%>
    // Si esta activado el servicio Web de FirmaDoc
    if ( (!codigoEstadoFirma) || (codigoEstadoFirma=='') ) result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.Null'/>";
    if ( (codigoDocumento) && (codigoEstadoFirma) ) {
        if (codigoEstadoFirma == '-1') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.-1'/>";
        } else if (codigoEstadoFirma == '0') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.0'/>";
        } else if (codigoEstadoFirma == '1') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.1'/>";
        } else if (codigoEstadoFirma == '2') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.2'/>";
        } else if (codigoEstadoFirma == '3') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.3'/>";
        } else if (codigoEstadoFirma == '4') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.4'/>";
        } else if (codigoEstadoFirma == '5') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.5'/>";
        } else if (codigoEstadoFirma == '6') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.Accede.EstadoFirma.6'/>";
        } else {
            result = codigoEstadoFirma;
        }
    }

    <% } else { %>
    // Si no esta activado el servicio Web de FirmaDoc.
    if ( (!codigoEstadoFirma) || (codigoEstadoFirma=='') ) result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Null'/>";
    var portafirmas = "<%=PORTAFIRMAS%>";
    if ( (codigoDocumento) && (codigoEstadoFirma) ) {
        var paramCodigoDocumento=''+codigoDocumento+'';
        if (codigoEstadoFirma == 'E') {
            if (""!=portafirmas) {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.E'/> - <a href='javascript:enviarDocumentoAFirma("+paramCodigoDocumento+", \"E\")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarFirma'/></a>";
            } else {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.E'/>";
            }
        } else if (codigoEstadoFirma == 'O') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.O'/> - <a href='javascript:verDatosFirmaDocumentoSegunTipo("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.VerDatos'/></a>";
        } else if (codigoEstadoFirma == 'T') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.T'/> - <a href='javascript:iniciarFirmaDocumento("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Firmar'/></a>";
        } else if (codigoEstadoFirma == 'L') {
            if (""!=portafirmas) {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.L'/> - <a href='javascript:enviarDocumentoAFirma("+paramCodigoDocumento+",\"L\")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarPortaFirma'/></a>";
            } else {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.L'/>";
            }
        } else if (codigoEstadoFirma == 'U') {
            if (""!=portafirmas) {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.U'/> - <a href='javascript:enviarDocumentoAFirma("+paramCodigoDocumento+",\"U\")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarPortaFirma'/></a>";
            } else {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.U'/>";
            }
        } else if (codigoEstadoFirma == 'F') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.F'/> - <a href='javascript:verDatosFirmaDocumentoSegunTipo("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.VerDatos'/></a>";
        } else if (codigoEstadoFirma == 'R') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.R'/> - <a href='javascript:verDatosFirmaDocumentoSegunTipo("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.VerDatos'/></a>";
        } else if (codigoEstadoFirma == 'M') {
            if (""!=portafirmas) {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.M'/> - <a href='javascript:enviarDocumentoAFirma("+paramCodigoDocumento+",\"M\")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.ReenviarPortaFirma'/></a>";
            } else {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.M'/>";
            }
        } else if (codigoEstadoFirma == 'V') {
            if (""!=portafirmas) {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.V'/> - <a href='javascript:enviarDocumentoAFirma("+paramCodigoDocumento+",\"V\")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.ReenviarPortaFirma'/></a>";
            } else {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.V'/>";
            }
        } else {
            result = codigoEstadoFirma;
        }
    }
    <% } %>

    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.getEstadoFirmaVisual("+codigoDocumento+","+codigoEstadoFirma+")="+result+" END");
    </c:if>
    return result;
}
function enviarDocumentoAFirma( codigoDocumento, codigoEstadoFirma ) { 
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.enviarDocumentoAFirma("+codigoDocumento+", "+codigoEstadoFirma+") BEGIN");
    </c:if>
    if (codigoEstadoFirma === 'E' || !codigoEstadoFirma) {
    if(jsp_alerta("C",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarFirma.EstaSeguro'/>')) {
            enviarAFirmar(codigoDocumento, "");
        }
    } else if (codigoEstadoFirma === 'L' || codigoEstadoFirma === 'M' || codigoEstadoFirma === 'U' || codigoEstadoFirma === 'V') {
        if(jsp_alerta("C",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarPortaFirma.EstaSeguro'/>')) {
            if (codigoEstadoFirma === 'L' || codigoEstadoFirma === 'M') {
                obtenerFlujoFirmaDefecto(codigoDocumento);
            } else {
                obtenerUsuarioFirmaDefecto(codigoDocumento);
            }
        }
    }
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.enviarDocumentoAFirma() END");
    </c:if>
}

// Personalizar el flujo y el circuito de firmas para este documento
function enviarDocumentoAFirmaFlujo(mapaParams) {
    pleaseWait('on');
    
    if (mapaParams && mapaParams.codDocumento && mapaParams.firmaFlujoUsuarios
            && mapaParams.firmaFlujoUsuarios.flujo && mapaParams.firmaFlujoUsuarios.usuariosCircuito) {
        var codigoDocumento = mapaParams.codDocumento;
        var flujo = mapaParams.firmaFlujoUsuarios.flujo;
            
        // Si se acepta, se tomara el flujo por defecto, de lo contrario se
        // muestra la pantalla para personalizar el flujo
        var textoModificarFlujo = '<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarFirma.ModificarFlujo'/>';
        textoModificarFlujo = textoModificarFlujo.replace("%FLUJO%", flujo.nombre);

        if(!jsp_alerta("C", textoModificarFlujo)) { // Flujo personalizado
            var source = "<c:url value='/sge/DefinicionFlujosFirma.do?opcion=cargarPantallaDefinicionCircuitoFirmaPersonalizada'/>";
            source += '&idFlujo=' + flujo.id;

            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                                null, 'width=700,height=450,status='+ '<%=statusBar%>',
                                function(respuesta){
                                    if (respuesta) {
                                        var params = {
                                            codDocumento: codigoDocumento,
                                            datosFlujo: JSON.parse(respuesta)
                                        };
                                        
                                        prepararEnvioFlujoFirmaTramitePersonalizado(params);
                                    } else {
                                        pleaseWait('off');
                                    }
                                });
        } else { // Flujo por defecto
            var params = {
                codDocumento: codigoDocumento,
                datosFlujo: mapaParams.firmaFlujoUsuarios
            };

    prepararEnvioFlujoFirmaTramitePersonalizado(params);
        }
    } else {
        jsp_alerta('A', TEXTOS_I18_ERROR_INTERNO);
        pleaseWait('off');
    }
}

// Personalizar el usuario firmante para este documento
function enviarDocumentoAFirmaUsuario(mapaParams) {
    pleaseWait('on');

    if (mapaParams && mapaParams.codDocumento && mapaParams.usuarioFirmante) {
        var codigoDocumento = mapaParams.codDocumento;
        var usuario = mapaParams.usuarioFirmante;
            
        // Si se acepta, se tomara el usuario por defecto, de lo contrario se
        // muestra la pantalla para personalizarlo
        var textoModificarUsuario = '<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarFirma.ModificarUsuario'/>';
        textoModificarUsuario = textoModificarUsuario.replace("%USUARIO%", usuario.nombre);

        if(!jsp_alerta("C", textoModificarUsuario)) { // Usuario personalizado
            var source = "<c:url value='/sge/DefinicionFlujosFirma.do?opcion=cargarPantallaEleccionUsuarioFirmante'/>";
            var args = null;
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                                args, 'width=700,height=550,status='+ '<%=statusBar%>',
                                function(respuesta){
                                    if (respuesta) {
                                        var params = {
                                            codDocumento: codigoDocumento,
                                            datosUsuario: respuesta
                                        };
                                        
                                        prepararEnvioUsuarioFirmaTramitePersonalizado(params);
                                    } else {
                                        pleaseWait('off');
                                    }
                                });
        } else { // Usuario por defecto
            var params = {
                codDocumento: codigoDocumento,
                datosUsuario: usuario
            };
            
            prepararEnvioUsuarioFirmaTramitePersonalizado(params);
        }
    } else {
        jsp_alerta('A', TEXTOS_I18_ERROR_INTERNO);
        pleaseWait('off');
    }
}

function enviarAFirmar(codigoDocumento, codigoDocumentoAnterior) {
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.enviarAFirmar() ENVIANDO DOCUMENTO A FIRMA!");
    </c:if>
    //bloqueamos la pantalla antes de iniciar cambiarEstado
    pleaseWaitCustom('on','<%=descriptor.getDescripcion("msjProcesoLento")%>','<%=descriptor.getDescripcion("msjCargDatos")%>',450);
    document.forms[0].codDocumento.value = ""+codigoDocumento+"___"+codigoDocumentoAnterior+"";
    document.forms[0].estadoFirma.value = 'O';
    document.forms[0].opcion.value="cambiarEstadoFirmaDocumentoCRD";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
    document.forms[0].submit();
}

function iniciarFirmaDocumento( codigoDocumento ) {
    <c:if test="${JS_DEBUG_LEVEL >= 80}">
    alert("TramitacionExpedientes.iniciarFirmaDocumento("+codigoDocumento+") BEGIN");
    </c:if>
    if(jsp_alerta("C",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Firmar.EstaSeguro'/>')) {
        var finalURL = '<c:url value="/sge/documentofirma/PrepareFirmaDocumentoTramitacion.do?"/>';
        finalURL = finalURL + "idMunicipio="+document.forms[0].codMunicipio.value;
        finalURL = finalURL + "&idProcedimiento="+document.forms[0].codProcedimiento.value;
        finalURL = finalURL + "&idEjercicio="+document.forms[0].ejercicio.value;
        finalURL = finalURL + "&idNumeroExpediente="+document.forms[0].numeroExpediente.value;
        finalURL = finalURL + "&idTramite="+document.forms[0].codTramite.value;
        finalURL = finalURL + "&idOcurrenciaTramite="+document.forms[0].ocurrenciaTramite.value;
        finalURL = finalURL + "&idNumeroDocumento="+codigoDocumento;
        finalURL = finalURL + "&tipoDocumento=0";
        <c:if test="${JS_DEBUG_LEVEL >= 80}">
        alert("TramitacionExpedientes.iniciarFirmaDocumento() finalURL="+finalURL);
        </c:if>


        var opt ='unadorned:yes;resizable:1;dialogHeight:300px;dialogwidth:640px;scroll:no;status=no;resizable:no;';
        
        abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + finalURL ,window,
	'width=640,height=300,scrollbars=no,status='+ '<%=statusBar%>',function(result){
                            if (result != undefined) 
                                    setPopUpFirmaPlantillaResult(result[0], result[1]);
                            cargaDocumentos();
                    });
    }
    <c:if test="${JS_DEBUG_LEVEL >= 80}">
    alert("TramitacionExpedientes.iniciarFirmaDocumento() END");
    </c:if>
}

// Ver la firma de los tipo T y O
function verDatosFirmaDocumento( codigoDocumento ) {
    <c:if test="${JS_DEBUG_LEVEL >= 80}">
    alert("TramitacionExpedientes.verDatosFirmaDocumento("+codigoDocumento+") BEGIN");
    </c:if>
    var finalURL = '<c:url value="/sge/documentofirma/ViewDocumentoFirma.do?"/>';
    finalURL = finalURL + "idMunicipio="+document.forms[0].codMunicipio.value;
    finalURL = finalURL + "&idProcedimiento="+document.forms[0].codProcedimiento.value;
    finalURL = finalURL + "&idEjercicio="+document.forms[0].ejercicio.value;
    finalURL = finalURL + "&idNumeroExpediente="+document.forms[0].numeroExpediente.value;
    finalURL = finalURL + "&idTramite="+document.forms[0].codTramite.value;
    finalURL = finalURL + "&idOcurrenciaTramite="+document.forms[0].ocurrenciaTramite.value;
    finalURL = finalURL + "&idNumeroDocumento="+codigoDocumento;
    finalURL = finalURL + "&tipoDocumento=0";
    finalURL = finalURL + "&contador="+ Math.random();
    <c:if test="${JS_DEBUG_LEVEL >= 80}">
    alert("TramitacionExpedientes.verDatosFirmaDocumento() finalURL="+finalURL);
    </c:if>
    abrirXanelaAuxiliar(finalURL, null,'width='+650+',height='+350+',status=no,resizable=no,scrollbars=no',function(){
            <c:if test="${JS_DEBUG_LEVEL >= 80}">
            alert("TramitacionExpedientes.verDatosFirmaDocumento() END");
            </c:if>
        });
}
function callbackCambioEstadoDocumento(respOpcion, codigoDocumento, nuevoEstadoFirma) {
    pleaseWaitCustom('off',false,'<%=descriptor.getDescripcion("msjCargDatos")%>');
  if(respOpcion == "cambiadoEstadoFirmaDocumentoCRD") {
      
    actualizarEstadoFirmatabla(codigoDocumento, nuevoEstadoFirma);
    jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Cambio.Hecho'/>');
  } else if (respOpcion == "cambiadoEstadoFirmaDocumentoCRDSinAlerta") {
    actualizarEstadoFirmatabla(codigoDocumento, nuevoEstadoFirma);
  }else if (respOpcion == "cambiadoEstadoFirmaDocumentoCRDNoEnviadoMail") {
    actualizarEstadoFirmatabla(codigoDocumento, nuevoEstadoFirma);
    jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Cambio.Hecho.No.Mail'/>');
  } else {
    jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Cambio.NoHecho'/>');
  }
}


function actualizarEstadoFirmatabla(codigoDocumento, nuevoEstadoFirma) {
    var ccedi = 0;
      for (ccedi=0; ccedi < listaDocumentosOriginal.length; ccedi++) {
          if (listaDocumentosOriginal[ccedi][0]==codigoDocumento) {
              listaDocumentos[ccedi][5]= getEstadoFirmaVisual(codigoDocumento,nuevoEstadoFirma);
              listaDocumentosOriginal[ccedi][7]=nuevoEstadoFirma;
          }
      }
      tab.lineas=listaDocumentos;
      refresca();
}

function comprobarFirmasAntesFinalizacionTramite() {
    var i = 0;
    var estadoDocumento = '';
    for (i = 0; i < listaDocumentosOriginal.length; i++) {
        estadoDocumento = listaDocumentosOriginal[i][7];
        if ( (estadoDocumento) && (estadoDocumento!='') ) estadoDocumento = estadoDocumento.toUpperCase();
        if ( (estadoDocumento=='E') || (estadoDocumento=='O') || (estadoDocumento=='T') || (estadoDocumento=='L') || (estadoDocumento=='U') ) <%-- || (estadoDocumento=='R') ) --%>
            return false;
    }
    return true;
}

function newActiveXObject(clsname) {
    try {
        return new ActiveXObject(clsname);
    } catch (jsEx) {
        jsp_alerta('A','<fmt:message key="ErrorMessages.Word.NotOpen"/>',APP_TITLE);
        return null;
    }
}

function wordOpenDocument(oWord, docUrl) {
    try {
        return oWord.Documents.Open(docUrl);
    } catch (jsEx) {
        jsp_alerta('A', '<fmt:message key="ErrorMessages.Document.NotOpen"/>',APP_TITLE);
        oWord.Quit();
        return null;
    }
}
<jsp:include page="/jsp/portafirmas/documentoportafirmas/jsGetUrlDocumentoPortafirmas.jsp"/>

function pulsarVerDoc() {
  var i = tab.selectedIndex;
  if(i != -1) {
      var expHistorico = <%=tramExpForm.isExpHistorico()%>;

      var extension = getFilenameExtension(listaDocumentosOriginal[i][1]).toUpperCase();
      var codMunicipio = document.forms[0].codMunicipio.value;
      var numero = document.forms[0].numeroExpediente.value;
      var codTramite = document.forms[0].codTramite.value;
      var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
      var numeroDocumento = listaDocumentosOriginal[i][0];

      // Construimos el codigo para el servlet de visualización de documentos:
      // <extension>-<codMunicipio>-<numExpediente>-<codTramite>-<ocuTramite>-<numDocumento>
      var codigo = extension+"-"+codMunicipio+"-"+numero+"-"+codTramite+"-"+ocurrenciaTramite+"-"+numeroDocumento;

      var url = "<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios;jsessionid=" + '<%=session.getId()%>' +
          "?codigo=" + codigo + "&opcion=7" + "&expHistorico="+expHistorico + "&embedded=false";
  
      window.open(url, "oculto",
                "left=10, top=10, width=1, height=1, scrollbars=no, menubar=no, location=no, resizable=no");
    } else {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }
}

function inicializar() {  
  document.forms[0].desdeConsulta.value = "<%=request.getParameter("desdeConsulta")%>";
  document.forms[0].desdeExpRel.value = "<%=request.getParameter("desdeExpRel")%>";
  document.forms[0].porCampoSup.value="<%=request.getParameter("porCampoSup")%>";
  
  if("<%=hayInteresadosAdmiten%>"!=null && "<%=hayInteresadosAdmiten%>"=="1")
    document.forms[0].hayInteresadosNotifAutorizada.value=true;
  else
    document.forms[0].hayInteresadosNotifAutorizada.value=false;
    
  tp1.setSelectedIndex(0);
  if(document.forms[0].plazo.value != 0) {
    mostrarCapas('capaPlazoTramExp');
    if(document.forms[0].tipoPlazo.value == "N") {
      document.forms[0].tipoPlazo.value = "DÍAS NATURALES";
    } else if(document.forms[0].tipoPlazo.value == "H") {
      document.forms[0].tipoPlazo.value = "DÍAS HÁBILES";
    } else if(document.forms[0].tipoPlazo.value == "M") {
      document.forms[0].tipoPlazo.value = "MESES";
    }
  }
  if(document.forms[0].fechaFin.value == "" || document.forms[0].fechaFin.value == null) {
    activarFormulario();
  } else {
    desactivarFormulario();
    try{
        //document.getElementById("cmdVolver").disabled = false;
        if(document.getElementById("cmdVolver")!=null) document.getElementById("cmdVolver").disabled = false;
        
    }catch(Err){
        alert("Err.description: "  + Err.description);
     
    }
  }
  if(document.forms[0].permiso.value == "no") {
    desactivarFormulario();
    document.getElementById("cmdVolver").disabled = false;
  }

  //BLOQUEOS
  if (document.forms[0].bloqueo.value=="1") { //Bloqueado por el usuario
      deshabilitarBoton(document.forms[0].cmdBloquear);
  } else if (document.forms[0].bloqueo.value=="2") { //Bloqueado por otro usuario

      deshabilitarBoton(document.forms[0].cmdBloquear);
      deshabilitarBoton(document.forms[0].cmdDesbloquear);
  } else { // No bloqueado
      deshabilitarBoton(document.forms[0].cmdDesbloquear);
  }

  <% int posicionDocumento =0; %>
  <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaDocumentos">
    var cadenaEditorTexto = '<bean:write name="elemento" property="editorTexto"/>';
    if (cadenaEditorTexto === '') {
        cadenaEditorTexto = TEXTOS_I18_EDITOR_TEXTO_NULL;
    }
    listaDocumentos[cont] = ['<bean:write name="elemento" property="descDocumento" />',
                          '<bean:write name="elemento" property="fechaCreacion" />',
                          '<bean:write name="elemento" property="fechaModificacion"/>',
                          <c:if test="${elemento.fechaInforme ne ' '}">
                          '<bean:write name="elemento" property="fechaInforme"/>&nbsp;<a href="javascript:modificarFechaInforme(<c:out value='${elemento.codDocumento}'/>,<%=posicionDocumento%>);"><span class="fa fa-calendar" aria-hidden="true" title="<%=descriptor.getDescripcion("etiq_modFecInformeDoc")%>"/></span></a>',
                          </c:if>
                          <c:if test="${elemento.fechaInforme eq ' '}">
                          '<a href="javascript:modificarFechaInforme(<c:out value='${elemento.codDocumento}'/>,<%=posicionDocumento%>);"><span class="fa fa-calendar" aria-hidden="true" title="<%=descriptor.getDescripcion("etiq_modFecInformeDoc")%>" ></span></a>',
                          </c:if>
                          '<bean:write name="elemento" property="usuario"/>',
                          getEstadoFirmaVisual('<bean:write name="elemento" property="codDocumento" />','<bean:write name="elemento" property="estadoFirma"/>'),
                          cadenaEditorTexto
                            ];
    listaDocumentosOriginal[cont] = ['<bean:write name="elemento" property="codDocumento" />',
                            '<bean:write name="elemento" property="descDocumento" />',
                          '<bean:write name="elemento" property="fechaCreacion" />',
                          '<bean:write name="elemento" property="fechaModificacion"/>',
                          '<bean:write name="elemento" property="fechaInforme"/>',
                          '<bean:write name="elemento" property="usuario"/>',
                          '<bean:write name="elemento" property="interesado"/>',
                          '<bean:write name="elemento" property="estadoFirma"/>',
                          '<bean:write name="elemento" property="relacion"/>',
                          '<bean:write name="elemento" property="editorTexto"/>'
                            ];           
    cont++;
    <%  posicionDocumento++; %>
  </logic:iterate>

    tab.lineas=listaDocumentos;
    refresca();
  <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaCodDocumentosTramite">
    listaCodDocumentosTramite[cont1] = ['<bean:write name="elemento" property="codDocumento" />'];
    cont1++;
  </logic:iterate>
  <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaEnlaces">
  	<logic:equal name="elemento" property="estadoEnlace" value="1" >
    	listaEnlaces[cont2] = ['<bean:write name="elemento" property="descEnlace" />',
                           '<bean:write name="elemento" property="url" />'];
    	cont2++;
    </logic:equal>
  </logic:iterate>
  if((document.forms[0].modoConsulta.value == "si") || (document.forms[0].bloqueo.value=="2")) {

    desactivarFormulario();
    document.getElementById("cmdVolver").style.cursor = "hand";
    document.getElementById("cmdVolver").disabled = false;
  }
  refresca();    

<% if (tramExpForm.getMostrarFormsPDF()) {%>
  //Inicializar la lista de Formularios PDF
  inicializarFormsPDF();
<%}%>

  var observ = "";
  <% if (tramExpForm.getObservaciones()!=null){%>
    observ = unescape("<%=StringEscapeUtils.escapeJavaScript(StringEscapeUtils.unescapeJava(tramExpForm.getObservaciones()))%>");
  <%}%>
  if (observ != "")
  {
  document.forms[0].observaciones.value = observ;
  }
  domlay('capaEnlaces',1,0,0,enlaces());
  var instrucc = unescape('<bean:write name="TramitacionExpedientesForm" property="instrucciones"/>');
  if (instrucc != ""){
    document.forms[0].instrucciones.value = instrucc;
  }
  inicializarBotonesDatosSuplementarios();

 compruebaModificadoTramitacion();
 
   // Cargar lista de uors de registro
  <logic:present name="TramitacionExpedientesForm" property="uorsDeRegistro">
    var i=0;
    <logic:iterate id="uor" name="TramitacionExpedientesForm" property="uorsDeRegistro">
      listaCodUorsRegistro[i] = <bean:write name="uor" property="uor_cod"/>;
      i++;
    </logic:iterate>
  </logic:present>
  
  <%
    TramitacionExpedientesValueObject tramitacionDocumentos = (TramitacionExpedientesValueObject) tramExpForm.getTramitacionExpedientes();
    int longitudPlantillas = (tramitacionDocumentos.getListaCodDocumentosTramite()).size();
    int longitudDocumentos = (tramitacionDocumentos.getListaDocumentos()).size();    
        %>
        if (0=='<%=longitudPlantillas%>') deshabilitarBoton(document.forms[0].cmdAltaDoc);
        if (0=='<%=longitudDocumentos%>') {
            deshabilitarBoton(document.forms[0].cmdVerDoc);
            deshabilitarBoton(document.forms[0].cmdModificarDoc);
            deshabilitarBoton(document.forms[0].cmdEliminarDoc);
            <%if ("si".equals(m_Config.getString("JSP.BotonCSV"))){%>
            deshabilitarBoton(document.forms[0].cmdCSVDoc);
            <%}%>
        }
  
    <c:if test="${sessionScope.TramitacionExpedientesForm.bloquearPlazos}">
        deshabilitarImagenCal("calFechaInicioPlazo",true);
        deshabilitarImagenCal("calFechaFinPlazo",true);
        document.forms[0].fechaInicioPlazo.readOnly = true;
        document.forms[0].fechaInicioPlazo.className = "inputTextoObligatorio";
        document.forms[0].fechaLimite.readOnly = true;
        document.forms[0].fechaLimite.className = "inputTextoObligatorio";
        document.forms[0].fechaFinPlazo.readOnly = true;
        document.forms[0].fechaFinPlazo.style.visibility="hidden";
        document.getElementById("calFechaFinPlazo").style.cursor=false;
        document.getElementById("calFechaInicioPlazo").style.cursor=false;
        document.getElementById("calFechaInicioPlazo").style.display="none";
        document.getElementById("calFechaFinPlazo").style.display="none";
        document.getElementById("etiqFechaFin").style.visibility="hidden";
    </c:if>    
    
    /** LLAMADA A OPERACIONES JAVASCRIPTS DE PANTALLAS DE EXPEDIENTES DE MODULOS EXTERNOS **/
    var funcionesCargaModulo = new Array();
    var contador = 0;
    <%
      ArrayList<String> funciones = tramExpForm.getFuncionesJSModulosExternosAccederPantallaTramite();
      for(int cont = 0;funciones!=null && cont<funciones.size();cont++){
    %>
        funcionesCargaModulo[contador] = "<%=funciones.get(cont)%>";
        contador++;
    <%
      }// for
    %>
            
    for(i=0;funcionesCargaModulo!=null && i<funcionesCargaModulo.length;i++){    
        eval(funcionesCargaModulo[i] + "();");
    }  
}

function mostrarCalFechaInicioPlazo(evento) {
    if(window.event) evento = window.event;
  if (document.getElementById("calFechaInicioPlazo").className.indexOf("fa-calendar") != -1 )
    showCalendar('forms[0]','fechaInicioPlazo',null,null,null,'','calFechaInicioPlazo','',null,null,null,null,null,null,null,'calcularFechaLimite()',evento);
}

function mostrarCalFechaFinPlazo(evento) {
  if(window.event) evento = window.event;
  if (document.getElementById("calFechaFinPlazo").className.indexOf("fa-calendar") != -1 )
    showCalendar('forms[0]','fechaFinPlazo',null,null,null,'','calFechaFinPlazo','',null,null,null,null,null,null,null,'calcularFechaLimite()',evento);
}
function enlaces() {
  var htmlString = "";
  if (listaEnlaces.length > 0){
    htmlString += '<table border="0px" cellpadding="2" cellspacing="4" align="left" width="100%">';
    for(i=0; i < listaEnlaces.length; i++) {
      htmlString += '<tr><td width="100%" align="left" class="etiqueta">';
      htmlString += '<a href=' + listaEnlaces[i][1] + ' target=blank class=enlace><b>';
      htmlString += listaEnlaces[i][0] + '</font></b></a></td></tr>';
    }
    htmlString += '</table>';
  }
  return (htmlString);
}

function activarFormulario () {
    habilitarImagenCal("calFechaInicioPlazo",true);
    habilitarImagenCal("calFechaFinPlazo",true);
    habilitarDatos(document.forms[0]);    
}

function desactivarFormulario() {

  deshabilitarImagenCal("calFechaInicioPlazo",true);
  deshabilitarImagenCal("calFechaFinPlazo",true);
  deshabilitarDatos(document.forms[0]);
  if (document.forms[0].cmdVerDoc != null) habilitarBoton(document.forms[0].cmdVerDoc);
  if (document.getElementById("cmdVolver") != null)  habilitarBoton(document.getElementById("cmdVolver"));
   

}

function pulsarVolver() {    
  pleaseWait('on');
  document.forms[0].opcion.value="cargarPestTram";
  document.forms[0].target="mainFrame";
  
  document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>" + "?expHistorico=<bean:write name='TramitacionExpedientesForm' property='expHistorico' scope='session'/>";
  document.forms[0].submit();
}

function pulsarAltaDoc() {        
    if ((getNumInteresadosExpediente()==0) &&(interesado_Obligatorio() == "1")){
	jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{  
        var source = "<c:url value='/sge/TramitacionExpedientes.do?opcion=listaDocumentosTramite'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
	'width=700,height=450,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                              document.forms[0].codDocumento.value = datosConsulta[0];
                              document.forms[0].codPlantilla.value = datosConsulta[1];
                              document.forms[0].nombreDocumento.value = datosConsulta[2];
                              document.forms[0].editorTexto.value=datosConsulta[4];

                              if(datosConsulta[3] == "S") {
                                if (document.forms[0].titular.value == "") {
                                    jsp_alerta("A", '<fmt:message key='Sge.TramitacionExpedientesForm.Interesado.Requerido'/>');
                                } else {
                                      var source1 = "<c:url value='/sge/TramitacionExpedientes.do?opcion=verInteresados&nCS='/>"+document.forms[0].numeroExpediente.value+
                                                    "&codMun=" + document.forms[0].codMunicipio.value + "&codProc=" + document.forms[0].codProcedimiento.value+
                                                    "&eje=" + document.forms[0].ejercicio.value;
                                      abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source1,'ventana',
                                            'width=700,height=450,status='+ '<%=statusBar%>',function(datosConsulta1){
                                                    if(datosConsulta1!=undefined){
                                                      listaCodInteresados = datosConsulta1[1];
                                                      listaVersInteresados = datosConsulta1[2];
                                                    }
                                                    if(listaCodInteresados.length !=0) {
                                                          document.forms[0].listaCodInteresados.value = listaCodInteresados;
                                                          document.forms[0].listaVersInteresados.value = listaVersInteresados;
                                                          document.forms[0].opcion.value="irAlAction";
                                                          document.forms[0].target="oculto";
                                                          document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";        
                                                          document.forms[0].submit();
                                                    }
                                                    listaCodInteresados = "";
                                                    listaVestInteresados = "";
                                            });
                                }
                            } else {
                                    document.forms[0].listaCodInteresados.value = listaCodInteresados;
                                    document.forms[0].listaVersInteresados.value = listaVersInteresados;
                                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                                    document.forms[0].opcion.value="irAlAction";
                                    document.forms[0].target="oculto";	    
                                    document.forms[0].submit();
                                    listaCodInteresados = "";
                                    listaVestInteresados = "";
                            }
                        }else{
                            listaCodInteresados = "";
                            listaVestInteresados = "";
                        }
                    });
    }
}
function editor() {
   
    if (document.forms[0].editorTexto.value=="WORD"){
  document.forms[0].opcion.value="altaDocumento";
        document.forms[0].target="oculto";}
     else if (document.forms[0].editorTexto.value=="ODT"){
  document.forms[0].opcion.value="altaDocumentoAdjunto";
        document.forms[0].target="oculto";
        pleaseWait('on');
    }
    else {
        document.forms[0].opcion.value="altaDocumentoOOffice";
        document.forms[0].target="oculto";}
  
  document.forms[0].action="<c:url value='/sge/DocumentosExpediente.do'/>";
  document.forms[0].submit();
  document.forms[0].listaCodInteresados.value = "";
  document.forms[0].listaVersInteresados.value = "";
}


function abrirODT(codigoPlantilla){  
  
   window.open("<%=request.getContextPath()%>/MostrarPlantillaODT?codigoPlantilla=" + codigoPlantilla+"&sessionid=" + "<%=session.getId()%>" ,  "ventana1",
            "left=10, top=10, width=1, height=1, scrollbars=no, menubar=no, location=no, resizable=yes");
            
   cargaDocumentos();
   pleaseWait('off');
    
}


function pulsarModificarDoc() {
  var i = tab.selectedIndex;  
  if(i != -1) {      
    if (listaDocumentosOriginal[i][9]==='') {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoEditable")%>");
    } else if (listaDocumentosOriginal[i][8]!='') {        
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoModifDocRel")%>");
    } else if (listaDocumentosOriginal[i][7]!='T'
            && listaDocumentosOriginal[i][7]!='E'
            && listaDocumentosOriginal[i][7]!='O'
            && listaDocumentosOriginal[i][7]!='L'
            && listaDocumentosOriginal[i][7]!='U'
            && listaDocumentosOriginal[i][7]!='M'
            && listaDocumentosOriginal[i][7]!='V'
            && listaDocumentosOriginal[i][7]!='') {        
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoModifDocFirmado")%>");        
    } else {
        if ( (listaDocumentosOriginal[i][6]!='F') &&
             (listaDocumentosOriginal[i][6]!='R') &&
             (listaDocumentosOriginal[i][6]!='O') ) {
            document.forms[0].numeroDocumento.value = listaDocumentosOriginal[i][0];
            document.forms[0].nombreDocumento.value = listaDocumentosOriginal[i][1];
            if (listaDocumentosOriginal[i][9]=="WORD"){
            document.forms[0].opcion.value="modificarDocumento";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/DocumentosExpediente.do'/>";
                document.forms[0].submit();
            }else if(listaDocumentosOriginal[i][9]=='OOFFICE'){
            
                document.forms[0].opcion.value="modificarDocumentoOOffice";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/DocumentosExpediente.do'/>";
                document.forms[0].submit();
            }else{
            
                var codProcedimiento = document.forms[0].codProcedimiento.value;
                var codMunicipio = document.forms[0].codMunicipio.value;
                var ejercicio = document.forms[0].ejercicio.value;
                var numero = document.forms[0].numero.value; 
                document.forms[0].numExpediente.value=numero;
                var codTramite = document.forms[0].codTramite.value;
                var ocurrencia = document.forms[0].ocurrenciaTramite.value;
                var codDocumento = document.forms[0].codDocumento.value;
                var nombreDocumento=listaDocumentosOriginal[i][1];
                var numeroDocumento=listaDocumentosOriginal[i][0];
                

                var parametros = "&codProcedimiento=" + codProcedimiento +"&codMunicipio=" + codMunicipio +"&ejercicio=" + ejercicio + "&numeroExpediente=" + numero +"&codTramite=" + codTramite + "&ocurrencia=" + ocurrencia + "&nombreDocumento="+nombreDocumento +"&numeroDocumento="+numeroDocumento+"&codDocumento="+codDocumento;
                if(parametros!=null){
                source = "<html:rewrite page='/sge/DocumentosExpediente.do'/>?opcion=modificarDocumentoODT"+parametros;      
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
                'width=500,height=500,status='+ '<%=statusBar%>',function(){
                    cargaDocumentos();
                });
                }
            }
        } else {
            jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.ModificarDocumento.EstadoFirmaIncompatible'/>');
        }
    }
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }
  listaCodInteresados = "";
  listaVestInteresados = "";
}
function cargaDocumentos() {
  document.forms[0].opcion.value="actualizarTablaDocumentos";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
  document.forms[0].submit();
}
function actualizaTabla(lista,lista2) {
  listaDocumentos = new Array();
  listaDocumentosOriginal = new Array();
  listaDocumentos = lista;
  listaDocumentosOriginal = lista2;
  tab.lineas=listaDocumentos;
  if (lista.length!=0) {
      habilitarBoton(document.forms[0].cmdVerDoc);
      habilitarBoton(document.forms[0].cmdModificarDoc);
      habilitarBoton(document.forms[0].cmdEliminarDoc);
      <%if ("si".equals(m_Config.getString("JSP.BotonCSV"))){%>
      habilitarBoton(document.forms[0].cmdCSVDoc);
      <%}%>

  }
  refresca();
}
function pulsarEliminarDoc() {
  if(tab.selectedIndex != -1) {
      if (listaDocumentosOriginal[tab.selectedIndex][8]!='') {
          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimDocRel")%>");
      } else if (listaDocumentosOriginal[tab.selectedIndex][7]=='F') {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimDocFirmado")%>");
      } else if (listaDocumentosOriginal[tab.selectedIndex][7]=='O') {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimDocEnProcesoFirma")%>");
      } else if (listaDocumentosOriginal[tab.selectedIndex][7]=='R') {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimDocRechazado")%>");
      } else if(jsp_alerta("C","<%=descriptor.getDescripcion("msjBorrarDoc")%>")) {
              document.forms[0].editorTexto.value=listaDocumentosOriginal[tab.selectedIndex][9];
              document.forms[0].codDocumento.value = listaDocumentosOriginal[tab.selectedIndex][0];
              document.forms[0].opcion.value="eliminarDocumentoCRD";
              document.forms[0].target="oculto";
              document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>?nombreDocumento=" + listaDocumentosOriginal[tab.selectedIndex][1];
              document.forms[0].submit();
      }
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }
}

function pulsarCSVDoc() {
    var i = tab.selectedIndex;
    
    if(i !== -1) {
        if (listaDocumentosOriginal[i][8] !== '') {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjNoModifDocRel")%>');
        } else if (listaDocumentosOriginal[i][7] == 'F') {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjNoModifDocFirmado")%>');
        } else if (listaDocumentosOriginal[i][7] == 'R') {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjNoModifDocRechazado")%>');
        } else if (listaDocumentosOriginal[i][7] == 'O') {
            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoModifDocEnProcesoFirma")%>");
        } else if (jsp_alerta("C",'<%=descriptor.getDescripcion("msjCrearCSV")%>') == 1) {
            var editorTexto = listaDocumentosOriginal[tab.selectedIndex][9];
            var codDocumento = listaDocumentosOriginal[tab.selectedIndex][0];
            var nombreDocumento = listaDocumentosOriginal[tab.selectedIndex][1];
            document.forms[0].codDocumento.value = codDocumento;
            document.forms[0].nombreDocumento.value = nombreDocumento;
            document.forms[0].editorTexto.value = editorTexto;
            
            crearCSVDocumentoTramite(codDocumento, nombreDocumento, editorTexto, successCrearCSVDocumentoTramite);
        }
    } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function eliminacionCRD(respOpcion) {
  if(respOpcion == "eliminadoCRD") {
    eliminarDocumento();
    jsp_alerta("A","<%=descriptor.getDescripcion("msjDocElimi")%>");
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimiDoc")%>");
  }
}
function eliminarDocumento() {
  var list = new Array();
  var listOrig = new Array();
  tamIndex=tab.selectedIndex;
  tamLength=tab.lineas.length;
  for (i=tamIndex - 1; i < listaDocumentos.length - 1; i++){
    if (i + 1 <= listaDocumentos.length - 2){
      listaDocumentos[i + 1]=listaDocumentos[i + 2];
      listaDocumentosOriginal[i + 1]=listaDocumentosOriginal[i + 2];
    }
  }
  for(j=0; j < (listaDocumentos.length-1) ; j++) {
    list[j] = listaDocumentos[j];
    listOrig[j] = listaDocumentosOriginal[j];
  }
  tab.lineas=list;
  refresca();
  listaDocumentos=list;
  listaDocumentosOriginal = listOrig;
  if (list.length!=0) {
      habilitarBoton(document.forms[0].cmdVerDoc);
      habilitarBoton(document.forms[0].cmdModificarDoc);
      habilitarBoton(document.forms[0].cmdEliminarDoc);
      <%if ("si".equals(m_Config.getString("JSP.BotonCSV"))){%>
      habilitarBoton(document.forms[0].cmdCSVDoc);
      <%}%>
  }
  else{
      deshabilitarBoton(document.forms[0].cmdVerDoc);
      deshabilitarBoton(document.forms[0].cmdModificarDoc);
      deshabilitarBoton(document.forms[0].cmdEliminarDoc);
      <%if ("si".equals(m_Config.getString("JSP.BotonCSV"))){%>
      deshabilitarBoton(document.forms[0].cmdCSVDoc);
      <%}%>   
  }
}

// Comprueba que no se exceda el tamaño maximo de las observaciones
                function verificarTamanoTextArea(){
                    var observaciones = document.forms[0].observaciones.value;

                    var mensaje = "";
                       if(observaciones!=null && observaciones.length>1000)
                       mensaje += '<%=descriptor.getDescripcion("etiq_MaxCObserv")%> 1000 <br/>';

                    return mensaje;
                }


function pulsarGrabar() {       
    
    if ((getNumInteresadosExpediente()==0) &&(interesado_Obligatorio() == "1")){
	jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{  
        var validarObs = verificarTamanoTextArea();
        var resultado = false;
        if (campos_vacios ==true)
        {
            msnj = "<%=descriptor.getDescripcion("msjValidarExpresion")%>";    
            if(jsp_alerta("C",msnj) ==1)             
            {
                resultado = true;
            }
        }
        else
            resultado = true;

        if (resultado ==true) 
        {
            if(validarObs!=null && validarObs.length>0)
                jsp_alerta('A',validarObs);
            else            
            {
                pleaseWait('on');
                document.forms[0].opcion.value="grabarTramite";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                document.forms[0].submit();
            }
        }
   }
}
function grabacionTramite(respOpcion) {
    pleaseWait('off');
  if ("noGrabado"==respOpcion){
      msnj = "<%=descriptor.getDescripcion("msjTramNoGrabado")%>";
      jsp_alerta("A",msnj);
  } 
  else
  {
    msnj = "<%=descriptor.getDescripcion("msjTramGrabado")%>";
    jsp_alerta("A",msnj);
  }
}
function ErrorExpresion(respOpcion) {
    if ("ErrorExpresion"== respOpcion.substring(0,14)){
        var campos = respOpcion.substring(15,respOpcion.length);
        var campos_fin = campos.split("#");
        msnj = "<%=descriptor.getDescripcion("msjExpresionErr")%>" + ":" ;
        for (i = 0; i < campos_fin.length; i++) 
        {            
            msnj = msnj + "<br>" + "     " + campos_fin[i];                 
        }      
        jsp_alerta("A",msnj);                        
    }
}
function finalizarExpediente(mensaje) {   
        jsp_alerta("A",mensaje);
        activarBotonFinalizarTramite();
}

function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp","Microsoft.XMLHttp"
          ];

        if (window.XMLHttpRequest){
                // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                return new XMLHttpRequest();
        }else if (window.ActiveXObject){
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                    try {
                        var oXmlHttp = new ActiveXObject(aVersions[i]);
                        return oXmlHttp;
                    }catch (error) {
                    //no necesitamos hacer nada especial
                    }
             }
        }
     }

	function verificarTramiteConNotificacionEnviada(){
		
		var ajax = getXMLHttpRequest();
		var exito = false;
		if(ajax!=null){

			var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
			var codTramite       = document.forms[0].codTramite.value;
			var codProcedimiento = document.forms[0].codProcedimiento.value;
			var codMunicipio     = document.forms[0].codMunicipio.value;
			var numero           = document.forms[0].numero.value;
			var ejercicio        = document.forms[0].ejercicio.value;

			var url             = "<%=request.getContextPath()%>/Notificacion.do";
			var parametros = "opcion=tramiteConNotificacionEnviada&codTramite=" + codTramite + "&ocurrenciaTramite=" + ocurrenciaTramite + "&codProcedimiento=" + escape(codProcedimiento)
				+ "&codOrganizacion=" + escape(codMunicipio) + "&numero=" + escape(numero) + "&ejercicio=" + escape(ejercicio);
                
			ajax.open("POST",url,false);
			ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
			ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
			ajax.send(parametros);

			try{
				if (ajax.readyState==4 && ajax.status==200){

					// En IE el XML viene en responseText y no en la propiedad responseXML
				   var text = ajax.responseText;				   
				   if(text!=null && text!=""){
						var datos = new Array();
						datos = text.split("=");
						var dato  = datos[0];
						var valor = datos[1];
						if(dato!=null && dato!="" && valor!=null && valor!=""){
							if(dato=="TIENE_NOTIFICACION" && valor=="true"){
								exito =true;
							}else
								exito = false;
						}

				   }
				}
			}catch(Err){
				alert("Error.descripcion: " + Err.description);
			}
		}

        return exito;
  }


function tieneTramiteCamposObligatoriosSinCubrir(){ 
        var ajax = getXMLHttpRequest();
        
        var exito = false;
        var codMunicipio     = document.forms[0].codMunicipio.value;
        
        var codTramite        = document.forms[0].codTramite.value;
        var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
        var numExpediente = document.forms[0].numero.value;
                
        var result ="";    
        if(ajax!=null){                                                         
            var url = "<%=request.getContextPath()%>/VerificarCamposObligatoriosTramiteSinValor.do";       
            var parametros = "codMunicipio=" + escape(codMunicipio) + "&numExpediente=" + escape(numExpediente) + "&codTramite=" + escape(codTramite) + "&ocurrenciaTramite=" + escape(ocurrenciaTramite);
                                                   
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try{            
                if (ajax.readyState==4 && ajax.status==200){         
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                   var text = ajax.responseText;                           
                   if (text.trim() != ""){                                        
                       if(text.trim()=="0")
                          exito = true; 
                    }
                }
            }catch(Err){
                alert("Error.descripcion: " + Err.description);            
            }
        }
        
        return exito;
     }

function comprobarObligatorios(){

        var camposCumplimentados = true;
	
      $('.inputTextoObligatorio').each(function() {	
       
            
            if($(this).attr('readonly') != 'readonly'){
            
               if ($.trim($(this).val())== '')  camposCumplimentados=false;	
           }	
       });	
    if(camposCumplimentados){	
        $('.inputTxtFechaObligatorio').each(function() {	
           if($(this).attr('readonly') != 'readonly'){
            
               if ($.trim($(this).val())== '')  camposCumplimentados=false;	
           }		
           });	
   }	
   if(camposCumplimentados){	
        $('.textareaTextoObligatorio').each(function() {	
           if($(this).attr('readonly') != 'readonly'){	
            
               if ($.trim($(this).val())== '')  camposCumplimentados=false;	
           }		
           }); 	
   }	
   if(camposCumplimentados){	
        $('.inputTextoObligatorio2').each(function() {	
            if ($.trim($(this).val())== '')  camposCumplimentados=false;	
        }); 	
   }	
    	
return camposCumplimentados;	
}

function pulsarFinalizar() { 
    if ((getNumInteresadosExpediente()==0) &&(interesado_Obligatorio() == "1")){
	jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
	
    }else if(!comprobarObligatorios()){
	
         jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
	

    } else {
            
           var avanzo = false;
           <% if ((tramExpForm.getAdmiteNotificacionElectronica()).equals("1")) {%>

           <% if ((tramExpForm.getNotificacionObligatoria()).equals("1")) {%>         
               
               var mostrarNotificacion = false;               
                if (document.forms[0].hayInteresadosNotifAutorizada.value != "true"){
                   jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoExistenIntNotifElect")%>');
                   avanzo=true;
               } else mostrarNotificacion = true;
               
               if(mostrarNotificacion){
                    document.forms[0].opcion.value="chequeoDocumentosTramitacionFirmados";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();
               }
           //}
           <%} else {%>

              
              var enviarNotificacion = false;    
              if (document.forms[0].hayInteresadosNotifAutorizada.value == "true" && jsp_alerta("C",'<%=descriptor.getDescripcion("msgEnviarNotifNoObligatoria")%>')){                 
                  
                   document.forms[0].opcion.value="chequeoDocumentosTramitacionFirmados";
                   document.forms[0].target="oculto";
                   document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                   document.forms[0].submit();
               } else {                  
                    avanzo = true;
              }              
           <%}%>
           <%}else{%>
               avanzo=true;               
           <%}%>
               if(avanzo){
                   document.getElementById("cmdFinalizar").disabled= true;               
                    document.forms[0].opcion.value="grabarTramite";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>?finalizar=true";
                    document.forms[0].submit();
               }
               
       //}
    }
}

 function mostrarAlertaNotificacions(mensaje)
 {
            jsp_alerta('A',mensaje);
 }


function mostrarVentanaNotificacionDatos(urlPantallaDatosNotificacion)
{
        var codMunicipio = document.forms[0].codMunicipio.value;
        var codProcedimiento = document.forms[0].codProcedimiento.value;
        var procedimiento = document.forms[0].procedimiento.value;
        var ejercicio = document.forms[0].ejercicio.value;
        var numero = document.forms[0].numero.value;
        var codTramite = document.forms[0].codTramite.value;
        var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;

        source = "<html:rewrite page='/Notificacion.do'/>?opcion=mostrarPantallaNotificacion"+"&codMunicipio=" + codMunicipio+"&codProcedimiento=" + codProcedimiento+"&ejercicio=" + ejercicio+"&numero=" + numero+"&codTramite=" + codTramite+"&ocurrenciaTramite=" + ocurrenciaTramite+"&procedimiento=" + procedimiento + "&recargar=no";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
	'width=1100,height=950,status='+ '<%=statusBar%>',function(ventana){
                        if(ventana!=undefined){
                            postNotificacionEnviada = true;
                            finalizaTramitePostNotificacion();
                            //actualizaDocs(ventana);
                        }
                    });
}

function finalizaTramitePostNotificacion()
{
    bloquearBotonesTemporal('cmdFinalizar');
    document.forms[0].opcion.value="grabarTramite";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>?finalizar=true";
    document.forms[0].submit();
}

function consultarTramites(nCS,oblig,literalFinalizarEntrada){

    literalFinalizar = literalFinalizarEntrada;
    var datosAEnviarTramites = new Array();
    datosAEnviarTramites[0] = nCS;
    datosAEnviarTramites[1] = oblig;
    datosAEnviarTramites[2] = postNotificacionEnviada;
    var source1 = "<c:url value='/jsp/sge/listaTramitesTramitacion.jsp?opcion=null'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source1,datosAEnviarTramites,'width=790,height=500,status='+ '<%=statusBar%>', 
        function(datosConsultaT) {
        if(datosConsultaT!=undefined){
            if(datosConsultaT[0] =="si") {
                document.forms[0].listaCodTramites.value = datosConsultaT[1];
                document.forms[0].listaModoTramites.value = datosConsultaT[2];
                document.forms[0].listaUtrTramites.value = datosConsultaT[3];
                document.forms[0].listaTramSigNoCumplenCondEntrada.value= datosConsultaT[4];
                if(datosConsultaT[1]!=""){
                    var codigos = datosConsultaT[1].split("§¥");
                    if((codigos.length-1)==1) codTramAbrir = codigos[0];
                    else codTramAbrir = null;
                } else codTramAbrir = null;
                if(datosConsultaT[5]=="tramite"){
                    cargaDirecta = true;
                } else cargaDirecta = false;
                
                if(datosConsultaT[1] !="") {
                    document.forms[0].opcion.value=literalFinalizar;
                } else {
                    document.forms[0].opcion.value="finalizarSinCondicion";
                }
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";

                //En tramite que no cumple condiciones de entrada
                if(datosConsultaT[4].indexOf("no")!=-1){
                    var source = "<c:url value='/jsp/sge/informacionTramiteCondEntradaNoValidas.jsp'/>";
                    var datosEntrada = new Array();
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,datosEntrada,'width=700,height=550,status='+ '<%=statusBar%>', 
                        function(datosInformacion) {
                            document.forms[0].submit();
                        });
                }else{
                    document.forms[0].submit();
                }
            }
        } else {
            activarBotonFinalizarTramite();
        }
    });
}

function procesoFinalizar() {    
    if( validarObligatoriosAqui() ) {
        if (!comprobarFirmasAntesFinalizacionTramite()) {
            jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.FinalizacionTramite.Error.AlgunDocumentoSinFirmar'/>');
        } else {

          if(tieneTareasPendientesInicio){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgTareasPendientesIniAlert")%>');
          }else{          
          if(document.forms[0].accion.value == "F") {
              //En esta condicion se finaliza el expedietne
            if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
              document.forms[0].opcion.value="finalizarExpediente";
              document.forms[0].target="oculto";
              document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
              document.forms[0].submit();
            }
            else{
                    activarBotonFinalizarTramite();;
                }
          } else if(document.forms[0].accion.value == "T") {
                consultarTramites(0,document.forms[0].obligat.value,"finalizarConTramites");
          } else if(document.forms[0].accion.value == "R") {
            var datosAEnviar = new Array();
            datosAEnviar[0] = document.forms[0].accion.value;
            var source = "<c:url value='/jsp/sge/favorDesfavor.jsp?opcion=null'/>";
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,datosAEnviar,'width=700,height=330,status='+ '<%=statusBar%>', 
                function(datosConsulta){
            if(datosConsulta!=undefined){
              if(datosConsulta[0] =="siFavorable") {
                if(document.forms[0].accionAfirmativa.value == "T") {
                    consultarTramites(1,document.forms[0].obligat.value,"finalizarConResolucionFavorableConTramites");
                } else {
                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                    document.forms[0].opcion.value="finalizarConResolucionFavorable";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();
                  }else
                    {
                        activarBotonFinalizarTramite();
                    }
                }
              } else if(datosConsulta[0] =="noFavorable") {
                if(document.forms[0].accionNegativa.value == "T") {
                    consultarTramites(2,document.forms[0].obligatorioDesf.value,"finalizarConResolucionDesfavorableConTramites");
                } else {
                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                    document.forms[0].opcion.value="finalizarConResolucionDesfavorable";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();
                  }else{
                        activarBotonFinalizarTramite();
                    }
                }
              }
            }else{
                    activarBotonFinalizarTramite();
                }
              });
          } else if(document.forms[0].accion.value == "P") {
            var datosAEnviar = new Array();
            datosAEnviar[0] = document.forms[0].accion.value;
            datosAEnviar[1] = document.forms[0].pregunta.value;
            var source = "<c:url value='/jsp/sge/favorDesfavor.jsp?opcion=null'/>";
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,datosAEnviar,'width=700,height=330,status='+ '<%=statusBar%>', 
                function(datosConsulta){
            if(datosConsulta!=undefined){
              if(datosConsulta[0] =="siFavorable") {
                if(document.forms[0].accionAfirmativa.value == "T") {
                    consultarTramites(1,document.forms[0].obligat.value,"finalizarConPreguntaFavorableConTramites");
                } else {
                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                    document.forms[0].opcion.value="finalizarConPreguntaFavorable";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();
                  }else
                    {
                        activarBotonFinalizarTramite();
                    }
                }
              } else if(datosConsulta[0] =="noFavorable") {
                if(document.forms[0].accionNegativa.value == "T") {
                    consultarTramites(2,document.forms[0].obligatorioDesf.value,"finalizarConPreguntaDesfavorableConTramites");
                } else {
                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                    document.forms[0].opcion.value="finalizarConPreguntaDesfavorable";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();
                  }else{
                        activarBotonFinalizarTramite();
                    }
                }
              }
            }else {
                    activarBotonFinalizarTramite();
                }
                });
          } else { 
            if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondSinCond")%>") == 1) {
              document.forms[0].opcion.value="finalizarSinCondicion";
              document.forms[0].target="oculto";
              document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
              document.forms[0].submit();
            }else
                {
                    activarBotonFinalizarTramite();
                }
          }
          }// else hay tareas pendientes de inicio
        }
    }
}

function finalizacionTramite(respOpcion, procedimientoAsociado) { 
    if ("noFinalizado"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjTramNoFinalizado")%>";
        jsp_alerta("A",msnj);
    } else if ("yaFinalizado"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjExpYaFinalizado")%>";
        jsp_alerta("A",msnj);
        pulsarVolver();
    } else if (respOpcion == "AutoRetrocedido") {        
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjTramAutoRet")%>")
    } else if (respOpcion == "FinalizadoNormal") {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoIniciadosTodos")%>");
        pulsarVolver();
    } else { 
        <%-- ORIGINAL
        if (procedimientoAsociado != '') {  
            document.forms[0].procedimientoAsociadoAIniciar.value = procedimientoAsociado;
            document.forms[0].opcion.value="iniciarProcedimientoAsociado";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
            document.forms[0].submit();
        } else {
            pulsarVolver();
        }
        --%>
        // #212448
        if (procedimientoAsociado != '') {  
            document.forms[0].procedimientoAsociadoAIniciar.value = procedimientoAsociado;
            document.forms[0].opcion.value="iniciarProcedimientoAsociado";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
            document.forms[0].submit();
        } else if(cargaDirecta && codTramAbrir!=null){
            obtenerDatosTramite(codTramAbrir);
        } else {
            pulsarVolver();
        }
    }     
    activarBotonFinalizarTramite();
}

function obtenerDatosTramite(codTram){
    pleaseWait('on');
    var numExp = document.forms[0].numeroExpediente.value;
    try{
        $.ajax({
            url: '<c:url value='/sge/TramitacionExpedientes.do'/>',
            type: 'POST',
            async: true,
             data: 'numExpediente=' + numExp + '&codTramite=' + codTram + '&opcion=cargarTramiteIniciado',

            success: procesarRespuestaObtenerDatosTramite,
            error: muestraErrorRespuestaObtenerDatosTramite
        });           
   }catch(Err){
        pleaseWait('off');
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorIniciarTrDirecto")%>');
        alert("Error1");
   }
}

function procesarRespuestaObtenerDatosTramite(ajaxResult){
    pleaseWait('off');
    if(ajaxResult){
        var datos = JSON.parse(ajaxResult);
        datos = datos.tabla;
         // Si estamos en expedientes relacionados, se deja ver la tramitacion en modo consulta. Es como si se viniese desde consulta
        if(document.forms[0].desdeExpRel.value=='si')
           document.forms[0].desdeConsulta.value = "si";

        if((document.forms[0].modoConsulta.value == "si")&& (document.forms[0].desdeConsulta.value != "si")){
            jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoCons")%>");
            return;
        }
        if(datos.permisoTramite == "si") {  //Comprueba si el usuario tiene el mismo cargo del tramite
            var datosTramite = [datos.ocuTramite,datos.codTramite,datos.descTramite,datos.fechaIniTramite,datos.fechaFinTramite,datos.codUniTramTramite];
            if(datos.fechaFinTramite == null || datos.fechaFinTramite == "") {
                if(datos.consultarTramite == "no") {
                    jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoCons")%>"); 
                }
            }            
            var fichaTramitacionExterna = verificarPantallaExterna(datosTramite);       
            if(fichaTramitacionExterna[0]!=null && fichaTramitacionExterna[0]!="" && fichaTramitacionExterna[0]!="null"
              && fichaTramitacionExterna[1]!=null && fichaTramitacionExterna[1]!="" && fichaTramitacionExterna[1]!="null"){
                if ((getNumInteresadosExpediente()== 0) && (interesado_Obligatorio() == "1")){                      
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
                }else{
                    pleaseWait('on');                        
                    ventanaTramitacionExterna = window.open(fichaTramitacionExterna[0],'ventanaTramitacionExterna',fichaTramitacionExterna[1]);
                    pleaseWait('off');
                    verificarCierreFichaTramitacionExterna();
                }                        
            }else{
                document.forms[0].ejercicio.value = datos.ejercicio;
                document.forms[0].numExpediente.value = datos.numero;
                document.forms[0].numeroExpediente.value = datos.numero;
                pleaseWait('on');
                activarFormulario();
                document.forms[0].ocurrenciaTramite.value = datosTramite[0];
                document.forms[0].codTramite.value = datosTramite[1];
                document.forms[0].codUnidadTramitadoraTram.value = datosTramite[5];
                document.forms[0].opcion.value="inicio";
                document.forms[0].target="mainFrame";
                document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                document.forms[0].submit();
            }// else
                
        } else {
            jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoCons")%>");
        }
    }
}

function muestraErrorRespuestaObtenerDatosTramite(){
    pleaseWait('off');
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorIniciarTrDirecto")%>');
}

function finalizacionExpediente(respOpcion, procedimientoAsociado){
    if ("expedienteNoFinalizado"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjExpNoFinalizado")%>";
        jsp_alerta("A",msnj);
    } else if ("noFinalizadoFirmasExpediente"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjDocsConFirmas")%>";
        jsp_alerta("A",msnj);
    } else if ("expedienteConTramitesIniciados"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjTramIniciado")%>";
        jsp_alerta("A",msnj);
    }else {
        msnj = "<%=descriptor.getDescripcion("msjExpFinalizado")%>";
        jsp_alerta("A",msnj);
        if (procedimientoAsociado != '') {  
            document.forms[0].procedimientoAsociadoAIniciar.value = procedimientoAsociado;
            document.forms[0].opcion.value="iniciarProcedimientoAsociado";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
            document.forms[0].submit();
        } else {    
            pulsarVolverBuzonExpPendientes(); // Pagina de buzon y expedientes pendientes.
        }
    }
}

function enviarInfoExpIniciado(uorInicioExp, uorInicioTram) {
    document.forms[0].opcion.value="enviarInfoExpIniciado";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>?uorInicioExp=" + uorInicioExp + "&uorInicioTram=" + uorInicioTram;
    document.forms[0].submit();
}

function pulsarVolver1() {pulsarVolver();}

function tramitesPendientes(notifRealizada, resultadoFinalizar) {
    finalizacionTramite(resultadoFinalizar);
    if (notifRealizada=="no"){
        msnj = "<%=descriptor.getDescripcion("msjNotifNoRealiz")%>";
        jsp_alerta("A",msnj);
    }
}
function pulsarVolverBuzonExpPendientes(){
    pleaseWait('on');
    document.forms[0].opcion.value="inicio";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
    document.forms[0].submit();
}
function comprobarFecha(inputFecha,calcularFecha) {
 if (Trim(inputFecha.value)!='') {
  if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
    jsp_alerta("A","<%=descriptor.getDescripcion("fechaNoVal")%>");
    return false;
  }
 }
 if(calcularFecha) {
     calcularFechaLimite();
 }
 return true;
}
function calcularFechaLimite() {
  var fechaLimite = "";
  fechaInicio = document.forms[0].fechaInicioPlazo.value;
  tipoPlazo = document.forms[0].tipoPlazo.value;
  plazo = document.forms[0].plazo.value;
  var dia = fechaInicio.substring(0,2);
  var mes = fechaInicio.substring(3,5);
  var ano = fechaInicio.substring(6,10);
  var diasMeses = new Array();
  var meses = new Array();
  meses = [01,02,03,04,05,06,07,08,09,10,11,12];
  if(document.forms[0].fechaFinPlazo.value=='' && document.forms[0].fechaInicioPlazo.value!=''){
  if (ano%4 == 0) {
    diasMeses = [31,29,31,30,31,30,31,31,30,31,30,31];
  } else {
    diasMeses = [31,28,31,30,31,30,31,31,30,31,30,31];
  }
  var m ="";
  var j;
  for(var i=0; i < meses.length; i++) {
    if(meses[i] == mes) {
      m=diasMeses[i];
      j = i;
    }
  }
  var suma;
  if(tipoPlazo == "DÍAS NATURALES" ) {
    suma = eval(plazo)+eval(dia);
    if(suma <= m) {
      fechaLimite = suma + "/" + mes + "/" + ano;
    } else {
      diasQueVan = eval(m)-eval(dia);
      diasQueQuedan = eval(plazo)-eval(diasQueVan);
      if(j==11) {
        j = -1;
        ano = eval(ano)+1;
        if (ano%4 == 0) {
          diasMeses = [31,29,31,30,31,30,31,31,30,31,30,31];
        } else {
          diasMeses = [31,28,31,30,31,30,31,31,30,31,30,31];
        }
      }
      var dM = diasMeses[j+1];
      var M = meses[j+1];
      fechaLimite = seguirCalculando(diasQueQuedan,dM,M,ano,diasMeses,meses,eval(j+1));
    }
    document.forms[0].fechaLimite.value = fechaLimite;
    document.forms[0].fechaFinPlazo.focus();
  } else if(tipoPlazo == "MESES" ) {
    suma = eval(plazo)+eval(mes);
    if(suma <=12) {
       fechaLimite = dia + "/" + suma + "/" + ano;
    } else {
      mesesQueVan = 12-eval(mes);
      ano = eval(ano)+1;
      plazo = eval(plazo)-eval(mesesQueVan);
      division = Math.ceil(plazo/12);
      if(division >1) {
        sumando = eval(division-1);
      } else {
        sumando = 0;
      }
      ano = eval(ano) + sumando;
      if (ano%4 == 0) {
        diasMeses = [31,29,31,30,31,30,31,31,30,31,30,31];
      } else {
        diasMeses = [31,28,31,30,31,30,31,31,30,31,30,31];
      }
      mesQueCae = eval(plazo) - (sumando*12);
      if(mesQueCae == "02" && dia>diasMeses[1]) {
        mesQueCae = eval(mesQueCae) + 1;
        dia = eval(dia)-eval(diasMeses[1]);
      }
      if(mesQueCae<10) mesQueCae = "0" + mesQueCae;
      if(dia.length<2) dia = "0" + dia;
      fechaLimite = dia + "/" + mesQueCae + "/" + ano;
    }
    document.forms[0].fechaLimite.value = fechaLimite;
    document.forms[0].fechaFinPlazo.focus();
  } else if(tipoPlazo == "DÍAS HÁBILES" ) {
    document.forms[0].opcion.value="diasHabiles";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
    document.forms[0].submit();
  }
}
  else if(document.forms[0].fechaFinPlazo.value=='' && document.forms[0].fechaInicioPlazo.value==''){
document.forms[0].fechaLimite.value='';
}
  else{
      document.forms[0].fechaLimite.value=document.forms[0].fechaFinPlazo.value;
  }
}
function seguirCalculando(plazo,m,mes,ano,diasMeses,meses,j) {
  suma = eval(plazo)+eval(0);
  if(suma <= m) {
    if(suma<10) suma= "0" + suma;
    if(mes<10) mes = "0" + mes;
    fechaLimite = suma + "/" + mes + "/" + ano;
  } else {
    diasQueVan = eval(m)-eval(0);
    diasQueQuedan = eval(plazo)-eval(diasQueVan);
    if(eval(j+1) > 11) {
      ano = eval(ano)+eval(1);
      if (ano%4 == 0) {
        diasMeses = [31,29,31,30,31,30,31,31,30,31,30,31];
      } else {
        diasMeses = [31,28,31,30,31,30,31,31,30,31,30,31];
      }
      j=-1;
    }
    var dM = diasMeses[j+1];
    var M = meses[j+1];
    fechaLimite = seguirCalculando(diasQueQuedan,dM,M,ano,diasMeses,meses,eval(j+1));
  }
  return fechaLimite;
}
function diasHabiles(f) {
  document.forms[0].fechaLimite.value = f;
  //document.forms[0].fechaFinPlazo.focus();
}
function validarObligatoriosAqui(){
    if (validarObligatorios("<%=descriptor.getDescripcion("msjObligTodos")%>") ) {
         var capaPlazoTramExp = document.getElementsByClassName("capaPlazoTramExp")	
         if (capaPlazoTramExp.lenght > 0) { // para IE9, los campos con display=none no los localiza	
            if (capaPlazoTramExp[0].style.display!="none") {
                if ( (document.forms[0].plazo.value=="") || (document.forms[0].tipoPlazo.value=="")) {
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                    activarBotonFinalizarTramite();
                    return false;
                }
            }
         }
    } else{ 
            activarBotonFinalizarTramite();
            return false;
        }  

    return true;

    }

function callFromTableTo(rowID,tableName){
  //si no es la tabla de formularios es la de documentos
  if (tableName=='tabFormsPDF'){
    pulsarVerFormPDF();
  }else if (document.forms[0].cmdGrabar.disabled == true) {
	document.forms[0].codDocumento.value = listaDocumentosOriginal[rowID][0];
	document.forms[0].nombreDocumento.value = listaDocumentosOriginal[rowID][1];
	pulsarVerDoc();
  }
}

function pulsarBloquear() {
    if ((getNumInteresadosExpediente()==0) &&(interesado_Obligatorio() == "1")){
	jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{  
        if(jsp_alerta("C","<%=descriptor.getDescripcion("msjBlqTramite")%>")) {
            document.forms[0].opcion.value="bloquear";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
            document.forms[0].submit();
        }
    }
}

function pulsarDesbloquear() {
    if(jsp_alerta("C","<%=descriptor.getDescripcion("msjDblqTramite")%>")) {
        document.forms[0].opcion.value="desbloquear";
        document.forms[0].target="mainFrame";
        document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
        document.forms[0].submit();
    }
}

function pulsarGenerarSalida() {
    if ((getNumInteresadosExpediente() == "") &&(interesado_Obligatorio() == "1")){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{
        if (listaCodUorsRegistro.length == 1) {
          abrirAltaSalida(listaCodUorsRegistro[0]);

        }else if(listaCodUorsRegistro.length == 0){
	
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjUsuNoTieneOfiReg")%>');
        } else {
          var source = "<html:rewrite page='/jsp/sge/listadoUORsRegistro.jsp?dummy='/>";
          abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp?source='/>" + source,null,
	'width=800,height=600,status='+ '<%=statusBar%>',function(uorDeRegistro){
                        if (uorDeRegistro!=null && uorDeRegistro!=undefined) {
                          abrirAltaSalida(uorDeRegistro);
                        }
                  });
        }
    }
}

function abrirAltaSalida(codigoUorRegistro) {
  var source = "<html:rewrite page='/MantAnotacionRegistro.do?opcion=salida_desde_tramitar&codigoUorRegistro='/>" + codigoUorRegistro;
  abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp?source='/>" + source,null,
	'width=990,height=680,status='+ '<%=statusBar%>',function(result){
                       
                  });
}


function modificaVariableCambiosCamposSupl() //Solo tiene sentido en la ficha de expediente, pero es necesario definirla aqui tambien
{}

    function ventanaPopUpModal(opcion,codigo) {    
        if (document.forms[0].modoConsulta.value!="si") {            
            var codTramite = document.forms[0].codTramite.value;
            var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
                        
            var source = "<html:rewrite page='/sge/DatosSuplementariosFichero.do'/>?opcion=" + opcion + "&codigo="+ codigo + "&codTramite=" + codTramite + "&ocurrenciaTramite=" + ocurrenciaTramite;
            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,"ventana1",
                    'width=550,height=450,scrollbars=no,status='+ '<%=statusBar%>',function(result){
                            if (result != undefined) {
                                actualizarFicheros(result[0], result[1]);
                            }
                    });            
        }        
    }
            
    function actualizarFicheros(campo, path){
        eval("document.forms[0]."+campo+".value='"+path+"'");
        eval("habilitarBoton(document.forms[0].cmdVisualizar"+campo+");");
        eval("habilitarBoton(document.forms[0].cmdEliminar"+campo+");");
        eval("habilitarBoton(document.forms[0].cmdCSV"+campo+");");
    }

    function onClickDocumento(codigo, nombreFich) { 
        var cod=codigo;
        //if(cod!=null && cod.length>0 && nombreFich!=null && nombreFich!="")
            //cod = cod.substring(0,cod.indexOf('_'));            
        window.open("<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios?codigo=" + cod +
                    "&nombreFich=" + nombreFich + "&opcion=1", "ventana1",
                    "left=10, top=10, width=500, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
    }


    function onClickEliminarDocumento(codigo) {
        
        var msgErr1 = '<%=descriptor.getDescripcion("errEliminarDocCampoSup")%>';
        var msgErr2 = '<%=descriptor.getDescripcion("errEliminarDocCampoSup2")%>';
        
        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjBorrarFichero")%>') ==1) {
            if(marcarEliminadoCampoSuplementarioFichero(codigo,"TRAMITE",msgErr1,msgErr2)){        
            
                eval("document.forms[0]."+codigo+".value='';");
                eval("deshabilitarBoton(document.forms[0].cmdVisualizar"+codigo+");");
                eval("deshabilitarBoton(document.forms[0].cmdEliminar"+codigo+");");
                eval("deshabilitarBoton(document.forms[0].cmdCSV"+codigo+");");
            }
            }
        }
 
    function onClickCrearCSVDocumento(codigo) {
        if (jsp_alerta("C",'<%=descriptor.getDescripcion("msjCrearCSV")%>') == 1) {
            crearCSVCampoSuplementarioFicheroTramite(codigo);
        }
    }
 
    // Ver fecha vencimiento - campoFecha.jsp
            function onClickVer(idCampo, plazo, periodoPlazo, campoActivo){
               var campo = document.getElementById(idCampo);
               if (campo==null) return;
               //Si no se ha definido una fecha
               if ((campo.value =='') || (campo.value =="")){
                   jsp_alerta('A','<%=descriptor.getDescripcion("msgNoFecha")%>');
               }else{
                   //si no se ha definido un plazo
                   if ((plazo=='null')||(plazo=="null")||(plazo==null)){
                       jsp_alerta('A','<%=descriptor.getDescripcion("msgNoPlazo")%>');
                   }else{//se ha definido un plazo
                        var parametros ="opcion=verFechaVencimiento"
                        parametros += "&fecha=";
                        parametros += campo.value;
                        parametros += "&plazo=";
                        parametros += plazo;
                        parametros += "&periodoPlazo=";
                        parametros += periodoPlazo;
                        abrirXanelaAuxiliar('<%=request.getContextPath()%>/sge/TramitacionExpedientes.do?' + parametros,null,
                                'width=400,height=450',function(rslt){});
                   }
               }
            }
            
            function onClickDesactivar(idCampo){
                if (idCampo==null)return;
                var cmdActivar = document.getElementById("enlaceActivar"+idCampo);
                var cmdDesactivar = document.getElementById("enlaceDesactivar"+idCampo);
                var campo =  document.getElementById("activar"+idCampo);
                //al pulsar bton desactivar, se oculta el bton activar
                cmdActivar.style.visibility='visible';
                cmdActivar.style.display='inline';
                cmdDesactivar.style.display='none';
                campo.value="desactivada"; 
            }
            
            function onClickActivar(idCampo){
                if (idCampo==null)return;
                var cmdActivar = document.getElementById("enlaceActivar"+idCampo);
                var cmdDesactivar = document.getElementById("enlaceDesactivar"+idCampo);
                var campo =  document.getElementById("activar"+idCampo);
                //al pulsar bton desactivar, se oculta el bton activar
                cmdDesactivar.style.visibility='visible';
                cmdActivar.style.display='none';
                cmdDesactivar.style.display='inline';
                campo.value="activada"; 
            }
            
    function recuperarFechasInforme(numeroDocumento){
        var aFecha = new Array();
        for(i=0;i<listaDocumentosOriginal.length;i++){
            if(listaDocumentosOriginal[i][0]==numeroDocumento){
                var fechaInforme = listaDocumentosOriginal[i][4];
                var fechaCreacion = listaDocumentosOriginal[i][2];
                aFecha[0] = fechaInforme;
                aFecha[1] = fechaCreacion;
                break;
            }// if
        }// for
        return aFecha;
    }


    function actualizarFechaInforme(numeroDocumento,fechaInforme){        
        for(i=0;i<listaDocumentosOriginal.length;i++){
            if(listaDocumentosOriginal[i][0]==numeroDocumento){
                listaDocumentosOriginal[i][4] = fechaInforme;
                listaDocumentos[i][3] = fechaInforme +  "&nbsp;<a href='javascript:modificarFechaInforme(" + numeroDocumento + "," + i + ");'>" + '<span class="fa fa-calendar" aria-hidden="true" title="<%=descriptor.getDescripcion("etiq_modFecInformeDoc")%>"/></span></a>';
                
                break;
            }// if
        }// for
        tab.lineas = listaDocumentos;
        refresca();
    }


    function getNombreDocumento(numeroDocumento){
        var nombre = "";
        for(i=0;i<listaDocumentosOriginal.length;i++){
            if(listaDocumentosOriginal[i][0]==numeroDocumento){
                nombre  = listaDocumentosOriginal[i][1];
                break;
            }// if
        }// for

        return nombre;
    }




    function modificarFechaInforme(doc,posicion){
        var documentoSeleccionado = tab.selectedIndex;
        var codProcedimiento = document.forms[0].codProcedimiento.value;
        var codMunicipio        = document.forms[0].codMunicipio.value;
        var ejercicio              = document.forms[0].ejercicio.value;
        var numero               = document.forms[0].numero.value;        
        var codTramite          = document.forms[0].codTramite.value;
        var ocurrencia           = document.forms[0].ocurrenciaTramite.value;
        var aFechas              = new Array();
        aFechas                    = recuperarFechasInforme(doc);
        var fechaInforme       = aFechas[0];
        var fechaCreacion      = aFechas[1];

        if(listaDocumentosOriginal[posicion][8]!=''){            
            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoModifFecInformeDocRel")%>");
        }else{
      
            var parametros = "?codDocumento=" + doc + "&codProcedimiento=" + codProcedimiento +
                                       "&codMunicipio=" + codMunicipio + "&ejercicio=" + ejercicio + "&numero=" + numero +
                                       "&codTramite=" + codTramite + "&ocurrencia=" + ocurrencia + "&fechaInforme=" + fechaInforme + "&fechaCreacion=" + fechaCreacion;

            if(parametros!=undefined){
                var source = "<html:rewrite page='/jsp/sge/fechaInformeDocumentoTramitacion.jsp'/>" + parametros;
                 var datosAEnviar = {'descDoc':getNombreDocumento(doc)};
                abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,datosAEnviar,
                        'width=450,height=330,scrollbars=no,status='+ '<%=statusBar%>',function(result){
                                if(result!=undefined){
                                    if(result[0]=="fail"){
                                    }else{
                                        actualizarFechaInforme(doc,result[1]);

                                    }
                                }
                        });
            }
       }//else
    }


    function pulsarEjecutarTareasPendientes(){
        var codTramite        = document.forms[0].codTramite.value;
        var ocurrencia        = document.forms[0].ocurrenciaTramite.value;
        var codMunicipio      = document.forms[0].codMunicipio.value;
        var numero            = document.forms[0].numero.value;
        var codProcedimiento  = document.forms[0].codProcedimiento.value;
        var ejercicio         = document.forms[0].ejercicio.value;

        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msgConfirmEjecTareasPendientes")%>')){
            var parametros = "&codTramite=" + codTramite + "&ocurrencia=" + ocurrencia + "&numero=" + numero + "&codMunicipio=" + codMunicipio + "&codProcedimiento=" + codProcedimiento + "&ejercicio=" + ejercicio;
            document.forms[0].target="oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/sge/TramitacionExpedientes.do?opcion=ejecutarTareasInicioPendientes" + parametros;
            document.forms[0].submit();
        }
    }
    
    var listaCampos = new Array();
    var listaAgrupaciones = new Array();
    
    function posicionarCampos(){

        var contCampos = 0;
        var contAgrupaciones = 0;
        <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="estructuraDatosSuplementarios">
            var campo = new Array();
                campo[0] = '<bean:write name="elemento" property="codCampo" />';
                campo[1] = '<bean:write name="elemento" property="posicionar" />';
                campo[2] = '<bean:write name="elemento" property="posX" />';
                campo[3] = '<bean:write name="elemento" property="posY" />';
                campo[4] = '<bean:write name="elemento" property="codAgrupacion" />';
                campo[5] = '<bean:write name="elemento" property="codTipoDato" />';
            listaCampos[contCampos] = campo;
            contCampos ++;
        </logic:iterate>
        
        <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaAgrupacionesCampos">
            var agrupacion = new Array();
                agrupacion[0] = '<bean:write name="elemento" property="codAgrupacion" />';
                agrupacion[1] = '<bean:write name="elemento" property="ordenAgrupacion" />';
            listaAgrupaciones[contAgrupaciones] = agrupacion;
            contAgrupaciones ++;
        </logic:iterate>
        
        for (i=0; i < listaCampos.length; i++) {
            var campo = new Array();
                campo = listaCampos[i];
            var nomCampo = "campo_" + campo[0];
            var nomAgrupacion = "capaDatosSuplementarios_" + campo[4];
            var posX = campo[2];
            var posY = campo[3];
            if(posX != undefined && posX != null && posX != "" && posX !=  " "){
                if(posY != undefined && posY != null && posY != "" && posY !=  " "){
                    //if(posX != 0 && posY != 0){
                        if(document.getElementById(nomCampo)){
                            var posParent = $('#'+nomAgrupacion).offset();
                            posX = parseInt(posX) + parseInt(posParent.left);
                            posY = parseInt(posY) + parseInt(posParent.top);                            
                            $('#'+nomCampo).css('z-index', i+1);
                            $('#'+nomCampo).css('position', 'absolute');
                            //$('#'+nomCampo).offset({ top: posY, left: posX, of: nomAgrupacion});                                                             
                                                                                  
                            var div_ancho_pantalla = $( document ).width();
                            var mitad=div_ancho_pantalla/2;
                          
                            if (posX<100){                          
                                $('#'+nomCampo).offset({ top: posY, left: posX, of: nomAgrupacion}); 
                            }
                            else {    
                                $('#'+nomCampo).offset({ top: posY, left: mitad, of: nomAgrupacion}); 
                            }

                        }//if(document.getElementById(nomCampo))
                    //}//if(posX != 0 && posY != 0)
                }//if(posY != undefined && posY != null && posY != "" && posY !=  " ")
            }//if(posX != undefined && posX != null && posX != "" && posX !=  " ")
        }//for (i=0; i < listaCampos.length; i++)
        
        for (x=0; x < listaAgrupaciones.length; x++) {
            var agrupacion = new Array();
                agrupacion = listaAgrupaciones[x];
            calcularAltoAgrupacion(agrupacion[0]);
        }//for (i=0; i < listaAgrupaciones.length; i++)
        
    }//posicionarCampos
    
    function calcularAltoAgrupacion(codAgrupacion){
        var tamanho = 0;
        var maxY=0;
        var ultimoCampo=new Array();
        for (i=0; i < listaCampos.length; i++) {
            var campo = new Array();
                campo = listaCampos[i];           
            if(campo[4] == codAgrupacion){
                var posY = parseInt(campo[3]);
                if(posY>maxY) {
                    ultimoCampo=campo;
                    maxY = posY;
                }
            }//if(campo[4] == codAgrupacion)
        }//for (i=0; i < listaCampos.length; i++) 
       
       if (listaCampos.length>0 && maxY > 0){
            if (ultimoCampo[5] == 4)               
              tamanho = maxY+ 120;
            else 
                tamanho = maxY+ 40;
       }
        
        document.getElementById("capaDatosSuplementarios_" + codAgrupacion).style.height = tamanho + "px";
    }//calcularAltoAgrupacion
   
    
    
    function activarBotonFinalizarTramite(){          
         var instruccion = "eval(document.getElementById('cmdFinalizar').disabled=false);";
         var t=setTimeout(instruccion,1500);
    }
    
    
     function mostrarVentana(mensaje,title) {
       
       $('#popupTextoLargo').fadeIn('slow');
		$('.popup-overlay').fadeIn('slow');
		$('.popup-overlay').height($(window).height());
      domlay('capaTitulo',1,0,0,title);
      domlay('capaTextoLargo',1,0,0,mensaje);
                
   }
   
   function cerrarVentanaTextoLargo(){
       $('#popupTextoLargo').fadeOut('slow');
	$('.popup-overlay').fadeOut('slow');
	return false;
   }
  
   /*function pulsarAnexarDoc()
{        
        source = "<html:rewrite page='/sge/DocumentosExpediente.do'/>?opcion=anexarDocumentoAdjunto";      
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
                'width=500,height=500,status='+ '<%=statusBar%>',function(listaDocsExternosJSON){
            if(listaDocsExternosJSON!=undefined){
                // Se recibe el String en formato JSON con los datos de los documentos externos 
                // una vez dado de alta el nuevo            
                procesarRespuestaListadoDocumentosExternos(listaDocsExternosJSON)
            }
                  });
    
}*/
  
  
  function errorAltaDocumento(){
        jsp_alerta("A","No se ha podido generar el documento");
        pleaseWait('off');
  }

</script>

</head>
<BODY class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
    
<html:form action="/sge/TramitacionExpedientes.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codProcedimiento" />
<html:hidden  property="codMunicipio" />
<html:hidden  property="ejercicio" />
<html:hidden  property="numero" />
<html:hidden  property="ocurrenciaTramite" />
<html:hidden  property="codDocumento" />
<html:hidden  property="estadoFirma" />
<html:hidden  property="codPlantilla" />
<html:hidden  property="codTramite" />
<html:hidden  property="codUsuario" />
<html:hidden  property="accion" />
<html:hidden  property="accionAfirmativa" />
<html:hidden  property="accionNegativa" />
<html:hidden  property="obligat" />
<html:hidden  property="obligatorioDesf" />
<html:hidden  property="pregunta" />
<html:hidden  property="permiso" />
<html:hidden  property="bloqueo" />
<html:hidden  property="listaCodInteresados"/>
<html:hidden  property="listaVersInteresados"/>
<input type="hidden" name="numeroDocumento" value="">
<input type="hidden" name="listaCodTramites" value="">
<input type="hidden" name="listaModoTramites" value="">
<input type="hidden" name="listaUtrTramites" value="">
 <input type="hidden" name="listaTramSigNoCumplenCondEntrada" value=""/>
<html:hidden  property="codFormPDF" />
<input type="hidden" name="nombreDocumento" value="">
<input type="hidden" name="desdeConsulta">
<input type="hidden" name="desdeExpRel">
<input type="hidden" name="porCampoSup">
<html:hidden  property="modoConsulta" />
<html:hidden  property="expRelacionado" />
<html:hidden  property="codMunicipioIni"/>
<html:hidden  property="ejercicioIni"/>
<html:hidden  property="numeroIni"/>
<html:hidden  property="desdeInformesGestion"/>
<html:hidden  property="todos"/>
<html:hidden  property="admiteNotificacionElectronica"/>
<html:hidden  property="notificacionObligatoria"/>
<input type="hidden" name="procedimientoAsociadoAIniciar" value=""/>
<input type="hidden" name="admiteNotificacionElectronica" value=""/>
<input type="hidden" name="hayInteresadosNotifAutorizada"/>
<!-- #212448 -->
<input type="hidden" name="codUnidadTramitadoraTram"/>
<input type="hidden" name="numExpediente"/>
<html:hidden  property="editorTexto"/>

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_tramit")%></div>
<div class="encabezadoGrisGrande">
    <table style="width:100%">
        <tr>
            <td>
                <div class="etiqueta" style="width:10%;float:left;padding-top:10px" ><%=descriptor.getDescripcion("etiq_numExp")%>:&nbsp;</div>
                <html:text styleId="obligatorio" styleClass="inputTexto" style="width:14%" property="numeroExpediente" maxlength="30" readonly="true"
                            onkeyup="xAMayusculas(this);"/>
               <html:text style="width:74%" styleId="obligatorio" styleClass="inputTexto" property="procedimiento" maxlength="255" readonly="true"
                            onkeyup="xAMayusculas(this);"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="etiqueta" style="width:10%;float:left;padding-top:10px" ><%=descriptor.getDescripcion("gEtiq_titular")%>:&nbsp;</div>
                <html:text  styleClass="inputTexto" property="titular" style="width:88.6%" maxlength="255" readonly="true"
                             onkeyup="xAMayusculas(this);"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="etiqueta" style="width:10%;float:left;padding-top:10px" ><%=descriptor.getDescripcion("gEtiq_tramite")%>:&nbsp;</div>
                    <html:text styleId="obligatorio" styleClass="inputTexto" property="tramite" style="width:88.6%" maxlength="255" readonly="true"
                                onkeyup="xAMayusculas(this);"/>
            </td>
        </tr>
    </table>
</div>
<div class="contenidoPantalla">
        <div class="tab-pane" id="tab-pane-1">
            <script type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
            </script>
            <!-- CAPA 1: DATOS GENERALES -->
            <div class="tab-page" id="tabPage1">
                <h2 class="tab" id="pestana1"><%=descriptor.getDescripcion("res_pestana1")%></h2>
                <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
                    <TABLE style="width:100%" class="contenidoPestanha">
                        <tr>
                            <td style="width:20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fecIni")%>:</td>
                            <td>
                                <span class="columnP">
                                    <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" style="width:12%" maxlength="9" property="fechaInicio" readonly="true"
                                               onkeyup = "return SoloCaracteresFecha(this);"
                                               onfocus = "this.select();"/>
                                </span>
                                <span style="width:10%;margin-left:5%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fecFin")%>:</span>
                                <span  class="columnP">
                                    <html:text styleClass="inputTxtFecha" style="width:12%" maxlength="9" property="fechaFin" readonly="true"
                                               onkeyup = "return SoloCaracteresFecha(this);" onfocus = "this.select();"/>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidTramit")%>:</td>
                            <td class="columnP">
                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="unidadTramitadora" 
                                    style="width:100%" maxlength="255" readonly="true" onkeyup="xAMayusculas(this);"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_clasTram")%>:</td>
                            <td class="columnP">
                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="clasificacionTramite" 
                                           style="width:100%" maxlength="255" readonly="true" onkeyup="xAMayusculas(this);"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_UserTram")%>:</td>
                            <td class="columnP">
                                <html:text property="nombreUsuario" styleClass="inputTextoDeshabilitado" 
                                           style="width:100%" readonly="true"/>
                            </td>
                        </tr>
                        <tr class="capaPlazoTramExp" style="display: none">	
                            <!-- #275169 : Se quita class="etiquetaPlazoMaximo" -->	
                            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_plzMax")%>:</td>
                            <td class="columnP">
                                <html:text styleClass="inputTextoObligatorio" property="plazo" style="width:12%" 
                                           maxlength="2" readonly="true" onkeyup="return SoloDigitosNumericos(this);"/>&nbsp;
                                <html:text styleClass="inputTextoObligatorio" property="tipoPlazo" style="width:35%" 
                                           maxlength="25" readonly="true" onkeyup="return xAMayusculas(this);"/>
                            </td>
                        </tr>
                         <tr class="capaPlazoTramExp" style="display: none">
                            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_fIniPlz")%>:</td>
                            <td>
                                <span class="columnP">
                                    <html:text styleClass="inputTxtFecha" style="width:12%" maxlength="10" 
                                               property="fechaInicioPlazo" onkeyup = "return SoloCaracteresFecha(this);"
                                               onblur = "javascript:return comprobarFecha(this,true);" onfocus = "this.select();"/>
                                    <A href="javascript:calClick(event);return false;" onclick="mostrarCalFechaInicioPlazo(event);return false;" style="text-decoration:none;" >
                                        <span class="fa fa-calendar" aria-hidden="true" id="calFechaInicioPlazo" name="calFechaInicioPlazo" alt="Data" ></span>
                                    </A>
                                </span>
                                <span style="width:12%;margin-left:10%" class="etiqueta"><%=descriptor.getDescripcion("etiq_fecLim")%>:</span>
                                <span class="columnP">
                                    <html:text styleClass="inputTxtFecha" style="width:12%" maxlength="10" property="fechaLimite" readonly="true"
                                               onkeyup = "return SoloCaracteresFecha(this);" onfocus = "this.select();"/>
                                </span>
                                <span style="width:12%;margin-left:5%" id="etiqFechaFin" class="etiqueta"><%=descriptor.getDescripcion("etiq_fFinPlz")%>:</span>
                                <span class="columnP">
                                    <html:text styleClass="inputTxtFecha" style="width:12%" maxlength="10" property="fechaFinPlazo"
                                               onkeyup = "javascript:return SoloCaracteresFecha(this);"
                                               onblur = "javascript:return comprobarFecha(this,true);"/>
                                    <A href="javascript:calClick(event);" onClick="mostrarCalFechaFinPlazo(event);return false;" style="text-decoration:none;" >
                                        <span class="fa fa-calendar" aria-hidden="true" id="calFechaFinPlazo" name="calFechaFinPlazo" alt="Data" ></span>
                                    </A>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="etiqueta"><%=descriptor.getDescripcion("etiqObs")%>:</td>
                            <td class="columnP">
                                <html:textarea styleClass="textareaTexto" style="width:750px;text-transform:none" rows="3" property="observaciones"
                                               value=""></html:textarea>
                            </td>
                        </tr>
                        <TR>
                            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocs")%></TD>
                        </TR>
                        <TR>
                            <TD id="tablaDoc" colspan="2"></TD>
                        </TR>
                        <TR>
                            <TD colspan="2" style="width: 80%" align="center">
                                <input type="button" name="cmdAltaDoc" onclick="pulsarAltaDoc();" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
                                <input type="button" class="botonGeneral" accesskey="V" value="Ver" name="cmdVerDoc" onclick="pulsarVerDoc();">
                                <input type="button" class="botonGeneral" accesskey="M" name="cmdModificarDoc" onclick="pulsarModificarDoc();" value="<%=descriptor.getDescripcion("gbModificar")%>">
                                <input type="button" class="botonGeneral" accesskey="E" name="cmdEliminarDoc" onclick="pulsarEliminarDoc();" value="<%=descriptor.getDescripcion("gbEliminar")%>">
                                <%
                                if ("si".equals(m_Config.getString("JSP.BotonCSV"))){
                                %>
                                    <input type="button" class="botonGeneral" accesskey="C" name="cmdCSVDoc" onclick="pulsarCSVDoc();" value="<%=descriptor.getDescripcion("gbCSV")%>">
                                <%}%>
                            </td>
                        </tr>                                                    
                        <tr id="msgTareasFila" style="display:none">
                            <td colspan="2">
                                <div id="msgTareasPendientesInicio" name="msgTareasPendientesInicio" class="msgTareasPendientesInicio"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <%
                Vector estructuraDatosSuplementarios = (Vector) tramExpForm.getEstructuraDatosSuplementarios();
                Vector estructuraAgrupacionesCampos = (Vector) tramExpForm.getListaAgrupacionesCampos();
                Vector valoresDatosSuplementarios = (Vector) tramExpForm.getValoresDatosSuplementarios();
                Boolean cargarVista = tramExpForm.getCargarVista();
                boolean mostrarPestanhaDatos = false;

                if (estructuraDatosSuplementarios != null) {
                    int lengthEstructuraDatosSuplementarios = estructuraDatosSuplementarios.size();
                    int lengthValoresDatosSuplementarios = valoresDatosSuplementarios.size();
                    int lengthEstructuraAgrupacionesCampos = estructuraAgrupacionesCampos.size();
                    for (int i = 0; i < lengthEstructuraDatosSuplementarios; i++) {
                        EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                        //Si el estado de algun campo es distinto NO entonces se mostrara la pestanha de Datos de un tramite
                        if (!eC.getActivo().equalsIgnoreCase("NO")) {
                            mostrarPestanhaDatos = mostrarPestanhaDatos || true;
                        }
                        if (eC.getCodTipoDato().equals("5")) {
                            campos = campos + "," + eC.getCodCampo() + "_" + eC.getOcurrencia();
                        }
                    }   

                    if ((lengthEstructuraDatosSuplementarios > 0) && (mostrarPestanhaDatos)) {
                %>
                        <!-- CAPA 2: PESTAÑA DE DATOS -->
                        <div class="tab-page" id="tabPage2" >
                            <h2 class="tab" id="pestana2"><%=descriptor.getDescripcion("etiqDSupTram") %></h2>
                            <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
                            <DIV id="capaDatosSuplementarios" name="capaDatosSuplementarios">
                                <TABLE id ="tablaDatosSuplementarios" class="contenidoPestanha">
                                    <TR>
                                        <TD class="sub3titulo"><%=descriptor.getDescripcion("etiqDatosSupl")%></TD>
                                    </TR>
                                    <%for (int z = 0; z < lengthEstructuraAgrupacionesCampos; z++) {
                                        DefinicionAgrupacionCamposValueObject agrupacion = 
                                        (DefinicionAgrupacionCamposValueObject)estructuraAgrupacionesCampos.get(z);
                                    %>
                                        <% if(!agrupacion.getCodAgrupacion().equalsIgnoreCase("DEF")){%>
                                            <tr>
                                                <td class="sub3titulo">
                                                    &nbsp;
                                                    <%=agrupacion.getDescAgrupacion()%>
                                                </td>
                                            </tr>
                                        <%}%>
                                    <TR>
                                        <TD>
                                            <DIV id="capaDatosSuplementarios_<%=agrupacion.getCodAgrupacion()%>" 
                                                    name="capaDatosSuplementarios_<%=agrupacion.getCodAgrupacion()%>"
                                                    style="position: relative">
                                                <%for (int k = 0; k < lengthEstructuraDatosSuplementarios; k++) {
                                                    EstructuraCampo campo = 
                                                        (EstructuraCampo) estructuraDatosSuplementarios.get(k);
                                                    if(campo.getCodAgrupacion().equalsIgnoreCase(agrupacion.getCodAgrupacion())){
                                                        request.setAttribute("CAMPO_BEAN", estructuraDatosSuplementarios.get(k));
                                                        request.setAttribute("beanVO", valoresDatosSuplementarios.get(k));

                                                        if ((campo.getCodTipoDato().equals("8"))||(campo.getCodTipoDato().equals("9"))) {
                                                            existenCamposCalculados=true;                                                                                        
                                                        }

                                                        if (campo.getCodTipoDato().equals("5")) {
                                                            nombre_campo_aux = campo.getCodCampo();
                                                            if (campo.getOcurrencia() != null) 
                                                                nombre_campo_aux += "_" +campo.getOcurrencia();
                                                        } else if (campo.getCodTipoDato().equals("6")) {
                                                            nombre_campo_aux = campo.getCodCampo();
                                                            if (campo.getOcurrencia() != null)
                                                                nombre_campo_aux = "codT_" + campo.getCodTramite() + "_" + campo.getOcurrencia() + "_" + campo.getCodCampo();
                                                        } else if (campo.getOcurrencia() != null) {
                                                            nombre_campo_aux = "T_" + campo.getCodTramite() + "_" + campo.getOcurrencia() + "_" + campo.getCodCampo();
                                                        }else {
                                                            nombre_campo_aux = "T_" + campo.getCodTramite() + "_" + campo.getCodCampo();
                                                        }    
                                                        if (nombre_campo.equals("")){
                                                            nombre_campo = campo.getCodCampo();
                                                            nombre_campo_descripcion = campo.getDescCampo();
                                                            nombre_campo_carga = nombre_campo_aux;
                                                        }else{   
                                                            nombre_campo = nombre_campo +"#"+ campo.getCodCampo();  
                                                            nombre_campo_descripcion = nombre_campo_descripcion  + "#" + campo.getDescCampo();
                                                            nombre_campo_carga = nombre_campo_carga + "#" + nombre_campo_aux;
                                                        }
                                                       if ((fechaFinExpediente != null) && (!fechaFinExpediente.equalsIgnoreCase(""))) {%>
                                                            <div id="campo_<%=campo.getCodCampo()%>" 
                                                                style="width: <%=campo.getTamanoVista()+ '%'%>;">
                                                                <jsp:include page="/jsp/plantillas/CampoVista.jsp?desactivaFormulario=true" flush="true" />
                                                            </div>
                                                        <%}else{%>
                                                            <div id="campo_<%=campo.getCodCampo()%>" 
                                                                style="width: <%=campo.getTamanoVista()+ '%'%>;">
                                                                <jsp:include page="/jsp/plantillas/CampoVista.jsp?desactivaFormulario=false" flush="true" />
                                                            </div>
                                                        <%}//if ((fechaFinExpediente != null) && (!fechaFinExpediente.equalsIgnoreCase(""))) 
                                                    }//if(campo.getCodAgrupacion().equalsIgnoreCase(agrupacion.getCodAgrupacion()))
                                                }//for (int k = 0; k < lengthEstructuraDatosSuplementarios; k++)
                                            %>
                                        </DIV>
                                    </TD>
                                </TR>
                                <tr>
                                    <td>
                                        &nbsp;
                                    </td>
                                </tr>
                                <%}%>

                            <%if(cargarVista){%>
                                <script type="text/javascript">posicionarCampos();</script>
                            <%}%>
                        </TABLE>
                            </DIV>
                            <div id="popupTextoLargo" style="display: none;">
                                <div class="content-popup">
                                    <div>
                                        <h2><text id ="capaTitulo"></text></h2>
                                        <textarea id="capaTextoLargo" styleClass="textareaTexto" style="text-transform: none;width:100%" cols="150" rows="18" readonly></textarea>
                                        <div id="dialog_botonera" style="margin-top: 10px; margin-right: 5px; float: right">
                                            <input type="button" class="botonGeneral" value="Cerrar" onClick="cerrarVentanaTextoLargo();">
                                        </div>
                                    </div>
                                </div>
                            </div>  
                </div>
                <%
}
}
Vector lEnlaces = (Vector) tramExpForm.getListaEnlaces();
String inst = (String) tramExpForm.getInstrucciones();
if ((lEnlaces != null && lEnlaces.size() > 0) || ((inst != null) && !"".equals(inst))) {
                %>                  
                <!-- CAPA 4: PESTAÑA DE ENLACES -->
                <div class="tab-page" id="tabPage4">
                    <h2 class="tab">Info adicional</h2>
                    <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
                    <TABLE id ="tablaEnlaces" class="contenidoPestanha">
                        <%
                if (lEnlaces != null) {
                    if (lEnlaces.size() > 0) {
                        %>                                                       
                        <TR>
                            <TD class="sub3titulo" colspan="7">&nbsp;Enlaces</TD>
                        </TR>
                        <TR>
                            <TD style="width: 100%">
                                <DIV id="capaEnlaces" name="capaEnlaces" style="min-height:140px;overflow:auto;"></DIV>
                            </TD>
                        </TR>                                                   
                        <%
                                } else {
                        %>
                        <DIV id="capaEnlaces" name="capaEnlaces" style="display:none"></DIV>
                        <%
                                }
                            } else {
                        %>
                        <DIV id="capaEnlaces" name="capaEnlaces" style="display:none"></DIV>
                        <%
                }
                if (inst != null) {
                    if (!inst.equals("")) {
                        %>
                        <TR>
                            <TD class="sub3titulo" colspan="7">&nbsp;<%=descriptor.getDescripcion("etiqInstrucc")%></TD>
                        </TR>
                        <tr>
                            <td style="width: 100%" class="columnP">
                                <html:textarea styleClass="textoArea" cols="114" rows="11" property="instrucciones" readonly="true"></html:textarea>
                            </td>
                        </tr>                                                    
                        <%
                    }
                }
                        %>
                    </table>
                </div>
                <%
            } else {
                %>
                <DIV id="capaEnlaces" name="capaEnlaces" style="display:none"></DIV>
                <%
            }
                %>
                <% if (tramExpForm.getMostrarFormsPDF()) {%>
                <jsp:include page="/jsp/sge/capaFormsPDF.jsp" /> 
                <%}
                %>


            <!-------------- MODULOS DE INTEGRACIÓN EXTERNOS ----------------->
            <%
                ArrayList<ModuloIntegracionExterno> modulos = tramExpForm.getModulosExternos();
                for(int i=0;modulos!=null && i<modulos.size();i++) {
                    ModuloIntegracionExterno modulo = modulos.get(i);                                                
                    String nombreModulo = modulo.getNombreModulo();                                                                                                  
                    ArrayList<DatosPantallaModuloVO> pantallas = modulo.getListaPantallasTramite();

                    for(int j=0;pantallas!=null && j<pantallas.size();j++) {
                        String url = pantallas.get(j).getUrl();
                        String operacion = pantallas.get(j).getOperacionProceso();
            %>                                               
                    <jsp:include page="<%=url%>" flush="true">
                        <jsp:param name="idioma" value="<%=idioma%>"/>
                        <jsp:param name="nombreModulo" value="<%=nombreModulo%>"/>
                        <jsp:param name="codOrganizacionModulo" value="<%=tramExpForm.getCodMunicipio()%>"/>
                        <jsp:param name="codigoTramite" value="<%=tramExpForm.getCodTramite()%>"/>
                        <jsp:param name="ocuTramite" value="<%=tramExpForm.getOcurrenciaTramite()%>"/>
                        <jsp:param name="numero" value="<%=tramExpForm.getNumero()%>"/>
                        <jsp:param name="operacion" value="<%=operacion%>"/>
                    </jsp:include>
           <%
                    }//for
                }// for
            %>

            <!--------------- MODULOS DE INTEGRACIÓN EXTERNOS ---------------->
            </div>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <logic:notEmpty name="TramitacionExpedientesForm" property="tienePermisoSalidas">
                <input type= "button" class="botonLargo" accesskey="S" name="cmdGenerarSalida" onclick="pulsarGenerarSalida();" value="<%=descriptor.getDescripcion("gbGenerarSalida")%>">
        </logic:notEmpty>
        <% if(tramExpForm.tieneTareasPendientesInicio()) {
        %>            
            <input type= "button" class="botonLargo" accesskey="G" name="cmdTareasPendientes" onclick="pulsarEjecutarTareasPendientes();" value="Ejecutar tareas">
        <%}//if%>
        <input type= "button" class="botonGeneral" accesskey="G" name="cmdBloquear" onclick="pulsarBloquear();" value="<%=descriptor.getDescripcion("gbBloquear")%>">
        <input type= "button" class="botonGeneral" accesskey="G" name="cmdDesbloquear" onclick="pulsarDesbloquear();" value="<%=descriptor.getDescripcion("gbDesbloquear")%>">
        <input type= "button" class="botonGeneral" accesskey="G" name="cmdGrabar" onclick="pulsarGrabar();" value="<%=descriptor.getDescripcion("gbGrabar")%>">
        <input type= "button" class="botonLargo" accesskey="F" name="cmdFinalizar" id="cmdFinalizar" onclick="pulsarFinalizar();" value="<%=descriptor.getDescripcion("gbFinalizar")%>">
        <input type= "button" class="botonGeneral" accesskey="V" name="cmdVolver" id="cmdVolver" onclick="pulsarVolver();" value="<%=descriptor.getDescripcion("gbVolver")%>">
    </div>
</div>

</html:form>

<%
    Vector estructuraDatosSuplementariosAux = null;
    Vector valoresDatosSuplementariosAux = null;
    estructuraDatosSuplementariosAux = (Vector) tramExpForm.getEstructuraDatosSuplementarios();
    valoresDatosSuplementariosAux = (Vector) tramExpForm.getValoresDatosSuplementarios();
    if (estructuraDatosSuplementariosAux != null) {
        int lengthEstructuraDatosSuplementariosAux = estructuraDatosSuplementariosAux.size();
        int lengthValoresDatosSuplementariosAux = valoresDatosSuplementariosAux.size();
        if (lengthEstructuraDatosSuplementariosAux>0 ) {
            int j=0;
            for (int i=0;i<lengthEstructuraDatosSuplementariosAux;i++)
            {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valoresDatosSuplementariosAux.elementAt(i);
                String nombre = "T_" + eC.getCodTramite() + "_" + eC.getOcurrencia() + "_" + eC.getCodCampo();
                String tipo = eC.getCodTipoDato();
                String valor = cF.getString(eC.getCodCampo());
                if("SI".equals(eC.getBloqueado())) eC.setSoloLectura("true");
                if (tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegable"))  ) {
                   %>

                    <script type="text/javascript">
                         eval("var combo<%=nombre%> = new Combo('<%=nombre%>',<%=idioma%>)");
                          eval("combo<%=nombre%>.addItems2(<%=eC.getListaCodDesplegable()%>,<%=eC.getListaDescDesplegable()%>,<%=eC.getListaEstadoValorDesplegable()%>)");
                         eval("combo<%=nombre%>.buscaLinea('<%=valor%>');");
                          <%if ((eC.getSoloLectura().equals("true"))||("SI".equals(eC.getBloqueado()))) {%>
                    eval("combo<%=nombre%>.deactivate();");
                         <%}%>
                        nombreCombos[<%=j%>]=['<%=nombre%>'];
                        <%j++;%>
                    </script>
            <%}
            }
        }
    }
%>
<script type="text/javascript">
var campos_vacios = false;
var nombre_campos_vacios = "";
recargarCamposCalculados();
var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
        '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
        '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
        '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
        document.getElementById('tablaDoc'));
tab.addColumna('240','left',"<%= descriptor.getDescripcion("gEtiqDocumento")%>");
tab.addColumna('100','center',"<%= descriptor.getDescripcion("etiq_fCrea")%>");
tab.addColumna('110','center',"<%= descriptor.getDescripcion("etiq_fMod")%>");
tab.addColumna('110','center',"Fecha del informe");
tab.addColumna('150','center',"<%= descriptor.getDescripcion("gEtiq_usuar")%>");
tab.addColumna('100','center',"<%= descriptor.getDescripcion("etiq_estadoFirma")%>");
tab.addColumna('70','center',"<%= descriptor.getDescripcion("editorTexto")%>");

tab.displayCabecera=true;
tab.displayTabla();

function refresca() {
  tab.displayTabla();
}
function mostrarCapas(nombreCapa) {
  var capas = document.getElementsByName(nombreCapa);
  if(capas==null || capas.length==0){	
      capas = document.getElementsByClassName(nombreCapa);	
  }
  
  for (var i=0;i<capas.length;i++){
      capas[i].style.display='';
  }
}

//Usado para el calendario
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
    if(window.event)
        aux = window.event;
    else
        aux = evento;

    var tecla = 0;
    if(aux.keyCode)
        tecla = aux.keyCode;
    else
        tecla = aux.which;


  if('Alt+F'==tecla) {
    pulsarFinalizar();
  } else if('Alt+G'==tecla) {
    pulsarGrabar();
  } else if('Alt+V'==tecla) {
    pulsarVolver();
  }

  if (tecla == 38 || tecla == 40){
    upDownTable(tab,listaDocumentos);
  }
  if(tecla == 13){
	if((tab.selectedIndex>-1)&&(tab.selectedIndex < listaDocumentos.length)){
      callFromTableTo(tab.selectedIndex,tab.id);
    }
  }
  if(tecla == 1){
      if(IsCalendarVisible) replegarCalendario(coordx,coordy);
      for(i=0;i<nombreCombos.length;i++) {
        var nCombo=eval('combo'+nombreCombos[i]);
         eval('if (nCombo.base.style.visibility == "visible") nCombo.ocultar()');
      }
  }
  if(tecla == 9){
    if(IsCalendarVisible) hideCalendar();
       for(i=0;i<nombreCombos.length;i++) {
            var nCombo=eval('combo'+nombreCombos[i]);
            eval('nCombo.ocultar()');
       }
  }

  keyDel(aux);
}

function inicializarBotonesDatosSuplementarios(){
    var botones=[];
    var j=0;
    if((document.forms[0].modoConsulta.value == "si") || (document.forms[0].bloqueo.value=="2") || ((document.forms[0].fechaFin.value != "") && (document.forms[0].fechaFin.value != null))) {
        <%String[] matriz= campos.split(",");
        String aux="";
        for (int i=0;i<matriz.length;i++){
           aux=matriz[i];%>
            if ((<%=(aux!=null)%>) && (<%=(!aux.equals(""))%>) && (eval("document.forms[0].<%=aux%>.value")!=""))
                botones[j++]= document.forms[0].cmdVisualizar<%=matriz[i].toUpperCase()%>;
            if (document.getElementById("imagenBoton<%=matriz[i].toUpperCase()%>")!=null)
                document.getElementById("imagenBoton<%=matriz[i].toUpperCase()%>").style.color="#f6f6f6";
        <%}%>
        habilitarGeneral(botones);
    } else {
        <%matriz= campos.split(",");
      for (int i=0;i<matriz.length;i++){%>
        if (<%=(matriz[i]!=null && !matriz[i].equals(""))%> && eval("document.forms[0].<%=matriz[i]%>.value")=="") {
            botones[j++]= document.forms[0].cmdVisualizar<%=matriz[i].toUpperCase()%>;
            botones[j++]= document.forms[0].cmdEliminar<%=matriz[i].toUpperCase()%>;
            botones[j++]= document.forms[0].cmdCSV<%=matriz[i].toUpperCase()%>;
          }
        <%}%>
        deshabilitarGeneral(botones);
    }

<% if (tramExpForm.getMostrarFormsPDF()) {%>
    tratarBotonesFormsPDF();
<%}%>
}

function antesDeCambiarPestana() {
    compruebaModificadoTramitacion();    
}


function compruebaModificadoTramitacion(){
    <%Vector valoresDatosSuplementarios = (Vector) tramExpForm.getValoresDatosSuplementarios();
    int lengthValoresDatosSuplementarios = valoresDatosSuplementarios.size();%>
    
    //tabPage1 datos generales
    var selected1 = document.getElementById("pestana1").className;
    var changeSelected1 = false;
    if ((selected1 == "tab selected") || (selected1 == "tab-remark selected")) {
        changeSelected1 = true;
    }
    if (document.forms[0].observaciones.value!="") {
        if (changeSelected1) document.getElementById("pestana1").className="tab-remark selected";
        else document.getElementById("pestana1").className="tab-remark";
    } else{
        if (changeSelected1) document.getElementById("pestana1").className="tab selected";
        else document.getElementById("pestana1").className="tab";
    }
      
    //tabPage2 datos
    if (document.getElementById("pestana2") != null) {
        var marcado2 = false;
        <% 
        Vector estructuraDatosSuplementarios = (Vector) tramExpForm.getEstructuraDatosSuplementarios();
        if (estructuraDatosSuplementarios != null && estructuraDatosSuplementarios.size() > 0) {
            for(int k=0;k<estructuraDatosSuplementarios.size();k++){
                EstructuraCampo estCampo = (EstructuraCampo)estructuraDatosSuplementarios.get(k);
                String nameCampo = estCampo.getCodCampo();
                if (estCampo.getCodTramite() != null) {
                    if (estCampo.getCodTipoDato().equals("5")) {
                        nameCampo = estCampo.getCodCampo();
                        if (estCampo.getOcurrencia() != null) nameCampo += "_" +estCampo.getOcurrencia();
                    } else if (estCampo.getCodTipoDato().equals("6")) {
                        nameCampo = estCampo.getCodCampo();
                        if (estCampo.getOcurrencia() != null)
                            nameCampo = "codT_" + estCampo.getCodTramite() + "_" + estCampo.getOcurrencia() + "_" + estCampo.getCodCampo();
                    } else if (estCampo.getOcurrencia() != null) {
                        nameCampo = "T_" + estCampo.getCodTramite() + "_" + estCampo.getOcurrencia() + "_" + estCampo.getCodCampo();
                    } else {
                        nameCampo = "T_" + estCampo.getCodTramite() + "_" + estCampo.getCodCampo();
                }
                }
        %>
                if(document.getElementsByName("<%=nameCampo%>")[0].value!="") marcado2=true;
                //if (document.getElementById("<%=nameCampo%>").value != "") marcado2=true;
        <%
            }
        }
        %> 

        var selected2 = document.getElementById("pestana2").className;
        var changeSelected2 = false;
        if ((selected2 == "tab selected") || (selected2 == "tab-remark selected")) {
            changeSelected2 = true;
        }
        if (marcado2) {
            if (changeSelected2) document.getElementById("pestana2").className="tab-remark selected";
            else document.getElementById("pestana2").className="tab-remark";
        } else{
            if (changeSelected2) document.getElementById("pestana2").className="tab selected";
            else document.getElementById("pestana2").className="tab";
        }
    }
    
    //tabPage3 documentos
    if (document.getElementById("pestana3") != null) {
        var selected3 = document.getElementById("pestana3").className;
        var changeSelected3 = false;
        if ((selected3 == "tab selected") || (selected3 == "tab-remark selected")) {
            changeSelected3 = true;
        }
        if (listaDocumentos.length > 0) {
            if (changeSelected3) document.getElementById("pestana3").className="tab-remark selected";
            else document.getElementById("pestana3").className="tab-remark";
        } else{
            if (changeSelected3) document.getElementById("pestana3").className="tab selected";
            else document.getElementById("pestana3").className="tab";
        }   
    }
}

if(tieneTareasPendientesInicio){    
    mostrarMsgTareasPendientesInicio();
}

function mostrarMsgTareasPendientesInicio(){

    var mensaje = "";

    <logic:iterate name="TramitacionExpedientesForm" property="tareasPendientesInicioTramite" id="tarea">
        mensaje = mensaje + "-&nbsp;" + '<bean:write name="tarea" property="mensajeError"/>' + "</br>";
    </logic:iterate>

    document.getElementById("msgTareasPendientesInicio").innerHTML = mensaje;
    document.getElementById("msgTareasFila").style.display = "";
}

function actualizarTareasPendientesInicio(continuar , mensaje, mensajeCompleto){    
    if(continuar){
        tieneTareasPendientesInicio = false;
        document.getElementById("msgTareasPendientesInicio").innerHTML = "";
        document.forms[0].cmdTareasPendientes.style.visibility = "hidden";
        document.getElementById("msgTareasFila").style.display = "none";
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgEjecucTareasPendientesExito")%>');        
    }else{
        document.getElementById("msgTareasPendientesInicio").innerHTML = mensajeCompleto;
        document.getElementById("msgTareasFila").style.display = "";
        tieneTareasPendientesInicio = true;
        jsp_alerta("A",mensaje);
    }
}

function recargarCamposCalculados() {        
    var ajax = getXMLHttpRequest();
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var numero           = document.forms[0].numero.value;   
    var codTramite       = document.forms[0].codTramite.value;
    
    var expedienteHistorico = <%=tramExpForm.isExpHistorico()%>;
    if(!expedienteHistorico){    
        // Si el expediente no está en el histórico, se procede a realizar la recargar
        // de los campos suplementarios
        if(ajax!=null)
        {                
            var i;
            var j;
            var valor = "";
            var url = "<%=request.getContextPath() %>" + "/sge/TramitacionExpedientes.do";              
            var cadena = "<%=nombre_campo%>";
            var cadena_descrip = "<%=nombre_campo_descripcion%>";
            var cadena_carga = "<%=nombre_campo_carga%>";
            var campos2 = cadena.split("#");
            var campos_carga = cadena_carga.split("#");
            var campos_desc = cadena_descrip.split("#");
            var hayCamposCalculados="<%=existenCamposCalculados%>";

            var carga = false;
            nombre_campos_vacios = "";

            var parametros = "&opcion=calculo_expresion&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                                + "&numero=" + escape(numero) + "&codTramite=" + escape(codTramite);

            if(hayCamposCalculados=='true'){

                if (cadena != ""){
                    for (i = 0; i < campos2.length; i++) 
                    {   
                        valor = eval("document.forms[0]."+campos_carga[i]+".value");
                        valor = valor.replace(".","");
                        valor = valor.replace(",",".");    
                        parametros = parametros + "&" + campos2[i] + "=" + valor;
                    }
                }
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);
                try
                {            
                    if (ajax.readyState==4 && ajax.status==200)
                    {
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                       var text = ajax.responseText;               
                       var datosRespuesta = text.split("#");               
                       campos_vacios = false;
                       for (i = 0; i < datosRespuesta.length - 1 ; i++) 
                       {                   
                           var linea = datosRespuesta[i].split("|");                   
                           for(j=0; j< campos2.length;j++)
                           {                    
                               if(campos2[j] == linea[0])
                                { 
                                   linea[1] = linea[1].replace(".",",");                                            
                                   eval("document.forms[0]."+campos_carga[j]+".value='"+linea[1]+"';");
                                   result = eval("document.forms[0]."+campos_carga[j]+".value;");
                                   eval("document.forms[0].cmdVerOpe_"+campos_carga[j]+".style.display='';");                               
                                   if (result == "")
                                   {
                                        campos_vacios = true                                
                                        carga = true
                                   }                
                                   if (carga == true)
                                   {
                                        if (nombre_campos_vacios == "")
                                            nombre_campos_vacios = campos_desc[j].toString();
                                        else
                                            nombre_campos_vacios = nombre_campos_vacios + "#" + campos_desc[j].toString();                           
                                        carga = false;
                                   }
                                }
                           }                                                           
                       }
                    }                
                }catch(Err){
                    alert("Error.descripcion: " + Err.description);
                }
            }
        }//if(ajax!=null)
    }
}//recargarDatosCalculados

function pulsarVerExpre(nombre)
{
    var ajax = getXMLHttpRequest();
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio = document.forms[0].codMunicipio.value;
    var codTramite = document.forms[0].codTramite.value;
    var ocurrencia = document.forms[0].ocurrenciaTramite.value;
    var cadena = "";
    if(ajax!=null)
    {       
        //le pasamos como parametro el nombre del campo sin tener en cuenta la composicion que se realiza teniendo en cuenta la ocurrencia y los tramites.        
        if (ocurrencia == "" || ocurrencia == null)
            cadena = "T_" + codTramite + "_";
        else
            cadena = "T_" + codTramite + "_" + ocurrencia +"_";
                    
        nombre = nombre.substring(cadena.length,nombre.length);
       
        
        var url = "<%=request.getContextPath() %>" + "/sge/TramitacionExpedientes.do";       
        var parametros = "&opcion=recuperarExpresion&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                            + "&campo=" + nombre +"&tramite=" + codTramite;
                    
        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        try
        {            
            if (ajax.readyState==4 && ajax.status==200)
            {         
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var text = ajax.responseText;                                             
               if (text.trim() != ""){                                        
                       abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mostrarExpresion.jsp'/>?sql="+text,'',
                                'width=550,height=350,scrollbars=no,status='+ '<%=statusBar%>',function(){});
                   }
            }
               
        }catch(Err){
            alert("Error.descripcion: " + Err.description);            
        }
    }//if(ajax!=null)
}
function pulsarVerExterno(nombre)
{  
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;           
    var codTramite       = document.forms[0].codTramite.value;
    var ocurrencia       = document.forms[0].ocurrenciaTramite.value;
    var valor = eval("document.forms[0]."+nombre+".value");
    var cadena_nombre = "";
    var cadena = "";
    
    if (ocurrencia == "" || ocurrencia == null)
        cadena = "T_" + codTramite + "_";
    else
        cadena = "T_" + codTramite + "_" + ocurrencia +"_";

    cadena_nombre = nombre.substring(cadena.length,nombre.length);
     
    var parametros = "?opcion=recuperarExterno&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                            + "&campo=" + cadena_nombre +"&tramite=" + codTramite + "&valor_dato='" + valor + "'&ocurrecia=" + ocurrencia;
        
    abrirXanelaAuxiliar("<html:rewrite page='" + "/sge/TramitacionExpedientes.do" + "'/>" + parametros,'',
	'width=700,height=450,scrollbars=no',function(result){
                            if (result != null){            
                                eval("document.forms[0]."+nombre+"_CODSEL.value='"+result[0]+"'");
                                eval("document.forms[0]."+nombre+".value='"+result[1]+"'");
                            }        
                    });
}



function interesado_Obligatorio(){    
    if(esInteresadoObligatorio=="true") 
        return "1";
    else
        return "0";
}

function getContextPath(){
    var contextPath = '<%=request.getContextPath()%>';
    return contextPath;
}//getContextPath

                                                                                
</script>
<% if (tramExpForm.getMostrarFormsPDF()) {%>
<jsp:include page="/jsp/sge/scriptsFormsPDF.jsp" />
<%}%>
<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>
</html:html>
