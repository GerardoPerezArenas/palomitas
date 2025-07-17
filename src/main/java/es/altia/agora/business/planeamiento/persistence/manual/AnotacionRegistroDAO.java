package es.altia.agora.business.planeamiento.persistence.manual;

import es.altia.agora.business.planeamiento.AnotacionRegistroValueObject;
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

public class AnotacionRegistroDAO {

    //Para el fichero de configuracion tecnico.
    private static Config m_ConfigTechnical;
    //Para informacion de logs.
    private static Log m_Log = LogFactory.getLog(AnotacionRegistroDAO.class.getName());


    private static AnotacionRegistroDAO instance = null;
    //Nombre de Tabla y nombres de campos
    private static String nombreTabla = null;
    private static String tipoRegistro = null;
    private static String codigoSubseccion = null;
    private static String anho = null;
    private static String numero = null;
    private static String numeroAnotacion = null;
    private static String fechaAnotacion = null;
    private static String observaciones = null;

    protected AnotacionRegistroDAO() {
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados

        nombreTabla = m_ConfigTechnical.getString("SQL.U_REA.Tabla");
        tipoRegistro = m_ConfigTechnical.getString("SQL.U_REA.TipoRegistro");
        codigoSubseccion = m_ConfigTechnical.getString("SQL.U_REA.CodigoSubseccion");
        anho = m_ConfigTechnical.getString("SQL.U_REA.Anho");
        numero = m_ConfigTechnical.getString("SQL.U_REA.Numero");
        numeroAnotacion = m_ConfigTechnical.getString("SQL.U_REA.NumeroAnotacion");
        fechaAnotacion = m_ConfigTechnical.getString("SQL.U_REA.FechaAnotacion");
        observaciones = m_ConfigTechnical.getString("SQL.U_REA.Observaciones");
    }

    public static AnotacionRegistroDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(AnotacionRegistroDAO.class){
                if (instance == null)
                    instance = new AnotacionRegistroDAO();
            }
        }
        return instance;
    }

    public void create(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion, String[] params)
            throws BDException {

        ResultSet rs = null;
        String sql = "";
        AdaptadorSQLBD abd = null;
        try{
            abd = new AdaptadorSQLBD(params);
            m_Log.info("AnotacionRegistroDAO.create");

            sql = "SELECT " + abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{numeroAnotacion})+
                  " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " +
                    codigoSubseccion + "=? AND " + anho + "=? AND " + numero + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, anotacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, anotacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, anotacionRegistroVO.getAnho());
            ps.setInt(i++, anotacionRegistroVO.getNumero().intValue());
            rs = ps.executeQuery();
            int valor = 0;
            while(rs.next()){
                valor = rs.getInt(1);
            }

            sql = "INSERT INTO " + nombreTabla + "(" + tipoRegistro + "," + codigoSubseccion + "," + anho + "," +
                    numero + "," + numeroAnotacion + "," + fechaAnotacion + "," + observaciones +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?)";

            ps.close();
            ps = conexion.prepareStatement(sql);
            i = 1;
            ps.setString(i++, anotacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, anotacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, anotacionRegistroVO.getAnho());
            ps.setInt(i++, anotacionRegistroVO.getNumero().intValue());
            ps.setString(i++, (new Integer(valor+1)).toString());
            ps.setDate(i++, new Date(anotacionRegistroVO.getFechaAnotacion().getTimeInMillis()));
            ps.setString(i++, anotacionRegistroVO.getObservaciones());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void delete(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("AnotacionRegistroDAO.delete");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " +
                    anho + "=? AND " + numero + "=? AND " + numeroAnotacion + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, anotacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, anotacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, anotacionRegistroVO.getAnho());
            ps.setInt(i++, anotacionRegistroVO.getNumero().intValue());
            ps.setString(i++, anotacionRegistroVO.getNumeroAnotacion());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void deleteByRegistro(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("AnotacionRegistroDAO.deleteByRegistro");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " +
                    anho + "=? AND " + numero + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, anotacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, anotacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, anotacionRegistroVO.getAnho());
            ps.setInt(i++, anotacionRegistroVO.getNumero().intValue());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void modify(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{

            m_Log.info("AnotacionRegistroDAO.modify");

            sql = "UPDATE " + nombreTabla + " SET " + fechaAnotacion + "=?, " + observaciones + "=? WHERE " +
                    tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " + anho + "=? AND "
                    + numero + "=? AND " + numeroAnotacion + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setDate(i++, new Date(anotacionRegistroVO.getFechaAnotacion().getTimeInMillis()));
            ps.setString(i++, anotacionRegistroVO.getObservaciones());
            ps.setString(i++, anotacionRegistroVO.getTipoRegistro().toString());
            ps.setString(i++, anotacionRegistroVO.getCodigoSubseccion());
            ps.setString(i++, anotacionRegistroVO.getAnho());
            ps.setInt(i++, anotacionRegistroVO.getNumero().intValue());
            ps.setString(i++, anotacionRegistroVO.getNumeroAnotacion());

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

                anotaciones.add(new AnotacionRegistroValueObject(tipoReg, codSub, num, año, numAnot, fecAnot,
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

                anotaciones.add(new AnotacionRegistroValueObject(tipoReg, codSub, num, año, numAnot, fecAnot,
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



      public String getNombreUORRegistro(String tipoEntrada,int ejercicio,Long numero,int codDepto,String[] params) {
        String nombre = null;
        AdaptadorSQLBD adapt = null;
        Connection con =null;
        Statement st = null;
        ResultSet rs = null;
        try{
            adapt =new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if(con!=null){
                String sql ="SELECT UOR_COD_VIS,UOR_NOM FROM R_RES,A_UOR WHERE RES_TIP='" + tipoEntrada + "' AND RES_EJE=" + ejercicio +
                            " AND RES_NUM=" + numero + " AND RES_DEP=" + codDepto + " AND RES_UOR=A_UOR.UOR_COD";

                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    nombre = rs.getString("UOR_NOM");
                }
            }


        }catch(BDException e){
            e.printStackTrace();
        }
        catch(SQLException e){
            m_Log.error(" " + e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }

            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }
        return nombre;
    }
      
    public String getOficinaUorRegistro (String tipoEntrada,int ejercicio,Long numero,int codDepto,String[] params){
        if(m_Log.isDebugEnabled()) m_Log.debug("getOficinaRegistro( tipoEntrada = " + tipoEntrada + " ejercicio = " + ejercicio + " numero = " + numero + ") : BEGIN");
        String nombre = null;
        AdaptadorSQLBD adapt = null;
        Connection con =null;
        Statement st = null;
        ResultSet rs = null;
        try{
            if(m_Log.isDebugEnabled()) m_Log.debug("Creamos la conexión a la BBDD");
            adapt =new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            if(con!=null){
                String sql ="SELECT UOR_COD_VIS,UOR_NOM FROM R_RES,A_UOR WHERE RES_TIP='" + tipoEntrada + "' AND RES_EJE=" + ejercicio +
                                " AND RES_NUM=" + numero + " AND RES_DEP=" + codDepto + " AND RES_OFI=A_UOR.UOR_COD";
                m_Log.debug(" ==== sql = " + sql);
                m_Log.debug(" ===== Creamos el statement y ejecutamos la conexión");
                st = con.createStatement();
                rs = st.executeQuery(sql);
                m_Log.debug(" ====== Recorremos el resultset con los resultados");
                while(rs.next()){
                    nombre = rs.getString("UOR_NOM");
                }//while(rs.next())
                
                m_Log.debug(" ======= nombre uor: " + nombre);
            }//if(con!=null)
            if(nombre == null){
                if(m_Log.isDebugEnabled()) m_Log.debug("La consulta no ha devuelto ningún resultado");
                nombre = " ";
            }//if(nombre == null)
        }catch(BDException e){
            m_Log.error("Se ha producido un error BBDD recuperando la oficina de registro ", e);
        }catch(SQLException e){
            m_Log.error("Se ha producido un error SQL recuperando la oficina de registro ", e);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
               m_Log.error("Se ha producido un error cerrando el statement y el resultset ", e);
            }//try-catch
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Devolvemos la conexión al adaptador");
                adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Se ha producido un error devolviendo la conexión " , e);
            }//try-catch
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug(" ======== getOficinaRegistro() : END");
        if(m_Log.isDebugEnabled()) m_Log.debug(" ======= nombre = " + nombre);
        return nombre;
    }//getNombreUORRegistro
    
}//class