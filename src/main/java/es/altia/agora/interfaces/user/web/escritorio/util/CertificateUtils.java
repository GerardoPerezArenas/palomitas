package es.altia.agora.interfaces.user.web.escritorio.util;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;

public class CertificateUtils {
    
    public static X509Certificate[] getCertificatesChain(HttpServletRequest request) {
        X509Certificate[] result = null;
        Object obj = request.getAttribute("javax.servlet.request.X509Certificate");
        if (obj != null) {
            if (obj instanceof String) {
                String certString = (String) obj;
                try {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    result = new X509Certificate[1];
                    result[0] = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certString.getBytes()));
                } catch (CertificateException e) {
                    result = null;
                }
            } else if (obj instanceof X509Certificate) {
                result = new X509Certificate[1];
                result[0] = (X509Certificate) obj;
            } else if (obj instanceof X509Certificate[]) {
                result = (X509Certificate[]) obj;
            }
        }
        return result;
    }
    
    public static boolean hasCerts(X509Certificate[] a) {
        return ( (a!=null) && (a.length>0) );
    }

}
