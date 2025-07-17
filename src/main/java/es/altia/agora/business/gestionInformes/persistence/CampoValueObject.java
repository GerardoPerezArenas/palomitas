package es.altia.agora.business.gestionInformes.persistence;

import es.altia.technical.ValueObject;
import es.altia.technical.ValidationException;
import es.altia.technical.Messages;

import java.io.Serializable;

public class CampoValueObject implements Serializable, ValueObject {

    public CampoValueObject(){}

    public CampoValueObject (String codPlantilla, String idCampo, String titulo, String tituloOriginal, String origen,
                             String posx, String posy, String align, String tabla, String ancho, String elipsis,
                             String condicionCriterio, String valor1Criterio, String valor2Criterio, String tipo,String orden) {
        this.codPlantilla = codPlantilla;
        this.idCampo = idCampo;
        this.titulo = titulo;
        this.tituloOriginal = tituloOriginal;
        this.origen = origen;
        this.posx = posx;
        this.posy = posy;
        this.align = align;
        this.tabla = tabla;
        this.ancho = ancho;
        this.elipsis = elipsis;
        this.condicionCriterio = condicionCriterio;
        this.valor1Criterio = valor1Criterio;
        this.valor2Criterio = valor2Criterio;
        this.tipo = tipo;
        this.orden=orden;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloOriginal() {
        return tituloOriginal;
    }

    public void setTituloOriginal(String tituloOriginal) {
        this.tituloOriginal = tituloOriginal;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getPosx() {
        return posx;
    }

    public void setPosx(String posx) {
        this.posx = posx;
    }

    public String getPosy() {
        return posy;
    }

    public void setPosy(String posy) {
        this.posy = posy;
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

    public String getElipsis() {
        return elipsis;
    }

    public void setElipsis(String elipsis) {
        this.elipsis = elipsis;
    }

    public String getCondicionCriterio() {
        return condicionCriterio;
    }

    public void setCondicionCriterio(String condicionCriterio) {
        this.condicionCriterio = condicionCriterio;
    }

    public String getValor1Criterio() {
        return valor1Criterio;
    }

    public void setValor1Criterio(String valor1Criterio) {
        this.valor1Criterio = valor1Criterio;
    }

    public String getValor2Criterio() {
        return valor2Criterio;
    }

    public void setValor2Criterio(String valor2Criterio) {
        this.valor2Criterio = valor2Criterio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }
    public String toString() {
        return ("| codPlantilla = " + codPlantilla + " |\n" +
				"| idCampo = " + idCampo + " |\n" +
                "| titulo = " + titulo + " |\n" +
                "| tituloOriginal = " + tituloOriginal + " |\n" +
                "| origen = " + origen + " |\n" +
                "| posx = " + posx + " |\n" +
                "| posy = " + posy + " |\n" +
                "| align = " + align + " |\n" +
                "| tabla = " + tabla + " |\n" +
                "| ancho = " + ancho + " |\n" +
                "| elipsis = " + elipsis + " |\n" +
                "| condicionCriterio = " + condicionCriterio + " |\n" +
                "| valor1Criterio = " + valor1Criterio + " |\n" +
                "| valor2Criterio = " + valor2Criterio + " |\n" +
                "| tipo = " + tipo + "|\n"+
                "| ordench = " + orden + "|\n");
    }
    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception es.altia.technical.ValidationException si el estado no es válido
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

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    private String codPlantilla;
    private String idCampo;
    private String titulo;
    private String tituloOriginal;
    private String origen;
    private String posx;
    private String posy;
    private String align;
    private String tabla;
    private String ancho;
    private String elipsis;
    private String condicionCriterio;
    private String valor1Criterio;
    private String valor2Criterio;
    private String tipo;
    private String orden;

/* ******************************************************** */
}
