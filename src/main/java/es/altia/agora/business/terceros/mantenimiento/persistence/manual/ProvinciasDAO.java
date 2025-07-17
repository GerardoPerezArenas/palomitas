// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;
import java.util.HashMap;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: ProvinciasDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ProvinciasDAO  {

  private static HashMap provinciasDescripciones = null;
  private static ProvinciasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(ProvinciasDAO.class.getName());

  protected ProvinciasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ProvinciasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (ProvinciasDAO.class) {
        if (instance == null) {
          instance = new ProvinciasDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarProvincia(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int cont = 0;
    int cont1 = 0;
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_MUN WHERE " + campos.getString("SQL.T_MUN.idPais")+"="+
            gVO.getAtributo("codPais")+ " AND " + 
            campos.getString("SQL.T_MUN.idProvincia")+"="+ gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        cont++;	
      }
      rs.close();
      
      sql = "SELECT * FROM T_PJU WHERE " + campos.getString("SQL.T_PJU.idPais")+"="+
            gVO.getAtributo("codPais")+ " AND " + 
            campos.getString("SQL.T_PJU.idProvincia")+"="+ gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        cont1++;	
      }
      rs.close();
      
      if(cont == 0 && cont1 == 0) {
	      sql = "DELETE FROM T_PRV WHERE " + 
	        campos.getString("SQL.T_PRV.idPais")+"="+gVO.getAtributo("codPais")+" AND "+ 
	        campos.getString("SQL.T_PRV.idProvincia")+"="+gVO.getAtributo("codigo");
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
      }
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    if(cont == 0 && cont1 == 0) {
      lista = getListaProvincias(gVO,params);
    } else {
    	gVO.setAtributo("puedeEliminar","no");
    	lista.addElement(gVO);
    }
    return lista;
  }

  public Vector getListaProvincias(GeneralValueObject gVO1,String[] params){
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
//      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      String codPais = (String)gVO1.getAtributo("codPais");
      String codProvincia = (String)gVO1.getAtributo("codProvincia");
      if(codProvincia!=null){
        // Creamos la select con los parametros adecuados.
        sql = "SELECT T_PRV.*,T_AUT.* "+
              "FROM "+GlobalNames.ESQUEMA_GENERICO +"T_PRV T_PRV "+
                " left join "+GlobalNames.ESQUEMA_GENERICO +"T_AUT T_AUT on ("+campos.getString("SQL.T_PRV.idPais")+"="+campos.getString("SQL.T_AUT.idPais")+" AND "+campos.getString("SQL.T_PRV.autonomia")+"="+campos.getString("SQL.T_AUT.idAutonomia")+" )"
              +" WHERE "+ campos.getString("SQL.T_PRV.idPais")+"="+codPais+       
              " AND "+campos.getString("SQL.T_PRV.idProvincia")+"="+codProvincia;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_PRV.idPais")));
          gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_PRV.idProvincia")));
          gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_PRV.nombre")));
          gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PRV.nombreLargo")));
          gVO.setAtributo("codAutonomia",rs.getString(campos.getString("SQL.T_AUT.idAutonomia")));
          gVO.setAtributo("descAutonomia",rs.getString(campos.getString("SQL.T_AUT.nombre")));
          resultado.add(gVO);
        }
        rs.close();
        sql = "SELECT T_PRV.*,T_AUT.* "+
              "FROM "+GlobalNames.ESQUEMA_GENERICO +"T_PRV T_PRV" +
                 " left join "+GlobalNames.ESQUEMA_GENERICO +"T_AUT T_AUT on ("+campos.getString("SQL.T_PRV.idPais")+"="+campos.getString("SQL.T_AUT.idPais")+" AND "+campos.getString("SQL.T_PRV.autonomia")+"="+campos.getString("SQL.T_AUT.idAutonomia")+" )"
              +" WHERE "+ campos.getString("SQL.T_PRV.idPais")+"="+codPais+             
             " AND "+campos.getString("SQL.T_PRV.idProvincia")+"<>"+codProvincia +
             " AND " + campos.getString("SQL.T_PRV.nombre")+" NOT LIKE 'DESCONOCIDA'" +
             " AND " + campos.getString("SQL.T_PRV.nombre")+" NOT LIKE 'EXTRANJERO'";
        
        String[] orden = {campos.getString("SQL.T_PRV.nombre"),"1"};
        sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_PRV.idPais")));
          gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_PRV.idProvincia")));
          gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_PRV.nombre")));
          gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PRV.nombreLargo")));
          gVO.setAtributo("codAutonomia",rs.getString(campos.getString("SQL.T_AUT.idAutonomia")));
          gVO.setAtributo("descAutonomia",rs.getString(campos.getString("SQL.T_AUT.nombre")));
          resultado.add(gVO);
        }
        rs.close();
        sql = "SELECT T_PRV.*,T_AUT.* "+
              "FROM "+GlobalNames.ESQUEMA_GENERICO +"T_PRV T_PRV" +
                 " left join "+GlobalNames.ESQUEMA_GENERICO +"T_AUT T_AUT on ("+campos.getString("SQL.T_PRV.idPais")+"="+campos.getString("SQL.T_AUT.idPais")+" AND "+campos.getString("SQL.T_PRV.autonomia")+"="+campos.getString("SQL.T_AUT.idAutonomia")+" )"
              +" WHERE "+ campos.getString("SQL.T_PRV.idPais")+"="+codPais+
              " AND (" +  campos.getString("SQL.T_PRV.nombre")+" LIKE 'DESCONOCIDA'" +
              " OR " + campos.getString("SQL.T_PRV.nombre")+" LIKE 'EXTRANJERO' )" ;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_PRV.idPais")));
          gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_PRV.idProvincia")));
          gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_PRV.nombre")));
          gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PRV.nombreLargo")));
          gVO.setAtributo("codAutonomia",rs.getString(campos.getString("SQL.T_AUT.idAutonomia")));
          gVO.setAtributo("descAutonomia",rs.getString(campos.getString("SQL.T_AUT.nombre")));
          resultado.add(gVO);
        }
        rs.close();
        stmt.close();
      }else{
        sql = "SELECT T_PRV.*,T_AUT.* "+
              "FROM "+GlobalNames.ESQUEMA_GENERICO +"T_PRV T_PRV"+
              " left join "+GlobalNames.ESQUEMA_GENERICO +"T_AUT T_AUT on ("+campos.getString("SQL.T_PRV.idPais")+"="+campos.getString("SQL.T_AUT.idPais")+" AND "+campos.getString("SQL.T_PRV.autonomia")+"="+campos.getString("SQL.T_AUT.idAutonomia")+" )"
              +" WHERE "+ campos.getString("SQL.T_PRV.idPais")+"="+codPais+
               " AND " + campos.getString("SQL.T_PRV.nombre")+" NOT LIKE 'DESCONOCIDA'" +
              " AND " + campos.getString("SQL.T_PRV.nombre")+" NOT LIKE 'EXTRANJERO'";

        String[] orden = {campos.getString("SQL.T_PRV.nombre"),"1"};
        sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_PRV.idPais")));
          gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_PRV.idProvincia")));
          gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_PRV.nombre")));
          gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PRV.nombreLargo")));
          gVO.setAtributo("codAutonomia",rs.getString(campos.getString("SQL.T_AUT.idAutonomia")));
          gVO.setAtributo("descAutonomia",rs.getString(campos.getString("SQL.T_AUT.nombre")));
          resultado.add(gVO);
        }
        rs.close();
        sql = "SELECT T_PRV.*,T_AUT.* "+
              "FROM "+GlobalNames.ESQUEMA_GENERICO +"T_PRV T_PRV" 
              + " left join "+GlobalNames.ESQUEMA_GENERICO +"T_AUT T_AUT on ("+campos.getString("SQL.T_PRV.idPais")+"="+campos.getString("SQL.T_AUT.idPais")+" AND "+campos.getString("SQL.T_PRV.autonomia")+"="+campos.getString("SQL.T_AUT.idAutonomia")+" )"
              +" WHERE "+ campos.getString("SQL.T_PRV.idPais")+"="+codPais+ 
              " AND (" +  campos.getString("SQL.T_PRV.nombre")+" LIKE 'DESCONOCIDA'" +
              " OR " + campos.getString("SQL.T_PRV.nombre")+" LIKE 'EXTRANJERO' )";        
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_PRV.idPais")));
          gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_PRV.idProvincia")));
          gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_PRV.nombre")));
          gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PRV.nombreLargo")));
          gVO.setAtributo("codAutonomia",rs.getString(campos.getString("SQL.T_AUT.idAutonomia")));
          gVO.setAtributo("descAutonomia",rs.getString(campos.getString("SQL.T_AUT.nombre")));
          resultado.add(gVO);
        }
        rs.close();
        stmt.close();
      }
    }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try {
//      commitTransaction(abd,conexion);
            abd.devolverConexion(conexion);
        }catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ProvinciasDAO.getListaProvincias");
        }
    }
    return resultado;
  }

  public Vector modificarProvincia(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "UPDATE T_PRV SET " + 
        campos.getString("SQL.T_PRV.idProvincia")+"="+gVO.getAtributo("codigo")+","+ 
        campos.getString("SQL.T_PRV.nombre")+"='"+gVO.getAtributo("descripcion")+"',"+
        campos.getString("SQL.T_PRV.nombreLargo")+"='"+gVO.getAtributo("nombreLargo")+"',"+ 
        campos.getString("SQL.T_PRV.autonomia")+"='"+gVO.getAtributo("codAutonomia")+"'"+
        " WHERE " + 
        campos.getString("SQL.T_PRV.idPais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_PRV.idProvincia")+"="+gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el update son : " + res);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaProvincias(gVO,params);
    return lista;
  }

  public Vector altaProvincia(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "INSERT INTO T_PRV("+
       campos.getString("SQL.T_PRV.idPais")+","+
       campos.getString("SQL.T_PRV.idProvincia")+","+ 
       campos.getString("SQL.T_PRV.nombre")+","+
       campos.getString("SQL.T_PRV.nombreLargo")+","+ 
       campos.getString("SQL.T_PRV.autonomia")+
       ") VALUES (" + 
       gVO.getAtributo("codPais") +"," + 
       gVO.getAtributo("codigo") +",'" + 
       gVO.getAtributo("descripcion") + "','"+
       gVO.getAtributo("nombreLargo")+"','"+ 
       gVO.getAtributo("codAutonomia")+"')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaProvincias(gVO,params);
    return lista;
   }

   public HashMap getDescripcionesProvincias(String[] params){

       if (provinciasDescripciones==null){
            AdaptadorSQLBD abd = null;
            Connection conexion = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "";
            HashMap provincias = new HashMap();
            try{
                abd = new AdaptadorSQLBD(params);
                conexion = abd.getConnection();
                stmt = conexion.createStatement();
                sql = "SELECT *"+
                      " FROM "+GlobalNames.ESQUEMA_GENERICO +"T_PRV";

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    provincias.put(rs.getString(campos.getString("SQL.T_PRV.idPais"))+rs.getString(campos.getString("SQL.T_PRV.idProvincia")),
                                   rs.getString(campos.getString("SQL.T_PRV.nombre")));
                }

            }catch (Exception e){
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally{
                try {
                    abd.devolverConexion(conexion);
                }catch (BDException e) {
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ProvinciasDAO.getListaProvincias");
                }
                provinciasDescripciones = provincias;
            }
       }
        return provinciasDescripciones;
  }

    public GeneralValueObject getProvinciaByPaisAndDesc(int codPais, String descProvincia, String params[])
            throws BDException, SQLException {

        String sqlQuery = "SELECT PRV_COD, PRV_AUT, PRV_NOM, PRV_NOL FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PRV " +
                "WHERE PRV_PAI = ? AND (PRV_NOM = ? OR PRV_NOL = ?)";

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> CODIGO DE PAIS: " + codPais);
            m_Log.debug("PARAMETRO 2 --> NOMBRE DE LA PROVINCIA: " + descProvincia);
            ps.setInt(1, codPais);
            ps.setString(2, descProvincia);
            ps.setString(3, descProvincia);

            rs = ps.executeQuery();
            if (rs.next()) {
                GeneralValueObject provinciaRecuperado = new GeneralValueObject();
                provinciaRecuperado.setAtributo("paisProvincia", Integer.toString(codPais));
                provinciaRecuperado.setAtributo("codigoProvincia", rs.getString(1));
                provinciaRecuperado.setAtributo("autonomiaProvincia", rs.getString(2));
                provinciaRecuperado.setAtributo("nombreProvincia", rs.getString(3));
                provinciaRecuperado.setAtributo("nomLargoProvincia", rs.getString(4));
                return provinciaRecuperado;
            }
            else throw new SQLException("NO SE HA ENCONTRADO NINGUNA PROVINCIA PARA ESE PAIS CON ESA DESCRIPCION");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
    }

    public GeneralValueObject getProvinciaByPaisAndCodigo(int codPais, int codProvincia, String params[])
            throws BDException, SQLException {

        String sqlQuery = "SELECT PRV_AUT, PRV_NOM, PRV_NOL FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PRV " +
                "WHERE PRV_PAI = ? AND PRV_COD = ?";

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> CODIGO DE PAIS: " + codPais);
            m_Log.debug("PARAMETRO 2 --> NOMBRE DE LA PROVINCIA: " + codProvincia);
            ps.setInt(1, codPais);
            ps.setInt(2, codProvincia);

            rs = ps.executeQuery();
            if (rs.next()) {
                GeneralValueObject provinciaRecuperado = new GeneralValueObject();
                provinciaRecuperado.setAtributo("paisProvincia", Integer.toString(codPais));
                provinciaRecuperado.setAtributo("codigoProvincia", Integer.toString(codProvincia));
                provinciaRecuperado.setAtributo("autonomiaProvincia", rs.getString(1));
                provinciaRecuperado.setAtributo("nombreProvincia", rs.getString(2));
                provinciaRecuperado.setAtributo("nomLargoProvincia", rs.getString(3));
                return provinciaRecuperado;
            }
            else throw new SQLException("NO SE HA ENCONTRADO NINGUNA PROVINCIA PARA ESE PAIS CON ESA DESCRIPCION");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
    }

    
    /**
     * Devuelve la descripción de una determinada provincia
     * @param codPais: Código del país
     * @param codProvincia: Código de la provincia
     * @param con: Conexión a la BBDD
     * @return String con el nombre o String vacío sino se ha podido recuperar    
     */
    public String getDescripcionProvincia(int codPais, int codProvincia,Connection con){

        PreparedStatement ps = null;
        ResultSet rs = null;
        String salida = "";
        
        try {
            
            String sql = "SELECT PRV_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PRV " +
                     "WHERE PRV_PAI = ? AND PRV_COD = ?";
            
            ps = con.prepareStatement(sql);
            
            int i=1;
            ps.setInt(i++, codPais);
            ps.setInt(i++, codProvincia);

            rs = ps.executeQuery();
            while(rs.next()) {
                salida = rs.getString("PRV_NOM");
            }
            

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            try{
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }            
        }
        
        return salida;
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
  
}
