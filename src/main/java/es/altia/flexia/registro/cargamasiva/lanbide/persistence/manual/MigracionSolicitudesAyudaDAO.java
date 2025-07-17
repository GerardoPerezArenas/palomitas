package es.altia.flexia.registro.cargamasiva.lanbide.persistence.manual;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.registro.cargamasiva.lanbide.util.MigracionSolicitudesAyudaConstantesDatos;
import es.altia.flexia.registro.cargamasiva.lanbide.vo.SolicitudVO;
import es.altia.flexia.webservice.objetos.RegistroVO;
import es.altia.util.commons.DateOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class MigracionSolicitudesAyudaDAO {

    private static MigracionSolicitudesAyudaDAO instance = null;
    private static Logger log = Logger.getLogger(MigracionSolicitudesAyudaDAO.class.getName());

    public MigracionSolicitudesAyudaDAO() {
    }
	
    public static MigracionSolicitudesAyudaDAO getInstance() {
        // Necesitamos sincronización aquí para serializar (no multithread)
        // las invocaciones a este metodo
        synchronized(MigracionSolicitudesAyudaDAO.class) {
            if (instance == null) {
              instance = new MigracionSolicitudesAyudaDAO();
            }      
        }
        return instance;
    }
	
    public List<SolicitudVO> obtenerSolicitudes(Connection con, String codProc) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder();
        List<SolicitudVO> solicitudes = null;

        if (codProc == null || "".equals(codProc.trim())) {
            return solicitudes;
        }

        try {
            //query = "Select * From Lbweb13.Em_Solicitud_Ayudas@Flbgen_Lbi.Lanbide.Lan";
            //query = "Select * From Lbweb13.Em_Solicitud_Artistas@Flbgen_Lbi.Lanbide.Lan";

            if ("ATASE".equals(codProc)) {
                query.append("SELECT CORR_CONSULTA, CIF, EMAIL, NOMBRE, APE1, APE2, REGISTRO, TFNO, ");
                query.append(" TO_CHAR(FECHA_ALTA,'DD/MM/YYYY HH24:MI:SS') AS FECHA_ALTA, ARCHIVO0, ARCHIVO1, ARCHIVO2, ARCHIVO3, ARCHIVO4, ARCHIVO5, AMBITO, MUNICIPIO FROM EM_SOLICITUD_AYUDAS");
                query.append(" ORDER BY CORR_CONSULTA");
            } else if ("ACASE".equals(codProc)) {
                query.append("SELECT CORR_CONSULTA, CIF, EMAIL, NOMBRE, APE1, APE2, REGISTRO, TFNO, ");
                query.append(" TO_CHAR(FECHA_ALTA,'DD/MM/YYYY HH24:MI:SS') AS FECHA_ALTA, ARCHIVO0, ARCHIVO1, ARCHIVO2, ARCHIVO3, ARCHIVO4, ARCHIVO5, ARCHIVO6, ARCHIVO7, ARCHIVO8 FROM EM_SOLICITUD_ARTISTAS");
                query.append(" ORDER BY CORR_CONSULTA");
            } else if ("AERTE".equalsIgnoreCase(codProc)) {
                query.append("SELECT CORR_REG, \n" +
                        "       NUM_DOC, \n" +
                        "       CORRELEC, \n" +
                        "       NOMBRE, \n" +
                        "       APELLIDO1, \n" +
                        "       APELLIDO2, \n" +
                        "       TFNO, \n" +
                        "       FECHA_ALTA, \n" +
                        "       NVL(MIN(COD_RETIVIPU),'18') COD_RETIVIPU, \n" +
                        "       RENOVP, \n" +
                        "       RENUVP, \n" +
                        "       REBISDUP,\n" +
                        "       REESCALE, \n" +
                        "       REPISO, \n" +
                        "       RELEPU, \n" +
                        "       REMUNI, \n" +
                        "       CODPOS, \n" +
                        "       CODPRO \n" +
                        "FROM (SELECT CORR_REG, NUM_DOC, CORRELEC, NOMBRE, APELLIDO1, APELLIDO2, \n" +
                        "             CASE WHEN TFNO1 IS NOT NULL THEN (TFNO1 || ', ' || TFNO2) ELSE TFNO2 END AS TFNO, TO_CHAR(FEC_REG,'DD/MM/YYYY HH24:MI:SS') AS FECHA_ALTA, \n" +
                        "             T_TVI.TVI_COD COD_RETIVIPU, \n" +
                        "             RENOVP, RENUVP, REBISDUP, REESCALE, REPISO, RELEPU, LTRIM(SUBSTR(REMUNI,3,3),'0') REMUNI, CODPOS, CODPRO  \n" +
                        "     FROM AYUDAS_ERTE \n" +
                        "     LEFT JOIN T_TVI ON T_TVI.TVI_ABR=AYUDAS_ERTE.RETIVIPU\n" +
                        "     ORDER BY CORR_REG)\n" +
                            "GROUP BY CORR_REG, NUM_DOC, CORRELEC, NOMBRE, APELLIDO1, APELLIDO2, TFNO, FECHA_ALTA, RENOVP, RENUVP, REBISDUP, REESCALE, REPISO, RELEPU, REMUNI, CODPOS, CODPRO\n" +
                            " ORDER BY CORR_REG");
            }


            log.debug(String.format("MigracionSolicitudesAyudaDAO.obtenerSolicitudes::Query = %s", query));

            if (query == null || "".equals(query.toString())) {
                return solicitudes;
            }

            ps = con.prepareStatement(query.toString());
            rs = ps.executeQuery();

            solicitudes = new ArrayList<SolicitudVO>();
            while(rs.next()) {
                SolicitudVO solicitud = null;
                if ("ATASE".equals(codProc)) {
                    solicitud = new SolicitudVO(rs.getInt("CORR_CONSULTA"),
                        DateOperations.toCalendar(rs.getString("FECHA_ALTA"),"dd/MM/yyyy HH:mm:ss"), rs.getString("REGISTRO"));
                    solicitud.setDatosIntereComunes(rs.getString("NOMBRE"), rs.getString("APE1"), rs.getString("APE2"), 
                            rs.getString("TFNO"),rs.getString("EMAIL"),rs.getString("CIF"));
                    List<String> nombres = new ArrayList<String>();
                    solicitud.setDatosProvMunCP(rs.getString("AMBITO"), rs.getString("MUNICIPIO"));

                    for (int i = 0; i < 6; i++) {
                        nombres.add(rs.getString("ARCHIVO"+i));
                    }
                    solicitud.setNombresDocumentos(nombres);
                } else if ("ACASE".equals(codProc)) {
                    solicitud = new SolicitudVO(rs.getInt("CORR_CONSULTA"),
                        DateOperations.toCalendar(rs.getString("FECHA_ALTA"), "dd/MM/yyyy HH:mm:ss"), rs.getString("REGISTRO"));
                    solicitud.setDatosIntereComunes(rs.getString("NOMBRE"), rs.getString("APE1"), rs.getString("APE2"),
                        rs.getString("TFNO"), rs.getString("EMAIL"), rs.getString("CIF"));
                    List<String> nombres = new ArrayList<String>();
                    solicitud.setCodProvincia(MigracionSolicitudesAyudaConstantesDatos.CODPROVINCIA);
                    solicitud.setCodMunicipio(MigracionSolicitudesAyudaConstantesDatos.CODMUNICIPIO);
                    solicitud.setCodPostal("");

                    for (int i = 0; i < 9; i++) {
                        nombres.add(rs.getString("ARCHIVO"+i));
                    }
                    solicitud.setNombresDocumentos(nombres);
                } else if ("AERTE".equals(codProc)) {
                    solicitud = new SolicitudVO(rs.getInt("CORR_REG"),
                            DateOperations.toCalendar(rs.getString("FECHA_ALTA"), "dd/MM/yyyy HH:mm:ss"), "");
                    solicitud.setDatosIntereComunes(rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"),
                            rs.getString("TFNO"), rs.getString("CORRELEC"), rs.getString("NUM_DOC"));
                    solicitud.setCodProvincia(rs.getString("CODPRO"));
                    solicitud.setCodPostal(rs.getString("CODPOS"));
                    solicitud.setCodMunicipio(rs.getString("REMUNI"));
                    solicitud.setTipoVia(rs.getString("COD_RETIVIPU"));
                    solicitud.setNombreVia(rs.getString("RENOVP"));
                    solicitud.setNumeroVia(rs.getString("RENUVP"));
                    solicitud.setBisDuplicado(rs.getString("REBISDUP"));
                    solicitud.setEscalera(rs.getString("REESCALE"));
                    solicitud.setPiso(rs.getString("REPISO"));
                    solicitud.setLetra(rs.getString("RELEPU"));

                }
                solicitudes.add(solicitud);
            }
        } catch (SQLException sqle) {
                log.error("Ha ocurrido un error al obtener las solicitude de ayuda", sqle);
                throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al cerrar recursos de BBDD", ex);
                throw ex;
            }
        }

        return solicitudes;
    }

    public void indicarAnotacionNoTelematica(RegistroVO anotacion, Connection con) throws SQLException {
        PreparedStatement ps = null;
        String query;

        try {
            query = "UPDATE R_RES SET REGISTRO_TELEMATICO = 0 WHERE RES_DEP = ? AND RES_UOR = ? AND RES_TIP = ? AND RES_EJE = ? AND RES_NUM = ?";
            log.debug(String.format("MigracionSolicitudesAyudaDAO.indicarAnotacionNoTelematica::Query = %s", query));
            log.debug(String.format("MigracionSolicitudesAyudaDAO.indicarAnotacionNoTelematica::Parámetro de la query = %d-%d-%s-%s-%s",
                    anotacion.getDepartamento(), anotacion.getCodUorRegistro(), anotacion.getTipo(), anotacion.getEjercicio(), anotacion.getNumero()));

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, anotacion.getDepartamento());
            ps.setInt(contbd++, anotacion.getCodUorRegistro());
            ps.setString(contbd++, anotacion.getTipo());
            ps.setString(contbd++, anotacion.getEjercicio());
            ps.setString(contbd++, anotacion.getNumero());

            ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al indicar que la anotación no es telemática", sqle);
            throw sqle;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al cerrar recursos de BBDD", ex);
                throw ex;
            }
        }
    }

    public ArrayList<GeneralValueObject> obtenerProcPermitidos(Connection con, String codProc, int idioma) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder();
        ArrayList<GeneralValueObject> lista = null;

        try {
            //query = "Select * From Lbweb13.Em_Solicitud_Ayudas@Flbgen_Lbi.Lanbide.Lan";
            query.append("SELECT PRO_COD, PML_VALOR FROM E_PRO ");
            query.append(" INNER JOIN E_PML ON E_PML.PML_COD = E_PRO.PRO_COD ");
            query.append(" WHERE E_PRO.PRO_COD IN (" + codProc + ") ");
            query.append(" AND E_PML.PML_LENG = " + idioma);
            query.append(" ORDER BY E_PML.PML_VALOR");
            log.debug(String.format("MigracionSolicitudesAyudaDAO.obtenerProcPermitidos::Query = %s", query.toString()));

            ps = con.prepareStatement(query.toString());
            rs = ps.executeQuery();

            lista = new ArrayList<GeneralValueObject>();

            while(rs.next()) {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codigo",rs.getString("PRO_COD"));
                gVO.setAtributo("descripcion", rs.getString("PML_VALOR"));
                lista.add(gVO);
            }
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al obtener los tipos de procedimientos", sqle);
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al cerrar recursos de BBDD", ex);
                throw ex;
            }
        }

        return lista;
    }
}
