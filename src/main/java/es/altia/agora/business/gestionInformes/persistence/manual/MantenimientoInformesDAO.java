package es.altia.agora.business.gestionInformes.persistence.manual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.AmbitoListaValueObject;
import es.altia.agora.business.gestionInformes.CriteriosSolicitudValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.gestionInformes.SolicitudesListaValueObject;
import es.altia.agora.business.gestionInformes.tareas.AsistenteEstado;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.GeneralOperations;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class MantenimientoInformesDAO {
   //Para el fichero de configuracion tecnico.
   protected static Config conf;
   //Para informacion de logs.
   protected static Log m_Log =
            LogFactory.getLog(SolicitudesInformesDAO.class.getName());


   private static MantenimientoInformesDAO instance = null;

   protected MantenimientoInformesDAO() {
	super();
	//Queremos usar el fichero de configuracion techserver
       conf = ConfigServiceHelper.getConfig("techserver");
          //Conexion
   }

   public static MantenimientoInformesDAO getInstance() {
	//si no hay ninguna instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
	   synchronized(MantenimientoInformesDAO.class){
		if (instance == null)
		   instance = new MantenimientoInformesDAO();
	   }
	}
	return instance;
   }

   
    public void modificarOrigen (ElementoListaValueObject siVO, String[] params) throws Exception {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt;
        String codigo=siVO.getCodigo();
        String descripcion=siVO.getDescripcion();
        String tab=siVO.getTab();
        String modo=siVO.getModo();
        
        m_Log.debug(codigo);
        m_Log.debug(descripcion);
        m_Log.debug(tab);
        m_Log.debug(modo);
        
        
        try {
        	 abd = new AdaptadorSQLBD(params);
             conexion = abd.getConnection();
             abd.inicioTransaccion(conexion);
            String sql ="UPDATE INF_ORIGEN SET "+
            			conf.getString("SQL.INF_ORIGEN.cod")+"=" +codigo +"," +
            			conf.getString("SQL.INF_ORIGEN.desc")+"='"+descripcion+"'," +
            			conf.getString("SQL.INF_ORIGEN.tab")+"='"+tab+"'," +
            			conf.getString("SQL.INF_ORIGEN.mod")+"='"+modo+"' WHERE " +
            			conf.getString("SQL.INF_ORIGEN.cod")+"="+codigo;
            			
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            abd.finTransaccion(conexion);

        } catch (SQLException sqle) {
            abd.rollBack(conexion);
            m_Log.error("Error de SQL en anotaInicioSolicitud: " + sqle.getMessage());
            throw new Exception(sqle);
        } catch (BDException bde) {
            abd.rollBack(conexion);
            if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaInicioSolicitud: " + bde.getMessage());
            throw new Exception(bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
                }
            }
        }
    }
    public void insertarOrigen (ElementoListaValueObject siVO, String[] params) throws Exception {
    	
    	String sqlInsertParam = "INSERT INTO INF_ORIGEN("+ conf.getString("SQL.INF_ORIGEN.cod")+","+
            			conf.getString("SQL.INF_ORIGEN.desc")+","+
            			conf.getString("SQL.INF_ORIGEN.tab")+","+
            			conf.getString("SQL.INF_ORIGEN.mod")+") VALUES (?,?,?,?)";    	    	        
    	
    	AdaptadorSQLBD abd = null;
        Connection con = null;
        abd = new AdaptadorSQLBD(params);
        con = abd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;                			
		String codigo=siVO.getCodigo();
	     String descripcion=siVO.getDescripcion();
	     String tab=siVO.getTab();
	     String modo=siVO.getModo();
		ps = con.prepareStatement(sqlInsertParam);
		
		
		int i=1;
           
		ps.setString(i++, codigo);
		ps.setString(i++, descripcion);
		ps.setString(i++,tab);
		ps.setString(i++, modo);
		
		
		int insertedRows = ps.executeUpdate();
		if (insertedRows != 1)
		    throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR EL PARAMETRO");
		
		GeneralOperations.closeResultSet(rs);
		GeneralOperations.closeStatement(ps);                               	
		
	}
public void insertarModoOrigen (ElementoListaValueObject siVO, String[] params) throws Exception {
    	
		String sqlInsertParam = "INSERT INTO INF_MODOORIGEN (ID_MODOORIGEN,DESC_MODOORIGEN) VALUES (?,?)";    	    	        
    	
    	AdaptadorSQLBD abd = null;
        Connection con = null;
        abd = new AdaptadorSQLBD(params);
        con = abd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;                			
		String codigo=siVO.getCodigo();
	     String descripcion=siVO.getDescripcion();
		ps = con.prepareStatement(sqlInsertParam);
		
		
		int i=1;
           
		ps.setString(i++, codigo);
		ps.setString(i++, descripcion);
		
		
		int insertedRows = ps.executeUpdate();
		if (insertedRows != 1)
		    throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR EL PARAMETRO");
		
		GeneralOperations.closeResultSet(rs);
		GeneralOperations.closeStatement(ps);                               	
		
	}
public Integer insertarCampo (AmbitoListaValueObject siVO, String[] params) throws Exception {
	 AdaptadorSQLBD abd = null;
     Connection conexion = null;
     Statement stmt;
     Integer codigo=0;
 	String nome=siVO.getNome();
 	String campo=siVO.getCampo();
 	String tipo=siVO.getTipo();
 	Integer lonxitude=siVO.getLonxitude();
 	Integer origen=siVO.getOrigen();
 	String nomeas=siVO.getNomeas();
 	Integer criterio=siVO.getCriterio();
     
     m_Log.debug(codigo);
     m_Log.debug(nome);
     m_Log.debug(campo);
     m_Log.debug(tipo);
     m_Log.debug(lonxitude);
     m_Log.debug(origen);
     m_Log.debug(nomeas);
     m_Log.debug(criterio);
     
     
     try {
     	 abd = new AdaptadorSQLBD(params);
          conexion = abd.getConnection();
          abd.inicioTransaccion(conexion);
          String sql2="SELECT MAX("+
         			conf.getString("SQL.INF_CAMPOS.cod")+") AS CODIGO FROM INF_CAMPOS";

          if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql2);
          Statement stmt2 = conexion.createStatement();
          ResultSet rs2 = stmt2.executeQuery(sql2);
          while(rs2.next()){
              codigo=rs2.getInt(1);
              }
          m_Log.debug("************codigo: " + codigo);
          rs2.close();
          stmt2.close();
          codigo=codigo+1;
         String sql ="INSERT INTO INF_CAMPOS("+
         			conf.getString("SQL.INF_CAMPOS.cod")+"," +
         			conf.getString("SQL.INF_CAMPOS.nom")+"," +
         			conf.getString("SQL.INF_CAMPOS.cam")+","+
         			conf.getString("SQL.INF_CAMPOS.tip")+"," +
         			conf.getString("SQL.INF_CAMPOS.lon")+"," +
         			conf.getString("SQL.INF_CAMPOS.org")+"," +
         			conf.getString("SQL.INF_CAMPOS.nomas")+"," +
         			conf.getString("SQL.INF_CAMPOS.criterio")+") VALUES ("+codigo+",'"+nome+"','"+campo+"','"+tipo+"',"+lonxitude+","+origen+",'"+nomeas+"',"+criterio+")";
         
         stmt = conexion.createStatement();
         if(m_Log.isDebugEnabled()) m_Log.debug(sql);
         stmt.executeUpdate(sql);
         stmt.close();

         abd.finTransaccion(conexion);

     } catch (SQLException sqle) {
         abd.rollBack(conexion);
         m_Log.error("Error de SQL en anotaInicioSolicitud: " + sqle.getMessage());
         throw new Exception(sqle);
     } catch (BDException bde) {
         abd.rollBack(conexion);
         if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaInicioSolicitud: " + bde.getMessage());
         throw new Exception(bde);
     } finally {
         if (conexion != null) {
             try {
                 abd.devolverConexion(conexion);
             } catch(BDException bde) {
                 m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
             }
         }
     }
     return codigo;
 }
public void modificarCampo (AmbitoListaValueObject siVO, String[] params) throws Exception {


    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt;
    Integer codigo=siVO.getCodigo();
 	String nome=siVO.getNome();
 	String campo=siVO.getCampo();
 	String tipo=siVO.getTipo();
 	Integer lonxitude=siVO.getLonxitude();
 	Integer origen=siVO.getOrigen();
 	String nomeas=siVO.getNomeas();
 	Integer criterio=siVO.getCriterio();
     
     m_Log.debug(codigo);
     m_Log.debug(nome);
     m_Log.debug(campo);
     m_Log.debug(tipo);
     m_Log.debug(lonxitude);
     m_Log.debug(origen);
     m_Log.debug(nomeas);
     m_Log.debug(criterio);
    
    try {
    	 abd = new AdaptadorSQLBD(params);
         conexion = abd.getConnection();
         abd.inicioTransaccion(conexion);
        String sql ="UPDATE INF_CAMPOS SET "+
        			conf.getString("SQL.INF_CAMPOS.cod")+"=" +codigo +"," +
        			conf.getString("SQL.INF_CAMPOS.nom")+"='"+nome+"'," +
        			conf.getString("SQL.INF_CAMPOS.cam")+"='"+campo+"'," +
        			conf.getString("SQL.INF_CAMPOS.tip")+"='"+tipo+"'," +
        			conf.getString("SQL.INF_CAMPOS.lon")+"="+lonxitude+"," +
        			conf.getString("SQL.INF_CAMPOS.org")+"="+origen+"," +
        			conf.getString("SQL.INF_CAMPOS.nomas")+"='"+nomeas+"'," +
        			conf.getString("SQL.INF_CAMPOS.criterio")+"="+criterio+" WHERE " +
        			conf.getString("SQL.INF_CAMPOS.cod")+"="+codigo;
        			
        
        
        stmt = conexion.createStatement();
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();

        abd.finTransaccion(conexion);

    } catch (SQLException sqle) {
        abd.rollBack(conexion);
        m_Log.error("Error de SQL en anotaInicioSolicitud: " + sqle.getMessage());
        throw new Exception(sqle);
    } catch (BDException bde) {
        abd.rollBack(conexion);
        if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaInicioSolicitud: " + bde.getMessage());
        throw new Exception(bde);
    } finally {
        if (conexion != null) {
            try {
                abd.devolverConexion(conexion);
            } catch(BDException bde) {
                m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
            }
        }
    }
}
public void modificarModoOrigen (ElementoListaValueObject siVO, String[] params) throws Exception {

    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt;
    String codigo=siVO.getCodigo();
    String descripcion=siVO.getDescripcion();
    String tab=siVO.getTab();
    String modo=siVO.getModo();
    
    m_Log.debug(codigo);
    m_Log.debug(descripcion);


    try {
    	 abd = new AdaptadorSQLBD(params);
         conexion = abd.getConnection();
         abd.inicioTransaccion(conexion);
        String sql ="UPDATE INF_MODOORIGEN SET ID_MODOORIGEN =" +codigo +",DESC_MODOORIGEN ='"+descripcion+"' WHERE ID_MODOORIGEN ="+codigo;
        			
        stmt = conexion.createStatement();
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();

        abd.finTransaccion(conexion);

    } catch (SQLException sqle) {
        abd.rollBack(conexion);
        m_Log.error("Error de SQL en anotaInicioSolicitud: " + sqle.getMessage());
        throw new Exception(sqle);
    } catch (BDException bde) {
        abd.rollBack(conexion);
        if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaInicioSolicitud: " + bde.getMessage());
        throw new Exception(bde);
    } finally {
        if (conexion != null) {
            try {
                abd.devolverConexion(conexion);
            } catch(BDException bde) {
                m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
            }
        }
    }
}
public void eliminarModoOrigen (ElementoListaValueObject siVO, String[] params) throws Exception {

    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt;
    String codigo=siVO.getCodigo();
    String descripcion=siVO.getDescripcion();
    String tab=siVO.getTab();
    String modo=siVO.getModo();
    
    m_Log.debug(codigo);
    m_Log.debug(descripcion);


    try {
    	 abd = new AdaptadorSQLBD(params);
         conexion = abd.getConnection();
         abd.inicioTransaccion(conexion);
        String sql ="UPDATE INF_MODOORIGEN SET ID_MODOORIGEN =" +codigo +",DESC_MODOORIGEN ='"+descripcion+"' WHERE ID_MODOORIGEN ="+codigo;
        			
        stmt = conexion.createStatement();
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();

        abd.finTransaccion(conexion);

    } catch (SQLException sqle) {
        abd.rollBack(conexion);
        m_Log.error("Error de SQL en anotaInicioSolicitud: " + sqle.getMessage());
        throw new Exception(sqle);
    } catch (BDException bde) {
        abd.rollBack(conexion);
        if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaInicioSolicitud: " + bde.getMessage());
        throw new Exception(bde);
    } finally {
        if (conexion != null) {
            try {
                abd.devolverConexion(conexion);
            } catch(BDException bde) {
                m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
            }
        }
    }
}
 public void eliminarOrigen (Integer codigo, String[] params) throws Exception {
	 String sqlDelParam = "DELETE INF_ORIGEN WHERE ID_ORIGEN = ?";

	   m_Log.debug("Estoy en delete  "+codigo);
	 AdaptadorSQLBD abd = null;
     Connection con = null;
     abd = new AdaptadorSQLBD(params);
     con = abd.getConnection();
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
        

         ps = con.prepareStatement(sqlDelParam);
         if(m_Log.isDebugEnabled()) m_Log.debug(sqlDelParam);
  
			ps.setInt(1, codigo);            
         
         ps.executeUpdate();
         
         if(m_Log.isDebugEnabled()) m_Log.debug(sqlDelParam);
         
	    } catch (SQLException sqle) {
	        m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW");
	        throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW", sqle);
	    } finally {
	        GeneralOperations.closeResultSet(rs);
	        GeneralOperations.closeStatement(ps);
	       
	    }                             	
		
	}
 public void eliminarModoOrigen (Integer codigo, String[] params) throws Exception {
	 String sqlDelParam = "DELETE INF_MODOORIGEN WHERE ID_MODOORIGEN = ?";

	   m_Log.debug("Estoy en delete  "+codigo);
	 AdaptadorSQLBD abd = null;
     Connection con = null;
     abd = new AdaptadorSQLBD(params);
     con = abd.getConnection();
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
        

         ps = con.prepareStatement(sqlDelParam);
         if(m_Log.isDebugEnabled()) m_Log.debug(sqlDelParam);
  
			ps.setInt(1, codigo);            
         
         ps.executeUpdate();
         
	   
	    } catch (SQLException sqle) {
	        m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW");
	        throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW", sqle);
	    } finally {
	        GeneralOperations.closeResultSet(rs);
	        GeneralOperations.closeStatement(ps);
	       
	    }                             	
		
	}
 public String eliminarCampo(String desc,String nom,Integer codigo, String[] params) throws Exception {
	 String resultado = null;
	 m_Log.debug("***Estoy en delete  "+codigo);
	 AdaptadorSQLBD abd = null;
	    Connection conexion = null;
	    Statement stmt=null;
	    ResultSet rs = null;
	    try {
	    	 abd = new AdaptadorSQLBD(params);
	         conexion = abd.getConnection();
	         abd.inicioTransaccion(conexion);
	         //compruebo que el campo no este asociado a ninguna plantilla sino no se puede eliminar
	        String sql ="SELECT INF_CAMPOS.NOME,PLANT_INF_COL.PLANT_INF_COL_TITULO FROM INF_CAMPOS,INF_ORIGEN,PLANT_INFORMES,PLANT_INF_COL WHERE(INF_ORIGEN.ID_ORIGEN ="+desc+")" +
	        		"AND(INF_CAMPOS.NOME = '"+nom+"')" +
	        		"AND(INF_CAMPOS.ORIGEN = INF_ORIGEN.ID_ORIGEN)" +
	        		"AND(PLANT_INFORMES.PLANT_ORIGEN = INF_ORIGEN.ID_ORIGEN)" +
	        		"AND(PLANT_INF_COL.PLANT_INF_COL_PLANTILLA = PLANT_INFORMES.PLANT_PLANTILLA)" +
	        		"AND(PLANT_INF_COL.PLANT_INF_COL_TITULO = INF_CAMPOS.NOME)";

	        stmt = conexion.createStatement();
	        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	        rs = stmt.executeQuery(sql);
            if (rs.next()) {
            	resultado = rs.getString(1);
            }
            rs.close();
            stmt.close();
            if(resultado==null){
            	 m_Log.debug("*********************  dentro   "+resultado);
            	 String sqlDelParam = "DELETE INF_CAMPOS WHERE CODIGO = ?";
                 PreparedStatement ps2 = null;
                 ResultSet rs2 = null;
                 try {
                    

                     ps2 = conexion.prepareStatement(sqlDelParam);
                     if(m_Log.isDebugEnabled()) m_Log.debug(sqlDelParam);
              
            			ps2.setInt(1, codigo);            
                     
                     ps2.executeUpdate();
                     
            	   
            	    } catch (SQLException sqle) {
            	        m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW");
            	        throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW", sqle);
            	    } finally {
            	        GeneralOperations.closeResultSet(rs2);
            	        GeneralOperations.closeStatement(ps2);
            	       
            	    }                             	
            		
            	 
            }
	        abd.finTransaccion(conexion);

	    } catch (SQLException sqle) {
	        abd.rollBack(conexion);
	        m_Log.error("Error de SQL en consultar campo en plantilla: " + sqle.getMessage());
	        throw new Exception(sqle);
	    } catch (BDException bde) {
	        abd.rollBack(conexion);
	        if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaInicioSolicitud: " + bde.getMessage());
	        throw new Exception(bde);
	    } finally {
	        if (conexion != null) {
	            try {
	                abd.devolverConexion(conexion);
	            } catch(BDException bde) {
	                m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
	            }
	        }
	    }
	    m_Log.debug("*********************     "+resultado);
	 	return resultado;
	}
} 
	/* String sqlDelParam = "DELETE INF_CAMPOS WHERE CODIGO = ?";

	  
	 AdaptadorSQLBD abd = null;
     Connection con = null;
     abd = new AdaptadorSQLBD(params);
     con = abd.getConnection();
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
        

         ps = con.prepareStatement(sqlDelParam);
         if(m_Log.isDebugEnabled()) m_Log.debug(sqlDelParam);
  
			ps.setInt(1, codigo);            
         
         ps.executeUpdate();
         
	   
	    } catch (SQLException sqle) {
	        m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW");
	        throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW", sqle);
	    } finally {
	        GeneralOperations.closeResultSet(rs);
	        GeneralOperations.closeStatement(ps);
	       
	    }                             	
		
	}*/
	 
