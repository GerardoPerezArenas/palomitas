package es.altia.flexia.portafirmasexternocliente.vo;

/**
 * @author david.caamano
 * @version 12/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 12/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class InfoTramiteVO {
    
    private String descripcionTramite;
    private String fechaInicioTramite;

    public String getDescripcionTramite() {
        return descripcionTramite;
    }//getDescripcionTramite
    public void setDescripcionTramite(String descripcionTramite) {
        this.descripcionTramite = descripcionTramite;
    }//setDescripcionTramite

    public String getFechaInicioTramite() {
        return fechaInicioTramite;
    }//getFechaInicioTramite
    public void setFechaInicioTramite(String fechaInicioTramite) {
        this.fechaInicioTramite = fechaInicioTramite;
    }//setFechaInicioTramite
    
}//class
