package es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.vo;

/**
 *
 * @author oscar
 */
public class DocumentoExternoPortafirmasVO {
    private int codDocumento;
    private int codOrganizacion;
    private int ejercicio;
    private String[] params;

    /**
     * @return the codDocumento
     */
    public int getCodDocumento() {
        return codDocumento;
    }

    /**
     * @param codDocumento the codDocumento to set
     */
    public void setCodDocumento(int codDocumento) {
        this.codDocumento = codDocumento;
    }

    /**
     * @return the codOrganizacion
     */
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    /**
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }    

    /**
     * @return the params
     */
    public String[] getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(String[] params) {
        this.params = params;
    }
    
}
