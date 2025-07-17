package es.altia.agora.interfaces.user.web.listeners;

import es.altia.util.commons.JODConverterHelper;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.artofsolving.jodconverter.office.OfficeException;

public class JODConverterInitContextListener implements ServletContextListener {
    
    protected static Log log = LogFactory.getLog(JODConverterInitContextListener.class.getName());
    
    // Mensajes de error
    private static final String MSJ_ERROR_CONECTAR_LIBREOFFICE = "Error al intentar conectarse a LibreOffice/OpenOffice";
    private static final String MSJ_ERROR_DESCONECTAR_LIBREOFFICE = "Error al intentar desconectarse de LibreOffice/OpenOffice. Sera necesario matar el proceso de forma manual.";
    
    public void contextInitialized(ServletContextEvent sce) {
        if (log.isDebugEnabled()) {
            log.debug("JODConverterInitContextListener: contextInitialized");
        
        }
        
        try {
            log.info("Conectando la(s) conexion(es) a LibreOffice/OpenOffice");
            JODConverterHelper.connect();
        } catch (OfficeException oe) {
            log.error(MSJ_ERROR_CONECTAR_LIBREOFFICE, oe);
            throw oe;
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (log.isDebugEnabled()) {
            log.debug("JODConverterInitContextListener: contextDestroyed");
        }
        
        try {
            log.info("Desconectando la(s) conexion(es) a LibreOffice/OpenOffice");
            JODConverterHelper.disconnect();
        } catch (OfficeException oe) {
            log.error(MSJ_ERROR_DESCONECTAR_LIBREOFFICE, oe);
            throw oe;
        }
    }
}
