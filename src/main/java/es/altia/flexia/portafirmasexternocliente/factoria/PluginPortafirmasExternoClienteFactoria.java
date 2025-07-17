package es.altia.flexia.portafirmasexternocliente.factoria;

import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;
import es.altia.flexia.portafirmasexternocliente.plugin.PluginPortafirmasExternoCliente;
import es.altia.flexia.portafirmasexternocliente.util.ConstantesPortafirmasExternoCliente;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import es.altia.flexia.portafirmasexternocliente.plugin.service.PluginPortafirmasExternoService;

/**
 * @author david.caamano
 * @version 04/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 04/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class PluginPortafirmasExternoClienteFactoria {
    
    //Logger
    private static Logger log = Logger.getLogger(PluginPortafirmasExternoClienteFactoria.class);
    
    /**
     * Devuelve una implementacion del cliente de portafirmas externo
     * 
     * @param codOrganizacion
     * @return PluginPortafirmasCliente
     */
    public static PluginPortafirmasExternoCliente getImplClass(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getImplClass() : BEGIN");
        PluginPortafirmasExternoCliente pluginPortafirmasCliente = null;
        try{
            ResourceBundle bundlePortafirmas = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
            String plugin = bundlePortafirmas.getString(codOrganizacion + ConstantesPortafirmasExternoCliente.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE);
            if(log.isDebugEnabled()) log.debug("plugin = " + plugin);
            String className = bundlePortafirmas.getString(ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE + 
                        ConstantesPortafirmasExternoCliente.BARRA + plugin 
                        + ConstantesPortafirmasExternoCliente.CLASE_IMPL_PORTAFIRMAS_EXTERNO_CLIENTE);
            if(log.isDebugEnabled()) log.debug("className = " + className);
            Class clase = Class.forName(className);
            pluginPortafirmasCliente = (PluginPortafirmasExternoCliente) clase.newInstance();
        }catch(Exception e){ 
            log.error("Se ha producido un error instanciando el plugin del cliente de portafirmas "  + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getImplClass() : END");
        return pluginPortafirmasCliente;
    }//getImplClass
    
    /**
     * Devuelve una implementacion del servicio de envio al portafirmas de lanbide
     * 
     * @param codOrganizacion
     * @return PluginPortafirmasExternoService
     */
    public static PluginPortafirmasExternoService getPortafirmaExternoServImplClass(){
        if(log.isDebugEnabled()) log.debug("getServImplClass() : BEGIN");
        PluginPortafirmasExternoService pluginPortafirmasLanbideService = null;
        try{
             ResourceBundle bundlePortafirmas = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
            String servicioLanbide = ConstantesPortafirmasExternoCliente.PLUGIN_LANBIDE_SERVICIO;
            if(log.isDebugEnabled()) log.debug("la propieedad servicioLanbide  = " + servicioLanbide);
            String className = bundlePortafirmas.getString(servicioLanbide);
            if(log.isDebugEnabled()) log.debug("className Servicio = " + className);
            Class clase = Class.forName(className);
            pluginPortafirmasLanbideService = (PluginPortafirmasExternoService) clase.newInstance();
        }catch(Exception e){ 
            log.error("Se ha producido un error instanciando el plugin del servicio del cliente de portafirmas "  + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getServImplClass() : END");
        return pluginPortafirmasLanbideService;
    }//getPortafirmaExternoServImplClass
    
    /**
     * Devuelve un Boolean que indica si hay definido un cliente externo para el portafirmas
     * 
     * @param codOrganizacion
     * @return Boolean
     */
    public static Boolean getExistePortafirmasExterno (String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getExistePortafirmasExterno() : BEGIN");
        Boolean existePortafirmasExterno = false;
        try{
            ResourceBundle bundlePortafirmas = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
            String pluginPortafirmasExterno = bundlePortafirmas.getString(codOrganizacion + ConstantesPortafirmas.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO);
            if(pluginPortafirmasExterno != null && "S".equalsIgnoreCase(pluginPortafirmasExterno)){
                if(log.isDebugEnabled()) log.debug("Existe portafirmas externo");
                existePortafirmasExterno = true;
            }//if(pluginPortafirmasExterno != null && "S".equalsIgnoreCase(pluginPortafirmasExterno))
        }catch(Exception e){ 
            log.error("Se ha producido un error comprobando si existe un portafirmas externo "  + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getExistePortafirmasExterno() : END");
        return existePortafirmasExterno;
    }//getExistePortafirmasExterno
    
    /**
    * Recupera el codigo del cliente del portafirmas externo
    * 
    * @param codOrganizacion
    * @return String
    */
    public static String getCodClientePortafirmasExterno (String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getCodClientePortafirmasExterno() : BEGIN");
        String codClientePortafirmasExterno = new String("");
        try{
            ResourceBundle bundlePortafirmas = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
            String propCodClientePortafirmasExterno = bundlePortafirmas.getString(codOrganizacion + ConstantesPortafirmas.BARRA + 
                    ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE);
            if(propCodClientePortafirmasExterno != null && !"".equalsIgnoreCase(propCodClientePortafirmasExterno)){
                if(log.isDebugEnabled()) log.debug("propCodClientePortafirmasExterno = " + propCodClientePortafirmasExterno);
                codClientePortafirmasExterno = propCodClientePortafirmasExterno;
            }//if(propCodClientePortafirmasExterno != null && !"".equalsIgnoreCase(propCodClientePortafirmasExterno))
        }catch(Exception e){ 
            log.error("Se ha producido un error recuperando el codigo del cliente del portafirmas externo "  + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCodClientePortafirmasExterno() : END");
        return codClientePortafirmasExterno;
    }//getCodClientePortafirmasExterno
    
}//class