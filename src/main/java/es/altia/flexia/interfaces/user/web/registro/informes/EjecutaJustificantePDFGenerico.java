package es.altia.flexia.interfaces.user.web.registro.informes;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.commons.JasperReportsOperations;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;




public class EjecutaJustificantePDFGenerico implements IEjecutaJustificantePDF{

    private Logger log = Logger.getLogger(EjecutaJustificantePDFGenerico.class);
    private final String BARRA = "/";
	public EjecutaJustificantePDFGenerico() {
		super();		
	}


    private String formatearNumeroAsiento(String numero){

        int total = ConstantesDatos.LONGITUD_MAXIMA_NUMERO_ASIENTO - numero.length();
        StringBuffer aux = new StringBuffer();
        if(numero!=null && numero.length()<6){
            for(int i=0;i<total;i++){
                aux.append(ConstantesDatos.ZERO);
            }
        }// if
        aux.append(numero);
        return aux.toString();        
    }




	public String ejecutaJustificantePDF(String origenPlantilla, String nombrePlantilla,String descUorRegistro, String xml, boolean printBarcode, String destinoPdf) throws Exception{

		Map parameters = new HashMap();
		try
		{
			InputSource isoXML = new InputSource( new StringReader( xml ));
			SAXBuilder builder = new SAXBuilder();
	        Document doc = builder.build(isoXML);
	        Iterator itr = doc.getDescendants();
	        while (itr.hasNext()) {
	            Content c = (Content) itr.next();
	            if (c instanceof Text) {
	            	if(c.getParentElement().getName().equals("NUMEROANOTACION") && printBarcode){
	            		parameters.put(c.getParentElement().getName(),c.getValue());
                        String numeroAnotacion = c.getValue();
                        String[] datos = numeroAnotacion.split(ConstantesDatos.BARRA);
                        String ejercicio = "";
                        String numero = "";
                        String barcode = "";
                        if(datos!=null && datos.length==2){
                            ejercicio = datos[0];
                            numero    = datos[1];
                            barcode = ejercicio + this.formatearNumeroAsiento(numero);
                        }
                        
                        parameters.put("BARCODE",barcode);	            		
	            	}else{
	            		parameters.put(c.getParentElement().getName(),c.getValue());
	            	}
                    parameters.put("DESC_UOR_REGISTRO",descUorRegistro);

	           }
	        }
		}

		catch(Exception ex) {
			 log.error(ex.getLocalizedMessage());
			 return "";
		}

       	long start = System.currentTimeMillis();

  	    try {
                log.info("Inicio de cración del justificante, los parámetros son: origenPlantilla: " + origenPlantilla + ",nombrePlantilla: " + nombrePlantilla + ", start: " + start);
                log.info("Parámetros de la función de creción del fichero PDF : Clave - Valor ");
                for (Object name : parameters.keySet()) {
                    String key = name.toString();
                    String value = parameters.get(name).toString();
                    log.info(key + " " + value);
                }
                log.info("volcamos la plantilla al fichero PDF");
                JasperFillManager.fillReportToFile(origenPlantilla + File.separator + nombrePlantilla + ".jasper", origenPlantilla + File.separator + nombrePlantilla + start + ".jrprint", parameters, new JREmptyDataSource());

                log.info("Creamos el objecto jasperPrint");
                JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(new File(origenPlantilla + File.separator + nombrePlantilla + start + ".jrprint"));
                log.info("Creamos el nombre del fichero PDF");
                String nombreFicheroDestino = nombrePlantilla + start + ".pdf";
                log.info("Creamos el objeto FILE");
                File destFile = new File(destinoPdf, nombreFicheroDestino);
                log.info("Inicializamos JRPdfExporter");
                JRPdfExporter exporter = new JRPdfExporter();
                log.info("Seteamos el objeto  jasperPrint y el nombre del fichero en el JRPdfExporter");
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
                log.info("Exportamos el fichero");
                exporter.exportReport();
                
            log.info("Retornamos el nombre del fichero");
            return nombreFicheroDestino;

  		} catch (JRException e) {
            log.error("EjecutaJustificantePDF " + e.getMessage());
			e.printStackTrace();
			return "";
  		} catch (Exception e) {
            log.error("EjecutaJustificantePDF " + e.getMessage());
			e.printStackTrace();
            return "";
  		}
	}

    public byte[] generaJustificantePDF(String origenPlantilla, String nombrePlantilla, String descUorRegistro, String xml, boolean printBarcode) throws Exception {

        Map parameters = new HashMap();
        byte[] resultado = null;

        try {
            InputSource isoXML = new InputSource(new StringReader(xml));
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(isoXML);
            Iterator itr = doc.getDescendants();
            while (itr.hasNext()) {
                Content c = (Content) itr.next();
                if (c instanceof Text) {
                    if (c.getParentElement().getName().equals("NUMEROANOTACION") && printBarcode) {
                        parameters.put(c.getParentElement().getName(), c.getValue());
                        String numeroAnotacion = c.getValue();
                        String[] datos = numeroAnotacion.split(ConstantesDatos.BARRA);
                        String ejercicio = "";
                        String numero = "";
                        String barcode = "";
                        if (datos != null && datos.length == 2) {
                            ejercicio = datos[0];
                            numero = datos[1];
                            barcode = ejercicio + this.formatearNumeroAsiento(numero);
                        }

                        parameters.put("BARCODE", barcode);
                    } else {
                        parameters.put(c.getParentElement().getName(), c.getValue());
                    }
                    parameters.put("DESC_UOR_REGISTRO", descUorRegistro);

                }
            }
            // Parameter containing the resource bundle desired locale.
            parameters.put("REPORTS_DIR", origenPlantilla);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            return null;
        }

        String rutaPlantilla = origenPlantilla + File.separator + nombrePlantilla;
        return JasperReportsOperations.getInstance().generarJasperReportPDF(rutaPlantilla, parameters);
        
    }

    public byte[] generaJustificantePDF_SIR(String origenPlantilla, String nombrePlantilla, Map<String,Object> parametros) throws Exception {

        Map parameters = new HashMap();
        byte[] resultado = null;

        try {
            for ( Map.Entry<String, Object> mapEntry: parametros.entrySet()){
                if (mapEntry.getValue() instanceof String) {
                    parameters.put(mapEntry.getKey(), (mapEntry.getValue() != null && mapEntry.getValue() != "null" ? (String) mapEntry.getValue(): ""));
                }else if (mapEntry.getValue() instanceof ArrayList){
                    parameters.put(mapEntry.getKey(), (mapEntry.getValue() != null  ? mapEntry.getValue(): new ArrayList()));
                }
            }
            // Parameter containing the resource bundle desired locale.
            parameters.put("REPORTS_DIR", origenPlantilla);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            return null;
        }

        String rutaPlantilla = origenPlantilla + File.separator + nombrePlantilla;
        return JasperReportsOperations.getInstance().generarJasperReportPDF(rutaPlantilla, parameters);

    }

    public String ejecutaJustificantePDF_SIR(String origenPlantilla, String nombrePlantilla, String descUorRegistro, Map<String,Object> parameters, String destinoPdf) throws Exception{

        if(parameters==null)
            parameters = new HashMap();

        // Parameter containing the resource bundle desired locale.
        parameters.put("REPORTS_DIR", origenPlantilla);

        long start = System.currentTimeMillis();

        try {
            log.info("Inicio de cración del justificante de SIR, los parámetros son: origenPlantilla: " + origenPlantilla + ",nombrePlantilla: " + nombrePlantilla + ", start: " + start);
            log.info("Parámetros de la función de creción del fichero PDF : Clave - Valor ");

            for ( Map.Entry<String, Object> mapEntry: parameters.entrySet()){
                if (mapEntry.getValue() instanceof String) {
                    parameters.put(mapEntry.getKey(), (mapEntry.getValue() != null && mapEntry.getValue() != "null" ? (String) mapEntry.getValue(): ""));
                }else if (mapEntry.getValue() instanceof ArrayList){
                    parameters.put(mapEntry.getKey(), (mapEntry.getValue() != null  ? mapEntry.getValue(): new ArrayList()));
                }
                log.info(mapEntry.getKey() + " " + mapEntry.getValue());
            }
            /*
            for (Object name : parameters.keySet()) {
                String key = name.toString();
                String value = parameters.get(name)!= null ? parameters.get(name).toString() : "null";
                log.info(key + " " + value);
            }
            */
            log.info("volcamos la plantilla al fichero PDF - SIR ");
            JasperFillManager.fillReportToFile(origenPlantilla + File.separator + nombrePlantilla + ".jasper", origenPlantilla + File.separator + nombrePlantilla + start + ".jrprint", parameters, new JREmptyDataSource());

            log.info("Creamos el objecto jasperPrint - SIR ");
            JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(new File(origenPlantilla + File.separator + nombrePlantilla + start + ".jrprint"));
            log.info("Creamos el nombre del fichero PDF - SIR ");
            String nombreFicheroDestino = nombrePlantilla + start + ".pdf";
            log.info("Creamos el objeto FILE - SIR ");
            File destFile = new File(destinoPdf, nombreFicheroDestino);
            log.info("Inicializamos JRPdfExporter");
            JRPdfExporter exporter = new JRPdfExporter();
            log.info("Seteamos el objeto  jasperPrint y el nombre del fichero en el JRPdfExporter - SIR ");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
            log.info("Exportamos el fichero");
            exporter.exportReport();

            log.info("Retornamos el nombre del fichero - SIR ");
            return nombreFicheroDestino;

        } catch (JRException e) {
            log.error("EjecutaJustificantePDF_SIR" + e.getMessage());
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            log.error("EjecutaJustificantePDF_SIR " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

}