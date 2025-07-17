package es.altia.agora.business.sge.persistence.manual;


import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.DateOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.jdbc.JdbcOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

/**
 * Operaciones de manejo de documento externos contra la base de datos
 * @author manuel.bahamonde
 */

public class ExpedienteOtroDocumentoDAO {

    //Para el fichero de configuracion tecnico.
    protected static Config conf;
    //Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(ExpedienteOtroDocumentoDAO.class.getName());
    
    private static ExpedienteOtroDocumentoDAO instance = null;

    protected ExpedienteOtroDocumentoDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        conf = ConfigServiceHelper.getConfig("techserver");
    }

    public static ExpedienteOtroDocumentoDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        synchronized (ExpedienteOtroDocumentoDAO.class) {
            if (instance == null) {
                instance = new ExpedienteOtroDocumentoDAO();
            }
        }
        return instance;
    }

  

    /**
    Devuelve toda la informacion de un Documento asociado a un determinado expediente, asi como su contenido
     * @param codDocumento Codigo interno del documento
     * @param params Parametros de conexion a la base de datos
     * @return Booleano inidicando si existe o no el documento
     */
    public ExpedienteOtroDocumentoVO obtenerDocumento(String codDocumento, String[] params) {

        ExpedienteOtroDocumentoVO expedienteOtroDocumentoVO = new ExpedienteOtroDocumentoVO();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        byte[] r = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt;
            String sql = "";


            sql = " SELECT DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_COD, DOC_EXT_NOM," + abd.convertir("DOC_EXT_FAL", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA, DOC_EXT_FIL, DOC_EXT_TIP "
                    + " FROM E_DOC_EXT WHERE DOC_EXT_COD=? ";
            stmt = conexion.prepareStatement(sql);
            stmt.setString(1, codDocumento);
            rs = stmt.executeQuery();


            while (rs.next()) {
                expedienteOtroDocumentoVO.setMunicipio(rs.getString("DOC_EXT_MUN"));
                expedienteOtroDocumentoVO.setEjercicio(rs.getString("DOC_EXT_EJE"));
                expedienteOtroDocumentoVO.setNumeroExpediente(rs.getString("DOC_EXT_NUM"));
                expedienteOtroDocumentoVO.setCodigoDocumento(rs.getString("DOC_EXT_COD"));
                expedienteOtroDocumentoVO.setNombreDocumento(rs.getString("DOC_EXT_NOM"));
                expedienteOtroDocumentoVO.setFechaAltaDocuemento(rs.getString(6));
                expedienteOtroDocumentoVO.setTipoDocumento(rs.getString("DOC_EXT_TIP"));

                java.io.InputStream st = rs.getBinaryStream("DOC_EXT_FIL");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (st != null) {
                     while ((c = st.read()) != -1) {
                         ot.write(c);
                     }
                }
                ot.flush();
                r = ot.toByteArray();
                ot.close();
                st.close();
                expedienteOtroDocumentoVO.setContenidoDocumento(r);

            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Exception capturada en: " + getClass().getName());
            }
            expedienteOtroDocumentoVO = null;

        } finally {
            try {
                abd.devolverConexion(conexion);
            } catch (Exception bde) {
                bde.getMessage();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Exception capturada en: " + getClass().getName());
                }
            }
            return expedienteOtroDocumentoVO;
        }
    }
    

    /**
     Obtiene la lista de los documentos asociados a un determinado expediente
    * @param numeroExpediente Numero del expediente
    * @param params Parametros de conexion a la base de datos
    * @return Booleano inidicando si existe o no el documento
    */
    public ArrayList<ExpedienteOtroDocumentoVO> listaOtrosDocumentosExpediente(String municipio, String ejercicio, String numeroExpediente, String[] params) {
        ArrayList<ExpedienteOtroDocumentoVO> listaOtrosDocumentosExpediente = new ArrayList<ExpedienteOtroDocumentoVO>();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        byte[] r = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt;
            String sql = "";

            sql =" SELECT DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_COD, DOC_EXT_NOM,DOC_EXT_FAL, DOC_EXT_TIP,DOC_EXT_EXT " +
                    " FROM E_DOC_EXT WHERE  DOC_EXT_MUN=? AND DOC_EXT_EJE=? AND DOC_EXT_NUM=? ";
            stmt = conexion.prepareStatement(sql);
            stmt.setString(1, municipio);
            stmt.setString(2, ejercicio);
            stmt.setString(3, numeroExpediente);
            rs = stmt.executeQuery();

             while (rs.next()) {
                ExpedienteOtroDocumentoVO expOtroDocVO = new ExpedienteOtroDocumentoVO();
                expOtroDocVO.setMunicipio(rs.getString("DOC_EXT_NUM"));
                expOtroDocVO.setEjercicio(rs.getString("DOC_EXT_EJE"));
                expOtroDocVO.setNumeroExpediente(rs.getString("DOC_EXT_NUM"));
                expOtroDocVO.setCodigoDocumento(rs.getString("DOC_EXT_COD"));
                expOtroDocVO.setNombreDocumento(rs.getString("DOC_EXT_NOM"));
                expOtroDocVO.setFechaAltaDocuemento(DateOperations.extraerFechaTimeStamp(rs.getTimestamp("DOC_EXT_FAL")));
                expOtroDocVO.setTipoDocumento(rs.getString("DOC_EXT_TIP"));
                expOtroDocVO.setExtension(rs.getString("DOC_EXT_EXT"));
               listaOtrosDocumentosExpediente.add(expOtroDocVO);
            }
            rs.close();
            stmt.close();


        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Exception capturada en: " + getClass().getName());
            }

        } finally { 
            try {
                abd.devolverConexion(conexion);
            } catch (Exception bde) {
                bde.getMessage();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Exception capturada en: " + getClass().getName());
                }
            }
            return listaOtrosDocumentosExpediente;
        }
    }


    
    
    
    public ArrayList<ExpedienteOtroDocumentoVO> listaOtrosDocumentosExpediente(String municipio,String ejercicio,String numeroExpediente,String expHistorico,Connection con) {
        ArrayList<ExpedienteOtroDocumentoVO> listaOtrosDocumentosExpediente = new ArrayList<ExpedienteOtroDocumentoVO>();
        ResultSet rs = null;
        PreparedStatement stmt = null;        
        
        try {
                        
            String sql = "";
            if ("true".equals(expHistorico))
                sql =" SELECT DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_COD, DOC_EXT_NOM,DOC_EXT_FAL, DOC_EXT_TIP,DOC_EXT_EXT " +
                        " FROM HIST_E_DOC_EXT WHERE  DOC_EXT_MUN=? AND DOC_EXT_EJE=? AND DOC_EXT_NUM=? ORDER BY DOC_EXT_FAL DESC, DOC_EXT_NOM ASC";
            else
                sql =" SELECT DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_COD, DOC_EXT_NOM,DOC_EXT_FAL, DOC_EXT_TIP,DOC_EXT_EXT " +
                        " FROM E_DOC_EXT WHERE  DOC_EXT_MUN=? AND DOC_EXT_EJE=? AND DOC_EXT_NUM=? ORDER BY DOC_EXT_FAL DESC, DOC_EXT_NOM ASC";

            stmt = con.prepareStatement(sql);
            stmt.setString(1, municipio);
            stmt.setString(2, ejercicio);
            stmt.setString(3, numeroExpediente);
            rs = stmt.executeQuery();

             while (rs.next()) {
                ExpedienteOtroDocumentoVO expOtroDocVO = new ExpedienteOtroDocumentoVO();
                expOtroDocVO.setMunicipio(municipio);
                expOtroDocVO.setEjercicio(rs.getString("DOC_EXT_EJE"));
                expOtroDocVO.setNumeroExpediente(rs.getString("DOC_EXT_NUM"));
                expOtroDocVO.setCodigoDocumento(rs.getString("DOC_EXT_COD"));
                expOtroDocVO.setNombreDocumento(rs.getString("DOC_EXT_NOM"));
                expOtroDocVO.setFechaAltaDocuemento(DateOperations.extraerFechaTimeStamp(rs.getTimestamp("DOC_EXT_FAL")));
                expOtroDocVO.setTipoDocumento(rs.getString("DOC_EXT_TIP"));
                expOtroDocVO.setExtension(rs.getString("DOC_EXT_EXT"));
               listaOtrosDocumentosExpediente.add(expOtroDocVO);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Exception capturada en: " + getClass().getName());
            }

        } finally {
            try {
                
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
                
            } catch (Exception bde) {
                bde.getMessage();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Exception capturada en: " + getClass().getName());
                }
            }
            return listaOtrosDocumentosExpediente;
        }
    }
    
    
    
    
    
    
  

     
    /**
     Comprueba si existe un documento asociado al expediente
    * @param numeroExpediente Numero del expediente
    * @param codDocumento Codigo interno del documento
    * @param params Parametros de conexion a la base de datos
    * @return Booleano inidicando si existe o no el documento
    */
    public boolean existeDocumento(String municipio, String ejercicio, String numeroExpediente, String codDocumento, String[] params) {

        boolean existeDoc = false;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt;
            String sql = "";

            sql = " SELECT  DOC_EXT_COD FROM E_DOC_EXT WHERE DOC_EXT_MUN =? AND DOC_EXT_EJE =? AND DOC_EXT_NUM = ? AND DOC_EXT_COD=?";

            stmt = conexion.prepareStatement(sql);

            stmt.setString(1, municipio);
            stmt.setString(2, ejercicio);
            stmt.setString(3, numeroExpediente);
            stmt.setString(4, codDocumento);
            rs = stmt.executeQuery();
            existeDoc = rs.next();

            stmt.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Exception capturada en: " + getClass().getName());
            }
            existeDoc = false;
        } finally {
            try {
                abd.devolverConexion(conexion);
            } catch (Exception bde) {
                bde.getMessage();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Exception capturada en: " + getClass().getName());
                }
            }
            return existeDoc;
        }
    }



 /**
     Da de alta un nuevo documento externo asociado a un determinado expediente
    * @param expedienteOtroDocumentoVO Documento que se quiere asociar  a un determinado expediente
    * @param con: Conexión a la base de datos
    * @return Código del documento recien insertado o -1 si no se ha podido insertar
    */
    public int altaDocumentoAntiguo(ExpedienteOtroDocumentoVO expedienteOtroDocumentoVO, Connection con){
        int resultado = -1;
        byte[] ficheroWord = expedienteOtroDocumentoVO.getContenidoDocumento();
        
        try{
                        
            String sql = "";
            Statement stmt1;
            PreparedStatement stmt2;
            ResultSet rs = null;

            // obtenemos el codigo interno del documento
            int codigoDocumento=1;
            sql=" SELECT MAX(DOC_EXT_COD) FROM E_DOC_EXT ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt1 = con.createStatement();
            rs = stmt1.executeQuery(sql);
            if (rs.next()) {
                codigoDocumento = rs.getInt(1)+1;
            }
            rs.close();
            stmt1.close();
            
            m_Log.debug("-> Siguiente codigo Documento : " + codigoDocumento);
             
            sql = "INSERT INTO E_DOC_EXT(DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_COD, DOC_EXT_NOM, DOC_EXT_FAL, DOC_EXT_FIL, DOC_EXT_TIP, DOC_EXT_EXT) VALUES (?,?,?,?,?,?,?,?,?)";
                    
            stmt2 = con.prepareStatement(sql);
            // metemos los parametros de la consulta
            stmt2.setString(1, expedienteOtroDocumentoVO.getMunicipio());
            stmt2.setString(2, expedienteOtroDocumentoVO.getEjercicio());
            stmt2.setString(3, expedienteOtroDocumentoVO.getNumeroExpediente());
            stmt2.setString(4, String.valueOf(codigoDocumento));
            stmt2.setString(5, String.valueOf(expedienteOtroDocumentoVO.getNombreDocumento()));
            stmt2.setTimestamp(6, DateOperations.toSQLTimestamp(Calendar.getInstance()));
            if(ficheroWord!=null && ficheroWord.length>=1){
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt2.setBinaryStream(7, st, ficheroWord.length);
            }else{
                // Si no hay fichero se guarda un null en el campo doc_ext_fil. Esto sólo se debe dar cuando se ha configurado FLEXIA para que almacene los documentos adjuntos en un gestor documental,
                // de modo que se guarda en base de datos una referencia al documento pero su contenido estará en el gestor.
                stmt2.setNull(7,java.sql.Types.BINARY);
            }
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            stmt2.setString(8, expedienteOtroDocumentoVO.getTipoDocumento());
            stmt2.setString(9,expedienteOtroDocumentoVO.getExtension());
            int rowsInserted = stmt2.executeUpdate();
            m_Log.debug("Nº de documentos externos insertados: " + rowsInserted);
            stmt2.close();
            // Se devolverá el código del documento recien insertado
            resultado = codigoDocumento;

        }catch (Exception ex){            
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("altaDocumento: " + getClass().getName());
        }
        return resultado;
    } 
    
    /**
     * Actualiza el documento
     * 
     * @param expedienteOtroDocumentoVO
     * @param con
     * @return 
     */
    public boolean modificaDocumentoFichero(ExpedienteOtroDocumentoVO expedienteOtroDocumentoVO, Connection con) {
        boolean correcto = false;
        PreparedStatement ps = null;
    
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE E_DOC_EXT ")
               .append(" SET DOC_EXT_NOM = ?, ")
               .append(" DOC_EXT_FAL = ?, ")
               .append(" DOC_EXT_FIL = ?, ")
               .append(" DOC_EXT_TIP = ?, ")
               .append(" DOC_EXT_EXT = ? ")
               .append(" WHERE DOC_EXT_COD = ? ");

            Timestamp fechaActual = DateOperations.toSQLTimestamp(Calendar.getInstance());
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug("PARAMS:");
                m_Log.debug(String.format("DOC_EXT_NOM: %s", expedienteOtroDocumentoVO.getNombreDocumento()));
                m_Log.debug(String.format("DOC_EXT_FAL: %s", fechaActual));
                m_Log.debug(String.format("DOC_EXT_FIL is null?: %b", expedienteOtroDocumentoVO.getContenidoDocumento() == null));
                m_Log.debug(String.format("DOC_EXT_TIP: %s", expedienteOtroDocumentoVO.getTipoDocumento()));
                m_Log.debug(String.format("DOC_EXT_EXT: %s", expedienteOtroDocumentoVO.getExtension()));
                m_Log.debug(String.format("DOC_EXT_COD: %s", expedienteOtroDocumentoVO.getCodigoDocumento()));
            }
            
            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    expedienteOtroDocumentoVO.getNombreDocumento(),
                    fechaActual,
                    expedienteOtroDocumentoVO.getContenidoDocumento(),
                    expedienteOtroDocumentoVO.getTipoDocumento(),
                    expedienteOtroDocumentoVO.getExtension(),
                    expedienteOtroDocumentoVO.getCodigoDocumento());

            int rowsActualizados = ps.executeUpdate();
            m_Log.debug(" num documentos externos actualizados: " + rowsActualizados);

            if (rowsActualizados == 1) {
                correcto = true;
            } else {
                m_Log.error("Se actualizaron mas filas de las esperadas");
            }
        } catch (Exception ex) {
            m_Log.error("Error al intentar actualizar el fichero del documento", ex);
            ex.printStackTrace();
        } finally {
            try {
                SigpGeneralOperations.closeStatement(ps);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return correcto;
    }
    
    
    public int altaDocumento(ExpedienteOtroDocumentoVO expedienteOtroDocumentoVO, Connection con){
        int resultado = -1;
        byte[] ficheroWord = expedienteOtroDocumentoVO.getContenidoDocumento();
        Statement stmt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
                        
            String sql = "";
            
            ResourceBundle configTechserver = ResourceBundle.getBundle("techserver");            
            String gestor = configTechserver.getString("CON.gestor");
            
            if(gestor.equalsIgnoreCase(ConstantesDatos.ORACLE)) {
                // En la consulta para oracle, el valor para el campo DOC_EXT_COD se asigna por medio de una secuencia de oracle
                sql = "INSERT INTO E_DOC_EXT(DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_COD, DOC_EXT_NOM, DOC_EXT_FAL, DOC_EXT_FIL, DOC_EXT_TIP, DOC_EXT_EXT) VALUES (?,?,?,SEQ_E_DOC_EXT.nextval,?,?,?,?,?)";
            }
            
            if(gestor.equalsIgnoreCase(ConstantesDatos.SQLSERVER)) {
                // En la consulta para sqlserver, el valor para el campo DOC_EXT_COD lo asigna la BBDD porque es un campo IDENTITY
                //sql = "INSERT INTO E_DOC_EXT(DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_NOM, DOC_EXT_FAL, DOC_EXT_FIL, DOC_EXT_TIP, DOC_EXT_EXT) VALUES (?,?,?,?,?,?,?,?)";
                return altaDocumentoAntiguo( expedienteOtroDocumentoVO,  con);
                
            }
                    
            ps = con.prepareStatement(sql);
            // metemos los parametros de la consulta
            ps.setString(1, expedienteOtroDocumentoVO.getMunicipio());
            ps.setString(2, expedienteOtroDocumentoVO.getEjercicio());
            ps.setString(3, expedienteOtroDocumentoVO.getNumeroExpediente());
            //stmt2.setString(4, String.valueOf(codigoDocumento));
            ps.setString(4, String.valueOf(expedienteOtroDocumentoVO.getNombreDocumento()));
            ps.setTimestamp(5, DateOperations.toSQLTimestamp(Calendar.getInstance()));
            if(ficheroWord!=null && ficheroWord.length>=1){
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                ps.setBinaryStream(6, st, ficheroWord.length);
            }else{
                // Si no hay fichero se guarda un null en el campo doc_ext_fil. Esto sólo se debe dar cuando se ha configurado FLEXIA para que almacene los documentos adjuntos en un gestor documental,
                // de modo que se guarda en base de datos una referencia al documento pero su contenido estará en el gestor.
                ps.setNull(6,java.sql.Types.BINARY);
            }
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            ps.setString(7, expedienteOtroDocumentoVO.getTipoDocumento());
            ps.setString(8,expedienteOtroDocumentoVO.getExtension());
            int rowsInserted = ps.executeUpdate();
            m_Log.debug("Nº de documentos externos insertados: " + rowsInserted);
            ps.close();
            
            
            // Se recupera el código del documento
            sql = "SELECT MAX(DOC_EXT_COD) AS NUM FROM E_DOC_EXT";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                resultado = rs.getInt("NUM");
            }
            
        }catch (Exception ex){            
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("altaDocumento: " + getClass().getName());
        } finally { 
            try{
                if(ps!=null) ps.close();
                if(stmt!=null) stmt.close();
                
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return resultado;
    }



  /**
    Elimina todos los documentos asociados y metadatos a un determinado expediente
     * @param municipio Municipio del expediente
     * @param ejercicio Ejercicio del expediente
     * @param numeroExpediente Numero del expediente
     * @param codDocumento: Código del documento
     * @param params Parametros de conexion a la base de datos
     * @return True si se ha eliminado y false en caso contrario
     */
    public boolean eliminaDocumento(String municipio, String ejercicio, String numeroExpediente,String codDocumento,Connection con) {
        boolean exito = false;
        PreparedStatement stmt=null;

        try {                        
            Long idMetadato = getDocumentoIdMetadato(municipio, ejercicio, numeroExpediente, codDocumento, con);
            String sql = " DELETE FROM E_DOC_EXT WHERE DOC_EXT_MUN=?  AND DOC_EXT_EJE=? AND DOC_EXT_NUM=? AND DOC_EXT_COD=?";
            stmt = con.prepareStatement(sql);

            int i=1;
            stmt.setInt(i++,Integer.parseInt(municipio));
            stmt.setString(i++, ejercicio);
            stmt.setString(i++, numeroExpediente);
            stmt.setLong(i++, Long.parseLong(codDocumento));
            int resultado = stmt.executeUpdate();
            m_Log.debug("eliminaDocumentoExpediente nº de documentos eliminados: " + resultado);
            stmt.close();
            
            // Eliminar metadatos asociados
            if (idMetadato != null) {
                DocumentoDAO.getInstance().eliminarMetadato(idMetadato, con);
            }
            exito = true;

        } catch (Exception ex) {                        
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(ex.getMessage());
            }
        } finally {
            try {
               if(stmt!=null) stmt.close();
            } catch (Exception bde) {
                bde.getMessage();                
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Exception capturada en: " + getClass().getName());
                }
            }          
        }

          return exito;
    }

 /**
     * Recupera un documento externo asociado a un expediente
     *
     * @param codDocumento: C\F3digo del documento
     * @param ejercicio: Ejercicio
     * @param codMunicipio: C\F3digo del municipio
     * @param numeroExpediente Numero del expediente
     * @param con: Conexi\F3n a la base de datos
     * @return ExpedienteOtroDocumentoVO: Objeto con los datos del documento
     */
    public ExpedienteOtroDocumentoVO getDocumento(String codDocumento, String ejercicio, String codMunicipio,
            String numeroExpediente, boolean expHistorico, Connection con) {

        return getDocumento(codDocumento, ejercicio, codMunicipio, numeroExpediente, expHistorico, true, con);
    }


    /**
     * Recupera un documento externo asociado a un expediente
     * @param codDocumento: Código del documento
     * @param ejercicio: Ejercicio
     * @param codMunicipio: Código del municipio
    * @param numeroExpediente Numero del expediente
    * @param con: Conexión a la base de datos
    * @return ExpedienteOtroDocumentoVO: Objeto con los datos del documento
    */
   private ExpedienteOtroDocumentoVO getDocumento(String codDocumento,String ejercicio,String codMunicipio, 
            String numeroExpediente,  boolean expHistorico,  boolean conContenidoFichero,Connection con) {
        ExpedienteOtroDocumentoVO documento = new ExpedienteOtroDocumentoVO();
        byte[] r = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {            
             StringBuilder sql = new StringBuilder();

            if (conContenidoFichero) {
                sql.append("SELECT DOC_EXT_NOM, DOC_EXT_TIP, DOC_EXT_FIL, DOC_EXT_FAL, DOC_EXT_EXT ");
            } else {
                sql.append("SELECT DOC_EXT_NOM, DOC_EXT_TIP, DOC_EXT_FAL, DOC_EXT_EXT ");
            }
            
            if (expHistorico) {
                sql.append("FROM HIST_E_DOC_EXT ");
            } else {
                sql.append("FROM E_DOC_EXT ");
            }

            sql.append("WHERE  DOC_EXT_MUN=? AND DOC_EXT_EJE=? AND DOC_EXT_NUM=? AND DOC_EXT_COD=? ");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql.toString()));
            }
            
            stmt = con.prepareStatement(sql.toString());
            int i=1;
            stmt.setLong(i++,Long.parseLong(codMunicipio));
            stmt.setLong(i++, Long.parseLong(ejercicio));
            stmt.setString(i++, numeroExpediente);
            stmt.setLong(i++, Long.parseLong(codDocumento));
            
            rs = stmt.executeQuery();

           while (rs.next()) {
                if (conContenidoFichero) {
                java.io.InputStream st = rs.getBinaryStream("DOC_EXT_FIL");
                    if (st != null) {
                        m_Log.debug("InputStream !=null");
                    } else {
                        m_Log.debug("InputStream ==null");
                    }

                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                    if (ot != null) {
                        m_Log.debug("ot!=null");
                    } else {
                        m_Log.debug("ot==null");
                    }
                int c;
                if (st != null) {
                     while ((c = st.read()) != -1) {
                         ot.write(c);
                     }
                }
                ot.flush();
                r = ot.toByteArray();
                ot.close();
                st.close();
                documento.setContenidoDocumento(r);
                }

                documento.setMunicipio(codMunicipio);
                documento.setEjercicio(ejercicio);
                documento.setNumeroExpediente(numeroExpediente);
                documento.setCodigoDocumento(codDocumento);
                documento.setNombreDocumento(rs.getString("DOC_EXT_NOM"));
                documento.setFechaAltaDocuemento(DateOperations.extraerFechaTimeStamp(rs.getTimestamp("DOC_EXT_FAL")));
                documento.setTipoDocumento(rs.getString("DOC_EXT_TIP"));
                documento.setExtension(rs.getString("DOC_EXT_EXT"));

             
              
            }
           
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Exception capturada en: " + getClass().getName());
            }

        } finally {
            try {
                    if(rs!=null) rs.close();
                    if(stmt!=null) stmt.close();
            } catch (Exception bde) {
                bde.getMessage();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Exception capturada en: " + getClass().getName());
                }
            }
            return documento;
        }
    }

   
   
       /**
     * Recupera un documento externo asociado a un expediente
     *
     * @param codDocumento: C\F3digo del documento
     * @param ejercicio: Ejercicio
     * @param codMunicipio: C\F3digo del municipio
     * @param numeroExpediente Numero del expediente
     * @param expHistorico Si tiene que buscarlo en el historico
     * @param con: Conexi\F3n a la base de datos
     * @return ExpedienteOtroDocumentoVO: Objeto con los datos del documento
     */
    public ExpedienteOtroDocumentoVO getDocumentoSinFichero(String codDocumento, String ejercicio, String codMunicipio,
            String numeroExpediente, boolean expHistorico, Connection con) {
        
        return getDocumento(codDocumento, ejercicio, codMunicipio, numeroExpediente, expHistorico, false, con);
    }
   
    /**
     * Obteniene el id de los metadatos del documento externo
     * 
     * @param municipio
     * @param ejercicio
     * @param numeroExpediente
     * @param codDocumento
     * @param con
     * @return 
     * @throws es.altia.common.exception.TechnicalException 
     */
    public Long getDocumentoIdMetadato(String municipio, String ejercicio, String numeroExpediente, String codDocumento, Connection con)
            throws TechnicalException {
        Long idMetadato = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DOC_EXT_ID_METADATO ")
               .append("FROM E_DOC_EXT ")
               .append("WHERE DOC_EXT_MUN = ? AND DOC_EXT_EJE = ? AND DOC_EXT_NUM = ? AND DOC_EXT_COD = ?");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: DOC_EXT_MUN=%s AND DOC_EXT_EJE=%s AND DOC_EXT_NUM=%s AND DOC_EXT_COD=%s, ",
                        municipio, ejercicio, numeroExpediente, codDocumento));
            }

            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    municipio,
                    ejercicio,
                    numeroExpediente,
                    codDocumento);

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                idMetadato = JdbcOperations.getLongFromResultSet(resultSet, "DOC_EXT_ID_METADATO");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al intentar obtener el campo ID_METADATO del documento externo", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(resultSet);
            SigpGeneralOperations.closeStatement(ps);
        }

        return idMetadato;
    }

    public boolean actualizarIdMetadato(ExpedienteOtroDocumentoVO expedienteOtroDocumentoVO, Connection con)
           throws TechnicalException {
        boolean exito = false;
        PreparedStatement ps = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE E_DOC_EXT ")
               .append("SET DOC_EXT_ID_METADATO = ? ")
               .append("WHERE DOC_EXT_MUN = ? AND DOC_EXT_EJE = ? AND DOC_EXT_NUM = ? AND DOC_EXT_COD = ?");

            Long idMetadato = expedienteOtroDocumentoVO.getIdMetadato();
            String municipio = expedienteOtroDocumentoVO.getMunicipio();
            String ejercicio = expedienteOtroDocumentoVO.getEjercicio();
            String numeroExpediente = expedienteOtroDocumentoVO.getNumeroExpediente();
            String codDocumento = expedienteOtroDocumentoVO.getCodigoDocumento();
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug("Params:");
                m_Log.debug(String.format("SET: DOC_EXT_ID_METADATO = %d ", idMetadato));
                m_Log.debug(String.format("WHERE: DOC_EXT_MUN=%s AND DOC_EXT_EJE=%s AND DOC_EXT_NUM=%s AND DOC_EXT_COD=%s ",
                        municipio, ejercicio, numeroExpediente, codDocumento));
            }

            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    idMetadato,
                    municipio,
                    ejercicio,
                    numeroExpediente,
                    codDocumento);

            int filasActualizadas = ps.executeUpdate();
            m_Log.debug("actualizarIdMetadato num de documentos actualizados: " + filasActualizadas);
            
            if (filasActualizadas == 1) {
                exito = true;                
            } else {
                m_Log.error("Se ha actualizado mas de una fila");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al actualizar el campo ID_METADATO del documento externo", ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }

        return exito;
    }
   



}
