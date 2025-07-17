package es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by IntelliJ IDEA.
 * User: marcos.baltar
 * Date: 30-may-2006
 * Time: 17:32:01
 * To change this template use File | Settings | File Templates.
 */
public class UsuarioDAO {

    protected static Config m_ConfigTechnical;

    protected static String usu_cod;
    protected static String usu_idi;
    protected static String usu_nom;
    protected static String usu_log;
    protected static String usu_pas;

    protected static Log m_Log = LogFactory.getLog(UsuarioDAO.class.getName());


    public UsuarioDAO() {
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso	a los	mensajes de	error	localizados

        usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
        usu_idi = m_ConfigTechnical.getString("SQL.A_USU.idioma");
        usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");
        usu_log = m_ConfigTechnical.getString("SQL.A_USU.login");
        usu_pas = m_ConfigTechnical.getString("SQL.A_USU.password");

    }

    public boolean existeUsuarioCodigo(String codigo, Connection con)
            throws TechnicalException {

        Statement st = null;
        String sql = "";

        sql = "SELECT " + usu_cod
                + " FROM A_USU WHERE "
                + usu_cod + " = " + codigo;
        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeUsuarioCodigo(UsuarioValueObject user, Connection con)
            throws TechnicalException {

        Statement st = null;
        String sql = "";

        sql = "SELECT " + usu_cod + "," + usu_nom
                + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE "
                + usu_cod + " = " + user.getIdUsuario();
        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                user.setNombreUsu(rs.getString(usu_nom));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeUsuarioCodigo(UsuarioValueObject user, String[] params)
            throws TechnicalException {

        Statement st = null;
        String sql = "";
        AdaptadorSQLBD bd = new AdaptadorSQLBD(params);
        Connection con = null;
        ResultSet rs = null;

        try {
            con = bd.getConnection();

            sql = "SELECT " + usu_cod + "," + usu_nom
                    + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE "
                    + usu_cod + " = " + user.getIdUsuario();
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                user.setNombreUsu(rs.getString(usu_nom));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs!=null) rs.close();
                if (st!=null) st.close();
                bd.devolverConexion(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
