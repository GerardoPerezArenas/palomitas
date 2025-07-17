package es.altia.agora.business.administracion.mantenimiento;

import java.io.Serializable;
import java.sql.Timestamp;

import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

public class AccesoModuloUsuarioVO implements Serializable, ValueObject {

	private Integer idUsuario;
    private String loginUsuario;
    private String nombreUsuario;
    private Integer idOrganizacion;
    private String nombreOrganizacion;
    private Integer idAplicacion;
    private String nombreAplicacion;
    private Timestamp fechaHora;

    public AccesoModuloUsuarioVO() {
        super();
    }

    public AccesoModuloUsuarioVO(Integer idUsuario, String loginUsuario, String nombreUsuario, Integer idOrganizacion, String nombreOrganizacion, Integer idAplicacion, String nombreAplicacion, Timestamp fechaHora) {
        this.idUsuario = idUsuario;
        this.loginUsuario = loginUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idOrganizacion = idOrganizacion;
        this.nombreOrganizacion = nombreOrganizacion;
        this.idAplicacion = idAplicacion;
        this.nombreAplicacion = nombreAplicacion;
        this.fechaHora = fechaHora;
    }
    
	public void validate(String idioma) throws ValidationException {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public Integer getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(Integer idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }

    public Integer getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(Integer idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public void setNombreAplicacion(String nombreAplicacion) {
        this.nombreAplicacion = nombreAplicacion;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }  
}