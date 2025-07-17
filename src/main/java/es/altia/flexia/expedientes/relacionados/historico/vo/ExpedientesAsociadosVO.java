package es.altia.flexia.expedientes.relacionados.historico.vo;

/**
 * @author david.caamano
 * 
 * Clase que contendra la informacion necesaria para saber si se permite devolver una 
 * entrada del historico al buzon
 */
public class ExpedientesAsociadosVO {
    
    private Boolean permitimosDesasociar = false;
    private String numExpedientesAsociados;

    public String getNumExpedientesAsociados() {
        return numExpedientesAsociados;
    }
    public void setNumExpedientesAsociados(String numExpedientesAsociados) {
        this.numExpedientesAsociados = numExpedientesAsociados;
    }

    public Boolean getPermitimosDesasociar() {
        return permitimosDesasociar;
    }
    public void setPermitimosDesasociar(Boolean permitimosDesasociar) {
        this.permitimosDesasociar = permitimosDesasociar;
    }

}//ExpedientesAsociadosVO
