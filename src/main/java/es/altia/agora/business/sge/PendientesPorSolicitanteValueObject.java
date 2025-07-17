package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class PendientesPorSolicitanteValueObject implements Serializable, ValueObject {

    /** Construye un nuevo PendientesPorSolicitante por defecto. */
    public PendientesPorSolicitanteValueObject() {
        super();
    }

public String getTxtNombre() {
  return txtNombre;
}
public void setTxtNombre(String txtNombre) {
  this.txtNombre = txtNombre;
}
public String getTxtDNI() {
  return txtDNI;
}
public void setTxtDNI(String txtDNI) {
  this.txtDNI = txtDNI;
}
public String getDescTipoDoc() {
  return descTipoDoc;
}
public void setDescTipoDoc(String descTipoDoc) {
  this.descTipoDoc = descTipoDoc;
}
public String getCodTipoDoc() {
  return codTipoDoc;
}
public void setCodTipoDoc(String codTipoDoc) {
  this.codTipoDoc = codTipoDoc;
}
public String getTxtRegistros() {
  return txtRegistros;
}
public void setTxtRegistros(String txtRegistros) {
  this.txtRegistros = txtRegistros;
}
public String getVersion() {
  return version;
}
public void setVersion(String version) {
  this.version = version;
}
public String getIdentificadorTerc() {
  return identificadorTerc;
}
public void setIdentificadorTerc(String identificadorTerc) {
  this.identificadorTerc = identificadorTerc;
}
public String getAno() {
    return ano;
  }
public void setAno(String ano) {
  this.ano = ano;
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
public String getEstado() {
  return estado;
}
public void setEstado(String estado) {
  this.estado = estado;
}
public String getFechaFin() {
  return fechaFin;
}
public void setFechaFin(String fechaFin) {
  this.fechaFin = fechaFin;
}
public String getFechaInicio() {
  return fechaInicio;
}
public void setFechaInicio(String fechaInicio) {
  this.fechaInicio = fechaInicio;
}
public String getNumero() {
  return numero;
}
public void setNumero(String numero) {
  this.numero = numero;
}
public String getEntrada() {
  return entrada;
}
public void setEntrada(String entrada) {
  this.entrada = entrada;
}


public Vector getListaTiposDocumentos() {
  return listaTiposDocumentos;
}
public void setListaTiposDocumentos(Vector listaTiposDocumentos) {
  this.listaTiposDocumentos = listaTiposDocumentos;
}
public Vector getConsulta() {
  return consulta;
}
public void setConsulta(Vector consulta) {
  this.consulta = consulta;
}

    public void copy(PendientesPorSolicitanteValueObject other) {
      this.codTipoDoc = other.codTipoDoc;
      this.descTipoDoc = other.descTipoDoc;
      this.txtDNI = other.txtDNI;
      this.txtNombre = other.txtNombre;
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

    private String codTipoDoc;
    private String descTipoDoc;
    private String txtDNI;
    private String txtNombre;
    private String txtRegistros;
    private String version;
    private String identificadorTerc;
    private String codDepartamento;
    private String codProcedimiento;
    private String ano;
    private String numero;
    private String fechaInicio;
    private String fechaFin;
    private String estado;
    private String entrada;

    private Vector listaTiposDocumentos;
    private Vector consulta;


    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
