/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.notificaciones;


import es.altia.technical.ValueObject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import java.util.*;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.notificaciones.TipoNotificacionValueObject;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class NotificacionesUtil {

      private static final Log _log =
            LogFactory.getLog(NotificacionesUtil.class.getName());

    public NotificacionesUtil() {
    } 

    public static ArrayList<TipoNotificacionValueObject> getTiposNotificacion()
    {
        ArrayList<TipoNotificacionValueObject> lista=new ArrayList();
        
            
            Config notificaciones = ConfigServiceHelper.getConfig("notificaciones");
            String codigos = notificaciones.getString("codigos_notificaciones_flexia");
            String descripciones = notificaciones.getString("descripcion_notificaciones_flexia");
         
         
            StringTokenizer tokenCodigos = new StringTokenizer(codigos, ";");
            StringTokenizer tokenDescripciones = new StringTokenizer(descripciones, ";");
          
            
            
         
             while(tokenCodigos.hasMoreTokens()){
                 TipoNotificacionValueObject tipoNotificacionesVO=new TipoNotificacionValueObject();
                 String codNot=tokenCodigos.nextToken();
                 String descNot=tokenDescripciones.nextToken();
                 tipoNotificacionesVO.setCodigo(codNot);
                 tipoNotificacionesVO.setDescripcion(descNot);
                 
                 lista.add(tipoNotificacionesVO);
                 
             }
            
            return lista;
        
        


    }

}
