/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.dao;

import es.altia.agora.business.registro.sir.vo.SirOficina;
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
public class SirOficinaDAO {
    
    private static final Logger log = Logger.getLogger(SirOficinaDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final SirMappingVoDAO sirMappingVoDao = new SirMappingVoDAO();
    
    public SirOficina getSirOficinaByCodigo(String codigo, Connection con) throws Exception {
        log.info("getSirOficinaByCodigo - Begin () " + formatDate.format(new Date()));
        SirOficina retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_OFICINA "
                    + " WHERE codigo=? "
                   ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setString(1, codigo);
            log.info("Param ? : " + codigo
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = sirMappingVoDao.getSirOficina(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirOficina ... " + ex.getMessage(), ex);
            throw new Exception(ex);
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
                throw new Exception(e);
            }
        }
        log.info("getSirOficinaByCodigo - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<SirOficina> getAllSirOficina(Connection con) throws Exception {
        log.info("getAllSirOficina - Begin () " + formatDate.format(new Date()));
        List<SirOficina> retorno = new ArrayList<SirOficina>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_OFICINA "
                    ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(sirMappingVoDao.getSirOficina(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirOficina ... " + ex.getMessage(), ex);
            throw new Exception(ex);
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
                throw new Exception(e);
            }
        }
        log.info("getAllSirOficina - End () " + formatDate.format(new Date()));
        return retorno;
    }
}
