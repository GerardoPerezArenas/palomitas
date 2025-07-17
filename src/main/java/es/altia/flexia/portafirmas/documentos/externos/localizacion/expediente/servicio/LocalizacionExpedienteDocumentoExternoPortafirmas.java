
package es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.servicio;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.servicio.exception.LocalizacionExpedienteDocumentoExternoPortafirmasException;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.vo.DocumentoExternoPortafirmasVO;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.vo.SalidaServicioLocalizacionExpedientePortafirmasVO;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Clase utilizada para localizar el expediente al que pertenece un documento externo enviado al portafirmas a través
 * del servicio web de envío de documentos externos al portafirmas 
 * @author oscar
 */
public class LocalizacionExpedienteDocumentoExternoPortafirmas {
 
    private static LocalizacionExpedienteDocumentoExternoPortafirmas instance=null;
    private Logger log = Logger.getLogger(LocalizacionExpedienteDocumentoExternoPortafirmas.class);
    
    private LocalizacionExpedienteDocumentoExternoPortafirmas(){}
    
    public static LocalizacionExpedienteDocumentoExternoPortafirmas getInstance(){
        if(instance==null)
            instance = new LocalizacionExpedienteDocumentoExternoPortafirmas();
        
        return instance;
    }
    
    
    public SalidaServicioLocalizacionExpedientePortafirmasVO getExpediente(DocumentoExternoPortafirmasVO doc) throws LocalizacionExpedienteDocumentoExternoPortafirmasException{
        
        SalidaServicioLocalizacionExpedientePortafirmasVO salida = null;
        
        try{
            ResourceBundle CONFIG = ResourceBundle.getBundle("Portafirmas"); 
            String serviciosDisponibles = CONFIG.getString(doc.getCodOrganizacion() + "/SERVICIOS_LOCALIZACION_EXPEDIENTE_DOCS_EXTERNO");
            String[] servicios = serviciosDisponibles.split(ConstantesDatos.DOT_COMMA);
        
            if(servicios==null){
                salida.setStatus(1);
                salida.setDescStatus("NO ESTÁ DISPONIBLE NINGÚN SERVICIO DE LOCALIZACION DE EXPEDIENTE PARA DOCUMENTOS EXTERNOS ENVIADOS AL PORTAFIRMAS");
                salida.setNumExpediente(null);
                salida.setCodProcedimiento(null);                
            }else{
                
                for(int i=0;i<servicios.length;i++){
                    String servicio = servicios[i];
                    if(servicio!=null && !"".equals(servicio)){
                        
                        try{
                            String implClass = CONFIG.getString(doc.getCodOrganizacion() + "/SERVICIO_LOCALIZACION_EXPEDIENTE_DOC_EXTERNO/" + servicio + "/IMPLCLASS");
                            if(implClass!=null){

                                Class clase = Class.forName(implClass);                        
                                ServicioLocalizacionExpedientePortafirmas implClassServicio = (ServicioLocalizacionExpedientePortafirmas)clase.newInstance();
                                if(implClassServicio!=null){                            
                                    salida = implClassServicio.getExpediente(doc);
                                    if(salida!=null && salida.getStatus()==0 && salida.getNumExpediente()!=null){
                                        break;                                
                                    }                            
                                }                        
                            }   
                        }catch(Exception e){
                            log.error("Error a instanciar la clase del servicio " + servicio + ". No existe el servicio de localización de expediente de documento externo: " + e.getMessage());
                            
                        }
                    }
                }//for
            }// else
            
        }catch(Exception e){
            e.printStackTrace();
            throw new LocalizacionExpedienteDocumentoExternoPortafirmasException(e.getMessage(),e);
        }
        return salida;
    }
    
}
