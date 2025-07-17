package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

public class DomicilioInteresadoModuloIntegracionVO {
    private String idDomicilio;
    private String idPais;
    private String idProvincia;
    private String idMunicipio;

    private String pais;
    private String provincia;
    private String municipio;
    private String codigoPostal;

    private String codigoVia;
    private String descripcionVia;
    private String idPaisVia;
    private String idProvinciaVia;
    private String idMunicipioVia;
    private int tipoVia;

    private String domicilio;
    private String numDesde;
    private String numHasta;
    private String letraDesde;
    private String letraHasta;
    private String bloque;
    private String portal;
    private String escalera;
    private String planta;
    private String puerta;
    private String barriada;

    private String codTipoUso;
    private String descTipoUso;
    private String normalizado;
    private String codECO;
    private String codESI;
    private String descESI;
    
    private boolean porDefecto;
    
    public boolean isPorDefecto(){
        return porDefecto;
    }
        
    public void setPorDefecto(boolean defecto){
        this.porDefecto = defecto;
    }
    
    /**
     * @return the idDomicilio
     */
    public String getIdDomicilio() {
        return idDomicilio;
    }

    /**
     * @param idDomicilio the idDomicilio to set
     */
    public void setIdDomicilio(String idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    /**
     * @return the idPais
     */
    public String getIdPais() {
        return idPais;
    }

    /**
     * @param idPais the idPais to set
     */
    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    /**
     * @return the idProvincia
     */
    public String getIdProvincia() {
        return idProvincia;
    }

    public int getTipoVia() {
        return tipoVia;
    }

    public void setTipoVia(int tipoVia) {
        this.tipoVia = tipoVia;
    }

    /**
     * @param idProvincia the idProvincia to set
     */
    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }

    /**
     * @return the idMunicipio
     */
    public String getIdMunicipio() {
        return idMunicipio;
    }

    /**
     * @param idMunicipio the idMunicipio to set
     */
    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    /**
     * @return the pais
     */
    public String getPais() {
        return pais;
    }

    /**
     * @param pais the pais to set
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * @return the provincia
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * @param provincia the provincia to set
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * @return the municipio
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * @param municipio the municipio to set
     */
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    /**
     * @return the codigoPostal
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * @param codigoPostal the codigoPostal to set
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * @return the codigoVia
     */
    public String getCodigoVia() {
        return codigoVia;
    }

    /**
     * @param codigoVia the codigoVia to set
     */
    public void setCodigoVia(String codigoVia) {
        this.codigoVia = codigoVia;
    }

    /**
     * @return the descripcionVia
     */
    public String getDescripcionVia() {
        return descripcionVia;
    }

    /**
     * @param descripcionVia the descripcionVia to set
     */
    public void setDescripcionVia(String descripcionVia) {
        this.descripcionVia = descripcionVia;
    }

    /**
     * @return the idPaisVia
     */
    public String getIdPaisVia() {
        return idPaisVia;
    }

    /**
     * @param idPaisVia the idPaisVia to set
     */
    public void setIdPaisVia(String idPaisVia) {
        this.idPaisVia = idPaisVia;
    }

    /**
     * @return the idProvinciaVia
     */
    public String getIdProvinciaVia() {
        return idProvinciaVia;
    }

    /**
     * @param idProvinciaVia the idProvinciaVia to set
     */
    public void setIdProvinciaVia(String idProvinciaVia) {
        this.idProvinciaVia = idProvinciaVia;
    }

    /**
     * @return the idMunicipioVia
     */
    public String getIdMunicipioVia() {
        return idMunicipioVia;
    }

    /**
     * @param idMunicipioVia the idMunicipioVia to set
     */
    public void setIdMunicipioVia(String idMunicipioVia) {
        this.idMunicipioVia = idMunicipioVia;
    }

    
    /**
     * @return the domicilio
     */
    public String getDomicilio() {
        return domicilio;
    }

    /**
     * @param domicilio the domicilio to set
     */
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    /**
     * @return the numDesde
     */
    public String getNumDesde() {
        return numDesde;
    }

    /**
     * @param numDesde the numDesde to set
     */
    public void setNumDesde(String numDesde) {
        this.numDesde = numDesde;
    }

    /**
     * @return the numHasta
     */
    public String getNumHasta() {
        return numHasta;
    }

    /**
     * @param numHasta the numHasta to set
     */
    public void setNumHasta(String numHasta) {
        this.numHasta = numHasta;
    }

    /**
     * @return the letraDesde
     */
    public String getLetraDesde() {
        return letraDesde;
    }

    /**
     * @param letraDesde the letraDesde to set
     */
    public void setLetraDesde(String letraDesde) {
        this.letraDesde = letraDesde;
    }

    /**
     * @return the letraHasta
     */
    public String getLetraHasta() {
        return letraHasta;
    }

    /**
     * @param letraHasta the letraHasta to set
     */
    public void setLetraHasta(String letraHasta) {
        this.letraHasta = letraHasta;
    }

    /**
     * @return the bloque
     */
    public String getBloque() {
        return bloque;
    }

    /**
     * @param bloque the bloque to set
     */
    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    /**
     * @return the portal
     */
    public String getPortal() {
        return portal;
    }

    /**
     * @param portal the portal to set
     */
    public void setPortal(String portal) {
        this.portal = portal;
    }

    /**
     * @return the escalera
     */
    public String getEscalera() {
        return escalera;
    }

    /**
     * @param escalera the escalera to set
     */
    public void setEscalera(String escalera) {
        this.escalera = escalera;
    }

    /**
     * @return the planta
     */
    public String getPlanta() {
        return planta;
    }

    /**
     * @param planta the planta to set
     */
    public void setPlanta(String planta) {
        this.planta = planta;
    }

    /**
     * @return the puerta
     */
    public String getPuerta() {
        return puerta;
    }

    /**
     * @param puerta the puerta to set
     */
    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    /**
     * @return the barriada
     */
    public String getBarriada() {
        return barriada;
    }

    /**
     * @param barriada the barriada to set
     */
    public void setBarriada(String barriada) {
        this.barriada = barriada;
    }

    /**
     * @return the codTipoUso
     */
    public String getCodTipoUso() {
        return codTipoUso;
    }

    /**
     * @param codTipoUso the codTipoUso to set
     */
    public void setCodTipoUso(String codTipoUso) {
        this.codTipoUso = codTipoUso;
    }

    /**
     * @return the descTipoUso
     */
    public String getDescTipoUso() {
        return descTipoUso;
    }

    /**
     * @param descTipoUso the descTipoUso to set
     */
    public void setDescTipoUso(String descTipoUso) {
        this.descTipoUso = descTipoUso;
    }

    /**
     * @return the normalizado
     */
    public String getNormalizado() {
        return normalizado;
    }

    /**
     * @param normalizado the normalizado to set
     */
    public void setNormalizado(String normalizado) {
        this.normalizado = normalizado;
    }

    /**
     * @return the codECO
     */
    public String getCodECO() {
        return codECO;
    }

    /**
     * @param codECO the codECO to set
     */
    public void setCodECO(String codECO) {
        this.codECO = codECO;
    }

    /**
     * @return the codESI
     */
    public String getCodESI() {
        return codESI;
    }

    /**
     * @param codESI the codESI to set
     */
    public void setCodESI(String codESI) {
        this.codESI = codESI;
    }

    /**
     * @return the descESI
     */
    public String getDescESI() {
        return descESI;
    }

    /**
     * @param descESI the descESI to set
     */
    public void setDescESI(String descESI) {
        this.descESI = descESI;
    }

}