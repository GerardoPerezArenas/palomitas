package es.altia.agora.business.gestionInformes;
import java.io.Serializable;

public class CriteriosValueObject implements Serializable {

    private String codPlantilla;
    private String id;
    private String campo;
    private String tipo;
    private String condicion;
    private String valor1;
    private String valor2;
    private String titulo;
    private String origen;
    private String tabla;

  public CriteriosValueObject(){
      super();
  }

  public CriteriosValueObject(String codPlantilla, String id, String campo, String tipo, String condicion,
                              String valor1, String valor2, String titulo, String origen, String tabla){
    this.codPlantilla = codPlantilla;
    this.id = id;
    this.campo = campo;
    this.tipo = tipo;
    this.condicion = condicion;
    this.valor1 = valor1;
    this.valor2 = valor2;
    this.titulo = titulo;
    this.origen = origen;
    this.tabla = tabla;
  }


    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getValor1() {
        return valor1;
    }

    public void setValor1(String valor1) {
        this.valor1 = valor1;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String toString() {
        return ("| codPlantilla = " + codPlantilla + " |\n" +
				"| id = " + id + " |\n" +
                "| tipo = " + tipo + " |\n" +
                "| condicion = " + condicion + " |\n" +
                "| valor1 = " + valor1 + " |\n" +
                "| valor2 = " + valor2 + "|\n" +
                "| titulo = " + titulo + "|\n" +
                "| origen = " + origen + "|\n" +
                "| tabla = " + tabla + "|\n");
    }

}
