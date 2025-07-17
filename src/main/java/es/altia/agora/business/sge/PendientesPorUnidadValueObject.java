package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class PendientesPorUnidadValueObject implements Serializable, ValueObject {

    /** Construye un nuevo pendientesPorUnidad por defecto. */
    public PendientesPorUnidadValueObject() {
        super();
    }

public String getCodUnidad() {
  return codUnidad;
}
public void setCodUnidad(String codUnidad) {
  this.codUnidad = codUnidad;
}
public String getDescUnidad() {
  return descUnidad;
}
public void setDescUnidad(String descUnidad) {
  this.descUnidad = descUnidad;
}
public String getTxtRegistros() {
  return txtRegistros;
}
public void setTxtRegistros(String txtRegistros) {
  this.txtRegistros = txtRegistros;
}

public Vector getListaUnidades() {
  return listaUnidades;
}
public void setListaUnidades(Vector listaUnidades) {
  this.listaUnidades=listaUnidades;
}

    public void copy(PendientesPorUnidadValueObject other) {
      this.codUnidad = other.codUnidad;
      this.descUnidad = other.descUnidad;
      this.txtRegistros = other.txtRegistros;
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

    private String codUnidad;
    private String descUnidad;
    private String txtRegistros;

    private Vector listaUnidades;


    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
