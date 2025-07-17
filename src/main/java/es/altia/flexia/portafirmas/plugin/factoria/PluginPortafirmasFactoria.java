package es.altia.flexia.portafirmas.plugin.factoria;

import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;
import java.util.ResourceBundle;


public class PluginPortafirmasFactoria {
    
    
    public static PluginPortafirmas getImplClass(String codOrganizacion)
    {
        PluginPortafirmas pluginClass = null;
        try{
            ResourceBundle bundleRegistro = ResourceBundle.getBundle(ConstantesPortafirmas.FICHERO_PROPERTIES);
            String plugin = bundleRegistro.getString(codOrganizacion + ConstantesPortafirmas.BARRA +
                    ConstantesPortafirmas.PLUGIN_PORTAFIRMAS);
            String className = bundleRegistro.getString(ConstantesPortafirmas.PLUGIN_PORTAFIRMAS + 
                    ConstantesPortafirmas.BARRA + plugin + ConstantesPortafirmas.CLASE_IMPL_PORTAFIRMAS);

            Class clase = Class.forName(className);
            pluginClass = (PluginPortafirmas)clase.newInstance();
        }catch(Exception e){          
            e.printStackTrace();
            try{
                  // Si no se ha definido ninguna propiedad de configuración del plugin se supone que se utiliza Flexia
                Class clase  =Class.forName(ConstantesPortafirmas.CLASE_PLUGIN_DEFECTO);
                pluginClass = (PluginPortafirmas)clase.newInstance();
            }catch(Exception f){
                f.printStackTrace();
            }

        }

        return pluginClass;
    }
    
    
}
