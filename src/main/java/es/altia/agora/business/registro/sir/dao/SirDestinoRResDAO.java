/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.dao;

import es.altia.agora.business.registro.sir.vo.SirDestinoRRes;
import es.altia.agora.business.registro.sir.vo.SirDestinoRResDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class SirDestinoRResDAO {
    
    private static final Logger log = Logger.getLogger(SirDestinoRResDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final SirMappingVoDAO sirMappingVoDao = new SirMappingVoDAO();

    public SirDestinoRRes getSirDestinoRResByPKRRes(int RES_DEP,int RES_UOR, String RES_TIP,int RES_EJE, int RES_NUM,  Connection con) throws Exception {
        log.info("getSirDestinoRResByPKRRes - Begin () " + formatDate.format(new Date()));
        SirDestinoRRes retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_DESTINO_R_RES "
                    + " WHERE RES_DEP=? "
                    + " and RES_UOR=? "
                    + " and RES_TIP=? "
                    + " and RES_EJE=? "
                    + " and RES_NUM=? "
                    ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParams = 1;
            pt.setInt(contParams++, RES_DEP);
            pt.setInt(contParams++, RES_UOR);
            pt.setString(contParams++, RES_TIP);
            pt.setInt(contParams++, RES_EJE);
            pt.setInt(contParams++, RES_NUM);
            log.info("Param ? : " + RES_DEP
                    +" , " +  RES_UOR
                    +" , " +  RES_TIP
                    +" , " +  RES_EJE
                    +" , " +  RES_NUM
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = sirMappingVoDao.getSirDestinoRRes(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("getSirDestinoRResByPKRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public SirDestinoRResDto getSirDestinoRResDTOByPKRRes(int idioma, int RES_DEP,int RES_UOR, String RES_TIP,int RES_EJE, int RES_NUM,  Connection con) throws Exception {
        log.info("getSirDestinoRResDTOByPKRRes - Begin () " + formatDate.format(new Date()));
        SirDestinoRResDto retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT SIR_DESTINO_R_RES.* "
                    + " ,case when 4=? then NOMBREUNIDAD_EU else NOMBREUNIDAD_ES end as nombreUnidad "
                    + " FROM SIR_DESTINO_R_RES "
                    + " LEFT JOIN sir_unidad_dir3 ON sir_unidad_dir3.codigoUnidad=sir_destino_r_res.codigounidad "
                    + " WHERE RES_DEP=? "
                    + " and RES_UOR=? "
                    + " and RES_TIP=? "
                    + " and RES_EJE=? "
                    + " and RES_NUM=? "
                    ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParams = 1;
            pt.setInt(contParams++, idioma);
            pt.setInt(contParams++, RES_DEP);
            pt.setInt(contParams++, RES_UOR);
            pt.setString(contParams++, RES_TIP);
            pt.setInt(contParams++, RES_EJE);
            pt.setInt(contParams++, RES_NUM);
            log.info("Param ? : " + idioma
                    +", " + RES_DEP
                    +", " +  RES_UOR
                    +", " +  RES_TIP
                    +", " +  RES_EJE
                    +", " +  RES_NUM
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                SirDestinoRRes retornoTemp = sirMappingVoDao.getSirDestinoRRes(rs);
                retorno = new SirDestinoRResDto(retornoTemp, rs.getString("nombreUnidad"));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("getSirDestinoRResDAOByPKRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public SirDestinoRRes getSirDestinoRResDAOByTipoEjercicioNumeroRegistro(String RES_TIP,int RES_EJE, int RES_NUM,  Connection con) throws Exception {
        log.info("getSirDestinoRResDAOByTipoEjercicioNumeroRegistro - Begin () " + formatDate.format(new Date()));
        SirDestinoRRes retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_DESTINO_R_RES "
                    + " WHERE RES_DEP=? "
                    + " and RES_UOR=? "
                    + " and RES_TIP=? "
                    + " and RES_EJE=? "
                    + " and RES_NUM=? "
                    ;

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParams = 1;
            pt.setString(contParams++, RES_TIP);
            pt.setInt(contParams++, RES_EJE);
            pt.setInt(contParams++, RES_NUM);
            log.info("Param ? : " + RES_TIP
                    +" , " +  RES_EJE
                    +" , " +  RES_NUM
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = sirMappingVoDao.getSirDestinoRRes(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("getSirDestinoRResDTOByPKRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }

    public List<SirDestinoRRes> getAllSirDestinoRRes(Connection con) throws Exception {
        log.info("getAllSirDestinoRRes - Begin () " + formatDate.format(new Date()));
        List<SirDestinoRRes> retorno = new ArrayList<SirDestinoRRes>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_DESTINO_R_RES "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(sirMappingVoDao.getSirDestinoRRes(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("getAllSirDestinoRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<SirDestinoRRes> getSirDestinoRResByDIR3Destino(String codigoUnidad, Connection con) throws Exception {
        log.info("getSirDestinoRResByDIR3Destino - Begin () " + formatDate.format(new Date()));
        List<SirDestinoRRes> retorno = new ArrayList<SirDestinoRRes>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_DESTINO_R_RES"
                    + " WHERE codigoUnidad=? "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParams = 1;
            pt.setString(contParams++, codigoUnidad);
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(sirMappingVoDao.getSirDestinoRRes(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("getSirDestinoRResByDIR3Destino - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public SirDestinoRRes createSirDestinoRRes(SirDestinoRRes dato, Connection con) throws Exception {
        log.info("createSirDestinoRRes - Begin () " + formatDate.format(new Date()));
        SirDestinoRRes retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            if (dato != null) {
                String query = " INSERT INTO "
                        + " SIR_DESTINO_R_RES "
                        + " (RES_DEP,RES_UOR,RES_TIP,RES_EJE,RES_NUM,codigoUnidad,oficinaRegistroSIR,numeroRegistroSIR,usuarioRegistroSIR,fechaRegistroSIR,fechaRegistroSistemaSIR )"
                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";

                log.info("sql = " + query);
                pt = con.prepareStatement(query);
                int contParams = 1;
                pt.setInt(contParams++, dato.getRES_DEP());
                pt.setInt(contParams++, dato.getRES_UOR());
                pt.setString(contParams++, dato.getRES_TIP());
                pt.setInt(contParams++, dato.getRES_EJE());
                pt.setInt(contParams++, dato.getRES_NUM());
                pt.setString(contParams++, (dato.getCodigoUnidad()!=null && !dato.getCodigoUnidad().isEmpty() && !dato.getCodigoUnidad().equalsIgnoreCase("null")?dato.getCodigoUnidad():null));
                pt.setString(contParams++, (dato.getOficinaRegistroSIR()!=null && !dato.getOficinaRegistroSIR().isEmpty() && !dato.getOficinaRegistroSIR().equalsIgnoreCase("null")?dato.getOficinaRegistroSIR():null));
                pt.setString(contParams++, (dato.getNumeroRegistroSIR()!=null && !dato.getNumeroRegistroSIR().isEmpty() && !dato.getNumeroRegistroSIR().equalsIgnoreCase("null")?dato.getNumeroRegistroSIR():null));
                pt.setString(contParams++, (dato.getUsuarioRegistroSIR()!=null && !dato.getUsuarioRegistroSIR().isEmpty() && !dato.getUsuarioRegistroSIR().equalsIgnoreCase("null")?dato.getUsuarioRegistroSIR():null));
                if(dato.getFechaRegistroSIR()!=null)
                    pt.setDate(contParams++, (new java.sql.Date(dato.getFechaRegistroSIR().getTime())));
                else
                    pt.setNull(contParams++,Types.DATE );
                if(dato.getFechaRegistroSistemaSIR()!=null)
                    pt.setDate(contParams++, (new java.sql.Date(dato.getFechaRegistroSistemaSIR().getTime())));
                else
                    pt.setNull(contParams++,Types.DATE );
                log.info("Param ? : " + dato.getRES_DEP()
                        + "," + dato.getRES_UOR()
                        + "," + dato.getRES_TIP()
                        + "," + dato.getRES_EJE()
                        + "," + dato.getRES_NUM()
                        + "," + dato.getCodigoUnidad()
                        + "," + dato.getOficinaRegistroSIR()
                        + "," + dato.getNumeroRegistroSIR()
                        + "," + dato.getUsuarioRegistroSIR()
                        + "," + (dato.getFechaRegistroSIR()!=null ? formatDate.format(dato.getFechaRegistroSIR()) : "")
                        + "," + (dato.getFechaRegistroSistemaSIR()!=null ? formatDate.format(dato.getFechaRegistroSistemaSIR()) : "")
                );
                if (pt.executeUpdate() > 0) {
                    retorno = this.getSirDestinoRResByPKRRes(dato.getRES_DEP(), dato.getRES_UOR(), dato.getRES_TIP(), dato.getRES_EJE(), dato.getRES_NUM(), con);
                    log.info("Insercion Correcta: " + dato.toString());
                } else {
                    log.error("No se ha podido realizar la insercion .... : " + dato.toString());
                }
            } else {
                log.error("No se puede insertar, datos a procesar recibidos a null");
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error Guardar entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("createSirDestinoRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public SirDestinoRRes updateCodigoUnidadSirDestinoRRes(SirDestinoRRes dato, Connection con) throws Exception {
        log.info("updateCodigoUnidadSirDestinoRRes - Begin () " + formatDate.format(new Date()));
        SirDestinoRRes retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            if (dato != null) {
                String query = " update "
                        + " SIR_DESTINO_R_RES "
                        + " set codigoUnidad=? "
                        + " where RES_DEP=? "
                        + " and RES_UOR=? "
                        + " and RES_TIP=? "
                        + " and RES_EJE=? "
                        + " and RES_NUM=? ";

                log.info("sql = " + query);
                pt = con.prepareStatement(query);
                int contParams = 1;                
                pt.setString(contParams++, dato.getCodigoUnidad());
                pt.setInt(contParams++, dato.getRES_DEP());
                pt.setInt(contParams++, dato.getRES_UOR());
                pt.setString(contParams++, dato.getRES_TIP());
                pt.setInt(contParams++, dato.getRES_EJE());
                pt.setInt(contParams++, dato.getRES_NUM());
                log.info("Param ? : " + dato.getCodigoUnidad()
                        + "," + dato.getRES_DEP()
                        + "," + dato.getRES_UOR()
                        + "," + dato.getRES_TIP()
                        + "," + dato.getRES_EJE()
                        + "," + dato.getRES_NUM()
                );
                if (pt.executeUpdate() > 0) {
                    retorno = this.getSirDestinoRResByPKRRes(dato.getRES_DEP(), dato.getRES_UOR(), dato.getRES_TIP(), dato.getRES_EJE(), dato.getRES_NUM(), con);
                    log.info("Actualizacion Correcta: " + dato.toString());
                } else {
                    log.error("No se ha podido realizar la Actualizacion .... : " + dato.toString());
                }
            } else {
                log.error("No se puede insertar, datos a procesar recibidos a null");
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error Guardar entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("updateCodigoUnidadSirDestinoRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public SirDestinoRRes updateSirDestinoRRes(SirDestinoRRes dato, Connection con) throws Exception {
        log.info("updateSirDestinoRRes - Begin () " + formatDate.format(new Date()));
        SirDestinoRRes retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            if (dato != null) {
                String query = " update "
                        + " SIR_DESTINO_R_RES "
                        + " set codigoUnidad=? "
                        + " , oficinaRegistroSIR=? "
                        + " , numeroRegistroSIR=? "
                        + " , usuarioRegistroSIR=? "
                        + " , fechaRegistroSIR=? "
                        + " , fechaRegistroSistemaSIR=? "
                        + " where RES_DEP=? "
                        + " and RES_UOR=? "
                        + " and RES_TIP=? "
                        + " and RES_EJE=? "
                        + " and RES_NUM=? ";

                log.info("sql = " + query);
                pt = con.prepareStatement(query);
                int contParams = 1;
                pt.setString(contParams++, (dato.getCodigoUnidad() != null && !dato.getCodigoUnidad().isEmpty() && !dato.getCodigoUnidad().equalsIgnoreCase("null") ? dato.getCodigoUnidad() : null));
                pt.setString(contParams++, (dato.getOficinaRegistroSIR() != null && !dato.getOficinaRegistroSIR().isEmpty() && !dato.getOficinaRegistroSIR().equalsIgnoreCase("null") ? dato.getOficinaRegistroSIR() : null));
                pt.setString(contParams++, (dato.getNumeroRegistroSIR() != null && !dato.getNumeroRegistroSIR().isEmpty() && !dato.getNumeroRegistroSIR().equalsIgnoreCase("null") ? dato.getNumeroRegistroSIR() : null));
                pt.setString(contParams++, (dato.getUsuarioRegistroSIR() != null && !dato.getUsuarioRegistroSIR().isEmpty() && !dato.getUsuarioRegistroSIR().equalsIgnoreCase("null") ? dato.getUsuarioRegistroSIR() : null));
                if (dato.getFechaRegistroSIR() != null) {
                    pt.setDate(contParams++, (new java.sql.Date(dato.getFechaRegistroSIR().getTime())));
                } else {
                    pt.setNull(contParams++, Types.DATE);
                }
                if (dato.getFechaRegistroSistemaSIR() != null)
                    pt.setDate(contParams++, (new java.sql.Date(dato.getFechaRegistroSistemaSIR().getTime())));
                else
                    pt.setNull(contParams++, Types.DATE);
                pt.setInt(contParams++, dato.getRES_DEP());
                pt.setInt(contParams++, dato.getRES_UOR());
                pt.setString(contParams++, dato.getRES_TIP());
                pt.setInt(contParams++, dato.getRES_EJE());
                pt.setInt(contParams++, dato.getRES_NUM());
                log.info("Param ? : " + dato.getCodigoUnidad()
                        + "," + dato.getOficinaRegistroSIR()
                        + "," + dato.getNumeroRegistroSIR()
                        + "," + dato.getUsuarioRegistroSIR()
                        + "," + (dato.getFechaRegistroSIR() != null ? formatDate.format(dato.getFechaRegistroSIR()) : "")
                        + "," + (dato.getFechaRegistroSistemaSIR() != null ? formatDate.format(dato.getFechaRegistroSistemaSIR()) : "")
                        + "," + dato.getRES_DEP()
                        + "," + dato.getRES_UOR()
                        + "," + dato.getRES_TIP()
                        + "," + dato.getRES_EJE()
                        + "," + dato.getRES_NUM()
                );
                if (pt.executeUpdate() > 0) {
                    retorno = this.getSirDestinoRResByPKRRes(dato.getRES_DEP(), dato.getRES_UOR(), dato.getRES_TIP(), dato.getRES_EJE(), dato.getRES_NUM(), con);
                    log.info("Actualizacion Correcta: " + dato.toString());
                } else {
                    log.error("No se ha podido realizar la Actualizacion .... : " + dato.toString());
                }
            } else {
                log.error("No se puede insertar, datos a procesar recibidos a null");
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error Guardar entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("updateSirDestinoRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public boolean deleteSirDestinoRResForRResPk(SirDestinoRRes dato, Connection con) throws Exception {
        log.info("deleteSirDestinoRRes - Begin () " + formatDate.format(new Date()));
        boolean retorno = false;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            if (dato != null) {
                String query = " DELETE FROM "
                        + " SIR_DESTINO_R_RES "
                        + " where RES_DEP=? "
                        + " and RES_UOR=? "
                        + " and RES_TIP=? "
                        + " and RES_EJE=? "
                        + " and RES_NUM=? ";

                log.info("sql = " + query);
                pt = con.prepareStatement(query);
                int contParams = 1;
                pt.setInt(contParams++, dato.getRES_DEP());
                pt.setInt(contParams++, dato.getRES_UOR());
                pt.setString(contParams++, dato.getRES_TIP());
                pt.setInt(contParams++, dato.getRES_EJE());
                pt.setInt(contParams++, dato.getRES_NUM());
                log.info("Param ? : " + dato.getRES_DEP()
                        + "," + dato.getRES_UOR()
                        + "," + dato.getRES_TIP()
                        + "," + dato.getRES_EJE()
                        + "," + dato.getRES_NUM()
                );
                if (pt.executeUpdate() > 0) {
                    retorno=true;
                    log.info("Eliminacion Correcta: " + dato.toString());
                } else {
                    log.error("No se ha podido realizar la Actualizacion .... : " + dato.toString());
                }
            } else {
                log.error("No se puede insertar, datos a procesar recibidos a null");
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error Eliminar entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("deleteSirDestinoRRes - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public boolean deleteSirDestinoRResForCodigoUnidad(String codigoUnidad, Connection con) throws Exception {
        log.info("deleteSirDestinoRResForCodigoUnidad - Begin () " + formatDate.format(new Date()));
        boolean retorno = false;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            if (codigoUnidad != null && !codigoUnidad.isEmpty() && !codigoUnidad.equalsIgnoreCase("null")) {
                String query = " DELETE FROM "
                        + " SIR_DESTINO_R_RES "
                        + " where codigoUnidad=?"
                        ;

                log.info("sql = " + query);
                pt = con.prepareStatement(query);
                int contParams = 1;
                pt.setString(contParams++, codigoUnidad);
                log.info("Param ? : " + codigoUnidad
                );
                if (pt.executeUpdate() > 0) {
                    retorno = true;
                    log.info("Eliminacion Correcta: " + codigoUnidad);
                } else {
                    log.error("No se ha podido realizar la Actualizacion .... : " + codigoUnidad);
                }
            } else {
                log.error("No se puede insertar, datos a procesar recibidos a null");
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error Eliminar entity SirDestinoRRes ... " + ex.getMessage(), ex);
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
        log.info("deleteSirDestinoRResForCodigoUnidad - End () " + formatDate.format(new Date()));
        return retorno;
    }


    public String getCodigoOficinaForCodigoUnidad(String codigoUnidad, Connection con) throws Exception {
        String codigoOficina = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT CODIGOOFICINA FROM SIR_UNIDAD_DIR3 WHERE CODIGOUNIDAD = ?";
            pt = con.prepareStatement(query);
            pt.setString(1, codigoUnidad);
            rs = pt.executeQuery();
            if (rs.next()) {
                codigoOficina = rs.getString("CODIGOOFICINA");
            }
        } catch (Exception ex) {
            throw new Exception("Error al obtener CODIGOOFICINA para codigoUnidad: " + codigoUnidad, ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignore) {}
            if (pt != null) try { pt.close(); } catch (Exception ignore) {}
        }
        return codigoOficina;
    }




}
