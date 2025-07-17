package es.altia.agora.business.administracion;

/**
 *
 * @author oscar.rodriguez
 */
public class OrganizacionVO {
    private int codOrganizacion;
    private String descripcionOrganizacion;
    private String iconoOrganizacion;
    private int cssOrganizacion;
    
    public OrganizacionVO(){ }
    
    public OrganizacionVO(int codOrganizacion,String descripcionOrganizacion,String iconoOrganizacion,int cssOrganizacion){
        this.codOrganizacion = codOrganizacion;
        this.descripcionOrganizacion = descripcionOrganizacion;
        this.iconoOrganizacion = iconoOrganizacion;
        this.cssOrganizacion = cssOrganizacion;
    }
            
    public OrganizacionVO(OrganizacionVO org){
        this.codOrganizacion = org.getCodOrganizacion();
        this.descripcionOrganizacion = org.getDescripcionOrganizacion();
        this.iconoOrganizacion = org.getIconoOrganizacion();
        this.cssOrganizacion = org.getCssOrganizacion();
    }
            
    /**
     * @return the codOrganizacion
     */
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    /**
     * @return the descripcionOrganizacion
     */
    public String getDescripcionOrganizacion() {
        return descripcionOrganizacion;
    }

    /**
     * @param descripcionOrganizacion the descripcionOrganizacion to set
     */
    public void setDescripcionOrganizacion(String descripcionOrganizacion) {
        this.descripcionOrganizacion = descripcionOrganizacion;
    }

    /**
     * @return the iconoOrganizacion
     */
    public String getIconoOrganizacion() {
        return iconoOrganizacion;
    }

    /**
     * @param descripcionOrganizacion the descripcionOrganizacion to set
     */
    public void setIconoOrganizacion(String iconoOrganizacion) {
        this.iconoOrganizacion = iconoOrganizacion;
    }

    public int getCssOrganizacion() {
        return cssOrganizacion;
    }

    public void setCssOrganizacion(int cssOrganizacion) {
        this.cssOrganizacion = cssOrganizacion;
    }
}