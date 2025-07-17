<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.DefinicionTramitesForm"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.sge.DefinicionTramitesValueObject"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto Tramite </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<%
  int idioma=1;
  int apl=4;
  Vector listaEnlaces = new Vector();
  UsuarioValueObject usuario = null;
  DefinicionTramitesForm dtForm = null;
    if (session!=null){
      dtForm = (DefinicionTramitesForm)session.getAttribute("DefinicionTramitesForm");
    listaEnlaces = dtForm.getListaEnlaces();
    usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
    }
 Vector listaDocusErroneos = dtForm.getListaDocusErroneos();
 Config m_Conf = ConfigServiceHelper.getConfig("error");
 Config m_Config = ConfigServiceHelper.getConfig("common");
 Config m_Documentos=ConfigServiceHelper.getConfig("documentos");	
    Boolean visibleAppExt=false;	
    try{	
    visibleAppExt=m_Documentos.getString("VISIBLE_EXT").toUpperCase().equals("SI");	
    }catch(Exception e){	
        visibleAppExt=false;	
    }

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

<script>


var listaTablaEntrada = new Array();
var listaTablaEntradaOriginal = new Array();
var listaDoc = new Array();
var listaDocOriginal = new Array();
var listaEnlaces = new Array();
var listaEnlacesOriginal = new Array();
var id_tramiteTabla = new Array();
var codigo_tramiteTabla = new Array();
var nombre_tramiteTabla = new Array();
var listaCampos = new Array();
var listaCamposOriginal = new Array();
var listaConfSW = new Array();
var listaConfSWCfos = new Array();
var etiqFirmaDoc = '<%=descriptor.getDescripcion("firmaDoc")%>';
var listaAgrupaciones = new Array();
var listaAgrupacionesOriginal = new Array();

var cont = 0;
var cont1 = 0;
var cont2 = 0;
var cont3 = 0;
var cont4 = 0;
var cont5 = 0;
var cont6 = 0;
var confSW = <%=session.getAttribute("configurandoSW")%>;

function redirecciona() {
    parent.mainFrame.pleaseWait('off');
	if (confSW=="1") {
		recargaConfSW();
	}
	else recargaTodo();
}

function recargaConfSW() {

    var tiposOperaciones       = new Array();
    var nombresOperaciones     = new Array();
    var nombresModulo          = new Array();
    var tipoOperacionRetroceso = new Array();

    <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaConfSW">
    
        listaConfSW[cont5] = ['<bean:write name="elemento" property="codIniciar"/>',
                              '<bean:write name="elemento" property="codAvanzar"/>' ,
	  						  '<bean:write name="elemento" property="codRetroceder"/>'];
                                                            
	listaConfSWCfos[cont5]        = ['<bean:write name="elemento" property="cfoIniciar"/>',
                                         '<bean:write name="elemento" property="cfoAvanzar"/>' ,
                                         '<bean:write name="elemento" property="cfoRetroceder"/>'];
                              
        tiposOperaciones[cont5]       = ['<bean:write name="elemento" property="tipoOperacionIniciar"/>','<bean:write name="elemento" property="tipoOperacionAvanzar"/>','<bean:write name="elemento" property="tipoOperacionRetroceder"/>'];
    	nombresOperaciones[cont5]     = ['<bean:write name="elemento" property="nombreOperacionIniciar"/>','<bean:write name="elemento" property="nombreOperacionAvanzar"/>','<bean:write name="elemento" property="nombreOperacionRetroceder"/>'];
        nombresModulo[cont5]          = ['<bean:write name="elemento" property="nombreModuloIniciar"/>','<bean:write name="elemento" property="nombreModuloAvanzar"/>','<bean:write name="elemento" property="nombreModuloRetroceder"/>']
        tipoOperacionRetroceso[cont5] = ['<bean:write name="elemento" property="codRetroceder"/>','<bean:write name="elemento" property="cfoRetroceder"/>','<bean:write name="elemento" property="tipoRetroceso"/>'];
        
        cont5++;  						  
    </logic:iterate>
        
    parent.mainFrame.recuperaConfSW(listaConfSW,listaConfSWCfos,tiposOperaciones,nombresOperaciones,nombresModulo,tipoOperacionRetroceso);
}

function recargaTodo() { 
    var orden = 0;
    var identificador;
    var codigo;
    var descripcion;
    var insertado = false;
    var datos = new Array();
    datos[1] = '<bean:write name="DefinicionTramitesForm" property="txtCodigo"/>';
    datos[2] = '<bean:write name="DefinicionTramitesForm" property="txtDescripcion"/>';
    datos[3] = '<bean:write name="DefinicionTramitesForm" property="numeroTramite"/>';
    datos[4] = '<bean:write name="DefinicionTramitesForm" property="nombreTramite"/>';
    datos[5] = '<bean:write name="DefinicionTramitesForm" property="codigoTramite"/>';
    datos[6] = '<bean:write name="DefinicionTramitesForm" property="descAreaTra"/>';
    datos[7] = '<bean:write name="DefinicionTramitesForm" property="ocurrencias"/>';
    datos[8] = '<bean:write name="DefinicionTramitesForm" property="codClasifTramite"/>';
    datos[9] = '<bean:write name="DefinicionTramitesForm" property="descClasifTramite"/>';
    datos[10] = '<bean:write name="DefinicionTramitesForm" property="plazo"/>';
    datos[11] = '<bean:write name="DefinicionTramitesForm" property="unidadesPlazo"/>';
    datos[12] = '<bean:write name="DefinicionTramitesForm" property="disponible"/>';
    datos[13] = '<bean:write name="DefinicionTramitesForm" property="codUnidadInicio"/>';
    datos[14] = '<bean:write name="DefinicionTramitesForm" property="descUnidadInicio"/>';
    datos[15] = '<bean:write name="DefinicionTramitesForm" property="codUnidadTramite"/>';
    datos[16] = '<bean:write name="DefinicionTramitesForm" property="descUnidadTramite"/>';
    datos[17] = '<bean:write name="DefinicionTramitesForm" property="posicion"/>';
    datos[18] = '<bean:write name="DefinicionTramitesForm" property="tramiteEliminado"/>';
    datos[19] = '<bean:write name="DefinicionTramitesForm" property="texto"/>';
    datos[20] = '<bean:write name="DefinicionTramitesForm" property="tipoCondicion"/>';
    datos[21] = '<bean:write name="DefinicionTramitesForm" property="tipoFavorableSI"/>';
    datos[22] = '<bean:write name="DefinicionTramitesForm" property="tipoFavorableNO"/>';
    datos[23] = '<bean:write name="DefinicionTramitesForm" property="unidadesPlazo"/>';
    datos[24] = '<bean:write name="DefinicionTramitesForm" property="tramiteInicio"/>';
    datos[25] = '<bean:write name="DefinicionTramitesForm" property="tramitePregunta"/>';
    datos[26] = unescape('<bean:write name="DefinicionTramitesForm" property="instrucciones"/>');
<logic:empty name="DefinicionTramitesForm" property="infoSWAvanzar">
    datos[27] = '';
</logic:empty>
<logic:notEmpty name="DefinicionTramitesForm" property="infoSWAvanzar">
    datos[27] = '<bean:write name="DefinicionTramitesForm" property="infoSWAvanzar.codOpSW"/>';
</logic:notEmpty>
<logic:empty name="DefinicionTramitesForm" property="infoSWRetroceder">
    datos[28] = '';
</logic:empty>
<logic:notEmpty name="DefinicionTramitesForm" property="infoSWRetroceder">
    datos[28] = '<bean:write name="DefinicionTramitesForm" property="infoSWRetroceder.codOpSW"/>';
</logic:notEmpty>
    datos[29] = '';
    datos[30] = '';
    datos[31] = '<bean:write name="DefinicionTramitesForm" property="notUnidadTramitIni"/>';
    datos[32] = '<bean:write name="DefinicionTramitesForm" property="notUnidadTramitFin"/>';
    datos[33] = '<bean:write name="DefinicionTramitesForm" property="notUsuUnidadTramitIni"/>';
    datos[34] = '<bean:write name="DefinicionTramitesForm" property="notUsuUnidadTramitFin"/>';
    datos[35] = '<bean:write name="DefinicionTramitesForm" property="notInteresadosIni"/>';
    datos[36] = '<bean:write name="DefinicionTramitesForm" property="notInteresadosFin"/>';
    datos[37] = '<bean:write name="DefinicionTramitesForm" property="codVisibleUnidadInicio"/>';
    datos[39] = '<bean:write name="DefinicionTramitesForm" property="codExpRel"/>';
    datos[40] = '<bean:write name="DefinicionTramitesForm" property="descExpRel"/>';
    datos[41] = '<bean:write name="DefinicionTramitesForm" property="codCargo"/>';
    datos[42] = '<bean:write name="DefinicionTramitesForm" property="codVisibleCargo"/>';
    datos[43] = '<bean:write name="DefinicionTramitesForm" property="plazoFin"/>';
    datos[44] = <bean:write name="DefinicionTramitesForm" property="generarPlazos"/>;
    datos[45] = <bean:write name="DefinicionTramitesForm" property="codigoInternoTramite"/>;
    datos[46] = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioTramiteIni"/>';
    datos[47] = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioTramiteFin"/>';
    datos[48] = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioExpedIni"/>';
    datos[49] = '<bean:write name="DefinicionTramitesForm" property="notUsuInicioExpedFin"/>';
    datos[50] = <bean:write name="DefinicionTramitesForm" property="tramiteNotificado"/>;
    datos[51] = '<bean:write name="DefinicionTramitesForm" property="notUsuTraFinPlazo"/>';	
    datos[52] = '<bean:write name="DefinicionTramitesForm" property="notUsuExpFinPlazo"/>';	
    datos[53] = '<bean:write name="DefinicionTramitesForm" property="notUORFinPlazo"/>';
    var numeroTramite = new Number(datos[3]);


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
    <%--
     
    listaTablaEntradaOriginal[cont1] = ['<bean:write name="elemento" property="idTramiteCondEntrada" />',
            '<bean:write name="elemento" property="tipoCondEntrada" />',
            '<bean:write name="elemento" property="codTramiteCondEntrada" />',
            '<bean:write name="elemento" property="descTramiteCondEntrada"/>',
            '<bean:write name="elemento" property="estadoTramiteCondEntrada" />',
            '<bean:write name="elemento" property="codCondEntrada" />'];
      --%>  
     listaTablaEntradaOriginal[cont1] = ['<bean:write name="elemento" property="idTramiteCondEntrada" />',
            '<bean:write name="elemento" property="tipoCondEntrada" />',
            '<bean:write name="elemento" property="codTramiteCondEntrada" />',
            '<bean:write name="elemento" property="descTramiteCondEntrada"/>',
            '<bean:write name="elemento" property="estadoTramiteCondEntrada" />',
            '<bean:write name="elemento" property="codCondEntrada" />',
            '<bean:write name="elemento" property="codigoDoc" />'];
        
    cont1++;
</logic:iterate>
<logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaDocumentos">
    var int = "";
    var rel = "";
    var docImg = "";
    var inter = '<bean:write name="elemento" property="interesado" />';
    var docAct = '<bean:write name="elemento" property="docActivo"/>';
    var relac = '<bean:write name="elemento" property="relacion" />';
    if (inter == "S") {
        int = "POR INTERESADO";
    } else {
        int = "NO POR INTERESADO";
    }
    if (relac.trim() == "S") {
        rel = "<%=m_Config.getString("constante.nombre.relacion").toUpperCase()%>";
    } else {
        rel = "EXPEDIENTE";
    }
    if (docAct == "SI") {
        docImg = '<span class="fa fa-check 2x"></span>';
    } else {
        docImg = '<span class="fa fa-close 2x"></span>';
    }
      <%if(visibleAppExt){%>	
        var docImg2="";	
        var visibleExt='<bean:write name="elemento" property="visibleExt"/>';	
        if (visibleExt == "SI") {	
            docImg2 = '<span class="fa fa-check 2x"></span>';	
        } else {	
            docImg2 = '<span class="fa fa-close 2x"></span>';	
        }	
        listaDoc[cont2] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>', int	
            , parent.mainFrame.getEstadoFirmaVisual('<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="firma"/>')	
            ,docImg,rel,'','<bean:write name="elemento" property="editorTexto" />',docImg2];	
    <%}else{%>	
        listaDoc[cont2] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>', int	
            , parent.mainFrame.getEstadoFirmaVisual('<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="firma"/>')	
            ,docImg,rel,'','<bean:write name="elemento" property="editorTexto" />'];	
    <%}%>
    cont2++;
</logic:iterate>
<logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaDocumentos">
<%if(visibleAppExt){%>	
    listaDocOriginal[cont3] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>',	
            '<bean:write name="elemento" property="visibleInternet" />', '<bean:write name="elemento" property="plantilla" />',	
            '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="interesado" />'	
            , '<bean:write name="elemento" property="firma" />'	
            ,'<bean:write name="elemento" property="docActivo"/>',null,null,'<bean:write name="elemento" property="visibleExt"/>'];	
    <%}else{%>	
     listaDocOriginal[cont3] = ['<bean:write name="elemento" property="codigoDoc" />', '<bean:write name="elemento" property="nombreDoc"/>',	
            '<bean:write name="elemento" property="visibleInternet" />', '<bean:write name="elemento" property="plantilla" />',	
            '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="interesado" />'	
            , '<bean:write name="elemento" property="firma" />'	
            ,'<bean:write name="elemento" property="docActivo"/>'];	
    <%}%>
    cont3++;
</logic:iterate>

<%for (int j=0;j<listaDocusErroneos.size();j++) { %>
<%String mensaje = MessageFormat.format(m_Conf.getString("error.plantillas.tramites"),
new Object[] { ((DefinicionTramitesValueObject)listaDocusErroneos.elementAt(j)).getCodPlantilla(),
((DefinicionTramitesValueObject)listaDocusErroneos.elementAt(j)).getCodigoDoc(),
((DefinicionTramitesValueObject)listaDocusErroneos.elementAt(j)).getCodigoTramite()});%>
    jsp_alerta("A", '<%=mensaje%>');
<%}%>

<logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaTramites">
    identificador = '<bean:write name="elemento" property="identificador"/>';
    codigo = new Number('<bean:write name="elemento" property="codigo"/>');
    descripcion = '<bean:write name="elemento" property="descripcion"/>';
    if ((codigo > numeroTramite) && (!insertado)) {
        id_tramiteTabla[orden] = datos[5];
        codigo_tramiteTabla[orden] = datos[3];
        nombre_tramiteTabla[orden] = datos[4];
        orden++;
        id_tramiteTabla[orden] = identificador;
        codigo_tramiteTabla[orden] = codigo;
        nombre_tramiteTabla[orden] = descripcion;
        insertado = true;
    } else {
        id_tramiteTabla[orden] = identificador;
        codigo_tramiteTabla[orden] = codigo;
        nombre_tramiteTabla[orden] = descripcion;
    }
    orden++;
</logic:iterate>
    if (!insertado) {
        id_tramiteTabla[orden] = datos[5];
        codigo_tramiteTabla[orden] = datos[3];
        nombre_tramiteTabla[orden] = datos[4];
    }

    var i = 0;
<%for(int j=0;j<listaEnlaces.size();j++){%>
    listaEnlaces[i] = ['<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("descripcion")%>',
            '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("url")%>',
            '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("estado")%>'];
    listaEnlacesOriginal[i] = ['<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("codigo")%>',
            '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("descripcion")%>',
            '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("url")%>',
            '<%=(String)((GeneralValueObject)listaEnlaces.get(j)).getAtributo("estado")%>'];
    i++;
<%}%>

    var campoAct = "";
    var campoImg = "";
<logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaCampos">
    campoAct = '<bean:write name="elemento" property="activo" />';
    if (campoAct == 'SI') {
        campoImg = '<span class="fa fa-check 2x"></span>';
    } else {
        campoImg = '<span class="fa fa-close 2x"></span>';
    }
    listaCampos[cont4] = ['<bean:write name="elemento" property="codCampo" />',
            '<bean:write name="elemento" property="descCampo" />',campoImg];
    listaCamposOriginal[cont4] = ['<bean:write name="elemento" property="codCampo" />','<bean:write name="elemento" property="descCampo" />',
            '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="codTipoDato" />',
            '<bean:write name="elemento" property="tamano" />','<bean:write name="elemento" property="descMascara" />',
            '<bean:write name="elemento" property="obligat" />','<bean:write name="elemento" property="orden" />',
            '<bean:write name="elemento" property="descPlantilla" />','<bean:write name="elemento" property="descTipoDato" />',
            '<bean:write name="elemento" property="rotulo" />','<bean:write name="elemento" property="visible" />',
            '<bean:write name="elemento" property="activo" />','<bean:write name="elemento" property="oculto" />',
            '<bean:write name="elemento" property="bloqueado" />','<bean:write name="elemento" property="plazoFecha" />',
            '<bean:write name="elemento" property="checkPlazoFecha" />',
            '<bean:write name="elemento" property="validacion" />','<bean:write name="elemento" property="operacion" />',
            '<bean:write name="elemento" property="codAgrupacion" />','<bean:write name="elemento" property="posX" />',
            '<bean:write name="elemento" property="posY" />'];
    cont4++;
</logic:iterate>

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

var tiposOperaciones    = new Array();
var nombresOperaciones  = new Array();
var nombresModulo       = new Array();
var tipoOperacionRetroceso = new Array();

<logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaConfSW">
  	listaConfSW[cont5] = ['<bean:write name="elemento" property="codIniciar"/>' ,
                          '<bean:write name="elemento" property="codAvanzar"/>' ,
  						  '<bean:write name="elemento" property="codRetroceder"/>'];
                          

  	listaConfSWCfos[cont5] = ['<bean:write name="elemento" property="cfoIniciar"/>' ,
                              '<bean:write name="elemento" property="cfoAvanzar"/>' ,
                              '<bean:write name="elemento" property="cfoRetroceder"/>'];

    tiposOperaciones[cont5]   = ['<bean:write name="elemento" property="tipoOperacionIniciar"/>','<bean:write name="elemento" property="tipoOperacionAvanzar"/>','<bean:write name="elemento" property="tipoOperacionRetroceder"/>'];
    nombresOperaciones[cont5] = ['<bean:write name="elemento" property="nombreOperacionIniciar"/>','<bean:write name="elemento" property="nombreOperacionAvanzar"/>','<bean:write name="elemento" property="nombreOperacionRetroceder"/>'];
    nombresModulo[cont5]      = ['<bean:write name="elemento" property="nombreModuloIniciar"/>','<bean:write name="elemento" property="nombreModuloAvanzar"/>','<bean:write name="elemento" property="nombreModuloRetroceder"/>']
    tipoOperacionRetroceso[cont5] = ['<bean:write name="elemento" property="codRetroceder"/>','<bean:write name="elemento" property="cfoRetroceder"/>','<bean:write name="elemento" property="tipoRetroceso"/>'];
    cont5++;  						  
</logic:iterate>

  i = 0;
  listaUnidadesTramitadoras = new Array();
  <logic:present name="DefinicionTramitesForm" property="unidadesTramitadoras">
      <logic:iterate id="uor" name="DefinicionTramitesForm" property="unidadesTramitadoras">
          listaUnidadesTramitadoras[i] = '<bean:write name="uor" property="uor_cod"/>';
          i++;
      </logic:iterate>
  </logic:present>

    var enlace = '<bean:write name="DefinicionTramitesForm" property="tramiteActual"/>';
    var numTramites = '<bean:write name="DefinicionTramitesForm" property="numeroTramites"/>';

     //notificaciones automaticas

var notificarCercaFinPlazo=<bean:write name="DefinicionTramitesForm" property="notificarCercaFinPlazo"/>;
var notificarFueraDePlazo=<bean:write name="DefinicionTramitesForm" property="notificarFueraDePlazo"/>;
var tipoNotCercaFinPlazo='<bean:write name="DefinicionTramitesForm" property="tipoNotCercaFinPlazo"/>';
var tipoNotFueraDePlazo='<bean:write name="DefinicionTramitesForm" property="tipoNotFueraDePlazo"/>';
var admiteNotifElect = '<bean:write name="DefinicionTramitesForm" property="admiteNotificacionElectronica"/>';
var tipoNotifElect = '<bean:write name="DefinicionTramitesForm" property="codigoTipoNotificacionElectronica"/>';
var tipoUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="tipoUsuarioFirma"/>';
var codigoOtroUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="codigoOtroUsuarioFirma"/>';
var nombreOtroUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="nombreOtroUsuarioFirma"/>';

var notifObligatoria = '<bean:write name="DefinicionTramitesForm" property="notificacionElectronicaObligatoria"/>';
var certificadoOrganismo = '<bean:write name="DefinicionTramitesForm" property="certificadoOrganismoFirmaNotificacion"/>';
/************ Carga de plugin de pantallas de tramitacion externa *****************/
var contPantallas     = 0;
var codPluginSeleccionado	    = '<bean:write name="DefinicionTramitesForm" property="codPluginPantallaTramitacionExterna" ignore="true"/>';
var urlPluginSeleccionado		= '<bean:write name="DefinicionTramitesForm" property="urlPluginPantallaTramitacionExterna" ignore="true"/>';
var implClassPluginSeleccionado = '<bean:write name="DefinicionTramitesForm" property="implClassPluginPantallaTramitacionExterna" ignore="true"/>';

var contPantallas = 0;
var listaPluginPantallaTramitacionExterna =  new Array();
var listaUrlPantallaTramitacionExterna    = new Array();

var codigosPlugin =  new Array();
var descripcionesPlugin = new Array();

<logic:iterate name="DefinicionTramitesForm" property="pluginPantallasTramitacionExterna" id="pantallaExterna">
    var  codAux = '<bean:write name="pantallaExterna" property="nombrePlugin" ignore="true"/>';
    listaPluginPantallaTramitacionExterna[contPantallas] = [codAux,'<bean:write name="pantallaExterna" property="descripcionPluginDefinicionTramite" ignore="true"/>','<bean:write name="pantallaExterna" property="implClass" ignore="true"/>'];
    codigosPlugin[contPantallas] = codAux;
    descripcionesPlugin[contPantallas] = '<bean:write name="pantallaExterna" property="descripcionPluginDefinicionTramite" ignore="true"/>';
    
    var contadorEnlaces = 0;
     <logic:iterate name="pantallaExterna" property="enlaces" id="enlace">        
        listaUrlPantallaTramitacionExterna[contadorEnlaces] = [codAux,'<bean:write name="enlace" property="url" ignore="true"/>','<bean:write name="enlace" property="idEnlaceConfiguracion" ignore="true"/>'];
        contadorEnlaces++;
    </logic:iterate>
    contPantallas++;
</logic:iterate>

parent.mainFrame.actualizarListadoPluginTramitacionExterna(codPluginSeleccionado,urlPluginSeleccionado,implClassPluginSeleccionado,listaPluginPantallaTramitacionExterna,listaUrlPantallaTramitacionExterna,codigosPlugin,descripcionesPlugin);

/*********************************************************************************/

var codDepartamento = '<bean:write name="DefinicionTramitesForm" property="codDepartamentoNotificacion"/>';

var listaNotAutomaticas=[notificarCercaFinPlazo,notificarFueraDePlazo,tipoNotCercaFinPlazo,tipoNotFueraDePlazo,admiteNotifElect,tipoNotifElect,tipoUsuarioFirma,codigoOtroUsuarioFirma,nombreOtroUsuarioFirma,notifObligatoria,certificadoOrganismo,codDepartamento];

    parent.mainFrame.document.forms[0].listaCodTramitesFlujoSalida.value = "";
    parent.mainFrame.document.forms[0].listaNombreTramitesFlujoSalida.value = "";
    parent.mainFrame.document.forms[0].listaDescTramitesFlujoSalida.value = "";
    parent.mainFrame.document.forms[0].listaNumerosSecuenciaFlujoSalida.value = "";
    parent.mainFrame.document.forms[0].oblig.value = "";
    parent.mainFrame.document.forms[0].numeroCondicionSalida.value = "";
    parent.mainFrame.document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value = "";
    parent.mainFrame.document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value = "";
    parent.mainFrame.document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value = "";
    parent.mainFrame.document.forms[0].listaNumerosSecuenciaFlujoSalidaNoFavorable.value = "";
    parent.mainFrame.document.forms[0].obligNoFavorable.value = "";
    parent.mainFrame.document.forms[0].numeroCondicionSalidaNoFavorable.value = "";
    
    parent.mainFrame.recuperaTramite(datos, listaTablaEntrada, listaTablaEntradaOriginal,
            codigo_tramiteTabla, nombre_tramiteTabla, listaDoc, listaDocOriginal,
            listaEnlaces, listaEnlacesOriginal, enlace, numTramites, id_tramiteTabla, codigo_tramiteTabla,
            nombre_tramiteTabla, listaCampos, listaCamposOriginal,listaConfSW,listaConfSWCfos, listaUnidadesTramitadoras, 
            listaNotAutomaticas,tiposOperaciones,nombresOperaciones,nombresModulo,tipoOperacionRetroceso,listaAgrupaciones, 
            listaAgrupacionesOriginal);

}

</script>
</head>
<body onLoad="redirecciona();">
</body>
</html>
