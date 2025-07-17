package es.altia.agora.business.administracion;

import java.io.Serializable;
import java.util.Vector;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

public class AutorizacionesExternasValueObject implements Serializable, ValueObject {

    /** Construye un nuevo registroEntradaSalida por defecto. */
    public AutorizacionesExternasValueObject() {
        super();
    }

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
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

    /** Devuelve un booleano que representa si el estado de este RegistroSaida es válido. */
    public boolean IsValid() { return isValid; }

	
	public void setIsValid(boolean isValid) {
		this.isValid = isValid; 
	}

	public void setRespOpcion(String respOpcion) {
		this.respOpcion = respOpcion; 
	}

	public void setListaComboAplicaciones(Vector listaComboAplicaciones) {
		this.listaComboAplicaciones = listaComboAplicaciones; 
	}

	public void setListaEntidades(Vector listaEntidades) {
		this.listaEntidades = listaEntidades; 
	}

	public void setListaUsuarios(Vector listaUsuarios) {
		this.listaUsuarios = listaUsuarios; 
	}

	public void setListaAplicaciones(Vector listaAplicaciones) {
		this.listaAplicaciones = listaAplicaciones; 
	}

	public void setListaEnt(Vector listaEnt) {
		this.listaEnt = listaEnt; 
	}

	public boolean getIsValid() {
		return (this.isValid); 
	}

	public String getRespOpcion() {
		return (this.respOpcion); 
	}

	public Vector getListaComboAplicaciones() {
		return (this.listaComboAplicaciones); 
	}

	public Vector getListaEntidades() {
		return (this.listaEntidades); 
	}

	public Vector getListaUsuarios() {
		return (this.listaUsuarios); 
	}

	public Vector getListaAplicaciones() {
		return (this.listaAplicaciones); 
	}

	public Vector getListaEnt() {
		return (this.listaEnt); 
	}

	
	public void setCodAplicacion(String codAplicacion) {
		this.codAplicacion = codAplicacion; 
	}

	public void setCodOrganizacion(String codOrganizacion) {
		this.codOrganizacion = codOrganizacion; 
	}

	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad; 
	}

	public String getCodAplicacion() {
		return (this.codAplicacion); 
	}

	public String getCodOrganizacion() {
		return (this.codOrganizacion); 
	}

	public String getCodEntidad() {
		return (this.codEntidad); 
	}

	
	public void setNombreOrganizacion(String nombreOrganizacion) {
		this.nombreOrganizacion = nombreOrganizacion; 
	}

	public void setNombreEntidad(String nombreEntidad) {
		this.nombreEntidad = nombreEntidad; 
	}

	public String getNombreOrganizacion() {
		return (this.nombreOrganizacion); 
	}

	public String getNombreEntidad() {
		return (this.nombreEntidad); 
	}

	
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion; 
	}

	public String getAutorizacion() {
		return (this.autorizacion); 
	}

	
	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario; 
	}

	public String getCodUsuario() {
		return (this.codUsuario); 
	}

	
	public void setNombreAplicacion(String nombreAplicacion) {
		this.nombreAplicacion = nombreAplicacion; 
	}

	public String getNombreAplicacion() {
		return (this.nombreAplicacion); 
	}

	
	public void setListaOrganizacionesUsuarios(Vector listaOrganizacionesUsuarios) {
		this.listaOrganizacionesUsuarios = listaOrganizacionesUsuarios; 
	}

	public void setListaEntidadesUsuarios(Vector listaEntidadesUsuarios) {
		this.listaEntidadesUsuarios = listaEntidadesUsuarios; 
	}

	public void setListaUsuariosPrimera(Vector listaUsuariosPrimera) {
		this.listaUsuariosPrimera = listaUsuariosPrimera; 
	}

	public void setListaUsuariosSegunda(Vector listaUsuariosSegunda) {
		this.listaUsuariosSegunda = listaUsuariosSegunda; 
	}

	public void setListaOrganizacionesUsuariosSegunda(Vector listaOrganizacionesUsuariosSegunda) {
		this.listaOrganizacionesUsuariosSegunda = listaOrganizacionesUsuariosSegunda; 
	}

	public void setListaEntidadesUsuariosSegunda(Vector listaEntidadesUsuariosSegunda) {
		this.listaEntidadesUsuariosSegunda = listaEntidadesUsuariosSegunda; 
	}

	public Vector getListaOrganizacionesUsuarios() {
		return (this.listaOrganizacionesUsuarios); 
	}

	public Vector getListaEntidadesUsuarios() {
		return (this.listaEntidadesUsuarios); 
	}

	public Vector getListaUsuariosPrimera() {
		return (this.listaUsuariosPrimera); 
	}

	public Vector getListaUsuariosSegunda() {
		return (this.listaUsuariosSegunda); 
	}

	public Vector getListaOrganizacionesUsuariosSegunda() {
		return (this.listaOrganizacionesUsuariosSegunda); 
	}

	public Vector getListaEntidadesUsuariosSegunda() {
		return (this.listaEntidadesUsuariosSegunda); 
	}

	
	public void setListaAplicacionesPrimera(Vector listaAplicacionesPrimera) {
		this.listaAplicacionesPrimera = listaAplicacionesPrimera; 
	}

	public void setListaAplicacionesUsuariosSegunda(Vector listaAplicacionesUsuariosSegunda) {
		this.listaAplicacionesUsuariosSegunda = listaAplicacionesUsuariosSegunda; 
	}

	public Vector getListaAplicacionesPrimera() {
		return (this.listaAplicacionesPrimera); 
	}

	public Vector getListaAplicacionesUsuariosSegunda() {
		return (this.listaAplicacionesUsuariosSegunda); 
	}

	
	public void setBaseDeDatos(String baseDeDatos) {
		this.baseDeDatos = baseDeDatos; 
	}

	public String getBaseDeDatos() {
		return (this.baseDeDatos); 
	}

    

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
    
    private String respOpcion;
    
    private String codAplicacion;
    private String codOrganizacion;
    private String codEntidad;
    private String nombreOrganizacion;
    private String nombreEntidad;
    private String autorizacion;
    private String codUsuario;
    private String nombreAplicacion;
    private String baseDeDatos;
    
    private Vector listaComboAplicaciones;
    private Vector listaEntidades;
    private Vector listaUsuarios;
    private Vector listaAplicaciones;
    private Vector listaEnt;
    
    private Vector listaOrganizacionesUsuarios;
    private Vector listaEntidadesUsuarios;
    private Vector listaAplicacionesPrimera;
    private Vector listaUsuariosPrimera;
    private Vector listaOrganizacionesUsuariosSegunda;
    private Vector listaEntidadesUsuariosSegunda;
    private Vector listaUsuariosSegunda;
    private Vector listaAplicacionesUsuariosSegunda;
}
