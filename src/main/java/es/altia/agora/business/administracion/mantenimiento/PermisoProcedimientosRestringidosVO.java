
package es.altia.agora.business.administracion.mantenimiento;

/**
 * Reprenta el permiso que tiene un determinado usuario sobre un procedimiento restringido
 * @author oscar.rodriguez
 */
public class PermisoProcedimientosRestringidosVO {
    private String codUsuario;
    private String codProcedimiento;
    private String codMunicipio;
    private String codEntidad;
    private String descProcedimiento;
    private boolean usuarioConPermisoProcedimientoRestringido = false;
    private String descOrganizacion;
    private String descEntidad;

    /**
     * @return the codUsuario
     */
    public String getCodUsuario() {
        return codUsuario;
    }

    /**
     * @param codUsuario the codUsuario to set
     */
    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the codMunicipio
     */
    public String getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the codEntidad
     */
    public String getCodEntidad() {
        return codEntidad;
    }

    /**
     * @param codEntidad the codEntidad to set
     */
    public void setCodEntidad(String codEntidad) {
        this.codEntidad = codEntidad;
    }

    /**
     * @return the descProcedimiento
     */
    public String getDescProcedimiento() {
        return descProcedimiento;
    }

    /**
     * @param descProcedimiento the descProcedimiento to set
     */
    public void setDescProcedimiento(String descProcedimiento) {
        this.descProcedimiento = descProcedimiento;
    }

    /**
     * @return the usuarioConPermisoProcedimientoRestringido
     */
    public boolean isUsuarioConPermisoProcedimientoRestringido() {
        return usuarioConPermisoProcedimientoRestringido;
    }

    /**
     * @param usuarioConPermisoProcedimientoRestringido the usuarioConPermisoProcedimientoRestringido to set
     */
    public void setUsuarioConPermisoProcedimientoRestringido(boolean usuarioConPermisoProcedimientoRestringido) {
        this.usuarioConPermisoProcedimientoRestringido = usuarioConPermisoProcedimientoRestringido;
    }

    /**
     * @return the descOrganizacion
     */
    public String getDescOrganizacion() {
        return descOrganizacion;
    }

    /**
     * @param descOrganizacion the descOrganizacion to set
     */
    public void setDescOrganizacion(String descOrganizacion) {
        this.descOrganizacion = descOrganizacion;
    }

    /**
     * @return the descEntidad
     */
    public String getDescEntidad() {
        return descEntidad;
    }

    /**
     * @param descEntidad the descEntidad to set
     */
    public void setDescEntidad(String descEntidad) {
        this.descEntidad = descEntidad;
    }
    
}