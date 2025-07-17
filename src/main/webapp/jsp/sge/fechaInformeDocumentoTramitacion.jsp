<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.technical.ConstantesDatos" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="java.util.Vector" %>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<%
  int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }
String userAgent = request.getHeader("user-agent");
%>

<TITLE></TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<!-- Estilos -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>


<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<SCRIPT type="text/javascript">   

var codDocumento     = "<%=request.getParameter("codDocumento")%>";
var codProcedimiento = "<%=request.getParameter("codProcedimiento")%>";
var codMunicipio        = "<%=request.getParameter("codMunicipio")%>";
var ejercicio              = "<%=request.getParameter("ejercicio")%>";
var numero             = "<%=request.getParameter("numero")%>";
var codTramite          = "<%=request.getParameter("codTramite")%>";
var ocurrencia           = "<%=request.getParameter("ocurrencia")%>";
var fechaInforme       = "<%=request.getParameter("fechaInforme")%>";
var fechaCreacion     = "<%=request.getParameter("fechaCreacion")%>";


function inicializar() {
    window.focus();
    document.forms[0].fechaInforme.value = fechaInforme;    
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
        }
        else
            if (window.ActiveXObject){
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

function pulsarAceptar(){
    
    var salida = new Array();    

    var fechaCorrecta = comprobarFecha(document.forms[0].fechaInforme);
    var comparacionFechas = compararFechas(fechaCreacion,document.forms[0].fechaInforme.value);
    if(document.forms[0].fechaInforme.value!=null && document.forms[0].fechaInforme.value.length>0 && fechaCorrecta){
        var source = "<html:rewrite page='/sge/DocumentosExpediente.do?opcion=actualizarFechaInforme'/>";
        var salir = false;
        var parametros = "opcion=actualizarFechaInforme&codMunicipio=" + codMunicipio + "&codProcedimiento=" + codProcedimiento + "&ejercicio=" + ejercicio +
                                   "&numero=" + numero + "&codTramite=" + codTramite + "&ocurrencia=" + ocurrencia + "&codDocumento=" + codDocumento + "&fechaInforme=" + document.forms[0].fechaInforme.value;
                            
        var ajax = getXMLHttpRequest();
        ajax.open("POST",source,false); // Llamada síncrona, mientras el servidor no de una respuesta no se continua con la ejecución
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        ajax.send(parametros); 

        // Se recuperan las unidades organicas de cada trámite si los tiene
        var res = ajax.responseText;
        var primerResultado = new Array();
        primerResultado = res.split("=");
        if(primerResultado!=null && primerResultado.length>=1){

            var retorno = new Array();            
            if(primerResultado[0].trim()=="ok" && primerResultado[1].trim()=="si"){                
                retorno[0] = "ok";
                retorno[1] = document.forms[0].fechaInforme.value;
                self.parent.opener.retornoXanelaAuxiliar(retorno);
            }else{                
                retorno[0] = "fail";
                self.parent.opener.retornoXanelaAuxiliar(retorno);
            }            
        }
    }else{
       if(!fechaCorrecta)
            jsp_alerta("A","<%=descriptor.getDescripcion("fechaNoVal")%>");
       else{
           if(!comparacionFechas) jsp_alerta("A","<%=descriptor.getDescripcion("msg_errorFechasDocumento")%>");
        }
    }
    
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar(undefined);
}

  

function ltrim(s) {
	return s.replace(/^\s+/, "");
}

function rtrim(s) {
	return s.replace(/\s+$/, "");
 }

function trim(s) {
	return rtrim(ltrim(s));
}


/**
 * Compara la fecha de inicio con la de fin y si la de inicio es menor o igual que la de fin devuelve true.
 * Las fechas se pasan en formado dd/mm/yyyy
 */
function compararFechas(fechaInicio,fechaFin){    
    var exito = false;
    var dFecInicio = fechaInicio.split("/");
    var dFecFin     = fechaFin.split("/");
    if(dFecInicio!=null && dFecInicio.length==3 && dFecFin!=null && dFecFin.length==3){
        var dIni  = dFecInicio[0];
        var mIni = dFecInicio[1];
        var yIni  = dFecInicio[2];

        var dFin  = dFecFin[0];
        var mFin = dFecFin[1];
        var yFin  = dFecFin[2];

        dFechaInicio = new Date(yIni+"/"+mIni + "/"+dIni);
        dFechaFin   = new Date(yFin+"/"+mFin+"/"+dFin);

        if(dFechaInicio<=dFechaFin) exito = true;
    }
    
    return exito;
}

function mostrarCalFechaInforme(evento) {
    if(window.event) evento = window.event;
  if (document.getElementById("calFechaInforme").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaInforme',null,null,null,'','calFechaInforme','',null,null,null,null,null,null,null,'',evento);
}

function comprobarFecha(inputFecha) {
 if (Trim(inputFecha.value)!=' ') {
  if (!ValidarFechaConFormato(document.forms[0],inputFecha)){    
    return false;
  }
 } 
 return true;
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
            if(IsCalendarVisible) replegarCalendario(coordx,coordy);
        }
        if (teclaAuxiliar == 9){
            if(IsCalendarVisible) hideCalendar();
        }
}
</SCRIPT>
</head>

<BODY class="bandaBody" onload="javascript:inicializar();">
<html:form action="/sge/DocumentosExpediente.do" target="_self">

<html:hidden  property="opcion" value=""/>
<input type="hidden" name="tipoAccion" value="">
<html:hidden  property="codMunicipio" value=""/>
<html:hidden  property="codProcedimiento" value=""/>
<html:hidden  property="ejercicio" value=""/>
<html:hidden  property="numeroExpediente" value=""/>
<html:hidden  property="codTramite" value=""/>
<html:hidden  property="ocurrenciaTramite" value=""/>
<html:hidden  property="codDocumento" value=""/>
<html:hidden  property="numeroDocumento" value=""/>

<div class="txttitblanco"><%=descriptor.getDescripcion("etiq_modFecInformeDoc")%></div>
<div class="contenidoPantalla">
    <table border="0">
        <tr>
            <td class="etiqueta">
                <html:text styleClass="inputTxtFecha" size="10" maxlength="10" property="fechaInforme"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();"/>
                <A href="javascript:calClick(event);return false;" onclick="mostrarCalFechaInforme(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                    <span class="fa fa-calendar" aria-hidden="true"  style="border: 0" name="calFechaInforme" id="calFechaInforme" alt="Data"></span>
                </A>
            </td>
        </tr>
    </table>
    <div id="enlace" STYLE="position: relative; width:100%;"></div>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAnadir")%>" name="cmdAnadir" onClick="pulsarAceptar();return false;">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdSalir" onClick="pulsarCancelar();">
    </DIV>
</div>
</html:form>



<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>

</html:html>


