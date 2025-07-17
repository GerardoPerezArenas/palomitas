/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.expedientes.relacionados.historico.plugin;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.flexia.expedientes.relacionados.historico.util.ResultadoPluginExpRelHist;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesAsociadosVO;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesRelacionadosHistoricoVO;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author david.vidal
 */
public class PluginExpedientesRelacionadosHistoricoFlexia extends PluginExpedientesRelacionadosHistorico {

    //Logger
    private static Logger log = Logger.getLogger(PluginExpedientesRelacionadosHistoricoFlexia.class);
    
    @Override
    public ResultadoPluginExpRelHist recuperarPermisosDeshacerRelacion(ExpedientesRelacionadosHistoricoVO registro) {    
        if(log.isDebugEnabled()) log.debug("recuperarPermisosDeshacerRelacion() : BEGIN");
        ResultadoPluginExpRelHist devolucion = new ResultadoPluginExpRelHist();
        devolucion.setStatus(0);
        if(log.isDebugEnabled()) log.debug("recuperarPermisosDeshacerRelacion() : END");
        return devolucion;
    }//recuperarPermisosDeshacerRelacion

}//class
