/*______________________________BOF_________________________________*/
package es.altia.util.commons;

import org.apache.commons.lang.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.time.DateUtils;


/**
 * 
 * @version $\Revision$ $\Date$
 */
public final class DateOperations {
    /*_______Constants______________________________________________*/
    public static final String LATIN_DATE_FORMAT = "dd/MM/yyyy";
    public static final String LATIN_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String ENGLISH_DATE_FORMAT = "MM/dd/yyyy";
    public static final String DESC_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LATIN_DATE_24HOUR_WITHOUT_ZERO_FORMAT = "d/M/yyyy HH:mm:ss";
    public static final String LATIN_DATE_24HOUR = "dd/MM/yyyy HH:mm:ss";
    public static final String LATIN_DATE_24HOUR_NOSECONDS = "dd/MM/yyyy HH:mm";
    public static final String DATE_TIME_FOR_FILE = "yyyyMMddHHmmss";
    public static final String DATE_TIME_FOR_FILE_WITH_MILIS = "yyyyMMddHHmmssSSS";
    public static final String DATE_TIME_WITH_TIMEZONE_OFFSET = "yyyy-MM-dd EEE HH:mm:ss Z";

    public static final String[] SUPPORTED_DATE_FORMATS = {
        LATIN_DATE_24HOUR,
        LATIN_DATE_24HOUR_WITHOUT_ZERO_FORMAT,
        LATIN_DATE_24HOUR_NOSECONDS,
        LATIN_DATE_FORMAT,
        ENGLISH_DATE_FORMAT,
        DESC_DATE_FORMAT,
        DATE_TIME_WITH_TIMEZONE_OFFSET,
        DATE_TIME_FOR_FILE_WITH_MILIS,
        DATE_TIME_FOR_FILE
    };
    
    public static final Calendar DATE_1900_01_01 = toCalendar("1900-01-01", DESC_DATE_FORMAT);
    public static final Calendar DATE_VERY_FUTURE = toCalendar("2200-01-01", DESC_DATE_FORMAT);
    private static final int[] MONTH_LENGTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] LEAP_MONTH_LENGTH = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    
    /*_______Operations_____________________________________________*/
    private DateOperations(){}

    /**
      * Use dd/MM/yyyy for spanish format
      * Use MM/dd/yyyy for uk format
      *
      * Returns null if any error
      **/
    public static final String toString(java.util.Date dt, String fmt) {
        try {
//            final SimpleDateFormat formatter = new SimpleDateFormat(fmt);
//            return formatter.format(dt);
            return DateFormatUtils.format(dt,fmt);
        } catch (Exception e) {
            return null;
        }//try-catch
    }//toString

    /**
      * Use dd/MM/yyyy for spanish format
      * Use MM/dd/yyyy for uk format
      *
      * Returns null if any error
      **/
    public static final String toString(Calendar dt, String fmt) {
        return toString(toDate(dt),fmt);
    }//toString

    /**
      * Use dd/MM/yyyy for spanish format
      * Use MM/dd/yyyy for uk format
      *
      * Returns null if any error
      **/
    public static final java.util.Date toDate(String dt, String fmt) {
        java.util.Date result;

        try {
            final SimpleDateFormat formatter = new SimpleDateFormat(fmt);
            formatter.setLenient(false);
            result = formatter.parse(dt);
        } catch (Exception e) {
            result = null;
        }//try-catch

        return result;
    }//toDate

    public static final java.util.Date toDate(Calendar cal) {
        java.util.Date result = null;

        if (cal!=null)
            result = cal.getTime();

        return result;
    }//toDate

    public static final java.sql.Date toSQLDate(Calendar cal) {
        java.sql.Date result = null;

        if (cal!=null)
            result = new java.sql.Date(cal.getTime().getTime());

        return result;
    }//toSQLDate

    public static final Calendar toCalendar(java.util.Date dt) {
        Calendar result = null;

        if (dt!=null) {
            result = Calendar.getInstance();
            result.clear();
            result.setTime(dt);
        }//if

        return result;
    }//toCalendar

    /**
     * Years between 0 and 19 are transformed to 2000 to 2019
     * Years between 20 and 99 are transformed to 1920 to 1999
     *
     * Advice: Do not use this kind of 'intelligence'
     *
     * @param c the calendar (this method modifies input calendar if necesary)
     * @return the same calendar modified if necesary
     */
    public static final Calendar toFourDigitsYear(Calendar c) {
        if (c!=null) {
            if (c.get(Calendar.YEAR)<20)
                c.add(Calendar.YEAR,2000);
            else if (c.get(Calendar.YEAR)<100)
                c.add(Calendar.YEAR,1900);
        }//if
        return c;
    }//toFourDigitsYear

    public static final Calendar toCalendar(String dt, String fmt) {
        Calendar result = null;
        if (dt!=null) {
            result= toCalendar(toDate(dt,fmt));
        }//if
        return result;
    }//toCalendar

    public static final Timestamp toSQLTimestamp(Calendar cal) {
        java.sql.Timestamp result = null;

        if (cal!=null)
            result = new java.sql.Timestamp(cal.getTime().getTime());

        return result;
    }//toSQLTimestamp

    /**
     * Returns the length of the specified month in days. Month is 1 for January
     * and 12 for December.
     *
     * @param year   year
     * @param m      month
     *
     * @return length of the specified month in days
     */
    public static final int getMonthLength(int year, int m) {
        if ((m < 1) || (m > 12)) return -1;
        if (isLeapYear(year))
            return LEAP_MONTH_LENGTH[m];
        else
            return MONTH_LENGTH[m];
    }//getMonthLength

    /**
     * Check if the given year is leap year.
     *
     * @param y      year to check
     *
     * @return <code>true</code> if the year is a leap year
     */
    public static final boolean isLeapYear(int y) {
        boolean result = false;

        if (((y % 4) == 0) &&			// must be divisible by 4...
            ((y < 1582) ||				// and either before reform year...
             ((y % 100) != 0) ||		// or not a century...
             ((y % 400) == 0))) {		// or a multiple of 400...
                result = true;			// for leap year.
        }//if
        return result;
    }//isLeapYear

    /**
     * Calculates day of year from given time stamp.
     * It may not work for some dates in 1582.
     *
     * @param year
     * @param month
     * @param day
     *
     * @return day of year in range: [1-366]
     */
    public static final int dayOfYear(int year, int month, int day) {
        if (isLeapYear(year))
            return ((275 * month) / 9) - ((month + 9) / 12) + day - 30;
        else
            return ((275 * month) / 9) - (((month + 9) / 12) << 1) + day - 30;
    }//dayOfYear

    /**
     * Traduce el mes de formato numerico a texto. En el idioma que se designe
     * @param mes mes a traducir en formato numérico
     * @param idioma el idioma en que queremos el mes
     * @param pais el pais con el que queremos traducir el mes
     * @return el mes en formato de texto
     */
     public static String traducirMes (int mes, String pais,String idioma){

        Locale locale = new Locale(idioma, pais);
        
        ResourceBundle fecha = ResourceBundle.getBundle("es.altia.util.commons.fechas",
                locale);
        return fecha.getString(String.valueOf(mes));
    }


    public static String traducirFechaEuskera(String fecha) {
        String salida = "";

        try{
            Locale locale = new Locale("eu","ES");
            ResourceBundle config = ResourceBundle.getBundle("es.altia.util.commons.fechas",locale);
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date d = s.parse(fecha);
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(d);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);
        //<AÑO>ko <MES> <NÚMERO>a

            String euMes = config.getString(Integer.toString(mes));
            salida=ano+"(e)ko " + euMes + " " + dia + "(a)";
            //Se deja entre comentarios la traduccion del codigo mantenido en Vitoria aunque en PRO esta igual que el core
            //salida=ano+"(e)ko " + euMes + " " + dia + "(e)an";

        //Por ejemplo, "9 de Marzo de 2012" sería "2012ko martxoaren 9a"
        //urtarrilaren otsailaren martxoaren apirilaren maiatzaren ekainaren uztailaren abuztuaren irailaren urriaren azaroaren abenduaren
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return salida;
    }



     public static String tratarNumero(String num){
        int tamano = num.length();
        String salida;

        if (tamano == 4){
            if (num.startsWith("1"))
                return "mil "+ tratarNumero(num.substring(1));
            else
                return tratarNumero(num.substring(0,1)) + " mil " + tratarNumero(num.substring(1));
        }else

        if (tamano == 3){
            if (num.equals("100"))
                return "cien";
            else if (num.startsWith("0"))
                return tratarNumero(num.substring(1));
            else if (num.startsWith("1"))
                return "ciento "+ tratarNumero(num.substring(1));
            else
                return tratarNumero(num.substring(0,1))+ "cientos "+tratarNumero(num.substring(1));
        }else

        if (tamano == 2){
            if (num.startsWith("0"))
                return tratarNumero(num.substring(1));
            if (num.startsWith("9"))
                return "noventa" +  (!tratarNumero(num.substring(1)).equals("") ? " y " + tratarNumero(num.substring(1)):"");
            if (num.startsWith("8"))
                return "ochenta" +  (!tratarNumero(num.substring(1)).equals("") ? " y " + tratarNumero(num.substring(1)):"");
            if (num.startsWith("7"))
                return "setenta"+  (!tratarNumero(num.substring(1)).equals("") ? " y " + tratarNumero(num.substring(1)):"");
            if (num.startsWith("6"))
                return "sesenta"+  (!tratarNumero(num.substring(1)).equals("") ? " y " + tratarNumero(num.substring(1)):"");
            if (num.startsWith("5"))
                return "cincuenta"+  (!tratarNumero(num.substring(1)).equals("") ? " y " + tratarNumero(num.substring(1)):"");
            if (num.startsWith("4"))
                return "cuarenta"+  (!tratarNumero(num.substring(1)).equals("") ? " y " + tratarNumero(num.substring(1)):"");
            if (num.startsWith("3"))
                return "treinta"+  (!tratarNumero(num.substring(1)).equals("") ? " y " + tratarNumero(num.substring(1)):"");
            if (num.equals("20"))
                return "veinte";
            else if (num.startsWith("2"))
                return "veinti"+ tratarNumero(num.substring(1));
            else if (num.equals("10"))
                return "diez";
            else if (num.equals("11"))
                return "once";
            else if (num.equals("12"))
                return "doce";
            else if (num.equals("13"))
                return "trece";
            else if (num.equals("14"))
                return  "catorce";
            else if (num.equals("15"))
                return "quince";
            else if (num.equals("16"))
                return "dieciseis";
            else if (num.equals("17"))
                return "diecisiete";
            else if (num.equals("18"))
                return "diceciocho";
            else if (num.equals("19"))
                return "diecinueve";
        }else

        if (tamano == 1){
            if (num.equals("1"))
                return "uno";
            else if (num.equals("2"))
                return "dos";
            else if (num.equals("3"))
                return "tres";
            else if (num.equals("4"))
                return "cuatro";
            else if (num.equals("5"))
                return "cinco";
            else if (num.equals("6"))
                return "seis";
            else if (num.equals("7"))
                return "siete";
            else if (num.equals("8"))
                return "ocho";
            else if (num.equals("9"))
                return "nueve";
        }
        return "";
    }

    /**
      * Convierte un objeto java.sql.Timestamp en un String
      * @param t
      * @return String en formato dd/mm/aaaa
      */
     public static String timeStampToString(Timestamp t){
         return timeStampToString(t, DateOperations.LATIN_DATE_FORMAT);
     }

    /**
     * Convierte un objeto java.sql.Timestamp en un String con el formato
     * especificado
     *
     * @param t
     * @param format
     * @return fecha
     */
     public static String timeStampToString(Timestamp t, String format){
         String timeStampString = null;

         if (t != null && format != null) {
             Calendar c = Calendar.getInstance();
             c.clear();
             c.setTimeInMillis(t.getTime());

             SimpleDateFormat sf = new SimpleDateFormat(format);
             timeStampString = sf.format(c.getTime());
         }

         return timeStampString;
     }
     
      /**
      * Extrae la fecha de un Timestamp
      * @param t: Timestamp
      * @return String en formato dd/mm/aaaa
      */
     public static String extraerFechaTimeStamp(Timestamp t){

         Calendar c = Calendar.getInstance();
         c.clear();
         c.setTimeInMillis(t.getTime());

         SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
         return sf.format(c.getTime());
     }


      /**
      * Extrae la hora de un Timestamp
      * @param t: Timestamp
      * @return Hora en formato hh:mm
      */
     public static String extraerHoraTimeStamp(Timestamp t){

         Calendar c = Calendar.getInstance();
         c.clear();
         c.setTimeInMillis(t.getTime());

         SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
         return sf.format(c.getTime());
     }
	 
	 public static String extraerFechayHoraTimeStamp(Timestamp t){
		 
		 Calendar c = Calendar.getInstance();
		 c.clear();
		 c.setTimeInMillis(t.getTime());
		 
		 SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		 return sf.format(c.getTime());
	 }
	 
	 private Timestamp getTimestamp(String fecha,String formato){
        Timestamp time = null;
        if(!"".equals(fecha) && !"".equals(formato)){
            if("dd/mm/yyyy".equals(formato)){
                String[] datos = fecha.split("/");
                if(datos!=null && datos.length==3){
                    Calendar c = Calendar.getInstance();
                    c.clear();
                    c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(datos[0]));
                    c.set(Calendar.MONTH,Integer.parseInt(datos[1]));
                    c.set(Calendar.YEAR,Integer.parseInt(datos[2]));
                    time = new Timestamp(c.getTimeInMillis());
                }
            }
        }

        return time;
    }

     public static Timestamp toTimestamp(Calendar c){
        Timestamp t  = null;
        
        if (c != null)
            t  = new Timestamp(c.getTimeInMillis());
        
        return t;
     }


     public static Calendar toCalendar(Timestamp t){
        Calendar c = null;
        if (t != null) {
            c = Calendar.getInstance();
            c.clear();
            c.setTimeInMillis(t.getTime());
        }
        return c;
     }


     public static Timestamp toTimestamp(String fecha){
         java.sql.Timestamp t = null;
         if(fecha!=null && fecha.length()>0){
             
             try{
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date dFecha = sf.parse(fecha);
             
                Calendar c = Calendar.getInstance();
                c.clear();
                c.setTimeInMillis(dFecha.getTime());
                t= new java.sql.Timestamp(c.getTimeInMillis());
             }catch(Exception e){}                                        
         }
         
         return t;
     }


     /**
      * Convierte una fecha almacenada en un String a un Timestamp que se encuentra
      * en un determinado formato, por ejemplo: dd/MM/yyyy
      * @param fecha
      * @param formato
      * @return
      */
      public static Timestamp toTimestamp(String fecha,String formato){
         java.sql.Timestamp t = null;
         if(fecha!=null && fecha.length()>0){

             try{
                SimpleDateFormat sf = new SimpleDateFormat(formato);
                java.util.Date dFecha = sf.parse(fecha);
                Calendar c = Calendar.getInstance();
                c.clear();
                c.setTimeInMillis(dFecha.getTime());
                t= new java.sql.Timestamp(c.getTimeInMillis());

             }catch(Exception e){
                e.printStackTrace();
             }
         }

         return t;
     }

    /**
     * Convierte una fecha almacenada en un String a un Timestamp intentando
     * adivinar el formato
     *
     * @param fecha
     * @return
     */
    public static Timestamp guessTimestamp(String fecha) {
        java.sql.Timestamp t = null;
        if (fecha != null && fecha.length() > 0) {

            try {
                java.util.Date dFecha = DateUtils.parseDate(fecha, SUPPORTED_DATE_FORMATS);
                Calendar c = Calendar.getInstance();
                c.clear();
                c.setTimeInMillis(dFecha.getTime());
                t = toTimestamp(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return t;
    }
    
      public static Calendar timestampToCalendar(java.sql.Timestamp timestamp){
          Calendar c = null;
          if(timestamp!=null){
                c  = Calendar.getInstance();
                c.clear();
                c.setTimeInMillis(timestamp.getTime());
          }

          return c;
      }


      /**
       * Crea un timestamp a partir de un String con fecha en formato dd/MM/yyyy y se añade
       * la hora 00:00:00 al String para generar el Timestamp
       * @param fecha
       * @return
       */
      public static Timestamp getTimestampInicioFromString(String fecha){
          Timestamp time =null;
          String[] datos = fecha.trim().split("/");
          String dia  = datos[0];
          String mes  = datos[1];
          String year = datos[2];

          if(fecha!=null && !"".equals(fecha) && NumericOperations.isInteger(dia) && NumericOperations.isInteger(mes) && NumericOperations.isInteger(year)){
            Calendar c = Calendar.getInstance();
            c.clear();
            c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dia));
            c.set(Calendar.MONTH,Integer.parseInt(mes)-1);
            c.set(Calendar.YEAR,Integer.parseInt(year));
            c.set(Calendar.HOUR,0);
            c.set(Calendar.MINUTE,0);
            c.set(Calendar.SECOND,0);            
            time = new Timestamp(c.getTimeInMillis());
          }

          return time;
      }


     /**
       * Crea un timestamp a partir de un String con fecha en formato dd/MM/yyyy y se añade
       * la hora 00:00:00 al String para generar el Timestamp
       * @param fecha
       * @return
       */
      public static Timestamp getTimestampFinFromString(String fecha){
          Timestamp time =null;
          String[] datos = fecha.trim().split("/");
          String dia  = datos[0];
          String mes  = datos[1];
          String year = datos[2];

          if(fecha!=null && !"".equals(fecha) && NumericOperations.isInteger(dia) && NumericOperations.isInteger(mes) && NumericOperations.isInteger(year)){
            Calendar c = Calendar.getInstance();
            c.clear();
            c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dia));
            c.set(Calendar.MONTH,Integer.parseInt(mes)-1);
            c.set(Calendar.YEAR,Integer.parseInt(year));
            c.set(Calendar.HOUR,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);            
            time = new Timestamp(c.getTimeInMillis());
          }

          return time;
      }



   /**
     * convierte una fecha en formato dd/MM/yyyy en una fecha en el formato indicado en el parámetro FORMATO.
     * @param VALOR: Fecha a convertir en formato dd/MM/yyyy
     * @param FORMATO: formato de la fecha que debe ser de los aceptados por la clase SimpleDateFormat
     * @return String si se ha cambiado el formato de la fecha y espacio en blanco sino se ha podido realizar el cambio
     */
    public static String formatoAlternativoCampoFecha(String VALOR,String FORMATO){
        String result = "";
         try {

             if ((!"".equals(VALOR)) && (VALOR != null)) {
                 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                 java.util.Date fecha = formatter.parse(VALOR);

                 formatter = new SimpleDateFormat(FORMATO);
                 result = formatter.format(fecha);
             }

        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Compara dos string de fecha
     * @param fecha1 Primera fecha a comparar
     * @param fecha2 Segunda fecha a comparar
     * @return boolean true si fecha1 es posterior, false en otro caso
     */
    public static boolean compararFechasString(String fecha1,String fecha2){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        Date date1 = null, date2 = null;
        //convertimos los string en date, puede lanzar exception
        try{
            date1 = formato.parse(fecha1);
            date2 = formato.parse(fecha2);
            return (date1.after(date2));
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
        public static String formatoAlternativoCampoFechaHora(String VALOR,String FORMATO){	
        String result = "";
         try {
             if ((!"".equals(VALOR)) && (VALOR != null)) {	
                 String formatoAuxiliar = "dd/MM/yyyy";	
                 if(FORMATO.indexOf("HH:")!=-1) formatoAuxiliar = "dd/MM/yyyy HH:mm" ;	
                SimpleDateFormat formatter = new SimpleDateFormat(formatoAuxiliar);
                java.util.Date fecha = formatter.parse(VALOR);
                formatter = new SimpleDateFormat(FORMATO);	
                result = formatter.format(fecha);
             }	
        } catch (Exception e) {
            result = "";	
            e.printStackTrace();	
        }	
        return result;	
    }
    

    /**
     * Operacion a realizar en las consultar con campos fechas
     */
    public enum OPERACION_FECHA_BD {
        NADA("-1"),
        IGUAL("0"),
        MAYOR("1"),
        MAYOR_IGUAL("2"),
        MENOR("3"),
        MENOR_IGUAL("4"),
        ENTRE("5"),
        NULO("6"),
        NO_NULO("7");

        private final String modo;

        private OPERACION_FECHA_BD(String modo) {
            this.modo = modo;
        }

        public String getModo() {
            return modo;
        }
        
        public static OPERACION_FECHA_BD obtieneOperacion(String modo) {
            OPERACION_FECHA_BD operacion = null;
            
            for (OPERACION_FECHA_BD op : OPERACION_FECHA_BD.values()) {
                if (op.getModo().equals(modo)) {
                    operacion = op;
                    break;
                }
            }
            
            return operacion;
        }
    }
}//class
/*______________________________EOF_________________________________*/
