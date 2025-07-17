package es.altia.agora.webservice.registro;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.CriticalException;
import es.altia.agora.webservice.registro.exceptions.NoExisteSWException;
import es.altia.agora.webservice.registro.exceptions.InstanciacionRegistroException;
import es.altia.agora.webservice.tercero.exception.InstanciacionBusquedaTerceroException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AccesoRegistroFactoria {

    protected static Config confRegistro = ConfigServiceHelper.getConfig("Registro");
    protected static Log log = LogFactory.getLog(AccesoRegistroFactoria.class.getName());

    private AccesoRegistroFactoria() {}

    private static Class getImplClass(String idServicio, String prefijoPropiedad) throws InstanciacionRegistroException {

        Class theClass;

        try {

            String implClassName = confRegistro.getString(prefijoPropiedad + idServicio + "/implClassName");
            theClass = Class.forName(implClassName);

        } catch (ClassNotFoundException cnfe) {
            log.error("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION");
            cnfe.printStackTrace();
            throw new InstanciacionRegistroException("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR " +
                    "EN EL FICHERO DE CONFIGURACION", cnfe);
        } catch (CriticalException ce) {
            log.error("NO SE HA ENCONTRADO LA PROPIEDAD DE LA CLASE QUE SE QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION");
            ce.printStackTrace();
            throw new InstanciacionRegistroException("NO SE HA ENCONTRADO LA PROPIEDAD DE LA CLASE QUE SE " +
                    "QUIERE INSTANCIAR EN EL FICHERO DE CONFIGURACION", ce);
        }

        return theClass;

    }

    public static AccesoRegistro getImpl(String idServicio, String prefijoPropiedad) throws InstanciacionRegistroException {
        try {

            AccesoRegistro implClass =
                    (AccesoRegistro) getImplClass(idServicio, prefijoPropiedad).newInstance();
            implClass.setNombreServicio(idServicio);
            implClass.setPrefijoPropiedad(prefijoPropiedad + idServicio + "/");
            return implClass;

        } catch (InstantiationException ie) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO");
            ie.printStackTrace();
            throw new InstanciacionRegistroException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL " +
                    "SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO", ie);
        } catch (IllegalAccessException iae) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE");
            iae.printStackTrace();
            throw new InstanciacionRegistroException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL " +
                    "SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE", iae);
        }
    }

}
