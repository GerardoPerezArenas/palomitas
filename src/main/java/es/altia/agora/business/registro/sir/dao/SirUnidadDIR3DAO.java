/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.dao;

import es.altia.agora.business.registro.sir.vo.RowMapperSirUnidadDIR3Dto;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3Dto;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3Filtros;
import es.altia.agora.business.util.GlobalNames;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author INGDGC
 */
public class SirUnidadDIR3DAO {
    
    private static final Logger log = Logger.getLogger(SirUnidadDIR3DAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final SirMappingVoDAO sirMappingVoDao = new SirMappingVoDAO();

    public SirUnidadDIR3 getSirUnidadDIR3ByCodigo(String codigo, Connection con) throws Exception {
        log.info("getSirUnidadDIR3ByCodigo - Begin () " + formatDate.format(new Date()));
        SirUnidadDIR3 retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_UNIDAD_DIR3 "
                    + " WHERE codigoUnidad=? ";

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setString(1, codigo);
            log.info("Param ? : " + codigo
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = sirMappingVoDao.getSirUnidadDIR3(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirUnidadDIR3 ... " + ex.getMessage(), ex);
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
        log.info("getSirUnidadDIR3ByCodigo - End () " + formatDate.format(new Date()));
        return retorno;
    }

    public List<SirUnidadDIR3> getAllSirUnidadDIR3(Connection con) throws Exception {
        log.info("getAllSirUnidadDIR3 - Begin () " + formatDate.format(new Date()));
        List<SirUnidadDIR3> retorno = new ArrayList<SirUnidadDIR3>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_UNIDAD_DIR3 "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(sirMappingVoDao.getSirUnidadDIR3(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirUnidadDIR3 ... " + ex.getMessage(), ex);
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
        log.info("getAllSirUnidadDIR3 - End () " + formatDate.format(new Date()));
        return retorno;
    }
    public int getTotalSirUnidadDIR3Registradas(Connection con) throws Exception {
        log.info("getTotalSirUnidadDIR3Registradas - Begin () " + formatDate.format(new Date()));
        int retorno = 0;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " select count(1) totalUnidadesDIR3Sistema from sir_unidad_dir3 "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno = rs.getInt("totalUnidadesDIR3Sistema");
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirUnidadDIR3 ... " + ex.getMessage(), ex);
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
        log.info("getTotalSirUnidadDIR3Registradas - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<SirUnidadDIR3Dto> getAllSirUnidadDIR3ByFilter(SirUnidadDIR3Filtros filtros,NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws Exception {
        log.info("getAllSirUnidadDIR3ByFilter - Begin () " + formatDate.format(new Date()));
        List<SirUnidadDIR3Dto> retorno = new ArrayList<SirUnidadDIR3Dto>();
        try {
            String nombreUnidad = (filtros.getIdioma()==4 ? filtros.getNombreUnidad_EU() : filtros.getNombreUnidad_ES());
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idioma", filtros.getIdioma())
                    .addValue("filtrarCodigouUnidad", filtros.isStringNullOrEmpty(filtros.getCodigoUnidad())? 0 : 1)
                    .addValue("codigoUnidad", filtros.getCodigoUnidad())
                    .addValue("filtrarNombreUnidad", filtros.isStringNullOrEmpty(nombreUnidad)? 0 : 1)
                    .addValue("nombreUnidad", nombreUnidad)
                    .addValue("filtrarCodigoOficina", filtros.isStringNullOrEmpty(filtros.getCodigoOficina())? 0 : 1)
                    .addValue("filtrarNombreOficina", filtros.isStringNullOrEmpty(filtros.getNombreOficina())? 0 : 1)
                    .addValue("nombreOficina", filtros.getNombreOficina())
                    .addValue("codigoOficina", filtros.getCodigoOficina())
                    .addValue("filtrarCodigoOrganismo", filtros.isStringNullOrEmpty(filtros.getCodigoOrganismo())? 0 : 1)
                    .addValue("codigoOrganismo", filtros.getCodigoOrganismo())
                    .addValue("filtrarCodigoRaiz", filtros.isStringNullOrEmpty(filtros.getCodigoRaiz())? 0 : 1)
                    .addValue("codigoRaiz", filtros.getCodigoRaiz())
                    .addValue("filtrarNombreRaiz", filtros.isStringNullOrEmpty(filtros.getNombreRaiz())? 0 : 1)
                    .addValue("nombreRaiz", filtros.getNombreRaiz())
                    .addValue("filtrarCodigoNivelAdministrativo", filtros.isStringNullOrEmpty(filtros.getCodigoNivelAdministrativo())? 0 : 1)
                    .addValue("codigoNivelAdministrativo", filtros.getCodigoNivelAdministrativo())
                    .addValue("filtrarCodigoComunidadAutonoma", filtros.isStringNullOrEmpty(filtros.getCodigoComunidadAutonoma())? 0 : 1)
                    .addValue("comunidadAutonoma", filtros.getCodigoComunidadAutonoma())
                    .addValue("filtrarCodigoProvincia", filtros.isStringNullOrEmpty(filtros.getCodigoProvincia())? 0 : 1)
                    .addValue("codigoProvincia", filtros.getCodigoProvincia())
                    .addValue("filtrarFechaActivacionDesde", filtros.isStringNullOrEmpty(filtros.getFechaActivacionDesde())? 0 : 1)
                    .addValue("filtrarFechaActivacionHasta", filtros.isStringNullOrEmpty(filtros.getFechaActivacionHasta())? 0 : 1)
                    .addValue("fechaActivacionDesde", filtros.getFechaActivacionDesde())
                    .addValue("fechaActivacionHasta", filtros.getFechaActivacionHasta())
                    .addValue("offsetQuery", filtros.getOffsetQuery())
                    .addValue("fetchQuery", filtros.getNumResultadosPorPagina())
                    ;
            
            String query = " select * from (select ROWNUM ordenPaginacion, datos.* from ( " // Comentar cuando se pase a Oracle19-Batera Con el offset no es necesario
                    + " select CODIGOUNIDAD, NOMBREUNIDAD_ES, NOMBREUNIDAD_EU, CODIGOOFICINA "
                    + ",CODIGOORGANISMO, CODIGORAIZ, CODIGONIVELADMINISTRATIVO, CODIGOCOMUNIDADAUTONOMA "
                    + ", CODIGOPROVINCIA, FECHAACTIVACION "
                    //  --Descripciones
                    + " ,(case when :idioma=4 then sir_oficina.nombre_eu else sir_oficina.nombre_es end ) oficina "
                    + " ,(case when :idioma=4 then sir_organismo.nombre_eu else sir_organismo.nombre_es end ) organismo "
                    + " ,(case when :idioma=4 then sir_raiz.nombre_eu else sir_raiz.nombre_es end ) raiz "
                    + " ,(case when :idioma=4 then sir_nivel_administrativo.nombre_eu else sir_nivel_administrativo.nombre_es end ) nivelAdministrativo "
                    + " ,(case when :idioma=4 then t_aut.aut_nol else t_aut.aut_nol end ) comunidadAutonoma "
                    + " ,(case when :idioma=4 then t_prv.prv_nol else t_prv.prv_nol end ) provincia "
                    // -- Caluculo resultados totales/ registro en el sistema
                    + " ,sum(1) over (partition by 1)  numResultadosTotal "
                    + " ,(select count(1) totalUnidadesDIR3Sistema from sir_unidad_dir3 ) totalUnidadesDIR3Sistema "
                    // -- Criterios Ordenacion segun idioma, upper
                    + ", (case when :idioma=4  then nombreunidad_eu else  nombreunidad_es end)  nombreUnidadOrder "
                    // -- Codigo Visible en regexlan si existe la unidad
                    + ", CODVISIBLEUORFLEXIA "
                    + " from sir_unidad_dir3 "
                    + " left join sir_oficina on sir_oficina.codigo=sir_unidad_dir3.codigooficina "
                    + " left join sir_organismo on sir_organismo.codigo=sir_unidad_dir3.codigoorganismo "
                    + " left join sir_raiz on sir_raiz.codigo=sir_unidad_dir3.codigoraiz "
                    + " left join sir_nivel_administrativo on sir_nivel_administrativo.codigo=sir_unidad_dir3.codigoniveladministrativo "
                    + " left join " + GlobalNames.ESQUEMA_GENERICO + "t_aut t_aut on t_aut.aut_cod=sir_unidad_dir3.codigocomunidadautonoma "
                    + " left join " + GlobalNames.ESQUEMA_GENERICO + "t_prv t_prv on t_prv.prv_cod=sir_unidad_dir3.codigoprovincia "
                    + " where "
                    + " (:filtrarCodigouUnidad=0 or upper(codigounidad)  like '%'||upper(:codigoUnidad)||'%') "
                    + " and (:filtrarNombreUnidad=0 or (upper(case when :idioma=4  then nombreunidad_eu else  nombreunidad_es end) like '%'||upper(:nombreUnidad)||'%')) "
                    + " and (:filtrarCodigoOficina=0 or upper(codigoOficina)  like '%'||upper(:codigoOficina)||'%') "
                    + " and (:filtrarNombreOficina=0 or (upper(case when :idioma=4  then sir_oficina.nombre_eu else  sir_oficina.nombre_es end) like '%'||upper(:nombreOficina)||'%')) "
                    + " and (:filtrarCodigoOrganismo=0 or upper(codigoOrganismo)  like '%'||upper(:codigoOrganismo)||'%') "
                    + " and (:filtrarCodigoRaiz=0 or upper(codigoRaiz)  like '%'||upper(:codigoRaiz)||'%') "
                    + " and (:filtrarNombreRaiz=0 or (upper(case when :idioma=4  then sir_raiz.nombre_eu else sir_raiz.nombre_es end) like '%'||upper(:nombreRaiz)||'%')) "
                    + " and (:filtrarCodigoNivelAdministrativo=0 or upper(codigoNivelAdministrativo)=upper(:codigoNivelAdministrativo)) "
                    + " and (:filtrarCodigoComunidadAutonoma=0 or upper(codigocomunidadautonoma)=upper(:comunidadAutonoma)) "
                    + " and (:filtrarCodigoProvincia=0 or upper(codigoProvincia)=upper(:codigoProvincia)) "
                    + " and (:filtrarFechaActivacionDesde=0 or to_date(to_char(fechaActivacion,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date(:fechaActivacionDesde,'dd/MM/yyyy')) "
                    + " and (:filtrarFechaActivacionHasta=0 or to_date(to_char(fechaActivacion,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date(:fechaActivacionHasta,'dd/MM/yyyy')) "
                    + " order by NLSSORT(nombreUnidadOrder, 'NLS_SORT=spanish') asc  "
                    //+ " OFFSET :offsetQuery ROWS  FETCH NEXT :fetchQuery ROWS ONLY "
                    // OFFSET No va en version 11 de oracle, cuando sen pase a Batera la version es 19, activar este uso.
                    // y Comentar la linea siguiente
                    + " ) datos) where ordenPaginacion BETWEEN :offsetQuery+1 and (:offsetQuery + :fetchQuery) "
                    ;
            log.info("sql = " + query);
            log.info("params = " + namedParameters.toString());
            retorno = namedParameterJdbcTemplate.query(query, namedParameters,new RowMapperSirUnidadDIR3Dto());
        } catch (Exception ex) {
            log.error("Se ha producido un error recoger entity SirUnidadDIR3 ... " + ex.getMessage(), ex);
            throw new Exception(ex);
        } 
        log.info("getAllSirUnidadDIR3ByFilter - End () " + formatDate.format(new Date()));
        return retorno;
    }

    private String getClausulaWhereFiltros(SirUnidadDIR3Filtros filtros) {
        String respuesta = "";
        String clausulaPrevia = "";
        try {
            if(filtros!=null){
                if(!filtros.isStringNullOrEmpty(filtros.getCodigoUnidad()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " codigoUnidad='"+ filtros.getCodigoUnidad()+ "'";
                if(!filtros.isStringNullOrEmpty(filtros.getNombreUnidad_ES()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " UPPER(nombreUnidad_ES) LIKE'%'||UPPER("+ filtros.getNombreUnidad_ES() + ")||'%'";
                if(!filtros.isStringNullOrEmpty(filtros.getNombreUnidad_EU()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " UPPER(nombreUnidad_EU) LIKE'%'||UPPER("+ filtros.getNombreUnidad_EU() + ")||'%'";
                if(!filtros.isStringNullOrEmpty(filtros.getCodigoOficina()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " codigoOficina='"+ filtros.getCodigoOficina() + "'";
                if(!filtros.isStringNullOrEmpty(filtros.getCodigoOrganismo()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " codigoOrganismo='"+ filtros.getCodigoOrganismo()+ "'";
                if(!filtros.isStringNullOrEmpty(filtros.getCodigoRaiz()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " codigoRaiz='"+ filtros.getCodigoRaiz() + "'";
                if(!filtros.isStringNullOrEmpty(filtros.getCodigoNivelAdministrativo()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " codigoNivelAdministrativo='"+ filtros.getCodigoNivelAdministrativo() + "'";
                if(!filtros.isStringNullOrEmpty(filtros.getCodigoComunidadAutonoma()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " codigoComunidadAutonoma='"+ filtros.getCodigoComunidadAutonoma()+ "'";
                if(!filtros.isStringNullOrEmpty(filtros.getCodigoProvincia()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " codigoProvincia='"+ filtros.getCodigoProvincia()+ "'";
                if(!filtros.isStringNullOrEmpty(filtros.getFechaActivacionDesde()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " TO_DATE(TO_CHAR(fechaActivacion,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('"+ filtros.getFechaActivacionDesde()+ "','dd/MM/yyyy')";
                if(!filtros.isStringNullOrEmpty(filtros.getFechaActivacionHasta()))
                    respuesta = filtros.getClausulaPrevia(clausulaPrevia) + " TO_DATE(TO_CHAR(fechaActivacion,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('"+ filtros.getFechaActivacionHasta()+ "','dd/MM/yyyy')";
            }
        } catch (Exception e) {
            log.error("Error al obtener la clausula where ... ", e);
        }
        return respuesta; 
    }
    
    public SirUnidadDIR3 getSirUnidadDIR3ByCodVisibleUorFlexia(String codVisibleUorFlexia, Connection con) throws Exception {
        log.info("getSirUnidadDIR3ByCodVisibleUorFlexia - Begin () "+ codVisibleUorFlexia + " " + formatDate.format(new Date()));
        SirUnidadDIR3 retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM SIR_UNIDAD_DIR3 "
                    + " WHERE codVisibleUorFlexia=? ";
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setString(1, codVisibleUorFlexia);
            log.info("Param ? : " + codVisibleUorFlexia
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = sirMappingVoDao.getSirUnidadDIR3(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity SirUnidadDIR3 ... " + ex.getMessage(), ex);
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
        log.info("getSirUnidadDIR3ByCodVisibleUorFlexia - End () " + (retorno!=null ? retorno.toString() : "null") + formatDate.format(new Date()));
        return retorno;
    }
    
    private int getOffsetQuery(int numResultadosPorPagina, int numPaginaRecuperar){
        return (numPaginaRecuperar > 1 ? ((numPaginaRecuperar-1)*numResultadosPorPagina):0);
    }

    
}
