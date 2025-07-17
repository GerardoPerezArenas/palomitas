package es.altia.agora.interfaces.user.web.listeners;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationInitContextListener implements ServletContextListener {
    
    protected static Log log = LogFactory.getLog(ApplicationInitContextListener.class.getName());

    public void contextInitialized(ServletContextEvent sce) {
        log.debug("ApplicationInitContextListener: contextInitialized");
        
        Config configVersion = ConfigServiceHelper.getConfig("version");
        ServletContext servletContext = sce.getServletContext();
        String appVersion = configVersion.getString("version.aplicacion");
        
        log.info(String.format("Version de la aplicacion: %s", appVersion));
        
        servletContext.setAttribute("appVersion", appVersion);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("ApplicationInitContextListener: contextDestroyed");
    }
}
