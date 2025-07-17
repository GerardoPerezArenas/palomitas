// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionTramitesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class PendientesPorUnidadDAO {

  private static PendientesPorUnidadDAO instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
            LogFactory.getLog(PendientesPorUnidadDAO.class.getName());

  protected static String uor_nom;
  protected static String uor_cod;
  //protected static String uor_dep;

  protected PendientesPorUnidadDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    uor_nom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
    uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
    //uor_dep = m_ConfigTechnical.getString("SQL.A_UOR.departamento");

  }

  public static PendientesPorUnidadDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized(PendientesPorUnidadDAO.class) {
        if (instance == null) {
          instance = new PendientesPorUnidadDAO();
        }
      }
    }
    return instance;
  }

  public Vector getListaUnidades(String[] params)
      throws AnotacionRegistroException, TechnicalException{

    //Queremos estar informados de cuando este metod es ejecutado
    m_Log.debug("getListaUnidades");

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    String sql = "";
    Vector list = new Vector();
    ElementoListaValueObject elemListVO;
    int orden = 0;

    try{
      AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      st = con.createStatement();
      sql = "SELECT " + uor_cod + ","+ uor_nom + " FROM  A_UOR";

      String parametros[] = {"2","2"};
      sql += oad.orderUnion(parametros);

      if(m_Log.isDebugEnabled()) m_Log.debug("PendientesPorUnidadDAO, getListaUnidades: Sentencia SQL:" + sql);
      rs = st.executeQuery(sql);
      while(rs.next()){
        elemListVO = new ElementoListaValueObject(rs.getString(uor_cod),rs.getString(uor_nom),orden++);
        list.addElement(elemListVO);
      }
      m_Log.debug("PendientesPorUnidadDAO, getListaUnidades: Lista unidades cargada");
      if(m_Log.isDebugEnabled()) m_Log.debug("PendientesPorUnidadDAO, getListaUnidades: Tamaño lista:" + list.size());
    }catch (Exception e) {
      list = null;
      e.printStackTrace();
      m_Log.error(e.getMessage());
      throw new AnotacionRegistroException(m_ConfigError.getString("Error.PendientesPorUnidadDAO.getListaProcedimientos"), e);
    }finally {
      try{
          if (st!=null) st.close();
          if (rs!=null) rs.close();
          if(con != null) con.close();
      } catch(SQLException sqle) {
           sqle.printStackTrace();
           m_Log.error("SQLException del finally: " + sqle.getMessage());
      }
    }
    //Queremos estar informados de cuando este metodo ha finalizado
    m_Log.debug("getListaUnidades");
    return list;
  }
}