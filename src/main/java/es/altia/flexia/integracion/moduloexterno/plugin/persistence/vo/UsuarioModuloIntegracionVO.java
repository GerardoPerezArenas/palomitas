package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.util.Date;

/**
 * @author david.caamano
 * @version 16/01/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16/01/2013 * Edición inicial</li>
 * </ol> 
 */
public class UsuarioModuloIntegracionVO {
    
    private Integer usuCod;
    private String usuLog;
    private String usuNom;
    private Integer usuFirmante;
    private String usuEmail;
    private String usuNif;
    private Date usuFechaBaja;

    public Integer getUsuCod() {
        return usuCod;
    }//getUsuCod
    public void setUsuCod(Integer usuCod) {
        this.usuCod = usuCod;
    }//setUsuCod
    
    public String getUsuLog() {
        return usuLog;
    }//getUsuLog
    public void setUsuLog(String usuLog) {
        this.usuLog = usuLog;
    }//setUsuLog
    
    public String getUsuNom() {
        return usuNom;
    }//getUsuNom
    public void setUsuNom(String usuNom) {
        this.usuNom = usuNom;
    }//setUsuNom

    public Integer getUsuFirmante() {
        return usuFirmante;
    }//getUsuFirmante
    public void setUsuFirmante(Integer usuFirmante) {
        this.usuFirmante = usuFirmante;
    }//setUsuFirmante
    
    public String getUsuEmail() {
        return usuEmail;
    }//getUsuEmail
    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }//setUsuEmail

    public String getUsuNif() {
        return usuNif;
    }//getUsuNif
    public void setUsuNif(String usuNif) {
        this.usuNif = usuNif;
    }//setUsuNif
    
    public Date getUsuFechaBaja() {
        return usuFechaBaja;
    }//getUsuFechaBaja
    public void setUsuFechaBaja(Date usuFechaBaja) {
        this.usuFechaBaja = usuFechaBaja;
    }//setUsuFechaBaja

}//class
