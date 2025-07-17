/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.util.processor;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.xmldata.LicenseFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.exceptions.LoadLicenseException;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;

/**
 *
 * @author Óscar Rodríguez Brea
 */
public class ControllerPrincipal extends ActionServlet {

    private Logger log = Logger.getLogger(ControllerPrincipal.class);
    private Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    private Config m_License = ConfigServiceHelper.getConfig("license");
    private final String CADUCA_SESION = m_ConfigTechnical.getString("URL.caducaSesion");
    private final String LOGIN_JSP = "/jsp/escritorio/login.jsp";
    private final String BARRA_INVERTIDA = "/"; 

    protected void process(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        // Identify the path component we will use to select a mapping.
        
        String rutaFicheroLicencias = getServletContext().getRealPath("/") + m_License.getString("filename_license");
        String rutaFicheroEsquema = getServletContext().getRealPath("/") + m_License.getString("filename_xsd");
        log.debug("rutaFicheroLicencias original: " + rutaFicheroLicencias);
        log.debug("rutaFicheroEsquema original: " + rutaFicheroEsquema);
        log.debug("ConstantesDatos.APPLICATION_LICENSE: " + ConstantesDatos.APPLICATION_LICENSE);
        
        if(!getServletContext().getRealPath("/").endsWith(File.separator))
        {
            log.debug("ControllerPrincipal getServletContext().getRealPath('/') no termina /");            
            log.debug("");
            rutaFicheroLicencias = getServletContext().getRealPath("/") + BARRA_INVERTIDA +  m_License.getString("filename_license");
            rutaFicheroEsquema   = getServletContext().getRealPath("/") + BARRA_INVERTIDA +  m_License.getString("filename_xsd");
            log.debug("rutaFicheroLicencias modificado: " + rutaFicheroLicencias);
            log.debug("rutaFicheroEsquema modificado: " + rutaFicheroEsquema);
        }else log.debug("ControllerPrincipal serveltContext.getPath termina en " + File.separator);                          

        LicenseFactory factory = (LicenseFactory) getServletContext().getAttribute(ConstantesDatos.APPLICATION_LICENSE);

        if (factory == null) {
            log.debug("No existe la factoria en context");
            
            factory = LicenseFactory.getInstance();
            try {
                // Se cargan los datos de xml en la caché de la factoría de licencias
                factory.loadLicenses(rutaFicheroLicencias, rutaFicheroEsquema);
                log.debug("Después de cargar licencias");

                if (factory != null) {
                    this.getServletContext().setAttribute(ConstantesDatos.APPLICATION_LICENSE, factory);

                    log.debug("cargadas licencias en context");
                    log.debug("redirigiendo a " + request.getContextPath() + LOGIN_JSP);
                    //response.sendRedirect(request.getContextPath() + LOGIN_JSP);
                    super.process(request, response);
                }
            }
            catch(LoadLicenseException e){
                log.error("STOP: LoadLicenseException: No existe el archivo de licencias");                         
                response.sendRedirect(request.getContextPath() + CADUCA_SESION);
            }
        } else {

            super.process(request, response);
        }
    }
}
