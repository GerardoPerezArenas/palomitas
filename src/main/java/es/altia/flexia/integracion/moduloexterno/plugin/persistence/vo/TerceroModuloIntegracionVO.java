package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.util.ArrayList;


public class TerceroModuloIntegracionVO {
 
    private String codTercero; //código del tercero
    private String versionTercero; // versión del tercero
    private String documentoTercero; //documento del tercero
    private String tipoDocumentoTercero; //TIPO DE DOCUMENTO DEL TERCERO
    private String nombreTercero; //nombre del tercero
    private String apellido1Tercero; //primer apellido del tercero
    private String apellido2Tercero; //segundo apellido del tercero
    private String nombreCompleto; //representa el nombre completo
    private String telefonoTercero; //telefono del tercero
    private String email; //email del tercero
    private String fechaAlta; //fecha de alta del tercero
    private String fechaBaja; //fecha de baja del tercero
    private String usuarioBaja; //representa el código de usuario que ha dado de baja al tercero
    private String externalCode; //Código externo del tercero
    private String normalizado="";
    private String usuarioAlta;
    private String moduloAlta;
    private String domPrincipal=""; //Domicilio principal del tercero
    private char situacion;
    private ArrayList<DomicilioInteresadoModuloIntegracionVO> domicilios; //lista de domicilios del tercero
   

    public String getTipoDocumentoTercero() {
        return tipoDocumentoTercero;
    }

    public void setTipoDocumentoTercero(String tipoDocumentoTercero) {
        this.tipoDocumentoTercero = tipoDocumentoTercero;
    }
   
 
    public String getDocumentoTercero() {
        return documentoTercero;
    }

    public void setDocumentoTercero(String documentoTercero) {
        this.documentoTercero = documentoTercero;
    }

    public String getNormalizado() {
        return normalizado;
    }

    public void setNormalizado(String normalizado) {
        this.normalizado = normalizado;
    }

    

    public String getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(String usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public String getModuloAlta() {
        return moduloAlta;
    }

    public void setModuloAlta(String moduloAlta) {
        this.moduloAlta = moduloAlta;
    }

    public String getDomPrincipal() {
        return domPrincipal;
    }

    public void setDomPrincipal(String domPrincipal) {
        this.domPrincipal = domPrincipal;
    }
  
    
    
    public TerceroModuloIntegracionVO(){}
    public TerceroModuloIntegracionVO(String ident, String ver, String tipoDoc,
                             String doc, String nom,String ap1,  String ap2,
                             String nor, String telf, String email, char sit, String fAlta,
                             String usuAlta, String mod, String fBaja, String usuBaja, int domPrincipal){
    this.codTercero = ident;
    this.versionTercero = ver;
  
    this.tipoDocumentoTercero=tipoDoc;
    this.documentoTercero = (doc==null)?"":doc;
    this.nombreTercero = (nom==null)?"":nom;
    this.apellido1Tercero = (ap1==null?"":ap1);
   
    this.apellido2Tercero = (ap2==null)?"":ap2;
  
    this.normalizado = nor;
    this.telefonoTercero = (telf==null)?"":telf;
    this.email = (email==null)?"":email;
    this.situacion = sit;
    this.fechaAlta = fAlta;
    this.usuarioAlta = usuAlta;
    this.moduloAlta = mod;
    this.fechaBaja = fBaja;
    this.usuarioBaja = usuBaja;
    this.domPrincipal=String.valueOf(domPrincipal);
  }

    public char getSituacion() {
        return situacion;
    }

    public void setSituacion(char situacion) {
        this.situacion = situacion;
    }
    
    
    
    public String getCodTercero() {
        return codTercero;
    }

    public void setCodTercero(String codTercero) {
        this.codTercero = codTercero;
    }

    public String getVersionTercero() {
        return versionTercero;
    }

    public void setVersionTercero(String versionTercero) {
        this.versionTercero = versionTercero;
    }

    public String getNombreTercero() {
        return nombreTercero;
    }

    public void setNombreTercero(String nombreTercero) {
        this.nombreTercero = nombreTercero;
    }

    public String getApellido1Tercero() {
        return apellido1Tercero;
    }

    public void setApellido1Tercero(String apellido1Tercero) {
        this.apellido1Tercero = apellido1Tercero;
    }

    public String getApellido2Tercero() {
        return apellido2Tercero;
    }

    public void setApellido2Tercero(String apellido2Tercero) {
        this.apellido2Tercero = apellido2Tercero;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefonoTercero() {
        return telefonoTercero;
    }

    public void setTelefonoTercero(String telefonoTercero) {
        this.telefonoTercero = telefonoTercero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getUsuarioBaja() {
        return usuarioBaja;
    }

    public void setUsuarioBaja(String usuarioBaja) {
        this.usuarioBaja = usuarioBaja;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public ArrayList<DomicilioInteresadoModuloIntegracionVO> getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(ArrayList<DomicilioInteresadoModuloIntegracionVO> domicilios) {
        this.domicilios = domicilios;
    }

    public void setUsuarioAlta(int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

   
  
    
}//class