package es.altia.util.commons;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Clase que contiene operaciones a realizar sobre números
 * @author Óscar
 */
public class NumericOperations {

    /**
     * Comprueba si un String contiene un número entero
     * @param numero: String con el numero
     * @return True si es un entero o false en caso contrario
     */
    public static boolean isInteger(String numero){
        boolean exito = false;
        try{
            Integer.parseInt(numero);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }

        return exito;
    }


  /**
     * Comprueba si un String contiene un número de tipo double
     * @param numero: String con el numero
     * @return True si es un double o false en caso contrario
     */
    public static boolean isDouble(String numero){
        boolean exito = false;
        try{
            Double.parseDouble(numero);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }

        return exito;
    }


    /**
     * Comprueba si un String contiene un número de tipo long
     * @param numero: String con el numero
     * @return True si es un long o false en caso contrario
     */
    public static boolean isLong(String numero){
        boolean exito = false;
        try{
            Long.parseLong(numero);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }

        return exito;
    }

	/**
     * Obtiene el valor redondeado de un double
     * @param valor double
     * @param tipo 1: redondea al alza (4.1 -> 5); -1: redondea a la baja (4.9 -> 4); 0: si pasa de .5 redondea al alza, si no a la baja 
     * @return 
     */
    public static double redondearDouble(double valor, int tipo){
        if(tipo>0) return Math.ceil(valor);
        else if(tipo<0) return Math.floor(valor);
        return Math.round(valor);
    }
    
	/**
	 * Formatea un double como string según el formato decimal, con ',' como separador decimal y '.' como separador de miles
	 * @param num
	 * @return 
	 */
    public static String doubleToFormattedString(Double num)
    {
        /** FORMATO DECIMAL: #,###,##0.00
        * Los # indican valores no obligatorios
        * Los 0 indican que si no hay valor se pondrá un cero
        */

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(new Locale("es", "ES"));
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.'); 

        NumberFormat formatter = new DecimalFormat("#,###,##0.00", otherSymbols);

        String pattern = ((DecimalFormat) formatter).toPattern();
        String newPattern = pattern.replace("\u00A4", "").trim();

        String ret = formatter.format(num);
        return ret;
    }
      
	/**
	 * Formatea un doble, redondeando previamente su valor, a string sin ceros. 
	 * El string resultante tendra ',' como separador decimal y '.' como separador de miles.
	 * @param num
	 * @param redondear
	 * @param tipoRedondeo
	 * @return 
	 */
    public static String doubleToFormattedStringSinZeros(Double num, boolean redondear, int tipoRedondeo) {
        if(redondear) 
            num = redondearDouble(num, tipoRedondeo);
        
        String formatDouble = doubleToFormattedString(num);
        int pos = formatDouble.indexOf(",00");

        if (pos != -1) {
            return formatDouble.substring(0, pos);
        }
        return formatDouble;
    }
	
	/**
	 * Formatea un doble, redondeando previamente su valor, a string sin ceros. 
	 * El string resultante tendra ',' como separador decimal y '.' como separador de miles.
	 * @param num
	 * @param redondear
	 * @param tipoRedondeo
	 * @return 
	 */
    public static String doubleToStringSinZeros(Double num, boolean redondear, int tipoRedondeo) {
        if(redondear) 
            num = redondearDouble(num, tipoRedondeo);
        
        return String.format("%.0f", num);
    }
}
