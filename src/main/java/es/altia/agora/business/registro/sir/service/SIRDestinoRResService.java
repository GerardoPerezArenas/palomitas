/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.service;

import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.registro.sir.dao.SirDestinoRResDAO;
import es.altia.agora.business.registro.sir.dao.SirGenericDAO;
import es.altia.agora.business.registro.sir.dao.SirUnidadDIR3DAO;
import es.altia.agora.business.registro.sir.vo.SirDestinoRRes;
import es.altia.agora.business.registro.sir.vo.SirDestinoRResDto;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class SIRDestinoRResService {
    
    private static final Logger log  = Logger.getLogger(SIRDestinoRResService.class);
    
    private  final AnotacionRegistroManager anotacionRegistroManager = AnotacionRegistroManager.getInstance();
    private  final AnotacionRegistroDAO anotacionRegistroDAO = AnotacionRegistroDAO.getInstance();
    private  final SirUnidadDIR3DAO sirUnidadDIR3DAO = new SirUnidadDIR3DAO();
    private  final SirGenericDAO sirGenericDAO = new SirGenericDAO();
    private  final SirDestinoRResDAO sirDestinoRResDAO = new SirDestinoRResDAO();

    
    public boolean existenDatosSirDestinoRResForPKRRes(SirDestinoRRes sirDestinoRRes, AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        boolean respuesta = false;
        Connection con = null;
        try {
            if (sirDestinoRRes != null) {
                con = adaptadorSQLBD.getConnection();
                SirDestinoRRes objeto = sirDestinoRResDAO.getSirDestinoRResByPKRRes(sirDestinoRRes.getRES_DEP()
                        ,sirDestinoRRes.getRES_UOR()
                        ,sirDestinoRRes.getRES_TIP()
                        ,sirDestinoRRes.getRES_EJE()
                        ,sirDestinoRRes.getRES_NUM()
                        , con);
                respuesta = objeto!=null;
            } else {
                log.error("No se procesa peticion de guardado, daton de entrada llegan null");
            }

        } catch (Exception e) {
            log.error("Error al guardar  datos SirDestinoRRes: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("existenDatosSirDestinoRResForPKRRes Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    public SirDestinoRRes crearDatosSirDestinoRRes(int idioma,SirDestinoRRes sirDestinoRRes ,AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        SirDestinoRRes respuesta = null;
        Connection con = null;
        try {
            if(sirDestinoRRes!=null){
                con = adaptadorSQLBD.getConnection();
                respuesta=sirDestinoRResDAO.createSirDestinoRRes(sirDestinoRRes, con);
            }else{
                log.error("No se procesa peticion de guardado, daton de entrada llegan null");
            }
            
        } catch (Exception e) {
            log.error("Error al guardar  datos SirDestinoRRes: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("guardarDatosSirDestinoRRes Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    public SirDestinoRRes actualizarCodigoUnidadSirDestinoRRes(int idioma,SirDestinoRRes sirDestinoRRes ,AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        SirDestinoRRes respuesta = null;
        Connection con = null;
        try {
            if(sirDestinoRRes!=null){
                con = adaptadorSQLBD.getConnection();
                respuesta=sirDestinoRResDAO.updateCodigoUnidadSirDestinoRRes(sirDestinoRRes, con);
            }else{
                log.error("No se procesa peticion de guardado, daton de entrada llegan null");
            }
            
        } catch (Exception e) {
            log.error("Error al guardar  datos SirDestinoRRes: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("guardarDatosSirDestinoRRes Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    public SirDestinoRRes updateSirDestinoRRes(int idioma,SirDestinoRRes sirDestinoRRes ,AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        SirDestinoRRes respuesta = null;
        Connection con = null;
        try {
            if(sirDestinoRRes!=null){
                con = adaptadorSQLBD.getConnection();
                respuesta=sirDestinoRResDAO.updateSirDestinoRRes(sirDestinoRRes, con);
            }else{
                log.error("No se procesa peticion de guardado, daton de entrada llegan null");
            }
            
        } catch (Exception e) {
            log.error("Error al guardar  datos SirDestinoRRes: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("updateSirDestinoRRes Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    public boolean eliminarSirDestinoRResForPKRRes(int idioma,SirDestinoRRes sirDestinoRRes ,AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        boolean respuesta = false;
        Connection con = null;
        try {
            if(sirDestinoRRes!=null){
                con = adaptadorSQLBD.getConnection();
                respuesta=sirDestinoRResDAO.deleteSirDestinoRResForRResPk(sirDestinoRRes, con);
            }else{
                log.error("No se procesa peticion de eliminado, datos de entrada llegan null");
            }
            
        } catch (Exception e) {
            log.error("Error al guardar  datos SirDestinoRRes: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("guardarDatosSirDestinoRRes Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    public SirDestinoRResDto getSirDestinoRResByPKRRes(int idioma,SirDestinoRRes sirDestinoRRes ,AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        SirDestinoRResDto respuesta = null;
        Connection con = null;
        try {
            if(sirDestinoRRes!=null){
                con = adaptadorSQLBD.getConnection();
                respuesta=sirDestinoRResDAO.getSirDestinoRResDTOByPKRRes(idioma,sirDestinoRRes.getRES_DEP()
                        ,sirDestinoRRes.getRES_UOR()
                        ,sirDestinoRRes.getRES_TIP()
                        ,sirDestinoRRes.getRES_EJE()
                        ,sirDestinoRRes.getRES_NUM()
                        ,con);
            }else{
                log.error("No se procesa peticion de recuperacion, datos de entrada llegan null");
            }
            
        } catch (Exception e) {
            log.error("Error al recuperar  datos SirDestinoRRes: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("getSirDestinoRResByPKRRes Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
}
