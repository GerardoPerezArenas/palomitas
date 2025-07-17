package es.altia.agora.business.sge.persistence;

import es.altia.common.service.config.*;
import es.altia.common.exception.TechnicalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.persistence.manual.DocumentosExpedienteDAO;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirmaBBDD;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business.SWFirmaDocManager;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentosExpedienteManager {

    private static DocumentosExpedienteManager instance = null;
    protected static Config conf;
    protected static Config m_ConfigCommon;
    protected static Log m_Log = LogFactory.getLog(DocumentosExpedienteManager.class.getName());

    protected DocumentosExpedienteManager() {
        //Queremos usar el fichero de configuración technical y common
        conf = ConfigServiceHelper.getConfig("techserver");
        m_ConfigCommon = ConfigServiceHelper.getConfig("common");
    }

    /**
     * Factory method para el <code>Singelton</code>.
     * @return La unica instancia de DocumentosAplicacionManager
     */
    public static DocumentosExpedienteManager getInstance() {

        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(DocumentosExpedienteManager.class) {
                if (instance == null)
                    instance = new DocumentosExpedienteManager();
            }
        }
        return instance;
    }


    public String consultaXML(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosExpedienteManager.consultaXML");

        String resultado="";
        try {
            resultado = DocumentosExpedienteDAO.getInstance().consultaXML(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }


    public String consultaXML2(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosExpedienteManager.consultaXML2");

        String resultado="";
        try {
            resultado = DocumentosExpedienteDAO.getInstance().consultaXML2(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }

      public HashMap consultaHashEtiquetasValor(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosExpedienteManager.consultaXML");

        HashMap resultado=new HashMap();
        try {
            resultado = DocumentosExpedienteDAO.getInstance().consultaHashEtiquetasValor(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }
      
      
       public HashMap consultaHashEtiquetasValor2(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosExpedienteManager.consultaXML");

        HashMap resultado=new HashMap();
        try {
            resultado = DocumentosExpedienteDAO.getInstance().consultaHashEtiquetasValor2(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }

    public byte[] loadDocumento(GeneralValueObject gVO,String[] params) {

        // Comienzo del metodo.
        m_Log.info("DocumentosExpedienteManager.loadDocumento");

        byte[] resultado=null;
        try {
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si"))
                resultado = SWFirmaDocManager.getInstance(params).loadDocumento(gVO);
            else
                resultado = DocumentosExpedienteDAO.getInstance().loadDocumento(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
    }
   

    public int grabarDocumento(GeneralValueObject gVO, String[] params) throws WSException, TechnicalException {

        // Comienzo del metodo.
        m_Log.info("DocumentosExpedienteManager.grabarDocumento");
        int resultado;
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si"))
                resultado = SWFirmaDocManager.getInstance(params).grabarDocumento(gVO);
            else
                resultado = DocumentosExpedienteDAO.getInstance().grabarDocumento(gVO,params);
        return (resultado);
    }


    public void actualizarFechaInforme(GeneralValueObject gVO, String[] params){

        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        try{
            
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            // Comienzo del metodo.
            m_Log.info(" ==============> DocumentosExpedienteManager.actualizarFechaInforme BEGIN");            
            DocumentosExpedienteDAO.getInstance().actualizarFechaInforme(gVO,con);
            adapt.finTransaccion(con);
            m_Log.info(" DocumentosExpedienteManager.actualizarFechaInforme END <==============");
        }catch(Exception e){
            try{
                adapt.rollBack(con);
                e.printStackTrace();
                m_Log.debug(this.getClass().getName() + ".actualizarFechaInforme: " + e.getMessage());
            }catch(Exception ex){
                ex.printStackTrace();
                m_Log.debug(this.getClass().getName() + ".actualizarFechaInforme: " + ex.getMessage());
            }
        }
    }


    /**
     * Graba un documento en base de datos pero en el contenido no se graba el contenido del documento, puesto que éste se almacena en un gestor documental
     * @param gVO: Objeto que contiene los datos del objeto a dar de alta
     * @param params: Parámetros de conexión a base de datos
     * @return Número de documento
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * @throws es.altia.common.exception.TechnicalException
     */
    public int grabarDocumentoGestor(GeneralValueObject gVO, String[] params) throws WSException, TechnicalException {
        // Comienzo del metodo.
        m_Log.info("DocumentosExpedienteManager.grabarDocumentoGestor");
        int resultado;
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")){
                // TODO: DOCUMENTO FIRMADO EN GESTOR por hacer y fijar la salida
                resultado = SWFirmaDocManager.getInstance(params).grabarDocumento(gVO);
            }
            else
                resultado = DocumentosExpedienteDAO.getInstance().grabarDocumentoGestor(gVO,params);

        return resultado;
    }



     /**
     * Recupera el nombre original de un documento de tramitación
     * @param gVO: Objeto que contiene los datos del objeto
     * @param params: Parámetros de conexión a base de datos
     * @return Nombre original del documento
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * @throws es.altia.common.exception.TechnicalException
     */
    public String getNombreDocumentoGestor(GeneralValueObject gVO, String[] params) throws WSException, TechnicalException {
        // Comienzo del metodo.
        m_Log.info("DocumentosExpedienteManager.getNombreDocumentoDocumentoGestor");
        String resultado ="";
        Connection con = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            resultado = DocumentosExpedienteDAO.getInstance().getNombreDocumentoGestor(gVO, con);
        }catch(Exception e){
            throw new TechnicalException("Error " + e.getMessage());
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
            }
        }

        return resultado;
    }


  /**
     * Graba un documento en base de datos y devuelve el numero de documento que se le asigna
     * @param gVO: Objeto que contiene los datos del objeto a dar de alta
     * @param params: Parámetros de conexión a base de datos
     * @return Número de documento
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * @throws es.altia.common.exception.TechnicalException
     */
    public int grabarDocumentoGestorContenido(GeneralValueObject gVO, String[] params) throws WSException, TechnicalException {
        // Comienzo del metodo.
        m_Log.info("DocumentosExpedienteManager.grabarDocumentoGestorContenido");
        int resultado;
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")){
                // TODO: DOCUMENTO FIRMADO EN GESTOR por hacer y fijar la salida
                resultado = SWFirmaDocManager.getInstance(params).grabarDocumento(gVO);
            }
            else
                resultado = DocumentosExpedienteDAO.getInstance().grabarDocumentoGestorContenido(gVO,params);

        return resultado;
    }
    
    public void duplicarDocumentoTramitacionConFirma(DocumentoFirma doc, String[] params) throws BDException, SQLException{
        m_Log.info("DocumentosExpedienteManager.duplicarDocumentoTramitacionConFirma");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DocumentosExpedienteDAO docExpDAO = DocumentosExpedienteDAO.getInstance();
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            // Obtenemos los datos del documento
            docExpDAO.getDocumentoTramitacionConFirma(doc, con);
            
            // Al duplicar si el estado de los ficheros es L (un flujo de firma) o U (un usuario),
            // para determinar si se trata de un documento original se modifica los estados y
            // se convierten a M y V respectivamente.
            // Si lo que se duplica es un fichero en estado M y V se debe primero convertir
            // su estado a L y U, respectivamente, para que el documento se procese con sus
            // estados normales, ya que el original pasa a ser la copia (tal y como está implementado)
            // y durante ese duplicado se establecen los valores M y V.
            docExpDAO.revertEstadoDuplicadoDocumentoTramitacionConFirma(doc, con);
            
            // Añadimos un nuevo documento con los mismos datos
            int codDocDuplicado = docExpDAO.setDocumentoTramitacionConFirma(doc, adapt, con);
            m_Log.debug(String.format("Código del documento duplicado = %d.", codDocDuplicado));
            
            if(codDocDuplicado == -1) {
                throw new TechnicalException("El documento duplicado no ha sido insertado.");
            }
            
            SigpGeneralOperations.commit(adapt, con);
        } catch (TechnicalException te) {
            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException tte) {
                tte.printStackTrace();
                m_Log.error("Ha ocurrido un error al realizar rollback en BBDD");
            }

            m_Log.error(te.getMessage());
            throw new BDException(te.getMessage());
        } catch (BDException bde){
            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException te) {
                te.printStackTrace();
                m_Log.error("Ha ocurrido un error al realizar rollback en BBDD");
            }

            m_Log.error("Ha ocurrido un error de BBDD.");
            throw bde;
        } catch (SQLException ex) {
            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error("Ha ocurrido un error al realizar rollback en BBDD");
            }

            m_Log.error("Ha ocurrido un error al duplicar el documento.");
            throw ex;
        } finally {
            try{
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (TechnicalException te) {
                m_Log.error("Ha ocurrido un error al devolver la conexión de BBDD");
            }
        }
    }
    
    public void eliminarDocumentoTramitacionDuplicado (DocumentoFirmaBBDD docBD, String[] params) throws BDException, SQLException{
        m_Log.info("DocumentosExpedienteManager.eliminarDocumentoTramitacionDuplicado");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DocumentosExpedienteDAO docExpDAO = DocumentosExpedienteDAO.getInstance();
        
        
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            docExpDAO.eliminarDocumentoTramitacionDuplicado(docBD, adapt, con);
            
        } catch (BDException bde){
            m_Log.error("Ha ocurrido un error al obtener una conexión a la BBDD.");
            throw bde;
        } finally {
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("Ha ocurrido un error al devolver la conexión");
            }
        }
    }

}