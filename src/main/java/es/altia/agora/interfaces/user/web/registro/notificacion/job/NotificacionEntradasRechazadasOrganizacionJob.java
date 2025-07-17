

package es.altia.agora.interfaces.user.web.registro.notificacion.job;

import es.altia.agora.interfaces.user.web.registro.notificacion.batch.NotificacionEntradasRechazadasManager;
import es.altia.agora.interfaces.user.web.registro.notificacion.job.util.NotificacionJobLanbideUtils;
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
 * @author altia
 */
public class NotificacionEntradasRechazadasOrganizacionJob implements Job{
     private static Log m_Log = LogFactory.getLog(NotificacionEntradasRechazadasOrganizacionJob.class);
    
    protected static Config m_ConfigTechnical;
    protected static String gestorD;
    
    
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try{
            // Comprobamos si el servidor que ejecuta el job es el especificado por lanbide
            if(NotificacionJobLanbideUtils.getServer().equals(System.getProperty("weblogic.Name"))){
                m_Log.info("-------- lanzo el proceso NotificacionEntradasRechazadasOrganizacionJob::Weblogic.Name: " + System.getProperty("weblogic.Name") + " ------");
                // se obtiene el gesto de bbdd
                m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
                gestorD = m_ConfigTechnical.getString("CON.gestor");

                //obtenemos los detalles de la tarea a ejecutar
                JobDetail jd = arg0.getJobDetail();

                //obtenemos los parámetros
                String jndis = (String) jd.getJobDataMap().getString("jndis_lanzamiento_proceso");

                StringTokenizer tokens = new StringTokenizer(jndis,";");
                while(tokens.hasMoreTokens()){
                    String params[] = {gestorD, "", "" , "", "", "", tokens.nextToken()};
                    NotificacionEntradasRechazadasManager.getInstance().notificarRechazadasOrganizacion(params);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            m_Log.error("Error "+e.getMessage());
            throw new JobExecutionException("Error interno");
        }
    }
    
}
