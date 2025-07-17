package es.altia.util.commons;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.DocumentConversionException;
import es.altia.common.exception.FormatNotSupportedException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DefaultDocumentFormatRegistry;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeConnectionProtocol;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;
import com.sun.star.beans.PropertyValue;
import java.util.HashMap;
import java.util.Map;
import org.artofsolving.jodconverter.document.DocumentFamily;

public class JODConverterHelper {

    private static final Config config = ConfigServiceHelper.getConfig("techserver");
    private static final Logger log = Logger.getLogger(JODConverterHelper.class);
    
    private static int[] ports;
    private static int maxTasksPerProcess;
    private static long taskExecutionTimeout;
    private static long taskQueueTimeout;
    private static String officeHome;
    private static boolean connectOnStart;
    private static boolean esGestionInterna;

    private static OfficeManager officeManagerInstance;

    public static void convertirDocumento(File fileIn, String tipoMimeIn, File fileOut, String tipoMimeOut, String pdfA) throws DocumentConversionException, FormatNotSupportedException {
        try {
            // Comprobamos parametros
            if (fileIn == null || fileOut == null || StringUtils.isEmpty(tipoMimeIn) || StringUtils.isEmpty(tipoMimeOut)) {
                throw new DocumentConversionException("Parametros nulos o vacios");
            }

            // Obtenemos los tipos a partir del tipo mime
            log.debug("se  obtiene los tipos a partir del tipo mime");
            DocumentFormat formatoDocOrigen = new DefaultDocumentFormatRegistry().getFormatByMediaType(tipoMimeIn);
            DocumentFormat formatoDocDestino = new DefaultDocumentFormatRegistry().getFormatByMediaType(tipoMimeOut);
            
            // Comprobamos si existe el tipo mime
            if (formatoDocOrigen == null) {
                throw new FormatNotSupportedException(String.format("Tipo mime del documento origen no soportado: %s", tipoMimeIn));
            }
            if (formatoDocDestino == null) {
                throw new FormatNotSupportedException(String.format("Tipo mime del documento destino no soportado: %s", tipoMimeOut));
            }

            if (pdfA != null && !"".equals(pdfA) && "SI".equals(pdfA.toUpperCase())) {
                log.debug("se trata de un PDFA");
                Map map = new HashMap();
                map.put("FilterName", "writer_pdf_Export");
                PropertyValue[] aFilterData = new PropertyValue[1];
                aFilterData[0] = new PropertyValue();
                aFilterData[0].Name = "SelectPdfVersion";
                aFilterData[0].Value = 1;
                map.put("FilterData", aFilterData);
                formatoDocDestino.setStoreProperties(DocumentFamily.TEXT, map);
            }
            
            // Conversion
            log.debug("se realiza la conexion a OpenOffice");
            OfficeDocumentConverter converter = new OfficeDocumentConverter(connect());
            log.debug("se realiza la conversion");
            converter.convert(fileIn, fileOut, formatoDocDestino);
        } catch (OfficeException ex) {
            throw new DocumentConversionException("Error al convertir el documento", ex);
        }
    }

    public static void convertirDocumento(File fileIn, File fileOut) throws DocumentConversionException {
        try {
            // Comprobamos parametros
            if (fileIn == null || fileOut == null) {
                throw new DocumentConversionException("Parametros nulos o vacios");
            }

            // Conversion
            log.debug("se realiza la conexion a OpenOffice");
            OfficeDocumentConverter converter = new OfficeDocumentConverter(connect());
            log.debug("se realiza la conversion");
            converter.convert(fileIn, fileOut);
        } catch (OfficeException ex) {
            throw new DocumentConversionException("Error al convertir el documento", ex);
        }
    }

    public static byte[] convertirDocumentoAPdf(String directorioTemporal, byte[] ficheroOriginal, String tipoMimeOriginal, String pdfA)
            throws DocumentConversionException, FormatNotSupportedException, Exception {
        byte[] ficheroConvertido = null;
        File ficheroOriginalTemp = null;
        File ficheroDestinoTemp = null;

        try {
            log.debug("Accede al metodo que crea los directorios temporales");
            createTempDir(directorioTemporal);
            
            // Creando ficheros temporales
            SimpleDateFormat patronFecha = new SimpleDateFormat(DateOperations.DATE_TIME_FOR_FILE_WITH_MILIS);
            String fechaHora = patronFecha.format(Calendar.getInstance().getTime());
            String parteComun = String.format("%s%s%s%s",
                    directorioTemporal, File.separator, ConstantesDatos.PREFIJO_FICHERO_TMP_CSV, fechaHora);
            String tempFileNameOrigen = String.format("%s.%s",
                    parteComun, ConstantesDatos.EXTENSION_FICHERO_TMP_CSV);
            String tempFileNameDestino = String.format("%s%s.%s",
                    parteComun, ConstantesDatos.SUFIJO_FICHERO_TMP_CSV, ConstantesDatos.EXTENSION_FICHERO_TMP_CSV);
            
            if (log.isDebugEnabled()) {
                log.debug(String.format("Fichero temporal original: %s", tempFileNameOrigen));
                log.debug(String.format("Fichero temporal destino: %s", tempFileNameDestino));
            }
            
            ficheroOriginalTemp = new File(tempFileNameOrigen);
            ficheroDestinoTemp = new File(tempFileNameDestino);
            FileUtils.writeByteArrayToFile(ficheroOriginalTemp, ficheroOriginal);

            // Conversion de documento a PDF
            log.debug("Accede al metodo que crea los directorios temporales");
            convertirDocumento(ficheroOriginalTemp, tipoMimeOriginal, ficheroDestinoTemp, MimeTypes.PDF[0], pdfA);

            log.debug("Se obtiene el documento pdf");
            ficheroConvertido = FileUtils.readFileToByteArray(ficheroDestinoTemp);
        } catch (FormatNotSupportedException fnse) {
            throw fnse;
        } catch (DocumentConversionException dce) {
            throw dce;
        } catch (IOException ioe) {
            throw new DocumentConversionException("Error al intentar crear los ficheros temporales para la conversion de formato", ioe);
        } catch (Exception ex) {
            throw ex;
        } finally {
            FileUtils.deleteQuietly(ficheroOriginalTemp);
            FileUtils.deleteQuietly(ficheroDestinoTemp);
        }

        return ficheroConvertido;
    }
    
    public static void convertirDocumentoAPdf(File fileIn, File fileOut) throws DocumentConversionException {
        convertirDocumento(fileIn, fileOut);
    }

    public static void convertirDocumentoAPdf(File fileIn, String tipoMimeIn, File fileOut) throws DocumentConversionException, FormatNotSupportedException {
        convertirDocumento(fileIn, tipoMimeIn, fileOut, MimeTypes.PDF[0], "NO");
    }

    public static OfficeManager connect() throws OfficeException {
        synchronized (JODConverterHelper.class) {
            if (officeManagerInstance == null) {
                configureConnection();
                officeManagerInstance.start();
            }
        }

        return officeManagerInstance;
    }

    public static void configureConnection() {
        esGestionInterna = ConstantesDatos.SI.equalsIgnoreCase(config.getString("JODCONVERTER/GESTION_INTERNA"));
        setupConfiguration();

        if (esGestionInterna) {
            officeManagerInstance = getInternalOfficeManager();
        } else {
            officeManagerInstance = getExternalOfficeManager();
        }
    }

    public static void disconnect() throws OfficeException {
        synchronized (JODConverterHelper.class) {
            if (officeManagerInstance != null) {
                officeManagerInstance.stop();
                officeManagerInstance = null;
            }
        }
    }

    private static OfficeManager getInternalOfficeManager() {
        OfficeManager officeManager = new DefaultOfficeManagerConfiguration()
                // either socket or pipe, pipe is faster but requires native libraries
                .setConnectionProtocol(OfficeConnectionProtocol.SOCKET)
                // the installation directory of the openoffice installation
                .setOfficeHome(officeHome)
                // the amount of ports in the array determines how many instances are managed
                .setPortNumbers(ports)
                // after how many tasks the process is restarted as a work around to the known memory leaks in openoffice
                .setMaxTasksPerProcess(maxTasksPerProcess)
                // how long a task can run before it is considered timed out, default is 2 min
                .setTaskExecutionTimeout(taskExecutionTimeout)
                // how long the code waits for a process to become available, default is 30 seconds
                .setTaskQueueTimeout(taskQueueTimeout)
                .buildOfficeManager();

        return officeManager;
    }

    private static OfficeManager getExternalOfficeManager() {
        OfficeManager officeManager = new ExternalOfficeManagerConfiguration()
                .setConnectOnStart(connectOnStart)
                .setPortNumber(ports[0])
                .buildOfficeManager();

        return officeManager;
    }

    private static void setupConfiguration() {
        if (esGestionInterna) {
            String puertosCadena = config.getString("JODCONVERTER/GESTION_INTERNA/PORTS");
            String[] puertosCadenaArray = StringUtils.split(puertosCadena, ";");
            ports = new int[puertosCadenaArray.length];
            for (int i = 0; i < puertosCadenaArray.length; i++) {
                ports[i] = Integer.parseInt(puertosCadenaArray[i]);
            }
            maxTasksPerProcess = Integer.parseInt(config.getString("JODCONVERTER/GESTION_INTERNA/MAX_TASK_PER_PROCESS"));
            taskExecutionTimeout = Long.parseLong(config.getString("JODCONVERTER/GESTION_INTERNA/TASK_EXECUTION_TIMEOUT"));
            taskQueueTimeout = Long.parseLong(config.getString("JODCONVERTER/GESTION_INTERNA/TASK_QUEUE_TIMEOUT"));
            officeHome = config.getString("JODCONVERTER/GESTION_INTERNA/OFFICEHOME");
            
            if (log.isDebugEnabled()) {
                log.debug("PARAMETROS:");
                log.debug(String.format("Modo Gestion Interna"));
                log.debug(String.format("Puertos: %s", puertosCadena));
                log.debug(String.format("maxTasksPerProcess: %d", maxTasksPerProcess));
                log.debug(String.format("taskExecutionTimeout: %d", taskExecutionTimeout));
                log.debug(String.format("taskQueueTimeout: %d", taskQueueTimeout));
                log.debug(String.format("officeHome: %s", officeHome));
            }
        } else {
            ports = new int[1];
            ports[0] = Integer.parseInt(config.getString("JODCONVERTER/GESTION_EXTERNA/PORT"));
            connectOnStart = config.getBoolean("JODCONVERTER/GESTION_EXTERNA/CONNECT_ON_START");

            if (log.isDebugEnabled()) {
                log.debug("PARAMETROS:");
                log.debug(String.format("Modo Gestion Externa"));
                log.debug(String.format("Puerto: %d", ports[0]));
                log.debug(String.format("connectOnStart: %b", connectOnStart));
            }
        }
    }

    private static void createTempDir(String directorioTemporal) throws IOException {
        if (StringUtils.isNotEmpty(directorioTemporal)) {
            File dirTemporal = new File(directorioTemporal);
            if (!dirTemporal.exists()) {
                dirTemporal.mkdirs();
            } else if (!dirTemporal.isDirectory()) {
                throw new IOException(String.format("La ruta temporal no es un directorio. %s", directorioTemporal));
            }
        }
    }
}
