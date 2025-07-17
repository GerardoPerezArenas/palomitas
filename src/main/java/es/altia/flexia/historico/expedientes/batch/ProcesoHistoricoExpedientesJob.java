/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expedientes.batch;

import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.historico.expediente.vo.ComunicacionVO;
import es.altia.flexia.historico.expediente.vo.CronoVO;
import es.altia.flexia.historico.expediente.vo.DocExtVO;
import es.altia.flexia.historico.expediente.vo.DocumentoPresentadoVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionVO;
import es.altia.flexia.historico.expediente.vo.ExpedienteEnvioHistoricoVO;
import es.altia.flexia.historico.expediente.vo.ExpedienteVO;
import es.altia.flexia.historico.expediente.vo.InteresadoExpedienteVO;
import es.altia.flexia.historico.expediente.vo.ListTramOrigVO;
import es.altia.flexia.historico.expediente.vo.NotificacionVO;
import es.altia.flexia.historico.expediente.vo.ProcedimientoHistoricoVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoExternoVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoVO;
import es.altia.flexia.historico.expediente.vo.SituacionExpedienteAnuladoVO;
import es.altia.flexia.historico.expedientes.dao.ExpedienteDAO;
import es.altia.flexia.historico.expedientes.dao.ExpedienteEnvioHistoricoDAO;
import es.altia.flexia.historico.expedientes.dao.HistoricoExpedienteDAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author santiagoc
 */
public class ProcesoHistoricoExpedientesJob implements Job
{

    //private static Log logger = Logger.getLogger(ProcesoHistoricoExpedientesJob.class);
    private static Logger logger = Logger.getLogger(ProcesoHistoricoExpedientesJob.class);
    
    
    //Para el fichero	de configuracion tecnico.
    protected static Config m_ConfigTechnical;
    protected static String gestorD;

     
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        // TODO Auto-generated method stub
       
        logger.info("***********************************  ProcesoHistoricoExpedientesJob ==============> ");
        try
        {
            //Obtenemos el gestor de BBDD
            m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
            gestorD = m_ConfigTechnical.getString("CON.gestor");
            String servidior_cluster_ejecucion=m_ConfigTechnical.getString("servidor_ejecucion_quartz");
            
            // Obtenemos los detalles de la tarea a ejecutar
            JobDetail jd = arg0.getJobDetail();

            // Obtenemos los parámetros
            //Si estamos en un entorno weblogic con servidor en cluster:
            if(("".equals(servidior_cluster_ejecucion))||(servidior_cluster_ejecucion.equals(System.getProperty("weblogic.Name")))){
                String jndis = (String) jd.getJobDataMap().get("jndis_lanzamiento_proceso");

                StringTokenizer tokens = new StringTokenizer(jndis,";");
                while(tokens.hasMoreTokens())
                {
                    String params[] = { gestorD, "", "", "", "", "", tokens.nextToken() };
                    proceso(params);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("Error " + e.getMessage());
            throw new JobExecutionException("Error interno");
        }
    }

    private void proceso(String[] params)
    {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try
        {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
        }
        catch(Exception ex)
        {
            logger.error("Error en la conexion a BD.", ex);
        }
        
        if(adapt != null && con != null)
        {
            try
            {
                ExpedienteEnvioHistoricoDAO expEnvHistDAO = ExpedienteEnvioHistoricoDAO.getInstance();
                ExpedienteDAO expDAO = ExpedienteDAO.getInstance();
                HistoricoExpedienteDAO histExpDAO = HistoricoExpedienteDAO.getInstance();
                
                
                ArrayList<ProcedimientoHistoricoVO> procedimientos = expEnvHistDAO.getProcedimientos(con);
                ArrayList<String> numExpedientesProcedimiento = null;
                
                int organizacion;
                int ejercicio;
                long idProceso;
                String procedimiento = null;        
                String[] datosNumExp = null;
                
                ExpedienteVO expediente = null;
                SituacionExpedienteAnuladoVO sitExp = null;
                GeneralValueObject gvo = null;
                ExpedienteVO pend = null;
                ExpedienteEnvioHistoricoVO env = null;
                ArrayList<CronoVO> tramitesExpediente = null;
                ArrayList<ListTramOrigVO> tramitesOrigenExpediente = null;
                ArrayList<InteresadoExpedienteVO> interesadosExpediente = null;
                ArrayList<DocumentoPresentadoVO> documentosPresentados = null;
                ArrayList<DocExtVO> documentosExternos = null;
                ArrayList<NotificacionVO> notificaciones = null;
                ArrayList<ComunicacionVO> comunicaciones = null;
                ArrayList<RegistroRelacionadoVO> registrosRelacionados = null;
                ArrayList<RegistroRelacionadoExternoVO> registrosRelacionadosExternos = null;
                ArrayList<DocumentoTramitacionVO> documentosTramitacion = null;
                ArrayList<OperacionExpedienteVO> operaciones = null;
                ArrayList<ValorCampoSuplementarioVO> valoresDatosSuplementarios = null;
                boolean resultadoGrabar = false;      
                int maximoExpedientesProceso = 0;
                int maximoExpedientesProcesoDefecto = Integer.parseInt(m_ConfigTechnical.getString("numero_maximo_expedientes_paso_historico_defecto"));
                
                logger.debug(" (Traza de control) Número máximo de expedientes a pasar al histórico valor por defecto: " + maximoExpedientesProcesoDefecto);
                
                
                // Se obtiene el día de la semana en el que se ejecuta el proceso. Según dicho valor, se recupera el número máximo 
                // de expedientes a procesar
                int diaSemana = -1;
                try{
                    Calendar momentoActual = Calendar.getInstance();
                    diaSemana = momentoActual.get(Calendar.DAY_OF_WEEK);
                    // 1:domingo; 2:lunes; 3:martes; 4:miércoles; 5:jueves; 6:viernes; 7:sábado
                    logger.debug(" =====> (Traza de control): Día de la semana en la que se ejecuta el proceso: " + diaSemana);
                    
                    maximoExpedientesProceso = Integer.parseInt(m_ConfigTechnical.getString("numero_maximo_expedientes_paso_historico/" + diaSemana));
                    logger.debug(" =====> (Traza de control): Numero de expedientes a procesar el " + diaSemana + ": " + maximoExpedientesProceso);
                }catch(Exception e){
                    maximoExpedientesProceso = maximoExpedientesProcesoDefecto;
                    logger.error(" ====>Error al recuperar el número máximo de expedientes a procesar en un determinado día de la semana: " + e.getMessage());
                    logger.debug(" ====> Se toma como máximo de expedientes a procesar, el valor por defecto: " + maximoExpedientesProcesoDefecto);
                    e.printStackTrace();                                        
                }
                
                int contadorExpedientes = 0;
                
                for(ProcedimientoHistoricoVO proc : procedimientos)
                {
                    organizacion = proc.getCodOrganizacion();
                    
                    logger.debug(" (Traza de control) Se van a procesar los expedientes del procedimiento " + proc.getCodProcedimiento());
                    numExpedientesProcedimiento = expEnvHistDAO.getExpedientesProcedimiento(organizacion, proc.getCodProcedimiento(), proc.getMeses(), con,params);
                    for(String numExpediente : numExpedientesProcedimiento)                 
                    {
                    
                        // Si el contador + 1 supera a máximo de expedientes, se sale del bucle para no procesar más expedientes
                        if((contadorExpedientes + 1)>maximoExpedientesProceso) break;
                        
                        idProceso = expEnvHistDAO.obtenerIdProceso(con);
                        logger.debug(" (Traza de control) Procesando expediente "+ numExpediente + " del procedimiento " + proc.getCodProcedimiento());
                        if(numExpediente != null && !numExpediente.equals(""))
                        {
                            try {                                   
                                
                                env = new ExpedienteEnvioHistoricoVO();
                                env.setId(idProceso);
                                env.setCodOrganizacion(organizacion);
                                env.setFecEnvArchivo(new java.util.GregorianCalendar());
                                env.setFechaProceso(new java.util.GregorianCalendar());
                                env.setNumExpediente(numExpediente);
                                
                                expEnvHistDAO.crearEnvioExpedienteHistorico(env, con,params);
                                
                                adapt.inicioTransaccion(con);
                                
                                datosNumExp = numExpediente.split("/");
                                ejercicio = Integer.parseInt(datosNumExp[0]);
                                procedimiento = datosNumExp[1];
                                pend = expDAO.getExpediente(organizacion, ejercicio, numExpediente, con);
                                
                                organizacion = pend.getCodOrganizacion();
                                
                                expediente = expDAO.getExpediente(organizacion, ejercicio, numExpediente, con);
                                
                                tramitesExpediente = expDAO.getTramitesExpediente(organizacion, ejercicio, procedimiento, numExpediente, con);
                                
                                tramitesOrigenExpediente = expDAO.getListaTramitesOrigen(organizacion, ejercicio, procedimiento, numExpediente, con);
                                
                                interesadosExpediente = expDAO.getInteresadosExpediente(organizacion, ejercicio, numExpediente, con);
                                
                                documentosPresentados = expDAO.getListaDocumentosPresentados(organizacion, ejercicio, procedimiento, numExpediente, con);
                                
                                documentosExternos = expDAO.getListaDocumentosExternos(organizacion, ejercicio, numExpediente, con);
                                
                                notificaciones = expDAO.getListaNotificaciones(organizacion, ejercicio, numExpediente, procedimiento, con);
                                
                                comunicaciones = expDAO.getListaComunicaciones(organizacion, ejercicio, numExpediente, con);
                                
                                registrosRelacionados = expDAO.getListaRegistroRelacionado(organizacion, ejercicio, numExpediente, con);
                                
                                registrosRelacionadosExternos = expDAO.getListaRegistroRelacionadoExterno(organizacion, numExpediente, con);
                                
                                //TODO tener en cuenta metadatos de documentos
								
                                documentosTramitacion = expDAO.getListaDocumentosTramitacion(organizacion, ejercicio, procedimiento, numExpediente, con);
                                
                                sitExp = expDAO.getSituacionExpedienteAnulado(organizacion, ejercicio, numExpediente, con);
                                
                                operaciones = OperacionesExpedienteDAO.getInstance().recuperarOperacionesExpediente(organizacion, 
                                        numExpediente, false, null, con);
                                
                                valoresDatosSuplementarios = expDAO.getValoresDatosSuplementarios(organizacion, numExpediente, adapt, con);

                                // Si todo ha ido bien, se ha recuperado toda la información a cargar del expediente, y se procederá a darla de alta en las tablas equivalentes, pero del histórico. 
                                if(expediente != null)
                                {
                                    resultadoGrabar = histExpDAO.grabarExpedienteHistorico(expediente, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar historicoExpediente para el expediente "+numExpediente);
                                }
                                
                                if(tramitesExpediente != null && tramitesExpediente.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarTramitesExpediente(tramitesExpediente, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar tramitesExpediente para el expediente "+numExpediente);
                                }
                                
                                if(tramitesOrigenExpediente != null && tramitesOrigenExpediente.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarListaTramitesOrigen(tramitesOrigenExpediente, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar listaTramitesOrigen para el expediente "+numExpediente);
                                }
                                
                                if(interesadosExpediente != null && interesadosExpediente.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarInteresadosExpediente(interesadosExpediente, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar interesadosExpediente para el expediente "+numExpediente);
                                }
                                
                                if(documentosPresentados != null && documentosPresentados.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarDocumentosPresentados(documentosPresentados, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar documentosPresentados para el expediente "+numExpediente);
                                }
                                
                                if(documentosExternos != null && documentosExternos.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarDocumentosExternos(documentosExternos, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar documentosExternos para el expediente "+numExpediente);
                                }
                                
                                if(notificaciones != null && notificaciones.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarNotificaciones(notificaciones, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar notificaciones para el expediente "+numExpediente);
                                }
                                
                                if(comunicaciones != null && comunicaciones.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarComunicaciones(comunicaciones, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar comunicaciones para el expediente "+numExpediente);
                                }
                                
                                if(registrosRelacionados != null && registrosRelacionados.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarRegistrosRelacionados(registrosRelacionados, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar registrosRelacionados para el expediente "+numExpediente);
                                }
                                
                                if(registrosRelacionadosExternos != null && registrosRelacionadosExternos.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarRegistrosExternosRelacionados(registrosRelacionadosExternos, idProceso, con,params);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar registrosExternosRelacionados para el expediente "+numExpediente);
                                }
                                
                                if(documentosTramitacion != null && documentosTramitacion.size() > 0)
                                {
                                    resultadoGrabar = histExpDAO.grabarDocumentosTramitacion(documentosTramitacion, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar documentosTramitacion para el expediente "+numExpediente);
                                }
								
                                //TODO tener en cuenta metadatos de documentos
                                
                                if(sitExp != null)
                                {
                                    resultadoGrabar = histExpDAO.grabarSituacionExpedienteAnulado(sitExp, idProceso, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar situacionExpedienteAnulado para el expediente "+numExpediente);
                                }
                                
                                if (operaciones != null && !operaciones.isEmpty())
                                    histExpDAO.grabarListaOperacionesHist(operaciones, idProceso, con);

                                
                                if(valoresDatosSuplementarios != null && !valoresDatosSuplementarios.isEmpty())
                                {
                                    resultadoGrabar = histExpDAO.grabarValoresDatosSuplementarios(idProceso, valoresDatosSuplementarios, adapt, con);
                                    if(!resultadoGrabar)
                                        throw new BDException("Error al grabar datos suplementarios para el expediente "+numExpediente);
                                }
                                // Si todo ha ido bien, y se ha almacenado toda la información del expediente en las correspondientes tablas del histórico, se procede a eliminar toda la información del expediente recuperada.
				// Una vez realizados todos los cambios, se procede a marcar como procesado el expediente en la tabla EXP_ENVIO_HISTORICO y se confirma la transacción
                                expDAO.borrarDatosExpediente(organizacion, ejercicio, procedimiento, numExpediente, con);
                                
                                try
                                {
                                    expEnvHistDAO.marcarEnvioExpedienteHistorico(env, 1, "", con,params);
                                }
                                catch(Exception e)
                                {
                                    logger.error("Error al actualizar registro EXP_ENVIO_HISTORICO: "+numExpediente, e);
                                }
                                adapt.finTransaccion(con);
                                //adapt.rollBack(con);
                                //32. Cuando el proceso finaliza , se cierra la conexión a la BBDD.
                                
                            }// try
                            catch(Exception ex)
                            {
                                //29. Si se ha producido algún error, se deshacen los cambios realizados y se aborta la transacción contra base de datos.
                                logger.error("Error en la transacción al pasar al histórico el expediente: " + numExpediente);
                                adapt.rollBack(con);
                                //Se procede a marcar como no procesado el expediente en la tabla EXP_ENVIO_HISTORICO invocando a ExpedienteEnvioHistoricoDAO.marcarProcesadoEnvioExpedienteHistorico().
                                try
                                {
                                    expEnvHistDAO.marcarEnvioExpedienteHistorico(env, 2, ex.getMessage(), con,params);
                                }
                                catch(Exception e)
                                {
                                    logger.error("Error al actualizar registro EXP_ENVIO_HISTORICO: "+ numExpediente + ": " +  e.getMessage());
                                }
                            }// catch
                            
                            contadorExpedientes++;
                            logger.debug(" ===> (Traza de control) contadorExpedientes: " + contadorExpedientes);
                            
                        }                        
                    }
                }// for
                
                logger.debug(" (Traza de control): Fin de ejecución del proceso de pase de expedientes al histórico <=================== ");
            }
            catch (Exception ex){
                ex.printStackTrace();
                logger.error("Error durante la ejecución del proceso BATCH de paso de expediente al histórico: " + ex.getMessage(), ex);
            }
            finally{
                
                if(con != null){
                    try
                    {
                        con.close();
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                        logger.error("Error cerrando conexion a BD en proceso BATCH de paso de expedientes al histórico: " + ex.getMessage(), ex);
                    }
                }
            }
        }
    }   
    /*
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        try
        {
            //Obtenemos el gestor de BBDD
            m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
            gestorD = m_ConfigTechnical.getString("CON.gestor");

            // Obtenemos los parámetros
            String params[] = { "ORACLE", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@oracle11:1521:ora11", "FLBPRU", "FLBPRU", "C:\\logs\\sge\\sge.log", null};
            ProcesoHistoricoExpedientesJob p = new ProcesoHistoricoExpedientesJob();
            p.proceso(params);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("Error " + e.getMessage());
        }
    }
    */
}