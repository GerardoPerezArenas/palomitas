package es.altia.agora.interfaces.user.web.registro.notificacion.job.util;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.log4j.Logger;

public class NotificacionJobLanbideUtils {
    private static Logger log = Logger.getLogger(NotificacionJobLanbideUtils.class.getName());
    
    public static String getServer(){
        log.debug("NotificacionJobLanbideUtils.getServer()::BEGIN");
        Config configLanbide = ConfigServiceHelper.getConfig("Lanbide");
        String server = configLanbide.getString("SERVIDOR");
        //log.info("NotificacionJobLanbideUtils.getServer()::END - Server: " + server);
        return server;
    }
}
