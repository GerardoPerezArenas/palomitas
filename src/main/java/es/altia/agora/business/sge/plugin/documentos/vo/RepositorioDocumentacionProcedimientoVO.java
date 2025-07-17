package es.altia.agora.business.sge.plugin.documentos.vo;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author oscar
 */
public class RepositorioDocumentacionProcedimientoVO implements Serializable{
    private int id;    
    private String codProcedimiento;
    private String nombreProcedimiento;
    private String nombrePlugin;
    private String implClassPlugin;
    private Calendar fechaAlta;
    private int idPlugin;
    private String digitalizacion;

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
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the nombreProcedimiento
     */
    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    /**
     * @param nombreProcedimiento the nombreProcedimiento to set
     */
    public void setNombreProcedimiento(String nombreProcedimiento) {
        this.nombreProcedimiento = nombreProcedimiento;
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

    /**
     * @return the idPlugin
     */
    public int getIdPlugin() {
        return idPlugin;
    }

    /**
     * @param idPlugin the idPlugin to set
     */
    public void setIdPlugin(int idPlugin) {
        this.idPlugin = idPlugin;
    }  

    /**
     * @return the digitalizacion
     */
    public String getDigitalizacion() {
        return digitalizacion;
    }

    /**
     * @param digitalizacion the digitalizacion to set
     */
    public void setDigitalizacion(String digitalizacion) {
        this.digitalizacion = digitalizacion;
    }
    
    
}
