package es.altia.flexia.sir.model;

/**
 * Estado en el que se encuentra un {@link Asiento}. D
 */
public enum EstadoAsiento {

	/** El asiento ha sido aceptado. */
	ACEPTADO(3),
	/** Faltan documentos obligatorios por ser recibidos. */
	FALTAN_DOCUMENTOS(0),
	/** Que no se ha aceptado ni rechazado el asiento. */
	PENDIENTE(1),
	/** El asiento ha sido rechazado. */
	RECHAZADO(2);

	private final int valor;

	private EstadoAsiento(int valor) {
		this.valor = valor;
	}

	/**
	 * Recupera el valor interno del enumerado.
	 *
	 * @return Valor interno.
	 */
	public int getValor() {
		return valor;
	}

	/**
	 * Recupera un valor del enumerado a partir de su valor interno.
	 *
	 * @param valor Valor para el cual se quiere recuperar el valor del enumerado correspondiente.
	 * @return EstadoAsiento de valor correspondiente.
	 */
	public static EstadoAsiento getEnum(int valor) {
		switch (valor) {
			case 3:
				return ACEPTADO;
			case 0:
				return FALTAN_DOCUMENTOS;
			case 1:
				return PENDIENTE;
			case 2:
				return RECHAZADO;
			default:
				return null;
		}
	}

}
