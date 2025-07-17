package es.altia.agora.business.registro.mantenimiento.persistence;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.mantenimiento.HojaArbolClasifAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantClasifAsuntosDAO;
import es.altia.agora.interfaces.user.web.registro.mantenimiento.exceptions.EliminarClasificacionAsuntoException;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MantClasifAsuntosManager {

    // La instancia unica
    private static MantClasifAsuntosManager instance = null;
    // Log para mensajes
    protected static Log m_Log =
        LogFactory.getLog(MantClasifAsuntosManager.class.getName());

    protected MantClasifAsuntosManager() {
    }
    
    /**
     * Factory method para el <code>Singleton</code>.
     * @return La unica instancia de MantAsuntosManager
     */
   public static MantClasifAsuntosManager getInstance() {
     //Si no hay una instancia de esta clase tenemos que crear una.
         if (instance == null) {       
             synchronized(MantClasifAsuntosManager.class) {
         if (instance == null)
           instance = new MantClasifAsuntosManager();
             }
         }
         return instance;
   }
   
   /**
    * Carga todas las clasificaciones de  asunto 
    * @return Un Array de MantClasifAsuntosValueObject 
    * 
    */
   public ArrayList<MantClasifAsuntosValueObject> cargarClasifAsuntos(Integer unidadOrganica,String[] params) {
       
       if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->cargarClasifAsuntos.BEGIN");
       ArrayList<MantClasifAsuntosValueObject> clasifAsuntos = new ArrayList<MantClasifAsuntosValueObject>();

       try{
           
           clasifAsuntos = MantClasifAsuntosDAO.getInstance().cargarClasifAsuntos(unidadOrganica,params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->cargarClasifAsuntos.END");
       return clasifAsuntos;       
   }
   
   
    /**
    * Graba una nueva clasificacion de asunto en la BD.  
    */
   public void grabarAlta(MantClasifAsuntosValueObject asunto, String[] params) {              
     
       if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->grabarAlta.BEGIN"+ asunto.toString());
       try{
           MantClasifAsuntosDAO.getInstance().grabarAlta(asunto, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       } 
       if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->grabarAlta.END");
   }
   
   
    /**
    * Modifica los datos de una clasificacion de  asunto en la BD.  
    */
   public void grabarModificacion(String descripcion, Integer codigo, Integer unidadRegistro, String[] params) {              
      if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->grabarModificacion.BEGIN");
      if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->grabarModificacion.Descripcion"+descripcion);
      if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->grabarModificacion.Codigo"+codigo);
        if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->grabarModificacion.UnidadRegistro"+unidadRegistro);
      try{
           MantClasifAsuntosDAO.getInstance().grabarModificacion(descripcion, codigo, unidadRegistro, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }             
     if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->grabarModificacion.END");
   }
   

    /**
    * Recupera de la Bd una clasificacion de asunto, qe tiene como codigo, el pasado
    * como parametro
    * @return Un MantClasifAsuntosValueObject que contendrá toda la información del asunto. 
    */
   public MantClasifAsuntosValueObject cargarClasifAsunto(Integer codigo,Integer unidadRegistro, String[] params) {  
      if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->cargarClasifAsunto.BEGIN");
      if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->cargarClasifAsunto, codigo pasado: "+codigo);
     if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->cargarClasifAsunto, unidadRegistro pasado: "+unidadRegistro);
      
      MantClasifAsuntosValueObject clasifAsunto= new MantClasifAsuntosValueObject();
       try{
           clasifAsunto = MantClasifAsuntosDAO.getInstance().cargarClasifAsunto(codigo,unidadRegistro, params);
       
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
       if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->cargarClasifAsunto.END. Resultado"+clasifAsunto.toString());
      
       return clasifAsunto;       
   }
   
  
   /**
     *  Elimina un clasificación de asunto la BD.
     *
     * @param clasifAsunto El MantClasifAsuntosValueObject que representa el asunto.
     * @param params Parámetros de conexión a BD (para inicializar el adaptador).
    */
   public void eliminarClasifAsunto(int codigo,int unidadRegistro, String[] params) throws EliminarClasificacionAsuntoException {
      
       if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->eliminarClasifAsunto.BEGIN");
       if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->eliminarClasifAsunto,para codigo: "+ codigo + "y unidad de registro: "+ unidadRegistro);
       
      
       
       //Aqui tendremos que hacer la comprobacion de si existen tipos de asuntos, que tengan
       //la clasificacion que nos queremos cargar, si es así devolvemos exception
       
          
           if( MantClasifAsuntosDAO.getInstance().consultarExisteAsuntoConClasificacion(codigo, unidadRegistro, params)){
                if(m_Log.isDebugEnabled()) m_Log.debug("Lanzamos la excepcion!!!!!!!!!!!!!!!!!!");
                  throw new EliminarClasificacionAsuntoException();
           }else{
              if(m_Log.isDebugEnabled()) m_Log.debug("No hay asuntos de esta clasificacion, asi que borramos la clasificacion de la BD");
              MantClasifAsuntosDAO.getInstance().borrarClasifAsunto(codigo,unidadRegistro, params);
           
           }
 
  if(m_Log.isDebugEnabled()) m_Log.debug("MantClasifAsuntosManager->eliminarClasifAsunto.END");
}//eliminarClasifAsunto

   
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public ArrayList<HojaArbolClasifAsuntosValueObject> getArbolClasifAsuntos(RegistroValueObject registro,boolean todosAsuntos,String[] params){
        
       m_Log.debug("getArbol......BEGIN");    
       m_Log.debug("getArbol......Registro es: "+registro); 
       m_Log.debug("getArbol......Unidad de registro es: "+registro.getUnidadOrgan()); 
       m_Log.debug("getArbol......El pasar todos los asuntos es: "+todosAsuntos); 
       ArrayList<HojaArbolClasifAsuntosValueObject> arbolResultado = new ArrayList<HojaArbolClasifAsuntosValueObject>();
  
      //Primeramente obtenemos todas las clasificaciones existente
      ArrayList<MantClasifAsuntosValueObject> clasificaciones= MantClasifAsuntosManager.getInstance().cargarClasifAsuntos(registro.getUnidadOrgan(), params);
      
      m_Log.debug("getArbol......El tamaño de todas las clasificaciones e: "+clasificaciones.size());
      for(int i=0; i<clasificaciones.size(); i++){
           
         MantClasifAsuntosValueObject actual= clasificaciones.get(i); 
         HojaArbolClasifAsuntosValueObject hoja =new HojaArbolClasifAsuntosValueObject();
         m_Log.debug("getArbol....... La clasificacion a analizar es: "+ actual.getDescripcion());  
         hoja.setCodigo(actual.getCodigo());
         hoja.setDescripcion(actual.getDescripcion());
         
         Vector<MantAsuntosValueObject> hijos= new Vector<MantAsuntosValueObject>();
         int codigoClasificacion=actual.getCodigo();
         //Estamos cargando los asuntos en la primera pantalla, devolvemos todos, los que están de alta y los que no
         hijos=MantAsuntosManager.getInstance().buscarAsuntosClasificacion(codigoClasificacion,registro,todosAsuntos, params);
         hoja.setAsuntosHijos(hijos);
         
         m_Log.debug("getArbol.......El número de asuntos para la clasificación es: "+ hijos.size());  
         arbolResultado.add(hoja);
         
      }
      m_Log.debug("getArbol......END.Imprimimos el arbol: ");   
//      
//      for(int i=0; i<arbolResultado.size(); i++){
//          HojaArbolClasifAsuntosValueObject hoja=arbolResultado.get(i);
//          m_Log.debug("Clasificacion : " +hoja.getDescripcion());
//          m_Log.debug("Clasificacion.codigo: "+ hoja.getCodigo());
//          Vector<MantAsuntosValueObject> hijos=hoja.getAsuntosHijos();
//          for(int j=0; j<hijos.size(); j++){
//          MantAsuntosValueObject asunto=hijos.get(j);
//           m_Log.debug("Asunto: "+asunto.getDescripcion()); 
//          
//          }
//      
//      }
      
    
     m_Log.debug("getArbol......END. Tamaño del resultado devuelto: "+arbolResultado.size());    
     return arbolResultado;
  }


/**
 * 
 * @param codAsunto: Codigo del asunto, del que queremos saber a que clasificacion pertenece
 * @param arbolClasifAsunto: Arbol en el que buscamos el código
 * @return Un integer que se corresponde con el nodo del arbol de la clasificación a la que pertenece el codAsunto
 * o -1, si no pertenece a ninguna
 */
 
 
public int dameHijoParaDesplegar(ArrayList<HojaArbolClasifAsuntosValueObject> arbolClasifAsunto, String codAsunto){
        
      m_Log.debug("DameHijoParaDesplegar......BEGIN");    
      int resultado=-1;
    
      String codigoAAnalizar;
      for(int i=0; i<arbolClasifAsunto.size(); i++){
          HojaArbolClasifAsuntosValueObject hoja=arbolClasifAsunto.get(i);
          //m_Log.debug("Codigo de la hoja (Codigo de clasificacion): "+ hoja.getCodigo());
          Vector<MantAsuntosValueObject>  asuntosHijos=hoja.getAsuntosHijos();
          for(MantAsuntosValueObject asunto:asuntosHijos){
            codigoAAnalizar=asunto.getCodigo();
            //m_Log.debug("El codigo a analizar es:"+codigoAAnalizar ); 
            if(codAsunto.equals(codigoAAnalizar)){
               // m_Log.debug("El codigo de la hoja es igual al codigo a analizar y este es:"+codigoAAnalizar );   
                resultado=i;
                break;
            } 
          }
       }
    
     //m_Log.debug("Dame Hijo para Desplegar. END. El resultado devuelto es: "+resultado);    
     return resultado;
  }




}