package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

/**
 * Valor que puede tomar un campo desplegable
 */
public class ValorCampoDesplegableModuloIntegracionVO {
    private String codigo;
    private String descripcion;

    /**
     * Devuelve el código que identifica a un valor de un campo desplegable
     * @return código
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Permite indicar el código de un valor de un campo suplementario
     * @param codigo: Código del valor de un campo suplementario
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

   /**
     * Devuelve el nombre otorgado al valor del campo suplementario
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }


   /**
     * Permite establecer la descripción del valor de un campo suplementario
     * @param descripcion: Descripción del valor del campo
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
}