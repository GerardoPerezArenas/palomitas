package es.altia.flexia.tramitacion.externa.plugin;


public class EnlaceTramitacionBase {
    private String idEnlaceConfiguracion;
    private String[] parametros;
    private String[] metodos;
    private String url;

    /**
     * @return the parametros
     */
    public String[] getParametros() {
        return parametros;
    }

    /**
     * @param parametros the parametros to set
     */
    public void setParametros(String[] parametros) {
        this.parametros = parametros;
    }

    /**
     * @return the metodos
     */
    public String[] getMetodos() {
        return metodos;
    }

    /**
     * @param metodos the metodos to set
     */
    public void setMetodos(String[] metodos) {
        this.metodos = metodos;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the idEnlaceConfiguracion
     */
    public String getIdEnlaceConfiguracion() {
        return idEnlaceConfiguracion;
    }

    /**
     * @param idEnlaceConfiguracion the idEnlaceConfiguracion to set
     */
    public void setIdEnlaceConfiguracion(String idEnlaceConfiguracion) {
        this.idEnlaceConfiguracion = idEnlaceConfiguracion;
    }

}