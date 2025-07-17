package es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual;

import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.TransformacionAtributoSelect;import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TerceroModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.log4j.Logger;


public class ViaIntegracionModuloExternoDAO {
    private static ViaIntegracionModuloExternoDAO instance = null;
    private Logger log = Logger.getLogger(ViaIntegracionModuloExternoDAO.class);
  

   public static ViaIntegracionModuloExternoDAO getInstance(){
        if(instance==null)
            instance = new ViaIntegracionModuloExternoDAO();

        return instance;
    }
    
    protected ViaIntegracionModuloExternoDAO() {}
     /**
     * Busca una vía por su pais, provincia, municipio, tipo (numerico) y nombre.
     * @param codPais
     * @param codProvincia
     * @param codMunicipio
     * @param tipoVia
     * @param nombreVia
     * @param con
     * @return Si existe la vía devuelve su código, en otro caso devuelve -1.
     * @throws java.sql.SQLException
     */
    public int buscarVia(int codPais, int codProvincia, int codMunicipio,
            int tipoVia, String nombreVia, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        int codigoVia = -1;
        if (log.isDebugEnabled()) log.debug("->ViaIntegracionModuloExternoDAO.buscarVia");
        
        try {

          // Comprobamos si existe
          sql = "SELECT VIA_COD FROM T_VIA " +
                "WHERE (VIA_NOM LIKE ? OR VIA_NOC LIKE ?) " +
                "AND VIA_PAI = " + codPais +
                "AND VIA_PRV = " + codProvincia +
                "AND VIA_MUN = " + codMunicipio +
                "AND VIA_SIT = 'A'";

          if (log.isDebugEnabled()) log.debug(sql);

          ps = con.prepareStatement(sql);
          int i = 1;
          ps.setString(i++, nombreVia.toUpperCase());
          ps.setString(i++, nombreVia.toUpperCase());

          rs = ps.executeQuery();
          if (rs.next()) {
              codigoVia = rs.getInt("VIA_COD");
          }
          rs.close();
          ps.close();
          
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return codigoVia;
    }

    /**
     * Da de alta una via.
     * @param codPais
     * @param codProvincia
     * @param codMunicipio
     * @param tipoVia
     * @param nombreVia
     * @param con
     * @return El código de la nueva vía.
     * @throws java.sql.SQLException
     */
    public int altaVia(int codPais, int codProvincia, int codMunicipio,
            int tipoVia, String nombreVia,int usuarioAlta, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        int nuevoCodigoVia = 1;
        if (log.isDebugEnabled()) log.debug("->ViaDAO.altaVia");

        try {
            // Primero obtenemos el nuevo codigo de via.
            sql = "SELECT MAX(VIA_COD) FROM T_VIA " +
                  "WHERE VIA_PAI = " + codPais +
                  " AND VIA_PRV = " + codProvincia +
                  " AND VIA_MUN = " + codMunicipio;

            if (log.isDebugEnabled()) log.debug(sql);

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                nuevoCodigoVia = rs.getInt(1) + 1;
            }
            rs.close();
            ps.close();

            // Una vez obtenido el nuevo codigo de via, insertamos.
            String nombreCortoVia = (nombreVia.length() > 25) ?
                                    nombreVia.substring(0, 25) :
                                    nombreVia;

           
            sql = "INSERT INTO T_VIA (VIA_PAI, VIA_PRV, VIA_MUN, VIA_COD, VIA_CIN, VIA_NOM, " +
                      "VIA_NOC, VIA_TVI, VIA_SIT, VIA_FAL, VIA_UAL, VIA_FBJ, VIA_UBJ, VIA_FIV" +
                    ") VALUES (" +
                    codPais + ", " + codProvincia + ", " + codMunicipio + ", " +
                    nuevoCodigoVia + ", " + nuevoCodigoVia + ", ?, ?, " + tipoVia + ", " +
                    "'A', ?, " +usuarioAlta + ", ?, ?, ?)";

            if (log.isDebugEnabled()) log.debug(sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setString(i++, nombreVia.toUpperCase()); // VIA_NOM
            ps.setString(i++, nombreCortoVia.toUpperCase()); // VIA_NOC
            ps.setTimestamp(i++, ahora); // VIA_FAL
            ps.setNull(i++, Types.DATE); // VIA_FBJ
            ps.setNull(i++, Types.INTEGER); // VIA_UBJ
            ps.setTimestamp(i++, ahora); // VIA_FIV

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return nuevoCodigoVia;
    }

    public boolean existeTipoVia(int tipoVia, Connection con)
            throws SQLException {

        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        boolean existe = false;
        if (log.isDebugEnabled()) log.debug("->ViaIntegracionModuloExternoDAO.existeTipoVia");

        try {

          // Comprobamos si existe
          sql = "SELECT TVI_COD FROM T_TVI " +
                "WHERE TVI_COD = " + tipoVia;
          if (log.isDebugEnabled()) log.debug(sql);

          st = con.createStatement();
          rs = st.executeQuery(sql);

          if (rs.next()) {
              existe = true;
          }
          rs.close();
          st.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }

        return existe;
    }

    public boolean existeProvincia(int codPais, int codPrv, Connection con,String organizacion)
            throws SQLException {

        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        boolean existe = false;

         log.debug("******************* existe Provincia "+organizacion);

        if (log.isDebugEnabled()) log.debug("->ViaIntegracionModuloExternoDAO.existeProvincia");

        try {

          // Comprobamos si existe
          sql = "SELECT PRV_COD " +
                "FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PRV " +
                "WHERE PRV_PAI = " + codPais + " " +
                "AND PRV_COD = " + codPrv;
          if (log.isDebugEnabled()) log.debug(sql);

          st = con.createStatement();
          rs = st.executeQuery(sql);

          if (rs.next()) {
              existe = true;
          }
          rs.close();
          st.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }

        return existe;
    }


    public boolean existeMunicipio(int codPais, int codPrv, int codMun, Connection con,String organizacion)
            throws SQLException {

        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        boolean existe = false;
        if (log.isDebugEnabled()) log.debug("->ViaIntegracionModuloExternoDAO.existeMunicipio");

        try {

          // Comprobamos si existe
          sql = "SELECT MUN_COD " +
                "FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
                "WHERE MUN_PAI = " + codPais + " " +
                "AND MUN_PRV = " + codPrv + " " +
                "AND MUN_COD = " + codMun;
          if (log.isDebugEnabled()) log.debug(sql);

          st = con.createStatement();
          rs = st.executeQuery(sql);

          if (rs.next()) {
              existe = true;
          }
          rs.close();
          st.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }

        return existe;
    }
       
}//class