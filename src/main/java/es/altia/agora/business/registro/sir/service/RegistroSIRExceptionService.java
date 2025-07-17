
package es.altia.agora.business.registro.sir.service;

import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6SIRExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6UtilExcepcion;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class RegistroSIRExceptionService {
    
    private static final Logger log  = Logger.getLogger(RegistroSIRExceptionService.class);
    
    public String getTextoMensajeErrorFromLan6SirException(Lan6SIRExcepcion ex){
        String respuesta = "Lan6SIRExcepcion";
        try {
            if(ex!=null)
                respuesta+=" " + getTextoCompletoErrorFormateado(ex.getMessage()
                        + " " + (getTextoCodigoMensajeErrorFromListasSincronizadas(ex.getCodes(),ex.getMessages()))
                        , ex.getSistema()
                        , ex.getMensajeExcepcion()
                        , ex.getLocalizedMessage()
                        , ex.getCausaExcepcion());
        } catch (Exception e) {
            log.error("Error al procesar getTextoMensajeErrorFromLan6SirException", e);
            respuesta+=". No se ha podido generar el mensaje de error" + e.getMessage();
        }
        return  respuesta;
    }
    
    public String getTextoMensajeErrorFromLan6UtilExcepcion(Lan6UtilExcepcion ex){
        String respuesta = "Lan6UtilExcepcion";
        try {
            if(ex!=null)
                respuesta+=" " + getTextoCompletoErrorFormateado(ex.getMessage()
                        + " " + (getTextoCodigoMensajeErrorFromListasSincronizadas(ex.getCodes(),ex.getMessages()))
                        , ex.getSistema()
                        , ex.getMensajeExcepcion()
                        , ex.getLocalizedMessage()
                        , ex.getCausaExcepcion());
        } catch (Exception e) {
            log.error("Error al procesar getTextoMensajeErrorFromLan6UtilExcepcion", e);
            respuesta+=". No se ha podido generar el mensaje de error" + e.getMessage();
        }
        return  respuesta;
    }
    
    public String getTextoMensajeErrorFromLan6Excepcion(Lan6Excepcion ex){
        String respuesta = "Lan6Excepcion";
        try {
            if(ex!=null)
                respuesta+=" " + getTextoCompletoErrorFormateado(ex.getMessage()
                        + " " + (getTextoCodigoMensajeErrorFromListasSincronizadas(ex.getCodes(),ex.getMessages()))
                        , ex.getSistema()
                        , ex.getMensajeExcepcion()
                        , ex.getLocalizedMessage()
                        , ex.getCausaExcepcion());
        } catch (Exception e) {
            log.error("Error al procesar getTextoMensajeErrorFromLan6Excepcion", e);
            respuesta+=". No se ha podido generar el mensaje de error" + e.getMessage();
        }
        return  respuesta;
    }
    
    private String  getTextoCompletoErrorFormateado(String Message,String Sistema,String MensajeExcepcion,String LocalizedMessage,String CausaExcepcion){
        String respuesta="";
        try {
            respuesta =
            ("Mensaje: " + Message).replaceAll("(\n|\r)", " - ").replaceAll("'|<|>|\"", "#")
                    + (". Sistema: " + Sistema).replaceAll("(\n|\r)", " - ").replaceAll("'|<|>|\"", "#")
                    + (". MensajeExcepcion: " + MensajeExcepcion).replaceAll("(\n|\r)", " - ").replaceAll("'|<|>|\"", "#")
                    + (". LocalizedMessage: " + LocalizedMessage).replaceAll("(\n|\r)", " - ").replaceAll("'|<|>|\"", "#")
                    + (". CausaExcepcion: " + CausaExcepcion).replaceAll("(\n|\r)", " - ").replaceAll("'|<|>|\"", "#")
                    ;
        } catch (Exception e) {
            log.error("Error al procesar getTextoCompletoErrorFormateado", e);
        }
        return respuesta;
    }
    
    private String getTextoCodigoMensajeErrorFromListasSincronizadas(ArrayList<String> listaCodigos, ArrayList<String> listaDescripciones){
        String respuesta = "";
        try {
            if(listaCodigos!=null && !listaCodigos.isEmpty()
                    && listaDescripciones!=null && !listaDescripciones.isEmpty()
                    && listaCodigos.size() == listaDescripciones.size()
                    ){
                for (int i = 0; i < listaCodigos.size(); i++) {
                    respuesta+= (!respuesta.isEmpty() ? " => " : "=> ") + listaCodigos.get(i) +"_"+ listaDescripciones.get(i);
                }
            }else{
                log.info("Una de las listas viene a null o no estan sincronizadas : " 
                        + "listaCodigos: " + (listaCodigos!=null ? "true" + listaCodigos.size() : " true - 0")
                        + "listaDescripciones: " + (listaDescripciones!=null ? "true" + listaDescripciones.size() : " false - 0")
                );
            }
        } catch (Exception e) {
            log.error("Error al porcesar la lista de Codigos de Error y decripciones de Lan6 " + e.getMessage() + " - " + e.getLocalizedMessage(), e);
        }
        return respuesta;
    }
}
