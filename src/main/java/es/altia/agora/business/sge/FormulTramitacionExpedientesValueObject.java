package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;

public class FormulTramitacionExpedientesValueObject implements Serializable, ValueObject {

    /** Construye un nuevo TramitacionExpedientes por defecto. */
    public FormulTramitacionExpedientesValueObject() {
        super();
    }

    public String getTxtDescripcion() {
      return txtDescripcion;
    }
    public void setTxtDescripcion(String txtDescripcion) {
      this.txtDescripcion = txtDescripcion;
    }
    public String getTxtExpediente() {
      return txtExpediente;
    }
    public void setTxtExpediente(String txtExpediente) {
      this.txtExpediente = txtExpediente;
    }
    public String getTxtProcedimiento() {
      return txtProcedimiento;
    }
    public void setTxtProcedimiento(String txtProcedimiento) {
      this.txtProcedimiento = txtProcedimiento;
    }
    public String getTxtSolicitante() {
      return txtSolicitante;
    }
    public void setTxtSolicitante(String txtSolicitante) {
      this.txtSolicitante = txtSolicitante;
    }
    public String getTxtTramite() {
      return txtTramite;
    }
    public void setTxtTramite(String txtTramite) {
      this.txtTramite = txtTramite;
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

    private String txtExpediente;
    private String txtProcedimiento;
    private String txtSolicitante;
    private String txtTramite;
    private String txtDescripcion;

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
