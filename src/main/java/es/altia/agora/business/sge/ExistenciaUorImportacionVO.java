package es.altia.agora.business.sge;

/**
 *
 * @author oscar.rodriguez
 */
public class ExistenciaUorImportacionVO {

    private String nombre;
    private String codigoUor;
    private String codigoUorVisible;
    private boolean existe = false;

    /**
     * @return the codigoUor
     */
    public String getCodigoUorVisible() {
        return codigoUorVisible;
    }

    /**
     * @param codigoUor the codigoUor to set
     */
    public void setCodigoUorVisible(String codigoUor) {
        this.codigoUorVisible = codigoUor;
    }

    /**
     * @return the existe
     */
    public boolean isExiste() {
        return existe;
    }

    /**
     * @param existe the existe to set
     */
    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the codigoUor
     */
    public String getCodigoUor() {
        return codigoUor;
    }

    /**
     * @param codigoUor the codigoUor to set
     */
    public void setCodigoUor(String codigoUor) {
        this.codigoUor = codigoUor;
    }

}