/**
 * XeradorInformesForm.java
 *
 * @author Julio J. Gómez Diaz. Altia COnsultores
 */

package es.altia.agora.interfaces.user.web.informes;

import org.apache.struts.action.ActionForm;

public class XeradorInformesForm extends ActionForm {
	private String textOperacion = null;

	private String[] comboCamposElexidos = null;
	private String COD_INFORMEXERADOROculto = null;
	private String comboFormatoInforme = null;
	private String comboEntidadeInforme = null;
	private String MNUCODOculto = null;
	private String codCampoInformeOculto = null;

	private String tipoFicheiroSaida = null;

	//
	// Propiedades de Ordenacion
	//
	private String comboCampoOrdenacion_1 = null;
	private String comboSentidoOrdenacion_1 = null;

	private String comboCampoOrdenacion_2 = null;
	private String comboSentidoOrdenacion_2 = null;
	private String comboCampoOrdenacion_3 = null;
	private String comboSentidoOrdenacion_3 = null;
	private String comboCampoOrdenacion_4 = null;
	private String comboSentidoOrdenacion_4 = null;
	private String comboCampoOrdenacion_5 = null;
	private String comboSentidoOrdenacion_5 = null;
	//
	// FIN DE: Propiedades de Ordenacion
	//

	//
	// Propiedades de Filtro
	//
	private String comboCampo_1 = null;
	private String comboCondicion_1 = null;
	private String textValor_1 = null;

	private String comboOperador_2 = null;
	private String comboCampo_2 = null;
	private String comboCondicion_2 = null;
	private String textValor_2 = null;

	private String comboOperador_3 = null;
	private String comboCampo_3 = null;
	private String comboCondicion_3 = null;
	private String textValor_3 = null;

	private String comboOperador_4 = null;
	private String comboCampo_4 = null;
	private String comboCondicion_4 = null;
	private String textValor_4 = null;

	private String comboOperador_5 = null;
	private String comboCampo_5 = null;
	private String comboCondicion_5 = null;
	private String textValor_5 = null;

	private String comboOperador_6 = null;
	private String comboCampo_6 = null;
	private String comboCondicion_6 = null;
	private String textValor_6 = null;

	private String comboOperador_7 = null;
	private String comboCampo_7 = null;
	private String comboCondicion_7 = null;
	private String textValor_7 = null;
	//
	// FIN DE: Propiedades de Filtro
	//

	//
	// Propiedades de listado
	//
	private String textCabeceira = null;
	private String comboFonteCabeceira = null;
	private String comboTamanoCabeceira = null;

	private String negritaCabeceira = null;
	private String subraiadoCabeceira = null;
	private String cursivaCabeceira = null;
	private String cabeceiraOficial = null;
	private String cabeceiraColumnas = null;
	private String cabeceiraCentro = null;

	private String textPe = null;
	private String comboFontePe = null;
	private String comboTamanoPe = null;

	private String dataInformePe = null;
	private String orientacionPaxina = null;
	private String numeroPaxinaPe = null;

	private String comboFonteDetalle = null;
	private String comboTamanoDetalle = null;
	private String textMarxeEsquerdo = null;
	private String textMarxeDereito = null;

	private String numerarLinhas = null;

	//
	// Propiedades catalogación
	//
	private String textDescripcion = null;
	private String textNomeInforme = null;

	//
	// Propiedades de etiquetas
	//
	private String comboTipoEtiqueta = null;
	private String etTextAncho = null;
	private String etTextAlto = null;
	private String etTextHorizontal = null;
	private String etTextVertical = null;
	private String etTextSuperior = null;
	private String etTextInferior = null;
	private String etTextEsquerdo = null;
	private String etTextDereito = null;
	private String etTextSuperiorEtiqueta = null;
	private String etTextEsquerdoEtiqueta = null;
	private String etComboFonteDetalle = null;
	private String etComboTamanoDetalle = null;
	private String etBordeEtiqueta = null;
	private String etCodEtiqueta = null;
	private String etNome = null;

	//
	// Campos creados al usar comboNuevo
	//
	private String codCampo_1 = null;
	private String codCampo_2 = null;
	private String codCampo_3 = null;
	private String codCampo_4 = null;
	private String codCampo_5 = null;
	private String codCampo_6 = null;
	private String codCampo_7 = null;

	private String codOperador_2 = null;
	private String codOperador_3 = null;
	private String codOperador_4 = null;
	private String codOperador_5 = null;
	private String codOperador_6 = null;
	private String codOperador_7 = null;

	private String codCondicion_1 = null;
	private String codCondicion_2 = null;
	private String codCondicion_3 = null;
	private String codCondicion_4 = null;
	private String codCondicion_5 = null;
	private String codCondicion_6 = null;
	private String codCondicion_7 = null;

	private String codCampoOrdenacion_1 = null;
	private String codCampoOrdenacion_2 = null;
	private String codCampoOrdenacion_3 = null;
	private String codCampoOrdenacion_4 = null;
	private String codCampoOrdenacion_5 = null;

	private String codSentidoOrdenacion_1 = null;
	private String codSentidoOrdenacion_2 = null;
	private String codSentidoOrdenacion_3 = null;
	private String codSentidoOrdenacion_4 = null;
	private String codSentidoOrdenacion_5 = null;

	private String codEntidadeInforme = null;
	private String codFormatoInforme = null;

	//
	// para ListaInformes
	//
	private String codAplicacion = null;
	private org.apache.struts.upload.FormFile ficheroWord;

	/**
	 * Returns the comboCampo_1.
	 * @return String
	 */
	public String getComboCampo_1() {
		return comboCampo_1;
	}

	/**
	 * Returns the comboCampo_2.
	 * @return String
	 */
	public String getComboCampo_2() {
		return comboCampo_2;
	}

	/**
	 * Returns the comboCampo_3.
	 * @return String
	 */
	public String getComboCampo_3() {
		return comboCampo_3;
	}

	/**
	 * Returns the comboCampo_4.
	 * @return String
	 */
	public String getComboCampo_4() {
		return comboCampo_4;
	}

	/**
	 * Returns the comboCampo_5.
	 * @return String
	 */
	public String getComboCampo_5() {
		return comboCampo_5;
	}

	/**
	 * Returns the comboCampo_6.
	 * @return String
	 */
	public String getComboCampo_6() {
		return comboCampo_6;
	}

	/**
	 * Returns the comboCampo_7.
	 * @return String
	 */
	public String getComboCampo_7() {
		return comboCampo_7;
	}

	/**
	 * Returns the comboCampoOrdenacion_1.
	 * @return String
	 */
	public String getComboCampoOrdenacion_1() {
		return comboCampoOrdenacion_1;
	}

	/**
	 * Returns the comboCampoOrdenacion_2.
	 * @return String
	 */
	public String getComboCampoOrdenacion_2() {
		return comboCampoOrdenacion_2;
	}

	/**
	 * Returns the comboCampoOrdenacion_3.
	 * @return String
	 */
	public String getComboCampoOrdenacion_3() {
		return comboCampoOrdenacion_3;
	}

	/**
	 * Returns the comboCampoOrdenacion_4.
	 * @return String
	 */
	public String getComboCampoOrdenacion_4() {
		return comboCampoOrdenacion_4;
	}

	/**
	 * Returns the comboCampoOrdenacion_5.
	 * @return String
	 */
	public String getComboCampoOrdenacion_5() {
		return comboCampoOrdenacion_5;
	}

	/**
	 * Returns the comboCamposElexidos.
	 * @return String[]
	 */
	public String[] getComboCamposElexidos() {
		return comboCamposElexidos;
	}

	/**
	 * Returns the comboCondicion_1.
	 * @return String
	 */
	public String getComboCondicion_1() {
		return comboCondicion_1;
	}

	/**
	 * Returns the comboCondicion_2.
	 * @return String
	 */
	public String getComboCondicion_2() {
		return comboCondicion_2;
	}

	/**
	 * Returns the comboCondicion_3.
	 * @return String
	 */
	public String getComboCondicion_3() {
		return comboCondicion_3;
	}

	/**
	 * Returns the comboCondicion_4.
	 * @return String
	 */
	public String getComboCondicion_4() {
		return comboCondicion_4;
	}

	/**
	 * Returns the comboCondicion_5.
	 * @return String
	 */
	public String getComboCondicion_5() {
		return comboCondicion_5;
	}

	/**
	 * Returns the comboCondicion_6.
	 * @return String
	 */
	public String getComboCondicion_6() {
		return comboCondicion_6;
	}

	/**
	 * Returns the comboCondicion_7.
	 * @return String
	 */
	public String getComboCondicion_7() {
		return comboCondicion_7;
	}

	/**
	 * Returns the comboFonteCabeceira.
	 * @return String
	 */
	public String getComboFonteCabeceira() {
		return comboFonteCabeceira;
	}

	/**
	 * Returns the comboFonteDetalle.
	 * @return String
	 */
	public String getComboFonteDetalle() {
		return comboFonteDetalle;
	}

	/**
	 * Returns the comboFontePe.
	 * @return String
	 */
	public String getComboFontePe() {
		return comboFontePe;
	}

	/**
	 * Returns the comboOperador_2.
	 * @return String
	 */
	public String getComboOperador_2() {
		return comboOperador_2;
	}

	/**
	 * Returns the comboOperador_3.
	 * @return String
	 */
	public String getComboOperador_3() {
		return comboOperador_3;
	}

	/**
	 * Returns the comboOperador_4.
	 * @return String
	 */
	public String getComboOperador_4() {
		return comboOperador_4;
	}

	/**
	 * Returns the comboOperador_5.
	 * @return String
	 */
	public String getComboOperador_5() {
		return comboOperador_5;
	}

	/**
	 * Returns the comboOperador_6.
	 * @return String
	 */
	public String getComboOperador_6() {
		return comboOperador_6;
	}

	/**
	 * Returns the comboOperador_7.
	 * @return String
	 */
	public String getComboOperador_7() {
		return comboOperador_7;
	}

	/**
	 * Returns the comboSentidoOrdenacion_1.
	 * @return String
	 */
	public String getComboSentidoOrdenacion_1() {
		return comboSentidoOrdenacion_1;
	}

	/**
	 * Returns the comboSentidoOrdenacion_2.
	 * @return String
	 */
	public String getComboSentidoOrdenacion_2() {
		return comboSentidoOrdenacion_2;
	}

	/**
	 * Returns the comboSentidoOrdenacion_3.
	 * @return String
	 */
	public String getComboSentidoOrdenacion_3() {
		return comboSentidoOrdenacion_3;
	}

	/**
	 * Returns the comboSentidoOrdenacion_4.
	 * @return String
	 */
	public String getComboSentidoOrdenacion_4() {
		return comboSentidoOrdenacion_4;
	}

	/**
	 * Returns the comboSentidoOrdenacion_5.
	 * @return String
	 */
	public String getComboSentidoOrdenacion_5() {
		return comboSentidoOrdenacion_5;
	}

	/**
	 * Returns the comboTamanoCabeceira.
	 * @return String
	 */
	public String getComboTamanoCabeceira() {
		return comboTamanoCabeceira;
	}

	/**
	 * Returns the comboTamanoDetalle.
	 * @return String
	 */
	public String getComboTamanoDetalle() {
		return comboTamanoDetalle;
	}

	/**
	 * Returns the comboTamanoPe.
	 * @return String
	 */
	public String getComboTamanoPe() {
		return comboTamanoPe;
	}

	/**
	 * Returns the textCabeceira.
	 * @return String
	 */
	public String getTextCabeceira() {
		return textCabeceira;
	}

	/**
	 * Returns the textDescripcion.
	 * @return String
	 */
	public String getTextDescripcion() {
		return textDescripcion;
	}

	/**
	 * Returns the textMarxeDereito.
	 * @return String
	 */
	public String getTextMarxeDereito() {
		return textMarxeDereito;
	}

	/**
	 * Returns the textMarxeEsquerdo.
	 * @return String
	 */
	public String getTextMarxeEsquerdo() {
		return textMarxeEsquerdo;
	}

	/**
	 * Returns the textOperacion.
	 * @return String
	 */
	public String getTextOperacion() {
		return textOperacion;
	}

	/**
	 * Returns the textPe.
	 * @return String
	 */
	public String getTextPe() {
		return textPe;
	}

	/**
	 * Returns the textValor_1.
	 * @return String
	 */
	public String getTextValor_1() {
		return textValor_1;
	}

	/**
	 * Returns the textValor_2.
	 * @return String
	 */
	public String getTextValor_2() {
		return textValor_2;
	}

	/**
	 * Returns the textValor_3.
	 * @return String
	 */
	public String getTextValor_3() {
		return textValor_3;
	}

	/**
	 * Returns the textValor_4.
	 * @return String
	 */
	public String getTextValor_4() {
		return textValor_4;
	}

	/**
	 * Returns the textValor_5.
	 * @return String
	 */
	public String getTextValor_5() {
		return textValor_5;
	}

	/**
	 * Returns the textValor_6.
	 * @return String
	 */
	public String getTextValor_6() {
		return textValor_6;
	}

	/**
	 * Returns the textValor_7.
	 * @return String
	 */
	public String getTextValor_7() {
		return textValor_7;
	}

	/**
	 * Sets the comboCampo_1.
	 * @param comboCampo_1 The comboCampo_1 to set
	 */
	public void setComboCampo_1(String comboCampo_1) {
		this.comboCampo_1 = comboCampo_1;
	}

	/**
	 * Sets the comboCampo_2.
	 * @param comboCampo_2 The comboCampo_2 to set
	 */
	public void setComboCampo_2(String comboCampo_2) {
		this.comboCampo_2 = comboCampo_2;
	}

	/**
	 * Sets the comboCampo_3.
	 * @param comboCampo_3 The comboCampo_3 to set
	 */
	public void setComboCampo_3(String comboCampo_3) {
		this.comboCampo_3 = comboCampo_3;
	}

	/**
	 * Sets the comboCampo_4.
	 * @param comboCampo_4 The comboCampo_4 to set
	 */
	public void setComboCampo_4(String comboCampo_4) {
		this.comboCampo_4 = comboCampo_4;
	}

	/**
	 * Sets the comboCampo_5.
	 * @param comboCampo_5 The comboCampo_5 to set
	 */
	public void setComboCampo_5(String comboCampo_5) {
		this.comboCampo_5 = comboCampo_5;
	}

	/**
	 * Sets the comboCampo_6.
	 * @param comboCampo_6 The comboCampo_6 to set
	 */
	public void setComboCampo_6(String comboCampo_6) {
		this.comboCampo_6 = comboCampo_6;
	}

	/**
	 * Sets the comboCampo_7.
	 * @param comboCampo_7 The comboCampo_7 to set
	 */
	public void setComboCampo_7(String comboCampo_7) {
		this.comboCampo_7 = comboCampo_7;
	}

	/**
	 * Sets the comboCampoOrdenacion_1.
	 * @param comboCampoOrdenacion_1 The comboCampoOrdenacion_1 to set
	 */
	public void setComboCampoOrdenacion_1(String comboCampoOrdenacion_1) {
		this.comboCampoOrdenacion_1 = comboCampoOrdenacion_1;
	}

	/**
	 * Sets the comboCampoOrdenacion_2.
	 * @param comboCampoOrdenacion_2 The comboCampoOrdenacion_2 to set
	 */
	public void setComboCampoOrdenacion_2(String comboCampoOrdenacion_2) {
		this.comboCampoOrdenacion_2 = comboCampoOrdenacion_2;
	}

	/**
	 * Sets the comboCampoOrdenacion_3.
	 * @param comboCampoOrdenacion_3 The comboCampoOrdenacion_3 to set
	 */
	public void setComboCampoOrdenacion_3(String comboCampoOrdenacion_3) {
		this.comboCampoOrdenacion_3 = comboCampoOrdenacion_3;
	}

	/**
	 * Sets the comboCampoOrdenacion_4.
	 * @param comboCampoOrdenacion_4 The comboCampoOrdenacion_4 to set
	 */
	public void setComboCampoOrdenacion_4(String comboCampoOrdenacion_4) {
		this.comboCampoOrdenacion_4 = comboCampoOrdenacion_4;
	}

	/**
	 * Sets the comboCampoOrdenacion_5.
	 * @param comboCampoOrdenacion_5 The comboCampoOrdenacion_5 to set
	 */
	public void setComboCampoOrdenacion_5(String comboCampoOrdenacion_5) {
		this.comboCampoOrdenacion_5 = comboCampoOrdenacion_5;
	}

	/**
	 * Sets the comboCamposElexidos.
	 * @param comboCamposElexidos The comboCamposElexidos to set
	 */
	public void setComboCamposElexidos(String[] comboCamposElexidos) {
		this.comboCamposElexidos = comboCamposElexidos;
	}

	/**
	 * Sets the comboCondicion_1.
	 * @param comboCondicion_1 The comboCondicion_1 to set
	 */
	public void setComboCondicion_1(String comboCondicion_1) {
		this.comboCondicion_1 = comboCondicion_1;
	}

	/**
	 * Sets the comboCondicion_2.
	 * @param comboCondicion_2 The comboCondicion_2 to set
	 */
	public void setComboCondicion_2(String comboCondicion_2) {
		this.comboCondicion_2 = comboCondicion_2;
	}

	/**
	 * Sets the comboCondicion_3.
	 * @param comboCondicion_3 The comboCondicion_3 to set
	 */
	public void setComboCondicion_3(String comboCondicion_3) {
		this.comboCondicion_3 = comboCondicion_3;
	}

	/**
	 * Sets the comboCondicion_4.
	 * @param comboCondicion_4 The comboCondicion_4 to set
	 */
	public void setComboCondicion_4(String comboCondicion_4) {
		this.comboCondicion_4 = comboCondicion_4;
	}

	/**
	 * Sets the comboCondicion_5.
	 * @param comboCondicion_5 The comboCondicion_5 to set
	 */
	public void setComboCondicion_5(String comboCondicion_5) {
		this.comboCondicion_5 = comboCondicion_5;
	}

	/**
	 * Sets the comboCondicion_6.
	 * @param comboCondicion_6 The comboCondicion_6 to set
	 */
	public void setComboCondicion_6(String comboCondicion_6) {
		this.comboCondicion_6 = comboCondicion_6;
	}

	/**
	 * Sets the comboCondicion_7.
	 * @param comboCondicion_7 The comboCondicion_7 to set
	 */
	public void setComboCondicion_7(String comboCondicion_7) {
		this.comboCondicion_7 = comboCondicion_7;
	}

	/**
	 * Sets the comboFonteCabeceira.
	 * @param comboFonteCabeceira The comboFonteCabeceira to set
	 */
	public void setComboFonteCabeceira(String comboFonteCabeceira) {
		this.comboFonteCabeceira = comboFonteCabeceira;
	}

	/**
	 * Sets the comboFonteDetalle.
	 * @param comboFonteDetalle The comboFonteDetalle to set
	 */
	public void setComboFonteDetalle(String comboFonteDetalle) {
		this.comboFonteDetalle = comboFonteDetalle;
	}

	/**
	 * Sets the comboFontePe.
	 * @param comboFontePe The comboFontePe to set
	 */
	public void setComboFontePe(String comboFontePe) {
		this.comboFontePe = comboFontePe;
	}

	/**
	 * Sets the comboOperador_2.
	 * @param comboOperador_2 The comboOperador_2 to set
	 */
	public void setComboOperador_2(String comboOperador_2) {
		this.comboOperador_2 = comboOperador_2;
	}

	/**
	 * Sets the comboOperador_3.
	 * @param comboOperador_3 The comboOperador_3 to set
	 */
	public void setComboOperador_3(String comboOperador_3) {
		this.comboOperador_3 = comboOperador_3;
	}

	/**
	 * Sets the comboOperador_4.
	 * @param comboOperador_4 The comboOperador_4 to set
	 */
	public void setComboOperador_4(String comboOperador_4) {
		this.comboOperador_4 = comboOperador_4;
	}

	/**
	 * Sets the comboOperador_5.
	 * @param comboOperador_5 The comboOperador_5 to set
	 */
	public void setComboOperador_5(String comboOperador_5) {
		this.comboOperador_5 = comboOperador_5;
	}

	/**
	 * Sets the comboOperador_6.
	 * @param comboOperador_6 The comboOperador_6 to set
	 */
	public void setComboOperador_6(String comboOperador_6) {
		this.comboOperador_6 = comboOperador_6;
	}

	/**
	 * Sets the comboOperador_7.
	 * @param comboOperador_7 The comboOperador_7 to set
	 */
	public void setComboOperador_7(String comboOperador_7) {
		this.comboOperador_7 = comboOperador_7;
	}

	/**
	 * Sets the comboSentidoOrdenacion_1.
	 * @param comboSentidoOrdenacion_1 The comboSentidoOrdenacion_1 to set
	 */
	public void setComboSentidoOrdenacion_1(String comboSentidoOrdenacion_1) {
		this.comboSentidoOrdenacion_1 = comboSentidoOrdenacion_1;
	}

	/**
	 * Sets the comboSentidoOrdenacion_2.
	 * @param comboSentidoOrdenacion_2 The comboSentidoOrdenacion_2 to set
	 */
	public void setComboSentidoOrdenacion_2(String comboSentidoOrdenacion_2) {
		this.comboSentidoOrdenacion_2 = comboSentidoOrdenacion_2;
	}

	/**
	 * Sets the comboSentidoOrdenacion_3.
	 * @param comboSentidoOrdenacion_3 The comboSentidoOrdenacion_3 to set
	 */
	public void setComboSentidoOrdenacion_3(String comboSentidoOrdenacion_3) {
		this.comboSentidoOrdenacion_3 = comboSentidoOrdenacion_3;
	}

	/**
	 * Sets the comboSentidoOrdenacion_4.
	 * @param comboSentidoOrdenacion_4 The comboSentidoOrdenacion_4 to set
	 */
	public void setComboSentidoOrdenacion_4(String comboSentidoOrdenacion_4) {
		this.comboSentidoOrdenacion_4 = comboSentidoOrdenacion_4;
	}

	/**
	 * Sets the comboSentidoOrdenacion_5.
	 * @param comboSentidoOrdenacion_5 The comboSentidoOrdenacion_5 to set
	 */
	public void setComboSentidoOrdenacion_5(String comboSentidoOrdenacion_5) {
		this.comboSentidoOrdenacion_5 = comboSentidoOrdenacion_5;
	}

	/**
	 * Sets the comboTamanoCabeceira.
	 * @param comboTamanoCabeceira The comboTamanoCabeceira to set
	 */
	public void setComboTamanoCabeceira(String comboTamanoCabeceira) {
		this.comboTamanoCabeceira = comboTamanoCabeceira;
	}

	/**
	 * Sets the comboTamanoDetalle.
	 * @param comboTamanoDetalle The comboTamanoDetalle to set
	 */
	public void setComboTamanoDetalle(String comboTamanoDetalle) {
		this.comboTamanoDetalle = comboTamanoDetalle;
	}

	/**
	 * Sets the comboTamanoPe.
	 * @param comboTamanoPe The comboTamanoPe to set
	 */
	public void setComboTamanoPe(String comboTamanoPe) {
		this.comboTamanoPe = comboTamanoPe;
	}

	/**
	 * Sets the textCabeceira.
	 * @param textCabeceira The textCabeceira to set
	 */
	public void setTextCabeceira(String textCabeceira) {
		this.textCabeceira = textCabeceira;
	}

	/**
	 * Sets the textDescripcion.
	 * @param textDescripcion The textDescripcion to set
	 */
	public void setTextDescripcion(String textDescripcion) {
		this.textDescripcion = textDescripcion;
	}

	/**
	 * Sets the textMarxeDereito.
	 * @param textMarxeDereito The textMarxeDereito to set
	 */
	public void setTextMarxeDereito(String textMarxeDereito) {
		this.textMarxeDereito = textMarxeDereito;
	}

	/**
	 * Sets the textMarxeEsquerdo.
	 * @param textMarxeEsquerdo The textMarxeEsquerdo to set
	 */
	public void setTextMarxeEsquerdo(String textMarxeEsquerdo) {
		this.textMarxeEsquerdo = textMarxeEsquerdo;
	}

	/**
	 * Sets the textOperacion.
	 * @param textOperacion The textOperacion to set
	 */
	public void setTextOperacion(String textOperacion) {
		this.textOperacion = textOperacion;
	}

	/**
	 * Sets the textPe.
	 * @param textPe The textPe to set
	 */
	public void setTextPe(String textPe) {
		this.textPe = textPe;
	}

	/**
	 * Sets the textValor_1.
	 * @param textValor_1 The textValor_1 to set
	 */
	public void setTextValor_1(String textValor_1) {
		this.textValor_1 = textValor_1;
	}

	/**
	 * Sets the textValor_2.
	 * @param textValor_2 The textValor_2 to set
	 */
	public void setTextValor_2(String textValor_2) {
		this.textValor_2 = textValor_2;
	}

	/**
	 * Sets the textValor_3.
	 * @param textValor_3 The textValor_3 to set
	 */
	public void setTextValor_3(String textValor_3) {
		this.textValor_3 = textValor_3;
	}

	/**
	 * Sets the textValor_4.
	 * @param textValor_4 The textValor_4 to set
	 */
	public void setTextValor_4(String textValor_4) {
		this.textValor_4 = textValor_4;
	}

	/**
	 * Sets the textValor_5.
	 * @param textValor_5 The textValor_5 to set
	 */
	public void setTextValor_5(String textValor_5) {
		this.textValor_5 = textValor_5;
	}

	/**
	 * Sets the textValor_6.
	 * @param textValor_6 The textValor_6 to set
	 */
	public void setTextValor_6(String textValor_6) {
		this.textValor_6 = textValor_6;
	}

	/**
	 * Sets the textValor_7.
	 * @param textValor_7 The textValor_7 to set
	 */
	public void setTextValor_7(String textValor_7) {
		this.textValor_7 = textValor_7;
	}

	/**
	 * Returns the comboTipoEtiqueta.
	 * @return String
	 */
	public String getComboTipoEtiqueta() {
		return comboTipoEtiqueta;
	}

	/**
	 * Returns the etTextAlto.
	 * @return String
	 */
	public String getEtTextAlto() {
		return etTextAlto;
	}

	/**
	 * Returns the etTextAncho.
	 * @return String
	 */
	public String getEtTextAncho() {
		return etTextAncho;
	}

	/**
	 * Returns the etTextDereito.
	 * @return String
	 */
	public String getEtTextDereito() {
		return etTextDereito;
	}

	/**
	 * Returns the etTextEsquerdo.
	 * @return String
	 */
	public String getEtTextEsquerdo() {
		return etTextEsquerdo;
	}

	/**
	 * Returns the etTextEsquerdoEtiqueta.
	 * @return String
	 */
	public String getEtTextEsquerdoEtiqueta() {
		return etTextEsquerdoEtiqueta;
	}

	/**
	 * Returns the etTextHorizontal.
	 * @return String
	 */
	public String getEtTextHorizontal() {
		return etTextHorizontal;
	}

	/**
	 * Returns the etTextInferior.
	 * @return String
	 */
	public String getEtTextInferior() {
		return etTextInferior;
	}

	/**
	 * Returns the etTextSuperior.
	 * @return String
	 */
	public String getEtTextSuperior() {
		return etTextSuperior;
	}

	/**
	 * Returns the etTextSuperiorEtiqueta.
	 * @return String
	 */
	public String getEtTextSuperiorEtiqueta() {
		return etTextSuperiorEtiqueta;
	}

	/**
	 * Returns the etTextVertical.
	 * @return String
	 */
	public String getEtTextVertical() {
		return etTextVertical;
	}

	/**
	 * Sets the comboTipoEtiqueta.
	 * @param comboTipoEtiqueta The comboTipoEtiqueta to set
	 */
	public void setComboTipoEtiqueta(String comboTipoEtiqueta) {
		this.comboTipoEtiqueta = comboTipoEtiqueta;
	}

	/**
	 * Sets the etTextAlto.
	 * @param etTextAlto The etTextAlto to set
	 */
	public void setEtTextAlto(String etTextAlto) {
		this.etTextAlto = etTextAlto;
	}

	/**
	 * Sets the etTextAncho.
	 * @param etTextAncho The etTextAncho to set
	 */
	public void setEtTextAncho(String etTextAncho) {
		this.etTextAncho = etTextAncho;
	}

	/**
	 * Sets the etTextDereito.
	 * @param etTextDereito The etTextDereito to set
	 */
	public void setEtTextDereito(String etTextDereito) {
		this.etTextDereito = etTextDereito;
	}

	/**
	 * Sets the etTextEsquerdo.
	 * @param etTextEsquerdo The etTextEsquerdo to set
	 */
	public void setEtTextEsquerdo(String etTextEsquerdo) {
		this.etTextEsquerdo = etTextEsquerdo;
	}

	/**
	 * Sets the etTextEsquerdoEtiqueta.
	 * @param etTextEsquerdoEtiqueta The etTextEsquerdoEtiqueta to set
	 */
	public void setEtTextEsquerdoEtiqueta(String etTextEsquerdoEtiqueta) {
		this.etTextEsquerdoEtiqueta = etTextEsquerdoEtiqueta;
	}

	/**
	 * Sets the etTextHorizontal.
	 * @param etTextHorizontal The etTextHorizontal to set
	 */
	public void setEtTextHorizontal(String etTextHorizontal) {
		this.etTextHorizontal = etTextHorizontal;
	}

	/**
	 * Sets the etTextInferior.
	 * @param etTextInferior The etTextInferior to set
	 */
	public void setEtTextInferior(String etTextInferior) {
		this.etTextInferior = etTextInferior;
	}

	/**
	 * Sets the etTextSuperior.
	 * @param etTextSuperior The etTextSuperior to set
	 */
	public void setEtTextSuperior(String etTextSuperior) {
		this.etTextSuperior = etTextSuperior;
	}

	/**
	 * Sets the etTextSuperiorEtiqueta.
	 * @param etTextSuperiorEtiqueta The etTextSuperiorEtiqueta to set
	 */
	public void setEtTextSuperiorEtiqueta(String etTextSuperiorEtiqueta) {
		this.etTextSuperiorEtiqueta = etTextSuperiorEtiqueta;
	}

	/**
	 * Sets the etTextVertical.
	 * @param etTextVertical The etTextVertical to set
	 */
	public void setEtTextVertical(String etTextVertical) {
		this.etTextVertical = etTextVertical;
	}

	/**
	 * Returns the textNomeInforme.
	 * @return String
	 */
	public String getTextNomeInforme() {
		return textNomeInforme;
	}

	/**
	 * Sets the textNomeInforme.
	 * @param textNomeInforme The textNomeInforme to set
	 */
	public void setTextNomeInforme(String textNomeInforme) {
		this.textNomeInforme = textNomeInforme;
	}

	/**
	 * Returns the cOD_INFORMEXERADOROculto.
	 * @return String
	 */
	public String getCOD_INFORMEXERADOROculto() {
		return COD_INFORMEXERADOROculto;
	}

	/**
	 * Sets the cOD_INFORMEXERADOROculto.
	 * @param cOD_INFORMEXERADOROculto The cOD_INFORMEXERADOROculto to set
	 */
	public void setCOD_INFORMEXERADOROculto(String cOD_INFORMEXERADOROculto) {
		COD_INFORMEXERADOROculto = cOD_INFORMEXERADOROculto;
	}

	/**
	 * Returns the comboEntidadeInforme.
	 * @return String
	 */
	public String getComboEntidadeInforme() {
		return comboEntidadeInforme;
	}

	/**
	 * Returns the comboFormatoInforme.
	 * @return String
	 */
	public String getComboFormatoInforme() {
		return comboFormatoInforme;
	}

	/**
	 * Sets the comboEntidadeInforme.
	 * @param comboEntidadeInforme The comboEntidadeInforme to set
	 */
	public void setComboEntidadeInforme(String comboEntidadeInforme) {
		this.comboEntidadeInforme = comboEntidadeInforme;
	}

	/**
	 * Sets the comboFormatoInforme.
	 * @param comboFormatoInforme The comboFormatoInforme to set
	 */
	public void setComboFormatoInforme(String comboFormatoInforme) {
		this.comboFormatoInforme = comboFormatoInforme;
	}

	/**
	 * Returns the cabeceiraCentro.
	 * @return String
	 */
	public String getCabeceiraCentro() {
		return cabeceiraCentro;
	}

	/**
	 * Returns the cabeceiraColumnas.
	 * @return String
	 */
	public String getCabeceiraColumnas() {
		return cabeceiraColumnas;
	}

	/**
	 * Returns the cabeceiraOficial.
	 * @return String
	 */
	public String getCabeceiraOficial() {
		return cabeceiraOficial;
	}

	/**
	 * Returns the cursivaCabeceira.
	 * @return String
	 */
	public String getCursivaCabeceira() {
		return cursivaCabeceira;
	}

	/**
	 * Returns the dataInformePe.
	 * @return String
	 */
	public String getDataInformePe() {
		return dataInformePe;
	}

	/**
	 * Returns the negritaCabeceira.
	 * @return String
	 */
	public String getNegritaCabeceira() {
		return negritaCabeceira;
	}

	/**
	 * Returns the orientacionPaxina.
	 * @return String
	 */
	public String getOrientacionPaxina() {
		return orientacionPaxina;
	}

	/**
	 * Returns the subraiadoCabeceira.
	 * @return String
	 */
	public String getSubraiadoCabeceira() {
		return subraiadoCabeceira;
	}

	/**
	 * Sets the cabeceiraCentro.
	 * @param cabeceiraCentro The cabeceiraCentro to set
	 */
	public void setCabeceiraCentro(String cabeceiraCentro) {
		this.cabeceiraCentro = cabeceiraCentro;
	}

	/**
	 * Sets the cabeceiraColumnas.
	 * @param cabeceiraColumnas The cabeceiraColumnas to set
	 */
	public void setCabeceiraColumnas(String cabeceiraColumnas) {
		this.cabeceiraColumnas = cabeceiraColumnas;
	}

	/**
	 * Sets the cabeceiraOficial.
	 * @param cabeceiraOficial The cabeceiraOficial to set
	 */
	public void setCabeceiraOficial(String cabeceiraOficial) {
		this.cabeceiraOficial = cabeceiraOficial;
	}

	/**
	 * Sets the cursivaCabeceira.
	 * @param cursivaCabeceira The cursivaCabeceira to set
	 */
	public void setCursivaCabeceira(String cursivaCabeceira) {
		this.cursivaCabeceira = cursivaCabeceira;
	}

	/**
	 * Sets the dataInformePe.
	 * @param dataInformePe The dataInformePe to set
	 */
	public void setDataInformePe(String dataInformePe) {
		this.dataInformePe = dataInformePe;
	}

	/**
	 * Sets the negritaCabeceira.
	 * @param negritaCabeceira The negritaCabeceira to set
	 */
	public void setNegritaCabeceira(String negritaCabeceira) {
		this.negritaCabeceira = negritaCabeceira;
	}

	/**
	 * Sets the orientacionPaxina.
	 * @param orientacionPaxina The orientacionPaxina to set
	 */
	public void setOrientacionPaxina(String orientacionPaxina) {
		this.orientacionPaxina = orientacionPaxina;
	}

	/**
	 * Sets the subraiadoCabeceira.
	 * @param subraiadoCabeceira The subraiadoCabeceira to set
	 */
	public void setSubraiadoCabeceira(String subraiadoCabeceira) {
		this.subraiadoCabeceira = subraiadoCabeceira;
	}

	/**
	 * Returns the etBordeEtiqueta.
	 * @return String
	 */
	public String getEtBordeEtiqueta() {
		return etBordeEtiqueta;
	}

	/**
	 * Returns the etComboFonteDetalle.
	 * @return String
	 */
	public String getEtComboFonteDetalle() {
		return etComboFonteDetalle;
	}

	/**
	 * Returns the etComboTamanoDetalle.
	 * @return String
	 */
	public String getEtComboTamanoDetalle() {
		return etComboTamanoDetalle;
	}

	/**
	 * Sets the etBordeEtiqueta.
	 * @param etBordeEtiqueta The etBordeEtiqueta to set
	 */
	public void setEtBordeEtiqueta(String etBordeEtiqueta) {
		this.etBordeEtiqueta = etBordeEtiqueta;
	}

	/**
	 * Sets the etComboFonteDetalle.
	 * @param etComboFonteDetalle The etComboFonteDetalle to set
	 */
	public void setEtComboFonteDetalle(String etComboFonteDetalle) {
		this.etComboFonteDetalle = etComboFonteDetalle;
	}

	/**
	 * Sets the etComboTamanoDetalle.
	 * @param etComboTamanoDetalle The etComboTamanoDetalle to set
	 */
	public void setEtComboTamanoDetalle(String etComboTamanoDetalle) {
		this.etComboTamanoDetalle = etComboTamanoDetalle;
	}

	/**
	 * Returns the numeroPaxinaPe.
	 * @return String
	 */
	public String getNumeroPaxinaPe() {
		return numeroPaxinaPe;
	}

	/**
	 * Sets the numeroPaxinaPe.
	 * @param numeroPaxinaPe The numeroPaxinaPe to set
	 */
	public void setNumeroPaxinaPe(String numeroPaxinaPe) {
		this.numeroPaxinaPe = numeroPaxinaPe;
	}

	/**
	 * Returns the etCodEtiqueta.
	 * @return String
	 */
	public String getEtCodEtiqueta() {
		return etCodEtiqueta;
	}

	/**
	 * Sets the etCodEtiqueta.
	 * @param etCodEtiqueta The etCodEtiqueta to set
	 */
	public void setEtCodEtiqueta(String etCodEtiqueta) {
		this.etCodEtiqueta = etCodEtiqueta;
	}

	/**
	 * Returns the mNUCODOculto.
	 * @return String
	 */
	public String getMNUCODOculto() {
		return MNUCODOculto;
	}

	/**
	 * Sets the mNUCODOculto.
	 * @param mNUCODOculto The mNUCODOculto to set
	 */
	public void setMNUCODOculto(String mNUCODOculto) {
		MNUCODOculto = mNUCODOculto;
	}

	/**
	 * Returns the codCampoInformeOculto.
	 * @return String
	 */
	public String getCodCampoInformeOculto() {
		return codCampoInformeOculto;
	}

	/**
	 * Sets the codCampoInformeOculto.
	 * @param codCampoInformeOculto The codCampoInformeOculto to set
	 */
	public void setCodCampoInformeOculto(String codCampoInformeOculto) {
		this.codCampoInformeOculto = codCampoInformeOculto;
	}

	/**
	 * Returns the etNome.
	 * @return String
	 */
	public String getEtNome() {
		return etNome;
	}

	/**
	 * Sets the etNome.
	 * @param etNome The etNome to set
	 */
	public void setEtNome(String etNome) {
		this.etNome = etNome;
	}

	/**
	 * Returns the tipoFicheiroSaida.
	 * @return String
	 */
	public String getTipoFicheiroSaida() {
		return tipoFicheiroSaida;
	}

	/**
	 * Sets the tipoFicheiroSaida.
	 * @param tipoFicheiroSaida The tipoFicheiroSaida to set
	 */
	public void setTipoFicheiroSaida(String tipoFicheiroSaida) {
		this.tipoFicheiroSaida = tipoFicheiroSaida;
	}

	/**
	 * Returns the numerarLinhas.
	 * @return String
	 */
	public String getNumerarLinhas() {
		return numerarLinhas;
	}

	/**
	 * Sets the numerarLinhas.
	 * @param numerarLinhas The numerarLinhas to set
	 */
	public void setNumerarLinhas(String numerarLinhas) {
		this.numerarLinhas = numerarLinhas;
	}

	/**
	 * Returns the codCampo_1.
	 * @return String
	 */
	public String getCodCampo_1() {
		return codCampo_1;
	}

	/**
	 * Returns the codCampo_2.
	 * @return String
	 */
	public String getCodCampo_2() {
		return codCampo_2;
	}

	/**
	 * Returns the codCampo_3.
	 * @return String
	 */
	public String getCodCampo_3() {
		return codCampo_3;
	}

	/**
	 * Returns the codCampo_4.
	 * @return String
	 */
	public String getCodCampo_4() {
		return codCampo_4;
	}

	/**
	 * Returns the codCampo_5.
	 * @return String
	 */
	public String getCodCampo_5() {
		return codCampo_5;
	}

	/**
	 * Returns the codCampo_6.
	 * @return String
	 */
	public String getCodCampo_6() {
		return codCampo_6;
	}

	/**
	 * Returns the codCampo_7.
	 * @return String
	 */
	public String getCodCampo_7() {
		return codCampo_7;
	}

	/**
	 * Returns the codCampoOrdenacion_1.
	 * @return String
	 */
	public String getCodCampoOrdenacion_1() {
		return codCampoOrdenacion_1;
	}

	/**
	 * Returns the codCampoOrdenacion_2.
	 * @return String
	 */
	public String getCodCampoOrdenacion_2() {
		return codCampoOrdenacion_2;
	}

	/**
	 * Returns the codCampoOrdenacion_3.
	 * @return String
	 */
	public String getCodCampoOrdenacion_3() {
		return codCampoOrdenacion_3;
	}

	/**
	 * Returns the codCampoOrdenacion_4.
	 * @return String
	 */
	public String getCodCampoOrdenacion_4() {
		return codCampoOrdenacion_4;
	}

	/**
	 * Returns the codCampoOrdenacion_5.
	 * @return String
	 */
	public String getCodCampoOrdenacion_5() {
		return codCampoOrdenacion_5;
	}

	/**
	 * Returns the codEntidadeInforme.
	 * @return String
	 */
	public String getCodEntidadeInforme() {
		return codEntidadeInforme;
	}

	/**
	 * Returns the codFormatoInforme.
	 * @return String
	 */
	public String getCodFormatoInforme() {
		return codFormatoInforme;
	}

	/**
	 * Returns the codOperador_2.
	 * @return String
	 */
	public String getCodOperador_2() {
		return codOperador_2;
	}

	/**
	 * Returns the codOperador_3.
	 * @return String
	 */
	public String getCodOperador_3() {
		return codOperador_3;
	}

	/**
	 * Returns the codOperador_4.
	 * @return String
	 */
	public String getCodOperador_4() {
		return codOperador_4;
	}

	/**
	 * Returns the codOperador_5.
	 * @return String
	 */
	public String getCodOperador_5() {
		return codOperador_5;
	}

	/**
	 * Returns the codOperador_6.
	 * @return String
	 */
	public String getCodOperador_6() {
		return codOperador_6;
	}

	/**
	 * Returns the codOperador_7.
	 * @return String
	 */
	public String getCodOperador_7() {
		return codOperador_7;
	}

	/**
	 * Returns the codSentidoOrdenacion_1.
	 * @return String
	 */
	public String getCodSentidoOrdenacion_1() {
		return codSentidoOrdenacion_1;
	}

	/**
	 * Returns the codSentidoOrdenacion_2.
	 * @return String
	 */
	public String getCodSentidoOrdenacion_2() {
		return codSentidoOrdenacion_2;
	}

	/**
	 * Returns the codSentidoOrdenacion_3.
	 * @return String
	 */
	public String getCodSentidoOrdenacion_3() {
		return codSentidoOrdenacion_3;
	}

	/**
	 * Returns the codSentidoOrdenacion_4.
	 * @return String
	 */
	public String getCodSentidoOrdenacion_4() {
		return codSentidoOrdenacion_4;
	}

	/**
	 * Returns the codSentidoOrdenacion_5.
	 * @return String
	 */
	public String getCodSentidoOrdenacion_5() {
		return codSentidoOrdenacion_5;
	}

	/**
	 * Sets the codCampo_1.
	 * @param codCampo_1 The codCampo_1 to set
	 */
	public void setCodCampo_1(String codCampo_1) {
		this.codCampo_1 = codCampo_1;
	}

	/**
	 * Sets the codCampo_2.
	 * @param codCampo_2 The codCampo_2 to set
	 */
	public void setCodCampo_2(String codCampo_2) {
		this.codCampo_2 = codCampo_2;
	}

	/**
	 * Sets the codCampo_3.
	 * @param codCampo_3 The codCampo_3 to set
	 */
	public void setCodCampo_3(String codCampo_3) {
		this.codCampo_3 = codCampo_3;
	}

	/**
	 * Sets the codCampo_4.
	 * @param codCampo_4 The codCampo_4 to set
	 */
	public void setCodCampo_4(String codCampo_4) {
		this.codCampo_4 = codCampo_4;
	}

	/**
	 * Sets the codCampo_5.
	 * @param codCampo_5 The codCampo_5 to set
	 */
	public void setCodCampo_5(String codCampo_5) {
		this.codCampo_5 = codCampo_5;
	}

	/**
	 * Sets the codCampo_6.
	 * @param codCampo_6 The codCampo_6 to set
	 */
	public void setCodCampo_6(String codCampo_6) {
		this.codCampo_6 = codCampo_6;
	}

	/**
	 * Sets the codCampo_7.
	 * @param codCampo_7 The codCampo_7 to set
	 */
	public void setCodCampo_7(String codCampo_7) {
		this.codCampo_7 = codCampo_7;
	}

	/**
	 * Sets the codCampoOrdenacion_1.
	 * @param codCampoOrdenacion_1 The codCampoOrdenacion_1 to set
	 */
	public void setCodCampoOrdenacion_1(String codCampoOrdenacion_1) {
		this.codCampoOrdenacion_1 = codCampoOrdenacion_1;
	}

	/**
	 * Sets the codCampoOrdenacion_2.
	 * @param codCampoOrdenacion_2 The codCampoOrdenacion_2 to set
	 */
	public void setCodCampoOrdenacion_2(String codCampoOrdenacion_2) {
		this.codCampoOrdenacion_2 = codCampoOrdenacion_2;
	}

	/**
	 * Sets the codCampoOrdenacion_3.
	 * @param codCampoOrdenacion_3 The codCampoOrdenacion_3 to set
	 */
	public void setCodCampoOrdenacion_3(String codCampoOrdenacion_3) {
		this.codCampoOrdenacion_3 = codCampoOrdenacion_3;
	}

	/**
	 * Sets the codCampoOrdenacion_4.
	 * @param codCampoOrdenacion_4 The codCampoOrdenacion_4 to set
	 */
	public void setCodCampoOrdenacion_4(String codCampoOrdenacion_4) {
		this.codCampoOrdenacion_4 = codCampoOrdenacion_4;
	}

	/**
	 * Sets the codCampoOrdenacion_5.
	 * @param codCampoOrdenacion_5 The codCampoOrdenacion_5 to set
	 */
	public void setCodCampoOrdenacion_5(String codCampoOrdenacion_5) {
		this.codCampoOrdenacion_5 = codCampoOrdenacion_5;
	}

	/**
	 * Sets the codEntidadeInforme.
	 * @param codEntidadeInforme The codEntidadeInforme to set
	 */
	public void setCodEntidadeInforme(String codEntidadeInforme) {
		this.codEntidadeInforme = codEntidadeInforme;
	}

	/**
	 * Sets the codFormatoInforme.
	 * @param codFormatoInforme The codFormatoInforme to set
	 */
	public void setCodFormatoInforme(String codFormatoInforme) {
		this.codFormatoInforme = codFormatoInforme;
	}

	/**
	 * Sets the codOperador_2.
	 * @param codOperador_2 The codOperador_2 to set
	 */
	public void setCodOperador_2(String codOperador_2) {
		this.codOperador_2 = codOperador_2;
	}

	/**
	 * Sets the codOperador_3.
	 * @param codOperador_3 The codOperador_3 to set
	 */
	public void setCodOperador_3(String codOperador_3) {
		this.codOperador_3 = codOperador_3;
	}

	/**
	 * Sets the codOperador_4.
	 * @param codOperador_4 The codOperador_4 to set
	 */
	public void setCodOperador_4(String codOperador_4) {
		this.codOperador_4 = codOperador_4;
	}

	/**
	 * Sets the codOperador_5.
	 * @param codOperador_5 The codOperador_5 to set
	 */
	public void setCodOperador_5(String codOperador_5) {
		this.codOperador_5 = codOperador_5;
	}

	/**
	 * Sets the codOperador_6.
	 * @param codOperador_6 The codOperador_6 to set
	 */
	public void setCodOperador_6(String codOperador_6) {
		this.codOperador_6 = codOperador_6;
	}

	/**
	 * Sets the codOperador_7.
	 * @param codOperador_7 The codOperador_7 to set
	 */
	public void setCodOperador_7(String codOperador_7) {
		this.codOperador_7 = codOperador_7;
	}

	/**
	 * Sets the codSentidoOrdenacion_1.
	 * @param codSentidoOrdenacion_1 The codSentidoOrdenacion_1 to set
	 */
	public void setCodSentidoOrdenacion_1(String codSentidoOrdenacion_1) {
		this.codSentidoOrdenacion_1 = codSentidoOrdenacion_1;
	}

	/**
	 * Sets the codSentidoOrdenacion_2.
	 * @param codSentidoOrdenacion_2 The codSentidoOrdenacion_2 to set
	 */
	public void setCodSentidoOrdenacion_2(String codSentidoOrdenacion_2) {
		this.codSentidoOrdenacion_2 = codSentidoOrdenacion_2;
	}

	/**
	 * Sets the codSentidoOrdenacion_3.
	 * @param codSentidoOrdenacion_3 The codSentidoOrdenacion_3 to set
	 */
	public void setCodSentidoOrdenacion_3(String codSentidoOrdenacion_3) {
		this.codSentidoOrdenacion_3 = codSentidoOrdenacion_3;
	}

	/**
	 * Sets the codSentidoOrdenacion_4.
	 * @param codSentidoOrdenacion_4 The codSentidoOrdenacion_4 to set
	 */
	public void setCodSentidoOrdenacion_4(String codSentidoOrdenacion_4) {
		this.codSentidoOrdenacion_4 = codSentidoOrdenacion_4;
	}

	/**
	 * Sets the codSentidoOrdenacion_5.
	 * @param codSentidoOrdenacion_5 The codSentidoOrdenacion_5 to set
	 */
	public void setCodSentidoOrdenacion_5(String codSentidoOrdenacion_5) {
		this.codSentidoOrdenacion_5 = codSentidoOrdenacion_5;
	}

	/**
	 * Returns the codCondicion_1.
	 * @return String
	 */
	public String getCodCondicion_1() {
		return codCondicion_1;
	}

	/**
	 * Returns the codCondicion_2.
	 * @return String
	 */
	public String getCodCondicion_2() {
		return codCondicion_2;
	}

	/**
	 * Returns the codCondicion_3.
	 * @return String
	 */
	public String getCodCondicion_3() {
		return codCondicion_3;
	}

	/**
	 * Returns the codCondicion_4.
	 * @return String
	 */
	public String getCodCondicion_4() {
		return codCondicion_4;
	}

	/**
	 * Returns the codCondicion_5.
	 * @return String
	 */
	public String getCodCondicion_5() {
		return codCondicion_5;
	}

	/**
	 * Returns the codCondicion_6.
	 * @return String
	 */
	public String getCodCondicion_6() {
		return codCondicion_6;
	}

	/**
	 * Returns the codCondicion_7.
	 * @return String
	 */
	public String getCodCondicion_7() {
		return codCondicion_7;
	}

	/**
	 * Sets the codCondicion_1.
	 * @param codCondicion_1 The codCondicion_1 to set
	 */
	public void setCodCondicion_1(String codCondicion_1) {
		this.codCondicion_1 = codCondicion_1;
	}

	/**
	 * Sets the codCondicion_2.
	 * @param codCondicion_2 The codCondicion_2 to set
	 */
	public void setCodCondicion_2(String codCondicion_2) {
		this.codCondicion_2 = codCondicion_2;
	}

	/**
	 * Sets the codCondicion_3.
	 * @param codCondicion_3 The codCondicion_3 to set
	 */
	public void setCodCondicion_3(String codCondicion_3) {
		this.codCondicion_3 = codCondicion_3;
	}

	/**
	 * Sets the codCondicion_4.
	 * @param codCondicion_4 The codCondicion_4 to set
	 */
	public void setCodCondicion_4(String codCondicion_4) {
		this.codCondicion_4 = codCondicion_4;
	}

	/**
	 * Sets the codCondicion_5.
	 * @param codCondicion_5 The codCondicion_5 to set
	 */
	public void setCodCondicion_5(String codCondicion_5) {
		this.codCondicion_5 = codCondicion_5;
	}

	/**
	 * Sets the codCondicion_6.
	 * @param codCondicion_6 The codCondicion_6 to set
	 */
	public void setCodCondicion_6(String codCondicion_6) {
		this.codCondicion_6 = codCondicion_6;
	}

	/**
	 * Sets the codCondicion_7.
	 * @param codCondicion_7 The codCondicion_7 to set
	 */
	public void setCodCondicion_7(String codCondicion_7) {
		this.codCondicion_7 = codCondicion_7;
	}

	/**
	 * Returns the codAplicacion.
	 * @return String
	 */
	public String getCodAplicacion() {
		return codAplicacion;
	}

	/**
	 * Sets the codAplicacion.
	 * @param codAplicacion The codAplicacion to set
	 */
	public void setCodAplicacion(String codAplicacion) {
		this.codAplicacion = codAplicacion;
	}

	
	/**
	 * Returns the ficheroWord.
	 * @return org.apache.struts.upload.FormFile
	 */
	public org.apache.struts.upload.FormFile getFicheroWord() {
		return ficheroWord;
	}

	/**
	 * Sets the ficheroWord.
	 * @param ficheroWord The ficheroWord to set
	 */
	public void setFicheroWord(org.apache.struts.upload.FormFile ficheroWord) {
		this.ficheroWord = ficheroWord;
	}

}
