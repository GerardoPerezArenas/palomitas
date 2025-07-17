/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.administracion;

/**
 *
 * @author adrian
 */
public class ParametrosBDVO {
    
    int codOrganizacion;
    int codEntidad;
    int codAplicacion;
    String gestor;
    String driver;
    String url;
    String usuario;
    String password;
    String fichlog;
    String jndi;

    public ParametrosBDVO(){ }

    public ParametrosBDVO(int codOrganizacion,int codEntidad,int codAplicacion,String gestor,String driver,
            String url,String usuario,String password,String fichlog,String jndi){ 
        this.codOrganizacion = codOrganizacion;
        this.codEntidad = codEntidad;
        this.codAplicacion = codAplicacion;
        this.gestor = gestor;
        this.driver = driver;
        this.url = url;
        this.usuario = usuario;
        this.password = password;
        this.fichlog = fichlog;
        this.jndi = jndi;
    }
    
    public ParametrosBDVO(ParametrosBDVO params){ 
        this.codOrganizacion = params.getCodOrganizacion();
        this.codEntidad = params.getCodEntidad();
        this.codAplicacion = params.getCodAplicacion();
        this.gestor = params.getGestor();
        this.driver = params.getDriver();
        this.url = params.getUrl();
        this.usuario = params.getUsuario();
        this.password = params.getPassword();
        this.fichlog = params.getFichlog();
        this.jndi = params.getJndi();
    }
    
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(int codEntidad) {
        this.codEntidad = codEntidad;
    }

    public int getCodAplicacion() {
        return codAplicacion;
    }

    public void setCodAplicacion(int codAplicacion) {
        this.codAplicacion = codAplicacion;
    }

    public String getGestor() {
        return gestor;
    }

    public void setGestor(String gestor) {
        this.gestor = gestor;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFichlog() {
        return fichlog;
    }

    public void setFichlog(String fichlog) {
        this.fichlog = fichlog;
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }
    
    
}
