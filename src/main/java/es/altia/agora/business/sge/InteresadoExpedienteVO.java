package es.altia.agora.business.sge;

import java.io.Serializable;

public class InteresadoExpedienteVO implements Serializable {
    
    private int codTercero;
    private int numVersion;
    private String nombreCompleto;
    private int codigoRol;
    private String descRol;
    private boolean porDefecto;
    private boolean mostrar;
    private int codDomicilio;
    private String domicilio;

    private String telf;
    private String email;
    private String tipoDoc;
    private String txtDoc;
    private String cp;
    private String pais;
    private String provincia;
    private String municipio;
    private String admiteNotificacion;

    public InteresadoExpedienteVO() {
    }

    public InteresadoExpedienteVO(int codTercero, int numVersion, String nombreCompleto, 
            int codigoRol, String descRol, boolean porDefecto, boolean mostrar, 
            int codDomicilio, String domicilio, String admiteNotificacion ) {
        
        this.codTercero = codTercero;
        this.numVersion = numVersion;
        this.nombreCompleto = nombreCompleto;
        this.codigoRol = codigoRol;
        this.descRol = descRol;
        this.porDefecto = porDefecto;
        this.mostrar = mostrar;
        this.codDomicilio = codDomicilio;
        this.domicilio = domicilio;
        this.admiteNotificacion = admiteNotificacion;
    }

    public int getCodTercero() {
        return codTercero;
    }

    public void setCodTercero(int codTercero) {
        this.codTercero = codTercero;
    }

    public int getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(int codigoRol) {
        this.codigoRol = codigoRol;
    }

    public String getDescRol() {
        return descRol;
    }

    public void setDescRol(String descRol) {
        this.descRol = descRol;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getNumVersion() {
        return numVersion;
    }

    public void setNumVersion(int numVersion) {
        this.numVersion = numVersion;
    }

    public boolean isPorDefecto() {
        return porDefecto;
    }

    public void setPorDefecto(boolean porDefecto) {
        this.porDefecto = porDefecto;
    }

    public int getCodDomicilio() {
        return codDomicilio;
    }

    public void setCodDomicilio(int codDomicilio) {
        this.codDomicilio = codDomicilio;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getTxtDoc() {
        return txtDoc;
    }

    public void setTxtDoc(String txtDoc) {
        this.txtDoc = txtDoc;
    }

    /**
     * @return the admiteNotificacion
     */
    public String getAdmiteNotificacion() {
        return admiteNotificacion;
    }

    /**
     * @param admiteNotificacion the admiteNotificacion to set
     */
    public void setAdmiteNotificacion(String admiteNotificacion) {
        this.admiteNotificacion = admiteNotificacion;
    }

    

}
