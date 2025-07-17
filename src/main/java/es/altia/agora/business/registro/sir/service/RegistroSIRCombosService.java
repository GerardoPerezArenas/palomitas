
package es.altia.agora.business.registro.sir.service;

import es.altia.agora.business.registro.sir.dao.SirGenericDAO;
import es.altia.agora.business.registro.sir.dao.SirNivelAdministrativoDAO;
import es.altia.agora.business.registro.sir.dao.SirOrganismoDAO;
import es.altia.agora.business.registro.sir.vo.SirNivelAdministrativo;
import es.altia.agora.business.registro.sir.vo.SirOrganismo;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3Dto;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3Filtros;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class RegistroSIRCombosService {
    
    private static final Logger log = Logger.getLogger(RegistroSIRCombosService.class);
    
    private final SirGenericDAO sirGenericDAO = new SirGenericDAO();
    private final SirNivelAdministrativoDAO sirNivelAdministrativoDAO = new SirNivelAdministrativoDAO();
    private final SirOrganismoDAO sirOrganismoDAO = new SirOrganismoDAO();
    private final RegistroSIRService registroSIRService = new RegistroSIRService();
    
    public List<GeneralComboVO> getUnidadesDir3GeneralCombo(SirUnidadDIR3Filtros filtros, AdaptadorSQLBD adapt) throws Exception {
        List<GeneralComboVO> respuesta = new ArrayList<GeneralComboVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            List<SirUnidadDIR3Dto> listaDIR3 = registroSIRService.getAllSirUnidadDIR3ByFilter(filtros, adapt);
            for (SirUnidadDIR3Dto sirUnidadDIR3 : listaDIR3) {
                GeneralComboVO elemento = new GeneralComboVO();
                elemento.setCodigo(sirUnidadDIR3.getCodigoUnidad());
                elemento.setDescripcion((filtros.getIdioma() == 4 ? sirUnidadDIR3.getNombreUnidad_EU() : sirUnidadDIR3.getNombreUnidad_ES()));
                respuesta.add(elemento);
            }
        } catch (Exception e) {
            log.error("Error al recuperar lista unidades DIR3 : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adapt.devolverConexion(con);
                log.error("getUnidadesDir3GeneralCombo Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    public List<GeneralComboVO> getProvinciasXComunidadGeneralCombo(String codigoComunidadAutonoma, AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        Connection con = null;
        try {
            con = adaptadorSQLBD.getConnection();
            return sirGenericDAO.getProvinciasXComunidadGeneralCombo(codigoComunidadAutonoma, con);
        } catch (Exception e) {
            log.error("Error al recuperar lista Provincias : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("getProvinciasXComunidadGeneralCombo Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
    }

    public List<GeneralComboVO> getListaComboComunidadAutonoma(AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        Connection con = null;
        try {
            con = adaptadorSQLBD.getConnection();
            return sirGenericDAO.getListaComboComunidadAutonoma(con);
        } catch (Exception e) {
            log.error("Error al recuperar lista comunidades autonomas : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("getListaComboComunidadAutonoma Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
    }

    public List<GeneralComboVO> getListaComboNivelAdministrativo(int idioma, AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        List<GeneralComboVO> respuesta = new ArrayList<GeneralComboVO>();
        Connection con = null;
        try {
            con = adaptadorSQLBD.getConnection();
            List<SirNivelAdministrativo> listaSirNivelAdministrativo = sirNivelAdministrativoDAO.getAllSirNivelAdministrativo(con);
            for (SirNivelAdministrativo sirNivelAdministrativo : listaSirNivelAdministrativo) {
                GeneralComboVO elemento = new GeneralComboVO();
                elemento.setCodigo(sirNivelAdministrativo.getCodigo());
                elemento.setDescripcion((idioma == 4 ? sirNivelAdministrativo.getNombre_EU() : sirNivelAdministrativo.getNombre_ES()));
                respuesta.add(elemento);
            }
        } catch (Exception e) {
            log.error("Error al recuperar lista Nivel Administrativo : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("getListaComboNivelAdministrativo Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }

    public List<GeneralComboVO> getListaComboOrganismos(int idioma, AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        List<GeneralComboVO> respuesta = new ArrayList<GeneralComboVO>();
        Connection con = null;
        try {
            con = adaptadorSQLBD.getConnection();
            List<SirOrganismo> listaSirOrganismo = sirOrganismoDAO.getAllSirOrganismo(con);
            for (SirOrganismo sirOrganismo : listaSirOrganismo) {
                GeneralComboVO elemento = new GeneralComboVO();
                elemento.setCodigo(sirOrganismo.getCodigo());
                elemento.setDescripcion((idioma == 4 ? sirOrganismo.getNombre_EU() : sirOrganismo.getNombre_ES()));
                respuesta.add(elemento);
            }
        } catch (Exception e) {
            log.error("Error al recuperar lista Organismos : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("getListaComboOrganismos Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }


    
}
