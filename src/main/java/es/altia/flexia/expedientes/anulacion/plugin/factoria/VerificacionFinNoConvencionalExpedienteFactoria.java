package es.altia.flexia.expedientes.anulacion.plugin.factoria;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.expedientes.anulacion.exception.VerificacionFinNoConvencionalInstanceException;
import es.altia.flexia.expedientes.anulacion.plugin.VerificacionFinNoConvencionalExpediente;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Factoria de plugin de verificación de finalización no convencional/anulación de un expediente 
 */
public class VerificacionFinNoConvencionalExpedienteFactoria {   
    private final static String CONFIG_FILE = "finalizacionNoConvencional";
    private final static String SERVICIOS_DISPONBLES = "/FIN_NOCONVENCIONAL/DISPONIBLES";
    private final static String FIN_NOCONVENCIONAL   = "/FIN_NOCONVENCIONAL/";
    private final static String IMPLCLASS            = "/IMPLCLASS";
    private final static String DESCRIPCION          = "/DESCRIPCION";
    private final static String IMPLCLASS_DEFECTO    = "es.altia.flexia.expedientes.anulacion.plugin.VerificacionFinNoConvencionalExpedienteFlexia";
    
    private static Logger log = Logger.getLogger(VerificacionFinNoConvencionalExpedienteFactoria.class);
    
    /**
     * Recupera los servicios de finalización de expedientes de forma no convencional que se encuentran
     * habilitados para una determinada organización
     * @param codOrganizacion: Código de la organización
     * @return ArrayList<VerificacionFinNoConvencionalExpediente> con la lista de servicios disponibles
     */
    public static ArrayList<VerificacionFinNoConvencionalExpediente> getServiciosDisponibles(int codOrganizacion){
        ArrayList<VerificacionFinNoConvencionalExpediente> salida = new ArrayList<VerificacionFinNoConvencionalExpediente>();
        try{
            ResourceBundle config  = ResourceBundle.getBundle(CONFIG_FILE);
            String lista_servicios = config.getString(codOrganizacion + SERVICIOS_DISPONBLES);
            if(lista_servicios!=null && !"".equals(lista_servicios)){
                String[] servicios = lista_servicios.split(ConstantesDatos.DOT_COMMA);

                for(int i=0;servicios!=null && i<servicios.length;i++){

                    String servicio = servicios[i];
                    if(servicio!=null && !"".equals(servicio)){
                        String nameClass   = config.getString(codOrganizacion + FIN_NOCONVENCIONAL + servicio + IMPLCLASS);            
                        String descripcion = config.getString(codOrganizacion + FIN_NOCONVENCIONAL + servicio + DESCRIPCION);                                            
                             
                        if(nameClass!=null && !"".equals(nameClass) && descripcion!=null && !"".equals(descripcion)){
                            log.debug("Nombre clase: " + nameClass + " con descripción " + descripcion);
                            Class clase      = Class.forName(nameClass);
                            VerificacionFinNoConvencionalExpediente implClass = (VerificacionFinNoConvencionalExpediente)clase.newInstance();
                            if(implClass!=null){
                                implClass.setNombre(servicio);
                                implClass.setImplClass(nameClass);                        
                                implClass.setDescripcion(descripcion);                        
                                salida.add(implClass);                        
                            }
                        }
                    }
                }
            }
            
        }catch(Exception e){
            log.error("Error al recuperar la lista de servicios disponbles: " + e.getMessage());
        }        
        return salida;
    }
    
  /**
     * Devuelve la instancia del plugin utilizado por defecto por el sistema en el caso de que no exista
     * otro plugin definido para una instalación de Flexia.     
     * @return VerificacionFinNoConvencionalExpediente
     */
    public static VerificacionFinNoConvencionalExpediente getImplClassDefecto(){
        VerificacionFinNoConvencionalExpediente implClass = null;
        try{
            Class clase = Class.forName(IMPLCLASS_DEFECTO);
            implClass   = (VerificacionFinNoConvencionalExpediente)clase.newInstance();
                        
        }catch(Exception e){
            log.error("Error al recuperar la instancia de plugin de anulación de expedente por defecto: " + e.getMessage());
        }        
        return implClass;
    }
    
    
   /**
     * Recupera los servicios de finalización de expedientes de forma no convencional que se encuentran
     * habilitados para una determinada organización
     * @param codOrganizacion: Código de la organización
     * @param nombre: Nombre del servicio
     * @return ArrayList<VerificacionFinNoConvencionalExpediente> con la lista de servicios disponibles
     */
    public static VerificacionFinNoConvencionalExpediente getImplClass(int codOrganizacion,String nombre) throws VerificacionFinNoConvencionalInstanceException{
        VerificacionFinNoConvencionalExpediente implClass = null;
        
        try{
            ResourceBundle config  = ResourceBundle.getBundle(CONFIG_FILE);
            String nameClass = config.getString(codOrganizacion + FIN_NOCONVENCIONAL + nombre + IMPLCLASS);
            
            Class clase = Class.forName(nameClass);
            implClass   = (VerificacionFinNoConvencionalExpediente)clase.newInstance();
            if(implClass!=null) {
                // Se recupera la descripción breve del plugin
                String descripcion = config.getString(codOrganizacion + FIN_NOCONVENCIONAL + nombre + DESCRIPCION);

                implClass.setImplClass(nameClass);
                implClass.setDescripcion(descripcion);
                implClass.setNombre(nombre);
            }
            
        }catch(Exception e){
            log.error("Error al recuperar la instancia del servicio " + nombre + ": " + e.getMessage());
            throw new VerificacionFinNoConvencionalInstanceException("Error al recuperar la instancia del servicio " + nombre + ": " + e.getMessage());
        }                
        return implClass;
    }
}
