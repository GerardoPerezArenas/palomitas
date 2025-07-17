package es.altia.agora.business.integracionsw;

import java.io.Serializable;

public class ParametroConfigurableVO implements Serializable {
	private long cfo;
    private int codigoOp;
    private int ordenParam;
    private String nombreDefinicion;
    private String tituloParam;
    private int tipoValorPaso;
    private String codCampoExp;
    private String valorConstante;
    private int obligatorio;

    public ParametroConfigurableVO() {
        this.codCampoExp = "";
        this.valorConstante = "";    	
    }

    public ParametroConfigurableVO(long cfo, int codigoOp, int ordenParam, String nombreDefinicion, String tituloParam,
                                   int tipoValorPaso, String codCampoExp, String valorConstante) {
    	this.cfo = cfo;
        this.codigoOp = codigoOp;
        this.ordenParam = ordenParam;
        this.nombreDefinicion = nombreDefinicion;
        this.tituloParam = tituloParam;
        this.tipoValorPaso = tipoValorPaso;
        this.codCampoExp = codCampoExp;
        this.valorConstante = valorConstante;
    }

    public long getCfo() {
		return cfo;
	}

	public void setCfo(long cfo) {
		this.cfo = cfo;
	}

    public int getCodigoOp() {
        return codigoOp;
    }

    public void setCodigoOp(int codigoOp) {
        this.codigoOp = codigoOp;
    }

    public int getOrdenParam() {
        return ordenParam;
    }

    public void setOrdenParam(int ordenParam) {
        this.ordenParam = ordenParam;
    }

    public String getNombreDefinicion() {
        return nombreDefinicion;
    }

    public void setNombreDefinicion(String nombreDefinicion) {
        this.nombreDefinicion = nombreDefinicion;
    }

    public String getTituloParam() {
        return tituloParam;
    }

    public void setTituloParam(String tituloParam) {
        this.tituloParam = tituloParam;
    }

    public int getTipoValorPaso() {
        return tipoValorPaso;
    }

    public void setTipoValorPaso(int tipoValorPaso) {
        this.tipoValorPaso = tipoValorPaso;
    }

    public String getCodCampoExp() {
        return codCampoExp;
    }

    public void setCodCampoExp(String codCampoExp) {
        this.codCampoExp = codCampoExp;
    }

    public String getValorConstante() {
        return valorConstante;
    }

    public void setValorConstante(String valorConstante) {
        this.valorConstante = valorConstante;
    }

    public String toString() {
        return cfo + " | " + codigoOp + " | " + ordenParam
        		 + " | " + nombreDefinicion + " | " + tituloParam + " | " +
                tipoValorPaso + " | " + obligatorio + " | " + codCampoExp + " | " + valorConstante;
    }

	public int getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(int obligatorio) {
		this.obligatorio = obligatorio;
    }
}
