<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head>
<title>Oculto Buzón</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=1;
	int cod_org=1;
	int cod_dep=1;
	int cod_ent= 1;
	int cod_unidOrg=1;
	String desc_org="";
	String desc_ent="";

	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		idioma = usuarioVO.getIdioma();
		apl = usuarioVO.getAppCod();
		cod_org= usuarioVO.getOrgCod();
		cod_dep= usuarioVO.getDepCod();
		cod_ent = usuarioVO.getEntCod();
		cod_unidOrg = usuarioVO.getUnidadOrgCod();
		desc_org = usuarioVO.getOrg();
		desc_ent = usuarioVO.getEnt();
	}
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
</head>

<body onLoad="javascript:{realizar_comprobación()}">
<form>
	<input type="hidden" name="acceso" value="consultar" />
</form>
</body>
<script type="text/javascript">
function realizar_comprobación(){
<% 
	MantAnotacionRegistroForm marf = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
	RegistroValueObject rvo = marf.getRegistro();
	if(!("actualizacion_ya_realizada".equals(rvo.getRespOpcion()))){//vengo del botón de buzón
%>
var x = '<bean:write name="BuzonForm" property="filas"/>';

if (x == 'true')
	window.open("<%=request.getContextPath()%>/jsp/registro/buzon.jsp","ventanaBuzon","status="+ '<%=statusBar%>' + ",height=300,width=800,left=112,top=234,modal=No");
else{
	var msj = "<%=descriptor.getDescripcion("msjBuz")%>";
	jsp_alerta("A",msj);
}
<%
	} else {//vengo de una actualizacion ya hecha
%>
		var error = "<%=descriptor.getDescripcion("msjErrorBuz")%>";
		jsp_alerta("A",error);
<%
	rvo.setRespOpcion("");
%>
		document.forms[0].action="<%=request.getContextPath()%>/Buzon.do";
		document.forms[0].submit();<!-- voy otra vez a consultar lo que hay en el buzón -->
<%}%>
}
</script>
</html>
