// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: EtiquetasDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class EtiquetasDAO
{
  private static EtiquetasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(EtiquetasDAO.class.getName());

  protected EtiquetasDAO()
  {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }//de la funcion

  public static EtiquetasDAO getInstance()
  {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null)
    {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (EtiquetasDAO.class)
      {
        if (instance == null)
        {
          instance = new EtiquetasDAO();
        }//del if
      }//del  synchronized
    }//del if
    return instance;
  }//de la funcion

  public Vector eliminarEtiqueta(String codigo, String[] params)
  {
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try
    {
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_ETI WHERE " +
        campos.getString("SQL.A_ETI.codigo")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      stmt.close();
    }
    catch (Exception e)
    {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
    finally
    {
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    Vector lista = new Vector();
    lista = getListaEtiquetas(params);
    return lista;
  }//de la funcion

  public Vector getListaEtiquetas(String[] params)
  {
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String parteFrom = "";
    String parteWhere = "";
    String[] joins = new String[11];
    try
    {
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      //Definimos la parte From, los campos a seleccionar
      parteFrom = "A_ETI.*,"+campos.getString("SQL.A_APL.nombre")+","+
          "USER_COL_COMMENTS.comments comColumna,"+
          "USER_TAB_COMMENTS.table_name,USER_TAB_COMMENTS.comments comTabla";
      //No hay parte Where, son las condiciones normales (no las de los joins)
      parteWhere = "";
      //Definimos el join
      //Tabla principal del join, tabla 1ª
      joins[0] = "A_ETI";
      //Tipo de join que permite coger los campos aunque sean nulos
      joins[1] = "LEFT";
      //Segunda tabla con la que se hará el join
      joins[2] = "USER_COL_COMMENTS";
      //Condiciones del join para las tablas anteriores
      joins[3] = campos.getString("SQL.A_ETI.codBD")+"="+"USER_COL_COMMENTS.column_name";
      //Tipo de join que permite coger los campos aunque sean nulos
      joins[4] = "LEFT";
      //Tercera tabla del join
      joins[5] = "USER_TAB_COMMENTS";
      //Condiciones del join para esta tabla con la anterior
      joins[6] = "USER_COL_COMMENTS.table_name=USER_TAB_COMMENTS.table_name";
      //Tipo de join que permite coger los campos aunque sean nulos
      joins[7] = "LEFT";
      //Tercera tabla del join
      joins[8] = GlobalNames.ESQUEMA_GENERICO + "A_APL";
      //Condiciones del join para esta tabla con la anterior
      joins[9] = GlobalNames.ESQUEMA_GENERICO + campos.getString("SQL.A_APL.codigo") + "=" +
                 campos.getString("SQL.A_ETI.codigoAplicacion");
      //Para que el join se evalue de izq a der sin anidamiento
      joins[10] = "false";
      sql= abd.join(parteFrom,parteWhere,joins);
      String[] orden = {campos.getString("SQL.A_ETI.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next())
      {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codEtiq",rs.getString(campos.getString("SQL.A_ETI.codigo")));
        gVO.setAtributo("nomEtiq",rs.getString(campos.getString("SQL.A_ETI.nombre")));
        gVO.setAtributo("descEtiq",rs.getString(campos.getString("SQL.A_ETI.descripcion")));
        gVO.setAtributo("codAplic",rs.getString(campos.getString("SQL.A_ETI.codigoAplicacion")));
        gVO.setAtributo("descAplic",rs.getString(campos.getString("SQL.A_APL.nombre")));
        gVO.setAtributo("codTabla",rs.getString("table_name"));
        gVO.setAtributo("descTabla",rs.getString("comTabla"));
        gVO.setAtributo("codCampo",rs.getString(campos.getString("SQL.A_ETI.codBD")));
        gVO.setAtributo("descCampo",rs.getString("comColumna"));
        resultado.add(gVO);
      }//del while
      rs.close();
      stmt.close();
    }
    catch (Exception e)
    {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
    finally
    {
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    return resultado;
  }//de la funcion

  public Vector modificarEtiqueta(GeneralValueObject gVO, String[] params)
  {
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try
    {
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "UPDATE A_ETI SET " +
        campos.getString("SQL.A_ETI.codigo")+"="+gVO.getAtributo("codEtiq")+","+
        campos.getString("SQL.A_ETI.descripcion")+"='"+gVO.getAtributo("descEtiq")+"',"+
        campos.getString("SQL.A_ETI.codBD")+"='"+gVO.getAtributo("codCampo")+"',"+
        campos.getString("SQL.A_ETI.nombre")+"='"+gVO.getAtributo("nomEtiq")+"',"+
        campos.getString("SQL.A_ETI.codigoAplicacion")+"="+gVO.getAtributo("codAplic")+
        " WHERE " +
        campos.getString("SQL.A_ETI.codigo")+"="+gVO.getAtributo("codEtiqAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      //m_Log.debug("las filas afectadas en el update son : " + res);
    }
    catch (Exception e)
    {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
    finally
    {
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    Vector lista = new Vector();
    lista = getListaEtiquetas(params);
    return lista;
  }//de la funcion

  public Vector altaEtiqueta(GeneralValueObject gVO, String[] params)
  {
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try
    {
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "INSERT INTO A_ETI("+
        campos.getString("SQL.A_ETI.codigo")+","+
        campos.getString("SQL.A_ETI.descripcion")+","+
        campos.getString("SQL.A_ETI.codBD")+","+
        campos.getString("SQL.A_ETI.nombre")+","+
        campos.getString("SQL.A_ETI.codigoAplicacion")+
        ") VALUES (" +
        gVO.getAtributo("codEtiq")+",'"+
        gVO.getAtributo("descEtiq")+"','"+
        gVO.getAtributo("codCampo")+"','"+
        gVO.getAtributo("nomEtiq")+"',"+
        gVO.getAtributo("codAplic")+")";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }
    catch (Exception e)
    {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
    finally
    {
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    Vector lista = new Vector();
    lista = getListaEtiquetas(params);
    return lista;
  }//de la funcion

  private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e)
  {
    try
    {
      bd.rollBack(con);
    }
    catch (Exception e1)
    {
      e1.printStackTrace();
    }
    finally
    {
      e.printStackTrace();
      m_Log.error(e.getMessage());
    }
  }//de la funcion

  private void commitTransaction(AdaptadorSQLBD bd,Connection con)
  {
    try
    {
      bd.finTransaccion(con);
      bd.devolverConexion(con);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      m_Log.error(ex.getMessage());
    }
  }//de la funcion

}//de la clase
