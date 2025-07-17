package es.altia.agora.business.integracionsw;

import java.io.Serializable;

public class DefinicionParametroEntradaVO implements Serializable {

    private int codigoParam;
    private int codigoOpDef;
    private int codigoSW;
    private String nombreOpSW;
    private String descParam;
    private String tituloParam;
    private boolean esObligatorio;
    private int tipoParametro;
    private String valorDefecto;

    public int getCodigoParam() {
        return codigoParam;
    }

    public void setCodigoParam(int codigoParam) {
        this.codigoParam = codigoParam;
    }

    public int getCodigoOpDef() {
        return codigoOpDef;
    }

    public void setCodigoOpDef(int codigoOpDef) {
        this.codigoOpDef = codigoOpDef;
    }

    public int getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(int codigoSW) {
        this.codigoSW = codigoSW;
    }

    public String getNombreOpSW() {
        return nombreOpSW;
    }

    public void setNombreOpSW(String nombreOpSW) {
        this.nombreOpSW = nombreOpSW;
    }

    public String getDescParam() {
        return descParam;
    }

    public void setDescParam(String descParam) {
        this.descParam = descParam;
    }

    public String getTituloParam() {
        return tituloParam;
    }

    public void setTituloParam(String tituloParam) {
        this.tituloParam = tituloParam;
    }

    public boolean isEsObligatorio() {
        return esObligatorio;
    }

    public void setEsObligatorio(boolean esObligatorio) {
        this.esObligatorio = esObligatorio;
    }

    public int getTipoParametro() {
        return tipoParametro;
    }

    public void setTipoParametro(int tipoParametro) {
        this.tipoParametro = tipoParametro;
    }

    public String getValorDefecto() {
        return valorDefecto;
    }

    public void setValorDefecto(String valorDefecto) {
        this.valorDefecto = valorDefecto;
    }

    public DefinicionParametroEntradaVO copy() {
        DefinicionParametroEntradaVO nuevoParam = new DefinicionParametroEntradaVO();
        nuevoParam.setCodigoOpDef(this.getCodigoOpDef());
        nuevoParam.setCodigoParam(this.getCodigoParam());
        nuevoParam.setCodigoSW(this.getCodigoSW());
        nuevoParam.setDescParam(this.getDescParam());
        nuevoParam.setEsObligatorio(this.isEsObligatorio());
        nuevoParam.setNombreOpSW(this.getNombreOpSW());
        nuevoParam.setTipoParametro(this.getTipoParametro());
        nuevoParam.setTituloParam(this.getTituloParam());
        nuevoParam.setValorDefecto(this.getValorDefecto());
        return nuevoParam;
    }

    public String toString() {
        return codigoParam + " | " + codigoOpDef + " | " + codigoSW + " | " + nombreOpSW + " | " + descParam + " | " +
                tituloParam + " | " + esObligatorio + " | " + tipoParametro + " | " + valorDefecto;
    }
}
