package es.altia.flexia.portafirmasexternocliente.plugin.lanbide.dao;

import es.altia.flexia.portafirmasexternocliente.vo.EstadoPortafirmasHistVO;
import es.altia.flexia.portafirmasexternocliente.vo.EstadoPortafirmasVO;
import es.altia.util.SQLUtils;
import es.altia.util.commons.DateOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;


public class LanbideEstadoPortafirmasDao {
    
    //Logger
    private static Logger log = Logger.getLogger(LanbideEstadoPortafirmasDao.class);
    
    //Instance
    private static LanbideEstadoPortafirmasDao instance = null;
    
    /**
     * Recupera una instancia de la clase
     * 
     * @return LanbideEstadoPortafirmasDao
     */
    public static LanbideEstadoPortafirmasDao getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(LanbideEstadoPortafirmasDao.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.debug("Creamos una nueva instancia de la clase");
                    instance = new LanbideEstadoPortafirmasDao();
                }//if(instance == null)
            }//synchronized(LanbideEstadoPortafirmasDao.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * SQL de inserccion en la tabla ESTADO_PORTAFIRMAS
     */
    private final String SQL_INSERTAR_ESTADO_PORTAFIRMAS = "INSERT INTO ESTADO_PORTAFIRMAS "
            + "(ID_ESTADO_PORTAFIRMAS, DOCUMENTO_EXTENSION, TASK_OID, TIPO_PLATEA, SUB_TIPO_PLATEA, ESTADO, BUZON, FIRMA_INFORMANTE, FECHA_PET, FECH_ACT) "
            + "VALUES "
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    
    /**
     * Metodo donde se inserta un registro en la tabla estado del portafirmas (ESTADO_PORTAFIRMAS)
     * @param estadoPortafirmasVO
     * @param con
     * @return
     * @throws Exception 
     */
    public Long insertarEstadoPortafirmas(EstadoPortafirmasVO estadoPortafirmasVO, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : BEGIN");
        
        Long idEstadoPortafirmas = null;
        
        try{
            //Se obtiene el idEstadoPortafirmas de la secuencia
            idEstadoPortafirmas = SQLUtils.obtenerValorSecuencia("ID_ESTADO_PORTAFIRMAS_SEQ",con);
            
            PreparedStatement ps = con.prepareStatement(SQL_INSERTAR_ESTADO_PORTAFIRMAS);
            if(log.isDebugEnabled()) log.debug("SQL = " + SQL_INSERTAR_ESTADO_PORTAFIRMAS);
            if(log.isDebugEnabled()) log.debug(idEstadoPortafirmas);
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getDocumentoExtension());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getTaskOid());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getTipoPlatea());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getSubTipoPlatea());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getEstado());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getBuzon());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getFirmaInformante());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getFechaPeticion());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getFechaActualizacion());
            int i= 1;
            ps.setLong(i++, idEstadoPortafirmas);
            ps.setString(i++, estadoPortafirmasVO.getDocumentoExtension());
            ps.setString(i++, estadoPortafirmasVO.getTaskOid());
            ps.setString(i++, estadoPortafirmasVO.getTipoPlatea());
            ps.setString(i++, estadoPortafirmasVO.getSubTipoPlatea());
            ps.setInt(i++, estadoPortafirmasVO.getEstado());
            ps.setString(i++, estadoPortafirmasVO.getBuzon());
            ps.setString(i++, estadoPortafirmasVO.getFirmaInformante());
            ps.setTimestamp(i++, DateOperations.toTimestamp(estadoPortafirmasVO.getFechaPeticion()));
            ps.setTimestamp(i++, DateOperations.toTimestamp(estadoPortafirmasVO.getFechaActualizacion()));
            
            ps.executeQuery();
            
            if(log.isDebugEnabled()) log.debug("Se ha isnertado correctamente y el idEstadoPortafirmas que se devuelve es " + idEstadoPortafirmas);
            
        }catch(Exception ex){
            log.error("Se ha producido un error al insertar el estado del portafirmas = " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : END");
        return idEstadoPortafirmas;
    }//insertarEstadoPortafirmas
    
    /**
     * SQL de inserccion en la tabla ESTADO_PORTAFIRMAS_H
     */
    private final String SQL_INSERTAR_ESTADO_PORTAFIRMAS_HIST = "INSERT INTO ESTADO_PORTAFIRMAS_H "
            + "(ID_ESTADO_PORTAFIRMAS_H, ESTADO_PORTAFIRMAS, TASK_OID, TIPO_PLATEA, SUB_TIPO_PLATEA, ESTADO, BUZON, FIRMA_INFORMANTE, FECH_ACT) "
            + "VALUES "
            + "(?, ?, ?, ?, ?, ?, ?, ?, ? )";
    
    /**
     * Metodo donde se inserta un registro en la tabla historico de estados del portafirmas (ESTADO_PORTAFIRMAS_H)
     * @param estadoPortafirmasHistVO
     * @param con
     * @return
     * @throws Exception 
     */
    public Long insertarEstadoPortafirmasHist(EstadoPortafirmasHistVO estadoPortafirmasHistVO, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : BEGIN");
        
        Long idEstadoPortafirmasHist = null;
        
        try{
            //Se obtiene el idEstadoPortafirmas de la secuencia
            idEstadoPortafirmasHist = SQLUtils.obtenerValorSecuencia("ID_ESTADO_PORTAFIRMAS_H_SEQ",con);
            
            EstadoPortafirmasVO estadoPortafirmasVO = estadoPortafirmasHistVO.getEstadoPortafirmas();
            
            PreparedStatement ps = con.prepareStatement(SQL_INSERTAR_ESTADO_PORTAFIRMAS_HIST);
            if(log.isDebugEnabled()) log.debug("SQL = " + SQL_INSERTAR_ESTADO_PORTAFIRMAS_HIST);
            if(log.isDebugEnabled()) log.debug(idEstadoPortafirmasHist);
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getIdEstadoPortafirmas());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getTaskOid());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getTipoPlatea());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getSubTipoPlatea());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getEstado());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getBuzon());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getFirmaInformante());
            if(log.isDebugEnabled()) log.debug(estadoPortafirmasVO.getFechaActualizacion());
            int i= 1;
            ps.setLong(i++, idEstadoPortafirmasHist);
            ps.setLong(i++, estadoPortafirmasVO.getIdEstadoPortafirmas());
            ps.setString(i++, estadoPortafirmasVO.getTaskOid());
            ps.setString(i++, estadoPortafirmasVO.getTipoPlatea());
            ps.setString(i++, estadoPortafirmasVO.getSubTipoPlatea());
            ps.setInt(i++, estadoPortafirmasVO.getEstado());
            ps.setString(i++, estadoPortafirmasVO.getBuzon());
            ps.setString(i++, estadoPortafirmasVO.getFirmaInformante());
            ps.setTimestamp(i++, DateOperations.toTimestamp(estadoPortafirmasVO.getFechaActualizacion()));
            
            ps.executeQuery();
            
            if(log.isDebugEnabled()) log.debug("Se ha isnertado correctamente y el idEstadoPortafirmasHist que se devuelve es " + idEstadoPortafirmasHist);
            
        }catch(Exception ex){
            log.error("Se ha producido un error al insertar en el historico de estado del portafirmas = " + ex.getMessage());
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : END");
        return idEstadoPortafirmasHist;
    }//insertarEstadoPortafirmas
    
}//class
