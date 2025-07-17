package es.altia.agora.business.integracionsw;

import java.io.Serializable;

public class ParametroSWVO implements Serializable {

	private TipoServicioWebVO tipoParametro;
    private String nombreParametro;

    public ParametroSWVO(TipoServicioWebVO tipoParametro, String nombreParametro) {
        this.tipoParametro = tipoParametro;
        this.nombreParametro = nombreParametro;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public TipoServicioWebVO getTipoParametro() {
        return tipoParametro;
    }

    public void setTipoParametro(TipoServicioWebVO tipoParametro) {
        this.tipoParametro = tipoParametro;
    }
}
