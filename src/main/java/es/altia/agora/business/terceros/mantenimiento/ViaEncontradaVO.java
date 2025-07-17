package es.altia.agora.business.terceros.mantenimiento;

import java.io.Serializable;

public class ViaEncontradaVO implements Serializable {

    private String origen = "SGE";
    private int codigoVia;
    private String nombreVia;
    private String nombreCortoVia;
    private int codigoTipoVia;
    private String descTipoVia;
    private int codigoPais;
    private String descPais;
    private int codigoProvincia;
    private String descProvincia;
    private int codigoMunicipio;
    private String descMunicipio;
    private InfoTrameroViaVO infoTramero;

    public ViaEncontradaVO() {
    }

    public ViaEncontradaVO(String origen, int codigoVia, String nombreVia, String nombreCortoVia, int codigoTipoVia,
                           String descTipoVia, int codigoPais, String descPais, int codigoProvincia,
                           String descProvincia, int codigoMunicipio, String descMunicipio,
                           InfoTrameroViaVO infoTramero) {

        this.origen = origen;
        this.codigoVia = codigoVia;
        this.nombreVia = nombreVia;
        this.nombreCortoVia = nombreCortoVia;
        this.codigoTipoVia = codigoTipoVia;
        this.descTipoVia = descTipoVia;
        this.codigoPais = codigoPais;
        this.descPais = descPais;
        this.codigoProvincia = codigoProvincia;
        this.descProvincia = descProvincia;
        this.codigoMunicipio = codigoMunicipio;
        this.descMunicipio = descMunicipio;
    }

    public InfoTrameroViaVO getInfoTramero() {
        return infoTramero;
    }

    public void setInfoTramero(InfoTrameroViaVO infoTramero) {
        this.infoTramero = infoTramero;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public int getCodigoVia() {
        return codigoVia;
    }

    public void setCodigoVia(int codigoVia) {
        this.codigoVia = codigoVia;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public String getNombreCortoVia() {
        return nombreCortoVia;
    }

    public void setNombreCortoVia(String nombreCortoVia) {
        this.nombreCortoVia = nombreCortoVia;
    }

    public int getCodigoTipoVia() {
        return codigoTipoVia;
    }

    public void setCodigoTipoVia(int codigoTipoVia) {
        this.codigoTipoVia = codigoTipoVia;
    }

    public String getDescTipoVia() {
        return descTipoVia;
    }

    public void setDescTipoVia(String descTipoVia) {
        this.descTipoVia = descTipoVia;
    }

    public int getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(int codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getDescPais() {
        return descPais;
    }

    public void setDescPais(String descPais) {
        this.descPais = descPais;
    }

    public int getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(int codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public String getDescProvincia() {
        return descProvincia;
    }

    public void setDescProvincia(String descProvincia) {
        this.descProvincia = descProvincia;
    }

    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(int codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getDescMunicipio() {
        return descMunicipio;
    }

    public void setDescMunicipio(String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }
}
