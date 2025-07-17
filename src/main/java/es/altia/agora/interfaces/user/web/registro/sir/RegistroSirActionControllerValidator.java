/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.registro.sir;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author INGDGC
 */
public class RegistroSirActionControllerValidator {
    
    private UsuarioValueObject usuarioValueObject;
    private HttpSession session;

    public RegistroSirActionControllerValidator(HttpServletRequest request) {
        if(request!=null)
            session=request.getSession();
        if(session!=null && session.getAttribute("usuario")!=null)
            usuarioValueObject = (UsuarioValueObject)session.getAttribute("usuario");            
    }

    public UsuarioValueObject getUsuarioValueObject() {
        return usuarioValueObject;
    }

    public void setUsuarioValueObject(UsuarioValueObject usuarioValueObject) {
        this.usuarioValueObject = usuarioValueObject;
    }

    public boolean existeSession() {
        return session!=null;
    }

    public boolean existeUsuarioSession() {
        return usuarioValueObject!=null;
    }
    
    public boolean existeDatosSessionUsuario(){
        return existeSession() && existeUsuarioSession();
    }

    @Override
    public String toString() {
        return "RegistroSirActionControllerValidator{" + "usuarioValueObject=" + new Gson().toJson(usuarioValueObject) + ", session=" + new Gson().toJson(session) + '}';
    }
    
    
    
}
