<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.common.service.config.Config" %>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <title> Tramitacion expedientes </title>

        <jsp:include page="/jsp/plantillas/Metas.jsp" />

        <%
        Log m_log = LogFactory.getLog(this.getClass().getName());
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 1;

        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
        Config m_Config = ConfigServiceHelper.getConfig("common");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

<script>

        function redirecciona(){
            var opcion = "<%=request.getAttribute("ERROR_VERIFICACION_ANULACION")%>";

            if(opcion!=null && opcion=="NO_PERMITIR_FINALIZACION"){
                parent.mainFrame.mostrarMensajeAnulacionExpediente();
            }else
            if(opcion!=null && opcion=="ERROR_AL_VERIFICAR"){
                parent.mainFrame.mostrarMsgErrorVerificacionAnulacionExpediente();
            }else{
            
                var expFinalizado ='<bean:write name="TramitacionExpedientesForm" property="respOpcion"/>';
                parent.mainFrame.volverFinalizacionNoConvencional(expFinalizado);
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