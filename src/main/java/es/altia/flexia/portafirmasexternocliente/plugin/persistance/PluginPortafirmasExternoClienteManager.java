/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.portafirmasexternocliente.plugin.persistance;

import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramitacionVO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.flexia.portafirmasexternocliente.exception.PortafirmasExternoException;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.flexia.portafirmasexternocliente.plugin.PluginPortafirmasExternoCliente;
import es.altia.flexia.portafirmasexternocliente.plugin.persistance.manual.PluginPortafirmasExternoClienteDao;
import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;
import es.altia.flexia.portafirmasexternocliente.vo.InfoTramiteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * @author david.caamano
 * @version 04/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 04/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class PluginPortafirmasExternoClienteManager {
    
    //Logger
    private static Logger log = Logger.getLogger(PluginPortafirmasExternoClienteManager.class);
    
    //Instance
    private static PluginPortafirmasExternoClienteManager instance = null;
    
    /**
     * Recupera una instancia (Singleton) de la clase
     * 
     * @return PluginPortafirmasExternoClienteManager
     */
    public static PluginPortafirmasExternoClienteManager getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(PluginPortafirmasExternoClienteManager.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.debug("Instanciamos la clase");
                    instance = new PluginPortafirmasExternoClienteManager();
                }//if(instance == null)
            }//synchronized(PluginPortafirmasExternoClienteManager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Recupera un documento de tramitacion por el procedimiento, numero de expediente, codigo de tramite, ocurrencia y codigo del documento
     * 
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @param params
     * @return DocumentoTramitacionVO
     * @throws Exception 
     */
    public DocumentoTramitacionVO getDocumentoTramitacion(String codOrganizacion, String codProcedimiento, String numExpediente, String codTramite, 
            String ocurrenciaTramite, String codDocumento, String[] params, String portafirmas) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DocumentoTramitacionVO doc = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if(log.isDebugEnabled()) log.debug("Recuperamos el documento de tramitación");
            doc = PluginPortafirmasExternoClienteDao.getInstance().
                    getDocumentoTramitacion(codOrganizacion, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, codDocumento, con,
                    params, portafirmas);
            
            if(doc != null){
                if(log.isDebugEnabled()) log.debug("Recuperamos la info del firmante del documento");
                doc = PluginPortafirmasExternoClienteDao.getInstance().getDatosFirmaDocumentoTramitacion(doc, con, portafirmas);
            }//if(doc != null)
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el documento de tramitacion " + ex.getMessage());
            throw ex;
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getDocumentoTramitacion() : END");
        return doc;
    }//getDocumentoTramitacion
    
    /**
     * Recupera la lista de documentos pendientes de firma para un expediente, tramite y ocurrencia de tramite que estén en un portafirmas
     * externo.
     * 
     * @param codOrganizacion
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param params
     * @return ArrayList<DocumentoTramitacionVO>
     * @throws Exception 
     */
    public ArrayList<DocumentoTramitacionVO> getDocumentosTramitacionFirmaPendiente(String codOrganizacion, String codProcedimiento, 
            String numExpediente, String codTramite, String ocurrenciaTramite, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDocumentosTramitacionFirmaPendiente() : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<DocumentoTramitacionVO> listaDocumentos = new ArrayList<DocumentoTramitacionVO>();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if(log.isDebugEnabled()) log.debug("Recuperamos la lista de documentos pendientes de firma");
            listaDocumentos  = PluginPortafirmasExternoClienteDao.getInstance().
                    getDocumentosTramitacionFirmaPendiente(codOrganizacion, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando los documentos pendientes de firma " + ex.getMessage());
            throw ex;
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDocumentosTramitacionFirmaPendiente() : END");
        return listaDocumentos;
    }//getDocumentosTramitacionFirmaPendiente
    
    /**
     * Recupera la lista de documentos pendientes de firma para un expediente,
     * tramite y ocurrencia de tramite que estén en un portafirmas externo.
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param ocuTramite
     * @param params
     * @return ArrayList<DocumentoTramitacionVO>
     * @throws Exception
     */
    public ArrayList<FirmaDocumentoTramitacionVO> getDocumentosTramitacionFirmaPendienteFlujoUsuario(
            Integer codOrganizacion, String codProcedimiento, Integer ejercicio, String numExpediente,
            Integer codTramite, Integer ocuTramite, String[] params)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("getDocumentosTramitacionFirmaPendienteFlujoUsuario() : BEGIN");
        }
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<FirmaDocumentoTramitacionVO> listaDocumentos = new ArrayList<FirmaDocumentoTramitacionVO>();
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if (log.isDebugEnabled()) {
                log.debug("Recuperamos la lista de documentos pendientes de firma");
            }
            listaDocumentos = PluginPortafirmasExternoClienteDao.getInstance().
                    getDocumentosTramitacionFirmaPendienteFlujoUsuario(
                            codOrganizacion, codProcedimiento, ejercicio, numExpediente,
                            codTramite, ocuTramite, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los documentos pendientes de firma " + ex.getMessage());
            throw ex;
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getDocumentosTramitacionFirmaPendienteFlujoUsuario() : END");
        }
        return listaDocumentos;
    }//getDocumentosTramitacionFirmaPendiente
    
    /**
     * Actualiza el estado de firma del documento de tramitacion
     * 
     * @param doc
     * @param estado
     * @param codUsuarioFirmante
     * @param params
     * @throws Exception 
     */
    public void actualizaEstadoDocumento(DocumentoTramitacionVO doc, String estado, Integer codUsuarioFirmante, String[] params) 
            throws Exception{
        if(log.isDebugEnabled()) log.debug("actualizaEstadoDocumento() : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            con.setAutoCommit(false);
            if(log.isDebugEnabled()) log.debug("Actualizamos las tablas de los documentos de tramitacion");
            PluginPortafirmasExternoClienteDao.getInstance().actualizaEstadoDocumento(doc, estado, codUsuarioFirmante, con);
            con.commit();
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando los documentos pendientes de firma " + ex.getMessage());
            adapt.rollBack(con);
            throw ex;
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("actualizaEstadoDocumento() : END");
    }//actualizaEstadoDocumento
    
    /**
     * Recupera de la BBDD el codigo de usuario del usuario con el documento que le pasamos como parametro
     * 
     * @param nif
     * @param params
     * @return Integer
     * @throws Exception 
     */
    public Integer getCodUsuarioPorNif (String nif,  String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCodUsuarioPorNif() : BEGIN"); 
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Integer doc = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if(log.isDebugEnabled()) log.debug("Recuperamos el numero de usuario por su documento");
            doc = PluginPortafirmasExternoClienteDao.getInstance().getCodUsuarioPorNif(nif, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el usuario firmante " + ex.getMessage());
            throw ex;
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCodUsuarioPorNif() : END");
        return doc;
    }//getCodUsuarioPorNif
    
    /**
     * Metodo que devuelve la informacion de un tramite en funcion del expediente, numero y ocurrencia del tramite
     * 
     * @param codOrganizacion
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param params
     * @return InfoTramiteVO
     * @throws Exception 
     */
    public InfoTramiteVO getInfoTramite (String codOrganizacion, String numExpediente, String codTramite, 
            String ocurrenciaTramite, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getInfoTramite() : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        InfoTramiteVO info = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if(log.isDebugEnabled()) log.debug("Recuperamos la informacion del tramite");
            info = PluginPortafirmasExternoClienteDao.getInstance().
                    getInfoTramite(codOrganizacion, numExpediente, codTramite, ocurrenciaTramite, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la informacion del tramite " + ex.getMessage());
            throw ex;
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getInfoTramite() : END");
        return info;
    }//getInfoTramite
    
    /**
     * Devuelve una lista con los tramites pendientes de un expediente
     * 
     * @param codOrganizacion
     * @param numExpediente
     * @param params
     * @return ArrayList<TramitacionExpedientesValueObject>
     * @throws Exception 
     */
    public ArrayList<TramitacionExpedientesValueObject> getTramitesPendientes(String codOrganizacion, String numExpediente, String[] params)
            throws Exception{
        if(log.isDebugEnabled()) log.debug("getTramitesPendientes() : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<TramitacionExpedientesValueObject> listaTramites = new ArrayList<TramitacionExpedientesValueObject>();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if(log.isDebugEnabled()) log.debug("Recuperamos los tramites pendientes");
            listaTramites = PluginPortafirmasExternoClienteDao.getInstance().getTramitesPendientes(codOrganizacion, numExpediente, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando los tramites pendientes para el expediente: " + numExpediente + " " 
                    + ex.getMessage());
            throw ex;
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getTramitesPendientes() : END");
        return listaTramites;
    }//getTramitesPendientes
    
    /**
     * Comprueba que un usuario exista en el portafirmas externo
     * 
     * @param codOrganizacion
     * @param codMunicipio
     * @param documento
     * @param params
     * @return Boolean
     * @throws Exception 
     */
    public Boolean comprobarUsuarioPortafirmasPorDocumento(String codOrganizacion, String documento,  String[] params) throws Exception{

        log.debug("comprobarUsuarioPortafirmasPorDocumento() : BEGIN");
        
        Boolean existeUsuario = null;
        
        try{
            //Recuperamos la propiedad que nos indica si existe un portafirmas externo
            log.debug("Comprobamos si existe un portafirmas externo");
            
            Boolean existePortafirmasExterno
                    = PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(codOrganizacion);
            
            if (log.isDebugEnabled()) {
                log.debug("existe un portafirmas externo = " + existePortafirmasExterno);
            }

            if (existePortafirmasExterno) {
                PluginPortafirmasExternoCliente pluginPortafirmasExterno = PluginPortafirmasExternoClienteFactoria.getImplClass(codOrganizacion);
                existeUsuario = pluginPortafirmasExterno.comprobarUsuarioPortafirmas(codOrganizacion, documento, params);
            } else { // Si no existe portafirmas externos enviamos que el usuario existe
                existeUsuario = Boolean.TRUE;
            }
            
            if (existeUsuario == null) { // Ha ocurrido un error
                throw new PortafirmasExternoException("Ha ocurrido un error al intentar acceder al portafirmas externo");
            }
        } catch(Exception ex) {
            log.error("Se ha producido un error comprobando el usuario firmante: " + ex.getMessage());
            throw ex;
        }
        
        log.debug("comprobarUsuarioPortafirmasPorDocumento() : END");
        
        return existeUsuario;
    }//comprobarUsuarioPortafirmasPorDocumento

}//class
