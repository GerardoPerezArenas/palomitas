package es.altia.util.commons;

import es.altia.common.exception.DocumentConversionException;
import es.altia.util.exceptions.JODReportsException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JODReportsOperations {

    private static final Log log = LogFactory.getLog(JODReportsOperations.class.getName());
    private String templatePath;
    private String documentPath;
    private String documentPdfPath;
    private Map datos;

    private JODReportsOperations() {
    }

    public JODReportsOperations(String pathTemplate, String pathDocument, Map datos) {
        this.templatePath = pathTemplate;
        this.documentPath = pathDocument;
        this.datos = datos;
    }

    /**
     * @return the pathTemplate
     */
    public String getPathTemplate() {
        return templatePath;
    }

    /**
     * @param pathTemplate the pathTemplate to set
     */
    public void setPathTemplate(String pathTemplate) {
        this.templatePath = pathTemplate;
    }

    /**
     * @return the pathDocument
     */
    public String getPathDocument() {
        return documentPath;
    }

    /**
     * @param pathDocument the pathDocument to set
     */
    public void setPathDocument(String pathDocument) {
        this.documentPath = pathDocument;
    }

    /**
     * @return the datos
     */
    public Map getDatos() {
        return datos;
    }

    /**
     * @param datos the datos to set
     */
    public void setDatos(Map datos) {
        this.datos = datos;
    }

    public void createOdtDocument() throws JODReportsException {
        log.debug("JODReportsOperations.createOdtDocument");

        try {
            DocumentTemplateFactory plantillaFactoria = new DocumentTemplateFactory();
            DocumentTemplate plantilla = plantillaFactoria.getTemplate(new File(this.templatePath));

            log.debug("datos que pasamos a la plantilla: " + this.datos);
            log.debug("ruta del documento: " + this.documentPath);

            plantilla.createDocument(this.datos, new FileOutputStream(this.documentPath));
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            throw new JODReportsException(ioe.getMessage());
        } catch (DocumentTemplateException dte) {
            log.error(dte.getMessage());
            throw new JODReportsException(dte.getMessage());
        }
    }

    public void createPdfDocument() throws JODReportsException, DocumentConversionException {
        log.debug("JODReportsOperations.createPdfDocument");

        createOdtDocument();
        try {
            JODConverterHelper.convertirDocumentoAPdf(new File(this.documentPath), new File(this.documentPdfPath));
        } catch (DocumentConversionException ex) {
            log.error("Error al convertir el documento a pdf");
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error generico al convertir el documento a pdf");
        }
    }

    /**
     * @return the documentPdfPath
     */
    public String getDocumentPdfPath() {
        return documentPdfPath;
    }

    /**
     * @param documentPdfPath the documentPdfPath to set
     */
    public void setDocumentPdfPath(String documentPdfPath) {
        this.documentPdfPath = documentPdfPath;
    }
}
