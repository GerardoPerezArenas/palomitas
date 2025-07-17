package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;

public class RelacionEntradasSalidasValueObject implements Serializable, ValueObject {

    /** Construye un nuevo RelacionEntradasSalidas por defecto. */
    public RelacionEntradasSalidasValueObject() {
        super();
    }

    public String getAsunto() {
      return asunto;
    }
    public void setAsunto(String asunto) {
      this.asunto = asunto;
    }
    public String getTxtDocumento() {
      return txtDocumento;
    }
    public void setTxtDocumento(String txtDocumento) {
      this.txtDocumento = txtDocumento;
    }
    public String getTxtFechaES() {
      return txtFechaES;
    }
    public void setTxtFechaES(String txtFechaES) {
      this.txtFechaES = txtFechaES;
    }
    public String getTxtNombre() {
      return txtNombre;
    }
    public void setTxtNombre(String txtNombre) {
      this.txtNombre = txtNombre;
    }
    public String getTxtNumeroRegistro() {
      return txtNumeroRegistro;
    }
    public void setTxtNumeroRegistro(String txtNumeroRegistro) {
      this.txtNumeroRegistro = txtNumeroRegistro;
    }
    public String getTxtRemitente() {
      return txtRemitente;
    }
    public void setTxtRemitente(String txtRemitente) {
      this.txtRemitente = txtRemitente;
    }
    public String getTxtTipoDocumento() {
      return txtTipoDocumento;
    }
    public void setTxtTipoDocumento(String txtTipoDocumento) {
      this.txtTipoDocumento = txtTipoDocumento;
    }
    public String getTxtTipoES() {
      return txtTipoES;
    }
    public void setTxtTipoES(String txtTipoES) {
      this.txtTipoES = txtTipoES;
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

    private String txtDocumento;
    private String txtNombre;
    private String txtNumeroRegistro;
    private String txtFechaES;
    private String txtTipoES;
    private String txtTipoDocumento;
    private String txtRemitente;
    private String asunto;

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
