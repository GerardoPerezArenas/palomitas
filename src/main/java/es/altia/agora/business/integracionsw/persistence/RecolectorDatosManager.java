package es.altia.agora.business.integracionsw.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.agora.business.integracionsw.persistence.manual.RecolectorDatosDAO;
import es.altia.common.exception.TechnicalException;

public class RecolectorDatosManager {

    private static RecolectorDatosManager instance = null;

    protected static Log m_Log = LogFactory.getLog(RecolectorDatosManager.class.getName());

    protected RecolectorDatosManager() {
    }

    public static RecolectorDatosManager getInstance() {
        if (instance == null) {
            synchronized (RecolectorDatosManager.class) {
                if (instance == null) instance = new RecolectorDatosManager();
            }
        }
        return instance;
    }

    public String getDatoVistaCRO(int ocurrencia, String numExpediente, int codTramite, String codigoCampo, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoVistaCRO");
        try {
            return RecolectorDatosDAO.getInstance().getDatoVistaCRO(ocurrencia, numExpediente, codTramite, codigoCampo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoVistaCRO");

        }
    }

    public String getDatoVistaINT(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoVistaINT");
        try {
            return RecolectorDatosDAO.getInstance().getDatoVistaINT(codMunicipio, numExpediente, codigoCampo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoVistaINT");

        }
    }

    public String getDatoTablaTNU(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTNU");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTNU(codMunicipio, numExpediente, codigoCampo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTNU");

        }
    }

    public String getDatoTablaTNUT(int codMunicipio, String numExpediente, String codigoCampo,int ocurrencia, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTNUT");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTNUT(codMunicipio, numExpediente, codigoCampo, ocurrencia, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTNUT");

        }
    }

    public String getDatoTablaTXT(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTXT");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTXT(codMunicipio, numExpediente, codigoCampo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTXT");

        }
    }

    public String getDatoTablaTXTT(int codMunicipio, String numExpediente, String codigoCampo, int ocurrencia,String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTXTT");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTXTT(codMunicipio, numExpediente, codigoCampo, ocurrencia, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTXTT");

        }
    }

    public String getDatoTablaTFE(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTFE");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTFE(codMunicipio, numExpediente, codigoCampo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTFE");

        }
    }

    public String getDatoTablaTFET(int codMunicipio, String numExpediente, String codigoCampo, int ocurrencia,String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTFET");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTFET(codMunicipio, numExpediente, codigoCampo, ocurrencia, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTFET");

        }
    }

    public String getDatoTablaTTL(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTTL");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTTL(codMunicipio, numExpediente, codigoCampo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTTL");

        }
    }

    public String getDatoTablaTTLT(int codMunicipio, String numExpediente, String codigoCampo, int ocurrencia,String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTTLT");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTTLT(codMunicipio, numExpediente, codigoCampo, ocurrencia, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTTLT");

        }
    }

    public String getDatoTablaTDE(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTDE");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTDE(codMunicipio, numExpediente, codigoCampo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTDE");

        }
    }

    public String getDatoTablaTDET(int codMunicipio, String numExpediente, String codigoCampo,int ocurrencia, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Comienzo getDatoTablaTDET");
        try {
            return RecolectorDatosDAO.getInstance().getDatoTablaTDET(codMunicipio, numExpediente, codigoCampo, ocurrencia, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("RecolectorDatosManager: Fin getDatoTablaTDET");

        }
    }
}
