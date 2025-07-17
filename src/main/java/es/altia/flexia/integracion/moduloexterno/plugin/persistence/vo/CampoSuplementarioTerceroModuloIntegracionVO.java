package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author david.caamano
 * @version 05/12/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 05/12/2012 * Edición inicial</li>
 * </ol> 
 */
public class CampoSuplementarioTerceroModuloIntegracionVO {
    
    private String codOrganizacion;
    private Integer codTercero;
    private String valorTexto;
    private Calendar valorFecha;
    private String valorNumero;
    private String nombreFichero;
    private String tipoMimeFichero;    
    private String codigoDesplegable;
    private String valorDesplegable;
    private String descripcionValorDesplegable;
    private byte[] valorFichero;
    private String codigoCampo;
    private String rotuloCampo;
    private String descripcionCampo;
    private int tipoCampo = -999;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }//getCodOrganizacion
    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }//setCodOrganizacion

    public Integer getCodTercero() {
        return codTercero;
    }//getCodTercero
    public void setCodTercero(Integer codTercero) {
        this.codTercero = codTercero;
    }//setCodTercero

    public String getCodigoCampo() {
        return codigoCampo;
    }//getCodigoCampo
    public void setCodigoCampo(String codigoCampo) {
        this.codigoCampo = codigoCampo;
    }//setCodigoCampo

    public String getCodigoDesplegable() {
        return codigoDesplegable;
    }//getCodigoDesplegable
    public void setCodigoDesplegable(String codigoDesplegable) {
        this.codigoDesplegable = codigoDesplegable;
    }//setCodigoDesplegable

    public String getDescripcionCampo() {
        return descripcionCampo;
    }//getDescripcionCampo
    public void setDescripcionCampo(String descripcionCampo) {
        this.descripcionCampo = descripcionCampo;
    }//setDescripcionCampo

    public String getDescripcionValorDesplegable() {
        return descripcionValorDesplegable;
    }//getDescripcionValorDesplegable
    public void setDescripcionValorDesplegable(String descripcionValorDesplegable) {
        this.descripcionValorDesplegable = descripcionValorDesplegable;
    }//setDescripcionValorDesplegable

    public String getNombreFichero() {
        return nombreFichero;
    }//getNombreFichero
    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }//setNombreFichero

    public String getRotuloCampo() {
        return rotuloCampo;
    }//getRotuloCampo
    public void setRotuloCampo(String rotuloCampo) {
        this.rotuloCampo = rotuloCampo;
    }//setRotuloCampo

    public int getTipoCampo() {
        return tipoCampo;
    }//getTipoCampo
    public void setTipoCampo(int tipoCampo) {
        this.tipoCampo = tipoCampo;
    }//setTipoCampo

    public String getTipoMimeFichero() {
        return tipoMimeFichero;
    }//getTipoMimeFichero
    public void setTipoMimeFichero(String tipoMimeFichero) {
        this.tipoMimeFichero = tipoMimeFichero;
    }//setTipoMimeFichero

    public String getValorDesplegable() {
        return valorDesplegable;
    }//getValorDesplegable
    public void setValorDesplegable(String valorDesplegable) {
        this.valorDesplegable = valorDesplegable;
    }//setValorDesplegable

    public Calendar getValorFecha() {
        return valorFecha;
    }//getValorFecha
    public void setValorFecha(Calendar valorFecha) {
        this.valorFecha = valorFecha;
    }//setValorFecha
    public String getValorFechaAsString(){
        String fecha = null;
        if(this.valorFecha!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            fecha = sf.format(this.valorFecha.getTime());
        }
        
        return fecha;
    }//getValorFechaAsString

    public byte[] getValorFichero() {
        return valorFichero;
    }//getValorFichero
    public void setValorFichero(byte[] valorFichero) {
        this.valorFichero = valorFichero;
    }//setValorFichero

    public String getValorNumero() {
        return valorNumero;
    }//getValorNumero
    public void setValorNumero(String valorNumero) {
        this.valorNumero = valorNumero;
    }//setValorNumero

    public String getValorTexto() {
        return valorTexto;
    }//getValorTexto
    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }//setValorTexto

}//class
