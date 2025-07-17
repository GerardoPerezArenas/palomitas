package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.manual.DocumentoDAO;
import es.altia.agora.business.sge.persistence.manual.ExpedienteOtroDocumentoDAO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class ExpedienteOtroDocumentoManager {

    private static ExpedienteOtroDocumentoManager instance  =null;
    private Logger log = Logger.getLogger(ExpedienteOtroDocumentoManager.class);

    private ExpedienteOtroDocumentoManager(){
    }

    public static ExpedienteOtroDocumentoManager getInstance(){
        if(instance==null)
            instance = new ExpedienteOtroDocumentoManager();

        return instance;
    }


    /**
     * Operación mediante la cual se da de alta un documento externo en base de datos
     * @param expedienteOtroDocumentoVO: Contenido del documento externo a dar de alta
     * @param params: String[] con los parámetros de conexión a la base de datos
     * @return Código del documento insertado o -1 si ha ocurrido algún error
     */
     public int altaDocumento(ExpedienteOtroDocumentoVO expedienteOtroDocumentoVO, String[] params){
        Connection con = null;
        int resultado = -1;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
         try {             
             con = adapt.getConnection();
             adapt.inicioTransaccion(con);
             resultado = ExpedienteOtroDocumentoDAO.getInstance().altaDocumento(expedienteOtroDocumentoVO, con);
             adapt.finTransaccion(con);

         }catch(Exception e){
             e.printStackTrace();
             log.error(e.getMessage());
             try{
                adapt.rollBack(con);
             }catch(Exception ee){ }
             
         }finally{
             try{
                 if(con!=null) con.close();
             }catch(SQLException e){
                 e.printStackTrace();
             }
         }

         return resultado;
     }


     /**
      * Elimina un documento externo determinado asociado a un expediente
      * @param municipio: Código del municipio
      * @param ejercicio: Ejercicio
      * @param numeroExpediente: Nº del expediente
      * @param codDocumento: Código del documento a eliminar
      * @param params: Parámetros de conexión a la BBDD
      * @return Un boolean
      */
    public boolean eliminaDocumento(String municipio, String ejercicio, String numeroExpediente,String codDocumento,String[] params) {
        boolean exito = false;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con = null;
        try{
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            exito = ExpedienteOtroDocumentoDAO.getInstance().eliminaDocumento(municipio, ejercicio, numeroExpediente, codDocumento, con);
            
            if (exito) {
            adapt.finTransaccion(con);
            } else {
                adapt.rollBack(con);
            }
        }catch(Exception e){
            e.printStackTrace();
            try{
                adapt.rollBack(con);
            }catch(Exception ee){
                ee.printStackTrace();
            }
        }finally{
            try{
                 if(con!=null) con.close();
             }catch(SQLException e){
                 e.printStackTrace();
             }
        }
        return exito;
    }

    /**
     * Anade el documento CSV en la base de datos sustituyendo el anterior
     *
     * @param expedienteOtroDocumentoVO Documento que se quiere modificar
     * @param metadatos Metadatos CSV del documento
     * @param params: Parámetros de conexión a la BBDD
     * @return Un boolean
     */
    public boolean modificaDocumentoCSV(ExpedienteOtroDocumentoVO expedienteOtroDocumentoVO, MetadatosDocumentoVO metadatos, String[] params) {
        boolean exito = false;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con = null;
        ExpedienteOtroDocumentoDAO expOtroDocumentoDao = ExpedienteOtroDocumentoDAO.getInstance();
        DocumentoDAO documentoDao = DocumentoDAO.getInstance();

        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            // Se inserta el fichero en base de datos
            exito = expOtroDocumentoDao.modificaDocumentoFichero(expedienteOtroDocumentoVO, con);

            // Se actualiza la tabla de metadatos
            if (exito) {
                exito = false;
                
                // Insertamos los datos en la tabla de metadatos
                Long idMetadato = documentoDao.insertarMetadatoCSV(metadatos, con, params);
                
                if (idMetadato != null) {
                    // Insertamos, si fuese necesario, la referencia a la tabla de metadatos en la tabla de documentos
                    if (!idMetadato.equals(expedienteOtroDocumentoVO.getIdMetadato())) {
                        expedienteOtroDocumentoVO.setIdMetadato(idMetadato);
                        exito = expOtroDocumentoDao.actualizarIdMetadato(expedienteOtroDocumentoVO, con);
                    }
                } else {
                    log.error("No se ha insertado correctamente el metadato");
                }
            }
            
            if (exito) {
                adapt.finTransaccion(con);
            } else {
                adapt.rollBack(con);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                adapt.rollBack(con);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }

    /**
     * Recupera un documento externo asociado a un expediente
     * @param codDocumento: Código del documento
     * @param ejercicio: Ejercicio
     * @param codMunicipio: Código del municipio
     * @param numeroExpediente: Nº. del expediente
     * @param params: Parámetros de conexión a la BD
     * @return ExpedienteOtroDocumentoVO
     */
       public ExpedienteOtroDocumentoVO getDocumento(String codDocumento,String ejercicio,String codMunicipio, 
              String numeroExpediente, boolean expHistorico, String[] params){
          ExpedienteOtroDocumentoVO doc = new ExpedienteOtroDocumentoVO();
          Connection con = null;
          try{
              AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
              con = adapt.getConnection();
              doc = ExpedienteOtroDocumentoDAO.getInstance().getDocumento(codDocumento, ejercicio, 
                      codMunicipio, numeroExpediente, expHistorico, con);
          }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                 if(con!=null) con.close();
             }catch(SQLException e){
                 e.printStackTrace();
             }
        }
          return doc;
      }

    /**
     * Recupera un documento externo asociado a un expediente sin contenido del fichero
     *
     * @param codDocumento: Código del documento
     * @param ejercicio: Ejercicio
     * @param codMunicipio: Código del municipio
     * @param numeroExpediente: Nº. del expediente
     * @param params: Parámetros de conexión a la BD
     * @return ExpedienteOtroDocumentoVO
     */
    public ExpedienteOtroDocumentoVO getDocumentoSinFichero(String codDocumento, String ejercicio, String codMunicipio,
            String numeroExpediente, boolean expHistorico, String[] params) {
        ExpedienteOtroDocumentoVO doc = new ExpedienteOtroDocumentoVO();
        Connection con = null;
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            doc = ExpedienteOtroDocumentoDAO.getInstance().getDocumentoSinFichero(codDocumento, ejercicio,
                    codMunicipio, numeroExpediente, expHistorico, con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return doc;
    }
       
    /**
     * Recupera el id de los metadatos del documento externo
     *
     * @param codMunicipio: codigo del municipio
     * @param ejercicio: ejercicio
     * @param numeroExpediente: nº. del expediente
     * @param codDocumento: codigo del documento
     * @param params: parametros de conexion a la BD
     * @return Id del metadato
     * @throws es.altia.common.exception.TechnicalException
     */
    public Long getDocumentoIdMetadato(String codMunicipio, String ejercicio, String numeroExpediente, String codDocumento, String[] params) 
            throws TechnicalException {
        Long id = null;
        Connection con = null;
        
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            id = ExpedienteOtroDocumentoDAO.getInstance().getDocumentoIdMetadato(codMunicipio, ejercicio,
                    numeroExpediente, codDocumento, con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
 
     public boolean existeDocumentoMetadatoCSV(String codDocumento, String[] params) {
        boolean existe = false;

        try {
            existe = DocumentoManager.getInstance().existeMetadatoCSV(
                    params, ConstantesDatos.SUBCONSULTA_E_EXT_DOC_ID_METADATO_CODIGO_INTERNO, codDocumento);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return existe;
    }
}
