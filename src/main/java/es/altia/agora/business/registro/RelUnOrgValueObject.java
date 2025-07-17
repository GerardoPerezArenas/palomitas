// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro;

// PAQUETES IMPORTADOS
import java.io.Serializable;
import java.util.Vector;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase RelUnOrgValueObject</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.1
 */

public class RelUnOrgValueObject implements Serializable, ValueObject {

  private Vector deptos;
  private Vector uniTram;
  private String diaDesde;
  private String mesDesde;
  private String anoDesde;
  private String diaHasta;
  private String mesHasta;
  private String anoHasta;
  private String codDepto;
  private String descDepto;
  private String codUniTramFirst;
  private String descUniTramFirst;
  private String codUniTramLast;
  private String descUniTramLast;
  private String regAux;
  private String orden;
  private String tipo;

  // Variable booleana que indica si el estado de la instancia es válido o no
  private boolean isValid;

  public RelUnOrgValueObject() {
    super();
  }

  public RelUnOrgValueObject(Vector listDeptos,Vector listUniTram){
    deptos = listDeptos;
    uniTram = listUniTram;
  }

  public Vector getDeptos(){
    return deptos;
  }

  public void setDeptos(Vector listDeptos){
    deptos = listDeptos;
  }

  public Vector getUniTram(){
    return uniTram;
  }

  public void setUniTram(Vector listUniTram){
    uniTram = listUniTram;
  }

  public String getDiaDesde(){
    return diaDesde;
  }

  public void setDiaDesde(String dia){
    diaDesde=dia;
  }

  public String getMesDesde(){
    return mesDesde;
  }

  public void setMesDesde(String mes){
    mesDesde=mes;
  }

  public String getAnoDesde(){
    return anoDesde;
  }

  public void setAnoDesde(String ano){
    anoDesde=ano;
  }

  public String getDiaHasta(){
    return diaHasta;
  }

  public void setDiaHasta(String dia){
    diaHasta=dia;
  }

  public String getMesHasta(){
    return mesHasta;
  }

  public void setMesHasta(String mes){
    mesHasta=mes;
  }

  public String getAnoHasta(){
    return anoHasta;
  }

  public void setAnoHasta(String ano){
    anoHasta=ano;
  }

  public String getCodDepto(){
    return codDepto;
  }

  public void setCodDepto(String cod){
    codDepto=cod;
  }

  public String getDescDepto(){
    return descDepto;
  }

  public void setDescDepto(String desc){
    descDepto=desc;
  }

  public String getCodUniTramFirst(){
    return codUniTramFirst;
  }

  public void setCodUniTramFirst(String cod){
    codUniTramFirst=cod;
  }

  public String getDescUniTramFirst(){
    return descUniTramFirst;
  }

  public void setDescUniTramFirst(String desc){
    descUniTramFirst=desc;
  }

  public String getCodUniTramLast(){
    return codUniTramLast;
  }

  public void setCodUniTramLast(String cod){
    codUniTramLast=cod;
  }

  public String getDescUniTramLast(){
    return descUniTramLast;
  }

  public void setDescUniTramLast(String desc){
    descUniTramLast=desc;
  }

  public String getRegAux(){
    return regAux;
  }

  public void setRegAux(String reg){
    regAux=reg;
  }

  public String getOrden(){
    return orden;
  }

  public void setOrden(String orden){
    this.orden=orden;
  }

  public String getTipo(){
    return tipo;
  }

  public void setTipo(String tipo){
    this.tipo=tipo;
  }

  public void validate(String idioma) throws ValidationException {
    String sufijo = "";
    if ("euskera".equals(idioma)) sufijo="_eu";
    boolean correcto = true;
    Messages errors = new Messages();

    if (!errors.empty()) throw new ValidationException(errors);
    isValid = true;
  }

  /** Devuelve un booleano que representa si el estado de este RegistroEntrada es válido. */
  public boolean IsValid() { return isValid; }
}