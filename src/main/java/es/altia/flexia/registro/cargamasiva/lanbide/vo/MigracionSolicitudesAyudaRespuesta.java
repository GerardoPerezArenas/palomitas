package es.altia.flexia.registro.cargamasiva.lanbide.vo;

import java.util.List;

public class MigracionSolicitudesAyudaRespuesta {
	private List<String> mensajes;
	private int codResultado;
	private String mensajeResultado;

	/**
	 * @return the mensajes
	 */
	public List<String> getMensajes() {
		return mensajes;
	}

	/**
	 * @param mensajes the mensajes to set
	 */
	public void setMensajes(List<String> mensajes) {
		this.mensajes = mensajes;
	}

	/**
	 * @return the codResultado
	 */
	public int getCodResultado() {
		return codResultado;
	}

	/**
	 * @param codResultado the codResultado to set
	 */
	public void setCodResultado(int codResultado) {
		this.codResultado = codResultado;
	}

	/**
	 * @return the mensajeResultado
	 */
	public String getMensajeResultado() {
		return mensajeResultado;
	}

	/**
	 * @param mensajeResultado the mensajeResultado to set
	 */
	public void setMensajeResultado(String mensajeResultado) {
		this.mensajeResultado = mensajeResultado;
	}
}
