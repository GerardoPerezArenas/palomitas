package es.altia.agora.interfaces.user.web.gestionInformes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.CriticalException;
import es.altia.agora.interfaces.user.web.gestionInformes.exception.InstanciacionDatosInformesException;

public class FactoriaDatosInformes {
    protected static Log log = LogFactory.getLog(FactoriaDatosInformes.class.getName());

    private FactoriaDatosInformes() {}

    private static Class getImplClass(String origen) throws InstanciacionDatosInformesException {

        Class theClass;

        try {

            String implClassName = "es.altia.agora.business.gestionInformes.persistence.manual.DatosInformes" + origen;
            theClass = Class.forName(implClassName);

        } catch (ClassNotFoundException cnfe) {
            log.error("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR");
            cnfe.printStackTrace();
            throw new InstanciacionDatosInformesException("NO SE HA ENCONTRADO LA CLASE QUE SE QUIERE INSTANCIAR ", cnfe);
        }

        return theClass;

    }

    public static FachadaDatosInformes getImpl(String origen) throws InstanciacionDatosInformesException {
        try {

            FachadaDatosInformes implClass =
                    (FachadaDatosInformes) getImplClass(origen).newInstance();
            implClass.setOrigen(origen);
            return implClass;

        } catch (InstantiationException ie) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE DEL SERVICIO DE BUSQUEDA YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO");
            ie.printStackTrace();
            throw new InstanciacionDatosInformesException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE " +
                    "YA QUE EL CONSTRUCTOR POR DEFECTO NO ES VALIDO", ie);
        } catch (IllegalAccessException iae) {
            log.error("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE");
            iae.printStackTrace();
            throw new InstanciacionDatosInformesException("NO SE HA PODIDO DEVOLVER UNA INSTANCIA DE LA CLASE " +
                    "YA QUE EL CONSTRUCTOR POR DEFECTO NO ES ACCESIBLE", iae);
        }
    }

}
