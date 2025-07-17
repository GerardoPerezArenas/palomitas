package es.altia.flexia.sir.exception;

import es.altia.util.exceptions.ExcepcionConCodigo;

/**
 * Representa una excepción durante un proceso relacionado con las clases que dan soporte al SIR.
 */
public class GestionSirException extends ExcepcionConCodigo {

	public GestionSirException(String codigo, String mensaje, Throwable excepcion, Object... parametros) {
		super(codigo, mensaje, excepcion, parametros);
	}

	public GestionSirException(String codigo, String mensaje, Object... parametros) {
		super(codigo, mensaje, parametros);
	}

}
