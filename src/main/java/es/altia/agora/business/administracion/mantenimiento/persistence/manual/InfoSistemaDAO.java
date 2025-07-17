// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.LabelValueTO;
import java.util.ArrayList;
import java.sql.ResultSet;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: IdiomasesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class InfoSistemaDAO  {
  private static InfoSistemaDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(InfoSistemaDAO.class.getName());

  protected InfoSistemaDAO() { 
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static InfoSistemaDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (InfoSistemaDAO.class) {
        if (instance == null) {
          instance = new InfoSistemaDAO();
        }
      }
    }
    return instance;
  }

  
  
  public Vector ejecutarConsulta(String[] params, String consulta){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    ResultSet rs;
    Vector resultados=new Vector();
    Vector aux=new Vector();
    
    try{
      //m_Log.debug("A por el OAD");
     
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
      
      
      m_Log.debug(consulta);
      rs=stmt.executeQuery(consulta);

     int columnas = rs.getMetaData().getColumnCount();
     
     Vector cabeceras=new Vector();
     for(int i=1; i<=columnas; i++){
         String nombreColumna=rs.getMetaData().getColumnName(i);
         m_Log.debug(nombreColumna+";");
        
         cabeceras.add(nombreColumna);
     }
     
     resultados.add(cabeceras);
     
     
     while(rs.next()){
         Vector fila= new Vector(columnas);
         for(int i=1; i<=columnas; i++){
             if(rs.getObject(i)!=null)
             {
             m_Log.debug((rs.getObject(i).toString())+";");
             fila.add((rs.getObject(i).toString()));
             }
             
             else{
                 fila.add(" ");
                
             }
      }
      resultados.add(fila);
      
      
    }
     
     stmt.close();
     rs.close();
     if(m_Log.isDebugEnabled()) m_Log.debug("resultado: "+resultados.toString());
    
      
    } catch (Exception e) {
    	try {
    		// Se deshace la transacción
    		abd.rollBack(conexion);
    	} catch (BDException ex) {
    		//ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            
    	}
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        aux.add(e.getMessage().replaceAll("[\n\r]",""));
        resultados.add(aux);
    } finally {
        try{
            
            abd.devolverConexion(conexion);
            return resultados;
        } catch (Exception e) {
            //e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    return resultados;
    
  }
 
  public Vector ConsultasAdministrador(String[] params, String consulta){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    ResultSet rs; 
    Vector resultadosExp=new Vector();
    Vector aux=new Vector();
    
    try{
      //m_Log.debug("A por el OAD");
     
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
        
      if(m_Log.isDebugEnabled()) m_Log.debug(consulta);
      rs=stmt.executeQuery(consulta);

     int columnas = rs.getMetaData().getColumnCount();
     
     Vector cabeceras=new Vector();
     for(int i=1; i<=columnas; i++){
         String nombreColumna=rs.getMetaData().getColumnName(i);
        
             cabeceras.add(nombreColumna);
         
     }
 
     resultadosExp.add(cabeceras);
    
     while(rs.next()){
         Vector fila= new Vector(columnas);
         for(int i=1; i<=columnas; i++){
             if(rs.getObject(i)!=null)
             {
             fila.add((rs.getObject(i).toString()));
             }    
             else{
                 fila.add(" ");
             }
      }
      resultadosExp.add(fila);
    }
     
     stmt.close();
     rs.close();
     if(m_Log.isDebugEnabled()) m_Log.debug("resultadoExp: "+resultadosExp.toString());
    
      
    } catch (Exception e) {
    	try {
    		// Se deshace la transacción
    		abd.rollBack(conexion);
    	} catch (BDException ex) {
    		ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            
    	}
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        aux.add(e.getMessage().replaceAll("[\n\r]",""));
        resultadosExp.add(aux);
    } finally {
        try{
            
            abd.devolverConexion(conexion);
            return resultadosExp;
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    return resultadosExp;
    
  }	
    
     
     }
