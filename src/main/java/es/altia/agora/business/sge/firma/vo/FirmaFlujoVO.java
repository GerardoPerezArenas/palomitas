package es.altia.agora.business.sge.firma.vo;

import java.util.List;

public class FirmaFlujoVO {

    private Integer id;
    private String nombre;
    private Integer idTipoFirma;
    private String tipoFirma;
    private boolean activo;
    private List<FirmaCircuitoVO> listaFirmasCircuito;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdTipoFirma() {
        return idTipoFirma;
    }

    public void setIdTipoFirma(Integer idTipoFirma) {
        this.idTipoFirma = idTipoFirma;
    }

    public String getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(String tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 59 * hash + (this.idTipoFirma != null ? this.idTipoFirma.hashCode() : 0);
        hash = 59 * hash + (this.tipoFirma != null ? this.tipoFirma.hashCode() : 0);
        hash = 59 * hash + (this.activo ? 1 : 0);
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
        final FirmaFlujoVO other = (FirmaFlujoVO) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        if (this.idTipoFirma != other.idTipoFirma && (this.idTipoFirma == null || !this.idTipoFirma.equals(other.idTipoFirma))) {
            return false;
        }
        if ((this.tipoFirma == null) ? (other.tipoFirma != null) : !this.tipoFirma.equals(other.tipoFirma)) {
            return false;
        }
        if (this.activo != other.activo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FlujoFirmaVO{" + "id=" + id + ", nombre=" + nombre + ", idTipoFirma=" + idTipoFirma + ", tipoFirma=" + tipoFirma + ", activo=" + activo + '}';
    }
    
    	/**
	 * @return the listaFirmasCircuito
	 */
	public List<FirmaCircuitoVO> getListaFirmasCircuito() {
		return listaFirmasCircuito;
	}

	/**
	 * @param listaFirmasCircuito the listaFirmasCircuito to set
	 */
	public void setListaFirmasCircuito(List<FirmaCircuitoVO> listaFirmasCircuito) {
		this.listaFirmasCircuito = listaFirmasCircuito;
	}


}
