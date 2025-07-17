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
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

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
		<TITLE>:::Buzon Entrada de asiento</TITLE>
			<jsp:include page="/jsp/plantillas/Metas.jsp" />

		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
		<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script> 
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/sir/verXmlAsiento.js"></script>
		<script type="text/javascript">
			//Funcion necesaria para poder acceder al ContextPath desde el JS
			function getContextPath() {
				var contextPath = '<%=request.getContextPath()%>';
				return contextPath;
			}

			//Funcion necesaria para acceder a los mensajes desde el JS
			function getMensaje(codMensaje) {
				var mensaje = "";
				switch (codMensaje) {
					case "msgErrGenServ":
						return mensaje = '<%=descriptor.getDescripcion("msgErrGenServ")%>';
				}
				return mensaje;
			}
		</script>
	</head>
	<body class="bandaBody" onload="inicializar()">
		<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titVerDatosIntercambio")%></div>
		<div class="contenidoPantalla">
			<div class="tab-pane" id="tab-pane-1">

				<script type="text/javascript">
					tp1 = new WebFXTabPane(document.getElementById("tab-pane-1"));
				</script>

				<div class="tab-page" id="tabPage1">
					<table id="tabla" style="width: 100%">
					</table>
				</div>
			</div>
		</div>
	</body>
</html:html>
