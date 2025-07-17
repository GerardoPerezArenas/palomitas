package es.altia.agora.business.terceros;

import java.io.Serializable;

/**
* <p>Título: @gora</p>
* <p>Descripción: </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
* @author Manuel Vera Silvestre
* @version 1.0
*/

public class ParametrosTerceroValueObject implements Serializable {
    private String pais = "";
    private String nomPais = "";
    private String paisProvincia = "";
    private String provincia = "";
    private String nomProvincia = "";
    private String paisMunicipio = "";
    private String provinciaMunicipio = "";
    private String municipio = "";
    private String nomMunicipio = "";
    private String tipoDocumento = "";
    private String nomTipoDocumento = "";
    private String tipoOcupacion = "";
    private String nomTipoOcupacion = "";
    private String tipoOcupacionPrincipal = "";
    private String nomTipoOcupacionPrincipal = "";
    private String identificadorMultiple = "0";
    private String lugar = "";
    private String usuario = "";
    private String familiasPadron = "";

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        pais = (pais!=null)? pais:"";
        this.pais = pais;
    }

    public void setNomPais(String nomPais) {
        nomPais = (nomPais!=null)? nomPais:"";
        this.nomPais = nomPais;
    }

    public String getNomPais() {
        return nomPais;
    }

    public void setPaisProvincia(String paisProvincia) {
        paisProvincia = (paisProvincia!=null)? paisProvincia:"";
        this.paisProvincia = paisProvincia;
    }

    public String getPaisProvincia() {
        return paisProvincia;
    }

    public void setProvincia(String provincia) {
        provincia = (provincia!=null)? provincia:"";
        this.provincia = provincia;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setNomProvincia(String nomProvincia) {
        nomProvincia = (nomProvincia!=null)? nomProvincia:"";
        this.nomProvincia = nomProvincia;
    }

    public String getNomProvincia() {
        return nomProvincia;
    }

    public void setPaisMunicipio(String paisMunicipio) {
        paisMunicipio = (paisMunicipio!=null)? paisMunicipio:"";
        this.paisMunicipio = paisMunicipio;
    }

    public String getPaisMunicipio() {
        return paisMunicipio;
    }

    public void setProvinciaMunicipio(String provinciaMunicipio) {
        provinciaMunicipio = (provinciaMunicipio!=null)? provinciaMunicipio:"";
        this.provinciaMunicipio = provinciaMunicipio;
    }

    public String getProvinciaMunicipio() {
        return provinciaMunicipio;
    }

    public void setMunicipio(String municipio) {
        municipio = (municipio!=null)? municipio:"";
        this.municipio = municipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setNomMunicipio(String nomMunicipio) {
        nomMunicipio = (nomMunicipio!=null)? nomMunicipio:"";
        this.nomMunicipio = nomMunicipio;
    }

    public String getNomMunicipio() {
        return nomMunicipio;
    }

    public void setTipoDocumento(String tipoDocumento) {
        tipoDocumento = (tipoDocumento!=null)? tipoDocumento:"";
        this.tipoDocumento = tipoDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setNomTipoDocumento(String nomTipoDocumento) {
        nomTipoDocumento = (nomTipoDocumento!=null)? nomTipoDocumento:"";
        this.nomTipoDocumento = nomTipoDocumento;
    }

    public String getNomTipoDocumento() {
        return nomTipoDocumento;
    }

    public void setTipoOcupacion(String tipoOcupacion) {
        tipoOcupacion = (tipoOcupacion!=null)? tipoOcupacion:"";
        this.tipoOcupacion = tipoOcupacion;
    }

    public String getTipoOcupacion() {
        return tipoOcupacion;
    }

    public void setTipoOcupacionPrincipal(String tipoOcupacionPrincipal) {
        tipoOcupacionPrincipal = (tipoOcupacionPrincipal!=null)? tipoOcupacionPrincipal:"";
        this.tipoOcupacionPrincipal = tipoOcupacionPrincipal;
    }

    public String getTipoOcupacionPrincipal() {
        return tipoOcupacionPrincipal;
    }

    public void setNomTipoOcupacion(String nomTipoOcupacion) {
        nomTipoOcupacion = (nomTipoOcupacion!=null)? nomTipoOcupacion:"";
        this.nomTipoOcupacion = nomTipoOcupacion;
    }

    public String getNomTipoOcupacion() {
        return nomTipoOcupacion;
    }

    public void setNomTipoOcupacionPrincipal(String nomTipoOcupacionPrincipal) {
        nomTipoOcupacionPrincipal = (nomTipoOcupacionPrincipal!=null)? nomTipoOcupacionPrincipal:"";
        this.nomTipoOcupacionPrincipal = nomTipoOcupacionPrincipal;
    }

    public String getNomTipoOcupacionPrincipal() {
        return nomTipoOcupacionPrincipal;
    }

    public void setIdentificadorMultiple(String identificadorMultiple) {
        this.identificadorMultiple = identificadorMultiple;
    }

    public String getIdentificadorMultiple() {
        return identificadorMultiple;
    }

    public void setLugar(String lugar) {
        lugar = (lugar!=null)?lugar:"";
        this.lugar = lugar;
    }

    public String getLugar() {
        return lugar;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setFamiliasPadron(String familiasPadron) {
        this.familiasPadron = familiasPadron;
    }

    public String getFamiliasPadron() {
        return (this.familiasPadron);
    }



}