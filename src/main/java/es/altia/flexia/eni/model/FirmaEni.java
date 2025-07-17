package es.altia.flexia.eni.model;

import es.altia.eni.conversoreni.domain.Firma;
import es.altia.eni.conversoreni.domain.FirmaCertificadoBinario;
import es.altia.eni.conversoreni.domain.FirmaCertificadoReferencia;
import es.altia.eni.conversoreni.domain.FirmaCertificadoXml;
import es.altia.eni.conversoreni.domain.FirmaCsv;
import es.altia.eni.conversoreni.domain.FirmaTipo;
import es.altia.eni.conversoreni.domain.FormatoFirma;

import es.altia.util.jdbc.sqlbuilder.RowResult;

/**
 * Representa una firma de las posibles segun el formato ENI. Se puede comprobar que tipo de firma es utilizando el
 * metodo {@link FirmaEni#getTipoFirma}.
 */
public class FirmaEni {
	
	private byte[] contenido;
	private FormatoFirmaEni formato;
	private long id;
	private String regulacionCsv;

	private TipoFirmaEni tipo;

	public final static String CONTENIDO = "CONTENIDO";
	public final static String FORMATO_FIRMA_ENI = "FORMATO_FIRMA_ENI";
	public final static String ID = "ID";
	public final static String TIPO_FIRMA_ENI = "TIPO_FIRMA_ENI";
	public final static String TABLA = "FIRMAS_ENI";
	public final static String SECUENCIA = "FIRMAS_ENI_SECUENCIA";

	public FirmaEni() {
	}

	/**
	 * Crea una firma a partir de un {@link RowResult}.
	 * 
	 * @param row 
	 */
	public FirmaEni(RowResult row) {
		this.contenido = row.get("CONTENIDO", byte[].class);
		this.formato = FormatoFirmaEni.getFormatoFirmaEni(row.getString("FORMATO_FIRMA_ENI"));
		this.id = row.getInteger("ID");
		this.regulacionCsv = row.getString("REGULACION_CSV");
		this.tipo = TipoFirmaEni.getTipoFirma(row.getString("TIPO_FIRMA_ENI"));
	}
	
	/**
	 * Crea una FirmaEni a partir de una {@link Firma firma} de la libreria ConversorENI.
	 * 
	 * @param firma 
	 */
	public FirmaEni(Firma firma) {
		switch (firma.getTipo()) {
			case CSV:
				this.regulacionCsv = ((FirmaCsv) firma).getRegulacion();
				this.contenido = ((FirmaCsv) firma).getCsv().getBytes();
				break;
			case CERTIFICADO_BINARIO:
			case CERTIFICADO_XML:
				this.contenido = firma.getContenidoBytes();
				break;
			case REFERENCIA:
				this.contenido = firma.getContenidoString().getBytes();
				break;
			default:
				break; 
		}
		this.formato = FormatoFirmaEni.valueOf(firma.getFormato().toString());
		this.tipo = TipoFirmaEni.valueOf(firma.getTipo().toString());
	}

	/**
	 * Devuelve una {@link Firma} creada a partir de la instancia.
	 * 
	 * @return 
	 */
	public Firma toEni() {
		Firma firma;
		String valorContenido;
		switch (tipo) {
			case CERTIFICADO_BINARIO:
				firma = new FirmaCertificadoBinario();
				((FirmaCertificadoBinario) firma).setContenido(this.contenido);
				break;
			case CERTIFICADO_XML:
				firma = new FirmaCertificadoXml();
				((FirmaCertificadoXml) firma).setContenido(this.contenido);
				break;
			case CSV:
				firma = new FirmaCsv();
				valorContenido = (this.contenido != null) ? new String(this.contenido) : null;
				((FirmaCsv) firma).setCsv(valorContenido);
				((FirmaCsv) firma).setRegulacion(this.regulacionCsv);
				break;
			case REFERENCIA:
				firma = new FirmaCertificadoReferencia();
				valorContenido = (this.contenido != null) ? new String(this.contenido) : null;
				((FirmaCertificadoReferencia) firma).setReferencia(valorContenido);
				break;
			default:
				return null;
		}
		firma.setTipo(FirmaTipo.valueOf(this.tipo.getValor()));
		firma.setFormato(FormatoFirma.valueOf(this.formato.getValor()));
		
		return firma;
	}

	/**
	 * Puede adquirir diferentes valores segun el tipo de firma:
	 * - {@link TipoFirmaEni#CERTIFICADO_BINARIO}: Contenido en bytes.
	 * - {@link TipoFirmaEni#CERTIFICADO_XML}: Contenido en bytes en formato XML.
	 * - {@link TipoFirmaEni#CSV}: Representa el CSV (Codigo Seguro de Verificacion). Los bytes forman un String.
	 * - {@link TipoFirmaEni#REFERENCIA}: Referencia a otro elemento XML. Los bytes forman un String.
	 * 
	 * @return 
	 */
	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}

	public FormatoFirmaEni getFormato() {
		return formato;
	}

	public void setFormato(FormatoFirmaEni formato) {
		this.formato = formato;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TipoFirmaEni getTipo() {
		return tipo;
	}

	public void setTipo(TipoFirmaEni tipo) {
		this.tipo = tipo;
	}

	public String getRegulacionCsv() {
		return regulacionCsv;
	}

	public void setRegulacionCsv(String regulacionCsv) {
		this.regulacionCsv = regulacionCsv;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("FirmaEni{")
				.append("contenido=").append(contenido)
				.append(", formato=").append(formato)
				.append(", id=").append(id)
				.append(", regulacionCsv=").append(regulacionCsv)
				.append(", tipo=").append(tipo)
				.append('}').toString();
	}

}
