/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.dao;

import es.altia.agora.business.registro.sir.vo.SirLocalidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author INGDGC
 */
public class SirLocalidadesDAO {
    
    private static final Logger log = Logger.getLogger(SirLocalidadesDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final SirMappingVoDAO sirMappingVoDao = new SirMappingVoDAO();

    private static final String codigoEntidadForLanbide = "01";
    
    public SirLocalidades getSirLocalidadesByCodigoProvinciaCodFlexia(String codigoProvincia, String codLocalidadFlexia, Connection con) throws Exception {
        log.info("getSirLocalidadesByCodigoProvinciaCodFlexia - Begin () " + formatDate.format(new Date()));
        SirLocalidades retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_LOCALIDADES "
                    + " WHERE COD_ENTIDAD='"+codigoEntidadForLanbide+"'"
                    + " and COD_PROVINCIA=? "
                    + " and COD_LOCALIDAD_FLEXIA=? "
                   ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setString(1, codigoProvincia);
            pt.setString(2, codLocalidadFlexia);
            log.info("Param ? : " + codigoProvincia
                    + ", " + codLocalidadFlexia
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = sirMappingVoDao.getSirLocalidades(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirLocalidades ... " + ex.getMessage(), ex);
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
        log.info("getSirLocalidadesByCodigoProvinciaCodFlexia - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<SirLocalidades> getAllSirLocalidades(Connection con) throws Exception {
        log.info("getAllSirLocalidades - Begin () " + formatDate.format(new Date()));
        List<SirLocalidades> retorno = new ArrayList<SirLocalidades>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM COD_LOCALIDAD "
                    ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(sirMappingVoDao.getSirLocalidades(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirLocalidades ... " + ex.getMessage(), ex);
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
        log.info("getAllSirLocalidades - End () " + formatDate.format(new Date()));
        return retorno;
    }

    public SirLocalidades getSirLocalidadesByPKEntidadProvinciaLocalidad(String codigoEntidad,String codigoProvincia, String codLocalidad, Connection con) throws Exception {
        log.info("getSirLocalidadesByPKEntidadProvinciaLocalidad - Begin () " + formatDate.format(new Date()));
        SirLocalidades retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_LOCALIDADES "
                    + " WHERE "
                    + " and COD_ENTIDAD = ? "
                    + " and COD_PROVINCIA=? "
                    + " and COD_LOCALIDAD_FLEXIA=? "
                    ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setString(1, codigoEntidad);
            pt.setString(2, codigoProvincia);
            pt.setString(3, codLocalidad);
            log.info("Param ? : "  + codigoEntidad
                    + ", " + codigoProvincia
                    + ", " + codLocalidad
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = sirMappingVoDao.getSirLocalidades(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirLocalidades ... " + ex.getMessage(), ex);
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
        log.info("getSirLocalidadesByPKEntidadProvinciaLocalidad - End () " + formatDate.format(new Date()));
        return retorno;
    }
}
