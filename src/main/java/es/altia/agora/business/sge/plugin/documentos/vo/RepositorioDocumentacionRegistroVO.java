package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.Calendar;

/**
 *
 * @author oscar
 */
public class RepositorioDocumentacionRegistroVO {
    private int id;    
    private int idAlmacen;
    private String nombrePlugin;
    private String implClassPlugin;
    private Calendar fechaAlta;

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
     * @return the implClassPlugin
     */
    public String getImplClassPlugin() {
        return implClassPlugin;
    }

    /**
     * @param implClassPlugin the implClassPlugin to set
     */
    public void setImplClassPlugin(String implClassPlugin) {
        this.implClassPlugin = implClassPlugin;
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

    /**
     * @return the idAlmacen
     */
    public int getIdAlmacen() {
        return idAlmacen;
    }

    /**
     * @param idAlmacen the idAlmacen to set
     */
    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    /**
     * @return the nombrePlugin
     */
    public String getNombrePlugin() {
        return nombrePlugin;
    }

    /**
     * @param nombrePlugin the nombrePlugin to set
     */
    public void setNombrePlugin(String nombrePlugin) {
        this.nombrePlugin = nombrePlugin;
    }
}
