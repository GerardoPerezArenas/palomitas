package es.altia.agora.business.sge.plugin.documentos.exception;

/**
 * Excepción que se lanza si ocurre algún error al guardar/modificar o recuperar un documento de tramitación
 * @author oscar.rodriguez
 */
public class AlmacenDocumentoTramitacionException extends Exception{

    private int codigo;
    private String descripcionError;

    
    public AlmacenDocumentoTramitacionException(int codigo,String message){
        this.codigo = codigo;
        this.descripcionError = message;
    }    

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
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