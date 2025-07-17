package es.altia.agora.business.planeamiento.persistence.manual;

import es.altia.agora.business.planeamiento.PromotorValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class PromotorDAO {

    //Para el fichero de configuracion tecnico.
    private static Config m_ConfigTechnical;
    //Para informacion de logs.
    private static Log m_Log = LogFactory.getLog(PromotorDAO.class.getName());


    private static PromotorDAO instance = null;
    //Nombre de Tabla y nombres de campos
    private static String nombreTabla = null;
    private static String tipoRegistro = null;
    private static String codigoSubseccion = null;
    private static String anho = null;
    private static String numero = null;
    private static String codigo = null;

    protected PromotorDAO() {
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados

        nombreTabla = m_ConfigTechnical.getString("SQL.U_PRM.Tabla");
        tipoRegistro = m_ConfigTechnical.getString("SQL.U_PRM.TipoRegistro");
        codigoSubseccion = m_ConfigTechnical.getString("SQL.U_PRM.CodigoSubseccion");
        anho = m_ConfigTechnical.getString("SQL.U_PRM.Anho");
        numero = m_ConfigTechnical.getString("SQL.U_PRM.Numero");
        codigo = m_ConfigTechnical.getString("SQL.U_PRM.Codigo");
    }

    public static PromotorDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(PromotorDAO.class){
                if (instance == null)
                    instance = new PromotorDAO();
            }
        }
        return instance;
    }

    public void create(PromotorValueObject promotorVO, Connection conexion)
            throws BDException {

        ResultSet rs = null;
        String sql = "";

        try{

            m_Log.info("PromotorDAO.create");

            sql = "INSERT INTO " + nombreTabla + "(" + tipoRegistro + "," + codigoSubseccion + "," + anho + "," +
                    numero + "," + codigo + ") VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, promotorVO.getTipoRegistro().toString());
            ps.setString(i++, promotorVO.getCodigoSubseccion());
            ps.setString(i++, promotorVO.getAnho());
            ps.setInt(i++, promotorVO.getNumero().intValue());
            ps.setInt(i++, promotorVO.getCodigo().intValue());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void delete(PromotorValueObject promotorVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("PromotorDAO.delete");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " +
                    anho + "=? AND " + numero + "=? AND " + codigo + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, promotorVO.getTipoRegistro().toString());
            ps.setString(i++, promotorVO.getCodigoSubseccion());
            ps.setString(i++, promotorVO.getAnho());
            ps.setInt(i++, promotorVO.getNumero().intValue());
            ps.setInt(i++, promotorVO.getCodigo().intValue());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void deleteByRegistro(PromotorValueObject promotorVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("PromotorDAO.deleteByRegistro");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " +
                    anho + "=? AND " + numero + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, promotorVO.getTipoRegistro().toString());
            ps.setString(i++, promotorVO.getCodigoSubseccion());
            ps.setString(i++, promotorVO.getAnho());
            ps.setInt(i++, promotorVO.getNumero().intValue());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public Vector findAll(String[] params) throws TechnicalException {

        Vector contadores = new Vector();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + anho + ", " + numero
                    + ", " + codigo + " FROM " + nombreTabla;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            int i = 1;
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                Integer cod = new Integer(rs.getInt(i++));
                contadores.add(new PromotorValueObject(tipoReg, codSub, num, año, cod));
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
        return contadores;
    }

    public Vector findByRegistro(Character tipReg, String codSubsec, String ano, Integer numRegistro,
                                 String[] params) throws TechnicalException {

        Vector contadores = new Vector();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + anho + ", " + numero
                    + ", " + codigo + " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion +
                    "=? AND " + anho + "=? AND " + numero + "=?";

            st = con.prepareStatement(sql);
            int i = 1;
            st.setString(i++, tipReg.toString());
            st.setString(i++, codSubsec);
            st.setString(i++, ano);
            st.setInt(i++, numRegistro.intValue());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                Integer cod = new Integer(rs.getInt(i++));
                contadores.add(new PromotorValueObject(tipoReg, codSub, num, año, cod));
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
        return contadores;
    }

    /**
     * Devuelve todos los registros que contienen todos los codigos de promotor pasados por parametro
     *
     * @param codigosPromotor
     * @param params
     * @return
     * @throws TechnicalException
     */
    public Vector findByPromotores(Collection codigosPromotor, String[] params) throws TechnicalException {

        Vector contadores = new Vector();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String sqlBase = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + anho + ", " + numero
                            + " FROM " + nombreTabla + " WHERE " + codigo + "=?";

            if (!codigosPromotor.isEmpty()) {
                sql = sql + sqlBase;
            }
            for (int i=1;i<codigosPromotor.size();i++) {
                sql = oad.intersect(sql, sqlBase);
                //sql = sql + " INTERSECT " + sqlBase;
            }

            st = con.prepareStatement(sql);
            int i = 1;
            Iterator codigosPromotorIt = codigosPromotor.iterator();
            while (codigosPromotorIt.hasNext()) {
                Integer codigoPromotor = (Integer) codigosPromotorIt.next();
                st.setInt(i++, codigoPromotor.intValue());
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                contadores.add(new PromotorValueObject(tipoReg, codSub, num, año, new Integer(-1)));
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
        return contadores;
    }
}