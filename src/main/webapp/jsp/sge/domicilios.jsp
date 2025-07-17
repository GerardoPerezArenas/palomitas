<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject" %>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.StringBuffer"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.util.StringTokenizer"%>
<HTML>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES - Domicilios :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<style>


    .contenidoPantalla {
	width: 100%;
	padding-top: 3px;
	text-align: center;
	background-color: #fffaff;
}



</style>
<%
UsuarioValueObject usuarioVO = new UsuarioValueObject();
ParametrosTerceroValueObject ptVO = null;
int idioma=1;
int apl=1;
String existeTramero = "";
String codMun = "";
if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
    existeTramero = usuarioVO.getExisteTramero();
 }
if (session.getAttribute("parametrosTercero") != null){
    ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    codMun = ptVO.getMunicipio();
}
Config m_Conf = ConfigServiceHelper.getConfig("common");
boolean bAltaViaDirecta = false;
String JSP_altaViaDirecta = m_Conf.getString("JSP.altaViaDirecta");
String JSP_entidadColectiva = m_Conf.getString("JSP.EntidadColectiva");
String JSP_entidadSingular = m_Conf.getString("JSP.EntidadSingular");
String CodigoSinTipoVia = m_Conf.getString("T_TVI.CodigoSinTipoVia");
String ecv = "visible";
String esv = "visible";
if("no".equals(JSP_entidadColectiva)) ecv = "hidden";
if("no".equals(JSP_entidadSingular)) esv = "hidden";
StringTokenizer st = new StringTokenizer(JSP_altaViaDirecta, ",");
while (st.hasMoreTokens()){
    if(st.nextToken().trim().equals(String.valueOf(usuarioVO.getOrgCod()))){ bAltaViaDirecta = true;}
}

String hayBuscada="";
String consulta ="";
if (session.getAttribute("modoInicio") != null) {
    if ("consultaLocalizacion".equals(session.getAttribute("modoInicio"))){
	  consulta="si";
    } else if ("recargarDomicilio".equals(session.getAttribute("modoInicio"))){
      hayBuscada="si";
    } else if("recargarDomicilioInicio".equals(session.getAttribute("modoInicio"))){
      hayBuscada = "siInicio";
    }
    session.removeValue("modoInicio");
}
BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
Vector listaCodPostales = bForm.getListaCodPostales();
int lengthCodPostales = listaCodPostales.size();
String codigos ="";
if (lengthCodPostales > 0 ) {
      for(int j=0;j<lengthCodPostales-1;j++){
        codigos += "\""+  ((GeneralValueObject)listaCodPostales.get(j)).getAtributo("codPostal")+ "\",";
      }
      codigos += "\""+  ((GeneralValueObject)listaCodPostales.get(lengthCodPostales-1)).getAtributo("codPostal")+ "\"";
}
Vector listaECOs = bForm.getListaECOs();
Vector listaESIs = bForm.getListaESIs();
//Vector listaVias = bForm.getListaVias();
int lengthECOs = listaECOs.size();
int lengthESIs = listaESIs.size();
StringBuffer codECOs =new StringBuffer("");
StringBuffer descECOs =new StringBuffer("");
StringBuffer codESIs =new StringBuffer("");
StringBuffer descESIs =new StringBuffer("");
StringBuffer listaECOESIs =new StringBuffer("");
if (lengthECOs > 0) {
    GeneralValueObject ecos;
    for(int j=0;j<lengthECOs-1;j++){
            ecos = (GeneralValueObject)listaECOs.get(j);
            codECOs.append("\"").append((String) ecos.getAtributo("codECO")).append("\",");
            descECOs.append("\"").append((String) ecos.getAtributo("descECO")).append("\",");
    }
    ecos = (GeneralValueObject)listaECOs.get(lengthECOs-1);
    codECOs.append("\"").append((String) ecos.getAtributo("codECO")).append("\"");
    descECOs.append("\"").append((String) ecos.getAtributo("descECO")).append("\"");
}
if (lengthESIs > 0) {
    GeneralValueObject esiss;
    for(int j=0;j<lengthESIs-1;j++) {
        esiss = (GeneralValueObject)listaESIs.get(j);
        codESIs.append("\"").append((String) esiss.getAtributo("codEntidadSingular")).append("\",");
        descESIs.append("\"").append((String) esiss.getAtributo("nombreOficial")).append("\",");
        listaECOESIs.append("[\"").append((String) esiss.getAtributo("codEntidadColectiva")).append("\",\"").append((String) esiss.getAtributo("descEntidadColectiva")).append("\"],");
    }
    esiss = (GeneralValueObject)listaESIs.get(lengthESIs-1);
    codESIs.append("\"").append((String) esiss.getAtributo("codEntidadSingular")).append("\"");
    descESIs.append("\"").append((String) esiss.getAtributo("nombreOficial")).append("\"");
    listaECOESIs.append("[\"").append((String) esiss.getAtributo("codEntidadColectiva")).append("\",\"").append((String) esiss.getAtributo("descEntidadColectiva")).append("\"]");
}

    // Creamos las estructuras para almacenar los combos de provincias y municipios.
    StringBuffer codigosProvincias = new StringBuffer("");
    StringBuffer descsProvincias = new StringBuffer("");
    Vector listaProvincias = bForm.getListaProvincias();
    int longitudListaProvs = listaProvincias.size();
    if (longitudListaProvs > 0) {
        for (int j = 0; j < longitudListaProvs; j++) {
            GeneralValueObject provincia = (GeneralValueObject)listaProvincias.get(j);
            if (j != 0) {
                codigosProvincias.append(",");
                descsProvincias.append(",");
            }
            codigosProvincias.append("\"").append(provincia.getAtributo("codigo")).append("\"");
            descsProvincias.append("\"").append(provincia.getAtributo("descripcion")).append("\"");
        }
    }

    StringBuffer codigosMunicipios = new StringBuffer("");
    StringBuffer descsMunicipios = new StringBuffer("");
    Vector listaMunicipios = bForm.getListaMunicipios();
    int longitudListaMuns = listaMunicipios.size();
    if (longitudListaMuns > 0) {
        for (int j = 0; j < longitudListaMuns; j++) {
            GeneralValueObject municipio = (GeneralValueObject)listaMunicipios.get(j);
            if (j != 0) {
                codigosMunicipios.append(",");
                descsMunicipios.append(",");
            }
            codigosMunicipios.append("\"").append(municipio.getAtributo("codMunicipio")).append("\"");
            descsMunicipios.append("\"").append(municipio.getAtributo("nombreOficial")).append("\"");
        }
    }

Vector relacionDomicilios = (Vector) session.getAttribute("RelacionDomicilios");
TramitacionExpedientesForm tEF = (TramitacionExpedientesForm)  session.getAttribute("TramitacionExpedientesForm");
TramitacionExpedientesValueObject tEVO = tEF.getTramitacionExpedientes();
int idDomicilio = -1;
if(relacionDomicilios != null) {
 idDomicilio = Integer.parseInt(tEVO.getIdDomicilio());
}
String codLocalizacion = "";
String codPostal = "";
String idVia ="";
String codVia = "";
String descVia = "";
String domicilio = "";
String refCatastral = "";
String numDesde = "";
String letraDesde = "";
String numHasta = "";
String letraHasta = "";
String bloque = "";
String portal = "";
String escal = "";
String planta = "";
String puerta = "";
String codECO = "";
String descECO = "";
String codESI = "";
String descESI = "";
String codTVia = "";
String descTVia = "";
if ( relacionDomicilios != null && idDomicilio !=-1) {
    GeneralValueObject gVO = (GeneralValueObject) relacionDomicilios.get(idDomicilio);
    codLocalizacion = (String)gVO.getAtributo("idDomicilio");
    codPostal = (String)gVO.getAtributo("codPostal");
    codVia = (String)gVO.getAtributo("codVia");
    descVia = (String)gVO.getAtributo("descVia");
	idVia = (String)gVO.getAtributo("idVia");
    numDesde = (String)gVO.getAtributo("numDesde");
    letraDesde = (String)gVO.getAtributo("letraDesde");
    numHasta = (String)gVO.getAtributo("numHasta");
    letraHasta = (String)gVO.getAtributo("letraHasta");
    bloque = (String)gVO.getAtributo("bloque");
    portal = (String)gVO.getAtributo("portal");
    escal = (String)gVO.getAtributo("escalera");
    planta = (String)gVO.getAtributo("planta");
    puerta = (String)gVO.getAtributo("puerta");
    codECO = (String)gVO.getAtributo("codECO");
    codTVia = (String)gVO.getAtributo("codTipoVia");
    descTVia = (String)gVO.getAtributo("descTipoVia");
    if (codECO == null) {
		codECO ="";
		descECO ="";
	} else descECO = (String)gVO.getAtributo("descECO");
	codESI = (String)gVO.getAtributo("codESI");
	if (codESI == null) {
		codESI ="";
		descESI ="";
	} else	descESI = (String)gVO.getAtributo("descESI");

	if (codVia == null) codVia="";
	domicilio = (String)gVO.getAtributo("domicilio");
    refCatastral = (String)gVO.getAtributo("refCatastral");
}
    String statusBar = m_Conf.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request"
  class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaBusquedaTerceros.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<SCRIPT type="text/javascript">
var modificarExpedientesRelacionados = false;
var expedientesSeleccionados = new Array();
var vCamposTexto;
var vPrismatVia;
// Listas de valores.
var codPostales = new Array();
codPostales = [<%= codigos%>];
/* Anadir ECO/ESI */
var codECOs = new Array();
var descECOs = new Array();
var codESIs = new Array();
var descESIs = new Array();
var listaECOESIs = new Array();
var codESIsOld = new Array();
var descESIsOld = new Array();
var listaECOESIsOld = new Array();
codECOs= [<%= codECOs %>];
descECOs= [<%= descECOs %>];
codESIsOld = [<%= codESIs %>];
descESIsOld = [<%= descESIs %>];
listaECOESIsOld = [<%= listaECOESIs %>];
codESIs= [<%= codESIs %>];
descESIs= [<%= descESIs %>];
listaECOESIs = [<%= listaECOESIs %>];

        var codProvincias = new Array();
        codProvincias = [<%=codigosProvincias%>];
        var descProvincias = new Array();
        descProvincias = [<%=descsProvincias%>];
        var codMunicipios = new Array();
        codMunicipios = [<%=codigosMunicipios%>];
        var descMunicipios = new Array();
        descMunicipios = [<%=descsMunicipios%>];
        var codMunicipiosDefecto = new Array();
        codMunicipiosDefecto = [<%=codigosMunicipios%>];
        var descMunicipiosDefecto = new Array();
        descMunicipiosDefecto = [<%=descsMunicipios%>];


function cargarListas(){
    document.forms[0].opcion.value="cargarListas";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/BusquedaTerceros.do'/>";
    document.forms[0].ventana.value="true";
    document.forms[0].submit();
}

function cargarListaViasBuscadas(total, idVia, codVia, descVia, descTipoVia, codECO, codESI, codTipoVia){
    if (total==0){
        document.forms[0].txtCodVia.value = "";
        document.forms[0].codVia.value = "";
        document.forms[0].descVia.value = "";
        document.forms[0].txtDomicilio.value = "";
        document.forms[0].codTVia.value = "";
        document.forms[0].descTVia.value = "";
        if(<%=bAltaViaDirecta%>){
            if ( jsp_alerta("C",'<%=descriptor.getDescripcion("msjNoDatosPregNueva")%>') == 1 ){
                iniciarAltaViaDirecta();
            }
        }else{
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');
        }

    } else 	if (total==1){
        document.forms[0].codECO.value = codECO;
        if (codESI != '') comboESI.buscaCodigo(codESI);
        document.forms[0].txtCodViaOculto.value = idVia;
        document.forms[0].codTVia.value = codTipoVia;
        document.forms[0].descTVia.value = descTipoVia;
        document.forms[0].txtCodVia.value = codVia;
        document.forms[0].codVia.value = codVia;
        document.forms[0].descVia.value = descVia;
        document.forms[0].txtDomicilio.value = "";
        document.forms[0].txtNombreVia.value = descVia;
    } else {
        var source = "<c:url value='/jsp/terceros/listaViasBuscadas.jsp?opcion=null'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'',
	'width=720,height=400,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined && datosConsulta[0] == "altaViaDirecta"){
                            iniciarAltaViaDirecta();
                        } else if(datosConsulta!=undefined){
                            if (datosConsulta[4]=='null') datosConsulta[4] = '';
                            document.forms[0].codECO.value = datosConsulta[4];
                            if ( datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);
                            document.forms[0].txtCodViaOculto.value = datosConsulta[0];
                            document.forms[0].codTVia.value = datosConsulta[6];
                            document.forms[0].descTVia.value = datosConsulta[3];
                            document.forms[0].txtCodVia.value = datosConsulta[1];
                            document.forms[0].codVia.value = datosConsulta[1];
                            document.forms[0].descVia.value = datosConsulta[2];
                            document.forms[0].txtDomicilio.value = "";
                            document.forms[0].txtNombreVia.value = datosConsulta[2];
                        }
                });
    }
}
function iniciarAltaViaDirecta(){
        pleaseWait1("on",top.mainFrame);
        document.forms[0].opcion.value="iniciarAltaDirecta";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
        document.forms[0].submit();
}
function altaViaDirectaIniciada(codTipoVia,descTipoVia) {
    var argumentos = new Array();
    argumentos = [document.forms[0].codProvincia.value,document.forms[0].codMunicipio.value,
                  codTipoVia, descTipoVia];
    var source = "<%=request.getContextPath()%>/jsp/terceros/altaViaDirecta.jsp?opcion=altaViaDirecta";
    pleaseWait1("off",top.mainFrame); // Viene del buscar via
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
	'width=500,height=310,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                                document.forms[0].codECO.value = datosConsulta[4];
                                if ( datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);
                                document.forms[0].txtCodVia.value = datosConsulta[1];
                                document.forms[0].codVia.value = datosConsulta[1];
                                document.forms[0].descVia.value = datosConsulta[2];
                                document.forms[0].txtDomicilio.value = "";
                                document.forms[0].txtCodViaOculto.value = datosConsulta[0];
                                document.forms[0].codTVia.value = datosConsulta[6];
                                document.forms[0].descTVia.value = datosConsulta[3];
                        }
                });
}
function pulsarBuscarVia(opcion) {
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
                    jsp_alerta("A", '<%=descriptor.getDescripcion("msjIntroMun")%>');
                    return;
                }
            }

            document.forms[0].opcion.value=opcion;
            document.forms[0].target="oculto";
         <% if (existeTramero.equals("si")) { %>
            document.forms[0].action="<html:rewrite page='/terceros/mantenimiento/Viales.do'/>";
         <% } else { %>
            document.forms[0].action="<html:rewrite page='/terceros/mantenimiento/Viales.do?txtNumDesde=&txtNumHasta='/>";
         <% } %>
            document.forms[0].submit();
        }
}

function advertirBusquedaTramero() {
    var numDesde = document.forms[0].txtNumDesde.value;
    var numHasta = document.forms[0].txtNumHasta.value;
        <% if (existeTramero.equals("si")) { %>
        if ((numDesde!="" && numDesde!=null) || (numHasta!="" && numHasta!=null)) {
            if (jsp_alerta("",'<%=descriptor.getDescripcion("msjBusqTramero")%>')) {
                return true;
            } else
                return false;
        } else
            return true;
        <% } else { %>
        return true;
        <% } %>
}

/* fin anadir ECO/ESI */


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
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }

      
	if (teclaAuxiliar == 1){
            if (comboProvincia.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboProvincia.ocultar()',20);
            if (comboMunicipio.base.style.visibility == "visible" && isClickOutCombo(comboMunicipio,coordx,coordy)) setTimeout('comboMunicipio.ocultar()',20);
            if (comboECO.base.style.visibility == "visible" && isClickOutCombo(comboECO,coordx,coordy)) setTimeout('comboECO.ocultar()',20);
            if (comboESI.base.style.visibility == "visible" && isClickOutCombo(comboESI,coordx,coordy)) setTimeout('comboESI.ocultar()',20);
            if (comboPostal.base.style.visibility == "visible" && isClickOutCombo(comboPostal,coordx,coordy)) setTimeout('comboPostal.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            if (comboProvincia.base.style.visibility == "visible") comboProvincia.ocultar();
            if (comboMunicipio.base.style.visibility == "visible") comboMunicipio.ocultar();
            if (comboECO.base.style.visibility == "visible") comboECO.ocultar();
            if (comboESI.base.style.visibility == "visible") comboESI.ocultar();
            if (comboPostal.base.style.visibility == "visible") comboPostal.ocultar();
        }

 keyDel(evento);
}
function recuperaDatosIniciales(){
  comboPostal.addItems(codPostales,codPostales);
  /* anadir ECO/ESI */
  comboECO.addItems(codECOs,descECOs);
  comboESI.addItems(codESIs,descESIs);
  /* Fin anadir ECO/ESI */
    comboProvincia.addItems(codProvincias, descProvincias);
    comboMunicipio.addItems(codMunicipios, descMunicipios);
}

function inicializar() {
  	vCamposTexto = new Array(document.forms[0].codProvincia, document.forms[0].descProvincia
					,document.forms[0].codMunicipio, document.forms[0].descMunicipio
					,document.forms[0].txtDomicilio,document.forms[0].txtRefCatastral
					,document.forms[0].txtNumDesde,document.forms[0].txtLetraDesde
  					,document.forms[0].txtNumHasta,document.forms[0].txtLetraHasta
					,document.forms[0].txtBloque,document.forms[0].txtPortal
					,document.forms[0].txtEsc,document.forms[0].txtPlta
					,document.forms[0].txtPta,document.forms[0].txtDomicilio);
	vPrismatVia = new Array( document.forms[0].botonV);
    recuperaDatosIniciales(); // Listas
	valoresPorDefecto();
	mostrarCapasBotones('capaBotones2');
<%if(hayBuscada.equals("si")){ %>
    recargaDomicilio();
<% } else if(hayBuscada.equals("siInicio")) { %>
    recargaDomicilio();
<% } else { %>
    borrarDatos();
<% } %>
    pleaseWait1('off',top.mainFrame);
}
function borrarDatos() {
    comboECO.selectItem(-1);
	comboESI.selectItem(-1);
	comboPostal.selectItem(-1);
	document.forms[0].codVia.value = "";
	document.forms[0].descVia.value = "";
    document.forms[0].txtCodVia.value = "";
	document.forms[0].txtCodViaOculto.value = "";
    document.forms[0].codTVia.value = "";
    document.forms[0].descTVia.value = "";
    document.forms[0].txtNombreVia.value = "";
    document.forms[0].txtNumDesde.value = "";
    document.forms[0].txtLetraDesde.value = "";
    document.forms[0].txtNumHasta.value = "";
    document.forms[0].txtLetraHasta.value = "";
    document.forms[0].txtBloque.value = "";
    document.forms[0].txtPortal.value = "";
    document.forms[0].txtEsc.value = "";
    document.forms[0].txtPlta.value = "";
    document.forms[0].txtPta.value = "";
    document.forms[0].txtDomicilio.value = "";
    document.forms[0].txtRefCatastral.value = "";
    expedientesSeleccionados = new Array();
}
function valoresPorDefecto(){
    document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
    document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
    document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
    document.forms[0].codMunicipio.value = <%= codMun %>;
    document.forms[0].codECO.value = codECOs[0];
    document.forms[0].descECO.value = descECOs[0];
	/* anadir ECOESI */
	codESIs = codESIsOld;
	descESIs = descESIsOld;
	listaECOESIs = listaECOESIsOld;
	comboESI.addItems(codESIs,descESIs);
    comboECO.selectItem(-1);
	comboESI.selectItem(-1);
	/* fin anadir ECOESI */

}
function pulsarLimpiar(){
	borrarDatos();
	valoresPorDefecto();
	window.focus();
}
function pulsarCancelar() {
    self.parent.opener.retornoXanelaAuxiliar();
}
function recargaDomicilio() {
<% if(hayBuscada.equals("si")) { %>
    document.forms[0].codLocalizacionNueva.value = '<%= codLocalizacion %>';
<% } else { %>
    document.forms[0].codLocalizacion.value = '<%= codLocalizacion %>';
<% } %>
  document.forms[0].descPostal.value = '<%= codPostal %>';
  document.forms[0].txtNumDesde.value = '<%= numDesde %>';
  document.forms[0].txtLetraDesde.value = '<%= letraDesde %>';
  document.forms[0].txtNumHasta.value = '<%= numHasta %>';
  document.forms[0].txtLetraHasta.value = '<%= letraHasta %>';
  document.forms[0].txtBloque.value = '<%= bloque %>';
  document.forms[0].txtPortal.value = '<%= portal %>';
  document.forms[0].txtEsc.value = '<%= escal %>';
  document.forms[0].txtPlta.value = '<%= planta %>';
  document.forms[0].txtPta.value = '<%= puerta %>';
  document.forms[0].txtDomicilio.value = '<%= domicilio %>';
  document.forms[0].txtRefCatastral.value = '<%= refCatastral %>';
  if ('<%= codECO%>'!=""){
	document.forms[0].codECO.value = '<%= codECO%>';
    document.forms[0].descECO.value = '<%= descECO%>';
  }if ('<%= codESI%>'!=""){
    document.forms[0].codESI.value = '<%= codESI %>';
    document.forms[0].descESI.value = '<%= descESI %>';
  }if ('<%= codVia%>'!=""){
  	document.forms[0].codVia.value = '<%= codVia %>';
  	document.forms[0].descVia.value = '<%= descVia %>';
  	document.forms[0].txtCodVia.value = '<%= codVia %>';
  	document.forms[0].txtNombreVia.value = '<%= descVia %>';
	document.forms[0].txtCodViaOculto.value = '<%= idVia %>';
    document.forms[0].codTVia.value = '<%=codTVia%>';
    document.forms[0].descTVia.value = '<%=descTVia%>';
  }
}
function separadorComaDomicilio(domicilio){
    var l = domicilio.length;
    if (l>=2){
        if (domicilio.substring(l-2,l)==', ')
            return '';
        else return ', ';
    } else {
        return '';
    }
}
function domicilioCompleto(){
    var domCompleto="";
    with (document.forms[0]) {
        if (descVia.value!=''){
            if (codTVia.value != '<%=CodigoSinTipoVia%>'){
                domCompleto += descTVia.value + " ";
            }
            domCompleto += descVia.value;
        }
        //añado la puerta, piso, letra.., si tiene
         var caracteristicasDom="";
       // caracteristicasDom += separadorComaDomicilio(domCompleto);
        caracteristicasDom += ( txtNumDesde.value !='')?(' '+txtNumDesde.value ):'';
        caracteristicasDom += ( txtLetraDesde.value !='')?(' '+txtLetraDesde.value):'';
        caracteristicasDom += ( txtNumHasta.value !='')?('-'+txtNumHasta.value):'';
        caracteristicasDom += ( txtLetraHasta.value !='')?(' '+txtLetraHasta.value):'';
        caracteristicasDom += ( txtBloque.value !='')?(' Blq. '+txtBloque.value):'';
        caracteristicasDom += ( txtPortal.value !='')?(' Port. '+txtPortal.value):'';
        caracteristicasDom += ( txtEsc.value !='')?(' Esc. '+txtEsc.value):'';
        caracteristicasDom += ( txtPlta.value !='')?(' Plta. '+txtPlta.value):'';
        caracteristicasDom += ( txtPta.value !='')?(' Pta. '+txtPta.value):'';

        if(domCompleto!="" && caracteristicasDom!="")domCompleto +=", "+caracteristicasDom;
        else domCompleto +=caracteristicasDom;

        var txtDom="";
        if (txtDomicilio.value!=''){
            txtDom = txtDomicilio.value;
        }
          if(caracteristicasDom!="" && txtDom!="")domCompleto +=", "+txtDom;
          else domCompleto +=txtDom;

        //domCompleto += separadorComaDomicilio(domCompleto);
        domCompleto +=  descESI.value;
        if (!eval(codECO.value==0) && !eval(codECO.value=="")){
            //domCompleto += separadorComaDomicilio(domCompleto);
            domCompleto += descECO.value;
        }
     /*   if(Trim(txtRefCatastral.value) != ''){
            domCompleto += separadorComaDomicilio(domCompleto);
            domCompleto += Trim(txtRefCatastral.value);
        }*/
    }
    return domCompleto;
}
/* Depende de domicilio completo */
function domicilioConsulta(){
    var domConsulta="";
    with (document.forms[0]) {
        if (descVia.value!=''){
            if (codTVia.value != '<%=CodigoSinTipoVia%>'){
                domConsulta += descTVia.value + " ";
            }
            domConsulta += descVia.value;
        }
        domConsulta +="*";
        domConsulta += ( txtNumDesde.value !='')?(' '+txtNumDesde.value ):'';
        domConsulta += ( txtLetraDesde.value !='')?(' '+txtLetraDesde.value):'';
        domConsulta += ( txtNumHasta.value !='')?('-'+txtNumHasta.value):'';
        domConsulta += ( txtLetraHasta.value !='')?(' '+txtLetraHasta.value):'';
        domConsulta += ( txtBloque.value !='')?(' Blq. '+txtBloque.value):'';
        domConsulta += ( txtPortal.value !='')?(' Port. '+txtPortal.value):'';
        domConsulta += ( txtEsc.value !='')?(' Esc. '+txtEsc.value):'';
        domConsulta += ( txtPlta.value !='')?(' Plta. '+txtPlta.value):'';
        domConsulta += ( txtPta.value !='')?(' Pta. '+txtPta.value):'';
        domConsulta +="*";
        if (txtDomicilio.value!=''){
            domConsulta += txtDomicilio.value;
        }
        domConsulta +="*" + descESI.value +"*";
        if (!eval(codECO.value==0) && !eval(codECO.value=="")){
            domConsulta += descECO.value;
        }
    }
    return domConsulta;
}
function pulsarAceptar(){
        var prov = Trim(document.forms[0].codProvincia.value);
        if (prov.length==1) prov = '0'+prov;
        <% if ("si".equals(consulta)) { %>
        if (Trim(document.forms[0].codVia.value) == ''  && Trim(document.forms[0].txtDomicilio.value) == '' ){            
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoViaDom")%>');
        } else if ((Trim(document.forms[0].descPostal.value)!='') && (Trim(document.forms[0].descPostal.value.substring(0,2) ) !=  prov )){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoCPProvincia")%>');
        }else{            
            pulsarAceptarConsulta();
        }
        <% } else { %>        
        pulsarGrabar();
        <% } %>


}

/* CONSULTA */
function pulsarAceptarConsulta() {
/*<!--
Si la via seleccionada no es de la base de datos de Agora se inserta en esta
Una via no pertenece a Agora si su codigo es 0 (codigo no existente en Agora)
    -->*/
if (document.forms[0].txtCodViaOculto.value == "0"){
    document.forms[0].opcion.value="altaExt";
    document.forms[0].action="<html:rewrite page='/terceros/mantenimiento/Viales.do'/>";
    document.forms[0].submit();
}

    document.forms[0].descLocalizacionNueva.value = domicilioCompleto();
    var retorno = new Array();
    retorno[0] = document.forms[0].descLocalizacionNueva.value;
    retorno[1] = ''; // Demas criterios para consulta
    retorno[2] = domicilioConsulta();
    retorno[3] = document.forms[0].txtRefCatastral.value;
    self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function tratarExpedientesSeleccionados(){
    var salida = "";
    var ALMOHADILLA = "#";
    var ultimo;
    ultimo = expedientesSeleccionados[expedientesSeleccionados.length-1];
    if(ultimo=="modificarExpedientesRelacionados")
        modificarExpedientesRelacionados = true;
   
    if(expedientesSeleccionados!=null && expedientesSeleccionados.length>0){
        for(i=0;i<(expedientesSeleccionados.length-1);i++){
                salida += expedientesSeleccionados[i];
                if((expedientesSeleccionados.length-1)-i>1) salida += ALMOHADILLA;
        }// for
    }// if
    return salida;
}

/* NO CONSULTA */
function pulsarGrabar() {
  var prov = Trim(document.forms[0].codProvincia.value);
  if (prov.length==1) prov = '0'+prov;
  if( validarObligatorios('<%=descriptor.getDescripcion(" ")%>')){
    if (Trim(document.forms[0].codVia.value) == ''  && Trim(document.forms[0].txtDomicilio.value) == '' ){        
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoViaDom")%>');

    } else if ((Trim(document.forms[0].descPostal.value)!='') && (Trim(document.forms[0].descPostal.value.substring(0,2) ) !=  prov )){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoCPProvincia")%>');
    }else{
        var expedientes = tratarExpedientesSeleccionados();
        document.forms[0].expSeleccionadosMismaLocalizacion.value = expedientes;        
        document.forms[0].txtNombreVia.value = document.forms[0].descVia.value;
        document.forms[0].txtDomicilio.disabled = false;
        document.forms[0].codMunicipio.disabled = false;
        document.forms[0].descLocalizacionNueva.value = domicilioCompleto();        
        document.forms[0].opcion.value="grabarLocalizacion";
        document.forms[0].target="oculto";
        
        var parametroCancelar ="si";
        if(modificarExpedientesRelacionados){
            parametroCancelar = "no";
        }  
        
        document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>?cancelarExpRelacionadosLocalizacion=" + parametroCancelar;
        document.forms[0].submit();
    }
  }
}
function grabacionLocalizacion(respOpcion, codLoc) {
    pulsarAceptar2(codLoc);
}
function pulsarAceptar2(codLoc) {
  var retorno = new Array();
  retorno[0] = document.forms[0].descLocalizacionNueva.value;//retorno[0] = domicilioCompleto();//document.forms[0].txtDomicilio.value;
  retorno[1] = codLoc;
  retorno[2] = document.forms[0].txtRefCatastral.value;
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}
function grabacionDomicilio(respOpcion,codigoLocalizacion) {
    if(respOpcion == "domicilioNoGrabado") {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjDomNoGrab")%>');
    } else if(respOpcion == "domicilioGrabado") {
      document.forms[0].codLocalizacion.value = codigoLocalizacion;
      jsp_alerta('A','<%=descriptor.getDescripcion("msjDomGrab")%>');
      desactivarFormulario();
      mostrarCapasBotones('capaBotones2');
    }
}
/* FIN NO CONSULTA */
function desactivarFormulario() {
	deshabilitarGeneral(vCamposTexto);
	deshabilitarImagen(vPrismatVia,true);
    var vectorBotones = new Array(document.forms[0].cmdAlta,document.forms[0].cmdBuscar,document.forms[0].cmdCancelar,
                                  document.forms[0].cmdAceptar,document.forms[0].cmdCancelarBusqueda,document.forms[0].cmdGrabar,
                                  document.forms[0].cmdCancelarAlta);
    habilitarGeneral(vectorBotones);
    comboECO.deactivate();
	comboESI.deactivate();
    comboPostal.deactivate();
}
function activarFormulario() {
	habilitarGeneral(vCamposTexto);
	habilitarImagen(vPrismatVia,true);
    comboECO.activate();
	comboESI.activate();
    comboPostal.activate();
}
function pulsarEliminar() {
	document.forms[0].txtDomicilio.disabled = false;
    document.forms[0].codMunicipio.disabled = false;
    document.forms[0].opcion.value="eliminarLocalizacion";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
    document.forms[0].submit();
}
function eliminarLocalizacion(respOpcion) {
  pulsarEliminar2();
}
function pulsarEliminar2() {
var retorno = new Array();
  retorno[0] = "";
  retorno[1] = "";
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function cargarListaMunicipios() {
    document.forms[0].opcion.value = "cargarMunicipios";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/terceros/mantenimiento/Parametros.do'/>";
    document.forms[0].submit();
}

function desHabilitarECOESIVIA(valor) {
    if (valor) {
        comboECO.deactivate();
        comboESI.deactivate();
    } else {
        comboECO.activate();
        comboESI.activate();
    }
}


function pulsarBuscarLocalizacion(){  
  var prov = Trim(document.forms[0].codProvincia.value);
  if (prov.length==1) prov = '0'+prov;
  if( validarObligatorios('<%=descriptor.getDescripcion(" ")%>')){
    if (Trim(document.forms[0].codVia.value) == ''  && Trim(document.forms[0].txtDomicilio.value) == '' ){        
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoViaDom")%>');
    }else{
        document.forms[0].txtNombreVia.value = document.forms[0].descVia.value;
        document.forms[0].txtDomicilio.disabled = false;
        document.forms[0].codMunicipio.disabled = false;
        document.forms[0].descLocalizacionNueva.value = domicilioCompleto();

        // Se abre la ventana con el resultado d ela busqueda
        var argumentos = new Array();
        var source = "<c:url value='/sge/ConsultaExpedientes.do?opcion=expedientesRelacionadosConLocalizacion'/>"
        +"&loc="  + document.forms[0].descLocalizacionNueva.value + "&num=" + document.forms[0].numeroExpediente.value;
        
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,argumentos,
	'width=975,height=570,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=null && datos.length>0){
                            // Se almacenan los expedientes seleccionados con la misma localización en la variable global expedientesSeleccionados
                           expedientesSeleccionados = datos;
                        }
                 });
    }
  }
} //pulsarBuscarLocalizacion

</SCRIPT>
</head>
<BODY class="bandaBody" onload="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/sge/TramitacionExpedientes.do" target="_self">
<input  type="hidden" name="opcion">
<input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
<input type="hidden" name="ventana" value="mainFrame">
<html:hidden  property="codLocalizacion" />
<html:hidden  property="ejercicio" />
<html:hidden  property="numeroExpediente" />
<html:hidden  property="codTramite" />
<input type="hidden" name="txtCodViaOculto">
<input type="hidden" name="txtCodVia">
<input type="hidden" name="codTVia">
<input type="hidden" name="descTVia">
<input type="hidden" name="txtNombreVia">
<input type="hidden" name="codLocalizacionNueva">
<input type="hidden" name="descLocalizacionNueva">
<input type="hidden" name="expSeleccionadosMismaLocalizacion">

<div id="titulo" class="txttitblanco">
    <%=descriptor.getDescripcion("tit_Domicilios")%>
</div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td  align="left">
                    <table style="width: 100%;" cellspacing="0px" cellpadding="0px">
                        <tr>
                            <td style="width: 8%;" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                            <td style="width: 39%;">
                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="codProvincia" style="width:10%"/>
                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="descProvincia" style="width:80%" readonly="true"/>
                                <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:pointer;"></span></a>
                            </td>
                            <td style="width: 12%; margin-left:1px" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
                            <td style="padding-left:3px">
                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codMunicipio" style="width:10%"/>
                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="descMunicipio" style="width:80%" readonly="true"/>
                                <a id="anchorMunicipio" name="anchorMunicipio" href="">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio" style="cursor:pointer;"></span>
                                </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- anadir ECO/ESI -->
            <tr>
                <td style="width: 100%;" align="center" cellpadding="0px" cellspacing="0px">
                    <%
                    if (ecv.equals("visible")) {
                    %>
                    <DIV STYLE="width: 50%; visibility:visible; display: inline; float:left">
                    <%
                    } else {
                    %>
                    <DIV STYLE="visibility:hidden; display: none">
                    <%
                    }
                    %>
                        <table STYLE="display: inline;" cellpadding="0" cellspacing="0">
                            <tr>
                                <td style="width: 22%;" class="etiqueta"><%=descriptor.getDescripcion("gEtiqParroquia")%>:</td>
                                <td>
                                    <input type="text" id="codECO" name="codECO" class="inputTexto" size="5">
                                    <input type="text" id="descECO" name="descECO" class="inputTexto" style="width:260" readonly="true">
                                    <a href="" id="anchorECO" name="anchorECO"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonECO" name="botonECO" style="cursor:'hand'"></span></a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <%
                    if (esv.equals("visible")) {
                    %>
                    <DIV STYLE="width: 50%; visibility:visible; float: right; display: inline; ">
                    <%
                    } else {
                    %>
                    <DIV STYLE="visibility:hidden; display: none">
                    <%
                    }
                    %>
                        <table STYLE="display: inline;" cellpadding="0" cellspacing="0">
                            <tr>
                                <td style="width: 24%;" class="etiqueta"><%=descriptor.getDescripcion("gEtiqLugar")%>:</td>
                                <td>
                                    <input type="text" id="codESI" name="codESI" class="inputTexto" size="5">
                                    <input type="text" id="descESI" name="descESI" class="inputTexto" style="width:260" readonly="true">
                                    <a href="" id="anchorESI" name="anchorESI"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonESI" name="botonESI" style="cursor:hand; border: 0px none"></span></a>
                                </td>
                            </tr>
                        </table>
                    </div>

                </td>
            </tr>
            <tr>
                <td style="width: 100%" align="center">
                    <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
                        <tr>
                            <td style="width: 11%" class="etiqueta"><%=descriptor.getDescripcion("etiqVia")%>:</td>
                            <td style="width: 39%" class="columnP">
                                <input type="hidden" name="codVia">
                                <input type="text" name="descVia" class="inputTexto" style="width:85%" maxlength=27 onkeypress="PasaAMayusculas(event);">
                                <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Vía" style="cursor:hand; border: 0px none;" onclick="pulsarBuscarVia('buscarVias');"></span>
                            </td>
                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmplazamiento")%>:</td>
                            <td class="columnP" style="padding-left:3px">
                                <input type="text" name="txtDomicilio" class="inputTexto" style="width:85%" maxlength=100 onKeyPress="return PasaAMayusculas(event);">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- fin anadir ECO/ESI -->
            <tr>
                <td>
                    <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                        <tr>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_PNumero")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_UNumero")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("manTer_EtiqBLQ")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("manTer_EtiqPTL")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_Escalera")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_Planta")%></td>
                            <td class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_Puerta")%></td>
                        </tr>
                        <tr>
                            <td class="columnP" align="left">
                                <html:text styleClass="inputTexto"  property="txtNumDesde" size="10" maxlength="3" onkeypress = "javascript:return SoloDigitos(event);"/>
                            </td>
                            <td class="columnP" align="left">
                                <html:text styleClass="inputTexto"  property="txtLetraDesde" size="10" maxlength="1" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                            <td class="columnP" align="left">
                                <html:text styleClass="inputTexto"  property="txtNumHasta" size="10" maxlength="3" onkeypress = "javascript:return SoloDigitos(event);"/>
                            </td>
                            <td class="columnP" align="left">
                                <html:text styleClass="inputTexto"  property="txtLetraHasta" size="10" maxlength="1" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                            <td class="columnP"  align="left">
                                <html:text styleClass="inputTexto"  property="txtBloque" size="10" maxlength="3" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                            <td class="columnP"  align="left">
                                <html:text styleClass="inputTexto"  property="txtPortal" size="10" maxlength="2" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                            <td class="columnP"  align="left">
                                <html:text styleClass="inputTexto"  property="txtEsc" size="10" maxlength="2" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                            <td class="columnP"  align="left">
                                <html:text styleClass="inputTexto"  property="txtPlta" size="10" maxlength="3" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                            <td class="columnP"  align="left">
                                <html:text styleClass="inputTexto"  property="txtPta" size="10" maxlength="4" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
                        <tr>
                            <td style="width: 11%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqRefCatastral")%>:</td>
                            <td style="width: 39%">
                                <html:text  styleClass="inputTexto"  property="txtRefCatastral" style="width:308px" maxlength="20" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </td>
                            <td class="etiqueta" style="width: 12%"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</td>
                            <td style="padding-left:3px">
                                <html:text styleClass="inputTexto" property="descPostal" size="11" maxlength="5"  />
                                    <A href="" id="anchorPostal" name="anchorPostal">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonPostal" name="botonPostal" style="cursor:hand; border: 0px none"></span>
                                    </A>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    <!-------------------------------------- BOTONES. ------------------------------------------>
    <!-- Definimos varias capas para los botones según lo que se permite hacer. -->
    <DIV id="capaBotones2" name="capaBotones2" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onClick="pulsarAceptar();return false;" accesskey="A">
        <c:if test="${requestScope.desdeAltaLocalizacion eq 'si'}">
            <input type="button" class="botonMasLargo" value="<%=descriptor.getDescripcion("gbBusqLocalizacion")%>" name="cmdBusqLocalizacion" onClick="pulsarBuscarLocalizacion();return false;" accesskey="L">
        </c:if>
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar" onClick="pulsarLimpiar();return false;" accesskey="L">
        <% if (!"si".equals(consulta)) { %>
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" onClick="pulsarEliminar();return false;" accesskey="E">
        <% } %>
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarBusqueda" onClick="pulsarCancelar();return false;" accesskey="C">
    </DIV>
</DIV>
</html:form>
<script type="text/javascript">

    var	comboProvincia = new Combo("Provincia");
    var	comboMunicipio = new Combo("Municipio");

var comboECO = new Combo("ECO");
var comboESI = new Combo("ESI");
var comboPostal = new Combo("Postal");
comboECO.change = function() {
    limpiar(['codESI','descESI','codVia','descVia','txtCodVia','txtCodViaOculto', 'codTVia', 'descTVia', 'txtNombreVia', 'txtDomicilio']);
    if(comboECO.cod.value.length!=0){
        var i = comboECO.selectedIndex-1;
    }
}
comboESI.change = function() {
    limpiar(['codVia','descVia','txtCodVia','txtCodViaOculto', 'codTVia', 'descTVia', 'txtNombreVia', 'txtDomicilio']);
    if(comboESI.cod.value.length!=0){
        var i = comboESI.selectedIndex-1;
        if(i>=0){
            document.forms[0].codECO.value = listaECOESIs[i][0];
            document.forms[0].descECO.value = listaECOESIs[i][1];
        }
    }
}

function mostrarCapasBotones(nombreCapa) {
  document.getElementById('capaBotones2').style.display='none';
  document.getElementById(nombreCapa).style.display='block';
}

    comboProvincia.change = function() {
        limpiar(['codECO', 'descECO', 'codESI','descESI','codVia','descVia','txtCodVia','txtCodViaOculto', 'codTVia', 'descTVia', 'txtNombreVia', 'txtDomicilio']);
        limpiar(["codMunicipio","descMunicipio"]);
        if (comboProvincia.des.value.length != 0) {
            cargarListaMunicipios();
        } else {
            comboMunicipio.addItems([], []);
        }
    }

    comboMunicipio.change = function() {
        limpiar(['codECO', 'descECO', 'codESI','descESI','codVia','descVia','txtCodVia','txtCodViaOculto', 'codTVia', 'descTVia', 'txtNombreVia', 'txtDomicilio']);
        if (comboMunicipio.cod.value.length != 0) {
            if ((comboMunicipio.cod.value == "<%=ptVO.getMunicipio()%>") && ( comboProvincia.cod.value == "<%=ptVO.getProvincia()%>"))
                desHabilitarECOESIVIA(false);
            else desHabilitarECOESIVIA(true);
        }
    }

    function cargarComboBox(cod, des) {
        eval("comboMunicipio.addItems(cod, des)");
    }

</script></BODY></HTML>
