package es.altia.agora.business.gestionInformes;

import java.io.Serializable;

public class SolicitudesListaValueObject implements Serializable {



  public SolicitudesListaValueObject(){
      super();
  }

  public SolicitudesListaValueObject(String codigo, String titulo, String estado, String codProcedimiento){
    this.codigo             = codigo;
    this.titulo             = titulo;
    this.estado          = estado;
    this.codProcedimiento   = codProcedimiento;
  }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    private String codigo;
    private String titulo;
    private String estado;
    private String codProcedimiento;
    private String origen;
    private String fecha;
}
