/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.plugin;

import java.sql.Connection;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.util.ArrayList;

import es.altia.flexia.notificacion.persistence.*;
import es.altia.flexia.notificacion.vo.*;

import org.apache.log4j.Logger;


public class PluginListadoNotificacionPlatea implements PluginListadoNotificacion{

    protected static Config conf =
            ConfigServiceHelper.getConfig("notificaciones");
    
    private Logger log = Logger.getLogger(PluginListadoNotificacionPlatea.class);

    private String codOrganizacion;
   

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

   

    public ArrayList<NotificacionVO> getNotificacionesExpediente(int codOrganizacion, int idioma, String numExpediente, String expHistorico, Connection con) throws TechnicalException {
        log.debug("getNotificacionesExpediente():BEGIN");
        
        ArrayList<NotificacionVO> notificaciones = new ArrayList();
        try{
            notificaciones = NotificacionDAO.getInstance().getNotificacionesExpediente(codOrganizacion,idioma,numExpediente,expHistorico,con, false);
        }catch (Exception e) {
            e.printStackTrace();
	}finally{
            return notificaciones;
	}
    }






}
