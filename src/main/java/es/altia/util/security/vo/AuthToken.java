package es.altia.util.security.vo;

import java.util.Calendar;

public class AuthToken {
    private Long id;
    private String token;
    private Calendar fechaCaducidad;
    private Integer idUsuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Calendar getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Calendar fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "AuthToken{" + "id=" + id + ", token=" + token + ", fechaCaducidad=" + fechaCaducidad + ", idUsuario=" + idUsuario + '}';
    }

}
