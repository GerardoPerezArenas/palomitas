package es.altia.agora.business.integracionsw;

import java.io.Serializable;
import java.util.Vector;

public class InfoConfTramSWVO implements Serializable {

    private int codOpSW;
    private boolean obligatorio;
    private String tituloOperacion;
    private Vector listaParamsEntrada;
    private Vector listaParamsSalida;
    private int codMunicipio;
    private String txtCodigo;
    private int codigoTramite;
    private boolean operacionModulo=false;
    private String nombreModulo;

    public InfoConfTramSWVO() {}

    public InfoConfTramSWVO(int codOpSW, boolean obligatorio, String tituloOperacion, Vector listaParamsEntrada,
                            Vector listaParamsSalida) {
        this.codOpSW = codOpSW;
        this.obligatorio = obligatorio;
        this.tituloOperacion = tituloOperacion;
        this.listaParamsEntrada = listaParamsEntrada;
        this.listaParamsSalida = listaParamsSalida;
    }

    public int getCodOpSW() {
        return codOpSW;
    }

    public void setCodOpSW(int codOpSW) {
        this.codOpSW = codOpSW;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Vector getListaParamsEntrada() {
        return listaParamsEntrada;
    }

    public void setListaParamsEntrada(Vector listaParamsEntrada) {
        this.listaParamsEntrada = listaParamsEntrada;
    }

    public Vector getListaParamsSalida() {
        return listaParamsSalida;
    }

    public void setListaParamsSalida(Vector listaParamsSalida) {
        this.listaParamsSalida = listaParamsSalida;
    }

    public String getTituloOperacion() {
        return tituloOperacion;
    }

    public void setTituloOperacion(String tituloOperacion) {
        this.tituloOperacion = tituloOperacion;
    }

	public int getCodigoTramite() {
		return codigoTramite;
	}

	public void setCodigoTramite(int codigoTramite) {
		this.codigoTramite = codigoTramite;
	}

	public int getCodMunicipio() {
		return codMunicipio;
	}

	public void setCodMunicipio(int codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

	public String getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(String txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

    /**
     * @return the operacionModulo
     */
    public boolean isOperacionModulo() {
        return operacionModulo;
    }

    /**
     * @param operacionModulo the operacionModulo to set
     */
    public void setOperacionModulo(boolean operacionModulo) {
        this.operacionModulo = operacionModulo;
    }

    /**
     * @return the nombreModulo
     */
    public String getNombreModulo() {
        return nombreModulo;
    }

    /**
     * @param nombreModulo the nombreModulo to set
     */
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }
}
