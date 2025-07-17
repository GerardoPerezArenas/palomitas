package es.altia.agora.business.planeamiento.mantenimiento.persistence.manual;

import java.util.Vector;
import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.agora.business.planeamiento.mantenimiento.MantOrganoAprobacionValueObject;

public class MantOrganoAprobacionDAO {

    //Para el fichero de configuracion tecnico.
    private static Config m_ConfigTechnical;
    //Para informacion de logs.
    private static Log m_Log = LogFactory.getLog(MantOrganoAprobacionDAO.class.getName());


    private static MantOrganoAprobacionDAO instance = null;
    //Nombre de Tabla y nombres de campos
    private static String nombreTabla = null;
    private static String codigo = null;
    private static String descripcion = null;

    protected MantOrganoAprobacionDAO() {
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados

        nombreTabla = m_ConfigTechnical.getString("SQL.U_ORG.Tabla");
        codigo = m_ConfigTechnical.getString("SQL.U_ORG.Codigo");
        descripcion = m_ConfigTechnical.getString("SQL.U_ORG.Descripcion");
    }

    public static MantOrganoAprobacionDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(MantOrganoAprobacionDAO.class){
                if (instance == null)
                    instance = new MantOrganoAprobacionDAO();
            }
        }
        return instance;
    }

    public void create(MantOrganoAprobacionValueObject mantOrganoAprobacionVO, String[] params)
            throws TechnicalException, BDException {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try{

            m_Log.info("MantOrganoAprobacionDAO.create");
            //m_Log.info("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;
            //stmt = conexion.createStatement();

            //Cogemos el ultimo identificador para generar el siguiente
/*            sql = "SELECT MAX(" + codigo + ") FROM " + nombreTabla;
            rs = stmt.executeQuery(sql);
            int valor = 0;
            while(rs.next()){
                valor = (new Integer(rs.getString(1))).intValue();
            }
*/
            sql = "INSERT INTO " + nombreTabla + "(" + codigo + "," + descripcion + ") VALUES (?, ?)";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
//            ps.setString(i++, (new Integer(valor + 1)).toString());
            ps.setString(i++, mantOrganoAprobacionVO.getCodigo());
            ps.setString(i++, mantOrganoAprobacionVO.getDescripcion());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
            conexion.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            abd.rollBack(conexion);
        } finally {
            if (conexion != null)
                try{
                    conexion.close();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException: " + ex.getMessage()) ;
                }
        }
    }

    public void delete(MantOrganoAprobacionValueObject mantOrganoAprobacionVO, String[] params)
            throws TechnicalException,BDException {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";

        try{
            m_Log.info("MantOrganoAprobacionDAO.delete");
            //m_Log.info("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.info("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;

            sql = "DELETE FROM " + nombreTabla + " WHERE " + codigo + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, mantOrganoAprobacionVO.getCodigo());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
            conexion.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            abd.rollBack(conexion);
        } finally {
            if (conexion != null)
                try{
                    conexion.close();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
                }
        }
    }

    public void modify(MantOrganoAprobacionValueObject mantOrganoAprobacionVO, String[] params)
            throws TechnicalException,BDException {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";

        try{

            m_Log.info("MantOrganoAprobacionDAO.modify");
            //m_Log.info("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.info("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;

            sql = "UPDATE " + nombreTabla + " SET " + descripcion + "=? WHERE " + codigo + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, mantOrganoAprobacionVO.getDescripcion());
            ps.setString(i++, mantOrganoAprobacionVO.getCodigo());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
            conexion.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            abd.rollBack(conexion);
        } finally {
            if (conexion != null)
                try{
                    conexion.close();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
                }
        }
    }

    public Vector findAll(String[] params) throws TechnicalException {

        Vector organos = new Vector();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT " + codigo + ", " + descripcion + " FROM " + nombreTabla  + " ORDER BY " + codigo;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            int i = 1;
            while(rs.next()){
                i = 1;
                String cod = rs.getString(i++);
                String desc = rs.getString(i++);
                organos.add(new MantOrganoAprobacionValueObject(cod, desc));
            }

            rs.close();
            st.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return organos;
    }
}