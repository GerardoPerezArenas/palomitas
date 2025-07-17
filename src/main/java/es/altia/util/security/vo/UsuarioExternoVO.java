package es.altia.util.security.vo;

import java.util.Calendar;

public class UsuarioExternoVO {
    private Integer id;
    private String nombre;
    private String usuario;
    private String password;
    private Integer estado;
    private Integer intentosFallidos;
    private Calendar fechaAlta;
    private Calendar fechaModificacion;
    private Calendar fechaBaja;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(Integer intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Calendar getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Calendar fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Calendar getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Calendar fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    @Override
    public String toString() {
        return "UsuarioExternoVO{" + "id=" + id + ", nombre=" + nombre + ", usuario=" + usuario + ", password=" + password + ", estado=" + estado + ", intentosFallidos=" + intentosFallidos + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion + ", fechaBaja=" + fechaBaja + '}';
    }

}
