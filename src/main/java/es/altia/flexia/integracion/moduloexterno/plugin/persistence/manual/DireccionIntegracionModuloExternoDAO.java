package es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual;

import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import org.apache.log4j.Logger;


public class DireccionIntegracionModuloExternoDAO {
    private static DireccionIntegracionModuloExternoDAO instance = null;
    private Logger log = Logger.getLogger(DireccionIntegracionModuloExternoDAO.class);
  

   public static DireccionIntegracionModuloExternoDAO getInstance(){
        if(instance==null)
            instance = new DireccionIntegracionModuloExternoDAO();

        return instance;
    }
    
    protected DireccionIntegracionModuloExternoDAO() {}
     /**
     * Busca el domicilio cuyos datos se indican en el VO
     * @param dir
     * @param con
     * @return El codigo de domicilio si se encuentra, -1 en otro caso
     * @throws java.sql.SQLException
     */
    public int buscarDireccion(DomicilioInteresadoModuloIntegracionVO dir, Connection con)
            throws SQLException {

        String sql;
        int codigoDomicilio = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (log.isDebugEnabled()) log.debug("->DireccionDAO.buscarDireccion");
        String codPais = dir.getIdPais();
        String codProv = dir.getIdProvincia();
        String codMun = dir.getIdMunicipio();
        Integer codPaisInt=Integer.parseInt(codPais);
        Integer codProvInt=Integer.parseInt(codProv);
        Integer codMunInt=Integer.parseInt(codMun);
        
        try {
            // Comprobar si existe la via en T_VIA
            ViaIntegracionModuloExternoDAO viaDAO = new ViaIntegracionModuloExternoDAO();
            int codigoVia = -1;
            if (dir.getDescripcionVia()!=null && !"".equals(dir.getDescripcionVia()))
            codigoVia = viaDAO.buscarVia(codPaisInt, codProvInt,
                   codMunInt, dir.getTipoVia(), dir.getDescripcionVia(), con);

            if (codigoVia == -1) {
                if (log.isDebugEnabled()) log.debug("No existe la via");
                return -1;
            } else {
                if (log.isDebugEnabled()) log.debug("Existe la via con codigo " + codigoVia);
            }

            // Buscar domicilio
            sql = "SELECT DNN_DOM " +
                  "FROM T_DNN " +
                  "WHERE DNN_PAI = " + codPais +
                  " AND DNN_PRV = " + codProv +
                  " AND DNN_MUN = " + codMun;

            

              if (codigoVia!= -1)
                sql += " AND DNN_VIA = " +codigoVia;
            else
                sql += " AND DNN_VIA IS NULL";


             if (dir.getTipoVia() != -1)
                sql += " AND DNN_TVI = " + dir.getTipoVia();
            else
                sql += " AND DNN_TVI IS NULL";


            if (dir.getBloque()!=null)
                sql += " AND DNN_BLQ = ?";
            else
                sql += " AND DNN_BLQ IS NULL";

            if (dir.getPortal()!=null)
                sql += " AND DNN_POR = ?";
            else
                sql += " AND DNN_POR IS NULL";

            if (dir.getEscalera()!=null)
                sql += " AND DNN_ESC = ?";
            else
                sql += " AND DNN_ESC IS NULL";

            if (dir.getPlanta()!=null)
                sql += " AND DNN_PLT = ?";
            else
                sql += " AND DNN_PLT IS NULL";

            if (dir.getPuerta()!=null)
                sql += " AND DNN_PTA = ?";
            else
                sql += " AND DNN_PTA IS NULL";

            if (dir.getCodigoPostal()!=null)
                sql += " AND DNN_CPO = ?";
            else
                sql += " AND DNN_CPO IS NULL";

            sql += " AND DNN_SIT = 'A'";
            if (log.isDebugEnabled()) log.debug(sql);

            ps = con.prepareStatement(sql);

            int i = 1;
           
            if (dir.getBloque()!=null) ps.setString(i++, dir.getBloque());
            if (dir.getPortal()!=null) ps.setString(i++, dir.getPortal());
            if (dir.getEscalera()!=null) ps.setString(i++, dir.getEscalera());
            if (dir.getPlanta()!=null) ps.setString(i++, dir.getPlanta());
            if (dir.getPuerta()!=null) ps.setString(i++, dir.getPuerta());
            if (dir.getCodigoPostal()!=null) ps.setString(i++, dir.getCodigoPostal());

            rs = ps.executeQuery();

            // Obtener resultado
            if (rs.next()) {
                codigoDomicilio = rs.getInt("DNN_DOM");
                if (log.isDebugEnabled()) log.debug("Domicilio encontrado: " + codigoDomicilio);
            } else {
                if (log.isDebugEnabled()) log.debug("Domicilio no encontrado");
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return codigoDomicilio;
    }

    /**
     * Inserta un domicilio nuevo
     * @param dir
     * @param con
     * @return El codigo del nuevo domicilio
     * @throws java.sql.SQLException
     */
    public int altaDireccion(DomicilioInteresadoModuloIntegracionVO dir, int usuarioAlta, Connection con)
            throws SQLException {

        String sql;
        int codigoDomicilio = 1;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        if (log.isDebugEnabled()) log.debug("->DireccionIntegracionModuloExternoDAO.altaDireccion");

        String codPais = dir.getIdPais();
        Integer codPaisInt=Integer.parseInt(codPais);
        String codProv = dir.getIdProvincia();
        Integer codProvInt=Integer.parseInt(codProv);
        String codMun = dir.getIdMunicipio();
        Integer codMunInt=Integer.parseInt(codMun);

        try {
            // Buscamos la via y la damos de alta si no existe
            ViaIntegracionModuloExternoDAO viaDAO = new ViaIntegracionModuloExternoDAO();
            int codigoVia = -1;

            if (dir.getDescripcionVia()!=null  && dir.getTipoVia()!=-1)
                codigoVia = viaDAO.buscarVia(codPaisInt, codProvInt, codMunInt, dir.getTipoVia(), dir.getDescripcionVia(), con);

            if (codigoVia == -1 && dir.getDescripcionVia()!=null  && dir.getTipoVia()!=-1) {
                codigoVia = viaDAO.altaVia(codPaisInt, codProvInt, codMunInt, dir.getTipoVia(), dir.getDescripcionVia(),usuarioAlta, con);
                if (log.isDebugEnabled()) log.debug("No existe la via. Creada nueva via con codigo " + codigoVia);
            } else {
                if (log.isDebugEnabled()) log.debug("Existe la via con codigo " + codigoVia);
            }

            // Insertamos el codigo postal (no se inserta si ya existe)
            CodigoPostalModuloIntegracionExternoDAO codigoPostalModuloIntegracionExternoDAO = new CodigoPostalModuloIntegracionExternoDAO();
            if (dir.getCodigoPostal()!=null && !"".equals(dir.getCodigoPostal().trim()))
                codigoPostalModuloIntegracionExternoDAO.altaCodPostal(codPaisInt, codProvInt, codMunInt, dir.getCodigoPostal(), con);

            // Codigo para el nuevo domicilio
            sql = "SELECT MAX(DOM_COD) FROM T_DOM";
            if (log.isDebugEnabled()) log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                codigoDomicilio = rs.getInt(1);
                codigoDomicilio++;
            }
            rs.close();
            if (log.isDebugEnabled()) log.debug("Codigo para el nuevo domicilio: " + codigoDomicilio);

            // Insercion en T_DOM
            sql = "INSERT INTO T_DOM (DOM_COD, DOM_NML) VALUES (" + codigoDomicilio + ", 2)";
            if (log.isDebugEnabled()) log.debug(sql);
            st.executeUpdate(sql);
            st.close();

            
            sql = 
                "INSERT INTO T_DNN(DNN_DOM,  DNN_PAI, DNN_PRV, DNN_MUN, DNN_VPA, " +
                                  " DNN_VPR, DNN_VMU, DNN_VIA, " +  
                                  " DNN_BLQ, DNN_POR, DNN_ESC, DNN_PLT ,DNN_PTA, " + 
                                  " DNN_CPO, DNN_SIT, DNN_FAL, DNN_UAL)" +
                "VALUES (" + codigoDomicilio + ", " +
                    codPais + ", " + codProv + ", " + codMun + ", " +
                    codPais + ", " + codProv + ", " + codMun + ",? " +
                    ", ?, ?, ?, ?, ?, "
                    +" ?, 'A', ?, " + usuarioAlta + ")";

            if (log.isDebugEnabled()) log.debug(sql);
            ps = con.prepareStatement(sql);
             
           int i = 1;
         

            //DNN_VIA
            if (codigoVia!= -1) ps.setInt(i++, codigoVia);
            else ps.setNull(i++, Types.INTEGER);
     
            // DNN_BLQ
            if (dir.getBloque()!=null) ps.setString(i++, dir.getBloque());
            else ps.setNull(i++, Types.VARCHAR);
            // DNN_POR
            if (dir.getPortal()!=null) ps.setString(i++, dir.getPortal());
            else ps.setNull(i++, Types.VARCHAR);
            // DNN_ESC
            if (dir.getEscalera()!=null) ps.setString(i++, dir.getEscalera());
            else ps.setNull(i++, Types.VARCHAR);
            // DNN_PLT
            if (dir.getPlanta()!=null) ps.setString(i++, dir.getPlanta());
            else ps.setNull(i++, Types.VARCHAR);
            // DNN_PTA
            if (dir.getPuerta()!=null) ps.setString(i++, dir.getPuerta());
            else ps.setNull(i++, Types.VARCHAR);
            
            if (dir.getCodigoPostal()!=null) ps.setString(i++, dir.getCodigoPostal());
            else ps.setNull(i++, Types.VARCHAR);
            // DNN_FAL
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(i++, ahora);
            
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) st.close();
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
     log.debug("DireccionIntegracionModuloExternoDAO.AltaDirección:codigoDomicilio:"+codigoDomicilio);
        return codigoDomicilio;
    }

   

       
}//class