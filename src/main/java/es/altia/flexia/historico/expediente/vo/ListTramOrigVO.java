/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

/**
 *
 * @author santiagoc
 * 
 * Tabla LIST_TRAM_ORIG
 */
public class ListTramOrigVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int ejercicio;//Se corresponde con el campo EJERCICIO
    private String codProcedimiento = null; //Se corresponde con el campo COD_PRO
    private int codMunicipio; //Se corresponde con el campo COD_MUN
    private String numExpediente = null; //Se corresponde con el campo NUM_EXP
    private int codTramiteOrigen; //Se corresponde con el campo COD_TRA_ORIGEN
    private int ocurrenciaTramiteOrigen; //Se corresponde con el campo OCU_TRA_ORIGEN
    private int codTramiteDestino; //Se corresponde con el campo COD_TRA_DESTINO
    private int ocurrenciaTramiteDestino; //Se corresponde con el campo OCU_TRA_DESTINO
    
    public ListTramOrigVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public int getCodTramiteOrigen() {
        return codTramiteOrigen;
    }

    public void setCodTramiteOrigen(int codTramiteOrigen) {
        this.codTramiteOrigen = codTramiteOrigen;
    }

    public int getOcurrenciaTramiteOrigen() {
        return ocurrenciaTramiteOrigen;
    }

    public void setOcurrenciaTramiteOrigen(int ocurrenciaTramiteOrigen) {
        this.ocurrenciaTramiteOrigen = ocurrenciaTramiteOrigen;
    }

    public int getCodTramiteDestino() {
        return codTramiteDestino;
    }

    public void setCodTramiteDestino(int codTramiteDestino) {
        this.codTramiteDestino = codTramiteDestino;
    }

    public int getOcurrenciaTramiteDestino() {
        return ocurrenciaTramiteDestino;
    }

    public void setOcurrenciaTramiteDestino(int ocurrenciaTramiteDestino) {
        this.ocurrenciaTramiteDestino = ocurrenciaTramiteDestino;
    }
}
