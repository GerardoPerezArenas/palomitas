<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DatosPantallaModuloVO"%>
<%@ page import="java.util.Date"%>
<%@ page import="es.altia.agora.technical.Fecha" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
 
<TITLE>::: EXPEDIENTES  Definición de Procedimientos:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }
  String municipio = Integer.toString(munic);
  String aplicacion = Integer.toString(apl);  
  // SE COMPRUEBA SI SE ESTÁ IMPORTANDO UN PROCEDIMIENTO DE UN ENTORNO REAL O PRODUCCIÓN AL ENTORNO DE PRUEBAS
  String codMunicipioProcImportar  = (String)request.getAttribute("codMunicipioProcImportar");
  String codAplicacionProcImportar = (String)request.getAttribute("codAplicacionProcImportar");
  String importarProcConEleccion       = (String)request.getAttribute("importarProcConEleccion");
  if(importarProcConEleccion!=null && "SI".equalsIgnoreCase(importarProcConEleccion) &&  !"".equals(codMunicipioProcImportar) && !"".equals(codAplicacionProcImportar)){
        municipio  = codMunicipioProcImportar;
        aplicacion = codAplicacionProcImportar;
  }

  String deTramite = "";
  Log m_log = LogFactory.getLog(this.getClass().getName());
  if(m_log.isDebugEnabled()) m_log.debug("la opcion en la jsp cogida de la session es : " + session.getAttribute("deTramite"));
  if(session.getAttribute("deTramite")!=null) {
          deTramite = (String) session.getAttribute("deTramite");
          session.removeValue("deTramite");
  }
  boolean hayBuscada=false;

  if (session.getAttribute("modoInicio") != null) {
      if ("recargar_buscada".equals((String) session.getAttribute("modoInicio"))){
        hayBuscada=true;
      }
      session.removeValue("modoInicio");
  }
    String importacionBiblioteca = "";
    if(request.getAttribute("importacionBiblioteca") != null)
        importacionBiblioteca = (String)request.getAttribute("importacionBiblioteca");

  Fecha f=new Fecha();
  Date fSistema = new Date();
  String fechaHoy = f.obtenerString(fSistema);
  String idSesion = session.getId();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");

    /** Se averigua cual es el navegador usado por el usuario **/
    String userAgent = request.getHeader("user-agent");

    String CargaCapaHistorico = (String)request.getAttribute("comprobacionHistoricoActiva");                        
        
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>     
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/xtree.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/WebFXImageTreeItem.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/JavaScriptUtil.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/definicionProcedimientos.js'/>"></script>
<SCRIPT type="text/javascript">
    dojo.require("dojo.io.*");
    dojo.require("dojo.event.*");
    
   var importarProcConEleccion  = "<%=importarProcConEleccion%>";
    var codOrganizacionUsuario = "<%=munic%>";
    var codMunicipioProcImportar = "<%=codMunicipioProcImportar%>";

// Listas de valores.
var cod_camposSupl = new Array();
var desc_camposSupl = new Array();
var cod_tipoProcedimiento = new Array();
var desc_tipoProcedimiento = new Array();
var cod_area = new Array();
var desc_area = new Array();
var tipo_oculto = new Array();
var list = new Array();
var listaDoc = new Array();
var listaDocOriginal = new Array();
var listaUnidadesInicio = new Array();
var listaUnidadesInicioOriginal = new Array();
var listaCampos = new Array();
var listaCamposOriginal = new Array();
var listaEnlaces = new Array();
var listaEnlacesOriginal = new Array();
var listaRoles = new Array();
var listaRolesOriginal = new Array();
var listaCamposSeleccionados = new Array();
var listaAgrupaciones = new Array();
var listaAgrupacionesOriginal = new Array();

/** prueba **/
var listaCodigosServiciosFinalizacion = new Array();
var listaDescripcionesServiciosFinalizacion = new Array();
var listaImplClassServiciosFinalizacion = new Array();




/** prueba **/


var radioButon = "";
var cont=0;
var cont1=0;
var cont2=0;
var cont3 = 0;
var cont4 = 0;
var cont5 = 0;
var cont6 = 0;
var cont7 = 0;
var cont8 = 0;
var codMunic = new Array();
var codProc = new Array();
var numProcedimientos;
var procedimientoActual = 1;
var consultando = false;
var operadorConsulta = '|&:<>!=';

var tabCamposPendientes;
var tabCamposSeleccionados;


function cargarDatosPluginHistorico(){         
    var codigo = '<%=request.getAttribute("listaCodigo")%>';
    var desc = '<%=request.getAttribute("listaDesc")%>';                
    var listaCodigo = codigo.split(";");
    var listaDesc = desc.split(";");
    var tempo = new Array();
    
    <%if ("SI".equals(CargaCapaHistorico)){%>  
        var comboClaseHist = new Combo("ClaseHist");                                    
        if ("null" == codigo){                    
            comboClaseHist.addItems(tempo,tempo);  
        }else{
           comboClaseHist.addItems(listaCodigo,listaDesc);  
        }
     <%}%>
}

function recargarComboHistorico(){    
    <%if ("SI".equals(CargaCapaHistorico)){%>
        var codigo = '<%=request.getAttribute("listaCodigo")%>';            
        var clase = '<%=request.getAttribute("Listaclase")%>';     

        var listaCodigo = codigo.split(";");
        var listaclase = clase.split(";");

        if (document.forms[0].codClaseHist.value == ""){
            document.forms[0].claseBuzonEntradaHistorico.value = "";
        }else{        
            for (i=0; i < listaCodigo.length;i++){
                if (listaCodigo[i] == document.forms[0].codClaseHist.value){
                    document.forms[0].claseBuzonEntradaHistorico.value = listaclase[i];
                    encontrado = true;
                }
            }
        }
   <%}%>
       
}
// JAVASCRIPT DE LA PESTAÑA DOCUMENTOS

function  inicializarBotonesRadio(){
  with (document.forms[0]){
  codEstado[0].checked=true;
  cqUnidadInicio[1].checked=true;
  codTipoInicio[0].checked=true;
  localizacion[0].checked=true;
  tipoPlazo[0].checked=false;
  tipoPlazo[1].checked=false;
  tipoPlazo[2].checked=false;
  }
}

function inicializar() {  
    <% if(importacionBiblioteca.equals("si")){ %>
        jsp_alerta("A","<%= descriptor.getDescripcion("msgExitoImpFlujo")%>");
    <% } %>
  window.focus();
  tabUnidades.readOnly = true;
  tp1.setSelectedIndex(0);
  obligatorioToNoObligatorio(document.forms[0]);
  activarFormulario();
  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  desactivarBotonesDoc();
  desactivarBotonesCampo();
  desactivarBotonesEnlaces();
  desactivarBotonesRoles();
    
  document.forms[0].codMunicipio.value = <%= municipio %>;
  document.forms[0].codAplicacion.value = <%= aplicacion %>;

<% if("deTramite".equals(deTramite)){ %>    
    var restringido = '<bean:write name="DefinicionProcedimientosForm" property="restringido"/>';
    var biblioteca = '<bean:write name="DefinicionProcedimientosForm" property="biblioteca"/>';
    var numeracionExp = '<bean:write name="DefinicionProcedimientosForm" property="numeracionExpedientesAnoAsiento"/>';
    
    // Se comprueba si el procedimiento esta o no restringido
    if(restringido!=undefined && restringido=="1"){
        document.forms[0].restringido.checked = true;
        document.forms[0].restringido.value='1';
    }else{
        document.forms[0].restringido.checked = false;
        document.forms[0].restringido.value='0';
    }
    
    /* envia al form el valor del campo biblioteca/libreria de flujo del procedimiento*/
    if(biblioteca!=undefined && biblioteca=="1"){
        // Se comprueba si el procedimiento esta o no restringido
        document.forms[0].biblioteca.checked = true;
        document.forms[0].biblioteca.value='1';
        // Como el procedimiento es una biblioteca de flujos, habrá que ocultar el botón [Biblioteca]
        document.forms[0].cmdBiblioteca.style.display='none';
    }else{
        document.forms[0].biblioteca.checked = false;
        document.forms[0].biblioteca.value='0';
    }
    
    // #303601 envia al form el valor del campo numeracionExpedientesAnoAsiento (caso al cargar el procedimiento desde el catalogo)
    if(numeracionExp!=undefined && numeracionExp=="1"){
        // Se comprueba si el procedimiento esta o no restringido
        document.forms[0].numeracionExpedientesAnoAsiento.checked = true;
        document.forms[0].numeracionExpedientesAnoAsiento.value='1';
    }else{
        document.forms[0].numeracionExpedientesAnoAsiento.checked = false;
        document.forms[0].numeracionExpedientesAnoAsiento.value='0';
    }
    
    document.forms[0].noModificar.value = "si";
    var disp = '<bean:write name="DefinicionProcedimientosForm" property="disponible"/>';
    if(disp == 1) {
      document.forms[0].disponible.checked = true;
    } else {
      document.forms[0].disponible.checked = false;
    }
  var tramInt = '<bean:write name="DefinicionProcedimientosForm" property="tramitacionInternet"/>';
  if(tramInt == 1) {
    document.forms[0].tramitacionInternet.checked = true;
  } else {
    document.forms[0].tramitacionInternet.checked = false;
  }
  var intOblig = '<bean:write name="DefinicionProcedimientosForm" property="interesadoOblig"/>';
  if(intOblig == 1) {
    document.forms[0].interesadoOblig.checked = true;
  } else {
    document.forms[0].interesadoOblig.checked = false;
  }
  
  var ClaseBuzonHist = '<bean:write name="DefinicionProcedimientosForm" property="claseBuzonEntradaHistorico"/>'; 
  var codigo = '<%=request.getAttribute("listaCodigo")%>';
  var desc = '<%=request.getAttribute("listaDesc")%>';
  var clase = '<%=request.getAttribute("Listaclase")%>';     
        
  var listaCodigo = codigo.split(";");
  var listaDesc = desc.split(";");
  var listaclase = clase.split(";");
  <%if ("SI".equals(CargaCapaHistorico)){%>
    document.forms[0].codClaseHist.value = "";
    document.forms[0].descClaseHist.value = "";
    document.forms[0].claseBuzonEntradaHistorico.value = "";
    for (i=0; i < listaCodigo.length;i++){
          if (listaclase[i] == ClaseBuzonHist){                
                document.forms[0].codClaseHist.value = listaCodigo[i];
                document.forms[0].descClaseHist.value = listaDesc[i];
                document.forms[0].claseBuzonEntradaHistorico.value = listaclase[i];                            
          }            
    }            
    <%}%> 

  var sWS = '<bean:write name="DefinicionProcedimientosForm" property="soloWS"/>';

  if(sWS == 1) {
    document.forms[0].soloWS.checked = true;
  } else {
    document.forms[0].soloWS.checked = false;
  }
  
    var cqUnidadInicio = "<bean:write name="DefinicionProcedimientosForm" property="cqUnidadInicio"/>";
    if (cqUnidadInicio=="1") document.forms[0].cqUnidadInicio[0].checked=true;
    else if (cqUnidadInicio=="0") document.forms[0].cqUnidadInicio[1].checked=true;

  cont3=0;
  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="tramites">
    list[cont3] = ['<bean:write name="elemento" property="codMunicipio" />',
                  '<bean:write name="elemento" property="txtCodigo"/>',
                  '<bean:write name="elemento" property="codigoTramite"/>',
                  '<bean:write name="elemento" property="numeroTramite"/>',
                  '<bean:write name="elemento" property="nombreTramite"/>',
                  '<bean:write name="elemento" property="codClasificacionTramite"/>',
                  '<bean:write name="elemento" property="descClasificacionTramite"/>',
                  ];
    cont3++;
  </logic:iterate>
  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listasDoc">
  var tieneFirmas = '<bean:write name="elemento" property="requiereFirma" />';
                var imagenFirma = "";
                if (tieneFirmas == "1") {
                    imagenFirma = '<span class="fa fa-check 2x"></span>';
                } else {
                    imagenFirma = '<span class="fa fa-close 2x"></span>';
                }
                
    listaDoc[cont1] = [ /* Datos documentos de procedimiento */
            '<bean:write name="elemento" property="nombreDocumento"/>',
                    '<bean:write name="elemento" property="condicion" />',
                    imagenFirma];
    cont1++;
  </logic:iterate>
  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listasDoc">
    listaDocOriginal[cont2] = [	/* Datos documentos de procedimiento */
                '<bean:write name="elemento" property="codigoDocumento" />',
                '<bean:write name="elemento" property="nombreDocumento"/>',
                '<bean:write name="elemento" property="condicion" />',
                '<bean:write name="elemento" property="requiereFirma" />'
                ];
    cont2++;
  </logic:iterate>
  tabDoc.lineas = listaDoc;
  refrescaDoc();
  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="tablaUnidadInicio">
    listaUnidadesInicio[cont4] = ['<bean:write name="elemento" property="codVisibleUnidadInicio" />',
                                  '<bean:write name="elemento" property="descUnidadInicio"/>'];
    listaUnidadesInicioOriginal[cont4] = ['<bean:write name="elemento" property="codUnidadInicio" />',
                                          '<bean:write name="elemento" property="descUnidadInicio"/>'];
    cont4++;
  </logic:iterate>
  tabUnidades.lineas=listaUnidadesInicio;
  refrescaUnidades();

  var lUI = crearListas();
  document.forms[0].listaCodUnidadesInicio.value = lUI[0];
  document.forms[0].listaDescUnidadesInicio.value = lUI[1];
  document.forms[0].listaCodVisibleUnidadesInicio.value = lUI[2];
  if(listaUnidadesInicio.length >0) {
    document.forms[0].codUnidadInicio.value = listaUnidadesInicio[0][0];
    document.forms[0].descUnidadInicio.value = listaUnidadesInicio[0][1];
  } else {
    document.forms[0].codUnidadInicio.value = "";
    document.forms[0].descUnidadInicio.value = "";
  }

  var campoAct = "";
  var campoImg = "";
  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaCampos">
    campoAct = '<bean:write name="elemento" property="activo" />';
    if (campoAct == "SI") {
        campoImg = '<span class="fa fa-check 2x"></span>';
    } else {
        campoImg = '<span class="fa fa-close 2x"></span>';
    }
    listaCampos[cont5] = ['<bean:write name="elemento" property="codCampo" />',
                          '<bean:write name="elemento" property="descCampo" />',
                          campoImg];
    listaCamposOriginal[cont5] =['<bean:write name="elemento" property="codCampo" />','<bean:write name="elemento" property="descCampo" />',
                                 '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="codTipoDato" />',
                                 '<bean:write name="elemento" property="tamano" />','<bean:write name="elemento" property="descMascara" />',
                                 '<bean:write name="elemento" property="obligat" />','<bean:write name="elemento" property="orden" />',
                                 '<bean:write name="elemento" property="descPlantilla" />','<bean:write name="elemento" property="descTipoDato" />',
                                 '<bean:write name="elemento" property="rotulo" />','<bean:write name="elemento" property="activo" />',
                                 '<bean:write name="elemento" property="oculto" />','<bean:write name="elemento" property="bloqueado" />',
                                 '<bean:write name="elemento" property="plazoFecha" />','<bean:write name="elemento" property="checkPlazoFecha" />',
                                 '<bean:write name="elemento" property="validacion" />','<bean:write name="elemento" property="operacion" />',
                                 '<bean:write name="elemento" property="codAgrupacion" />','<bean:write name="elemento" property="posX" />',
                                 '<bean:write name="elemento" property="posY" />'];
    cont5++;
  </logic:iterate>
  tabCampos.lineas = listaCampos;
  refrescaCampos();
  
  campoAct = "";
    campoImg = "";
    <logic:iterate id="agrupacion" name="DefinicionProcedimientosForm" property="listaAgrupaciones">
        campoAct = '<bean:write name="agrupacion" property="agrupacionActiva" />';
        if (campoAct == "SI") {
            campoImg = '<span class="fa fa-check 2x"></span>';
        } else {
            campoImg = '<span class="fa fa-close 2x"></span>';
        }
        listaAgrupaciones[cont8] = ['<bean:write name="agrupacion" property="codAgrupacion" />', 
                '<bean:write name="agrupacion" property="descAgrupacion" />', '<bean:write name="agrupacion" property="ordenAgrupacion" />',
                campoImg];
        listaAgrupacionesOriginal[cont8] = ['<bean:write name="agrupacion" property="codAgrupacion" />', 
                '<bean:write name="agrupacion" property="descAgrupacion" />', '<bean:write name="agrupacion" property="ordenAgrupacion" />',
                '<bean:write name="agrupacion" property="agrupacionActiva" />'];
        cont8++;
    </logic:iterate>
    tabAgrupaciones.lineas = listaAgrupaciones;
    refrescaAgrupaciones();
  
  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaEnlaces">
    listaEnlaces[cont6] = ['<bean:write name="elemento" property="descEnlace" />',
                           '<bean:write name="elemento" property="urlEnlace" />',
                           '<bean:write name="elemento" property="estadoEnlace" />'];
    listaEnlacesOriginal[cont6] =['<bean:write name="elemento" property="codEnlace" />',
                                  '<bean:write name="elemento" property="descEnlace" />',
                                  '<bean:write name="elemento" property="urlEnlace" />',
                                  '<bean:write name="elemento" property="estadoEnlace" />'];
    cont6++;
  </logic:iterate>
  tabEnlaces.lineas = listaEnlaces;
  refrescaEnlaces();



  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaRoles">
    var pD = '<bean:write name="elemento" property="rolPorDefecto" />';
    var marca="";
    if(pD == 1) {
      marca = "X";
    }

    var consultaWeb = '<bean:write name="elemento" property="consultaWebRol" />';
    var marcaWeb = "";
    if(consultaWeb==1)
        marcaWeb = "X";

    listaRoles[cont7] = ['<bean:write name="elemento" property="codRol" />',
                           '<bean:write name="elemento" property="descRol" />',
                           marca,marcaWeb];
    listaRolesOriginal[cont7] =['<bean:write name="elemento" property="codRol" />',
                                '<bean:write name="elemento" property="descRol" />',
                                '<bean:write name="elemento" property="rolPorDefecto" />','<bean:write name="elemento" property="consultaWebRol" />'];
    cont7++;
  </logic:iterate>
  tabRoles.lineas = listaRoles;
  refrescaRoles();

  pintaTramitesProcedimiento(list);
  desactivarFormulario();
  if(document.forms[0].importar.value == "si") {
    mostrarCapasBotones('capaBotones5');
  } else if(document.forms[0].deCatalogo.value == "si") {
    mostrarCapasBotones('capaBotones6');
    document.forms[0].cmdCancelarCatalogo.disabled = false;
  } else {
    mostrarCapasBotones('capaBotones2');
  }
<% } else if(hayBuscada){ %>
  consultando = true;
  recargaConsulta();
<% } else { %>
  inicializarBotonesRadio();
  mostrarCapasBotones('capaRadioButtons');
  mostrarCapasBotones('capaBotones1');
  consultando = true;
<% } %>

  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaTiposProcedimientos">
    cod_tipoProcedimiento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_tipoProcedimiento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
  </logic:iterate>

  <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaArea">
    cod_area['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_area['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
  </logic:iterate>

  var estado_enlaces = new Array('<%=descriptor.getDescripcion("etiq_si")%>','<%=descriptor.getDescripcion("etiq_no")%>');
  comboArea.addItems(cod_area,desc_area);
  comboTipoProcedimiento.addItems(cod_tipoProcedimiento,desc_tipoProcedimiento);
  comboEstadoEnlace.addItems(estado_enlaces,estado_enlaces);


  <%
    if("deTramite".equals(deTramite))
    {
  %>            
        var codigoServicioFin    = '<bean:write name="DefinicionProcedimientosForm" property="codServicioFinalizacion" ignore="true"/>';
        var implClassServicioFin = '<bean:write name="DefinicionProcedimientosForm" property="implClassServicioFinalizacion" ignore="true"/>';        
        seleccionarPluginServicioFinalizacion(codigoServicioFin,implClassServicioFin);        
  <%
    }
  %>        
}

function crearListas() {
  var listaCodUnidadesInicio = "";
  var listaCodVisibleUnidadesInicio = "";
  var listaDescUnidadesInicio = "";
  var listas = new Array();
  for (i=0; i < listaUnidadesInicio.length; i++) {
    listaCodUnidadesInicio += listaUnidadesInicioOriginal[i][0]+'§¥';
    listaCodVisibleUnidadesInicio += listaUnidadesInicio[i][0]+'§¥';
    listaDescUnidadesInicio += listaUnidadesInicioOriginal[i][1]+'§¥';
  }
  listas = [listaCodUnidadesInicio,listaDescUnidadesInicio,listaCodVisibleUnidadesInicio];
  return listas;
}
function mostrarCalFechaLimiteDesde(event) {
    if(window.event) event = window.event;
    if (document.getElementById("calFechaLimiteDesde").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaLimiteDesde',null,null,null,'','calFechaLimiteDesde','',null,null,null,null,null,null,null,'comprobarFechaVigencia()',event);
}
function mostrarCalFechaLimiteHasta(event) {
    if(window.event) event = window.event;
    if (document.getElementById("calFechaLimiteHasta").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaLimiteHasta',null,null,null,'','calFechaLimiteHasta','',null,null,null,null,null,null,null,'comprobarFechaVigencia()',event);
}
function pulsarSalir() {
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
  document.forms[0].submit();
}
function pulsarCancelarBuscar() { 
  clean();
    
  if(importarProcConEleccion!=null && (importarProcConEleccion=="si" || importarProcConEleccion=="SI")){
	document.forms[0].codMunicipio.value = codOrganizacionUsuario;
  }

  document.forms[0].listadoImportar.value = "";
  document.forms[0].txtCodigo.value = "";
  document.forms[0].txtDescripcion.value = "";
    document.forms[0].descripcionBreve.value = "";
  tp1.setSelectedIndex(0);
  mostrarCapasBotones('capaRadioButtons');
  mostrarCapasBotones('capaBotones1');
  domlay('capaNavegacionConsulta',0,0,0,'');
  activarFormulario();
  desactivarBotonesDoc();
  desactivarBotonesCampo();
  desactivarBotonesEnlaces();
  desactivarBotonesRoles();

  document.forms[0].plazo.value= "";
  document.forms[0].tipoPlazo[0].value= "";
  document.forms[0].tipoPlazo[1].value= "";
  document.forms[0].tipoPlazo[2].disable= true;
  document.forms[0].porcentaje.value= "";
  obligatorioToNoObligatorio(document.forms[0]);
  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  consultando = true;
  document.forms[0].tipoConsulta[0].checked=true;
  inicializarBotonesRadio();
  document.getElementById("capaBotonesNuevaConsulta").style.display = 'none';
  
  //Vacío de las variables javascript y campos de formulario utilizados en la vista de campos pendientes
  listaCamposSeleccionados = new Array();
  // Se actualiza la lista a partir de la cual se saben cuales son los campos a grabar
  document.forms[0].listaCodigosCamposPendientes.value  = "";
  document.forms[0].listaNombresCamposPendientes.value  = "";
  document.forms[0].listaTamanhoCamposPendientes.value  = "";
  document.forms[0].listaOrdenCamposPendientes.value    = "";
  document.forms[0].listaCampoSupCamposPendientes.value = "";						  
  tabPendientes.lineas = listaCamposSeleccionados;
  tabPendientes.displayTabla();
  desactivarBotonesVistaPendientes();
  //campos pendientes
  vaciarMsgPorcentaje();  
  desactivarPluginAnulacionExpediente();
 
  
  /*******/
  document.forms[0].target="mainFrame";
  document.forms[0].action="<%=request.getContextPath()%>/sge/DefinicionProcedimientos.do?opcion=inicio";
  document.forms[0].submit();
  /*******/
}

function pulsarAlta() { 
  clean();
  noObligatorioToObligatorio(document.forms[0]);
  noObligatorioAObligatorioDoc();
  noObligatorioAObligatorioEnlaces();
  noObligatorioAObligatorioRoles();

  document.forms[0].txtCodigo.value = '';
//  document.forms[0].codUnidadInicio.readOnly = true;
  document.forms[0].fechaLimiteDesde.value = '<%= fechaHoy %>';
  activarFormulario();
  activarBotonesDoc();
  activarBotonesCampo();
  activarBotonesEnlaces();
  activarBotonesRoles();

  inicializarBotonesRadio();
  mostrarCapasBotones('capaBotones4');

  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  consultando = false;
  
  /** Desactivación de los botones de la pestaña de la vista de pendientes si se está creando un procedimiento **/  
  desactivarBotonesVistaPendientes();
  vaciarMsgPorcentaje();
  
}



function vaciarMsgPorcentaje(){    
    document.getElementById("divPorcentaje").innerHTML="";
}

function altaRealizada() {
  mostrarCapasBotones('capaBotones2');
  desactivarFormulario();
  document.forms[0].noModificar.value = "si";
  numProcedimientos=1;
  domlay('capaNavegacionConsulta',0,0,0,' ');
  domlay('capaNavegacionConsulta',1,0,0,navegacionConsulta());
  var vector = new Array(document.forms[0].txtCodigo);
  deshabilitarGeneral(vector);
//  document.forms[0].codUnidadInicio.readOnly = false;
  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  if(document.forms[0].importar.value == "si") {
    document.forms[0].listadoImportar.value = "si";
  } else document.forms[0].listadoImportar.value = "";
  document.forms[0].importar.value= "no";

  if(document.forms[0].importar.value=="no"){
    importarProcConEleccion=null;
    codMunicipioProcImportar = null;
  }
}

function pulsarGrabarAlta() { 
 if( comprobarFechaVigencia()){
  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {

     var plazo          = document.forms[0].plazo.value;
     var porcentaje   = document.forms[0].porcentaje.value;
     var tipoPlazo0    = document.forms[0].tipoPlazo[0].checked;
     var tipoPlazo1    = document.forms[0].tipoPlazo[1].checked;
     var tipoPlazo2    = document.forms[0].tipoPlazo[2].checked;
     
     recargarComboHistorico();
     
     if(comprobarPlazo()==1){
          if(comprobarRolDefecto() == 1) {
              borrarDatosDoc();
              borrarDatosEnlaces();
              borrarDatosRoles();

              var vector = new Array(document.forms[0].txtCodigo);
              habilitarGeneral(vector);
              var listasDoc = crearListasDoc();
              document.forms[0].listaNombresDoc.value = listasDoc[0];
              document.forms[0].listaCondicionDoc.value = listasDoc[1];
              document.forms[0].listaCodigosDoc.value = listasDoc[2];
              var listasCampos = crearListasCampos();
              document.forms[0].listaCodCampos.value = listasCampos[0];
              document.forms[0].listaDescCampos.value = listasCampos[1];
              document.forms[0].listaCodPlantilla.value = listasCampos[2];
              document.forms[0].listaCodTipoDato.value = listasCampos[3];
              document.forms[0].listaTamano.value = listasCampos[4];
              document.forms[0].listaMascara.value = listasCampos[5];
              document.forms[0].listaObligatorio.value = listasCampos[6];
              document.forms[0].listaOrden.value = listasCampos[7];
              document.forms[0].listaRotulo.value = listasCampos[8];
              document.forms[0].listaActivos.value = listasCampos[9];
              document.forms[0].listaOcultos.value = listasCampos[10];
              document.forms[0].listaBloqueados.value = listasCampos[11];
              document.forms[0].listaPlazoFecha.value = listasCampos[12];
              document.forms[0].listaCheckPlazoFecha.value = listasCampos[13];
              document.forms[0].listaValidacion.value = listasCampos[14];
              document.forms[0].listaOperacion.value = listasCampos[15];
              document.forms[0].listaAgrupacionesCampos.value = listasCampos[16];
              document.forms[0].listaPosicionesX.value = listasCampos[17];
              document.forms[0].listaPosicionesY.value = listasCampos[18];
              
              var listasAgrupaciones = crearListasAgrupaciones();
              document.forms[0].listaCodAgrupaciones.value = listasAgrupaciones[0];
              document.forms[0].listaDescAgrupaciones.value = listasAgrupaciones[1];
              document.forms[0].listaOrdenAgrupaciones.value = listasAgrupaciones[2];
              document.forms[0].listaAgrupacionesActivas.value = listasAgrupaciones[3];
              
              var listasEnlaces = crearListasEnlaces();
              document.forms[0].listaCodEnlaces.value = listasEnlaces[0];
              document.forms[0].listaDescEnlaces.value = listasEnlaces[1];
              document.forms[0].listaUrlEnlaces.value = listasEnlaces[2];
              document.forms[0].listaEstadoEnlaces.value = listasEnlaces[3];
              var listasRoles = crearListasRoles();
              document.forms[0].listaCodRol.value = listasRoles[0];
              document.forms[0].listaDescRol.value = listasRoles[1];
              document.forms[0].listaPorDefecto.value = listasRoles[2];
              document.forms[0].listaConsultaWebRol.value = listasRoles[3];

              if(document.forms[0].listaCodUnidadesInicio.value != "" || document.forms[0].cqUnidadInicio[0].checked) {
                document.forms[0].codMunicipio.value = <%= municipio %>;
                document.forms[0].opcion.value="alta";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
                document.forms[0].submit();
              } else {
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjUnidadInicioNExis")%>');
              }
          } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjRolPDefNExist")%>');
          }
      }
   }
 }
}
function pulsarCancelarAlta() { 
  clean();

  document.forms[0].plazo.value = "";
  document.forms[0].tipoPlazo[0].checked=false;
  document.forms[0].tipoPlazo[1].checked=false;
  document.forms[0].tipoPlazo[2].checked=false;
  document.forms[0].porcentaje.value="";

  document.forms[0].txtCodigo.value = '';
  document.forms[0].txtDescripcion.value = '';
  document.forms[0].descripcionBreve.value = '';
  obligatorioToNoObligatorio(document.forms[0]);
  inicializarBotonesRadio();
  mostrarCapasBotones('capaRadioButtons');
  mostrarCapasBotones('capaBotones1');
  desactivarBotonesDoc();
  desactivarBotonesCampo();
  desactivarBotonesEnlaces();
  desactivarBotonesRoles();

  borrarDatosDoc();
  borrarDatosEnlaces();
  borrarDatosRoles();

  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  consultando = true;
  
  // Se desactivan los botones de la vista de pendientes
  desactivarBotonesVistaPendientes();  
  // Desactivar plugin anulación expediente
  desactivarPluginAnulacionExpediente();
}


function desactivarPluginAnulacionExpediente(){    
    comboFinalizacionExpediente.buscaLinea(-1);
    document.forms[0].descServicioFinalizacion.value= "";
    document.forms[0].codServicioFinalizacion.value = "";
    document.forms[0].implClassServicioFinalizacion.value = "";    
}

function pulsarConsultar() {
	if( comprobarFechaVigencia()){
            pleaseWait('on');
	  //document.forms[0].codMunicipio.value = <%= municipio %>;
	  document.forms[0].codMunicipio.value = codOrganizacionUsuario;
	  document.forms[0].importar.value = "no";
	  document.forms[0].opcion.value="consultar";
	  if (document.forms[0].tipoConsulta[0].checked)
	    document.forms[0].target="mainFrame";
	  else
	    document.forms[0].target="oculto";
	  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
	  document.forms[0].submit();
	  }

}
function noEncontrado() {
  clean();
  document.forms[0].txtCodigo.value = "";
  document.forms[0].txtDescripcion.value = "";
    document.forms[0].descripcionBreve.value = "";
}

function recuperaDatos(datos,lista,lista1,lista2,lista4,lista5,lista6,lista7,lista8,lista9,lista10,lista11,lista12,
                       lista13,lista14,codServicioFinalizacion,implClassServicioFinalizacion,lista15, lista16) { 
   window.focus(); 
   document.forms[0].plazo.value = datos[12];

     if(datos[13] == 1) {
        document.forms[0].tipoPlazo[0].checked = true;
     } else if (datos[13] == 2) {
     document.forms[0].tipoPlazo[1].checked = true;
        }else if (datos[13] == 3){
            document.forms[0].tipoPlazo[2].checked = true;
                }else{
            document.forms[0].tipoPlazo[0].checked = false;
            document.forms[0].tipoPlazo[1].checked = false;
            document.forms[0].tipoPlazo[2].checked = false;
        }

    /** SE COMPRUEBA SI EL PROCEDIMENTO ESTA RESTRINGIDO **/
    if(datos[24]!=undefined && datos[24]=="1"){
        // Se comprueba si el procedimiento esta o no restringido
        document.forms[0].restringido.checked = true;
        document.forms[0].restringido.value='1';
    }else{
        document.forms[0].restringido.checked = false;
        document.forms[0].restringido.value='0';
    }
    
    /* envia al form el valor del campo biblioteca/libreria de flujo del procedimiento*/
    if(datos[28]!=undefined && datos[28]=="1"){
        // Se comprueba si el procedimiento esta o no restringido
        document.forms[0].biblioteca.checked = true;
        document.forms[0].biblioteca.value='1';
        $('input[name="cmdBiblioteca"]').hide();
    }else{
        document.forms[0].biblioteca.checked = false;
        document.forms[0].biblioteca.value='0';
        $('input[name="cmdBiblioteca"]').show();
    }

    // #303601 envia al form el valor del campo numeracionExpedientesAnoAsiento
    if(datos[29]!=undefined && datos[29]=="1"){
        // Se comprueba si el procedimiento esta o no restringido
        document.forms[0].numeracionExpedientesAnoAsiento.checked = true;
        document.forms[0].numeracionExpedientesAnoAsiento.value='1';
    }else{
        document.forms[0].numeracionExpedientesAnoAsiento.checked = false;
        document.forms[0].numeracionExpedientesAnoAsiento.value='0';
    }

  document.forms[0].porcentaje.value = datos[23];
  document.forms[0].codMunicipio.value = datos[1];
  document.forms[0].txtCodigo.value = datos[2];
  document.forms[0].txtDescripcion.value = datos[3];
  document.forms[0].descripcionBreve.value = datos[22];
  document.forms[0].fechaLimiteDesde.value = datos[4];
  document.forms[0].fechaLimiteHasta.value = datos[5];
  document.forms[0].codArea.value = datos[6];
  document.forms[0].descArea.value = datos[7];
  document.forms[0].codTipoProcedimiento.value = datos[8];
  document.forms[0].descTipoProcedimiento.value = datos[9];
  if (datos[10]=="0") document.forms[0].codEstado[0].checked=true;
  else if (datos[10]=="1") document.forms[0].codEstado[1].checked=true;
  if (datos[21]=="1") document.forms[0].cqUnidadInicio[0].checked=true;
  else if (datos[21]=="0") document.forms[0].cqUnidadInicio[1].checked=true;
  if (datos[11]=="0") document.forms[0].codTipoInicio[0].checked=true;
  else if (datos[11]=="1") document.forms[0].codTipoInicio[1].checked=true;
  else if (datos[11]=="2") document.forms[0].codTipoInicio[2].checked=true;

  document.forms[0].codUnidadInicio.value = datos[15];
  document.forms[0].descUnidadInicio.value = datos[16];
  var disp = datos[17];
  if(disp == 1) {
    document.forms[0].disponible.checked = true;
  } else {
    document.forms[0].disponible.checked = false;
  }
  var tram = datos[18];
  if(tram == 1) {
    document.forms[0].tramitacionInternet.checked = true;
  } else {
    document.forms[0].tramitacionInternet.checked = false;
  }
  var obli = datos[25];
  if (obli == 1){
      document.forms[0].interesadoOblig.checked = true;
  }else {
      document.forms[0].interesadoOblig.checked = false;
  }
  //Recuperamos el valor de soloWS del oculto
  var soloWSV=datos[26];
  if (soloWSV == 1){
      document.forms[0].soloWS.checked = true;
  }else {
      document.forms[0].soloWS.checked = false;
  }
  var ClaseBuzonHist = datos[27];   
  
  var codigo = '<%=request.getAttribute("listaCodigo")%>';
  var desc = '<%=request.getAttribute("listaDesc")%>';        
  var clase = '<%=request.getAttribute("Listaclase")%>';     
        
  var listaCodigo = codigo.split(";");
  var listaDesc = desc.split(";");
  var listaclase = clase.split(";");
  <%if ("SI".equals(CargaCapaHistorico)){%>
    document.forms[0].codClaseHist.value = "";
    document.forms[0].descClaseHist.value = "";
    document.forms[0].claseBuzonEntradaHistorico.value = "";  
    for (i=0; i < listaCodigo.length;i++){
          if (listaclase[i] == ClaseBuzonHist){              
                document.forms[0].codClaseHist.value = listaCodigo[i];
                document.forms[0].descClaseHist.value = listaDesc[i];
                document.forms[0].claseBuzonEntradaHistorico.value = listaclase[i];                              
          }            
    } 
  <%}else{%>      
      document.forms[0].claseBuzonEntradaHistorico.value = ClaseBuzonHist;      
  <%}%>    
      
  if (datos[19]=="0") document.forms[0].localizacion[0].checked=true;
  else if (datos[19]=="1") document.forms[0].localizacion[1].checked=true;
  document.forms[0].tramiteInicio.value = datos[20];
  borrarTramitesArbol();
  list = lista;
  pintaTramitesProcedimiento(list);
  tabDoc.lineas = lista1;
  refrescaDoc();
  tabUnidades.lineas = lista12;
  refrescaUnidades();
  listaDoc = lista1;
  listaDocOriginal = lista2;
  tabCampos.lineas = lista6;
  refrescaCampos();
  listaCampos = lista6;
  listaCamposOriginal = lista7;
  
  tabAgrupaciones.lineas = lista15;
  refrescaAgrupaciones();
  listaAgrupaciones = lista15;
  listaAgrupacionesOriginal = lista16;

  listaEnlaces = lista8;
  listaEnlacesOriginal = lista9;
  tabEnlaces.lineas=listaEnlaces;
  refrescaEnlaces();
  listaRoles = lista10;
  listaRolesOriginal = lista11;
  tabRoles.lineas=listaRoles;
  refrescaRoles();
  document.forms[0].noModificar.value = "si";
  if(document.forms[0].importar.value == "si") {
    mostrarCapasBotones('capaBotones5');
    desactivarFormulario();
    document.forms[0].cmdNuevoTram.disabled = true;
    document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  } else {
    mostrarCapasBotones('capaBotones2');
    desactivarFormulario();
    document.forms[0].cmdNuevoTram.disabled = true;
    document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  }
  document.forms[0].listaCodUnidadesInicio.value = lista4;
  document.forms[0].listaDescUnidadesInicio.value = lista5;
  document.forms[0].listaCodVisibleUnidadesInicio.value = lista13;
     
  listaCamposSeleccionados = lista14;    
  //actualizarTablaVistaPendientesCamposDisponibles();
  actualizarTablaVistaPendientesCamposSeleccionados();
  actualizarFormulariosCamposVistaPendientes();
 
  seleccionarPluginServicioFinalizacion(codServicioFinalizacion,implClassServicioFinalizacion);
 
}


/**
 * Permite seleccionar en el combo de anulacion de expediente, un determinado plugin buscando por su codigo o nombre */
function seleccionarPluginServicioFinalizacion(codigoPlugin,implClassServicio){    
    document.forms[0].descServicioFinalizacion.value= "";
    document.forms[0].codServicioFinalizacion.value = "";
    document.forms[0].implClassServicioFinalizacion.value = "";

    if(codigoPlugin!=null && codigoPlugin!="" && implClassServicio!=null && implClassServicio!=""){
        var encontrado = false;

        for(i=0;listaCodigosServiciosFinalizacion!=null && i<listaCodigosServiciosFinalizacion.length;i++){
            if(listaCodigosServiciosFinalizacion[i]==codigoPlugin){
                encontrado = true;
                break;
            }// if                       
        }// for
        
        document.forms[0].descServicioFinalizacion.value= listaDescripcionesServiciosFinalizacion[i];
        document.forms[0].codServicioFinalizacion.value = codigoPlugin;        
        document.forms[0].implClassServicioFinalizacion.value = implClassServicio;
    }    
}

function actualizarTablaVistaPendientesCamposSeleccionados(){ 
    // Se actualiza la tabla con los campos disponibles
    var seleccionados = new Array();
    for(i=0;listaCamposSeleccionados!=null && listaCamposSeleccionados!="" && i<listaCamposSeleccionados.length;i++){
        seleccionados[i]= [listaCamposSeleccionados[i][0],listaCamposSeleccionados[i][1],listaCamposSeleccionados[i][2] + "%"];
    }
 
    tabPendientes.lineas = seleccionados;
    tabPendientes.displayTabla();
    mostrarPorcentajeAcumulado();
}


function pulsarModificar()
{
  mostrarCapasBotones('capaBotones3');
  domlay('capaNavegacionConsulta',0,0,0,' ');
  domlay('capaNavegacionConsulta',1,0,0,navegacionConsultaDesactivada());
  activarFormulario();
  noObligatorioToObligatorio(document.forms[0]);
  noObligatorioAObligatorioDoc();
  noObligatorioAObligatorioEnlaces();
  noObligatorioAObligatorioRoles();
  
  var vector = new Array(document.forms[0].txtCodigo);
  deshabilitarGeneral(vector);
  document.forms[0].cmdNuevoTram.disabled = false;
  document.forms[0].cmdNuevoTram.className = "botonGeneral";
  document.forms[0].porcentaje.disabled = false;


  activarBotonesDoc();
  activarBotonesCampo();
  activarBotonesEnlaces();
  activarBotonesRoles();
  
  consultando = false;
  document.forms[0].noModificar.value = "no"; 
}

function comprobarExistenciaExpedientesBiblioteca(codProcedimiento){
    try{
          $.ajax({
              url:  '<c:url value="/procedimiento/bibliotecaFlujoTramitacion.do"/>',
              type: 'POST',
              async: true,
              data: 'codProcedimiento=' + codProcedimiento + '&opcion=comprobarExpedientesProcedimiento',
              success: procesarRespuestaComprobarExpedientesProcedimiento,
              error: muestraErrorRespuestaComprobarExpedientesProcedimiento
          });           
     }catch(Err){
         jsp_alerta("A",'<%=descriptor.getDescripcion("etiqErrorComprobarExp")%>');
     } 
}
        
function procesarRespuestaComprobarExpedientesProcedimiento(ajaxResult){
    if(ajaxResult=="1"){
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqProcConExp")%>');
    } else {
        modificarProcedimiento();
    }
}

function muestraErrorRespuestaComprobarExpedientesProcedimiento(){
    jsp_alerta("A",'<%=descriptor.getDescripcion("etiqErrorComprobarExp")%>');
}

function modificarProcedimiento(){
    recargarComboHistorico();
    borrarDatosDoc();
    borrarDatosEnlaces();
    borrarDatosRoles();

    var vector = new Array(document.forms[0].txtCodigo);
    habilitarGeneral(vector);
    var listasDoc = crearListasDoc();
    document.forms[0].listaNombresDoc.value = listasDoc[0];
    document.forms[0].listaCondicionDoc.value = listasDoc[1];
    document.forms[0].listaCodigosDoc.value = listasDoc[2];
    var listasCampos = crearListasCampos();
    document.forms[0].listaCodCampos.value = listasCampos[0];
    document.forms[0].listaDescCampos.value = listasCampos[1];
    document.forms[0].listaCodPlantilla.value = listasCampos[2];
    document.forms[0].listaCodTipoDato.value = listasCampos[3];
    document.forms[0].listaTamano.value = listasCampos[4];
    document.forms[0].listaMascara.value = listasCampos[5];
    document.forms[0].listaObligatorio.value = listasCampos[6];
    document.forms[0].listaOrden.value = listasCampos[7];
    document.forms[0].listaRotulo.value = listasCampos[8];
    document.forms[0].listaActivos.value = listasCampos[9];
    document.forms[0].listaOcultos.value = listasCampos[10];
    document.forms[0].listaBloqueados.value = listasCampos[11];
    document.forms[0].listaPlazoFecha.value = listasCampos[12];
    document.forms[0].listaCheckPlazoFecha.value = listasCampos[13];
    document.forms[0].listaValidacion.value = listasCampos[14];
    document.forms[0].listaOperacion.value = listasCampos[15];
    document.forms[0].listaAgrupacionesCampos.value = listasCampos[16];
    document.forms[0].listaPosicionesX.value = listasCampos[17];
    document.forms[0].listaPosicionesY.value = listasCampos[18];
    var listasAgrupaciones = crearListasAgrupaciones();
    document.forms[0].listaCodAgrupaciones.value = listasAgrupaciones[0];
    document.forms[0].listaDescAgrupaciones.value = listasAgrupaciones[1];
    document.forms[0].listaOrdenAgrupaciones.value = listasAgrupaciones[2];
    document.forms[0].listaAgrupacionesActivas.value = listasAgrupaciones[3];                  
    var listasEnlaces = crearListasEnlaces();
    document.forms[0].listaCodEnlaces.value = listasEnlaces[0];
    document.forms[0].listaDescEnlaces.value = listasEnlaces[1];
    document.forms[0].listaUrlEnlaces.value = listasEnlaces[2];
    document.forms[0].listaEstadoEnlaces.value = listasEnlaces[3];
    var listasRoles = crearListasRoles();
    document.forms[0].listaCodRol.value = listasRoles[0];
    document.forms[0].listaDescRol.value = listasRoles[1];
    document.forms[0].listaPorDefecto.value = listasRoles[2];
    document.forms[0].listaConsultaWebRol.value = listasRoles[3];

  if(document.forms[0].listaCodUnidadesInicio.value != "" || document.forms[0].cqUnidadInicio[0].checked) {
          document.forms[0].opcion.value="modificar";
          document.forms[0].target="oculto";
          document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
          document.forms[0].submit();
  } else {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjUnidadInicioNExis")%>');
  }
}
    
function pulsarGrabarModificar() { 
if( comprobarFechaVigencia()){
  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
    if(comprobarPlazo() ==1) {
      if(comprobarRolDefecto() == 1) {
          var libreria = document.forms[0].biblioteca.value;
          var codProcedimiento = document.forms[0].txtCodigo.value;
            if(libreria==1) {
                comprobarExistenciaExpedientesBiblioteca(codProcedimiento);
            } else {
                 modificarProcedimiento(); 
            }
      } else {
	    jsp_alerta("A",'<%=descriptor.getDescripcion("msjRolPDefNExist")%>');
	  }
    }
  }
 }
}
function pulsarCancelarModificar() {
  mostrarCapasBotones('capaBotones2');
  if(numProcedimientos <1) {
    numProcedimientos = -1;
  }
  domlay('capaNavegacionConsulta',0,0,0,' ');
  domlay('capaNavegacionConsulta',1,0,0,navegacionConsulta());
  desactivarFormulario();
  desactivarBotonesDoc();
  desactivarBotonesCampo();
  desactivarBotonesEnlaces();
  desactivarBotonesRoles();

  borrarDatosDoc();
  borrarDatosEnlaces();
  borrarDatosRoles();

  var vector = new Array(document.forms[0].txtCodigo);
  deshabilitarGeneral(vector);
  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  consultando = true;
  document.forms[0].noModificar.value = "si";
}
function yaExisteProcedimiento() {
  desactivarFormulario();
  desactivarBotonesDoc();
  desactivarBotonesCampo();
  desactivarBotonesEnlaces();
  desactivarBotonesRoles();

  borrarDatosDoc();
  borrarDatosEnlaces();
  borrarDatosRoles();

  var vector = new Array(document.forms[0].txtCodigo);
  deshabilitarGeneral(vector);
  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
}
function modificacionRealizada() { 
  mostrarCapasBotones('capaBotones2');
  desactivarFormulario();
  document.forms[0].cmdNuevoTram.disabled = true;
  document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
  document.forms[0].noModificar.value = "si";
  recargaConsulta();
}
function pulsarEliminar() {
  if(jsp_alerta("C",'<%=descriptor.getDescripcion("desEliminar")%>') ==1) {
    document.forms[0].opcion.value="eliminar";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
    document.forms[0].submit();
  }
}
function procEliminado() {
  clean();
  consultando = true;
  document.forms[0].txtCodigo.value = "";
  document.forms[0].txtDescripcion.value = "";
    document.forms[0].descripcionBreve.value = "";
  habilitarDatos(document.forms[0]);
  document.forms[0].noModificar.value = "si";
  desactivarPluginAnulacionExpediente();
  
  if(document.getElementById('capaNavegacionConsulta').style.visibility == 'visible' && (numProcedimientos !=1) ){
      if(procedimientoActual == numProcedimientos) {
        document.forms[0].ultimo.value = "si";
        procedimientoEliminado(procedimientoActual-1);
      } else {
        document.forms[0].tipoConsulta[1].checked = true;
        procedimientoEliminado(procedimientoActual);
      }
  } else {
    document.forms[0].tipoConsulta[0].checked = true;
    activarFormulario();
    domlay('capaNavegacionConsulta',0,0,0,' ');
    obligatorioToNoObligatorio(document.forms[0]);
    mostrarCapasBotones('capaRadioButtons');
    mostrarCapasBotones('capaBotones1');
  }
}
function pulsarNuevoTramite() {
  if(document.forms[0].txtCodigo.value == "" || document.forms[0].txtDescripcion.value =="") {
    jsp_alerta("A",'<%=descriptor.getDescripcion("codDescProcVacio")%>');
  } else {
    var vector = new Array(document.forms[0].txtCodigo);
    habilitarGeneral(vector);
    var cui = document.forms[0].codUnidadInicio.value;
    var dui = document.forms[0].descUnidadInicio.value;    
    clean();
    document.forms[0].codUnidadInicio.value=cui;
    document.forms[0].descUnidadInicio.value=dui;
    document.forms[0].enviar.value = "";
    document.forms[0].opcion.value="inicio";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
    document.forms[0].submit();    
  }
}
function enviaTramite(cod, num, nombre) { 
  if(importarProcConEleccion!=null && codOrganizacionUsuario!=codMunicipioProcImportar && (importarProcConEleccion=="si" || importarProcConEleccion=="SI"))
  {
	// Si se está importando un procedimiento de un entorno real en pruebas
	enviaTramiteImportacionDesdePruebas(cod,num,nombre);
  }else{
	  var vector = new Array(document.forms[0].txtDescripcion,document.forms[0].txtCodigo);
	  habilitarGeneral(vector);
	  if(document.forms[0].importar.value == "si") {
		document.forms[0].codMunicipio.value = 0;
	  }

	  if(document.forms[0].deCatalogo.value == "si") {
		document.forms[0].deCatalogoDeProcedimiento.value = "si";
	  }
	  document.forms[0].codigoTramite.value = cod;
	  document.forms[0].numeroTramite.value = num;
	  document.forms[0].nombreTramite.value = nombre;
	  document.forms[0].enviar.value = "si";
	  document.forms[0].opcion.value="enviar";
	  document.forms[0].target="mainFrame";
	  document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
	  document.forms[0].submit();
  }
}



function enviaTramiteImportacionDesdePruebas(cod, num, nombre) {
  var vector = new Array(document.forms[0].txtDescripcion,document.forms[0].txtCodigo);
  habilitarGeneral(vector);
  if(document.forms[0].importar.value == "si") {
    document.forms[0].codMunicipio.value = codMunicipioProcImportar;
  }
  if(document.forms[0].deCatalogo.value == "si") {
    document.forms[0].deCatalogoDeProcedimiento.value = "si";
  }
  document.forms[0].codigoTramite.value = cod;
  document.forms[0].numeroTramite.value = num;
  document.forms[0].nombreTramite.value = nombre;
  document.forms[0].enviar.value = "si";
  document.forms[0].opcion.value="enviarTramiteRealImportar";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/DefinicionTramites.do'/>";
  document.forms[0].submit();
}



function desactivarFormulario() {
  deshabilitarDatos(document.forms[0]);
  deshabilitarImagenCal("calFechaLimiteDesde",true);
  deshabilitarImagenCal("calFechaLimiteHasta",true);
  comboArea.deactivate();
  comboEstadoEnlace.deactivate();
  comboTipoProcedimiento.deactivate();
  
  document.forms[0].biblioteca.disabled = true;
  document.forms[0].numeracionExpedientesAnoAsiento.disabled = true;
  document.forms[0].plazo.disabled = true;
  document.forms[0].tipoPlazo[0].disabled = true;
  document.forms[0].tipoPlazo[1].disabled = true;
  document.forms[0].tipoPlazo[2].disabled = true;
  document.forms[0].porcentaje.disabled = true;
  document.getElementById("et1").className  = 'etiquetaDeshabilitada';
  document.getElementById("et2").className ='etiquetaDeshabilitada';

  var vectorBotones = new Array(document.forms[0].cmdCancelarAlta,document.forms[0].cmdGrabarAlta,document.forms[0].cmdCancelarModificar,
    document.forms[0].cmdGrabarModificar,document.forms[0].cmdCancelarBuscar,document.forms[0].cmdEliminar,
    document.forms[0].cmdSalir,document.forms[0].cmdModificar,document.forms[0].cmdConsultar,
    document.forms[0].cmdAlta,document.forms[0].cmdListado,document.forms[0].cmdDescargar,document.forms[0].cmdCancelarImportar,
	document.forms[0].cmdDuplicar,document.forms[0].cmdImprimir,document.forms[0].cmdBiblioteca);
     habilitarGeneral(vectorBotones);
    document.getElementById('botonTipoInicio').style.visibility='hidden';

  with (document.forms[0]) {
  disponible.disabled = true;
  tramitacionInternet.disabled = true;
  interesadoOblig.disabled = true;
  soloWS.disabled=true;
	<%if ("SI".equals(CargaCapaHistorico)){%>
    codClaseHist.disable = true;
    descClaseHist.disable = true;
  <%}%>
  codEstado[0].disabled = true;
  codEstado[1].disabled = true;
  cqUnidadInicio[0].disabled = true;
  cqUnidadInicio[1].disabled = true;
  codTipoInicio[0].disabled = true;
  codTipoInicio[1].disabled = true;
  codTipoInicio[2].disabled = true;
  localizacion[0].disabled = true;
  localizacion[1].disabled = true;
  }
  desHabilitarImagenesArbol(false);
  desactivarTablas();
  // Se deshabilita el check restringido
  document.forms[0].restringido.disabled = true;
}
function activarFormulario() {
  habilitarDatos(document.forms[0]);
  habilitarImagenCal("calFechaLimiteDesde",true);
  habilitarImagenCal("calFechaLimiteHasta",true);
  document.getElementById('botonTipoInicio').style.visibility='visible';
  comboArea.activate();
  comboEstadoEnlace.activate();
  comboTipoProcedimiento.activate();

  var vectorBotones = new Array(document.forms[0].cmdCancelarAlta,document.forms[0].cmdGrabarAlta,document.forms[0].cmdCancelarModificar,
    document.forms[0].cmdGrabarModificar,document.forms[0].cmdCancelarBuscar,document.forms[0].cmdEliminar,
    document.forms[0].cmdSalir,document.forms[0].cmdModificar,document.forms[0].cmdConsultar,
    document.forms[0].cmdAlta,document.forms[0].cmdListado,document.forms[0].cmdDescargar,document.forms[0].cmdCancelarImportar,
	document.forms[0].cmdDuplicar,document.forms[0].cmdImprimir);

  document.forms[0].plazo.disabled=false;
  document.forms[0].porcentaje.disabled=true;
  document.forms[0].tipoPlazo[0].disabled=false;
  document.forms[0].tipoPlazo[1].disabled=false;
  document.forms[0].tipoPlazo[2].disabled=false;


  habilitarGeneral(vectorBotones);
  with (document.forms[0]) {
  disponible.disabled = false;
  tramitacionInternet.disabled = false;
  interesadoOblig.disabled = false;
  soloWS.disabled=false;
  <%if ("SI".equals(CargaCapaHistorico)){%>
    codClaseHist.disable = false;
    descClaseHist.disable = false;
  <%}%>
  codEstado[0].disabled = false;
  codEstado[1].disabled = false;
  cqUnidadInicio[0].disabled = false;
  cqUnidadInicio[1].disabled = false;
  codTipoInicio[0].disabled = false;
  codTipoInicio[1].disabled = false;
  codTipoInicio[2].disabled = false;
  localizacion[0].disabled = false;
  localizacion[0].disabled = false;
  }
  desHabilitarImagenesArbol(false);
  activarTablas();
}
function desactivarTablas() {
  if(document.forms[0].cmdAltaDoc.disabled == true) {
    tabDoc.readOnly = true;
  }
  if(document.forms[0].cmdAltaEnlaces.disabled == true) {
    tabEnlaces.readOnly = true;
  }
  if(document.forms[0].cmdAltaRoles.disabled == true) {
    tabRoles.readOnly = true;
  }

}
function activarTablas() {
  if(document.forms[0].cmdAltaDoc.disabled == false) {
    tabDoc.readOnly = false;
  }
  if(document.forms[0].cmdAltaCampo.disabled == false) {
    tabCampos.readOnly = false;
  }
  if(document.forms[0].cmdAltaEnlaces.disabled == false) {
    tabEnlaces.readOnly = false;
  }
  if(document.forms[0].cmdAltaRoles.disabled == false) {
    tabRoles.readOnly = false;
  }
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
        tp1.setSelectedIndex(0);
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
        tp1.setSelectedIndex(0);
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
function comparaFechasLocal(fecha1,fecha2) {
/*  Precondicion: las fecha son correctas.
  Salida: 1 -> la mayor es fecha1
      2 -> la mayor es fecha2
*/
   var dfecha1 = new Date(fecha1.substring(6,10), eval(fecha1.substring(3,5)-1),fecha1.substring(0,2) );
   var dfecha2 = new Date(fecha2.substring(6,10), eval(fecha2.substring(3,5)-1),fecha2.substring(0,2) );
   if(comparaFechas(dfecha1,dfecha2) == 1) {
    return 1;
   } else return 2;
}
function comprobarFechaVigencia() {
  var res = false;
  if (comprobarFecha(document.forms[0].fechaLimiteDesde) && comprobarFecha(document.forms[0].fechaLimiteHasta)){
    if (!consultando) {
      var fechaD = document.forms[0].fechaLimiteDesde.value.trim();
      var fechaH = document.forms[0].fechaLimiteHasta.value.trim();
      if (fechaH != '') {
        if (fechaD !='') {
          if (comparaFechasLocal(fechaD, fechaH)==2)
            res =true
        }
      }  else res = true;
    } else res = true; // No se comprueba intervalo

	if (!res) {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjFechDMH")%>');
	}
  }
  return res;
}
function borrarDatos()	 {
  document.forms[0].fechaLimiteDesde.value = "";
  document.forms[0].fechaLimiteHasta.value = "";
  document.forms[0].codArea.value = "";
  document.forms[0].descArea.value = "";
  document.forms[0].codTipoProcedimiento.value = "";
  document.forms[0].descTipoProcedimiento.value = "";
  document.forms[0].codEstado[0].checked=false;
  document.forms[0].codEstado[1].checked=false;
  document.forms[0].cqUnidadInicio[0].checked=false;
  document.forms[0].cqUnidadInicio[1].checked=false;
  document.forms[0].codTipoInicio[0].checked=false;
  document.forms[0].codTipoInicio[1].checked=false;
  document.forms[0].codTipoInicio[2].checked=false;
  document.forms[0].disponible.checked = false;
  document.forms[0].tramitacionInternet.checked = false;
  document.forms[0].interesadoOblig.checked = false;
  document.forms[0].soloWS.checked=false;
  document.forms[0].localizacion[0].checked=false;
  document.forms[0].localizacion[1].checked=false;
  document.forms[0].listaCodUnidadesInicio.value='';
  document.forms[0].listaDescUnidadesInicio.value='';
  <%if ("SI".equals(CargaCapaHistorico)){%>
    document.forms[0].codClaseHist.value = "";
    document.forms[0].descClaseHist.value = "";
    document.forms[0].claseBuzonEntradaHistorico.value = "";
  <%}%>
  
  
}
function clean() {
  borrarTramitesArbol();
  listaDoc = new Array();
  tabDoc.lineas = listaDoc;
  refrescaDoc();
  listaEnlaces = new Array();
  tabEnlaces.lineas = listaEnlaces;
  refrescaEnlaces();
  listaRoles = new Array();
  listaRolesOriginal = new Array();
  tabRoles.lineas = listaRoles;
  refrescaRoles();

    borrarTablaCampos();
  limpiarUnidadesInicio();
  borrarDatos();
}
function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones) {
    cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
}
function onBlurDepartamento(){
}
function callFromTableTo(rowID,tableName){
  if(tabCampos.id == tableName){
    verCampo(rowID);
  }
}

function actualizarListaUnidadesInicio(datos) {
    tabUnidades.lineas=datos;
    refrescaUnidades();
}
function pulsarListaUnidadesInicio() {
  if (document.forms[0].cqUnidadInicio[1].checked){
  var codMunicipio = document.forms[0].codMunicipio.value;
  var codProcedimiento = document.forms[0].txtCodigo.value;
  var soloConsulta = "";
  if(document.forms[0].txtDescripcion.disabled == true) {
    soloConsulta = "si";
  } else {
    soloConsulta = "no";
  }

// #265897: no se acepta una url de tanta longitud, las listas se pasan por parametro de abrirXanelaAuxiliar
    var listas = new Array();	
    listas.push(document.forms[0].listaCodUnidadesInicio.value);	
    listas.push(document.forms[0].listaCodVisibleUnidadesInicio.value);	
    listas.push(document.forms[0].listaDescUnidadesInicio.value);
    
  var source = "<c:url value='/sge/DefinicionProcedimientos.do?opcion=listaUnidadesInicio&codMun='/>"+codMunicipio+"&codProc="+codProcedimiento+
"&nCS="+soloConsulta;
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,listas,
        'width=950,height=450,status='+ '<%=statusBar%>',function(datos){
            if(datos!=undefined){
                if(document.forms[0].txtDescripcion.disabled == false) {
                  document.forms[0].listaCodVisibleUnidadesInicio.value = datos[5];
                  document.forms[0].listaCodUnidadesInicio.value = datos[0];
                  document.forms[0].listaDescUnidadesInicio.value = datos[1];
                  document.forms[0].codUnidadInicio.value = datos[2];
                  document.forms[0].descUnidadInicio.value = datos[3];
                  actualizarListaUnidadesInicio(datos[4]);
                }
            }
        });
  }
    else jsp_alerta("A",'<%=descriptor.getDescripcion("msjAvisoCqUnidadInic")%>');
}

function limpiarUnidadesInicio(){
    listaVacia = new Array();
    listaCodUI = listaVacia;
    listaCodVisibleUI = listaVacia;
    listaDescUI = listaVacia;
    document.forms[0].listaCodVisibleUnidadesInicio.value = listaVacia;
    document.forms[0].listaCodUnidadesInicio.value = listaVacia;
    document.forms[0].listaDescUnidadesInicio.value = listaVacia;
    document.forms[0].codUnidadInicio.value = "";
    document.forms[0].descUnidadInicio.value = "";
    actualizarListaUnidadesInicio(listaVacia);
}


function comprobarPlazo() {

    var plazo = document.forms[0].plazo.value;
    var tipoPlazo0 = document.forms[0].tipoPlazo[0].checked;
    var tipoPlazo1 = document.forms[0].tipoPlazo[1].checked;
    var tipoPlazo2 = document.forms[0].tipoPlazo[2].checked;

    if (document.forms[0].plazo.value != '') {
          if(!document.forms[0].tipoPlazo[0].checked && !document.forms[0].tipoPlazo[1].checked && !document.forms[0].tipoPlazo[2].checked)
          {
              jsp_alerta("A",'<%=descriptor.getDescripcion("plNotNoCor")%>');
              return 0;
          }
          else return 1;
    }

    // Se comprueba si se ha introducido porcentaje o tipo de plazo
    if(document.forms[0].plazo.value.length==0 && (document.forms[0].tipoPlazo[0].checked || document.forms[0].tipoPlazo[1].checked || document.forms[0].tipoPlazo[2].checked))
    {
        jsp_alerta("A",'<%=descriptor.getDescripcion("plNotNoCor")%>');
        return 0;
    } else return 1;


    // Se comprueba si se ha introducido porcentaje o tipo de plazo
    if(document.forms[0].porcentaje.value.length>0 && (document.forms[0].plazo.value.length==0 && !document.forms[0].tipoPlazo[0].checked && !document.forms[0].tipoPlazo[1].checked && !document.forms[0].tipoPlazo[2].checked))
    {
        jsp_alerta("A",'<%=descriptor.getDescripcion("plNotNoCor")%>');
        return 0;
    } else return 1;

}

function onClickTipoPlazo() {
  if (document.forms[0].plazo.value=='') {
    document.forms[0].tipoPlazo[0].checked = false;
    document.forms[0].tipoPlazo[1].checked = false;
    document.forms[0].tipoPlazo[2].checked = false;
  }
}
function navegacionConsulta() {
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',procedimientoActual,numProcedimientos,'determinarProcedimiento','<%=descriptor.getDescripcion("numResultados")%>',numProcedimientos);	
}
function navegacionConsultaDesactivada() {
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',procedimientoActual,numProcedimientos,null,'<%=descriptor.getDescripcion("numResultados")%>',numProcedimientos);	
}
function determinarProcedimiento(i){ 
  document.forms[0].posicionProcedimiento.value=i;
  document.forms[0].codMunicipio.value = <%= municipio %>;

  //document.forms[0].opcion.value="determinar_procedimiento";
  document.forms[0].target="oculto";

  if(importarProcConEleccion!="" && (importarProcConEleccion=="si" || importarProcConEleccion=="SI"))
      document.forms[0].opcion.value="determinar_procedimiento_real_importacion";
  else {
      document.forms[0].codMunicipio.value = 0;
      document.forms[0].opcion.value="determinar_procedimiento";
  }
  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
  document.forms[0].submit();
}
function procedimientoEliminado(i) {
  document.forms[0].posicionProcedimiento.value=i;
  document.forms[0].opcion.value="procedimiento_eliminado";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
  document.forms[0].submit();
}
function procedimientoAprocedimiento(totalProcedimientos,cMunic,cProc,pos){ 
    numProcedimientos = totalProcedimientos;
    codMunic= cMunic;
    codProc = cProc;
    actualizaAnotacionNavegacion(pos);

}
function actualizaAnotacionNavegacion(procedimientoSelecc) { 
  if (numProcedimientos>0) {
    document.forms[0].codAplicacion.value = <%= aplicacion %>
    document.forms[0].codMunicipio.value = codMunic;
    document.forms[0].txtCodigo.value = codProc;
    var vector = new Array(document.forms[0].txtCodigo);
    habilitarGeneral(vector);
    document.forms[0].opcion.value="buscar";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
    document.forms[0].submit();
    procedimientoActual = procedimientoSelecc;
  } else if (numProcedimientos == -1) {
    numProcedimientos = 1;
    mostrarCapasBotones('capaBotones2');
    desactivarFormulario();
  } else {
    mostrarCapasBotones('capaBotones2');
    desactivarFormulario();
    document.forms[0].cmdNuevoTram.disabled = true;
    document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
    document.forms[0].cmdModificar.disabled = true;
    document.forms[0].cmdEliminar.disabled = true;
  }
  domlay('capaNavegacionConsulta',1,0,0,navegacionConsulta());
}
function onchangeCampoCod(nmbCampoCod, nmbCampoDesc, vCod, vDesc,cjtoOp){
  if (consultando) {
    var campoCod = eval('document.forms[0].'+nmbCampoCod);
    var campoDesc = eval('document.forms[0].'+nmbCampoDesc);
    if (!contieneOperadoresConsulta(campoCod,cjtoOp)){
      divSegundoPlano=true;
      actualizarDescripcion(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
    } else 	campoDesc.value='';
  } else {
      divSegundoPlano=true;
      inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
  }
}
function onFocusCampoDesc(nmbCampoCod, nmbCampoDesc, vCod, vDesc,cjtoOp){
  if (consultando) {
    var campoCod = eval('document.forms[0].'+nmbCampoCod);
    if (!contieneOperadoresConsulta(campoCod,cjtoOp)){
      divSegundoPlano=true;
      inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
    }
  } else {
      divSegundoPlano=true;
      inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
  }
}
function onClickCampoDesc(nmbCampoCod, nmbCampoDesc, vCod, vDesc){
   if (consultando) {
     var campoCod = eval('document.forms[0].'+nmbCampoCod);
     campoCod.value ='';
   }
   divSegundoPlano=false;
   inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
}
function contieneOperadoresConsulta(campo,cjtoOp){
  var contiene=false;
  var v = campo.value;
  for (i = 0; i < v.length; i++){
    var c = v.charAt(i);
    if (cjtoOp.indexOf(c) != -1)  contiene=true;
  }
  return contiene;
}
function recargaConsulta() { 
  determinarProcedimiento('-1');
  document.getElementById("capaBotonesNuevaConsulta").style.display = 'none';
}
function pulsarListado() {
  var vector = new Array(document.forms[0].txtCodigo);
  habilitarGeneral(vector);
  pleaseWait('on');
  document.forms[0].opcion.value="ver_consulta_listado";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
  document.forms[0].submit();
}

// Funcion que copia el campo de descripcion al campo de descripcion Breve, truncandolo si es necesario.
function copiaDescripcion() {
    if (consultando == false) {
        var descProc = document.forms[0].txtDescripcion.value;
        if (descProc.length > 75) {
            descProc = descProc.substring(0,75);
        }
        document.forms[0].descripcionBreve.value = descProc;
        document.forms[0].descripcionBreve.focus();
    }
}

// JAVASCRIPT DE LA PESTAÑA DOCUMENTOS
function activarBotonesDoc() {
  var vectorBotones = new Array(document.forms[0].cmdAltaDoc,document.forms[0].cmdModificarDoc,document.forms[0].cmdEliminarDoc,
    document.forms[0].cmdLimpiarDoc);
  habilitarGeneral(vectorBotones);
}
function desactivarBotonesDoc() {
  var vectorBotones = new Array(document.forms[0].cmdAltaDoc,document.forms[0].cmdModificarDoc,document.forms[0].cmdEliminarDoc,
    document.forms[0].cmdLimpiarDoc);
  deshabilitarGeneral(vectorBotones);
}

function tieneFirmas() {
	return (listaDocOriginal[tabDoc.selectedIndex][3]==1);
}


function pulsarAltaDoc() {
  var desc = document.forms[0].nombreDoc.value;
  var yaExiste = 0;
  if(comprobarObligatoriosDoc()) {
    for(l=0; l < listaDoc.length; l++){
    if ((listaDoc[l][0]) == desc){
        yaExiste = 1;
      }
    }
    if(yaExiste == 0) {
      var lineas = tabDoc.lineas;
      for (i=0; i < lineas.length; i++) {
      }
	  var codDoc = 0;
	  if(i != 0) {
	   codDoc = listaDocOriginal[i-1][0];
	  }
      if(document.forms[0].condicion.value == "") {
        listaDoc[i]=[document.forms[0].nombreDoc.value," ",
        '<span class="fa fa-close 2x"></span>'];
        listaDocOriginal[i]=[((codDoc*1)+1),document.forms[0].nombreDoc.value," ","0"];
      } else {
        listaDoc[i]=[document.forms[0].nombreDoc.value,document.forms[0].condicion.value,
        '<span class="fa fa-close 2x"></span>'];
        listaDocOriginal[i]=[((codDoc*1)+1),document.forms[0].nombreDoc.value,document.forms[0].condicion.value,'0'];
      }
      tabDoc.lineas=listaDoc;
      refrescaDoc();
      borrarDatosDoc();
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
    }
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
  }
}
function pulsarModificarDoc() {
  var desc = document.forms[0].nombreDoc.value;
  var yaExiste = 0;
  var firmasDefinidas =listaDocOriginal[tabDoc.selectedIndex][3];
  var imagenFirma = listaDoc[tabDoc.selectedIndex][2];
  if(tabDoc.selectedIndex != -1) {
	if(!tieneFirmas()) {
		if (comprobarObligatoriosDoc()) {
		  for(l=0; l < listaDoc.length; l++){
			  var lineaSeleccionada;
			  lineaSeleccionada = tabDoc.selectedIndex;
			  if(l == lineaSeleccionada) {
				l= l;
			  } else {
				if ((listaDoc[l][0]) == desc ){
				  yaExiste = 1;
				}
			  }
		  }
		  if(yaExiste == 0) {
			var j = tabDoc.selectedIndex;
			var codDoc = listaDocOriginal[j][0];
			if(document.forms[0].condicion.value == "") {
			  listaDoc[j]=[document.forms[0].nombreDoc.value," ",imagenFirma];
			  listaDocOriginal[j]=[codDoc,document.forms[0].nombreDoc.value," ",firmasDefinidas];
			} else {
			  listaDoc[j]=[document.forms[0].nombreDoc.value,document.forms[0].condicion.value,imagenFirma];
			  listaDocOriginal[j]=[codDoc,document.forms[0].nombreDoc.value,document.forms[0].condicion.value,firmasDefinidas];
			}
			tabDoc.lineas=listaDoc;
			refrescaDoc();
			borrarDatosDoc();
		  } else {
			jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
		  }
		} else {
		  jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
		}
	} else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoModifFirma")%>');
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarFirmasDoc(){
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var codProcedimiento = document.forms[0].txtCodigo.value;
	var indiceDoc = tabDoc.selectedIndex;
    var parametros = new Array();
    parametros[0] = codMunicipio;
    parametros[1] = listaDocOriginal[indiceDoc][0];
    parametros[2] = codProcedimiento;
    parametros[3] = true;

    var source = "<c:url value='/ListadoFirmasDocumentoProcedimiento.do?opcion=entrada'/>" +
        "&codProcedimiento=" + codProcedimiento + "&codMunicipio=" + codMunicipio
        + "&codDocumento=" + listaDocOriginal[tabDoc.selectedIndex][0] +
        "&desdeDefinicion=true";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,parametros,
	'width=780,height=350,status='+ '<%=statusBar%>',function(numeroFirmas){
                        if(numeroFirmas!=undefined){
                            var imgFir;
                            var valorFir;
                            if (numeroFirmas>0) {
                                imgFir = '<span class="fa fa-check 2x"></span>';
                                            valorFir = 1;
                            } else {
                                imgFir = '<span class="fa fa-close 2x"></span>';
                                            valorFir = 0;
                            }
                            listaDoc[indiceDoc][2]=imgFir;
                            listaDocOriginal[indiceDoc][3]=valorFir;
                            tabDoc.lineas=listaDoc;
                            refrescaDoc();
                        }
                    });

}

function pulsarEliminarDoc() {
    if(tabDoc.selectedIndex != -1) {
		if(!tieneFirmas()) {
			if(comprobarObligatoriosDoc()) {
				if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarDoc")%>')) {
					tamIndex=tabDoc.selectedIndex;
					pleaseWait('on');
					var params = new Array();
					params['codOrganizacion'] = '' + document.forms[0].codMunicipio.value;
					params['procedimiento'] = '' + document.forms[0].txtCodigo.value;
					params['numDocumento'] = '' + listaDocOriginal[tamIndex][0];
					var bindArgs = {
						url: '<c:url value="/sge/DefinicionProcedimientos.do"/>?opcion=comprobarEliminarDoc',
						error: function(type, data, evt) {
							jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjErrorElimDoc"))%>');
						},
						mimetype: "text/json",
						content: params
					};
					var req = dojo.io.bind(bindArgs);
					dojo.event.connect(req, "load", this, "resultadoComprobacion");
				} else {
			 tabDoc.selectLinea(tabDoc.selectedIndex);
			 tabDoc.selectedIndex = -1;
			 borrarDatosDoc();
		   }
		 } else {
		   jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
		 }
	 } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoElimFirma")%>');
   } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function resultadoComprobacion(type, data, evt) {
    pleaseWait('off');
    if (data) {
        if (data.error) {
            jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjErrorElimDoc"))%>');
            return;
        }
        var existeDocumento = data.existeDocumento;
        if (!existeDocumento) {
            var list = new Array();
            var listOrig = new Array();
            tamIndex=tabDoc.selectedIndex;
            tamLength=tabDoc.lineas.length;
            for (i=tamIndex - 1; i < listaDoc.length - 1; i++){
                if (i + 1 <= listaDoc.length - 2){
                    listaDoc[i + 1]=listaDoc[i + 2];
                    listaDocOriginal[i + 1]=listaDocOriginal[i + 2];
                }
            }
            for(j=0; j < listaDoc.length-1 ; j++){
                list[j] = listaDoc[j];
                listOrig[j] = listaDocOriginal[j];
            }
            tabDoc.lineas=list;
            refrescaDoc();
            borrarDatosDoc();
            listaDoc=list;
            listaDocOriginal = listOrig;
            return;
        } else {
            jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjNoElimDoc"))%>');
            return;
        }
    }
    jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjErrorElimDoc"))%>');
    return;
}

function pulsarLimpiarDoc() {
  borrarDatosDoc();
  if(tabDoc.selectedIndex != -1 ) {
    tabDoc.selectLinea(tabDoc.selectedIndex);
    tabDoc.selectedIndex = -1;
  }
}
function borrarDatosDoc() {
  document.forms[0].nombreDoc.value = '';
  document.forms[0].condicion.value = '';
}
function comprobarObligatoriosDoc() {
  if(document.forms[0].nombreDoc.value == '' ) {
       return false;
  } else {
    return true;
  }
}
function crearListasDoc() {
  var listaCodigosDoc = "";
  var listaNombresDoc = "";
  var listaObligatoriosDoc = "";
  var listaCondicionDoc = "";
  var listaCodTipoDoc = "";
  for (i=0; i < listaDoc.length; i++) {
	listaNombresDoc +=listaDoc[i][0]+'§¥';
    listaCondicionDoc +=listaDoc[i][1]+'§¥';
	listaCodigosDoc += listaDocOriginal[i][0]+'§¥';
  }
  var listasDoc = new Array();
  listasDoc = [listaNombresDoc,listaCondicionDoc,listaCodigosDoc];
  return listasDoc;
}
function noObligatorioAObligatorioDoc() {

  document.forms[0].nombreDoc.className = "inputTextoObligatorio";
}
// FIN DE LOS JAVASCRIPT PERTENECIENTES A LA PESTAÑA DOCUMENTOS
// INICIO DE LOS JAVASRIPT PERTENECIENTES A LA PESTAÑA CAMPOS

function pulsarVista(){
    var nCS = "altaDesdeProcedimiento";
    var arrayAgrupaciones = crearListasAgrupaciones();
    var arrayCampos = crearListasCampos();
    
    var url = "<c:url value='/sge/DefinicionVista.do'/>";     
    var parametros = "&opcion=guardarDatosVistaSesion&nCS=" +nCS + 
            "&codCampo=" + cambiarSeparador(arrayCampos[0]) + 
            "&descCampo=" + cambiarSeparador(arrayCampos[1]) + 
            "&codTipoDato=" + cambiarSeparador(arrayCampos[3]) + 
            "&tamano=" + cambiarSeparador(arrayCampos[4]) + 
            "&agrupacionCampo=" + cambiarSeparador(arrayCampos[16]) + 
            "&campoActivo=" + cambiarSeparador(arrayCampos[9]) +
            "&posX=" + cambiarSeparador(arrayCampos[17]) + 
            "&posY=" + cambiarSeparador(arrayCampos[18]) +
            "&oculto=" + cambiarSeparador(arrayCampos[10]) +
            "&codAgrupaciones=" + cambiarSeparador(arrayAgrupaciones[0]) + 
            "&descAgrupaciones=" + cambiarSeparador(arrayAgrupaciones[1]) + 
            "&ordenAgrupaciones=" + cambiarSeparador(arrayAgrupaciones[2]);
    
    var valor = guardarDatosComposicionVistaEnSesion(url, parametros);
    if(valor==0){
        var source = "<c:url value='/sge/DefinicionVista.do?opcion=inicio'/>&nCS=" + nCS;    
    
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=1100,height=800,resizable=no,status='+ '<%=statusBar%>',function(datos){
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
                                                            arrayCampo[14],arrayCampo[15],arrayCampo[16],arrayCampo[17],arrayCampo[18],
                                                            campoCoordenadas[1],campoCoordenadas[2]];
                                            listaCamposOriginal[i] = nuevoCampo;
                                            break;
                                        }//if(arrayCampo[0] == campoCoordenadas[0])
                                    }//for (x=0; x < listaCampos.length; x++) 
                                }//if(datos[i] != undefined)
                            }//for (i=0; i < datos.length; i++) 
                        }//if(datos!=undefined) 
                    });    
    }else
    if(valor==1)
        jsp_alerta("A",'<%=descriptor.getDescripcion("getiq_sinAgrup")%>');
    else
    if(valor==2) 
       jsp_alerta("A",'<%=descriptor.getDescripcion("getiq_sinCampos")%>');        
    
}//pulsarVista

function pulsarAltaCampo() {
  var nCS = "altaDesdeProcedimiento"; 
  var codMunicipio     = document.forms[0].codMunicipio.value;
  var codProcedimiento = document.forms[0].txtCodigo.value;
  var arrayAgrupaciones = crearListasAgrupaciones();
  var source = "<c:url value='/sge/DefinicionCampo.do?opcion=inicio&nCS='/>"+nCS+"&codMunicipio="+codMunicipio+"&codProcedimiento="
      +codProcedimiento + "&codAgrupaciones="+arrayAgrupaciones[0]+
      "&descAgrupaciones="+arrayAgrupaciones[1]+"&ordenAgrupaciones="+arrayAgrupaciones[2];
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=950,height=550,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          var lista = new Array();
                          lista = [datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8],datos[9],datos[10],'SI',datos[11],
                              datos[12],datos[13],datos[14],datos[15],datos[16],datos[17]];
                          altaCampo(lista);
                        }
                    });
}
function pulsarModificarCampo() {
  var indice = tabCampos.selectedIndex;
  if(indice != -1) {
    var nCS = "modificarDesdeProcedimiento";
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
    var oculto="";
    var bloqueado="";
    var plazoFecha = "";
    var checkPlazoFecha = "";
    var validacion = "";
    var operacion = "";
    var agrupacionCampo = "";
    var listaCoordenadasX = "";
    var listaCoordenadasY = "";
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var codProcedimiento = document.forms[0].txtCodigo.value;
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
      oculto = listaCamposOriginal[indice][12];
      bloqueado = listaCamposOriginal[indice][13];
      plazoFecha = listaCamposOriginal[indice][14];
      checkPlazoFecha = listaCamposOriginal[indice][15];
      validacion = listaCamposOriginal[indice][16];      
      operacion = listaCamposOriginal[indice][17];
      
      if (validacion !== null)
      {
        validacion = replaceAll(validacion,"&lt;","<");
        validacion = replaceAll(validacion,"&gt;",">");      
        validacion = replaceAll(validacion,"+",";SUMA;");        
      }
      if (operacion !== null)
      {
        operacion =  replaceAll(operacion,"+",";SUMA;");
        operacion = replaceAll(operacion," DIAS ",";DIAS;");
        operacion = replaceAll(operacion," MESES ",";MESES;");
        operacion = replaceAll(operacion," ANOS ",";ANOS;");
      }
      agrupacionCampo = listaCamposOriginal[indice][18];
      listaCoordenadasX = listaCamposOriginal[indice][19];
      listaCoordenadasY = listaCamposOriginal[indice][20];
    }    
    var arrayAgrupaciones = crearListasAgrupaciones();
    var source = "<c:url value='/sge/DefinicionCampo.do?opcion=inicio&nCS='/>"+nCS+"&codMun="+codCampo+"&codProc="+
                 descCampo+"&codTram="+codPlantilla+"&eje="+codTipoDato+"&num="+tamano+
                 "&codClasifTram="+mascara+"&nombreCodTram="+ob+"&codPlantilla="+orden+
                 "&nombDoc="+descPlantilla+"&descTD="+descTipoDato+"&rotulo="+rotulo+"&oculto="+oculto+"&bloqueado="+bloqueado+
                 "&plazoFecha="+plazoFecha+"&checkPlazoFecha="+checkPlazoFecha+"&validacion="+validacion+"&operacion="+operacion+
                 "&codMunicipio="+codMunicipio+"&codProcedimiento="+codProcedimiento+"&agrupacionCampo="+agrupacionCampo+
                 "&codAgrupaciones="+arrayAgrupaciones[0]+
                 "&descAgrupaciones="+arrayAgrupaciones[1]+"&ordenAgrupaciones="+arrayAgrupaciones[2];
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=950,height=600,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          var lista = new Array();
                          lista = [datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8],datos[9],datos[10],'SI',
                              datos[11],datos[12],datos[13],datos[14],datos[15],datos[16],datos[17],listaCoordenadasX,listaCoordenadasY];
                          modificarCampo(lista);
                        }
                    });
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}
function verCampo(i) {
  if(document.forms[0].cmdAltaCampo.disabled == true) {
	  if(i != -1) {
	    var nCS = "modificarDesdeProcedimiento";
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
        var plazoFecha = "";
		var lectura = "si";
 		var validacion = "";
        var operacion = "";
        var codMunicipio     = document.forms[0].codMunicipio.value;
        var codProcedimiento = document.forms[0].txtCodigo.value;
	    
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
              oculto = listaCamposOriginal[indice][12];
              bloqueado = listaCamposOriginal[indice][13];
              plazoFecha = listaCamposOriginal[indice][14];
              checkPlazoFecha = listaCamposOriginal[indice][15];
              validacion = listaCamposOriginal[i][16];
              operacion = listaCamposOriginal[i][17];
              if (validacion != null)
              {
                validacion = replaceAll(validacion,"&lt;","<");
                validacion = replaceAll(validacion,"&gt;",">");  
                validacion = replaceall(validacion,"+",";SUMA;");                
              }
              if (operacion != null)
              {
                operacion = replaceall(operacion,"+",";SUMA;");
                operacion = replaceAll(operacion," DIAS ",";DIAS;");
                operacion = replaceAll(operacion," MESES ",";MESES;");
                operacion = replaceAll(operacion," ANOS ",";ANOS;");
              }
	    }              
	    var source = "<c:url value='/sge/DefinicionCampo.do?opcion=inicio&nCS='/>"+nCS+"&codMun="+codCampo+"&codProc="+
	                 descCampo+"&codTram="+codPlantilla+"&eje="+codTipoDato+"&num="+tamano+
	                 "&codClasifTram="+mascara+"&nombreCodTram="+ob+"&codPlantilla="+orden+
	                 "&nombDoc="+descPlantilla+"&descTD="+descTipoDato+"&rotulo="+rotulo+
					 "&lectura="+lectura+"&oculto="+oculto+"&bloqueado="+bloqueado+"&plazoFecha="+plazoFecha+"&checkPlazoFecha="+checkPlazoFecha+"&validacion="+validacion+"&operacion="+operacion+
                         "&codMunicipio="+codMunicipio+"&codProcedimiento="+codProcedimiento;
	    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
                                'width=650,height=475,status='+ '<%=statusBar%>',function(datos){
                                    if(datos!=undefined){
                                      var lista = new Array();
                                      lista = [datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8],datos[9],datos[10],'SI',datos[11],datos[12],datos[13], datos[14], datos[15], datos[16]];
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
      var plazoFecha = "";
      var checkPlazoFecha = "";
      var validacion = "";
      var agrupacionCampo = "";
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
        oculto = listaCamposOriginal[indice][12];
        bloqueado = listaCamposOriginal[indice][13];
        plazoFecha = listaCamposOriginal[indice][14];
        checkPlazoFecha = listaCamposOriginal[indice][15];
        validacion = listaCamposOriginal[indice][16];
        operacion = listaCamposOriginal[indice][17];
        validacion = replaceAll(validacion,"&lt;","<");
        validacion = replaceAll(validacion,"&gt;",">");
        agrupacionCampo = listaCamposOriginal[indice][18];
        listaCoordenadasX = listaCamposOriginal[indice][19];
        listaCoordenadasY = listaCamposOriginal[indice][20];
      }

        var lista = new Array();
        lista = [codCampo,descCampo,codPlantilla,codTipoDato,tamano,mascara,ob,orden,descPlantilla,descTipoDato,rotulo,'NO',
            oculto,bloqueado,plazoFecha,checkPlazoFecha,validacion,operacion,agrupacionCampo,listaCoordenadasX,listaCoordenadasY];
        modificarCampo(lista);
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');

}
<%-- Como no se borran directamente los campos (borrado lógico), cada vez que se entre en esta pagina
    lo primero que haremos será ordenar secuencialmente los números de orden para que no falle al ordenar
    comenzando en 1
--%>
function revisarNumeroOrden() {
    var orden = (0 * 1);
    for (i=0;i<listaCamposOriginal.length;i++) {
        listaCamposOriginal[i][7] = i + 1;
    }
}
function pulsarOrdenarCampo(){
  if(listaCamposOriginal.length>0){
    revisarNumeroOrden();
    var source = "<c:url value='/jsp/sge/ordenarCampos.jsp?posicionOrde=4'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,listaCamposOriginal,
	'width=670,height=500,scrollbars=no,status='+ '<%=statusBar%>',function(datosOrdenados){
                        if(datosOrdenados != undefined) {
                          listaCamposOriginal = datosOrdenados;
                          listaCampos = new Array();
                          var imagen;
                          for(m=0;m < listaCamposOriginal.length;m++) {
                              if (listaCamposOriginal[m][11] == 'SI') {
                                    imagen= '<span class="fa fa-check 2x"></span>';
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

      if (lista[11] == 'SI') {
          listaCampos[j]=[lista[0],lista[1],'<span class="fa fa-check 2x"></span>'];
      } else {
          listaCampos[j]=[lista[0],lista[1],'<span class="fa fa-close 2x"></span>'];
      }
      listaCamposOriginal[j]=[lista[0],lista[1],lista[2],lista[3],lista[4],lista[5],lista[6],ord,lista[8],lista[9],lista[10],lista[11],
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
  var ord = 0;
  var ordenNuevo = (0 * 1);<%-- para convertirlo en numerico --%>
  for(l=0; l < listaCampos.length; l++){
    if ((listaCampos[l][0]) == codigo){
      yaExiste = 1;
    }
    ordenNuevo = (listaCamposOriginal[l][7] * 1); <%-- para convertirlo en numerico --%>
    if(ord < ordenNuevo){
        ord = ordenNuevo;
    }
  }
  if(yaExiste == 0) {
    var lineas = tabCampos.lineas;
    for (i=0; i < lineas.length; i++) {
    }
    listaCampos[i]=[lista[0],lista[1],'<span class="fa fa-check 2x"></span>'];
    listaCamposOriginal[i]=[lista[0],lista[1],lista[2],lista[3],lista[4],lista[5],lista[6],((ord*1)+1),lista[8],lista[9],lista[10],
        lista[11],lista[12],lista[13],lista[14],lista[15],lista[16],lista[17],lista[18]];
    tabCampos.lineas=listaCampos;
    refrescaCampos();
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
  }
}
function activarBotonesCampo() {
  var vectorBotones = new Array(document.forms[0].cmdAltaCampo,document.forms[0].cmdModificarCampo,document.forms[0].cmdEliminarCampo,
    document.forms[0].cmdOrdenarCampo);
  habilitarGeneral(vectorBotones);
}
function desactivarBotonesCampo() {
  var vectorBotones = new Array(document.forms[0].cmdAltaCampo,document.forms[0].cmdModificarCampo,document.forms[0].cmdEliminarCampo,
    document.forms[0].cmdOrdenarCampo);
  deshabilitarGeneral(vectorBotones);
}
function borrarTablaCampos () {
  listaCampos = new Array();
  listaCamposOriginal = new Array();
  tabCampos.lineas=listaCampos;
  refrescaCampos();
}

function crearListasCampos() {
  var listaCodCampos = "";
  var listaDescCampos = "";
  var listaCodPlantilla = "";
  var listaCodTipoDato = "";
  var listaTamano = "";
  var listaMascara = "";
  var listaObligatorio = "";
  var listaOrden = "";
  var listaRotulo = "";
  var listaActivos = "";
  var listaOcultos = "";
  var listaBloqueados = "";
  var listaPlazoFecha = "";
  var listaCheckPlazoFecha = "";
  var listaValidacion = "";
  var listaOperacion = "";
  var listaAgrupacionesCampos = "";
  var listaPosicionXCampo = "";
  var listaPosicionYCampo = "";
  for (i=0; i < listaCampos.length; i++) {
    listaCodCampos += listaCamposOriginal[i][0]+'§¥';
    listaDescCampos += listaCamposOriginal[i][1]+'§¥';
    listaCodPlantilla += listaCamposOriginal[i][2]+'§¥';
    listaCodTipoDato += listaCamposOriginal[i][3]+'§¥';
    listaTamano += listaCamposOriginal[i][4]+'§¥';
    if(listaCamposOriginal[i][5] == "") {
      listaMascara += " "+'§¥';
    } else {
      listaMascara += listaCamposOriginal[i][5]+'§¥';
    }
    listaObligatorio += listaCamposOriginal[i][6]+'§¥';
    listaOrden += listaCamposOriginal[i][7]+'§¥';
    listaRotulo += listaCamposOriginal[i][10]+'§¥';
    listaActivos += listaCamposOriginal[i][11]+'§¥';
    listaOcultos += listaCamposOriginal[i][12]+'§¥';
    listaBloqueados += listaCamposOriginal[i][13]+'§¥';
    if(listaCamposOriginal[i][14] == "") {
      listaPlazoFecha += " "+'§¥';
    } else {
      listaPlazoFecha += listaCamposOriginal[i][14]+'§¥';
    }
    if(listaCamposOriginal[i][15] == "") {
      listaCheckPlazoFecha += " "+'§¥';
    } else {
      listaCheckPlazoFecha += listaCamposOriginal[i][15]+'§¥';
    }
    if(listaCamposOriginal[i][16] == ""){
        listaValidacion += " " + '§¥';        
    } else{
        listaValidacion += listaCamposOriginal[i][16]+'§¥';        
    }
    if(listaCamposOriginal[i][17] == ""){
        listaOperacion += " " + '§¥';        
    } else{
        listaOperacion += listaCamposOriginal[i][17]+'§¥';        
    }
    if(listaCamposOriginal[i][18] == ""){
        listaAgrupacionesCampos += " " + '§¥';  
    }else{
        listaAgrupacionesCampos += listaCamposOriginal[i][18]+'§¥';
    }
    listaPosicionXCampo += listaCamposOriginal[i][19]+'§¥';
    listaPosicionYCampo += listaCamposOriginal[i][20]+'§¥';
  }
  var listasCampos = new Array();

  listasCampos = [listaCodCampos,listaDescCampos,listaCodPlantilla,listaCodTipoDato,listaTamano,
                  listaMascara,listaObligatorio,listaOrden,listaRotulo,listaActivos,listaOcultos,listaBloqueados,listaPlazoFecha,
                  listaCheckPlazoFecha,listaValidacion,listaOperacion,listaAgrupacionesCampos,listaPosicionXCampo,listaPosicionYCampo];
    
    return listasCampos;
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
    var codigo = lista[0];
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
    var codigo = lista[0];
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
	'width=850,height=350,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            var lista = new Array();
                            lista = [datos[0], datos[1], datos[2],'SI'];
                            if(comprobarOrdenRepetido(datos[2])){
                                if (jsp_alerta('C', '<%=descriptor.getDescripcion("getiq_reordenargrupo")%>') == 1){
                                    modificacionAgrupacionOrdenada(lista);
                                }
                            }else{
                               //modificarAgrupacionCampos(lista); 
                               modificacionAgrupacionOrdenada(lista);
                            }//if(comprobarOrdenRepetido(datos[2]))
                        }//if(datos!=undefined)            
                    });
    }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}//pulsarModificarAgrupacion

function modificacionAgrupacionOrdenada(lista){
    var codigo = lista[0];
    var lineas = tabAgrupaciones.lineas;
    var auxListaAgrupaciones = new Array();
    var auxListaAgrupacionesOriginal = new Array();
    var reordenar = false;

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

function crearListasAgrupaciones() {
    var listaCodAgrupaciones = "";
    var listaDescAgrupaciones = "";
    var listaOrdenAgrupaciones = "";
    var listaAgrupacionesActivas = "";

    for (i=0; i < listaAgrupacionesOriginal.length; i++) {
        listaCodAgrupaciones += listaAgrupacionesOriginal[i][0]+'§¥';
        listaDescAgrupaciones += listaAgrupacionesOriginal[i][1]+'§¥';
        listaOrdenAgrupaciones += listaAgrupacionesOriginal[i][2]+'§¥';
        listaAgrupacionesActivas += listaAgrupacionesOriginal[i][3]+'§¥';
    }//for (i=0; i < listaAgrupacionesOriginal.length; i++)
    
    var listasAgrupaciones = new Array();
    listasAgrupaciones = [listaCodAgrupaciones, listaDescAgrupaciones, listaOrdenAgrupaciones, listaAgrupacionesActivas];
    return listasAgrupaciones;
}//crearListasAgrupaciones

// FIN DE LOS JAVASCRIPT PERTENECIENTES A LA PESTAÑA CAMPOS
// JAVASCRIPT PERTENECIENTES A LA PESTAÑA DE ENLACES
function activarBotonesEnlaces() {
  var vectorBotones = new Array(document.forms[0].cmdAltaEnlaces,document.forms[0].cmdModificarEnlaces,document.forms[0].cmdEliminarEnlaces,
    document.forms[0].cmdLimpiarEnlaces);
  habilitarGeneral(vectorBotones);
}
function desactivarBotonesEnlaces() {
  var vectorBotones = new Array(document.forms[0].cmdAltaEnlaces,document.forms[0].cmdModificarEnlaces,document.forms[0].cmdEliminarEnlaces,
    document.forms[0].cmdLimpiarEnlaces);
  deshabilitarGeneral(vectorBotones);
}
function borrarDatosEnlaces() {
  document.forms[0].txtDescUrl.value = '';
  document.forms[0].txtUrl.value = '';
  document.forms[0].descEstUrl.value = '';
}
function comprobarObligatoriosEnlaces() {
  if(document.forms[0].txtDescUrl.value == '' || document.forms[0].txtUrl.value == '' || document.forms[0].descEstUrl.value == '') {
    return false;
  } else {
    return true;
  }
}
function pulsarAltaEnlaces() {
  var desc = document.forms[0].txtUrl.value;
  var yaExiste = 0;
  var maxCod = 0;
  if(comprobarObligatoriosEnlaces()) {
    for(l=0; l < listaEnlaces.length; l++){
    if ((listaEnlaces[l][1]) == desc){
        yaExiste = 1;
      }
    if(maxCod < listaEnlacesOriginal[l][0]) maxCod = listaEnlacesOriginal[l][0];
    }
    if(yaExiste == 0) {
      var lineas = tabEnlaces.lineas;
      for (i=0; i < lineas.length; i++) {
      }
      var est = "";
      if(document.forms[0].descEstUrl.value == "Sí" || document.forms[0].descEstUrl.value == "Si") {
        est = "S";
      } else if(document.forms[0].descEstUrl.value == "No" || document.forms[0].descEstUrl.value == "Non") {
        est = "N";
      }
      listaEnlaces[i]=[document.forms[0].txtDescUrl.value,document.forms[0].txtUrl.value,est];
      listaEnlacesOriginal[i]=[((maxCod*1)+1),document.forms[0].txtDescUrl.value,document.forms[0].txtUrl.value,document.forms[0].descEstUrl.value];
      tabEnlaces.lineas=listaEnlaces;
      refrescaEnlaces();
      borrarDatosEnlaces();
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjUrlExiste")%>');
    }
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
  }
}
function pulsarModificarEnlaces() {
  var desc = document.forms[0].txtUrl.value;
  var yaExiste = 0;
  if(tabEnlaces.selectedIndex != -1) {
    if (comprobarObligatoriosEnlaces()) {
      for(l=0; l < listaEnlaces.length; l++){
        var lineaSeleccionada;
        lineaSeleccionada = tabEnlaces.selectedIndex;
        if(l == lineaSeleccionada) {
          l= l;
        } else {
          if ((listaEnlaces[l][1]) == desc ){
            yaExiste = 1;
          }
        }
      }
      if(yaExiste == 0) {
        var j = tabEnlaces.selectedIndex;
        var maxCod = listaEnlacesOriginal[j][0];
        var est = "";
        if(document.forms[0].descEstUrl.value == "Sí" || document.forms[0].descEstUrl.value == "Si") {
          est = "S";
        } else if(document.forms[0].descEstUrl.value == "No" || document.forms[0].descEstUrl.value == "Non") {
          est = "N";
        }
        listaEnlaces[j]=[document.forms[0].txtDescUrl.value,document.forms[0].txtUrl.value,est];
        listaEnlacesOriginal[j]=[maxCod,document.forms[0].txtDescUrl.value,document.forms[0].txtUrl.value,document.forms[0].descEstUrl.value];
        tabEnlaces.lineas=listaEnlaces;
        refrescaEnlaces();
        borrarDatosEnlaces();
      } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjUrlExiste")%>');
      }
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
    }
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}
function pulsarEliminarEnlaces() {
   if(tabEnlaces.selectedIndex != -1) {
     if(comprobarObligatoriosEnlaces()) {
       if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarEnl")%>')) {
         var list = new Array();
         var listOrig = new Array();
         tamIndex=tabEnlaces.selectedIndex;
         tamLength=tabEnlaces.lineas.length;
         for (i=tamIndex - 1; i < listaEnlaces.length - 1; i++){
           if (i + 1 <= listaEnlaces.length - 2){
             listaEnlaces[i + 1]=listaEnlaces[i + 2];
             listaEnlacesOriginal[i + 1]=listaEnlacesOriginal[i + 2];
           }
         }
         for(j=0; j < listaEnlaces.length-1 ; j++){
           list[j] = listaEnlaces[j];
           listOrig[j] = listaEnlacesOriginal[j];
         }
         tabEnlaces.lineas=list;
         refrescaEnlaces();
         borrarDatosEnlaces();
         listaEnlaces=list;
         listaEnlacesOriginal = listOrig;
       }
     } else {
       jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatos")%>');
     }
   } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}
function limpiarInputsEnlaces() {
  borrarDatosEnlaces();
  if(tabEnlaces.selectedIndex != -1 ) {
    tabEnlaces.selectLinea(tabEnlaces.selectedIndex);
    tabEnlaces.selectedIndex = -1;
  }
}
function noObligatorioAObligatorioEnlaces() {
  document.forms[0].txtDescUrl.className = "inputTextoObligatorio";
  document.forms[0].txtUrl.className = "inputTextoObligatorio";
  document.forms[0].descEstUrl.className = "inputTextoObligatorio";
}
function crearListasEnlaces() {
  var listaCodigosEnl = "";
  var listaDescripcionEnl = "";
  var listaURLEnl = "";
  var listaEstadoEnl = "";
  for (i=0; i < listaEnlaces.length; i++) {
    listaCodigosEnl +=listaEnlacesOriginal[i][0]+'§¥';
    listaDescripcionEnl +=listaEnlaces[i][0]+'§¥';
    listaURLEnl +=listaEnlaces[i][1]+'§¥';
    listaEstadoEnl +=listaEnlaces[i][2]+'§¥';
  }
  var listasEnl = new Array();
  listasEnl = [listaCodigosEnl,listaDescripcionEnl,listaURLEnl,listaEstadoEnl];
  return listasEnl;
}
// FIN DE LOS JAVASCRIPT PERTENECIENTES A LA PESTAÑA ENLACES
// JAVASCRIPT PERTENECIENTES A LA PESTAÑA DE ROLES
function activarBotonesRoles() {
  var vectorBotones = new Array(document.forms[0].cmdAltaRoles,document.forms[0].cmdModificarRoles,document.forms[0].cmdEliminarRoles,
    document.forms[0].cmdLimpiarRoles);
  habilitarGeneral(vectorBotones);
}
function desactivarBotonesRoles() {
  var vectorBotones = new Array(document.forms[0].cmdAltaRoles,document.forms[0].cmdModificarRoles,document.forms[0].cmdEliminarRoles,
    document.forms[0].cmdLimpiarRoles);
  deshabilitarGeneral(vectorBotones);
}
function borrarDatosRoles() {
  document.forms[0].codRol.value = '';
  document.forms[0].descRol.value = '';
  document.forms[0].porDefecto.checked = false;
  document.forms[0].consultaWeb.checked = false;
}
function comprobarObligatoriosRoles() {
  if(document.forms[0].codRol.value == '' || document.forms[0].descRol.value == '') {
    return false;
  } else {
    return true;
  }
}
function pulsarAltaRoles() {
  var desc = document.forms[0].codRol.value;
  var yaExiste = 0;
  var yaExistePorDefecto = 0;
  if (campoCorrecto(document.forms[0].descRol.value))
  {
  if(comprobarObligatoriosRoles()) {
    for(l=0; l < listaRoles.length; l++){
      if ((listaRoles[l][0]) == desc){
        yaExiste = 1;
      }
	  if(listaRoles[l][2] == "X") {
	    yaExistePorDefecto = 1;
	  }
    }
    if(yaExiste == 0) {
      var lineas = tabRoles.lineas;
      for (i=0; i < lineas.length; i++) {
      }
	  var pD = "";
	  var pDe = 0;
          var cw = "";
          var pCw = 0;
	  if(document.forms[0].porDefecto.checked == true) {
	    pD = "X";
		pDe = 1;
	  }
	  if(document.forms[0].consultaWeb.checked == true) {
	    cw = "X";
		pCw = 1;
	  }
	  if(yaExistePorDefecto == 1 && pD =="X") {
	    jsp_alerta('A','<%=descriptor.getDescripcion("msjYaExPD")%>');
	  } else {
        listaRoles[i]=[document.forms[0].codRol.value,document.forms[0].descRol.value,pD,cw];
        listaRolesOriginal[i]=[document.forms[0].codRol.value,document.forms[0].descRol.value,pDe,pCw];
        tabRoles.lineas=listaRoles;
        refrescaRoles();
        borrarDatosRoles();
	  }
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjCodRolExiste")%>');
    }
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
  }
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjDesRolError")%>');
}
function pulsarModificarRoles()
{
  var desc = document.forms[0].codRol.value;
  var yaExiste = 0;
  var yaExistePorDefecto = 0;
  if (campoCorrecto(document.forms[0].descRol.value))
  {
  if(tabRoles.selectedIndex != -1) {
    if (comprobarObligatoriosRoles()) {
      for(l=0; l < listaRoles.length; l++){
        var lineaSeleccionada;
        lineaSeleccionada = tabRoles.selectedIndex;
        if(l == lineaSeleccionada) {
          l= l;
        } else {
          if ((listaRoles[l][0]) == desc ){
            yaExiste = 1;
          }
        }
		if(listaRoles[l][2] == "X" && l!=lineaSeleccionada) {
	      yaExistePorDefecto = 1;
	    }
      }
      if(yaExiste == 0) {
        var j = tabRoles.selectedIndex;
		var pD = "";
	    var pDe = 0;
            var cw = "";
            var pCw = 0;
	    if(document.forms[0].porDefecto.checked == true) {
	      pD = "X";
		  pDe = 1;
	    }
	    if(document.forms[0].consultaWeb.checked == true) {
	      cw = "X";
		  pCw = 1;
	    }
		if(yaExistePorDefecto == 1 && pD =="X") {
	      jsp_alerta('A','<%=descriptor.getDescripcion("msjYaExPD")%>');
	    } else {
          listaRoles[j]=[document.forms[0].codRol.value,document.forms[0].descRol.value,pD,cw];
          listaRolesOriginal[j]=[document.forms[0].codRol.value,document.forms[0].descRol.value,pDe,pCw];
          tabRoles.lineas=listaRoles;
          refrescaRoles();
          borrarDatosRoles();
		}
      } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjCodRolExiste")%>');
      }
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
    }
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjDesRolError")%>');
}
function comprobarRolDefecto() {
  var yaExistePorDefecto = 0;
  for(l=0; l < listaRoles.length; l++){
	if(listaRoles[l][2] == "X" ) {
	  yaExistePorDefecto = 1;
	  break;
	}
  }
  return yaExistePorDefecto;
}
function pulsarEliminarRoles() {
   if(tabRoles.selectedIndex != -1) {
     if(comprobarObligatoriosRoles()) {
       if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarRol")%>')) {
         var list = new Array();
         var listOrig = new Array();
         tamIndex=tabRoles.selectedIndex;
         tamLength=tabRoles.lineas.length;
         for (i=tamIndex - 1; i < listaRoles.length - 1; i++){
           if (i + 1 <= listaRoles.length - 2){
             listaRoles[i + 1]=listaRoles[i + 2];
             listaRolesOriginal[i + 1]=listaRolesOriginal[i + 2];
           }
         }
         for(j=0; j < listaRoles.length-1 ; j++){
           list[j] = listaRoles[j];
           listOrig[j] = listaRolesOriginal[j];
         }
         tabRoles.lineas=list;
         refrescaRoles();
         borrarDatosRoles();
         listaRoles=list;
         listaRolesOriginal = listOrig;
       }
     } else {
       jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatos")%>');
     }
   } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}
function limpiarInputsRoles() {
  borrarDatosRoles();
  if(tabRoles.selectedIndex != -1 ) {
    tabRoles.selectLinea(tabRoles.selectedIndex);
    tabRoles.selectedIndex = -1;
  }
}
function noObligatorioAObligatorioRoles() {
  document.forms[0].codRol.className = "inputTextoObligatorio";
  document.forms[0].descRol.className = "inputTextoObligatorio";
}
function crearListasRoles() {
  var listaCodigosRol = "";
  var listaDescripcionRol = "";
  var listaPorDefecto = "";
  var listaConsultaWebRol = "";
  for (i=0; i < listaRoles.length; i++) {
    listaCodigosRol +=listaRoles[i][0]+'§¥';
    listaDescripcionRol +=listaRoles[i][1]+'§¥';
    if(listaRoles.length == 1) {
      listaPorDefecto += '1'+'§¥';
    } else {
      listaPorDefecto += listaRolesOriginal[i][2]+'§¥';
    }
    listaConsultaWebRol +=listaRolesOriginal[i][3]+'§¥';
  }
  var listasRol = new Array();
  listasRol = [listaCodigosRol,listaDescripcionRol,listaPorDefecto,listaConsultaWebRol];
  return listasRol;
}
// FIN DE LOS JAVASCRIPT PERTENECIENTES A LA PESTAÑA ROLES

function pulsarImportar() {
	clean();

	pleaseWait('on');
	var codMunicipio =  document.forms[0].codMunicipio.value;
	// Se importa desde el entorno
	document.forms[0].codMunicipio.value = codMunicipio;
	document.forms[0].codAplicacion.value = <%= aplicacion %>;
	document.forms[0].importar.value = "si";
	document.forms[0].opcion.value="importarProcedimiento";
	if (document.forms[0].tipoConsulta[0].checked)
		document.forms[0].target="mainFrame";
	else
		document.forms[0].target="oculto";

	document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
	document.forms[0].submit();
}

function pulsarDescargar() { 
  activarFormulario();

  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {      
      borrarDatosDoc();
      borrarDatosEnlaces();
      
      var listasDoc = crearListasDoc();
      document.forms[0].listaNombresDoc.value = listasDoc[0];
      document.forms[0].listaCondicionDoc.value = listasDoc[1];
      document.forms[0].listaCodigosDoc.value = listasDoc[2];
      
      var listasCampos = crearListasCampos();
      document.forms[0].listaCodCampos.value = listasCampos[0];
      document.forms[0].listaDescCampos.value = listasCampos[1];
      document.forms[0].listaCodPlantilla.value = listasCampos[2];
      document.forms[0].listaCodTipoDato.value = listasCampos[3];
      document.forms[0].listaTamano.value = listasCampos[4];
      document.forms[0].listaMascara.value = listasCampos[5];
      document.forms[0].listaObligatorio.value = listasCampos[6];
      document.forms[0].listaOrden.value = listasCampos[7];
      document.forms[0].listaRotulo.value = listasCampos[8];
      document.forms[0].listaActivos.value = listasCampos[9];
      document.forms[0].listaOcultos.value = listasCampos[10];
      document.forms[0].listaBloqueados.value = listasCampos[11];
      document.forms[0].listaPlazoFecha.value = listasCampos[12];
      document.forms[0].listaCheckPlazoFecha.value = listasCampos[13];
      document.forms[0].listaValidacion.value = listasCampos[14];
      document.forms[0].listaOperacion.value = listasCampos[15];      
      document.forms[0].listaAgrupacionesCampos.value = listasCampos[16];
      document.forms[0].listaPosicionesX.value = listasCampos[17];
      document.forms[0].listaPosicionesY.value = listasCampos[18];      
      var listasAgrupaciones = crearListasAgrupaciones();
      document.forms[0].listaCodAgrupaciones.value = listasAgrupaciones[0];
      document.forms[0].listaDescAgrupaciones.value = listasAgrupaciones[1];
      document.forms[0].listaOrdenAgrupaciones.value = listasAgrupaciones[2];
      document.forms[0].listaAgrupacionesActivas.value = listasAgrupaciones[3];

      var listasEnlaces = crearListasEnlaces();
      document.forms[0].listaCodEnlaces.value = listasEnlaces[0];
      document.forms[0].listaDescEnlaces.value = listasEnlaces[1];
      document.forms[0].listaUrlEnlaces.value = listasEnlaces[2];
      document.forms[0].listaEstadoEnlaces.value = listasEnlaces[3];
      var listasRoles = crearListasRoles();
      document.forms[0].listaCodRol.value = listasRoles[0];
      document.forms[0].listaDescRol.value = listasRoles[1];
      document.forms[0].listaPorDefecto.value = listasRoles[2];
      document.forms[0].listaConsultaWebRol.value = listasRoles[3];

      if(importarProcConEleccion!=null && (importarProcConEleccion=="si" || importarProcConEleccion=="SI")){
          // Se realiza la importacion con la eleccion del entorno
          document.forms[0].opcion.value="descargarConEleccionOrganizacion";

      }else{
          // Se realiza la importacion desde un entorno de real/producción de un procedimiento del entorno de pruebas
          document.forms[0].opcion.value="descargar";
      }
      
      document.forms[0].codMunicipio.value = <%= municipio %>;
      document.forms[0].target="oculto";
      document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
      document.forms[0].submit();
  }
}
// Pestañas
function antesDeCambiarPestana() {
  ocultarCalendario();
}
function pulsarCancelarCatalogo() {
  top.close();
}



/* *** Arbol de tramites ****** */
  var tramitesTree;
  var tramites = new Array(); // Tramites q se consultan. Indexados x nodo del arbol.
  var icoTram = 'fa fa-file-o';
  var icoClassTram = 'fa fa-clipboard';
  var icoListaTram = 'fa fa-archive';
  var imgBusq ='fa fa-search-plus';
  function pintaTramitesProcedimiento(listaTramites){
    var clases = new Array();
    var tramite;
    var clase;
    var i;
    for (i=0; i < listaTramites.length; i++ ) {

      clase = clases[listaTramites[i][5]];
      if (clase == undefined) {
        clase = new WebFXTreeItem(listaTramites[i][6],'',tramitesTree,icoClassTram,icoClassTram,'webfx-tree-item-tramite-II');
        clases[listaTramites[i][5]]=clase;
      }

      // Los tramites del primer nodo se pueden consultar.
      var nmbf =  "consultarTramite";
      tramite = new WebFXImageTreeItem(listaTramites[i][4],null,clase,icoTram,icoTram,'webfx-tree-item-tramite-II', imgBusq,nmbf);
      tramites[tramite.id]=[listaTramites[i][0],listaTramites[i][1],listaTramites[i][2],listaTramites[i][3],listaTramites[i][4]];
    }

    tramitesTree.expand();
}
function borrarTramitesArbol(){
  list = new Array();
  if (tramitesTree != null) {
    for (var i = tramitesTree.childNodes.length - 1; i >= 0; i--) {
      tramitesTree.childNodes[i]._remove();
    }
  }

}
function desHabilitarImagenesArbol(b){
  if (tramitesTree != null && tramitesTree.childNodes.length != 0) {
    tramitesTree.desHabilitarImagenesAll(b);
  }
}
function consultarTramite(id){
  if(document.forms[0].importar.value == "si") {
    jsp_alerta("A",'No se pueden visualizar los trámites desde una importación.');
  } else {
    var tramite = tramites[id];
    if (tramite != undefined){
        pleaseWait('on');
        enviaTramite(tramite[2],tramite[3],tramite[4]);
    }
  }
}

/* ****** Fin arbol de tramites */
function pulsarDuplicar() {
  var source = "<c:url value='/jsp/sge/nuevoCodigo.jsp?opcion=null'/>";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
        'width=600,height=250,status='+ '<%=statusBar%>',function(datos){
                if(datos!=undefined){
                      document.forms[0].nuevoCodProcedimiento.value = datos[0];
                      duplicar();
                }
          });
}
function duplicar() {
  activarFormulario();
  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {

      borrarDatosDoc();
      borrarDatosEnlaces();
      var listasDoc = crearListasDoc();
      document.forms[0].listaNombresDoc.value = listasDoc[0];
      document.forms[0].listaCondicionDoc.value = listasDoc[1];
      document.forms[0].listaCodigosDoc.value = listasDoc[2];
      var listasCampos = crearListasCampos();
      document.forms[0].listaCodCampos.value = listasCampos[0];
      document.forms[0].listaDescCampos.value = listasCampos[1];
      document.forms[0].listaCodPlantilla.value = listasCampos[2];
      document.forms[0].listaCodTipoDato.value = listasCampos[3];
      document.forms[0].listaTamano.value = listasCampos[4];
      document.forms[0].listaMascara.value = listasCampos[5];
      document.forms[0].listaObligatorio.value = listasCampos[6];
      document.forms[0].listaOrden.value = listasCampos[7];
      document.forms[0].listaRotulo.value = listasCampos[8];
      document.forms[0].listaActivos.value = listasCampos[9];
      document.forms[0].listaOcultos.value = listasCampos[10];
      document.forms[0].listaBloqueados.value = listasCampos[11];
      document.forms[0].listaPlazoFecha.value = listasCampos[12];
      document.forms[0].listaCheckPlazoFecha.value = listasCampos[13];
      document.forms[0].listaValidacion.value = listasCampos[14];
      document.forms[0].listaOperacion.value = listasCampos[15];
      document.forms[0].listaAgrupacionesCampos.value = listasCampos[16];
      document.forms[0].listaPosicionesX.value = listasCampos[17];
      document.forms[0].listaPosicionesY.value = listasCampos[18];
      
      var listasAgrupaciones = crearListasAgrupaciones();
      document.forms[0].listaCodAgrupaciones.value = listasAgrupaciones[0];
      document.forms[0].listaDescAgrupaciones.value = listasAgrupaciones[1];
      document.forms[0].listaOrdenAgrupaciones.value = listasAgrupaciones[2];
      document.forms[0].listaAgrupacionesActivas.value = listasAgrupaciones[3];

      var listasEnlaces = crearListasEnlaces();
      document.forms[0].listaCodEnlaces.value = listasEnlaces[0];
      document.forms[0].listaDescEnlaces.value = listasEnlaces[1];
      document.forms[0].listaUrlEnlaces.value = listasEnlaces[2];
      document.forms[0].listaEstadoEnlaces.value = listasEnlaces[3];
      var listasRoles = crearListasRoles();
      document.forms[0].listaCodRol.value = listasRoles[0];
      document.forms[0].listaDescRol.value = listasRoles[1];
      document.forms[0].listaPorDefecto.value = listasRoles[2];
      document.forms[0].listaConsultaWebRol.value = listasRoles[3];
      document.forms[0].opcion.value="duplicar";
      document.forms[0].target="oculto";
      document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
      document.forms[0].submit();

  }
}
function duplicacionRealizada() {
  document.forms[0].importar.value = "no";
  document.forms[0].listadoImportar.value = "";
  document.forms[0].txtCodigo.value = document.forms[0].nuevoCodProcedimiento.value;
  document.forms[0].opcion.value="consultar";
  document.forms[0].tipoConsulta[1].checked = true;
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
  document.forms[0].submit();
}
function pulsarImprimir() {
  var vector = new Array(document.forms[0].txtCodigo);
  habilitarGeneral(vector);
  pleaseWait('on');
  document.forms[0].opcion.value="imprimirProcedimiento";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
  document.forms[0].submit();
  deshabilitarGeneral(vector);
}
function pulsarBiblioteca(){
    var txtCodigo=document.forms[0].txtCodigo.value;
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/procedimiento/bibliotecaFlujoTramitacion.do'/>?opcion=getProcedimientosConsulta&txtCodigo="+txtCodigo;
    document.forms[0].submit();
}
function abrirInforme(nombre){
  // PDFS NUEVA SITUACION
  var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&nombre='/>"+nombre;
  ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source="+source,'ventana','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no');
  ventanaInforme.focus();
  // FIN PDFS NUEVA SITUACION
}


function CaracterValidos(objeto) {
   var valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ';
   var numeros='1234567890';
   
   if(consultando == true) {
	  valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ*&|';
   } else if(consultando == false) {
	  valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ';
   }

    if (objeto){
        var original = objeto.value;

        var salida = "";
        for(i=0;i<original.length;i++){

            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1 || original.charAt(i)==" "){
               var valido = true;
               if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1 && original.length==1) valido = false;
               if(valido) salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
    }
}

function campoCorrecto(campo)
{
   var caracter;
   valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ ';
   var numeros='1234567890';

   var longitud = Trim(campo).length;
   for (var i=0;i < longitud;i++)
   {
   	 caracter = campo.charAt(i);
   	 if (i == 0)
   	 {
   	   if(numeros.indexOf(caracter) != -1)
	  	{return false;}
       if (valores.indexOf(caracter) == -1)
        {return false;}
   	 }
   	 else
   	 {
       if (valores.indexOf(caracter) == -1)
        {return false;}
   	 }
	}
     return true;
}


function onClickUnidadesPlazo() {
    var cTipoPlazo_1 = document.forms[0].tipoPlazo[0].checked;
    var cTipoPlazo_2 = document.forms[0].tipoPlazo[1].checked;
    var cTipoPlazo_3 = document.forms[0].tipoPlazo[2].checked;
    var plazo = document.forms[0].plazo.value;

    if (plazo=='' && plazo.length==0){
        document.forms[0].tipoPlazo[0].checked = false;
        document.forms[0].tipoPlazo[1].checked = false;
        document.forms[0].tipoPlazo[2].checked = false;
        document.forms[0].porcentaje.value = "";
        document.forms[0].porcentaje.disabled = true;

        document.getElementById("et1").className  = 'etiquetaDeshabilitada';
        document.getElementById("et2").className ='etiquetaDeshabilitada';

    }
    if((cTipoPlazo_1 || cTipoPlazo_2 || cTipoPlazo_3) && plazo!=null && plazo.length>0>0){
            document.forms[0].porcentaje.disabled=false;
            document.getElementById("et1").className = 'etiqueta';
            document.getElementById("et2").className ='etiqueta';
    }
}


function actualizaAnotacionProcedimientoRealImportacionNavegacion(procedimientoSelecc) {
  if (numProcedimientos>0) {
    document.forms[0].codAplicacion.value = <%= aplicacion %>
    document.forms[0].codMunicipio.value = codMunic;
    document.forms[0].txtCodigo.value = codProc;
    var vector = new Array(document.forms[0].txtCodigo);
    habilitarGeneral(vector);
    document.forms[0].opcion.value="buscar_procedimiento_real_importar";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
    document.forms[0].submit();
    procedimientoActual = procedimientoSelecc;
  } else if (numProcedimientos == -1) {
    numProcedimientos = 1;
    mostrarCapasBotones('capaBotones2');
    desactivarFormulario();
  } else {
    mostrarCapasBotones('capaBotones2');
    desactivarFormulario();
    document.forms[0].cmdNuevoTram.disabled = true;
    document.forms[0].cmdNuevoTram.className = "botonGeneralDeshabilitado";
    document.forms[0].cmdModificar.disabled = true;
    document.forms[0].cmdEliminar.disabled = true;
  }
  domlay('capaNavegacionConsulta',1,0,0,navegacionConsulta());
}


function validarProcRestringido(){
    if(document.forms[0].restringido.checked)
        document.forms[0].restringido.value = "1";
    else
        document.forms[0].restringido.value = "0";
}

function validarProcBiblioteca(){
    if(document.forms[0].biblioteca.checked)
        document.forms[0].biblioteca.value = "1";
    else
        document.forms[0].biblioteca.value = "0";
}

function validarProcExpNumeracion(){
    var input = $("#numeracionExpedientesAnoAsiento");
    if(input.is(":checked")){
        input.val("1");
    } else {
        input.val("0");
    }
    console.dir(input);
}

function pulsarAltaCampoVistaPendientes(){	
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var codProcedimiento = document.forms[0].txtCodigo.value;	
	
    var source = "<c:url value='/DefinicionCamposVistaPendientesProcedimiento.do?opcion=entrada'/>" + "&codProcedimiento=" + codProcedimiento
				  + "&codMunicipio=" + codMunicipio;
	    
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,listaCamposSeleccionados,
	'width=982,height=650,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){        
                            if(datos!=null){
                                var codigos  = datos[0];
                                var nombres  = datos[1];
                                var anchura  = datos[2];
                                var activo   = datos[3];
                                var campoSup = datos[4];
                                actualizarVistaPendientes(codigos,nombres,anchura,activo,campoSup);
                            }
                        }
                    });
}


function actualizarVistaPendientes(codigos,nombres,anchura,activo,campoSup){ 
	var separador = '§¥';
	var contenidoTabla = new Array();
		
        var lCodigos  = codigos.split(separador);
	var lNombres  = nombres.split(separador);
	var lAnchura  = anchura.split(separador);
	var lActivo   = activo.split(separador);
	var lCampoSup = campoSup.split(separador);	
		
	if(lCodigos!=null && lNombres!=null && lAnchura!=null && lActivo!=null
		&& lCodigos.length>0 && lNombres.length>0 && lAnchura.length>0 && lActivo.length>0){		
		
		var mCodigos = "";
		var mNombres = "";		
		var mTamanho = "";
		var mOrden   = "";
		var mActivo  = "";
		var mCampo   = "";
		
		for(i=0;i<lCodigos.length;i++){				
			if(lCodigos[i]!="" && lCodigos[i].length>0 && lNombres[i]!="" && lNombres[i].length>0 && lActivo[i]!="" && lActivo.length>0 && lActivo[i].toUpperCase()=="SI"){                                
				contenidoTabla[i] = [lCodigos[i],lNombres[i],lAnchura[i] + '%'];
				mCodigos += lCodigos[i] + separador;
				mNombres += lNombres[i] + separador;
				mTamanho += lAnchura[i] + separador;
				mActivo  += lActivo[i] + separador;
				mCampo   += lCampoSup[i] + separador;
			}
		}//for
		
		tabPendientes.lineas = contenidoTabla;
		tabPendientes.displayTabla(); 			

                var orden = new Array();
                var valor = 1;

                for(j=0;j<lCodigos.length;j++){
                    orden[j]=valor;
                    valor++;
                }                

                actualizarListaCamposSeleccionados(lCodigos,lNombres,lAnchura,orden,lCampoSup);

		// Se actualiza la lista a partir de la cual se saben cuales son los campos a grabar
		document.forms[0].listaCodigosCamposPendientes.value  = mCodigos;
		document.forms[0].listaNombresCamposPendientes.value  = mNombres;
		document.forms[0].listaTamanhoCamposPendientes.value  = mTamanho;	
                
                document.forms[0].listaOrdenCamposPendientes.value    = getOrdenCamposVistaPendientes();
                
		document.forms[0].listaCampoSupCamposPendientes.value = mCampo;                
                mostrarPorcentajeAcumulado();
		
	}//if	
}

function mostrarPorcentajeAcumulado(){ 
    var contador = 0;;
    for(i=0;i<listaCamposSeleccionados.length;i++){
        contador = parseInt(contador) + parseInt(listaCamposSeleccionados[i][2]);
    }
    
    document.getElementById("divPorcentaje").innerHTML = "<%=descriptor.getDescripcion("etiqPorcentajeAcumulado")%>: " + contador + "%";

}

function actualizarListaCamposSeleccionados(codigos,nombres,tamanho,orden,campoSup){	
    listaCamposSeleccionados = new Array();

    for(i=0;codigos!=null && i<codigos.length;i++){

        var elCodigo = codigos[i];
        var elNombre = nombres[i];
        var elSize   = tamanho[i];
        var elOrden  = orden[i];
        var elCampo  = campoSup[i];

        if(elCodigo!=null && elCodigo!="" && elCodigo.length>0 && elNombre!=null && elNombre!="" && elNombre.length>0){
            listaCamposSeleccionados[i] = [elCodigo,elNombre,elSize,"SI",elOrden,"",elCampo];
        }
    }//for
}

function pulsarEliminarCampoVistaPendientes(){	
    var indice = tabPendientes.selectedIndex;
    if(indice==-1){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoCampoSelEliminar")%>');
    }else{
        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msgEliminarCampoPendiente")%>')){
            var auxiliar = new Array();
            var lineas   = new Array();
            var contador = 0;
            for(j=0;j<listaCamposSeleccionados.length;j++){
                if(j!=indice){
                        auxiliar[contador] = listaCamposSeleccionados[j];
                        contador++;
                }
            }//for

            listaCamposSeleccionados = auxiliar;
            actualizarTablaVistaPendientesCamposSeleccionados();
            actualizarFormulariosCamposVistaPendientes();
            mostrarPorcentajeAcumulado();
        }
    }
}


function actualizarFormulariosCamposVistaPendientes(){	
    var separador = '§¥';
    var mCodigos="";
    var mNombres="";
    var mTamanho="";
    var mOrden="";
    var mCampo="";
    
    for(i=0;i<listaCamposSeleccionados.length;i++){
		if(listaCamposSeleccionados[i][0]!="" && listaCamposSeleccionados[i][0].length>0 && listaCamposSeleccionados[i][1]!="" && listaCamposSeleccionados[i][1].length>0){
				mCodigos += listaCamposSeleccionados[i][0] + separador;
				mNombres += listaCamposSeleccionados[i][1] + separador;
				mTamanho += listaCamposSeleccionados[i][2] + separador;
				mCampo   += listaCamposSeleccionados[i][6] + separador;
		}
    }

    // Se actualiza la lista a partir de la cual se saben cuales son los campos a grabar
    document.forms[0].listaCodigosCamposPendientes.value  = mCodigos;
    document.forms[0].listaNombresCamposPendientes.value  = mNombres;
    document.forms[0].listaTamanhoCamposPendientes.value  = mTamanho;
    document.forms[0].listaOrdenCamposPendientes.value    = getOrdenCamposVistaPendientes();
    document.forms[0].listaCampoSupCamposPendientes.value = mCampo;
}


function getOrdenCamposVistaPendientes(){ 
    var separador = '§¥';

    // No hay orden, por tanto, se establece el orden de forma correlativa
    var indice = 1;
    var sOrden = "";
    for(j=0;j<listaCamposSeleccionados.length;j++){
        sOrden = sOrden + indice + separador;
        indice++;
    }
    return sOrden;
}


function desactivarBotonesVistaPendientes() {
  var vectorBotones = new Array(document.forms[0].btnAlta,document.forms[0].btnEliminar,document.forms[0].btnOrdenar);
  deshabilitarGeneral(vectorBotones);
}


function pulsarOrdenarCamposVistaPendientes(){
    var source = "<c:url value='/jsp/sge/ordenarCampos.jsp?posicionOrde=4'/>";
    
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,listaCamposSeleccionados,
	'width=670,height=500,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){

                            // La lista ordenada es la nueva lista de campos seleccionados
                            listaCamposSeleccionados = datos;        
                            actualizarFormulariosCamposVistaPendientes();
                            mostrarPorcentajeAcumulado();
                            var contenidoTabla = new Array();
                            for(i=0;i<listaCamposSeleccionados.length;i++){
                                contenidoTabla[i] = [listaCamposSeleccionados[i][0],listaCamposSeleccionados[i][1],listaCamposSeleccionados[i][2] + '%'];
                            }//for

                            tabPendientes.lineas = contenidoTabla;
                            tabPendientes.displayTabla();
                        }
                    });
}
</script>
   

</head>
<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
          inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/sge/DefinicionProcedimientos.do" target="_self">
<html:hidden  property="codUnidadInicio"   value=""/>
<html:hidden  property="descUnidadInicio"   value=""/>
<html:hidden  property="tipo_select"   value=""/>
<html:hidden  property="col_cod"   value=""/>
<html:hidden  property="col_desc"   value=""/>
<html:hidden  property="nom_tabla"   value=""/>
<html:hidden  property="input_cod"   value=""/>
<html:hidden  property="input_desc"   value=""/>
<html:hidden  property="column_valor_where" value=""/>
<html:hidden  property="whereComplejo" value=""/>
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codigoTramite" value=""/>
<html:hidden  property="numeroTramite" value=""/>
<html:hidden  property="nombreTramite" value=""/>
<html:hidden  property="enviar" value=""/>
<html:hidden property="posicionProcedimiento" value=""/>
<html:hidden property="codMunicipio" value="" />
<html:hidden property="codAplicacion" value="" />
<html:hidden property="importar" />
<html:hidden property="noModificar" />
<html:hidden property="deCatalogo" />
<html:hidden property="deCatalogoDeProcedimiento" value="no"/>
<html:hidden property="tramiteInicio" />
<html:hidden property="claseBuzonEntradaHistorico" value="" />
<input type="hidden" name="listadoImportar" value="">
<input type="hidden" name="nuevoCodProcedimiento">
<input type="hidden" name="ultimo" value="no">
<input type="hidden" name="listaNombresDoc" value="">
<input type="hidden" name="listaCodigosDoc" value="">
<input type="hidden" name="listaObligatoriosDoc" value="">
<input type="hidden" name="listaCondicionDoc" value="">
<input type="hidden" name="listaCodTipoDoc" value="">
<input type="hidden" name="listaCodCampos" value="">
<input type="hidden" name="listaDescCampos" value="">
<input type="hidden" name="listaCodPlantilla" value="">
<input type="hidden" name="listaCodTipoDato" value="">
<input type="hidden" name="listaTamano" value="">
<input type="hidden" name="listaMascara" value="">
<input type="hidden" name="listaObligatorio" value="">
<input type="hidden" name="listaOrden" value="">
<input type="hidden" name="listaRotulo" value="">
<input type="hidden" name="listaActivos" value="">
<input type="hidden" name="listaCodEnlaces" value="">
<input type="hidden" name="listaDescEnlaces" value="">
<input type="hidden" name="listaUrlEnlaces" value="">
<input type="hidden" name="listaEstadoEnlaces" value="">
<input type="hidden" name="listaCodRol" value="">
<input type="hidden" name="listaDescRol" value="">
<input type="hidden" name="listaConsultaWebRol" value="">
<input type="hidden" name="listaPorDefecto" value="">
<input type="hidden" name="listaCodUnidadesInicio" value="">
<input type="hidden" name="listaCodVisibleUnidadesInicio" value="">
<input type="hidden" name="listaDescUnidadesInicio" value="">
<input type="hidden" name="listaOcultos" value="">
<input type="hidden" name="listaBloqueados" value="">
<input type="hidden" name="listaPlazoFecha" value="">
<input type="hidden" name="listaCheckPlazoFecha" value="">
<input type="hidden" name="listaValidacion" value="">
<input type="hidden" name="listaOperacion" value="">
<input type="hidden" name="listaAgrupacionesCampos" value="">
<input type="hidden" name="listaPosicionesX" value="">
<input type="hidden" name="listaPosicionesY" value="">
<input type="hidden" name="listaCodAgrupaciones" value="">
<input type="hidden" name="listaDescAgrupaciones" value="">
<input type="hidden" name="listaOrdenAgrupaciones" value="">
<input type="hidden" name="listaAgrupacionesActivas" value="">
<input type="hidden" name="listaCantidadImporte" value="">
<input type="hidden" name="listaCampoSuplImporte" value="">
<input type="hidden" name="listaCodTramImporte" value="">
<input type="hidden" name="listaRango1Importe" value="">
<input type="hidden" name="listaRango2Importe" value="">			
<input type="hidden" name="listaCodigosCamposPendientes"  value=""/>
<input type="hidden" name="listaNombresCamposPendientes"  value=""/>
<input type="hidden" name="listaTamanhoCamposPendientes"  value=""/>
<input type="hidden" name="listaOrdenCamposPendientes"    value=""/>
<input type="hidden" name="listaCampoSupCamposPendientes" value=""/>
<input type="hidden" name="implClassServicioFinalizacion" value=""/>          
<div class="txttitblanco"><%=descriptor.getDescripcion("tit_defProc")%></div>
<div class="contenidoPantalla">
    <div class="tab-pane" id="tab-pane-1">
        <script type="text/javascript">
            tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
        </script>
        <div class="tab-page" id="tabPage1" >
            <h2 class="tab"><%=descriptor.getDescripcion("res_pestana1")%></h2>
            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
            <TABLE id ="tablaDatosGral" class="contenidoPestanha">
                <tr>
                <TD style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_codigo")%>:</TD>
                    <TD style="width: 80%" class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtCodigo" style="width:12%" maxlength="5"
                                   onkeyup="return CaracterValidos(this);"/>
                    </TD>
                </tr>
                <!-- Campo Descripcion -->
                <tr style="margin-top:2px;">
                    <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiq_desc")%>:</TD>
                    <TD class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtDescripcion" style="width:100%" maxlength="200" 
                                   onblur="javascript:{return xAMayusculas(this);copiaDescripcion();}"/>
                    </TD>
                </tr>
                    <!-- Campo Descripcion Breve -->
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiq_DescBrv")%>:</TD>
                        <TD class="columnP">
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descripcionBreve" style="width:100%" maxlength="75"
                                       onfocus="this.select()" onblur="return xAMayusculas(this);"/>
                        </TD>
                    </tr>

                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fechaVigD")%>:</TD>
                        <TD class="columnP">
                            <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" style="width:12%" maxlength="25" property="fechaLimiteDesde"
                                       onkeyup = "if(consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFechaVigencia(this);"
                                       onfocus = "this.select();"/>
                            <A href="javascript:calClick();return false;" onClick="mostrarCalFechaLimiteDesde(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                                <span class="fa fa-calendar" aria-hidden="true" id="calFechaLimiteDesde" name="calFechaLimiteDesde" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' ></span>
                            </A>
                        </TD>
                    </tr>
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fechaVigH")%>:</TD>
                        <TD class="columnP">
                            <html:text styleClass="inputTxtFecha" style="width:12%" maxlength="25" property="fechaLimiteHasta"
                                       onkeyup = "if(consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFechaVigencia();"
                                       onfocus = "this.select();"/>
                            <A href="javascript:calClick();return false;" onClick="mostrarCalFechaLimiteHasta(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                                <span class="fa fa-calendar" aria-hidden="true" id="calFechaLimiteHasta" name="calFechaLimiteHasta" alt="Data" ></span>
                            </A>
                        </TD>
                    </tr>
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_area")%>:</TD>
                        <TD class="columnP">
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codArea" style="width:8%"
                                       onkeyup="return SoloDigitos(this);"/>
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descArea" style="width:85%" readonly="true"/>
                            <A href="" id="anchorArea" name="anchorArea">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonArea" name="botonArea"></span>
                            </A>
                        </TD>
                    </tr>
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tipoProc")%>:</TD>
                        <TD class="columnP">
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codTipoProcedimiento" style="width:8%"
                                       onkeyup="return SoloDigitos(this);"/>
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descTipoProcedimiento" style="width:85%" readonly="true"/>
                            <A href="" id="anchorTipoProcedimiento" name="anchorTipoProcedimiento">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoProcedimiento" name="botonTipoProcedimiento"></span>
                            </A>
                        </TD>
                    </tr>
                    <tr>
                        <td class="etiqueta"><%=descriptor.getDescripcion("etiq_prNot")%>: </td>
                        <td class="columnP">

                         <table style="width:100%">
                         <tr>
                             <td style="width: 8%" class="columnP">
                                 <html:text styleClass="inputTexto" property="plazo" style="width:100%" maxlength="2" onkeyup="return SoloDigitos(this);" onchange="javascript:onClickUnidadesPlazo();"/>
                             </td>
                             <td style="width: 15%" class="columnP">
                                 <html:radio property="tipoPlazo" styleClass="textoSuelto" value="1" onclick="onClickUnidadesPlazo();"/> <%=descriptor.getDescripcion("etiq_plzHab")%>&nbsp;
                             </td>
                             <td style="width: 16%" class="columnP">
                                 <html:radio property="tipoPlazo" styleClass="textoSuelto" value="2" onclick="onClickUnidadesPlazo();"/> <%=descriptor.getDescripcion("etiq_plzNat")%>&nbsp;
                             </td>
                             <td class="columnP" style="width: 10%">
                                 <html:radio property="tipoPlazo" styleClass="textoSuelto" value="3" onclick="onClickUnidadesPlazo();"/>
                                 <%=descriptor.getDescripcion("etiq_plzMes")%>&nbsp;
                            </td>
                            <td style="width: 2%"></td>
                            <td id="et1" class="etiquetaDeshabilitada"><%=descriptor.getDescripcion("etiq_Avisoqueda")%></td>
                            <td class="columnP">
                                 <table style="width:100%">
                                     <tr>
                                         <td style="width: 10%">
                                             <html:text styleClass="inputTexto"  property="porcentaje" size="4" maxlength="3" onkeyup="return SoloDigitos(this);"  style="color:#465736"/>
                                         </td>
                                         <TD id="et2" class="etiquetaDeshabilitada">&nbsp;<%=descriptor.getDescripcion("etiq_plazoFinal")%></TD>
                                     </tr>
                                 </table>
                            </td>
                         </tr>
                     </table>
                        </td>
                    </tr>
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_estado")%>:</TD>
                        <TD class="columnP">
                            <table style="width:100%">
                                <tr>
                                    <td style="width: 15%" class="columnP">
                                        <html:radio property="codEstado" styleClass="textoSuelto" value="0" onclick="" /><%=descriptor.getDescripcion("etiq_estProv")%>&nbsp;
                                    </td>
                                    <td style="width: 85%" class="columnP">
                                        <html:radio property="codEstado" styleClass="textoSuelto" value="1" onclick=""/><%=descriptor.getDescripcion("etiq_estDef")%>
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </tr>
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_unidIni")%>:</TD>
                        <TD class="columnP">
                            <table style="width:100%">
                                <tr>
                                    <td style="width: 15%" class="columnP">
                                        <html:radio property="cqUnidadInicio" styleClass="textoSuelto" value="0" onclick="limpiarUnidadesInicio();" /><%=descriptor.getDescripcion("etiq_cqunidIni")%>&nbsp;
                                    </td>
                                    <td style="width: 20%" class="columnP">
                                        <html:radio property="cqUnidadInicio" styleClass="textoSuelto" value="1" onclick=""/><%=descriptor.getDescripcion("etiq_otrasunidIni")%>
                                    </td>
                                    <td class="columnP">
                                        <span class="fa fa-search" aria-hidden="true"  name="botonTipoInicio"  id="botonTipoInicio" onClick="pulsarListaUnidadesInicio();"></span>
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </tr>
                    <tr>
                        <td colspan=2 id="tablaUnidades" style="width: 100%"></td>
                    </tr>
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_tipoInicio")%>:</TD>
                        <TD class="columnP">
                            <table style="width:100%">
                                <tr>
                                    <td style="width: 15%" class="columnP">
                                        <html:radio property="codTipoInicio" styleClass="textoSuelto" value="0" onclick=""/><%=descriptor.getDescripcion("etiq_tInicOfi")%>&nbsp;
                                    </td>
                                    <td style="width: 20%" class="columnP">
                                        <html:radio property="codTipoInicio" styleClass="textoSuelto" value="1" onclick=""/><%=descriptor.getDescripcion("etiq_tInicIns")%>
                                    </td>
                                    <td class="columnP">
                                        <html:radio property="codTipoInicio" styleClass="textoSuelto" value="2" onclick=""/><%=descriptor.getDescripcion("etiq_tInicAmb")%>
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </tr>
                    <tr>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_loc")%>:</TD>
                        <TD class="columnP">
                            <table style="width:100%">
                                <tr>
                                    <td style="width: 15%" class="columnP">
                                        <html:radio property="localizacion" styleClass="textoSuelto" value="0" onclick=""/><%=descriptor.getDescripcion("etiq_si")%>
                                    </td>
                                    <td class="columnP">
                                        <html:radio property="localizacion" styleClass="textoSuelto" value="1" onclick=""/><%=descriptor.getDescripcion("etiq_no")%>
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </tr>

                    <TR>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqRestringido")%>:</TD>
                        <TD class="columnP">
                            <html:checkbox property="restringido" onclick="javascript:validarProcRestringido();"/>
                            <html:checkbox property="biblioteca" style="margin-left: 88px" onclick="javascript:validarProcBiblioteca()" />&nbsp;&nbsp;<%=descriptor.getDescripcion("etiqLibFlujo")%>
                            <c:if test="${sessionScope.DefinicionProcedimientosForm.propNumExpedientesAnoAsientoBuzon eq 'si'}">
                                <html:checkbox property="numeracionExpedientesAnoAsiento" styleId="numeracionExpedientesAnoAsiento" style="margin-left: 88px" onclick="javascript:validarProcExpNumeracion()" />&nbsp;&nbsp;<%=descriptor.getDescripcion("etiqExpAnoAsiento")%>
                            </c:if>
                        </TD>
                    </TR>
                    <tr>
                        <td style="width: 100%" colspan="2">
                            <table style="width:100%" style="margin-top:2px;">
                                <tr>
                                    <TD style="width: 25%" class="columnP"><input type="checkbox" name="disponible" value="si">&nbsp;&nbsp;<%=descriptor.getDescripcion("dispWeb")%>
                                    </TD>
                                    <TD style="width: 35%" class="columnP"><input type="checkbox" name="tramitacionInternet" value="internet">&nbsp;&nbsp;<%=descriptor.getDescripcion("tramInt")%>
                                    </TD>
                                    <TD class="columnP"><input type="checkbox" name="interesadoOblig" value="">&nbsp;&nbsp;<%=descriptor.getDescripcion("intOblig")%>
                                    </TD>
                                    <TD class="columnP"><input type="checkbox" name="soloWS" value="soloWS">&nbsp;&nbsp;<%=descriptor.getDescripcion("soloWS")%>
                                    </TD>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <!-- CAPA 2: DOCUMENTOS DEL PROCEDIMIENTO
            ------------------------------ -->
            <div class="tab-page" id="tabPage2">
                <h2 class="tab"><%=descriptor.getDescripcion("etiqDocs")%></h2>
                <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" border=0 cellspacing="0px" cellpadding="0px">
                    <TR>
                        <TD id="tablaDoc" style="width: 100%; padding-bottom: 2px"></TD>
                    </TR>
                    <TR>
                        <TD style="width: 100%">
                            <input type="text" style="width: 55%" class="inputTextoObligatorio" name="nombreDoc" maxlength="255"  onblur="return xAMayusculas(this);">
                            <input style="width:37%" type="text" class="inputTexto" name="condicion" maxlength="2000"  onblur="return xAMayusculas(this);">
                        </TD>
                    </TR>
                    <TR>
                        <TD style="padding-top:10px;text-align:center">
                            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaDoc" onClick="pulsarAltaDoc();">
                            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificarDoc" onClick="pulsarModificarDoc();">
                            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarDoc" onClick="pulsarEliminarDoc();">
                            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiarDoc" onClick="pulsarLimpiarDoc();">
                            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbFirmas")%>" name="cmdFirmasDoc" onClick="pulsarFirmasDoc();">
                        </TD>
                    </TR>
                </TABLE>
            </div>
            <!-- CAPA 3: PESTAÑA DE ENLACES
            ------------------------------ -->
            <div class="tab-page" id="tabPage3">
                <h2 class="tab"><%=descriptor.getDescripcion("gbEnlaces")%></h2>
                <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" style="padding-bottom: 5px">
                    <tr>
                        <td id="tablaEnlaces"></td>
                    </tr>                                                            
                    <tr>
                        <td>
                        <!-- Descripcion -->
                            <input style="width: 39%;" type="text" class="inputTextoObligatorio" name="txtDescUrl" maxlength="100" style="text-transform:none;">
                        <!-- Url -->
                            <input style="width: 39%;" type="text" class="inputTextoObligatorio" name="txtUrl" maxlength="1000" style="text-transform:none;">
                        <!-- Estado -->
                            <input style="width: 17%" type="text" class="inputTextoObligatorio" name="descEstUrl" id="descEstUrl" readonly="true">
                            <A href="" name="anchorEstUrl" id="anchorEstUrl">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonEstUrl" id="botonEstUrl" style="border: 0" style="cursor:hand;"></span>
                            </A>
                        </td>
                    </tr>
                </table>
                <div style="text-align:center">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaEnlaces" onClick="pulsarAltaEnlaces();">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificarEnlaces" onClick="pulsarModificarEnlaces();">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarEnlaces" onClick="pulsarEliminarEnlaces();">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiarEnlaces" onClick="limpiarInputsEnlaces();">
                </div>
            </div>
            <!-- CAPA 4: PESTAÑA DE CAMPOS
            ------------------------------ -->
            <div class="tab-page" id="tabPage4">
                <h2 class="tab"><%=descriptor.getDescripcion("etiqCampos")%></h2>
                <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
                <table style="width:100%">
                    <tr>
                        <td class="sub3titulo">
                            Definición de campos
                        </td>
                    </tr>
                </table>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" border=0 cellspacing="0px" cellpadding="0px" style="padding-bottom: 5px">
                    <TR style="padding-bottom: 4px">
                        <TD id="tablaCampos"></TD>
                    </TR>
                </TABLE>
                <div style="text-align:center">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaCampo" onClick="pulsarAltaCampo();">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificarCampo" onClick="pulsarModificarCampo();">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarCampo" onClick="pulsarEliminarCampo();">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbOrdenar")%>" name="cmdOrdenarCampo" onClick="pulsarOrdenarCampo();" style="display:none">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("getiq_vista")%>" name="cmdVista" onClick="pulsarVista();">
                </div>
                </br>
                <table style="width:100%">
                    <tr>
                        <td class="sub3titulo">
                            <%=descriptor.getDescripcion("getiq_defagrupacion")%>
                        </td>
                    </tr>
                </table>
                <TABLE id ="tablaDatosAgrupaciones" class="contenidoPestanha" border=0 cellspacing="0px" cellpadding="0px" style="padding-bottom: 5px">
                    <TR style="padding-bottom: 4px">
                        <TD id="tablaAgrupaciones"></TD>
                    </TR>
                </TABLE>
                <div style="padding-left: 300px;float:left">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaAgrupacion" onClick="pulsarAltaAgrupacion();">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificarAgrupacion" onClick="pulsarModificarAgrupacion();">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarAgrupacion" onClick="pulsarEliminarAgrupacion();">
                </div>
            </div>
            <!-- CAPA 5: ROLES
            ----------------------------- -->
            <div class="tab-page" id="tabPage5">
                <h2 class="tab"><%=descriptor.getDescripcion("etiq_roles")%></h2>
                <script type="text/javascript">tp1_p5 = tp1.addTabPage( document.getElementById( "tabPage5" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" style="padding-bottom: 5px">
                    <tr>
                        <td id="tablaRoles"></td>
                    </tr>
                    <tr>
                        <td>
                            <input style="width:17%" type="text" class="inputTextoObligatorio" name="codRol" size="3" maxlength="2" onkeyup="return SoloDigitos(this);">
                            <input style="width: 62%;margin-right:5%" type="text" class="inputTextoObligatorio" name="descRol" size="50" maxlength="1000" onblur="return CaracterValidos(this);">
                            <input style="margin-right:7%" type="checkbox" name="porDefecto" value="si">
                            <input type="checkbox" name="consultaWeb" value="si">
                        </td>
                    </tr>
                </TABLE>
                <div style="padding-left: 250px;float:left">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaRoles" onClick="pulsarAltaRoles();">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificarRoles" onClick="pulsarModificarRoles();">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarRoles" onClick="pulsarEliminarRoles();">
                    <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiarRoles" onClick="limpiarInputsRoles();">
                </div>
            </div>
   <!-- CAPA 6: VISTA DE PENDIENTES
            ----------------------------- -->
            <div class="tab-page" id="tabPage6">
                <h2 class="tab"><%=descriptor.getDescripcion("etiqVistaPendientes")%></h2>
                <script type="text/javascript">tp1_p5 = tp1.addTabPage( document.getElementById( "tabPage6" ) );</script>
                <table style="width:100%">														                                                        														
                <tr>
                        <td colspan="2" id="tablaPendientes"></td>
                <tr>														
                        <td colspan="2" align="right" class="etiqueta">&nbsp;<div id="divPorcentaje" class="etiqueta"></div></td>
                </tr>
                <tr>
                        <td colspan="2" style="padding-left:300px">
                                <input type="button" name="btnAlta" onClick="javascript:pulsarAltaCampoVistaPendientes();" value="<%=descriptor.getDescripcion("gbAlta")%>" class="botonGeneral"/>																
                                <input type="button" name="btnEliminar" onClick="javascript:pulsarEliminarCampoVistaPendientes();" value="<%=descriptor.getDescripcion("gbEliminar")%>" class="botonGeneral"/>
                                <input type="button" name="btnOrdenar" onClick="javascript:pulsarOrdenarCamposVistaPendientes();" value="<%=descriptor.getDescripcion("gbOrdenar")%>" class="botonGeneral"/>																
                        </td>
                </tr>
                <tr>
                        <td colspan="2">&nbsp;</td>
                </tr>
                </table>																											                                                       
        </div>


           <!-- CAPA 7: ANULACIÓN DE EXPEDIENTE ----------------------------- -->
            <div class="tab-page" id="tabPage99">
                <h2 class="tab"><%=descriptor.getDescripcion("etiqAnulacionExpediente")%></h2>
                <script type="text/javascript">tp1_p9 = tp1.addTabPage( document.getElementById( "tabPage99" ) );</script>
                <table style="width:100%">														                                                        														
                <tr>
                            <td colspan="2" align="center">
                                    <table style="width:100%">
                                    <tr>
                                        <td class="etiqueta" style="width:28%;">                                                                                    
                                            <%=descriptor.getDescripcion("etiqVerifAnularExp")%>
                                        </td>
                                        <!---------->                                                                                
                                         <TD class="columnP">
                                            <html:text styleId="inputTexto" styleClass="inputTexto"  property="codServicioFinalizacion" 
                                                onkeyup="return SoloDigitos(this);" style="width:10%;visible:none;"/>

                                            <html:text styleId="inputTexto" styleClass="inputTexto" property="descServicioFinalizacion" style="width:80%" readonly="true"/>
                                            <A href="" id="anchorServicioFinalizacion" name="anchorServicioFinalizacion">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonArea" name="botonArea" style="border: 0px"></span>
                                            </A>
                                        </TD>

                                        <!----------->
                                    </tr>
                                    </table>
                            </td>
                    <tr>														
                            <td colspan="2" align="right" class="etiqueta">&nbsp;<div id="divPorcentaje" class="etiqueta"></div></td>
                    </tr>
                    <tr>
                            <td colspan="2" align="center">
                                    &nbsp;
                            </td>
                    </tr>
                    <tr>
                            <td colspan="2">&nbsp;</td>
                    </tr>
                    </table>																											                                                       
            </div>
            <!-- CAPA 7: TRÁMITES ----------------------------- -->
            <div class="tab-page" id="tabPage7">
                <h2 class="tab"><%=descriptor.getDescripcion("res_pestana2")%></h2>
                <script type="text/javascript">tp1_p6 = tp1.addTabPage( document.getElementById( "tabPage7" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" style="padding-bottom: 5px">
                    <TR>
                        <TD style="width: 100%">
                            <div class="webfx-tree-div" style="min-height:200px;width:100%">
                                <script type="text/javascript">
                                    tramitesTree = new WebFXTree('CLASIFICACIÓN DE TRÁMITES');
                                    tramitesTree.icon=icoClassTram;
                                    tramitesTree.openIcon=icoClassTram;
                                    tramitesTree.estilo='webfx-tree-item-tramite-II';
                                    document.write(tramitesTree);
                                </script>
                            </div>
                        </TD>
                    </TR>
                    <TR>
                        <TD align="center">
                            <input type="button" class="botonGeneral" accesskey="N" value="<%=descriptor.getDescripcion("bNuevoTram")%>" name="cmdNuevoTram" onClick="pulsarNuevoTramite();">
                        </td>
                    </TR>
                </TABLE>
            </div>
            <%if ("SI".equals(CargaCapaHistorico)){%>
            <!-- CAPA 8: COMPROBACION DE RECUPERACION DE HISTORICO ----------------------------- -->
            <div class="tab-page" id="tabPage8">
                <h2 class="tab"><%=descriptor.getDescripcion("msjPestHist")%></h2>
                <script type="text/javascript">tp1_p8 = tp1.addTabPage( document.getElementById( "tabPage8" ) );</script>
                <table style="width:100%">														                                                        														
                    <tr>
                        <td class="etiqueta" style="width:8%">                                                                                    
                            <%=descriptor.getDescripcion("msjBuzHist")%> 
                        </td>                                                                                                                                               
                        <TD class="columnP">                                                                                                                                                
                            <html:text styleId="inputTexto" styleClass="inputTexto"  property="codClaseHist" style="width:10%"/>
                            <html:text styleId="inputTexto" styleClass="inputTexto"  property="descClaseHist" style="width:85%"/>
                            <A href="" id="anchorClaseHist" name="anchorClaseHist">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonClaseHist" name="botonClaseHist"></span>
                            </A>
                        </td>                                                            
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                </table>																											                                                       
            </div>      
            <%}%>                               
           <!-- PESTAÑAS CORRESPONDIENTES A MÓDULOS DE INTEGRACIÓN CON PANTALLAS DEFINIDAS A NIVEL DE DEFINICIÓN DE PROCEDIMIENTO -->
            <%
            DefinicionProcedimientosForm DefinicionProcedimientosForm = (DefinicionProcedimientosForm)session.getAttribute("DefinicionProcedimientosForm");
            ArrayList<ModuloIntegracionExterno> modulos = DefinicionProcedimientosForm.getListaModulosPantallasDefinicionProcedimientos();


            for(int i=0;modulos!=null && i<modulos.size();i++){
                ModuloIntegracionExterno modulo = modulos.get(i);
                String nombreModulo = modulo.getNombreModulo();
                ArrayList<DatosPantallaModuloVO> pantallas = modulo.getListaPantallasDefinicionProcedimiento();                                                     
               for(int j=0;pantallas!=null && j<pantallas.size();j++){                                                        
                 String url = pantallas.get(j).getUrl();                                                      
                 String codProcedimiento = pantallas.get(j).getCodProcedimiento();                                                      
                 String operacion = pantallas.get(j).getOperacionProceso();
            %>
                 <jsp:include page="<%=url%>" flush="false">

                    <jsp:param name="nombreModulo" value="<%=nombreModulo%>"/>
                    <jsp:param name="codOrganizacion" value="<%=municipio%>"/>
                    <jsp:param name="operacionProceso" value="<%=operacion%>"/>
                    <jsp:param name="idioma" value="<%=idioma%>"/>
                    <jsp:param name="tipo" value="2"/>
                    <jsp:param name="codProcedimiento" value="<%=codProcedimiento%>"/>

                </jsp:include>

            <%
                  }// for pantallas
            }// for modulos
            %>
     </div>

<TABLE style="width: 98%;margin-left:15px;" id="tabla1">
    <!-- ----------------------- RELACIÓN DE ENTRADAS ---------------------------- -->
    <TR>
        <TD>
            <DIV id="capaRadioButtons" name="capaRadioButtons" STYLE="position:absolute; width:100%; height:0px; visibility:hidden;">
                <TABLE style="width:95%">
                    <TR>
                        <TD valing="top">
                            <TABLE style="width:225px" class="subsubtitulo">
                                <TR>
                                    <TD style="width: 75%" class="textoSuelto">
                                        <input type="radio" name="tipoConsulta" class="textoSuelto" value="listado" CHECKED> <%=descriptor.getDescripcion("gEtiqListPro")%></input>
                                        <BR>
                                        <input type="radio" name="tipoConsulta" class="textoSuelto" value="registro" > <%=descriptor.getDescripcion("gEtiqProAPro")%></input>
                                    </TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </TABLE>
            </DIV>
        </TD>
    </TR>
    <!-- ----------------------- NAVEGACIÓN ---------------------------- -->
    <TR>
        <TD>
            <DIV id="capaNavegacionConsulta" name="capaNavegacionConsulta" class="dataTables_wrapper" style="height:30px;"></DIV>
        </TD>
    </TR>
</TABLE>
                                    
   </div>
    <div class="capaFooter">

    <DIV id="capaBotones1" name="capaBotones1" STYLE="display:none" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" onClick="pulsarAlta();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbConsultar")%>" name="cmdConsultar" onClick="pulsarConsultar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbImportar")%>" name="cmdImportar" onClick="pulsarImportar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onClick="pulsarSalir();">
    </DIV>
    <DIV id="capaBotones2" name="capaBotones2" STYLE="display:none" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnBiblioteca")%>" name="cmdBiblioteca" onClick="pulsarBiblioteca();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbImprimir")%>" name="cmdImprimir" onClick="pulsarImprimir();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbDuplicar")%>" name="cmdDuplicar" onClick="pulsarDuplicar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificar" onClick="pulsarModificar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" onClick="pulsarEliminar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarBuscar" onClick="pulsarCancelarBuscar();">
        <DIV id="capaBotonesNuevaConsulta" name="capaBotonesNuevaConsulta" STYLE="float:left;margin-right: 5px;display:none">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbListado")%>' name="cmdListado" onClick="pulsarListado();return false;">
        </DIV>
    </DIV>
    <DIV id="capaBotones3" name="capaBotones3" STYLE="display:none" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdGrabarModificar" onClick="pulsarGrabarModificar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarModificar" onClick="pulsarCancelarModificar();">
    </DIV>
    <DIV id="capaBotones4" name="capaBotones4" STYLE="display:none" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdGrabarAlta" onClick="pulsarGrabarAlta();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarAlta" onClick="pulsarCancelarAlta();">
    </DIV>
    <DIV id="capaBotones5" name="capaBotones5" STYLE="display:none" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbDescargar")%>" name="cmdDescargar" onClick="pulsarDescargar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarImportar" onClick="pulsarCancelarBuscar();">
    </DIV>
    <DIV id="capaBotones6" name="capaBotones6" STYLE="display:none" class="botoneraPrincipal">
        <input type="button" style="color: #ffffff" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarCatalogo" id="cmdCancelarCatalogo" onClick="pulsarCancelarCatalogo();">
    </DIV>
</div>
</html:form>
<script type="text/javascript">
            function mostrarCapasBotones(nombreCapa) {
                document.getElementById('capaRadioButtons').style.display='none';
                document.getElementById('capaBotones1').style.display='none';
                document.getElementById('capaBotones2').style.display='none';
                document.getElementById('capaBotonesNuevaConsulta').style.display='none';
                document.getElementById('capaBotones3').style.display='none';
                document.getElementById('capaBotones4').style.display='none';
                document.getElementById('capaBotones5').style.display='none';
                document.getElementById('capaBotones6').style.display='none';
                document.getElementById(nombreCapa).style.display='';
                //En el antiguo diseño los radio buttons estaban en la misma capa que los botones de consulta, pero
                //con el nuevo diseño hay que separarlos; por este motivo, mostramos la capa de los radio buttons siempre
                //que se muestra la capa de botones de consulta.
                if (nombreCapa == 'capaBotones1') document.getElementById('capaRadioButtons').style.visibility='visible';
                if (nombreCapa == 'capaBotones2') document.getElementById('capaBotonesNuevaConsulta').style.display='';
            }

            //Cargamos el combo de plugin historico en caso de ser necesario            
            cargarDatosPluginHistorico();
            // JAVASCRIPT DE LAS UNIDADES DE INICIO
            var tabUnidades = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUnidades'));

            tabUnidades.addColumna('120','center','<%=descriptor.getDescripcion("gEtiq_codigo")%>');
            tabUnidades.addColumna('783','center','<%=descriptor.getDescripcion("gEtiq_desc")%>');
                    tabUnidades.displayCabecera=false;
                    tabUnidades.displayTabla();
                    function refrescaUnidades() {
                        tabUnidades.displayTabla();
                    }
                    // FIN JAVASCRIPT DE LAS UNIDADES DE INICIO

                    // JAVASCRIPT DE LA PESTAÑA DOCUMENTOS
                    tabDoc = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDoc'));
                    tabDoc.addColumna('500','left','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
                        tabDoc.addColumna('350','left','<%= descriptor.getDescripcion("gEtiq_cond")%>');
                        tabDoc.addColumna('60','center','<%= descriptor.getDescripcion("gEtiq_firmas")%>');
                            tabDoc.displayCabecera=true;
                            tabDoc.displayTabla();
                            function refrescaDoc() {
                                tabDoc.displayTabla();
                            }
                            tabDoc.displayDatos = pintaDatosDoc;
                            function pintaDatosDoc(datos){
                                if(document.forms[0].cmdAltaDoc.disabled == false) {
                                    borrarDatosDoc();
                                    document.forms[0].nombreDoc.value = datos[0];
                                    document.forms[0].condicion.value = datos[1];
                                    if(tabDoc.selectedIndex != -1) {
                                        var j = tabDoc.selectedIndex;
                                    }
                                }
                            }

// FIN DE LOS JAVASCRIPT'S PERTENECIENTES A LA PESTAÑA DOCUMENTOS
// JAVASCRIPT DE LA PESTAÑA CAMPOS
tabCampos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaCampos'));
tabCampos.addColumna('200','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tabCampos.addColumna('600','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tabCampos.addColumna('120','center','Activo');
tabCampos.displayCabecera=true;
tabCampos.displayTabla();

function refrescaCampos() {
    tabCampos.displayTabla();
}

tabAgrupaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAgrupaciones'));
tabAgrupaciones.addColumna('200','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tabAgrupaciones.addColumna('480','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tabAgrupaciones.addColumna('120','center','<%= descriptor.getDescripcion("getiq_orden")%>');
tabAgrupaciones.addColumna('120','center','<%= descriptor.getDescripcion("getiq_activo")%>');
tabAgrupaciones.displayCabecera=true;
tabAgrupaciones.displayTabla();

function refrescaAgrupaciones(){
    tabAgrupaciones.displayTabla();
}

// FIN DE LOS JAVASCRIPT'S PERTENECIENTES A LA PESTAÑA CAMPOS
// JAVASCRIPT DE LA PESTAÑA ENLACES
tabEnlaces = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaEnlaces'));
tabEnlaces.addColumna('360','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tabEnlaces.addColumna('360','left','URL');
tabEnlaces.addColumna('200','left','<%= descriptor.getDescripcion("etiq_estado")%>');
tabEnlaces.displayCabecera=true;
tabEnlaces.displayTabla();


// TABLA CON LOS CAMPOS PARA LA VISTA DE PENDIENTES
var tabPendientes = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaPendientes'));
	
tabPendientes.addColumna('0','left','<%=descriptor.getDescripcion("etiqColumna")%>');	
tabPendientes.addColumna('473','left','<%=descriptor.getDescripcion("etiqColumna")%>');
tabPendientes.addColumna('433','left','<%=descriptor.getDescripcion("etiqAnchura")%>');
tabPendientes.displayCabecera=true;
tabPendientes.displayTabla();

    function refrescaEnlaces() {
        tabEnlaces.displayTabla();
    }
    tabEnlaces.displayDatos = pintaDatosEnlaces;
    function pintaDatosEnlaces(datos){
        if(document.forms[0].cmdAltaEnlaces.disabled == false) {
            borrarDatosEnlaces();
            document.forms[0].txtDescUrl.value = datos[0];
            document.forms[0].txtUrl.value = datos[1];
            if(datos[2] == "S") {
                document.forms[0].descEstUrl.value = '<%=descriptor.getDescripcion("etiq_si")%>';
            } else if(datos[2] == "N") {
            document.forms[0].descEstUrl.value = '<%=descriptor.getDescripcion("etiq_no")%>';
        }
    }
}
// FIN DE LOS JAVASCRIPT'S PERTENECIENTES A LA PESTAÑA ENLACES

// JAVASCRIPT DE LA PESTAÑA ROLES
tabRoles = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaRoles'));
tabRoles.addColumna('150','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tabRoles.addColumna('590','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tabRoles.addColumna('90','center','<%= descriptor.getDescripcion("etiq_porDef")%>');
tabRoles.addColumna('90','center','<%= descriptor.getDescripcion("gEtiq_cw")%>');
tabRoles.displayCabecera=true;
tabRoles.displayTabla();

function refrescaRoles() {
  tabRoles.displayTabla();
}
tabRoles.displayDatos = pintaDatosRoles;
function pintaDatosRoles(datos){
  if(document.forms[0].cmdAltaRoles.disabled == false) {
    borrarDatosRoles();
    document.forms[0].codRol.value = datos[0];
    document.forms[0].descRol.value = datos[1];
	if(datos[2] == "X") {
	  document.forms[0].porDefecto.checked = true;
	}
	if(datos[3] == "X") {
	  document.forms[0].consultaWeb.checked = true;
	}

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
// FIN DE LOS JAVASCRIPT'S PERTENECIENTES A LA PESTAÑA ROLES
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


                                                        if('Alt+C'==tecla) {
                                                            if(document.getElementById('capaBotones2').style.display=='')
                                                                pulsarCancelarBuscar();
                                                            if(document.getElementById('capaBotones3').style.display=='')
                                                                pulsarCancelarModificar();
                                                            if(document.getElementById('capaBotones4').style.display=='')
                                                                pulsarCancelarAlta();
                                                            if(document.getElementById('capaBotones1').style.display=='')
                                                                pulsarConsultar();
                                                            if(document.getElementById('capaBotones5').style.display=='')
                                                                pulsarCancelarBuscar();
                                                            if(document.getElementById('capaBotones6').style.display=='')
                                                                pulsarCancelarCatalogo();
                                                        } else if('Alt+A'==tecla) {
                                                        if(document.getElementById('capaBotones1').style.display=='')
                                                            pulsarAlta();
                                                        if(tp1.getSelectedIndex() == 1)
                                                            pulsarAltaDoc();
                                                        if(tp1.getSelectedIndex() == 2)
                                                            pulsarAltaEnlaces();
                                                        if(tp1.getSelectedIndex() == 3)
                                                            pulsarAltaCampo();
                                                        if(tp1.getSelectedIndex() == 4)
                                                            pulsarAltaRoles();
                                                    } else if('Alt+M'==tecla) {
                                                    if(document.getElementById('capaBotones2').style.display=='')
                                                        pulsarModificar();
                                                    if(tp1.getSelectedIndex() == 1)
                                                        pulsarModificarDoc();
                                                    if(tp1.getSelectedIndex() == 2)
                                                        pulsarModificarEnlaces();
                                                    if(tp1.getSelectedIndex() == 3)
                                                        pulsarModificarCampo();
                                                    if(tp1.getSelectedIndex() == 4)
                                                        pulsarModificarRoles();
                                                } else if('Alt+E'==tecla) {
                                                if(document.getElementById('capaBotones2').style.display=='')
                                                    pulsarEliminar();
                                                if(tp1.getSelectedIndex() == 1)
                                                    pulsarEliminarDoc();
                                                if(tp1.getSelectedIndex() == 2)
                                                    pulsarEliminarEnlaces();
                                                if(tp1.getSelectedIndex() == 3)
                                                    pulsarEliminarCampo();
                                                if(tp1.getSelectedIndex() == 4)
                                                    pulsarEliminarRoles();
                                            } else if('Alt+S'==tecla) {
                                            if(document.getElementById('capaBotones1').style.display=='')
                                                pulsarSalir();
                                        } else if('Alt+G'==tecla) {
                                        if(document.getElementById('capaBotones3').style.display=='')
                                            pulsarGrabarModificar();
                                        if(document.getElementById('capaBotones4').style.display=='')
                                            pulsarGrabarAlta();
                                    } else if('Alt+I'==tecla) {
                                    if(document.getElementById('capaBotones1').style.display=='')
                                        pulsarImportar();
                                    if(document.getElementById('capaBotones2').style.display=='')
                                        pulsarImprimir();
                                } else if('Alt+L'==tecla) {
                                if(document.getElementById('capaBotonesNuevaConsulta').style.display=='')
                                    pulsarListado();
                                if(tp1.getSelectedIndex() == 1)
                                    pulsarLimpiarDoc();
                                if(tp1.getSelectedIndex() == 2)
                                    limpiarInputsEnlaces();
                                if(tp1.getSelectedIndex() == 4)
                                    limpiarInputsRoles();
                            } else if('Alt+D'==tecla) {
                            if(document.getElementById('capaBotones5').style.display=='')
                                pulsarDescargar();
                            if(document.getElementById('capaBotones2').style.display=='')
                                pulsarDuplicar();
                        } else if('Alt+O'==tecla) {
                        if(tp1.getSelectedIndex() == 3)
                            pulsarOrdenarCampo();
                    }

                    if (tecla == 1){
                       if (comboArea.base.style.visibility == "visible" && isClickOutCombo(comboArea,coordx,coordy)) setTimeout('comboArea.ocultar()',20);
                       if (comboTipoProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboTipoProcedimiento,coordx,coordy)) setTimeout('comboTipoProcedimiento.ocultar()',20);
                       if (comboEstadoEnlace.base.style.visibility == "visible" && isClickOutCombo(comboEstadoEnlace,coordx,coordy)) setTimeout('comboEstadoEnlace.ocultar()',20);
                       if(IsCalendarVisible) replegarCalendario(coordx,coordy);
                    }
                    if (tecla == 9){
                      comboArea.ocultar();
                      comboTipoProcedimiento.ocultar();
                      comboEstadoEnlace.ocultar();
                      if(IsCalendarVisible) hideCalendar();
                    }

                    keyDel(aux);
                }

                var comboArea = new Combo("Area");
                var comboTipoProcedimiento = new Combo("TipoProcedimiento");
                var comboEstadoEnlace = new Combo("EstUrl");
                
                
                /** prueba **/
                var comboFinalizacionExpediente = new Combo("ServicioFinalizacion");
                
                
                var contServicios = 0;                                
                <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="serviciosVerificacionFinExpediente">
                    listaCodigosServiciosFinalizacion[contServicios] = ['<bean:write name="elemento" property="nombre" ignore="true"/>'];
                    listaDescripcionesServiciosFinalizacion[contServicios] = ['<bean:write name="elemento" property="descripcion" ignore="true"/>'];
                    listaImplClassServiciosFinalizacion[contServicios] = ['<bean:write name="elemento" property="nombre" ignore="true"/>','<bean:write name="elemento" property="descripcion" ignore="true"/>','<bean:write name="elemento" property="implClass" ignore="true"/>'];
                    contServicios++;
                
                </logic:iterate>                


                comboFinalizacionExpediente.addItems(listaCodigosServiciosFinalizacion,listaDescripcionesServiciosFinalizacion);
                comboFinalizacionExpediente.change = cambioFinalizacionExpediente;
                
                function cambioFinalizacionExpediente(){                   
                    if(comboFinalizacionExpediente!=null && comboFinalizacionExpediente.selectedIndex!=-1 && comboFinalizacionExpediente.selectedIndex!=0){
                        var index = comboFinalizacionExpediente.selectedIndex;                   
                        document.forms[0].implClassServicioFinalizacion.value = listaImplClassServiciosFinalizacion[index-1][2];
                    }
                }
        </script>
        
    </BODY>
</html:html>
