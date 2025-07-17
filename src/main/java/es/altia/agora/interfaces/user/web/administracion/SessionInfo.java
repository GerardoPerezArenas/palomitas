// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;



  
public class SessionInfo {
    /*_______Constants______________________________________________*/

    /*_______Attributes_____________________________________________*/
    private String pId = null;
    private HttpSession httpSession = null;

    /*_______Operations_____________________________________________*/
    public SessionInfo(HttpSession session) {
        httpSession = session;
        pId = session.getId();
    }//constructor

    protected HttpSession getHttpSession() {
        return httpSession;
    }

    public String getId() {
        return pId;
    }//getId

    public void clear() {
        httpSession = null;
    }//clear

    public int getIdleTime() {
        try {
            long diffMilliSeconds =  System.currentTimeMillis() - getLastAccessedTime();
            return (int) diffMilliSeconds;
        } catch (Exception ise) {
            //ignore: invalidated session
            return -1;
        }
    }

    public int getTTL() {
        try {
            long diffMilliSeconds = (1000*getMaxInactiveInterval()) - (System.currentTimeMillis() - getLastAccessedTime());
            return (int) diffMilliSeconds;
        } catch (Exception ise) {
            //ignore: invalidated session
            return -1;
        }
    }

    public int getAge() {
        try {
            long diffMilliSeconds = getLastAccessedTime() - getCreationTime();
            return (int) diffMilliSeconds;
        } catch (Exception ise) {
            //ignore: invalidated session
            return -1;
        }
    }

    public long getCreationTime() {
        try {
            return (httpSession==null) ? -1 : httpSession.getCreationTime();
        } catch (Exception ise) {
            //ignore: invalidated session
            return -1;
        }
    }

    public Date getCreationTimeAsDate() {
        return new Date(getCreationTime());
    }
    public String getCreationTimeAsString()
    {
        String fecha="";
        Date f=new Date(getCreationTime());
        fecha=dateToString(f);
        return fecha;
    }

    public long getLastAccessedTime() {
        try {
            return (httpSession==null) ? -1 : httpSession.getLastAccessedTime();
        } catch (Exception ise) {
            //ignore: invalidated session
            return -1;
        }
    }

    public Date getLastAccessedTimeAsDate() {
        return new Date(getLastAccessedTime());
    }
    public String getLastAccesedTimeAsString()
    {
        String fecha="";
        Date f=new Date(getLastAccessedTime());
        fecha=dateToString(f);
        return fecha;
    }

    public int getMaxInactiveInterval() {
        try {
            return (httpSession==null) ? -1 : httpSession.getMaxInactiveInterval();
        } catch (Exception ise) {
            //ignore: invalidated session
            return 0;
        }
    }



    public boolean invalidate() {
        if (httpSession!=null) {
            try {
                httpSession.invalidate();
                return true;
            } catch (Exception ise) {
                //ignore
            }//try-catch
        }//if
        return false;
    }//invalidate

    private static String dateToString (java.util.Date date){
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

        int sec = date.getSeconds();
        if( sec < 10 )
          fecha = fecha + ":0"+ String.valueOf(sec) ;
        else
          fecha = fecha + ":"+ String.valueOf(sec) ;

        return fecha;
      }catch(Exception ex){
        return "";
      }
    }
    
    // Comprueba si existe el atributo usuarioEscritorio y si es así devuelve el login
    public static String getSessionUserLogin(HttpSession session){
        String textoLogin = "Usuario logado: ";
        if(session.getAttribute("usuarioEscritorio") != null){
            UsuarioEscritorioValueObject usuarioEscritorioValueObject = (UsuarioEscritorioValueObject) session.getAttribute("usuarioEscritorio");
            textoLogin += usuarioEscritorioValueObject.getLogin();
        } else {
            textoLogin += "--";
        }
        return textoLogin;
    }


}//de la clase