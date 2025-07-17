package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import java.util.Vector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.common.exception.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;

import java.sql.PreparedStatement;
import java.sql.SQLException;



public class MantRegistroExternoDAO {

  
    protected static Config m_ConfigTechnical;
    protected static Config m_ConfigError;  
    protected static Log m_Log =
            LogFactory.getLog(MantRegistroExternoDAO.class.getName());            

    private static MantRegistroExternoDAO instance = null;

  
  	protected MantRegistroExternoDAO() {
    	super();        
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");        
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

  	public static MantRegistroExternoDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(MantRegistroExternoDAO.class){
                if (instance == null)
                    instance = new MantRegistroExternoDAO();
                }
        }
        return instance;
  	}

  	public Vector loadOrganizacionesExternas(String[] params) throws TechnicalException {

		Vector organizaciones = new Vector();
		
		AdaptadorSQLBD abd = null;
		Connection conexion = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		
		try{
		
			  abd = new AdaptadorSQLBD(params);		
			  conexion = abd.getConnection();
			  // Creamos la select con los parametros adecuados.
			  sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + m_ConfigTechnical.getString("SQL.A_ORGEX.nombreTabla") + " " + m_ConfigTechnical.getString("SQL.A_ORGEX.nombreTabla");
			  String[] orden = {m_ConfigTechnical.getString("SQL.A_ORGEX.codigo"),"1"};
			  sql += abd.orderUnion(orden);
			  
			  if(m_Log.isDebugEnabled()) m_Log.debug(sql);

			  stmt = conexion.createStatement();
			  rs = stmt.executeQuery(sql);
			  int ordenElem = 0;
			  m_Log.debug("Lista organizaciones externas: ");
			  while(rs.next()){
				ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(m_ConfigTechnical.getString("SQL.A_ORGEX.codigo")),rs.getString(m_ConfigTechnical.getString("SQL.A_ORGEX.descripcion")), ordenElem++);
				m_Log.debug(elemListVO.getOrden() + " -> " + elemListVO.getCodigo() + " " + elemListVO.getDescripcion());
				organizaciones.add(elemListVO);
			  }
			  rs.close();
			  stmt.close();

		}catch (Exception e){
			  organizaciones = new Vector();
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
        return organizaciones;
    }
       
 	public String altaOrganizacionExterna(GeneralValueObject orgExtVO, String[] params) throws TechnicalException {

	  AdaptadorSQLBD abd = null;
	  Connection conexion = null;
	  ResultSet rs = null;
	  String sql = "";
	
	  String resultado = "";
	  
	  try{
	  		
        String[] parametros = (String []) params.clone();
        parametros[6] = m_ConfigTechnical.getString("CON.jndi");
		abd = new AdaptadorSQLBD(parametros);
		conexion = abd.getConnection();

		/*
		int ideMax = 0;
		sql = "SELECT MAX(" + m_ConfigTechnical.getString("SQL.A_ORGEX.codigo") + ") AS COD FROM " + m_ConfigTechnical.getString("SQL.A_ORGEX.nombreTabla");
		rs = stmt.executeQuery(sql);
		if (rs.next()){
		  ideMax = rs.getInt("COD");
		} else ideMax = 0;
		rs.close();
		
		m_Log.debug("El último codigo de organizaciones externas : " + ideMax);
		ideMax += 1;
		
		*/		
		String codigo = (String) orgExtVO.getAtributo("idOrganizacionExterna");
		int ideMax = Integer.parseInt(codigo);
				
		String descripcion = (String) orgExtVO.getAtributo("desOrganizacionExterna");
	
		sql = "INSERT INTO " + m_ConfigTechnical.getString("SQL.A_ORGEX.nombreTabla") +
              " VALUES( " + ideMax + ", '" + descripcion + "')";
		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	
		PreparedStatement ps = conexion.prepareStatement(sql);
		int res = ps.executeUpdate();
		m_Log.debug("Filas insertadas: " + res);
		if (res >0 ) resultado ="insertada";
		else resultado="no insertada";
	
		ps.close();

	  } catch (Exception ex) {
			resultado="no insertada";
	  		ex.printStackTrace();
	  		m_Log.debug(ex.getMessage());  		
	  } finally {
          try{
              abd.devolverConexion(conexion);
          }catch (Exception e){
              e.printStackTrace();
              if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
          }
	  }
      return resultado;
	}
	
	public String eliminarOrganizacionExterna(GeneralValueObject orgExtVO, String[] params) throws TechnicalException {

	  AdaptadorSQLBD abd = null;
	  Connection conexion = null;
      AdaptadorSQLBD oad = null;
      Connection con = null;
	  ResultSet rs = null;
	  Statement stmt = null;
	  String sql = "";
	  String error = "";
	  boolean dependencias = false;
	  
	  try{

		abd = new AdaptadorSQLBD(params);	
		conexion = abd.getConnection();
		stmt = conexion.createStatement();

		String identificador = (String) orgExtVO.getAtributo("idOrganizacionExterna");
		m_Log.debug("El código de la organización externa a eliminar es: " + identificador);

		// Comprobar dependencias.
		int cuenta1 = 0;
		sql = "SELECT count(*) AS CUENTA FROM " + GlobalNames.ESQUEMA_GENERICO + m_ConfigTechnical.getString("SQL.A_UOREX.nombreTabla")
			+ " WHERE " + m_ConfigTechnical.getString("SQL.A_UOREX.organizacion") + "=" + identificador;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		rs = stmt.executeQuery(sql);
		if (rs.next()){
			cuenta1 = rs.getInt("CUENTA");
		} 
		rs.close();
		m_Log.debug(String.valueOf(cuenta1));
		
		int cuenta2 = 0;
		sql = "SELECT count(*) AS CUENTA FROM R_RES "  
			+ " WHERE (" + m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino") + "= 1" // a otro registro
			+ " AND " + m_ConfigTechnical.getString("SQL.R_RES.orgDestAnot") + "=" + identificador  		 
			+ " ) OR (" + m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino") + "= 2" // procedente de otro registro
			+ " AND " + m_ConfigTechnical.getString("SQL.R_RES.orgDestAnot") + "=" + identificador + ")";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		rs = stmt.executeQuery(sql);
		if (rs.next()){
			cuenta2 = rs.getInt("CUENTA");
		}
		rs.close();
		m_Log.debug(String.valueOf(cuenta2));
		
		if (cuenta1>0 || cuenta2>0){		
			dependencias = true;
			error="tiene dependencias";
		}
		stmt.close();

        String[] parametros = (String []) params.clone();
        parametros[6] = m_ConfigTechnical.getString("CON.jndi");
        oad = new AdaptadorSQLBD(parametros);
        //m_Log.debug("A por la conexion");
        con = oad.getConnection();

		if (!dependencias) {
			// Eliminar
			sql = "DELETE FROM " + m_ConfigTechnical.getString("SQL.A_ORGEX.nombreTabla") +
				  " WHERE " + m_ConfigTechnical.getString("SQL.A_ORGEX.codigo") + " =" + identificador ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			PreparedStatement ps = con.prepareStatement(sql);
			int res = ps.executeUpdate();
			m_Log.debug("Filas borradas: " + res);
			if (res <= 0) error = "no eliminada";
			else error="eliminada";
			ps.close();
		}
				

	  } catch (Exception e) {
		error = "no eliminada";
		e.printStackTrace();
		m_Log.debug(e.getMessage());  		
	  }finally {
          try{
              abd.devolverConexion(conexion);
              oad.devolverConexion(con);
          }catch (Exception e){
              e.printStackTrace();
              if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
          }
	  }
     return error;
	 }


	public String modificarOrganizacionExterna(GeneralValueObject orgExtVO, String[] params) throws TechnicalException {

		  AdaptadorSQLBD abd = null;
		  Connection conexion = null;
		  ResultSet rs = null;		  
		  String sql = "";
		  String error = "";
		  boolean dependencias = false;
	  
		  try{

            String[] parametros = (String []) params.clone();
            parametros[6] = m_ConfigTechnical.getString("CON.jndi");
			abd = new AdaptadorSQLBD(parametros);
			conexion = abd.getConnection();

			String identificador = (String) orgExtVO.getAtributo("idOrganizacionExterna");
			String descripcion = (String) orgExtVO.getAtributo("desOrganizacionExterna");
			m_Log.debug("El código de la organización externa a modificar es: " + identificador);

			// Eliminar
			sql = "UPDATE " + m_ConfigTechnical.getString("SQL.A_ORGEX.nombreTabla") +
                  " SET " + m_ConfigTechnical.getString("SQL.A_ORGEX.descripcion") + " = '" + descripcion +"' " +
			      " WHERE " + m_ConfigTechnical.getString("SQL.A_ORGEX.codigo") + " =" + identificador ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			PreparedStatement ps = conexion.prepareStatement(sql);
			int res = ps.executeUpdate();
			m_Log.debug("Filas modificadas: " + res);
			if (res <= 0) error = "no modificada";
			else error="modificada";
			
			ps.close();		

		  } catch (Exception ex) {
			  error = "no modificada";
			  ex.printStackTrace();
			  m_Log.debug(ex.getMessage());  		
		  } finally {
              try{
                  abd.devolverConexion(conexion);
              }catch (Exception e){
                  e.printStackTrace();
                  if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
              }
		  }
     return error;
	}


	public Vector loadUnidadesRegistroExternas(GeneralValueObject orgExtVO, String[] params) throws TechnicalException {

		Vector unidadesRegistro = new Vector();
		
		AdaptadorSQLBD abd = null;
		Connection conexion = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		
		try{
		
			  abd = new AdaptadorSQLBD(params);		
			  conexion = abd.getConnection();

			  String identificador = (String) orgExtVO.getAtributo("idOrganizacionExterna");
			
			  m_Log.debug("El código de la organización externa consultada es: " + identificador);
			  // Creamos la select con los parametros adecuados.
			  
			  sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + m_ConfigTechnical.getString("SQL.A_UOREX.nombreTabla");
			  if (identificador != null)
			  	if (!"".equals(identificador))
			  		sql +=" WHERE " + m_ConfigTechnical.getString("SQL.A_UOREX.organizacion") + "=" + identificador;
			  String[] orden = {m_ConfigTechnical.getString("SQL.A_UOREX.codigo"),"1"};
			  sql += abd.orderUnion(orden);
			  
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);

			  stmt = conexion.createStatement();
			  rs = stmt.executeQuery(sql);
			  int ordenElem = 0;
			  m_Log.debug("Lista organizaciones externas: ");
			  while(rs.next()){
				GeneralValueObject elemListVO = new GeneralValueObject();
				elemListVO.setAtributo("codigo",rs.getString(m_ConfigTechnical.getString("SQL.A_UOREX.codigo")));				
				elemListVO.setAtributo("descripcion",rs.getString(m_ConfigTechnical.getString("SQL.A_UOREX.descripcion")));
				elemListVO.setAtributo("organizacion",rs.getString(m_ConfigTechnical.getString("SQL.A_UOREX.organizacion")));			
				m_Log.debug((String) elemListVO.getAtributo("codigo") + " , " + (String) elemListVO.getAtributo("descripcion") + " , " + (String) elemListVO.getAtributo("organizacion"));
				unidadesRegistro.add(elemListVO);
			  }
			  rs.close();
			  stmt.close();

		}catch (Exception e){
				unidadesRegistro = new Vector();
				e.printStackTrace();
				m_Log.debug(e.getMessage());
		}finally{
            try{
                 abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
		}
    return unidadesRegistro;
	}

	public String altaUnidadRegistroExterna(GeneralValueObject uniRegExtVO, String[] params) throws TechnicalException {

		  AdaptadorSQLBD abd = null;
		  Connection conexion = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String sql = "";
	
		  String resultado = "";
	  
		  try{
	  		
            String[] parametros = (String []) params.clone();
            parametros[6] = m_ConfigTechnical.getString("CON.jndi");
			abd = new AdaptadorSQLBD(parametros);
			conexion = abd.getConnection();

			String codigo = (String) uniRegExtVO.getAtributo("codUnidadRegistroExterna");
			int ideMax = Integer.parseInt(codigo);
										
			String descripcion = (String) uniRegExtVO.getAtributo("desUnidadRegistroExterna");
			String organizacion = (String) uniRegExtVO.getAtributo("orgUnidadRegistroExterna");
	
			sql = "INSERT INTO " + m_ConfigTechnical.getString("SQL.A_UOREX.nombreTabla") 
				+ " ( "+ m_ConfigTechnical.getString("SQL.A_UOREX.codigo") 
				+ " , " + m_ConfigTechnical.getString("SQL.A_UOREX.descripcion")
				+ " , " + m_ConfigTechnical.getString("SQL.A_UOREX.organizacion")
				+ ") VALUES( " + ideMax + ", '" + descripcion + "'," + organizacion +" )";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

			PreparedStatement ps = conexion.prepareStatement(sql);
			int res = ps.executeUpdate();
			m_Log.debug("Filas insertadas: " + res);
			if (res >0 ) resultado ="insertada";
			else resultado="no insertada";
	
			ps.close();

		  } catch (Exception e) {
				  resultado = "no insertada";
				  e.printStackTrace();
				  m_Log.debug(e.getMessage());  		
		  } finally {
              try{
                   abd.devolverConexion(conexion);
              }catch (Exception e){
                  e.printStackTrace();
                  if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
              }
		  }
        return resultado;
		}

	public String modificarUnidadRegistroExterna(GeneralValueObject uniRegExtVO, String[] params) throws TechnicalException {

			  AdaptadorSQLBD abd = null;
			  Connection conexion = null;
			  ResultSet rs = null;		  
			  String sql = "";
			  String error = "";
			  boolean dependencias = false;
	  
			  try{

                String[] parametros = (String []) params.clone();
                parametros[6] = m_ConfigTechnical.getString("CON.jndi");
				abd = new AdaptadorSQLBD(parametros);
				conexion = abd.getConnection();

				String identificador = (String) uniRegExtVO.getAtributo("idUnidadRegistroExterna");
				String descripcion = (String) uniRegExtVO.getAtributo("desUnidadRegistroExterna");
				String organizacion = (String) uniRegExtVO.getAtributo("orgUnidadRegistroExterna");
				
				m_Log.debug("El código de la unidad de registro externa a modificar es: " + identificador);

				// Eliminar
				sql = "UPDATE " + m_ConfigTechnical.getString("SQL.A_UOREX.nombreTabla") + " SET "
						+ m_ConfigTechnical.getString("SQL.A_UOREX.descripcion") + " = '" + descripcion +"' "
						+ " WHERE " + m_ConfigTechnical.getString("SQL.A_UOREX.codigo") + " =" + identificador +" AND "
                                                +m_ConfigTechnical.getString("SQL.A_UOREX.organizacion") + " =" + organizacion;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				PreparedStatement ps = conexion.prepareStatement(sql);
				int res = ps.executeUpdate();
				m_Log.debug("Filas modificadas: " + res);
				if (res <= 0) error = "no modificada";
				else error="modificada";
			
				ps.close();		

			  } catch (Exception e) {
					  error = "no modificada";
					  e.printStackTrace();
					  m_Log.debug(e.getMessage());  		
			  } finally {
                  try{
                      abd.devolverConexion(conexion);
                  }catch (Exception e){
                      e.printStackTrace();
                      if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                  }
			  }
        return error;
		}

	public String eliminarUnidadRegistroExterna(GeneralValueObject uniRegExtVO, String[] params) throws TechnicalException {

		  AdaptadorSQLBD abd = null;
		  Connection conexion = null;
          AdaptadorSQLBD oad = null;
          Connection con = null;
		  ResultSet rs = null;
		  Statement stmt = null;
		  String sql = "";
		  String error = "";
		  boolean dependencias = false;
	  
		  try{

			abd = new AdaptadorSQLBD(params);	
			conexion = abd.getConnection();
			stmt = conexion.createStatement();

			String identificador = (String) uniRegExtVO.getAtributo("idUnidadRegistroExterna");
			String organizacion = (String) uniRegExtVO.getAtributo("orgUnidadRegistroExterna");
			m_Log.debug("El código de la unidad de registro externa a eliminar es: " + identificador + " y su organizacion externa " + organizacion);

			// Comprobar dependencias.
			
			int cuenta1 = 0;
			sql = "SELECT count(*) AS CUENTA FROM R_RES "  
				+ " WHERE (" + m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino") + "= 1" // a otro registro
				+ " AND " + m_ConfigTechnical.getString("SQL.R_RES.orgDestAnot") + "=" + organizacion  		 
				+ " AND " + m_ConfigTechnical.getString("SQL.R_RES.unidOrigDest") + "=" + identificador
				+ " ) OR (" + m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino") + "= 2" // procedente de otro registro
				+ " AND " + m_ConfigTechnical.getString("SQL.R_RES.orgOrigAnot") + "=" + organizacion
				+ " AND " + m_ConfigTechnical.getString("SQL.R_RES.unidOrigAnot") + "=" + identificador 
				+ ")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()){
				cuenta1 = rs.getInt("CUENTA");
			}
			rs.close();
			m_Log.debug(String.valueOf(cuenta1));
		
			if (cuenta1>0 ){		
				dependencias = true;
				error="tiene dependencias";
			}
			stmt.close();
		
            String[] parametros = (String []) params.clone();
            parametros[6] = m_ConfigTechnical.getString("CON.jndi");
            oad = new AdaptadorSQLBD(parametros);
            //m_Log.debug("A por la conexion");
            con = oad.getConnection();

			if (!dependencias) {
				// Eliminar
				sql = "DELETE FROM " + m_ConfigTechnical.getString("SQL.A_UOREX.nombreTabla") 
					+ " WHERE " + m_ConfigTechnical.getString("SQL.A_UOREX.codigo") + " =" + identificador
					+ " AND " + m_ConfigTechnical.getString("SQL.A_UOREX.organizacion") + " =" + organizacion;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				PreparedStatement ps = con.prepareStatement(sql);
				int res = ps.executeUpdate();
				m_Log.debug("Filas borradas: " + res);
				if (res <= 0) error = "no eliminada";
				else error="eliminada";
				ps.close();
			}

		  } catch (Exception e) {
			error = "no eliminada";
			e.printStackTrace();
			m_Log.debug(e.getMessage());  		
		  }finally {
              try{
                   abd.devolverConexion(conexion);
                   oad.devolverConexion(con);
              }catch (Exception e){
                  e.printStackTrace();
                  if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
              }
		  }
        return error;
	    }


	private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
  		try {
			bd.rollBack(con);
  		}catch(Exception e1) {
			e1.printStackTrace();
			m_Log.error(e1.getMessage());
  		}finally {
			e.printStackTrace();
			m_Log.error(e.getMessage());
  		}
	}

	private void commitTransaction(AdaptadorSQLBD bd,Connection con){
  		try{
			bd.finTransaccion(con);
			bd.devolverConexion(con);
  		}catch(Exception ex) {
			ex.printStackTrace();
			m_Log.error(ex.getMessage());
  		}
  	}
  
  }