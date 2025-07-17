// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.persistence.manual;

// PAQUETES IMPORTADOS
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
 * <p>Descripción: AutorizacionesInternasDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */

public class AutorizacionesInternasDAO  {
      private static AutorizacionesInternasDAO instance = null;
      protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
      protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
          LogFactory.getLog(AutorizacionesInternasDAO.class.getName());

    protected static String ugo_gru;
    protected static String ugo_usu;
    protected static String ugo_org;
    protected static String ugo_ent;
    protected static String ugo_apl;

    protected static String gru_cod;
    protected static String gru_nom;

     protected static String uae_org;
     protected static String uae_ent;
     protected static String uae_apl;
     protected static String uae_usu;

    protected static String usu_cod;
     protected static String usu_nom;
     protected static String usu_log;

     protected static String rpg_org;
     protected static String rpg_ent;
     protected static String rpg_apl;
     protected static String rpg_tip;
     protected static String rpg_gru;
     protected static String rpg_pro;

     protected static String rpu_org;
     protected static String rpu_ent;
     protected static String rpu_apl;
     protected static String rpu_tip;
     protected static String rpu_usu;
     protected static String rpu_pro;

  protected AutorizacionesInternasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    ugo_gru = m_ConfigTechnical.getString("SQL.A_UGO.grupo");
    ugo_usu = m_ConfigTechnical.getString("SQL.A_UGO.usuario");
    ugo_org = m_ConfigTechnical.getString("SQL.A_UGO.organizacion");
    ugo_ent = m_ConfigTechnical.getString("SQL.A_UGO.entidad");
    ugo_apl = m_ConfigTechnical.getString("SQL.A_UGO.aplicacion");

    gru_cod	= m_ConfigTechnical.getString("SQL.A_GRU.codGrupo");
    gru_nom = m_ConfigTechnical.getString("SQL.A_GRU.nombreGrupo");

    uae_org = m_ConfigTechnical.getString("SQL.A_UAE.organizacion");
    uae_ent = m_ConfigTechnical.getString("SQL.A_UAE.entidad");
    uae_apl = m_ConfigTechnical.getString("SQL.A_UAE.aplicacion");
    uae_usu = m_ConfigTechnical.getString("SQL.A_UAE.usuario");

    usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
    usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");
    usu_log = m_ConfigTechnical.getString("SQL.A_USU.login");

    rpg_org  = m_ConfigTechnical.getString("SQL.A_RPG.organizacion");
    rpg_ent  = m_ConfigTechnical.getString("SQL.A_RPG.entidad");
    rpg_apl  = m_ConfigTechnical.getString("SQL.A_RPG.aplicacion");
    rpg_tip  = m_ConfigTechnical.getString("SQL.A_RPG.tipo");
    rpg_gru  = m_ConfigTechnical.getString("SQL.A_RPG.grupo");
    rpg_pro  = m_ConfigTechnical.getString("SQL.A_RPG.proceso");

    rpu_org  = m_ConfigTechnical.getString("SQL.A_RPU.organizacion");
    rpu_ent  = m_ConfigTechnical.getString("SQL.A_RPU.entidad");
    rpu_apl  = m_ConfigTechnical.getString("SQL.A_RPU.aplicacion");
    rpu_tip  = m_ConfigTechnical.getString("SQL.A_RPU.tipo");
    rpu_usu  = m_ConfigTechnical.getString("SQL.A_RPU.usuario");
    rpu_pro  = m_ConfigTechnical.getString("SQL.A_RPU.proceso");
  }

  public Vector getListaGrupos(GeneralValueObject gVO,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String from = "";
    String where = "";

    if  ((gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null)
        && (gVO.getAtributo("codEntidad") != null) ) {
        String aplicacion = (String) gVO.getAtributo("codAplicacion");
        String organizacion = (String) gVO.getAtributo("codOrganizacion");
        String entidad = (String) gVO.getAtributo("codEntidad");
        String grupo ="";
        if (!"".equals(aplicacion) && !"".equals(organizacion) && !"".equals(entidad)) {
            try{
              abd = new AdaptadorSQLBD(params);
              conexion = abd.getConnection();
              abd.inicioTransaccion(conexion);
              // Consulta del menu (suponemos que solo hay uno)
              from = " DISTINCT " + gru_cod + "," + gru_nom + "," + rpg_pro + "," + rpg_tip;
              where = ugo_apl + "=" + aplicacion + " AND " + ugo_org + "=" + organizacion + " AND " +
                      ugo_ent + "=" + entidad;
              String[] join = new String[8];
              join[0] = GlobalNames.ESQUEMA_GENERICO + "A_UGO";
              join[1] = "LEFT";
              join[2] = GlobalNames.ESQUEMA_GENERICO + "A_GRU";
              join[3] = GlobalNames.ESQUEMA_GENERICO + "A_UGO." + ugo_gru + "=" + GlobalNames.ESQUEMA_GENERICO + "A_GRU."
                        + gru_cod;
              join[4] = "LEFT";
              join[5] = GlobalNames.ESQUEMA_GENERICO + "A_RPG";
              join[6] = GlobalNames.ESQUEMA_GENERICO + "A_UGO." + ugo_apl + "=" + GlobalNames.ESQUEMA_GENERICO + "A_RPG."
                        + rpg_apl + " AND " + GlobalNames.ESQUEMA_GENERICO + "A_UGO." + ugo_org + "="
                        + GlobalNames.ESQUEMA_GENERICO + "A_RPG." +rpg_org + " AND " + GlobalNames.ESQUEMA_GENERICO + "A_UGO."
                        + ugo_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "A_RPG." + rpg_ent + " AND "
                        + GlobalNames.ESQUEMA_GENERICO + "A_UGO." + ugo_gru + "=" + GlobalNames.ESQUEMA_GENERICO + "A_RPG."
                        + rpg_gru;
              join[7] = "false";

              sql = abd.join(from,where,join);
              
              String parametros[] = {"1","1"};
              sql += abd.orderUnion(parametros);
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              stmt = conexion.createStatement();
              rs = stmt.executeQuery(sql);

              GeneralValueObject gVO1 = new GeneralValueObject();
              Vector listaPermisos = new Vector();
              GeneralValueObject gVO2 = new GeneralValueObject();

              while(rs.next()){
                  grupo = rs.getString(gru_cod);
                  if (grupo != null)
                      if (grupo.equals((String) gVO1.getAtributo("codGrupo")))
                      {
                          gVO2 = new GeneralValueObject();
                          gVO2.setAtributo("codProceso", rs.getString(rpg_pro));
                          gVO2.setAtributo("tipProceso", rs.getString(rpg_tip));
                          listaPermisos.addElement(gVO2);

                      } else {
                          gVO1 = new GeneralValueObject();
                          String desc = rs.getString(gru_nom);
                          gVO1.setAtributo("codGrupo", grupo);
                          gVO1.setAtributo("descGrupo", desc);
                          resultado.addElement(gVO1);
                          listaPermisos = new Vector();
                          gVO2 = new GeneralValueObject();
                          gVO2.setAtributo("codProceso", rs.getString(rpg_pro));
                          gVO2.setAtributo("tipProceso", rs.getString(rpg_tip));
                          listaPermisos.addElement(gVO2);
                          gVO1.setAtributo("listaPermisos", listaPermisos);
                      }
              }
              rs.close();
              stmt.close();


            }catch (Exception e){
                  rollBackTransaction(abd,conexion,e);
            }finally{
              commitTransaction(abd,conexion);
            }
        }
    }
    return resultado;
  }

  public Vector getListaUsuarios(GeneralValueObject gVO,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String from = "";
    String where = "";
    if  ((gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null)
        && (gVO.getAtributo("codEntidad") != null) ) {
        String aplicacion = (String) gVO.getAtributo("codAplicacion");
        String organizacion = (String) gVO.getAtributo("codOrganizacion");
        String entidad = (String) gVO.getAtributo("codEntidad");
        String usuario ="";
        String grupo ="";
        if (!"".equals(aplicacion) && !"".equals(organizacion) && !"".equals(entidad)) {
            try{
              abd = new AdaptadorSQLBD(params);
              conexion = abd.getConnection();
              abd.inicioTransaccion(conexion);
              // Consulta del menu (suponemos que solo hay uno)
              from = uae_usu +","+ ugo_gru + "," +usu_nom + "," + usu_log + "," + rpu_pro + "," + rpu_tip;
              where = uae_apl + "=" + aplicacion + " AND " + uae_org + "=" + organizacion + " AND " + uae_ent + "=" + entidad;

              String[] join = new String[11];
              join[0] = GlobalNames.ESQUEMA_GENERICO + "A_UGO";
              join[1] = "INNER";
              join[2] = GlobalNames.ESQUEMA_GENERICO + "A_UAE";
              join[3] = GlobalNames.ESQUEMA_GENERICO + "a_uae." + uae_apl + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ugo." +
                        ugo_apl + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_uae." + uae_org + "=" +
                        GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_org + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_uae." +
                        uae_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_ent + " AND " +
                        GlobalNames.ESQUEMA_GENERICO + "a_uae." + uae_usu + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ugo." +
                        ugo_usu;
              join[4] = "INNER";
              join[5] = GlobalNames.ESQUEMA_GENERICO + "A_USU";
              join[6] = GlobalNames.ESQUEMA_GENERICO + "a_uae." + uae_usu + "=" + GlobalNames.ESQUEMA_GENERICO + "a_usu." +
                        usu_cod;
              join[7] = "LEFT";
              join[8] = GlobalNames.ESQUEMA_GENERICO + "A_RPU";
              join[9] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_apl + "=" + GlobalNames.ESQUEMA_GENERICO
                        + "a_rpu." + rpu_apl + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_org + "="
                        + GlobalNames.ESQUEMA_GENERICO + "a_rpu." + rpu_org + " AND " + GlobalNames.ESQUEMA_GENERICO
                        + "a_ugo." + ugo_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_rpu." + rpu_ent + " AND "
                        + GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_usu + "=" + GlobalNames.ESQUEMA_GENERICO + "a_rpu."
                        + rpu_usu;
              join[10] = "false";
              sql = abd.join(from,where,join);
              //String parametros[] = {"1","1"};
              
              String parametros[] = {"4","4"};
              sql += abd.orderUnion(parametros);

              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              stmt = conexion.createStatement();
              rs = stmt.executeQuery(sql);

              GeneralValueObject gVO1 = new GeneralValueObject();
              Vector listaPermisos = new Vector();
              GeneralValueObject gVO2 = new GeneralValueObject();

              while(rs.next()){
                  usuario = rs.getString(uae_usu);
                  grupo = rs.getString(ugo_gru);
                  if ( (usuario != null) && (grupo != null) )
                      if (usuario.equals((String) gVO1.getAtributo("codUsuario")) && grupo.equals((String) gVO1.getAtributo("codGrupo")))
                      {
                          gVO2 = new GeneralValueObject();
                          String codProceso = rs.getString(rpu_pro);
                          if (codProceso != null) {
                              gVO2.setAtributo("codProceso", codProceso);
                              gVO2.setAtributo("tipProceso", rs.getString(rpu_tip));
                              listaPermisos.addElement(gVO2);
                          }

                      } else {
                          gVO1 = new GeneralValueObject();
                          gVO1.setAtributo("codUsuario", usuario);
                          gVO1.setAtributo("codGrupo", rs.getString(ugo_gru));
                          gVO1.setAtributo("nomUsuario", rs.getString(usu_nom));
                          gVO1.setAtributo("logUsuario", rs.getString(usu_log));
                          resultado.addElement(gVO1);
                              listaPermisos = new Vector();
                          gVO2 = new GeneralValueObject();
                          String codProceso = rs.getString(rpu_pro);
                          if (codProceso != null) {
                              gVO2.setAtributo("codProceso", codProceso);
                              gVO2.setAtributo("tipProceso", rs.getString(rpu_tip));
                              listaPermisos.addElement(gVO2);
                          }
                          gVO1.setAtributo("listaPermisos",listaPermisos);
                    }
              }
              rs.close();
              stmt.close();
            if (m_Log.isDebugEnabled()) m_Log.debug(resultado);
            }catch (Exception e){
                  rollBackTransaction(abd,conexion,e);
            }finally{
              commitTransaction(abd,conexion);
            }
        }
    }
    return resultado;
  }


  public void actualizarPermisosGrupo(GeneralValueObject gVO, String[] params){
       if  ((gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null)
        && (gVO.getAtributo("codEntidad") != null) && (gVO.getAtributo("codigoGU") != null)) {

          String aplicacion = (String) gVO.getAtributo("codAplicacion");
        String organizacion = (String) gVO.getAtributo("codOrganizacion");
        String entidad = (String) gVO.getAtributo("codEntidad");
        String grupo = (String) gVO.getAtributo("codigoGU");

        if (!"".equals(aplicacion) && !"".equals(organizacion) && !"".equals(entidad)&& !"".equals(grupo)) {
            AdaptadorSQLBD abd = null;
            Connection conexion = null;
            Statement stmt = null;
            String sql = "";
            try{
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
              abd = new AdaptadorSQLBD(parametros);
              conexion = abd.getConnection();
              abd.inicioTransaccion(conexion);

              stmt = conexion.createStatement();
              sql = "DELETE FROM A_RPG WHERE " +
                    rpg_apl + "=" + aplicacion + " AND " + rpg_org + "=" + organizacion +
                    " AND " + rpg_ent + "=" + entidad + " AND " + rpg_gru + "=" + grupo;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              stmt.executeUpdate(sql);
              stmt.close();
              Vector procesosPermisos = (Vector) gVO.getAtributo("nuevosPermisos");
              if (procesosPermisos == null) procesosPermisos = new Vector();
              for (int i=0; i<procesosPermisos.size(); i++) {
                      Vector par = (Vector) procesosPermisos.elementAt(i);
                      String codProceso = (String) par.elementAt(0);
                      String permiso = (String) par.elementAt(1);

                     boolean insertado = false;
                    // Comprobamos que no se ha insertado en otro punto de menu.
                    sql = "SELECT " + rpg_pro + " FROM A_RPG WHERE "
                          + rpg_apl + "=" + aplicacion + " AND "
                          + rpg_org + "=" + organizacion + " AND " + rpg_ent + "=" + entidad
                          + " AND " + rpg_gru + "=" + grupo + " AND " + rpg_pro + "=" + codProceso;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                    stmt = conexion.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        if (rs.getString(rpg_pro)!= null) insertado = true;
                    }
                    rs.close();
                    stmt.close();

                      if(!insertado) {
                        sql = "INSERT INTO A_RPG("+ rpg_gru+","+rpg_org+","+rpg_ent+","
                              + rpg_apl+","+rpg_pro+","+rpg_tip+")"
                              + " VALUES (" + grupo + "," + organizacion +","+ entidad +"," + aplicacion + ","
                              + codProceso +"," + permiso +")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.createStatement();
                        stmt.executeUpdate(sql);
                        stmt.close();
                    }
              }

            }catch (Exception e){
                  rollBackTransaction(abd,conexion,e);
            }finally{
              commitTransaction(abd,conexion);
            }
        }
    }
  }

  public void actualizarPermisosUsuario(GeneralValueObject gVO, String[] params){
       if  ((gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null)
        && (gVO.getAtributo("codEntidad") != null) && (gVO.getAtributo("codigoGU") != null)) {

        String aplicacion = (String) gVO.getAtributo("codAplicacion");
        String organizacion = (String) gVO.getAtributo("codOrganizacion");
        String entidad = (String) gVO.getAtributo("codEntidad");
        String usuario = (String) gVO.getAtributo("codigoGU");

        if (!"".equals(aplicacion) && !"".equals(organizacion) && !"".equals(entidad)&& !"".equals(usuario)) {
            AdaptadorSQLBD abd = null;
            Connection conexion = null;
            Statement stmt = null;
            String sql = "";
            try{
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
              abd = new AdaptadorSQLBD(parametros);
              conexion = abd.getConnection();
              abd.inicioTransaccion(conexion);

              stmt = conexion.createStatement();
              sql = "DELETE FROM A_RPU WHERE "
                    + rpu_apl + "=" + aplicacion + " AND " + rpu_org + "=" + organizacion
                    + " AND " + rpu_ent + "=" + entidad + " AND " + rpu_usu + "=" + usuario;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              stmt.executeUpdate(sql);
              stmt.close();
              Vector procesosPermisos = (Vector) gVO.getAtributo("nuevosPermisos");
              if (procesosPermisos == null) procesosPermisos = new Vector();
              for (int i=0; i<procesosPermisos.size(); i++) {
                      Vector par = (Vector) procesosPermisos.elementAt(i);
                      String codProceso = (String) par.elementAt(0);
                      String permiso = (String) par.elementAt(1);
                      if (!"-1".equals(permiso)) {
                           boolean insertado = false;
                          // Comprobamos que no se ha insertado en otro punto de menu.
                        sql = "SELECT " + rpu_pro + " FROM A_RPU WHERE "
                              + rpu_apl + "=" + aplicacion + " AND " + rpu_org + "=" + organizacion + " AND "
                              + rpu_ent + "=" + entidad + " AND " + rpu_usu + "=" + usuario + " AND "
                              + rpu_pro + "=" + codProceso;
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                          stmt = conexion.createStatement();
                          ResultSet rs = stmt.executeQuery(sql);
                          while(rs.next()){
                              if (rs.getString(rpu_pro)!= null) insertado = true;
                          }
                          rs.close();
                          stmt.close();

                          if(!insertado) {
                            //stmt = conexion.createStatement();
                            //stmt.executeUpdate(sql);
                            //stmt.close();
                            sql = "INSERT INTO A_RPU("+ rpu_usu+","+rpu_org+","+rpu_ent+
                                  ","+rpu_apl+","+rpu_pro+","+rpu_tip+")" + " VALUES (" + usuario + "," + organizacion +
                                  ","+ entidad +"," + aplicacion + "," + codProceso +"," + permiso +")";
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            stmt = conexion.createStatement();
                            stmt.executeUpdate(sql);
                            stmt.close();
                        }
                    }
              }
            }catch (Exception e){
                  rollBackTransaction(abd,conexion,e);
            }finally{
              commitTransaction(abd,conexion);
            }
        }
    }
  }

  public static AutorizacionesInternasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (AutorizacionesInternasDAO.class) {
        if (instance == null) {
          instance = new AutorizacionesInternasDAO();
        }
      }
    }
    return instance;
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
