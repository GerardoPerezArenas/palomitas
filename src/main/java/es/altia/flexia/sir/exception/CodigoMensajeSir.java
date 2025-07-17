package es.altia.flexia.sir.exception;

public enum CodigoMensajeSir {
	// Errores busqueda asiento
	ERROR_BUSQUEDA_INTERCAMBIO							("SIR_201"),
	ASIENTO_NO_ENCONTRADO								("SIR_202"),
	ERROR_ASIENTOS_PARA_ENVIAR							("SIR_203"),
	ERROR_BUSCAR_ASIENTO_ESTADO							("SIR_204"),
	ERROR_CARGAR_FICHERO_INTERCAMBIO					("SIR_205"),
	
	// Errores aceptar asiento
	EXITO_ACEPTAR_ASIENTO								("SIR_300"),
	ERROR_ACEPTAR_ASIENTO								("SIR_301"),
	ERROR_CREACION_TERCERO								("SIR_302"),
	ERROR_CREACION_REGISTRO								("SIR_303"),
	ERROR_REGISTRO_ACEPTAR_ASIENTO						("SIR_311"),
	
	// Errores rechazar 	
	EXITO_RECHAZAR_REGISTRO								("SIR_400"),
	ERROR_RECHAZAR_ASIENTO								("SIR_401"),
	ERROR_SICRES_RECHAZAR_ASIENTO						("SIR_402"),
	ERROR_REGISTRO_RECHAZO_ASIENTO						("SIR_411"),
	
	// Errores conversion SIR - Flexia
	ERROR_TIPO_DOCUMENTO_IDENTIFICACION_NO_EXISTENTE	("SIR_501"),
	ERROR_CODIGO_PAIS_NO_EXISTENTE						("SIR_502"),
	
	// Errores actualizar intercambio
	EXITO_CAMBIO_ESTADO_INTERCAMBIO						("SIR_600"),
	ERROR_ACTUALIZAR_INTERCAMBIO						("SIR_601"),
	
	// Errores enviar registro
	EXITO_ENVIAR_ASIENTO								("SIR_700"),
	ERROR_ENVIAR_ASIENTO								("SIR_701"),
	ERROR_BUSCAR_REGISTRO								("SIR_702"),
	
	// Errores cargar fichero
	ERROR_CARGAR_FICHERO								("SIR_801");
	
	private final String codigo;

	private CodigoMensajeSir(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * Recupera el valor interno del enumerado.
	 *
	 * @return Valor interno.
	 */
	public String getCodigo() {
		return codigo;
	}
}
