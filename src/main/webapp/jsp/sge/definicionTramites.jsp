<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.DefinicionTramitesForm"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.sge.DefinicionTramitesValueObject"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>

<html:html> 

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: EXPEDIENTES  Definición de Tramites:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos --> 
<%
    int idioma = 1;
    int apl = 4;
    int codOrg = 0;
    int codEnt = 1;
   
    Vector listaEnlaces = new Vector();
    DefinicionTramitesForm dtForm = null;
    if (session != null) {
        dtForm = (DefinicionTramitesForm) session.getAttribute("DefinicionTramitesForm");
        listaEnlaces = dtForm.getListaEnlaces();
    
    
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
            codOrg = usuario.getOrgCod();
            codEnt = usuario.getEntCod();
        }
    }

    String enviar = "";
    if (session != null && session.getAttribute("enviar") != null) {
        enviar = (String) session.getAttribute("enviar");
        session.setAttribute("enviar", null);

    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    Vector listaDocusErroneos = new Vector();
    if (dtForm != null) {
        listaDocusErroneos = dtForm.getListaDocusErroneos();
    }
    Config m_Conf = ConfigServiceHelper.getConfig("error");

 Config m_Documentos=ConfigServiceHelper.getConfig("documentos");	
    Boolean visibleAppExt=false;	
System.out.println(visibleAppExt);
    try{

    visibleAppExt=m_Documentos.getString("VISIBLE_EXT").toUpperCase().equals("SI");	
    }catch(Exception e){	
        visibleAppExt=false;	
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/operacionesModulo.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/definicionTramites.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/JavaScriptUtil.js'/>"></script>

<SCRIPT type="text/javascript">
 //indica si tiene que estar activo el combo notificacion
var comboActivo =true;
// === UORs
var uors = new Array();
var uorcods = new Array();
var cargos = new Array();
var cargoscods = new Array();
    <%Vector listaUORDTOs = dtForm.getListaUnidadesTramitadoras();
        for (int j=0; j<listaUORDTOs.size(); j++) {
            UORDTO dto = (UORDTO)listaUORDTOs.get(j);%>
            // array con los objetos tipo uor mapeados por el array de arriba
            uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
            // array con los códigos visibles
            uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
        <%}
    %>
    <%Vector listaCargosDTOs = dtForm.getListaCargos();
    for (int j=0; j<listaCargosDTOs.size(); j++) {
        UORDTO dto = (UORDTO)listaCargosDTOs.get(j);%>
        // array con los objetos tipo uor mapeados por el array de arriba
        cargos[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
        // array con los códigos visibles
        cargoscods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
    <%}%>

var listaTramites = new Array();
var cod_tipoTramite = new Array();
var desc_tipoTramite = new Array();
var cod_clasifTramite = new Array();
var desc_clasifTramite = new Array();
var cod_expRel = new Array();
var desc_expRel = new Array();
var estado_tramiteTabla = new Array('FINALIZADO','INICIADO','NO INICIADO','FAVORABLE','DESFAVORABLE','NO INICIADO O FINALIZADO');
var etiqFirmaDoc = '<%=descriptor.getDescripcion("firmaDoc")%>';
var condicionTabla = new Array('TRÁMITE','EXPRESION',etiqFirmaDoc);
var id_tramiteTabla = new Array();
var codigo_tramiteTabla = new Array();
var nombre_tramiteTabla = new Array();
var visible_internet = new Array('<%=descriptor.getDescripcion("etiq_si")%>','<%=descriptor.getDescripcion("etiq_no")%>');
var estado_enlaces = new Array('<%=descriptor.getDescripcion("etiq_si")%>','<%=descriptor.getDescripcion("etiq_no")%>');
var cod_webServices = new Array();
var desc_webServices = new Array();
var listaUnidadesTramitadoras = new Array();
var tipoOrigenOperaciones = new Array();
var tituloOperaciones     = new Array();
var nombreModulos         = new Array();
var alturaPantallas       = new Array();
var anchuraPantallas      = new Array();
var urlPantallas      	  = new Array();

var descNotificacionTramite=new Array('<%=descriptor.getDescripcion("unaVez")%>','<%=descriptor.getDescripcion("diariamente")%>');
var codNotificacionTramite=new Array('1','2');


var notificarCercaFinPlazo=false;
var notificarFueraDePlazo=false;
var tipoNotCercaFinPlazo=0;
var tipoNotFueraDePlazo=0;
var admiteNotifElect=0;
var tipoNotifElect=-1;
var notifElectObligatoria="off";
var certificadoNotif="off";
var tipoUsuarioFirma='';
var codigoOtroUsuarioFirma;
var nombreOtroUsuarioFirma='';


var codigo_tipoNotificacionElectronica=new Array();
var nombre_tipoNotificacionElectronica=new Array();

var codigo_documentoPresentado=new Array();
var nombre_documentoPresentado=new Array();
var estado_documentoPresentado=new Array('PENDIENTE', 'FIRMADO', 'RECHAZADO', 'CIRCUITO FINALIZADO','SUBSANADO');


var cont =0;
var cont1 =0;
var cont2 = 0;
var cont3 = 0;
var cont4 = 0;
var cont5 = 0;
var cont6 = 0;
var deInsercion="";
var tab;
var tabDoc;
var tabEnlaces;
var tabAux;
var listaTablaEntrada = new Array();
var listaTablaEntradaOriginal = new Array();
var listaDoc = new Array();
var listaDocOriginal = new Array();
var listaEnlaces = new Array();
var listaEnlacesOriginal = new Array();
var listaAux = new Array();
var listaCampos = new Array();
var listaCamposOriginal = new Array();
var listaConfSW = new Array();
var listNom = new Array();
var listaConfSWCfos = new Array();

var tramiteActual = 1;
var numeroTramites = 10;

var tipoCond = "";
var tipoCondSF = "";
var tipoCondNF = "";

var codAntiguoSWAvanzar = '-1';
var codAntiguoSWRetroceder = '-1';

var tiposOperaciones = new Array();
var nombresOperaciones = new Array();
var nombresModulo = new Array();
var tipoOperacionRetroceso = new Array();

var codigosDepartamentos = new Array();
var descripcionesDepartamentos = new Array();

var listaAgrupaciones = new Array();
var listaAgrupacionesOriginal = new Array();

var howManySW=0;

var litMosPagDePags = '<%=descriptor.getDescripcion("mosPagDePags")%>';
var litAnterior = '<%=descriptor.getDescripcion("anterior")%>';
var litSiguiente = '<%=descriptor.getDescripcion("siguiente")%>';

function enlaceTramite(pos) {
  borrarTablaEntrada();
  document.forms[0].codigoProcedimiento.value = document.forms[0].txtCodigo.value;
  document.forms[0].opcion.value="enlaceTramite";
  document.forms[0].enlace.value=pos;
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
  document.forms[0].submit();
}

function pintaTramiteActual() { 
  document.forms[0].codigoProcedimiento.value = document.forms[0].txtCodigo.value;
  document.forms[0].opcion.value="pintaTramiteActual";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
  document.forms[0].submit();
}

function inicializar() {    
  comprobarConfiguracionSW();
  window.focus();
  comprobarConfiguracionSW();
  mostrarCapasBotones('capaBotones4');
  desactivarBotonesListasTramites();
  tp1.setSelectedIndex(0);
  document.forms[0].codMunicipio.value = '<bean:write name="DefinicionTramitesForm" property="codMunicipio"/>';
  borrarDatos();
  
  document.forms[0].radioUnidadInicio[3].checked=true;
  document.forms[0].codUnidadTramite[0].checked= true;
  document.forms[0].enviar.value = '<%= enviar %>';

  cargaArraysComunes(false);
  
  var j = 0;
  
  tab.lineas=listaTablaEntrada;
  refresca();
  tabDoc.lineas=listaDoc;
  refrescaDoc();
  if(document.getElementById('capaBotones4').style.display=='') {
    var vectorBotonesDoc = [document.forms[0].cmdAltaDoc,document.forms[0].cmdAdjuntarDoc, document.forms[0].cmdModificarDoc,
	                        document.forms[0].cmdEliminarDoc,document.forms[0].cmdActivarDoc];
    deshabilitarGeneral(vectorBotonesDoc);
  }

 notificarCercaFinPlazo=false;
 notificarFueraDePlazo=false;
 tipoNotCercaFinPlazo=0;
 tipoNotFueraDePlazo=0;
 admiteNotifElect=0;
}

<c:set var="JS_DEBUG_LEVEL" value="0" />
function getEstadoFirmaVisual( codigoDocumento, codigoEstadoFirma ) {
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("DefinicionTramites.getEstadoFirmaVisual("+codigoDocumento+","+codigoEstadoFirma+") BEGIN");
    </c:if>
    var result = "";
    if (codigoDocumento) {
        var paramCodigoDocumento=''+codigoDocumento+'';
        if ( (!codigoEstadoFirma) || (codigoEstadoFirma=='' || codigoEstadoFirma == 'N') ) {
            result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.Null"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+');"></span>';
        } else {
            if (codigoEstadoFirma == 'O') {
                result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.O"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+');"></span>';
            } else if (codigoEstadoFirma == 'T') {
                result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.T"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+');"></span>';
            } else if (codigoEstadoFirma == 'L') {
                result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.L"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+');"></span>';
            } else if (codigoEstadoFirma == 'U') {
                result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.U"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+');"></span>';
            } else if (codigoEstadoFirma == 'S') {
                result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.S"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+');"></span>';
            } else {
                result = codigoEstadoFirma;
            }
        }
    }
    <c:if test="${JS_DEBUG_LEVEL >= 70}">
    alert("DefinicionTramites.getEstadoFirmaVisual("+codigoDocumento+","+codigoEstadoFirma+")="+result+" END");
    </c:if>
    return result;
}

var popup = null;

function pulsarModificarFirmaPlantilla( dscCodigoPlantilla ) {
    if(tabDoc.selectedIndex != -1){
        var dscCodigoMunicipio = document.forms[0].codMunicipio.value;
        var dscCodigoProcedimiento = document.forms[0].txtCodigo.value;
        var dscCodigoTramite = document.forms[0].codigoTramite.value;
        var urlParams = "idMunicipio="+dscCodigoMunicipio;
        urlParams = urlParams + "&idProcedimiento="+dscCodigoProcedimiento;
        urlParams = urlParams + "&idTramite="+dscCodigoTramite;
        urlParams = urlParams + "&idPlantilla="+dscCodigoPlantilla;
    


        var source = "<html:rewrite page='/sge/plantillafirma/PreparePlantillaFirma.do'/>?"+ urlParams;
        abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,window,
	'width=650,height=350,scrollbars=no,status='+ '<%=statusBar%>',function(result){
                        if (result != undefined) setPopUpFirmaPlantillaResult(result[0], result[1]);
                    });
    }
    else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function cargaArraysComunes(comprobarInsercion){
  var orden=0;
  var identificador;
  var codigo;
  var descripcion;
  var insertado=false;
  var numeroTramite=new Number(document.forms[0].numeroTramite.value);  

    var j=0;
    <%
    Vector listaWS = dtForm.getListaWebServices();
    if (listaWS == null) listaWS = new Vector();
    for(int i=0; i<listaWS.size(); i++) {
        GeneralValueObject infoSW = (GeneralValueObject)listaWS.get(i);
        String tipoOrigen = "WS";
        String nombreModulo = "";
		String altoPantalla = "";
		String anchoPantalla = "";
		String urlPantalla   = "";
        // Si la operación es de tipo modulo de integración
        if(infoSW.getAtributo("tipoOrigen")!=null && "MI".equals(infoSW.getAtributo("tipoOrigen"))){
            tipoOrigen    = "MODULO";
            nombreModulo  = (String)infoSW.getAtributo("nombreModulo");
            altoPantalla  = (String)infoSW.getAtributo("altoPantalla");
            anchoPantalla = (String)infoSW.getAtributo("anchoPantalla");
            urlPantalla   = (String)infoSW.getAtributo("urlPantallaConfiguracion");
        }
    %>
    desc_webServices[j]      = '<%=infoSW.getAtributo("tituloOp")%> (<%=infoSW.getAtributo("tituloSW")%>)';
    cod_webServices[j]       = '<%=infoSW.getAtributo("codigoOp")%>';
    tipoOrigenOperaciones[j] = '<%=tipoOrigen%>';
    tituloOperaciones[j]     = '<%=infoSW.getAtributo("tituloOp")%>';
    nombreModulos[j]         = '<%=nombreModulo%>';
	alturaPantallas[j]       = '<%=altoPantalla%>';
	anchuraPantallas[j]      = '<%=anchoPantalla%>';
	urlPantallas[j]     	 = '<%=urlPantalla%>';	 
    j++;
	<%}%>    

  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaClasifTramite">
    cod_clasifTramite['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_clasifTramite['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
  </logic:iterate>
  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaExpRel">
	cod_expRel['<bean:write name="elemento" property="orden"/>']="<bean:write name="elemento" property="codigo"/>";
	desc_expRel['<bean:write name="elemento" property="orden"/>']="<bean:write name="elemento" property="descripcion"/>";
   </logic:iterate>

    var cont=0;
  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaTiposNotificacionElectronica">

    codigo_tipoNotificacionElectronica[cont]='<bean:write name="elemento" property="codigo"/>';
    nombre_tipoNotificacionElectronica[cont]='<bean:write name="elemento" property="descripcion"/>';
    cont++;
  </logic:iterate>

  cont=0;
  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaDocsPresentados">
    codigo_documentoPresentado[cont]='<bean:write name="elemento" property="codigo"/>';
    nombre_documentoPresentado[cont]='<bean:write name="elemento" property="nombre"/>';
    cont++;
  </logic:iterate>
      
  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaTramites">
    if (comprobarInsercion){
        identificador='<bean:write name="elemento" property="identificador"/>';
        codigo = new Number('<bean:write name="elemento" property="codigo"/>');
        descripcion='<bean:write name="elemento" property="descripcion"/>';
        if ((codigo>numeroTramite) && (!insertado)) {
            id_tramiteTabla[orden]=document.forms[0].codigoTramite.value;
            codigo_tramiteTabla[orden]=document.forms[0].numeroTramite.value;
            nombre_tramiteTabla[orden]=document.forms[0].nombreTramite.value;
            orden++;
            id_tramiteTabla[orden]=identificador;
            codigo_tramiteTabla[orden]=codigo;
            nombre_tramiteTabla[orden]=descripcion;
            insertado=true;
        }else {
            id_tramiteTabla[orden]=identificador;
            codigo_tramiteTabla[orden]=codigo;
            nombre_tramiteTabla[orden]=descripcion;
        }
        orden++;
    } else {
        id_tramiteTabla['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="identificador"/>';
        codigo_tramiteTabla['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        nombre_tramiteTabla['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
    }    
  </logic:iterate>

  if (comprobarInsercion && !insertado) {
    id_tramiteTabla[orden]=document.forms[0].codigoTramite.value;
    codigo_tramiteTabla[orden]=document.forms[0].numeroTramite.value;
    nombre_tramiteTabla[orden]=document.forms[0].nombreTramite.value;
  }

  cargarCombos();
  limpiarCombosWS();
  document.forms[0].instrucciones.value= unescape('<bean:write name="DefinicionTramitesForm" property="instrucciones"/>');

} //fin cargaArraysComunes

function recibir() { 

  window.focus();
  mostrarCapasBotones('capaBotones1');
  desactivarBotonesListasTramites();
  tp1.setSelectedIndex(0);
  document.forms[0].enviar.value = '<%= enviar %>';
  document.forms[0].codMunicipio.value = '<bean:write name="DefinicionTramitesForm" property="codMunicipio"/>';
  document.forms[0].codVisibleUnidadInicio.value = '<bean:write name="DefinicionTramitesForm" property="codVisibleUnidadInicio"/>';
 
  if (document.forms[0].plazo.value=='') {
     document.forms[0].plazoFin.disabled = true;
     document.getElementById("et1").className  = 'etiquetaDeshabilitada';
     document.getElementById("et2").className ='etiquetaDeshabilitada';
     document.forms[0].plazoFin.value='';
     document.forms[0].generarPlazos.disabled = true;
     document.getElementById("et3").className ='etiquetaDeshabilitada';
     document.forms[0].generarPlazos.checked = false;
  } else {
     document.forms[0].plazoFin.disabled = false;
     document.getElementById("et1").className  = 'etiqueta';
     document.getElementById("et2").className ='etiqueta';
     document.forms[0].plazoFin.value='<bean:write name="DefinicionTramitesForm" property="plazoFin"/>';
     document.getElementById("et3").className ='etiqueta';
     document.forms[0].generarPlazos.checked = <bean:write name="DefinicionTramitesForm" property="generarPlazos"/>;
  }
 
  onchangeCodUnidadInicio();
  document.forms[0].cod_visible_cargo.value = '<bean:write name="DefinicionTramitesForm" property="codVisibleCargo"/>';
  onchangeCodCargo();

  cargaArraysComunes(true);

  cont=0;
  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listasCondEntrada">
      tipo = "<bean:write name="elemento" property="tipoCondEntrada"/>";
      if (tipo=="TRÁMITE"||tipo==etiqFirmaDoc) {          
          listaTablaEntrada[cont] = ['<bean:write name="elemento" property="tipoCondEntrada"/>',
                                     '<bean:write name="elemento" property="descTramiteCondEntrada"/>/<bean:write name="elemento" property="estadoTramiteCondEntrada" />'];
      } else {
          listaTablaEntrada[cont] = ['<bean:write name="elemento" property="tipoCondEntrada"/>',
                                     '<bean:write name="elemento" property="expresionCondEntrada"/>'];
      }
      cont++;
  </logic:iterate>

  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listasCondEntrada">
      listaTablaEntradaOriginal[cont1] = ['<bean:write name="elemento" property="idTramiteCondEntrada" />',
                        '<bean:write name="elemento" property="tipoCondEntrada" />',
                        '<bean:write name="elemento" property="codTramiteCondEntrada" />',
                        '<bean:write name="elemento" property="descTramiteCondEntrada"/>',
                        '<bean:write name="elemento" property="estadoTramiteCondEntrada" />',
                        '<bean:write name="elemento" property="codCondEntrada" />',
                        '<bean:write name="elemento" property="codigoDoc" />'];                    
                    
      cont1++;
  </logic:iterate>
  
  
 
  tab.lineas=listaTablaEntrada;
  refresca();
  borrarDatosTablaEntrada();

  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaDocumentos">
    var int = "";
    var rel = "";
    var docImg = "";
    var inter = '<bean:write name="elemento" property="interesado" />';
    var docAct = '<bean:write name="elemento" property="docActivo"/>';
    var relac = '<bean:write name="elemento" property="relacion" />';    
    if(relac.trim() == "S") {
	  rel='RELACION';
	} else {
	  rel = "EXPEDIENTE";
	}
    if(inter == "S") {
	  int = "POR INTERESADO";
	} else {
	  int = "NO POR INTERESADO";
	}
    if (docAct == "SI") {
        docImg = '<span class="fa fa-check 2x"></span>';
    } else {
        docImg = '<span class="fa fa-close 2x"></span>';
    }
  <%if(visibleAppExt){%>	
         var visibleExt='<bean:write name="elemento" property="visibleExt"/>';	
         var docImg2="";	
         if(visibleExt=="SI"){	
            docImg2 = '<span class="fa fa-check 2x"></span>';	
         }else{	
            docImg2 = '<span class="fa fa-close 2x"></span>';	
         }	
         listaDoc[cont2] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>', int	
                                , getEstadoFirmaVisual('<bean:write name="elemento" property="codigoDoc" />','<bean:write name="elemento" property="firma"/>')	
                                ,docImg,rel,'','<bean:write name="elemento" property="editorTexto"/>',docImg2];	
    	
    <%}else{%>	
        listaDoc[cont2] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>', int	
                                , getEstadoFirmaVisual('<bean:write name="elemento" property="codigoDoc" />','<bean:write name="elemento" property="firma"/>')	
                                ,docImg,rel,'','<bean:write name="elemento" property="editorTexto"/>'];	
   	
    <%}%>
    cont2++;
  </logic:iterate>

  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaDocumentos">
    relac = '<bean:write name="elemento" property="relacion" />';
        <%if(visibleAppExt){%>	
        listaDocOriginal[cont3] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>',	
                               '<bean:write name="elemento" property="visibleInternet" />','<bean:write name="elemento" property="plantilla" />',	
                               '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="interesado" />'	
                               ,'<bean:write name="elemento" property="firma" />'	
                               ,'<bean:write name="elemento" property="docActivo" />'	
                               ,'<bean:write name="elemento" property="relacion"/>',null,null,'<bean:write name="elemento" property="visibleExt"/>'];	
     <%}else{%>	
        listaDocOriginal[cont3] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>',	
                               '<bean:write name="elemento" property="visibleInternet" />','<bean:write name="elemento" property="plantilla" />',	
                               '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="interesado" />'	
                               ,'<bean:write name="elemento" property="firma" />'	
                               ,'<bean:write name="elemento" property="docActivo" />'	
                               ,'<bean:write name="elemento" property="relacion"/>'];	
     <%}%>
    cont3++;
  </logic:iterate>
  tabDoc.lineas=listaDoc;
  refrescaDoc();

  <% if (listaDocusErroneos!=null) {
    for (int j=0;j<listaDocusErroneos.size();j++) { %>
        <%String mensaje = MessageFormat.format(m_Conf.getString("error.plantillas.tramites"),
        new Object[] { ((DefinicionTramitesValueObject)listaDocusErroneos.elementAt(j)).getCodPlantilla(),
                    ((DefinicionTramitesValueObject)listaDocusErroneos.elementAt(j)).getCodigoDoc(),
                    ((DefinicionTramitesValueObject)listaDocusErroneos.elementAt(j)).getCodigoTramite()});%>
        jsp_alerta("A",'<%=mensaje%>');
    <%}
  }%>

  var i=0;

  <% if (listaEnlaces != null) {
  	for(int j=0;j<listaEnlaces.size();j++){%>
      listaEnlaces[i] = ['<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("descripcion")%>',
              '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("url")%>',
              '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("estado")%>'];
    listaEnlacesOriginal[i] = ['<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("codigo")%>',
              '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("descripcion")%>',
              '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("url")%>',
              '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("estado")%>'];
    i++;
  <%} }%>
  tabEnlaces.lineas=listaEnlaces;
  tabEnlaces.displayTabla();

  var campoAct = "";
  var campoImg = "";
  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaCampos">
  campoAct = '<bean:write name="elemento" property="activo" />';
    if (campoAct == "SI") {
        campoImg = '<span class="fa fa-check 2x"></span>';
    } else {
        campoImg = '<span class="fa fa-close 2x"></span>';
    }
    listaCampos[cont4] = ['<bean:write name="elemento" property="codCampo" />',
                          '<bean:write name="elemento" property="descCampo" />',campoImg];
    listaCamposOriginal[cont4] =['<bean:write name="elemento" property="codCampo" />','<bean:write name="elemento" property="descCampo" />',
                                 '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="codTipoDato" />',
                                 '<bean:write name="elemento" property="tamano" />','<bean:write name="elemento" property="descMascara" />',
                                 '<bean:write name="elemento" property="obligat" />','<bean:write name="elemento" property="orden" />',
                                 '<bean:write name="elemento" property="descPlantilla" />','<bean:write name="elemento" property="descTipoDato" />',
                                 '<bean:write name="elemento" property="rotulo" />','<bean:write name="elemento" property="visible" />',
				 '<bean:write name="elemento" property="activo" />','<bean:write name="elemento" property="oculto" />','<bean:write name="elemento" property="bloqueado" />',
                                 '<bean:write name="elemento" property="plazoFecha" />','<bean:write name="elemento" property="checkPlazoFecha" />',
                                 '<bean:write name="elemento" property="validacion" />','<bean:write name="elemento" property="operacion" />',
                                 '<bean:write name="elemento" property="codAgrupacion" />','<bean:write name="elemento" property="posX" />',
                                 '<bean:write name="elemento" property="posY" />'];
    cont4++;
  </logic:iterate>
  tabCampos.lineas = listaCampos;
  refrescaCampos();
  
  campoAct = "";
    campoImg = "";
    <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaAgrupaciones">
        campoAct = '<bean:write name="elemento" property="agrupacionActiva" />';
        if (campoAct == "SI") {
            campoImg = '<span class="fa fa-check 2x"></span>';
        } else {
            campoImg = '<span class="fa fa-close 2x"></span>';
        }
        listaAgrupaciones[cont6] = ['<bean:write name="elemento" property="codAgrupacion"/>',
                                        '<bean:write name="elemento" property="descAgrupacion"/>',
                                        '<bean:write name="elemento" property="ordenAgrupacion"/>',
                                        campoImg];
        listaAgrupacionesOriginal[cont6] = ['<bean:write name="elemento" property="codAgrupacion"/>',
                                        '<bean:write name="elemento" property="descAgrupacion"/>',
                                        '<bean:write name="elemento" property="ordenAgrupacion"/>',
                                        '<bean:write name="elemento" property="agrupacionActiva"/>'];
                                            
        cont6++;
    </logic:iterate>
    tabAgrupaciones.lineas = listaAgrupaciones;
    refrescaAgrupaciones();


  <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaConfSW">
  	listaConfSW[cont5] = ['<bean:write name="elemento" property="codIniciar"/>',
                          '<bean:write name="elemento" property="codAvanzar"/>' , 
        				  '<bean:write name="elemento" property="codRetroceder"/>'];

  	listaConfSWCfos[cont5] = ['<bean:write name="elemento" property="cfoIniciar"/>' , 
                              '<bean:write name="elemento" property="cfoAvanzar"/>' ,
                              '<bean:write name="elemento" property="cfoRetroceder"/>'];
    
	tiposOperaciones[cont5]   = ['<bean:write name="elemento" property="tipoOperacionIniciar"/>','<bean:write name="elemento" property="tipoOperacionAvanzar"/>','<bean:write name="elemento" property="tipoOperacionRetroceder"/>'];
	nombresOperaciones[cont5] = ['<bean:write name="elemento" property="nombreOperacionIniciar"/>','<bean:write name="elemento" property="nombreOperacionAvanzar"/>','<bean:write name="elemento" property="nombreOperacionRetroceder"/>'];
        nombresModulo[cont5] = ['<bean:write name="elemento" property="nombreModuloIniciar"/>','<bean:write name="elemento" property="nombreModuloAvanzar"/>','<bean:write name="elemento" property="nombreModuloRetroceder"/>']
        tipoOperacionRetroceso[cont5] = ['<bean:write name="elemento" property="codRetroceder"/>','<bean:write name="elemento" property="cfoRetroceder"/>','<bean:write name="elemento" property="tipoRetroceso"/>'];
        
	cont5++;  						  
  </logic:iterate>
      
        howManySW = listaConfSW.length;    
	listNom = getNombresConfSW(listaConfSW);

	tabConfSW.lineas = listNom;
	tabConfSW.displayTabla();
	
  var tipoUP = '<bean:write name="DefinicionTramitesForm" property="unidadesPlazo"/>';
  document.forms[0].unidadesPlazo.value=tipoUP;
  var disp = '<bean:write name="DefinicionTramitesForm" property="disponible"/>';
  if(disp == 1) {
    document.forms[0].disponible.checked = true;
  } else {
    document.forms[0].disponible.checked = false;
  }
  var tramInic = '<bean:write name="DefinicionTramitesForm" property="tramiteInicio"/>';
  if(tramInic == 1) {
    document.forms[0].tramiteInicio.checked = true;
  } else {
    document.forms[0].tramiteInicio.checked = false;
  }

  //NOTIFICACIONES
    var notUnidadTramitIni = '<bean:write name="DefinicionTramitesForm" property="notUnidadTramitIni"/>';
    if(notUnidadTramitIni == 1) {
      document.forms[0].checkUdadTramitIni.checked = true;
    } else {
      document.forms[0].checkUdadTramitIni.checked = false;
    }
    var notUnidadTramitFin = '<bean:write name="DefinicionTramitesForm" property="notUnidadTramitFin"/>';
    if(notUnidadTramitFin == 1) {
      document.forms[0].checkUdadTramitFin.checked = true;
    } else {
      document.forms[0].checkUdadTramitFin.checked = false;
    }
    var notUsuUnidadTramitIni = '<bean:write name="DefinicionTramitesForm" property="notUsuUnidadTramitIni"/>';
    if(notUsuUnidadTramitIni == 1) {
      document.forms[0].checkUsuUdadTramitIni.checked = true;
    } else {
      document.forms[0].checkUsuUdadTramitIni.checked = false;
    }
    var notUsuUnidadTramitFin = '<bean:write name="DefinicionTramitesForm" property="notUsuUnidadTramitFin"/>';
    if(notUsuUnidadTramitFin == 1) {
      document.forms[0].checkUsuUdadTramitFin.checked = true;
    } else {
      document.forms[0].checkUsuUdadTramitFin.checked = false;
    }
    var notInteresadosIni = '<bean:write name="DefinicionTramitesForm" property="notInteresadosIni"/>';
    if(notInteresadosIni == 1) {
      document.forms[0].checkInteresadosIni.checked = true;
    } else {
      document.forms[0].checkInteresadosIni.checked = false;
    }
    var notInteresadosFin = '<bean:write name="DefinicionTramitesForm" property="notInteresadosFin"/>';
    if(notInteresadosFin == 1) {
      document.forms[0].checkInteresadosFin.checked = true;
    } else {
      document.forms[0].checkInteresadosFin.checked = false;
    }

    var notUsuInicioTramiteIni = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioTramiteIni"/>';

    if(notUsuInicioTramiteIni == 1){
        document.forms[0].checkUsuarioTramiteIni.checked = true;
    } else {
        document.forms[0].checkUsuarioTramiteIni.checked = false;
    }

    var notUsuInicioTramiteFin = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioTramiteFin"/>';

    if(notUsuInicioTramiteFin == 1){
        document.forms[0].checkUsuarioTramiteFin.checked = true;
    } else {
        document.forms[0].checkUsuarioTramiteFin.checked = false;
    }

    var notUsuInicioExpedIni = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioExpedIni"/>';
	
    if(notUsuInicioExpedIni == 1){
        document.forms[0].checkUsuarioExpedIni.checked = true;
    } else {
        document.forms[0].checkUsuarioExpedFin.checked = false;
    }

    var notUsuInicioExpedFin = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioExpedFin"/>';

    if(notUsuInicioExpedFin == 1){
        document.forms[0].checkUsuarioExpedFin.checked = true;
    }else {
        document.forms[0].checkUsuarioExpedFin.checked = false;
    }
    var notUsuTraFinPlazo = '<bean:write name="DefinicionTramitesForm" property="notUsuTraFinPlazo"/>';	
    if(notUsuTraFinPlazo == 1){	
        document.forms[0].checkNotUsuTraFinPlazo.checked = true;	
    }else{	
        document.forms[0].checkNotUsuTraFinPlazo.checked = false;	
    }	
    	
    var notUsuExpFinPlazo = '<bean:write name="DefinicionTramitesForm" property="notUsuExpFinPlazo"/>';	
    if(notUsuExpFinPlazo ==1){
        document.forms[0].checkNotUsuExpFinPlazo.checked = true;	
    }else{	
        document.forms[0].checkNotUsuExpFinPlazo.checked = false;	
    }	
    var notUORFinPlazo = '<bean:write name="DefinicionTramitesForm" property="notUORFinPlazo"/>';	
    if(notUORFinPlazo == 1){	
        document.forms[0].checkNotUORFinPlazo.checked = true;	
    }else {	
        document.forms[0].checkNotUORFinPlazo.checked = false;	
    }

    var admiteNotifElect = '<bean:write name="DefinicionTramitesForm" property="admiteNotificacionElectronica"/>';
	var tipoNotifElect = '<bean:write name="DefinicionTramitesForm" property="codigoTipoNotificacionElectronica"/>';

    if(admiteNotifElect == '1') {
 
      document.forms[0].admiteNotificacionElectronica.checked = true;
    } else {
      document.forms[0].admiteNotificacionElectronica.checked = false;
    }
    
    var notifElectObligatoria = '<bean:write name="DefinicionTramitesForm" property="notificacionElectronicaObligatoria"/>';        
    if(notifElectObligatoria.toUpperCase()== 'ON') {
      document.forms[0].notificacionElectronicaObligatoria.checked = true;
    } else {
      document.forms[0].notificacionElectronicaObligatoria.checked = false;
    }
    
    
    var certificadoOrganismo = '<bean:write name="DefinicionTramitesForm" property="certificadoOrganismoFirmaNotificacion"/>';
    document.forms[0].certificadoOrganismoFirmaNotificacion.value=certificadoOrganismo;
    
    //So esta
  var soEsta = '<bean:write name="DefinicionTramitesForm" property="tramitePregunta"/>';
  if(soEsta == 1) {
    document.forms[0].soloEsta.checked = true;
  } else {
    document.forms[0].soloEsta.checked = false;
  }
  tipoCond = '<bean:write name="DefinicionTramitesForm" property="tipoCondicion"/>';
  tipoCondSF = '<bean:write name="DefinicionTramitesForm" property="tipoFavorableSI"/>';
  tipoCondNF = '<bean:write name="DefinicionTramitesForm" property="tipoFavorableNO"/>';
  tipoCondicion();
  if(document.forms[0].importar.value == "si") {
    desactivarBotonesListasTramites();
    desactivarBotonesCapa1();
  }
  if(document.forms[0].noModificar.value == "si") {
    desactivarBotonesListasTramites();
  }
  if(document.forms[0].texto.value != "" || document.forms[0].tipoCondicion[3].checked == true) {
    activarButtonsAcciones();
  }
  var vector = new Array(document.forms[0].txtCodigo,document.forms[0].txtDescripcion);
  deshabilitarGeneral(vector);
  if(document.forms[0].importar.value == "si") {
    desactivarFormulario();
  }
  // NAVEGACION TRAMITES.  23/09/2003
  tramiteEnNavegacion('<bean:write name="DefinicionTramitesForm" property="tramiteActual"/>','<bean:write name="DefinicionTramitesForm" property="numeroTramites"/>');

  activarBotonesListas();
  desactivarFormulario();

  mostrarCapasBotones('capaBotones1');
  document.forms[0].nombreTramiteI.value = document.forms[0].nombreTramite.value;

  if(document.forms[0].deCatalogo.value == "si") {
    mostrarCapasBotones('capaBotones2');
    document.forms[0].cmdCancelarCatalogo.disabled = false;
    document.forms[0].cmdCancelarCatalogo.style.color = '#ffffff';
  }

  document.forms[0].codUnidadInicio.value='<bean:write name="DefinicionTramitesForm" property="codUnidadInicio"/>';
  if(document.forms[0].codUnidadInicio.value == -99999) {
      document.forms[0].descUnidadInicio.value = "";
      document.forms[0].radioUnidadInicio[0].checked=true;
      document.forms[0].codVisibleUnidadInicio.value = "";
  } else if(document.forms[0].codUnidadInicio.value == -99998) {
      document.forms[0].descUnidadInicio.value = "";
      document.forms[0].radioUnidadInicio[1].checked=true;
      document.forms[0].codVisibleUnidadInicio.value = "";
  } else if(document.forms[0].codUnidadInicio.value == "") {
      document.forms[0].descUnidadInicio.value = "";
      document.forms[0].radioUnidadInicio[2].checked=true;
      document.forms[0].codVisibleUnidadInicio.value = "";
  } else {
      document.forms[0].radioUnidadInicio[3].checked=true;
  }
  
  var codUnidadTramite='<bean:write name="DefinicionTramitesForm" property="codUnidadTramite"/>';
  seleccionUnidadTramite(codUnidadTramite);
  
  j = 0;
  listaUnidadesTramitadoras = new Array();
  <logic:present name="DefinicionTramitesForm" property="unidadesTramitadoras">
      <logic:iterate id="uor" name="DefinicionTramitesForm" property="unidadesTramitadoras">
          listaUnidadesTramitadoras[j] = '<bean:write name="uor" property="uor_cod"/>';
          j++;
      </logic:iterate>
  </logic:present>
  mostrarListaUnidadTramite();

  
  var botonesCapaUno = [document.forms[0].cmdAlta, document.forms[0].cmdModificar, document.forms[0].cmdEliminarTram, document.forms[0].cmdDefProc];
  habilitarGeneral(botonesCapaUno);
 
  var botonesListaTramites = [document.forms[0].cmdListaTramite, document.forms[0].cmdListaTramiteSiFavorable, document.forms[0].cmdListaTramiteNoFavorable];
  habilitarGeneral(botonesListaTramites);

  //notificaciones automaticas

 notificarCercaFinPlazo=<bean:write name="DefinicionTramitesForm" property="notificarCercaFinPlazo"/>;
 notificarFueraDePlazo=<bean:write name="DefinicionTramitesForm" property="notificarFueraDePlazo"/>;
 tipoNotCercaFinPlazo='<bean:write name="DefinicionTramitesForm" property="tipoNotCercaFinPlazo"/>';
 tipoNotFueraDePlazo='<bean:write name="DefinicionTramitesForm" property="tipoNotFueraDePlazo"/>';
 admiteNotifElect = '<bean:write name="DefinicionTramitesForm" property="admiteNotificacionElectronica"/>';
 tipoNotifElect = '<bean:write name="DefinicionTramitesForm" property="codigoTipoNotificacionElectronica"/>';
 tipoUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="tipoUsuarioFirma"/>';
 codigoOtroUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="codigoOtroUsuarioFirma"/>';
 nombreOtroUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="nombreOtroUsuarioFirma"/>';
 var valorNotifObligatorias='<bean:write name="DefinicionTramitesForm" property="notificacionElectronicaObligatoriaValue"/>';
 var valorCertOrganismo='<bean:write name="DefinicionTramitesForm" property="certificadoOrganismoFirmaNotificacionValue"/>';
 var codDepartamentoNotificacion = '<bean:write name="DefinicionTramitesForm" property="codDepartamentoNotificacion"/>';
 
actualizarNotAutomaticas(notificarCercaFinPlazo,notificarFueraDePlazo,tipoNotCercaFinPlazo,tipoNotFueraDePlazo,admiteNotifElect,tipoNotifElect,tipoUsuarioFirma,codigoOtroUsuarioFirma,nombreOtroUsuarioFirma,valorNotifObligatorias,valorCertOrganismo,codDepartamentoNotificacion);


}

function recuperaTramite(datos,lista1,lista2,lista3,lista4,lista5,lista6,lista7,lista8,enlace1,
    numTramites1,lista9,lista10,lista11,lista12,lista13,lista14,lista15, listaUtrs, listaNotAutomaticas,ltiposOperaciones,
    lnombresOperaciones,lnombresModulos,listaTiposRetroceso, listaAgrup, listaAgrupOriginal) {        
  window.focus();
  desactivarBotonesListasTramites();
  document.forms[0].txtCodigo.value = datos[1];
  document.forms[0].txtDescripcion.value = datos[2];
  document.forms[0].numeroTramite.value = datos[3];
  document.forms[0].nombreTramite.value = datos[4];
  document.forms[0].codigoTramite.value = datos[5];
  document.forms[0].codigoInternoTramite.value = datos[45];
  document.forms[0].ocurrencias.value = datos[7];
  document.forms[0].codClasifTramite.value = datos[8];
  document.forms[0].descClasifTramite.value = datos[9];
  document.forms[0].plazo.value = datos[10];
  if(document.forms[0].tramiteNotificado)
    document.forms[0].tramiteNotificado.checked = datos[50];
  
  if( document.forms[0].plazo.value !=""){

     document.getElementById("et1").className  = 'etiqueta';
     document.getElementById("et2").className ='etiqueta';
     document.getElementById("et3").className ='etiqueta';
     document.forms[0].plazoFin.value=datos[43];
     document.forms[0].generarPlazos.checked = datos[44];     
  }else{

     document.getElementById("et1").className  = 'etiquetaDeshabilitada';
     document.getElementById("et2").className ='etiquetaDeshabilitada';
     document.getElementById("et3").className ='etiquetaDeshabilitada';
     document.forms[0].plazoFin.value='';
     document.forms[0].generarPlazos.checked = false;     
  }
  document.forms[0].generarPlazos.disabled = true;
  
  
  
  document.forms[0].unidadesPlazo.value=datos[11];
  var disp = datos[12];
  if(disp == 1) {
    document.forms[0].disponible.checked = true;
  } else {
    document.forms[0].disponible.checked = false;
  }
  //UNIDAD INICIO
  document.forms[0].codUnidadInicio.value = datos[13];
  document.forms[0].codVisibleUnidadInicio.value = datos[37];
  if(document.forms[0].codUnidadInicio.value == -99999) {
    document.forms[0].descUnidadInicio.value = "";
    document.forms[0].radioUnidadInicio[0].checked=true;
    document.forms[0].codVisibleUnidadInicio.value = "";
  } else if(document.forms[0].codUnidadInicio.value == -99998) {
      document.forms[0].descUnidadInicio.value = "";
      document.forms[0].radioUnidadInicio[1].checked=true;
      document.forms[0].codVisibleUnidadInicio.value = "";
  } else if(document.forms[0].codUnidadInicio.value == "") {
      document.forms[0].descUnidadInicio.value = "";
      document.forms[0].radioUnidadInicio[2].checked=true;
      document.forms[0].codVisibleUnidadInicio.value = "";
  } else {
      document.forms[0].descUnidadInicio.value = datos[14];
      document.forms[0].radioUnidadInicio[3].checked=true;
  }
 
  // Unidad de tramite
  seleccionUnidadTramite(datos[15]);
  listaUnidadesTramitadoras = listaUtrs;
  mostrarListaUnidadTramite();
  
  tramiteEnNavegacion(enlace1,numTramites1);

  var eliminadoTramite = datos[18];
  if(eliminadoTramite == "eliminado") {
    var texto1='<%=descriptor.getDescripcion("eliminacionHecha")%>';
    jsp_alerta("A",texto1);
  } else if(eliminadoTramite == "noEliminado") {
    var texto1='<%=descriptor.getDescripcion("eliminacionNoHecha")%>';
    jsp_alerta("A",texto1);
  }
  document.forms[0].texto.value = datos[19];
  tipoCond = datos[20];
  if(tipoCond == "Tramite") {
    document.forms[0].tipoCondicion[1].checked = true;
    document.forms[0].cmdListaTramite.disabled = false;
  } else if(tipoCond == "Pregunta") {
    document.forms[0].tipoCondicion[4].checked = true;
  } else if(tipoCond == "Finalizacion") {
    document.forms[0].tipoCondicion[2].checked = true;
  } else if(tipoCond == "Resolucion") {
    document.forms[0].tipoCondicion[3].checked = true;
  } else {
    document.forms[0].tipoCondicion[0].checked = true;
  }
  tipoCondSF = datos[21];
  if(tipoCondSF == "TramiteSI") {
    document.forms[0].tipoFavorableSI[0].checked = true;
    document.forms[0].cmdListaTramiteSiFavorable.disabled = false;
  } else if(tipoCondSF == "FinalizacionSI") {
    document.forms[0].tipoFavorableSI[1].checked = true;
  } else {
    document.forms[0].tipoFavorableSI[0].checked = false;
    document.forms[0].tipoFavorableSI[1].checked = false;
  }
  tipoCondNF = datos[22];
  if(tipoCondNF == "TramiteNO") {
    document.forms[0].tipoFavorableNO[0].checked = true;
    document.forms[0].cmdListaTramiteNoFavorable.disabled = false;
  } else if(tipoCondNF == "FinalizacionNO") {
    document.forms[0].tipoFavorableNO[1].checked = true;
  } else {
    document.forms[0].tipoFavorableNO[0].checked = false;
    document.forms[0].tipoFavorableNO[1].checked = false;
  }
  if(document.forms[0].texto.value != "" || document.forms[0].tipoCondicion[3].checked == true) {
    activarButtonsAcciones();
  }
  var tipoUP = datos[23];
  if(tipoUP == "H")
    document.forms[0].unidadesPlazo[0].checked=true;
  else if(tipoUP == "N")
    document.forms[0].unidadesPlazo[1].checked=true;
  else if(tipoUP == "M")
    document.forms[0].unidadesPlazo[2].checked=true;
  else {
    document.forms[0].unidadesPlazo[0].checked=false;
    document.forms[0].unidadesPlazo[1].checked=false;
    document.forms[0].unidadesPlazo[2].checked=false;
  }
  var tramInic = datos[24];
  if(tramInic == 1) {
    document.forms[0].tramiteInicio.checked = true;
  } else {
    document.forms[0].tramiteInicio.checked = false;
  }

  var soEsta = datos[25];
  if(soEsta == 1) {
    document.forms[0].soloEsta.checked = true;
  } else {
    document.forms[0].soloEsta.checked = false;
  }
  document.forms[0].instrucciones.value=datos[26];

    var notUnidadTramitIni = datos[31];
    if(notUnidadTramitIni == 1) {
      document.forms[0].checkUdadTramitIni.checked = true;
    } else {
      document.forms[0].checkUdadTramitIni.checked = false;
    }
    var notUnidadTramitFin = datos[32];
    if(notUnidadTramitFin == 1) {
      document.forms[0].checkUdadTramitFin.checked = true;
    } else {
      document.forms[0].checkUdadTramitFin.checked = false;
    }
    var notUsuUnidadTramitIni = datos[33];
    if(notUsuUnidadTramitIni == 1) {
      document.forms[0].checkUsuUdadTramitIni.checked = true;
    } else {
      document.forms[0].checkUsuUdadTramitIni.checked = false;
    }
    var notUsuUnidadTramitFin = datos[34];
    if(notUsuUnidadTramitFin == 1) {
      document.forms[0].checkUsuUdadTramitFin.checked = true;
    } else {
      document.forms[0].checkUsuUdadTramitFin.checked = false;
    }
    var notInteresadosIni = datos[35];
    if(notInteresadosIni == 1) {
      document.forms[0].checkInteresadosIni.checked = true;
    } else {
      document.forms[0].checkInteresadosIni.checked = false;
    }
    var notInteresadosFin = datos[36];
    if(notInteresadosFin == 1) {
      document.forms[0].checkInteresadosFin.checked = true;
    } else {
      document.forms[0].checkInteresadosFin.checked = false;
    }
    
    var notUsuInicioTramiteIni = datos[46];

    if(notUsuInicioTramiteIni == 1){
        document.forms[0].checkUsuarioTramiteIni.checked = true;
    } else{
        document.forms[0].checkUsuarioTramiteIni.checked = false;
    }

    var notUsuInicioTramiteFin = datos[47];

    if(notUsuInicioTramiteFin == 1){
        document.forms[0].checkUsuarioTramiteFin.checked = true;
    } else {
        document.forms[0].checkUsuarioTramiteFin.checked = false;
    }

    var notUsuInicioExpedIni = datos[48];

    if(notUsuInicioExpedIni == 1){
        document.forms[0].checkUsuarioExpedIni.checked = true;
    }else{
        document.forms[0].checkUsuarioExpedIni.checked = false;
    }

    var notUsuInicioExpedFin = datos[49];

    if(notUsuInicioExpedFin == 1){
        document.forms[0].checkUsuarioExpedFin.checked = true;
    }else{
        document.forms[0].checkUsuarioExpedFin.checked = false;
    }
    var notUsuTraFinPlazo = datos[51];	
    if(notUsuTraFinPlazo == 1){	
        document.forms[0].checkNotUsuTraFinPlazo.checked = true;	
    }else{	
        document.forms[0].checkNotUsuTraFinPlazo.checked = false;	
    }	
    var notUsuExpFinPlazo = datos[52];	
    if(notUsuExpFinPlazo == 1){	
        document.forms[0].checkNotUsuExpFinPlazo.checked = true;	
        	
    }else{	
        document.forms[0].checkNotUsuExpFinPlazo.checked = false;	
    }	
    var notUORFinPlazo = datos[53]	
    if(notUORFinPlazo ==1){	
        document.forms[0].checkNotUORFinPlazo.checked = true;	
    }else{	
        document.forms[0].checkNotUORFinPlazo.checked = false;	
    }
    
    document.forms[0].codExpRel.value = datos[39];
    document.forms[0].descExpRel.value = datos[40];
    document.forms[0].codCargo.value = datos[41];
    document.forms[0].cod_visible_cargo.value = datos[42];
    onchangeCodCargo();


  if(document.forms[0].importar.value == "si") {
    desactivarBotonesListasTramites();
    desactivarBotonesCapa1();
  }
  if(document.forms[0].noModificar.value == "si") {
    desactivarBotonesListasTramites();
  }
  activarBotonesListas();
  listaTablaEntrada = lista1;
  listaTablaEntradaOriginal = lista2;
  tab.lineas=listaTablaEntrada;
  refresca();

  codigo_tramiteTabla = lista3;
  nombre_tramiteTabla = lista4;

  listaDoc = lista5;
  listaDocOriginal = lista6;
  tabDoc.lineas=listaDoc;
  refrescaDoc();
  //borrarDatosDoc();

  listaEnlaces = lista7;
  listaEnlacesOriginal = lista8;
  tabEnlaces.lineas=listaEnlaces;
  tabEnlaces.displayTabla();
  borrarDatosEnlaces();

  id_tramiteTabla=lista9;
  codigo_tramiteTabla=lista10;
  nombre_tramiteTabla = lista11;
  cargarCombos();
  limpiarCombosWS();

  listaCampos = lista12;
  listaCamposOriginal = lista13;
  tabCampos.lineas = listaCampos;
  refrescaCampos();
  
  listaAgrupaciones = listaAgrup;
  listaAgrupacionesOriginal = listaAgrupOriginal;
  tabAgrupaciones.lineas = listaAgrupaciones;
  refrescaAgrupaciones();

  listaConfSW = lista14;
  listaConfSWCfos = lista15;
  howManySW = listaConfSW.length;
  tiposOperaciones = ltiposOperaciones;
  nombresOperaciones = lnombresOperaciones;
  nombresModulo = lnombresModulos;
  

  listNom = getNombresConfSW(listaConfSW);

  tabConfSW.lineas = listNom;
  tabConfSW.displayTabla();
  document.forms[0].nombreTramiteI.value = document.forms[0].nombreTramite.value;


  var vector = new Array(document.forms[0].txtCodigo,document.forms[0].txtDescripcion);
  deshabilitarGeneral(vector);
  
  var botonesCapaUno = [document.forms[0].cmdAlta, document.forms[0].cmdModificar, document.forms[0].cmdEliminarTram, document.forms[0].cmdDefProc];
  habilitarGeneral(botonesCapaUno);
  
  var botonesListaTramites = [document.forms[0].cmdListaTramite, document.forms[0].cmdListaTramiteSiFavorable, document.forms[0].cmdListaTramiteNoFavorable];
  habilitarGeneral(botonesListaTramites);

  tipoOperacionRetroceso = listaTiposRetroceso;
  actualizarNotAutomaticas(listaNotAutomaticas[0],listaNotAutomaticas[1],listaNotAutomaticas[2],listaNotAutomaticas[3],listaNotAutomaticas[4],listaNotAutomaticas[5],listaNotAutomaticas[6],listaNotAutomaticas[7],listaNotAutomaticas[8],listaNotAutomaticas[9],listaNotAutomaticas[10],listaNotAutomaticas[11]);  
}

function tramiteEliminado(datos) {
  borrarDatos();

  var l = new Array();
  tab.lineas = l;
  refresca();
  tabDoc.lineas = l;
  refrescaDoc();
  tp1.setSelectedIndex(0);
  var eliminadoTramite = datos[1];
  if(eliminadoTramite == "eliminado") {
    var texto1='<%=descriptor.getDescripcion("eliminacionHecha")%>';
    jsp_alerta("A",texto1);
  } else if(eliminadoTramite == "noEliminado") {
    var texto1='<%=descriptor.getDescripcion("eliminacionNoHecha")%>';
    jsp_alerta("A",texto1);
  }
  document.forms[0].codMunicipio.value = datos[2];
  document.forms[0].txtCodigo.value = datos[3];
  document.forms[0].txtDescripcion.value = datos[4];
  document.forms[0].enviar.value = datos[5];
  deInsercion = "no";
  borrarTablaEntrada();
  borrarTablaDoc();
  // NAVEGACION TRAMITES.  23/09/2003
  tramiteEnNavegacion(datos[6],datos[7]);

  mostrarCapasBotones('capaBotones1');
}

function pulsarProcedimiento() {
  var vector = new Array(document.forms[0].txtCodigo,document.forms[0].txtDescripcion);
  habilitarGeneral(vector);
  document.forms[0].opcion.value="envioDeTramite";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
  document.forms[0].submit();
}

function pulsarEliminarTramite() {
  if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjEliminarTram")%>') ==1) {
      pleaseWait('on');
    document.forms[0].opcion.value="eliminar";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
    document.forms[0].submit();
  }
}

function desactivarFormulario() { 
    comboActivo=false;
  deshabilitarDatos(document.forms[0]);
  activarBotonera();  
  comboClasifTramite.deactivate();
    comboExpRel.deactivate();
  var comboUnidadInicio = [document.forms[0].codVisibleUnidadInicio,document.forms[0].descUnidadInicio,
                  document.getElementById("botonUnidadInicio")];
  deshabilitarGeneral(comboUnidadInicio);
    var comboCargo = [document.forms[0].cod_visible_cargo,document.forms[0].descCargo,document.getElementById("botonCargo")];
    deshabilitarGeneral(comboCargo);
    deshabilitarImagen(document.getElementsByClassName("fa-chevron-circle-down"),true);
  comboTramiteTabla.deactivate();
  comboEstadoTramiteTabla.deactivate();
  comboEstadoEnlace.deactivate();
  comboAvanzar.deactivate();
  comboRetroceder.deactivate();
  comboIniciar.deactivate();
  comboCondicionTabla.deactivate();

  comboNotCercaFinPlazo.deactivate();
  comboNotFueraDePlazo.deactivate();
  comboAdmiteNotifElect.deactivate();

  document.forms[0].radioUnidadInicio[0].disabled = true;
  document.forms[0].radioUnidadInicio[1].disabled = true;
  document.forms[0].radioUnidadInicio[2].disabled = true;
  document.forms[0].radioUnidadInicio[3].disabled = true;

  document.forms[0].codUnidadTramite[0].disabled = true;
  document.forms[0].codUnidadTramite[1].disabled = true;
  document.forms[0].codUnidadTramite[2].disabled = true;
  document.forms[0].codUnidadTramite[3].disabled = true;
  document.forms[0].codUnidadTramite[4].disabled = true;
  document.getElementById("botonListadoUtrs").style.visibility = "hidden";

  document.forms[0].disponible.disabled = true;
  document.forms[0].tramiteInicio.disabled = true;
  document.forms[0].soloEsta.disabled = true;
  document.forms[0].tipoCondicion[0].disabled = true;
  document.forms[0].tipoCondicion[1].disabled = true;
  document.forms[0].tipoCondicion[2].disabled = true;
  document.forms[0].tipoCondicion[3].disabled = true;
  document.forms[0].tipoCondicion[4].disabled = true;
  document.forms[0].tipoFavorableNO[0].disabled = true;
  document.forms[0].tipoFavorableNO[1].disabled = true;
  document.forms[0].tipoFavorableSI[0].disabled = true;
  document.forms[0].tipoFavorableSI[1].disabled = true;
  //Notificaciones
  document.forms[0].checkUdadTramitIni.disabled = true;
  document.forms[0].checkUdadTramitFin.disabled = true;
  document.forms[0].checkUsuUdadTramitIni.disabled = true;
  document.forms[0].checkUsuUdadTramitFin.disabled = true;
  document.forms[0].checkInteresadosIni.disabled = true;
  document.forms[0].checkInteresadosFin.disabled = true;
  document.forms[0].notificarCercaFinPlazo.disabled = true;
  document.forms[0].notificarFueraDePlazo.disabled = true;
  document.forms[0].admiteNotificacionElectronica.disabled = true;
  document.forms[0].notificacionElectronicaObligatoria.disabled = true;
  document.forms[0].checkUsuarioTramiteIni.disabled = true;
  document.forms[0].checkUsuarioTramiteFin.disabled = true;
  document.forms[0].checkUsuarioExpedIni.disabled = true;
  document.forms[0].checkUsuarioExpedFin.disabled = true;
  document.forms[0].checkNotUsuTraFinPlazo.disabled=true;	
  document.forms[0].checkNotUsuExpFinPlazo.disabled=true;	
  document.forms[0].checkNotUORFinPlazo.disabled=true;
   

  //Notificaciones
  borrarDatosTablaEntrada();
  borrarDatosEnlaces();
  desactivarTablas();
  document.forms[0].unidadesPlazo[0].disabled = true;
    document.forms[0].unidadesPlazo[1].disabled = true;
    document.forms[0].unidadesPlazo[2].disabled = true;
     document.forms[0].plazoFin.disabled = true;
     document.forms[0].generarPlazos.disabled = true;
     document.getElementById("et1").className  = 'etiquetaDeshabilitada';
     document.getElementById("et2").className ='etiquetaDeshabilitada';
     document.getElementById("et3").className ='etiquetaDeshabilitada';
 
    if(document.forms[0].tramiteNotificado)
        document.forms[0].tramiteNotificado.disabled = true;
}

function activarFormulario() {
    comobActivo=true;
    habilitarDatos(document.forms[0]);    
    document.forms[0].instrucciones.readOnly = false;
    document.forms[0].instrucciones.className = "textareaTexto";  
    
    comboClasifTramite.activate();
    comboExpRel.activate();
    var comboCargo = [document.forms[0].cod_visible_cargo,document.forms[0].descCargo,document.getElementById('botonCargo')];
    habilitarGeneral(comboCargo);
    deshabilitarImagen(document.getElementsByClassName("fa-chevron-circle-down"),false);

    
    if (document.forms[0].radioUnidadInicio[3].checked == true) {
        var comboUnidadInicio = [document.forms[0].codVisibleUnidadInicio,document.forms[0].descUnidadInicio];
        habilitarGeneral(comboUnidadInicio);
        deshabilitarImagen(document.getElementsByName("botonUnidadInicio"),false);
    } else {
        var comboUnidadInicio = [document.forms[0].codVisibleUnidadInicio,document.forms[0].descUnidadInicio];
        deshabilitarGeneral(comboUnidadInicio);
        deshabilitarImagen(document.getElementsByName("botonUnidadInicio"),true);
    }
    
    if (document.forms[0].codUnidadTramite[4].checked == true) {
        document.getElementById("botonListadoUtrs").style.visibility = "visible";
    } else {
        document.getElementById("botonListadoUtrs").style.visibility = "hidden";
    }
    comboTramiteTabla.deactivate();
    comboEstadoTramiteTabla.deactivate();

    comboEstadoEnlace.activate();
    comboAvanzar.activate();
    comboRetroceder.activate();
    comboIniciar.activate();
    comboCondicionTabla.activate();

    document.forms[0].disponible.disabled = false;
    document.forms[0].tramiteInicio.disabled = false;
    
    document.forms[0].radioUnidadInicio[0].disabled = false;
    document.forms[0].radioUnidadInicio[1].disabled = false;
    document.forms[0].radioUnidadInicio[2].disabled = false;
    document.forms[0].radioUnidadInicio[3].disabled = false;

    document.forms[0].codUnidadTramite[0].disabled = false;
    document.forms[0].codUnidadTramite[1].disabled = false;
    document.forms[0].codUnidadTramite[2].disabled = false;
    document.forms[0].codUnidadTramite[3].disabled = false;
    document.forms[0].codUnidadTramite[4].disabled = false;

    document.forms[0].soloEsta.disabled = false;
    document.forms[0].cmdListaTramite.disabled = true; 
    document.forms[0].cmdListaTramiteSiFavorable.disabled = true; 
    document.forms[0].cmdListaTramiteNoFavorable.disabled = true; 
    //Notificaciones
    document.forms[0].checkUdadTramitIni.disabled = false;
    document.forms[0].checkUdadTramitFin.disabled = false;
    document.forms[0].checkUsuUdadTramitIni.disabled = false;
    document.forms[0].checkUsuUdadTramitFin.disabled = false;
    document.forms[0].checkInteresadosIni.disabled = false;
    document.forms[0].checkInteresadosFin.disabled = false;
    document.forms[0].checkUsuarioTramiteIni.disabled = false;
    document.forms[0].checkUsuarioTramiteFin.disabled = false;
    document.forms[0].checkUsuarioExpedIni.disabled = false;
    document.forms[0].checkUsuarioExpedFin.disabled = false;
    document.forms[0].notificarCercaFinPlazo.disabled = false;
    document.forms[0].notificarFueraDePlazo.disabled = false;
    comboNotCercaFinPlazo.activate();
    comboNotFueraDePlazo.activate();
    
    // #279909
    if(!document.forms[0].tramiteNotificado || document.forms[0].tramiteNotificado.checked==true) { 
        activarCapaNotificacionesElectronicas();
    } else {
        desactivarCapaNotificacionesElectronicas();
    }
    
    comprobarAdmiteNotificacion();
    
    activarDestinatariosFuncionPlazo();
    //Notificaciones
    activarTablas();
     
}

function comprobarCondicionesSalida() {
  if(document.forms[0].tipoCondicion[0].checked || document.forms[0].tipoCondicion[2].checked
  || document.forms[0].tipoCondicion[1].checked || document.forms[0].tipoCondicion[3].checked) {
    return true;
  }
  if(document.forms[0].tipoCondicion[4].checked && document.forms[0].texto.value =="") {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjFaltaPregunta")%>');
  }
  if(document.forms[0].tipoCondicion[4].checked && document.forms[0].texto.value !="") {
    return true;
  }
  return false;
}

function onClickUnidadesPlazo() {
  if (document.forms[0].plazo.value=='') {
    document.forms[0].unidadesPlazo[0].checked = false;
    document.forms[0].unidadesPlazo[1].checked = false;
    document.forms[0].unidadesPlazo[2].checked = false;
     document.forms[0].plazoFin.disabled = true;
     document.forms[0].generarPlazos.disabled = true;
     document.getElementById("et1").className  = 'etiquetaDeshabilitada';
     document.getElementById("et2").className ='etiquetaDeshabilitada';
     document.getElementById("et3").className ='etiquetaDeshabilitada';
     document.forms[0].plazoFin.value='';
     document.forms[0].generarPlazos.checked = false;
  }else{
     document.forms[0].plazoFin.disabled = false;
     document.forms[0].generarPlazos.disabled = false;
     document.getElementById("et1").className = 'etiqueta';
     document.getElementById("et2").className ='etiqueta';
     document.getElementById("et3").className ='etiqueta';
     var porcentajePlazo = '<bean:write name="DefinicionTramitesForm" property="plazoFin"/>';
     if (porcentajePlazo == '') document.forms[0].plazoFin.value='0';
  }
}

function comprobarPlazo() {
  if(document.forms[0].plazo.value != '') {
    if( !document.forms[0].unidadesPlazo[0].checked && !document.forms[0].unidadesPlazo[1].checked && !document.forms[0].unidadesPlazo[2].checked) {
    jsp_alerta("A",'<%=descriptor.getDescripcion("plNotNoCor")%>');
      return 0;
    }
  }
  return 1;
}

function comprobarUnidades() {
    if ((document.forms[0].radioUnidadInicio[3].checked == true) && (document.forms[0].codVisibleUnidadInicio.value=="")) {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjOtraUnidadInicio")%>');
        return 0;
    }
    if ((document.forms[0].codUnidadTramite[4].checked == true) && (listaUnidadesTramitadoras.length == 0)) { 
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjOtrasUtrs")%>');
        return 0;
    }
    return 1;
}

function pulsarAltaTablaEntrada() { 
    if(comboCondicionTabla.des.value.length != 0) {
        var tipoCondicion = document.forms[0].descCondicionTabla.value;
      if(tipoCondicion == "TRÁMITE" || tipoCondicion == etiqFirmaDoc) {
          var cod = document.forms[0].codTramiteTabla.value;
          var yaExiste = 0;
          if(comprobarObligatoriosTablaEntrada()) {
            for(l=0; l < listaTablaEntradaOriginal.length; l++){
              if ((listaTablaEntradaOriginal[l][2]) == cod && listaTablaEntradaOriginal[l][1]==tipoCondicion){
                yaExiste = 1;
              }
            }

            if(yaExiste == 0) {
                        var id = "";
                        if(tipoCondicion == "TRÁMITE"){
                          for(j=0; j < codigo_tramiteTabla.length; j++) {     
                              
                                if((codigo_tramiteTabla[j]) == cod) {
                                    id = id_tramiteTabla[j];
                                }
                          }
                        }
              var lineas = tab.lineas;
              i = lineas.length;

              listaTablaEntrada[i]=[document.forms[0].descCondicionTabla.value, document.forms[0].descTramiteTabla.value+"/"+document.forms[0].descEstadoTramiteTabla.value];
              var docSeleccionado = comboCondicionTabla.selectedIndex;
              var condicionSeleccionada = comboTramiteTabla.selectedIndex;
        			  
              var COD_DOCUMENTO_PRESENTADO="";            
              if(docSeleccionado==2){
                  // TIPO DE CONDICION ES UN DOCUMENTO
                  COD_DOCUMENTO_PRESENTADO = codigo_documentoPresentado[condicionSeleccionada];
                  
              }else COD_DOCUMENTO_PRESENTADO = "";
                
              listaTablaEntradaOriginal[i]=[id,
                                    document.forms[0].descCondicionTabla.value,
                                    document.forms[0].codTramiteTabla.value,
                                    document.forms[0].descTramiteTabla.value,
                                    document.forms[0].descEstadoTramiteTabla.value,
                                    i+1,COD_DOCUMENTO_PRESENTADO];
                      
              tab.lineas=listaTablaEntrada;
              refresca();
              borrarDatosTablaEntrada();
            } else {
              jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
            }
          } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
          }
      } else {
          var ok = true;
          for (i=0; i < listaTablaEntrada.length; i++) {
              if (listaTablaEntrada[i][0] == "EXPRESION") {
                  ok = false;
                  break;
              }
          }
          if (ok) {
              abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + 
                        "<html:rewrite page='/jsp/sge/ventanaSQLTramites.jsp'/>",'',
                        'width=1050,height=750,scrollbars=no',function(sql){
                        if (sql!=null)
                            pulsarAltaExpresionTablaEntrada(sql);
                    });
          }
          else {
              jsp_alerta("A",'<%=descriptor.getDescripcion("msjExpresionYaExiste")%>');
          }
      }
    }
}

function pulsarModificarTablaEntrada() { 

    if(tab.selectedIndex != -1) {
        var tipoCondicion = listaTablaEntradaOriginal[tab.selectedIndex][1];
      if(tipoCondicion == "TRÁMITE" || tipoCondicion == etiqFirmaDoc) {
      var cod = document.forms[0].codTramiteTabla.value;
      var yaExiste = 0;
        if (comprobarObligatoriosTablaEntrada()) {                  
          for(l=0; l < listaTablaEntradaOriginal.length; l++){
            if(l != tab.selectedIndex) {
                    if ((listaTablaEntradaOriginal[l][2]) == cod && listaTablaEntradaOriginal[l][1]==tipoCondicion){
                yaExiste = 1;
              }
            }
          }
                  
          if(yaExiste == 0) {
            var id = "";
            if(tipoCondicion == "TRÁMITE"){
              var id = "";
              for(j=0; j < codigo_tramiteTabla.length; j++) {                 
                if((codigo_tramiteTabla[j]) == cod) {
                  id = id_tramiteTabla[j];
                }
              }
            }                   
            var j = tab.selectedIndex;
                  listaTablaEntrada[j]=[document.forms[0].descCondicionTabla.value, document.forms[0].descTramiteTabla.value+"/"+document.forms[0].descEstadoTramiteTabla.value];
			
            var docSeleccionado = comboCondicionTabla.selectedIndex;
            var condicionSeleccionada = comboTramiteTabla.selectedIndex;
                    
            var COD_DOCUMENTO_PRESENTADO="";
			 
            if(docSeleccionado==2){
                // TIPO DE CONDICION ES UN DOCUMENTO
                COD_DOCUMENTO_PRESENTADO = codigo_documentoPresentado[condicionSeleccionada];                
            }else COD_DOCUMENTO_PRESENTADO = "";
			
			listaTablaEntradaOriginal[j]=[id,
                                          document.forms[0].descCondicionTabla.value,
                                          document.forms[0].codTramiteTabla.value,
                                          document.forms[0].descTramiteTabla.value,
                                          document.forms[0].descEstadoTramiteTabla.value,
                                          j+1,COD_DOCUMENTO_PRESENTADO];						
			
            tab.lineas=listaTablaEntrada;
            refresca();
            borrarDatosTablaEntrada();
          } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
          }
        } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
        }
        } else {
            var parametro = listaTablaEntrada[tab.selectedIndex][1];
            parametro = parametro.replace(/&lt;/g,"<");
            parametro = parametro.replace(/&gt;/g,">");
            parametro = parametro.replace(/&#39;/g,"'");
            parametro = parametro.replace(/%/g,"_ascii_37_");

            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + 
                    "<html:rewrite page='/jsp/sge/ventanaSQLTramites.jsp'/>?sql="+parametro,'',
            'width=1050,height=500,status='+ '<%=statusBar%>',function(sql){
                        if (sql!=null) 
                            pulsarModificarExpresionTablaEntrada(sql);
                    });
      }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');

}

function pulsarEliminarTablaEntrada() {
   if(tab.selectedIndex != -1) {
       if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarCondEntrada")%>')) {
       var list = new Array();
       var listOrig = new Array();
       tamIndex=tab.selectedIndex;
       tamLength=tab.lineas.length;
       for (i=tamIndex - 1; i < listaTablaEntrada.length - 1; i++){
         if (i + 1 <= listaTablaEntrada.length - 2){
           listaTablaEntrada[i + 1]=listaTablaEntrada[i + 2];
           listaTablaEntradaOriginal[i + 1]=listaTablaEntradaOriginal[i + 2];
         }
       }
       for(j=0; j < listaTablaEntrada.length-1 ; j++){
         list[j] = listaTablaEntrada[j];
         listOrig[j] = listaTablaEntradaOriginal[j];
         listOrig[j][5] = j+1;
       }
       tab.lineas=list;
       refresca();
       borrarDatosTablaEntrada();
       listaTablaEntrada=list;
       listaTablaEntradaOriginal = listOrig;
       } else {
         tab.selectLinea(tab.selectedIndex);
         tab.selectedIndex = -1;
         borrarDatosTablaEntrada();
       }
   } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarAltaEnlaces() {
  var url = document.forms[0].txtUrl.value;
  var yaExiste = 0;
  var maxCod = -1;
  if(comprobarObligatoriosEnlaces()) {
    for(l=0; l < listaEnlaces.length; l++){
      if(maxCod < listaEnlacesOriginal[l][0]) maxCod = listaEnlacesOriginal[l][0];
        if ((listaEnlaces[l][1]).toUpperCase() == url )	yaExiste = 1;
    }
    if(yaExiste == 0){
      var est = "1";
      if(document.forms[0].descEstUrl.value == '<%=descriptor.getDescripcion("etiq_no")%>') est = "0";
      listaEnlaces[listaEnlaces.length]=[document.forms[0].txtDescUrl.value, document.forms[0].txtUrl.value, est];
      listaEnlacesOriginal[listaEnlacesOriginal.length]=[((maxCod*1)+1),document.forms[0].txtDescUrl.value, document.forms[0].txtUrl.value, est];
      tabEnlaces.lineas=listaEnlaces;
      tabEnlaces.displayTabla();
      borrarDatosEnlaces();
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
    }
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
  }
}

function pulsarModificarEnlaces() {
  var url = document.forms[0].txtUrl.value;
  var yaExiste = 0;
  if(tabEnlaces.selectedIndex != -1) {
    if(comprobarObligatoriosEnlaces()) {
        for(l=0; l < listaEnlaces.length; l++){
        if(l != tabEnlaces.selectedIndex) {
            if ((listaEnlaces[l][1]) == url ){
            yaExiste = 1;
            }
        }
        }
        if(yaExiste == 0){
        var est = "1";
        if(document.forms[0].descEstUrl.value == '<%=descriptor.getDescripcion("etiq_no")%>') est = "0";
        listaEnlaces[tabEnlaces.selectedIndex]=[document.forms[0].txtDescUrl.value, document.forms[0].txtUrl.value, est];
        listaEnlacesOriginal[tabEnlaces.selectedIndex]=[listaEnlacesOriginal[tabEnlaces.selectedIndex][0],document.forms[0].txtDescUrl.value, document.forms[0].txtUrl.value, est];
        tabEnlaces.lineas=listaEnlaces;
        tabEnlaces.displayTabla();
        borrarDatosEnlaces();
      } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
      }
    } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
    }
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarEliminarEnlaces() {
  if(tabEnlaces.selectedIndex != -1) {
      if(comprobarObligatoriosEnlaces()) {
          if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarEnlace")%>')) {
            var list = new Array();
            var listOrig = new Array();
            tamIndex = tabEnlaces.selectedIndex;
            tamLength = listaEnlaces.length;
            for (i=0; i < tamLength-1; i++){
          if(i < tamIndex){
            list[i] = listaEnlaces[i];
            listOrig[i] = listaEnlacesOriginal[i];
          }else{
            list[i]=listaEnlaces[i+1];
            listOrig[i]=listaEnlacesOriginal[i+1];
          }
            }
        listaEnlaces=list;
            listaEnlacesOriginal = listOrig;

            tabEnlaces.lineas=listaEnlaces;
        tabEnlaces.displayTabla();
            borrarDatosEnlaces();
          } else {
            tabEnlaces.selectLinea(tabEnlaces.selectedIndex);
            tabEnlaces.selectedIndex = -1;
            borrarDatosEnlaces();
          }
      } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
      }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarNuevoWord() {
    document.forms[0].codProcedimiento.value = document.forms[0].txtCodigo.value;
    document.forms[0].codTramite.value = document.forms[0].codigoTramite.value;
    document.forms[0].codDocumento.value = "";
    document.forms[0].nombreDocumento.value = "";
    document.forms[0].docActivo.value = "SI";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";

    abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + 
            '<%=request.getContextPath()%>/jsp/editor/definicionDocumentoTramite.jsp',null,
        'width=700,height=500,scrollbars=no,status='+ '<%=statusBar%>',function(arrayResp){

    if (arrayResp != undefined && arrayResp != null){
        document.forms[0].relacion.value = arrayResp[0];
        document.forms[0].interesado.value =arrayResp[1];
        document.forms[0].editorTexto.value =arrayResp[2];
        if ("WORD" == arrayResp[2])
            document.forms[0].opcion.value = 'verDocumento';
        else 
            document.forms[0].opcion.value = 'verDocumentoOOffice';
  
     document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
     document.forms[0].submit();
   }
  });
}

function pulsarNuevaPlantilla() {
    document.forms[0].modificando.value=false;
    document.forms[0].codProcedimiento.value = document.forms[0].txtCodigo.value;
    document.forms[0].codTramite.value = document.forms[0].codigoTramite.value;
    document.forms[0].codDocumento.value = "";
    document.forms[0].nombreDocumento.value = "";
    document.forms[0].docActivo.value = "SI";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";


    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codTramite = document.forms[0].codTramite.value;
    var codAplicacion= document.forms[0].codAplicacion.value;
   
    
    
    source = "<html:rewrite page='/editor/DocumentosAplicacion.do'/>?opcion=documentoNuevo&codProcedimiento="+codProcedimiento+"&codTramite="+ codTramite+"&codAplicacion="+ codAplicacion;      
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
                'width=700,height=500,status='+ '<%=statusBar%>',function(listaDocsExternosJSON){
            if(listaDocsExternosJSON!=undefined){
                // Se recibe el String en formato JSON con los datos de los documentos externos 
                // una vez dado de alta el nuevo            
                this.cargaDocumentos();
            }
                  });
}


function cargaDocumentos(){
    document.forms[0].opcion.value = 'cargarDocumentosDesdeDefinicion';
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
    document.forms[0].submit();
}


function recuperaDatos(listaDocumentos,listaDocumentosOriginal) {
  listaDoc = listaDocumentos;
  listaDocOriginal = listaDocumentosOriginal;
  for(var i=0;i < listaDoc.length;i++) {
    if(listaDoc[i][2] == "S") {
	  listaDoc[i][2] = "POR INTERESADO";
    } else {
	  listaDoc[i][2] = "NO POR INTERESADO";
    }
    if (listaDoc[i][4] == "SI"){
        listaDoc[i][4] = '<span class="fa fa-check 2x"></span>';
    } else {
        listaDoc[i][4] = '<span class="fa fa-close 2x"></span>';
    }
    
    <%if(visibleAppExt){%>	
        if(listaDoc[i][8]== 'SI'){	
                    listaDoc[i][8]='<span class="fa fa-check 2x"</span>';	
        }else{	
                    listaDoc[i][8]='<span class="fa fa-close 2x"></span>';	
        }	
     <%}%>
  }
  tabDoc.lineas=listaDoc;
  refrescaDoc();
}

function pulsarModificarWord(){
    if(tabDoc.selectedIndex != -1){
        var codigoplantilla=listaDocOriginal[tabDoc.selectedIndex][4];
        var textoplantilla=listaDoc[tabDoc.selectedIndex][1];
        if(listaDoc[tabDoc.selectedIndex][2] == "POR INTERESADO") {
            document.forms[0].interesado.value = "S";
        } else {
            document.forms[0].interesado.value = "N";
        }
        if(listaDoc[tabDoc.selectedIndex][5] == "<%=m_Config.getString("constante.nombre.relacion").toUpperCase()%>") {
            document.forms[0].relacion.value = "S";
        } else {
            document.forms[0].relacion.value = "N";
        }
        document.forms[0].codProcedimiento.value = document.forms[0].txtCodigo.value;
        document.forms[0].codTramite.value = document.forms[0].codigoTramite.value;
        document.forms[0].codDocumento.value = codigoplantilla;
        document.forms[0].nombreDocumento.value = textoplantilla;
        document.forms[0].docActivo.value = "SI";
        document.forms[0].editorTexto.value = "SI";
        if (listaDoc[tabDoc.selectedIndex][7]=="WORD"){
            document.forms[0].opcion.value = 'verDocumento';
        }else if(listaDoc[tabDoc.selectedIndex][7]=="ODT"){   	
            document.forms[0].modificando.value = true;	
            document.forms[0].opcion.value="documentoModificar";
            var codProcedimiento = document.forms[0].codProcedimiento.value;	
            var codTramite = document.forms[0].codTramite.value;	
            var codAplicacion= document.forms[0].codAplicacion.value;	
            var interesado=document.forms[0].interesado.value;	
            var relacion=document.forms[0].relacion.value;	
            source = "<html:rewrite page='/editor/DocumentosAplicacion.do'/>?opcion=documentoModificar&codProcedimiento="+codProcedimiento+"&codTramite="+ codTramite+"&codAplicacion="+ codAplicacion;      	
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',	
                'width=700,height=500,status='+ '<%=statusBar%>',function(listaDocsExternosJSON){	
                if(listaDocsExternosJSON!=undefined){	
                // Se recibe el String en formato JSON con los datos de los documentos externos 	
                // una vez dado de alta el nuevo            	
                	
                }	
              });	
        }else {	
            document.forms[0].opcion.value = 'verDocumentoOOffice';
        }
        document.forms[0].target = "oculto";
        document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
        document.forms[0].submit();
    }  else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function descargarDoc(){

    if(tabDoc.selectedIndex !=-1){
        var textoplantilla=listaDoc[tabDoc.selectedIndex][1];
        document.forms[0].codProcedimiento.value=document.forms[0].txtCodigo.value;
        document.forms[0].codTramite.value=document.forms[0].codigoTramite.value;
        document.forms[0].codDocumento.value=listaDocOriginal[tabDoc.selectedIndex][4];
        document.forms[0].nombreDocumento.value=textoplantilla;
        if(listaDoc[tabDoc.selectedIndex][7]=="ODT"){
           document.forms[0].editorTexto.value=listaDoc[tabDoc.selectedIndex][7];	
           document.forms[0].opcion.value = 'descargarDocumento';	
           document.forms[0].target = "mainFrame";	
           document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";	
           document.forms[0].submit();	
       }	
} else{	
           jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');	
        }	
    	
}

function activarDoc(){
    if(tabDoc.selectedIndex != -1){
		if (listaDocOriginal[tabDoc.selectedIndex][7] == "NO") {
			document.forms[0].codProcedimiento.value = document.forms[0].txtCodigo.value;
			document.forms[0].codTramite.value = document.forms[0].codigoTramite.value;
			document.forms[0].codDocumento.value=listaDocOriginal[tabDoc.selectedIndex][4];
			document.forms[0].nombreDocumento.value=listaDoc[tabDoc.selectedIndex][1];
			document.forms[0].docActivo.value = "SI";
			document.forms[0].opcion.value = 'eliminarDocumento'; <%-- se utiliza la opcion de eliminar para activar tambien --%>
			document.forms[0].target = "oculto";
			document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
			document.forms[0].submit();
			document.forms[0].codDocumento.value='';
			document.forms[0].nombreDocumento.value='';
		} else {
			jsp_alerta('A', 'El documento ya se encuetra activo');
		}
    }else
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarEliminarDoc(){
  if(tabDoc.selectedIndex != -1){
	  if (listaDocOriginal[tabDoc.selectedIndex][7] == "SI") {
		document.forms[0].codProcedimiento.value = document.forms[0].txtCodigo.value;
		document.forms[0].codTramite.value = document.forms[0].codigoTramite.value;
		document.forms[0].codDocumento.value=listaDocOriginal[tabDoc.selectedIndex][4];
		document.forms[0].nombreDocumento.value=listaDoc[tabDoc.selectedIndex][1];
		document.forms[0].docActivo.value = "NO";
		document.forms[0].opcion.value = 'eliminarDocumento';
		document.forms[0].target = "oculto";
		document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
		document.forms[0].submit();
		document.forms[0].codDocumento.value='';
		document.forms[0].nombreDocumento.value='';
	} else {
		jsp_alerta('A', 'El documento ya se encuentra desactivado');
	}
  }else
	jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function eliminarDocumento(){
    var aviso = '<%=descriptor.getDescripcion("docDesactivado")%>';
    
    var linha = tabDoc.getLinea();
    var selIndex = tabDoc.selectedIndex;
    if (listaDocOriginal[selIndex][7] == 'SI'){ // Se ha pulsado [desactivar]
        listaDocOriginal[selIndex][7] = 'NO';
        linha[4] = '<span class="fa fa-close 2x"></span>';
    } else { // Se ha pulsado [activar]
        listaDocOriginal[selIndex][7] = 'SI';
        linha[4] = '<span class="fa fa-check 2x"></span>';
        aviso = '<%=descriptor.getDescripcion("docRecuperado")%>';
    }

    tabDoc.setLinea(linha);

    document.forms[0].codDocumento.value='';
    document.forms[0].nombreDocumento.value='';

    jsp_alerta('A',aviso);

}//de la funcion

function pulsarVisibleExt(){
	
    if(tabDoc.selectedIndex !=-1){
	
        document.forms[0].codProcedimiento.value = document.forms[0].txtCodigo.value;
        document.forms[0].codTramite.value = document.forms[0].codigoTramite.value;	
        document.forms[0].codDocumento.value=listaDocOriginal[tabDoc.selectedIndex][4];	
        document.forms[0].nombreDocumento.value=listaDoc[tabDoc.selectedIndex][1];	
        document.forms[0].visibleExt.value = (listaDocOriginal[tabDoc.selectedIndex][11]=="NO")?"SI":"NO";	
        document.forms[0].opcion.value="visibleExterior";	
        document.forms[0].target="oculto";	
        document.forms[0].action="<c:url value='/editor/DocumentosAplicacion.do'/>";	
        document.forms[0].submit();	
        document.forms[0].codDocumento.value='';	
        document.forms[0].nombreDocumento.value='';	
    }else{	
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');	
    }	
}

function pulsarListaTramite() {
  if(document.forms[0].importar.value == "si" || document.forms[0].tipoCondicion[1].disabled == true) {
    var codMun = document.forms[0].codMunicipio.value;
    var codProc = document.forms[0].txtCodigo.value;
    var codTram = document.forms[0].codigoTramite.value;
    var listaCodTramFS = document.forms[0].listaCodTramitesFlujoSalida.value;
    var listaDescTramFS = document.forms[0].listaDescTramitesFlujoSalida.value;
    var listaNombreTramFS = document.forms[0].listaNombreTramitesFlujoSalida.value;
    var ob = document.forms[0].oblig.value;
    var imp = document.forms[0].importar.value;
    var codApl = document.forms[0].codAplicacion.value;
    var source = "<c:url value='/sge/TablasIntercambiadoras.do?opcion=inicioListaSoloLectura'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+0+
            "&codMun="+codMun+"&codProc="+codProc+"&codTram="+codTram+"&eje="+listaCodTramFS+
            "&num="+listaDescTramFS+"&codClasifTram="+ob+"&nombreCodTram="+listaNombreTramFS+
            "&codPlantilla="+imp+"&nombDoc="+codApl,'ventana',
            'width=700,height=530,status='+ '<%=statusBar%>',function(datosConsulta){});
  } else {
    var codMun = document.forms[0].codMunicipio.value;
    var codProc = document.forms[0].txtCodigo.value;
    var codTram = "";
    if(document.getElementById('capaBotones4').style.display=='') {
      codTram = "";
    } else {
      codTram = document.forms[0].codigoTramite.value;
    }
    var listaCodTramFS = document.forms[0].listaCodTramitesFlujoSalida.value;
    var listaDescTramFS = document.forms[0].listaDescTramitesFlujoSalida.value;
    var listaNombreTramFS = document.forms[0].listaNombreTramitesFlujoSalida.value;
    var ob = document.forms[0].oblig.value;
    if (listaCodTramFS == "" || listaDescTramFS == "" || listaNombreTramFS == "") {
        listaCodTramFS = "";
        listaDescTramFS = "";
        listaNombreTramFS = "";
    }
    var source = "<c:url value='/sge/TablasIntercambiadoras.do?opcion=inicio'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+0+
            "&codMun="+codMun+"&codProc="+codProc+"&codTram="+codTram+"&eje="+listaCodTramFS+
            "&num="+listaDescTramFS+"&codClasifTram="+ob+"&nombreCodTram="+listaNombreTramFS,'ventana',
            'width=950,height=540,status='+ '<%=statusBar%>',function(datosConsulta){
                if(datosConsulta!=undefined){
                    if(datosConsulta[0] =="si") {
                        document.forms[0].listaCodTramitesFlujoSalida.value = datosConsulta[1];
                        document.forms[0].listaNombreTramitesFlujoSalida.value = datosConsulta[5];
                        document.forms[0].listaNumerosSecuenciaFlujoSalida.value = datosConsulta[2];
                        document.forms[0].listaDescTramitesFlujoSalida.value = datosConsulta[4];
                        document.forms[0].oblig.value = datosConsulta[3];
                        document.forms[0].numeroCondicionSalida.value = 0;
                    }
                }
            });
    }
}

function pulsarListaTramiteSiFavorable() {
  if(document.forms[0].importar.value == "si" || document.forms[0].tipoFavorableSI[0].disabled == true) {
    var codMun = document.forms[0].codMunicipio.value;
    var codProc = document.forms[0].txtCodigo.value;
    var codTram = document.forms[0].codigoTramite.value;
    var listaCodTramFS = document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value;
    var listaDescTramFS = document.forms[0].listaDescTramitesFlujoSalidaSiFavorable.value;
    var listaNombreTramFS = document.forms[0].listaNombreTramitesFlujoSalidaSiFavorable.value;
    var ob = document.forms[0].obligSiFavorable.value;
	var imp = document.forms[0].importar.value;
	var codApl = document.forms[0].codAplicacion.value;
    var source = "<c:url value='/sge/TablasIntercambiadoras.do?opcion=inicioListaSoloLectura'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+1+
        "&codMun="+codMun+"&codProc="+codProc+"&codTram="+codTram+"&eje="+listaCodTramFS+
        "&num="+listaDescTramFS+"&codClasifTram="+ob+"&nombreCodTram="+listaNombreTramFS+
        "&codPlantilla="+imp+"&nombDoc="+codApl,'ventana',
            'width=700,height=530,status='+ '<%=statusBar%>',function(datosConsulta){});
  } else {
    var codMun = document.forms[0].codMunicipio.value;
    var codProc = document.forms[0].txtCodigo.value;
    var codTram = "";
    if(document.getElementById('capaBotones4').style.display=='') {
      codTram = "";
    } else {
      codTram = document.forms[0].codigoTramite.value;
    }
    var listaCodTramFS = document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value;
    var listaDescTramFS = document.forms[0].listaDescTramitesFlujoSalidaSiFavorable.value;
    var listaNombreTramFS = document.forms[0].listaNombreTramitesFlujoSalidaSiFavorable.value;
    var ob = document.forms[0].obligSiFavorable.value;
    if (listaCodTramFS == "" || listaDescTramFS == "" || listaNombreTramFS == "") {
        listaCodTramFS = "";
        listaDescTramFS = "";
        listaNombreTramFS = "";
    }
    var source = "<c:url value='/sge/TablasIntercambiadoras.do?opcion=inicio'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+1+
        "&codMun="+codMun+"&codProc="+codProc+"&codTram="+codTram+"&eje="+listaCodTramFS+
        "&num="+listaDescTramFS+"&codClasifTram="+ob+"&nombreCodTram="+listaNombreTramFS,'ventana',
            'width=950,height=540,status='+ '<%=statusBar%>',function(datosConsulta){
                    if(datosConsulta!=undefined){
                        if(datosConsulta[0] =="si") {
                          document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value = datosConsulta[1];
                          document.forms[0].listaNombreTramitesFlujoSalidaSiFavorable.value = datosConsulta[5];
                          document.forms[0].listaNumerosSecuenciaFlujoSalidaSiFavorable.value = datosConsulta[2];
                          document.forms[0].listaDescTramitesFlujoSalidaSiFavorable.value = datosConsulta[4];
                          document.forms[0].obligSiFavorable.value = datosConsulta[3];
                          document.forms[0].numeroCondicionSalidaSiFavorable.value = 1;
                        }
                    }
            });
  }
}

function pulsarListaTramiteNoFavorable() {
  if(document.forms[0].importar.value == "si" || document.forms[0].tipoFavorableNO[0].disabled == true) {
    var codMun = document.forms[0].codMunicipio.value;
    var codProc = document.forms[0].txtCodigo.value;
    var codTram = document.forms[0].codigoTramite.value;
    var listaCodTramFS = document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value;
    var listaDescTramFS = document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value;
    var listaNombreTramFS = document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value;
    var ob = document.forms[0].obligNoFavorable.value;
    var imp = document.forms[0].importar.value;
    var codApl = document.forms[0].codAplicacion.value;
    var source = "<c:url value='/sge/TablasIntercambiadoras.do?opcion=inicioListaSoloLectura'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+2+
        "&codMun="+codMun+"&codProc="+codProc+"&codTram="+codTram+"&eje="+listaCodTramFS+
        "&num="+listaDescTramFS+"&codClasifTram="+ob+"&nombreCodTram="+listaNombreTramFS+
        "&codPlantilla="+imp+"&nombDoc="+codApl,'ventana',
        'width=700,height=530,status='+ '<%=statusBar%>',function(datosConsulta){});
  } else {
    var codMun = document.forms[0].codMunicipio.value;
    var codProc = document.forms[0].txtCodigo.value;
    var codTram = "";
    if(document.getElementById('capaBotones4').style.display=='') {
      codTram = "";
    } else {
      codTram = document.forms[0].codigoTramite.value;
    }
    var listaCodTramFS = document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value;
    var listaDescTramFS = document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value;
    var listaNombreTramFS = document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value;
    var ob = document.forms[0].obligNoFavorable.value;
    if (listaCodTramFS == "" || listaDescTramFS == "" || listaNombreTramFS == "") {
        listaCodTramFS = "";
        listaDescTramFS = "";
        listaNombreTramFS = "";
    }
    var source = "<c:url value='/sge/TablasIntercambiadoras.do?opcion=inicio'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+2+
        "&codMun="+codMun+"&codProc="+codProc+"&codTram="+codTram+"&eje="+listaCodTramFS+
        "&num="+listaDescTramFS+"&codClasifTram="+ob+"&nombreCodTram="+listaNombreTramFS,'ventana',
        'width=950,height=540,status='+ '<%=statusBar%>',function(datosConsulta){
            if(datosConsulta!=undefined){
                if(datosConsulta[0] =="si") {
                  document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value = datosConsulta[1];
                  document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value = datosConsulta[5];
                  document.forms[0].listaNumerosSecuenciaFlujoSalidaNoFavorable.value = datosConsulta[2];
                  document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value = datosConsulta[4];
                  document.forms[0].obligNoFavorable.value = datosConsulta[3];
                  document.forms[0].numeroCondicionSalidaNoFavorable.value = 2;
                }
            }
        });
    }
}

function flujoSalidaNoGrabado() {
  jsp_alerta('A', '<%=descriptor.getDescripcion("msjGrabListSelec")%>');
}

function verListaPlantillas(){
    if (document.forms[0].codClasifTramite.value.trim()!=''){
        var argumentos = new Array();
        var opcion = "listaPlantillas";
        var source = "<c:url value='/sge/DefinicionTramites.do?opcion='/>"+opcion
        + "&codClasifTram=" + document.forms[0].codClasifTramite.value+"&nCS="+document.forms[0].cod;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,argumentos,
	'width=450,height=330,scrollbars=no,status='+ '<%=statusBar%>',function(datosPlantilla){
                        if(datosPlantilla != undefined)
                            actualizarPlantilla(datosPlantilla);
                  });
    }
}

function actualizarPlantilla(datosPlantilla) {
  if (datosPlantilla!=undefined) {
    document.forms[0].plantilla.value=datosPlantilla[0];
    if(datosPlantilla[1] !=undefined) {
      document.forms[0].codPlantilla.value=datosPlantilla[1];
    } else {
      document.forms[0].codPlantilla.value="";
    }
  }
}

function activarBotonesListas() {
  if(document.forms[0].tipoCondicion[1].checked == true) {
    document.forms[0].cmdListaTramite.disabled = false;
  }
  if(document.forms[0].tipoFavorableSI[0].checked == true) {
    document.forms[0].cmdListaTramiteSiFavorable.disabled = false;
  }
  if(document.forms[0].tipoFavorableNO[0].checked == true) {
    document.forms[0].cmdListaTramiteNoFavorable.disabled = false;
  }
}

function pulsarModificar() { 
  activarFormulario();
  tipoCondicion();
  var nCSTramite = null;
  var nCSTramiteS = null;
  var nCSTramiteN = null;
  if(tipoCond == "Tramite") {
      nCSTramite = 0;
  } else {
    if(tipoCondSF == "TramiteSI") {
        nCSTramiteS = 1;
    }
    if(tipoCondNF == "TramiteNO") {
        nCSTramiteN = 2;
    }
  }

  activarBotonesPulsarModificar(); 
  var codMun = document.forms[0].codMunicipio.value;
  var codProc = document.forms[0].txtCodigo.value;
  var codTram = document.forms[0].codigoTramite.value;
  var codApl = document.forms[0].codAplicacion.value;
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/sge/TablasIntercambiadoras.do?opcion=cargarListasTramSal"
        +"&nCSTramite="+nCSTramite+"&nCSTramiteS="+nCSTramiteS+"&nCSTramiteN="+nCSTramiteN+"&codMun="+codMun
        +"&codProc="+codProc+"&codTram="+codTram+"&nombDoc="+codApl;
  document.forms[0].submit();
  domlay('capaNavegacion',0,0,0,' ');
  domlay('capaNavegacion',1,0,0,navegacionConsultaDesactivada());
  mostrarCapasBotones('capaBotones3');
  
  onClickUnidadesPlazo();

}

function comprobarPantallasTramitacionExterna(){
    var exito =true;
    var codPlugin = document.forms[0].codPluginPantallaTramitacionExterna.value;
    var descripcionPlugin = document.forms[0].descPluginPantallaTramitacionExterna.value;
    var codUrl = document.forms[0].codUrlPluginPantallaTramitacionExterna.value;
    var descripcionUrl = document.forms[0].descUrlPluginPantallaTramitacionExterna.value;
    if((codPlugin!="" && descripcionPlugin!="" && codUrl!="" && descripcionUrl!="") ||
        (codPlugin=="" && descripcionPlugin=="" && codUrl=="" && descripcionUrl=="")){
        exito =false;
    }else
        jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorPantTramExterna")%>');
    
    return exito;
}

function pulsarGrabarModificar() {
    
    var listaPlazoFecha = "";
    var listaCheckPlazoFecha = "";
    
    if (validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
        if (comprobarCondicionesSalida()) {
            if (comprobarPlazo() == 1) {
                if (comprobarListasSalidaLlenas()) {
                    if (comprobarUnidades()) {
                        if (document.forms[0].codVisibleUnidadInicio.value != "" && document.forms[0].codVisibleUnidadInicio.value != null)
                            onchangeCodUnidadInicio();

                        onchangeCodCargo();
                        limpiarCondicionesSalida();
                        actualizarTipoCondicion();
                        var vector = new Array(document.forms[0].txtCodigo, document.forms[0].txtDescripcion);
                        habilitarGeneral(vector);
                        borrarDatosTablaEntrada();

                        crearListasTablaEntrada();                       
                        crearListasDoc();
                        crearListasEnlaces();
                        
                        crearListasCampos();
                        crearListasAgrupaciones();
                        
                        //PARA EL CAMPO PLAZO Y EL PERIODO
                        for (i=0; i < listaCampos.length; i++) {
                             //PlazoFecha y CheckPlazoFecha
                            if(listaCamposOriginal[i][15] == "") {
                                 listaPlazoFecha += " "+'§¥';
                            } else {
                                listaPlazoFecha += listaCamposOriginal[i][15]+'§¥';
                            }
                            if(listaCamposOriginal[i][16] == "") {
                                 listaCheckPlazoFecha += " "+'§¥';
                            } else {
                                listaCheckPlazoFecha += listaCamposOriginal[i][16]+'§¥';
                            }
                       }//cierre for
                       document.forms[0].listaPlazoFecha.value = listaPlazoFecha;
                       document.forms[0].listaCheckPlazoFecha.value = listaCheckPlazoFecha; 
                        
                        crearListaUnidadesTramite();
                       if(comprobarNotificacionesCercaFinPlazo()){
                          return;
                       }
                        if(comprobarNotificacionesFueraDePlazo()){
                          return;
                       }
					  
                        document.forms[0].tipoUsuarioFirma.value=tipoUsuarioFirma;
                        document.forms[0].codigoOtroUsuarioFirma.value=codigoOtroUsuarioFirma;

                        if(comprobarCheckTipoNotifElectronica())
                          return;

                       if(comprobarPantallasTramitacionExterna())
                           return;

                       if(comprobarPluginPantallaTramitacionExterna()){
                           jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorCondTramExterna")%>');
                           return;
                       }
                       if(comprobarDestinatariosNofificacionFinPlazo()){	
                           jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorFinPlazoDestinatarios")%>');	
                           return;	
                       }	
                       if(comprobarNotificacionesFuncionPlazo()){	
                           jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorNotFuncionPlazo")%>');	
                           return;	
                       }
                       
                        if (document.forms[0].plazo.value=='')
                            document.forms[0].generarPlazos.checked = false;
                           
                        document.forms[0].opcion.value = "actualizar";
                        document.forms[0].target = "oculto";
                        document.forms[0].action = "<c:url value='/sge/DefinicionTramites.do'/>";
                        document.forms[0].submit();
                    }
                }
            }
        }
    }
}

function pulsarAlta() { 
    listaTablaEntrada = new Array();
    listaTablaEntradaOriginal = new Array();
    tab.lineas=listaTablaEntrada;
    refresca();
    borrarDatosTablaEntrada();
    
    listaDoc = new Array();
    listaDocOriginal = new Array();
    tabDoc.lineas=listaDoc;
    refrescaDoc();
    
    listaEnlaces = new Array();
    listaEnlacesOriginal = new Array();
    tabEnlaces.lineas=listaEnlaces;
    tabEnlaces.displayTabla();
    borrarDatosEnlaces();
    
    listaCampos = new Array();
    listaCamposOriginal = new Array();
    tabCampos.lineas=listaCampos;
    tabCampos.displayTabla();
    
    listaAgrupaciones = new Array();
    listaAgrupacionesOriginal = new Array();
    tabAgrupaciones.lineas = listaAgrupaciones;
    tabAgrupaciones.displayTabla();
    
    activarFormulario();
    borrarDatos();
    activarBotonesPulsarAlta();
    
    document.forms[0].radioUnidadInicio[0].checked= true;
    document.forms[0].codUnidadInicio.value = -99999;
    document.forms[0].codUnidadTramite[0].checked= true;
    
    document.forms[0].nombreTramiteI.value = "";
    document.forms[0].tipoCondicion[0].checked = true;
    limpiarCondicionesSalida();
    domlay('capaNavegacion',0,0,0,' ');
    domlay('capaNavegacion',1,0,0,navegacionConsultaDesactivada());
    mostrarCapasBotones('capaBotones4');
    if(document.getElementById('capaBotones4').style.display=='') {
        var vectorBotonesDoc = [document.forms[0].cmdAltaDoc,document.forms[0].cmdAdjuntarDoc,
            document.forms[0].cmdModificarDoc,document.forms[0].cmdEliminarDoc,document.forms[0].cmdActivarDoc];
        deshabilitarGeneral(vectorBotonesDoc);
    }
	if(comprobarCheckTipoNotifElectronica()){
		return;
	}

    limpiarPantallasTramitacionExterna();
    desactivarCombosWS();
    
    document.forms[0].opcion.value="actualizarComboCondEnt";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
    document.forms[0].submit();
}

function pulsarGrabarAlta() { 
 
  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
   if(comprobarCondicionesSalida()) {
    if(comprobarPlazo() ==1) {
     if(comprobarListasSalidaLlenas()) {
      if(comprobarUnidades()) {
           if (document.forms[0].codVisibleUnidadInicio.value!="" && document.forms[0].codVisibleUnidadInicio.value!=null)
               onchangeCodUnidadInicio();
          if (document.forms[0].cod_visible_cargo.value!="" && document.forms[0].cod_visible_cargo.value!=null) {
            onchangeCodCargo();
              }
           limpiarCondicionesSalida();
           actualizarTipoCondicion();
           var vector = new Array(document.forms[0].txtCodigo,document.forms[0].txtDescripcion);
           habilitarGeneral(vector);
           borrarDatosTablaEntrada();

           crearListasTablaEntrada();
           crearListasDoc();
           crearListasEnlaces();
           crearListasCampos();
           crearListasAgrupaciones();
           crearListaUnidadesTramite();
     
           if(comprobarNotificacionesCercaFinPlazo()){
              return;
           }
            if(comprobarNotificacionesFueraDePlazo()){
              return;
           }

           if(comprobarPantallasTramitacionExterna()){
              return;
           }

           if(comprobarPluginPantallaTramitacionExterna()){
               jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorCondTramExterna")%>');
               return;
           }
           
            if(comprobarDestinatariosNofificacionFinPlazo()){	
             jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorFinPlazoDestinatarios")%>');	
             return;	
           }	
           if(comprobarNotificacionesFuncionPlazo()){	
             jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorNotFuncionPlazo")%>');	
             return;	
           }
            if (document.forms[0].plazo.value=='') {

                document.forms[0].generarPlazos.checked = false;               
            }

           document.forms[0].opcion.value="insertar";
           document.forms[0].target="oculto";
           document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
           document.forms[0].submit();
      }
     }
    }
   }
  }
}

function comprobarListasSalidaLlenas() {
  if(document.forms[0].tipoCondicion[1].checked == true &&
     document.forms[0].listaCodTramitesFlujoSalida.value == "") {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjListTramVac")%>');
    return false;
  }
  if(document.forms[0].tipoFavorableSI[0].checked == true &&
     document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value == "") {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjListTramVac")%>');
    return false;
  }
  if(document.forms[0].tipoFavorableNO[0].checked == true &&
     document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value =="") {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjListTramVac")%>');
    return false;
  }
  return true;
}

function tablasNoLlenas() {
  jsp_alerta('A','<%=descriptor.getDescripcion("msjTabNoLlenas")%>');
}

function pulsarVista(){
    var nCS = "altaDesdeProcedimiento";
    
    crearListasAgrupaciones();
    crearListasCampos();
    
    var url = "<c:url value='/sge/DefinicionVista.do'/>";      
    var parametros = "&opcion=guardarDatosVistaSesion&nCS=" +nCS + 
            "&codCampo=" + cambiarSeparador(document.forms[0].listaCodCampos.value) + 
            "&descCampo=" + cambiarSeparador(document.forms[0].listaDescCampos.value) + 
            "&codTipoDato=" + cambiarSeparador(document.forms[0].listaCodTipoDato.value) + 
            "&tamano=" + cambiarSeparador(document.forms[0].listaTamano.value) + 
            "&agrupacionCampo=" + cambiarSeparador(document.forms[0].listaCodAgrupacion.value) + 
            "&campoActivo=" + cambiarSeparador(document.forms[0].listaActivo.value) +
            "&posX=" + cambiarSeparador(document.forms[0].listaPosicionesX.value) + 
            "&posY=" + cambiarSeparador(document.forms[0].listaPosicionesY.value) + 
            "&oculto=" + cambiarSeparador(document.forms[0].listaOcultos.value) +
            "&codAgrupaciones=" + cambiarSeparador(document.forms[0].listaCodAgrupaciones.value) + 
            "&descAgrupaciones=" + cambiarSeparador(document.forms[0].listaDescAgrupaciones.value) + 
            "&ordenAgrupaciones=" + cambiarSeparador(document.forms[0].listaOrdenAgrupaciones.value);
       
    var valor = guardarDatosComposicionVistaEnSesion(url, parametros);
    if(valor==0){
        var source = "<c:url value='/sge/DefinicionVista.do?opcion=inicio'/>&nCS=" + nCS;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=1100,height=800,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            for (i=0; i < datos.length; i++) {
                                if(datos[i] != undefined){
                                    var campoCoordenadas = new Array();
                                        campoCoordenadas = datos[i];

                                    for (x=0; x < listaCamposOriginal.length; x++) {
                                        var arrayCampo = new Array();
                                            arrayCampo = listaCamposOriginal[x];
                                        if(arrayCampo[0] == campoCoordenadas[0]){
                                            var nuevoCampo = new Array();
                                                nuevoCampo = [arrayCampo[0],arrayCampo[1],arrayCampo[2],arrayCampo[3],arrayCampo[4],arrayCampo[5],arrayCampo[6],
                                                            arrayCampo[7],arrayCampo[8],arrayCampo[9],arrayCampo[10],arrayCampo[11],arrayCampo[12],arrayCampo[13],
                                                            arrayCampo[14],arrayCampo[15],arrayCampo[16],arrayCampo[17],arrayCampo[18],arrayCampo[19],
                                                            campoCoordenadas[1],campoCoordenadas[2]];
                                            listaCamposOriginal[i] = nuevoCampo;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                });
   }else  
   if(valor==1)
        jsp_alerta("A",'<%=descriptor.getDescripcion("getiq_sinAgrup")%>');
   else
   if(valor==2) 
       jsp_alerta("A",'<%=descriptor.getDescripcion("getiq_sinCampos")%>');
   
}//pulsarVista

function pulsarAltaCampo() {
  crearListasAgrupaciones();
  var listaCodAgrupacionCampo = document.forms[0].listaCodAgrupaciones.value;
  var listaDescAgrupacionCampo = document.forms[0].listaDescAgrupaciones.value;
  var listaOrdenAgrupacionCampo = document.forms[0].listaOrdenAgrupaciones.value;
  var nCS = "altaDesdeTramite";
  var codMunicipio     = document.forms[0].codMunicipio.value;
  var codProcedimiento = document.forms[0].txtCodigo.value;
  var codTramite = document.forms[0].codigoTramite.value

  var source = "<c:url value='/sge/DefinicionCampo.do?opcion=inicio&nCS='/>"+nCS+"&codMunicipio="+codMunicipio
      +"&codProcedimiento="+codProcedimiento+"&codTramite="+codTramite+"&codAgrupaciones="+listaCodAgrupacionCampo+
        "&descAgrupaciones="+listaDescAgrupacionCampo+"&ordenAgrupaciones="+listaOrdenAgrupacionCampo;
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
        'width=860,height=620,status='+ '<%=statusBar%>',function(datos){
                    if(datos!=undefined){
                      var lista = new Array();
                      lista = [datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8],datos[9],datos[10],datos[11],datos[12],
                          datos[13],datos[14],datos[15],datos[16],datos[17],datos[18]];
                      altaCampo(lista);
                    }
            });
}

function pulsarModificarCampo() {
  var indice = tabCampos.selectedIndex;
  if(indice != -1) {
    var nCS = "modificarDesdeTramite";
    var codCampo = "";
    var descCampo = "";
    var codPlantilla = "";
    var codTipoDato = "";
    var tamano = "";
    var mascara = "";
    var ob = "";
    var orden = "";
    var descPlantilla = "";
    var descTipoDato = "";
    var rotulo = "";
    var visible = "";
    var activo = "";
    var oculto = "";
    var bloqueado="";
    var plazoFecha="";
    var checkPlazoFecha="";
    var validacion="";
    var operacion = "";
    var agrupacionCampo = "";
    listaCoordenadasXmod = "";
    listaCoordenadasYmod = "";
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var codProcedimiento = document.forms[0].txtCodigo.value;
    var codTramite = document.forms[0].codigoTramite.value
    if(listaCampos.length >0) {
      codCampo = listaCamposOriginal[indice][0];
      descCampo = listaCamposOriginal[indice][1];
      codPlantilla = listaCamposOriginal[indice][2];
      codTipoDato = listaCamposOriginal[indice][3];
      tamano = listaCamposOriginal[indice][4];
      mascara = listaCamposOriginal[indice][5];
      ob = listaCamposOriginal[indice][6];
      orden = listaCamposOriginal[indice][7];
      descPlantilla = listaCamposOriginal[indice][8];
      descTipoDato = listaCamposOriginal[indice][9];
      rotulo = listaCamposOriginal[indice][10];
      visible = listaCamposOriginal[indice][11];
      activo = listaCamposOriginal[indice][12];
      oculto = listaCamposOriginal[indice][13];
      bloqueado = listaCamposOriginal[indice][14];
      plazoFecha = listaCamposOriginal[indice][15];
      checkPlazoFecha = listaCamposOriginal[indice][16];
      validacion = listaCamposOriginal[indice][17];
      operacion= listaCamposOriginal[indice][18];
      agrupacionCampo = listaCamposOriginal[indice][19];
      listaCoordenadasXmod = listaCamposOriginal[indice][20];
      listaCoordenadasYmod = listaCamposOriginal[indice][21];
      if (validacion != null)
      {
        validacion = replaceAll(validacion,"&lt;","<");
        validacion = replaceAll(validacion,"&gt;",">");      
        validacion = replaceAll(validacion,"+",";SUMA;");                
      }
      if (operacion != null)
      {
        operacion =  replaceAll(operacion,"+",";SUMA;");      
        operacion = replaceAll(operacion," DIAS ",";DIAS;");
        operacion = replaceAll(operacion," MESES ",";MESES;");
        operacion = replaceAll(operacion," ANOS ",";ANOS;");
      }
    }
    crearListasAgrupaciones();
    var listaCodAgrupacionCampo = document.forms[0].listaCodAgrupaciones.value;
    var listaDescAgrupacionCampo = document.forms[0].listaDescAgrupaciones.value;
    var listaOrdenAgrupacionCampo = document.forms[0].listaOrdenAgrupaciones.value;
    var source = "<c:url value='/sge/DefinicionCampo.do?opcion=inicio&nCS='/>"+nCS+"&codMun="+codCampo+"&codProc="+
                 descCampo+"&codTram="+codPlantilla+"&eje="+codTipoDato+"&num="+tamano+
                 "&codClasifTram="+mascara+"&nombreCodTram="+ob+"&codPlantilla="+orden+
                 "&nombDoc="+descPlantilla+"&descTD="+descTipoDato+"&rotulo="+rotulo+"&visible="+visible+"&oculto="+oculto+"&bloqueado="
                 +bloqueado+"&plazoFecha="+plazoFecha+"&checkPlazoFecha="+checkPlazoFecha+"&validacion="+validacion+"&operacion="+operacion +
                 "&codMunicipio="+codMunicipio+"&codProcedimiento="+codProcedimiento+"&codTramite="+codTramite+
                 "&agrupacionCampo="+agrupacionCampo+"&codAgrupaciones="+listaCodAgrupacionCampo+"&descAgrupaciones="+listaDescAgrupacionCampo+
                 "&ordenAgrupaciones="+listaOrdenAgrupacionCampo;;
    
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=860,height=620,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          var lista = new Array();
                          lista = [datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8],datos[9],datos[10],datos[11],datos[12],
                              datos[13],datos[14],datos[15],datos[16],datos[17],datos[18],listaCoordenadasXmod,listaCoordenadasYmod];
                          modificarCampo(lista);
                        }
                });
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function verCampo(i) {
  if(document.forms[0].cmdAltaCampo.disabled == true) {
	  if(i != -1) {
	    var nCS = "modificarDesdeTramite";
	    var codCampo = "";
	    var descCampo = "";
	    var codPlantilla = "";
	    var codTipoDato = "";
	    var tamano = "";
	    var mascara = "";
	    var ob = "";
	    var orden = "";
	    var descPlantilla = "";
	    var descTipoDato = "";
	    var rotulo = "";
	    var visible = "";
            var lectura = "si";
            var validacion ="";
            var operacion ="";
            if(listaCampos.length >0) {
                codCampo = listaCamposOriginal[i][0];
                descCampo = listaCamposOriginal[i][1];
                codPlantilla = listaCamposOriginal[i][2];
                codTipoDato = listaCamposOriginal[i][3];
                tamano = listaCamposOriginal[i][4];
                mascara = listaCamposOriginal[i][5];
                ob = listaCamposOriginal[i][6];
                orden = listaCamposOriginal[i][7];
                descPlantilla = listaCamposOriginal[i][8];
                descTipoDato = listaCamposOriginal[i][9];
                rotulo = listaCamposOriginal[i][10];
                visible = listaCamposOriginal[i][11];
                oculto = listaCamposOriginal[indice][13];
                bloqueado = listaCamposOriginal[indice][14];
                plazoFecha = listaCamposOriginal[indice][15];
                checkPlazoFecha = listaCamposOriginal[indice][16];
                validacion = listaCamposOriginal[i][17];
                operacion =  listaCamposOriginal[i][18];       
          if (validacion != null){
            validacion = replaceAll(validacion,"&lt;","<");
            validacion = replaceAll(validacion,"&gt;",">");      
            validacion = replaceAll(validacion,"+",";SUMA;");                
          }
          if(operacion != null) {
            operacion =  replaceAll(operacion,"+",";SUMA;"); 
            operacion = replaceAll(operacion," DIAS ",";DIAS;");
            operacion = replaceAll(operacion," MESES ",";MESES;");
            operacion = replaceAll(operacion," ANOS ",";ANOS;");
          }
        }
        var source = "<c:url value='/sge/DefinicionCampo.do?opcion=inicio&nCS='/>"+nCS+"&codMun="+codCampo+"&codProc="+
         descCampo+"&codTram="+codPlantilla+"&eje="+codTipoDato+"&num="+tamano+
         "&codClasifTram="+mascara+"&nombreCodTram="+ob+"&codPlantilla="+orden+
         "&nombDoc="+descPlantilla+"&descTD="+descTipoDato+"&rotulo="+rotulo+"&visible="+visible+
         "&lectura="+lectura+"&oculto="+oculto+"&bloqueado="+bloqueado+"&plazoFecha="+plazoFecha+"&checkPlazoFecha="+checkPlazoFecha+"&bloqueado="+bloqueado+"&validacion="+validacion+"&operacion="+operacion;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=850,height=330,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          var lista = new Array();
                          lista = [datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8],datos[9],datos[10],datos[11],datos[12],datos[13],datos[14],datos[15],,datos[16],datos[17]];
                          modificarCampo(lista);
                        }
                });
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

function pulsarEliminarCampo() {
    var indice = tabCampos.selectedIndex;
    if(indice != -1) {
      var codCampo = "";
      var descCampo = "";
      var codPlantilla = "";
      var codTipoDato = "";
      var tamano = "";
      var mascara = "";
      var ob = "";
      var orden = "";
      var descPlantilla = "";
      var descTipoDato = "";
      var rotulo = "";
      var visible = "";
      var activo = "";
      var validacion = "";
      var operacion = "";
      var codAgrupacion = "";
      var listaCoordenadasX = "";
      var listaCoordenadasY = "";
      if(listaCampos.length >0) {
        codCampo = listaCamposOriginal[indice][0];
        descCampo = listaCamposOriginal[indice][1];
        codPlantilla = listaCamposOriginal[indice][2];
        codTipoDato = listaCamposOriginal[indice][3];
        tamano = listaCamposOriginal[indice][4];
        mascara = listaCamposOriginal[indice][5];
        ob = listaCamposOriginal[indice][6];
        orden = listaCamposOriginal[indice][7];
        descPlantilla = listaCamposOriginal[indice][8];
        descTipoDato = listaCamposOriginal[indice][9];
        rotulo = listaCamposOriginal[indice][10];
        visible = listaCamposOriginal[indice][11];
        activo = listaCamposOriginal[indice][12];
        oculto = listaCamposOriginal[indice][13];
        bloqueado = listaCamposOriginal[indice][14];
        plazoFecha = listaCamposOriginal[indice][15];
        checkPlazoFecha = listaCamposOriginal[indice][16];
        validacion  = listaCamposOriginal[indice][17];
        operacion = listaCamposOriginal[indice][18];
        codAgrupacion = listaCamposOriginal[indice][19];
        listaCoordenadasX = listaCamposOriginal[indice][20];
        listaCoordenadasY = listaCamposOriginal[indice][21];
      }
        var lista = new Array();
        lista = [codCampo,descCampo,codPlantilla,codTipoDato,tamano,mascara,ob,orden,descPlantilla,descTipoDato,rotulo,visible,
            oculto,bloqueado,plazoFecha,checkPlazoFecha,validacion,operacion,codAgrupacion, listaCoordenadasX, listaCoordenadasY];
        eliminarCampo(lista);
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');

    }

function eliminarCampo(lista) {
    var codigo = lista[0];
    var yaExiste = 0;
    var ord = 0;
    if(tabCampos.selectedIndex != -1) {
      for(l=0; l < listaCampos.length; l++){
        var lineaSeleccionada;
        lineaSeleccionada = tabCampos.selectedIndex;
        if(l == lineaSeleccionada) {
          l= l;
        } else {
          if ((listaCampos[l][0]) == codigo ){
            yaExiste = 1;
          }
        }
      }
      if(yaExiste == 0) {
        var j = tabCampos.selectedIndex;
        ord = listaCamposOriginal[j][7];

        listaCampos[j]=[lista[0],lista[1],'<span class="fa fa-close 2x"></span>'];
        listaCamposOriginal[j]=[lista[0],lista[1],lista[2],lista[3],lista[4],lista[5],lista[6],ord,lista[8],lista[9],lista[10],
            lista[11],'NO',lista[12],lista[13],lista[14],lista[15],lista[16],lista[17],lista[18],lista[19],lista[20]];

    tabCampos.lineas=listaCampos;
    refrescaCampos();

      } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
      }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');

}

function pulsarOrdenarCampo(){
  if(listaCamposOriginal.length>0){
    revisarNumeroOrden();
    var source = "<c:url value='/jsp/sge/ordenarCampos.jsp?posicionOrde=7'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,listaCamposOriginal,
	'width=670,height=500,scrollbars=no,status='+ '<%=statusBar%>',function(datosOrdenados){
                        if(datosOrdenados != undefined) {
                          listaCamposOriginal = datosOrdenados;
                          listaCampos = new Array();
                          for(m=0; m < listaCamposOriginal.length;m++) {
                              if (listaCamposOriginal[m][12] == 'SI') {
                                    imagen='<span class="fa fa-check 2x"></span>';
                              } else {
                                    imagen='<span class="fa fa-close 2x"></span>';
                              }
                              listaCampos[m] = [listaCamposOriginal[m][0],listaCamposOriginal[m][1],imagen];
                          }
                          tabCampos.lineas=listaCampos;
                          refrescaCampos();
                        }
                });
  }
}


function modificarCampo(lista) {
  var codigo = lista[0];
  var yaExiste = 0;
  var ord = 0;
  if(tabCampos.selectedIndex != -1) {
    for(l=0; l < listaCampos.length; l++){
      var lineaSeleccionada;
      lineaSeleccionada = tabCampos.selectedIndex;
      if(l == lineaSeleccionada) {
        l= l;
      } else {
        if ((listaCampos[l][0]) == codigo ){
          yaExiste = 1;
        }
      }
    }
    if(yaExiste == 0) {
      var j = tabCampos.selectedIndex;
      ord = listaCamposOriginal[j][7];
      listaCampos[j]=[lista[0],lista[1],'<span class="fa fa-check 2x"></span>'];
      listaCamposOriginal[j]=[lista[0],lista[1],lista[2],lista[3],lista[4],lista[5],lista[6],ord,lista[8],lista[9],lista[10],lista[11],'SI',
          lista[12],lista[13],lista[14],lista[15],lista[16],lista[17],lista[18],lista[19],lista[20]];      

      tabCampos.lineas=listaCampos;
      refrescaCampos();
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
    }
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function altaCampo(lista) {
  var codigo = lista[0];
  var yaExiste = 0;
  var ord = (0 * 1); 
  var ordenNuevo = 0;
  for(l=0; l < listaCampos.length; l++){
    if ((listaCampos[l][0]) == codigo){
      yaExiste = 1;
    }
    ordenNuevo = (listaCamposOriginal[l][7] * 1); 
    if(ord < ordenNuevo) {
        ord = ordenNuevo;
    }
  }
  if(yaExiste == 0) {
    var lineas = tabCampos.lineas;
    for (i=0; i < lineas.length; i++) {
    }
    listaCampos[i]=[lista[0],lista[1],'<span class="fa fa-check 2x"></span>'];
    listaCamposOriginal[i]=[lista[0],lista[1],lista[2],lista[3],lista[4],lista[5],lista[6],((ord*1)+1),lista[8],lista[9],lista[10],
        lista[11],'SI',lista[12],lista[13],lista[14],lista[15],lista[16],lista[17],lista[18]];
    
    tabCampos.lineas=listaCampos;
    refrescaCampos();
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
  }
}

function pulsarAltaAgrupacion() {
  var nCS = "altaDesdeProcedimiento";
  var source = "<c:url value='/sge/DefinicionAgrupacionCampo.do?opcion=inicio&nCS='/>"+nCS;
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=850,height=380,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            var lista = new Array();
                              lista = [datos[0],datos[1],datos[2],'SI'];
                              if(comprobarCodigosAgrupacion(datos[0])){
                                  if(comprobarOrdenRepetido(datos[2])){
                                      if (jsp_alerta('C', '<%=descriptor.getDescripcion("getiq_reordenargrupo")%>') == 1){
                                          altaAgrupacionOrdenada(lista);
                                      }
                                  }else{
                                      altaAgrupacion(lista);
                                  }//if(comprobarOrdenRepetido(datos[2]))
                              }else{
                                  jsp_alerta('A', '<%=descriptor.getDescripcion("getiq_existegrupo")%>');
                              }//if(comprobarCodigosAgrupacion(datos[0]))
                        }//if(datos!=undefined)
                });
}//pulsarAltaAgrupacion

function altaAgrupacionOrdenada(lista){
    var lineas = tabAgrupaciones.lineas;
    var auxListaAgrupaciones = new Array();
    var auxListaAgrupacionesOriginal = new Array();
    var reordenar = false;
    for (i=0; i < lineas.length; i++) {
        if(i+1 == lista[2]){
            auxListaAgrupaciones[i] = [lista[0], lista[1], i+1,'<span class="fa fa-check 2x"></span>'];
            auxListaAgrupacionesOriginal[i] = [lista[0], lista[1], i+1,lista[3]];
            
            auxListaAgrupaciones[i+1] = [listaAgrupaciones[i][0], listaAgrupaciones[i][1], i+2, listaAgrupaciones[i][3]];
            auxListaAgrupacionesOriginal[i+1] = [listaAgrupacionesOriginal[i][0], listaAgrupacionesOriginal[i][1], i+2, listaAgrupacionesOriginal[i][3]];
            reordenar = true;
        }else{
            if(reordenar == false){
                auxListaAgrupaciones[i] = [listaAgrupaciones[i][0], listaAgrupaciones[i][1], i+1, listaAgrupaciones[i][3]];
                auxListaAgrupacionesOriginal[i] = [listaAgrupacionesOriginal[i][0], listaAgrupacionesOriginal[i][1], i+1, listaAgrupacionesOriginal[i][3]];
            }else{
                auxListaAgrupaciones[i+1] = [listaAgrupaciones[i][0], listaAgrupaciones[i][1], i+2, listaAgrupaciones[i][3]];
                auxListaAgrupacionesOriginal[i+1] = [listaAgrupacionesOriginal[i][0], listaAgrupacionesOriginal[i][1], i+2, listaAgrupacionesOriginal[i][3]];
            }//if(reordenar == false)
        }//if(i+1 == lista[2])
    }//for (i=0; i < lineas.length; i++)
    listaAgrupaciones = auxListaAgrupaciones;
    listaAgrupacionesOriginal = auxListaAgrupacionesOriginal;
    tabAgrupaciones.lineas = listaAgrupaciones;
    refrescaAgrupaciones();
}//altaAgrupacionOrdenada

function altaAgrupacion(lista){
    var lineas = tabAgrupaciones.lineas;
    for (i=0; i < lineas.length; i++) {
    }
    listaAgrupaciones[i] = [lista[0], lista[1], lista[2],'<span class="fa fa-check 2x"></span>'];
    listaAgrupacionesOriginal[i] = [lista[0], lista[1], lista[2],lista[3]];
    tabAgrupaciones.lineas = listaAgrupaciones;
    refrescaAgrupaciones();
}//altaAgrupacion

function comprobarCodigosAgrupacion(nuevoCod){
    var retorno = true;
    for (i=0; i < listaAgrupacionesOriginal.length; i++) {
        var cod = listaAgrupacionesOriginal[i][0];
        if(cod == nuevoCod){
            retorno = false;
            break;
        }//if(cod == nuevoCod)
    }//for (i=0; i < listaAgrupacionesOriginal.length; i++) 
    return retorno;
}//comprobarCodigosAgrupacion

function comprobarOrdenRepetido(ordenNuevaAgrupacion){
    var ordenRepetido = false;
    for (i=0; i < listaAgrupacionesOriginal.length; i++) {
        var orden = listaAgrupacionesOriginal[i][2];
        if(orden == ordenNuevaAgrupacion){
            ordenRepetido = true;
        }//if(orden == ordenNuevaAgrupacion)
    }//for (i=0; i < listaAgrupacionesOriginal.length; i++) 
    return ordenRepetido;
}//comprobarOrdenRepetido

function pulsarModificarAgrupacion() {
    var indice = tabAgrupaciones.selectedIndex;
    if(indice != -1) {
        var nCS = "modificarDesdeProcedimiento";
        var codAgrupacion = "";
        var descAgrupacion = "";
        var ordenAgrupacion = "";
        if(listaAgrupaciones.length >0) {
            codAgrupacion = listaAgrupacionesOriginal[indice][0];
            descAgrupacion = listaAgrupacionesOriginal[indice][1];
            ordenAgrupacion = listaAgrupacionesOriginal[indice][2];
        }//if(listaAgrupaciones.length >0) 
        var source = "<c:url value='/sge/DefinicionAgrupacionCampo.do?opcion=inicio&nCS='/>"+ nCS + "&codAgrupacion=" + codAgrupacion 
            + "&descAgrupacion="+descAgrupacion+"&ordenAgrupacion="+ordenAgrupacion;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=850,height=190,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            var lista = new Array();
                            lista = [datos[0], datos[1], datos[2],'SI'];
                            if(comprobarOrdenRepetido(datos[2])){
                                if (jsp_alerta('C', '<%=descriptor.getDescripcion("getiq_reordenargrupo")%>') == 1){
                                    modificacionAgrupacionOrdenada(lista);
                                }
                            }else{
                               modificacionAgrupacionOrdenada(lista);
                            }//if(comprobarOrdenRepetido(datos[2]))
                        }//if(datos!=undefined)
                });
    }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}//pulsarModificarAgrupacion

function modificacionAgrupacionOrdenada(lista){
    var lineas = tabAgrupaciones.lineas;
    var auxListaAgrupaciones = new Array();
    var auxListaAgrupacionesOriginal = new Array();
    
   tabAgrupaciones.lineas = new Array();
   refrescaAgrupaciones();
   var orden = 1;
   if(lineas.length == (parseInt(lista[2]))){
        for (i=0; i < listaAgrupaciones.length; i++) {
            if(listaAgrupaciones[i][0] != lista[0]){
                var agrupacion = new Array();
                var agrupacionOriginal = new Array();

                agrupacion = listaAgrupaciones[i];
                agrupacionOriginal = listaAgrupacionesOriginal[i];

                agrupacion[2] = orden;
                agrupacionOriginal[2] = orden;
                orden++;
                auxListaAgrupaciones.push(agrupacion);
                auxListaAgrupacionesOriginal.push(agrupacionOriginal);
            }//if(listaAgrupaciones[i][0] != lista[0])
        }//for (i=0; i < listaAgrupaciones.length; i++)
        listaAgrupaciones = auxListaAgrupaciones;
        listaAgrupacionesOriginal = auxListaAgrupacionesOriginal;
   
        tabAgrupaciones.lineas = listaAgrupaciones;
        refrescaAgrupaciones();
        altaAgrupacion(lista);
   }else{
        for (i=0; i < listaAgrupaciones.length; i++) {
            if(listaAgrupaciones[i][0] != lista[0]){
                auxListaAgrupaciones.push(listaAgrupaciones[i]);
                auxListaAgrupacionesOriginal.push(listaAgrupacionesOriginal[i]);
            }//if(listaAgrupaciones[i][0] != lista[0])
        }//for (i=0; i < listaAgrupaciones.length; i++) 
   
        listaAgrupaciones = auxListaAgrupaciones;
        listaAgrupacionesOriginal = auxListaAgrupacionesOriginal;
   
        tabAgrupaciones.lineas = listaAgrupaciones;
        refrescaAgrupaciones();
        altaAgrupacionOrdenada(lista);
   }//if(tabAgrupaciones.lineas < lista[2])
   
}//modificacionAgrupacionOrdenada

function modificarAgrupacionCampos(lista){
    var codigo = lista[0];
    var yaExiste = 0;
    if(tabAgrupaciones.selectedIndex != -1) {
        for(l=0; l < listaAgrupaciones.length; l++){
            var lineaSeleccionada;
            lineaSeleccionada = tabAgrupaciones.selectedIndex;
            if(l == lineaSeleccionada){
                l= l;
            }else{
                if ((listaAgrupaciones[l][0]) == codigo ){
                    yaExiste = 1;
                }//if ((listaCampos[l][0]) == codigo )
            }//if(l == lineaSeleccionada)
        }//for(l=0; l < listaAgrupaciones.length; l++)
        if(yaExiste == 0) {
            var j = tabAgrupaciones.selectedIndex;
            listaAgrupacionesOriginal[j] = [lista[0],lista[1],lista[2],lista[3]];
            if(lista[3] == "SI"){
                listaAgrupaciones[j] = [lista[0],lista[1],lista[2],'<span class="fa fa-check 2x"></span>'];
            }else{
                listaAgrupaciones[j] = [lista[0],lista[1],lista[2],'<span class="fa fa-close 2x"></span>'];
            }//if(lista[3] == "SI")
            tabAgrupaciones.lineas = listaAgrupaciones;
            refrescaAgrupaciones();
        }else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
        }//if(yaExiste == 0) 
    }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}//modificarAgrupacionCampos

function pulsarEliminarAgrupacion(){
    var indice = tabAgrupaciones.selectedIndex;
    if(indice != -1) {
        var codAgrupacion = "";
        var descAgrupacion = "";
        var ordenAgrupacion = "";
        if(listaAgrupaciones.length >0) {
            codAgrupacion = listaAgrupacionesOriginal[indice][0];
            descAgrupacion = listaAgrupacionesOriginal[indice][1];
            ordenAgrupacion = listaAgrupacionesOriginal[indice][2];
        }//if(listaAgrupaciones.length >0) 
        var lista = new Array();
        lista = [codAgrupacion,descAgrupacion,ordenAgrupacion,'NO'];
        modificarAgrupacionCampos(lista);
    }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}//pulsarEliminarAgrupacion

function onClickHrefUnidadInicio() {
    if (document.forms[0].radioUnidadInicio[3].checked == true){
    var datos;
    var argumentos = new Array();
    argumentos[0] = document.forms[0].codVisibleUnidadInicio.value;
    var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalUOR" +
                    "&codOrganizacion=" + <%=codOrg%> + "&codEntidad=" + <%=codEnt%>;

    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=750,height=600',function(datos){
                        if(datos != null) {
                            document.forms[0].codUnidadInicio.value = datos[2];
                            document.forms[0].descUnidadInicio.value = datos[1];
                            document.forms[0].codVisibleUnidadInicio.value = datos[0];
                        }
                        if((document.forms[0].codVisibleUnidadInicio.value != '') && (document.forms[0].descUnidadInicio.value == '')) {
                            document.forms[0].codUnidadInicio.value = '';
                            document.forms[0].codVisibleUnidadInicio.value = '';
                        }
                });
}
}

function seleccionUnidadTramite(codUnidadTramite) {
    if(codUnidadTramite == <%=ConstantesDatos.TRA_UTR_EXPEDIENTE%>) {
        document.forms[0].codUnidadTramite[0].checked=true;
    } else if(codUnidadTramite == <%=ConstantesDatos.TRA_UTR_INICIA%>) {
        document.forms[0].codUnidadTramite[1].checked=true;
    } else if(codUnidadTramite == <%=ConstantesDatos.TRA_UTR_ANTERIOR%>) {
        document.forms[0].codUnidadTramite[2].checked=true;
    } else if(codUnidadTramite == <%=ConstantesDatos.TRA_UTR_CUALQUIERA%>) {
        document.forms[0].codUnidadTramite[3].checked=true;
    } else {
        document.forms[0].codUnidadTramite[4].checked=true;
    }
}

function pulsarListadoUtrs() {
    var argumentos = new Array();
    argumentos['uordtos'] = uors;
    argumentos['utrs'] = listaUnidadesTramitadoras;
    var source = "<html:rewrite page='/jsp/sge/listadoUTRs.jsp?dummy='/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,argumentos,
	'width=900,height=400,status='+ '<%=statusBar%>',function(datos){
                        if (datos != undefined) {
                             listaUnidadesTramitadoras = datos;
                            // Recargar tabla
                            mostrarListaUnidadTramite();
                        }
                  });
}

function crearListaUnidadesTramite() {
	  txtListaUors = "";
	  for (i=0; i<listaUnidadesTramitadoras.length; i++) {
	        txtListaUors += listaUnidadesTramitadoras[i] + '§¥';
	  } 
	  document.forms[0].txtUnidadesTramitadoras.value = txtListaUors;
}

function onClickRadioUnidadInicio() {
    if (document.forms[0].radioUnidadInicio[0].checked == true) {
        document.forms[0].codUnidadInicio.value = -99999;
        document.forms[0].descUnidadInicio.value = "";
        document.forms[0].codVisibleUnidadInicio.value = "";
    } else if (document.forms[0].radioUnidadInicio[1].checked == true) {
        document.forms[0].codUnidadInicio.value = -99998;
        document.forms[0].descUnidadInicio.value = "";
        document.forms[0].codVisibleUnidadInicio.value = "";
    } else if (document.forms[0].radioUnidadInicio[2].checked == true) {
        document.forms[0].codUnidadInicio.value = "";
        document.forms[0].descUnidadInicio.value = "";
        document.forms[0].codVisibleUnidadInicio.value = "";
    }
    if (document.forms[0].radioUnidadInicio[3].checked == true) {
        var comboUnidadInicio = [document.forms[0].codVisibleUnidadInicio,document.forms[0].descUnidadInicio];
        deshabilitarImagenBoton("botonUnidadInicio",false);
        habilitarGeneral(comboUnidadInicio);
    } else {
        var comboUnidadInicio = [document.forms[0].codVisibleUnidadInicio,document.forms[0].descUnidadInicio];
        deshabilitarImagenBoton("botonUnidadInicio",true);
        deshabilitarGeneral(comboUnidadInicio);
    }
}

function onClickRadioUnidadTramite() {
    // Para cualquier valor que no sea 'Otras' se borra la lista de UTRs y se 
    // esconde el icono de la lupa
    if (document.forms[0].codUnidadTramite[0].checked == true ||
        document.forms[0].codUnidadTramite[1].checked == true ||
        document.forms[0].codUnidadTramite[2].checked == true ||
        document.forms[0].codUnidadTramite[3].checked == true) {
        
        listaUnidadesTramitadoras = new Array();
        mostrarListaUnidadTramite();
        document.getElementById("botonListadoUtrs").style.visibility = "hidden";
    // Otras
    } else {
        document.getElementById("botonListadoUtrs").style.visibility = "visible";    
    }
    

    // No permitir 'La que lo inicia' si es tramite de inicio
    if (document.forms[0].codUnidadTramite[1].checked == true) {
        if (document.forms[0].tramiteInicio.checked == true) {
    	     jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoOpcionTramiteInicio")%>');
    	     document.forms[0].codUnidadTramite[0].checked = true;
        }
    }
    // No permitir 'La del tramite anterior' si es tramite de inicio
    if (document.forms[0].codUnidadTramite[2].checked == true) {
        if (document.forms[0].tramiteInicio.checked == true) {
    	     jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoTramiteAnterior")%>');
    	     document.forms[0].codUnidadTramite[0].checked = true;
        }
    }
}

// Esta funcion impide definir el tramite como un tramite de inicio cuando
// esta marcado unidad de tramitacion 'La del tramite anterior'
function deshabilitarTramiteAnterior(){
    if (document.forms[0].codUnidadTramite[2].checked == true &&
        document.forms[0].tramiteInicio.checked == true) {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoTramiteAnterior")%>');
        document.forms[0].tramiteInicio.checked = false;
    }
}

function onchangeCodCargo() {
    //if (!contieneOperadoresConsulta(document.forms[0].codUnidadesOrganicas,operadorConsulta)) {
        var cargo = buscarUorPorCodVisibleEstado(cargos, document.forms[0].cod_visible_cargo.value, 'A');
        if(cargo != null) {
            document.forms[0].codCargo.value = cargo.uor_cod;
            document.forms[0].descCargo.value = cargo.uor_nom;
        }
        else { // ha dado null para alta, buscamos de baja
            cargo = buscarUorPorCodVisibleEstado(cargos, document.forms[0].cod_visible_cargo.value, 'B');
            if(cargo != null) {
                document.forms[0].codCargo.value = cargo.uor_cod;
                document.forms[0].descCargo.value = cargo.uor_nom;
            }
        }
        if(cargo == null) {
            document.forms[0].codCargo.value = '';
            document.forms[0].descCargo.value = '';
            document.forms[0].cod_visible_cargo.value = '';
        }
    //}
}

function onClickHrefCargo() {
    var argumentos = new Array();
    argumentos[0] = document.forms[0].cod_visible_cargo.value;
    var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalCargo" +
                    "&codOrganizacion=" + <%=codOrg%> + "&codEntidad=" + <%=codEnt%>;

    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=750,height=450',function(datos){
                        if(datos != null) {
                            document.forms[0].codCargo.value = datos[2];
                            document.forms[0].descCargo.value = datos[1];
                            document.forms[0].cod_visible_cargo.value = datos[0];
                        }
                        if((document.forms[0].cod_visible_cargo.value != '') && (document.forms[0].descCargo.value == '')) {
                            document.forms[0].codCargo.value = '';
                            document.forms[0].cod_visible_cargo.value = '';
                        }
                });
}


function pulsarConfigurarSW(avanzar) {

    var source = "<c:url value='/sge/ConfiguracionSWTramite.do?opcion=inicioConfiguracion'/>";	
    var cfo = -1;
	var codigoOperacion = -1;
    
    if (avanzar=='avanzar') {
    	cfo = document.forms[0].cfoAvanzar.value;
		codigoOperacion = document.forms[0].codAvanzarSW.value;
    }else
    if (avanzar=='retroceder') {
    	cfo = document.forms[0].cfoRetroceder.value;
		codigoOperacion = document.forms[0].codRetrocederSW.value;
    }else
    if (avanzar=='iniciar') {
    	cfo = document.forms[0].cfoIniciar.value;
		codigoOperacion = document.forms[0].codIniciarSW.value;
    }

	// Se busca el código de la operación para comprobar si es de un web service o de un módulo de integración, ya que
	// estos últimos no se configuran
	var indice = -1;    
	for(i=0;cod_webServices!=null && i<cod_webServices.length;i++){
		if(cod_webServices[i] ==codigoOperacion){
			indice=i;
			break;			
		}		
	}//for

    // SE COMPRUEBA CUAL ES EL TIPO DE ORIGEN DE LA OPERACION
    var tipoOrigen="";
	var alto;
	var ancho;

	if(indice!=-1){
		tipoOrigen  = tipoOrigenOperaciones[indice];
		alto        =  alturaPantallas[indice];
		ancho       = anchuraPantallas[indice];
		urlPantalla = urlPantallas[indice];
	}
  
	source += '&codSW=' + cfo;
    var datos = "";
    if (cfo==-1){
        jsp_alerta('A','<%=descriptor.getDescripcion("msgSelecOperacionWS")%>');
    }else {
        if(tipoOrigen=="WS")
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source=' + source, null,
	'width=650,height=580,status='+ '<%=statusBar%>',function(datos){});
        else{
            // Se establecen valores por defecto para el alto y ancho de la pantalla
            if(ancho=="" || alto==""){
                    ancho = "650";
                    alto  = "490";
            }
            if(urlPantalla==""){
                    jsp_alerta('A','<%=descriptor.getDescripcion("msgNoConfigOperacionModulo")%>');
            }else{
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>' + urlPantalla, null,
                            'width=' + ancho + ',height=' + alto + ',status='+ '<%=statusBar%>',function(datos){});
            }
        } 
    }
}

function pulsarAltaConfSW() { 

    document.getElementById('maxOrdEjec').value= howManySW;;
    document.forms[0].opcion.value = "altaSW";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/sge/DefinicionTramites.do'/>";

    var codAvanzarSW    = document.forms[0].codAvanzarSW.value;
    var codRetrocederSW = document.forms[0].codRetrocederSW.value;
    var codIniciarSW    = document.forms[0].codIniciarSW.value;
		
	if(codAvanzarSW!=null && codAvanzarSW!=-1 && codAvanzarSW!="" && codIniciarSW!=null && codIniciarSW!=-1 && codIniciarSW!=""){
		jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrExistirIniciarAvanzar")%>');
	}else{
        var esTramiteInicio = document.forms[0].tramiteInicio.checked;
        
        if(esTramiteInicio && codRetrocederSW!=-1){
			jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrTramInicioOpRetroceder")%>');
		}else{
                        
                        var tipoRetroceso = document.forms[0].codTipoRetrocesoSW.value;
                        
                        if((codIniciarSW!=-1 || codAvanzarSW!=-1 ||  codRetrocederSW!=-1) && codRetrocederSW!=null && codRetrocederSW!=-1 && (tipoRetroceso==null || tipoRetroceso=="" || tipoRetroceso==-1)){
                            jsp_alerta("A","<%=descriptor.getDescripcion("msgTipoRetrocesoOblig")%>");
                            
                        }else{
                        
                            var origenIniciar= "";
                            var nombreOperacionIniciar = "";
                            var nombreModuloIniciar   = "";
                            if(codIniciarSW!=null && codIniciarSW!=-1){
                                    for(i=0;i<cod_webServices.length;i++){
                                            if(cod_webServices[i]==codIniciarSW){
                                                    break;
                                            }
                                    }
                                    origenIniciar = tipoOrigenOperaciones[i];
                                    nombreOperacionIniciar = tituloOperaciones[i];
                                    nombreModuloIniciar    = nombreModulos[i];
                            }

                            var origenAvanzar = "";
                            var nombreOperacionAvanzar = "";
                            var nombreModuloAvanzar    = "";
                            if(codAvanzarSW!=null && codAvanzarSW!=-1){
                                    for(i=0;i<cod_webServices.length;i++){
                                            if(cod_webServices[i]==codAvanzarSW){
                                                    break;
                                            }
                                    }
                                    origenAvanzar = tipoOrigenOperaciones[i];
                                    nombreOperacionAvanzar = tituloOperaciones[i];
                                    nombreModuloAvanzar    = nombreModulos[i];
                            }

                            var origenRetroceder 		  = "";
                            var nombreOperacionRetroceder = "";
                            var nombreModuloRetroceder    = "";
                            if(codRetrocederSW!=null && codRetrocederSW!=-1){

                                    for(i=0;i<cod_webServices.length;i++){
                                            if(cod_webServices[i]==codRetrocederSW){
                                                    break;
                                            }
                                    }
                                    origenRetroceder 		  = tipoOrigenOperaciones[i];
                                    nombreOperacionRetroceder = tituloOperaciones[i];
                                    nombreModuloRetroceder    = nombreModulos[i];
                            }

                            document.forms[0].tituloOperacionAvanzar.value    = nombreOperacionAvanzar;
                            document.forms[0].tituloOperacionRetroceder.value = nombreOperacionRetroceder;
                            document.forms[0].tipoOrigenAvanzar.value= origenAvanzar;
                            document.forms[0].tipoOrigenRetroceder.value      = origenRetroceder;
                            document.forms[0].nombreModuloAvanzar.value         = nombreModuloAvanzar;
                            document.forms[0].nombreModuloRetroceder.value      = nombreModuloRetroceder;

                            document.forms[0].tipoOrigenIniciar.value			= origenIniciar;
                            document.forms[0].tituloOperacionIniciar.value	    = nombreOperacionIniciar;
                            document.forms[0].nombreModuloIniciar.value			= nombreModuloIniciar;


                            document.forms[0].submit();
                            limpiarCombosWS();
                   }// else
		}
    }
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

function tieneTramiteTareasPendientes(){
    var exito = false;

	var ajax = getXMLHttpRequest();

	if(ajax!=null){

        document.forms[0].codOrdenEjec.value = tabConfSW.selectedIndex;
		var url = "<%=request.getContextPath()%>/sge/DefinicionTramites.do";
        var parametros = "&opcion=comprobarExistenciaTareasPendientesInicio&orden=" + document.forms[0].codOrdenEjec.value;		        
		ajax.open("POST",url,false);
		ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
		ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
		ajax.send(parametros);

		try{            
			if (ajax.readyState==4 && ajax.status==200){
				var xmlDoc = null;
				if(navigator.appName.indexOf("Internet Explorer")!=-1){
					// En IE el XML viene en responseText y no en la propiedad responseXML
					   var text = ajax.responseText;
					   xmlDoc=new ActiveXObject("Microsoft.XMLDOM");                       
					   xmlDoc.async="false";
					   xmlDoc.loadXML(text);
				}else{
					// En el resto de navegadores el XML se recupera de la propiedad responseXML
					xmlDoc = ajax.responseXML;
				}

				nodos = xmlDoc.getElementsByTagName("VERIFICACION_TAREAS_PENDIENTES_TRAMITE");
				var elemento = nodos[0];

				var hijos = elemento.childNodes;
				var tieneTareasPendientes = null;
				 for(j=0;hijos!=null && j<hijos.length;j++){
					if(hijos[j].nodeName=="TIENE_TAREAS_PENDIENTES"){
						tieneTareasPendientes = hijos[j].childNodes[0].nodeValue;
                        break;
					}
				 }
                 if(tieneTareasPendientes=="SI")
                     exito = true;
                 else exito = false;
		    }// if
		}catch(err){
			alert("descripción: " + err.description);
		} 		
	}
	return exito;
}

function pulsarEliminarConfSW() {
	if (tabConfSW.selectedIndex == -1) {
	  noFilaSelected();
    } else {

        var continuar = false;
        if(tieneTramiteTareasPendientes()){
            // Si hay tareas pendientes para la operación a eliminar, entonces se solicita confirmacion al usuario para eliminarla
           if(jsp_alerta("C",'<%=descriptor.getDescripcion("msgConfirElimOpInicio")%>')){
               continuar = true;
           }
        }else continuar = true
           
        if(continuar){
            document.forms[0].codOrdenEjec.value = tabConfSW.selectedIndex;
            document.forms[0].opcion.value = "eliminarSW";
            document.forms[0].target="oculto";
            document.forms[0].action = "<c:url value='/sge/DefinicionTramites.do'/>";
            document.forms[0].submit();
            limpiarCombosWS();
        }
	}	
}

function pulsarGrabarConfSW() { 
	
	if (tabConfSW.selectedIndex == -1) {
	  noFilaSelected();
	} else {	  
	  	
  	  document.forms[0].codOrdenEjec.value = tabConfSW.selectedIndex;     
      document.forms[0].cfoAvanzar.value = listaConfSWCfos[tabConfSW.selectedIndex][1];
	  document.forms[0].cfoRetroceder.value = listaConfSWCfos[tabConfSW.selectedIndex][2];
	  document.forms[0].cfoIniciar.value    = listaConfSWCfos[tabConfSW.selectedIndex][0];

      var codAvanzarSW    = document.forms[0].codAvanzarSW.value;
      var codRetrocederSW = document.forms[0].codRetrocederSW.value;
      var codIniciarSW    = document.forms[0].codIniciarSW.value;
      
	if(codAvanzarSW!=null && codAvanzarSW!=-1 && codAvanzarSW!="" && codIniciarSW!=null && codIniciarSW!=-1 && codIniciarSW!=""){ 
		jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrExistirIniciarAvanzar")%>');
	}else{
        var esTramiteInicio = document.forms[0].tramiteInicio.checked;
        
        if(esTramiteInicio && codRetrocederSW!=-1){ 
			jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrTramInicioOpRetroceder")%>');
		}else{	
			var origenIniciar= "";
			var nombreOperacionIniciar = "";
			var nombreModuloIniciar   = "";
			if(codIniciarSW!=null && codIniciarSW!=-1){
				for(i=0;i<cod_webServices.length;i++){
					if(cod_webServices[i]==codIniciarSW){
						break;
					}
				}
				origenIniciar = tipoOrigenOperaciones[i];
				nombreOperacionIniciar = tituloOperaciones[i];
				nombreModuloIniciar    = nombreModulos[i];
			}

			var origenAvanzar = "";
			var nombreOperacionAvanzar = "";
			var nombreModuloAvanzar    = "";
			if(codAvanzarSW!=null && codAvanzarSW!=-1){
				for(i=0;i<cod_webServices.length;i++){
					if(cod_webServices[i]==codAvanzarSW){
						break;
					}
				}
				origenAvanzar = tipoOrigenOperaciones[i];
				nombreOperacionAvanzar = tituloOperaciones[i];
				nombreModuloAvanzar    = nombreModulos[i];
			}

			var origenRetroceder 		  = "";
			var nombreOperacionRetroceder = "";
			var nombreModuloRetroceder    = "";
			if(codRetrocederSW!=null && codRetrocederSW!=-1){

				for(i=0;i<cod_webServices.length;i++){
					if(cod_webServices[i]==codRetrocederSW){
						break;
					}
				}
				origenRetroceder 		  = tipoOrigenOperaciones[i];
				nombreOperacionRetroceder = tituloOperaciones[i];
				nombreModuloRetroceder    = nombreModulos[i];
			}
			
			document.forms[0].tituloOperacionAvanzar.value    = nombreOperacionAvanzar;
			document.forms[0].tituloOperacionRetroceder.value = nombreOperacionRetroceder;
			document.forms[0].tipoOrigenAvanzar.value= origenAvanzar;
			document.forms[0].tipoOrigenRetroceder.value      = origenRetroceder;
			document.forms[0].nombreModuloAvanzar.value         = nombreModuloAvanzar;
			document.forms[0].nombreModuloRetroceder.value      = nombreModuloRetroceder;
			document.forms[0].tipoOrigenIniciar.value			= origenIniciar;
			document.forms[0].tituloOperacionIniciar.value	    = nombreOperacionIniciar;
			document.forms[0].nombreModuloIniciar.value			= nombreModuloIniciar;
				  
			  document.forms[0].opcion.value = "grabarSW";
			  document.forms[0].target="oculto";
			  document.forms[0].action = "<c:url value='/sge/DefinicionTramites.do'/>";			  
			  document.forms[0].submit();
			  limpiarCombosWS()
			}		
		}
	}
}

function noFilaSelected() {
	jsp_alerta('A',"Por favor, seleccione una fila");
}

function getNombresConfSW(listaCodigosOps) { 	
	var listaNombres = new Array();	  	
   
	for(i=0;i<listaCodigosOps.length;i++){
		var aux = new Array();
		for(j=0;j<3;j++){
			var tipoOperacion   = tiposOperaciones[i][j];
			var nombreOperacion = nombresOperaciones[i][j];
            var nombreModulo    = nombresModulo[i][j];		
			var descripcion = "";
			for(h=0;h<desc_webServices.length;h++){
				var datosAuxiliar = desc_webServices[h].split("(");
				var nombre = datosAuxiliar[0].trim();                
				if(nombre.trim()==nombreOperacion.trim()){
					// Se comprueba si la operación es de WS o de módulo                    
					if(tipoOrigenOperaciones[h]=="WS"){						
						descripcion = desc_webServices[h];
						break;
					}else
					if(tipoOrigenOperaciones[h]=="MODULO" && tipoOrigenOperaciones[h]==tipoOperacion){												
						if(nombreModulos[h]==nombreModulo){
							descripcion = desc_webServices[h];						
						}else{                            
							descripcion = '<%=descriptor.getDescripcion("msgOpModuloNoDisponible")%>';
                         }
						break;
					}					
				}
			}						
            if(descripcion=="" && tipoOperacion=="MODULO" && nombreModulo!=""){				
                aux[j] = '<%=descriptor.getDescripcion("msgOpModuloNoDisponible")%>';
			}
            else aux[j] = descripcion;
		} // for
        listaNombres[i] = aux;
	} // for
   
	return listaNombres;
}

function modificarEtiquetaFirma()
{
	if(tipoUsuarioFirma=='0')
	{ 
		document.forms[0].firma.value='<%= descriptor.getDescripcion("gEtiq_firma")%>:';
		document.forms[0].nombreOtroUsuarioFirma.value='<%= descriptor.getDescripcion("gEtiq_firmaTramitador")%>';
	}
	else if (tipoUsuarioFirma=='1')
	{  
		document.forms[0].firma.value='<%= descriptor.getDescripcion("gEtiq_firma")%>:';
		document.forms[0].nombreOtroUsuarioFirma.value=nombreOtroUsuarioFirma;
	}
	else
	{   
		document.forms[0].firma.value='';
		document.forms[0].nombreOtroUsuarioFirma.value='';
	}
}

function comprobarNotificacionesCercaFinPlazo(){

 if(document.forms[0].notificarCercaFinPlazo.checked==true){
      if(document.forms[0].codNotCercaFinPlazo.value==""){
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFrecuNotiObl")%>');
          return true;
      }else{
          return false;
      }
  }
 
}
function comprobarNotificacionesFueraDePlazo(){
 if(document.forms[0].notificarFueraDePlazo.checked==true){
      if(document.forms[0].codNotFueraDePlazo.value==""){
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFrecuNotiObl")%>');
          return true;
      }else{
          return false;
      }
  }

}

function comprobarCheckTipoNotifElectronica() {
	if(document.forms[0].admiteNotificacionElectronica.checked==true){      
	  if(document.forms[0].codAdmiteNotifElect.value==""){          
          jsp_alerta('A','<%=descriptor.getDescripcion("msjSelecTipoNotif")%>');
          return true;
      }

      if(document.forms[0].codDepartamentoNotificacion.value==""){
          jsp_alerta('A','<%=descriptor.getDescripcion("msgDeptoObligNotificacion")%>');
          return true;
      }
      
      return false;
	}
}


function mostrarErrorCargarEtiquetas(codError){	
    if(codError===1){	
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrSinEtiquetas")%>');	
    } else if(codError===2){	
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrObtenerEtiquetas")%>');	
    } else if(codError===3){	
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');	
    }	
}


</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{pleaseWait('off'); 
        
        <% if("si".equals(enviar)) { %>
          recibir();
        <% } else { %>
          inicializar();
        <% } %>
        }">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/sge/DefinicionTramites.do" target="_self">

<html:hidden  property="tipo_select"   value=""/>
<html:hidden  property="col_cod"   value=""/>
<html:hidden  property="col_desc"   value=""/>
<html:hidden  property="nom_tabla"   value=""/>
<html:hidden  property="input_cod"   value=""/>
<html:hidden  property="input_desc"   value=""/>
<html:hidden  property="column_valor_where" value=""/>
<html:hidden  property="whereComplejo" value=""/>
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codMunicipio" />
<html:hidden property="codAplicacion" />
<html:hidden property="importar" />
<html:hidden property="codigoTramite" />
<html:hidden property="noModificar" />
<html:hidden property="deCatalogo" />
<html:hidden property="deCatalogoDeProcedimiento" />

<html:hidden property="certificadoOrganismoFirmaNotificacionValue" />
<html:hidden property="notificacionElectronicaObligatoriaValue" />

<input type="hidden" name="enviar" value="no">

<input type="hidden" name="listaCodTramitesTabla" value="">
<input type="hidden" name="listaTiposTabla" value="">
<input type="hidden" name="listaTramitesTabla" value="">
<input type="hidden" name="listaEstadosTabla" value="">
<input type="hidden" name="listaCodCondicionTabla" value="">
<input type="hidden" name="listaExpresionesTabla" value="">
<input type="hidden" name="listaCodigosDocTabla" value="">

<input type="hidden" name="codPlantilla" value="">
<input type="hidden" name="listaNombresDoc" value="">
<input type="hidden" name="listaCodigosDoc" value="">
<input type="hidden" name="listaVisibleDoc" value="">
<input type="hidden" name="listaPlantillaDoc" value="">
<input type="hidden" name="listaCodPlantilla" value="">
<input type="hidden" name="listaFirmaPlantilla" value="">
<input type="hidden" name="listaDocActivos" value="">

<input type="hidden" name="listaCodigoEnlaces" value="">
<input type="hidden" name="listaDescripcionEnlaces" value="">
<input type="hidden" name="listaUrlEnlaces" value="">
<input type="hidden" name="listaEstadoEnlaces" value="">

<input type="hidden" name="listaCodCampos" value="">
<input type="hidden" name="listaDescCampos" value="">
<input type="hidden" name="listaCodPlantill" value="">
<input type="hidden" name="listaCodTipoDato" value="">
<input type="hidden" name="listaTamano" value="">
<input type="hidden" name="listaMascara" value="">
<input type="hidden" name="listaObligatorio" value="">
<input type="hidden" name="listaOrden" value="">
<input type="hidden" name="listaRotulo" value="">
<input type="hidden" name="listaVisible" value="">
<input type="hidden" name="listaActivo" value="">
<input type="hidden" name="listaOcultos" value="">
<input type="hidden" name="listaBloqueados" value="">
<input type="hidden" name="listaPlazoFecha" value="">
<input type="hidden" name="listaCheckPlazoFecha" value="">
<input type="hidden" name="listaValidacion" value="">
<input type="hidden" name="listaOperacion" value="">
<input type="hidden" name="listaCodAgrupacion" value="">
<input type="hidden" name="listaPosicionesX" value="">
<input type="hidden" name="listaPosicionesY" value="">

<input type="hidden" name="listaCodAgrupaciones" value="">
<input type="hidden" name="listaDescAgrupaciones" value="">
<input type="hidden" name="listaOrdenAgrupaciones" value="">
<input type="hidden" name="listaAgrupacionesActivas" value="">

<input type="hidden" name="listaCodTramitesFlujoSalida" value="">
<input type="hidden" name="listaNombreTramitesFlujoSalida" value="">
<input type="hidden" name="listaDescTramitesFlujoSalida" value="">
<input type="hidden" name="listaNumerosSecuenciaFlujoSalida" value="">
<input type="hidden" name="oblig" value="">
<input type="hidden" name="numeroCondicionSalida" value="">

<input type="hidden" name="listaCodTramitesFlujoSalidaSiFavorable" value="">
<input type="hidden" name="listaNombreTramitesFlujoSalidaSiFavorable" value="">
<input type="hidden" name="listaDescTramitesFlujoSalidaSiFavorable" value="">
<input type="hidden" name="listaNumerosSecuenciaFlujoSalidaSiFavorable" value="">
<input type="hidden" name="obligSiFavorable" value="">
<input type="hidden" name="numeroCondicionSalidaSiFavorable" value="">

<input type="hidden" name="listaCodTramitesFlujoSalidaNoFavorable" value="">
<input type="hidden" name="listaNombreTramitesFlujoSalidaNoFavorable" value="">
<input type="hidden" name="listaDescTramitesFlujoSalidaNoFavorable" value="">
<input type="hidden" name="listaNumerosSecuenciaFlujoSalidaNoFavorable" value="">
<input type="hidden" name="obligNoFavorable" value="">
<input type="hidden" name="numeroCondicionSalidaNoFavorable" value="">

<input type="hidden" name="enlace" value="">
<input type="hidden" name="codigoProcedimiento" value="">

<input type="hidden" name="interesado" value="">
<input type="hidden" name="editorTexto" value="">
<input type="hidden" name="relacion" value="">
<input type="hidden" name="codDocumento" value="">
<input type="hidden" name="nombreDocumento" value="">
<input type="hidden" name="docActivo" value="">
<input type="hidden" name="codProcedimiento" value="">
<input type="hidden" name="codTramite" value="">
<input type="hidden" name="txtUnidadesTramitadoras" value="">
<input type="hidden" name="visibleExt" value="">
<input type="hidden" name="modificando" value="">


<input type="hidden" name="codigoTipoNotificacionElectronica" value="">
<input type="hidden" name="tipoUsuarioFirma" value="">
<input type="hidden" name="codigoOtroUsuarioFirma" value="">

<!--- INTEGRACION DE OPERACIONES DE WS Y DE MÓDULOS EXTERNOS ----->
<input type="hidden" name="tipoOrigenAvanzar" value=""/>
<input type="hidden" name="tipoOrigenRetroceder" value=""/>
<input type="hidden" name="tituloOperacionAvanzar" value=""/>
<input type="hidden" name="tituloOperacionRetroceder" value=""/>

<input type="hidden" name="nombreModuloAvanzar" value=""/>
<input type="hidden" name="nombreModuloRetroceder" value=""/>

<input type="hidden" name="tipoOrigenIniciar" value=""/>
<input type="hidden" name="tituloOperacionIniciar" value=""/>
<input type="hidden" name="nombreModuloIniciar" value=""/>

<input type="hidden" name="implClassPluginPantallaTramExterna" value=""/>

<!--- INTEGRACION DE OPERACIONES DE WS Y DE MÓDULOS EXTERNOS ----->
<div class="txttitblanco"><%=descriptor.getDescripcion("tit_defTram")%></div>
<div class="encabezadoGris">
    <span class="etiqueta" style="width:10%" ><%=descriptor.getDescripcion("res_etiqProc")%>:</span>
    <html:text styleId="obligatorio" styleClass="inputTexto" property="txtCodigo" style="width:5%" readonly="true" onkeyup="return xAMayusculas(this);"/>
    <html:text styleId="obligatorio" styleClass="inputTexto" style="width:42%" property="txtDescripcion" maxlength="50" readonly="true" onblur="return xAMayusculas(this);"/>
    <span class="etiqueta" style="width:10%;margin-left:1%" ><%=descriptor.getDescripcion("gEtiq_tramite")%>:</span>
    <input type="text" name="nombreTramiteI" class="inputTexto" style="width:32%" readonly="true">
</div>
<div class="contenidoPantalla">
    <div class="tab-pane" id="tab-pane-1" style="width:100%;">
        <script type="text/javascript">
            tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
        </script>
        <div class="tab-page" id="tabPage1" >
            <h2 class="tab"><%=descriptor.getDescripcion("res_pestana1")%></h2>
            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
            <TABLE id ="tablaDatosDef" class="contenidoPestanha" style="width:100%;">
                <TR>
                    <TD class="sub3titulo" colspan="2"><%=descriptor.getDescripcion("res_etiqDefTra")%></TD>
                </TR>
                <tr>
                    <TD style="width:18%;" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tramite")%>:</TD>
                    <TD class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="numeroTramite" style="width:10%" maxlength="4" onkeyup="return SoloDigitosNumericos(this);"/>
                    </TD>
                </tr>
                <tr>
                    <TD style="width:18%;" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_nombre")%>:</TD>
                    <TD class="columnP">
                        <html:text styleClass="inputTexto" property="codigoInternoTramite" style="width:10%" readonly="true"/>
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="nombreTramite" maxlength="500" style="width:80%" onblur="return SoloCaracterValidos(this);"/>
                    </TD>
                 </tr>
                 <tr>
                     <td style="width:18%;" class="etiqueta"><%=descriptor.getDescripcion("etiq_ocurr")%>:</td>
                     <td valign="top" class="columnP">
                         <html:text styleClass="inputTexto" property="ocurrencias" style="width:10%" maxlength="2" onkeyup="return SoloDigitos(this);"/>
                         <input type="checkbox" name="tramiteInicio" style="margin-left:30%" value="si" onclick="deshabilitarTramiteAnterior();">&nbsp;&nbsp;<%=descriptor.getDescripcion("tramInic")%>
                         <input type="checkbox" name="disponible" value="si">&nbsp;&nbsp;<%=descriptor.getDescripcion("dispTramElect")%>
                     </td>
                 </tr>
                 <tr>
                     <TD style="width:18%;" class="etiqueta"><%=descriptor.getDescripcion("etiq_clasTram")%>:</TD>
                     <TD valign="top" class="columnP">
                         <html:text styleClass="inputTextoObligatorio" styleId="obligatorio" property="codClasifTramite" style="width:10%" onkeyup="return xAMayusculas(this);" onfocus="this.select();"/>
                         <html:text styleClass="inputTextoObligatorio" styleId="obligatorio" property="descClasifTramite" style="width:80%" readonly="true"/>
                         <A id="anchorClasifTramite" name="anchorClasifTramite"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonClasifTramite" name="botonClasifTramite"></span></A>
                     </TD>
                 </tr>
                 <tr>
                     <TD style="width:18%;" class="etiqueta"><%=descriptor.getDescripcion("etiq_prNot")%>:</TD>
                     <TD class="columnP">
                         <table width="100%">
                             <tr>
                                 <td style="width: 7%" class="columnP">
                                     <html:text styleClass="inputTexto" property="plazo" style="width:99%" maxlength="2" onkeyup="return SoloDigitos(this);" onchange="javascript:onClickUnidadesPlazo();"/>
                                 </td>
                                 <td style="width: 15%" class="columnP">
                                     <html:radio property="unidadesPlazo" styleClass="textoSuelto" value="H" onclick="onClickUnidadesPlazo();"/> <%=descriptor.getDescripcion("etiq_plzHab")%>&nbsp;
                                 </td>
                                 <td style="width: 15%" class="columnP">
                                     <html:radio property="unidadesPlazo" styleClass="textoSuelto" value="N" onclick="onClickUnidadesPlazo();"/> <%=descriptor.getDescripcion("etiq_plzNat")%>&nbsp;
                                 </td>
                                 <td class="columnP" style="width: 10%">
                                     <html:radio property="unidadesPlazo" styleClass="textoSuelto" value="M" onclick="onClickUnidadesPlazo();"/>
                                     <%=descriptor.getDescripcion("etiq_plzMes")%>&nbsp;
                                </td>
                                <td style="width: 3%"></td>
                                <td id="et1" class="etiquetaDeshabilitada"><%=descriptor.getDescripcion("etiq_Avisoqueda")%></td>
                                <td class="columnP">
                                     <table width="100%">
                                         <tr>
                                             <td style="width: 7%" class="columnP">
                                                 <input type="text" class="inputTexto" disabled name="plazoFin" style="width:99%" maxlength="3" onkeyup="return SoloDigitos(this);"  style="color:#465736"/>
                                             </td>
                                             <TD id="et2" class="etiquetaDeshabilitada">&nbsp;<%=descriptor.getDescripcion("etiq_plazoFinal")%></TD>
                                         </tr>
                                     </table>
                                </td>
                             </tr>
                         </table>
                     </TD>
                 </tr>
                 <tr>
                     <td colspan="2" style="width: 100%">
                         <table style="width: 100%">
                             <tr>
                                <td style="width: 60%"></td>
                                <td class="columnP" style="width: 2%">
                                    <html:checkbox property="generarPlazos" disabled="true" value="on"/>
                                </td>
                                <td id="et3" class="etiquetaDeshabilitada"><%=descriptor.getDescripcion("etiqGenerarPlazos")%></td>
                            </tr>
                         </table>
                     </td>
                 </tr>
                 <TR>
                    <TD class="sub3titulo" colspan="2"> <%=descriptor.getDescripcion("unid_tramit")%> </TD>
                 </tr>
                 <tr>
                     <TD style="width:18%;" class="etiqueta"><%=descriptor.getDescripcion("etiq_unidIniMan")%>:</TD>
                     <TD valign="top" class="columnP">
                         <table width="100%">
                             <tr>
                                 <td style="width: 20%" class="columnP">
                                     <input type="radio" name="radioUnidadInicio" class="textoSuelto" value="C" onclick="onClickRadioUnidadInicio();"/> <%=descriptor.getDescripcion("etiqLaDelExp")%>&nbsp;
                                 </td>
                                 <td style="width: 20%" class="columnP">
                                     <input type="radio" name="radioUnidadInicio" class="textoSuelto" value="E" onclick="onClickRadioUnidadInicio();"/> <%=descriptor.getDescripcion("etiq_cqunidIni")%>&nbsp;
                                 </td>
                                 <td style="width: 20%" class="columnP">
                                     <input type="radio" name="radioUnidadInicio" class="textoSuelto" value="V" onclick="onClickRadioUnidadInicio();"/> <%=descriptor.getDescripcion("etiqSinUnidad")%>&nbsp;
                                 </td>
                                 <td class="columnP">
                                     <input type="radio" name="radioUnidadInicio" class="textoSuelto" value="O" onclick="onClickRadioUnidadInicio();"/> <%=descriptor.getDescripcion("etiqOtra")%>&nbsp;
                                 </td>
                             </tr>
                         </table>
                     </td>
                 </tr>
                 <tr style="margin-top:2px;">
                     <TD style="width:18%;" class="etiqueta"></TD>
                     <TD valign="top" class="columnP">
                         <input type="hidden" name="codUnidadInicio" id="codUnidadInicio"/>
                         <input type="text" class="inputTexto" name="codVisibleUnidadInicio" style="width:10%" onkeyup="return xAMayusculas(this);" onfocus="javascript:this.select();" onchange="javascript:{onchangeCodUnidadInicio();}"/>
                         <input type=text class="inputTexto" name="descUnidadInicio" style="width:80%" readonly="true"
                                onclick="javascript:{onClickDescUnidadInicio();}"/>
                         <A href="javascript:{onClickHrefUnidadInicio();}" style="text-decoration:none;" id="anclaD2" name="anchorUnidadInicio">
                             <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUnidadInicio" name="botonUnidadInicio" style="cursor:hand;"></span>
                         </A>

                     </TD>
                 </tr>
                 <tr>
                     <TD style="width:18%;" class="etiqueta"><%=descriptor.getDescripcion("etiq_unidTram")%>:</TD>
                     <TD valign="top" class="columnP">
                         <table width="100%">
                             <tr>
                                 <td style="width: 18%" class="columnP">
                                     <input type="radio" name="codUnidadTramite" class="textoSuelto" value="<%=ConstantesDatos.TRA_UTR_EXPEDIENTE%>" onclick="onClickRadioUnidadTramite();" checked/> <%=descriptor.getDescripcion("etiqLaDelExp")%>&nbsp;
                                 </td>
                                 <td style="width: 15%" class="columnP">
                                     <input type="radio" name="codUnidadTramite" class="textoSuelto" value="<%=ConstantesDatos.TRA_UTR_INICIA%>" onclick="onClickRadioUnidadTramite();"/> <%=descriptor.getDescripcion("etiqLaQueLoIni")%>&nbsp;
                                 </td>
                                 <td style="width: 23%" class="columnP">
                                     <input type="radio" name="codUnidadTramite" class="textoSuelto" value="<%=ConstantesDatos.TRA_UTR_ANTERIOR%>" onclick="onClickRadioUnidadTramite();"/> <%=descriptor.getDescripcion("etiqLaDelTramAnt")%>&nbsp;
                                 </td>
                                 <td style="width: 15%" class="columnP">
                                     <input type="radio" name="codUnidadTramite" class="textoSuelto" value="<%=ConstantesDatos.TRA_UTR_CUALQUIERA%>" onclick="onClickRadioUnidadTramite();"/> <%=descriptor.getDescripcion("etiq_cqunidIni")%>&nbsp;
                                 </td>
                                 <td style="width: 10%" class="columnP">
                                     <input type="radio" name="codUnidadTramite" class="textoSuelto" value="<%=ConstantesDatos.TRA_UTR_OTRAS%>" onclick="onClickRadioUnidadTramite();"/> <%=descriptor.getDescripcion("etiq_otrasunidIni")%>&nbsp;
                                 </td>
                                  <td style="width: 40%" class="columnP">
                                     <input style="padding-left: 10px" type="checkbox" name="soloEsta" value="si">&nbsp;<%=descriptor.getDescripcion("etiq_soloEsta")%>
                                 </td>
                                 <td class="columnP">
                                   <span id="botonListadoUtrs" style="visibility:hidden">
                                     <span class="fa fa-search" aria-hidden="true"  title="<%=descriptor.getDescripcion("toolTipSelUTramite")%>" alt="<%=descriptor.getDescripcion("toolTipSelUTramite")%>" style="cursor:hand;" onclick="pulsarListadoUtrs();"></span>
                                   </span>
                                 </td>
                             </tr>
                         </table>
                     </td>
                 </tr>
                 <tr>
                     <TD style="width:18%;" class="etiqueta"></TD>
                     <TD style="padding: 10px 10px 10px 0;" id="tablaUnidadesTramite"></TD>                                                         
                 </tr>
                 <tr>
                     <td style="width:18%;" class="etiqueta">
                         <%= descriptor.getDescripcion("etiq_Cargo")%>:
                     </td>
                     <td valign="top" class="columnP">
                         <input type="hidden" name="codCargo" id="codCargo"/>
                         <input type=text class="inputTexto" name="cod_visible_cargo" style="width:10%"
                                onkeyup="return xAMayusculas(this);"
                                onfocus="javascript:this.select();"
                                onchange="javascript:{onchangeCodCargo();}"/>
                         <input type=text class="inputTexto" name="descCargo" style="width:80%" readonly="true"
                                onclick="javascript:{onClickDescCargo();}"/>
                         <A href="javascript:{onClickHrefCargo();}" style="text-decoration:none;" id="anclaD2" name="anchorCargo">
                             <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCargo" name="botonCargo"></span>
                         </A>

                     </td>
                 </tr>
             </table>
         </div>
         <div class="tab-page" id="tabPage2">
             <h2 class="tab"><%=descriptor.getDescripcion("etiqDocs")%></h2>
             <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
             <TABLE id ="tablaTemas" class="contenidoPestanha" style="padding-bottom: 10px;width:100%;">
                 <TR>
                     <TD id="tablaDoc"></TD>
                 </TR>
                 <tr>
                     <td>
                         <DIV STYLE="width:100%;text-align: center">
                            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAltaDoc" onclick="pulsarNuevoWord();">
                            <input type= "button" class="botonGeneral" value='Adjuntar' name="cmdAdjuntarDoc" onclick="pulsarNuevaPlantilla();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificarDoc" onclick="pulsarModificarWord();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbDesactivar")%> name="cmdEliminarDoc" onclick="pulsarEliminarDoc();">
                            <input type= "button" class="botonGeneral" value="Activar" name="cmdActivarDoc" onclick="activarDoc();">
                            <input type="button" class="botonGeneral" value="Descargar" name="DescargarDoc" onclick="descargarDoc();">
                            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("btnVerEtiquetas")%>' name="cmdEtiquetasDoc" onclick="pulsarVerEtiquetas();">
                             <%if(visibleAppExt){%>	
                              <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("btnVisibleExt")%>' id="btnVisibleExt" name="cmdVisibleExt" onclick="pulsarVisibleExt();">	
	
                            <%}%>
                         </DIV>
                     </td>    
                 </tr>    
             </table>                                                                                                  
         </div>
         <div class="tab-page" id="tabPage3">
             <h2 class="tab">Enlaces</h2>
             <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
             <TABLE id ="tablaE" class="contenidoPestanha" style="width:100%;">
                 <tr>
                     <td id="tablaEnlaces" style="padding-bottom: 10px"></td>
                 </tr>
                 <tr>
                     <!-- Descripcion -->
                     <td style="padding-bottom: 10px">
                         <input type="text" class="inputTextoObligatorio" style="width:44%" name="txtDescUrl" maxlength="100" onblur="return xAMayusculas(this);">
                         <input type="text" class="inputTextoObligatorio" style="width:44%" name="txtUrl" maxlength="1000" onblur="return xAMayusculas(this);">
                         <input type="text" class="inputTextoObligatorio" style="width:7%" name="descEstUrl" id="descEstUrl" readonly="true">
                         <A name="anchorEstUrl" id="anchorEstUrl">
                             <span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonEstUrl" id="botonEstUrl"></span>
                         </A>
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <DIV STYLE="width:100%;padding-left: 20%;">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaEnlaces" onclick="pulsarAltaEnlaces();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificarEnlaces" onclick="pulsarModificarEnlaces();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarEnlaces" onclick="pulsarEliminarEnlaces();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiarEnlaces" onclick="limpiarInputsEnlaces();">
                         </DIV>
                     </td>    
                 </tr>
             </TABLE>                                                                                                  
         </div>
         <div class="tab-page" id="tabPage4">
             <h2 class="tab"><%=descriptor.getDescripcion("etiqCampos")%></h2>
             <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
             <table class="contenidoPestanha" style="width:100%;">
                 <tr>
                     <td class="sub3titulo">
                         Definición de campos
                     </td>
                 </tr>
                 <TR>
                     <TD id="tablaCampos" style="padding-bottom: 10px"></td>
                 </TR>
                 <tr>
                     <td STYLE="text-align:center">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaCampo" onclick="pulsarAltaCampo();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificarCampo" onclick="pulsarModificarCampo();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarCampo" onclick="pulsarEliminarCampo();">
                            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbOrdenar")%> name="cmdOrdenarCampo" onclick="pulsarOrdenarCampo();" style="display:none">
                            <input type="button" class="botonGeneral" value="Vista" name="cmdVista" onClick="pulsarVista();">
                     </td>    
                 </tr>
                <tr>
                    <td class="sub3titulo">
                        <%=descriptor.getDescripcion("getiq_defagrupacion")%>
                    </td>
                </tr>
                <TR style="padding-bottom: 4px">
                    <TD id="tablaAgrupaciones"></TD>
                </tr>
                <tr>
                    <td style="text-align:center">
                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaAgrupacion" onClick="pulsarAltaAgrupacion();">
                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificarAgrupacion" onClick="pulsarModificarAgrupacion();">
                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarAgrupacion" onClick="pulsarEliminarAgrupacion();">
                    </td>
                </tr>
            </table>
         </div>
         <div class="tab-page" id="tabPage5">
             <h2 class="tab"><%=descriptor.getDescripcion("cond_entrada")%></h2>
             <script type="text/javascript">tp1_p5 = tp1.addTabPage( document.getElementById( "tabPage5" ) );</script>
             <TABLE id ="tablaTemas" class="contenidoPestanha" style="width:100%;">
                 <TR>
                     <TD id="tabla" style="padding-bottom: 10px">
                     </TD>
                 </TR>
                 <TR>
                     <TD style="padding-bottom: 10px" align="center">
                         <input type="text" style="width:150px" class="inputTextoObligatorio" name="descCondicionTabla">
                         <A id="anchorCondicionTabla"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicionTabla" name="botonCondicionTabla"></span></A>
                         <div style="margin-left: 10px; display: inline"></div>
                         <input type="text" class="inputTextoObligatorio"  name="codTramiteTabla" size="3" onkeyup="return xAMayusculas(this);" onfocus="this.select();"/>
                         <input type="text" style="width:250px" class="inputTextoObligatorio" name="descTramiteTabla"  size="45" readonly="true"/>
                         <A id="anchorTramiteTabla" name="anchorTramiteTabla"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTramiteTabla" name="botonTramiteTabla"></span></A>
                         <div style="margin-left: 10px; display: inline"></div>
                         <input type="text" style="width:250px" class="inputTextoObligatorio" name="descEstadoTramiteTabla"  size="50" readonly="true">
                         <A id="anchorEstadoTramiteTabla"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonEstadoTramiteTabla" name="botonEstadoTramiteTabla"></span></A>                                                             
                     </TD>
                 </TR>
             </TABLE>                                                                                                  
            <DIV STYLE="width:100%;text-align: center">
               <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaTablaEntrada" onclick="pulsarAltaTablaEntrada();">
               <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificarTablaEntrada" onclick="pulsarModificarTablaEntrada();">
               <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarTablaEntrada" onclick="pulsarEliminarTablaEntrada();">
               <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiarTablaEntrada" onclick="pulsarLimpiarTablaEntrada();">
            </DIV>
         </div>
         <div class="tab-page" id="tabPage6">
             <h2 class="tab"><%=descriptor.getDescripcion("cond_saida")%></h2>
             <script type="text/javascript">tp1_p6 = tp1.addTabPage( document.getElementById( "tabPage6" ) );</script>
             <TABLE id ="tablaCondSaida" class="contenidoPestanha" style="width:100%;" >
                 <TR>
                    <TD class="sub3titulo" colspan="2"> <%=descriptor.getDescripcion("cond_saida")%> </TD>
                 </tr>
                <tr>
                    <td colspan="2" class="etiqueta">
                        <input type="radio" name="tipoCondicion" value="sinCondicion" CHECKED onclick="javascript:radioTramite();"></input>
                        <%=descriptor.getDescripcion("etiq_SCondSai")%>
                    </td>                                                                     
                </tr>
                <tr>
                    <td colspan="2">
                        <span class="etiqueta">
                            <input type="radio" name="tipoCondicion" value="Tramite" onclick="javascript:radioTramite();"></input>
                            <%=descriptor.getDescripcion("gEtiq_tramite")%>
                        </span>
                        <span class="etiqueta" style="margin-left:20%">
                            <%=descriptor.getDescripcion("etiq_listTram")%>
                        </span>
                        <span class="columnP">
                            <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gEtiq_lista")%> name="cmdListaTramite" onclick="pulsarListaTramite();">
                        </span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="etiqueta">
                        <input type="radio" name="tipoCondicion" value="Finalizacion" onclick="javascript:radioTramite();"></input>
                        <%=descriptor.getDescripcion("gEtiq_finaliz")%>
                    </td>                                                                     
                </tr>
                <tr>
                    <td colspan="2" class="etiqueta" style="padding-top:15px">
                        <input type="radio" name="tipoCondicion" value="Resolucion" onclick="javascript:radioTramite();"></input>
                        <%=descriptor.getDescripcion("etiq_resaluc")%>
                    </td> 
                </tr>
                <tr>
                    <td colspan="2">
                        <span class="etiqueta">
                            <input type="radio" name="tipoCondicion" value="Pregunta" onclick="javascript:radioTramite();"></input>
                            <%=descriptor.getDescripcion("gEtiq_pregunta")%>
                        </span>
                        <span class="etiqueta" style="margin-left:19%">
                            <%=descriptor.getDescripcion("gEtiq_texto")%>:&nbsp;
                        <html:text styleClass="inputTexto" property="texto" size="35" onclick="javascript:seleccTipoCondicionPregunta(true);" onkeyup="return xAMayusculas(this);" onchange="javascript:onChangePregunta();" onfocus="javascript:onFocusPregunta();" onblur="javascript:onBlurPregunta();"/>
                        </span>
                    </td>
                </tr>
                 <tr>
                     <TD class="sub3titulo" style="width:50%"><%=descriptor.getDescripcion("etiq_accionFav")%></TD>
                     <TD class="sub3titulo"><%=descriptor.getDescripcion("etiq_accionDesFav")%></TD>
                 </tr>
                 <tr>
                     <td>
                         <table width="100%">
                             <tr>
                                 <TD style="width: 45%" class="etiqueta" >
                                     <input type="radio" name="tipoFavorableSI" value="TramiteSI" onclick="javasript:radioTramiteSI();"></input>
                                     <%=descriptor.getDescripcion("gEtiq_tramite")%>
                                 </TD>
                                 <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_listTram")%></TD>
                                 <TD class="columnP">
                                     <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gEtiq_lista")%> name="cmdListaTramiteSiFavorable" onclick="pulsarListaTramiteSiFavorable();">
                                 </TD>
                             </tr>
                             <tr>
                                 <TD colspan="3" class="etiqueta">
                                     <input type="radio" name="tipoFavorableSI" value="FinalizacionSI" onclick="javasript:radioTramiteSI();"></input>
                                     <%=descriptor.getDescripcion("gEtiq_finaliz")%>
                                 </TD>
                             </tr>
                         </table>
                     </td>
                     <td>
                         <table width="100%">
                             <tr>
                                 <TD class="etiqueta" style="width: 45%">
                                     <input type="radio" name="tipoFavorableNO" value="TramiteNO"  onclick="javasript:radioTramiteNO();"><%=descriptor.getDescripcion("gEtiq_tramite")%></input>
                                 </TD>
                                 <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_listTram")%></TD>
                                 <TD class="columnP">
                                     <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gEtiq_lista")%> name="cmdListaTramiteNoFavorable" onclick="pulsarListaTramiteNoFavorable();">
                                 </TD>
                             </tr>
                             <tr>
                                 <TD colspan="3" class="etiqueta">
                                     <input type="radio" name="tipoFavorableNO" value="FinalizacionNO" onclick="javasript:radioTramiteNO();"><%=descriptor.getDescripcion("gEtiq_finaliz")%></input>
                                 </td>
                             </tr>
                         </table>
                     </td>
                 </tr>                                                             
                 <tr style="margin-top: 15px">
                     <td colspan="2">
                        <span class="etiqueta"><%=descriptor.getDescripcion("etiq_iniExpRel")%>:</span>
                        <span class="columnP">
                            <html:text styleClass="inputTexto" property="codExpRel" style="width:5%" onkeyup="return xAMayusculas(this);" onfocus="this.select();"/>
                            <html:text styleClass="inputTexto" property="descExpRel" style="width:55%" readonly="true"/>
                            <A id="anchorExpRel" name="anchorExpRel"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonExpRel" name="botonExpRel"></span></A>
                        </span>
                     </td>
                 </tr>
             </table>
         </div>
         <div class="tab-page" id="tabPage7" >
             <h2 class="tab"><%=descriptor.getDescripcion("etiqPestana6")%></h2>
             <script type="text/javascript">tp1_p7 = tp1.addTabPage( document.getElementById( "tabPage7" ) );</script>
             <TABLE id ="tablaDatosDef" class="contenidoPestanha" style="width:100%">
                 <TR>
                     <TD class="etiqueta" valign="top"><%=descriptor.getDescripcion("etiqInstrucc")%>:</TD>
                     <TD class="columnP">
                         <html:textarea styleClass="textareaTexto" cols="140" rows="22" style="width:100%" property="instrucciones" onblur="xAMayusculas(this);"></html:textarea>
                     </TD>
                 </TR>                                                      
             </TABLE>
         </div>
         <div class="tab-page" id="tabPage8" >
             <h2 class="tab"><%=descriptor.getDescripcion("etiqPestana8")%></h2>
             <script type="text/javascript">tp1_p8 = tp1.addTabPage( document.getElementById( "tabPage8" ) );</script>
             <div id ="tablaNotifTramite" class="contenidoPestanha">
                 <div id="capaEmailsATramitadores">
                    <div class="sub3titulo-nomargin"> <%=descriptor.getDescripcion("not_unidad_trami")%> </div>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUdadTramitIni" value="N"></input>
                        <%=descriptor.getDescripcion("al_iniciar")%>
                    </p>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUdadTramitFin" value="N"></input>
                        <%=descriptor.getDescripcion("al_finalizar")%>
                    </p>
                    <div class="sub3titulo-nomargin"> <%=descriptor.getDescripcion("not_usu_unidad_trami")%> </div>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUsuUdadTramitIni" value="N"></input>
                        <%=descriptor.getDescripcion("al_iniciar")%>
                    </p>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUsuUdadTramitFin" value="N"></input>
                        <%=descriptor.getDescripcion("al_finalizar")%>
                    </p>
                    <div class="sub3titulo-nomargin"> <%=descriptor.getDescripcion("not_interesados")%> </div>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkInteresadosIni" value="N"></input>
                        <%=descriptor.getDescripcion("al_iniciar")%>
                    </p>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkInteresadosFin" value="N"></input>
                        <%=descriptor.getDescripcion("al_finalizar")%>
                    </p>
                    <!-- nuevos tipos notificacion -->
                    <div class="sub3titulo-nomargin"> <%=descriptor.getDescripcion("not_usu_inicio_tramite")%> </div>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUsuarioTramiteIni" value="N" />
                        <%=descriptor.getDescripcion("al_iniciar")%>
                    </p>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUsuarioTramiteFin" value="N" />
                        <%=descriptor.getDescripcion("al_finalizar")%>
                    </p>
                    <div class="sub3titulo-nomargin"> <%=descriptor.getDescripcion("not_usu_inicio_expediente")%> </div>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUsuarioExpedIni" value="N">
                        <%=descriptor.getDescripcion("al_iniciar")%>
                    </p>
                    <p class="columnP">
                        <input style="margin-right: 15px" type="checkBox" name="checkUsuarioExpedFin" value="N">
                        <%=descriptor.getDescripcion("al_finalizar")%>
                    </p>
                    <!--   -->
                   <div class="sub3titulo-nomargin"> <%=descriptor.getDescripcion("not_fechaLimite")%> </div>
                        <table style="width:50%; margin-left: -3px;">
                            <tr style="padding-bottom: 10px">
                                <td class="columnP">
                                     <html:checkbox style="margin-right: 5px" property="notificarCercaFinPlazo" onclick="activarSelectNotCercaFinPlazo();"> </html:checkbox>
                                     <%=descriptor.getDescripcion("notificarCercaFinPlazo")%>
                                </td>
                                <td>
                                   <input type="hidden" class="inputTextoObligatorio" name="codNotCercaFinPlazo" id="codNotCercaFinPlazo" size="0" readonly="true">
                                    <input type="text" class="inputTextoObligatorio" name="descNotCercaFinPlazo" id="descNotCercaFinPlazo" size="20" readonly="true" style="width:140px">
                                    <A name="anchorNotCercaFinPlazo" id="anchorNotCercaFinPlazo">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonNotCercaFinPlazo" id="botonNotCercaFinPlazo"></span>
                                    </A>
                                </td>
                            </tr>
                            <tr>
                                <td class="columnP">
                                     <html:checkbox style="margin-right: 5px" property="notificarFueraDePlazo" onclick="activarSelectNotFueraDePlazo();"></html:checkbox>
                                           <%=descriptor.getDescripcion("notificarFueraDePlazo")%>
                                </td>
                                 <td>
                                   <input type="hidden" class="inputTextoObligatorio" name="codNotFueraDePlazo" id="codNotFueraDePlazo" size="0" readonly="true">
                                    <input type="text" class="inputTextoObligatorio" name="descNotFueraDePlazo" id="descNotFueraDePlazo" readonly="true" style="width:140px">
                                    <A name="anchorNotFueraDePlazo" id="anchorNotFueraDePlazo">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonNotFueraDePlazo" id="botonNotFueraDePlazo"></span>
                                    </A>
                                </td>
                            </tr> 
                            <tr>	
                     <td colspan="2" class="columnP">	
                         <input style="margin-right:5px" type="checkBox" name="checkNotUsuTraFinPlazo" value="N">	
                         <%=descriptor.getDescripcion("etiqUsuTraFinPlazo")%>	
                     </td>	
                 </tr>	
                 <tr>	
                     <td colspan="2" class="columnP">	
                         <input style="margin-right:5px" type="checkBox" name="checkNotUsuExpFinPlazo" value="N">	
                          <%=descriptor.getDescripcion("etiqUsuExpFinPlazo")%>	
                     </td>	
                 </tr>	
                 <tr>	
                     <td colspan="2" class="columnP">	
                         <input style="margin-right:5px" type="checkBox" name="checkNotUORFinPlazo" value="N">	
                         <%=descriptor.getDescripcion("etiqUORFinPlazo")%>	
                     </td>	
                     	
                 </tr>
                        </table>
                 </div>
                 <!--Notificaciones electronicas-->
                 <div class="sub3titulo-nomargin"  style="margin: 5px 0 5px 0;"><%=descriptor.getDescripcion("etiqTituloTramiteNotif")%>  </div>
                <logic:present name="DefinicionTramitesForm" property="mostrarTramiteNotificado">
                    <div id="capaTramiteNotificado">
                        <!-- 
                           Al añadir el elemento de struts html:checkbox, es necesario en el Form.java reescribir el método reset() seteando a false
                        o 0 la propiedad del checkbox ya que cuando no se marca el check la propiedad no se establece correctamente (se le pasa null)
                        mmanteniendo un valor anterior si lo tuviera.
                        -->
                        <html:checkbox property="tramiteNotificado" style="margin-right: 15px" onclick="activarTramiteNotificado(this.checked);"></html:checkbox>
                        <span style="font-weight: bold"><%=descriptor.getDescripcion("etiqTramiteNotif")%></span>
                    </div>
                    <div id="capaNotificacionesElectronicas" class="capa-notif-tramite">
                 </logic:present>
                 <logic:notPresent name="DefinicionTramitesForm" property="mostrarTramiteNotificado">
                    <div id="capaNotificacionesElectronicas">
                 </logic:notPresent>
                        <table style="width:93%">
                            <tr style="padding-bottom: 8px">
                                <td class="columnP" style="width:32%">
                                     <html:checkbox style="margin-right: 5px" property="admiteNotificacionElectronica" onclick="activarAdmiteNotifElect();"> </html:checkbox>
                                     <%=descriptor.getDescripcion("admiteNotifElect")%>
                               </td>
                               <td>
                                   <input type="hidden" class="inputTextoObligatorio" name="codAdmiteNotifElect" id="codAdmiteNotifElect" size="0" readonly="true">
                                    <input type="text" class="inputTextoObligatorio" name="descAdmiteNotifElect" id="descAdmiteNotifElect" size="20" readonly="true" style="width:140px">
                                    <A name="anchorAdmiteNotifElect" id="anchorAdmiteNotifElect">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonAdmiteNotifElect" id="botonAdmiteNotifElect"></span>
                                    </A>
                                   <span style="margin-left:5%" class="etiqueta">
                                       <input type="text" style="border-width:0; FONT-WEIGHT:bold" class="inputTexto" name="firma" id="firma" size="8" readonly="true"> 											   
                                       <input type="text" style="border-width:0" class="inputTexto" name="nombreOtroUsuarioFirma" id="nombreOtroUsuarioFirma" size="20" readonly="true">
                                   </span>
                               </td>                                                        
                            </tr>
                            <tr>
                                <td class="columnP" style="width:32%">
                                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("etiqDeptoNotificacion")%>
                                </td>
                                <td>
                                    <input type="hidden" class="inputTextoObligatorio" name="codDepartamentoNotificacion" id="codDepartamentoNotificacion" size="0" readonly="true">
                                    <input type="text" class="inputTextoObligatorio" name="descDepartamentoNotificacion" id="descDepartamentoNotificacion" size="20" readonly="true" style="width:140px">
                                    <A name="anchorDepartamentoNotificacion" id="anchorDepartamentoNotificacion">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonDepartamentoNotificacion" id="botonDepartamentoNotificacion"></span>
                                    </A>
                                </td>
                            </tr>
                            <!-- Peticion #76148 Checkbox de obligatoriedad-->
                             <tr style="padding-bottom: 8px">
                                <td class="columnP" style="width:32%">
                                    <html:checkbox style="margin-right: 5px" property="notificacionElectronicaObligatoria" onclick="modificarNotifObligatoria();" > </html:checkbox>
                                     <%=descriptor.getDescripcion("notifElectObligatoria")%>
                               </td>
                            </tr>
                            <tr>
                                <td class="columnP" colspan="2">
                                   <html:checkbox style="margin-right: 5px" property="certificadoOrganismoFirmaNotificacion" onclick="modificarCertificadoOrganismo();" > </html:checkbox>
                                   <%=descriptor.getDescripcion("certificadoNotif")%>
                                </td>
                            </tr>
                     </table>                                                 
                </div>
            </div>
        </div>
         <div class="tab-page" id="tabPage9">
             <h2 class="tab"><%=descriptor.getDescripcion("etiqIntegracion")%></h2>
             <script type="text/javascript">tp1_p9 = tp1.addTabPage( document.getElementById( "tabPage9" ) );</script>
             <input type="hidden" name="codOrdenEjec" id="codOrdenEjec">
             <input type="hidden" name="maxOrdEjec" id="maxOrdEjec">
             <input type="hidden" name="cfoAvanzar" id="cfoAvanzar">
             <input type="hidden" name="cfoRetroceder" id="cfoRetroceder">
             <input type="hidden" name="cfoIniciar" id="cfoIniciar">
             <input type="hidden" name="estructuraCorrecta" id="estructuraCorrecta" value="">
             <script type="text/javascript">
                 document.forms[0].cfoAvanzar.value = -1;
                 document.forms[0].cfoRetroceder.value = -1;
                 document.forms[0].cfoIniciar.value = -1;
             </script>
             <TABLE id ="tablaConf" class="contenidoPestanha" style="width:100%">
                 <tr style="padding-bottom: 15px">
                 <td colspan="3" id="tablaConfiguracionSW"></td>
                 </tr>
                 <tr>
                     <td style="width: 10%" align="right">
                         <div class="etiqueta"><%=descriptor.getDescripcion("cswIniciar")%></div>
                     </td>
                     <td style="width: 75%" align="center">
                         <input type="hidden" name="codIniciarSW">
                         <input type="text" class="inputTextoObligatorio" name="descIniciarSW" size="104" style="width:600px">
                         <A id="anchorIniciarSW" name="anchorIniciarSW"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonIniciarSW" name="botonIniciarSW" style="cursor:hand; border: 0px none;"></span></A>
                     </td>
                     <!-- Boton -->
                     <td style="width: 15%" align="center">
                         <input type= "button" class="botonGeneral" value= "<%=descriptor.getDescripcion("cswconf")%>" name="cmdIniciarConfSW" onclick="pulsarConfigurarSW('iniciar');">
                     </td>
                 </tr>
                 <!-- Avanzar -->
                 <tr> 
                     <td style="width: 10%" align="right">
                         <div class="etiqueta"><%=descriptor.getDescripcion("cswAv")%></div>
                     </td>
                     <td style="width: 75%" align="center">
                         <input type="hidden" name="codAvanzarSW">
                         <input type="text" class="inputTextoObligatorio" name="descAvanzarSW"  size="104" style="width:600px">
                         <A id="anchorAvanzarSW" name="anchorAvanzarSW"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonAvanzarSW" name="botonAvanzarSW" style="cursor:hand; border: 0px none;"></span></A>      
                     </td>
                     <!-- Boton -->
                     <td style="width: 15%" align="center">
                         <input type= "button" class="botonGeneral" value= "<%=descriptor.getDescripcion("cswconf")%>" name="cmdAvanConfSW" onclick="pulsarConfigurarSW('avanzar');">
                     </td>
                 </tr>
                 <!-- Retroceder -->
                 <tr> 
                     <td style="width: 10%" align="right">
                         <div class="etiqueta"><%=descriptor.getDescripcion("cswRet")%></div>
                     </td>
                     <td style="width: 75%" align="center">
                         <input type="hidden" name="codRetrocederSW">
                         <input type="text" class="inputTextoObligatorio" name="descRetrocederSW"  size="104" style="width:600px">
                         <A id="anchorRetrocederSW" name="anchorRetrocederSW"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRetrocederSW" name="botonRetrocederSW" style="cursor:hand; border: 0px none;"></span></A>      
                     </td>
                     <!-- Boton -->
                     <td style="width: 15%" align="center">
                         <input type= "button" class="botonGeneral" value= "<%=descriptor.getDescripcion("cswconf")%>" name="cmdRetConfSW" onclick="pulsarConfigurarSW('retroceder');">
                     </td>
                 </tr>
                 <!-- Tipo operación retroceso -->
                 <tr> 
                     <td style="width: 10%" align="right">
                         <div class="etiqueta"><%=descriptor.getDescripcion("etiqTipoRetroceso")%></div>
                     </td>
                     <td style="width: 75%" align="center">
                        <input type="hidden" name="codTipoRetrocesoSW">
                        <input type="text" class="inputTextoObligatorio" name="descTipoRetrocesoSW"  size="104" style="width:600px">
                        <A id="anchorTipoRetrocesoSW" name="anchorTipoRetrocesoSW"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoRetrocesoSW" name="botonTipoRetrocesoSW" style="cursor:hand; border: 0px none;"></span></A>      
                     </td>
                     <!-- Boton -->
                     <td style="width: 15%">
                         &nbsp;
                     </td>
                 </tr>
                 <tr align="center">
                     <td colspan="3">
                        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaConfSW" onclick="pulsarAltaConfSW();">
                        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%> name="cmdGrabarConfSW" onclick="pulsarGrabarConfSW();">
                        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarConfSW" onclick="pulsarEliminarConfSW();">
                        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiarrConfSW" onclick="limpiarCombosWS();">
                     </td>                                                                                                                  
                 </tr>
             </TABLE>                                                                                                  
         </div>
         <!--- PESTAÑA DE SELECCIÓN DE PLUGIN DE PANTALLA DE TRAMITACION EXTERNA --->
         <div class="tab-page" id="tabPage12" >
             <h2 class="tab"><%=descriptor.getDescripcion("etiqPantTramExterna")%></h2>
             <script type="text/javascript">tp1_p12 = tp1.addTabPage( document.getElementById( "tabPage12" ) );</script>
             <TABLE id ="tablaPantallasTramitacionExterna" class="contenidoPestanha" style="width:100%">
                 <tr>
                     <TD class="columnP" colspan="2">
                        &nbsp;
                     </td>
                 </tr>
                 <tr style="padding-bottom: 10px">
                     <TD class="columnP" width="200px;">
                         <%=descriptor.getDescripcion("etiqPluginTramExterna")%>:
                     </td>
                     <td>
                         <input type="hidden" name="codPluginPantallaTramitacionExterna">
                         <input type="text" class="inputTexto" name="descPluginPantallaTramitacionExterna"  size="104" style="width:600px">
                         <A id="anchorPluginPantallaTramitacionExterna" name="anchorPluginPantallaTramitacionExterna"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonPluginPantallaTramitacionExterna" name="botonPluginPantallaTramitacionExterna" style="cursor:hand; border: 0px none;"></span></A>
                     </td>
                 </tr>
                 <tr style="padding-bottom: 10px">
                     <td class="columnP" width="200px;">
                        <%=descriptor.getDescripcion("etiqURLPantTramExterna")%>:
                     </td>
                     <td>
                         <input type="hidden" name="codUrlPluginPantallaTramitacionExterna">
                         <input type="text" class="inputTexto" name="descUrlPluginPantallaTramitacionExterna"  size="104" style="width:600px">
                         <A id="anchorUrlPluginPantallaTramitacionExterna" name="anchorUrlPluginPantallaTramitacionExterna"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonUrlPluginPantallaTramitacionExterna" name="botonUrlPluginPantallaTramitacionExterna" style="cursor:hand; border: 0px none;"></span></A>
                     </td>
                 </tr>
             </table>
             <table align="center">
             <tr>
                 <td colspan="2"><input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiarPantallasTramExterna" onclick="javascript:limpiarPantallasTramitacionExterna();"/></td>
             </tr>
             </table>
         </div>
     </div>

    <TABLE style="width: 100%;margin-top:35px">
        <TR>
            <TD style="width: 100%;">
                <DIV id="capaNavegacion" name="capaNavegacion" class="dataTables_wrapper"></DIV>
            </TD>
        </TR>
    </TABLE>
  </div>
    <div class="capaFooter">
    <DIV id="capaBotones1" name="capaBotones1" style="display:none" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" onclick="pulsarAlta();">
        <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificar" onclick="pulsarModificar();">
        <input type= "button" class="botonLargo" style="width:130;" value='<%=descriptor.getDescripcion("bEliminarTram")%>' name="cmdEliminarTram"  onclick="pulsarEliminarTramite();">
        <input type= "button" class="botonLargo" style="width:130;" value='<%=descriptor.getDescripcion("bDefProc")%>' name="cmdDefProc"  onclick="pulsarProcedimiento();">
    </DIV>
    <DIV id="capaBotones2" name="capaBotones2"  style="display:none" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarCatalogo"  id="cmdCancelarCatalogo" onclick="pulsarCancelarCatalogo();">
    </DIV>
    <DIV id="capaBotones3" name="capaBotones3"  style="display:none" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabarModificar" onclick="pulsarGrabarModificar();">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarEliminar"  onclick="pulsarCancelarModificar();">
    </DIV>
    <DIV id="capaBotones4" name="capaBotones4"  style="display:none" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%> name="cmdGrabarAlta" onclick="pulsarGrabarAlta();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelarAlta"  onclick="pulsarCancelarAlta();">
    </DIV>
</div>
</html:form>

<script type="text/javascript">

// TABLA DE UNIDADES DE TRAMITE
var tablaUnidadesTramite = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaUnidadesTramite"),770);

tablaUnidadesTramite.addColumna('145','center','');
tablaUnidadesTramite.addColumna('612','center','');
tablaUnidadesTramite.displayCabecera=false;

tablaUnidadesTramite.lineas = new Array();
tablaUnidadesTramite.displayTabla();  

var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('200','center','<%= descriptor.getDescripcion("gEtiq_tramite_cond")%>');
tab.addColumna('700','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tab.displayCabecera=true;

var tabDoc = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDoc'));

tabDoc.addColumna('70','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tabDoc.addColumna('240','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tabDoc.addColumna('170','center','Por interesado');
tabDoc.addColumna('130','center','Firma');
tabDoc.addColumna('70','center','Activo');
tabDoc.addColumna('150','center','<%=m_Config.getString("constante.nombre.relacion")%>/Exped.');
tabDoc.addColumna('0','center','Codigo Visible ');  //Columna necesaria. En la aplicacion definicion de documentos se recupera del oculto ocultoManDocumentosaplicacion que se usa aqui tambien
tabDoc.addColumna('70','center','<%= descriptor.getDescripcion("editorTexto")%>');
<%if(visibleAppExt){%>	
tabDoc.addColumna('70','center','<%=descriptor.getDescripcion("visibleAppExt")%>');
<%}%>	
tabDoc
tabDoc.displayCabecera=true;
tabDoc.displayTabla();


var tabEnlaces = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaEnlaces'));
tabEnlaces.addColumna('400','left','<%=descriptor.getDescripcion("gEtiq_desc")%>');
tabEnlaces.addColumna('400','left','URL');
tabEnlaces.addColumna('100','left','Estado');

tabEnlaces.displayCabecera=true;
tabEnlaces.displayTabla();

var tabCampos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaCampos'));

tabCampos.addColumna('220','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tabCampos.addColumna('600','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tabCampos.addColumna('100','center','Activo');
tabCampos.displayCabecera=true;
tabCampos.displayTabla();

var tabAgrupaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAgrupaciones'));
tabAgrupaciones.addColumna('200','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tabAgrupaciones.addColumna('480','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tabAgrupaciones.addColumna('120','center','<%= descriptor.getDescripcion("getiq_orden")%>');
tabAgrupaciones.addColumna('120','center','<%= descriptor.getDescripcion("getiq_activo")%>');
tabAgrupaciones.displayCabecera=true;
tabAgrupaciones.displayTabla();

function refrescaAgrupaciones(){
    tabAgrupaciones.displayTabla();
}

var tabConfSW = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaConfiguracionSW'));
tabConfSW.addColumna('300','left','<%=descriptor.getDescripcion("cswIniciar")%>');
tabConfSW.addColumna('300','left','<%=descriptor.getDescripcion("cswAv")%>');
tabConfSW.addColumna('300','left','<%=descriptor.getDescripcion("cswRet")%>');

tabConfSW.displayCabecera=true;
tabConfSW.displayTabla();

var comboEstadoEnlace = new Combo("EstUrl");
var comboClasifTramite = new Combo("ClasifTramite");
var comboExpRel = new Combo("ExpRel");
var comboTramiteTabla = new Combo("TramiteTabla");
var comboEstadoTramiteTabla = new Combo("EstadoTramiteTabla");
var comboAvanzar = new Combo("AvanzarSW");
var comboRetroceder = new Combo("RetrocederSW");
var comboCondicionTabla = new Combo("CondicionTabla");
var comboNotCercaFinPlazo=new Combo("NotCercaFinPlazo");
var comboNotFueraDePlazo=new Combo("NotFueraDePlazo");
var comboAdmiteNotifElect=new Combo("AdmiteNotifElect");
var comboIniciar         = new Combo("IniciarSW");
var comboDepartamento    = new Combo("DepartamentoNotificacion");

var comboTipoRetroceder = new Combo("TipoRetrocesoSW");


// Plugin de pantalla de tramitacion externa ==========>
var comboUrlTramitacionExterna = new Combo("UrlPluginPantallaTramitacionExterna");
var comboTramitacionExterna = new Combo("PluginPantallaTramitacionExterna");
var pluginPantallaTramitacionExterna = new Array();
var urlPluginPantallasTramitacionExterna = new Array();
var contPantallas = 0;

var codigosPlugin = new Array();
var descripcionPlugin = new Array();
<logic:iterate name="DefinicionTramitesForm" property="pluginPantallasTramitacionExterna" id="pantalla">
    var codAux = '<bean:write name="pantalla" property="nombrePlugin" ignore="true"/>';
    
    pluginPantallaTramitacionExterna[contPantallas] = ['<bean:write name="pantalla" property="nombrePlugin" ignore="true"/>','<bean:write name="pantalla" property="descripcionPluginDefinicionTramite" ignore="true"/>','<bean:write name="pantalla" property="implClass" ignore="true"/>'];                                                       
    codigosPlugin[contPantallas] = '<bean:write name="pantalla" property="nombrePlugin" ignore="true"/>';
    descripcionPlugin[contPantallas] = '<bean:write name="pantalla" property="descripcionPluginDefinicionTramite" ignore="true"/>';
    
    var contadorEnlaces = 0;
    <logic:iterate name="pantalla" property="enlaces" id="enlace">        
        urlPluginPantallasTramitacionExterna[contadorEnlaces] = [codAux,'<bean:write name="enlace" property="url" ignore="true"/>','<bean:write name="enlace" property="idEnlaceConfiguracion" ignore="true"/>'];
        contadorEnlaces++;
    </logic:iterate>
    contPantallas++;
</logic:iterate>

comboTramitacionExterna.addItems(codigosPlugin,descripcionPlugin);
comboTramitacionExterna.change = onChangeComboTramitacionExterna;

var codPluginSeleccionado	    = '<bean:write name="DefinicionTramitesForm" property="codPluginPantallaTramitacionExterna" ignore="true"/>';
var urlPluginSeleccionado		= '<bean:write name="DefinicionTramitesForm" property="urlPluginPantallaTramitacionExterna" ignore="true"/>';
var implClassPluginSeleccionado = '<bean:write name="DefinicionTramitesForm" property="implClassPluginPantallaTramitacionExterna" ignore="true"/>';

// Se comprueba si hay un plugin de tramitacion externa ya seleccionado para el tramite

if(codPluginSeleccionado!=null && codPluginSeleccionado!="" &&
   urlPluginSeleccionado!=null && urlPluginSeleccionado!="" && implClassPluginSeleccionado!="" && implClassPluginSeleccionado!=null && implClassPluginSeleccionado!=""){

   
   for(i=0;i<pluginPantallaTramitacionExterna.length;i++){
       if(codPluginSeleccionado==pluginPantallaTramitacionExterna[i][0]){   
           break;
       }
   }
   
   document.forms[0].implClassPluginPantallaTramExterna.value = implClassPluginSeleccionado;
   var posicion = i+1;   
   comboTramitacionExterna.selectItem(posicion);
   onChangeComboTramitacionExterna();
   
}//if


// ======== TIPO DE OPERACION DE RETROCESO DE TRÁMITE =====>
var codTiposRetroceso = new Array();
codTiposRetroceso[0] = -1;
codTiposRetroceso[1] = 1;
codTiposRetroceso[2] = 2;

var descripcionesTiposRetroceso = new Array();

descripcionesTiposRetroceso[0] = '';
descripcionesTiposRetroceso[1] = '<%=descriptor.getDescripcion("tipRetrocesoTramCerrado")%>';
descripcionesTiposRetroceso[2] = '<%=descriptor.getDescripcion("tipRetrocesoTramAbierto")%>';

comboTipoRetroceder.addItems(codTiposRetroceso,descripcionesTiposRetroceso);



// <======== TIPO DE OPERACION DE RETROCESO DE TRÁMITE =====
// 
// Plugin de pantalla de tramitacion externa ==========>
var contDepto = 0;
<logic:iterate name="DefinicionTramitesForm" property="listaDepartamentosNotificacion" id="departamento">
    var codigo      = '<bean:write name="departamento" property="codigo" ignore="true"/>';
    var descripcion = '<bean:write name="departamento" property="descripcion" ignore="true"/>';
    codigosDepartamentos[contDepto] = [codigo];
    descripcionesDepartamentos[contDepto] = [descripcion];
    contDepto++;
</logic:iterate>



comboDepartamento.addItems(codigosDepartamentos,descripcionesDepartamentos);
// <====== DEPARTAMENTOS NOTIFICACION ====

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
function checkKeysLocal(evento,tecla) {

}

comboCondicionTabla.change = function() { 
  if(comboCondicionTabla.des.value.length != 0) {
      
    if(document.forms[0].descCondicionTabla.value == "TRÁMITE") {      
        comboTramiteTabla.activate();
        comboEstadoTramiteTabla.activate();
		comboTramiteTabla.clearItems();
		comboEstadoTramiteTabla.clearItems();		
		comboTramiteTabla.addItems(codigo_tramiteTabla,nombre_tramiteTabla);
		comboEstadoTramiteTabla.addItems(estado_tramiteTabla,estado_tramiteTabla);		
	} else if(document.forms[0].descCondicionTabla.value == etiqFirmaDoc) {            
        comboTramiteTabla.activate();
        comboEstadoTramiteTabla.activate();	
		comboTramiteTabla.clearItems();
		comboEstadoTramiteTabla.clearItems();
		comboTramiteTabla.addItems(codigo_documentoPresentado,nombre_documentoPresentado);
		comboEstadoTramiteTabla.addItems(estado_documentoPresentado,estado_documentoPresentado);				
	} else{            
        comboTramiteTabla.selectItem(-1);
        comboTramiteTabla.deactivate();
        comboEstadoTramiteTabla.selectItem(-1);
        comboEstadoTramiteTabla.deactivate();
    }
  }
}


function actualizarListadoPluginTramitacionExterna(pluginSeleccionado,urlSeleccionado,classSeleccionado,listaPlugin,listaUrl,listaCodigosPlugin,listaDescripcionesPlugin){

    limpiarPantallasTramitacionExterna();    
    codPluginSeleccionado                               = pluginSeleccionado;
    urlPluginSeleccionado                               = urlSeleccionado;
    document.forms[0].implClassPluginPantallaTramExterna.value = classSeleccionado;
    implClassPluginSeleccionado                         = classSeleccionado;
    pluginPantallaTramitacionExterna                    = listaPlugin;
    urlPluginPantallasTramitacionExterna                = listaUrl;

    comboTramitacionExterna.addItems(listaCodigosPlugin,listaDescripcionesPlugin);
    comboTramitacionExterna.change = onChangeComboTramitacionExterna;

    
    // Se comprueba si hay un plugin de tramitacion externa ya seleccionado para el tramite
    if(codPluginSeleccionado!=null && codPluginSeleccionado!="" &&
       urlPluginSeleccionado!=null && urlPluginSeleccionado!="" && implClassPluginSeleccionado!="" && implClassPluginSeleccionado!=null && implClassPluginSeleccionado!=""){

       for(i=0;i<pluginPantallaTramitacionExterna.length;i++){
           if(codPluginSeleccionado==pluginPantallaTramitacionExterna[i][0]){
               break;
           }
       }
       document.forms[0].implClassPluginPantallaTramExterna.value = implClassPluginSeleccionado;
       var posicion = i+1;
       comboTramitacionExterna.selectItem(posicion);
       onChangeComboTramitacionExterna();

    }//if

}//if


function onChangeComboTramitacionExterna(){ 
    var indice = comboTramitacionExterna.selectedIndex;    
    if(indice!=-1){
        var codPluginSeleccionado 		   = document.forms[0].codPluginPantallaTramitacionExterna.value;
        var descripcionPluginSeleccionado = document.forms[0].descPluginPantallaTramitacionExterna.value;

        if(indice==0 && (codPluginSeleccionado==null || codPluginSeleccionado=="") && (descripcionPluginSeleccionado==null || descripcionPluginSeleccionado=="")){
           limpiarPantallasTramitacionExterna();
        }else{
            var urls = new Array();
            var codigosUrls = new Array();
            var contador = 0;
            var codigo;

            for(i=0;urlPluginPantallasTramitacionExterna!=null && i<urlPluginPantallasTramitacionExterna.length;i++){
               codigo = urlPluginPantallasTramitacionExterna[i][0];
               if(codigo == codPluginSeleccionado){
                   //codigosUrls[contador] = contador + "-" + urlPluginPantallasTramitacionExterna[i][0];
                   codigosUrls[contador] = urlPluginPantallasTramitacionExterna[i][2];
                   urls[contador] = urlPluginPantallasTramitacionExterna[i][1];
                   contador++;
               }// if

            }// for

            var implClass = "";
            for(i=0;i<pluginPantallaTramitacionExterna.length;i++){
                if(pluginPantallaTramitacionExterna[i][0]==codigo){
                   implClass = pluginPantallaTramitacionExterna[i][2];
                }
            }

            document.forms[0].implClassPluginPantallaTramExterna.value=implClass;
            // Se busca la clase que implementa el plugin

            if(comboUrlTramitacionExterna!=null && comboUrlTramitacionExterna!=undefined){

               comboUrlTramitacionExterna.addItems(codigosUrls,urls);
               if(codPluginSeleccionado!=null && codPluginSeleccionado!="" && urlPluginSeleccionado!=null && urlPluginSeleccionado!="" && implClassPluginSeleccionado!="" && implClassPluginSeleccionado!=null){
                   for(i=0;i<urls.length;i++){
                       if(urls[i]==urlPluginSeleccionado){

                           break;
                       }
                   }// for
                   comboUrlTramitacionExterna.selectItem(i+1);
               }// if
               else // Se comprueba si hay una sóla url para el plugin, porque entonces se carga esta
               if(urlPluginPantallasTramitacionExterna!=null && urlPluginPantallasTramitacionExterna.length==1){

                   comboUrlTramitacionExterna.selectItem(1);
               }
            }//if

       }// else
   }
}

</script>
<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
<script>

    function rellenarDatos(tableObject, rowID){
      tabAux = tableObject;
      if(tableObject==tab){
        listaAux = listaTablaEntrada;
        if(document.forms[0].cmdAltaTablaEntrada.disabled == false) {
          if((rowID>-1)&&(!(tableObject.ultimoTable))){
                    
            if (listaTablaEntradaOriginal[rowID][1] == "TRÁMITE"){
                comboTramiteTabla.clearItems();
                comboEstadoTramiteTabla.clearItems();						
                comboTramiteTabla.addItems(codigo_tramiteTabla,nombre_tramiteTabla);
                comboEstadoTramiteTabla.addItems(estado_tramiteTabla,estado_tramiteTabla);		
				
                comboCondicionTabla.buscaLinea(listaTablaEntradaOriginal[rowID][1]);                
                comboTramiteTabla.buscaLinea(listaTablaEntradaOriginal[rowID][2]);               
                comboEstadoTramiteTabla.buscaLinea(listaTablaEntradaOriginal[rowID][4]);
                comboTramiteTabla.activate();
                comboEstadoTramiteTabla.activate();				
				
            }else        
             if (listaTablaEntradaOriginal[rowID][1] == etiqFirmaDoc){                                           
				
                comboCondicionTabla.buscaLinea(listaTablaEntradaOriginal[rowID][1]);                
                				
                var indiceDocPresentado = buscarDocumentoPresentado(listaTablaEntradaOriginal[rowID][6]);
                indiceEstado = buscarEstadoDocumento(listaTablaEntradaOriginal[rowID][4]);

                // Se vacían los combos
                comboTramiteTabla.clearItems();
                comboEstadoTramiteTabla.clearItems();
                // Se añaden los elementos de cada uno teniendo en cuenta que se trata de una 
                // condición de tipo documento
                comboTramiteTabla.addItems(codigo_documentoPresentado,nombre_documentoPresentado);
                comboEstadoTramiteTabla.addItems(estado_documentoPresentado,estado_documentoPresentado);				

                comboTramiteTabla.selectItem(indiceDocPresentado);
                comboEstadoTramiteTabla.selectItem(indiceEstado);                

                comboTramiteTabla.activate();
                comboEstadoTramiteTabla.activate();				
            } 
            else { 
                comboCondicionTabla.buscaLinea(listaTablaEntradaOriginal[rowID][1]);
                comboTramiteTabla.selectItem(-1);
                comboEstadoTramiteTabla.selectItem(-1);
                comboTramiteTabla.deactivate();
                comboEstadoTramiteTabla.deactivate();
            }
            
          }else borrarDatosTablaEntrada();
        }

      }else if(tableObject==tabEnlaces){
        listaAux = listaEnlaces;
        if(document.forms[0].cmdAltaEnlaces.disabled == false) {
          if((rowID>-1)&&(!(tableObject.ultimoTable))){
            document.forms[0].txtDescUrl.value = listaEnlaces[rowID][0];
            document.forms[0].txtUrl.value = listaEnlaces[rowID][1];
            if(listaEnlaces[rowID][2] == "1") comboEstadoEnlace.buscaLinea('<%=descriptor.getDescripcion("etiq_si")%>');
            else comboEstadoEnlace.buscaLinea('<%=descriptor.getDescripcion("etiq_no")%>');
          }else borrarDatosEnlaces();
        }
      }
    }
	
function buscarDocumentoPresentado(doc){        
    var indice=-1;
    for(i=0;codigo_documentoPresentado!=null && i<codigo_documentoPresentado.length;i++){
        if(codigo_documentoPresentado[i]==doc){
            indice = i;
            break;
        }
    }

    return indice;
}
	
	
	
function buscarEstadoDocumento(estado){        
    var indice=-1;
    for(i=0;estado_documentoPresentado!=null && i<estado_documentoPresentado.length;i++){
        if(estado_documentoPresentado[i]==estado){
            indice = i;
            break;
        }
    }
    return indice;
}
function pulsarVerExterno(nombre)
{  
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;       
    var valor = eval("document.forms[0]."+nombre+".value");    
    
    abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mostrarExterno.jsp'/>?codMunicipio=" + escape(codMunicipio) + 
            "&codProcedimiento='" + escape(codProcedimiento) + "'&nombreCampo='" + nombre+"'&valor_dato='"+valor+"'",'',
            'width=700,height=150,scrollbars=no',function(result){
                        if (result != "" && result != null){            
                            eval("document.forms[0]."+nombre+".value='"+result+"'");
                        }        
                });
}
    
</script>
</BODY>
</html:html>
