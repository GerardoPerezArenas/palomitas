package es.altia.flexia.portafirmasexternocliente.plugin.lanbide.servicios;

import es.altia.flexia.portafirmasexternocliente.plugin.lanbide.dao.LanbideEstadoPortafirmasDao;
import es.altia.flexia.portafirmasexternocliente.vo.EstadoPortafirmasHistVO;
import es.altia.flexia.portafirmasexternocliente.vo.EstadoPortafirmasVO;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.Map;
import org.apache.log4j.Logger;


public class LanbideEstadoPortafirmasManager {
    
    //Logger
    private static Logger log = Logger.getLogger(LanbideEstadoPortafirmasManager.class);
    
    //Instance
    private static LanbideEstadoPortafirmasManager instance = null;
    
    /**
     * Recupera una instancia (Singleton) de la clase
     * 
     * @return LanbideEstadoPortafirmasManager
     */
    public static LanbideEstadoPortafirmasManager getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(LanbideEstadoPortafirmasManager.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.debug("Instanciamos la clase");
                    instance = new LanbideEstadoPortafirmasManager();
                }//if(instance == null)
            }//synchronized(LanbideEstadoPortafirmasManager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
   /**
    * Metodo en el cual se inserta el estado inicial de un portafirmas y tambien en el historico de estados
    * @param mapa
    * @param con
    * @return
    * @throws Exception 
    */
    public Long insertarEstadoInicialPortafirmas (Map<String,String> mapa, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("insertarEstadoInicialPortafirmas() : BEGIN");
        Long idEstadoPortafirmas = null;
        try{
            if(log.isDebugEnabled()) log.debug("Creamos la clase EstadoPortafirmasVO y seteamos los valores");
            EstadoPortafirmasVO estadoPortafirmasVO = new EstadoPortafirmasVO();
            estadoPortafirmasVO.setDocumentoExtension(mapa.get("extension"));
            estadoPortafirmasVO.setBuzon(mapa.get("buzon"));
            estadoPortafirmasVO.setFechaActualizacion(new GregorianCalendar());
            estadoPortafirmasVO.setFechaPeticion(new GregorianCalendar());
            estadoPortafirmasVO.setFirmaInformante(mapa.get("firmaImformante"));
            estadoPortafirmasVO.setTaskOid("0");
            estadoPortafirmasVO.setEstado(0);
            estadoPortafirmasVO.setTipoPlatea("FIRMAS");
            
            idEstadoPortafirmas = LanbideEstadoPortafirmasDao.getInstance().insertarEstadoPortafirmas(estadoPortafirmasVO, con);
            
            if(idEstadoPortafirmas != null){
                if(log.isDebugEnabled()) log.debug("Hemos insertado el registro en la tabla de estados del portafirmas");
                //Ahora insertamos en el historico de estados del portafirmas
                EstadoPortafirmasHistVO estadoPortafirmasHistVO = new EstadoPortafirmasHistVO();
                estadoPortafirmasVO.setIdEstadoPortafirmas(idEstadoPortafirmas);
                estadoPortafirmasVO.setFechaActualizacion(new GregorianCalendar());
                estadoPortafirmasHistVO.setEstadoPortafirmas(estadoPortafirmasVO);
                
                LanbideEstadoPortafirmasDao.getInstance().insertarEstadoPortafirmasHist(estadoPortafirmasHistVO, con);
                
            }//if(doc != null)
        }catch(Exception ex){
            log.error("Se ha producido un error a la hora de realizar la inserccion inicial de un estado del portafirmas " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("insertarEstadoInicialPortafirmas() : END");
        return idEstadoPortafirmas;
    }//insertarEstadoInicialPortafirmas

}//class
