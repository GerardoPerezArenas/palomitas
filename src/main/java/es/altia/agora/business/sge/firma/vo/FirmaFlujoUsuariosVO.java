package es.altia.agora.business.sge.firma.vo;

import java.util.List;

public class FirmaFlujoUsuariosVO {

    private FirmaFlujoVO flujo;
    private List<FirmaCircuitoVO> usuariosCircuito;

    public FirmaFlujoVO getFlujo() {
        return flujo;
    }

    public void setFlujo(FirmaFlujoVO flujo) {
        this.flujo = flujo;
    }

    public List<FirmaCircuitoVO> getUsuariosCircuito() {
        return usuariosCircuito;
    }

    public void setUsuariosCircuito(List<FirmaCircuitoVO> usuariosCircuito) {
        this.usuariosCircuito = usuariosCircuito;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.flujo != null ? this.flujo.hashCode() : 0);
        hash = 97 * hash + (this.usuariosCircuito != null ? this.usuariosCircuito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FirmaFlujoUsuariosVO other = (FirmaFlujoUsuariosVO) obj;
        if (this.flujo != other.flujo && (this.flujo == null || !this.flujo.equals(other.flujo))) {
            return false;
        }
        if (this.usuariosCircuito != other.usuariosCircuito && (this.usuariosCircuito == null || !this.usuariosCircuito.equals(other.usuariosCircuito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FirmaFlujoUsuariosVO{" + "flujo=" + flujo + ", usuariosCircuito=" + usuariosCircuito + '}';
    }

}
