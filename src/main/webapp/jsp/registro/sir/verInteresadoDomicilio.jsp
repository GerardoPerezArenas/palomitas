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
		<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titInfoInteresado")%></div>
		<div class="contenidoPantalla">
			<table>
				<!-- TABLA DATOS DE INTERESADOS-->
				<tr>
					<td colspan="6" class="sub3titulo"><%=descriptor.getDescripcion("etiqTablaDocumentoInteresado")%></td>
				</tr><tr>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoTipoDocId")%></span>														</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="tipoDocumentoIdentificacion" name="tipoDocumentoIdentificacion" disabled="">		</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoDocumentoId")%></span>													</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="documentoIdentificacion" name="documentoIdentificacion" disabled="">				</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoNombreRazon")%></span>													</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="nombreRazonSocial" name="nombreRazonSocial" disabled="">							</td>
				</tr><tr>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoCorreoElectr")%></span>											</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="correoElectronico" name="correoElectronico" disabled="">							</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoTelefono")%></span>												</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%"id="telefono" name="telefono" disabled="">												</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoDirElectrHab")%></span>											</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="direccionElectronicaHabilitada" name="direccionElectronicaHabilitada" disabled=""></td>
				</tr><tr>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoCanalComuni")%></span>											</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="canalPreferenteComunicacion" name="canalPreferenteComunicacion" disabled="">		</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqInteresadoObservaciones")%></span>										</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="observaciones" name="observaciones" disabled="">									</td>
				</tr><tr>
					<!-- TABLA DATOS DE DOMICILIO-->
					<td colspan="6" class="sub3titulo"><%=descriptor.getDescripcion("etiqTablaDomicilioInteresado")%></td>
				</tr><tr>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDomicilioPais")%> </span>													</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="pais" name="pais" disabled="">													</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDomicilioProvincia")%></span>												</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="provincia" name="provincia" disabled="">											</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDomicilioMunicipio")%></span>												</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="municipio" name="municipio" disabled="">											</td>
				</tr><tr>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDomicilioDireccion")%></span>												</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="direccion" name="direccion" disabled="">											</td>
					<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDomicilioCodPostal")%></span>												</td>
					<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codPostal" name="codPostal" disabled="">											</td>
				</tr>
			</table>
		</div>
	</body>
</html:html>
<script type="text/javascript">
	
function get(dato) {
	return (!dato) ? "" : dato;
}

	var argVentana = self.parent.opener.xanelaAuxiliarArgs;
	var interesado = argVentana[0];
	
//	INTERESADO
	document.getElementById("canalPreferenteComunicacion").value += get(interesado.canalPreferenteComunicacion);
	document.getElementById("correoElectronico").value += get(interesado.correoElectronico);
	document.getElementById("direccionElectronicaHabilitada").value += get(interesado.direccionElectronicaHabilitada);
	document.getElementById("documentoIdentificacion").value += get(interesado.documentoIdentificacion);
	document.getElementById("nombreRazonSocial").value += get(interesado.nombreRazonSocial);
	document.getElementById("telefono").value += get(interesado.telefono);
	document.getElementById("tipoDocumentoIdentificacion").value += get(interesado.tipoDocumentoIdentificacion);
	document.getElementById("observaciones").value += get(interesado.observaciones);
//	DOMICILIO
	document.getElementById("codPostal").value += get(interesado.domicilio.codPostal);
	document.getElementById("direccion").value += get(interesado.domicilio.direccion);
	document.getElementById("municipio").value += get(interesado.domicilio.municipio);
	document.getElementById("pais").value += get(interesado.domicilio.pais);
	document.getElementById("provincia").value += get(interesado.domicilio.provincia);
	
</script>
