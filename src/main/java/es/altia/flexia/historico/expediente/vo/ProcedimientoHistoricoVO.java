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
 * Tabla PROCEDIMIENTO_HISTORICO
 */
public class ProcedimientoHistoricoVO
{
    private String codProcedimiento = null; //Representa el c�digo del procedimiento.
    private int meses; //Representa el n�mero de meses
    private int codOrganizacion; //Representa el c�digo de organizaci�n
    
    public ProcedimientoHistoricoVO()
    {
        
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getMeses() {
        return meses;
    }

    public void setMeses(int meses) {
        this.meses = meses;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }
}
