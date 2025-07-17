package es.altia.flexia.eni.model;

import es.altia.eni.conversoreni.domain.FirmaTipo;
import es.altia.util.StringUtils;

/**
 * Tipo de firmas posibles estipuladas por el ENI.
 */
public enum TipoFirmaEni {
	CERTIFICADO_BINARIO("CERTIFICADO_BINARIO"),
	CERTIFICADO_XML("CERTIFICADO_XML"),
	CSV("CSV"),
	REFERENCIA("REFERENCIA");

	private String valor;

	private TipoFirmaEni(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return this.valor;
	}

	/**
	 * Devuelve un TipoFirmaEni a partir de un String en caso de hallar correspondencia.
	 *
	 * @param valor
	 * @return
	 */
	public static TipoFirmaEni getTipoFirma(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			if (valor.equals(CERTIFICADO_BINARIO.getValor())) {
				return CERTIFICADO_BINARIO;
			}
			if (valor.equals(CERTIFICADO_XML.getValor())) {
				return CERTIFICADO_XML;
			}
			if (valor.equals(CSV.getValor())) {
				return CSV;
			}
			if (valor.equals(REFERENCIA.getValor())) {
				return REFERENCIA;
			}
		}
		return null;
	}

	/**
	 * Obtiene el valor del enumerado a partir del enumerado analogo de la libreria de conversion ENI.
	 *
	 * @param firmaTipo
	 * @return
	 */
	public static TipoFirmaEni getTipoFirma(FirmaTipo firmaTipo) {
		switch (firmaTipo) {
			case CSV:
				return CSV;
			case CERTIFICADO_BINARIO:
				return CERTIFICADO_BINARIO;
			case CERTIFICADO_XML:
				return CERTIFICADO_XML;
			case REFERENCIA:
				return REFERENCIA;
			default:
				return null;
		}
	}

}
