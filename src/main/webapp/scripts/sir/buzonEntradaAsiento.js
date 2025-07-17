/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// VARIABLES GLOBALES
var filaSeleccionada;
var estadoOriginal = {};
const PRIMERA_FILA = 1; // Para evitar confusion de 0 o 1
const NOMBRE_CHECKBOX = "docSuplPreparado";
const docsSuplEnum = Object.freeze({
	OBLIGATORIOS_NO_ENTREGADOS : 0, 
	OPCIONALES_NO_ENTREGADOS : 1, 
	SIN_DOCUMENTOS_PENDIENTES : 2
});

//METODO INICIALIZAR TABLA
function inicializar() {
	window.focus();
	deshabilitarBotones();
	cargaTabla();
}

//Metodo que recorre los elemento de la lista de asientos y los mete en las filas de la tabla.
function cargarDatos() {
	let tabla = [];
	let i = PRIMERA_FILA;
	for (let asiento of listaAsientos) {
		let fila = [];
		let checked = asiento.estado === "PENDIENTE";
		let docsNecesarios = asiento.codDocFisica !== "3"; 
		let color = cargarColorFila(asiento);

		fila.push(changeColor(color, asiento.codIntercambio));
		fila.push(changeColor(color, asiento.fechaEntrada));
		fila.push(changeColor(color, asiento.codEntidadRegistralOrigen));
		fila.push(changeColor(color, asiento.descripcionAsunto));
		fila.push(changeColor(color, asiento.numExpediente));
		fila.push(changeColor(color, asiento.codTipoAnotacion));
		fila.push(prepararCheckbox(i, checked, docsNecesarios));
		tabla.push(fila);
		
		i++;
	}
	return tabla;
}

function comprobarEstadoDocumentos(estadoAsiento, codDocFisica) {
	if (estadoAsiento === "PENDIENTE") {
		return docsSuplEnum.SIN_DOCUMENTOS_PENDIENTES;
	}
	if (estadoAsiento === "FALTAN_DOCUMENTOS" && codDocFisica === "1") {
		return docsSuplEnum.OBLIGATORIOS_NO_ENTREGADOS;
	} 
	if (estadoAsiento === "FALTAN_DOCUMENTOS" && codDocFisica === "2") { 
		return docsSuplEnum.OPCIONALES_NO_ENTREGADOS;
	}
	return null; // no deberia ocurrir nunca
}

function changeColor(color, dato){
	
	let filaTablaColor = "<font color='" + color + "'>" + cargarDato(dato) + "</font>";
	return filaTablaColor;
}

function cargarDato(dato) {
	return ((!dato || dato === "null") ? "" : dato);
}

function cargarColorFila(asiento){
	let color = "#000000";
	let estadoDocumentos = comprobarEstadoDocumentos(asiento.estado, asiento.codDocFisica);
	if(estadoDocumentos === docsSuplEnum.OBLIGATORIOS_NO_ENTREGADOS){
		color = "#FF0000";
	}else if(estadoDocumentos === docsSuplEnum.OPCIONALES_NO_ENTREGADOS){
		color = "#FFBF00";
	}
	return color;
}


function prepararCheckbox(indice, checked, docsNecesarios) {
	let nombreCheckbox = NOMBRE_CHECKBOX + indice;
	let check = createCheckbox(nombreCheckbox, checked, "toggleCheckDocSupl", !docsNecesarios);
	estadoOriginal[nombreCheckbox] = checked;
	return check;
}

function createCheckbox(nombre, checked, onClick, disabled) {
	let checkbox = "<input type='checkbox' class='check' " +
			"name='" + nombre + "'" +
			"id='" + nombre + "'" +
			((checked) ? " CHECKED " : "") +
			"onclick='javascript:"+ onClick + "();'" +
			((disabled) ? " disabled " : "") +
			">";
	
	return checkbox;
}

function getFilasCambiadas() {
	let asientos = [];
	let i = PRIMERA_FILA;
	for (asiento of listaAsientos) {
		let asientoTmp = {
			codIntercambio : asiento.codIntercambio
		};
		let id = "#" + NOMBRE_CHECKBOX + i;
		let checkActual = $(id).is(":checked");
		if (checkActual !== estadoOriginal[id.substr(1)]) {
			if (checkActual) {
				asientoTmp.estado = "PENDIENTE";
			} else {
				if (asiento.codDocFisica !== "1") {
					asientoTmp.estado = "PENDIENTE";
				} else {
					asientoTmp.estado = "FALTAN_DOCUMENTOS";
				}
			}
			asientos.push(asientoTmp);
		}
		i++;
	}
	return asientos;
}

function toggleCheckDocSupl() {
	let estadosIguales = true;
	for (i = PRIMERA_FILA; i <= tablaAsientos.lineas.length; i++) {
		let id = "#" + NOMBRE_CHECKBOX + i;
		let checkActual = $(id).is(":checked");
		estadosIguales = (estadosIguales && checkActual === estadoOriginal[id.substr(1)]);
	}
	
	if (estadosIguales) {
		deshabilitarGeneral($("#botonGrabar"));
	} else {
		habilitarGeneral($("#botonGrabar"));
	};
}

function cargaTabla() {
	tablaAsientos.lineas = cargarDatos();
	refresca(tablaAsientos);
	deshabilitarGeneral($("#botonGrabar"));
	
}

// BOTONES
function habilitarBotones() {
	habilitarGeneral($("#botonAceptar"));
	habilitarGeneral($("#botonCancelar"));
}

function deshabilitarBotones() {
	deshabilitarGeneral($("#botonAceptar"));
	deshabilitarGeneral($("#botonCancelar"));
}

//FUNCIONES TABLA
function refresca(tabla) {
	tabla.displayTabla();
}

function limpiarTabla() {
	tablaAsientos.lineas = new Array();
	refresca(tablaAsientos);
}

function rellenarDatos(tableName, rowID) {
	if (tablaAsientos.selectedIndex === -1) {
		deshabilitarBotones();
	} else {
		habilitarGeneral($("#botonCancelar"));
		let asiento = listaAsientos[tablaAsientos.selectedIndex];
		if (comprobarEstadoDocumentos(asiento.estado, asiento.codDocFisica) === docsSuplEnum.OBLIGATORIOS_NO_ENTREGADOS) {
			deshabilitarGeneral($("#botonAceptar"));
		} else {
			habilitarGeneral($("#botonAceptar"));
		}
		filaSeleccionada = tablaAsientos.selectedIndex;
	}
}

function getSelectedCodIntercambio() {
	return listaAsientos[filaSeleccionada].codIntercambio;
}

//EVENTO DE LA TABLA
function callFromTableTo(rowID, tableName) {
	var source = getContextPath() + "/buzonEntradaSir.do?opcion=visualizarRegistro&codigoIntercambio=" + getSelectedCodIntercambio();
	abrirXanelaAuxiliar(getContextPath() + "/jsp/sge/mainVentana.jsp?source=" + source, null
			, 'width=1850,height=680,status=' +
			'<%=statusBar%>', function () {});
}

//EVENTO DE LOS BOTONES
function pulsarAceptar() {
	if (tablaAsientos.selectedIndex !== -1) {
		pleaseWait('on');
		try {
			$.ajax({
				url: getContextPath() + '/buzonEntradaSir.do',
				type: 'POST',
				async: true,
				data: {"codigoIntercambio": getSelectedCodIntercambio(), 'opcion': 'aceptar'},
				success:function (response) { 
						procesarSuccessBoton(response);
					},
				error: procesarErrorBoton
			});
		} catch (Err) {
			pleaseWait('off');
			jsp_alerta("A", getMensaje("msgErrGenServ"));
		}
	} else {
		alert("No hay ningún campo seleccionado");
	}
}

function pulsarGrabar() {
	pleaseWait("on");
	try {
		$.ajax({
			url: getContextPath() + "/buzonEntradaSir.do",
			type: "POST",
			async: true,
			data: {
				"asientosCambiados": JSON.stringify(getFilasCambiadas()), 
				"opcion": "actualizarEstadoAsiento"
			},
			success: function (response) {
				procesarSuccessBoton(response);
			},
			error: procesarErrorBoton
		});
	} catch(error) {9
		pleaseWait("off");
		jsp_alerta("A", getMensaje("msgErrGenServ"));
	}
}

function pulsarRechazar() {
	var source = getContextPath() + "/buzonEntradaSir.do?";
	abrirXanelaAuxiliar(getContextPath() + "/jsp/registro/sir/motivoRechazo.jsp?source=" + source, null
			, 'width=900,height=350,status=' +
			'<%=statusBar%>', function (respuesta) {
				if (respuesta != null) {
					respuestaRechazar(respuesta);
				}
			});
}

function respuestaRechazar(motivoRechazo) {
	if (tablaAsientos.selectedIndex !== -1) {
		pleaseWait('on');
		try {
			$.ajax({
				url: getContextPath() + '/buzonEntradaSir.do',
				type: 'POST',
				async: true,
				data: {	'codigoIntercambio'	: getSelectedCodIntercambio(),
						'descTipoAnotacion'	: motivoRechazo,
						'opcion'			: 'rechazar'},
				success: function (response) {
					procesarSuccessBoton(response);
					},
				error: procesarErrorBoton
			});
		} catch (Err) {
			pleaseWait('off');
			jsp_alerta("A", getMensaje("msgErrGenServ"));
		}
	} else {
		alert("No hay ningún campo seleccionado");
	}
}

//RESPUESTA DE LOS BOTONES
function procesarErrorBoton() {
	pleaseWait('off');
	alert("Ha habido un error de conexión");
}

function procesarSuccessBoton(response) {
	pleaseWait('off');
	jsp_alerta("A", response);
	location.reload();
}




