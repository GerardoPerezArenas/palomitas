<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%
	int idioma = 1;
	int apl = 1;
	if (session != null) {
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		if (usuario != null) {
			idioma = usuario.getIdioma();
			apl = usuario.getAppCod();
		}
	}
	Config m_Config = ConfigServiceHelper.getConfig("common");
	String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl%>" />

<html:html>
	<head>
		<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
		<TITLE>:::VISUALIZAR INTERESADO:::</TITLE>
			<jsp:include page="/jsp/plantillas/Metas.jsp" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
		<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script> 
		<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
	</head>
	<body>

		<!--TODO insertar una etiqueta mejor en base de datos -->
		<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titMotivoRechazo")%></div>
		<div class="contenidoPantalla">
			<div style="float:left;width:100%">
				<span class="etiqueta"><%=descriptor.getDescripcion("etiqMotivoRechazo")%></span>
				<span class="columnP">
					<textarea class="capaTextoLargo" name="motivoRechazo" cols="74" rows="5"
							style="width:99%" id="motivoRechazo"></textarea>
				</span>
			</div>
			<div class="botoneraPrincipal">
				<input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbRechazar")%> 
					   name="botonRechazar"  id="botonRechazar" onClick="pulsarRechazar();" accesskey="A">
				<input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> 
					   name="botonCancelar" id="botonCancelar" onClick="pulsarCancelar();" accesskey="E">
			</div>
		</div>
	</body>
</html:html>
<script type="text/javascript">
	function pulsarRechazar() {
		if (document.getElementById('motivoRechazo').value != "") {
			self.parent.opener.retornoXanelaAuxiliar(document.getElementById('motivoRechazo').value);
		}else{
			jsp_alerta("A", "<%=descriptor.getDescripcion("msjFaltaMotivoRechazo")%>");
		}
		
	}
	
	function pulsarCancelar() {
		self.parent.opener.retornoXanelaAuxiliar();
	}
</script>
