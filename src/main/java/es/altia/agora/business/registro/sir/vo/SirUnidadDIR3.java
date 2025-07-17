/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BD: SIR_UNIDAD_DIR3
 * @author INGDGC
 */
public class SirUnidadDIR3 {
    
    private static final SimpleDateFormat formatDateddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    
    private String codigoUnidad;    // 	VARCHAR2 ( 100 BYTE) PRIMARY KEY
    private String nombreUnidad_ES;    // 	 VARCHAR2(1500 BYTE)
    private String nombreUnidad_EU;    // 	 VARCHAR2(1500 BYTE)
    private String codigoOficina;    // 		VARCHAR2 ( 100 BYTE)
    private String codigoOrganismo;    // 	 VARCHAR2 ( 100 BYTE)
    private String codigoRaiz;    // 	 VARCHAR2( 100 BYTE)
    private String codigoNivelAdministrativo;    // 	 VARCHAR2 ( 100 BYTE)
    private String codigoComunidadAutonoma;    // 	 VARCHAR2 ( 100 BYTE)
    private String codigoProvincia;    // 	 VARCHAR2 ( 100 BYTE)
    private String codVisibleUorFlexia;    // 	  varchar2(15 BYTE)
    private Date fechaActivacion;    // 	 date
    
    public SirUnidadDIR3(String codigoUnidad, String nombreUnidad_ES, String nombreUnidad_EU, String codigoOficina, String codigoOrganismo, String codigoRaiz, String codigoNivelAdministrativo, String codigoComunidadAutonoma, String codigoProvincia,String codVisibleUorFlexia, Date fechaActivacion) {
        this.codigoUnidad = codigoUnidad;
        this.nombreUnidad_ES = nombreUnidad_ES;
        this.nombreUnidad_EU = nombreUnidad_EU;
        this.codigoOficina = codigoOficina;
        this.codigoOrganismo = codigoOrganismo;
        this.codigoRaiz = codigoRaiz;
        this.codigoNivelAdministrativo = codigoNivelAdministrativo;
        this.codigoComunidadAutonoma = codigoComunidadAutonoma;
        this.codigoProvincia = codigoProvincia;
        this.codVisibleUorFlexia = codVisibleUorFlexia;
        this.fechaActivacion = fechaActivacion;
    }

    public SirUnidadDIR3() {
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getNombreUnidad_ES() {
        return nombreUnidad_ES;
    }

    public void setNombreUnidad_ES(String nombreUnidad_ES) {
        this.nombreUnidad_ES = nombreUnidad_ES;
    }

    public String getNombreUnidad_EU() {
        return nombreUnidad_EU;
    }

    public void setNombreUnidad_EU(String nombreUnidad_EU) {
        this.nombreUnidad_EU = nombreUnidad_EU;
    }

    public String getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(String codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public String getCodigoOrganismo() {
        return codigoOrganismo;
    }

    public void setCodigoOrganismo(String codigoOrganismo) {
        this.codigoOrganismo = codigoOrganismo;
    }

    public String getCodigoRaiz() {
        return codigoRaiz;
    }

    public void setCodigoRaiz(String codigoRaiz) {
        this.codigoRaiz = codigoRaiz;
    }

    public String getCodigoNivelAdministrativo() {
        return codigoNivelAdministrativo;
    }

    public void setCodigoNivelAdministrativo(String codigoNivelAdministrativo) {
        this.codigoNivelAdministrativo = codigoNivelAdministrativo;
    }

    public String getCodigoComunidadAutonoma() {
        return codigoComunidadAutonoma;
    }

    public void setCodigoComunidadAutonoma(String codigoComunidadAutonoma) {
        this.codigoComunidadAutonoma = codigoComunidadAutonoma;
    }

    public String getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(String codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public String getCodVisibleUorFlexia() {
        return codVisibleUorFlexia;
    }

    public void setCodVisibleUorFlexia(String codVisibleUorFlexia) {
        this.codVisibleUorFlexia = codVisibleUorFlexia;
    }

    public Date getFechaActivacion() {
        return fechaActivacion;
    }
    
    public String getFechaActivacionAsString() {
        return (fechaActivacion!=null ? formatDateddMMyyyy.format(fechaActivacion) : null);
    }

    public void setFechaActivacion(Date fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    @Override
    public String toString() {
        return "SirUnidadDIR3{" + "codigoUnidad=" + codigoUnidad + ", nombreUnidad_ES=" + nombreUnidad_ES + ", nombreUnidad_EU=" + nombreUnidad_EU + ", codigoOficina=" + codigoOficina + ", codigoOrganismo=" + codigoOrganismo + ", codigoRaiz=" + codigoRaiz + ", codigoNivelAdministrativo=" + codigoNivelAdministrativo + ", codigoComunidadAutonoma=" + codigoComunidadAutonoma + ", codigoProvincia=" + codigoProvincia + ", codVisibleUorFlexia=" + codVisibleUorFlexia + ", fechaActivacion=" + getFechaActivacionAsString() + '}';
    }
    
}
