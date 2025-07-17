function inicializar() {
	cargarXml((xml) => {
		var parser = new DOMParser();
		console.log(xml);
		var xml_doc = parser.parseFromString(xml, "application/xml");
		var json = ficheroSicresToJson(xml_doc);
		console.log(json);
		crearHtml(json);
	});
	const xml = '<Fichero_Intercambio_SICRES_3>  <De_Origen_o_Remitente>    <Codigo_Entidad_Registral_Origen>O00008826</Codigo_Entidad_Registral_Origen>    <Decodificacion_Entidad_Registral_Origen>REXISTRO XERAL DA XUNTA DE GALICIA</Decodificacion_Entidad_Registral_Origen>    <Numero_Registro_Entrada>2018/82761</Numero_Registro_Entrada>    <Fecha_Hora_Entrada>20181029095728</Fecha_Hora_Entrada>    <Timestamp_Entrada/>    <Codigo_Unidad_Tramitacion_Origen>A12017436</Codigo_Unidad_Tramitacion_Origen>    <Decodificacion_Unidad_Tramitacion_Origen>Axencia Galega de Infraestruturas</Decodificacion_Unidad_Tramitacion_Origen>  </De_Origen_o_Remitente>  <De_Destino>    <Codigo_Entidad_Registral_Destino>O00003204</Codigo_Entidad_Registral_Destino>    <Decodificacion_Entidad_Registral_Destino>Registro del Defensor del Pueblo</Decodificacion_Entidad_Registral_Destino>    <Codigo_Unidad_Tramitacion_Destino/>    <Decodificacion_Unidad_Tramitacion_Destino/>  </De_Destino>  <De_Interesado>    <Tipo_Documento_Identificacion_Interesado>O</Tipo_Documento_Identificacion_Interesado>    <Documento_Identificacion_Interesado>A12017436</Documento_Identificacion_Interesado>    <Razon_Social_Interesado/>    <Nombre_Interesado/>    <Primer_Apellido_Interesado/>    <Segundo_Apellido_Interesado/>    <Tipo_Documento_Identificacion_Representante/>    <Documento_Identificacion_Representante/>    <Razon_Social_Representante/>    <Nombre_Representante/>    <Primer_Apellido_Representante/>    <Segundo_Apellido_Representante/>    <Pais_Interesado/>    <Provincia_Interesado/>    <Municipio_Interesado/>    <Direccion_Interesado/>    <Codigo_Postal_Interesado/>    <Correo_Electronico_Interesado/>    <Telefono_Contacto_Interesado/>    <Direccion_Electronica_Habilitada_Interesado/>    <Canal_Preferente_Comunicacion_Interesado/>    <Pais_Representante/>    <Provincia_Representante/>    <Municipio_Representante/>    <Direccion_Representante/>    <Codigo_Postal_Representante/>    <Correo_Electronico_Representante/>    <Telefono_Contacto_Representante/>    <Direccion_Electronica_Habilitada_Representante/>    <Canal_Preferente_Comunicacion_Representante/>    <Observaciones/>  </De_Interesado>  <De_Asunto>    <Resumen>NÂº EXPEDIENTE:	16543651345 GABINO QUINTANILLA</Resumen>    <Codigo_Asunto_Segun_Destino/>    <Referencia_Externa/>    <Numero_Expediente/>  </De_Asunto>  <De_Anexo>    <Nombre_Fichero_Anexado>Oficio Defensor Pueblo.pdf</Nombre_Fichero_Anexado>    <Identificador_Fichero>O00008826_18_10082761_01_0001.pdf</Identificador_Fichero>    <Validez_Documento>04</Validez_Documento>    <Tipo_Documento>02</Tipo_Documento>    <Certificado/>    <Firma_Documento/>    <TimeStamp/>    <Validacion_OCSP_Certificado/>    <Hash>MjdjN2UxZmRiMDNiNjNkZTNmYzJmNTRjZTNjYmRmZGI3MjUxMWFhNw==</Hash>    <Tipo_MIME>application/pdf</Tipo_MIME>    <Anexo/>    <Identificador_Documento_Firmado>O00008826_18_10082761_01_0001.pdf</Identificador_Documento_Firmado>    <Observaciones/>  </De_Anexo>    <De_Anexo>    <Nombre_Fichero_Anexado>reciboAnotacion.pdf</Nombre_Fichero_Anexado>    <Identificador_Fichero>O00008826_18_10082761_01_0002.pdf</Identificador_Fichero>    <Validez_Documento>04</Validez_Documento>    <Tipo_Documento>02</Tipo_Documento>    <Certificado/>    <Firma_Documento/>    <TimeStamp/>    <Validacion_OCSP_Certificado/>    <Hash>ZjcyMDc4YTQ0ZWVkNjdlMmRjYWIzZjQxZGE0YmE5M2M0NDkyMWY1Mg==</Hash>    <Tipo_MIME>application/pdf</Tipo_MIME>    <Anexo/>    <Identificador_Documento_Firmado>O00008826_18_10082761_01_0002.pdf</Identificador_Documento_Firmado>    <Observaciones>Recibo Anotacion 29/10/2018 14:21:05</Observaciones>  </De_Anexo>  <De_Anexo>    <Nombre_Fichero_Anexado>reciboAnotacion.pdf</Nombre_Fichero_Anexado>    <Identificador_Fichero>O00008826_18_10082761_01_0003.pdf</Identificador_Fichero>    <Validez_Documento>04</Validez_Documento>    <Tipo_Documento>02</Tipo_Documento>    <Certificado/>    <Firma_Documento/>    <TimeStamp/>    <Validacion_OCSP_Certificado/>    <Hash>OTk0ODg0YWVhNjIxNTM0OWM4ZDUxYzNmNzIyYzY2YzE3MjEzMTA1OQ==</Hash>    <Tipo_MIME>application/pdf</Tipo_MIME>    <Anexo/>    <Identificador_Documento_Firmado>O00008826_18_10082761_01_0003.pdf</Identificador_Documento_Firmado>    <Observaciones>Recibo Anotacion 26/10/2018 14:47:31</Observaciones>  </De_Anexo>  <De_Internos_Control>    <Tipo_Transporte_Entrada/>    <Numero_Transporte_Entrada/>    <Nombre_Usuario>Usuario de administrador</Nombre_Usuario>    <Contacto_Usuario/>    <Identificador_Intercambio>O00008826_18_10082761</Identificador_Intercambio>    <Aplicacion_Version_Emisora/>    <Tipo_Anotacion>02</Tipo_Anotacion>    <Descripcion_Tipo_Anotacion>RECTIFICA LA ENTRADA 2018/82332</Descripcion_Tipo_Anotacion>    <Tipo_Registro>1</Tipo_Registro>    <Documentacion_Fisica>3</Documentacion_Fisica>    <Observaciones_Apunte/>    <Indicador_Prueba>0</Indicador_Prueba>    <Codigo_Entidad_Registral_Inicio>O00008826</Codigo_Entidad_Registral_Inicio>    <Decodificacion_Entidad_Registral_Inicio>REXISTRO XERAL DA XUNTA DE GALICIA</Decodificacion_Entidad_Registral_Inicio>  </De_Internos_Control>  <De_Formulario_Generico>    <Expone/>    <Solicita/>  </De_Formulario_Generico></Fichero_Intercambio_SICRES_3>';

}

function cargarXml(callback) {
	var argVentana = self.parent.opener.xanelaAuxiliarArgs;
	var codIntercambio = argVentana[0];
	try {
		$.ajax({
			url: getContextPath() + '/buzonEntradaSir.do',
			type: 'POST',
			async: true,
			data: {
				"codigoIntercambio": codIntercambio,
				"opcion": "cargarFicheroIntercambio"},
			success: callback,
			error: procesarErrorBoton
		});
	} catch (Err) {
		jsp_alerta("A", getMensaje("msgErrGenServ"));
	}
}

//RESPUESTA DE LOS BOTONES
function procesarErrorBoton() {
	alert("Ha habido un error de conexión");
}


function crearHtml(jsonFichero, numFila) {
	var toggleCrearFila = true;
	numFila = (numFila) ? numFila : 1;
	var idFila = "";
	for (clave in jsonFichero) {
		if (typeof (jsonFichero[clave]) === "object") {
			if (clave.substring(0, 3).toUpperCase() === "DE_") {
				// Comparar con titulos de secciones como De_Anexo
				crearSeccion(clave);
			}
			numFila = crearHtml(jsonFichero[clave], numFila);
		} else {
			if (jsonFichero[clave].trim()) {
				if (toggleCrearFila) {
					idFila = crearFila(numFila);
					numFila++;
				}
				toggleCrearFila = !toggleCrearFila;
				insertarEntrada(clave, jsonFichero[clave], idFila);
			}
		}
	}
	return numFila;
}

function crearSeccion(titulo) {
	let html = '<tr>' +
			'<td class="sub3titulo" colspan="6">' + presentarTexto(titulo) + '</td>' +
			'</tr>';
	$(html).appendTo("#tabla");
}

function insertarEntrada(clave, valor, idFila) {
	let html = '<td><span  class="etiqueta" style="margin-left: 1%;">' + presentarTexto(clave) + '</span>																  </td>' +
			'<td style="width:35%;"><input class="inputTextoDeshabilitado" type="text" style="width:100%;" id="codAsiento" name="codAsiento" disabled="true" value=' + valor + '></td>'
	$(html).appendTo("#" + idFila);
}

function crearFila(numFila) {
	let id = "fila" + numFila;
	let html = '<tr id="' + id + '"></tr>'
	$(html).appendTo("#tabla");
	return id;
}

function presentarTexto(clave) {
	return clave.split("_").join(" ");
}

function ficheroSicresToJson(ficheroSicres) {
	// Create the return object
	var json = {};
	if (ficheroSicres.nodeType === 3) { // text
		json = ficheroSicres.nodeValue;
	}
	if (ficheroSicres.hasChildNodes() && ficheroSicres.childNodes.length === 1 && ficheroSicres.childNodes[0].nodeType === 3) {
		json = ficheroSicres.childNodes[0].nodeValue;
	} else if (ficheroSicres.hasChildNodes()) {
		for (var i = 0; i < ficheroSicres.childNodes.length; i++) {
			var item = ficheroSicres.childNodes.item(i);
			var nodeName = item.nodeName;
			if (item.nodeType !== 3 || item.nodeValue.trim()) {
				if (typeof (json[nodeName]) === "undefined") {
					let resultado = ficheroSicresToJson(item);
					if (!$.isEmptyObject(resultado)) {
						json[nodeName] = resultado;
					}
				} else {
					if (typeof (json[nodeName].push) === "undefined") {
						var old = json[nodeName];
						json[nodeName] = [];
						json[nodeName].push(old);
					}
					json[nodeName].push(ficheroSicresToJson(item));
				}
			}
		}
	}
	return json;
}
