package es.altia.agora.business.geninformes.utils.XeracionInformes;

public final class CampoSeleccionInforme {

	private String codEstructura = null;

	private String posicion = null;

	private String codCampoInforme = null;
	private String ancho = null;

	//
	// Campos de CAMPOINFORME
	//
	private String nomeCampo = null;

	private String campo = null;
	private String tipoCampo = null;
	private String lonxitudeCampo = null;
	private String selectValores = null;
	private String fromValores = null;
	private String whereValores = null;
	private String nomeAs = null;

	public boolean equals(Object o) {
		return (
			this.codCampoInforme.equals(
				((CampoSeleccionInforme) o).getCodCampoInforme()));

	}
	/**
	 * Returns the ancho.
	 * @return String
	 */
	public String getAncho() {
		return ancho;
	}

	/**
	 * Returns the campo.
	 * @return String
	 */
	public String getCampo() {
		return campo;
	}

	/**
	 * Returns the codCampoInforme.
	 * @return String
	 */
	public String getCodCampoInforme() {
		return codCampoInforme;
	}

	/**
	 * Returns the codEstructura.
	 * @return String
	 */
	public String getCodEstructura() {
		return codEstructura;
	}

	/**
	 * Returns the fromValores.
	 * @return String
	 */
	public String getFromValores() {
		return fromValores;
	}

	/**
	 * Returns the lonxitudeCampo.
	 * @return String
	 */
	public String getLonxitudeCampo() {
		return lonxitudeCampo;
	}

	/**
	 * Returns the nomeCampo.
	 * @return String
	 */
	public String getNomeCampo() {
		return nomeCampo;
	}

	/**
	 * Returns the posicion.
	 * @return String
	 */
	public String getPosicion() {
		return posicion;
	}

	/**
	 * Returns the selectValores.
	 * @return String
	 */
	public String getSelectValores() {
		return selectValores;
	}

	/**
	 * Returns the tipoCampo.
	 * @return String
	 */
	public String getTipoCampo() {
		return tipoCampo;
	}

	/**
	 * Returns the whereValores.
	 * @return String
	 */
	public String getWhereValores() {
		return whereValores;
	}

	/**
	 * Sets the ancho.
	 * @param ancho The ancho to set
	 */
	public void setAncho(String ancho) {
		this.ancho = ancho;
	}

	/**
	 * Sets the campo.
	 * @param campo The campo to set
	 */
	public void setCampo(String campo) {
		this.campo = campo;
	}

	/**
	 * Sets the codCampoInforme.
	 * @param codCampoInforme The codCampoInforme to set
	 */
	public void setCodCampoInforme(String codCampoInforme) {
		this.codCampoInforme = codCampoInforme;
	}

	/**
	 * Sets the codEstructura.
	 * @param codEstructura The codEstructura to set
	 */
	public void setCodEstructura(String codEstructura) {
		this.codEstructura = codEstructura;
	}

	/**
	 * Sets the fromValores.
	 * @param fromValores The fromValores to set
	 */
	public void setFromValores(String fromValores) {
		this.fromValores = fromValores;
	}

	/**
	 * Sets the lonxitudeCampo.
	 * @param lonxitudeCampo The lonxitudeCampo to set
	 */
	public void setLonxitudeCampo(String lonxitudeCampo) {
		this.lonxitudeCampo = lonxitudeCampo;
	}

	/**
	 * Sets the nomeCampo.
	 * @param nomeCampo The nomeCampo to set
	 */
	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	/**
	 * Sets the posicion.
	 * @param posicion The posicion to set
	 */
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	/**
	 * Sets the selectValores.
	 * @param selectValores The selectValores to set
	 */
	public void setSelectValores(String selectValores) {
		this.selectValores = selectValores;
	}

	/**
	 * Sets the tipoCampo.
	 * @param tipoCampo The tipoCampo to set
	 */
	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	/**
	 * Sets the whereValores.
	 * @param whereValores The whereValores to set
	 */
	public void setWhereValores(String whereValores) {
		this.whereValores = whereValores;
	}

	/**
	 * Returns the nomeAs.
	 * @return String
	 */
	public String getNomeAs() {
		return nomeAs;
	}

	/**
	 * Sets the nomeAs.
	 * @param nomeAs The nomeAs to set
	 */
	public void setNomeAs(String nomeAs) {
		this.nomeAs = nomeAs;
	}

}
