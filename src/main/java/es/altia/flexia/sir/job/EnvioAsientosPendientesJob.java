/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.sir.job;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.sir.GestionSir;
import es.altia.util.security.SeguridadManager;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Kevin
 */
public class EnvioAsientosPendientesJob implements Job{

		private static final Logger LOGGER = Logger.getLogger(EnvioAsientosPendientesJob.class);

	
	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {

		try {

			//Obtenemos el gestor de BBDD
			Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
			String gestorD = m_ConfigTechnical.getString("CON.gestor");

			// Obtenemos los detalles de la tarea a ejecutar
			JobDetail jd = jec.getJobDetail();

			// Obtenemos los parámetros
			String jndis = (String) jd.getJobDataMap().get("jndis_lanzamiento_proceso");

			StringTokenizer tokens = new StringTokenizer(jndis, ";");
			while (tokens.hasMoreTokens()) {
				String params[] = {gestorD, "", "", "", "", "", tokens.nextToken()};
				GestionSir gestionSir = new GestionSir();
				gestionSir.enviarAsientosPendientes(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(String.format("Error: %s", e.getMessage()));
			throw new JobExecutionException("Error interno");
		}
	}
	
}
