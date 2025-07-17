<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.ConsultaExpedientePorCampoSupForm"%>
<%@ page import="es.altia.agora.technical.EstructuraCampo"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO"%>
<%@ page import="java.util.ArrayList"%>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES  Consulta de Expedientes Por Datos Suplementarios:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<%int idioma=1;
    int apl=1;
    int munic = 0;
    String soloConsulta = "no";
    if (session!=null){
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
            munic = usuario.getOrgCod();
            soloConsulta = usuario.getSoloConsultarExp();
        }
    }
    String municipio = Integer.toString(munic);
    String aplicacion = Integer.toString(apl);

    Config m_Config = ConfigServiceHelper.getConfig("common");

    Log _log = LogFactory.getLog(this.getClass());
    Vector estructuraDatosSuplementarios = null;
    Vector estructuraDatosSuplementariosTramites = null;
    Vector valoresDatosSuplementarios = null;
    
    
    ConsultaExpedientePorCampoSupForm expForm = (ConsultaExpedientePorCampoSupForm)session.getAttribute("ConsultaExpedientePorCampoSupForm");
    estructuraDatosSuplementarios = (Vector) expForm.getEstructuraDatosSuplementarios();
    estructuraDatosSuplementariosTramites = (Vector) expForm.getEstructuraDatosSuplementariosTramites();
    ////Mai
    //Necesito o array de módulos externos
    ArrayList<ModuloIntegracionExterno> modulosExternos= expForm.getEstructuraModulosExtensionCamposConsulta();
    int numeroModulosExternos=0;
    if (modulosExternos!=null){
     numeroModulosExternos=modulosExternos.size();
       }
    ////Mai
    int lengthEstructuraDatosSuplementarios = 0;
    int lengthEstructuraDatosSuplementariosTramites = 0;
    if (estructuraDatosSuplementarios != null) {
        lengthEstructuraDatosSuplementarios = estructuraDatosSuplementarios.size();
        if (lengthEstructuraDatosSuplementarios>0 ) {
            for (int i=0;i<lengthEstructuraDatosSuplementarios;i++)  {
                _log.debug(i+":"+estructuraDatosSuplementarios.get(i));
            }
        }
    }
    

    if (estructuraDatosSuplementariosTramites != null) {
        lengthEstructuraDatosSuplementariosTramites = estructuraDatosSuplementariosTramites.size();
    }
    String carga = request.getParameter("carga");

    String userAgent = request.getHeader("user-agent");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<SCRIPT type="text/javascript">
var cod_procedimiento = new Array();
var desc_procedimiento = new Array();
var cod_clasifTramite = new Array();
var desc_clasifTramite = new Array();
var nombreCombos = new Array();
var procedimientoActual = 1;
var procedimientoInf = 1;
var procedimientoSup = 10;
var consultando = false;
var operadorConsulta = '|&:<>!=';
function inicializar() {
    habilitarImagenCal("calFechaInicio",true);
    habilitarImagenCal("calFechaFin",true);
    mostrarCapasBotones('capaBotones1');
    consultando = true;
<logic:iterate id="elemento" name="ConsultaExpedientePorCampoSupForm" property="listaProcedimientos">
    cod_procedimiento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_procedimiento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
</logic:iterate>
    borrarDatos();
    cargarCombos();
<% if ((carga == null) || ((carga != null) && (!carga.equalsIgnoreCase("inicio")))) {
if (expForm.getCodigoProcedimiento() != null) { %>
    document.forms[0].codigoProcedimiento.value = "<%=expForm.getCodigoProcedimiento()%>";
    document.forms[0].descProcedimiento.value = "<%=expForm.getDescProcedimiento()%>";
<% } %>
<% } %>
}
function mostrarCalFechaInicio(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calFechaInicio").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaInicio',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
}
function mostrarCalFechaFin(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calFechaFin").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaFin',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
}
function pulsarSalir() {
    if(document.forms[0].expRelacionado.value == "si" || document.forms[0].deAdjuntar.value == "si") {
        self.close();
    } else {
        document.forms[0].opcion.value="inicio";
        document.forms[0].target="mainFrame";
    <% if(soloConsulta.equals("no")) { %>
        document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
    <% } else { %>
        document.forms[0].action="<c:url value='/sge/ConsultaExpedientePorCampoSup.do'/>";
    <% } %>
        document.forms[0].submit();
    }
}
function desactivarFormulario() {
    deshabilitarDatos(document.forms[0]);
    comboProcedimiento.deactivate();
    comboClasifTramite.deactivate();
    var vectorBotones = new Array(document.forms[0].cmdCancelarAlta,document.forms[0].cmdGrabarAlta,document.forms[0].cmdCancelarModificar,
            document.forms[0].cmdGrabarModificar,document.forms[0].cmdCancelarBuscar,document.forms[0].cmdEliminar,
            document.forms[0].cmdSalir,document.forms[0].cmdModificar,document.forms[0].cmdConsultar,
            document.forms[0].cmdAlta,document.forms[0].cmdListado,document.forms[0].cmdDescargar,document.forms[0].cmdCancelarImportar);
    habilitarGeneral(vectorBotones);
}
function activarFormulario() {
    habilitarDatos(document.forms[0]);
    comboProcedimiento.activate();
    comboClasifTramite.activate();
    var vectorBotones = new Array(document.forms[0].cmdCancelarAlta,document.forms[0].cmdGrabarAlta,document.forms[0].cmdCancelarModificar,
            document.forms[0].cmdGrabarModificar,document.forms[0].cmdCancelarBuscar,document.forms[0].cmdEliminar,
            document.forms[0].cmdSalir,document.forms[0].cmdModificar,document.forms[0].cmdConsultar,
            document.forms[0].cmdAlta,document.forms[0].cmdListado,document.forms[0].cmdDescargar,document.forms[0].cmdCancelarImportar);
    habilitarGeneral(vectorBotones);
}
function comprobarData(inputFecha) {
    if (Trim(inputFecha.value)!='') {
        if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
            if (navigator.appName.indexOf("Explorer") == -1) {
                if((document.getElementById(inputFecha.name).name!='fechaFin')&&(document.getElementById(inputFecha.name).name!='fechaInicio'))
                    document.getElementById(inputFecha.name).value='';
            }
            return false;
        }
    }
    return true;
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
                document.getElementById(inputFecha.name).focus();
                document.getElementById(inputFecha.name).select();
                if (navigator.appName.indexOf("Explorer") == -1) {
                if((document.getElementById(inputFecha.name).name!='fechaFin')&&(document.getElementById(inputFecha.name).name!='fechaInicio'))
                    document.getElementById(inputFecha.name).value='';
                }
                return false;
            } else {
                inputFecha.value = fechaFormateada;
                return true;
            }
        } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
            var D = ValidarFechaConFormato(inputFecha.value,formato);
            if (!D[0]){
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                document.getElementById(inputFecha.name).focus();
                document.getElementById(inputFecha.name).select();
               if (navigator.appName.indexOf("Explorer") == -1) {
                    if((document.getElementById(inputFecha.name).name!='fechaFin')&&(document.getElementById(inputFecha.name).name!='fechaInicio')) document.getElementById(inputFecha.name).value='';
                }
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
function contieneOperadoresConsulta(campo,cjtoOp){
    var contiene=false;
    var v = campo.value;
    for (i = 0; i < v.length; i++){
        var c = v.charAt(i);
        if (cjtoOp.indexOf(c) != -1)  contiene=true;
    }
    return contiene;
}

    function pulsarConsultar() {

        var valido=true;

        if (!comprobarFecha(document.forms[0].fechaInicio))valido=false;//comprobamos la fecha inicio
        else if (!comprobarFecha(document.forms[0].fechaFin))valido=false;//comprobamos la fecha Fin

        if(valido){// si las fechas son correctas
            if(document.forms[0].fechaFin.value!="" && document.forms[0].fechaInicio.value!=""){//si se introducen las 2 fechas
                if( validarFechaAnterior(document.forms[0].fechaInicio.value,document.forms[0].fechaFin.value)){//validamos que la fecha fin no sea mayor a la de inicio


                    if (validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                        if((document.forms[0].codigoProcedimiento.value) != ""){
                            pleaseWait('on');
                            document.forms[0].opcion.value="consultarListado";
                            document.forms[0].target="mainFrame";
                            document.forms[0].action="<c:url value='/sge/ConsultaExpedientePorCampoSup.do'/>";
                            document.forms[0].submit();
                        }
                    }
                }
            }else{
                if (validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    if((document.forms[0].codigoProcedimiento.value) != ""){
                        pleaseWait('on');
                        document.forms[0].opcion.value="consultarListado";
                        document.forms[0].target="mainFrame";
                        document.forms[0].action="<c:url value='/sge/ConsultaExpedientePorCampoSup.do'/>";
                        document.forms[0].submit();
                    }
                }
            }
        }
    }

function consultaCamposProcedimiento() {
    pleaseWait('on');
    document.forms[0].opcion.value="consultarCampos";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/ConsultaExpedientePorCampoSup.do'/>";
    document.forms[0].submit();
}

function borrarDatos() {
    document.forms[0].numeroExpediente.value = "";
    comboProcedimiento.selectItem("-1");
    document.forms[0].descProcedimiento.value="";
    document.forms[0].fechaInicio.value = "";
    document.forms[0].fechaFin.value = "";
    document.forms[0].estado[3].checked = true;
    document.forms[0].titular.value = "";
    document.forms[0].tercero.value="";
    document.forms[0].versionTercero.value="";
}
function cargarCombos() {
    comboProcedimiento.addItems(cod_procedimiento,desc_procedimiento);
}

function pulsarVerExterno(nombre){  
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;        
    var valor = eval("document.forms[0]."+nombre+".value");    
    
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+"<html:rewrite page='/jsp/sge/mostrarExterno.jsp'/>?codMunicipio=" + escape(codMunicipio) + 
                "&codProcedimiento='" + escape(codProcedimiento) + "'&nombreCampo='" + nombre+"'&valor_dato='"+valor+
                "'&opcion_pr_tra='procedimiento'",'',
                'width=700,height=450,scrollbars=no',function(result){
                    if (result != null){            
                        eval("document.forms[0]."+nombre+".value='"+result+"'");
                    }        
                });
}
</SCRIPT>
</head>
<BODY  class="bandaBody" onload="javascript:{ pleaseWait('off'); 
inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include> 
<html:form action="/sge/ConsultaExpedientePorCampoSup.do">
<html:hidden property="opcion" value=""/>
<html:hidden property="tercero" />
<html:hidden property="versionTercero" />
<html:hidden property="codMunicipio" value="<%=municipio%>" />
<input type="hidden" name="expRelacionado" >
<input type="hidden" name="codMunExpIni" value="">
<input type="hidden" name="ejercicioExpIni" value="">
<input type="hidden" name="numeroExpIni" value="">
<input type="hidden" name="deAdjuntar" >
<input type="hidden" name="codUnidadTram" value ="">
<input type="hidden" name="codTramite" value ="">
<input type="hidden" name="codTipoProced" value ="">
<input type="hidden" name="codArea" value ="">
<input type="hidden" name="tipoDocumentoTercero" value ="">
<input type="hidden" name="documentoTercero" value ="">
    
<div class="txttitblanco"><%=descriptor.getDescripcion("tit_consExp")%></div>
<div class="contenidoPantalla">						
    <table style="width: 100%">
        <TR>
            <td colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("msjDatosBasBusq")%>:</td>
        </TR>
        <tr>
            <td class="etiqueta" style="width:11%"><%=descriptor.getDescripcion("gEtiqProc")%>:</td>
            <td class="columnP">
                <html:text styleId="codProcedimiento" property="codigoProcedimiento"
                           onkeyup="return xAMayusculas(this);"
                           styleClass="inputTextoObligatorio" style="width:8%"/>
                <html:text styleId="descProcedimiento" property="descProcedimiento"
                           styleClass="inputTextoObligatorio"  readonly="true" style="width:70%"/>
                <A href="" id="anchorProcedimiento" name="anchorProcedimiento" style="text-decoration:none;" >
                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonProcedimiento" name="botonProcedimiento"></span></A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta" style="width:10%"><%=descriptor.getDescripcion("etiq_numExp")%>:</td>
            <td>
                <span class="columnP">
                    <html:text styleClass="inputTexto" property="numeroExpediente" style="width:20%" maxlength="30"
                               onblur="return xAMayusculas(this);"/>
                </span>
                <span style="margin-left:2%" class="etiqueta"><%=descriptor.getDescripcion("etiqFechIni")%>:</span>
                <span class="columnP">
                    <html:text styleClass="inputTxtFecha" size="7" maxlength="25" property="fechaInicio" styleId="fechaInicio"
                               onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                               onblur = "javascript:return comprobarFecha(this);" onfocus = "this.select();"/>
                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                        <span class="fa fa-calendar" aria-hidden="true" id="calFechaInicio" name="calFechaInicio" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' ></span>
                    </A>
                </span>
                <span style="margin-left:2%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fecFin")%>:</span>
                <span class="columnP">
                    <html:text styleClass="inputTxtFecha" size="7" maxlength="25" property="fechaFin" styleId="fechaFin" 
                               onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                               onblur = "javascript:return comprobarFecha(this);" onfocus = "this.select();"/>
                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                        <span class="fa fa-calendar" aria-hidden="true" id="calFechaFin" name="calFechaFin" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' ></span>
                    </A>
                </span>
            </td>
        </tr>
        <tr>
            <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_estado")%>:</TD>
            <td class="columnP">
                <input type="radio" name="estado" class="textoSuelto" value="abierto"><%=descriptor.getDescripcion("gbPendientes")%>
                <input style="margin-left: 20px" type="radio" name="estado" class="textoSuelto" value="cerrado"><%=descriptor.getDescripcion("gbFinalizados")%>
                <input style="margin-left: 20px" type="radio" name="estado" class="textoSuelto" value="anulado"><%=descriptor.getDescripcion("gbAnulados")%>
                <input style="margin-left: 20px" type="radio" name="estado" class="textoSuelto" value="sinEstado" CHECKED><%=descriptor.getDescripcion("gbTodos")%>&nbsp;
            </td>
        </tr>
       <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_titular")%>:</td>
            <td class="columnP">
                <html:text property="titular" styleClass="inputTexto" readonly="true" style="width:85%"/>&nbsp;
                <span class="fa fa-search" aria-hidden="true"  id="botonTerceros" name="botonTerceros" onclick="pulsarBuscarTerceros();"></span>
            </td>
        </tr>
         <tr>
            <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_Inicio")%>:</TD>
            <td class="columnP">
                <input type="radio" name="TipoInicio" class="textoSuelto" value="Instancia"><%=descriptor.getDescripcion("gbDeInstancia")%>
                <input style="margin-left: 20px" type="radio" name="TipoInicio" class="textoSuelto" value="Oficio"><%=descriptor.getDescripcion("gbDeOficio")%>
                <input style="margin-left: 20px" type="radio" name="TipoInicio" class="textoSuelto" value="Todos" CHECKED><%=descriptor.getDescripcion("gbTodos")%>&nbsp;
            </td>
        </tr>
        <TR>
            <td colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("msjDatosSupBusq")%>:</td>
        </TR>
        <tr>
            <td colspan="2">
                <div style=" height:100%; overflow:auto;">
                    <table width="100%">
                        <%if ((carga == null) || ((carga != null) && (!carga.equalsIgnoreCase("inicio")))) {
                            for (int k = 0; k < lengthEstructuraDatosSuplementarios; k++) {
                                EstructuraCampo campo = (EstructuraCampo) estructuraDatosSuplementarios.get(k);
                                if ((campo != null) && (!campo.getCodTipoDato().equals("4")) && (!campo.getCodTipoDato().equals("5"))) {
                                    request.setAttribute("CAMPO_BEAN", estructuraDatosSuplementarios.get(k));
                        %>
                        <jsp:include page="/jsp/plantillas/CampoVistaConsulta.jsp" flush="true" />
                        <%                                          
                                }
                            }
                            for (int k = 0; k < lengthEstructuraDatosSuplementariosTramites; k++) {
                                EstructuraCampo campo = (EstructuraCampo) estructuraDatosSuplementariosTramites.get(k);
                                if ((campo != null) && (!campo.getCodTipoDato().equals("4")) && (!campo.getCodTipoDato().equals("5"))) {
                                    request.setAttribute("CAMPO_BEAN", estructuraDatosSuplementariosTramites.get(k));
                        %>                                              
                        <jsp:include page="/jsp/plantillas/CampoVistaConsulta.jsp" flush="true" />
                        <%                                          
                                }
                            }   

                            if (numeroModulosExternos>0){

                                for (int k = 0; k <numeroModulosExternos; k++) {

                                   ModuloIntegracionExterno modulo=modulosExternos.get(k);
                                   String nombreModulo=modulo.getDescripcionModulo();
                                   ArrayList<EstructuraCampoModuloIntegracionVO> camposConsultaModuloExterno=modulo.getCamposConsulta();
                                   int lonxitudeCamposConsultaModuloExterno=0;
                                   if(camposConsultaModuloExterno!=null){
                                        lonxitudeCamposConsultaModuloExterno=camposConsultaModuloExterno.size();
                                    }
                          %>
                                     <TR>
                                         <td class="sub3titulo"  colspan="2"><%=nombreModulo%>:</td>
                                     </TR>  

                                 <%  
                                     for (int w = 0; w<lonxitudeCamposConsultaModuloExterno; w++) {
                                        EstructuraCampoModuloIntegracionVO campo= (EstructuraCampoModuloIntegracionVO) camposConsultaModuloExterno.get(w);
                                        if (campo != null){
                                             request.setAttribute("CAMPO_BEAN", camposConsultaModuloExterno.get(w));
                                 %>                                              
                                 <jsp:include page="/jsp/plantillas/moduloExtension/CampoVistaConsultaME.jsp" flush="true" />
                                 <%                                          
                                          }
                                     }
                                }
                           }
                            session.removeAttribute("tramiteCodigo");
                        }
                        %>                                                   
                    </table>
                </div>
            </td>
        </tr>
    </table>
      </div>
    <div class="capaFooter">
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbConsultar")%> name="cmdConsultar"  onclick="pulsarConsultar();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onclick="pulsarSalir();">
    </DIV>   
</DIV>   

</html:form>

<%if ((carga == null) || ((carga != null) && (!carga.equalsIgnoreCase("inicio")))) {
    Vector estructuraDatosSuplementariosAux = null;
    estructuraDatosSuplementariosAux = (Vector) expForm.getEstructuraDatosSuplementarios();
    if (estructuraDatosSuplementariosAux != null) {
        int lengthEstructuraDatosSuplementariosAux = estructuraDatosSuplementariosAux.size();
        if (lengthEstructuraDatosSuplementariosAux>0 ) {
            for (int i=0;i<lengthEstructuraDatosSuplementariosAux;i++)
            {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(i);
                String nombre = eC.getCodCampo();
                if (eC.getCodTramite() != null) {
                    if (eC.getOcurrencia() != null)
                        nombre = "T_" + eC.getCodTramite() + "_" + eC.getOcurrencia() + "_" + eC.getCodCampo();
                    else
                        nombre = "T_" + eC.getCodTramite() + "_" + eC.getCodCampo();
                }
                String tipo = eC.getCodTipoDato();
                _log.debug("NOMBRE "+nombre);
                _log.debug("TIPO   "+tipo);
                if (tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegable"))) {%>
                    <script type="text/javascript">
                         eval("var combo<%=nombre%> = new Combo('<%=nombre%>',<%=idioma%>)");
                         eval("combo<%=nombre%>.addItems(<%=eC.getListaCodDesplegable()%>,<%=eC.getListaDescDesplegable()%>)");
                    </script>
            <%}
            }
        }
    }
    Vector estructuraDatosSuplementariosTramitesAux = null;
    estructuraDatosSuplementariosTramitesAux = (Vector) expForm.getEstructuraDatosSuplementariosTramites();
    if (estructuraDatosSuplementariosTramitesAux != null) {
        int lengthEstructuraDatosSuplementariosTramitesAux = estructuraDatosSuplementariosTramitesAux.size();
        if (lengthEstructuraDatosSuplementariosTramitesAux>0 ) {
            int j=0;
            for (int i=0;i<lengthEstructuraDatosSuplementariosTramitesAux;i++)
            {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosTramitesAux.elementAt(i);
                String nombre = eC.getCodCampo();
                if (eC.getCodTramite() != null) {
                    if (eC.getOcurrencia() != null)
                        nombre = "T_" + eC.getCodTramite() + "_" + eC.getOcurrencia() + "_" + eC.getCodCampo();
                    else
                        nombre = "T_" + eC.getCodTramite() + "_" + eC.getCodCampo();
                }
                String tipo = eC.getCodTipoDato();
                _log.debug("NOMBRE "+nombre);
                _log.debug("TIPO   "+tipo);
                if (tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegable"))) {%>
                    <script type="text/javascript">
                         eval("var combo<%=nombre%> = new Combo('<%=nombre%>',<%=idioma%>)");
                         eval("combo<%=nombre%>.addItems(<%=eC.getListaCodDesplegable()%>,<%=eC.getListaDescDesplegable()%>)");
                         nombreCombos[<%=j%>]=['<%=nombre%>'];
                        <%j++;%>
                    </script>
                <%}
            }
        }
    }
    //Mai
    
   ArrayList<EstructuraCampoModuloIntegracionVO> camposConsultaModuloExternoAux=null;
   camposConsultaModuloExternoAux=(ArrayList<EstructuraCampoModuloIntegracionVO>) expForm.getCamposConsulta();
   int lonxitudeCamposConsultaModuloExternoAux=0;
   if(camposConsultaModuloExternoAux!=null){
        lonxitudeCamposConsultaModuloExternoAux=camposConsultaModuloExternoAux.size();
        _log.debug("Lonxitude do array de campos de consulta do modulo de externo: "+lonxitudeCamposConsultaModuloExternoAux);
   
        if (lonxitudeCamposConsultaModuloExternoAux>0 ) {
                int j=0;
                for (int i=0;i<lonxitudeCamposConsultaModuloExternoAux;i++)
                {
                    EstructuraCampoModuloIntegracionVO eC = (EstructuraCampoModuloIntegracionVO) camposConsultaModuloExternoAux.get(i);
                    String nombre = eC.getCodCampo();
                    Integer tipo = eC.getTipoCampo();
                    _log.debug("NOMBRE. Campos de Consulta: "+nombre);
                    _log.debug("TIPO. Campos de Consulta:   "+tipo);
                  // m_Config.getString("codigoCampoConsultaDesplegable")
                    if (tipo==6) {%>
                        
                        <script type="text/javascript">
                           
                             eval("var combo<%=nombre%> = new Combo('<%=nombre%>',<%=idioma%>)");
                             eval("combo<%=nombre%>.addItems(<%=eC.getListaCodDesplegable()%>,<%=eC.getListaDescDesplegable()%>)");
                             nombreCombos[<%=j%>]=['<%=nombre%>'];
                            <%j++;%>
                        </script>
                    <%}
                }
            }
    }
    
    
    
}%>


<script type="text/javascript">

    function mostrarCapasBotones(nombreCapa) {
        document.getElementById('capaBotones1').style.visibility='hidden';
        document.getElementById(nombreCapa).style.visibility='visible';
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
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else{
            teclaAuxiliar = evento.which;
        }


        if (teclaAuxiliar == 1){
            if (comboProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);  //Combos creados con new Combo
            if(IsCalendarVisible) replegarCalendario(coordx,coordy);
             for(i=0;i<nombreCombos.length;i++) {
                 var nCombo=eval('combo'+nombreCombos[i]);
                 eval('if (nCombo.base.style.visibility == "visible" && isClickOutCombo(nCombo,coordx,coordy)) nCombo.ocultar()');
             }
        }
        if (teclaAuxiliar == 9){
           if (comboProcedimiento.base.style.visibility == "visible") comboProcedimiento.ocultar();
            if(IsCalendarVisible) hideCalendar();
            for(i=0;i<nombreCombos.length;i++) {
                var nCombo=eval('combo'+nombreCombos[i]);
                eval('nCombo.ocultar()');
            }
        }
        keyDel(evento);
    }
    
    var comboProcedimiento= new Combo("Procedimiento");
    function cargarComboBox(cod, des){
        eval(auxCombo+".addItems(cod,des)");
    }
    comboProcedimiento.change = function() {
        consultaCamposProcedimiento();
    }
    
    function pulsarBuscarTerceros() {
    var argumentos = new Array();
    argumentos['modo'] = 'seleccion';
    argumentos['terceros'] =[ ];
    var source = '<c:url value='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true'/>';
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
          'width=998,height=830',function(datos){
        if(datos!=undefined){
          document.forms[0].tercero.value = datos[2];
          document.forms[0].versionTercero.value = datos[3];
          document.forms[0].titular.value = datos[6];
          document.forms[0].tipoDocumentoTercero.value = datos[4];
          document.forms[0].documentoTercero.value = datos[5];
        }
    });
}
        
</script>
<div id="desplegable" style="overflow: auto; visibility: hidden; BORDER: 0px"></div>

</BODY>
</html:html>
