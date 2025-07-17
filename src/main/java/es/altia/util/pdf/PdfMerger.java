package es.altia.util.pdf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PdfMerger {
    private static final Log log = LogFactory.getLog(PdfMerger.class.getName());
    
    /**
     * Une en único archivo pdf varios pdfs
     * @param pdfs Lista con los flujos de datos de cada pdf independiente
     * @param outputStream Flujo de datos del pdf resultante
     * @param paginate Indicar o no la página correspondiente en el pdf resultante
     */
    public static void concatPDFs(List<InputStream> pdfs, OutputStream outputStream, boolean paginate) {
        log.info("PdfMerger.concatPDFs::BEGIN");
        Document document = new Document();
        try {
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();
 
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
 
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
 
            document.open();
            PdfContentByte cb = writer.getDirectContent();
 
            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();
 
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();
 
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
 
                    
                    document.newPage();
 
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                    if (paginate) {
                        cb.beginText();
                        cb.getPdfDocument().getPageSize();
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
            log.info("PdfMerger.concatPDFs::END");
        } catch (Exception e) {
            log.error("PdfMerger.concatPDFs: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
