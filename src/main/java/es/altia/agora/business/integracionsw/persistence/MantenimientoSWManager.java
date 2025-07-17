package es.altia.agora.business.integracionsw.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.TechnicalException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.agora.business.integracionsw.persistence.manual.MantenimientoSWDAO;
import es.altia.agora.business.integracionsw.persistence.manual.DefinicionOperacionesSWDAO;
import es.altia.agora.business.integracionsw.InfoServicioWebVO;

import java.util.Collection;

public class MantenimientoSWManager {

    private static MantenimientoSWManager instance = null;

    protected static Log m_Log = LogFactory.getLog(MantenimientoSWManager.class.getName());

    protected MantenimientoSWManager() {}

    public static MantenimientoSWManager getInstance() {
        if (instance == null) {
            synchronized(MantenimientoSWManager.class) {
                if (instance == null) instance = new MantenimientoSWManager();
            }
        }
        return instance;
    }

    public boolean existeServicioWebPorTitulo(String tituloSW, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo existeServicioWebPorTitulo");
        try {
            return MantenimientoSWDAO.getInstance().existeServicioWebPorTitulo(tituloSW, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin existeServicioWebPorTitulo");

        }
    }

    public InfoServicioWebVO altaServicioWeb(InfoServicioWebVO infoSWVO, String[] params) throws InternalErrorException{

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo altaServicioWeb");
        try {
            return MantenimientoSWDAO.getInstance().altaServicioWeb(infoSWVO, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin altaServicioWeb");
        }
    }

    public void cambiarEstadoPublicacionSW(int codigoSW, boolean nuevoEstado, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo cambiarEstadoPublicacionSW");
        try {
            MantenimientoSWDAO.getInstance().cambiarEstadoPublicacionSW(codigoSW, nuevoEstado, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin cambiarEstadoPublicacionSW");                   
        }
    }

    public Collection getListaServiciosWeb(String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo getListaServiciosWeb");
        try {
            return MantenimientoSWDAO.getInstance().getListaServiciosWeb(params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin getListaServiciosWeb");
        }
    }

    public InfoServicioWebVO getInfoGeneralSWByCodigo(int codigoSW, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo getInfoGeneralSWByCodigo");
        try {
            return MantenimientoSWDAO.getInstance().getInfoGeneralSWByCodigo(codigoSW, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin getInfoGeneralSWByCodigo");
        }
    }

    public Collection getOperacionesByServicioWeb(int codigoSW, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo getOperacionesByServicioWeb");
        try {
            return MantenimientoSWDAO.getInstance().getOperacionesByServicioWeb(codigoSW, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin getOperacionesByServicioWeb");
        }
    }

    public boolean puedePublicarseServicioWeb(int codigoSW, String[] params) throws TechnicalException, InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo puedePublicarseServicioWeb");
        try {
            return MantenimientoSWDAO.getInstance().puedePublicarseServicioWeb(codigoSW, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin puedePublicarseServicioWeb");
        }
    }

    public void eliminarServicioWeb(int codigoSW, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Comienzo eliminarServicioWeb");
        try {
            Collection ops = DefinicionOperacionesSWManager.getInstance().getOpsDefinidasBySW(codigoSW, params);
            MantenimientoSWDAO.getInstance().eliminarServicioWeb(codigoSW, ops, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("MantenimientoSWManager: Fin eliminarServicioWeb");
        }
    }
}
