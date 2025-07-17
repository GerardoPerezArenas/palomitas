package es.altia.flexia.terceros.integracion.externa.excepciones;

public class CamposObligatoriosTerceroExternoException extends Exception {
    
    private String listaCamposObligatorios = null;

    public CamposObligatoriosTerceroExternoException(String message,String listaCampos){
        super(message);
        this.listaCamposObligatorios = listaCampos;
    }

    public CamposObligatoriosTerceroExternoException(Throwable exception,String listaCampos){
        super(exception);        
        this.listaCamposObligatorios = listaCampos;
    }

    public CamposObligatoriosTerceroExternoException(String message,Throwable exception,String listaCampos){
        super(message,exception);        
        this.listaCamposObligatorios = listaCampos;
    }
    
    /**
     * @return the listaCamposObligatorios
     */
    public String getListaCamposObligatorios() {
        return listaCamposObligatorios;
    }

    /**
     * @param listaCamposObligatorios the listaCamposObligatorios to set
     */
    public void setListaCamposObligatorios(String listaCamposObligatorios) {
        this.listaCamposObligatorios = listaCamposObligatorios;
    }

}