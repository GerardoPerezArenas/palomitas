// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro;

// PAQUETES IMPORTADOS
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DiligenciasValueObject</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DiligenciasValueObject implements Serializable, ValueObject  {

  private int codDepto;
  private int codUnidad;
  private String fecha;
  private String anotacion;
  private String fechaBuscada;
  private String anotacionBuscada;
  private char tipo;
  private Vector listaDiligencias = new Vector();
  /** Variable booleana que indica si el estado de la instancia de RegistroEntrada es válido o no */
  private boolean isValid;

  public DiligenciasValueObject() {
    super();
  }

  public DiligenciasValueObject(int cDepto, int cUni, String fecha,
                                String anotacion, char tipo) {
    this.codDepto = cDepto;
    this.codUnidad = cUni;
    this.fecha = fecha;
    this.anotacion = anotacion;
    this.tipo = tipo;
  }

  public int getCodDepto(){
    return codDepto;
  }

  public void setCodDepto(int cDepto){
    codDepto = cDepto;
  }

  public int getCodUnidad(){
    return codUnidad;
  }

  public void setCodUnidad(int cUni){
    codUnidad = cUni;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getAnotacion() {
    return anotacion;
  }

  public void setAnotacion(String anotacion) {
    this.anotacion = anotacion;
  }
  
  public String getFechaBuscada() {
    return fechaBuscada;
  }

  public void setFechaBuscada(String fechaBuscada) {
    this.fechaBuscada = fechaBuscada;
  }

  public String getAnotacionBuscada() {
    return anotacionBuscada;
  }

  public void setAnotacionBuscada(String anotacionBuscada) {
    this.anotacionBuscada = anotacionBuscada;
  }

  public char getTipo(){
    return tipo;
  }

  public void setTipo(char tipo){
    this.tipo = tipo;
  }
  
  public Vector getListaDiligencias(){
    return listaDiligencias;
  }

  public void setListaDiligencias(Vector listaDiligencias){
    this.listaDiligencias = listaDiligencias;
  }


  public void validate(String idioma) throws ValidationException {
    String sufijo = "";
    if ("euskera".equals(idioma)) sufijo="_eu";
    boolean correcto = true;
    Messages errors = new Messages();

    //if ((txtDiaAbrir == null) || (txtMesAbrir == null) || ((txtAnoAbrir == null)))
    //  errors.add(new Message("formulario", "registroentrada.error.fecha.required"));
    if (!errors.empty()) throw new ValidationException(errors);
      isValid = true;
  }

  /** Devuelve un booleano que representa si el estado de este RegistroEntrada es válido. */
  public boolean IsValid() { return isValid; }
}