// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: TextosFijosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class TextosFijosDAO  {
  private static TextosFijosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TextosFijosDAO.class.getName());

  protected TextosFijosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TextosFijosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (TextosFijosDAO.class) {
        if (instance == null) {
          instance = new TextosFijosDAO();
        }
      }
    return instance;
  }

  public Vector eliminarTextoFijo(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_TEX WHERE " +
        campos.getString("SQL.A_TEX.codApli")+"="+gVO.getAtributo("codApli")+" AND "+
        campos.getString("SQL.A_TEX.codText")+"='"+gVO.getAtributo("codText")+"'"+" AND "+
        campos.getString("SQL.A_TEX.codIdi")+"="+gVO.getAtributo("codIdi");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      stmt.close();
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    resultado = getListaTextosFijos(gVO,params);
    return resultado;
  }

  public Vector getListaTextosFijos(GeneralValueObject parametros,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String parteWhere = "";
    String camposAseleccionar = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      //Determinamos los campos que vamos a seleccionar
      camposAseleccionar=campos.getString("SQL.A_TEX.codApli")+","+
         campos.getString("SQL.A_TEX.codText")+","+
         campos.getString("SQL.A_TEX.codIdi")+","+
         campos.getString("SQL.A_TEX.texto")+","+
         campos.getString("SQL.A_TEX.estado")+","+
         abd.convertir(campos.getString("SQL.A_TEX.fechaCreMod"),AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
         " AS fecha"+","+
         campos.getString("SQL.A_IDI.descripcion");
      //Condiciones normales fuera del join
      parteWhere=campos.getString("SQL.A_TEX.codApli")+"="+parametros.getAtributo("codApli");
      //Definimos un Array de strings para el join
      String[] joins = new String[5];
      //Tabla principal del join, tabla 1ª
      joins[0] = GlobalNames.ESQUEMA_GENERICO + "A_TEX";
      //Tipo de join que permite coger los campos aunque sean nulos
      joins[1] = "LEFT";
      //Segunda tabla con la que se hará el join
      joins[2] = GlobalNames.ESQUEMA_GENERICO + "A_IDI";
      //Condiciones del join para las tablas anteriores
      joins[3] = GlobalNames.ESQUEMA_GENERICO + "a_tex." + campos.getString("SQL.A_TEX.codIdi") + "=" +
                 GlobalNames.ESQUEMA_GENERICO + "a_idi." + campos.getString("SQL.A_IDI.codigo");
      //Para que el join se evalue de izq a der sin anidamiento
      joins[4] = "false";
      sql= abd.join(camposAseleccionar,parteWhere,joins);
      //Ordenamos por el Código de la Entidad Singular
      String[] orden = {campos.getString("SQL.A_TEX.texto"),"4"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // TextoFijo
        gVO.setAtributo("codApli",rs.getString(campos.getString("SQL.A_TEX.codApli")));
        gVO.setAtributo("codText",rs.getString(campos.getString("SQL.A_TEX.codText")));
        gVO.setAtributo("codIdi",rs.getString(campos.getString("SQL.A_TEX.codIdi")));
        gVO.setAtributo("descIdi",rs.getString(campos.getString("SQL.A_IDI.descripcion")));
        gVO.setAtributo("texto",rs.getString(campos.getString("SQL.A_TEX.texto")));
        gVO.setAtributo("estado",rs.getString(campos.getString("SQL.A_TEX.estado")));
        gVO.setAtributo("fechaCreMod",rs.getString("fecha"));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    return resultado;
  }

    public Vector modificarTextoFijo(GeneralValueObject gVO, String[] params) throws TechnicalException {
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
        AdaptadorSQLBD abd = new AdaptadorSQLBD(parametros);
        
        Connection conexion = null;
        PreparedStatement stmt = null;
        
        try {
      conexion = abd.getConnection();
            String sql = "UPDATE A_TEX SET TEX_APL = ?, TEX_COD = ?, TEX_IDI = ?, TEX_DES = ?, TEX_USO = ?, " +
                    "TEX_FEC = " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE,null) + " " +
                    "WHERE TEX_APL = ? AND TEX_COD = ? AND TEX_IDI = ?";
            
            stmt = conexion.prepareStatement(sql);
            int i = 1;
            stmt.setInt(i++, Integer.parseInt((String)gVO.getAtributo("codApli")));
            stmt.setString(i++, (String)gVO.getAtributo("codText"));
            stmt.setInt(i++, Integer.parseInt((String)gVO.getAtributo("codIdi")));
            stmt.setString(i++, (String)gVO.getAtributo("texto"));
            if (!gVO.getAtributo("estado").equals("null")) stmt.setInt(i++, Integer.parseInt((String)gVO.getAtributo("estado")));
            else stmt.setNull(i++, Types.INTEGER);
            stmt.setInt(i++, Integer.parseInt((String)gVO.getAtributo("codApli")));
            stmt.setString(i++, (String)gVO.getAtributo("codTextAntiguo"));
            stmt.setInt(i++, Integer.parseInt((String)gVO.getAtributo("codIdiAntiguo")));
            
            stmt.executeUpdate();
      stmt.close();
        
        } catch (BDException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMensaje(), ex);
        } catch (SQLException e) {
        e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        }catch (Exception e){
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(abd, conexion);
    }
        
        return getListaTextosFijos(gVO, params);
  }

  public Vector altaTextoFijo(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "INSERT INTO A_TEX("+
        campos.getString("SQL.A_TEX.codApli")+","+
        campos.getString("SQL.A_TEX.codText")+","+
        campos.getString("SQL.A_TEX.codIdi")+","+
        campos.getString("SQL.A_TEX.texto")+","+
        campos.getString("SQL.A_TEX.estado")+","+
        campos.getString("SQL.A_TEX.fechaCreMod")+
        ") VALUES (" +
        gVO.getAtributo("codApli")+",'"+
        gVO.getAtributo("codText")+"',"+
        gVO.getAtributo("codIdi")+",'"+
        gVO.getAtributo("texto")+"',"+
        gVO.getAtributo("estado")+","+
        abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE,null)+")";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    resultado = getListaTextosFijos(gVO,params);
    return resultado;
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