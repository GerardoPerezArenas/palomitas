package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class TablasIntercambiadorasValueObject implements Serializable, ValueObject {

    /** Construye un nuevo Certificados por defecto. */
    public TablasIntercambiadorasValueObject() {
        super();
    }

    public Vector getListaTramitesSeleccion() {
    return listaTramitesSeleccion;
  }
  public void setListaTramitesSeleccion(Vector listaTramitesSeleccion) {
    this.listaTramitesSeleccion = listaTramitesSeleccion;
  }
  public Vector getListaTramitesTodos() {
    return listaTramitesTodos;
  }
  public void setListaTramitesTodos(Vector listaTramitesTodos) {
    this.listaTramitesTodos = listaTramitesTodos;
  }

  public String getCodMunicipio() {
  return codMunicipio;
}
public void setCodMunicipio(String codMunicipio) {
  this.codMunicipio = codMunicipio;
}
public String getCodProcedimiento() {
  return codProcedimiento;
}
public void setCodProcedimiento(String codProcedimiento) {
  this.codProcedimiento = codProcedimiento;
}
public String getCodTramite() {
  return codTramite;
}
public void setCodTramite(String codTramite) {
  this.codTramite = codTramite;
}
public String getNumeroCondicionSalida() {
  return numeroCondicionSalida;
}
public void setNumeroCondicionSalida(String numeroCondicionSalida) {
  this.numeroCondicionSalida = numeroCondicionSalida;
  }
  public Vector getListaCodTramitesFlujoSalida() {
    return listaCodTramitesFlujoSalida;
  }
  public void setListaCodTramitesFlujoSalida(Vector listaCodTramitesFlujoSalida) {
    this.listaCodTramitesFlujoSalida = listaCodTramitesFlujoSalida;
  }
  public Vector getListaNumerosSecuenciaFlujoSalida() {
    return listaNumerosSecuenciaFlujoSalida;
  }
  public void setListaNumerosSecuenciaFlujoSalida(Vector listaNumerosSecuenciaFlujoSalida) {
    this.listaNumerosSecuenciaFlujoSalida = listaNumerosSecuenciaFlujoSalida;
  }
  public String getObligatorio() {
    return obligatorio;
  }
  public void setObligatorio(String obligatorio) {
    this.obligatorio = obligatorio;
  }


     /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
    boolean correcto = true;
        Messages errors = new Messages();

        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Devuelve un booleano que representa si el estado de este RegistroSaida es válido. */
    public boolean IsValid() { return isValid; }

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    private Vector listaTramitesTodos;
    private Vector listaTramitesSeleccion;
    private String codMunicipio;
    private String codProcedimiento;
    private String codTramite;
    private String numeroCondicionSalida;
    private String obligatorio;

    private Vector listaCodTramitesFlujoSalida;
    private Vector listaNumerosSecuenciaFlujoSalida;

}
