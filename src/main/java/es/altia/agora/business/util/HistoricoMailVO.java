package es.altia.agora.business.util;

import java.util.List;

public class HistoricoMailVO {
    Integer razon;
	List<String> para;
	List<String> cc;
	List<String> cco;
	String asunto;
	String contenido;
	List<String> ficherosAdjuntos;

    public HistoricoMailVO() {
    }
    
    public HistoricoMailVO(Integer razon, List<String> para, List<String> cc, List<String> cco, String asunto, String contenido, List<String> ficherosAdjuntos) {
        this.razon = razon;
        this.para = para;
        this.cc = cc;
        this.cco = cco;
        this.asunto = asunto;
        this.contenido = contenido;
        this.ficherosAdjuntos = ficherosAdjuntos;
    }

    public Integer getRazon() {
        return razon;
    }

    public void setRazon(Integer razon) {
        this.razon = razon;
    }

    public List<String> getPara() {
        return para;
    }

    public void setPara(List<String> para) {
        this.para = para;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getCco() {
        return cco;
    }

    public void setCco(List<String> cco) {
        this.cco = cco;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<String> getFicherosAdjuntos() {
        return ficherosAdjuntos;
    }

    public void setFicherosAdjuntos(List<String> ficherosAdjuntos) {
        this.ficherosAdjuntos = ficherosAdjuntos;
    }
    
}
