package es.altia.agora.business.planeamiento.persistence.manual;

import es.altia.agora.business.planeamiento.AnotacionRectificacionRegistroValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Vector;

public class AnotacionRectificacionRegistroDAO {

    //Para el fichero de configuracion tecnico.
    private static Config m_ConfigTechnical;
    //Para informacion de logs.
    private static Log m_Log = LogFactory.getLog(AnotacionRectificacionRegistroDAO.class.getName());


    private static AnotacionRectificacionRegistroDAO instance = null;
    //Nombre de Tabla y nombres de campos
    private static String nombreTabla = null;
    private static String tipoRegistro = null;
    private static String codigoSubseccion = null;
    private static String anho = null;
    private static String numero = null;
    private static String numeroAnotacion = null;
    private static String fechaAnotacion = null;
    private static String observaciones = null;

    protected AnotacionRectificacionRegistroDAO() {
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados

        nombreTabla = m_ConfigTechnical.getString("SQL.U_REAR.Tabla");
        tipoRegistro = m_ConfigTechnical.getString("SQL.U_REAR.TipoRegistro");
        codigoSubseccion = m_ConfigTechnical.getString("SQL.U_REAR.CodigoSubseccion");
        anho = m_ConfigTechnical.getString("SQL.U_REAR.Anho");
        numero = m_ConfigTechnical.getString("SQL.U_REAR.Numero");
        numeroAnotacion = m_ConfigTechnical.getString("SQL.U_REAR.NumeroAnotacion");
        fechaAnotacion = m_ConfigTechnical.getString("SQL.U_REAR.FechaAnotacion");
        observaciones = m_ConfigTechnical.getString("SQL.U_REAR.Observaciones");
    }

    public static AnotacionRectificacionRegistroDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(AnotacionRectificacionRegistroDAO.class){
                if (instance == null)
                    instance = new AnotacionRectificacionRegistroDAO();
            }
        }
        return instance;
    }

    public void create(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion, String[] params)
            throws BDException {

        ResultSet rs = null;
        String sql = "";
        AdaptadorSQLBD abd = null;

        try{
            abd = new AdaptadorSQLBD(params);
            m_Log.info("AnotacionRectificacionRegistroDAO.create");

            sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{numeroAnotacion})+
                  " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " +
                    codigoSubseccion + "=? AND " + anho + "=? AND " + numero + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, AnotacionRectificacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getAnho());
            ps.setInt(i++, AnotacionRectificacionRegistroVO.getNumero().intValue());
            rs = ps.executeQuery();
            int valor = 0;
            while(rs.next()){
                valor = rs.getInt(1);
            }

            sql = "INSERT INTO " + nombreTabla + "(" + tipoRegistro + "," + codigoSubseccion + "," +
                    anho + "," + numero + "," + numeroAnotacion + "," + fechaAnotacion + "," + observaciones +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?)";

            ps.close();
            ps = conexion.prepareStatement(sql);
            i = 1;
            ps.setString(i++, AnotacionRectificacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getAnho());
            ps.setInt(i++, AnotacionRectificacionRegistroVO.getNumero().intValue());
            ps.setString(i++, (new Integer(valor+1)).toString());
            ps.setDate(i++, new Date(AnotacionRectificacionRegistroVO.getFechaAnotacion().getTimeInMillis()));
            ps.setString(i++, AnotacionRectificacionRegistroVO.getObservaciones());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void delete(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("AnotacionRectificacionRegistroDAO.delete");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " +
                    anho + "=? AND " + numero + "=? AND " + numeroAnotacion + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, AnotacionRectificacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getAnho());
            ps.setInt(i++, AnotacionRectificacionRegistroVO.getNumero().intValue());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getNumeroAnotacion());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void deleteByRegistro(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("AnotacionRectificacionRegistroDAO.deleteByRegistro");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " +
                    anho + "=? AND " + numero + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, AnotacionRectificacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getAnho());
            ps.setInt(i++, AnotacionRectificacionRegistroVO.getNumero().intValue());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void modify(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{

            m_Log.info("AnotacionRectificacionRegistroDAO.modify");

            sql = "UPDATE " + nombreTabla + " SET " + fechaAnotacion + "=?, " + observaciones + "=? WHERE " +
                    tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " + anho + "=? AND "
                    + numero + "=? AND " + numeroAnotacion + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setDate(i++, new Date(AnotacionRectificacionRegistroVO.getFechaAnotacion().getTimeInMillis()));
            ps.setString(i++, AnotacionRectificacionRegistroVO.getObservaciones());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getAnho());
            ps.setInt(i++, AnotacionRectificacionRegistroVO.getNumero().intValue());
            ps.setString(i++, AnotacionRectificacionRegistroVO.getNumeroAnotacion());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public Vector findAll(String[] params) throws TechnicalException {

        Vector anotaciones = new Vector();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + anho + ", " + numero
                    + "," + numeroAnotacion + "," + fechaAnotacion + "," + observaciones + " FROM " + nombreTabla;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            int i = 1;
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numAnot = rs.getString(i++);
                Calendar fecAnot = Calendar.getInstance();
                fecAnot.setTime(rs.getDate(i++));
                String obs = rs.getString(i++);

                anotaciones.add(new AnotacionRectificacionRegistroValueObject(tipoReg, codSub, num, año, numAnot, fecAnot,
                        obs));
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
        return anotaciones;
    }

    public Vector findByRegistro(Character tipReg, String codSubsec, String ano, Integer numRegistro,
                                 String[] params) throws TechnicalException {

        Vector anotaciones = new Vector();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + anho + ", " + numero
                    + "," + numeroAnotacion + "," + fechaAnotacion + "," + observaciones + " FROM " + nombreTabla +
                    " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " + anho + "=? AND " + numero + "=?";

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
                String numAnot = rs.getString(i++);
                Calendar fecAnot = Calendar.getInstance();
                fecAnot.setTime(rs.getDate(i++));
                String obs = rs.getString(i++);

                anotaciones.add(new AnotacionRectificacionRegistroValueObject(tipoReg, codSub, num, año, numAnot, fecAnot,
                        obs));
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
        return anotaciones;
    }
}