package es.altia.flexia.registro.cargamasiva.lanbide.vo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SolicitudVO {
    // Campos con informacion correspondientes a la anotacion de registro
    private Integer idSolicitud;
    private Calendar fecAlta;
    private String codRegistro;
    // Campos con informacion correspondientes a los  documentos
    private List<String> nombresDocumentos;
    // Campos con informacion correspondientes al interesado
    private String nifInteresado;
    private String codProvincia;
    private String codPostal; // El codPostal recibido se corresponde con un codigo postal: dos dígitos de codProvincia + tres dígitos de codPostal
    private String codMunicipio;
    private String emailInteresado;
    private String nombreInteresado;
    private String ape1Interesado;
    private String ape2Interesado;
    private String telfInteresado;
    // Dirección de residencia
    private String tipoVia;
    private String nombreVia;
    private String numeroVia;
    private String bisDuplicado;
    private String escalera;
    private String piso;
    private String letra;

    public SolicitudVO() {
    }

    public SolicitudVO(Integer idSolicitud, Calendar fecAlta, String codRegistro) {
        this.idSolicitud = idSolicitud;
        this.fecAlta = fecAlta;
        this.codRegistro = codRegistro;
    }

    public void setDatosIntereComunes(String nombreInteresado, String ape1Interesado, String ape2Interesado, 
        String telfInteresado, String emailInteresado, String nifInteresado) {
        this.nombreInteresado = nombreInteresado;
        this.ape1Interesado = ape1Interesado;
        this.ape2Interesado = ape2Interesado;
        this.telfInteresado = telfInteresado;
        this.emailInteresado = emailInteresado;
        this.nifInteresado = nifInteresado;
    }


    /**
     * @return the idSolicitud
     */
    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    /**
     * @param idSolicitud the idSolicitud to set
     */
    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * @return the fecAlta
     */
    public Calendar getFecAlta() {
        return fecAlta;
    }

    /**
     * @param fecAlta the fecAlta to set
     */
    public void setFecAlta(Calendar fecAlta) {
        this.fecAlta = fecAlta;
    }

    /**
     * @return the codRegistro
     */
    public String getCodRegistro() {
        return codRegistro;
    }

    /**
     * @param codRegistro the codRegistro to set
     */
    public void setCodRegistro(String codRegistro) {
        this.codRegistro = codRegistro;
    }

    public void setNombresDocumentos(List<String>nombres) {

        if (nombres != null && nombres.size() > 0) {
           this.nombresDocumentos = new ArrayList<String>();
           this.nombresDocumentos.addAll(nombres);
        }
    }

    public List<String> getNombresDocumentos() {
        return this.nombresDocumentos;
    }

    /**
     * @return the nifInteresado
     */
    public String getNifInteresado() {
        return nifInteresado;
    }

    /**
     * @param nifInteresado the nifInteresado to set
     */
    public void setNifInteresado(String nifInteresado) {
        this.nifInteresado = nifInteresado;
    }

    /**
     * @return the codProvincia
     */
    public String getCodProvincia() {
        return codProvincia;
    }

    public String getCodProvinciaPorCodPostal() {
        return codPostal.substring(0, 1);
    }

    /**
     * @param codProvincia the codProvincia to set
     */
    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    /**
     * @return the codPostal
     */
    public String getCodPostal() {
        return codPostal;
    }

    /**
     * @param codPostal the codPostal to set
     */
    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    /**
     * @return the emailInteresado
     */
    public String getEmailInteresado() {
        return emailInteresado;
    }

    /**
     * @param emailInteresado the emailInteresado to set
     */
    public void setEmailInteresado(String emailInteresado) {
        this.emailInteresado = emailInteresado;
    }

    /**
     * @return the nombreInteresado
     */
    public String getNombreInteresado() {
        return nombreInteresado;
    }

    /**
     * @param nombreInteresado the nombreInteresado to set
     */
    public void setNombreInteresado(String nombreInteresado) {
        this.nombreInteresado = nombreInteresado;
    }

    /**
     * @return the ape1Interesado
     */
    public String getApe1Interesado() {
        return ape1Interesado;
    }

    /**
     * @param ape1Interesado the ape1Interesado to set
     */
    public void setApe1Interesado(String ape1Interesado) {
        this.ape1Interesado = ape1Interesado;
    }

    /**
     * @return the ape2Interesado
     */
    public String getApe2Interesado() {
        return ape2Interesado;
    }

    /**
     * @param ape2Interesado the ape2Interesado to set
     */
    public void setApe2Interesado(String ape2Interesado) {
        this.ape2Interesado = ape2Interesado;
    }

    /**
     * @return the telfInteresado
     */
    public String getTelfInteresado() {
        return telfInteresado;
    }

    /**
     * @param telfInteresado the telfInteresado to set
     */
    public void setTelfInteresado(String telfInteresado) {
        this.telfInteresado = telfInteresado;
    }

    /**
     * Metodo que setea el Codigo Posta, Provincia y Municipio
     * @param codProvincia
     * @param codPostal 
     */
    public void setDatosProvMunCP (String codProvincia, String codPostal) {
        this.codPostal = codPostal;
        this.codProvincia = codProvincia;
        this.setCodMunicipio();
    }

    /**
     * @return the codMunicipio
     */
    public String getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio() {
        this.codMunicipio = codPostal.substring(2);
    }

    //Dirección
    public String getTipoVia() {
        return tipoVia;
    }

    public void setTipoVia(String tipoVia) {
        this.tipoVia = tipoVia;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public String getNumeroVia() {
        return numeroVia;
    }

    public void setNumeroVia(String numeroVia) {
        this.numeroVia = numeroVia;
    }

    public String getBisDuplicado() {
        return bisDuplicado;
    }

    public void setBisDuplicado(String bisDuplicado) {
        this.bisDuplicado = bisDuplicado;
    }

    public String getEscalera() {
        return escalera;
    }

    public void setEscalera(String escalera) {
        this.escalera = escalera;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }
        
	
}
