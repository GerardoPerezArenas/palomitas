<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES  Consulta de Expedientes:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />


<%int idioma=1;
  int apl=1;
  int munic = 0;
  String soloConsulta = "no";
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      session.setAttribute("soloTerceroFlexia", "si");
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
    String statusBar = m_Config.getString("JSP.StatusBar");
    String userAgent = request.getHeader("user-agent");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
<SCRIPT type="text/javascript">
var cod_procedimiento = new Array();
var desc_procedimiento = new Array();

var cod_clasifTramite = new Array();
var desc_clasifTramite = new Array();
var cod_tipoAnotacion = new Array();
var desc_tipoAnotacion = new Array();
var procedimientoActual = 1;
var procedimientoInf = 1;
var procedimientoSup = 10;
var consultando = false;
var operadorConsulta = '|&:<>!=';
var informesDireccion = false;
var ventanaPadre = null;
if (self.parent.opener)
    if (self.parent.opener.xanelaAuxiliarArgs) {
        if (self.parent.opener.xanelaAuxiliarArgs['informesDireccion']) informesDireccion = true;
        ventanaPadre = self.parent.opener.xanelaAuxiliarArgs['ventanaPadre'];
    }

function inicializar() {
 
  document.forms[0].desdeConsulta.value = "<%=request.getParameter("desdeConsulta")%>";
  habilitarImagenCal("calFechaInicio",true);
  habilitarImagenCal("calFechaFin",true);
  mostrarCapasBotones('capaBotones1');
  consultando = true;
  <logic:iterate id="elemento" name="ConsultaExpedientesForm" property="listaProcedimientos">
    cod_procedimiento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_procedimiento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
  </logic:iterate>
  <logic:iterate id="elemento" name="ConsultaExpedientesForm" property="listaClasifTramite">
    cod_clasifTramite['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_clasifTramite['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
  </logic:iterate>
  
  
  cod_tipoAnotacion['0']='E';
  desc_tipoAnotacion['0']='<%=descriptor.getDescripcion("etiqTipoAnotEntrada")%>';
  
  cod_tipoAnotacion['1']='S';
  desc_tipoAnotacion['1']='<%=descriptor.getDescripcion("etiqTipoAnotSalida")%>';
  
  document.forms[0].expRelacionado.value = '<bean:write name="ConsultaExpedientesForm" property="expRelacionado"/>';
  
  if(document.forms[0].expRelacionado.value == "si") {
    document.forms[0].codMunExpIni.value = '<bean:write name="ConsultaExpedientesForm" property="codMunicipio"/>';
    document.forms[0].ejercicioExpIni.value = '<bean:write name="ConsultaExpedientesForm" property="ejercicio"/>';
    document.forms[0].numeroExpIni.value = '<bean:write name="ConsultaExpedientesForm" property="numeroExpediente"/>';
  }
  document.forms[0].deAdjuntar.value = '<bean:write name="ConsultaExpedientesForm" property="deAdjuntar"/>';
  borrarDatos();
  cargarCombos();
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
  if(document.forms[0].expRelacionado.value == "si" || document.forms[0].deAdjuntar.value == "si" || informesDireccion) {
    self.parent.opener.retornoXanelaAuxiliar();
  } else {
    document.forms[0].opcion.value="inicio";
    document.forms[0].target="mainFrame";
  <% if("no".equals(soloConsulta)) { %>
	document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
  <% } else { %>
    document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>";
  <% } %>
    document.forms[0].submit();
  }
}
function desactivarFormulario() {
  deshabilitarDatos(document.forms[0]);
  comboProcedimiento.deactivate();
  comboRol.deactivate();
  comboClasifTramite.deactivate();
  comboTipoAnotacion.deactivate();
  var vectorBotones = new Array(document.forms[0].cmdCancelarAlta,document.forms[0].cmdGrabarAlta,document.forms[0].cmdCancelarModificar,
    document.forms[0].cmdGrabarModificar,document.forms[0].cmdCancelarBuscar,document.forms[0].cmdEliminar,
    document.forms[0].cmdSalir,document.forms[0].cmdModificar,document.forms[0].cmdConsultar,
    document.forms[0].cmdAlta,document.forms[0].cmdListado,document.forms[0].cmdDescargar,document.forms[0].cmdCancelarImportar);
  habilitarGeneral(vectorBotones);
}
function activarFormulario() {
  habilitarDatos(document.forms[0]);
  comboProcedimiento.activate();
  comboRol.deactivate();
  comboClasifTramite.activate();
  comboTipoAnotacion.activate();
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
function pulsarBuscarTerceros() {
  var argumentos = new Array();
  argumentos['modo'] = 'seleccion';
  argumentos['terceros'] =[ ];
  var source = '<c:url value='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true'/>';
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
	'width=998,height=700,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          document.forms[0].tercero.value = datos[2];
                          document.forms[0].versionTercero.value = datos[3];
                          document.forms[0].titular.value = datos[6];
                          document.forms[0].domicilio.value = datos[14];
                          document.forms[0].tipoDocumentoTercero.value = datos[4];
                          document.forms[0].documentoTercero.value = datos[5];
                        }
                    });
}

function pulsarConsultar() {
    var valido=true;

    if (!comprobarFecha(document.forms[0].fechaInicio))valido=false;//comprobamos la fecha inicio
    else if (!comprobarFecha(document.forms[0].fechaFin))valido=false;//comprobamos la fecha Fin
    if(valido){// si las fechas son correctas
        if(document.forms[0].fechaFin.value!="" && document.forms[0].fechaInicio.value!=""){//si se introducen las 2 fechas
            if( validarFechaAnterior(document.forms[0].fechaInicio.value,document.forms[0].fechaFin.value)){//validamos que la fecha fin no sea mayor a la de inicio

                var  modoConsultaExpRel= '<%=(String)request.getAttribute("modoConsultaExpRel")%>';
                pleaseWait('on');
                if (informesDireccion) {
                    document.forms[0].informesDireccion.value = 'true';
                }
                document.forms[0].numeroExpediente.value=document.forms[0].numeroExpediente.value.toUpperCase();
                document.forms[0].opcion.value="consultar";
                document.forms[0].target="mainFrame";
                document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>" + "?modoConsultaExpRel=" + modoConsultaExpRel;
                document.forms[0].submit();
            }
        }else{
            var  modoConsultaExpRel= '<%=(String)request.getAttribute("modoConsultaExpRel")%>';
            pleaseWait('on');
            if (informesDireccion) {
                document.forms[0].informesDireccion.value = 'true';
            }
            document.forms[0].numeroExpediente.value=document.forms[0].numeroExpediente.value.toUpperCase();
            document.forms[0].opcion.value="consultar";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>" + "?modoConsultaExpRel=" + modoConsultaExpRel;
            document.forms[0].submit();
        }
    }
}


function borrarDatos() {
  document.forms[0].numeroExpediente.value = "";
  document.forms[0].ejercicioExpediente.value = "";
  comboProcedimiento.selectItem("-1");
  comboRol.selectItem("-1");
  comboClasifTramite.selectItem("-1");
  comboTipoAnotacion.selectItem("-1");
  document.forms[0].fechaInicio.value = "";
  document.forms[0].titular.value = "";
  document.forms[0].tercero.value="";
  document.forms[0].versionTercero.value="";
  document.forms[0].domicilio.value = "";
  document.forms[0].fechaFin.value = "";
  document.forms[0].localizacion.value = "";
  document.forms[0].localizacion2.value = "";
  document.forms[0].estado[3].checked = true;
  document.forms[0].ejercicioAnotacion.value = "";
  document.forms[0].numeroAnotacion.value = "";
  document.forms[0].asuntoConsulta.value = "";
  document.forms[0].observaciones.value = "";  
}
function abrirDomicilios() {
  var argumentos = new Array();
  var source = "<c:url value='/sge/TramitacionExpedientes.do?opcion=abrirDomiciliosConsulta'/>";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,argumentos,
        'width=950,height=450,status='+ '<%=statusBar%>',function(datos){
            if(datos!=undefined){
              document.forms[0].localizacion2.value = datos[0];
              document.forms[0].localizacion.value = datos[2];
              document.forms[0].refCatastral.value = datos[3];
            }
        });
}
function cargarCombos() {
        
	comboProcedimiento.addItems(cod_procedimiento,desc_procedimiento);
        comboRol.selectItem("-1");
	comboClasifTramite.addItems(cod_clasifTramite,desc_clasifTramite);
	comboTipoAnotacion.addItems(cod_tipoAnotacion,desc_tipoAnotacion);
}
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include> 
<html:form action="/sge/ConsultaExpedientes.do" target="_self">
<html:hidden property="opcion" value=""/>
<html:hidden property="tercero" />
<html:hidden property="versionTercero" />
<html:hidden property="domicilio" />
<input type="hidden" name="expRelacionado" >
<input type="hidden" name="codMunExpIni" value="">
<input type="hidden" name="ejercicioExpIni" value="">
<input type="hidden" name="numeroExpIni" value="">
<input type="hidden" name="deAdjuntar" >
<input type="hidden" name="codUnidadTram" value ="">
<input type="hidden" name="codTramite" value ="">
<input type="hidden" name="codTipoProced" value ="">
<input type="hidden" name="codArea" value ="">
<input type="hidden" name="desdeConsulta">
<input type="hidden" name="informesDireccion" value="false">

<input type="hidden" name="tipoDocumentoTercero" />
<input type="hidden" name="documentoTercero" />




<style type="text/css">

</style>

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_consExp")%></div>
<div class="contenidoPantalla">
    <TABLE id ="tablaDatosGral">
        <tr>
            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("etiq_numExp")%>:</td>
            <td class="columnP">
                <html:text styleClass="inputTexto" property="numeroExpediente" size="30" maxlength="30" onblur="return xAMayusculas(this);"/>
            </td>
        </tr>
        <tr>
            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEjerExp")%>:</td>
            <td class="columnP">
                <html:text styleClass="inputTexto" property="ejercicioExpediente" style="width:8%" maxlength="4" onkeyup = "return SoloDigitosNumericos(this);" onblur="return xAMayusculas(this);"/>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codProcedimiento" property="codigoProcedimiento" onkeyup="return xAMayusculas(this);" styleClass="inputTexto" style="width:9.5%"/>
                <html:text styleId="descProcedimiento" property="descProcedimiento" styleClass="inputTexto"  readonly="true" style="width:85%"/>
                <A href="" id="anchorProcedimiento" name="anchorProcedimiento" style="text-decoration:none;" > 
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonProcedimiento" name="botonProcedimiento" style="cursor:hand; border:0px none"/>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"></span><%=descriptor.getDescripcion("gEtiqRol")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codRol" property="codRol" onkeyup="return xAMayusculas(this);" styleClass="inputTexto" style="width:9.5%"/>
                <html:text styleId="descRol" property="descRol" styleClass="inputTexto"  readonly="true" style="width:85%"/>
                <A href="" id="anchorRol" name="anchorRol" style="text-decoration:none;" > 
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRol" name="botonRol" style="cursor:hand; border:0px none"/>
                </A>
            </td>
        </tr>

        <tr>
            <td class="etiqueta"></span><%=descriptor.getDescripcion("etiqFechIni")%>:</td>
            <td wclass="columnP">
        <html:text styleClass="inputTxtFecha" size="26" maxlength="25" property="fechaInicio" styleId="fechaInicio"
                           onkeyup = "if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                           onblur = "javascript:return comprobarFecha(this);"
                           onfocus = "this.select();"/>
                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                    <span class="fa fa-calendar" aria-hidden="true" id="calFechaInicio" name="calFechaInicio" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>'  style="cursor:hand; border:0px none"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_fecFin")%>:</td>
            <td class="columnP">
                <html:text styleClass="inputTxtFecha" size="26" maxlength="25" property="fechaFin" styleId="fechaFin"
                           onkeyup = "if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                           onblur = "javascript:return comprobarFecha(this);"
                           onfocus = "this.select();"/>
                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                    <span class="fa fa-calendar" aria-hidden="true" id="calFechaFin" name="calFechaFin" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>'  style="cursor:hand; border:0px none"></span>
                </A>
            </td>
        </tr>
        <tr>
            <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_estado")%>:</TD>
            <TD class="columnP">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width: 18%" class="columnP" valign="top">
                            <input type="radio" name="estado" class="textoSuelto" value="abierto"><%=descriptor.getDescripcion("gbPendientes")%>
                        </td>
                        <td style="width: 18%" class="columnP" valign="top">
                            <input type="radio" name="estado" class="textoSuelto" value="cerrado"><%=descriptor.getDescripcion("gbFinalizados")%>
                        </td>
                        <td style="width: 18%" class="columnP" valign="top">
                            <input type="radio" name="estado" class="textoSuelto" value="anulado"><%=descriptor.getDescripcion("gbAnulados")%>
                        </td>
                        <td class="columnP" valign="top">
                            <input type="radio" name="estado" class="textoSuelto" value="sinEstado" CHECKED><%=descriptor.getDescripcion("gbTodos")%>&nbsp;
                        </td>                                                                    
                    </tr>
                </table>
            </TD>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_titular")%>:</td>
            <td class="columnP">
                <html:text property="titular" styleClass="inputTexto" readonly="true" style="width:95%"/>&nbsp;
                <span class="fa fa-search" aria-hidden="true"  id="botonTerceros" name="botonTerceros" style="cursor:hand;  border:0px none" onclick="javascript:pulsarBuscarTerceros();"></span>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Asunto")%>:</td>
            <td class="columnP">
        <html:text property="asuntoConsulta" styleClass="inputTexto" style=" text-transform: none;width:95%" />&nbsp;
            </td>
        </tr>
         <tr>
            <td class="etiqueta">Observaciones:</td>
            <td class="columnP">
                <html:text property="observaciones" styleClass="inputTexto" style=" text-transform: none;width:95%"  />&nbsp;
               </td>
        </tr>
        <tr>
            <td class="etiqueta" valign="top"><%=descriptor.getDescripcion("etiq_localiz")%>:</td>
            <td>
                <input type="text" class="inputTexto" name="localizacion2" readonly="true" style="width:95%">&nbsp;
                <html:hidden property="localizacion"/>
                <span class="fa fa-home" aria-hidden="true"  name="botonTer" style="cursor:hand;  border:0px none" alt="Domicilios" onclick="javascript:abrirDomicilios();"></span>                                                            
            </td>
        </tr>
        <input type="hidden" class="inputTexto" size="30" name="refCatastral" readonly="true">
        <tr>
            <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_clasTram")%>:</TD>
            <TD valign="top" class="columnP">
                <html:text styleId="codClasifTramite" property="codClasifTramite"          
                           styleClass="inputTexto" style="width:9.5%"/>
                <html:text styleId="descClasifTramite" property="descClasifTramite" 
                           styleClass="inputTexto"  readonly="true" style="width:85%"/>
                <A href="" id="anchorClasifTramite" name="anchorClasifTramite" style="text-decoration:none;" > 
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonClasifTramite" name="botonClasifTramite" style="cursor:hand; border:0px none"></span>
                </A>
            </TD>
        </tr>
        <tr>                          
            <TD style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("etiqNumAnotacion")%>:</TD>
            <TD>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>	                          
                        <TD style="width: 20%" class="columnP">
                            <html:text  styleClass='inputTexto' size="5" maxlength="4" property="ejercicioAnotacion"
                                        onkeyup="return SoloDigitosNumericos(this);" onfocus="this.select();"/>&nbsp;<span style="font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11px; font-style:normal; font-weight: normal; font-variant: normal; color: #999999;">/</span>
                            <html:text  styleClass='inputTexto' size="9" maxlength="8" property="numeroAnotacion"
                                       onkeyup="return SoloDigitosNumericos(this);"
                                        onfocus="this.select();" />
                        </TD>                                                     	                         
                        <TD style="width: 18%" align="left" class="etiqueta"><%=descriptor.getDescripcion("etiqTipoAnotacion")%>:</TD>
                        <TD class="columnP">
                            <html:text styleId="codTipoAnotacion" property="codTipoAnotacion"          
                                       styleClass="inputTexto" size="2"/>
                            <html:text styleId="descTipoAnotacion" property="descTipoAnotacion" 
                                       styleClass="inputTexto"  size="9" readonly="true"/>
                            <A href="" id="anchorTipoAnotacion" name="anchorTipoAnotacion" style="text-decoration:none;" > 
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoAnotacion" name="botonTipoAnotacion" style="cursor:hand; border:0px none"></span>
                            </A>
                        </TD>                      
                    </tr>
                </table>
            </td>                                                                           
        </tr>
        <tr>
            <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_Inicio")%>:</TD>
            <TD class="columnP">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width: 18%" class="columnP" valign="top">
                            <input type="radio" name="TipoInicio" class="textoSuelto" value="Instancia"><%=descriptor.getDescripcion("gbDeInstancia")%>
                        </td>
                        <td style="width: 18%" class="columnP" valign="top">
                            <input type="radio" name="TipoInicio" class="textoSuelto" value="Oficio"><%=descriptor.getDescripcion("gbDeOficio")%>
                        </td>
                        <td class="columnP" valign="top">
                            <input type="radio" name="TipoInicio" class="textoSuelto" value="Todos" CHECKED><%=descriptor.getDescripcion("gbTodos")%>&nbsp;
                        </td>                                                                    
                    </tr>
                </table>
            </TD>
        </tr>
        <tr>
            <TD class="etiqueta"><%=descriptor.getDescripcion("etiqDesdeRegTelematico")%>:</TD>
            <TD class="columnP">
                <input type="checkbox" name="expTelematico" class="textoSuelto" value="1">
            </TD>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("tipoBusqueda")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codTipoBusqueda" property="tipoBusqueda" style="display:none" size="1"/>
                <input type="text" id="descTipoBusqueda" name="descTipoBusqueda" class="inputTexto"  size="20" readonly="true"/>
                <a href="" id="anchorTipoBusqueda" name="anchorTipoBusqueda" style="text-decoration:none;" > 
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoBusqueda" name="botonTipoBusqueda" style="cursor:hand; border:0px none"/>
                </a>
            </td>
        </tr>
    </table>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal"></span>
        <INPUT type= "button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbConsultar")%> name="cmdConsultar"  onclick="pulsarConsultar();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onclick="pulsarSalir();">
    </DIV>    
</div>    
</html:form>
<script type="text/javascript">
function mostrarCapasBotones(nombreCapa) {
  document.getElementById('capaBotones1').style.visibility='hidden';
  document.getElementById(nombreCapa).style.visibility='visible';
}



document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
   var teclaAuxiliar = evento.which;
    var coordx = evento.clientX;
    var coordy = evento.clientY;
    
	if (teclaAuxiliar == 1){

            if (comboProcedimiento.base.style.display != "none" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);

            if (comboClasifTramite.base.style.display != "none" && isClickOutCombo(comboClasifTramite,coordx,coordy)) setTimeout('comboClasifTramite.ocultar()',20);
            if (comboTipoAnotacion.base.style.display != "none" && isClickOutCombo(comboTipoAnotacion,coordx,coordy)) setTimeout('comboTipoAnotacion.ocultar()',20);
            if (comboTipoBusqueda.base.style.display != "none" && isClickOutCombo(comboTipoBusqueda,coordx,coordy)) setTimeout('comboTipoBusqueda.ocultar()',20);
            if (comboRol.base.style.display != "none" && isClickOutCombo(comboRol,coordx,coordy)) setTimeout('comboRol.ocultar()',20);
           if (IsCalendarVisible) replegarCalendarioGeneral(evento);
 
        }
        if (teclaAuxiliar == 9){
            comboProcedimiento.ocultar();
            comboClasifTramite.ocultar();
            comboTipoAnotacion.ocultar();
            comboTipoBusqueda.ocultar();
            comboRol.ocultar();
            if(IsCalendarVisible) hideCalendar();
        }
    keyDel(evento);

}

var comboProcedimiento= new Combo("Procedimiento");
comboProcedimiento.change=cargarRoles;
var comboRol=new Combo("Rol");
var comboClasifTramite= new Combo("ClasifTramite");
var comboTipoAnotacion= new Combo("TipoAnotacion");
function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
}


var comboTipoBusqueda = new Combo("TipoBusqueda");
cargarComboTipoBusqueda();

function cargarRoles(){
   
    var listaCodigosRol=new Array();
    var listaDescripcionesRol=new Array();
    //codigosUnidadesDocumentales = new Array();
    var codProcedimientoSeleccionado = document.getElementById("codProcedimiento").value;
    
    var ajax = getXMLHttpRequest();
    var CONTEXT_PATH = '<%=request.getContextPath()%>';
    if(ajax!=null && codProcedimientoSeleccionado!=null && codProcedimientoSeleccionado!=""){
            var url = CONTEXT_PATH + "/sge/ConsultaExpedientes.do";
            var parametros = "opcion=recuperarRolProcedimiento&codProcedimiento=" + codProcedimientoSeleccionado;        
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
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//
                 nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                 
                  var elemento = nodos[0];
                  var hijos = elemento.childNodes;
                  for(x=0;hijos!=null && x<hijos.length;x++){
                                   
                                    var codigoRol = "";
                                    var descripcionRol ="";
                                    if(hijos[x].nodeName == "ROLES"){
                                       
                                        var listaRoles = xmlDoc.getElementsByTagName("ROL");
                                        
                                        for(x=0;listaRoles!=null && x<listaRoles.length;x++){
                                            var rol = listaRoles[x].childNodes;
                                            
                                            for(y=0;rol!=null && y<rol.length;y++){
                                                if(rol[y].nodeName=="CODIGO"){ 
                                                    codigoRol = rol[y].childNodes[0].nodeValue;
                                                   
                                                }else if(rol[y].nodeName=="DESCRIPCION"){
                                                    descripcionRol = rol[y].childNodes[0].nodeValue;
                                                    

                                                }
                                                listaCodigosRol[x]=codigoRol;
                                                listaDescripcionesRol[x]=descripcionRol;
                                                
                                           
                                           }//for
                                        }//for
                                    }//if   

                    }//for   
         
         
         //Pues ahora creamos el combo de roles.
         comboRol.clearItems();
         comboRol.addItems(listaCodigosRol, listaDescripcionesRol);
         }catch(Err){
                    alert("cargarRoles. Error.descripcion: " + Err.description);
         }//
   }//ajax distinto de null 
 
 }
 
 function cargarComboTipoBusqueda(){
    var codigosTipoBusqueda = new Array();
    var descripcionesTipoBusqueda = new Array();

    codigosTipoBusqueda[0]         = "0";
    descripcionesTipoBusqueda[0] = '<%=descriptor.getDescripcion("activos")%>';
    codigosTipoBusqueda[1]         = "1";
    descripcionesTipoBusqueda[1] = '<%=descriptor.getDescripcion("activos")%>' + '/' + '<%=descriptor.getDescripcion("historico")%>';
    codigosTipoBusqueda[2]         = "2";
    descripcionesTipoBusqueda[2] = '<%=descriptor.getDescripcion("historico")%>';
    
    comboTipoBusqueda.addItems(codigosTipoBusqueda,descripcionesTipoBusqueda);
    
    comboTipoBusqueda.deactivate();        
    comboTipoBusqueda.selectItem(1);        
    comboTipoBusqueda.activate();
}

    /** 
    Devuelve un objeto XMLHttpRequest
   */
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
    
    
    document.onkeydown = function(){
	
        if (event.keyCode == 13) 	
            pulsarConsultar();
    }
    
    
</script>
<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

</BODY>
</html:html>
