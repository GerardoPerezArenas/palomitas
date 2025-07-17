// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.OrganizacionVO;
import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.administracion.exception.LoginDuplicadoException;
import es.altia.agora.business.administracion.mantenimiento.DirectivaVO;
import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.sge.persistence.manual.PermisoProcRestringidoDAO;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.EncriptacionContrasenha;
import es.altia.util.cache.CacheDatosFactoria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;
import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DepartamentosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class UsuariosGruposDAO  {
  private static UsuariosGruposDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(UsuariosGruposDAO.class.getName());

  protected static String usu_cod;
  protected static String usu_idi;
  protected static String usu_nom;
  protected static String usu_log;
  protected static String usu_pas;
  protected static String usu_act;
  protected static String usu_con;
  protected static String usu_lis;
  protected static String usu_teb;
  protected static String usu_tdm;
  protected static String usu_ico;
  protected static String usu_mpr;
  public static String usu_nif;
  public static String usu_firmante;
  protected static String usu_email;
  protected static String usu_firma;
  protected static String usu_tipo_firma;
  protected static String usu_buzfir;
	protected static String usu_blq;

  protected static String usu_rotacion_pass_cod;
  protected static String gru_cod;
  protected static String gru_nom;

  protected static String idi_cod;
  protected static String idi_nom;

  protected static String org_cod;
  protected static String org_des;

  protected static String ous_usu;
  protected static String ous_org;

  protected static String ugo_gru;
  protected static String ugo_usu;
  protected static String ugo_org;
  protected static String ugo_ent;
  protected static String ugo_apl;

  protected static String apl_cod;
  protected static String apl_nom;

  protected static String ent_cod;
  protected static String ent_nom;
  protected static String ent_org;

  protected static String uou_uor;
  protected static String uou_usu;
  protected static String uou_org;
  protected static String uou_ent;
  protected static String uou_car;

  protected static String uor_cod;
  protected static String uor_nom;
  protected static String uor_pad;

  protected static String aae_org;
  protected static String eea_ent;
  protected static String eea_apl;
  protected static String eea_bde;

  protected static String rpg_gru;

  protected static String rpu_usu;

  protected static String aau_apl;
  protected static String aau_usu;
  protected static String aau_org;
  protected static String aau_ent;

  protected static String uae_org;
  protected static String uae_ent;
  protected static String uae_apl;
  protected static String uae_usu;
  protected static String uae_ede;

  protected static String car_cod;
  protected static String car_nom;




  protected UsuariosGruposDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    usu_cod = campos.getString("SQL.A_USU.codigo");
    usu_idi = campos.getString("SQL.A_USU.idioma");
    usu_nom = campos.getString("SQL.A_USU.nombre");
    usu_log = campos.getString("SQL.A_USU.login");
    usu_pas = campos.getString("SQL.A_USU.password");
    usu_act = campos.getString("SQL.A_USU.puedeActualizar");
    usu_con = campos.getString("SQL.A_USU.puedeConsultar");
    usu_lis = campos.getString("SQL.A_USU.puedeListar");
    usu_teb = campos.getString("SQL.A_USU.tiempoMaxBloqueos");
    usu_tdm = campos.getString("SQL.A_USU.tipoMenu");
    usu_ico = campos.getString("SQL.A_USU.iconos");
    usu_mpr = campos.getString("SQL.A_USU.mostrarProcesosRest");
    usu_nif = campos.getString("SQL.A_USU.nif");
    usu_firmante = campos.getString("SQL.A_USU.firmante");
		usu_blq = campos.getString("SQL.A_USU.estado");
    usu_email = campos.getString("SQL.A_USU.email");
    usu_firma = campos.getString("SQL.A_USU.firma");
    usu_tipo_firma = campos.getString("SQL.A_USU.tipo_firma");
    usu_buzfir = campos.getString("SQL.A_USU.buzonFirma");

    usu_rotacion_pass_cod=campos.getString("SQL.A_USU_ROTACION_PASS.codigo_usuario");

    gru_cod = campos.getString("SQL.A_GRU.codGrupo");
    gru_nom = campos.getString("SQL.A_GRU.nombreGrupo");

    idi_cod = campos.getString("SQL.A_IDI.codigo");
    idi_nom = campos.getString("SQL.A_IDI.descripcion");

    org_cod = campos.getString("SQL.A_ORG.codigo");
    org_des = campos.getString("SQL.A_ORG.descripcion");

    ous_usu = campos.getString("SQL.A_OUS.usuario");
    ous_org = campos.getString("SQL.A_OUS.organizacion");

    ugo_gru = campos.getString("SQL.A_UGO.grupo");
    ugo_usu = campos.getString("SQL.A_UGO.usuario");
    ugo_org = campos.getString("SQL.A_UGO.organizacion");
    ugo_ent = campos.getString("SQL.A_UGO.entidad");
    ugo_apl = campos.getString("SQL.A_UGO.aplicacion");

    apl_cod = campos.getString("SQL.A_APL.codigo");
    apl_nom = campos.getString("SQL.A_APL.nombre");

    ent_cod = campos.getString("SQL.A_ENT.codigo");
    ent_nom = campos.getString("SQL.A_ENT.nombre");
    ent_org = campos.getString("SQL.A_ENT.organizacion");

    uou_uor = campos.getString("SQL.A_UOU.unidadOrg");
    uou_usu = campos.getString("SQL.A_UOU.usuario");
    uou_org = campos.getString("SQL.A_UOU.organizacion");
    uou_ent = campos.getString("SQL.A_UOU.entidad");
    uou_car = campos.getString("SQL.A_UOU.cargo");

    uor_cod = campos.getString("SQL.A_UOR.codigo");
    uor_nom = campos.getString("SQL.A_UOR.nombre");
    uor_pad = campos.getString("SQL.A_UOR.padre");

    aae_org = campos.getString("SQL.A_EEA.organizacion");
    eea_ent = campos.getString("SQL.A_EEA.entidad");
    eea_apl = campos.getString("SQL.A_EEA.aplicacion");
    eea_bde = campos.getString("SQL.A_EEA.jndi");

    rpg_gru = campos.getString("SQL.A_RPG.grupo");

    rpu_usu = campos.getString("SQL.A_RPU.usuario");   

    aau_apl = campos.getString("SQL.A_AAU.aplicacion");
    aau_usu = campos.getString("SQL.A_AAU.usuario");
    aau_org = campos.getString("SQL.A_AAU.organizacion");
    aau_ent = campos.getString("SQL.A_AAU.entidad");

    uae_org = campos.getString("SQL.A_UAE.organizacion");
    uae_ent = campos.getString("SQL.A_UAE.entidad");
    uae_apl = campos.getString("SQL.A_UAE.aplicacion");
    uae_usu = campos.getString("SQL.A_UAE.usuario");
    uae_ede = campos.getString("SQL.A_UAE.ejercicioPD");

    car_cod = campos.getString("SQL.A_CAR.codigo");
    car_nom = campos.getString("SQL.A_CAR.nombre");
  }

  public static UsuariosGruposDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (UsuariosGruposDAO.class) {
        if (instance == null) {
                    instance = new UsuariosGruposDAO();
                }
            }
        }
        return instance;
    }

    public Vector getUsuarios(String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            sql = "SELECT "+abd.convertir("USU_FBA", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+
                    " AS FBAJA,"+ usu_cod + "," + usu_log + "," + usu_nom + " FROM " + GlobalNames.ESQUEMA_GENERICO +
                  "A_USU ORDER BY 3";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                GeneralValueObject g = new GeneralValueObject();
                String codUsuario = rs.getString(usu_cod);
                g.setAtributo("codUsuario",codUsuario);
                String login = rs.getString(usu_log);
                g.setAtributo("login",login);
                String nombreUsuario = rs.getString(usu_nom);
                g.setAtributo("nombreUsuario",nombreUsuario);
                String fechaEliminado = rs.getString("FBAJA");
                g.setAtributo("fechaEliminado",fechaEliminado);
                lista.addElement(g);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return lista;
    }

    public Vector getUsuariosLocal(String codOrg,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            from = usu_cod + "," + usu_log + "," + usu_nom + ","+
                    abd.convertir("USU_FBA", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+" AS FBAJA ";
            where = ous_org + "=" + codOrg;
            String[] join = new String[5];
            join[0] = GlobalNames.ESQUEMA_GENERICO + "A_USU";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_ous";
            join[3] = GlobalNames.ESQUEMA_GENERICO + "a_usu." + usu_cod + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ous." +
                      ous_usu;
            join[4] = "false";
            sql = abd.join(from,where,join);
            String parametros[] = {"3","3"};
            sql += abd.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("getUsuariosLocal sql: " + sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                GeneralValueObject g = new GeneralValueObject();
                String codUsuario = rs.getString(usu_cod);
                g.setAtributo("codUsuario",codUsuario);
                String login = rs.getString(usu_log);
                g.setAtributo("login",login);
                String nombreUsuario = rs.getString(usu_nom);
                g.setAtributo("nombreUsuario",nombreUsuario);
                String fechaEliminado = rs.getString("FBAJA");
                g.setAtributo("fechaEliminado",fechaEliminado);
                lista.addElement(g);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return lista;
    }

    public Vector getGrupos(String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            sql = "SELECT " + gru_cod + "," + gru_nom + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_GRU";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                GeneralValueObject g = new GeneralValueObject();
                String codGrupo = rs.getString(gru_cod);
                g.setAtributo("codGrupo",codGrupo);
                String nombreGrupo = rs.getString(gru_nom);
                g.setAtributo("nombreGrupo",nombreGrupo);
                lista.addElement(g);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return lista;
    }

    public UsuariosGruposValueObject getDatosUsuarios(UsuariosGruposValueObject ugVO,String codUsuario,String[] params, String portafirmas){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector listaGrupos = new Vector();
        Vector listaUnidOrganicas = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            // PESTAÑA DE DATOS GENERALES
            
            from = usu_nom + "," + usu_log + "," + usu_pas + "," + usu_idi + "," + idi_nom + "," + usu_nif + "," +
			usu_firmante + "," +usu_email + "," +usu_tipo_firma +","+ usu_blq+",";
            
            if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
               from = from + (usu_buzfir +",");
            }
            
            from = from + (abd.convertir("USU_FBA", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+" AS FBAJA,USU_CAMBIO_PASSWORD ");
            where = usu_cod + "=" + codUsuario;
            String[] join = new String[5];
            join[0] = GlobalNames.ESQUEMA_GENERICO + "A_USU";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_idi";
            join[3] = GlobalNames.ESQUEMA_GENERICO + "a_usu." + usu_idi + "=" + GlobalNames.ESQUEMA_GENERICO + "a_idi." +
                      idi_cod;
            join[4] = "false";
            sql = abd.join(from,where,join);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                String nombreUsuario = rs.getString(usu_nom);
                ugVO.setNombreUsuario(nombreUsuario);
                String login = rs.getString(usu_log);
                ugVO.setLogin(login);
                String contrasena = rs.getString(usu_pas);
                ugVO.setContrasena(contrasena);
                ugVO.setContrasena2(contrasena);
                String codIdioma= rs.getString(usu_idi);
                ugVO.setCodIdioma(codIdioma);
                String descIdioma = rs.getString(idi_nom);
                ugVO.setDescIdioma(descIdioma);
                String nifUsuario = rs.getString(usu_nif);
                ugVO.setNif(nifUsuario);
                int esFirmante = rs.getInt(usu_firmante);
                ugVO.setFirmante(esFirmante);
                String email = rs.getString(usu_email);
                ugVO.setEmail(email);
                String tipoFirma = rs.getString(usu_tipo_firma);
                ugVO.setTipoFirma(tipoFirma);
		int estado = rs.getInt(usu_blq);
		ugVO.setEstado(estado);
                
                if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                    String buzonFirma = rs.getString(usu_buzfir);
                    ugVO.setBuzonFirma(buzonFirma);
                }
                
                String fechaEliminado = rs.getString("FBAJA");
                ugVO.setFechaEliminado(fechaEliminado);
                int usuCambioPassword = rs.getInt("USU_CAMBIO_PASSWORD");
                ugVO.setCambioPantallaObligatorio("false");
                if(usuCambioPassword==1)
                    ugVO.setCambioPantallaObligatorio("true");
				
            }
            rs.close();
            stmt.close();
            //AÑADIR FICHERO
            ugVO.setFicheroFirmaFisico(getFicheroFormulario(ugVO,codUsuario,params));

            m_Log.debug("FICHERO ..."+ugVO.getFicheroFirma());
            m_Log.debug("FISICO FICHERO ..."+ugVO.getFicheroFirmaFisico());
            m_Log.debug("TIPO FICHERO ..."+ugVO.getTipoFirma());

            // PESTAÑA DE GRUPOS
            from = ugo_org + "," + ugo_ent + "," + ugo_apl + "," + ugo_gru + "," + gru_nom + "," +
                   org_des + "," + ent_nom + "," + apl_nom;
            where = ugo_usu + "=" + codUsuario;
            String[] join1 = new String[14];
            join1[0] = GlobalNames.ESQUEMA_GENERICO + "A_UGO";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_gru";
            join1[3] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_gru + "=" + GlobalNames.ESQUEMA_GENERICO + "a_gru." +
                       gru_cod;
            join1[4] = "INNER";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_org";
            join1[6] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." +
                       org_cod;
            join1[7] = "INNER";
            join1[8] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
            join1[9] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_org + " AND " +
                       GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_cod;
            join1[10] = "INNER";
            join1[11] = GlobalNames.ESQUEMA_GENERICO + "a_apl";
            join1[12] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_apl + "=" + GlobalNames.ESQUEMA_GENERICO + "a_apl." +
                        apl_cod;
            join1[13] = "false";
            sql = abd.join(from,where,join1);
            sql += " ORDER BY 4,1,2,3";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            String entrar = "no";
            while(rs.next()) {
                entrar = "si";
                UsuariosGruposValueObject ug = new UsuariosGruposValueObject();
                String codOrganizacion = rs.getString(ugo_org);
                ug.setCodOrganizacion(codOrganizacion);
                String nombreOrganizacion = rs.getString(org_des);
                ug.setNombreOrganizacion(nombreOrganizacion);
                String codEntidad = rs.getString(ugo_ent);
                ug.setCodEntidad(codEntidad);
                String nombreEntidad = rs.getString(ent_nom);
                ug.setNombreEntidad(nombreEntidad);
                String codAplicacion = rs.getString(ugo_apl);
                ug.setCodAplicacion(codAplicacion);
                String nombreAplicacion = rs.getString(apl_nom);
                ug.setNombreAplicacion(nombreAplicacion);
                String codGrupo = rs.getString(ugo_gru);
                ug.setCodGrupo(codGrupo);
                String nombreGrupo = rs.getString(gru_nom);
                ug.setNombreGrupo(nombreGrupo);
                listaGrupos.addElement(ug);
                ugVO.setListaGrupos(listaGrupos);
            }
            if("no".equals(entrar)) {
                ugVO.setListaGrupos(listaGrupos);
            }
            rs.close();
            stmt.close();
            // PESTAÑA DE UNIDADES ORGANICAS
            //1º que tengan cargo
            from = uou_org + "," + uou_ent + "," + uou_uor + "," + org_des + "," + ent_nom + "," + car_nom + "," + car_cod  + "," + uor_pad;
            where = uou_usu + "=" + codUsuario;
            String[] join2 = new String[14];
            join2[0] = GlobalNames.ESQUEMA_GENERICO + "A_UOU";
            join2[1] = "INNER";
            join2[2] = "a_uor";
            join2[3] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_uor + "=a_uor." + uor_cod;
            join2[4] = "INNER";
            join2[5] = GlobalNames.ESQUEMA_GENERICO + "a_org";
            join2[6] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." +
                       org_cod;
            join2[7] = "INNER";
            join2[8] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
            join2[9] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_org + " AND " +
                       GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_cod;
            join2[10] = "INNER";
            join2[11] = "a_car";
            join2[12] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_car + "=a_car." + car_cod;
            join2[13] = "false";
            sql = abd.join(from,where,join2);


            //2º que no tengan cargo
            from = uou_org + "," + uou_ent + "," + uou_uor + "," + org_des + "," + ent_nom + "," + null + "," + null + "," + uor_pad ;
            where = uou_usu + "=" + codUsuario + " and " + uou_car + " is null";
            String[] join3 = new String[11];
            join3[0] = GlobalNames.ESQUEMA_GENERICO + "A_UOU";
            join3[1] = "INNER";
            join3[2] = "a_uor";
            join3[3] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_uor + "=a_uor." + uor_cod;
            join3[4] = "INNER";
            join3[5] = GlobalNames.ESQUEMA_GENERICO + "a_org";
            join3[6] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." +
                       org_cod;
            join3[7] = "INNER";
            join3[8] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
            join3[9] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_org + " AND " +
                       GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_cod;
            join3[10] = "false";
            sql += " UNION " + abd.join(from,where,join3);

            sql += " ORDER BY 1,2";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 = "si";
                UsuariosGruposValueObject ug = new UsuariosGruposValueObject();
                String codOrganizacion = rs.getString(uou_org);
                ug.setCodOrganizacion(codOrganizacion);
                String nombreOrganizacion = rs.getString(org_des);
                ug.setNombreOrganizacion(nombreOrganizacion);
                String codEntidad = rs.getString(uou_ent);
                ug.setCodEntidad(codEntidad);
                String nombreEntidad = rs.getString(ent_nom);
                ug.setNombreEntidad(nombreEntidad);
                String codUnidOrganica = rs.getString(uou_uor);
                ug.setCodUnidOrganica(codUnidOrganica);

                String uorPadre = rs.getString(uor_pad);
                ug.setCodUnidadOrganicaPadre(uorPadre);

                String codCargo = rs.getString(car_cod);
                ug.setCodCargo(codCargo);
                String nombreCargo = rs.getString(car_nom);
                ug.setNombreCargo(nombreCargo);
                listaUnidOrganicas.addElement(ug);
                ugVO.setListaUnidOrganicas(listaUnidOrganicas);
            }
            if("no".equals(entrar1)) {
                ugVO.setListaUnidOrganicas(listaUnidOrganicas);
            }

            // Carga de la lista de directivas
            ugVO.setDirectivas(getListaDirectivas(codUsuario, conexion));
            

            /***** COMPROBACIÓN DE SI EL USUARIO TIENE LA DIRECTIVA DE PROCEDIMIENTOS RESTRINGIDOS *****/
            // En el caso de que el usuario tenga la directiva de procedimientos restringidos, se recuperan los procedimientos sobre los que tiene permiso
            Vector<DirectivaVO> directivas = ugVO.getDirectivas();
            
            String nombreDirectiva = "PROCEDIMIENTOS_RESTRINGIDOS";
            boolean existeDirectiva = false;
            for(int i=0;directivas!=null && i<directivas.size();i++){
                if(directivas.get(i).getCodigo().equals(nombreDirectiva)){
                    existeDirectiva = true;
                    break;
                }
            }// for

            if(existeDirectiva){
                // Si existe la directiva de PROCEDIMIENTOS RESTRINGIDOS, se comprueba si el usuario tiene permiso sobre los mismos
                ugVO.setProcedimientosRestringidos(PermisoProcRestringidoDAO.getInstance().getProcedimientosRestringidosPermisoUsuario(codUsuario,null,params,conexion));
            }
            /***********************************/
            
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return ugVO;
    }

    public Vector getListaUnidadesOrganicasUsuario(UsuariosGruposValueObject u,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        String codUsuario = u.getCodUsuario();
        String codOrganizacion = u.getCodOrganizacion();
        Vector listaUnidOrganicas = new Vector();

        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            from = uou_org + "," + uou_ent + "," + uou_uor + "," + org_des + "," + ent_nom +
                    "," + uor_nom + "," + car_cod + "," + car_nom + "," + uor_pad ;
            where = uou_usu + "=" + codUsuario + " AND " + uou_org + "=" + codOrganizacion;
            String[] join2 = new String[14];
            join2[0] = GlobalNames.ESQUEMA_GENERICO + "A_UOU";
            join2[1] = "INNER";
            join2[2] = "a_uor";
            join2[3] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_uor + "=a_uor." + uor_cod;
            join2[4] = "INNER";
            join2[5] = GlobalNames.ESQUEMA_GENERICO + "a_org";
            join2[6] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." +
                       org_cod;
            join2[7] = "INNER";
            join2[8] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
            join2[9] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_org + " AND " +
                       GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_cod;
            join2[10] = "left";
            join2[11] = "a_car";
            join2[12] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_car + "=a_car." + car_cod;
            join2[13] = "false";
            sql = abd.join(from,where,join2);


            sql += " ORDER BY 6,1,2";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 = "si";
                UsuariosGruposValueObject ug = new UsuariosGruposValueObject();
                String codOrg = rs.getString(uou_org);
                ug.setCodOrganizacion(codOrg);
                String nombreOrganizacion = rs.getString(org_des);
                ug.setNombreOrganizacion(nombreOrganizacion);
                String codEntidad = rs.getString(uou_ent);
                ug.setCodEntidad(codEntidad);
                String nombreEntidad = rs.getString(ent_nom);
                ug.setNombreEntidad(nombreEntidad);
                String codUnidOrganica = rs.getString(uou_uor);
                ug.setCodUnidOrganica(codUnidOrganica);
                String nombreUnidOrganica = rs.getString(uor_nom);
                ug.setNombreUnidOrganica(nombreUnidOrganica);
                String codCargo = rs.getString(car_cod);
                ug.setCodCargo(codCargo);
                String nombreCargo = rs.getString(car_nom);
                ug.setNombreCargo(nombreCargo);
                
                String uorPadre = rs.getString(uor_pad);
                ug.setCodUnidadOrganicaPadre(uorPadre);

                listaUnidOrganicas.addElement(ug);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return listaUnidOrganicas;
    }

    public byte[] getFicheroFormulario(UsuariosGruposValueObject ugVO,String codUsuario,String[] params){
      byte[] fichero = null;
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
          sql = "SELECT " + usu_firma + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE " + usu_cod + "=" + codUsuario;
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          while (rs.next()){
              InputStream st = rs.getBinaryStream(usu_firma);
              if (st!=null) {
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read())!= -1){
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
              } else {
                  return null;
              }
          }
          rs.close();
          stmt.close();
      }catch (Exception e){
        rollBackTransaction(abd,conexion,e);
      }finally{
        commitTransactAndCloseConn(abd,conexion);
      }
        return fichero;
    }

    public UsuariosGruposValueObject getDatosUsuariosLocal(UsuariosGruposValueObject ugVO,String codUsuario,String codOrg,String codEnt,String[] params, String portafirmas){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector listaGrupos = new Vector();
        Vector listaUnidOrganicas = new Vector();
        Vector listaCargos = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            // PESTAÑA DE DATOS GENERALES
            /**
            from = abd.convertir("USU_FBA", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+
                    " AS FBAJA,"+usu_nom + "," + usu_log + "," + usu_pas + "," + usu_idi + "," + idi_nom + "," + usu_nif + "," +
                   usu_firmante + "," + usu_email + "," + usu_tipo_firma + "," + usu_blq;
            */
            from = abd.convertir("USU_FBA", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+
                    " AS FBAJA,"+usu_nom + "," + usu_log + "," + usu_pas + "," + usu_idi + "," + idi_nom + "," + usu_nif + "," +
                   usu_firmante + "," + usu_email + "," + usu_tipo_firma + "," + usu_blq + ",USU_CAMBIO_PASSWORD";
            
            if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                    from = from + (", " + usu_buzfir + " ");
            }
            where = usu_cod + "=" + codUsuario;
            String[] join = new String[5];
            join[0] = GlobalNames.ESQUEMA_GENERICO + "A_USU";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_idi";
            join[3] = GlobalNames.ESQUEMA_GENERICO + "a_usu." + usu_idi + "=" + GlobalNames.ESQUEMA_GENERICO + "a_idi." +
                      idi_cod;
            join[4] = "false";
			
            sql = abd.join(from,where,join);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                String nombreUsuario = rs.getString(usu_nom);
                ugVO.setNombreUsuario(nombreUsuario);
                String login = rs.getString(usu_log);
                ugVO.setLogin(login);
                String contrasena = rs.getString(usu_pas);
                ugVO.setContrasena(contrasena);
                ugVO.setContrasena2(contrasena);
                String codIdioma= rs.getString(usu_idi);
                ugVO.setCodIdioma(codIdioma);
                String descIdioma = rs.getString(idi_nom);
                ugVO.setDescIdioma(descIdioma);
                String nifUsuario = rs.getString(usu_nif);
                ugVO.setNif(nifUsuario);
                int esFirmante = rs.getInt(usu_firmante);
                ugVO.setFirmante(esFirmante);
                String email = rs.getString(usu_email);
                ugVO.setEmail(email);
                String tipoFirma = rs.getString(usu_tipo_firma);
                ugVO.setTipoFirma(tipoFirma);
                String fechaEliminado = rs.getString("FBAJA");
                ugVO.setFechaEliminado(fechaEliminado);
                int estado = rs.getInt(usu_blq);
                ugVO.setEstado(estado);
                
                int usuCambioPassword = rs.getInt("USU_CAMBIO_PASSWORD");                
                ugVO.setCambioPantallaObligatorio("false");
                if(usuCambioPassword==1)
                    ugVO.setCambioPantallaObligatorio("true");
                
                if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                    String buzonFirma = rs.getString(usu_buzfir);
                    ugVO.setBuzonFirma(buzonFirma);
                }
            }
            rs.close();
            stmt.close();
            //AÑADIR FICHERO
            ugVO.setFicheroFirmaFisico(getFicheroFormulario(ugVO,codUsuario,params));

            m_Log.debug("FICHERO ..."+ugVO.getFicheroFirma());
            m_Log.debug("FISICO FICHERO ..."+ugVO.getFicheroFirmaFisico());
            m_Log.debug("TIPO FICHERO ..."+ugVO.getTipoFirma());

            // PESTAÑA DE GRUPOS
            from = ugo_org + "," + ugo_ent + "," + ugo_apl + "," + ugo_gru + "," + gru_nom + "," +
                    org_des + "," + ent_nom + "," + apl_nom;
            where = ugo_usu + "=" + codUsuario + " AND " + ugo_org + "=" + codOrg + " AND " +
                    ugo_ent + "=" + codEnt;
            String[] join1 = new String[14];
            join1[0] = GlobalNames.ESQUEMA_GENERICO + "A_UGO";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_gru";
            join1[3] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_gru + "=" + GlobalNames.ESQUEMA_GENERICO + "a_gru." +
                       gru_cod;
            join1[4] = "INNER";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_org";
            join1[6] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." +
                       org_cod;
            join1[7] = "INNER";
            join1[8] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
            join1[9] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_org + " AND " +
                       GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_cod;
            join1[10] = "INNER";
            join1[11] = GlobalNames.ESQUEMA_GENERICO + "a_apl";
            join1[12] = GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_apl + "=" + GlobalNames.ESQUEMA_GENERICO + "a_apl." +
                        apl_cod;
            join1[13] = "false";
            sql = abd.join(from,where,join1);
            sql += " ORDER BY 4,1,2,3";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            String entrar = "no";
            while(rs.next()) {
                entrar = "si";
                UsuariosGruposValueObject ug = new UsuariosGruposValueObject();
                String codOrganizacion = rs.getString(ugo_org);
                ug.setCodOrganizacion(codOrganizacion);
                String nombreOrganizacion = rs.getString(org_des);
                ug.setNombreOrganizacion(nombreOrganizacion);
                String codEntidad = rs.getString(ugo_ent);
                ug.setCodEntidad(codEntidad);
                String nombreEntidad = rs.getString(ent_nom);
                ug.setNombreEntidad(nombreEntidad);
                String codAplicacion = rs.getString(ugo_apl);
                ug.setCodAplicacion(codAplicacion);
                String nombreAplicacion = rs.getString(apl_nom);
                ug.setNombreAplicacion(nombreAplicacion);
                String codGrupo = rs.getString(ugo_gru);
                ug.setCodGrupo(codGrupo);
                String nombreGrupo = rs.getString(gru_nom);
                ug.setNombreGrupo(nombreGrupo);
                listaGrupos.addElement(ug);
                ugVO.setListaGrupos(listaGrupos);
            }
            if("no".equals(entrar)) {
                ugVO.setListaGrupos(listaGrupos);
            }
            rs.close();
            stmt.close();
            // PESTAÑA DE UNIDADES ORGANICAS
            from = uou_org + "," + uou_ent + "," + uou_uor + "," + org_des + "," + ent_nom + "," +
                    uor_nom + "," + car_cod + "," + car_nom  + "," + uor_pad;
            where = uou_usu + "=" + codUsuario + " AND " + uou_org + "=" + codOrg + " AND " +
                    uou_ent + "=" + codEnt;
            String[] join2 = new String[14];
            join2[0] = GlobalNames.ESQUEMA_GENERICO + "A_UOU";
            join2[1] = "INNER";
            join2[2] = "a_uor";
            join2[3] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_uor + "=a_uor." + uor_cod;
            join2[4] = "INNER";
            join2[5] = GlobalNames.ESQUEMA_GENERICO + "a_org";
            join2[6] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." +
                       org_cod;
            join2[7] = "INNER";
            join2[8] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
            join2[9] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_org + " AND " +
                       GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_cod;
            join2[10] = "INNER";
            join2[11] = "a_car";
            join2[12] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_car + "=a_car." + car_cod;
            join2[13] = "false";
            sql = abd.join(from,where,join2);

            //las que no tengan cargo
            from = uou_org + "," + uou_ent + "," + uou_uor + "," + org_des + "," + ent_nom + "," + uor_nom + "," + null + "," +
                   null + "," + uor_pad ;
            where = uou_usu + "=" + codUsuario + " AND " + uou_org + "=" + codOrg + " AND " +
                    uou_ent + "=" + codEnt + " and " + uou_car + " is null";
            String[] join3 = new String[11];
            join3[0] = GlobalNames.ESQUEMA_GENERICO + "A_UOU";
            join3[1] = "INNER";
            join3[2] = "a_uor";
            join3[3] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_uor + "=a_uor." + uor_cod;
            join3[4] = "INNER";
            join3[5] = GlobalNames.ESQUEMA_GENERICO + "a_org";
            join3[6] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." +
                       org_cod;
            join3[7] = "INNER";
            join3[8] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
            join3[9] = GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_org + " AND " +
                       GlobalNames.ESQUEMA_GENERICO + "a_uou." + uou_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." +
                       ent_cod;
            join3[10] = "false";
            sql += " UNION " + abd.join(from,where,join3);

            sql += " ORDER BY 6,1,2";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 = "si";
                UsuariosGruposValueObject ug = new UsuariosGruposValueObject();
                String codOrganizacion = rs.getString(uou_org);
                ug.setCodOrganizacion(codOrganizacion);
                String nombreOrganizacion = rs.getString(org_des);
                ug.setNombreOrganizacion(nombreOrganizacion);
                String codEntidad = rs.getString(uou_ent);
                ug.setCodEntidad(codEntidad);
                String nombreEntidad = rs.getString(ent_nom);
                ug.setNombreEntidad(nombreEntidad);
                String codUnidOrganica = rs.getString(uou_uor);
                ug.setCodUnidOrganica(codUnidOrganica);
                String nombreUnidOrganica = rs.getString(uor_nom);
                ug.setNombreUnidOrganica(nombreUnidOrganica);

                String uorPadre = rs.getString(uor_pad);
                ug.setCodUnidadOrganicaPadre(uorPadre);

                listaUnidOrganicas.addElement(ug);
                String codCargo = rs.getString(car_cod);
                ug.setCodCargo(codCargo);
                String nombreCargo = rs.getString(car_nom);
                ug.setNombreCargo(nombreCargo);
                ugVO.setListaUnidOrganicas(listaUnidOrganicas);
            }
            if("no".equals(entrar1)) {
                ugVO.setListaUnidOrganicas(listaUnidOrganicas);
            }

            // Carga de la lista de directivas
            ugVO.setDirectivas(getListaDirectivas(codUsuario, conexion));



              /***** COMPROBACIÓN DE SI EL USUARIO TIENE LA DIRECTIVA DE PROCEDIMIENTOS RESTRINGIDOS *****/
            // En el caso de que el usuario tenga la directiva de procedimientos restringidos, se recuperan los procedimientos sobre los que tiene permiso
            Vector<DirectivaVO> directivas = ugVO.getDirectivas();

            String nombreDirectiva = "PROCEDIMIENTOS_RESTRINGIDOS";
            boolean existeDirectiva = false;
            for(int i=0;directivas!=null && i<directivas.size();i++){
                if(directivas.get(i).getCodigo().equals(nombreDirectiva)){
                    existeDirectiva = true;
                    break;
                }
            }// for

            if(existeDirectiva){
                // Si existe la directiva de PROCEDIMIENTOS RESTRINGIDOS, se comprueba si el usuario tiene permiso sobre los mismos
                ugVO.setProcedimientosRestringidos(PermisoProcRestringidoDAO.getInstance().getProcedimientosRestringidosPermisoUsuario(codUsuario,null,params,conexion));
            }
            /***********************************/
            
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return ugVO;
    }

    public Vector getListaOrganizaciones(String codUsuario,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        UsuariosGruposValueObject ugVO = new UsuariosGruposValueObject();
        Vector listaOrganizaciones = new Vector();
        Vector listaOrganizacionesUsuario = new Vector();
        Vector listaOrganizacionesFinal = new Vector();
        try{
            SortedMap <String,OrganizacionVO> organizaciones = (SortedMap <String,OrganizacionVO>) CacheDatosFactoria.getImplOrganizaciones().getDatos();

            for(Map.Entry<String,OrganizacionVO> entry : organizaciones.entrySet()) {
                OrganizacionVO organizacion = entry.getValue();
                UsuariosGruposValueObject u = new UsuariosGruposValueObject();
                u.setCodOrganizacion(String.valueOf(organizacion.getCodOrganizacion()));
                u.setNombreOrganizacion(organizacion.getDescripcionOrganizacion());
                listaOrganizaciones.addElement(u);
            }
            if(codUsuario != null) {
                abd = new AdaptadorSQLBD(params);
                conexion = abd.getConnection();
                sql = "SELECT " + org_cod + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_ORG, " +
                      GlobalNames.ESQUEMA_GENERICO + "A_OUS WHERE " + ous_usu + "=" + codUsuario +
                      " AND " + GlobalNames.ESQUEMA_GENERICO + "a_ous." + ous_org + "=" + GlobalNames.ESQUEMA_GENERICO +
                      "a_org." + org_cod + " ORDER BY " + org_cod;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt = conexion.prepareStatement(sql);
                rs = stmt.executeQuery();
                while(rs.next()) {
                    UsuariosGruposValueObject u = new UsuariosGruposValueObject();
                    String codOrganizacion = rs.getString(org_cod);
                    u.setCodOrganizacion(codOrganizacion);
                    listaOrganizacionesUsuario.addElement(u);
                }
                int m=0;
                for(int i=0;i<listaOrganizacionesUsuario.size();i++) {
                    UsuariosGruposValueObject u = new UsuariosGruposValueObject();
                    u = (UsuariosGruposValueObject) listaOrganizacionesUsuario.elementAt(i);
                    String cO = (String) u.getCodOrganizacion();
                    for(int j=m;j<listaOrganizaciones.size();j++) {
                        UsuariosGruposValueObject u2 = new UsuariosGruposValueObject();
                        u2 = (UsuariosGruposValueObject) listaOrganizaciones.elementAt(j);
                        String cO2 = (String) u2.getCodOrganizacion();
                        if(cO.equals(cO2)) {
                            u2.setAutorizacion("si");
                            m=j+1;
                            if(i != (listaOrganizacionesUsuario.size()-1)) {
                                j=listaOrganizaciones.size();
                            }
                        } else {
                            u2.setAutorizacion("no");
                        }
                        listaOrganizacionesFinal.addElement(u2);
                    }
                }
            }
            if(listaOrganizacionesUsuario.size() == 0) {
                listaOrganizacionesFinal = listaOrganizaciones;
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return listaOrganizacionesFinal;
    }

    public Vector getListaOrganizacionesLocal(String codOrg,String descOrg,String codUsuario,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        UsuariosGruposValueObject ugVO = new UsuariosGruposValueObject();
        Vector listaOrganizacionesUsuario = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            if(codUsuario != null) {
                sql = "SELECT " + org_cod + "," + org_des + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_ORG, " +
                      GlobalNames.ESQUEMA_GENERICO + "A_OUS WHERE " + ous_usu + "=" + codUsuario + " AND " + ous_org + "=" +
                      codOrg + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_ous." + ous_org + "=" +
                      GlobalNames.ESQUEMA_GENERICO + "a_org." + org_cod + " ORDER BY " + org_cod;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt = conexion.prepareStatement(sql);
                rs = stmt.executeQuery();
                while(rs.next()) {
                    UsuariosGruposValueObject u = new UsuariosGruposValueObject();
                    String codOrganizacion = rs.getString(org_cod);
                    if(codOrganizacion != null && codOrganizacion.equals(codOrg)) {
                        u.setCodOrganizacion(codOrganizacion);
                        String nombreOrganizacion = rs.getString(org_des);
                        u.setNombreOrganizacion(nombreOrganizacion);
                        u.setAutorizacion("si");
                    } else {
                        u.setCodOrganizacion(codOrg);
                        u.setNombreOrganizacion(descOrg);
                        u.setAutorizacion("no");
                    }
                    listaOrganizacionesUsuario.addElement(u);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return listaOrganizacionesUsuario;
    }

    public Vector getListaUsuariosGrupos(String codGrupo,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            sql = "SELECT DISTINCT " + ugo_usu + "," + usu_nom + "," + ugo_apl + "," + apl_nom + "," +
                    ugo_ent + "," + ent_nom + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UGO, " +
                    GlobalNames.ESQUEMA_GENERICO + "A_USU, " + GlobalNames.ESQUEMA_GENERICO + "A_APL, " +
                    GlobalNames.ESQUEMA_GENERICO + "A_ENT WHERE " + ugo_gru + "=" + codGrupo + " AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_ugo." +ugo_usu + "=" + GlobalNames.ESQUEMA_GENERICO + "a_usu." +
                    usu_cod + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_ugo." + ugo_apl + "=" +
                    GlobalNames.ESQUEMA_GENERICO + "a_apl." + apl_cod + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_ugo." +
                    ugo_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." + ent_cod + " ORDER BY " + ugo_usu + "," +
                    ugo_apl + "," + ugo_ent;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                UsuariosGruposValueObject u = new UsuariosGruposValueObject();
                String codUsuario = rs.getString(ugo_usu);
                u.setCodUsuario(codUsuario);
                String nombreUsuario = rs.getString(usu_nom);
                u.setNombreUsuario(nombreUsuario);
                String codAplicacion = rs.getString(ugo_apl);
                u.setCodAplicacion(codAplicacion);
                String nombreAplicacion = rs.getString(apl_nom);
                u.setNombreAplicacion(nombreAplicacion);
                String codEntidad = rs.getString(ugo_ent);
                u.setCodEntidad(codEntidad);
                String nombreEntidad = rs.getString(ent_nom);
                u.setNombreEntidad(nombreEntidad);
                lista.addElement(u);
            }
            if (m_Log.isDebugEnabled())  m_Log.debug("la longitud de la lista de usuarios de grupos es : " +
                lista.size());

            }catch (Exception e){
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            } finally {
                    try{
                        if (rs!=null) rs.close();
                        if (stmt!=null) stmt.close();
                        abd.devolverConexion(conexion);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                    }
        }
        return lista;
    }

    /**
     * Devuelve un vector de DirectivaVO con todas las directivas existentes,
     * codigo y nombre de su aplicacion y si tienen permiso o no para el usuario
     * indicado.
     * @throws es.altia.common.exception.TechnicalException
     */
    private Vector<DirectivaVO> getListaDirectivas(String codUsuario, Connection con)
            throws TechnicalException {

        m_Log.debug("UsuariosGruposDAO.getListaDirectivas");
        Statement st = null;
        ResultSet rs = null;
        Vector<DirectivaVO> resultado = new Vector<DirectivaVO>();

        try {

            String sql = "SELECT CODIGO, APL, APL_NOM, ETIQ, USU " +
                        " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_DIRECTIVA A_DIR" +
                        " LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU_DIR A_USU_DIR" +
                        " ON (A_DIR.CODIGO = A_USU_DIR.DIR AND (A_USU_DIR.USU=" + codUsuario + " OR A_USU_DIR.USU IS NULL))" +
                        " LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_APL A_APL ON (A_DIR.APL = A_APL.APL_COD)" +
                        " ORDER BY APL, CODIGO";
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                DirectivaVO dir = new DirectivaVO();
                dir.setCodigo(rs.getString("CODIGO"));
                dir.setAplCod(rs.getString("APL"));
                dir.setAplDesc(rs.getString("APL_NOM"));
                dir.setMensaje(rs.getString("ETIQ"));
                if (rs.getString("USU") != null) {
                    dir.setPermiso("SI");
                } else {
                    dir.setPermiso("");
                }
                resultado.add(dir);
            }

        } catch (SQLException ex) {
            m_Log.error("NO SE PUDIERON CARGAR LAS DIRECTIVAS DEL USUARIO");
            ex.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }

        return resultado;
    }

    /**
     * Borra las filas de A_USU_DIR (permisos sobre directivas) que existan
     * para el usuario codUsuario e inserta los permisos para las directivas
     * del vector.
     * @throws es.altia.common.exception.TechnicalException
     */
    private void setListaDirectivas(Vector<DirectivaVO> directivas, String codUsuario, Connection con)
            throws Exception {

        m_Log.debug("UsuariosGruposDAO.setListaDirectivas");
        Statement st = null;
        PreparedStatement ps = null;

        try {

            String sql = "DELETE FROM A_USU_DIR WHERE USU=" + codUsuario;
            m_Log.debug(sql);
            st = con.createStatement();
            st.executeUpdate(sql);
            st.close();

            /** SE BORRAN LOS PERMISOS QUE PUDIERA TENER EL USUARIO SOBRE PROCEDIMIENTOS RESTRINGIDOS **/
            sql = "DELETE FROM USUARIO_PROC_RESTRINGIDO WHERE USU_COD=" + codUsuario;
            m_Log.debug("sql:  " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            m_Log.debug(" rowsDeleted: " + rowsDeleted);

            sql = "INSERT INTO A_USU_DIR(DIR, USU) " +
                  " VALUES (?, " + codUsuario + ")";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);

            for (DirectivaVO dir : directivas) {
                ps.setString(1, dir.getCodigo());
                ps.executeUpdate();
            }
            ps.close();

        } catch (Exception e) {
            m_Log.error("NO SE PUDIERON GRABAR LAS DIRECTIVAS DEL USUARIO");
            throw e;
        } finally {
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Comprueba si un usuario tiene permiso para una directiva.
     * @param codDirectiva Codigo de la directiva
     * @param codUsuario Codigo del usuario
     * @param params Parametros de conexion
     * @return true si tiene permiso
     * @throws es.altia.common.exception.TechnicalException
     */
    public boolean tienePermisoDirectiva(String codDirectiva, int codUsuario, String[] params)
            throws TechnicalException{

        m_Log.debug("UsuariosGruposDAO.tienePermisoDirectiva");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement st = null;
        ResultSet rs = null;
        boolean tienePermiso = false;

        try {

            String sql = "SELECT DIR" +
                        " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU_DIR A_USU_DIR" +
                        " WHERE A_USU_DIR.USU=" + codUsuario + 
                        " AND A_USU_DIR.DIR='" + codDirectiva + "'";
            m_Log.debug(sql);
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next()) {
                tienePermiso = true;
            }

        } catch (Exception ex) {
            m_Log.error("NO SE PUDO COMPROBAR LA DIRECTIVA " + codDirectiva);
            ex.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }

        return tienePermiso;
    }

    public int insertarUsuario(UsuariosGruposValueObject ugVO,String[] params, String portafirmas) throws LoginDuplicadoException{
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int maxCod = 0;
        int numeroPass = 0;
        int resultado = 0;
        try{
            int CAMBIO_PASSWORD_OBLIGATORIO = 0;            
            if(ugVO.getCambioPantallaObligatorio()!=null && "on".equals(ugVO.getCambioPantallaObligatorio()))
                CAMBIO_PASSWORD_OBLIGATORIO = 1;
            
            //m_Log.debug("A por el OAD");
            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            
            //Comprobamos existencia de usuario con ese login (insensitivo mayusculas/minusculas)
            if(!existeUsuLogin(ugVO.getLogin(), conexion)) {
                abd.inicioTransaccion(conexion);
                
                //Se inserta el usuario en A_USU
                sql = "SELECT " + usu_cod + " FROM A_USU ORDER BY 1 DESC";
                /**/
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt = conexion.prepareStatement(sql);
                rs = stmt.executeQuery();
                if(rs.next()) {
                    maxCod = rs.getInt(usu_cod);
                    maxCod++;
                }
                rs.close();
                stmt.close();

                ugVO.setCodUsuario(Integer.toString(maxCod));
                // Se obtiene el hash SHA-1 de la contraseña que es lo que se almacenará en base de datos.
                String hashContrasenha = EncriptacionContrasenha.getHashSHA_1(ugVO.getContrasena());

                sql = "INSERT INTO A_USU (" + usu_cod + "," + usu_idi + "," + usu_log +
                        "," + usu_pas + "," + usu_nom + "," + usu_act + "," + usu_con + "," + usu_lis + "," + usu_teb +
                        "," + usu_tdm + "," + usu_ico + "," + usu_mpr + "," + usu_nif + "," + usu_firmante + "," + usu_email +
                            "," + usu_firma+ "," + usu_tipo_firma +","+ usu_blq + ",USU_CAMBIO_PASSWORD";

               if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                        sql = sql + (", "+ usu_buzfir);
                }

               sql = sql + (") VALUES (" + maxCod + "," + ugVO.getCodIdioma() +
                        //",'" + ugVO.getLogin() + "','" + ugVO.getContrasena() + "','" + ugVO.getNombreUsuario() +
                        ",'" + ugVO.getLogin() + "','" + hashContrasenha + "','" + ugVO.getNombreUsuario() +
                        "',1,1,1,1,1,1,1,"+( (ugVO.getNif()!=null)?("'"+ugVO.getNif()+"'"):"NULL" )+","+ugVO.getFirmante() +
                            ",'"+ugVO.getEmail()+"',?,?, "+ ugVO.getEstado() + ","  + CAMBIO_PASSWORD_OBLIGATORIO );

               if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                        sql = sql + (", '"+ ugVO.getBuzonFirma()+ "'");
                }

               sql = sql + ( ")");
           
           
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                PreparedStatement ps = conexion.prepareStatement(sql);
                if (ugVO.getFicheroFirma()!=null){
                    if(m_Log.isDebugEnabled()) m_Log.debug("Fichero correcto");
                    FormFile fichero = (FormFile)ugVO.getFicheroFirma();
                    byte[] doc = fichero.getFileData();
                    if(m_Log.isDebugEnabled()) m_Log.debug(doc);
                    ps.setBinaryStream(1, fichero.getInputStream(), fichero.getFileSize());
                    ps.setString(2,fichero.getContentType());
    //                if(m_Log.isDebugEnabled()) m_Log.debug("Fichero correcto");
    //                byte[] doc = ugVO.getFicheroFirmaFisico();
    //                if(m_Log.isDebugEnabled()) m_Log.debug(doc);
    //                ps.setBytes(1,doc);
    //                ps.setString(2,ugVO.getTipoFirma());
                } else {
                    if(m_Log.isDebugEnabled()) m_Log.debug("Fichero incorrecto");
                    ps.setNull(1,java.sql.Types.LONGVARBINARY);
                    ps.setNull(2,java.sql.Types.VARCHAR);
                }
                ps.executeUpdate();
                ps.close();

                //Se insertan los demas datos de usuario
                Vector listaOrganizacionesUsuario = ugVO.getListaOrganizaciones();
                if(listaOrganizacionesUsuario.size() >0) {
                    for(int i=0;i<listaOrganizacionesUsuario.size();i++) {
                        sql = "INSERT INTO A_OUS (" + ous_org + "," + ous_usu + ") " +
                              "VALUES (" + listaOrganizacionesUsuario.elementAt(i) + "," + maxCod + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();
                    }
                }

                Vector listaOrganizacionesGrupos = ugVO.getListaOrganizacionesGrupos();
                Vector listaAplicacionesGrupos = ugVO.getListaAplicacionesGrupos();
                Vector listaEntidadesGrupos = ugVO.getListaEntidadesGrupos();
                Vector listaGrupos = ugVO.getListaGrupos();
                if(listaOrganizacionesGrupos.size() >0) {
                    for(int i=0;i<listaOrganizacionesGrupos.size();i++) {
                        sql = "INSERT INTO " +GlobalNames.ESQUEMA_GENERICO + "A_UGO (" + ugo_usu + "," + ugo_org + "," +
                                ugo_ent + "," + ugo_apl + "," + ugo_gru + ") VALUES (" + maxCod + "," +
                                listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                                "," + listaAplicacionesGrupos.elementAt(i) + "," +
                                listaGrupos.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();

                        sql = "SELECT * FROM A_AAU WHERE " + aau_usu + "=" + maxCod +
                              " AND " + aau_apl + "=" + listaAplicacionesGrupos.elementAt(i);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        rs = stmt.executeQuery();
                        String existeAAU = "no";
                        while(rs.next()) {
                            existeAAU = "si";
                        }
                        rs.close();
                        stmt.close();

                        if(existeAAU.equals("no")) {
                            sql = "INSERT INTO A_AAU (" + aau_usu + "," + aau_org + "," +
                                  aau_ent + "," + aau_apl + ") VALUES (" + maxCod + "," +
                                  listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                                  "," + listaAplicacionesGrupos.elementAt(i) + ")";
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            stmt = conexion.prepareStatement(sql);
                            resultado = stmt.executeUpdate();
                            stmt.close();
                        }

                        sql = "SELECT * FROM A_UAE WHERE " +
                              uae_usu + "=" + maxCod + " AND " + uae_org + "=" + listaOrganizacionesGrupos.elementAt(i) + " AND " +
                              uae_ent + "=" + listaEntidadesGrupos.elementAt(i)+ " AND " +
                              uae_apl + "=" + listaAplicacionesGrupos.elementAt(i);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        rs = stmt.executeQuery();
                        String existeUAE = "no";
                        while(rs.next()) {
                            existeUAE = "si";
                        }
                        rs.close();
                        stmt.close();

                        if(existeUAE.equals("no")) {
                            sql = "INSERT INTO A_UAE (" + uae_usu + "," + uae_org + "," +
                                  uae_ent + "," + uae_apl + "," + uae_ede + ") VALUES (" + maxCod + "," +
                                  listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                                  "," + listaAplicacionesGrupos.elementAt(i) + ",null)";
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            stmt = conexion.prepareStatement(sql);
                            resultado = stmt.executeUpdate();
                            stmt.close();
                        }
                    }
                }

                Vector listaOrganizacionesUOR = ugVO.getListaOrganizacionesUOR();
                Vector listaEntidadesUOR = ugVO.getListaEntidadesUOR();
                Vector listaUnidadesOrganicas = ugVO.getListaUnidadesOrganicas();
                Vector listaCargosUOR = ugVO.getListaCargosUOR();
                if(listaOrganizacionesUOR.size() >0) {
                    for(int i=0;i<listaOrganizacionesUOR.size();i++) {
                        if (listaUnidadesOrganicas.elementAt(i).equals("-1")) {
                            Vector UORs = UORsManager.getInstance().getListaUOROrdenPorDesc(params);
                            for (int j=0;j<UORs.size(); j++) {
                                UORDTO dto = (UORDTO) UORs.get(j);
                                sql = "INSERT INTO A_UOU (" + uou_usu + "," + uou_org + "," +
                                      uou_ent + "," + uou_uor + "," + uou_car + ") VALUES (" + maxCod + "," +
                                      listaOrganizacionesUOR.elementAt(i) + "," + listaEntidadesUOR.elementAt(i) +
                                      "," + dto.getUor_cod() + "," + listaCargosUOR.elementAt(i) + ")";
                                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                                stmt = conexion.prepareStatement(sql);
                                resultado = stmt.executeUpdate();
                                stmt.close();
                            }
                        } else {
                            sql = "INSERT INTO A_UOU (" + uou_usu + "," + uou_org + "," +
                                  uou_ent + "," + uou_uor + "," + uou_car + ") VALUES (" + maxCod + "," +
                                  listaOrganizacionesUOR.elementAt(i) + "," + listaEntidadesUOR.elementAt(i) +
                                  "," + listaUnidadesOrganicas.elementAt(i) + "," + listaCargosUOR.elementAt(i) + ")";
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            stmt = conexion.prepareStatement(sql);
                            resultado = stmt.executeUpdate();
                            stmt.close();
                        }
                    }
                }

                //Se insertan datos de rotacion de contrasenha
                sql = "SELECT COD_PASS FROM A_USU_ROTACION_PASS ORDER BY 1";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt = conexion.prepareStatement(sql);
                rs = stmt.executeQuery();
                while(rs.next()) {
                    numeroPass = rs.getInt("COD_PASS");
                    numeroPass++;
                }
                rs.close();
                stmt.close();


                Calendar fechaYear = Calendar.getInstance();
                SimpleDateFormat formatYear = new SimpleDateFormat("dd/MM/yy");
                String fecAlta = formatYear.format(fechaYear.getTime());

                /*
                 sql = "INSERT INTO A_USU_ROTACION_PASS (COD_PASS, FEC_ALTA, USU_COD, USU_PASS) "
                            + " VALUES ('"+numeroPass+"', TO_DATE ('"+fecAlta+"',' DD-MM-YY '),'"+maxCod+"','"+hashContrasenha+"') ";    */            
                sql = "INSERT INTO A_USU_ROTACION_PASS (COD_PASS, FEC_ALTA, USU_COD, USU_PASS) "
                            + " VALUES (?,?,?,?) ";

                m_Log.debug(sql);
                stmt = conexion.prepareStatement(sql);
                int indice = 1;
                stmt.setInt(indice++, numeroPass);
                stmt.setTimestamp(indice++, DateOperations.toTimestamp(fechaYear));
                stmt.setInt(indice++, maxCod);
                stmt.setString(indice++,hashContrasenha);
                resultado = stmt.executeUpdate();
                stmt.close();

                // Inserción de las directivas del usuario
                setListaDirectivas(ugVO.getDirectivas(), Integer.toString(maxCod), conexion);

                // Se insertan los permisos sobre los procedimentos restringidos si los hubiese
                 boolean permisoRestringido = PermisoProcRestringidoDAO.getInstance().insertarPermisosUsuarioProcedimientosRestringidos(ugVO.getProcedimientosRestringidos(),maxCod,conexion);
                 if(!permisoRestringido){
                    m_Log.debug(" ==============> NO SE HAN INSERTADO LOS PERMISOS RESTRINGIDO DEL USUARIO");
                   throw new Exception("Error en el tratamiento de los permisos sobre procedimientos restringidos");
                 }
            } else {
                //Ya existe un usuario con ese login
                m_Log.warn("El usuario no se inserta porque ya existe un usuario con el login " + ugVO.getLogin());
                throw new LoginDuplicadoException();
            }

        }catch (LoginDuplicadoException lde){
            resultado = -2;
            //En este caso no se hace rollback porque este error está fuera de la transaccion
            throw lde;
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            if(resultado >= 0) {
                commitTransactAndCloseConn(abd,conexion);
            }
        }
        return resultado;
  }

    public int modificarUsuario(UsuariosGruposValueObject ugVO,String[] params, String portafirmas){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int maxCod = 0;
    int resultado = 0;
    int numeroPass = 0;
    boolean claveModificada = false;
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);

          Config ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
          String passwordRotacion = ConfigTechnical.getString("password.rotacion.numero");
          
         int CAMBIO_PASSWORD_OBLIGATORIO = 0;
         if(ugVO.getCambioPantallaObligatorio()!=null && "on".equalsIgnoreCase(ugVO.getCambioPantallaObligatorio()))
            CAMBIO_PASSWORD_OBLIGATORIO = 1;
		 
         String claveActual = this.getContrasenhaUsuario(Integer.parseInt(ugVO.getCodUsuario()),conexion);

            if(claveActual!=null && claveActual.equals(ugVO.getContrasena())){
                // La contraseña no se modifica
              sql = "UPDATE A_USU SET " + usu_idi + "=" + ugVO.getCodIdioma() + "," +
                  usu_log + "='" + ugVO.getLogin() +"',"  +
                  usu_nom + "='" + ugVO.getNombreUsuario() + "',"+
                  usu_nif + "="+ ( (ugVO.getNif()!=null)?("'"+ugVO.getNif()+"'"):"NULL" ) + ","+
                  usu_firmante + "='"+ ugVO.getFirmante()+ "',"+
                  usu_email +"='"+ugVO.getEmail()+ "',";
                if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                    sql = sql + (usu_buzfir + "='"+ugVO.getBuzonFirma()+ "'," );
                }
                   sql = sql + (usu_blq +"="+ ugVO.getEstado() + "," + 
                  "USU_CAMBIO_PASSWORD=" + CAMBIO_PASSWORD_OBLIGATORIO);
            }else{
                // Las contraseñas son distintas y por tanto se modifica
                sql = "UPDATE A_USU SET " + usu_idi + "=" + ugVO.getCodIdioma() + "," +
                  usu_log + "='" + ugVO.getLogin() +"'," + usu_pas + "='" + EncriptacionContrasenha.getHashSHA_1(ugVO.getContrasena()) + "'," +
                  usu_nom + "='" + ugVO.getNombreUsuario() + "',"+
                  usu_nif + "="+ ( (ugVO.getNif()!=null)?("'"+ugVO.getNif()+"'"):"NULL" ) + ","+
                  usu_firmante + "='"+ ugVO.getFirmante()+ "',"+
                  usu_email +"='"+ugVO.getEmail()+"',";
                if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                    sql = sql + (usu_buzfir + "='"+ugVO.getBuzonFirma()+ "'," );
                }     
                sql = sql + (usu_blq + "="+ugVO.getEstado() + "," + 
                  "USU_CAMBIO_PASSWORD=" + CAMBIO_PASSWORD_OBLIGATORIO);

                claveModificada = true;
            }


            if (!ugVO.getFechaEliminado().equals("") || ugVO.getFechaEliminado()!=null) sql +=",USU_FBA=NULL";
            if ((ugVO.getFicheroFirma()!=null) || (ugVO.getFicheroFirmaFisico()==null)) sql += ","+usu_firma + "=?," +
                usu_tipo_firma +"=?";
            sql += " WHERE " + usu_cod + "=" + ugVO.getCodUsuario();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement ps = conexion.prepareStatement(sql);
            if (ugVO.getFicheroFirmaFisico()==null){ //Se ha eliminado el fichero
                if(m_Log.isDebugEnabled()) m_Log.debug("Fichero eliminado");
                ps.setNull(1,java.sql.Types.LONGVARBINARY);
                ps.setNull(2,java.sql.Types.VARCHAR);
            } else if (ugVO.getFicheroFirma()!=null){ //Está en memoria
                if(m_Log.isDebugEnabled()) m_Log.debug("Fichero correcto");
                FormFile fichero = (FormFile)ugVO.getFicheroFirma();
                byte[] doc = fichero.getFileData();
                if(m_Log.isDebugEnabled()) m_Log.debug(doc);
                ps.setBinaryStream(1, fichero.getInputStream(), fichero.getFileSize());
                ps.setString(2,fichero.getContentType());
            }
            ps.executeUpdate();
            ps.close();

            sql = "DELETE FROM A_OUS WHERE " +
                  ous_usu + "=" + ugVO.getCodUsuario() ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            Vector listaOrganizacionesUsuario = ugVO.getListaOrganizaciones();
            if(listaOrganizacionesUsuario.size() >0) {
                for(int i=0;i<listaOrganizacionesUsuario.size();i++) {
                    sql = "INSERT INTO A_OUS (" + ous_org + "," + ous_usu + ") VALUES (" +
                          listaOrganizacionesUsuario.elementAt(i) + "," + ugVO.getCodUsuario() + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    resultado = stmt.executeUpdate();
                    stmt.close();
                }
            }

            sql = "DELETE FROM A_UGO WHERE " +
                  ugo_usu + "=" + ugVO.getCodUsuario();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_AAU WHERE " +
                  aau_usu + "=" + ugVO.getCodUsuario();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_UAE WHERE " +
                  uae_usu + "=" + ugVO.getCodUsuario();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            Vector listaOrganizacionesGrupos = ugVO.getListaOrganizacionesGrupos();
            Vector listaAplicacionesGrupos = ugVO.getListaAplicacionesGrupos();
            Vector listaEntidadesGrupos = ugVO.getListaEntidadesGrupos();
            Vector listaGrupos = ugVO.getListaGrupos();
            if(listaOrganizacionesGrupos.size() >0) {
                for(int i=0;i<listaOrganizacionesGrupos.size();i++) {
                    sql = "INSERT INTO A_UGO (" + ugo_usu + "," + ugo_org + "," +
                          ugo_ent + "," + ugo_apl + "," + ugo_gru + ") VALUES (" + ugVO.getCodUsuario() + "," +
                          listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                          "," + listaAplicacionesGrupos.elementAt(i) + "," + listaGrupos.elementAt(i) + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    resultado = stmt.executeUpdate();
                    stmt.close();

                    sql = "SELECT * FROM A_AAU WHERE " +
                          aau_usu + "=" + ugVO.getCodUsuario() + " AND " + aau_apl + "=" +
                          listaAplicacionesGrupos.elementAt(i);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    String existeAAU = "no";
                    while(rs.next()) {
                        existeAAU = "si";
                    }
                    rs.close();
                    stmt.close();

                    if(existeAAU.equals("no")) {
                        sql = "INSERT INTO A_AAU (" + aau_usu + "," + aau_org + "," +
                              aau_ent + "," + aau_apl + ") VALUES (" + ugVO.getCodUsuario() + "," +
                              listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                              "," + listaAplicacionesGrupos.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();
                    }

                    sql = "SELECT * FROM A_UAE WHERE " +
                          uae_usu + "=" + ugVO.getCodUsuario() + " AND " + uae_org + "=" +
                          listaOrganizacionesGrupos.elementAt(i) + " AND " +
                          uae_ent + "=" + listaEntidadesGrupos.elementAt(i)+ " AND " +
                          uae_apl + "=" + listaAplicacionesGrupos.elementAt(i);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    String existeUAE = "no";
                    while(rs.next()) {
                        existeUAE = "si";
                    }
                    rs.close();
                    stmt.close();

                    if(existeUAE.equals("no")) {
                        sql = "INSERT INTO A_UAE (" + uae_usu + "," + uae_org + "," +
                              uae_ent + "," + uae_apl + ") VALUES (" + ugVO.getCodUsuario() + "," +
                              listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                              "," + listaAplicacionesGrupos.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();
                    }
                }
            }

            sql = "DELETE FROM A_UOU WHERE " +
                  uou_usu + "=" + ugVO.getCodUsuario();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            Vector listaOrganizacionesUOR = ugVO.getListaOrganizacionesUOR();
            Vector listaEntidadesUOR = ugVO.getListaEntidadesUOR();
            Vector listaUnidadesOrganicas = ugVO.getListaUnidadesOrganicas();
            Vector listaCargosUOR = ugVO.getListaCargosUOR();
            if(m_Log.isDebugEnabled()) m_Log.debug("LISTA ORG ......"+listaOrganizacionesUOR);
            if(m_Log.isDebugEnabled()) m_Log.debug("LISTA ENT ......"+listaEntidadesUOR);
            if(m_Log.isDebugEnabled()) m_Log.debug("LISTA UOR ......"+listaUnidadesOrganicas);
            if(m_Log.isDebugEnabled()) m_Log.debug("LISTA CAR ......"+listaCargosUOR);
            if(listaOrganizacionesUOR.size() >0) {
                for(int i=0;i<listaOrganizacionesUOR.size();i++) {
                    if (listaUnidadesOrganicas.elementAt(i).equals("-1")) {
//                        String jndi = UsuariosGruposManager.getInstance().obtenerJNDI((String)listaEntidadesUOR.get(i), (String)listaOrganizacionesUOR.get(i), String.valueOf("1"), params);
//                        String[] paramsNuevos = new String[7];
//                        paramsNuevos[0] = params[0];
//                        paramsNuevos[6] = jndi;
//                        Vector UORs = UORsManager.getInstance().getListaUOROrdenPorDesc(paramsNuevos);
                        Vector UORs = UORsManager.getInstance().getListaUOROrdenPorDesc(params);
                        for (int j=0;j<UORs.size(); j++) {
                            UORDTO dto = (UORDTO) UORs.get(j);
                            sql = "INSERT INTO A_UOU (" + uou_usu + "," + uou_org + "," +
                                  uou_ent + "," + uou_uor + "," + uou_car + ") VALUES (" + ugVO.getCodUsuario() + "," +
                                  listaOrganizacionesUOR.elementAt(i) + "," + listaEntidadesUOR.elementAt(i) +
                                  "," + dto.getUor_cod() + "," + listaCargosUOR.elementAt(i) + ")";
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            stmt = conexion.prepareStatement(sql);
                            resultado = stmt.executeUpdate();
                            stmt.close();
                        }
                    } else {
                        sql = "INSERT INTO A_UOU (" + uou_usu + "," + uou_org + "," +
                              uou_ent + "," + uou_uor + "," + uou_car + ") VALUES (" + ugVO.getCodUsuario() + "," +
                              listaOrganizacionesUOR.elementAt(i) + "," + listaEntidadesUOR.elementAt(i) +
                              "," + listaUnidadesOrganicas.elementAt(i) + "," + listaCargosUOR.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();
                    }
                }
            }



            // Inserción de las directivas del usuario
            setListaDirectivas(ugVO.getDirectivas(), ugVO.getCodUsuario(), conexion);
            /*** SI LA CLAVE HA SIDO MODIFICADA SE INSERTA EN LA TABLA DE ROTACIÓN DE PASSWORD **/
            if (claveModificada) {
                boolean exito = this.insertarPasswordRotacion(Integer.parseInt(ugVO.getCodUsuario()), EncriptacionContrasenha.getHashSHA_1(ugVO.getContrasena()), conexion);
                if (!exito) {
                    abd.rollBack(conexion);
                    resultado = -1;
                }
            }

            // Se inserta los posibles permisos que pueda tener el usuario sobre los procedimientos

            boolean restringir = PermisoProcRestringidoDAO.getInstance().insertarPermisosUsuarioProcedimientosRestringidos(ugVO.getProcedimientosRestringidos(),Integer.parseInt(ugVO.getCodUsuario()), conexion);
            if(!restringir){
                abd.rollBack(conexion);
                resultado = -1;
            }


        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransactAndCloseConn(abd,conexion);
        }
        return resultado;
    }



    // Modifica los datos basicos de usuario (nombre,contraseña,idioma,nif y correo electronico)
    public int modificarDatosBasicosUsuario(UsuariosGruposValueObject ugVO,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int maxCod = 0;
        int resultado = 0;
        try{          
          String[] parametros = (String []) params.clone();
          parametros[6] = campos.getString("CON.jndi");
          abd = new AdaptadorSQLBD(parametros);          
          conexion = abd.getConnection();
          abd.inicioTransaccion(conexion);
                sql = "UPDATE A_USU SET " + 
                      usu_idi + "=" + ugVO.getCodIdioma() + "," +
                      usu_pas + "='" + EncriptacionContrasenha.getHashSHA_1(ugVO.getContrasena()) + "'," +
                      usu_nom + "='" + ugVO.getNombreUsuario() + "',"+
                      usu_nif + "="+ ( (ugVO.getNif()!=null)?("'"+ugVO.getNif()+"'"):"NULL" ) + ","+                      
    			      usu_email +"='"+ugVO.getEmail()+ "'" +    			
                      " WHERE " + usu_cod + "=" + ugVO.getCodUsuario();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.executeUpdate();
                ps.close();
            }catch (Exception e){
                rollBackTransaction(abd,conexion,e);
                resultado = -1;
            }finally{
                commitTransactAndCloseConn(abd,conexion);
            }
            return resultado;
        }


    private String getContrasenhaUsuario(int codUsuario,Connection con){
        String clave = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT USU_PAS FROM A_USU WHERE USU_COD=" + codUsuario;
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                clave = rs.getString("USU_PAS");
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                    e.printStackTrace();
            }
        }
        return clave;
    }


    public int modificarUsuarioLocal(UsuariosGruposValueObject ugVO,String codOrg,String[] params, String portafirmas){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int maxCod = 0;
        int resultado = 0;
        boolean claveModificada =false;
        try{
            int CAMBIO_PASSWORD_OBLIGATORIA = 0;
            if(ugVO.getCambioPantallaObligatorio()!=null && "on".equalsIgnoreCase(ugVO.getCambioPantallaObligatorio()))        
                CAMBIO_PASSWORD_OBLIGATORIA = 1;
                
            //m_Log.debug("A por el OAD");
            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);


            String claveActual = this.getContrasenhaUsuario(Integer.parseInt(ugVO.getCodUsuario()),conexion);

            if(claveActual!=null && claveActual.equals(ugVO.getContrasena())){
                // La contraseña no se modifica
                sql = "UPDATE A_USU SET " + usu_idi + "=" + ugVO.getCodIdioma() + "," +
                  usu_log + "='" + ugVO.getLogin() +"'," +
                  usu_nom + "='" + ugVO.getNombreUsuario() + "',"+
                  usu_nif + "="+ ( (ugVO.getNif()!=null)?("'"+ugVO.getNif()+"'"):"NULL" ) + ","+
                  usu_firmante + "='"+ ugVO.getFirmante()+ "',"+
                  usu_email +"='"+ugVO.getEmail()+"'," + usu_blq + "="+ugVO.getEstado() + "," + 
                  "USU_CAMBIO_PASSWORD=" + CAMBIO_PASSWORD_OBLIGATORIA;
            }else{
                claveModificada = true;
                // Las contraseñas son distintas
                sql = "UPDATE A_USU SET " + usu_idi + "=" + ugVO.getCodIdioma() + "," +
                  usu_log + "='" + ugVO.getLogin() +"'," + usu_pas + "='" + EncriptacionContrasenha.getHashSHA_1(ugVO.getContrasena()) + "'," +
                  usu_nom + "='" + ugVO.getNombreUsuario() + "',"+
                  usu_nif + "="+ ( (ugVO.getNif()!=null)?("'"+ugVO.getNif()+"'"):"NULL" ) + ","+
                  usu_firmante + "='"+ ugVO.getFirmante()+ "',"+
                  usu_email +"='"+ugVO.getEmail()+"'," + usu_blq + "="+ugVO.getEstado() + "," + 
                  "USU_CAMBIO_PASSWORD=" + CAMBIO_PASSWORD_OBLIGATORIA; 
            }

            /*
            sql = "UPDATE A_USU SET " + usu_idi + "=" + ugVO.getCodIdioma() + "," +
                  usu_log + "='" + ugVO.getLogin() +"'," + usu_pas + "='" + EncriptacionContrasenha.getHashSHA_1(ugVO.getContrasena()) + "'," +
                  usu_nom + "='" + ugVO.getNombreUsuario() + "',"+
                  usu_nif + "="+ ( (ugVO.getNif()!=null)?("'"+ugVO.getNif()+"'"):"NULL" ) + ","+
                  usu_firmante + "='"+ ugVO.getFirmante()+ "',"+
                  usu_email +"='"+ugVO.getEmail()+"'," + usu_blq + "="+ugVO.getEstado() + " "; */

            if (!ugVO.getFechaEliminado().equals("") || ugVO.getFechaEliminado()!=null) sql +=",USU_FBA=NULL";
            if ((ugVO.getFicheroFirma()!=null) || (ugVO.getFicheroFirmaFisico()==null)) sql += ","+usu_firma + "=?," + usu_tipo_firma +"=?";
            
            if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                if (ugVO.getBuzonFirma() != null && !"".equals(ugVO.getBuzonFirma().trim())) {
                    sql += ", "+usu_buzfir + "= '"+ ugVO.getBuzonFirma().trim() + "' ";
                } else {
                    sql += ", "+usu_buzfir + " = NULL ";
                }
               
            }
            
            sql += " WHERE " + usu_cod + "=" + ugVO.getCodUsuario();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement ps = conexion.prepareStatement(sql);
            if (ugVO.getFicheroFirmaFisico()==null){ //Se ha eliminado el fichero
                if(m_Log.isDebugEnabled()) m_Log.debug("Fichero eliminado");
                ps.setNull(1,java.sql.Types.LONGVARBINARY);
                ps.setNull(2,java.sql.Types.VARCHAR);
            } else if (ugVO.getFicheroFirma()!=null){ //Está en memoria
                if(m_Log.isDebugEnabled()) m_Log.debug("Fichero correcto");
                FormFile fichero = (FormFile)ugVO.getFicheroFirma();
                byte[] doc = fichero.getFileData();
                if(m_Log.isDebugEnabled()) m_Log.debug(doc);
                ps.setBinaryStream(1, fichero.getInputStream(), fichero.getFileSize());
                ps.setString(2,fichero.getContentType());
            }
            ps.executeUpdate();
            ps.close();


            sql = "DELETE FROM A_OUS WHERE " +
                  ous_usu + "=" + ugVO.getCodUsuario() + " AND " + ous_org + "=" + codOrg;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            Vector listaOrganizacionesUsuario = ugVO.getListaOrganizaciones();
            if(listaOrganizacionesUsuario!=null && listaOrganizacionesUsuario.size() >0) {
                for(int i=0;i<listaOrganizacionesUsuario.size();i++) {
                    sql = "INSERT INTO A_OUS (" + ous_org + "," + ous_usu + ") VALUES (" +
                            listaOrganizacionesUsuario.elementAt(i) + "," + ugVO.getCodUsuario() + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    resultado = stmt.executeUpdate();
                    stmt.close();
                }
            }

            sql = "DELETE FROM A_UGO WHERE " +
                  ugo_usu + "=" + ugVO.getCodUsuario() +" AND " + ugo_org + "=" + codOrg;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();

            Vector listaOrganizacionesGrupos = ugVO.getListaOrganizacionesGrupos();
            Vector listaAplicacionesGrupos = ugVO.getListaAplicacionesGrupos();
            Vector listaEntidadesGrupos = ugVO.getListaEntidadesGrupos();
            Vector listaGrupos = ugVO.getListaGrupos();
            if(listaOrganizacionesGrupos != null && listaOrganizacionesGrupos.size() >0) {
                for(int i=0;i<listaOrganizacionesGrupos.size();i++) {
                    sql = "INSERT INTO A_UGO (" + ugo_usu + "," + ugo_org + "," +
                          ugo_ent + "," + ugo_apl + "," + ugo_gru + ") VALUES (" + ugVO.getCodUsuario() + "," +
                          listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                          "," + listaAplicacionesGrupos.elementAt(i) + "," +
                          listaGrupos.elementAt(i) + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    resultado = stmt.executeUpdate();
                    stmt.close();

                    sql = "SELECT * FROM A_AAU WHERE " +
                          aau_usu + "=" + ugVO.getCodUsuario() + " AND " + aau_apl + "=" +
                          listaAplicacionesGrupos.elementAt(i);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    String existeAAU = "no";
                    while(rs.next()) {
                        existeAAU = "si";
                    }
                    rs.close();
                    stmt.close();

                    if(existeAAU.equals("no")) {
                        sql = "INSERT INTO A_AAU (" + aau_usu + "," + aau_org + "," +
                              aau_ent + "," + aau_apl + ") VALUES (" + ugVO.getCodUsuario() + "," +
                              listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                              "," + listaAplicacionesGrupos.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();
                    }

                    sql = "SELECT * FROM A_UAE WHERE " +
                          uae_usu + "=" + ugVO.getCodUsuario() + " AND " + uae_org + "=" +
                          listaOrganizacionesGrupos.elementAt(i) + " AND " +
                          uae_ent + "=" + listaEntidadesGrupos.elementAt(i)+ " AND " +
                          uae_apl + "=" + listaAplicacionesGrupos.elementAt(i);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    String existeUAE = "no";
                    while(rs.next()) {
                        existeUAE = "si";
                    }
                    rs.close();
                    stmt.close();

                    if(existeUAE.equals("no")) {
                        sql = "INSERT INTO A_UAE (" + uae_usu + "," + uae_org + "," +
                              uae_ent + "," + uae_apl + ") VALUES (" + ugVO.getCodUsuario() + "," +
                              listaOrganizacionesGrupos.elementAt(i) + "," + listaEntidadesGrupos.elementAt(i) +
                              "," + listaAplicacionesGrupos.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();
                    }
                }
            }

            sql = "DELETE FROM A_UOU WHERE " +
                  uou_usu + "=" + ugVO.getCodUsuario() + " AND " + uou_org + "=" + codOrg;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            Vector listaOrganizacionesUOR = ugVO.getListaOrganizacionesUOR();
            Vector listaEntidadesUOR = ugVO.getListaEntidadesUOR();
            Vector listaUnidadesOrganicas = ugVO.getListaUnidadesOrganicas();
            Vector listaCargosUOR = ugVO.getListaCargosUOR();
            if(listaOrganizacionesUOR!=null && listaOrganizacionesUOR.size() >0) {
                for(int i=0;i<listaOrganizacionesUOR.size();i++) {
                    if (listaUnidadesOrganicas.elementAt(i).equals("-1")) {
//                        String jndi = UsuariosGruposManager.getInstance().obtenerJNDI((String)listaEntidadesUOR.get(i), (String)listaOrganizacionesUOR.get(i), String.valueOf("1"), params);
//                        String[] paramsNuevos = new String[7];
//                        paramsNuevos[0] = params[0];
//                        paramsNuevos[6] = jndi;
//                        Vector UORs = UORsManager.getInstance().getListaUOROrdenPorDesc(paramsNuevos);
                        Vector UORs = UORsManager.getInstance().getListaUOROrdenPorDesc(params);
                        for (int j=0;j<UORs.size(); j++) {
                            UORDTO dto = (UORDTO) UORs.get(j);
                            sql = "INSERT INTO A_UOU (" + uou_usu + "," + uou_org + "," +
                                  uou_ent + "," + uou_uor + "," + uou_car + ") VALUES (" + ugVO.getCodUsuario() + "," +
                                  listaOrganizacionesUOR.elementAt(i) + "," + listaEntidadesUOR.elementAt(i) +
                                  "," + dto.getUor_cod() + "," + listaCargosUOR.elementAt(i) + ")";
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            stmt = conexion.prepareStatement(sql);
                            resultado = stmt.executeUpdate();
                            stmt.close();
                        }
                    } else {
                        sql = "INSERT INTO A_UOU (" + uou_usu + "," + uou_org + "," +
                              uou_ent + "," + uou_uor + "," + uou_car + ") VALUES (" + ugVO.getCodUsuario() + "," +
                              listaOrganizacionesUOR.elementAt(i) + "," + listaEntidadesUOR.elementAt(i) +
                              "," + listaUnidadesOrganicas.elementAt(i) + "," + listaCargosUOR.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt = conexion.prepareStatement(sql);
                        resultado = stmt.executeUpdate();
                        stmt.close();
                    }
                }
            }

            // Inserción de las directivas del usuario
            setListaDirectivas(ugVO.getDirectivas(), ugVO.getCodUsuario(), conexion);
             if(claveModificada){
                boolean exito = this.insertarPasswordRotacion(Integer.parseInt(ugVO.getCodUsuario()), EncriptacionContrasenha.getHashSHA_1(ugVO.getContrasena()),conexion);
                if(!exito){
                    abd.rollBack(conexion);
                    resultado = -1;
                }
             }


            // Se inserta los posibles permisos que pueda tener el usuario sobre los procedimientos
            boolean restringir = PermisoProcRestringidoDAO.getInstance().insertarPermisosUsuarioProcedimientosRestringidos(ugVO.getProcedimientosRestringidos(),Integer.parseInt(ugVO.getCodUsuario()), conexion);
            if(!restringir){
                abd.rollBack(conexion);
                resultado = -1;
            }
            
            
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransactAndCloseConn(abd,conexion);
        }
        return resultado;
    }



   /**
     * Tras una modificación de la password del un determinado usuario, se inserta la misma en la tabla de rotación de passwored
     * @param codUsuario: Código del usuario
     * @param password: Password
     * @param con: conexión
     * @return boolean
     */
    public int getCodigoPasswordRotacion(Connection con) {
        int salida = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MAX(COD_PASS) AS NUM FROM A_USU_ROTACION_PASS";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                salida = rs.getInt("NUM") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return salida;
    }

    /**
     * Tras una modificación de la password del un determinado usuario, se inserta la misma en la tabla de rotación de passwored
     * @param codUsuario: Código del usuario
     * @param password: Password
     * @param con: conexión
     * @return boolean
     */
    public boolean insertarPasswordRotacion(int codUsuario, String password, Connection con) {
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("techserver");
            int numero = Integer.parseInt(bundle.getString(ConstantesDatos.PROPIEDAD_NUMERO_PASSWORD_ROTACION));
            String sql = "SELECT COUNT(*) AS NUM FROM A_USU_ROTACION_PASS WHERE USU_COD = ?";
            m_Log.debug(sql);
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, codUsuario);
            rs = ps.executeQuery();

            int num = 0;
            while (rs.next()) {
                num = rs.getInt("NUM");
            }

            ps.close();
            rs.close();
            if (num >= numero) {
                sql = "DELETE FROM A_USU_ROTACION_PASS WHERE USU_COD=? AND FEC_ALTA=(SELECT MIN(FEC_ALTA) FROM A_USU_ROTACION_PASS WHERE USU_COD=?)";
                m_Log.debug(sql);
                ps = con.prepareStatement(sql);
                i = 1;
                ps.setInt(i++, codUsuario);
                ps.setInt(i++, codUsuario);
                int rows = ps.executeUpdate();
                m_Log.debug("Número pasword eliminada del usuario " + codUsuario + " es " + rows);
                ps.close();
            }

            int codigo = this.getCodigoPasswordRotacion(con);
            sql = "INSERT INTO A_USU_ROTACION_PASS(COD_PASS,USU_PASS,FEC_ALTA,USU_COD) VALUES(?,?,?,?)";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            i = 1;
            ps.setInt(i++, codigo);
            ps.setString(i++, password);
            ps.setTimestamp(i++, DateOperations.toTimestamp(Calendar.getInstance()));
            ps.setInt(i++, codUsuario);

            int rows = ps.executeUpdate();
            m_Log.debug("Insertada password en tabla de rotación de password para usuario " + codUsuario + " es " + rows);
            if (rows == 1) {
                exito = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            exito = false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exito;
    }

    public int eliminarUsuario(String codUsuario,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";
        int resultado = 0;
        int sePuedeEliminar = esEliminacionSegura(codUsuario,params);
        if (sePuedeEliminar==0){
        try{

            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);

            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "DELETE FROM A_AAU WHERE " + aau_usu +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_OUS WHERE " + ous_usu +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_RPU WHERE " + rpu_usu +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_UAE WHERE " + uae_usu +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_UGO WHERE " + ugo_usu +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_UOU WHERE " + uou_usu +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_USU_ROTACION_PASS WHERE " + usu_rotacion_pass_cod +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_USU WHERE " + usu_cod +
                  "=" + codUsuario ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_USU_DIR WHERE USU=" + codUsuario;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransactAndCloseConn(abd,conexion);
        }
        } else if (sePuedeEliminar>0){
            try {
                String[] parametros = (String[]) params.clone();
                parametros[6] = campos.getString("CON.jndi");
                abd = new AdaptadorSQLBD(parametros);

                conexion = abd.getConnection();
                abd.inicioTransaccion(conexion);
                sql = "UPDATE A_USU SET USU_FBA=" + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE , params)
                        + " WHERE " + usu_cod + "=" + codUsuario ;
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                stmt = conexion.prepareStatement(sql);
                resultado = stmt.executeUpdate();
                if (resultado==1) resultado=-99;
                stmt.close();
            } catch (Exception e) {
                rollBackTransaction(abd, conexion, e);
                resultado = -1;
            } finally {
                commitTransactAndCloseConn(abd, conexion);
            }
        }
        return resultado;
    }

    public int insertarGrupo(UsuariosGruposValueObject ugVO,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int maxCod = 0;
        int resultado = 0;
        try{
            //m_Log.debug("A por el OAD");
            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "SELECT " + gru_cod + " FROM A_GRU ORDER BY 1";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                maxCod = rs.getInt(gru_cod);
                maxCod++;
            }
            rs.close();
            stmt.close();
            sql = "INSERT INTO A_GRU (" + gru_cod + "," + gru_nom + ") VALUES (" +
                  maxCod + ",'" + ugVO.getNombreGrupo() + "')";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();

        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransactAndCloseConn(abd,conexion);
        }
        return resultado;
    }

    public int eliminarGrupo(String codGrupo,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";
        int resultado = 0;
        try{
            //m_Log.debug("A por el OAD");
            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "DELETE FROM A_UGO WHERE " + ugo_gru +
                  "=" + codGrupo ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_RPG WHERE " + rpg_gru +
                  "=" + codGrupo ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            sql = "DELETE FROM A_GRU WHERE " + gru_cod +
                  "=" + codGrupo ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransactAndCloseConn(abd,conexion);
        }
        return resultado;
    }

    public int modificarGrupo(UsuariosGruposValueObject ugVO,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";
        int maxCod = 0;
        int resultado = 0;
        try{
            //m_Log.debug("A por el OAD");
            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "UPDATE A_GRU SET " + gru_nom + "='" + ugVO.getNombreGrupo() +
                  "' WHERE " + gru_cod + "=" + ugVO.getCodGrupo();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();

        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransactAndCloseConn(abd,conexion);
        }
        return resultado;
    }

    public String  obtenerJNDI(String codEntidad, String codOrganizacion, String codAplicacion,
                               String[] params){
        
        String jndi = "";
        if (codEntidad!=null && !"".equals(codEntidad) && codAplicacion!=null && !"".equals(codAplicacion)){
            ParametrosBDVO parametrosBD = (ParametrosBDVO)CacheDatosFactoria.getImplParametrosBD().getDatoClaveUnica(codOrganizacion,
                    codEntidad,codAplicacion);
            
            if (parametrosBD!=null) 
                jndi = parametrosBD.getJndi();
        } else {
            SortedMap <ArrayList<String>,ParametrosBDVO> listaParametrosBD = (SortedMap <ArrayList<String>,ParametrosBDVO>) CacheDatosFactoria.getImplParametrosBD().getDatos();
            if (listaParametrosBD!=null) {
                for(Map.Entry<ArrayList<String>,ParametrosBDVO> entry : listaParametrosBD.entrySet()) {
                    ParametrosBDVO parametrosBD = entry.getValue();
                    if (String.valueOf(parametrosBD.getCodOrganizacion()).equals(codOrganizacion)) {
                        jndi = parametrosBD.getJndi();
                        break;
                    }
                }
            }
        }
        return jndi;
    }

    public Vector getListaCargos(UsuariosGruposValueObject u,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector listaCargos = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            sql = "SELECT " + car_cod + "," + car_nom + " FROM A_CAR ORDER BY " + car_nom;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                elemListVO = new ElementoListaValueObject(rs.getString(car_cod),rs.getString(car_nom),orden++);
                listaCargos.addElement(elemListVO);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return listaCargos;
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

    private void commitTransactAndCloseConn(AdaptadorSQLBD bd,Connection con){
        try{
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        }catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage());
        }
    }

    public int cuentaDocumentosPendientesDeFirmarPorUsuario(int codigoUsuario, String[] params) {
        return cuentaDeTablaPorCriterio(params, "E_PORTAFIRMAS", "(USU_COD="+codigoUsuario+") AND (FIR_EST='O')");
    }

    public int cuentaPlantillasParaFirmarPorUsuario(int codigoUsuario, String[] params) {
        return cuentaDeTablaPorCriterio(params, "E_DOT_FIR", "(USU_COD="+codigoUsuario+")");
    }

    public int cuentaDelegacionesHaciaUsuario(int codigoUsuario, String[] params) {
        return cuentaDeTablaPorCriterio(params, GlobalNames.ESQUEMA_GENERICO + "USU_FIR_DEL", "(USU_DELEGADO="+codigoUsuario+")");
    }

    private int cuentaDeTablaPorCriterio(String[] params, String tabla, String where){
      AdaptadorSQLBD abd = null;
      Connection conexion = null;
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "SELECT COUNT(*) FROM "+tabla+" WHERE ("+where+")";
      int result = 0;

      try{
        if(m_Log.isDebugEnabled()) m_Log.debug("UsuariosGruposDAO.cuentaTablaPorCriterio() PARAMS = {"+
            BasicTypesOperations.toString(params,",") + "}");
        abd = new AdaptadorSQLBD(params);
        conexion = abd.getConnection();
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            result = rs.getInt(1);
        }//if
      }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
      }
      if (m_Log.isDebugEnabled()) m_Log.debug("UsuariosGruposDAO.cuentaTablaPorCriterio() Result = "+ result);
      return result;
    }

    /*
     * Miramos los datos asociados al usuario. Si el usuario ha tramitado algún expediente o ha dado de alta alguna
     * anotación o la ha modificado, no se puede eliminar físicamente porque perderíamos información.
     * */
    private int esEliminacionSegura (String codUsuario,String[] params){
         
        Vector<String> jndis = new Vector<String>();
        int resultado = 0;
        String[] parametros = (String []) params.clone();
        
        SortedMap<ArrayList<String>,ParametrosBDVO> listaParametrosBD = (SortedMap<ArrayList<String>,ParametrosBDVO>)CacheDatosFactoria.getImplParametrosBD().getDatos();
    
        for(Map.Entry<ArrayList<String>,ParametrosBDVO> entry : listaParametrosBD.entrySet()) {
            ParametrosBDVO parametrosBD = entry.getValue();
            if ((parametrosBD.getCodAplicacion() == 1 || parametrosBD.getCodAplicacion() == 4) &&
                    !jndis.contains(parametrosBD.getJndi()))
                jndis.add(parametrosBD.getJndi());
        }

        int i=0;
        while (resultado==0 && i<jndis.size()){
            parametros[6] = jndis.get(i);
            resultado=resultado + cuentaDeTablaPorCriterio (parametros,"E_CRO","CRO_USU="+codUsuario);
            resultado=resultado + cuentaDeTablaPorCriterio (parametros,"R_RES","RES_USU="+codUsuario);
            resultado=resultado + cuentaDeTablaPorCriterio (parametros,"R_HISTORICO","USUARIO="+codUsuario);
            i++;
        }
        
        return resultado;
    }

    /* ******************************************************** */

    /**
     * Funcion que devuelve el mail de un usuario
     */
    public String getMailByUsuario(int codigoUsuario, String []params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String mail = "";

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE " +
                  usu_cod + " = " + codigoUsuario;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                mail = rs.getString(1);
            }//if
            
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
                try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                    abd.devolverConexion(conexion);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }
        return mail;
    }

    /**
     * Funcion que devuelve el codigo del usuario en el que delega el actual en caso de que exista
     * y -1 en otro caso
     */
    public int getUsuarioDelegado(int codigoUsuario, String []params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int delegado = -1;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT USU_DELEGADO FROM " + GlobalNames.ESQUEMA_GENERICO + "USU_FIR_DEL WHERE " +
                  usu_cod + " = " + codigoUsuario;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                delegado = rs.getInt(1);
            }//if
            
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
                try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                    abd.devolverConexion(conexion);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }
        return delegado;
    }
    
    
     /**
     * Devuelve el número de usuarios existentes en la base de datos
     * @param params: Parámetros de conexión a la BD
     * @return int
     */
     public int countNumUsers(String[] params){
         
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";        
        int resultado = 0;
        try{
                
            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);            
            conexion = abd.getConnection();
            
            Config configForm = ConfigServiceHelper.getConfig("formulariosPdf");
            String usuarioSW = configForm.getString("usuarioParaSGE");
                    
            sql = "SELECT count(*) as num FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_LOG!='" + usuarioSW + "'" ;
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                resultado = rs.getInt(1);            
            }
            
                  
        }catch (Exception e){           
            m_Log.error(e.getMessage());
        }finally{
            
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
            }
            catch(Exception e)
            { m_Log.error(e.getMessage()); }
         }
        return resultado;
    }   

    public Vector getUsuariosFirmantesUnidadCargo(String codOrg, String uor, String cargo, String[] params)
            throws TechnicalException {
        
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String cargoTodos = CargosDAO.getInstance().getCargoPorCodigoVisible("TD", params).getUor_cod();
        Vector<UsuariosGruposValueObject> resultado = new Vector<UsuariosGruposValueObject>();
        UsuariosGruposValueObject user = new UsuariosGruposValueObject();
        
        try{
            
           
                abd = new AdaptadorSQLBD(params);
            
            conexion = abd.getConnection();
            sql = "SELECT DISTINCT USU_COD,USU_NOM, USU_EMAIL from "+GlobalNames.ESQUEMA_GENERICO+"A_USU LEFT JOIN "+
                    GlobalNames.ESQUEMA_GENERICO+"A_UOU ON "
                    + "(UOU_USU=USU_COD) LEFT JOIN A_UOR ON (UOR_COD=UOU_UOR) LEFT JOIN A_CAR ON "
                    + "(CAR_COD=UOU_CAR) WHERE UOU_ORG=? AND  USU_FIRMANTE=1 ";
            if (uor!=null && !uor.equalsIgnoreCase("")){
                sql += " AND UOR_COD=?  ";
            }
            
            if (cargo!=null && !cargo.equalsIgnoreCase("")){
                sql += "AND (CAR_COD=? OR CAR_COD=" + cargoTodos + ")"; //EL USUARIO DISPONE DEL CARGO EN CUESTION O DE TODOS
            }
            
            sql+=" ORDER BY USU_NOM";
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            int i=1;
            stmt.setInt(i++, Integer.parseInt(codOrg));
            if (uor!=null && !uor.equalsIgnoreCase("")){
               stmt.setInt(i++, Integer.parseInt(uor));
            }
            if (cargo!=null && !cargo.equalsIgnoreCase("")){
                stmt.setInt(i++, Integer.parseInt(cargo));
            }
            
            rs = stmt.executeQuery();
            while(rs.next()) {
                user = new UsuariosGruposValueObject();
                user.setCodUsuario(rs.getString("USU_COD"));
                user.setNombreUsuario(rs.getString("USU_NOM"));
                user.setEmail(rs.getString("USU_EMAIL"));
                resultado.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(abd, conexion);
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
        return resultado;       
        
    }
    
     /**
     * Devuelve el número de usuarios existentes en la base de datos. Tiene en cuenta que se
     * puede filtrar por login y nombre
     * @param login: Login del usuario
     * @param nombre: Nombre del usuario
     * @param params: Parámetros de conexión a la BD
     * @return int
     */
     public int countNumUsers(String login,String nombre,Connection conexion){
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";        
        int resultado = 0;
        try{
              
            Config configForm = ConfigServiceHelper.getConfig("formulariosPdf");
            String usuarioSW = configForm.getString("usuarioParaSGE");
                    
            sql = "SELECT count(*) as num FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_LOG!='" + usuarioSW + "'" ;
            if(login!=null && !"".equals(login))
                sql = sql + " AND UPPER(USU_LOG) LIKE ('%" + login.toUpperCase() + "%')";
            
            if(nombre!=null && !"".equals(nombre))
                sql = sql + " AND UPPER(USU_NOM) LIKE ('%" + nombre.toUpperCase() + "%')";
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                resultado = rs.getInt(1);            
            }
            
                  
        }catch (Exception e){           
            m_Log.error(e.getMessage());
        }finally{
            
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
            }
            catch(Exception e)
            { m_Log.error(e.getMessage()); }
         }
        return resultado;
    }   
     
   
     
     /**
     * Recupera la lista de usuarios de una determinada organización
     * @param codOrg: Código de la organización
     * @param login: Login del usuario
     * @param nombre: Nombre del usuario
     * @param params: Parámetros de conexión a la BBDD
     * @return  Vector con la lista de usuarios recuperados
     */
    public Vector getUsuariosLocalFiltroBusqueda(String codOrg,String login,String nombre,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            from = usu_cod + "," + usu_log + "," + usu_nom + ","+
                    abd.convertir("USU_FBA", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+" AS FBAJA ";
            where = ous_org + "=" + codOrg + " AND UPPER(USU_LOG)!='SW.FORMULARIOS'";
            if(login!=null && !"".equals(login))
                where = where + " AND UPPER(USU_LOG) LIKE ('%" + login.toUpperCase() + "%')";
            
            if(nombre!=null && !"".equals(nombre))
                where = where + " AND UPPER(USU_NOM) LIKE ('%" + nombre.toUpperCase() + "%')";            
            
            String[] join = new String[5];
            join[0] = GlobalNames.ESQUEMA_GENERICO + "A_USU";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_ous";
            join[3] = GlobalNames.ESQUEMA_GENERICO + "a_usu." + usu_cod + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ous." +
                      ous_usu;
            join[4] = "false";
            sql = abd.join(from,where,join);
            String parametros[] = {"3","3"};
            sql += abd.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("getUsuariosLocal sql: " + sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                GeneralValueObject g = new GeneralValueObject();
                String codUsuario = rs.getString(usu_cod);
                g.setAtributo("codUsuario",codUsuario);
                String loginr = rs.getString(usu_log);
                g.setAtributo("login",loginr);
                String nombreUsuario = rs.getString(usu_nom);
                g.setAtributo("nombreUsuario",nombreUsuario);
                String fechaEliminado = rs.getString("FBAJA");
                g.setAtributo("fechaEliminado",fechaEliminado);
                lista.addElement(g);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return lista;
    }
    
    
    
    public Vector getUsuariosFiltroBusqueda(String login,String nombre,String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            sql = "SELECT " + abd.convertir("USU_FBA", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                  " AS FBAJA,"+ usu_cod + "," + usu_log + "," + usu_nom + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU";
            
            String where = " UPPER(USU_LOG)!='SW.FORMULARIOS'";
            if(login!=null && !"".equals(login))
                where = where + " AND UPPER(USU_LOG) LIKE ('%" + login.toUpperCase() + "%')";
            
            if(nombre!=null && !"".equals(nombre))
                where = where + " AND UPPER(USU_NOM) LIKE ('%" + nombre.toUpperCase() + "%')";            
                    
            String order = " ORDER BY 3";
            
            sql = sql + order;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                GeneralValueObject g = new GeneralValueObject();
                String codUsuario = rs.getString(usu_cod);
                g.setAtributo("codUsuario",codUsuario);
                String loginr = rs.getString(usu_log);
                g.setAtributo("login",loginr);
                String nombreUsuario = rs.getString(usu_nom);
                g.setAtributo("nombreUsuario",nombreUsuario);
                String fechaEliminado = rs.getString("FBAJA");
                g.setAtributo("fechaEliminado",fechaEliminado);
                lista.addElement(g);
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return lista;
    } 
    
    
    
    
     public boolean tienePermisoDirectiva(String codDirectiva, int codUsuario, Connection con)
            throws TechnicalException{

        m_Log.debug("UsuariosGruposDAO.tienePermisoDirectiva");        
        Statement st = null;
        ResultSet rs = null;
        boolean tienePermiso = false;

        try {

            String sql = "SELECT DIR" +
                        " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU_DIR A_USU_DIR" +
                        " WHERE A_USU_DIR.USU=" + codUsuario + 
                        " AND A_USU_DIR.DIR='" + codDirectiva + "'";
            m_Log.debug(sql);            
            st = con.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next()) {
                tienePermiso = true;
            }

        } catch (Exception ex) {
            m_Log.error("NO SE PUDO COMPROBAR LA DIRECTIVA " + codDirectiva);
            ex.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);            
        }
        return tienePermiso;
    }

 
     
     /********* NUEVO *********************/

     /**
      * Recupera una lista de unidades organicas sobre las que el usuario tiene permiso.
      * @param u: Objeto de la clase UsuariosGruposValueObject con los datos del usuario
      * @param params
      * @return 
      */
     public ArrayList<String> getListaCodigosUnidadesOrganicasUsuario(int codUsuario,int codOrganizacion,Connection con){      
         ArrayList<String> lista = new ArrayList<String>();
         try{
            lista = getListaCodigosUnidadesOrganicasUsuario(new Integer(codUsuario), new Integer(codOrganizacion), null, con);
         } catch (Exception e){
             if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage(), e);
         }
         
         return lista;
     }
     
     public ArrayList<String> getListaCodigosUnidadesOrganicasUsuario(Integer codUsuario,Integer codOrganizacion,Integer entidad, Connection con) throws Exception{                
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        
        ArrayList<String> listaUnidOrganicas = new ArrayList<String>();

        try{
            
            sql = "SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU,a_uor " + 
                  "WHERE UOU_USU=? AND UOU_ORG=? AND ";
            if(entidad != null){
                sql += "UOU_ENT = ? AND ";
            }
            sql += GlobalNames.ESQUEMA_GENERICO + "a_uou.UOU_UOR=a_uor.UOR_COD";                          
            /*
            sql = "SELECT UOU_UOR " + 
                  "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU,a_uor," + GlobalNames.ESQUEMA_GENERICO + "a_org," + GlobalNames.ESQUEMA_GENERICO + "a_ent,a_car " + 
                  "WHERE UOU_USU=? AND UOU_ORG=? AND " + GlobalNames.ESQUEMA_GENERICO + "a_uou.UOU_UOR=a_uor.UOR_COD AND " + GlobalNames.ESQUEMA_GENERICO + "a_uou.UOU_ORG=" + GlobalNames.ESQUEMA_GENERICO + "a_org.ORG_COD AND " + 
                    GlobalNames.ESQUEMA_GENERICO + "a_uou.UOU_ORG=" + GlobalNames.ESQUEMA_GENERICO + "a_ent.ENT_ORG AND " + GlobalNames.ESQUEMA_GENERICO + "a_uou.UOU_ENT=" + GlobalNames.ESQUEMA_GENERICO + "a_ent.ENT_COD AND " + 
                    GlobalNames.ESQUEMA_GENERICO + "A_UOU.UOU_CAR=A_CAR.CAR_COD(+)  ORDER BY 6,1,2";
            */
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);            
            ps = con.prepareStatement(sql);
            ps.setInt(1,codUsuario);
            ps.setInt(2,codOrganizacion);
            if(entidad != null){
                ps.setInt(3, entidad);
            }
            
            rs = ps.executeQuery();
            
            while(rs.next()) {                
                listaUnidOrganicas.add(rs.getString("UOU_UOR"));
            }
        }catch (Exception e){
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage(), e);
            throw e;
        } finally {
            try{
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();            
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        return listaUnidOrganicas;
    }
     
     public boolean existeUsuLogin(String usuLogin, Connection con) throws SQLException {
         PreparedStatement ps = null;
         ResultSet rs = null;
         StringBuilder query = null; 
         int count = -1;
         
         try {
             query = new StringBuilder("SELECT COUNT(*) FROM ");
             query.append(GlobalNames.ESQUEMA_GENERICO).append("A_USU WHERE UPPER(USU_LOG) = UPPER(?)");
             m_Log.debug(String.format("Query = %s", query.toString()));
             m_Log.debug(String.format("Parámetros = %s", usuLogin));
             
             ps = con.prepareStatement(query.toString());
             ps.setString(1, usuLogin);
             rs = ps.executeQuery();
             if(rs.next()) {
                 count = rs.getInt(1);
             }
         } catch (SQLException sqle) {
             m_Log.error("Ha ocurrido un error al comprobar la existencia de un usuario con el login " + usuLogin, sqle);
             throw sqle;
         } finally {
             try {
                 if(rs != null) rs.close();
                 if(ps != null) ps.close();
             } catch (Exception e) {
                 m_Log.error("Ha ocurrido un error al liberar recursos de base de datos", e);
             }
         }
         
         return count > 0; 
     }
}