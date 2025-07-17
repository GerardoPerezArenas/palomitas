package es.altia.agora.business.sge;

/**
 * @author david.caamano
 * @version 14/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 14/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class DefinicionAgrupacionCamposValueObject {
    
    private String codAgrupacion;
    private String descAgrupacion;
    private Integer ordenAgrupacion;
    private String codProcedimiento;
    private String agrupacionActiva;

    public String getCodAgrupacion() {
        return codAgrupacion;
    }
    public void setCodAgrupacion(String codAgrupacion) {
        this.codAgrupacion = codAgrupacion;
    }

    public String getDescAgrupacion() {
        return descAgrupacion;
    }
    public void setDescAgrupacion(String descAgrupacion) {
        this.descAgrupacion = descAgrupacion;
    }

    public Integer getOrdenAgrupacion() {
        return ordenAgrupacion;
    }
    public void setOrdenAgrupacion(Integer ordenAgrupacion) {
        this.ordenAgrupacion = ordenAgrupacion;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getAgrupacionActiva() {
        return agrupacionActiva;
    }
    public void setAgrupacionActiva(String agrupacionActiva) {
        this.agrupacionActiva = agrupacionActiva;
    }
    
}//class

