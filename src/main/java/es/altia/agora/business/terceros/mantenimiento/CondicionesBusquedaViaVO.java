package es.altia.agora.business.terceros.mantenimiento;

import java.io.Serializable;

public class CondicionesBusquedaViaVO implements Serializable {

    private int codOrganizacion;
    private int codPais;
    private int codProvincia;
    private int codMunicipio;
    private String nombreVia;
    private int codigoECO;
    private int codigoESI;

    public CondicionesBusquedaViaVO() {
        codOrganizacion = -1;
        codPais = -1;
        codProvincia = -1;
        codMunicipio = -1;
        nombreVia = null;
        codigoECO = -1;
        codigoESI = -1;
    }

    public CondicionesBusquedaViaVO(int codPais, int codProvincia, int codMunicipio, String nombreVia,
                                    int codOrganizacion, int codigoECO, int codigoESI) {
        this.codPais = codPais;
        this.codProvincia = codProvincia;
        this.codMunicipio = codMunicipio;
        this.nombreVia = nombreVia;
        this.codOrganizacion = codOrganizacion;
        this.codigoECO = codigoECO;
        this.codigoESI = codigoESI;
    }

    public int getCodPais() {
        return codPais;
    }

    public void setCodPais(int codPais) {
        this.codPais = codPais;
    }

    public int getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(int codProvincia) {
        this.codProvincia = codProvincia;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getCodigoECO() {
        return codigoECO;
    }

    public void setCodigoECO(int codigoECO) {
        this.codigoECO = codigoECO;
    }

    public int getCodigoESI() {
        return codigoESI;
    }

    public void setCodigoESI(int codigoESI) {
        this.codigoESI = codigoESI;
    }
}
