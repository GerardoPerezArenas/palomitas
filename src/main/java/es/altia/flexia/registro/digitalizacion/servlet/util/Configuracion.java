package es.altia.flexia.registro.digitalizacion.servlet.util;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

public class Configuracion {
    private static Config properties = ConfigServiceHelper.getConfig("WSFlexia");

    public static String organizacion ="";
    
    public static String getEsquemaGenerico(String organizacion) {
          String[]  paramsBD = getParamsBD(organizacion);
          String esquemaGenerico=null;

        if (paramsBD[0].toUpperCase().equalsIgnoreCase("ORACLE")) {
       	    esquemaGenerico = paramsBD[5] + ".";
        } else if (paramsBD[0].toUpperCase().equalsIgnoreCase("SQLSERVER")) {
       	    esquemaGenerico = paramsBD[5] + ".dbo.";
        }
        return esquemaGenerico;
    }

    public static String[] getParamsBD(String organizacion) {
        
        String[]  paramsBD = new String[7];
        paramsBD[0] = properties.getString(organizacion+"/BBDD/gestor");
        paramsBD[6] = properties.getString(organizacion+"/BBDD/jndi");
       

        return paramsBD;
    }
}
