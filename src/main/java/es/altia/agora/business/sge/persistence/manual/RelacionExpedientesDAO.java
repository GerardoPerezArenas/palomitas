package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.RelacionExpedientesValueObject;
import es.altia.agora.business.sge.FichaRelacionExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Vector;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RelacionExpedientesDAO {

  /* Declaracion de servicios */
  protected static Config m_ConfigTechnical; // Fichero de configuracion tecnico
  protected static Log m_Log =
          LogFactory.getLog(RelacionExpedientesDAO.class.getName());

  /* Instancia unica */
    private static RelacionExpedientesDAO instance = null;

    protected static String idiomaDefecto;
  protected static String sql_hte_ter; // Identificador tercero
  protected static String sql_hte_nvr; // Version tercero
  protected static String sql_hte_nan;
  protected static String sql_hte_a1a;
  protected static String sql_hte_a2a;
  protected static String sql_hte_noc;
  protected static String sql_hte_p1a;
  protected static String sql_hte_p2a;

  protected static String ext_mun;
  protected static String ext_eje;
  protected static String ext_num;
  protected static String ext_pro;
  protected static String ext_ter;
  protected static String ext_nvr;
  protected static String ext_rol;
  protected static String ext_dot;

  protected static String sql_usu_nombre;
  protected static String sql_usu_codigo;

  protected static String sql_uou_org;
  protected static String sql_uou_ent;
  protected static String sql_uou_uor;
  protected static String sql_uou_usu;

  protected static String sql_exp_codMun;
  protected static String sql_exp_codProc;
  protected static String sql_exp_tra;
  protected static String sql_exp_ejer;
  protected static String sql_exp_num;
  protected static String sql_exp_fechIni;
  protected static String sql_exp_uor;
  protected static String sql_exp_estado;
  protected static String sql_exp_asunto;

  protected static String sql_pro_codMun;
  protected static String sql_pro_cod;
  protected static String sql_pro_utr;
  protected static String sql_pro_tipo;
  protected static String sql_pro_fechHast;
  protected static String sql_pro_fechDesd;
  protected static String sql_pro_tipInic;
  protected static String sql_pro_estado;
  protected static String sql_pro_tramInternet;

  protected static String sql_pml_codMun;
  protected static String sql_pml_codProc;
  protected static String sql_pml_campo;
  protected static String sql_pml_lengua;
  protected static String sql_pml_valorCampo;

  protected static String usu_cod;
  protected static String usu_nom;

  protected static String sql_cro_mun;
  protected static String sql_cro_pro;
  protected static String sql_cro_eje;
  protected static String sql_cro_num;
  protected static String sql_cro_tra;
  protected static String sql_cro_ocu;
  protected static String sql_cro_fef;
  protected static String sql_cro_fli;
  protected static String sql_cro_uor;
  protected static String sql_cro_ffp;

  protected static String sql_tra_mun;
  protected static String sql_tra_pro;
  protected static String sql_tra_cod;
  protected static String sql_tra_utr;

  protected static String sql_tml_mun;
  protected static String sql_tml_pro;
  protected static String sql_tml_tra;
  protected static String sql_tml_cmp;
  protected static String sql_tml_leng;
  protected static String sql_tml_valor;

  protected static String rol_mun;
  protected static String rol_pro;
  protected static String rol_cod;
  protected static String rol_des;
  protected static String rol_pde;

  protected static String sql_nex_codMun;
  protected static String sql_nex_codProc;
  protected static String sql_nex_uor;
  protected static String sql_nex_num;
  protected static String sql_nex_ejer;

  protected static String sql_rel_codMun;
  protected static String sql_rel_codProc;
  protected static String sql_rel_ejer;
  protected static String sql_rel_num;
  protected static String sql_rel_estado;
  protected static String sql_rel_fecIni;
  protected static String sql_rel_usuIni;
  protected static String sql_rel_uorIni;
  protected static String sql_rel_fecFin;
  protected static String sql_rel_usuFin;
  protected static String sql_rel_uorFin;
  protected static String sql_rel_traIni;
  protected static String sql_rel_traAct;
  protected static String sql_rel_asunto;
  protected static String sql_rel_obs;

  protected static String sql_rel_exp_codMun;
  protected static String sql_rel_exp_codProc;
  protected static String sql_rel_exp_ejer;
  protected static String sql_rel_exp_num;
  protected static String sql_rel_exp_codMunR;
  protected static String sql_rel_exp_codProcR;
  protected static String sql_rel_exp_ejerR;
  protected static String sql_rel_exp_numR;

  protected static String sql_rel_cro_mun;
  protected static String sql_rel_cro_pro;
  protected static String sql_rel_cro_eje;
  protected static String sql_rel_cro_num;
  protected static String sql_rel_cro_tra;
  protected static String sql_rel_cro_ocu;
  protected static String sql_rel_cro_fef;
  protected static String sql_rel_cro_fli;
  protected static String sql_rel_cro_uor;
  protected static String sql_rel_cro_ffp;

  protected static String sql_blq_mun;
  protected static String sql_blq_pro;
  protected static String sql_blq_eje;
  protected static String sql_blq_num;
  protected static String sql_blq_tra;
  protected static String sql_blq_ocu;
  protected static String sql_blq_usu;
  
  private String limiteFilasMostrar = null;

  protected RelacionExpedientesDAO() {
    super();
    // Fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    

    idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
    sql_hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
    sql_hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");
    sql_hte_nan = m_ConfigTechnical.getString("SQL.T_HTE.nombre");
    sql_hte_a1a = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
    sql_hte_a2a = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
    sql_hte_p1a = m_ConfigTechnical.getString("SQL.T_HTE.partApellido1");
    sql_hte_p2a = m_ConfigTechnical.getString("SQL.T_HTE.partApellido2");

    ext_mun = m_ConfigTechnical.getString("SQL.E_EXT.codMunicipio");
    ext_eje = m_ConfigTechnical.getString("SQL.E_EXT.ano");
    ext_num = m_ConfigTechnical.getString("SQL.E_EXT.numero");
    ext_pro = m_ConfigTechnical.getString("SQL.E_EXT.codProcedimiento");
    ext_ter = m_ConfigTechnical.getString("SQL.E_EXT.codTercero");
    ext_nvr = m_ConfigTechnical.getString("SQL.E_EXT.verTercero");
    ext_rol = m_ConfigTechnical.getString("SQL.E_EXT.rolTercero");
    ext_dot = m_ConfigTechnical.getString("SQL.E_EXT.codDomicilio");

    sql_uou_uor= m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
    sql_uou_usu= m_ConfigTechnical.getString("SQL.A_UOU.usuario");
    sql_uou_org= m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
    sql_uou_ent= m_ConfigTechnical.getString("SQL.A_UOU.entidad");

    sql_usu_nombre = m_ConfigTechnical.getString("SQL.A_USU.nombre");
    sql_usu_codigo = m_ConfigTechnical.getString("SQL.A_USU.codigo");

    sql_exp_codMun= m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
    sql_exp_codProc= m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
    sql_exp_ejer= m_ConfigTechnical.getString("SQL.E_EXP.ano");
    sql_exp_num= m_ConfigTechnical.getString("SQL.E_EXP.numero");
    sql_exp_fechIni= m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio");
    sql_exp_uor=  m_ConfigTechnical.getString("SQL.E_EXP.uor");
    sql_exp_estado= m_ConfigTechnical.getString("SQL.E_EXP.estado");
    sql_exp_asunto= m_ConfigTechnical.getString("SQL.E_EXP.asunto");
    sql_exp_tra= m_ConfigTechnical.getString("SQL.E_EXP.codTramiteUltCerrado");

    sql_pro_codMun= m_ConfigTechnical.getString("SQL.E_PRO.municipio");
    sql_pro_cod= m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
    sql_pro_fechHast= m_ConfigTechnical.getString("SQL.E_PRO.fechaLimiteHasta");
    sql_pro_fechDesd= m_ConfigTechnical.getString("SQL.E_PRO.fechaLimiteDesde");
    sql_pro_utr=m_ConfigTechnical.getString("SQL.E_PRO.unidad");
    sql_pro_tipo= m_ConfigTechnical.getString("SQL.E_PRO.tipoProcedimiento");
    sql_pro_tipInic= m_ConfigTechnical.getString("SQL.E_PRO.tipoInicio");
    sql_pro_estado= m_ConfigTechnical.getString("SQL.E_PRO.estadoProcedimiento");
    sql_pro_tramInternet= m_ConfigTechnical.getString("SQL.E_PRO.tramitacionInternet");

    sql_pml_codMun= m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");
    sql_pml_codProc= m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
    sql_pml_campo= m_ConfigTechnical.getString("SQL.E_PML.codCampoML");
    sql_pml_lengua= m_ConfigTechnical.getString("SQL.E_PML.idioma");
    sql_pml_valorCampo= m_ConfigTechnical.getString("SQL.E_PML.valor");

    usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
    usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");

    sql_cro_mun = m_ConfigTechnical.getString("SQL.E_CRO.codMunicipio");
    sql_cro_pro = m_ConfigTechnical.getString("SQL.E_CRO.codProcedimiento");
    sql_cro_eje = m_ConfigTechnical.getString("SQL.E_CRO.ano");
    sql_cro_num = m_ConfigTechnical.getString("SQL.E_CRO.numero");
    sql_cro_tra = m_ConfigTechnical.getString("SQL.E_CRO.codTramite");
    sql_cro_ocu = m_ConfigTechnical.getString("SQL.E_CRO.ocurrencia");
    sql_cro_fef = m_ConfigTechnical.getString("SQL.E_CRO.fechaFin");
    sql_cro_fli = m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite");
    sql_cro_uor = m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora");
    sql_cro_ffp = m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo");

    sql_blq_mun = m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codMunicipio");
    sql_blq_pro = m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codProcedimiento");
    sql_blq_eje = m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ano");
    sql_blq_num = m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.numero");
    sql_blq_tra = m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.tramite");
    sql_blq_ocu = m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ocurrencia");
    sql_blq_usu = m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.usuario");

    sql_tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
    sql_tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
    sql_tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
    sql_tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");

    sql_tml_mun = m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
    sql_tml_pro = m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
    sql_tml_tra = m_ConfigTechnical.getString("SQL.E_TML.codTramite");
    sql_tml_cmp = m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
    sql_tml_leng = m_ConfigTechnical.getString("SQL.E_TML.idioma");
    sql_tml_valor = m_ConfigTechnical.getString("SQL.E_TML.valor");

    rol_mun = m_ConfigTechnical.getString("SQL.E_ROL.codMunicipio");
    rol_pro = m_ConfigTechnical.getString("SQL.E_ROL.codProcedimiento");
    rol_cod = m_ConfigTechnical.getString("SQL.E_ROL.codRol");
    rol_des = m_ConfigTechnical.getString("SQL.E_ROL.descripcion");
    rol_pde = m_ConfigTechnical.getString("SQL.E_ROL.porDefecto");

    sql_nex_codMun= m_ConfigTechnical.getString("SQL.G_NEX.codMunicipio");
    sql_nex_codProc= m_ConfigTechnical.getString("SQL.G_NEX.codProcedimiento");
    sql_nex_num= m_ConfigTechnical.getString("SQL.G_NEX.numero");
    sql_nex_ejer= m_ConfigTechnical.getString("SQL.G_NEX.ejercicio");

    sql_rel_codMun= m_ConfigTechnical.getString("SQL.G_REL.codMunicipio");
    sql_rel_codProc= m_ConfigTechnical.getString("SQL.G_REL.codProcedimiento");
    sql_rel_ejer= m_ConfigTechnical.getString("SQL.G_REL.ano");
    sql_rel_num= m_ConfigTechnical.getString("SQL.G_REL.numero");
    sql_rel_estado= m_ConfigTechnical.getString("SQL.G_REL.estado");
    sql_rel_fecIni= m_ConfigTechnical.getString("SQL.G_REL.fechaInicio");
    sql_rel_usuIni= m_ConfigTechnical.getString("SQL.G_REL.usuarioInicio");
    sql_rel_uorIni=  m_ConfigTechnical.getString("SQL.G_REL.uorInicio");
    sql_rel_fecFin= m_ConfigTechnical.getString("SQL.G_REL.fechaFin");
    sql_rel_usuFin= m_ConfigTechnical.getString("SQL.G_REL.usuarioFin");
    sql_rel_uorFin=  m_ConfigTechnical.getString("SQL.G_REL.uorFin");
    sql_rel_traIni=  m_ConfigTechnical.getString("SQL.G_REL.codTramiteInicio");
    sql_rel_traAct= m_ConfigTechnical.getString("SQL.G_REL.codTramiteActual");
    sql_rel_asunto= m_ConfigTechnical.getString("SQL.G_REL.asunto");
    sql_rel_obs= m_ConfigTechnical.getString("SQL.G_REL.observaciones");

    sql_rel_exp_codMun= m_ConfigTechnical.getString("SQL.G_EXP.codMunicipio");
    sql_rel_exp_codProc= m_ConfigTechnical.getString("SQL.G_EXP.codProcedimiento");
    sql_rel_exp_ejer= m_ConfigTechnical.getString("SQL.G_EXP.ejercicio");
    sql_rel_exp_num= m_ConfigTechnical.getString("SQL.G_EXP.numero");
    sql_rel_exp_codMunR= m_ConfigTechnical.getString("SQL.G_EXP.codMunicipioR");
    sql_rel_exp_codProcR= m_ConfigTechnical.getString("SQL.G_EXP.codProcedimientoR");
    sql_rel_exp_ejerR= m_ConfigTechnical.getString("SQL.G_EXP.ejercicioR");
    sql_rel_exp_numR= m_ConfigTechnical.getString("SQL.G_EXP.numeroR");

    sql_rel_cro_mun = m_ConfigTechnical.getString("SQL.G_CRO.codMunicipio");
    sql_rel_cro_pro = m_ConfigTechnical.getString("SQL.G_CRO.codProcedimiento");
    sql_rel_cro_eje = m_ConfigTechnical.getString("SQL.G_CRO.ano");
    sql_rel_cro_num = m_ConfigTechnical.getString("SQL.G_CRO.numero");
    sql_rel_cro_tra = m_ConfigTechnical.getString("SQL.G_CRO.codTramite");
    sql_rel_cro_ocu = m_ConfigTechnical.getString("SQL.G_CRO.ocurrencia");
    sql_rel_cro_fef = m_ConfigTechnical.getString("SQL.G_CRO.fechaFin");
    sql_rel_cro_fli = m_ConfigTechnical.getString("SQL.G_CRO.fechaLimite");
    sql_rel_cro_uor = m_ConfigTechnical.getString("SQL.G_CRO.codUnidadTramitadora");
    sql_rel_cro_ffp = m_ConfigTechnical.getString("SQL.G_CRO.fechaFinPlazo");
  }


  public static RelacionExpedientesDAO getInstance() {
    if (instance == null) {
      // Sincronizacion para serializar (no multithread) las invocaciones de este metodo.
      synchronized(TramitacionDAO.class) {
        if (instance == null)
          instance = new RelacionExpedientesDAO();
      }
    }
    return instance;
  }

  public Vector getExpedientesPendientes(UsuarioValueObject uVO, String codProcedimiento, String codTramite, String codUtr, String[] params)
  throws TramitacionException, TechnicalException {

    m_Log.debug("getExpedientesPendientes en RelacionExpedientesDAO");

    Vector lista = new Vector();
    Vector listaFinal = new Vector();

    AdaptadorSQLBD oad = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql="";

    try{
      oad = new AdaptadorSQLBD(uVO.getParamsCon());
        con = oad.getConnection();
        if(m_Log.isDebugEnabled()) m_Log.debug("PROCEDIMIENTO ........... "+codProcedimiento);
        if(m_Log.isDebugEnabled()) m_Log.debug("TRAMITE ................. "+codTramite);

        ResourceBundle propertiesExp = ResourceBundle.getBundle("Expediente");  
        String numFilasMostrar = "all";
        String limiteO = "";
        String limiteS = "";
        
        try{
            numFilasMostrar = propertiesExp.getString(uVO.getOrgCod() + ConstantesDatos.BARRA + "CREAR_RELACION_EXPEDIENTES_MOSTRADOS"); 
            int valor = Integer.parseInt(numFilasMostrar);
        }catch(Exception e){
            m_Log.error(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + ConstantesDatos.BARRA + "CREAR_RELACION_EXPEDIENTES_MOSTRADOS" + " EN Expediente.properties");
            numFilasMostrar="all";
        } 
        this.limiteFilasMostrar = numFilasMostrar;
        if(!numFilasMostrar.equals("all")){
            if(params[0]!=null && params[0].equalsIgnoreCase("oracle")){
                limiteO = " AND ROWNUM BETWEEN 1 AND "+numFilasMostrar;
            } else if(params[0]!=null) {
                limiteS = "TOP "+numFilasMostrar+" ";
            }
        }
        
        sql = "SELECT DISTINCT "+ limiteS + sql_exp_codMun+ ","+sql_exp_codProc+ "," +sql_exp_ejer+ ","+sql_exp_num
                       + ","+oad.convertir(sql_exp_fechIni,oad.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fIni " + "," +
                       sql_pml_valorCampo + "," + sql_exp_fechIni + "," + sql_exp_asunto + "," + sql_blq_usu + "," + usu_nom;
        sql += " FROM E_CRO";
        sql += " LEFT JOIN E_EXP ON (" + sql_exp_codMun + "=" + sql_cro_mun + " AND " +
                 sql_exp_ejer + "=" +  sql_cro_eje + " AND " +
                                         sql_exp_num + "=" + sql_cro_num +") ";
        sql += "LEFT JOIN E_TRA ON (" + sql_cro_mun +"="+ sql_tra_mun + " AND " +
                 sql_cro_pro +"="+ sql_tra_pro + " AND " +
                                        sql_cro_tra + "="+ sql_tra_cod +") ";
        sql += "LEFT JOIN E_EXP_BLOQ ON (" + sql_cro_mun +"="+ sql_blq_mun + " AND " +
                 sql_cro_pro +"="+ sql_blq_pro + " AND " +
                 sql_cro_eje +"="+ sql_blq_eje + " AND " +
                 sql_cro_num +"="+ sql_blq_num + " AND " +
                 sql_cro_tra +"="+ sql_blq_tra + " AND " +
                                             sql_cro_tra + "="+ sql_blq_tra +") ";
        sql += "LEFT JOIN "+ GlobalNames.ESQUEMA_GENERICO + "a_usu ON (" + usu_cod + " = " + sql_blq_usu +")";
        sql += "INNER JOIN E_PRO ON ("+ sql_exp_codMun + "="+ sql_pro_codMun + " AND " +
                                        sql_exp_codProc + "="+ sql_pro_cod + ") ";
        sql += "INNER JOIN E_PML ON (" + sql_exp_codMun + "=" + sql_pml_codMun + " AND " +
                                         sql_exp_codProc + "=" + sql_pml_codProc + ")";
        sql += " WHERE " + sql_exp_codProc + "='" + codProcedimiento
                       + "' AND " + sql_cro_tra +"="+codTramite
                       + " AND CRO_UTR = " + codUtr
                       + " AND " + sql_cro_fef + " IS NULL AND ";
        sql += sql_pml_campo + "='NOM' "
                        + " AND " + sql_pml_lengua + "='"+idiomaDefecto+"' "
                        + " AND " + sql_exp_estado + " = 0 "
                        + " AND ((exists ( "
                        + " SELECT DISTINCT " +sql_uou_uor
                        + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU "
                        + " WHERE "+ sql_uou_usu + "=" + uVO.getIdUsuario()
                        + " AND "+ sql_uou_org +"="+ uVO.getOrgCod()
                        + " AND "+ sql_uou_ent +"="+ uVO.getEntCod()
                        + " AND "+ sql_uou_uor +"="+ sql_exp_uor
                        + "))OR("
                        + " exists (SELECT DISTINCT "+ sql_uou_uor
                        + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU "
                        + " WHERE "+ sql_uou_usu +"="+ uVO.getIdUsuario()
                        + " AND "+ sql_uou_org +"="+ uVO.getOrgCod()
                        + " AND "+ sql_uou_ent +"="+ uVO.getEntCod()
                        + " AND " + sql_uou_uor + "=" + sql_cro_uor +")) "
                        + " AND " + sql_cro_fef + " IS NULL "
                        + " ) ";

   
        // QUITAR LOS EXPEDIENTES QUE ESTEAN EN OTRA RELACIÓN ABIERTA
        sql += " AND (not exists (SELECT DISTINCT EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM"
                     + " FROM G_REL,G_EXP"
                     + " WHERE G_REL.REL_MUN = G_EXP.REL_MUN AND G_REL.REL_PRO = G_EXP.REL_PRO AND"
                     + " G_REL.REL_EJE = G_EXP.REL_EJE AND G_REL.REL_NUM = G_EXP.REL_NUM AND"
                     + " G_EXP.EXP_MUN = E_EXP.EXP_MUN AND G_EXP.EXP_PRO = E_EXP.EXP_PRO AND"
                     + " G_EXP.EXP_EJE = E_EXP.EXP_EJE AND G_EXP.EXP_NUM = E_EXP.EXP_NUM AND G_REL.REL_EST=0))";

        sql+=limiteO;

        String pOrder = " ORDER BY "+ sql_exp_fechIni + " DESC ";

        sql += pOrder;

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = con.prepareStatement(sql);
        rs = stmt.executeQuery();
        while(rs.next()){
          TramitacionValueObject tvo1 = new TramitacionValueObject();
          tvo1.setCodMunicipio((String) rs.getString(sql_exp_codMun));
          tvo1.setCodProcedimiento((String) rs.getString(sql_exp_codProc));
          tvo1.setDescProcedimiento((String) rs.getString(sql_pml_valorCampo));
          tvo1.setEjercicio((String) rs.getString(sql_exp_ejer));
          tvo1.setNumero((String) rs.getString(sql_exp_num));
          tvo1.setFechaInicioExpediente((String) rs.getString("fIni"));
          String asuntoExp = rs.getString(sql_exp_asunto);
          if (asuntoExp != null)
              tvo1.setAsuntoExp(oad.js_escape(asuntoExp));
          else tvo1.setAsuntoExp("");
          String bloqueo = rs.getString(sql_blq_usu);
          if (bloqueo != null) {
              tvo1.setBloqUsuCod(rs.getString(sql_blq_usu));
              tvo1.setBloqUsuNom(rs.getString(usu_nom));
          }
          else {
              tvo1.setBloqUsuCod("");
              tvo1.setBloqUsuNom("");
          }
          lista.addElement(tvo1);
        }
        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(stmt);

        for(int i=0;i<lista.size();i++) {
          String fueraDePlazo = "no";
          String pendiente = "no";
          TramitacionValueObject tvo1 = new TramitacionValueObject();
          tvo1 = (TramitacionValueObject) lista.elementAt(i);

          int codRolPD = -1;
          sql = "SELECT " + rol_cod + " FROM E_ROL WHERE " + rol_mun + "=" + tvo1.getCodMunicipio() +
                " AND " + rol_pro + "='" + tvo1.getCodProcedimiento() + "' AND " + rol_pde +
                "=1";
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = con.prepareStatement(sql);
          rs = stmt.executeQuery();
          while(rs.next()) {
            codRolPD = rs.getInt(rol_cod);
          }
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(stmt);

            sql = "SELECT " + oad.convertir(sql_cro_fli,oad.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS fLimite, " + oad.convertir(sql_cro_fef,oad.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS fFin,"+ oad.convertir(sql_cro_ffp,oad.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS fFinPlazo, (CASE WHEN ((exists (SELECT DISTINCT "+ sql_uou_uor +
                                                        " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU " +
                                                        " WHERE " + sql_uou_usu +"="+ uVO.getIdUsuario()+ " AND "+
                                                                    sql_uou_org +"="+ uVO.getOrgCod() + " AND " +
                                                                    sql_uou_ent +"="+ uVO.getEntCod() + " AND " +
                                                                    sql_uou_uor + "=" + sql_cro_uor +")) " +
                                    " AND " + sql_cro_fef + " IS NULL ) THEN 'A' ELSE '' END ) AS PENDIENTE" +
                    " FROM E_CRO"
                    + " LEFT JOIN E_EXP ON (CRO_MUN=EXP_MUN AND CRO_EJE=EXP_EJE AND CRO_NUM=EXP_NUM)"
                    + " WHERE " + sql_cro_mun + "=" + tvo1.getCodMunicipio() +
                    " AND " + sql_cro_eje + "=" + tvo1.getEjercicio() +
                    " AND " + sql_cro_pro + "='" + tvo1.getCodProcedimiento() +
                    "' AND " + sql_cro_num + "='" + tvo1.getNumero()+"'" ;

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = con.prepareStatement(sql);
          rs = stmt.executeQuery(); 
          while(rs.next()) {
            String fechaLimite = rs.getString("fLimite");
            String fechaFinPlazo = rs.getString("fFinPlazo");
            String fechaFinTramite = rs.getString("fFin");
            if(fechaFinTramite == null || "".equals(fechaFinTramite))
            {// el tramite no esta finalizado
             // Se cambio para que en caso de estar rellana la fecha fin de plazo
             // no se ponga el expediente en rojo(no esta fuera de plazo)
              if(fechaLimite != null && !"".equals(fechaLimite) &&
                 (fechaFinPlazo == null || "".equals(fechaFinPlazo)))
              {
                Calendar calendario = Calendar.getInstance();
                java.util.Date date = calendario.getTime();
                Fecha fecha = new Fecha();
                java.util.Date dateLimite = fecha.obtenerDate(fechaLimite);
                if(date.compareTo(dateLimite) >=0) {
                  fueraDePlazo = "si";
                }
              }
            }
            String pend = rs.getString("PENDIENTE");
            if(pend != null && pend.equals("A")) {
                pendiente = "si";
            }
          }
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(stmt);

            sql = "SELECT " + ext_rol + "," + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{sql_hte_p1a, "''", sql_hte_a1a}) + " AS ap1, " +
                    oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{sql_hte_p2a, "''", sql_hte_a2a}) + " AS ap2, " + sql_hte_nan + " AS nombre" +
                    " FROM E_EXT"
                    + " LEFT JOIN T_HTE ON (EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR)"
                    + " WHERE " + ext_mun + "=" + tvo1.getCodMunicipio() + " AND " +
                    ext_eje + "=" + tvo1.getEjercicio() + " AND " + ext_num + "='" +
                    tvo1.getNumero() + 
                    "' AND MOSTRAR = 1 ORDER BY 1";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            
          stmt = con.prepareStatement(sql);
          rs = stmt.executeQuery();
          String entrar = "no";
          if (rs.next()){
                     String ap1=rs.getString("ap1");
                     String ap2=rs.getString("ap2");
                     String nombre=rs.getString("nombre");
                     String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);                              
              
            int rol = rs.getInt(ext_rol);
            if(rol == codRolPD) {
              if(entrar.equals("no")) {
                tvo1.setTitular(titular);
                entrar = "si";
              }
            } else {
              if(entrar.equals("no")) {
                if("  , ".equals(titular))  tvo1.setTitular("");
                else tvo1.setTitular(titular);
              }
            }
          }
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(stmt);
          
          tvo1.setFueraDePlazo(fueraDePlazo);
          tvo1.setPendiente(pendiente);
          listaFinal.addElement(tvo1);
        }
        
     } catch (SQLException ex) {
        listaFinal = null;
        ex.printStackTrace();
        if (m_Log.isErrorEnabled())  m_Log.error(ex.getMessage());
        throw new TramitacionException("Error. TramitacionDAO.getExpedientesPendientes", ex);
    } catch (BDException e) {
        listaFinal = null;
        e.printStackTrace();
        if (m_Log.isErrorEnabled())  m_Log.error(e.getMessage());
        throw new TramitacionException("Error. TramitacionDAO.getExpedientesPendientes", e);
      } finally {
        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(stmt);
        SigpGeneralOperations.devolverConexion(oad, con);
      }

    m_Log.debug("getListaExpedientesPendientes en TramitacionDAO");
    return listaFinal;
  }
  
  private String getTramiteActualAbiertoRelacion(String numExpedienteRelacion,String codOrganizacion, String codProcedimiento, String ejercicio ,Connection con){
      
      String nombre = "";
      Statement st = null;
      ResultSet rs = null;
      
      try{
          String sql = "SELECT TML_VALOR FROM G_CRO,E_TML WHERE CRO_NUM='" + numExpedienteRelacion + "' " + 
                       "AND CRO_TRA=TML_TRA AND CRO_MUN=TML_MUN AND CRO_PRO=TML_PRO AND CRO_PRO='" + codProcedimiento + "' " + 
                       "AND CRO_EJE=" + ejercicio + " AND CRO_MUN=" + codOrganizacion + " AND CRO_FEF IS NULL " + 
                       "ORDER BY CRO_TRA DESC";
          
          m_Log.debug(sql);
          
          st = con.createStatement();
          rs = st.executeQuery(sql);
          
          while(rs.next()){
              nombre = rs.getString("TML_VALOR");
              break;
          }
          
          
      }catch(SQLException e){
          
      }finally{
          try{
              if(st!=null) st.close();
              if(rs!=null) rs.close();
              
          }catch(SQLException e){
              m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
          }
          
          return nombre;
      }
  }
  
  
  public Vector getRelacionesPendientes(UsuarioValueObject uVO, String codProcedimiento, String codTramite,String[] params)
    throws TramitacionException, TechnicalException {

      m_Log.debug("getExpedientesPendientes en RelacionExpedientesDAO");

      Vector listaFinal = new Vector();

      AdaptadorSQLBD oad = null;
      Connection con = null;
      PreparedStatement stmt = null;
      ResultSet rs = null;
      String sql="";
      String where = "";
      String from = "";

      try{
        oad = new AdaptadorSQLBD(uVO.getParamsCon());
          con = oad.getConnection();
          if(m_Log.isDebugEnabled()) m_Log.debug("PROCEDIMIENTO ........... "+codProcedimiento);
          if(m_Log.isDebugEnabled()) m_Log.debug("TRAMITE ................. "+codTramite);

          sql = "SELECT DISTINCT " + sql_rel_codMun + ", " + sql_rel_ejer + ", " + sql_rel_codProc + ", "  + sql_rel_num + ", " +
                  oad.convertir(sql_rel_fecIni,oad.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS fInicio, " +
                   sql_usu_nombre + ", INICIO.TML_VALOR AS INICIAL , ACTUAL.TML_VALOR AS ACTUAL, REL_FIN, REL_UOI ";
          from = " FROM G_REL, G_CRO, " + GlobalNames.ESQUEMA_GENERICO + "A_USU, E_TML INICIO, E_TML ACTUAL ";
          where = " WHERE " + sql_rel_usuIni + "=" + sql_usu_codigo + " AND " +
                  sql_rel_codProc + " = '" + codProcedimiento + "' AND " +
                  sql_rel_estado + "=0 "
                  + "AND ACTUAL.TML_TRA=REL_TRAI AND ACTUAL.TML_PRO=REL_PRO AND REL_TRI = INICIO.TML_TRA "
                  + "AND INICIO.TML_PRO=REL_PRO AND CRO_NUM=REL_NUM ";



          if (!codTramite.equals(null) && !codTramite.equals("")) where += " AND " + sql_rel_cro_tra + "=" + codTramite
                  + " AND " + sql_rel_cro_fef + " IS NULL";
          String pOrder = " ORDER BY "+ sql_rel_fecIni + " DESC ";

          sql += from + where + pOrder;

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = con.prepareStatement(sql);
          rs = stmt.executeQuery();
          while(rs.next()){
            if(m_Log.isDebugEnabled()) m_Log.debug("RELACIÓN ................. "+rs.getString(sql_rel_num));
            FichaRelacionExpedientesValueObject reVO = new FichaRelacionExpedientesValueObject();
            
            String numExpedienteRelacion = rs.getString(sql_rel_num);
            String codMunicipio = rs.getString(sql_rel_codMun);
            String ejercicio = rs.getString(sql_rel_ejer);
            
            
            reVO.setCodMunicipio(codMunicipio);
            reVO.setEjercicio(ejercicio);
            reVO.setCodProcedimiento(codProcedimiento);
            reVO.setNumeroRelacion(numExpedienteRelacion);
            reVO.setFechaInicio((String) rs.getString("fInicio"));
            reVO.setUsuarioInicio((String) rs.getString(sql_usu_nombre));
            reVO.setTramiteInicio((String) rs.getString("INICIAL"));
            //reVO.setTramiteActual((String) rs.getString("ACTUAL"));
            reVO.setTramiteActual(getTramiteActualAbiertoRelacion(numExpedienteRelacion,codMunicipio,codProcedimiento, ejercicio ,con));
             reVO.setCodUorInicial(rs.getString("REL_UOI"));
            
            
            listaFinal.addElement(reVO);
          }
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(stmt);

          for (int i=0; i<listaFinal.size(); i++) {
              String codUsuario = "";
              String nomUsuario = "";
              FichaRelacionExpedientesValueObject reVO = (FichaRelacionExpedientesValueObject)listaFinal.get(i);
              sql = "SELECT " + sql_blq_usu + ", " + sql_usu_nombre +
                    " FROM G_EXP, E_EXP_BLOQ, " + GlobalNames.ESQUEMA_GENERICO+"A_USU " +
                    " WHERE " + sql_rel_exp_codMun + " = " + reVO.getCodMunicipio()+ " AND " +
                                sql_rel_exp_ejer + " = " + reVO.getEjercicio() + " AND " +
                                sql_rel_exp_codProc + " = '" + reVO.getCodProcedimiento() +"' AND " +
                                sql_rel_exp_num + "='" + reVO.getNumeroRelacion() + "' AND " +
                                sql_rel_exp_codMunR + " = " + sql_blq_mun + " AND " +
                                sql_rel_exp_ejerR + " = " + sql_blq_eje + " AND " +
                                sql_rel_exp_codProcR + " = " + sql_blq_pro + " AND " +
                                sql_rel_exp_numR + " = " + sql_blq_num + " AND " +
                                sql_usu_codigo + " = " + sql_blq_usu;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              stmt = con.prepareStatement(sql);
              rs = stmt.executeQuery();
              while (rs.next()) {
                  codUsuario = rs.getString(sql_blq_usu);
                  nomUsuario = rs.getString(sql_usu_nombre);
                  if (!codUsuario.equals(String.valueOf(uVO.getIdUsuario()))) break;
              }
              reVO.setCodBloqueo(codUsuario);
              reVO.setNomBloqueo(nomUsuario);
              SigpGeneralOperations.closeResultSet(rs);
              SigpGeneralOperations.closeStatement(stmt);
          }
        } catch (BDException e) {
            listaFinal = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TramitacionException("Error. RelacionExpedientesDAO.getRelacionesPendientes", e);
        } catch (SQLException e) {
            listaFinal = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TramitacionException("Error. RelacionExpedientesDAO.getRelacionesPendientes", e);
        } finally {
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(stmt);
          SigpGeneralOperations.devolverConexion(oad, con);
        }

      m_Log.debug("getRelacionesPendientes en RelacionExpedientesDAO");
      return listaFinal;
    }

    public Vector getRelacionExpedientesPendientes(UsuarioValueObject uVO, RelacionExpedientesValueObject reVO, String[] params)
    throws TramitacionException, TechnicalException {

        m_Log.debug("getRelacionExpedientesPendientes en RelacionExpedientesDAO");

        Vector<RelacionExpedientesValueObject> lista = new Vector<RelacionExpedientesValueObject>();
        Vector<RelacionExpedientesValueObject> listaFinal = new Vector<RelacionExpedientesValueObject>();

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            oad = new AdaptadorSQLBD(uVO.getParamsCon());
            con = oad.getConnection();

            String from = "DISTINCT " + sql_rel_codMun + "," + sql_rel_codProc + "," + sql_rel_ejer + "," + sql_rel_num
                    + "," + oad.convertir(sql_rel_fecIni, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni " + "," +
                    sql_pml_valorCampo + "," + sql_rel_fecIni + "," + sql_rel_asunto;
            ArrayList<String> join = new ArrayList<String>();
            join.add(" G_CRO");
            join.add("LEFT");
            join.add("G_REL");
            join.add(sql_rel_codMun + "=" + sql_rel_cro_mun + " AND " +
                    sql_rel_ejer + "=" + sql_rel_cro_eje + " AND " +
                    sql_rel_num + "=" + sql_rel_cro_num);
            join.add("LEFT");
            join.add("E_TRA");
            join.add(sql_rel_cro_mun + "=" + sql_tra_mun + " AND " +
                    sql_rel_cro_pro + "=" + sql_tra_pro + " AND " +
                    sql_rel_cro_tra + "=" + sql_tra_cod);
            join.add("INNER");
            join.add("E_PRO");
            join.add(sql_rel_codMun + "=" + sql_pro_codMun + " AND " +
                    sql_rel_codProc + "=" + sql_pro_cod);
            join.add("INNER");
            join.add("E_PML");
            join.add(sql_rel_codMun + "=" + sql_pml_codMun + " AND " +
                    sql_rel_codProc + "=" + sql_pml_codProc);
            join.add("false");

            String where = sql_pml_campo + "='NOM' "
                    + " AND " + sql_pml_lengua + "='"+idiomaDefecto+"' "
                    + " AND " + sql_rel_estado + " = 0 "
                    + " AND ((exists ( "
                    + " SELECT DISTINCT " + sql_uou_uor
                    + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU "
                    + " WHERE " + sql_uou_usu + "=" + uVO.getIdUsuario()
                    + " AND " + sql_uou_org + "=" + uVO.getOrgCod()
                    + " AND " + sql_uou_ent + "=" + uVO.getEntCod()
                    + " AND " + sql_uou_uor + "=" + sql_rel_uorIni
                    + "))OR("
                    + " exists (SELECT DISTINCT " + sql_uou_uor
                    + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU "
                    + " WHERE " + sql_uou_usu + "=" + uVO.getIdUsuario()
                    + " AND " + sql_uou_org + "=" + uVO.getOrgCod()
                    + " AND " + sql_uou_ent + "=" + uVO.getEntCod()
                    + " AND " + sql_uou_uor + "=" + sql_rel_cro_uor + ")) "
                    + " AND " + sql_rel_cro_fef + " IS NULL "
                    + " ) ";

            String sql2 = oad.join(from, where, join.toArray(new String[]{}));

            String pOrder = " ORDER BY " + sql_rel_fecIni + " DESC ";

            String sql = sql2 + pOrder;

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                RelacionExpedientesValueObject tvo1 = new RelacionExpedientesValueObject();
                tvo1.setCodMunicipio(rs.getString(sql_rel_codMun));
                tvo1.setCodProcedimiento(rs.getString(sql_rel_codProc));
                tvo1.setDescProcedimiento(rs.getString(sql_pml_valorCampo));
                tvo1.setEjercicio(rs.getString(sql_rel_ejer));
                tvo1.setNumeroRelacion(rs.getString(sql_rel_num));
                tvo1.setFechaInicio(rs.getString("fIni"));
                String asuntoExp = rs.getString(sql_rel_asunto);
                if (asuntoExp != null)
                    tvo1.setAsunto(AdaptadorSQLBD.js_escape(asuntoExp));
                else tvo1.setAsunto("");
                lista.addElement(tvo1);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);

            for (int i = 0; i < lista.size(); i++) {
                String fueraDePlazo = "no"; 
                String pendiente = "no";
                RelacionExpedientesValueObject tvo1 = lista.elementAt(i);

                sql = "SELECT " + oad.convertir(sql_rel_cro_fli, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                        " AS fLimite, " + oad.convertir(sql_rel_cro_fef, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                        " AS fFin," + oad.convertir(sql_rel_cro_ffp, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                        " AS fFinPlazo, (CASE WHEN ((exists (SELECT DISTINCT " + sql_uou_uor +
                        " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                        " WHERE " + sql_uou_usu + "=" + uVO.getIdUsuario() + " AND " +
                        sql_uou_org + "=" + uVO.getOrgCod() + " AND " +
                        sql_uou_ent + "=" + uVO.getEntCod() + " AND " +
                        sql_uou_uor + "=" + sql_cro_uor + ")) " +
                        " AND " + sql_rel_cro_fef + " IS NULL ) THEN 'A' ELSE '' END ) AS PENDIENTE" +
                        " FROM G_CRO"
                        + " LEFT JOIN G_REL ON (CRO_MUN=REL_MUN AND CRO_EJE=REL_EJE AND CRO_NUM=REL_NUM)"
                        + " WHERE " + sql_rel_cro_mun + "=" + tvo1.getCodMunicipio() +
                        " AND " + sql_rel_cro_eje + "=" + tvo1.getEjercicio() +
                        " AND " + sql_rel_cro_pro + "='" + tvo1.getCodProcedimiento() +
                        "' AND " + sql_rel_cro_num + "='" + tvo1.getNumeroRelacion()+"'";
                     

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String fechaLimite = rs.getString("fLimite");
                    String fechaFinPlazo = rs.getString("fFinPlazo");
                    String fechaFinTramite = rs.getString("fFin");
                    if (fechaFinTramite == null || "".equals(fechaFinTramite)) {
                        // Se cambio para que en caso de estar rellana la fecha fin de plazo
                        // no se ponga el expediente en rojo(no esta fuera de plazo)
                        if (fechaLimite != null && !"".equals(fechaLimite) &&
                                (fechaFinPlazo == null || "".equals(fechaFinPlazo))) {
                            Calendar calendario = Calendar.getInstance();
                            java.util.Date date = calendario.getTime();
                            java.util.Date dateLimite = Fecha.obtenerDate(fechaLimite);
                            if (date.compareTo(dateLimite) >= 0) {
                                fueraDePlazo = "si";
                            }
                        }
                    }
                    String pend = rs.getString("PENDIENTE");
                    if (pend != null && pend.equals("A")) {
                        pendiente = "si";
                    }
                }
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(stmt);

                tvo1.setFueraDePlazo(fueraDePlazo);
                tvo1.setPendiente(pendiente);
                listaFinal.addElement(tvo1);

            }
        } catch (BDException e) {
            listaFinal = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TramitacionException("Error. RelacionExpedientesDAO.getExpedientesPendientes", e);
        } catch (SQLException e) {
            listaFinal = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TramitacionException("Error. RelacionExpedientesDAO.getExpedientesPendientes", e);

        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        m_Log.debug("getListaExpedientesPendientes en RelacionExpedientesDAO");
        return listaFinal;
    }
    
    /**
     * @return the limiteFilasMostrar
     */
    public String getLimiteExpedientesMostrar() {
        return limiteFilasMostrar;
    }
 
}