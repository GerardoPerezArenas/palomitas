package es.altia.agora.business.sge.persistence.manual;

import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa la búsqueda y actualización de técnicos de referencia.
 * @author alberto.pulpeiro
 */
public class TenicoReferenciaDAO {
    //Para el fichero de configuracion tecnico.

    protected static Config conf;
    //Para informacion de logs.
    protected static Log m_Log
            = LogFactory.getLog(TenicoReferenciaDAO.class.getName());

    private static TenicoReferenciaDAO instance = null;

    protected TenicoReferenciaDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        conf = ConfigServiceHelper.getConfig("techserver");
    }

    public static TenicoReferenciaDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized (TenicoReferenciaDAO.class) {
                if (instance == null) {
                    instance = new TenicoReferenciaDAO();
                }
            }
        }
        return instance;
    }

    public List<TecnicoReferenciaDTO> loadTecnicosReferencia(String[] params) {
        List<TecnicoReferenciaDTO> listaTecnico = new ArrayList<TecnicoReferenciaDTO>();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            String sql = "SELECT NUMERO_DOCUMENTO_TECNICA,NOMBRE_TECNICA,APELLIDO1_TECNICA, APELLIDO2_TECNICA FROM " + GlobalNames.ESQUEMA_GENERICO + "S_VW_OR_TECNICOS_LISTA ORDER BY APELLIDO1_TECNICA ASC,APELLIDO2_TECNICA ASC,NOMBRE_TECNICA ASC";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql: " + sql);
            }
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TecnicoReferenciaDTO dto = new TecnicoReferenciaDTO();
                dto.setDocumentosIdentificacion(rs.getString("NUMERO_DOCUMENTO_TECNICA"));
                dto.setNombre(rs.getString("NOMBRE_TECNICA"));
                dto.setApellido1(rs.getString("APELLIDO1_TECNICA"));
                dto.setApellido2(rs.getString("APELLIDO2_TECNICA"));
                listaTecnico.add(dto);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en loadTecnicosReferencia: " + sqle.toString());
        } catch (BDException bde) {
            m_Log.error("error del OAD en el metodo loadTecnicosReferencia: "
                    + bde.toString());
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch (BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return listaTecnico;
    }

}
