package es.altia.agora.business.administracion.mantenimiento.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.ParametrosDAO;
import java.util.HashMap;

public class ParametrosManager {

  private static ParametrosManager instance = null;
    
  public static ParametrosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      synchronized(ParametrosManager.class) {
        if (instance == null) {
          instance = new ParametrosManager();
        }
      }
    }
    return instance;
  }
    
    /**
     * Obtiene todos los parametros propios de la organizacion.
     * @param params parametros de conexion a BD
     * @return hashmap con todos los parametros
     * @throws java.lang.Exception
     */
    public HashMap<String,String> obtenerParametros(String[] params)
            throws Exception {

        ParametrosDAO parametrosDAO = ParametrosDAO.getInstance();
        HashMap<String,String> resultado = new HashMap<String,String>();
        try{
          resultado = parametrosDAO.obtenerParametros(params);
        }catch(Exception ce){
          throw ce;
        }
        
        return resultado;
    }
    
    /**
     * Obtiene el valor del parametro cuyo codigo se indica, lanza una excepcion
     * si no se encuentra el parametro buscado.
     * @param codigo codigo del parametro cuyo valor se quiere obtener
     * @param params parametros de conexion a BD
     * @return valor del parametro indicado
     * @throws java.lang.Exception
     */
    public String obtenerParametro(String codigo, String[] params)
            throws Exception {
        
        ParametrosDAO parametrosDAO = ParametrosDAO.getInstance();
        String resultado = null;
        try{
          resultado = parametrosDAO.obtenerParametro(codigo, params);
        }catch(Exception ce){
          throw ce;
        }

        return resultado;
    }
    
    /** 
     * Actualiza los valores de los parametros que se incluyan en el HashMap.
     * @param parametros codigos y valores de todos los parametros
     * @param params parametros de conexion a BD
     * @throws java.lang.Exception
     */
    public void grabarParametros(HashMap<String,String> parametros, String[] params)
            throws Exception {
        
        ParametrosDAO parametrosDAO = ParametrosDAO.getInstance();
        try{
          parametrosDAO.grabarParametros(parametros, params);
        }catch(Exception ce){
          throw ce;
        }
    }
}
