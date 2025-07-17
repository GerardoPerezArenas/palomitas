package es.altia.agora.business.sge.plugin.documentos;
import es.altia.agora.business.sge.plugin.documentos.dao.RepositorioPluginAlmacenamientoDocumentosManager;
import java.io.Serializable;
import java.util.Hashtable;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class AlmacenDocumentoTramitacionFactoria implements Serializable,RepositorioDocumentacionListener{

    private static AlmacenDocumentoTramitacionFactoria instance = null;    
    private static Logger log = Logger.getLogger(AlmacenDocumentoTramitacionFactoria.class);
    
    // Caché por organización
    private static Hashtable<String,Hashtable<String,AlmacenDocumento>> cacheProcedimientos = new Hashtable<String,Hashtable<String,AlmacenDocumento>>();
    private static Hashtable<String,AlmacenDocumento> cacheRegistro = new Hashtable<String,AlmacenDocumento>();
    private static Hashtable<String,AlmacenDocumento> cacheDocumentosEnviadosPortafirmas = new Hashtable<String,AlmacenDocumento>();
    

    private AlmacenDocumentoTramitacionFactoria(){
    }
       
    public static AlmacenDocumentoTramitacionFactoria getInstance(String codOrganizacion){
        RepositorioPluginAlmacenamientoDocumentosManager manager = new RepositorioPluginAlmacenamientoDocumentosManager();
        if(instance==null){
          instance = new AlmacenDocumentoTramitacionFactoria();
        }
        
        // Si en la caché de plugin de almacenamiento por procedimiento, no existe la lista de 
        // plugins para la organización, hay que recuperarla
        if(!cacheProcedimientos.contains(codOrganizacion)){             
           //pluginProcedimientos = manager.getPluginAlmacenamientoDocumentoProcedimiento(codOrganizacion);                                   
           Hashtable<String,AlmacenDocumento> pluginProcedimientos = manager.getPluginAlmacenamientoDocumentoProcedimiento(codOrganizacion);
           if(pluginProcedimientos!=null)
                cacheProcedimientos.put(codOrganizacion,pluginProcedimientos);
        }

        if(!cacheRegistro.contains(codOrganizacion)){                  
            AlmacenDocumento almacenDocumento = manager.getPluginAlmacenamientoRegistro(codOrganizacion);
            if(almacenDocumento!=null)            
                cacheRegistro.put(codOrganizacion,almacenDocumento);
        }
        
        if(!cacheDocumentosEnviadosPortafirmas.contains(codOrganizacion)){
            AlmacenDocumento almacenDocumento = manager.getPluginAlmacenamientoDocumentosExternosPortafirmas(codOrganizacion);
            if(almacenDocumento!=null)
                cacheDocumentosEnviadosPortafirmas.put(codOrganizacion,almacenDocumento);
        }        
        return instance;
    }
       
    
    /**
     * Devuelve el plugin de almacenamiento a ejecutar para un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @return Plugin que implementa la interfaz AlmacenDocumento
     */
     public AlmacenDocumento getImplClassPluginProcedimiento(String codOrganizacion,String codProcedimiento){
         AlmacenDocumento almacen  =null;
         
         try{
            if(cacheProcedimientos!=null){
              Hashtable<String,AlmacenDocumento> lista = cacheProcedimientos.get(codOrganizacion);                
              almacen = (AlmacenDocumento)lista.get(codProcedimiento);            
            } 
            
            log.debug(" ==================> "  + this.getClass().getName() + ".getImplClassPluginProcedimiento() ======>");
            
            // Si el procedimiento no tiene asociado un plugin, entonces se devuelve el genérico, que es de 
            // almacenamiento de documentos en base de datos
            if(almacen==null){                  
                Class implClass = Class.forName("es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoBBDDImpl");
                almacen = (AlmacenDocumento)implClass.newInstance();
                log.debug(" ==================> "  + this.getClass().getName() + ".getImplClassPluginProcedimiento() almacen es nulo, se obtiene la implementacion para BBDD: " + almacen.getClass().getName());
            }else
                log.debug(" ==================> "  + this.getClass().getName() + ".getImplClassPluginProcedimiento() almacen de tipo: " + almacen.getClass().getName());
         }catch(Exception e){
             e.printStackTrace();
             log.error("Error al obtener la implementación del plugin de almacenamiento de documentos de expediente: " + e.getMessage());
         }         
         return almacen;
     }
    
     
   /**
     * Devuelve el plugin de almacenamiento a ejecutar para almacenar los documentos de registro     
     * @param codOrganizacion: Código de la organización
     * @return Plugin que implementa la interfaz AlmacenDocumento
     */
     public AlmacenDocumento getImplClassRegistro(String codOrganizacion){
         AlmacenDocumento almacen  =null;
         
         try{
            if(cacheRegistro!=null){
                almacen = cacheRegistro.get(codOrganizacion);
            } 
            
            log.debug(" ==================> "  + this.getClass().getName() + ".getImplClassRegistro() ======>");
            // Si el procedimiento no tiene asociado un plugin, entonces se devuelve el genérico, que es de 
            // almacenamiento de documentos en base de datos
            if(almacen==null){
                Class implClass = Class.forName("es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoBBDDImpl");
                almacen = (AlmacenDocumento)implClass.newInstance();
                 log.debug(" ==================> "  + this.getClass().getName() + ".getImplClassRegistro() almacen es nulo, se obtiene la implementacion para BBDD: " + almacen.getClass().getName());
            } else
                log.debug(" ==================> "  + this.getClass().getName() + ".getImplClassRegistro() almacen de tipo: " + almacen.getClass().getName());           
            
         }catch(Exception e){
             e.printStackTrace();
             log.error("Error al obtener la implementación del plugin de almacenamiento de registro: " + e.getMessage());
         }
         
         return almacen;
     }
       
     
     
   /**
     * Devuelve el plugin de almacenamiento a ejecutar para almacenar los documentos de registro     
     * @return Plugin que implementa la interfaz AlmacenDocumento
     */
     public AlmacenDocumento getImplClassDocumentoExternoPortafirmas(String codOrganizacion){
         AlmacenDocumento almacen  =null;
         
         try{
            if(cacheDocumentosEnviadosPortafirmas!=null){
                almacen = cacheDocumentosEnviadosPortafirmas.get(codOrganizacion);
            } 
            
            // Si el procedimiento no tiene asociado un plugin, entonces se devuelve el genérico, que es de 
            // almacenamiento de documentos en base de datos
            if(almacen==null){
                Class implClass = Class.forName("es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoBBDDImpl");
                almacen = (AlmacenDocumento)implClass.newInstance();
            }            
            
         }catch(Exception e){
             e.printStackTrace();
             log.error("Error al obtener la implementación del plugin de almacenamiento de documentos externos enviados al portafirmas de Flexia: " + e.getMessage());
         }         
         return almacen;
     }
       
      
    public void recargarPluginProcedimiento(String codOrganizacion,Hashtable<String,AlmacenDocumento> pluginProcedimientos){        
        this.cacheProcedimientos.put(codOrganizacion,pluginProcedimientos);
    }
    
    public void recargarPluginRegistro(String codOrganizacion,AlmacenDocumento almacen){
        this.cacheRegistro.put(codOrganizacion,almacen);
    }       
    
}