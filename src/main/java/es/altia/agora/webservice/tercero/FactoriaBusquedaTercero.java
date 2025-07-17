package es.altia.agora.webservice.tercero;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.CriticalException;
import es.altia.agora.webservice.tercero.exception.InstanciacionBusquedaTerceroException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FactoriaBusquedaTercero {

    protected static Config confServBusqueda =
            ConfigServiceHelper.getConfig("Terceros");
    protected static Log log = LogFactory.getLog(FactoriaBusquedaTercero.class.getName());

    private FactoriaBusquedaTercero() {}

    private static Class getImplClass(String idServicio, String prefijoPropiedad) throws InstanciacionBusquedaTerceroException {

        Class theClass;

        try {

            String implClassName = confServBusqueda.getString(prefijoPropiedad + idServicio + "/implClassName");
            theClass = Class.forName(implClassName);

        } catch (ClassNotFoundException cnfe) {
            log.error("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION");
            cnfe.printStackTrace();
            throw new InstanciacionBusquedaTerceroException("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR " +
                    "EN EL FICHERO DE CONFIGURACION", cnfe);
        } catch (CriticalException ce) {
            log.error("NO SE HA ENCONTRADO LA PROPIEDAD DE LA CLASE QUE SE QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION");
            ce.printStackTrace();
            throw new InstanciacionBusquedaTerceroException("NO SE HA ENCONTRADO LA PROPIEDAD DE LA CLASE QUE SE " +
                    "QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION", ce);
        }

        return theClass;

    }

    public static FachadaBusquedaTercero getImpl(String idServicio, String prefijoPropiedad) throws InstanciacionBusquedaTerceroException {
        try {

            FachadaBusquedaTercero implClass =
                    (FachadaBusquedaTercero) getImplClass(idServicio, prefijoPropiedad).newInstance();
            implClass.setNombreServicio(idServicio);
            implClass.setPrefijoPropiedad(prefijoPropiedad + idServicio + "/");
            return implClass;

        } catch (InstantiationException ie) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO");
            ie.printStackTrace();
            throw new InstanciacionBusquedaTerceroException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL " +
                    "SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO", ie);
        } catch (IllegalAccessException iae) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE");
            iae.printStackTrace();
            throw new InstanciacionBusquedaTerceroException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL " +
                    "SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE", iae);
        }
    }


}
