/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function inicializar() {
	window.focus();
	deshabilitarGeneral($("#botonVerInteresado"));
	cargaTablas();
}

function rellenarDatos(tableName, rowID) {
	if (tablaInteresadosDomicilio.selectedIndex === -1) {
		deshabilitarGeneral($("#botonVerInteresado"));
	} else {
		habilitarGeneral($("#botonVerInteresado"));
	}
}

function cargarDatosAnexo() {
	let tabla = [];
	for (let anexos of asientosSeleccionado.anexos) {
		let fila = [];
		fila.push(get(anexos.nombreFichero));
		fila.push(get(anexos.identificadorFichero));
		fila.push(get(anexos.validezDocumento));
		fila.push(get(anexos.tipoDocumento));
		fila.push(get(anexos.firmaDocumento));
		fila.push(get(anexos.tipoMIME));
		fila.push(get(anexos.identificadorDocumentoFirmado));
		fila.push(get(anexos.observaciones));
		tabla.push(fila);
	}
	return tabla;
}


function cargarDatosInteresados() {
	let tabla = [];
	for (let interesados of asientosSeleccionado.interesados) {
		let fila = [];
		fila.push(get(interesados.tipoDocumentoIdentificacion));
		fila.push(get(interesados.documentoIdentificacion));
		fila.push(get(interesados.nombreRazonSocial));
		tabla.push(fila);
	}
	return tabla;
}

function cargaTablas() {
	tablaAnexos.lineas = cargarDatosAnexo();
	tablaInteresadosDomicilio.lineas = cargarDatosInteresados();
	refresca(tablaAnexos);
	refresca(tablaInteresadosDomicilio);
}

function refresca(tabla) {
	tabla.displayTabla();
}

function verDatosInteresado() {
	var datos = new Array();
	datos[0] = asientosSeleccionado.interesados[tablaInteresadosDomicilio.selectedIndex];
	var source = getContextPath() + "/buzonEntradaSir.do?";
	abrirXanelaAuxiliar(getContextPath() + "/jsp/registro/sir/verInteresadoDomicilio.jsp?source=" + source, datos
			, 'width=900,height=580,status=' +
			'<%=statusBar%>', function (datos) {});
}

function verDatosIntercambio() {
	var datos = new Array();
	datos[0] = asientosSeleccionado.codIntercambio;
	var source = getContextPath() + "/buzonEntradaSir.do?";
	abrirXanelaAuxiliar(getContextPath() + "/jsp/registro/sir/verXmlAsiento.jsp?source=" + source, datos
			, 'width=900,height=580,status=' +
			'<%=statusBar%>', function (datos) {});
}

function verDocumentoAnexo() {
	var datosAnexo = new Array();
	datosAnexo[0] = asientosSeleccionado.anexos[tablaAnexos.selectedIndex];
	//pleaseWait('on');
	try{
		$.ajax({
			url: getContextPath() + '/buzonEntradaSir.do',
			type: 'POST',
			async: true,
			data: {	'opcion': 'recuperarFicheroAnexoVisualizar', 
					'datosAnexo': asientosSeleccionado.anexos[tablaAnexos.selectedIndex].identificadorFichero},
			success: procesarRespuestaVerResultados,
			error: errorEnLaConexion 
		}); 
		} catch (Err) {
			//pleaseWait('off');
			jsp_alerta("A", getMensaje("msgErrGenServ"));
		}
}

function errorEnLaConexion(){
	//pleaseWait('off');
	jsp_alerta("A", getMensaje("msgErrGenServ"));

}