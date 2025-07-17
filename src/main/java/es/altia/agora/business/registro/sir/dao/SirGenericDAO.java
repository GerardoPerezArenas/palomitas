/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class SirGenericDAO {
    
    private static final Logger log = Logger.getLogger(SirGenericDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final SirMappingVoDAO sirMappingVoDao = new SirMappingVoDAO();
    
    public List<GeneralComboVO> getProvinciasXComunidadGeneralCombo(String codigoComunidadAutonoma, Connection con) throws Exception{
        log.info("getProvinciasXComunidadGeneralCombo - Begin () " + formatDate.format(new Date()));
        List<GeneralComboVO> retorno = new ArrayList<GeneralComboVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " select PRV_COD codigo , PRV_NOL descripcion from " + GlobalNames.ESQUEMA_GENERICO + "t_prv where prv_aut=? "
                    + " order by NLSSORT(descripcion, 'NLS_SORT=spanish') asc ";
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setString(1, codigoComunidadAutonoma);
            log.info("Param ? : " + codigoComunidadAutonoma
            );
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(sirMappingVoDao.getGeneralComboVO(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity GeneralComboVO ... " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw e;
            }
        }
        log.info("getProvinciasXComunidadGeneralCombo - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<GeneralComboVO> getListaComboComunidadAutonoma(Connection con) throws Exception{
        log.info("getListaComboComunidadAutonoma - Begin () " + formatDate.format(new Date()));
        List<GeneralComboVO> retorno = new ArrayList<GeneralComboVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " select aut_COD codigo , aut_NOL descripcion from " + GlobalNames.ESQUEMA_GENERICO + "t_aut "
                    + " order by NLSSORT(descripcion, 'NLS_SORT=spanish') asc ";
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(sirMappingVoDao.getGeneralComboVO(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity GeneralComboVO ... " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw e;
            }
        }
        log.info("getListaComboComunidadAutonoma - End () " + formatDate.format(new Date()));
        return retorno;
    }
}
