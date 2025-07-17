package es.altia.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author oscar.rodriguez
 */
public class EncriptacionContrasenha  {

    /*
     * Devuelve el hash SHA-1 correspondiente a una cadena de caracteres
     * @param: String del que se obtiene el hash
     * @return: Hash SHA-1 con una longitud de 40 caracteres
     */
    public static String getHashSHA_1(String message) throws NoSuchAlgorithmException{
        MessageDigest md;
        byte[] buffer, digest;
        String hash = "";
        
        buffer = message.getBytes();
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();

        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }

        return hash;
    }    
}
