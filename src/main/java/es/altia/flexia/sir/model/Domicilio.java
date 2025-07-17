package es.altia.flexia.sir.model;

import es.altia.util.jdbc.sqlbuilder.RowResult;

/**
 * Representa el domicilio de uno o más {@link Interesado interesados}. Para servir de puente entre las clases de la
 * librería de integración del SIR y las clases que ya existen en Flexia respetará lo necesario de cada una de las
 * partes.
 * Corresponde a la tabla DOMICILIO o a las tablas T_DNN, T_VIA y T_TVI.
 */
public class Domicilio {

	private Integer codDomicilio;		// COD_DOMICILIO 
	private String codPostal;		// COD_POSTAL (T_DNN.DNN_CPO) 
	private String direccion;		// DIRECCION (T_DNN.DNN_DMC) 
	private String municipio;		// MUNICIPIO (T_DNN.DNN_MUN) 
	private String pais;			// PAIS (T_DNN.DNN_PAI) 
	private String provincia;		// PROVINCIA (T_DNN.DNN_PRV) 
	// Atributos Flexia
	private String codDnn;			// DESC_VIA (T_DNN.DNN_DOM, PK)
	private String codTipoVia;		// ID_DOMICILIO (T_TVI.TVI_COD, PK)
	private String codVia;			// TIPO_VIA (T_VIA.VIA_COD, PK en conjunto con VIA_PAI, VIA_PRV y VIA_MUN)

	public Domicilio() {
	}

	/**
	 * Constructor a partir de una fila de la tabla Domicilio.
	 *
	 * @param row
	 */
	public Domicilio(RowResult row) {
		this.codDomicilio = row.getInteger("COD_DOMICILIO");
		this.codPostal = row.getString("COD_POSTAL");
		this.direccion = row.getString("DIRECCION");
		this.municipio = row.getString("MUNICIPIO");
		this.pais = row.getString("PAIS");
		this.provincia = row.getString("PROVINCIA");
		// Atributos Flexia
		this.codDnn = row.getString("COD_DNN");
		this.codTipoVia = row.getString("COD_TIPO_VIA");
		this.codVia = row.getString("COD_VIA");
	}

	/**
	 * @return Codigo unico del domicilio autogenerado por la base de datos.
	 */
	public int getCodDomicilio() {
		return codDomicilio;
	}

	public void setCodDomicilio(int codDomicilio) {
		this.codDomicilio = codDomicilio;
	}
	
	/**
	 * @return Codigo postal del domicilio.
	 */
	public String getCodPostal() {
		return codPostal;
	}

	/**
	 * @param codPostal Cadena de 5 digitos. Puede empezar por 0.
	 */
	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}

	/**
	 * @return Direccion del domicilio.
	 */
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return Municipio del domicilio. 
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio Cadena de 4 digitos. Puede empezar por 0.
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return Pais del domicilio.
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais Cadena de 4 digitos. Puede empezar por 0.
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return Provincia del domicilio.
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia Cadena de dos digitos. Puede empezar por 0.
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * @return Codigo del tipo de via.
	 */
	public String getCodTipoVia() {
		return codTipoVia;
	}

	public void setCodTipoVia(String codTipoVia) {
		this.codTipoVia = codTipoVia;
	}

	/**
	 * @return Codigo del domicilio.
	 */
	public String getCodDnn() {
		return codDnn;
	}

	public void setCodDnn(String codDnn) {
		this.codDnn = codDnn;
	}

	/**
	 * @return Codigo de la via.
	 */
	public String getCodVia() {
		return codVia;
	}

	public void setCodVia(String codVia) {
		this.codVia = codVia;
	}

	@Override
	public String toString() {
		return new StringBuilder("Bean{").append(
				", pais=").append(pais)
				.append("codDomicilio=").append(codDomicilio)
				.append(", provincia=").append(provincia)
				.append(", municipio=").append(municipio)
				.append(", direccion=").append(direccion)
				.append(", codPostal=").append(codPostal)
				.append(", codTipoVia=").append(codTipoVia)
				.append(", codVia=").append(codVia)
				.append(", codDnn=").append(codDnn)
				.append('}').toString();
	}
}
