package es.altia.agora.business.sge.firma.vo;

public class FirmaCircuitoVO {

    private Integer idFlujoFirma;
    private String nombreFlujoFirma;
    private Integer idUsuario;
    private String logUsuario;
    private String dniUsuario;
    private String nombreUsuario;
    private String documentoUsuario;
    private Integer orden;

    public Integer getIdFlujoFirma() {
        return idFlujoFirma;
    }

    public void setIdFlujoFirma(Integer idFlujoFirma) {
        this.idFlujoFirma = idFlujoFirma;
    }

    public String getNombreFlujoFirma() {
        return nombreFlujoFirma;
    }

    public void setNombreFlujoFirma(String nombreFlujoFirma) {
        this.nombreFlujoFirma = nombreFlujoFirma;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getDocumentoUsuario() {
        return documentoUsuario;
    }

    public void setDocumentoUsuario(String documentoUsuario) {
        this.documentoUsuario = documentoUsuario;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
	
	/**
	 * @return the logUsuario
	 */
	public String getLogUsuario() {
		return logUsuario;
	}

	/**
	 * @param logUsuario the logUsuario to set
	 */
	public void setLogUsuario(String logUsuario) {
		this.logUsuario = logUsuario;
	}

        public String getDniUsuario() {
            return dniUsuario;
        }

        public void setDniUsuario(String dniUsuario) {
            this.dniUsuario = dniUsuario;
        }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.idFlujoFirma != null ? this.idFlujoFirma.hashCode() : 0);
        hash = 47 * hash + (this.nombreFlujoFirma != null ? this.nombreFlujoFirma.hashCode() : 0);
        hash = 47 * hash + (this.idUsuario != null ? this.idUsuario.hashCode() : 0);
        hash = 47 * hash + (this.nombreUsuario != null ? this.nombreUsuario.hashCode() : 0);
        hash = 47 * hash + (this.documentoUsuario != null ? this.documentoUsuario.hashCode() : 0);
        hash = 47 * hash + (this.orden != null ? this.orden.hashCode() : 0);
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
        final FirmaCircuitoVO other = (FirmaCircuitoVO) obj;
        if (this.idFlujoFirma != other.idFlujoFirma && (this.idFlujoFirma == null || !this.idFlujoFirma.equals(other.idFlujoFirma))) {
            return false;
        }
        if ((this.nombreFlujoFirma == null) ? (other.nombreFlujoFirma != null) : !this.nombreFlujoFirma.equals(other.nombreFlujoFirma)) {
            return false;
        }
        if (this.idUsuario != other.idUsuario && (this.idUsuario == null || !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        if ((this.nombreUsuario == null) ? (other.nombreUsuario != null) : !this.nombreUsuario.equals(other.nombreUsuario)) {
            return false;
        }
        if ((this.documentoUsuario == null) ? (other.documentoUsuario != null) : !this.documentoUsuario.equals(other.documentoUsuario)) {
            return false;
        }
        if (this.orden != other.orden && (this.orden == null || !this.orden.equals(other.orden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CircuitoFirmaVO{" + ", idFlujoFirma=" + idFlujoFirma + ", nombreFlujoFirma=" + nombreFlujoFirma + ", idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + ", logUsuario=" + logUsuario + ", documentoUsuario=" + documentoUsuario + ", orden=" + orden + '}';
    }
}
