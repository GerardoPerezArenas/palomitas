package es.altia.util.security.util;

import es.altia.agora.technical.ConstantesDatos;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TokenGenerator {

    private static final int BYTES_TOKEN = 20;

    private static final Log log = LogFactory.getLog(TokenGenerator.class.getName());

    /**
     * Genera un token. El token se genera utilizando la clase SecureRandom
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String generarToken() throws UnsupportedEncodingException {
        log.debug("generarToken");
        
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[BYTES_TOKEN];
        random.nextBytes(bytes);
        String token = new String(new Base64().encode(bytes), ConstantesDatos.CODIFICACION_UTF);

        if (log.isDebugEnabled()) {
            log.debug(String.format("token generado: %s", token));
        }
        
        return token;
    }
}
