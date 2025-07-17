// NOMBRE DEL PAQUETE
package es.altia.agora.business.util;

import java.util.Date;
import java.util.Calendar;

public class ComparaFecha  
{

  public ComparaFecha() {}

  public boolean compararFechas(Date fecha1, Date fecha2) 
  {
     if(fecha1.compareTo(fecha2)<=0) 
     {return false;} 
     else 
     {return true;}
  }
 
  /**
   * Devuelve un objeto Calendar a partir de un String con el siguiente formato dd/mm/yyyy
   * @param fecha. String fecha con el formato dd/mm/yyyy
   * @return Objeto calendar
   */

   public Calendar obtenerCalendar(String fecha)
   {
	   Calendar calendario = Calendar.getInstance();
	   int dia = new Integer(fecha.substring(0,2)).intValue();
	   int mes = new Integer(fecha.substring(3,5)).intValue()-1;
	   int anno = new Integer(fecha.substring(6,fecha.length())).intValue();
	   calendario.set(anno,mes,dia);
	   return calendario;
   }
    
 /**
   * Si fecha1 es anterior  o igual a fecha2 devuelve true
   * Si fecha1 es posterior a fecha2 devuelve false
   * @param fecha1. String fecha con el formato dd/mm/yyyy
   * @param fecha2. String fecha con el formato dd/mm/yyyy
   * @return 1 si fecha1 es anterior a fecha2
   *		 0 si son iguales
   *		-1 si fecha1 es posterior a fecha2
   */
  public int compararFechas(String fecha1,String fecha2)
  {
	 int devolver = 0;
	 Calendar calFecha1 = obtenerCalendar(fecha1);
	 Calendar calFecha2 = obtenerCalendar(fecha2);
	 if (calFecha2.after(calFecha1))
	 	devolver = 1;
	 else if (calFecha2.equals(calFecha1))
	 	devolver = 0;
	 else
	 	devolver = -1;
	 return devolver;
	 
	 //return ((calFecha2.after(calFecha1))||(calFecha2.equals(calFecha1)));
  } 

}
