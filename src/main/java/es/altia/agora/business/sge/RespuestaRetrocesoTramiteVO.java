package es.altia.agora.business.sge;

import java.util.ArrayList;

/**
 *
 * @author oscar.rodriguez
 */
public class RespuestaRetrocesoTramiteVO {
 
    // Código trámite
    private int codTramite;
    // Ocurrencia trámite
    private int ocurrenciaTramite;
    // Trámites abierto por el trámite anterior
    private ArrayList<TramitacionExpedientesValueObject> tramitesDestino = null;
    // Trámite pendiente o finalizado
    private boolean expedienteFinalizado;
    // Trámite pendiente o finalizado
    private boolean tramiteFinalizado;
    // Tipo de respuesta. Puede tomar diferentes valores: expedienteFinalizado, tramiteFinalizaConTramitesAbiertos
    private String tipoRespuesta;

    private boolean reabrirTramiteOrigen = false;
    private int codTramiteOrigenReabrir=-1;
    private int ocurrenciaTramiteOrigenReabrir=-1;
    private String nomTramOrigenReabrir;
    private String fecIniTramOrigenReabrir;
    
    /**
     * @return the ocurrenciaTramite
     */
    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    /**
     * @param ocurrenciaTramite the ocurrenciaTramite to set
     */
    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    /**
     * @return the codTramite
     */
    public int getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the tramitesDestino
     */
    public ArrayList<TramitacionExpedientesValueObject> getTramitesDestino() {
        return tramitesDestino;
    }

    /**
     * @param tramitesDestino the tramitesDestino to set
     */
    public void setTramitesDestino(ArrayList<TramitacionExpedientesValueObject> tramitesDestino) {
        this.tramitesDestino = tramitesDestino;
    }

    

    /**
     * @return the tipoRespuesta
     */
    public String getTipoRespuesta() {
        return tipoRespuesta;
    }

    /**
     * @param tipoRespuesta the tipoRespuesta to set
     */
    public void setTipoRespuesta(String tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }

    /**
     * @return the expedienteFinalizado
     */
    public boolean isExpedienteFinalizado() {
        return expedienteFinalizado;
    }

    /**
     * @param expedienteFinalizado the expedienteFinalizado to set
     */
    public void setExpedienteFinalizado(boolean expedienteFinalizado) {
        this.expedienteFinalizado = expedienteFinalizado;
    }

    /**
     * @return the tramiteFinalizado
     */
    public boolean isTramiteFinalizado() {
        return tramiteFinalizado;
    }

    /**
     * @param tramiteFinalizado the tramiteFinalizado to set
     */
    public void setTramiteFinalizado(boolean tramiteFinalizado) {
        this.tramiteFinalizado = tramiteFinalizado;
    }

    /**
     * @return the reabrirTramiteOrigen
     */
    public boolean isReabrirTramiteOrigen() {
        return reabrirTramiteOrigen;
    }

    /**
     * @param reabrirTramiteOrigen the reabrirTramiteOrigen to set
     */
    public void setReabrirTramiteOrigen(boolean reabrirTramiteOrigen) {
        this.reabrirTramiteOrigen = reabrirTramiteOrigen;
    }

    /**
     * @return the codTramiteOrigenReabrir
     */
    public int getCodTramiteOrigenReabrir() {
        return codTramiteOrigenReabrir;
    }

    /**
     * @param codTramiteOrigenReabrir the codTramiteOrigenReabrir to set
     */
    public void setCodTramiteOrigenReabrir(int codTramiteOrigenReabrir) {
        this.codTramiteOrigenReabrir = codTramiteOrigenReabrir;
    }

    /**
     * @return the ocurrenciaTramiteOrigenReabrir
     */
    public int getOcurrenciaTramiteOrigenReabrir() {
        return ocurrenciaTramiteOrigenReabrir;
    }

    /**
     * @param ocurrenciaTramiteOrigenReabrir the ocurrenciaTramiteOrigenReabrir to set
     */
    public void setOcurrenciaTramiteOrigenReabrir(int ocurrenciaTramiteOrigenReabrir) {
        this.ocurrenciaTramiteOrigenReabrir = ocurrenciaTramiteOrigenReabrir;
    }

    public String getNomTramiteOrigenReabrir() {
        return nomTramOrigenReabrir;
    }

    public void setNomTramiteOrigenReabrir(String nomTramOrigenReabrir) {
        this.nomTramOrigenReabrir = nomTramOrigenReabrir;
    }

    public String getFecIniTramiteOrigenReabrir() {
        return fecIniTramOrigenReabrir;
    }

    public void setFecIniTramiteOrigenReabrir(String fecIniTramOrigenReabrir) {
        this.fecIniTramOrigenReabrir = fecIniTramOrigenReabrir;
    }

}