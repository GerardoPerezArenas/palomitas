package es.altia.agora.business.integracionsw.exception;

public class FaltaDatoObligatorioException extends Exception {

    private String nombreCampo;
    

    public FaltaDatoObligatorioException(String nombreCampo) {
        super();
        this.nombreCampo = nombreCampo;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }
}
