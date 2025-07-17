package es.altia.agora.business.integracionsw;

public class DefinicionOperacionVO {

    private int codigoDefinicionOp;
    private String nombreDefinicionOp;
    private boolean publicada;
    private int codigoSW;
    private String nombreOpWSDL;
    private boolean estructuraDef;


    public DefinicionOperacionVO(int codigoDefinicionOp, String nombreDefinicionOp, boolean publicada, int codigoSW,
                                 String nombreOpWSDL, boolean estructuraDef) {
        this.codigoDefinicionOp = codigoDefinicionOp;
        this.nombreDefinicionOp = nombreDefinicionOp;
        this.publicada = publicada;
        this.codigoSW = codigoSW;
        this.nombreOpWSDL = nombreOpWSDL;
        this.estructuraDef = estructuraDef;
    }


    public int getCodigoDefinicionOp() {
        return codigoDefinicionOp;
    }

    public void setCodigoDefinicionOp(int codigoDefinicionOp) {
        this.codigoDefinicionOp = codigoDefinicionOp;
    }

    public String getNombreDefinicionOp() {
        return nombreDefinicionOp;
    }

    public void setNombreDefinicionOp(String nombreDefinicionOp) {
        this.nombreDefinicionOp = nombreDefinicionOp;
    }

    public boolean isPublicada() {
        return publicada;
    }

    public void setPublicada(boolean publicada) {
        this.publicada = publicada;
    }

    public int getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(int codigoSW) {
        this.codigoSW = codigoSW;
    }

    public String getNombreOpWSDL() {
        return nombreOpWSDL;
    }

    public void setNombreOpWSDL(String nombreOpWSDL) {
        this.nombreOpWSDL = nombreOpWSDL;
    }

    public boolean isEstructuraDef() {
        return estructuraDef;
    }

    public void setEstructuraDef(boolean estructuraDef) {
        this.estructuraDef = estructuraDef;
    }
}
