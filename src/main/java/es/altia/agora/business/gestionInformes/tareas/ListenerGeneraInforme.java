/**
 * 
 */
package es.altia.agora.business.gestionInformes.tareas;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.persistence.SolicitudesInformesManager;

/**
 * @author BCS
 *
 */
public class ListenerGeneraInforme implements JobListener {
    private static Log _log = LogFactory.getLog(ListenerGeneraInforme.class);
    /* (non-Javadoc)
      * @see org.quartz.JobListener#getName()
      */
    public String getName() {
        _log.info("getName");
        return "ListenerGeneraInforme";
    }

    /* (non-Javadoc)
      *  Este método no debería ser llamado nunca. Lo ponemos por si falla algo y tener mas información.
      * @see org.quartz.JobListener#jobExecutionVetoed(org.quartz.JobExecutionContext)
      */
    public void jobExecutionVetoed(JobExecutionContext arg0) {
//		 Obtenemos los detalles del trabajo
        JobDetail jd = arg0.getJobDetail();

        // Obtenemos los parámetros
        String codigoSolicitud = (String) jd.getJobDataMap().get("codigoSolicitud");
        _log.info("jobExecutionVetoed");
        _log.debug("codigoSolicitud ="+codigoSolicitud);
    }

    /* (non-Javadoc)
      *  Este método se ejecuta cuando se va a disparar el proceso.
      * @see org.quartz.JobListener#jobToBeExecuted(org.quartz.JobExecutionContext)
      */
    public void jobToBeExecuted(JobExecutionContext arg0) {
        _log.info("jobToBeExecuted");

        // Obtenemos los detalles del trabajo
        JobDetail jd = arg0.getJobDetail();

        try {
            // Obtenemos los parámetros
            String codigoSolicitud = (String)jd.getJobDataMap().get("codigoSolicitud");
            UsuarioValueObject usuario = (UsuarioValueObject) jd.getJobDataMap().get("usuario");
            String[] params = usuario.getParamsCon();

            // Actualizamos la info de la BD
            int estado = AsistenteEstado.ESTADO_EN_PROCESO;
            SolicitudesInformesManager.getInstance().anotaInicioSolicitud(params, codigoSolicitud, estado);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* (non-Javadoc)
      * Este método se ejecuta cuando se finaliza el proceso.
      * @see org.quartz.JobListener#jobWasExecuted(org.quartz.JobExecutionContext, org.quartz.JobExecutionException)
      */
    public void jobWasExecuted(JobExecutionContext contexto,
                               JobExecutionException excepcion) {
        // TODO Auto-generated method stub
        _log.info("jobWasExecuted");

        // Obtenemos los detalles del trabajo
        JobDetail jd = contexto.getJobDetail();
        long tiempo = contexto.getJobRunTime(); // en milisegundos

        // Obtenemos los parámetros
        String codigoSolicitud = (String)jd.getJobDataMap().get("codigoSolicitud");
        String fichero = (String)jd.getJobDataMap().get("fichero");
        UsuarioValueObject usuario = (UsuarioValueObject) jd.getJobDataMap().get("usuario");
        String[] params = usuario.getParamsCon();
        String hayDatos = (String)jd.getJobDataMap().get("hayDatos");

	int estado = AsistenteEstado.ESTADO_FINALIZADO;
        _log.debug("EXCEPCION: "+excepcion);
        if (excepcion != null) {
			estado = AsistenteEstado.ESTADO_ERROR;
            _log.debug("ESTADO CAMBIADO A ERROR: "+estado);
        } else if (hayDatos!=null && hayDatos.equals("no")) {
            estado = AsistenteEstado.ESTADO_FINALIZADO_SIN_DATOS;
            _log.debug("ESTADO CAMBIADO A SIN DATOS: "+estado);
		}
		// Actualizamos la info de la BD
		try {
            SolicitudesInformesManager.getInstance().anotaFinSolicitud(params, codigoSolicitud, tiempo, fichero, estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
