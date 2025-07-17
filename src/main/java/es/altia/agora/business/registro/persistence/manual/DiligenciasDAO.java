// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.registro.DiligenciasValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DiligenciasDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DiligenciasDAO {

  	private static DiligenciasDAO instance = null;
	protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
	protected static Config m_ConfigError; // Para los mensajes de error localizados
	protected static Log m_Log =
            LogFactory.getLog(DiligenciasDAO.class.getName());

    protected static String codDepto;
	protected static String codUnidad;
	protected static String fecha;
	protected static String tipo;
	protected static String anotacion;

  	protected DiligenciasDAO() {
    		super();
    		// Queremos usar el fichero de configuracion techserver
    		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    		// Queremos tener acceso a los mensajes de error localizados
    		m_ConfigError = ConfigServiceHelper.getConfig("error");

    		codDepto = m_ConfigTechnical.getString("SQL.R_DIL.codDepto");
    		codUnidad = m_ConfigTechnical.getString("SQL.R_DIL.codUnidad");
    		fecha = m_ConfigTechnical.getString("SQL.R_DIL.fecha");
    		tipo = m_ConfigTechnical.getString("SQL.R_DIL.tipo");
    		anotacion = m_ConfigTechnical.getString("SQL.R_DIL.anotacion");
  	}

  	public static DiligenciasDAO getInstance() {
    		// si no hay ninguna instancia de esta clase tenemos que crear una
    		if (instance == null) {
      		// Necesitamos sincronizacion para serializar (no multithread)
      		// Las invocaciones de este metodo
      		synchronized(DiligenciasDAO.class) {
        			if (instance == null) {
          				instance = new DiligenciasDAO();
        			}
      		}
    		}
    		return instance;
  	}

  	public Vector loadVector(DiligenciasValueObject diligVO,String[] params) throws TechnicalException{
    		String sql= "";
    		AdaptadorSQLBD bd = null;
    		Connection con=null;
    		ResultSet rs=null;
    		Statement state=null;
    		Vector lista = new Vector();
    		TransformacionAtributoSelect transformador;
    		try{
      		bd = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(bd);
            con = bd.getConnection();
      		state = con.createStatement();

      		sql = "SELECT " + anotacion +
      		      "," + bd.convertir(fecha, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA" +
      		      " FROM R_DIL WHERE " +
            		codDepto + "=" + diligVO.getCodDepto() + " AND " +
            		codUnidad + "="+ diligVO.getCodUnidad() +
            		" AND " + tipo + "='" + diligVO.getTipo()+"' ";
            		
          if ( diligVO.getFecha() != null) {
	          if ( !"".equals(diligVO.getFecha()))  		
          		sql += "AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(fecha , diligVO.getFecha());
          }
          if (diligVO.getAnotacion() != null) {
          	if (!"".equals(diligVO.getAnotacion().trim())) {
            	String condicion = transformador.construirCondicionWhereConOperadores(anotacion, diligVO.getAnotacion().trim(),false); 
            	if (!"".equals(condicion)) {
								sql += " AND " + condicion + " ";
							}
		  			}
        	}
        	sql += " ORDER BY " + fecha;
            		
            		
      		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      		
      		rs=state.executeQuery(sql);
      		while(rs.next()) {
        		DiligenciasValueObject dilig = new DiligenciasValueObject();
        		dilig.setAnotacion(bd.js_escape(TransformacionAtributoSelect.replace(rs.getString(anotacion),"'","\'")));
        		dilig.setFecha(rs.getString("FECHA"));
        		lista.addElement(dilig);
        	}
      		rs.close();
      		state.close();
    		}catch(Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    		}finally {
                try{
                    bd.devolverConexion(con);
                }catch (Exception e){
                    e.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }
    		}
    		return lista;
  	}
  	
  public DiligenciasValueObject load(DiligenciasValueObject diligVO,String[] params) throws TechnicalException{
    		String sql= "";
    		AdaptadorSQLBD bd = null;
    		Connection con=null;
    		ResultSet rs=null;
    		Statement state=null;
    		try{
      		bd = new AdaptadorSQLBD(params);
      		con = bd.getConnection();
      		state = con.createStatement();

      		sql = "SELECT * FROM R_DIL WHERE " +
            		codDepto + "=" + diligVO.getCodDepto() + " AND " +
            		codUnidad + "="+ diligVO.getCodUnidad() + " AND " +
            		fecha + "=" + bd.convertir("'" + diligVO.getFecha() + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
            		" AND " + tipo + "='" + diligVO.getTipo()+"'";
      		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      		
      		rs=state.executeQuery(sql);
      		if(rs.next())
        			diligVO.setAnotacion(bd.js_escape(TransformacionAtributoSelect.replace(rs.getString(anotacion),"'","\'")));
      		else diligVO.setAnotacion("");
      		rs.close();
      		state.close();
            }catch(Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally {
                try{
                    bd.devolverConexion(con);
                }catch (Exception e){
                    e.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }
            }
    		return diligVO;
  	}


	public int modify(DiligenciasValueObject diligVO,String[] params)throws TechnicalException{
    		String sql= "";
	    	AdaptadorSQLBD bd = null;
	    	Connection con=null;
	    	ResultSet rs=null;
	    	Statement state=null;
	    	int correcto = 0;
    		try{
      		bd = new AdaptadorSQLBD(params);
      		con = bd.getConnection();
      		state = con.createStatement();

      		sql = "UPDATE R_DIL SET " + anotacion + "='" + TransformacionAtributoSelect.replace(diligVO.getAnotacion(),"'","''") +
            		"' WHERE " + codDepto + "=" + diligVO.getCodDepto() + " AND " +
            		codUnidad + "="+ diligVO.getCodUnidad() + " AND " +
            		fecha + "=" + bd.convertir("'" + diligVO.getFecha() + "'",
                      AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
            		" AND " + tipo + "='" + diligVO.getTipo()+"'";
      		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      		
      		state.executeUpdate(sql);
	  		diligVO.setAnotacion(bd.js_escape(TransformacionAtributoSelect.replace(diligVO.getAnotacion(),"'","\'")));
	  		state.close();
            }catch(Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally {
                try{
                    bd.devolverConexion(con);
                }catch (Exception e){
                    e.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }
            }
    		return correcto;
  	}

  	public int insert(DiligenciasValueObject diligVO,String[] params)throws TechnicalException{
    		String sql= "";
    		AdaptadorSQLBD bd = null;
    		Connection con=null;
    		ResultSet rs=null;
    		Statement state=null;
    		int correcto = 0;
    		try{
      		bd = new AdaptadorSQLBD(params);
      		con = bd.getConnection();
      		state = con.createStatement();
      		
      		String anot = "";
      		sql = "SELECT * FROM R_DIL WHERE " +
            		codDepto + "=" + diligVO.getCodDepto() + " AND " +
            		codUnidad + "="+ diligVO.getCodUnidad() + " AND " +
            		fecha + "=" + bd.convertir("'" + diligVO.getFecha() + "'",
                      AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
            		" AND " + tipo + "='" + diligVO.getTipo()+"'";
      		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      		
      		rs=state.executeQuery(sql);
      		if(rs.next()) {
      			anot = rs.getString(anotacion);
      		}
      		rs.close();
      		
      		if(anot == null || anot.equals("")) {
	      		sql = "INSERT INTO R_DIL VALUES(" + diligVO.getCodDepto() + ", " +
	            		diligVO.getCodUnidad() + ", " +
	            		bd.convertir("'" + diligVO.getFecha() + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY")
                          + ", '" +
	            		diligVO.getTipo() + "', '" + TransformacionAtributoSelect.replace(diligVO.getAnotacion(),"'","''") + "')";
          } else {
            sql = "UPDATE R_DIL SET " + anotacion + "='" + TransformacionAtributoSelect.replace(diligVO.getAnotacion(),"'","''") +
            		"' WHERE " + codDepto + "=" + diligVO.getCodDepto() + " AND " +
            		codUnidad + "="+ diligVO.getCodUnidad() + " AND " +
            		fecha + "=" + bd.convertir("'" + diligVO.getFecha() + "'",
                    AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
            		" AND " + tipo + "='" + diligVO.getTipo()+"'";	
          }
      		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      
      		state.executeUpdate(sql);
	  		diligVO.setAnotacion(bd.js_escape(TransformacionAtributoSelect.replace(diligVO.getAnotacion(),"'","\'")));
	  		state.close();
            }catch(Exception e){
                correcto = -1;
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally {
                try{
                    bd.devolverConexion(con);
                }catch (Exception e){
                    e.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }
            }
    		return correcto;
  	}

  	public int delete(DiligenciasValueObject diligVO,String[] params) throws TechnicalException{
    		String sql= "";
    		AdaptadorSQLBD bd = null;
    		Connection con=null;
    		ResultSet rs=null;
    		Statement state=null;
    		int correcto = 0;
    		try{
      		bd = new AdaptadorSQLBD(params);
      		con = bd.getConnection();
      		state = con.createStatement();

      		sql = "DELETE FROM R_DIL" + " WHERE " +
            		codDepto + "=" + diligVO.getCodDepto() + " AND " +
            		codUnidad + "="+ diligVO.getCodUnidad() + " AND " +
            		fecha + "=" + bd.convertir("'" + diligVO.getFecha() + "'",
                      AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
            		" AND " + tipo + "='" + diligVO.getTipo()+"'";
      		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      
      		state.executeUpdate(sql);
      		state.close();
            }catch(Exception e){
                correcto = -1;
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally {
                try{
                    bd.devolverConexion(con);
                }catch (Exception e){
                    e.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }
            }
    		return correcto;
  	}

  	private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
    		try {
      		bd.rollBack(con);
    		}catch(Exception e1) {
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
    		}catch(Exception ex) {
      		ex.printStackTrace();
      		m_Log.error("SQLException: " + ex.getMessage()) ;
    		}
  	}
}