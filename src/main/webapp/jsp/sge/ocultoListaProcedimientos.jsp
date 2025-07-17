<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto lista de procedimientos </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma=1;
            int apl=1;

            if (session.getAttribute("usuario") != null){
                    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                    idioma = usuarioVO.getIdioma();
                    apl = usuarioVO.getAppCod();
            }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script>
    var datos = new Array();

    function redirecciona(){
        var error = '<c:out value="${requestScope.error}"/>';
        var errorLeerFichero = '<c:out value="${requestScope.errorLeerFichero}"/>';
        var errorGuardarDatosSupFichero = '<c:out value="${requestScope.errorGuardarDatosSupFichero}"/>';
        var errorGuardarInteresadosFichero = '<c:out value="${requestScope.errorGuardarInteresadosFichero}"/>';
        var errorGuardarInteresadosCampOblig = '<c:out value="${requestScope.errorGuardarInteresadosCampOblig}"/>';
        var errorCargarExtension = '<c:out value="${requestScope.errorCargarExtension}"/>';
        var errorGuardarDomicilioInteresadosIncompleto = '<c:out value="${requestScope.errorGuardarDomicilioInteresadosIncompleto}"/>';
        var errorNoExisteInteresadoAsociadoAnotacion = '<c:out value="${requestScope.errorNoExisteInteresadoAsociadoAnotacion}"/>';
        var errorActualizarNotifElectInteresadoAnotacion = '<c:out value="${requestScope.errorActualizarNotifElectInteresadoAnotacion}"/>';
        
        var errorExpedienteRelacionadoNoExiste = '<c:out value="${requestScope.errorExpedienteRelacionadoNoExiste}"/>';
        var expedienteRelacionado              = '<c:out value="${requestScope.expedienteRelacionado}"/>';
        var errorRelacionarExpedienteIniciado  = '<c:out value="${requestScope.errorRelacionarExpedienteIniciado}"/>';
        
        
        var errorEjecucionExtension = '<c:out value="${requestScope.errorEjecucionExtension}"/>';        
        var nombreDocumento = '<c:out value="${requestScope.nombreDocumento}"/>';
        
        if (errorLeerFichero != ''){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorLeerFichero")%>' + " " + nombreDocumento);
            pleaseWait('off');
        }else{
            if (errorGuardarDatosSupFichero != '' || errorGuardarInteresadosFichero != '' || errorGuardarInteresadosCampOblig != '' || errorCargarExtension != '' 
                || errorGuardarDomicilioInteresadosIncompleto!='' || errorNoExisteInteresadoAsociadoAnotacion!='' || errorActualizarNotifElectInteresadoAnotacion!='' || errorEjecucionExtension!='' || errorExpedienteRelacionadoNoExiste!='' || errorRelacionarExpedienteIniciado!=''){ 
                
                if (errorGuardarDatosSupFichero != ''){ jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorGuardarDatosSupFichero")%>' + " " + nombreDocumento);}
                if (errorGuardarInteresadosFichero != ''){ jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorGuardarInterFichero")%>' + " " + nombreDocumento);}
                if (errorGuardarInteresadosCampOblig != ''){ jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorGuardarInterCampOblig")%>' + " " + nombreDocumento);}                
                if (errorGuardarDomicilioInteresadosIncompleto != ''){ jsp_alerta("A",'<%=descriptor.getDescripcion("errInteresadoIncompleto")%>' + " " + nombreDocumento);}   
                if (errorNoExisteInteresadoAsociadoAnotacion != ''){ jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorNoExisteInterAsocAnot")%>' + " " + nombreDocumento);}   
                if (errorActualizarNotifElectInteresadoAnotacion != ''){ jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorActualizarInterAnot")%>' + " " + nombreDocumento);}   
                if (errorCargarExtension != ''){ jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorCargarExtension")%>'+ " " + nombreDocumento);}                                
                if (errorEjecucionExtension != ''){ jsp_alerta("A","<%=descriptor.getDescripcion("msjErrorEjecucionExtension")%>" + " " + errorEjecucionExtension);}
                if (errorExpedienteRelacionadoNoExiste!='' && expedienteRelacionado!=''){ jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorExpRelacNoExiste1")%>" + " " + expedienteRelacionado + " " + "<%=descriptor.getDescripcion("msgErrorExpRelacNoExiste2")%> ");}
                if (errorRelacionarExpedienteIniciado!='' && expedienteRelacionado!=''){ jsp_alerta("A","<%=descriptor.getDescripcion("msgErrRelacExpIniciado1")%>" + " " + expedienteRelacionado + " " + "<%=descriptor.getDescripcion("msgErrRelacExpIniciado2")%> ");}
                
                
                
                var datos = '<c:out value="${requestScope.expedienteCreado}"/>';
                parent.mainFrame.recibirNumero(datos);
            }else{
                if (error != '') {
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjErrorIniciarExpediente")%>');                    
                    pleaseWait('off');
                }else{
                    var datos = '<c:out value="${requestScope.expedienteCreado}"/>';
                    parent.mainFrame.recibirNumero(datos);
                }
            }
        }
    }
    </script>
</head>
<body onLoad="redirecciona();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>
    <p>&nbsp;<p><center>
</body>
</html>
