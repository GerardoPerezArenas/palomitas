/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.plugin;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.CriticalException;

import es.altia.common.exception.TechnicalException;
import es.altia.util.exceptions.InternalErrorException;
import java.sql.SQLException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FactoriaPluginNotificacion {


    protected static Config confServBusqueda =
            ConfigServiceHelper.getConfig("notificaciones");
    protected static Log log = LogFactory.getLog(FactoriaPluginNotificacion.class.getName());



     private FactoriaPluginNotificacion() {}

    public static Class getImplClass(String codOrganizacion) throws Exception {

        Class theClass=null;

        try {

            String plugin=confServBusqueda.getString(codOrganizacion +"/plugin");
            String implClassName = confServBusqueda.getString(codOrganizacion +"/Notificacion/"+plugin+"/implClass");
            String url=confServBusqueda.getString(codOrganizacion +"/Notificacion/"+plugin+"/urlPaginaDatosNotificacion");
            theClass = Class.forName(implClassName);

        } catch (Exception cnfe) {
            log.error("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION");
            cnfe.printStackTrace();
           
        }

        return theClass;

    }
    
      public static PluginNotificacion getImpl(String codOrganizacion) throws Exception {

        try {

            PluginNotificacion implClass =
                    (PluginNotificacion) getImplClass(codOrganizacion).newInstance();
            implClass.setCodOrganizacion(codOrganizacion);

            return implClass;

        } catch (InstantiationException ie) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO");
            ie.printStackTrace();
            return null;

        } catch (IllegalAccessException iae) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE");
            iae.printStackTrace();
            return null;
        }
    }
    
      public static Class getImplClassListaNotif(String codOrganizacion) throws Exception {
           Class theClass=null;

        try {

            String plugin=confServBusqueda.getString(codOrganizacion +"/plugin");
            String implClassName = confServBusqueda.getString(codOrganizacion +"/Notificacion/"+plugin+"/implClassListadoNotif");
            theClass = Class.forName(implClassName);

        } catch (Exception cnfe) {
            log.error("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION");
            cnfe.printStackTrace();
           
        }

        return theClass;
          
      }
      
      public static PluginListadoNotificacion getImplListaNotif(String codOrganizacion) throws Exception {

        try {
           
            PluginListadoNotificacion implClass =
                    (PluginListadoNotificacion) getImplClassListaNotif(codOrganizacion).newInstance();
            implClass.setCodOrganizacion(codOrganizacion);

            return implClass;
            

        } catch (InstantiationException ie) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO");
            ie.printStackTrace();
            return null;

        } catch (IllegalAccessException iae) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE");
            iae.printStackTrace();
            return null;
        }
    }


}
