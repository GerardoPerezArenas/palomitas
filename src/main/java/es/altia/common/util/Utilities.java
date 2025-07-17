package es.altia.common.util;

import es.altia.agora.technical.ConstantesDatos;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;

public class Utilities {
    
    // Expresion Regular NIF
    private static final String NIF_REGEX = "[0-9]{1,8}[TRWAGMYFPDXBNJZSQVHLCKEO]";
    private static final String NIF_LETRAS = "TRWAGMYFPDXBNJZSQVHLCKEO";
    private static final String NIE_REGEX = "[XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKEO]";

    // Expresion Regular CIF
    private static final String CIF_REGEX = "[ABCDEFGHKLMNPQS][0-9,A-J]$";
    private static final String CIF_LETRAS = "ABCDEFGHIJ";
    private static Logger log = Logger.getLogger(Utilities.class);
    
    public static boolean isInteger(String dato){
        boolean exito = false;
        try{
            Integer.parseInt(dato);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }
        
        return exito;
    }
    
    
    public static boolean isLong(String dato){
        boolean exito = false;
        try{
            Long.parseLong(dato);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }
        
        return exito;
    }
    
    
    public static boolean isValidDate(String dato,String formato){
        boolean exito = false;
        try{
            if(dato!=null && formato!=null){
                SimpleDateFormat sf = new SimpleDateFormat(formato);
                Date date  = sf.parse(dato);
                if(date!=null) exito = true;
            }
            
        }catch(Exception e){
            exito = false;
        }
        
        return exito;
    }
    
    
    
    /**
  * Valida el NIF
  * 
  * @param nif Documento de identificación
  * @return true si coincide con la expresión regular, false en caso
  *         contrario
  * @throws Exception
  */
    public static boolean validaNIF(String nif) throws Exception {

	boolean valido = false;
	try {
		Pattern p = Pattern.compile(NIF_REGEX);
		Matcher m = p.matcher(nif);
		if (m.matches()) {
			int nifNum = Integer.parseInt(nif.substring(0, nif.length() - 1));
			int pos = nifNum % 23;
			char letra = nif.charAt(nif.length() - 1);
			valido = NIF_LETRAS.charAt(pos) == letra;
		}
	} catch (PatternSyntaxException e) {
		log.error("Saltó excepción en validaNIF");
		e.printStackTrace();
	} catch (Exception ex) {
		log.error("Saltó excepción en validaNIF");
		throw new Exception(ex.getMessage());
	}
	return valido;
}

    
    
   /**
     * Valida el CIF
     * 
     * @param cif Documento de identificacion
     * @return true si coincide con la expresión regular, false en caso
     *         contrario
     * @throws Exception
     */  
    public static boolean validaCIF(String cif){
        int par = 0;
        int non = 0;
        String letras="ABCDEFGHJKLMNPQRSUVW";
        String letrasInicio="KPQS";
        String letrasFin="ABEH";
        String letrasPosiblesFin="JABCDEFGHI";

        String letra = Character.toString(cif.charAt(0)).toUpperCase();
        String caracterControl  ="";

            if (cif.length()!=9){
                return false;
            } else{
                caracterControl =Character.toString(cif.charAt(8)).toUpperCase();
            }

            for (int zz=2;zz<8;zz+=2)
            {
                par = par + Integer.parseInt(Character.toString(cif.charAt(zz)));
            }

            for (int zz=1;zz<9;zz+=2)
            {
                int nn = 2 * Integer.parseInt(Character.toString(cif.charAt(zz)));
                if (nn > 9) 
                    nn = 1+(nn-10);

                non = non+nn;
            }

            int parcial = par + non;
            int control = (10 - ( parcial % 10));
            if (control==10) control=0;


        /*
        * El valor del último carácter:
        * Será una LETRA si la clave de entidad es K, P, Q ó S.
        * Será un NUMERO si la entidad es A, B, E ó H.
        * Para otras claves de entidad: el dígito podrá ser tanto número como letra.
        * */

        if (letrasInicio.indexOf(letra)!=-1){
            return (Character.toString(letrasPosiblesFin.charAt(control)).equals(caracterControl));        
        } else if (letrasFin.indexOf(letra)!=-1){
            return (caracterControl.equals(Integer.toString(control)));

        } else if (letras.indexOf(letra)!=-1){        
            if((Character.toString(letrasPosiblesFin.charAt(control)).equals(caracterControl)) || caracterControl.equals(Integer.toString(control))){
                return true;            
            }else return false;        

        } else{
            return false;
        }
    }
    
    
    
    /**
     * Valida el CIF
     * 
     * @param cif Documento de identificacion
     * @return true si coincide con la expresión regular, false en caso
     *         contrario
     * @throws Exception
     *
    public  static boolean validaCIF(String cif) throws Exception {
       if(log.isDebugEnabled()) log.debug("validaCIF ( CIF = " + cif + " ) : BEGIN");
	boolean valido = false;
	try {
            String cifValidar = cif.toUpperCase();
            if(log.isDebugEnabled()) log.debug("cifValidar = " + cifValidar);
            Pattern p = Pattern.compile(CIF_REGEX);
            Matcher m = p.matcher(cifValidar);
            if (m.matches()) {
                   if(log.isDebugEnabled()) log.debug("La expresión regular valido correctamente el CIF");
                    int cifLength=cifValidar.length();
                    String numero = cifValidar.substring(1, cifLength - 1);
                    int A = 0;
                    int B = 0;
                    int dec;
                    int length=numero.length();
                    for (int i = 0; i < length; i++) {
                        if ((i % 2) == 0) {
                            dec = 2 * Integer.parseInt("" + numero.charAt(i));                                
                            if(dec > 9){
                               dec = 1 +(dec - 10);
                            }//if(dec > 9)
                            B += dec;
                        } else {
                            A += Integer.parseInt("" + numero.charAt(i));
                        }
                    }
                    
                    int C = A + B;
                    int D = 10 - (C % 10);
                    char DC = cifValidar.charAt(cifLength - 1);

                    try {
                        if (Integer.parseInt("" + DC) == D) {
                                valido = true;
                                if(log.isDebugEnabled()) log.debug("valido = " + valido);
                        }
                    } catch (NumberFormatException e) {
                        if(log.isDebugEnabled()) log.debug("NumberFormatException");
                        if (DC == CIF_LETRAS.charAt(D - 1)) {
                                valido = true;
                                if(log.isDebugEnabled()) log.debug("valido = " + valido);
                        }
                    }
                    
            } else{ if(log.isDebugEnabled()) log.debug("La expresión regular NO VALIDÓ correctamente el CIF");}
            
	} catch (PatternSyntaxException psex) {
		log.error("Saltó excepción en validaCIF");
                psex.printStackTrace();
		throw new Exception(psex.getMessage());
	} catch (Exception ex) {
		log.error("Saltó excepción");
                ex.printStackTrace();
		throw new Exception(ex.getMessage());
        }       
	if(log.isDebugEnabled()) log.debug("validaCIF() : END");
	return valido;
        
    }//validaCIF
    **/
    
    public static boolean validarNie(String Nie)
    {

        int LONGITUD = 9;

        // Si se trata de un NIF
        // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.
        if (Nie.length() != LONGITUD) {

            return false;
        }

        // Comprobas que el formato se corresponde con el de un NIE
        char primeraLetra = Nie.charAt(0);
        String numero = Nie.substring(1,8);
        char ultimaLetra = Nie.charAt(8);
        int numeros=0;
        try
         {
           numeros=Integer.parseInt(numero);
         }
         catch (NumberFormatException e)
         {
            return false;
         }

        // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
        if (primeraLetra =='Y') numeros = numeros + 10000000;
        else if (primeraLetra == 'Z') numeros = numeros + 20000000;
        else if (primeraLetra != 'X') {

            return false;
        }

        // Validamos el caracter de control.
        char letraCorrecta = getLetraNif(numeros);
        if (ultimaLetra != letraCorrecta) {

            return false;
        }
        return true;
    }
    
    
   private static char getLetraNif(int dni) {
         String lockup = "TRWAGMYFPDXBNJZSQVHLCKE";

        char letraReal=lockup.charAt(dni % 23);
        return letraReal;
    }
    
   
    /**
    * Método auxiliar que devuelve un trozo del contenido de un fichero xml pasandole los tags de inicio y fin, Ej: <tagEj> </tagEj>
    * @param documento Objeto que contiene el documento
    * @param tagInicial: Tag inicial
    * @param tagFinal: Tag final
    * @return Trozo del documento recuperado. Si se produce un error en el procesamiento devuelve un error.
    */
     public static String leerContenidoFicheroXML(byte[] documento, String tagInicial, String tagFinal){
         
       if(log.isDebugEnabled()){ log.debug("Utilities.leerContenidoFicheroXML: Inicio lectura contenido fichero " );}
       
       String documentoStr = "";
       try{
            
            String fichero = new String(documento, ConstantesDatos.CODIFICACION_UTF);
            int inicio = fichero.indexOf(tagInicial);
            int fin = fichero.indexOf(tagFinal) + tagFinal.length();
            if (inicio>0 && fin>0 && inicio<fin){
                documentoStr = fichero.substring(inicio, fin);
            }
        
        }catch ( IOException io ) {
            documentoStr = null;
           log.error("IOExecption: " + io.getMessage());
        }catch ( Exception e) {
            documentoStr = null;            
            log.error("Exception: " + e.getMessage());
        }
        
        if(log.isDebugEnabled()){ log.debug("Utilities.leerContenidoFicheroXML: Fin lectura contenido fichero ");}
              
        return documentoStr;

     }

    
}
