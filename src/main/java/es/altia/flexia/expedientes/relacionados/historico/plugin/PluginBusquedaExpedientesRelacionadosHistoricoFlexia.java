package es.altia.flexia.expedientes.relacionados.historico.plugin;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.flexia.expedientes.relacionados.historico.util.ConstantesExpedientesRelacionadosHistorico;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesAsociadosVO;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesRelacionadosHistoricoVO;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * @author david.caamano
 * 
 * Clase de plugin para la busqueda y gestion de la desasociacion de expedientes relacionados en el historico de entradas de registro
 * para el cliente generico de Flexia.
 * 
 */
public class PluginBusquedaExpedientesRelacionadosHistoricoFlexia extends PluginBusquedaExpedientesRelacionadosHistorico {
    
    //Logger
    private static Logger log = Logger.getLogger(PluginBusquedaExpedientesRelacionadosHistoricoFlexia.class);
    
     //Fichero de configuracion
    private static ResourceBundle bundleRegistro = ResourceBundle.getBundle("Registro");
    
    //Constantes
    private static String BLOQUEAR_PANTALLA = "BLOQUEAR_PANTALLA_DESASOCIAR";
    
    /**
     * Este metodo indicara si debemos bloquear la posibilidad de deseleccionar expedientes de la lista de expedientes relacionados
     * de una anotacion al devolverla al buzon desde el historico.
     * 
     * Recuperara la informacion de la propiedad [cod_organizacion]/BLOQUEAR_PANTALLA_DESASOCIAR del fichero Registro.properties
     * 
     * @param codOrganizacion
     * @return Boolean
     */
    @Override
    public Boolean bloquearPantalla(String codOrganizacion) {
        if(log.isDebugEnabled()) log.debug("bloquearPantalla() : BEGIN");
        Boolean bloquearPantalla = false;
        try{
            String valor = bundleRegistro.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA + BLOQUEAR_PANTALLA);
            if("SI".equalsIgnoreCase(valor)){
                bloquearPantalla = true;
            }//if("SI".equalsIgnoreCase(valor))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la propiedad que indica si debemos de bloquear la pantalla de expedientes asociados"
                    + "por defecto devolvera el valor false " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("bloquearPantalla = " + String.valueOf(bloquearPantalla));
        if(log.isDebugEnabled()) log.debug("bloquearPantalla() : END");
        return bloquearPantalla;
    }//bloquearPantalla

    /**
     * Este metodo devolvera encapsulado en el objeto ExpedientesAsociadosVO la información que indica si permitimos desasociar los
     * expedientes asociados a una entrada de registro que se encuentre en el historico y el numero de expedientes asociados de la
     * entrada.
     * 
     * Para este cliente permitiremos desasociar los expedientes indiferentemente del estado en que se encuentren por lo que devolveremos
     * siempre true.
     * 
     * El numero de expedientes asociados devueltos no tendra en cuenta ningun estado en concreto por lo que devolveremos el numero
     * total de expedientes asociados.
     * 
     * @param departamento
     * @param ejercicio
     * @param tipo_reg
     * @param params
     * @return ExpedientesAsociadosVO
     * @throws TramitacionException 
     */
    @Override
    public ExpedientesAsociadosVO getComprobarDesasociacion(String uni_registro, String ejercicio, String tipo_reg,
        String[] params) throws TramitacionException{
        if(log.isDebugEnabled()) log.debug("getNumeroExpedientesAsociados() : BEGIN");
        ArrayList<ExpedientesRelacionadosHistoricoVO> expedientes = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        ExpedientesAsociadosVO expedientesAsociados = new ExpedientesAsociadosVO();
        try{
            //Recuperamos la lista de expedientes con su estado
            //Para el cliente altia obviamos el estado del expediente y devolvemos el numero total de expedientes y el campo
            //permitimosDesasociar siempre a TRUE
            expedientes = TramitacionManager.getInstance().getExpedientesAsociados(uni_registro, ejercicio, tipo_reg, params);
            
            expedientesAsociados.setPermitimosDesasociar(true);
            expedientesAsociados.setNumExpedientesAsociados(String.valueOf(expedientes.size()));
        }catch(TramitacionException trEx){
            log.error("Se ha producido un error recuperando el numero de expedientes asociados " + trEx.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getNumeroExpedientesAsociados() : END");
        return expedientesAsociados;
    }//getNumeroExpedientesAsociados

    
    @Override 
    public ExpedientesAsociadosVO getComprobarDesasociacion(String departamento, String ejercicio, String tipo_reg,String uni_registro,
        String[] params) throws TramitacionException{
        if(log.isDebugEnabled()) log.debug("getNumeroExpedientesAsociados() : BEGIN");
        ArrayList<ExpedientesRelacionadosHistoricoVO> expedientes = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        ExpedientesAsociadosVO expedientesAsociados = new ExpedientesAsociadosVO();
        try{
            //Recuperamos la lista de expedientes con su estado
            //Para el cliente altia obviamos el estado del expediente y devolvemos el numero total de expedientes y el campo
            //permitimosDesasociar siempre a TRUE
            expedientes = TramitacionManager.getInstance().getExpedientesAsociados(departamento, ejercicio, tipo_reg,uni_registro, params);
            
            expedientesAsociados.setPermitimosDesasociar(true);
            expedientesAsociados.setNumExpedientesAsociados(String.valueOf(expedientes.size()));
        }catch(TramitacionException trEx){
            log.error("Se ha producido un error recuperando el numero de expedientes asociados " + trEx.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getNumeroExpedientesAsociados() : END");
        return expedientesAsociados;
    }//getNumeroExpedientesAsociados

    /**
     * Este metodo devolvera la lista de numeros de expediente asociados a la entrada de registro.
     * 
     * Para este cliente devolveremos la lista total de expedientes asociados sin tener en cuenta el estado del expediente.
     * 
     * @param departamento
     * @param ejercicio
     * @param tipo_reg
     * @param params
     * @return ArrayList<String>
     * @throws TramitacionException 
     */
    @Override
    public ArrayList<String> listaExpedientes(String uni_registro, String ejercicio, String tipo_reg, String[] params)
        throws TramitacionException{
        if(log.isDebugEnabled()) log.debug("listaExpedientes() : BEGIN");
        ArrayList<ExpedientesRelacionadosHistoricoVO> expedientes = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        ArrayList<String> listaExpedientes = new ArrayList<String>();
        try{
            expedientes = TramitacionManager.getInstance().getExpedientesAsociados(uni_registro, ejercicio, tipo_reg, params);
            for(ExpedientesRelacionadosHistoricoVO expRel : expedientes){
                listaExpedientes.add(expRel.getExpediente());
            }//for(ExpedientesRelacionadosHistoricoVO expRel : expedientes)
        }catch(TramitacionException trEx){
            log.error("Se ha producido un error recuperando los expedientes asociados " + trEx.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("listaExpedientes() : END");
        return listaExpedientes;
    }//listaExpedientes
    
   
    /**
     * Este metodo devolvera la lista de numeros de expediente asociados a la entrada de registro.
     * 
     * Para este cliente devolveremos la lista total de expedientes asociados sin tener en cuenta el estado del expediente.
     * 
     * @param departamento
     * @param ejercicio
     * @param tipo_reg
     * @param params
     * @return ArrayList<String>
     * @throws TramitacionException 
     */
    @Override
    public ArrayList<String> listaExpedientes(String departamento, String ejercicio, String tipo_reg, String uni_registro,String[] params)
        throws TramitacionException{
        if(log.isDebugEnabled()) log.debug("listaExpedientes() : BEGIN");
        ArrayList<ExpedientesRelacionadosHistoricoVO> expedientes = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        ArrayList<String> listaExpedientes = new ArrayList<String>(); 
        try{
            expedientes = TramitacionManager.getInstance().getExpedientesAsociados(departamento, ejercicio, tipo_reg,uni_registro, params);
            for(ExpedientesRelacionadosHistoricoVO expRel : expedientes){
                listaExpedientes.add(expRel.getExpediente());
            }//for(ExpedientesRelacionadosHistoricoVO expRel : expedientes)
        }catch(TramitacionException trEx){
            log.error("Se ha producido un error recuperando los expedientes asociados " + trEx.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("listaExpedientes() : END");
        return listaExpedientes;
    }//listaExpedientes
   
}//class
