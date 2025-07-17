/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.expedientes.relacionados.historico.factoria;


import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.flexia.expedientes.relacionados.historico.plugin.PluginBusquedaExpedientesRelacionadosHistorico;
import es.altia.flexia.expedientes.relacionados.historico.plugin.PluginExpedientesRelacionadosHistorico;
import es.altia.flexia.expedientes.relacionados.historico.util.ConstantesExpedientesRelacionadosHistorico;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author david.vidal
 */
public class PluginExpedientesRelacionadosHistoricoFactoria {
    
    //Logger
    private static Logger log = Logger.getLogger(PluginExpedientesRelacionadosHistoricoFactoria.class);
    
    /**
     * Metodo que devuelve la implementacion de la clase de plugin de expedientes relacionados historicos
     * 
     * @param codOrganizacion
     * @param procedimiento
     * @param params
     * @return 
     */
    public static PluginExpedientesRelacionadosHistorico  getImplClass(String codOrganizacion, String procedimiento, String[] params){
        if(log.isDebugEnabled()) log.debug("getImplClass() : BEGIN");
        PluginExpedientesRelacionadosHistorico pluginCliente = null;
        try{
            //llamada al dao para recuperar la clase que implementa                                   
            String className = TramitacionManager.getInstance().getClasePluginExpedRelacHistorico(procedimiento, params);            
            ResourceBundle bundleExpediente = ResourceBundle.getBundle("BuzonEntradaHistorico");  
            //en caso de que el resultado sea nulo se recupera la clase generica
            if(className == null || className == ""){
              className = bundleExpediente.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                               ConstantesExpedientesRelacionadosHistorico.PLUGIN_GENERICO +                                
                               ConstantesExpedientesRelacionadosHistorico.CLASE_HISTORICO_IMP);   
            }
            Class clase = Class.forName(className);
            pluginCliente = (PluginExpedientesRelacionadosHistorico) clase.newInstance();
        }catch(Exception e){ 
            log.error("Se ha producido un error instanciando el plugin del cliente de expedientes relacionados en historico"  + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getImplClass() : END");
        return pluginCliente;
    }//getImplClass
     

    /**
     * Metodo que devuelve las informacion (Descripcion, Codigo y clase) de las implementaciones disponibles del plugin
     * 
     * @param codOrganizacion
     * @return 
     */
    public static ArrayList<PluginExpedientesRelacionadosHistorico> getPluginExpedientesRelacionadosHistorico(String codOrganizacion){
        ArrayList<PluginExpedientesRelacionadosHistorico> salida = new ArrayList<PluginExpedientesRelacionadosHistorico>();
        if(log.isDebugEnabled()) log.debug("getPluginExpedientesRelacionadosHistorico() : BEGIN");
        if(!"".equals(codOrganizacion)){

            ResourceBundle config = ResourceBundle.getBundle("BuzonEntradaHistorico");
            String valor = config.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                            ConstantesExpedientesRelacionadosHistorico.GESTOR + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                            ConstantesExpedientesRelacionadosHistorico.PLUGIN_LISTA_OPERADORES);            
            String[] servicios = valor.split(";");
            if(servicios!=null && servicios.length>=1){                 
                for(int i=0;i<servicios.length;i++){
                    String servicio = servicios[i];
                    if(servicio!=null && !"".equals(servicio)){
                        String descripcion = config.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                                servicio + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                                ConstantesExpedientesRelacionadosHistorico.PLUGIN_DESCRIP);
                        String codigo = config.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                                servicio + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                                ConstantesExpedientesRelacionadosHistorico.PLUGIN_COD);
                        String implClass = config.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                                servicio + ConstantesExpedientesRelacionadosHistorico.BARRA + 
                                ConstantesExpedientesRelacionadosHistorico.PLUGIN_CLASE);
                        try{
                            if(descripcion!=null && !"".equals(descripcion) && codigo!=null && !"".equals(codigo) && implClass!=null && !"".equals(implClass)){
                                 Class clase = Class.forName(implClass);
                                 PluginExpedientesRelacionadosHistorico pluginCliente = (PluginExpedientesRelacionadosHistorico) clase.newInstance();
                                 pluginCliente.setDescripcion(descripcion);
                                 pluginCliente.setCodigo(codigo);
                                 pluginCliente.setImplClass(implClass);
                                 salida.add(pluginCliente);
                            }
                        }catch(Exception e){
                            log.error("Error al cargar el servicio " + servicio + " en recuperación de anotaciones del histórico del buzón de entrada: " + e.getMessage());
                        }//try-catch
                    }//if(servicio!=null && !"".equals(servicio))
                }//for(int i=0;i<servicios.length;i++)
            }//if(servicios!=null && servicios.length>=1)
        }//if(!"".equals(codOrganizacion))
        if(log.isDebugEnabled()) log.debug("getPluginExpedientesRelacionadosHistorico() : END");
        return salida;        
    }//getPluginExpedientesRelacionadosHistorico
    
    /**
     * Devuelve una implementacion de la clase de busqueda de expedientes relacionados en el historico
     * 
     * @param codOrganizacion
     * @return 
     */
    public static PluginBusquedaExpedientesRelacionadosHistorico getPluginBusquedaExpedientesRelacionadosHistorico(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getPluginBusquedaExpedientesRelacionadosHistorico() : BEGIN");
        PluginBusquedaExpedientesRelacionadosHistorico claseBusqueda = null;
        try{
            ResourceBundle config = ResourceBundle.getBundle("BuzonEntradaHistorico");
            String className  = config.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA +
                    ConstantesExpedientesRelacionadosHistorico.PLUGIN_BUSQUEDA);
            
            try{
                Class clase = Class.forName(className);
                claseBusqueda = (PluginBusquedaExpedientesRelacionadosHistorico) clase.newInstance();
            }catch(ClassNotFoundException cnfe){
               log.error("No se encuentra la clase = " + cnfe.getMessage()); 
            }//
        }catch(Exception e){ 
            log.error("Se ha producido un error instanciando el plugin de busqueda "
                    + "del cliente de expedientes relacionados en historico"  + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getPluginBusquedaExpedientesRelacionadosHistorico() : END");
        return claseBusqueda;
    }//getPluginBusquedaExpedientesRelacionadosHistorico
    
}//class
