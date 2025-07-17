/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.agora.business.registro.sir.vo;

import es.lanbide.lan6.adaptadoresPlatea.sir.beans.Lan6SalidaRegistroSIRRespuesta;

/**
 * Objeto con codigo y descripcion de la respuesta de envio Salida al sistema SIR - Lanbide
 * @author INGDGC
 */
public class SirRegistroSalidaResponse {
    
    /**
     * codigo respuesta : Menor 0 = Excepciones;0=OK ; Mayor 0 = Errores Validacion o controlados
     */
    private int codigoIntRespuesta;
    private String codEstadoRespGestionSIR;
    private String descEstadoRespGestionSIR;
    private Lan6SalidaRegistroSIRRespuesta lan6SalidaRegistroSIRRespuesta;

    public SirRegistroSalidaResponse() {
    }

    public SirRegistroSalidaResponse(int codigoIntRespuesta,String codEstadoRespGestionSIR, String descEstadoRespGestionSIR) {
        this.codigoIntRespuesta = codigoIntRespuesta;
        this.codEstadoRespGestionSIR = codEstadoRespGestionSIR;
        this.descEstadoRespGestionSIR = descEstadoRespGestionSIR;
    }

    public SirRegistroSalidaResponse(int codigoIntRespuesta,String codEstadoRespGestionSIR, String descEstadoRespGestionSIR, Lan6SalidaRegistroSIRRespuesta lan6SalidaRegistroSIRRespuesta) {
        this.codigoIntRespuesta = codigoIntRespuesta;
        this.codEstadoRespGestionSIR = codEstadoRespGestionSIR;
        this.descEstadoRespGestionSIR = descEstadoRespGestionSIR;
        this.lan6SalidaRegistroSIRRespuesta = lan6SalidaRegistroSIRRespuesta;
    }

    public int getCodigoIntRespuesta() {
        return codigoIntRespuesta;
    }

    /**
     * set codigo respuesta : Menor 0 = Excepciones;0=OK ; Mayor 0 = Errores Validacion o controlados
     * @param codigoIntRespuesta 
     */
    public void setCodigoIntRespuesta(int codigoIntRespuesta) {
        this.codigoIntRespuesta = codigoIntRespuesta;
    }

    /**
     * get codigo respuesta : Menor 0 = Excepciones;0=OK ; Mayor 0 = Errores Validacion o controlados
     * @return 
     */
    public String getCodEstadoRespGestionSIR() {
        return codEstadoRespGestionSIR;
    }

    public void setCodEstadoRespGestionSIR(String codEstadoRespGestionSIR) {
        this.codEstadoRespGestionSIR = codEstadoRespGestionSIR;
    }

    public String getDescEstadoRespGestionSIR() {
        return descEstadoRespGestionSIR;
    }

    public void setDescEstadoRespGestionSIR(String descEstadoRespGestionSIR) {
        this.descEstadoRespGestionSIR = descEstadoRespGestionSIR;
    }

    public Lan6SalidaRegistroSIRRespuesta getLan6SalidaRegistroSIRRespuesta() {
        return lan6SalidaRegistroSIRRespuesta;
    }

    public void setLan6SalidaRegistroSIRRespuesta(Lan6SalidaRegistroSIRRespuesta lan6SalidaRegistroSIRRespuesta) {
        this.lan6SalidaRegistroSIRRespuesta = lan6SalidaRegistroSIRRespuesta;
    }            
}
