/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;


// PAQUETES IMPORTADOS
import java.util.Vector;
import java.util.Iterator;
import java.sql.*;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQL;
/**
 *
 * @author paz.rodriguez
 */
public class CamposDesplegablesExternoDAO {
 private static CamposDesplegablesExternoDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(CamposDesplegablesExternoDAO.class.getName());

  protected CamposDesplegablesExternoDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static CamposDesplegablesExternoDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (CamposDesplegablesExternoDAO.class) {
        if (instance == null) {
          instance = new CamposDesplegablesExternoDAO();
        }
      }
    }
    return instance;
  }
    public Vector getListaCampoDesplegablesExterno(String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      m_Log.info("getListaCampoDesplegablesExterno");
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql = "SELECT CODIGO, DESCRIPCION FROM DESPLEGABLE_EXTERNO ";
      //String[] orden = {campos.getString("SQL.DESPLEGABLE_EXTERNO.CODIGO"),"1"};
      //sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getAdjunto(): -> SQL " + sql);}

      stmt = conexion.prepareStatement(sql);
      //if(m_Log.isDebugEnabled()){m_Log.info(this.getClass().getName() + ".getAdjunto(): -> Ejecutando query....");}
      rs = stmt.executeQuery();
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString("CODIGO"));
        gVO.setAtributo("descripcion",rs.getString("DESCRIPCION"));
        resultado.add(gVO);
      }
    }catch (Exception e){
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
    return resultado;
  }

public Vector altaCampoDesplegableExterno(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
      sql = "INSERT INTO DESPLEGABLE_EXTERNO (CODIGO, DESCRIPCION) VALUES ('" +
       gVO.getAtributo("codigo") +"','" +
       gVO.getAtributo("descripcion") + "')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
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
    Vector lista = new Vector();
    lista = getListaCampoDesplegablesExterno(params);
    return lista;
   }
   

public int modificaGuardaCampoDesplegableExterno(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int resultadoModificacion=0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "UPDATE DESPLEGABLE_EXTERNO set DRIVER_JDBC =?, URL_JDBC=?, USUARIO=?, PASSWORD=?, TABLA=?, CAMPO_CODIGO=?, CAMPO_VALOR=?, CAMPO_VALOR_IDIALT=? WHERE CODIGO=? AND DESCRIPCION=? ";
      
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      int contbd = 1;
      stmt.setString(contbd++, (String)gVO.getAtributo("descDriver_JDBC"));
      stmt.setString(contbd++, (String)gVO.getAtributo("urlDriver"));
      stmt.setString(contbd++, (String)gVO.getAtributo("usuario"));
      stmt.setString(contbd++, (String)gVO.getAtributo("contrasena"));
      stmt.setString(contbd++, (String)gVO.getAtributo("tabla"));
      stmt.setString(contbd++, (String)gVO.getAtributo("campoCodigo"));
      stmt.setString(contbd++, (String)gVO.getAtributo("campoValor"));
      stmt.setString(contbd++, (String)gVO.getAtributo("campoValorId2"));
      stmt.setString(contbd++, (String)gVO.getAtributo("codigo"));
      stmt.setString(contbd, (String)gVO.getAtributo("descripcion"));

      resultadoModificacion = stmt.executeUpdate();
      resultadoModificacion=1;
      stmt.close();
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }
        catch (Exception sqle)
        {
            rollBackTransaction(abd,conexion,sqle);
            resultadoModificacion=0;

        }
        finally
        {
            commitTransaction(abd,conexion);
        }
    //Vector lista = new Vector();
    //lista = getListaCampoDesplegablesExterno(params);
    return resultadoModificacion;
   }

public Vector probarConexion(GeneralValueObject gVO, String[] params){
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    //Vector resultado = new Vector();
    Vector <GeneralValueObject> resultado = new Vector<GeneralValueObject>();
    Vector resultado2 = new Vector();
    GeneralValueObject registro = new GeneralValueObject();
    


    String driver = (String) gVO.getAtributo("descDriver_JDBC");
    String url = (String)gVO.getAtributo("urlDriver");
    String usr = (String)gVO.getAtributo("usuario");
    String pwd = (String)gVO.getAtributo("contrasena");
    String tabla = (String)gVO.getAtributo("tabla");
    String campoCodigo = (String)gVO.getAtributo("campoCodigo");
    String campoValor = (String)gVO.getAtributo("campoValor");
    String campoValorId2 = (String)gVO.getAtributo("campoValorId2");
    String cod_Campo = (String)gVO.getAtributo("codigo");
    StringBuilder sql = new StringBuilder("SELECT ").append(campoCodigo).append(",").append(campoValor);
    if(campoValorId2 !=null && !campoValorId2.equals("")) {
        sql.append("||'|'||").append(campoValorId2);
    }
    sql.append(" FROM ").append(tabla);
    m_Log.debug("sql->" + sql);
    
    try{
        //Cargar o Registrar el Driver JDBC
        Class.forName(driver);
        //Obtener la conexion
        //conn = DriverManager.getConnection(ulrjdbc);
        conn = DriverManager.getConnection(url,usr,pwd);
        if (conn!=null){
            m_Log.debug("Conexion a la bd" + url + "....ok!");
            //Crear el comando SQL
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());
           
            while(rs.next()){
                GeneralValueObject gVO2 = new GeneralValueObject();
                gVO2.setAtributo("codigoV",rs.getString(1));
                gVO2.setAtributo("valor",rs.getString(2));
                gVO2.setAtributo("codigo",  cod_Campo);
                resultado.add(gVO2);
            }
     
            rs.close();
            stmt.close();
            conn.close();
        }//cierre if
    }catch(ClassNotFoundException cnfe){
        m_Log.error("Driver JDBC no encontrado: " + cnfe.getMessage());
        cnfe.printStackTrace();
        
    }catch(SQLException sqle){
        m_Log.error("Error al conectarse a la BD: " + sqle.getMessage());
        
    }catch(Exception e){
        m_Log.error("Error general: " + e.getMessage());
        
    }finally{
        try{
            if(rs!=null) rs.close();
            if(stmt!=null) stmt.close();
            if(conn!=null) conn.close();
            
        }catch(SQLException e){
            m_Log.error("Error al cerrar los recursos asociados a la BBDD: " + e.getMessage());
        }
    }

    return resultado;
   }

public Vector insertaCampoDesplegableExterno(GeneralValueObject gVO, String[] params){
     Vector <GeneralValueObject> resultado = new Vector<GeneralValueObject>();
     resultado = null;
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    int resultadoInsercion=0;

    //1º Comprobamos la conexion
    resultado = probarConexion(gVO, params);
    if (resultado == null || resultado.isEmpty()){
        m_Log.debug("No se ha establecio conexion");
    }else{
        //2º Insertamos en la tabla
         try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            stmt = conexion.createStatement();
            //3ºBorrado de la tabla donde insertar
            sql = "DELETE FROM DESPLEGABLE_EXTERNO_VALOR WHERE COD_CAMPO= '" + gVO.getAtributo("codigo")+ "'";
            //comentamos esta parte ya que los valores se obtendrán dinamicamente
            /*if(m_Log.isDebugEnabled()) m_Log.info(sql);
            stmt = conexion.createStatement();
            stmt.executeUpdate(sql);*/

            for (int j = 0; j < resultado.size(); j++) {
                GeneralValueObject registro = (GeneralValueObject)resultado.get(j);
                sql = "INSERT INTO DESPLEGABLE_EXTERNO_VALOR (COD_CAMPO, CODIGO, VALOR) VALUES ('" +
                registro.getAtributo("codigo") +"','" +
                registro.getAtributo("codigoV") +"','" +
                registro.getAtributo("valor") + "')";
                //comentamos esta parte ya que los valores se obtendrán dinamicamente
               /* if(m_Log.isDebugEnabled()) m_Log.info(sql);
                stmt = conexion.createStatement();
                stmt.executeUpdate(sql);
                resultadoInsercion=1;*/
            }

      
         }catch (Exception e){
                e.printStackTrace();
                resultadoInsercion = 0;
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                resultadoInsercion = 0;
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
    }//cierre 
    try{
    if(resultadoInsercion==1){
    Vector lista = new Vector(); 
    gVO.setAtributo("campo", gVO.getAtributo("codigo"));
    lista = getListaCampoSupDesplegablesExterno(gVO,params);
    return lista;
    }
    else return null;
    }catch (Exception ex)
    {
         ex.printStackTrace();        
         if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
         return null;
    }
}

    public Vector getListaValoresCampoDesplegablesExterno(GeneralValueObject gVO, String[] params){
      Vector resultado = new Vector();
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "";
      try{
        //m_Log.debug("A por el OAD");
        abd = new AdaptadorSQLBD(params);
        //m_Log.debug("A por la conexion");
        conexion = abd.getConnection();
        // Creamos la select con los parametros adecuados.

        String campo = (String)gVO.getAtributo("campo");
        sql = "SELECT CODIGO, DESCRIPCION, DRIVER_JDBC, URL_JDBC, USUARIO, PASSWORD, TABLA, CAMPO_CODIGO, CAMPO_VALOR, CAMPO_VALOR_IDIALT  " +
              "FROM DESPLEGABLE_EXTERNO WHERE CODIGO ='" +
              campo + "'";
        //String[] orden = {campos.getString("SQL.E_DES_VAL.codigoValor"),"1"};
        //sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject temp = new GeneralValueObject();
          temp.setAtributo("codigoCampo",rs.getString(1));
          temp.setAtributo("descripcionCampo",rs.getString(2));
          temp.setAtributo("descDriver_JDBC",rs.getString(3));
          temp.setAtributo("urlDriver",rs.getString(4));
          temp.setAtributo("usuario",rs.getString(5));
          temp.setAtributo("contrasena",rs.getString(6));
          temp.setAtributo("tabla",rs.getString(7));
          temp.setAtributo("campoCodigo",rs.getString(8));
          temp.setAtributo("campoValor",rs.getString(9));
          temp.setAtributo("campoValorId2",rs.getString(10));
          resultado.add(temp);
        }
      }catch (Exception e){
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
      return resultado;
    }

    public Vector modificarCampoDesplegableExterno(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      //stmt = conexion.createStatement();
       sql = "UPDATE DESPLEGABLE_EXTERNO set CODIGO=?, DESCRIPCION=? WHERE CODIGO=? ";
       
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      stmt = conexion.prepareStatement(sql);
      
      stmt.setString(1, (String)gVO.getAtributo("codigo"));
      stmt.setString(2, (String)gVO.getAtributo("descripcion"));
      stmt.setString(3, (String)gVO.getAtributo("codigoAntiguo"));
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      int res = stmt.executeUpdate();
      //m_Log.debug("las filas afectadas en el update son : " + res);
      stmt.close();
      }
      catch (Exception sqle){
            rollBackTransaction(abd,conexion,sqle);
        }
      finally{
            commitTransaction(abd,conexion);
        }
    Vector lista = new Vector();
    lista = getListaCampoDesplegablesExterno(params);
    return lista;
  }
  public Vector getListaCampoSupDesplegablesExterno(GeneralValueObject gVO, String[] params){
      Vector resultado = new Vector();
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = ""; 
      try{
        //m_Log.debug("A por el OAD");
        abd = new AdaptadorSQLBD(params);
        //m_Log.debug("A por la conexion");
        conexion = abd.getConnection();
        // Creamos la select con los parametros adecuados.

        String campo = (String)gVO.getAtributo("campo");
        sql = "SELECT CODIGO, VALOR " +
              "FROM DESPLEGABLE_EXTERNO_VALOR WHERE COD_CAMPO ='" +
              campo + "' order by CODIGO";
        //String[] orden = {campos.getString("SQL.E_DES_VAL.codigoValor"),"1"};
        //sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);  
        //comentamos esta parte ya que los valores se obtendrán dinamicamente
       /* stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject temp = new GeneralValueObject();
          temp.setAtributo("codigo",rs.getString(1));
          temp.setAtributo("descripcion",rs.getString(2));
          resultado.add(temp);
        }*/
      }catch (Exception e){
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
      return resultado;
}
private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
        try
        {
            bd.rollBack(con);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        finally
        {
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }
}

private void commitTransaction(AdaptadorSQLBD bd,Connection con){
        try
        {
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
        }
}



    /**
     * Comprueba si un campo desplegable externo está asignado como campo suplementario a nivel de procedimiento
     * @param codCampo: Código del campo desplegable externo
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean estaCampoAsignadoCampoSuplementarioProcedimiento(String codCampo,Connection con) throws SQLException {
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA,DESPLEGABLE_EXTERNO " + 
                         "WHERE PCA_ACTIVO='SI' AND PCA_DESPLEGABLE=? AND PCA_DESPLEGABLE=DESPLEGABLE_EXTERNO.CODIGO";
            m_Log.debug(sql);
            
            int i=1;            
            ps = con.prepareStatement(sql);
            ps.setString(i++,codCampo);
            rs = ps.executeQuery();
            
            int num =0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }
            if(num>=1) exito = true;
            
        }catch(SQLException e){            
            m_Log.error("Error al comprobar si un campo desplegable está asignado a un campo suplementario a nivel de procedimiento: " + e.getMessage());            
            throw e;
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();                
            }catch(SQLException e){
                m_Log.error("Error al cerrar los recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }            
        }        
        return exito;
    }
    
    
    /**
     * Comprueba si un campo desplegable externo está asignado como campo suplementario a nivel de trámite
     * @param codCampo: Código del campo desplegable externo
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean estaCampoAsignadoCampoSuplementarioTramite(String codCampo,Connection con) throws SQLException{
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA, DESPLEGABLE_EXTERNO " + 
                         "WHERE TCA_ACTIVO='SI' AND TCA_DESPLEGABLE=? AND TCA_DESPLEGABLE=DESPLEGABLE_EXTERNO.CODIGO";
            m_Log.debug(sql);
            
            int i=1;            
            ps = con.prepareStatement(sql);
            ps.setString(i++,codCampo);
            rs = ps.executeQuery();
            
            int num=0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }
            if(num>=1) exito = true;
            
        }catch(SQLException e){            
            m_Log.error("Error al comprobar si un campo desplegable está asignado a un campo suplementario a nivel de trámite: " + e.getMessage());            
            throw e;
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();                
            }catch(SQLException e){
                m_Log.error("Error al cerrar los recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }            
        }        
        return exito;
    }



    
    /**
     * Comprueba si un campo desplegable externo está asignado como campo suplementario a nivel de trámite
     * @param codCampo: Código del campo desplegable externo
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean eliminarCampoDesplegableExterno(String codCampo,Connection con) throws SQLException{
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "DELETE FROM DESPLEGABLE_EXTERNO WHERE CODIGO=?";
            m_Log.debug(sql);
            
            int i=1;            
            ps = con.prepareStatement(sql);
            ps.setString(i++,codCampo);
            int rows= ps.executeUpdate();
            if(rows>=1) exito = true;
            
            
        }catch(SQLException e){            
            m_Log.error("Error al eliminar el campo desplegable externo " + codCampo + ": " +  e.getMessage());     
            throw e;
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();                
            }catch(SQLException e){
                m_Log.error("Error al cerrar los recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }            
        }        
        return exito;
    }

    
    
}
