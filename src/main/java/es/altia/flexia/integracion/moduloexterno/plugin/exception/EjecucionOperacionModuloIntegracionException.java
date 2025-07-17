package es.altia.flexia.integracion.moduloexterno.plugin.exception;

/**
 *
 * @author Administrador
 */
public class EjecucionOperacionModuloIntegracionException extends Exception{
    private String operacion;
    private String descripcionModulo;
    private String nombreModulo;
    private String codigoErrorOperacion;

    public EjecucionOperacionModuloIntegracionException(String operacion,String descripcionModulo,String nombreModulo,String codigoErrorOperacion) {
        super();
        this.operacion    = operacion;
        this.descripcionModulo = nombreModulo;
        this.nombreModulo = nombreModulo;
        this.codigoErrorOperacion = codigoErrorOperacion;
    }

    /**
     * @return the operacion
     */
    public String getOperacion() {
        return operacion;
    }

    /**
     * @param operacion the operacion to set
     */
    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    /**
     * @return the nombreModulo
     */
    public String getDescripcionModulo() {
        return descripcionModulo;
    }

    /**
     * @param nombreModulo the nombreModulo to set
     */
    public void setDescripcionModulo(String descripcionModulo) {
        this.descripcionModulo = descripcionModulo;
    }

    public String getNombreModulo(){
        return this.nombreModulo;
    }

    public void setNombreModulo(String nombre){
        this.nombreModulo = nombre;
    }
    
    /**
     * @return the codigoErrorOperacion
     */
    public String getCodigoErrorOperacion() {
        return codigoErrorOperacion;
    }

    /**
     * @param codigoErrorOperacion the codigoErrorOperacion to set
     */
    public void setCodigoErrorOperacion(String codigoErrorOperacion) {
        this.codigoErrorOperacion = codigoErrorOperacion;
    }
}
