package es.altia.agora.business.usuariosforms;

import java.io.Serializable;
import java.util.Vector;

import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

public class UsuariosFormsValueObject implements Serializable, ValueObject {
	
	public boolean isValid;
	private String login;
	private String password; //Determinar si se necesitará un password2 para verificaciones
	private String password2;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String nif;
	private String email;
	private String codDepartamento; //UOR a la que pertenece
	private String codPerfil; //Cargo que ostenta
	private String codCatProfesional; //Codigo de categoria profesional
	private String codEntidad; //Entidad de la empresa a la que pertenece
	////private String borrar;
	private String bloqueado;
	
    private String paginaListado;
    private String numLineasPaginaListado;
    
    private String descDepartamento;
    private String descPerfil;
    private String nomEntidad;
    private String descCatProfesional;
    
    private Vector listaUORs;
    private Vector listaPerfiles;
    private Vector listaCatProfesionales;
    private Vector listaTodosUsuarios;
    
    private String respOpcion;
    
	
    public UsuariosFormsValueObject() {
        super();
    }
    
	public String getLogin(){
		return login;
	} 
	
	public void setLogin (String login){
		this.login = login;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword2 (String password2){
		this.password2 = password2;
	}
	
	public String getPassword2(){
		return password2;
	}
	
	public void setPassword (String password){
		this.password = password;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public void setNombre (String nombre){
		this.nombre = nombre;
	}
	
	public String getApellido1(){
		return apellido1;
	}
	
	public void setApellido1 (String apellido1){
		this.apellido1 = apellido1;
	}
	
	public String getApellido2(){
		return apellido2;
	}
	
	public void setApellido2 (String apellido2){
		this.apellido2 = apellido2;
	}
	
	public String getNif(){
		return nif;
	}
	
	public void setNif (String nif){
		this.nif = nif;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail (String email){
		this.email = email;
	}
	
	public String getCodDepartamento(){
		return codDepartamento;
	}
	
	public void setCodDepartamento (String codDepartamento){
		this.codDepartamento = codDepartamento;
	}
	
	public String getCodPerfil(){
		return codPerfil;
	}
	
	public void setCodPerfil (String codPerfil){
		this.codPerfil = codPerfil;
	}
	
	public String getCodCatProfesional(){
		return codCatProfesional;
	}
	
	public void setCodCatProfesional (String codCatProfesional){
		this.codCatProfesional = codCatProfesional;
	}
	
	public String getCodEntidad(){
		return codEntidad;
	}
	
	public void setCodEntidad(String codEntidad){
		this.codEntidad = codEntidad;
	}
	/*
	public String getBorrar(){
		return borrar;
	}
	
	public void setBorrar(String borrar){
		this.borrar = borrar;
	}
	*/
	public String getBloqueado(){
		return bloqueado;
	}
	
	public void setBloqueado (String bloqueado){
		this.bloqueado = bloqueado;
	}
	
	public void setPaginaListado(String paginaListado) {
		this.paginaListado = paginaListado; 
	}

	public void setNumLineasPaginaListado(String numLineasPaginaListado) {
		this.numLineasPaginaListado = numLineasPaginaListado; 
	}

	public boolean getIsValid() {
		return (this.isValid); 
	}

	public String getPaginaListado() {
		return (this.paginaListado); 
	}

	public String getNumLineasPaginaListado() {
		return (this.numLineasPaginaListado); 
	}
	
	public String getDescDepartamento(){
		return descDepartamento;
	}
	
	public void setDescDepartamento (String nomDepartamento){
		this.descDepartamento = nomDepartamento;
	}
	
	public String getDescPerfil(){
		return descPerfil;
	}
	
	public void setDescPerfil (String nomPerfil){
		this.descPerfil = nomPerfil;
	}
	
	public String getNomEntidad(){
		return nomEntidad;
	}
	
	public void setNomEntidad (String nomEntidad){
		this.nomEntidad = nomEntidad;
	}
	
	public Vector getListaTodosUsuarios() {
		return (this.listaTodosUsuarios); 
	}

	public void setListaTodosUsuarios(Vector listaTodosUsuarios) {
		this.listaTodosUsuarios = listaTodosUsuarios; 
	}
	
	public void setListaUors (Vector listaUORs){
		this.listaUORs = listaUORs;
	}
	
	public Vector getListaUORs(){
		return this.listaUORs;
	}
	
	public void setListaPerfiles(Vector listaPerfiles){
		this.listaPerfiles = listaPerfiles;
	}
	
	public Vector getListaPerfiles(){
		return this.listaPerfiles;
	}
	
    public void setDescCatProfesional (String descCatProfesional){
    	this.descCatProfesional = descCatProfesional;
    }
    
    public String getDescCatProfesional(){
    	return this.descCatProfesional;
    }
    
    public void setListaCatProfesionales (Vector listaCatProfesionales){
    	this.listaCatProfesionales = listaCatProfesionales;
    }
    
    public Vector getListaCatProfesionales(){
    	return this.listaCatProfesionales;
    }
    
	public void setRespOpcion(String respOpcion) {
		this.respOpcion = respOpcion; 
	}

	public String getRespOpcion() {
		return (this.respOpcion); 
	}
	
	public void validate (String idioma) throws ValidationException {
		boolean correcto = true;
		Messages errors = new Messages ();
		if (!errors.empty()) throw new ValidationException (errors);
		isValid = true;
	}
	
	
}