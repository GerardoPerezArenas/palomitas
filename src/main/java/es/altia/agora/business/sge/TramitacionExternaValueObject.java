package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class TramitacionExternaValueObject implements Serializable, ValueObject {

    /** Construye un nuevo TramitacionExterna por defecto. */
    public TramitacionExternaValueObject() {
        super();
    }

    public String getCodDepartamento() {
      return codDepartamento;
    }
    public void setCodDepartamento(String codDepartamento) {
      this.codDepartamento = codDepartamento;
    }
    public String getCodProcedimiento() {
      return codProcedimiento;
    }
    public void setCodProcedimiento(String codProcedimiento) {
      this.codProcedimiento = codProcedimiento;
    }
    public String getDescDepartamento() {
      return descDepartamento;
    }
    public void setDescDepartamento(String descDepartamento) {
      this.descDepartamento = descDepartamento;
    }
    public String getDescProcedimiento() {
      return descProcedimiento;
    }
    public void setDescProcedimiento(String descProcedimiento) {
      this.descProcedimiento = descProcedimiento;
    }
    public String getCodTramiteExterno() {
      return codTramiteExterno;
    }
    public void setCodTramiteExterno(String codTramiteExterno) {
      this.codTramiteExterno = codTramiteExterno;
    }
    public String getDescTramiteExterno() {
      return descTramiteExterno;
    }
    public void setDescTramiteExterno(String descTramiteExterno) {
      this.descTramiteExterno = descTramiteExterno;
    }


  public Vector getListaDepartamentos() {
    return listaDepartamentos;
  }
  public void setListaDepartamentos(Vector listaDepartamentos) {
    this.listaDepartamentos=listaDepartamentos;
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

    private String codDepartamento;
    private String descDepartamento;
    private String codProcedimiento;
    private String descProcedimiento;
    private String codTramiteExterno;
    private String descTramiteExterno;

    private Vector listaDepartamentos;

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
