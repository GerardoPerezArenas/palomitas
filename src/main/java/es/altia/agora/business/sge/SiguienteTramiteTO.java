package es.altia.agora.business.sge;

import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.io.Serializable;

public class SiguienteTramiteTO implements Serializable, ValueObject {

    String codigoTramiteFlujoSalida;
    String codigoVisibleTramiteFlujoSalida;
    String descripcionTramiteFlujoSalida;
    String numeroSecuencia;
    int modoSeleccionUnidad;
    private String codUnidadInicioTramite;

    private boolean isValid;

    public SiguienteTramiteTO() {
        super();
    }

    public String getCodigoTramiteFlujoSalida() {
        return codigoTramiteFlujoSalida;
    }

    public void setCodigoTramiteFlujoSalida(String codigoTramiteFlujoSalida) {
        this.codigoTramiteFlujoSalida = codigoTramiteFlujoSalida;
    }

    public String getCodigoVisibleTramiteFlujoSalida() {
        return codigoVisibleTramiteFlujoSalida;
    }

    public void setCodigoVisibleTramiteFlujoSalida(String codigoVisibleTramiteFlujoSalida) {
        this.codigoVisibleTramiteFlujoSalida = codigoVisibleTramiteFlujoSalida;
    }

    public String getDescripcionTramiteFlujoSalida() {
        return descripcionTramiteFlujoSalida;
    }

    public void setDescripcionTramiteFlujoSalida(String descripcionTramiteFlujoSalida) {
        this.descripcionTramiteFlujoSalida = descripcionTramiteFlujoSalida;
    }

    public int getModoSeleccionUnidad() {
        return modoSeleccionUnidad;
    }

    public void setModoSeleccionUnidad(int modoSeleccionUnidad) {
        this.modoSeleccionUnidad = modoSeleccionUnidad;
    }

    public String getNumeroSecuencia() {
        return numeroSecuencia;
    }

    public void setNumeroSecuencia(String numeroSecuencia) {
        this.numeroSecuencia = numeroSecuencia;
    }

    public boolean IsValid() { return isValid; }

    public void validate(String idioma) throws ValidationException {

        Messages errors = new Messages();
        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /**
     * @return the codUnidadInicioTramite
     */
    public String getCodUnidadInicioTramite() {
        return codUnidadInicioTramite;
    }

    /**
     * @param codUnidadInicioTramite the codUnidadInicioTramite to set
     */
    public void setCodUnidadInicioTramite(String codUnidadInicioTramite) {
        this.codUnidadInicioTramite = codUnidadInicioTramite;
    }
}
