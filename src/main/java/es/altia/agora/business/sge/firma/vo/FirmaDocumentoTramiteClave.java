package es.altia.agora.business.sge.firma.vo;

public class FirmaDocumentoTramiteClave {

    Integer codMunicipio;
    String codProcedimiento;
    Integer ejercicio;
    String numExpediente;
    Integer codTramite;
    Integer codOcurrencia;
    Integer codDocumento;

    public FirmaDocumentoTramiteClave() {
    }

    public FirmaDocumentoTramiteClave(Integer codMunicipio, String codProcedimiento, Integer ejercicio, String numExpediente, Integer codTramite, Integer codOcurrencia, Integer codDocumento) {
        this.codMunicipio = codMunicipio;
        this.codProcedimiento = codProcedimiento;
        this.ejercicio = ejercicio;
        this.numExpediente = numExpediente;
        this.codTramite = codTramite;
        this.codOcurrencia = codOcurrencia;
        this.codDocumento = codDocumento;
    }

    public Integer getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(Integer codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(Integer codTramite) {
        this.codTramite = codTramite;
    }

    public Integer getCodOcurrencia() {
        return codOcurrencia;
    }

    public void setCodOcurrencia(Integer codOcurrencia) {
        this.codOcurrencia = codOcurrencia;
    }

    public Integer getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(Integer codDocumento) {
        this.codDocumento = codDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.codMunicipio != null ? this.codMunicipio.hashCode() : 0);
        hash = 97 * hash + (this.codProcedimiento != null ? this.codProcedimiento.hashCode() : 0);
        hash = 97 * hash + (this.ejercicio != null ? this.ejercicio.hashCode() : 0);
        hash = 97 * hash + (this.numExpediente != null ? this.numExpediente.hashCode() : 0);
        hash = 97 * hash + (this.codTramite != null ? this.codTramite.hashCode() : 0);
        hash = 97 * hash + (this.codOcurrencia != null ? this.codOcurrencia.hashCode() : 0);
        hash = 97 * hash + (this.codDocumento != null ? this.codDocumento.hashCode() : 0);
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
        final FirmaDocumentoTramiteClave other = (FirmaDocumentoTramiteClave) obj;
        if (this.codMunicipio != other.codMunicipio && (this.codMunicipio == null || !this.codMunicipio.equals(other.codMunicipio))) {
            return false;
        }
        if ((this.codProcedimiento == null) ? (other.codProcedimiento != null) : !this.codProcedimiento.equals(other.codProcedimiento)) {
            return false;
        }
        if (this.ejercicio != other.ejercicio && (this.ejercicio == null || !this.ejercicio.equals(other.ejercicio))) {
            return false;
        }
        if ((this.numExpediente == null) ? (other.numExpediente != null) : !this.numExpediente.equals(other.numExpediente)) {
            return false;
        }
        if (this.codTramite != other.codTramite && (this.codTramite == null || !this.codTramite.equals(other.codTramite))) {
            return false;
        }
        if (this.codOcurrencia != other.codOcurrencia && (this.codOcurrencia == null || !this.codOcurrencia.equals(other.codOcurrencia))) {
            return false;
        }
        if (this.codDocumento != other.codDocumento && (this.codDocumento == null || !this.codDocumento.equals(other.codDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FirmaDocumentoTramiteClave{" + "codMunicipio=" + codMunicipio + ", codProcedimiento=" + codProcedimiento + ", ejercicio=" + ejercicio + ", numExpediente=" + numExpediente + ", codTramite=" + codTramite + ", codOcurrencia=" + codOcurrencia + ", codDocumento=" + codDocumento + '}';
    }

}
