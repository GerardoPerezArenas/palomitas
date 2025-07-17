package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.ErrorImportacionXPDL;
import es.altia.agora.business.sge.ExistenciaUorImportacionVO;
import es.altia.agora.business.sge.RolProcedimientoVO;
import es.altia.agora.business.sge.TablasIntercambiadorasValueObject;
import es.altia.agora.business.sge.firma.dao.FirmaFlujoDAO;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.StringOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import java.util.Map;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.CallableStatement;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;

/**
 * Objeto de acceso a datos encargado de importar en cualquier entorno  un nuevo procedimiento con sus trámites
 * @author oscar.rodriguez
 */
public class ImportacionProcedimientoDAO {

    private static ImportacionProcedimientoDAO instance = null;
    private Logger log = Logger.getLogger(ImportacionProcedimientoDAO.class);
    protected static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    private final String ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_1 = "errNoExisteCargoTramite1";
    private final String ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_2 = "errNoExisteCargoTramite2";
    private final String ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_3 = "errNoExisteCargoTramite3";
    private final String ERR_NO_EXISTE_DESPLEGABLE_TRAMITE_PARTE_1 = "errNoExisteDesplTramite1";
    private final String ERR_NO_EXISTE_DESPLEGABLE_TRAMITE_PARTE_2 = "errNoExisteDesplTramite2";
    private final String ERR_NO_EXISTE_DESPLEGABLE_TRAMITE_PARTE_3 = "errNoExisteDesplTramite3";
    private final String ERR_NO_EXISTE_USUARIO_FIRMA_FLUJO = "errNoExisteUsuarioFirmaDoc";
    private final int XPDL_ERROR_NO_EXISTE_USUARIO_FIRMA_FLUJO = 4;


    private final String ERR_NO_EXISTE_DESPLEGABLE_PROC_PARTE_1 = "errNoExisteDesplProc1";
    private final String ERR_NO_EXISTE_DESPLEGABLE_PROC_PARTE_2 = "errNoExisteDesplProc2";
    private final String ERR_NO_EXISTE_DESPLEGABLE_PROC_PARTE_3 = "errNoExisteDesplProc3";

    private final String ERR_TRAMITES_PENDIENTES_TRAMITAR_PARTE_1 = "errTramitesPendientesTramitar1";
    private final String ERR_TRAMITES_PENDIENTES_TRAMITAR_PARTE_2 = "errTramitesPendientesTramitar2";
    private final String ERR_TRAMITES_PENDIENTES_TRAMITAR_PARTE_3 = "errTramitesPendientesTramitar3";
    
    private final String ERR_TRAMITES_CLASIFICACION = "errTramitesClasificacion";
    
    protected static String dep_nom;
    protected static String dep_cod;

    protected static String tpr_cod;

    protected static String npr_ide;
    protected static String npr_cod;
    protected static String npr_des;

    protected static String uor_nom;
    protected static String uor_cod;
    protected static String uor_cod_vis;

    protected static String pro_mun;
    protected static String pro_cod;
    protected static String pro_flh;
    protected static String pro_fld;
    protected static String pro_are;
    protected static String pro_plz;
    protected static String pro_und;
    protected static String pro_sil;
    protected static String pro_utr;
    protected static String pro_tip;
    protected static String pro_ini;
    protected static String pro_est;
    protected static String pro_din;
    protected static String pro_tin;
    protected static String pro_loc;
    protected static String pro_fba;
    protected static String pro_tri;
    protected static String pro_des;

    protected static String tra_mun;
    protected static String tra_pro;
    protected static String tra_cod;
    protected static String tra_cou;
    protected static String tra_vis;
    protected static String tra_uin;
    protected static String tra_utr;
    protected static String tra_plz;
    protected static String tra_und;
    protected static String tra_are;
    protected static String tra_ocu;
    protected static String tra_cls;
    protected static String tra_fba;

    protected static String exp_mun;
    protected static String exp_pro;

    protected static String exr_mun;
    protected static String exr_pro;

    protected static String tdo_cod;

    protected static String dop_mun;
    protected static String dop_pro;
    protected static String dop_cod;
    protected static String dop_obl;
    protected static String dop_tdo;

    protected static String are_cod;

    protected static String utr_cod;

    protected static String org_cod;
    protected static String org_des;

    protected static String aae_org;
    protected static String eea_apl;
    protected static String eea_bde;

    protected static String pml_mun;
    protected static String pml_cod;
    protected static String pml_cmp;
    protected static String pml_leng;
    protected static String pml_valor;

    protected static String tml_mun;
    protected static String tml_pro;
    protected static String tml_tra;
    protected static String tml_cmp;
    protected static String tml_leng;
    protected static String tml_valor;

    protected static String dpml_mun;
    protected static String dpml_pro;
    protected static String dpml_dop;
    protected static String dpml_cmp;
    protected static String dpml_leng;
    protected static String dpml_valor;

    protected static String tpml_cod;
    protected static String tpml_cmp;
    protected static String tpml_leng;
    protected static String tpml_valor;

    protected static String tdml_cod;
    protected static String tdml_cmp;
    protected static String tdml_leng;
    protected static String tdml_valor;

    protected static String utml_cod;
    protected static String utml_cmp;
    protected static String utml_leng;
    protected static String utml_valor;

    protected static String aml_cod;
    protected static String aml_cmp;
    protected static String aml_leng;
    protected static String aml_valor;

    protected static String cls_cod;
    protected static String cls_ord;

    protected static String cml_cod;
    protected static String cml_cmp;
    protected static String cml_leng;
    protected static String cml_valor;

    protected static String pui_mun;
    protected static String pui_pro;
    protected static String pui_cod;

    protected static String doe_mun;
    protected static String doe_eje;
    protected static String doe_num;
    protected static String doe_pro;
    protected static String doe_cod;
    protected static String doe_fec;

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
  protected static String pca_desplegable;

    // TABLA E_TCA (Campos Suplementarios de Tramites
    protected static String tca_mun;
    protected static String tca_pro;
    protected static String tca_cod;
    protected static String tca_des;
    protected static String tca_tda;
    protected static String tca_activo;
    protected static String tca_tra;
    protected static String tca_plt;
    protected static String tca_desplegable;

    protected static String tda_cod;
    protected static String tda_des;
    protected static String tda_tab;

    protected static String plt_cod;
    protected static String plt_des;
    protected static String plt_url;

    protected static String enp_mun;
    protected static String enp_pro;
    protected static String enp_cod;
    protected static String enp_des;
    protected static String enp_url;
    protected static String enp_est;

    protected static String rtdp_tda;
    protected static String rtdp_plt;

    protected static String mas_cod;
    protected static String mas_des;

    protected static String rtdm_tda;
    protected static String rtdm_mas;

    protected static String rol_mun;
    protected static String rol_pro;
    protected static String rol_cod;
    protected static String rol_des;
    protected static String rol_pde;

    // Tablas E_DES y E_DES_VAL (Campos desplegables y sus valores).
    protected static String des_cod;
    protected static String des_nom;
    protected static String des_val_cod;


    /*************** CAMPOS PARA LOS TRÁMITES *****************/

    protected	static String tra_pre;
    protected	static String tra_ins;
    protected	static String tra_uti;
    protected	static String tra_utf;
    protected	static String tra_usi;
    protected	static String tra_usf;
    protected	static String tra_ini;
    protected	static String tra_inf;
    protected	static String tra_wst_cod;
    protected	static String tra_wst_ob;
    protected	static String tra_wsr_cod;
    protected	static String tra_wsr_ob;
    protected   static String tra_prr;
    protected   static String tra_car;

    protected	static String usu_cod;
    protected	static String usu_nom;

    protected	static String uou_uor;
    protected	static String uou_usu;

    protected	static String sal_mun;
    protected	static String sal_pro;
    protected	static String sal_tra;
    protected	static String sal_tac;
    protected	static String sal_taa;
    protected	static String sal_tan;
    protected	static String sal_obl;
    protected	static String sal_obld;
    
    protected	static String ent_mun;
    protected	static String ent_pro;
    protected	static String ent_tra;
    protected	static String ent_cod;
    protected	static String ent_ctr;
    protected	static String ent_est;
    protected	static String ent_tipo;
    protected	static String ent_exp;

    protected	static String dot_mun;
    protected	static String dot_pro;
    protected	static String dot_tra;
    protected	static String dot_cod;
    protected	static String dot_tdo;
    protected	static String dot_vis;
    protected	static String dot_frm;
    protected	static String dot_plt;
    protected	static String dot_activo;


    protected	static String fls_mun;
    protected	static String fls_pro;
    protected	static String fls_tra;
    protected	static String fls_nuc;
    protected	static String fls_nus;
    protected	static String fls_cts;


    protected	static String dtml_mun;
    protected	static String dtml_pro;
    protected	static String dtml_tra;
    protected	static String dtml_dot;
    protected	static String dtml_cmp;
    protected	static String dtml_leng;
    protected	static String dtml_valor;

    protected	static String sml_mun;
    protected	static String sml_tra;
    protected	static String sml_pro;
    protected	static String sml_cmp;
    protected	static String sml_leng;
    protected	static String sml_valor;
    
    protected	static String ten_mun;
    protected	static String ten_pro;
    protected	static String ten_tra;
    protected	static String ten_cod;
    protected	static String ten_des;
    protected	static String ten_url;
    protected	static String ten_est;
    
    protected	static String tca_tam;
    protected	static String tca_mas;
    protected	static String tca_obl;
    protected	static String tca_nor;
    protected	static String tca_rot;
    protected	static String tca_vis;

    protected static String aplt_cod;
    protected static String aplt_des;
    protected static String aplt_apl;
    protected static String aplt_doc;
    protected static String aplt_pro;
    protected static String aplt_tra;
    protected static String aplt_int;
    protected static String aplt_rel;

    protected static String car_cod_vis;
    protected static String car_cod;

    protected static String idiomaDefecto;

    /**********************************************************/

    private ImportacionProcedimientoDAO(){
         // Queremos usar el fichero de configuracion techserver
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
        // Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        dep_nom = m_ConfigTechnical.getString("SQL.A_DEP.nombre");
        dep_cod = m_ConfigTechnical.getString("SQL.A_DEP.codigo");

        tpr_cod = m_ConfigTechnical.getString("SQL.A_TPR.codProcedimiento");

        uor_nom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
        uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        uor_cod_vis = m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible");

        npr_ide = m_ConfigTechnical.getString("SQL.E_NPR.identificador");
        npr_cod = m_ConfigTechnical.getString("SQL.E_NPR.codDepartamento");
        npr_des = m_ConfigTechnical.getString("SQL.E_NPR.descDepartamento");

        pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");
        pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
        pro_tri = m_ConfigTechnical.getString("SQL.E_PRO.codTramiteInicio");
        pro_flh = m_ConfigTechnical.getString("SQL.E_PRO.fechaLimiteHasta");
        pro_fld = m_ConfigTechnical.getString("SQL.E_PRO.fechaLimiteDesde");
        pro_are = m_ConfigTechnical.getString("SQL.E_PRO.area");
        pro_plz = m_ConfigTechnical.getString("SQL.E_PRO.plazoMax");
        pro_und = m_ConfigTechnical.getString("SQL.E_PRO.unidadNotif");
        pro_sil = m_ConfigTechnical.getString("SQL.E_PRO.tipoSilencio");
        pro_utr = m_ConfigTechnical.getString("SQL.E_PRO.unidad");
        pro_tip = m_ConfigTechnical.getString("SQL.E_PRO.tipoProcedimiento");
        pro_ini = m_ConfigTechnical.getString("SQL.E_PRO.tipoInicio");
        pro_est = m_ConfigTechnical.getString("SQL.E_PRO.estadoProcedimiento");
        pro_din = m_ConfigTechnical.getString("SQL.E_PRO.disponible");
        pro_tin= m_ConfigTechnical.getString("SQL.E_PRO.tramitacionInternet");
        pro_loc= m_ConfigTechnical.getString("SQL.E_PRO.poseeLocalizacion");
        pro_fba= m_ConfigTechnical.getString("SQL.E_PRO.fechaBaja");
        pro_tri= m_ConfigTechnical.getString("SQL.E_PRO.codTramiteInicio");
        pro_des = m_ConfigTechnical.getString("SQL.E_PRO.descripcionBreve");

        exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");

        exr_mun = m_ConfigTechnical.getString("SQL.E_EXR.codMunicipio");
        exr_pro = m_ConfigTechnical.getString("SQL.E_EXR.codProcedimiento");

        tdo_cod = m_ConfigTechnical.getString("SQL.E_TDO.codDocumento");

        dop_mun = m_ConfigTechnical.getString("SQL.E_DOP.codMunicipio");
        dop_pro = m_ConfigTechnical.getString("SQL.E_DOP.codProcedimiento");
        dop_cod = m_ConfigTechnical.getString("SQL.E_DOP.codDocumento");
        dop_obl = m_ConfigTechnical.getString("SQL.E_DOP.obligatorio");
        dop_tdo = m_ConfigTechnical.getString("SQL.E_DOP.tipoDocumento");

        are_cod = m_ConfigTechnical.getString("SQL.A_ARE.codArea");

        utr_cod = m_ConfigTechnical.getString("SQL.A_UTR.codUnidadTramitadora");

        org_cod = m_ConfigTechnical.getString("SQL.A_ORG.codigo");
        org_des = m_ConfigTechnical.getString("SQL.A_ORG.descripcion");

        aae_org = m_ConfigTechnical.getString("SQL.A_EEA.organizacion");
        eea_apl = m_ConfigTechnical.getString("SQL.A_EEA.aplicacion");
        eea_bde = m_ConfigTechnical.getString("SQL.A_EEA.jndi");

        pml_mun = m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");
        pml_cod = m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
        pml_cmp = m_ConfigTechnical.getString("SQL.E_PML.codCampoML");
        pml_leng = m_ConfigTechnical.getString("SQL.E_PML.idioma");
        pml_valor = m_ConfigTechnical.getString("SQL.E_PML.valor");

        tml_mun = m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
        tml_pro = m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
        tml_tra = m_ConfigTechnical.getString("SQL.E_TML.codTramite");
        tml_cmp = m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
        tml_leng = m_ConfigTechnical.getString("SQL.E_TML.idioma");
        tml_valor = m_ConfigTechnical.getString("SQL.E_TML.valor");

        dpml_mun = m_ConfigTechnical.getString("SQL.E_DPML.codMunicipio");
        dpml_pro = m_ConfigTechnical.getString("SQL.E_DPML.codProcedimiento");
        dpml_dop = m_ConfigTechnical.getString("SQL.E_DPML.codDocumento");
        dpml_cmp = m_ConfigTechnical.getString("SQL.E_DPML.codCampoML");
        dpml_leng = m_ConfigTechnical.getString("SQL.E_DPML.idioma");
        dpml_valor = m_ConfigTechnical.getString("SQL.E_DPML.valor");

        tpml_cod = m_ConfigTechnical.getString("SQL.A_TPML.codTipoProcedimiento");
        tpml_cmp = m_ConfigTechnical.getString("SQL.A_TPML.codCampoML");
        tpml_leng = m_ConfigTechnical.getString("SQL.A_TPML.idioma");
        tpml_valor = m_ConfigTechnical.getString("SQL.A_TPML.valor");

        tdml_cod = m_ConfigTechnical.getString("SQL.E_TDML.codTipoDocumento");
        tdml_cmp = m_ConfigTechnical.getString("SQL.E_TDML.codCampoML");
        tdml_leng = m_ConfigTechnical.getString("SQL.E_TDML.idioma");
        tdml_valor = m_ConfigTechnical.getString("SQL.E_TDML.valor");

        utml_cod = m_ConfigTechnical.getString("SQL.A_UTML.codUnidadTramitadora");
        utml_cmp = m_ConfigTechnical.getString("SQL.A_UTML.codCampoML");
        utml_leng = m_ConfigTechnical.getString("SQL.A_UTML.idioma");
        utml_valor = m_ConfigTechnical.getString("SQL.A_UTML.valor");

        aml_cod = m_ConfigTechnical.getString("SQL.A_AML.codArea");
        aml_cmp = m_ConfigTechnical.getString("SQL.A_AML.codCampoML");
        aml_leng = m_ConfigTechnical.getString("SQL.A_AML.idioma");
        aml_valor = m_ConfigTechnical.getString("SQL.A_AML.valor");

        cls_cod = m_ConfigTechnical.getString("SQL.A_CLS.codTramite");
        cls_ord = m_ConfigTechnical.getString("SQL.A_CLS.orden");

        cml_cod = m_ConfigTechnical.getString("SQL.A_CML.codTramite");
        cml_cmp = m_ConfigTechnical.getString("SQL.A_CML.codCampoML");
        cml_leng = m_ConfigTechnical.getString("SQL.A_CML.idioma");
        cml_valor = m_ConfigTechnical.getString("SQL.A_CML.valor");

        pui_mun = m_ConfigTechnical.getString("SQL.E_PUI.codMunicipio");
        pui_pro = m_ConfigTechnical.getString("SQL.E_PUI.codProcedimiento");
        pui_cod = m_ConfigTechnical.getString("SQL.E_PUI.codUnidadInicio");

        doe_mun = m_ConfigTechnical.getString("SQL.E_DOE.codMunicipio");
        doe_eje = m_ConfigTechnical.getString("SQL.E_DOE.ejercicio");
        doe_num = m_ConfigTechnical.getString("SQL.E_DOE.numero");
        doe_pro = m_ConfigTechnical.getString("SQL.E_DOE.codProcedimiento");
        doe_cod = m_ConfigTechnical.getString("SQL.E_DOE.codDocumento");
        doe_fec = m_ConfigTechnical.getString("SQL.E_DOE.fechaEntrega");

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
        pca_desplegable = m_ConfigTechnical.getString("SQL.E_PCA.desplegable");

      
        tda_cod = m_ConfigTechnical.getString("SQL.E_TDA.codDato");
        tda_des = m_ConfigTechnical.getString("SQL.E_TDA.descripcion");
        tda_tab = m_ConfigTechnical.getString("SQL.E_TDA.tabla");

        plt_cod = m_ConfigTechnical.getString("SQL.E_PLT.codPlantilla");
        plt_des = m_ConfigTechnical.getString("SQL.E_PLT.descripcion");
        plt_url = m_ConfigTechnical.getString("SQL.E_PLT.url");

        enp_mun = m_ConfigTechnical.getString("SQL.E_ENP.codMunicipio");
        enp_pro = m_ConfigTechnical.getString("SQL.E_ENP.codProcedimiento");
        enp_cod = m_ConfigTechnical.getString("SQL.E_ENP.codEnlace");
        enp_des = m_ConfigTechnical.getString("SQL.E_ENP.descripcion");
        enp_url = m_ConfigTechnical.getString("SQL.E_ENP.url");
        enp_est = m_ConfigTechnical.getString("SQL.E_ENP.estado");

        rtdp_tda = m_ConfigTechnical.getString("SQL.E_RTDP.codTipoDato");
        rtdp_plt = m_ConfigTechnical.getString("SQL.E_RTDP.codPlantilla");

        mas_cod = m_ConfigTechnical.getString("SQL.E_MAS.codMascara");
        mas_des = m_ConfigTechnical.getString("SQL.E_MAS.descMascara");

        rtdm_tda = m_ConfigTechnical.getString("SQL.E_RTDM.codTipoDato");
        rtdm_mas = m_ConfigTechnical.getString("SQL.E_RTDM.codMascara");

        rol_mun = m_ConfigTechnical.getString("SQL.E_ROL.codMunicipio");
        rol_pro = m_ConfigTechnical.getString("SQL.E_ROL.codProcedimiento");
        rol_cod = m_ConfigTechnical.getString("SQL.E_ROL.codRol");
        rol_des = m_ConfigTechnical.getString("SQL.E_ROL.descripcion");
        rol_pde = m_ConfigTechnical.getString("SQL.E_ROL.porDefecto");

        // Tablas E_DES y E_DES_VAL (Campos Desplegables):
        des_cod = m_ConfigTechnical.getString("SQL.E_DES.codigo");
        des_nom = m_ConfigTechnical.getString("SQL.E_DES.nombre");
        des_val_cod = m_ConfigTechnical.getString("SQL.E_DES_VAL.codigoValor");



        /***** CAMPOS PARA TRÁMITES ******/
        tra_mun	= m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
        tra_pro	= m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
        tra_cod	= m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
        tra_cou	= m_ConfigTechnical.getString("SQL.E_TRA.codTramiteUsuario");
        tra_vis	= m_ConfigTechnical.getString("SQL.E_TRA.visibleInternet");
        tra_uin	= m_ConfigTechnical.getString("SQL.E_TRA.unidadInicio");
        tra_utr	= m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");
        tra_plz	= m_ConfigTechnical.getString("SQL.E_TRA.plazo");
        tra_und	= m_ConfigTechnical.getString("SQL.E_TRA.unidadesPlazo");
        tra_are	= m_ConfigTechnical.getString("SQL.E_TRA.codArea");
        tra_ocu	= m_ConfigTechnical.getString("SQL.E_TRA.ocurrencias");
        tra_cls	= m_ConfigTechnical.getString("SQL.E_TRA.clasificacion");
        tra_fba	= m_ConfigTechnical.getString("SQL.E_TRA.fechaBaja");
        tra_pre	= m_ConfigTechnical.getString("SQL.E_TRA.tramitePregunta");
        tra_ins	= m_ConfigTechnical.getString("SQL.E_TRA.instrucciones");
        tra_uti	= m_ConfigTechnical.getString("SQL.E_TRA.notUnidadTramitIni");
        tra_utf	= m_ConfigTechnical.getString("SQL.E_TRA.notUnidadTramitFin");
        tra_usi	= m_ConfigTechnical.getString("SQL.E_TRA.notUsuUnidadTramitIni");
        tra_usf = m_ConfigTechnical.getString("SQL.E_TRA.notUsuUnidadTramitFin");
        tra_ini	= m_ConfigTechnical.getString("SQL.E_TRA.notInteresadosIni");
        tra_inf	= m_ConfigTechnical.getString("SQL.E_TRA.notInteresadosFin");
        tra_wst_cod	= m_ConfigTechnical.getString("SQL.E_TRA.wsTramitar");
        tra_wst_ob = m_ConfigTechnical.getString("SQL.E_TRA.wsTramitarTipo");
        tra_wsr_cod	= m_ConfigTechnical.getString("SQL.E_TRA.wsRetroceder");
        tra_wsr_ob	= m_ConfigTechnical.getString("SQL.E_TRA.wsRetrocederTipo");
        tra_prr	= m_ConfigTechnical.getString("SQL.E_TRA.expRel");
        tra_car	= m_ConfigTechnical.getString("SQL.E_TRA.cargo");

        usu_cod	= m_ConfigTechnical.getString("SQL.A_USU.codigo");
        usu_nom	= m_ConfigTechnical.getString("SQL.A_USU.nombre");

        uou_uor	= m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        uou_usu	= m_ConfigTechnical.getString("SQL.A_UOU.usuario");

        sal_mun	= m_ConfigTechnical.getString("SQL.E_SAL.codMunicipio");
        sal_pro	= m_ConfigTechnical.getString("SQL.E_SAL.codProcedimiento");
        sal_tra	= m_ConfigTechnical.getString("SQL.E_SAL.codTramite");
        sal_tac	= m_ConfigTechnical.getString("SQL.E_SAL.tipoAccion");
        sal_taa	= m_ConfigTechnical.getString("SQL.E_SAL.tipoAccionAfirm");
        sal_tan	= m_ConfigTechnical.getString("SQL.E_SAL.tipoAccionNeg");
        sal_obl	= m_ConfigTechnical.getString("SQL.E_SAL.tramitesObligatorios");
        sal_obld 	= m_ConfigTechnical.getString("SQL.E_SAL.tramitesObligatoriosD");

        utr_cod	= m_ConfigTechnical.getString("SQL.A_UTR.codUnidadTramitadora");

        cls_cod	= m_ConfigTechnical.getString("SQL.A_CLS.codTramite");

        ent_mun	= m_ConfigTechnical.getString("SQL.E_ENT.codMunicipio");
        ent_pro	= m_ConfigTechnical.getString("SQL.E_ENT.codProcedimiento");
        ent_tra	= m_ConfigTechnical.getString("SQL.E_ENT.codTramite");
        ent_cod	= m_ConfigTechnical.getString("SQL.E_ENT.codCondicion");
        ent_ctr	= m_ConfigTechnical.getString("SQL.E_ENT.codTramiteCond");
        ent_est	= m_ConfigTechnical.getString("SQL.E_ENT.estadoTramiteCond");
        ent_tipo = m_ConfigTechnical.getString("SQL.E_ENT.tipoCond");
        ent_exp = m_ConfigTechnical.getString("SQL.E_ENT.expresionCond");

        dot_mun	= m_ConfigTechnical.getString("SQL.E_DOT.codMunicipio");
        dot_pro	= m_ConfigTechnical.getString("SQL.E_DOT.codProcedimiento");
        dot_tra	= m_ConfigTechnical.getString("SQL.E_DOT.codTramite");
        dot_cod	= m_ConfigTechnical.getString("SQL.E_DOT.codDocumento");
        dot_tdo	= m_ConfigTechnical.getString("SQL.E_DOT.codTipoDocumento");
        dot_vis	= m_ConfigTechnical.getString("SQL.E_DOT.visibleInternet");
        dot_frm	= m_ConfigTechnical.getString("SQL.E_DOT.firma");
        dot_plt	= m_ConfigTechnical.getString("SQL.E_DOT.codPlantilla");
        dot_activo	= m_ConfigTechnical.getString("SQL.E_DOT.activo");


        fls_mun	= m_ConfigTechnical.getString("SQL.E_FLS.codMunicipio");
        fls_pro	= m_ConfigTechnical.getString("SQL.E_FLS.codProcedimiento");
        fls_tra	= m_ConfigTechnical.getString("SQL.E_FLS.codTramite");
        fls_nuc	= m_ConfigTechnical.getString("SQL.E_FLS.numeroCondicion");
        fls_nus	= m_ConfigTechnical.getString("SQL.E_FLS.numeroSecuencia");
        fls_cts	= m_ConfigTechnical.getString("SQL.E_FLS.codTramiteSiguiente");

        cml_cod	= m_ConfigTechnical.getString("SQL.A_CML.codTramite");
        cml_cmp	= m_ConfigTechnical.getString("SQL.A_CML.codCampoML");
        cml_leng 	= m_ConfigTechnical.getString("SQL.A_CML.idioma");
        cml_valor 	= m_ConfigTechnical.getString("SQL.A_CML.valor");

        tml_mun	= m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
        tml_pro	= m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
        tml_tra	= m_ConfigTechnical.getString("SQL.E_TML.codTramite");
        tml_cmp	= m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
        tml_leng 	= m_ConfigTechnical.getString("SQL.E_TML.idioma");
        tml_valor 	= m_ConfigTechnical.getString("SQL.E_TML.valor");

        utml_cod 	= m_ConfigTechnical.getString("SQL.A_UTML.codUnidadTramitadora");
        utml_cmp 	= m_ConfigTechnical.getString("SQL.A_UTML.codCampoML");
        utml_leng 	= m_ConfigTechnical.getString("SQL.A_UTML.idioma");
        utml_valor 	= m_ConfigTechnical.getString("SQL.A_UTML.valor");

        sml_mun	= m_ConfigTechnical.getString("SQL.E_SML.codMunicipio");
        sml_tra	= m_ConfigTechnical.getString("SQL.E_SML.codTramite");
        sml_pro	= m_ConfigTechnical.getString("SQL.E_SML.codProcedimiento");
        sml_cmp	= m_ConfigTechnical.getString("SQL.E_SML.codCampoML");
        sml_leng 	= m_ConfigTechnical.getString("SQL.E_SML.idioma");
        sml_valor 	= m_ConfigTechnical.getString("SQL.E_SML.valor");

        aml_cod	= m_ConfigTechnical.getString("SQL.A_AML.codArea");
        aml_cmp	= m_ConfigTechnical.getString("SQL.A_AML.codCampoML");
        aml_leng 	= m_ConfigTechnical.getString("SQL.A_AML.idioma");
        aml_valor 	= m_ConfigTechnical.getString("SQL.A_AML.valor");

        pml_mun	= m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");
        pml_cod	= m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
        pml_cmp	= m_ConfigTechnical.getString("SQL.E_PML.codCampoML");
        pml_leng	= m_ConfigTechnical.getString("SQL.E_PML.idioma");
        pml_valor 	= m_ConfigTechnical.getString("SQL.E_PML.valor");

        pui_mun	= m_ConfigTechnical.getString("SQL.E_PUI.codMunicipio");
        pui_pro	= m_ConfigTechnical.getString("SQL.E_PUI.codProcedimiento");
        pui_cod	= m_ConfigTechnical.getString("SQL.E_PUI.codUnidadInicio");

        ten_mun	= m_ConfigTechnical.getString("SQL.E_TEN.codMunicipio");
        ten_pro	= m_ConfigTechnical.getString("SQL.E_TEN.codProcedimiento");
        ten_tra	= m_ConfigTechnical.getString("SQL.E_TEN.codTramite");
        ten_cod	= m_ConfigTechnical.getString("SQL.E_TEN.codEnlace");
        ten_des	= m_ConfigTechnical.getString("SQL.E_TEN.descripcion");
        ten_url	= m_ConfigTechnical.getString("SQL.E_TEN.url");
        ten_est	= m_ConfigTechnical.getString("SQL.E_TEN.estado");

        // Tabla E_TCA (Campos suplementarios de tramite)
        tca_mun	= m_ConfigTechnical.getString("SQL.E_TCA.codMunicipio");
        tca_pro	= m_ConfigTechnical.getString("SQL.E_TCA.codProcedimiento");
        tca_tra	= m_ConfigTechnical.getString("SQL.E_TCA.codTramite");
        tca_cod	= m_ConfigTechnical.getString("SQL.E_TCA.codCampo");
        tca_des	= m_ConfigTechnical.getString("SQL.E_TCA.descripcion");
        tca_plt	= m_ConfigTechnical.getString("SQL.E_TCA.codPlantilla");
        tca_tda	= m_ConfigTechnical.getString("SQL.E_TCA.codTipoDato");
        tca_tam	= m_ConfigTechnical.getString("SQL.E_TCA.tamano");
        tca_mas	= m_ConfigTechnical.getString("SQL.E_TCA.mascara");
        tca_obl	= m_ConfigTechnical.getString("SQL.E_TCA.obligatorio");
        tca_nor	= m_ConfigTechnical.getString("SQL.E_TCA.numeroOrden");
        tca_rot	= m_ConfigTechnical.getString("SQL.E_TCA.rotulo");
        tca_vis	= m_ConfigTechnical.getString("SQL.E_TCA.visible");
        tca_activo = m_ConfigTechnical.getString("SQL.E_TCA.activo");
        tca_desplegable = m_ConfigTechnical.getString("SQL.E_TCA.desplegable");

        tda_cod = m_ConfigTechnical.getString("SQL.E_TDA.codDato");
        tda_des = m_ConfigTechnical.getString("SQL.E_TDA.descripcion");
        tda_tab = m_ConfigTechnical.getString("SQL.E_TDA.tabla");

        plt_cod = m_ConfigTechnical.getString("SQL.E_PLT.codPlantilla");
        plt_des = m_ConfigTechnical.getString("SQL.E_PLT.descripcion");
        plt_url = m_ConfigTechnical.getString("SQL.E_PLT.url");

        aplt_cod = m_ConfigTechnical.getString("SQL.A_PLT.codigo");
        aplt_des = m_ConfigTechnical.getString("SQL.A_PLT.descripcion");
        aplt_apl = m_ConfigTechnical.getString("SQL.A_PLT.codigoApli");
        aplt_doc = m_ConfigTechnical.getString("SQL.A_PLT.doc");
        aplt_pro = m_ConfigTechnical.getString("SQL.A_PLT.procedimiento");
        aplt_tra = m_ConfigTechnical.getString("SQL.A_PLT.tramite");
        aplt_int = m_ConfigTechnical.getString("SQL.A_PLT.interesado");
        aplt_rel = m_ConfigTechnical.getString("SQL.A_PLT.relacion");

        car_cod_vis = m_ConfigTechnical.getString("SQL.A_CAR.codigoVisible");
        car_cod = m_ConfigTechnical.getString("SQL.A_CAR.codigo");
        /***** CAMPOS PARA TRÁMITES *****/


        idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
    }

    public static ImportacionProcedimientoDAO getInstance(){
        if(instance==null)
            instance = new ImportacionProcedimientoDAO();

        return instance;
    }

    
    /**
     * Devuelve el valor siguiente para el campo ID_FIRMA de la tabla E_DOC_FIRMAS
     * @param con: Conexión a la BBDD
     * @return Un int
     */
    private int getNextIdFirma(Connection con){
        int idFirma = 0;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            log.debug("getNextIdFirma ======>");
            String sql  ="SELECT COUNT(*) AS NUM FROM E_DEF_FIRMA";
            log.debug(sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            int num = 0;
            while(rs.next()){                
                num = rs.getInt("NUM");
            }
            
            st.close();
            rs.close();
            
            if(num==0){
                log.debug("No hay elementos en e_def_firma <<>> idFirma = 1");
                idFirma = 1;                
            }else{
                
                sql = "SELECT MAX(ID_FIRMA) AS NUM FROM E_DEF_FIRMA";
                st = con.createStatement();
                rs = st.executeQuery(sql);                
                while(rs.next()){
                    idFirma = rs.getInt("NUM") + 1;
                }// while                
                
            }// else            
            
        }catch(SQLException e){
            e.printStackTrace();
            idFirma = -1;
            
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();      
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return idFirma;
    }
    

    public boolean insertarProcedimiento(DefinicionProcedimientosValueObject defProcVO,Connection conexion,String[] params)
            throws TechnicalException{
        boolean exito = false;
        String sql= "";
        String sql1 = "";
        String sqlDoc = "";
        String yaExiste = "no";
        int res = 0;
        int res1 = 0;
        int res3 = 0;
        int resEnlaces = 0;
        int resRoles = 0;

        Vector codDepartV = new Vector();
        Vector codV = new Vector();
        ResultSet rs = null;
        Statement st = null;
        PreparedStatement ps =  null;

        if(log.isDebugEnabled()) log.debug("Entra en el insert del DAO");
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            st = conexion.createStatement();

            sql1 = "SELECT " + pro_mun + "," + pro_cod + " FROM E_PRO";
            if(log.isDebugEnabled()) log.debug(sql1);

            rs = st.executeQuery(sql1);

            while ( rs.next() ) {
                String codMunic = rs.getString(pro_mun);
                codDepartV.addElement(codMunic);
                String cod = rs.getString(pro_cod);
                codV.addElement(cod);
            }

            for(int i=0;i<codDepartV.size();i++) {
                if(defProcVO.getCodMunicipio().equals(codDepartV.elementAt(i)) &&
                        defProcVO.getTxtCodigo().equals(codV.elementAt(i))) {
                    yaExiste = "si";
                }
            }
            rs.close();

            // PESTAÑA DATOS

            if("no".equals(yaExiste)) {
                // TODO: Ver que se hace con el código del usuario al que se dirige la notificación de un trámite porque al importar puede no existir
                // en el entorno al que se importe.

                sql = "INSERT INTO E_PRO (" + pro_mun + "," + pro_cod + "," + pro_flh + "," + pro_fld +
                        "," + pro_are + "," + pro_plz + "," + pro_und + "," + pro_sil + "," +
                        pro_tip + "," + pro_ini + "," + pro_est + "," + pro_din + "," + pro_tin + "," + pro_loc + "," +
                        pro_tri + ", " + pro_des + ",PRO_PORCENTAJE,PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS,PRO_INTOBL,PRO_RESTRINGIDO,PRO_LIBRERIA,PRO_EXPNUMANOT,PRO_SOLOWS"  + ") VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                        defProcVO.getTxtCodigo();
                if(defProcVO.getFechaLimiteHasta() == null || defProcVO.getFechaLimiteHasta().equals(""))
                    sql += "',null,";
                else
                    sql += "'," + oad.convertir("'" + defProcVO.getFechaLimiteHasta() + "'",
                            AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ",";
                    sql += oad.convertir("'" + defProcVO.getFechaLimiteDesde() + "'",
                            AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "," +
                        defProcVO.getCodArea() + ",";
                if(defProcVO.getPlazo() == null || defProcVO.getPlazo().equals(""))
                    sql += "null," + "null,";
                else
                    sql += defProcVO.getPlazo() + "," + "'" + defProcVO.getTipoPlazo() + "',";


                sql += "?,?,?,?,?,?,?,";                        

                if(defProcVO.getTramiteInicio() == null || defProcVO.getTramiteInicio().equals(""))
                        sql += "null, ";
                else
                    sql += defProcVO.getTramiteInicio() + ", ";
          // Campo de Bescripcion Breve.
          if (defProcVO.getDescripcionBreve() == null || defProcVO.getDescripcionBreve().equals("")) {
              sql += "null,";
          } else {
                    sql += "'" + defProcVO.getDescripcionBreve().trim() + "',";
          }

          
          /**** ORIGINAL 
          if(defProcVO.getPorcentaje()!=null && defProcVO.getPorcentaje().length()>0)
                sql += "'" + defProcVO.getPorcentaje() + "')";
          else sql += "null)";
          *****/
                 
          
          
          /*** PLUGIN DE FINALIZACIÓN NO CONVENCIONAL DE EXPEDIENTE ***/
          //PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS
          
          if(defProcVO.getPorcentaje()!=null && defProcVO.getPorcentaje().length()>0)
                sql += "'" + defProcVO.getPorcentaje() + "',";
          else sql += "null,";
          
          if(StringOperations.stringNoNuloNoVacio(defProcVO.getCodServicioFinalizacion()) && StringOperations.stringNoNuloNoVacio(defProcVO.getImplClassServicioFinalizacion())){
                sql += "'" + defProcVO.getCodServicioFinalizacion() + "','" + defProcVO.getImplClassServicioFinalizacion() + "',";              
          }else
                sql += "null,null,";
          
          if((defProcVO.getInteresadoOblig() != null)&&(!"".equals(defProcVO.getInteresadoOblig())))
              sql += defProcVO.getInteresadoOblig() + ",";
          else
              sql += "0,";
          
          sql += Integer.parseInt(defProcVO.getRestringido())+","+ Integer.parseInt(defProcVO.getBiblioteca())+","+ defProcVO.getNumeracionExpedientesAnoAsiento()+","+Integer.parseInt(defProcVO.getSoloWS())+")";
          
          
          /*** PLUGIN DE FINALIZACIÓN NO CONVENCIONAL DE EXPEDIENTE ***/
          
          

                if(log.isDebugEnabled()) log.debug(sql);

                ps = conexion.prepareStatement(sql);
                int i=1;

                if(defProcVO.getTipoSilencio() !=null && !"".equals(defProcVO.getTipoSilencio())){
                    ps.setInt(i++,Integer.parseInt(defProcVO.getTipoSilencio()));
                }else
                    ps.setNull(i++,java.sql.Types.INTEGER);

                if(defProcVO.getCodTipoProcedimiento() !=null && !"".equals(defProcVO.getCodTipoProcedimiento())){
                    ps.setInt(i++,Integer.parseInt(defProcVO.getCodTipoProcedimiento()));
                }else
                    ps.setNull(i++,java.sql.Types.INTEGER);

                if(defProcVO.getCodTipoInicio()!=null && !"".equals(defProcVO.getCodTipoInicio())){
                    ps.setInt(i++,Integer.parseInt(defProcVO.getCodTipoInicio()));
                }else
                    ps.setNull(i++,java.sql.Types.INTEGER);

                if(defProcVO.getCodEstado() !=null && !"".equals(defProcVO.getCodEstado())){
                    ps.setInt(i++,Integer.parseInt(defProcVO.getCodEstado()));
                }else
                    ps.setNull(i++,java.sql.Types.INTEGER);

                if(defProcVO.getDisponible() !=null && !"".equals(defProcVO.getDisponible())){
                    ps.setInt(i++,Integer.parseInt(defProcVO.getDisponible()));
                }else
                    ps.setNull(i++,java.sql.Types.INTEGER);

                if(defProcVO.getTramitacionInternet() !=null && !"".equals(defProcVO.getTramitacionInternet())){
                    ps.setInt(i++,Integer.parseInt(defProcVO.getTramitacionInternet()));
                }else
                    ps.setNull(i++,java.sql.Types.INTEGER);

                if(defProcVO.getLocalizacion() !=null && !"".equals(defProcVO.getLocalizacion())){
                    ps.setInt(i++,Integer.parseInt(defProcVO.getLocalizacion()));
                }else
                    ps.setNull(i++,java.sql.Types.INTEGER);

                res = ps.executeUpdate();
                
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_PRO son :::::::::::::: : " + res);

                sql = "INSERT INTO E_PML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
            defProcVO.getTxtCodigo() + "','NOM','" + idiomaDefecto + "','" + defProcVO.getTxtDescripcion().trim() + "')";
                if(log.isDebugEnabled()) log.debug(sql);

        res1 = st.executeUpdate(sql);
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_PML son :::::::::::::: : " + res1);

                Vector listaCodUnidadInicio = defProcVO.getListaCodUnidadInicio();
                if(listaCodUnidadInicio != null) {
                    for(i=0;i<listaCodUnidadInicio.size();i++) {
                        String codigoUnidadInicio = this.recuperaCodigoUnidadInicio(listaCodUnidadInicio.elementAt(i).toString(), conexion);
                        sql = "INSERT INTO E_PUI (" + pui_mun + "," + pui_pro + "," + pui_cod + ") VALUES (" +
                                defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() + "'," +
                                codigoUnidadInicio + ")";
                        if(log.isDebugEnabled()) log.debug(sql);

            res3 += st.executeUpdate(sql);
                    }
                    if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_PUI son res3:::::::::::::: : " + res3);
                }

                // PESTAÑA DE DOCUMENTOS

                Vector listaNombresDoc = defProcVO.getListaNombresDoc();
                Vector listaCondicionDoc = defProcVO.getListaCondicionDoc();
                Vector listaCodigosDoc = defProcVO.getListaCodigosDoc();
                for(int j=0;j<listaNombresDoc.size();j++) {

                    sqlDoc = "INSERT INTO E_DOP (" + dop_mun + "," + dop_pro + "," + dop_cod +
                            ") VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ")";
                    if(log.isDebugEnabled()) log.debug(sqlDoc);
          st.executeUpdate(sqlDoc);

          
                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'NOM','1','" +
                            listaNombresDoc.elementAt(j) + "')";
           
                    if(log.isDebugEnabled()) log.debug(sqlDoc);
          st.executeUpdate(sqlDoc);

                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'CON','1','" +
                            listaCondicionDoc.elementAt(j) + "')";

            
                    if(log.isDebugEnabled()) log.debug(sqlDoc);
          st.executeUpdate(sqlDoc);
                }

                //CIRCUITO DE FIRMAS DE DOCUMENTOS DE PROCEDIMIENTO
                ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentoProcedimiento = defProcVO.getFirmasDocumentosProcedimiento();
                 for(int j=0;j<firmasDocumentoProcedimiento.size();j++) {

                     FirmasDocumentoProcedimientoVO firmaVO=firmasDocumentoProcedimiento.get(j);
                     
                     /*** RECUPERA EL ID DE FIRMA **/
                     int maxIdFirma = this.getNextIdFirma(conexion);                     
                     //String sqlFir = "INSERT INTO E_DEF_FIRMA VALUES((SELECT MAX(ID_FIRMA+1) FROM E_DEF_FIRMA)," + firmaVO.getUsuario() +","+firmaVO.getOrden()+","+firmaVO.getCodDocumento()+ "," +defProcVO.getCodMunicipio()+ ",'" +
                     
                     String sqlFir = "INSERT INTO E_DEF_FIRMA(ID_FIRMA, FIRMA_USUARIO, FIRMA_ORDEN, FIRMA_COD_DOC, FIRMA_MUN, FIRMA_PROC, FIRMA_UOR, FIRMA_CARGO, FIRMA_FIN_REC, FIRMA_TRA_SUB)"+
                             " VALUES("  + maxIdFirma + ", ?,"+firmaVO.getOrden()+","+firmaVO.getCodDocumento()+ "," +defProcVO.getCodMunicipio()+ ",'" +
                     defProcVO.getTxtCodigo() + "','" + firmaVO.getUor()+"', ?, ?, ?)";

                     ps = conexion.prepareStatement(sqlFir);
                     if (firmaVO.getUsuario() == null || "".equals(firmaVO.getUsuario()))
                        ps.setNull(1,java.sql.Types.INTEGER);
                     else
                        ps.setInt(1, Integer.parseInt(firmaVO.getUsuario()));
                     
                     if (firmaVO.getCargo() == null || "".equals(firmaVO.getCargo()))
                        ps.setNull(2,java.sql.Types.INTEGER);
                     else
                        ps.setInt(2, Integer.parseInt(firmaVO.getCargo()));
                     
                     ps.setString(3,(firmaVO.getFinalizaRechazo()==null || "".equals(firmaVO.getFinalizaRechazo()))?"0":firmaVO.getFinalizaRechazo());
                     ps.setString(4, (firmaVO.getTramitar()==null || "".equals(firmaVO.getTramitar()))?"0":firmaVO.getTramitar());
                     log.debug("sqlFir: " + sqlFir);                      
                     res = ps.executeUpdate();
                     ps.close();
                 }

                // PESTAÑA DE CAMPOS

                Vector listaCodCampos = defProcVO.getListaCodCampos();
                Vector listaDescCampos = defProcVO.getListaDescCampos();
                Vector listaCodPlantilla = defProcVO.getListaCodPlantilla();
                Vector listaCodTipoDato = defProcVO.getListaCodTipoDato();
                Vector listaTamano = defProcVO.getListaTamano();
                Vector listaMascara = defProcVO.getListaMascara();
                Vector listaObligatorio = defProcVO.getListaObligatorio();
                Vector listaRotulo = defProcVO.getListaRotulo();
                Vector listaActivos = defProcVO.getListaActivos();
                Vector listaOcultos = defProcVO.getListaOcultos();
                Vector listaBloqueados = defProcVO.getListaBloqueados();
                Vector listaPlazoFecha = defProcVO.getListaPlazoFecha();
                Vector listaCheckPlazoFecha = defProcVO.getListaCheckPlazoFecha();
                Vector listaValidacion = defProcVO.getListaValidacion();
                Vector listaOperacion = defProcVO.getListaOperacion();
                Vector listaAgrupacionCampo = defProcVO.getListaAgrupacionesCampo();
                Vector listaPosicionesX = defProcVO.getListaPosicionesX();
                Vector listaPosicionesY = defProcVO.getListaPosicionesY();
                String plantilla="", desplegable="";
                String valida = "";
                
                log.debug("CAMPOS DEL PROCEDIMIENTO="+listaCodCampos.size());
                for(int j=0;j<listaCodCampos.size();j++) {
                    if ("SI".equalsIgnoreCase((String)listaActivos.elementAt(j))) {
                        if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                            desplegable = (String)listaCodPlantilla.elementAt(j);
                        } else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                            desplegable = (String)listaCodPlantilla.elementAt(j);
                        } else {
                            plantilla = (String)listaCodPlantilla.elementAt(j);
                            desplegable = "";
                        }
                        sql = "INSERT INTO E_PCA (" + pca_mun + "," + pca_pro + "," + pca_cod + "," + pca_des + "," +
                                pca_plt + "," + pca_tda + "," + pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," +
                                pca_rot + "," + pca_activo + "," + pca_desplegable +",PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO, PERIODO_PLAZO,PCA_GROUP," +
                                " PCA_POS_X, PCA_POS_Y ) VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                defProcVO.getTxtCodigo() + "','" + listaCodCampos.elementAt(j) + "','" + listaDescCampos.elementAt(j) +
                "'," + plantilla + ",'" + listaCodTipoDato.elementAt(j) + "'," + listaTamano.elementAt(j);
                        if(listaMascara.elementAt(j) == null || "".equals(listaMascara.elementAt(j)) || " ".equals(listaMascara.elementAt(j))) {
                            sql += ",null,";
                        } else {
                            sql += ",'" +  listaMascara.elementAt(j) + "',";
                        }
                        sql += listaObligatorio.elementAt(j) + "," + j + ",'" + listaRotulo.elementAt(j) + "','" +
                               ((String)listaActivos.elementAt(j)).toUpperCase() + "','" + desplegable+ "','"+listaOcultos.elementAt(j) +"','"+
                                listaBloqueados.elementAt(j) + "',";
                        
                        if((listaPlazoFecha!=null) && (listaCheckPlazoFecha!=null)){                        
                            if((listaPlazoFecha.size()==listaCodCampos.size()) && (listaCheckPlazoFecha.size()==listaCodCampos.size())){
                                    if(listaPlazoFecha.elementAt(j) == null || "".equals(listaPlazoFecha.elementAt(j)) || " ".equals(listaPlazoFecha.elementAt(j))) {
                                    //if ((!listaCodTipoDato.elementAt(j).equals("3")) || (!"3".equals(listaCodTipoDato.elementAt(j)))){    
                                        sql += "null, null";
                                    } else {
                                        sql += "'" +  listaPlazoFecha.elementAt(j) + "','" + listaCheckPlazoFecha.elementAt(j) +"'";
                                    }
                               } else{ //El tamanho no es el mismo
                                  sql += "null, null";
                               }
                        } else {// son nulas listaPlazoFecha o listaCheckPlazoFecha
                            sql += "null, null";
                        } 

                        if(listaAgrupacionCampo != null && listaAgrupacionCampo.size() > 0){
                            if("".equals(listaAgrupacionCampo.elementAt(j)) || " ".equals(listaAgrupacionCampo.elementAt(j))){
                                sql += ",null";
                            }else{
                                sql += ",'" + listaAgrupacionCampo.elementAt(j) + "'";
                            }
                        }else{
                            sql += ",null";
                        }
                            
                        if (listaAgrupacionCampo == null){
                            sql +=",null";
                        }else if(listaPosicionesX != null && listaAgrupacionCampo.size() > 0){
                            if(listaPosicionesX.elementAt(j) == null || "".equals(listaPosicionesX.elementAt(j))){
                                sql +=",null";
                            }else{
                                sql +="," + listaPosicionesX.elementAt(j);
                            }
                        }else{
                            sql +=",null";
                        }        

                        if (listaAgrupacionCampo == null){
                            sql +=",null)";
                        }else if(listaPosicionesY != null && listaAgrupacionCampo.size() > 0){
                            if(listaPosicionesY.elementAt(j) == null || "".equals(listaPosicionesY.elementAt(j))){
                                sql +=",null)";
                            }else{
                                sql +="," + listaPosicionesY.elementAt(j) + ")";
                            }
                        }else{
                            sql +=",null)";
                        }
                        
                        if(log.isDebugEnabled()) log.debug(sql);
                        
                        log.debug("sql: " + sql);
                        st.executeUpdate(sql);                    
                    
                        String tip_dat = (String) listaCodTipoDato.elementAt(j);
                        valida = listaValidacion.elementAt(j).toString();            
                        if (tip_dat.trim().equals("1") && !"".equals(valida))
                        {   
                            //valida = listaValidacion.elementAt(contador_expresion).toString();
                            valida = valida.replace("/&lt;/g","<");  
                            valida = valida.replace("/&gt;/g",">"); 
                            valida = valida.replace("&lt;","<");  
                            valida = valida.replace("&gt;",">"); 


                            sql = " INSERT INTO EXPRESION_CAMPO_NUM_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,EXPRESION) VALUES " + 
                              " ("+ defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() +"','" +listaCodCampos.elementAt(j) +"','" + valida +                                
                              "')";                                                 
                            st.executeUpdate(sql);                                        
                        }
                        if (tip_dat.trim().equals("8") || tip_dat.trim().equals("9"))
                        {                               
                            valida = listaOperacion.elementAt(j).toString();
                            if (!"".equals(valida))
                            {
                                sql = " INSERT INTO EXPRESION_CAMPO_CAL_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,TIPO_DATO,EXPRESION) VALUES " + 
                                  " ("+ defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() +"','" +listaCodCampos.elementAt(j) +"',"+tip_dat+",'" + valida +                                
                                  "')";                                                 
                                st.executeUpdate(sql);                                            
                            }
                        }                    
                    }                    
                }
                
                Vector listaCodAgrupacion = defProcVO.getListaCodAgrupaciones();
                Vector listaDescAgrupacion = defProcVO.getListaDescAgrupaciones();
                Vector listaOrdenAgrupacion = defProcVO.getListaOrdenAgrupaciones();
                Vector listaAgrupacionActiva = defProcVO.getListaAgrupacionesActivas();
                
                for(int j=0;j<listaCodAgrupacion.size();j++){
                    if ("SI".equalsIgnoreCase((String)listaAgrupacionActiva.elementAt(j))) {
                       sql = "INSERT INTO E_PCA_GROUP (PCA_ID_GROUP,PCA_DESC_GROUP,PCA_ORDER_GROUP,PCA_PRO,PCA_ACTIVE) "+
                               "VALUES ('"  + listaCodAgrupacion.elementAt(j) + "','" + listaDescAgrupacion.elementAt(j) + "'," +
                                                       listaOrdenAgrupacion.elementAt(j) + ",'" + defProcVO.getTxtCodigo() + "','" + 
                                                       ((String)listaAgrupacionActiva.elementAt(j)).toUpperCase() +"')";

                        if(log.isDebugEnabled()) log.debug(sql);
                        
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
                
                // PESTAÑA DE ENLACES
                Vector listaCodEnlaces = defProcVO.getListaCodEnlaces();
                Vector listaDescEnlaces = defProcVO.getListaDescEnlaces();
                Vector listaUrlEnlaces = defProcVO.getListaUrlEnlaces();
                Vector listaEstadoEnlaces = defProcVO.getListaEstadoEnlaces();
                for(int m=0;m<listaCodEnlaces.size();m++) {
                    sql = "INSERT INTO E_ENP (" + enp_mun + "," + enp_pro + "," + enp_cod + "," + enp_des + "," +
                            enp_url + "," + enp_est + ") VALUES (" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodEnlaces.elementAt(m) + ",'" +
                            listaDescEnlaces.elementAt(m) + "','" + listaUrlEnlaces.elementAt(m) + "',";
                    if("S".equals(listaEstadoEnlaces.elementAt(m))) {
                        sql += "1)";
                    } else if("N".equals(listaEstadoEnlaces.elementAt(m))) {
                        sql += "0)";
                    }
                    if(log.isDebugEnabled()) log.debug(sql);

                    log.debug("sql: " + sql);
          resEnlaces = st.executeUpdate(sql);
                }
                if(listaCodEnlaces.size() == 0 ) {
                    resEnlaces = 1;
                }

                // PESTAÑA DE ROLES
                Vector listaCodRoles = defProcVO.getListaCodRoles();
                Vector listaDescRoles = defProcVO.getListaDescRoles();
                Vector listaPorDefecto = defProcVO.getListaPorDefecto();
                Vector listaConsultaWebRol = defProcVO.getListaConsultaWebRol();
                String porDefecto = "no";
                for(int v=0;v<listaPorDefecto.size();v++) {
                    String pD = (String) listaPorDefecto.elementAt(v);
                    if("1".equals(pD)) {
                        porDefecto = "si";
                        break;
                    }
                }
                if("no".equals(porDefecto)) {
                    listaPorDefecto.insertElementAt("1",0);
                }
                for(int m=0;m<listaCodRoles.size();m++) {
                    sql = "INSERT INTO E_ROL (" + rol_mun + "," + rol_pro + "," + rol_cod + "," + rol_des + "," +
                            rol_pde + ",ROL_PCW) VALUES (" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodRoles.elementAt(m) + ",'" +
                            listaDescRoles.elementAt(m) + "'," + listaPorDefecto.elementAt(m) + ","+listaConsultaWebRol.elementAt(m)+")";
                    if(log.isDebugEnabled()) log.debug(sql);
                    log.debug("sql: " + sql);
          resRoles = st.executeUpdate(sql);
                }
                if(listaCodRoles.size() == 0 ) {
                    resRoles = 1;
                }

                if(res == 0 || resEnlaces == 0 || resRoles == 0) {
                log.debug("res VALE: " + res + " resEnlaces VALE: " + resEnlaces  +
                                " resRoles VALE: " + resRoles);
                } else {
                    exito = true;
                }

            } 
            st.close();
            

        } catch (Exception ex) {
            ex.printStackTrace();
             log.error("Excepcion capturada en: " + getClass().getName());
             throw new TechnicalException("Error durante el alta del procedimiento");
        }finally{
            try{

                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }


    /**
     * Comprueba la existencia de determinadas unidades orgánicas
     * @param listaUnidades: Vector de String que contiene los códigos visibles de las unidades orgánicas
     * @param descripcionUnidades: Tabla hash con las descripciones. La clave es el código visible de la uor
     * @param con: Conexión a la base de datos
     * @return ArrayList<ExistenciaUorImportacionVO>
     */
    public ArrayList<ExistenciaUorImportacionVO> existenUors(Vector<String> listaUnidades,Hashtable<String,String> descripcionUnidades,Connection con){

        ArrayList<ExistenciaUorImportacionVO> salida = new ArrayList<ExistenciaUorImportacionVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            if(listaUnidades!=null){
                for(Iterator it = listaUnidades.iterator();it.hasNext();){
                    String cod = (String)it.next();
                    String sql = "SELECT UOR_COD,UOR_NOM,UOR_COD_VIS FROM A_UOR WHERE UOR_COD_VIS=?";
                    log.debug("sql: " + sql);
                    log.debug("param: " + cod);

                    ps = con.prepareStatement(sql);
                    int i=1;
                    ps.setString(i++, cod);

                    rs = ps.executeQuery();
                    ExistenciaUorImportacionVO uor = new ExistenciaUorImportacionVO();
                    uor.setCodigoUorVisible(cod);
                    uor.setNombre(descripcionUnidades.get(cod));
                    uor.setExiste(false);

                    while(rs.next()){
                        String nombre = rs.getString("UOR_NOM");
                        String codVisible = rs.getString("UOR_COD_VIS");
                        String codUor = rs.getString("UOR_COD");
                        if(nombre!=null && codVisible!=null && nombre.length()>0 && codVisible.length()>0){
                            uor.setExiste(true);
                            uor.setNombre(nombre);
                            uor.setCodigoUor(codUor);
                        }
                    }// while

                    salida.add(uor);
                }//for
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
    }
    
    /**
     * Recupera a partir del codigo visible de una unidad de inicio su identificador asociado
     * @param unidadeCodigoVisible
     * @param con
     * @return 
     */
    public String recuperaCodigoUnidadInicio (String unidadeCodigoVisible, Connection con){
        log.debug("entrando en el dao para recuperar el codigo de una unidad de inicio");
        ResultSet rs = null;
        String codigoUnidadInicio = null;
        PreparedStatement ps = null;
    
        String consulta = "SELECT UOR_COD FROM A_UOR WHERE UOR_COD_VIS=?";
        log.debug("sql: " + consulta);
        log.debug("param: " + unidadeCodigoVisible);
 
        try {           
                ps = con.prepareStatement(consulta);
                ps.setString(1, unidadeCodigoVisible);
                rs = ps.executeQuery();
                if(rs.next()){
                codigoUnidadInicio = rs.getString("UOR_COD");     
            }
            
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar el codigo de la unidad de inicio con código visible: "+ unidadeCodigoVisible+ " "+ ex.getMessage());
        }
    log.debug("entrando en el dao para recuperar el codigo de una unidad de inicio");
    return codigoUnidadInicio;
    }



  /**
     * Recupera las unidades de inicio de un procedimiento que este tenga asignadas y que no se encuentren ya en una determinada lista de unidades
     * @param listaUnidades: Vector de String que contiene los códigos visibles de las unidades orgánicas
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return ArrayList<ExistenciaUorImportacionVO>
     */
    public ArrayList<ExistenciaUorImportacionVO> getUnidadesInicioProcedimiento(Vector<String> listaUnidades,String codProcedimiento,Connection con){

        ArrayList<ExistenciaUorImportacionVO> salida = new ArrayList<ExistenciaUorImportacionVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            if(listaUnidades!=null){
                String sql2 = "";

                //for(Iterator it = listaUnidades.iterator();it.hasNext();){
                for(int i=0;i<listaUnidades.size();i++){
                    sql2 = sql2  + "'" + (String)listaUnidades.get(i) + "'";
                    if(listaUnidades.size()-i>1)
                        sql2 = sql2 + ",";
                }// for

                String sql = "SELECT UOR_COD,UOR_NOM,UOR_COD_VIS FROM A_UOR,E_PUI WHERE  PUI_PRO=? AND PUI_COD=UOR_COD AND  UOR_COD_VIS NOT IN (" + sql2 + ")";
                log.debug(sql);
                ps = con.prepareStatement(sql);
                ps.setString(1, codProcedimiento);
                rs = ps.executeQuery();

                while(rs.next()){
                    String nombre = rs.getString("UOR_NOM");
                    String codVisible = rs.getString("UOR_COD_VIS");
                    String codUor = rs.getString("UOR_COD");
                     
                    ExistenciaUorImportacionVO uor = new ExistenciaUorImportacionVO();
                    uor.setExiste(false);
                    uor.setNombre(nombre);
                    uor.setCodigoUor(codUor);
                    uor.setCodigoUorVisible(codVisible);
                    salida.add(uor);
                }// while

            }//if

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
    }




     /**
     * Comprueba si existe un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la base de datos
     * @return Boolean
     */
    public boolean existeProcedimiento(String codProcedimiento,String codMunicipio,Connection con)
    {
        boolean exito = false;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_PRO WHERE PRO_COD=? AND PRO_MUN=?";
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,codMunicipio);
            ps.setInt(i++, Integer.parseInt(codMunicipio));

            rs = ps.executeQuery();
            while(rs.next())
            {
                int num = rs.getInt("NUM");
                if(num>=1) exito = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }// existeProcedimiento

    

    
    /**
     * Inserta un trámite 
     * @param defTramVO: Definición del trámite
     * @param conexion: Conexión a la base de datos
     * @param params: Parámetros de conexión a la base de datos
     * @return
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.util.conexion.BDException
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
     public int insertarTramite(DefinicionTramitesValueObject defTramVO,Connection conexion, String[] params) throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        String sql1 =	"";
        String sqlCond = "";
        String sqlCondSML =	"";
        int res	= 0;
        int res1 = 0;
        int res2 = 0;
        int res3 = 0;
        int resCondEnt = 0;
        int resEnl = 0;
        int resTotal = 0;
        int resSML = 0;
        AdaptadorSQLBD abd = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        boolean insertUnidadesTramitadoras = true;

        if(log.isDebugEnabled()) log.debug("Entra	en el	insert del DAO");

        try{

            if(log.isDebugEnabled()) log.debug("A por el OAD");
            abd =	new AdaptadorSQLBD(params);
            
            // creamos el ddepartamento de notificación que se exporta en el trámite en caso de no existir.
            DefinicionTramitesDAO.getInstance().comprobarErrorDepartamentoNotificacion(defTramVO, conexion);

            // PESTAÑA DE DATOS

			// Select para utilizar el código siguiente al del último trámite insertado
            sql1 = "SELECT " + tra_cod + " FROM	E_TRA	WHERE	" + tra_mun	+ "="	+ defTramVO.getCodMunicipio()	+
                    " AND " + tra_pro + "='" + defTramVO.getTxtCodigo() + "'";
            String parametros[] = {"1","1"};
            sql1 += abd.orderUnion(parametros);

            if(log.isDebugEnabled()) log.debug(sql1);

            st = conexion.prepareStatement(sql1);
            rs = st.executeQuery();
            String codTramite	= "0";
            int codTram	= 0;
            while	( rs.next()	) {
                codTramite = rs.getString(tra_cod);
            }
            rs.close();
            st.close();
            codTram = Integer.parseInt(codTramite);
            codTram = codTram	+ 1;
            codTramite = Integer.toString(codTram);
            defTramVO.setCodigoTramite(codTramite);

            sql =	"INSERT INTO E_TRA (" +	tra_mun + "," + tra_pro	+ ","	+ tra_cou +	"," +	tra_cod + "," +
                    tra_vis + "," + tra_uin	+ ","	+ tra_utr +	"," +	tra_plz + "," + tra_und	+ ","	+ tra_ins + "," +
                    tra_ocu + "," + tra_cls	+ ","	+ tra_pre +	","+ tra_uti +	","+ tra_utf +	","+ tra_usi +	","+
                    tra_usf +	","+ tra_ini +	","+ tra_inf + ", TRA_NOTIF_UITI, TRA_NOTIF_UITF, TRA_NOTIF_UIEI, TRA_NOTIF_UIEF, " + tra_prr + "," + tra_car +
                    ",TRA_FIN, TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP,TRA_NOTIFICACION_ELECTRONICA,"+
                    "TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA,TRA_NOTIFICADO,COD_DEPTO_NOTIFICACION,TRA_NOTIF_ELECT_OBLIG,TRA_NOTIF_FIRMA_CERT_ORG) VALUES(" + defTramVO.getCodMunicipio() + ",'" +
                    defTramVO.getTxtCodigo() + "'," + defTramVO.getNumeroTramite() +
                    "," +	codTramite + "," + defTramVO.getDisponible();
            if(defTramVO.getCodUnidadInicio() == null || defTramVO.getCodUnidadInicio().equals("")){
                sql	+= ",null";
            } else if ("-99999".equals(defTramVO.getCodUnidadInicio()) || "-99998".equals(defTramVO.getCodUnidadInicio())) {
                sql += "," + defTramVO.getCodUnidadInicio();
            }else{
                sql	+= "," + this.recuperaCodigoUnidadInicio(defTramVO.getCodUnidadInicio(), conexion);
            }
            if(defTramVO.getCodUnidadTramite().equals("") || defTramVO.getCodUnidadTramite() ==	null)
                sql	+= ",null";
            else
                sql	+= "," + defTramVO.getCodUnidadTramite();
            if (defTramVO.getPlazo() != null) {
                if (!defTramVO.getPlazo().equals(""))
                    sql += ","+defTramVO.getPlazo()	+ ",'"+defTramVO.getUnidadesPlazo()	+ "'";
                else sql += ", null" + ", null";
            }
            else sql +=	",null" + ", null";
            if (defTramVO.getInstrucciones() != null){
                if (!"".equals(defTramVO.getInstrucciones())){
                    sql += ",'" + defTramVO.getInstrucciones() + "'";
                    defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(defTramVO.getInstrucciones()));
                } else sql += " ,null";
            } else sql += ", null";

            if (defTramVO.getOcurrencias() != null) {
                if(!defTramVO.getOcurrencias().equals(""))
                    sql += ","+defTramVO.getOcurrencias();
                else sql += ",null";
            }
            else sql +=	",null";
            sql += "," + defTramVO.getCodClasifTramite() + "," + defTramVO.getTramitePregunta();

            sql += ",'" + defTramVO.getNotUnidadTramitIni() + "','" + defTramVO.getNotUnidadTramitFin()
                    + "','" + defTramVO.getNotUsuUnidadTramitIni() + "','" + defTramVO.getNotUsuUnidadTramitFin()
                    + "','" + defTramVO.getNotInteresadosIni() + "','" + defTramVO.getNotInteresadosFin() + "'";
            
              if ((defTramVO.getNotUsuInicioTramiteIni() != null) &&
                (!defTramVO.getNotUsuInicioTramiteIni().equals(""))) 
                    sql += ",'"+defTramVO.getNotUsuInicioTramiteIni()+ "'";
                else sql += ",'N'";
              
               if ((defTramVO.getNotUsuInicioTramiteFin() != null) &&
                (!defTramVO.getNotUsuInicioTramiteFin().equals(""))) 
                    sql += ",'"+defTramVO.getNotUsuInicioTramiteFin()+ "'";
                else sql += ",'N'";
               
                if ((defTramVO.getNotUsuInicioExpedIni() != null) &&
                (!defTramVO.getNotUsuInicioExpedIni().equals(""))) 
                    sql += ",'"+defTramVO.getNotUsuInicioExpedIni()+ "'";
                else sql += ",'N'";
                
                 if ((defTramVO.getNotUsuInicioExpedFin() != null) &&
                (!defTramVO.getNotUsuInicioExpedFin().equals(""))) 
                    sql += ",'"+defTramVO.getNotUsuInicioExpedFin()+ "'";
                else sql += ",'N'";
                 
         

            if(defTramVO.getCodExpRel() ==	null || defTramVO.getCodExpRel().equals(""))
                sql	+= ",null";
            else
                sql	+= ",'" + defTramVO.getCodExpRel() + "'";



            if(defTramVO.getCodCargo() ==	null || defTramVO.getCodCargo().equals("")){                
                sql	+= ",null";
            }
            else{
                /** Se comprueba si el código de cargo existe y se recupera el código interno del mismo */
                int codCargo = this.getCodigoAreaByCodVisible(defTramVO.getCodVisibleCargo(), conexion);
                log.debug("Para el cargo " + defTramVO.getCodCargo() + " y código visible: " + defTramVO.getCodVisibleCargo() + " le corresponde en el entorno actual el cargo: " + codCargo);
                if(codCargo!=-1)
                    sql	+= "," + codCargo;
                else
                    sql	+= ",null";
                //sql	+= "," + defTramVO.getCodCargo();
            }
            
            sql	+= "," + defTramVO.getPlazoFin();
            sql +=",?,?,?,?,?,?,?,?,?,?,?,?,?";
            sql += ")";
           

            if(log.isDebugEnabled()) log.debug(sql);
            PreparedStatement	ps = conexion.prepareStatement(sql);
             int i = 1;
             ps.setBoolean(i++, defTramVO.isGenerarPlazos());
             ps.setBoolean(i++, defTramVO.getNotificarCercaFinPlazo());
             ps.setBoolean(i++, defTramVO.getNotificarFueraDePlazo());
             ps.setInt(i++, defTramVO.getTipoNotCercaFinPlazo());
             ps.setInt(i++, defTramVO.getTipoNotFueraDePlazo());


             //TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA
             if(defTramVO.getAdmiteNotificacionElectronica()!=null && defTramVO.getAdmiteNotificacionElectronica().length()>0) {
                 ps.setString(i++,defTramVO.getAdmiteNotificacionElectronica());
             }else {
                 ps.setNull(i++,java.sql.Types.VARCHAR);
             }

             if(defTramVO.getCodigoTipoNotificacionElectronica()!=null && defTramVO.getCodigoTipoNotificacionElectronica().length()>0) {
                 ps.setInt(i++,Integer.parseInt(defTramVO.getCodigoTipoNotificacionElectronica()));
             }else{
                 ps.setNull(i++,java.sql.Types.INTEGER);
             }
             if(defTramVO.getTipoUsuarioFirma()!=null && defTramVO.getTipoUsuarioFirma().length()>0) {
                 ps.setString(i++,defTramVO.getTipoUsuarioFirma());
             }else{
                 ps.setNull(i++,java.sql.Types.VARCHAR);
             }
             if(defTramVO.getCodigoOtroUsuarioFirma()!=null && defTramVO.getCodigoOtroUsuarioFirma().length()>0) {
                 ps.setInt(i++,Integer.parseInt(defTramVO.getCodigoOtroUsuarioFirma()));
             }else{
                 ps.setNull(i++,java.sql.Types.INTEGER);
             }
             ps.setInt(i++, (defTramVO.isTramiteNotificado()?1:0));
             ps.setString(i++,defTramVO.getCodDepartamentoNotificacion());
             ps.setBoolean(i++, defTramVO.getNotificacionElectronicaObligatoria());
             ps.setBoolean(i++, defTramVO.getCertificadoOrganismoFirmaNotificacion());

            res = ps.executeUpdate();
            ps.close();
            if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el insert E_TRA son ::::::::::::::	: " +	res);

            sql =	"INSERT INTO E_TML VALUES(" +	defTramVO.getCodMunicipio() +
                    ",'" + defTramVO.getTxtCodigo() + "'," + codTramite +	",'NOM','" + idiomaDefecto + "','" +
                    defTramVO.getNombreTramite() + "')";

            if(log.isDebugEnabled()) log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            ps.close();
            if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el insert E_TML son ::::::::::::::	: " +	res1);

            if(defTramVO.getTramiteInicio().equals("1")) {
                sql	= "UPDATE E_PRO SET " +	pro_tri + "=" + codTramite + " WHERE " +	pro_mun + "=" +
                        defTramVO.getCodMunicipio()	+ " AND " +	pro_cod + "='" + defTramVO.getTxtCodigo()	+ "'";

                if(log.isDebugEnabled()) log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res3 = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas	en el	update de E_PRO son :::::::::::::: : " + res3);
            }

            if(res !=0)	{
                // PESTAÑA DE DOCUMENTOS
                Vector listaCodigosDoc = defTramVO.getListaCodigosDoc();
                Vector listaNombresDoc = defTramVO.getListaNombresDoc();
                Vector listaVisibleDoc = defTramVO.getListaVisibleDoc();
                Vector listaPlantillaDoc = defTramVO.getListaPlantillaDoc();
                Vector listaContPlantilla = defTramVO.getListaContidoPlantilla();
                Vector listaIntPlantilla = defTramVO.getListaInteresadoPlantilla();
                Vector listaRelPlantilla = defTramVO.getListaRelacionPlantilla();
                Vector listaEditoresTexto = defTramVO.getListaEditoresTexto();
                Vector listaFirmaDoc = defTramVO.getListaFirmaDoc();
                Vector listaDocActivos = defTramVO.getListaDocActivos();
                if(log.isDebugEnabled()) log.debug("el tamaño	de las listas de documentos es : " +
                        listaCodigosDoc.size()	+ "|"	+ listaNombresDoc.size() +
                        "|" + listaVisibleDoc.size()	+ "|"	+ listaPlantillaDoc.size() );
                for(int l=0;l<listaCodigosDoc.size();l++){
                    Integer codPlantilla = 0;
                    if ((String)listaPlantillaDoc.elementAt(l)!=null && !"".equals(listaPlantillaDoc.elementAt(l))) {
                        sql = "select " + abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[] {abd.funcionMatematica(
                        AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[] {aplt_cod}) + "+1", "1"}) + " as " +
                        aplt_cod + " from A_PLT";
                        if(log.isDebugEnabled()) log.debug(sql);
                        ps = conexion.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            codPlantilla = rs.getInt(aplt_cod);
                        }
                        rs.close();
                        ps.close();
                        
                        sql = "INSERT INTO A_PLT (PLT_COD,PLT_DES,PLT_APL,PLT_DOC,PLT_PRO,PLT_TRA,PLT_INT," + 
                                "PLT_REL,PLT_EDITOR_TEXTO) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

                        if(log.isDebugEnabled()) log.debug(sql);
                        ps = conexion.prepareStatement(sql);
                        int ind=1;
                        ps.setInt(ind++, codPlantilla);
                        ps.setString(ind++,(String)listaPlantillaDoc.elementAt(l));
                        ps.setInt(ind++, 4); 
                        String contPlantilla = (String)listaContPlantilla.elementAt(l);
                        if (contPlantilla!=null) {
                            byte[] documento = Base64.decodeBase64(contPlantilla.getBytes());
                            java.io.InputStream ist = new java.io.ByteArrayInputStream(documento);
                            ps.setBinaryStream(ind++,ist,documento.length);
                        } else {
                            ps.setNull(ind++, java.sql.Types.BLOB);
                        }
                        ps.setString(ind++,defTramVO.getTxtCodigo());
                        ps.setInt(ind++, Integer.parseInt(codTramite));
                        ps.setString(ind++,(String)listaIntPlantilla.elementAt(l));
                        ps.setString(ind++,(String)listaRelPlantilla.elementAt(l));
                        ps.setString(ind++,(String)listaEditoresTexto.elementAt(l));

                        ps.executeUpdate();
                        ps.close();
                    }
							
                    // Se guarda el tipo de firma en una variable porque se va a comprobar su valor repetidas veces
					String tipoFirma = (String) listaFirmaDoc.elementAt(l);
					
                    sql = "INSERT INTO E_DOT (" + dot_mun + "," + dot_pro + "," + dot_tra + "," + dot_cod + ", DOT_TDO, " +
                            dot_vis + ", DOT_FRM, " + dot_plt + "," + dot_activo + ") VALUES(" +defTramVO.getCodMunicipio() +",'" +
                            defTramVO.getTxtCodigo() + "'," + codTramite + "," + listaCodigosDoc.elementAt(l) + ", null, '"
                            + listaVisibleDoc.elementAt(l) + "',";
                        if (tipoFirma!=null) 
                            sql +="'"+tipoFirma+"',";
                        else 
                            sql +="NULL,";
                        if (listaPlantillaDoc.elementAt(l) == null || "".equals(listaPlantillaDoc.elementAt(l))) {
                            sql += "null,";
                        } else {
                            sql += codPlantilla + ",";
                    }
                    sql += "'" + listaDocActivos.elementAt(l) + "')";
                    if (log.isDebugEnabled()) {
                        log.debug(sql);
                    }
                    ps = conexion.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();

                    if (tipoFirma != null && !tipoFirma.equals("") && !tipoFirma.equals("T")) {
                        // Si el documento tiene un tipo de firma en E_DOT.DOT_FRM no nulo, no vacio y distinto de T (firma tipo tramitador: no soportada)
                        // se inserta los datos de la firma en E_DOT_FIR
                        insertarFirmaDocumento(defTramVO.getCodMunicipio(), defTramVO.getTxtCodigo(),
                                defTramVO.getCodigoTramite(), (String) listaCodigosDoc.elementAt(l),
                                defTramVO.getListaFirmasFlujo().get(l), defTramVO.getListaFirmasDocumentoIdsUsuario().get(l),
                                defTramVO.getListaFirmasDocumentoLogsUsuario().get(l), defTramVO.getListaFirmasDocumentoDnisUsuario().get(l), conexion);
                        
                    } else if (tipoFirma == null || tipoFirma.equals("") || tipoFirma.equals("T")) {
                        borrarFirmaDocumento(defTramVO.getCodMunicipio(), defTramVO.getTxtCodigo(), defTramVO.getCodigoTramite(), (String) listaCodigosDoc.elementAt(l), conexion);
                    }
                }

                // PESTAÑA DE CONDICIONES DE ENTRADA

                Vector listaTramitesTablaEntrada = defTramVO.getListaTramitesTabla();
                Vector listaCodTramitesTablaEntrada =	defTramVO.getListaCodTramitesTabla();
                Vector listaEstadoTablaEntrada = defTramVO.getListaEstadosTabla();
                Vector listaTipoTablaEntrada = defTramVO.getListaTiposTabla();
                Vector listaExpresionesTablaEntrada = defTramVO.getListaExpresionesTabla();
                Vector listaCodigoDocumentoEntrada = defTramVO.getListaCodigosDocTabla();
                for(int	j=0;j<listaTramitesTablaEntrada.size();j++){
                    if(listaTipoTablaEntrada.elementAt(j).equals("TRÁMITE")) {
                    sql =	"INSERT INTO E_ENT (" +	ent_mun + "," + ent_pro	+ ","	+ ent_tra +	"," +	ent_cod +
                    "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + ") VALUES("	+ defTramVO.getCodMunicipio() + ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
                    //listaCodCondicionTablaEntrada.elementAt(j)
                    (j+1)  + "," + listaCodTramitesTablaEntrada.elementAt(j);
                    if(listaEstadoTablaEntrada.elementAt(j).equals("FINALIZADO")) {
                          sql += ",'F'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("INICIADO")) {
                          sql += ",'I'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("NO INICIADO")) {
                          sql += ",'O'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("FAVORABLE")) {
                          sql += ",'S'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("DESFAVORABLE")) {
                          sql += ",'N'";
                    } else {
                          sql += ",'C'";
                    }
                      sql += ",'T')";
                    }else if(listaTipoTablaEntrada.elementAt(j).equals("DOCUMENTO")) {
                    sql =	"INSERT INTO E_ENT (" +	ent_mun + "," + ent_pro	+ ","	+ ent_tra +	"," +	ent_cod +
                    "," + ent_ctr	+ ","	+ ent_est +"," + ent_tipo +",ENT_DOC) VALUES("	+ defTramVO.getCodMunicipio() + ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
                    //listaCodCondicionTablaEntrada.elementAt(j)
                    (j+1)  + ",0";
                                
                    if(listaEstadoTablaEntrada.elementAt(j).equals("FIRMADO")) {
                          sql += ",'F'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("PENDIENTE")) {
                          sql += ",'O'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("RECHAZADO")) {
                          sql += ",'R'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("CIRCUITO FINALIZADO")) {
                          sql += ",'C'";
                    } 
                                                                                
                      sql += ",'D',"+listaCodigoDocumentoEntrada.elementAt(j)+")";
                    } else { //tipo Expresion
                    sql = "INSERT	INTO E_ENT (" + ent_mun	+ ","	+ ent_pro +	"," +	ent_tra + "," + ent_cod	+
                        "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + "," + ent_exp + ") VALUES("+ defTramVO.getCodMunicipio()	+ ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," + (j+1);
                    String aux =(String) listaExpresionesTablaEntrada.elementAt(j);
                    aux = aux.replaceAll("&lt;","<");
                    aux = aux.replaceAll("&gt;",">");
                    aux = aux.replaceAll("&#39;","'");

                    sql += ", 0 , ' ' ,'E'," + abd.addString(aux) + ")";
                    }

                    if(log.isDebugEnabled()) log.debug(sql);

                    ps = conexion.prepareStatement(sql);
                    resCondEnt = ps.executeUpdate();
                    ps.close();
                }
                if(listaTramitesTablaEntrada.size() == 0 ) {
                    resCondEnt = 1;
                }
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_ENT son :::::::::::::: : "	+ resCondEnt);

                // PESTAÑA DE ENLACES
                Vector listaCodigoEnlaces = defTramVO.getListaCodigoEnlaces();
                Vector listaDescripcionEnlaces =	defTramVO.getListaDescripcionEnlaces();
                Vector listaUrlEnlaces = defTramVO.getListaUrlEnlaces();
                Vector listaEstadoEnlaces = defTramVO.getListaEstadoEnlaces();
                for(int j=0;j<listaCodigoEnlaces.size();j++) {
                    sql =	"INSERT INTO E_TEN (" +	ten_mun + "," + ten_pro	+ ","	+ ten_tra +	"," +	ten_cod +
                            ","	+ ten_des +	"," +	ten_url + "," +	ten_est + ") VALUES(" +	defTramVO.getCodMunicipio() +	",'" +
                            defTramVO.getTxtCodigo() + "'," +codTramite + "," +
                            listaCodigoEnlaces.elementAt(j) + ",'" + listaDescripcionEnlaces.elementAt(j) + "','" +
                            listaUrlEnlaces.elementAt(j) + "'," + listaEstadoEnlaces.elementAt(j)+")";

                    ps = conexion.prepareStatement(sql);
                    resEnl = ps.executeUpdate();
                    ps.close();
                }
                if(listaCodigoEnlaces.size() == 0 ) {
                    resEnl = 1;
                }
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_TEN son :::::::::::::: : "	+ resEnl);

                // PESTAÑA DE CONDICIONES DE SALIDA
                String tipoCondicion = defTramVO.getTipoCondicion();
                String tipoFavorableSI = defTramVO.getTipoFavorableSI();
                String tipoFavorableNO = defTramVO.getTipoFavorableNO();
                String tFSI ="";
                String tFNO =	"";
                if(tipoFavorableSI != null)
                    tFSI = tipoFavorableSI.substring(0,1);
                if(tipoFavorableNO != null)
                    tFNO = tipoFavorableNO.substring(0,1);

                if("Tramite".equals(tipoCondicion)) {
                    sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                            sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                            defTramVO.getTxtCodigo() + "'," +	codTramite + ",'T','','')" ;
                }
                if("Finalizacion".equals(tipoCondicion)) {
                    sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                            sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                            defTramVO.getTxtCodigo() +	"'," + codTramite	+ ",'F','','')";
                }
                if("Pregunta".equals(tipoCondicion)) {
                    sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                            sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                            defTramVO.getTxtCodigo() + "'," +	codTramite + "," +
                            "'P','" +	tFSI + "','" + tFNO +"')";
                    sqlCondSML = "INSERT INTO E_SML (" + sml_mun + "," + sml_pro + "," + sml_tra + "," +
                            sml_cmp + "," + sml_leng + "," + sml_valor + ") VALUES(" +
                            defTramVO.getCodMunicipio() + ",'"	+ defTramVO.getTxtCodigo() + "'," +
                            codTramite	+ ",'TXT','" + idiomaDefecto + "','" + defTramVO.getTexto() +	"')";
                }
                if("Resolucion".equals(tipoCondicion)) {
                    sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                            sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                            defTramVO.getTxtCodigo() + "'," +	codTramite + "," +
                            "'R','" +	tFSI + "','" + tFNO +"')";
                }

                if("sinCondicion".equals(tipoCondicion)) {
                    sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                            sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                            defTramVO.getTxtCodigo() +	"'," + codTramite	+ ","	+
                            "'','','')";
                }

                if(log.isDebugEnabled()) log.debug(sqlCond);
                ps = conexion.prepareStatement(sqlCond);
                res2 = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el INSERT de	E_SAL	son :::::::::::::: : " + res2);

                if(!sqlCondSML.equals("")) {
                    if(log.isDebugEnabled()) log.debug(sqlCondSML);
                    ps = conexion.prepareStatement(sqlCondSML);
                    resSML = ps.executeUpdate();
                    ps.close();
                    if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el INSERT de E_SML son	:::::::::::::: : " + resSML);
                }

                // PESTAÑA DE CAMPOS

                Vector listaCodCampos = defTramVO.getListaCodCampos();
                Vector listaDescCampos = defTramVO.getListaDescCampos();
                Vector listaCodPlantill = defTramVO.getListaCodPlantill();
                Vector listaCodTipoDato = defTramVO.getListaCodTipoDato();
                Vector listaTamano = defTramVO.getListaTamano();
                Vector listaMascara = defTramVO.getListaMascara();
                Vector listaObligatorio = defTramVO.getListaObligatorio();
                Vector listaOrden = defTramVO.getListaOrden();
                Vector listaRotulo = defTramVO.getListaRotulo();
                Vector listaVisible = defTramVO.getListaVisible();
                Vector listaActivo = defTramVO.getListaActivo();
                Vector listaOculto = defTramVO.getListaOcultos();
                Vector listaBloqueado = defTramVO.getListaBloqueados();
                Vector listaPlazoFecha = defTramVO.getListaPlazoFecha();
                Vector listaCheckPlazoFecha = defTramVO.getListaCheckPlazoFecha();
                Vector listaValidacion = defTramVO.getListaValidacion();
                Vector listaOperacion = defTramVO.getListaOperacion();
                Vector listaAgrupacionCampo = defTramVO.getListaCodAgrupacionCampo();
                Vector listaPosicionesX = defTramVO.getListaPosX();
                Vector listaPosicionesY = defTramVO.getListaPosY();
                
                String valida = "";
                
                for(int j=0;j<listaCodCampos.size();j++){
                    if ("SI".equalsIgnoreCase((String)listaActivo.elementAt(j))) {
                        String plantilla="", desplegable="";
                        if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                            plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                            desplegable=(String)listaCodPlantill.elementAt(j);
                        }else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
                            plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                            desplegable=(String)listaCodPlantill.elementAt(j);
                        } else {
                            plantilla=(String)listaCodPlantill.elementAt(j);
                            desplegable="";
                        }
                        sql = "INSERT INTO E_TCA (" + tca_mun + "," + tca_pro + "," + tca_tra + ","+ tca_cod + "," +
                                tca_des + "," + tca_plt + "," + tca_tda + "," + tca_tam + "," + tca_mas + "," + tca_obl +
                        "," + tca_nor + "," + tca_rot + "," + tca_vis + "," + tca_activo + "," + tca_desplegable +
                        ",TCA_OCULTO,TCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO, PCA_GROUP, TCA_POS_X,TCA_POS_Y) VALUES (" + 
                        defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() + "',"+ codTramite +
                        ",'" + listaCodCampos.elementAt(j) + "','" + listaDescCampos.elementAt(j) + "'," +
                        plantilla + ",'" + listaCodTipoDato.elementAt(j) + "'," + listaTamano.elementAt(j);
                        if(listaMascara.elementAt(j) == null || "".equals(listaMascara.elementAt(j)) || " ".equals(listaMascara.elementAt(j))) {
                            sql += ",null,";
                        } else {
                            sql += ",'" +  listaMascara.elementAt(j) + "',";
                        }
                  
                         sql += listaObligatorio.elementAt(j) + "," + listaOrden.elementAt(j) + ",'" +
                        listaRotulo.elementAt(j) + "','"+ listaVisible.elementAt(j) + "','" +
                        listaActivo.elementAt(j)+ "','" + desplegable + "','" +
                        listaOculto.elementAt(j)+ "','" + listaBloqueado.elementAt(j) + "',";
                        
                         if((listaPlazoFecha!=null) && (listaCheckPlazoFecha!=null)){                        
                            if((listaPlazoFecha.size()==listaCodCampos.size()) && (listaCheckPlazoFecha.size()==listaCodCampos.size())){
                                    if(listaPlazoFecha.elementAt(j) == null || "".equals(listaPlazoFecha.elementAt(j)) || " ".equals(listaPlazoFecha.elementAt(j))) {
                                    //if ((!listaCodTipoDato.elementAt(j).equals("3")) || (!"3".equals(listaCodTipoDato.elementAt(j)))){    
                                        sql += "null, null";
                                    } else {
                                        sql += "'" +  listaPlazoFecha.elementAt(j) + "','" + listaCheckPlazoFecha.elementAt(j) +"'";
                                    }
                               } else{ //El tamanho no es el mismo
                                  sql += "null, null";
                               }
                        } else {// son nulas listaPlazoFecha o listaCheckPlazoFecha
                            sql += "null, null";
                        } 
                        
                        if (listaAgrupacionCampo == null){
                            sql += ",null";
                        }else if(listaAgrupacionCampo != null && listaAgrupacionCampo.size() > 0){
                            if(listaAgrupacionCampo.elementAt(j) == null || "".equals(listaAgrupacionCampo.elementAt(j)) 
                                || " ".equals(listaAgrupacionCampo.elementAt(j))){
                                sql += ",null";
                            }else{
                                sql += ",'" + listaAgrupacionCampo.elementAt(j) + "'";
                            }
                        }else{
                            sql += ",null";
                        }

                        if (listaAgrupacionCampo == null){
                            sql += ",null";
                        }else if(listaPosicionesX != null && listaAgrupacionCampo.size() > 0)
                        {    
                            if(listaPosicionesX.elementAt(j) == null || "".equals(listaPosicionesX.elementAt(j))){
                                sql +=",null";
                            }else{
                                sql +="," + listaPosicionesX.elementAt(j);
                            }
                        }else{
                            sql +=",null";
                        }        
                        
                        if (listaAgrupacionCampo == null){
                            sql += ",null)";                        
                        }else if(listaPosicionesY != null && listaAgrupacionCampo.size() > 0)
                        {
                            if(listaPosicionesY.elementAt(j) == null || "".equals(listaPosicionesY.elementAt(j))){
                                sql +=",null)";
                            }else{
                                sql +="," + listaPosicionesY.elementAt(j) + ")";
                            }
                        }else{
                            sql +=",null)";
                        }
                        
                        if(log.isDebugEnabled()) log.debug(sql);
                        
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();
                        ps.close();
                        
                        String tip_dat = (String) listaCodTipoDato.elementAt(j);
                        valida = listaValidacion.elementAt(j).toString();            
                        if (tip_dat.trim().equals("1") && !"".equals(valida))
                        { 

                            valida = valida.replace("/&lt;/g","<");  
                            valida = valida.replace("/&gt;/g",">"); 
                            valida = valida.replace("&lt;","<");  
                            valida = valida.replace("&gt;",">"); 

                            sql = " INSERT INTO EXPRESION_CAMPO_NUM_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,EXPRESION) VALUES " + 
                              " ("+ defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() +"'," + defTramVO.getCodigoTramite() + ",'" +listaCodCampos.elementAt(j) +"','" + valida +                                
                              "')";
                            ps = conexion.prepareStatement(sql);
                            ps.executeUpdate();            
                            ps.close(); 
                        }                        
                        if (tip_dat.trim().equals("8") || tip_dat.trim().equals("9"))
                        {   
                            valida = listaOperacion.elementAt(j).toString();       
                            if (!"".equals(valida))      
                            {
                                sql = " INSERT INTO EXPRESION_CAMPO_CAL_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,TIPO_DATO,EXPRESION) VALUES " + 
                                  " ("+ defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() + "'," + defTramVO.getCodigoTramite() + ",'"+ listaCodCampos.elementAt(j) +"',"+tip_dat+",'" + valida +                                
                                  "')";                    
                                ps = conexion.prepareStatement(sql);
                                ps.executeUpdate();    
                                ps.close();
                            }
                        }                                                   
                    }
                }
                if (defTramVO.getListaCodAgrupacion() != null){
                    Vector listaCodAgrupacion = defTramVO.getListaCodAgrupacion();
                    Vector listaDescAgrupacion = defTramVO.getListaDescAgrupacion();
                    Vector listaOrdenAgrupacion = defTramVO.getListaOrdenAgrupacion();
                    Vector listaAgrupacionActiva = defTramVO.getListaAgrupacionActiva();

                    for(int j=0;j<listaCodAgrupacion.size();j++){
                        if ("SI".equalsIgnoreCase((String)listaAgrupacionActiva.elementAt(j))) {
                           sql = "INSERT INTO E_TCA_GROUP (TCA_ID_GROUP,TCA_DESC_GROUP,TCA_ORDER_GROUP,TCA_PRO,TCA_ACTIVE,TCA_TRA) "+
                                   "VALUES ('"  + listaCodAgrupacion.elementAt(j) + "','" + listaDescAgrupacion.elementAt(j) + "'," +
                                                           listaOrdenAgrupacion.elementAt(j) + ",'" + defTramVO.getTxtCodigo() + "','" + 
                                                           listaAgrupacionActiva.elementAt(j) + "'," + defTramVO.getCodigoTramite()+")";

                            if(log.isDebugEnabled()) log.debug(sql);

                            ps = conexion.prepareStatement(sql);
                            ps.executeUpdate();
                            ps.close();
                        }
                    }
                }
                
                // Se insertan las unidades tramitadoras del trámite
                if(defTramVO.getCodUnidadTramite()!=null && "0".equals(defTramVO.getCodUnidadTramite())){                     
                     try{
                        UnidadesTramitacionDAO.getInstance().insertUTR(Integer.parseInt(defTramVO.getCodMunicipio()),defTramVO.getTxtCodigo(),Integer.parseInt(codTramite),defTramVO.getListaUnidadesTramitadoras(), conexion);                        

                     }catch(TechnicalException e){
                         e.printStackTrace();
                         insertUnidadesTramitadoras = false;
                     }
                }// if

                try{
                    // Grabar las condiciones de salida del trámite
                    prepararGrabacionFlujoSalida (defTramVO,conexion);
                }catch(Exception e){
                    log.error(" ******************* error: " + e.getMessage());
                    e.printStackTrace();
                    resTotal = -1;
                }

            } else {
                if(log.isDebugEnabled()) log.debug("entra	en yaExise igual a si");
                resTotal = -1;
            }

            if(res != 0	&& res2 !=0	&& resCondEnt !=0	&& resEnl !=0 && insertUnidadesTramitadoras){
                resTotal = 1;
            } else {
              
                    log.error("INSERT NO HECHO EN " + getClass().getName());
                    log.debug("res VALE: " + res + " res2 VALE: " + res2 + " resCondEnt VALE: " + resCondEnt  +
                            " resEnl VALE: " + resEnl);
              
            }

        }catch(Exception ex)	{
            ex.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally {
            try {
                if (rs!=null) rs.close();
                if (st!=null) st.close();
            } catch(Exception ex) {
                ex.printStackTrace();
                log.error("Exception: " + ex.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("el entero que devuelve el insert es	: " +	resTotal);
        return resTotal;
    }

/**
     * Para cada trámite a importar se actualiza el código de trámite que le corresponde en el nuevo entorno puesto que se está actualizando el trámite y el código interno del mismo
     * no tiene porque coincidir con el procedente de una nueva definición. Esto se hace a posteriori porque en la tabla E_FLS en el campo FLS_CTS no se sabe
 *   * que código corresponde hasta que se insertan todos los tramites.
     * @param tramites: Vector con los trámite
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return Vector<DefinicionTramitesValueObject>
     */
    public void actualizarCodigosTramite(HashMap<String,String> relacionCodigosTramites,String codMunicipio,String codProcedimiento,Connection con) {


        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        try{
			
			String sql = "DELETE E_FLS WHERE "
					+ fls_mun + "=" + codMunicipio + " AND "
					+ fls_pro + "= 'TMP$'";
			log.debug("Eliminar posibles valores basura: " + sql);

			ps = con.prepareStatement(sql);
			ps.executeUpdate();
			
			for (Map.Entry<String, String> entrada : relacionCodigosTramites.entrySet()) {
                            String codTraOrigen = entrada.getKey();
                            String codTraNuevo = entrada.getValue();

			   if (!codTraNuevo.equals(codTraOrigen)) {
				   log.debug(String.format("Cambiando codigos tramite : %s->%s", codTraOrigen, codTraNuevo));
					// Actualizamos los codigos de los trámites a los que se transicionan, dado que a la hora de
					// importar ya se ha insertado FLS_TRA con el valor actualizado.
					// También actualizamos el procedimiento a un valor temporal de forma que el valor nuevo no se 
					// corresponda con otro antiguo y no dé violación de restricción
					sql = "UPDATE E_FLS SET " + fls_cts + "=" + codTraNuevo + ", " 
							+ fls_pro + "='TMP$'"
							+ " WHERE "
							+ fls_mun + "=" + codMunicipio + " AND "
							+ fls_pro + "='" + codProcedimiento + "' AND "
							+ fls_cts + "=" + codTraOrigen;
					log.debug("actualizarCodigosTramite sql: " + sql);

					ps = con.prepareStatement(sql);
					int resultado = ps.executeUpdate();
					log.debug("resultado update: " + resultado);
					ps.close();

				   String sql2=" UPDATE E_ENT SET ENT_CTR="+codTraNuevo+", ENT_PRO='TMP$' WHERE ENT_PRO='"+codProcedimiento+"' AND ENT_CTR="+codTraOrigen;
				   log.debug("actualizarCodigo de ENT_CTR de las condiciones de entrada: " + sql2);
				   ps2 = con.prepareStatement(sql2);
				   ps2.executeUpdate();
				   ps2.close();
			   }

             }
            
            String sql2=" UPDATE E_FLS SET FLS_PRO='"+codProcedimiento+"' WHERE FLS_PRO='TMP$'";
            log.debug("actualizarCodigo procedimiento de auxiliar al correcto sql: " + sql2);
            ps2 = con.prepareStatement(sql2);
            int resultado=ps2.executeUpdate();
            ps2.close();
            
            sql2=" UPDATE E_ENT SET ENT_PRO='"+codProcedimiento+"' WHERE ENT_PRO='TMP$'";
            log.debug("actualizarCodigo procedimiento de auxiliar al correcto sql: " + sql2);
            ps2 = con.prepareStatement(sql2);
            resultado=ps2.executeUpdate();
            ps2.close();
			log.debug("Fin actualizarCodigosTramite");

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(ps!=null) ps.close();
                if(ps2!=null) ps2.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }
    
    
    /**
     * Para cada trámite a importar se inserta el código de clasificación. Pero si este n existe en el entorno a importar se producirán errores en la visualización
     * Se actualizan los valores a una clasificación por defecto
     * @param tramites: Vector con los trámite
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return Vector<DefinicionTramitesValueObject>
     */
     public void actualizarClasificacionDefectoTramite(String codMunicipio,String codProcedimiento,Connection con) {


        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;

        try{

           String sql ="SELECT MIN (CML_COD)FROM "+GlobalNames.ESQUEMA_GENERICO+"A_CML";
           log.debug("actualizarClasificacionDefectoTramite sql, min cod clasificacion: " + sql);

           st = con.createStatement();
           rs = st.executeQuery(sql);
           int minCodClasif=1;
           while(rs.next()){
                    minCodClasif=rs.getInt(1);
                }
           
            
           String sql2=" UPDATE E_TRA SET TRA_CLS="+minCodClasif+" WHERE TRA_CLS NOT IN (SELECT CML_COD FROM "+GlobalNames.ESQUEMA_GENERICO+"A_CML where CML_LENG=1) AND TRA_MUN="+codMunicipio+" AND TRA_PRO='"+codProcedimiento+"'";
            log.debug("actualizarCodigo clasificacion tramite a por defecto: " + sql2);
            ps = con.prepareStatement(sql2);
            int resultado=ps.executeUpdate(); 
            ps.close();
            

        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(ps!=null) ps.close();
                if(st!=null) st.close();
               
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    /**
     * Para cada trámite a importar se obtiene el código de trámite que le corresponde en el nuevo entorno puesto que se está actualizando el trámite y el código interno del mismo
     * no tiene porque coincidir con el procedente de una nueva definición
     * @param tramites: Vector con los trámite
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return Vector<DefinicionTramitesValueObject>
     */
    public HashMap<String, String> actualizarTramiteConCodigoTramite(Vector<DefinicionTramitesValueObject> tramites,String codProcedimiento,Connection con) {

        Statement st = null;
        ResultSet rs = null;
        HashMap<String, String> relacionCodigosTramites = new HashMap<String, String>();
        String codigoAntiguo = null;
        
        try{
            for(int i=0;i<tramites.size();i++){
                 String codigoNuevo = null;
                 DefinicionTramitesValueObject t = tramites.get(i);
                
                String codVisibleTramite = t.getNumeroTramite();
                codigoAntiguo = t.getCodigoTramite();
				// Se obtiene el codigo de tramite interno relacionado al codigo visible de cada uno de los tramites importados
                String sql ="SELECT TRA_COD FROM E_TRA WHERE TRA_PRO='" + codProcedimiento + "' AND TRA_COU='" + codVisibleTramite + "'";
                log.debug("actualizarTramiteConCodigoTramite sql: " + sql);

                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    codigoNuevo = rs.getString("TRA_COD");
                    t.setCodigoTramite(codigoNuevo);
                    relacionCodigosTramites.put(codigoAntiguo, codigoNuevo);
                    log.debug("El trámite con codigo visible " + codVisibleTramite + " y nombre: " + t.getNombreTramite() + " tiene como código interno de trámite: " + t.getCodigoTramite());
                }
            }// for
        }catch(SQLException e){
            e.printStackTrace();

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return relacionCodigosTramites;
    }


    /**
     * Comprueba si existe la definición de un trámite. La comprobación se realiza por el código visible de trámite, por tanto este debe ser único.
     * @param defTramVO: Objeto con la definición del trámite
     * @param conexion: Conexión a la base de datos
     * @return Un boolean
     */
    public boolean existeDefinicionTramite(DefinicionTramitesValueObject defTramVO, Connection conexion){
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        String codVisibleTramite = defTramVO.getNumeroTramite();
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_TRA WHERE TRA_PRO='" + defTramVO.getTxtCodigo() + "' AND TRA_COU='" + defTramVO.getNumeroTramite()
                           + "' AND TRA_MUN=" + defTramVO.getCodMunicipio();

            int i = 1;
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                int num = rs.getInt("NUM");
                if(num>=1) exito = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }

   
    public int modificarTramite(DefinicionTramitesValueObject defTramVO,AdaptadorSQLBD abd, Connection conexion) throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        String sqlCond = "";
        String sqlCondSML =	"";
        String sqlBorrar = "";
        PreparedStatement ps = null;
        int res	= 0;
        int res2 = 0;
        int res3 = 0;
        int res4 = 0;
        int res5 = 0;
        int resTotal = 0;
        int resBorrar	=0;
        int resCondEnt = 0;
        int resEnl = 0;
        int resBorrarDoc = 0;
        int resBorrarPlt = 0;
        int resSML = 0;        
        ResultSet rs = null;
        if(log.isDebugEnabled()) log.debug("Entra	en el	modify del DAO");

        try{
            if(log.isDebugEnabled()) log.debug("entra en el modify");
            
            // creamos el ddepartamento de notificación que se exporta en el trámite en caso de no existir.
            DefinicionTramitesDAO.getInstance().comprobarErrorDepartamentoNotificacion(defTramVO, conexion);
            
            // PESTAÑA DE DATOS
            sql =	"UPDATE E_TRA SET	" + tra_cou	+ "="	+ defTramVO.getNumeroTramite() + "," +
                    tra_vis + "=" + defTramVO.getDisponible();

            if(defTramVO.getCodUnidadInicio() == null || defTramVO.getCodUnidadInicio().equals("")){
                sql	+= "," + tra_uin + "=null";
            }else if ("-99999".equals(defTramVO.getCodUnidadInicio()) || "-99998".equals(defTramVO.getCodUnidadInicio())) {
                    sql	+= "," + tra_uin + "=" + defTramVO.getCodUnidadInicio();
            }else{
                    sql	+= "," + tra_uin + "=" + this.recuperaCodigoUnidadInicio(defTramVO.getCodUnidadInicio(), conexion);
            }
            if(defTramVO.getCodUnidadTramite() ==null || defTramVO.getCodUnidadTramite().equals(""))
                sql	+= "," + tra_utr + "=null";
            else
                sql	+= "," + tra_utr + "=" + defTramVO.getCodUnidadTramite();
            if(defTramVO.getPlazo() == null ||defTramVO.getPlazo().equals(""))
                sql	+= "," + tra_plz + "=null" + "," + tra_und + "=null";
            else
                sql	+= "," + tra_plz + "=" + defTramVO.getPlazo() +
                        "," + tra_und +  "='" + defTramVO.getUnidadesPlazo() + "'";
            if(defTramVO.getOcurrencias()==null || defTramVO.getOcurrencias().equals(""))
                sql	+= "," + tra_ocu + "=null";
            else
                sql	+= "," + tra_ocu + "=" + defTramVO.getOcurrencias();

            if (defTramVO.getInstrucciones()!= null){
                sql += "," + tra_ins + "='" + defTramVO.getInstrucciones()+"' ";
                defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(defTramVO.getInstrucciones()));
            }
            if(defTramVO.getCodExpRel() == null || defTramVO.getCodExpRel().equals(""))
                sql	+= "," + tra_prr + "=null";
            else
                sql	+= "," + tra_prr + "='" + defTramVO.getCodExpRel() + "'";

            if(	defTramVO.getCodCargo() == null || defTramVO.getCodCargo().equals(""))
                sql	+= "," + tra_car + "=null";
            else{
               // sql	+= "," + tra_car + "='" + defTramVO.getCodCargo() + "'";
                /** Se comprueba si el código de cargo existe y se recupera el código interno del mismo */
                int codCargo = this.getCodigoAreaByCodVisible(defTramVO.getCodVisibleCargo(), conexion);
                log.debug("Para el cargo " + defTramVO.getCodCargo() + " y código visible: " + defTramVO.getCodVisibleCargo() + " le corresponde en el entorno actual el cargo: " + codCargo);
                if(codCargo!=-1)                    
                    sql	+= "," + tra_car + "='" + codCargo + "'";
                else
                    sql	+= "," + tra_car + "=null";
            }

            
            sql += "," + tra_cls + "=" + defTramVO.getCodClasifTramite() + "," +
                    tra_pre +  "=" + defTramVO.getTramitePregunta();

            // NOTIFICACIONES
            sql += "," + tra_uti + "='" + defTramVO.getNotUnidadTramitIni() + "'";
            sql += "," + tra_utf + "='" + defTramVO.getNotUnidadTramitFin() + "'";
            sql += "," + tra_usi + "='" + defTramVO.getNotUsuUnidadTramitIni() + "'";
            sql += "," + tra_usf + "='" + defTramVO.getNotUsuUnidadTramitFin() + "'";
            sql += "," + tra_ini + "='" + defTramVO.getNotInteresadosIni() + "'";
            sql += "," + tra_inf + "='" + defTramVO.getNotInteresadosFin() + "'";
            
            
            if ((defTramVO.getNotUsuInicioTramiteIni() != null) &&
            (!defTramVO.getNotUsuInicioTramiteIni().equals(""))) 
                sql += ", TRA_NOTIF_UITI ='" + defTramVO.getNotUsuInicioTramiteIni() +"'";
            else  sql += ", TRA_NOTIF_UITI ='N'";
            
            if ((defTramVO.getNotUsuInicioTramiteFin() != null) &&
            (!defTramVO.getNotUsuInicioTramiteFin().equals(""))) 
                sql += ", TRA_NOTIF_UITF ='" + defTramVO.getNotUsuInicioTramiteFin() +"'";
            else  sql += ", TRA_NOTIF_UITF ='N'";
            
            if ((defTramVO.getNotUsuInicioExpedIni() != null) &&
            (!defTramVO.getNotUsuInicioExpedIni().equals(""))) 
                sql += ", TRA_NOTIF_UIEI ='" + defTramVO.getNotUsuInicioExpedIni() +"'";
            else  sql += ", TRA_NOTIF_UIEI ='N'";
            
            if ((defTramVO.getNotUsuInicioExpedFin() != null) &&
            (!defTramVO.getNotUsuInicioExpedFin().equals(""))) 
                sql += ", TRA_NOTIF_UIEF ='" + defTramVO.getNotUsuInicioExpedFin() +"'";
            else  sql += ", TRA_NOTIF_UIEF ='N'";
            
            sql += ", TRA_FIN =" + defTramVO.getPlazoFin();
            sql += ", TRA_NOTCERCAFP =?";
            sql += ", TRA_NOTFUERADP =?";
            sql += ", TRA_TIPNOTCFP =?";
            sql += ", TRA_TIPNOTFDP =?";
            sql += ", TRA_GENERARPLZ = ?";
            sql += ", TRA_NOTIFICADO = ?";
            sql += ", TRA_NOTIFICACION_ELECTRONICA = ?";
            sql += ", TRA_COD_TIPO_NOTIFICACION = ?";
            sql += ", COD_DEPTO_NOTIFICACION = ?";
            sql += ", TRA_NOTIF_ELECT_OBLIG = ?";
            sql += ", TRA_NOTIF_FIRMA_CERT_ORG = ?";


            // INFORMACIÓN SERVICIOS WEB
            sql += " WHERE " +	tra_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+
                    tra_pro + "='" + defTramVO.getTxtCodigo()	+ "' AND " + tra_cod + "=" + defTramVO.getCodigoTramite();

            if(log.isDebugEnabled()) log.debug(sql);
            ps = conexion.prepareStatement(sql);

            int i=1;
            ps.setBoolean(i++, defTramVO.getNotificarCercaFinPlazo());
            ps.setBoolean(i++,defTramVO.getNotificarFueraDePlazo());
            ps.setInt(i++, defTramVO.getTipoNotCercaFinPlazo());
            ps.setInt(i++, defTramVO.getTipoNotFueraDePlazo());
            ps.setBoolean(i++, defTramVO.isGenerarPlazos());
            ps.setInt(i++, (defTramVO.isTramiteNotificado()?1:0));
            ps.setInt(i++, "1".equals(defTramVO.getAdmiteNotificacionElectronica()) ? 1 : 0);
            ps.setString(i++, defTramVO.getCodigoTipoNotificacionElectronica());
            ps.setString(i++, defTramVO.getCodDepartamentoNotificacion());
            ps.setBoolean(i++, defTramVO.getNotificacionElectronicaObligatoria());
            ps.setBoolean(i++, defTramVO.getCertificadoOrganismoFirmaNotificacion());
            
            res = ps.executeUpdate();
            ps.close();
            if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el modify de E_TRA son	:::::::::::::: : " + res);

            sql =	"UPDATE E_TML SET	" + tml_valor + "='" + defTramVO.getNombreTramite() +	"'" +
                    " WHERE " +	tml_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+
                    tml_pro + "='" + defTramVO.getTxtCodigo()	+ "' AND " + tml_tra + "=" + defTramVO.getCodigoTramite() +
                    " AND	" + tml_cmp	+ "='NOM' AND " +	tml_leng + "='" + idiomaDefecto + "'";

            if(log.isDebugEnabled()) log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res3 = ps.executeUpdate();
            ps.close();
            if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el modify de E_TML son	:::::::::::::: : " + res3);

            sql =	"SELECT " +	pro_tri + "	FROM E_PRO WHERE " + pro_mun + "=" + defTramVO.getCodMunicipio() +
                    " AND	" + pro_cod	+ "='" + defTramVO.getTxtCodigo() +	"'";
            if(log.isDebugEnabled()) log.debug(sql);
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            String codTramiteInicio	= "";
            while(rs.next()) {
                codTramiteInicio = rs.getString(pro_tri);
            }
            rs.close();
            ps.close();

            if(defTramVO.getTramiteInicio().equals("1")) {
                sql	= "UPDATE E_PRO SET " +	pro_tri + "=" + defTramVO.getCodigoTramite() + " WHERE " +	pro_mun + "=" +
                        defTramVO.getCodMunicipio()	+ " AND " +	pro_cod + "='" + defTramVO.getTxtCodigo()	+ "'";

                if(log.isDebugEnabled()) log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res5 = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas	en el	update de E_PRO son :::::::::::::: : " + res5);
            }

            if(codTramiteInicio != null) {
                if(defTramVO.getTramiteInicio().equals("0") && codTramiteInicio.equals(defTramVO.getCodigoTramite())) {
                    sql = "UPDATE	E_PRO	SET "	+ pro_tri +	"=null" + "	WHERE	" +  pro_mun + "=" +
                            defTramVO.getCodMunicipio() + "	AND "	+ pro_cod +	"='" + defTramVO.getTxtCodigo() + "'";

                    if(log.isDebugEnabled()) log.debug(sql);
                    ps = conexion.prepareStatement(sql);
                    res5 = ps.executeUpdate();
                    ps.close();
                    if(log.isDebugEnabled()) log.debug("las filas afectadas en el update de	E_PRO	son :::::::::::::: : " + res5);
                }
            }

            if(res !=0)	{
                    
                // PESTAÑA DE DOCUMENTOS
                Vector listaCodigosDoc = defTramVO.getListaCodigosDoc();
                Vector listaVisibleDoc = defTramVO.getListaVisibleDoc();
                Vector listaPlantillaNomes = defTramVO.getListaPlantillaDoc();
                Vector listaCodPlantilla = defTramVO.getListaCodPlantilla();
                Vector listaContPlantilla = defTramVO.getListaContidoPlantilla();
                Vector listaRelPlantilla = defTramVO.getListaRelacionPlantilla();
                Vector listaIntPlantilla = defTramVO.getListaInteresadoPlantilla();
                Vector listaFirmaPlantilla = defTramVO.getListaFirmaDoc();
                Vector listaDocActivos = defTramVO.getListaDocActivos();
                Vector listaEditoresTexto = defTramVO.getListaEditoresTexto();
                
                /* Realizamos la comprobación de que campos pueden ser borrados y cuales no */
                HashMap insertar = new HashMap();
                boolean inserta = true;
                PreparedStatement statExiste = null;
                ResultSet rsExiste = null;
                for(int j=0;j<listaCodigosDoc.size();j++){
                    inserta = true;
                    /* Comprobamos si el codigo del documento existe en otras tablas */
                    if(log.isDebugEnabled()) log.debug("CAMPO ACTIVO = " + (String)listaDocActivos.elementAt(j));
                    if ("NO".equalsIgnoreCase((String)listaDocActivos.elementAt(j))) {
                        String sqlExiste = "SELECT COUNT (*)  FROM e_dot WHERE e_dot.dot_cod = '" + listaCodigosDoc.elementAt(j)  + "'";
                        sqlExiste += " AND e_dot.dot_pro = '" + defTramVO.getTxtCodigo() + "'";
                        sqlExiste += " AND e_dot.dot_tra = " + defTramVO.getCodigoTramite();
                        sqlExiste += " AND e_dot.dot_mun = '" + defTramVO.getCodMunicipio() + "'";
                        sqlExiste += " AND e_dot.dot_cod IN (SELECT crd_dot FROM e_crd WHERE crd_pro = dot_pro AND crd_tra = dot_tra AND crd_mun = dot_mun)";
                        if(log.isDebugEnabled()) log.debug("CONSULTA = " + sqlExiste);
                        statExiste = conexion.prepareStatement(sqlExiste);
                        rsExiste = statExiste.executeQuery();
                        if (rsExiste.next()) {
                            if (rsExiste.getInt(1) <= 0) { // No existen campos en otras tablas, SE DEBE BORRAR EL DOCUMENTO FISICAMENTE DE LA TABLA
                                inserta = false;
                            } //si es mayor que 0,se debe volver a insertar
                        }
                        rsExiste.close();
                        statExiste.close();
                    }
                    insertar.put((String)listaCodigosDoc.elementAt(j),new Boolean(inserta));
                    if(log.isDebugEnabled()) log.debug("EL RESULTADO DE LA CONSULTA, se inserta documento ? = " + insertar);
                }
                /* Fin de la comprobación de los campos que pueden ser borrados y cuales no*/
                
                sqlBorrar	= "DELETE FROM E_DOT WHERE " +  dot_mun +	"=" +	defTramVO.getCodMunicipio() +	" AND	" + dot_pro	+ "='" +
                        defTramVO.getTxtCodigo() + "' AND " +	dot_tra + "=" + defTramVO.getCodigoTramite();
                if(log.isDebugEnabled()) log.debug(sqlBorrar);
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrarDoc = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas	en el eliminar de E_DOT	son :::::::::::::: : " + resBorrarDoc);

                sqlBorrar = "DELETE FROM A_PLT WHERE PLT_APL = 4 AND PLT_PRO = '" +  defTramVO.getTxtCodigo() +
                        "' AND PLT_TRA = " + defTramVO.getCodigoTramite();
                if(log.isDebugEnabled()) log.debug(sqlBorrar);
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrarPlt = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de A_PLT	son :::::::::::::: : " + resBorrarPlt);

                // Borra las entradas de documentos dependientes de un procedimiento
                borrarTablasPestanaDocumento(defTramVO.getCodMunicipio(), defTramVO.getTxtCodigo(), 
                        defTramVO.getCodigoTramite(), conexion);
                
                Boolean seInserta;
                if(log.isDebugEnabled()) log.debug("el tamaío de la lista de codigos de documento es : " + listaCodigosDoc.size());
                for(int l=0;l<listaCodigosDoc.size();l++) {
                    if(log.isDebugEnabled()) log.debug("en el for");
                    seInserta = (Boolean)insertar.get((String)listaCodigosDoc.elementAt(l));
                    
                    if (seInserta.booleanValue()) {
                        Integer codPlantilla = 0;
                        if (listaPlantillaNomes.elementAt(l) != null && !"".equals(listaPlantillaNomes.elementAt(l))) {                    
                            sql = "select " + abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[] {abd.funcionMatematica(
                            AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[] {aplt_cod}) + "+1", "1"}) + " as " +
                            aplt_cod + " from A_PLT";
                            if(log.isDebugEnabled()) log.debug(sql);
                            ps = conexion.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                codPlantilla = rs.getInt(aplt_cod);
                            }
                            rs.close();
                            ps.close();
                        }
                        
						// Se almacena el tipo de firma en una variable porque se va a usar repetidas veces
						String tipoFirma = (String) listaFirmaPlantilla.elementAt(l);
						
                        sql = "INSERT INTO E_DOT (" + dot_mun + "," +	dot_pro + "," + dot_tra	+ "," + dot_cod + ", DOT_TDO, " +
                            dot_vis + ", DOT_FRM, " + dot_plt + "," + dot_activo + ") VALUES(" +defTramVO.getCodMunicipio() +",'" +
                            defTramVO.getTxtCodigo() + "'," + defTramVO.getCodigoTramite() + "," + listaCodigosDoc.elementAt(l) + ", null, '"
                            + listaVisibleDoc.elementAt(l) + "',";
                        if (listaFirmaPlantilla.elementAt(l)!=null) 
                            sql +="'"+listaFirmaPlantilla.elementAt(l)+"',";
                        else 
                            sql +="NULL,";
                        if(listaPlantillaNomes.elementAt(l) == null || "".equals(listaPlantillaNomes.elementAt(l))) {
                            sql += "null,";
                        } else {
                            sql += codPlantilla + ",";
                        }
                        sql += "'" + listaDocActivos.elementAt(l) + "')";
                        if(log.isDebugEnabled()) log.debug("el Insert en E_DOT es : " + sql);
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();
                        ps.close();
                        
                        if (listaPlantillaNomes.elementAt(l) != null && !"".equals(listaPlantillaNomes.elementAt(l))) {
                            sql = "INSERT INTO A_PLT (PLT_COD,PLT_DES,PLT_APL,PLT_DOC,PLT_PRO,PLT_TRA,PLT_INT," + 
                                    "PLT_REL,PLT_EDITOR_TEXTO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                            if(log.isDebugEnabled()) log.debug(sql);
                            ps = conexion.prepareStatement(sql);
                            int ind=1;
                            ps.setInt(ind++,codPlantilla);
                            ps.setString(ind++,(String)listaPlantillaNomes.elementAt(l));
                            ps.setInt(ind++, 4); 
                            String contPlantilla = (String)listaContPlantilla.elementAt(l);
                            if (contPlantilla!=null) {
                                byte[] documento = Base64.decodeBase64(contPlantilla.getBytes());
                                java.io.InputStream ist = new java.io.ByteArrayInputStream(documento);
                                ps.setBinaryStream(ind++,ist,documento.length);
                            } else {
                                ps.setNull(ind++, java.sql.Types.BLOB);
                            }
                            ps.setString(ind++,defTramVO.getTxtCodigo());
                            ps.setInt(ind++, Integer.parseInt(defTramVO.getCodigoTramite()));
                            ps.setString(ind++,(String)listaRelPlantilla.elementAt(l));
                            ps.setString(ind++,(String)listaIntPlantilla.elementAt(l));
                            ps.setString(ind++,(String)listaEditoresTexto.elementAt(l));
                            
                            ps.executeUpdate();
                            ps.close();
                        }

                        if (tipoFirma != null && !tipoFirma.equals("") && !tipoFirma.equals("T")) {
                            // Si el documento tiene un tipo de firma en E_DOT.DOT_FRM no nulo, no vacio y distinto de T (firma tipo tramitador: no soportada)
                            // se inserta los datos de la firma en E_DOT_FIR
                            insertarFirmaDocumento(defTramVO.getCodMunicipio(), defTramVO.getTxtCodigo(),
                                    defTramVO.getCodigoTramite(), (String) listaCodigosDoc.elementAt(l),
                                    defTramVO.getListaFirmasFlujo().get(l), defTramVO.getListaFirmasDocumentoIdsUsuario().get(l),
                                    defTramVO.getListaFirmasDocumentoLogsUsuario().get(l), defTramVO.getListaFirmasDocumentoDnisUsuario().get(l), conexion);

                        } else if (tipoFirma == null || tipoFirma.equals("") || tipoFirma.equals("T")) {
                            borrarFirmaDocumento(defTramVO.getCodMunicipio(), defTramVO.getTxtCodigo(), defTramVO.getCodigoTramite(), (String) listaCodigosDoc.elementAt(l), conexion);
                        }
                    }
                }

                // PESTAÑA DE CONDICIONES DE ENTRADA
               
                Vector listaTramitesTablaEntrada = defTramVO.getListaTramitesTabla();
                Vector listaCodTramitesTablaEntrada =	defTramVO.getListaCodTramitesTabla();
                Vector listaEstadoTablaEntrada = defTramVO.getListaEstadosTabla();
                Vector listaTipoTablaEntrada = defTramVO.getListaTiposTabla();
                Vector listaExpresionesTablaEntrada = defTramVO.getListaExpresionesTabla();
                Vector listaCodigoDocumentoEntrada = defTramVO.getListaCodigosDocTabla();
                
                sqlBorrar = "DELETE FROM E_ENT WHERE " + ent_mun + "=" + defTramVO.getCodMunicipio() + " AND " + ent_pro + "='"
                        + defTramVO.getTxtCodigo() + "' AND " + ent_tra + "=" + defTramVO.getCodigoTramite();
                if (log.isDebugEnabled()) {
                    log.debug(sqlBorrar);
                }
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                ps.close();
                if (log.isDebugEnabled()) {
                    log.debug("las filas afectadas	en el	eliminar de	E_ENT	son :::::::::::::: : " + resBorrar);
                }
                
                for(int	j=0;j<listaTramitesTablaEntrada.size();j++){
                    if(listaTipoTablaEntrada.elementAt(j).equals("TRÁMITE")) {
                    sql =	"INSERT INTO E_ENT (" +	ent_mun + "," + ent_pro	+ ","	+ ent_tra +	"," +	ent_cod +
                    "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + ") VALUES("	+ defTramVO.getCodMunicipio() + ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
                    //listaCodCondicionTablaEntrada.elementAt(j)
                    (j+1)  + "," + listaCodTramitesTablaEntrada.elementAt(j);
                    if(listaEstadoTablaEntrada.elementAt(j).equals("FINALIZADO")) {
                          sql += ",'F'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("INICIADO")) {
                          sql += ",'I'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("NO INICIADO")) {
                          sql += ",'O'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("FAVORABLE")) {
                          sql += ",'S'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("DESFAVORABLE")) {
                          sql += ",'N'";
                    } else {
                          sql += ",'C'";
                    }
                      sql += ",'T')";
                    }else if(listaTipoTablaEntrada.elementAt(j).equals("DOCUMENTO")) {
                    sql =	"INSERT INTO E_ENT (" +	ent_mun + "," + ent_pro	+ ","	+ ent_tra +	"," +	ent_cod +
                    "," + ent_ctr	+ ","	+ ent_est +"," + ent_tipo +",ENT_DOC) VALUES("	+ defTramVO.getCodMunicipio() + ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
                    //listaCodCondicionTablaEntrada.elementAt(j)
                    (j+1)  + ",0";
                                
                    if(listaEstadoTablaEntrada.elementAt(j).equals("FIRMADO")) {
                          sql += ",'F'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("PENDIENTE")) {
                          sql += ",'O'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("RECHAZADO")) {
                          sql += ",'R'";
                    } else if(listaEstadoTablaEntrada.elementAt(j).equals("CIRCUITO FINALIZADO")) {
                          sql += ",'C'";
                    } 
                                                                                
                      sql += ",'D',"+listaCodigoDocumentoEntrada.elementAt(j)+")";
                    } else { //tipo Expresion
                    sql = "INSERT	INTO E_ENT (" + ent_mun	+ ","	+ ent_pro +	"," +	ent_tra + "," + ent_cod	+
                        "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + "," + ent_exp + ") VALUES("+ defTramVO.getCodMunicipio()	+ ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," + (j+1);
                    String aux =(String) listaExpresionesTablaEntrada.elementAt(j);
                    aux = aux.replaceAll("&lt;","<");
                    aux = aux.replaceAll("&gt;",">");
                    aux = aux.replaceAll("&#39;","'");

                    sql += ", 0 , ' ' ,'E'," + abd.addString(aux) + ")";
                    }

                    if(log.isDebugEnabled()) log.debug(sql);

                    ps = conexion.prepareStatement(sql);
                    resCondEnt += ps.executeUpdate();
                    ps.close();
                }
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_ENT son resCondEnt:::::::::::::: : "	+ resCondEnt);
                if(listaTramitesTablaEntrada.size() == 0 ) {
                    resCondEnt = 1;
                }

                // PESTAÑA DE ENLACES
                Vector listaCodigoEnlaces = defTramVO.getListaCodigoEnlaces();
                Vector listaDescripcionEnlaces =	defTramVO.getListaDescripcionEnlaces();
                Vector listaUrlEnlaces = defTramVO.getListaUrlEnlaces();
                Vector listaEstadoEnlaces = defTramVO.getListaEstadoEnlaces();
                sqlBorrar	= "DELETE FROM E_TEN WHERE " + ten_mun + "=" + defTramVO.getCodMunicipio() + " AND " + ten_pro + "='"	+
                        defTramVO.getTxtCodigo() + "' AND " + ten_tra + "=" + defTramVO.getCodigoTramite();
                if(log.isDebugEnabled()) log.debug(sqlBorrar);
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas	en el	eliminar de	E_TEN	son :::::::::::::: : " + resBorrar);

                for(int j=0;j<listaCodigoEnlaces.size();j++) {
                    sql =	"INSERT INTO E_TEN (" +	ten_mun + "," + ten_pro	+ ","	+ ten_tra +	"," +	ten_cod +
                            ","	+ ten_des +	"," +	ten_url + "," +	ten_est + ") VALUES(" +	defTramVO.getCodMunicipio() +	",'" +
                            defTramVO.getTxtCodigo() + "'," + defTramVO.getCodigoTramite() + "," +
                            listaCodigoEnlaces.elementAt(j) + ",'" + listaDescripcionEnlaces.elementAt(j) + "','" +
                            listaUrlEnlaces.elementAt(j) + "'," + listaEstadoEnlaces.elementAt(j)+")";
                    if(log.isDebugEnabled()) log.debug(sql);

                    ps = conexion.prepareStatement(sql);
                    resEnl += ps.executeUpdate();
                    ps.close();
                }
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_TEN son resEnl:::::::::::::: : "	+ resEnl);
                if(listaCodigoEnlaces.size() == 0 ) {
                    resEnl = 1;
                }

                // PESTAÑA DE CONDICIONES DE SALIDA

                String tipoCondicion = defTramVO.getTipoCondicion();
                String tipoFavorableSI = defTramVO.getTipoFavorableSI();
                String tipoFavorableNO = defTramVO.getTipoFavorableNO();
                String tFSI ="";
                String tFNO = "";
                if(tipoFavorableSI !=	null)
                    tFSI = tipoFavorableSI.substring(0,1);
                if(tipoFavorableNO !=	null)
                    tFNO = tipoFavorableNO.substring(0,1);

                if("Tramite".equals(tipoCondicion)) {
                    sqlCond	= "UPDATE E_SAL SET " +	sal_mun + "=" + defTramVO.getCodMunicipio() + "," + sal_pro	+
                            "='" + defTramVO.getTxtCodigo() + "'," + sal_tra + "=" + defTramVO.getCodigoTramite() + "," +
                            sal_tac + "='T',"	+ sal_taa +	"=''," + sal_tan + "=''" + " WHERE " +
                            sal_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+ sal_pro +
                            "='" + defTramVO.getTxtCodigo() + "' AND " + sal_tra + "=" + defTramVO.getCodigoTramite();
                }
                if("Finalizacion".equals(tipoCondicion)) {
                    sqlCond	= "UPDATE E_SAL SET " +	sal_mun + "=" + defTramVO.getCodMunicipio() + "," + sal_pro	+
                            "='" + defTramVO.getTxtCodigo() + "'," + sal_tra + "=" + defTramVO.getCodigoTramite() + "," +
                            sal_tac + "='F',"	+ sal_taa +	"=''," + sal_tan + "=''" + " WHERE " +
                            sal_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+ sal_pro +
                            "='" + defTramVO.getTxtCodigo() + "' AND " + sal_tra + "=" + defTramVO.getCodigoTramite();
                }
                if("Pregunta".equals(tipoCondicion)) {
                    sqlCond	= "UPDATE E_SAL SET " +	sal_mun + "=" + defTramVO.getCodMunicipio() + "," + sal_pro	+
                            "='" + defTramVO.getTxtCodigo() + "'," + sal_tra + "=" + defTramVO.getCodigoTramite() + "," +
                            sal_tac + "='P',"	+ sal_taa +	"='" + tFSI	+ "'," + sal_tan + "='"	+
                            tFNO +"'" +	" WHERE " +	sal_mun + "=" + defTramVO.getCodMunicipio() +
                            " AND	" + sal_pro	+ "='" + defTramVO.getTxtCodigo() +	"' AND " + sal_tra + "=" +
                            defTramVO.getCodigoTramite();
                    sqlCondSML = "UPDATE E_SML SET " + sml_valor + "='" + defTramVO.getTexto() +
                            "'  WHERE " + sml_mun + "=" + defTramVO.getCodMunicipio() +
                            " AND " + sml_pro + "='"	+ defTramVO.getTxtCodigo() + "' AND	" + sml_tra	+ "="	+
                            defTramVO.getCodigoTramite() +	" AND	" + sml_cmp	+ "='TXT' AND " +
                            sml_leng + "='" + idiomaDefecto + "'";
                }
                if("Resolucion".equals(tipoCondicion)) {
                    sqlCond	= "UPDATE E_SAL SET " +	sal_mun + "=" + defTramVO.getCodMunicipio() + "," + sal_pro	+
                            "='" + defTramVO.getTxtCodigo() + "'," + sal_tra + "=" + defTramVO.getCodigoTramite() + "," +
                            sal_tac + "='R',"	+ sal_taa +	"='" + tFSI	+ "'," + sal_tan + "='"	+
                            tFNO +"'" +	" WHERE " +	sal_mun + "=" + defTramVO.getCodMunicipio() +
                            " AND	" + sal_pro	+ "='" + defTramVO.getTxtCodigo() +	"' AND " + sal_tra + "=" +
                            defTramVO.getCodigoTramite();
                }

                if("sinCondicion".equals(tipoCondicion)) {
                    sqlCond	= "UPDATE E_SAL SET " +	sal_mun + "=" + defTramVO.getCodMunicipio() + "," + sal_pro	+
                            "='" + defTramVO.getTxtCodigo() + "'," + sal_tra + "=" + defTramVO.getCodigoTramite() + "," +
                            sal_tac + "=''," + sal_taa + "='',"	+ sal_tan +	"=''"	+
                            " WHERE " +	sal_mun + "=" + defTramVO.getCodMunicipio() +
                            " AND	" + sal_pro	+ "='" + defTramVO.getTxtCodigo() +	"' AND " + sal_tra + "=" +
                            defTramVO.getCodigoTramite();
                }

                if(log.isDebugEnabled()) log.debug(sqlCond);
                ps = conexion.prepareStatement(sqlCond);
                res2 = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas	en el	UPDATE de E_SAL son :::::::::::::: : " + res2);
                if(res2 !=0) {
                    if(!"".equals(sqlCondSML)) {
                        if(log.isDebugEnabled()) log.debug(sqlCondSML);
                        ps = conexion.prepareStatement(sqlCondSML);
                        res4 = ps.executeUpdate();
                        ps.close();
                        if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el UPDATE de E_SML son	:::::::::::::: : " + res4);
                    }
                    if(res4	==0 && "Pregunta".equals(tipoCondicion) )	{
                        sqlCondSML = "INSERT INTO E_SML (" + sml_mun + "," + sml_pro + "," + sml_tra + "," +
                                sml_cmp + "," + sml_leng + "," + sml_valor + ") VALUES(" +
                                defTramVO.getCodMunicipio() + ",'"	+ defTramVO.getTxtCodigo() + "'," +
                                defTramVO.getCodigoTramite()	+ ",'TXT','" + idiomaDefecto + "','" + defTramVO.getTexto() +	"')";
                        if(log.isDebugEnabled()) log.debug(sqlCondSML);
                        ps = conexion.prepareStatement(sqlCondSML);
                        res4 = ps.executeUpdate();
                        ps.close();
                        if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el INSERT de E_SML son	:::::::::::::: : " + res4);
                    }
                } else {
                    if("Tramite".equals(tipoCondicion)) {
                        sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                                sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                                defTramVO.getTxtCodigo() + "',"	+ defTramVO.getCodigoTramite() + ",'T','','')" ;
                    }
                    if("Finalizacion".equals(tipoCondicion)) {
                        sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                                sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                                defTramVO.getTxtCodigo() + "',"	+ defTramVO.getCodigoTramite() + ",'F','','')";
                    }
                    if("Pregunta".equals(tipoCondicion)) {
                        sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                                sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                                defTramVO.getTxtCodigo() + "',"	+ defTramVO.getCodigoTramite() + "," +
                                "'P','"	+ tFSI + "','" + tFNO +"')";
                        sqlCondSML = "INSERT INTO E_SML (" + sml_mun + "," + sml_pro + "," + sml_tra + "," +
                                sml_cmp + "," + sml_leng + "," + sml_valor + ") VALUES(" +
                                defTramVO.getCodMunicipio() + ",'"	+ defTramVO.getTxtCodigo() + "'," +
                                defTramVO.getCodigoTramite()	+ ",'TXT','" + idiomaDefecto + "','" + defTramVO.getTexto() +	"')";
                    }

                    if("sinCondicion".equals(tipoCondicion)) {
                        sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                                sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                                defTramVO.getTxtCodigo() + "',"	+ defTramVO.getCodigoTramite() + "," +
                                "'','','')";
                    }

                    if(log.isDebugEnabled()) log.debug(sqlCond);
                    ps = conexion.prepareStatement(sqlCond);
                    res2 = ps.executeUpdate();
                    ps.close();
                    if(log.isDebugEnabled()) log.debug("las filas afectadas en el INSERT de	E_SAL	son :::::::::::::: : " + res2);

                    if(!sqlCondSML.equals("")) {
                        if(log.isDebugEnabled()) log.debug(sqlCondSML);
                        ps = conexion.prepareStatement(sqlCondSML);
                        resSML = ps.executeUpdate();
                        ps.close();
                        if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el INSERT de E_SML son	:::::::::::::: : " + resSML);
                    }
                }

            }

            this.tratarCamposSuplementariosTramite(defTramVO,defTramVO.getTxtCodigo(),defTramVO.getCodMunicipio(), defTramVO.getCodigoTramite(), conexion);

            this.prepararGrabacionFlujoSalida(defTramVO,conexion);

            if(res != 0	&& res2 !=0	&& resCondEnt !=0	&& resEnl !=0){
                resTotal = 1;
            } else {                
                log.error("MODIFICACION NO HECHA EN " + getClass().getName());
                log.debug("res VALE: " + res + " res2 VALE: " + res2 + " resCondEnt VALE: " + resCondEnt  +
                        " resEnl VALE: " + resEnl);
                
            }


            try{
                // Grabar las condiciones de salida del trámite
                prepararGrabacionFlujoSalida (defTramVO,conexion);
                
            }catch(Exception e){
                log.error(" ******************* error: " + e.getMessage());
                e.printStackTrace();
                resTotal = -1;
            }

        } catch(Exception ex)	{
            ex.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            try{
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
            } catch(Exception ex)	{
                ex.printStackTrace();
               log.error("Exception: " + ex.getMessage());
            }
        }
        return resTotal;
    }

    /**
     * Borra las tablas relacionadas con los Documentos de un Trámite.
     * 
     * @param codigoMunicipio
     * @param codigoProcedimiento
     * @param codigoTramite 
     */
    private void borrarTablasPestanaDocumento(String codigoMunicipio, String codigoProcedimiento, String codigoTramite,
            Connection conexion) throws SQLException {
        log.debug("Comienza borrarTablasPestanaDocumento");
        SqlBuilder sql = new SqlBuilder();
        
        // Preparar SELECT para recoger los códigos de firmas que serán borradas
        sql.select("DOT_FLUJO").from("E_DOT_FIR")
                .whereEqualsParametrizado(dot_mun)
                .andEqualsParametrizado(dot_pro)
                .andEqualsParametrizado(dot_tra);
        PreparedStatement ps = conexion.prepareStatement(sql.toString());
        JdbcOperations.setValues(ps, 1, codigoMunicipio, codigoProcedimiento, codigoTramite);
        ResultSet resultado = ps.executeQuery();
        
        List<String> codigosFirmas = new ArrayList<String>();
        while (resultado.next()) {
            codigosFirmas.add(resultado.getString("DOT_FLUJO"));
        }
        
        // Se borran en orden por dependencia
        if (codigosFirmas != null && !codigosFirmas.isEmpty()) {
            borrarFirmaCircuito(codigosFirmas, conexion);
            borrarFirmaFlujo(codigosFirmas, conexion);
        }
        borrarFirmasTramite(codigoMunicipio, codigoProcedimiento, codigoTramite, conexion);
        
    }

    /**
     * Borra las Firmas de un Documento de un Trámite.
     * 
     * @param codigoMunicipio
     * @param codigoProcedimiento
     * @param codigoTramite
     * @param conexion
     * @throws SQLException 
     */
    private void borrarFirmasTramite(String codigoMunicipio, String codigoProcedimiento, 
            String codigoTramite, Connection conexion)throws SQLException {
        // Construir y ejecutar SQL
        SqlBuilder sql = new SqlBuilder();
        sql.delete().from("E_DOT_FIR")
                .whereEqualsParametrizado(dot_mun)
                .andEqualsParametrizado(dot_pro)
                .andEqualsParametrizado(dot_tra);
        PreparedStatement pst = null;
        
        try {
            pst = conexion.prepareStatement(sql.toString());

            // Debug
            if (log.isDebugEnabled()) {
                log.debug(sql.toString());
            }
            JdbcOperations.setValues(pst, 1, codigoMunicipio, codigoProcedimiento, codigoTramite);
            int filasAfectadas = pst.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug(String.format("Filas borradas de E_DOT_FIR: %d", filasAfectadas));
            }
        } catch (SQLException e) {
            throw(e);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }
    
    /**
     * Borra la FirmaFlujo de un Documento de un Trámite.
     * 
     * @param idFirmasFlujo
     * @param conexion
     * @throws SQLException 
     */
    private void borrarFirmaFlujo(List<String> idFirmasFlujo, Connection conexion) throws SQLException {
        // Construir y ejecutar SQL
        SqlBuilder sql = new SqlBuilder();
        sql.delete().from("FIRMA_FLUJO")
                .whereInParametrizado("ID", idFirmasFlujo);
        PreparedStatement pst = conexion.prepareStatement(sql.toString());
        
        // Debug
        if (log.isDebugEnabled()) {
            log.debug(sql.toString());
        }
        try {
            JdbcOperations.setValues(pst, 1, idFirmasFlujo);
            int filasAfectadas = pst.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug(String.format("Filas borradas de FIRMA_FLUJO: %d", filasAfectadas));
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     * Borra la FirmaCircuito de un Documento de un Trámite.
     * 
     * @param idFirmasFlujo
     * @param conexion
     * @throws SQLException 
     */
    private void borrarFirmaCircuito(List<String> idFirmasFlujo, Connection conexion) throws SQLException {
        // Construir y ejecutar SQL
        SqlBuilder sql = new SqlBuilder();
        sql.delete().from("FIRMA_CIRCUITO")
                .whereInParametrizado("ID_FIRMA_FLUJO", idFirmasFlujo);
        
        PreparedStatement pst = null;
        try {
            pst = conexion.prepareStatement(sql.toString());

            if (log.isDebugEnabled()) {
                log.debug(sql.toString());
            }

            JdbcOperations.setValues(pst, 1, idFirmasFlujo);
            int filasAfectadas = pst.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug(String.format("Filas borradas de FIRMA_CIRCUITO: %d", filasAfectadas));
            }
        } catch (SQLException e) {
            throw(e);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }
    
	/**
     * Inserta la Firma del Documento de un Trámite  (E_DOT_FIR). 
     * 
     * @param codigoMunicipio
     * @param codigoProcedimiento
     * @param codigoTramite
     * @param codigoDocumento
     * @param firmaFlujo
     * @param firmaCircuito
     * @param conexion
     * @throws SQLException 
     */
    private void insertarFirmaDocumento(String codigoMunicipio, String codigoProcedimiento, String codigoTramite, 
            String codigoDocumento, FirmaFlujoVO firmaFlujo, String firmaDocumentoUsuarioId, String firmaDocumentoUsuarioLog, String firmaDocumentoDniUsuario,
			Connection conexion) throws SQLException {
        log.debug("Comienza insertarFirmaDocumento");
        String codigoFirma = null;
        
        if (firmaFlujo != null && firmaFlujo.getId() != null) {
            codigoFirma = String.valueOf(firmaFlujo.getId());
			firmaDocumentoUsuarioId = "-1"; // Si existe un flujo el codigo de usuario en E_DOT_FIR es -1
        } else if(firmaDocumentoUsuarioId == null) {
			firmaDocumentoUsuarioId = "-1"; // Si no existe un flujo se supone que debe existir el codigo de usuario en E_DOT_FIR
											// Pero si no es así y ambos valores son nulos el codigo de  usuario se establece a -1
		}
        
		insertarFirmaDocumento(codigoMunicipio, codigoProcedimiento, codigoTramite, codigoDocumento, 
                firmaDocumentoUsuarioId, codigoFirma, firmaDocumentoUsuarioLog, firmaDocumentoDniUsuario, conexion);
		
        log.debug("Termina insertarFirmaDocumento");
    }
    
    
    /**
     * Borrar datos de la table  e_dot_fir cuando een el xpdl no se adjuntan datos relacinados con la firma
     * @param codigoMunicipio
     * @param codigoProcedimiento
     * @param codigoTramite
     * @param codigoDocumento
     * @param firmaFlujo
     * @param firmaDocumentoUsuarioId
     * @param firmaDocumentoUsuarioLog
     * @param conexion
     * @throws SQLException 
     */
    private void borrarFirmaDocumento(String codigoMunicipio, String codigoProcedimiento, String codigoTramite, 
            String codigoDocumento, Connection conexion) throws SQLException {
        
            log.debug("Comienza borrarFirmaDocumento");
        
                // Se mira si existe en la DB firmas para ese documento
		SqlBuilder sql = new SqlBuilder().select().from("E_DOT_FIR")
				.whereEqualsParametrizado("DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD");
		SqlExecuter sqlExecuter = new SqlExecuter(sql);
		sqlExecuter.setValues(codigoMunicipio, codigoProcedimiento, codigoTramite, codigoDocumento).logSqlDebug(log);		
		QueryResult resultado = sqlExecuter.executeQuery(conexion);
                
                if (resultado.next()) {
                log.debug("Existen datos de firma para el documento: "+ codigoDocumento+" del trámite: "+ codigoTramite+ " procedemos a borrarlo");
                SqlBuilder sqlBorrado = new SqlBuilder().delete().from("E_DOT_FIR").whereEqualsParametrizado("DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD");
		SqlExecuter consulta = new SqlExecuter(sqlBorrado);
		consulta.setValues(codigoMunicipio, codigoProcedimiento, codigoTramite, codigoDocumento).logSqlDebug(log);
		consulta.executeUpdate(conexion);
                }

		
        log.debug("Termina borrarFirmaDocumento");
    }
    
    /**
     * Inserta los flujos de firma y los circuitos de firma de estos (FIRMA_FLUJO y FIRMA_CIRCUITO). 
     * 
	 * @param listaFlujos
     * @param conexion
	 * @return 
     * @throws SQLException 
     */
    public Map<Integer,Integer> insertarFlujosYCircuitosFirma(List<FirmaFlujoVO> listaFlujos, String codProc, Connection conexion) throws SQLException {
    log.debug("Comienza insertarFlujosYCircuitosFirma");

            List<FirmaCircuitoVO> listaCircuitos = null;
            boolean circuitoIns = false;
            int totalIns = 0;

            Map<Integer,Integer> mapeoIdsCompleto = new HashMap<Integer,Integer>();

            for(FirmaFlujoVO firmaFlujo : listaFlujos){
        Map<Integer, Integer> mapeoIdFlujo = insertarFirmaFlujo(firmaFlujo, codProc, conexion);
                    if(mapeoIdFlujo != null && !mapeoIdFlujo.isEmpty()) {
                            Boolean mismoflujo = Boolean.FALSE;
                            mapeoIdsCompleto.putAll(mapeoIdFlujo);
                            listaCircuitos = firmaFlujo.getListaFirmasCircuito();
                            for(int index = 0; index < listaCircuitos.size(); index ++) {
                                    FirmaCircuitoVO firmaCircuito = listaCircuitos.get(index);
                                    firmaCircuito.setIdFlujoFirma(mapeoIdFlujo.get(firmaFlujo.getId()));
                                    if(index == 0 || circuitoIns) {
                                            circuitoIns = insertarFirmaCircuito(firmaCircuito, mismoflujo, conexion);
                                            mismoflujo = Boolean.TRUE;
                                    }
                            }
                            if(circuitoIns) totalIns ++;
                    }
            }
    log.debug("Termina insertarFlujosYCircuitosFirma");
            if (totalIns == listaFlujos.size())
                    return mapeoIdsCompleto;
            return null;
    }
    
    /**
     * Inserta la Firma (E_DOT_FIR) de un Documento que pertenece a un Trámite.
     * 
     * @param codigoMunicipio
     * @param codigoProcedimiento
     * @param codigoTramite
     * @param codigoDocumento
     * @param codigoUsuario
     * @param codigoFirma
     * @param conexion
     * @throws SQLException 
     */
    private void insertarFirmaDocumento(String codigoMunicipio, String codigoProcedimiento, String codigoTramite, 
            String codigoDocumento, String codigoUsuario, String codigoFirma, String logUsuario,String firmaDocumentoDniUsuario, Connection conexion) 
            throws SQLException {
        

        
        if (!codigoUsuario.equals("-1")) { // si el documento tiene firma de tipo usuario se comprueba si existe en nuestro sistema por el nif, en caso de no existir se devuelve un -2
            Integer codigoUsuarioAux = existeUsuarioPorDni(firmaDocumentoDniUsuario, conexion);
            codigoUsuario = codigoUsuarioAux != null ? codigoUsuarioAux.toString() : "-2";
        }
        
		// Se mira si existe en la DB
		SqlBuilder sql = new SqlBuilder()
				.select("DOT_FLUJO").from("E_DOT_FIR")
				//.whereEqualsParametrizado("DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD", "USU_COD");
				.whereEqualsParametrizado("DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD");
		SqlExecuter sqlExecuter = new SqlExecuter(sql);
		sqlExecuter.setValues(codigoMunicipio, codigoProcedimiento, codigoTramite, codigoDocumento).logSqlDebug(log);
		
		QueryResult resultado = sqlExecuter.executeQuery(conexion);
		if (resultado.next()){ // Si existe
			// UPDATE
			String codigoFirmaDB = resultado.getString("DOT_FLUJO");
			sqlExecuter = new SqlExecuter(sql.updateParametrizado("E_DOT_FIR", "DOT_FLUJO", "USU_COD")
					//.whereEqualsParametrizado("DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD", "USU_COD"));
					.whereEqualsParametrizado("DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD"));
			sqlExecuter.setValues(codigoFirma, codigoUsuario, codigoMunicipio, codigoProcedimiento, codigoTramite, 
					codigoDocumento).logSqlDebug(log);
			sqlExecuter.executeUpdate(conexion);
		} else { // Si no existe
			// INSERT
			sql.insertIntoParametrizado("E_DOT_FIR", "DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD", "USU_COD", "DOT_FLUJO");
			sqlExecuter = new SqlExecuter(sql);
			sqlExecuter.setValues(codigoMunicipio, codigoProcedimiento, codigoTramite, codigoDocumento,
					codigoUsuario, codigoFirma).logSqlDebug(log);
			sqlExecuter.executeUpdate(conexion);
		}
    }

    /**
     * Inserta la FirmaFlujo correspondiente que se informa en el documento XPDL.
     * 
     * @param firmaFlujo
     * @param conexion
     * @throws SQLException 
     */
    private Map<Integer, Integer> insertarFirmaFlujo(FirmaFlujoVO firmaFlujo, String codProc, Connection conexion) throws SQLException {
        Integer idFlujoOriginal = -1;
		Integer idFlujoInserted = -1;
		Map<Integer,Integer> mapeoIds = new HashMap<Integer, Integer>();
		
		try {
			idFlujoOriginal = firmaFlujo.getId();
			
			// Se comprueba si ya existe en bd un flujo con el mismo nombre del flujo que viene en el xpdl.
			// El nombre de un flujo debe ser único
			idFlujoInserted = FirmaFlujoDAO.getInstance().existeFlujoPorNombre(firmaFlujo.getNombre(), conexion);
			if(idFlujoInserted != -1) { // Existe un flujo con ese nombre, se actualiza el flujo
				actualizarDatosFlujo(idFlujoInserted, firmaFlujo, conexion);
			} else { // No existe un flujo con ese nombre
				// Se establece un nuevo nombre para el flujo: <nombreAnterior>_<codProc>
				firmaFlujo.setNombre(firmaFlujo.getNombre() + "_" + codProc);
				
				// Se comprueba si existe un flujo con el nuevo nombre (ya ha sido importado con anterioridad)
				idFlujoInserted = FirmaFlujoDAO.getInstance().existeFlujoPorNombre(firmaFlujo.getNombre(), conexion);
				if(idFlujoInserted != -1) { // Existe un flujo con ese nombre, se actualiza el flujo
					actualizarDatosFlujo(idFlujoInserted, firmaFlujo, conexion);
				} else { // No existe un flujo con ese nombre, se inserta el nuevo flujo
					idFlujoInserted = insertarNuevoFlujo(firmaFlujo, conexion);
				}
			}			
			
			log.debug("idFlujoOriginal: " + idFlujoOriginal + " - idFlujoFinal: " + idFlujoInserted);
			if(idFlujoInserted != -1) mapeoIds.put(idFlujoOriginal, idFlujoInserted);
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error al insertar los flujos de firma. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
		
		return mapeoIds;
    }

    /**
     * Inserta un nuevo flujo en la bd cuyo id será el nextval de la secuencia FIRMA_FLUJO_ID
     * 
     * @param firmaFlujo
     * @param conexion
     * @throws SQLException 
     */
    private Integer insertarNuevoFlujo(FirmaFlujoVO firmaFlujo, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder();
		CallableStatement callable = null;
		Integer idFlujoInserted = -1;
		
		
		try {
			// Envolvemos la query con BEGIN-END para convertirla en un bloque PL/SQL y poder incluir RETURNING id INTO
			// Con esto podemos recuperar, usando CallableStatemente, el último id insertado
			sql.append("BEGIN ")
					.append("INSERT INTO FIRMA_FLUJO (ID, NOMBRE, ID_TIPO, ACTIVO) ")
					.append("VALUES (FIRMA_FLUJO_ID.nextval,?,?,?) RETURNING id INTO ?; ")
					.append("END;");
			log.debug("SQL: " + sql);
			log.debug(String.format("Parámetros pasados a la query: %s-%s-%s",firmaFlujo.getNombre(),firmaFlujo.getIdTipoFirma(),(firmaFlujo.isActivo())?1:0));
			
			callable = conexion.prepareCall(sql.toString());
			int contbd = 1;
			callable.setString(contbd++, firmaFlujo.getNombre());
			callable.setInt(contbd++, firmaFlujo.getIdTipoFirma());
			callable.setInt(contbd++, (firmaFlujo.isActivo())?1:0);
			// registerOutParameter indica a JDBC que tipo de dato es el parametro externo que la query devuelve
			callable.registerOutParameter(contbd, java.sql.Types.INTEGER);
			callable.execute();
			idFlujoInserted = callable.getInt(contbd);
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error al insertar los nuevos flujos de firma. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
		
		return idFlujoInserted;
    }

    /**
     * Actualiza los datos de ID_TIPO y ACTIVO de un flujo en la bd a partir del que viene en el documento XPDL
     * 
     * @param firmaFlujo
     * @param conexion
     * @throws SQLException 
     */
    private void actualizarDatosFlujo(Integer idFlujo, FirmaFlujoVO firmaFlujo, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder();
		SqlExecuter executer = null;
		
		try {
			executer = new SqlExecuter(sql.updateParametrizado("FIRMA_FLUJO", "ID_TIPO", "ACTIVO").whereEqualsParametrizado("ID"));
			executer.setValues(firmaFlujo.getIdTipoFirma(), (firmaFlujo.isActivo())?1:0, idFlujo).logSqlDebug(log);
			executer.executeUpdate(conexion);
			
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error al actualizar los flujos de firma. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
    }

    /**
     * Inserta la FirmaCircuito correspondiente a un Trámite.
     * 
     * @param codigoFirma
     * @param firmaCircuito
     * @param conexion
     * @throws SQLException 
     */
    private boolean insertarFirmaCircuito(FirmaCircuitoVO firmaCircuito, Boolean mismoflujo,  Connection conexion) throws SQLException {
        FirmaFlujoDAO firmaFlujoDAO = FirmaFlujoDAO.getInstance();
        boolean updated = false;
        Integer idFlujo = firmaCircuito.getIdFlujoFirma();
        String  nifUsuario = firmaCircuito.getDniUsuario();
        
        //Comprobamos si existe el usuario por su NIF
        Integer idUsuario = existeUsuarioPorDni(nifUsuario, conexion);
        if (Boolean.FALSE.equals(mismoflujo)){
            borrarDatosCircuitoFirma(idFlujo, conexion);
        }
        if (idUsuario != null) {
            if (firmaFlujoDAO.existeCircuitoParaFlujo(idFlujo, conexion)) {
                if (firmaFlujoDAO.existeFirmanteEnFlujo(idFlujo, idUsuario, conexion) == -1) {
                    int maxOrden = firmaFlujoDAO.getMaxOrdenCircuito(idFlujo, conexion);
                    // Insertamos el siguiente firmante del circuito
                    updated = insertarFirmante(idFlujo, idUsuario, new Integer(++maxOrden), conexion);
                } else {
                    updated = true;
                }
            } else { // No existe el circuito
                updated = insertarFirmante(idFlujo, idUsuario, 1, conexion);
            }
        } else {
            // en caso de no existir el usuario se mandará un mensaje de error al final del procesamiento
             updated = true;
        }

        return updated;
    }
    
    	/**
     * Inserta un nuevo flujo en la bd cuyo id será el nextval de la secuencia FIRMA_FLUJO_ID
     * 
     * @param firmaFlujo
     * @param conexion
     * @throws SQLException 
     */
    private void borrarDatosCircuitoFirma(Integer idFlujo,Connection conexion) throws SQLException {
        
        log.debug("Comienza borrarDatosCircuitoFirma para el flujo de firma con id: "+idFlujo);

         // Se mira si existe en la DB un circuito de firmas para ese flujo de firma
         SqlBuilder sql = new SqlBuilder().select().from("FIRMA_CIRCUITO").whereEqualsParametrizado("ID_FIRMA_FLUJO");
         SqlExecuter sqlExecuter = new SqlExecuter(sql);
         sqlExecuter.setValues(idFlujo).logSqlDebug(log);		
         QueryResult resultado = sqlExecuter.executeQuery(conexion);

         if (resultado.next()) {
         log.debug("Existe un circuito de firmas para o flujo de firma : "+ idFlujo+ " procedemos a borrarlo");
         SqlBuilder sqlBorrado = new SqlBuilder().delete().from("FIRMA_CIRCUITO").whereEqualsParametrizado("ID_FIRMA_FLUJO");
         SqlExecuter consulta = new SqlExecuter(sqlBorrado);
         consulta.setValues(idFlujo).logSqlDebug(log);
         consulta.executeUpdate(conexion);
         }


        log.debug("Termina borrarDatosCircuitoFirma para el flujo de firma con id: "+idFlujo);
        
    }
	
	/**
     * Inserta un nuevo flujo en la bd cuyo id será el nextval de la secuencia FIRMA_FLUJO_ID
     * 
     * @param firmaFlujo
     * @param conexion
     * @throws SQLException 
     */
    private boolean insertarFirmante(Integer idFlujo, Integer idUsuario, Integer ordenFirmante, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder();
		SqlExecuter sqlExecuter = null;
		Map<String, Object> camposCircuito = null;
		int updated = 0;
		
		
		try {
			camposCircuito = new HashMap<String, Object>();
			camposCircuito.put("ID_FIRMA_FLUJO", idFlujo);
			camposCircuito.put("ID_USUARIO", idUsuario);
			camposCircuito.put("ORDEN", ordenFirmante);

			sqlExecuter = new SqlExecuter();
			sqlExecuter.insertWithValues("FIRMA_CIRCUITO",camposCircuito).logSqlDebug(log);
			updated = sqlExecuter.executeUpdate(conexion);
		} catch (SQLException sqle){
			log.error("Ha ocurrido un error al insertar los nuevos flujos de firma. " + sqle.getMessage());
			sqle.printStackTrace();
			throw sqle;
		}
		
		return updated > 0;
    }
	
	/**
	 * Actualiza los ids de los flujos en los documentos de los tramites con los valores insertados finalmente en FIRMA_FLUJO
	 * Elimina de FIRMA_FLUJO y FIRMA_CIRCUITO los datos de flujos no usados en ningún documento
	 * @param codProcedimiento
	 * @param mapeoIds
	 * @param con
	 * @throws SQLException 
	 */
	public void actualizarFlujosFirma(String codProcedimiento, Map<Integer,Integer> mapeoIds, Connection con) throws SQLException {
		actualizarIdsFlujosFirmaEnDocumentos(codProcedimiento, mapeoIds, con);
		FirmaFlujoDAO.getInstance().eliminarFlujosYCircuitosNoUsados(con);
	}
	
	/**
	 * Actualiza los ids de los flujos en los documentos de los tramites con los valores insertados finalmente en FIRMA_FLUJO
	 * @param codProcedimiento
	 * @param mapeoIds
	 * @param con
	 * @throws SQLException 
	 */
	public void actualizarIdsFlujosFirmaEnDocumentos(String codProcedimiento, Map<Integer,Integer> mapeoIds,
			Connection con) throws SQLException {
		SqlBuilder sqlSelect = new SqlBuilder();
		SqlBuilder sqlUpdate = new SqlBuilder();
		SqlExecuter execSelect = null;
		SqlExecuter execUpdate = null;
		Integer newId = null;
		Set<Integer> oldIds = null;
		
		try {
			sqlSelect.select("DOT_FLUJO").from("E_DOT_FIR")
					.whereEqualsParametrizado("DOT_PRO")
					.andEqualsParametrizado("DOT_FLUJO");
			sqlUpdate.updateParametrizado("E_DOT_FIR", "DOT_FLUJO")
					.whereEqualsParametrizado("DOT_PRO")
					.andEqualsParametrizado("DOT_FLUJO");
			oldIds = mapeoIds.keySet();
			for(Integer oldId : oldIds){
				// Se recupera el nuevo id de flujo correspondiente al antiguo en el mapa que guarda las referencias
				newId = mapeoIds.get(oldId);
				
				if(!oldId.equals(newId)) { //Los ids de flujo han cambiado
					// Se ejecuta la select para comprobar que realmente hay documentos con el id de flujo en cuestion enlazado
					execSelect = new SqlExecuter(sqlSelect);
					execSelect.setValues(codProcedimiento, oldId).logSqlDebug(log);

					if (execSelect.executeQuery(con).next()) { // Si existe
						execUpdate = new SqlExecuter(sqlUpdate);
						execUpdate.setValues(newId, codProcedimiento, oldId).logSqlDebug(log);
						execUpdate.executeUpdate(con);
					} 
				}
			}
		} catch (SQLException sqle) {
			log.error("Ha ocurrido un error en ImportacionProcedimientoDAO.actualizarIdsFlujosFirmaEnDocumentos. " + sqle.getMessage());
			throw sqle;
		}
	}

    /**
     * Comprueba la existencia de un usuario por login y código en la BD.
     * 
     * @param codigoUsuario
     * @param logUsuario
     * @param conexion
     * @return
     * @throws SQLException 
     */
    private boolean existeUsuario(String codigoUsuario, String logUsuario, Connection conexion) throws SQLException {
        SqlBuilder sql = new SqlBuilder()
				.select(SqlBuilder.countAllAs("COINCIDENCIAS"))
				.from(SqlBuilder.columnaConTabla(GlobalNames.ESQUEMA_GENERICO, "A_USU"))
				.whereEqualsParametrizado(usu_cod)
				.andEqualsParametrizado("USU_LOG");
        if (log.isDebugEnabled()) {
            log.debug("Query = " + sql.toString());
            log.debug(String.format("Parámetros pasados a la query = %s-%s",codigoUsuario,logUsuario));
        }
        int coincidencias;
        PreparedStatement pst = null;
        ResultSet resultado = null;
        try {        
            pst= conexion.prepareStatement(sql.toString());
            JdbcOperations.setValues(pst, 1, codigoUsuario, logUsuario);
            resultado = pst.executeQuery();
            resultado.next();
            // Si hay algún resultado, existe en la BD
            coincidencias = resultado.getInt("COINCIDENCIAS");
            return coincidencias > 0;
        }
        catch (SQLException e) {
            throw (e);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (resultado != null) {
                resultado.close();
            }
        }
    }
    
    
        /**
     * Comprueba la existencia de un usuario por login y código en la BD.
     * 
     * @param codigoUsuario
     * @param logUsuario
     * @param conexion
     * @return devuelve el id del primer resultado obtenido, en caso de no obtener ninguno, null
     * @throws SQLException 
     */
    private Integer existeUsuarioPorDni(String dniUsuario, Connection conexion) throws SQLException {
        Integer idUsuario;
        
        SqlBuilder sql = new SqlBuilder()
				.select("USU_COD")
				.from(SqlBuilder.columnaConTabla(GlobalNames.ESQUEMA_GENERICO, "A_USU"))
				.whereEqualsParametrizado("USU_NIF");
				
        if (log.isDebugEnabled()) {
            log.debug("Query = " + sql.toString());
            log.debug(String.format("Parámetros pasados a la query = %s",dniUsuario));
        }
        
        PreparedStatement pst = null;
        ResultSet resultado = null;
        try {        
            pst= conexion.prepareStatement(sql.toString());
            JdbcOperations.setValues(pst, 1, dniUsuario);
            resultado = pst.executeQuery();

            // Si hay algún resultado, existe en la BD
            idUsuario = resultado.next() ?  resultado.getInt("USU_COD") : null;
            return idUsuario;
        }
        catch (SQLException e) {
            throw (e);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (resultado != null) {
                resultado.close();
            }
        }
    }
    
      public int grabarFlujoSalida(TablasIntercambiadorasValueObject tabInterVO,Connection conexion) throws TechnicalException,BDException,AnotacionRegistroException{
        String	sql= "";
        String	sqlBorrar =	"";
        int res = 0;
        int res1 = 0;
        int resBorrar =0;
        PreparedStatement ps = null;
        log.debug("Entra en grabarFlujoSalida del	DAO");

        try{

            Vector listaCodTramitesFlujoSalida	= tabInterVO.getListaCodTramitesFlujoSalida();
            Vector listaNumerosSecuenciaFlujoSalida = tabInterVO.getListaNumerosSecuenciaFlujoSalida();

            if(tabInterVO.getNumeroCondicionSalida().equals("0") || tabInterVO.getNumeroCondicionSalida().equals("3")) {
                sqlBorrar = "DELETE FROM E_FLS WHERE "	+  fls_mun + "=" + tabInterVO.getCodMunicipio()	+ " AND " +	fls_pro + "='" +
                        tabInterVO.getCodProcedimiento() +	"' AND " + fls_tra + "=" + tabInterVO.getCodTramite();
            } else {
                sqlBorrar = "DELETE FROM E_FLS WHERE "	+  fls_mun + "=" + tabInterVO.getCodMunicipio()	+ " AND " +	fls_pro + "='" +
                        tabInterVO.getCodProcedimiento() +	"' AND " + fls_tra + "=" + tabInterVO.getCodTramite()	+
                        " AND (" +	fls_nuc + "=" + tabInterVO.getNumeroCondicionSalida()	+
                        " OR	" + fls_nuc	+ "=0)";
            }
            if(log.isDebugEnabled()) log.debug(sqlBorrar);
            ps =	conexion.prepareStatement(sqlBorrar);
            resBorrar = ps.executeUpdate();
            ps.close();
            if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_FLS son ::::::::::::::	: " +	resBorrar);
            if(log.isDebugEnabled()) log.debug("el	tamaío de la lista de tramites seleccionados es	: " +	listaCodTramitesFlujoSalida.size());

            for(int l=0;l<listaCodTramitesFlujoSalida.size();l++) {
                res = 0;
                sql = "INSERT INTO E_FLS (" + fls_mun + "," + fls_pro + "," + fls_tra + "," + fls_nuc +
                        "," + fls_nus + "," + fls_cts + ") VALUES(" + tabInterVO.getCodMunicipio()	+ ",'" +
                        tabInterVO.getCodProcedimiento() +	"'," + tabInterVO.getCodTramite() +	"," +
                        tabInterVO.getNumeroCondicionSalida() + "," + listaNumerosSecuenciaFlujoSalida.elementAt(l) + "," +
                        listaCodTramitesFlujoSalida.elementAt(l)	+ ")";
                if(log.isDebugEnabled()) log.debug(sql);
                ps	= conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                ps.close();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de	E_FLS	son :::::::::::::: : " + res);
            }
            String obligatorio = "null";
            if ("0".equals(tabInterVO.getObligatorio()) || "1".equals(tabInterVO.getObligatorio()) || "2".equals(tabInterVO.getObligatorio()))
                obligatorio = tabInterVO.getObligatorio();
            
            if(tabInterVO.getNumeroCondicionSalida().equals("2") ) {
                sql	= "UPDATE E_SAL SET " +	sal_obld + "=" + obligatorio +	" WHERE " +	 sal_mun + "=" + tabInterVO.getCodMunicipio() +
                        " AND	" + sal_pro	+ "='" + tabInterVO.getCodProcedimiento()	+
                        "' AND " + sal_tra + "=" + tabInterVO.getCodTramite();
            } else if(tabInterVO.getNumeroCondicionSalida().equals("0") || tabInterVO.getNumeroCondicionSalida().equals("1") ){
                sql	= "UPDATE E_SAL SET " +	sal_obl + "=" + obligatorio + "," +	sal_obld + "=null" +
                        " WHERE " +	sal_mun + "=" + tabInterVO.getCodMunicipio() +
                        " AND	" + sal_pro	+ "='" + tabInterVO.getCodProcedimiento()	+
                        "' AND " + sal_tra + "=" + tabInterVO.getCodTramite();
            } else {
                sql	= "UPDATE E_SAL SET " +	sal_obl + "=null" + "," +	sal_obld + "=null" +
                        " WHERE " +	sal_mun + "=" + tabInterVO.getCodMunicipio() +
                        " AND	" + sal_pro	+ "='" + tabInterVO.getCodProcedimiento()	+
                        "' AND " + sal_tra + "=" + tabInterVO.getCodTramite();
            }

            if(log.isDebugEnabled()) log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            ps.close();
            if(log.isDebugEnabled()) log.debug("las	filas	afectadas en el update de E_SAL son	:::::::::::::: : " + res1);

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try{
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return	res;
    }


    public void prepararGrabacionFlujoSalida (DefinicionTramitesValueObject dtVO,Connection con) throws AnotacionRegistroException{

      String codTramite = dtVO.getCodigoTramite();
      String codProcedimiento = dtVO.getTxtCodigo();
      String codMunicipio = dtVO.getCodMunicipio();
      String tipoCondicion = dtVO.getTipoCondicion();
      String tipoCondicionSI = dtVO.getTipoFavorableSI();
      String tipoCondicionNO = dtVO.getTipoFavorableNO();


      log.debug("prepararGrabacionFlujoSalida codTramite: " + codTramite);
      log.debug("prepararGrabacionFlujoSalida codProcedimiento: " + codProcedimiento);
      log.debug("prepararGrabacionFlujoSalida codMunicipio: " + codMunicipio);
      log.debug("prepararGrabacionFlujoSalida tipoCondicion: " + tipoCondicion);
      log.debug("prepararGrabacionFlujoSalida tipoCondicionSI: " + tipoCondicionSI);
      log.debug("prepararGrabacionFlujoSalida tipoCondicionNO: " + tipoCondicionNO);
      
      if (tipoCondicion!=null && tipoCondicion.equals("Tramite")){
            TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
            Vector flujoSalida = dtVO.getListaTramitesFavorable();
            log.debug(" el flujo de salida tipoCondicion tramite es: " + flujoSalida);
            String codigoTipoSalida ="0";
            String obligatorio = dtVO.getObligatorio();
            Vector<String> listaCodTramitesFlujoSalida = asignarListaCodTramitesSalida(flujoSalida);
            log.debug(" codigo tramites flujo de salida tipoCondicion tramite es: " + listaCodTramitesFlujoSalida);
            Vector<String> listaNumSecuenciaFlujoSalida = asignarListaNumSecuenciaTramitesSalida(flujoSalida,codigoTipoSalida);
            if(listaCodTramitesFlujoSalida.size()>0 && listaNumSecuenciaFlujoSalida.size()>0){
                tabInterVO.setObligatorio(obligatorio);
                tabInterVO.setCodProcedimiento(codProcedimiento);
                tabInterVO.setCodTramite(codTramite);
                tabInterVO.setListaCodTramitesFlujoSalida(listaCodTramitesFlujoSalida);
                tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaNumSecuenciaFlujoSalida);
                tabInterVO.setCodMunicipio(codMunicipio);
                tabInterVO.setNumeroCondicionSalida(codigoTipoSalida);
                if (obligatorio!=null && !obligatorio.equals("")){
                    try{
                        this.grabarFlujoSalida(tabInterVO,con);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }//if
      }

      if (tipoCondicionSI!=null && !tipoCondicionSI.equals("")){
            TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
            Vector flujoSalida = dtVO.getListaTramitesFavorable();
            log.debug(" el flujo de salida es casa favorable: " + flujoSalida);
            String codigoTipoSalida ="1";
            Vector<String> listaCodTramitesFlujoSalida = asignarListaCodTramitesSalida(flujoSalida);
            log.debug(" tramite flujoSalida casa favorable: " + listaCodTramitesFlujoSalida);
            Vector<String> listaNumSecuenciaFlujoSalida = asignarListaNumSecuenciaTramitesSalida(flujoSalida,codigoTipoSalida);
            if(listaCodTramitesFlujoSalida.size()>0 && listaNumSecuenciaFlujoSalida.size()>0){
                String obligatorio = dtVO.getObligatorio();

                tabInterVO.setObligatorio(obligatorio);
                tabInterVO.setCodProcedimiento(codProcedimiento);
                tabInterVO.setCodTramite(codTramite);
                tabInterVO.setListaCodTramitesFlujoSalida(listaCodTramitesFlujoSalida);
                tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaNumSecuenciaFlujoSalida);
                tabInterVO.setCodMunicipio(codMunicipio);
                tabInterVO.setNumeroCondicionSalida(codigoTipoSalida);
                try{
                    this.grabarFlujoSalida(tabInterVO,con);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
      }

       if (tipoCondicionNO!=null && !tipoCondicionNO.equals("")){
            TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
            Vector flujoSalida = dtVO.getListaTramitesDesfavorable();
            log.debug(" el flujo de salida es casa desfavorable: " + flujoSalida);
            String codigoTipoSalida ="2";
            Vector<String> listaCodTramitesFlujoSalida = asignarListaCodTramitesSalida(flujoSalida);
            log.debug(" tramite flujoSalida casa desfavorable: " + listaCodTramitesFlujoSalida);
            Vector<String> listaNumSecuenciaFlujoSalida = asignarListaNumSecuenciaTramitesSalida(flujoSalida,codigoTipoSalida);
            if(listaCodTramitesFlujoSalida.size()>0 && listaNumSecuenciaFlujoSalida.size()>0){
                String obligatorio = dtVO.getObligatorioDesf();
                tabInterVO.setObligatorio(obligatorio);
                tabInterVO.setCodProcedimiento(codProcedimiento);
                tabInterVO.setCodTramite(codTramite);
                tabInterVO.setListaCodTramitesFlujoSalida(listaCodTramitesFlujoSalida);
                tabInterVO.setListaNumerosSecuenciaFlujoSalida(listaNumSecuenciaFlujoSalida);
                tabInterVO.setCodMunicipio(codMunicipio);
                tabInterVO.setNumeroCondicionSalida(codigoTipoSalida);
                try{
                    this.grabarFlujoSalida(tabInterVO,con);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
      }
  }// prepararGrabacionFlujoSalida
    

  private Vector asignarListaCodTramitesSalida(Vector flujoSalida){
      Vector resultado = new Vector();
      if (flujoSalida!=null){
        for (int i=0;i<flujoSalida.size();i++){
              DefinicionTramitesValueObject t = (DefinicionTramitesValueObject)flujoSalida.elementAt(i);
              String destino = t.getCodTramiteFlujoSalida();
              resultado.add(destino);
        }
     }
     return resultado;
  }
   private Vector asignarListaNumSecuenciaTramitesSalida(Vector flujoSalida,String tipoCondicion){
      Vector resultado = new Vector();
      if (flujoSalida!=null){
      for (int i=0;i<flujoSalida.size();i++){
           DefinicionTramitesValueObject t = (DefinicionTramitesValueObject)flujoSalida.elementAt(i);
              if (tipoCondicion.equals("1"))
                resultado.add(t.getCodTramiteFlujoSalida());
              else
                resultado.add((Integer.toString(i+1)));
        }
     }
     return resultado;
  }


   /**
    * Comprueba la existencia del procedimiento
    * @param codProcedimiento: Código del procedimiento
    * @param params: Parámetros de conexión a la base de datos
    * @return Un boolean
    */
   public boolean existeProcedimiento(String codProcedimiento,Connection con){
       PreparedStatement ps = null;
       ResultSet rs = null;
       boolean exito = false;

       try{
           String sql = "SELECT COUNT(*) AS NUM FROM E_PRO WHERE PRO_COD=?";
           int i=1;
           ps = con.prepareStatement(sql);
           ps.setString(i++,codProcedimiento);

           rs = ps.executeQuery();
           while(rs.next()){
               int num = rs.getInt("NUM");
               if(num>=1) exito = true;
           }


       }catch(SQLException e){
           e.printStackTrace();
       }finally{
           
           try{
               if(ps!=null) ps.close();
               if(rs!=null) rs.close();
           }catch(SQLException e){
                e.printStackTrace();
           }
       }

       return exito;
       
   }// existeProcedimiento




   /**
    * Verifica la existencia de un rol de procedimiento
    * @param codProcedimiento: Código d
    * @param codigoRol
    * @param codMunicipio
    * @param con
    * @return
    */
   public boolean verificarExistenciaRol(String codProcedimiento,String codigoRol,String codMunicipio,Connection con){
       boolean exito = false;
       PreparedStatement ps = null;
       ResultSet rs = null;
       try{
           String sql = "SELECT COUNT(*) AS NUM FROM E_ROL WHERE ROL_COD=? AND ROL_MUN=? AND ROL_PRO=?";
           int i = 1;
           ps = con.prepareStatement(sql);
           ps.setInt(i++, Integer.parseInt(codigoRol));
           ps.setInt(i++, Integer.parseInt(codMunicipio));
           ps.setString(i++,codProcedimiento);

           rs = ps.executeQuery();
           int num = 0;
           while(rs.next()){
               num = rs.getInt("NUM");

           }
           if(num>=1) exito = true;

       }catch(SQLException e){
           e.printStackTrace();
       }finally{
           try{
               if(ps!=null) ps.close();
               if(rs!=null) rs.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }
       return exito;
   }

   /**
    * Comprueba la existencia de determinados roles en una organización determinada
    * @param con: Conexión a la base de datos
    * @return
    */
   public ArrayList<RolProcedimientoVO> verificarExistenciaRoles(String codProcedimiento,Vector listaCodigoRoles,Vector listaDescripcionRoles,Connection con){
       ArrayList<RolProcedimientoVO> roles = new ArrayList<RolProcedimientoVO>(); 
       PreparedStatement ps = null;
       ResultSet rs = null;
       ArrayList<String> rolesActuales = new ArrayList<String>();
       try{
            String sql = "SELECT ROL_COD FROM E_ROL WHERE ROL_PRO=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            while(rs.next()){
                rolesActuales.add(rs.getString("ROL_COD"));
            }

            for(int i=0; listaCodigoRoles!=null && i< listaCodigoRoles.size();i++){
                String rolNuevo = (String)listaCodigoRoles.get(i);
                if(!rolesActuales.contains(rolNuevo)){
                    RolProcedimientoVO rol = new RolProcedimientoVO();
                    rol.setCodigo(rolNuevo);
                    rol.setDescripcion((String)listaDescripcionRoles.get(i));
                    roles.add(rol);
                }//if
                
            }//for

       } catch(SQLException e){
           e.printStackTrace();
       }finally{
           try{
               if(ps!=null) ps.close();
               if(rs!=null) rs.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }

        return roles;
   }



  
   public int getCodigoAreaByCodVisible(String codVisible,Connection con){
       int codArea = -1;
       Statement st = null;
       ResultSet rs = null;

       try{
           String sql  ="SELECT CAR_COD FROM A_CAR WHERE CAR_COD_VIS='" + codVisible + "'";
           log.debug("getCodigoAreaByCodVisible: " + sql);

           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next()){
               codArea = rs.getInt("CAR_COD");
           }
           
       }catch(SQLException e){
           e.printStackTrace();
       }finally{
           try{
               if(st!=null) st.close();
               if(rs!=null) rs.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }// finally
       
       return codArea;
   }


   /**
    * Comprueba si existe un determinado desplegable
    * @param codDesplegable: Código del desplegable
    * @param con: Conexión a la base de datos
    * @return Un boolean
    */
   private boolean existeCampoDesplegable(String codDesplegable,Connection con){
       Statement st = null;
       ResultSet  rs = null;
       boolean exito = false;
       try{
           String sql ="SELECT COUNT(*) AS NUM FROM E_DES WHERE DES_COD='" + codDesplegable + "'";
           log.debug("CONSULTA EXISTE CAMPO DESPLEGABLE: " + sql);
           st = con.createStatement();
           rs = st.executeQuery(sql);
           int num = 0;
           while(rs.next()){
               num = rs.getInt("NUM");
           }

           if(num>0)
               exito = true;
           
       }catch(SQLException e){
           e.printStackTrace();
       }finally{
            try{
               if(st!=null) st.close();
               if(rs!=null) rs.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }
       return exito;
   }// existeCampoDesplegable
   
   
   private boolean existeCampoDesplegableExterno(String codDesplegable,Connection con){
       Statement st = null;
       ResultSet  rs = null;
       boolean exito = false;
       try{
           String sql ="SELECT COUNT(*) AS NUM FROM DESPLEGABLE_EXTERNO WHERE CODIGO='" + codDesplegable + "'";
           log.debug("CONSULTA EXISTE CAMPO DESPLEGABLE EXTERNO: " + sql);
           st = con.createStatement();
           rs = st.executeQuery(sql);
           int num = 0;
           while(rs.next()){
               num = rs.getInt("NUM");
           }

           if(num>0)
               exito = true;
           
       }catch(SQLException e){
           log.error("Error consultando si existe un campo desplegable externo "+ e.getMessage());
       }finally{
            try{
               if(st!=null) st.close();
               if(rs!=null) rs.close();
           }catch(SQLException e){
               log.error("Error cerrando conexiones "+ e.getMessage());
           }
       }
       return exito;
   }


   /**
    * Comprueba que errores se han producido durante la importación pero que no impiden que este se lleve a cabo
    * @param defProcVO: DefinicionProcedimientosValueObject con la información del procedimiento y de sus trámites
    * @param codIdioma: Código del idioma del usuario
    * @param codAplicacion: Código de la aplicación
    * @param con: Conexión a la base de datos
    * @return ArrayList<ErrorImportacionXPDL> con el listado de errores.
    * Los códigos de error son:  1 = No existe el cargo
    *                                        2 = No existe un campo desplegable para un campo suplementario de procedimiento, pero aún así se da de alta el campo
    *                                        3 = No existe un campo desplegable para un campo suplementario de trámite, pero aún así se da de alta el campo
    *                                        4 = No existe código de usuario relacionado con la firma.
    */
   public ArrayList<ErrorImportacionXPDL> verificarErroresImportacionProcedimientoNuevo(DefinicionProcedimientosValueObject defProcVO,int codIdioma,int codAplicacion,Connection con){
       ArrayList<ErrorImportacionXPDL> errores= new ArrayList<ErrorImportacionXPDL>();
       PreparedStatement ps = null;
       ResultSet rs = null;

       try{
           TraductorAplicacionBean traductor = new TraductorAplicacionBean();
           traductor.setApl_cod(codAplicacion);
           traductor.setIdi_cod(codIdioma);

           //traductor.getDescripcion(tpr_cod)

           Vector tramites = defProcVO.getTramites();
           

            /*** COMPROBACIÓN DE LOS CAMPOS SUPLEMENTARIOS A NIVEL DE PROCEDIMIENTO **/
           Vector listaCodCamposProc = defProcVO.getListaCodCampos();
           Vector listaDescripcionCamposProc = defProcVO.getListaDescCampos();     
           Vector listaCodPlantillaProc = defProcVO.getListaCodPlantilla();
           Vector listaCodTipoDato = defProcVO.getListaCodTipoDato();
           Vector listaActivosProc = defProcVO.getListaActivos();
           Vector listaOcultosProc = defProcVO.getListaOcultos();
           Vector listaBloqueadosProc = defProcVO.getListaBloqueados();
           
           // comprueba existencia campos del Procedimiento
           for (int j = 0; j < listaCodCamposProc.size(); j++) {
               if ("SI".equalsIgnoreCase((String) listaActivosProc.elementAt(j))) {
                   if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                       if (!this.existeCampoDesplegable(listaCodPlantillaProc.get(j).toString(), con)) {
                           AnadeErrorNoExisteCampoDesplegable(codIdioma, traductor, listaDescripcionCamposProc.get(j).toString(), defProcVO, errores);
                       }
                   } else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
                       if (!this.existeCampoDesplegableExterno(listaCodPlantillaProc.get(j).toString(), con))
                       AnadeErrorNoExisteCampoDesplegableExterno(codIdioma, traductor, listaDescripcionCamposProc.get(j).toString(), defProcVO, errores);
                   }
               }
           } // for

           boolean ExistenTramitesSinClasificacion=false;   

           String sql = "SELECT COUNT(*) AS NUM FROM A_CAR WHERE CAR_COD_VIS=?";
           ps = con.prepareStatement(sql);
           /*** COMPROBACIÓN DE LOS CARGOS DE CADA TRÁMITE DEL PROCEDIMIENTO **/
           for(int i=0;i<tramites.size();i++){
               DefinicionTramitesValueObject tramite = (DefinicionTramitesValueObject)tramites.get(i);
               int num = 0;
               if(tramite.getCodCargo()!=null && !"".equals(tramite.getCodCargo()) && 
                       tramite.getCodVisibleCargo()!=null && !"".equals(tramite.getCodVisibleCargo()))
               {
                    ps.setString(1,tramite.getCodVisibleCargo());
                    rs = ps.executeQuery();
                    while(rs.next()){
                        num = rs.getInt("NUM");
                    }

                    if(num==0){
                        // No existe el cargo, por tanto se informa del error
                        ErrorImportacionXPDL error = new ErrorImportacionXPDL();
                        error.setCodError(codIdioma);
                        error.setDescripcionError(traductor.getDescripcion(ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_1) + " " + tramite.getCodCargo() + " "
                                + traductor.getDescripcion(ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_2) + " " +  tramite.getCodVisibleCargo() + " "
                                + traductor.getDescripcion(ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_3)  + " " + tramite.getNombreTramite());
                        errores.add(error);
                    }
               }// if


               /** COMPROBACIÓN DE LA EXISTENCIA DE LOS DESPLEGABLES DE UN CAMPO SUPLEMENTARIO A NIVEL DE TRÁMITE **/
               Vector listaCodCamposTram = tramite.getListaCodCampos();
               Vector listaDescripcionCamposTra = tramite.getListaDescCampos();
               Vector listaCodPlantillTram = tramite.getListaCodPlantill();
               Vector listaActivoTram = tramite.getListaActivo();
               Vector listaCodTipoDatoTram = tramite.getListaCodTipoDato();
               for (int j = 0; j < listaCodCamposTram.size(); j++) {         
                   if ("SI".equalsIgnoreCase((String) listaActivoTram.elementAt(j))) {
                       if (listaCodTipoDatoTram.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable")) && !this.existeCampoDesplegable(listaCodPlantillTram.get(j).toString(), con)) {
                           AnadeErrorCampoDesplegableTramite(codIdioma, traductor, listaDescripcionCamposTra.get(j).toString(), tramite, errores);
                       } else if (listaCodTipoDatoTram.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt")) && !this.existeCampoDesplegableExterno(listaCodPlantillTram.get(j).toString(), con)) {
                           this.AnadeErrorCampoDesplegableExternoTramite(codIdioma, traductor, listaDescripcionCamposTra.get(j).toString(), tramite, errores);
                       }
                   }
               }// for
 
                 String clasifiTramite = tramite.getCodClasifTramite();
                 
                 String sql2 = "SELECT COUNT(*) AS NUM FROM "+GlobalNames.ESQUEMA_GENERICO+"A_CLS WHERE CLS_COD=?";
                 
              
                 ps = con.prepareStatement(sql2);
                 
                  ps.setString(1,clasifiTramite);
                  rs = ps.executeQuery(); 
                  int NumClasifTram = 0;
                                 
                  if(rs.next()){
                     NumClasifTram = rs.getInt("NUM");  
                  }
                  
                  if(NumClasifTram==0) {
                      ExistenTramitesSinClasificacion=true;
                  }                                                  
                  
                // Comprobar errores de firma de documento por tramite
                comprobarErroresFirmaDocumento(errores, tramite, traductor, con);
           }// for
           
           
           if(ExistenTramitesSinClasificacion){
              sql = "SELECT CML_VALOR FROM "+GlobalNames.ESQUEMA_GENERICO+"A_CML WHERE CML_COD=(SELECT MIN(CML_COD) FROM "+GlobalNames.ESQUEMA_GENERICO+"A_CML) ";

              ps = con.prepareStatement(sql);

              rs = ps.executeQuery();
              String clasifTram = "";
              if(rs.next()){
                 clasifTram = rs.getString("CML_VALOR");  
             }

                  
             ErrorImportacionXPDL error = new ErrorImportacionXPDL();
             error.setCodError(codIdioma);
             
             error.setDescripcionError(traductor.getDescripcion(ERR_TRAMITES_CLASIFICACION)+" "+clasifTram);
             
             errores.add(error);  
            
           }
      
		   // Comprobar errores de circuitos de firma
		   comprobarErrorFirmaCircuito(errores, defProcVO.getListaFlujosFirma(), traductor, con);


       }catch(SQLException e){
           e.printStackTrace();
       }finally{
           try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return errores;
   }
   
    
    /**
     * Reúne los errores que se hayan podido producir en la creación de los objetos pertinentes a los datos 
	 * de la firma del documento de trámite si esta es de usuario
     * Por ahora sólo se comprueba que haya un usuario en caso de haber una firma.
     * 
     * @param tramite
     * @return 
     */
    private void comprobarErroresFirmaDocumento(List<ErrorImportacionXPDL> errores, DefinicionTramitesValueObject tramite, 
            TraductorAplicacionBean traductor, Connection conexion) throws SQLException {
        
		String codigoUsuario;
		String logUsuario;
                String nifUsuario;
		
        // Estamos en el tramite, tenemos que recorres los documentos que vienen en listas independientes
		for(int i=0; i<tramite.getListaCodigosDoc().size(); i++){
			String codigoDoc = (String) tramite.getListaCodigosDoc().get(i);
			String nombreDoc = (String) tramite.getListaNombresDoc().get(i);
			
			// Necesitamos comprobar el tipo de firma, porque habrá documentos que no requieren firma (firma vacia) o con firma de flujo (L)
			// cuyo usuario firmante no habra que comprobar. 
			// Para los documentos con firma tipo tramitador (T) no hay en E_DOT_FIR por lo que tampoco se tienen que comprobar
			String firmaDoc = (String) tramite.getListaFirmaDoc().get(i);
			if(!firmaDoc.isEmpty() && !firmaDoc.equals("L") && !firmaDoc.equals("T")){
				//Obtenemos el codigo y log de usuario que consta como firmante unico del documento
				codigoUsuario = tramite.getListaFirmasDocumentoIdsUsuario().get(i);
				logUsuario = tramite.getListaFirmasDocumentoLogsUsuario().get(i);
                                nifUsuario =  tramite.getListaFirmasDocumentoDnisUsuario().get(i);

				String textoError = String.format("%s %s - %s", traductor.getDescripcion("xpdlDocumento"), codigoDoc, nombreDoc);
				comprobarErrorFirmaUsuario(errores, textoError, codigoUsuario, logUsuario, nifUsuario, traductor, conexion);
			}
		}
		
    }       
    
	/**
	 * Reúne los errores que se hayan podido producir en la creación de los objetos pertinentes a los datos 
	 * de la firma del documento de trámite si esta es de flujo
     * Por ahora sólo se comprueba que haya un usuario en caso de haber una firma.
	 * 
	 * @param errores
	 * @param firmaCircuito
	 * @param traductor
	 * @param conexion
	 * @throws SQLException 
	 */
    private void comprobarErrorFirmaCircuito(List<ErrorImportacionXPDL> errores,
        List<FirmaFlujoVO> listaFlujos, TraductorAplicacionBean traductor, Connection conexion) throws SQLException {
        Integer codigoFlujo;
        String nombreFlujo;
        Integer codigoUsuario = null;
        String logUsuario = null;
        String  nifUsuario =  null;
        
            for(FirmaFlujoVO flujo : listaFlujos){
            codigoFlujo = flujo.getId();
            nombreFlujo = flujo.getNombre();

                for(FirmaCircuitoVO firmaCircuito : flujo.getListaFirmasCircuito()){
                    // Obtenemos el codigo y log de usuario que consta como firmante de un circuito de firmas del documento
                    codigoUsuario = firmaCircuito.getIdUsuario();
                    logUsuario = firmaCircuito.getLogUsuario();
                    nifUsuario =  firmaCircuito.getDniUsuario();

                    String textoError = String.format("%s %s - %s", traductor.getDescripcion("etiqFlujo"), codigoFlujo, nombreFlujo);
                    comprobarErrorFirmaUsuario(errores, textoError, String.valueOf(codigoUsuario), logUsuario, nifUsuario, traductor, conexion);
                }
            }
    }

	/**
	 * Muestra el error de usuario firmante incorrecto
	 * @param tipoFirma 0 si es firma de usuario, el usuario se obtiene de E_DOT_FIR; 1 si es firma de flujo, el usuario se obtiene de FIRMA_CIRCUITO
	 * @param erroresborrarFirmaDocumento
	 * @param codigoUsuario
	 * @param logUsuario
	 * @param traductor
	 * @param conexion
	 * @throws SQLException 
	 */
    private void comprobarErrorFirmaUsuario(List<ErrorImportacionXPDL> errores, String textoErrorPorTipoFirma, String codigoUsuario, String logUsuario, String nifUsuario, 
            TraductorAplicacionBean traductor, Connection conexion) throws SQLException {
        if (codigoUsuario == null || logUsuario == null || nifUsuario == null || existeUsuarioPorDni(nifUsuario, conexion) == null) {
            ErrorImportacionXPDL error = new ErrorImportacionXPDL();
			StringBuilder descError = new StringBuilder(textoErrorPorTipoFirma).append(": ")
					.append(traductor.getDescripcion(ERR_NO_EXISTE_USUARIO_FIRMA_FLUJO))
					.append(" ").append(codigoUsuario).append(" - ").append(logUsuario).append(", con NIF:").append(nifUsuario);
            error.setCodError(XPDL_ERROR_NO_EXISTE_USUARIO_FIRMA_FLUJO);
            error.setDescripcionError(descError.toString());
            errores.add(error);                            
            log.debug(String.format("El usuario no encontrado tiene codigo %s y login %s", codigoUsuario, logUsuario));
        }
    }    
   /**
    *  Modifica un determinado procedimiento
    * @param defProcVO: Objeto con la definición del procedimiento
    * @param params: Parámetros de conexión a la base de datos
    * @param con: Conexión a la base de datos
    * @return 
    * @throws es.altia.common.exception.TechnicalException
    * @throws es.altia.util.conexion.BDException
    * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
    */
   public boolean modificarProcedimiento(DefinicionProcedimientosValueObject defProcVO, AdaptadorSQLBD abd, Connection conexion)
            throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        String sqlDoc = "";
        String sqlBorrar = "";
        String codMunicipio = defProcVO.getCodMunicipio();
        String codigo = defProcVO.getTxtCodigo();
        int res = 0;
        int res1 = 0;
        int res3 = 0;
        int resBorrar =0;
        int resEnlaces = 0;
        int resBorrarDPML =0;
        boolean procedimientoActualizado = false;
        int resRoles = 0;

        Statement stmt = null;
        ResultSet rs = null;
        
        if(log.isDebugEnabled()) log.debug("Entra en el modificar del DAO");

        try{
            
            // PESTAÑA DATOS
            if(log.isDebugEnabled()) log.debug("el tipo de plazo es : " + defProcVO.getTipoPlazo());

            sql = "UPDATE E_PRO SET " +  pro_mun + "=" + defProcVO.getCodMunicipio() + "," +
                    pro_cod + "='" + defProcVO.getTxtCodigo() + "',";
            if(defProcVO.getFechaLimiteHasta() == null || defProcVO.getFechaLimiteHasta().equals(""))
                sql += pro_flh + "=null,";
            else
                sql += pro_flh + "=" + abd.convertir("'"+defProcVO.getFechaLimiteHasta()+"'",
                       AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ",";
            sql += pro_fld + "=" + abd.convertir("'"+defProcVO.getFechaLimiteDesde()+"'",
                       AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                    "," + pro_are + "=" + defProcVO.getCodArea() + "," ;
            if(defProcVO.getPlazo() == null || defProcVO.getPlazo().equals(""))
                sql += pro_plz + "=null," + pro_und + "=null,";
            else
                sql += pro_plz + "=" + defProcVO.getPlazo() + "," + pro_und + "='" + defProcVO.getTipoPlazo() + "',";
            sql += pro_sil + "=" + defProcVO.getTipoSilencio() +
                    "," + pro_tip + "=" +
                    defProcVO.getCodTipoProcedimiento()+ "," + pro_ini + "=" + defProcVO.getCodTipoInicio() + "," +
                    pro_est + "=" + defProcVO.getCodEstado() + "," + pro_din + "=" + defProcVO.getDisponible() +
                    "," + pro_tin + "=" + defProcVO.getTramitacionInternet() + "," + pro_loc + "=" +
                    defProcVO.getLocalizacion() + ", ";

            if (defProcVO.getDescripcionBreve() == null || defProcVO.getDescripcionBreve().equals("")) {
                sql += pro_des + " = " + null;
            } else {
                sql += pro_des + " = '" + defProcVO.getDescripcionBreve() + "'";
            }


            if(defProcVO.getPorcentaje()!=null && defProcVO.getPorcentaje().length()>0)
                sql += ",PRO_PORCENTAJE=" + defProcVO.getPorcentaje();
            else
                sql += ",PRO_PORCENTAJE=" + null;
            
            
            /*** PLUGIN DE FINALIZACIÓN NO CONVENCIONAL DE EXPEDIENTE ***/
            if(StringOperations.stringNoNuloNoVacio(defProcVO.getCodServicioFinalizacion()) && StringOperations.stringNoNuloNoVacio(defProcVO.getImplClassServicioFinalizacion())){
                // Si hay plugin de finalización no convencional para el procedimiento                
                sql += ",PLUGIN_ANULACION_NOMBRE='" + defProcVO.getCodServicioFinalizacion() + "',PLUGIN_ANULACION_IMPLCLASS='" + defProcVO.getImplClassServicioFinalizacion() + "'";                
            }else
                sql += ",PLUGIN_ANULACION_NOMBRE=NULL,PLUGIN_ANULACION_IMPLCLASS=NULL";

            /*** PLUGIN DE FINALIZACIÓN NO CONVENCIONAL DE EXPEDIENTE ***/
            
            if(defProcVO.getInteresadoOblig() != null)
                sql += ",PRO_INTOBL=" + defProcVO.getInteresadoOblig() + ",";
            else
                sql += ",PRO_INTOBL=0,";
            
            //
            //  Lanbide 
            //
            //
            sql += "PRO_RESTRINGIDO = " + Integer.parseInt(defProcVO.getRestringido()) + 
                    ",PRO_LIBRERIA = " + Integer.parseInt(defProcVO.getBiblioteca()) +
                    ",PRO_EXPNUMANOT = " + defProcVO.getNumeracionExpedientesAnoAsiento() +
                    ",PRO_SOLOWS = " + Integer.parseInt(defProcVO.getSoloWS());
            
            
            sql += " WHERE " + pro_mun + "=" + codMunicipio + " AND " +
                    pro_cod + "='" + codigo + "'";

            if(log.isDebugEnabled()) log.debug(sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(log.isDebugEnabled()) log.debug("las filas afectadas en el modificar de E_PRO son res: " + res);
            ps.close();

            sql = "UPDATE E_PML SET " + pml_valor + "='" + defProcVO.getTxtDescripcion() + "' WHERE " + pml_mun + "=" + codMunicipio +
                    " AND " + pml_cod + "='" + codigo + "' AND " + pml_cmp + "='NOM' AND " +
                    pml_leng + "='"+idiomaDefecto+"'";

            if(log.isDebugEnabled()) log.debug(sql);

            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            if(log.isDebugEnabled()) log.debug("las filas afectadas en el modificar de E_PML son res1: " + res1);
            ps.close();

            Vector listaCodUnidadInicio = defProcVO.getListaCodUnidadInicio();
            if(listaCodUnidadInicio != null) {
                sqlBorrar = "DELETE FROM E_PUI WHERE " + pui_mun + "=" + codMunicipio + " AND " + pui_pro + "='" + codigo + "'";
                if(log.isDebugEnabled()) log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                if(log.isDebugEnabled()) log.debug(ps.toString());
                resBorrar = ps.executeUpdate();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_PUI son resBorrar: " + resBorrar);
                ps.close();
                for (int i = 0; i < listaCodUnidadInicio.size(); i++) {
                    String codigoUnidadInicio = this.recuperaCodigoUnidadInicio(listaCodUnidadInicio.elementAt(i).toString(), conexion);
                    // no hace falta validar que UOR_COD no es nulo dado que esto se hace en la validación previa de existencia de una unidad de inicio
                    sql = "INSERT INTO E_PUI (" + pui_mun + "," + pui_pro + "," + pui_cod + ") VALUES ("
                            + defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() + "',"
                            + codigoUnidadInicio + ")";
                    if (log.isDebugEnabled()) {
                        log.debug(sql);
                    }

                    ps = conexion.prepareStatement(sql);
                    res3 = ps.executeUpdate();
                    if(log.isDebugEnabled()) log.debug("las filas afectadas en el insert  de E_PUI son res3: " + res3);
                    ps.close();
                }
            }

            // PESTAÑA DOCUMENTOS

            Vector listaNombresDoc = defProcVO.getListaNombresDoc();
            Vector listaCondicionDoc = defProcVO.getListaCondicionDoc();
            Vector listaCodigosDoc = defProcVO.getListaCodigosDoc();
            ArrayList <FirmasDocumentoProcedimientoVO> listaSinaturas = defProcVO.getFirmasDocumentosProcedimiento();
            
            for(int j=0;j<listaNombresDoc.size();j++) {
                String existeDOE = "no";
                sql= "SELECT " + doe_num + " FROM E_DOE WHERE " + doe_mun + "=" + defProcVO.getCodMunicipio() +
                        " AND " + doe_pro + "='" + defProcVO.getTxtCodigo() + "' AND " + doe_cod + "=" +
                        listaCodigosDoc.elementAt(j);
                if(log.isDebugEnabled()) log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    existeDOE = "si";
                }
                rs.close();
                stmt.close();
                if("no".equals(existeDOE)) {

                    sqlBorrar = "DELETE FROM E_DPML WHERE " + dpml_mun + "=" + codMunicipio + " AND " + dpml_pro + "='" +
                            codigo + "' AND " + dpml_dop + "=" + listaCodigosDoc.elementAt(j);
                    if(log.isDebugEnabled()) log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrarDPML += ps.executeUpdate();
                    ps.close();
                    
                    sqlBorrar	= "DELETE FROM E_ENT WHERE " + ent_mun + "=" + codMunicipio + " AND " + ent_pro + "='"	+
                        codigo + "'";
                    if(log.isDebugEnabled()) log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrar = ps.executeUpdate();
                    ps.close();
                    if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_ENT son: " + resBorrar);



                    sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio + " AND " +
                            dop_pro + "='" + codigo + "' AND " + dop_cod + "=" + listaCodigosDoc.elementAt(j);
                    if(log.isDebugEnabled()) log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrar += ps.executeUpdate();
                    ps.close();

                    sqlDoc = "INSERT INTO E_DOP (" + dop_mun + "," + dop_pro + "," + dop_cod +
                            ") VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ")";
                    if(log.isDebugEnabled()) log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();

                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'NOM','"+idiomaDefecto+"','" +
                            listaNombresDoc.elementAt(j) + "')";
                    if(log.isDebugEnabled()) log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();

                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'CON','"+idiomaDefecto+"','" +
                            listaCondicionDoc.elementAt(j) + "')";
                    if(log.isDebugEnabled()) log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();
                
                }
                ArrayList <FirmasDocumentoProcedimientoVO> listaSinaturasDocumento = new  ArrayList <FirmasDocumentoProcedimientoVO>();
                
                for (FirmasDocumentoProcedimientoVO sinatura : listaSinaturas) {
                    if (listaCodigosDoc.elementAt(j).equals(sinatura.getCodDocumento())){ 
                        listaSinaturasDocumento.add(sinatura);
                        break;
                    }
                }
                
                FirmasDocumentoProcedimientoDAO.getInstance().guardarListaFirmas(listaSinaturasDocumento,codMunicipio, codigo, (String) listaCodigosDoc.elementAt(j), conexion);
            }
            if(log.isDebugEnabled()){
                log.debug("las filas afectadas en el eliminar de E_DPML son resBorrarDPML: " + resBorrarDPML);
                log.debug("las filas afectadas en el eliminar de E_DOP son resBorrar: " + resBorrar);
            }

            if(listaNombresDoc.size() != 0) {
                Vector listaDOE = new Vector();
                sql= "SELECT " + doe_cod + " FROM E_DOE WHERE " + doe_mun + "=" + defProcVO.getCodMunicipio() +
                        " AND " + doe_pro + "='" + defProcVO.getTxtCodigo() + "'";
                for(int i=0;i<listaNombresDoc.size();i++) {
                    sql += " AND " + doe_cod + "<>" + listaCodigosDoc.elementAt(i);
                }
                if(log.isDebugEnabled()) log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    String codDoe = rs.getString(doe_cod);
                    listaDOE.addElement(codDoe);
                }
                rs.close();
                stmt.close();
                sqlBorrar = "DELETE FROM E_DPML WHERE " + dpml_mun + "=" + codMunicipio +
                        " AND " + dpml_pro + "='" + codigo + "'";
                for(int i=0;i<listaNombresDoc.size();i++) {
                    sqlBorrar += " AND " + dpml_dop + "<>" + listaCodigosDoc.elementAt(i);
                }
                for(int i=0;i<listaDOE.size();i++) {
                    sqlBorrar += " AND " + dpml_dop + "<>" + listaDOE.elementAt(i);
                }
                if(log.isDebugEnabled()) log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                resBorrarDPML = ps.executeUpdate();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_DPML son: " + resBorrarDPML);
                ps.close();

                sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio +
                        " AND " + dop_pro + "='" + codigo + "'";
                for(int i=0;i<listaNombresDoc.size();i++) {
                    sqlBorrar += " AND " + dop_cod + "<>" + listaCodigosDoc.elementAt(i);
                }
                for(int i=0;i<listaDOE.size();i++) {
                    sqlBorrar += " AND " + dop_cod + "<>" + listaDOE.elementAt(i);
                }
                if(log.isDebugEnabled()) log.debug(sqlBorrar);
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_DOP son: " + resBorrar);
                ps.close();
            }
            if(listaNombresDoc.size() == 0) {
                Vector listaDOE = new Vector();
                sql= "SELECT " + doe_cod + " FROM E_DOE WHERE " + doe_mun + "=" + defProcVO.getCodMunicipio() +
                        " AND " + doe_pro + "='" + defProcVO.getTxtCodigo() + "'";
                if(log.isDebugEnabled()) log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    String codDoe = rs.getString(doe_cod);
                    listaDOE.addElement(codDoe);
                }
                rs.close();
                stmt.close();
                for(int i=0;i<listaDOE.size();i++) {

                    sqlBorrar = "DELETE FROM E_DPML WHERE " + dpml_mun + "=" + codMunicipio + " AND " + dpml_pro + "='" +
                            codigo + "' AND " + dpml_dop + "<>" + listaDOE.elementAt(i);
                    if(log.isDebugEnabled()) log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrarDPML += ps.executeUpdate();
                    ps.close();

                    sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio + " AND " +
                            dop_pro + "='" + codigo + "' AND " + dop_cod + "<>" + listaDOE.elementAt(i);
                    if(log.isDebugEnabled()) log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrar += ps.executeUpdate();
                    ps.close();
                }
                if(log.isDebugEnabled()){
                    log.debug("las filas afectadas en el eliminar de E_DPML son resBorrarDPML: " + resBorrarDPML);
                    log.debug("las filas afectadas en el eliminar de E_DOP son resBorrar: " + resBorrar);
                }

                if(listaDOE.size() == 0) {
                    sqlBorrar = "DELETE FROM E_DPML WHERE " + dpml_mun + "=" + codMunicipio + " AND " + dpml_pro + "='" +
                            codigo + "'";
                    if(log.isDebugEnabled()) log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrarDPML = ps.executeUpdate();
                    if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_DPML son: " + resBorrarDPML);
                    ps.close();

                    sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio + " AND " +
                            dop_pro + "='" + codigo + "'";
                    if(log.isDebugEnabled()) log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrar = ps.executeUpdate();
                    if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_DOP son: " + resBorrar);
                    ps.close();
                }
            }
                
            // PESTAÑA DE CAMPOS
            Vector listaCodCampos = defProcVO.getListaCodCampos();
            Vector listaDescCampos = defProcVO.getListaDescCampos();
            Vector listaCodPlantilla = defProcVO.getListaCodPlantilla();
            Vector listaCodTipoDato = defProcVO.getListaCodTipoDato();
            Vector listaTamano = defProcVO.getListaTamano();
            Vector listaMascara = defProcVO.getListaMascara();
            Vector listaObligatorio = defProcVO.getListaObligatorio();
            Vector listaRotulo = defProcVO.getListaRotulo();
            Vector listaActivos = defProcVO.getListaActivos();
            Vector listaOcultos = defProcVO.getListaOcultos();
            Vector listaBloqueados = defProcVO.getListaBloqueados();
            Vector listaPlazoFecha = defProcVO.getListaPlazoFecha();
            Vector listaCheckPlazoFecha = defProcVO.getListaCheckPlazoFecha();
            Vector listaValidacion = defProcVO.getListaValidacion();
            Vector listaOperacion = defProcVO.getListaOperacion();
            Vector listaAgrupacionCampo = defProcVO.getListaAgrupacionesCampo();
            Vector listaPosicionesX = defProcVO.getListaPosicionesX();
            Vector listaPosicionesY = defProcVO.getListaPosicionesY();
            String plantilla="", desplegable="";
            String valida = "";
            
            /** SE COMPRUEBA CUALES SON LOS CAMPOS QUE NO ESTÁN EN LA NUEVA DEFINICIÓN Y QUE VAN A SER O ELIMINADOS O MARCAR COMO ELIMINADOS **/
            boolean eliminados = this.eliminarCamposSuplementariosProcedimientoViejos(listaCodCampos, defProcVO.getTxtCodigo(), codMunicipio, conexion);
            log.debug(" Los campos suplementarios de procedimiento " + eliminados);
                                    
            /* Realizamos la comprobación de que campos pueden ser borrados y cuales no */
            HashMap insertar = new HashMap();
            boolean inserta = true;
            PreparedStatement statExiste = null;
            ResultSet rsExiste = null;
            for(int j=0;j<listaCodCampos.size();j++) {
                inserta = true;
                /* Comprobamos si el codigo del campo existe en otras tablas */
                if(log.isDebugEnabled()) log.debug("CAMPO ACTIVO = " + (String)listaActivos.elementAt(j));
                if ("NO".equalsIgnoreCase((String)listaActivos.elementAt(j))) {
                    String sqlExiste = "SELECT COUNT (*)  FROM e_pca WHERE e_pca.pca_cod =?";
                    sqlExiste += " AND e_pca.pca_pro = ?";
                    sqlExiste += " AND e_pca.pca_mun = ?";
                    sqlExiste += " AND ( e_pca.pca_cod IN (SELECT tnu_cod FROM e_tnu) OR e_pca.pca_cod IN (SELECT tfe_cod FROM e_tfe) OR e_pca.pca_cod IN (SELECT ttl_cod FROM e_ttl) OR e_pca.pca_cod IN (SELECT txt_cod FROM e_txt) OR e_pca.pca_cod IN (SELECT TDE_COD FROM E_TDE) OR e_pca.pca_cod IN (SELECT tnuc_cod FROM e_tnuc) OR e_pca.pca_cod IN (SELECT tfec_cod FROM e_tfec)OR e_pca.pca_cod IN (SELECT TDEX_cod FROM E_TDEX))";

                    statExiste = conexion.prepareStatement(sqlExiste);
                    int i=1;
                    statExiste.setString(i++,(String)listaCodCampos.elementAt(j));
                    statExiste.setString(i++,defProcVO.getTxtCodigo());
                    statExiste.setInt(i++,Integer.parseInt(defProcVO.getCodMunicipio()));

                    if(log.isDebugEnabled()) log.debug("CONSULTA = " + sqlExiste);

                    rsExiste = statExiste.executeQuery();
                    if (rsExiste.next()) {
                        if (rsExiste.getInt(1) <= 0) { // Existen campos en otras tablas, NO SE DEBE BORRAR EL CAMPO FISICAMENTE DE LA TABLA
                            inserta = false;
                        }
                    }
                    rsExiste.close();
                    statExiste.close();
                }
                insertar.put((String)listaCodCampos.elementAt(j),new Boolean(inserta));
            }
            if(log.isDebugEnabled()) log.debug("CAMPOS A INSERTAR: = " + insertar);
            /* Fin de la comprobación de los campos que pueden ser borrados y cuales no*/


            // SE BORRAN SOLO LOS CAMPOS SUPLEMENTARIOS ASOCIADOS AL PROCEDIMIENTO Y QUE SE ENCUENTREN ENTRE LOS QUE ESTÁN EN LA NUEVA DEFINICIÓN, PORQUE
            // LOS VIEJOS YA EXISTENTES YA HAN SIDO TRATADOS.
            String aux = "";
            for(int i=0;i<listaCodCampos.size();i++){
                aux = aux + "'" + listaCodCampos.get(i) + "'";
                if(listaCodCampos.size() - i>1){
                    aux = aux + ",";
                }
            }
            
            //sqlBorrar = "DELETE FROM E_PCA WHERE " + pca_mun + "=" + codMunicipio + " AND " + pca_pro + "='" + codigo + "'";
            if(listaCodCampos!=null && listaCodCampos.size()>0){
                // SOLO SI EN LA NUEVA DEFINICIÓN HAY CAMPOS SUPLEMENTARIOS DE PROCEDIMIENTO, SE PROCEDE A ELIMINAR LA DEFINICIÓN QUE HUBIESE
                // DE ESTOS CAMPOS.
                sqlBorrar = "DELETE FROM E_PCA WHERE " + pca_mun + "=" + codMunicipio + " AND " + pca_pro + "='" + codigo + "' AND PCA_COD IN (" + aux + ")";
                if(log.isDebugEnabled()) log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_PCA son :::::::::::::: : " + resBorrar);
                ps.close();
                
                sqlBorrar = "DELETE FROM EXPRESION_CAMPO_NUM_PROC WHERE COD_ORGANIZACION = "  + codMunicipio + " AND COD_PROCEDIMIENTO = '" + codigo + "' AND COD_CAMPO IN (" + aux + ")";
                if(log.isDebugEnabled()) log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de EXPRESION_CAMPO_NUM_PROC son :::::::::::::: : " + resBorrar);
                ps.close();
                
                sqlBorrar = "DELETE FROM EXPRESION_CAMPO_CAL_PROC WHERE COD_ORGANIZACION = " + codMunicipio + " AND COD_PROCEDIMIENTO ='" + codigo + "' AND COD_CAMPO IN (" + aux + ")";
                if(log.isDebugEnabled()) log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de EXPRESION_CAMPO_CAL_PROC son :::::::::::::: : " + resBorrar);
                ps.close();
                
                sqlBorrar = "DELETE FROM E_PCA_GROUP WHERE PCA_PRO = '" + codigo+"'";
                if(log.isDebugEnabled()) log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_PCA_GROUP son :::::::::::::: : " + resBorrar);
                ps.close();
            }
            
            Boolean seInserta = new Boolean(true);
            for(int j=0;j<listaCodCampos.size();j++) {
                seInserta = (Boolean)insertar.get((String)listaCodCampos.elementAt(j));
                if (seInserta.booleanValue()) {
                        if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                            desplegable = (String)listaCodPlantilla.elementAt(j);
                        } else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                            desplegable = (String)listaCodPlantilla.elementAt(j);
                        } else {
                            plantilla = (String)listaCodPlantilla.elementAt(j);
                            desplegable = "";
                        }
                        sql = "INSERT INTO E_PCA (" + pca_mun + "," + pca_pro + "," + pca_cod + "," + pca_des + "," +
                                pca_plt + "," + pca_tda + "," + pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," +
                                pca_rot + "," + pca_activo + "," + pca_desplegable +",PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO, PERIODO_PLAZO,PCA_GROUP," +
                                " PCA_POS_X, PCA_POS_Y ) VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                                defProcVO.getTxtCodigo() + "','" + listaCodCampos.elementAt(j) + "','" + listaDescCampos.elementAt(j) +
                                "'," + plantilla + ",'" + listaCodTipoDato.elementAt(j) + "'," + listaTamano.elementAt(j);
                        if(listaMascara.elementAt(j) == null || "".equals(listaMascara.elementAt(j)) || " ".equals(listaMascara.elementAt(j))) {
                            sql += ",null,";
                        } else {
                            sql += ",'" +  listaMascara.elementAt(j) + "',";
                        }
                        sql += listaObligatorio.elementAt(j) + "," + j + ",'" + listaRotulo.elementAt(j) + "','" +
                               ((String)listaActivos.elementAt(j)).toUpperCase() + "','" + desplegable+ "','"+listaOcultos.elementAt(j) +"','"+
                                listaBloqueados.elementAt(j) + "',";
                        
                        if((listaPlazoFecha!=null) && (listaCheckPlazoFecha!=null)){                        
                            if((listaPlazoFecha.size()==listaCodCampos.size()) && (listaCheckPlazoFecha.size()==listaCodCampos.size())){
                                    if(listaPlazoFecha.elementAt(j) == null || "".equals(listaPlazoFecha.elementAt(j)) || " ".equals(listaPlazoFecha.elementAt(j))) {
                                        sql += "null, null";
                                    } else {
                                        sql += "'" +  listaPlazoFecha.elementAt(j) + "','" + listaCheckPlazoFecha.elementAt(j) +"'";
                                    }
                               } else{ //El tamanho no es el mismo
                                  sql += "null, null";
                               }
                        } else {// son nulas listaPlazoFecha o listaCheckPlazoFecha
                            sql += "null, null";
                        } 

                        if(listaAgrupacionCampo != null && listaAgrupacionCampo.size() > 0)
                        {
                            if(listaAgrupacionCampo.elementAt(j) == null || "".equals(listaAgrupacionCampo.elementAt(j)) 
                                || " ".equals(listaAgrupacionCampo.elementAt(j))){
                                sql += ",null";
                            }else{
                                sql += ",'" + listaAgrupacionCampo.elementAt(j) + "'";
                            }
                        }else{
                            sql += ",null";
                        }
                            
                        if (listaAgrupacionCampo == null){
                            sql +=",null";
                        }else if(listaPosicionesX != null && listaAgrupacionCampo.size() > 0){
                            if(listaPosicionesX.elementAt(j) == null || "".equals(listaPosicionesX.elementAt(j))){
                                sql +=",null";
                            }else{
                                sql +="," + listaPosicionesX.elementAt(j);
                            }
                        }else{
                            sql +=",null";
                        }        

                        if (listaAgrupacionCampo == null){
                            sql +=",null)";
                        }else if(listaPosicionesY != null && listaAgrupacionCampo.size() > 0){
                            if(listaPosicionesY.elementAt(j) == null || "".equals(listaPosicionesY.elementAt(j))){
                                sql +=",null)";
                            }else{
                                sql +="," + listaPosicionesY.elementAt(j) + ")";
                            }
                        }else{
                            sql +=",null)";
                        }
                        
                        if(log.isDebugEnabled()) log.debug(sql);
                        
                        log.debug("sql: " + sql);
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();                    
                    
                        String tip_dat = (String) listaCodTipoDato.elementAt(j);
                        valida = (String) listaValidacion.elementAt(j);            
                        if (tip_dat.trim().equals("1") && valida!= null &&!"".equals(valida))
                        {   
                            //valida = listaValidacion.elementAt(contador_expresion).toString();
                            valida = valida.replace("/&lt;/g","<");  
                            valida = valida.replace("/&gt;/g",">"); 
                            valida = valida.replace("&lt;","<");  
                            valida = valida.replace("&gt;",">"); 


                            sql = " INSERT INTO EXPRESION_CAMPO_NUM_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,EXPRESION) VALUES " + 
                              " ("+ defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() +"','" +listaCodCampos.elementAt(j) +"','" + valida +                                
                              "')";                                                 
                            ps.executeUpdate(sql);                                        
                        }
                        if (tip_dat.trim().equals("8") || tip_dat.trim().equals("9"))
                        {                               
                            valida = listaOperacion.elementAt(j).toString();
                            if (!"".equals(valida))
                            {
                                sql = " INSERT INTO EXPRESION_CAMPO_CAL_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,TIPO_DATO,EXPRESION) VALUES " + 
                                  " ("+ defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() +"','" +listaCodCampos.elementAt(j) +"',"+tip_dat+",'" + valida +                                
                                  "')";                                                 
                                ps.executeUpdate(sql);                                            
                            }
                        }
                }
            }
            
            Vector listaCodAgrupacion = defProcVO.getListaCodAgrupaciones();
            Vector listaDescAgrupacion = defProcVO.getListaDescAgrupaciones();
            Vector listaOrdenAgrupacion = defProcVO.getListaOrdenAgrupaciones();
            Vector listaAgrupacionActiva = defProcVO.getListaAgrupacionesActivas();

            for(int j=0;j<listaCodAgrupacion.size();j++){
                if ("SI".equalsIgnoreCase((String)listaAgrupacionActiva.elementAt(j))) {
                   sql = "INSERT INTO E_PCA_GROUP (PCA_ID_GROUP,PCA_DESC_GROUP,PCA_ORDER_GROUP,PCA_PRO,PCA_ACTIVE) "+
                           "VALUES ('"  + listaCodAgrupacion.elementAt(j) + "','" + listaDescAgrupacion.elementAt(j) + "'," +
                                                   listaOrdenAgrupacion.elementAt(j) + ",'" + defProcVO.getTxtCodigo() + "','" + 
                                                   ((String)listaAgrupacionActiva.elementAt(j)).toUpperCase() +"')";

                    if(log.isDebugEnabled()) log.debug(sql);

                    ps = conexion.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                }
            }
                
            // PESTAÑA DE ENLACES
            Vector listaCodEnlaces = defProcVO.getListaCodEnlaces();
            Vector listaDescEnlaces = defProcVO.getListaDescEnlaces();
            Vector listaUrlEnlaces = defProcVO.getListaUrlEnlaces();
            Vector listaEstadoEnlaces = defProcVO.getListaEstadoEnlaces();

            sqlBorrar = "DELETE FROM E_ENP WHERE " + enp_mun + "=" + codMunicipio + " AND " + enp_pro + "='" + codigo + "'";
            if(log.isDebugEnabled()) log.debug(sqlBorrar);
            ps = conexion.prepareStatement(sqlBorrar);
            resBorrar = ps.executeUpdate();
            if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_ENP son :::::::::::::: : " + resBorrar);
            ps.close();
            for(int m=0;m<listaCodEnlaces.size();m++) {
                sql = "INSERT INTO E_ENP (" + enp_mun + "," + enp_pro + "," + enp_cod + "," + enp_des + "," +
                        enp_url + "," + enp_est + ") VALUES (" + defProcVO.getCodMunicipio() + ",'" +
                        defProcVO.getTxtCodigo() + "'," + listaCodEnlaces.elementAt(m) + ",'" +
                        listaDescEnlaces.elementAt(m) + "','" + listaUrlEnlaces.elementAt(m) + "',";
                if("S".equals(listaEstadoEnlaces.elementAt(m))) {
                    sql += "1)";
                } else if("N".equals(listaEstadoEnlaces.elementAt(m))) {
                    sql += "0)";
                }
                if(log.isDebugEnabled()) log.debug(sql);
                ps = conexion.prepareStatement(sql);
                resEnlaces = ps.executeUpdate();
                ps.close();
            }
            if(listaCodEnlaces.size() == 0 ) {
                resEnlaces = 1;
            }

            // PESTAÑA DE ROLES
            Vector listaCodRoles = defProcVO.getListaCodRoles();
            Vector listaDescRoles = defProcVO.getListaDescRoles();
            Vector listaPorDefecto = defProcVO.getListaPorDefecto();
            Vector listaConsultaWebRol = defProcVO.getListaConsultaWebRol();
            String porDefecto = "no";
            for(int v=0;v<listaPorDefecto.size();v++) {
                String pD = (String) listaPorDefecto.elementAt(v);
                if("1".equals(pD)) {
                    porDefecto = "si";
                    break;
                }
            }
            if("no".equals(porDefecto)) {
                listaPorDefecto.insertElementAt("1",0);
            }

            sqlBorrar = "DELETE FROM E_ROL WHERE " + rol_mun + "=" + codMunicipio + " AND " + rol_pro + "='" + codigo + "'";
            if(log.isDebugEnabled()) log.debug(sqlBorrar);
            ps = conexion.prepareStatement(sqlBorrar);
            resBorrar = ps.executeUpdate();
            if(log.isDebugEnabled()) log.debug("las filas afectadas en el eliminar de E_ROL son :::::::::::::: : " + resBorrar);
            ps.close();
            for(int m=0;m<listaCodRoles.size();m++) {
                sql = "INSERT INTO E_ROL (" + rol_mun + "," + rol_pro + "," + rol_cod + "," + rol_des + "," +
                        rol_pde + ",ROL_PCW) VALUES (" + defProcVO.getCodMunicipio() + ",'" +
                        defProcVO.getTxtCodigo() + "'," + listaCodRoles.elementAt(m) + ",'" +
                        listaDescRoles.elementAt(m) + "'," + listaPorDefecto.elementAt(m) + "," + listaConsultaWebRol.elementAt(m)+")";
                if(log.isDebugEnabled()) log.debug(sql);
                ps = conexion.prepareStatement(sql);
                resRoles = ps.executeUpdate();
                ps.close();
            }
            if(listaCodRoles.size() == 0 ) {
                resRoles = 1;
            }

            if(res !=0 && resEnlaces !=0 && resRoles !=0)  {
                // comentamos el cierre de la transacción dado que, la que se está utilizando se declara fuera del método
                // lo que provoca que si cerramos la conexion en este punto, se hagan commits sin posibilidad de rollback
                // en el resto llamadas a Daos en el método.
                //abd.finTransaccion(conexion);
                procedimientoActualizado = true;
				if(log.isDebugEnabled()){
					log.debug("MODIFICACION HECHA EN " + getClass().getName());
					log.debug("res VALE: " + res + " resEnlaces VALE: " + resEnlaces  +
								" resRoles VALE: " + resRoles);
					}
            } else {
                // en este caso podemos dejar abierto el rollback dado que la variable procedimientoActualizado será false y saldrá directamente en el método general
                abd.rollBack(conexion);
            }

        } catch (Exception ex) {
            
            ex.printStackTrace();
            if(log.isDebugEnabled()) log.error("Excepcion capturada en: " + this.getClass().getName());

        } 
        return procedimientoActualizado;
    }



     /**
     * Comprueba si hay expedientes de un determinado procedimiento,pendientes de tramitar en alguno de los trámites ya existentes que no se encuentran en la nueva definición
     * @param defVO: DefinicionProcedimientosValueObject con la nueva definición del procedimiento y de sus trámites
     * @param params: Parámetros de conexión a la base de datos
     * @return ArrayList<ErrorImportacionXPDL>: Listado con los trámites que no pueden ser actualizados porque hay expedientes pendientes de trámitar en los mismos.
     */
   public ArrayList<ErrorImportacionXPDL> tieneExpedientesPendientesTramitar(DefinicionProcedimientosValueObject defVO,int codIdioma,int codAplicacion, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int numero = -1;
        ArrayList<ErrorImportacionXPDL> errores = new ArrayList<ErrorImportacionXPDL>();

        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(codAplicacion);
        traductor.setIdi_cod(codIdioma);
        
        try{
            String sql = "";
            Vector<DefinicionTramitesValueObject> tramites = (Vector<DefinicionTramitesValueObject>)defVO.getTramites();
            String codProcedimiento = defVO.getTxtCodigo();
            String nombreProcedimento = defVO.getDescripcionBreve();

            // Se recuperan los trámites actuales del procedimiento
            sql = "SELECT TRA_COD,TRA_MUN,TML_VALOR,TRA_COU,TRA_PRO FROM E_TRA,E_TML WHERE TRA_COD=TML_TRA AND TRA_MUN=TML_MUN AND TRA_PRO=TML_PRO AND TRA_PRO=? AND TRA_FBA IS NULL";
            log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setString(1,codProcedimiento);
            rs = ps.executeQuery();

            ArrayList<DefinicionTramitesValueObject> tramitesActuales = new ArrayList<DefinicionTramitesValueObject>();
            while(rs.next()){
                String codTramite   = rs.getString("TRA_COD");
                String codMunicipio = rs.getString("TRA_MUN");
                String nombre   = rs.getString("TML_VALOR");
                String codTramiteVisible  = rs.getString("TRA_COU");
                
                DefinicionTramitesValueObject aux = new DefinicionTramitesValueObject();
                aux.setNombreTramite(nombre);
                aux.setNumeroTramite(codTramiteVisible);
                aux.setCodMunicipio(codMunicipio);
                aux.setCodigoTramite(codTramite);                
                tramitesActuales.add(aux);
            }
            ps.close();
            rs.close();

            ArrayList<DefinicionTramitesValueObject> tramitesARevisar = new ArrayList<DefinicionTramitesValueObject>();
            /** Se recorren los trámites actuales y se comprueban con los nuevos */
            for(int i=0;i<tramitesActuales.size();i++){
                DefinicionTramitesValueObject actual = tramitesActuales.get(i);
                int contador  = 0;

                String nombreActual = actual.getNombreTramite().trim();
                String numeroActual = actual.getNumeroTramite().trim();

                for(int j=0;j<tramites.size();j++){
                    DefinicionTramitesValueObject nuevo = (DefinicionTramitesValueObject)tramites.get(j);

                    String nombreNuevo= nuevo.getNombreTramite().trim();
                    String numeroNuevo = nuevo.getNumeroTramite().trim();
                    log.debug("nuevo nombreTramite: " + nuevo.getNombreTramite());
                    log.debug("nuevo númeroTramite: " + nuevo.getNumeroTramite());


                    if(nombreNuevo.equals(nombreActual) && numeroNuevo.equals(numeroActual)){
                        // Entonces el trámite actual es uno de los existentes en la nueva definición
                        contador++;
                        break;
                    }  
                }// for

                if(contador==0){
                    tramitesARevisar.add(actual);
                }
            }// for


            log.debug("El número de trámites a revisar : " + tramitesARevisar.size());

            for(int i=0;i<tramitesARevisar.size();i++){
                DefinicionTramitesValueObject revisar = tramitesARevisar.get(i);
                String codTramite      = revisar.getCodigoTramite();
                String codMunicipio    = revisar.getCodMunicipio();
                String nombreTramite = revisar.getNombreTramite();

                // Se recuperan los trámites
                sql = "SELECT COUNT(CRO_NUM) AS NUM FROM E_CRO,E_EXP WHERE CRO_TRA=" + codTramite + " AND CRO_MUN=" + codMunicipio +
                        "  AND CRO_PRO='" + codProcedimiento + "' AND CRO_FEF IS NULL";
                log.debug(sql);

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

                rs.next();
                numero = rs.getInt("NUM");

                if(numero>0){
                    ErrorImportacionXPDL error = new ErrorImportacionXPDL();
                    error.setCodError(3);
                    error.setDescripcionError(traductor.getDescripcion(ERR_TRAMITES_PENDIENTES_TRAMITAR_PARTE_1) + " " + nombreTramite + " " +
                            traductor.getDescripcion(ERR_TRAMITES_PENDIENTES_TRAMITAR_PARTE_2) + " " + nombreProcedimento + " " + traductor.getDescripcion(ERR_TRAMITES_PENDIENTES_TRAMITAR_PARTE_3));

                    errores.add(error);
                }
            }//for

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
          try{
              if(ps!=null) ps.close();
              if(rs!=null) rs.close();
          } catch(SQLException e){
              e.printStackTrace();
          }
        }

        return errores;
   }



  /**
     * Recupera las unidades tramitadoras de un trámite y que no se encuentren ya en una determinada lista de unidades
     * @param dtVO: Objeto DefinicionTramitesValueObject con la definición del trámite
     * @param uors: Vector<UORDTO> con la colección de unidades tramitadoras del trámite que se va a importar
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return ArrayList<ExistenciaUorImportacionVO>
     */
    public ArrayList<ExistenciaUorImportacionVO> getUnidadesTramitadorasTramite(DefinicionTramitesValueObject dtVO,Vector<UORDTO> listaUnidades,String codProcedimiento,Connection con){

        ArrayList<ExistenciaUorImportacionVO> salida = new ArrayList<ExistenciaUorImportacionVO>();
        Statement st = null;        
        ResultSet rs = null;

        try{
            if(listaUnidades!=null){
                String sql2 = "";

                //for(Iterator it = listaUnidades.iterator();it.hasNext();){
                for(int i=0;i<listaUnidades.size();i++){
                    sql2 = sql2  + "'" + (String)listaUnidades.get(i).getUor_cod_vis() + "'";
                    if(listaUnidades.size()-i>1)
                        sql2 = sql2 + ",";
                }// for

                String nombreTramite         = dtVO.getNombreTramite().trim();
                String codigoVisibleTramite  = dtVO.getNumeroTramite();
                
                // Se recupera el código del trámite correspondiente al trámite porque no tiene porque coincidir con la nueva definición del trámite. La búsqueda se realiza
                // a partir del código visible del trámite, código de procedimiento y del nombre del trámite
                String sql ="SELECT TRA_COD,TRA_COU FROM E_TRA,E_TML WHERE TRA_COD=TML_TRA AND TRA_PRO=TML_PRO AND TRA_MUN=TML_MUN AND TRA_PRO='" + codProcedimiento + "' AND TRA_COU='" + codigoVisibleTramite + "'";
                st = con.createStatement();
                rs = st.executeQuery(sql);

                String codTramite ="";
                String codTramiteVisible ="";
                while(rs.next()){
                    codTramite = rs.getString("TRA_COD");
                    codTramiteVisible = rs.getString("TRA_COU");
                }
                
                log.debug("getUnidadesTramitadorasTramite codTramite: " + codTramite + ",codTramiteVisible:  " + codTramiteVisible);
                sql = "SELECT UOR_NOM,UOR_COD,UOR_COD_VIS FROM E_TRA_UTR,A_UOR WHERE TRA_PRO='" + codProcedimiento + "'  AND TRA_COD=" + codTramite +
                        " AND TRA_UTR_COD=UOR_COD AND UOR_COD NOT IN (" + sql2 + ")";
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    String nombre       = rs.getString("UOR_NOM");
                    String codVisible    = rs.getString("UOR_COD_VIS");
                    String codUor        = rs.getString("UOR_COD");

                    log.debug("codUor: " + codUor + ", codVisible: " + codVisible + ", nombre: " + nombre);                    
                    ExistenciaUorImportacionVO uor = new ExistenciaUorImportacionVO();
                    uor.setExiste(false);
                    uor.setNombre(nombre);
                    uor.setCodigoUor(codUor);
                    uor.setCodigoUorVisible(codVisible);
                    salida.add(uor);
                }// while

            }//if

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
    }



  /**
     * Recupera la unidad de inicio manual que tiene un trámite actualmentes unidades tramitadoras de un trámite y que no se encuentren ya en una determinada lista de unidades
     * @param dtVO: Objeto DefinicionTramitesValueObject con la definición del trámite
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return ArrayList<ExistenciaUorImportacionVO>
     */
    public ArrayList<ExistenciaUorImportacionVO> getUnidadInicioManualTramite(DefinicionTramitesValueObject dtVO,String codProcedimiento,Connection con){

        ArrayList<ExistenciaUorImportacionVO> salida = new ArrayList<ExistenciaUorImportacionVO>();
        Statement st = null;
        ResultSet rs = null;

        try{

                String nombreTramite         = dtVO.getNombreTramite().trim();
                String codigoVisibleTramite  = dtVO.getNumeroTramite();

                // Se recupera el código del trámite correspondiente al trámite porque no tiene porque coincidir con la nueva definición del trámite. La búsqueda se realiza
                // a partir del código visible del trámite, código de procedimiento y del nombre del trámite
                String sql ="SELECT TRA_COD,TRA_COU FROM E_TRA,E_TML WHERE TRA_COD=TML_TRA AND TRA_PRO=TML_PRO AND TRA_MUN=TML_MUN AND TRA_PRO='" + codProcedimiento + "' AND TRA_COU='" + codigoVisibleTramite + "'";
                st = con.createStatement();
                rs = st.executeQuery(sql);

                String codTramite ="";
                String codTramiteVisible ="";
                while(rs.next()){
                    codTramite = rs.getString("TRA_COD");
                    codTramiteVisible = rs.getString("TRA_COU");
                }

                log.debug("getUnidadesTramitadorasTramite codTramite: " + codTramite + ",codTramiteVisible:  " + codTramiteVisible);
                sql = "SELECT UOR_COD,UOR_NOM,UOR_COD_VIS FROM E_TRA,A_UOR WHERE TRA_UIN = UOR_COD AND TRA_PRO='" + codProcedimiento + "' AND  TRA_COD=" + codTramite + " AND TRA_COU='" + codigoVisibleTramite + "'";
                log.debug(sql);
                
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    String nombre       = rs.getString("UOR_NOM");
                    String codVisible    = rs.getString("UOR_COD_VIS");
                    String codUor        = rs.getString("UOR_COD");

                    log.debug("codUor: " + codUor + ", codVisible: " + codVisible + ", nombre: " + nombre);
                    ExistenciaUorImportacionVO uor = new ExistenciaUorImportacionVO();
                    uor.setExiste(false);
                    uor.setNombre(nombre);
                    uor.setCodigoUor(codUor);
                    uor.setCodigoUorVisible(codVisible);
                    salida.add(uor);
                }// while

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
    }



  /**
     * Da de baja los trámites existentes en un procedimiento y que no se encuentran en la nueva definición del mismo. 
     * @param defVO: DefinicionProcedimientosValueObject con la nueva definición del procedimiento y de sus trámites
     * @param params: Parámetros de conexión a la base de datos
     * @return ArrayList<ErrorImportacionXPDL>: Listado con los trámites que no pueden ser actualizados porque hay expedientes pendientes de trámitar en los mismos.
     */
   public boolean eliminarTramitesExistentesNoEstanEnNuevaDefinicion(DefinicionProcedimientosValueObject defVO,AdaptadorSQLBD abd, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;

        try{
            String sql;
            Vector<DefinicionTramitesValueObject> tramites = (Vector<DefinicionTramitesValueObject>)defVO.getTramites();
            String codProcedimiento = defVO.getTxtCodigo();
            
            // Se recuperan los trámites actuales del procedimiento no dados de baja
            sql = "SELECT TRA_COD,TRA_MUN,TML_VALOR,TRA_COU,TRA_PRO FROM E_TRA,E_TML WHERE TRA_COD=TML_TRA AND TRA_MUN=TML_MUN AND TRA_PRO=TML_PRO AND TRA_PRO=? AND TRA_FBA IS NULL";
            log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setString(1,codProcedimiento);
            rs = ps.executeQuery();

            ArrayList<DefinicionTramitesValueObject> tramitesActuales = new ArrayList<DefinicionTramitesValueObject>();
            while(rs.next()){
				DefinicionTramitesValueObject aux = new DefinicionTramitesValueObject();
				aux.setCodMunicipio(rs.getString("TRA_MUN")); // Codigo de municipio
				aux.setCodigoTramite(rs.getString("TRA_COD")); // Codigo del tramite
				aux.setNombreTramite(rs.getString("TML_VALOR")); // Nombre del tramite
				aux.setNumeroTramite(rs.getString("TRA_COU")); // Codigo visible del tramite
                tramitesActuales.add(aux);
            }
            ps.close();
            rs.close();

            ArrayList<DefinicionTramitesValueObject> tramitesARevisar = new ArrayList<DefinicionTramitesValueObject>();
            // Se recorren los trámites actuales y se comparan los codigos visibles de los nuevos con los ya existentes
			boolean existe;
			StringBuilder tramitesExistentes = new StringBuilder();
			StringBuilder tramitesNoExistentes = new StringBuilder();
            for(int i=0;i<tramitesActuales.size();i++){
                DefinicionTramitesValueObject tramiteActual = tramitesActuales.get(i);
				existe = false;
                String numeroActual = tramiteActual.getNumeroTramite().trim();

                for(int j=0;j<tramites.size();j++){
                    DefinicionTramitesValueObject nuevo = (DefinicionTramitesValueObject)tramites.get(j);

					String numeroNuevo = nuevo.getNumeroTramite().trim();
                    if(numeroNuevo.equals(numeroActual)){
						tramitesExistentes.append(String.format("%s ", numeroNuevo));
                        // Entonces el trámite actual es uno de los existentes en la nueva definición
                        existe = true;
                        break;
                    }
                }// for

                if(!existe){
					// Si el tramite no existe en la nueva definicion, se eliminara
					tramitesNoExistentes.append(String.format("%s ", numeroActual));
                    tramitesARevisar.add(tramiteActual);
                }
            }// for

			// Log de numero de tramites no existen y que tramites son los que si y que no existen
            log.debug(String.format("El número de trámites a eliminar porque no existen en la nueva definición: %d", 
					tramitesARevisar.size()));
			log.debug(String.format("Tramites existentes: %s\n Tramites no existentes: %s", 
					tramitesExistentes.toString(), tramitesNoExistentes.toString()));
			
            for(int i=0;i<tramitesARevisar.size();i++){
                DefinicionTramitesValueObject tramiteARevisar = tramitesARevisar.get(i);
                String codTramite = tramiteARevisar.getCodigoTramite();
                String codMunicipio = tramiteARevisar.getCodMunicipio();
				
				String fechaBajaTramite = abd.convertir(abd.convertir(abd.funcionFecha(
						AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null), 
						AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
						AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY");
				sql = "UPDATE E_TRA SET " + tra_fba	+ "=" + fechaBajaTramite + " WHERE " +
						tra_mun + "=" + codMunicipio + " AND " + tra_pro + "='" + codProcedimiento + "' AND " +
						tra_cod + "=" + codTramite;

				if(log.isDebugEnabled()) log.debug(sql);
				ps = con.prepareStatement(sql);
				int filasAfectadas = ps.executeUpdate();
				ps.close();
				if(log.isDebugEnabled()) {
					log.debug(String.format("%d filas han sido afectadas en el UPDATE de eliminar de E_TRA son", filasAfectadas));
				}
				
				// Se actualiza el tramite de inicio (PRO_TRI) a null donde PRO_TRI = tramiteARevisar
				sql = "UPDATE E_PRO SET " + pro_tri + "=null" + " WHERE " + pro_mun + "=" +
						codMunicipio + " AND " + pro_cod + "='" + codProcedimiento + "' AND " +
						pro_tri + "=" + codTramite;

				if(log.isDebugEnabled()) log.debug(sql);
				ps = con.prepareStatement(sql);
				filasAfectadas = ps.executeUpdate();
				ps.close();
				
				if (log.isDebugEnabled()) {
					log.debug(String.format("%d filas han sido afectadas en el UPDATE de E_PRO son", filasAfectadas));
				}
            
            }//for

            exito = true;

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
          try{
              if(ps!=null) ps.close();
              if(rs!=null) rs.close();
          } catch(SQLException e){
              e.printStackTrace();
          }
        }

        return exito;
   }




   private boolean eliminarCamposSuplementariosProcedimientoViejos(Vector listaNuevosCampos,String codProcedimiento,String codMunicipio,Connection con){

       Statement st = null;
       ResultSet rs  = null;
       boolean exito = false;
       try{

           String sql2="";
           for(int i=0;listaNuevosCampos!=null && i<listaNuevosCampos.size();i++){
                sql2 = sql2 + "'" + listaNuevosCampos.get(i) + "'";
                if(listaNuevosCampos.size() - i>1)
                    sql2 = sql2 + ",";
           }

           // SE BUSCAN LOS CAMPOS SUPLEMENTARIOS DEL PROCEDIMIENTO QUE NO ESTÉN ENTRE LOS NUEVOS Y QUE PUEDAN TENER ASIGNADO ALGÚN VALOR PARA ALGUNO DE
           // LOS EXPEDIENTES, ESTOS CAMPOS SIMPLEMENTE SE DESACTIVAN Y NO SE ELIMINAN

           String sql = "";
           if(listaNuevosCampos!=null && listaNuevosCampos.size()>0){
                    sql = "SELECT PCA_COD FROM e_pca WHERE " +
                            "e_pca.pca_cod NOT IN (" + sql2 + ") " +
                            "AND e_pca.pca_pro = '" + codProcedimiento + "' " +
                            "AND e_pca.pca_mun =" + codMunicipio +
                            "AND ( e_pca.pca_cod IN (SELECT tnu_cod FROM e_tnu) OR e_pca.pca_cod IN (SELECT tfe_cod FROM e_tfe) "+
                            "OR e_pca.pca_cod IN (SELECT ttl_cod FROM e_ttl) OR e_pca.pca_cod IN (SELECT txt_cod FROM e_txt) " +
                            "OR e_pca.pca_cod IN (SELECT TDE_COD FROM E_TDE) OR e_pca.pca_cod IN (SELECT tnuc_cod FROM e_tnuc) " +
                            "OR e_pca.pca_cod IN (SELECT tfec_cod FROM e_tfec)OR e_pca.pca_cod IN (SELECT TDEX_cod FROM E_TDEX))";
           }else{
               sql = "SELECT PCA_COD FROM e_pca WHERE " +
                            "e_pca.pca_pro = '" + codProcedimiento + "' " +
                            "AND e_pca.pca_mun =" + codMunicipio +
                            "AND ( e_pca.pca_cod IN (SELECT tnu_cod FROM e_tnu) OR e_pca.pca_cod IN (SELECT tfe_cod FROM e_tfe) "+
                            "OR e_pca.pca_cod IN (SELECT ttl_cod FROM e_ttl) OR e_pca.pca_cod IN (SELECT txt_cod FROM e_txt) " +
                            "OR e_pca.pca_cod IN (SELECT TDE_COD FROM E_TDE) OR e_pca.pca_cod IN (SELECT tnuc_cod FROM e_tnuc) " +
                            "OR e_pca.pca_cod IN (SELECT tfec_cod FROM e_tfec) OR e_pca.pca_cod IN (SELECT TDEX_cod FROM E_TDEX))";
           }

           log.debug(sql);
           st = con.createStatement();
           rs = st.executeQuery(sql);
           ArrayList<String> codigos = new ArrayList<String>();
           while(rs.next()){
               codigos.add(rs.getString("PCA_COD"));
           }

           st.close();
           rs.close();
           // SE MARCAN COMO DESACTIVADOS LOS CAMPOS RECUPERADOS ANTERIORMENTE
           for(int i=0;i<codigos.size();i++){
                sql = "UPDATE E_PCA SET PCA_ACTIVO='NO' WHERE PCA_COD='" + codigos.get(i) + "' AND PCA_PRO='" + codProcedimiento + "' AND PCA_MUN=" + codMunicipio;
                log.debug(sql);
                st  = con.createStatement();
                int res = st.executeUpdate(sql);
                log.debug("Se ha desactivado " + res + " un campo suplementario de procedimiento");
                
           }// for

           st.close();

           // SE BUSCAN LOS CAMPOS SUPLEMENTARIOS DEL PROCEDIMIENTO QUE NO ESTÉN ENTRE LOS NUEVOS Y QUE NO TIENEN NINGÚN VALOR ASIGNADO PARA NINGUN EXPEDIENTE
           if(listaNuevosCampos!=null && listaNuevosCampos.size()>0){
               sql = "SELECT PCA_COD FROM e_pca WHERE " +
                        "e_pca.pca_cod NOT IN (" + sql2 + ") " +
                        "AND e_pca.pca_pro = '" + codProcedimiento + "' " +
                        "AND e_pca.pca_mun =" + codMunicipio;
           }else{
               sql = "SELECT PCA_COD FROM e_pca WHERE " +
                        "e_pca.pca_pro = '" + codProcedimiento + "' " +
                        "AND e_pca.pca_mun =" + codMunicipio;
           }

           log.debug(sql);
           st = con.createStatement();
           rs = st.executeQuery(sql);
           ArrayList<String> codigosAux = new ArrayList<String>();
           while(rs.next()){               
               String codigo = rs.getString("PCA_COD");
               if(!codigos.contains(codigo))
                   codigosAux.add(codigo);

           }// while

                      
           st.close();
           rs.close();
           
           // SE ELIMINAN LOS CAMPOS SUPLEMENTARIOS RECUPERADOS EN LA ÚLTIMA CONSULTA
           for(int i=0;i<codigosAux.size();i++){
                sql = "DELETE FROM E_PCA WHERE PCA_COD='" + codigosAux.get(i) + "' AND PCA_PRO='" + codProcedimiento + "' AND PCA_MUN=" + codMunicipio;                
                log.debug(sql);
                st  = con.createStatement();
                int res = st.executeUpdate(sql);
                log.debug("Se ha eliminado el campo suplementario de procedimiento " + codigosAux.get(i) + ", resultado: " + res);
           }// for

           exito =true;
       }catch(SQLException e){
           e.printStackTrace();
       }finally{
           try{

               if(st!=null) st.close();
               if(rs!=null) rs.close();

           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return exito;
   }


   /** Elimina un campo suplementario de trámite */
   private boolean eliminarCampoSupTramite( String codCampo,String codTramite,String codProcedimiento,String codMunicipio,Connection con){
       boolean exito = false;
       Statement st = null;
       try{
           String sql = "DELETE FROM E_TCA WHERE TCA_COD='" + codCampo + "' AND TCA_TRA=" + codTramite + " AND TCA_PRO='" + codProcedimiento + "' AND " +
                             "TCA_MUN=" + codMunicipio;
           
           st = con.createStatement();
           int resultado = st.executeUpdate(sql);
           log.debug("eliminarCampoSupTramite: " + resultado);
           if(resultado==1) exito = true;

       }catch(SQLException e){
           e.printStackTrace();;
       }finally{
           try{
               if(st!=null) st.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return exito;
   }

   /** Desactiva un campo suplementario de trámite */
   private boolean desactivarCampoSupTramite( String codCampo,String codTramite,String codProcedimiento,String codMunicipio,Connection con){
       boolean exito = false;
       Statement st = null;
       try{
           String sql = "UPDATE E_TCA SET TCA_ACTIVO='NO' WHERE TCA_COD='" + codCampo + "' AND TCA_PRO='" + codProcedimiento + "' AND TCA_MUN=" + codMunicipio + " AND TCA_TRA=" + codTramite;
           st = con.createStatement();
           int resultado = st.executeUpdate(sql);
           log.debug("desactivarCampoSupTramite: " + resultado);
           if(resultado==1) exito = true;

       }catch(SQLException e){
           e.printStackTrace();;
       }finally{
           try{
               if(st!=null) st.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return exito;
   }


    /** Comrueba si un campo suplementario tiene asignado algún valor para alguna ocurrencia del trámite al que está asociado */
   private boolean tieneCampoSupAsignadoValor(int tipoDato, String codCampo,String codTramite,String codProcedimiento,String codMunicipio,Connection con){
       boolean exito = false;
       Statement st = null;
       ResultSet rs = null;
       try{
           String sql = "";
           if(tipoDato==1) // TIPO NUMERICO
               sql = " SELECT COUNT(*) AS NUM FROM E_TNUT WHERE TNUT_COD='" + codCampo + "' AND TNUT_PRO='" + codProcedimiento + "' AND TNUT_MUN=" +
                        codMunicipio + " AND TNUT_TRA=" + codTramite;
           else
           if(tipoDato==2) // TIPO TEXTO
               sql = " SELECT COUNT(*) AS NUM FROM E_TXTT WHERE TXTT_COD='" + codCampo + "' AND TXTT_PRO='" + codProcedimiento + "' AND TXTT_TRA=" + codTramite +
                       " AND TXTT_MUN=" + codMunicipio;
           else
           if(tipoDato==3) // TIPO FECHA
               sql = " SELECT COUNT(*) AS NUM FROM E_TFET WHERE TFET_COD='" + codCampo + "' AND TFET_PRO='" + codProcedimiento + "' AND TFET_TRA=" + codTramite +
                       " AND TFET_MUN=" + codMunicipio;
           else
           if(tipoDato==4) // TIPO TEXTO LARGO
               sql = " SELECT COUNT(*) AS NUM FROM E_TTLT WHERE TTLT_COD='" + codCampo + "' AND TTLT_PRO='" + codProcedimiento + "' AND TTLT_TRA=" + codTramite +
                       " AND TTLT_MUN=" + codMunicipio;
           else
           if(tipoDato==5) // TIPO FICHERO
               sql = " SELECT COUNT(*) AS NUM FROM E_TFIT WHERE TFIT_COD='" + codCampo + "' AND TFIT_PRO='" + codProcedimiento + "' AND TFIT_TRA=" + codTramite +
                       " AND TFIT_MUN=" + codMunicipio;
           else
           if(tipoDato==6) // TIPO DESPLEGABLE
               sql = " SELECT COUNT(*) AS NUM FROM E_TDET WHERE TDET_COD='" + codCampo + "' AND TDET_PRO='" + codProcedimiento + "' AND TDET_TRA=" + codTramite +
                       " AND TDET_MUN=" + codMunicipio;
           
           if(tipoDato==8) // TIPO NUMERICO CALCULADO
               sql = " SELECT COUNT(*) AS NUM FROM E_TNUCT WHERE TNUCT_COD='" + codCampo + "' AND TNUCT_PRO='" + codProcedimiento + "' AND TNUCT_MUN=" +
                        codMunicipio + " AND TNUCT_TRA=" + codTramite;
           else           
           if(tipoDato==9) // TIPO FECHA CALCULADA
               sql = " SELECT COUNT(*) AS NUM FROM E_TFECT WHERE TFECT_COD='" + codCampo + "' AND TFECT_PRO='" + codProcedimiento + "' AND TFECT_TRA=" + codTramite +
                       " AND TFECT_MUN=" + codMunicipio;
           else
           if(tipoDato==10) // TIPO DESPLEGABLE EXTERNO
               sql = " SELECT COUNT(*) AS NUM FROM E_TDEXT WHERE TDEXT_COD='" + codCampo + "' AND TDEXT_PRO='" + codProcedimiento + "' AND TDEXT_TRA=" + codTramite +
                       " AND TDEXT_MUN=" + codMunicipio;
           log.debug("tieneCampoSupAsignadoValor sql : " + sql);
           st = con.createStatement();
           rs =  st.executeQuery(sql);
           while(rs.next()){
               int num = rs.getInt("NUM");
               log.debug("tieneCampoSupAsignadoValor num: " + num);
               if(num>=1)
                   exito = true;
           }
           
       }catch(SQLException e){
           e.printStackTrace();;
       }finally{
           try{
               if(st!=null) st.close();
               if(rs!=null) rs.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return exito;
   }

   /**
    * Método encargado del tratamiento de los campos suplementarios existentes a nivel de trámite
    * @param defTramVO: Objeto con la definición del trámite
    * @param codProcedimiento: Código del procedimiento
    * @param codMunicipio: Código del municipio
    * @param codTramite: Código del trámite
    * @param con: Conexión a la base de datos
    * @return Boolean
    */
     private boolean tratarCamposSuplementariosTramite(DefinicionTramitesValueObject defTramVO,String codProcedimiento,String codMunicipio,String codTramite,Connection con){

       PreparedStatement ps = null;
       Statement st = null;
       ResultSet rs  = null;
       boolean exito = false;
       try{

               // SE BUSCAN LOS CAMPOS SUPLEMENTARIOS DEL TRÁMITE QUE NO ESTÉN ENTRE LOS NUEVOS Y QUE PUEDAN TENER ASIGNADO ALGÚN VALOR PARA ALGUNO DE
               // LAS OCURRENCIAS DEL MISMO EXPEDIENTES, ESTOS CAMPOS SIMPLEMENTE SE DESACTIVAN Y NO SE ELIMINAN

               Vector listaNuevosCampos = defTramVO.getListaCodCampos();
                Vector listaDescCampos = defTramVO.getListaDescCampos();
                Vector listaCodPlantill = defTramVO.getListaCodPlantill();
                Vector listaCodTipoDato = defTramVO.getListaCodTipoDato();
                Vector listaTamano = defTramVO.getListaTamano();
                Vector listaMascara = defTramVO.getListaMascara();
                Vector listaObligatorio = defTramVO.getListaObligatorio();
                Vector listaOrden = defTramVO.getListaOrden();
                Vector listaRotulo = defTramVO.getListaRotulo();
                Vector listaVisible = defTramVO.getListaVisible();
                Vector listaActivo = defTramVO.getListaActivo();
                Vector listaOculto = defTramVO.getListaOcultos();
                Vector listaBloqueado = defTramVO.getListaBloqueados();
                Vector listaPlazoFecha = defTramVO.getListaPlazoFecha();
                Vector listaCheckPlazoFecha = defTramVO.getListaCheckPlazoFecha();
                Vector listaValidacion = defTramVO.getListaValidacion();
                Vector listaOperacion = defTramVO.getListaOperacion();
                Vector listaAgrupacionCampo = defTramVO.getListaCodAgrupacionCampo();
                Vector listaPosicionesX = defTramVO.getListaPosX();
                Vector listaPosicionesY = defTramVO.getListaPosY();

               String sql = "";
               if(listaNuevosCampos!=null && listaNuevosCampos.size()>0){

                   String sql2="";
                   for(int i=0;listaNuevosCampos!=null && i<listaNuevosCampos.size();i++){
                        sql2 = sql2 + "'" + listaNuevosCampos.get(i) + "'";
                        if(listaNuevosCampos.size() - i>1)
                            sql2 = sql2 + ",";
                   }

                    /** se recuperan los campos suplementarios del trámite que no se encuentran en la nueva definición para comprobar si se eliminan o se dan de baja */

                         sql = "SELECT TCA_COD,TCA_TDA  FROM e_tca WHERE e_tca.tca_cod NOT IN(" + sql2 + ")" +
                                 " AND e_tca.tca_pro = '" + codProcedimiento + "' " +
                                 " AND e_tca.tca_tra = " +codTramite +
                                 " AND e_tca.tca_mun = " + codMunicipio;


                     log.debug("eliminarCamposSuplementariosTramiteViejos sql: " + sql);
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                         String tcaCod = rs.getString("TCA_COD");
                         String tipoDato = rs.getString("TCA_TDA");

                          boolean tiene = this.tieneCampoSupAsignadoValor(Integer.parseInt(tipoDato), tcaCod, codTramite, codProcedimiento, codMunicipio, con);
                          if(tiene) // Se desactiva el campo
                              this.desactivarCampoSupTramite(tcaCod, codTramite, codProcedimiento, codMunicipio, con);
                          else // Se elimina el campo suplementario fisicamente de la base de datos
                              this.eliminarCampoSupTramite(tcaCod, codTramite, codProcedimiento, codMunicipio, con);

                    }


                    

                    /** DE ENTRE LOS CAMPOS SUPLEMENTARIOS DEL TRÁMITE QUE SI ESTÁN EN LA NUEVA DEFINICIÓN, SE DECIDE SI SE ELIMINAN O SE DESACTIVANSe eliminan los campos suplementarios del trámite que si están en la nueva definición porque se procede a su inserción **/
                    sql = "SELECT TCA_COD,TCA_TDA  FROM e_tca WHERE e_tca.tca_cod IN(" + sql2 + ")" +
                             " AND e_tca.tca_pro = '" + codProcedimiento + "' " +
                             " AND e_tca.tca_tra = " +codTramite +
                             " AND e_tca.tca_mun = " + codMunicipio;

                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    
                    Hashtable<String,Boolean> insertar = new Hashtable<String, Boolean>();
                    while(rs.next()){
                         String tcaCod = rs.getString("TCA_COD");
                         String tipoDato = rs.getString("TCA_TDA");

                          boolean tiene = this.tieneCampoSupAsignadoValor(Integer.parseInt(tipoDato), tcaCod, codTramite, codProcedimiento, codMunicipio, con);
                          if(!tiene){
                              // Se puede eliminar el campo e insertarlo de nuevo con la nueva definición
                              this.eliminarCampoSupTramite(tcaCod, codTramite, codProcedimiento, codMunicipio, con);
                              insertar.put(tcaCod,true);
                          }else{ // Se desmarca el campo suplementario pero no se elimina físicamente de la BD
                              this.desactivarCampoSupTramite(tcaCod, codTramite, codProcedimiento, codMunicipio, con);
                              insertar.put(tcaCod,false);
                          }

                    }// while


                    for(int i=0;listaNuevosCampos!=null && i<listaNuevosCampos.size();i++){
                        if(!insertar.containsKey((String)listaNuevosCampos.get(i))){
                            insertar.put((String)listaNuevosCampos.get(i),true);
                        }
                    }

                  boolean seInserta = false;
                    for(int i=0;listaNuevosCampos!=null && i<listaNuevosCampos.size();i++){
                        if(insertar.containsKey((String)listaNuevosCampos.get(i))){
                            seInserta = (Boolean)insertar.get((String)listaNuevosCampos.get(i));
                            if (seInserta) {
                                String plantilla = "", desplegable = "";
                                if (listaCodTipoDato.elementAt(i).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                                    plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                                    desplegable=(String)listaCodPlantill.elementAt(i);
                                } else if (listaCodTipoDato.elementAt(i).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
                                    plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                                    desplegable=(String)listaCodPlantill.elementAt(i);
                                } else {
                                    plantilla=(String)listaCodPlantill.elementAt(i);
                                    desplegable="";
                                }

                                sql = "INSERT INTO E_TCA (" + tca_mun + "," + tca_pro + "," + tca_tra + ","+ tca_cod + "," +
                                        tca_des + "," + tca_plt + "," + tca_tda + "," + tca_tam + "," + tca_mas + "," + tca_obl +
                                "," + tca_nor + "," + tca_rot + "," + tca_vis + "," + tca_activo + "," + tca_desplegable + 
                                        ",TCA_OCULTO,TCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO, PCA_GROUP, TCA_POS_X,TCA_POS_Y) VALUES(" + defTramVO.getCodMunicipio() + ",'" +
                                        defTramVO.getTxtCodigo() + "',"+ defTramVO.getCodigoTramite() + ",'" + listaNuevosCampos.elementAt(i) + "','" +
                                    listaDescCampos.elementAt(i) + "'," + plantilla + ",'" + listaCodTipoDato.elementAt(i) + "'," + listaTamano.elementAt(i);
                                    if(listaMascara.elementAt(i) == null || "".equals(listaMascara.elementAt(i)) || " ".equals(listaMascara.elementAt(i))) {
                                        sql += ",null,";
                                    } else {
                                        sql += ",'" +  listaMascara.elementAt(i) + "',";
                                    }

                                sql += listaObligatorio.elementAt(i) + "," + listaOrden.elementAt(i) + ",'" +
                                listaRotulo.elementAt(i) + "','"+ listaVisible.elementAt(i) + "','" +
                                listaActivo.elementAt(i)+ "','" + desplegable + "','" +
                                listaOculto.elementAt(i)+ "','" + listaBloqueado.elementAt(i) + "',";
                                if((listaPlazoFecha!=null) && (listaCheckPlazoFecha!=null)){                        
                                   if((listaPlazoFecha.size()==listaNuevosCampos.size()) && (listaCheckPlazoFecha.size()==listaNuevosCampos.size())){
                                           if(listaPlazoFecha.elementAt(i) == null || "".equals(listaPlazoFecha.elementAt(i)) || " ".equals(listaPlazoFecha.elementAt(i))) {
                                               sql += "null, null";
                                           } else {
                                               sql += "'" +  listaPlazoFecha.elementAt(i) + "','" + listaCheckPlazoFecha.elementAt(i) +"'";
                                           }
                                      } else{ //El tamanho no es el mismo
                                         sql += "null, null";
                                      }
                               } else {// son nulas listaPlazoFecha o listaCheckPlazoFecha
                                   sql += "null, null";
                               } 

                               if (listaAgrupacionCampo == null){
                                   sql += ",null";
                               }else if(listaAgrupacionCampo != null && listaAgrupacionCampo.size() > 0){
                                   if(listaAgrupacionCampo.elementAt(i) == null || "".equals(listaAgrupacionCampo.elementAt(i)) 
                                       || " ".equals(listaAgrupacionCampo.elementAt(i))){
                                       sql += ",null";
                                   }else{
                                       sql += ",'" + listaAgrupacionCampo.elementAt(i) + "'";
                                   }
                               }else{
                                   sql += ",null";
                               }

                               if (listaAgrupacionCampo == null){
                                   sql += ",null";
                               }else if(listaPosicionesX != null && listaAgrupacionCampo.size() > 0)
                               {    
                                   if(listaPosicionesX.elementAt(i) == null || "".equals(listaPosicionesX.elementAt(i))){
                                       sql +=",null";
                                   }else{
                                       sql +="," + listaPosicionesX.elementAt(i);
                                   }
                               }else{
                                   sql +=",null";
                               }        

                               if (listaAgrupacionCampo == null){
                                   sql += ",null)";                        
                               }else if(listaPosicionesY != null && listaAgrupacionCampo.size() > 0)
                               {
                                   if(listaPosicionesY.elementAt(i) == null || "".equals(listaPosicionesY.elementAt(i))){
                                       sql +=",null)";
                                   }else{
                                       sql +="," + listaPosicionesY.elementAt(i) + ")";
                                   }
                               }else{
                                   sql +=",null)";
                               }
                                        
                                 if(log.isDebugEnabled()) log.debug(sql);

                                ps = con.prepareStatement(sql);
                                ps.executeUpdate();
                                ps.close();
                                
                                sql = " DELETE EXPRESION_CAMPO_NUM_TRAM WHERE COD_ORGANIZACION = " + defTramVO.getCodMunicipio() + 
                                        " AND COD_PROCEDIMIENTO = '" + defTramVO.getTxtCodigo() + "' AND COD_TRAMITE = "+
                                        defTramVO.getCodigoTramite() + " AND COD_CAMPO = '" + listaNuevosCampos.elementAt(i) +"'";
                                ps = con.prepareStatement(sql);
                                ps.executeUpdate();            
                                ps.close(); 

                                sql = " DELETE EXPRESION_CAMPO_CAL_TRAM WHERE COD_ORGANIZACION = " + defTramVO.getCodMunicipio() + 
                                        " AND COD_PROCEDIMIENTO = '" + defTramVO.getTxtCodigo() + "' AND COD_TRAMITE = "+
                                        defTramVO.getCodigoTramite() + " AND COD_CAMPO = '" + listaNuevosCampos.elementAt(i) +"'";
                                ps = con.prepareStatement(sql);
                                ps.executeUpdate();            
                                ps.close(); 
                                    
                                String tip_dat = (String) listaCodTipoDato.elementAt(i);
                               
                               String valida = (String) listaValidacion.elementAt(i);            
                                
                                if (tip_dat.trim().equals("1") && valida != null && !"".equals(valida)) { 
		valida = valida.replace("/&lt;/g","<");  
		valida = valida.replace("/&gt;/g",">"); 
		valida = valida.replace("&lt;","<");  
		valida = valida.replace("&gt;",">"); 

		sql = " INSERT INTO EXPRESION_CAMPO_NUM_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,EXPRESION) VALUES " + 
		  " ("+ defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() +"'," + defTramVO.getCodigoTramite() + ",'" +listaNuevosCampos.elementAt(i) +"','" + valida +                                
		  "')";
		ps = con.prepareStatement(sql);
		ps.executeUpdate();            
		ps.close(); 
                                }                        
                                if (tip_dat.trim().equals("8") || tip_dat.trim().equals("9")) {   
		valida = (String) listaOperacion.elementAt(i);       
		if (valida != null && !"".equals(valida)) {
                                        sql = " INSERT INTO EXPRESION_CAMPO_CAL_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,TIPO_DATO,EXPRESION) VALUES " + 
                                          " ("+ defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() + "'," + defTramVO.getCodigoTramite() + ",'"+ listaNuevosCampos.elementAt(i) +"',"+tip_dat+",'" + valida +                                
                                          "')";                    
                                        ps = con.prepareStatement(sql);
                                        ps.executeUpdate();    
                                        ps.close();
		}
                                }                                                   
                            }
                        }//if
                   }
                    exito =true;
               }else{
                   // Si en la nueva definición no hay nuevos campos suplementarios, entonces se eliminan los existentes

                           sql = "SELECT TCA_COD,TCA_TDA  FROM e_tca WHERE " +
                                 " e_tca.tca_pro = '" + codProcedimiento + "' " +
                                 " AND e_tca.tca_tra = " +codTramite +
                                 " AND e_tca.tca_mun = " + codMunicipio;


                            log.debug("eliminarCamposSuplementariosTramiteViejos sql: " + sql);
                            st = con.createStatement();
                            rs = st.executeQuery(sql);
                            while(rs.next()){
                                 String tcaCod = rs.getString("TCA_COD");
                                 String tipoDato = rs.getString("TCA_TDA");

                                  boolean tiene = this.tieneCampoSupAsignadoValor(Integer.parseInt(tipoDato), tcaCod, codTramite, codProcedimiento, codMunicipio, con);
                                  if(tiene) // Se desactiva el campo
                                      this.desactivarCampoSupTramite(tcaCod, codTramite, codProcedimiento, codMunicipio, con);
                                  else // Se elimina el campo suplementario fisicamente de la base de datos
                                      this.eliminarCampoSupTramite(tcaCod, codTramite, codProcedimiento, codMunicipio, con);

                        }
                            exito = true;
               }

               
       }catch(SQLException e){
           e.printStackTrace();
       }finally{
           try{

               if(st!=null) st.close();
               if(rs!=null) rs.close();

           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return exito;
   }



 /**
    * Comprueba que errores se han producido durante la importación pero que no impiden que este se lleve a cabo. Sólo para cuando se actualiza un procedimiento ya existente
    * @param defProcVO: DefinicionProcedimientosValueObject con la información del procedimiento y de sus trámites
    * @param codIdioma: Código del idioma del usuario
    * @param codAplicacion: Código de la aplicación
    * @param con: Conexión a la base de datos
    * @return ArrayList<ErrorImportacionXPDL> con el listado de errores.
    * Los códigos de error son:  1 = No existe el cargo
    *                                        2 = No existe un campo desplegable para un campo suplementario de procedimiento, pero aún así se da de alta el campo
    *                                        3 = No existe un campo desplegable para un campo suplementario de trámite, pero aún así se da de alta el campo
    */
   public ArrayList<ErrorImportacionXPDL> verificarErroresImportacionProcedimientoExistente(DefinicionProcedimientosValueObject defProcVO,int codIdioma,int codAplicacion,Connection con){
       ArrayList<ErrorImportacionXPDL> errores= new ArrayList<ErrorImportacionXPDL>();
       PreparedStatement ps = null;
       ResultSet rs = null;

       try{
           TraductorAplicacionBean traductor = new TraductorAplicacionBean();
           traductor.setApl_cod(codAplicacion);
           traductor.setIdi_cod(codIdioma);
           
           
           
            /*** COMPROBACIÓN DE LOS CAMPOS SUPLEMENTARIOS A NIVEL DE PROCEDIMIENTO **/
           Vector listaCodCamposProc = defProcVO.getListaCodCampos();
           Vector listaDescripcionCamposProc = defProcVO.getListaDescCampos();     
           Vector listaCodPlantillaProc = defProcVO.getListaCodPlantilla();
           Vector listaCodTipoDato = defProcVO.getListaCodTipoDato();
           Vector listaActivosProc = defProcVO.getListaActivos();
           Vector listaOcultosProc = defProcVO.getListaOcultos();
           Vector listaBloqueadosProc = defProcVO.getListaBloqueados();
           
           
           // comprueba existencia campos del Procedimiento
           for (int j = 0; j < listaCodCamposProc.size(); j++) {
               if ("SI".equalsIgnoreCase((String) listaActivosProc.elementAt(j))) {
                   if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                       if (!this.existeCampoDesplegable(listaCodPlantillaProc.get(j).toString(), con)) {
                           AnadeErrorNoExisteCampoDesplegable(codIdioma, traductor, listaDescripcionCamposProc.get(j).toString(), defProcVO, errores);
                       }
                   } else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
                       if (!this.existeCampoDesplegableExterno(listaCodPlantillaProc.get(j).toString(), con))
                       AnadeErrorNoExisteCampoDesplegableExterno(codIdioma, traductor, listaDescripcionCamposProc.get(j).toString(), defProcVO, errores);
                   }
               }
           } // for
                  

           Vector tramites = defProcVO.getTramites();
           
           String sql = "SELECT COUNT(*) AS NUM FROM A_CAR WHERE CAR_COD_VIS=?";
           ps = con.prepareStatement(sql);
           /*** COMPROBACIÓN DE LOS CARGOS DE CADA TRÁMITE DEL PROCEDIMIENTO **/
           for(int i=0;i<tramites.size();i++){
               DefinicionTramitesValueObject tramite = (DefinicionTramitesValueObject)tramites.get(i);
               int num = 0;
               if(tramite.getCodCargo()!=null && !"".equals(tramite.getCodCargo()) &&
                       tramite.getCodVisibleCargo()!=null && !"".equals(tramite.getCodVisibleCargo()))
               {
                    ps.setString(1,tramite.getCodVisibleCargo());
                    rs = ps.executeQuery();
                    while(rs.next()){
                        num = rs.getInt("NUM");
                    }

                    if(num==0){
                        // No existe el cargo, por tanto se informa del error
                        ErrorImportacionXPDL error = new ErrorImportacionXPDL();
                        error.setCodError(codIdioma);
                        error.setDescripcionError(traductor.getDescripcion(ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_1) + " " + tramite.getCodCargo() + " "
                                + traductor.getDescripcion(ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_2) + " " +  tramite.getCodVisibleCargo() + " "
                                + traductor.getDescripcion(ERR_NO_EXISTE_CARGO_TRAMITE_PARTE_3)  + " " + tramite.getNombreTramite());
                        errores.add(error);
                    }
               }// if


               /** COMPROBACIÓN DE LA EXISTENCIA DE LOS DESPLEGABLES DE UN CAMPO SUPLEMENTARIO A NIVEL DE TRÁMITE **/
               Vector listaCodCamposTram = tramite.getListaCodCampos();
               Vector listaDescripcionCamposTra = tramite.getListaDescCampos();
               Vector listaCodPlantillTram = tramite.getListaCodPlantill();
               Vector listaActivoTram = tramite.getListaActivo();
               Vector listaCodTipoDatoTram = tramite.getListaCodTipoDato();
               for (int j = 0; j < listaCodCamposTram.size(); j++) {         
                   if ("SI".equalsIgnoreCase((String) listaActivoTram.elementAt(j))) {
                       if (listaCodTipoDatoTram.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable")) && !this.existeCampoDesplegable(listaCodPlantillTram.get(j).toString(), con)) {
                           AnadeErrorCampoDesplegableTramite(codIdioma, traductor, listaDescripcionCamposTra.get(j).toString(), tramite, errores);
                       } else if (listaCodTipoDatoTram.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt")) && !this.existeCampoDesplegableExterno(listaCodPlantillTram.get(j).toString(), con)) {
                           this.AnadeErrorCampoDesplegableExternoTramite(codIdioma, traductor, listaDescripcionCamposTra.get(j).toString(), tramite, errores);
                       }
                   }
               }// for

               // Comprobar errores de firma de documento por tramite
               comprobarErroresFirmaDocumento(errores, tramite, traductor, con);
           }// for

           // Comprobar errores de circuitos de firma
           comprobarErrorFirmaCircuito(errores, defProcVO.getListaFlujosFirma(), traductor, con);

       }catch(SQLException e){
           e.printStackTrace();
       }

       return errores;
   }

    private void AnadeErrorCampoDesplegableTramite(int codIdioma, TraductorAplicacionBean traductor, String desplegable, DefinicionTramitesValueObject tramite, ArrayList<ErrorImportacionXPDL> errores) {   
        ErrorImportacionXPDL error = new ErrorImportacionXPDL();
        error.setCodError(codIdioma);
        String descripcionError = traductor.getDescripcion("errNoExisteDesplTramite");
        descripcionError =  descripcionError.replace("[0]", desplegable);
        descripcionError =  descripcionError.replace("[1]", tramite.getNombreTramite());
        error.setDescripcionError(descripcionError);      
        errores.add(error);
    }
    
    private void AnadeErrorCampoDesplegableExternoTramite(int codIdioma, TraductorAplicacionBean traductor, String desplegable, DefinicionTramitesValueObject tramite, ArrayList<ErrorImportacionXPDL> errores) {
        ErrorImportacionXPDL error = new ErrorImportacionXPDL();
        error.setCodError(codIdioma);
        String descripcionError = traductor.getDescripcion("errNoExisteDesplExtTramite");
        descripcionError =  descripcionError.replace("[0]", desplegable);
        descripcionError =  descripcionError.replace("[1]", tramite.getNombreTramite());
        error.setDescripcionError(descripcionError);      
        errores.add(error);
    }

    private void AnadeErrorNoExisteCampoDesplegable(int codIdioma, TraductorAplicacionBean traductor, String desplegable, DefinicionProcedimientosValueObject defProcVO, ArrayList<ErrorImportacionXPDL> errores) {
         ErrorImportacionXPDL error = new ErrorImportacionXPDL();
        error.setCodError(codIdioma);
        String descripcionError = traductor.getDescripcion("errNoExisteDesplProc");
        descripcionError =  descripcionError.replace("[0]", desplegable);
        descripcionError =  descripcionError.replace("[1]", defProcVO.getTxtDescripcion());
        error.setDescripcionError(descripcionError);      
        errores.add(error);
    }
    
    
    private void AnadeErrorNoExisteCampoDesplegableExterno(int codIdioma, TraductorAplicacionBean traductor, String desplegable, DefinicionProcedimientosValueObject defProcVO, ArrayList<ErrorImportacionXPDL> errores) {
         ErrorImportacionXPDL error = new ErrorImportacionXPDL();
        error.setCodError(codIdioma);
        String descripcionError = traductor.getDescripcion("errNoExisteDesplExtProc");
        descripcionError =  descripcionError.replace("[0]", desplegable);
        descripcionError =  descripcionError.replace("[1]", defProcVO.getTxtDescripcion());
        error.setDescripcionError(descripcionError);      
        errores.add(error);
    }
}