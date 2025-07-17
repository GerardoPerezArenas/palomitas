/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.business.comunicaciones;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.business.comunicaciones.persistence.AdjuntoComunicacionFlexiaDAO;
import es.altia.flexia.business.comunicaciones.persistence.ComunicacionesFlexiaDAO;
import es.altia.flexia.business.comunicaciones.vo.ComunicacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Ricardo
 */
public class ComunicacionFlexiaManager {
      // Mi propia instancia usada en el metodo getInstance.
    private static ComunicacionFlexiaManager instance = null;

    protected static Log m_Log =
            LogFactory.getLog(ComunicacionFlexiaManager.class.getName());

    /**
     *  Construnctor
     */
    protected ComunicacionFlexiaManager() {
      
    }


    /** Método que inicializa la clase si no lo habia sido previamente
     * 
     * @return Instancia de ComunicacionFlexiaManager
     */
    public static ComunicacionFlexiaManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(ComunicacionFlexiaManager.class) {
                if (instance == null)
                    instance = new ComunicacionFlexiaManager();
            }
        }
        return instance;
    }
   
   
    
    /** Método que obtiene los datos de una comunicación por identificador único
     * 
     * @param idComunicacion Identificador único de la comunicación a buscar
     * @param params Parámetros de conexión a BBDD
     * @return Datos de la comunicación buscada
     * @throws TramitacionException Si ocurre error inesperado lanzará excepción
     * @throws TechnicalException Ante error técnico lanzará excepción
     */
    public ComunicacionVO getComunicacion(Long idComunicacion, String[] params) throws TramitacionException, TechnicalException{
        if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion() : ID" + idComunicacion );}
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        try {
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion() :  Se obtiene la conexion");}
            con = oad.getConnection();
            
            //Buscamos los datos de la comunicacion
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion() :  Se llama al DAO de comunicaciones");}
            ComunicacionVO salida = ComunicacionesFlexiaDAO.getInstance().getComunicacion(idComunicacion, con);
            
            //Buscamos los adjuntos y los asignamos
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion() :  Buscando los adjuntos");}
            salida.setListaAdjuntos(AdjuntoComunicacionFlexiaDAO.getInstance().getListaAdjuntos(idComunicacion, con));
            
            return salida;
            
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } finally {
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion() :  Cerrando la conexión");}
            //Se librea la conexion
            SigpGeneralOperations.devolverConexion(oad, con);
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicacion() :  FIN");}
        }                
    }
     
     
    /** Método que marca una comunicación como leida
     * 
     * @param idComunicacion Identificador único de la comunicación
     * @param params Parámetros de conexión con BBDD
     * @return Si la operación ha tenido éxito o no
     * @throws TramitacionException  Si ocurre error inesperado lanzará excepción
     * @throws TechnicalException Ante error técnico lanzará excepción
     */
    public Boolean marcarComunicacion(Long idComunicacion, String[] params) throws TramitacionException, TechnicalException{
        if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".marcarComunicacion() : ID" + idComunicacion );}
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        try {
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".marcarComunicacion() :  Se obtiene la conexion");}
            con = oad.getConnection();
            
            //Buscamos los datos de la comunicacion
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".marcarComunicacion() :  Se llama al DAO de comunicaciones");}
            return ComunicacionesFlexiaDAO.getInstance().marcarComunicacionLeida(idComunicacion, con);
            
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } finally {
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".marcarComunicacion() :  Cerrando la conexión");}
            //Se librea la conexion
            SigpGeneralOperations.devolverConexion(oad, con);
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".marcarComunicacion() :  FIN");}
        }                
    }
}
