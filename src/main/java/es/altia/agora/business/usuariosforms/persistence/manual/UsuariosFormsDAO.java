package es.altia.agora.business.usuariosforms.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UsuariosGruposDAO;
import es.altia.agora.business.usuariosforms.UsuariosFormsValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;


public class UsuariosFormsDAO {

	private static UsuariosFormsDAO instance = null;	
	protected static Log m_Log = LogFactory.getLog(UsuariosGruposDAO.class.getName());
    static  Config m_ConfigTechnical;
    static String buscarCategoria;

    static{
        m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        buscarCategoria = m_ConfigTechnical.getString("datosUsuarios");
    }

	
	public UsuariosFormsValueObject getDatosUsuario(UsuariosFormsValueObject ufVO, String logUsu, String codOrg, String[] params){
		
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	String sql = "";
    	Connection con = null;
    	
    	try{
    		con = abd.getConnection();
    		
            if (buscarCategoria.equalsIgnoreCase("si")){
    		

                sql = " SELECT USU_LOGIN, USU_NOMBRE, USU_APE1, USU_APE2, USU_NIF, USU_EMAIL, USU_PSW, USU_BLQ , " +
                        "ENT_COD, ID_CATEGORIA, DESCRICION  FROM USUARIO_TEMP JOIN CATEGORIAS ON (ID_CATEGORIA=CATPROF_COD) WHERE USU_LOGIN='" + logUsu + "'  ";

                
            }
            else{

                String select = " SELECT USU_LOGIN, USU_NOMBRE, USU_APE1, USU_APE2, USU_NIF, USU_EMAIL, USU_PSW, USU_BLQ , ENT_COD";
                String where= " FROM USUARIO_TEMP WHERE USU_LOGIN='" + logUsu + "' ";

                sql = select + where;
            }
    		
    		m_Log.debug("getDatosUsuario=" + sql);
    		
    		ps = con.prepareStatement(sql);
    		rs = ps.executeQuery();
    		
    		while(rs.next()){
    			String login = rs.getString("USU_LOGIN");
    			ufVO.setLogin(login);
    			String nombre = rs.getString("USU_NOMBRE");
    			ufVO.setNombre(nombre);
    			String ape1 = rs.getString("USU_APE1");
    			ufVO.setApellido1(ape1);
    			String ape2 = rs.getString("USU_APE2");
    			ufVO.setApellido2(ape2);
    			String nif = rs.getString("USU_NIF");
    			ufVO.setNif(nif);
    			String email = rs.getString("USU_EMAIL");
    			ufVO.setEmail(email);
    			String password = rs.getString("USU_PSW");
    			ufVO.setPassword(password);
    			ufVO.setPassword2(password);
    			String bloqueado = rs.getString("USU_BLQ");
    			ufVO.setBloqueado(bloqueado);
    			//String codDepartamento = rs.getString("UOR_COD_VIS");
    			//ufVO.setCodDepartamento(codDepartamento);
    			//String nomDepartamento = rs.getString("UOR_NOM");
    			//ufVO.setDescDepartamento(nomDepartamento);
    			//String codPerfil = rs.getString ("CAR_COD_VIS");
    			//ufVO.setCodPerfil(codPerfil);
    			//String nomPerfil = rs.getString("CAR_NOM");
    			//ufVO.setDescPerfil(nomPerfil);
    			String codEntidad = rs.getString("ENT_COD");
    			ufVO.setCodEntidad(codEntidad);

                if (buscarCategoria.equalsIgnoreCase("si")){
                    String codCatProf = rs.getString ("ID_CATEGORIA");
                    ufVO.setCodCatProfesional(codCatProf);
                    String descCatProfesional = rs.getString ("DESCRICION");
                    ufVO.setDescCatProfesional(descCatProfesional);
                }
                else {
                    ufVO.setCodCatProfesional("");
                    ufVO.setDescCatProfesional("");
                }
            
    		}
    	
    	
    	} catch (BDException e) {
    		m_Log.debug("Error en la conexión a la base de datos.");
    		e.printStackTrace();
    	} catch (SQLException e) {
    		m_Log.debug("Error en la consulta a la base de datos.");
    		e.printStackTrace();
    	} finally {
    		try{
    			if (rs!=null) rs.close();
    			if (ps!=null) ps.close();
    			abd.devolverConexion(con);
    		}
    		catch (Exception ex) {
    			ex.printStackTrace();             
    			if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
    		}
    	}
		return ufVO;
	}
	
	
    public Vector getUsuariosForms(String codOrg,String[] params){
    	
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	String sql = "";
    	Connection con = null;
    	Vector<GeneralValueObject> usuarios = new Vector<GeneralValueObject>();
    	
    	try {
    		
    		con = abd.getConnection();

    		sql = "SELECT USU_LOGIN, USU_NOMBRE, USU_APE1, USU_APE2 FROM USUARIO_TEMP " +
    				"ORDER BY USU_NOMBRE";		
    		
    		m_Log.debug("getUsuariosForms ----------> " + sql);
    		ps = con.prepareStatement(sql);
    		rs = ps.executeQuery();
    		
    		while (rs.next()){
    			GeneralValueObject gVO = new GeneralValueObject();
    			String login = rs.getString("USU_LOGIN");
    			gVO.setAtributo("login", login);
    			String nombreCompleto = rs.getString("USU_NOMBRE") + " " + rs.getString("USU_APE1")
    									+ " " + rs.getString("USU_APE2");
    			gVO.setAtributo("nombreUsuario", nombreCompleto);
    			gVO.setAtributo("codUsuario", login);
    			usuarios.addElement(gVO);
    		}
		
		} catch (BDException e) {
			m_Log.debug("Error en la conexión a la base de datos.");
			e.printStackTrace();
		
		} catch (SQLException e) {
			m_Log.debug("Error en la consulta a la base de datos.");
			e.printStackTrace();
		} finally {
			try{
				if (rs!=null) rs.close();
				if (ps!=null) ps.close();
				abd.devolverConexion(con);
			}
			catch (Exception ex) {
				ex.printStackTrace();             
				if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
			}
		}
    	
    	
    	return usuarios;
    }//getUsuariosForms
	
	
    public UsuariosFormsValueObject getUnidadOrganicaUsuario(UsuariosFormsValueObject ufVO,String[] params){
    	//Pendiente de eliminar, creo que no se usa.
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	String sql = "";
    	Connection con = null;
    	
    	try{
		String[] join = new String [5];
		String from = " UOR_NOM, UOR_COD_VIS  ";
		String where= "USU_LOGIN='" + ufVO.getLogin() + "' ";
		join[0]= "USUARIO_TEMP";
		join[1]= "INNER";
		join[2]= "A_UOR";
		join[3]= "UOR_COD_VIS=DEP_COD";
		join[4]= "false";
		sql = abd.join(from, where, join);
		
		m_Log.debug("getUnidadOrganicaUsuario -------->" + sql);
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		
		while (rs.next()){
			UsuariosFormsValueObject uVO = new UsuariosFormsValueObject();
			String nombreUor = rs.getString("UOR_NOM");
			ufVO.setDescDepartamento(nombreUor);
			String uorCod = rs.getString ("UOR_COD_VIS");
			ufVO.setCodDepartamento(uorCod);
		}
		
		
		} catch (BDException e) {
			m_Log.debug("Error en la conexión a la base de datos.");
			e.printStackTrace();
		
		} catch (SQLException e) {
			m_Log.debug("Error en la consulta a la base de datos.");
			e.printStackTrace();
		} finally {
			try{
				if (rs!=null) rs.close();
				if (ps!=null) ps.close();
				abd.devolverConexion(con);
			}
			catch (Exception ex) {
				ex.printStackTrace();             
				if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
			}
		}
    	return ufVO;
    	
    }
    
    public Vector getListaPerfiles(String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
          abd = new AdaptadorSQLBD(params);
          conexion = abd.getConnection();

          sql = "SELECT CAR_COD_VIS, CAR_NOM , CAR_COD FROM A_CAR WHERE CAR_NOM<>'TODOS'";
          String[] orden = {"CAR_COD_VIS","1"};
          sql += abd.orderUnion(orden);
          
          if(m_Log.isDebugEnabled()) m_Log.debug("getListaPerfiles -------> " + sql);
          stmt = conexion.prepareStatement(sql);
          rs = stmt.executeQuery();
          
          int i=0;
          while(rs.next()){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPerfil",rs.getString("CAR_COD_VIS"));
            gVO.setAtributo("descPerfil",rs.getString("CAR_NOM"));
            gVO.setAtributo("cod_interno_Perfil",rs.getString("CAR_COD"));

            resultado.add(gVO);
          }
          rs.close();
          stmt.close(); 
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
        return resultado;
    }
    
    public Vector getListaCatProfesionales(String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
          abd = new AdaptadorSQLBD(params);
          conexion = abd.getConnection();

          sql = "SELECT ID_CATEGORIA, DESCRICION FROM CATEGORIAS";
          String[] orden = {"DESCRICION","1"};
          sql += abd.orderUnion(orden);
          
          if(m_Log.isDebugEnabled()) m_Log.debug("getListaCatProfesionales -------> " + sql);
          stmt = conexion.prepareStatement(sql);
          rs = stmt.executeQuery();
          
          while(rs.next()){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("cod_catProf",rs.getString("ID_CATEGORIA"));
            gVO.setAtributo("desc_catProf",rs.getString("DESCRICION"));
            resultado.add(gVO);
          }
          rs.close();
          stmt.close(); 
        }catch (SQLException e){
            try{
                 abd.devolverConexion(conexion);
                 if(m_Log.isErrorEnabled()) m_Log.error("getListaCatProfesionales 1 "+e.getMessage());
            }catch (Exception ex){
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("getListaCatProfesionales 2 "+ex.getMessage());
           }
        }catch (BDException ex){
            if(m_Log.isErrorEnabled()) m_Log.error("getListaCatProfesionales 3 "+ex.getMessage());
        }
        return resultado;
    }
    
    
    public Vector getListaUors(String[] params){
        Vector resultado = new Vector();
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(params[6]);
        if (unidadesOrg!=null && !unidadesOrg.isEmpty()) {
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if ("A".equals(unidad.getUor_estado())){
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("cod_departamento",unidad.getUor_cod_vis());
                    gVO.setAtributo("desc_departamento",unidad.getUor_nom());
                    gVO.setAtributo("cod_interno_dep", unidad.getUor_cod());
                    resultado.add(gVO);
                }
            }
        } 
        return resultado;
    }
    
    
    public int eliminarUsuario(String codUsuario,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";
        int resultado = 0;
        try{
            
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            //sql = "UPDATE USUARIO_TEMP SET BORRAR='1' WHERE USU_LOGIN='" + codUsuario + "'" ;
            sql="DELETE FROM USUARIO_TEMP WHERE USU_LOGIN='" + codUsuario + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransaction(abd,conexion);
        }
        return resultado;
    }
    
    
    public int modificarUsuario (UsuariosFormsValueObject ufVO,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";
        int resultado = 0;
        try{
            
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql = "UPDATE USUARIO_TEMP SET USU_NOMBRE='" + ufVO.getNombre() + 
            	"', USU_APE1='" + ufVO.getApellido1() + "', USU_APE2='" + ufVO.getApellido2() + 
            	"', USU_NIF='" + ufVO.getNif() + "', USU_EMAIL='" + ufVO.getEmail() + 
            	"', ENT_COD='"+ ufVO.getCodEntidad() + "', CATPROF_COD= '" + ufVO.getCodCatProfesional() +
            	"', USU_PSW='" + ufVO.getPassword() + "', USU_BLQ='" + ufVO.getBloqueado() +
            	"' WHERE USU_LOGIN='" +ufVO.getLogin() + "'";
           
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();

        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransaction(abd,conexion);
        }
        return resultado;
    }
    
    public int insertarUsuario (UsuariosFormsValueObject ufVO,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";
        int resultado = 0;
        try{
            
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            
            sql = "INSERT INTO USUARIO_TEMP (USU_LOGIN, USU_NOMBRE,USU_APE1, USU_APE2, USU_NIF, USU_EMAIL, " +
            		"USU_PSW, USU_BLQ, ENT_COD,CATPROF_COD) VALUES ('"+ ufVO.getLogin() +"','"+
            		ufVO.getNombre() + "','" + ufVO.getApellido1() + "','" + ufVO.getApellido2() + "','" + 
            		ufVO.getNif() + "','" + ufVO.getEmail() + "','" +ufVO.getPassword() + "',0,'" + ufVO.getCodEntidad() + "','" + 
            		ufVO.getCodCatProfesional() + "')";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();

         
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
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
    
    
    
    
    
	public static UsuariosFormsDAO getInstance() {
		// si no hay ninguna instancia de esta clase tenemos que crear una
		if (instance == null) {
			// Necesitamos sincronizacion para serializar (no multithread)
			// Las invocaciones de este metodo
			synchronized (UsuariosFormsDAO.class) {
				if (instance == null) {
					instance = new UsuariosFormsDAO();
				}
			}
		}
		return instance;
	}
	
}//