/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.registro.digitalizacion.lanbide.persistence.manual;

import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class GeneralComboServiceDAO {
    
    Logger LOG = Logger.getLogger(GeneralComboServiceDAO.class);
    
    public List<GeneralComboVO> getComboProcedimiento(int codOrganizacion,Connection con)
            throws SQLException {
        LOG.info("getComboProcedimiento - Begin");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        List<GeneralComboVO> respuesta = new ArrayList<GeneralComboVO>();
        try {
            query = " SELECT PRO_COD, PRO_DES "
                    + " FROM E_PRO "
                    + " WHERE PRO_EST=1 "
                    + " AND PRO_MUN=? "
                    + " ORDER BY PRO_COD ";
            LOG.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrganizacion);
            LOG.info("Param ? : " + codOrganizacion);
            rs = ps.executeQuery();
            while (rs.next()) {
                GeneralComboVO elementoListaRetorno = new GeneralComboVO();
                elementoListaRetorno.setCodigo(rs.getString("PRO_COD"));
                elementoListaRetorno.setDescripcion(rs.getString("PRO_DES"));
                respuesta.add(elementoListaRetorno);
            }
        } catch (SQLException sqle) {
            LOG.error("ERROR al reuperar la lista de combo procedimiento ");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                LOG.error("Error al cerrar los recursos de la base de datos");
            }
            LOG.info("getComboProcedimiento - End");
        }
        return respuesta;
    }
}
