package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.Calendar;

/**
 * Representa a un plugin de almacenamiento de documentos
 * @author oscar
 */
public class PluginAlmacenamientoVO {
    
    // Identificador del plugin en la tabla ALMACEN_DOC_DISPONIBLES
    private int id;    
    // Nombre del plugin
    private String nombre;
    // Clase que implementa el plugin
    private String implClass;
    // Fecha de alta del registro en base de datos
    private Calendar fechaAlta;

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

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the fechaAlta
     */
    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * @param fechaAlta the fechaAlta to set
     */
    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    
    
}