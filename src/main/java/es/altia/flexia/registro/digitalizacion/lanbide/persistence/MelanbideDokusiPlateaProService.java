
package es.altia.flexia.registro.digitalizacion.lanbide.persistence;

import es.altia.flexia.registro.digitalizacion.lanbide.persistence.manual.MelanbideDokusiPlateaProDAO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.MelanbideDokusiPlateaPro;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class MelanbideDokusiPlateaProService {
    
    private static final Logger log = Logger.getLogger(MelanbideDokusiPlateaProService.class);
    private static SimpleDateFormat formatDateLogMetodos = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static MelanbideDokusiPlateaProDAO melanbideDokusiPlateaProDAO = new MelanbideDokusiPlateaProDAO();
    
    /**
     * Obtiene del codigo de procedimiento de Platea segun el codigo de
     * procedimiento Incluye mejora para unificar properties a BD Para Flexia y
     * WebServices. Tabla MELANBIDE_DOKUSI_PLATEAPRO. Recibe codigoProcedimiento de flexia
     *
     * @param codigoProcedimiento
     * @param adapt
     * @return
     */
    public String getCodigoProcedimientoPlatea(String codigoProcedimiento, AdaptadorSQLBD adapt) {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return getCodigoProcedimientoPlatea(codigoProcedimiento, con);
        } catch (Exception e) {
            log.error("Excepion  - getCodigoProcedimientoPlatea - ", e);
            return "";
        } finally {
            try {
                if (adapt != null) {
                    adapt.devolverConexion(con);
                }
            } catch (Exception e) {
                log.error("getCodigoProcedimientoPlatea - Error al cerrar la conexion BBDD", e);
            }
        }
    }

    /**
     * Obtiene del codigo de procedimiento de Platea segun el codigo de
     * procedimiento Incluye mejora para unificar properties a BD Para Flexia y
     * WebServices. Tabla MELANBIDE_DOKUSI_PLATEAPRO. Recibe codigoProcedimiento de Flexia
     *
     * @param codigoProcedimiento
     * @param con
     * @return
     */
    public static String getCodigoProcedimientoPlatea(String codigoProcedimiento, Connection con) {
        log.info("getCodigoProcedimientoPlatea - Begin - " + codigoProcedimiento + " " + formatDateLogMetodos.format(new Date()));
        String respuesta = "";
        try {
            if (codigoProcedimiento != null && !codigoProcedimiento.isEmpty()) {
                MelanbideDokusiPlateaPro procedimientoPlatea = melanbideDokusiPlateaProDAO.getMelanbideDokusiPlateaProByCodProcedimientoFlexia(codigoProcedimiento, con);
                if (procedimientoPlatea != null) {
                    respuesta = procedimientoPlatea.getCodigoProcedimientoPlatea();
                }
            }
        } catch (Exception e) {
            log.error("Excepion  - getCodigoProcedimientoPlatea - ", e);
            respuesta = "";
        }
        log.info("getCodigoProcedimientoPlatea - respuesta: " + respuesta + " " + formatDateLogMetodos.format(new Date()));
        return respuesta;
    }

    public String getCodigoProcedimientoPlatea(String codigoProcedimiento, String[] paramDBConnection) {
        return getCodigoProcedimientoPlatea(codigoProcedimiento, new AdaptadorSQLBD(paramDBConnection));
    }
    public MelanbideDokusiPlateaPro getMelanbideDokusiPlateaProById(int id, AdaptadorSQLBD adapt) {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return getMelanbideDokusiPlateaProById(id, con);
        } catch (Exception e) {
            log.error("Excepion  - getMelanbideDokusiPlateaProById - ", e);
            return null;
        } finally {
            try {
                if (adapt != null) {
                    adapt.devolverConexion(con);
                }
            } catch (Exception e) {
                log.error("getMelanbideDokusiPlateaProById - Error al cerrar la conexion BBDD", e);
            }
        }
    }

    public MelanbideDokusiPlateaPro getMelanbideDokusiPlateaProById(int id, Connection con) throws Exception {
        log.info("getMelanbideDokusiPlateaProById - Begin - " + id + " " + formatDateLogMetodos.format(new Date()));
        try {
            return melanbideDokusiPlateaProDAO.getMelanbideDokusiPlateaProById(id, con);
        } catch (Exception e) {
            log.error("Excepion  - getMelanbideDokusiPlateaProById - ", e);
            throw e;
        }
    }
    
    public List<MelanbideDokusiPlateaPro> getTodosMelanbideDokusiPlateaPro(AdaptadorSQLBD adapt) {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return getTodosMelanbideDokusiPlateaPro(con);
        } catch (Exception e) {
            log.error("Excepion  - getTodosMelanbideDokusiPlateaPro - ", e);
            return null;
        } finally {
            try {
                if (adapt != null) {
                    adapt.devolverConexion(con);
                }
            } catch (Exception e) {
                log.error("getTodosMelanbideDokusiPlateaPro - Error al cerrar la conexion BBDD", e);
            }
        }
    }

    public List<MelanbideDokusiPlateaPro> getTodosMelanbideDokusiPlateaPro(Connection con) throws Exception {
        log.info("getTodosMelanbideDokusiPlateaPro - Begin - " +  formatDateLogMetodos.format(new Date()));
        try {
            return melanbideDokusiPlateaProDAO.getTodosMelanbideDokusiPlateaPro( con);
        } catch (Exception e) {
            log.error("Excepion  - getTodosMelanbideDokusiPlateaPro - ", e);
            throw e;
        }
    }
    
}
