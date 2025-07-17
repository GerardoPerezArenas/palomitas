package es.altia.agora.business.planeamiento.persistence.manual;

import es.altia.agora.business.planeamiento.ContadorRegistroValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;

public class ContadorRegistroDAO {

    //Para el fichero de configuracion tecnico.
    private static Config m_ConfigTechnical;
    //Para informacion de logs.
    private static Log m_Log = LogFactory.getLog(ContadorRegistroDAO.class.getName());


    private static ContadorRegistroDAO instance = null;
    //Nombre de Tabla y nombres de campos
    private static String nombreTabla = null;
    private static String tipoRegistro = null;
    private static String codigoSubseccion = null;
    private static String anho = null;
    private static String numero = null;

    protected ContadorRegistroDAO() {
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados

        nombreTabla = m_ConfigTechnical.getString("SQL.U_CON.Tabla");
        tipoRegistro = m_ConfigTechnical.getString("SQL.U_CON.TipoRegistro");
        codigoSubseccion = m_ConfigTechnical.getString("SQL.U_CON.CodigoSubseccion");
        anho = m_ConfigTechnical.getString("SQL.U_CON.Anho");
        numero = m_ConfigTechnical.getString("SQL.U_CON.Numero");
    }

    public static ContadorRegistroDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(ContadorRegistroDAO.class){
                if (instance == null)
                    instance = new ContadorRegistroDAO();
            }
        }
        return instance;
    }

    public Integer create(ContadorRegistroValueObject contadorRegistroVO, Connection conexion, String[] params)
            throws BDException {

        ResultSet rs = null;
        String sql = "";
        AdaptadorSQLBD abd = null;
        try{
            abd = new AdaptadorSQLBD(params);
            m_Log.info("ContadorRegistroDAO.create");

            //Cogemos el ultimo identificador para generar el siguiente
            sql = "SELECT " + abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{numero})+
                  " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " +
                    codigoSubseccion + "=?";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, contadorRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, contadorRegistroVO.getCodigoSubseccion());
            rs = ps.executeQuery();
            int valor = 0;
            if ((rs.next()) && (rs.getInt(1)!=0)){
                valor = rs.getInt(1);
                sql = "UPDATE " + nombreTabla + " SET " + numero + "=? WHERE " + tipoRegistro + "=? AND " +
                        codigoSubseccion + "=?";

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps.close();
                ps = conexion.prepareStatement(sql);
                i = 1;
                ps.setInt(i++, (valor + 1));
                ps.setString(i++, contadorRegistroVO.getTipoRegistro().toString());
                ps.setString(i++, contadorRegistroVO.getCodigoSubseccion());
            } else {
                sql = "INSERT INTO " + nombreTabla + "(" + tipoRegistro + "," + codigoSubseccion + "," + anho + "," +
                        numero + ") VALUES (?, ?, ?, ?)";
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps.close();
                ps = conexion.prepareStatement(sql);
                i = 1;
                ps.setString(i++, contadorRegistroVO.getTipoRegistro().toString());
                ps.setString(i++, contadorRegistroVO.getCodigoSubseccion());
                ps.setString(i++, contadorRegistroVO.getAnho());
                ps.setInt(i++, (valor +1));
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
            return new Integer(valor + 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void delete(ContadorRegistroValueObject contadorRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("ContadorRegistroDAO.delete");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, contadorRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, contadorRegistroVO.getCodigoSubseccion());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void modify(ContadorRegistroValueObject contadorRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{

            m_Log.info("ContadorRegistroDAO.modify");

            sql = "UPDATE " + nombreTabla + " SET " + numero + "=? WHERE " + tipoRegistro + "=? AND " +
                    codigoSubseccion + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, contadorRegistroVO.getNumero().intValue());
            ps.setString(i++, contadorRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, contadorRegistroVO.getCodigoSubseccion());
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
                    + " FROM " + nombreTabla;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            int i = 1;
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                contadores.add(new ContadorRegistroValueObject(tipoReg, codSub, num, año));
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