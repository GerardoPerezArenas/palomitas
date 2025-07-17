package es.altia.util.security.certificates.verifiers;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.util.security.certificates.VerificadorCertificado;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 */
public class VerificadorCertificadoX509 implements VerificadorCertificado {
    private static final String CLSNAME = "VerificadorCertificadoX509";
    protected static Log _log =
            LogFactory.getLog(VerificadorCertificadoX509.class.getName());

    private X509Certificate certificado = null;
    private boolean valido = false;
    private int motivoInvalidez = 0;
    private boolean revocado = true;
    private String motivoRevocacion = null;
    private String fechaRevocacion = null;
    private String nif = null;

    /**
     * Verifica las situación del certificado y obtiene los atributos del mismo.<br>
     * <br>
     *
     * @param rutaParametros Ruta completa al fichero de parámetros para el verificador.
     * @param certificado    Ceritificado que se quiere verificar.
     * @since 1.0
     */
    public void verifica(String rutaParametros, X509Certificate certificado) {
        try {
            this.certificado = certificado;

            //Cargamos la CA
            Hashtable tabla;
            tabla = ObtenerSeccion(rutaParametros, "CACertificate");
            String RutaCertCA = valorAtributo(tabla, "RutaCertCA") + valorAtributo(tabla, "FicheroCertCA");

            FileInputStream certCAfich = new FileInputStream(RutaCertCA);
            BufferedInputStream bis = new BufferedInputStream(certCAfich);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate certCA = cf.generateCertificate(bis);

            PublicKey PubCA = certCA.getPublicKey();
            certCAfich.close();

            // Comprobamos que el certificado fue emitido por la CA
            certificado.verify(PubCA);

            // Comprobamos su validez temporal
            certificado.checkValidity();

            // Comprobamos revocación
            BigInteger numeroSerie = certificado.getSerialNumber();
            tabla = ObtenerSeccion(rutaParametros, "CARevocados");
            fechaRevocacion = valorAtributo(tabla, numeroSerie.toString(16));
            if (fechaRevocacion == null) {
                valido = true;
                revocado = false;
            } else {
                revocado = true;
                valido = false;
            }//if
            nif = extraeNIFTipoFNMT(certificado.getSubjectDN().getName());
        } catch (NoSuchAlgorithmException e) {
            motivoInvalidez = ERROR_ALGORITMO_NO_VALID0;
        } catch (InvalidKeyException e) {
            motivoInvalidez = ERROR_CLAVE_NO_VALIDA;
        } catch (NoSuchProviderException e) {
            motivoInvalidez = ERROR_PROVEEDOR_NO_VALID0;
        } catch (SignatureException e) {
            motivoInvalidez = ERROR_FIRMA_NO_VALIDA;
        } catch (CertificateException e) {
            String cual = e.getClass().getName();
            if (cual.equals("CertificateExpiredException"))
                motivoInvalidez = ERROR_CERTIFICADO_CADUCADO;
            else if (cual.equals("CertificateNotYetValidException"))
                motivoInvalidez = ERROR_CERTIFICADO_AUN_NO_VALIDO;
            else
                motivoInvalidez = ERROR_CERTIFICADO_GRAL;
        } catch (Exception e) {
            motivoInvalidez = ERROR_INTERNO;
            _log.error(CLSNAME+".verifica() Error interno:"+e.getClass() +"\n con el mensaje:	" + e.getMessage() +	"\n Traza: \n",e);
        }//try-catch
    }//verifica

    /**
     * Nos dice si el certificado es válido.<br>
     * <br>
     *
     * @return true o false en función de la validez del certificado.
     * @since 1.0
     */
    public boolean esValido() {
        return valido;
    }

    /**
     * En caso de que el certificado no sea válido, devuelve el motivo de la invalidez.<br>
     * <br>
     *
     * @return motivo de la invalidez..
     * @since 1.0
     */
    public int motivoInvalidez() {
        return motivoInvalidez;
    }

    /**
     * Nos dice si el certificado ha sido revocado o no.<br>
     * <br>
     *
     * @return devuelve un booleano que indica si el estado de revocación del
     *         certificado.
     * @since 1.0
     */
    public boolean esRevocado() {

        return revocado;
    }

    /**
     * En caso de que este revocado, nos devuelve la fecha de revocación del certificado.<br>
     * <br>
     *
     * @return La fecha de revocación.
     * @since 1.0
     */
    public String fechaRevocacion() {
        return fechaRevocacion;
    }

    /**
     * En caso de que este revocado, nos devuelve una cadena con el motivo de revocación
     * de revocación del certificado.<br>
     * <br>
     *
     * @return devuelve el motivo.
     * @since 1.0
     */
    public String motivoRevocacion() {
        return motivoRevocacion;
    }

    /**
     * Devuelve el NIF asociado al certificado<br>
     * <br>
     *
     * @return Cadena NIF.
     * @since 1.0
     */
    public String nifSujeto() {
        return nif;
    }

//
// MÉTODOS AUXILIARES
//
    /**
     * Genera una tabla HASH de la seccion del fichero de configuración indicados.<br>
     * <br>
     *
     * @param rutaFichero Indica la ruta de acceso al fichero de configuración.
     * @param Seccion     Nombre de la sección del fichero de configuración que
     *                    se quiere cargar.
     * @return Devuelve una tabla hash con todos los valores encontrados en la
     *         sección.
     * @throws java.io.FileNotFoundException Si el fichero del que se intenta leer no
     *                                       existe.
     * @throws java.io.IOException           Error durante la lectura del fichero.
     */
    private Hashtable ObtenerSeccion(String rutaFichero, String Seccion)
            throws FileNotFoundException, IOException {
        boolean EncSecc = false;
        boolean EncCab = false;
        Hashtable ValoresSeccion = null;
        ValoresSeccion = new Hashtable();
        String thisLine = "";
        FileReader fileIni = new FileReader(rutaFichero);
        BufferedReader br = new BufferedReader(fileIni);

        // Se leen lineas del fichero de configuracion hasta que se
        // llegue al fin de fichero o bien se encuentre la seccion
        // buscada y durante el proceso de la misma se pase a la siguiente
        while ((thisLine = br.readLine()) != null &&
                !(EncSecc && thisLine.startsWith("["))) {
            if (thisLine.startsWith("[" + Seccion + "]"))
                EncSecc = true;

            // Estamos en la seccion buscada
            if ((EncSecc) && (!thisLine.startsWith("["))) {
                StringTokenizer st = new StringTokenizer(thisLine, "=");
                String valor = "";
                String cabecera = "";
                if (st.hasMoreTokens())
                    cabecera = st.nextToken();
                if (st.hasMoreTokens())
                    valor = st.nextToken();
                //Se introducen los valore en la Hashtable
                ValoresSeccion.put(cabecera, valor);
            }
        } // end while
        br.close();
        fileIni.close();
        return ValoresSeccion;//No se ha encontrado el valor
    }

    /**
     * Devuelve el valor del atributo que se indica en la tabla indicada.<br>
     * <br>
     *
     * @param tabla          Tabla de valores en la que se quiere buscar.
     * @param nombreAtributo Atributo que se quiere buscar.
     * @return El valor del atributo buscado.
     */
    private String valorAtributo(Hashtable tabla, String nombreAtributo) {
        if (tabla == null) return null;
        if (nombreAtributo == null) return null;

        //Se busca el atributo en la tabla
        if (!tabla.containsKey(nombreAtributo)) return null;

        return (String) tabla.get((Object) nombreAtributo);
    }

    /**
     * A partir de la cadena correspondiente al Nombre Distintivo del certificado Tipo FNMT
     * obtiene el NIF.<br>
     * <br>
     *
     * @param sujeto Nombre distintivo del propietario del certificado.
     * @return el NIF.
     */
    private String extraeNIFTipoFNMT(String sujeto) {
        int contadorOU = 0;
        int posicion;

        StringTokenizer st = new StringTokenizer(sujeto, ",");
        String token;

        String CN = null;
        String nombre = null;
        String nif = null;
        String tipo = null;

        while (st.hasMoreTokens()) {
            token = st.nextToken().trim();
            if (token.startsWith("CN=")) {
                CN = token.substring(3);
                posicion = token.indexOf(" - ");
                if (posicion == -1) {
                    nombre = token.substring(3);
                    nif = "";
                } else {
                    nombre = token.substring(3 + 6, posicion);//6=nombre
                    nif = token.substring(posicion + 6).trim();//b-bNIF

                }
            } else if (token.startsWith("OU=")) {
                contadorOU++;
                if (contadorOU == 2) {
                    tipo = token.substring(3).trim();
                }
            }

        }

        return nif;
    }

}//class
