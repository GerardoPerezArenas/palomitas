/*
 * GestorConexion.java
 *
 * Created on 17 de octubre de 2002, 13:08
 */

package es.altia.agora.interfaces.user.web.util;

import es.altia.technical.*;

import java.sql.*;

import javax.naming.*;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author
 * @version
 */
public class GestorConexion {

    private Connection conexion;
    private DataSource dataSource;
    private ResultSet resultSet;
    private String jndiName;
    private static final String JNDI_PREFIX = "java:comp/env/";
    private static Log log =
            LogFactory.getLog(GestorConexion.class.getName());

    /** Creates new GestorConexion */
    public GestorConexion() throws SQLException,NamingException{
      this.jndiName="lectura_jndi";
      this.conectar();
    }

    /**Construye un Gestor de Conexiones a partir de un JNDI_NAME**/
    public GestorConexion(String jndiName) throws SQLException,NamingException{
      this.jndiName=jndiName;
      this.conectar();
      log.debug("Creada instancia de GestorConexion");
    }
    /**Abre una conexión con la Base de Datos**/
    public void conectar() throws SQLException,NamingException{
      if(this.conexion!=null){
            if(this.conexion.isClosed())
              this.conexion=this.getConnection(this.jndiName);
      }else this.conexion=this.getConnection(this.jndiName);
    }

    /**Cierra la conexión con la Base de Datos**/
    public void desconectar() {
      /*Eliminar una conmexión*/
      try{
        if(this.resultSet!=null)this.resultSet.close();
        if(this.conexion!=null){
           this.conexion.close();
           log.debug("Conexión cerrada en GestorConexión");
        }
      }catch(SQLException e){
        log.error("Excepcion en el método desconectar de GestorConexión: ");
        e.printStackTrace();
      }
    }

    /**Ejecuta una sentencia sql contra la Base de Datos con la que
     se ha establecido la conexión**/
    public ResultSet ejecutarQuery (String query) throws SQLException{
      /*Ejecutar una sentencia sql*/
      ResultSet rs=null;

      try{

       Statement stm = this.conexion.createStatement();
       rs= stm.executeQuery(query);

      }catch(Exception e){
        try{
        conexion.rollback();
        if(this.conexion!=null)this.conexion.close();
        }catch(SQLException ee){
            if(rs!=null) rs.close();

            throw ee;
        }

      }


       return this.resultSet=rs;


    }



    public int ejecutarUpdate (String query) throws SQLException{
      /*Ejecutar una sentencia sql*/
      int resultado=0;

      try{

       Statement stm = this.conexion.createStatement();
       resultado= stm.executeUpdate(query);

      }catch(Exception e){
        try{
        conexion.close();
        }catch(SQLException ee){

            conexion.rollback();
            throw ee;
        }

      }


       return resultado;


    }

    /**Realiza el comit de las sentencias ejecutadas**/
    public void commit () throws SQLException{
       /*Realiza un commit de una consulta*/
      conexion.commit();
    }




    private Connection getConnection(String jndiName){
      Context ctx = null;
      Connection con = null;
      try {
        PortableContext pc = PortableContext.getInstance();
        if (log.isDebugEnabled()) log.debug("He cogido el jndi: "+ jndiName);
        //DataSource ds = (DataSource)pc.lookup( JNDI_PREFIX + jndiName, DataSource.class);
        DataSource ds = (DataSource)pc.lookup(jndiName, DataSource.class);
        con=ds.getConnection();
        log.debug("Adquirida Conexion en GestorConexion");
      } catch (Exception e) {
        log.error("Error en el getConnection de GestorConexión");
        e.printStackTrace();
      }
      return con;
    }
  }
