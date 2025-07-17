package es.altia.flexia.registro.digitalizacion.lanbide.util;

import es.lanbide.lan6.adaptadoresPlatea.utilidades.config.Lan6Config;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

public class GestionAdaptadoresLan6Config {

    private static Logger log = Logger.getLogger(GestionAdaptadoresLan6Config.class);
    private static final Lan6Config lan6Config = new Lan6Config();

    /**
     * Obtiene el valor de una entrada de configuracion de adaptadotes, previamente almacenada en el fichero de configuraicon AdaptadoresPlateaLan6.properties
     * @param nombreEntradaProperty
     * @return valor de la entrada
     */
    public String getElementoConfigFicheroAdaptadoresProperties(String nombreEntradaProperty){
        String respuesta = "";
        log.info("getElementoConfigFicheroAdaptadoresProperties => " + nombreEntradaProperty);
        try {
            if(nombreEntradaProperty != null && !nombreEntradaProperty.isEmpty()){
                respuesta =lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, nombreEntradaProperty);
            }
        } catch (Exception e) {
            log.error("Error al recuperar una entrada de configuracion de adaptadores. " + e.getMessage() + " => "+ ExceptionUtils.getRootCause(e),e);
        }
        log.info("getElementoConfigFicheroAdaptadoresProperties - respuesta =>  " + respuesta);
        return respuesta;
    }

    /**
     * Obtiene el codigo de procedimiento de platea para un procedimiento de Flexia
     * @param codigoProcedimientoFlexia
     * @return Codigo procedimiento en platea para procedimiento de regexlan
     */
    public String getCodProcedimientoPlatea(String codigoProcedimientoFlexia){
        String respuesta = "";
        log.info("getCodProcedimientoPlatea => " + codigoProcedimientoFlexia);
        try {
            if(codigoProcedimientoFlexia != null && !codigoProcedimientoFlexia.isEmpty()){
                respuesta =lan6Config.getProcComReg(codigoProcedimientoFlexia);
            }
        } catch (Exception e) {
            log.error("Error al recuperar una entrada de configuracion de adaptadores. " + e.getMessage() + " => "+ ExceptionUtils.getRootCause(e),e);
        }
        log.info("getCodProcedimientoPlatea - respuesta =>  " + respuesta);
        return respuesta;
    }

}
