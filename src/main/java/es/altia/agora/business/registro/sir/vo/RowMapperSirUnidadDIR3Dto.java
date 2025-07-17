/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Mapea los datos desdes la sql consulta por filtros a un objeto SirUnidadDIR3Dto
 * @author INGDGC
 */
public class RowMapperSirUnidadDIR3Dto implements RowMapper{
    
    @Override
    // CODIGOUNIDAD, NOMBREUNIDAD_ES, NOMBREUNIDAD_EU, CODIGOOFICINA, CODIGOORGANISMO, CODIGORAIZ, CODIGONIVELADMINISTRATIVO, 
            //CODIGOCOMUNIDADAUTONOMA, CODIGOPROVINCIA, FECHAACTIVACION, OFICINA, ORGANISMO, RAIZ, NIVELADMINISTRATIVO,
            //COMUNIDADAUTONOMA, PROVINCIA, NUMRESULTADOSTOTAL, TOTALUNIDADESDIR3SISTEMA, NOMBREUNIDADORDER
    public SirUnidadDIR3Dto mapRow(ResultSet rs, int rowNum) throws SQLException {
        SirUnidadDIR3Dto sirUnidadDIR3Dto = new SirUnidadDIR3Dto(rs.getString("codigoUnidad"),
                 rs.getString("nombreUnidad_ES"),
                 rs.getString("nombreUnidad_EU"),
                 rs.getString("codigoOficina"),
                 rs.getString("codigoOrganismo"),
                 rs.getString("codigoRaiz"),
                 rs.getString("codigoNivelAdministrativo"),
                 rs.getString("codigoComunidadAutonoma"),
                 rs.getString("codigoProvincia"),
                 rs.getString("codVisibleUorFlexia"),
                 rs.getDate("fechaActivacion")
        );
        sirUnidadDIR3Dto.setOficina(rs.getString("oficina"));
        sirUnidadDIR3Dto.setOrganismo(rs.getString("organismo"));
        sirUnidadDIR3Dto.setRaiz(rs.getString("raiz"));
        sirUnidadDIR3Dto.setNivelAdministrativo(rs.getString("nivelAdministrativo"));
        sirUnidadDIR3Dto.setComunidadAutonoma(rs.getString("comunidadAutonoma"));
        sirUnidadDIR3Dto.setProvincia(rs.getString("provincia"));
        sirUnidadDIR3Dto.setNumResultadosTotal(rs.getInt("numResultadosTotal"));
        //sirUnidadDIR3Dto.setTotalUnidadesDIR3Sistema(rs.getInt("totalUnidadesDIR3Sistema"));
        return sirUnidadDIR3Dto;
    }


    
}
