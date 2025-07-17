package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CampoSuplementarioModuloIntegracionVO {
    private String codOrganizacion;
    private String codProcedimiento;
    private String numExpediente;
    private String ejercicio;
    private String codTramite;
    private String ocurrenciaTramite;
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
    private boolean tramite;
    
    public String toString(){
        StringBuffer s = new StringBuffer();
        s.append("cod.organización: " + codOrganizacion + ",codProcedimiento: " + codProcedimiento + ",ejercicio: " +  ejercicio +  ",numExpediente: " + numExpediente);
        s.append("codTramite: " + codTramite + ",ocurrenciaTramite: " + ocurrenciaTramite);
        s.append("codigoCampo: " + codigoCampo + ", rotuloCampo: " + rotuloCampo + ", descripcionCampo: " + descripcionCampo);
        s.append("tipoCampo: " + tipoCampo);
        s.append("");
        if(tramite)
            s.append(" el campo es de trámite");
        else
            s.append(" el campo es de expediente");

        s.append("tipoCampo: " + tipoCampo);
        s.append("valorNumero: " + valorNumero);
        s.append("valorTexto: " + valorTexto);
        s.append("codigoDesplegable: " + codigoDesplegable);
        s.append("valorDesplegable: "  + valorDesplegable);
        s.append("descripcionValorDesplegable: "  + descripcionValorDesplegable);
        s.append("valorFichero: "  + getValorFichero());
        s.append("nombreFichero: " + getNombreFichero());
        s.append("tipoMimeFichero: " + getTipoMimeFichero());
        s.append("valorFecha: " + getValorFechaAsString());
        
        return s.toString();
    }

    /**
     * @return the codOrganizacion
     */
    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
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
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the codTramite
     */
    public String getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the ocurrenciaTramite
     */
    public String getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    /**
     * @param ocurrenciaTramite the ocurrenciaTramite to set
     */
    public void setOcurrenciaTramite(String ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    /**
     * @return the valorTexto
     */
    public String getValorTexto() {
        return valorTexto;
    }

    /**
     * @param valorTexto the valorTexto to set
     */
    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    /**
     * @return the valorFecha
     */
    public Calendar getValorFecha() {
        return valorFecha;
    }

    /**
     * @param valorFecha the valorFecha to set
     */
    public void setValorFecha(Calendar valorFecha) {
        this.valorFecha = valorFecha;
    }

    /**
     * @return the valorNumero
     */
    public String getValorNumero() {
        return valorNumero;
    }

    /**
     * @param valorNumero the valorNumero to set
     */
    public void setValorNumero(String valorNumero) {
        this.valorNumero = valorNumero;
    }

    /**
     * @return the codigoCampo
     */
    public String getCodigoCampo() {
        return codigoCampo;
    }

    /**
     * @param codigoCampo the codigoCampo to set
     */
    public void setCodigoCampo(String codigoCampo) {
        this.codigoCampo = codigoCampo;
    }

    /**
     * @return the rotuloCampo
     */
    public String getRotuloCampo() {
        return rotuloCampo;
    }

    /**
     * @param rotuloCampo the rotuloCampo to set
     */
    public void setRotuloCampo(String rotuloCampo) {
        this.rotuloCampo = rotuloCampo;
    }

    /**
     * @return the descripcionCampo
     */
    public String getDescripcionCampo() {
        return descripcionCampo;
    }

    /**
     * @param descripcionCampo the descripcionCampo to set
     */
    public void setDescripcionCampo(String descripcionCampo) {
        this.descripcionCampo = descripcionCampo;
    }

    /**
     * @return the tipoCampo
     */
    public int getTipoCampo() {
        return tipoCampo;
    }

    /**
     * @param tipoCampo the tipoCampo to set
     */
    public void setTipoCampo(int tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    /**
     * @return the tramite
     */
    public boolean isTramite() {
        return tramite;
    }

    /**
     * @param tramite the tramite to set
     */
    public void setTramite(boolean tramite) {
        this.tramite = tramite;
    }

    /**
     * @return the valorFichero
     */
    public byte[] getValorFichero() {
        return this.valorFichero;
    }


  /**
     * @return the valorFichero
     */
    public void setValorFichero(byte[] fichero) {
        this.valorFichero = fichero;
    }

   
    /**
     * @return the valorDesplegable
     */
    public String getValorDesplegable() {
        return valorDesplegable;
    }

    /**
     * @param valorDesplegable the valorDesplegable to set
     */
    public void setValorDesplegable(String valorDesplegable) {
        this.valorDesplegable = valorDesplegable;
    }

    /**
     * @return the codigoDesplegable
     */
    public String getCodigoDesplegable() {
        return codigoDesplegable;
    }

    /**
     * @param codigoDesplegable the codigoDesplegable to set
     */
    public void setCodigoDesplegable(String codigoDesplegable) {
        this.codigoDesplegable = codigoDesplegable;
    }

    /**
     * @return the tipoMimeFichero
     */
    public String getTipoMimeFichero() {
        return tipoMimeFichero;
    }

    /**
     * @param tipoMimeFichero the tipoMimeFichero to set
     */
    public void setTipoMimeFichero(String tipoMimeFichero) {
        this.tipoMimeFichero = tipoMimeFichero;
    }

    /**
     * @return the nombreFichero
     */
    public String getNombreFichero() {
        return nombreFichero;
    }

    /**
     * @param nombreFichero the nombreFichero to set
     */
    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public String getValorFechaAsString(){
        String fecha = null;
        if(this.valorFecha!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            fecha = sf.format(this.valorFecha.getTime());
        }
        
        return fecha;
    }

    /**
     * @return the ejercicio
     */
    public String getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the descripcionValorDesplegable
     */
    public String getDescripcionValorDesplegable() {
        return descripcionValorDesplegable;
    }

    /**
     * @param descripcionValorDesplegable the descripcionValorDesplegable to set
     */
    public void setDescripcionValorDesplegable(String descripcionValorDesplegable) {
        this.descripcionValorDesplegable = descripcionValorDesplegable;
    }
}