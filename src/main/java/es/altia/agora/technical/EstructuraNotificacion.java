package es.altia.agora.technical;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: susana.rodriguez
 * Date: 25-abr-2006
 * Time: 19:57:13
 * To change this template use File | Settings | File Templates.
 */
public class EstructuraNotificacion implements Serializable {
    private String nombreTramite;
    private String plazoTramite;
    private String unidadPlazo;
    private Vector listaEMailsUOR;
    private Vector listaEMailsUsusUOR;
    private Vector listaEMailsInteresados;
    private String listaEmailsUsuInicioTramite;
    private String listaEmailsUsuInicioExped;

    public String getNombreTramite() {
        return nombreTramite;
    }

    public Vector getListaEMailsUOR() {
        return listaEMailsUOR;
    }

    public Vector getListaEMailsUsusUOR() {
        return listaEMailsUsusUOR;
    }

    public Vector getListaEMailsInteresados() {
        return listaEMailsInteresados;
    }

    public void setNombreTramite(String nombreTramite) {
        this.nombreTramite = nombreTramite;
    }

    public void setListaEMailsUOR(Vector listaEMailsUOR) {
        this.listaEMailsUOR = listaEMailsUOR; 
    }

    public void setListaEMailsUsusUOR(Vector listaEMailsUsusUOR) {
        this.listaEMailsUsusUOR = listaEMailsUsusUOR;
    }

    public void setListaEMailsInteresados(Vector listaEMailsInteresados) {
        this.listaEMailsInteresados = listaEMailsInteresados;
    }

    public String getListaEmailsUsuInicioTramite() {
        return listaEmailsUsuInicioTramite;
    }

    public void setListaEmailsUsuInicioTramite(String listaEmailsUsuInicioTramite) {
        this.listaEmailsUsuInicioTramite = listaEmailsUsuInicioTramite;
    }

    public String getListaEmailsUsuInicioExped() {
        return listaEmailsUsuInicioExped;
    }

    public void setListaEmailsUsuInicioExped(String listaEmailsUsuInicioExped) {
        this.listaEmailsUsuInicioExped = listaEmailsUsuInicioExped;
    }
    
    public String getPlazoTramite() {
        return plazoTramite;
    }

    public void setPlazoTramite(String plazoTramite) {
        this.plazoTramite = plazoTramite;
    }

    public String getUnidadPlazo() {
        return unidadPlazo;
    }

    public void setUnidadPlazo(String unidadPlazo) {
        this.unidadPlazo = unidadPlazo;
    }
    
    
    

    public String toString() {
        return "Nombre de tramite: " + nombreTramite + "\n" + "Lista de mails UOR: " + listaEMailsUOR + "\n" +
               "Lista de mails Usuarios UOR: " + listaEMailsUsusUOR + "\n" + "Lista de mails Interesados: " +
               listaEMailsInteresados + "\n";
    }
}
