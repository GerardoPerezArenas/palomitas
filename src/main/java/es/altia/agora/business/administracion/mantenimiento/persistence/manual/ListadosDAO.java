// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO;
import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: OrganizacionesesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ListadosDAO  {
  private static ListadosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(ListadosDAO.class.getName());

  protected ListadosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ListadosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (ListadosDAO.class) {
        if (instance == null) {
          instance = new ListadosDAO();
        }
      }
    }
    return instance;
  }


   public Vector getListadosParametrizables(String[] params){
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
      sql = "SELECT * FROM A_LIST";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString("LIST_COD"));
        gVO.setAtributo("descripcion",rs.getString("LIST_NOM"));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
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
   
 public Vector getListaCamposListados(CamposListadosParametrizablesVO gVO,String[] params){
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
      sql = "SELECT * FROM A_CAMPLIST WHERE CAMPLIST_CODLIST="+gVO.getCodCampo();
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        CamposListadosParametrizablesVO gVOCampos = new CamposListadosParametrizablesVO();
        gVOCampos.setCodCampo(Integer.parseInt(rs.getString("CAMPLIST_COD")));
        gVOCampos.setNomCampo(rs.getString("CAMPLIST_NOM"));
        int act=Integer.parseInt(rs.getString("CAMPLIST_ACTIVO"));
        String activo="";
        if (1==act){
            activo="SI";
        }else{
            activo="NO";
        }
        gVOCampos.setActCampo(activo);
        gVOCampos.setTamanoCampo(Integer.parseInt(rs.getString("CAMPLIST_TAMANO")));
        resultado.add(gVOCampos);
      }
      rs.close();
      stmt.close();
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
 public int grabarListaCamposListados(CamposListadosParametrizablesVO gVO,String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
     PreparedStatement ps,ps2 =null;
    String sql,sql1;
    int act=0;
    int codListado;
    int res =0;
    codListado=gVO.getCodListado();
    m_Log.debug("  codListado  "+codListado);
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();

   for (int i=0;i<gVO.getvCodCampo().size();i++){
       if (gVO.getvActCampo().elementAt(i).equals("SI")){
            act=1;
        }else{
            act=0;
        }

       sql1="UPDATE A_CAMPLIST SET CAMPLIST_TAMANO="+gVO.getvTamanoCampo().elementAt(i)+", CAMPLIST_ACTIVO="+act+
           " WHERE CAMPLIST_COD="+gVO.getvCodCampo().elementAt(i) + " AND CAMPLIST_CODLIST=" + gVO.getCodListado();
      
        if(m_Log.isDebugEnabled()) m_Log.debug(sql1);
     ps2 = conexion.prepareStatement(sql1);
      int res1= ps2.executeUpdate();
      m_Log.debug("las filas afectadas en el update son : " + res1);
       
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
return res;
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