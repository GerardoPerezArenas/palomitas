package es.altia.agora.interfaces.user.web.registro.mantenimiento.exceptions;

/**
 * Excepción que se lanza si intentamos eliminar la clasificación
 * de un asunto, y existen tipos de asunto con esta clasificación.
 * @author mai
 */
public class EliminarClasificacionAsuntoException extends Exception{

    private int codigo;
    private String descripcionError;

   public EliminarClasificacionAsuntoException(){
     
    }  
    
    public EliminarClasificacionAsuntoException(int codigo,String message){
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