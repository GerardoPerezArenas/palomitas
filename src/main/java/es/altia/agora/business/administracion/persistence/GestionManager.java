package es.altia.agora.business.administracion.persistence;

import java.util.Vector;

import es.altia.agora.business.administracion.GestionValueObject;
import es.altia.agora.business.administracion.HorarioCentroValueObject;
import es.altia.agora.business.administracion.exception.GestionException;
import es.altia.agora.business.administracion.persistence.manual.AgendaDAO;
import es.altia.agora.business.administracion.persistence.manual.CalendarioGeneralDAO;
import es.altia.agora.business.select.persistence.manual.SelectListaDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GestionManager {
    /**
     * Esto es un código que sigue el patrón de diseño <code>Singleton</code>
     * Los métodos de negocio gestionan que la persistencia sea manual o automática
     * Es protected, por lo que la única manera de instanciar esta clase es usando el factory method <code>getInstance</code>
     */
    protected GestionManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }


    public GestionValueObject inicCalendarioGeneral(GestionValueObject laGestion,String[] params) throws GestionException{

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("inicCalendarioLaboral");

        try{
            //if(m_Log.isInfoEnabled()) m_Log.info("esta en el try del manager");
            CalendarioGeneralDAO.getInstance().obtenerFestivos(laGestion,params);
            CalendarioGeneralDAO.getInstance().obtenerListaDiasOcupados(laGestion,params);
        } catch (Exception ce) {
              m_Log.error("Problema técnico de JDBC " + ce.getMessage());
              throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
          }

        m_Log.debug("Obtenidos los dias festivos del calendario laboral general");

        return laGestion;
    }


    public void grabarCalendarioGeneral(Vector festivos,String[] params) throws GestionException{

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("grabarCalendarioGeneral");

        try{
            CalendarioGeneralDAO.getInstance().eliminarFestivos(params);
            CalendarioGeneralDAO.getInstance().grabarFestivos(festivos,params);

        }catch (Exception ce){
            m_Log.error("Problema técnico de JDBC " + ce.getMessage());
            throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
         }

        m_Log.debug("Grabados los dias festivos del calendario laboral general");
    }


    public GestionValueObject inicCalendarioCentro(GestionValueObject laGestion) throws GestionException{

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("inicCalendarioCentro");

        try{
            //Obtenemos la lista de codigos y descripciones de centros+ubicaciones.
            laGestion.setLista_centros_ubicaciones(SelectListaDAO.getInstance().loadListaCentrosUbic());

            //CalendarioGeneralDAO.getInstance().obtenerFestivos(laGestion);
            CalendarioGeneralDAO.getInstance().obtenerFestivosCentro(laGestion);
            CalendarioGeneralDAO.getInstance().obtenerVacacionesCentro(laGestion);
            CalendarioGeneralDAO.getInstance().obtenerListaDiasOcupadosCentro(laGestion);

        }catch (Exception ce){
            m_Log.error("Problema técnico de JDBC " + ce.getMessage());
            throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
         }

        m_Log.debug("Obtenidos los dias festivos y de vacaciones del calendario laboral del centro");

        return laGestion;
    }


    public void grabarCalendarioCentro(Vector festivosCentro, Vector vacacionesCentro, GestionValueObject laGestion) throws GestionException{

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("grabarCalendarioCentro");

        try{
            CalendarioGeneralDAO.getInstance().eliminarFestivosCentro(laGestion);
            CalendarioGeneralDAO.getInstance().grabarFestivosCentro(festivosCentro, vacacionesCentro, laGestion);
        }catch (Exception ce){
            m_Log.error("Problema técnico de JDBC " + ce.getMessage());
            throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
         }

        m_Log.debug("Grabados los dias festivos y de vacaciones del calendario del centro");
    }


    public GestionValueObject inicHorarioCentro(GestionValueObject laGestion) throws GestionException{

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("inicHorarioCentro");

        try{
            //Obtenemos la lista de codigos y descripciones de centros+ubicaciones.
            laGestion.setLista_centros_ubicaciones(SelectListaDAO.getInstance().loadListaCentrosUbic());

            CalendarioGeneralDAO.getInstance().obtenerHorarioCentro(laGestion);
            CalendarioGeneralDAO.getInstance().obtenerListaDiasOcupadosCentroParaHorario(laGestion);


        } catch (Exception ce) {
              m_Log.error("Problema técnico de JDBC " + ce.getMessage());
              throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
          }

        m_Log.debug("Obtenido el horario laboral del centro");

        return laGestion;
    }

    public Vector obtenerFestivosPorAno(String ano,String[] params) throws GestionException{

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("obtenerFestivosPorAno");
        Vector diasFestivos = new Vector();

        try{

            diasFestivos = CalendarioGeneralDAO.getInstance().obtenerFestivosPorAno(ano,params);

        } catch (Exception ce) {
              m_Log.error("Problema técnico de JDBC " + ce.getMessage());
              throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
          }

        m_Log.debug("Obtenido los dias festivos");

        return diasFestivos;
    }


    public void grabarHorarioCentro(Vector losHorarioCentro, GestionValueObject laGestion) throws GestionException{

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("grabarHorarioCentro");

        try{
            CalendarioGeneralDAO.getInstance().eliminarHorarioCentro(laGestion);
            CalendarioGeneralDAO.getInstance().grabarHorarioCentro(losHorarioCentro, laGestion);
        }catch (Exception ce){
            m_Log.error("Problema técnico de JDBC " + ce.getMessage());
            throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
         }

        m_Log.debug("Grabado el horario del centro");
    }


    public void generarAgendasCentro(Vector losHorarioCentro, GestionValueObject laGestion) throws GestionException{
      //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("generarAgendasCentro");

        try{
            HorarioCentroValueObject horarioVO;
            for (int i = 0; i<losHorarioCentro.size(); i++)
            {
        horarioVO = (HorarioCentroValueObject)losHorarioCentro.elementAt(i);
                String f_ini = horarioVO.getFecha_inicio();
                String f_fin = horarioVO.getFecha_fin();
                String centro = laGestion.getCentro();
            String ubic = laGestion.getUbicacion();
            AgendaDAO.getInstance().generarAgendaCentro(f_ini, f_fin, centro, ubic);
            }
        }catch (Exception ce){
            m_Log.error("Problema técnico de JDBC " + ce.getMessage());
            throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
         }
        m_Log.debug("Generadas las agendas del centro");
    }


    public void generarAgendaPorDiasCentro(Vector fechas, String centro, String ubic) throws GestionException{
      //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("generarAgendaDiaCentro");

        try
        {
            String fecha = new String();
            for (int i = 0; i<fechas.size(); i++)
            {
                fecha = (String)fechas.elementAt(i);
            AgendaDAO.getInstance().generarAgendaCentro(fecha, fecha, centro, ubic);
            }
        }catch (Exception ce){
            m_Log.error("Problema técnico de JDBC " + ce.getMessage());
            throw new GestionException("Problema técnico de JDBC " + ce.getMessage());
         }
        m_Log.debug("Generada la agenda del centro para un día");
    }

    /**
     * Factory method para el <code>Singelton</code>.
     * @return La unica instancia de PersonaFisicaManager
     */
    public static GestionManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized(GestionManager.class) {
                if (instance == null) {
                    instance = new GestionManager();
                }
            }
        }
        return instance;
    }

    // Mi propia instancia usada en el metodo getInstance
    private static GestionManager instance = null;

    // Declaracion de servicios
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion technical
    protected static Config m_ConfigError; // Para el fichero de mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(GestionManager.class.getName());
}
