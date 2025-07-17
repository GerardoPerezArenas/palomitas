package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class MantTemaDAO {

  //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
  //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
  //Para informacion de logs.
    protected static Log m_Log =
          LogFactory.getLog(MantTemaDAO.class.getName());

    private static MantTemaDAO instance = null;

    protected static String tem_ide;
    protected static String tem_cod;
    protected static String tem_des;

    protected static String ret_tem;
    protected static String res_fec;

  protected MantTemaDAO() {
    super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        tem_ide = m_ConfigTechnical.getString("SQL.R_TEM.identificador");
        tem_cod = m_ConfigTechnical.getString("SQL.R_TEM.codigo");
        tem_des = m_ConfigTechnical.getString("SQL.R_TEM.descripcion");

        ret_tem = m_ConfigTechnical.getString("SQL.R_RET.idTema");
        res_fec =  m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");

  }

  public static MantTemaDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(MantTemaDAO.class){
                if (instance == null)
                    instance = new MantTemaDAO();
                }
        }
        return instance;
  }

  public Vector loadTemas(String[] params) throws TechnicalException {

    Vector temas = new Vector();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    AdaptadorSQLBD oad = null;

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      st = con.createStatement();

      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_TEMAS = "SELECT " + tem_ide + ", " + tem_cod + ", " + tem_des + " FROM R_TEM ORDER BY " + tem_des;
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS);
      rs = st.executeQuery(SQL_BUSCA_TEMAS);
      while(rs.next()){
        MantTemasValueObject mantTemasVO = new MantTemasValueObject(rs.getInt(tem_ide), rs.getString(tem_cod), rs.getString(tem_des), "");
        temas.addElement(mantTemasVO);
      }

      rs.close();
      st.close();
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
      //Aquí se pueden lanzar TechnicalException que no se capturan.
        try{
            oad.devolverConexion(con);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
  return temas;
  }
  
  public Vector buscaTemasDescripcion(String[] params,String nombreTema) throws TechnicalException {

    Vector temas = new Vector();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    TransformacionAtributoSelect transformador;
    AdaptadorSQLBD oad = null;

    try{
      oad = new AdaptadorSQLBD(params);
      transformador = new TransformacionAtributoSelect(oad);
      con = oad.getConnection();
      st = con.createStatement();

      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_TEMAS = "SELECT " + tem_ide + ", " + tem_cod + ", " + tem_des + " FROM R_TEM ";
      if(nombreTema != null && !"".equals(nombreTema)) {
				 String condicion = transformador.construirCondicionWhereConOperadores(tem_des, nombreTema.trim(),false); 
				 if (!"".equals(condicion)) {
			   	SQL_BUSCA_TEMAS += " WHERE " + condicion + " ";
			  }	
			}
      SQL_BUSCA_TEMAS += " ORDER BY " + tem_des;
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS);
      rs = st.executeQuery(SQL_BUSCA_TEMAS);
      while(rs.next()){
        MantTemasValueObject mantTemasVO = new MantTemasValueObject(rs.getInt(tem_ide), rs.getString(tem_cod), rs.getString(tem_des), "");
        temas.addElement(mantTemasVO);
      }

      rs.close();
      st.close();
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
      //Aquí se pueden lanzar TechnicalException que no se capturan.
        try{
            oad.devolverConexion(con);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
  return temas;
  }


  public Vector loadTemasUtilizados(String[] params) throws TechnicalException {

    Vector temasUtilizados = new Vector();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    AdaptadorSQLBD oad = null;

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      st = con.createStatement();

      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + ret_tem + " FROM R_RET WHERE " + ret_tem + " IS NOT NULL";
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
      rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
      while(rs.next()){
        temasUtilizados.addElement(rs.getString(ret_tem));
      }

      rs.close();
      st.close();
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
      //Aquí se pueden lanzar TechnicalException que no se capturan.
        try{
            oad.devolverConexion(con);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
  return temasUtilizados;
  }

  public Vector loadTemasUtilizadosCerrados(String fechaCerradoE,String fechaCerradoS,String[] params) throws TechnicalException {

       Vector temasUtilizadosCerrados = new Vector();
       Connection con = null;
       Statement st = null;
       ResultSet rs = null;
       AdaptadorSQLBD oad = null;

       try{
         oad = new AdaptadorSQLBD(params);
         con = oad.getConnection();
         st = con.createStatement();

         // Creamos la select con los parametros adecuados.
         String SQL_BUSCA_TEMAS_UTIL;
         if(!fechaCerradoE.equals("")) {
           SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT R_RET." + ret_tem + " FROM R_RET,R_RES WHERE (R_RES." + res_fec + " <= " +
                   oad.convertir("'" + fechaCerradoE + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                   " AND " + ret_tem + " IS NOT NULL)";
           if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
           rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
           while(rs.next()){
             temasUtilizadosCerrados.addElement(rs.getString(ret_tem));
           }
           rs.close();
         }

         if(!fechaCerradoS.equals("")) {
           SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT R_RET." + ret_tem + " FROM R_RET,R_RES WHERE (R_RES." + res_fec + " <=" +
                   oad.convertir("'" + fechaCerradoS + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                   " AND " + ret_tem + " IS NOT NULL)";
           if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
           rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
           while(rs.next()){
             temasUtilizadosCerrados.addElement(rs.getString(ret_tem));
           }
           rs.close();
         }
         st.close();
       }catch (Exception e){
           e.printStackTrace();
           if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
       }finally{
         //Aquí se pueden lanzar TechnicalException que no se capturan.
           try{
               oad.devolverConexion(con);
           }catch (Exception e){
               e.printStackTrace();
               if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
           }
       }
    return temasUtilizadosCerrados;
    }

  public Vector eliminarTema(MantTemasValueObject temasVO, String[] params) throws TechnicalException,BDException {

  AdaptadorSQLBD abd = null;
  Connection conexion = null;
  Statement stmt = null;
  String sql = "";

  try{

    m_Log.debug("A por el OAD");
    abd = new AdaptadorSQLBD(params);
    m_Log.debug("A por la conexion");
    conexion = abd.getConnection();

    int identificador = temasVO.getIde();
    m_Log.debug("la descripcion en el DAO es : " + identificador);

    sql = "DELETE FROM R_TEM WHERE " + tem_ide + " =" + identificador ;
    if(m_Log.isDebugEnabled()) m_Log.debug(sql);

    PreparedStatement ps = conexion.prepareStatement(sql);
    int res = ps.executeUpdate();
    m_Log.debug("las filas afectadas en el delete son : " + res);

    } catch (Exception e) {
      e.printStackTrace();
      if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    } finally {
      try{
          abd.devolverConexion(conexion);
      }catch (Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }
    }

    Vector lista = new Vector();
    lista = loadTemas(params);
    return lista;

  }

  public Vector modificarTema(MantTemasValueObject temasVO, String[] params) throws TechnicalException,BDException {

AdaptadorSQLBD abd = null;
Connection conexion = null;
Statement stmt = null;
String sql = "";

try{

  m_Log.debug("A por el OAD");
  abd = new AdaptadorSQLBD(params);
  m_Log.debug("A por la conexion");
  conexion = abd.getConnection();

  int identificador = temasVO.getIde();
  String codigo = temasVO.getCodigo();
  String descripcion = temasVO.getTxtNomeDescripcion();
  m_Log.debug("el identificador en el DAO es : " + identificador);

  sql = "UPDATE R_TEM SET " + tem_cod + " = '" + codigo + "', " + tem_des + " = '" + descripcion + "' WHERE " + tem_ide + " = " + identificador;
  if(m_Log.isDebugEnabled()) m_Log.debug(sql);

  PreparedStatement ps = conexion.prepareStatement(sql);
  int res = ps.executeUpdate();
  m_Log.debug("las filas afectadas en el update son : " + res);

  } catch (Exception e) {
    e.printStackTrace();
    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
  } finally {
    try{
        abd.devolverConexion(conexion);
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
  }

  Vector lista = new Vector();
  lista = loadTemas(params);
  return lista;

  }

  public Vector altaTema(MantTemasValueObject temasVO, String[] params) throws TechnicalException,BDException {

AdaptadorSQLBD abd = null;
Connection conexion = null;
Statement stmt = null;
ResultSet rs = null;
String sql = "";

try{

  m_Log.debug("A por el OAD");
  abd = new AdaptadorSQLBD(params);
  m_Log.debug("A por la conexion");
  conexion = abd.getConnection();
  stmt = conexion.createStatement();

  int ideMax = 0;
  sql = "SELECT " + abd.funcionMatematica(AdaptadorSQL.FUNCIONMATEMATICA_MAX, new String[]{tem_ide}) + " FROM R_TEM";
  if(m_Log.isDebugEnabled()) m_Log.debug(sql);        
  rs = stmt.executeQuery(sql);
  while(rs.next()){
    ideMax = rs.getInt(1);
  }

  m_Log.debug("el identificador más grande es : " + ideMax);

  String codigo = temasVO.getCodigo();
  String descripcion = temasVO.getTxtNomeDescripcion();

  sql = "INSERT INTO R_TEM VALUES( " + (ideMax + 1) + ", '" + codigo + "', '" + descripcion + "')";
  if(m_Log.isDebugEnabled()) m_Log.debug(sql);

  PreparedStatement ps = conexion.prepareStatement(sql);
  int res = ps.executeUpdate();
  m_Log.debug("las filas afectadas en el insert son : " + res);


  } catch (Exception e) {
    e.printStackTrace();
    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
  } finally {
    try{
        abd.devolverConexion(conexion);
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
  }

  Vector lista = new Vector();
  lista = loadTemas(params);
  return lista;

  }


}