/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.plugin;


import java.util.Vector;


import es.altia.common.exception.TechnicalException;


import es.altia.flexia.notificacion.vo.*;
import java.sql.Connection;
import java.util.ArrayList;

public interface PluginListadoNotificacion {
    
    public void setCodOrganizacion(String codOrganizacion) throws TechnicalException ;
    public ArrayList<NotificacionVO> getNotificacionesExpediente(int codOrganizacion,int idioma,String numExpediente,String expHistorico,Connection con) throws TechnicalException ;


}
