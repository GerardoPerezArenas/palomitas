package es.altia.flexia.eni.model;

import es.altia.eni.conversoreni.domain.FormatoFirma;
import es.altia.util.StringUtils;

/**
 * Tipos de formato posibles del estipulados por el ENI.
 */
public enum FormatoFirmaEni {
	CSV("CSV"),
	CADES_ATTACHED_EXPLICIT("CADES_ATTACHED_EXPLICIT"),
	CADES_ATTACHED_IMPLICIT("CADES_ATTACHED_IMPLICIT"),
	CADES_DETACHED_EXPLICIT("CADES_DETACHED_EXPLICIT"),
	XADES_ENVELOPED("XADES_ENVELOPED"),
	XADES_INTERNALLY_DETACHED("XADES_INTERNALLY_DETACHED"),
	PADES("PADES");

	private String valor;

	private FormatoFirmaEni(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return this.valor;
	}

	/**
	 * Devuelve un FormatoFirmaEni a partir de un String en caso de hallar correspondencia.
	 *
	 * @param valor
	 * @return
	 */
	public static FormatoFirmaEni getFormatoFirmaEni(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			if (valor.equals(CSV.getValor())) {
				return CSV;
			}
			if (valor.equals(CADES_ATTACHED_EXPLICIT.getValor())) {
				return CADES_ATTACHED_EXPLICIT;
			}
			if (valor.equals(CADES_ATTACHED_IMPLICIT.getValor())) {
				return CADES_ATTACHED_IMPLICIT;
			}
			if (valor.equals(CADES_DETACHED_EXPLICIT.getValor())) {
				return CADES_DETACHED_EXPLICIT;
			}
			if (valor.equals(XADES_ENVELOPED.getValor())) {
				return XADES_ENVELOPED;
			}
			if (valor.equals(XADES_INTERNALLY_DETACHED.getValor())) {
				return XADES_INTERNALLY_DETACHED;
			}
			if (valor.equals(PADES.getValor())) {
				return PADES;
			}
		}
		return null;
	}

	/**
	 * Obtiene el valor del enumerado a partir del enumerado analogo de la libreria de conversion ENI.
	 *
	 * @param formatoFirma
	 * @return
	 */
	public static FormatoFirmaEni getFormatoFirmaEni(FormatoFirma formatoFirma) {
		switch (formatoFirma) {
			case CSV:
				return CSV;
			case XADES_INTERNALLY_DETACHED:
				return XADES_INTERNALLY_DETACHED;
			case XADES_ENVELOPED:
				return XADES_ENVELOPED;
			case CADES_DETACHED_EXPLICIT:
				return CADES_DETACHED_EXPLICIT;
			case CADES_ATTACHED_IMPLICIT:
				return CADES_ATTACHED_IMPLICIT;
			case PADES:
				return PADES;
			default:
				return null;
		}
	}
}
