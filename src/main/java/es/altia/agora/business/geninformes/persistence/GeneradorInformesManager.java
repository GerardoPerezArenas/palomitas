// NOMBRE DEL PAQUETE
package es.altia.agora.business.geninformes.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.agora.business.geninformes.persistence.manual.GeneradorInformesDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.util.GeneralValueObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DiligenciasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class GeneradorInformesManager {

  private static GeneradorInformesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(GeneradorInformesManager.class.getName());

  protected GeneradorInformesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static GeneradorInformesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(GeneradorInformesManager.class) {
        if (instance == null) {
                    instance = new GeneradorInformesManager();
                }
            }
        }
        return instance;
    }

    public Vector getListaEntidades(String codAplicacion,String[] params)
            throws AnotacionRegistroException{

        Vector res = null;

        m_Log.info("getListaEntidades");

        try {

            m_Log.debug("Usando persistencia manual");
            res = GeneradorInformesDAO.getInstance().getListaEntidades(codAplicacion,params);
            m_Log.debug("lista de entidades obtenidos");

        } catch (Exception ce) {
            res = null;
            if ( m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return res;
    }

    public Vector getListaCampos(String codEntidad,String[] params)
            throws AnotacionRegistroException{

        Vector res = null;

        m_Log.info("getListaCampos");

        try {

            m_Log.debug("Usando persistencia manual");
            res = GeneradorInformesDAO.getInstance().getListaCampos(codEntidad,params);
            m_Log.debug("lista de campos obtenidos");

        } catch (Exception ce) {
            res = null;
            if ( m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return res;
    }

    public String getNombreVista(String codEntidad,String[] params) throws AnotacionRegistroException{
        String nombreVista = "";
        m_Log.info("getNombreVista");

        try {
            m_Log.debug("Usando persistencia manual");
            nombreVista = GeneradorInformesDAO.getInstance().getNombreVista(codEntidad,params);
            m_Log.debug("lista de campos obtenidos");

        } catch (Exception ce) {
            nombreVista = null;
            if ( m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return nombreVista;
    }

    public int altaInforme(GeneralValueObject gVO,String[] params)
            throws AnotacionRegistroException {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("altaInforme");
        int i;

        try {
            m_Log.debug("Usando persistencia manual");

            i = GeneradorInformesDAO.getInstance().altaInforme(gVO,params);

            m_Log.debug("estructura informe insertado correctamente");

        } catch (Exception ce) {
            if ( m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

        }
        return i;
    }

    public GeneralValueObject obtenerInforme(GeneralValueObject gVO,String[] params)
            throws AnotacionRegistroException{

        m_Log.info("obtenerInforme");

        try {

            m_Log.debug("Usando persistencia manual");
            gVO = GeneradorInformesDAO.getInstance().obtenerInforme(gVO,params);
            m_Log.debug("informe obtenidos");

        } catch (Exception ce) {
            gVO = null;
            if ( m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return gVO;
    }

    public int modificarInforme(GeneralValueObject gVO,String[] params)
            throws AnotacionRegistroException {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("modificarInforme");
        int i;

        try {
            m_Log.debug("Usando persistencia manual");

            i = GeneradorInformesDAO.getInstance().modificarInforme(gVO,params);

            m_Log.debug("estructura informe modificado correctamente");

        } catch (Exception ce) {
            if ( m_Log.isErrorEnabled())m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

        }
        return i;
    }

    public Vector listaCamposDisponiblesEnteros(String codEntidad,String[] params)
            throws AnotacionRegistroException{

        Vector res = null;

        m_Log.info("listaCamposDisponiblesEnteros");

        try {

            m_Log.debug("Usando persistencia manual");
            res = GeneradorInformesDAO.getInstance().listaCamposDisponiblesEnteros(codEntidad,params);
            m_Log.debug("lista de entidades obtenidos");

        } catch (Exception ce) {
            res = null;
            if ( m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return res;
    }

}