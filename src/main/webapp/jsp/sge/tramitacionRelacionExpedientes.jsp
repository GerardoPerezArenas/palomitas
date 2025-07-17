<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="java.util.ResourceBundle"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.technical.EstructuraCampo"%>
<%@ page import="es.altia.agora.technical.CamposFormulario"%>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DatosPantallaModuloVO"%>

<%Log mLog = LogFactory.getLog(this.getClass().getName());
  String fechaFinExpediente = "";
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
  
  HashMap mapa= tramExpForm.getMapa();
  if (mapa!=null){
      mLog.debug("TramitacionRelacionExpedientes.jsp: . Tamanho del mapa"+mapa.size());
  }
  
  mLog.debug("FECHA FIN =" + fechaFinExpediente + "-----");
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String campos="";

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
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<html:html><head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE10"/>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES  Tramitación de Expedientes :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/vbscript" src="<c:url value='/scripts/documentos.vbs'/>"></script>


<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

<SCRIPT type="text/javascript">
var listaDocumentos = new Array();
var listaDocumentosOriginal = new Array();
var listaCodDocumentosTramite = new Array();
var listaEnlaces = new Array();
var listaCodInteresados = "";
var listaVersInteresados = "";
var listaExpNoInteresados = new Array();
var nombreCombos = new Array();
var cont = 0;
var cont1 = 0;
var cont2 = 0;
var cont3 = 0;
<c:set var="JS_DEBUG_LEVEL" value="0" />
function getEstadoFirmaVisual( codigoDocumento, codigoEstadoFirma) {
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.getEstadoFirmaVisual("+codigoDocumento+","+codigoEstadoFirma+") BEGIN");
    </c:if>
    var result = "";
    if ( (!codigoEstadoFirma) || (codigoEstadoFirma=='') ) result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Null'/>";
    if ( (codigoDocumento) && (codigoEstadoFirma) ) {
        var paramCodigoDocumento=''+codigoDocumento+'';
        var portafirmas = "<%=PORTAFIRMAS%>";
        if (codigoEstadoFirma == 'E') {
            if (""!=portafirmas) {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.E'/> - <a href='javascript:enviarDocumentoAFirma("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarFirma'/></a>";
            } else {
                result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.E'/>";
            }
        } else if (codigoEstadoFirma == 'O') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.O'/> - <a href='javascript:verDatosFirmaDocumento("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.VerDatos'/></a>";
        } else if (codigoEstadoFirma == 'T') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.T'/> - <a href='javascript:iniciarFirmaDocumento("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Firmar'/></a>";
        } else if (codigoEstadoFirma == 'F') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.F'/> - <a href='javascript:verDatosFirmaDocumento("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.VerDatos'/></a>";
        } else if (codigoEstadoFirma == 'R') {
            result = "<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.R'/> - <a href='javascript:verDatosFirmaDocumento("+paramCodigoDocumento+")'><fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.VerDatos'/></a>";
        } else {
            result = codigoEstadoFirma;
        }
    }
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.getEstadoFirmaVisual("+codigoDocumento+","+codigoEstadoFirma+")="+result+" END");
    </c:if>
    return result;
}
function enviarDocumentoAFirma( codigoDocumento ) {
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.enviarDocumentoAFirma("+codigoDocumento+") BEGIN");
    </c:if>
    if(jsp_alerta("C",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.EnviarFirma.EstaSeguro'/>')) {
        <c:if test="${JS_DEBUG_LEVEL >= 70}">
        alert("TramitacionExpedientes.enviarDocumentoAFirma() ENVIANDO DOCUMENTO A FIRMA!");
        </c:if>
        document.forms[0].codDocumento.value = ""+codigoDocumento+"";
        document.forms[0].estadoFirma.value = 'O';
        document.forms[0].opcion.value="cambiarEstadoFirmaDocumentoCRD";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
        document.forms[0].submit();
    }
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("TramitacionExpedientes.enviarDocumentoAFirma() END");
    </c:if>
}
function iniciarFirmaDocumento( codigoDocumento) {
    <c:if test="${JS_DEBUG_LEVEL >= 80}">
    alert("TramitacionExpedientes.iniciarFirmaDocumento("+codigoDocumento+") BEGIN");
    </c:if>
    if(jsp_alerta("C",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Firmar.EstaSeguro'/>')) {
        var baseURL = '<c:url value="/sge/documentofirma/PrepareFirmaDocumentoTramitacion.do?"/>';
        var finalURL = baseURL;
        finalURL = finalURL + "idMunicipio="+document.forms[0].codMunicipio.value;
        finalURL = finalURL + "&idProcedimiento="+document.forms[0].codProcedimiento.value;
        finalURL = finalURL + "&idEjercicio="+document.forms[0].ejercicio.value;
        //finalURL = finalURL + "&idNumeroExpediente="+document.forms[0].numeroExpediente.value;
        finalURL = finalURL + "&idNumeroExpediente="+document.forms[0].numeroRelacion.value;
        finalURL = finalURL + "&idTramite="+document.forms[0].codTramite.value;
        finalURL = finalURL + "&idOcurrenciaTramite="+document.forms[0].ocurrenciaTramite.value;
        finalURL = finalURL + "&idNumeroDocumento="+codigoDocumento;
        finalURL = finalURL + "&tipoDocumento=1";
        
        <c:if test="${JS_DEBUG_LEVEL >= 80}">
        alert("TramitacionExpedientes.iniciarFirmaDocumento() finalURL="+finalURL);
        </c:if>
        abrirXanelaAuxiliar(finalURL, null,'width='+700+',height='+400+',status=no,resizable=no,scrollbars=no',function(){
                    cargaDocumentos();
                });
    }
    <c:if test="${JS_DEBUG_LEVEL >= 80}">
    alert("TramitacionExpedientes.iniciarFirmaDocumento() END");
    </c:if>
}
function verDatosFirmaDocumento( codigoDocumento ) {
    <c:if test="${JS_DEBUG_LEVEL >= 80}">
    alert("TramitacionExpedientes.verDatosFirmaDocumento("+codigoDocumento+") BEGIN");
    </c:if>
    var finalURL = '<c:url value="/sge/documentofirma/ViewDocumentoFirma.do?"/>';
    finalURL = finalURL + "idMunicipio="+document.forms[0].codMunicipio.value;
    finalURL = finalURL + "&idProcedimiento="+document.forms[0].codProcedimiento.value;
    finalURL = finalURL + "&idEjercicio="+document.forms[0].ejercicio.value;
    finalURL = finalURL + "&idNumeroExpediente="+document.forms[0].numeroRelacion.value;
    finalURL = finalURL + "&idTramite="+document.forms[0].codTramite.value;
    finalURL = finalURL + "&idOcurrenciaTramite="+document.forms[0].ocurrenciaTramite.value;
    finalURL = finalURL + "&idNumeroDocumento="+codigoDocumento;
    finalURL = finalURL + "&tipoDocumento=1";
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
  if(respOpcion == "cambiadoEstadoFirmaDocumentoCRD") {
    var ccedi = 0;
    for (ccedi=0; ccedi < listaDocumentosOriginal.length; ccedi++) {
        if (listaDocumentosOriginal[ccedi][0]==codigoDocumento) {
            listaDocumentos[ccedi][4]= getEstadoFirmaVisual(codigoDocumento,nuevoEstadoFirma);
            listaDocumentosOriginal[ccedi][6]=nuevoEstadoFirma;
        }
    }
    tab.lineas=listaDocumentos;
    refresca();
    jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Cambio.Hecho'/>');
  } else if (respOpcion == "cambiadoEstadoFirmaDocumentoCRDNoEnviadoMail") {
      var ccedi = 0;
      for (ccedi=0; ccedi < listaDocumentosOriginal.length; ccedi++) {
          if (listaDocumentosOriginal[ccedi][0]==codigoDocumento) {
              listaDocumentos[ccedi][4]= getEstadoFirmaVisual(codigoDocumento,nuevoEstadoFirma);
              listaDocumentosOriginal[ccedi][6]=nuevoEstadoFirma;
          }
      }
      tab.lineas=listaDocumentos;
      refresca();
      jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Cambio.Hecho.No.Mail'/>');
  } else {
    jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.EstadoFirma.Accion.Cambio.NoHecho'/>');
  }
}
function comprobarFirmasAntesFinalizacionTramite() {
    var i = 0;
    var estadoDocumento = '';
    for (i = 0; i < listaDocumentosOriginal.length; i++) {
        estadoDocumento = listaDocumentosOriginal[i][6];
        if ( (estadoDocumento) && (estadoDocumento!='') ) estadoDocumento = estadoDocumento.toUpperCase();
        if ( (estadoDocumento=='E') || (estadoDocumento=='O') || (estadoDocumento=='T') ) <%-- || (estadoDocumento=='R') ) --%>
            return false;
    }
    return true;
}

function getExceptionMessage(jsEx) {
    if (jsEx) {
        if (jsEx.description == null) return jsEx.message;
        else return jsEx.description;
    } else {
        return "";
    }
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



function getUrlForDownloadDocumentRelacion(idMunicipio, idNumeroRelacion, idTramite, 
    idOcurrenciaTramite, idNumeroDocumento, nombreDocumento, editorTexto) {
    <%String urlHost = StrutsUtilOperations.getProtocol(request) + "://" + request.getHeader("host");%>
    var numRelacion = idNumeroRelacion.replace("/","+").replace("/","+");
    var url = "<%=urlHost%>" + "<%=request.getContextPath()%>";
    url = url + "/temp/relaciones/<%=params[0]%>/<%=params[6]%>/" + idMunicipio + "/" +
                numRelacion + "/" +
                idTramite + "/" + idOcurrenciaTramite + "/" +
                idNumeroDocumento + "/" + nombreDocumento + (editorTexto == "OOFFICE"?".odt":".doc") +
                "?jsessionid=" + "<%=session.getId()%>";
    return url;
}


function pulsarVerDoc() {
  var i = tab.selectedIndex;
  if(i != -1) {
      var url = getUrlForDownloadDocumentServlet(
          document.forms[0].codMunicipio.value,
          document.forms[0].numeroExpediente.value,
          document.forms[0].codTramite.value,
          document.forms[0].ocurrenciaTramite.value,
          listaDocumentosOriginal[i][0],
          false);
            
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
  tp1.setSelectedIndex(0);
  if(document.forms[0].plazo.value != 0) {
    mostrarCapas('capaPlazo');
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
    document.forms[0].cmdVolver.disabled = false;
  }
  if(document.forms[0].permiso.value == "no") {
    desactivarFormulario();
    document.forms[0].cmdVolver.disabled = false;
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

  <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaDocumentos">
    opcionGrabar='<bean:write name="elemento" property="opcionGrabar"/>';
    if (opcionGrabar == '1') img ="<span class="fa fa-check 2x"></span>";
    else img ="";
    listaDocumentos[cont] = ['<bean:write name="elemento" property="descDocumento" />',
                          '<bean:write name="elemento" property="fechaCreacion" />',
                          '<bean:write name="elemento" property="fechaModificacion"/>',
                          '<bean:write name="elemento" property="usuario"/>',
                          getEstadoFirmaVisual('<bean:write name="elemento" property="codDocumento" />','<bean:write name="elemento" property="estadoFirma"/>'),
                          '<bean:write name="elemento" property="editorTexto"/>'
                            ];
    listaDocumentosOriginal[cont] = ['<bean:write name="elemento" property="codDocumento" />',
                          '<bean:write name="elemento" property="descDocumento" />',
                          '<bean:write name="elemento" property="fechaCreacion" />',
                          '<bean:write name="elemento" property="fechaModificacion"/>',
                          '<bean:write name="elemento" property="usuario"/>',
                          '<bean:write name="elemento" property="interesado"/>',
                          '<bean:write name="elemento" property="estadoFirma"/>',
                          '<bean:write name="elemento" property="opcionGrabar"/>',
                          '<bean:write name="elemento" property="editorTexto"/>'
                            ];

    cont++;
  </logic:iterate>
  tab.lineas=listaDocumentos;
if (listaDocumentos.length>0) document.forms[0].cmdVerDoc.disabled=false;
  refresca();
  <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaCodDocumentosTramite">
    listaCodDocumentosTramite[cont1] = ['<bean:write name="elemento" property="codDocumento" />'];
    cont1++;
  </logic:iterate>
  <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaEnlaces">
    listaEnlaces[cont2] = ['<bean:write name="elemento" property="descEnlace" />',
                           '<bean:write name="elemento" property="url" />'];
    cont2++;
  </logic:iterate>
  if ((document.forms[0].modoConsulta.value == "si") || (document.forms[0].bloqueo.value=="2")) {
    desactivarFormulario();
    document.forms[0].cmdVolver.style.cursor = "hand";
    document.forms[0].cmdVolver.disabled = false;
  }
  var observ = unescape('<bean:write name="TramitacionExpedientesForm" property="observaciones"/>');
  if (observ != "")
  {
  document.forms[0].observaciones.value = observ;
  }
  domlay('capaEnlaces',1,0,0,enlaces());
  var instrucc = unescape('<bean:write name="TramitacionExpedientesForm" property="instrucciones"/>');
  if (instrucc != "")
  {
	  document.forms[0].instrucciones.value = instrucc;
  }
  inicializarBotonesDatosSuplementarios();


<logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaExpedientesNoInteresados">
  listaExpNoInteresados[cont3] = ['<bean:write name="elemento" property="numExpedienteNoInteresado" />'];
  cont3++;
</logic:iterate>
}

function mostrarCalFechaInicioPlazo(evento) {
  if(window.event) evento = window.event;
  
  if (document.getElementById("calFechaInicioPlazo").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaInicioPlazo',null,null,null,'','calFechaInicioPlazo','',null,null,null,null,null,null,null,'calcularFechaLimite()',evento);
}

function mostrarCalFechaFinPlazo(evento) {
  if(window.event) evento = window.event;
  if (document.getElementById("calFechaFinPlazo").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaFinPlazo',null,null,null,'','calFechaFinPlazo','',null,null,null,null,null,null,null,null,evento);
}

function enlaces() {
  var htmlString = "";
  if (listaEnlaces.length > 0){
    htmlString += '<table border="0px" cellpadding="2" cellspacing="4" align="left" width="100%">';
    for(i=0; i < listaEnlaces.length; i++) {
      htmlString += '<tr><td width="100%" align="left" class="etiqueta">';
      htmlString += '<a href=' + listaEnlaces[i][1] + '?numeroExpediente=' + document.forms[0].numeroExpediente.value + '&codTramite=' + document.forms[0].codTramite.value;
	  htmlString += ' target=blank class=enlace><b>';
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
}
function pulsarVolver() {
  pleaseWait('on');
  document.forms[0].opcion.value="cargarPestTram";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/FichaRelacionExpedientes.do'/>";
  document.forms[0].submit();
}
function pulsarAltaDoc() {
  document.forms[0].opcionGrabar.value = "N";
  var source = "<c:url value='/sge/TramitacionRelacionExpedientes.do?opcion=listaDocumentosTramite'/>";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
	'width=700,height=450,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined){
                              document.forms[0].codDocumento.value = datosConsulta[0];
                              document.forms[0].codPlantilla.value = datosConsulta[1];
                              document.forms[0].nombreDocumento.value = datosConsulta[2];
                              document.forms[0].tipoPlantilla.value = datosConsulta[4];
                              document.forms[0].editorTexto.value=datosConsulta[5];
                              
                              if(datosConsulta[3] == "S") {
                                    var source1 = "<c:url value='/sge/TramitacionRelacionExpedientes.do?opcion=verInteresados&nCS='/>"+ document.forms[0].numeroRelacion.value+
                                                      "&codMun=" + document.forms[0].codMunicipio.value + "&codProc=" + document.forms[0].codProcedimiento.value+
                                                      "&eje=" + document.forms[0].ejercicio.value;
                                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source1,'ventana',
                                            'width=700,height=450,status='+ '<%=statusBar%>',function(datosConsulta1){
                                                    if(datosConsulta1!=undefined){
                                                          listaCodInteresados = datosConsulta1[1];
                                                          listaVersInteresados = datosConsulta1[2];
                                                    }
                                                    if (listaCodInteresados.length !=0){
                                                        document.forms[0].listaCodInteresados.value = listaCodInteresados;
                                                        document.forms[0].listaVersInteresados.value = listaVersInteresados;
                                                        document.forms[0].opcion.value="irAlAction";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
                                                        document.forms[0].submit();
                                                    }
                                                    listaCodInteresados = "";
                                                    listaVestInteresados = "";
                                            });
                              } else {
                                    document.forms[0].listaCodInteresados.value = listaCodInteresados;
                                    document.forms[0].listaVersInteresados.value = listaVersInteresados;
                                    document.forms[0].opcion.value="irAlAction";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
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
function editor() {
    if (document.forms[0].editorTexto.value=="WORD"){
  document.forms[0].opcion.value="altaDocumento";
  document.forms[0].target="oculto";
    }
    else{
        document.forms[0].opcion.value="altaDocumentoOOffice";
        document.forms[0].target="oculto";
        }
  
  document.forms[0].action="<c:url value='/sge/DocumentosRelacionExpedientes.do'/>";
  document.forms[0].submit();
  document.forms[0].listaCodInteresados.value = "";
  document.forms[0].listaVersInteresados.value = "";
}
function pulsarModificarDoc() {
    
  var i = tab.selectedIndex;
  if(i != -1) {      
    if ( (listaDocumentosOriginal[i][6]!='F') &&
         (listaDocumentosOriginal[i][6]!='R') &&
         (listaDocumentosOriginal[i][6]!='O') ) {
        document.forms[0].numeroDocumento.value = listaDocumentosOriginal[i][0];
        document.forms[0].nombreDocumento.value = listaDocumentosOriginal[i][1];
        document.forms[0].opcionGrabar.value = listaDocumentosOriginal[i][7];
        document.forms[0].target="oculto";
        document.forms[0].editorTexto.value = listaDocumentosOriginal[i][8];

        if (listaDocumentosOriginal[i][8]=="WORD")
            document.forms[0].opcion.value="modificarDocumento";
        else
            document.forms[0].opcion.value="modificarDocumentoOOffice";
            
        document.forms[0].action="<c:url value='/sge/DocumentosRelacionExpedientes.do'/>";
        document.forms[0].submit();
    } else {
        jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.ModificarDocumento.EstadoFirmaIncompatible'/>');
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
  document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
  document.forms[0].submit();
}
function actualizaTabla(lista,lista2) {
  listaDocumentos = new Array();
  listaDocumentosOriginal = new Array();
  listaDocumentos = lista;
  listaDocumentosOriginal = lista2;
  tab.lineas=listaDocumentos;
  refresca();
}
function pulsarEliminarDoc() { 
  if(tab.selectedIndex != -1) {
        /*
     if ( (listaDocumentosOriginal[tab.selectedIndex][6]!='F') &&
         (listaDocumentosOriginal[tab.selectedIndex][6]!='R') &&
         (listaDocumentosOriginal[tab.selectedIndex][6]!='O') ) { */
    if ( (listaDocumentosOriginal[tab.selectedIndex][6]!='F') &&
         (listaDocumentosOriginal[tab.selectedIndex][6]!='R')) {

            if(jsp_alerta("C","<%=descriptor.getDescripcion("msjBorrarDoc")%>")) {
              document.forms[0].editorTexto.value=listaDocumentosOriginal[tab.selectedIndex][8];
              document.forms[0].codDocumento.value = listaDocumentosOriginal[tab.selectedIndex][0];
              document.forms[0].opcion.value="eliminarDocumentoCRD";
              document.forms[0].target="oculto";
              document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>?nombreDocumento=" +  listaDocumentosOriginal[tab.selectedIndex][1];
              document.forms[0].submit();
            }
    }else{
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoElimDocEstFirmado")%>');
    }
    
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
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
}
function pulsarGrabar() {
  document.forms[0].opcion.value="grabarTramite";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
  document.forms[0].submit();
}
function grabacionTramite(respOpcion) {
  if ("noGrabado"==respOpcion){
      msnj = "<%=descriptor.getDescripcion("msjTramNoGrabado")%>";
      jsp_alerta("A",msnj);
  } else {
    msnj = "<%=descriptor.getDescripcion("msjTramGrabado")%>";
    jsp_alerta("A",msnj);
  }
}

function finalizarExpediente(mensaje) {
  jsp_alerta("A",mensaje);
}

    
function consultarTramites(nCS,oblig,literalFinalizarEntrada){
    literalFinalizar = literalFinalizarEntrada;
    var datosAEnviarTramites = new Array();
    datosAEnviarTramites[0] = nCS;
    datosAEnviarTramites[1] = oblig;
    var source1 = "<c:url value='/jsp/sge/listaTramitesTramitacion.jsp?opcion=null'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source1,datosAEnviarTramites,'width=680,height=500,status='+ '<%=statusBar%>', 
        function(datosConsultaT) {
        if(datosConsultaT!=undefined && datosConsultaT[0] =="si") {
            document.forms[0].listaCodTramites.value = datosConsultaT[1];
            document.forms[0].listaModoTramites.value = datosConsultaT[2];
            document.forms[0].listaUtrTramites.value = datosConsultaT[3];
            document.forms[0].listaTramSigNoCumplenCondEntrada.value= datosConsultaT[4];
            if(datosConsultaT[1] !="") {
                document.forms[0].opcion.value=literalFinalizar;
            } else {
                document.forms[0].opcion.value="finalizarSinCondicion";
            }
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";

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
    });
}
    
function pulsarFinalizar() {
    if( validarObligatoriosAqui() ) {
        if (!comprobarFirmasAntesFinalizacionTramite()) {
            jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.FinalizacionTramite.Error.AlgunDocumentoSinFirmar'/>');
        } else {
          if(document.forms[0].accion.value == "F") {
            if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinalRelacion")%>") == 1) {
              document.forms[0].opcion.value="finalizarExpediente";
              document.forms[0].target="oculto";
              document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
              document.forms[0].submit();
            }
          } else if(document.forms[0].accion.value == "T") {
            consultarTramites(0,document.forms[0].obligat.value,"finalizarConTramites");
          } else if(document.forms[0].accion.value == "R") {
            var datosAEnviar = new Array();
            datosAEnviar[0] = document.forms[0].accion.value;
            var source = "<c:url value='/jsp/sge/favorDesfavor.jsp?opcion=null'/>";
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,datosAEnviar,
	'width=700,height=330,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined){                
                              if(datosConsulta[0] =="siFavorable") {
                                if(document.forms[0].accionAfirmativa.value == "T") {
                                    consultarTramites(1,document.forms[0].obligat.value,"finalizarConResolucionFavorableConTramites");
                                } else {
                                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                                    document.forms[0].opcion.value="finalizarConResolucionFavorable";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
                                    document.forms[0].submit();
                                  }
                                }
                              } else if(datosConsulta[0] =="noFavorable") {
                                if(document.forms[0].accionNegativa.value == "T") {
                                    consultarTramites(2,document.forms[0].obligatorioDesf.value,"finalizarConResolucionDesfavorableConTramites");
                                } else {
                                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                                    document.forms[0].opcion.value="finalizarConResolucionDesfavorable";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
                                    document.forms[0].submit();
                                  }
                                }
                              }
                            }
                    });
          } else if(document.forms[0].accion.value == "P") {
            var datosAEnviar = new Array();
            datosAEnviar[0] = document.forms[0].accion.value;
            datosAEnviar[1] = document.forms[0].pregunta.value;
            var source = "<c:url value='/jsp/sge/favorDesfavor.jsp?opcion=null'/>";
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,datosAEnviar,
	'width=700,height=330,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined){
                              if(datosConsulta[0] =="siFavorable") {
                                if(document.forms[0].accionAfirmativa.value == "T") {
                                    consultarTramites(1,document.forms[0].obligat.value,"finalizarConPreguntaFavorableConTramites");
                                } else {
                                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                                    document.forms[0].opcion.value="finalizarConPreguntaFavorable";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
                                    document.forms[0].submit();
                                  }
                                }
                              } else if(datosConsulta[0] =="noFavorable") {
                                if(document.forms[0].accionNegativa.value == "T") {
                                    consultarTramites(2,document.forms[0].obligatorioDesf.value,"finalizarConPreguntaDesfavorableConTramites");
                                } else {
                                  if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                                    document.forms[0].opcion.value="finalizarConPreguntaDesfavorable";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
                                    document.forms[0].submit();
                                  }
                                }
                              }
                            }
                    });
          } else {
            if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondSinCond")%>") == 1) {
              document.forms[0].opcion.value="finalizarSinCondicion";
              document.forms[0].target="oculto";
              document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
              document.forms[0].submit();
            }
          }
        }
    }
}
function finalizacionTramite(respOpcion) {
  if ("noFinalizado"==respOpcion) {
    msnj = "<%=descriptor.getDescripcion("msjTramNoFinalizado")%>";
    jsp_alerta("A",msnj);
  } else if (respOpcion == "AutoRetrocedido") {
      jsp_alerta("A", "<%=descriptor.getDescripcion("msjTramAutoRet")%>")
  } else if (respOpcion == "FinalizadoNormal") {
      jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoIniciadosTodos")%>");
      pulsarVolver();
  } else {
    msnj = "<%=descriptor.getDescripcion("msjTramFinalizado")%>";
    jsp_alerta("A",msnj);
    pulsarVolver();
  }
}
function finalizacionExpediente(respOpcion){
  if ("expedienteNoFinalizado"==respOpcion) {
    msnj = "<%=descriptor.getDescripcion("msjRelNoFinalizado")%>";
    jsp_alerta("A",msnj);
  }else if ("expedienteConTramitesIniciados"==respOpcion) {
    msnj = "<%=descriptor.getDescripcion("msjRelTramIniciado")%>";
    jsp_alerta("A",msnj);
  }else {
    msnj = "<%=descriptor.getDescripcion("msjRelFinalizado")%>";
    jsp_alerta("A",msnj);
    pulsarVolverRelacionesPendientes(); // Pagina de buzon y expedientes pendientes.
  }
}

function tramitesPendientes(listaTramitesPendientes,notifRealizada, resultadoFinalizar) {
  var source = "<c:url value='/jsp/sge/informacionTramitesRelaciones.jsp?opcion=null'/>";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,listaTramitesPendientes,
	'width=680,height=400,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined){
                              finalizacionTramite(resultadoFinalizar);
                              if (notifRealizada=="no"){
                                  msnj = "<%=descriptor.getDescripcion("msjNotifNoRealiz")%>";
                                  jsp_alerta("A",msnj);
                              }
                            }
                    });
}
function pulsarVolverRelacionesPendientes(){
    pleaseWait('on');
    document.forms[0].opcion.value="pendientes";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
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
   if(inputFecha.value != "") {
     calcularFechaLimite();
   }
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
  document.forms[0].fechaFinPlazo.focus();
}
function validarObligatoriosAqui(){
	if (validarObligatorios("<%=descriptor.getDescripcion("msjObligTodos")%>") ) {
		if (document.getElementById("capaPlazo").style.visibility=="visible") {
			if ( (document.forms[0].plazo.value=="") || (document.forms[0].tipoPlazo.value=="")) {
					jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
					return false;
			}
		}
	} else return false;
	return true;
}
function callFromTableTo(rowID,tableName){
  if(document.forms[0].cmdGrabar.disabled == true) {
	document.forms[0].codDocumento.value = listaDocumentosOriginal[rowID][0];
	document.forms[0].nombreDocumento.value = listaDocumentosOriginal[rowID][1];
	verDoc();
  }
}

function pulsarBloquear() {
    if(jsp_alerta("C","<%=descriptor.getDescripcion("msjBlqTramite")%>")) {
        document.forms[0].opcion.value="bloquear";
        document.forms[0].target="mainFrame";
        document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
        document.forms[0].submit();
    }
}

function pulsarDesbloquear() {
    if(jsp_alerta("C","<%=descriptor.getDescripcion("msjDblqTramite")%>")) {
        document.forms[0].opcion.value="desbloquear";
        document.forms[0].target="mainFrame";
        document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
        document.forms[0].submit();
    }
}
</SCRIPT>

<script type="text/javascript">
    function ventanaPopUpModal(opcion,codigo) {
        if((document.forms[0].modoConsulta.value != "si") && (document.forms[0].bloqueo.value == "0") && ((document.forms[0].fechaFin.value == "") || (document.forms[0].fechaFin.value == null))) {
            //day = new Date();
            //id = day.getTime();
            //eval("page" + id + " = window.open(<html:rewrite page='/sge/DatosSuplementariosFichero.do'/>, '" + id + "', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=440,height=175');");
            //window.open("<html:rewrite page='/sge/DatosSuplementariosFichero.do'/>?opcion=" + opcion +"&codigo="+ codigo ,"ventana1","left=10, top=10, width=500, height=200, scrollbars=no, menubar=no, location=no, resizable=no")
             var source = "<html:rewrite page='/sge/DatosSuplementariosFichero.do'/>?opcion=" + opcion + "&codigo="+ codigo;
             abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,"ventana1",
	'width=450,height=450,scrollbars=no,status='+ '<%=statusBar%>',function(result){
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
    }

    function onClickDocumento(codigo, nombreFich) {
        window.open("<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios?codigo=" + codigo +
                    "&nombreFich=" + nombreFich + "&opcion=2", "ventana1",
                    "left=10, top=10, width=1, height=1, scrollbars=no, menubar=no, location=no, resizable=no");
    }

    function onClickEliminarDocumento(codigo) {
        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjBorrarFichero")%>') ==1) {
            eval("document.forms[0]."+codigo+".value='';");
            eval("deshabilitarBoton(document.forms[0].cmdVisualizar"+codigo+");");
            eval("deshabilitarBoton(document.forms[0].cmdEliminar"+codigo+");");
        }
    }

function modificaVariableCambiosCamposSupl() //Solo tiene sentido en la ficha de expediente, pero es necesario definirla aqui tambien
{}

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
                                'width=400,height=450,status='+ '<%=statusBar%>',function(rslt){});
                   }
               }
            }
            
            function onClickDesactivar(idCampo){

                if (idCampo==null)return;

               var cmdActivar = document.getElementById("cmdActivar"+idCampo);
               var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
               var campo =  document.getElementById("activar"+idCampo);
               //al pulsar bton desactivar, se oculta el bton activar
               cmdActivar.style.visibility='visible';
               cmdActivar.style.display='inline';
               cmdDesactivar.style.display='none';
               campo.value="desactivada"; 

            }
            function onClickActivar(idCampo){
                 if (idCampo==null)return;

               var cmdActivar = document.getElementById("cmdActivar"+idCampo);
               var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
               var campo =  document.getElementById("activar"+idCampo);

             //al pulsar bton desactivar, se oculta el bton activar
             cmdDesactivar.style.visibility='visible';
             cmdActivar.style.display='none';
             cmdDesactivar.style.display='inline';
             campo.value="activada"; 

            }
</script>

<script type="text/vbscript">
Sub verDoc()
Dim plantilla
If (Not IsNull(document.forms(0).codDocumento.value) And Not IsEmpty(document.forms(0).codDocumento.value) And Not(document.forms(0).codDocumento.value="")) Then
	plantilla = "<%=dominioFinal%>/temp/relaciones/<%=params[0]%>/<%=params[6]%>/"& _
	            replace(document.forms(0).numeroRelacion.value,"/","%2F") & "/" &_
				document.forms(0).codTramite.value & "/" &_
				document.forms(0).ocurrenciaTramite.value & "/" &_
				document.forms(0).codDocumento.value & "/" &_
				document.forms(0).nombreDocumento.value & ".doc"
	Call verDocumentoDesdeTramitacion(plantilla)
End If
End Sub
</script>
</head>
<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">
            
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
            
<html:form action="/sge/TramitacionRelacionExpedientes.do" target="_self">
<html:hidden  property="opcionGrabar" value=""/>
<html:hidden  property="opcion" value=""/>
<html:hidden  property="numeroExpediente" />
<html:hidden  property="numeroRelacion" />
<html:hidden  property="codProcedimiento" />
<html:hidden  property="codMunicipio" />
<html:hidden  property="ejercicio" />
<html:hidden  property="numero" />
<html:hidden  property="ocurrenciaTramite" />
<html:hidden  property="codDocumento" />
<html:hidden  property="estadoFirma" />
<html:hidden  property="codPlantilla" />
<html:hidden  property="tipoPlantilla" value=""/>
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
<html:hidden  property="editorTexto"/>
<div class="txttitblanco"><%=descriptor.getDescripcion("tit_tramit")%> : <%=descriptor.getDescripcion("tit_Rel")%></div>
<div class="encabezadoGrisGrande">
    <table width="100%">
        <tr>
            <td style="width:9%" class="etiqueta"><%=descriptor.getDescripcion("etiq_numRel")%>:&nbsp;</td>
            <td style="width:20%" class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="numeroRelacionMostrar" maxlength="30" style="width:99%" readonly="true" onkeypress="javascript:PasaAMayusculas(event);"/>
            </td>
            <td class="columnP" style="padding-right:10px">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="procedimiento" style="width:100%" maxlength="255" readonly="true" onkeypress="javascript:PasaAMayusculas(event);"/>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tramite")%>:&nbsp;</td>
            <td colspan="2" class="columnP" style="padding-right:10px">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="tramite" style="width:100%" maxlength="255" readonly="true"
                           onkeypress="javascript:PasaAMayusculas(event);"/>
            </td>
        </tr>                                                        
    </TABLE>
</div>
<div class="contenidoPantalla">
    <div class="tab-pane" id="tab-pane-1">
        <script type="text/javascript">
            tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
        </script>
        <!-- CAPA 1: DATOS GENERALES ------------------------------ -->
        <div class="tab-page" id="tabPage1" >
            <h2 class="tab" id="pestana1"><%=descriptor.getDescripcion("res_pestana1")%></h2>
            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
            <TABLE width="100%">
                <tr>
                    <td style="width:17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fecIni")%>:</td>
                    <td style="width:83%" 
                        <span class="columnP">
                            <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="10" maxlength="9" property="fechaInicio" readonly="true"
                                       onkeyup = "javascript:return SoloCaracteresFecha(this);" onfocus = "this.select();"/>
                        </span>
                        <span style="margin-left:8%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fecFin")%>:</span>
                        <span style="margin-left:8%" class="columnP">
                            <html:text styleClass="inputTxtFecha" size="10" maxlength="9" property="fechaFin" readonly="true"
                                       onkeyup = "javascript:return SoloCaracteresFecha(this);" onfocus = "this.select();"/>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td style="width:17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidTramit")%>:</td>
                    <td class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="unidadTramitadora" style="width:750px" maxlength="255" readonly="true"
                                   onkeyup="javascript:xAMayusculas(this);"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:17%" class="etiqueta"><%=descriptor.getDescripcion("etiq_clasTram")%>:</td>
                    <td class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="clasificacionTramite" style="width:750px" maxlength="255" readonly="true"
                                   onkeyup="javascript:xAMayusculas(this);"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <DIV id="capaPlazo" name="capaPlazo" STYLE="width:100%; height:40px; display:none; padding-left:-2px;">
                            <table width="100%" class="contenidoPestanha" cellpadding="0px" cellspacing="0">
                                <tr >
                                    <td style="width:153px;" class="etiqueta"><%=descriptor.getDescripcion("etiq_plzMax")%>:</td>
                                    <td colspan="5" class="columnP">
                                        <html:text styleClass="inputTextoObligatorio" property="plazo" size="10" maxlength="2" readonly="true" onkeypress="javascript:return SoloDigitos(event);"/>&nbsp;
                                        <html:text styleClass="inputTextoObligatorio" property="tipoPlazo" style="width:207px" maxlength="25" readonly="true" onkeyup="javascript:xAMayusculas(this);"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="etiqueta" style="padding-top: 3px;"><%=descriptor.getDescripcion("etiq_fIniPlz")%>:</td>
                                    <td style="width:119px;padding-top: 3px;" class="columnP" align="left">
                                        <html:text styleClass="inputTxtFecha" size="10" maxlength="10" property="fechaInicioPlazo"
                                                   onkeyup = "javascript:return SoloCaracteresFecha(this);"
                                                   onblur = "javascript:return comprobarFecha(this,true);" onfocus = "this.select();"/>
                                        <A href="javascript:calClick(event);return false;" onclick="mostrarCalFechaInicioPlazo(event);return false;" style="text-decoration:none;" >
                                            <span class="fa fa-calendar" aria-hidden="true" style="border: 0" id="calFechaInicioPlazo" name="calFechaInicioPlazo" alt="Data" ></span>
                                        </A>
                                    </td>
                                    <td style="width:92px;padding-top: 3px;" class="etiqueta"><%=descriptor.getDescripcion("etiq_fecLim")%>:</td>
                                    <td style="width:10%;padding-top: 3px;" class="columnP">
                                        <html:text styleClass="inputTxtFecha" size="10" maxlength="10" property="fechaLimite" readonly="true"
                                                   onkeyup = "javascript:return SoloCaracteresFecha(this);" onfocus = "this.select();"/>
                                    </td>
                                    <td style="width:13%;padding-top: 3px;" class="etiqueta"><%=descriptor.getDescripcion("etiq_fFinPlz")%>:</td>
                                    <td class="columnP" style="padding-top: 3px;">
                                        <html:text styleClass="inputTxtFecha" size="10" maxlength="10" property="fechaFinPlazo"
                                                   onkeyup = "javascript:return SoloCaracteresFecha(this);"
                                                   onblur = "javascript:return comprobarFecha(this,false);" onfocus = "this.select();"/>
                                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinPlazo(event);return false;" style="text-decoration:none;" >
                                            <span class="fa fa-calendar" aria-hidden="true" style="border: 0" id="calFechaFinPlazo" name="calFechaFinPlazo" alt="Data" ></span>
                                        </A>
                                    </td>
                                </tr>
                            </table>
                        </DIV>
                    </td>
                </tr>
                <tr>
                    <td style="width:17%" class="etiqueta"><%=descriptor.getDescripcion("etiqObs")%>:</td>
                    <td class="columnP">
                        <html:textarea styleClass="textareaTexto" style="width:750px;text-transform:none;" rows="3" property="observaciones"
                                       onkeypress="javascript:ControlarCaracteres(event);" value=""></html:textarea>
                    </td>
                </tr>
                <TR>
                    <TD class="sub3titulo" colspan="2">&nbsp;<%=descriptor.getDescripcion("etiqDocs")%></TD>
                </TR>
                <TR>
                    <TD id="tablaDoc" style="width: 100%" align="left" colspan="2"></TD>
                </TR>
                <TR>
                    <TD colspan="2" style="text-align:center">
                        <input type="button" name="cmdAltaDoc" onclick="pulsarAltaDoc();" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
                        <input type="button" class="botonGeneral" style="color:#ffffff" accesskey="V" value="Ver" name="cmdVerDoc" onclick="pulsarVerDoc();">
                        <input type="button" class="botonGeneral" accesskey="M" name="cmdModificarDoc" onclick="pulsarModificarDoc();" value="<%=descriptor.getDescripcion("gbModificar")%>">
                        <input type="button" class="botonGeneral" accesskey="E" name="cmdEliminarDoc" onclick="pulsarEliminarDoc();" value="<%=descriptor.getDescripcion("gbEliminar")%>">
                    </td>
                </tr>
            </table>
        </div>
        <%
    Vector estructuraDatosSuplementarios = (Vector) tramExpForm.getEstructuraDatosSuplementarios();
    Vector valoresDatosSuplementarios = (Vector) tramExpForm.getValoresDatosSuplementarios();
    boolean mostrarPestanhaDatos = false;

    if (estructuraDatosSuplementarios != null) {
    int lengthEstructuraDatosSuplementarios = estructuraDatosSuplementarios.size();
    int lengthValoresDatosSuplementarios = valoresDatosSuplementarios.size();
    for (int i = 0; i < lengthEstructuraDatosSuplementarios; i++) {
    EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
    if (!eC.getActivo().equalsIgnoreCase("NO")) {
    mostrarPestanhaDatos = mostrarPestanhaDatos || true;
    }
    if (eC.getCodTipoDato().equals("5")) {
    campos = campos + "," + eC.getCodCampo() + "_" + eC.getOcurrencia();
    }
    }

    if ((lengthEstructuraDatosSuplementarios > 0) && (mostrarPestanhaDatos)) {
        %>
        <!-- CAPA 2: PESTAÑA DE DATOS
        ------------------------------ -->
        <div class="tab-page" id="tabPage2">
            <h2 class="tab" id="pestana2">Datos suplementarios</h2>
            <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
            <TABLE id ="tablaDatosSuplementarios" class="contenidoPestanha">
                <TR>
                    <TD>
                        <DIV id="capaDatosSuplementarios" name="capaDatosSuplementarios">
                            <%
    for (int k = 0; k < lengthEstructuraDatosSuplementarios; k++) {
    request.setAttribute("CAMPO_BEAN", estructuraDatosSuplementarios.get(k));
    request.setAttribute("beanVO", valoresDatosSuplementarios.get(k));
    if ((fechaFinExpediente != null) && (!fechaFinExpediente.equalsIgnoreCase(""))) {
                            %>
                            <jsp:include page="/jsp/plantillas/CampoVista.jsp?desactivaFormulario=true" flush="true" />
                            <%
                                        } else {
                            %>
                            <jsp:include page="/jsp/plantillas/CampoVista.jsp?desactivaFormulario=false" flush="true" />
                            <%
    }
    } 
                            %>
                        </DIV>
                    </TD>
                </TR>
            </TABLE>
        </div>
        <%
    }
    }
    Vector lEnlaces = (Vector) tramExpForm.getListaEnlaces();
    String inst = (String) tramExpForm.getInstrucciones();
    if ((lEnlaces != null && lEnlaces.size() > 0) || ((inst != null) && !"".equals(inst))) {
        %>                  
        <!-- CAPA 4: PESTAÑA DE ENLACES -->
        <div class="tab-page" id="tabPage4" style="height:250px">
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
                        <DIV id="capaEnlaces" name="capaEnlaces" style="height:140px;overflow:auto;"></DIV>
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
                        <html:textarea styleClass="textoArea" cols="152" rows="11" property="instrucciones" readonly="true"></html:textarea>
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




    <%----- INCORPORACIÓN DE MÓDULOS EXTERNOS -------%>

    <%
    ArrayList<ModuloIntegracionExterno> modulos = tramExpForm.getModulosExternos();
    for(int i=0;modulos!=null && i<modulos.size();i++)
    {
    ModuloIntegracionExterno modulo = modulos.get(i);
    String nombreModulo = modulo.getNombreModulo();
    ArrayList<DatosPantallaModuloVO> pantallas = modulo.getListaPantallasTramite();

    for(int j=0;pantallas!=null && j<pantallas.size();j++)
    {
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
    <jsp:param name="codProcedimiento" value="<%=tramExpForm.getCodProcedimiento()%>"/>
    </jsp:include>
    <%
    }// for pantallas
    }// for modulos
    %>
    <%----- INCORPORACIÓN DE MÓDULOS EXTERNOS -------%>
    </div>
    <div id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="B" name="cmdBloquear" onclick="pulsarBloquear();" value=<%=descriptor.getDescripcion("gbBloquear")%>>
        <input type= "button" class="botonGeneral" accesskey="D" name="cmdDesbloquear" onclick="pulsarDesbloquear();" value=<%=descriptor.getDescripcion("gbDesbloquear")%>>
        <input type= "button" class="botonGeneral" accesskey="G" name="cmdGrabar" onclick="pulsarGrabar();" value=<%=descriptor.getDescripcion("gbGrabar")%>>
        <input type= "button" class="botonLargo" accesskey="F" name="cmdFinalizar" onclick="pulsarFinalizar();" value=<%=descriptor.getDescripcion("gbFinalizar")%>>
        <input type= "button" class="botonGeneral" style="color:#ffffff" accesskey="V" name="cmdVolver" onclick="pulsarVolver();" value=<%=descriptor.getDescripcion("gbVolver")%>>
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
                if (tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegable")) || tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegableExt"))) {%>
                    <script type="text/javascript">
                         eval("var combo<%=nombre%> = new Combo('<%=nombre%>',<%=idioma%>)");
                         eval("combo<%=nombre%>.addItems(<%=eC.getListaCodDesplegable()%>,<%=eC.getListaDescDesplegable()%>)");
                         eval("combo<%=nombre%>.buscaLinea('<%=valor%>');");
                         <%if (eC.getSoloLectura().equals("true")) {%>
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
if(document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tablaDoc);
else tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDoc'));
tab.addColumna('350','left',"<%= descriptor.getDescripcion("gEtiqDocumento")%>");
tab.addColumna('100','center',"<%= descriptor.getDescripcion("etiq_fCrea")%>");
tab.addColumna('110','center',"<%= descriptor.getDescripcion("etiq_fMod")%>");
tab.addColumna('150','center',"<%= descriptor.getDescripcion("gEtiq_usuar")%>");
tab.addColumna('100','center',"<%= descriptor.getDescripcion("etiq_estadoFirma")%>");
tab.addColumna('70','center',"<%= descriptor.getDescripcion("editorTexto")%>");

tab.height = 120;
tab.displayCabecera=true;
tab.displayTabla();
function refresca() {
  tab.displayTabla();
}
function mostrarCapas(nombreCapa) {
  document.getElementById(nombreCapa).style.display='block';
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
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

  if('Alt+F'==tecla) {
    pulsarFinalizar();
  } else if('Alt+G'==tecla) {
    pulsarGrabar();
  } else if('Alt+V'==tecla) {
    pulsarVolver();
  }
  
  if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
    upDownTable(tab,listaDocumentos,teclaAuxiliar);
  }
  
  if(teclaAuxiliar == 13){
	if((tab.selectedIndex>-1)&&(tab.selectedIndex < listaDocumentos.length)){
      callFromTableTo(tab.selectedIndex,tab.id,teclaAuxiliar);
    }
  }
  if(teclaAuxiliar == 1){
      if(IsCalendarVisible) replegarCalendario(coordx,coordy);
      for(i=0;i<nombreCombos.length;i++) {
        var nCombo=eval('combo'+nombreCombos[i]);
         eval('if (nCombo.base.style.visibility == "visible") nCombo.ocultar()');
      }
  }
  if(teclaAuxiliar == 9){
    if(IsCalendarVisible) hideCalendar();
       for(i=0;i<nombreCombos.length;i++) {
            var nCombo=eval('combo'+nombreCombos[i]);
            eval('nCombo.ocultar()');
       }
  }
  keyDel(evento);
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
            //j=j+1;
          }
        <%}%>
        deshabilitarGeneral(botones);
    }
}
</script>


<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>
</html:html>
