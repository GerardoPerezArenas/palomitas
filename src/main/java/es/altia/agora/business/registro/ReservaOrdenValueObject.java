package es.altia.agora.business.registro;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Date;
import es.altia.agora.technical.Fecha;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReservaOrdenValueObject implements Serializable, ValueObject {

    protected static Log m_Log =
                LogFactory.getLog(ReservaOrdenValueObject.class.getName());

    /** Construye un nuevo ReservaOrden por defecto. */
    public ReservaOrdenValueObject() {
        super();
    }

    /*
    public ReservaOrdenValueObject() {

    }
    */

    public String getEjercicio(){
       return ejercicio;
   }

   public void setEjercicio(String param) {
       this.ejercicio=param;
   }

   public int getTxtNumRegistrado(){
       return txtNumRegistrado;
   }

   public void setTxtNumRegistrado(int param) {
       this.txtNumRegistrado=param;
   }

   public String getTxtDiaEntrada() {
       if(fecha==null) {
         return txtDiaEntrada;
       } else {
       Fecha f = new Fecha();
       String ff = f.obtenerString(fecha);
       m_Log.debug("el dia es: "+ ff.substring(0,2));
       return ff.substring(0,2);
       }
   }

   public void setTxtDiaEntrada(String param) {
       this.txtDiaEntrada=param;
   }

   public String getTxtMesEntrada(){
       if(fecha==null) {
       return txtDiaEntrada;
       } else {
       Fecha f = new Fecha();
       String ff = f.obtenerString(fecha);
       return ff.substring(3,5);
       }
   }

   public void setTxtMesEntrada(String param) {
       this.txtMesEntrada=param;
   }

   public String getTxtAnoEntrada(){
       if(fecha==null) {
       return txtDiaEntrada;
       } else {
       Fecha f = new Fecha();
       String ff = f.obtenerString(fecha);
       return ff.substring(6,10);
       }
   }

   public void setTxtAnoEntrada(String param) {
       this.txtAnoEntrada=param;
   }

   public void setCodDepto(int codDepto) {
      this.codDepto = codDepto;
   }

   public int getCodDepto() {
      return codDepto;
   }

   public void setCodUnidad(int codUnidad) {
      this.codUnidad = codUnidad;
   }

   public int getCodUnidad() {
      return codUnidad;
   }

   public void setTipoReg(String tipoReg) {
      this.tipoReg = tipoReg;
   }

   public String getTipoReg() {
      return tipoReg;
   }

   public Date getFecha() {
      return fecha;
   }

   public void setFecha(Date fecha) {
      this.fecha = fecha;
   }

   public void setFecha(String param) {
      Fecha f= new Fecha();
      this.fecha=f.obtenerDate(param);
   }

   public String getFechaString() {

      String dia=getTxtDiaEntrada();
      String mes=getTxtMesEntrada();
      String anho=getTxtAnoEntrada();;
      String data=dia+"/"+mes+"/"+anho;
      m_Log.debug("La fecha para el ultimo insert es "+ data);
      return data;
   }

   public boolean getA() {
       return a;
   }

   public void setA(boolean a) {
       this.a=a;
   }

   public Vector getCodigos() {
     return codigos;
   }

   public void setCodigos(Vector param) {
     this.codigos=param;
   }

   public String getFec() {
     m_Log.debug("la fecha es ****************** " + fec);
     return fec;
   }

   public void setFec(String param) {
     this.fec=param;
   }

   public String getDia() {
    return dia;
  }
  public void setDia(String dia) {
    this.dia = dia;
  }
  public String getHora() {
    return hora;
  }
  public void setHora(String hora) {
    this.hora = hora;
  }
  public String getMes() {
    return mes;
  }
  public void setMes(String mes) {
    this.mes = mes;
  }
  public String getMin() {
    return min;
  }
  public void setMin(String min) {
    this.min = min;
  }
  public String getAno() {
    return ano;
  }
  public void setAno(String ano) {
    this.ano = ano;
  }

  public String getCantidad() {
   return cantidad;
  }
  public void setCantidad(String param) {
   this.cantidad = param;
  }

  public Vector getNumeros() {    
   return numeros;
  }
  public void setNumeros(Vector param) {
   this.numeros = param;
  }

  public int getUsuario() {
     return usuario;
  }

  public void setUsuario(int usuario) {
     this.usuario = usuario;
  }

  
  public String getNombreUsuario() {
        return nombreUsuario;
    }

  public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }



  public void copy(ReservaOrdenValueObject other) {
        this.ejercicio=other.ejercicio;
        this.txtNumRegistrado=other.txtNumRegistrado;
        this.txtDiaEntrada=other.txtDiaEntrada;
        this.txtMesEntrada=other.txtMesEntrada;
        this.txtAnoEntrada=other.txtAnoEntrada;

    }


    /**
     * Valida el estado de esta ReservaOrden
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


    private String ejercicio;
    private int txtNumRegistrado;
    private String txtDiaEntrada;
    private String txtMesEntrada;
    private String txtAnoEntrada;
    private int codDepto;
    private int codUnidad;
    private String tipoReg;
    private Date fecha;
    private boolean a;
    private Vector codigos;
    private String fec;
    private String dia;
    private String mes;
    private String ano;
    private String hora;
    private String min;
    private String cantidad;
    private Vector numeros;
    private int usuario;
    private String nombreUsuario;
    
    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;         

    public String toString() {
        return codUnidad + "/" + codDepto + "/" + tipoReg + "/" + ejercicio + "/" + txtNumRegistrado + " - " + fecha;
    }
}
