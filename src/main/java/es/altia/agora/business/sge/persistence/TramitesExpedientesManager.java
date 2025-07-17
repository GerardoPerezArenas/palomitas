package es.altia.agora.business.sge.persistence;

import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.ArrayList;

public class TramitesExpedientesManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static TramitesExpedientesManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(TramitesExpedientesManager.class.getName());          

  protected TramitesExpedientesManager() {
    //Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }


  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de FichaExpedienteManager
  */
  public static TramitesExpedientesManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
      synchronized(TramitesExpedientesManager.class) {
        if (instance == null)
        instance = new TramitesExpedientesManager();
      }
    }
    return instance;
  }


  public Vector getTramitesExpedienteSinFinalizar(TramitacionExpedientesValueObject teVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getTramitesExpedienteSinFinalizar");
    Vector lista = new Vector();

    try{
      lista = TramitesExpedienteDAO.getInstance().getTramitesExpedienteSinFinalizar(teVO,params);
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }

    public Vector getTramitesRelacionSinFinalizar(TramitacionExpedientesValueObject teVO,String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getTramitesRelacionSinFinalizar");
      Vector lista = new Vector();

      try{
        lista = TramitesExpedienteDAO.getInstance().getTramitesRelacionSinFinalizar(teVO,params);
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }

    public String getDescripcionTramInicio(String codProcedimiento,String[] params){
        m_Log.debug("getDescripcionTramInicio init");
        String des = "";
        AdaptadorSQLBD oad = null;
        Connection con = null;
        try{
             oad = new AdaptadorSQLBD(params);

            des = TramitesExpedienteDAO.getInstance().getDescripcionTramInicio(codProcedimiento, con);
        }
        catch(Exception e){
            e.printStackTrace();
            m_Log.debug("Error: " + e.getMessage());
        } finally{
            try{
                oad.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return des;
    }


    public ArrayList<GeneralValueObject> getOcurrenciaTramiteExpediente(GeneralValueObject gvo,String[] params) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
         ArrayList<GeneralValueObject> tramites = new  ArrayList<GeneralValueObject>();
        try{
             adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            tramites= TramitesExpedienteDAO.getInstance().getOcurrenciaTramiteExpediente(gvo, con);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return tramites;
    }

}

