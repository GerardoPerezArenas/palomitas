package es.altia.flexia.sir.model;

import es.altia.util.jdbc.sqlbuilder.RowResult;
import java.util.Date;

/**
 * Informacion de un intercambio del SIR.
 */
public class Intercambio {

	private Asiento asiento;				// COD_ASIENTO
	private String codIntercambio;			// COD_INTERCAMBIO
	private EstadoAsiento estadoAsiento;	// ESTADO
	private Date fechaRechazo;				// FECHA_RECHAZO
	private String motivoRechazo;			// MOTIVO_RECHAZO

	public Intercambio() {}
	
	public Intercambio(RowResult row) {
		this.asiento = new Asiento();
		this.asiento.setCodAsiento(row.getInteger("COD_ASIENTO"));
		this.codIntercambio = row.getString("COD_INTERCAMBIO");
		this.estadoAsiento = EstadoAsiento.getEnum(row.getInteger("ESTADO"));
		this.fechaRechazo = row.get("FECHA_RECHAZO", Date.class);
		this.motivoRechazo = row.getString("MOTIVO_RECHAZO");
	}
	
	/**
	 * @return {@link Asiento} al que esta relacionado el intercambio.
	 */
	public Asiento getAsiento() {
		return asiento;
	}

	public void setAsiento(Asiento asiento) {
		this.asiento = asiento;
	}

	/**
	 * @return Codigo unico del intercambio.
	 */
	public String getCodIntercambio() {
		return codIntercambio;
	}

	/**
	 * @param codIntercambio De formato CodigoEntidadRegistralOrigen_AA_NumeroSecuencial donde:
	 * <ul>
	 * <li>CódigoEntidadRegistralOrigen: Codigo que figura para la Entidad Origen en el Directorio Comun, codificado en
	 * base a 21 caracteres</li>
	 * <li>AA: Año en curso en dos digitos.</li>
	 * <li>NumeroSecuencial: Numero de secuencia para evitar la repeticion de dos identificadores.</li>
	 * </ul>.
	 */
	public void setCodIntercambio(String codIntercambio) {
		this.codIntercambio = codIntercambio;
	}

	/**
	 * @return {@link EstadoAsiento} del asiento.
	 */
	public EstadoAsiento getEstado() {
		return estadoAsiento;
	}

	public void setEstado(EstadoAsiento estadoAsiento) {
		this.estadoAsiento = estadoAsiento;
	}
	
	/**
	 * @return Fecha de rechazo del intercambio.
	 */
	public Date getFechaRechazo() {
		return fechaRechazo;
	}

	public void setFechaRechazo(Date fechaRechazo) {
		this.fechaRechazo = fechaRechazo;
	}

	/**
	 * @return Motivo de rechazo del intercambio.
	 */
	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}
	
	
	
}
