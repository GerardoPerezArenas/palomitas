/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.registro.digitalizacion.lanbide.persistence;

import es.altia.flexia.registro.digitalizacion.lanbide.persistence.manual.GeneralComboServiceDAO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class GeneralComboServiceManager {
    
    Logger LOG = Logger.getLogger(GeneralComboServiceManager.class);
    private GeneralComboServiceDAO generalComboServiceDAO = new GeneralComboServiceDAO();
    
    
    public List<GeneralComboVO> getComboProcedimiento(int codigoOrgaizacion,String[] params) throws Exception{
        LOG.info("getComboProcedimiento Manager - Begin");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            return generalComboServiceDAO.getComboProcedimiento(codigoOrgaizacion, con);
        } catch (Exception e) {
            LOG.error("Error al recuperar el desplegable de procedimientos.", e);
            throw e;
        }finally{
            if(con!=null && !con.isClosed()){
                adapt.devolverConexion(con);
            }
            LOG.info("getComboProcedimiento Manager - End");
        }
    }
    
}
