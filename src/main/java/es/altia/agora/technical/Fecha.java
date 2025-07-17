package es.altia.agora.technical;

import java.sql.Date;
import java.util.Calendar;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Fecha {
    protected static Log m_Log = LogFactory.getLog(Fecha.class.getName());

    public Fecha (){

    }

    public static java.util.Date obtenerDate(String s){

       int dia,mes,anho;

       try{
           m_Log.debug("dia: " + s.substring(0,2));
           m_Log.debug("mes: " + s.substring(3,5));
           m_Log.debug("mes: " + s.substring(6,10));
            dia=Integer.parseInt(s.substring(0,2));
            mes=Integer.parseInt(s.substring(3,5))-1;
            anho=Integer.parseInt(s.substring(6,10))-1900;
            return new Date(anho,mes,dia);
       }
       catch(Exception ex){
          return null;
       }
    }

    public static String obtenerString (java.util.Date date){
      String fecha="";
      try{
        int dia = date.getDate();
        if (dia < 10)
            fecha = "0" + String.valueOf(dia) + "/" ;
        else
            fecha = String.valueOf(dia) + "/" ;

        int mes = date.getMonth()+1;
        if(mes < 10)
            fecha = fecha + "0" + String.valueOf(mes)+ "/" ;
        else
            fecha = fecha + String.valueOf(mes) + "/" ;

        int anho = date.getYear() + 1900;
        fecha = fecha + String.valueOf(anho) ;

        return fecha;
      }catch(Exception ex){
            return "";
      }
    }

    /** Convierte un objeto Calendar en un java.sql.Timestamp
     * @param Calendar
     * @return Timestamp
     */
    public static java.util.Calendar toCalendar(java.sql.Timestamp c){
        Calendar ca = Calendar.getInstance();
        ca.clear();
        ca.setTimeInMillis(c.getTime());
        return ca;
    }

  //obtiene un Date a partir del formato 2002-01-02 09:00
  public static java.util.Date obtenerDateCompleto(String s){

        int dia,mes,anho,hora,minuto;
        dia=Integer.parseInt(s.substring(8,10));
        mes=Integer.parseInt(s.substring(5,7))-1;
        anho=Integer.parseInt(s.substring(0,4))-1900;
        hora=Integer.parseInt(s.substring(11,13));
        minuto=Integer.parseInt(s.substring(14,16));
        java.util.Date fecha=new java.util.Date(anho, mes, dia, hora, minuto);
        if(m_Log.isDebugEnabled()) m_Log.debug(" > " + fecha);
        return fecha;
  }

  //obtiene un Date a partir del formato 01-02-2002 09:00
  public static java.util.Date obtenerDateCompleto2(String s){

        int dia,mes,anho,hora,minuto;
        dia=Integer.parseInt(s.substring(0,2));
        mes=Integer.parseInt(s.substring(3,5))-1;
        anho=Integer.parseInt(s.substring(6,10))-1900;
        hora=Integer.parseInt(s.substring(11,13));
        minuto=Integer.parseInt(s.substring(14,16));
        java.util.Date fecha=new java.util.Date(anho, mes, dia, hora, minuto);
        return fecha;
  }

  //obtiene un Date a partir del formato 01-02-2002 09:00:00 (segundos incluidos)
  public static java.util.Date obtenerDateCompleto3(String s){

        int dia,mes,anho,hora,minuto,segundo;
        dia=Integer.parseInt(s.substring(0,2));
        mes=Integer.parseInt(s.substring(3,5))-1;
        anho=Integer.parseInt(s.substring(6,10))-1900;
        hora=Integer.parseInt(s.substring(11,13));
        minuto=Integer.parseInt(s.substring(14,16));
        segundo=Integer.parseInt(s.substring(17,19));
        java.util.Date fecha=new java.util.Date(anho, mes, dia, hora, minuto, segundo);
        if(m_Log.isDebugEnabled()) m_Log.debug(" > " + fecha);
        return fecha;
  }


    //Obtiene los siguientes 5 días laborables a la hora indicada
    public Vector obtenerSig5DiasLaborables(Calendar dia) {

        int cont = 0;
        Vector vecFechas = new Vector();
        java.util.Date date1 = dia.getTime();
        String fechaCompleta1 = this.construirFechaCompleta(date1);
        m_Log.debug(fechaCompleta1);
        vecFechas.addElement(fechaCompleta1);
        if(m_Log.isDebugEnabled()) m_Log.debug("dia de la semana: " + dia.get(Calendar.DAY_OF_WEEK));
        m_Log.debug("Siguientes dias:");
        while (cont<5) {
                dia.add(Calendar.DAY_OF_MONTH, 1);
                int diaDeLaSemana = dia.get(Calendar.DAY_OF_WEEK); //1:Domingo 2:Lunes ... 7:Sábado
                if (diaDeLaSemana!=1 && diaDeLaSemana!=7) {
                    java.util.Date date = dia.getTime();
                    String fechaCompleta = this.construirFechaCompleta(date);
                    m_Log.debug(fechaCompleta);
                    vecFechas.addElement(fechaCompleta);
                    if(m_Log.isDebugEnabled()) m_Log.debug("dia de la semana: " + dia.get(Calendar.DAY_OF_WEEK));
                    cont+=1;
                }
        }
        return vecFechas;
    }

    //Obtiene los siguientes 5 días laborables a partir de la hora indicada
    public Vector obtenerSig5DiasLaborablesHora(Calendar dia) {

        int cont = 0;
        Vector vecFechas = new Vector();
        java.util.Date date1 = dia.getTime();
        String fechaCompleta1 = this.construirFechaCompleta(date1);
        m_Log.debug(fechaCompleta1);
        vecFechas.addElement(fechaCompleta1);
        if(m_Log.isDebugEnabled()) m_Log.debug("dia de la semana: " + dia.get(Calendar.DAY_OF_WEEK));
        m_Log.debug("Siguientes dias:");
        while (cont < 5) {
                dia.add(Calendar.DAY_OF_MONTH, 1);
                fechaCompleta1 = this.construirFechaCompleta(dia.getTime());
                dia.set(this.getAnoInteger(fechaCompleta1),this.getMesInteger(fechaCompleta1),this.getDiaInteger(fechaCompleta1),1,0);
                int diaDeLaSemana = dia.get(Calendar.DAY_OF_WEEK); //1:Domingo 2:Lunes ... 7:Sábado
                if (diaDeLaSemana!=1 && diaDeLaSemana!=7) {
                    java.util.Date date = dia.getTime();
                    String fechaCompleta = this.construirFechaCompleta(date);
                    m_Log.debug(fechaCompleta);
                    vecFechas.addElement(fechaCompleta);
                    if(m_Log.isDebugEnabled()) m_Log.debug("dia de la semana: " + dia.get(Calendar.DAY_OF_WEEK));
                    cont += 1;
                }
        }
        return vecFechas;
    }

    public int getAnoInteger(String fechaCita) {

        int ano = Integer.parseInt(fechaCita.substring(6,10));
        if(m_Log.isDebugEnabled()) m_Log.debug(" ANO -> " + ano);
        return ano;
    }

    public int getMesInteger(String fechaCita) {

        int mes = Integer.parseInt(fechaCita.substring(3,5));
        mes -=1; //Restamos 1 al mes pq van del 0 al 11
        if(m_Log.isDebugEnabled()) m_Log.debug(" MES -> " + mes);
        return mes;
    }

    public int getDiaInteger(String fechaCita) {

        int dia = Integer.parseInt(fechaCita.substring(0,2));
        if(m_Log.isDebugEnabled()) m_Log.debug(" DIA -> " + dia);
        return dia;
    }

    public int getHorasInteger(String HoraCita) {

        int horas = Integer.parseInt(HoraCita.substring(0,2));
        if(m_Log.isDebugEnabled()) m_Log.debug(" HORA -> " + horas);
        return horas;
    }

    public int getMinutosInteger(String HoraCita) {

        int minutos = Integer.parseInt(HoraCita.substring(3,5));
        if(m_Log.isDebugEnabled()) m_Log.debug(" MINUTO -> " + minutos);
        return minutos;
    }

    public String construirFechaCompleta(java.util.Date date) {

        String fechaCompleta="";
        if (date.getDate()<10)
            fechaCompleta="0"+date.getDate();
        else
            fechaCompleta=fechaCompleta+date.getDate();
        if ((date.getMonth()+1)<10)
            fechaCompleta=fechaCompleta+"/0"+(date.getMonth()+1);
        else
            fechaCompleta=fechaCompleta+"/"+(date.getMonth()+1);

        fechaCompleta=fechaCompleta+"/"+(date.getYear()+1900);

        if (date.getHours()<10)
            fechaCompleta=fechaCompleta+" 0"+date.getHours();
        else
            fechaCompleta=fechaCompleta+" "+date.getHours();
        if (date.getMinutes()<10)
            fechaCompleta=fechaCompleta+":0"+date.getMinutes();
        else
            fechaCompleta=fechaCompleta+":"+date.getMinutes();

        return fechaCompleta;
    }

    public String construirFecha(java.util.Date date) {

        String fecha="";
        if (date.getDate()<10)
            fecha="0"+date.getDate();
        else
            fecha=fecha+date.getDate();
        if ((date.getMonth()+1)<10)
            fecha=fecha+"/0"+(date.getMonth()+1);
        else
            fecha=fecha+"/"+(date.getMonth()+1);
        fecha=fecha+"/"+(date.getYear()+1900);
        m_Log.debug(fecha);
        return fecha;
    }

    public String construirHora(java.util.Date date) {

        String hora="";
        if (date.getHours()<10)
            hora=hora+"0"+date.getHours();
        else
            hora=hora+date.getHours();
        if (date.getMinutes()<10)
            hora=hora+":0"+date.getMinutes();
        else
            hora=hora+":"+date.getMinutes();
         if (date.getSeconds()<10)
            hora=hora+":0"+date.getSeconds();
        else
            hora=hora+":"+date.getSeconds(); 
        return hora;
    }

    public String obtenerDiaSemana(java.util.Date date) {

        String dia="";
        int numDia;

        numDia = date.getDay();
        if (numDia==0) dia="Domingo";
        if (numDia==1) dia="Lunes";
        if (numDia==2) dia="Martes";
        if (numDia==3) dia="Miércoles";
        if (numDia==4) dia="Jueves";
        if (numDia==5) dia="Viernes";
        if (numDia==6) dia="Sábado";
        if(m_Log.isDebugEnabled()) m_Log.debug("Día de la semana: " + dia);

        return dia;
    }

    public String obtenerDiaSemana(java.util.Date date, String idioma) {

        String dia="";
        int numDia;

        numDia = date.getDay();
        if ((numDia==0) && (idioma.trim().equals("espanol"))) dia="Domingo";
        if ((numDia==0) && (!idioma.trim().equals("espanol"))) dia="Igandea";
        if ((numDia==1) && (idioma.trim().equals("espanol"))) dia="Lunes";
        if ((numDia==1) && (!idioma.trim().equals("espanol"))) dia="Astelehena";
        if ((numDia==2) && (idioma.trim().equals("espanol"))) dia="Martes";
        if ((numDia==2) && (!idioma.trim().equals("espanol"))) dia="Asteartea";
        if ((numDia==3) && (idioma.trim().equals("espanol"))) dia="Miércoles";
        if ((numDia==3) && (!idioma.trim().equals("espanol"))) dia="Asteaskena";
        if ((numDia==4) && (idioma.trim().equals("espanol"))) dia="Jueves";
        if ((numDia==4) && (!idioma.trim().equals("espanol"))) dia="Osteguna";
        if ((numDia==5) && (idioma.trim().equals("espanol"))) dia="Viernes";
        if ((numDia==5) && (!idioma.trim().equals("espanol"))) dia="Ostirala";
        if ((numDia==6) && (idioma.trim().equals("espanol"))) dia="Sábado";
        if ((numDia==6) && (!idioma.trim().equals("espanol"))) dia="Larunbata";

        if(m_Log.isDebugEnabled()) m_Log.debug("Día de la semana: " + dia);

        return dia;
    }


    public String obtenerHoraDeFechaCompleta (String fechaComp) {

        int posDosPuntos = fechaComp.indexOf(":");
        if(m_Log.isDebugEnabled()) m_Log.debug(" posicion dos puntos = " + posDosPuntos);
        String hora = fechaComp.substring(posDosPuntos-3, posDosPuntos+3);
        if(m_Log.isDebugEnabled()) m_Log.debug(" Hora -> " + hora);

        return hora;
    }

    public java.util.Date obtenerDiasLaborablesAntes( int n_dias, java.util.Date fecha_act) {

      Calendar fecha_cal = Calendar.getInstance();
      String fecha_string = obtenerString(fecha_act);
      fecha_cal.set(this.getAnoInteger(fecha_string),this.getMesInteger(fecha_string),this.getDiaInteger(fecha_string));
      fecha_cal.add(Calendar.DATE , -n_dias); //Resta n días a la fecha
      fecha_act = fecha_cal.getTime();
      while (fecha_act.getDay()==0 || fecha_act.getDay()==6) {
        fecha_cal.add(Calendar.DATE , -1);
        fecha_act = fecha_cal.getTime();
      }
      return fecha_act;
    }

    public String obtenerDiasLaborablesAntes( int n_dias, String fecha_act) {

        Calendar fecha_cal = Calendar.getInstance();
        if(m_Log.isDebugEnabled()) m_Log.debug(" fecha_act: " + fecha_act);
        String fecha_string = fecha_act;
        java.util.Date fecha_date = new java.util.Date();
        fecha_cal.set(this.getAnoInteger(fecha_string),this.getMesInteger(fecha_string),this.getDiaInteger(fecha_string));
        fecha_cal.add(Calendar.DATE , -n_dias); //Resta n días a la fecha
        fecha_date = fecha_cal.getTime();
        while (fecha_date.getDay()==0 || fecha_date.getDay()==6) {
            fecha_cal.add(Calendar.DATE , -1);
            fecha_date = fecha_cal.getTime();
        }
        if(m_Log.isDebugEnabled()) m_Log.debug(" ** " + fecha_date);
        fecha_act = this.construirFecha(fecha_date);
        return fecha_act;
    }

    public String obtenerFechaFinal(String fecha) {

        Calendar fecha_cal = Calendar.getInstance();
        fecha_cal.set(this.getAnoInteger(fecha),this.getMesInteger(fecha),this.getDiaInteger(fecha), 23,59);
        return this.construirFechaCompleta(fecha_cal.getTime());
    }

    public String obtenerMinutos(java.util.Date date) {

        String fechaCompleta = "";
        if (date.getHours()<10)
            fechaCompleta="0"+date.getHours();
        else
            fechaCompleta=""+date.getHours();
        if (date.getMinutes()<10)
            fechaCompleta=fechaCompleta+":0"+date.getMinutes();
        else
            fechaCompleta=fechaCompleta+":"+date.getMinutes();
        if (date.getSeconds()<10)
            fechaCompleta=fechaCompleta+":0"+date.getSeconds();
        else
            fechaCompleta=fechaCompleta+":"+date.getSeconds();
        return fechaCompleta;
    }

    //Devuelve un objeto de tipo Calendar q contendrá la fecha y hora q se pasan como parametros
    //Fecha:     dd/mm/aaaa
    //Horario:   hh:mm
    public java.util.Calendar obtenerCalendar(String fecha, String horario)
    {
      int anyo = this.getAnoInteger(fecha) - 1900;
      int mes = this.getMesInteger(fecha);
      int dia = this.getDiaInteger(fecha);
      int horas = this.getHorasInteger(horario);
      int mins = this.getMinutosInteger(horario);
      java.util.Date day = new java.util.Date (anyo, mes, dia, horas, mins);
      //Creamos un objeto de tipo Calendar,
      Calendar cal = Calendar.getInstance();
      cal.setTime(day);
      return cal;
    }

    public static String obtenerStringCompleto (java.util.Date date){
      String fecha="";
      try{
        int dia = date.getDate();
        if (dia < 10)
          fecha = "0" + String.valueOf(dia) + "/" ;
        else
          fecha = String.valueOf(dia) + "/" ;

        int mes = date.getMonth()+1;
        if(mes < 10)
          fecha = fecha + "0" + String.valueOf(mes)+ "/" ;
        else
          fecha = fecha + String.valueOf(mes) + "/" ;

        int anho = date.getYear() + 1900;
        fecha = fecha + String.valueOf(anho) ;


        int hora = date.getHours();
        if( hora < 10 )
          fecha = fecha + " 0"+ String.valueOf(hora) ;
        else
          fecha = fecha + " "+ String.valueOf(hora) ;


        int min = date.getMinutes();
        if( min < 10 )
          fecha = fecha + ":0"+ String.valueOf(min) ;
        else
          fecha = fecha + ":"+ String.valueOf(min) ;

        return fecha;
      }catch(Exception ex){
        return "";
      }
    }

    /**
     * Un año es bisisesto cuando su valor es múltiplo de 4 y
     * no es múltiplo de 100. Sin embargo, si que son bisiestos
     * los años seculares, esto es, los múltiplos de 400
     *
     * @param año entero representando el año
     * @return boolean segun el año sea o no bisiesto
     */
    public static boolean bisiesto(int año) {
        return ((año%4==0 && año%100!=0) || (año%400==0));
    }

    /**
     * Devuelve el número de días del mes, considerando
     * febrero como de 28 o 29 días según el año sea o no
     * bisiesto
     *
     * Debe ejecutarse con un mes y un año válido.
     * @param mes int representando el mes (de 1 a 12).
     * @param año int representando el año.
     * @return int indicando el numero de dias del mes
     */
    public static int numDiasMes(int mes, int año) {
        int dias = 31;
        switch (mes) {
            case 2: if (bisiesto(año)) dias=29; else dias=28;break;
            case 4:
            case 6:
            case 9:
            case 11: dias = 30; break;
        }
        return dias;
    }
}