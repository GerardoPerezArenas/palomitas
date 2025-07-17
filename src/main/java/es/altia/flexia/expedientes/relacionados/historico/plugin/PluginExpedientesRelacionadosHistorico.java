/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.expedientes.relacionados.historico.plugin;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.flexia.expedientes.relacionados.historico.util.ResultadoPluginExpRelHist;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesAsociadosVO;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesRelacionadosHistoricoVO;
import java.util.ArrayList;

/**
 *
 * @author david.vidal
 */
public abstract class PluginExpedientesRelacionadosHistorico {
    
    private String descripcion;
    private String codigo;
    private String implClass;

    public abstract ResultadoPluginExpRelHist recuperarPermisosDeshacerRelacion(ExpedientesRelacionadosHistoricoVO registro);
  
    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the implClass
     */
    public String getImplClass() {
        return implClass;
    }

    /**
     * @param implClass the implClass to set
     */
    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }
     
     
}
