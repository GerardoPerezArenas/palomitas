/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

import java.text.SimpleDateFormat;

/**
 * Visualizacion datos en pantalla Unidad destino de salidas SIR
 * @author INGDGC
 */
public class SirDestinoRResDto extends SirDestinoRRes{
    
    private static final SimpleDateFormat formatDateddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    
    private String nombreUnidad;

    public SirDestinoRResDto() {
    }

    public SirDestinoRResDto(int RES_DEP, int RES_UOR, String RES_TIP, int RES_EJE, int RES_NUM) {
        super(RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM);
    }

    public SirDestinoRResDto(SirDestinoRRes sirDestinoRRes, String nombreUnidad) {
        super(sirDestinoRRes.getRES_DEP(),sirDestinoRRes.getRES_UOR(), sirDestinoRRes.getRES_TIP(), sirDestinoRRes.getRES_EJE(), sirDestinoRRes.getRES_NUM(),sirDestinoRRes.getCodigoUnidad(),sirDestinoRRes.getOficinaRegistroSIR(),sirDestinoRRes.getNumeroRegistroSIR(),sirDestinoRRes.getUsuarioRegistroSIR(),sirDestinoRRes.getFechaRegistroSIR(),sirDestinoRRes.getFechaRegistroSistemaSIR(),sirDestinoRRes.getFechaRegistro());
        this.nombreUnidad=nombreUnidad;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }
    
    @Override
    public String toString() {
        return "SirDestinoRResDto{" + ""
                + "SirDestinoRRes=" + super.toString()
                + "nombreUnidad=" + nombreUnidad + '}';
    }
        
}
