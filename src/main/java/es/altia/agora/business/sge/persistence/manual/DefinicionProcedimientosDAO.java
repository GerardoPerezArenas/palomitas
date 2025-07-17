// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.administracion.mantenimiento.PermisoProcedimientosRestringidosVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.*;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.ImportacionFlujoBibliotecaException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.util.commons.StringOperations;
import es.altia.util.jdbc.JdbcOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionProcedimientosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class DefinicionProcedimientosDAO {

    private static DefinicionProcedimientosDAO instance = null;
    protected static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(DefinicionProcedimientosDAO.class.getName());

    protected static String idiomaDefecto;
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

    protected DefinicionProcedimientosDAO() {
        super();
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

        tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
        tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
        tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
        tra_cou = m_ConfigTechnical.getString("SQL.E_TRA.codTramiteUsuario");
        tra_vis = m_ConfigTechnical.getString("SQL.E_TRA.visibleInternet");
        tra_uin = m_ConfigTechnical.getString("SQL.E_TRA.unidadInicio");
        tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");
        tra_plz = m_ConfigTechnical.getString("SQL.E_TRA.plazo");
        tra_und = m_ConfigTechnical.getString("SQL.E_TRA.unidadesPlazo");
        tra_are = m_ConfigTechnical.getString("SQL.E_TRA.codArea");
        tra_ocu = m_ConfigTechnical.getString("SQL.E_TRA.ocurrencias");
        tra_cls = m_ConfigTechnical.getString("SQL.E_TRA.clasificacion");
        tra_fba = m_ConfigTechnical.getString("SQL.E_TRA.fechaBaja");

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

        // Tabla E_TCA (Campos suplementarios de tramite)
        tca_mun = m_ConfigTechnical.getString("SQL.E_TCA.codMunicipio");
        tca_pro = m_ConfigTechnical.getString("SQL.E_TCA.codProcedimiento");
        tca_cod = m_ConfigTechnical.getString("SQL.E_TCA.codCampo");
        tca_des = m_ConfigTechnical.getString("SQL.E_TCA.descripcion");
        tca_tda = m_ConfigTechnical.getString("SQL.E_TCA.codTipoDato");
        tca_activo = m_ConfigTechnical.getString("SQL.E_TCA.activo");
        tca_tra = m_ConfigTechnical.getString("SQL.E_TCA.codTramite");
        tca_plt = m_ConfigTechnical.getString("SQL.E_TCA.codPlantilla");
        tca_desplegable = m_ConfigTechnical.getString("SQL.E_TCA.desplegable");

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
        idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
    }

    public static DefinicionProcedimientosDAO getInstance() {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized(DefinicionProcedimientosDAO.class) {
                if (instance == null) {
                    instance = new DefinicionProcedimientosDAO();
                }            
        }
        return instance;
    }

    public Vector getListaTiposDepartamentos(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposDepartamentos");

        Connection con = null;
    Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + dep_cod + ","+ dep_nom + " FROM  A_DEP";
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTiposDepartamentos: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(dep_cod),rs.getString(dep_nom),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaTiposDepartamentos: Lista tipos departamentos cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTiposDepartamentos: Tamaño lista:" + list.size());
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaTiposDepartamentos"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaTiposDepartamentos");
        return list;
    }

    public Vector getListaArea(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaArea");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            from = are_cod + "," + aml_valor;
            String[] join = new String[5];
            join[0] = GlobalNames.ESQUEMA_GENERICO + "A_ARE";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_aml";
            join[3] = GlobalNames.ESQUEMA_GENERICO + "a_are." + are_cod + "=" +GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_cod +
                    " AND " + GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_cmp + "='NOM'";
            join[4] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaArea: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(are_cod),rs.getString(aml_valor),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaArea: Lista area cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaArea: Tamaño lista:" + list.size());
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaArea"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaArea");
        return list;
    }

    public Vector getListaUnidadInicio(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaUnidadInicio");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql="select "+uor_nom+","+uor_cod+","+uor_cod_vis+" from "+GlobalNames.ESQUEMA_GENERICO+"A_UOR A_UOR";
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaUnidadInicio: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(utr_cod),rs.getString(utml_valor),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaUnidadInicio: Lista unidad inicio cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaUnidadInicio"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaUnidadInicio");
        return list;
    }

    public Vector getTablaUnidadInicio(DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getTablaUnidadInicio");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector list = new Vector();
        String codMunicipio = defProcVO.getCodMunicipio();
        String codProcedimiento = defProcVO.getTxtCodigo();

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            from = uor_cod + "," + uor_nom + "," + uor_cod_vis;
            where = pui_mun + "=" + codMunicipio + " AND " + pui_pro + "='" + codProcedimiento + "'";
            String[] join = new String[5];
            join[0] = "E_PUI";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_uor";
            join[3] = "e_pui." + pui_cod + "=" + GlobalNames.ESQUEMA_GENERICO + "a_uor." + uor_cod ;
            join[4] = "false";
            sql = oad.join(from,where,join);

            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getTablaUnidadInicio: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codUnidadInicio = rs.getString(uor_cod);
                dPVO.setCodUnidadInicio(codUnidadInicio);
                String codVisibleUnidadInicio = rs.getString(uor_cod_vis);
                dPVO.setCodVisibleUnidadInicio(codVisibleUnidadInicio);
                String descUnidadInicio = rs.getString(uor_nom);
                dPVO.setDescUnidadInicio(descUnidadInicio);
                list.addElement(dPVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getTablaUnidadInicio: Lista unidad inicio cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getTablaUnidadInicio"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getTablaUnidadInicio");
        return list;
    }

    public Vector getListaTiposProcedimientos(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposProcedimientos");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String from = "";
        String where = "";
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            from = tpr_cod + "," + tpml_valor;
            String[] join = new String[5];
            join[0] = GlobalNames.ESQUEMA_GENERICO + "A_TPR";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_tpml";
            join[3] = GlobalNames.ESQUEMA_GENERICO + "a_tpr." + tpr_cod + "=" + GlobalNames.ESQUEMA_GENERICO + "a_tpml." + tpml_cod +
                    " AND " + GlobalNames.ESQUEMA_GENERICO + "a_tpml." + tpml_cmp + "='NOM' AND "+ GlobalNames.ESQUEMA_GENERICO + "a_tpml." + tpml_leng + "="+idiomaDefecto;
            join[4] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTiposProcedimientos: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(tpr_cod),rs.getString(tpml_valor),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaTiposProcedimientos: Lista tipos procedimientos cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaTiposProcedimientos"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaTiposProcedimientos");
        return list;
    }

    public Vector getListaTiposNaturaleza(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposNaturaleza");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + npr_cod + ","+ npr_des + " FROM  E_NPR";
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTiposNaturaleza: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(npr_cod),rs.getString(npr_des),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaTiposNaturaleza: Lista tipos procedimientos cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaTiposNaturaleza"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaTiposNaturaleza");
        return list;
    }

    public Vector getListaCamposDesplegables(String[] params)
        throws AnotacionRegistroException, TechnicalException{

      //Queremos estar informados de cuando este metod es ejecutado
      m_Log.debug("getListaCamposDesplegables");

      Connection con = null;
      Statement st = null;
      ResultSet rs = null;
      String sql = "";
      Vector list = new Vector();
      ElementoListaValueObject elemListVO;
      int orden = 0;

      try{
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        con = oad.getConnection();
        st = con.createStatement();
        //sql = "SELECT " + des_cod + ","+ des_nom + " FROM  E_DES";
        sql="SELECT combo.des_cod, combo.des_nom, (select max ("+oad.tamanoTexto("valores.des_nom")+") from e_des_val valores where valores.des_cod=combo.des_cod) as tamano  FROM  E_DES combo order by 1";
       
        if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaCamposDesplegables: Sentencia SQL:" + sql);
        rs = st.executeQuery(sql);
        while(rs.next()){
          elemListVO = new ElementoListaValueObject(rs.getString(des_cod),rs.getString(des_nom),orden++,rs.getInt("tamano"));
          list.addElement(elemListVO);
        }
        m_Log.debug("DefinicionProcedimientosDAO, getListaCamposDesplegables: Lista campos desplegables cargada");
        rs.close();
        st.close();
        }catch (Exception e) {
          list = null;
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
          throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaCamposDesplegables"), e);
        }finally {
          try{
            con.close();
          } catch(SQLException sqle) {
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
          }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaCamposDesplegables");
        return list;
    }

    public Vector getListaPlantillas(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaPlantillas");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + plt_cod + ","+ plt_des + " FROM  E_PLT";
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaPlantillas: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(plt_cod),rs.getString(plt_des),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaPlantillas: Lista tipos plantillas cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaPlantillas"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaPlantillas");
        return list;
    }

    public Vector getListaMascaras(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaMascaras");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + mas_cod + ","+ mas_des + " FROM  E_MAS";
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaMascaras: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(mas_cod),rs.getString(mas_des),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaMascaras: Lista tipos plantillas cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaMascaras"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaMascaras");
        return list;
    }

    public Vector getListaRelacionTipoDatoPlantillas(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaRelacionTipoDatoPlantillas");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + rtdp_tda + ","+ rtdp_plt + " FROM  E_RTDP";
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaRelacionTipoDatoPlantillas: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(rtdp_tda),rs.getString(rtdp_plt),orden++);
                list.addElement(elemListVO);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaRelacionTipoDatoPlantillas: Tamaño lista:" + list.size());
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaPlantillas"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaPlantillas");
        return list;
    }

    public Vector getListaRelacionTipoDatoMascaras(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaRelacionTipoDatoMascaras");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + rtdm_tda + ","+ rtdm_mas + " FROM  E_RTDM";
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaRelacionTipoDatoMascaras: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(rtdm_tda),rs.getString(rtdm_mas),orden++);
                list.addElement(elemListVO);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaRelacionTipoDatoMascaras: Tamaño lista:" + list.size());
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaRelacionTipoDatoMascaras"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaRelacionTipoDatoMascaras");
        return list;
    }

    public Vector getListaTipoDato(String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTipoDato");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + tda_cod + ","+ tda_des + " FROM  E_TDA";
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTipoDato: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(tda_cod),rs.getString(tda_des),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaTipoDato: Lista tipos plantillas cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaTipoDato"), e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaTipoDato");
        return list;
    }

    /**
     * Devuelve la lista de documentos para el procedimiento indicado.
     * 
     * @param proc GeneralValueObject con el municipio y codigo del procedimiento en 'codMunicipio' y 'codProcedimiento'. 
     * @return Una lista de los nombres de documentos del procedimiento en forma de ElementoListaValueObject, con
     *         el nombre del documento en el atributo 'descripcion'.
     */
    public Vector<ElementoListaValueObject> getListaDocumentos(GeneralValueObject proc, String[] params) {
        
        m_Log.info("DefinicionProcedimientosDAO->getListaDocumentos");
        Vector<ElementoListaValueObject> docs = new Vector<ElementoListaValueObject>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();            
            
            sql = "SELECT " + dpml_valor + " FROM E_DPML" + 
                 " WHERE " + dpml_mun + "=" + proc.getAtributo("codMunicipio") +
                 " AND " + dpml_pro + "='" + proc.getAtributo("codProcedimiento") + "'" +
                 " AND " + dpml_cmp + "='NOM'";
            m_Log.debug(sql);
            
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            
            ElementoListaValueObject doc = null;
            while(rs.next()) {
                doc = new ElementoListaValueObject();
                doc.setDescripcion(rs.getString(dpml_valor));
                docs.add(doc);
            }
                        
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
        }
        finally {
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        
        return docs;      
    }

    /**
     * Devuelve la lista de documentos para el procedimiento indicado.
     *
     * @param proc GeneralValueObject con el municipio y codigo del procedimiento en 'codMunicipio' y 'codProcedimiento'.
     * @return Una lista de los nombres de documentos del procedimiento en forma de ElementoListaValueObject, con
     *         el nombre del documento en el atributo 'descripcion'.
     */
    public ArrayList<DocumentoExpedienteVO> getListaDocumentos(DefinicionTramitesValueObject proc, String[] params) {

        m_Log.info("DefinicionProcedimientosDAO->getListaDocumentos");
        ArrayList<DocumentoExpedienteVO> docs = new ArrayList<DocumentoExpedienteVO>();
        Connection con = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
        String sql = "SELECT DOP_COD ,  dpml1.DPML_VALOR AS nombreDocumento,    "
            + "COUNT(ID_FIRMA) AS FIRMAS FROM   e_dpml dpml1,  e_dpml dpml2,  E_DOP left JOIN E_DEF_FIRMA "
            + "ON (firma_mun=dop_mun and firma_proc=dop_pro and firma_cod_doc=dop_cod) WHERE DOP_MUN = ? " 
            + "AND DOP_PRO = ? AND e_dop.DOP_MUN  =dpml1.DPML_MUN AND e_dop.DOP_PRO  =dpml1.DPML_PRO "
            + "AND e_dop.DOP_COD  =dpml1.DPML_DOP AND dpml1.DPML_CMP ='NOM' AND dpml1.DPML_LENG= ? AND "
            + "e_dop.DOP_MUN  =dpml2.DPML_MUN AND e_dop.DOP_PRO  =dpml2.DPML_PRO AND "
            + "e_dop.DOP_COD  =dpml2.DPML_DOP AND dpml2.DPML_CMP ='CON' AND dpml2.DPML_LENG= ? "
            + " GROUP BY DOP_COD, dpml1.DPML_VALOR, dpml2.DPML_VALOR ";
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();


            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(proc.getCodMunicipio()));
            ps.setString(i++, proc.getTxtCodigo());
            ps.setString(i++, idiomaDefecto);
            ps.setString(i++, idiomaDefecto);


            rs = ps.executeQuery();

            DocumentoExpedienteVO doc = null;
            while(rs.next()) {
                if (rs.getInt("FIRMAS")>0) {
                    doc = new DocumentoExpedienteVO();
                    doc.setCodigo(rs.getString("DOP_COD"));
                    doc.setNombre(rs.getString("nombreDocumento"));
                    docs.add(doc);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
        finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }

        return docs;
    }
    public int insert(Connection conexion,DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        String sql1 = "";
        String sqlDoc = "";
        String yaExiste = "no";
        int res = 0;
        int res1 = 0;
        int res3 = 0;
        int resTotal = 0;
        int resEnlaces = 0;
        int resRoles = 0;
    
        Vector codDepartV = new Vector();
        Vector codV = new Vector();
        ResultSet rs = null;
        Statement st = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("Entra en el insert del DAO");
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            st = conexion.createStatement();

            sql1 = "SELECT " + pro_mun + "," + pro_cod + " FROM E_PRO";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql1);

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


                /*** ORIGINAL
                sql = "INSERT INTO E_PRO (" + pro_mun + "," + pro_cod + "," + pro_flh + "," + pro_fld +
                        "," + pro_are + "," + pro_plz + "," + pro_und + "," + pro_sil + "," +
                        pro_tip + "," + pro_ini + "," + pro_est + "," + pro_din + "," + pro_tin + "," + pro_loc + "," +
                        pro_tri + ", " + pro_des + ",PRO_PORCENTAJE,PRO_RESTRINGIDO"  + ") VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                        defProcVO.getTxtCodigo();
                ***/

                /*** PRUEBA ****/
                sql = "INSERT INTO E_PRO (" + pro_mun + "," + pro_cod + "," + pro_flh + "," + pro_fld +
                        "," + pro_are + "," + pro_plz + "," + pro_und + "," + pro_sil + "," +
                        pro_tip + "," + pro_ini + "," + pro_est + "," + pro_din + "," + pro_tin + "," + pro_loc + "," +
                        pro_tri + ", " + pro_des + ",PRO_PORCENTAJE,PRO_RESTRINGIDO,PRO_LIBRERIA,PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS,PRO_INTOBL,PRO_SOLOWS,PLUGIN_RELAC_HISTORICO"  + ") VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                        defProcVO.getTxtCodigo();

                /*** PRUEBA ****/

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
                sql += defProcVO.getTipoSilencio() + "," + defProcVO.getCodTipoProcedimiento()+ "," +
                        defProcVO.getCodTipoInicio() + "," + defProcVO.getCodEstado() + "," +
                        defProcVO.getDisponible() + "," + defProcVO.getTramitacionInternet() + "," +
                        defProcVO.getLocalizacion() + ", ";
                if(defProcVO.getTramiteInicio() == null || defProcVO.getTramiteInicio().equals(""))
                        sql += "null, ";
                else
              sql += defProcVO.getTramiteInicio() + ", ";
          // Campo de Bescripcion Breve.
          if (defProcVO.getDescripcionBreve() == null || defProcVO.getDescripcionBreve().equals("")) {
              sql += "null)";
          } else {
                    sql += "'" + defProcVO.getDescripcionBreve().trim() + "',";
          }

          if(defProcVO.getPorcentaje()!=null && defProcVO.getPorcentaje().length()>0)
                sql += "'" + defProcVO.getPorcentaje() + "'";
          else sql += "null";
          
          
          /** SE INDICA SI EL PROCEDIMIENTO ESTÁ RESTRINGIDO **/

          /*** ORIGINAL **
          if(defProcVO.getRestringido()!=null && ("0".equals(defProcVO.getRestringido()) || "1".equals(defProcVO.getRestringido()))){
                sql += "," + defProcVO.getRestringido() + ")";
          }else{
                sql+=",0)";
          }
          ***/

          if(defProcVO.getRestringido()!=null && ("0".equals(defProcVO.getRestringido()) || "1".equals(defProcVO.getRestringido()))){
                sql += "," + defProcVO.getRestringido() ;
          }else{
                sql+=",0";
          }
          
          /* SE AÑADE A LA CONSULTA EL VALOR QUE INDICA SI EL PROCEDIMIENTO ES LIBRERIA DE FLUJO O NO */

          if(defProcVO.getBiblioteca()!=null && ("0".equals(defProcVO.getBiblioteca()) || "1".equals(defProcVO.getBiblioteca()))){
                sql += "," + defProcVO.getBiblioteca() ;
          }else{
                sql+=",0";
          }


           /***** PLUGIN DE FINALIZACIÓN NO CONVENCIONAL DE EXPEDIENTE ****/

           // PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS
           if(StringOperations.stringNoNuloNoVacio(defProcVO.getCodServicioFinalizacion()) && StringOperations.stringNoNuloNoVacio(defProcVO.getImplClassServicioFinalizacion())){
                sql += ",'" + defProcVO.getCodServicioFinalizacion() + "','" + defProcVO.getImplClassServicioFinalizacion() +"'";
           }
           else
               sql += ",NULL,NULL";
           
           sql += "," + defProcVO.getInteresadoOblig();  // +  ")";
           
           //Mai: engado columna de soloWS
           m_Log.debug("DefinicionProcedimientoDAO.INSERT. El valor que le paso a soloWS es: "+ defProcVO.getSoloWS());
           sql += "," + defProcVO.getSoloWS();
                  
		   if(defProcVO.getClaseBuzonEntradaHistorico() == null || defProcVO.getClaseBuzonEntradaHistorico().equals(""))                    
                sql += ",null)";
           else
                sql += ",'" + defProcVO.getClaseBuzonEntradaHistorico() + "')";
               
               
           /***** PLUGIN DE FINALIZACIÓN NO CONVENCIONAL DE EXPEDIENTE ****/


                if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO.sql.INSERT: " + sql);

                res = st.executeUpdate(sql);
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_PRO son :::::::::::::: : " + res);

                sql = "INSERT INTO E_PML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                    defProcVO.getTxtCodigo() + "','NOM','" + idiomaDefecto +"','" + defProcVO.getTxtDescripcion().trim() + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                res1 = st.executeUpdate(sql);
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_PML son :::::::::::::: : " + res1);

                Vector listaCodUnidadInicio = defProcVO.getListaCodUnidadInicio();
                if(listaCodUnidadInicio != null) {
                    for(int i=0;i<listaCodUnidadInicio.size();i++) {
                        sql = "INSERT INTO E_PUI (" + pui_mun + "," + pui_pro + "," + pui_cod + ") VALUES (" +
                                defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() + "'," +
                                listaCodUnidadInicio.elementAt(i) + ")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                        res3 += st.executeUpdate(sql);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_PUI son res3:::::::::::::: : " + res3);
                }

                // PESTAÑA DE DOCUMENTOS

                Vector listaNombresDoc = defProcVO.getListaNombresDoc();
                Vector listaCondicionDoc = defProcVO.getListaCondicionDoc();
                Vector listaCodigosDoc = defProcVO.getListaCodigosDoc();
                for(int j=0;j<listaNombresDoc.size();j++) {

                    sqlDoc = "INSERT INTO E_DOP (" + dop_mun + "," + dop_pro + "," + dop_cod +
                            ") VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    st.executeUpdate(sqlDoc);

                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'NOM','"+ idiomaDefecto +"','" +
                            listaNombresDoc.elementAt(j) + "')";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    st.executeUpdate(sqlDoc);

                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'CON','"+idiomaDefecto+"','" +
                            listaCondicionDoc.elementAt(j) + "')";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    st.executeUpdate(sqlDoc);
                }

                // PESTAÑA DE CAMPOS

                Vector listaCodCampos = defProcVO.getListaCodCampos();
                Vector listaDescCampos = defProcVO.getListaDescCampos();
                Vector listaCodPlantilla = defProcVO.getListaCodPlantilla();
                Vector listaCodTipoDato = defProcVO.getListaCodTipoDato();
                Vector listaTamano = defProcVO.getListaTamano();
                Vector listaMascara = defProcVO.getListaMascara();
                Vector listaObligatorio = defProcVO.getListaObligatorio();
                Vector listaOrden = defProcVO.getListaOrden();
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
                         
				
                for(int j=0;j<listaCodCampos.size();j++) {
                    if ("SI".equalsIgnoreCase((String)listaActivos.elementAt(j))) {
                        if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                            desplegable = (String)listaCodPlantilla.elementAt(j);
                        }else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))){ //desplegables externos
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                            desplegable = (String)listaCodPlantilla.elementAt(j);
                        }else{
                            plantilla = (String)listaCodPlantilla.elementAt(j);
                            desplegable = "";
                        }
                        sql = "INSERT INTO E_PCA (" + pca_mun + "," + pca_pro + "," + pca_cod + "," + pca_des + "," +
                                pca_plt + "," + pca_tda + "," + pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," +
                pca_rot + "," + pca_activo + "," + pca_desplegable +",PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO, PERIODO_PLAZO,PCA_GROUP,"
                + "PCA_POS_X, PCA_POS_Y ) "    
                + " VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                defProcVO.getTxtCodigo() + "','" + listaCodCampos.elementAt(j) + "','" + listaDescCampos.elementAt(j) +
                "'," + plantilla + ",'" + listaCodTipoDato.elementAt(j) + "'," + listaTamano.elementAt(j);
                        if(listaMascara.elementAt(j) == null || "".equals(listaMascara.elementAt(j)) || " ".equals(listaMascara.elementAt(j))) {
                            sql += ",null,";
                        } else {
                            sql += ",'" +  listaMascara.elementAt(j) + "',";
                        }
                        sql += listaObligatorio.elementAt(j) + "," + j + ",'" + listaRotulo.elementAt(j) + "','" +
                               ((String)listaActivos.elementAt(j)).toUpperCase() + "','" + desplegable +"','" + listaOcultos.elementAt(j)+ "','" + listaBloqueados.elementAt(j)+ "',";
                        
                        //insertamos el campo PLAZO_AVISO Y pERIODO_PLAZO
                       
                        
                    if((listaPlazoFecha!=null) && (listaCheckPlazoFecha!=null)){
                        
                      if((listaPlazoFecha.size()==listaCodCampos.size()) &&
                              (listaCheckPlazoFecha.size()==listaCodCampos.size())){
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
                        if(listaAgrupacionCampo.elementAt(j) == null || "".equals(listaAgrupacionCampo.elementAt(j)) 
                            || " ".equals(listaAgrupacionCampo.elementAt(j))){
                            sql += ",null";
                        }else{
                            sql += ",'" + listaAgrupacionCampo.elementAt(j) + "'";
                        }
                    }else{
                        sql += ",null";
                    }
                   
                    if(listaPosicionesX != null && listaAgrupacionCampo.size() > 0){  
                        if(listaPosicionesX.elementAt(j) == null || "".equals(listaPosicionesX.elementAt(j))
                                || "undefined".equals(listaPosicionesX.elementAt(j))){
                            sql +=",null";
                        }else{
                            sql +="," + listaPosicionesX.elementAt(j);
                        }/*if(listaPosicionesX.elementAt(j) == null || "".equals(listaPosicionesX.elementAt(j))
                                || "undefined".equals(listaPosicionesX.elementAt(j)))*/
                    }else{
                        sql +=",null";
                    }//if(listaPosicionesX != null && listaAgrupacionCampo.size() > 0)        
                    
                    if(listaPosicionesY != null && listaAgrupacionCampo.size() > 0){
                        if(listaPosicionesY.elementAt(j) == null || "".equals(listaPosicionesY.elementAt(j))
                                || "undefined".equals(listaPosicionesY.elementAt(j))){
                            sql +=",null)";
                        }else{
                            sql +="," + listaPosicionesY.elementAt(j) + ")";
                        }/*if(listaPosicionesY.elementAt(j) == null || "".equals(listaPosicionesY.elementAt(j))
                                || "undefined".equals(listaPosicionesY.elementAt(j)))*/
                    }else{
                        sql +=",null)";
                    }//if(listaPosicionesY != null && listaAgrupacionCampo.size() > 0)
                    
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
                        m_Log.debug("grabacion de nueva expresion de validacion = " +listaValidacion.elementAt(j));                            
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
                            m_Log.debug("grabacion de nueva expresion de calculo = " +listaOperacion.elementAt(j));
                        }
                    }
                }
            }
                
                //Agrupaciones de campos suplementarios
                if(m_Log.isDebugEnabled()) m_Log.debug("Actualizamos las agrupaciones de campos suplementarios : BEGIN");
                Vector listaCodAgrupaciones = defProcVO.getListaCodAgrupaciones();
                Vector listaDescAgrupaciones = defProcVO.getListaDescAgrupaciones();
                Vector listaOrdenAgrupaciones = defProcVO.getListaOrdenAgrupaciones();
                Vector listaAgrupacionesActivas = defProcVO.getListaAgrupacionesActivas();
                
                for(int w=0; w<listaCodAgrupaciones.size(); w++){
                sql = "Insert into E_PCA_GROUP (PCA_ID_GROUP, PCA_DESC_GROUP, PCA_ORDER_GROUP, PCA_PRO, PCA_ACTIVE) values ('" 
                        + listaCodAgrupaciones.elementAt(w) + "',"
                        + "'" + listaDescAgrupaciones.elementAt(w) + "',"
                        + listaOrdenAgrupaciones.elementAt(w) + ","
                        + "'" + defProcVO.getTxtCodigo() + "',"
                        + "'" + listaAgrupacionesActivas.elementAt(w) + "'"
                        + ")";
                
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    st.executeUpdate(sql);
                }//for(int w=0; w<listaCodAgrupaciones; w++)
                if(m_Log.isDebugEnabled()) m_Log.debug("Actualizamos las agrupaciones de campos suplementarios : END");
                
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
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    resEnlaces = st.executeUpdate(sql);
                }
                if(listaCodEnlaces.size() == 0 ) {
                    resEnlaces = 1;
                }

                //FIRMAS DE DOCUMENTOS DE PROCEDIMIENTO

                ArrayList<FirmasDocumentoProcedimientoVO> listaFirmasVO = new ArrayList<FirmasDocumentoProcedimientoVO>();
                listaFirmasVO=defProcVO.getFirmasDocumentosProcedimiento();
                ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentoProcedimiento = defProcVO.getFirmasDocumentosProcedimiento();
                String firmaCargo = null;
                for(int j=0;j<firmasDocumentoProcedimiento.size();j++) {
                    FirmasDocumentoProcedimientoVO firmaVO=firmasDocumentoProcedimiento.get(j);
                    
                    if(firmaVO.getCargo()!=null && !firmaVO.getCargo().equals("")){
                        firmaCargo = firmaVO.getCargo();
                    } else {
                        firmaCargo = null;
                    } 
                    String sqlFir = "INSERT INTO E_DEF_FIRMA (ID_FIRMA,FIRMA_USUARIO,FIRMA_ORDEN,FIRMA_COD_DOC,FIRMA_MUN,FIRMA_PROC,FIRMA_UOR,FIRMA_CARGO,FIRMA_TRA_SUB) VALUES((SELECT "
                        + oad.funcionSistema(oad.FUNCIONSISTEMA_NVL, new String[]{"MAX(ID_FIRMA+1)", "1"}) +
                        " FROM E_DEF_FIRMA)," + firmaVO.getUsuario() +","+firmaVO.getOrden()+","+firmaVO.getCodDocumento()+ "," +defProcVO.getCodMunicipio()+ ",'" +
                    defProcVO.getTxtCodigo() + "'," + firmaVO.getUor()+","+firmaCargo+","+firmaVO.getTramitar()+")";
                    m_Log.debug("sqlFir: " + sqlFir);
                    st.executeUpdate(sqlFir);
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
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          resRoles = st.executeUpdate(sql);
                }
                if(listaCodRoles.size() == 0 ) {
                    resRoles = 1;
                }

                if(res == 0 || resEnlaces == 0 || resRoles == 0) {
                    resTotal = 0;
            if(m_Log.isErrorEnabled()){
                m_Log.debug("res VALE: " + res + " resEnlaces VALE: " + resEnlaces  +
                                " resRoles VALE: " + resRoles);
                    }
                } else {
                    resTotal = 1;
                }

            } else {
                resTotal = -1;
            }
      st.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        }
        return resTotal;
    }


    public String getIDELista(String codigo, String nombreTabla, String atribCodigo, String atribIDE,String[] params)
            throws AnotacionRegistroException, TechnicalException {

        String ide=null;
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getIDELista");

        String sql = "";

        Connection con = null;
   Statement st = null;
        ResultSet rs = null;

        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
     st = con.createStatement();
            // Creamos la select con los parametros adecuados (campos y tablas)
            sql = "SELECT "+ atribIDE +" FROM "+ nombreTabla;
            sql = sql + " WHERE "+atribCodigo +"='"+codigo +"'";

            if(m_Log.isDebugEnabled()) m_Log.debug("getIDELista: Sentencia SQL:" + sql);

            //Ahora el JDBCWrapper tiene suficiente informacion para construir la sentencia sql.
            //De este modo, la podemos ejecutar.

     rs = st.executeQuery(sql);

            if ( rs.next() ) // En teoría sólo obtendremos uno.
            {
                ide = rs.getString(atribIDE);
                m_Log.debug("getIDELista: Cargado el registro");
            }
            rs.close();
            st.close();
        } catch (Exception e)
        {
            ide=null;
            e.printStackTrace();
            //Si la lectura tiene problemas tenemos que lanzar y loggear la excepcion
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getIDELista"), e);
        }
        finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getIDELista");
        return ide;
    }

    public String obtenerJndi(DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("obtenerJndi");
        String jndi = "";
        SortedMap<ArrayList<String>,ParametrosBDVO> listaParametrosBD = (SortedMap<ArrayList<String>,ParametrosBDVO>)CacheDatosFactoria.getImplParametrosBD().getDatos();
        
        if (listaParametrosBD!=null) {
            for(Map.Entry<ArrayList<String>,ParametrosBDVO> entry : listaParametrosBD.entrySet()) {
                ParametrosBDVO parametrosBD = entry.getValue();
                if (parametrosBD.getCodOrganizacion() == Integer.parseInt(defProcVO.getCodMunicipio()) && 
                        parametrosBD.getCodAplicacion() == Integer.parseInt(defProcVO.getCodAplicacion())){
                    jndi = parametrosBD.getJndi();
                    break;
                }
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("obtenerJndi");
        return jndi;
    }

    public DefinicionProcedimientosValueObject buscar(DefinicionProcedimientosValueObject defProcVO, String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("buscar");

        Connection con = null;
        PreparedStatement st;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        String codMunicipio = defProcVO.getCodMunicipio();
        String codigo = defProcVO.getTxtCodigo();
        String fechaLH = "";
        String fechaLD = "";
        String fechaA = "";
        int cont = 0;
        int cont1 = 0;
        int cont2 = 0;
        int cont3 = 0;
        Vector tramites = new Vector();
        Vector listasDoc = new Vector();
        Vector listasCampos = new Vector();
        Vector listaUnidadesInicio = new Vector();
        Vector listaEnlaces = new Vector();
        Vector listaRoles = new Vector();
        Vector listasAgrupaciones = new Vector();
        
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            // PESTAÑA DATOS

            from = " PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS, PRO_RESTRINGIDO,PRO_PORCENTAJE,"+pro_mun + "," + pro_cod + ",pml1." + pml_valor + " AS nombreProc,"
                    + oad.convertir(pro_fld, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO , "DD/MM/YYYY") +
                    " AS " + pro_fld + ","+ oad.convertir(pro_flh, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO , "DD/MM/YYYY") +
                    " AS " + pro_flh + "," + pro_are + "," + aml_valor + "," + pro_plz + "," + pro_und +
                    "," + pro_sil + "," + pro_tip + "," + tpr_cod + "," + tpml_valor + "," + pro_ini +
                    "," + pro_est + "," + pro_din + "," + pro_tin + "," + tpr_cod + "," + pro_loc+ "," + pro_tri +
                    ", " + pro_des + ", PRO_INTOBL, PRO_SOLOWS,PLUGIN_RELAC_HISTORICO, E_PRO.PRO_LIBRERIA, E_PRO.PRO_EXPNUMANOT ";
            where = pro_mun + "=" + codMunicipio + " AND " + pro_cod + "='" + codigo + "' AND " +
            		"pml1." + pml_cmp + " ='NOM' " + " AND " +
            		"pml1." + pml_leng + " ='"+idiomaDefecto+"' " + " AND " +
            		GlobalNames.ESQUEMA_GENERICO + "a_tpml." + tpml_cmp + "='NOM' AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_tpml." + tpml_leng + " ='"+idiomaDefecto+"' AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_cmp + "='NOM' AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_leng + " ='"+idiomaDefecto+"' ";
            String[] join = new String[14];
            join[0] = "E_PRO";
            join[1] = "INNER";
            join[2] = "e_pml pml1";
            join[3] = "e_pro." + pro_mun + "=pml1." + pml_mun + " AND " +
                 "e_pro." + pro_cod + "=pml1." + pml_cod ;
            join[4] = "INNER";
            join[5] = GlobalNames.ESQUEMA_GENERICO + "a_tpr";
            join[6] = "e_pro." + pro_tip + "=" + GlobalNames.ESQUEMA_GENERICO + "a_tpr." + tpr_cod;
            join[7] = "LEFT";
            join[8] = GlobalNames.ESQUEMA_GENERICO + "a_tpml";
            join[9] = "e_pro." + pro_tip + "=" + GlobalNames.ESQUEMA_GENERICO + "a_tpml." + tpml_cod;                    
            join[10] = "INNER";
            join[11] = GlobalNames.ESQUEMA_GENERICO + "a_aml";
            join[12] = "e_pro." + pro_are + "=" + GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_cod;                   
            join[13] = "false";

            sql = oad.join(from,where,join);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while ( rs.next() ) {
                cont++;
                String descripcion = rs.getString("nombreProc");
                defProcVO.setTxtDescripcion(descripcion);
                fechaLH = rs.getString(pro_flh);
                defProcVO.setFechaLimiteHasta(fechaLH);
                fechaLD = rs.getString(pro_fld);
                defProcVO.setFechaLimiteDesde(fechaLD);
                String codArea = rs.getString(pro_are);
                defProcVO.setCodArea(codArea);
                String descArea = rs.getString(aml_valor);
                defProcVO.setDescArea(descArea);
                String codTipoProcedimiento = rs.getString(tpr_cod);
                defProcVO.setCodTipoProcedimiento(codTipoProcedimiento);
                String desTipoProcedimiento = rs.getString(tpml_valor);
                defProcVO.setDescTipoProcedimiento(desTipoProcedimiento);
                String codEstado = rs.getString(pro_est);
                defProcVO.setCodEstado(codEstado);
                String codTipoInicio = rs.getString(pro_ini);
                defProcVO.setCodTipoInicio(codTipoInicio);
                String plazo = rs.getString(pro_plz);
                defProcVO.setPlazo(plazo);
                String tipoPlazo = rs.getString(pro_und);
                defProcVO.setTipoPlazo(tipoPlazo);
                String silencio = rs.getString(pro_sil);
                defProcVO.setTipoSilencio(silencio);
                String disponible = rs.getString(pro_din);
                defProcVO.setDisponible(disponible);
                String tramitacionInternet = rs.getString(pro_tin);
                defProcVO.setTramitacionInternet(tramitacionInternet);
                String poseeLocalizacion = rs.getString(pro_loc);
                defProcVO.setLocalizacion(poseeLocalizacion);
                String tramiteInicio = rs.getString(pro_tri);
                defProcVO.setTramiteInicio(tramiteInicio);
                String descripcionBreve = rs.getString(pro_des);
                defProcVO.setDescripcionBreve(descripcionBreve);
                defProcVO.setPorcentaje(rs.getString("PRO_PORCENTAJE"));
                // Se comprueba si el procedimiento está restringido
                defProcVO.setRestringido(rs.getString("PRO_RESTRINGIDO"));
                defProcVO.setBiblioteca(rs.getString("PRO_LIBRERIA"));
                defProcVO.setInteresadoOblig(rs.getString("PRO_INTOBL"));
                //Mai recuperamos el valor de soloWS, (recuperamos como String un numero)
                // para debug
                m_Log.debug("Recuperamos de la bd para soloWS: "+ rs.getString("PRO_SOLOWS"));
                //
                defProcVO.setSoloWS(rs.getString("PRO_SOLOWS"));
				defProcVO.setClaseBuzonEntradaHistorico(rs.getString("PLUGIN_RELAC_HISTORICO"));
                /***** Plugin de finalización no convencional de expediente ******/
                String codPlugin = rs.getString("PLUGIN_ANULACION_NOMBRE");
                String implClassPlugin = rs.getString("PLUGIN_ANULACION_IMPLCLASS");
                defProcVO.setCodServicioFinalizacion(codPlugin);
                defProcVO.setImplClassServicioFinalizacion(implClassPlugin);
                /***** Plugin de finalización no convencional de expediente ******/

                // #303601
                defProcVO.setNumeracionExpedientesAnoAsiento(rs.getInt("PRO_EXPNUMANOT"));
            }
            rs.close();
            st.close();

            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de E_PRO son : " + cont);
            defProcVO.setNoEncontrado(cont);

      from = uor_cod + "," + uor_nom + "," + uor_cod_vis;
            where = pui_mun + "=" + codMunicipio + " AND " + pui_pro + "='" + codigo + "'";
            String[] join3 = new String[5];
            join3[0] = "E_PUI";
            join3[1] = "INNER";
            join3[2] = "a_uor";
            join3[3] = "e_pui." + pui_cod + "=" + "a_uor." + uor_cod;
            join3[4] = "false";
            sql = oad.join(from,where,join3);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar3 = "no";
            while ( rs.next() ) {
                entrar3 = "si";
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codUnidadInicio = rs.getString(uor_cod);
                dPVO.setCodUnidadInicio(codUnidadInicio);
                String codVisibleUnidadInicio = rs.getString(uor_cod_vis);
                dPVO.setCodVisibleUnidadInicio(codVisibleUnidadInicio);
                String descUnidadInicio = rs.getString(uor_nom);
                dPVO.setDescUnidadInicio(descUnidadInicio);
                listaUnidadesInicio.addElement(dPVO);
                defProcVO.setTablaUnidadInicio(listaUnidadesInicio);
            }
            if("no".equals(entrar3)) {
                defProcVO.setTablaUnidadInicio(listaUnidadesInicio);
                defProcVO.setCqUnidadInicio("1");  
            } else {
                defProcVO.setCqUnidadInicio("0");
            }
            rs.close();
            st.close();

            // PESTAÑA TRAMITES

            from = tra_cod + "," + tra_cou + "," + tml_valor +"," +cml_cod + "," + cml_valor +"," + cls_ord ;
            where = tra_mun + "=" + codMunicipio + " AND " + tra_pro + "='" + codigo + "' AND " +
                    tra_fba + " IS null";
            String[] join1 = new String[11];
            join1[0] = "E_TRA";
            join1[1] = "INNER";
            join1[2] = "e_tml";
            join1[3] = "e_tra." + tra_mun + "=e_tml." + tml_mun +  " AND " +
                    "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                    "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                    "e_tml." + tml_cmp + "='NOM'" + " AND " +
                    "e_tml." + tml_leng + "='"+idiomaDefecto+"'";
            join1[4] = "INNER";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_cml";
            join1[6] = "e_tra." + tra_cls + "=" + GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_cod +  " AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_cmp + "='NOM'" + " AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_leng + "='"+idiomaDefecto+"'";
            join1[7] = "INNER";
            join1[8] = GlobalNames.ESQUEMA_GENERICO + "a_cls";
            join1[9] = "e_tra." + tra_cls + "=" + GlobalNames.ESQUEMA_GENERICO + "a_cls." + cls_cod ;
            join1[10] = "false";

            sql = oad.join(from,where,join1);
            String parametros[] = {"6","6"};
            sql += oad.orderUnion(parametros);
            sql += "," + tra_cou;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codTramite = rs.getString(tra_cod);
                dPVO.setCodigoTramite(codTramite);
                String numeroTramite = rs.getString(tra_cou);
                dPVO.setNumeroTramite(numeroTramite);
                String nomTramite = rs.getString(tml_valor);
                dPVO.setNombreTramite(nomTramite);
                dPVO.setCodClasificacionTramite(rs.getString(cml_cod));
                dPVO.setDescClasificacionTramite(rs.getString(cml_valor));
                tramites.addElement(dPVO);
                defProcVO.setTramites(tramites);
            }
            if(entrar.equals("no")) {
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                dPVO.setNumeroTramite("");
                dPVO.setNombreTramite("");
                defProcVO.setTramites(tramites);
            }
            rs.close();
            st.close();
            // PESTAÑA DOCUMENTOS

            sql = "SELECT DOP_COD,  dpml1.DPML_VALOR AS nombreDocumento,  dpml2.DPML_VALOR AS condicion,  "
                    + "COUNT(ID_FIRMA) AS FIRMAS FROM   e_dpml dpml1,  e_dpml dpml2,  E_DOP left JOIN E_DEF_FIRMA "
                    + "ON (firma_mun=dop_mun and firma_proc=dop_pro and firma_cod_doc=dop_cod) WHERE DOP_MUN = " +codMunicipio
                    + "AND DOP_PRO        ='" + codigo +
                    "' AND e_dop.DOP_MUN  =dpml1.DPML_MUN AND e_dop.DOP_PRO  =dpml1.DPML_PRO "
                    + "AND e_dop.DOP_COD  =dpml1.DPML_DOP AND dpml1.DPML_CMP ='NOM' AND dpml1.DPML_LENG='"
                    + idiomaDefecto + "' AND "
                    + "e_dop.DOP_MUN  =dpml2.DPML_MUN AND e_dop.DOP_PRO  =dpml2.DPML_PRO AND "
                    + "e_dop.DOP_COD  =dpml2.DPML_DOP AND dpml2.DPML_CMP ='CON' AND dpml2.DPML_LENG='"
                    + idiomaDefecto + "'"
                    + " GROUP BY DOP_COD, dpml1.DPML_VALOR, dpml2.DPML_VALOR ORDER BY DOP_COD";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar1 = "no";
            while ( rs.next() ) {
                entrar1 = "si";
                cont2++;
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codDocumento = rs.getString(dop_cod);
                dPVO.setCodigoDocumento(codDocumento);
                String nombreDocumento = rs.getString("nombreDocumento");
                dPVO.setNombreDocumento(nombreDocumento);
                String condicion = rs.getString("condicion");
                dPVO.setCondicion(condicion);
                if (rs.getInt("FIRMAS")>0) dPVO.setRequiereFirma("1");
                else dPVO.setRequiereFirma("0");
                listasDoc.addElement(dPVO);
                defProcVO.setListasDoc(listasDoc);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de E_DOP son : " + cont2);
            if(entrar1.equals("no")) {
                defProcVO.setListasDoc(listasDoc);
            }
            rs.close();
            st.close();

            // PESTAÑA CAMPOS

            from = pca_cod + "," + pca_des + "," + pca_plt + "," + pca_tda + "," + pca_tam + "," +
             pca_mas + "," + pca_obl + "," + pca_nor + "," + tda_des + "," + plt_des + "," +
             pca_rot + "," + pca_activo + "," + pca_desplegable+",PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO" +
			 ",EXPRESION_CAMPO_NUM_PROC.EXPRESION AS EXPRESION,"+" EXPRESION_CAMPO_CAL_PROC.EXPRESION AS EXPRESION_CAL "
                    + ", PCA_GROUP, PCA_POS_X, PCA_POS_Y";
            where = pca_mun + "=" + codMunicipio + " AND " + pca_pro + "='" + codigo + "'";
            String[] join4 = new String[14];
            join4[0] = "E_PCA";
            join4[1] = "INNER";
            join4[2] = "e_tda";
            join4[3] = "e_pca." + pca_tda + "=e_tda." + tda_cod;
            join4[4] = "INNER";
            join4[5] = "e_plt";
            join4[6] = "e_pca." + pca_plt + "=e_plt." + plt_cod;
            join4[7] = "LEFT";
            join4[8] = "EXPRESION_CAMPO_NUM_PROC";
            join4[9] = "e_pca." + pca_mun + " = EXPRESION_CAMPO_NUM_PROC.COD_ORGANIZACION " +
                       " AND E_PCA." + pca_pro +" = EXPRESION_CAMPO_NUM_PROC.COD_PROCEDIMIENTO "  +
                       " AND E_PCA." + pca_cod +" = EXPRESION_CAMPO_NUM_PROC.COD_CAMPO ";
            join4[10] = "LEFT";
            join4[11] = "EXPRESION_CAMPO_CAL_PROC";
            join4[12] = "e_pca." + pca_mun + " = EXPRESION_CAMPO_CAL_PROC.COD_ORGANIZACION " +
                       " AND E_PCA." + pca_pro +" = EXPRESION_CAMPO_CAL_PROC.COD_PROCEDIMIENTO "  +
                       " AND E_PCA." + pca_cod +" = EXPRESION_CAMPO_CAL_PROC.COD_CAMPO ";
            join4[13] = "false";
            

            sql = oad.join(from,where,join4);
            String parametros1[] = {"8","8"};
            sql += oad.orderUnion(parametros1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar2 = "no";
            while ( rs.next() ) {
                entrar2 = "si";
                cont3++;
                DefinicionCampoValueObject dCVO = new DefinicionCampoValueObject();
                String codCampo = rs.getString(pca_cod);
                dCVO.setCodCampo(codCampo);
                String descCampo = rs.getString(pca_des);
                dCVO.setDescCampo(descCampo);
                String codTipoDato = rs.getString(pca_tda);
                dCVO.setCodTipoDato(codTipoDato);
                String codCampoDesplegable = "";
                if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                    codCampoDesplegable = rs.getString(pca_desplegable);
                    dCVO.setCodPlantilla(codCampoDesplegable);
                    /*codCampoDesplegable = rs.getString(pca_desplegable);
                    String codPlantilla = m_CommonProperties.getString("E_PLT.CodigoPlantillaCampoDesplegable");
                    dCVO.setCodPlantilla(rs.getString(pca_plt));*/
                }else if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))){ //desplegables externos
                    codCampoDesplegable = rs.getString(pca_desplegable);
                    dCVO.setCodPlantilla(codCampoDesplegable);
                    /*codCampoDesplegable = rs.getString(pca_desplegable);
                    String codPlantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                    dCVO.setCodPlantilla(rs.getString(pca_plt));*/
                } else {
                    String codPlantilla = rs.getString(pca_plt);
                    dCVO.setCodPlantilla(codPlantilla);
                }
                if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                    
                    PreparedStatement stAux;
                    ResultSet rsAux = null;
                    sql="select max ("+oad.tamanoTexto("valores.des_nom")+") from e_des_val valores where valores.des_cod='"+codCampoDesplegable+"'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stAux = con.prepareStatement(sql);
                    rsAux = stAux.executeQuery();
                    if ( rsAux.next() ) {
                          String  tam=rsAux.getString(1);
                          dCVO.setTamano(tam);
                    }
                    rsAux.close();
                    stAux.close();
                    
                    
                } else
                {
                    String tamano = rs.getString(pca_tam);
                    dCVO.setTamano(tamano);
                }
                
                String mascara = rs.getString(pca_mas);
                dCVO.setDescMascara(mascara);
                String obligatorio = rs.getString(pca_obl);
                dCVO.setObligat(obligatorio);
                String numeroOrden = rs.getString(pca_nor);
                dCVO.setOrden(numeroOrden);
                String descPlantilla = rs.getString(plt_des);
                dCVO.setDescPlantilla(descPlantilla);
                String descTipoDato = rs.getString(tda_des);
                dCVO.setDescTipoDato(descTipoDato);
                String rotulo = rs.getString(pca_rot);
                dCVO.setRotulo(rotulo);
                String activo = rs.getString(pca_activo);
                dCVO.setActivo(activo);
                String oculto = rs.getString("PCA_OCULTO");
                dCVO.setOculto(oculto);
                String bloqueado = rs.getString("PCA_BLOQ");
                dCVO.setBloqueado(bloqueado);
                //PLAZO_AVISO,PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                dCVO.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                dCVO.setCheckPlazoFecha(checkPlazoFecha);                
                String Validacion = rs.getString("EXPRESION");
                dCVO.setValidacion(Validacion);    
                String Operacion = rs.getString("EXPRESION_CAL");
                dCVO.setOperacion(Operacion);
                String agrupacionCampo = rs.getString("PCA_GROUP");
                if(agrupacionCampo != null){
                    dCVO.setCodAgrupacion(agrupacionCampo);
                }else{
                    dCVO.setCodAgrupacion("DEF");
                }//if(agrupacionCampo != null || !agrupacionCampo.equalsIgnoreCase(""))
                String posX = rs.getString("PCA_POS_X");
                dCVO.setPosX(String.valueOf(posX));
                String posY = rs.getString("PCA_POS_Y");
                dCVO.setPosY(String.valueOf(posY));
                listasCampos.addElement(dCVO); 
                defProcVO.setListaCampos(listasCampos);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de E_PCA son : " + cont3);
            if(entrar2.equals("no")) {
                defProcVO.setListaCampos(listasCampos);
            }
            rs.close();
            st.close();
            
            //AGRUPACION DE CAMPOS
            if(m_Log.isDebugEnabled()) m_Log.debug("Cargamos las agrupaciones de campos : BEGIN");
            sql = "Select PCA_ID_GROUP, PCA_DESC_GROUP, PCA_ORDER_GROUP, PCA_ACTIVE From E_PCA_GROUP where PCA_PRO = '" + codigo + "'"
                    + " order by PCA_ORDER_GROUP ASC";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while ( rs.next() ) {
                DefinicionAgrupacionCamposValueObject defAgrupCamposVO = new DefinicionAgrupacionCamposValueObject();
                String codAgrupacion = rs.getString("PCA_ID_GROUP");
                defAgrupCamposVO.setCodAgrupacion(codAgrupacion);
                String descAgrupacion = rs.getString("PCA_DESC_GROUP");
                defAgrupCamposVO.setDescAgrupacion(descAgrupacion);
                Integer ordenAgrupacion = rs.getInt("PCA_ORDER_GROUP");
                defAgrupCamposVO.setOrdenAgrupacion(ordenAgrupacion);
                String agrupacionActiva =rs.getString("PCA_ACTIVE");
                defAgrupCamposVO.setAgrupacionActiva(agrupacionActiva);
                listasAgrupaciones.addElement(defAgrupCamposVO);
                defProcVO.setListaAgrupaciones(listasAgrupaciones);
            }//while ( rs.next() ) 
            rs.close();
            st.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("Cargamos las agrupaciones de campos : END");

            // PESTAÑA DE ENLACES

            sql = "SELECT " + enp_cod + "," + enp_des + "," + enp_url + "," + enp_est + " FROM E_ENP WHERE " +
                    enp_mun + "=" + codMunicipio + " AND " + enp_pro + "='" + codigo + "'";
            String parametros2[] = {"1","1"};
            sql += oad.orderUnion(parametros2);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar4 = "no";
            while(rs.next()) {
                entrar4 = "si";
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codEnlace = rs.getString(enp_cod);
                dPVO.setCodEnlace(codEnlace);
                String descEnlace = rs.getString(enp_des);
                dPVO.setDescEnlace(descEnlace);
                String urlEnlace = rs.getString(enp_url);
                dPVO.setUrlEnlace(urlEnlace);
                String estadoEnlace = rs.getString(enp_est);
                if("1".equals(estadoEnlace)) {
                    dPVO.setEstadoEnlace("S");
                } else if("0".equals(estadoEnlace)) {
                    dPVO.setEstadoEnlace("N");
                }
                listaEnlaces.addElement(dPVO);
                defProcVO.setListaEnlaces(listaEnlaces);
            }
            if("no".equals(entrar4)) {
                defProcVO.setListaEnlaces(listaEnlaces);
            }
            rs.close();
            st.close();
            // PESTAÑA DE ROLES

            sql = "SELECT " + rol_cod + "," + rol_des + "," + rol_pde + ",ROL_PCW FROM E_ROL WHERE " + rol_mun + "=" +
                    codMunicipio + " AND " + rol_pro + "='" + codigo + "'";
            String parametros3[] = {"1","1"};
            sql += oad.orderUnion(parametros3);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar5 = "no";
            while ( rs.next() ) {
                entrar5 = "si";
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codRol = rs.getString(rol_cod);
                dPVO.setCodRol(codRol);
                String descRol = rs.getString(rol_des);
                dPVO.setDescRol(descRol);
                String rolPorDefecto = rs.getString(rol_pde);
                dPVO.setRolPorDefecto(rolPorDefecto);
                String consultaWeb = rs.getString("ROL_PCW");
                dPVO.setConsultaWebRol(consultaWeb);
                listaRoles.addElement(dPVO);
                defProcVO.setListaRoles(listaRoles);
            }
            if("no".equals(entrar5)) {
                defProcVO.setListaRoles(listaRoles);
            }

            // PESTAÑA DE VISTA DE EXPEDIENTES PENDIENTES            
            ArrayList<CampoListadoPendientesProcedimientoVO> camposSeleccionados = CamposListadoPendientesProcedimientoDAO.getInstance().getCamposSeleccionados(defProcVO.getTxtCodigo(), defProcVO.getCodMunicipio(), con);
            m_Log.debug("************* campos vista pendientes seleccionados: " + camposSeleccionados.size());
            defProcVO.setCamposPendientesSeleccionados(camposSeleccionados);

            rs.close();
            st.close();

        } catch (Exception e) {
            defProcVO = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
        }
        finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("buscar");
        return defProcVO;
    }

    public Vector consultar(DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("consultar");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sqlTotal = "";
        String sql = "";
        String sqlWhere = "";
        int cont = 0;
        Vector consulta = new Vector();
        TransformacionAtributoSelect transformador;
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(oad);
            con = oad.getConnection();
            transformador = new TransformacionAtributoSelect(oad);
            st = con.createStatement();
            sql = "SELECT DISTINCT " + pro_mun + "," + org_des + "," + pro_cod + ",e_pml1." + pml_valor +
           " AS nombreProcedimiento, " + oad.convertir(pro_flh, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO , "DD/MM/YYYY") +
                    " AS " + pro_flh;
            if(defProcVO.getCodUnidadInicio() ==null || defProcVO.getCodUnidadInicio().equals("")) {
               sql += " FROM E_PRO," + GlobalNames.ESQUEMA_GENERICO + "A_ORG,e_pml e_pml1,e_pml e_pml2 ";
            } else {
               sql += " FROM E_PRO," + GlobalNames.ESQUEMA_GENERICO + "A_ORG,e_pml e_pml1,e_pml e_pml2, E_PUI ";
            }
            if(defProcVO.getCodMunicipio() !=null) {
                if (!"".equals(defProcVO.getCodMunicipio())) {
                    String condicion = pro_mun + "=" + defProcVO.getCodMunicipio();
                    if (!"".equals(condicion)) {
                        sqlWhere += " WHERE " + condicion + " ";
                        m_Log.debug("COMPROBAR 1 -> "+condicion);
                    }
                }
            }

            if(defProcVO.getTxtCodigo() != null && !"".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getTxtCodigo())) {
                    String condicion = transformador.construirCondicionWhereConOperadores(pro_cod, defProcVO.getTxtCodigo().trim(),false);
                    if (!"".equals(condicion)) {
                        sqlWhere += " AND " + condicion + " ";
                        m_Log.debug("COMPROBAR 2 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getTxtCodigo() != null && "".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getTxtCodigo())) {
                    String condicion = transformador.construirCondicionWhereConOperadores(pro_cod, defProcVO.getTxtCodigo().trim(),false);
                    if (!"".equals(condicion)) {
                        sqlWhere += " WHERE " + condicion + " ";
                        m_Log.debug("COMPROBAR 3 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getTxtDescripcion() != null && !"".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getTxtDescripcion())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("e_pml1." + pml_valor, defProcVO.getTxtDescripcion().trim(),false);
                    if (!"".equals(condicion)) {
                        sqlWhere += " AND " + condicion + " ";
                        m_Log.debug("COMPROBAR 4 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getTxtDescripcion() != null && "".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getTxtDescripcion())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("e_pml1." + pml_valor, defProcVO.getTxtDescripcion().trim(),false);
                    if (!"".equals(condicion)) {
                        sqlWhere += " WHERE " + condicion + " ";
                        m_Log.debug("COMPROBAR 5 -> "+condicion);
                    }
                }
            }

       // Descripcion Breve de Procedimiento
       if (defProcVO.getDescripcionBreve() != null) {
           if (!defProcVO.getDescripcionBreve().equals("")) {
               String condicion = transformador.construirCondicionWhereConOperadores(
                       pro_des, defProcVO.getDescripcionBreve().trim(), false);
               if (!condicion.equals("")) {
                   if (sqlWhere.equals("")) sqlWhere += " WHERE " + condicion + " ";
                   else sqlWhere += " AND " + condicion + " ";
               }
           }
       }
           
            if(defProcVO.getFechaLimiteDesde() != null && !"".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getFechaLimiteDesde())) {
                    String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha(pro_fld, defProcVO.getFechaLimiteDesde());
                    if (!"".equals(condicion)) {
                        sqlWhere += " AND " + condicion + " ";
                        m_Log.debug("CAMPO 6 -> "+pro_fld);
                        m_Log.debug("COMPROBAR 6 -> "+condicion);
                        m_Log.debug("CONDICION 6 -> "+defProcVO.getFechaLimiteDesde());
                    }
                }
            }
            if(defProcVO.getFechaLimiteDesde() !=null && "".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getFechaLimiteDesde())) {
                    String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha(pro_fld, defProcVO.getFechaLimiteDesde());
                    if (!"".equals(condicion)) {
                        sqlWhere += " WHERE " + condicion + " ";
                        m_Log.debug("COMPROBAR 7 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getFechaLimiteHasta() !=null && !"".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getFechaLimiteHasta())) {
                    String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha(pro_flh, defProcVO.getFechaLimiteHasta());
                    if (!"".equals(condicion)) {
                        sqlWhere += " AND " + condicion + " ";
                        m_Log.debug("COMPROBAR 8 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getFechaLimiteHasta() !=null && "".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getFechaLimiteHasta())) {
                    String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha(pro_flh, defProcVO.getFechaLimiteHasta());
                    if (!"".equals(condicion)) {
                        sqlWhere += " WHERE " + condicion + " ";
                        m_Log.debug("COMPROBAR 9 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getCodArea() !=null && !"".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getCodArea())) {
                    String condicion = transformador.construirCondicionWhereConOperadores(pro_are, defProcVO.getCodArea().trim(),true);
                    if (!"".equals(condicion)) {
                        sqlWhere += " AND " + condicion + " ";
                        m_Log.debug("COMPROBAR 10 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getCodArea() !=null && "".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getCodArea())) {
                    String condicion = transformador.construirCondicionWhereConOperadores(pro_are, defProcVO.getCodArea().trim(),true);
                    if (!"".equals(condicion)) {
                        sqlWhere += " WHERE " + condicion + " ";
                        m_Log.debug("COMPROBAR 11 -> "+condicion);
                    }
                }
            }
            if(defProcVO.getCodTipoProcedimiento() !=null && !"".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getCodTipoProcedimiento())) {
                    String condicion = transformador.construirCondicionWhereConOperadores(pro_tip, defProcVO.getCodTipoProcedimiento().trim(),true);
                    if (!"".equals(condicion)) {
                        sqlWhere += " AND " + condicion + " ";
                    }
                }
            }
            if(defProcVO.getCodTipoProcedimiento() !=null && "".equals(sqlWhere)) {
                if (!"".equals(defProcVO.getCodTipoProcedimiento())) {
                    String condicion = transformador.construirCondicionWhereConOperadores(pro_tip, defProcVO.getCodTipoProcedimiento().trim(),true);
                    if (!"".equals(condicion)) {
                        sqlWhere += " WHERE " + condicion + " ";
                    }
                }
            }
            //filtro por campo pro_libreria, biblioteca/
            if(defProcVO.getBiblioteca() !=null && defProcVO.getBiblioteca().equals("1")) {
                String condicion = "PRO_LIBRERIA = "+defProcVO.getBiblioteca();
                    if (!"".equals(condicion)) {
                        if(!"".equals(sqlWhere)) sqlWhere += " AND ";
                        else sqlWhere += " WHERE ";
                        sqlWhere += condicion + " ";
                    }
            }

     if(defProcVO.getListaCodUnidadInicio().size()>0 && !"".equals(sqlWhere)) {
       sqlWhere += " AND " + pro_mun + "=" + pui_mun + " AND " + pro_cod + "=" + pui_pro + " AND ";
       String listaCodUnidadInicio = (String)defProcVO.getListaCodUnidadInicio().elementAt(0);
       for (int i=1;i<defProcVO.getListaCodUnidadInicio().size();i++) {
            listaCodUnidadInicio = listaCodUnidadInicio + "|" + ((String)defProcVO.getListaCodUnidadInicio().elementAt(i)).trim();
       }
       String condicion = transformador.construirCondicionWhereConOperadores(pui_cod, listaCodUnidadInicio,true);
                    if (!"".equals(condicion)) {
         sqlWhere += condicion + " ";
                    }
                }

     if(defProcVO.getListaCodUnidadInicio().size()>0 && "".equals(sqlWhere)) {
       sqlWhere += " WHERE " + pro_mun + "=" + pui_mun + " AND " + pro_cod + "=" + pui_pro + " AND ";
       String listaCodUnidadInicio = (String)defProcVO.getListaCodUnidadInicio().elementAt(0);
       for (int i=1;i<defProcVO.getListaCodUnidadInicio().size();i++) {
            listaCodUnidadInicio = listaCodUnidadInicio + "|" + ((String)defProcVO.getListaCodUnidadInicio().elementAt(i)).trim();
            }
       String condicion = transformador.construirCondicionWhereConOperadores(pui_cod, listaCodUnidadInicio,true);
                    if (!"".equals(condicion)) {
         sqlWhere += condicion + " ";
                }
            }

            if(!"".equals(sqlWhere)) {
              sqlWhere += " AND E_PRO." + pro_mun + "=" + GlobalNames.ESQUEMA_GENERICO + "A_ORG." + org_cod +
                        " AND e_pro." + pro_mun + "=e_pml1." + pml_mun + " AND e_pro." +
                        pro_cod + "=e_pml1." + pml_cod + " AND e_pml1." + pml_cmp +
                        "='NOM'" + " AND e_pml1." + pml_leng + "='"+idiomaDefecto+"'";
            }
            if("".equals(sqlWhere)) {
              sqlWhere += " WHERE E_PRO." + pro_mun + "=" + GlobalNames.ESQUEMA_GENERICO + "A_ORG." + org_cod + " AND e_pro." + pro_mun +
                        "=e_pml1." + pml_mun + " AND e_pro." + pro_cod + "=e_pml1." + pml_cod +
                        " AND e_pml1." + pml_cmp + "='NOM'" + " AND e_pml1." + pml_leng +
                        "='"+idiomaDefecto+"'";
            }

            String parametros[] = {"3","3"};
            sqlWhere += oad.orderUnion(parametros);

            sqlTotal = sql + sqlWhere;

            if(m_Log.isDebugEnabled()) m_Log.debug(sqlTotal);
     rs = st.executeQuery(sqlTotal);
            while ( rs.next() ) {
                cont++;
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codMunicipio = rs.getString(pro_mun);
                dPVO.setCodMunicipio(codMunicipio);
                String descMunicipio = rs.getString(org_des);
                dPVO.setDescMunicipio(descMunicipio);
                String codProcedimiento = rs.getString(pro_cod);
                dPVO.setTxtCodigo(codProcedimiento);
                String descProcedimiento = rs.getString("nombreProcedimiento");
                dPVO.setTxtDescripcion(descProcedimiento);
                String fechaLimiteHasta = rs.getString(pro_flh);
                String fueraLimite = "no";
                if(m_Log.isDebugEnabled()) m_Log.debug("la fecha limite es : " + fechaLimiteHasta);
                if(fechaLimiteHasta != null && !"".equals(fechaLimiteHasta)) {
                    Calendar calendario = Calendar.getInstance();
                    java.util.Date date = calendario.getTime();
                    if(m_Log.isDebugEnabled()) m_Log.debug("la fecha actual es : " + date);                    
                    java.util.Date dateLimite = Fecha.obtenerDate(fechaLimiteHasta);
                    if(m_Log.isDebugEnabled()) m_Log.debug("la fecha limite como un DATE es : " + dateLimite);
                    if(date.compareTo(dateLimite) >=0) {
                        fueraLimite = "si";
                    }
                }
                dPVO.setFueraLimite(fueraLimite);
                consulta.addElement(dPVO);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de consultar son : " + cont);

            rs.close();
            st.close();

        } catch (Exception e) {
            defProcVO = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
        }
        finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("consultar");
        return consulta;
    }
     public int modificar(DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException{
         m_Log.info("Metodo modificar");
        String sql= "";
        String sqlDoc = "";
        String sqlBorrar = "";
        String codMunicipio = defProcVO.getCodMunicipio();
        String codigo = defProcVO.getTxtCodigo();
        int res = 0;
        int res1 = 0;
        int res2 = 0;
        int res3 = 0;
        int resBorrar =0;
        int resEnlaces = 0;
        int resBorrarDPML =0;
        int resTotal = 0;
        int resRoles = 0;

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
    Statement stmt = null;
        ResultSet rs = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("Entra en el modificar del DAO");

        try{

            if(m_Log.isDebugEnabled()) m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            if(m_Log.isDebugEnabled()) m_Log.debug("A por la conexion");
            conexion = abd.getConnection();

            conexion.setAutoCommit(false) ;
            abd.inicioTransaccion(conexion);

            // PESTAÑA DATOS
            if(m_Log.isDebugEnabled()) m_Log.debug("el tipo de plazo es : " + defProcVO.getTipoPlazo());

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
                                     
            sql += ",PRO_INTOBL=" +defProcVO.getInteresadoOblig();
            
            //Mai:Engadimos a actualizacion para a columna de soloWS
            //Log
             m_Log.debug("DefinicionProcedimientosDAO. Modificar: Valor que se le pasa para soloWS: "+ defProcVO.getSoloWS());
            
             sql += " ,PRO_SOLOWS= " +defProcVO.getSoloWS();
            
			if(defProcVO.getClaseBuzonEntradaHistorico() == null || defProcVO.getClaseBuzonEntradaHistorico().equals(""))                    
                sql += ",PLUGIN_RELAC_HISTORICO = " + null;
            else
                sql += ",PLUGIN_RELAC_HISTORICO='" +defProcVO.getClaseBuzonEntradaHistorico() +"'";
                

            if(defProcVO.getPorcentaje()!=null && defProcVO.getPorcentaje().length()>0)
                sql += ",PRO_PORCENTAJE=" + defProcVO.getPorcentaje();
            else
                sql += ",PRO_PORCENTAJE=" + null;

            /** SE INDICA SI EL PROCEDIMIENTO ESTÁ RESTRINGIDO **/
            if(defProcVO.getRestringido()!=null && ("0".equals(defProcVO.getRestringido()) ||  "1".equals(defProcVO.getRestringido())))
                sql += ", PRO_RESTRINGIDO=" + defProcVO.getRestringido();
            else
                sql += ", PRO_RESTRINGIDO=0";
            
            /** se modifica el campo pro_libreria de acuerdo al valor del  check **/
            if(defProcVO.getBiblioteca()!=null && ("0".equals(defProcVO.getBiblioteca()) ||  "1".equals(defProcVO.getBiblioteca())))
                sql += ", PRO_LIBRERIA=" + defProcVO.getBiblioteca();
            else
                sql += ", PRO_LIBRERIA=0";
            
            // #303601
            sql += ", PRO_EXPNUMANOT=" + defProcVO.getNumeracionExpedientesAnoAsiento();

            if(StringOperations.stringNoNuloNoVacio(defProcVO.getCodServicioFinalizacion()) && StringOperations.stringNoNuloNoVacio(defProcVO.getImplClassServicioFinalizacion())){
                sql += ",PLUGIN_ANULACION_NOMBRE='" + defProcVO.getCodServicioFinalizacion() + "',PLUGIN_ANULACION_IMPLCLASS='" + defProcVO.getImplClassServicioFinalizacion() + "'";
            }else
                sql += ",PLUGIN_ANULACION_NOMBRE=NULL,PLUGIN_ANULACION_IMPLCLASS=NULL";

            sql += " WHERE " + pro_mun + "=" + codMunicipio + " AND " +
                    pro_cod + "='" + codigo + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO. Modificar. sql: " + sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el modificar de E_PRO son res:::::::::::::: : " + res);
            ps.close();

            sql = "UPDATE E_PML SET " + pml_valor + "='" + defProcVO.getTxtDescripcion() + "' WHERE " + pml_mun + "=" + codMunicipio +
                    " AND " + pml_cod + "='" + codigo + "' AND " + pml_cmp + "='NOM' AND " +
                    pml_leng + "='"+idiomaDefecto+"'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el modificar de E_PML son res1:::::::::::::: : " + res1);
            ps.close();

            Vector listaCodUnidadInicio = defProcVO.getListaCodUnidadInicio();
            if(listaCodUnidadInicio != null) {
                sqlBorrar = "DELETE FROM E_PUI WHERE " + pui_mun + "=" + codMunicipio + " AND " + pui_pro + "='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                if(m_Log.isDebugEnabled()) m_Log.debug(ps.toString());
                resBorrar = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_PUI son resBorrar:::::::::::::: : " + resBorrar);
                ps.close();
                for(int i=0;i<listaCodUnidadInicio.size();i++) {
                    sql = "INSERT INTO E_PUI (" + pui_mun + "," + pui_pro + "," + pui_cod + ") VALUES (" +
                            defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() + "'," +
                            listaCodUnidadInicio.elementAt(i) + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                    ps = conexion.prepareStatement(sql);
                    res3 = ps.executeUpdate();
                    if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_PUI son res3:::::::::::::: : " + res3);
                    ps.close();
                }
            }

            // PESTAÑA DOCUMENTOS

            Vector listaNombresDoc = defProcVO.getListaNombresDoc();
            Vector listaCondicionDoc = defProcVO.getListaCondicionDoc();
            Vector listaCodigosDoc = defProcVO.getListaCodigosDoc();

            for(int j=0;j<listaNombresDoc.size();j++) {
                String existeDOE = "no";
                sql= "SELECT " + doe_num + " FROM E_DOE WHERE " + doe_mun + "=" + defProcVO.getCodMunicipio() +
                        " AND " + doe_pro + "='" + defProcVO.getTxtCodigo() + "' AND " + doe_cod + "=" +
                        listaCodigosDoc.elementAt(j);
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrarDPML += ps.executeUpdate();
                    ps.close();

                    sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio + " AND " +
                            dop_pro + "='" + codigo + "' AND " + dop_cod + "=" + listaCodigosDoc.elementAt(j);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrar += ps.executeUpdate();
                    ps.close();

                    sqlDoc = "INSERT INTO E_DOP (" + dop_mun + "," + dop_pro + "," + dop_cod +
                            ") VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();

                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'NOM','"+idiomaDefecto+"','" +
                            listaNombresDoc.elementAt(j) + "')";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();

                    sqlDoc = "INSERT INTO E_DPML VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "'," + listaCodigosDoc.elementAt(j) + ",'CON','"+idiomaDefecto+"','" +
                            listaCondicionDoc.elementAt(j) + "')";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();
                }else
                {
                     sqlDoc = "UPDATE  E_DPML  SET DPML_VALOR='"+ listaNombresDoc.elementAt(j)+"' WHERE DPML_MUN=" + defProcVO.getCodMunicipio() + " AND DPML_PRO='" +
                            defProcVO.getTxtCodigo() + "' AND DPML_DOP=" + listaCodigosDoc.elementAt(j) + " AND DPML_CMP='NOM' AND DPML_LENG='"+idiomaDefecto + "'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();
                    
                    sqlDoc = "UPDATE  E_DPML  SET DPML_VALOR='"+ listaCondicionDoc.elementAt(j)+"' WHERE DPML_MUN=" + defProcVO.getCodMunicipio() + " AND DPML_PRO='" +
                            defProcVO.getTxtCodigo() + "' AND DPML_DOP=" + listaCodigosDoc.elementAt(j) + " AND DPML_CMP='CON' AND DPML_LENG='"+idiomaDefecto + "'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlDoc);
                    ps = conexion.prepareStatement(sqlDoc);
                    ps.executeUpdate();
                    ps.close();
                }
            }
            if(m_Log.isDebugEnabled()){
                m_Log.debug("las filas afectadas en el eliminar de E_DPML son resBorrarDPML:::::::::::::: : " + resBorrarDPML);
                m_Log.debug("las filas afectadas en el eliminar de E_DOP son resBorrar:::::::::::::: : " + resBorrar);
            }

            if(listaNombresDoc.size() != 0) {
                Vector listaDOE = new Vector();
                sql= "SELECT " + doe_cod + " FROM E_DOE WHERE " + doe_mun + "=" + defProcVO.getCodMunicipio() +
                        " AND " + doe_pro + "='" + defProcVO.getTxtCodigo() + "'";
                for(int i=0;i<listaNombresDoc.size();i++) {
                    sql += " AND " + doe_cod + "<>" + listaCodigosDoc.elementAt(i);
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                resBorrarDPML = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_DPML son :::::::::::::: : " + resBorrarDPML);
                ps.close();

                sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio +
                        " AND " + dop_pro + "='" + codigo + "'";
                for(int i=0;i<listaNombresDoc.size();i++) {
                    sqlBorrar += " AND " + dop_cod + "<>" + listaCodigosDoc.elementAt(i);
                }
                for(int i=0;i<listaDOE.size();i++) {
                    sqlBorrar += " AND " + dop_cod + "<>" + listaDOE.elementAt(i);
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_DOP son :::::::::::::: : " + resBorrar);
                ps.close();
            }
            if(listaNombresDoc.size() == 0) {
                Vector listaDOE = new Vector();
                sql= "SELECT " + doe_cod + " FROM E_DOE WHERE " + doe_mun + "=" + defProcVO.getCodMunicipio() +
                        " AND " + doe_pro + "='" + defProcVO.getTxtCodigo() + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrarDPML += ps.executeUpdate();
                    ps.close();

                    sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio + " AND " +
                            dop_pro + "='" + codigo + "' AND " + dop_cod + "<>" + listaDOE.elementAt(i);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrar += ps.executeUpdate();
                    ps.close();
                }
                if(m_Log.isDebugEnabled()){
                    m_Log.debug("las filas afectadas en el eliminar de E_DPML son resBorrarDPML:::::::::::::: : " + resBorrarDPML);
                    m_Log.debug("las filas afectadas en el eliminar de E_DOP son resBorrar:::::::::::::: : " + resBorrar);
                }

                if(listaDOE.size() == 0) {
                    sqlBorrar = "DELETE FROM E_DPML WHERE " + dpml_mun + "=" + codMunicipio + " AND " + dpml_pro + "='" +
                            codigo + "'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrarDPML = ps.executeUpdate();
                    if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_DPML son :::::::::::::: : " + resBorrarDPML);
                    ps.close();

                    sqlBorrar = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio + " AND " +
                            dop_pro + "='" + codigo + "'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrar = ps.executeUpdate();
                    if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_DOP son :::::::::::::: : " + resBorrar);
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
            Vector listaOrden = defProcVO.getListaOrden();
            Vector listaRotulo = defProcVO.getListaRotulo();
            Vector listaActivos = defProcVO.getListaActivos();
            Vector listaOcultos = defProcVO.getListaOcultos();
            Vector listaBloqueados = defProcVO.getListaBloqueados();
            Vector listaPlazoFecha = defProcVO.getListaPlazoFecha();
            Vector listaCheckPlazoFecha = defProcVO.getListaCheckPlazoFecha();
            Vector listaValidaciones = defProcVO.getListaValidacion();
            Vector listaOperaciones = defProcVO.getListaOperacion();
            Vector listaAgrupacionCampo = defProcVO.getListaAgrupacionesCampo();
            Vector listaPosicionesX = defProcVO.getListaPosicionesX();
            Vector listaPosicionesY = defProcVO.getListaPosicionesY();
            String valida= "";
            

            /* Realizamos la comprobación de que campos pueden ser borrados y cuales no */
            HashMap insertar = new HashMap();
            boolean inserta = true;
            PreparedStatement statExiste = null;
            ResultSet rsExiste = null;
            for(int j=0;j<listaCodCampos.size();j++) {
                inserta = true;
                /* Comprobamos si el codigo del campo existe en otras tablas */
                if(m_Log.isDebugEnabled()) m_Log.debug("CAMPO ACTIVO = " + (String)listaActivos.elementAt(j));
                if ("NO".equalsIgnoreCase((String)listaActivos.elementAt(j))) {
                    String sqlExiste = "SELECT COUNT (*)  FROM e_pca WHERE e_pca.pca_cod =?";
                    sqlExiste += " AND e_pca.pca_pro = ?";
                    sqlExiste += " AND e_pca.pca_mun = ?";
                    sqlExiste += " AND ( e_pca.pca_cod IN (SELECT tnu_cod FROM e_tnu) OR e_pca.pca_cod IN (SELECT tfe_cod FROM e_tfe) OR e_pca.pca_cod IN (SELECT ttl_cod FROM e_ttl) OR e_pca.pca_cod IN (SELECT txt_cod FROM e_txt) OR e_pca.pca_cod IN (SELECT TDE_COD FROM E_TDE))";

                    statExiste = conexion.prepareStatement(sqlExiste);
                    int i=1;
                    statExiste.setString(i++,(String)listaCodCampos.elementAt(j));
                    statExiste.setString(i++,defProcVO.getTxtCodigo());
                    statExiste.setInt(i++,Integer.parseInt(defProcVO.getCodMunicipio()));                    

                    if(m_Log.isDebugEnabled()) m_Log.debug("CONSULTA = " + sqlExiste);

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
            if(m_Log.isDebugEnabled()) m_Log.debug("CAMPOS A INSERTAR: = " + insertar);
            /* Fin de la comprobación de los campos que pueden ser borrados y cuales no*/

            sqlBorrar = "DELETE FROM E_PCA WHERE " + pca_mun + "=" + codMunicipio + " AND " + pca_pro + "='" + codigo + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);

            ps = conexion.prepareStatement(sqlBorrar);
            resBorrar = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_PCA son :::::::::::::: : " + resBorrar);
            
            sql =" DELETE FROM EXPRESION_CAMPO_NUM_PROC WHERE COD_ORGANIZACION = " + codMunicipio +" AND COD_PROCEDIMIENTO = '" + codigo +"'";
            ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
            
             sql =" DELETE FROM EXPRESION_CAMPO_CAL_PROC WHERE COD_ORGANIZACION = " + codMunicipio +" AND COD_PROCEDIMIENTO = '" + codigo +"'";
            ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
                        
            Boolean seInserta = new Boolean(true);
            
            for(int j=0;j<listaCodCampos.size();j++) {
                seInserta = (Boolean)insertar.get((String)listaCodCampos.elementAt(j));
                if (seInserta.booleanValue()) 
				{
		            if(m_Log.isDebugEnabled()) m_Log.debug(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable")+" TIPO DE DATO :  "+listaCodTipoDato.elementAt(j));
		            String plantilla="",desplegable="";
		            if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) 
                            {
		                plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
		                desplegable=(String)listaCodPlantilla.elementAt(j);
		            }
                            else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt")))
                            {
                                plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
		                desplegable=(String)listaCodPlantilla.elementAt(j);
                            }
                            else 
                            {
		                plantilla=(String)listaCodPlantilla.elementAt(j);
		                desplegable="";
		            }
                    sql = "INSERT INTO E_PCA (" + pca_mun + "," + pca_pro + "," + pca_cod + "," + pca_des + "," +
                            pca_plt + "," + pca_tda + "," + pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," +
                            pca_rot + "," + pca_activo + "," + pca_desplegable + ",PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO, PERIODO_PLAZO,  PCA_GROUP,"
                            + "PCA_POS_X, PCA_POS_Y) "
                            + " VALUES(" + defProcVO.getCodMunicipio() + ",'" +
                            defProcVO.getTxtCodigo() + "','" + listaCodCampos.elementAt(j) + "','" + listaDescCampos.elementAt(j) +
                            "'," + plantilla + ",'" + listaCodTipoDato.elementAt(j) + "'," + listaTamano.elementAt(j);
                    if(listaMascara.elementAt(j) == null || "".equals(listaMascara.elementAt(j)) || " ".equals(listaMascara.elementAt(j))) 
					{
                        sql += ",null,";
                    } 
					else 
					{
                        sql += ",'" +  listaMascara.elementAt(j) + "',";
                    }
                    sql += listaObligatorio.elementAt(j) + "," + listaOrden.elementAt(j) + ",'" +
                           listaRotulo.elementAt(j) + "','" + ((String)listaActivos.elementAt(j)).toUpperCase() + "','" + desplegable+ "','"+ listaOcultos.elementAt(j)+"','"+ listaBloqueados.elementAt(j)+ "', ";
					//insertamos el campo PLAZO_AVISO Y pERIODO_PLAZO
                    
                    if(listaPlazoFecha.elementAt(j) == null || "".equals(listaPlazoFecha.elementAt(j)) || " ".equals(listaPlazoFecha.elementAt(j))) {
                    //if ((!listaCodTipoDato.elementAt(j).equals("3")) || (!"3".equals(listaCodTipoDato.elementAt(j)))){
                        sql += "null, null";
                    } else {
                        sql += "'" +  listaPlazoFecha.elementAt(j) + "','" + listaCheckPlazoFecha.elementAt(j) +"'";
                    }
                    
                    if(listaAgrupacionCampo.size() == 0
                            || listaAgrupacionCampo.elementAt(j) == null 
                            || "".equals(listaAgrupacionCampo.elementAt(j)) 
                            || " ".equals(listaAgrupacionCampo.elementAt(j))){
                        sql += ",null";
                    }else{
                        sql += ",'" + listaAgrupacionCampo.elementAt(j) + "'";
                    }
                    
                    if(listaPosicionesX.size() == 0
                            || listaPosicionesX.elementAt(j) == null 
                            || "".equals(listaPosicionesX.elementAt(j))
                            || " ".equals(listaPosicionesX.elementAt(j))
                            || "undefined".equals(listaPosicionesX.elementAt(j))){
                            sql +=",null";
                    }else{
                            sql +="," + listaPosicionesX.elementAt(j);
                    }

                    if(listaPosicionesY.size() == 0
                            || listaPosicionesY.elementAt(j) == null 
                            || "".equals(listaPosicionesY.elementAt(j))
                            || " ".equals(listaPosicionesY.elementAt(j))
                            || "undefined".equals(listaPosicionesY.elementAt(j))){
                            sql +=",null)";
                    }else{
                            sql +="," + listaPosicionesY.elementAt(j) + ")";
                    }
                    
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps = conexion.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                    
                    String tip_dat = (String) listaCodTipoDato.elementAt(j);
                    
                    //Solo se cargan las validacion para los campos numericos
                    valida = listaValidaciones.elementAt(j).toString().trim();
                    if (tip_dat.trim().equals("1") && !"".equals(valida))
                    {                                                
                        //valida = listaValidaciones.elementAt(contador_expresion).toString();
                        valida = valida.replace("/&lt;/g","<");  
                        valida = valida.replace("/&gt;/g",">"); 
                        valida = valida.replace("&lt;","<");  
                        valida = valida.replace("&gt;",">"); 

                        sql = " INSERT INTO EXPRESION_CAMPO_NUM_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,EXPRESION) VALUES " + 
                              " ("+ defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() +"','" +listaCodCampos.elementAt(j) +"','" + valida +                                
                              "')";
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();            
                        ps.close();                    
                        m_Log.debug("Grabacion de nueva expresion de validacion = " +listaValidaciones.elementAt(j));
                        
                    }
                    if (tip_dat.trim().equals("8") || tip_dat.trim().equals("9"))
                    {   
                        valida = listaOperaciones.elementAt(j).toString();
                        if (!"".equals(valida))                        
                        {
                            //valida = listaOperaciones.elementAt(contador_expresion_cal).toString();                            
                            sql = " INSERT INTO EXPRESION_CAMPO_CAL_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,TIPO_DATO,EXPRESION) VALUES " + 
                              " ("+ defProcVO.getCodMunicipio() + ",'" + defProcVO.getTxtCodigo() +"','" +listaCodCampos.elementAt(j) +"',"+tip_dat+",'" + valida +                                
                              "')";                    
                            ps = conexion.prepareStatement(sql);
                            ps.executeUpdate();    
                            ps.close();
                            m_Log.debug("grabacion de nueva expresion de calculo = " +listaOperaciones.elementAt(j));                              
                        }
                    }                    
                }
            }
            
            //Agrupaciones de campos suplementarios
            if(m_Log.isDebugEnabled()) m_Log.debug("Actualizamos las agrupaciones de campos suplementarios : BEGIN");
            Vector listaCodAgrupaciones = defProcVO.getListaCodAgrupaciones();
            Vector listaDescAgrupaciones = defProcVO.getListaDescAgrupaciones();
            Vector listaOrdenAgrupaciones = defProcVO.getListaOrdenAgrupaciones();
            Vector listaAgrupacionesActivas = defProcVO.getListaAgrupacionesActivas();
            
            for(int w=0; w<listaCodAgrupaciones.size(); w++){
                Boolean existe = false;
                //Comprobamos si existe ya una agrupacion con este codigo para el mismo procedimiento
                sql = "Select PCA_ID_GROUP from E_PCA_GROUP "
                        + " where PCA_ID_GROUP = '" + listaCodAgrupaciones.elementAt(w) + "' "
                        + " and PCA_PRO = '" + codigo + "'";
                ps = conexion.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    existe = true;
                }//while(rs.next())
                if(existe){
                    sql = "Update E_PCA_GROUP set PCA_DESC_GROUP = '" + listaDescAgrupaciones.elementAt(w) + "',"
                            + " PCA_ORDER_GROUP = " + listaOrdenAgrupaciones.elementAt(w) + ", "
                            + " PCA_ACTIVE = '" + listaAgrupacionesActivas.elementAt(w) + "' "
                            + " where PCA_ID_GROUP = '" + listaCodAgrupaciones.elementAt(w) + "'"
                            + " and PCA_PRO = '" + codigo + "'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps = conexion.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                }else{
                    sql = "Insert into E_PCA_GROUP (PCA_ID_GROUP, PCA_DESC_GROUP, PCA_ORDER_GROUP, PCA_PRO, PCA_ACTIVE) values ('" 
                        + listaCodAgrupaciones.elementAt(w) + "',"
                        + "'" + listaDescAgrupaciones.elementAt(w) + "',"
                        + listaOrdenAgrupaciones.elementAt(w) + ","
                        + "'" + codigo + "',"
                        + "'" + listaAgrupacionesActivas.elementAt(w) + "'"
                        + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps = conexion.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
               }//if(existe)
            }//for(int w=0; w<listaCodAgrupaciones; w++)
            if(m_Log.isDebugEnabled()) m_Log.debug("Actualizamos las agrupaciones de campos suplementarios : END");
            
            // PESTAÑA DE ENLACES
            Vector listaCodEnlaces = defProcVO.getListaCodEnlaces();
            Vector listaDescEnlaces = defProcVO.getListaDescEnlaces();
            Vector listaUrlEnlaces = defProcVO.getListaUrlEnlaces();
            Vector listaEstadoEnlaces = defProcVO.getListaEstadoEnlaces();

            sqlBorrar = "DELETE FROM E_ENP WHERE " + enp_mun + "=" + codMunicipio + " AND " + enp_pro + "='" + codigo + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
            ps = conexion.prepareStatement(sqlBorrar);
            resBorrar = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_ENP son :::::::::::::: : " + resBorrar);
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
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
            if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
            ps = conexion.prepareStatement(sqlBorrar);
            resBorrar = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_ROL son :::::::::::::: : " + resBorrar);
            ps.close();
            for(int m=0;m<listaCodRoles.size();m++) {
                sql = "INSERT INTO E_ROL (" + rol_mun + "," + rol_pro + "," + rol_cod + "," + rol_des + "," +
                        rol_pde + ",ROL_PCW) VALUES (" + defProcVO.getCodMunicipio() + ",'" +
                        defProcVO.getTxtCodigo() + "'," + listaCodRoles.elementAt(m) + ",'" +
                        listaDescRoles.elementAt(m) + "'," + listaPorDefecto.elementAt(m) + "," + listaConsultaWebRol.elementAt(m)+")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                resRoles = ps.executeUpdate();
                ps.close();
            }
            if(listaCodRoles.size() == 0 ) {
                resRoles = 1;
            }


            /************************** INSERCIÓN CAMPOS DE LA VISTA DE PENDIENTES DEL PROCEDIMIENTO ****************************/
            ArrayList<CampoListadoPendientesProcedimientoVO> campos = new ArrayList<CampoListadoPendientesProcedimientoVO>();
            Vector codigosCampos = defProcVO.getColeccionCodigosCamposPendientes();
            Vector nombresCampos = defProcVO.getColeccionNombresCamposPendientes();
            Vector ordenCampos   = defProcVO.getColeccionOrdenCamposPendientes();
            Vector camposSup     = defProcVO.getColeccionCampoSupCamposPendientes();
            Vector sizeCampos    = defProcVO.getColeccionTamanhoCamposPendientes();
            
            for(int i=0;i<codigosCampos.size();i++){
                String codCampo     = (String)codigosCampos.get(i);
                String nombre       = (String)nombresCampos.get(i);
                String orden        = (String)ordenCampos.get(i);
                String size         = (String)sizeCampos.get(i);
                String esCampoSup   = (String)camposSup.get(i);

                CampoListadoPendientesProcedimientoVO campo = new CampoListadoPendientesProcedimientoVO();
                campo.setCodigo(codCampo);
                campo.setNombreCampo(nombre);
                campo.setOrden(orden);
                campo.setTamanho(size);
                if(esCampoSup!=null && "false".equals(esCampoSup))
                    campo.setCampoSuplementario(false);
                else
                if(esCampoSup!=null && "true".equals(esCampoSup))
                    campo.setCampoSuplementario(true);
                
                campos.add(campo);

            }//for

            m_Log.debug(" =============> Nº de campos a insertar son: " + campos.size());
            boolean camposInsertados = CamposListadoPendientesProcedimientoDAO.getInstance().guardarCampos(campos,defProcVO.getTxtCodigo(),Integer.parseInt(defProcVO.getCodMunicipio()), conexion);
            m_Log.debug(" La operación de guardar campos es: " + camposInsertados);
            
            /************************** INSERCIÓN CAMPOS DE LA VISTA DE PENDIENTES DEL PROCEDIMIENTO ****************************/
            

            if(res !=0 && resEnlaces !=0 && resRoles !=0 && camposInsertados)  {
                abd.finTransaccion(conexion);
                resTotal = 1;
            if(m_Log.isErrorEnabled()){
                m_Log.debug("MODIFICACION HECHA EN " + getClass().getName());
                m_Log.debug("res VALE: " + res + " resEnlaces VALE: " + resEnlaces  +
                            " resRoles VALE: " + resRoles);
                }
            } else {
                abd.rollBack(conexion);
                resTotal = 0;
            }

        } catch (Exception ex) {
            abd.rollBack(conexion);
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + this.getClass().getName());

        } finally {
            if (conexion != null)
                try{
                    abd.devolverConexion(conexion);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }
        return resTotal;
    }

    public int eliminar(DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException{

        String sql= "";
        String sqlBorrarE_DOP= "";
        int res = 0;
        int res1 = 0;
        int resBorrar =0;
        int resBorrar1 =0;
        int resENP = 0;
        int resPCA = 0;
        int resPCAGROUP = 0;
        int resROL = 0;
        int resIMPORTE = 0;

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;

        if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO. Entra en  ELIMINAR");

        String codMunicipio = defProcVO.getCodMunicipio();
        String codigo = defProcVO.getTxtCodigo();
        int cont = 0;

        try{

            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            conexion.setAutoCommit(false) ;
            int numExpedientes = numExpedientesProcedimiento(conexion,defProcVO);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO. Eliminar procedimiento. Numero de expedientes: " + numExpedientes);
            if (numExpedientes == 0){
                if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO. Eliminar procedimiento. Borrado físico. ");
                // Borrado fisico.
                DefinicionTramitesValueObject dtVO = new DefinicionTramitesValueObject();
                dtVO.setTxtCodigo(codigo);
                dtVO.setCodMunicipio(codMunicipio);

                // Trámites
                DefinicionTramitesManager.getInstance().eliminarFisicamente(dtVO, params);

                // Documentos.
                sqlBorrarE_DOP = "DELETE FROM E_DPML WHERE " + dpml_mun + "=" + codMunicipio + " AND " + dpml_pro + "='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrarE_DOP);
                PreparedStatement ps = conexion.prepareStatement(sqlBorrarE_DOP);
                resBorrar = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_DPML: " + resBorrar);
                ps.close();

                sqlBorrarE_DOP = "DELETE FROM E_DOP WHERE " + dop_mun + "=" + codMunicipio + " AND " + dop_pro + "='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrarE_DOP);
                ps = conexion.prepareStatement(sqlBorrarE_DOP);
                resBorrar1 = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_DOP: " + resBorrar1);
                ps.close();

                // Enlaces.
                sql = "DELETE FROM E_ENP WHERE " + enp_mun + "=" + codMunicipio + " AND " + enp_pro + "='" +
                        codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                resENP = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_ENP : " + resENP);
                ps.close();

                // Campos.
                sql = "DELETE FROM E_PCA WHERE " + pca_mun + "=" + codMunicipio + " AND " + pca_pro + "='" +
                        codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                resPCA = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_PCA : " + resPCA);
                ps.close();
                
                sql = "DELETE FROM E_PCA_GROUP WHERE PCA_PRO ='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                resPCAGROUP = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_PCA_GROUP : " + resPCAGROUP);
                ps.close();

                // Roles.
                sql = "DELETE FROM E_ROL WHERE " + rol_mun + "=" + codMunicipio + " AND " + rol_pro + "='" +
                        codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                resROL = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_ROl : " + resROL);
                ps.close();

                // Unidades de inicio.
                sql = "DELETE FROM E_PUI WHERE " + pui_mun + "=" + codMunicipio + " AND " + pui_pro + "='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res1 = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_PUI : " + res1);
                ps.close();

                // Procedimiento.
                sql = "DELETE FROM E_PML WHERE " + pml_mun + "=" + codMunicipio + " AND " + pml_cod + "='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res1 = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_PLM : " + res1);
                ps.close();

                              
                sql = "DELETE FROM EXPRESION_CAMPO_CAL_PROC WHERE COD_ORGANIZACION =" + codMunicipio +
                        " AND COD_PROCEDIMIENTO ='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en EXPRESION_CAMPO_CAL_PROC: " + res);
                ps.close();
                
                sql = "DELETE FROM EXPRESION_CAMPO_NUM_PROC WHERE COD_ORGANIZACION =" + codMunicipio +
                        " AND COD_PROCEDIMIENTO ='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en COD_ORGANIZACION: " + res);
                ps.close();
                
                sql = "DELETE FROM E_PRO WHERE " + pro_mun + "=" + codMunicipio +
                        " AND " + pro_cod + "='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("Filas borradas en E_PRO: " + res);
                ps.close();

            } else if (numExpedientes > 0){
                if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO. Eliminar procedimiento. Borrado lógico. ");
                sql = "UPDATE E_PRO SET "
                        + pro_flh + "=" + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)
                        + " WHERE " + pro_mun + "=" + codMunicipio + " AND " + pro_cod + "='" + codigo + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                PreparedStatement ps = conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                ps.close();
            } // else // Nada.

            abd.finTransaccion(conexion);

        } catch (Exception ex) {
            abd.rollBack(conexion);
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + this.getClass().getName());
        } finally {
            try{
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO. Exception: " + ex.getMessage()) ;
            }
        }
        return res;
    }

    public Vector catalogoProcedimientos(DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws TramitacionException, TechnicalException {

        m_Log.debug("catalogoProcedimientos");

        AdaptadorSQLBD oad = null;
        Connection con = null;
   Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector catalogo = new Vector();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
     st = con.createStatement();
            sql = "SELECT DISTINCT " + pro_mun + "," + pro_cod  + ",e_pml." + pml_valor + " AS nombreProcedimiento, "
                    + pro_are + " AS codArea," + GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_valor + " AS nombreArea "
                    + " FROM E_PRO,e_pml," + GlobalNames.ESQUEMA_GENERICO + "a_aml"
                    + " WHERE  e_pro." + pro_mun + "=e_pml." + pml_mun
                    + " AND e_pro." + pro_cod + "=e_pml." + pml_cod
                    + " AND e_pml." + pml_cmp + "='NOM'"
                    + " AND e_pml." + pml_leng + "='"+idiomaDefecto+"'"
                    + " and e_pro." + pro_are + "=" + GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_cod
                    + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_cmp + "='NOM'"
                    + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_aml." + aml_leng + "='"+idiomaDefecto+"'"
                    + " AND " + pro_fld + "<=" + oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)
                    + " AND ( " + pro_flh + " IS NULL "
                    + " OR " + oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)
                    + "<=" + pro_flh + " ) "
                    + " AND " + pro_est + "="+idiomaDefecto+""
                    + " ORDER BY nombreArea, nombreProcedimiento " ;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
     rs = st.executeQuery(sql);
            while ( rs.next() ) {
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                dPVO.setCodMunicipio(rs.getString(pro_mun));
                dPVO.setTxtCodigo(rs.getString(pro_cod));
                dPVO.setTxtDescripcion(rs.getString("nombreProcedimiento"));
                dPVO.setCodArea(rs.getString("codArea"));
                dPVO.setDescArea(rs.getString("nombreArea"));
                catalogo.addElement(dPVO);
            }

            rs.close();
            st.close();

        } catch (SQLException esql) {
            catalogo = null;
            esql.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO. Catalogo procedimientos: " + esql.getMessage()) ;
            throw new TramitacionException("DefinicionProcedimientosDAO. Catalogo procedimientos."+esql.getMessage(), esql);

        } catch (BDException e) {
            catalogo = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO. Catalogo procedimientos: " + e.getMessage()) ;
            throw new TramitacionException("DefinicionProcedimientosDAO. Catalogo procedimientos."+e.getMessage(), e);

        } finally {
            if (con != null)
                try{
                    oad.devolverConexion(con);
                } catch(Exception ex) {
                    catalogo = null;
                    ex.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO. Catalogo procedimientos: " + ex.getMessage()) ;
                    throw new TramitacionException("DefinicionProcedimientosDAO. Catalogo procedimientos."+ex.getMessage(), ex);
                }
        }

        m_Log.debug("catalogoProcedimientos");
        return catalogo;
    }

    public Vector catalogoProcedimientosTramites(DefinicionProcedimientosValueObject defProcVO,String[] params)
            throws TramitacionException, TechnicalException {

        m_Log.debug("catalogoProcedimientoTramites");

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        Vector catalogoTramites = new Vector();

        try{
            DefinicionTramitesValueObject tVO=null;

            
            sql.append("SELECT ").append(tra_mun).append(", ").append(tra_cod)
                    .append(", ").append(tra_cou).append(", ").append(cls_ord)
                    .append(", ").append(tml_valor).append(" FROM E_TRA,")
                    .append(GlobalNames.ESQUEMA_GENERICO).append("A_CLS, E_TML")
                    .append(" WHERE ").append(tra_cls).append(" = ").append(cls_cod)
                    .append(" AND ").append(tra_mun).append(" = ").append(tml_mun)
                    .append(" AND ").append(tra_pro).append(" = ").append(tml_pro)
                    .append(" AND ").append(tra_cod).append(" = ").append(tml_tra)
                    .append(" AND ").append(tra_mun).append(" = ? AND ")
                    .append(tra_pro).append(" = ? AND ")
                    .append(tra_fba).append( " IS null AND ")
                    .append(tml_cmp).append(" = 'NOM' AND ")
                    .append(tml_leng).append(" = ").append(idiomaDefecto)
                    .append(" ORDER BY ").append(cls_ord).append(", ")
                    .append(tra_cou);

            if(m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(st, 1,
                    defProcVO.getCodMunicipio(), defProcVO.getTxtCodigo());
            rs = st.executeQuery();
            
            while ( rs.next() ) {
                tVO = new DefinicionTramitesValueObject();
                tVO.setCodMunicipio(rs.getString(tra_mun));
                tVO.setCodigoTramite(rs.getString(tra_cod));
                tVO.setNumeroTramite(rs.getString(tra_cou));
                tVO.setTxtCodigo(defProcVO.getTxtCodigo()); // Codigo procedimiento
                catalogoTramites.addElement(tVO);
            }
            rs.close();
            st.close();

            // Informacion de cada tramite.
            for (int i=0; i<catalogoTramites.size(); i++) {
                tVO = (DefinicionTramitesValueObject) catalogoTramites.elementAt(i);
                DefinicionTramitesManager.getInstance().getTramite(tVO,tVO.getCodMunicipio(), params);

                TablasIntercambiadorasValueObject tiVO = new TablasIntercambiadorasValueObject();
                tiVO.setCodMunicipio(tVO.getCodMunicipio());
                tiVO.setCodProcedimiento(tVO.getTxtCodigo());
                tiVO.setCodTramite(tVO.getCodigoTramite());


                Vector ltF = new Vector();
                Vector ltDF = new Vector();
                String tipoltF="";
                String tipoltDF="";

                if ("Tramite".equals(tVO.getTipoCondicion())) {
                    tiVO.setNumeroCondicionSalida("0");
                    tiVO = DefinicionTramitesManager.getInstance().getListaTramitesFlujoSalidaSeleccionada(tiVO,params);
                    tipoltF = tiVO.getObligatorio();
                    ltF = tiVO.getListaTramitesSeleccion();

                } else if ("Pregunta".equals(tVO.getTipoCondicion()) || "Resolucion".equals(tVO.getTipoCondicion())) {
                    if ("TramiteSI".equals(tVO.getTipoFavorableSI())) {
                        tiVO.setNumeroCondicionSalida("1");
                        tiVO = DefinicionTramitesManager.getInstance().getListaTramitesFlujoSalidaSeleccionada(tiVO,params);
                        tipoltF = tiVO.getObligatorio();
                        ltF = tiVO.getListaTramitesSeleccion();

                    }
                    if ("TramiteNO".equals(tVO.getTipoFavorableNO())) {
                        tiVO.setNumeroCondicionSalida("2");
                        tiVO = DefinicionTramitesManager.getInstance().getListaTramitesFlujoSalidaSeleccionada(tiVO,params);
                        tipoltDF = tiVO.getObligatorio();
                        ltDF = tiVO.getListaTramitesSeleccion();

                    }
                }

                tVO.setListaTramitesFavorable(ltF);
                tVO.setListaTramitesDesfavorable(ltDF);
                tVO.setObligatorio(tipoltF);
                tVO.setObligatorioDesf(tipoltDF);

            }
        } catch (SQLException esql) {
            catalogoTramites = null;
            esql.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO.CatalogoProcedimientoTramites: " + esql.getMessage()) ;
            throw new TramitacionException("DefinicionProcedimientosDAO.CatalogoProcedimientoTramites."+esql.getMessage(), esql);

        } catch (BDException e) {
            catalogoTramites = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO.CatalogoProcedimientoTramites: " + e.getMessage()) ;
            throw new TramitacionException("DefinicionProcedimientosDAO.catalogoProcedimientoTramites."+e.getMessage(), e);

        } catch (AnotacionRegistroException e) { // Debido a DefinicionTramitesManager
            catalogoTramites = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO.CatalogoProcedimientoTramites: " + e.getMessage()) ;
            throw new TramitacionException("DefinicionProcedimientosDAO.catalogoProcedimientoTramites."+e.getMessage(), e);

        }finally {
            if (con != null)
                try{
                    oad.devolverConexion(con);
                } catch(Exception ex) {
                    catalogoTramites = null;
                    ex.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error("DefinicionProcedimientosDAO.CatalogoProcedimientoTramites: " + ex.getMessage()) ;
                    throw new TramitacionException("DefinicionProcedimientosDAO.CatalogoProcedimientoTramites."+ex.getMessage(), ex);
                }
        }

        m_Log.debug("CatalogoProcedimientoTramites");
        return catalogoTramites;
    }

    private int numExpedientesProcedimiento(Connection con, DefinicionProcedimientosValueObject defProcVO)
    throws SQLException {

        int cuenta;
        Statement st = con.createStatement();
        ResultSet rs;
        String sql = "SELECT COUNT(*) AS NUMERO_PROCEDIMIENTOS FROM E_EXP, E_PRO WHERE "
                + exp_mun + "=" + pro_mun
                + " and " + exp_pro + "=" + pro_cod
                + " and " + pro_mun + "=" + defProcVO.getCodMunicipio()
                + " and " + pro_cod + "='" + defProcVO.getTxtCodigo() + "' ";

        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = st.executeQuery(sql);
        if (rs.next()) {
            cuenta = rs.getInt("NUMERO_PROCEDIMIENTOS");
        } else cuenta = -1;
        rs.close();
        st.close();
        return cuenta;
    }

    /**
     * Metodo utilizado para obtener una descripcion de los campos desplegables que estan definidos
     * en un cierto procedimiento. La descripcion de los campos se devuelve en forma de vector. Cada 4 posiciones del
     * vector, se representa un elemento de un campo desplegable.
     * 
     * @param   codigoProc  Codigo del procedimiento para el que se quieren buscar los campos desplegables.
     * @param   params      Parametros de Conexion.
     * @return              Cada 4 posiciones representa un elemento de un campo desplegable.
     * Sea i, multiplo de 4: i es el codigo del campo desplegable, i+1 nombre del campo desplegable,
     * i+2 codigo del valor del campo desplegable, i+3 nombre del valor del campo desplegable.
     * @throws SQLException Si hay algún error al realizar las consultas SQL.
     */
    public Vector obtenerCampoDesplegables(String codigoProc, String[] params)
    throws SQLException {

        m_Log.debug("DefinicionProcedimientosDAO --> Inicio obtenerCamposDesplegables");

        // Obtenemos la conexion.
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            // Preparamos la consulta.
            StringBuffer queryBuffer = new StringBuffer();
            queryBuffer.append("SELECT E_DES.").append(des_cod).append(", ");
            queryBuffer.append("E_DES.").append(des_nom).append(", ").append(des_val_cod).append(", ");
            queryBuffer.append("E_DES_VAL.").append(des_nom);
            queryBuffer.append(" FROM E_PCA, E_DES, E_DES_VAL");
            queryBuffer.append(" WHERE ").append(pca_plt).append(" = 6 AND ").append(pca_tda).append(" = 6 AND ");
            queryBuffer.append(pca_pro).append(" = ? AND ").append(pca_desplegable).append(" = E_DES.").append(des_cod);
            queryBuffer.append(" AND E_DES.").append(des_cod).append(" = E_DES_VAL.").append(des_cod);
            queryBuffer.append(" UNION ");
            queryBuffer.append("SELECT E_DES.").append(des_cod).append(", ");
            queryBuffer.append("E_DES.").append(des_nom).append(", ").append(des_val_cod).append(", ");
            queryBuffer.append("E_DES_VAL.").append(des_nom);
            queryBuffer.append(" FROM E_TCA, E_DES, E_DES_VAL");
            queryBuffer.append(" WHERE ").append(tca_plt).append(" = 6 AND ").append(tca_tda).append(" = 6 AND ");
            queryBuffer.append(tca_pro).append(" = ? AND ").append(tca_desplegable).append(" = E_DES.").append(des_cod);
            queryBuffer.append(" AND E_DES.").append(des_cod).append(" = E_DES_VAL.").append(des_cod);

            String queryString = new String(queryBuffer);
            m_Log.debug("CONSULTA SQL:");
            m_Log.debug(queryString);

            // Rellenamos la consulta parametrizada.
            ps = con.prepareStatement(queryString);
            ps.setString(1, codigoProc);
            ps.setString(2, codigoProc);

            // Ejecutamos la consulta.
            rs = ps.executeQuery();

            Vector infoCampoDesplegable = new Vector();
            while (rs.next()) {
                int i = 1;
                infoCampoDesplegable.add(rs.getString(i++)); // Codigo de desplegable.
                infoCampoDesplegable.add(rs.getString(i++)); // Nombre del desplegable.
                infoCampoDesplegable.add(rs.getString(i++)); // Codigo de opcion.
                infoCampoDesplegable.add(rs.getString(i)); // Texto de opcion.
            }

            // Cerrar la conexion.
            rs.close();
            ps.close();
            abd.devolverConexion(con);

            m_Log.debug("DefinicionProcedimientosDAO --> Inicio obtenerCamposDesplegables");
            return infoCampoDesplegable;

        } catch (BDException e) {
            e.printStackTrace();
            // Cerrar la conexion.
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            try {
                abd.devolverConexion(con);
            } catch (BDException e1) {
                e1.printStackTrace();
                throw new SQLException(e1.getMensaje());
            }
            throw new SQLException(e.getMensaje());
        }

    }
    
    public DefinicionProcedimientosValueObject getOpcionesInicioProcedimiento(String codProcedimiento, Connection con, AdaptadorSQLBD oad, UsuarioValueObject uVO) throws TramitacionException, TechnicalException {
                
        String sql = "SELECT DISTINCT PRO_MUN, PML_COD, PML_VALOR " + 
                "FROM E_PRO JOIN E_PML ON (PRO_MUN = PML_MUN AND PRO_COD = PML_COD) " +
                "WHERE PML_CMP = 'NOM' " + 
                "AND PML_LENG = "+idiomaDefecto+
                "AND PML_COD = ?";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            
            DefinicionProcedimientosValueObject dpVO = null;
            if (rs. next()) {
                int i = 1;
                dpVO = new DefinicionProcedimientosValueObject();
                dpVO.setCodMunicipio(rs.getString(i++));
                dpVO.setTxtCodigo(rs.getString(i++));
                dpVO.setTxtDescripcion(rs.getString(i++));
            }
            
            Vector l = TramitacionDAO.getInstance().getListaUORs_usuario(oad,con,dpVO,uVO);
            dpVO.setTablaUnidadInicio(l);
            
            // Obtener la lista de unidades orgánicas del trámite de inicio.
            Vector<UORDTO> uorsTramiteInicio = 
                    TramitacionDAO.getInstance().getUnidadesTramitadorasTramiteInicio(dpVO.getTxtCodigo(), con, oad);
            dpVO.setUorsTramiteInicio(uorsTramiteInicio);    
            
            return dpVO;
            
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }


    /**
     * Recupera los plazos de finalización
     * @param definicion: DefinicionProcedimientosValueObject
     * @return
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     * @throws es.altia.common.exception.TechnicalException
     */
    public DefinicionProcedimientosValueObject getPlazosFinalizacion(DefinicionProcedimientosValueObject definicion,Connection con)  throws TramitacionException, TechnicalException{

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT PRO_PORCENTAJE,PRO_UND,PRO_PLZ " +
                          "FROM E_PRO WHERE PRO_COD=?";
                          
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, definicion.getTxtCodigo());
            rs = ps.executeQuery();
            m_Log.debug(sql);
            
            if (rs. next()) {                
                
                definicion.setPorcentaje(rs.getString("PRO_PORCENTAJE"));
                definicion.setPlazo(rs.getString("PRO_PLZ"));
                definicion.setTipoPlazo(rs.getString("PRO_UND"));
            }
            
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return definicion;
    }


    /**
     * Recupera la descripción de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return String
     * @throws java.sql.SQLException si ocurre un error durante el acceso a la BBDD
     */
    public String getDescripcionProcedimiento(String codProcedimiento,Connection con) throws TechnicalException{
         PreparedStatement ps = null;
        ResultSet rs = null;
        String descripcion = null;

        String sql = "SELECT PRO_DES FROM E_PRO " +
                          "WHERE PRO_COD=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            m_Log.debug(sql);

            while(rs. next()) {
                descripcion =  rs.getString("PRO_DES");
            }

        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return descripcion;
    }
    
    /**
     * Recupera la descripción de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return String
     * @throws java.sql.SQLException si ocurre un error durante el acceso a la BBDD
     */
    public String getDescripcionMultiIdiProcedimiento(String codProcedimiento,Connection con) throws TechnicalException{
         PreparedStatement ps = null;
        ResultSet rs = null;
        String descripcion = null;

        String sql = "SELECT PML_VALOR FROM E_PML " +
                          "WHERE PML_COD = ? AND PML_CMP = 'NOM' AND PML_LENG = " + idiomaDefecto;

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            m_Log.debug(sql);

            while(rs. next()) {
                descripcion =  rs.getString("PML_VALOR");
            }

        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return descripcion;
    }


    /**
     * Recupera el código de municipio de un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return String con el código del municipio
     * @throws java.sql.SQLException si ocurre un error durante el acceso a la BBDD
     */
    public String getCodigoMunicipioProcedimiento(String codProcedimiento,Connection con) throws TechnicalException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String codMunicipio = "";

        String sql = "SELECT PRO_MUN FROM E_PRO " +
                          "WHERE PRO_COD=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            m_Log.debug(sql);

            while(rs. next()) {
                codMunicipio =  rs.getString("PRO_MUN");
            }

        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return codMunicipio;
    }




    public ArrayList<PermisoProcedimientosRestringidosVO> getProcedimientosRestringidos(String codOrganizacion,String codEntidad,Connection con){
         ArrayList<PermisoProcedimientosRestringidosVO> procs = new  ArrayList<PermisoProcedimientosRestringidosVO>();
         Statement st = null;
         ResultSet rs = null;

         String sql = "SELECT PRO_COD,PRO_DES FROM E_PRO " +
                          "WHERE PRO_RESTRINGIDO=1";

        try {
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                PermisoProcedimientosRestringidosVO proc = new PermisoProcedimientosRestringidosVO();
                String codProcedimiento = rs.getString("PRO_COD");
                String descProcedimiento = rs.getString("PRO_DES");
                proc.setCodProcedimiento(codProcedimiento);
                proc.setDescProcedimiento(descProcedimiento);
                proc.setCodMunicipio(codOrganizacion);
                proc.setCodEntidad(codEntidad);
                procs.add(proc);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return procs;

    }
 




    /**
     * Recupera la lista de procedimientos necesarios para rellenar el combo de la bandeja de expedientes pendientes. Tiene en cuenta que si
     * el procedimiento está restringido, se comprueba si el usuario tiene permiso para visualizarlo y trabajar con él
     * @param codUsuario: Código del usuario
     * @param con: Conexión a la BBDD
     * @return Vector    
     */
    public Vector getListaProcedimientosFiltroBandejaPendientes(UsuarioValueObject usuario, Connection con)
    {
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaProcedimientosFiltroBandejaPendientes");
        PreparedStatement ps = null;
        ResultSet rs = null;        
        String sql = "";
        Vector salida = new Vector();
        
        try{
                   sql = "SELECT PRO_COD, PRO_DES "
                    + " FROM E_PRO "
                    + " WHERE PRO_LIBRERIA <> 1 " 
                    + " AND PRO_FLD <=  ? " 
                    + " AND (PRO_FLH IS NULL "
                                + " OR ? < PRO_FLH "
                                + " OR PRO_COD IN (SELECT DISTINCT EXP_PRO " 
                                                                + " FROM E_EXP WHERE EXP_FEF IS NULL)) "
                    + " AND PRO_EST =1"
                    + " AND (EXISTS (SELECT DISTINCT UOU_UOR " 
                                            + " FROM " + GlobalNames.ESQUEMA_GENERICO+"A_UOU, E_PUI "
                                            + " WHERE UOU_USU = ? " 
                                            + " AND UOU_ORG = ? " 
                                            + " AND UOU_ENT = ? " 
                                            + " AND UOU_UOR = PUI_COD " 
                                            + " AND PUI_MUN = PRO_MUN "
                                            + " AND PUI_PRO = PRO_COD) "
                                + " OR (NOT EXISTS (SELECT DISTINCT PUI_COD " 
                                                            + " FROM E_PUI "
                                                            + " WHERE PUI_PRO = PRO_COD" 
                                                                + " AND PUI_MUN = PRO_MUN) "
                                        + " AND EXISTS (SELECT DISTINCT UOU_UOR " 
                                                                + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU "
                                                                + " WHERE UOU_USU = ? " 
                                                                + " AND UOU_ORG = ? " 
                                                                + " AND UOU_ENT = ?))) "
                    + " AND (E_PRO.PRO_RESTRINGIDO = 0 " 
                                + " OR (E_PRO.PRO_RESTRINGIDO = 1 " 
                                        + " AND E_PRO.PRO_COD IN (SELECT DISTINCT PRO_COD " 
                                                                                    + " FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO " 
                                                                                    + " WHERE USU_COD= ? " 
                                                                                    + " AND ORG_COD=E_PRO.PRO_MUN)))"
                           + "UNION "
                                +" SELECT DISTINCT PRO_COD, PRO_DES "
                                + "FROM E_PRO, E_TRA_UTR"
                                + " WHERE PRO_COD=TRA_PRO AND PRO_MUN=TRA_MUN AND "
                                + "(TRA_UTR_COD IN "
                                        + "(SELECT UOR_COD "
                                        + "FROM A_UOR WHERE UOR_COD IN "
                                                + "(SELECT UOU_UOR "
                                                + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU "
                                                + " WHERE UOU_USU = ?"
                                                + " AND UOU_ORG=?"
                                                + " AND UOU_ENT= ? )))"
                                        + " AND PRO_LIBRERIA <> 1"
                                        + " AND PRO_FLD <= ?"
                                        +  " AND (PRO_FLH IS NULL "
                                                + " OR ? < PRO_FLH)"
                                        + " AND PRO_EST=1"
                           + " UNION "
                               + " SELECT DISTINCT PRO_COD, PRO_DES FROM E_PRO, E_TRA"
                               + " WHERE TRA_UIN IN"
                                        + " (SELECT UOR_COD "
                                        + "FROM A_UOR WHERE UOR_COD IN "
                                                + " (SELECT UOU_UOR"
                                                + "  FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU "
                                                + " WHERE UOU_USU=? "
                                                + " AND UOU_ORG=? "
                                                + " AND UOU_ENT=?))"
                                         + " AND PRO_COD=TRA_PRO AND PRO_MUN=TRA_MUN"
                                         + " AND PRO_LIBRERIA <> 1"
                                         + " AND PRO_FLD <= ?"
                                         + " AND (PRO_FLH IS NULL "
                                                 + " OR ? < PRO_FLH)"
                                        + " AND PRO_EST=1"; 
            

            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            int i = 1;

            Date agora = new Date(Calendar.getInstance().getTimeInMillis());
            ps.setDate(i++, agora);
            ps.setDate(i++, agora);
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setDate(i++, agora);
            ps.setDate(i++, agora);
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setDate(i++, agora);
            ps.setDate(i++, agora);
            rs = ps.executeQuery();


            while ( rs.next() ) {            
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();                
                dPVO.setTxtCodigo(rs.getString("PRO_COD"));
                dPVO.setTxtDescripcion(rs.getString("PRO_DES"));
                salida.add(dPVO);

            }

        } catch (SQLException e) {
            e.printStackTrace();            
        }
        finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            } catch(SQLException sqle) {
                sqle.printStackTrace();                
            }
        }
        
        return salida;
    }


  /**
     * Comprueba si un procedimiento está restringido
     * el procedimiento está restringido, se comprueba si el usuario tiene permiso para visualizarlo y trabajar con él
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean estaProcedimientoRestringido(String codProcedimiento, Connection con) throws TechnicalException
    {
        Statement st = null;
        ResultSet rs = null;        
        boolean exito = false;
              
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_PRO WHERE PRO_COD='" + codProcedimiento.trim() + "' AND PRO_RESTRINGIDO=1";
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;
            while (rs.next()) {
                num = rs.getInt("NUM");
                if(num>=1) exito = true;
            }// while
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return exito;
    }




 /**
     * Recupera el código del área de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return String con el código del área indicado en la definición del procedimiento
     * @throws java.sql.SQLException si ocurre un error durante el acceso a la BBDD
     */
    public String getCodigoAreaProcedimiento(String codProcedimiento,Connection con) throws TechnicalException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String codArea = "";

        String sql = "SELECT PRO_ARE FROM E_PRO " +
                          "WHERE PRO_COD=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            m_Log.debug(sql);

            while(rs. next()) {
                codArea =  rs.getString("PRO_ARE");
            }

        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return codArea;
    }


    /**
     * Recupera el plugin de finalización no convencional de un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return String con el código del área indicado en la definición del procedimiento
     * @throws java.sql.SQLException si ocurre un error durante el acceso a la BBDD
     */
    public DefinicionProcedimientosValueObject getPluginFinalizacionNoConvencional(String codProcedimiento,Connection con) throws TechnicalException{
        DefinicionProcedimientosValueObject defProcVO = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS FROM E_PRO WHERE PRO_COD=?";
            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();

            while(rs. next()) {
                
                String plugin    =  rs.getString("PLUGIN_ANULACION_NOMBRE");
                String implClass =  rs.getString("PLUGIN_ANULACION_IMPLCLASS");

                m_Log.debug("*** plugin: " + plugin + ", implClass: " + implClass);
                if(StringOperations.stringNoNuloNoVacio(plugin) && StringOperations.stringNoNuloNoVacio(implClass)){
                    defProcVO = new DefinicionProcedimientosValueObject();
                    defProcVO.setCodServicioFinalizacion(plugin);
                    defProcVO.setImplClassServicioFinalizacion(implClass);
                    defProcVO.setTxtCodigo(codProcedimiento);
                }
            }

        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return defProcVO;
    }

    public Integer getCodigoDocumentoInicio(String codOrganizacion, String codPro, String nomDoc, Connection con) throws SQLException{
        if(m_Log.isDebugEnabled()) m_Log.debug("getCodigoDocumentoInicio() : BEGIN");
        Integer codDocumento = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql="";
        try{
            sql = "SELECT DOP_COD,  dpml1.DPML_VALOR AS nombreDocumento, dpml2.DPML_VALOR AS condicion" +
                    " FROM   e_dpml dpml1,  e_dpml dpml2,  E_DOP  WHERE DOP_MUN = " + Integer.valueOf(codOrganizacion) + 
                    " AND DOP_PRO= '" + codPro + "' AND e_dop.DOP_MUN  = dpml1.DPML_MUN" +
                    " AND e_dop.DOP_PRO = dpml1.DPML_PRO AND e_dop.DOP_COD = dpml1.DPML_DOP" +
                    " AND dpml1.DPML_LENG = '1'" +
                    " AND e_dop.DOP_MUN = dpml2.DPML_MUN AND e_dop.DOP_PRO = dpml2.DPML_PRO" +
                    " AND e_dop.DOP_COD = dpml2.DPML_DOP " +
                    " AND dpml2.DPML_LENG = '1' and dpml1.DPML_VALOR = '" + nomDoc + "'" + 
                    " GROUP BY DOP_COD, dpml1.DPML_VALOR, dpml2.DPML_VALOR order by DOP_COD"; 
            
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                codDocumento = rs.getInt("DOP_COD");
            }//if(rs.next())
        }catch(Exception ex){
            m_Log.error("Se ha producido un error buscando el código del documento", ex);
            throw new SQLException();
        }finally{
            if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos los recursos de la BBDD");
            con.close();
            stmt.close();
            rs.close();
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("getCodigoDocumentoInicio() : END");
        return codDocumento;
    }//getCodigoDocumentoInicio
    
    public boolean existeExpedientesProcedimiento(String codProcedimiento,Connection con) throws SQLException{
        if(m_Log.isDebugEnabled()) m_Log.debug("existeExpedientesProcedimiento() : BEGIN");
        Statement ps = null;
        ResultSet rs = null;
        String sql="";
        int numRows = 0;
        
        try{
            sql = "SELECT DISTINCT EXP_MUN, EXP_EJE, EXP_NUM FROM E_EXP" +
                    "  WHERE EXP_PRO = '"+codProcedimiento+"'"; 
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.createStatement();
            rs = ps.executeQuery(sql);
            
            while(rs.next()){
                numRows++;
            }
        }catch(Exception ex){
            m_Log.error("Se ha producido un error buscando expedientes de un procedimiento", ex);
            throw new SQLException();
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos los recursos de la BBDD");
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        if(numRows>0) return true;
        return false;
    }

    public ArrayList<DefinicionProcedimientosValueObject> getProcedimientosBiblioteca(Connection con) throws SQLException {
        if(m_Log.isDebugEnabled()) m_Log.debug("getProcedimientosBiblioteca() : BEGIN");
        Statement ps = null;
        ResultSet rs = null;
        String sql;
        ArrayList<DefinicionProcedimientosValueObject> lista = null;
        
        try{
            sql = "SELECT DISTINCT " +  pro_cod + "," + pml_valor+ " AS nombreProcedimiento "
                    + "FROM E_PRO,E_PML "
                    + "WHERE E_PRO." + pro_mun + "=E_PML." + pml_mun + " AND E_PRO." + pro_cod + "=E_PML." + pml_cod
                    + " AND PRO_LIBRERIA=1 ORDER BY " + pro_cod;
            
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.createStatement();
            rs = ps.executeQuery(sql);
            
            lista = new ArrayList<DefinicionProcedimientosValueObject>();;
            while(rs.next()){
                DefinicionProcedimientosValueObject dPVO = new DefinicionProcedimientosValueObject();
                String codProcedimiento = rs.getString(pro_cod);
                dPVO.setTxtCodigo(codProcedimiento);
                String descProcedimiento = rs.getString("nombreProcedimiento");
                dPVO.setTxtDescripcion(descProcedimiento);
                lista.add(dPVO);
            }
        }catch(Exception ex){
            m_Log.error("Se ha producido un error al obtener los procedimientos que son librerías de flujo", ex);
            throw new SQLException();
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos los recursos de la BBDD");
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return lista;
    }
    
    public DefinicionCampoValueObject cargarEstructuraDatosSuplementariosProcedimiento(Connection con, String codProcedimiento, int codMunicipio) throws ImportacionFlujoBibliotecaException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        int contbd = 1;
        ArrayList<EstructuraCampoAgrupado> listaCampos = new  ArrayList<EstructuraCampoAgrupado>();
        ArrayList<DefinicionAgrupacionCamposValueObject> listaAgrupaciones = new ArrayList<DefinicionAgrupacionCamposValueObject>();
        DefinicionCampoValueObject defCVO = new DefinicionCampoValueObject();
        
        m_Log.info("-------------------------DefinicionProcedimientoDAO: cargarEstructuraDatosSuplementariosProcedimiento::BEGIN" );
        try{           
            //1.Agrupaciones de campos E_PCA_GROUP
            sql = "SELECT PCA_ID_GROUP,PCA_DESC_GROUP,PCA_ORDER_GROUP,PCA_ACTIVE "
                    + "FROM E_PCA_GROUP "
                    + "WHERE PCA_PRO = ?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("DefinicionProcedimientosDAO->cargarEstructuraDatosSuplementariosProcedimiento: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            ps.setString(contbd, codProcedimiento);
            rs = ps.executeQuery();
            while (rs.next()) {
                DefinicionAgrupacionCamposValueObject dacVO = new DefinicionAgrupacionCamposValueObject();
                dacVO.setCodAgrupacion(rs.getString("PCA_ID_GROUP"));
                dacVO.setDescAgrupacion(rs.getString("PCA_DESC_GROUP"));
                dacVO.setOrdenAgrupacion(rs.getInt("PCA_ORDER_GROUP"));
                dacVO.setAgrupacionActiva(rs.getString("PCA_ACTIVE"));

                listaAgrupaciones.add(dacVO);
            }
            defCVO.setListaAgrupaciones(new Vector(listaAgrupaciones));

            rs.close();
            ps.close();

            //2. campos suplementarios E_PCA
            sql = "SELECT PCA_MUN,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,"
                    + "PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y,"
                    + "EXPRESION_CAMPO_NUM_PROC.EXPRESION AS EXPRESION_NUM,EXPRESION_CAMPO_CAL_PROC.EXPRESION AS EXPRESION_CAL "
                     + "FROM e_pca "	
                    + "LEFT JOIN expresion_campo_num_proc ON e_pca.pca_mun = expresion_campo_num_proc.cod_organizacion AND e_pca.pca_pro = expresion_campo_num_proc.cod_procedimiento AND e_pca.pca_cod = expresion_campo_num_proc.cod_campo "	
                    + "LEFT JOIN expresion_campo_cal_proc ON e_pca.pca_mun = expresion_campo_cal_proc.cod_organizacion AND e_pca.pca_pro = expresion_campo_cal_proc.cod_procedimiento AND e_pca.pca_cod = expresion_campo_cal_proc.cod_campo "	
                    + "WHERE PCA_MUN=? AND PCA_PRO=?";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("DefinicionProcedimientosDAO->cargarEstructuraDatosSuplementariosProcedimiento: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            contbd = 1;
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            rs = ps.executeQuery();
            
            while(rs.next()){
                EstructuraCampoAgrupado eC = new EstructuraCampoAgrupado();                
                eC.setCodMunicipio(rs.getString("PCA_MUN"));
                String codCampo = rs.getString(pca_cod);
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(pca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(pca_plt);
                eC.setCodPlantilla(codPlantilla);
                String codTipoDato = rs.getString(pca_tda);
                eC.setCodTipoDato(codTipoDato);
                String tamano = rs.getString(pca_tam);
                eC.setTamano(tamano);
                String mascara = rs.getString(pca_mas);
                eC.setMascara(mascara);
                String obligatorio = rs.getString(pca_obl);
                eC.setObligatorio(obligatorio);
                String numeroOrden = rs.getString(pca_nor);
                eC.setNumeroOrden(numeroOrden);
                String rotulo = rs.getString(pca_rot);
                eC.setRotulo(rotulo);
                String activo = rs.getString(pca_activo);
                eC.setActivo(activo);
                String bloqueado = rs.getString("PCA_BLOQ");                
                eC.setBloqueado(bloqueado);
                //PLAZO_AVISO, PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                eC.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                eC.setCheckPlazoFecha(checkPlazoFecha);
                String agrupacionCampo = rs.getString("PCA_GROUP");
                if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo)){
                    eC.setCodAgrupacion(agrupacionCampo);
                }else{
                    eC.setCodAgrupacion("DEF");
                }
                eC.setOculto(rs.getString("PCA_OCULTO"));
                String posX = rs.getString("PCA_POS_X");
                String posY = rs.getString("PCA_POS_Y");
                eC.setPosX(posX);
                eC.setPosY(posY);
                String desplegable = "";
                try {
                    if (rs.getString(pca_desplegable) != null) {
                        desplegable = rs.getString(pca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                } catch (NullPointerException e){
                }
                eC.setValidacion(rs.getString("EXPRESION_NUM"));
                eC.setOperacion(rs.getString("EXPRESION_CAL"));
                listaCampos.add(eC);
            }
            defCVO.setListaCampos(new Vector(listaCampos));
        }catch(SQLException e){
            e.printStackTrace();
            throw new ImportacionFlujoBibliotecaException(20,e.getMessage());
        }
        finally {
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch (SQLException ex) {
                m_Log.error("Error al liberar recursos de la BBDD");
            }
        }
        return defCVO;
    }
    
    /**
     * 
     * @param codCampo
     * @param codProcedimiento
     * @param con
     * @return
     * @throws SQLException 
     */
    public boolean existeCampoSuplementarioProcedimiento(String codCampo,String codProcedimiento,Connection con) throws ImportacionFlujoBibliotecaException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        int rsCont = 0;
        
        if(m_Log.isDebugEnabled()) m_Log.info("DefinicionProcedimientosDAO -> existeCampoSuplementarioProcedimiento::BEGIN");
        
        try{
            sql = "SELECT PCA_MUN,PCA_PRO,PCA_COD FROM E_PCA" +
                    "  WHERE PCA_PRO = '"+codProcedimiento+"' AND PCA_COD = '"+codCampo+"'"; 
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                rsCont++;
            }
        }catch(Exception ex){
            m_Log.error("Se ha producido un error al buscar en campos suplementarios del procedimiento", ex);
            throw new ImportacionFlujoBibliotecaException(50,ex.getMessage());
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos los recursos de la BBDD");
                if(rs!=null) rs.close();
                if(st!=null) st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return rsCont > 0;
    }
    
    public int altaDefinicionCampoSuplementario(EstructuraCampoAgrupado campo,String codProcedimiento,Connection con) throws ImportacionFlujoBibliotecaException{
        PreparedStatement ps = null, ps2 = null;
        int insertado = 0, insCN = 0, insCC = 0;
        String sql = "";
        
        if(m_Log.isDebugEnabled()) m_Log.info("DefinicionProcedimientosDAO -> altaDefinicionCampoSuplementario::BEGIN");
        
        String mascara = campo.getMascara();
        String activo = campo.getActivo();
        String despl = campo.getDesplegable();
        String oculto = campo.getOculto();
        String bloq = campo.getBloqueado();
        String plazoFecha = campo.getPlazoFecha();
        String periodo = campo.getCheckPlazoFecha();
        String group = campo.getCodAgrupacion();
        String posX = campo.getPosX();
        String posY = campo.getPosY();
                
        
        try{
            
            sql = "INSERT INTO E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,"
                            + "PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,"
                            + "PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y)  "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            int contbd = 1;
            int codMunicipio = Integer.parseInt(campo.getCodMunicipio());
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            String codCampo = campo.getCodCampo();
            ps.setString(contbd++, codCampo);
            ps.setString(contbd++, campo.getDescCampo());
            ps.setInt(contbd++, Integer.parseInt(campo.getCodPlantilla()));
            String tipo = campo.getCodTipoDato();
            ps.setInt(contbd++, Integer.parseInt(tipo));
            ps.setInt(contbd++, Integer.parseInt(campo.getTamano()));
            if (mascara!= null &&!"".equals(mascara)) {
                ps.setString(contbd++, mascara);
            } else ps.setNull(contbd++, java.sql.Types.VARCHAR);
            ps.setInt(contbd++, Integer.parseInt(campo.getObligatorio()));
            ps.setInt(contbd++, Integer.parseInt(campo.getNumeroOrden()));
            ps.setString(contbd++, campo.getRotulo());
            if (activo!= null &&!"".equals(activo)) {
                ps.setString(contbd++, activo);
            } else ps.setNull(contbd++, java.sql.Types.VARCHAR);
            if (despl!= null &&!"".equals(despl)) {
                ps.setString(contbd++, despl);
            } else ps.setNull(contbd++, java.sql.Types.VARCHAR);
            if (oculto!= null &&!"".equals(oculto)) {
                ps.setString(contbd++, oculto);
            } else ps.setNull(contbd++, java.sql.Types.VARCHAR);
            if (bloq!= null &&!"".equals(bloq)) {
                ps.setString(contbd++, bloq);
            } else ps.setNull(contbd++, java.sql.Types.VARCHAR);
            if (plazoFecha!= null &&!"".equals(plazoFecha)) {
                ps.setInt(contbd++, Integer.parseInt(plazoFecha));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            if (periodo!= null &&!"".equals(periodo)) {
                ps.setString(contbd++, periodo);
            } else ps.setNull(contbd++, java.sql.Types.VARCHAR);
            if (group!= null &&!"".equals(group)) {
                ps.setString(contbd++, group);
            } else ps.setNull(contbd++, java.sql.Types.VARCHAR);
            if (posX!= null &&!"".equals(posX)) {
                ps.setInt(contbd++, Integer.parseInt(posX));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            if (posY!= null &&!"".equals(posY)) {
                ps.setInt(contbd++, Integer.parseInt(posY));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            
            insertado = ps.executeUpdate();
            
            if (tipo.equals("1") && campo.getValidacion()!=null && !"".equals(campo.getValidacion())) {            
                sql = "INSERT INTO EXPRESION_CAMPO_NUM_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,EXPRESION) "
                        + "VALUES (?,?,?,?)";
                contbd = 1;
                ps2 = con.prepareStatement(sql);
                ps2.setInt(contbd++, codMunicipio);
                ps2.setString(contbd++, codProcedimiento);
                ps2.setString(contbd++, codCampo);
                ps2.setString(contbd++, campo.getValidacion());
                insCN += ps2.executeUpdate();
                ps2.close();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Si el tipo del campo es numérico, query: " + sql);
                }
            } else if ((tipo.equals("8") || tipo.equals("9")) && campo.getOperacion()!=null && !"".equals(campo.getOperacion())) {
                sql = "INSERT INTO EXPRESION_CAMPO_CAL_PROC (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_CAMPO,TIPO_DATO,EXPRESION) "
                        + "VALUES (?,?,?,?,?)";
                contbd = 1;
                ps2 = con.prepareStatement(sql);
                ps2.setInt(contbd++, codMunicipio);
                ps2.setString(contbd++, codProcedimiento);
                ps2.setString(contbd++, codCampo);
                ps2.setInt(contbd++, Integer.parseInt(tipo));
                ps2.setString(contbd++, campo.getOperacion());
                insCC += ps2.executeUpdate();
                ps2.close();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Si el tipo del campo es calculado, query: " + sql);
                }
            }
            
        }catch(Exception ex){
            m_Log.error("Se ha producido un error al insertar campos suplementarios del procedimiento", ex);
            throw new ImportacionFlujoBibliotecaException(70,ex.getMessage());
        }finally{
            try {
                if(ps!=null){
                    SigpGeneralOperations.closeStatement(ps);
                } 
                if(ps2!=null){
                    SigpGeneralOperations.closeStatement(ps2);
                }
            } catch (TechnicalException ex) {
                m_Log.error("Se ha producido un error al cerrar la conexión a la BBDD");
            }
        }
        
        return insertado;
    }
    
    public boolean existeAgrupacion(String codAgrupacion,String codProcedimiento,Connection con) throws ImportacionFlujoBibliotecaException{
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        int rsCont = 0;
        
        if(m_Log.isDebugEnabled()) m_Log.info("DefinicionProcedimientosDAO -> existeAgrupacion::BEGIN");
        
        try{
            sql = "SELECT PCA_ID_GROUP,PCA_DESC_GROUP,PCA_ORDER_GROUP,PCA_ACTIVE" +
                    " FROM E_PCA_GROUP" +
                    " WHERE PCA_PRO = '"+codProcedimiento+"' AND PCA_ID_GROUP = '"+codAgrupacion+"'"; 
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                rsCont++;
            }
        }catch(Exception ex){
            m_Log.error("Se ha producido un error al buscar en campos suplementarios del procedimiento", ex);
            throw new ImportacionFlujoBibliotecaException(31,ex.getMessage());
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos los recursos de la BBDD");
                if(rs!=null) rs.close();
                if(st!=null) st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return rsCont > 0;
    }
    
    /**
     * 
     * @param con Objeto de conexion a la bbdd
     * @param codProcedimiento String con el codigo de procedimiento biblioteca
     * @param agrupaciones array de agrupaciones del procedimiento biblioteca
     * @return Objeto resultado  con int de estado y array de agrupaciones no insertadas.Valores posibles del estado:
     *  --> 0: insercion correcta
     *  --> 1: excepcion en la insercion
     *  --> 2: alguna agrupaccion no insertada, en este caso en el array devuelto tenemos la lista
     */
    public ResultadoAltaCampoProcedimientoBibliotecaVO altaDefinicionAgrupaciones(Connection con, String codProcedimiento, ArrayList<DefinicionAgrupacionCamposValueObject> agrupaciones) throws ImportacionFlujoBibliotecaException{
        ResultadoAltaCampoProcedimientoBibliotecaVO resultado = new ResultadoAltaCampoProcedimientoBibliotecaVO();
        PreparedStatement ps = null;
        int insertado = 0;
        String sql = "", sqlGen = "";
        ArrayList<DefinicionAgrupacionCamposValueObject> noInsertado = new ArrayList<DefinicionAgrupacionCamposValueObject>();
        
        m_Log.info("DefinicionProcedimientosDAO -> altaDefinicionAgrupaciones::BEGIN");
        
        
        sqlGen="INSERT INTO E_PCA_GROUP (PCA_ID_GROUP,PCA_DESC_GROUP,PCA_ORDER_GROUP,PCA_PRO,PCA_ACTIVE) "+
                "VALUES (?,?,?,?,?)";
        
        try {
            for(DefinicionAgrupacionCamposValueObject defAgVO : agrupaciones){
                if(!this.existeAgrupacion(defAgVO.getCodAgrupacion(), codProcedimiento, con)){
                    sql=sqlGen;                                
                    int contbd = 1;
                    ps = con.prepareStatement(sql);                
                    ps.setString(contbd++, defAgVO.getCodAgrupacion());
                    ps.setString(contbd++, defAgVO.getDescAgrupacion());
                    ps.setInt(contbd++, defAgVO.getOrdenAgrupacion());
                    ps.setString(contbd++, codProcedimiento);
                    ps.setString(contbd++, defAgVO.getAgrupacionActiva());
                    insertado += ps.executeUpdate();
                } else noInsertado.add(defAgVO);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Query de inserción de agrupaciones: " + sqlGen);
                m_Log.debug("Filas insertadas: " + insertado);
            }
            
            if(noInsertado.isEmpty())
                resultado.setEstado(0);
            else{
                resultado.setEstado(2);
            }
            
            
        } catch (SQLException ex) {
            resultado.setEstado(1);
            ex.printStackTrace();
            m_Log.error("Error al grabar datos de campos suplementarios de tramite");
            
        } finally {
            if(ps!=null){
                try {
                    ps.close();
                } catch (SQLException ex) {
                    m_Log.error("Error al liberar recursos de BBDD.");
                }
            }
            resultado.setNoInsertado(noInsertado);
        }
        
       return resultado;
    }

    
    
    
}