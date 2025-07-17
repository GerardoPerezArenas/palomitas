package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.Fecha;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.interfaces.user.web.sge.FichaRelacionExpedientesForm;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.commons.DateOperations;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

public class FichaRelacionExpedientesDAO {

  /* Declaracion de servicios */
  protected static Config m_ConfigTechnical; // Fichero de configuracion tecnico
  protected static Log m_Log =
          LogFactory.getLog(FichaRelacionExpedientesDAO.class.getName());


  // protected static Config m_ConfigError; // Mensajes de error localizados

  /* Instancia unica */
    private static FichaRelacionExpedientesDAO instance = null;

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

  protected static String hte_ter;
  protected static String hte_nvr;
  protected static String hte_nom;
  protected static String hte_ap1;
  protected static String hte_pa1;
  protected static String hte_ap2;
  protected static String hte_pa2;
  protected static String hte_doc;

  protected static String sql_uor_nombre;
  protected static String sql_uor_codigo;
  protected static String sql_uor_codigo_visible;

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
  protected static String sql_exp_observaciones;

  protected static String bloq_mun;
  protected static String bloq_eje;
  protected static String bloq_num;
  protected static String bloq_pro;
  protected static String bloq_tra;
  protected static String bloq_ocu;
  protected static String bloq_usu;

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

  protected static String sql_cro_mun;
  protected static String sql_cro_pro;
  protected static String sql_cro_eje;
  protected static String sql_cro_num;
  protected static String sql_cro_tra;
  protected static String sql_cro_ocu;
  protected static String sql_cro_fei;
  protected static String sql_cro_fef;
  protected static String sql_cro_fli;
  protected static String sql_cro_uor;
  protected static String sql_cro_ffp;
  protected static String sql_cro_usu;
  protected static String sql_cro_usf;

  protected static String sql_tra_mun;
  protected static String sql_tra_pro;
  protected static String sql_tra_cod;
  protected static String sql_tra_utr;

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

  protected static String pml_mun;
  protected static String pml_cod;
  protected static String pml_cmp;
  protected static String pml_leng;
  protected static String pml_valor;

  protected static String pro_mun;
  protected static String pro_cod;
  protected static String pro_tri;
  protected static String pro_utr;
  protected static String pro_loc;

  protected static String usu_cod;
  protected static String usu_nom;

  protected static String uor_cod;
  protected static String uor_nom;

    protected static String tml_mun;
    protected static String tml_pro;
    protected static String tml_tra;
    protected static String tml_cmp;
    protected static String tml_leng;
    protected static String tml_valor;

    protected static String tra_mun;
    protected static String tra_pro;
    protected static String tra_cod;
    protected static String tra_utr;
    protected static String tra_cls;
    protected static String tra_ocu;
    protected static String tra_uin;
    protected static String tra_fba;
    protected static String tra_ws_cod;
    protected static String tra_ws_ob;
    protected static String tra_ws_cod_retro;
    protected static String tra_ws_ob_retro;

    protected static String cml_cod;
    protected static String cml_cmp;
    protected static String cml_leng;
    protected static String cml_valor;

    protected static String sql_dop_codMun;
    protected static String sql_dop_codPro;
    protected static String sql_dop_codDoc;
    protected static String sql_dop_obl;
    protected static String sql_dop_tdo;

    protected static String sql_dpml_codMun;
    protected static String sql_dpml_codProc;
    protected static String sql_dpml_codDoc;
    protected static String sql_dpml_campo;
    protected static String sql_dpml_idioma;
    protected static String sql_dpml_valor;

    protected static String sql_dor_codMun;
    protected static String sql_dor_ejer;
    protected static String sql_dor_num;
    protected static String sql_dor_codProc;
    protected static String sql_dor_codDoc;
    protected static String sql_dor_fEntreg;

    protected static String enp_mun;
    protected static String enp_pro;
    protected static String enp_cod;
    protected static String enp_des;
    protected static String enp_url;
    protected static String enp_est;

    protected static String rel_trp_mun;
    protected static String rel_trp_pro;
    protected static String rel_trp_eje;
    protected static String rel_trp_num;
    protected static String rel_trp_tra;
    protected static String rel_trp_ent;
    protected static String rel_trp_ctr;
    protected static String rel_trp_fei;
    protected static String rel_trp_fef;

    protected static String crd_mun;
    protected static String crd_pro;
    protected static String crd_eje;
    protected static String crd_num;
    protected static String crd_tra;
    protected static String crd_ocu;

    protected static String pca_mun;
    protected static String pca_pro;
    protected static String pca_cod;
    protected static String pca_des;
    protected static String pca_plt;
    protected static String pca_tda;
    protected static String pca_tam;
    protected static String pca_mas;
    protected static String pca_obl;
    protected static String pca_nor;
    protected static String pca_rot;
    protected static String pca_activo;

    protected static String plt_cod;
    protected static String plt_des;
    protected static String plt_url;

    protected	static String tca_mun;
    protected	static String tca_pro;
    protected	static String tca_tra;
    protected	static String tca_cod;
    protected	static String tca_des;
    protected	static String tca_plt;
    protected	static String tca_tda;
    protected	static String tca_rot;
    protected	static String tca_vis;
    protected	static String tca_activo;

    protected static String sql_rel_cro_mun;
    protected static String sql_rel_cro_pro;
    protected static String sql_rel_cro_tra;
    protected static String sql_rel_cro_eje;
    protected static String sql_rel_cro_num;
    protected static String sql_rel_cro_usu;
    protected static String sql_rel_cro_fei;
    protected static String sql_rel_cro_fef;
    protected static String sql_rel_cro_fip;
    protected static String sql_rel_cro_ffp;
    protected static String sql_rel_cro_flim;
    protected static String sql_rel_cro_utr;
    protected static String sql_rel_cro_ocu;
    protected static String sql_rel_cro_res;
    protected static String sql_rel_cro_obs;
    protected static String sql_rel_cro_usf;

    protected static String tfe_mun;
    protected static String tfe_eje;
    protected static String tfe_num;
    protected static String tfec_mun;
    protected static String tfec_eje;
    protected static String tfec_num;
    protected static String ttl_mun;
    protected static String ttl_eje;
    protected static String ttl_num;
    protected static String tfi_mun;
    protected static String tfi_eje;
    protected static String tfi_num;
    protected static String txt_mun;
    protected static String txt_eje;
    protected static String txt_num;
    protected static String tnu_mun;
    protected static String tnu_eje;
    protected static String tnu_num;
    protected static String tnuc_mun;
    protected static String tnuc_eje;
    protected static String tnuc_num;

    protected static String sql_doe_codMun;
    protected static String sql_doe_ejer;
    protected static String sql_doe_num;
    protected static String sql_doe_codProc;
    protected static String sql_doe_codDoc;
    protected static String sql_doe_fEntreg;

    protected static String tde_mun;
    protected static String tde_eje;
    protected static String tde_num;

  protected FichaRelacionExpedientesDAO() {
    super();
    // Fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Servicio de log



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

    hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
    hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");
    hte_nom = m_ConfigTechnical.getString("SQL.T_HTE.nombre");
    hte_ap1 = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
    hte_pa1 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido1");
    hte_ap2 = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
    hte_pa2 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido2");
    hte_doc = m_ConfigTechnical.getString("SQL.T_HTE.documento");

    sql_uou_uor= m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
    sql_uou_usu= m_ConfigTechnical.getString("SQL.A_UOU.usuario");
    sql_uou_org= m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
    sql_uou_ent= m_ConfigTechnical.getString("SQL.A_UOU.entidad");

    sql_uor_nombre = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
    sql_uor_codigo = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
    sql_uor_codigo_visible = m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible");

    sql_exp_codMun= m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
    sql_exp_codProc= m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
    sql_exp_ejer= m_ConfigTechnical.getString("SQL.E_EXP.ano");
    sql_exp_num= m_ConfigTechnical.getString("SQL.E_EXP.numero");
    sql_exp_fechIni= m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio");
    sql_exp_uor=  m_ConfigTechnical.getString("SQL.E_EXP.uor");
    sql_exp_estado= m_ConfigTechnical.getString("SQL.E_EXP.estado");
    sql_exp_asunto= m_ConfigTechnical.getString("SQL.E_EXP.asunto");
    sql_exp_tra= m_ConfigTechnical.getString("SQL.E_EXP.codTramiteUltCerrado");
    sql_exp_observaciones= m_ConfigTechnical.getString("SQL.E_EXP.observaciones");

    bloq_mun= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codMunicipio");
    bloq_eje= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ano");
    bloq_num= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.numero");
    bloq_pro= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codProcedimiento");
    bloq_tra= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.tramite");
    bloq_ocu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ocurrencia");
    bloq_usu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.usuario");

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

    sql_cro_mun = m_ConfigTechnical.getString("SQL.E_CRO.codMunicipio");
    sql_cro_pro = m_ConfigTechnical.getString("SQL.E_CRO.codProcedimiento");
    sql_cro_eje = m_ConfigTechnical.getString("SQL.E_CRO.ano");
    sql_cro_num = m_ConfigTechnical.getString("SQL.E_CRO.numero");
    sql_cro_tra = m_ConfigTechnical.getString("SQL.E_CRO.codTramite");
    sql_cro_ocu = m_ConfigTechnical.getString("SQL.E_CRO.ocurrencia");
    sql_cro_fei = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicio");
    sql_cro_fef = m_ConfigTechnical.getString("SQL.E_CRO.fechaFin");
    sql_cro_fli = m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite");
    sql_cro_uor = m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora");
    sql_cro_ffp = m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo");
    sql_cro_usu = m_ConfigTechnical.getString("SQL.E_CRO.codUsuario");
    sql_cro_usf = m_ConfigTechnical.getString("SQL.E_CRO.usuarioFinalizacion");

    sql_tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
    sql_tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
    sql_tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
    sql_tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");

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

    pml_mun = m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");
    pml_cod = m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
    pml_cmp = m_ConfigTechnical.getString("SQL.E_PML.codCampoML");
    pml_leng = m_ConfigTechnical.getString("SQL.E_PML.idioma");
    pml_valor = m_ConfigTechnical.getString("SQL.E_PML.valor");

    pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");
    pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
    pro_tri = m_ConfigTechnical.getString("SQL.E_PRO.codTramiteInicio");
    pro_utr = m_ConfigTechnical.getString("SQL.E_PRO.unidad");
    pro_loc = m_ConfigTechnical.getString("SQL.E_PRO.poseeLocalizacion");

    usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
    usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");

    uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
    uor_nom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");

      tml_mun = m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
      tml_pro = m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
      tml_tra = m_ConfigTechnical.getString("SQL.E_TML.codTramite");
      tml_cmp = m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
      tml_leng = m_ConfigTechnical.getString("SQL.E_TML.idioma");
      tml_valor = m_ConfigTechnical.getString("SQL.E_TML.valor");

      tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
      tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
      tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
      tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");
      tra_cls = m_ConfigTechnical.getString("SQL.E_TRA.clasificacion");
      tra_ocu = m_ConfigTechnical.getString("SQL.E_TRA.ocurrencias");
      tra_uin = m_ConfigTechnical.getString("SQL.E_TRA.unidadInicio");
      tra_fba = m_ConfigTechnical.getString("SQL.E_TRA.fechaBaja");
      tra_ws_cod = m_ConfigTechnical.getString("SQL.E_TRA.wsTramitar");
      tra_ws_ob = m_ConfigTechnical.getString("SQL.E_TRA.wsTramitarTipo");
      tra_ws_cod_retro = m_ConfigTechnical.getString("SQL.E_TRA.wsRetroceder");
      tra_ws_ob_retro = m_ConfigTechnical.getString("SQL.E_TRA.wsRetrocederTipo");

      cml_cod = m_ConfigTechnical.getString("SQL.A_CML.codTramite");
      cml_cmp = m_ConfigTechnical.getString("SQL.A_CML.codCampoML");
      cml_leng = m_ConfigTechnical.getString("SQL.A_CML.idioma");
      cml_valor = m_ConfigTechnical.getString("SQL.A_CML.valor");

      sql_dop_codMun= m_ConfigTechnical.getString("SQL.E_DOP.codMunicipio");
      sql_dop_codPro= m_ConfigTechnical.getString("SQL.E_DOP.codProcedimiento");
      sql_dop_codDoc= m_ConfigTechnical.getString("SQL.E_DOP.codDocumento");
      sql_dop_obl= m_ConfigTechnical.getString("SQL.E_DOP.obligatorio");
      sql_dop_tdo= m_ConfigTechnical.getString("SQL.E_DOP.tipoDocumento");

      sql_dpml_codMun= m_ConfigTechnical.getString("SQL.E_DPML.codMunicipio");
      sql_dpml_codProc= m_ConfigTechnical.getString("SQL.E_DPML.codProcedimiento");
      sql_dpml_codDoc= m_ConfigTechnical.getString("SQL.E_DPML.codDocumento");
      sql_dpml_campo= m_ConfigTechnical.getString("SQL.E_DPML.codCampoML");
      sql_dpml_idioma= m_ConfigTechnical.getString("SQL.E_DPML.idioma");
      sql_dpml_valor= m_ConfigTechnical.getString("SQL.E_DPML.valor");

      enp_mun = m_ConfigTechnical.getString("SQL.E_ENP.codMunicipio");
      enp_pro = m_ConfigTechnical.getString("SQL.E_ENP.codProcedimiento");
      enp_cod = m_ConfigTechnical.getString("SQL.E_ENP.codEnlace");
      enp_des = m_ConfigTechnical.getString("SQL.E_ENP.descripcion");
      enp_url = m_ConfigTechnical.getString("SQL.E_ENP.url");
      enp_est = m_ConfigTechnical.getString("SQL.E_ENP.estado");

      rel_trp_mun = m_ConfigTechnical.getString("SQL.E_TRP.codMunicipio");
      rel_trp_pro = m_ConfigTechnical.getString("SQL.E_TRP.codProcedimiento");
      rel_trp_eje = m_ConfigTechnical.getString("SQL.E_TRP.ejercicio");
      rel_trp_num = m_ConfigTechnical.getString("SQL.E_TRP.numeroExpediente");
      rel_trp_tra = m_ConfigTechnical.getString("SQL.E_TRP.codTramite");
      rel_trp_ent = m_ConfigTechnical.getString("SQL.E_TRP.codCondicionEntrada");
      rel_trp_ctr = m_ConfigTechnical.getString("SQL.E_TRP.codTramiteCondicionEntrada");

      pca_mun = m_ConfigTechnical.getString("SQL.E_PCA.codMunicipio");
      pca_pro = m_ConfigTechnical.getString("SQL.E_PCA.codProcedimiento");
      pca_cod = m_ConfigTechnical.getString("SQL.E_PCA.codCampo");
      pca_des = m_ConfigTechnical.getString("SQL.E_PCA.descripcion");
      pca_plt = m_ConfigTechnical.getString("SQL.E_PCA.codPlantilla");
      pca_tda = m_ConfigTechnical.getString("SQL.E_PCA.codTipoDato");
      pca_tam = m_ConfigTechnical.getString("SQL.E_PCA.tamano");
      pca_mas = m_ConfigTechnical.getString("SQL.E_PCA.mascara");
      pca_obl = m_ConfigTechnical.getString("SQL.E_PCA.obligatorio");
      pca_nor = m_ConfigTechnical.getString("SQL.E_PCA.numeroOrden");
      pca_rot = m_ConfigTechnical.getString("SQL.E_PCA.rotulo");
      pca_activo = m_ConfigTechnical.getString("SQL.E_PCA.activo");

      plt_cod = m_ConfigTechnical.getString("SQL.E_PLT.codPlantilla");
      plt_des = m_ConfigTechnical.getString("SQL.E_PLT.descripcion");
      plt_url = m_ConfigTechnical.getString("SQL.E_PLT.url");

      tca_mun	= m_ConfigTechnical.getString("SQL.E_TCA.codMunicipio");
      tca_pro	= m_ConfigTechnical.getString("SQL.E_TCA.codProcedimiento");
      tca_tra	= m_ConfigTechnical.getString("SQL.E_TCA.codTramite");
      tca_cod	= m_ConfigTechnical.getString("SQL.E_TCA.codCampo");
      tca_des	= m_ConfigTechnical.getString("SQL.E_TCA.descripcion");
      tca_plt	= m_ConfigTechnical.getString("SQL.E_TCA.codPlantilla");
      tca_tda	= m_ConfigTechnical.getString("SQL.E_TCA.codTipoDato");
      tca_rot	= m_ConfigTechnical.getString("SQL.E_TCA.rotulo");
      tca_vis	= m_ConfigTechnical.getString("SQL.E_TCA.visible");
      tca_activo	= m_ConfigTechnical.getString("SQL.E_TCA.activo");

      sql_rel_cro_mun = m_ConfigTechnical.getString("SQL.G_CRO.codMunicipio");
      sql_rel_cro_pro = m_ConfigTechnical.getString("SQL.G_CRO.codProcedimiento");
      sql_rel_cro_tra = m_ConfigTechnical.getString("SQL.G_CRO.codTramite");
      sql_rel_cro_eje = m_ConfigTechnical.getString("SQL.G_CRO.ano");
      sql_rel_cro_num = m_ConfigTechnical.getString("SQL.G_CRO.numero");
      sql_rel_cro_usu = m_ConfigTechnical.getString("SQL.G_CRO.codUsuario");
      sql_rel_cro_fei = m_ConfigTechnical.getString("SQL.G_CRO.fechaInicio");
      sql_rel_cro_fef = m_ConfigTechnical.getString("SQL.G_CRO.fechaFin");
      sql_rel_cro_utr = m_ConfigTechnical.getString("SQL.G_CRO.codUnidadTramitadora");
      sql_rel_cro_ocu = m_ConfigTechnical.getString("SQL.G_CRO.ocurrencia");
      sql_rel_cro_fip = m_ConfigTechnical.getString("SQL.G_CRO.fechaInicioPlazo");
      sql_rel_cro_ffp = m_ConfigTechnical.getString("SQL.G_CRO.fechaFinPlazo");
      sql_rel_cro_flim = m_ConfigTechnical.getString("SQL.G_CRO.fechaLimite");
      sql_rel_cro_res = m_ConfigTechnical.getString("SQL.G_CRO.resolucion");
      sql_rel_cro_obs = m_ConfigTechnical.getString("SQL.G_CRO.observaciones");
      sql_rel_cro_usf = m_ConfigTechnical.getString("SQL.G_CRO.usuarioFinalizacion");

      tnu_mun = m_ConfigTechnical.getString("SQL.E_TNU.codMunicipio");
      tnu_eje = m_ConfigTechnical.getString("SQL.E_TNU.ejercicio");
      tnu_num = m_ConfigTechnical.getString("SQL.E_TNU.numeroExpediente");
      tnuc_mun = m_ConfigTechnical.getString("SQL.E_TNUC.codMunicipio");
      tnuc_eje = m_ConfigTechnical.getString("SQL.E_TNUC.ejercicio");
      tnuc_num = m_ConfigTechnical.getString("SQL.E_TNUC.numeroExpediente");
      tfe_mun = m_ConfigTechnical.getString("SQL.E_TFE.codMunicipio");
      tfe_eje = m_ConfigTechnical.getString("SQL.E_TFE.ejercicio");
      tfe_num = m_ConfigTechnical.getString("SQL.E_TFE.numeroExpediente");
      tfec_mun = m_ConfigTechnical.getString("SQL.E_TFEC.codMunicipio");
      tfec_eje = m_ConfigTechnical.getString("SQL.E_TFEC.ejercicio");
      tfec_num = m_ConfigTechnical.getString("SQL.E_TFEC.numeroExpediente");
      txt_mun = m_ConfigTechnical.getString("SQL.E_TXT.codMunicipio");
      txt_eje = m_ConfigTechnical.getString("SQL.E_TXT.ejercicio");
      txt_num = m_ConfigTechnical.getString("SQL.E_TXT.numeroExpediente");
      tfi_mun = m_ConfigTechnical.getString("SQL.E_TFI.codMunicipio");
      tfi_eje = m_ConfigTechnical.getString("SQL.E_TFI.ejercicio");
      tfi_num = m_ConfigTechnical.getString("SQL.E_TFI.numeroExpediente");
      ttl_mun = m_ConfigTechnical.getString("SQL.E_TTL.codMunicipio");
      ttl_eje = m_ConfigTechnical.getString("SQL.E_TTL.ejercicio");
      ttl_num = m_ConfigTechnical.getString("SQL.E_TTL.numeroExpediente");

      sql_doe_codMun = m_ConfigTechnical.getString("SQL.E_DOE.codMunicipio");
      sql_doe_ejer = m_ConfigTechnical.getString("SQL.E_DOE.ejercicio");
      sql_doe_num = m_ConfigTechnical.getString("SQL.E_DOE.numero");
      sql_doe_codProc = m_ConfigTechnical.getString("SQL.E_DOE.codProcedimiento");
      sql_doe_codDoc = m_ConfigTechnical.getString("SQL.E_DOE.codDocumento");
      sql_doe_fEntreg = m_ConfigTechnical.getString("SQL.E_DOE.fechaEntrega");

      tde_mun = m_ConfigTechnical.getString("SQL.E_TDE.codMunicipio");
      tde_eje = m_ConfigTechnical.getString("SQL.E_TDE.ejercicio");
      tde_num = m_ConfigTechnical.getString("SQL.E_TDE.numeroExpediente");
  }


  public static FichaRelacionExpedientesDAO getInstance() {
    synchronized(RelacionExpedientesDAO.class) {
        if (instance == null)
          instance = new FichaRelacionExpedientesDAO();
      }
    return instance;
  }

    public Vector filtrarDatosSuplementariosRelacion(Vector estructuraDatosSuplementarios,
            Vector valoresDatosSuplementarios, String numeroRelacion,String[] params){

        /*Se recorre la estructura de datos consultando el trámite al que pertenecen. Si no pertenece a ninguno o
         pertenece a un trámite que no figura en la relación, se pone su valor a cero (trámites finalizados antes
         de crear la relación).

         Los campos del trámite en el que se inicia la relación dben comportarse como aquellos que figuran a nivel
         de expediente. Sabemos que pertenecen al trámite de inicio si ese codigo de trámite figura en la relación,
         y en dicha relación no existen más trámites.*/

        EstructuraCampo ec = null;

        for (int i=0;i<estructuraDatosSuplementarios.size();i++){
            ec = (EstructuraCampo)(estructuraDatosSuplementarios.elementAt(i));
            CamposFormulario aux = (CamposFormulario)valoresDatosSuplementarios.elementAt(i);
            HashMap mapa = aux.getCampos();            
            if (ec.getCodTramite()==null) {
                mapa.put(ec.getCodCampo(), "");
            }
            else{
                try {
                /*Si el trámite está abierto y es el primero no debe mostrarse el dato, en caso contrario si*/
                boolean inicio = datoEnTramiteinicioRelacion(ec.getCodTramite(),numeroRelacion,params);
                if(inicio) mapa.put(ec.getCodCampo(), "");
                } catch (TechnicalException ex) {
                    if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
                    ex.printStackTrace();
                }
            }
            m_Log.debug (ec.getCodTramite() + "-" + ec.getCodCampo() + "-" + mapa.get(ec.getCodCampo()));
            aux.set("campos", mapa);
            valoresDatosSuplementarios.set(i, aux);

        }

        return valoresDatosSuplementarios;        
    }

    
    
     public Vector filtrarDatosSuplementariosRelacion(Vector estructuraDatosSuplementarios,
            Vector valoresDatosSuplementarios, String numeroRelacion,Connection con){

        /*Se recorre la estructura de datos consultando el trámite al que pertenecen. Si no pertenece a ninguno o
         pertenece a un trámite que no figura en la relación, se pone su valor a cero (trámites finalizados antes
         de crear la relación).

         Los campos del trámite en el que se inicia la relación dben comportarse como aquellos que figuran a nivel
         de expediente. Sabemos que pertenecen al trámite de inicio si ese codigo de trámite figura en la relación,
         y en dicha relación no existen más trámites.*/

        EstructuraCampo ec = null;

        for (int i=0;i<estructuraDatosSuplementarios.size();i++){
            ec = (EstructuraCampo)(estructuraDatosSuplementarios.elementAt(i));
            CamposFormulario aux = (CamposFormulario)valoresDatosSuplementarios.elementAt(i);
            HashMap mapa = aux.getCampos();            
            if (ec.getCodTramite()==null) {
                mapa.put(ec.getCodCampo(), "");
            }
            else{
                try {
                      /*Si el trámite está abierto y es el primero no debe mostrarse el dato, en caso contrario si*/
                      boolean inicio = datoEnTramiteinicioRelacion(ec.getCodTramite(),numeroRelacion,con);
                      if(inicio) mapa.put(ec.getCodCampo(), "");
                        
                } catch (Exception ex) {
                    if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
                    ex.printStackTrace();
                }
            }
            m_Log.debug (ec.getCodTramite() + "-" + ec.getCodCampo() + "-" + mapa.get(ec.getCodCampo()));
            aux.set("campos", mapa);
            valoresDatosSuplementarios.set(i, aux);

        }

        return valoresDatosSuplementarios;        
    }

    
    
    
    
    public boolean datoEnTramiteinicioRelacion (String codTram, String numRel, String[] params) throws TechnicalException{

        m_Log.debug ("************ESTA EN TRAMITE DE INICIO DICHO DATO?");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        boolean inicio = false;

        try{
         oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            sql = "SELECT COUNT(*) AS CONTADOR FROM G_CRO WHERE CRO_NUM='" + numRel + "'";
             st = con.createStatement();
            rs = st.executeQuery(sql);
            m_Log.debug("sql="+sql);
            if (rs.next()){
                String contador = rs.getString("CONTADOR");
                if (contador.equals("1")) inicio=true;
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
       
        } catch (SQLException ex) {
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            ex.printStackTrace();
        } catch (BDException ex) {
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }        
        return inicio;
    }
    
    
    
    public boolean datoEnTramiteinicioRelacion (String codTram, String numRel,Connection con) throws TechnicalException{

        m_Log.debug ("************ESTA EN TRAMITE DE INICIO DICHO DATO?");        
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        boolean inicio = false;

        try{
         
            sql = "SELECT COUNT(*) AS CONTADOR FROM G_CRO WHERE CRO_NUM='" + numRel + "'";
             st = con.createStatement();
            rs = st.executeQuery(sql);
            m_Log.debug("sql="+sql);
            if (rs.next()){
                String contador = rs.getString("CONTADOR");
                if (contador.equals("1")) inicio=true;
            }
         
        } catch (SQLException ex) {
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);         
        }        
        return inicio;
    }
    
    
    


    public Vector filtrarEstructuraDatosRelaciones(Vector estructuraDatosSuplementarios,String numRel, String[] params){

        m_Log.debug ("************LIMPIEZA DE ESTRUCTURA DE CAMPOS SUPLEMENTARIOS");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        EstructuraCampo ec = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                ec = (EstructuraCampo) (estructuraDatosSuplementarios.elementAt(i));

                if (ec.getCodTramite() != null) {
                    sql = "SELECT CRO_TRA FROM G_CRO WHERE CRO_NUM='" + numRel + "' AND CRO_TRA=" + ec.getCodTramite();
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    m_Log.debug("sql=" + sql);

                    if (!rs.next()) {
                        estructuraDatosSuplementarios.remove(i);
                    }
                }

               
                
            }
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.devolverConexion(oad, con);
                
        } catch (TechnicalException ex) {
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BDException ex) {
            if (m_Log.isErrorEnabled()) {
                m_Log.error(ex.getMessage());
            }
            ex.printStackTrace();
        } finally{
            try {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            } catch (TechnicalException ex) {
                Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return estructuraDatosSuplementarios;
    }
    
    
    
    public Vector filtrarEstructuraDatosRelaciones(Vector estructuraDatosSuplementarios,String numRel, Connection con){

        m_Log.debug ("************LIMPIEZA DE ESTRUCTURA DE CAMPOS SUPLEMENTARIOS");        
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        EstructuraCampo ec = null;

        try {
            
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                ec = (EstructuraCampo) (estructuraDatosSuplementarios.elementAt(i));

                if (ec.getCodTramite() != null) {
                    sql = "SELECT CRO_TRA FROM G_CRO WHERE CRO_NUM='" + numRel + "' AND CRO_TRA=" + ec.getCodTramite();
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    m_Log.debug("sql=" + sql);

                    if (!rs.next()) {
                        estructuraDatosSuplementarios.remove(i);
                    }
                }
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally{
            try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
            
            } catch (TechnicalException ex) {
                Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return estructuraDatosSuplementarios;
    }
    
    
    
    

    public int insertRelacionExpediente(FichaRelacionExpedientesForm reForm, String utrTramite, String[] params) throws TechnicalException {
        
        int res = -1;
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try {
            
                oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();
                oad.inicioTransaccion(con) ; // Inicio transacción

                String ano = "";
                String num = "";

                // COGER NUMERO DE EJERCICIO
                sql = "SELECT " + oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "YYYY")
                + " AS ejercicio FROM "+GlobalNames.ESQUEMA_GENERICO+"A_ORG WHERE 1=1";
                if(m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: getNuevoNumeroExpediente--> "+ sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                if(rs.next()){
                    ano = (String) rs.getString("ejercicio");
                } else {
                    ano="";
                }
                
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);                    
                
                // CONTROLAR NUMERO DE LA SIGUIENTE RELACIÓN DE EXPEDIENTES
                if(m_Log.isDebugEnabled()) m_Log.debug("INICIO: "+reForm.getCodMunicipio());
                int ideMax = 0;
                sql = "SELECT " + oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{sql_nex_num})+ " as num"
                + " FROM G_NEX "
                + " WHERE " + sql_nex_codMun + "=" + Integer.parseInt(reForm.getCodMunicipio())
                + " AND " + sql_nex_ejer +"="+oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "YYYY")
                + " AND " + sql_nex_codProc + "='"+ reForm.getCodProcedimiento()+"' ";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                if (rs.next()){
                    ideMax = (int) rs.getInt("num");
                }
                
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);            

                num = String.valueOf(ideMax+1);
                if(ideMax == 0){
                    st = con.createStatement();
                    sql = "insert into g_nex (" + sql_nex_codMun + ", " + sql_nex_codProc + ", " + sql_nex_ejer + ", " + sql_nex_num + ") values (" + Integer.parseInt(reForm.getCodMunicipio()) + ", '" + reForm.getCodProcedimiento() + "', " + ano + ", " + num + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    res = st.executeUpdate(sql);
                    SigpGeneralOperations.closeStatement(st);            
                }else{
                    st = con.createStatement();
                    sql = "update g_nex set " + sql_nex_num + " = " + num + " where " + sql_nex_codMun + " = " + Integer.parseInt(reForm.getCodMunicipio()) + " and " + sql_nex_codProc + " = '" + reForm.getCodProcedimiento() + "' and " + sql_nex_ejer + " = " + ano;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    res = st.executeUpdate(sql);
                     m_Log.debug("ejecutamos la consulta");
                    SigpGeneralOperations.closeStatement(st);
                    m_Log.debug("cerramos la conexion");
                }

                //INSERTAR NUEVA RELACIÓN
                if ( (num.length()>0) && (num.length()<6) ) num= "000000".substring(0,6-num.length()) +num;
                num = ano+"/"+reForm.getCodProcedimiento()+"/"+num;
                reForm.setNumeroRelacion(num);
                reForm.setEjercicio(ano);
                m_Log.debug("numero Relacion" + reForm.getNumeroRelacion());
                m_Log.debug("numero ejercicio "+ reForm.getEjercicio());
                m_Log.debug("1 "+ Integer.parseInt(reForm.getCodMunicipio()));
                m_Log.debug(" 2 "+ reForm.getCodProcedimiento());
                m_Log.debug(" 3 "+ ano);
                m_Log.debug(" 4 "+ num);
                m_Log.debug(" 5 "+ oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null));
                m_Log.debug(" 6 "+ Integer.parseInt(reForm.getUsuarioIni()));
                m_Log.debug(" 7 "+ Integer.parseInt(reForm.getTramiteIni()));
                 m_Log.debug(" 8 "+ reForm.getCodUORIni());
                
                sql = "insert into g_rel (" + sql_rel_codMun + ", " + sql_rel_codProc + ", " + sql_rel_ejer + ", ";
                sql += sql_rel_num + ", " + sql_rel_estado + ", " + sql_rel_fecIni + ", " + sql_rel_fecFin + ", ";
                sql += sql_rel_usuIni + ", " + sql_rel_usuFin + ", " + sql_rel_traIni + ", " + sql_rel_traAct + ", " + sql_rel_uorIni + ") values (";
                sql += Integer.parseInt(reForm.getCodMunicipio()) + ", '" + reForm.getCodProcedimiento() + "', " + ano + ", '";
                sql += num + "', 0, " + oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null) + ", null, " + Integer.parseInt(reForm.getUsuarioIni()) + ",null , ";
                sql += Integer.parseInt(reForm.getTramiteIni()) + ", " + Integer.parseInt(reForm.getTramiteIni()) + ", ";
                sql += Integer.parseInt(reForm.getCodUORIni()) + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = con.createStatement();
                res = st.executeUpdate(sql);
                SigpGeneralOperations.closeStatement(st);            

                String bloqueo = reForm.getBloqueo();
                m_Log.debug("BLOQUEO : " + bloqueo);
                // EXPEDIENTES DE LA RELACIÓN
                Vector listaMunExp = reForm.getListaMunExp();
                Vector listaProExp = reForm.getListaProExp();
                Vector listaEjeExp = reForm.getListaEjeExp();
                Vector listaNumExp = reForm.getListaNumExp();
                if(m_Log.isDebugEnabled()) m_Log.debug("Expedientes : "+listaMunExp.size());
                for(int m=0;m<listaMunExp.size();m++) {
                  if (bloqueo.equals("1") || bloqueo.equals("0")) {
                      sql = "DELETE FROM E_EXP_BLOQ WHERE " + bloq_mun + " = " + listaMunExp.elementAt(m) + " AND " +
                              bloq_pro + " ='" + listaProExp.elementAt(m) + "' AND " +
                              bloq_eje + " =" + listaEjeExp.elementAt(m) + " AND " +
                              bloq_num + " ='" + listaNumExp.elementAt(m) + "'";
                      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                      st = con.createStatement();
                      res = st.executeUpdate(sql);
                      SigpGeneralOperations.closeStatement(st);
                  }
                  if (bloqueo.equals("1")) {
                      sql = "INSERT INTO E_EXP_BLOQ (" + bloq_mun + "," + bloq_pro + "," + bloq_eje + ",";
                      sql += bloq_num + "," + bloq_tra + "," + bloq_ocu + "," + bloq_usu + ") VALUES (";
                      sql += listaMunExp.elementAt(m) + ",'" + listaProExp.elementAt(m) + "',";
                      sql += listaEjeExp.elementAt(m) + ",'" +listaNumExp.elementAt(m) + "',";
                      sql += reForm.getTramiteIni() + ",1," + reForm.getUsuarioIni()+")";
                      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                      st = con.createStatement();
                      res = st.executeUpdate(sql);
                      SigpGeneralOperations.closeStatement(st);
                  }

                  sql = "INSERT INTO G_EXP (" + sql_rel_exp_codMun + "," + sql_rel_exp_codProc + ",";
                  sql += sql_rel_exp_ejer + "," + sql_rel_exp_num + "," + sql_rel_exp_codMunR + "," + sql_rel_exp_codProcR + ",";
                  sql += sql_rel_exp_ejerR + "," + sql_rel_exp_numR + ") VALUES (";
                  sql += reForm.getCodMunicipio() + ",'" + reForm.getCodProcedimiento() + "'," + ano + ",'" + num + "'," ;
                  sql += listaMunExp.elementAt(m) + ",'" + listaProExp.elementAt(m) + "',";
                  sql += listaEjeExp.elementAt(m) + ",'" +listaNumExp.elementAt(m) + "')";
                  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                  st = con.createStatement();
                  res = st.executeUpdate(sql);
                  SigpGeneralOperations.closeStatement(st);
                  
                  GeneralValueObject aux = new GeneralValueObject();
                  aux.setAtributo("codProcedimiento", (String) listaProExp.elementAt(m));
                  aux.setAtributo("codMunicipio", (String) listaMunExp.elementAt(m));
                  aux.setAtributo("ejercicio", (String) listaEjeExp.elementAt(m));
                  aux.setAtributo("numero", (String) listaNumExp.elementAt(m));
                  
                }

               //Trámite inicial de la relación
                sql = "INSERT INTO G_CRO  ("
                    + sql_rel_cro_mun +", "+ sql_rel_cro_pro +","+ sql_rel_cro_eje+","+ sql_rel_cro_num+","+ sql_rel_cro_tra+","
                    + sql_rel_cro_ocu+","+sql_rel_cro_fei+","+sql_rel_cro_fef+","+sql_rel_cro_usu+","+sql_rel_cro_utr
                    + ") VALUES ("
                    + reForm.getCodMunicipio() + ",'"+ reForm.getCodProcedimiento() + "'," + ano + ",'" + num +"'"
                    + "," + reForm.getTramiteIni() + ", 1,"+oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null)
                    + ", null" + ", " + reForm.getUsuarioIni() + "," + utrTramite +")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = con.createStatement();
                res = st.executeUpdate(sql);
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.commit(oad, con);
                SigpGeneralOperations.devolverConexion(oad, con);
                
        } catch (BDException ex) {
            Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {  
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            return res;
        }
            

       
    }

    public int deshacerRelacionExpedientes(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql;
        int res = -1;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con); 

            // Lo primero es controlar si hay algún trámite bloqueado por un usuario q
            // no sea el que está logueado
            boolean ok = true;
            //Expedientes y tramites de la relacion
            Vector expedientes = cargaListaExpedientes(gVO, params);
            Vector tramites = cargaListaTramites(gVO, params);
            String cad_exped = "";
            for (Object objExpediente : expedientes) {
                GeneralValueObject exped = (GeneralValueObject) objExpediente;
                cad_exped = cad_exped + ", '" + exped.getAtributo("numExp") + "'";
            }
            if (cad_exped.length() > 0) {
                cad_exped = cad_exped.substring(1, cad_exped.length());
                for (Object objTramite : tramites) {
                    GeneralValueObject tramite = (GeneralValueObject) objTramite;

                    st = con.createStatement();
                    sql = "SELECT * FROM E_EXP_BLOQ WHERE " + bloq_mun + " = " + gVO.getAtributo("codMunicipio") + " and " +
                            bloq_pro + " = '" + gVO.getAtributo("codProcedimiento") + "' and " +
                            bloq_num + " in (" + cad_exped + ") and " +
                            bloq_tra + " = " + tramite.getAtributo("codTramite") + " and " +
                            bloq_ocu + " = " + tramite.getAtributo("ocurrenciaTramite") + " and " +
                            bloq_usu + " <> " + gVO.getAtributo("usuario");
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);
                    if (rs.next()) {
                        ok = false;
                        break;
                    }
                    SigpGeneralOperations.closeResultSet(rs);
                    SigpGeneralOperations.closeStatement(st);
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("OK : " + ok);
            }


            if (ok) {
                st = con.createStatement();
                //DESHACER LA RELACIÓN
                sql = "UPDATE G_REL SET " + sql_rel_estado + "=1, " + sql_rel_fecFin + "=" + oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", " + sql_rel_usuFin + "=" +
                        gVO.getAtributo("usuario") + "," + sql_rel_uorFin + "=" + gVO.getAtributo("codUOR") +
                        " WHERE " + sql_rel_num + "='" + gVO.getAtributo("numero") + "' AND " +
                        sql_rel_codProc + "='" + gVO.getAtributo("codProcedimiento") + "' AND " +
                        sql_rel_codMun + "=" + gVO.getAtributo("codMunicipio") + " AND " +
                        sql_rel_ejer + "=" + gVO.getAtributo("ejercicio");
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                res = st.executeUpdate(sql);
                SigpGeneralOperations.closeStatement(st);

                //Bloqueos
                if (m_Log.isDebugEnabled()) m_Log.debug("Bloqueos : " + gVO.getAtributo("bloqueos"));
                if (gVO.getAtributo("bloqueos").equals("0")) {
                    for (Object objExpediente : expedientes) {
                        GeneralValueObject exped = (GeneralValueObject) objExpediente;
                        if (m_Log.isDebugEnabled()) m_Log.debug("EXPEDIENTE : " + exped.getAtributo("numExp"));
                        for (Object objTramite : tramites) {
                            GeneralValueObject tramite = (GeneralValueObject) objTramite;
                            if (m_Log.isDebugEnabled()) m_Log.debug("TRAMITE : " + tramite);
                            st = con.createStatement();
                            sql = "DELETE FROM E_EXP_BLOQ WHERE " + bloq_mun + " = " + gVO.getAtributo("codMunicipio") + " and " +
                                    bloq_eje + " = " + exped.getAtributo("ejeExp") + " and " +
                                    bloq_pro + " = '" + gVO.getAtributo("codProcedimiento") + "' and " +
                                    bloq_num + " = '" + exped.getAtributo("numExp") + "' and " +
                                    bloq_tra + " = " + tramite.getAtributo("codTramite") + " and " +
                                    bloq_ocu + " = " + tramite.getAtributo("ocurrenciaTramite");
                            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                            res = st.executeUpdate(sql);
                            SigpGeneralOperations.closeStatement(st);
                        }
                    }
                }
            } else {               
                res = 999;
            }
            
            SigpGeneralOperations.commit(oad, con);
            SigpGeneralOperations.devolverConexion(oad, con);

        } catch (SQLException ex) {
            SigpGeneralOperations.rollBack(oad, con);
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        } catch (BDException e) {
            SigpGeneralOperations.rollBack(oad, con);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        } finally {           
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return res;
    }

     private String getNombreUorByCodigo(String codigoUor,Connection con,String jndi) throws TechnicalException{
        String descripcion = "";
        
        UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(jndi,codigoUor);

        if (uorDTO!=null) { 
            descripcion = uorDTO.getUor_nom();
        } 

        return descripcion;
    }
    
    
    
    
    
    public GeneralValueObject cargaRelacionExpedientes(GeneralValueObject gVO, String[] params) throws TechnicalException {
      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      ResultSet rs = null;

      try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      st = con.createStatement();

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");

      String sql = "SELECT " + pml_valor +
      " FROM E_PML" +
      " WHERE " + pml_mun + " = " + codMunicipio + " AND " + pml_cod + " = '" + codProcedimiento + "' AND " + pml_cmp + " = 'NOM' AND " + pml_leng + " = "+idiomaDefecto;

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = st.executeQuery(sql);
      while(rs.next()){
        gVO.setAtributo("nombreProcedimiento",rs.getString(pml_valor));
      }
      
      SigpGeneralOperations.closeResultSet(rs);
      SigpGeneralOperations.closeStatement(st);
      
           sql = "SELECT " + usu_nom + "," + sql_rel_uorIni + "," +
                sql_rel_fecIni + " AS fInicio " + "," +
                sql_rel_fecFin + " AS fFin " + "," + sql_rel_obs +
                "," + sql_rel_asunto +" FROM G_REL,"+GlobalNames.ESQUEMA_GENERICO+"A_USU" +
                " WHERE " + sql_rel_codMun + " = " + codMunicipio + " AND " + sql_rel_ejer + " = " + ejercicio + " AND " +
                sql_rel_num + "='" + numero + "' AND " + sql_rel_usuIni + " = " + usu_cod;      

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      st = con.createStatement();
      rs = st.executeQuery(sql);      
      
      while(rs.next()){        
        Timestamp timeFecInicio = rs.getTimestamp("fInicio");
        Timestamp timeFecFin    = rs.getTimestamp("fFin");
        if(timeFecInicio!=null)
            gVO.setAtributo("fechaInicioRelacion",DateOperations.timeStampToString(timeFecInicio));
        else
            gVO.setAtributo("fechaInicioRelacion","");

        if(timeFecFin!=null)
            gVO.setAtributo("fechaFinRelacion",DateOperations.timeStampToString(timeFecFin));
        else
            gVO.setAtributo("fechaFinRelacion","");

        gVO.setAtributo("usuarioRelacion",rs.getString(usu_nom));
        String uorInicio = rs.getString(sql_rel_uorIni);
        gVO.setAtributo("codUnidadOrganicaExp",uorInicio);
        gVO.setAtributo("descUnidadOrganicaExp",getNombreUorByCodigo(uorInicio,con,params[6]));
        String observ = rs.getString(sql_rel_obs);
        if (observ != null)
          gVO.setAtributo("observaciones",oad.js_escape(observ));
        else gVO.setAtributo("observaciones",observ);
        String asunto = rs.getString(sql_rel_asunto);
        gVO.setAtributo("asuntoUnescape", asunto);  
        if (asunto != null)
          gVO.setAtributo("asunto",oad.js_escape(asunto));
        else gVO.setAtributo("asunto",asunto);
      }
      
      SigpGeneralOperations.closeResultSet(rs);
      SigpGeneralOperations.closeStatement(st);
      SigpGeneralOperations.devolverConexion(oad, con);
      
    }catch (SQLException e){
          m_Log.error(e.getMessage());
          e.printStackTrace();
          //if(m_Log.isErrorEnabled()) m_Log.error("");
    }finally{
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(st);
          SigpGeneralOperations.devolverConexion(oad, con);
          return gVO;
    }
  }
    
    
    
    
    
    
   public GeneralValueObject cargaRelacionExpedientes(GeneralValueObject gVO,AdaptadorSQLBD oad,Connection con) throws TechnicalException {      
      Statement st = null;
      ResultSet rs = null;

      try{
      
        st = con.createStatement();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");

        String sql = "SELECT " + pml_valor +
        " FROM E_PML" +
        " WHERE " + pml_mun + " = " + codMunicipio + " AND " + pml_cod + " = '" + codProcedimiento + "' AND " + pml_cmp + " = 'NOM' AND " + pml_leng + " = "+idiomaDefecto;

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = st.executeQuery(sql);
        while(rs.next()){
          gVO.setAtributo("nombreProcedimiento",rs.getString(pml_valor));
        }

        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(st);

             sql = "SELECT " + usu_nom + "," + sql_rel_uorIni + "," +
                  sql_rel_fecIni + " AS fInicio " + "," +
                  sql_rel_fecFin + " AS fFin " + "," + sql_rel_obs +
                  "," + sql_rel_asunto +" FROM G_REL,"+GlobalNames.ESQUEMA_GENERICO+"A_USU" +
                  " WHERE " + sql_rel_codMun + " = " + codMunicipio + " AND " + sql_rel_ejer + " = " + ejercicio + " AND " +
                  sql_rel_num + "='" + numero + "' AND " + sql_rel_usuIni + " = " + usu_cod;      

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        st = con.createStatement();
        rs = st.executeQuery(sql);      

        while(rs.next()){        
          Timestamp timeFecInicio = rs.getTimestamp("fInicio");
          Timestamp timeFecFin    = rs.getTimestamp("fFin");
          if(timeFecInicio!=null)
              gVO.setAtributo("fechaInicioRelacion",DateOperations.timeStampToString(timeFecInicio));
          else
              gVO.setAtributo("fechaInicioRelacion","");

          if(timeFecFin!=null)
              gVO.setAtributo("fechaFinRelacion",DateOperations.timeStampToString(timeFecFin));
          else
              gVO.setAtributo("fechaFinRelacion","");

          gVO.setAtributo("usuarioRelacion",rs.getString(usu_nom));
          String uorInicio = rs.getString(sql_rel_uorIni);
          gVO.setAtributo("codUnidadOrganicaExp",uorInicio);
          gVO.setAtributo("descUnidadOrganicaExp",getNombreUorByCodigo(uorInicio,con,oad.getParametros()[6]));
          String observ = rs.getString(sql_rel_obs);
          if (observ != null)
            gVO.setAtributo("observaciones",oad.js_escape(observ));
          else gVO.setAtributo("observaciones",observ);
          String asunto = rs.getString(sql_rel_asunto);
          gVO.setAtributo("asuntoUnescape", asunto);  
          if (asunto != null)
            gVO.setAtributo("asunto",oad.js_escape(asunto));
          else gVO.setAtributo("asunto",asunto);
        }
        
      
    }catch (SQLException e){
          m_Log.error(e.getMessage());
          e.printStackTrace();
          //if(m_Log.isErrorEnabled()) m_Log.error("");
    }finally{
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(st);
          return gVO;
    }
  }
    
    
    
    
    

    public Vector cargaTramites(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        Vector tramites = new Vector();
        Vector tramites1 = new Vector();
        Vector tramitesFinal = new Vector();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            String usuario = (String) gVO.getAtributo("usuario");
            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codEntidad = (String) gVO.getAtributo("codEntidad");
            String numeroExpediente = (String) gVO.getAtributo("numeroExpediente");
            m_Log.debug("NUMERO EXPEDIENTE : " + numeroExpediente);

            String sql = "select " + sql_cro_ocu + "," + tra_cod + "," + tml_valor + "," + sql_cro_uor
                    + ", " + oad.convertir(sql_cro_fei, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fInicio "
                    + ", " + oad.convertir(sql_cro_fef, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin "
                    + ", " + oad.convertir(sql_cro_ffp, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFinPlazo "
                    + ", " + sql_cro_usu + "," + usu_nom + "," + sql_cro_usf + ", " + cml_valor + "," + pro_tri + ","
                    + oad.convertir(sql_cro_fli, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fLimite "
                    + " from g_cro, e_tra, e_tml, " + GlobalNames.ESQUEMA_GENERICO + "a_cml a_cml," + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu, e_pro "
                    + " where " + sql_cro_mun + " = " + codMunicipio + " and " + sql_cro_pro + " = '" + codProcedimiento + "' and " + sql_cro_eje + " = " + ejercicio + " and " + sql_cro_num + " = '" + numero + "' and "
                    + tra_mun + " = " + sql_cro_mun + " and " + tra_pro + " = " + sql_cro_pro + " and " + tra_cod + " = " + sql_cro_tra + " and "
                    + usu_cod + " = " + sql_cro_usu + " and "
                    + tml_mun + " = " + tra_mun + " and " + tml_pro + " = " + tra_pro + " and " + tml_tra + " = " + tra_cod + " and " + tml_cmp + " = 'NOM' and " + tml_leng + " = '" + idiomaDefecto + "' and "
                    + cml_cod + " = " + tra_cls + " and " + cml_cmp + " = 'NOM' and " + cml_leng + " = '" + idiomaDefecto + "'"
                    + " AND " + tra_mun + "=" + pro_mun + " AND " + tra_pro + "=" + pro_cod
                    + " order by " + sql_cro_fei + " desc";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject tramiteVO = new GeneralValueObject();
                String codTramite = rs.getString(tra_cod);
                tramiteVO.setAtributo("codTramite", codTramite);
                tramiteVO.setAtributo("ocurrenciaTramite", rs.getString(sql_cro_ocu));
                tramiteVO.setAtributo("tramite", rs.getString(tml_valor));
                tramiteVO.setAtributo("fehcaInicio", rs.getString("fInicio"));
                String fechaFin = rs.getString("fFin");
                tramiteVO.setAtributo("fechaFin", fechaFin);
                tramiteVO.setAtributo("codUsuario", rs.getString(sql_cro_usu));
                tramiteVO.setAtributo("usuario", rs.getString(usu_nom));
                tramiteVO.setAtributo("codUsuarioFinalizacion", rs.getString(sql_cro_usf));
                tramiteVO.setAtributo("clasificacion", rs.getString(cml_valor));
                tramiteVO.setAtributo("codUniTramTramite", rs.getString(sql_cro_uor));
                String codTramiteInicio = rs.getString(pro_tri);
                if (codTramiteInicio != null && codTramiteInicio.equals(codTramite)) {
                    tramiteVO.setAtributo("tramiteInicio", "si");
                } else {
                    tramiteVO.setAtributo("tramiteInicio", "no");
                }
                String fechaLimite = rs.getString("fLimite");
                String fechaFinPlazo = rs.getString("fFinPlazo");
                if (fechaFin == null && fechaLimite != null && !"".equals(fechaLimite)) {
                    if (fechaFinPlazo == null || "".equals(fechaFinPlazo)) {
                        Calendar calendario = Calendar.getInstance();
                        java.util.Date date = calendario.getTime();
                        Fecha fecha = new Fecha();
                        java.util.Date dateLimite = fecha.obtenerDate(fechaLimite);
                        if (date.compareTo(dateLimite) >= 0) {
                            tramiteVO.setAtributo("fueraDePlazo", "si");
                        } else {
                            tramiteVO.setAtributo("fueraDePlazo", "no");
                        }
                    } else {
                        tramiteVO.setAtributo("fueraDePlazo", "no");
                    }

                } else {
                    tramiteVO.setAtributo("fueraDePlazo", "no");
                }
                tramites.add(tramiteVO);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            for (int j = 0; j < tramites.size(); j++) {
                GeneralValueObject g = new GeneralValueObject();
                g = (GeneralValueObject) tramites.elementAt(j);

                sql = "select " + bloq_usu + ", " + usu_nom
                        + " from e_exp_bloq, " + GlobalNames.ESQUEMA_GENERICO + "a_usu "
                        + " where " + bloq_mun + " = " + codMunicipio + " and " + bloq_eje + " = " + ejercicio + " and " + bloq_pro + " = '" + codProcedimiento + "' and "
                        + bloq_tra + " = " + g.getAtributo("codTramite") + " and " + bloq_ocu + " = " + g.getAtributo("ocurrenciaTramite") + " and "
                        + bloq_num + " = '" + numeroExpediente + "' and " + bloq_usu + " = " + usu_cod;
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                st = con.createStatement();
                rs = st.executeQuery(sql);
                g.setAtributo("unidad", "");
                if (rs.next()) {
                    g.setAtributo("usuarioBloq", rs.getString(usu_nom));
                    g.setAtributo("codUsuarioBloq", rs.getString(bloq_usu));
                } else {
                    g.setAtributo("usuarioBloq", "");
                    g.setAtributo("codUsuarioBloq", "");
                }
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);

                String codUnidadTramitadoraTram = (String) g.getAtributo("codUniTramTramite");
                if (codUnidadTramitadoraTram == null || "".equals(codUnidadTramitadoraTram)) {
                    g.setAtributo("unidad", "");
                } else {
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],codUnidadTramitadoraTram);
                    
                    if (uorDTO!=null) {
                        g.setAtributo("unidad",uorDTO.getUor_nom());
                    } 
                }
                tramites1.addElement(g);
            }

            for (int i = 0; i < tramites1.size(); i++) {
                GeneralValueObject g = new GeneralValueObject();
                g = (GeneralValueObject) tramites1.elementAt(i);
                GeneralValueObject g1 = new GeneralValueObject();
                g1.setAtributo("codMunicipio", codMunicipio);
                g1.setAtributo("codProcedimiento", codProcedimiento);
                g1.setAtributo("ejercicio", ejercicio);
                g1.setAtributo("numero", numero);
                g1.setAtributo("usuario", usuario);
                g1.setAtributo("codOrganizacion", codOrganizacion);
                g1.setAtributo("codEntidad", codEntidad);
                g1.setAtributo("ocurrenciaTramite", g.getAtributo("ocurrenciaTramite"));
                g1.setAtributo("codTramite", g.getAtributo("codTramite"));

                int resultado = TramitesRelacionExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, g1);

                String consultar = "no";
                if (resultado == 1) {
                    consultar = "si";
                }
                g.setAtributo("consultar", consultar);
                tramitesFinal.addElement(g);
            }

            SigpGeneralOperations.devolverConexion(oad, con);

        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
            }
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            return tramitesFinal;
        }
    }

    
    
    
    
   public Vector cargaTramites(GeneralValueObject gVO,AdaptadorSQLBD oad,Connection con) throws TechnicalException {        
        Statement st = null;
        ResultSet rs = null;
        Vector tramites = new Vector();
        Vector tramites1 = new Vector();
        Vector tramitesFinal = new Vector();

        try {
            
            st = con.createStatement();
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            String usuario = (String) gVO.getAtributo("usuario");
            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codEntidad = (String) gVO.getAtributo("codEntidad");
            String numeroExpediente = (String) gVO.getAtributo("numeroExpediente");
            m_Log.debug("NUMERO EXPEDIENTE : " + numeroExpediente);

            String sql = "select " + sql_cro_ocu + "," + tra_cod + "," + tml_valor + "," + sql_cro_uor
                    + ", " + oad.convertir(sql_cro_fei, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fInicio "
                    + ", " + oad.convertir(sql_cro_fef, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin "
                    + ", " + oad.convertir(sql_cro_ffp, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFinPlazo "
                    + ", " + sql_cro_usu + "," + usu_nom + "," + sql_cro_usf + ", " + cml_valor + "," + pro_tri + ","
                    + oad.convertir(sql_cro_fli, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fLimite "
                    + " from g_cro, e_tra, e_tml, " + GlobalNames.ESQUEMA_GENERICO + "a_cml a_cml," + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu, e_pro "
                    + " where " + sql_cro_mun + " = " + codMunicipio + " and " + sql_cro_pro + " = '" + codProcedimiento + "' and " + sql_cro_eje + " = " + ejercicio + " and " + sql_cro_num + " = '" + numero + "' and "
                    + tra_mun + " = " + sql_cro_mun + " and " + tra_pro + " = " + sql_cro_pro + " and " + tra_cod + " = " + sql_cro_tra + " and "
                    + usu_cod + " = " + sql_cro_usu + " and "
                    + tml_mun + " = " + tra_mun + " and " + tml_pro + " = " + tra_pro + " and " + tml_tra + " = " + tra_cod + " and " + tml_cmp + " = 'NOM' and " + tml_leng + " = '" + idiomaDefecto + "' and "
                    + cml_cod + " = " + tra_cls + " and " + cml_cmp + " = 'NOM' and " + cml_leng + " = '" + idiomaDefecto + "'"
                    + " AND " + tra_mun + "=" + pro_mun + " AND " + tra_pro + "=" + pro_cod
                    + " order by " + sql_cro_fei + " desc";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject tramiteVO = new GeneralValueObject();
                String codTramite = rs.getString(tra_cod);
                tramiteVO.setAtributo("codTramite", codTramite);
                tramiteVO.setAtributo("ocurrenciaTramite", rs.getString(sql_cro_ocu));
                tramiteVO.setAtributo("tramite", rs.getString(tml_valor));
                tramiteVO.setAtributo("fehcaInicio", rs.getString("fInicio"));
                String fechaFin = rs.getString("fFin");
                tramiteVO.setAtributo("fechaFin", fechaFin);
                tramiteVO.setAtributo("codUsuario", rs.getString(sql_cro_usu));
                tramiteVO.setAtributo("usuario", rs.getString(usu_nom));
                tramiteVO.setAtributo("codUsuarioFinalizacion", rs.getString(sql_cro_usf));
                tramiteVO.setAtributo("clasificacion", rs.getString(cml_valor));
                tramiteVO.setAtributo("codUniTramTramite", rs.getString(sql_cro_uor));
                String codTramiteInicio = rs.getString(pro_tri);
                if (codTramiteInicio != null && codTramiteInicio.equals(codTramite)) {
                    tramiteVO.setAtributo("tramiteInicio", "si");
                } else {
                    tramiteVO.setAtributo("tramiteInicio", "no");
                }
                String fechaLimite = rs.getString("fLimite");
                String fechaFinPlazo = rs.getString("fFinPlazo");
                if (fechaFin == null && fechaLimite != null && !"".equals(fechaLimite)) {
                    if (fechaFinPlazo == null || "".equals(fechaFinPlazo)) {
                        Calendar calendario = Calendar.getInstance();
                        java.util.Date date = calendario.getTime();
                        Fecha fecha = new Fecha();
                        java.util.Date dateLimite = fecha.obtenerDate(fechaLimite);
                        if (date.compareTo(dateLimite) >= 0) {
                            tramiteVO.setAtributo("fueraDePlazo", "si");
                        } else {
                            tramiteVO.setAtributo("fueraDePlazo", "no");
                        }
                    } else {
                        tramiteVO.setAtributo("fueraDePlazo", "no");
                    }

                } else {
                    tramiteVO.setAtributo("fueraDePlazo", "no");
                }
                tramites.add(tramiteVO);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            for (int j = 0; j < tramites.size(); j++) {
                GeneralValueObject g = new GeneralValueObject();
                g = (GeneralValueObject) tramites.elementAt(j);

                sql = "select " + bloq_usu + ", " + usu_nom
                        + " from e_exp_bloq, " + GlobalNames.ESQUEMA_GENERICO + "a_usu "
                        + " where " + bloq_mun + " = " + codMunicipio + " and " + bloq_eje + " = " + ejercicio + " and " + bloq_pro + " = '" + codProcedimiento + "' and "
                        + bloq_tra + " = " + g.getAtributo("codTramite") + " and " + bloq_ocu + " = " + g.getAtributo("ocurrenciaTramite") + " and "
                        + bloq_num + " = '" + numeroExpediente + "' and " + bloq_usu + " = " + usu_cod;
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                st = con.createStatement();
                rs = st.executeQuery(sql);
                g.setAtributo("unidad", "");
                if (rs.next()) {
                    g.setAtributo("usuarioBloq", rs.getString(usu_nom));
                    g.setAtributo("codUsuarioBloq", rs.getString(bloq_usu));
                } else {
                    g.setAtributo("usuarioBloq", "");
                    g.setAtributo("codUsuarioBloq", "");
                }
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);

                String codUnidadTramitadoraTram = (String) g.getAtributo("codUniTramTramite");
                if (codUnidadTramitadoraTram == null || "".equals(codUnidadTramitadoraTram)) {
                    g.setAtributo("unidad", "");
                } else {
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],codUnidadTramitadoraTram);
                    
                    if (uorDTO!=null) {
                        g.setAtributo("unidad",uorDTO.getUor_nom());
                    } 
                }
                tramites1.addElement(g);
            }

            for (int i = 0; i < tramites1.size(); i++) {
                GeneralValueObject g = new GeneralValueObject();
                g = (GeneralValueObject) tramites1.elementAt(i);
                GeneralValueObject g1 = new GeneralValueObject();
                g1.setAtributo("codMunicipio", codMunicipio);
                g1.setAtributo("codProcedimiento", codProcedimiento);
                g1.setAtributo("ejercicio", ejercicio);
                g1.setAtributo("numero", numero);
                g1.setAtributo("usuario", usuario);
                g1.setAtributo("codOrganizacion", codOrganizacion);
                g1.setAtributo("codEntidad", codEntidad);
                g1.setAtributo("ocurrenciaTramite", g.getAtributo("ocurrenciaTramite"));
                g1.setAtributo("codTramite", g.getAtributo("codTramite"));

                int resultado = TramitesRelacionExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, g1);

                String consultar = "no";
                if (resultado == 1) {
                    consultar = "si";
                }
                g.setAtributo("consultar", consultar);
                tramitesFinal.addElement(g);
            }

            
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
            }
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);            
            return tramitesFinal;
        }
    }
    
    
    
    
    
    public Vector cargaListaTramites(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        
        Vector tramites = new Vector();
        
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numero = (String) gVO.getAtributo("numero");
        
        try {
            
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            
            sql = "select " + sql_cro_ocu + "," + sql_cro_tra
                    + " from g_cro "
                    + " where " + sql_cro_mun + " = " + codMunicipio + " and " + sql_cro_pro + " = '" + codProcedimiento + "' and "
                    + sql_cro_eje + " = " + ejercicio + " and " + sql_cro_num + " = '" + numero + "'"
                    + " order by " + sql_cro_fei + " desc";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                GeneralValueObject tramiteVO = new GeneralValueObject();
                String codTramite = rs.getString(sql_cro_tra);
                tramiteVO.setAtributo("codTramite", codTramite);
                tramiteVO.setAtributo("ocurrenciaTramite", rs.getString(sql_cro_ocu));
                tramites.add(tramiteVO);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            
        } catch (BDException ex) {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            tramites = new Vector(); 
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
            e.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return tramites;
        
    }


    public Vector cargaListaExpedientes(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null, st2;
        ResultSet rs = null, rs2;
        String sql;

        Vector<GeneralValueObject> expedientes = new Vector<GeneralValueObject>();

        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numero = (String) gVO.getAtributo("numero");

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT E_EXP." + sql_exp_asunto + " AS asunto, E_EXP." + sql_exp_num + " AS numero, E_EXP." +
                    sql_exp_codMun + " AS munic, E_EXP." + sql_exp_ejer + " AS ejer, E_EXP." + sql_exp_codProc +
                    " AS proced FROM G_EXP,E_EXP WHERE E_EXP." + sql_exp_codMun + "=G_EXP." + sql_rel_exp_codMunR +
                    " AND E_EXP." + sql_exp_codProc + "=G_EXP." + sql_rel_exp_codProcR + " AND E_EXP." + sql_exp_ejer + "=G_EXP." + sql_rel_exp_ejerR +
                    " AND E_EXP." + sql_exp_num + "=G_EXP." + sql_rel_exp_numR + " AND G_EXP." + sql_rel_exp_codMun + "=" + codMunicipio +
                    " AND G_EXP." + sql_rel_exp_codProc + "='" + codProcedimiento + "' AND G_EXP." + sql_rel_exp_ejer + "=" + ejercicio +
                    " AND G_EXP." + sql_rel_exp_num + "='" + numero + "'";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);

            while (rs.next()) {
                gVO = new GeneralValueObject();
                gVO.setAtributo("asuExp", rs.getString("asunto"));
                gVO.setAtributo("numExp", rs.getString("numero"));
                gVO.setAtributo("ejeExp", rs.getString("ejer"));

                int codRolPD = -1;
                sql = "SELECT " + rol_cod + " FROM E_ROL WHERE " + rol_mun + "=" + rs.getString("munic") +
                        " AND " + rol_pro + "='" + rs.getString("proced") + "' AND " + rol_pde + "=1";
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                st2 = con.createStatement();
                rs2 = st2.executeQuery(sql);
                while (rs2.next()) {
                    codRolPD = rs2.getInt(rol_cod);
                }
                SigpGeneralOperations.closeResultSet(rs2);
                SigpGeneralOperations.closeStatement(st2);
                
                st2 = con.createStatement();
                sql = "SELECT " + ext_rol + "," +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{hte_pa1, "''", hte_ap1})+" AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{hte_pa2,"''", hte_ap2})+" AS ap2, " + hte_nom +" AS nombre"+
                        " FROM E_EXT"
                        + " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)"
                        + " WHERE " + ext_mun + "=" + rs.getString("munic") + " AND " +
                        ext_eje + "=" + rs.getString("ejer") + " AND " + ext_num + "='" +
                        rs.getString("numero") + "' AND " +
                        " MOSTRAR = 1 ORDER BY MOSTRAR DESC, 1";
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs2 = st2.executeQuery(sql);
                
                gVO.setAtributo("titExp", "");
                if (rs2.next()) {
                     //formato del interesado
                     String ap1=rs2.getString("ap1");
                     String ap2=rs2.getString("ap2");
                     String nombre=rs2.getString("nombre");
                     String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);                              
                     
                     gVO.setAtributo("titExp", titular);                     
                    
                }
                SigpGeneralOperations.closeResultSet(rs2);
                SigpGeneralOperations.closeStatement(st2);
                expedientes.addElement(gVO);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } catch (BDException ex) {
            expedientes = new Vector<GeneralValueObject>(); 
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            ex.printStackTrace();
        } catch (SQLException e) {
            expedientes = new Vector<GeneralValueObject>();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return expedientes;
    }
    
    
    
    public Vector cargaListaExpedientes(GeneralValueObject gVO,AdaptadorSQLBD oad,Connection con) throws TechnicalException {
        Statement st = null, st2 = null;
        ResultSet rs = null, rs2 = null;
        String sql;

        Vector<GeneralValueObject> expedientes = new Vector<GeneralValueObject>();

        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numero = (String) gVO.getAtributo("numero");

        try {

            st = con.createStatement();
            sql = "SELECT E_EXP." + sql_exp_asunto + " AS asunto, E_EXP." + sql_exp_num + " AS numero, E_EXP." +
                    sql_exp_codMun + " AS munic, E_EXP." + sql_exp_ejer + " AS ejer, E_EXP." + sql_exp_codProc +
                    " AS proced FROM G_EXP,E_EXP WHERE E_EXP." + sql_exp_codMun + "=G_EXP." + sql_rel_exp_codMunR +
                    " AND E_EXP." + sql_exp_codProc + "=G_EXP." + sql_rel_exp_codProcR + " AND E_EXP." + sql_exp_ejer + "=G_EXP." + sql_rel_exp_ejerR +
                    " AND E_EXP." + sql_exp_num + "=G_EXP." + sql_rel_exp_numR + " AND G_EXP." + sql_rel_exp_codMun + "=" + codMunicipio +
                    " AND G_EXP." + sql_rel_exp_codProc + "='" + codProcedimiento + "' AND G_EXP." + sql_rel_exp_ejer + "=" + ejercicio +
                    " AND G_EXP." + sql_rel_exp_num + "='" + numero + "'";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);

            while (rs.next()) {
                gVO = new GeneralValueObject();
                gVO.setAtributo("asuExp", rs.getString("asunto"));
                gVO.setAtributo("numExp", rs.getString("numero"));
                gVO.setAtributo("ejeExp", rs.getString("ejer"));

                int codRolPD = -1;
                sql = "SELECT " + rol_cod + " FROM E_ROL WHERE " + rol_mun + "=" + rs.getString("munic") +
                        " AND " + rol_pro + "='" + rs.getString("proced") + "' AND " + rol_pde + "=1";
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                st2 = con.createStatement();
                rs2 = st2.executeQuery(sql);
                while (rs2.next()) {
                    codRolPD = rs2.getInt(rol_cod);
                }
                SigpGeneralOperations.closeResultSet(rs2);
                SigpGeneralOperations.closeStatement(st2);
                
                st2 = con.createStatement();
                sql = "SELECT " + ext_rol + "," +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{hte_pa1, "''", hte_ap1})+" AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{hte_pa2,"''", hte_ap2})+" AS ap2, " + hte_nom +" AS nombre"+
                        " FROM E_EXT"
                        +" left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)"
                        + " WHERE " + ext_mun + "=" + rs.getString("munic") + " AND " +
                        ext_eje + "=" + rs.getString("ejer") + " AND " + ext_num + "='" +
                        rs.getString("numero") + "' AND " +
                        "  MOSTRAR = 1 ORDER BY MOSTRAR DESC, 1";
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs2 = st2.executeQuery(sql);
                
                gVO.setAtributo("titExp", "");
                if (rs2.next()) {
                     //formato del interesado
                     String ap1=rs2.getString("ap1");
                     String ap2=rs2.getString("ap2");
                     String nombre=rs2.getString("nombre");
                     String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);                              
                     
                     gVO.setAtributo("titExp", titular);                     
                    
                }
                SigpGeneralOperations.closeResultSet(rs2);
                SigpGeneralOperations.closeStatement(st2);
                expedientes.addElement(gVO);
            }
            
        }catch (SQLException e) {
            expedientes = new Vector<GeneralValueObject>();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);         
            SigpGeneralOperations.closeResultSet(rs2);
            SigpGeneralOperations.closeStatement(st2);
        }
        return expedientes;
    }


    public Vector cargaListaInteresados(GeneralValueObject gVO, String[] params) throws TechnicalException {

      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      ResultSet rs = null;
      String sql = null;

      Vector interesados = new Vector();

      Vector expedientes = new Vector();
      expedientes = cargaListaExpedientes(gVO, params);
      String cadExp = "";
      for (int m=0; m< expedientes.size(); m++) {
          GeneralValueObject temp = (GeneralValueObject)expedientes.get(m);
          String numExp = (String)temp.getAtributo("numExp");
          cadExp += "'" + numExp + "',";
      }
      cadExp = cadExp.substring(0, cadExp.length()-1);

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");

      try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            String from = "distinct " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT, new String[]{hte_pa1, "' '", hte_ap1, "' '", hte_pa2, "' '",
                          hte_ap2, "', '", hte_nom}) + " AS titular ," + hte_doc +" AS documento, " + hte_ter + " AS idTercero ";
            String where = ext_mun + "=" + codMunicipio + " AND " + ext_num + " in (" + cadExp + ")";
            String[] join = new String[8];
            join[0] = "E_EXT";
            join[1] = "INNER";
            join[2] = "e_rol";
            join[3] = "e_ext." + ext_mun + "=e_rol." + rol_mun + " AND " +
                    "e_ext." + ext_rol+ "=e_rol." + rol_cod + " AND " +
                    "e_ext.ext_pro= " +  "e_rol." + rol_pro;
            join[4] = "LEFT";
            join[5] = "t_hte";
            join[6] = "e_ext." + ext_ter + "=t_hte." + hte_ter + " AND " +
                    "e_ext." + ext_nvr + "=t_hte." + hte_nvr;
            join[7] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject inter = new GeneralValueObject();
                String titular = rs.getString("titular");
                inter.setAtributo("titular", titular);
                String documento = rs.getString("documento");
                if (documento == null) inter.setAtributo("documento", "         ");
                else inter.setAtributo("documento", documento);
                String idTercero = rs.getString("idTercero");
                if(idTercero == null) inter.setAtributo("idTercero", " ");
                else inter.setAtributo("idTercero", idTercero);
                interesados.addElement(inter);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
                    
        } catch (SQLException ex) {
            interesados = new Vector();
          if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
          ex.printStackTrace();
        }catch (BDException e){
          interesados = new Vector();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
          e.printStackTrace();
        }finally{
           SigpGeneralOperations.closeResultSet(rs);
           SigpGeneralOperations.closeStatement(st);
           SigpGeneralOperations.devolverConexion(oad, con);
        }
        return interesados;
    }


    public Vector cargaListaInteresadosxExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {

      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      ResultSet rs = null;
      String sql = null;

      Vector temp = new Vector();

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String ejercicio = (String)gVO.getAtributo("ejercicio");

      try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            Vector expedientes = new Vector();
            expedientes = cargaListaExpedientes(gVO, params);
            for (int m=0; m< expedientes.size(); m++) {
                GeneralValueObject exp = new GeneralValueObject();

                GeneralValueObject t = (GeneralValueObject)expedientes.get(m);
                String numExp = (String)t.getAtributo("numExp");
                exp.setAtributo("expediente", numExp);
                st = con.createStatement();
                String from = oad.funcionCadena(oad.FUNCIONCADENA_CONCAT, new String[]{hte_pa1, "' '", hte_ap1, "' '", hte_pa2, "' '",
                              hte_ap2, "', '", hte_nom}) + " AS titular ";
                String where = ext_mun + "=" + codMunicipio + " AND " + ext_eje + "=" +
                        ejercicio + " AND " + ext_num + " = '" + numExp + "'";
                String[] join = new String[8];
                join[0] = "E_EXT";
                join[1] = "INNER";
                join[2] = "e_rol";
                join[3] = "e_ext." + ext_mun + "=e_rol." + rol_mun + " AND " +
                        "e_ext." + ext_rol+ "=e_rol." + rol_cod + " AND " +
                        "e_ext.ext_pro= " +  "e_rol." + rol_pro;
                join[4] = "LEFT";
                join[5] = "t_hte";
                join[6] = "e_ext." + ext_ter + "=t_hte." + hte_ter + " AND " +
                        "e_ext." + ext_nvr + "=t_hte." + hte_nvr;
                join[7] = "false";

                sql = oad.join(from,where,join);
                String parametros[] = {"1","1"};
                sql += oad.orderUnion(parametros);
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                st = con.createStatement();
                rs = st.executeQuery(sql);
                Vector interesados = new Vector();
                while(rs.next()){
                    String titular = rs.getString("titular");
                    interesados.addElement(titular);
                }
                exp.setAtributo("interesados", interesados);               
            temp.add(exp);    
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
          }
        } catch (SQLException e) {
            temp = new Vector(); // Vacío
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
            e.printStackTrace();
        } catch (BDException e) {
            temp = new Vector(); // Vacío
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
            e.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return temp;
    }


    public int actualizaListaDocumentosRelacion(GeneralValueObject gVO, String[] params) throws TechnicalException {

      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      String sql = null;
      int resultado=0;
      int resultado1 = 0;

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");

      Vector documentos = (Vector) gVO.getAtributo("listaDocumentos");
      GeneralValueObject documento= null;
      if(m_Log.isDebugEnabled()) m_Log.debug("el vector documentos es  : " + documentos);
      if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño del vector documentos es  : " + documentos.size());

      if (documentos != null) {
        if (documentos.size()>0) {
          try{

          oad = new AdaptadorSQLBD(params);
          con = oad.getConnection();
          st = con.createStatement();
          oad.inicioTransaccion(con);

          // Borrar todos.
          sql ="DELETE FROM g_dor "
          + " WHERE g_dor."+sql_dor_codProc+" = '" + codProcedimiento + "' AND  g_dor."+sql_dor_codMun+" = " + codMunicipio
          + " AND g_dor."+sql_dor_ejer+" ='"+ ejercicio +"'" + " AND g_dor."+sql_dor_num+" ='"+ numero+"'";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          resultado1 = st.executeUpdate(sql);
          SigpGeneralOperations.closeStatement(st);

          sql = "INSERT INTO G_DOR ( " + sql_dor_codMun + "," + sql_dor_ejer + ", " + sql_dor_num
          + ", " + sql_dor_codProc + "," + sql_dor_fEntreg + "," + sql_dor_codDoc
          + ") VALUES ( " + codMunicipio + "," + ejercicio + ",'" + numero +"'"
          + ",'" + codProcedimiento+"'"
          + ", " + oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null) + ", ";


          String entrar = "no";
          for (int i=0; i<documentos.size(); i++) {
            documento = (GeneralValueObject) documentos.elementAt(i);
            String entregado = (String) documento.getAtributo("ENTREGADO");
            if ("SI".equals(entregado)) {
              entrar = "si";
              String sql1 =  sql + (String) documento.getAtributo("codigo") + ")";
              if(m_Log.isDebugEnabled()) m_Log.debug(sql1);
              st = con.createStatement();
              resultado = st.executeUpdate(sql1);
            }
          }
          if("no".equals(entrar)) {
            resultado = 1;
          }
          if (resultado >= 0)
              SigpGeneralOperations.commit(oad, con);
          else
              SigpGeneralOperations.rollBack(oad, con);
        }catch (BDException e){
              SigpGeneralOperations.rollBack(oad, con);
              resultado=-1;
              e.printStackTrace();
              if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch (SQLException e){
              SigpGeneralOperations.rollBack(oad, con);
              resultado=-1;
              e.printStackTrace();
              if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
           SigpGeneralOperations.closeStatement(st);
           SigpGeneralOperations.devolverConexion(oad, con);
           
        }
    } else {
      resultado = 1;
    }
    }
    return resultado;
    }

    public int grabarRelacion(GeneralValueObject gVO, String[] params) throws TechnicalException {

      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      String sql = null;
      int resultado=0;

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numeroRelacion = (String)gVO.getAtributo("numero");
      String observaciones = (String)gVO.getAtributo("observaciones");
      String asunto = (String)gVO.getAtributo("asunto");

      try{

      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);
      st = con.createStatement();

      sql = "UPDATE G_REL SET ";
      if(observaciones == null || "".equals(observaciones)) {
        sql += sql_rel_obs + "=null,";
      } else {
        sql += sql_rel_obs + "='" + observaciones + "',";
      }
      if(asunto == null || "".equals(asunto)) {
        sql += sql_rel_asunto + "=null";
      } else {
        sql += sql_rel_asunto + "='" + asunto + "'";
      }
      sql += " WHERE " + sql_rel_codMun + "=" + codMunicipio + " AND " +
      sql_rel_ejer + "=" + ejercicio + " AND " + sql_rel_num + "='" + numeroRelacion + "'";

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      resultado = st.executeUpdate(sql);
      SigpGeneralOperations.closeStatement(st);

      if (resultado >= 0)
          SigpGeneralOperations.commit(oad, con);
      else
          SigpGeneralOperations.rollBack(oad, con);

    }catch (BDException e){
          SigpGeneralOperations.rollBack(oad, con);
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
          resultado=-1;
    }catch (SQLException e){
          SigpGeneralOperations.rollBack(oad, con);
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
          resultado=-1;
    }finally{
        SigpGeneralOperations.closeStatement(st);                          
        SigpGeneralOperations.devolverConexion(oad, con);
    }
    return resultado;
    }

    public Vector cargaTramitesDisponibles(GeneralValueObject gVO, String[] params) throws TechnicalException {
      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      ResultSet rs = null;

      Vector tramites = new Vector();

      try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      st = con.createStatement();

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numeroRelacion");
      String codUsuario = (String) gVO.getAtributo("usuario");
      String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
      String codEntidad = (String) gVO.getAtributo("codEntidad");

      String sql = "SELECT DISTINCT " + tra_cod + "," + tml_valor + "," + cml_valor +
      "," + tra_uin + " FROM G_REL,E_TRA,E_TML,"+GlobalNames.ESQUEMA_GENERICO+"A_CML WHERE " + sql_rel_codMun + "=" + tra_mun + " AND " +
      sql_rel_codProc + "=" + tra_pro + " AND " + tml_mun + "=" + tra_mun + " AND " + tml_pro + "=" +
      tra_pro + " AND " + tml_tra + "=" + tra_cod + " AND " + tml_cmp + "='NOM' AND " +
      tml_leng + "='"+idiomaDefecto+"' AND " + cml_cod + "=" + tra_cls + " AND " + cml_cmp + "='NOM' AND " +
      cml_leng + "='"+idiomaDefecto+"' AND " + tra_mun + "=" + codMunicipio + " AND " + tra_pro + "='" +
      codProcedimiento + "' AND " + tra_fba + " IS NULL AND ((EXISTS ( SELECT DISTINCT " +
      sql_uou_uor + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU WHERE " + sql_uou_usu + "=" + codUsuario + " AND " + sql_uou_org + "=" +
      codOrganizacion + " AND " + sql_uou_ent + "=" + codEntidad + " AND " + sql_uou_uor + "=" +
      sql_rel_uorIni + ") AND " + tra_uin + " IS NOT NULL) OR EXISTS (SELECT DISTINCT " + sql_uou_uor +
      " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU WHERE " + sql_uou_usu + "=" + codUsuario + " AND " + sql_uou_org + "=" +
      codOrganizacion + " AND " + sql_uou_ent + "=" + codEntidad + " AND " + sql_uou_uor + "=" + tra_uin + ") OR " + tra_uin + "=-99999 OR " + tra_uin + "=-99998) AND ( " +
      tra_ocu + " > ( select count(*) from g_cro where " + sql_cro_mun + "=" +
      codMunicipio + " and " + sql_cro_pro +"='"+codProcedimiento+"'"+ " and " + sql_cro_eje + " = " + ejercicio + " and " +
      sql_cro_num + " = '" + numero + "' " + " and " + sql_cro_tra + " = " + tra_cod + " ) "+
      " or "  + tra_ocu + " is null )" + " order by "+ cml_valor ;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = st.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject tramiteVO = new GeneralValueObject();
        tramiteVO.setAtributo("codTramite",rs.getString(tra_cod));
        tramiteVO.setAtributo("tramite",rs.getString(tml_valor));
        tramiteVO.setAtributo("clasificacion",rs.getString(cml_valor));
        tramiteVO.setAtributo("codUnidadInicio",rs.getString(tra_uin));
        tramites.add(tramiteVO);
      }

      SigpGeneralOperations.closeStatement(st);
      SigpGeneralOperations.closeResultSet(rs);
      

    }catch (BDException e){         
          m_Log.error(e.getMessage());
          e.printStackTrace();
          tramites = new Vector();
    }finally{
        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(st);
        SigpGeneralOperations.devolverConexion(oad, con);
        return tramites;
    }
    }

    public int retrocederRelacion(GeneralValueObject gVO, String[] params) throws TechnicalException {
      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      ResultSet rs = null;
      int res = -1;
      StringBuffer sql = new StringBuffer();

      try{
        oad = new AdaptadorSQLBD(params);
        con = oad.getConnection();
        oad.inicioTransaccion(con) ; 
        st = con.createStatement();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numeroRelacion");
        String codTramiteRetroceder = (String) gVO.getAtributo("codTramiteRetroceder");
        String ocurrenciaTramiteRetroceder = (String) gVO.getAtributo("ocurrenciaTramiteRetroceder");
        String fecha_inicio = "";
        String fecha_fin = "";

        sql.append ("SELECT ").append(oad.convertir(sql_cro_fei, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                  " AS FEI, ").append(oad.convertir(sql_cro_fef, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                  " AS FEF");
        sql.append (" FROM G_CRO");
        sql.append (" WHERE ");
        sql.append (sql_cro_mun).append(" = ").append(codMunicipio);
        sql.append (" AND ").append(sql_cro_pro).append(" = '").append(codProcedimiento).append("'");
        sql.append (" AND ").append(sql_cro_eje).append(" = '").append(ejercicio).append("'");
        sql.append (" AND ").append(sql_cro_num).append(" = '").append(numero).append("'");
        sql.append (" AND ").append(sql_cro_tra).append(" = ").append(codTramiteRetroceder);
          sql.append (" AND ").append(sql_cro_ocu).append(" = ").append(ocurrenciaTramiteRetroceder);
        sql.append (" ORDER BY " + sql_cro_fei + " DESC");

        if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
        rs = st.executeQuery(sql.toString());

        while(rs.next()){
          fecha_fin = (rs.getString("FEF")!=null)?rs.getString("FEF"):"";
          fecha_inicio = rs.getString("FEI");
        }
        gVO.setAtributo("fechaInicio", fecha_inicio);
        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(st);


        if(fecha_fin.equals("") && fecha_inicio!= null && !fecha_inicio.equals("")) {

            sql = new StringBuffer();
            sql.append ("DELETE FROM G_CRO");
            sql.append (" WHERE ");
            sql.append (sql_cro_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(sql_cro_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(sql_cro_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(sql_cro_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(sql_cro_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(sql_cro_ocu).append(" = ").append(ocurrenciaTramiteRetroceder);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            st = con.createStatement();
            res = st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } else {
            String sqlPru = "SELECT CRO_FEI FROM G_CRO WHERE CRO_PRO='" + codProcedimiento + "' AND CRO_EJE=" + ejercicio
                    + " AND CRO_NUM='" + numero + "' AND CRO_TRA=" + codTramiteRetroceder + " AND CRO_OCU=" + ocurrenciaTramiteRetroceder;
            PreparedStatement ps = con.prepareStatement(sqlPru);
            rs = ps.executeQuery();
            rs.next();
            
            Timestamp cro_fei= rs.getTimestamp("CRO_FEI");
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            
            String sqlUpdate = "UPDATE G_CRO SET CRO_FEF=?,CRO_USF=? WHERE CRO_PRO='" + codProcedimiento + "' AND CRO_EJE=" + ejercicio
                    + " AND CRO_NUM='" + numero + "' AND CRO_TRA=" + codTramiteRetroceder + " AND CRO_OCU=" + ocurrenciaTramiteRetroceder
                    + " AND CRO_FEI=?";
            
            m_Log.debug("********* sqlUpdate; " + sqlUpdate);
            ps = con.prepareStatement(sqlUpdate);
            int i=1;
            ps.setNull(i++,java.sql.Types.TIMESTAMP);
            ps.setNull(i++,java.sql.Types.INTEGER);
            ps.setTimestamp(i++,cro_fei);
            int rowsUpdated = ps.executeUpdate();
            SigpGeneralOperations.closeStatement(ps);
            m_Log.debug("FichaRelacionExpedientesDAO rowsUpdated: " + rowsUpdated);
            
          String codTramiteUltCerrado = "";
          String ocuTramiteUltCerrado = "";
          sql = new StringBuffer();
          sql.append ("SELECT ").append(sql_cro_tra + "," + sql_cro_ocu);
          sql.append (" FROM G_CRO");
          sql.append (" WHERE ");
          sql.append (sql_cro_mun).append(" = ").append(codMunicipio);
          sql.append (" AND ").append(sql_cro_pro).append(" = '").append(codProcedimiento).append("'");
          sql.append (" AND ").append(sql_cro_eje).append(" = '").append(ejercicio).append("'");
          sql.append (" AND ").append(sql_cro_num).append(" = '").append(numero).append("'");
          sql.append (" ORDER BY " + sql_cro_fef + " DESC");
          
          if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
          st = con.createStatement();
          rs = st.executeQuery(sql.toString());

          if(rs.next()) {
              codTramiteUltCerrado = rs.getString(sql_cro_tra);
              ocuTramiteUltCerrado = rs.getString(sql_cro_ocu);
          }
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(st);
          
          sql = new StringBuffer();
          sql.append ("UPDATE G_REL SET ");
          if(codTramiteUltCerrado != null && !"".equals(codTramiteUltCerrado)) {
              sql.append (sql_rel_traIni).append(" = ").append(codTramiteUltCerrado).append(",");
            sql.append (sql_rel_traAct).append(" = ").append(ocuTramiteUltCerrado);
          } else {
              sql.append (sql_rel_traIni).append(" = ").append("NULL").append(",");
            sql.append (sql_rel_traAct).append(" = ").append("NULL");
          }
          sql.append (" WHERE ");
          sql.append (sql_rel_codMun).append(" = ").append(codMunicipio);
          sql.append (" AND ").append(sql_rel_ejer).append(" = '").append(ejercicio).append("'");
          sql.append (" AND ").append(sql_rel_num).append(" = '").append(numero).append("'");

          if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
          st = con.createStatement();
          res = st.executeUpdate(sql.toString());
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(st);
        }

        SigpGeneralOperations.commit(oad, con);

      }catch (BDException e){
        SigpGeneralOperations.rollBack(oad, con);
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        e.printStackTrace();
      }finally{
          SigpGeneralOperations.closeResultSet(rs);
          SigpGeneralOperations.closeStatement(st);
          SigpGeneralOperations.devolverConexion(oad, con);
          return res;
      }
    }
        
  public Vector cargaEstructuraInteresados(GeneralValueObject gVO, String[] params) throws TechnicalException {
       
            AdaptadorSQLBD oad = null;
            Connection con = null;
            Statement st = null;
            ResultSet rs = null;
            String sql = "";
            String from = "";
            String where = "";
            Vector lista = new Vector();

             try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

            sql = "SELECT " + rol_des + " FROM E_ROL WHERE " + rol_mun + "=" +
                  codMunicipio + " AND " + rol_pro + "='" + codProcedimiento + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
              EstructuraCampo eC = new EstructuraCampo();
              String codCampo = rs.getString(rol_des);
              eC.setCodCampo(codCampo);
              lista.addElement(eC);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            
        } catch (SQLException ex) {
            Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BDException ex) {
            Logger.getLogger(FichaRelacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st); 
          SigpGeneralOperations.devolverConexion(oad, con);
          return lista;
        }

    
  }


   

    public int eliminarDatosSuplementariosExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        int res = -1;
        int resultadoEliminar = 0;
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String sql = "";
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; // Inicio transacción
            st = con.createStatement();
            sql = "DELETE FROM E_TNU WHERE " + tnu_mun + "=" + codMunicipio + " AND " + tnu_eje + "=" +
                  ejercicio + " AND " + tnu_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TNUC WHERE " + tnuc_mun + "=" + codMunicipio + " AND " + tnuc_eje + "=" +
                  ejercicio + " AND " + tnuc_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TTL WHERE " + ttl_mun + "=" + codMunicipio + " AND " + ttl_eje + "=" +
                  ejercicio + " AND " + ttl_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TXT WHERE " + txt_mun + "=" + codMunicipio + " AND " + txt_eje + "=" +
                  ejercicio + " AND " + txt_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TDEX WHERE TDEX_MUN =" + codMunicipio + " AND TDEX_EJE =" +
                  ejercicio + " AND TDEX_NUM ='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TFI WHERE " + tfi_mun + "=" + codMunicipio + " AND " + tfi_eje + "=" +
                  ejercicio + " AND " + tfi_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TFE WHERE " + tfe_mun + "=" + codMunicipio + " AND " + tfe_eje + "=" +
                  ejercicio + " AND " + tfe_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TFEC WHERE " + tfec_mun + "=" + codMunicipio + " AND " + tfec_eje + "=" +
                  ejercicio + " AND " + tfec_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            
            st = con.createStatement();
            sql = "DELETE FROM E_TDE WHERE " + tde_mun + "=" + codMunicipio + " AND " + tde_eje + "=" +
                  ejercicio + " AND " + tde_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            res = st.executeUpdate(sql);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.commit(oad, con);                        
            
          }catch (BDException e){
            SigpGeneralOperations.rollBack(oad, con);
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            resultadoEliminar = 0;
          }finally{
              SigpGeneralOperations.devolverConexion(oad, con);
              return resultadoEliminar;
          }
    }

    public int eliminarDocumentosExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int resultadoEliminar = 0;
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numero = (String) gVO.getAtributo("numero");
        String sql = "";
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; 
            st = con.createStatement();
            sql ="DELETE FROM E_DOE WHERE e_doe." + sql_doe_codProc + " = '" + codProcedimiento + "' AND  e_doe." +
                    sql_doe_codMun + " = " + codMunicipio + " AND e_doe." + sql_doe_ejer + " ='" + ejercicio +
                    "' AND e_doe."+sql_doe_num+" ='"+ numero+"'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.commit(oad, con);
            
          }catch (BDException e){
            SigpGeneralOperations.rollBack(oad, con);
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            resultadoEliminar = 0;
          }finally{
              SigpGeneralOperations.devolverConexion(oad, con);
              return resultadoEliminar;
          }
    }
}