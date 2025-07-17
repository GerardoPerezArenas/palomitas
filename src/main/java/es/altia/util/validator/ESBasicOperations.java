/*______________________________BOF_________________________________*/
package es.altia.util.validator;

import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.commons.BasicValidations;

import java.security.cert.X509Certificate;
import java.util.StringTokenizer;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * @author
 * @version $\Revision$ $\Date$
 */
public class ESBasicOperations {
    private static final String LETRAS_NIF="TRWAGMYFPDXBNJZSQVHLCKET";
    private static final String LETRAS_CIF_ORGANIZACION="ABCDEFGHKLMNPQS";
    private static final String[] LETRAS_CIF_DCS=new String[]{"J","A","B","C","D","E","F","G","H","I"};
    private static final int[] v1 = new int[] {0,2,4,6,8,1,3,5,7,9};
    protected static final Log m_Log =
            LogFactory.getLog(ESBasicOperations.class.getName());

    private ESBasicOperations(){}

    public static final boolean isNIF(final String number, final String letter) {
        try {
            final int dni = Integer.parseInt(number);
	        final int posicion = dni % 23;
	        final String letra = LETRAS_NIF.substring(posicion,posicion+1);
            return (letra.equals(letter));
        } catch (Throwable t) {
            return false;
        }//try-catch
    }//isNIF

    public static final boolean isCIF(final String typeLetter, final String number, final String dcLetter) {
        boolean result = (!BasicValidations.isEmpty(typeLetter)) &&
             (!BasicValidations.isEmpty(number)) &&
             (!BasicValidations.isEmpty(dcLetter));
        if (result) {
            result = (BasicValidations.isStringWithLengthBetween(typeLetter,1,1)) &&
                     (BasicValidations.isStringWithLengthBetween(dcLetter,1,1)) &&
                     (BasicValidations.isNumberBetween(number,0,9999999));
        }//if
        if (result) {
            result = (LETRAS_CIF_ORGANIZACION.indexOf(typeLetter.toUpperCase())>=0);
            final int dc = digitoCIF(number);
            if (BasicValidations.isNumberBetween(dcLetter,0,9))
                result = result && (dc==BasicTypesOperations.toInt(dcLetter));
            else
                result = result && (LETRAS_CIF_DCS[dc].equals(dcLetter));
        }//if
        return result;
    }//isCIF

    private static final int digitoCIF(final String cif) {
        int temp = 0;
	    for( int i = 1; i <= 5; i += 2 ) {
    		temp = temp + v1[ BasicTypesOperations.toInt(cif.substring(i-1,1)) ];
    		temp = temp + BasicTypesOperations.toInt(cif.substring(i,1));
    	}//for
		temp = temp + v1[ BasicTypesOperations.toInt(cif.substring(6,1)) ];
		temp = (10 - ( temp % 10));
	    return (temp % 10);
    }//digitoCif

    public static final String parseNIFFromFNMTCertificate(final X509Certificate cert) {
        String sujeto = cert.getSubjectDN().getName();
        int posicion;

        StringTokenizer st = new StringTokenizer(sujeto, ",");
        String token;

        String nif = null;

        while (st.hasMoreTokens()) {
            token = st.nextToken().trim();
            if (token.startsWith("CN=")) {
                posicion = token.indexOf(" - ");
                if ( (posicion > 0) && (token.length()>posicion+6) ) {
                    nif = token.substring(posicion + 6).trim();//b-bNIF
                }//if
            }//if
        }//while

        return nif;
    }//parseNIFFromFNMTCertificate

    /**
    * Método que valida el número de la cuenta
    * @param idEntidad entidad
    * @param idSucursal sucursal
    * @param digControl digito de control
    * @param numCuenta número de cuenta
    * @return boolean En función del resultado, retorna true si se cumple o false si la comprobación es incorrecta
    */
    public static final boolean isCCC(String idEntidad,String idSucursal,String digControl,String numCuenta){
            //concatena entidad y sucursal
            String banco_sucursal = idEntidad.trim() + idSucursal.trim();
            //saca longitud de la concatenacion anterior
            int longitud_banco = banco_sucursal.length();
            String numero;
            int primer_digito;
            int segundo_digito;
            int[] control = new int[11];
                    control[1] = 6;
                    control[2] = 3;
                    control[3] = 7;
                    control[4] = 9;
                    control[5] = 10;
                    control[6] = 5;
                    control[7] = 8;
                    control[8] = 4;
                    control[9] = 2;
                    control[10] = 1;
            // calcula el digito de control para el para banco-sucursal
            int suma = 0;
            for (int i=1;i < longitud_banco+1 ;i++ ){
                if (i == longitud_banco) numero = banco_sucursal.substring(0,1);
                else numero = banco_sucursal.substring(longitud_banco-i,longitud_banco-i+1);
                int num = Integer.parseInt(numero);
                suma = suma + (num * control[i]);
            }//for
            if (suma % 11 != 0) primer_digito = 11 - (suma % 11);
            else primer_digito = 0;

            //calcula el digito de control para el numero de cuenta
            String numero_cuenta =numCuenta.trim();
            suma = 0;
            int longitud_cuenta = numero_cuenta.length();

            for (int i=1;i < longitud_cuenta+1 ;i++ ){
                    if (i == longitud_cuenta) numero = numero_cuenta.substring(0,1);
                    else numero =	numero_cuenta.substring(longitud_cuenta-i,longitud_cuenta-i+1);
                    int num = Integer.parseInt(numero);
                    suma = suma + (num * control[i]);
            }//for
            if (suma % 11 != 0) segundo_digito = 11 - (suma % 11);
            else segundo_digito = 0;

            //se construyen los digitos de control
            // si alguno de los digitos es 10 se coge solo el 1
            if (primer_digito == 10) primer_digito = 1;
            if (segundo_digito == 10) segundo_digito = 1;

            String digitos_control = Integer.toString(primer_digito) + Integer.toString(segundo_digito);
            if (!digitos_control.equals(digControl.trim())) return false;
            return true;
    }//esCCC



    public static void main(String[] args) {
        if (args[0]!=null) {
            if (args[0].equals("nif")) if(m_Log.isDebugEnabled()) m_Log.debug("NIF validation = " + isNIF(args[1],args[2]));
            if (args[0].equals("cif")) if(m_Log.isDebugEnabled()) m_Log.debug("CIF validation = " + isCIF(args[1],args[2],args[3]));
            if (args[0].equals("ccc")) if(m_Log.isDebugEnabled()) m_Log.debug("CCC validation = " + isCCC(args[1],args[2],args[3],args[4]));
        }//if
    }//main
}//class
/*______________________________EOF_________________________________*/