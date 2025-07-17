package es.altia.flexia.registro.oficinasregistro.lanbide.vo;

/**
 * Representa a una unidad organizativa que hace de oficina
 * @author oscar.rodriguez
 */
public class OficinaRegistroLanbideVO {
    private String codOficina;
    private String codVisibleOficina;
    private String nombreOficina;

    /**
     * @return the codOficina
     */
    public String getCodOficina() {
        return codOficina;
    }

    /**
     * @param codOficina the codOficina to set
     */
    public void setCodOficina(String codOficina) {
        this.codOficina = codOficina;
    }

    /**
     * @return the codVisibleOficina
     */
    public String getCodVisibleOficina() {
        return codVisibleOficina;
    }

    /**
     * @param codVisibleOficina the codVisibleOficina to set
     */
    public void setCodVisibleOficina(String codVisibleOficina) {
        this.codVisibleOficina = codVisibleOficina;
    }

    /**
     * @return the nombreOficina
     */
    public String getNombreOficina() {
        return nombreOficina;
    }

    /**
     * @param nombreOficina the nombreOficina to set
     */
    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

}