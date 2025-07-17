package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.common.exception.TechnicalException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class UnidadesTramitacionDAO {

    private static UnidadesTramitacionDAO instance	= null;
    protected static Log m_Log = LogFactory.getLog(UnidadesTramitacionDAO.class.getName());

    protected UnidadesTramitacionDAO() {
        super();
    }

    public static UnidadesTramitacionDAO getInstance() {
        synchronized(UnidadesTramitacionDAO.class)	{
            if (instance ==	null)	{
                 instance = new UnidadesTramitacionDAO();
            }
        }
        return instance;
    }

    public Vector<UORDTO> getUTRByTramite(int codOrg, String proc, int codTram, Connection con)
            throws TechnicalException {

        m_Log.debug("UnidadesTramitacionDAO.getUTRByTramite - Codigo procedimiento: " + proc);

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        Vector<UORDTO> list = new Vector<UORDTO>();

        try {
            sql = "SELECT UOR_COD, UOR_PAD, UOR_NOM, UOR_TIP, UOR_FECHA_ALTA, " +
                         "UOR_FECHA_BAJA, UOR_ESTADO, UOR_COD_VIS, UOR_EMAIL, " +
                         "UOR_NO_VIS, UOR_COD_ACCEDE, UOR_REX_GENERAL " +
                  "FROM E_TRA_UTR INNER JOIN A_UOR ON (TRA_UTR_COD = UOR_COD) " +
                  "WHERE TRA_MUN=" + codOrg + " AND TRA_PRO=? AND TRA_COD=" + codTram+
                  " AND UOR_ESTADO='A' ORDER BY UOR_NOM";
            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setString(1,proc);
            rs = ps.executeQuery();
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString("UOR_COD"));
                dto.setUor_cod_vis(rs.getString("UOR_COD_VIS"));
                dto.setUor_cod_accede(rs.getString("UOR_COD_ACCEDE"));
                dto.setUor_estado(rs.getString("UOR_ESTADO"));

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                dto.setUor_fecha_alta(df.format(new Date(rs.getTimestamp("UOR_FECHA_ALTA").getTime())));
                Timestamp fechaBaja = rs.getTimestamp("UOR_FECHA_BAJA");
                if (fechaBaja!=null) {
                    dto.setUor_fecha_baja(df.format(new Date(fechaBaja.getTime())));
                }

                dto.setUor_nom(rs.getString("UOR_NOM"));
                dto.setUor_pad(rs.getString("UOR_PAD"));
                dto.setUor_tipo(rs.getString("UOR_TIP"));
                dto.setUor_email(rs.getString("UOR_EMAIL"));
                dto.setUor_no_registro(rs.getString("UOR_NO_VIS").charAt(0));
                dto.setUor_rexistro_xeral(rs.getString("UOR_REX_GENERAL"));
                list.add(dto);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage());
        } finally {
            try {
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }

        return list;
    }

    public void insertUTR(int codOrg, String proc, int codTram,
            Vector<UORDTO> uors, Connection con)
            throws TechnicalException {


        PreparedStatement ps = null;
        String sql = "";

        try {           
            sql = "INSERT INTO E_TRA_UTR (TRA_MUN, TRA_PRO, TRA_COD, TRA_UTR_COD) " +
                  "VALUES (?,?,?,?)";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);

            //for (UORDTO uor : uors) {
            for(int j=0;uors!=null && j<uors.size();j++){
                UORDTO uorTO = uors.get(j);
                int i=1;
                ps.setInt(i++,codOrg);
                ps.setString(i++,proc);
                ps.setInt(i++,codTram);
                ps.setInt(i++,Integer.parseInt(uorTO.getUor_cod()));

                int rowsInserted = ps.executeUpdate();
                m_Log.debug("Nº filas insertadas : " + rowsInserted);
            }
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage());
        } finally {
            try {
                if (ps!=null) ps.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
    }

    public void insertUTRImportacion (int codOrg, String proc, int codTram,
            Vector<UORDTO> uors, Connection con)
            throws TechnicalException {

        PreparedStatement ps = null;
        String sql = "";

        try {
            for(int j=0;uors!=null && j<uors.size();j++){
                UORDTO uorTO = uors.get(j);
                m_Log.debug ("uor="+uorTO.getUor_cod_vis()+"-"+uorTO.getUor_nom());
                sql ="INSERT INTO E_TRA_UTR (TRA_MUN, TRA_PRO, TRA_COD, TRA_UTR_COD)  SELECT " + codOrg +
                        ", '" + proc + "', " + codTram + ", UOR_COD FROM A_UOR WHERE UOR_COD_VIS='" + uorTO.getUor_cod_vis() +
                        "' AND UOR_FECHA_BAJA IS NULL";

            ps = con.prepareStatement(sql);
                int rowsInserted = ps.executeUpdate();
                ps.close();
                m_Log.debug("Nº filas insertadas : " + rowsInserted);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage());
        } finally {
            try {
                if (ps!=null) ps.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
    }

    public void deleteUTRByTramite(int codOrg, String proc, int codTram, Connection con)
            throws TechnicalException {
                m_Log.debug("UnidadesTramitacionDAO.deleteUTRByTramite");

        PreparedStatement ps = null;
        String sql = "";

        try {
            sql = "DELETE FROM E_TRA_UTR " +
                  "WHERE TRA_MUN=" + codOrg + " AND TRA_PRO=? AND TRA_COD=" + codTram;
            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setString(1,proc);
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage());
        } finally {
            try {
                if (ps!=null) ps.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
    }
    
    /**
     * Elimina las unidades tramitadoras asociadas a un determinado procedimiento. Sólo debe ser llamada para cuando
     * se elimina un procedimineto
     * @param codOrg: Código de la organización
     * @param proc: Códiog del procedimiento
     * @param conexión a la base de datos
     * @throws es.altia.common.exception.TechnicalException si ocurre un error durante el borrado
     */
    public void deleteProcedimientoUTRByCodProc(int codOrg, String proc, Connection con)
            throws TechnicalException {
                m_Log.debug("UnidadesTramitacionDAO.deleteUTRByTramite");

        PreparedStatement ps = null;
        String sql = "";

        try {
            sql = "DELETE FROM E_TRA_UTR WHERE TRA_MUN=? AND TRA_PRO=?";
            m_Log.debug("deleteProcedimientoUTRByTramite sql: " + sql);
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++,codOrg);
            ps.setString(i++,proc);
            
            int deleteProcedimientoUTRByTramite = ps.executeUpdate();
            m_Log.debug("nº filas eliminadas: " + deleteProcedimientoUTRByTramite);
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage());
        } finally {
            try {
                if (ps!=null) ps.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
    }




}
