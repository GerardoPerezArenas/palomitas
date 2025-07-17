package es.altia.agora.business.integracionsw;

public class DefinicionParametroSalidaVO {

    private int codOpDef;
    private int codigoSW;
    private String nombreOpSW;
    private String descParam;
    private String tituloParam;
    private int tipoDato;
    private String valorCorrecto;

    public int getCodOpDef() {
        return codOpDef;
    }

    public void setCodOpDef(int codOpDef) {
        this.codOpDef = codOpDef;
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

    public int getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(int tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getValorCorrecto() {
        return valorCorrecto;
    }

    public void setValorCorrecto(String valorCorrecto) {
        this.valorCorrecto = valorCorrecto;
    }
}
