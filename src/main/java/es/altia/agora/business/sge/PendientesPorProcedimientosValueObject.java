package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class PendientesPorProcedimientosValueObject implements Serializable, ValueObject {

    /** Construye un nuevo registroEntradaSalida por defecto. */
    public PendientesPorProcedimientosValueObject() {
        super();
    }

public String getCodProcedimiento() {
  return codProcedimiento;
}
public void setCodProcedimiento(String codProcedimiento) {
  this.codProcedimiento = codProcedimiento;
}
public String getCodMunicipio() {
  return codMunicipio;
}
public void setCodMunicipio(String codMunicipio) {
  this.codMunicipio = codMunicipio;
}
public String getDescProcedimiento() {
  return descProcedimiento;
}
public void setDescProcedimiento(String descProcedimiento) {
  this.descProcedimiento = descProcedimiento;
}
public String getTxtRegistros() {
  return txtRegistros;
}
public void setTxtRegistros(String txtRegistros) {
  this.txtRegistros = txtRegistros;
}
public String getAno() {
  return ano;
}
public void setAno(String ano) {
  this.ano = ano;
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
public String getTercero() {
  return tercero;
}
public void setTercero(String tercero) {
  this.tercero = tercero;
}
public String getVersion() {
  return version;
}
public void setVersion(String version) {
  this.version = version;
}
public String getNombreCompleto() {
  return nombreCompleto;
}
public void setNombreCompleto(String nombreCompleto) {
  this.nombreCompleto = nombreCompleto;
}
public String getEntrada() {
  return entrada;
}
public void setEntrada(String entrada) {
  this.entrada = entrada;
}

public Vector getListaProcedimientos() {
  return listaProcedimientos;
}
public void setListaProcedimientos(Vector listaProcedimientos) {
  this.listaProcedimientos=listaProcedimientos;
}
public Vector getConsulta() {
  return consulta;
}
public void setConsulta(Vector consulta) {
  this.consulta=consulta;
}


    public void copy(PendientesPorProcedimientosValueObject other) {
      this.codProcedimiento = other.codProcedimiento;
      this.descProcedimiento = other.descProcedimiento;
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

    private String codMunicipio;
    private String codProcedimiento;
    private String descProcedimiento;
    private String txtRegistros;
    private String ano;
    private String numero;
    private String fechaInicio;
    private String fechaFin;
    private String tercero;
    private String version;
    private String estado;
    private String nombreCompleto;
    private String entrada;

    private Vector listaProcedimientos;
    private Vector consulta;


    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
