package es.altia.util.security;

import es.altia.agora.business.sge.persistence.manual.DocumentoDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.util.security.vo.AuthToken;
import es.altia.util.security.vo.UsuarioExternoVO;
import es.altia.util.security.util.TokenGenerator;
import es.altia.util.security.dao.SeguridadDAO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.util.StringUtils;

public class SeguridadManager {

    private static SeguridadManager instance;

    private static final Log log = LogFactory.getLog(SeguridadManager.class.getName());
    
    private static SeguridadDAO seguridadDAO;
    private static DocumentoDAO documentoDAO;
    
    private static Config configTechserver;
    
    // Constantes
    private static final String HTTP_AUTHORIZATION = "Authorization";
    private static final String HTTP_BASIC = "Basic";
    private static final String HTTP_SEPARADOS_CREDENCIALES = ":";
    private static final String TOKEN_EXTERNOS_MINUTOS_CADUCIDAD = "TOKEN/EXTERNOS/MINUTOS_CADUCIDAD";
    private static final String AUTH_EXTERNOS_MAX_INTENTOS_FALLIDOS = "AUTH/EXTERNOS/MAX_INTENTOS_FALLIDOS";
    
    
    private SeguridadManager() {
        seguridadDAO = SeguridadDAO.getInstance();
        documentoDAO = DocumentoDAO.getInstance();
        configTechserver = ConfigServiceHelper.getConfig("techserver");
    }
    
    /**
     * Obtiene una instancia unica.
     *
     * @return
     */
    public static SeguridadManager getInstance() {
        SeguridadManager temp = instance;
        if (temp == null) {
            synchronized (SeguridadManager.class) {
                temp = instance;
                if (temp == null) {
                    temp = new SeguridadManager();
                    instance = temp;
                }
            }
        }

        return instance;
    }

    /**
     * Comprueba las credenciales proporcionadas en el header HTTP y genera un
     * token de autenticacion para los usuarios externos
     *
     * @param httpRequest
     * @param con
     * @param adapt
     * @param params
     * @return token de autenticacion
     * @throws TechnicalException
     * @throws SecurityException
     */
    public AuthToken getAuthTokenExternos(
            HttpServletRequest httpRequest, Connection con,
            AdaptadorSQLBD adapt, String[] params)
            throws TechnicalException, SecurityException {
        AuthToken token = null;

        if (httpRequest != null) {
            // Se espera el siguiente formato: "Authorization: Basic base64credentials"
            // base64credentials son las credenciales codificados en base64 con
            // el formato: "username:password"
            // Ej: Authorization: Basic AJ49WN3jdm240dm=
            String authorization = httpRequest.getHeader(HTTP_AUTHORIZATION);

            if (authorization != null && authorization.startsWith(HTTP_BASIC)) {
                try {
                    String base64Credentials = authorization.substring(HTTP_BASIC.length()).trim();

                    String credentials = new String(
                            new Base64().decode(base64Credentials.getBytes(ConstantesDatos.CODIFICACION_UTF)),
                            Charset.forName(ConstantesDatos.CODIFICACION_UTF));

                    final String[] credentialsValue = credentials.split(HTTP_SEPARADOS_CREDENCIALES, 2);
                    String user = credentialsValue[0];
                    String password = credentialsValue[1];

                    if (log.isDebugEnabled()) {
                        log.debug(String.format("user = %s", user));
                        log.debug(String.format("password = %s", password));
                    }

                    // Comprobacion de usuario
                    UsuarioExternoVO usuario = comprobarAuthExterno(user, password, params);
                    
                    // Generacion del token
                    if (usuario != null) {
                        token = generarAuthTokenExternos();
                        token.setIdUsuario(usuario.getId());
                        
                        seguridadDAO.insertarTokenExternos(token, con, adapt);
                    } else {
                        throw new SecurityException(String.format(
                                "Las credenciales del usuario [%s] no son correctas", user));
                    }
                } catch (SecurityException se) {
                    token = null;
                    throw se;
                } catch (TechnicalException tex) {
                    token = null;
                    throw tex;
                } catch (Exception ex) {
                    token = null;
                    throw new TechnicalException(String.format(
                            "Hubo un error al generar el token: %s", ex.getMessage()), ex);
                }
            }
        }

        return token;
    }
    
    /**
     * Genera un token de autenticacion para usuarios externos
     * 
     * @param user
     * @param password
     * @return 
     */
    private static AuthToken generarAuthTokenExternos()
            throws TechnicalException {
        AuthToken authToken = null;
        
        try {
            int minutosCaducidad = configTechserver.getInt(TOKEN_EXTERNOS_MINUTOS_CADUCIDAD);

            // Calcular fechaCaducidad
            Calendar fechaActual = Calendar.getInstance();
            fechaActual.add(Calendar.MINUTE, minutosCaducidad);

            // Generar token
            String token = TokenGenerator.generarToken();
            
            authToken = new AuthToken();
            authToken.setToken(token);
            authToken.setFechaCaducidad(fechaActual);
            
            if (log.isDebugEnabled()) {
                log.debug(String.format("Token generado: %s", authToken.toString()));
            }
        } catch (Exception ex) {
            authToken = null;
            throw new TechnicalException(String.format("Hubo un error al generar el token: %s", ex.getMessage()), ex);
        }
        
        return authToken;
    }

    /**
     * Comprueba si las credenciales aportadas son las correctas. En caso
     * contrario aumenta el marcador de sesiones incorrectas
     * 
     * @param user
     * @param password
     * @param con
     * @return
     * @throws TechnicalException
     * @throws SecurityException 
     */
    private UsuarioExternoVO comprobarAuthExterno(
            String user, String password, String[] params)
            throws TechnicalException, SecurityException {
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        UsuarioExternoVO usuario = null;

        if (log.isDebugEnabled()) {
            log.debug("comprobarAuthExterno");
            log.debug(String.format("user: %s", user));
            log.debug(String.format("password: %s", password));
        }
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            String hashContrasena = DigestUtils.shaHex(password);
            usuario = seguridadDAO.getAuthExterno(user, con);

            if (usuario != null) {
                log.debug("El usuario existe");
                
                Integer estado = usuario.getEstado();
                
                // Si la contrasena no es correcta
                if (!StringUtils.equals(usuario.getPassword(), hashContrasena)) {
                    log.debug("Credenciales incorrectas");
                    
                    // Si no esta activo se indica que las credenciales no son correctas
                    // para que no se sepa el estado del usuario si no se tiene la contrasena
                    if (ConstantesDatos.ESTADO_AUTH_EXTERNOS_ACTIVO.equals(estado)) {
                        // Incrementamos el contador de intentos fallidos
                        seguridadDAO.incrementarAuthExternoIntentosFallidos(usuario.getId(), con);

                        // Si se superan los intentos se bloquea la cuenta
                        Integer intentosFallidos = usuario.getIntentosFallidos() + 1; // Los anteriores intentos mas este intento
                        Integer maxIntentosFallidos = configTechserver.getInt(AUTH_EXTERNOS_MAX_INTENTOS_FALLIDOS);
                       
                        if (log.isDebugEnabled()) {
                            log.debug(String.format("intentosFallidos: %d", intentosFallidos));
                            log.debug(String.format("maxIntentosFallidos: %d", maxIntentosFallidos));
                        }
                        
                        if (intentosFallidos >= maxIntentosFallidos) {
                            log.debug("Se bloquea el usuario por superar el numero de intentos fallidos");
                            seguridadDAO.bloquearAuthExterno(usuario.getId(), con);
                            
                            throw new SecurityException("Se ha bloqueado el usuario por superar el número máximo de intentos");
                        }
                    }
                    
                    throw new SecurityException("Las credenciales no son correctas");
                } else { // Si la contrasena es correcta
                    log.debug("Credenciales correctas");
                    
                    // Comprobamos el estado del usuario
                    if (!ConstantesDatos.ESTADO_AUTH_EXTERNOS_ACTIVO.equals(estado)) {
                        if (ConstantesDatos.ESTADO_AUTH_EXTERNOS_BLOQUEADO.equals(estado)) {
                            log.debug("El usuario está bloqueado");

                            throw new SecurityException("El usuario está bloqueado");
                        } else {
                            log.debug("El usuario no está activo");

                            throw new SecurityException("El usuario no está activo");
                        }
                    }

                    log.debug("El usuario está activo");
                    
                    // Todo ha sido correcto, por lo que reiniciamos el contador de intentos fallidos
                    seguridadDAO.reiniciarAuthExternoIntentosFallidos(usuario.getId(), con);
                }
            } else {
                log.debug("El usuario no existe");
                throw new SecurityException("Las credenciales no son correctas");
            }
            
            SigpGeneralOperations.commit(adapt, con);
        } catch (SecurityException se) {
            SigpGeneralOperations.commit(adapt, con);
            throw se;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            SigpGeneralOperations.rollBack(adapt, con);
            throw new TechnicalException(String.format(
                    "Error al comprobar las credenciales: %s", ex.getMessage()), ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
        }

        return usuario;
    }

    /**
     * Elimina todos los tokens caducados
     *
     * @param params
     * @throws es.altia.common.exception.TechnicalException
     */
    public void eliminarTokenExternoCaducadosConReferencias(String[] params)
            throws TechnicalException {
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            Calendar fechaActual = Calendar.getInstance();
            
            if (log.isDebugEnabled()) {
                log.debug(String.format(
                        "Se borraran los tokens con fecha de caducidad anterior a: %s ",
                        fechaActual.getTime()));
            }
            // Se borran los registros de las tablas que hacen referencia al token
            documentoDAO.eliminarCsvPendienteProcesarCaducados(fechaActual, con);
            
            // Se borran los propios tokens
            seguridadDAO.eliminarTokenExternoCaducados(fechaActual, con);

            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            SigpGeneralOperations.rollBack(adapt, con);
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
        }
    }

}
