package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class MantRemitenteDAO {

    //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
    //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
    //Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(MantRemitenteDAO.class.getName());

    private static MantRemitenteDAO instance = null;

    protected static String tem_ide;
    protected static String tem_cod;
    protected static String tem_des;
    protected static String tem_act;

    protected static String res_tpe;
    protected static String res_fec;

    protected MantRemitenteDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        tem_ide = m_ConfigTechnical.getString("SQL.R_TPE.identificador");
        tem_cod = m_ConfigTechnical.getString("SQL.R_TPE.codigo");
        tem_des = m_ConfigTechnical.getString("SQL.R_TPE.descripcion");
        tem_act = m_ConfigTechnical.getString("SQL.R_TPE.activo");

        res_tpe = m_ConfigTechnical.getString("SQL.R_RES.tipoRemitente");
        res_fec =  m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");

      }

  public static MantRemitenteDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(MantRemitenteDAO.class){
                if (instance == null)
                    instance = new MantRemitenteDAO();
                }
        }
        return instance;
  }

  public Vector loadTemas(String[] params) throws TechnicalException, BDException {

          Vector temas = new Vector();
    //Usar el JDBCWrapper es mas sencillo que usar JDBC directamente
    AdaptadorSQLBD abd = null;
    Connection conexion = null;

    try{

      m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      m_Log.debug("A por la conexion");
      conexion = abd.getConnection();

      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_TEMAS = "SELECT " + tem_ide + ", " + tem_cod + ", " + tem_des + ", " + tem_act + " FROM R_TPE ORDER BY " + tem_des;
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS);

      PreparedStatement ps = conexion.prepareStatement(SQL_BUSCA_TEMAS);
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        MantTemasValueObject mantTemasVO = new MantTemasValueObject(rs.getInt(tem_ide), rs.getString(tem_cod), rs.getString(tem_des),"",rs.getString(tem_act));
        temas.addElement(mantTemasVO);
      }
      rs.close();
      ps.close();
    } catch (Exception e) {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
    finally {
        try{
            abd.devolverConexion(conexion);
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
                String SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_tpe + " FROM R_RES WHERE " + res_tpe + " IS NOT NULL";
                m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
                rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
                while(rs.next()){
                  temasUtilizados.addElement(rs.getString(res_tpe));
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
                  SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_tpe + " FROM R_RES WHERE (" + res_fec + " <= " +
                          oad.convertir("'" + fechaCerradoE + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                          " AND " + res_tpe + " IS NOT NULL)";
                  if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
                  rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
                  while(rs.next()){
                    temasUtilizadosCerrados.addElement(rs.getString(res_tpe));
                  }
                  rs.close();
                }

                if(!fechaCerradoS.equals("")) {
                  SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_tpe + " FROM R_RES WHERE (" + res_fec + " <= " +
                          oad.convertir("'" + fechaCerradoS + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                          " AND " + res_tpe + " IS NOT NULL)";
                  if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
                  rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
                  while(rs.next()){
                    temasUtilizadosCerrados.addElement(rs.getString(res_tpe));
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

        sql = "DELETE FROM R_TPE WHERE " + tem_ide + " =" + identificador ;
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
      String activo = temasVO.getActivo();
      m_Log.debug("el identificador en el DAO es : " + identificador);

      sql = "UPDATE R_TPE SET " + tem_cod + " = '" + codigo + "', " + tem_des + " = '" + descripcion + "', " + tem_act + " = '" + activo + "' WHERE " + tem_ide + " = " + identificador;
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
  public void modificarTemaActivo(String idModificar, String[] params) throws TechnicalException,BDException {

	    AdaptadorSQLBD abd = null;
	    Connection conexion = null;
	    Statement stmt = null;
	    String sql = "";

	    try{

	      m_Log.debug("A por el OAD");
	      abd = new AdaptadorSQLBD(params);
	      m_Log.debug("A por la conexion");
	      conexion = abd.getConnection();


	      sql = "UPDATE R_TPE SET " + tem_act + " = 'NO' WHERE " + tem_ide + " = " + idModificar;
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
      sql = "SELECT " + abd.funcionMatematica(AdaptadorSQL.FUNCIONMATEMATICA_MAX, new String[]{tem_ide}) + " FROM R_TPE";
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        m_Log.debug("dentro del while de maximo identificador");
        ideMax = rs.getInt(1);
        m_Log.debug("el identificador es : " + ideMax);
      }

      m_Log.debug("el identificador más grande es : " + ideMax);

      String codigo = temasVO.getCodigo();
      String descripcion = temasVO.getTxtNomeDescripcion();
      String activo = temasVO.getActivo();

      sql = "INSERT INTO R_TPE VALUES( " + (ideMax + 1) + ", '" + codigo + "', '" + descripcion + "', '" + activo + "')";
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