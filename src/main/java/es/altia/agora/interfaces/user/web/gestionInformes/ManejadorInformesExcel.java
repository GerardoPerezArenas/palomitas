
package es.altia.agora.interfaces.user.web.gestionInformes;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ListLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.gestionInformes.InformeValueObject;

import es.altia.agora.business.gestionInformes.CuerpoInformeValueObject;
import es.altia.agora.business.gestionInformes.InformeTableModel;
import es.altia.agora.business.escritorio.UsuarioValueObject;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import es.altia.util.exceptions.InternalErrorException;
import java.io.FileNotFoundException;

import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParagraph;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
//import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRProperties;


public class ManejadorInformesExcel {
     protected static Log m_Log = LogFactory.getLog(ManejadorInformesExcel.class.getName());
     protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
     protected static Config m_Doc = ConfigServiceHelper.getConfig("documentos");
     
     private static ManejadorInformesExcel instance = null;
     
     
    
     public static ManejadorInformesExcel getInstance() {
     //si no hay ninguna instancia de esta clase tenemos que crear una.
     if (instance == null) {
        synchronized(ManejadorInformesExcel.class){
         if (instance == null)
            instance = new ManejadorInformesExcel();
        }
     }
     return instance;
    }
     
     
    public String generarInformeExcel(Vector columnasInforme, Vector datosInforme,String host, String context, UsuarioValueObject usuario, int formato, Vector listaTiposCamposInforme, InformeValueObject informeVO, InformeTableModel informeTableModel, String accion, String protocolo) throws Exception{
       JasperPrint jasperPrint;
       JasperReport jasperReport;
                    
       Map parametros = new HashMap();
       
        m_Log.debug("-- generarInformeExcel()");
        try {
        
        String destinoInforme = null;
        
        
        FastReportBuilder fastReportBuilder = new FastReportBuilder();
         
        if(informeVO.getTitulo()!=null){
            parametros.put("header",informeVO.getTitulo());
            fastReportBuilder.setTitle(informeVO.getTitulo());
        }
        
        Style headerStyle = new Style();
        headerStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        
        Style detailStyle = new Style();
        detailStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        
        Page pageOrientation=null;
        if (informeVO.getOrientacion().equalsIgnoreCase("horizontal")) {
            pageOrientation=Page.Page_A4_Landscape();
        } else {
            pageOrientation=Page.Page_A4_Portrait();
        }
         
        Vector vectorCuerpoInformeVO = informeVO.getVectorCuerpoInformeVO();
        CuerpoInformeValueObject cuerpoInformeVO;
       
        for(int i=0; i<columnasInforme.size(); i++){
            cuerpoInformeVO = (CuerpoInformeValueObject) vectorCuerpoInformeVO.elementAt(i);
           
            AbstractColumn column = ColumnBuilder.getInstance()
                .setColumnProperty("Columna"+i, String.class.getName())
                .setTitle(columnasInforme.get(i).toString())
                .setWidth(Integer.parseInt(cuerpoInformeVO.getAncho()))
                .setStyle(detailStyle).setHeaderStyle(headerStyle).build();
                
            fastReportBuilder.addColumn(column)
                    .setPrintColumnNames(true)
                    .setIgnorePagination(true)
                    .setPrintBackgroundOnOddRows(true)
                    .setMargins(Integer.parseInt(informeVO.getMargenSuperior()),Integer.parseInt(informeVO.getMargenInferior()),
                            Integer.parseInt(informeVO.getMargenDerecho()),Integer.parseInt(informeVO.getMargenIzquierdo()))
                    .setUseFullPageWidth(true)
                    .setPageSizeAndOrientation(pageOrientation)
                    .setReportName(informeVO.getTitulo());
        }
        fastReportBuilder.setUseFullPageWidth(true);
       
      
        //Se pasa la plantilla JasperReport (con la estructura básica) a DynamicJasper para su generación   
       
        // Rutas de la plantilla 
        StringBuilder plantillaJasper = new StringBuilder();

        plantillaJasper.append(m_Conf.getString("SIGP.webapp_base_dir"))
                       .append(File.separator)
                       .append(ConstantesDatos.RUTA_PLANTILLAS_JASPER)
                       .append(File.separator)
                       .append(ConstantesDatos.FICHERO_PLANTILLA_EXCEL_INFORMES);
                
        fastReportBuilder.setTemplateFile(plantillaJasper.toString());
        
        DynamicReport dinamicReport = fastReportBuilder.build();
        JRDataSource ds = setDatosInforme(datosInforme);
        
        jasperReport = DynamicJasperHelper.generateJasperReport(dinamicReport, new ListLayoutManager(),parametros);
        
        // Se modifica la propiedad RParagraph.DEFAULT_TAB_STOP_WIDTH the JasperReport para admitir textos largos.
        JRProperties.setProperty(JRParagraph.DEFAULT_TAB_STOP_WIDTH, "1");
        if (ds != null) {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, ds);
        } else {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros);
        }
        //JRXlsExporter exporter = new JRXlsExporter();
        JRXlsxExporter exporter = new JRXlsxExporter ();
        
        File directorioTemp = new File(m_Conf.getString("PDF.base_dir") + File.separator + "tmp");
            if (!directorioTemp.exists()) {
                directorioTemp.mkdirs();
            }//if
            
        File informe;
        informe = File.createTempFile("informe_",".xlsx",directorioTemp);
              
       destinoInforme = informe.getAbsolutePath().replace('\\','/');
            if(m_Log.isDebugEnabled()){
                m_Log.debug("\n\nINFORME:getAbsolutePath() "+informe.getAbsolutePath()+"\n\n");
                m_Log.debug("destinoInforme "+destinoInforme);
            }//if

        
        File outputFile = new File(destinoInforme);
        FileOutputStream out = new FileOutputStream(outputFile);

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out); 

        //Parametros para la exportación a excel
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
      
        
        exporter.exportReport();
        
        return destinoInforme;
        
        } catch (FileNotFoundException e) {
            m_Log.error("Error al leer la plantilla o generar el xlsx");
            e.printStackTrace();
            throw new InternalErrorException(e);
        } catch (JRException e) {
            m_Log.error("No se ha podido generar el xlsx");
            e.printStackTrace();
            throw new InternalErrorException(e);
        }
}

protected JRDataSource setDatosInforme(Vector datosInforme) {
    List listaDatos = new ArrayList();
    Vector fila;
    for(int i=0; i<datosInforme.size(); i++){
        Map columnas = new HashMap();
        fila = (Vector)datosInforme.get(i);
        for (int j=0; j<fila.size(); j++) {
            columnas.put("Columna"+j, fila.get(j));
        }
        listaDatos.add(columnas);
    }
    JRDataSource ds = new JRMapCollectionDataSource(listaDatos);
    return ds;

    } 
}
