package es.altia.util.security.dao;

import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.security.vo.AuthToken;
import es.altia.util.security.vo.UsuarioExternoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.util.StringUtils;

public class SeguridadDAO {

    private static SeguridadDAO instance;

    private static final Log log = LogFactory.getLog(SeguridadDAO.class.getName());

    private SeguridadDAO() {
    }

    /**
     * Obtiene una instancia unica.
     *
     * @return
     */
    public static SeguridadDAO getInstance() {
        SeguridadDAO temp = instance;
        if (temp == null) {
            synchronized (SeguridadDAO.class) {
                temp = instance;
                if (temp == null) {
                    temp = new SeguridadDAO();
                    instance = temp;
                }
            }
        }

        return instance;
    }

    /**
     * Comprueba las credenciales de un usuario o aplicacion externa
     *
     * @param user
     * @param con
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public UsuarioExternoVO getAuthExterno(String user, Connection con)
            throws TechnicalException {

        log.debug("getAuthExterno");

        UsuarioExternoVO usuario = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ID, NOMBRE, PASSWORD, ESTADO, INTENTOS_FALLIDOS ")
               .append("FROM AUTH_EXTERNOS ")
               .append("WHERE USUARIO = ? ")
               .append("AND FECHA_BAJA IS NULL ");
                    
            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("USUARIO = %s", user));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    user);

            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new UsuarioExternoVO();
                usuario.setId(rs.getInt("ID"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setPassword(rs.getString("PASSWORD"));
                usuario.setEstado(rs.getInt("ESTADO"));
                usuario.setIntentosFallidos(rs.getInt("INTENTOS_FALLIDOS"));
                usuario.setUsuario(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return usuario;
    }

    /**
     * Inserta un token de autenticacion de usuario o aplicacion externa
     *
     * @param token
     * @param usuario
     * @param conexion
     * @param adapt
     */
    public void insertarTokenExternos(AuthToken token, Connection conexion, AdaptadorSQLBD adapt)
            throws TechnicalException {
        PreparedStatement stmt = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            
            // Construccion de la consulta
            if (StringUtils.equals(ConstantesDatos.ORACLE, adapt.getTipoGestor()))
                sql.append("INSERT INTO AUTH_TOKEN_EXTERNOS (ID, TOKEN, ID_USUARIO, FECHA_CADUCIDAD) ")
                   .append("VALUES (auth_token_externos_id.NEXTVAL, ?, ?, ?)");
            else {
                sql.append("INSERT INTO AUTH_TOKEN_EXTERNOS (TOKEN, ID_USUARIO, FECHA_CADUCIDAD) ")
                        .append("VALUES (?, ?, ?)");
            }

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql));
                log.debug(String.format("AuthToken: %s", token.toString()));
            }
            
            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    token.getToken(),
                    token.getIdUsuario(),
                    DateOperations.toTimestamp(token.getFechaCaducidad()));

            // Ejecucion de la insert
            int filasAfectadas = stmt.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
            
            SigpGeneralOperations.closeStatement(stmt);
            
            // Obtenemos el id del token generado
            obtenerIdToken(token, conexion);
        } catch (Exception sqle) {
            log.error("Error al insertar el token en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al insertar el token en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
    /**
     * Obtiene el id del token
     * 
     * @param token
     * @param conexion 
     * @return
     * @throws es.altia.common.exception.TechnicalException 
     */
    public Long obtenerIdToken(String token, Connection conexion)
            throws TechnicalException {
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        obtenerIdToken(authToken, conexion);
                
        return authToken.getId();
    }
    
    /**
     * Obtiene el id del token
     * 
     * @param token
     * @param conexion 
     * @throws es.altia.common.exception.TechnicalException 
     */
    public void obtenerIdToken(AuthToken token, Connection conexion)
            throws TechnicalException {

        log.debug("obtenerIdToken");

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ID ")
               .append("FROM AUTH_TOKEN_EXTERNOS ")
               .append("WHERE TOKEN = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("TOKEN = %s", token.getToken()));
            }

            ps = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    token.getToken());

            rs = ps.executeQuery();

            if (rs.next()) {
                Long idToken = rs.getLong("ID");
                token.setId(idToken);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Eliminar un token de autenticacion externo
     * 
     * @param idToken
     * @param conexion
     * @throws TechnicalException 
     */
    public void eliminarTokenExternos(Long idToken, Connection conexion) 
        throws TechnicalException {
        PreparedStatement stmt = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta
            sql.append("DELETE AUTH_TOKEN_EXTERNOS ")
                    .append("WHERE ID = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql));
                log.debug("PARAMS:");
                log.debug(String.format("ID: %d", idToken));
            }

            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    idToken);

            // Ejecucion de la delete
            int filasAfectadas = stmt.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            log.error("Error al borrar el token en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al borrar el token en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }

    /**
     * Elimina todos los tokenes caducados
     * 
     * @param fecha fecha a partir de la que se calcula si han caducado
     * (la actual, normalmente)
     * @param conexion
     * @throws TechnicalException 
     */
    public void eliminarTokenExternoCaducados(Calendar fecha, Connection conexion)
            throws TechnicalException {
        PreparedStatement stmt = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta
            sql.append("DELETE AUTH_TOKEN_EXTERNOS ")
               .append("WHERE FECHA_CADUCIDAD <= ? ");
            
            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql));
                log.debug("PARAMS:");
                log.debug(String.format("fecha: %s", fecha));
            }

            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    DateOperations.toTimestamp(fecha));

            // Ejecucion de la delete
            int filasAfectadas = stmt.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            log.error("Error al borrar los token caducados en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al borrar los token caducados en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }

    /**
     * Reinicia el contador de intentos fallidos del usuario
     * 
     * @param id
     * @param conexion
     * @throws TechnicalException 
     */
    public void reiniciarAuthExternoIntentosFallidos(Integer id, Connection conexion)
            throws TechnicalException {
        PreparedStatement stmt = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta
            sql.append("UPDATE AUTH_EXTERNOS ")
               .append("SET INTENTOS_FALLIDOS = 0, FECHA_MODIFICACION = ? ")
               .append("WHERE ID = ? ");
            
            Calendar fechaActual = Calendar.getInstance();
            
            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql));
                log.debug("PARAMS:");
                log.debug(String.format("FECHA_MODIFICACION: %s", fechaActual.getTime()));
                log.debug(String.format("ID: %d", id));
            }

            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    DateOperations.toTimestamp(fechaActual),
                    id);

            // Ejecucion de la delete
            int filasAfectadas = stmt.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            log.error("Error al reiniciar los intentos fallidos del usuario externo en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al reiniciar los intentos fallidos del usuario externo en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }

    /**
     * Incrementa el contador de intentos fallidos del usuario
     * 
     * @param id
     * @param conexion
     * @throws TechnicalException 
     */
    public void incrementarAuthExternoIntentosFallidos(Integer id, Connection conexion)
            throws TechnicalException {
        PreparedStatement stmt = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta
            sql.append("UPDATE AUTH_EXTERNOS ")
                    .append("SET INTENTOS_FALLIDOS = INTENTOS_FALLIDOS + 1, FECHA_MODIFICACION = ? ")
                    .append("WHERE ID = ? ");

            Calendar fechaActual = Calendar.getInstance();

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql));
                log.debug("PARAMS:");
                log.debug(String.format("FECHA_MODIFICACION: %s", fechaActual.getTime()));
                log.debug(String.format("ID: %d", id));
            }

            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    DateOperations.toTimestamp(fechaActual),
                    id);

            // Ejecucion de la delete
            int filasAfectadas = stmt.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            log.error("Error al incrementar los intentos fallidos del usuario externo en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al incrementar los intentos fallidos del usuario externo en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }

    /**
     * Bloquear el usuario externo
     * 
     * @param id
     * @param conexion
     * @throws TechnicalException 
     */
    public void bloquearAuthExterno(Integer id, Connection conexion)
            throws TechnicalException {
        PreparedStatement stmt = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Construccion de la consulta
            sql.append("UPDATE AUTH_EXTERNOS ")
                    .append("SET ESTADO = ").append(ConstantesDatos.ESTADO_AUTH_EXTERNOS_BLOQUEADO)
                    .append(", FECHA_MODIFICACION = ? ")
                    .append("WHERE ID = ? ");

            Calendar fechaActual = Calendar.getInstance();

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql));
                log.debug("PARAMS:");
                log.debug(String.format("FECHA_MODIFICACION: %s", fechaActual.getTime()));
                log.debug(String.format("ID: %d", id));
            }

            // Poblar variables bind de la insert
            stmt = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            indexStart = JdbcOperations.setValues(stmt, indexStart,
                    DateOperations.toTimestamp(fechaActual),
                    id);

            // Ejecucion de la delete
            int filasAfectadas = stmt.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("filasAfectadas: %d ", filasAfectadas));
            }
        } catch (Exception sqle) {
            log.error("Error al bloquear el usuario externo en bbdd");
            sqle.printStackTrace();
            throw new TechnicalException("Error al bloquear el usuario externo en bbdd");
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
}
