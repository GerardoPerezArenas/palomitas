package es.altia.agora.business.geninformes.utils.XeracionInformes;

/**
 * @author jgd
 * Clase EstructuraEntidades en el fichero EstructuraEntidades.java,creado el 31-oct-2003.
 * 
 */
public final class EstructuraEntidades {
	private static String separador = "_";

	private String codEstructura = null;
	private String codInformeXerador = null;
	private String codEntidadeInforme = null;
	private String codPai = null;
	private String posicion = null;
	private String consultaSQL = null;

	private EstructuraEntidades pai = null;
	private java.util.Collection listaInstancias = new java.util.Vector();

	private java.util.Vector camposSeleccion = null;
	private java.util.Vector camposOrde = null;
	private java.util.Vector camposCondicion = null;
	private java.util.Vector hijos = null;

	private java.util.AbstractList coleccionJoinsConPadre = null;
	private java.util.AbstractList coleccionUnionJoinsConHijos = null;
	private boolean consultaEjecutada = false;

	private es.altia.util.conexion.Cursor cursorConsulta = null;
	private java.util.Vector valoresParametrosConsulta = new java.util.Vector();

	//
	// DATOS DE ENTIDADE
	//
	private String nomeEntidade = null;
	private String clausulaFrom = null;
	private String clausulaWhere = null;
	private String nomeVista = null;
        
        // #239565: atributo con la descripcion del tipo de informe
        private String descTipoInforme;
         // #267396: atributo con el formato en el que se mostrar?n las fechas	
        private String formatoFecha;

	public EstructuraEntidades(String codInforme, String codEntidade) {
		camposSeleccion = new java.util.Vector();
		camposOrde = new java.util.Vector();
		camposCondicion = new java.util.Vector();
		hijos = new java.util.Vector();
		codInformeXerador = codInforme;
		codEntidadeInforme = codEntidade;

	}

	public boolean equals(Object o) {
		return (
			this.codEntidadeInforme.equals(
				((EstructuraEntidades) o).getCodEntidadeInforme()));
	}

	public void addCampo(CampoSeleccionInforme cs) {
		camposSeleccion.add(cs);
	}

	public void addCampo(CampoOrdeInforme cs) {
		camposOrde.add(cs);
	}

	public void addCampo(CampoCondicionInforme cs) {
		camposCondicion.add(cs);
	}

	public void addHijo(EstructuraEntidades ee) {
		hijos.add(ee);
	}

	/**
	 * Returns the camposCondicion.
	 * @return java.util.Vector
	 */
	public java.util.Vector getCamposCondicion() {
		return camposCondicion;
	}

	/**
	 * Returns the camposOrde.
	 * @return java.util.Vector
	 */
	public java.util.Vector getCamposOrde() {
		return camposOrde;
	}

	/**
	 * Returns the camposSeleccion.
	 * @return java.util.Vector
	 */
	public java.util.Vector getCamposSeleccion() {
		return camposSeleccion;
	}

	/**
	 * Returns the codEntidadeInforme.
	 * @return String
	 */
	public String getCodEntidadeInforme() {
		return codEntidadeInforme;
	}

	/**
	 * Returns the codInformeXerador.
	 * @return String
	 */
	public String getCodInformeXerador() {
		return codInformeXerador;
	}

	/**
	 * Returns the hijos.
	 * @return java.util.Vector
	 */
	public java.util.Vector getHijos() {
		return hijos;
	}

	/**
	 * Sets the camposCondicion.
	 * @param camposCondicion The camposCondicion to set
	 */
	public void setCamposCondicion(java.util.Vector camposCondicion) {
		this.camposCondicion = camposCondicion;
	}

	/**
	 * Sets the camposOrde.
	 * @param camposOrde The camposOrde to set
	 */
	public void setCamposOrde(java.util.Vector camposOrde) {
		this.camposOrde = camposOrde;
	}

	/**
	 * Sets the camposSeleccion.
	 * @param camposSeleccion The camposSeleccion to set
	 */
	public void setCamposSeleccion(java.util.Vector camposSeleccion) {
		this.camposSeleccion = camposSeleccion;
	}

	/**
	 * Sets the codEntidadeInforme.
	 * @param codEntidadeInforme The codEntidadeInforme to set
	 */
	public void setCodEntidadeInforme(String codEntidadeInforme) {
		this.codEntidadeInforme = codEntidadeInforme;
	}

	/**
	 * Sets the codInformeXerador.
	 * @param codInformeXerador The codInformeXerador to set
	 */
	public void setCodInformeXerador(String codInformeXerador) {
		this.codInformeXerador = codInformeXerador;
	}

	/**
	 * Sets the hijos.
	 * @param hijos The hijos to set
	 */
	public void setHijos(java.util.Vector hijos) {
		this.hijos = hijos;
	}

	/**
	 * Returns the clausulaFrom.
	 * @return String
	 */
	public String getClausulaFrom() {
		return clausulaFrom;
	}

	/**
	 * Returns the clausulaWhere.
	 * @return String
	 */
	public String getClausulaWhere() {
		return clausulaWhere;
	}

	/**
	 * Returns the nomeEntidade.
	 * @return String
	 */
	public String getNomeEntidade() {
		return nomeEntidade;
	}

	/**
	 * Sets the clausulaFrom.
	 * @param clausulaFrom The clausulaFrom to set
	 */
	public void setClausulaFrom(String clausulaFrom) {
		this.clausulaFrom = clausulaFrom;
	}

	/**
	 * Sets the clausulaWhere.
	 * @param clausulaWhere The clausulaWhere to set
	 */
	public void setClausulaWhere(String clausulaWhere) {
		this.clausulaWhere = clausulaWhere;
	}

	/**
	 * Sets the nomeEntidade.
	 * @param nomeEntidade The nomeEntidade to set
	 */
	public void setNomeEntidade(String nomeEntidade) {
		this.nomeEntidade = nomeEntidade;
	}

	/**
	 * Returns the codPai.
	 * @return String
	 */
	public String getCodPai() {
		return codPai;
	}

	/**
	 * Sets the codPai.
	 * @param codPai The codPai to set
	 */
	public void setCodPai(String codPai) {
		this.codPai = codPai;
	}

	/**
	 * Returns the posicion.
	 * @return String
	 */
	public String getPosicion() {
		return posicion;
	}

	/**
	 * Sets the posicion.
	 * @param posicion The posicion to set
	 */
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	/**
	 * Returns the nomeVista.
	 * @return String
	 */
	public String getNomeVista() {
		return nomeVista;
	}

	/**
	 * Sets the nomeVista.
	 * @param nomeVista The nomeVista to set
	 */
	public void setNomeVista(String nomeVista) {
		this.nomeVista = nomeVista;
	}

	/**
	 * Returns the pai.
	 * @return EstructuraEntidades
	 */
	public EstructuraEntidades getPai() {
		return pai;
	}

	/**
	 * Sets the pai.
	 * @param pai The pai to set
	 */
	public void setPai(EstructuraEntidades pai) {
		this.pai = pai;
	}

	/**
	 * Returns the consultaSQL.
	 * @return String
	 */
	public String getConsultaSQL() {
		return consultaSQL;
	}

	/**
	 * Sets the consultaSQL.
	 * @param consultaSQL The consultaSQL to set
	 */
	public void setConsultaSQL(String consultaSQL) {
		this.consultaSQL = consultaSQL;
	}

	/**
	 * Returns the coleccionJoinsConPadre.
	 * @return java.util.AbstractList
	 */
	public java.util.AbstractList getColeccionJoinsConPadre() {
		return coleccionJoinsConPadre;
	}

	/**
	 * Returns the coleccionUnionJoinsConHijos.
	 * @return java.util.AbstractList
	 */
	public java.util.AbstractList getColeccionUnionJoinsConHijos() {
		return coleccionUnionJoinsConHijos;
	}

	/**
	 * Sets the coleccionJoinsConPadre.
	 * @param coleccionJoinsConPadre The coleccionJoinsConPadre to set
	 */
	public void setColeccionJoinsConPadre(
		java.util.AbstractList coleccionJoinsConPadre) {
		this.coleccionJoinsConPadre = coleccionJoinsConPadre;
	}

	/**
	 * Sets the coleccionUnionJoinsConHijos.
	 * @param coleccionUnionJoinsConHijos The coleccionUnionJoinsConHijos to set
	 */
	public void setColeccionUnionJoinsConHijos(
		java.util.AbstractList coleccionUnionJoinsConHijos) {
		this.coleccionUnionJoinsConHijos = coleccionUnionJoinsConHijos;
	}

	/**
	 * Returns the listaInstancias.
	 * @return java.util.HashMap
	 */
	public java.util.Collection getListaInstancias() {
		return listaInstancias;
	}

	/**
	 * Sets the listaInstancias.
	 * @param listaInstancias The listaInstancias to set
	 */
	public void setListaInstancias(java.util.Collection listaInstancias) {
		this.listaInstancias = listaInstancias;
	}

	/**
	 * Returns the consultaEjecutada.
	 * @return boolean
	 */
	public boolean isConsultaEjecutada() {
		return consultaEjecutada;
	}

	/**
	 * Sets the consultaEjecutada.
	 * @param consultaEjecutada The consultaEjecutada to set
	 */
	public void setConsultaEjecutada(boolean consultaEjecutada) {
		this.consultaEjecutada = consultaEjecutada;
	}

	/**
	 * Returns the cursorConsulta.
	 * @return es.altia.util.conexion.Cursor
	 */
	public es.altia.util.conexion.Cursor getCursorConsulta() {
		return cursorConsulta;
	}

	/**
	 * Sets the cursorConsulta.
	 * @param cursorConsulta The cursorConsulta to set
	 */
	public void setCursorConsulta(
		es.altia.util.conexion.Cursor cursorConsulta) {
		this.cursorConsulta = cursorConsulta;
	}

	/**
	 * Returns the codEstructura.
	 * @return String
	 */
	public String getCodEstructura() {
		return codEstructura;
	}

	/**
	 * Sets the codEstructura.
	 * @param codEstructura The codEstructura to set
	 */
	public void setCodEstructura(String codEstructura) {
		this.codEstructura = codEstructura;
	}

	/**
	 * Method getPrefijo. Devuelve el prefijo ( ENT1_ENT2_ENT3 ) hasta esta
	 * entidad incluida
	 * @return String
	 */
	public String getPrefijo() {
		String salida = null;
		if (this.getPai() != null)
			salida =
				(this.getPai()).getPrefijo()
					+ separador
					+ this.getNomeEntidade();
		else
			salida = this.getNomeEntidade();

		return salida;
	}

	public String toString() {

		StringBuffer sb = new StringBuffer();
		java.util.Iterator iter = null;
		CampoSeleccionInforme cs = null;
		CampoOrdeInforme co = null;
		CampoCondicionInforme cc = null;
		EstructuraEntidades hijo = null;

		sb.append("EstructuraEntidades->\n");
		sb.append(
			"codEstrcutura:"
				+ codEstructura
				+ "\tcodInforme:"
				+ codInformeXerador
				+ ".\n");
		sb.append(
			"codPAi:"
				+ codPai
				+ ".codEntidadeInforme:"
				+ codEntidadeInforme
				+ ".\n");
		sb.append(
			"Posicion:"
				+ posicion
				+ ".consultaEjecutada:"
				+ consultaEjecutada
				+ ".\n");
		sb.append("consultaSQL:" + consultaSQL + ".\n");
		sb.append("Campos:\n");
		sb.append("Campos seleccion:\n");
		if (camposSeleccion.size() > 0) {
			iter = camposSeleccion.iterator();
			while (iter.hasNext()) {
				cs = (CampoSeleccionInforme) iter.next();
				sb.append(
					"Campo: Posicion:"
						+ cs.getPosicion()
						+ ".codCampoInforme:"
						+ cs.getCodCampoInforme()
						+ "codEstructura"
						+ cs.getCodEstructura()
						+ ".NomeCampo:"
						+ cs.getNomeCampo()
						+ ".Campo:"
						+ cs.getCampo()
						+ ".\n");
			}

		}
		sb.append("Campos condición:\n");
		if (camposCondicion.size() > 0) {
			iter = camposCondicion.iterator();
			while (iter.hasNext()) {
				cc = (CampoCondicionInforme) iter.next();
				sb.append(
					"Campo: Posicion:"
						+ cc.getPosicion()
						+ ".codCampoInforme:"
						+ cc.getCodCampoInforme()
						+ "codEstructura"
						+ cc.getCodEstructura()
						+ ".NomeCampo:"
						+ cc.getNomeCampo()
						+ ".Campo:"
						+ cc.getCampo()
						+ ".operador:"
						+ cc.getOperador()
						+ ".clausula:"
						+ cc.getClausula()
						+ ".valor:"
						+ cc.getValor()
						+ ".\n");
			}

		}

		sb.append("Campos orde:\n");
		if (camposOrde.size() > 0) {
			iter = camposOrde.iterator();
			while (iter.hasNext()) {
				co = (CampoOrdeInforme) iter.next();
				sb.append(
					"Campo: Posicion:"
						+ co.getPosicion()
						+ ".codCampoInforme:"
						+ co.getCodCampoInforme()
						+ "codEstructura"
						+ co.getCodEstructura()
						+ ".NomeCampo:"
						+ co.getNomeCampo()
						+ ".Campo:"
						+ co.getCampo()
						+ ".TipoOrde:"
						+ co.getTipoOrde()
						+ ".\n");
			}

		}
		sb.append("\n\t\t\tHIJOS:\n");
		if (hijos.size() > 0) {
			iter = hijos.iterator();
			while (iter.hasNext()) {
				hijo = (EstructuraEntidades) iter.next();

				sb.append("Hijo " + hijo.getPosicion() + ":\n");
				sb.append(hijo.toString());
				sb.append("<<<<<<<<<<<<<<<<<<\n");
			}
		}
		sb.append("--------------------------END--\n");

		return sb.toString();

	}
	/**
	 * Returns the valoresParametrosConsulta.
	 * @return java.util.Vector
	 */
	public java.util.Vector getValoresParametrosConsulta() {
		return valoresParametrosConsulta;
	}

	/**
	 * Sets the valoresParametrosConsulta.
	 * @param valoresParametrosConsulta The valoresParametrosConsulta to set
	 */
	public void setValoresParametrosConsulta(
		java.util.Vector valoresParametrosConsulta) {
		this.valoresParametrosConsulta = valoresParametrosConsulta;
	}

    /**
     * @return the descTipoInforme
     */
    public String getDescTipoInforme() {
        return descTipoInforme;
    }

    /**
     * @param descTipoInforme the descTipoInforme to set
     */
    public void setDescTipoInforme(String descTipoInforme) {
        this.descTipoInforme = descTipoInforme;
    }

    public String getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }
    
    

}
