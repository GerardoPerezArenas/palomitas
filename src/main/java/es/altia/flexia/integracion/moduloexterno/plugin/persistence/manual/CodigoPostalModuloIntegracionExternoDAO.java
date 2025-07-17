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


public class CodigoPostalModuloIntegracionExternoDAO {
    private static CodigoPostalModuloIntegracionExternoDAO instance = null;
    private Logger log = Logger.getLogger(CodigoPostalModuloIntegracionExternoDAO.class);
  

   public static CodigoPostalModuloIntegracionExternoDAO getInstance(){
        if(instance==null)
            instance = new CodigoPostalModuloIntegracionExternoDAO();

        return instance;
    }
    
    protected CodigoPostalModuloIntegracionExternoDAO() {}
    /**
     * Inserta un codigo postal, si no existe ya.
     * @param codPais
     * @param codProvincia
     * @param codMunicipio
     * @param codPostal
     * @param con
     * @throws java.sql.SQLException
     */
    public void altaCodPostal(int codPais, int codProvincia, int codMunicipio,
            String codPostal, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";

        try {

          // Comprobamos si ya existe
          sql = "SELECT CPO_COD " +
                "FROM T_CPO " +
                "WHERE CPO_PAI = " + codPais + " AND CPO_PRV = " + codProvincia +
                " AND CPO_MUN = " + codMunicipio + " AND CPO_COD = ?";

          if (log.isDebugEnabled()) log.debug("->CodPostalDAO.altaCodPostal");
          if (log.isDebugEnabled()) log.debug(sql);

          ps = con.prepareStatement(sql);
          int i = 1;
          ps.setString(i++, codPostal);

          rs = ps.executeQuery();
          boolean existe = rs.next();
          rs.close();
          ps.close();

          // Se inserta si no existe
          if (!existe) {
              sql = "INSERT INTO T_CPO (CPO_PAI, CPO_PRV, CPO_MUN, CPO_COD, CPO_DEF) " +
                    "VALUES (" + codPais + "," + codProvincia + "," + codMunicipio + ",?,0)";

              if (log.isDebugEnabled()) log.debug(sql);

              ps = con.prepareStatement(sql);
              i = 1;
              ps.setString(i++, codPostal);

              ps.executeUpdate();
              ps.close();
          }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }
       
}//class