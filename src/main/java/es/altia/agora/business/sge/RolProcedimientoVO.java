package es.altia.agora.business.sge;

/**
 *
 * @author oscar.rodriguez
 */
public class RolProcedimientoVO {

    private String codigo;
    private String descripcion;
    private String defecto;
    private String estado;

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the defecto
     */
    public String getDefecto() {
        return defecto;
    }

    /**
     * @param defecto the defecto to set
     */
    public void setDefecto(String defecto) {
        this.defecto = defecto;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}
