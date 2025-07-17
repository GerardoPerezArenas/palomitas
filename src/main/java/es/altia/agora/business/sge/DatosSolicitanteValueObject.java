package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class DatosSolicitanteValueObject implements Serializable, ValueObject {

    /** Construye un nuevo DatosSolicitante por defecto. */
    public DatosSolicitanteValueObject() {
        super();
    }

    public String getCodDepartamento() {
     return codDepartamento;
   }
   public void setCodDepartamento(String codDepartamento) {
     this.codDepartamento = codDepartamento;
   }
   public String getCodMunicipio() {
     return codMunicipio;
   }
   public void setCodMunicipio(String codMunicipio) {
     this.codMunicipio = codMunicipio;
   }
   public String getCodPais() {
     return codPais;
   }
   public void setCodPais(String codPais) {
     this.codPais = codPais;
   }
   public String getCodProcedimiento() {
     return codProcedimiento;
   }
   public void setCodProcedimiento(String codProcedimiento) {
     this.codProcedimiento = codProcedimiento;
   }
   public String getCodProvincia() {
     return codProvincia;
   }
   public void setCodProvincia(String codProvincia) {
     this.codProvincia = codProvincia;
   }
   public String getCodTipoDoc() {
     return codTipoDoc;
   }
   public void setCodTipoDoc(String codTipoDoc) {
     this.codTipoDoc = codTipoDoc;
   }
   public String getDescDepartamento() {
     return descDepartamento;
   }
   public void setDescDepartamento(String descDepartamento) {
     this.descDepartamento = descDepartamento;
   }
   public String getDescMunicipio() {
     return descMunicipio;
   }
   public void setDescMunicipio(String descMunicipio) {
     this.descMunicipio = descMunicipio;
   }
   public String getDescPais() {
     return descPais;
   }
   public void setDescPais(String descPais) {
     this.descPais = descPais;
   }
   public String getDescProcedimiento() {
     return descProcedimiento;
   }
   public void setDescProcedimiento(String descProcedimiento) {
     this.descProcedimiento = descProcedimiento;
   }
   public String getDescProvincia() {
     return descProvincia;
   }
   public void setDescProvincia(String descProvincia) {
     this.descProvincia = descProvincia;
   }
   public String getDescTipoDoc() {
     return descTipoDoc;
   }
   public void setDescTipoDoc(String descTipoDoc) {
     this.descTipoDoc = descTipoDoc;
   }
   public String getTxtCodigoPostal() {
     return txtCodigoPostal;
   }
   public void setTxtCodigoPostal(String txtCodigoPostal) {
     this.txtCodigoPostal = txtCodigoPostal;
   }
   public String getTxtDNI() {
     return txtDNI;
   }
   public void setTxtDNI(String txtDNI) {
     this.txtDNI = txtDNI;
   }
   public String getTxtDomicilio() {
     return txtDomicilio;
   }
   public void setTxtDomicilio(String txtDomicilio) {
     this.txtDomicilio = txtDomicilio;
   }
   public String getTxtExpediente() {
     return txtExpediente;
   }
   public void setTxtExpediente(String txtExpediente) {
     this.txtExpediente = txtExpediente;
   }
   public String getTxtNombre() {
     return txtNombre;
   }
   public void setTxtNombre(String txtNombre) {
     this.txtNombre = txtNombre;
   }
   public String getTxtRegistro() {
     return txtRegistro;
   }
   public void setTxtRegistro(String txtRegistro) {
     this.txtRegistro = txtRegistro;
   }
   public String getTxtTelefono() {
     return txtTelefono;
   }
   public void setTxtTelefono(String txtTelefono) {
     this.txtTelefono = txtTelefono;
  }

  public Vector getListaDepartamentos() {
   return listaDepartamentos;
  }
  public void setListaDepartamentos(Vector listaDepartamentos) {
   this.listaDepartamentos = listaDepartamentos;
  }
  public Vector getListaTiposDocumentos() {
   return listaTiposDocumentos;
  }
  public void setListaTiposDocumentos(Vector listaTiposDocumentos) {
   this.listaTiposDocumentos = listaTiposDocumentos;
  }
  public Vector getListaPaises() {
   return listaPaises;
  }
  public void setListaPaises(Vector listaPaises) {
   this.listaPaises = listaPaises;
  }
  public Vector getListaProvincias() {
   return listaProvincias;
  }
  public void setListaProvincias(Vector listaProvincias) {
   this.listaProvincias = listaProvincias;
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

    private String txtRegistro;
    private String txtExpediente;
    private String codDepartamento;
    private String descDepartamento;
    private String codProcedimiento;
    private String descProcedimiento;
    private String codTipoDoc;
    private String descTipoDoc;
    private String txtDNI;
    private String txtNombre;
    private String txtDomicilio;
    private String codPais;
    private String descPais;
    private String codProvincia;
    private String descProvincia;
    private String codMunicipio;
    private String descMunicipio;
    private String txtCodigoPostal;
    private String txtTelefono;

    private Vector listaDepartamentos;
    private Vector listaTiposDocumentos;
    private Vector listaPaises;
    private Vector listaProvincias;

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
