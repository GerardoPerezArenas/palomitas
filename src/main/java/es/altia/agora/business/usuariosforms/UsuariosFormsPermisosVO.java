/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.usuariosforms;

import java.io.Serializable;

/**
 *
 * @author manuel.bahamonde
 */
public class UsuariosFormsPermisosVO implements Serializable {

    private String loginUsuarioPermiso;         // login del usuario del catalogo
    private String codUnidadOrganicaPermiso;    // Codigo interno de la unidad organica asociada al usuario del catalogo
    private String nombreUnidadOrganicaPermiso; // Nombre de la uniddad organica asociada al usuario del catalogo
    private String codVisibleUnidadOrganicaPermiso; // Codigo visible de la unidad organica
    private String codCargoPermiso;             // Codigo interno del cargo asociado al usuario del catalogo
    private String nombreCargoPermiso;          // Nombre del cargo asociado al usuario del catalogo
    private String codVisibleCargoPermiso;      //  codigo visible del cargo
    

    public UsuariosFormsPermisosVO() {
    }



    public String getCodCargoPermiso() {
        return codCargoPermiso;
    }

    public void setCodCargoPermiso(String codCargoPermiso) {
        this.codCargoPermiso = codCargoPermiso;
    }

    public String getCodUnidadOrganicaPermiso() {
        return codUnidadOrganicaPermiso;
    }

    public void setCodUnidadOrganicaPermiso(String codUnidadOrganicaPermiso) {
        this.codUnidadOrganicaPermiso = codUnidadOrganicaPermiso;
    }

    public String getLoginUsuarioPermiso() {
        return loginUsuarioPermiso;
    }

    public void setLoginUsuarioPermiso(String loginUsuarioPermiso) {
        this.loginUsuarioPermiso = loginUsuarioPermiso;
    }

    public String getNombreCargoPermiso() {
        return nombreCargoPermiso;
    }

    public void setNombreCargoPermiso(String nombreCargoPermiso) {
        this.nombreCargoPermiso = nombreCargoPermiso;
    }

    public String getNombreUnidadOrganicaPermiso() {
        return nombreUnidadOrganicaPermiso;
    }

    public void setNombreUnidadOrganicaPermiso(String nombreUnidadOrganicaPermiso) {
        this.nombreUnidadOrganicaPermiso = nombreUnidadOrganicaPermiso;
    }

    public String getCodVisibleCargoPermiso() {
        return codVisibleCargoPermiso;
    }

    public void setCodVisibleCargoPermiso(String codVisibleCargoPermiso) {
        this.codVisibleCargoPermiso = codVisibleCargoPermiso;
    }

    public String getCodVisibleUnidadOrganicaPermiso() {
        return codVisibleUnidadOrganicaPermiso;
    }

    public void setCodVisibleUnidadOrganicaPermiso(String codVisibleUnidadOrganicaPermiso) {
        this.codVisibleUnidadOrganicaPermiso = codVisibleUnidadOrganicaPermiso;
    }



    public String toString() {
        return "UsuariosFormsPermisosVO{"
                + " loginUsuarioPermiso='" + loginUsuarioPermiso + "|"
                + " codUnidadOrganicaPermiso='" + codUnidadOrganicaPermiso + "|"
                + "  codCargoPermiso='" + codCargoPermiso + "|"
                + "  nombreCargoPermiso='" + nombreCargoPermiso + "|"
                + "  nombreUnidadOrganicaPermiso='" + nombreUnidadOrganicaPermiso + "|"
                + "  codVisibleCargoPermiso='" + codVisibleCargoPermiso + "|"
                + "  codVisibleCargoPermiso='" + codVisibleCargoPermiso + "}";
    }


}
