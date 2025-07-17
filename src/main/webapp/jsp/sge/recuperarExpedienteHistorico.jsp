<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

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
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>::: EXPEDIENTES  Reabrir expediente:::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>


<script type="text/javascript">
function reabrirExpediente(){
    if (document.forms[0].numeroExpediente.value==''){
        jsp_alerta('A',"<%=descriptor.getDescripcion("msjNumReabrirExp")%>");            
    }else{                    
        pleaseWait('on');
        try{
            var numExp = $("#numeroExpediente").val();

            $.ajax({
                 url:  '<c:url value="/sge/RecuperarExpedienteHistorico.do"/>',
                 type: 'POST',
                 async: true,
                 data: 'opcion=recuperarExpedienteHistorico&numExp=' + numExp,
                 success: procesarRespuestaGrabar,
                 error: muestraErrorGrabar
            });      
        }catch(Err){
            pleaseWait('off');
            jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorGenerico")%>' + ' ' + Err.description);            
        } 
    }
 }

function procesarRespuestaGrabar(ajaxResult){    
    pleaseWait('off');
    
    var respuesta = JSON.parse(ajaxResult);    
    if(respuesta!=null){        
        switch(respuesta.status){
            case 0:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgDatosGrabados")%>');
                break;
            case 1:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgErrConexionBD")%>');
                break;
            case 2:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgErrGrabarDatos")%>');
                break;                      
            case 3:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorGenerico")%>');
                break;    
            case 4:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgExpNoEncHist")%>');
                break;    
        }
    }
}

function muestraErrorGrabar(ajaxResult){
    pleaseWait('off');
    jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorGenerico")%>');    
}
</script>
        
</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off');  }">
    
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
    <form name="formulario" method="post">
        <div class="txttitblanco"><%=descriptor.getDescripcion("recuperarExpHistorico")%></div>
        <div class="contenidoPantalla">						
            <table style="width: 100%"> 		                 
                <tr>
                    <td class="etiqueta" align="RIGHT" style="padding-right:7px;">
                        <%=descriptor.getDescripcion("etiq_numExp")%>:
                    </td>
                    <td style="width: 250px">
                        <input type="text" class="inputTexto" id="numeroExpediente" name="numeroExpediente" size="30" maxlength="30" onblur="return xAMayusculas(this);"/>
                    </td>
                    <td> 
                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("recuperar")%>" onclick="reabrirExpediente()"/>
                    </td>
                </tr>                                           
            </table>                  
        </div>
    </form>
</BODY>
</html:html>
