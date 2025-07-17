/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.registro.digitalizacion.lanbide.persistence.manual;

import es.altia.flexia.registro.digitalizacion.lanbide.vo.MelanbideDokusiPlateaPro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class MelanbideDokusiPlateaProDAO {
    
    private static final Logger log = Logger.getLogger(MelanbideDokusiPlateaProDAO.class);
    private static final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public MelanbideDokusiPlateaPro getMelanbideDokusiPlateaProById(int id,Connection con) throws SQLException, Exception {
        log.info(" getMelanbideDokusiPlateaProById - Begin " + id + " " + formatFechaLog.format(new Date()));
        MelanbideDokusiPlateaPro resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + "    from Melanbide_Dokusi_PlateaPro "
                    + "    where "
                    + "    id=? "
                ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int params = 1;
            ps.setInt(params++, id);
            log.info("params = " + id
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = new MelanbideDokusiPlateaPro(
                        rs.getInt("id"),
                        rs.getString("codigoProcedimientoFlexia"),
                        rs.getString("codigoProcedimientoPlatea"),
                        rs.getDate("fecha_creacion")
                );
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException getMelanbideDokusiPlateaProById ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Exception getMelanbideDokusiPlateaProById ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getMelanbideDokusiPlateaProById - End - " + formatFechaLog.format(new Date())
                + (resultado != null ? " " + resultado.toString() : "")
        );
        return resultado;
    }
    
    public MelanbideDokusiPlateaPro getMelanbideDokusiPlateaProByCodProcedimientoFlexia(String codigoProcedimientoFlexia,Connection con) throws SQLException, Exception {
        log.info(" getMelanbideDokusiPlateaProByCodProcedimientoFlexia - Begin " + codigoProcedimientoFlexia + " " + formatFechaLog.format(new Date()));
        MelanbideDokusiPlateaPro resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + "    from Melanbide_Dokusi_PlateaPro "
                    + "    where "
                    + "    codigoProcedimientoFlexia=? "
                ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int params = 1;
            ps.setString(params++, codigoProcedimientoFlexia);
            log.info("params = " + codigoProcedimientoFlexia
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = new MelanbideDokusiPlateaPro(
                        rs.getInt("id"),
                        rs.getString("codigoProcedimientoFlexia"),
                        rs.getString("codigoProcedimientoPlatea"),
                        rs.getDate("fecha_creacion")
                );
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException getMelanbideDokusiPlateaProByCodProcedimientoFlexia ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Exception getMelanbideDokusiPlateaProByCodProcedimientoFlexia ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getMelanbideDokusiPlateaProByCodProcedimientoFlexia - End - " + formatFechaLog.format(new Date()) 
                +  (resultado!=null?" " + resultado.toString():"")
        );
        return resultado;
    }
    
    /**
     * Devuelve una lista de procedimiento de flexia asociados a un procedimiento de Platea
     * @param codigoProcedimientoPlatea
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public List<MelanbideDokusiPlateaPro> getMelanbideDokusiPlateaProByCodProcedimientoPlatea(String codigoProcedimientoPlatea,Connection con) throws SQLException, Exception {
        log.info(" getMelanbideDokusiPlateaProByCodProcedimientoPlatea - Begin " + codigoProcedimientoPlatea + " " + formatFechaLog.format(new Date()));
        List<MelanbideDokusiPlateaPro> resultado = new ArrayList<MelanbideDokusiPlateaPro>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + "    from Melanbide_Dokusi_PlateaPro "
                    + "    where "
                    + "    codigoProcedimientoPlatea=? "
                ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int params = 1;
            ps.setString(params++, codigoProcedimientoPlatea);
            log.info("params = " + codigoProcedimientoPlatea
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(new MelanbideDokusiPlateaPro(
                        rs.getInt("id"),
                        rs.getString("codigoProcedimientoFlexia"),
                        rs.getString("codigoProcedimientoPlatea"),
                        rs.getDate("fecha_creacion")
                ));
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException getMelanbideDokusiPlateaProByCodProcedimientoPlatea ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Exception getMelanbideDokusiPlateaProByCodProcedimientoPlatea ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getMelanbideDokusiPlateaProByCodProcedimientoPlatea - End - " + formatFechaLog.format(new Date())
                +  " " + Arrays.toString(resultado.toArray())
        );
        return resultado;
    }
    
    public List<MelanbideDokusiPlateaPro> getTodosMelanbideDokusiPlateaPro(Connection con) throws SQLException, Exception {
        log.info(" getTodosMelanbideDokusiPlateaPro - Begin " + " " + formatFechaLog.format(new Date()));
        List<MelanbideDokusiPlateaPro> resultado = new ArrayList<MelanbideDokusiPlateaPro>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + "    from Melanbide_Dokusi_PlateaPro "
                ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(new MelanbideDokusiPlateaPro(
                        rs.getInt("id"),
                        rs.getString("codigoProcedimientoFlexia"),
                        rs.getString("codigoProcedimientoPlatea"),
                        rs.getDate("fecha_creacion")
                ));
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException getTodosMelanbideDokusiPlateaPro ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Exception getTodosMelanbideDokusiPlateaPro ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getTodosMelanbideDokusiPlateaPro - End - " + formatFechaLog.format(new Date())
                + " " + Arrays.toString(resultado.toArray())
        );
        return resultado;
    }
    
}
