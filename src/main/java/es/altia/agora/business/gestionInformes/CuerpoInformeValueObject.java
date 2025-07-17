package es.altia.agora.business.gestionInformes;
import java.io.Serializable;

public class CuerpoInformeValueObject implements Serializable {

    private String codPlantilla;
    private String idCampo;
    private String origen;
    private String posX;
    private String posY;
    private String titulo;
    private String align;
    private String tabla;
    private String ancho;
    private boolean elipsis;
    private String orden;



  public CuerpoInformeValueObject(){
      super();
  }

  public CuerpoInformeValueObject(String codPlantilla, String idCampo, String origen, String posX, String posY,
                                  String titulo, String align, String tabla, String ancho, boolean elipsis,String orden){
    this.codPlantilla = codPlantilla;
    this.idCampo = idCampo;
    this.origen = origen;
    this.posX = posX;
    this.posY = posY;
    this.titulo = titulo;
    this.align  = align;
    this.tabla = tabla;
    this.ancho = ancho;
    this.elipsis = elipsis;
    this.orden = orden;
   
  }

  public String getOrden() {
      return orden;
  }

  public void setOrden(String orden) {
      this.orden = orden;
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

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getAncho() {
        return ancho;
    }

    public void setAncho(String ancho) {
        this.ancho = ancho;
    }

    public boolean getElipsis() {
        return elipsis;
    }

    public void setElipsis(boolean elipsis) {
        this.elipsis = elipsis;
    }

    public String toString() {
        return ("| codPlantilla = " + codPlantilla + " |\n" +
            	"| idCampo = " + idCampo + " |\n" +
				"| origen = " + origen + " |\n" +
                "| posX = " + posX + " |\n" +
                "| posY = " + posY + " |\n" +
                "| titulo = " + titulo + " |\n" +
                "| align = " + align + " |\n" +
                "| tabla = " + tabla + " |\n" +
                "| ancho = " + ancho + " |\n" +
                "| elipsis = " + elipsis + " |\n" +
                "| orden = " + orden + " |\n\n");
    }

}
