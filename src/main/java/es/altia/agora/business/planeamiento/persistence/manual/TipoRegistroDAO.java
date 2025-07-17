package es.altia.agora.business.planeamiento.persistence.manual;

import es.altia.agora.business.planeamiento.TipoRegistroValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;

public class TipoRegistroDAO {

    //Para el fichero de configuracion tecnico.
    private static Config m_ConfigTechnical;
    //Para informacion de logs.
    private static Log m_Log = LogFactory.getLog(TipoRegistroDAO.class.getName());


    private static TipoRegistroDAO instance = null;
    //Nombre de Tabla y nombres de campos
    private static String nombreTabla = null;
    private static String codigo = null;
    private static String descripcion = null;

    protected TipoRegistroDAO() {
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados

        nombreTabla = m_ConfigTechnical.getString("SQL.U_TRE.Tabla");
        codigo = m_ConfigTechnical.getString("SQL.U_TRE.Codigo");
        descripcion = m_ConfigTechnical.getString("SQL.U_TRE.Descripcion");
    }

    public static TipoRegistroDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(TipoRegistroDAO.class){
                if (instance == null)
                    instance = new TipoRegistroDAO();
            }
        }
        return instance;
    }

    public void create(TipoRegistroValueObject tipoRegistroVO, String[] params)
            throws TechnicalException, BDException {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try{

            m_Log.info("TipoRegistroDAO.create");
            m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;
            stmt = conexion.createStatement();
            //Cogemos el ultimo identificador para generar el siguiente
            sql = "SELECT " +abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{codigo}) +
                  " FROM " + nombreTabla;
            rs = stmt.executeQuery(sql);
            int valor = 0;
            while(rs.next()){
                valor = (new Integer(rs.getString(1))).intValue();
            }

            sql = "INSERT INTO " + nombreTabla + "(" + codigo + "," + descripcion + ") VALUES (?, ?)";

            stmt.close();
            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, (new Integer(valor+1)).toString());
            ps.setString(i++, tipoRegistroVO.getDescripcion());
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

    public void delete(TipoRegistroValueObject tipoRegistroVO, String[] params)
            throws TechnicalException,BDException {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";

        try{
            m_Log.info("TipoRegistroDAO.delete");
            m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;

            sql = "DELETE FROM " + nombreTabla + " WHERE " + codigo + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, tipoRegistroVO.getCodigo());
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

    public void modify(TipoRegistroValueObject tipoRegistroVO, String[] params)
            throws TechnicalException,BDException {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";

        try{

            m_Log.info("TipoRegistroDAO.modify");
            m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;

            sql = "UPDATE " + nombreTabla + " SET " + descripcion + "=? WHERE " + codigo + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, tipoRegistroVO.getDescripcion());
            ps.setString(i++, tipoRegistroVO.getCodigo());
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

        Vector tipos = new Vector();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT " + codigo + ", " + descripcion + " FROM " + nombreTabla;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                String cod = rs.getString(1);
                String desc = rs.getString(2);
                tipos.add(new TipoRegistroValueObject(cod, desc));
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
        return tipos;
    }
}