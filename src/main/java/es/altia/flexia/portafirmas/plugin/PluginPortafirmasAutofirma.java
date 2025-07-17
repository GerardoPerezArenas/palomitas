package es.altia.flexia.portafirmas.plugin;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.x509.certificados.Interpretador;
import es.altia.x509.certificados.InterpretadorException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import xades4j.XAdES4jException;
import xades4j.providers.CertificateValidationProvider;
import xades4j.providers.impl.PKIXCertificateValidationProvider;
import xades4j.verification.XAdESVerificationResult;
import xades4j.verification.XadesVerificationProfile;
import xades4j.verification.XadesVerifier;

public class PluginPortafirmasAutofirma implements PluginPortafirmas {

    private final Logger log = Logger.getLogger(PluginPortafirmasAutofirma.class);

    /**
     * Verificar la firma
     * 
     * En caso de Pades y Xades la firma esta dentro del contenido firmado. Si es Cades se
     * considera que viene el contenido firmado separado de la firma.
     *
     * @param documento
     * @return
     * @throws TechnicalException
     */
    @Override
    public boolean verificarFirma(DocumentoFirmadoVO documento) throws TechnicalException {

        if (log.isDebugEnabled()) {
            log.debug("verificarFirma");
            log.debug("documento:");
            log.debug(String.format("tipoMime = %s", documento.getTipoMime()));
            log.debug(String.format("firma = %s", documento.getFirma()));
            log.debug(String.format("ficheroHash64 = %s", documento.getFicheroHash64()));
            log.debug(String.format("ficheroFirma is null = %b", documento.getFicheroFirma() != null));
        }

        boolean firmaValida = true;
        ByteArrayInputStream bytesFirma = null;

        try {
            bytesFirma = new ByteArrayInputStream(getFirmaDescodificada(documento));

            // Cargamos el almacen de claves
            KeyStore keyStore = cargarKeystoreFirmaDocumentos();

            // Comprobamos el tipo del fichero para determinar el tipo
            // de verificacion de firma a realizar
            String tipoMime = documento.getTipoMime();

            // Para PDFs
            if (MimeTypes.PDF[0].equalsIgnoreCase(tipoMime)) {
                firmaValida = verificarFirmaPdf(bytesFirma, keyStore);
                // Para XMLs
            } else if (MimeTypes.XML[0].equalsIgnoreCase(tipoMime) || MimeTypes.XML[1].equalsIgnoreCase(tipoMime)) {
                firmaValida = verificarFirmaXml(bytesFirma, keyStore);
                // Para Resto de ficheros
            } else {
                firmaValida = verificarFirmaCades(bytesFirma, documento.getFicheroFirma(), keyStore);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            firmaValida = false;
        } finally {
            IOUtils.closeQuietly(bytesFirma);
        }

        return firmaValida;
    }

    /**
     * Verificar la firma para ficheros PDF
     *
     * @param bytesDoc
     * @param keyStore
     * @return
     * @throws IOException
     * @throws SignatureException
     */
    private boolean verificarFirmaPdf(ByteArrayInputStream bytesDoc, KeyStore keyStore)
            throws IOException, SignatureException {

        log.debug("verificarFirmaPdf");

        PdfReader pdfReader = null;

        try {
            pdfReader = new PdfReader(bytesDoc);
            AcroFields acroFields = pdfReader.getAcroFields();

            // No hay firmas
            List<String> nombreFirmas = acroFields.getSignatureNames();
            if (nombreFirmas.isEmpty()) {
                log.info("El documento no contiene ninguna firma");
                return false;
            }

            for (String nombre : nombreFirmas) {
                // Comprobar si la firma cubre todo el documento
                if (!acroFields.signatureCoversWholeDocument(nombre)) {
                    log.info(String.format("La firma no cubre todo el documento: %s", nombre));
                    return false;
                }

                PdfPKCS7 pk = acroFields.verifySignature(nombre, ConstantesDatos.SECURITY_PROVIDER_BOUNCYCASTLE);

                // Comprobar si se ha alterado el documento despues de la firma
                if (!pk.verify()) {
                    log.info(String.format("El documento ha sido modificado despues de la firma: %s", nombre));
                    return false;
                }

                // Comprobar certificados offline
                Calendar cal = pk.getSignDate();
                Certificate[] certificates = pk.getCertificates();
                Object fails[] = PdfPKCS7.verifyCertificates(certificates, keyStore, null, cal);

                if (fails != null) {
                    log.info(String.format("Certificados no validos para la firma: %s", nombre));
                    return false;
                }
            }

            return true;
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }
        }
    }

    /**
     * Verificar la firma para ficheros XML. Solo se verifican firmas
     * Xades Enveloping, de momento.
     *
     * @param firmaBytes
     * @param keyStore
     * @return
     * @throws TechnicalException
     */
    private boolean verificarFirmaXml(ByteArrayInputStream firmaBytes, KeyStore keyStore)
            throws TechnicalException {

        log.debug("verificarFirmaXml");

        boolean firmaValida = firmasValidas(getFirmaInfoXml(firmaBytes, keyStore));
        
        if (log.isDebugEnabled()) {
            log.debug(String.format("Firma es valida? -> %b", firmaValida));
        }
        
        return firmaValida;
    }

    private boolean firmasValidas(List<FirmaVO> firmas) {
        boolean firmaValida = true;

        if (firmas != null && !firmas.isEmpty()) {
            for (FirmaVO firma : firmas) {
                if (!firma.getValido()) {
                    firmaValida = false;
                    break;
                }
            }
        }
        
        return firmaValida;
    }
    
    /**
     * Verificar y obtener datos de la firma
     *
     * @param documento
     * @return
     * @throws TechnicalException
     */
    @Override
    public ArrayListFirmasVO verificarFirmaInfo(DocumentoFirmadoVO documento) throws TechnicalException {

        log.debug("verificarFirmaInfo");

        ArrayListFirmasVO salida = new ArrayListFirmasVO();
        salida.setGlobalValidity(false);
        PdfReader pdfReader = null;
        ByteArrayInputStream bytesFirma = null;

        try {
            // Cargamos el almacen de claves
            KeyStore keyStore = cargarKeystoreFirmaDocumentos();
            bytesFirma = new ByteArrayInputStream(getFirmaDescodificada(documento));

            // Comprobamos el tipo del fichero para determinar el tipo
            // de verificacion de firma a realizar
            List<FirmaVO> firmas = null;
            String tipoMime = documento.getTipoMime();

            // Para PDFs
            if (MimeTypes.PDF[0].equalsIgnoreCase(tipoMime)) {
                boolean validezFirma = verificarFirmaPdf(bytesFirma, keyStore);
                bytesFirma.reset();
                firmas = getFirmaInfoPdf(bytesFirma, validezFirma);
            } else if (MimeTypes.XML[0].equalsIgnoreCase(tipoMime) || MimeTypes.XML[1].equalsIgnoreCase(tipoMime)) {
                firmas = getFirmaInfoXml(bytesFirma, keyStore);
            } else {
                firmas = getFirmaInfoCades(bytesFirma, documento.getFicheroFirma(), keyStore);
            }

            salida.addAll(firmas);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================ > " + this.getClass().getName() + " ERROR: " + e.getMessage());
            salida.setResultadoVerificacion(e.getMessage());
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }

            IOUtils.closeQuietly(bytesFirma);
        }

        return salida;
    }

    /**
     * Obtiene la firma descodificada.
     *
     * @param documento
     * @return
     * @throws UnsupportedEncodingException
     * @throws TechnicalException
     */
    private byte[] getFirmaDescodificada(DocumentoFirmadoVO documento)
            throws UnsupportedEncodingException, TechnicalException {
        byte[] firmaDescodificada = null;

        String ficheroFirma = documento.getFirma();
        // La firma viene en base64 es necesario decodificarlo antes de la verificacion
        if (ficheroFirma != null && !ficheroFirma.isEmpty()) {
            firmaDescodificada = Base64.decode(ficheroFirma.getBytes(ConstantesDatos.CHARSET_UTF_8));
        } else {
            throw new TechnicalException("No se encuentra la firma para verificar");
        }

        return firmaDescodificada;
    }

    /**
     * Extrae la informacion de las firmas para archivos PDF
     *
     * @param bytesDoc
     * @return
     * @throws IOException
     */
    private List<FirmaVO> getFirmaInfoPdf(ByteArrayInputStream bytesDoc, boolean validezFirma)
            throws IOException, InterpretadorException {

        log.debug("getFirmaInfoPdf");

        PdfReader pdfReader = null;
        List<FirmaVO> firmas = new ArrayList<FirmaVO>();

        try {
            pdfReader = new PdfReader(bytesDoc);
            AcroFields acroFields = pdfReader.getAcroFields();

            FirmaVO firmaVO = null;
            PdfPKCS7 pk = null;
            X509Certificate certificado = null;
            List<String> nombreFirmas = acroFields.getSignatureNames();
            for (String nombre : nombreFirmas) {
                pk = acroFields.verifySignature(nombre, ConstantesDatos.SECURITY_PROVIDER_BOUNCYCASTLE);
                certificado = pk.getSigningCertificate();

                firmaVO = extraerInfoCertificado(certificado);
                firmaVO.setValido(validezFirma);
                firmas.add(firmaVO);
            }
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }
        }

        return firmas;
    }

    /**
     * Extrae la informacion de las firmas para archivos XML
     *
     * @param bytesDoc
     * @param validezFirma
     * @return
     * @throws IOException
     */
    private List<FirmaVO> getFirmaInfoXml(ByteArrayInputStream bytesDoc, KeyStore keyStore)
            throws TechnicalException {

        log.debug("getFirmaInfoXml");

        FirmaVO firmaVO = null;
        List<FirmaVO> firmas = new ArrayList<FirmaVO>();

        try {
            CertificateValidationProvider certValidator = new PKIXCertificateValidationProvider(
                    keyStore, false, new CertStore[0]);

            // Construimos los verificadores con el almacen de certificados especificado
            XadesVerificationProfile perfilVerificacion = new XadesVerificationProfile(certValidator);
            perfilVerificacion.acceptUnknownProperties(true);
            XadesVerifier verificador = perfilVerificacion.newVerifier();

            // OJO: Este debug manipula el ByteArrayInputStream, por lo que se resetea despues de
            // logear el contenido para que lo pueda leer el parseador de documentos DOM
            if (log.isDebugEnabled()) {
                log.debug("XML a procesar:");
                log.debug(new String(IOUtils.toByteArray(bytesDoc), ConstantesDatos.CHARSET_UTF_8));
                bytesDoc.reset();
            }

            // Obtenemos el documento a verificar a partir del array de bytes
            DocumentBuilderFactory docBuilderFactoria = DocumentBuilderFactory.newInstance();
            docBuilderFactoria.setNamespaceAware(true);
            DocumentBuilder docBuilder = docBuilderFactoria.newDocumentBuilder();
            Document documento = docBuilder.parse(bytesDoc);

            // Obtenemos el nodo de la firma
            Element elemento = documento.getDocumentElement();
            if (elemento != null) {
                // Si el primer elemento no es la firma, se procede a buscarla
                if (!XMLSignature.XMLNS.equals(elemento.getNamespaceURI()) || !elemento.getNodeName().endsWith("Signature")) {
                    NodeList nodeList = elemento
                            .getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
                    if (nodeList != null && nodeList.getLength() > 0) {
                        elemento = (Element) nodeList.item(0);
                    } else {
                        throw new TechnicalException("El documento no contiene una firma XML");
                    }
                }
            } else {
                throw new TechnicalException("El documento no contiene un XML valido");
            }

            try {
                XAdESVerificationResult resultado = verificador.verify(elemento, null);
                firmaVO = extraerInfoCertificado(resultado.getValidationCertificate());
                firmaVO.setValido(true);
            } catch (XAdES4jException ex) {
                log.warn(String.format("Ha fallado la validacion de la firma del XML: ", ex.getMessage()));
                firmaVO = new FirmaVO();
                firmaVO.setValido(false);
            }
            
            firmas.add(firmaVO);
        } catch (TechnicalException te) {
            throw te;
        } catch (Exception e) {
            throw new TechnicalException("Error al intentar verificar la firma XML", e);
        }

        return firmas;
    }

    /**
     * Verificar la firma para ficheros con firma CADES
     * 
     * @param firmaBytes
     * @param fichero
     * @param keyStore
     * @return 
     * @throws TechnicalException 
     */
    private boolean verificarFirmaCades(ByteArrayInputStream firmaBytes, File fichero, KeyStore keyStore)
            throws TechnicalException {
        log.debug("verificarFirmaCades");
        
        boolean firmaValida = firmasValidas(getFirmaInfoCades(firmaBytes, fichero, keyStore));
        
        if (log.isDebugEnabled()) {
            log.debug(String.format("Firma es valida? -> %b", firmaValida));
        }
        
        return firmaValida;
    }
    
    /**
     * Extrae la informacion de las firmas para archivos Cades
     * 
     * @param firma
     * @param fichero
     * @param keyStore
     * @return
     * @throws TechnicalException 
     */
    private List<FirmaVO> getFirmaInfoCades(ByteArrayInputStream firma, File fichero, KeyStore keyStore)
            throws TechnicalException {

        log.debug("getFirmaInfoCades");

        FirmaVO firmaVO = null;
        List<FirmaVO> firmas = new ArrayList<FirmaVO>();
        boolean firmaValida = false;
        
        try {
            JcaX509CertificateConverter x509CertificateConverter
                    = new JcaX509CertificateConverter().setProvider(ConstantesDatos.SECURITY_PROVIDER_BOUNCYCASTLE);
            JcaSimpleSignerInfoVerifierBuilder simpleSignerInfoVerifierBuilder
                    = new JcaSimpleSignerInfoVerifierBuilder().setProvider(ConstantesDatos.SECURITY_PROVIDER_BOUNCYCASTLE);

            CMSProcessable contenido = new CMSProcessableByteArray(FileUtils.readFileToByteArray(fichero));
            CMSSignedData signedData = new CMSSignedData(contenido, IOUtils.toByteArray(firma));
            SignerInformationStore signers = signedData.getSignerInfos();
            Iterator<SignerInformation> it = signers.getSigners().iterator();

            while (it.hasNext()) {
                SignerInformation signer = it.next();
                Collection certCollection = signedData.getCertificates().getMatches(signer.getSID());
                Iterator<X509CertificateHolder> certIt = certCollection.iterator();
                X509CertificateHolder certHolder = certIt.next();
                X509Certificate certificado = x509CertificateConverter.getCertificate(certHolder);

                firmaValida = signer.verify(simpleSignerInfoVerifierBuilder.build(certificado));
                
                firmaVO = extraerInfoCertificado(certificado);
                firmaVO.setValido(firmaValida);
                firmas.add(firmaVO);
            }
        } catch (InterpretadorException ex) {
            log.error("No se pudo extraer la informacion del certificado", ex);
            throw new TechnicalException("No se pudo extraer la informacion del certificado", ex);
        } catch (CertificateException ex) {
            log.error("Error en el formato del certificado", ex);
            throw new TechnicalException("Error en el formato del certificado", ex);
        } catch (CMSException ex) {
            log.error("Error al intentar verificar la firma", ex);
            throw new TechnicalException("Error al intentar verificar la firma", ex);
        } catch (IOException ex) {
            log.error("Error al leer la firma o el contenido del fichero", ex);
            throw new TechnicalException("Error al leer la firma o el contenido del fichero", ex);
        } catch (OperatorCreationException ex) {
            log.error("Error al intentar verificar la firma", ex);
            throw new TechnicalException("Error al intentar verificar la firma", ex);
        }
        
        return firmas;
    }

    /**
     * Extrae la informacion de un certificado X509Certificate y la inserta en 
     * un objeto FirmaVO.
     * 
     * @param certificado
     * @return
     * @throws InterpretadorException 
     */
    private FirmaVO extraerInfoCertificado(X509Certificate certificado) throws InterpretadorException {
        FirmaVO firmaVO = new FirmaVO();
        
        if (certificado != null) {
            DateFormat dateFormatter = new SimpleDateFormat(DateOperations.DATE_TIME_WITH_TIMEZONE_OFFSET, new Locale("es", "ES"));
            Interpretador interprete = new Interpretador(certificado);

            firmaVO.setAsuntoCertificado(StringUtils.trim(certificado.getSubjectDN().toString()));
            firmaVO.setNif(StringUtils.trim(interprete.getNif()));
            firmaVO.setNombrePersona(es.altia.agora.interfaces.user.web.util.FormateadorTercero.concatenarNombre(interprete.getNombre(), interprete.getApellido1(), interprete.getApellido2()));
            firmaVO.setEmisorCertificado(StringUtils.trim(certificado.getIssuerDN().toString()));
            firmaVO.setValidez(StringUtils.trim(dateFormatter.format(certificado.getNotAfter())));
        }
        
        return firmaVO;
    }
    
    /**
     * Este metodo no se utiliza, puesto que la firma se realiza con una
     * libreria de javascript en las pantallas (Miniapplet.js)
     *
     * @param documento
     * @return
     * @throws TechnicalException
     */
    @Override
    public String firmaServidor(byte[] documento) throws TechnicalException {
        throw new TechnicalException("Operacion no soportada");
    }

    /**
     * Cargar el almacen de claves con las claves publicas para la validacion
     *
     * @return
     * @throws TechnicalException
     */
    private KeyStore cargarKeystoreFirmaDocumentos() throws TechnicalException {

        log.debug("cargarKeystoreFirmaDocumentos");

        KeyStore keyStore = null;
        FileInputStream file = null;

        try {
            // Si no existe el proveedor de bouncyCastle se añade
            if (Security.getProvider(ConstantesDatos.SECURITY_PROVIDER_BOUNCYCASTLE) == null) {
                Class provider = Class.forName(ConstantesDatos.SECURITY_PROVIDER_BOUNCYCASTLE_CLASS);
                Security.addProvider((Provider) provider.newInstance());
            }

            // Obtenemos el almacen de claves para verificar la firma Si no existe se carga el que viene por defecto en java
            Config configPortafirmas = ConfigServiceHelper.getConfig("Portafirmas");
            String rutaKeystore = configPortafirmas.getString("PluginPortafirmas/AUTOFIRMA/keyStorePath");
            String passwordKeystore = configPortafirmas.getString("PluginPortafirmas/AUTOFIRMA/keyStorePassword");

            if (!StringUtils.isEmpty(rutaKeystore)) {
                file = new FileInputStream(rutaKeystore);
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(file, (passwordKeystore != null) ? passwordKeystore.toCharArray() : null);
            } else {
                keyStore = PdfPKCS7.loadCacertsKeyStore();
            }
        } catch (Exception ex) {
            log.error("No se pudo cargar el almacen de claves", ex);
            throw new TechnicalException("No se pudo cargar el almacen de claves", ex);
        } finally {
            IOUtils.closeQuietly(file);
        }

        return keyStore;
    }
}
