package es.altia.agora.webservice.registro.rec.dao;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.jdbc.GeneralOperations;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class RecFacade {

    private static RecFacade instance = null;
    private final String PREFFIX_REC        = "Rec";
    private final String SUFFIX_URL_JDBC    = "url_jdbc";
    private final String SUFFIX_BBDD_DRIVER = "bbdd_driver";
    private final String SUFFIX_USER_JDBC   = "user_jdbc";
    private final String SUFFIX_PASS_JDBC   = "pass_jdbc";
    private final String BARRA = "/";
    private final int LONGITUD_MAXIMA_NUMANOTACIONREC = 6;
    
    private Logger log = Logger.getLogger(RecFacade.class);    
            
    private RecFacade(){}
    
    public static RecFacade getInstance(){
        if(instance==null)
            instance = new RecFacade();        
        return instance;
    }
        
    /**
     * Devuelve la conexión a la BBDD del rec
     * @return
     */
    private Connection getConnection(){        
        Connection connection = null;
        try{
        
            ResourceBundle bundle = ResourceBundle.getBundle("es.altia.agora.webservice.registro.rec.configuracion.configuracion");        
            String url_jdbc    = bundle.getString(PREFFIX_REC + BARRA + SUFFIX_URL_JDBC);
            String bbdd_driver = bundle.getString(PREFFIX_REC + BARRA + SUFFIX_BBDD_DRIVER);
            String user_jdbc   = bundle.getString(PREFFIX_REC + BARRA + SUFFIX_USER_JDBC);
            String pass_jdbc   = bundle.getString(PREFFIX_REC + BARRA + SUFFIX_PASS_JDBC);
                
            // Se instancia el driver de la BBDD del REC
            Class.forName(bbdd_driver);    
            connection = DriverManager.getConnection(url_jdbc,user_jdbc,pass_jdbc);
        }
        catch(Exception e){
            log.error("Error al obtener conexión a la base de datos: " + e.getMessage());
        }                        
        return connection;
    }
    
    
    public Vector<TramitacionValueObject> getAnotacionesRec(String fechaDesde,String fechaHasta,String origen) throws Exception
    {
        Connection connection = null;
        Vector<TramitacionValueObject> aux = null;
                 
        try{
            connection = this.getConnection();
            RecDAO dao = RecDAO.getInstance();
            aux = dao.getAnotacionesRec(connection, fechaDesde, fechaHasta, origen);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            GeneralOperations.closeConnection(connection);
        }        
        return aux;
    }
    
    /**
     * Recupera una determinada anotaciones     
     * @param codigoUnuidadRegistro: Código de la unidad de registro
     * @param numeroAsiento: Número de asiento
     * @param ejercicioAsiento: Ejercicio del asiento
     * @return Colección de TramitacionValueObject
     * @throws java.lang.Exception
     */
    public TramitacionValueObject getAnotacionRec(String codigoUnidadRegistro,String numeroAsiento,String ejercicioAsiento) throws Exception
    {
        Connection connection = null;
        TramitacionValueObject aux = new TramitacionValueObject();
        
        try{
            connection = this.getConnection();
            RecDAO dao = RecDAO.getInstance();
            aux = dao.getAnotacionRec(connection,codigoUnidadRegistro,numeroAsiento,ejercicioAsiento);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            GeneralOperations.closeConnection(connection);
        }        
        return aux;
    }
        
    
    /**
     * Recupera la información de registro de una anotación del REC
     * @param registroVO: Datos del Registro 
     * @throws java.lang.Exception si ocurre algo grave
     */
    public void getInfoAsientoConsulta(RegistroValueObject registroVO) throws Exception{
        Connection connection = null;
        try{
            connection = this.getConnection();
            RecDAO dao = RecDAO.getInstance();
            dao.getInfoAsientoConsulta(connection, registroVO);                             
            registroVO.setUorCodVisible("1");
            registroVO.setTipoRegOrigen("REC");
            registroVO.setNomUniDestinoOrd("SUBDIRECCIÓN GENERAL DE SIMPLIFICACIÓN ADMINISTRATIVA");
        }
        catch(Exception e){
          e.printStackTrace();
          throw e;
        }
        finally{
            GeneralOperations.closeConnection(connection);
        }
    }
    
    /**
     * Recupera los documentos asociados a una anotación del REC
     * @param numeroAnotacion
     * @return
     */
    public DocumentoValueObject[] getDocumentosAnotacion(String numeroAnotacion) throws Exception
    {
        Connection connection = null;
        DocumentoValueObject[] salida = null;
        try{
            RecDAO dao = RecDAO.getInstance();
            return dao.getDocumentosAnotacion(connection, numeroAnotacion);
        }
        catch(Exception e){
          e.printStackTrace();
          throw e;
        }
        finally{
            GeneralOperations.closeConnection(connection);
        }
    }
   
 
    
    private void iniciarExpedienteAsiento(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws Exception{               
            log.debug("iniciarExpedienteAsiento init");            
            

            log.debug("iniciarExpedienteAsiento end");
    }       
    
    
    public void cambiarEstadoAsiento(String numeroAnotacion,int estado,int codUor,String nombreUor,String codProcedimiento) throws Exception{
        log.debug("RecFacade.cambiarEstadoAsiento init");
        log.debug("RecFacade.cambiarEstadoAsiento numeroAnotacion: " + numeroAnotacion);
        log.debug("RecFacade.cambiarEstadoAsiento estado: " + estado);
        log.debug("RecFacade.cambiarEstadoAsiento codUor: " + codUor);
        log.debug("RecFacade.cambiarEstadoAsiento nombreUor: " + nombreUor);
        log.debug("RecFacade.cambiarEstadoAsiento codProcedimiento: " + codProcedimiento);
        
        Connection connection = null;
        try{
            connection = this.getConnection();
            RecDAO dao = RecDAO.getInstance();
            dao.cambiarEstadoAsiento(connection,numeroAnotacion,estado,codUor,nombreUor,codProcedimiento);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            GeneralOperations.closeConnection(connection);
        }
    }


     /**
     * Recupera un determinado documento asociado a una anotación del REC
     * @param registroVO: Datos del Registro
     * @param codDocumento: Código/Identificador del documento
     * @throws java.lang.Exception si ocurre algo grave
     */
    public DocumentoValueObject getDocumentoAnotacion(RegistroValueObject registroVO,int codDocumento) throws Exception{
        Connection connection = null;
        try{
            log.debug("RecFacade.getDocumento codProcedimiento: " + codDocumento);
            connection = this.getConnection();
            RecDAO dao = RecDAO.getInstance();
            ///
            String periodo = String.valueOf(registroVO.getAnoReg());
            String numero = String.valueOf(registroVO.getNumReg());
            String tipo = String.valueOf(registroVO.getTipoReg());
            String uor = String.valueOf(registroVO.getUnidadOrgan());
            int organizacion = registroVO.getIdOrganizacion();

            StringBuffer sb = new StringBuffer();
            if(numero.length()<=6){
                for(int i =0;i<LONGITUD_MAXIMA_NUMANOTACIONREC-numero.length();i++){
                    sb.append("0");
                }
            }
            sb.append(numero);
            
            //Config config = ConfigServiceHelper.getConfig("common");
            //uor = config.getString("uor/REC/" + uor);
            ResourceBundle bundle = ResourceBundle.getBundle("es.altia.agora.webservice.registro.rec.configuracion.configuracion");
            uor = bundle.getString("uor/REC/" + uor);

            log.debug("RecDAO.getInfoAsientoConsulta uor recuperada de configuración: " + uor);
            String numeroAnotacionRec = periodo + uor + sb.toString();
            log.debug("getInfoAsientoConsulta numeroAnotacionRec a buscar: " + numeroAnotacionRec);
            ///
            return dao.getDocumentoAnotacion(connection, codDocumento,numeroAnotacionRec);

        }
        catch(Exception e){
          e.printStackTrace();
          throw e;
        }
        finally{
            GeneralOperations.closeConnection(connection);
        }
    }
    
}
