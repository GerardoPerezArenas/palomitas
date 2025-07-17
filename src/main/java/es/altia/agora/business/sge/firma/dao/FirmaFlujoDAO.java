package es.altia.agora.business.sge.firma.dao;

import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramiteClave;
import es.altia.agora.business.sge.firma.vo.FirmaFirmanteVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoUsuariosVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.sge.firma.vo.FirmaTipoVO;
import es.altia.agora.business.sge.firma.vo.FirmaUsuarioVO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class FirmaFlujoDAO {

    private static FirmaFlujoDAO instance = null;
    private static final Logger log = Logger.getLogger(FirmaFlujoDAO.class);

    private FirmaFlujoDAO() {
    }

    /**
     * Obtiene una instancia unica.
     *
     * @return
     */
    public static FirmaFlujoDAO getInstance() {
        FirmaFlujoDAO temp = instance;
        if (temp == null) {
            synchronized (FirmaFlujoDAO.class) {
                temp = instance;
                if (temp == null) {
                    temp = new FirmaFlujoDAO();
                    instance = temp;
                }
            }
        }

        return instance;
    }

    /**
     * Obtiene un listado de los tipos de firmas disponibles
     *
     * @param con
     * @return
     * @throws TechnicalException
     */
    public List<FirmaTipoVO> getListaTipoFirmas(Connection con)
            throws TechnicalException {

        log.debug("getTipoFirmas");

        List<FirmaTipoVO> resultado = new ArrayList<FirmaTipoVO>();
        FirmaTipoVO firmaTipo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT ID, NOMBRE FROM FIRMA_TIPO ORDER BY NOMBRE";

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Listado tipo de firma: %s", sql));
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                firmaTipo = new FirmaTipoVO();
                firmaTipo.setId(rs.getInt("ID"));
                firmaTipo.setNombre(rs.getString("NOMBRE"));

                resultado.add(firmaTipo);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Obtiene un listado de los flujos de firma disponibles
     *
     * @param con
     * @return
     * @throws TechnicalException
     */
    public List<FirmaFlujoVO> getListaFlujosFirma(Connection con)
            throws TechnicalException {

        log.debug("getFlujosFirma");

        List<FirmaFlujoVO> resultado = new ArrayList<FirmaFlujoVO>();
        FirmaFlujoVO firmaFlujo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT flujo.ID as ID,")
                    .append(" flujo.NOMBRE AS NOMBRE,")
                    .append(" flujo.ID_TIPO as ID_TIPO,")
                    .append(" tipo.NOMBRE as NOMBRE_TIPO,")
                    .append(" flujo.ACTIVO as ACTIVO ")
                    .append("FROM FIRMA_FLUJO flujo INNER JOIN FIRMA_TIPO tipo ON flujo.ID_TIPO = tipo.ID ")
                    .append("ORDER BY flujo.NOMBRE");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Listado flujos de firma: %s", sql.toString()));
            }

            ps = con.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                firmaFlujo = new FirmaFlujoVO();
                firmaFlujo.setId(rs.getInt("ID"));
                firmaFlujo.setNombre(rs.getString("NOMBRE"));
                firmaFlujo.setIdTipoFirma(rs.getInt("ID_TIPO"));
                firmaFlujo.setTipoFirma(rs.getString("NOMBRE_TIPO"));
                firmaFlujo.setActivo(JdbcOperations.getBooleanFromIntegerResultSet(rs, "ACTIVO"));

                resultado.add(firmaFlujo);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Obtiene el listado de firmantes del circuito de un flujo de firma
     *
     * @param idFlujoFirma
     * @param con
     * @return
     * @throws TechnicalException
     */
    public List<FirmaCircuitoVO> getListaCircuitoFirmasByIdFlujo(Integer idFlujoFirma, Connection con)
            throws TechnicalException {

        log.debug("getListaCircuitoFirmasByIdFlujo");

        List<FirmaCircuitoVO> resultado = new ArrayList<FirmaCircuitoVO>();
        FirmaCircuitoVO firmaCircuito = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT circuito.ID_FIRMA_FLUJO as ID_FIRMA_FLUJO, ")
                    .append(" circuito.ID_USUARIO as ID_USUARIO, ")
                    .append(" circuito.orden as ORDEN, ")
                    .append(" usu.USU_NOM as NOMBRE, ")
                    .append(" usu.USU_NIF as DOCUMENTO, ")
                    .append(" usu.USU_LOG as LOG ")
                    .append("FROM FIRMA_CIRCUITO circuito INNER JOIN ")
                    .append(GlobalNames.ESQUEMA_GENERICO).append("A_USU usu ON circuito.ID_USUARIO = usu.USU_COD ")
                    .append("WHERE circuito.ID_FIRMA_FLUJO = ? ")
                    .append("ORDER BY circuito.ORDEN");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Circuito de firmas para un flujo: %s", sql.toString()));
                log.debug(String.format("Parámetros pasados a la SQL: %s", idFlujoFirma));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart, idFlujoFirma);

            rs = ps.executeQuery();

            while (rs.next()) {
                firmaCircuito = new FirmaCircuitoVO();
                firmaCircuito.setIdFlujoFirma(rs.getInt("ID_FIRMA_FLUJO"));
                firmaCircuito.setIdUsuario(rs.getInt("ID_USUARIO"));
                firmaCircuito.setOrden(rs.getInt("ORDEN"));
                firmaCircuito.setNombreUsuario(rs.getString("NOMBRE"));
                firmaCircuito.setLogUsuario(rs.getString("LOG"));
                firmaCircuito.setDocumentoUsuario(rs.getString("DOCUMENTO"));

                resultado.add(firmaCircuito);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Obtiene un listado de los flujos de firma disponibles
     *
	 * @param codProc
     * @param con
     * @return
     * @throws TechnicalException
     */
    public List<FirmaFlujoVO> getListaFlujosFirmaByCodProc(String codProc, Connection con)
            throws TechnicalException {

        log.debug("getListaFlujosFirmaByCodProc");

        List<FirmaFlujoVO> resultado = new ArrayList<FirmaFlujoVO>();
        FirmaFlujoVO firmaFlujo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT DISTINCT flujo.ID as ID,")
                    .append(" flujo.NOMBRE AS NOMBRE,")
                    .append(" flujo.ID_TIPO as ID_TIPO,")
                    .append(" tipo.NOMBRE as NOMBRE_TIPO,")
                    .append(" flujo.ACTIVO as ACTIVO ")
                    .append("FROM FIRMA_FLUJO flujo INNER JOIN FIRMA_TIPO tipo ON flujo.ID_TIPO = tipo.ID ")
					.append("INNER JOIN E_DOT_FIR dotfir ON flujo.ID = dotfir.DOT_FLUJO ")
					.append("WHERE dotfir.DOT_PRO = ? ")
                    .append("ORDER BY flujo.NOMBRE");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Listado flujos de firma: %s", sql.toString()));
                log.debug(String.format("Parámetros de la SQL: %s", codProc));
            }

            ps = con.prepareStatement(sql.toString());
			
			int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart, codProc);
			
            rs = ps.executeQuery();

            while (rs.next()) {
                firmaFlujo = new FirmaFlujoVO();
                firmaFlujo.setId(rs.getInt("ID"));
                firmaFlujo.setNombre(rs.getString("NOMBRE"));
                firmaFlujo.setIdTipoFirma(rs.getInt("ID_TIPO"));
                firmaFlujo.setTipoFirma(rs.getString("NOMBRE_TIPO"));
                firmaFlujo.setActivo(JdbcOperations.getBooleanFromIntegerResultSet(rs, "ACTIVO"));

                resultado.add(firmaFlujo);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Inserta un flujo de firma en base de datos
     *
     * @param firmaFlujo
     * @param adapt
     * @param con
     * @return
     * @throws TechnicalException
     */
    public Integer insertarFlujoFirma(FirmaFlujoVO firmaFlujo, AdaptadorSQL adapt, Connection con)
            throws TechnicalException {

        log.debug("insertarFlujoFirma");

        Integer id = null;
        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            if (ConstantesDatos.ORACLE.equals(adapt.getTipoGestor())) {
                sql.append("INSERT INTO FIRMA_FLUJO (ID, NOMBRE, ID_TIPO, ACTIVO) ")
                        .append("VALUES (firma_flujo_id.NEXTVAL, ?, ?, ?)");
            } else if (ConstantesDatos.SQLSERVER.equals(adapt.getTipoGestor())) {
                sql.append("INSERT INTO FIRMA_FLUJO (NOMBRE, ID_TIPO, ACTIVO) ")
                        .append("VALUES (?, ?, ?)");
            }

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Insertar flujo de firma: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("NOMBRE = %s", firmaFlujo.getNombre()));
                log.debug(String.format("ID_TIPO = %d", firmaFlujo.getIdTipoFirma()));
                log.debug(String.format("ACTIVO = %b", firmaFlujo.isActivo()));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    firmaFlujo.getNombre(),
                    firmaFlujo.getIdTipoFirma(),
                    JdbcOperations.convertBooleanToIntegerForDB(firmaFlujo.isActivo()));

            ps.executeUpdate();

            SigpGeneralOperations.closeStatement(ps);

            id = getIdFlujoFirma(firmaFlujo.getNombre(), con);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }

        return id;
    }

    /**
     * Elimina un flujo de firma en base de datos
     *
     * @param id
     * @param con
     * @throws TechnicalException
     */
    public void eliminarFlujoFirma(Integer id, Connection con)
            throws TechnicalException {

        log.debug("eliminarFlujoFirma");

        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Es necesario eliminar primero los circuitos definidos para este flujo
            eliminarCircuitosFlujosFirma(id, con);

            sql.append("DELETE FROM FIRMA_FLUJO WHERE ID = ?");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL eliminar flujo de firma: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("ID = %d", id));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart, id);

            int filas = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("Se han borrado %d filas del flujo", filas));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Activa/Desactiva un flujo de firma en base de datos
     *
     * @param id
     * @param activar
     * @param con
     * @throws TechnicalException
     */
    public void activarDesactivarFlujoFirma(Integer id, Boolean activar, Connection con)
            throws TechnicalException {

        log.debug("activarDesactivarFlujoFirma");

        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE FIRMA_FLUJO SET ACTIVO = ? WHERE ID = ?");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL activar/desactivar flujo de firma: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("ID = %d", id));
                log.debug(String.format("ACTIVO = %d", JdbcOperations.convertBooleanToIntegerForDB(activar)));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    JdbcOperations.convertBooleanToIntegerForDB(activar),
                    id);

            int filas = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("Se han modificado %d filas del flujo", filas));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Actualiza los datos del circuito de un flujo de firma en base de datos
     *
     * @param idFlujoFirma
     * @param idsUsuario
     * @param con
     * @throws TechnicalException
     */
    public void actualizarDatosCircuitoFlujo(Integer idFlujoFirma, List<Integer> idsUsuario, Connection con)
            throws TechnicalException {

        log.debug("actualizarDatosCircuitoFlujo");

        PreparedStatement ps = null;

        try {
            // Se borran los datos anteriores
            eliminarCircuitosFlujosFirma(idFlujoFirma, con);

            // Se insertan los nuevos firmantes
            if (idsUsuario != null && !idsUsuario.isEmpty()) {
                StringBuilder sql = new StringBuilder();

                sql.append("INSERT INTO FIRMA_CIRCUITO (ID_FIRMA_FLUJO, ID_USUARIO, ORDEN) ")
                        .append("VALUES (?, ?, ?)");

                if (log.isDebugEnabled()) {
                    log.debug(String.format("SQL insertar circuitos del flujo de firma: %s", sql.toString()));
                }

                ps = con.prepareStatement(sql.toString());

                int orden = 1;
                int indexStart = 1;
                for (int i = 0; i < idsUsuario.size(); i++) {
                    Integer idUsuario = idsUsuario.get(i);

                    if (log.isDebugEnabled()) {
                        log.debug(String.format("USUARIO[%d]:", i));
                        log.debug(String.format("ID_FIRMA_FLUJO = %d", idFlujoFirma));
                        log.debug(String.format("ID_USUARIO = %d", idUsuario));
                        log.debug(String.format("ORDEN = %d", orden));
                    }

                    indexStart = 1;
                    JdbcOperations.setValues(ps, indexStart,
                            idFlujoFirma,
                            idUsuario,
                            orden++);

                    ps.addBatch();
                }
                
                ps.executeBatch();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Inserta los datos del flujo de firmas personalizado de un tramite.
     * 
     * @param clave
     * @param idTipoFirma
     * @param con
     * @throws TechnicalException 
     */
    public void insertarFlujoTramitePersonalizado(
            FirmaDocumentoTramiteClave clave, Integer idTipoFirma,
            Connection con)
            throws TechnicalException {
        
        log.debug("insertarFlujoTramitePersonalizado");

        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("INSERT INTO E_CRD_FIR_FLUJO (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO, ID_TIPO_FIRMA) ")
               .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL insertar flujo de firma personalizado de tramite: %s", sql.toString()));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    clave.getCodMunicipio(),
                    clave.getCodProcedimiento(),
                    clave.getEjercicio(),
                    clave.getNumExpediente(),
                    clave.getCodTramite(),
                    clave.getCodOcurrencia(),
                    clave.getCodDocumento(),
                    idTipoFirma);

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }
    
    /**
     * Inserta los datos de los usuarios firmantes personalizados
     * de un documento de tramite.
     * 
     * @param clave
     * @param idsUsuario
     * @param con
     * @throws TechnicalException 
     */
    public void insertarFirmantesTramitePersonalizado(
            FirmaDocumentoTramiteClave clave, List<Integer> idsUsuario,
            Connection con)
            throws TechnicalException {
        
        log.debug("insertarFirmantesTramitePersonalizado");

        PreparedStatement ps = null;

        try {
            // Se insertan los nuevos firmantes
            if (idsUsuario != null && !idsUsuario.isEmpty()) {
                StringBuilder sql = new StringBuilder();

                sql.append("INSERT INTO E_CRD_FIR_FIRMANTES (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO, ID_USUARIO, ORDEN) ")
                   .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");

                if (log.isDebugEnabled()) {
                    log.debug(String.format("SQL insertar firmantes personalizados: %s", sql.toString()));
                }

                ps = con.prepareStatement(sql.toString());

                if (log.isDebugEnabled()) {
                    log.debug(String.format("clave = %s", clave));
                }

                int orden = 1;
                int indexStart = 1;
                for (int i = 0; i < idsUsuario.size(); i++) {
                    Integer idUsuario = idsUsuario.get(i);

                    if (log.isDebugEnabled()) {
                        log.debug(String.format("USUARIO[%d]:", i));
                        log.debug(String.format("ID_USUARIO = %d", idUsuario));
                        log.debug(String.format("ORDEN = %d", orden));
                    }

                    indexStart = 1;
                    JdbcOperations.setValues(ps, indexStart,
                            clave.getCodMunicipio(),
                            clave.getCodProcedimiento(),
                            clave.getEjercicio(),
                            clave.getNumExpediente(),
                            clave.getCodTramite(),
                            clave.getCodOcurrencia(),
                            clave.getCodDocumento(),
                            idUsuario,
                            orden++);

                    ps.addBatch();
                }
                
                ps.executeBatch();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }
    
    /**
     * Actualiza el usuario de modificacion del documento del tramite
     *
     * @param id
     * @param activar
     * @param con
     * @throws TechnicalException
     */
    public void actualizarUsuarioModificacionDocumentoTramite(
            FirmaDocumentoTramiteClave clave, List<Integer> idsUsuario,
            Connection con) throws TechnicalException {

        log.debug("activarDesactivarFlujoFirma");

        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE E_CRD SET CRD_USM = ? WHERE CRD_MUN = ? AND CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? AND CRD_NUD = ?");

            if (log.isDebugEnabled()) {
                    log.debug(String.format("SQL insertar firmantes personalizados: %s", sql.toString()));
                }

            ps = con.prepareStatement(sql.toString());
            
            if (log.isDebugEnabled()) {
                    log.debug(String.format("clave = %s", clave));
                }

            int orden = 1;
            int indexStart = 1;
            for (int i = 0; i < idsUsuario.size(); i++) {
                Integer idUsuario = idsUsuario.get(i);

                if (log.isDebugEnabled()) {
                    log.debug(String.format("USUARIO[%d]:", i));
                    log.debug(String.format("ID_USUARIO = %d", idUsuario));
                    log.debug(String.format("ORDEN = %d", orden));
                }

                indexStart = 1;
                JdbcOperations.setValues(ps, indexStart,
                        idUsuario,
                        clave.getCodMunicipio(),
                        clave.getCodProcedimiento(),
                        clave.getEjercicio(),
                        clave.getNumExpediente(),
                        clave.getCodTramite(),
                        clave.getCodOcurrencia(),
                        clave.getCodDocumento());

                ps.addBatch();
            }

            ps.executeBatch();

       } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Elimina los circuitos de los flujo de firma en base de datos
     *
     * @param id
     * @param con
     * @throws TechnicalException
     */
    public boolean eliminarCircuitosFlujosFirma(Integer id, Connection con)
            throws TechnicalException {

        log.debug("eliminarCircuitosFlujosFirma");

        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("DELETE FROM FIRMA_CIRCUITO WHERE ID_FIRMA_FLUJO = ?");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL eliminar circuitos del flujo de firma: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("ID_FIRMA_FLUJO = %d", id));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart, id);

            int filas = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("Se han borrado %d filas del circuito", filas));
            }
			
			return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Obtiene el id del flujo a partir del nombre
     *
     * @param nombre
     * @param con
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public Integer getIdFlujoFirma(String nombre, Connection con)
            throws TechnicalException {

        log.debug("getIdFlujoFirma");

        Integer id = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ID FROM FIRMA_FLUJO WHERE NOMBRE = ?");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Obtener id de flujo de firma: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("NOMBRE = %s", nombre));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart, nombre);

            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return id;
    }

    /**
     * Obtiene el flujo a partir del id
     *
     * @param id
     * @param con
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public FirmaFlujoVO getFlujoFirma(Integer id, Connection con)
            throws TechnicalException {

        log.debug("getFlujoFirma");

        FirmaFlujoVO flujo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ID, NOMBRE, ID_TIPO FROM FIRMA_FLUJO WHERE ID = ?");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Obtener el flujo de firma: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("ID = %d", id));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                flujo = new FirmaFlujoVO();
                flujo.setId(rs.getInt("ID"));
                flujo.setNombre(rs.getString("NOMBRE"));
                flujo.setIdTipoFirma(rs.getInt("ID_TIPO"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return flujo;
    }
    
    /**
     * Obtiene el listado de los usuarios de firma disponibles
     *
     * @param obligatorioNIF
     * @param con
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public List<FirmaUsuarioVO> getListaUsuariosFirmaDisponibles(boolean obligatorioNIF,boolean obligBuzonFirma, Connection con)
            throws TechnicalException {

        log.debug("getListaUsuariosFirmaDisponibles");

        List<FirmaUsuarioVO> resultado = new ArrayList<FirmaUsuarioVO>();
        FirmaUsuarioVO firmaUsuario = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT USU_COD, USU_LOG, USU_NOM, USU_NIF ");
            
            if (obligBuzonFirma) {
                sql.append(" , USU_BUZFIR ");
            }
            
           sql.append("FROM ").append(GlobalNames.ESQUEMA_GENERICO).append("A_USU ")
           .append("WHERE USU_FIRMANTE = 1 ");
            
            if (obligatorioNIF) {
                sql.append(" AND USU_NIF IS NOT NULL ");
            }
            
            if (obligBuzonFirma) {
                sql.append(" AND USU_BUZFIR IS NOT NULL ");
            }
                    
            sql.append("ORDER BY USU_NOM");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Listado usuarios de firma: %s", sql.toString()));
            }

            ps = con.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                firmaUsuario = new FirmaUsuarioVO();
                firmaUsuario.setIdUsuario(rs.getInt("USU_COD"));
                firmaUsuario.setLogin(rs.getString("USU_LOG"));
                firmaUsuario.setNombre(rs.getString("USU_NOM"));
                firmaUsuario.setDocumento(rs.getString("USU_NIF"));
                if (obligBuzonFirma) {
                    firmaUsuario.setBuzonFirma(rs.getString("USU_BUZFIR"));
                }

                resultado.add(firmaUsuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Obtiene un listado de flujos con los usuarios asociados al mismo. Si se
     * especifica el flag activo a true, solo se seleccionaran los que estan
     * activos.
     *
     * @param idFlujo
     * @param activo
     * @param con
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public List<FirmaFlujoUsuariosVO> getListaFlujosFirmaConUsuarios(Integer idFlujo, Boolean activo, Connection con)
            throws TechnicalException {

        log.debug("getListaFlujosFirmaConUsuarios");

        List<FirmaFlujoUsuariosVO> resultado = new ArrayList<FirmaFlujoUsuariosVO>();
        FirmaFlujoUsuariosVO flujoUsuarioCircuito = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            boolean filtroWhere = false;
            StringBuilder sql = new StringBuilder();
            
            // Select y From
            sql.append("SELECT ff.ID AS FLUJO_ID, ")
               .append("    ff.NOMBRE AS FLUJO_NOMBRE, ")
               .append("    ff.ID_TIPO AS FLUJO_ID_TIPO, ")
               .append("    ft.NOMBRE AS FLUJO_TIPO, ")
               .append("    ff.ACTIVO AS FLUJO_ACTIVO, ")
               .append("    fc.ID_USUARIO AS CIRCUITO_ID_USUARIO, ")
               .append("    usu.USU_NOM AS USUARIO_NOMBRE, ")
               .append("    usu.USU_NIF AS DOCUMENTO, ")
               .append("    fc.ORDEN AS CIRCUITO_ORDEN ")
               .append("FROM FIRMA_FLUJO ff ")
               .append("INNER JOIN FIRMA_CIRCUITO fc ON ff.ID = fc.ID_FIRMA_FLUJO ")
               .append("INNER JOIN FIRMA_TIPO ft ON ff.ID_TIPO = ft.ID ")
               .append("INNER JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("A_USU usu ON fc.ID_USUARIO = usu.USU_COD ");
            
            // Where
            // Activo
            if (Boolean.TRUE.equals(activo)) {
                filtroWhere = JdbcOperations.anadirFiltroWhere(sql, filtroWhere);
                sql.append("ff.ACTIVO = 1 ");
            }
            // IdFlujo
            if (idFlujo != null) {
                filtroWhere = JdbcOperations.anadirFiltroWhere(sql, filtroWhere);
                sql.append("ff.ID = ? ");
            }
            
            // Order By
            sql.append("ORDER BY ff.NOMBRE, fc.ORDEN ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Listado flujos de firma con usuarios: %s", sql.toString()));
            }

            // Preparar query
            ps = con.prepareStatement(sql.toString());
            
            int indexStart = 1;
            if (idFlujo != null) {
                indexStart = JdbcOperations.setValues(ps, indexStart, idFlujo);
            }
            
            // Ejecutar query
            rs = ps.executeQuery();
            
            // Leer resultados
            FirmaFlujoVO firmaFlujo = null;
            FirmaCircuitoVO firmaUsuarioCircuito = null;
            List<FirmaCircuitoVO> listaUsuariosCircuito = null;
            int idFlujoActual = -1;
            int idFlujoAnterior = -1;
            while (rs.next()) {
                idFlujoActual = rs.getInt("FLUJO_ID");

                if (idFlujoActual != idFlujoAnterior) {
                    idFlujoAnterior = idFlujoActual;
                    
                    // Lista de usuarios del circuito
                    listaUsuariosCircuito = new ArrayList<FirmaCircuitoVO>();
                    
                    // Flujo
                    firmaFlujo = new FirmaFlujoVO();
                    firmaFlujo.setId(idFlujoActual);
                    firmaFlujo.setNombre(rs.getString("FLUJO_NOMBRE"));
                    firmaFlujo.setIdTipoFirma(rs.getInt("FLUJO_ID_TIPO"));
                    firmaFlujo.setTipoFirma(rs.getString("FLUJO_TIPO"));
                    
                    // FlujoUsuarioCircuito
                    flujoUsuarioCircuito = new FirmaFlujoUsuariosVO();
                    flujoUsuarioCircuito.setFlujo(firmaFlujo);
                    flujoUsuarioCircuito.setUsuariosCircuito(listaUsuariosCircuito);
                    resultado.add(flujoUsuarioCircuito);
                }

                // Usuario del circuito
                firmaUsuarioCircuito = new FirmaCircuitoVO();
                firmaUsuarioCircuito.setIdUsuario(rs.getInt("CIRCUITO_ID_USUARIO"));
                firmaUsuarioCircuito.setNombreUsuario(rs.getString("USUARIO_NOMBRE"));
                firmaUsuarioCircuito.setDocumentoUsuario(rs.getString("DOCUMENTO"));
                firmaUsuarioCircuito.setOrden(rs.getInt("CIRCUITO_ORDEN"));
                listaUsuariosCircuito.add(firmaUsuarioCircuito);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Obtiene un listado del estado de las firmas del circuito (E_CRD_FIR_FIRMANTES)
     * 
     * @param clave
     * @param con
     * @return
     * @throws TechnicalException 
     */
    public List<FirmaFirmanteVO> getListaEstadosCircuitoFirmas(
            FirmaDocumentoTramiteClave clave, Connection con)
            throws TechnicalException {
        
        log.debug("getListaEstadosCircuitoFirmas");

        List<FirmaFirmanteVO> resultado = new ArrayList<FirmaFirmanteVO>();
        FirmaFirmanteVO firmaUsuario = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT fusu.ID_USUARIO AS ID_USUARIO, ")
               .append("       usu.USU_NOM AS NOMBRE_USUARIO, ")
               .append("       fusu.ORDEN AS ORDEN, ")
               .append("       fusu.ESTADO_FIRMA AS ESTADO_FIRMA, ")
               .append("       fusu.FECHA_FIRMA AS FECHA_FIRMA, crdfir.OBSERV AS OBSERVACIONES")
               .append(" FROM E_CRD_FIR_FIRMANTES fusu ")
               .append(" INNER JOIN ")
               .append(" E_CRD_FIR crdfir ON crdfir.CRD_MUN = fusu.COD_MUNICIPIO AND crdfir.CRD_PRO = fusu.COD_PROCEDIMIENTO AND crdfir.CRD_EJE = fusu.EJERCICIO ") 
               .append(" AND crdfir.CRD_NUM = fusu.NUM_EXPEDIENTE AND crdfir.CRD_TRA = fusu.COD_TRAMITE AND crdfir.CRD_OCU = fusu.COD_OCURRENCIA AND crdfir.CRD_NUD = fusu.COD_DOCUMENTO")
               .append(" INNER JOIN ")
               .append(GlobalNames.ESQUEMA_GENERICO).append("A_USU usu ON usu.USU_COD = fusu.ID_USUARIO ")
               .append(" WHERE fusu.COD_MUNICIPIO = ? ")
               .append("        AND fusu.COD_PROCEDIMIENTO = ? ")
               .append("        AND fusu.EJERCICIO = ? ")
               .append("        AND fusu.NUM_EXPEDIENTE = ? ")
               .append("        AND fusu.COD_TRAMITE = ? ")
               .append("        AND fusu.COD_OCURRENCIA = ? ")
               .append("        AND fusu.COD_DOCUMENTO = ? ")
               .append(" ORDER BY fusu.ORDEN ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Listado de estados de firma del circuito: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("clave = %s", clave.toString()));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    clave.getCodMunicipio(),
                    clave.getCodProcedimiento(),
                    clave.getEjercicio(),
                    clave.getNumExpediente(),
                    clave.getCodTramite(),
                    clave.getCodOcurrencia(),
                    clave.getCodDocumento());

            rs = ps.executeQuery();

            while (rs.next()) {
                firmaUsuario = new FirmaFirmanteVO();
                firmaUsuario.setId(rs.getInt("ID_USUARIO"));
                firmaUsuario.setNombre(rs.getString("NOMBRE_USUARIO"));
                firmaUsuario.setOrden(rs.getInt("ORDEN"));
                firmaUsuario.setEstadoFirma(rs.getString("ESTADO_FIRMA"));
                firmaUsuario.setFechaFirma(
                        DateOperations.toCalendar(rs.getTimestamp("FECHA_FIRMA")));
                firmaUsuario.setObservaciones(rs.getString("OBSERVACIONES"));

                resultado.add(firmaUsuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }
    
    /**
     * Obtiene el flujo y circuito a partir de la pk del documento de tramite (E_CRD)
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param codOcurrencia
     * @param codDocumento
     * @param con
     * @return
     * @throws TechnicalException 
     */
    public Integer getIdFlujoByDocumentoTramite(
            Integer codMunicipio, String codProcedimiento, Integer ejercicio, String numExpediente,
            Integer codTramite, Integer codOcurrencia, Integer codDocumento,
            Connection con)
            throws TechnicalException {

        log.debug("getIdFlujoByDocumentoTramite");

        Integer resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT fir.DOT_FLUJO AS FLUJO_ID ")
               .append("FROM E_CRD crd ")
               .append("INNER JOIN E_DOT_FIR fir ON crd.CRD_DOT = fir.DOT_COD AND DOT_MUN = ? AND DOT_PRO = ? AND DOT_TRA = ? ")
               .append("WHERE crd.CRD_MUN = ? ")
               .append("AND crd.CRD_PRO = ? ")
               .append("AND crd.CRD_EJE = ? ")
               .append("AND crd.CRD_NUM = ? ")
               .append("AND crd.CRD_TRA = ? ")
               .append("AND crd.CRD_OCU = ? ")
               .append("AND crd.CRD_NUD = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Id de firma por documento de tramite: %s", sql.toString()));
            }

            // Preparar query
            ps = con.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(ps, indexStart,
                    codMunicipio,
					codProcedimiento,
					codTramite,
					codMunicipio,
					codProcedimiento,
					ejercicio,
					numExpediente,
					codTramite,
					codOcurrencia,
					codDocumento);
            
            rs = ps.executeQuery();
            
            // Leer resultados
            while (rs.next()) {
                resultado = rs.getInt("FLUJO_ID");
                if (rs.wasNull()) {
                    resultado = null;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }

    /**
     * Obtiene el usuario firmante partir de la pk del documento de tramite (E_CRD)
     * 
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @param con
     * @return
     * @throws TechnicalException 
     */
    public FirmaUsuarioVO getUsuarioPorDefectoByDocumentoTramite(
            Integer codOrganizacion, String codProcedimiento, Integer ejercicio, String numExpediente,
            Integer codTramite, Integer ocurrenciaTramite, Integer codDocumento, String portafirmas,
            Connection con)
            throws TechnicalException {

        log.debug("getUsuarioPorDefectoByDocumentoTramite");

        FirmaUsuarioVO resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT usu.USU_COD AS ID_USUARIO, ")
               .append("    usu.USU_LOG AS LOGIN, ")
               .append("    usu.USU_NOM AS NOMBRE_USUARIO, ")
               .append("    usu.USU_NIF AS DOCUMENTO_USUARIO ");
           if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
              sql.append(", usu.USU_BUZFIR AS BUZON_FIRMAS ");
           }   
               sql.append("FROM   E_CRD crd ")
               .append("INNER JOIN E_DOT_FIR fir ")
               .append("    ON crd.CRD_DOT = fir.DOT_COD ")
               .append("    AND DOT_MUN = ? ")
               .append("    AND DOT_PRO = ? ")
               .append("    AND DOT_TRA = ? ")
               .append("INNER JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("A_USU usu ")
               .append("    ON fir.USU_COD = usu.USU_COD ");
           if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
              sql.append(" AND usu.USU_BUZFIR IS NOT NULL ");
           }  
               sql.append("WHERE  crd.CRD_MUN = ? ")
               .append("    AND crd.CRD_PRO = ? ")
               .append("    AND crd.CRD_EJE = ? ")
               .append("    AND crd.CRD_NUM = ? ")
               .append("    AND crd.CRD_TRA = ? ")
               .append("    AND crd.CRD_OCU = ? ")
               .append("    AND crd.CRD_NUD = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Obtener usuario firmante por documento tramite: %s", sql.toString()));
            }

            // Preparar query
            ps = con.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(ps, indexStart,
                    codOrganizacion,
					codProcedimiento,
					codTramite,
					codOrganizacion,
					codProcedimiento,
					ejercicio,
					numExpediente,
					codTramite,
					ocurrenciaTramite,
					codDocumento);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                resultado = new FirmaUsuarioVO();
                resultado.setIdUsuario(rs.getInt("ID_USUARIO"));
                resultado.setLogin(rs.getString("LOGIN"));
                resultado.setNombre(rs.getString("NOMBRE_USUARIO"));
                resultado.setDocumento(rs.getString("DOCUMENTO_USUARIO"));
                if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                    resultado.setBuzonFirma(rs.getString("BUZON_FIRMAS"));
                } 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }
    
    /**
     * Obtiene el usuario firmante partir del Documento Tramite (E_CRD_FIR_FIRMANTE)
     * 
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @param con
     * @return
     * @throws TechnicalException 
     */
    public FirmaUsuarioVO getUsuarioFirmanteByDocumentoTramite(
            Integer codOrganizacion, String codProcedimiento, Integer ejercicio, String numExpediente,
            Integer codTramite, Integer ocurrenciaTramite, Integer codDocumento, String portafirmas,
            Connection con)
            throws TechnicalException {

        log.debug("getUsuarioPorDefectoByDocumentoTramite");

        FirmaUsuarioVO resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT usu.USU_COD AS ID_USUARIO, ")
               .append("    usu.USU_LOG AS LOGIN, ")
               .append("    usu.USU_NOM AS NOMBRE_USUARIO, ")
               .append("    usu.USU_NIF AS DOCUMENTO_USUARIO ");
           if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
              sql.append(", usu.USU_BUZFIR AS BUZON_FIRMAS ");
           }   
               sql.append("FROM   E_CRD_FIR_FIRMANTES crd_fir_firmante ")
               .append("INNER JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("A_USU usu ")
               .append("    ON crd_fir_firmante.ID_USUARIO = usu.USU_COD ");
           if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
              sql.append(" AND usu.USU_BUZFIR IS NOT NULL ");
           }  
               sql.append("WHERE  crd_fir_firmante.COD_MUNICIPIO = ? ")
               .append("    AND crd_fir_firmante.COD_PROCEDIMIENTO = ? ")
               .append("    AND crd_fir_firmante.EJERCICIO = ? ")
               .append("    AND crd_fir_firmante.NUM_EXPEDIENTE = ? ")
               .append("    AND crd_fir_firmante.COD_TRAMITE = ? ")
               .append("    AND crd_fir_firmante.COD_OCURRENCIA = ? ")
               .append("    AND crd_fir_firmante.COD_DOCUMENTO = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Obtener usuario firmante por documento tramite: %s", sql.toString()));
                log.debug(String.format("Parametros de la SQL: %d-%s-%d-%s-%d-%d-%d", codOrganizacion, codProcedimiento,
                        ejercicio, numExpediente, codTramite, ocurrenciaTramite, codDocumento));
            }

            // Preparar query
            ps = con.prepareStatement(sql.toString());
            
            int indexStart = 1;
            indexStart = JdbcOperations.setValues(ps, indexStart,
                    codOrganizacion,
					codProcedimiento,
                                        ejercicio,
					numExpediente,
					codTramite,
					ocurrenciaTramite,
					codDocumento);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                resultado = new FirmaUsuarioVO();
                resultado.setIdUsuario(rs.getInt("ID_USUARIO"));
                resultado.setLogin(rs.getString("LOGIN"));
                resultado.setNombre(rs.getString("NOMBRE_USUARIO"));
                resultado.setDocumento(rs.getString("DOCUMENTO_USUARIO"));
                if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                    resultado.setBuzonFirma(rs.getString("BUZON_FIRMAS"));
                } 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }
    
    
    /**
     * Obtiene el tipo de firma a partir del id
     *
     * @param id
     * @param con
     * @return
     * @throws TechnicalException
     */
    public FirmaTipoVO getTipoFirma(Integer id, Connection con)
            throws TechnicalException {

        log.debug("getTipoFirma");

        FirmaTipoVO tipo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ID, NOMBRE FROM FIRMA_TIPO WHERE ID = ?");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL Obtener el tipo de firma: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("ID = %d", id));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                tipo = new FirmaTipoVO();
                tipo.setId(rs.getInt("ID"));
                tipo.setNombre(rs.getString("NOMBRE"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return tipo;
    }
    
    /**
     * Elimina un flujo de firmas personalizado de tramites
     * 
     * @param clave
     * @param con
     * @throws TechnicalException 
     */
    public void eliminarFlujoTramitePersonalizado(
            FirmaDocumentoTramiteClave clave, Connection con)
            throws TechnicalException {

        log.debug("eliminarFlujoTramitePersonalizado");

        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("DELETE E_CRD_FIR_FLUJO ")
               .append("WHERE COD_MUNICIPIO = ? ")
               .append("   AND COD_PROCEDIMIENTO = ? ")
               .append("   AND EJERCICIO = ? ")
               .append("   AND NUM_EXPEDIENTE = ? ")
               .append("   AND COD_TRAMITE = ? ")
               .append("   AND COD_OCURRENCIA = ? ")
               .append("   AND COD_DOCUMENTO = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL eliminar el flujo de firma personalizado de tramite: %s",
                        sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("clave = %s", clave.toString()));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    clave.getCodMunicipio(),
                    clave.getCodProcedimiento(),
                    clave.getEjercicio(),
                    clave.getNumExpediente(),
                    clave.getCodTramite(),
                    clave.getCodOcurrencia(),
                    clave.getCodDocumento());

            int filas = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("Se han borrado %d filas", filas));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Elimina los firmantes personalizados del documento de tramite
     * 
     * @param clave
     * @param con
     * @throws TechnicalException 
     */
    public void eliminarFirmantesTramitePersonalizado(
            FirmaDocumentoTramiteClave clave, Connection con)
            throws TechnicalException {

        log.debug("eliminarFirmantesTramitePersonalizado");

        PreparedStatement ps = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("DELETE E_CRD_FIR_FIRMANTES ")
               .append("WHERE COD_MUNICIPIO = ? ")
               .append("   AND COD_PROCEDIMIENTO = ? ")
               .append("   AND EJERCICIO = ? ")
               .append("   AND NUM_EXPEDIENTE = ? ")
               .append("   AND COD_TRAMITE = ? ")
               .append("   AND COD_OCURRENCIA = ? ")
               .append("   AND COD_DOCUMENTO = ? ");

            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL eliminar firmantes personalizados del documento de tramite: %s",
                        sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("clave = %s", clave.toString()));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    clave.getCodMunicipio(),
                    clave.getCodProcedimiento(),
                    clave.getEjercicio(),
                    clave.getNumExpediente(),
                    clave.getCodTramite(),
                    clave.getCodOcurrencia(),
                    clave.getCodDocumento());

            int filas = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug(String.format("Se han borrado %d filas", filas));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Comprueba si el tipo de firma para un documento de tramitacion que ya ha
     * sido enviado al portafirmas es de tipo nuevo L o U (Flujo o Un Usuario))
     * 
     * @param clave
     * @param con
     * @return
     * @throws TechnicalException 
     */
    public Boolean comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion(
            FirmaDocumentoTramiteClave clave, Connection con)
            throws TechnicalException {

        log.debug("comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion");

        Boolean resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT 1 AS RESULTADO ")
               .append("FROM E_CRD_FIR_FIRMANTES ")
               .append("WHERE COD_MUNICIPIO = ? ")
               .append(" AND COD_PROCEDIMIENTO = ? ")
               .append(" AND EJERCICIO = ? ")
               .append(" AND NUM_EXPEDIENTE = ? ")
               .append(" AND COD_TRAMITE = ? ")
               .append(" AND COD_OCURRENCIA = ? ")
               .append(" AND COD_DOCUMENTO = ? ");
               
            if (log.isDebugEnabled()) {
                log.debug(String.format("SQL: %s", sql.toString()));
                log.debug("PARAMS:");
                log.debug(String.format("clave = %s", clave.toString()));
            }

            ps = con.prepareStatement(sql.toString());

            int indexStart = 1;
            JdbcOperations.setValues(ps, indexStart,
                    clave.getCodMunicipio(),
                    clave.getCodProcedimiento(),
                    clave.getEjercicio(),
                    clave.getNumExpediente(),
                    clave.getCodTramite(),
                    clave.getCodOcurrencia(),
                    clave.getCodDocumento());

            rs = ps.executeQuery();

            if (rs.next()) {
                resultado = Boolean.TRUE;
            } else {
                resultado = Boolean.FALSE;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return resultado;
    }
	
	/**
     * Comprueba si existe un flujo con el mismo nombre que el que se quiere insertar
     * 
	 * @param nombreFlujo
     * @param conexion
	 * @return 
     * @throws SQLException 
     */
    public Integer existeFlujoPorNombre(String nombreFlujo, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder();
		SqlExecuter executer = null;
		QueryResult qresult = null;
		Integer flujoId = -1;
		
		try {
			executer = new SqlExecuter(sql.select("ID").from("FIRMA_FLUJO").whereEqualsParametrizado("NOMBRE"));
			executer.setValues(nombreFlujo).logSqlDebug(log);
			
			qresult = executer.executeQuery(conexion);
			if(qresult.next()) { // Existe
				flujoId = qresult.getInteger("ID");
			} 			
			
			log.debug("El id del flujo encontrado es: " + flujoId);
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error comprobando la existencia de un flujo por nombre. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
		
		return flujoId;
    }
	
	/**
     * Comprueba si existe un  circuito de firmas de un determinado flujo.
     * 
	 * @param idFlujo
     * @param conexion
	 * @return 
     * @throws SQLException 
     */
    public boolean existeCircuitoParaFlujo(Integer idFlujo, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder();
		SqlExecuter executer = null;
		QueryResult qresult = null;
		boolean existe = false;
		
		try {
			executer = new SqlExecuter(sql.select("ORDEN").from("FIRMA_CIRCUITO").whereEqualsParametrizado("ID_FIRMA_FLUJO"));
			executer.setValues(idFlujo).logSqlDebug(log);
			
			qresult = executer.executeQuery(conexion);
			if(qresult.next()) { // Existe
				existe = true;
			} 	
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error comprobando la existencia de un circuito de firmas para un determinado flujo. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
		
		return existe;
    }
	
	/**
     * Obtiene el maximo orden existente en un circuito de firmas de un determinado flujo.
     * 
	 * @param idFlujo
     * @param conexion
	 * @return 
     * @throws SQLException 
     */
    public int getMaxOrdenCircuito(Integer idFlujo, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder();
		SqlExecuter executer = null;
		QueryResult qresult = null;
		int maxOrden = -1;
		
		try {
			executer = new SqlExecuter(sql.select("MAX(ORDEN)").as("ORDEN").from("FIRMA_CIRCUITO").whereEqualsParametrizado("ID_FIRMA_FLUJO"));
			executer.setValues(idFlujo).logSqlDebug(log);
			
			qresult = executer.executeQuery(conexion);
			if(qresult.next()) { // Existe
				maxOrden = qresult.getInteger("ORDEN");
			} 	
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error recuperando el orden máximo de un circuito de firmas para un determinado flujo. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
		
		return maxOrden;
    }
	
	/**
     * Comprueba si existe un firmante en concreto en el circuito de firmas de un determina flujo
     * 
	 * @param idFlujo
	 * @param idUsuario
     * @param conexion
	 * @return 
     * @throws SQLException 
     */
    public Integer existeFirmanteEnFlujo(Integer idFlujo, Integer idUsuario, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder();
		SqlExecuter executer = null;
		QueryResult qresult = null;
		Integer ordenFirma = -1;
		
		try {
			executer = new SqlExecuter(sql.select("ORDEN").from("FIRMA_CIRCUITO").whereEqualsParametrizado("ID_FIRMA_FLUJO", "ID_USUARIO"));
			executer.setValues(idFlujo, idUsuario).logSqlDebug(log);
			
			qresult = executer.executeQuery(conexion);
			if(qresult.next()) { // Existe
				ordenFirma = qresult.getInteger("ORDEN");
			} 			
			
			log.debug("El orden del firmante es: " + ordenFirma);
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error comprobando la existencia de un flujo por nombre. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
		
		return ordenFirma;
    }
	
	/**
	 * Elimina de FIRMA_FLUJO y FIRMA_CIRCUITO los datos de flujos no usados en ningún documento
	 * @param codProcedimiento
	 * @param mapeoIds
	 * @param con
	 * @throws SQLException 
	 */
	public void eliminarFlujosYCircuitosNoUsados(Connection con) throws SQLException {
		SqlBuilder sqlSelect = new SqlBuilder();
		SqlBuilder sqlSubSelect = new SqlBuilder();
        PreparedStatement pst = null;
        ResultSet resultado = null;
		Integer idFlujo = null;
		
		try {
			sqlSubSelect.selectDistinct("DOT_FLUJO").from("E_DOT_FIR").where("DOT_FLUJO ","IS NOT NULL","");
			sqlSelect.select("ID").from("FIRMA_FLUJO").whereNotIn("ID", sqlSubSelect);
			
			pst = con.prepareStatement(sqlSelect.toString());
			if (log.isDebugEnabled()) {
				log.debug("Query = " + sqlSelect.toString());
			}
            resultado = pst.executeQuery();
            
			while(resultado.next()){ // Hay flujos cuyo id esta en FIRMA_FLUJO pero no en E_DOT_FIR
				idFlujo = resultado.getInt("ID");
				
				boolean borrado = eliminarCircuitosFlujosFirma(idFlujo, con); // Se elimina los circuitos con id de flujo sea el actual de los recuperados de la query
				if(borrado){ // El borrado de circuitos tuvo exito, se elimina el flujo en cuestion
					eliminarFlujoFirma(idFlujo, con); // Se elimina el flujo
				}
			}
        
         
		} catch (SQLException sqle) {
			log.error("Ha ocurrido un error en FirmaFlujoDAO.eliminarFlujosYCircuitosNoUsados(). " + sqle.getMessage());
			throw sqle;
		} catch (TechnicalException te) {
			log.error("Ha ocurrido un error en FirmaFlujoDAO.eliminarFlujosYCircuitosNoUsados(). " + te.getMessage());
			te.printStackTrace();
		} finally {
            if (pst != null) {
                pst.close();
            }
            if (resultado != null) {
                resultado.close();
            }
        }
	}
}
