package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.CSVPendienteVO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.jdbc.JdbcOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;

public class DocumentoDAO {

    protected static Config conf;
    protected static Log m_Log = LogFactory.getLog(DocumentoDAO.class.getName());
    private static DocumentoDAO instance = null;

    protected DocumentoDAO() {
        super();
        conf = ConfigServiceHelper.getConfig("techserver");
    }

    public static DocumentoDAO getInstance() {
        synchronized (DocumentoDAO.class) {
            if (instance == null) {
                instance = new DocumentoDAO();
            }
        }

        return instance;
    }

    /**
     * Inserta los metadatos de un documento en base de datos.
     *
     * @param datos objeto que contiene los datos del objeto a dar de alta
     * @param conexion conexion de base de datos
     * @param params parametros de base de datos
     * @return identificador en base de datos del metadato
     * @throws es.altia.common.exception.TechnicalException
     */
    public Long insertarMetadatoCSV(MetadatosDocumentoVO datos, Connection conexion, String[] params) throws TechnicalException {
        Long idMetadato = null;
        AdaptadorSQLBD abd = null;
        PreparedStatement stmt = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            abd = new AdaptadorSQLBD(params);
            
            idMetadato = datos.getId();
            String csv = datos.getCsv();
            String csvAplicacion = datos.getCsvAplicacion();
            String csvUri = datos.getCsvUri();
            
            // Construccion de la consulta
            if (idMetadato == null) { // Hay que realizar un insert solo si el ID viene vacio
                if (ConstantesDatos.ORACLE.equalsIgnoreCase(abd.getTipoGestor())) {
                    sql.append("INSERT INTO METADATO_DOCUMENTO (ID_METADATO, CSV, CSV_APLICACION, CSV_URI) ")
                       .append("VALUES (metadato_documento_id_metadato.NEXTVAL, ?, ?, ?)");
                } else {
                    sql.append("INSERT INTO METADATO_DOCUMENTO (CSV, CSV_APLICACION, CSV_URI) ")
                       .append("VALUES (?, ?, ?)");
                }
            } else { // Hay que realizar un update
                sql.append("UPDATE METADATO_DOCUMENTO ")
                   .append("SET CSV = ?, CSV_APLICACION =?, CSV_URI = ? ")
                   .append("WHERE ID_METADATO = ?");
            }

            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());
            int numColumna = 1;
             m_Log.debug("csv "+csv);
             m_Log.debug("csvAplicacion "+csvAplicacion);
             m_Log.debug("csvUri "+csvUri);
            JdbcOperations.setValues(stmt, numColumna,
                    csv,
                    csvAplicacion,
                    csvUri);
            numColumna += 3;
            
            if (idMetadato != null) {
                JdbcOperations.setValues(stmt, numColumna,
                        idMetadato);
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: %s", datos.toString()));
            }

            // Ejecucion de la insert
            int retorno=stmt.executeUpdate();
             m_Log.debug("Eretorno retorno "+retorno);
            SigpGeneralOperations.closeStatement(stmt);

            // Obtener el id de la fila insertada
            if (idMetadato == null) {
                idMetadato = obtenerIdMetadatoByCSV(csv, conexion);
                 m_Log.debug("idMetadato "+idMetadato);
            }
        } catch (Exception sqle) {
            idMetadato = null;
            m_Log.error("Error al insertar los metadatos en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al insertar los metadatos en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }

        return idMetadato;
    }

    /**
     * Elimina los metadatos de un documento en base de datos.
     * 
     * @param idMetadato identificador en base de datos del metadato
     * @param conexion conexion de base de datos
     * @throws es.altia.common.exception.TechnicalException
     */
    public void eliminarMetadato(Long idMetadato, Connection conexion) throws TechnicalException {
        PreparedStatement stmt = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta eliminacion
            sql.append("DELETE METADATO_DOCUMENTO ")
               .append("WHERE ID_METADATO = ?");

            // Poblar variables bind de la eliminacion
            stmt = conexion.prepareStatement(sql.toString());
            JdbcOperations.setValues(stmt, 1,
                    idMetadato);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: idMetadato=%d", idMetadato));
            }

            // Ejecucion de la eliminacion
            stmt.executeUpdate();
        } catch (Exception sqle) {
            m_Log.error("Error al insertar los metadatos en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al insertar los metadatos en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
    /**
     * Elimina los metadatos de documentos en base de datos.
     * 
     * @param idsMetadato identificadores en base de datos del metadato
     * @param conexion conexion de base de datos
     * @throws es.altia.common.exception.TechnicalException
     */
    public void eliminarMetadato(ArrayList<Long> idsMetadato, Connection conexion) throws TechnicalException {
        PreparedStatement stmt = null;

        try {
            StringBuilder sql = new StringBuilder();
            
            if (idsMetadato != null && !idsMetadato.isEmpty()) {
                // Construccion de la consulta eliminacion
                sql.append("DELETE METADATO_DOCUMENTO ")
                   .append("WHERE ID_METADATO = (");

                // Creando IN
                for (int i = 0; i < idsMetadato.size(); i++) {
                    if (i != 0) {
                        sql.append(",");
                    }
                    
                    sql.append("?");
                    
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(String.format("idsMetadato[i]: %d", idsMetadato.get(i)));
                    }
                }
                sql.append(")");
                
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(String.format("SQL: %s", sql));
                }
                
                // Poblar variables bind de la eliminacion
                stmt = conexion.prepareStatement(sql.toString());
                JdbcOperations.setValues(stmt, 1,
                        idsMetadato);

                // Ejecucion de la eliminacion
                stmt.executeUpdate();
            }
        } catch (Exception sqle) {
            m_Log.error("Error al insertar los metadatos en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al insertar los metadatos en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
    /**
     * Obtiene el id del metadato que corresponde al codigo seguro
     * de verificacion pasado por parametro
     * 
     * @param csv codigo seguro de verificacion
     * @param conexion conexion de base de datos
     * @return identificador en base de datos del metadato
     * @throws es.altia.common.exception.TechnicalException
     */
    public Long obtenerIdMetadatoByCSV(String csv, Connection conexion) throws TechnicalException {
        Long idMetadato = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        m_Log.error("CSV en botener "+csv);
        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta insert
            sql.append("SELECT ID_METADATO ")
               .append("FROM METADATO_DOCUMENTO ")
               .append("WHERE CSV = '"+csv+"'");
  
            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());
           

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: csv=", csv));
            }

            // Ejecucion de la insert
            result = stmt.executeQuery();
            result.next();
            
            idMetadato = JdbcOperations.getLongFromResultSet(result, "ID_METADATO");
        } catch (Exception sqle) {
            m_Log.error("Error al insertar los metadatos en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al insertar los metadatos en bbdd");
        } finally {
            SigpGeneralOperations.closeResultSet(result);
            SigpGeneralOperations.closeStatement(stmt);
        }

        return idMetadato;
    }

    /**
     * Busca si esta informado el campo CSV para los metadatos de un documento 
     * en base de datos. Se le debe pasar una subconsulta que retorna el ID
     * del metadato a borrar y los parametros que necesite dicha subconsulta.
     * 
     * Ejemplo:
     * subSelect: SELECT ID_METADATO FROM E_DOC_EXT WHERE DOC_EXT_COD=?
     * paramSubSelect: paramSubSelect[0] = 1
     *
     * @param subSelect subconsulta para obtener el id del metadato
     * @param paramSubSelect argumentos para la subconsulta
     * @param conexion conexion de base de datos
     * @return true si existe, false si no existe
     * @throws es.altia.common.exception.TechnicalException
     */
    public boolean existeMetadatoCSV(String subSelect, Object[] paramSubSelect, Connection conexion) throws TechnicalException {
        boolean existe = false;
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta
            sql.append(" SELECT CSV ")
               .append(" FROM METADATO_DOCUMENTO ")
               .append(" WHERE ID_METADATO = ( ")
               .append(subSelect)
               .append(" )");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
            }
            
            // Poblar variables bind de la sentencia
            stmt = conexion.prepareStatement(sql.toString());
            
            if (paramSubSelect != null) {
                m_Log.debug("Params:");
                for (int i = 0; i < paramSubSelect.length; i++) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(String.format("Valor[i]: %s", paramSubSelect[i]));
                    }
                    
                    JdbcOperations.setValues(stmt, i + 1, paramSubSelect[i]);
                }
            }

            // Ejecucion de la sentencia
            result = stmt.executeQuery();
            
            if (result.next()) {
                String csv = result.getString("CSV");
                if (StringUtils.isNotEmpty(csv)) {
                    existe = true;
                }
            }
        } catch (Exception sqle) {
            m_Log.error("Error al intentar determinar si existe el metadato en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al intentar determinar si existe el metadato en bbdd");
        } finally {
            SigpGeneralOperations.closeResultSet(result);
            SigpGeneralOperations.closeStatement(stmt);
        }
        
        return existe;
    }

    /**
     * Obtiene los metadatos de un documento
     *
     * @param idMetadatos identificador del metadato
     * @param conexion conexion de base de datos
     * @param params parametros de conexion a base de datos
     * @return el valor de los metadatos
     * @throws es.altia.common.exception.TechnicalException
     */
    public MetadatosDocumentoVO getMetadatos(Long idMetadatos, Connection conexion, String[] params)
            throws TechnicalException {
        MetadatosDocumentoVO resultado = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            StringBuilder sql = new StringBuilder();
        
            // Construccion de la consulta
            sql.append(" SELECT ID_METADATO, ")
               .append("   CSV, ")
               .append("   CSV_APLICACION, ")
               .append("   CSV_URI ")
               .append(" FROM METADATO_DOCUMENTO ")
               .append(" WHERE ID_METADATO = ? ");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
            }
            
            // Poblar variables bind de la sentencia
            stmt = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart, idMetadatos);
            
            // Ejecucion de la sentencia
            result = stmt.executeQuery();
            
            if (result.next()) {
                resultado = new MetadatosDocumentoVO();
                resultado.setId(JdbcOperations.getLongFromResultSet(result, "ID_METADATO"));
                resultado.setCsv(result.getString("CSV"));
                resultado.setCsvAplicacion(result.getString("CSV_APLICACION"));
                resultado.setCsvUri(result.getString("CSV_URI"));
            }
        } catch (Exception sqle) {
            sqle.printStackTrace();
            throw new TechnicalException("Error al intentar obtener los metadatos de bbdd");
        } finally {
            SigpGeneralOperations.closeResultSet(result);
            SigpGeneralOperations.closeStatement(stmt);
        }
        
        return resultado;
    }
    
    /**
     * Inserta el csv pendiente de procesar
     *
     * @param datos objeto que contiene los datos del objeto a dar de alta
     * @param conexion conexion de base de datos
     * @throws es.altia.common.exception.TechnicalException
     */
    public void insertarCsvPendienteProcesar(CSVPendienteVO datos, Connection conexion) throws TechnicalException {
        PreparedStatement stmt = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            // Construccion de la consulta
            sql.append("INSERT INTO CSV_PENDIENTES_PROCESAR (ID_TOKEN, CSV) ")
               .append("VALUES (?, ?)");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: %s", datos.toString()));
            }
            
            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    datos.getIdToken(),
                    datos.getCsv());

            // Ejecucion de la insert
            int filasAfectadas = stmt.executeUpdate();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            m_Log.error("Error al insertar el csv a procesar en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al insertar el csv a procesar en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
    /**
     * Eliminar el csv pendiente de procesar
     *
     * @param idToken
     * @param csv
     * @param conexion conexion de base de datos
     * @throws es.altia.common.exception.TechnicalException
     */
    public void eliminarCsvPendienteProcesar(Long idToken, String csv, Connection conexion)
            throws TechnicalException {
        PreparedStatement stmt = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            // Construccion de la consulta
            sql.append("DELETE CSV_PENDIENTES_PROCESAR ")
               .append("WHERE ID_TOKEN = ? ")
               .append(" AND CSV = ? ");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug("PARAMS:");
                m_Log.debug(String.format("ID_TOKEN: %d", idToken));
                m_Log.debug(String.format("CSV: %s", csv));
            }
            
            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    idToken,
                    csv);

            // Ejecucion de la delete
            int filasAfectadas = stmt.executeUpdate();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            m_Log.error("Error al borrar el csv a procesar en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al borrar el csv a procesar en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
    /**
     * Eliminar los csv pendientes de procesar que hayan caducado
     *
     * @param fecha fecha a partir de la que se calcula si han caducado
     * (la actual, normalmente)
     * @param conexion conexion de base de datos
     * @throws es.altia.common.exception.TechnicalException
     */
    public void eliminarCsvPendienteProcesarCaducados(Calendar fecha, Connection conexion)
            throws TechnicalException {
        PreparedStatement stmt = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            // Construccion de la consulta
            sql.append("DELETE CSV_PENDIENTES_PROCESAR ")
               .append("WHERE ID_TOKEN IN ")
               .append("(SELECT ID FROM AUTH_TOKEN_EXTERNOS WHERE FECHA_CADUCIDAD <= ?) ");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug("PARAMS:");
                m_Log.debug(String.format("fecha: %s", fecha));
            }
            
            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    DateOperations.toTimestamp(fecha));

            // Ejecucion de la delete
            int filasAfectadas = stmt.executeUpdate();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            m_Log.error("Error al borrar los csv a procesar caducados en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al borrar los csv a procesar caducados en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
    /**
     * Comprobar si existe el csv pendiente de procesar
     *
     * @param token
     * @param csv
     * @param conexion conexion de base de datos
     * @return 
     * @throws es.altia.common.exception.TechnicalException
     */
    public boolean existeCsvPendienteProcesar(String token, String csv, Connection conexion) throws TechnicalException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            Calendar fechaActual = Calendar.getInstance();
            
            // Construccion de la consulta
            sql.append("SELECT 1 ")
               .append("FROM CSV_PENDIENTES_PROCESAR cpr ")
               .append("INNER JOIN AUTH_TOKEN_EXTERNOS at ")
               .append("ON cpr.ID_TOKEN = at.ID ")
               .append("WHERE at.TOKEN = ? ")
               .append(" AND at.FECHA_CADUCIDAD > ?")
               .append(" AND cpr.CSV = ? ");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug("Params:");
                m_Log.debug(String.format("TOKEN: %s", token));
                m_Log.debug(String.format("FECHA_CADUCIDAD: %s", fechaActual));
                m_Log.debug(String.format("CSV: %s", csv));
            }
            
            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    token,
                    DateOperations.toTimestamp(fechaActual),
                    csv);

            // Ejecucion de la consulta
            rs = stmt.executeQuery();

            if (rs.next()) {
                existe = true;
            }
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("existe: %b ", existe));
            }
        } catch (Exception sqle) {
            m_Log.error("Error al comprobar el csv a procesar en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al comprobar el csv a procesar en bbdd");
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
        
        return existe;
    }
}
