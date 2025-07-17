// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.RolVO;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.MunicipiosDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.ProvinciasDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

 
public class InteresadosDAO  {
  private static InteresadosDAO instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(InteresadosDAO.class.getName());

  protected static String ext_mun;
  protected static String ext_eje;
  protected static String ext_num;
  protected static String ext_pro;
  protected static String ext_ter;
  protected static String ext_nvr;
  protected static String ext_rol;
  protected static String ext_dot;
  protected static String ext_notificacion_electronica;

//Interesados
	protected static String sql_intecodUor;
	protected static String sql_intetipo;
	protected static String sql_inteano;
	protected static String sql_intenumero;
	protected static String sql_intecoddepartamento;
	protected static String sql_intecodTercero;
	protected static String sql_inteverTercero;
	protected static String sql_intecodDomicilio;
	protected static String sql_interolTercero;
	
  protected static String hte_ter;
  protected static String hte_nvr;
  protected static String hte_pa1;
  protected static String hte_ap1;
  protected static String hte_pa2;
  protected static String hte_ap2;
  protected static String hte_nom;
  protected static String hte_tlf;
  protected static String hte_dce;
  protected static String hte_tip;
  protected static String hte_doc;
  

  protected static String dom_cod;
  protected static String dom_nml;

  protected static String dsu_cod;
  protected static String dsu_pai;
  protected static String dsu_prv;
  protected static String dsu_mun;
  protected static String dsu_via;
  protected static String dsu_nud;
  protected static String dsu_led;
  protected static String dsu_nuh;
  protected static String dsu_leh;
  protected static String dsu_blq;
  protected static String dsu_por;

  protected static String dpo_dom;
  protected static String dpo_dsu;
  protected static String dpo_esc;
  protected static String dpo_plt;
  protected static String dpo_pta;

  protected static String via_pai;
  protected static String via_prv;
  protected static String via_mun;
  protected static String via_cod;
  protected static String via_tvi;
  protected static String via_nom;

  protected static String tvi_des;
  protected static String tvi_abr;
  protected static String tvi_cod;

  protected static String dnn_dom;
  protected static String dnn_dmc;
  protected static String dnn_tvi;
  protected static String dnn_nud;
  protected static String dnn_led;
  protected static String dnn_nuh;
  protected static String dnn_leh;
  protected static String dnn_blq;
  protected static String dnn_por;
  protected static String dnn_esc;
  protected static String dnn_plt;
  protected static String dnn_pta;
  protected static String dnn_pai;
    protected static String dnn_prv;
    protected static String dnn_mun;
    protected static String dnn_via;
    protected static String dnn_cpo;

  

  protected static String rol_mun;
  protected static String rol_pro;
  protected static String rol_cod;
  protected static String rol_des;
  protected static String rol_pde;
  
  protected static String rol_codR;
  protected static String rol_desR;
  protected static String rol_pdeR;
  protected static String grupo;
  protected static String grupoPorDefecto; // Id del grupo de roles por defecto (para registro)
                                
  
  protected static String exp_mun;
  protected static String exp_pro;
  protected static String exp_eje;
  protected static String exp_num;


  //para registro
  
  protected static String res_dep;
  protected static String res_uor;
  protected static String res_tip;
  protected static String res_eje;
  protected static String res_num;

  protected static String r_cod;
  protected static String r_des;
  protected static String r_pde;
  protected static String idPais;
  protected static String idProvincia;
  protected static String idMunicipio;
  protected static String pais;
  protected static String provincia;
  protected static String propais;
  protected static String municipio;
  protected static String munpais;
  protected static String munipro;
  

  
  protected InteresadosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    ext_mun = m_ConfigTechnical.getString("SQL.E_EXT.codMunicipio");
    ext_eje = m_ConfigTechnical.getString("SQL.E_EXT.ano");
    ext_num = m_ConfigTechnical.getString("SQL.E_EXT.numero");
    ext_pro = m_ConfigTechnical.getString("SQL.E_EXT.codProcedimiento");
    ext_ter = m_ConfigTechnical.getString("SQL.E_EXT.codTercero");
    ext_nvr = m_ConfigTechnical.getString("SQL.E_EXT.verTercero");
    ext_rol = m_ConfigTechnical.getString("SQL.E_EXT.rolTercero");
    ext_dot = m_ConfigTechnical.getString("SQL.E_EXT.codDomicilio");
    ext_notificacion_electronica = m_ConfigTechnical.getString("SQL.E_EXT.notificacionElectronica");

//  Interesados
	sql_intecodUor= m_ConfigTechnical.getString ("SQL.R_EXT.codUor");
	sql_intetipo= m_ConfigTechnical.getString ("SQL.R_EXT.tipo");
	sql_inteano= m_ConfigTechnical.getString ("SQL.R_EXT.ano");
	sql_intenumero= m_ConfigTechnical.getString ("SQL.R_EXT.numero");
	sql_intecoddepartamento= m_ConfigTechnical.getString ("SQL.R_EXT.coddepartamento");
	sql_intecodTercero= m_ConfigTechnical.getString ("SQL.R_EXT.codTercero");
	sql_inteverTercero= m_ConfigTechnical.getString ("SQL.R_EXT.verTercero");
	sql_intecodDomicilio= m_ConfigTechnical.getString ("SQL.R_EXT.codDomicilio");
	sql_interolTercero= m_ConfigTechnical.getString ("SQL.R_EXT.rolTercero");

    hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
    hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");
    hte_pa1 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido1");
    hte_ap1 = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
    hte_pa2 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido2");
    hte_ap2 = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
    hte_nom = m_ConfigTechnical.getString("SQL.T_HTE.nombre");
    hte_tlf = m_ConfigTechnical.getString("SQL.T_HTE.telefono");
    hte_dce = m_ConfigTechnical.getString("SQL.T_HTE.email");
    hte_tip = m_ConfigTechnical.getString("SQL.T_HTE.tipoDocumento");
    hte_doc = m_ConfigTechnical.getString("SQL.T_HTE.documento");
    
    

    dom_cod = m_ConfigTechnical.getString("SQL.T_DOM.idDomicilio");
    dom_nml = m_ConfigTechnical.getString("SQL.T_DOM.normalizado");

    dsu_cod = m_ConfigTechnical.getString("SQL.T_DSU.identificador");
    dsu_pai = m_ConfigTechnical.getString("SQL.T_DSU.pais");
    dsu_prv = m_ConfigTechnical.getString("SQL.T_DSU.provincia");
    dsu_mun = m_ConfigTechnical.getString("SQL.T_DSU.municipio");
    dsu_via = m_ConfigTechnical.getString("SQL.T_DSU.vial");
    dsu_nud = m_ConfigTechnical.getString("SQL.T_DSU.numeroDesde");
    dsu_led = m_ConfigTechnical.getString("SQL.T_DSU.letraDesde");
    dsu_nuh = m_ConfigTechnical.getString("SQL.T_DSU.numeroHasta");
    dsu_leh = m_ConfigTechnical.getString("SQL.T_DSU.letraHasta");
    dsu_blq = m_ConfigTechnical.getString("SQL.T_DSU.bloque");
    dsu_por = m_ConfigTechnical.getString("SQL.T_DSU.portal");

    dpo_dom = m_ConfigTechnical.getString("SQL.T_DPO.domicilio");
    dpo_dsu = m_ConfigTechnical.getString("SQL.T_DPO.suelo");
    dpo_esc = m_ConfigTechnical.getString("SQL.T_DPO.escalera");
    dpo_plt = m_ConfigTechnical.getString("SQL.T_DPO.planta");
    dpo_pta = m_ConfigTechnical.getString("SQL.T_DPO.puerta");

    via_pai = m_ConfigTechnical.getString("SQL.T_VIA.pais");
    via_prv = m_ConfigTechnical.getString("SQL.T_VIA.provincia");
    via_mun = m_ConfigTechnical.getString("SQL.T_VIA.municipio");
    via_cod = m_ConfigTechnical.getString("SQL.T_VIA.identificador");
    via_tvi = m_ConfigTechnical.getString("SQL.T_VIA.tipo");
    via_nom = m_ConfigTechnical.getString("SQL.T_VIA.nombreVia");

    tvi_des = m_ConfigTechnical.getString("SQL.T_TVI.tipoVia");
    tvi_abr = m_ConfigTechnical.getString("SQL.T_TVI.abreviatura");
    tvi_cod = m_ConfigTechnical.getString("SQL.T_TVI.codigo");

    dnn_dom = m_ConfigTechnical.getString("SQL.T_DNN.idDomicilio");
    dnn_dmc = m_ConfigTechnical.getString("SQL.T_DNN.domicilio");
    dnn_tvi = m_ConfigTechnical.getString("SQL.T_DNN.idTipoVia");
    dnn_nud = m_ConfigTechnical.getString("SQL.T_DNN.numDesde");
    dnn_led = m_ConfigTechnical.getString("SQL.T_DNN.letraDesde");
    dnn_nuh = m_ConfigTechnical.getString("SQL.T_DNN.numHasta");
    dnn_leh = m_ConfigTechnical.getString("SQL.T_DNN.letraHasta");
    dnn_blq = m_ConfigTechnical.getString("SQL.T_DNN.bloque");
    dnn_por = m_ConfigTechnical.getString("SQL.T_DNN.portal");
    dnn_esc = m_ConfigTechnical.getString("SQL.T_DNN.escalera");
    dnn_plt = m_ConfigTechnical.getString("SQL.T_DNN.planta");
    dnn_pta = m_ConfigTechnical.getString("SQL.T_DNN.puerta");
      dnn_pai = m_ConfigTechnical.getString("SQL.T_DNN.idPaisD");
      dnn_prv = m_ConfigTechnical.getString("SQL.T_DNN.idProvinciaD");
      dnn_mun = m_ConfigTechnical.getString("SQL.T_DNN.idMunicipioD");
      dnn_via = m_ConfigTechnical.getString("SQL.T_DNN.codigoVia");
      dnn_cpo = m_ConfigTechnical.getString("SQL.T_DNN.codigoPostal");

    rol_mun = m_ConfigTechnical.getString("SQL.E_ROL.codMunicipio");
    rol_pro = m_ConfigTechnical.getString("SQL.E_ROL.codProcedimiento");
    rol_cod = m_ConfigTechnical.getString("SQL.E_ROL.codRol");
    rol_des = m_ConfigTechnical.getString("SQL.E_ROL.descripcion");
    rol_pde = m_ConfigTechnical.getString("SQL.E_ROL.porDefecto");
    
    rol_codR = m_ConfigTechnical.getString("SQL.R_ROL.codigo");
    rol_desR = m_ConfigTechnical.getString("SQL.R_ROL.descripcion");
    rol_pdeR = "ROL_PDE";
    grupo = m_ConfigTechnical.getString("SQL.R_ROL.grupo");
    grupoPorDefecto = m_ConfigTechnical.getString("SQL.R_ROL.grupoPorDefecto");
 
    exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
    exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
    exp_eje = m_ConfigTechnical.getString("SQL.E_EXP.ano");
    exp_num = m_ConfigTechnical.getString("SQL.E_EXP.numero");

   //para registro

    res_dep = m_ConfigTechnical.getString("SQL.R_RES.codDpto");
    res_uor = m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
    res_tip = m_ConfigTechnical.getString("SQL.R_RES.tipoReg");
    res_eje = m_ConfigTechnical.getString("SQL.R_RES.ejercicio");
    res_num = m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacion");
    

    r_cod = m_ConfigTechnical.getString("SQL.R_ROL.codigo");
    r_des = m_ConfigTechnical.getString("SQL.R_ROL.descripcion");
    r_pde = "ROL_PDE";
    
    idPais = m_ConfigTechnical.getString("SQL.T_PAI.idPais");
    pais = m_ConfigTechnical.getString("SQL.T_PAI.nombre");
    idProvincia = m_ConfigTechnical.getString("SQL.T_PRV.idProvincia");
    provincia = m_ConfigTechnical.getString("SQL.T_PRV.nombre");
    propais= m_ConfigTechnical.getString("SQL.T_PRV.idPais");
    idMunicipio = m_ConfigTechnical.getString("SQL.T_MUN.idMunicipio");
    municipio = m_ConfigTechnical.getString("SQL.T_MUN.nombre");
    munpais = m_ConfigTechnical.getString("SQL.T_MUN.idPais");
    munipro = m_ConfigTechnical.getString("SQL.T_MUN.idProvincia");

  }

  public static InteresadosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo
      synchronized (InteresadosDAO.class) {
        if (instance == null) {
          instance = new InteresadosDAO();
        }
      }
    }
    return instance;
  }

  
  
    public Vector<InteresadoExpedienteVO> getListaInteresados(GeneralValueObject gVO, String[] params) {


        AdaptadorSQLBD obd = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        
        Vector<InteresadoExpedienteVO> resultado = new Vector<InteresadoExpedienteVO>();
        try {

            obd = new AdaptadorSQLBD(params);
            con = obd.getConnection();
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            sql = "SELECT " + dom_nml + "," + dom_cod + "," + ext_ter + "," + rol_cod +","+ext_nvr+
                    " FROM T_DOM,E_EXT,E_EXP,E_ROL WHERE " + ext_mun +
                    " = " + codMunicipio + " AND " + ext_eje + " = " + ejercicio + " AND " + ext_num + " = '" +
                    numero + "' AND " + ext_dot + "=" + dom_cod + " AND " + ext_mun + "=" +
                    exp_mun + " AND " + ext_eje + "=" + exp_eje + " AND " + ext_num + "=" +
                    exp_num + " AND " + ext_pro + "=" + rol_pro + " AND " + ext_rol + "=" +
                    rol_cod + " AND "+ ext_mun+"="+rol_mun+" ORDER BY MOSTRAR DESC, ROL_COD";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            rs = st.executeQuery(sql);
            
            Vector listaDomicilios = new Vector();
            Vector listaNormalizados = new Vector();
            Vector listaTerceros = new Vector();
            Vector listaVersionesTercero = new Vector();
            while (rs.next()) {
                String normalizado = rs.getString(dom_nml);
                String codDomicilio = rs.getString(dom_cod);
                String codTercero = rs.getString(ext_ter);
                String numeroVersion = rs.getString(ext_nvr);
                listaDomicilios.addElement(codDomicilio);
                listaNormalizados.addElement(normalizado);
                listaTerceros.addElement(codTercero);
                listaVersionesTercero.addElement(numeroVersion);
            }
            rs.close();
            
            for (int i = 0; i < listaDomicilios.size(); i++) {
                String codDomicilio = (String) listaDomicilios.elementAt(i);
                String codTercero = (String) listaTerceros.elementAt(i);
                String verTercero = (String) listaVersionesTercero.elementAt(i);
                 
                if ("1".equals((String) listaNormalizados.elementAt(i))) {
                    sql = "SELECT T_DSU.*,T_DPO.*,T_VIA.*,T_TVI.*, " + ext_ter + ", " + ext_nvr + ", " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{hte_pa1, "''", hte_ap1}) + " AS ap1, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{hte_pa2, "''", hte_ap2}) + " AS ap2, " + hte_nom + " AS nombre, " +
                            hte_tlf + "," + hte_dce + "," + hte_tip + "," + hte_doc + ", PAI_NOM, PRV_NOM, MUN_NOM " +
                            "EXT_ROL , MOSTRAR, EXT_NOTIFICACION_ELECTRONICA " + rol_des + "," + rol_pde + "," + ext_dot +
                            " FROM E_ROL,T_DSU,T_DPO,T_VIA,T_TVI," +
                            GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV, E_EXT" +
                            " left joint T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)" +
                            " WHERE " +
                            ext_mun + " = " + codMunicipio + " AND " +
                            ext_eje + " = " + ejercicio + " AND " +
                            ext_num + " = '" + numero + "' AND " +
                            ext_ter + "=" + codTercero + " AND " +
                            ext_nvr + "=" + verTercero + " AND " +
                            ext_mun + "=" + rol_mun + " AND " +
                            rol_pro + "='" + codProcedimiento + "' AND " +
                            ext_rol + "=" + rol_cod + " AND " +
                            " PAI_COD = DSU_PAI AND PRV_PAI = DSU_PAI AND MUN_PAI = DSU_PAI AND " +
                            " PRV_COD = DSU_PRV AND MUN_PRV = DSU_PRV AND " +
                            " MUN_COD = DSU_MUN AND " +                           
                            dpo_dom + "=" + codDomicilio + " AND " +
                            dpo_dsu + "=" + dsu_cod + " AND " +
                            dsu_pai + "=" + via_pai + " AND " + dsu_prv + "=" + via_prv + " AND " + dsu_mun + "=" +
                            via_mun + " AND " + dsu_via + "=" + via_cod + " AND " + via_tvi + "=" + tvi_cod +
                            " ORDER BY " + ext_rol;
                } else {
                    sql = "SELECT T_DNN.*, " + ext_ter + ", " + ext_nvr + ", " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{hte_pa1, "''", hte_ap1}) + " AS ap1, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{hte_pa2, "''", hte_ap2}) + " AS ap2, " + hte_nom + " AS nombre, " +
                            hte_tlf + "," + hte_dce + "," + hte_tip + "," + hte_doc + ", PAI_NOM, PRV_NOM, MUN_NOM, " +
                            ext_rol + ", MOSTRAR,  EXT_NOTIFICACION_ELECTRONICA, " + rol_des + "," + rol_pde + ", " + ext_dot + "," + tvi_des + "," + via_nom +
                            " FROM E_ROL, " +
                            GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,E_EXT" +
                            " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)," +
                            " T_DNN "+
                            " left join T_TVI on (DNN_TVI=TVI_COD)" +
                            " left join T_VIA on (DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN AND DNN_VIA=VIA_COD) "+
                            " WHERE " +
                            ext_mun + " = " + codMunicipio + " AND " +
                            ext_eje + " = " + ejercicio + " AND " +
                            ext_num + " = '" + numero + "' AND " +
                            ext_ter + "=" + codTercero + " AND " +
                            ext_nvr + "=" + verTercero + " AND " +
                            ext_mun + "=" + rol_mun + " AND " +
                            rol_pro + "='" + codProcedimiento + "' AND " +
                            ext_rol + "=" + rol_cod + " AND " +
                            ext_pro + "=" + rol_pro + " AND " +
                            " PAI_COD = DNN_PAI AND PRV_PAI = DNN_PAI AND MUN_PAI = DNN_PAI AND " +
                            " PRV_COD = DNN_PRV AND MUN_PRV = DNN_PRV AND " +
                            " MUN_COD = DNN_MUN AND " +                            
                            dnn_dom + "=" + codDomicilio;
                    sql += " ORDER BY " + ext_rol;
                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }

                rs = st.executeQuery(sql);


                while (rs.next()) {
                    //para dar formato al interesado
                    String ap1 = rs.getString("ap1");
                    String ap2 = rs.getString("ap2");
                    String nombre = rs.getString("nombre");
                    String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                    InteresadoExpedienteVO interesadoVO = new InteresadoExpedienteVO();
                    interesadoVO.setCodTercero(rs.getInt("EXT_TER"));
                    interesadoVO.setNumVersion(rs.getInt("EXT_NVR"));
                    interesadoVO.setNombreCompleto(titular);
                    interesadoVO.setCodigoRol(rs.getInt("EXT_ROL"));
                    interesadoVO.setDescRol(rs.getString("ROL_DES"));
                    interesadoVO.setPorDefecto(rs.getBoolean("ROL_PDE"));
                    interesadoVO.setCodDomicilio(rs.getInt("EXT_DOT"));
                    interesadoVO.setMostrar(rs.getBoolean("MOSTRAR"));
                    
                    String notificacion = rs.getString("EXT_NOTIFICACION_ELECTRONICA");
                    interesadoVO.setAdmiteNotificacion("0"); 
                    if(notificacion!=null && !"".equals(notificacion))
                        interesadoVO.setAdmiteNotificacion(notificacion); 
                    

                    interesadoVO.setTelf(rs.getString(hte_tlf));
                    interesadoVO.setEmail(rs.getString(hte_dce));
                    interesadoVO.setTipoDoc(rs.getString(hte_tip));
                    interesadoVO.setTxtDoc(rs.getString(hte_doc));
                    interesadoVO.setPais(rs.getString("PAI_NOM"));
                    interesadoVO.setProvincia(rs.getString("PRV_NOM"));
                    interesadoVO.setMunicipio(rs.getString("MUN_NOM"));
                    
                     if ("1".equals((String) listaNormalizados.elementAt(i))) {
                        String descTipoVia = rs.getString(tvi_des);
                        String descVia = rs.getString(via_nom);
                        String numDesde = rs.getString(dsu_nud);
                        String letraDesde = rs.getString(dsu_led);
                        String numHasta = rs.getString(dsu_nuh);
                        String letraHasta = rs.getString(dsu_leh);
                        String bloque = rs.getString(dsu_blq);
                        String portal = rs.getString(dsu_por);
                        String escalera = rs.getString(dpo_esc);
                        String planta = rs.getString(dpo_plt);
                        String puerta = rs.getString(dpo_pta);
                        String domicilio = "";
                        domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                        domicilio = (descVia != null && !descVia.equals("")) ? domicilio + descVia + " " : domicilio;
                        domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                        domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                        domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                        domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                        domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                        domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                        domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                        domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                        domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;

                        interesadoVO.setDomicilio(domicilio);
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(domicilio);
                        }
                        interesadoVO.setCp("");

                    } else {
                        String descTipoVia = rs.getString(tvi_des);
                        String numDesde = rs.getString(dnn_nud);
                        String letraDesde = rs.getString(dnn_led);
                        String numHasta = rs.getString(dnn_nuh);
                        String letraHasta = rs.getString(dnn_leh);
                        String bloque = rs.getString(dnn_blq);
                        String portal = rs.getString(dnn_por);
                        String escalera = rs.getString(dnn_esc);
                        String planta = rs.getString(dnn_plt);
                        String puerta = rs.getString(dnn_pta);
                        String emplaz = rs.getString(dnn_dmc);
                        String domic = rs.getString(via_nom);
                        String domicilio = "";
                        domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                        domicilio = (domic != null && !domic.equals("")) ? domicilio + domic + " " : domicilio;
                        Config m_Conf = ConfigServiceHelper.getConfig("common");
                        if (m_Conf.getString("JSP.Emplazamiento").equals("si")) {
                            domicilio = (emplaz != null && !emplaz.equals("")) ? domicilio + " " + emplaz + " " : domicilio;
                        }
                        domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                        domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                        domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                        domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                        domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                        domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                        domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                        domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                        domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;

                        m_Log.debug("---Domicilio: " + domicilio);
                        interesadoVO.setDomicilio(domicilio);

                        interesadoVO.setCp(rs.getString(dnn_cpo));
                    }
                    resultado.add(interesadoVO);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                obd.devolverConexion(con);
            } catch (Exception bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return resultado;
    }

    
    
    
    
     public Vector<InteresadoExpedienteVO> getListaInteresados(GeneralValueObject gVO,AdaptadorSQLBD obd,Connection con) {
       
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        
        Vector<InteresadoExpedienteVO> resultado = new Vector<InteresadoExpedienteVO>();
        try {

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            
            if (esExpedienteHistorico(numero, con))
                sql = "SELECT DOM_NML,DOM_COD,EXT_TER,ROL_COD,EXT_NVR"+
                        " FROM T_DOM,HIST_E_EXT,HIST_E_EXP,E_ROL WHERE EXT_MUN=? AND EXT_EJE=? AND EXT_NUM=? " +
                        "AND EXT_DOT=DOM_COD AND EXT_MUN = " +
                        "EXP_MUN AND EXT_EJE = EXP_EJE AND EXT_NUM = " +
                        "EXP_NUM AND EXT_PRO = ROL_PRO  AND EXT_ROL =" +
                        " ROL_COD AND EXT_MUN = ROL_MUN ORDER BY MOSTRAR DESC, ROL_COD";
            else
                sql = "SELECT DOM_NML,DOM_COD,EXT_TER,ROL_COD,EXT_NVR"+
                        " FROM T_DOM,E_EXT,E_EXP,E_ROL WHERE EXT_MUN=? AND EXT_EJE=? AND EXT_NUM=? " +
                        "AND EXT_DOT=DOM_COD AND EXT_MUN = " +
                        "EXP_MUN AND EXT_EJE = EXP_EJE AND EXT_NUM = " +
                        "EXP_NUM AND EXT_PRO = ROL_PRO  AND EXT_ROL =" +
                        " ROL_COD AND EXT_MUN = ROL_MUN ORDER BY MOSTRAR DESC, ROL_COD";
                

            int j = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(j++,Integer.parseInt(codMunicipio));
            ps.setInt(j++,Integer.parseInt(ejercicio));
            ps.setString(j++,numero);
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            rs = ps.executeQuery();
            
            Vector listaDomicilios = new Vector();
            Vector listaNormalizados = new Vector();
            Vector listaTerceros = new Vector();
            Vector listaVersionesTercero = new Vector();
            while (rs.next()) {
                String normalizado = rs.getString("DOM_NML");
                String codDomicilio = rs.getString("DOM_COD");
                String codTercero = rs.getString("EXT_TER");
                String numeroVersion = rs.getString("EXT_NVR");
                listaDomicilios.addElement(codDomicilio);
                listaNormalizados.addElement(normalizado);
                listaTerceros.addElement(codTercero);
                listaVersionesTercero.addElement(numeroVersion);
            }
            rs.close();
            ps.close();
            
            for (int i = 0; i < listaDomicilios.size(); i++) {
                String codDomicilio = (String) listaDomicilios.elementAt(i);
                String codTercero = (String) listaTerceros.elementAt(i);
                String verTercero = (String) listaVersionesTercero.elementAt(i);
                 
                if ("1".equals((String) listaNormalizados.elementAt(i))) {
                  
                      sql = "SELECT T_DSU.*,T_DPO.*,T_VIA.*,T_TVI.*, EXT_TER,EXT_NVR," +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " +
                            "HTE_TLF,HTE_DCE,HTE_TID,HTE_DOC, PAI_NOM, PRV_NOM, MUN_NOM " +
                            "EXT_ROL , MOSTRAR, EXT_NOTIFICACION_ELECTRONICA ROL_DES,ROL_PDE,EXT_DOT" +
                            " FROM E_ROL,T_DSU,T_DPO,T_VIA,T_TVI," +
                            GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,";
                    
                    if (esExpedienteHistorico(numero, con))
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,HIST_E_EXT";
                    else
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,E_EXT";
   
                    sql = sql + " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)" +
                            " WHERE " +
                            "ext_mun = ? AND " +
                            "ext_eje = ? AND " +
                            "ext_num = ? AND " +
                            "ext_ter = ? AND " +
                            "ext_nvr  = ? AND " +
                            "ext_mun = rol_mun  AND " +
                            "rol_pro  = ? AND " +
                            "ext_rol = rol_cod AND " +
                            " PAI_COD = DSU_PAI AND PRV_PAI = DSU_PAI AND MUN_PAI = DSU_PAI AND " +
                            " PRV_COD = DSU_PRV AND MUN_PRV = DSU_PRV AND " +
                            " MUN_COD = DSU_MUN AND " +                           
                            "dpo_dom = codDomicilio  AND " +
                            "dpo_dsu =  dsu_cod AND " +
                            "dsu_pai = via_pai AND dsu_prv = via_prv  AND  dsu_mun = " +
                            "via_mun AND dsu_via = via_cod  AND  via_tvi = tvi_cod" +
                            " ORDER BY ext_rol";
                } else {
                     sql = "SELECT T_DNN.*, ext_ter,ext_nvr, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"hte_pa1", "''", "hte_ap1"}) + " AS ap1, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"hte_pa2", "''", "hte_ap2"}) + " AS ap2, hte_nom AS nombre, " +
                            "hte_tlf,hte_dce,HTE_TID,hte_doc, PAI_NOM, PRV_NOM, MUN_NOM, " +
                            "ext_rol , MOSTRAR,  EXT_NOTIFICACION_ELECTRONICA, rol_des ,rol_pde,ext_dot,tvi_des,via_nom" +
                            " FROM E_ROL, " +
                            GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,";
                    
                    if (esExpedienteHistorico(numero, con))
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,HIST_E_EXT";
                    else
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,E_EXT";
   
                    sql = sql + " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)," +
                            " T_DNN "+
                            " left join T_TVI on (DNN_TVI=TVI_COD)" +
                            " left join T_VIA on (DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN AND DNN_VIA=VIA_COD) "+
                            " WHERE " +
                            "ext_mun =? AND " +
                            "ext_eje =? AND " +
                            "ext_num  =? AND " +
                            "ext_ter =? AND " +
                            "ext_nvr = ? AND " +
                            "ext_mun =ROL_MUN AND " +
                            "rol_pro =? AND " +
                            "ext_rol = rol_cod  AND " +
                            "ext_pro = rol_pro  AND " +
                            " PAI_COD = DNN_PAI AND PRV_PAI = DNN_PAI AND MUN_PAI = DNN_PAI AND " +
                            " PRV_COD = DNN_PRV AND MUN_PRV = DNN_PRV AND " +
                            " MUN_COD = DNN_MUN AND " +
                            "dnn_dom = " + codDomicilio ;
                            
                    sql += " ORDER BY ext_rol";        
                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                  
                ps = con.prepareStatement(sql);
                int z=1;
                ps.setInt(z++,Integer.parseInt(codMunicipio));
                ps.setInt(z++,Integer.parseInt(ejercicio));
                ps.setString(z++,numero);
                ps.setInt(z++,Integer.parseInt(codTercero));
                ps.setInt(z++,Integer.parseInt(verTercero));
                ps.setString(z++,codProcedimiento);
                
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    //para dar formato al interesado
                    String ap1 = rs.getString("ap1");
                    String ap2 = rs.getString("ap2");
                    String nombre = rs.getString("nombre");
                    String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                    InteresadoExpedienteVO interesadoVO = new InteresadoExpedienteVO();
                    interesadoVO.setCodTercero(rs.getInt("EXT_TER"));
                    interesadoVO.setNumVersion(rs.getInt("EXT_NVR"));
                    interesadoVO.setNombreCompleto(titular);
                    interesadoVO.setCodigoRol(rs.getInt("EXT_ROL"));
                    interesadoVO.setDescRol(rs.getString("ROL_DES"));
                    interesadoVO.setPorDefecto(rs.getBoolean("ROL_PDE"));
                    interesadoVO.setCodDomicilio(rs.getInt("EXT_DOT"));
                    interesadoVO.setMostrar(rs.getBoolean("MOSTRAR"));
                    
                    String notificacion = rs.getString("EXT_NOTIFICACION_ELECTRONICA");
                    interesadoVO.setAdmiteNotificacion("0"); 
                    if(notificacion!=null && !"".equals(notificacion))
                        interesadoVO.setAdmiteNotificacion(notificacion); 
                    

                    interesadoVO.setTelf(rs.getString("hte_tlf"));
                    interesadoVO.setEmail(rs.getString("hte_dce"));
                    interesadoVO.setTipoDoc(rs.getString("HTE_TID"));
                    interesadoVO.setTxtDoc(rs.getString("hte_doc"));
                    interesadoVO.setPais(rs.getString("PAI_NOM"));
                    interesadoVO.setProvincia(rs.getString("PRV_NOM"));
                    interesadoVO.setMunicipio(rs.getString("MUN_NOM"));
                    
                     if ("1".equals((String) listaNormalizados.elementAt(i))) {
                       String descTipoVia = rs.getString("tvi_des");
                        String descVia = rs.getString("via_nom");
                        String numDesde = rs.getString("dsu_nud");
                        String letraDesde = rs.getString("dsu_led");
                        String numHasta = rs.getString("dsu_nuh");
                        String letraHasta = rs.getString("dsu_leh");
                        String bloque = rs.getString("dsu_blq");
                        String portal = rs.getString("dsu_por");
                        String escalera = rs.getString("dpo_esc");
                        String planta = rs.getString("dpo_plt");
                        String puerta = rs.getString("dpo_pta");
                        String domicilio = "";
                        domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                        domicilio = (descVia != null && !descVia.equals("")) ? domicilio + descVia + " " : domicilio;
                        domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                        domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                        domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                        domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                        domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                        domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                        domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                        domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                        domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;

                        interesadoVO.setDomicilio(domicilio);
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(domicilio);
                        }
                        interesadoVO.setCp("");

                    } else {
                          String descTipoVia = rs.getString("tvi_des");
                        String numDesde = rs.getString("dnn_nud");
                        String letraDesde = rs.getString("dnn_led");
                        String numHasta = rs.getString("dnn_nuh");
                        String letraHasta = rs.getString("dnn_leh");
                        String bloque = rs.getString("dnn_blq");
                        String portal = rs.getString("dnn_por");
                        String escalera = rs.getString("dnn_esc");
                        String planta = rs.getString("dnn_plt");
                        String puerta = rs.getString("dnn_pta");
                        String emplaz = rs.getString("dnn_dmc");
                        String domic = rs.getString("via_nom");
                        String domicilio = "";
                        domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                        domicilio = (domic != null && !domic.equals("")) ? domicilio + domic + " " : domicilio;
                        Config m_Conf = ConfigServiceHelper.getConfig("common");
                        if (m_Conf.getString("JSP.Emplazamiento").equals("si")) {
                            domicilio = (emplaz != null && !emplaz.equals("")) ? domicilio + " " + emplaz + " " : domicilio;
                        }
                        domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                        domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                        domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                        domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                        domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                        domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                        domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                        domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                        domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;

                        m_Log.debug("---Domicilio: " + domicilio);
                        interesadoVO.setDomicilio(domicilio);

                        interesadoVO.setCp(rs.getString("dnn_cpo"));
                    }
                    resultado.add(interesadoVO);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                
            } catch (Exception bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return resultado;
    }
    
    
    
  public Vector getListaInteresadosRegistro(GeneralValueObject gVO, String[] params){
	    Vector resultado = new Vector();
	    AdaptadorSQLBD obd = null;
	    Connection con = null;
	    Statement st = null;
	    ResultSet rs = null;
	    String sql = "";
	    Vector listaDomicilios = new Vector();
	    Vector listaNormalizados= new Vector();
	    Vector listaTerceros = new Vector();
	    Vector listaPais = new Vector();
	    Vector listaMunicipio = new Vector();
	    Vector listaProvincia = new Vector();

	    try{

	      obd = new AdaptadorSQLBD(params);
	      con = obd.getConnection();
	      st = con.createStatement();

	      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
	     // String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
	      String ejercicio = (String)gVO.getAtributo("ejercicio");
	      String numero = (String)gVO.getAtributo("numero");
	      String codtip = (String)gVO.getAtributo("codTip");
	      String codOur = (String)gVO.getAtributo("codOur");
       String codDep = (String)gVO.getAtributo("codDep");
	      String codProc = (String)gVO.getAtributo("codProc");
	      String munProc = (String)gVO.getAtributo("munProc");
	      
	      // Caso de un alta: no hay ningun interesado aun en BD.
	      if (ejercicio==null || ejercicio.equals("") || numero==null || numero.equals("")) {
	          return resultado;
	      }
	      	 
	      m_Log.debug("Parametros busqueda interesados:\n " + gVO);
	      
	      if (codProc != null && !codProc.equals("") && !codProc.equals("null")) {  
                  // Cogemos roles del procedimiento (E_ROL)
        	  sql = "SELECT " + dom_nml + "," + dom_cod + "," + sql_intecodTercero + "," + rol_cod + "," 
        	  + dnn_pai +"," + dnn_prv +"," + dnn_mun + 
        	  " FROM T_DOM,R_EXT,R_RES,E_ROL,T_DNN WHERE " + sql_intetipo +" = '" + codtip + 
        	  "' AND " +sql_intecodUor +" = " + codOur + 
        	  " AND " + sql_intecoddepartamento + " = " + codDep +
        	  " AND " + sql_inteano + " = " + ejercicio + 
        	  " AND " + sql_intenumero + " = " + numero + 
        	  " AND " + sql_intecodDomicilio + "=" + dom_cod + 
        	  " AND " + sql_intecodUor + "=" + res_uor + 
        	  " AND " + sql_intecoddepartamento + " = " + res_dep + 
        	  " AND " + sql_inteano + "=" + res_eje + 
        	  " AND " + sql_intenumero + "=" + res_num +
              " AND " + sql_intetipo + "=" + res_tip  +
              " AND " + sql_intecodDomicilio + "=" + dnn_dom  +
              " AND " + sql_interolTercero + "=" + rol_cod + 
              " AND " + rol_mun + "=" + munProc + 
              " AND " + rol_pro + "='" + codProc + "' ORDER BY " + rol_cod;
	      } else {  
                  // Cogemos un grupo de roles (R_ROL)
	          sql = "SELECT " + dom_nml + "," + dom_cod + "," + sql_intecodTercero + "," + r_cod + "," 
              + dnn_pai +"," + dnn_prv +"," + dnn_mun + 
              " FROM T_DOM,R_EXT,R_RES,R_ROL,T_DNN WHERE " + sql_intetipo +" = '" + codtip + 
              "' AND " +sql_intecodUor +" = " + codOur + 
              " AND " + sql_intecoddepartamento + " = " + codDep +
              " AND " + sql_inteano + " = " + ejercicio + 
              " AND " + sql_intenumero + " = " + numero + 
              " AND " + sql_intecodDomicilio + "=" + dom_cod + 
              " AND " + sql_intecodUor + "=" + res_uor + 
              " AND " + sql_intecoddepartamento + " = " + res_dep +                           
              " AND " + sql_inteano + "=" + res_eje + 
              " AND " + sql_intenumero + "=" + res_num +
              " AND " + sql_intetipo + "=" + res_tip  +
              " AND " + sql_intecodDomicilio + "=" + dnn_dom  +
              " AND " + grupo + "=" + grupoPorDefecto + 
              " AND " + sql_interolTercero + "=" + r_cod + " ORDER BY " + r_cod;
	      }
	      	   
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

	      rs = st.executeQuery(sql);
	      while(rs.next()){
	        String normalizado = rs.getString(dom_nml);
	        String codDomicilio = rs.getString(dom_cod);
	        String codTercero = rs.getString(ext_ter);
	        String codPais = rs.getString(dnn_pai);
	        String codProvincia = rs.getString(dnn_prv);
	        String codMun = rs.getString(dnn_mun);
	        listaDomicilios.addElement(codDomicilio);
	        listaNormalizados.addElement(normalizado);
	        listaTerceros.addElement(codTercero);
	        listaPais.addElement(codPais);
	        listaProvincia.addElement(codProvincia);
	        listaMunicipio.addElement(codMun);	        	   
	      }
	      rs.close();

	      for(int i=0;i<listaDomicilios.size();i++) {
	        String codDomicilio = (String) listaDomicilios.elementAt(i);
	        String codTercero = (String) listaTerceros.elementAt(i);
	        String codPais = (String) listaPais.elementAt(i);
	        String codProvincia = (String) listaProvincia.elementAt(i);
	        String codMun = (String) listaMunicipio.elementAt(i);
	        if("1".equals((String)listaNormalizados.elementAt(i))) {
	       /*   sql =	"SELECT T_DSU.*,T_DPO.*,T_VIA.*,T_TVI.*, " + ext_ter + ", " + ext_nvr + ", " +
	                  obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,
	                 new String[]{hte_pa1, "' '", hte_ap1, "' '", hte_pa2, "' '", hte_ap2, " (CASE WHEN " + hte_pa1 +
	               " IS NOT NULL OR " + hte_ap1 + " IS NOT NULL OR " + hte_pa2 +
	               " IS NOT NULL OR " + hte_ap2 + " IS NOT NULL THEN ',' ELSE '' END)", hte_nom}) +
	                " AS titular, " + ext_rol + ", " + rol_des + "," + rol_pde + "," + ext_dot +
	               " FROM E_EXT,E_ROL,T_HTE,T_DSU,T_DPO,T_VIA,T_TVI WHERE " +
	                ext_mun + " = " + codMunicipio + " AND " +
	                ext_eje + " = " + ejercicio + " AND " +
	                ext_num +" = '" + numero + "' AND " +
	                ext_ter + "=" + codTercero + " AND " +
	                ext_mun + "=" + rol_mun + " AND " +
	                rol_pro + "='" + codProcedimiento + "' AND " +
	                ext_rol + "=" + rol_cod + " AND " +
	                obd.joinLeft(new String[]{ext_ter,ext_nvr},new String[]{hte_ter,hte_nvr})+ " AND "+
	                dpo_dom + "=" + codDomicilio + " AND " +
	                dpo_dsu + "=" + dsu_cod + " AND " +
	                dsu_pai + "=" + via_pai + " AND " + dsu_prv + "=" + via_prv + " AND " + dsu_mun + "=" +
	                via_mun + " AND " + dsu_via + "=" + via_cod + " AND " + via_tvi + "=" + tvi_cod +
	               " ORDER BY " + ext_rol;*/
	        } else {
	            if (codProc != null && !codProc.equals("") && !codProc.equals("null")) {  
                     // Cogemos roles del procedimiento (E_ROL)
    	            sql = "SELECT T_DNN.*, " + pais +", " + provincia +", " + municipio +", " + sql_intecodTercero + ", " + sql_inteverTercero + ", " +
                            obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa1, "''", hte_ap1})+" AS ap1, " +
                            obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa2,"''", hte_ap2})+" AS ap2, " + hte_nom +" AS nombre, "+ 
                            sql_interolTercero + "," + rol_des + "," + rol_pde + ", " + sql_intecodDomicilio + ","
        	                + tvi_des + "," + via_nom +"," + hte_tlf +", " + hte_dce +"," + hte_tip +"," + hte_doc +"," 
        	                + dnn_cpo +
        	                " FROM E_ROL,"+
                            GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                            GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
                            GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,R_EXT" +
                             " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)," +
                            " T_DNN "+
                            " left join T_TVI on (DNN_TVI=TVI_COD)" +
                            " left join T_VIA on (DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN AND DNN_VIA=VIA_COD) "+
                            " WHERE " +
        	                sql_inteano + " = " + ejercicio + " AND " +
        	                sql_intenumero + " = '" + numero + "' AND " +
        	                sql_intecodTercero + "=" + codTercero + " AND " +
        	                sql_interolTercero + "=" + rol_cod + " AND " +
        	                rol_mun + "=" + munProc + " AND " + 
        	                rol_pro + "='" + codProc + "' AND " + 
        	                sql_intecodUor + "='" +codOur + "' AND " +
        	                sql_intetipo + "='" +codtip + "' AND " +
        	                idPais + "=" + codPais + " AND " +
        	                idProvincia + "=" + codProvincia + " AND " +
        	                idMunicipio + "=" + codMun + " AND " +
        	                propais + "=" + codPais + " AND " +
        	                munpais + "=" + codPais + " AND " +
        	                munipro + "=" + codProvincia + " AND " +
        	                dnn_dom + "=" + codDomicilio;                          	                
	                sql +=" ORDER BY " + ext_rol;
	            } else {  
                        // Cogemos un grupo de roles (R_ROL)
	                sql =  "SELECT T_DNN.*, " + pais +", " + provincia +", " + municipio +", " + sql_intecodTercero + ", " + sql_inteverTercero + ", " +
                            obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa1, "''", hte_ap1})+" AS ap1, " +
                            obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa2, "''",hte_ap2})+" AS ap2, " + hte_nom +" AS nombre, "+
                                sql_interolTercero + ", " + r_des + "," + r_pde + ", " + sql_intecodDomicilio + ","
	                       + tvi_des + "," + via_nom +"," + hte_tlf +"," + hte_dce +"," + hte_tip +"," + hte_doc +"," 
	                       + dnn_cpo +
	                       " FROM R_ROL,"+ 
	                       GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
	                       GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
	                       GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,R_EXT" +
                            " LEFT join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)," +
                            " T_DNN "+
                            " left join T_TVI on (DNN_TVI=TVI_COD)" +
                            " left join T_VIA on (DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN AND DNN_VIA=VIA_COD) "+
                            " WHERE " +
	                       sql_inteano + " = " + ejercicio + " AND " +
	                       sql_intenumero + " = '" + numero + "' AND " +
	                       sql_intecodTercero + "=" + codTercero + " AND " +
	                       sql_interolTercero + "=" + r_cod + " AND " +
	                       grupo + "=" + grupoPorDefecto + " AND " +  
	                       sql_intecodUor + "='" +codOur + "' AND " +
	                       sql_intetipo + "='" +codtip + "' AND " +
	                       idPais + "=" + codPais + " AND " +
	                       idProvincia + "=" + codProvincia + " AND " +
	                       idMunicipio + "=" + codMun + " AND " +
	                       propais + "=" + codPais + " AND " +
	                       munpais + "=" + codPais + " AND " +
	                       munipro + "=" + codProvincia + " AND " +
	                      dnn_dom + "=" + codDomicilio ;                         
	                       
	                sql +=" ORDER BY " + ext_rol;
	                
	            }
	        }

	        if(m_Log.isDebugEnabled()) m_Log.debug(sql);

	        rs = st.executeQuery(sql);
       
                String porDefecto = "";
	        while(rs.next()){
                    //para dar formato al interesado
                     String ap1=rs.getString("ap1");
                     String ap2=rs.getString("ap2");
                     String nombre=rs.getString("nombre");
                     
                     String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                     
                     
	          GeneralValueObject genVO = new GeneralValueObject();
	          genVO.setAtributo("codigoTercero",rs.getString(sql_intecodTercero));
	          genVO.setAtributo("versionTercero",rs.getString(sql_inteverTercero));
	          genVO.setAtributo("titular",titular);
	          genVO.setAtributo("rol",rs.getString(sql_interolTercero));
	          genVO.setAtributo("descRol",rs.getString(r_des));
                  // Para los procedimientos, el valor de porDefecto es 1 ó 0, hay
                  // que traducirlo a 'SI' o 'NO'
                  porDefecto = rs.getString(r_pde);
                  if (porDefecto.equals("0")) porDefecto = "NO";
                  else if (porDefecto.equals("1")) porDefecto = "SI";
	          genVO.setAtributo("porDefecto",porDefecto);
	          genVO.setAtributo("domicilio",rs.getString(sql_intecodDomicilio));
	          genVO.setAtributo("telefono",rs.getString(hte_tlf));
	          genVO.setAtributo("email",rs.getString(hte_dce));
	          genVO.setAtributo("tip",rs.getString(hte_tip));
	          genVO.setAtributo("doc",rs.getString(hte_doc));
	          genVO.setAtributo("cp",rs.getString(dnn_cpo));
	          genVO.setAtributo("pais",rs.getString(pais));
	          genVO.setAtributo("provincia",rs.getString(provincia));
	          genVO.setAtributo("municipio",rs.getString(municipio));
	     
	          
	          if("1".equals((String)listaNormalizados.elementAt(i))) {
	            String descTipoVia = rs.getString(tvi_des);
	            String descVia = rs.getString(via_nom);
	            String numDesde = rs.getString(dsu_nud);
	            String letraDesde = rs.getString(dsu_led);
	            String numHasta = rs.getString(dsu_nuh);
	            String letraHasta = rs.getString(dsu_leh);
	            String bloque = rs.getString(dsu_blq);
	            String portal = rs.getString(dsu_por);
	            String escalera = rs.getString(dpo_esc);
	            String planta = rs.getString(dpo_plt);
	            String puerta = rs.getString(dpo_pta);
	            String domicilio = "";
	            domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio+descTipoVia+" ":domicilio;
	            domicilio = (descVia != null && !descVia.equals("")) ? domicilio+descVia+" " :domicilio;
	            domicilio = (numDesde !=null && !numDesde.equals("")) ? domicilio+" "+numDesde:domicilio;
	            domicilio = (letraDesde !=null && !letraDesde.equals("")) ? domicilio+" "+letraDesde+" ":domicilio;
	            domicilio = (numHasta !=null && !numHasta.equals("")) ? domicilio+" "+numHasta:domicilio;
	            domicilio = (letraHasta !=null && !letraHasta.equals("")) ? domicilio+" "+letraHasta:domicilio;
	            domicilio = (bloque != null && !bloque.equals("")) ? domicilio+" Bl. "+bloque:domicilio;
	            domicilio = (portal != null && !portal.equals("")) ? domicilio+" Portal "+portal:domicilio;
	            domicilio = (escalera != null && !escalera.equals("")) ? domicilio+" Esc. "+escalera:domicilio;
	            domicilio = (planta != null && !planta.equals("")) ? domicilio+" "+planta+"º ":domicilio;
	            domicilio = (puerta !=null && !puerta.equals("")) ? domicilio+puerta:domicilio;
	            genVO.setAtributo("descDomicilio",domicilio);
	            if(m_Log.isDebugEnabled()) m_Log.debug("->Domicilio: " + domicilio);
	          } else {
	            String descTipoVia = rs.getString(tvi_des);
	            String numDesde = rs.getString(dnn_nud);
	            String letraDesde = rs.getString(dnn_led);
	            String numHasta = rs.getString(dnn_nuh);
	            String letraHasta = rs.getString(dnn_leh);
	            String bloque = rs.getString(dnn_blq);
	            String portal = rs.getString(dnn_por);
	            String escalera = rs.getString(dnn_esc);
	            String planta = rs.getString(dnn_plt);
	            String puerta = rs.getString(dnn_pta);
	            String emplaz = rs.getString(dnn_dmc);
	              String domic = rs.getString(via_nom);
	            String domicilio = "";
	            domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio+descTipoVia+" ":domicilio;
	            domicilio = (domic != null && !domic.equals("")) ? domicilio+domic+" " :domicilio;
	            Config m_Conf = ConfigServiceHelper.getConfig("common");	            	            
	            if (m_Conf.getString("JSP.Emplazamiento").equals("si")) {
	                domicilio = (emplaz != null && !emplaz.equals("")) ? domicilio+" "+emplaz+" " :domicilio;	                
	            }
	            domicilio = (numDesde !=null && !numDesde.equals("")) ? domicilio+" "+numDesde:domicilio;
	            domicilio = (letraDesde !=null && !letraDesde.equals("")) ? domicilio+" "+letraDesde+" ":domicilio;
	            domicilio = (numHasta !=null && !numHasta.equals("")) ? domicilio+" "+numHasta:domicilio;
	            domicilio = (letraHasta !=null && !letraHasta.equals("")) ? domicilio+" "+letraHasta:domicilio;
	            domicilio = (bloque != null && !bloque.equals("")) ? domicilio+" Bl. "+bloque:domicilio;
	            domicilio = (portal != null && !portal.equals("")) ? domicilio+" Portal "+portal:domicilio;
	            domicilio = (escalera != null && !escalera.equals("")) ? domicilio+" Esc. "+escalera:domicilio;
	            domicilio = (planta != null && !planta.equals("")) ? domicilio+" "+planta+"º ":domicilio;
	            domicilio = (puerta !=null && !puerta.equals("")) ? domicilio+puerta:domicilio;

	            m_Log.debug("->Domicilio: " + domicilio);
	            genVO.setAtributo("descDomicilio",domicilio);
	          }
	          resultado.add(genVO);
	        }
	      }

	    }catch (Exception e){
	        e.printStackTrace();
	        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
	    }finally{
	        try{
	                if (rs!=null) rs.close();
	                if (st!=null) st.close();
	            obd.devolverConexion(con);
	        }catch(Exception bde) {
	            bde.printStackTrace();
	            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
	        }
	    }
	    return resultado;
	  }
  
  /**
   * Devuelve un vector de GeneralValueObject con todos los datos de los interesados
   * del registrovo pasado. El código es el mismo que en la otra version de este metodo, pero
   * adaptado para usar argumentos diferentes.
   * @param vo vo del que se quieren los interesados.
   * @param codProc codigo del procedimiento que indica los roles de los interesados
   * @param con conexion abierta a la BD
   * @param params parametros de conexion
   * @return Vector de GeneralValueObject
   * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
   */
   public Vector<GeneralValueObject> getListaInteresadosRegistro(RegistroValueObject vo, 
           String codProc, Connection con, String[] params)
      throws AnotacionRegistroException, TechnicalException{
       
        Vector resultado = new Vector();
        AdaptadorSQLBD obd = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector listaDomicilios = new Vector();
        Vector listaNormalizados= new Vector();
        Vector listaTerceros = new Vector();
        Vector listaPais = new Vector();
        Vector listaMunicipio = new Vector();
        Vector listaProvincia = new Vector();

        try{

          obd = new AdaptadorSQLBD(params);
          st = con.createStatement();

          String ejercicio = Integer.toString(vo.getAnoReg());
          String numero = Long.toString(vo.getNumReg());
          String codTip = vo.getTipoReg();
          String codUor = Integer.toString(vo.getUnidadOrgan());
          String codDep = Integer.toString(vo.getIdentDepart());

          m_Log.debug("Parametros busqueda interesados:\n " + ejercicio + "/" + numero + " " + codTip + "," + codUor + "," + codDep);

          if (codProc != null && !codProc.equals("") && !codProc.equals("null")) {  
              // Cogemos roles del procedimiento (E_ROL)
              sql = "SELECT " + dom_nml + "," + dom_cod + "," + sql_intecodTercero + "," + rol_cod + "," 
                       + dnn_pai +"," + dnn_prv +"," + dnn_mun + 
                    " FROM T_DOM,R_EXT,R_RES,E_ROL,T_DNN WHERE " + sql_intetipo +" = '" + codTip + 
                    "' AND " +sql_intecodUor +" = " + codUor + 
                    " AND " + sql_intecoddepartamento + " = " + codDep + 
                    " AND " + sql_inteano + " = " + ejercicio + 
                    " AND " + sql_intenumero + " = " + numero + 
                    " AND " + sql_intecodDomicilio + "=" + dom_cod + 
                    " AND " + sql_intecodUor + "=" + res_uor + 
                    " AND " + sql_intecoddepartamento + " = " + res_dep + 
                    " AND " + sql_inteano + "=" + res_eje + 
                    " AND " + sql_intenumero + "=" + res_num +
                    " AND " + sql_intetipo + "=" + res_tip  +
                    " AND " + sql_intecodDomicilio + "=" + dnn_dom  +
                    " AND " + sql_interolTercero + "=" + rol_cod + 
                    " AND " + rol_pro + "='" + codProc + "' ORDER BY " + rol_cod;
          } else {  
              // Cogemos un grupo de roles (R_ROL)
              sql = "SELECT " + dom_nml + "," + dom_cod + "," + sql_intecodTercero + "," + r_cod + "," 
                       + dnn_pai +"," + dnn_prv +"," + dnn_mun + 
                    " FROM T_DOM,R_EXT,R_RES,R_ROL,T_DNN WHERE " + sql_intetipo +" = '" + codTip + 
                    "' AND " +sql_intecodUor +" = " + codUor + 
                    " AND " + sql_intecoddepartamento + " = " + codDep +  
                    " AND " + sql_inteano + " = " + ejercicio + 
                    " AND " + sql_intenumero + " = " + numero + 
                    " AND " + sql_intecodDomicilio + "=" + dom_cod + 
                    " AND " + sql_intecodUor + "=" + res_uor + 
                    " AND " + sql_intecoddepartamento + " = " + res_dep + 
                    " AND " + sql_inteano + "=" + res_eje + 
                    " AND " + sql_intenumero + "=" + res_num +
                    " AND " + sql_intetipo + "=" + res_tip  +
                    " AND " + sql_intecodDomicilio + "=" + dnn_dom  +
                    " AND " + grupo + "=" + grupoPorDefecto + 
                    " AND " + sql_interolTercero + "=" + r_cod + " ORDER BY " + r_cod;
          }

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          st = con.createStatement();
          rs = st.executeQuery(sql);
          while(rs.next()){
            listaDomicilios.addElement(rs.getString(dom_cod));
            listaNormalizados.addElement(rs.getString(dom_nml));
            listaTerceros.addElement(rs.getString(ext_ter));
            listaPais.addElement(rs.getString(dnn_pai));
            listaProvincia.addElement(rs.getString(dnn_prv));
            listaMunicipio.addElement(rs.getString(dnn_mun));	        	   
          }
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(st);

          for(int i=0;i<listaDomicilios.size();i++) {
            String codDomicilio = (String) listaDomicilios.elementAt(i);
            String codTercero = (String) listaTerceros.elementAt(i);
            String codPais = (String) listaPais.elementAt(i);
            String codProvincia = (String) listaProvincia.elementAt(i);
            String codMun = (String) listaMunicipio.elementAt(i);

            if (codProc != null && !codProc.equals("") && !codProc.equals("null")) {  
             // Cogemos roles del procedimiento (E_ROL)
            sql = "SELECT T_DNN.*, " + pais +", " + provincia +", " + municipio +", " + sql_intecodTercero + ", " + sql_inteverTercero + ", " +
                    obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa1, "''", hte_ap1})+" AS ap1, " +
                    obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa2,"''", hte_ap2})+" AS ap2, " + hte_nom +" AS nombre, "+ 
                    sql_interolTercero + "," + rol_des + "," + rol_pde + ", " + sql_intecodDomicilio + ","
                        + tvi_des + "," + via_nom +"," + hte_tlf +", " + hte_dce +"," + hte_tip +"," + hte_doc +"," 
                        + dnn_cpo + ", TID_DES" +
                        " FROM E_ROL,T_TID,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,R_EXT" +
                    " LEFT JOIN T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)," +
                            " T_DNN "+
                            " left join T_TVI on (DNN_TVI=TVI_COD)" +
                            " left join T_VIA on (DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN AND DNN_VIA=VIA_COD) "+
                            " WHERE " +
                        sql_inteano + " = " + ejercicio + " AND " +
                        sql_intenumero + " = '" + numero + "' AND " +
                        sql_intecodTercero + "=" + codTercero + " AND " +
                        sql_interolTercero + "=" + rol_cod + " AND " + 
                        rol_pro + "='" + codProc + "' AND " + 
                        sql_intecodUor + "='" +codUor + "' AND " +
                        sql_intetipo + "='" +codTip + "' AND " +
                        idPais + "=" + codPais + " AND " +
                        idProvincia + "=" + codProvincia + " AND " +
                        idMunicipio + "=" + codMun + " AND " +
                        propais + "=" + codPais + " AND " +
                        munpais + "=" + codPais + " AND " +
                        munipro + "=" + codProvincia + " AND " +
                        hte_tip + "= TID_COD AND " +                        
                        dnn_dom + "=" + codDomicilio; 
                sql +=" ORDER BY " + ext_rol;
            } else {  
                // Cogemos un grupo de roles (R_ROL)
                sql =  "SELECT T_DNN.*, " + pais +", " + provincia +", " + municipio +", " + sql_intecodTercero + ", " + sql_inteverTercero + ", " +
                    obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa1, "''", hte_ap1})+" AS ap1, " +
                    obd.funcionCadena(obd.FUNCIONCADENA_CONCAT,new String[]{hte_pa2, "''",hte_ap2})+" AS ap2, " + hte_nom +" AS nombre, "+
                        sql_interolTercero + ", " + r_des + "," + r_pde + ", " + sql_intecodDomicilio + ","
                       + tvi_des + "," + via_nom +"," + hte_tlf +"," + hte_dce +"," + hte_tip +"," + hte_doc +"," 
                       + dnn_cpo + ", TID_DES" +
                       " FROM R_ROL,T_TID,"+
                       GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                       GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
                       GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,R_EXT" +
                    " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)," +
                            " T_DNN "+
                            " left join T_TVI on (DNN_TVI=TVI_COD)" +
                            " left join T_VIA on (DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN AND DNN_VIA=VIA_COD) "+
                            " WHERE " +
                       sql_inteano + " = " + ejercicio + " AND " +
                       sql_intenumero + " = '" + numero + "' AND " +
                       sql_intecodTercero + "=" + codTercero + " AND " +
                       sql_interolTercero + "=" + r_cod + " AND " +
                       grupo + "=" + grupoPorDefecto + " AND " +  
                       sql_intecodUor + "='" +codUor + "' AND " +
                       sql_intetipo + "='" +codTip + "' AND " +
                       idPais + "=" + codPais + " AND " +
                       idProvincia + "=" + codProvincia + " AND " +
                       idMunicipio + "=" + codMun + " AND " +
                       propais + "=" + codPais + " AND " +
                       munpais + "=" + codPais + " AND " +
                       munipro + "=" + codProvincia + " AND " +
                       hte_tip + "= TID_COD AND " +
                       dnn_dom + "=" + codDomicilio ;          
                       
                sql +=" ORDER BY " + ext_rol;

            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            String porDefecto = "";
            while (rs.next()) {
                  //para dar formato al interesado
                  String ap1 = rs.getString("ap1");
                  String ap2 = rs.getString("ap2");
                  String nombre = rs.getString("nombre");

                  String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                  GeneralValueObject genVO = new GeneralValueObject();
                  genVO.setAtributo("codigoTercero", rs.getString(sql_intecodTercero));
                  genVO.setAtributo("versionTercero", rs.getString(sql_inteverTercero));
                  genVO.setAtributo("titular", titular);
                  genVO.setAtributo("rol", rs.getString(sql_interolTercero));
                  genVO.setAtributo("descRol", rs.getString(r_des));
                  // Para los procedimientos, el valor de porDefecto es 1 ó 0, hay
                  // que traducirlo a 'SI' o 'NO'
                  porDefecto = rs.getString(r_pde);
                  if (porDefecto.equals("0")) {
                      porDefecto = "NO";
                  } else if (porDefecto.equals("1")) {
                      porDefecto = "SI";
                  }
                  genVO.setAtributo("porDefecto", porDefecto);
                  genVO.setAtributo("domicilio", rs.getString(sql_intecodDomicilio));
                  genVO.setAtributo("telefono", rs.getString(hte_tlf));
                  genVO.setAtributo("email", rs.getString(hte_dce));
                  genVO.setAtributo("tip", rs.getString(hte_tip));
                  genVO.setAtributo("tipoDoc", rs.getString("TID_DES"));
                  genVO.setAtributo("doc", rs.getString(hte_doc));
                  genVO.setAtributo("cp", rs.getString(dnn_cpo));
                  genVO.setAtributo("pais", rs.getString(pais));
                  genVO.setAtributo("provincia", rs.getString(provincia));
                  genVO.setAtributo("municipio", rs.getString(municipio));


                  if ("1".equals((String) listaNormalizados.elementAt(i))) {
                      String descTipoVia = rs.getString(tvi_des);
                      String descVia = rs.getString(via_nom);
                      String numDesde = rs.getString(dsu_nud);
                      String letraDesde = rs.getString(dsu_led);
                      String numHasta = rs.getString(dsu_nuh);
                      String letraHasta = rs.getString(dsu_leh);
                      String bloque = rs.getString(dsu_blq);
                      String portal = rs.getString(dsu_por);
                      String escalera = rs.getString(dpo_esc);
                      String planta = rs.getString(dpo_plt);
                      String puerta = rs.getString(dpo_pta);
                      String domicilio = "";
                      domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                      domicilio = (descVia != null && !descVia.equals("")) ? domicilio + descVia + " " : domicilio;
                      domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                      domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                      domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                      domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                      domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                      domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                      domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                      domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                      domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;
                      genVO.setAtributo("descDomicilio", domicilio);
                  } else {
                      String descTipoVia = rs.getString(tvi_des);
                      String numDesde = rs.getString(dnn_nud);
                      String letraDesde = rs.getString(dnn_led);
                      String numHasta = rs.getString(dnn_nuh);
                      String letraHasta = rs.getString(dnn_leh);
                      String bloque = rs.getString(dnn_blq);
                      String portal = rs.getString(dnn_por);
                      String escalera = rs.getString(dnn_esc);
                      String planta = rs.getString(dnn_plt);
                      String puerta = rs.getString(dnn_pta);
                      String emplaz = rs.getString(dnn_dmc);
                      String domic = rs.getString(via_nom);
                      String domicilio = "";
                      domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                      domicilio = (domic != null && !domic.equals("")) ? domicilio + domic + " " : domicilio;
                      Config m_Conf = ConfigServiceHelper.getConfig("common");
                      if (m_Conf.getString("JSP.Emplazamiento").equals("si")) {
                          domicilio = (emplaz != null && !emplaz.equals("")) ? domicilio + " " + emplaz + " " : domicilio;
                      }
                      domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                      domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                      domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                      domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                      domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                      domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                      domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                      domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                      domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;

                      genVO.setAtributo("descDomicilio", domicilio);
                  }
                  resultado.add(genVO);
              }
            SigpGeneralOperations.closeStatement(st);
          }

        } catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            throw new AnotacionRegistroException("ERROR",e);
        } finally {
           SigpGeneralOperations.closeResultSet(rs);
           SigpGeneralOperations.closeStatement(st);
        }
        return resultado;
   }
   
  public Vector getListaRolesRegistro(String[] params) {

	   //Queremos estar informados de cuando este metod es ejecutado
	   m_Log.debug("getListaRoles");

	   AdaptadorSQLBD oad = null;
	   Connection con = null;
	   Statement st = null;
	   ResultSet rs = null;
	   String sql = "";
	   Vector list = new Vector();
	   int orden = 0;
	  
	   try{
	     oad = new AdaptadorSQLBD(params);
	     con = oad.getConnection();
	     st = con.createStatement();
	     sql = "SELECT " + r_cod + "," + r_des + "," + r_pde + " FROM R_ROL WHERE " + grupo + "=" + grupoPorDefecto + "AND ROL_ACT='SI'";
	     String parametros[] = {"1","1"};
	     sql += oad.orderUnion(parametros);
	     if(m_Log.isDebugEnabled()) m_Log.debug("InteresadosDAO, getListaRoles: Sentencia SQL:" + sql);
	     rs = st.executeQuery(sql);
	     while(rs.next()){
	       GeneralValueObject g = new GeneralValueObject();
	       g.setAtributo("codRol",rs.getString(rol_cod));
	       g.setAtributo("descRol",rs.getString(rol_des));
	       g.setAtributo("porDefecto",rs.getString(rol_pde));
	       list.addElement(g);
	     }
	     m_Log.debug("InteresadosDAO, getListaRoles: Lista area cargada");
	     if(m_Log.isDebugEnabled()) m_Log.debug("InteresadosDAO, getListaRoles: Tamaño lista:" + list.size());
	     }catch (Exception e) {
	       list = null;
	       e.printStackTrace();
	       if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
	     }finally{
	        try{
	                if (rs!=null) rs.close();
	                if (st!=null) st.close();
	            oad.devolverConexion(con);
	        }catch(Exception bde) {
	            bde.printStackTrace();
	            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
	        }
	    }
	     //Queremos estar informados de cuando este metodo ha finalizado
	     m_Log.debug("getListaRoles");
	     return list;
	  }
  
  public Vector getListaRoles(GeneralValueObject gVO,String[] params) {

   //Queremos estar informados de cuando este metod es ejecutado
   m_Log.debug("getListaRoles");

   AdaptadorSQLBD oad = null;
   Connection con = null;
   Statement st = null;
   ResultSet rs = null;
   String sql = "";
   Vector list = new Vector();
   int orden = 0;
   String codMunicipio = (String)gVO.getAtributo("codMunicipio");
   String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

   try{
     oad = new AdaptadorSQLBD(params);
     con = oad.getConnection();
     st = con.createStatement();
     sql = "SELECT " + rol_cod + "," + rol_des + "," + rol_pde + " FROM E_ROL WHERE " + rol_pro + "='" + codProcedimiento + "'";
     String parametros[] = {"1","1"};
     sql += oad.orderUnion(parametros);
     if(m_Log.isDebugEnabled()) m_Log.debug("InteresadosDAO, getListaRoles: Sentencia SQL:" + sql);
     rs = st.executeQuery(sql);
     while(rs.next()){
       GeneralValueObject g = new GeneralValueObject();
       g.setAtributo("codRol",rs.getString(rol_cod));
       g.setAtributo("descRol",rs.getString(rol_des));
       g.setAtributo("porDefecto",rs.getString(rol_pde));
       list.addElement(g);
     }
     m_Log.debug("InteresadosDAO, getListaRoles: Lista area cargada");
     if(m_Log.isDebugEnabled()) m_Log.debug("InteresadosDAO, getListaRoles: Tamaño lista:" + list.size());
     }catch (Exception e) {
       list = null;
       e.printStackTrace();
       if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
     }finally{
        try{
                if (rs!=null) rs.close();
                if (st!=null) st.close();
            oad.devolverConexion(con);
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        }
    }
     //Queremos estar informados de cuando este metodo ha finalizado
     m_Log.debug("getListaRoles");
     return list;
  }


  public Vector eliminarInteresado(GeneralValueObject gVO, String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD obd = null;
    Connection con = null;
    Statement st = null;
    String sql = "";

    try{

      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
      obd.inicioTransaccion(con);
      st = con.createStatement();

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");
      String codigoTercero = (String)gVO.getAtributo("codigoTercero");
      String versionTercero = (String)gVO.getAtributo("versionTercero");

      sql =	"DELETE FROM E_EXT " +
          " WHERE " + ext_mun + " = " + codMunicipio + " AND " + ext_eje + " = " + ejercicio + " AND " + ext_num + " = '" + numero + "' AND " +
          ext_ter + " = " + codigoTercero + " AND " + ext_nvr + " = " + versionTercero;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      st.executeUpdate(sql);
      obd.finTransaccion(con);

    }catch (Exception e){
      try{
            obd.rollBack(con);
      }catch(Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
      }
      e.printStackTrace();
      if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());

    }finally{
      try{
            if (st!=null) st.close();
            obd.devolverConexion(con);
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        }
    }
    resultado = getListaInteresados(gVO,params);
    return resultado;
  }


  public Vector modificarInteresado(GeneralValueObject gVO, String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD obd = null;
    Connection con = null;
    Statement st = null;
    String sql = "";
    try{
      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
      st = con.createStatement();

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");
      String codigoTercero = (String)gVO.getAtributo("codigoTercero");
      String versionTercero = (String)gVO.getAtributo("versionTercero");
      String rol = (String)gVO.getAtributo("rol");

      sql =	"UPDATE E_EXT SET " +
          ext_rol + " = " + rol +
          " WHERE " + ext_mun + " = " + codMunicipio + " AND " + ext_eje + " = " + ejercicio + " AND " + ext_num + " = '" + numero + "' AND " +
          ext_ter + " = " + codigoTercero + " AND " + ext_nvr + " = " + versionTercero;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      int res = st.executeUpdate(sql);
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        return null;
    }finally{
        try{
            if (st!=null) st.close();
            obd.devolverConexion(con);
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        }
    }
    resultado = getListaInteresados(gVO,params);
    return resultado;
  }

  public Vector altaInteresado(GeneralValueObject gVO, String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD obd = null;
    Connection con = null;
    Statement st = null;
    String sql = "";

    try{
      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
      st = con.createStatement();

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");
      String codigoTercero = (String)gVO.getAtributo("codigoTercero");
      String versionTercero = (String)gVO.getAtributo("versionTercero");
      String domicilio = (String)gVO.getAtributo("domicilio");
      String rol = (String)gVO.getAtributo("rol");
      String notifElectronica = (String) gVO.getAtributo("notifElectronica");

      sql = "INSERT INTO E_EXT("+
        ext_mun + ", " + ext_eje + ", " + ext_num + ", " + ext_ter + ", " + ext_nvr + ", " + ext_dot + ", " + ext_rol + "," + ext_pro + "," +  ext_notificacion_electronica +
        ") VALUES (" + codMunicipio + ", " + ejercicio + ", '" + numero + "', " + codigoTercero + ", " + versionTercero + ", " + 
        domicilio + ", " + rol + ",'" + codProcedimiento + "','" + notifElectronica + "')";
      
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      st.executeUpdate(sql);
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
    }finally{
        try{
            if (st!=null) st.close();
            obd.devolverConexion(con);
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        }
    }

    resultado = getListaInteresados(gVO,params);
    return resultado;
   }
   
   public int grabarInteresadosRegistro(GeneralValueObject gVO, String[] params){
	   m_Log.debug("Dentro de grabar interesado registro");
	    AdaptadorSQLBD obd = null;
	    Connection con = null;
	    Statement st = null;
	    String sql = "";
	    int resultado = 0;

	    try{
	      obd = new AdaptadorSQLBD(params);
	      con = obd.getConnection();
	      obd.inicioTransaccion(con);
	      st = con.createStatement();
	      String codOur= (String)gVO.getAtributo("codOur");
	      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
	      //String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
	      String ejercicio = (String)gVO.getAtributo("ejercicio");
	      String numeroExpediente = (String)gVO.getAtributo("numero");
	      String tipo= (String)gVO.getAtributo("codTip");
	      String codDep= (String)gVO.getAtributo("codDep");
	      Vector listaCodTercero = (Vector) gVO.getAtributo("listaCodTercero");
			  Vector listaVersionTercero = (Vector) gVO.getAtributo("listaVersionTercero");
			  Vector listaCodDomicilio = (Vector) gVO.getAtributo("listaCodDomicilio");
			  Vector listaRol = (Vector) gVO.getAtributo("listaRol");      

	      sql = "DELETE FROM R_EXT WHERE " + sql_inteano + "=" +
			  ejercicio + " AND " + sql_intenumero + "='" + numeroExpediente + "'"+ " AND " + sql_intecodUor + "='" + codOur + "'";
			  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			  resultado = st.executeUpdate(sql);
			
			  if(listaCodTercero.size() >0) {
			    for (int i=0; i<listaCodTercero.size(); i++) {
			      sql = "INSERT INTO R_EXT ( " + sql_intecodUor + "," + sql_intetipo + "," + sql_inteano + "," + sql_intenumero +  
			      ","+ sql_intecoddepartamento + "," + sql_intecodTercero + "," + sql_inteverTercero + "," + sql_intecodDomicilio + "," + sql_interolTercero + ") " +
			      "VALUES (" + codOur + ",'" + tipo +
			      "'," + ejercicio + "," + numeroExpediente + ","+ codDep + "," +
			      listaCodTercero.elementAt(i) + "," +
			      listaVersionTercero.elementAt(i) + "," + listaCodDomicilio.elementAt(i) + "," +
			      listaRol.elementAt(i) + ")";
			      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			      resultado = st.executeUpdate(sql);
			    }
			  }
	          obd.finTransaccion(con);

	    }catch (Exception e){
	        try{
	            obd.rollBack(con);
	        }catch(Exception ex){
	            ex.printStackTrace();
	            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
	        }
	        e.printStackTrace();
	        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
	    }finally{
	        try{
	            if (st!=null) st.close();
	            obd.devolverConexion(con);
	        }catch(Exception e){
	            e.printStackTrace();
	            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
	        }
	    }
	    
	    return resultado;
	   }
   
   /***************** NUEVO ****************************/
   public boolean altaInteresado(GeneralValueObject gVO,Connection con){
       boolean exito = false;                    
       Statement st = null;
       String sql = "";
        
       try{        
          st = con.createStatement();

          String codMunicipio = (String)gVO.getAtributo("codMunicipio");
          String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
          String ejercicio = (String)gVO.getAtributo("ejercicio");
          String numero = (String)gVO.getAtributo("numero");
          String codigoTercero = (String)gVO.getAtributo("codigoTercero");
          String versionTercero = (String)gVO.getAtributo("versionTercero");
          String domicilio = (String)gVO.getAtributo("domicilio");
          String rol = (String)gVO.getAtributo("rol");
          String notifElectronica = (String) gVO.getAtributo("notifElectronica");

          sql = "INSERT INTO E_EXT("+
            ext_mun + ", " + ext_eje + ", " + ext_num + ", " + ext_ter + ", " + ext_nvr + ", " + ext_dot + ", " + ext_rol + "," + ext_pro + "," +  ext_notificacion_electronica +
            ") VALUES (" + codMunicipio + ", " + ejercicio + ", '" + numero + "', " + codigoTercero + ", " + versionTercero + ", " + 
            domicilio + ", " + rol + ",'" + codProcedimiento + "','" + notifElectronica + "')";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);

          int rowsUpdated = st.executeUpdate(sql);
          m_Log.debug("Numero interesados insertados : " + rowsUpdated);
          if(rowsUpdated==1)
              exito = true;
          
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                if (st!=null) st.close();
            
            }catch(Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            }
        }
        return exito;
   }
 
   
   
   /************************ NUEVO ******************************/
   
   /**
    * Recupera la lista de roles de un procedimiento
    * @param codOrganizacion: Código de la organización
    * @param codProcedimiento: Código del procedimiento
    * @param con: Conexión a la BD
    * @return 
    */
   public ArrayList<RolVO> getListaRoles(int codOrganizacion,String codProcedimiento,Connection con) {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaRoles");

        ArrayList<RolVO> roles = new ArrayList<RolVO>();
        PreparedStatement ps = null;    
        ResultSet rs = null;
        String sql = "";        
        
        try{
            
            sql = "SELECT ROL_COD,ROL_DES,ROL_PDE FROM E_ROL WHERE ROL_MUN=? AND ROL_PRO=? ORDER BY ROL_COD";
            ps = con.prepareStatement(sql);
            ps.setInt(1,codOrganizacion);
            ps.setString(2,codProcedimiento);
            
            rs = ps.executeQuery();
            while(rs.next()) {
                RolVO rol = new RolVO();
                rol.setCodigo(rs.getInt("ROL_COD"));
                rol.setDescripcion(rs.getString("ROL_DES"));                
                int pde = rs.getInt("ROL_PDE");
                rol.setPde("NO");
                if(pde==1) rol.setPde("SI");                
                roles.add(rol);
                
            }// while            
            
          }catch (Exception e) {                
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
                
          }finally{
             try{
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
                 
             }catch(Exception bde) {
                 bde.printStackTrace();
                 if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
             }
         }
         return roles;
      }



   
    public ArrayList<InteresadoExpedienteVO> getListaInteresadosExpediente(GeneralValueObject gVO,AdaptadorSQLBD obd,Connection con) {
            
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        
        ArrayList<InteresadoExpedienteVO> resultado = new ArrayList<InteresadoExpedienteVO>();
        
        try {

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            
            if (esExpedienteHistorico(numero, con))
                sql = "SELECT DOM_NML,DOM_COD,EXT_TER,ROL_COD,EXT_NVR"+
                        " FROM T_DOM,HIST_E_EXT,HIST_E_EXP,E_ROL WHERE EXT_MUN=? AND EXT_EJE=? AND EXT_NUM=? " +
                        "AND EXT_DOT=DOM_COD AND EXT_MUN = " +
                        "EXP_MUN AND EXT_EJE = EXP_EJE AND EXT_NUM = " +
                        "EXP_NUM AND EXT_PRO = ROL_PRO  AND EXT_ROL =" +
                        " ROL_COD AND EXT_MUN = ROL_MUN ORDER BY MOSTRAR DESC, ROL_COD";
            else
                sql = "SELECT DOM_NML,DOM_COD,EXT_TER,ROL_COD,EXT_NVR"+
                        " FROM T_DOM,E_EXT,E_EXP,E_ROL WHERE EXT_MUN=? AND EXT_EJE=? AND EXT_NUM=? " +
                        "AND EXT_DOT=DOM_COD AND EXT_MUN = " +
                        "EXP_MUN AND EXT_EJE = EXP_EJE AND EXT_NUM = " +
                        "EXP_NUM AND EXT_PRO = ROL_PRO  AND EXT_ROL =" +
                        " ROL_COD AND EXT_MUN = ROL_MUN ORDER BY MOSTRAR DESC, ROL_COD";
                

            int j = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(j++,Integer.parseInt(codMunicipio));
            ps.setInt(j++,Integer.parseInt(ejercicio));
            ps.setString(j++,numero);
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            rs = ps.executeQuery();
            
            Vector listaDomicilios = new Vector();
            Vector listaNormalizados = new Vector();
            Vector listaTerceros = new Vector();
            Vector listaVersionesTercero = new Vector();
            while (rs.next()) {
                String normalizado = rs.getString("DOM_NML");
                String codDomicilio = rs.getString("DOM_COD");
                String codTercero = rs.getString("EXT_TER");
                String numeroVersion = rs.getString("EXT_NVR");
                listaDomicilios.addElement(codDomicilio);
                listaNormalizados.addElement(normalizado);
                listaTerceros.addElement(codTercero);
                listaVersionesTercero.addElement(numeroVersion);
            }
            rs.close();
            ps.close();
            
            for (int i = 0; i < listaDomicilios.size(); i++) {
                String codDomicilio = (String) listaDomicilios.elementAt(i);
                String codTercero = (String) listaTerceros.elementAt(i);
                String verTercero = (String) listaVersionesTercero.elementAt(i);
                 
                if ("1".equals((String) listaNormalizados.elementAt(i))) {
                  
                      sql = "SELECT T_DSU.*,T_DPO.*,T_VIA.*,T_TVI.*, EXT_TER,EXT_NVR," +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " +
                            "HTE_TLF,HTE_DCE,HTE_TID,HTE_DOC, PAI_NOM, PRV_NOM, MUN_NOM " +
                            "EXT_ROL , MOSTRAR, EXT_NOTIFICACION_ELECTRONICA ROL_DES,ROL_PDE,EXT_DOT" +
                            " FROM E_ROL,T_DSU,T_DPO,T_VIA,T_TVI," +
                            GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,";
                    
                    if (esExpedienteHistorico(numero,con))
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,HIST_E_EXT";
                    else
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,E_EXT";
   
                    sql = sql + " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)" +
                            " WHERE " +
                            "ext_mun = ? AND " +
                            "ext_eje = ? AND " +
                            "ext_num = ? AND " +
                            "ext_ter = ? AND " +
                            "ext_nvr  = ? AND " +
                            "ext_mun = rol_mun  AND " +
                            "rol_pro  = ? AND " +
                            "ext_rol = rol_cod AND " +
                            " PAI_COD = DSU_PAI AND PRV_PAI = DSU_PAI AND MUN_PAI = DSU_PAI AND " +
                            " PRV_COD = DSU_PRV AND MUN_PRV = DSU_PRV AND " +
                            " MUN_COD = DSU_MUN AND " +                           
                            "dpo_dom = codDomicilio  AND " +
                            "dpo_dsu =  dsu_cod AND " +
                            "dsu_pai = via_pai AND dsu_prv = via_prv  AND  dsu_mun = " +
                            "via_mun AND dsu_via = via_cod  AND  via_tvi = tvi_cod" +
                            " ORDER BY ext_rol";
                } else {
                     sql = "SELECT T_DNN.*, ext_ter,ext_nvr, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"hte_pa1", "''", "hte_ap1"}) + " AS ap1, " +
                            obd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"hte_pa2", "''", "hte_ap2"}) + " AS ap2, hte_nom AS nombre, " +
                            "hte_tlf,hte_dce,HTE_TID,hte_doc, PAI_NOM, PRV_NOM, MUN_NOM, " +
                            "ext_rol , MOSTRAR,  EXT_NOTIFICACION_ELECTRONICA, rol_des ,rol_pde,ext_dot,tvi_des,via_nom" +
                            " FROM E_ROL, " +
                            GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
	                        GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,";
                    
                    if (esExpedienteHistorico(numero, con))
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,HIST_E_EXT";
                    else
                        sql = sql + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,E_EXT";
   
                    sql = sql + " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)," +
                            " T_DNN "+
                            " left join T_TVI on (DNN_TVI=TVI_COD)" +
                            " left join T_VIA on (DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN AND DNN_VIA=VIA_COD) "+
                            " WHERE " +
                            "ext_mun =? AND " +
                            "ext_eje =? AND " +
                            "ext_num  =? AND " +
                            "ext_ter =? AND " +
                            "ext_nvr = ? AND " +
                            "ext_mun =ROL_MUN AND " +
                            "rol_pro =? AND " +
                            "ext_rol = rol_cod  AND " +
                            "ext_pro = rol_pro  AND " +
                            " PAI_COD = DNN_PAI AND PRV_PAI = DNN_PAI AND MUN_PAI = DNN_PAI AND " +
                            " PRV_COD = DNN_PRV AND MUN_PRV = DNN_PRV AND " +
                            " MUN_COD = DNN_MUN AND " +
                            "dnn_dom = " + codDomicilio ;
                            
                    sql += " ORDER BY ext_rol";        
                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                  
                ps = con.prepareStatement(sql);
                int z=1;
                ps.setInt(z++,Integer.parseInt(codMunicipio));
                ps.setInt(z++,Integer.parseInt(ejercicio));
                ps.setString(z++,numero);
                ps.setInt(z++,Integer.parseInt(codTercero));
                ps.setInt(z++,Integer.parseInt(verTercero));
                ps.setString(z++,codProcedimiento);
                
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    //para dar formato al interesado
                    String ap1 = rs.getString("ap1");
                    String ap2 = rs.getString("ap2");
                    String nombre = rs.getString("nombre");
                    String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                    InteresadoExpedienteVO interesadoVO = new InteresadoExpedienteVO();
                    interesadoVO.setCodTercero(rs.getInt("EXT_TER"));
                    interesadoVO.setNumVersion(rs.getInt("EXT_NVR"));
                    interesadoVO.setNombreCompleto(titular);
                    interesadoVO.setCodigoRol(rs.getInt("EXT_ROL"));
                    interesadoVO.setDescRol(rs.getString("ROL_DES"));
                    interesadoVO.setPorDefecto(rs.getBoolean("ROL_PDE"));
                    interesadoVO.setCodDomicilio(rs.getInt("EXT_DOT"));
                    interesadoVO.setMostrar(rs.getBoolean("MOSTRAR"));
                    
                    String notificacion = rs.getString("EXT_NOTIFICACION_ELECTRONICA");
                    interesadoVO.setAdmiteNotificacion("0"); 
                    if(notificacion!=null && !"".equals(notificacion))
                        interesadoVO.setAdmiteNotificacion(notificacion); 
                    

                    interesadoVO.setTelf(rs.getString("hte_tlf"));
                    interesadoVO.setEmail(rs.getString("hte_dce"));
                    interesadoVO.setTipoDoc(rs.getString("HTE_TID"));
                    interesadoVO.setTxtDoc(rs.getString("hte_doc"));
                    interesadoVO.setPais(rs.getString("PAI_NOM"));
                    interesadoVO.setProvincia(rs.getString("PRV_NOM"));
                    interesadoVO.setMunicipio(rs.getString("MUN_NOM"));
                    
                     if ("1".equals((String) listaNormalizados.elementAt(i))) {
                       String descTipoVia = rs.getString("tvi_des");
                        String descVia = rs.getString("via_nom");
                        String numDesde = rs.getString("dsu_nud");
                        String letraDesde = rs.getString("dsu_led");
                        String numHasta = rs.getString("dsu_nuh");
                        String letraHasta = rs.getString("dsu_leh");
                        String bloque = rs.getString("dsu_blq");
                        String portal = rs.getString("dsu_por");
                        String escalera = rs.getString("dpo_esc");
                        String planta = rs.getString("dpo_plt");
                        String puerta = rs.getString("dpo_pta");
                        String domicilio = "";
                        domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                        domicilio = (descVia != null && !descVia.equals("")) ? domicilio + descVia + " " : domicilio;
                        domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                        domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                        domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                        domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                        domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                        domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                        domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                        domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                        domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;

                        interesadoVO.setDomicilio(domicilio);
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(domicilio);
                        }
                        interesadoVO.setCp("");

                    } else {
                          String descTipoVia = rs.getString("tvi_des");
                        String numDesde = rs.getString("dnn_nud");
                        String letraDesde = rs.getString("dnn_led");
                        String numHasta = rs.getString("dnn_nuh");
                        String letraHasta = rs.getString("dnn_leh");
                        String bloque = rs.getString("dnn_blq");
                        String portal = rs.getString("dnn_por");
                        String escalera = rs.getString("dnn_esc");
                        String planta = rs.getString("dnn_plt");
                        String puerta = rs.getString("dnn_pta");
                        String emplaz = rs.getString("dnn_dmc");
                        String domic = rs.getString("via_nom");
                        String domicilio = "";
                        domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
                        domicilio = (domic != null && !domic.equals("")) ? domicilio + domic + " " : domicilio;
                        Config m_Conf = ConfigServiceHelper.getConfig("common");
                        if (m_Conf.getString("JSP.Emplazamiento").equals("si")) {
                            domicilio = (emplaz != null && !emplaz.equals("")) ? domicilio + " " + emplaz + " " : domicilio;
                        }
                        domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                        domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                        domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                        domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                        domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                        domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                        domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
                        domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                        domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;

                        m_Log.debug("---Domicilio: " + domicilio);
                        interesadoVO.setDomicilio(domicilio);

                        interesadoVO.setCp(rs.getString("dnn_cpo"));
                    }
                    resultado.add(interesadoVO);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                
            } catch (Exception bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return resultado;
    }

    private boolean esExpedienteHistorico(final String numero, final Connection con) throws TechnicalException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean result = true;
        try {
            final String sql = "SELECT EXP_PRO FROM E_EXP" +
                    " WHERE" +
                    " EXP_NUM=?";
            if (m_Log.isDebugEnabled())
                m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setString(1, numero);

            rs = ps.executeQuery();
            result = !rs.next();

        } catch (final Exception ex) {
            m_Log.error(ex.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        return result;
    }

    /**
     * Devuelve un GeneralValueObject con los nombres y documentos de los interesados de un expediente, que tienen 
     * @param numExpediente:_ Número del expediente
     * @param con: Conexión a la BBDD
     * @return GeneralValueObject que en el atributo "nombres" contiene los nombres con el campo "EXT_MOSTRAR=1" del expediente. 
     *          Se concatenan por medio de un ;. En el atributo "documentos" están los documentos concatenados por ;
     */
    public GeneralValueObject getDocumentosNombresInteresadosImpresionExpediente(String numExpediente,Connection con) {
        GeneralValueObject gvo = new GeneralValueObject();
        ResultSet rs = null;
        PreparedStatement ps = null;
        
        try {
            
            String[] datos = numExpediente.split("/");
            String codProcedimiento = datos[1];
            
            String sql = "SELECT HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2 FROM E_EXT, T_HTE " + 
                         "WHERE EXT_NUM=? AND EXT_PRO=? AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR AND MOSTRAR=1";
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,numExpediente);
            ps.setString(i++,codProcedimiento);
            
            rs = ps.executeQuery();
            
            StringBuffer nombres = new StringBuffer();
            StringBuffer documentos = new StringBuffer();
            int contador = 0;
            while(rs.next()) {
                String documento = rs.getString("HTE_DOC");
                String nombre    = rs.getString("HTE_NOM");
                String ap1       = rs.getString("HTE_AP1");
                String ap2       = rs.getString("HTE_AP2");
                
                if(contador>=1) {
                    nombres.append(" ; ");
                    documentos.append(" ; ");
                }
                
                if(ap1!=null && !"".equals(ap1.trim())) nombres.append(ap1);
                if(ap2!=null && !"".equals(ap2.trim())) {
                    if(ap1!=null && !"".equals(ap1.trim())) {
                        nombres.append(" ");
                    }
                    nombres.append(ap2);
                }
                if(nombre!=null && !"".equals(nombre.trim())) {                    
                    if((ap1!=null && !"".equals(ap1.trim())) || (ap2!=null && !"".equals(ap2.trim()))) nombres.append(",");
                    nombres.append(nombre);
                }
                documentos.append(documento);
                contador++;
            }
            
            gvo.setAtributo("nombres",nombres.toString());
            gvo.setAtributo("documentos",documentos.toString());
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();                
            }catch(SQLException e){
            }
        }
        
        return gvo;
    } 
    /*
    public ArrayList<TercerosValueObject> getListaCodsInteresadosRegistro(RegistroValueObject rVO, Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<TercerosValueObject> interesados = new ArrayList<TercerosValueObject>();
        String query;
        
        try {
            query = "SELECT EXT_TER, EXT_NVR FROM R_EXT WHERE EXT_UOR=? AND EXT_TIP=? AND EXT_EJE=? AND EXT_NUM=? AND EXT_DEP=?";
            String queryParams = rVO.getUnidadOrgan() + "-" + rVO.getTipoReg()+ "-" + rVO.getAnoReg()+ "-" +rVO.getNumReg()+ "-" +rVO.getIdentDepart();
            m_Log.debug("sql = " + query);
            m_Log.debug("Parametros pasados a la query: " + queryParams);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, rVO.getUnidadOrgan());
            ps.setString(contbd++, rVO.getTipoReg());
            ps.setInt(contbd++, rVO.getAnoReg());
            ps.setLong(contbd++, rVO.getNumReg());
            ps.setInt(contbd, rVO.getIdentDepart());
            
            rs = ps.executeQuery();
            while(rs.next()){
                TercerosValueObject tercero = new TercerosValueObject();
                tercero.setIdentificador(String.valueOf(rs.getInt("EXT_TER")));
                tercero.setVersion(String.valueOf(rs.getInt("EXT_NVR")));
                interesados.add(tercero);
            }
            
        } catch (SQLException ex){
            m_Log.error("Error al recuperar los interesados del registro");
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException ex) {
                m_Log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return interesados;
    }
    */
    
    public ArrayList<GeneralValueObject> getListaExpedientesInteresado(TercerosValueObject interesado, String filtroProc, boolean busqPorDoc, boolean filtroVersion, Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> numsExpediente = new ArrayList<String>();
        ArrayList<GeneralValueObject> listado = new ArrayList<GeneralValueObject>();

        StringBuilder query = new StringBuilder("");
        StringBuilder queryParam = new StringBuilder("");
        
        try {
            // Obtenemos los expedientes del tercero con estado distinto de ANULADO (si no se indica el procedimiento)
            // o igual a PENDIENTE (si se indica el procedimiento)
            query.append("SELECT EXT_NUM,EXP_EST,EXP_FEI,EXP_ASU FROM E_EXT JOIN E_EXP ON (E_EXT.EXT_NUM = E_EXP.EXP_NUM)"); 
            if(!busqPorDoc) { //Se activa la busqueda por codigo y, opcionalmente, numero de version
		query.append(" WHERE EXT_TER = ?");
		queryParam.append(interesado.getIdentificador());
		if(filtroVersion){
                    query.append(" AND EXT_NVR = ?");
                    queryParam.append("-").append(interesado.getVersion());
		}
            } else { //Se activa la busqueda por numero de documento de identificacion
		query.append(" JOIN T_HTE ON (E_EXT.EXT_TER = T_HTE.HTE_TER) AND (E_EXT.EXT_NVR = T_HTE.HTE_NVR) WHERE T_HTE.HTE_DOC = ?");
		queryParam.append(interesado.getDocumento());
            }
            if(filtroProc!=null){	
                query.append(" AND EXP_PRO = ? AND EXP_EST = 0");	
                queryParam.append("-").append(filtroProc);	
            } else query.append(" AND EXP_EST != 1");
            // query += " ORDER BY EXP_FEI DESC";
            m_Log.debug("sql = " + query);
            m_Log.debug("Parametros pasados a la query: " + queryParam);
            
            ps = con.prepareStatement(query.toString());
            int contbd = 1;
            if(!busqPorDoc) {
		ps.setInt(contbd++, Integer.parseInt(interesado.getIdentificador()));
		if(filtroVersion)
                    ps.setInt(contbd++, Integer.parseInt(interesado.getVersion()));
            } else {
                ps.setString(contbd++, interesado.getDocumento());				
            }
            if(filtroProc!=null)	
                ps.setString(contbd++, filtroProc);
            
            rs = ps.executeQuery();
            while(rs.next()){
                String numero = rs.getString("EXT_NUM");
                if(numsExpediente.isEmpty() || !numsExpediente.contains(numero)){
                    numsExpediente.add(numero);
                
                    GeneralValueObject expediente = new GeneralValueObject();	
                    expediente.setAtributo("numExpediente", numero);
                    expediente.setAtributo("fechaInicio", rs.getDate("EXP_FEI"));	
                    expediente.setAtributo("asunto", rs.getString("EXP_ASU"));	
                    expediente.setAtributo("estado", rs.getInt("EXP_EST"));	
                    listado.add(expediente);
                }
            }
            
        } catch (SQLException ex){
            m_Log.error("Error al obtener los números de expediente del interesado");
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException ex) {
                m_Log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return listado;
    }

    public ArrayList<GeneralValueObject> getListaExpedientesRelacionadosInteresado(TercerosValueObject interesado, List<String> filtroProc, boolean busqPorDoc, boolean filtroVersion, Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> numsExpediente = new ArrayList<String>();
        ArrayList<GeneralValueObject> listado = new ArrayList<GeneralValueObject>();

        StringBuilder query = new StringBuilder("");
        StringBuilder queryParam = new StringBuilder("");

        try {
            // Obtenemos los expedientes del tercero con estado distinto de ANULADO (si no se indica el procedimiento)
            // o igual a PENDIENTE (si se indica el procedimiento)
            query.append("SELECT EXT_NUM,EXP_EST,EXP_FEI,EXP_ASU FROM E_EXT JOIN E_EXP ON (E_EXT.EXT_NUM = E_EXP.EXP_NUM)");
            if(!busqPorDoc) { //Se activa la busqueda por codigo y, opcionalmente, numero de version
                query.append(" WHERE EXT_TER = ?");
                queryParam.append(interesado.getIdentificador());
                if(filtroVersion){
                    query.append(" AND EXT_NVR = ?");
                    queryParam.append("-").append(interesado.getVersion());
                }
            } else { //Se activa la busqueda por numero de documento de identificacion
                query.append(" JOIN T_HTE ON (E_EXT.EXT_TER = T_HTE.HTE_TER) AND (E_EXT.EXT_NVR = T_HTE.HTE_NVR) WHERE T_HTE.HTE_DOC = ?");
                queryParam.append(interesado.getDocumento());
            }
            String inProc = "(";
            if(filtroProc!=null){
                if(filtroProc!=null) {
                    for (int i = 0 ; i < filtroProc.size() ; i++) {
                        if (i < filtroProc.size() - 1) {
                            inProc += "'" + filtroProc.get(i) + "'" + ",";
                        }
                        else {
                            inProc += "'" + filtroProc.get(i) + "'" + ")";
                        }
                    }
                }
                query.append(" AND EXP_PRO IN " + inProc + " AND EXP_EST = 0");
                queryParam.append("-").append(filtroProc);
            } else query.append(" AND EXP_EST != 1");
            // query += " ORDER BY EXP_FEI DESC";
            m_Log.debug("sql = " + query);
            m_Log.debug("Parametros pasados a la query: " + queryParam);

            ps = con.prepareStatement(query.toString());
            int contbd = 1;
            if(!busqPorDoc) {
                ps.setInt(contbd++, Integer.parseInt(interesado.getIdentificador()));
                if(filtroVersion)
                    ps.setInt(contbd++, Integer.parseInt(interesado.getVersion()));
            } else {
                ps.setString(contbd++, interesado.getDocumento());
            }

            rs = ps.executeQuery();
            while(rs.next()){
                String numero = rs.getString("EXT_NUM");
                if(numsExpediente.isEmpty() || !numsExpediente.contains(numero)){
                    numsExpediente.add(numero);

                    GeneralValueObject expediente = new GeneralValueObject();
                    expediente.setAtributo("numExpediente", numero);
                    expediente.setAtributo("fechaInicio", rs.getDate("EXP_FEI"));
                    expediente.setAtributo("asunto", rs.getString("EXP_ASU"));
                    expediente.setAtributo("estado", rs.getInt("EXP_EST"));
                    listado.add(expediente);
                }
            }

        } catch (SQLException ex){
            m_Log.error("Error al obtener los números de expediente del interesado");
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException ex) {
                m_Log.error("Error al liberar recursos de BBDD");
            }
        }

        return listado;
    }

   /************/
    public TercerosValueObject getInteresadoExpediente(int codTercero,int versionTercero,int codDomicilio,String numExpediente,Connection con)
           throws TechnicalException{

        TercerosValueObject tercero = null;        
        String sql = null;
        Statement st = null;
        ResultSet rs = null;        
                
        try {
                        
            sql = "SELECT DNN_PAI,DNN_PRV,DNN_MUN,DNN_TVI,VIA_NOM,DNN_DMC,DNN_NUD,DNN_NUH,DNN_LED,DNN_LEH,DNN_BLQ,DNN_ESC,DNN_PLT,DNN_POR," +
                  "DNN_PTA,DNN_CPO,HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2,HTE_TID,HTE_DOC,HTE_DCE,HTE_TLF " +
                  "FROM T_DNN,T_TVI,T_VIA," + GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI," + GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN," + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV, T_HTE, T_DOT " +
                  "WHERE PAI_COD = DNN_PAI AND PRV_PAI = DNN_PAI AND MUN_PAI = DNN_PAI " +
                  "AND  PRV_COD = DNN_PRV AND MUN_PRV = DNN_PRV AND  MUN_COD = DNN_MUN " +
                  "AND DNN_DOM=" + codDomicilio + " AND dnn_tvi = tvi_cod (+)  AND dnn_pai = via_pai (+) " +
                  "AND dnn_prv = via_prv (+)  AND dnn_mun = via_mun (+)  AND dnn_via = via_cod (+)" +
                  "AND T_DOT.DOT_DOM=" + codDomicilio + " AND T_DOT.DOT_TER=HTE_TER AND  HTE_TER=" + codTercero + " AND HTE_NVR=" + versionTercero;

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                //guardamos datos domicilio interesado
                DomicilioSimpleValueObject direccionVO=new DomicilioSimpleValueObject();
                direccionVO.setIdPais(rs.getString("DNN_PAI"));
                direccionVO.setIdProvincia(rs.getString("DNN_PRV"));
                direccionVO.setIdMunicipio(rs.getString("DNN_MUN"));
                direccionVO.setTipoVia(rs.getString("DNN_TVI"));
                direccionVO.setDescVia(rs.getString("VIA_NOM"));
                direccionVO.setNumDesde(rs.getString("DNN_NUD"));
                direccionVO.setNumHasta(rs.getString("DNN_NUH"));
                direccionVO.setLetraDesde(rs.getString("DNN_LED"));
                direccionVO.setLetraHasta(rs.getString("DNN_LEH"));
                direccionVO.setBloque(rs.getString("DNN_BLQ"));
                direccionVO.setEscalera(rs.getString("DNN_ESC"));
                direccionVO.setPlanta(rs.getString("DNN_PLT"));
                direccionVO.setPortal(rs.getString("DNN_POR"));
                direccionVO.setPuerta(rs.getString("DNN_PTA"));
                direccionVO.setCodigoPostal(rs.getString("DNN_CPO"));
                direccionVO.setDomicilio(rs.getString("DNN_DMC"));                
                direccionVO.setProvincia(ProvinciasDAO.getInstance().getDescripcionProvincia(Integer.parseInt(direccionVO.getIdPais()), Integer.parseInt(direccionVO.getIdProvincia()),con));                    
                direccionVO.setMunicipio(MunicipiosDAO.getInstance().getDescripcionMunicipio(Integer.parseInt(direccionVO.getIdPais()), Integer.parseInt(direccionVO.getIdProvincia()),Integer.parseInt(direccionVO.getIdMunicipio()),con));
                
                //guardamos datos interesado
                tercero=new TercerosValueObject();
                tercero.setIdentificador(Integer.toString(codTercero));
                tercero.setVersion(Integer.toString(versionTercero));
                tercero.setIdDomicilio(Integer.toString(codDomicilio));
                tercero.setNombre(rs.getString("HTE_NOM"));
                tercero.setApellido1(rs.getString("HTE_AP1"));
                tercero.setApellido2(rs.getString("HTE_AP2"));
                tercero.setTipoDocumento(rs.getInt("HTE_TID")+"");
                tercero.setDocumento(rs.getString("HTE_DOC"));
                tercero.setEmail(rs.getString("HTE_DCE"));
                tercero.setTelefono(rs.getString("HTE_TLF"));
                
                Vector domicilios = new Vector();
                domicilios.add(direccionVO);
                tercero.setDomicilios(domicilios);                
            }
            
        }catch (Exception e){
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);
        }
        
        return tercero;
   }
   
   
	
	public QueryResult getInteresadosByExpediente(Connection con, SqlBuilder sqlBuilder) {
		SqlExecuter executer = new SqlExecuter(sqlBuilder);
		try {
			return executer.executeQuery(con);
		} catch (SQLException ex) {
			m_Log.error("Error Consultar los interesados : ", ex);
		}
		return null;
	}
}
