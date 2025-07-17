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

/**
 * <p>Título: @gora</p>
 * <p>Descripción: TablasAtributosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class TablasAtributosDAO
{
  private static TablasAtributosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TablasAtributosDAO.class.getName());

  protected TablasAtributosDAO()
  {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }//de la funcion

  public static TablasAtributosDAO getInstance()
  {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null)
    {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (TablasAtributosDAO.class)
      {
        if (instance == null)
        {
          instance = new TablasAtributosDAO();
        }//del if
      }//del  synchronized
    }//del if
    return instance;
  }//de la funcion


  public Vector getListaTablasAtributos(String[] params)
  {
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String parteFrom = "";
    String parteWhere = "";
    String[] joins = new String[5];
    try
    {
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      //Definimos la parte From, los campos a seleccionar
      parteFrom = "USER_TAB_COMMENTS.table_name,USER_TAB_COMMENTS.comments comTabla,"+
          "USER_COL_COMMENTS.column_name,USER_COL_COMMENTS.comments comColumna";
      //No hay parte Where, son las condiciones normales (no las de los joins)
      parteWhere = "";
      //Definimos el join
      //Tabla principal del join, tabla 1ª
      joins[0] = "USER_TAB_COMMENTS";
      //Tipo de join que permite coger los campos aunque sean nulos
      joins[1] = "LEFT";
      //Segunda tabla con la que se hará el join
      joins[2] = "USER_COL_COMMENTS";
      //Condiciones del join para las tablas anteriores
      joins[3] = "USER_TAB_COMMENTS.table_name"+"="+"USER_COL_COMMENTS.table_name";
      //Tipo de join que permite coger los campos aunque sean nulos
      joins[4] = "false";
      sql= abd.join(parteFrom,parteWhere,joins);
      String[] orden = {"USER_TAB_COMMENTS.table_name","1","USER_COL_COMMENTS.column_name","3"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      //Tenemos que separar de la consulta las tablas de los campos,
      //en un vector de GeneralValueObject, cada elemento contien:
      // 1. El nombre de la tabla
      // 2. El comentario de la tabla
      // 3. Un Vector de GeneralValueObject con;
      //    1. El nombre del campo.
      //    2. El comentario del campo
      //Variables para albergar los datos del registro
      String nombreTabla="";
      String nombreTablaAntiguo="";
      String comentarioTabla="";
      String nombreCampo="";
      String comentarioCampo="";

      //Estructuras para albergar los datos
      Vector vectorCampos=null;
      GeneralValueObject tabla=null;
      GeneralValueObject tablaAnterior=null;
      GeneralValueObject campo=null;
      while(rs.next())
      {
        nombreTabla=rs.getString("table_name");
        comentarioTabla=rs.getString("comTabla");
        nombreCampo=rs.getString("column_name");
        comentarioCampo=rs.getString("comColumna");
        if (!nombreTabla.equalsIgnoreCase(nombreTablaAntiguo))//Viene una nueva tabla
        {
          if (tablaAnterior!=null)
          {
            //Añadimos el vector de campos a la Nueva Tabla
            tablaAnterior.setAtributo("vectorCampos",vectorCampos);
            resultado.add(tablaAnterior);
          }
          //Creamos una nueva Tabla
          tabla=new GeneralValueObject();
          //Insertamos los datos de la Tabla en ésta
          tabla.setAtributo("nombreTabla",nombreTabla);
          tabla.setAtributo("comentarioTabla",comentarioTabla);
          //Creamos el vector de campos para la Tabla actual
          vectorCampos=new Vector();
          //Creamos el Campo actual
          campo=new GeneralValueObject();
          //Insertamos los datos del Campo en este
          campo.setAtributo("nombreCampo",nombreCampo);
          campo.setAtributo("comentarioCampo",comentarioCampo);
          //Añadimos el Nuevo campo al vector de campos
          vectorCampos.add(campo);
        }
        else
        {
          //Creamos el Campo actual
          campo=new GeneralValueObject();
          //Insertamos los datos del Campo en este
          campo.setAtributo("nombreCampo",nombreCampo);
          campo.setAtributo("comentarioCampo",comentarioCampo);
          //Añadimos el Nuevo campo al vector de campos
          vectorCampos.add(campo);
        }
        nombreTablaAntiguo=nombreTabla;
        tablaAnterior=tabla;
      }//del while
      if (tablaAnterior!=null)
      {
        tablaAnterior.setAtributo("vectorCampos",vectorCampos);
        resultado.add(tablaAnterior);
      }
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
