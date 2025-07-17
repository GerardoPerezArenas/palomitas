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
import es.altia.flexia.business.comunicaciones.vo.AdjuntoComunicacionVO;
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
public class AdjuntoComunicacionFlexiaManager {
      // Mi propia instancia usada en el metodo getInstance.
    private static AdjuntoComunicacionFlexiaManager instance = null;

    protected static Log m_Log =
            LogFactory.getLog(AdjuntoComunicacionFlexiaManager.class.getName());

    /**
     *  Construnctor
     */
    protected AdjuntoComunicacionFlexiaManager() {
      
    }


    /** Método que inicializa la clase si no lo habia sido previamente
     * 
     * @return Instancia de AdjuntoComunicacionFlexiaManager
     */
    public static AdjuntoComunicacionFlexiaManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(AdjuntoComunicacionFlexiaManager.class) {
                if (instance == null)
                    instance = new AdjuntoComunicacionFlexiaManager();
            }
        }
        return instance;
    }
    
        
 
    public AdjuntoComunicacionVO getAdjunto(Long idComunicacion,Long idAdjunto, String[] params) throws TramitacionException, TechnicalException{
        if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto() : ID" + idAdjunto );}
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        try {
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto() :  Se obtiene la conexion");}
            con = oad.getConnection();
            
            //Buscamos los datos del adjunto
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto() :  Se llama al DAO de adjuntos");}
            return AdjuntoComunicacionFlexiaDAO.getInstance().getAdjunto(idComunicacion, idAdjunto, con);
            
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } finally {
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto() :  Cerrando la conexión");}
            //Se librea la conexion
            SigpGeneralOperations.devolverConexion(oad, con);
            if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto() :  FIN");}
        }                
    }
     
     
    
}
