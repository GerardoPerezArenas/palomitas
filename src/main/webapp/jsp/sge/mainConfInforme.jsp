<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%  UsuarioValueObject usuarioVO = null;
    int idioma = 1;
    String usu = "";
    boolean mostrarTodosNiveles=true;
    if (session!=null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        usu = usuarioVO.getNombreUsu();
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    Config configExp = ConfigServiceHelper.getConfig("Expediente");
    
    String nivelesInforme=configExp.getString("nivelesInformesGestion");
    if("2".equals(nivelesInforme)) mostrarTodosNiveles=false;
    //System.out.println("\n\n\n Niveles---:"+nivelesInforme);
    //System.out.println("\n\n\n mostrar Niveles---:"+mostrarTodosNiveles);
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>Configuraci&oacute;n de Informe de Gesti&oacute;n</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />



    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.3.2.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-ui-1.7.3.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery-ui.css'/>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>

<script type="text/javascript">
var lStrGrupo = '';
var lStrFiltro = '';
var mStrCombo    = '';
var mIntCombo    = 0;
var lStrArea     = "imgCampo1";
var lStrUnidad   = "imgCampo2";
var lStrTipoProc = "imgCampo3";
var lStrProc     = "imgCampo4";
var lStrTipoTram = "imgCampo5";
var lStrTramite  = "imgCampo6";

var lStrClaveArea     = "ARE";
var lStrClaveUnidad   = "UTR";
var lStrClaveTipoProc = "TPR";
var lStrClaveProc     = "PRO";
var lStrClaveTipoTram = "CLS";
var lStrClaveTramite  = "TRA";

var srcObj = new Object;
var comboActual = -1;
var mStrOrigen = '';
var mStrDestino = '';

var mArrGrupo           = new Array ('', '', '', '', '', '');
var mArrValores         = new Array (0, 0, 0, 0, 0, 0);

var mArrFiltroCriterios = new Array ('', '', '', '', '', '');
var mArrFiltroValores   = new Array (0, 0, 0, 0, 0, 0);

var lObjCombo;

<%--Variable para controlar que se coge en el combo de tramite (El codigo del tramite si se desea filtrar o el texto del tramite si se desea generar el informe)--%>
var preenvio = false;

var niv=2;

$(function() {
    $("#lyrOrigen1,#lyrOrigen2,#lyrOrigen3,#lyrOrigen4,#lyrOrigen5,#lyrOrigen6").draggable({
        start: function() {
            srcObj = this;
            mStrOrigen = this.id;
            startDrag(this);
        },
        revert: true
    });
    $("#lyrDestino1,#lyrDestino2 <%if (mostrarTodosNiveles){%>,#lyrDestino3,#lyrDestino4,#lyrDestino5,#lyrDestino6<%}%>").droppable({
            //accept: '#lyrOrigen1, #lyrOrigen2,#lyrOrigen3,#lyrOrigen4,#lyrOrigen5,#lyrOrigen6',
            activeClass: 'ui-state-hover',
            hoverClass: 'ui-state-active',
            drop: function(event, ui) {
                    var dropOk=drop(event,this);

                    if (dropOk){
                        //to cancel the revert effect, just remove the helper
                        ui.helper.fadeOut();
                    }else{
                        //revert effect will be executed (as planned)
                        ObtenerDatosCombo();
                    }
            }
    });
});


function startDrag(pObjOrigen){
    mStrOrigen = pObjOrigen.id;
}

function finDrag(event,pObjOrigen){
    event= event || window.event;
    mover(event,pObjOrigen);
}

function Change (pIntCombo){
    if (mIntCombo != pIntCombo) ObtenerDatosCombo();
}

function CargarCombo(){
    var i, j, k;
    var lStrCapaDestino;
    var lIntCombo = 0;
    
    for (i = niv; i >= 1; i--){
        if (lIntCombo == 0){
            if (eval('Trim(lyrDestino' + i + '.innerHTML) != "&nbsp;"')) lIntCombo = i;
        }
    }
    
    mIntCombo = lIntCombo;
    
    // LIMPIAR CRITERIOS -->
    lStrGrupo = '';
    lStrFiltro = '';

    for (i = 1; i <= niv; i++){
        mArrGrupo[i] = '';
        mArrValores[i] = '';
        mArrFiltroCriterios[i] = 'XXX';
        mArrFiltroValores[i] = 0;
    }

    // Criterios Seleccionados

    j = 0;
    k = 0;

    for (var w = 1; w <= lIntCombo; w++){

        lObjCombo = eval('document.getElementById("select' + w + '")');        
        lStrCapaDestino = eval('document.getElementById("lyrDestino' + w + '").innerHTML');

        if (lStrCapaDestino.indexOf(lStrArea) != -1) {
            mArrGrupo[j] = lStrClaveArea;
            if (lObjCombo.options.length > 0){
                mArrValores[j] = lObjCombo.options[lObjCombo.selectedIndex].value;
            }
            j++;

            if (lObjCombo.selectedIndex >= 0){
                mArrFiltroCriterios[k] = lStrClaveArea;
                if (lObjCombo.options.length > 0){
                    mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                }
                k++;
            }
        }

        if (lStrCapaDestino.indexOf(lStrUnidad) != -1) {
            mArrGrupo[j] = lStrClaveUnidad;
            if (lObjCombo.options.length > 0){
                mArrValores[j] = lObjCombo.options[lObjCombo.selectedIndex].value;
            }
            j++;

            if (lObjCombo.selectedIndex >= 0){
                if (lObjCombo.selectedIndex >= 0){
                    mArrFiltroCriterios[k] = lStrClaveUnidad;
                if (lObjCombo.options.length > 0){
                    mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                }
                k++;
            }
            }
        }

        if (lStrCapaDestino.indexOf(lStrTipoProc) != -1) {
            mArrGrupo[j] = lStrClaveTipoProc;
            if (lObjCombo.options.length > 0){
                mArrValores[j] = lObjCombo.options[lObjCombo.selectedIndex].value;
            }
            j++;

            if (lObjCombo.selectedIndex >= 0){
                if (lObjCombo.selectedIndex >= 0){
                    mArrFiltroCriterios[k] = lStrClaveTipoProc;
                if (lObjCombo.options.length > 0){
                    mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                }
                k++;
            }
            }
        }

        if (lStrCapaDestino.indexOf(lStrProc) != -1) {
            mArrGrupo[j] = lStrClaveProc;
            if (lObjCombo.options.length > 0){
                mArrValores[j] = lObjCombo.options[lObjCombo.selectedIndex].value;
            }
            j++;

            if (lObjCombo.selectedIndex >= 0){
                if (lObjCombo.selectedIndex >= 0){
                    mArrFiltroCriterios[k] = lStrClaveProc;
                if (lObjCombo.options.length > 0){
                    mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                }
                k++;
            }
            }
        }

        if (lStrCapaDestino.indexOf(lStrTipoTram) != -1) {
            mArrGrupo[j] = lStrClaveTipoTram;
            if (lObjCombo.options.length > 0){
                mArrValores[j] = lObjCombo.options[lObjCombo.selectedIndex].value;
            }
            j++;

            if (lObjCombo.selectedIndex >= 0){
                if (lObjCombo.selectedIndex >= 0){
                    mArrFiltroCriterios[k] = lStrClaveTipoTram;
                if (lObjCombo.options.length > 0){
                    mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                }
                k++;
            }
            }
        }

        if (lStrCapaDestino.indexOf(lStrTramite) != -1) {
            mArrGrupo[j] = lStrClaveTramite;
            if (lObjCombo.options.length > 0){
                mArrValores[j] = lObjCombo.options[lObjCombo.selectedIndex].value;
            }
            j++;

            if (lObjCombo.selectedIndex >= 0){
                mArrFiltroCriterios[k] = lStrClaveTramite;
                if (lObjCombo.options.length > 0){
                    if (preenvio) {
                        if (lObjCombo.options[lObjCombo.selectedIndex].value==-1) {
                            mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                        } else {
                            mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                        }
                    } else {
                        mArrFiltroValores[k]   = lObjCombo.options[lObjCombo.selectedIndex].value;
                    }                    
                }
                k++;
            }
        }
    }
    
   

    // CADENA DE AGRUPACION -->

    for (i = 0; i < lIntCombo; i++) {
        if (mArrGrupo[i] != ''){
            lStrGrupo += mArrGrupo[i] + ',';
        }
    }
    lStrGrupo = Left(lStrGrupo, lStrGrupo.length-1)

    // Cadena de filtro -->
    //alert(mArrFiltroCriterios[0]);

    for (i = 0; i < niv; i++) {
        if (mArrFiltroCriterios[i] == 'XXX') mArrFiltroCriterios[i] = '';
    }

    for (i = 0; i < lIntCombo; i++)
    {
        if (mArrFiltroCriterios[i] != '')
        {
            lStrFiltro += mArrFiltroCriterios[i] + ',';
            lStrFiltro += mArrFiltroValores[i] + ',';
        }
    }
    lStrFiltro = Left(lStrFiltro, lStrFiltro.length-1);

}

function ObtenerDatosCombo(){
    //Debe fijarse preenvio a false para que al cargar combo coja el valor del campo para filtrar
    preenvio=false;
    CargarCombo();
    
    mIntCombo = mStrDestino;
    llamarAction();
}

function Enviar(){
    <%--Debe fijarse preenvio a true para que al cargar combo coja el texto del campo para generar el informe--%>
            
    if (comprueba_obligatorios_intervalo() == false)       
    {        
        jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjInterval"))%>');
    }
    else
    {
        preenvio =true;
        CargarCombo();

        var lStrURL = "<c:url value='/informes/Informes.do?opcion=cargar'/>";

        document.forms[1].opcion.value="cargar";
        document.forms[1].Grupo.value=lStrGrupo.toLowerCase();
        document.forms[1].Filtro.value=lStrFiltro.toLowerCase();
        document.forms[1].VerPend.value = document.frmEj.chkVerPend.checked;
        document.forms[1].VerFin.value = document.frmEj.chkVerFin.checked;
        document.forms[1].VerVol.value = document.frmEj.chkVerVol.checked;
        for (i=0;i<document.frmEj.chkModo.length;i++){
            if (document.frmEj.chkModo[i].checked) document.forms[1].TipoSalida.value=document.frmEj.chkModo[i].value;
        }    
        for (i=0;i<document.frmEj.chkTiempo.length;i++){
            if (document.frmEj.chkTiempo[i].checked)
            {
                if (document.frmEj.chkTiempo[i].value == "INT") //INTERVALO
                    {
                        document.forms[1].Tiempo.value= document.frmEj.fechaDesde.value +"||"+document.frmEj.fechaHasta.value;

                    }
                else
                    {
                        document.forms[1].Tiempo.value=document.frmEj.chkTiempo[i].value;            
                    }                        
            }
        }    
        if(document.forms[1].Grupo.value != "") {
            if(comprobarFiltroVolumenExp()) { 
                if(document.forms[1].TipoSalida.value != "HTM") {
                    document.forms[1].target = "new";
                    document.forms[1].action = lStrURL;
                    document.forms[1].submit();
                } else {

                    var source = "<c:url value='/informes/Informes.do?opcion=cargarHTM&nCS='/>"+document.forms[1].opcion.value+"&grupos="+
                            document.forms[1].Grupo.value+"&filtros="+document.forms[1].Filtro.value+"&verPend="+
                            document.forms[1].VerPend.value+"&verFin="+document.forms[1].VerFin.value+"&verVol="+
                            document.forms[1].VerVol.value+"&tipoSalida="+document.forms[1].TipoSalida.value+
                            "&tiempo="+document.forms[1].Tiempo.value;
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
                            'width=992,height=600,left=150,top=75,scrollbars=yes,status='+ '<%=statusBar%>',function(datosConsulta){
                                    if(datosConsulta!=undefined){

                                    }
                            });
                }
            } else {
                jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjFiltVolExpInc"))%>');
            }
        }
    }
}

function comprobarFiltroVolumenExp() { 
    if(document.forms[1].VerVol.value == "true") {
        var losGrupos = document.forms[1].Grupo.value;
        var vector = losGrupos.split(",");
        for(var m=0;m<vector.length;m++) {
            if(vector[m] == "cls" || vector[m] == "tra") {
                return false;
            }
        }
        return true;
    } else {
        return true;
    }
}


function llamarAction(){ 
    document.forms[0].combo.value =  mArrGrupo[mIntCombo-1];

    for (var i = 1; i < 7; i++) {
        eval('document.forms[0].criterio' + i + '.value = mArrFiltroCriterios[' + (i-1) + ']');
        eval('document.forms[0].valor' + i + '.value = mArrFiltroValores[' + (i-1) + ']');
    }

    document.forms[0].opcion.value = "cargar";
    document.forms[0].target = "oculto";
    document.forms[0].action="<c:url value='/sge/ConfInforme.do'/>";
    document.forms[0].submit();
    pleaseWait('on');
}

function ValidarDestino(pObjDestino){

   
    // No permitir arrastrar de destino a destino o de origen a origen -->
    if ((pObjDestino.id.indexOf('lyrDestino') >= 0) && (mStrOrigen.indexOf ('lyrDestino') >= 0)) {
        
        jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_CampoDestNoValido"))%>');
        return false;
    }
    
    if ((pObjDestino.id.indexOf('lyrOrigen') >= 0) && (mStrOrigen.indexOf ('lyrOrigen')) >= 0) {
        jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_CampoDestNoValido"))%>');
        return false;
    }
    
    // COMPROBACIONES AL ARRASTRAR DE ORIGEN A DESTINO -->
    if (mStrOrigen.indexOf('lyrOrigen') >= 0){
        var lIntIndiceDestino = pObjDestino.id.charAt(pObjDestino.id.length-1);
        // Comprobar si los criterios de grupo de página se rellenan sin dejar huecos -->
        if (lIntIndiceDestino > 1){
            if (lIntIndiceDestino != 6) {
                if (Trim(eval('lyrDestino' + (lIntIndiceDestino-1).toString() + '.innerHTML')) == '&nbsp;') {
                    jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_CampoDestNoValido"))%>');
                    return false;
                }
            }
        }
        // Comprobar que TRAMITE no está antes de PROCEDIMIENTO -->
        if (eval(mStrOrigen + '.innerHTML').indexOf(lStrTramite) > 0){
            var lBolEstaProc = false
            for (var i = lIntIndiceDestino; i >= 1; i--){
                if (eval('lyrDestino' + i + '.innerHTML.indexOf(lStrProc) >= 0')) lBolEstaProc = true;
            }
            if (!lBolEstaProc) {
                jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_TramPrevioProc"))%>');
                return false;
            }
        }
    }
    return true;
}

function Left (pStrCadena, pIntLongitud){
    return pStrCadena.substr(0, pIntLongitud);
}

function Right (pStrCadena, pIntLongitud){
    return pStrCadena.substr(pStrCadena.length - pIntLongitud, pStrCadena.length);
}

function Trim(Cadena){
    //
    // Trim()
    // Quita los espacios al inicio y al final de una cadena
    //

    var varI;
    var varRetorno;

    varRetorno = Cadena;

    while (varRetorno.charAt(0) == " ") {
        varRetorno = varRetorno.substr(1, varRetorno.length -1);
    }

    while (varRetorno.charAt(varRetorno.length -1) == " ") {
        varRetorno = varRetorno.substr(0, varRetorno.length -1);
    }

    return(varRetorno);
}


function devolverCriterio (pIntIdCriterio){
    // mover la imagen al origen -->

    // Buscar una casilla de la caja origen disponible -->

    var i = 0;
    do {
        i++;
        // alert(i + ": " + eval('window.lyrOrigen' + i + '.innerHTML'));
    }
    while ((i <= niv) && (eval('lyrOrigen' + i + '.innerHTML') != '&nbsp;'));

    // Mover la imagen a una casilla origen libre -->

    eval('lyrOrigen' + i + '.innerHTML = lyrDestino' + pIntIdCriterio + '.innerHTML');
    eval('lyrDestino' + pIntIdCriterio + ".innerHTML = '&nbsp;'");

    // ocultar el combo -->

    eval('lyrCboDestino' + pIntIdCriterio + ".style.visibility = 'hidden'");

    lObjCombo = eval('frmEj.select' + pIntIdCriterio);
    lObjCombo.selectedIndex = 0;
}

function mover(event,pObjOrigen){

    event= event || window.event;

    srcObj = pObjOrigen;
    mStrOrigen = pObjOrigen.id;

    if (pObjOrigen.id.indexOf('lyrOrigen') >= 0){
        // De origen a destino -->
        for (var i = 1; i <= niv; i++){
            if (eval('lyrCboDestino' + i + ".style.visibility == 'hidden'")){
                eval('drop(event,lyrDestino' + i + ')')
                return false;
            }
        }
    } else {
        // De destino a origen -->
        var lStrIndice = mStrOrigen.charAt(mStrOrigen.length-1);
        if (mStrOrigen.indexOf('lyrDestino') >= 0){
            for (var i = lStrIndice; i <= niv; i++){
                if (eval('Trim(lyrDestino' + i + '.innerHTML) != "&nbsp;"')){
                    devolverCriterio (i);
                }
            }
        }
    }
}

function drop(event,pObjDestino) {

    event= event || window.event;

    var lStrDestino = pObjDestino.id;
   
    mStrDestino = Right(pObjDestino.id, 1);
    event.returnValue = false;

    if (!ValidarDestino(pObjDestino)){
       
        return false;
    } 

    if (Trim(pObjDestino.innerHTML) == '&nbsp;'){
        pObjDestino.innerHTML = eval('' + mStrOrigen + '.innerHTML');
        eval('' + mStrOrigen + ".innerHTML = '&nbsp;'");
        var lStrEstado = (pObjDestino.id == 'lyrOrigen' + pObjDestino.id.charAt(pObjDestino.id.length-1))? 'hidden' : 'visible';
        var lIntIndiceCombo = (Left(lStrDestino, 9) != 'lyrOrigen')? lStrDestino.charAt(lStrDestino.length-1): mStrOrigen.charAt(mStrOrigen.length-1);
        eval('lyrCboDestino' + lIntIndiceCombo + ".style.visibility = '" +  lStrEstado + "'");
    } else {
        jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msj_CampoDestNoValido"))%>');
    }

    // Si se está retirando el criterio i y está seleccionado
    // también el criterio i+j para algún j > 0
    // ocultar también esos criterios
    if (mStrOrigen.indexOf('lyrDestino') >= 0){
        for (var i = mStrOrigen.charAt(mStrOrigen.length-1); i <= niv; i++){
            if (eval('Trim(lyrDestino' + i + '.innerHTML) != "&nbsp;"')){
                devolverCriterio (i);
            }
        }
    }
}

function addAttribute(oObj, sVal) {
    var loc = oObj.indexOf(">");
    return oObj.substring(0, loc) + ' ' + sVal + '>';
}

function Cancelar(){
    document.forms[0].target = "mainFrame";
    document.forms[0].action="<c:url value='/jsp/sge/mainConfInforme.jsp'/>";
    document.forms[0].submit();
}

function inicio() {
    window.focus();     
    <%if (mostrarTodosNiveles){%>
            niv=6;           
    <%}%>
}

</script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

</head>

<style type="text/css">

    .label3d {
        background-color: #DCDCCC;
        border: 2px outset;
    }

</style>

<body class="bandaBody" onload="javascript:{pleaseWait('off');   inicio();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form name = "frmEj" action = "">

<input type="hidden" name="opcion" value="">
<input type="hidden" name="combo" value="">

<input type="hidden" name="criterio1" value="">
<input type="hidden" name="criterio2" value="">
<input type="hidden" name="criterio3" value="">
<input type="hidden" name="criterio4" value="">
<input type="hidden" name="criterio5" value="">
<input type="hidden" name="criterio6" value="">

<input type="hidden" name="valor1" value="">
<input type="hidden" name="valor2" value="">
<input type="hidden" name="valor3" value="">
<input type="hidden" name="valor4" value="">
<input type="hidden" name="valor5" value="">
<input type="hidden" name="valor6" value="">

<div class="txttitblanco"><%=descriptor.getDescripcion("etiqInfGest")%></div>
<div class="contenidoPantalla">
    <table class="contenidoPestanha" style="padding-top: 5px; padding-bottom: 5px">
        <tr>
            <td style="width: 70%" align="center" valign="top" class="cuadroFondoBlanco">
                <table cellspacing="2" style="width: 100%">
                    <tr>
                        <td class="sub3titulo" style="width: 100%" colspan="2">&nbsp;<%=descriptor.getDescripcion("gCampos_Dispon")%></td>
                    </tr>
                    <tr >
                        <td style="height: 15px"></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table id="elementosInformeGestion" style="width: 100%">
                                <tr align="center" valign="middle">
                                    <td style="width: 33%; height: 30px" id = "Origen1"> 
                                        <div id = "Origen1">
                                        <div id="lyrOrigen1" style="width:100%; height:20px"  onDblClick="mover(event,this); ObtenerDatosCombo()">
                                            <span name = "imgCampo1" class="arrastrable">Área</span>
                                        </div>
                                        </div>
                                    </td>
                                    <td style="width: 34%; height: 30px" id = "Origen2"> 
                                        <div id="lyrOrigen2" style="width:100%; height: 20px"  onDblClick="mover(event,this); ObtenerDatosCombo()" >
                                            <span name = "imgCampo2" class="arrastrable">Unidad</span>                                            
                                        </div>
                                    </td>
                                    <td style="width: 33%; height: 30px" id = "Origen3"> 
                                        <div id="lyrOrigen3" style="width:100%; height: 20px" onDblClick="mover(event,this); ObtenerDatosCombo()" >
                                            <span name = "imgCampo3" class="arrastrable">Tipo procedimiento</span>                                                                                        
                                        </div>
                                    </td>
                                </tr>
                                <tr align="center" valign="middle">
                                    <td style="height: 30px" id = "Origen4"> 
                                        <div id="lyrOrigen4" style="width:100%; height: 20px" onDblClick="mover(event,this); ObtenerDatosCombo()">
                                            <span name = "imgCampo4" class="arrastrable">Procedimiento</span>                                            
                                        </div>
                                    </td>
                                    <td id = "Origen5"> 
                                        <div id="lyrOrigen5" style="width:100%; height: 20px" onDblClick="mover(event,this); ObtenerDatosCombo()" >
                                            <span name = "imgCampo5" class="arrastrable">Clasificación trámite</span>                                                                                        
                                        </div>
                                    </td>
                                    <td id = "Origen6"> 
                                        <div id="lyrOrigen6" style="width:100%; height: 20px" onDblClick="mover(event,this);ObtenerDatosCombo();"  >
                                            <span name = "imgCampo6" class="arrastrable">Trámite</span>                                                                                                                                    
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr >
                        <td style="height: 35px"></td>
                    </tr>
                    <tr >
                        <td class="sub3titulo camposInformeGestion"><%=descriptor.getDescripcion("etiqAgrup")%></td>
                        <td class="sub3titulo"><%=descriptor.getDescripcion("gEtiq_filtro")%></td>
                    </tr>
                    <tr>
                        <td style="height: 8px"></td>
                    </tr>
                    <tr>
                        <td style="width:40%"> 
                            <table id="seleccionInformeGestion" height="30" style="width: 100%">
                                <tr valign="middle">
                                    <td id = "celdaDestino1" align="center">
                                        <div id="lyrDestino1" class="capaCombo"  ondragend="finDrag(event,this)"  onDblClick="mover(event,this)"  >&nbsp;
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td> 
                            <div id="lyrCboDestino1" style="width:100%; visibility: hidden">
                                <select id="select1" name="select1" class="inputTexto" style="width: 100%; height: 20px;font-size: 9px;" onChange="Change(1)">
                                </select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="height: 5px"></td>
                    </tr>
                    <tr>
                        <td> 
                            <table height="30" style="width: 100%">
                                <tr valign="middle">
                                    <td id = "celdaDestino2" align="center" >
                                        <div id="lyrDestino2" ondragend="finDrag(event,this)" onDblClick="mover(event,this)" class="capaCombo">&nbsp; </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td> 
                            <div id="lyrCboDestino2" style="width:100%; visibility: hidden">
                                <select id="select2" name="select2" class="inputTexto" style="width: 100%; height: 20px;font-size: 9px;" onChange="Change(2)">
                                </select>
                            </div>
                        </td>
                    </tr>
                    <%if(mostrarTodosNiveles){%>
                    <td style="height: 5px"></td>
                    </tr>
                    <tr>
                        <td> 
                            <table height="30" style="width: 100%">
                                <tr valign="middle">
                                    <td id = "celdaDestino3" align="center" >
                                        <div id="lyrDestino3" ondragend="finDrag(event,this)" onDblClick="mover(event,this)" class="capaCombo">&nbsp; </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td> 
                            <div id="lyrCboDestino3" style="width:100%; visibility: hidden">
                                <select id="select3" name="select3" class="inputTexto" style="width: 100%; height: 20px;font-size: 9px;" onChange="Change(3)">
                                </select>
                            </div>
                        </td>
                    </tr>
                     <td style="height: 5px"></td>
                    </tr>
                    <tr>
                        <td> 
                            <table height="30" style="width: 100%">
                                <tr valign="middle">
                                    <td id = "celdaDestino4" align="center" >
                                        <div id="lyrDestino4" ondragend="finDrag(event,this)" onDblClick="mover(event,this)" class="capaCombo">&nbsp; </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td> 
                            <div id="lyrCboDestino4" style="width:100%; visibility: hidden">
                                <select id="select4" name="select4" class="inputTexto" style="width: 100%; height: 20px;font-size: 9px;" onChange="Change(4)">
                                </select>
                            </div>
                        </td>
                    </tr>

                     <td style="height: 5px"></td>
                    </tr>
                    <tr>
                        <td> 
                            <table height="30" style="width: 100%">
                                <tr valign="middle">
                                    <td id = "celdaDestino5" align="center" >
                                        <div id="lyrDestino5" ondragend="finDrag(event,this)" onDblClick="mover(event,this)" class="capaCombo">&nbsp; </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td> 
                            <div id="lyrCboDestino5" style="width:100%; visibility: hidden">
                                <select id="select5" name="select5" class="inputTexto" style="width: 100%; height: 20px;font-size: 9px;" onChange="Change(5)">
                                </select>
                            </div>
                        </td>
                    </tr>

                     <td style="height: 5px"></td>
                    </tr>
                    <tr>
                        <td> 
                            <table height="30" style="width: 100%">
                                <tr valign="middle">
                                    <td id = "celdaDestino6" align="center" >
                                        <div id="lyrDestino6" ondragend="finDrag(event,this)" onDblClick="mover(event,this)" class="capaCombo">&nbsp; </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td> 
                            <div id="lyrCboDestino6" style="width:100%; visibility: hidden">
                                <select id="select6" name="select6" class="inputTexto" style="width: 100%; height: 20px;font-size: 9px;" onChange="Change(6)">
                                </select>
                            </div>
                        </td>
                    </tr>
                    <%}%>

                </table>
            </td>
            <td style="width:30%" valign="top">
                <!-- inciso -->
                <table class="cuadroFondoBlanco" style="margin-bottom:10px; margin-left:10px">
                    <tr>
                        <td class="sub3titulo" colspan="2" align="center" ><%=descriptor.getDescripcion("etiqVisualiz")%></td>
                    </tr>
                    <tr >
                        <td class="columnP" > <input name="chkVerPend" type="checkbox" id="chkVerPend" value="checkbox" checked>
                        <%=descriptor.getDescripcion("etiqTarPend")%></td>
                    </tr>
                    <tr >
                        <td class="columnP"> <input name="chkVerFin" id="chkVerFin" type="checkbox" value="checkbox" checked>
                        <%=descriptor.getDescripcion("etiqTarRealiz")%></td>
                    </tr>
                    <tr >
                        <td class="columnP" > <input type="checkbox" id="chkVerVol" name="chkVerVol" value="checkbox">
                        <%=descriptor.getDescripcion("etiqVolExp")%></td>
                    </tr>
                </table>
                <table class="cuadroFondoBlanco" style="margin-bottom:10px; margin-left:10px">
                    <tr><td class="sub3titulo" colspan="2" align="center"><%=descriptor.getDescripcion("etiqModVisualiz")%></td></tr>
                    <tr >
                        <td class="columnP"><input name="chkModo" type="radio" value="HTM" checked>HTML</input></td>
                    </tr>
                    <tr >
                        <td class="columnP"><input type="radio" name="chkModo" value="XLS">Excel</input></td>
                    </tr>
                    <tr >
                        <td class="columnP"><input type="radio" name="chkModo" value="PDF">PDF</input></td>
                    </tr>
                </table>
                <table class="cuadroFondoBlanco" style="margin-left:10px">
                    <tr><td class="sub3titulo" colspan="2" align="center"><%=descriptor.getDescripcion("etiqTiempMed")%></td></tr>
                    <tr >
                        <td class="columnP" ><input type="radio" name="chkTiempo" value="SEM" onclick="ocultarIntervalo();"><%=descriptor.getDescripcion("etiqUltSem")%></input></td>
                    </tr>
                    <tr >
                        <td class="columnP" ><input name="chkTiempo" type="radio" value="MES" onclick="ocultarIntervalo();"><%=descriptor.getDescripcion("etiqUltMes")%></input></td>
                    </tr>
                    <tr >
                        <td class="columnP" ><input type="radio" name="chkTiempo" value="TRI" onclick="ocultarIntervalo();"><%=descriptor.getDescripcion("etiqUltTrim")%></input></td>
                    </tr>
                    <tr >
                        <td class="columnP" ><input name="chkTiempo" type="radio" value="ALL" checked onclick="ocultarIntervalo();"><%=descriptor.getDescripcion("gbTodos")%></input></td>
                    </tr> 
                    <tr >
                        <td class="columnP" ><input name="chkTiempo" type="radio" value="INT" onclick="mostrarIntervalo();">Intervalo</input></td>
                    </tr>
                </table>
                <div id = "CapaIntervalo" style=" visibility: hidden">
                <table>                                                                                            
                    <tr >
                        <td width="30px"></td>
                        <td class="columnP">Desde</td>
                        <td>
                            <input class="inputTxtFechaObligatorio" type="text" size="11" maxlength="10" id ="fechaDesde" name="fechaDesde" onkeypress = "javascript: return soloCaracteresFecha(event);"onblur = "javascript:return comprobarFecha(this);">                                                
                           <A href="javascript:calClick();return false;" onClick="mostrarCalDesde(event);return false;"onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                                <span class="fa fa-calendar" aria-hidden="true" name="calDesde" id="calDesde"></span>
                           </a>
                        </td>  
                    </tr>
                    <tr>
                        <td width="30px"></td>
                        <td class="columnP">Hasta</td>
                        <td>
                            <input class="inputTxtFechaObligatorio" type="text" size="11" maxlength="10" id ="fechaHasta" name="fechaHasta" onkeypress = "javascript: return soloCaracteresFecha(event);"onblur = "javascript:return comprobarFecha(this);">                                                
                           <A href="javascript:calClick();return false;" onClick="mostrarCalHasta(event);return false;"onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                                <span class="fa fa-calendar" aria-hidden="true" name="calHasta" id="calHasta"></span>
                           </a>
                        </td>                                                
                    </tr>        
                </table>
                </div>
                <!-- inciso -->
                <!-- ********************************************************************-->
            </td>
        </tr>                                                               
    </table>
    <div id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="Submit" onClick="Enviar()">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" onclick="Cancelar()">
    </div>
</div>
</form>
<form name = "frmEj2" action = "">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="Grupo" value="">
    <input type="hidden" name="Filtro" value="">
    <input type="hidden" name="VerPend" value="">
    <input type="hidden" name="VerFin" value="">
    <input type="hidden" name="VerVol" value="">
    <input type="hidden" name="TipoSalida" value="">
    <input type="hidden" name="Tiempo" value="">
</form>
<SCRIPT type="text/javascript">
function comprobarFecha(inputFecha) {
   
  var formato = 'dd/mm/yyyy';
  if (Trim(inputFecha.value)!='') {    
      var D = ValidarFechaConFormato(inputFecha.value,formato);   
      if (!D[0]){
        jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
        if (navigator.appName.indexOf("Explorer") == -1) {
            inputFecha.value='';
         }
        inputFecha.focus();
        return false;
      } else {
        inputFecha.value = D[1];
        return true;
      }    
  }
  return true;
}
function soloCaracteresFecha(e) {                
    PasaAMayusculas(e);    
    var tecla, caracter;
    var alfanumericos = '0123456789/-';
    tecla = e.keyCode;
   	if (tecla == null) return true;
    caracter = String.fromCharCode(tecla);        
    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
		return true;
    return false;   
}
function mostrarCalDesde(evento) 
{
            if(window.event) evento = window.event;
            if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1) {
                showCalendar('forms[0]', 'fechaDesde', null, null, null, '', 'calDesde', '',null,null,null,null,null,null,null,null,evento);
            }
}
function mostrarCalHasta(evento) 
{
            if(window.event) evento = window.event;
            if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1) {
                showCalendar('forms[0]', 'fechaHasta', null, null, null, '', 'calHasta', '',null,null,null,null,null,null,null,null,evento);
            }
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
function mostrarIntervalo(){    
    document.getElementById('CapaIntervalo').style.visibility='visible';  
}
function ocultarIntervalo(){        
    document.getElementById('CapaIntervalo').style.visibility='hidden'
}
function comprueba_obligatorios_intervalo(){
    var i = 0;    
    for (i=0;i<document.frmEj.chkTiempo.length;i++)
    {
        if (document.frmEj.chkTiempo[i].checked)
        {              
            if ((document.frmEj.chkTiempo[i].value == "INT") && ((document.frmEj.fechaDesde.value == "") || (document.frmEj.fechaHasta.value == "")))
                {              
                    return false;
                }
            else
                {                 
                    return true;
                }
        }
    }    
}
</script>    
 
</body>
</html>
