package es.altia.util.commons;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.io.IOOperations;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JasperReportsOperations {

    private static final Log logger = LogFactory.getLog(JasperReportsOperations.class.getName());
    private static JasperReportsOperations instance = null;

    private JasperReportsOperations() {
    }

    public static JasperReportsOperations getInstance() {
        if (instance == null) {
            instance = new JasperReportsOperations();
        }
        return instance;
    }

    public void generatePdf(String ficheroPlantilla, String dirPdf,
            String ficheroDestino, Map<String, Object> parameters) throws InternalErrorException {
        try {
            
            JasperPrint print = JasperFillManager.fillReport(ficheroPlantilla, parameters, new JREmptyDataSource());
            
            String rutaCompleta = String.format("%s%s%s", dirPdf, File.separator, ficheroDestino);
            
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream(rutaCompleta.toString()));

            exporter.exportReport();
        } catch (FileNotFoundException e) {
            logger.error("Error al leer la plantilla o generar el pdf");
            e.printStackTrace();
            throw new InternalErrorException(e);
        } catch (JRException e) {
            logger.error("No se ha podido generar el pdf");
            e.printStackTrace();
            throw new InternalErrorException(e);
        }
    }

    /**
     * Genera un informe jasper y lo devuelve como byte[]. Este método falla en fillReport si la plantilla tiene imágenes
     * @param rutaAbsolutaReporteName Ruta absoluta del archivo .jasper
     * @param datos Parametros con los que rellenar el informe
     * @return Byte[] del pdf resultante
     */
    public byte[] generarJasperReportPDF(String rutaAbsolutaReportName,Map<String,Object> datos) {
        JasperPrint jasperPrint = null;
        byte[] result = null;
        
        try {
            File fichJasper = new File(rutaAbsolutaReportName+".jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fichJasper);
            jasperPrint = JasperFillManager.fillReport(jasperReport, datos,new JREmptyDataSource());
            if (jasperPrint!=null) {
                ByteArrayOutputStream outAux = new ByteArrayOutputStream();

                try{
                    JRAbstractExporter exporter = null;
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (outAux));
                    
                    exporter.exportReport();
                    result = (outAux.toByteArray());
                } catch (Exception e) {
                    logger.error("No se ha podido exportar a pdf");
                    e.printStackTrace();
                } finally {
                    IOOperations.closeOutputStreamSilently(outAux);
                }
            }
        } catch (JRException ex) {
            logger.error("No se ha podido generar el informe");
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Genera un informe jasper y lo devuelve como byte[]. Este método falla en fillReport si la plantilla tiene imágenes
     * @param rutaAbsolutaReporteName Ruta absoluta del archivo .jasper
     * @param datos Parametros con los que rellenar el informe
     * @return Byte[] del pdf resultante
     */
    public byte[] generarPDF(String rutaAbsolutaReport, Map<String,Object> datos) {
        JasperPrint jasperPrint = null;
        byte[] result = null;
        String ficheroOrigenJasper = null;
        String fichSalidaTempJRprint = null;
        
        try {
            ficheroOrigenJasper = rutaAbsolutaReport + ".jasper";
            fichSalidaTempJRprint = rutaAbsolutaReport + System.currentTimeMillis() + ".jrprint";
            JasperFillManager.fillReportToFile(ficheroOrigenJasper,  datos, new JREmptyDataSource());
            jasperPrint = (JasperPrint)JRLoader.loadObject(new File(fichSalidaTempJRprint));
            if (jasperPrint!=null) {
                ByteArrayOutputStream outAux = new ByteArrayOutputStream();

                try{
                    JRAbstractExporter exporter = null;
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (outAux));
                    
                    exporter.exportReport();
                    result = (outAux.toByteArray());
                } catch (Exception e) {
                    logger.error("No se ha podido exportar a pdf");
                    e.printStackTrace();
                } finally {
                    IOOperations.closeOutputStreamSilently(outAux);
                }
            }
        } catch (JRException ex) {
            logger.error("No se ha podido generar el informe");
            ex.printStackTrace();
        }
        return result;
    }
}
