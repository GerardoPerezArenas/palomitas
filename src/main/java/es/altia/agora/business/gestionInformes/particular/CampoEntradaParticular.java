package es.altia.agora.business.gestionInformes.particular;

import java.util.ArrayList;
import java.io.Serializable;

public class CampoEntradaParticular implements Serializable {

    public final static String TIPO_CAMPO_SELECT = "SELECT";
    public final static String TIPO_CAMPO_CALENDAR = "CALENDAR";
    public final static String TIPO_CAMPO_RANGO_CALENDAR = "RANGO_CALENDAR";
    public final static String TIPO_CAMPO_RADIO = "RADIO";
    public final static String TIPO_CAMPO_LIST = "LIST";
    
    private String tituloCampo;
    private String idCampo;
    private String tipoCampo;
    private ArrayList<CodigoEtiqueta> valoresCampo;

    public CampoEntradaParticular(String idCampo, String tipoCampo, String tituloCampo, ArrayList<CodigoEtiqueta> valoresCampo) {
        this.idCampo = idCampo;
        this.tipoCampo = tipoCampo;
        this.tituloCampo = tituloCampo;
        this.valoresCampo = valoresCampo;
    }

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(String tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public ArrayList<CodigoEtiqueta> getValoresCampo() {
        return valoresCampo;
    }

    public void setValoresCampo(ArrayList<CodigoEtiqueta> valoresCampo) {
        this.valoresCampo = valoresCampo;
    }

    public String getTituloCampo() {
        return tituloCampo;
    }

    public void setTituloCampo(String tituloCampo) {
        this.tituloCampo = tituloCampo;
    }
}
