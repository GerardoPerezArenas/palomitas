package es.altia.flexia.registro.justificante.vo;

public class JustificanteRegistroPersonalizadoVO {
    private String nombreJustificante;
    private String extensionJustificante;
    private long tamanhoJustificante;
    private String rutaDiscoJustificante;
    private boolean defecto;
    private String descripcionDefecto;
    private String descripcionJustificante;
    // #239565: 0:justificante, 1:peticion_modelo_respuesta
    private int tipoJustificante;

    /**
     * @return the nombreJustificante
     */
    public String getNombreJustificante() {
        return nombreJustificante;
    }

    /**
     * @param nombreJustificante the nombreJustificante to set
     */
    public void setNombreJustificante(String nombreJustificante) {
        this.nombreJustificante = nombreJustificante;
    }

    /**
     * @return the extensionJustificante
     */
    public String getExtensionJustificante() {
        return extensionJustificante;
    }

    /**
     * @param extensionJustificante the extensionJustificante to set
     */
    public void setExtensionJustificante(String extensionJustificante) {
        this.extensionJustificante = extensionJustificante;
    }

    /**
     * @return the tamanhoJustificante
     */
    public long getTamanhoJustificante() {
        return tamanhoJustificante;
    }

    /**
     * @param tamanhoJustificante the tamanhoJustificante to set
     */
    public void setTamanhoJustificante(long tamanhoJustificante) {
        this.tamanhoJustificante = tamanhoJustificante;
    }

    /**
     * @return the rutaDiscoJustificante
     */
    public String getRutaDiscoJustificante() {
        return rutaDiscoJustificante;
    }

    /**
     * @param rutaDiscoJustificante the rutaDiscoJustificante to set
     */
    public void setRutaDiscoJustificante(String rutaDiscoJustificante) {
        this.rutaDiscoJustificante = rutaDiscoJustificante;
    }

    /**
     * @return the defecto
     */
    public boolean isDefecto() {
        return defecto;
    }

    /**
     * @param defecto the defecto to set
     */
    public void setDefecto(boolean defecto) {
        this.defecto = defecto;
    }

    /**
     * @return the descripcionJustificante
     */
    public String getDescripcionJustificante() {
        return descripcionJustificante;
    }

    /**
     * @param descripcionJustificante the descripcionJustificante to set
     */
    public void setDescripcionJustificante(String descripcionJustificante) {
        this.descripcionJustificante = descripcionJustificante;
    }

    /**
     * @return the descripcionDefecto
     */
    public String getDescripcionDefecto() {
        return descripcionDefecto;
    }

    /**
     * @param descripcionDefecto the descripcionDefecto to set
     */
    public void setDescripcionDefecto(String descripcionDefecto) {
        this.descripcionDefecto = descripcionDefecto;
    }

    /**
     * @return the tipoJustificante
     */
    public int getTipoJustificante() {
        return tipoJustificante;
    }

    /**
     * @param tipoJustificante the tipoJustificante to set
     */
    public void setTipoJustificante(int tipoJustificante) {
        this.tipoJustificante = tipoJustificante;
    }

    @Override
    public String toString() {
        return "JustificanteRegistroPersonalizadoVO{" +
                "nombreJustificante='" + nombreJustificante + '\'' +
                ", extensionJustificante='" + extensionJustificante + '\'' +
                ", tamanhoJustificante=" + tamanhoJustificante +
                ", rutaDiscoJustificante='" + rutaDiscoJustificante + '\'' +
                ", defecto=" + defecto +
                ", descripcionDefecto='" + descripcionDefecto + '\'' +
                ", descripcionJustificante='" + descripcionJustificante + '\'' +
                ", tipoJustificante=" + tipoJustificante +
                '}';
    }
}