package es.altia.flexia.portafirmas.plugin;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.x509.certificados.validacion.ValidacionCertificado;
import org.apache.log4j.Logger;

//import es.altia.x509.certificados.Interpretador;
//import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
//import javax.transaction.NotSupportedException;
//
//import java.io.File;
//import java.security.Security;
//import java.security.cert.CertStore;
//import java.security.cert.X509Certificate;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.ResourceBundle;
//import java.util.StringTokenizer;
//import org.bouncycastle.cms.CMSProcessableFile;
//import org.bouncycastle.cms.CMSSignedData;
//import org.bouncycastle.cms.SignerInformation;
//import org.bouncycastle.cms.SignerInformationStore;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.util.encoders.Base64;



public class PluginPortafirmasFlexia implements PluginPortafirmas{

    private Logger log = Logger.getLogger(PluginPortafirmasFlexia.class);
    
    public boolean verificarFirma(DocumentoFirmadoVO documento) throws TechnicalException{
        
        ValidacionCertificado validar = new ValidacionCertificado();
        return validar.verificarFirma(documento.getFicheroFirma(), documento.getFirma());
        
    }
    
    
    public ArrayListFirmasVO verificarFirmaInfo(DocumentoFirmadoVO documento) throws TechnicalException{
        throw new TechnicalException("OPERACION NO SOPORTADA");
        // NO SOPORTADO POR LAS NUEVAS LIBRERIAS
//        ArrayListFirmasVO salida = new ArrayListFirmasVO();
//        salida.setGlobalValidity(false);
//        try{
//            FirmaVO firmaVO = new FirmaVO();
//            
//            
//            ValidacionCertificado validar = new ValidacionCertificado();
//            log.debug("==========documento.getFicheroFirma() "+documento.getFicheroFirma());
//             log.debug("==========documento.getFirma() "+documento.getFirma());
//            
//            firmaVO.setValido(validar.verificarFirma(documento.getFicheroFirma(), documento.getFirma()));
//            firmaVO.setValido(true);
//            
//            
//            CMSProcessableFile datos = new CMSProcessableFile(documento.getFicheroFirma());
//            
//            byte[] bFirma = Base64.decode(documento.getFirma());            
//            Security.addProvider(new BouncyCastleProvider());
//            CMSSignedData s = new CMSSignedData(datos, bFirma);
//            
//            CertStore certs = s.getCertificatesAndCRLs("Collection", "BC");            
//            SignerInformationStore signers = s.getSignerInfos();
//            Collection c = signers.getSigners();
//            Iterator it = c.iterator();
//            
//            while (it.hasNext())
//            {
//                SignerInformation signer = (SignerInformation) it.next();
//                Collection certCollection = certs.getCertificates(signer.getSID());
//
//                Iterator certIt = certCollection.iterator();
//                X509Certificate cert = (X509Certificate) certIt.next();
//   
//                if (signer.verify(cert.getPublicKey(), "BC"))
//                {         
//                    Interpretador interpretador=new Interpretador(cert);                   
//                    firmaVO.setNif(interpretador.getNif());
//                }
//            }
//            
//            salida.add(firmaVO);
//            
//            return salida ;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            log.error("================ > " + this.getClass().getName() + " ERROR: " + e.getMessage());
//            salida.setResultadoVerificacion(e.getMessage());
//            return salida;
//        }
        
    }
    
    
   public String firmaServidor(byte[] documento) throws TechnicalException{
       throw new TechnicalException("Operacion no soportada todavía");
   }
}
