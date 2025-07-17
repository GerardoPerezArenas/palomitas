package es.altia.agora.business.administracion.mantenimiento.persistence.manual;


public class UorPermisoVO {
    
    private int status;
    private String codigoUor;
    private String codigoUorVisible;
    private String descripcionUor;

    /**
     * @return the codigoUor
     */
    public String getCodigoUor() {
        return codigoUor;
    }

    /**
     * @param codigoUor the codigoUor to set
     */
    public void setCodigoUor(String codigoUor) {
        this.codigoUor = codigoUor;
    }

    /**
     * @return the descripcionUor
     */
    public String getDescripcionUor() {
        return descripcionUor;
    }

    /**
     * @param descripcionUor the descripcionUor to set
     */
    public void setDescripcionUor(String descripcionUor) {
        this.descripcionUor = descripcionUor;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the codigoUorVisible
     */
    public String getCodigoUorVisible() {
        return codigoUorVisible;
    }

    /**
     * @param codigoUorVisible the codigoUorVisible to set
     */
    public void setCodigoUorVisible(String codigoUorVisible) {
        this.codigoUorVisible = codigoUorVisible;
    }
       
    
}