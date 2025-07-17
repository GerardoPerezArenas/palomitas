package es.altia.agora.business.registro;

import java.io.Serializable;

/**
 *
 * @author juan.jato
 */
public class HistoricoMovimientoValueObject implements Serializable {
     
    private int codigo = -1;
    private int codigoUsuario = -1;
    private String nombreUsuario= "";
    private String fecha = "";
    private String tipoEntidad = "";
    private String codigoEntidad = "";
    private int tipoMovimiento = -1;
    private String descMovimiento = "";
    private String detallesMovimiento = "";
    
    public HistoricoMovimientoValueObject() {
        super();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getDescMovimiento() {
        return descMovimiento;
    }

    public void setDescMovimiento(String descMovimiento) {
        this.descMovimiento = descMovimiento;
    }

    public String getDetallesMovimiento() {
        return detallesMovimiento;
    }

    public void setDetallesMovimiento(String detallesMovimiento) {
        this.detallesMovimiento = detallesMovimiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public int getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(int tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    
    public String toString() {
       return  "codigo: " + codigo + 
               ", usuario: " + codigoUsuario + "-" + nombreUsuario +
               ", fecha: " + fecha +
               ", entidad: " + codigoEntidad + "-" + tipoEntidad +
               ", tipo: " + tipoMovimiento + "-" + descMovimiento +
               ", detalles: " + detallesMovimiento;   
    }
    
}
