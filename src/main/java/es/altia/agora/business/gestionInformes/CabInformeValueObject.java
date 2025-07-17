package es.altia.agora.business.gestionInformes;
import java.io.Serializable;

public class CabInformeValueObject implements Serializable {

    private String codPlantilla;
    private String idCampo;
    private String posX;
    private String posY;
    private String tipo;
    private String informacion;

  public CabInformeValueObject(){
      super();
  }

  public CabInformeValueObject(String codPlantilla, String idCampo, String posX, String posY, String tipo,
                               String informacion){
    this.codPlantilla = codPlantilla;
    this.idCampo = idCampo;
    this.posX = posX;
    this.posY = posY;
    this.tipo = tipo;
    this.informacion  = informacion;
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

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
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
                "| posX = " + posX + " |\n" +
                "| posY = " + posY + " |\n" +
                "| tipo = " + tipo + " |\n" +
                "| informacion = " + informacion + " |\n");
    }

}
