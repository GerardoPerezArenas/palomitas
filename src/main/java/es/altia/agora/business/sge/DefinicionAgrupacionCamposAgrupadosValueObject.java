package es.altia.agora.business.sge;

import es.altia.agora.technical.EstructuraCampoAgrupado;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author oscar
 */
public class DefinicionAgrupacionCamposAgrupadosValueObject implements Serializable {
    private String codAgrupacion;
    private String descAgrupacion;
    private Integer ordenAgrupacion;
    private String codProcedimiento;
    private String agrupacionActiva;
    private ArrayList<EstructuraCampoAgrupado> estructura;
       
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

    /**
     * @return the estructura
     */
    public ArrayList<EstructuraCampoAgrupado> getEstructura() {
        return estructura;
    }

    /**
     * @param estructura the estructura to set
     */
    public void setEstructura(ArrayList<EstructuraCampoAgrupado> estructura) {
        this.estructura = estructura;
    }

   
}
