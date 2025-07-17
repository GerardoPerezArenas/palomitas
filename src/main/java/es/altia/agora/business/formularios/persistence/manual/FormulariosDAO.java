// NOMBRE DEL PAQUETE
package es.altia.agora.business.formularios.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: EntidadesesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class FormulariosDAO  {
  private static FormulariosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(FormulariosDAO.class.getName());

  protected FormulariosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static FormulariosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (FormulariosDAO.class) {
        if (instance == null) {
          instance = new FormulariosDAO();
        }
      }
    }
    return instance;
  }

  public Vector getListaFormulariosSinParametros(String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Date aux;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    try{
        //m_Log.debug("A por el OAD");
        abd = new AdaptadorSQLBD(params);
        //m_Log.debug("A por la conexion");
        conexion = abd.getConnection();
        abd.inicioTransaccion(conexion);
        // Creamos la select con los parametros adecuados.
        sql = "SELECT " + campos.getString("SQL.F_DEF_FORM.codigo") +
                "," + campos.getString("SQL.F_DEF_FORM.codigoVisible") +
                "," + campos.getString("SQL.F_DEF_FORM.descripcion") +
                "," + campos.getString("SQL.F_DEF_FORM.version") +
                "," + campos.getString("SQL.F_DEF_FORM.fechaAlta") +
                "," + campos.getString("SQL.F_DEF_FORM.fechaBaja") +
              " FROM F_DEF_FORM WHERE 1=1";

        String[] orden = {campos.getString("SQL.F_DEF_FORM.codigo"),"1"};
        sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigo")));
          gVO.setAtributo("codVisibleForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigoVisible")));
          gVO.setAtributo("descForm",rs.getString(campos.getString("SQL.F_DEF_FORM.descripcion")));
          gVO.setAtributo("versionForm",rs.getString(campos.getString("SQL.F_DEF_FORM.version")));
          aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaAlta"));
          gVO.setAtributo("fechaAltaForm",formato.format(aux));
          aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaBaja"));
          if (aux != null) gVO.setAtributo("fechaBajaForm",formato.format(aux));
          resultado.add(gVO);
        }
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return resultado;
  }

  public Vector getListaFormularios(GeneralValueObject gVO1,String[] params){
    TransformacionAtributoSelect transformador;
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Date aux;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    try{
        //m_Log.debug("A por el OAD");
        abd = new AdaptadorSQLBD(params);
        transformador = new TransformacionAtributoSelect(abd);
        //m_Log.debug("A por la conexion");
        conexion = abd.getConnection();
        abd.inicioTransaccion(conexion);
        // Creamos la select con los parametros adecuados.
        sql = "SELECT " + campos.getString("SQL.F_DEF_FORM.codigo") +
                "," + campos.getString("SQL.F_DEF_FORM.codigoVisible") +
                "," + campos.getString("SQL.F_DEF_FORM.descripcion") +
                "," + campos.getString("SQL.F_DEF_FORM.version") +
                "," + campos.getString("SQL.F_DEF_FORM.fechaAlta") +
                "," + campos.getString("SQL.F_DEF_FORM.fechaBaja") +
              " FROM F_DEF_FORM WHERE 1=1";

        if (gVO1 == null) gVO1 = new GeneralValueObject();
        //CODIGO FORMULARIO
        if (!(gVO1.getAtributo("codFormulario").equals(null)) && !(gVO1.getAtributo("codFormulario").equals("")))
            sql = sql+" AND "+campos.getString("SQL.F_DEF_FORM.codigoVisible")+"='"+gVO1.getAtributo("codFormulario")+"'";
        //NOMBRE FORMULARIO
        String condicion = transformador.construirCondicionWhereConOperadores(campos.getString("SQL.F_DEF_FORM.descripcion"), gVO1.getAtributo("descFormulario").toString(),true);
        if (!(gVO1.getAtributo("descFormulario").equals(null)) && !(gVO1.getAtributo("descFormulario").equals("")))
            sql = sql+" AND "+condicion;
        //TIPO FORMULARIO
        if (!(gVO1.getAtributo("codTipo").equals(null)) && !(gVO1.getAtributo("codTipo").equals("")))
            sql = sql+" AND "+campos.getString("SQL.F_DEF_FORM.tipo")+"='"+gVO1.getAtributo("codTipo")+"'";
        //PROCEDIMIENTO FORMULARIO
        if (!(gVO1.getAtributo("codProcedimiento").equals(null)) && !(gVO1.getAtributo("codProcedimiento").equals("")))
            sql = sql+" AND "+campos.getString("SQL.F_DEF_FORM.procedimiento")+"='"+gVO1.getAtributo("codProcedimiento")+"'";

        String[] orden = {campos.getString("SQL.F_DEF_FORM.codigo"),"1"};
        sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigo")));
          gVO.setAtributo("codVisibleForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigoVisible")));
          gVO.setAtributo("descForm",rs.getString(campos.getString("SQL.F_DEF_FORM.descripcion")));
          gVO.setAtributo("versionForm",rs.getString(campos.getString("SQL.F_DEF_FORM.version")));
          aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaAlta"));
          gVO.setAtributo("fechaAltaForm",formato.format(aux));
          aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaBaja"));
          if (aux != null) gVO.setAtributo("fechaBajaForm",formato.format(aux));
          resultado.add(gVO);
        }
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return resultado;
  }

  public Vector getListaFormulariosTipo0(String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Date aux;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    try{
          //m_Log.debug("A por el OAD");
          abd = new AdaptadorSQLBD(params);
          //m_Log.debug("A por la conexion");
          conexion = abd.getConnection();
          abd.inicioTransaccion(conexion);
          // Creamos la select con los parametros adecuados.
          sql = "SELECT " + campos.getString("SQL.F_DEF_FORM.codigo") +
                  "," + campos.getString("SQL.F_DEF_FORM.codigoVisible") +
                  "," + campos.getString("SQL.F_DEF_FORM.descripcion") +
                  "," + campos.getString("SQL.F_DEF_FORM.version") +
                  "," + campos.getString("SQL.F_DEF_FORM.fechaAlta") +
                  "," + campos.getString("SQL.F_DEF_FORM.fechaBaja") +
                  "," + campos.getString("SQL.F_DEF_FORM.procedimiento") +
                " FROM F_DEF_FORM WHERE " + campos.getString("SQL.F_DEF_FORM.tipo")+"='0'" +
                  " AND "+campos.getString("SQL.F_DEF_FORM.fechaBaja")+" IS NULL" +
                  " AND "+campos.getString("SQL.F_DEF_FORM.visible")+"='1'";

          String[] orden = {campos.getString("SQL.F_DEF_FORM.codigo"),"1"};
          sql += abd.orderUnion(orden);
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigo")));
            gVO.setAtributo("codVisibleForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigoVisible")));
            gVO.setAtributo("descForm",rs.getString(campos.getString("SQL.F_DEF_FORM.descripcion")));
            gVO.setAtributo("versionForm",rs.getString(campos.getString("SQL.F_DEF_FORM.version")));
            gVO.setAtributo("procedimiento",rs.getString(campos.getString("SQL.F_DEF_FORM.procedimiento")));
            aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaAlta"));
            gVO.setAtributo("fechaAltaForm",formato.format(aux));
            aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaBaja"));
            if (aux != null) gVO.setAtributo("fechaBajaForm",formato.format(aux));
            resultado.add(gVO);
          }
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return resultado;
  }

    /**
     * Recuperar la lista de formularios de solicitud que puede iniciar un
     * usuario teniendo en cuenta su uor y su cargo.
     * Devuelve la descripcion del area, no su codigo
     * Solo lo usu catalogoFormularios
     * @param params
     * @param UOR
     * @param cargo
     * @return Vector de GeneralValueObject
     */
    public Vector getListaFormulariosTipo0Restricciones(String[] params, String loginUsuario, String cargo){
      Vector resultado = new Vector();
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "";
      String sql1 = "";
      String sql2 = "";
      String sql3 = "";
      Date aux;
      SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
      try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            /**
             * Creamos la select como 3 uniones para obtener los formularios que:
             * 1 = tiene fijadas restricciones pero el usuario puede verlos igual.
             * 2 = tienen fijados permisos y el usuario tienen alguno de ellos.
             * 3 = son accesibles a todas las unidades por lo que cualquier usuario puede verlos.
             */
            sql1 = "SELECT FDF_CODIGO, FDF_COD_VIS, FDF_NOMBRE, FDF_VERSION, FDF_FALTA, FDF_FBAJA,"
                  + "PRO_COD, FDF_INSTRUC," + campos.getString("SQL.A_AML.valor") + ","
                  + campos.getString("SQL.F_DEF_FORM.area")
                  + " FROM F_DEF_FORM, " + GlobalNames.ESQUEMA_GENERICO + "A_AML WHERE "
                  + campos.getString("SQL.F_DEF_FORM.area") + " = "
                  + campos.getString("SQL.A_AML.codArea")
                  + " AND FTF_COD='0' AND (FDF_FBAJA IS NULL OR FDF_FBAJA >"
                  + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)
                  + ") AND FDF_VISIBLE='1' AND FDF_UNIDADES='0' AND FDF_TIPO_RESTRICCION ='0' AND "
                  + " EXISTS (SELECT * FROM USUARIO_TEMP_PERMISOS WHERE USU_UOR_LOGIN='" + loginUsuario
                  + "' AND NOT EXISTS (SELECT * FROM F_RESTRICCION WHERE ((R_UOR=USU_UOR_COD AND R_CAR=USU_UOR_CARGO) "
                  + "OR(R_UOR=USU_UOR_COD AND R_CAR=0)) AND R_FORM=FDF_CODIGO)) ";

          sql2 = "SELECT FDF_CODIGO, FDF_COD_VIS, FDF_NOMBRE, FDF_VERSION, FDF_FALTA, FDF_FBAJA,"
                  + "PRO_COD, FDF_INSTRUC," + campos.getString("SQL.A_AML.valor") + ","
                  + campos.getString("SQL.F_DEF_FORM.area")
                  + " FROM F_DEF_FORM, " + GlobalNames.ESQUEMA_GENERICO + "A_AML WHERE "
                  + campos.getString("SQL.F_DEF_FORM.area") + " = "
                  + campos.getString("SQL.A_AML.codArea")
                  + " AND FTF_COD='0' AND (FDF_FBAJA IS NULL OR FDF_FBAJA >"
                  + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)
                  + ") AND FDF_VISIBLE='1' AND FDF_UNIDADES='0' AND FDF_TIPO_RESTRICCION ='1' AND"
                  + " EXISTS (SELECT * FROM USUARIO_TEMP_PERMISOS WHERE USU_UOR_LOGIN='" + loginUsuario
                  + "' AND EXISTS (SELECT * FROM F_RESTRICCION WHERE ((R_UOR=USU_UOR_COD AND R_CAR=USU_UOR_CARGO) "
                  + "OR(R_UOR=USU_UOR_COD AND R_CAR=0)) AND R_FORM=FDF_CODIGO)) ";

          sql3 = "SELECT FDF_CODIGO, FDF_COD_VIS, FDF_NOMBRE, FDF_VERSION, FDF_FALTA, FDF_FBAJA,"
                  + "PRO_COD, FDF_INSTRUC," + campos.getString("SQL.A_AML.valor") + ","
                  + campos.getString("SQL.F_DEF_FORM.area")
                  + " FROM F_DEF_FORM, " + GlobalNames.ESQUEMA_GENERICO + "A_AML WHERE "
                  + campos.getString("SQL.F_DEF_FORM.area") + " = "
                  + campos.getString("SQL.A_AML.codArea")
                  + " AND FTF_COD='0' AND (FDF_FBAJA IS NULL OR FDF_FBAJA >"
                  + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)
                  + ") AND FDF_VISIBLE='1' AND FDF_UNIDADES='1' ";

           


            String[] orden = {campos.getString("SQL.F_DEF_FORM.area"),"9",campos.getString("SQL.F_DEF_FORM.descripcion"),"3"};
            sql = sql1 + " UNION " + sql2 + " UNION " + sql3 + abd.orderUnion(orden);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
              GeneralValueObject gVO = new GeneralValueObject();
              gVO.setAtributo("codForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigo")));
              gVO.setAtributo("codVisibleForm",rs.getString(campos.getString("SQL.F_DEF_FORM.codigoVisible")));
              gVO.setAtributo("descForm",rs.getString(campos.getString("SQL.F_DEF_FORM.descripcion")));
              gVO.setAtributo("versionForm",rs.getString(campos.getString("SQL.F_DEF_FORM.version")));
              gVO.setAtributo("procedimiento",rs.getString(campos.getString("SQL.F_DEF_FORM.procedimiento")));
              aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaAlta"));
              gVO.setAtributo("fechaAltaForm",formato.format(aux));
              aux = rs.getDate(campos.getString("SQL.F_DEF_FORM.fechaBaja"));
              if (aux != null) gVO.setAtributo("fechaBajaForm",formato.format(aux));
              gVO.setAtributo("instruc",rs.getString(campos.getString("SQL.F_DEF_FORM.instruc")));
              gVO.setAtributo("area", rs.getString(campos.getString("SQL.A_AML.valor")));
              resultado.add(gVO);
            }
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransaction(abd,conexion);
      }
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