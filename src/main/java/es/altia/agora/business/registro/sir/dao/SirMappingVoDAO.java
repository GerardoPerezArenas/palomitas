/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.dao;

import es.altia.agora.business.registro.sir.vo.*;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class SirMappingVoDAO {
    
    private static final Logger log = Logger.getLogger(SirMappingVoDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public SirOficina getSirOficina(ResultSet rs){
        SirOficina respuesta = null;
        if(rs!=null){
            try {
                respuesta=new SirOficina(rs.getString("codigo")
                        ,rs.getString("nombre_ES")
                        ,rs.getString("nombre_EU")
                    );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a SirOficina " + e.getMessage(), e);
                respuesta=null;
            }
            
        }
        return respuesta;
    }
    
    public SirOrganismo getSirOrganismo(ResultSet rs){
        SirOrganismo respuesta = null;
        if(rs!=null){
            try {
                respuesta=new SirOrganismo(rs.getString("codigo")
                        ,rs.getString("nombre_ES")
                        ,rs.getString("nombre_EU")
                    );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a SirOrganismo " + e.getMessage(), e);
                respuesta=null;
            }
            
        }
        return respuesta;
    }
    
    public SirRaiz getSirRaiz(ResultSet rs){
        SirRaiz respuesta = null;
        if(rs!=null){
            try {
                respuesta=new SirRaiz(rs.getString("codigo")
                        ,rs.getString("nombre_ES")
                        ,rs.getString("nombre_EU")
                    );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a SirRaiz " + e.getMessage(), e);
                respuesta=null;
            }
            
        }
        return respuesta;
    }
    
    public SirNivelAdministrativo getSirNivelAdministrativo(ResultSet rs){
        SirNivelAdministrativo respuesta = null;
        if(rs!=null){
            try {
                respuesta=new SirNivelAdministrativo(rs.getString("codigo")
                        ,rs.getString("nombre_ES")
                        ,rs.getString("nombre_EU")
                    );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a SirNivelAdministrativo " + e.getMessage(), e);
                respuesta=null;
            }
            
        }
        return respuesta;
    }
    
    public SirUnidadDIR3 getSirUnidadDIR3(ResultSet rs) throws Exception {
        SirUnidadDIR3 respuesta = null;
        if (rs != null) {
            try {
                respuesta = new SirUnidadDIR3(rs.getString("codigoUnidad")
                         ,rs.getString("nombreUnidad_ES")
                         ,rs.getString("nombreUnidad_EU")
                         ,rs.getString("codigoOficina")
                         ,rs.getString("codigoOrganismo")
                         ,rs.getString("codigoRaiz")
                         ,rs.getString("codigoNivelAdministrativo")
                         ,rs.getString("codigoComunidadAutonoma")
                         ,rs.getString("codigoProvincia")
                         ,rs.getString("codVisibleUorFlexia")
                         ,rs.getDate("fechaActivacion")
                );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a SirUnidadDIR3 " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }

    public GeneralComboVO getGeneralComboVO(ResultSet rs) throws Exception {
        GeneralComboVO respuesta = null;
        if (rs != null) {
            try {
                respuesta = new GeneralComboVO();
                respuesta.setCodigo(rs.getString("codigo"));
                respuesta.setDescripcion(rs.getString("descripcion"));
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a GeneralComboVO " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }
    public SirDestinoRRes getSirDestinoRRes(ResultSet rs) throws Exception {
        SirDestinoRRes respuesta = null;
        if (rs != null) {
            try {
                respuesta = new SirDestinoRRes(rs.getInt("RES_DEP")
                        ,rs.getInt("RES_UOR")
                        ,rs.getString("RES_TIP")
                        ,rs.getInt("RES_EJE")
                        ,rs.getInt("RES_NUM")
                        ,rs.getString("codigoUnidad")
                        ,rs.getString("oficinaRegistroSIR")
                        ,rs.getString("numeroRegistroSIR")
                        ,rs.getString("usuarioRegistroSIR")
                        ,rs.getDate("fechaRegistroSIR")
                        ,rs.getDate("fechaRegistroSistemaSIR")
                        ,rs.getDate("fechaRegistro")
                );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a SirDestinoRRes " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }
    public SirLocalidades getSirLocalidades(ResultSet rs) throws Exception {
        SirLocalidades respuesta = null;
        if (rs != null) {
            try {
                respuesta = new SirLocalidades(rs.getString("COD_ENTIDAD")
                        ,rs.getString("COD_PROVINCIA")
                        ,rs.getString("COD_LOCALIDAD")
                        ,rs.getString("DENOMINACION")
                        ,rs.getString("COD_ISLA")
                        ,rs.getString("COD_LOCALIDAD_FLEXIA")
                        ,rs.getString("SUFIJO_DIR")
                );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a SirDestinoRRes " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }

}
