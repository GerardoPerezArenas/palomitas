// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion;

// PAQUETES IMPORTADOS
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import java.util.HashMap;


public class MyHttpSessionListener implements HttpSessionListener {

  private static int activeSessions = 0;

  public static HashMap mapa=new HashMap();
  


  public void sessionCreated(HttpSessionEvent se) {
      
    SessionInfo infoSesion=new SessionInfo(se.getSession());
    
    try {
        mapa.put(se.getSession().getId(), infoSesion);
       
     }catch(Exception e)
     {
          e.printStackTrace();
     }

  }

  public void sessionDestroyed(HttpSessionEvent se) {

      if(!mapa.isEmpty())
      mapa.remove(se.getSession().getId());
  }

  public static int getActiveSessions() {
    return activeSessions;
  }
  public static HashMap getInfoSessions() {
    
      if(!mapa.isEmpty())
      return mapa;
      else return null;
  }
  


}
