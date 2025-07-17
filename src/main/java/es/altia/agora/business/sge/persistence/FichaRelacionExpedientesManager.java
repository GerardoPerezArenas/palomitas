package es.altia.agora.business.sge.persistence;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.sge.persistence.manual.FichaRelacionExpedientesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.sge.FichaRelacionExpedientesForm;

import java.util.Vector;
import es.altia.util.conexion.*;
import java.sql.*;

public class FichaRelacionExpedientesManager {
  /**
    * Código que sigue el patrón de diseño <code>Singleton</code>
    * Los métodos de negocio gestionan que la persistencia sea manual o automática
    * Es protected, por lo que la única manera de instanciar esta clase es usando el factory method <code>getInstance</code>
    */

  // Mi propia instancia usada en el metodo getInstance.
  private static FichaRelacionExpedientesManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(FichaRelacionExpedientesManager.class.getName());


  protected FichaRelacionExpedientesManager() {
    // Fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Mensajes de error localizados
    // m_ConfigError = ConfigServiceHelper.getConfig("error");
    // Servicio de log

  }

  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de SelectManager
  */
  public static FichaRelacionExpedientesManager getInstance() {
    if (instance == null) {
    // Sincronización para serializar (no multithread) las invocaciones a este metodo
    synchronized(FichaRelacionExpedientesManager.class)
    {
      if (instance == null) {
        instance = new FichaRelacionExpedientesManager();
      }
    }
    }
    return instance;
  }

    public Vector filtrarDatosSuplementariosRelacion(Vector estructuraDatosSuplementarios, Vector valoresDatosSuplementarios, 
            String numeroRelacion, String[] params){
        return FichaRelacionExpedientesDAO.getInstance().filtrarDatosSuplementariosRelacion(estructuraDatosSuplementarios,
                valoresDatosSuplementarios,numeroRelacion,params);
    }

    public Vector filtrarEstructuraDatosRelaciones(Vector estructuraDatosSuplementarios,String numRel, String[] params) {
        return FichaRelacionExpedientesDAO.getInstance().filtrarEstructuraDatosRelaciones(estructuraDatosSuplementarios,
                numRel, params);
    }

    public int insertRelacionExpediente(FichaRelacionExpedientesForm inicioForm, String[] params, String utrTramite) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("insertExpediente");

        int res = -1;

        try{
            res = FichaRelacionExpedientesDAO.getInstance().insertRelacionExpediente(inicioForm, utrTramite, params);
        }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            return res;
        }
    }

    public int deshacerRelacionExpedientes(GeneralValueObject gVO, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("deshacerRelacionExpedientes");

        int res = -1;

        try{
            res = FichaRelacionExpedientesDAO.getInstance().deshacerRelacionExpedientes(gVO, params);
        }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            return res;
        }
    }

    public GeneralValueObject cargaRelacionExpedientes(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaRelacionExpedientes");

      try{
        gVO = FichaRelacionExpedientesDAO.getInstance().cargaRelacionExpedientes(gVO, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return gVO;
      }
    }

    public Vector cargaTramites(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaTramites");

      Vector tramites = new Vector();

      try{
        tramites = FichaRelacionExpedientesDAO.getInstance().cargaTramites(gVO, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return tramites;
      }
    }

    public Vector cargaListaExpedientes(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaListaExpedientes");

      Vector enlaces = new Vector();

      try{
        enlaces = FichaRelacionExpedientesDAO.getInstance().cargaListaExpedientes(gVO, params);
      }catch (TechnicalException te) {
        enlaces = new Vector(); // Vacío
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        enlaces = new Vector(); // Vacío
        e.printStackTrace();
      }finally{
        return enlaces;
      }
    }

    public Vector cargaListaInteresados(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaListaInteresados");

      Vector interesados = new Vector();

      try{
        interesados = FichaRelacionExpedientesDAO.getInstance().cargaListaInteresados(gVO, params);
      }catch (TechnicalException te) {
        interesados = new Vector(); // Vacío
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        interesados = new Vector(); // Vacío
        e.printStackTrace();
      }finally{
        return interesados;
      }
    }

    public Vector cargaListaInteresadosxExpediente(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaListaInteresados");

      Vector interesados = new Vector();

      try{
        interesados = FichaRelacionExpedientesDAO.getInstance().cargaListaInteresadosxExpediente(gVO, params);
      }catch (TechnicalException te) {
        interesados = new Vector(); // Vacío
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        interesados = new Vector(); // Vacío
        e.printStackTrace();
      }finally{
        return interesados;
      }
    }

    public Vector cargaTramitesDisponibles(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaTramitesDisponibles");

      Vector tramites = new Vector();

      try{
        tramites = FichaRelacionExpedientesDAO.getInstance().cargaTramitesDisponibles(gVO, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return tramites;
      }
    }

    public int grabarRelacion(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("grabarRelacionExpediente");

      int resultado = 0;
      try{
        resultado  = FichaRelacionExpedientesDAO.getInstance().grabarRelacion(gVO, params);
      }catch (TechnicalException te) {
        resultado=0;
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        resultado = 0;
        e.printStackTrace();
      }finally{
        return resultado;
      }
    }

    public int retrocederRelacion(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("retrocederRelacion");

      int res = -1;

      try{
        res = FichaRelacionExpedientesDAO.getInstance().retrocederRelacion(gVO, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return res;
      }
    }
}
