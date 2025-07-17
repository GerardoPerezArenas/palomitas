package es.altia.agora.business.administracion.mantenimiento;

public class TipoDocumentoVO {
    private String codigo;
    private String descripcion;
    private String personaFisica;

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
     * @return the personaFisica
     */
    public String getPersonaFisica() {
        return personaFisica;
    }

    /**
     * @param personaFisica the personaFisica to set
     */
    public void setPersonaFisica(String personaFisica) {
        this.personaFisica = personaFisica;
    }
    
}
