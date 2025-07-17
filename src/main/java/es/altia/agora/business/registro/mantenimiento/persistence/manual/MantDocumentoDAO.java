package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;
import es.altia.agora.technical.Fecha;
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
import java.sql.SQLException;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MantDocumentoDAO {

  //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
  //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
  //Para informacion de logs.
    protected static Log m_Log =
          LogFactory.getLog(MantDocumentoDAO.class.getName());

    private static MantDocumentoDAO instance = null;

    protected static String tem_ide;
    protected static String tem_cod;
    protected static String tem_des;
    protected static String tem_act;

    protected static String res_tdo;
    protected static String res_fec;

  protected MantDocumentoDAO() {
    super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

    tem_ide = m_ConfigTechnical.getString("SQL.R_TDO.identificador");
    tem_cod = m_ConfigTechnical.getString("SQL.R_TDO.codigo");
    tem_des = m_ConfigTechnical.getString("SQL.R_TDO.descripcion");
    tem_act=m_ConfigTechnical.getString("SQL.R_TDO.act");

    res_tdo =  m_ConfigTechnical.getString("SQL.R_RES.tipoDoc");
    res_fec =  m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");

  }

  public static MantDocumentoDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(MantDocumentoDAO.class){
                if (instance == null)
                    instance = new MantDocumentoDAO();
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
            String SQL_BUSCA_TEMAS = "SELECT " + tem_ide + ", " + tem_cod + ", " + tem_des +
                    ", " + tem_act +",TDO_EST, TDO_FEC FROM R_TDO ORDER BY " + tem_des;
            if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS);

            PreparedStatement ps = conexion.prepareStatement(SQL_BUSCA_TEMAS);
            ResultSet rs = ps.executeQuery();
            String estado = "";
            String fecha_baja="";
            SimpleDateFormat formatYear = new SimpleDateFormat("dd/MM/yyyy");

            while(rs.next()){
                
                if( "0".equals(rs.getString("TDO_EST"))){
                    estado="BAJA";
                }else{
                    estado="";
                }
                if(!"".equals(rs.getString("TDO_FEC")) && rs.getString("TDO_FEC") != null && !"null".equals(rs.getString("TDO_FEC"))){
                    fecha_baja = formatYear.format(rs.getDate("TDO_FEC"));
                }else{
                    fecha_baja ="";
                }
                MantTemasValueObject mantTemasVO = new MantTemasValueObject(rs.getInt(tem_ide), rs.getString(tem_cod), rs.getString(tem_des),
                "", rs.getString(tem_act), estado, fecha_baja);
                
                m_Log.debug("INFORMACION temas en el DAO *************: " + mantTemasVO.toString());
                temas.addElement(mantTemasVO);
            }
          }catch (Exception e){
              try{
                   abd.devolverConexion(conexion);
                   if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
              }catch (Exception ex){
                  ex.printStackTrace();
                  if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
              }
          }finally{
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
           String SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_tdo + " FROM R_RES WHERE "+ res_tdo + " IS NOT NULL";
           if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
           rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
           while(rs.next()){
             temasUtilizados.addElement(rs.getString(res_tdo));
           }

           m_Log.debug("el tamaño del vector temas utilizados es : " + temasUtilizados.size());

           rs.close();
           st.close();
         }catch (Exception e){
             try{
                  oad.devolverConexion(con);
                  if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
             }catch (Exception ex){
                 ex.printStackTrace();
                 if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
             }
         }finally{
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
           SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_tdo + " FROM R_RES WHERE (" + res_fec + " <= " +
                   oad.convertir("'" + fechaCerradoE + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                   " AND " + res_tdo + " IS NOT NULL)";
           if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
           rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
           while(rs.next()){
             temasUtilizadosCerrados.addElement(rs.getString(res_tdo));
           }
           rs.close();
         }

         if(!fechaCerradoS.equals("")) {
           SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_tdo + " FROM R_RES WHERE (" + res_fec + " <= " +
                   oad.convertir("'" + fechaCerradoS + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                   " AND " + res_tdo + " IS NOT NULL)";
           if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
           rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
           while(rs.next()){
             temasUtilizadosCerrados.addElement(rs.getString(res_tdo));
           }
           rs.close();
         }
         st.close();
       }catch (Exception e){
           try{
                oad.devolverConexion(con);
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
           }catch (Exception ex){
               ex.printStackTrace();
               if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
           }
       }finally{
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
    
    Calendar fechaYear = Calendar.getInstance();
    SimpleDateFormat formatYear = new SimpleDateFormat("dd/MM/yyyy");
    String fechaBaja =formatYear.format(fechaYear.getTime());

    sql = "UPDATE R_TDO SET TDO_EST = 0, TDO_FEC = '" + fechaBaja + "' WHERE " + tem_ide + " = " + identificador;
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
       } catch(Exception ex) {
          ex.printStackTrace();
           if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
       }
    }

    Vector lista = new Vector();
    lista = loadTemas(params);
    return lista;

  }
  
  public Vector recuperarTema(MantTemasValueObject temasVO, String[] params)throws TechnicalException,BDException{
      
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
         m_Log.debug("la descripción en el DAO es : "+identificador);
         
         sql = "UPDATE R_TDO SET TDO_EST = 1, TDO_FEC = '' WHERE "+tem_ide + "=" + identificador;
         
         if(m_Log.isDebugEnabled()){
             m_Log.debug(sql);
         }
         
         PreparedStatement ps = conexion.prepareStatement(sql);
         int res = ps.executeUpdate();
         m_Log.debug("las filas afectadas para reucperar son : "+res);
      }catch(Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()){
              m_Log.error(e.getMessage());
          }
      }finally{
          try{
              abd.devolverConexion(conexion);
          }catch(Exception ex){
              ex.printStackTrace();
              if(m_Log.isErrorEnabled()){
                  m_Log.error("Exception: "+ex.getMessage());
              }
          }
      }
      Vector lista = new Vector();
      lista = loadTemas(params);
      return lista;
  }
  
   public MantTemasValueObject getTemaByCod(String codigo, Connection con) throws SQLException {
      
      PreparedStatement ps = null;
      ResultSet rs = null;
      String sql = "";
      MantTemasValueObject temaVO = new MantTemasValueObject();
      
      try{
         temaVO.setCodigo(codigo);
         m_Log.debug("la descripción en el DAO es : "+codigo);
         
         sql = "SELECT TDO_FEC, TDO_IDE, TDO_EST, TDO_DES, TDO_ACT FROM R_TDO WHERE "+tem_cod + "=?";
         
         if(m_Log.isDebugEnabled()){
             m_Log.debug(sql);
         }
         
         ps = con.prepareStatement(sql);
         ps.setString(1, codigo);
         rs = ps.executeQuery();
         if(rs.next()){
             Date baja = rs.getDate("TDO_FEC");
             temaVO.setFecha(Fecha.obtenerString(baja));
             temaVO.setIde(rs.getInt("TDO_IDE"));
             temaVO.setEstado(rs.getString("TDO_EST"));
             temaVO.setTxtNomeDescripcion(rs.getString("TDO_DES"));
             temaVO.setActivo(rs.getString("TDO_ACT"));
         }
      }catch(SQLException e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()){
              m_Log.error(e.getMessage());
          }
          throw e;
      }finally{
          try{
              if(rs!=null) rs.close();
              if(ps!=null) ps.close();
          }catch(SQLException ex){
              ex.printStackTrace();
              if(m_Log.isErrorEnabled()){
                  m_Log.error("Exception: "+ex.getMessage());
              }
          }
      }
      return temaVO;
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

  sql = "UPDATE R_TDO SET " + tem_cod + " = '" + codigo + "', " + tem_des + " = '" + descripcion + "', " + tem_act+ " = '" + activo + "' WHERE " + tem_ide + " = " + identificador;
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
     } catch(Exception ex) {
        ex.printStackTrace();
         if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
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

	   /* int identificador = temasVO.getIde();
	    String codigo = temasVO.getCodigo();
	    String descripcion = temasVO.getTxtNomeDescripcion();
	    String activo = temasVO.getActivo();
	    m_Log.debug("el identificador en el DAO es : " + identificador);*/

	    sql = "UPDATE R_TDO SET " + tem_act+ " = 'NO' WHERE " + tem_ide + " = " + idModificar;
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
	       } catch(Exception ex) {
	          ex.printStackTrace();
	           if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
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
  sql = "SELECT " + abd.funcionMatematica(AdaptadorSQL.FUNCIONMATEMATICA_MAX, new String[]{tem_ide}) + " FROM R_TDO";
  rs = stmt.executeQuery(sql);
  while(rs.next()){
    ideMax = rs.getInt(1);
  }

  m_Log.debug("el identificador más grande es : " + ideMax);

  String codigo = temasVO.getCodigo();
  String descripcion = temasVO.getTxtNomeDescripcion();
  String activo = temasVO.getActivo();

  

  sql = "INSERT INTO R_TDO(TDO_IDE,TDO_COD,TDO_DES,TDO_ACT) VALUES( " + (ideMax + 1) + ", '" + codigo + "', '" + descripcion + "', '" + activo + "')";
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
     } catch(Exception ex) {
        ex.printStackTrace();
         if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
     }
  }

  Vector lista = new Vector();
  lista = loadTemas(params);
  return lista;

  }



}