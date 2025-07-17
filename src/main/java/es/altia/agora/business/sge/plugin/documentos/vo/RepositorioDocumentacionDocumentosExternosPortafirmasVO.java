package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.Calendar;

/**
 *
 * @author oscar
 */
public class RepositorioDocumentacionDocumentosExternosPortafirmasVO {
    private int id;    
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
}