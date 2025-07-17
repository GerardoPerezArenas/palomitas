package es.altia.agora.business.administracion.mantenimiento;

import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.io.Serializable;
import java.util.Vector;

public class CamposListadoParametrizablesProcedimientoVO implements Serializable, ValueObject{

    /** Construye un nuevo registroEntradaSalida por defecto. */
    public CamposListadoParametrizablesProcedimientoVO() {
        super();
    }

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        Messages errors = new Messages();

        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Devuelve un booleano que representa si el estado de este RegistroSaida es válido. */
    public boolean IsValid() { return isValid; }

	
    public void setIsValid(boolean isValid) {
            this.isValid = isValid;
    }

	

    public String getCodCampo() {
        return (this.codCampo);
    }

    public void setCodCampo(String codCampo) {
        this.codCampo = codCampo;
    }

    public int getCodListado() {
            return (this.codlistado);
    }

    public void setCodListado(int codlistado) {
            this.codlistado = codlistado;
    }

    public String getNomCampo() {
            return (this.nomCampo);
    }

    public void setNomCampo(String nomCampo) {
            this.nomCampo = nomCampo;
    }

     public String getActCampo() {
            return (this.actCampo);
    }

    public void setActCampo(String actCampo) {
            this.actCampo = actCampo;
    }

      public int getTamanoCampo() {
            return (this.tamanoCampo);
    }

    public void setTamanoCampo(int tamanoCampo) {
            this.tamanoCampo = tamanoCampo;
    }

    public String getOrdenCampo() {
            return (this.ordenCampo);
    }

    public void setOrdenCampo(String ordenCampo) {
            this.ordenCampo = ordenCampo;
    }

  public void setListaCampos(Vector listaCampos) {
    this.listaCampos = listaCampos;
  }
  public Vector getListaCampos() {
    return listaCampos;
  }
  //almacenamos en un vector los datos del listado para hacer el insert
   public void setvNomCampo(Vector vnomCampo) {
    this.vnomCampo = vnomCampo;
  }
  public Vector getvNomCampo() {
    return vnomCampo;
  }

 public void setvCodCampo(Vector vcodCampo) {
    this.vcodCampo = vcodCampo;
  }
  public Vector getvCodCampo() {
    return vcodCampo;
  }

  public void setvTamanoCampo(Vector vtamanoCampo) {
    this.vtamanoCampo = vtamanoCampo;
  }
  public Vector getvTamanoCampo() {
    return vtamanoCampo;
  }

   public void setvActCampo(Vector vactCampo) {
    this.vactCampo = vactCampo;
  }
  public Vector getvActCampo() {
    return vactCampo;
  }

   /**
     * @return the etiquetaIdioma
     */
  public String getEtiquetaIdioma() {
    return etiquetaIdioma;
  }

  /**
     * @param etiquetaIdioma the etiquetaIdioma to set
     */
  public void setEtiquetaIdioma(String etiquetaIdioma) {
    this.etiquetaIdioma = etiquetaIdioma;
  }


  /**
    * @return the campoSuplementario
    */
  public boolean isCampoSuplementario() {
      return campoSuplementario;
  }

  /**
     * @param campoSuplementario the campoSuplementario to set
     */
  public void setCampoSuplementario(boolean campoSuplementario) {
      this.campoSuplementario = campoSuplementario;
  }



    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
    
    private String codCampo;
    private int codlistado;
    private String nomCampo;
    private String actCampo;
    private int tamanoCampo;
    //private int ordenCampo;
    private String ordenCampo;
    private Vector listaCampos;
    private Vector vnomCampo;
    private Vector vcodCampo;
    private Vector vtamanoCampo;
    private Vector vactCampo;
    private String etiquetaIdioma;
    private boolean campoSuplementario;
    private boolean campoTexto;

    /**
     * @return the campoTexto
     */
    public boolean isCampoTexto() {
        return campoTexto;
    }

    /**
     * @param campoTexto the campoTexto to set
     */
    public void setCampoTexto(boolean campoTexto) {
        this.campoTexto = campoTexto;
    }

}
