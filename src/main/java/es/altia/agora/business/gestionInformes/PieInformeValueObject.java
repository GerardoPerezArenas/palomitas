package es.altia.agora.business.gestionInformes;
import java.io.Serializable;

public class PieInformeValueObject implements Serializable {

    private String codPlantilla;
    private String idCampo;
    private String tipo;
    private String informacion;

  public PieInformeValueObject(){
      super();
  }

  public PieInformeValueObject(String codPlantilla, String idCampo, String tipo, String informacion){
    this.codPlantilla = codPlantilla;
    this.idCampo = idCampo;
    this.tipo = tipo;
    this.informacion = informacion;
  }


    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String toString() {
        return ("| codPlantilla = " + codPlantilla + " |\n" +
				"| idCampo = " + idCampo + " |\n" +
                "| tipo = " + tipo + " |\n" +
                "| informacion = " + informacion + "|\n");
    }

}
