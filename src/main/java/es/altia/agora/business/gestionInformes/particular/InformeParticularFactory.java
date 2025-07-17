package es.altia.agora.business.gestionInformes.particular;

import es.altia.common.exception.TechnicalException;

import java.util.ResourceBundle;

public final class InformeParticularFactory {

    private final static String IMPL_CLASS_NAME_PREFIX = "InformeParticular/";
    private final static String IMPL_CLASS_NAME_SUFFIX = "/implClassName";

    private InformeParticularFactory() {}

    private static Class getImplClass(String tipoInforme) throws TechnicalException {

        ResourceBundle confFile = ResourceBundle.getBundle("es.altia.agora.business.gestionInformes.particular.InformeParticular");

        try {
            String daoClassName = confFile.getString(IMPL_CLASS_NAME_PREFIX + tipoInforme + IMPL_CLASS_NAME_SUFFIX);
            return Class.forName(daoClassName);
        } catch (ClassNotFoundException cnfe) {
            throw new TechnicalException(cnfe.getMessage(), cnfe.getException());
        }

    }

    public static InformeParticularFacade getImpl(String tipoInforme, String[] params) throws TechnicalException {

        try {
            InformeParticularFacade implFacade = (InformeParticularFacade) getImplClass(tipoInforme).newInstance();
            implFacade.setParamsBD(params);
            return implFacade;
        } catch (InstantiationException ie) {
            throw new TechnicalException(ie.getMessage(), ie.getCause());
        } catch (IllegalAccessException iae) {
            throw new TechnicalException(iae.getMessage(), iae.getCause());
        }
    }
}
