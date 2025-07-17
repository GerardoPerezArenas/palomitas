package es.altia.agora.business.sge;

import java.io.Serializable;

/**
 * Errores producidos durante
 * @author oscar.rodriguez
 */
public class ErrorImportacionXPDL implements Serializable {
    // Código identificativo del error producido
    private int codError;
    // Descripción del error internacionalizado.
    private String descripcionError;

    public ErrorImportacionXPDL(){
        
    }
    
    public ErrorImportacionXPDL(int codError, String descripcionError){
        this.codError = codError;
        this.descripcionError = descripcionError;
    }
    
    /**
     * @return the codError
     */
    public int getCodError() {
        return codError;
    }

    /**
     * @param codError the codError to set
     */
    public void setCodError(int codError) {
        this.codError = codError;
    }

    /**
     * @return the descripcionError
     */
    public String getDescripcionError() {
        return descripcionError;
    }

    /**
     * @param descripcionError the descripcionError to set
     */
    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }
    
}