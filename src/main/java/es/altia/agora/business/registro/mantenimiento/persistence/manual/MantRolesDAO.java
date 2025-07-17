package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import es.altia.agora.business.registro.mantenimiento.MantRolesValueObject;
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
import java.sql.Statement;
import java.util.Vector;

public class MantRolesDAO {

  //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
  //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
  //Para informacion de logs.
    protected static Log m_Log =
          LogFactory.getLog(MantRolesDAO.class.getName());

    private static MantRolesDAO instance = null;

  protected static String grupoPorDefecto; // Id del grupo de roles por defecto, que es
                                           // el que manejara este DAO

  protected MantRolesDAO() {
    super();
    //Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    grupoPorDefecto = m_ConfigTechnical.getString("SQL.R_ROL.grupoPorDefecto");
  }

    public static MantRolesDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        synchronized (MantRolesDAO.class) {
            if (instance == null) {
                instance = new MantRolesDAO();
            }
        }
        return instance;
    }

  public Vector loadRoles(String[] params) throws TechnicalException, BDException {

    Vector roles = new Vector();
    //Usar el JDBCWrapper es mas sencillo que usar JDBC directamente
                AdaptadorSQLBD abd = null;
                Connection conexion = null;

    try{

      m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_ROLES = "SELECT ROL_COD, ROL_DES, ROL_PDE, ROL_ACT, ROL_PCW FROM R_ROL " + 
                               "WHERE GRUPO =" + grupoPorDefecto + " ORDER BY ROL_DES";
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_ROLES);

      PreparedStatement ps = conexion.prepareStatement(SQL_BUSCA_ROLES);
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        MantRolesValueObject mantRolesVO = new MantRolesValueObject();
        mantRolesVO.setIde(rs.getInt("ROL_COD"));
        mantRolesVO.setTxtNomeDescripcion(rs.getString("ROL_DES"));
        mantRolesVO.setPorDefecto(rs.getString("ROL_PDE"));
        mantRolesVO.setActivo(rs.getString("ROL_ACT"));
        mantRolesVO.setConsultaWeb(rs.getString("ROL_PCW"));
        roles.addElement(mantRolesVO);
      }
    } catch (Exception e) {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally {
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
  return roles;
 }
  public String getRolDefecto(String[] params) throws TechnicalException, BDException {

    String rol = "";
    //Usar el JDBCWrapper es mas sencillo que usar JDBC directamente
                AdaptadorSQLBD abd = null;
                Connection conexion = null;

    try{

      m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_ROLES = "SELECT ROL_COD FROM R_ROL " +
                               "WHERE GRUPO =" + grupoPorDefecto + " AND ROL_PDE = 'SI'";
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_ROLES);

      PreparedStatement ps = conexion.prepareStatement(SQL_BUSCA_ROLES);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) rol =(rs.getString(1));
    } catch (Exception e) {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally {
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    return rol;
 }
  public Vector eliminarRol(MantRolesValueObject rolesVO, String[] params) throws TechnicalException,BDException {

    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";

    try{

      m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      m_Log.debug("A por la conexion");
      conexion = abd.getConnection();

      int identificador = rolesVO.getIde();
      m_Log.debug("la descripcion en el DAO es : " + identificador);
      sql = "UPDATE R_ROL SET ROL_ACT = 'NO' WHERE ROL_COD = " + identificador + " AND GRUPO =" + grupoPorDefecto;     

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
      lista = loadRoles(params);
      return lista;

    }

  public Vector modificarRol(MantRolesValueObject rolesVO, String[] params) throws TechnicalException,BDException {

    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";

    try{

      m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      m_Log.debug("A por la conexion");
      conexion = abd.getConnection();

     
      String codigo = rolesVO.getCodigo();
      Integer cod=0;
      cod=new Integer(codigo).intValue();
      String descripcion = rolesVO.getTxtNomeDescripcion();
      String porDefecto = rolesVO.getPorDefecto();
      String activo = rolesVO.getActivo();     
      String consultaWeb = rolesVO.getConsultaWeb();
 

      sql = "UPDATE R_ROL SET ROL_COD = '" + codigo + "', ROL_DES = '" + descripcion + "', ROL_PDE = '" + porDefecto + 
            "',ROL_ACT = '" + activo + "',ROL_PCW = '" + consultaWeb + "' WHERE ROL_COD = " + cod + " AND GRUPO =" + grupoPorDefecto;
//      sql = "UPDATE R_ROL SET " + rol_cod + " = '" + codigo + "', " + rol_des + " = '" + descripcion + "', " + rol_pde + " = '" + porDefecto + 
//            "' WHERE " + rol_cod + " = " + cod + " AND " + grupo + "=" + grupoPorDefecto;      
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
      lista = loadRoles(params);
      return lista;

    }
  public void modificarRolDefecto(String idModificar, String[] params) throws TechnicalException,BDException {

	    AdaptadorSQLBD abd = null;
	    Connection conexion = null;
	    Statement stmt = null;
	    String sql = "";

	    try{

	      m_Log.debug("A por el OAD");
	      abd = new AdaptadorSQLBD(params);
	      m_Log.debug("A por la conexion");
	      conexion = abd.getConnection();

	     
	      sql = "UPDATE R_ROL SET ROL_PDE = 'NO' WHERE ROL_COD = " + idModificar + " AND GRUPO =" + grupoPorDefecto;
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

    public Vector altaRol(MantRolesValueObject rolesVO, String[] params) throws TechnicalException, BDException {


        m_Log.debug("altaRol  roles");
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        String sql = "";

        try {

            conexion = abd.getConnection();
            
            String codigo = rolesVO.getCodigo();
            Integer cod = 0;
            cod = new Integer(codigo).intValue();

            String descripcion = rolesVO.getTxtNomeDescripcion();
            String activo = rolesVO.getActivo();
            String porDefecto = rolesVO.getPorDefecto();
            String consultaWeb = rolesVO.getConsultaWeb();

//      sql = "INSERT INTO R_ROL (" + rol_cod + ", " + rol_des + ", " + rol_pde + ", ROL_ACT, "+ grupo + ") " +
//             "VALUES( " + cod + ", '" + descripcion + "', '" + porDefecto + "', '" + activo + "', " + grupoPorDefecto + ")";
            sql = "INSERT INTO R_ROL (ROL_COD, ROL_DES, ROL_PDE, ROL_ACT, GRUPO, ROL_PCW) " +
                    "VALUES( " + cod + ", '" + descripcion + "', '" + porDefecto + "', '" + activo + "', " + grupoPorDefecto + ",'" + consultaWeb + "')";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            PreparedStatement ps = conexion.prepareStatement(sql);
            int res = ps.executeUpdate();
            m_Log.debug("las filas afectadas en el insert son : " + res);


        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        } finally {
            try {
                abd.devolverConexion(conexion);
            } catch (Exception e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error(e.getMessage());
                }
            }
        }

        Vector lista = new Vector();
        lista = loadRoles(params);
        return lista;

    }

}