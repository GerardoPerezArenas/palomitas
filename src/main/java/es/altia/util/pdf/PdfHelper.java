package es.altia.util.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import es.altia.common.exception.TechnicalException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

public class PdfHelper {

    private static final Log log = LogFactory.getLog(PdfHelper.class.getName());

    /**
     * Incrusta un texto informativo en un lateral en todas las paginas de un
     * PDF
     *
     * @param in flujo de datos de entrada
     * @param out flujo de datos de salida
     * @param textos textos a insertar y cada elemento sera una linea nueva
     * @param alto alto del contenedor
     * @param ancho ancho del contenedor
     * @param posXCentro posicion absoluta de la coordenada X del centro del
     * contenedor. Si es < 0 se toma la mitad del ancho de la pagina @param
     * posYCentro posicion absoluta
     * de la coordenada Y del centro del contenedor. Si es < 0 se toma la mitad
     * del alto de la pagina @param rotacion rotacion en grados
     * @param offsetPosXTexto distancia en pixeles de los textos con respecto al
     * parametro posX
     * @param tamanoFuente tamano de la fuente
     * @param superpuesto si se quiere sobreponer al contenido
     * @param pintarBordes si se quiere pintar los bordes
     * @throws DocumentException
     * @throws IOException
     */
    public static void incrustarTextoLateralPDF(InputStream in, OutputStream out,
            List<String> textos, float alto, float ancho, float posXCentro, float posYCentro, float rotacion,
            float offsetPosXTexto, float tamanoFuente,
            boolean superpuesto, boolean pintarBordes)
            throws DocumentException, IOException {

        PdfReader pdfReader = null;
        PdfStamper pdfStamper = null;

        if (log.isDebugEnabled()) {
            log.debug("incrustarTextoLateralPDF");
            log.debug("PARAMETROS:");
            log.debug(String.format("textos: [%s]", StringUtils.join(textos, ", ")));
            log.debug(String.format("alto: [%f]", alto));
            log.debug(String.format("ancho: [%f]", ancho));
            log.debug(String.format("posXCentro: [%f]", posXCentro));
            log.debug(String.format("posYCentro: [%f]", posYCentro));
            log.debug(String.format("rotacion: [%f]", rotacion));
            log.debug(String.format("offsetPosXTexto: [%f]", offsetPosXTexto));
            log.debug(String.format("tamanoFuente: [%f]", tamanoFuente));
            log.debug(String.format("superpuesto: [%b]", superpuesto));
            log.debug(String.format("pintarBordes: [%b]", pintarBordes));

        }

        try {
            PdfContentByte canvas = null;
            PdfTemplate textTemplate = null;
            Paragraph frase = null;
            ColumnText ct = null;
            Image textImg = null;
            float posX = 0;
            float posY = 0;
            float posXCentroCalculado = 0;
            float posYCentroCalculado = 0;
            double radianes = rotacion * Math.PI / 180D;
            float altoPagina = 0;
            float anchoPagina = 0;

            pdfReader = new PdfReader(in);
            pdfStamper = new PdfStamper(pdfReader, out);

            if (log.isDebugEnabled()) {
                log.debug(String.format("Numero de paginas: [%d]", pdfReader.getNumberOfPages()));
            }
            
            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
                posXCentroCalculado = posXCentro;
                posYCentroCalculado = posYCentro;
                altoPagina = pdfReader.getPageSizeWithRotation(i).getHeight();
                anchoPagina = pdfReader.getPageSizeWithRotation(i).getWidth();

                // Si una parte de la coordenada es inferior a 0 se tomara
                // el punto central en esa pagina para esa coordenada
                if (posXCentroCalculado < 0) {
                    posXCentroCalculado = anchoPagina / 2;
                }

                if (posYCentroCalculado < 0) {
                    posYCentroCalculado = altoPagina / 2;
                }

                // Calculo de la posicion del punto inicial del rectangulo (abajo a la izquierda)
                posX = (float) (posXCentroCalculado - (ancho / 2) * Math.cos(radianes) - (alto / 2) * Math.sin(radianes));
                posY = (float) (posYCentroCalculado + (alto / 2) * Math.cos(radianes) - (ancho / 2) * Math.sin(radianes));

                if (log.isDebugEnabled()) {
                    log.debug(String.format("Ancho pagina: [%f]", anchoPagina));
                    log.debug(String.format("Alto pagina: [%f]", altoPagina));
                    log.debug(String.format("posXCentro calculado: [%f]", posXCentroCalculado));
                    log.debug(String.format("posYCentro calculado: [%f]", posYCentroCalculado));
                    log.debug(String.format("posX: [%f]", posX));
                    log.debug(String.format("posY: [%f]", posY));
                }

                // Creacion del recuadro de texto
                if (superpuesto) {
                    canvas = pdfStamper.getOverContent(i);
                } else {
                    canvas = pdfStamper.getUnderContent(i);
                }

                textTemplate = canvas.createTemplate(ancho, alto);
                ct = new ColumnText(textTemplate);
                for (String texto : textos) {
                    frase = new Paragraph(texto, FontFactory.getFont(BaseFont.HELVETICA_BOLD, tamanoFuente));
                    frase.setAlignment(Element.ALIGN_LEFT);
                    frase.setLeading(tamanoFuente);
                    ct.setSimpleColumn(frase, offsetPosXTexto, 0, ancho, alto, 0, Element.ALIGN_LEFT);
                    ct.addElement(frase);
                }
                ct.go();

                // Creamos una imagen e insertamos el recuadro con el texto.
                // Esto se hace para poder rotarlo e incrustarlo en el lateral
                textImg = Image.getInstance(textTemplate);
                textImg.setInterpolation(true);
                textImg.scaleAbsolute(ancho, alto);
                textImg.setRotationDegrees(rotacion);
                textImg.setAbsolutePosition(posX, posY);
                if (pintarBordes) {
                    textImg.setBorder(Rectangle.BOX);
                    textImg.setBorderWidth(1);
                }

                canvas.addImage(textImg);
            }
        } catch (IOException ioe) {
            log.error("Error al intentar incrustar un texto en el pdf", ioe);
            throw ioe;
        } catch (DocumentException de) {
            log.error("Error al intentar incrustar un texto en el pdf", de);
            throw de;
        } finally {
            try {
                if (pdfStamper != null) {
                    pdfStamper.close();
                }
            } catch (Exception e) {
                log.error("Error al intentar cerrar el pdfStamper", e);
            }

            try {
                if (pdfReader != null) {
                    pdfReader.close();
                }
            } catch (Exception e) {
                log.error("Error al intentar cerrar el pdfReader", e);
            }
        }
    }

    /**
     * Aplica un escalado centrado del contenido del documento PDF de origen.
     * @param in
     * @param out
     * @param escala
     * @throws DocumentException
     * @throws IOException
     */
    public static void ajustarPaginaPDF(InputStream in, OutputStream out, float escala)
            throws DocumentException, IOException {

        if (log.isDebugEnabled()) {
            log.debug("ajustarPaginaPDF");
            log.debug("PARAMS:");
            log.debug(String.format("escala = %f", escala));
            log.debug(String.format("in es nulo? = %b", in == null));
            log.debug(String.format("out es nulo? = %b", out == null));
        }
        
        PDDocument document = null;
        PDPageContentStream contentStream = null;

        try {
            document = new PDDocument();

            // Cargamos el documento a convertir
            log.debug("Cargando documento a ajustar");
            PDDocument documentoImportado = PDDocument.load(in);

            // Importamos la informacion del documento
            log.debug("Importando metadatos del documento");
            document.setDocumentInformation(documentoImportado.getDocumentInformation());
            document.setDocumentId(documentoImportado.getDocumentId());

            // Importamos las paginas y las adaptamos al tamano requerido
            PDPage paginaPDF = null;
            PDRectangle rectangle = null;
            float offsetX = 0;
            float offsetY = 0;
            
            int numPaginas = documentoImportado.getNumberOfPages();

            if (log.isDebugEnabled()) {
                log.debug(String.format("Paginas totales = %d", numPaginas));
            }
            
            for (int i = 0; i < numPaginas; i++) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Pagina = %d", i));
                }
                
                paginaPDF = documentoImportado.getPage(i);

                // Adaptamos los recuadros al area visible
                rectangle = paginaPDF.getMediaBox();
                paginaPDF.setMediaBox(rectangle);
                paginaPDF.setArtBox(rectangle);
                paginaPDF.setCropBox(rectangle);
                paginaPDF.setTrimBox(rectangle);

                // Escalado del contenido de la pagina
                try {
                    offsetX = rectangle.getWidth() * (1 - escala) / 2;
                    offsetY = rectangle.getHeight() * (1 - escala) / 2;

                    if (log.isDebugEnabled()) {
                        log.debug(String.format("ancho = %f", rectangle.getWidth()));
                        log.debug(String.format("alto = %f", rectangle.getHeight()));
                        log.debug(String.format("offsetX = %f", offsetX));
                        log.debug(String.format("offsetY = %f", offsetY));
                    }
                    
                    contentStream = new PDPageContentStream(
                            document, paginaPDF, PDPageContentStream.AppendMode.PREPEND, false);
                    contentStream.transform(Matrix.getTranslateInstance(offsetX, offsetY));
                    contentStream.transform(Matrix.getScaleInstance(escala, escala));
                } finally {
                    if (contentStream != null) {
                        contentStream.close();
                    }
                }

                // Se anade la pagina al documento nuevo
                log.debug("Anadiendo pagina escalada");
                document.addPage(paginaPDF);
            }

            // Grabamos el documento en el flujo de salida
            log.debug("Grabando documento modificado");
            document.save(out);
        } catch (IOException ioe) {
            log.error(String.format("No se pudo escalar el contenido del documento PDF: %s", ioe.getMessage()));
            throw ioe;
        } catch (Exception e) {
            log.error(String.format("No se pudo escalar el contenido del documento PDF: %s", e.getMessage()));
            throw new TechnicalException("No se pudo escalar el contenido del documento PDF", e);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}
