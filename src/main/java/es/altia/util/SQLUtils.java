/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author jesus.cordoba-perez
 */
public class SQLUtils {
    
    //Logger
    private static Logger log = Logger.getLogger(SQLUtils.class);

    /**
     * Metodo que obtiene el valor de la secuencia pasada como parametro de entrada
     * @param secuencia
     * @param con
     * @return
     * @throws Exception
     */
    public static Long obtenerValorSecuencia(String secuencia, Connection con) throws Exception {
        if (SQLUtils.log.isDebugEnabled()) {
            SQLUtils.log.debug("obtenerSecuenciaEstadoPortafirmas() : BEGIN");
        }
        Long valorIdSecuencia = null;
        try {
            String sql = "select " + secuencia + ".nextval from DUAL";
            if (SQLUtils.log.isDebugEnabled()) {
                SQLUtils.log.debug("Consulta para obtener la secuencia: " + sql);
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valorIdSecuencia = rs.getLong(1);
            }
        } catch (SQLException ex) {
            SQLUtils.log.error("Se ha producido un error al obtener la secuencia = " + ex.getMessage());
            throw ex;
        }
        if (SQLUtils.log.isDebugEnabled()) {
            SQLUtils.log.debug("valorIdSecuencia = " + valorIdSecuencia);
        }
        if (SQLUtils.log.isDebugEnabled()) {
            SQLUtils.log.debug("obtenerSecuenciaEstadoPortafirmas() : END");
        }
        return valorIdSecuencia;
    }
    
}
