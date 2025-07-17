package es.altia.flexia.tramitacion.externa.plugin.config;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class Configuracion {
    private static Logger log = Logger.getLogger(Configuracion.class);

    /**
     * Recupera el valor de una determinada propiedad de un fichero de configuración
     * @param propiedad: Propiedad
     * @param FILENAME: Nombre del fichero de configuración
     * @return String con el valor de la propiedad o null en caso de que no se haya podido recuperar
     */
    public static String getValor(String propiedad,String FILENAME){
        String valor = null;

        try{
            ResourceBundle bundle = ResourceBundle.getBundle(FILENAME);
            valor = bundle.getString(propiedad);
        }catch(Exception e){
           log.error("No se ha podido recuperar el valor de la propiedad " + propiedad + " por " + e.getMessage());
        }

        return valor;
    }
}
