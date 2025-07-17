package es.altia.agora.business.integracionsw;

public class DefinicionArraySWVO {

    private int codigoOpDef;
    private int codigoParam;
    private String descParamArray;
    private int entradaSalida;
    private int numRepeticiones;

    public DefinicionArraySWVO() {}
    
    public int getCodigoOpDef() {
        return codigoOpDef;
    }

    public void setCodigoOpDef(int codigoOpDef) {
        this.codigoOpDef = codigoOpDef;
    }

    public String getDescParamArray() {
        return descParamArray;
    }

    public void setDescParamArray(String descParamArray) {
        this.descParamArray = descParamArray;
    }

    public int getEntradaSalida() {
        return entradaSalida;
    }

    public void setEntradaSalida(int entradaSalida) {
        this.entradaSalida = entradaSalida;
    }

    public int getNumRepeticiones() {
        return numRepeticiones;
    }

    public void setNumRepeticiones(int numRepeticiones) {
        this.numRepeticiones = numRepeticiones;
    }

    public int getCodigoParam() {
        return codigoParam;
    }

    public void setCodigoParam(int codigoParam) {
        this.codigoParam = codigoParam;
    }

    public String toString() {
        return "CodigoParam" + codigoParam + " | CodigoOpDef " + codigoOpDef + " | DescParamArray " + descParamArray + " | EntradaSalida " +
                entradaSalida + " | NumRepeticiones " + numRepeticiones;
    }

    public DefinicionArraySWVO copy() {
        DefinicionArraySWVO nuevoParam = new DefinicionArraySWVO();
        nuevoParam.setCodigoOpDef(this.getCodigoOpDef());
        nuevoParam.setCodigoParam(this.getCodigoParam());
        nuevoParam.setEntradaSalida(this.getEntradaSalida());
        nuevoParam.setDescParamArray(this.getDescParamArray());
        nuevoParam.setNumRepeticiones(this.getNumRepeticiones());
        return nuevoParam;
    }

}
