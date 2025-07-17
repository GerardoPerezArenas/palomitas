/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.cache;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author adrian
 */
public class CargaCacheServlet extends HttpServlet {

    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

    protected static Log log = LogFactory.getLog(CargaCacheServlet.class.getName());

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        CacheDatosFactoria.getImplOrganizaciones();
        CacheDatosFactoria.getImplEntidades();
        CacheDatosFactoria.getImplParametrosBD();
        CacheDatosFactoria.getImplUnidadesOrganicas();
        log.debug("cargando cache mediante CargaCacheServlet.............................");
    }
}