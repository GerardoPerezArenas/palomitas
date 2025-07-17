/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.sge.notificacion.job;

import es.altia.agora.interfaces.user.web.sge.notificacion.facade.NotificacionPlazoTramiteManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author mjesus.lopez
 *
 * ejecuta automaticamente le envio de
 * notificaciones de tramites
 */
public class NotificacionPlazoJob implements Job{

     private static Log m_log = LogFactory.getLog(NotificacionPlazoJob.class);
     //Para el fichero	de configuracion tecnico.
     protected static Config m_ConfigTechnical;
     protected static String gestorD;

     
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        // TODO Auto-generated method stub
  
  //m_log.info("***********************************  lanzo ");
        try {
            //Obtenemos el gestor de BBDD
              m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
             gestorD = m_ConfigTechnical.getString("CON.gestor");
            // Obtenemos los detalles de la tarea a ejecutar
            JobDetail jd = arg0.getJobDetail();

            // Obtenemos los parámetros
            String jndis = (String) jd.getJobDataMap().get("jndis_lanzamiento_proceso");

            StringTokenizer tokens = new StringTokenizer(jndis,";");
            while(tokens.hasMoreTokens()){
                String params[] = { gestorD, "", "", "", "", "", tokens.nextToken() };
                NotificacionPlazoTramiteManager.getInstance().notificarPlazoTramites(params);
            }

        } catch (Exception e) {
            e.printStackTrace();
            m_log.error("Error " + e.getMessage());
            throw new JobExecutionException("Error interno");
        }
    }

}
