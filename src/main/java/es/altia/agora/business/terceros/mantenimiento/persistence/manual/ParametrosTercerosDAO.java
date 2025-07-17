// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.terceros.ParametrosTerceroValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: ParametrosTercerosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ParametrosTercerosDAO  {
  private static ParametrosTercerosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(ParametrosTercerosDAO.class.getName());

  protected ParametrosTercerosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ParametrosTercerosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (ParametrosTercerosDAO.class) {
        if (instance == null) {
          instance = new ParametrosTercerosDAO();
        }
      }
    }
    return instance;
  }

  public ParametrosTerceroValueObject getParametrosTerceros(int orgCod, String[] params){
    ParametrosTerceroValueObject resultado = new ParametrosTerceroValueObject();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt;
    ResultSet rs;
    String sql;
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
//      abd.inicioTransaccion(conexion);
      // Creamos la select con los parametros adecuados.
      /*sql = "SELECT T_PRM.*,T_PAI.*,T_PRV.*,T_MUN.*,T_TID.*,"+
        "TOC_PR."+campos.getString("SQL.T_TOC.codigoUso")+" AS COD_TOC_PR,"+
        "TOC_PR."+campos.getString("SQL.T_TOC.descUso")+" AS DESC_TOC_PR,"+
        "T_TOC.* "+
        "FROM T_PRM,T_PAI,T_PRV,T_MUN,T_TID,T_TOC TOC_PR,T_TOC WHERE ";
      // CONDICIONES  
      sql+= campos.getString("SQL.T_PRM.codigo")+"=0 AND "+
        campos.getString("SQL.T_PRM.pais")+"="+campos.getString("SQL.T_PAI.idPais")+"(+) AND "+
        campos.getString("SQL.T_PRM.ppa")+"="+campos.getString("SQL.T_PRV.idPais")+"(+) AND "+
        campos.getString("SQL.T_PRM.provincia")+"="+campos.getString("SQL.T_PRV.idProvincia")+"(+) AND "+
        campos.getString("SQL.T_PRM.mpa")+"="+campos.getString("SQL.T_MUN.idPais")+"(+) AND "+
        campos.getString("SQL.T_PRM.mpr")+"="+campos.getString("SQL.T_MUN.idProvincia")+"(+) AND "+
        campos.getString("SQL.T_PRM.municipio")+"="+campos.getString("SQL.T_MUN.idMunicipio")+"(+) AND "+
        campos.getString("SQL.T_PRM.tid")+"="+campos.getString("SQL.T_TID.codigo")+"(+) AND "+
        campos.getString("SQL.T_PRM.ocupacion")+"=T_TOC."+campos.getString("SQL.T_TOC.codigoUso")+"(+) AND "+
        campos.getString("SQL.T_PRM.ocupacionPrincipal")+"=TOC_PR."+campos.getString("SQL.T_TOC.codigoUso");
      */
      String select = "T_PRM.*,T_PAI.*,T_PRV.*,T_MUN.*,T_TID.*,"+
        "TOC_PR."+campos.getString("SQL.T_TOC.codigoUso")+" AS COD_TOC_PR,"+
        "TOC_PR."+campos.getString("SQL.T_TOC.descUso")+" AS DESC_TOC_PR,"+
        "T_TOC.* ";
      String condiciones = campos.getString("SQL.T_PRM.codigo") + "=" + orgCod;
      String[] joins = new String[20];
      joins[0] = "T_PRM";
      joins[1] = "LEFT";
      joins[2] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
      joins[3] = campos.getString("SQL.T_PRM.pais")+"="+campos.getString("SQL.T_PAI.idPais");
      joins[4] = "LEFT";
      joins[5] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
      joins[6] = campos.getString("SQL.T_PRM.ppa")+"="+campos.getString("SQL.T_PRV.idPais")+" AND "+
        campos.getString("SQL.T_PRM.provincia")+"="+campos.getString("SQL.T_PRV.idProvincia");
      joins[7] = "LEFT";
      joins[8] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
      joins[9] = campos.getString("SQL.T_PRM.mpa")+"="+campos.getString("SQL.T_MUN.idPais")+" AND "+
        campos.getString("SQL.T_PRM.mpr")+"="+campos.getString("SQL.T_MUN.idProvincia")+" AND "+
        campos.getString("SQL.T_PRM.municipio")+"="+campos.getString("SQL.T_MUN.idMunicipio");
      joins[10] = "LEFT";
      joins[11] = "T_TID";
      joins[12] = campos.getString("SQL.T_PRM.tid")+"="+campos.getString("SQL.T_TID.codigo");
      joins[13] = "LEFT";
      joins[14] = "T_TOC";
      joins[15] = campos.getString("SQL.T_PRM.ocupacion")+"=T_TOC."+campos.getString("SQL.T_TOC.codigoUso");
      joins[16] = "INNER";
      joins[17] = "T_TOC TOC_PR";
      joins[18] = campos.getString("SQL.T_PRM.ocupacionPrincipal")+"=TOC_PR."+campos.getString("SQL.T_TOC.codigoUso");
      joins[19] = "false";
      sql=abd.join(select,condiciones,joins);
        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        resultado.setPais(rs.getString(campos.getString("SQL.T_PAI.idPais")));
        resultado.setNomPais(rs.getString(campos.getString("SQL.T_PAI.nombre")));
        resultado.setProvincia(rs.getString(campos.getString("SQL.T_PRV.idProvincia")));
        resultado.setNomProvincia(rs.getString(campos.getString("SQL.T_PRV.nombre")));
        resultado.setMunicipio(rs.getString(campos.getString("SQL.T_MUN.idMunicipio")));
        resultado.setNomMunicipio(rs.getString(campos.getString("SQL.T_MUN.nombre")));
        resultado.setTipoDocumento(rs.getString(campos.getString("SQL.T_TID.codigo")));
        resultado.setNomTipoDocumento(rs.getString(campos.getString("SQL.T_TID.nombre")));
        resultado.setTipoOcupacion(rs.getString(campos.getString("SQL.T_TOC.codigoUso")));
        resultado.setNomTipoOcupacion(rs.getString(campos.getString("SQL.T_TOC.descUso")));
        resultado.setTipoOcupacionPrincipal(rs.getString("COD_TOC_PR"));
        resultado.setNomTipoOcupacionPrincipal(rs.getString("DESC_TOC_PR"));
        resultado.setIdentificadorMultiple(rs.getString(campos.getString("SQL.T_PRM.multiple")));
        resultado.setLugar(rs.getString(campos.getString("SQL.T_PRM.lugar")));
      }
    }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try {
//      commitTransaction(abd,conexion);
            abd.devolverConexion(conexion);
        }catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ParametrosTercerosDAO.getParametrosTercero");
        }
    }
    return resultado;
  }

  public void modificarParametrosTerceros(ParametrosTerceroValueObject ptVO, String[] params) throws SQLException {
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;

    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
            String sql = "UPDATE T_PRM SET " +
        campos.getString("SQL.T_PRM.pais")+"="+ptVO.getPais()+","+
        campos.getString("SQL.T_PRM.ppa")+"="+ptVO.getPaisProvincia()+","+
        campos.getString("SQL.T_PRM.provincia")+"="+ptVO.getProvincia()+","+ 
        campos.getString("SQL.T_PRM.mpa")+"="+ptVO.getPaisMunicipio()+","+
        campos.getString("SQL.T_PRM.mpr")+"="+ptVO.getProvinciaMunicipio()+","+
        campos.getString("SQL.T_PRM.municipio")+"="+ptVO.getMunicipio()+","+
        campos.getString("SQL.T_PRM.tid")+"="+ptVO.getTipoDocumento()+","+ 
        campos.getString("SQL.T_PRM.ocupacion")+"="+ptVO.getTipoOcupacion()+","+ 
        campos.getString("SQL.T_PRM.ocupacionPrincipal")+"="+ptVO.getTipoOcupacionPrincipal()+","+
        campos.getString("SQL.T_PRM.multiple")+"="+ptVO.getIdentificadorMultiple()+","+ 
        campos.getString("SQL.T_PRM.lugar")+"='"+ptVO.getLugar()+"',"+
        campos.getString("SQL.T_PRM.usuario")+"="+ptVO.getUsuario()+","+ 
                    campos.getString("SQL.T_PRM.fecha") + " = " +
                    abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null);

      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
            if (stmt == null) stmt.close();
    }
  }

  private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
    try {
      bd.rollBack(con);
    }catch (Exception e1) {
      e1.printStackTrace();
    }finally {
      e.printStackTrace();
      m_Log.error(e.getMessage());
    }
  }

  private void commitTransaction(AdaptadorSQLBD bd,Connection con){
    try{
      bd.finTransaccion(con);
      bd.devolverConexion(con);
    }catch (Exception ex) {
      ex.printStackTrace();
      m_Log.error(ex.getMessage());
    }
  }   
  
}