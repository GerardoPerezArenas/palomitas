// NOMBRE DEL PAQUETE
package es.altia.agora.business.editor.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.editor.persistence.manual.EditorDAOWord;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.common.util.Registro;


public class EditorManagerWord {
  private static EditorManagerWord instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(EditorManagerWord.class.getName());


  protected EditorManagerWord() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EditorManagerWord getInstance(){
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EditorManagerWord.class) {
        if (instance == null) {
          instance = new EditorManagerWord();
        }
      }
    }
    return instance;
  }

  public Vector obtenerDatosEtiquetas(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("obtenerDatosEtiquetas");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    Vector vectorEtiquetas = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      vectorEtiquetas = editorDAOWord.obtenerDatosEtiquetas(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return vectorEtiquetas;
  }

  public void modificarPlantilla(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modificarPlantilla");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      editorDAOWord.modificarPlantilla(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }

  public void modificarPlantillaCRD(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modificarPlantillaCRD");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      editorDAOWord.modificarPlantillaCRD(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }

  public void grabarPlantilla(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("grabarPlantilla");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      editorDAOWord.grabarPlantilla(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }

  public void grabarCRD(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("grabarCRD");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      editorDAOWord.grabarCRD(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }


  public Registro obtenerDatosPlantilla(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("obtenerDatosPlantillas");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    Registro registro = new Registro();
    try{
      m_Log.debug("Usando persistencia manual");
      registro = editorDAOWord.obtenerDatosPlantilla(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return registro;
  }


  public Registro obtenerPlantilla(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("obtenerPlantilla");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    Registro registro = new Registro();
    try{
      m_Log.debug("Usando persistencia manual");
      registro = editorDAOWord.obtenerPlantilla(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return registro;
  }

  public Registro obtenerPlantillaCRD(String[] params,Registro parametros){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("obtenerPlantillaCRD");
    EditorDAOWord editorDAOWord = EditorDAOWord.getInstance();
    Registro registro = new Registro();
    try{
      m_Log.debug("Usando persistencia manual");
      registro = editorDAOWord.obtenerPlantillaCRD(params,parametros);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return registro;
  }


}
