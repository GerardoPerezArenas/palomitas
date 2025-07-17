package es.altia.agora.business.registro.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.persistence.manual.HistoricoMovimientoDAO;
import es.altia.common.exception.TechnicalException;

import java.sql.Connection;
import java.util.Vector;

/**
 *
 * @author juan.jato
 */
public class HistoricoMovimientoManager {

    // La instancia unica
    private static HistoricoMovimientoManager instance = 
            new HistoricoMovimientoManager();
    // Log para mensajes
    protected static Log m_Log =
            LogFactory.getLog(HistoricoMovimientoManager.class.getName());
    
    protected HistoricoMovimientoManager() {
    }
    
    /**
     * Factory method para el <code>Singleton</code>.
     *
     * @return La unica instancia de HistoricoMovimientoManager
     */
    public static HistoricoMovimientoManager getInstance() {
        return instance;
    }
    
    /**
     * Inserta el movimiento del hist�rico en la BD.
     * 
     * @param vo El HistoricoMovimientoValueObject que representa el movimiento.
     * @param con Conexi�n a la BD con transacci�n abierta.
     * @param params Par�metros de conexi�n a BD (para inicializar el adaptador).
     * @throws es.altia.common.exception.TechnicalException
     */
    public void insertarMovimientoHistorico(HistoricoMovimientoValueObject vo, Connection con, String[] params) 
            throws TechnicalException {
            HistoricoMovimientoDAO.getInstance().insertarMovimientoHistorico(vo, con, params);
    }
    
    /**
     * Devuelve el listado de movimientos para una anotaci�n, con los datos
     * necesarios para mostrarlo ATENCION excepto la descripci�n del movimiento 
     * (tan s�lo con su tipo num�rico) pues depende del idioma del usuario.
     * 
     * @param codigoAnotacion El c�digo de la anotacion en el formato D/U/T/A/N
     *        donde D = codigo departamento, U = codigo unidad organica, T = tipo
     *        de la anotaci�n, A = ejercicio, N = numero
     * @param params Parametros de conexi�n a BD.
     * @return Un vector de HistoricoMovimientoValueObject
     */
    public Vector<HistoricoMovimientoValueObject> obtenerHistoricoAnotacion(
            String codigoAnotacion, String[] params) {
       
       Vector<HistoricoMovimientoValueObject> resultado = new Vector<HistoricoMovimientoValueObject>();
       try {
           resultado = 
               HistoricoMovimientoDAO.getInstance().obtenerHistoricoAnotacion(
                   codigoAnotacion, params);
       } catch (Exception e) {
           m_Log.error("JDBC Technical problem " + e.getMessage());
           e.printStackTrace();
       }
       return resultado;
    }
    
    /**
     * Devuelve todos los datos de un movimiento incluyendo los detalles en formato
     * XML sin procesar (el procesamiento depende de la manera en que se vayan a mostrar).
     * @param codigoMovimiento C�digo identificativo del movimiento.
     * @param params Par�metros de conexi�n a BD.
     * @return Un HistoricoMovimientoValueObject con todos los datos del movimiento.
     */
    public HistoricoMovimientoValueObject obtenerMovimiento(int codigoMovimiento,
            String[] params) {
        
       HistoricoMovimientoValueObject resultado = new HistoricoMovimientoValueObject();
       try {
           resultado = 
               HistoricoMovimientoDAO.getInstance().obtenerMovimiento(
                   codigoMovimiento, params);
       } catch (Exception e) {
           m_Log.error("JDBC Technical problem " + e.getMessage());
           e.printStackTrace();
       }
       return resultado;
    }
}
