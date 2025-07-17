package es.altia.common.exception;     

public class ImportacionFlujoBibliotecaException extends Exception{
    
    private int estado;
    
    
    public ImportacionFlujoBibliotecaException(int estado){
        super();
        this.estado = estado;        
    }
    
    
    public ImportacionFlujoBibliotecaException(int estado,String message){
        super();
        this.estado = estado;        
    }
    
    

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }
}
