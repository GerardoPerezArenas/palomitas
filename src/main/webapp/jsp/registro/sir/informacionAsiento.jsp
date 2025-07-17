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


<html>
	<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
		<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
		<TITLE>:::Buzon Entrada de asiento</TITLE>

		<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
		<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
		<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">


		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/sir/informacionAsiento.js"></script>
	</head>
	<body class="bandaBody" onload="inicializar();">
		<form name="formulario" method="post">
			<input type="hidden"  name="nombreDocumento"/>
			<input type="hidden"  name="contenidoDocumento"/>
			<input type="hidden"  name="opcion"/>


			<!-- TODO insertar en base de datos un nombre mejor que este -->
			<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titInfoAsiento")%></div>
			<div class="contenidoPantalla">
				<div class="tab-pane" id="tab-pane-1">

					<script type="text/javascript">
						tp1 = new WebFXTabPane(document.getElementById("tab-pane-1"));
					</script>

					<div class="tab-page" id="tabPage1">
						<h2 class="tab"><%=descriptor.getDescripcion("etiqAsiento")%></h2>
						<table style="width: 100%">
							<!-- TABLA DATOS ASIENTO EN LA TABLA -->
							<tr>
								<td class="sub3titulo" colspan="6"><%=descriptor.getDescripcion("etiqTablaInfoAsiento")%></td>
							</tr>
							<tr><!-- Codigo asiento | Codigo entidad registral origen | Descripcion entidad registral origen -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodAsiento")%></span>									</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%;" id="codAsiento" name="codAsiento" disabled="">					</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodEntidadRegOrigen")%></span>						</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codEntidadRegOrigen" name="codEntidadRegOrigen" disabled="">	</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDescEntidadRegOrigen")%></span>						</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="descripcionEntidadRegistralOrigen" disabled="">				</td>
							</tr><tr><!-- Codigo asunto | Codigo tipo de anotacion | Tipo de anotacion -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodAsunto")%></span>									</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codAsunto" name="codAsunto" disabled="">						</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodTipoAnot")%></span>								</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codTipoAnotacion" disabled="">								</td>
								<td><span  class="etiqueta"style="margin-left: 1%"><%=descriptor.getDescripcion("etiqTipoAnotacion")%></span>								</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="tipoAnot" name="idUndRegOrigen" disabled="">					</td>
							</tr><tr><!-- Numero de expediente | Fecha de entrada |  -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqNumExpediente")%></span>								</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="numExpediente" name="numExpediente" disabled="">				</td>
								<td><span  class="etiqueta" style="margin-left: 1%" ><%=descriptor.getDescripcion("etiqFecEntrada")%></span>								</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="fecEntrada" name="fecEntrada" disabled="">					</td>
							</tr>
							<!-- TABLA DATOS DE VISUALIZACIÓN DE ASIENTO -->
							<tr>
								<td class="sub3titulo" colspan="6"><%=descriptor.getDescripcion("etiqTablaInfoAdiAsiento")%></td>
							</tr><tr><!-- Codigo tipo de registro | Codigo entidad registral inicio | Descripcion entidad registral inicio -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodTipoReg")%></span>									</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codTipoRegistro" disabled="">									</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodEntidadRegInicio")%></span>						</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codEntidadRegistralInicio" disabled="">						</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDescEntidadRegInicio")%></span>						</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="descripcionEntidadRegistralInicio" disabled="">				</td>
							</tr><tr><!-- Nombre de usuario | Codigo entidad registral destino | Descripcion entidad registral destino -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqNomUsuario")%></span>									</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="nombreUsuario" disabled="">									</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodEntidadRegDestino")%></span>						</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codEntidadRegistralDestino" disabled="">						</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDescEntidadRegDestino")%></span>						</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="descripcionEntidadRegistralDestino" disabled="">				</td>
							</tr><tr><!-- Numero registro de entrada | Codigo unidad tramitadora origen | Descripcion unidad tramitadora origen -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqNumRegEnt")%></span>									</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="numRegistroEntrada" disabled="">								</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodUnidTramOrigen")%></span>							</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codUnidadTramitadoraOrigen" disabled="">						</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDescUnidTramOrigen")%></span>							</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="descripcionUnidadTramitadoraOrigen" disabled="">				</td>
							</tr><tr><!-- Contacto de usuario | Codigo unidad tramitadora destino | Descripción unidad tramitadora destino -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqContactoUsuario")%></span>							</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="contactoUsuario" disabled="">									</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodUnidTramDestino")%></span>							</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codUnidadTramitadoraDestino" disabled="">						</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqDescUnidTramDestino")%></span>						</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="descripcionUnidadTramitadoraDestino" disabled="">				</td>
							</tr><tr><!-- Codigo de intercambio | Codigo tipo de transporte | Numero de transporte -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodIntercambio")%></span>								</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codIntercambio" disabled="">									</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodTipoTransporte")%></span>							</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codTipoTransporte" disabled="">								</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqNumTransporte")%></span>								</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="numTransporte" disabled="">									</td>
							</tr><tr><!-- Estado | Codigo documentación fisica | Version de la aplicacion -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqEstado")%></span>										</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="estado" disabled="">											</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqCodDocFis")%></span>									</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="codDocFisica" disabled="">									</td>
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqVersionAplicacion")%></span>							</td>
								<td><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="appVersion" disabled="">										</td>
							</tr><tr><!-- Resumen -->
								<td><span  class="etiqueta" style="margin-left: 1%"><%=descriptor.getDescripcion("etiqResumen")%></span>									</td>
								<td colspan="5"><input class="inputTextoDeshabilitado" type="text" style="width:100%" id="resumen" disabled="">								</td>
							</tr><tr><!-- Expone | Solicita -->
								<td colspan="3">
									<div style="float:left;width:100%">
										<span class="etiqueta"><%=descriptor.getDescripcion("etiqExpone")%></span>
										<span class="columnP">
											<textarea class="capaTextoLargo" name="expone" cols="74" rows="5"
													  style="width:99%" id="expone" disabled="">
											</textarea>
										</span>
									</div>
								</td>
								<td colspan="3">
									<div style="float:left;width:100%">
										<span class="etiqueta"><%=descriptor.getDescripcion("etiqSolicita")%></span>
										<span class="columnP">
											<textarea class="capaTextoLargo" name="solicita" cols="74" rows="5"
													  style="width:99%" id="solicita" disabled=""/>
											</textarea>
										</span>
									</div>
								</td>
							</tr>

						<tr>
							<td> 
								<div class="botoneraPrincipal">
									<input name="botonVerDatosIntercambio"  id="botonVerDatosIntercambio" class='botonGeneral' type="button" 
										   onclick="verDatosIntercambio()" value="<%=descriptor.getDescripcion("gbVerDatosIntercambio")%>" >
								</div>
							</td>
						</tr>
						</table>
					</div>
					<!-- TABLA VISUALIZACION DE INTERESADOS Y DOMICILIOS -->
					<div class="tab-page" id="tabPage2"  >
						<h2 class="tab"><%=descriptor.getDescripcion("etiqVerInteresadosYDomicilios")%></h2>
						<table>
							<tr>
								<td id="tablaInteresadosDomicilio" class="xTabla compact tablaDatos"> <!-- Procesos -->
							</tr>
							<tr>
								<td> 
									<div class="botoneraPrincipal">
										<input name="botonVerInteresado"  id="botonVerInteresado" class='botonGeneral' type="button" 
											   onclick="verDatosInteresado()" value="<%=descriptor.getDescripcion("gbVerInteresado")%>" >
									</div>
								</td>
							</tr>
						</table>
					</div>
					<!-- TABLA VISUALIZACION DE ANEXOS -->
					<div class="tab-page" id="tabPage3"  >
						<h2 class="tab"><%=descriptor.getDescripcion("etiqVerAnexos")%></h2>
						<table>
							<tr>
								<td id="tablaAnexos" class="xTabla compact tablaDatos"> <!-- Procesos -->
							</tr>
							<tr>
								<td> 
									<div class="botoneraPrincipal">
										<input name="botonVerAnexo"  id="botonVerAnexo" class='botonGeneral' type="button" 
											   onclick="verDocumentoAnexo()" value="<%=descriptor.getDescripcion("gbVerAnexo")%>" >
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</form>
	</body>
</html>

<script type="text/javascript">

function get(dato) {
	return (!dato) ? "" : dato;
}

//ASIENTOS
	var asientosSeleccionado =<%=request.getAttribute("asientosSeleccionado")%>;
	document.getElementById("appVersion").value += get(asientosSeleccionado.appVersion);
	document.getElementById("codAsiento").value += get(asientosSeleccionado.codAsiento);
	document.getElementById("codAsunto").value += get(asientosSeleccionado.codAsunto);
	document.getElementById("codDocFisica").value += get(asientosSeleccionado.codDocFisica);
	document.getElementById("codEntidadRegistralDestino").value += get(asientosSeleccionado.codEntidadRegistralDestino);
	document.getElementById("codEntidadRegistralInicio").value += get(asientosSeleccionado.codEntidadRegistralInicio);
	document.getElementById("codIntercambio").value += get(asientosSeleccionado.codIntercambio);
	document.getElementById("codTipoAnotacion").value += get(asientosSeleccionado.codTipoAnotacion);
	document.getElementById("codTipoRegistro").value += get(asientosSeleccionado.codTipoRegistro);
	document.getElementById("codTipoTransporte").value += get(asientosSeleccionado.codTipoTransporte);
	document.getElementById("codUnidadTramitadoraDestino").value += get(asientosSeleccionado.codUnidadTramitadoraDestino);
	document.getElementById("codUnidadTramitadoraOrigen").value += get(asientosSeleccionado.codUnidadTramitadoraOrigen);
	document.getElementById("contactoUsuario").value += get(asientosSeleccionado.contactoUsuario);
	document.getElementById("descripcionEntidadRegistralInicio").value += get(asientosSeleccionado.descripcionEntidadRegistralInicio);
	document.getElementById("descripcionEntidadRegistralOrigen").value += get(asientosSeleccionado.descripcionEntidadRegistralOrigen);
	document.getElementById("descripcionEntidadRegistralDestino").value += get(asientosSeleccionado.descripcionEntidadRegistralDestino);
	document.getElementById("descripcionUnidadTramitadoraDestino").value += get(asientosSeleccionado.descripcionUnidadTramitadoraDestino);
	document.getElementById("descripcionUnidadTramitadoraOrigen").value += get(asientosSeleccionado.descripcionUnidadTramitadoraOrigen);
	document.getElementById("estado").value += get(asientosSeleccionado.estado);
	document.getElementById("expone").value += get(asientosSeleccionado.expone);
	document.getElementById("fecEntrada").value += get(asientosSeleccionado.fechaEntrada);
	document.getElementById("codEntidadRegOrigen").value += get(asientosSeleccionado.codEntidadRegistralOrigen);
	document.getElementById("nombreUsuario").value += get(asientosSeleccionado.nombreUsuario);
	document.getElementById("numExpediente").value += get(asientosSeleccionado.numExpediente);
	document.getElementById("numRegistroEntrada").value += get(asientosSeleccionado.numRegistroEntrada);
	document.getElementById("numTransporte").value += get(asientosSeleccionado.numTransporte);
	document.getElementById("resumen").value += get(asientosSeleccionado.resumen);
	document.getElementById("solicita").value += get(asientosSeleccionado.solicita);
	document.getElementById("tipoAnot").value += get(asientosSeleccionado.tipoAnotacion);

//TABLA DE ANEXOS
	var tablaAnexos = new Tabla(true,
			'<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>',
			'<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>',
			'<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>',
			'<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>',
			'<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>',
			document.getElementById('tablaAnexos'));
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaNomFichero")%>");
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaIdFichero")%>");
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaValidezDocumento")%>");
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaTipoDocumento")%>");
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaFirmaDocumento")%>");
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaTipoMime")%>");
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaIdDocumentoFirmado")%>");
	tablaAnexos.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaObservaciones")%>");
	tablaAnexos.displayCabecera = true;

//TABLA DE INTERESADOS Y DOMICILIOS
	var tablaInteresadosDomicilio = new Tabla(true,
			'<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>',
			'<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>',
			'<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>',
			'<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>',
			'<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>',
			document.getElementById('tablaInteresadosDomicilio'));
	tablaInteresadosDomicilio.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaTipoDocIdent")%>");
	tablaInteresadosDomicilio.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaDocIdent")%>");
	tablaInteresadosDomicilio.addColumna('300', null, "<%=descriptor.getDescripcion("etiqTablaNombreORazon")%>");
	tablaInteresadosDomicilio.displayCabecera = true;

	function refresca(tabla) {
		tabla.displayTabla();
	}

	//Funcion necesaria para poder acceder al ContextPath desde el JS
	function getContextPath() {
		var contextPath = '<%=request.getContextPath()%>';
		return contextPath;
	}


	function procesarRespuestaVerResultados(result) {
		//pleaseWait('on');
		if (result) {
			var resultado = JSON.parse(result);
			if (resultado != undefined && resultado.error != undefined && resultado.error.length > 0) {
				jsp_alerta("A", datos.error)
			} else {
				document.forms[0].nombreDocumento.value = resultado.nombreDocumento;
				document.forms[0].contenidoDocumento.value = resultado.contenidoDocumento;
				document.forms[0].opcion.value = "visualizarDocumentoAnexo";
				document.forms[0].target = "mainFrame";
				document.forms[0].action = "<c:url value='/buzonEntradaSir.do'/>";
				document.forms[0].submit();
			}
		} else {
			jsp_alerta("A", '<%=descriptor.getDescripcion("msgErrGenServ")%>');
		}
		//pleaseWait('off');
	}


</script>