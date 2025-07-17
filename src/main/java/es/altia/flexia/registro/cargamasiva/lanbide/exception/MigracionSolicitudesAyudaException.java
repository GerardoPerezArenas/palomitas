package es.altia.flexia.registro.cargamasiva.lanbide.exception;

public class MigracionSolicitudesAyudaException extends Exception {
	private int codError;
	private String mensaje;

	public MigracionSolicitudesAyudaException() {
	}

	public MigracionSolicitudesAyudaException(int codError) {
		super();
		this.codError = codError;
	}

	public MigracionSolicitudesAyudaException(int codError, String mensaje) {
		super(mensaje);
		this.mensaje = mensaje;
		this.codError = codError;
	}

	public MigracionSolicitudesAyudaException(int codError, String mensaje, Throwable thrwbl) {
		super(mensaje, thrwbl);
		this.codError = codError;
		this.mensaje = mensaje;
	}

	/**
	 * @return the codError
	 */
	public int getCodError() {
		return codError;
	}

	/**
	 * @param codError the codError to set
	 */
	public void setCodError(int codError) {
		this.codError = codError;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
