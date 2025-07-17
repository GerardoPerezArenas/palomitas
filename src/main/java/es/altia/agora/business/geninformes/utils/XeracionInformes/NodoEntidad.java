package es.altia.agora.business.geninformes.utils.XeracionInformes;

//
// imports para crear el XML
//


/**
 * @author jgd
 * 
 * 
 */
public final class NodoEntidad {

	private java.util.HashMap campos = new java.util.HashMap();
	private java.util.HashMap hijosPorTipo = new java.util.HashMap();
	private String nomeEntidade = null;

	public void addCampo(String nome, String valor) {

		campos.put(nome, valor);
	}

	public String getCampo(String nome) {
		return (String) campos.get(nome);
	}

	public java.util.Vector getHijosTipo(String nome) {
		return (java.util.Vector) hijosPorTipo.get(nome);
	}

	public void addHijoTipo(String nome, NodoEntidad hijo) {
		java.util.Vector temp = null;
		if (hijosPorTipo.containsKey(nome)) {
			temp = (java.util.Vector) hijosPorTipo.get(nome);
			temp.add(hijo);

		} else {
			temp = new java.util.Vector();
			temp.add(hijo);
			hijosPorTipo.put(nome, temp);
		}
	}
	/**
	 * Returns the campos.
	 * @return java.util.HashMap
	 */
	public java.util.HashMap getCampos() {
		return campos;
	}

	/**
	 * Returns the hijosPorTipo.
	 * @return java.util.HashMap
	 */
	public java.util.HashMap getHijosPorTipo() {
		return hijosPorTipo;
	}

	/**
	 * Returns the nomeEntidade.
	 * @return String
	 */
	public String getNomeEntidade() {
		return nomeEntidade;
	}

	/**
	 * Sets the campos.
	 * @param campos The campos to set
	 */
	public void setCampos(java.util.HashMap campos) {
		this.campos = campos;
	}

	/**
	 * Sets the hijosPorTipo.
	 * @param hijosPorTipo The hijosPorTipo to set
	 */
	public void setHijosPorTipo(java.util.HashMap hijosPorTipo) {
		this.hijosPorTipo = hijosPorTipo;
	}

	/**
	 * Sets the nomeEntidade.
	 * @param nomeEntidade The nomeEntidade to set
	 */
	public void setNomeEntidade(String nomeEntidade) {
		this.nomeEntidade = nomeEntidade;
	}
	
	public void remove(String nombre) {
	  campos.remove(nombre);	
	}

    public String toString() {
        return "CAMPOS: " + campos + " | HIJOS POR TIPO: " + hijosPorTipo + " | NOMBRE ENTIDAD: " + nomeEntidade;
    }

}
