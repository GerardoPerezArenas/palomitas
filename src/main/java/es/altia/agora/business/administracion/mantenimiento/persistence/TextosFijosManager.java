// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.TextosFijosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: TextosFijosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class TextosFijosManager
{
  private static TextosFijosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TextosFijosManager.class.getName());

  protected TextosFijosManager()
  {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }//de la funcion

  public static TextosFijosManager getInstance()
  {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null)
    {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TextosFijosManager.class)
      {
        if (instance == null)
        {
          instance = new TextosFijosManager();
        }//del if
      }//del synchronized
    }//del if
    return instance;
  }//de la funcion

  public Vector getListaTextosFijos(GeneralValueObject gVO,String[] params)
  {
    log.debug("getListaTextosFijosValue");
    TextosFijosDAO textosFijosDAO = TextosFijosDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      log.debug("Usando persistencia manual");
      resultado = textosFijosDAO.getListaTextosFijos(gVO,params);
      return resultado;
    }
    catch(Exception ce)
    {
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }//del try catch
  }//de la funcion

  public Vector eliminarTextoFijo(GeneralValueObject gVO, String[] params)
  {
    log.debug("eliminarTextoFijo");
    TextosFijosDAO textosFijosDAO = TextosFijosDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      resultado = textosFijosDAO.eliminarTextoFijo(gVO,params);
    }
    catch (Exception e)
    {
      log.error("Excepción capturada: " + e.toString());
    }//del try catch
    return resultado;
  }//de la funcion

  public Vector modificarTextoFijo(GeneralValueObject gVO, String[] params)
  {
    log.debug("modificarTextoFijo");
    TextosFijosDAO textosFijosDAO = TextosFijosDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      resultado = textosFijosDAO.modificarTextoFijo(gVO, params);
    }
    catch(Exception e)
    {
      log.error("Excepcion capturada: " + e.toString());
    }//del try catch
    return resultado;
  }//de la funcion

  public Vector altaTextoFijo(GeneralValueObject gVO, String[] params)
  {
    log.debug("altaTextoFijo");
    TextosFijosDAO textosFijosDAO = TextosFijosDAO.getInstance();
    Vector resultado = new Vector();
    try
    {
      resultado = textosFijosDAO.altaTextoFijo(gVO,params);
    }
    catch (Exception e)
    {
      log.error("Excepción capturada: " + e.toString());
    }//del try catch
    return resultado;
  }//de la funcion
}//de la clase