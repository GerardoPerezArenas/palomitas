// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import java.util.Vector;
import java.util.Iterator;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase CampoDesplegablesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class CamposDesplegablesDAO  {
  private static CamposDesplegablesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(CamposDesplegablesDAO.class.getName());

  protected CamposDesplegablesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static CamposDesplegablesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (CamposDesplegablesDAO.class) {
        if (instance == null) {
          instance = new CamposDesplegablesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarCampoDesplegable(String codigo, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
      sql = "DELETE FROM E_DES WHERE " +
        campos.getString("SQL.E_DES.codigo")+"='"+codigo+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      sql = "DELETE FROM E_DES_VAL WHERE " +
            campos.getString("SQL.E_DES_VAL.campoValor")+"='"+codigo+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
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
    lista = getListaCampoDesplegables(params);
    return lista;
  }

  public Vector getListaCampoDesplegables(String[] params){
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
      sql = "SELECT * FROM E_DES ";
      String[] orden = {campos.getString("SQL.E_DES.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.E_DES.codigo")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.E_DES.nombre")));
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

  public Vector modificarCampoDesplegable(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "UPDATE E_DES SET " +
        campos.getString("SQL.E_DES.codigo")+"='"+gVO.getAtributo("codigo")+"',"+
        campos.getString("SQL.E_DES.nombre")+"='"+gVO.getAtributo("descripcion")+"'"+
        " WHERE " +
        campos.getString("SQL.E_DES.codigo")+"='"+gVO.getAtributo("codigoAntiguo")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el update son : " + res);
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
    lista = getListaCampoDesplegables(params);
    return lista;
  }

  public Vector altaCampoDesplegable(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO E_DES ("+
       campos.getString("SQL.E_DES.codigo")+","+
       campos.getString("SQL.E_DES.nombre")+
       ") VALUES ('" +
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
    lista = getListaCampoDesplegables(params);
    return lista;
   }

    public Vector getListaValoresCampoDesplegables(GeneralValueObject gVO, String[] params){
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
        sql = "SELECT E_DES." + campos.getString("SQL.E_DES.codigo") +
                    ",E_DES." + campos.getString("SQL.E_DES.nombre") +
                    ",E_DES_VAL." + campos.getString("SQL.E_DES_VAL.codigoValor") +
                    ",E_DES_VAL." + campos.getString("SQL.E_DES_VAL.nombreValor") +
                    ",E_DES_VAL.DES_VAL_ESTADO" +
              " FROM E_DES_VAL, E_DES WHERE E_DES." + campos.getString("SQL.E_DES.codigo") + " = E_DES_VAL." +
              campos.getString("SQL.E_DES_VAL.campoValor") + " AND E_DES_VAL." + campos.getString("SQL.E_DES.codigo")+"='" +
              campo + "' ";
        String[] orden = {campos.getString("SQL.E_DES_VAL.codigoValor"),"1"};
        sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject temp = new GeneralValueObject();
          temp.setAtributo("codigoCampo",rs.getString(1));
          temp.setAtributo("descripcionCampo",rs.getString(2));
          temp.setAtributo("codigoValor",rs.getString(3));
          temp.setAtributo("descripcionValor",rs.getString(4));
          temp.setAtributo("estadoValor",rs.getString(5));
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

    public Vector altaValorCampoDesplegable(GeneralValueObject gVO, String[] params){
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
        sql = "INSERT INTO E_DES_VAL ("+
         campos.getString("SQL.E_DES_VAL.campoValor")+","+
         campos.getString("SQL.E_DES_VAL.codigoValor")+","+
         campos.getString("SQL.E_DES_VAL.nombreValor")+
         ") VALUES ('" +
         gVO.getAtributo("campo") +"','" +
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
      lista = getListaValoresCampoDesplegables(gVO, params);
      return lista;
     }

    public Vector modificarValorCampoDesplegable(GeneralValueObject gVO, String[] params){
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      String sql = "";
      try{
        //m_Log.debug("A por el OAD");
        abd = new AdaptadorSQLBD(params);
        //m_Log.debug("A por la conexion");
        conexion = abd.getConnection();
        sql = "UPDATE E_DES_VAL SET " +
          campos.getString("SQL.E_DES_VAL.codigoValor")+"='"+gVO.getAtributo("codigo")+"',"+
          campos.getString("SQL.E_DES_VAL.nombreValor")+"='"+gVO.getAtributo("descripcion")+"'"+
          " WHERE " +
          campos.getString("SQL.E_DES_VAL.codigoValor")+"='"+gVO.getAtributo("codigoAntiguo")+"' AND " +
          campos.getString("SQL.E_DES_VAL.campoValor")+"='"+gVO.getAtributo("campo")+"'";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        int res = stmt.executeUpdate(sql);
        //m_Log.debug("las filas afectadas en el update son : " + res);
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
      lista = getListaValoresCampoDesplegables(gVO, params);
      return lista;
    }
    
    public Vector recuperarValorCampoDesplegable(GeneralValueObject gVO, String[] params){
         AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      String sql = "";
      try{
   
        abd = new AdaptadorSQLBD(params);
      
        conexion = abd.getConnection();
        sql = "UPDATE E_DES_VAL SET " +
          "E_DES_VAL.DES_VAL_ESTADO='A'"+
          " WHERE " +
          campos.getString("SQL.E_DES_VAL.codigoValor")+"='"+gVO.getAtributo("codigo")+"' AND " +
          campos.getString("SQL.E_DES_VAL.campoValor")+"='"+gVO.getAtributo("campo")+"' AND " +
          "E_DES_VAL.DES_VAL_ESTADO='B'";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        stmt.executeUpdate(sql);
        
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
      lista = getListaValoresCampoDesplegables(gVO, params);
      return lista;
    }
    
    public Vector eliminarValorCampoDesplegable(GeneralValueObject gVO, String[] params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt;
        ResultSet rs = null;
        String sql;
        String sql2;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            stmt = conexion.createStatement();
            sql="SELECT COUNT(*) AS NUM FROM ( SELECT TDE_VALOR FROM E_TDE WHERE TDE_VALOR='"+gVO.getAtributo("codigo")+"' UNION ALL "+
            "SELECT TDET_VALOR FROM E_TDET WHERE E_TDET.TDET_VALOR='"+gVO.getAtributo("codigo")+"')";
           
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            rs = stmt.executeQuery(sql);
            int num = -1;
            while(rs.next()){
                 num = rs.getInt("NUM");
            }

            if(num>=1){
                sql2="UPDATE E_DES_VAL SET DES_VAL_ESTADO='B' "
                +" WHERE " +
                campos.getString("SQL.E_DES_VAL.codigoValor")+"='"+gVO.getAtributo("codigo")+"' AND " +
                campos.getString("SQL.E_DES_VAL.campoValor")+"='"+gVO.getAtributo("campo")+"'";
                
            }else{
               sql2 = "DELETE FROM E_DES_VAL "
                      +"WHERE " +
                    campos.getString("SQL.E_DES_VAL.codigoValor") + "='" + gVO.getAtributo("codigo") 
                    + "' AND " +
                    campos.getString("SQL.E_DES_VAL.campoValor") + "='" + gVO.getAtributo("campo") + "'";
              
            }
            if (m_Log.isDebugEnabled()) m_Log.debug(sql2);
            stmt.executeUpdate(sql2);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try {
                abd.devolverConexion(conexion);
            } catch (Exception e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        Vector lista;
        lista = getListaValoresCampoDesplegables(gVO, params);
        return lista;
    }

    /**
     * Almacena en BBDD un conjunto de campos desplegables, si no están almacenados ya.
     * @param   desplegables    Vector con la descripción de los campos deplegables. Cada 4 posiciones representa
     * un elemento de un campo desplegable, siendo la posicion i el codigo del campo desplegable, i + 1 el nombre del
     * campo desplegable, i + 2 el codigo del valor del campo desplegable e i + 3 la descrición del elemento del campo
     * desplegable.
     * @param   params          Parametros de conexion a la BBDD.
     * @throws SQLException Si hay algún problema en el en las llamadas a la BBDD.
     */
    public void anhadirConjuntoDesplegables(Vector desplegables, String[] params)
    throws SQLException {

        m_Log.debug("CamposDesplegablesDAO --> Inicio anhadirConjuntoDesplegables");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Formatear el vector de Entrada a nuestra conveniencia.
        Vector desplegableGVO = new Vector();
        String codigoDesplegable = "";
        Vector codOpciones = new Vector();
        Vector desOpciones = new Vector();
        GeneralValueObject gVO = new GeneralValueObject();
        for (int i = 0; i < desplegables.size(); i = i + 4) {
            if (!codigoDesplegable.equals(desplegables.elementAt(i))) {
                if (i != 0) {
                    gVO.setAtributo("codOpciones", codOpciones);
                    gVO.setAtributo("desOpciones", desOpciones);
                    codOpciones = new Vector();
                    desOpciones = new Vector();
                    desplegableGVO.add(gVO);
                    gVO = new GeneralValueObject();
                }
                codigoDesplegable = (String) desplegables.elementAt(i);
                gVO.setAtributo("codDesplegable", codigoDesplegable);
                gVO.setAtributo("nomDesplegable", desplegables.elementAt(i + 1));
            }
            codOpciones.add(desplegables.elementAt(i + 2));
            desOpciones.add(desplegables.elementAt(i + 3));
        }
        gVO.setAtributo("codOpciones", codOpciones);
        gVO.setAtributo("desOpciones", desOpciones);
        desplegableGVO.add(gVO);

        // Insertar los valores de los campos desplegables.
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            Iterator itCampos = desplegableGVO.iterator();
            while (itCampos.hasNext()) {
                GeneralValueObject campoGVO = (GeneralValueObject) itCampos.next();
                String codDesplegable = (String) campoGVO.getAtributo("codDesplegable");
                String nomDesplegable = (String) campoGVO.getAtributo("nomDesplegable");
                Vector codigosOpciones = (Vector) campoGVO.getAtributo("codOpciones");
                Vector nombresOpciones = (Vector) campoGVO.getAtributo("desOpciones");

                // Comprobar si existe un campo desplegable con ese codigo.
                StringBuffer queryBuffer = new StringBuffer();
                queryBuffer.append("SELECT * FROM E_DES WHERE ");
                queryBuffer.append(campos.getString("SQL.E_DES.codigo")).append(" = ?");
                String queryString = new String(queryBuffer);
                m_Log.debug("CONSULTA SQL (Comprobar existencia de campo desplegable):");
                m_Log.debug(queryString);

                ps = conexion.prepareStatement(queryString);
                ps.setString(1, codDesplegable);

                rs = ps.executeQuery();

                if (!rs.next()) {
                    // No existe ese campo desplegable. Por lo tanto lo creamos.
                    queryString = "INSERT INTO E_DES VALUES (?, ?)";
                    m_Log.debug("CONSULTA SQL (Insertar campo desplegable):");
                    m_Log.debug(queryString);
                    ps = conexion.prepareStatement(queryString);
                    int i = 1;
                    ps.setString(i++, codDesplegable);
                    ps.setString(i, nomDesplegable);
                    int insertedRows = ps.executeUpdate();
                    if (insertedRows != 1) throw new BDException("ERROR al insertar un campo desplegable");

                    // Ahora insertamos los posibles valores para ese campo.
                    for (int j = 0; j < codigosOpciones.size(); j++) {
                        queryString = "INSERT INTO E_DES_VAL VALUES (?, ?, ?)";
                        m_Log.debug("CONSULTA SQL (Insertar valore de campo desplegable):");
                        m_Log.debug(queryString);
                        ps = conexion.prepareStatement(queryString);
                        i = 1;
                        ps.setString(i++, codDesplegable);
                        ps.setString(i++, (String)codigosOpciones.get(j));
                        ps.setString(i, (String)nombresOpciones.get(j));
                        insertedRows = ps.executeUpdate();
                        if (insertedRows != 1) throw new BDException("ERROR al insertar el valor de " +
                                "un campo desplegable");
                    }

                }
                rs.close();
                ps.close();
            }
            abd.devolverConexion(conexion);
        } catch (BDException bde) {
            bde.printStackTrace();
            // Cerrar la conexion.
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            try {
                abd.devolverConexion(conexion);
            } catch (BDException e1) {
                e1.printStackTrace();
                throw new SQLException(e1.getMensaje());
            }
            throw new SQLException(bde.getMensaje());
        }

        m_Log.debug("CamposDesplegablesDAO --> Fin anhadirConjuntoDesplegables");

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
  
  
  
  /**
     * Comprueba si un campo desplegable está asignado como campo suplementario a nivel de procedimiento
     * @param codCampo: Código del campo desplegable externo
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean estaCampoAsignadoCampoSuplementarioProcedimiento(String codCampo,Connection con) throws SQLException {
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA,E_DES " + 
                         "WHERE PCA_ACTIVO='SI' AND PCA_DESPLEGABLE=? AND E_DES.DES_COD=PCA_DESPLEGABLE";
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
     * Comprueba si un campo desplegable está asignado como campo suplementario a nivel de trámite
     * @param codCampo: Código del campo desplegable externo
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean estaCampoAsignadoCampoSuplementarioTramite(String codCampo,Connection con) throws SQLException{
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA, E_DES " + 
                         "WHERE TCA_ACTIVO='SI' AND TCA_DESPLEGABLE=? AND TCA_DESPLEGABLE=E_DES.DES_COD";
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
  
  

}