// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.EtiquetasDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: EtiquetasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class EtiquetasManager
{
  private static EtiquetasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(EtiquetasManager.class.getName());

  protected EtiquetasManager()
  {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }//de la funcion

  public static EtiquetasManager getInstance()
  {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null)
    {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EtiquetasManager.class)
      {
        if (instance == null)
        {
          instance = new EtiquetasManager();
        }//del if
      }//del synchronized
    }//del if
    return instance;
  }//de la funcion

  public Vector getListaEtiquetas(String[] params)
  {
    EtiquetasDAO etiquetasDAO = EtiquetasDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      log.debug("Usando persistencia manual");
      resultado = etiquetasDAO.getListaEtiquetas(params);
      return resultado;
    }
    catch(Exception ce)
    {
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }//de la funcion

  public Vector eliminarEtiqueta(String codigo, String[] params)
  {
    EtiquetasDAO etiquetasDAO = EtiquetasDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      resultado = etiquetasDAO.eliminarEtiqueta(codigo,params);
    }
    catch (Exception e)
    {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }//de la funcion

  public Vector modificarEtiqueta(GeneralValueObject gVO, String[] params)
  {
    EtiquetasDAO etiquetasDAO = EtiquetasDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      resultado = etiquetasDAO.modificarEtiqueta(gVO, params);
    }
    catch(Exception e)
    {
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }//de la funcion

  public Vector altaEtiqueta(GeneralValueObject gVO, String[] params)
  {
    EtiquetasDAO etiquetasDAO = EtiquetasDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      resultado = etiquetasDAO.altaEtiqueta(gVO,params);
    }
    catch (Exception e)
    {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }//de la funcion
}//de la clase