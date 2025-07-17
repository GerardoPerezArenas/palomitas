package es.altia.agora.business.integracionsw.persistence;

import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.integracionsw.DefinicionArraySWVO;
import es.altia.agora.business.integracionsw.DefinicionParametroEntradaVO;
import es.altia.agora.business.integracionsw.DefinicionParametroSalidaVO;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.integracionsw.ParametroConfigurableVO;
import es.altia.agora.business.integracionsw.exception.FalloPublicacionException;
import es.altia.agora.business.integracionsw.persistence.manual.DefinicionOperacionesSWDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefinicionOperacionesSWManager {

    private static DefinicionOperacionesSWManager instance = null;

    protected static Log m_Log = LogFactory.getLog(DefinicionOperacionesSWManager.class.getName());

    protected DefinicionOperacionesSWManager() {
    }

    public static DefinicionOperacionesSWManager getInstance() {
        if (instance == null) {
            synchronized (DefinicionOperacionesSWManager.class) {
                if (instance == null) instance = new DefinicionOperacionesSWManager();
            }
        }
        return instance;
    }

    public Collection getOpsDefinidasBySW(int codigoSW, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getOpsDefinidasBySW");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getOpsDefinidasBySW(codigoSW, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getOpsDefinidasBySW");

        }

    }

    public boolean existeOperacionByTitulo(String tituloOp, int codigoSW, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo existeOperacionByTitulo");
        try {
            return DefinicionOperacionesSWDAO.getInstance().existeOperacionByTitulo(tituloOp, codigoSW, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin existeOperacionByTitulo");

        }
    }

    public int creaEstructuraArray(int codigoSW, String nombreOpSW, String tituloOpSW, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo creaEstructuraArray");
        try {
            return DefinicionOperacionesSWDAO.getInstance().creaEstructuraArray(codigoSW, nombreOpSW, tituloOpSW, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin creaEstructuraArray");

        }
    }

    public void creaEstructuraDatos(int codigoOpDef, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo creaEstructuraDatos");
        try {
            DefinicionOperacionesSWDAO.getInstance().creaEstructuraDatos(codigoOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin creaEstructuraDatos");

        }
    }

    public GeneralValueObject getInfoGeneralOperacion(int codOpDef, String[] params)  throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getInfoGeneralOperacion");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getInfoGeneralOperacion(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getInfoGeneralOperacion");

        }       
    }

    public Collection<DefinicionParametroEntradaVO> getParamsDefEntrada(int codOpDef, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getParamsDefEntrada");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getParamsDefEntrada(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getParamsDefEntrada");

        }
    }

    public Collection<DefinicionParametroSalidaVO> getParamsDefSalida(int codOpDef, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getParamsDefSalida");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getParamsDefSalida(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getParamsDefSalida");

        }
    }

    public void isOperacionDefinida(int codOpDef, String[] params) throws InternalErrorException, FalloPublicacionException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo isOperacionDefinida");
        try {
            DefinicionOperacionesSWDAO.getInstance().isOperacionDefinida(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin isOperacionDefinida");
        }
    }

    public void cambiaEstadoPublicacion(int codOpDef, boolean publicar, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo cambiaEstadoPublicacion");
        try {
            DefinicionOperacionesSWDAO.getInstance().cambiaEstadoPublicacion(codOpDef, publicar, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin cambiaEstadoPublicacion");
        }
    }

    public void updateDefParamIn(DefinicionParametroEntradaVO paramIn, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo updateDefParamIn");
        try {
            DefinicionOperacionesSWDAO.getInstance().updateDefParamIn(paramIn, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin updateDefParamIn");
        }
    }

    public void updateDefParamOut(DefinicionParametroSalidaVO paramOut, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo updateDefParamOut");
        try {
            DefinicionOperacionesSWDAO.getInstance().updateDefParamOut(paramOut, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin updateDefParamOut");
        }
    }

    public boolean areArraysUndefined(int codOpDef, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo areArraysUndefined");
        try {
            return DefinicionOperacionesSWDAO.getInstance().areArraysUndefined(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin areArraysUndefined");
        }
    }

    public Collection getArraysByOpCod(int codOpDef, String[] params) throws InternalErrorException {
        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getArraysByOpCod");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getArraysByOpCod(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getArraysByOpCod");
        }
    }

    public void updateNumRepsArray(DefinicionArraySWVO defArray, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo updateNumRepsArray");
        try {
            DefinicionOperacionesSWDAO.getInstance().updateNumRepsArray(defArray, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin updateNumRepsArray");
        }
    }

    public boolean existeEstructuraDatos(int codigoOpDef, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo existeEstructuraDatos");
        try {
            return DefinicionOperacionesSWDAO.getInstance().existeEstructuraDatos(codigoOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin existeEstructuraDatos");
        }
    }

    public Vector getAllPublishedOperations(String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getAllPublishedOperations");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getAllPublishedOperations(params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getAllPublishedOperations");
        }
    }

    public Vector getParamsEntradaTramitacion(int codOpDef, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getParamsEntradaTramitacion");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getParamsEntradaTramitacion(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getParamsEntradaTramitacion");
        }
    }

    public Vector getParamsSalidaTramitacion(int codOpDef, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getParamsSalidaTramitacion");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getParamsSalidaTramitacion(codOpDef, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getParamsSalidaTramitacion");
        }
    }
    
    public List<AvanzarRetrocederSWVO> getOperacionesSWConParam(int codMunicipio, String codProcedimiento, int codTramite, String[] params) throws TechnicalException {
        m_Log.debug("getOperacionesSWConParam BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Vector<AvanzarRetrocederSWVO> operaciones = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            operaciones = DefinicionSWTramitacionManager.getInstance().getListaConfSW(codMunicipio, codProcedimiento, codTramite, params);
            for (AvanzarRetrocederSWVO op : operaciones) {
                ArrayList<ParametroConfigurableVO> lista = (ArrayList<ParametroConfigurableVO>) DefinicionOperacionesSWDAO.getInstance().getParametrosOpByCfo(op.getCfoFinal(), con);
                op.setListaParametros(lista);
            }
        } catch (AnotacionRegistroException are) {
            m_Log.error("ERROR AL OBTENER LA LISTA DE OPERACIONES DEL TRÁMITE");
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION DE BASE DE DATOS", are);
        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION DE BASE DE DATOS", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION DE BASE DE DATOS", bde);
        } catch (TechnicalException ex) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE UNA OPERACION");
            throw ex;
        } catch (InternalErrorException ex) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE UNA OPERACION");
            throw new TechnicalException(ex.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                m_Log.error("ERROR AL CERRAR LA CONEXION A LA BASE DE DATOS");
            }
        }
        m_Log.debug("getOperacionesSWConParam END");
        return operaciones;
    }

    public void grabaParametros(Vector paramsEntrada, Vector paramsSalida, String[] params) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo grabaParametros");
    	
    	try {
			for (Iterator it = paramsEntrada.iterator();it.hasNext();) {
				ParametroConfigurableVO param = (ParametroConfigurableVO)it.next();
				DefinicionOperacionesSWDAO.getInstance().grabaParametro(param,params);
			}
			for (Iterator it = paramsSalida.iterator();it.hasNext();) {
				ParametroConfigurableVO param = (ParametroConfigurableVO)it.next();
				DefinicionOperacionesSWDAO.getInstance().grabaParametro(param,params);
			}
		} catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
		} finally {
			if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin grabaParametros");
		}		
    	
    }
    
    public int getCodigoOp(long cfo, String[] params) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getCodigoOp");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getCodOp(cfo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getCodigoOp");
        }    	
    }
    public String getTituloOp(int codOp, String[] params) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getTituloOp");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getTituloOp(codOp, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getTituloOp");
        }    	
    }    
    
    public InfoConfTramSWVO getConfVO(long cfo, String[] params) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getConfVO");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getConfVO(cfo, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getConfVO");
        }    	
    }
    
    public Vector getParamsEntrada (long cfo,int codOp, String[] params) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getParamsEntrada");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getParamsEntrada(cfo,codOp, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getParamsEntrada");
        }    	
    }
    
    public Vector getParamsSalida (long cfo,int codOp, String[] params) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo getParamsSalida");
        try {
            return DefinicionOperacionesSWDAO.getInstance().getParamsSalida(cfo,codOp, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin getParamsSalida");
        }    	
    }    
    
    public void setObligatoria (long cfo,int ob, String[] params) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo setObligatoria");
        try {
            DefinicionOperacionesSWDAO.getInstance().setObligatoria(cfo,ob, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin setObligatoria");
        }        	
    }
    public void removeParamsSW(long cfo,Connection con) throws InternalErrorException {
    	if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Comienzo removeParamsSW");
        try {
            DefinicionOperacionesSWDAO.getInstance().removeParamsSW(cfo,con);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionOperacionesSWManager: Fin removeParamsSW");
        }        	
    }


    /**
     * Recupera los nombres de los módulos que puedan tener operaciones asociadas
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la base de datos
     * @return ArrayList<String>
     */
    public ArrayList<String> getModulos(String codProcedimiento,String[] params){
        ArrayList<String> modulos = new ArrayList<String>();
        Connection con = null;
        AdaptadorSQL adapt = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            modulos = DefinicionOperacionesSWDAO.getInstance().getModulos(codProcedimiento, con);

        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);

            }catch(BDException e){
                m_Log.error("Error al cerrar conexión a la BBDD");
                e.printStackTrace();
            }
        }

        return modulos;
    }
  
}
