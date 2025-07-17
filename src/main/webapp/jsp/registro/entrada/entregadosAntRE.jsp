<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DatosSuplementariosFicheroForm"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<html:html>
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=10"/>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Adjuntar Fichero:::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
    int idioma=1;
    int apl=4;
    String codUsu = "";
    String css = "";
    UsuarioValueObject usuario=new UsuarioValueObject();
    Log _log = LogFactory.getLog(this.getClass());
    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
	  	int cUsu = usuario.getIdUsuario();
        codUsu = Integer.toString(cUsu);
        css = usuario.getCss();
      }
    }
        
%>
<!-- Estilos -->

<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<!--
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
-->
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<body class="bandaBody" onload="">
<html:form action="/registro/EntregadosAnterior.do" target="_self" enctype="multipart/form-data">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="codigo" value="">
    <div id="titulo" class="txttitblanco">Documentos Entregados Anteriormente</div>
    <div class="contenidoPantalla">
    <table class="contenidoPestanha">
        <tr>
            <td colspan="2" class="etiqueta"> <%=descriptor.getDescripcion("res_fecDoc")%>:</td>
        </tr>
        <tr>
            <td colspan="2">
                <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="12"  property="fechaDocumento"
                           onkeyup = "javascript: return SoloCaracteresFecha(this);"
                           onfocus = "javascript: this.select();" />                                                        
                <a href="javascript:calClick(event);return false;" onClick="mostrarCalFechaLimiteDesde(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                    <span class="fa fa-calendar" aria-hidden="true" style="border: 0px none; height: 17; width: 25" id="calFechaLimiteDesde" name="calFechaLimiteDesde" alt="Fecha" ></span>
                </a>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="etiqueta">Tipo de Documento</td>
        </tr>    
        <tr>
            <td colspan="2">
                <input type="text" name="tipoDocumento" style="width: 356px" id="tipoDocumento" class="inputTexto" onkeyup="return xAMayusculas(this);"/> 
            </td>
        </tr> 
         <tr>
            <td colspan="2" class="etiqueta">Nombre de Documento</td>
        </tr>    
        <tr>
            <td colspan="2">
                <input type="text" name="nombreDocumento" style="width: 356px" id="nombreDocumento" class="inputTexto" onkeyup="return xAMayusculas(this);"/>
            </td>
        </tr> 
         <tr>
            <td colspan="2" class="etiqueta">Órgano</td>
        </tr>    
        <tr>
            <td colspan="2">
                <input type="text" name="organo" style="width: 356px" id="organo" class="inputTexto" onkeyup="return xAMayusculas(this);"/>
            </td>
        </tr> 
    </table>

    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%= descriptor.getDescripcion("etiq_limpiar") %>" name="Vaciar" onclick="javascript:resetearFormulario();"/>           												
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
    </div>
            </DIV>
</html:form>
<script language="javascript">
     function resetearFormulario(){   
        document.getElementById("nombreDocumento").value="";
        document.getElementById("tipoDocumento").value="";            
        document.getElementById("organo").value="";         
        document.forms[0].cmdAceptar.disabled = false;
                                    
    }
 
    /** Se validan los campos necesarios para dar de alta un documento */
    function validarCampos(){
        var nombreDocumento = document.forms[0].nombreDocumento.value;
        var tipoDocumento = document.forms[0].tipoDocumento.value;
        var organo       = document.forms[0].organo.value; 
                 
        if(nombreDocumento!=null && tipoDocumento!=null && organo!=null && nombreDocumento!="" && tipoDocumento!="" && organo!=""){             
            return true;
        }else{    
            return false;
        }
    }
    function pulsarAceptar() { 
      if (validarCampos()){
          document.forms[0].opcion.value = 'anteriorAlta';
          document.forms[0].action = "<html:rewrite page='/registro/EntregadosAnterior.do'/>";
          document.forms[0].submit();
      }else{              
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
      }            
    }

    function pulsarCancelar(){
        top.close();
    }
    
  function mostrarCalFechaLimiteDesde(evento) {
      if(window.event) evento = window.event;
      
    if (document.getElementById("calFechaLimiteDesde").className.indexOf("fa-calendar") != -1 ){
        
      showCalendar('forms[0]','fechaDocumento',null,null,null,'','calFechaLimiteDesde','',null,null,null,null,null,null,null,null,evento);}
  }
  
document.forms[0].tipoDocumento.value = 
      "<str:escape><bean:write name="EntregadosAnteriorForm" property="tipoDocumento" filter="false"/></str:escape>";
document.forms[0].nombreDocumento.value = 
      "<str:escape><bean:write name="EntregadosAnteriorForm" property="nombreDocumento" filter="false"/></str:escape>";
document.forms[0].organo.value = 
      "<str:escape><bean:write name="EntregadosAnteriorForm" property="organo" filter="false"/></str:escape>";

       
    //Usado para el calendario
var coordx=0;
var coordy=0;

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
</script>

</body>
</html:html>
