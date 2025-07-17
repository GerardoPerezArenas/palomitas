package es.altia.agora.business.integracionsw;

import java.io.Serializable;

public class CampoTipoCompuestoVO implements Serializable, Comparable {

    private String idCampo;
    private TipoServicioWebVO tipoCampo;

    public CampoTipoCompuestoVO() {
    }

    public CampoTipoCompuestoVO(String idCampo, TipoServicioWebVO tipoCampo) {
        this.idCampo = idCampo;
        this.tipoCampo = tipoCampo;
    }

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public TipoServicioWebVO getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(TipoServicioWebVO tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public int compareTo(Object other) {
        CampoTipoCompuestoVO otherCampo = (CampoTipoCompuestoVO)other;
        return idCampo.compareTo(otherCampo.getIdCampo());
    }

    public String toString() {
        return this.idCampo + " --> " + this.tipoCampo;
    }
}
