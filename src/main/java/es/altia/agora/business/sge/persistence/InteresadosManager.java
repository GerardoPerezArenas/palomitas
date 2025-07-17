// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.RolVO;
import es.altia.agora.business.sge.persistence.manual.*;
import es.altia.agora.business.util.*;
import es.altia.common.service.config.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;

public class InteresadosManager  {
  private static InteresadosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(InteresadosManager.class.getName());         

  protected InteresadosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static InteresadosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
      synchronized(InteresadosManager.class) {
        if (instance == null) {
          instance = new InteresadosManager();
        }
      }
    }
    return instance;
  }

  
  

    public Vector<InteresadoExpedienteVO> getListaInteresados(GeneralValueObject gVO, String[] params) {
    //public ArrayList<InteresadoExpedienteVO> getListaInteresados(GeneralValueObject gVO, String[] params) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getListaInteresadosManager");
        InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
        Vector resultado = new Vector();
        try {
            m_Log.debug("Usando persistencia manual");
            resultado = interesadosDAO.getListaInteresados(gVO, params);
            return resultado;
        } catch (Exception e) {
            m_Log.error("JDBC Technical problem " + e.getMessage());
        }
        // queremos estar informados de cuando este metodo finaliza
        m_Log.debug("getListaInteresadosManager");
        return resultado;
    }
  
  public Vector getListaInteresadosRegistro(GeneralValueObject gVO, String[] params){
	    // queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("getListaInteresadosManager");
	    InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
	    Vector resultado = new Vector();
	    try{
	      m_Log.debug("Usando persistencia manual");
	      resultado = interesadosDAO.getListaInteresadosRegistro(gVO,params);
	      return resultado;
	    }catch(Exception e){
	      m_Log.error("JDBC Technical problem " + e.getMessage());
	    }
	    // queremos estar informados de cuando este metodo finaliza
	    m_Log.debug("getListaInteresadosManager");
	    return resultado;
	  }

  public Vector getListaRoles(GeneralValueObject gVO, String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaRolesManager");
    InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = interesadosDAO.getListaRoles(gVO,params);
      return resultado;
    }catch(Exception e){
      m_Log.error("JDBC Technical problem " + e.getMessage());
    }
    // queremos estar informados de cuando este metodo finaliza
    m_Log.debug("getListaRolesManager");
    return resultado;
  }
  public Vector getListaRolesRegistro( String[] params){
	    // queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("getListaRolesManager");
	    InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
	    Vector resultado = new Vector();
	    try{
	      m_Log.debug("Usando persistencia manual");
	      resultado = interesadosDAO.getListaRolesRegistro(params);
	      return resultado;
	    }catch(Exception e){
	      m_Log.error("JDBC Technical problem " + e.getMessage());
	    }
	    // queremos estar informados de cuando este metodo finaliza
	    m_Log.debug("getListaRolesManager");
	    return resultado;
	  }
  

  public Vector eliminarInteresado(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("eliminarInteresado");
    InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = interesadosDAO.eliminarInteresado(gVO,params);
    }catch (Exception e){
      m_Log.error("Excepción capturada: " + e.toString());
    }
    m_Log.debug("eliminarInteresado");
    return resultado;
  }

  public Vector modificarInteresado(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modificarInteresado");
    InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = interesadosDAO.modificarInteresado(gVO,params);
    }catch(Exception e){
      m_Log.error("Excepcion capturada: " + e.toString());
    }
    m_Log.debug("modificarInteresado");
    return resultado;
  }

  public Vector altaInteresado(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("altaInteresado");
    InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = interesadosDAO.altaInteresado(gVO,params);
    }catch (Exception e) {
      m_Log.error("Excepción capturada: " + e.toString());
    }
    m_Log.debug("altaInteresado");
    return resultado;
  }
  
  public int grabarInteresadosRegistro(GeneralValueObject gVO, String[] params) {
	    //queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("grabarInteresadosRegistro");
	    InteresadosDAO interesadosDAO = InteresadosDAO.getInstance();
	    int resultado = 0;
	    try{
	      resultado = interesadosDAO.grabarInteresadosRegistro(gVO,params);
	    }catch (Exception e) {
	      m_Log.error("Excepción capturada: " + e.toString());
	    }
	    m_Log.debug("grabarInteresados");
	    return resultado;
	  }




    /*********************** NUEVO ************************/
    public ArrayList<RolVO> getListaRoles(int codOrganizacion,String codProcedimiento,String[] params) {
        ArrayList<RolVO> salida = null;
        AdaptadorSQLBD adapt = null;
        Connection con  = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            salida = InteresadosDAO.getInstance().getListaRoles(codOrganizacion, codProcedimiento, con);
            
        }catch(Exception e){
            e.printStackTrace();
        
        }finally { 
            try{
                if(con!=null) con.close();
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return salida;
    }
    
    
    
    /***************************** NUEVO **********************************/
    public ArrayList<InteresadoExpedienteVO> getListaInteresadosExpediente(GeneralValueObject gVO, String[] params) {
    
        AdaptadorSQLBD adapt = null;
        Connection con = null;        
        
        ArrayList<InteresadoExpedienteVO> resultado = new ArrayList<InteresadoExpedienteVO>();
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
                        
            resultado = InteresadosDAO.getInstance().getListaInteresadosExpediente(gVO,adapt,con);
            return resultado;
            
        } catch (Exception e) {
            m_Log.error("JDBC Technical problem " + e.getMessage());
            
        } finally { 
            try { 
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return resultado;
    }
 
    
    public GeneralValueObject getDocumentosNombresInteresadosImpresionExpediente(String numExpediente,String[] params) {
        GeneralValueObject gvo = new GeneralValueObject();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            gvo = InteresadosDAO.getInstance().getDocumentosNombresInteresadosImpresionExpediente(numExpediente, con);
            
        }catch(Exception e){
            e.printStackTrace();
            m_Log.error("Error al recuperar los datos de los interesados del expediente para la impresión de la ficha: " + e.getMessage());
            
        }finally {
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return gvo;
    }
    
	
	public QueryResult getInteresadosByExpediente(String numExpediente, String[] params){		
		AdaptadorSQLBD adapt = null;
		Connection con = null;
		QueryResult queryResult = null;
		SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.select("TER.TER_TID", "TER.TER_DOC")
				  .from("E_EXT EXT")
				  .innerJoin("T_TER TER", "EXT.EXT_TER", SqlBuilder.IGUAL, "TER.TER_COD")
				  .whereEquals("EXT.EXT_NUM", String.format("'%s'", numExpediente));
		try {
			adapt = new AdaptadorSQLBD(params);
			con = adapt.getConnection();
			adapt.inicioTransaccion(con);
			queryResult = InteresadosDAO.getInstance().getInteresadosByExpediente(con, sqlBuilder);
		} catch (BDException ex) {
			m_Log.error("Error al consultar los interesados del expediente [" + numExpediente + "]: " + ex);
		} finally {
			try {
				adapt.devolverConexion(con);
			} catch (BDException ex) {
				m_Log.error("Error al cerrar la conexion: " + ex);
			}
		}
		return queryResult;
	}
	
}