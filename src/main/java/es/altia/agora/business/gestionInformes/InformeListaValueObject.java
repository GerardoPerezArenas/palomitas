package es.altia.agora.business.gestionInformes;
import java.io.Serializable;

public class InformeListaValueObject implements Serializable {



  public InformeListaValueObject(){
      super();
  }

  public InformeListaValueObject(String codigo, String titulo, String publicado, String codProcedimiento, String origen){
    this.codigo             = codigo;
    this.titulo             = titulo;
    this.publicado          = publicado;
    this.codProcedimiento   = codProcedimiento;
    this.origen = origen;
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

    public String getPublicado() {
        return publicado;
    }

    public void setPublicado(String publicado) {
        this.publicado = publicado;
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

  private String codigo;
  private String titulo;
  private String publicado;
  private String codProcedimiento;
  private String origen;
}
