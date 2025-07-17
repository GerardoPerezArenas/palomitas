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
<%@page import="es.altia.agora.business.sge.InteresadoExpedienteVO"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto numero de expediente </title>
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
<script type="text/javascript">
var datos = new Array();
var respOpcion = '<bean:write name="TramitacionForm" property="respOpcion"/>';

function redirecciona(){
    datos[0] = '<bean:write name="TramitacionForm" property="codMunicipio"/>';
    datos[1] = '<bean:write name="TramitacionForm" property="codProcedimiento"/>';
    datos[2] = '<bean:write name="TramitacionForm" property="ejercicio"/>';
    datos[3] = '<bean:write name="TramitacionForm" property="numeroExpediente"/>';

    if(respOpcion == "permanenciaEnBuzonNuevoExpediente"){
        parent.mainFrame.iniciarExpediente('<bean:write name="opcionPermanencia"/>', '<bean:write name="tipoRegistroOrigen"/>');
    }//if respOpcion
}//redirecciona
</script>
</head>
<body onLoad="redirecciona();">
<form>
    <input type="hidden" name="opcion" value="">
</form>
<p>&nbsp;<p>
</body>
</html>