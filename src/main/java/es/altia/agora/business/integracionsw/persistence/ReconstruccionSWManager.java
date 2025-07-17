package es.altia.agora.business.integracionsw.persistence;

import es.altia.agora.business.integracionsw.OperacionServicioWebVO;
import es.altia.agora.business.integracionsw.TipoServicioWebVO;
import es.altia.agora.business.integracionsw.persistence.manual.ReconstruccionSWDAO;
import es.altia.common.exception.TechnicalException;
import es.altia.util.exceptions.InternalErrorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

public class ReconstruccionSWManager {

    private static ReconstruccionSWManager instance = null;

    protected static Log m_Log = LogFactory.getLog(ReconstruccionSWManager.class.getName());

    protected ReconstruccionSWManager() {
    }

    public static ReconstruccionSWManager getInstance() {
        if (instance == null) {
            synchronized(ReconstruccionSWManager.class) {
                if (instance == null) instance = new ReconstruccionSWManager();
            }
        }
        return instance;
    }

    public OperacionServicioWebVO reconstruirOperacionVO(int codOpDef, String[] paramsBD)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ReconstruccionSWManager: Comienzo reconstruirOperacionVO");
        try {
            return ReconstruccionSWDAO.getInstance().reconstruirOperacionVO(codOpDef, paramsBD);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("ReconstruccionSWManager: Fin reconstruirOperacionVO");

        }
    }

    public Collection<TipoServicioWebVO> getTiposSerializables(int codServicioWeb, String[] paramsBD)
    throws InternalErrorException {
        if (m_Log.isDebugEnabled()) m_Log.debug("ReconstruccionSWManager: Comienzo getTiposSerializables");
        try {
            return ReconstruccionSWDAO.getInstance().getTiposSerializables(codServicioWeb, paramsBD);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("ReconstruccionSWManager: Fin getTiposSerializables");

        }
    }

}
