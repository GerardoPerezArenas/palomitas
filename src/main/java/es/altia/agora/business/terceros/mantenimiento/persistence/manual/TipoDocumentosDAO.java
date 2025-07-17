// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: TipoDocumentosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TipoDocumentosDAO  {
  private static TipoDocumentosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TipoDocumentosDAO.class.getName());

  protected TipoDocumentosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TipoDocumentosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (TipoDocumentosDAO.class) {
        if (instance == null) {
          instance = new TipoDocumentosDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarTipoDocumento(String codigo, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    int cont = 0;
    int cont1 = 0;
    int cont2 = 0;
    int cont3 = 0;
    int cont4 = 0;
    int cont5 = 0;
    int cont6 = 0;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM P_HAB WHERE " + 
            campos.getString("SQL.P_HAB.tipoDocumento")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      } 
      rs.close();
      
      sql = "SELECT * FROM P_HOP WHERE " + 
            campos.getString("SQL.P_HOP.tipoDocumento")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont1++;
      } 
      rs.close();
      
      sql = "SELECT * FROM P_OPE WHERE " + 
            campos.getString("SQL.P_OPE.tipoDocumento")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont2++;
      } 
      rs.close();
      
      sql = "SELECT * FROM P_PRM WHERE " + 
            campos.getString("SQL.P_PRM.tipoDocumento")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont3++;
      } 
      rs.close();
      
      sql = "SELECT * FROM T_HTE WHERE " + 
            campos.getString("SQL.T_HTE.tipoDocumento")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont4++;
      } 
      rs.close();
      
      sql = "SELECT * FROM T_PRM WHERE " + 
            campos.getString("SQL.T_PRM.tid")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont5++;
      } 
      rs.close();
      
      sql = "SELECT * FROM T_TER WHERE " + 
            campos.getString("SQL.T_TER.tipoDocumento")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont6++;
      } 
      rs.close();
           
      if(cont == 0 && cont1 == 0 && cont2 == 0 && cont3 == 0 && cont4 == 0 && cont5 == 0 && cont6 == 0) {
	      sql = "DELETE FROM T_TID WHERE " +
	        campos.getString("SQL.T_TID.codigo")+"="+codigo;
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
    if(cont == 0 && cont1 == 0 && cont2 == 0 && cont3 == 0 && cont4 == 0 && cont5 == 0 && cont6 == 0) {
      lista = getListaTipoDocumentos(params);
    } else {
    	GeneralValueObject gVO = new GeneralValueObject();
    	gVO.setAtributo("puedeEliminar","no");
    	lista.addElement(gVO);
    }
    return lista;
  }

  public Vector getListaTipoDocumentos(String[] params){
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
      // Creamos la select con los parametros adecuados.
      sql = "SELECT * FROM T_TID ";
      String[] orden = {campos.getString("SQL.T_TID.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codTipoDoc",rs.getString(campos.getString("SQL.T_TID.codigo")));
        gVO.setAtributo("codigoINE",rs.getString(campos.getString("SQL.T_TID.docINE")));
        gVO.setAtributo("codigoAccede",rs.getString(campos.getString("SQL.T_TID.codigoAccede")));
        gVO.setAtributo("duplicado",rs.getString(campos.getString("SQL.T_TID.duplic")));
        gVO.setAtributo("persFJ",rs.getString(campos.getString("SQL.T_TID.fisJur")));
        gVO.setAtributo("normalizado",rs.getString(campos.getString("SQL.T_TID.normal")));
        gVO.setAtributo("descTipoDoc",rs.getString(campos.getString("SQL.T_TID.nombre")));
        gVO.setAtributo("grupo1",rs.getString(campos.getString("SQL.T_TID.grupo1")));
        gVO.setAtributo("tipo1",rs.getString(campos.getString("SQL.T_TID.tipo1")));
        gVO.setAtributo("grupo2",rs.getString(campos.getString("SQL.T_TID.grupo2")));
        gVO.setAtributo("tipo2",rs.getString(campos.getString("SQL.T_TID.tipo2")));
        gVO.setAtributo("grupo3",rs.getString(campos.getString("SQL.T_TID.grupo3")));
        gVO.setAtributo("tipo3",rs.getString(campos.getString("SQL.T_TID.tipo3")));
        gVO.setAtributo("grupo4",rs.getString(campos.getString("SQL.T_TID.grupo4")));
        gVO.setAtributo("tipo4",rs.getString(campos.getString("SQL.T_TID.tipo4")));
        gVO.setAtributo("grupo5",rs.getString(campos.getString("SQL.T_TID.grupo5")));
        gVO.setAtributo("tipo5",rs.getString(campos.getString("SQL.T_TID.tipo5")));
        gVO.setAtributo("longitudMaxima",rs.getString(campos.getString("SQL.T_TID.lonMax")));
        gVO.setAtributo("validacion",rs.getString(campos.getString("SQL.T_TID.valAso")));
        resultado.add(gVO);
      }
    }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try {
            if (rs!=null) rs.close();
            if (stmt!=null) stmt.close();
//          commitTransaction(abd,conexion);
            abd.devolverConexion(conexion);
        }catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TipoDocumentosDAO.getListaTipoDocumentos");
        }
    }
    return resultado;
  }

  public Vector modificarTipoDocumento(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "UPDATE T_TID SET " +
        campos.getString("SQL.T_TID.codigo")+"="+gVO.getAtributo("codTipoDoc")+","+
        campos.getString("SQL.T_TID.codigoAccede")+"='"+gVO.getAtributo("codigoAccede")+"',"+
        campos.getString("SQL.T_TID.docINE")+"="+gVO.getAtributo("codigoINE")+","+
        campos.getString("SQL.T_TID.duplic")+"="+gVO.getAtributo("duplicado")+","+
        campos.getString("SQL.T_TID.fisJur")+"="+gVO.getAtributo("persFJ")+","+
        campos.getString("SQL.T_TID.normal")+"="+gVO.getAtributo("normalizado")+","+
        campos.getString("SQL.T_TID.nombre")+"='"+gVO.getAtributo("descTipoDoc")+"',"+
        campos.getString("SQL.T_TID.grupo1")+"="+gVO.getAtributo("grupo1")+","+
        campos.getString("SQL.T_TID.tipo1")+"='"+gVO.getAtributo("tipo1")+"',"+
        campos.getString("SQL.T_TID.grupo2")+"="+gVO.getAtributo("grupo2")+","+
        campos.getString("SQL.T_TID.tipo2")+"='"+gVO.getAtributo("tipo2")+"',"+
        campos.getString("SQL.T_TID.grupo3")+"="+gVO.getAtributo("grupo3")+","+
        campos.getString("SQL.T_TID.tipo3")+"='"+gVO.getAtributo("tipo3")+"',"+
        campos.getString("SQL.T_TID.grupo4")+"="+gVO.getAtributo("grupo4")+","+
        campos.getString("SQL.T_TID.tipo4")+"='"+gVO.getAtributo("tipo4")+"',"+
        campos.getString("SQL.T_TID.grupo5")+"="+gVO.getAtributo("grupo5")+","+
        campos.getString("SQL.T_TID.tipo5")+"='"+gVO.getAtributo("tipo5")+"',"+
        campos.getString("SQL.T_TID.lonMax")+"="+gVO.getAtributo("longitudMaxima")+","+
        campos.getString("SQL.T_TID.valAso")+"='"+gVO.getAtributo("validacion")+"'"+
        " WHERE " +
        campos.getString("SQL.T_TID.codigo")+"="+gVO.getAtributo("codTipoDocAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el update son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      try {
          if (stmt!=null) stmt.close();
          commitTransaction(abd,conexion);
      }catch (Exception e) {
          e.printStackTrace();
          if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TipoDocumentosDAO.getListaTipoDocumentos");
      }
    }
    Vector lista = new Vector();
    lista = getListaTipoDocumentos(params);
    return lista;
  }

  public Vector altaTipoDocumento(GeneralValueObject gVO, String[] params){
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
      abd.inicioTransaccion(conexion);
      sql = "INSERT INTO T_TID("+
        campos.getString("SQL.T_TID.codigo")+","+
        campos.getString("SQL.T_TID.codigoAccede")+","+
        campos.getString("SQL.T_TID.docINE")+","+
        campos.getString("SQL.T_TID.duplic")+","+
        campos.getString("SQL.T_TID.fisJur")+","+
        campos.getString("SQL.T_TID.normal")+","+
        campos.getString("SQL.T_TID.nombre")+","+
        campos.getString("SQL.T_TID.grupo1")+","+
        campos.getString("SQL.T_TID.tipo1")+","+
        campos.getString("SQL.T_TID.grupo2")+","+
        campos.getString("SQL.T_TID.tipo2")+","+
        campos.getString("SQL.T_TID.grupo3")+","+
        campos.getString("SQL.T_TID.tipo3")+","+
        campos.getString("SQL.T_TID.grupo4")+","+
        campos.getString("SQL.T_TID.tipo4")+","+
        campos.getString("SQL.T_TID.grupo5")+","+
        campos.getString("SQL.T_TID.tipo5")+","+
        campos.getString("SQL.T_TID.lonMax")+","+
        campos.getString("SQL.T_TID.valAso")+
        ") VALUES (" +
        gVO.getAtributo("codTipoDoc")+",'"+
        gVO.getAtributo("codigoAccede")+"',"+
        gVO.getAtributo("codigoINE")+","+
        gVO.getAtributo("duplicado")+","+
        gVO.getAtributo("persFJ")+","+
        gVO.getAtributo("normalizado")+",'"+
        gVO.getAtributo("descTipoDoc")+"',"+
        gVO.getAtributo("grupo1")+",'"+
        gVO.getAtributo("tipo1")+"',"+
        gVO.getAtributo("grupo2")+",'"+
        gVO.getAtributo("tipo2")+"',"+
        gVO.getAtributo("grupo3")+",'"+
        gVO.getAtributo("tipo3")+"',"+
        gVO.getAtributo("grupo4")+",'"+
        gVO.getAtributo("tipo4")+"',"+
        gVO.getAtributo("grupo5")+",'"+
        gVO.getAtributo("tipo5")+"',"+
        gVO.getAtributo("longitudMaxima")+",'"+
        gVO.getAtributo("validacion")+"')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      try {
          if (stmt!=null) stmt.close();
          commitTransaction(abd,conexion);
      }catch (Exception e) {
          e.printStackTrace();
          if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TipoDocumentosDAO.getListaTipoDocumentos");
      }
    }
    Vector lista = new Vector();
    lista = getListaTipoDocumentos(params);
    return lista;
   }
   
  public String getByPrimaryKey(String[] params,String codigo)
  {
    String sql = "";
    AdaptadorSQLBD bd = null;
    Connection con=null;
    ResultSet rs=null;
    Statement state=null;
    String resultado = "";
    try
    {
      bd = new AdaptadorSQLBD(params);
      con = bd.getConnection();
//      bd.inicioTransaccion(con);
      state = con.createStatement();
      sql =  "SELECT "+campos.getString("SQL.T_TID.nombre");
      sql += " FROM T_TID ";
      sql += "WHERE "+campos.getString("SQL.T_TID.codigo") + "=" + codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = state.executeQuery(sql);
      while (rs.next()) {
        resultado =  rs.getString(campos.getString("SQL.T_TID.nombre"));
      }

    }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try {
            if (rs!=null) rs.close();
            if (state!=null) state.close();
//          commitTransaction(abd,conexion);
            bd.devolverConexion(con);
        }catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TipoDocumentosDAO.getByPrimaryKey");
        }
    }
    return resultado;
  }
  
  public Vector getListaTipoDocumentos(String[] params, String parametro, String campo)
    {
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    TransformacionAtributoSelect trans;
    String where;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      trans = new TransformacionAtributoSelect(abd);
      where = trans.construirCondicionWhereConOperadores(parametro,campo);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
//      abd.inicioTransaccion(conexion);
      // Creamos la select con los parametros adecuados.
      sql =  " SELECT "+campos.getString("SQL.T_TID.nombre");
      sql += " FROM T_TID ";
      sql += " WHERE "+ where;
      sql += " ORDER BY "+campos.getString("SQL.T_TID.nombre");

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next())
      {
        resultado.add(rs.getString(campos.getString("SQL.T_TID.nombre")));
      }
    }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try {
            if (rs!=null) rs.close();            
            if (stmt!=null) stmt.close();
//          commitTransaction(abd,conexion);
            abd.devolverConexion(conexion);
        }catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TipoDocumentosDAO.getListaTipoDocumentos");
        }
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
  
  
  
  
  
  /******************** NUEVO ***************************/
  /**
   * Recupera los tipos de documentos que pueden tener los terceros de Flexia
   * @param con: Conexión a la BBDD
   * @return ArrayList<GeneralValueObject>
   */
    public ArrayList<TipoDocumentoVO> getListaTipoDocumentos(Connection con){
      
        ArrayList<TipoDocumentoVO> resultado = new ArrayList<TipoDocumentoVO>();    
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
    
        try{
            // Creamos la select con los parametros adecuados.
            sql = "SELECT TID_COD,TID_DES,TID_PFI FROM T_TID ORDER BY TID_COD";        
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                TipoDocumentoVO tipo = new TipoDocumentoVO();
                tipo.setCodigo(rs.getString("TID_COD"));
                tipo.setDescripcion(rs.getString("TID_DES"));
                tipo.setPersonaFisica(rs.getString("TID_PFI"));                
                resultado.add(tipo);
            }

        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();

            }catch (Exception e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TipoDocumentosDAO.getListaTipoDocumentos");
            }
        }
        return resultado;
    }
  

}