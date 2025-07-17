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
 * <p>Descripción: PaisesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class PaisesDAO  {
  private static HashMap paisesDescripciones = null;
  private static PaisesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(PaisesDAO.class.getName());

  protected PaisesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static PaisesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (PaisesDAO.class) {
        if (instance == null) {
          instance = new PaisesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarPais(String cod, String[] params){
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
      
      sql = " SELECT * FROM T_AUT WHERE " + 
            campos.getString("SQL.T_AUT.idPais")+"="+cod;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      }
      rs.close();
      stmt.close();
      
      sql = " SELECT * FROM T_PRV WHERE " + 
            campos.getString("SQL.T_PRV.idPais")+"="+cod;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont1++;
      }
      rs.close();
      stmt.close();
      
      if(cont == 0 && cont1 == 0) {
        sql = "DELETE FROM T_PAI WHERE " + 
              campos.getString("SQL.T_PAI.idPais")+"="+cod;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        
        sql = "DELETE FROM T_MUN WHERE " + 
              campos.getString("SQL.T_MUN.idPais")+"=108 AND "+
        			campos.getString("SQL.T_MUN.idProvincia")+"=66 AND "+
        			campos.getString("SQL.T_MUN.idMunicipio")+"="+cod;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
      }
      
      
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    if(cont == 0 && cont1 == 0) {
      lista = getListaPaises(params);
    } else {
    	GeneralValueObject gVO = new GeneralValueObject();
    	gVO.setAtributo("puedeEliminar","no");
    	lista.addElement(gVO);
    }
    return lista;
  }

  public Vector getListaPaises(String[] params){
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
      // Creamos la select con los parametros adecuados.
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI WHERE "+
        campos.getString("SQL.T_PAI.idPais")+"=108";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_PAI.idPais")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_PAI.nombre")));
        gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PAI.nombreLargo")));
        gVO.setAtributo("digitoControl",rs.getString(campos.getString("SQL.T_PAI.digitoControl")));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PAI WHERE "+
        campos.getString("SQL.T_PAI.idPais")+"<>108";
      String[] orden = {campos.getString("SQL.T_PAI.nombre"),"1"};
      sql += abd.orderUnion(orden);
      stmt = conexion.createStatement();
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_PAI.idPais")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_PAI.nombre")));
        gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PAI.nombreLargo")));
        gVO.setAtributo("digitoControl",rs.getString(campos.getString("SQL.T_PAI.digitoControl")));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en PaisesDAO.getListaPaises");
        }
    }
    return resultado;
  }

  public Vector modificarPais(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE T_PAI SET " + 
        campos.getString("SQL.T_PAI.idPais")+"="+gVO.getAtributo("codigo")+","+ 
        campos.getString("SQL.T_PAI.nombre")+"='"+gVO.getAtributo("descripcion")+"',"+
        campos.getString("SQL.T_PAI.nombreLargo")+"='"+gVO.getAtributo("nombreLargo")+"',"+ 
        campos.getString("SQL.T_PAI.digitoControl")+"='"+gVO.getAtributo("digitoControl")+"'"+
        " WHERE " + 
        campos.getString("SQL.T_PAI.idPais")+"="+gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      stmt.close();
      sql = "UPDATE T_MUN SET " +
        campos.getString("SQL.T_MUN.idPais")+"=108,"+
        campos.getString("SQL.T_MUN.idProvincia")+"=66,"+
        campos.getString("SQL.T_MUN.idMunicipio")+"="+gVO.getAtributo("codigo")+","+
        campos.getString("SQL.T_MUN.nombre")+"='"+gVO.getAtributo("descripcion")+"',"+
        campos.getString("SQL.T_MUN.nombreLargo")+"='"+gVO.getAtributo("nombreLargo")+"',"+
        campos.getString("SQL.T_MUN.digitoControl")+"='"+gVO.getAtributo("digitoControl")+"'"+
        " WHERE " +
        campos.getString("SQL.T_MUN.idPais")+"=108 AND "+
        campos.getString("SQL.T_MUN.idProvincia")+"=66 AND "+
        campos.getString("SQL.T_MUN.idMunicipio")+"="+gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res1 = stmt.executeUpdate(sql);
      stmt.close();
      
      //m_Log.debug("las filas afectadas en el update son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaPaises(params);
    return lista;
  }

  public Vector altaPais(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO T_PAI("+
       campos.getString("SQL.T_PAI.idPais")+","+ 
       campos.getString("SQL.T_PAI.nombre")+","+
       campos.getString("SQL.T_PAI.nombreLargo")+","+ 
       campos.getString("SQL.T_PAI.digitoControl")+
       ") VALUES (" + 
       gVO.getAtributo("codigo") +",'" + 
       gVO.getAtributo("descripcion") + "','"+
       gVO.getAtributo("nombreLargo")+"','"+ 
       gVO.getAtributo("digitoControl")+"')";
      
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      
      sql = "INSERT INTO T_MUN("+
        campos.getString("SQL.T_MUN.idPais")+","+
        campos.getString("SQL.T_MUN.idProvincia")+","+
        campos.getString("SQL.T_MUN.idMunicipio")+","+
        campos.getString("SQL.T_MUN.partidoJudicial")+","+
        campos.getString("SQL.T_MUN.comarca")+","+
        campos.getString("SQL.T_MUN.nombre")+","+
        campos.getString("SQL.T_MUN.nombreLargo")+","+
        campos.getString("SQL.T_MUN.digitoControl")+","+
        campos.getString("SQL.T_MUN.superficie")+","+
        campos.getString("SQL.T_MUN.altitud")+","+
        campos.getString("SQL.T_MUN.kmtsCapital")+","+
        campos.getString("SQL.T_MUN.latitudNorte")+","+
        campos.getString("SQL.T_MUN.latitudSur")+","+
        campos.getString("SQL.T_MUN.longitudEste")+","+
        campos.getString("SQL.T_MUN.longitudOeste")+","+
        campos.getString("SQL.T_MUN.situacion")+
        ") VALUES (108,66,"+
        gVO.getAtributo("codigo")+",null,null,'"+
        gVO.getAtributo("descripcion")+"','"+
        gVO.getAtributo("nombreLargo")+"','"+
        gVO.getAtributo("digitoControl")+"',null,null,null,null,null,null,null,null)";
      
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      
      stmt.close();      
      
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaPaises(params);
    return lista;
   }

   public HashMap getDescripcionesPaises(String[] params){

       if (paisesDescripciones==null){
            AdaptadorSQLBD abd = null;
            Connection conexion = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "";
            HashMap paises = new HashMap();
            try{
                abd = new AdaptadorSQLBD(params);
                conexion = abd.getConnection();
                stmt = conexion.createStatement();
                sql = "SELECT *"+
                      " FROM "+ GlobalNames.ESQUEMA_GENERICO +"T_PAI";

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    paises.put(rs.getString(campos.getString("SQL.T_PAI.idPais")),
                               rs.getString(campos.getString("SQL.T_PAI.nombre")));
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
                paisesDescripciones = paises;
            }
       }
       
       return paisesDescripciones;
  }

    public GeneralValueObject getPaisByDescription(String descripcionPais, String[] params) throws BDException, SQLException {

        String sqlQuery = "SELECT PAI_COD, PAI_NOM, PAI_NOL, PAI_DCO FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PAI " +
                "WHERE (PAI_NOM = ? OR PAI_NOL = ?)";

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> NOMBRE DEL PAIS: " + descripcionPais);
            ps.setString(1, descripcionPais);
            ps.setString(2, descripcionPais);

            rs = ps.executeQuery();
            if (rs.next()) {
                GeneralValueObject paisRecuperado = new GeneralValueObject();
                paisRecuperado.setAtributo("codigoPais", rs.getString(1));
                paisRecuperado.setAtributo("nombrePais", rs.getString(2));
                paisRecuperado.setAtributo("nomLargoPais", rs.getString(3));
                paisRecuperado.setAtributo("codigoCtrlPais", rs.getString(4));
                return paisRecuperado;
            }
            else throw new SQLException("NO SE HA ENCONTRADO NINGUN PAIS CON ESA DESCRIPCION");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
    }

    public GeneralValueObject getPaisByCodigo(int codigoPais, String[] params) throws BDException, SQLException {

        String sqlQuery = "SELECT PAI_NOM, PAI_NOL, PAI_DCO FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PAI " +
                "WHERE (PAI_COD = ?)";

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> CODIGO DEL PAIS: " + codigoPais);
            ps.setInt(1, codigoPais);

            rs = ps.executeQuery();
            if (rs.next()) {
                GeneralValueObject paisRecuperado = new GeneralValueObject();
                paisRecuperado.setAtributo("codigoPais", Integer.toString(codigoPais));
                paisRecuperado.setAtributo("nombrePais", rs.getString(1));
                paisRecuperado.setAtributo("nomLargoPais", rs.getString(2));
                paisRecuperado.setAtributo("codigoCtrlPais", rs.getString(3));
                return paisRecuperado;
            }
            else throw new SQLException("NO SE HA ENCONTRADO NINGUN PAIS CON ESA DESCRIPCION");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
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