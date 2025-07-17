package es.altia.agora.business.terceros;

import es.altia.flexia.terceros.integracion.externa.vo.ErrorSistemaTerceroExternoVO;
import java.util.ArrayList;
import java.util.Hashtable;


public class SalidaTratamientoTerceroExterno {
    
    private int idTercero;
    private String  verTercero;

    /**
     * Tabla Hash que contiene un código de error y para cada uno de ellos la lista
     * de sistemas de terceros que han producido dicho error.
     * Códigos de error:
     *       1: Error al instanciar una clase del servicio
     *       2: Error durante la ejecución del servicio
     *       3: Error campos obligatorios sin cubrir
     *       4: Restricciones que no se cumplen durante el alta del tercero
     *       5: Restricciones que no se cumplen durante la modificación del tercero
     */
    private Hashtable<Integer,ArrayList<ErrorSistemaTerceroExternoVO>> erroresEjecucionServicio = null;

    /**
     * @return the idTercero
     */
    public int getIdTercero() {
        return idTercero;
    }

    /**
     * @param idTercero the idTercero to set
     */
    public void setIdTercero(int idTercero) {
        this.idTercero = idTercero;
    }

    public String  getVerTercero() {
        return verTercero;
    }

    public void setVerTercero(String verTercero) {
        this.verTercero = verTercero;
    }
    
    

    /**
     * @return the erroresEjecucionServicio
     */
    public Hashtable<Integer, ArrayList<ErrorSistemaTerceroExternoVO>> getErroresEjecucionServicio() {
        return erroresEjecucionServicio;
    }
    
    /**
     * @param erroresEjecucionServicio the erroresEjecucionServicio to set
     */
    public void setErroresEjecucionServicio(Hashtable<Integer, ArrayList<ErrorSistemaTerceroExternoVO>> erroresEjecucionServicio) {
        this.erroresEjecucionServicio = erroresEjecucionServicio;
    }
}