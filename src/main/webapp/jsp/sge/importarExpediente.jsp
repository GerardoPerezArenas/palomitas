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
		<TITLE>::: EXPEDIENTES  Importar Expediente:::</TITLE>
			<jsp:include page="/jsp/plantillas/Metas.jsp" />

		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
		<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script> 
		<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/jszip.min.js'/>"></script>
		
		
	</head>
	<body class="bandaBody" onload="javascript:{
				pleaseWait('off');
			}">
		<jsp:include page="/jsp/hidepage.jsp" flush="true">
			<jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
		</jsp:include>
		<form id="formImportar">
			<input type="hidden" name="opcion" id="opcionImportar" value="importarExpediente"/>
			<div class="txttitblanco"><%=descriptor.getDescripcion("tituloImportarExp")%></div>
				<div class="contenidoPantalla">
				
				<div class="etiqueta" style="margin-bottom: 5px;"><%=descriptor.getDescripcion("etiqBotonImportar")%></div>
				
				<input id="importeExpediente" type="file" name="importeExpediente" accept=".zip, application/x-compressed, application/x-zip-compressed, application/zip, multipart/x-zip">
				<br/><br/>
				<div> <%=descriptor.getDescripcion("msjTipoEntradaImport")%> </div>
				<br/>
				<div>
					<%=descriptor.getDescripcion("txtIndiceImportar")%>
					<br/>
					<%=descriptor.getDescripcion("txtDocumentoImportar")%>
				</div>
				<div class="botoneraPrincipal">
					<input id="botonLimpiar" type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> 
						name="botonLimpiar" onClick="limpiarInput();"> 
					<input id="botonImportar" type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbImportar")%> 
						name="botonImportar" onClick="procesarZip();"> 
				</div>
				<script type="text/javascript">
					var zipFile;
					var output = {};
					output.length = 0;
					var error = false;
					function capturarArchivo(evt) {
						document.getElementById('botonImportar').disabled = false;
						document.getElementById('botonLimpiar').disabled = false;
						zipFile = evt.target.files[0];
					}
					function procesarZip() {
						pleaseWait('on');
						var zip = new JSZip();
						zip.loadAsync(zipFile)
								.then(function (zip) {
									Object.keys(zip.files).forEach(function (filename) {
										zip.files[filename].async('string').then(function (fileData) {
											output.length = output.length + 1;
											if ((filename.split('.').pop()) != "xml") {
												error = true;
											}
											output[filename] = fileData
											if (output.length == Object.keys(zip.files).length) {
												if (error) {
													mostrarErrorZipInvalido();
												} else {
													transferirDatos(output);
												}
											}
										})
									});
								}, function () {
									alert("Not a valid zip file")
								});
					}
					function transferirDatos(output) {
						try {
							$.ajax({
								url: '<%=request.getContextPath()%>/ImportarExpediente.do',
								type: 'POST',
								async: true,
								data: {'importeExpediente': JSON.stringify(output), 'opcion': 'importar'},
								success: procesarRespuestaBtnExpediente,
								error: mostrarErrorBtnExpediente
							});
						} catch (Err) {
							pleaseWait('off');
							jsp_alerta("A", '<%=descriptor.getDescripcion("msgErrGenServ")%>');
						}
					}
					function procesarRespuestaBtnExpediente(ajaxResult) {
						$('body').append('<div style="display:none;">' + ajaxResult + '</div>');
						if ($('#respuesta').val() != "null") {
							alert($('#respuesta').val());
						}
						location.href = location.href;
					}
					function mostrarErrorZipInvalido() {
						alert("El zip no tiene el formato correcto");
						pleaseWait('off');
						location.href = location.href;
					}
					function mostrarErrorBtnExpediente(ajaxResult) {
						alert("Error al importar el archivo");
						pleaseWait('off');
						location.href = location.href;
					}
					
					function limpiarInput() {
						document.getElementById("importeExpediente").value = "";
						document.getElementById('botonImportar').disabled = true;
						document.getElementById('botonLimpiar').disabled = true;
					}
					
					document.getElementById('importeExpediente').addEventListener('change', capturarArchivo, false);
					document.getElementById('botonImportar').disabled = true;
					document.getElementById('botonLimpiar').disabled = true;
				</script>
				
			</div>
		</form>
	</body>
</html:html>