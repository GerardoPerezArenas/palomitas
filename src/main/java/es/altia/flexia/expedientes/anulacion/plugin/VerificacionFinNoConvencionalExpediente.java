package es.altia.flexia.expedientes.anulacion.plugin;

import es.altia.flexia.expedientes.anulacion.exception.VerificacionFinNoConvencionalExpedienteException;

/**
 * Clase padre de todos los plugin de verificación de finalización no convencinal de un expediente 
 */
public abstract class VerificacionFinNoConvencionalExpediente {

    private String nombre;
    private String descripcion;
    private String implClass;

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
     * @return the implClass
     */
    public String getImplClass() {
        return implClass;
    }

    /**
     * @param implClass the implClass to set
     */
    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }
   
    
    public abstract boolean verificarFinalizacionNoConvencional(int codOrganizacion,String codProcedimiento,String numExpediente,int codUsuario,String loginUsuario) throws VerificacionFinNoConvencionalExpedienteException;
    
    
}
