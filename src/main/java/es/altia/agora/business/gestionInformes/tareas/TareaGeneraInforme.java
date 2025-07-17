package es.altia.agora.business.gestionInformes.tareas;

import java.util.Date;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import es.altia.agora.business.gestionInformes.persistence.GestionInformesManager;
import es.altia.agora.business.gestionInformes.persistence.FichaInformeManager;
import es.altia.agora.business.gestionInformes.InformeValueObject;
import es.altia.agora.business.gestionInformes.InformeTableModel;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.gestionInformes.ManejadorInformes;
import es.altia.agora.interfaces.user.web.gestionInformes.ManejadorInformesExcel;
import es.altia.agora.interfaces.user.web.gestionInformes.ManejadorInformesPentahoPDF;



/**
 * @author BCS
 * 
 */
public class TareaGeneraInforme implements Job {

    private static Log _log = LogFactory.getLog(TareaGeneraInforme.class);

    /*
      * (non-Javadoc)
      *
      * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
      */
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        // TODO Auto-generated method stub
        _log.info("execute generar informe BEGIN");
        try {
            JobDetail jd = arg0.getJobDetail();
            String codigoSolicitud = (String) jd.getJobDataMap().get("codigoSolicitud");
            String codigoPlantilla = (String) jd.getJobDataMap().get("codigoPlantilla");
            String tipoFichero = (String) jd.getJobDataMap().get("tipoFichero");
            UsuarioValueObject usuario = (UsuarioValueObject) jd.getJobDataMap().get("usuario");
            String host = (String) jd.getJobDataMap().get("host");
            String context = (String) jd.getJobDataMap().get("context");
            String protocolo = (String) jd.getJobDataMap().get("protocolo");
            SolicitudInformeValueObject solicitudInformeVO = (SolicitudInformeValueObject) jd.getJobDataMap().get("solicitudInformeVO");
            String[] params = usuario.getParamsCon();
            String codOrganizacion = String.valueOf(usuario.getOrgCod());
            if(_log.isDebugEnabled()){
                _log.debug("codigoSolicitud: "+codigoSolicitud);
                _log.debug("codigoPlantilla: "+codigoPlantilla);
                _log.debug("tipoFichero: "+tipoFichero);
                _log.debug("host: "+host);
                _log.debug("context: "+context);
                _log.debug("protocolo: "+protocolo);
                _log.debug("codOrganizacion: "+codOrganizacion);
            }//if
            
            _log.debug("Recuperamos los tipos de campos del informe");
            Vector listaTiposCamposInforme = GestionInformesManager.getInstance().getTipoCamposInformes(params);
            _log.debug("FIN Recuperamos los tipos de campos del informe");
            _log.debug("Recuperamos la estructura del informe - ");
            InformeValueObject informeVO = FichaInformeManager.getInstance().getEstructuraInforme(params, codigoPlantilla, codigoSolicitud);
            _log.debug("FIN Recuperamos la estructura del informe");
            _log.debug("Recuperamos los campos ordenados del informe");
            Vector columnasInforme = FichaInformeManager.getInstance().getCamposOrdenadosInforme(codigoPlantilla,params);
            _log.debug("FIN Recuperamos los campos ordenados del informe");
            _log.debug("Recuperamos el origen de la plantilla");
            String origen = GestionInformesManager.getInstance().getOrigenPlantilla(params, codigoPlantilla);
            _log.debug("FIN Recuperamos el origen de la plantilla");
            _log.debug("Recuperamos los datos del informe");
            Vector datosInforme = FichaInformeManager.getInstance().getDatosExpedientes(solicitudInformeVO, codOrganizacion, origen, params);
            _log.debug("FIN Recuperamos los datos del informe");
            if (datosInforme.size()!=0) {
                InformeTableModel informeTableModel = new InformeTableModel();
                _log.debug("Construir informe table model");
                informeTableModel.construirInformeTableModel(columnasInforme,datosInforme);
                _log.debug("FIN Construir informe table model");
                //_log.info("id tipo fichero");
                int iTipoFichero = Integer.parseInt(tipoFichero);
                String fichero = null;
                if(iTipoFichero==0){
                    // Informe en PDF. Se genera con PENTAHO porque tras la introducción de los justificantes de registro en PDF se ha actualizado la versión
                    // de la librería de iText y ya no funcionaba con la versión de JFreeReport 0.8.8. Ha sido necesario utilizar PENTAHO
                    fichero = ManejadorInformesPentahoPDF.getInstance().generarInfome(host,context,usuario,Integer.parseInt(tipoFichero),listaTiposCamposInforme,informeVO,informeTableModel,"generar",protocolo);
                }else if(iTipoFichero==1 || iTipoFichero==3){
                    // Si el informe es en HTML, EXCEL o CSV, se genera con JFreeReport ya que con Pentaho hay problemas al generar el informe en estos formatos con JAVA 5
                    _log.debug("Generar informe");
                    fichero = ManejadorInformes.getInstance().generarInfome(host,context,usuario,Integer.parseInt(tipoFichero),listaTiposCamposInforme,informeVO,informeTableModel,"generar",protocolo);
                    _log.debug("FIN Generar informe");
                } else if(iTipoFichero==2){
                    // Si el informe es EXCEL se genera con JasperReports junto con DynamicJasper ya que JFreeReports  da problemas de incompatiblidad con la libreria poi
                    _log.debug("Generar informe EXCEL");                    
                     fichero = ManejadorInformesExcel.getInstance().generarInformeExcel(columnasInforme, datosInforme,host,context,usuario,Integer.parseInt(tipoFichero),listaTiposCamposInforme,informeVO,informeTableModel,"generar",protocolo);
                     //fichero = buildReport(columnasInforme, datosInforme);
                    _log.debug("FIN Generar informe EXCEL");
                }//if
                jd.getJobDataMap().put("fichero",fichero);
            } else {
                jd.getJobDataMap().put("hayDatos","no");
            }//if
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("Error " + e.getMessage());
            throw new JobExecutionException("Error interno");
        }//try-catch
         _log.info("execute fin generar informe END");
    }//execute
}//class
