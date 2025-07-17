<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@	page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@	page import="java.util.ArrayList"%>
<%@	page import="java.util.List"%>
<%@	page import="es.altia.flexia.sir.model.Asiento"%>

<%
	int idioma = 1;
	int apl = 5;
	String css = "";//esto esta bien??
	UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
	if (usuarioVO != null) {
		apl = usuarioVO.getAppCod();
		idioma = usuarioVO.getIdioma();
		css=usuarioVO.getCss();//esto esta bien??

	}
	
	//Status barr??
	Config m_Config = ConfigServiceHelper.getConfig("common");
	String statusBar = m_Config.getString("JSP.StatusBar");
%>

<html>
	<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
		<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
		<title>Buzon de entrada de asientos</title>

		<!-- Estilos -->
		<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
		<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
		<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value='${sessionScope.usuario.css}'/>"/>
		<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/sir/buzonEntradaAsiento.js"></script>
	</head>
	<body class="bandaBody" onload="javascript:{
				pleaseWait('off');
				inicializar();
			}">
		<jsp:include page="/jsp/hidepage.jsp" flush="true">
			<jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
		</jsp:include>
		<form action="/buzonEntradaSir.do" method="post" id="form">
			<input type="hidden" name="opcion" id="opcionEnviarAsiento" value="enviarAsiento"/>
			<input type="hidden" id="codIntercambio" name="codIntercambio"  value="">
			<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titBuzonEntradaIntercambio")%></div>
			<div class="contenidoPantalla">
				<div id="tablaAsientos" class="xTabla compact tablaDatos"></div>
				<div class="botoneraPrincipal">
					<input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> 
						   name="botonAceptar" id="botonAceptar" onClick="pulsarAceptar();" accesskey="A"> 
					<input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbRechazar")%> 
						   name="botonCancelar" id="botonCancelar" onClick="pulsarRechazar();" accesskey="E"> 
					<input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%> 
						   name="botonGrabar" id="botonGrabar" onClick="pulsarGrabar();" accesskey="E"> 
				</div>
			</div>
		</form>

		<script type="text/javascript">
			var listaAsientos =<%=request.getAttribute("listaAsientos")%>;
			
			var tablaAsientos = new Tabla(true,
					'<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>',
					'<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>',
					'<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>',
					'<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>',
					'<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>',
					document.getElementById('tablaAsientos'));
				tablaAsientos.addColumna('300', null, "<%= descriptor.getDescripcion("etiqTablaCodIntercambio")%>");
				tablaAsientos.addColumna('300', null, "<%= descriptor.getDescripcion("etiqTablaFechaEntrada")%>");
				tablaAsientos.addColumna('300', null, "<%= descriptor.getDescripcion("etiqTablaIdentUnidRegOrigen")%>");
				tablaAsientos.addColumna('300', null, "<%= descriptor.getDescripcion("etiqTablaDescAsunto")%>");
				tablaAsientos.addColumna('300', null, "<%= descriptor.getDescripcion("etiqTablaNumExpediente")%>");
				tablaAsientos.addColumna('300', null, "<%= descriptor.getDescripcion("etiqTablaCodTipoAnotacion")%>");
				tablaAsientos.addColumna('300', null, "<%= descriptor.getDescripcion("etiqTablaListoParaEnviar")%>");
			tablaAsientos.displayCabecera = true;
			
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
	</body>
</html>

