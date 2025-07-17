// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro.mantenimiento;

// PAQUETES IMPORTADOS
import java.io.Serializable;
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

public class ManActuacionesValueObject implements Serializable, ValueObject {
  
  private int ide;
  private String codigo;
  private String descripcion;
  private String fechaDesde;
  private String fechaHasta;
  private String actuacion;  

  // Variable booleana que indica si el estado de la instancia es válido o no
  private boolean isValid;

  public ManActuacionesValueObject() {
    super();
  }

  public ManActuacionesValueObject(int id, String cod, String desc, String fDesde, String fHasta, String act){
	ide = id;
    codigo = cod;
    descripcion = desc;
    fechaDesde = fDesde;
    fechaHasta = fHasta;
	actuacion = act;
  }

  public int getIde(){
    return ide;
  }
  public void setIde(int id){
    ide=id;
  }

  public String getCodigo(){
    return codigo;
  }
  public void setCodigo(String cod){
    codigo=cod;
  }

  public String getDescripcion(){
    return descripcion;
  }
  public void setDescripcion(String desc){
    descripcion=desc;
  }

  public String getFechaDesde(){
    return fechaDesde;
  }
  public void setFechaDesde(String fDesde){
    fechaDesde=fDesde;
  }

  public String getFechaHasta(){
    return fechaHasta;
  }
  public void setFechaHasta(String fHasta){
    fechaHasta=fHasta;
  }

  public String getActuacion(){
    return actuacion;
  }
  public void setActuacion(String act){
    actuacion=act;
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