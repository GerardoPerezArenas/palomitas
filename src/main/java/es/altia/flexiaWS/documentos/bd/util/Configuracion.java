package es.altia.flexiaWS.documentos.bd.util;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.ResourceBundle;

public class Configuracion {

    //private static ResourceBundle properties = ResourceBundle.getBundle("WSFlexia");
     private static Config properties = ConfigServiceHelper.getConfig("WSFlexia");

    public static String organizacion ="";
    public static String entidad ="";

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

    public static String getModuloAlta() {
         String moduloAlta = properties.getString("ModuloAlta");
        return moduloAlta;
    }
    
    public static String getEntidad() {
         String entidad = properties.getString(organizacion+"/entidad");
        return entidad;
    }
    
    public static String getEntidad(String org) {
         String entidad = properties.getString(org+"/entidad");
        return entidad;
    }

    public static String[] getParamsBD(String organizacion) {
        
        String[]  paramsBD = new String[7];
        paramsBD[0] = properties.getString(organizacion+"/BBDD/gestor");
        paramsBD[6] = properties.getString(organizacion+"/BBDD/jndi");
       

        return paramsBD;
    }

    public static String getUsuarioAlta() {
         String usuarioAlta;
         usuarioAlta = properties.getString("UsuarioAlta");
        return usuarioAlta;
    }

    public static String getUrlTercero() {
        String urlTercero=properties.getString("URL/Tercero");
        return urlTercero;
    }
    
    public static String getUrlFlexia() {
        String urlFlexia=properties.getString("urlFlexia");
        return urlFlexia;
    }

    public static String getAplicacion(){
         String apli=properties.getString("aplicacion");
         return apli;
    
    }
    
    public static void setOrganizacion(String org){
        organizacion = org;
    }

    public static String getCodigoIdioma(String codOrganizacion){
        String codIdioma = properties.getString(codOrganizacion + "/idioma_codigo");
        return codIdioma;
    }
    
    public static String getUsoToken(){
         String usoToken="NO";
        try{
            usoToken = properties.getString("usarTokenParaCSV");
        }
        catch (Exception e){
            return "NO";
        }
        return usoToken;
    }
}
