package es.altia.flexia.expedientes.relacionados.plugin.factoria;

import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionados;
import es.altia.flexia.expedientes.relacionados.plugin.util.ConstantesDatosExpRelacionados;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clase factoría de los plugin de expedientes relacionados de registro
 * @author oscar.rodriguez
 */
public class PluginExpedientesRelacionadosFactoria {

    private static final String PLUGIN_POR_DEFECTO                        = "es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionadosFlexia";
    private static final String URL_PUNTO_ENTRADA_POR_DEFECTO  = "/sge/FichaExpediente.do";

    /**
     * Recupera la instancia del plugin de expedientes relacionados correspondiente.
     * @param codOrganizacion: Código de la organización
     * @return PluginExpedientesRelacionados
     */
    public static PluginExpedientesRelacionados getImplClass(String codOrganizacion)
    {
        PluginExpedientesRelacionados pluginClass = null;
        try{
            ResourceBundle bundleRegistro = ResourceBundle.getBundle("Registro");
            String plugin = bundleRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS);
            String className = bundleRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin +
                    ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_IMPL_CLASS);

            Class clase = Class.forName(className);
            pluginClass = (PluginExpedientesRelacionados)clase.newInstance();
        }catch(Exception e){          
            e.printStackTrace();
            try{
                  // Si no se ha definido ninguna propiedad de configuración del plugin se supone que se utiliza Flexia
                Class clase  =Class.forName(PLUGIN_POR_DEFECTO);
                pluginClass = (PluginExpedientesRelacionados)clase.newInstance();
            }catch(Exception f){
                f.printStackTrace();
            }

        }

        return pluginClass;
    }


  /**
     * Recupera la lista de parámetros que se recuperan de la request.
     * La url se obtiene el fichero de configuración de Registro
     * @param codOrganizacion: Código de la organización
     * @return List que contiene String
     */
    public static List getListadoParametros(String codOrganizacion){
        String[] oParametros = null;
        try{
            ResourceBundle bundleRegistro = ResourceBundle.getBundle("Registro");
            String plugin = bundleRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS);

            String nombrePropiedadParametros = codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA +
                     ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_PARAMETROS + plugin;

            String propiedadParametros = bundleRegistro.getString(nombrePropiedadParametros);
            oParametros = propiedadParametros.split(ConstantesDatosExpRelacionados.COMMA);
        }catch(Exception e){
            e.printStackTrace();
            // Si no se encuentra la lista de parámetros, se carga el plugin de Flexia por defecto, por tanto
            // el funcionamiento es el normal. Se pasa la lista de parámetros necesaria
            oParametros = new String[8];
            oParametros[0] = "codExp";
            oParametros[1] = "opcion";
            oParametros[2] = "modoConsulta";
            oParametros[3] = "desdeAltaRE";
            oParametros[4] = "ejercicio";
            oParametros[5] = "numero";
            oParametros[6] = "codProcedimiento";
            oParametros[7] = "desdeConsulta";
        }
        return java.util.Arrays.asList(oParametros);

    }


    /**
     * Devuelve la URL de la pantalla del punto de entrada en la ficha del expediente que se utiliza para el plugin en cuestión.
     * La url se obtiene el fichero de configuración de Registro
     * @param codOrganizacion: Código de la organización
     * @return Un String
     */
    public static String getURLPantallaPuntoEntrada(String codOrganizacion){
        String url = null;
        try{
            ResourceBundle bundleRegistro = ResourceBundle.getBundle("Registro");
            String plugin = bundleRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS);
            url = bundleRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_PUNTO_ENTRADA + plugin);
            
        }catch(Exception e){
            e.printStackTrace();
            url = URL_PUNTO_ENTRADA_POR_DEFECTO;
        }
        return url;
    }


    /**
     * Comprueba si se ha indicado en el fichero de configuración si hay que recuperar el código de municipio para que forme parte
     * de la url de la ficha del expediente
     * @return Un boolean
     */
    public static boolean recuperarCodigoMunicipioExpediente(String codOrganizacion){
        boolean exito = false;

        try{
            ResourceBundle bundleRegistro = ResourceBundle.getBundle("Registro");
            String plugin = bundleRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS);
            String recuperarCodMunipio = bundleRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA + plugin
                + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_RECUPERAR_MUNICIPIO);
            
            if("SI".equalsIgnoreCase(recuperarCodMunipio))
                exito = true;

        }catch(Exception e){
            e.printStackTrace();
            // Si no se puede recuperar la propiedad correspondiente, entonces se carga el plugin por defecto de Flexia
            // y si es necesario recuperar el código de municipio del expediente
            exito = true;
        }

        return exito;
    }

}