package es.altia.agora.interfaces.user.web.usuariosforms;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import es.altia.agora.business.usuariosforms.UsuariosFormsValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

public class UsuariosFormsForm extends ActionForm {


    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(UsuariosFormsForm.class.getName());

    UsuariosFormsValueObject ufVO = new UsuariosFormsValueObject();
    Vector listaUsuariosFormsPermisos = new Vector();
    String codUOR ="";
    String descUOR ="";
    String codCargo = "";
    String descCargo = "";


    public UsuariosFormsValueObject getUsuariosForms(){
    	return ufVO;
    }
    
    public void setUsuariosForms (UsuariosFormsValueObject ufVO){
    	this.ufVO = ufVO;
    }
    
	public String getLogin(){
		return ufVO.getLogin();
	} 
	
	public void setLogin (String login){
		ufVO.setLogin(login);
	}
	
	public String getPassword(){
		return ufVO.getPassword();
	}
	
	public void setPassword (String password){
		ufVO.setPassword(password);
	}
	
	public String getPassword2(){
		return ufVO.getPassword2();
	}
	
	public void setPassword2 (String password2){
		ufVO.setPassword2(password2);
	}
	
	public String getNombre(){
		return ufVO.getNombre();
	}
	
	public void setNombre (String nombre){
		ufVO.setNombre(nombre);
	}
	
	public String getApellido1(){
		return ufVO.getApellido1();
	}
	
	public void setApellido1 (String apellido1){
		ufVO.setApellido1(apellido1);
	}
	
	public String getApellido2(){
		return ufVO.getApellido2();
	}
	
	public void setapellido2 (String apellido2){
		ufVO.setApellido2(apellido2);
	}
	
	public String getNif(){
		return ufVO.getNif();
	}
	
	public void setNif (String nif){
		ufVO.setNif(nif);
	}
	
	public String getEmail(){
		return ufVO.getEmail();
	}
	
	public void setEmail (String email){
		ufVO.setEmail(email);
	}
	
	public String getCodDepartamento(){
		return ufVO.getCodDepartamento();
	}
	
	public void setCodDepartamento (String codDepartamento){
		ufVO.setCodDepartamento(codDepartamento);
	}
	
	public String getCodPerfil(){
		return ufVO.getCodPerfil();
	}
	
	public void setCodPerfil (String codPerfil){
		ufVO.setCodPerfil(codPerfil);
	}
	
	public String getCodCatProfesional(){
		return ufVO.getCodCatProfesional();
	}
	
	public void setCodCatProfesional (String codCatProfesional){
		ufVO.setCodCatProfesional(codCatProfesional);
	}
	
	public String getCodEntidad(){
		return ufVO.getCodEntidad();
	}
	
	public void setCodEntidad(String codEntidad){
		ufVO.setCodEntidad(codEntidad);
	}
	/*
	public String getBorrar(){
		return ufVO.getBorrar();
	}
	
	public void setBorrar(String borrar){
		ufVO.setBorrar(borrar);
	}
	*/
	public String getBloqueado(){
		return ufVO.getBloqueado();
	}
	
	public void setBloqueado (String bloqueado){
		ufVO.setBloqueado(bloqueado);
	}
	
	public void setDescDepartamento (String nomDepartamento){
		ufVO.setDescDepartamento(nomDepartamento);
	}
	
	public String getDescDepartamento(){
		return ufVO.getDescDepartamento();
	}
	
	public void setDescPerfil (String nomPerfil){
		ufVO.setDescPerfil(nomPerfil);
	}
	
	public String getDescPerfil(){
		return ufVO.getDescPerfil();
	}
	
	public void setNomEntidad (String nomEntidad){
		ufVO.setNomEntidad(nomEntidad);
	}
	
	public String getNomEntidad(){
		return ufVO.getNomEntidad();
	}
	
	public void setListaTodosUsuarios(Vector listaTodosUsuarios){
		ufVO.setListaTodosUsuarios(listaTodosUsuarios);
	}
	
	public Vector getlistaTodosUsuarios(){
		return ufVO.getListaTodosUsuarios();
	}
	
	public void setPaginaListado (String paginaListado){
		ufVO.setPaginaListado(paginaListado);
	}
	
	public String getPaginaListado(){
		return ufVO.getPaginaListado();
	}
	
	public void setNumLineasPaginaListado (String numLineasPaginaListado){
		ufVO.setNumLineasPaginaListado(numLineasPaginaListado);
	}
	
	public String getNumLineasPaginaListado(){
		return ufVO.getNumLineasPaginaListado();
	}
	
	public void setListaUORs (Vector listaUORs){
		ufVO.setListaUors(listaUORs);
	}
	
	public Vector getListaUORs(){
		return ufVO.getListaUORs();
	}
	
	public void setListaPerfiles (Vector listaPerfiles){
		ufVO.setListaPerfiles(listaPerfiles);
	}
	
	public Vector getListaPerfiles(){
		return ufVO.getListaPerfiles();
	}
	
	public void setDescCatProfesional (String descCatProfesional){
		ufVO.setDescCatProfesional(descCatProfesional);
	}
	
	public String getDescCatProfesional(){
		return ufVO.getDescCatProfesional();
	}
	
	public void setListaCatProfesionales (Vector listaCatProfesionales){
		ufVO.setListaCatProfesionales(listaCatProfesionales);
	}
	
	public Vector getListaCatProfesionales(){
		return ufVO.getListaCatProfesionales();
	}
	
	public void setListaUors (Vector listaUors){
		ufVO.setListaUors(listaUors);
	}
	
	public Vector getListaUors(){
		return ufVO.getListaUORs();
	}
	
	public void setRespOpcion(String respOpcion) {
		ufVO.setRespOpcion(respOpcion); 
	}

	public String getRespOpcion() {
		return ufVO.getRespOpcion(); 
	}

    public String getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getCodUOR() {
        return codUOR;
    }

    public void setCodUOR(String codUOR) {
        this.codUOR = codUOR;
    }

    public String getDescUOR() {
        return descUOR;
    }

    public void setDescUOR(String descUOR) {
        this.descUOR = descUOR;
    }

    public String getDescCargo() {
        return descCargo;
    }

    public void setDescCargo(String descCargo) {
        this.descCargo = descCargo;
    }

    public Vector getListaUsuariosFormsPermisos() {
        return listaUsuariosFormsPermisos;
    }

    public void setListaUsuariosFormsPermisos(Vector listaUsuariosFormsPermisos) {
        this.listaUsuariosFormsPermisos = listaUsuariosFormsPermisos;
    }

   


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
    	m_Log.debug("validate");
    	ActionErrors errors = new ActionErrors();

    	try {
    		ufVO.validate(idioma);
    	} catch (ValidationException ve) {
    		//Hay errores...
    		//Tenemos que traducirlos a formato struts
    		errors=validationException(ve,errors);
    	}
    	return errors;
    }

    // Función que procesa los errores de validación a formato struts 
    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
      Iterator iter = ve.getMessages().get();
      while (iter.hasNext()) {
        Message message = (Message)iter.next();
        errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
      }
      return errors;
    }

}
