package es.altia.agora.business.registro;

public class AnotacionRegistroVO {

    Integer codDepartamento;
    Integer uor;
    String tipo;
    Integer ejercicio;
    Integer numero;

    public Integer getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(Integer codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public Integer getUor() {
        return uor;
    }

    public void setUor(Integer uor) {
        this.uor = uor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

}
