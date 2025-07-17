package es.altia.flexia.registro.oficinasregistro.lanbide.vo;

/**
 * Representa a una unidad organizativa que hace de oficina
 * @author oscar.rodriguez
 */
public class UnidadRegistroLanbideVO {
    private String codOficina;
    private String nivel;
    private String codVisibleOficina;
    private String nombreOficina;
    private String codPadre;
    private boolean oficinaRegistro;

    /**
     * @return the codOficina
     */
    public String getCodOficina() {
        return codOficina;
    }

    public String getCodPadre() {
        return codPadre;
    }

    public void setCodPadre(String codPadre) {
        this.codPadre = codPadre;
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

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the oficinaRegistro
     */
    public boolean isOficinaRegistro() {
        return oficinaRegistro;
    }

    /**
     * @param oficinaRegistro the oficinaRegistro to set
     */
    public void setOficinaRegistro(boolean oficinaRegistro) {
        this.oficinaRegistro = oficinaRegistro;
    }


}