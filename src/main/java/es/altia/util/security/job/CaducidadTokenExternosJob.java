package es.altia.util.security.job;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.security.SeguridadManager;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CaducidadTokenExternosJob implements Job {

    private static final Log log = LogFactory.getLog(CaducidadTokenExternosJob.class.getName());

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            SeguridadManager seguridadManager = SeguridadManager.getInstance();
            
            //Obtenemos el gestor de BBDD
            Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
            String gestorD = m_ConfigTechnical.getString("CON.gestor");
            
            // Obtenemos los detalles de la tarea a ejecutar
            JobDetail jd = arg0.getJobDetail();

            // Obtenemos los parámetros
            String jndis = (String) jd.getJobDataMap().get("jndis_lanzamiento_proceso");

            StringTokenizer tokens = new StringTokenizer(jndis, ";");
            while (tokens.hasMoreTokens()) {
                String params[] = {gestorD, "", "", "", "", "", tokens.nextToken()};
                
                seguridadManager.eliminarTokenExternoCaducadosConReferencias(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(String.format("Error: %s", e.getMessage()));
            throw new JobExecutionException("Error interno");
        }
    }
}
