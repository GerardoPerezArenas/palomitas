package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO; 
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.FichaRelacionExpedientesManager;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.integracionsw.PeticionSWVO;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.integracionsw.exception.NoServicioWebDefinidoException; 
import es.altia.agora.business.integracionsw.exception.FaltaDatoObligatorioException;
import es.altia.agora.business.integracionsw.procesos.GestorEjecucionTramitacion;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import es.altia.agora.business.sge.SiguienteTramiteTO;
import es.altia.agora.business.sge.firma.dao.FirmaFlujoDAO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramiteClave;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.collections.Pila;
import es.altia.catalogoformularios.util.exceptions.NotAllowActionException;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import es.altia.catalogoformularios.model.solicitudesfacade.actions.FinalizarFormularios;
import es.altia.catalogoformularios.model.solicitudesfacade.actions.ReabrirFormularios;

import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.flexia.portafirmasexternocliente.plugin.PluginPortafirmasExternoCliente;
import es.altia.flexia.portafirmasexternocliente.plugin.lanbide.servicios.LanbideEstadoPortafirmasManager;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.ConexionExterna;
import es.altia.util.evalua_cadena;
import es.altia.util.jdbc.JdbcOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.NumberUtils;


public class TramitacionExpedientesDAO {

    /* Declaracion de servicios */
    protected static Config m_ConfigTechnical; // Fichero de configuracion tecnico
    protected static Config m_ConfigCommon; // Fichero de configuracion
    protected static Log m_Log =
            LogFactory.getLog(TramitacionExpedientesDAO.class.getName());
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    protected static Config m_ConfigError; // Mensajes de error localizados

    // Constantes para controlar el funcionamiento de getFlujoSalida
    private static int FLUJO_LISTA_TRAMITES = 0; // Tramites sin pregunta
    private static int FLUJO_LISTA_TRAMITES_FAVORABLE = 1;  // Tramites con pregunta y respuesta favorable
    private static int FLUJO_LISTA_TRAMITES_NO_FAVORABLE = 2; // Tramites con pregunta y respuesta no favorable

    /* Instancia unica */
    private static TramitacionExpedientesDAO instance = null;

    protected static String idiomaDefecto;
    protected static String des_val_campo;
    protected static String des_val_cod;
    protected static String des_val_desc;
    protected static String des_val_estado;

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
    protected static String tra_ins;
    protected static String tra_prr;

    protected static String pro_mun;
    protected static String pro_cod;
    protected static String pro_utr;
    protected static String pro_loc;

    protected static String tml_mun;
    protected static String tml_pro;
    protected static String tml_tra;
    protected static String tml_cmp;
    protected static String tml_leng;
    protected static String tml_valor;

    protected static String cro_mun;
    protected static String cro_pro;
    protected static String cro_eje;
    protected static String cro_num;
    protected static String cro_tra;
    protected static String cro_fei;
    protected static String cro_fef;
    protected static String cro_utr;
    protected static String cro_usu;
    protected static String cro_fip;
    protected static String cro_fli;
    protected static String cro_ffp;
    protected static String cro_res;
    protected static String cro_obs;
    protected static String cro_ocu;

    protected static String cml_cod;
    protected static String cml_cmp;
    protected static String cml_leng;
    protected static String cml_valor;

    protected static String crd_mun;
    protected static String crd_pro;
    protected static String crd_eje;
    protected static String crd_num;
    protected static String crd_tra;
    protected static String crd_ocu;
    protected static String crd_nud;
    protected static String crd_fal;
    protected static String crd_fmo;
    protected static String crd_usc;
    protected static String crd_usm;
    protected static String crd_fil;
    protected static String crd_des;
    protected static String crd_dot;
    protected static String crd_fir_est;
    protected static String crd_exp_fd;
    protected static String crd_doc_fd;
    protected static String crd_fir_fd;
    protected static String crd_exp;
    protected static String crd_cod_pf_ext;
    protected static String crd_id_pf_ext;

    protected static String usu_cod;
    protected static String usu_nom;

    protected static String dot_mun;
    protected static String dot_pro;
    protected static String dot_tra;
    protected static String dot_cod;
    protected static String dot_plt;
    protected static String dot_activo;

    protected static String dtml_mun;
    protected static String dtml_pro;
    protected static String dtml_tra;
    protected static String dtml_dot;
    protected static String dtml_cmp;
    protected static String dtml_leng;
    protected static String dtml_valor;

    protected static String sal_mun;
    protected static String sal_pro;
    protected static String sal_tra;
    protected static String sal_tac;
    protected static String sal_taa;
    protected static String sal_tan;
    protected static String sal_obl;
    protected static String sal_obld;

    protected static String sml_mun;
    protected static String sml_tra;
    protected static String sml_pro;
    protected static String sml_cmp;
    protected static String sml_leng;
    protected static String sml_valor;

    protected static String fls_mun;
    protected static String fls_pro;
    protected static String fls_tra;
    protected static String fls_nuc;
    protected static String fls_nus;
    protected static String fls_cts;

    protected static String dnn_dom;
    protected static String dnn_tvi;
    protected static String dnn_pai;
    protected static String dnn_prv;
    protected static String dnn_mun;
    protected static String dnn_vpa;
    protected static String dnn_vpr;
    protected static String dnn_vmu;
    protected static String dnn_via;
    protected static String dnn_nud;
    protected static String dnn_led;
    protected static String dnn_nuh;
    protected static String dnn_leh;
    protected static String dnn_blq;
    protected static String dnn_por;
    protected static String dnn_esc;
    protected static String dnn_plt;
    protected static String dnn_pta;
    protected static String dnn_dmc;
    protected static String dnn_cpo;
    protected static String dnn_lug;
    protected static String dnn_sit;
    protected static String dnn_fal;
    protected static String dnn_ual;
    protected static String dnn_fbj;
    protected static String dnn_ubj;

    protected static String ent_mun;
    protected static String ent_pro;
    protected static String ent_tra;
    protected static String ent_cod;
    protected static String ent_ctr;
    protected static String ent_est;
    protected static String ent_tipo;
    protected static String ent_exp;

    protected static String trp_mun;
    protected static String trp_eje;
    protected static String trp_exp;
    protected static String trp_pro;
    protected static String trp_tra;
    protected static String trp_ent;
    protected static String trp_ctr;
    protected static String trp_est;
    protected static String trp_fei;
    protected static String trp_fef;
    protected static String trp_uor;

    protected static String exp_mun;
    protected static String exp_eje;
    protected static String exp_num;
    protected static String exp_loc;
    protected static String exp_clo;
    protected static String exp_pro;
    protected static String exp_uor;

    protected static String bloq_mun;
    protected static String bloq_eje;
    protected static String bloq_num;
    protected static String bloq_pro;
    protected static String bloq_tra;
    protected static String bloq_ocu;
    protected static String bloq_usu;

    protected static String uou_uor;
    protected static String uou_usu;
    protected static String uou_org;
    protected static String uou_ent;

        protected static String ten_mun;
    protected static String ten_pro;
    protected static String ten_tra;
    protected static String ten_cod;
    protected static String ten_des;
    protected static String ten_url;
    protected static String ten_est;

    protected static String uor_cod;
    protected static String uor_nom;

    protected static String tca_mun;
    protected static String tca_pro;
    protected static String tca_tra;
    protected static String tca_cod;
    protected static String tca_des;
    protected static String tca_plt;
    protected static String tca_tda;
    protected static String tca_tam;
    protected static String tca_mas;
    protected static String tca_obl;
    protected static String tca_nor;
    protected static String tca_rot;
    protected static String tca_vis;
    protected static String tca_activo;
    protected static String tca_desplegable;

    protected static String plt_cod;
    protected static String plt_des;
    protected static String plt_url;

    protected static String aplt_cod;
    protected static String plt_int;
    protected static String plt_rel;
    protected static String plt_pro;
    protected static String plt_tra;

    protected static String ext_mun;
    protected static String ext_eje;
    protected static String ext_num;
    protected static String ext_ter;
    protected static String ext_nvr;
    protected static String ext_rol;

    protected static String rol_mun;
    protected static String rol_pro;
    protected static String rol_cod;
    protected static String rol_des;
    protected static String rol_pde;

    protected static String hte_ter;
    protected static String hte_nvr;
    protected static String hte_nom;
    protected static String hte_ap1;
    protected static String hte_pa1;
    protected static String hte_ap2;
    protected static String hte_pa2;

    protected static String exp_rel_mun;
    protected static String exp_rel_pro;
    protected static String exp_rel_eje;
    protected static String exp_rel_num;
    protected static String exp_exp_mun;
    protected static String exp_exp_pro;
    protected static String exp_exp_eje;
    protected static String exp_exp_num;

    /**
     * Construye un nuevo TramitacionExpedientesDAO.
     * Es protected, por lo que la unica manera de instanciar esta clase
     * es usando el factory method <code>getInstance</code>
     */
    protected TramitacionExpedientesDAO() {
        super();
        m_ConfigCommon = ConfigServiceHelper.getConfig("common");
        // Fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

        // Mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
        des_val_campo = m_ConfigTechnical.getString("SQL.E_DES_VAL.campoValor");
        des_val_cod = m_ConfigTechnical.getString("SQL.E_DES_VAL.codigoValor");
        des_val_desc = m_ConfigTechnical.getString("SQL.E_DES_VAL.nombreValor");
        des_val_estado = m_ConfigTechnical.getString("SQL.E_DES_VAL.estadoValor");

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
        tra_ins = m_ConfigTechnical.getString("SQL.E_TRA.instrucciones");
        tra_prr = m_ConfigTechnical.getString("SQL.E_TRA.expRel");

        pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");
        pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
        pro_utr = m_ConfigTechnical.getString("SQL.E_PRO.unidad");
        pro_loc = m_ConfigTechnical.getString("SQL.E_PRO.poseeLocalizacion");

        tml_mun = m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
        tml_pro = m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
        tml_tra = m_ConfigTechnical.getString("SQL.E_TML.codTramite");
        tml_cmp = m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
        tml_leng = m_ConfigTechnical.getString("SQL.E_TML.idioma");
        tml_valor = m_ConfigTechnical.getString("SQL.E_TML.valor");

        cro_mun = m_ConfigTechnical.getString("SQL.E_CRO.codMunicipio");
        cro_pro = m_ConfigTechnical.getString("SQL.E_CRO.codProcedimiento");
        cro_eje = m_ConfigTechnical.getString("SQL.E_CRO.ano");
        cro_num = m_ConfigTechnical.getString("SQL.E_CRO.numero");
        cro_tra = m_ConfigTechnical.getString("SQL.E_CRO.codTramite");
        cro_fei = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicio");
        cro_fef = m_ConfigTechnical.getString("SQL.E_CRO.fechaFin");
        cro_utr = m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora");
        cro_usu = m_ConfigTechnical.getString("SQL.E_CRO.codUsuario");
        cro_fip = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicioPlazo");
        cro_fli = m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite");
        cro_ffp = m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo");
        cro_res = m_ConfigTechnical.getString("SQL.E_CRO.resolucion");
        cro_obs = m_ConfigTechnical.getString("SQL.E_CRO.observaciones");
        cro_ocu = m_ConfigTechnical.getString("SQL.E_CRO.ocurrencia");

        cml_cod = m_ConfigTechnical.getString("SQL.A_CML.codTramite");
        cml_cmp = m_ConfigTechnical.getString("SQL.A_CML.codCampoML");
        cml_leng = m_ConfigTechnical.getString("SQL.A_CML.idioma");
        cml_valor = m_ConfigTechnical.getString("SQL.A_CML.valor");

        crd_mun = m_ConfigTechnical.getString("SQL.E_CRD.codMunicipio");
        crd_pro = m_ConfigTechnical.getString("SQL.E_CRD.codProcedimiento");
        crd_eje = m_ConfigTechnical.getString("SQL.E_CRD.ejercicio");
        crd_num = m_ConfigTechnical.getString("SQL.E_CRD.numeroExpediente");
        crd_tra = m_ConfigTechnical.getString("SQL.E_CRD.codTramite");
        crd_ocu = m_ConfigTechnical.getString("SQL.E_CRD.ocurrencia");
        crd_nud = m_ConfigTechnical.getString("SQL.E_CRD.numeroDocumento");
        crd_fal = m_ConfigTechnical.getString("SQL.E_CRD.fechaAlta");
        crd_fmo = m_ConfigTechnical.getString("SQL.E_CRD.fechaModificacion");
        crd_usc = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioCreac");
        crd_usm = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioModif");
        crd_fil = m_ConfigTechnical.getString("SQL.E_CRD.fichero");
        crd_des = m_ConfigTechnical.getString("SQL.E_CRD.descripcion");
        crd_dot = m_ConfigTechnical.getString("SQL.E_CRD.codDocumento");
        crd_fir_est = m_ConfigTechnical.getString("SQL.E_CRD.estadoFirma");
        crd_exp_fd = m_ConfigTechnical.getString("SQL.E_CRD.expedienteFirmaDoc");
        crd_doc_fd = m_ConfigTechnical.getString("SQL.E_CRD.documentoFirmaDoc");
        crd_fir_fd = m_ConfigTechnical.getString("SQL.E_CRD.firmaFirmaDoc");
        crd_exp = m_ConfigTechnical.getString("SQL.E_CRD.expedientes");

        usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
        usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");

        dot_mun = m_ConfigTechnical.getString("SQL.E_DOT.codMunicipio");
        dot_pro = m_ConfigTechnical.getString("SQL.E_DOT.codProcedimiento");
        dot_tra = m_ConfigTechnical.getString("SQL.E_DOT.codTramite");
        dot_cod = m_ConfigTechnical.getString("SQL.E_DOT.codDocumento");
        dot_plt = m_ConfigTechnical.getString("SQL.E_DOT.codPlantilla");
        dot_activo = m_ConfigTechnical.getString("SQL.E_DOT.activo");

        sal_mun = m_ConfigTechnical.getString("SQL.E_SAL.codMunicipio");
        sal_pro = m_ConfigTechnical.getString("SQL.E_SAL.codProcedimiento");
        sal_tra = m_ConfigTechnical.getString("SQL.E_SAL.codTramite");
        sal_tac = m_ConfigTechnical.getString("SQL.E_SAL.tipoAccion");
        sal_taa = m_ConfigTechnical.getString("SQL.E_SAL.tipoAccionAfirm");
        sal_tan = m_ConfigTechnical.getString("SQL.E_SAL.tipoAccionNeg");
        sal_obl = m_ConfigTechnical.getString("SQL.E_SAL.tramitesObligatorios");
        sal_obld = m_ConfigTechnical.getString("SQL.E_SAL.tramitesObligatoriosD");

        sml_mun = m_ConfigTechnical.getString("SQL.E_SML.codMunicipio");
        sml_tra = m_ConfigTechnical.getString("SQL.E_SML.codTramite");
        sml_pro = m_ConfigTechnical.getString("SQL.E_SML.codProcedimiento");
        sml_cmp = m_ConfigTechnical.getString("SQL.E_SML.codCampoML");
        sml_leng = m_ConfigTechnical.getString("SQL.E_SML.idioma");
        sml_valor = m_ConfigTechnical.getString("SQL.E_SML.valor");

        fls_mun = m_ConfigTechnical.getString("SQL.E_FLS.codMunicipio");
        fls_pro = m_ConfigTechnical.getString("SQL.E_FLS.codProcedimiento");
        fls_tra = m_ConfigTechnical.getString("SQL.E_FLS.codTramite");
        fls_nuc = m_ConfigTechnical.getString("SQL.E_FLS.numeroCondicion");
        fls_nus = m_ConfigTechnical.getString("SQL.E_FLS.numeroSecuencia");
        fls_cts = m_ConfigTechnical.getString("SQL.E_FLS.codTramiteSiguiente");

        dnn_dom = m_ConfigTechnical.getString("SQL.T_DNN.idDomicilio");
        dnn_tvi = m_ConfigTechnical.getString("SQL.T_DNN.idTipoVia");
        dnn_pai = m_ConfigTechnical.getString("SQL.T_DNN.idPaisD");
        dnn_prv = m_ConfigTechnical.getString("SQL.T_DNN.idProvinciaD");
        dnn_mun = m_ConfigTechnical.getString("SQL.T_DNN.idMunicipioD");
        dnn_vpa = m_ConfigTechnical.getString("SQL.T_DNN.idPaisDVia");
        dnn_vpr = m_ConfigTechnical.getString("SQL.T_DNN.idProvinciaDVia");
        dnn_vmu = m_ConfigTechnical.getString("SQL.T_DNN.idMunicipioDVia");
        dnn_via = m_ConfigTechnical.getString("SQL.T_DNN.codigoVia");
        dnn_nud = m_ConfigTechnical.getString("SQL.T_DNN.numDesde");
        dnn_led = m_ConfigTechnical.getString("SQL.T_DNN.letraDesde");
        dnn_nuh = m_ConfigTechnical.getString("SQL.T_DNN.numHasta");
        dnn_leh = m_ConfigTechnical.getString("SQL.T_DNN.letraHasta");
        dnn_blq = m_ConfigTechnical.getString("SQL.T_DNN.bloque");
        dnn_por = m_ConfigTechnical.getString("SQL.T_DNN.portal");
        dnn_esc = m_ConfigTechnical.getString("SQL.T_DNN.escalera");
        dnn_plt = m_ConfigTechnical.getString("SQL.T_DNN.planta");
        dnn_pta = m_ConfigTechnical.getString("SQL.T_DNN.puerta");
        dnn_dmc = m_ConfigTechnical.getString("SQL.T_DNN.domicilio");
        dnn_cpo = m_ConfigTechnical.getString("SQL.T_DNN.codigoPostal");
        dnn_lug = m_ConfigTechnical.getString("SQL.T_DNN.barriada");
        dnn_sit = m_ConfigTechnical.getString("SQL.T_DNN.situacion");
        dnn_fal = m_ConfigTechnical.getString("SQL.T_DNN.fechaAlta");
        dnn_ual = m_ConfigTechnical.getString("SQL.T_DNN.usuarioAlta");
        dnn_fbj = m_ConfigTechnical.getString("SQL.T_DNN.fechaBaja");
        dnn_ubj = m_ConfigTechnical.getString("SQL.T_DNN.usuarioBaja");

        ent_mun= m_ConfigTechnical.getString("SQL.E_ENT.codMunicipio");
        ent_pro= m_ConfigTechnical.getString("SQL.E_ENT.codProcedimiento");
        ent_tra= m_ConfigTechnical.getString("SQL.E_ENT.codTramite");
        ent_cod= m_ConfigTechnical.getString("SQL.E_ENT.codCondicion");
        ent_ctr= m_ConfigTechnical.getString("SQL.E_ENT.codTramiteCond");
        ent_est= m_ConfigTechnical.getString("SQL.E_ENT.estadoTramiteCond");
        ent_tipo= m_ConfigTechnical.getString("SQL.E_ENT.tipoCond");
        ent_exp= m_ConfigTechnical.getString("SQL.E_ENT.expresionCond");

       

        exp_mun= m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        exp_eje= m_ConfigTechnical.getString("SQL.E_EXP.ano");
        exp_num= m_ConfigTechnical.getString("SQL.E_EXP.numero");
        exp_loc= m_ConfigTechnical.getString("SQL.E_EXP.localizacion");
        exp_clo= m_ConfigTechnical.getString("SQL.E_EXP.codLocalizacion");
        exp_pro= m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
        exp_uor= m_ConfigTechnical.getString("SQL.E_EXP.uor");

        bloq_mun= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codMunicipio");
        bloq_eje= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ano");
        bloq_num= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.numero");
        bloq_pro= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codProcedimiento");
        bloq_tra= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.tramite");
        bloq_ocu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ocurrencia");
        bloq_usu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.usuario");


        uou_uor= m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        uou_usu= m_ConfigTechnical.getString("SQL.A_UOU.usuario");
        uou_org= m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
        uou_ent= m_ConfigTechnical.getString("SQL.A_UOU.entidad");

        ten_mun= m_ConfigTechnical.getString("SQL.E_TEN.codMunicipio");
        ten_pro= m_ConfigTechnical.getString("SQL.E_TEN.codProcedimiento");
        ten_tra= m_ConfigTechnical.getString("SQL.E_TEN.codTramite");
        ten_cod= m_ConfigTechnical.getString("SQL.E_TEN.codEnlace");
        ten_des= m_ConfigTechnical.getString("SQL.E_TEN.descripcion");
        ten_url= m_ConfigTechnical.getString("SQL.E_TEN.url");
        ten_est= m_ConfigTechnical.getString("SQL.E_TEN.estado");

        uor_cod= m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        uor_nom= m_ConfigTechnical.getString("SQL.A_UOR.nombre");

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

        plt_cod = m_ConfigTechnical.getString("SQL.E_PLT.codPlantilla");
        plt_des = m_ConfigTechnical.getString("SQL.E_PLT.descripcion");
        plt_url = m_ConfigTechnical.getString("SQL.E_PLT.url");

        aplt_cod = m_ConfigTechnical.getString("SQL.A_PLT.codigo");
        plt_int = m_ConfigTechnical.getString("SQL.A_PLT.interesado");
        plt_rel = m_ConfigTechnical.getString("SQL.A_PLT.relacion");
        plt_pro = m_ConfigTechnical.getString("SQL.A_PLT.procedimiento");
        plt_tra = m_ConfigTechnical.getString("SQL.A_PLT.tramite");

        ext_mun = m_ConfigTechnical.getString("SQL.E_EXT.codMunicipio");
        ext_eje = m_ConfigTechnical.getString("SQL.E_EXT.ano");
        ext_num = m_ConfigTechnical.getString("SQL.E_EXT.numero");
        ext_ter = m_ConfigTechnical.getString("SQL.E_EXT.codTercero");
        ext_nvr = m_ConfigTechnical.getString("SQL.E_EXT.verTercero");
        ext_rol = m_ConfigTechnical.getString("SQL.E_EXT.rolTercero");

        rol_mun = m_ConfigTechnical.getString("SQL.E_ROL.codMunicipio");
        rol_pro = m_ConfigTechnical.getString("SQL.E_ROL.codProcedimiento");
        rol_cod = m_ConfigTechnical.getString("SQL.E_ROL.codRol");
        rol_des = m_ConfigTechnical.getString("SQL.E_ROL.descripcion");
        rol_pde = m_ConfigTechnical.getString("SQL.E_ROL.porDefecto");

        hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
        hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");
        hte_nom = m_ConfigTechnical.getString("SQL.T_HTE.nombre");
        hte_ap1 = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
        hte_pa1 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido1");
        hte_ap2 = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
        hte_pa2 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido2");

        exp_rel_mun = m_ConfigTechnical.getString("SQL.G_EXP.codMunicipioR");
        exp_rel_pro = m_ConfigTechnical.getString("SQL.G_EXP.codProcedimientoR");
        exp_rel_eje = m_ConfigTechnical.getString("SQL.G_EXP.ejercicioR");
        exp_rel_num = m_ConfigTechnical.getString("SQL.G_EXP.numeroR");
        exp_exp_mun = m_ConfigTechnical.getString("SQL.G_EXP.codMunicipio");
        exp_exp_pro = m_ConfigTechnical.getString("SQL.G_EXP.codProcedimiento");
        exp_exp_eje = m_ConfigTechnical.getString("SQL.G_EXP.ejercicio");
        exp_exp_num = m_ConfigTechnical.getString("SQL.G_EXP.numero");
    }

    public static TramitacionExpedientesDAO getInstance() {
            // Sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(TramitacionExpedientesDAO.class) {
                if (instance == null)
                    instance = new TramitacionExpedientesDAO();
            }
        return instance;
    }

    public TramitacionExpedientesValueObject cargarDatos(TramitacionExpedientesValueObject tEVO,String[] params)
            throws AnotacionRegistroException,TechnicalException, SQLException {

        //Comprobamos en caso de que exista un portafirmas externo si los documentos de tramitacion estan firmados y 
        //Recuperamos la propiedad que nos indica si existe un portafirmas externo
        if(m_Log.isDebugEnabled()) m_Log.debug("Comprobamos si existe un portafirmas externo");
        Boolean existePortafirmasExterno = 
                PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(tEVO.getCodOrganizacion());
        if(m_Log.isDebugEnabled()) m_Log.debug("existe un portafirmas externo = " + existePortafirmasExterno);

        if(existePortafirmasExterno){
            PluginPortafirmasExternoCliente pluginPortafirmasExterno =
                    PluginPortafirmasExternoClienteFactoria.getImplClass(tEVO.getCodMunicipio());
            
            String resultado = pluginPortafirmasExterno.actualizarFirmasDocumentosTramitacion(tEVO, params);
        }//if(existePortafirmasExterno)
        
        m_Log.debug("TramitacionExpedientesDAO.cargarDatos");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector<TramitacionExpedientesValueObject> listaDocumentos = new Vector<TramitacionExpedientesValueObject>();
        Vector listaCodDocumentosTramite = new Vector();
        Vector<SiguienteTramiteTO> listaTramitesFavorables = new Vector<SiguienteTramiteTO>();
        Vector<SiguienteTramiteTO> listaTramitesNoFavorables = new Vector<SiguienteTramiteTO>();
        Vector listaEnlaces = new Vector();
        UsuarioDAO userDAO = UsuarioDAO.getInstance();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            

            // PESTAÑA DE DATOS GENERALES

            from = tml_valor + "," + oad.convertir(cro_fei, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + cro_fei + ", " + oad.convertir(cro_fef, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + cro_fef + "," + tra_uin + "," + cro_utr + "," + tra_plz + "," + tra_ins +
                    "," + tra_und + "," + oad.convertir(cro_fip, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + cro_fip +
                    "," + oad.convertir(cro_fli, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+ " AS " + cro_fli + "," +
                    oad.convertir(cro_ffp, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " + cro_ffp + "," +
                    cml_valor + "," + cro_obs + ",cro_usf," + tra_prr +", tra_fin, TRA_GENERARPLZ,TRA_NOTIFICACION_ELECTRONICA, TRA_NOTIF_ELECT_OBLIG, TRA_NOTIF_FIRMA_CERT_ORG, PRO_INTOBL ";
            where = tra_mun + "=? AND " + tra_pro + "=? AND " + tra_cod + "=? AND " + cro_num + "=? AND " + cro_eje + "=? AND " +
                    cro_ocu + "=?" ;
            String[] join = new String[14];
            join[0] = "E_TRA";
            join[1] = "INNER";
            join[2] = "e_cro";
            join[3] = "e_tra." + tra_mun + "=e_cro." + cro_mun + " AND " +
                    "e_tra." + tra_pro + "=e_cro." + cro_pro + " AND " +
                    "e_tra." + tra_cod + "=e_cro." + cro_tra;
            join[4] = "INNER";
            join[5] = "e_tml";
            join[6] = "e_tra." + tra_mun + "=e_tml." + tml_mun + " AND " +
                    "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                    "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                    "e_tml." + tml_cmp + "='NOM' AND " +
                    "e_tml." + tml_leng + "='"+idiomaDefecto+"'";
            join[7] = "INNER";
            join[8] = GlobalNames.ESQUEMA_GENERICO + "a_cml a_cml";
            join[9] = "e_tra." + tra_cls + "=a_cml." + cml_cod + " AND " +
                    "a_cml." + cml_cmp + "='NOM' AND " +
                    "a_cml." + cml_leng + "='"+idiomaDefecto+"'";
            join[10] = "INNER";
            join[11] = "e_pro";
            join[12] = "e_tra." + tra_mun + "=e_pro." + pro_mun + " AND " +
                    "e_tra." + tra_pro + "=e_pro." + pro_cod;
            join[13] = "false";
            sql = oad.join(from,where,join);
            if(m_Log.isDebugEnabled()) m_Log.debug("TramitecinExpedientesDAO.cararDatos: " + sql);

            st=con.prepareStatement(sql);
            
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getCodTramite()));
            st.setString(4,  tEVO.getNumeroExpediente());
            st.setInt(5, Integer.parseInt(tEVO.getEjercicio()));
            st.setInt(6, Integer.parseInt( tEVO.getOcurrenciaTramite()));
           
            rs = st.executeQuery();
            
            while (rs.next()) {
                String descTramite = rs.getString(tml_valor);
                tEVO.setTramite(descTramite);
                String fechaInicio = rs.getString(cro_fei);
                tEVO.setFechaInicio(fechaInicio);
                String fechaFin = rs.getString(cro_fef);
                tEVO.setFechaFin(fechaFin);
                String codUnidadInicio = rs.getString(tra_uin);
                tEVO.setCodUnidadInicioTram(codUnidadInicio);
                String codUnidadTramitadoraTram = rs.getString(cro_utr);
                tEVO.setCodUnidadTramitadoraTram(codUnidadTramitadoraTram);
                String plazo = rs.getString(tra_plz);
                tEVO.setPlazo(plazo);
                String unidadPlazo = rs.getString(tra_und);
                tEVO.setTipoPlazo(unidadPlazo);
                String fechaInicioPlazo = rs.getString(cro_fip);
                tEVO.setFechaInicioPlazo(fechaInicioPlazo);
                String fechaLimite = rs.getString(cro_fli);
                tEVO.setFechaLimite(fechaLimite);
                String fechaFinPlazo = rs.getString(cro_ffp);
                tEVO.setFechaFinPlazo(fechaFinPlazo);
                String descClasificacionTramite = rs.getString(cml_valor);
                tEVO.setClasificacionTramite(descClasificacionTramite);
                String observaciones = rs.getString(cro_obs);
                if (observaciones != null) {
                   tEVO.setObservaciones(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.unescapeJava(rs.getString(cro_obs))));
                }
                String instrucciones = rs.getString(tra_ins);
                if (instrucciones != null) {
                    tEVO.setInstrucciones(AdaptadorSQLBD.js_escape(instrucciones));
                }
                String procedimientoAsociado = rs.getString(tra_prr);
                tEVO.setProcedimientoAsociado(procedimientoAsociado);

                // Se establece el código de usuario que finaliza el trámite en el caso
                // de que haya fecha de fin del mismo.
                if (fechaFin != null && fechaFin.length() >= 1) {
                    int codUsuarioFinTramite = rs.getInt("CRO_USF");
                    String nameUserFinTramite = userDAO.getNombreUsuario(params, codUsuarioFinTramite);
                    m_Log.debug("Nombre usuario fin tramite: " + nameUserFinTramite);
                    tEVO.setNombreUsuario(nameUserFinTramite);
                }

                m_Log.debug("Fecha fin del trámite: " + tEVO.getFechaFin());
                m_Log.debug("Nombre usuario finaliza trámite: " + tEVO.getNombreUsuario());

                int plazoCercaFin = rs.getInt("TRA_FIN");
                m_Log.debug("________________CERCA FIN PLAZO ______________");
                m_Log.debug("________________ CERCA FIN PLAZO" + plazoCercaFin);
                m_Log.debug("________________ fecha inicio plazo " + fechaInicioPlazo);
                if (fechaFin == null && fechaLimite != null && !"".equals(fechaLimite) && plazoCercaFin != 0) {
                    tEVO.setPlazoCercaFin(plazoCercaFin);

                } else {
                    tEVO.setPlazoCercaFin(plazoCercaFin);
                }
                tEVO.setBloquearPlazos(rs.getBoolean("TRA_GENERARPLZ"));
                tEVO.setAdmiteNotificacionElectronica(rs.getString("TRA_NOTIFICACION_ELECTRONICA"));
                
                //Inicio cogemos los nuevos valores
                Integer notifObligatoria = rs.getInt("TRA_NOTIF_ELECT_OBLIG");
                tEVO.setNotificacionObligatoria((notifObligatoria!=null)?notifObligatoria.toString():"0");
                
                Integer certificadoOrganismo = rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG");
                tEVO.setCertificadoOrganismo((certificadoOrganismo!=null)?certificadoOrganismo.toString():"0");
                
                String interesadoObligatorio = rs.getString("PRO_INTOBL");
                if(interesadoObligatorio!=null && "1".equals(interesadoObligatorio))
                    tEVO.setInteresadoObligatorio(true);
                else
                    tEVO.setInteresadoObligatorio(false);
                    
            }
            rs.close();
            st.close();

            if(tEVO.getCodUnidadInicioTram() == null || "".equals(tEVO.getCodUnidadInicioTram())) {
                tEVO.setCodUnidadInicioTram("");
                tEVO.setUnidadInicio("");
            } else {
                st.close();	
                UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],tEVO.getCodUnidadInicioTram());		
                if (uorDTO!=null)	
                    tEVO.setUnidadInicio(uorDTO.getUor_nom());
            }
            if(tEVO.getCodUnidadTramitadoraTram() == null || "".equals(tEVO.getCodUnidadTramitadoraTram())) {
                tEVO.setCodUnidadTramitadoraTram("");
                tEVO.setUnidadTramitadora("");
            } else {
                 UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],tEVO.getCodUnidadTramitadoraTram());		
                if (uorDTO!=null)	
                    tEVO.setUnidadTramitadora(uorDTO.getUor_nom());
            }

            // PESTAÑA DE DOCUMENTOS

            from = crd_nud + "," +  oad.convertir("CRD_FINF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FINF,"  + crd_des + "," + oad.convertir(crd_fal, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + crd_fal + "," + oad.convertir(crd_fmo, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS "
                    + crd_fmo + ",usu." + usu_nom + " AS usuarioCreacion,usu1." +
                    usu_nom + " AS usuarioModificacion," + plt_int + ",PLT_EDITOR_TEXTO, crd_id_metadato";

            where = crd_mun + "=? AND " + crd_pro + "=? AND " + crd_tra + "=? AND " + crd_num + "=" +
                    "? AND " + crd_ocu + " =? " ;

            // Comprobamos si esta activado el servicio Web de firmadoc.
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                from += ", " + crd_fir_fd;
                where += "AND " + crd_exp_fd + " IS NOT NULL " +
                        "AND " + crd_doc_fd + " IS NOT NULL ";
            } else {
                from += ", " + crd_fir_est;
                where += "AND " + crd_exp_fd + " IS NULL " +
                        "AND " + crd_doc_fd + " IS NULL " +
                        "AND " + crd_fir_fd + " IS NULL";
            }
            String[] join1 = new String[14];
            join1[0] = "E_CRD";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu";
            join1[3] = "e_crd." + crd_usc + "=usu." + usu_cod;
            join1[4] = "LEFT";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu1";
            join1[6] = "e_crd." + crd_usm + "=usu1." + usu_cod;
            join1[7] = "LEFT";
            join1[8] = "e_dot";
            join1[9] = "e_crd." + crd_pro + "=e_dot." + dot_pro + " AND " +
                    "e_crd." + crd_tra + "=e_dot." + dot_tra + " AND " +
                    "e_crd." + crd_dot + "=e_dot." + dot_cod;
            join1[10] = "LEFT";
            join1[11] = "a_plt";
            join1[12] = "e_dot." + dot_plt + "=a_plt." + plt_cod;
            join1[13] = "false";
            sql = oad.join(from,where,join1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st=con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getCodTramite()));
            st.setString(4, tEVO.getNumeroExpediente());
            st.setInt(5, Integer.parseInt(tEVO.getOcurrenciaTramite()));
             
            rs = st.executeQuery();
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(crd_nud);
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString(crd_des);
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString(crd_fal);
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString(crd_fmo);
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");

                String fechaInforme = rs.getString("CRD_FINF");
                if(fechaInforme!=null)
                    tramExpVO.setFechaInforme(fechaInforme);
                else
                    tramExpVO.setFechaInforme("");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString(plt_int);
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                       
                int codigoMetadato=(rs.getInt("crd_id_metadato"));
                int intExtension = descripcion.lastIndexOf(".");
                String extension = descripcion.substring(intExtension+1, descripcion.length());
                if(m_Log.isDebugEnabled()) m_Log.debug("extension vale " + extension);
                if (codigoMetadato>0 || 
                        (extension != null && !"".equals(extension.trim()) && "PDF".equals(extension.trim().toUpperCase()))){
                     if(m_Log.isDebugEnabled()) m_Log.debug("El documento tiene metadato, el editor pasa a ser NO EDITABLE");
                    tramExpVO.setEditorTexto("");
                }
                
                String estadoFirma;
                if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                    estadoFirma = rs.getString(crd_fir_fd);
                } else {
                    estadoFirma = rs.getString(crd_fir_est);
                }
                tramExpVO.setEstadoFirma(estadoFirma);
                tramExpVO.setRelacion("");
                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }

            //DOCUMENTOS DE LAS RELACIONES A LOS QUE PERTENECEN
            from = "DISTINCT CRD_NUD, CRD_DES, " +
                    oad.convertir("CRD_FAL", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FAL, " +
                    oad.convertir("CRD_FMO", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FMO, " +
                    "USU.USU_NOM AS usuarioCreacion, USU1.USU_NOM AS usuarioModificacion, PLT_INT,PLT_EDITOR_TEXTO,CRD_NUM ";

            where = "CRD_MUN = ? AND CRD_PRO = ? " +
                    "AND CRD_TRA = ? AND CRD_OCU = ? ";

            // Comprobamos si esta activado el servicio Web de firmadoc.
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                from += ", CRD_FIR_FD";
                where += "AND CRD_EXP_FD IS NOT NULL AND CRD_DOC_FD IS NOT NULL ";
            } else {
                from += ", CRD_FIR_EST";
                where += "AND CRD_EXP_FD IS NULL AND CRD_DOC_FD IS NULL AND CRD_FIR_FD IS NULL";
            }
            String[] join2 = new String[17];
            join2[0] = "G_CRD";
            join2[1] = "INNER";
            join2[2] = GlobalNames.ESQUEMA_GENERICO + "A_USU USU";
            join2[3] = "G_CRD.CRD_USC = USU.USU_COD";
            join2[4] = "LEFT";
            join2[5] = GlobalNames.ESQUEMA_GENERICO + "A_USU USU1";
            join2[6] = "G_CRD.CRD_USM = USU1.USU_COD";
            join2[7] = "LEFT";
            join2[8] = "E_DOT";
            join2[9] = "G_CRD.CRD_PRO = E_DOT.DOT_PRO AND G_CRD.CRD_TRA = E_DOT.DOT_TRA " +
                    "AND G_CRD.CRD_DOT = E_DOT.DOT_COD";
            join2[10] = "LEFT";
            join2[11] = "A_PLT";
            join2[12] = "E_DOT.DOT_PLT = A_PLT.PLT_COD";
            join2[13] = "INNER";
            join2[14] = "G_EXP";
            join2[15] = "G_CRD.CRD_MUN = G_EXP.REL_MUN AND G_CRD.CRD_PRO = G_EXP.REL_PRO " +
                    "AND G_CRD.CRD_NUM = G_EXP.REL_NUM AND G_EXP.EXP_NUM = ?";
            join2[16] = "false";
            sql = oad.join(from,where,join2);

            m_Log.debug("CONSULTA PARA RECUPERAR LOS DOCUMENTOS DE LAS RELACIONES A LAS QUE PERTENCE");
            m_Log.debug(sql);

            st=con.prepareStatement(sql);
            
            
             if (ConstantesDatos.ORACLE.equalsIgnoreCase(oad.getTipoGestor())) {	
                JdbcOperations.setValues(st, 1,	
                        Integer.parseInt(tEVO.getCodMunicipio()),	
                        tEVO.getCodProcedimiento(),	
                        Integer.parseInt(tEVO.getCodTramite()),	
                        Integer.parseInt(tEVO.getOcurrenciaTramite()),	
                        tEVO.getNumeroExpediente());	
            } else {	
                JdbcOperations.setValues(st, 1,	
                        tEVO.getNumeroExpediente(),	
                        Integer.parseInt(tEVO.getCodMunicipio()),	
                        tEVO.getCodProcedimiento(),	
                        Integer.parseInt(tEVO.getCodTramite()),	
                        Integer.parseInt(tEVO.getOcurrenciaTramite()));	
            }
            
            
            rs = st.executeQuery();
            while ( rs.next() ) {
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString("CRD_NUD");
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString("CRD_DES");
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString("CRD_FAL");
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString("CRD_FMO");
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString("PLT_INT");
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                String estadoFirma;
                if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                    estadoFirma = rs.getString("CRD_FIR_FD");
                } else {
                    estadoFirma = rs.getString("CRD_FIR_EST");
                }
                tramExpVO.setEstadoFirma(estadoFirma);
                tramExpVO.setRelacion(rs.getString("CRD_NUM"));

                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }
            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            rs.close();

            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            rs.close();



            sql = "SELECT " + dot_cod + " FROM E_DOT WHERE " +  dot_mun + "=?" + 
                    " AND " + dot_pro + "=? AND " + dot_tra + "=?" 
                    +" AND DOT_ACTIVO = 'SI'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            st=con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getCodTramite()));
           

            rs = st.executeQuery();
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 ="si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(dot_cod);
                tramExpVO.setCodDocumento(codDocumento);
                listaCodDocumentosTramite.addElement(tramExpVO);
                tEVO.setListaCodDocumentosTramite(listaCodDocumentosTramite);
            }
            if(entrar1.equals("no")) {
                tEVO.setListaCodDocumentosTramite(listaCodDocumentosTramite);
            }
            rs.close();

            // PESTAÑA DE ENLACES

            sql = "SELECT " + ten_des + "," + ten_url + "," + ten_est + " FROM E_TEN WHERE " + ten_mun + "=?" +
                    " AND " + ten_pro + "=?" +
                    " AND " + ten_tra + "=?" ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st=con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getCodTramite()));
           
            rs = st.executeQuery();
            
            String entrar5 = "no";
            while(rs.next()) {
                entrar5 ="si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String descEnlace = rs.getString(ten_des);
                tramExpVO.setDescEnlace(descEnlace);
                String url = rs.getString(ten_url);
                tramExpVO.setUrl(url);
                String estado = rs.getString(ten_est);
                tramExpVO.setEstadoEnlace(estado);
                listaEnlaces.addElement(tramExpVO);
                tEVO.setListaEnlaces(listaEnlaces);
            }
            if(entrar5.equals("no")) {
                tEVO.setListaEnlaces(listaEnlaces);
            }
            rs.close();

            // CONDICIONES DE SALIDA DEL TRAMITE
            sql = "SELECT " + sal_tac + "," + sal_taa + "," + sal_tan + "," + sal_obl + "," + sal_obld +
                    " FROM E_SAL,E_TRA WHERE " + sal_mun + "=? AND " + sal_pro + "=?" +
                    " AND " + sal_tra + "=?"  +
                    " AND " + sal_mun + "=" + tra_mun + " AND " + sal_pro + "=" + tra_pro + " AND " +
                    sal_tra + "=" + tra_cod + " AND " + tra_fba + " IS NULL";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st=con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getCodTramite()));
           
            rs = st.executeQuery();
            
            String entrar4 = "no";
            while ( rs.next() ) {
                entrar4 = "si";
                String accion = rs.getString(sal_tac);
                tEVO.setAccion(accion);
                String accionAfirmativa = rs.getString(sal_taa);
                tEVO.setAccionAfirmativa(accionAfirmativa);
                String accionNegativa = rs.getString(sal_tan);
                tEVO.setAccionNegativa(accionNegativa);
                String obligatorio = rs.getString(sal_obl);
                tEVO.setObligatorio(obligatorio);
                String obligatorioDesf = rs.getString(sal_obld);
                tEVO.setObligatorioDesf(obligatorioDesf);
            }
            rs.close();
            if(entrar4.equals("si")) {
                if("P".equals(tEVO.getAccion())) {
                    sql = "SELECT " + sml_valor + " FROM E_SML WHERE " + sml_mun + "=?"+
                            " AND " + sml_pro + "=? AND " + sml_tra + "=?" +
                           " AND " + sml_cmp + "='TXT' AND " + sml_leng + "='"+idiomaDefecto+"'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                    st=con.prepareStatement(sql);
                    st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
                    st.setString(2, tEVO.getCodProcedimiento());
                    st.setInt(3, Integer.parseInt(tEVO.getCodTramite()));

                    rs = st.executeQuery();
            
                    while ( rs.next() ) {
                        String pregunta = rs.getString(sml_valor);
                        tEVO.setPregunta(pregunta);
                    }
                }
            }
            rs.close();

            // LISTAS DEL FLUJO DE SALIDA
            String codOrg = tEVO.getCodMunicipio();
            String codProc = tEVO.getCodProcedimiento();
            String codTram = tEVO.getCodTramite();

            listaTramitesFavorables =
                getFlujoSalida(con, codOrg, codProc, codTram, FLUJO_LISTA_TRAMITES);

            if (listaTramitesFavorables.size() > 0) {
                tEVO.setListaTramitesFavorables(listaTramitesFavorables);
                tEVO.setListaTramitesNoFavorables(new Vector());
            } else {
                listaTramitesFavorables =
                    getFlujoSalida(con, codOrg, codProc, codTram, FLUJO_LISTA_TRAMITES_FAVORABLE);
                listaTramitesNoFavorables =
                    getFlujoSalida(con, codOrg, codProc, codTram, FLUJO_LISTA_TRAMITES_NO_FAVORABLE);
                tEVO.setListaTramitesFavorables(listaTramitesFavorables);
                tEVO.setListaTramitesNoFavorables(listaTramitesNoFavorables);
            }

            // PERMISO USUARIO
            String codUsuario = tEVO.getCodUsuario();
            String codOrganizacion = tEVO.getCodOrganizacion();
            String codEntidad = tEVO.getCodEntidad();
            String permiso = "no";
            sql = "SELECT DISTINCT " + exp_uor + "," + cro_utr + " FROM E_EXP, E_CRO WHERE " +
                    exp_mun + "=" + cro_mun + " AND " + exp_pro + "=" + cro_pro + " AND " + exp_eje + "=" +
                    cro_eje + " AND " + exp_num + "=" + cro_num + " AND " + exp_mun + "=?" +
                    " AND " + exp_pro + "=? AND " + exp_eje + "=?" + 
                    " AND " + exp_num + "=? AND " + cro_tra + "=?" +
                    " AND " + cro_ocu + "=? AND (EXISTS ( SELECT DISTINCT " +
                    uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " + uou_usu + "=?" +
                    " AND " + uou_org +
                    "=? AND " + uou_ent + "=? AND " + uou_uor +
                    "=" + exp_uor + ") OR EXISTS (SELECT DISTINCT " + uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO +
                    "A_UOU A_UOU WHERE " + uou_usu +
                    "=? AND " + uou_org + "=? AND " + uou_ent + "=?" +
                    " AND " + uou_uor + "=" + cro_utr + "))" ;

            if(m_Log.isDebugEnabled()) m_Log.debug( sql);

            st=con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getEjercicio()));
            st.setString(4, tEVO.getNumeroExpediente());
            st.setInt(5, Integer.parseInt(tEVO.getCodTramite()));
            st.setInt(6, Integer.parseInt(tEVO.getOcurrenciaTramite()));
            st.setInt(7, Integer.parseInt(codUsuario));
            st.setInt(8, Integer.parseInt(codOrganizacion));
            st.setInt(9, Integer.parseInt(codEntidad));
            st.setInt(10, Integer.parseInt(codUsuario));
            st.setInt(11, Integer.parseInt(codOrganizacion));
            st.setInt(12, Integer.parseInt(codEntidad));

            rs = st.executeQuery();
            if (rs.next()){
                String unidadControladora = (String) rs.getString(exp_uor);
                String unidadTramitadora = (String) rs.getString(cro_utr);
                if ( (unidadControladora != null) && (unidadTramitadora != null) ) permiso = "si";
            }
            //if(m_Log.isDebugEnabled()) m_Log.debug("el permiso del usuario es : " + permiso);
            tEVO.setPermiso(permiso);
            rs.close();

            //inicio COMPROBAR BLOQUEO DE USUARIO en el primer expediente de la relacion (todos serán iguales)
            sql = "SELECT " + exp_rel_num + " FROM G_EXP WHERE " +
                    exp_exp_mun + "=? AND " +
                    exp_exp_pro + "=? AND " +
                    exp_exp_eje + "=? AND " +
                    exp_exp_num + "=? ";
            if(m_Log.isDebugEnabled()) m_Log.debug(" PRIMER EXPEDIENTE : "+ sql);
            
            st=con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getEjercicio()));
            st.setString(4, tEVO.getNumeroRelacion());
           
            
            rs = st.executeQuery();
            
            if (rs.next()){
                tEVO.setNumeroExpediente(rs.getString(exp_rel_num));  // Expediente de la relacion
            }
            rs.close();

            sql = "SELECT " + bloq_usu + " FROM E_EXP_BLOQ WHERE " +
                    bloq_mun + "=? AND " +
                    bloq_pro + "=? AND " +
                    bloq_eje + "=? AND " +
                    bloq_num + "=? AND " +
                    bloq_tra + "=? AND " +
                    bloq_ocu + "=?";

            if(m_Log.isDebugEnabled()) m_Log.debug(" BLOQUEO RELACIONES : "+ sql);

            st=con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getEjercicio()));
            st.setString(4, tEVO.getNumeroExpediente());
            st.setInt(5, Integer.parseInt(tEVO.getCodTramite()));
            st.setInt(6, Integer.parseInt(tEVO.getOcurrenciaTramite()));
           
            
            rs = st.executeQuery();
            
            String bloqueo = ""; //Por defecto tramite no bloqueado
            if (rs.next()){
                bloqueo = rs.getString(bloq_usu);  // Usuario que bloquea el expediente en ese trémite
            }
            //if(m_Log.isDebugEnabled()) m_Log.debug("el permiso del usuario es : " + permiso);
            if(m_Log.isDebugEnabled()) m_Log.debug(" BLOQUEO RELACIONES : "+ bloqueo);
            tEVO.setBloqueo(bloqueo);
            rs.close();
            //fin COMPROBAR BLOQUEO DE USUARIO
            

        } catch (Exception e) {
            e.printStackTrace();
             
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.cargarDatos", e);
        } finally {
            try {              
                st.close();
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }

        return tEVO;
    }




    public Vector<SiguienteTramiteTO> getFlujoSalida(Connection con,
            String codOrg, String codProc, String codTram, int numCod)
        throws SQLException {

        String sql;
        PreparedStatement ps;
        ResultSet rs;
        Vector<SiguienteTramiteTO> lista = new Vector<SiguienteTramiteTO>();

        sql = "SELECT FLS_CTS, FLS_NUS, TRA_COU, TRA_UTR, TML_VALOR " +
              "FROM (E_FLS INNER JOIN E_TRA ON (FLS_MUN=TRA_MUN AND FLS_PRO=TRA_PRO " +
                    "AND FLS_CTS=TRA_COD AND TRA_FBA IS NULL)) " +
              "LEFT JOIN E_TML ON (FLS_MUN=TML_MUN AND FLS_PRO=TML_PRO AND FLS_CTS=TML_TRA " +
                    "AND TML_CMP='NOM' AND TML_LENG="+idiomaDefecto+") " +
              "WHERE FLS_MUN=? AND FLS_PRO=? AND FLS_TRA=? AND FLS_NUC=" + numCod;
        if (m_Log.isDebugEnabled()) m_Log.debug(sql);

        ps = con.prepareStatement(sql);
        int i=1;
        ps.setInt(i++,Integer.parseInt(codOrg));
        ps.setString(i++,codProc);
        ps.setInt(i++,Integer.parseInt(codTram));
        rs = ps.executeQuery();

        while (rs.next()) {
            SiguienteTramiteTO tramiteTO = new SiguienteTramiteTO();
            tramiteTO.setCodigoTramiteFlujoSalida(rs.getString("FLS_CTS"));
            tramiteTO.setNumeroSecuencia(rs.getString("FLS_NUS"));
            tramiteTO.setCodigoVisibleTramiteFlujoSalida(rs.getString("TRA_COU"));
            tramiteTO.setDescripcionTramiteFlujoSalida(rs.getString("TML_VALOR"));
            tramiteTO.setModoSeleccionUnidad(rs.getInt("TRA_UTR"));
            lista.addElement(tramiteTO);
        }
        rs.close();
        ps.close();

        return lista;
    }


    public TramitacionExpedientesValueObject getListaDocumentosTramite(TramitacionExpedientesValueObject tEVO,String[] params)
            throws AnotacionRegistroException,TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("GETLISTADOCUMENTOSTRAMITE");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector listaDocumentosTramite = new Vector();


        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            from = dot_cod + "," + plt_des + "," + dot_plt + "," + plt_int + "," + plt_rel + ",PLT_EDITOR_TEXTO ";
            where = dot_mun + "=" + tEVO.getCodMunicipio() + " AND " + dot_pro + "='" +
                    tEVO.getCodProcedimiento() + "' AND " + dot_tra + "=" + tEVO.getCodTramite() + " AND " + dot_activo + " = 'SI'";
            if (tEVO.getRelacion().equals("N")) where += " AND (PLT_REL is null or PLT_REL = 'N') ";
            String[] join = new String[5];
            join[0] = "E_DOT";
            join[1] = "INNER";
            join[2] = "a_plt";
            join[3] = "e_dot." + dot_plt	+ "=a_plt." + aplt_cod + " AND " +
                      "e_dot." + dot_pro	+ "=a_plt." + plt_pro + " AND " +
                      "e_dot." + dot_tra	+ "=a_plt." + plt_tra;
            join[4] = "false";
            sql = oad.join(from,where,join);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            String entrar = "no";
            while(rs.next()) {
                entrar = "si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDoc = rs.getString(dot_cod);
                tramExpVO.setCodigoDocumento(codDoc);
                String descDoc = rs.getString(plt_des);
                tramExpVO.setDescripcionDocumento(descDoc);
                String codPlantilla = rs.getString(dot_plt);
                tramExpVO.setCodPlantilla(codPlantilla);
                String interesado = rs.getString(plt_int);
                tramExpVO.setInteresado(interesado);
                String relacion = rs.getString(plt_rel);
                tramExpVO.setRelacion(relacion);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                listaDocumentosTramite.addElement(tramExpVO);
                tEVO.setListaDocumentosTramite(listaDocumentosTramite);
            }
            if(entrar.equals("no")) {
                tEVO.setListaDocumentosTramite(listaDocumentosTramite);
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.getListaDocumentosTramite", e);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaDocumentosTramite");
        return tEVO;
    }

    /**
     * Carga los documentos de un tramite (ficheros .doc de la pestaña documentos)
     * en el formato utilizado por los documentos de los asientos del registro
     * (un Vector de RegistroValueObject con los valores en los campos nombreDoc,
     * fechaDoc, tipoDoc y doc)
     */
    public Vector<RegistroValueObject> cargarDocumentosTramite(TramitacionExpedientesValueObject tVO, String[] params)
      throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("cargarDocumentosTramite");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        Vector<RegistroValueObject> docsTramite = new Vector();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();           

            sql =  "SELECT CRD_DES, " + oad.convertir("CRD_FAL", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FAL, CRD_FIL, CRD_NUD " +
                  " FROM E_CRD" +
                  " WHERE CRD_MUN=?" + 
                  " AND CRD_PRO=?"+
                  " AND CRD_EJE=?" + 
                  " AND CRD_NUM=?" + 
                  " AND CRD_TRA=?" + 
                  " AND CRD_OCU=?" ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = con.prepareStatement(sql);            
            ps.setInt(1,Integer.parseInt(tVO.getCodMunicipio()));
            ps.setString(2,tVO.getCodProcedimiento());
            ps.setInt(3,Integer.parseInt(tVO.getEjercicio()));
            ps.setString(4,tVO.getNumeroExpediente());
            ps.setInt(5,Integer.parseInt(tVO.getCodTramite()));
            ps.setInt(6,Integer.parseInt(tVO.getOcurrenciaTramite()));
            
            rs = ps.executeQuery();
            
            while(rs.next()) {
                RegistroValueObject docVO = new RegistroValueObject();
                docVO.setFechaDoc(rs.getString("CRD_FAL"));
                docVO.setTipoDoc(ConstantesDatos.TIPO_MIME_DOC_TRAMITES);
                docVO.setDoc(rs.getBytes("CRD_FIL"));
                docVO.setNombreDoc(rs.getString("CRD_NUD")+"_"+rs.getString("CRD_DES"));
                docVO.setEntregado("S");
                docsTramite.add(docVO);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.cargarDocumentosTramite", e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        m_Log.debug("cargarDocumentosTramite");
        return docsTramite;
    }

    public void getListaDocumentosCronologia(TramitacionExpedientesValueObject tEVO,String[] params)
            throws AnotacionRegistroException,TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaDocumentosCronologia");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector listaDocumentos = new Vector();


        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();


            from = crd_nud + "," + crd_des + "," + oad.convertir(crd_fal, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + crd_fal + "," + oad.convertir(crd_fmo, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS "
                    + crd_fmo + "," + oad.convertir("CRD_FINF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FINF,usu." + usu_nom + " AS usuarioCreacion,usu1." +
                    usu_nom + " AS usuarioModificacion," + plt_int + ",PLT_EDITOR_TEXTO, crd_id_metadato ";
            from += ","+crd_fir_est;
            where = crd_mun + " = " + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + " = '" + tEVO.getCodProcedimiento() + "' AND " +
                    crd_tra + " = " + tEVO.getCodTramite() + " AND " +
                    crd_num + "='" + tEVO.getNumeroExpediente() + "' AND " +
                    crd_ocu + " = " + tEVO.getOcurrenciaTramite() + " AND " +
                    crd_exp_fd + " IS NULL AND " +
                    crd_doc_fd + " IS NULL ";
            String[] join1 = new String[14];
            join1[0] = "E_CRD";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu";
            join1[3] = "e_crd." + crd_usc + "=usu." + usu_cod;
            join1[4] = "LEFT";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu1";
            join1[6] = "e_crd." + crd_usm + "=usu1." + usu_cod;
            join1[7] = "LEFT";
            join1[8] = "e_dot";
            join1[9] = "e_crd." + crd_pro + "=e_dot." + dot_pro + " AND " +
                    "e_crd." + crd_tra + "=e_dot." + dot_tra + " AND " +
                    "e_crd." + crd_dot + "=e_dot." + dot_cod;
            join1[10] = "LEFT";
            join1[11] = "a_plt";
            join1[12] = "e_dot." + dot_plt + "=a_plt." + plt_cod;
            join1[13] = "false";
            sql = oad.join(from,where,join1);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(crd_nud);
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString(crd_des);
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString(crd_fal);
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString(crd_fmo);
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");
                String fechaInforme = rs.getString("CRD_FINF");
                if(fechaInforme!=null)
                    tramExpVO.setFechaInforme(fechaInforme);
                else
                    tramExpVO.setFechaInforme(" ");

                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString(plt_int);
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                int codigoMetadato=(rs.getInt("crd_id_metadato"));
                if (codigoMetadato>0){
                     if(m_Log.isDebugEnabled()) m_Log.debug("El documento tiene metadato, el editor pasa a ser NO EDITABLE");
                    tramExpVO.setEditorTexto("");
                }
                String estadoFirma = rs.getString(crd_fir_est);
                tramExpVO.setEstadoFirma(estadoFirma);
                tramExpVO.setRelacion("");
                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }
            //DOCUMENTOS DE LAS RELACIONES A LOS QUE PERTENECEN
            from = "distinct " + crd_nud + "," + crd_des + "," + oad.convertir(crd_fal, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + crd_fal + "," + oad.convertir(crd_fmo, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS "
                    + crd_fmo + ",usu." + usu_nom + " AS usuarioCreacion,usu1." +
                    usu_nom + " AS usuarioModificacion," + plt_int + ",PLT_EDITOR_TEXTO,"+ crd_num;

            where = crd_mun + "=" + tEVO.getCodMunicipio() + " AND " + crd_pro + "='" + tEVO.getCodProcedimiento() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + " = " + tEVO.getOcurrenciaTramite() + " ";

            // Comprobamos si esta activado el servicio Web de firmadoc.
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                from += ", " + crd_fir_fd;
                where += "AND " + crd_exp_fd + " IS NOT NULL " +
                        "AND " + crd_doc_fd + " IS NOT NULL ";
            } else {
                from += ", " + crd_fir_est;
                where += "AND " + crd_exp_fd + " IS NULL " +
                        "AND " + crd_doc_fd + " IS NULL " +
                        "AND " + crd_fir_fd + " IS NULL";
            }
            String[] join2 = new String[17];
            join2[0] = "G_CRD";
            join2[1] = "INNER";
            join2[2] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu";
            join2[3] = "g_crd." + crd_usc + "=usu." + usu_cod;
            join2[4] = "LEFT";
            join2[5] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu1";
            join2[6] = "g_crd." + crd_usm + "=usu1." + usu_cod;
            join2[7] = "LEFT";
            join2[8] = "e_dot";
            join2[9] = "g_crd." + crd_pro + "=e_dot." + dot_pro + " AND " +
                    "g_crd." + crd_tra + "=e_dot." + dot_tra + " AND " +
                    "g_crd." + crd_dot + "=e_dot." + dot_cod;
            join2[10] = "LEFT";
            join2[11] = "a_plt";
            join2[12] = "e_dot." + dot_plt + "=a_plt." + plt_cod;
            join2[13] = "INNER";
            join2[14] = "g_exp";
            join2[15] = "g_crd." + crd_mun + "=g_exp." + exp_rel_mun + " AND " +
                    "g_crd." + crd_pro + "=g_exp." + exp_rel_pro + " AND " +
                    "g_exp." + exp_rel_num + " = '" + tEVO.getNumeroExpediente() +"'";
            join2[16] = "false";
            sql = oad.join(from,where,join2);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while ( rs.next() ) {
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(crd_nud);
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString(crd_des);
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString(crd_fal);
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString(crd_fmo);
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString(plt_int);
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                String estadoFirma;
                if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                    estadoFirma = rs.getString(crd_fir_fd);
                } else {
                    estadoFirma = rs.getString(crd_fir_est);
                }
                tramExpVO.setEstadoFirma(estadoFirma);
                //tramExpVO.setRelacion(crd_num);
                tramExpVO.setRelacion(rs.getString("CRD_NUM"));
                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }
            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.getListaDocumentosCronologia", e);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaDocumentosCronologia");
    }


    public void getListaDocumentosRelacionCronologia(TramitacionExpedientesValueObject tEVO,String[] params)
            throws AnotacionRegistroException,TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaDocumentosRelacionCronologia");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector listaDocumentos = new Vector();


        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            from = crd_nud + "," + crd_des + "," + oad.convertir(crd_fal, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + crd_fal + "," + oad.convertir(crd_fmo, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS "
                    + crd_fmo + ",usu." + usu_nom + " AS usuarioCreacion,usu1." +
                    usu_nom + " AS usuarioModificacion," + plt_int + ",PLT_EDITOR_TEXTO";
            from += ","+crd_fir_est+","+crd_exp;
            where = crd_mun + " = " + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + " = '" + tEVO.getCodProcedimiento() + "' AND " +
                    crd_tra + " = " + tEVO.getCodTramite() + " AND " +
                    crd_num + "='" + tEVO.getNumeroRelacion() + "' AND " +
                    crd_ocu + " = " + tEVO.getOcurrenciaTramite() + " AND " +
                    crd_exp_fd + " IS NULL AND " +
                    crd_doc_fd + " IS NULL ";
            String[] join1 = new String[14];
            join1[0] = "G_CRD";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu";
            join1[3] = "g_crd." + crd_usc + "=usu." + usu_cod;
            join1[4] = "LEFT";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu1";
            join1[6] = "g_crd." + crd_usm + "=usu1." + usu_cod;
            join1[7] = "LEFT";
            join1[8] = "e_dot";
            join1[9] = "g_crd." + crd_pro + "=e_dot." + dot_pro + " AND " +
                    "g_crd." + crd_tra + "=e_dot." + dot_tra + " AND " +
                    "g_crd." + crd_dot + "=e_dot." + dot_cod;
            join1[10] = "LEFT";
            join1[11] = "a_plt";
            join1[12] = "e_dot." + dot_plt + "=a_plt." + plt_cod;
            join1[13] = "false";
            sql = oad.join(from,where,join1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(crd_nud);
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString(crd_des);
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString(crd_fal);
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString(crd_fmo);
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString(plt_int);
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                String estadoFirma = rs.getString(crd_fir_est);
                tramExpVO.setEstadoFirma(estadoFirma);
                String opcionGrabar = rs.getString(crd_exp);
                tramExpVO.setOpcionGrabar(opcionGrabar);
                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }
            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.getListaDocumentosRelacionCronologia", e);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaDocumentosRelacionCronologia");
    }

    public int bloquearTramite(TramitacionExpedientesValueObject tEVO, Integer usuario, String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int resultado=0;
        String sql = "";

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);
                                     //+ exp_ref + "='"+ referencia+ "',"
            sql = "INSERT INTO E_EXP_BLOQ (" + bloq_mun + "," + bloq_eje + "," + bloq_num + "," + bloq_pro + "," + bloq_tra + "," +
                    bloq_ocu + "," + bloq_usu + ") VALUES (" + tEVO.getCodMunicipio() + "," + tEVO.getEjercicio() + ",'" + tEVO.getNumeroExpediente() +
                    "','" + tEVO.getCodProcedimiento() + "'," + tEVO.getCodTramite() + "," + tEVO.getOcurrenciaTramite() + "," + usuario + ")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);
            st.close();

        }catch ( SQLException e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.bloquearTamite " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.bloquearTamite " + bde.getMessage());
        } finally{
            try{
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.bloquearTamite.Fin transaccion y devolver conexion", bde);
            }
        }
        return resultado;

    }

    public int desbloquearTramite(TramitacionExpedientesValueObject tEVO,Integer usuario, String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int resultado=0;
        String sql = "";

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);
                                     //+ exp_ref + "='"+ referencia+ "',"
            sql = "DELETE FROM E_EXP_BLOQ WHERE " + bloq_mun + " = " + tEVO.getCodMunicipio() + " and " +
                                                   bloq_eje + " = " + tEVO.getEjercicio() + " and " +
                                                   bloq_num + " = '" + tEVO.getNumeroExpediente() + "' and " +
                                                   bloq_pro + " = '" + tEVO.getCodProcedimiento() + "' and " +
                                                   bloq_tra + " = " + tEVO.getCodTramite() + " and " +
                                                   bloq_ocu + " = " + tEVO.getOcurrenciaTramite() + " and " +
                                                   bloq_usu + " = " + usuario;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);
            st.close();

        }catch ( SQLException e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.desbloquearTamite " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.desbloquearTamite " + bde.getMessage());
        } finally{
            try{
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.desbloquearTamite.Fin transaccion y devolver conexion", bde);
            }
        }
        return resultado;

    }

    public int grabarTramite(TramitacionExpedientesValueObject tEVO,String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado=0;

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            resultado = TramitesExpedienteDAO.getInstance().actualizarTramite(oad,con,tEVO);

        }catch ( SQLException e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        } finally{
            try{
                if (resultado >= 0)
                    try{
                    con.commit();
                    m_Log.debug("*** DESPUÉS DE CONFIRMAR TRANSACCIÓN DE ACTUALIZARTRAMITE");
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                    //oad.finTransaccion(con);
                else
                    try{
                        con.rollback();
                        m_Log.debug("*** DESPUÉS DE CONFIRMAR TRANSACCIÓN DE ROLLBACK");
                        //oad.rollBack(con);
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }

                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }
        }

        return resultado;

    }



    public int grabarLocalizacion(GeneralValueObject gVO,String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int resultado=0;
        String sql = "";
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numeroExpediente");
        String codLocalizacion = (String) gVO.getAtributo("codLocalizacion");
        String localizacion = (String) gVO.getAtributo("localizacion");
        String referencia = (String) gVO.getAtributo("referencia");
        String codMunExp=(String) gVO.getAtributo("codMunExp");

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);
                                     //+ exp_ref + "='"+ referencia+ "',"
            sql = "UPDATE E_EXP SET " + exp_loc + "='" + localizacion + "'," + exp_clo + "=" + codLocalizacion +
                    " WHERE " + exp_mun + "=" + codMunExp + " AND " + exp_eje + "=" + ejercicio + " AND " +
                    exp_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);
            st.close();
        }catch ( SQLException e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        } finally{
            try{
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }
        }
        return resultado;

    }

    public int eliminarLocalizacion(GeneralValueObject gVO,String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int resultado=0;
        String sql = "";
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numeroExpediente");
        String codMunExp=(String) gVO.getAtributo("codMunExp");

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);

            sql = "UPDATE E_EXP SET " + exp_loc + "=null," + exp_clo + "=null" +
                    " WHERE " + exp_mun + "=" + codMunExp + " AND " + exp_eje + "=" + ejercicio + " AND " +
                    exp_num + "='" + numeroExpediente + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);
            st.close();
        }catch ( SQLException e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        } finally{
            try{
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }
        }
        return resultado;

    }

    public int eliminarDocumentoCRD(TramitacionExpedientesValueObject tEVO,String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int resultado=0;
        String sql = "";

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);

            // Se obtiene el id para borrar los metadatos
            Long idMetadatos = getDocumentoIdMetadato(
                    NumberUtils.createInteger(tEVO.getCodDocumento()),
                    NumberUtils.createInteger(tEVO.getEjercicio()),
                    NumberUtils.createInteger(tEVO.getCodMunicipio()),
                    tEVO.getNumeroExpediente(),
                    NumberUtils.createInteger(tEVO.getCodTramite()),
                    NumberUtils.createInteger(tEVO.getOcurrenciaTramite()),
                    tEVO.getCodProcedimiento(),
                    con);
            
            // Se borran las firmas de flujo/usuario
            eliminarDocumentoCRDFlujoFirmaPersonalizada(tEVO, con);
            
            // Se borra el documento
            sql = "DELETE FROM E_CRD WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);
             if (resultado == 1 && idMetadatos != null) {
                DocumentoDAO.getInstance().eliminarMetadato(idMetadatos, con);
            }
            st.close();
        }catch ( SQLException e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        } finally{
            try{
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }
        }
        return resultado;

    }

    public int eliminarDocumentoRelacionCRD(TramitacionExpedientesValueObject tEVO,String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int resultado=0;
        String sql = "",opcionGrabar = "0";

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);

            //Antes de nada saber si el documento ha sido grabado en todos los expedientes de la relación
            sql = "SELECT " + crd_exp + " FROM G_CRD WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroRelacion() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            if (rs.next()) {
                opcionGrabar = rs.getString(crd_exp);
            }
            rs.close();
            st.close();

            if (opcionGrabar.equals("1")) {
                Vector exps = new Vector();
                st = con.createStatement();
                sql = "SELECT EXP_CRD_EJE, EXP_CRD_MUN, EXP_CRD_NUD, EXP_CRD_NUM, EXP_CRD_OCU, EXP_CRD_PRO, EXP_CRD_TRA" +
                        " FROM G_CRD_EXP WHERE REL_CRD_MUN = " + tEVO.getCodMunicipio() + " AND REL_CRD_PRO='" +
                        tEVO.getCodProcedimiento() + "' AND REL_CRD_EJE =" + tEVO.getEjercicio() + " AND REL_CRD_NUM ='" +
                        tEVO.getNumeroRelacion() + "' AND REL_CRD_TRA =" + tEVO.getCodTramite() + " AND REL_CRD_OCU = " +
                        tEVO.getOcurrenciaTramite() + " AND REL_CRD_NUD = " + tEVO.getCodDocumento();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = st.executeQuery(sql);
                while ( rs.next() ) {
                    GeneralValueObject exp = new GeneralValueObject();
                    exp.setAtributo("ejercicio", rs.getString("EXP_CRD_EJE"));
                    exp.setAtributo("municipio", rs.getString("EXP_CRD_MUN"));
                    exp.setAtributo("numDoc", rs.getString("EXP_CRD_NUD"));
                    exp.setAtributo("numExp", rs.getString("EXP_CRD_NUM"));
                    exp.setAtributo("ocurrencia", rs.getString("EXP_CRD_OCU"));
                    exp.setAtributo("procedimiento", rs.getString("EXP_CRD_PRO"));
                    exp.setAtributo("tramite", rs.getString("EXP_CRD_TRA"));
                    exps.add(exp);
                }
                rs.close();
                st.close();
                Long idMetadatos = null;
                for (int i=0; i<exps.size(); i++) {
                    GeneralValueObject exp = (GeneralValueObject)exps.get(i);
                    st = con.createStatement();
                    
                     idMetadatos = getDocumentoIdMetadato(
                            NumberUtils.createInteger((String) exp.getAtributo("numDoc")),
                            NumberUtils.createInteger((String) exp.getAtributo("ejercicio")),
                            NumberUtils.createInteger((String) exp.getAtributo("municipio")),
                            (String) exp.getAtributo("numExp"),
                            NumberUtils.createInteger((String) exp.getAtributo("tramite")),
                            NumberUtils.createInteger((String) exp.getAtributo("ocurrencia")),
                            (String) exp.getAtributo("procedimiento"),
                            con);
                    sql = "DELETE FROM E_CRD WHERE " + crd_mun + "=" + exp.getAtributo("municipio") + " AND " +
                            crd_pro + "='" + exp.getAtributo("procedimiento") + "' AND " + crd_eje + "=" +
                            exp.getAtributo("ejercicio") + " AND " + crd_num + "='" + exp.getAtributo("numExp") +
                            "' AND " + crd_tra + "=" + exp.getAtributo("tramite") + " AND " + crd_ocu + "=" +
                            exp.getAtributo("ocurrencia") + " AND " + crd_nud + "=" + exp.getAtributo("numDoc");
                    if(m_Log.isDebugEnabled()) m_Log.debug("BORRADO EN E_CRD "+sql);
                    st.executeUpdate(sql);
                    st.close();
                    if (idMetadatos != null) {
                        DocumentoDAO.getInstance().eliminarMetadato(idMetadatos, con);
                    }
                }
                st = con.createStatement();
                sql = "DELETE FROM G_CRD_EXP WHERE REL_CRD_MUN = " + tEVO.getCodMunicipio() + " AND REL_CRD_PRO='" +
                        tEVO.getCodProcedimiento() + "' AND REL_CRD_EJE =" + tEVO.getEjercicio() + " AND REL_CRD_NUM ='" +
                        tEVO.getNumeroRelacion() + "' AND REL_CRD_TRA =" + tEVO.getCodTramite() + " AND REL_CRD_OCU = " +
                        tEVO.getOcurrenciaTramite() + " AND REL_CRD_NUD = " + tEVO.getCodDocumento();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st.executeUpdate(sql);
                st.close();
            }

            st = con.createStatement();
            sql = "DELETE FROM G_CRD WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroRelacion() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
            if(m_Log.isDebugEnabled()) m_Log.debug("BORRADO EN G_CRD "+sql);
            resultado = st.executeUpdate(sql);
            st.close();
        }catch ( SQLException e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        } finally{
            try{
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }
        }
        return resultado;

    }



    public int cambiarEstadoFirmaDocumentoCRD(TramitacionExpedientesValueObject tEVO, int idUsuario, String portafirmas, String[] params)
            throws TramitacionException, TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        int resultado = 0;
        String sql;

        
        //Recuperamos la propiedad que nos indica si existe un portafirmas externo
        if(m_Log.isDebugEnabled()) m_Log.debug("Comprobamos si existe un portafirmas externo");
        Boolean existePortafirmasExterno = 
                PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(tEVO.getCodOrganizacion());
        if(m_Log.isDebugEnabled()) m_Log.debug("existe un portafirmas externo = " + existePortafirmasExterno);

        //Recuperamos el codigo del cliente del portafirmas
        String clientePortafirmasExterno = new String();
        if(existePortafirmasExterno){
            if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos el cliente del portafirmas externo");
            String cliente = PluginPortafirmasExternoClienteFactoria.
                getCodClientePortafirmasExterno(tEVO.getCodMunicipio());
            if(cliente != null && !"".equalsIgnoreCase(cliente)){
                clientePortafirmasExterno = cliente;
                if(m_Log.isDebugEnabled()) m_Log.debug("Cliente del portafirmas externo =  " + cliente);
            }//if(clientePortafirmasExterno != null && !"".equalsIgnoreCase(clientePortafirmasExterno))
        }//if(existePortafirmasExterno)

        //Aqui pondriamos el control para ver si tenemos que enviar el documento a un portafirmas externo
        String resultadoPortafirmasExterno = new String("");
        
            
        try {
            if(existePortafirmasExterno){
                if(m_Log.isDebugEnabled()) m_Log.debug("Enviamos el documento al portafirmas externo");
                PluginPortafirmasExternoCliente portafirmasExterno =
                        PluginPortafirmasExternoClienteFactoria.getImplClass(tEVO.getCodOrganizacion());

                if(portafirmasExterno != null){
                    resultadoPortafirmasExterno = portafirmasExterno.enviarDocumentoTramitacionPortafirmas(tEVO.getCodOrganizacion(),
                            tEVO.getCodProcedimiento(), tEVO.getNumeroExpediente(), tEVO.getCodTramite(), tEVO.getOcurrenciaTramite(),
                            tEVO.getCodDocumento(), tEVO.getCodigoDocumentoAnterior(), idUsuario, new byte[9], params, portafirmas);
                    if(m_Log.isDebugEnabled()) m_Log.info("Id del documento en el portafirmas externo = " + resultadoPortafirmasExterno);
                }//if(portafirmasExterno != null)
            }//if(existePortafirmasExterno)
        
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);
            
            if(existePortafirmasExterno && ("".equalsIgnoreCase(resultadoPortafirmasExterno) || 
                PluginPortafirmasExternoCliente.OPERACION_ERROR.equals(resultadoPortafirmasExterno))){
                m_Log.error("Se ha producido un error enviando el documento al portafirmas externo");
                resultado = -1;
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite");
            }
            
            //Si se trata de un Portafirmas lanbide, como se han modificado la funcionalidad, se debe renombrar el nombre del documeto cambiando la extension a PDF
            if(existePortafirmasExterno && portafirmas != null && "LAN".equals(portafirmas)) {
                //Se actualiza el numero de documento de la tabla E_CRD_FIR_FIRMANTES para que apunte al documento correcto
                sql = "UPDATE E_CRD_FIR_FIRMANTES SET COD_DOCUMENTO =  " + tEVO.getCodDocumento() +
                " WHERE COD_MUNICIPIO =" + tEVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '" 
                        + tEVO.getCodProcedimiento() + "' AND  EJERCICIO =" + tEVO.getEjercicio() + 
                        " AND NUM_EXPEDIENTE ='" + tEVO.getNumeroExpediente() + "' AND COD_TRAMITE = " + tEVO.getCodTramite() + 
                        " AND COD_OCURRENCIA = " + tEVO.getOcurrenciaTramite() + " AND  COD_DOCUMENTO = " + tEVO.getCodigoDocumentoAnterior() +
                " AND  ID_USUARIO = (SELECT CRD_USM FROM E_CRD WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodigoDocumentoAnterior()+ ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                resultado = st.executeUpdate(sql);
                m_Log.info("resultado de la acualizacion de la tabla E_CRD_FIR_FIRMANTES del expediente " + tEVO.getNumeroExpediente() + " es: " + resultado);

                //Se actualiza el registro del documento convertido para renombrarlo correctamente y que apunte al usuario firmante correcto
                sql = "UPDATE E_CRD SET crd_des = SUBSTR(crd_des,0,instr(crd_des,'.',-1))||'pdf', CRD_USM = (SELECT ID_USUARIO FROM E_CRD_FIR_FIRMANTES WHERE COD_MUNICIPIO =" + tEVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '" 
                        + tEVO.getCodProcedimiento() + "' AND  EJERCICIO =" + tEVO.getEjercicio() + 
                        " AND NUM_EXPEDIENTE ='" + tEVO.getNumeroExpediente() + "' AND COD_TRAMITE = " + tEVO.getCodTramite() + 
                        " AND COD_OCURRENCIA = " + tEVO.getOcurrenciaTramite() + " AND  COD_DOCUMENTO = " + tEVO.getCodDocumento() +
                ") WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                resultado = st.executeUpdate(sql);
                m_Log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: " + resultado);


                //Se actualiza el documento original
                sql = "UPDATE E_CRD SET CRD_USM = " + idUsuario + 
                " WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodigoDocumentoAnterior();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                resultado = st.executeUpdate(sql);
                m_Log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: " + resultado);
            }

            final String nuevoEstadoFirma = ( (tEVO.getEstadoFirma()!=null)?("'"+tEVO.getEstadoFirma()+"'"):("NULL") );
            sql = "UPDATE E_CRD SET CRD_FIR_EST="+nuevoEstadoFirma+
                    " WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);
            m_Log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: " + resultado);
            
            m_Log.info(" tEVO.getNumeroExpediente() vale " + tEVO.getNumeroExpediente());
            //si se trata de un Portafirmas lanbide, como se han modificado la funcionalidad, 
            //se debe cambiar el estado del documento original para que aparezca el reenvio del documento
            if (portafirmas != null && "LAN".equals(portafirmas) && 
                    tEVO.getCodigoDocumentoAnterior() != null && !"".equals(tEVO.getCodigoDocumentoAnterior().trim())) {
                sql = "UPDATE E_CRD SET CRD_FIR_EST = 'V' "+
                    " WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodigoDocumentoAnterior();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                resultado = st.executeUpdate(sql);
                m_Log.info("resultado de la acualizacion de la tabla E_CRD del documento Original del expediente " + tEVO.getNumeroExpediente() + " es: " + resultado);
            }

            if ( (resultado >= 0) && (nuevoEstadoFirma!=null) && (nuevoEstadoFirma.equals("'O'")) ) {
                /* Averiguar código plantilla*/
                sql = "SELECT CRD_DOT FROM E_CRD WHERE "+ crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                        crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                        tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                        "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                        tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
                if(m_Log.isDebugEnabled()) m_Log.info(sql);
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    final int codigoPlantilla = rs.getInt(1);
                    rs.close();
                    /* Recuperar usuarios que deben firmar el documento */
                    sql = "SELECT USU_COD FROM E_DOT_FIR WHERE "+  dot_mun + "=" + tEVO.getCodMunicipio() +
                            " AND " + dot_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + dot_tra + "=" +
                            tEVO.getCodTramite()+" AND "+ dot_cod + "=" + codigoPlantilla;
                    Vector codigosUsuariosFirmantes = new Vector();
                    if(m_Log.isDebugEnabled()) m_Log.info(sql);
                    rs = st.executeQuery(sql);
                    while (rs.next()) {
                        codigosUsuariosFirmantes.add(new Integer(rs.getInt(1)));
                    }//while
                    rs.close();
                    //Si hay portafirmas externo añadimos el cliente del portafirmas y el id de firma generado.
                    if(existePortafirmasExterno && !"".equalsIgnoreCase(resultadoPortafirmasExterno)){
                        
                       Long idEstadoPortafirmas = null;
                       String OidDocumento = "";
                       if (portafirmas != null && "LAN".equals(portafirmas)) {
                           
                          Map<String,String> mapa = new HashMap<String,String>();
                          String[] resPortafirmasExterno = resultadoPortafirmasExterno.split("\\$");
                          
                          //Si todo ha ocurrido correctamente devuelve los datos necesatios concatenados con $
                          // 1 OID Documento, 2 Extension Documento, 3 Buzon, 4 Buzon Firma
                          OidDocumento = resPortafirmasExterno[0];
                          mapa.put("OidDocumento", OidDocumento);
                          mapa.put("extension", resPortafirmasExterno[1]);
                          mapa.put("buzon", resPortafirmasExterno[2]);
                          mapa.put("firmaImformante",resPortafirmasExterno[3]);
                          
                          //Se inserta en la tabla estado_portafirmas y estado_portafirmas_h la informacion indicando que se ha enviado al portafirmas la documentacion
                          idEstadoPortafirmas = LanbideEstadoPortafirmasManager.getInstance().insertarEstadoInicialPortafirmas(mapa,con);
  
                        }
                        
                        /* Mandar al portafirmas de cada uno de esos usuarios */
                        sql = "INSERT INTO E_CRD_FIR ("+crd_mun+","+crd_pro+","+crd_eje+","+crd_num+","+crd_tra+","+crd_ocu+","+crd_nud+",FIR_EST,"+usu_cod+
                            ",CRD_COD_PF_EXT,CRD_ID_SOL_PF_EXT";
                        if (portafirmas != null && "LAN".equals(portafirmas)) {
                            sql += ", ESTADO_PORTAFIRMAS";
                            
                        }
                        sql += ") VALUES (";
                        sql += tEVO.getCodMunicipio() + ",'" + tEVO.getCodProcedimiento() + "', " + tEVO.getEjercicio() + ", '" + tEVO.getNumeroExpediente() +
                                "', " + tEVO.getCodTramite() + ", " + tEVO.getOcurrenciaTramite() + ", " + tEVO.getCodDocumento()+
                                ",'O',";
                        sql += "(SELECT ID_USUARIO FROM E_CRD_FIR_FIRMANTES WHERE COD_MUNICIPIO = " + tEVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '" + tEVO.getCodProcedimiento()
                                + "' AND  EJERCICIO = " + tEVO.getEjercicio() + " AND NUM_EXPEDIENTE = '" + tEVO.getNumeroExpediente() + "' AND COD_TRAMITE = " + tEVO.getCodTramite()
                                + " AND COD_OCURRENCIA = " + tEVO.getOcurrenciaTramite() + " AND  COD_DOCUMENTO = " + tEVO.getCodDocumento() + "), ";
                        
                        if (portafirmas != null && "LAN".equals(portafirmas)) {
                            sql += "'" + clientePortafirmasExterno + "','" + OidDocumento + "','" + idEstadoPortafirmas + "' )";
                        } else {
                            sql += "'" + clientePortafirmasExterno + "','" + resultadoPortafirmasExterno + "')";
                        }
                        
                        if(m_Log.isDebugEnabled()) m_Log.info(sql);
                        st.executeUpdate(sql);
                    }else{
                        /* Mandar al portafirmas de cada uno de esos usuarios */
                        sql = "INSERT INTO E_CRD_FIR ("+crd_mun+","+crd_pro+","+crd_eje+","+crd_num+","+crd_tra+","+crd_ocu+","+crd_nud+",FIR_EST,"+usu_cod+") VALUES (";
                        sql += tEVO.getCodMunicipio() + ",'" + tEVO.getCodProcedimiento() + "', " + tEVO.getEjercicio() + ", '" + tEVO.getNumeroExpediente() +
                                "', " + tEVO.getCodTramite() + ", " + tEVO.getOcurrenciaTramite() + ", " + tEVO.getCodDocumento()+
                                ",'O',";
                        for (int i = 0; i < codigosUsuariosFirmantes.size(); i++) {
                            Integer firmante = (Integer)codigosUsuariosFirmantes.elementAt(i);
                            String queryFinal = sql + firmante +")";
                            if(m_Log.isDebugEnabled()) m_Log.info(queryFinal);
                            st.executeUpdate(queryFinal);
                        }//for
                    }//if(existePortafirmasExterno && !"".equalsIgnoreCase(resultadoPortafirmasExterno)) 
                }//if
            }//if
            st.close();
        } catch (SQLException e) {
            resultado = -1;
            m_Log.error("Se ha producido un error de tipo SQLException: " + e.getMessage());
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        } catch (BDException bde) {
            m_Log.error("Se ha producido un error de tipo TramitacionException: " + bde.getMessage());
            resultado = -1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        }catch (Exception ex) {
            m_Log.error("Se ha producido un error de tipo Exception: " + ex.getMessage());
            resultado = -1;
            ex.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + ex.getMessage());
        } finally {
            try {
                if (resultado >= 0) {
                   m_Log.debug("resultado es mayor a 0 y se hace commit de los cambios");
                   if (oad != null && con != null) {
                       oad.finTransaccion(con); 
                   } else {
                       m_Log.debug("AdaptadorSQLBD o Connection son nulos");
                   }
                   
                } else {
                    m_Log.debug("resultado es menor o igual a 0 y se realiza el rollback");
                    oad.rollBack(con);
                }
                
                if (oad != null && con != null) {
                    oad.devolverConexion(con);
                } else {
                    m_Log.debug("AdaptadorSQLBD o Connection son nulos");
                }

            } catch (BDException bde) {
                resultado = -1;
                bde.printStackTrace();
                if(m_Log.isDebugEnabled()) m_Log.error ("TramitacionExpedientesDAO. Exception: " + bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }//try-catch
        }//try-catch
        return resultado;
    }//cambiarEstadoFirmaDocumentoCRD
    /* ******************************************************** */

    public int cambiarEstadoFirmaDocumentoRelacion(TramitacionExpedientesValueObject tEVO, String[] params)
    throws TramitacionException, TechnicalException {

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st = null;
        int resultado = 0;
        String sql;

        try {

            con = oad.getConnection();
            st = con.createStatement();
            oad.inicioTransaccion(con);

            final String nuevoEstadoFirma = ( (tEVO.getEstadoFirma()!=null)?("'"+tEVO.getEstadoFirma()+"'"):("NULL") );

            sql = "UPDATE G_CRD SET CRD_FIR_EST = " + nuevoEstadoFirma +
                    " WHERE CRD_MUN = " + tEVO.getCodMunicipio() + " AND CRD_PRO = '" + tEVO.getCodProcedimiento() + "'" +
                    " AND CRD_EJE = " + tEVO.getEjercicio() + " AND CRD_NUM = '" + tEVO.getNumeroRelacion() + "'" +
                    " AND CRD_TRA = " + tEVO.getCodTramite() + " AND CRD_OCU = " + tEVO.getOcurrenciaTramite() +
                    " AND CRD_NUD = " + tEVO.getCodDocumento();

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);

            if ( (resultado >= 0) && (nuevoEstadoFirma!=null) && (nuevoEstadoFirma.equals("'O'")) ) {
                /* Averiguar código plantilla*/
                sql = "SELECT CRD_DOT FROM G_CRD " +
                        " WHERE CRD_MUN = " + tEVO.getCodMunicipio() + " AND CRD_PRO = '" + tEVO.getCodProcedimiento() + "'" +
                        " AND CRD_EJE = " + tEVO.getEjercicio() + " AND CRD_NUM = '" + tEVO.getNumeroRelacion() + "'" +
                        " AND CRD_TRA = " + tEVO.getCodTramite() + " AND CRD_OCU = " + tEVO.getOcurrenciaTramite() +
                        " AND CRD_NUD = " + tEVO.getCodDocumento();

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    final int codigoPlantilla = rs.getInt(1);
                    rs.close();
                    /* Recuperar usuarios que deben firmar el documento */
                    sql = "SELECT USU_COD FROM E_DOT_FIR" +
                            " WHERE DOT_MUN = " + tEVO.getCodMunicipio() + " AND DOT_PRO = '" + tEVO.getCodProcedimiento() + "'" +
                            " AND DOT_TRA = " + tEVO.getCodTramite() + " AND DOT_COD = " + codigoPlantilla;

                    Vector<Integer> codigosUsuariosFirmantes = new Vector<Integer>();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);
                    while (rs.next()) {
                        codigosUsuariosFirmantes.add(rs.getInt(1));
                    }

                    rs.close();
                    /* Mandar al portafirmas de cada uno de esos usuarios */
                    sql = "INSERT INTO G_CRD_FIR (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD, FIR_EST, USU_COD) " +
                            "VALUES (" + tEVO.getCodMunicipio() + ", '" + tEVO.getCodProcedimiento() + "', " +
                            tEVO.getEjercicio() + ", '" + tEVO.getNumeroRelacion() + "', " + tEVO.getCodTramite() + ", " +
                            tEVO.getOcurrenciaTramite() + ", " + tEVO.getCodDocumento() + ", 'O', ";
                    for (int i = 0; i < codigosUsuariosFirmantes.size(); i++) {
                        Integer firmante = codigosUsuariosFirmantes.elementAt(i);
                        String queryFinal = sql + firmante +")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(queryFinal);
                        st.executeUpdate(queryFinal);
                    }//for
                }//if
            }//if
            st.close();
        } catch (SQLException e) {
            resultado = -1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        } catch (BDException bde) {
            resultado = -1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        } finally {
            try {
                if (resultado >= 0)
                    oad.finTransaccion(con);
                else
                    oad.rollBack(con);
                oad.devolverConexion(con);
            } catch (BDException bde) {
                bde.printStackTrace();
                if(m_Log.isDebugEnabled()) m_Log.debug("TramitacionExpedientesDAO. Exception: " + bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }//try-catch
        }//try-catch
        return resultado;
    }

   

    /**
     * Metodo para tratar los tramites de condicion de Entrada
     * E_TRP
     *
     * @param oad Adaptador SQL para la BBDD.
     * @param con Conexión a la BB.DD.
     * @param tEVO TramitacionExpedientesValueObject Value Object con la informacion de tramitación.
     * @return Vector incluyendo las condiciones de Entrada no cumplidas
     * @throws SQLException Ante cualquier error contra la BBDD.
     */
    private Vector tratarTramCondEntrada(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO)
    throws Exception {

        Vector listaCondicionesEntrada;
        Vector listaCondicionesEntradaNoCumplidas = new Vector();

        try {
            Vector tramitesAIniciar = tEVO.getListaTramitesIniciar();
            listaCondicionesEntrada = getListaCondicionesEntrada(oad, con, tEVO, tramitesAIniciar);
            if (m_Log.isDebugEnabled())
                m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite: " +
                        listaCondicionesEntrada.size());

            if (listaCondicionesEntrada.size() > 0) {
                listaCondicionesEntradaNoCumplidas = comprobarCondicionesEntrada(con, tEVO, listaCondicionesEntrada, null);
            }
            if (m_Log.isDebugEnabled())
                m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite no cumplidas: " +
                        listaCondicionesEntradaNoCumplidas.size());
 
            //int resultado;insertarCondicionesEntrada(oad, con, listaCondicionesEntradaNoCumplidas);
            //if (m_Log.isDebugEnabled()) m_Log.debug("Resultado de Insertar condiciones de entrada no cumplidas: " + resultado);
        }
        catch (Exception e) {
            throw e;
        }

        return listaCondicionesEntradaNoCumplidas;
    }

    /**
     * Metodo para tratar los tramites de condicion de Entrada
     * E_TRP
     *
     * @return Vector con tramites que no cumplen las condiciones de entrada de tipo expresion.
     * @param oad Adaptador SQL de la Base de Datos.
     */
    public Vector tratarTramCondEntradaExpresion(AdaptadorSQL oad,Connection con,
                                         TramitacionExpedientesValueObject tEVO)
            throws TechnicalException, TramitacionException, Exception {

        Vector tramitesnoOK = new Vector();
        Vector listaCondicionesEntradaExpresion;
        Vector tramitesAIniciar = tEVO.getListaTramitesIniciar();

        listaCondicionesEntradaExpresion = getListaCondicionesEntradaExpresion(oad,con,tEVO,tramitesAIniciar);
        if (m_Log.isDebugEnabled())
            m_Log.debug("Numero de Condiciones de Entrada a comporbar de tipo expresión: " +
                    listaCondicionesEntradaExpresion.size());

        // Si hay alguna condición de entrada de tipo expresión.
        if (listaCondicionesEntradaExpresion.size() > 0) {
            try {
                 tramitesnoOK = comprobarCondicionesEntradaExpresion(tEVO.getEstructuraDatosSuplExpediente(), 
                        tEVO.getValoresDatosSuplExpediente(), tEVO.getEstructuraDatosSuplTramites(), 
                        tEVO.getValoresDatosSuplTramites(),  listaCondicionesEntradaExpresion);

            } catch (Exception e) {
                throw e;
            }
        }
        if (m_Log.isDebugEnabled())
            m_Log.debug("Numero de Tramites que no cumplen las condiciones de entrada de tipo expresion: " +
                    tramitesnoOK.size());

        return tramitesnoOK;
    }

    public void finalizarTramite(TramitacionExpedientesValueObject tEVO, 
                                   String[] params)
            throws TramitacionException, TechnicalException, WSException, EjecucionSWException, EjecucionOperacionModuloIntegracionException {

        // No tiene condiciones de salida.

        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado = 0;
       
        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            resultado = TramitesExpedienteDAO.getInstance().finalizarTramite(oad, con, tEVO);
            


            try {
                int codMunicipio = Integer.parseInt(tEVO.getCodMunicipio());
                int codTramite = Integer.parseInt(tEVO.getCodTramite());
                int ocurrencia = Integer.parseInt(tEVO.getOcurrenciaTramite());
                int ejercicio = Integer.parseInt(tEVO.getEjercicio());
                PeticionSWVO peticionSW = new PeticionSWVO(codMunicipio, tEVO.getCodProcedimiento(),
                        codTramite, true, false, false, tEVO.getNumeroExpediente(), ocurrencia, ejercicio, true,params);
                peticionSW.setOrigenLlamada(tEVO.getOrigenLlamada());
                GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                launcher.ejecutarSWTramitacion(peticionSW, oad, con);
            } catch (NoServicioWebDefinidoException e) {
                m_Log.debug("NO HAY NINGUNA LLAMADA A SERVICIO WEB DEFINIDA PARA ESTE TRAMITE");
            }

       
        } catch (SQLException e) {
            resultado = -1;
            
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.finalizarTamite " + e.getMessage());
        } catch (BDException bde) {
            resultado = -1;
            
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.finalizarTamite " + bde.getMessage());
        } catch (FaltaDatoObligatorioException fdoe) {
            resultado = -1;
           
            throw new TramitacionException("EL VALOR DEL CAMPO " + fdoe.getNombreCampo() + " ES OBLIGATORIO PARA LLAMAR " +
                    "AL SERVICIO WEB");
        } catch (EjecucionSWException eswe) {
            resultado = -1;
            throw eswe;
        }catch (EjecucionOperacionModuloIntegracionException eoe){
            resultado = -1;
           
            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
            traductor.setIdi_cod(tEVO.getCodIdiomaUsuario());

            StringBuffer mensaje = new StringBuffer();
            if(eoe.getNombreModulo()!=null && !"".equals(eoe.getCodigoErrorOperacion()) && eoe.getCodigoErrorOperacion()!=null && !"".equals(eoe.getCodigoErrorOperacion())){
                try{
                    ResourceBundle  resource = ResourceBundle.getBundle(eoe.getNombreModulo());
                    if(resource!=null){                        
                        // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                        String nombreMensaje = resource.getString(tEVO.getCodMunicipio() + ConstantesDatos.MODULO_INTEGRACION + eoe.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + eoe.getCodigoErrorOperacion());
                        mensaje.append(resource.getString(nombreMensaje + ConstantesDatos.GUION_BAJO + tEVO.getCodIdiomaUsuario()));
                    }
                }catch(Exception e){
                    m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + eoe.getNombreModulo() + " para la operación "
                            + eoe.getOperacion() + ". Se muestra un mensaje de error genérico.");

                    mensaje.delete(0,mensaje.length());
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion1"));
                    mensaje.append(" ");
                    mensaje.append(eoe.getOperacion());
                    mensaje.append(" ");
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion2"));
                    mensaje.append(eoe.getNombreModulo());
                    mensaje.append(" ");
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion3"));

                }// catch
            }// if

            throw new TramitacionException(mensaje.toString());
        }finally {
            try {
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            } catch (BDException bde) {
                resultado = -1;
                
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: " + bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.finalizarTramite.Fin transaccion y devolver conexion", bde);
            }
        }
  

    }

    public int finalizarExpediente(TramitacionExpedientesValueObject tEVO,  String[] params)
            throws TramitacionException, TechnicalException {

        // Condicion de salida: finalización de expediente.

        m_Log.debug("******************************FINALIZAR EXPEDIENTE");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado = 0;


        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            //Se comprueba si las firmas requeridas se han completado en los documentos de exoediente.
            boolean puedeFinalizar = 
                    ExpedientesDAO.getInstance().firmasPendientesExpediente(oad, con, tEVO);
            
            if (!puedeFinalizar) return -1;
            m_Log.debug("Puede finalizar le expediente? " + puedeFinalizar);

            resultado = TramitesExpedienteDAO.getInstance().finalizarTramite(oad, con, tEVO);


            if ("si".equals(m_ConfigCommon.getString("JSP.Formularios"))) {
                m_Log.debug("Llamando a formularios............");
                FinalizarFormularios finalizarFormularios =
                        new FinalizarFormularios(tEVO.getNumeroExpediente(), tEVO.getCodOrganizacion());
                finalizarFormularios.execute(con);
                m_Log.debug("Llamando exitosa");
            }

            resultado = ExpedientesDAO.getInstance().finalizarExpediente(oad, con, tEVO);//devuelve un int con estado expediente

              
            try {//coje datos vo y se los pasa al sw, hace la llamada al sw
                m_Log.debug("****dentro try de servicio web");
                int codMunicipio = Integer.parseInt(tEVO.getCodMunicipio());
                int codTramite = Integer.parseInt(tEVO.getCodTramite());
                int ocurrencia = Integer.parseInt(tEVO.getOcurrenciaTramite());
                int ejercicio = Integer.parseInt(tEVO.getEjercicio());
                PeticionSWVO peticionSW = new PeticionSWVO(codMunicipio, tEVO.getCodProcedimiento(),
                        codTramite, true, false, false, tEVO.getNumeroExpediente(), ocurrencia, ejercicio, true,params);
                peticionSW.setOrigenLlamada(tEVO.getOrigenLlamada());
                GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                // En websphere se produce un error con una conexión compartida. Por tanto para la llamanda
                // al GestorEjecucionTramitacion se obtiene una nueva conexión.
                /**
                 * Se elimina el cambio del comentario anterior ya que produce problemas
                 * si el servidor de apps es Tomcat, y actualmente no tenemos ningún cliente
                 * que use websphere.
                 */
                launcher.ejecutarSWTramitacion(peticionSW, oad, con);
            } catch (NoServicioWebDefinidoException e) {
                m_Log.debug("NO HAY NINGUNA LLAMADA A SERVICIO WEB DEFINIDA PARA ESTE TRAMITE");
            }
            return resultado;

        } catch (EjecucionSWException eswe) {
            rollBackTransaction(oad, con);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("ERROR SQL AL LLAMAR A LOS SERVICIOS WEB ASOCIADOS AL EXPEDIENTE");
            }
            throw new TramitacionException("ERROR SQL AL LLAMAR A LOS SERVICIOS WEB ASOCIADOS AL EXPEDIENTE (" + eswe.getMensaje() + ")", eswe);
        } catch (SQLException bde) {
            rollBackTransaction(oad, con);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("ERROR DE BASE DE DATOS AL INTENTAR FINALIZAR EL EXPEDIENTE");
            }
            throw new TramitacionException("ERROR BASE DE DATOS AL INTENTAR FINALIZAR EL EXPEDIENTE (" + bde.getMessage() + ")", bde);
        } catch (BDException bde) {
            rollBackTransaction(oad, con);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("ERROR DE BASE DE DATOS AL INTENTAR FINALIZAR EL EXPEDIENTE");
            }
            throw new TramitacionException("ERROR BASE DE DATOS AL INTENTAR FINALIZAR EL EXPEDIENTE (" + bde.getMessage() + ")", bde);
        } catch (TechnicalException te) {
            rollBackTransaction(oad, con);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("ERROR TECNICO AL INTENTAR FINALIZAR EL EXPEDIENTE");
            }
            throw new TramitacionException("ERROR TECNICO AL INTENTAR FINALIZAR EL EXPEDIENTE (" + te.getMessage() + ")", te);
        } catch (InternalErrorException iee) {
            rollBackTransaction(oad, con);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("ERROR SQL AL INTENTAR FINALIZAR LOS FORMULARIOS ASOCIADOS AL EXPEDIENTE");
            }
            throw new TramitacionException("ERROR SQL AL INTENTAR FINALIZAR LOS FORMULARIOS ASOCIADOS AL EXPEDIENTE (" + iee.getMessage() + ")", iee);
        } catch (NotAllowActionException naae) {
            rollBackTransaction(oad, con);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("ERROR SQL AL INTENTAR FINALIZAR LOS FORMULARIOS ASOCIADOS AL EXPEDIENTE");
            }
            throw new TramitacionException("ERROR SQL AL INTENTAR FINALIZAR LOS FORMULARIOS ASOCIADOS AL EXPEDIENTE (" + naae.getMessage() + ")", naae);
        } catch (FaltaDatoObligatorioException fdoe) {
            throw new TramitacionException("EL VALOR DEL CAMPO " + fdoe.getNombreCampo() + " ES OBLIGATORIO PARA LLAMAR " +
                    "AL SERVICIO WEB");
        }catch(EjecucionOperacionModuloIntegracionException e){
            rollBackTransaction(oad, con);
            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
            traductor.setIdi_cod(tEVO.getCodIdiomaUsuario());

            StringBuffer mensaje = new StringBuffer();

            if(e.getNombreModulo()!=null && !"".equals(e.getCodigoErrorOperacion()) && e.getCodigoErrorOperacion()!=null && !"".equals(e.getCodigoErrorOperacion())){
                try{
                    ResourceBundle  resource = ResourceBundle.getBundle(e.getNombreModulo());
                    if(resource!=null){                        
                        // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                        String pro = tEVO.getCodMunicipio() + ConstantesDatos.MODULO_INTEGRACION + e.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + e.getCodigoErrorOperacion();
                        m_Log.debug("propiedad a buscar: " + pro);
                        String nombreMensaje = resource.getString(tEVO.getCodMunicipio() + ConstantesDatos.MODULO_INTEGRACION + e.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + e.getCodigoErrorOperacion());
                        mensaje.append(resource.getString(nombreMensaje + ConstantesDatos.GUION_BAJO + tEVO.getCodIdiomaUsuario()));
                    }
                }catch(Exception ex){
                    m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + e.getNombreModulo() + " para la operación "
                            + e.getOperacion() + ". Se muestra un mensaje de error genérico.");

                    mensaje.delete(0,mensaje.length());
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion1"));
                    mensaje.append(" ");
                    mensaje.append(e.getOperacion());
                    mensaje.append(" ");
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion2"));
                    mensaje.append(e.getNombreModulo());
                    mensaje.append(" ");
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion3"));

                }// catch
            }// if
            
            throw new TramitacionException(mensaje.toString(),e);


        }
        finally {
            try {
            finTransaccion(oad, con);
                oad.devolverConexion(con);
            } catch (BDException ex) {
               ex.printStackTrace();
            }
        }
    }

  public int finalizarExpedienteNoConvencional(TramitacionExpedientesValueObject tEVO,Vector estructura, String[] params)
            throws TramitacionException, TechnicalException {
      
        m_Log.debug("Empezamos finalizarExpedienteNoConvencional");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado=0;
        int resultadoF=0;
       try{
        oad = new AdaptadorSQLBD(params);
        con = oad.getConnection();
        oad.inicioTransaccion(con);
        m_Log.debug("Empezamos finalizarExpedienteNoConvencional->getTramitesExpedienteSinFinalizarTotal:");
        m_Log.debug("Tevo: "+tEVO.toString()+" params: "+params);
        Vector listaTramitesNoFinalizados =TramitesExpedienteDAO.getInstance().getTramitesExpedienteSinFinalizarTotal(tEVO, params);
        for(int i=0;i<listaTramitesNoFinalizados.size();i++) {
                GeneralValueObject g = (GeneralValueObject) listaTramitesNoFinalizados.elementAt(i);
                String codTramite = (String) g.getAtributo("codTramite");
                tEVO.setCodTramite(codTramite);
                String ocurrenciaTramite = (String) g.getAtributo("ocurrenciaTramite");
                tEVO.setOcurrenciaTramite(ocurrenciaTramite);

               // tEVO.setListaCodDocumentosTramite(listaTramitesNoFinalizados)setCodDocumento(codDoc);
               //antes de finalizar el tramite cambiamos el estado de la firma (O=finalizado,null=sin firma,T=pendiente de firma)
                 resultado = TramitesExpedienteDAO.getInstance().cambiarEstadoFirmaDocumentoFinalizado(oad,con,tEVO);

                //se aprovecha finalizar tramite normal
                resultado = TramitesExpedienteDAO.getInstance().finalizarTramiteNoConvencional(oad,con,tEVO);

                 if(resultado > 0) {
                //DESPUES de finalizar tramite tenemos que indicar en la tabla E_TRASIT que se trata de un tramite finalizado
                //de forma no convencional
                 resultado = TramitesExpedienteDAO.getInstance().guardarSitucionTramite(oad,con,tEVO);
                 }
         }
         if ("si".equals(m_ConfigCommon.getString("JSP.Formularios"))) {
                    m_Log.debug("Llamando a formularios............");
                    FinalizarFormularios finalizarFormularios = new FinalizarFormularios(tEVO.getNumero(), tEVO.getCodOrganizacion());
                    finalizarFormularios.execute(con);
                    m_Log.debug("Llamando exitosa");
          }
        m_Log.debug(" LLamamos finalizarExpedienteNoConvencional");
        resultado = ExpedientesDAO.getInstance().finalizarExpedienteNoConvencional(oad,con,tEVO);
         //despues de finalizar expediente guardamos en la tabla E_EXPSIT la situacion del mismo
        //y tambien los motivos de la finalizacion en caso de se por forma no convencional
         if(resultado > 0) {
            //DESPUES de finalizar tramite tenemos que indicar en la tabla E_TRASIT que se trata de un tramite finalizado
            //de forma no convencional
             resultado = TramitesExpedienteDAO.getInstance().guardarSitucionExpediente(oad,con,tEVO);
         }

         if(resultado > 0) {
             //Actualizamos el estado de las firmas de documentos del expediente.
             resultadoF = ExpedientesDAO.getInstance().guardarSitucionFirmasDocumentos(oad,con,tEVO, "O", "N");
         }

         // Liberar las entradas de registro asociadas al expediente
           if(resultado > 0) {
              // Recuperar la lista de Anotaciones asociadas al expediente
               GeneralValueObject gVO =  new GeneralValueObject();
               gVO.setAtributo("numero",tEVO.getNumeroExpediente());
               ArrayList<AsientoFichaExpedienteVO> listaRegistrosExpediente = FichaExpedienteDAO.getInstance().cargaListaAsientosExpediente(gVO, "FLEXIA",params);
               if(listaRegistrosExpediente!= null && !listaRegistrosExpediente.isEmpty()){
                   for (AsientoFichaExpedienteVO asientoFichaExpedienteVO :  listaRegistrosExpediente){
                       m_Log.info("Expediente Anulado :" + tEVO.getNumeroExpediente() + " Liberando Entrada: " + asientoFichaExpedienteVO.getEjercicioAsiento()+"/"+asientoFichaExpedienteVO.getNumeroAsiento());
                       Map<String,String> respuestaLiberarER = liberarEntradaRegistroAlFinalizarExpteNoConvencional(tEVO.getNumeroExpediente(),asientoFichaExpedienteVO.getEjercicioAsiento(),asientoFichaExpedienteVO.getNumeroAsiento().intValue(),con);
                       m_Log.info("Resultado: " + (respuestaLiberarER!= null ? respuestaLiberarER.toString() : "null"));
                   }
               }
           }

       }catch ( Exception e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.reabrirExpediente " + e.getMessage());
        } finally{
            try{
                if (resultado >= 0 && resultadoF >= 0) {
                    oad.finTransaccion(con);
                    oad.devolverConexion(con);
                }else
                    oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.reabrirExpediente.Fin transaccion y devolver conexion", bde);
            }
        }

      return resultado;

    }

   public GeneralValueObject conFinalizarExpedienteNoConvencional(TramitacionExpedientesValueObject tEVO, Connection con)
            throws TramitacionException, TechnicalException {        
       
       String sql = "";
       String from = "";
       String where = "";
       ResultSet rs = null;
       PreparedStatement ps = null;
       int aux = 0;
       
       GeneralValueObject g = new GeneralValueObject();
       try{
        
            String codMunicipio = (String)tEVO.getCodMunicipio();
            String ejercicio =(String)tEVO.getEjercicio();
            String numero = (String)tEVO.getNumeroExpediente();
            boolean expedienteHistorico = false;
            
            m_Log.debug("   ***numero  " + numero);
            m_Log.debug("   ***ejercicio  " + ejercicio);
            
            sql = "SELECT COUNT(*) AS NUM FROM HIST_E_EXP WHERE EXP_NUM=? AND EXP_EJE=? AND EXP_MUN=?";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);            
            ps.setString(1,numero);
            ps.setInt(2,Integer.parseInt(ejercicio));
            ps.setInt(3,Integer.parseInt(codMunicipio));
            
            rs = ps.executeQuery();
            while(rs.next()) { 
                aux = rs.getInt("NUM");
            }
       
            ps.close();
            rs.close();
            if(aux>=1) expedienteHistorico = true;            

            if(!expedienteHistorico) {
                sql = "SELECT * FROM E_EXPSIT WHERE EXPSIT_NUM=? AND EXPSIT_MUN=? AND EXPSIT_EJE=?";
            } else {
                sql = "SELECT * FROM HIST_E_EXPSIT WHERE EXPSIT_NUM=? AND EXPSIT_MUN=? AND EXPSIT_EJE=?";                
            }

            m_Log.debug("TramitacionExpedientesDAO: Sentencia en conFinalizarExpedienteNoConvencional --> " + sql);
            ps = con.prepareStatement(sql);
            ps.setString(1,numero);
            ps.setInt(2, Integer.parseInt(codMunicipio));
            ps.setInt(3, Integer.parseInt(ejercicio));
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                String motivo = rs.getString("EXPSIT_JUST");
                String persona = rs.getString("EXPSIT_AUTORIZA");

                motivo  = StringEscapeUtils.escapeJavaScript(motivo);
                persona = StringEscapeUtils.escapeJavaScript(persona);            
                g.setAtributo("justificacion",motivo);
                g.setAtributo("perAutoriza", persona);             
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }finally{
           try{
               if(rs!=null) rs.close();
               if(ps!=null) ps.close();
               
           }catch(SQLException e){
               m_Log.error("Error al cerrar los recursos asociados a la conexión de BBDD: " + e.getMessage());
           }
        }
        return g;
    }

    public int reabrirExpediente(GeneralValueObject gVO,String[] params)
            throws TramitacionException,TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado=0;

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            String estado= (String) gVO.getAtributo("estadoExpediente");

            resultado = ExpedientesDAO.getInstance().reabrirExpediente(oad,con,gVO);
            if(resultado > 0) {
                //si el estado es 9 es un expediente cerrado por tramitacion
                //si es 1 es un expediente cerrado de forma no convencional
                if("9".equals(estado)){
                    resultado = TramitesExpedienteDAO.getInstance().reabrirUltimoTramite(oad,con,gVO);
                }else{
                    resultado = TramitesExpedienteDAO.getInstance().reabrirTramites(oad,con,gVO);
                }
             
                //Relanzamos las firmas de docs de expediente que se pararon
                TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
                teVO.setCodOrganizacion((String) gVO.getAtributo("codMunicipio"));
                teVO.setCodProcedimiento((String)gVO.getAtributo("codProcedimiento"));
                teVO.setNumero((String) gVO.getAtributo("numero"));
                resultado = ExpedientesDAO.getInstance().guardarSitucionFirmasDocumentos(oad,con,teVO, "N", "O");
             
                if ("si".equals(m_ConfigCommon.getString("JSP.Formularios"))) {
                    m_Log.debug("Llamando a formularios............");
                    ReabrirFormularios reabrirFormularios = new ReabrirFormularios((String) gVO.getAtributo("numero"));
                    reabrirFormularios.execute(con);
                    m_Log.debug("Formularios Reabiertos");
                }

            }
        }catch ( Exception e){
            resultado=-1;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.reabrirExpediente " + e.getMessage());
        } finally{
            try{
                if (resultado >= 0) {
                    oad.finTransaccion(con);
                    oad.devolverConexion(con);
                }else
                    oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.reabrirExpediente.Fin transaccion y devolver conexion", bde);
            }
        }
        return resultado;

    }

    public Vector finalizarConTramites(TramitacionExpedientesValueObject tEVO,  String[] params)
            throws TechnicalException, WSException, EjecucionSWException, TramitacionException {

        Vector devolver = new Vector();
        m_Log.debug("#################### -- FINALIZAR TRAMITE E INICIAR LOS SIGUIENTES -- ####################");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado = 0;

        try {
            // Obtener la conexion e iniciar la transacción.
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            // Finalizar Tramite.
            resultado = TramitesExpedienteDAO.getInstance().finalizarTramite(oad, con, tEVO);
             if(m_Log.isDebugEnabled()) m_Log.debug("resultado:::::"+resultado);
            if (resultado==-999) {
                tEVO.setRespOpcion("yaFinalizado");
            }else  if (resultado==-1) { 
                tEVO.setRespOpcion("sinPermiso");
                if(tEVO.getOrigenLlamada() != null && tEVO.getOrigenLlamada().equals(ConstantesDatos.ORIGEN_LLAMADA_WEB_SERVICE)) {
                    devolver.add(resultado);
                }
            } else if (resultado > 0) {
                if(tEVO.getOrigenLlamada() != null && tEVO.getOrigenLlamada().equals(ConstantesDatos.ORIGEN_LLAMADA_WEB_SERVICE)) {
                    tEVO.setRespOpcion("tramiteFinalizado");
                }
                devolver = finalizacionComunConTramites(tEVO,  oad, con, params);
            }

        } catch (SQLException e) {
            rollBackTransaction(oad, con);
            throw new TechnicalException(e.getMessage());
        } catch (BDException e) {
            rollBackTransaction(oad, con);
            throw new TechnicalException(e.getMessage());
        } catch (EjecucionSWException eswe) {
            rollBackTransaction(oad, con);
            throw eswe;
        } catch (TramitacionException te) {
        	rollBackTransaction(oad, con);
            throw te;
        } finally {
            if (resultado >= 0) finTransaccion(oad, con);
            else rollBackTransaction(oad, con);
            devolverConexion(oad, con);
        }
        m_Log.debug("finalizarcontramites devuelve " + devolver);
        return devolver;
    }

    public Vector finalizarConResolucionConTramites(TramitacionExpedientesValueObject tEVO, String[] params, boolean desFavorable)
            throws TramitacionException, TechnicalException, WSException, EjecucionSWException {

        Vector devolver = new Vector();
        m_Log.debug("############### -- FINALIZAR TRAMITE CON RESOLUCION E INICIAR LOS SIGUIENTES -- ###############");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado = 0;

        try {
            // Obtener la conexion e iniciar la transacción.
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            // Finalizar Tramite.
            resultado = TramitesExpedienteDAO.getInstance().finalizarTramiteConResolucion(oad, con, tEVO, desFavorable);

            if (resultado > 0) devolver = finalizacionComunConTramites(tEVO, oad, con, params);

        } catch (SQLException e) {
            rollBackTransaction(oad, con);
            throw new TechnicalException(e.getMessage());
        } catch (BDException e) {
            rollBackTransaction(oad, con);
            throw new TechnicalException(e.getMessage());
        } catch (EjecucionSWException eswe) {
            rollBackTransaction(oad, con);
            throw eswe;
        } catch (TramitacionException te) {
            rollBackTransaction(oad, con);
            throw te;
        } finally {
            if (resultado >= 0) finTransaccion(oad, con);
            else rollBackTransaction(oad, con);
            devolverConexion(oad, con);
        }
        m_Log.debug("finalizarcontramites devuelve " + devolver);
        return devolver;
    }

    public Vector finalizarConPreguntaConTramites(TramitacionExpedientesValueObject tEVO, String[] params, boolean desFavorable)
            throws TramitacionException, TechnicalException, WSException, EjecucionSWException {

        Vector devolver = new Vector();
        m_Log.debug("############### -- FINALIZAR TRAMITE CON PREGUNTA E INICIAR LOS SIGUIENTES -- ###############");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado = 0;

        try {
            // Obtener la conexion e iniciar la transacción.
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            // Finalizar Tramite.
            resultado = TramitesExpedienteDAO.getInstance().finalizarTramite(oad, con, tEVO);
            if (resultado==-999) {
                tEVO.setRespOpcion("yaFinalizado");
            } else if (resultado > 0) devolver = finalizacionComunConTramites(tEVO,  oad, con, params);

        } catch (SQLException e) {
            rollBackTransaction(oad, con);
            throw new TechnicalException(e.getMessage());
        } catch (BDException e) {
            rollBackTransaction(oad, con);
            throw new TechnicalException(e.getMessage());
        } catch (EjecucionSWException eswe) {
            rollBackTransaction(oad, con);
            throw eswe;
        } catch (TramitacionException te) {
            rollBackTransaction(oad, con);
            throw te;
        } finally {
            if (resultado >= 0) finTransaccion(oad, con);
            else rollBackTransaction(oad, con);
            devolverConexion(oad, con);
        }
        m_Log.debug("finalizarcontramites devuelve " + devolver);
        return devolver;
    }

    private Vector finalizacionComunConTramites(TramitacionExpedientesValueObject tEVO, 
                                                AdaptadorSQLBD abd, Connection con, String[] paramsBD)
            throws TramitacionException, WSException, EjecucionSWException {

        // Condicion de salida: lista de tramites.
        Vector<Object> devolver = new Vector<Object>();
     

        try {
            String codUnidadTramitadoraAnterior = tEVO.getCodUnidadTramitadoraTram();

            

            try { 
                int codMunicipio = Integer.parseInt(tEVO.getCodMunicipio());
                int codTramite = Integer.parseInt(tEVO.getCodTramite());
                int ocurrencia = Integer.parseInt(tEVO.getOcurrenciaTramite());
                int ejercicio = Integer.parseInt(tEVO.getEjercicio());
                PeticionSWVO peticionSW = new PeticionSWVO(codMunicipio, tEVO.getCodProcedimiento(),
                        codTramite, true, false,false, tEVO.getNumeroExpediente(), ocurrencia, ejercicio, true,paramsBD);      
                peticionSW.setOrigenLlamada(tEVO.getOrigenLlamada());
                GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                launcher.ejecutarSWTramitacion(peticionSW,abd, con);
            } catch (NoServicioWebDefinidoException e) {
                m_Log.debug("NO HAY NINGUN SERVICIO WEB DEFINIDO PARA LANZAR AL FINALIZAR ESTE TRAMITE");
            }



            
            Vector tramitesAIniciar = tEVO.getListaTramitesIniciar();
            if (tramitesAIniciar.size() > 0) {


                /** SE GUARDAN LOS TRÁMITES A ABRIR EN LA TABLA DE ORIGEN DE TRÁMITES CON ORIGEN EN EL TRÁMITE QUE SE AVANZA  */
                boolean enListaOrigen = this.guardarEnListaTramitesDeOrigen(tramitesAIniciar,tEVO, con);
                if(enListaOrigen)
                    m_Log.debug(" Se han guardado los trámites en la lista de trámites de origen ");
                else
                    m_Log.debug(" No se han guardado los trámites en la lista de trámites de origen ");
                /*** ****/


               

                    //Compruebo los tramites q tienen como condicion de entrada una expresión, y no la cumplen.
                Vector listaCondExpresionNoOk =
                        tratarTramCondEntradaExpresion(abd, con, tEVO);

                Vector listaCondEstadoTramiteNoOk = tratarTramCondEntrada(abd, con, tEVO);
                
                Vector listaCondFirmaDocNoOk = tratarTramCondEntradaFirmaDoc(con, tEVO);

                // Agrupamos las condiciones de entrada no cumplidas en un unico vector.
                for (int ii = 0; ii < listaCondEstadoTramiteNoOk.size(); ii++) {
                    GeneralValueObject condEstadoTramiteGVO = (GeneralValueObject) listaCondEstadoTramiteNoOk.get(ii);
                    String codTramCondEstado = (String) condEstadoTramiteGVO.getAtributo("codTramite");
                    String descTramCondEstado = (String) condEstadoTramiteGVO.getAtributo("descTramite");
                    String numeroExpediente = (String) condEstadoTramiteGVO.getAtributo("numeroExpediente");
                    String descTramiteCond = (String) condEstadoTramiteGVO.getAtributo("nombreTramiteCondicionEntrada");
                    String estadoTramiteCond = (String) condEstadoTramiteGVO.getAtributo("estadoCondicionEntrada");
                    GeneralValueObject condNoCumplidaGVO = new GeneralValueObject();
                    condNoCumplidaGVO.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condNoCumplidaGVO.setAtributo("descTramite", descTramiteCond);
                    condNoCumplidaGVO.setAtributo("estadoTramite", estadoTramiteCond);

                    boolean yaTratado = false;
                    for (int jj = 0; jj < listaCondExpresionNoOk.size(); jj++) {
                        GeneralValueObject condExpresionGVO = (GeneralValueObject) listaCondExpresionNoOk.get(jj);
                        String codTramCondExpresion = (String) condExpresionGVO.getAtributo("codTramite");

                        if (codTramCondEstado.equals(codTramCondExpresion)) {
                            Vector listaCondiciones = (Vector) condExpresionGVO.getAtributo("listaCondiciones");
                            listaCondiciones.add(condNoCumplidaGVO);
                            condExpresionGVO.setAtributo("listaCondiciones", listaCondiciones);
                            listaCondExpresionNoOk.setElementAt(condExpresionGVO, jj);
                            yaTratado = true;
                        }
                    }

                    if (!yaTratado) {
                        GeneralValueObject nuevoTramiteNoIniciado = new GeneralValueObject();
                        nuevoTramiteNoIniciado.setAtributo("codTramite", codTramCondEstado);
                        nuevoTramiteNoIniciado.setAtributo("numeroExpediente", numeroExpediente);
                        nuevoTramiteNoIniciado.setAtributo("descTramite", descTramCondEstado);
                        Vector listaCondiciones = new Vector();
                        listaCondiciones.add(condNoCumplidaGVO);
                        nuevoTramiteNoIniciado.setAtributo("listaCondiciones", listaCondiciones);
                        listaCondExpresionNoOk.add(nuevoTramiteNoIniciado);
                    }

                }
                for (int ii = 0; ii < listaCondFirmaDocNoOk.size(); ii++) {
                    GeneralValueObject condFirmaDocGVO = (GeneralValueObject) listaCondFirmaDocNoOk.get(ii);
                    String codTramCondDoc = (String) condFirmaDocGVO.getAtributo("codTramite");
                    String numeroExpediente = (String) condFirmaDocGVO.getAtributo("numeroExpediente");
                    String descTramCondDoc = (String) condFirmaDocGVO.getAtributo("descTramite");
                    GeneralValueObject condNoCumplidaGVO = (GeneralValueObject)((Vector)condFirmaDocGVO.getAtributo("listaCondiciones")).get(0);
                            
                    boolean yaTratado = false;
                    for (int jj = 0; jj < listaCondExpresionNoOk.size(); jj++) {
                        GeneralValueObject condExpresionGVO = (GeneralValueObject) listaCondExpresionNoOk.get(jj);
                        String codTramCondExpresion = (String) condExpresionGVO.getAtributo("codTramite");

                        if (codTramCondDoc.equals(codTramCondExpresion)) {
                            Vector listaCondiciones = (Vector) condExpresionGVO.getAtributo("listaCondiciones");
                            listaCondiciones.add(condNoCumplidaGVO);
                            condExpresionGVO.setAtributo("listaCondiciones", listaCondiciones);
                            listaCondExpresionNoOk.setElementAt(condExpresionGVO, jj);
                            yaTratado = true;
                        }
                    }

                    if (!yaTratado) {
                        GeneralValueObject nuevoTramiteNoIniciado = new GeneralValueObject();
                        nuevoTramiteNoIniciado.setAtributo("codTramite", codTramCondDoc);
                        nuevoTramiteNoIniciado.setAtributo("numeroExpediente", numeroExpediente);
                        nuevoTramiteNoIniciado.setAtributo("descTramite", descTramCondDoc);
                        Vector listaCondiciones = new Vector();
                        listaCondiciones.add(condNoCumplidaGVO);
                        nuevoTramiteNoIniciado.setAtributo("listaCondiciones", listaCondiciones);
                        listaCondExpresionNoOk.add(nuevoTramiteNoIniciado);
                    }

                }                

                m_Log.debug("El numero total de tramites que no se iniciaran es: " + listaCondExpresionNoOk.size());
                devolver = listaCondExpresionNoOk;

                if (tramitesAIniciar != null) {
                    if (tramitesAIniciar.size() > 0) {
                        for (int i = 0; i < tramitesAIniciar.size(); i++) {
                            TramitacionExpedientesValueObject t = (TramitacionExpedientesValueObject) tramitesAIniciar.elementAt(i);
                            rellenarDatosComunes(tEVO, t);

                            // Comprobar las condiciones de Entrada de tipo Estado de Tramite.
                            String iniciar = "si";
                            for (int m = 0; m < listaCondExpresionNoOk.size(); m++) {
                                GeneralValueObject g = (GeneralValueObject) listaCondExpresionNoOk.elementAt(m);
                                String codTramiteNoIniciar = (String) g.getAtributo("codTramite");
                                if (codTramiteNoIniciar.equals(t.getCodigoTramiteFlujoSalida())) iniciar = "no";
                            }

                            if (iniciar.equals("si")) {
                                t.setParamsBD(paramsBD);
                                int resultado = TramitesExpedienteDAO.getInstance().iniciarTramite(abd, con, t, codUnidadTramitadoraAnterior);
                                if (resultado > 0) {
                                    for (int j = 0; j < t.getListaEMailsAlIniciar().size(); j++) {
                                        tEVO.getListaEMailsAlIniciar().addElement(t.getListaEMailsAlIniciar().elementAt(j));
                                    }
                                    tEVO.setUnidadTramitadoraTramiteIniciado(t.getUnidadTramitadoraTramiteIniciado());

                                    t.setNumeroExpediente(t.getNumero());
                                   
                                    inicioDeTramite(abd, con, tEVO, new Vector<TramitacionExpedientesValueObject>());

                                  
                                }
                            }
                        }
                    }
                }
            }


        } catch (EjecucionSWException eswe) {
           throw eswe;
        } catch (WSException wse) {
           if (wse.isMandatoryExecution()) {
                wse.printStackTrace();
           }
           throw wse;
        }catch ( TramitacionException te){
            m_Log.error("TramitacionExpedientesDAO. Exception: "+ te.getMessage());
            te.printStackTrace();
            throw te;
        } catch (SQLException e) {
            throw new TramitacionException(e.getMessage());
        } catch (BDException e) {
            throw new TramitacionException(e.getMessage());
        } catch (TechnicalException e) {
            throw new TramitacionException(e.getMessage());
        } catch (FaltaDatoObligatorioException fdoe) {
            throw new TramitacionException("EL VALOR DEL CAMPO " + fdoe.getNombreCampo() + " ES OBLIGATORIO PARA LLAMAR " +
                    "AL SERVICIO WEB");
        } catch (EjecucionOperacionModuloIntegracionException eoe){
            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
            traductor.setIdi_cod(tEVO.getCodIdiomaUsuario());

            StringBuffer mensaje = new StringBuffer();

            if(eoe.getNombreModulo()!=null && !"".equals(eoe.getCodigoErrorOperacion()) && eoe.getCodigoErrorOperacion()!=null && !"".equals(eoe.getCodigoErrorOperacion())){
                try{
                    ResourceBundle  resource = ResourceBundle.getBundle(eoe.getNombreModulo());
                    if(resource!=null){                        
                        // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                        String nombreMensaje = resource.getString(tEVO.getCodMunicipio() + ConstantesDatos.MODULO_INTEGRACION + eoe.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + eoe.getCodigoErrorOperacion());                        
                        mensaje.append(resource.getString(nombreMensaje + ConstantesDatos.GUION_BAJO + tEVO.getCodIdiomaUsuario()));
                    }
                }catch(Exception e){
                    m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + eoe.getNombreModulo() + " para la operación "
                            + eoe.getOperacion() + ". Se muestra un mensaje de error genérico.");

                   
                    mensaje.delete(0,mensaje.length());
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion1"));
                    mensaje.append(" ");
                    mensaje.append(eoe.getOperacion());
                    mensaje.append(" ");
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion2"));
                    mensaje.append(eoe.getNombreModulo());
                    mensaje.append(" ");
                    mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion3"));
                    mensaje.append(" ");
                    if(!"".equals(eoe.getCodigoErrorOperacion())&&(eoe.getCodigoErrorOperacion()!=null)){
                        mensaje.append("[");
                        mensaje.append(eoe.getCodigoErrorOperacion());
                        mensaje.append("]");
                    }
                    
                }// catch                
            }// if

            throw new TramitacionException(mensaje.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new TramitacionException(e.getMessage());
        }

        m_Log.debug("finalizarcontramites devuelve " + devolver);
        return devolver;
    }




    /*
      * Elimina del vector de trámites de condiciones de inicio aquellos tramites
      * que ya se encuentran en el vector de trámites pendientes.
      * Esto es porque si un trámite que es pendiente, es tambien trámite de inicio,
      * se intentaria iniciar dos veces, lo cual no sería correcto
      * @param listaTramPtes Lista de tramites pendientes
      * @param listaTramIniciar Lista de tramites de condiciones de inicio
      * @return Vector con los tramites de las condiciones de inicio una vez eliminados
      * aquellos tramites coincidentes
      */
    


    public Vector iniciarTramitesManual(TramitacionExpedientesValueObject tEVO,String[] params)
            throws TramitacionException,TechnicalException,Exception {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        int resultado=0;
        Vector devolver = new Vector();

        try{
             oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            Vector tramitesAIniciar = (Vector) tEVO.getListaTramitesIniciar();
            oad.inicioTransaccion(con);                     
            tEVO.setParamsBD(params);
            devolver = inicioDeTramite(oad,con,tEVO,tramitesAIniciar);
            //Si lanzamos un tramite manual si se devuelve algún resultado es que algo no se cumple y no se ha iniciado el trámite manual
            if(devolver.size()==0)  this.guardarTramiteManualEnListaTramitesOrigen(tEVO, con);
            //devolver = inicioDeTramiteManual(oad,con,tEVO,tramitesAIniciar);
            resultado = 0;
            
        }catch ( SQLException e){
            resultado=-1;
            devolver = null;
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.iniciarTamiteManual " + e.getMessage());
        }catch ( BDException bde){
            resultado=-1;
            devolver = null;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.iniciarTamiteManual " + bde.getMessage());
        }catch (Exception e){
            resultado=-1;
            devolver = null;
            throw e;
        } finally{
            try{
                if (resultado >= 0) oad.finTransaccion(con);
                else oad.rollBack(con);
                oad.devolverConexion(con);

            }catch(BDException bde) {
                resultado=-1;
                devolver = null;
                bde.printStackTrace();
                m_Log.error("TramitacionExpedientesDAO. Exception: "+ bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.iniciarTamiteManual.Fin transaccion y devolver conexion", bde);
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("antes de devolver el vector en iniciarTramitesManual la longitud del vector es : " + devolver.size());

        return devolver;
    }

    public Vector cargaEstructuraDatosSuplementarios(TramitacionExpedientesValueObject tEVO, AdaptadorSQL oad, Connection con) throws TechnicalException {
        Statement st = null, st3 = null;
        ResultSet rs = null, rs3 = null;
        String sql = "", sql3 = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();
        
        String desdeJsp=tEVO.getDesdeJsp();
        String numero = tEVO.getNumeroExpediente();        
        String campoActivo = "";

        try{
            st = con.createStatement();

            from = tca_cod + "," + tca_des + "," + tca_plt + "," + plt_url + "," + tca_tda + "," +
                    tca_tam + "," + tca_mas + "," + tca_obl + "," + tca_nor + "," + tca_rot + "," + tca_vis + "," + tca_activo+ ""
                    + ",TCA_BLOQ, PLAZO_AVISO, PERIODO_PLAZO,PCA_GROUP ";
            where = tca_mun + " = " + tEVO.getCodMunicipio() + " AND " + tca_pro + " = '" +
                    tEVO.getCodProcedimiento() + "' AND " + tca_tra + "=" + tEVO.getCodTramite();
            //" AND tca_activo = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND TCA_OCULTO='NO'";
            String[] join = new String[5];
            join[0] = "E_TCA";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join[4] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"9","9"};
            sql += oad.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(tca_cod);
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(tca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(tca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(tca_tda);
                eC.setCodTipoDato(codTipoDato);
                String tamano = rs.getString(tca_tam);
                eC.setTamano(tamano);
                String mascara = rs.getString(tca_mas);
                eC.setMascara(mascara);
                String obligatorio = rs.getString(tca_obl);
                eC.setObligatorio(obligatorio);
                String numeroOrden = rs.getString(tca_nor);
                eC.setNumeroOrden(numeroOrden);
                String rotulo = rs.getString(tca_rot);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                String bloqueado = rs.getString("TCA_BLOQ");
                eC.setBloqueado(bloqueado);
                //PLAZO_AVISO, PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                eC.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                eC.setCheckPlazoFecha(checkPlazoFecha);
                
                eC.setRotulo(rotulo);
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                eC.setCodTramite(tEVO.getCodTramite());
                eC.setOcurrencia(tEVO.getOcurrenciaTramite());
                String agrupacionCampo = rs.getString("PCA_GROUP");
                if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo)){
                    eC.setCodAgrupacion(agrupacionCampo);
                }else{
                    eC.setCodAgrupacion("DEF");
                }//if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo))
                //Campos de tipo fecha
                 if (("3").equals(codTipoDato)){
                     m_Log.debug("TIPO DE DATO FECHA...");
                     m_Log.debug("CodCampo::" + codCampo);
                     st3 = con.createStatement();
                     sql3 = "SELECT PLAZO_ACTIVADO FROM  E_TFET WHERE TFET_COD ='"+
                            codCampo + "' AND TFET_NUM='"+
                            numero + "'";
                     rs3 = st3.executeQuery(sql3);
                     if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
                     while (rs3.next()) {
                        campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                        m_Log.debug("campoActivo:::" + campoActivo);
                        eC.setCampoActivo(campoActivo);
                    }
                    SigpGeneralOperations.closeStatement(st3);
                    SigpGeneralOperations.closeResultSet(rs3);
                }
                
                lista.addElement(eC);
            }
            rs.close();
            st.close();
        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            return lista;
        }
    }
    
    public Vector cargaEstructuraAgrupaciones(TramitacionExpedientesValueObject tEVO, String[] params) throws TechnicalException{
        if(m_Log.isDebugEnabled()) m_Log.debug("cargaEstructuraAgrupaciones() : BEGIN");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Vector lista = new Vector();
        try{
            //Creamos el grupo por defecto en la lista de agrupaciones
            DefinicionAgrupacionCamposValueObject dacdfVO = new DefinicionAgrupacionCamposValueObject();
                dacdfVO.setCodAgrupacion("DEF");
                dacdfVO.setDescAgrupacion("");
                dacdfVO.setOrdenAgrupacion(0);
            lista.add(dacdfVO);
            
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
                        
            String sql = "Select TCA_ID_GROUP, TCA_DESC_GROUP, TCA_ORDER_GROUP, TCA_ACTIVE "
                    + " From E_TCA_GROUP "
                    + " Where TCA_PRO = '" + tEVO.getCodProcedimiento() + "'"
                    + " And TCA_TRA = " + tEVO.getCodTramite()
                    + " And TCA_ACTIVE = 'SI' "
                    + " Order by TCA_ORDER_GROUP ASC";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            while(rs.next()){
                DefinicionAgrupacionCamposValueObject agrupacion = new DefinicionAgrupacionCamposValueObject();
                    agrupacion.setCodAgrupacion(rs.getString("TCA_ID_GROUP"));
                    agrupacion.setDescAgrupacion(rs.getString("TCA_DESC_GROUP"));
                    agrupacion.setAgrupacionActiva(rs.getString("TCA_ACTIVE"));
                    agrupacion.setOrdenAgrupacion(rs.getInt("TCA_ORDER_GROUP"));
                lista.add(agrupacion);
            }
            
            
        }catch (Exception e){
            m_Log.error("Se ha producido un error recuperando la lista de agrupaciones de los campos suplementarios de los tramites " + e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(con!=null) con.close();
            }catch(SQLException e) {
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }//try-catch
            
            if(m_Log.isDebugEnabled()) m_Log.debug("cargaEstructuraAgrupaciones() : END");
            return lista;
        }//try-catch
    }//cargaEstructuraAgrupaciones

    public Vector cargaEstructuraDatosSuplementarios(TramitacionExpedientesValueObject tEVO, String[] params)
            throws TechnicalException {

        m_Log.info("**************------------->cargaEstructuraDatosSuplementarios<----------------------*****************");
        AdaptadorSQLBD oad = null;
        Connection con = null,con2 = null;
        PreparedStatement st = null, st2 = null, st3 = null;
        ResultSet rs = null, rs2 = null, rs3 = null;
        String sql3 = "";
        Vector lista = new Vector();
        String numero = tEVO.getNumeroExpediente();        
        String campoActivo = "";

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; // Inicio transacción

            String desdeJsp=tEVO.getDesdeJsp();

            String from = tca_cod + ", " + tca_des + ", " + tca_plt + ", " + plt_url + ", " + tca_tda + ", " +
                    tca_tam + ", " + tca_mas + ", " + tca_obl + ", " + tca_nor + ", " + tca_rot + ", " +
                    tca_vis + ", " + tca_activo + ", " + tca_desplegable+ ", TCA_BLOQ, PLAZO_AVISO, PERIODO_PLAZO, PCA_GROUP, TCA_POS_X, TCA_POS_Y";
            String where = tca_mun + " =? AND " + tca_pro + " = ? AND " + tca_tra + " = ? AND " +
                    tca_activo + " = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND TCA_OCULTO='NO'";
            String[] join = new String[5];
            join[0] = "E_TCA";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join[4] = "false";

            String sql = oad.join(from,where,join);
            String parametros[] = {"9","9"};
            sql += oad.orderUnion(parametros);
            //st = con.createStatement();
            st = con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(tEVO.getCodMunicipio()));
            st.setString(2, tEVO.getCodProcedimiento());
            st.setInt(3, Integer.parseInt(tEVO.getCodTramite()));
           
            rs = st.executeQuery();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          
            
            
            Hashtable<String, ArrayList<String>>  CACHE_CAMPOS_DESPLEGABLES = new Hashtable<String,ArrayList<String>>();            
            
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(tca_cod);
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(tca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(tca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(tca_tda);
                eC.setCodTipoDato(codTipoDato);
                String tamano = rs.getString(tca_tam);
                eC.setTamano(tamano);
                String mascara = rs.getString(tca_mas);
                eC.setMascara(mascara);
                String obligatorio = rs.getString(tca_obl);
                eC.setObligatorio(obligatorio);
                String numeroOrden = rs.getString(tca_nor);
                eC.setNumeroOrden(numeroOrden);
                String rotulo = rs.getString(tca_rot);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                 String bloqueado = rs.getString("TCA_BLOQ");
                eC.setBloqueado(bloqueado);
                //PLAZO_AVISO, PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                eC.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                eC.setCheckPlazoFecha(checkPlazoFecha);
                eC.setRotulo(rotulo);
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String desplegable = "";
                eC.setCodTramite(tEVO.getCodTramite());
                eC.setOcurrencia(tEVO.getOcurrenciaTramite());
                try {
                    //Campos de tipo fecha
                     if (("3").equals(codTipoDato)){
                         m_Log.debug("TIPO DE DATO FECHA...");
                         m_Log.debug("CodCampo::" + codCampo);
                        
                         sql3 = "SELECT PLAZO_ACTIVADO FROM  E_TFET WHERE TFET_COD =? AND TFET_NUM=?";
                               
                        st3 = con.prepareStatement(sql3);

                        st3.setString(1, codCampo);
                        st3.setString(2, numero);
                       
                        rs3 = st3.executeQuery();
                        
                         if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
                         while (rs3.next()) {
                            campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                            m_Log.debug("campoActivo:::" + campoActivo);
                            eC.setCampoActivo(campoActivo);
                        }
                        SigpGeneralOperations.closeStatement(st3);
                        SigpGeneralOperations.closeResultSet(rs3);
                    }
                    if ( !rs.getString(tca_desplegable).equals(null)) {
                        desplegable = rs.getString(tca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        
                        
                            ArrayList<String> CAMPO_DESPLEGABLE = CACHE_CAMPOS_DESPLEGABLES.get(desplegable);
                            if(CAMPO_DESPLEGABLE!=null){                                
                                
                                for(int j=0;j<CAMPO_DESPLEGABLE.size();j++){                                    
                                    String dato = CAMPO_DESPLEGABLE.get(j);
                                    if(dato!=null){
                                        String[] datos = dato.split("-");
                                        listaCod.add("'" + datos[0] + "'");
                                        listaDesc.add("'" +datos[1] + "'");
                                        listaEstado.add("'"+datos[2]+"'");
                                    }                                    
                                }
                                
                                eC.setListaCodDesplegable(listaCod);
                                eC.setListaDescDesplegable(listaDesc);                
                                eC.setListaEstadoValorDesplegable(listaEstado);
                            }else{ 
                        
                        
                                con2 = oad.getConnection();
                                //Vector listaCod = new Vector();
                                //Vector listaDesc = new Vector();
                                String sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", " + des_val_estado +" FROM E_DES_VAL WHERE " +
                                       des_val_campo +"=? ORDER BY " + des_val_desc + " ASC";

                                st2 = con2.prepareStatement(sql2);
                                if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                                
                                st2.setString(1, desplegable);                                
                                rs2 = st2.executeQuery();

                                ArrayList<String> valoresDesplegable = new ArrayList<String>();
                                while (rs2.next()) {
                                    String codigoValor = rs2.getString(des_val_cod);
                                    String descValor = rs2.getString(des_val_desc);
                                    String estadoValor=rs2.getString(des_val_estado);

                                    listaCod.addElement("'" + codigoValor + "'");
                                    listaDesc.addElement("'" + descValor + "'");
                                    listaEstado.addElement("'"+estadoValor+"'");

                                    valoresDesplegable.add(codigoValor + "-" + descValor+"-"+estadoValor);

                                }// while
                                CACHE_CAMPOS_DESPLEGABLES.put(desplegable,valoresDesplegable);

                                rs2.close();
                                st2.close();
                                eC.setListaCodDesplegable(listaCod);
                                eC.setListaDescDesplegable(listaDesc);
                                 eC.setListaEstadoValorDesplegable(listaEstado);
                                oad.devolverConexion(con2);
                            }
                       
                            
                            
                          
                    }
                } catch (NullPointerException e){
                }
                String agrupacionCampo = rs.getString("PCA_GROUP");
                if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo)){
                    eC.setCodAgrupacion(agrupacionCampo);
                }else{
                    eC.setCodAgrupacion("DEF");
                }//if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo))
                if(codTipoDato != null && !"".equalsIgnoreCase(codTipoDato) && ("1".equalsIgnoreCase(codTipoDato) 
                                || "3".equalsIgnoreCase(codTipoDato)
                                || "8".equalsIgnoreCase(codTipoDato) 
                                || "9".equalsIgnoreCase(codTipoDato))){
                    eC.setTamanoVista("50");
                }else{
                    eC.setTamanoVista("100");
                }//
                String posX = String.valueOf(rs.getInt("TCA_POS_X"));
                String posY = String.valueOf(rs.getInt("TCA_POS_Y"));                
                if((posX != null) && (posY != null)){
                    eC.setPosicionar(true);
                }//if((posX != null) && (posY != null))
                eC.setPosX(posX);
                eC.setPosY(posY);
                lista.addElement(eC);
            }
            rs.close();
            st.close();
        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            try{
                oad.finTransaccion(con);
                oad.devolverConexion(con);
            }catch(BDException bde) {
                bde.getMensaje();
            }
            return lista;
        }
    }
    
    public Boolean cargarVista(TramitacionExpedientesValueObject tVO, String[] params) throws TechnicalException{
        if(m_Log.isDebugEnabled()) m_Log.debug("cargarVista() : BEGIN");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Boolean cargar = false;
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            String codProcedimiento = tVO.getCodProcedimiento();
            sql = "Select TCA_COD from E_TCA where TCA_PRO = '" + codProcedimiento + "' and TCA_ACTIVO = 'SI' AND TCA_OCULTO <> 'SI' and TCA_POS_X is null and "
                    + " TCA_TRA = " + tVO.getCodTramite(); 
            if(m_Log.isDebugEnabled()) m_Log.debug("sql =" + sql);
            rs = st.executeQuery(sql);
            if(!rs.next()){
                cargar = true;
            }//if(rs.next())
        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la base de datos: " + e.getMessage());
            }            
            
            if(m_Log.isDebugEnabled()) m_Log.debug("cargarVista() : END");
            return cargar;
        }//try-catch
    }//cargarVista

    public Vector cargaConsultaCamposSuplementarios(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null, rs3=null;
        Statement st3 = null;
        String sql = "", sql2 = "", sql3="";
        String from = "";
        String where = "";
        String campoActivo = "";
        Vector lista = new Vector();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; // Inicio transacción

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String desdeJsp = (String)gVO.getAtributo("desdeJsp");
            String consultaCampos = (String)gVO.getAtributo("consultaCampos");
            String numero = (String)gVO.getAtributo("numero");

            from = tca_cod + "," + tca_des + "," + tca_plt + "," + plt_url + "," + tca_tda + "," +
                    tca_tam + "," + tca_mas + "," + tca_obl + "," + tca_nor + "," + tca_rot + "," +
                    tca_vis + "," + tca_activo + "," + tca_tra + "," + tml_valor + "," + tca_desplegable+",TCA_BLOQ, PLAZO_AVISO, PERIODO_PLAZO";
            where = tca_mun + " = " + codMunicipio + " AND " + tca_pro + " = '" + codProcedimiento +
                    "' AND " + tca_vis + " = 'S' AND " + tca_activo + " = 'SI'";
            where += " AND e_tca.tca_mun =e_tml.tml_mun AND e_tca.tca_pro=e_tml.tml_pro and e_tca.tca_tra= e_tml.tml_tra";
            // + "' AND " + tca_tra + "=" + tEVO.getCodTramite();
            //" AND tca_activo = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND TCA_OCULTO='NO'";
            String[] join = new String[5];
            join[0] = "E_TML, E_TCA";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join[4] = "false";

            sql = oad.join(from,where,join);
            //String parametros[] = {"9","9"};
            //sql += oad.orderUnion(parametros);
            sql += " ORDER BY " + tca_tra + "," + tca_nor;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(tca_cod);
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(tca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(tca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(tca_tda);
                eC.setCodTipoDato(codTipoDato);
                String tamano = rs.getString(tca_tam);
                eC.setTamano(tamano);
                String mascara = rs.getString(tca_mas);
                eC.setMascara(mascara);
                //String obligatorio = rs.getString(tca_obl);
                eC.setObligatorio("no"); // Ponemos para que no cambie el tipo a mostrar en la pagina
                String numeroOrden = rs.getString(tca_nor);
                eC.setNumeroOrden(numeroOrden);
                String rotulo = rs.getString(tca_rot);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                String bloqueado = rs.getString("TCA_BLOQ");                
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
                //PLAZO_AVISO, PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                eC.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                eC.setCheckPlazoFecha(checkPlazoFecha);
                eC.setRotulo(rotulo);
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String codigoTramite = rs.getString(tca_tra);
                eC.setCodTramite(codigoTramite);
                String descripcionTramite = rs.getString(tml_valor);
                eC.setDescripcionTramite(descripcionTramite);
                String desplegable = "";
                try {
                    //Campos de tipo fecha
                     if (("3").equals(codTipoDato)){
                         m_Log.debug("TIPO DE DATO FECHA...");
                         m_Log.debug("CodCampo::" + codCampo);
                         st3 = con.createStatement();
                         sql3 = "SELECT PLAZO_ACTIVADO FROM  E_TFET WHERE TFET_COD ='"+
                                codCampo + "' AND TFET_NUM='"+
                                numero + "'";
                         rs3 = st3.executeQuery(sql3);
                         if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
                         while (rs3.next()) {
                            campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                            m_Log.debug("campoActivo:::" + campoActivo);
                            eC.setCampoActivo(campoActivo);
                        }
                        SigpGeneralOperations.closeStatement(st3);
                        SigpGeneralOperations.closeResultSet(rs3);
                    }
                    if ( !rs.getString(tca_desplegable).equals(null)) {
                        desplegable = rs.getString(tca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", " + des_val_estado + " FROM E_DES_VAL WHERE " +
                               des_val_campo +"='"+ desplegable + "'";
                        Connection con2 = oad.getConnection();
                        PreparedStatement st2 = con2.prepareStatement(sql2);
                        ResultSet rs2 = st2.executeQuery();
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                        while (rs2.next()) {
                            listaCod.addElement("'"+rs2.getString(des_val_cod)+"'");
                            listaDesc.addElement("'"+rs2.getString(des_val_desc)+"'");
                            listaEstado.addElement("'"+rs2.getString(des_val_estado)+"'");
                        }
                        rs2.close();
                        st2.close();
                        con2.close();
                        eC.setListaCodDesplegable(listaCod);
                        eC.setListaDescDesplegable(listaDesc);
                        eC.setListaEstadoValorDesplegable(listaEstado);
                    }
                } catch (NullPointerException e){
                }
                lista.addElement(eC);
            }
            rs.close();
            st.close();
        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            try{
                oad.finTransaccion(con);
                oad.devolverConexion(con);
            }catch(BDException bde) {
                bde.getMensaje();
            }
            return lista;
        }
    }

    
       
    
    public Vector cargaValoresDatosSuplementarios(TramitacionExpedientesValueObject tEVO,Vector eCs, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();
        CamposFormulario cF = null;
        
        
        CamposFormulario VALORES_CAMPOS_NUMERICOS = null;
        CamposFormulario VALORES_CAMPOS_TEXTO = null;
        CamposFormulario VALORES_CAMPOS_FECHA = null;
        CamposFormulario VALORES_CAMPOS_TEXTO_LARGO = null;
        CamposFormulario VALORES_CAMPOS_FICHERO = null;
        CamposFormulario VALORES_CAMPOS_NUMERICOS_CALCULADOS = null;
        CamposFormulario VALORES_CAMPOS_FECHA_CALCULADOS = null;
        CamposFormulario VALORES_CAMPOS_DESPLEGABLE = null;
        CamposFormulario VALORES_CAMPOS_DESPLEGABLE_EXTERNO = null;
        

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; // Inicio transacción

            
            /** CUANDO SE RECORRE LA ESTRUCTURA DE CAMPOS SUPLEMENTARIOS, SEGÚN EL TIPO DEL MISMO, SE RECUPERAN TODOS LOS CAMPOS
             ASOCIADOS AL TRÁMITE Y EXPEDIENTE SIN FILTRAR POR EL CÓDIGO DEL CAMPO. OBVIAMENTE ESTO NO ES ÓPTIMO, POR TANTO, PARA EVITAR
             HACER TANTAS CONSULTAS A BASE DE DATOS, LO QUE SE HACE ES SI HAY UN CAMPO DE TIPO NUMÉRICO, RECUPERAR TODOS LOS VALORES 
             DE CAMPOS DE TIPO NUMÉRICO ASOCIADOS AL TRÁMITE Y, A CONTINUACIÓN, GUARDARLOS EN UNA VARIABLE, DE MODO QUE SI EN LA SIGUIENTE
             ITERACCIÓN DEL BUCLE HAY OTRO CAMPO DE TIPO NUMÉRICO, COMO YA SE HAN RECUPERADO, NO SE VA A BASE DE DATOS DE NUEVO .
             
             AUNQUE ESTO NO ES DEL TODO OPTIMO, SE MEJORA LA VELOCIDAD DE EJECUCIÓN Y SIGUE FUNCIONANDO LA APLICACIÓN SIN NECESIDAD DE MODIFICAR
             DEMASIADO EL CÓDIGO **/
            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                if(m_Log.isDebugEnabled()) m_Log.debug("COD TIPO DATO ....................." + codTipoDato);
                if("1".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_NUMERICOS!=null)
                        cF = VALORES_CAMPOS_NUMERICOS;
                    else{
                        VALORES_CAMPOS_NUMERICOS = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_NUMERICOS;
                    }
                    
                } else if("2".equals(codTipoDato)) {
                    if(VALORES_CAMPOS_TEXTO!=null)
                        cF = VALORES_CAMPOS_TEXTO;
                    else{
                        VALORES_CAMPOS_TEXTO = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_TEXTO;
                    }
                    
                } else if("3".equals(codTipoDato)) {
                    if(VALORES_CAMPOS_FECHA!=null)
                        cF = VALORES_CAMPOS_FECHA;
                    else{
                        VALORES_CAMPOS_FECHA = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad,con,tEVO,mascara);
                        cF = VALORES_CAMPOS_FECHA;
                    }
                } else if("4".equals(codTipoDato)) {
                     if(VALORES_CAMPOS_TEXTO_LARGO!=null)
                        cF = VALORES_CAMPOS_TEXTO_LARGO;
                    else{
                        VALORES_CAMPOS_TEXTO_LARGO = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_TEXTO_LARGO;
                     }
                } else if("5".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_FICHERO!=null)
                        cF = VALORES_CAMPOS_FICHERO;
                    else{
                        VALORES_CAMPOS_FICHERO = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_FICHERO;
                    }
                } else if("6".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_DESPLEGABLE!=null)
                        cF = VALORES_CAMPOS_DESPLEGABLE;
                    else{
                        VALORES_CAMPOS_DESPLEGABLE = DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_DESPLEGABLE;
                    }
                } else if("8".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_NUMERICOS_CALCULADOS!=null)
                        cF = VALORES_CAMPOS_NUMERICOS_CALCULADOS;
                    else{
                        VALORES_CAMPOS_NUMERICOS_CALCULADOS = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad,con,tEVO);
                        cF = VALORES_CAMPOS_NUMERICOS_CALCULADOS;
                    }
                } else if("9".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_FECHA_CALCULADOS!=null)
                        cF = VALORES_CAMPOS_FECHA_CALCULADOS;
                    else{
                        VALORES_CAMPOS_FECHA_CALCULADOS = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad,con,tEVO,mascara);
                        cF = VALORES_CAMPOS_FECHA_CALCULADOS;
                    }
                } else if("10".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_DESPLEGABLE_EXTERNO!=null)
                        cF = VALORES_CAMPOS_DESPLEGABLE_EXTERNO;
                    else{
                        VALORES_CAMPOS_DESPLEGABLE_EXTERNO = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_DESPLEGABLE_EXTERNO;
                    }
                }
                lista.addElement(cF);
            }

        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            try{
                oad.finTransaccion(con);
                oad.devolverConexion(con);
            }catch(BDException bde) {
                bde.getMensaje();
            }
            return lista;
        }
    }
    
    
    
    
    public Vector cargaValoresDatosSuplEtiquetas(TramitacionExpedientesValueObject tEVO, Vector eCs, String[] params)
            throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Vector lista = new Vector();
        CamposFormulario cF = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con); // Inicio transacción

            for (int i = 0; i < eCs.size(); i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                if (m_Log.isDebugEnabled()) m_Log.debug("COD TIPO DATO ....................." + codTipoDato);
                if ("1".equals(codTipoDato)) {
                    cF = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad, con, tEVO);
                } else if ("2".equals(codTipoDato)) {
                    cF = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad, con, tEVO);
                } else if ("3".equals(codTipoDato)) {
                    cF = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad, con, tEVO, mascara);
                } else if ("4".equals(codTipoDato)) {
                    cF = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad, con, tEVO);
                } else if ("5".equals(codTipoDato)) {
                    cF = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad, con, tEVO);
                } else if ("6".equals(codTipoDato)) {
                    cF = DatosSuplementariosDAO.getInstance().getValsDespTramiteEtiquetas(oad, con, tEVO);
                } else if ("8".equals(codTipoDato)) { //Numérico calculado
                    cF = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad, con, tEVO, true);
                } else if ("9".equals(codTipoDato)) { //Fecha calculada
                    cF = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad, con, tEVO, mascara, true);
                } else if ("10".equals(codTipoDato)) { //Desplegable externo
                    cF = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad, con, tEVO);
                }
                lista.addElement(cF);
            }

        } catch (Exception e) {
            m_Log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            try {
                oad.finTransaccion(con);
                oad.devolverConexion(con);
            } catch (BDException bde) {
                bde.getMensaje();
            }
            return lista;
        }
    }

    public Vector cargaValoresDatosSuplementarios(TramitacionExpedientesValueObject tEVO,Vector eCs, AdaptadorSQL oad, Connection con) throws TechnicalException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();
        CamposFormulario cF = null;
        
        
        CamposFormulario CAMPOS_NUMERICOS_TRAMITE = null;
        CamposFormulario CAMPOS_TEXTO_TRAMITE = null;
        CamposFormulario CAMPOS_FECHA_TRAMITE = null;
        CamposFormulario CAMPOS_TEXTO_LARGO_TRAMITE = null;
        CamposFormulario CAMPOS_FICHERO_TRAMITE = null;
        CamposFormulario CAMPOS_DESPLEGABLE_TRAMITE = null;
        CamposFormulario CAMPOS_NUMERICOS_CALCULADOS_TRAMITE = null;
        CamposFormulario CAMPOS_FECHA_CALCULADOS_TRAMITE = null;
        CamposFormulario CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE = null;
        
        try{
            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                if("1".equals(codTipoDato)) {
                    if(CAMPOS_NUMERICOS_TRAMITE==null){
                        CAMPOS_NUMERICOS_TRAMITE= DatosSuplementariosDAO.getInstance().getValoresNumericosTramite((AdaptadorSQLBD)oad,con,tEVO);
                        cF = CAMPOS_NUMERICOS_TRAMITE;
                    }else
                        cF = CAMPOS_NUMERICOS_TRAMITE;
                } else if("2".equals(codTipoDato)) {
                    if(CAMPOS_TEXTO_TRAMITE==null){
                        CAMPOS_TEXTO_TRAMITE = DatosSuplementariosDAO.getInstance().getValoresTextoTramite((AdaptadorSQLBD)oad,con,tEVO);
                        cF = CAMPOS_TEXTO_TRAMITE;
                    }else
                        cF = CAMPOS_TEXTO_TRAMITE;
                } else if("3".equals(codTipoDato)) {
                    if(CAMPOS_FECHA_TRAMITE==null){
                        CAMPOS_FECHA_TRAMITE = DatosSuplementariosDAO.getInstance().getValoresFechaTramite((AdaptadorSQLBD)oad,con,tEVO,mascara);
                        cF= CAMPOS_FECHA_TRAMITE;
                    }else
                        cF= CAMPOS_FECHA_TRAMITE;
                    
                } else if("4".equals(codTipoDato)) {
                    if(CAMPOS_TEXTO_LARGO_TRAMITE==null){
                        CAMPOS_TEXTO_LARGO_TRAMITE = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite((AdaptadorSQLBD)oad,con,tEVO);
                        cF = CAMPOS_TEXTO_LARGO_TRAMITE;
                    }else
                        cF = CAMPOS_TEXTO_LARGO_TRAMITE;
                } else if("5".equals(codTipoDato)) {
                    if(CAMPOS_FICHERO_TRAMITE==null){
                        CAMPOS_FICHERO_TRAMITE = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite((AdaptadorSQLBD)oad,con,tEVO);
                        cF = CAMPOS_FICHERO_TRAMITE;
                    }else
                        cF = CAMPOS_FICHERO_TRAMITE;
                } else if("6".equals(codTipoDato)) {
                    if(CAMPOS_DESPLEGABLE_TRAMITE==null){
                        CAMPOS_DESPLEGABLE_TRAMITE= DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite((AdaptadorSQLBD)oad,con,tEVO);
                        cF = CAMPOS_DESPLEGABLE_TRAMITE;
                    }else
                        cF = CAMPOS_DESPLEGABLE_TRAMITE;
                } else if("8".equals(codTipoDato)) {
                    if(CAMPOS_NUMERICOS_CALCULADOS_TRAMITE==null){
                        CAMPOS_NUMERICOS_CALCULADOS_TRAMITE = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal((AdaptadorSQLBD)oad,con,tEVO);
                        cF = CAMPOS_NUMERICOS_CALCULADOS_TRAMITE;
                    }else
                        cF = CAMPOS_NUMERICOS_CALCULADOS_TRAMITE;
                } else if("9".equals(codTipoDato)) {
                    if(CAMPOS_FECHA_CALCULADOS_TRAMITE==null){
                        CAMPOS_FECHA_CALCULADOS_TRAMITE = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal((AdaptadorSQLBD)oad,con,tEVO,mascara);
                        cF = CAMPOS_FECHA_CALCULADOS_TRAMITE;
                    }else
                        cF = CAMPOS_FECHA_CALCULADOS_TRAMITE;
                    
                } else if("10".equals(codTipoDato)) {
                    if(CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE==null){
                        CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite((AdaptadorSQLBD)oad,con,tEVO);
                        cF = CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE;
                    }else
                        cF = CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE;
                }
                
                lista.addElement(cF);
            }

        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            return lista;
        }
    }


    public Vector getListaCondicionesEntrada(AdaptadorSQL oad, Connection con,TramitacionExpedientesValueObject tEVO, Vector tramitesAIniciar)
    {

        Statement stmt = null;
        String sql = "";
        String from = "";
        String where = "";
        ResultSet rs = null;
        Vector lista = new Vector();
        Vector listaResultado = new Vector();
        TramitacionExpedientesValueObject t = new TramitacionExpedientesValueObject();
        if(m_Log.isDebugEnabled()) m_Log.debug("ENTRA EN GET LISTA CONDICIONES ENTRADA:"+tramitesAIniciar.size());
        for (int i=0; i< tramitesAIniciar.size(); i++)
        {
             t= (TramitacionExpedientesValueObject) tramitesAIniciar.elementAt(i);
            try
            {
                from = ent_cod + "," + ent_ctr + "," + ent_est + ",tra." + tra_cou + " AS codTramiteVisible,tml. " +
                        tml_valor + " AS nombreTramite,tra1." + tra_cou + " AS codTrCondEntVis,tml1." + tml_valor +
                        " AS nomTrCondEntVis";
                where = ent_mun + "=" + tEVO.getCodMunicipio() + " AND " + ent_pro + "='" +
                        tEVO.getCodProcedimiento() + "' AND " + ent_tra + "=" +
                        t.getCodigoTramiteFlujoSalida();
                String[] join = new String[14];
                join[0] = "E_ENT";
                join[1] = "INNER";
                join[2] = "e_tra tra";
                join[3] = "e_ent." + ent_mun + "=tra." + tra_mun + " AND " +
                        "e_ent." + ent_pro + "=tra." + tra_pro + " AND " +
                        "e_ent." + ent_tra + "=tra." + tra_cod;
                join[4] = "INNER";
                join[5] = "e_tml tml";
                join[6] = "e_ent." + ent_mun + "=tml." + tml_mun + " AND " +
                        "e_ent." + ent_pro + "=tml." + tml_pro + " AND " +
                        "e_ent." + ent_tra + "=tml." + tml_tra + " AND " +
                        "tml." + tml_cmp + "='NOM'" + " AND " +
                        "tml." + tml_leng + "='"+idiomaDefecto+"'";
                join[7] = "INNER";
                join[8] = "e_tra tra1";
                join[9] = "e_ent." + ent_mun + "=tra1." + tra_mun + " AND " +
                        "e_ent." + ent_pro + "=tra1." + tra_pro + " AND " +
                        "e_ent." + ent_ctr + "=tra1." + tra_cod;
                join[10] = "INNER";
                join[11] = "e_tml tml1";
                join[12] = "e_ent." + ent_mun + "=tml1." + tml_mun + " AND " +
                        "e_ent." + ent_pro + "=tml1." + tml_pro + " AND " +
                        "e_ent." + ent_ctr + "=tml1." + tml_tra + " AND " +
                        "tml1." + tml_cmp + "='NOM'" + " AND " +
                        "tml1." + tml_leng + "='"+idiomaDefecto+"'";
                join[13] = "false";

                sql = oad.join(from,where,join);

                if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: Sentencia --> "+ sql);
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    GeneralValueObject g = new GeneralValueObject();
                    String codCondicionEntrada = rs.getString(ent_cod);
                    g.setAtributo("codCondicionEntrada",codCondicionEntrada);
                    String codTramiteCondicionEntrada = rs.getString(ent_ctr);
                    g.setAtributo("codTramiteCondicionEntrada",codTramiteCondicionEntrada);
                    String estadoCondicionEntrada = rs.getString(ent_est);
                    g.setAtributo("estadoCondicionEntrada",estadoCondicionEntrada);
                    g.setAtributo("codMunicipio",tEVO.getCodMunicipio());
                    g.setAtributo("codProcedimiento",tEVO.getCodProcedimiento());
                    g.setAtributo("codTramite",t.getCodigoTramiteFlujoSalida());
                    String codTramiteVisible = rs.getString("codTramiteVisible");
                    g.setAtributo("codTramiteVisible",codTramiteVisible);
                    String nombreTramite = rs.getString("nombreTramite");
                    g.setAtributo("nombreTramite",nombreTramite);
                    String codTramiteCondicionEntradaVisible = rs.getString("codTrCondEntVis");
                    g.setAtributo("codTramiteCondicionEntradaVisible",codTramiteCondicionEntradaVisible);
                    String nombreTramiteCondicionEntrada = rs.getString("nomTrCondEntVis");
                    g.setAtributo("nombreTramiteCondicionEntrada",nombreTramiteCondicionEntrada);
                    lista.addElement(g);
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: Resultado --> numero condiciones entrada.Tramite "+t.getCodigoTramiteFlujoSalida()+": " + lista.size());
                rs.close();
                
                stmt.close();

                // SQLException,TechnicalException, BDException
            }
            catch (Exception e)
            {
                lista = null;
                e.printStackTrace();
            }
        } // fin del for
        if(m_Log.isDebugEnabled()) m_Log.debug("Para cada uno de los elementos de la lista, consultamos su unidad tramitadora:"+lista.size());
        for(int j=0;j<lista.size();j++)
        {
            try
            {
                GeneralValueObject g = new GeneralValueObject();
                g = (GeneralValueObject) lista.elementAt(j);
                sql = "SELECT " + tra_utr + " FROM E_TRA WHERE " + tra_mun + "=" +
                        tEVO.getCodMunicipio() + " AND " + tra_pro + "='" + tEVO.getCodProcedimiento() +
                        "' AND " + tra_cod + "=" + tEVO.getCodTramite();
                if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getListaCondicionesEntrada --> "+ sql);
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                String codUnidadTramitadoraAnterior = "";
                while(rs.next())
                {
                    codUnidadTramitadoraAnterior = rs.getString(tra_utr);
                    g.setAtributo("codUnidadTramitadoraAnterior",codUnidadTramitadoraAnterior);
                }
                rs.close();
                if(codUnidadTramitadoraAnterior ==null || "".equals(codUnidadTramitadoraAnterior))
                {
                    sql = "SELECT " + exp_uor + " FROM E_EXP WHERE " + exp_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                            exp_eje + "=" + tEVO.getEjercicio() + " AND " + exp_num + "='" + tEVO.getNumeroExpediente() + "'";
                    if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getListaCondicionesEntrada --> "+ sql);
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        String codUnidadControladoraAnterior = rs.getString(exp_uor);
                        g.setAtributo("codUnidadControladoraAnterior",codUnidadControladoraAnterior);
                    }
                }
                rs.close();
                listaResultado.addElement(g);
                stmt.close();
            }
            catch (Exception e)
            {
                lista = null;
                e.printStackTrace();
            }
        }
        return listaResultado;
    }

    public Vector getListaCondicionesEntradaExpresion(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject tEVO, Vector tramitesAIniciar) {

        Statement stmt;
        ResultSet rs;
        Vector<GeneralValueObject> lista = new Vector<GeneralValueObject>();

        if (tramitesAIniciar == null) return lista;

        if (m_Log.isDebugEnabled())
            m_Log.debug("ENTRA EN GET LISTA CONDICIONES ENTRADA EXPRESION:" + tramitesAIniciar.size());
        for (int i = 0; i < tramitesAIniciar.size(); i++) {
            // Por aquí puede llegar o un objeto TramitacionExpedientesValueObject o un SiguienteTramiteTO. Si es éste último,
            // se hace una conversión a un TramitacionExpedienteValueObject
            Object obj = tramitesAIniciar.elementAt(i);
           TramitacionExpedientesValueObject t = (TramitacionExpedientesValueObject) tramitesAIniciar.elementAt(i);
            try {
                String selectCondsEntr = ent_cod + ", " + ent_ctr + ", " + ent_tipo + ", " + ent_exp + ", " + tml_valor;
                String whereCondsEntr = ent_mun + " = " + tEVO.getCodMunicipio() + " " +
                        "AND " + ent_pro + " = '" + tEVO.getCodProcedimiento() + "' " +
                        "AND " + ent_tra + " = " + t.getCodigoTramiteFlujoSalida() + " AND ENT_TIPO = 'E'";
                String[] joinCondsEntr = new String[5];
                joinCondsEntr[0] = "E_ENT";
                joinCondsEntr[1] = "INNER";
                joinCondsEntr[2] = "E_TML";
                joinCondsEntr[3] = "E_ENT." + ent_mun + " = E_TML." + tml_mun + " " +
                        "AND E_ENT." + ent_pro + " = E_TML." + tml_pro + " " +
                        "AND E_ENT." + ent_tra + " = E_TML." + tml_tra + " " +
                        "AND E_TML." + tml_cmp + " = 'NOM' AND E_TML." + tml_leng + " = '"+idiomaDefecto+"'";
                joinCondsEntr[4] = "false";
                String sql = oad.join(selectCondsEntr, whereCondsEntr, joinCondsEntr);

                if (m_Log.isDebugEnabled())
                    m_Log.debug("TramitacionExpedientesDAO: Sentencia en getListaCondicionesEntradaExpresion --> " + sql);
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    GeneralValueObject g = new GeneralValueObject();
                    String expCondicionEntrada = rs.getString(ent_exp);
                    g.setAtributo("expCondicionEntrada", expCondicionEntrada);
                    g.setAtributo("codTramiteSalida", t.getCodigoTramiteFlujoSalida());
                    g.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    g.setAtributo("nombreTramite", rs.getString(tml_valor));

                    lista.addElement(g);
                }

                if (m_Log.isDebugEnabled())
                    m_Log.debug("TramitacionDAO: Resultado --> numero condiciones entrada expresion.Tramite " +
                            t.getCodigoTramiteFlujoSalida() + ": " + lista.size());
                rs.close();
                stmt.close();

                // SQLException,TechnicalException, BDException
            }
            catch (Exception e) {
                lista = null;
                e.printStackTrace();
            }
        } // fin del for
        return lista;
    }

    public Vector comprobarCondicionesEntrada(Connection con, TramitacionExpedientesValueObject tEVO,
            Vector listaCondicionesEntrada, String codTramiteAFinalizar) {

        Statement stmt = null;
        String sql = null;
        ResultSet rs = null;
        Vector lista = new Vector();
        GeneralValueObject g = new GeneralValueObject();
        for (int i = 0; i < listaCondicionesEntrada.size(); i++) {
            g = (GeneralValueObject) listaCondicionesEntrada.elementAt(i);
            String codTramite = (String) g.getAtributo("codTramite");
            String codTramiteCondicionEntrada = (String) g.getAtributo("codTramiteCondicionEntrada");
            if (codTramiteCondicionEntrada.equals(codTramiteAFinalizar)) continue;
            String estadoCondicionEntrada = (String) g.getAtributo("estadoCondicionEntrada");
            String codCondicionEntrada = (String) g.getAtributo("codCondicionEntrada");
            String codTramiteVisible = (String) g.getAtributo("codTramiteVisible");
            String nombreTramite = (String) g.getAtributo("nombreTramite");
            String codTramiteCondicionEntradaVisible = (String) g.getAtributo("codTramiteCondicionEntradaVisible");
            String nombreTramiteCondicionEntrada = (String) g.getAtributo("nombreTramiteCondicionEntrada");
            String codUnidadTramitadoraAnterior = (String) g.getAtributo("codUnidadTramitadoraAnterior");
            String codUnidadControladoraAnterior = (String) g.getAtributo("codUnidadControladoraAnterior");
            String fechaFin = "";
            String resolucion = "";
            int cont = 0;

            try {

                sql = "SELECT " + cro_fef + "," + cro_res + " FROM E_CRO WHERE " +
                        cro_mun + "=" + tEVO.getCodMunicipio() + " AND " + cro_pro + "='" +
                        tEVO.getCodProcedimiento() + "' AND " + cro_eje + "=" + tEVO.getEjercicio() +
                        " AND " + cro_num + "='" + tEVO.getNumeroExpediente() + "' AND " +
                        cro_tra + "=" + codTramiteCondicionEntrada;

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("TramitesExpedienteDAO: sql de E_CRO --> " + sql);
                }
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    cont++;
                    fechaFin = rs.getString(cro_fef);
                    resolucion = rs.getString(cro_res);
                }
                if (cont > 0 && fechaFin == null) {
                    fechaFin = "";
                }
                if (cont > 0 && resolucion == null) {
                    resolucion = "";
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("la fecha fin y la resolucion son : " + fechaFin + "|" + resolucion);
                }
                if ("I".equals(estadoCondicionEntrada) && cont == 0) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "Iniciado");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    //Aqui es descripcion del trmaite que no se puede iniciar
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "Iniciado");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }
                if ("I".equals(estadoCondicionEntrada) && cont > 0 && !fechaFin.equals("")) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "Iniciado");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "Iniciado");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }
                if ("F".equals(estadoCondicionEntrada) && cont > 0 && fechaFin.equals("")) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "Finalizado");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "Finalizado");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }
                if ("F".equals(estadoCondicionEntrada) && cont == 0) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "Finalizado");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "Finalizado");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }
                if ("O".equals(estadoCondicionEntrada) && cont > 0) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "No Iniciado");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "No Iniciado");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }
                if ("S".equals(estadoCondicionEntrada) && !resolucion.equals("1")) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "Favorable");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "Favorable");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }
                if ("N".equals(estadoCondicionEntrada) && !resolucion.equals("0")) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "Desfavorable");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "Desfavorable");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }
                if ("C".equals(estadoCondicionEntrada) && cont > 0 && fechaFin.equals("")) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
                    gVO.setAtributo("ejercicio", tEVO.getEjercicio());
                    gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    gVO.setAtributo("codProcedimiento", tEVO.getCodProcedimiento());
                    gVO.setAtributo("codTramite", codTramite);
                    gVO.setAtributo("codCondicionEntrada", codCondicionEntrada);
                    gVO.setAtributo("codTramiteCondicionEntrada", codTramiteCondicionEntrada);
                    gVO.setAtributo("estadoCondicionEntrada", "No Iniciado o Finalizado");
                    gVO.setAtributo("codTramiteVisible", codTramiteVisible);
                    gVO.setAtributo("descTramite", nombreTramite);
                    gVO.setAtributo("codTramiteCondicionEntradaVisible", codTramiteCondicionEntradaVisible);
                    gVO.setAtributo("nombreTramiteCondicionEntrada", nombreTramiteCondicionEntrada);
                    gVO.setAtributo("resolucion", resolucion);
                    gVO.setAtributo("codUnidadTramitadoraAnterior", codUnidadTramitadoraAnterior);
                    gVO.setAtributo("codUnidadControladoraAnterior", codUnidadControladoraAnterior);
                    Vector aux = new Vector();
                    GeneralValueObject condicion = new GeneralValueObject();
                    condicion.setAtributo("descTramite", nombreTramiteCondicionEntrada);
                    condicion.setAtributo("tipoCondicion", "ESTADO_TRAMITE");
                    condicion.setAtributo("estadoTramite", "No iniciado o Finalizado");
                    aux.add(condicion);
                    gVO.setAtributo("listaCondiciones", aux);
                    lista.addElement(gVO);
                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("TramitacionDAO: getListaCondicionesEntrada --> numero condiciones entrada no cumplidas: " + lista.size());
                }
                rs.close();
                stmt.close();


            // SQLException,TechnicalException, BDException
            } catch (Exception e) {
                lista = null;
                e.printStackTrace();
            }
        }
        return lista;
    }


    public Vector comprobarCondicionesEntradaExpresion(
            Vector estructuraDSExpediente, Vector valoresDSExpediente, Vector estructuraDSTramites,
            Vector valoresDSTramites, Vector listaCondicionesEntradaExpresion)
            throws TechnicalException, Exception {

        // Inicializamos Valores.
        Vector noCumplenCondicion = new Vector();
        int tamEstExp = estructuraDSExpediente.size();
        int tamEstTra = estructuraDSTramites.size();
        String[] camposDSExpediente = new String[tamEstExp];
        int[] tiposDSExpediente = new int[tamEstExp];
        String[] datosDSExpediente = new String[tamEstExp];
        String[] nombresDSExpediente = new String[tamEstExp];

      try {
        //EXPEDIENTE: Crear los arrays de valores a partir de los vectores para pasarselos a la pila
        m_Log.debug("DATOS SUPLEMENTARIOS EXPEDIENTE ... ");
            for (int i=0; i < tamEstExp; i++) {
            EstructuraCampo eC = (EstructuraCampo) estructuraDSExpediente.elementAt(i);
            camposDSExpediente[i] = eC.getCodCampo();
            tiposDSExpediente[i] = Integer.valueOf(eC.getCodTipoDato()).intValue();
                nombresDSExpediente[i] = eC.getDescCampo();
            CamposFormulario cF = (CamposFormulario) valoresDSExpediente.get(i);
                m_Log.debug("    NOMBRE ... " + nombresDSExpediente[i]);
            m_Log.debug("    CAMPO  ... "+camposDSExpediente[i]);
            m_Log.debug("    TIPO   ... "+tiposDSExpediente[i]);

            if (tiposDSExpediente[i]==1) {
                    if (cF.getString(eC.getCodCampo()) != null)
                        datosDSExpediente[i] = cF.getString(eC.getCodCampo()).replaceAll(",",".");
                else
                    datosDSExpediente[i] = null;
            } else {
                if (cF!=null) datosDSExpediente[i] = cF.getString(eC.getCodCampo());
            }

            m_Log.debug("    DATO   ... "+datosDSExpediente[i]);
            m_Log.debug("    ......................................... ");
        }
        //TRAMITE: Crear los arrays de valores a partir de los vectores para pasarselos a la pila
        String[] camposDSTramites = new String[tamEstTra];
        int[] tiposDSTramites = new int[tamEstTra];
        String[] datosDSTramites = new String[tamEstTra];
            String[] nombresDSTramites = new String[tamEstTra];
        m_Log.debug("DATOS SUPLEMENTARIOS TRAMITES ... ");
        for (int i=0; i< tamEstTra; i++) {
            EstructuraCampo eC = (EstructuraCampo) estructuraDSTramites.elementAt(i);
            camposDSTramites[i] = eC.getCodCampo();
            tiposDSTramites[i] = Integer.valueOf(eC.getCodTipoDato()).intValue();
            nombresDSTramites[i] = eC.getDescCampo();
            CamposFormulario cF = (CamposFormulario) valoresDSTramites.get(i);
            m_Log.debug("    cF  ... '"+cF+"'");
            if (tiposDSTramites[i]==1) {
                datosDSTramites[i] = cF.getString(eC.getCodCampo());
                if (datosDSTramites[i]!=null)
                    datosDSTramites[i] = datosDSTramites[i].replaceAll(",",".");
                } else datosDSTramites[i] = cF.getString(eC.getCodCampo());
            
            //Si el dato se encuentra vacio se sustituye a null para evitar errores en la Pila
            if (datosDSTramites[i] != null && datosDSTramites[i].equals("")) {
                datosDSTramites[i] = null;
            }
            
            m_Log.debug("    CAMPO  ... "+camposDSTramites[i]);
            m_Log.debug("    TIPO   ... "+tiposDSTramites[i]);
            m_Log.debug("    DATO   ... "+datosDSTramites[i]);
            m_Log.debug("    ......................................... ");
        }

            GeneralValueObject g;

        for (int i=0; i< listaCondicionesEntradaExpresion.size(); i++) {
            g= (GeneralValueObject) listaCondicionesEntradaExpresion.elementAt(i);
            String expCondicionEntrada = (String) g.getAtributo("expCondicionEntrada");
            String codTramite = (String) g.getAtributo("codTramiteSalida");
                String numeroExpediente = (String) g.getAtributo("numeroExpediente");
                String nombreTramite = (String)g.getAtributo("nombreTramite");
            m_Log.debug("TRAMITE           ... "+codTramite);
            m_Log.debug("EXPRESION         ... "+expCondicionEntrada);

            Pila pila = new Pila();
            pila.camposExpediente=camposDSExpediente;
            pila.datosExpediente=datosDSExpediente;
            pila.tiposExpediente=tiposDSExpediente;
            pila.camposTramite=camposDSTramites;
            pila.datosTramite=datosDSTramites;
            pila.tiposTramite=tiposDSTramites;
            pila.rellenarPila(expCondicionEntrada);
            boolean result = pila.getResultado();
            m_Log.debug("RESULTADO PILA : "+result);
            if (!result) {
                // No se cumple la condición.
                    // Obtenemos que campos no cumplen la condición.
                StringTokenizer tokenizer = new StringTokenizer(pila.getCamposAnalizados(), "|");
                Vector camposAnalizados = new Vector();
                Vector nombresAnalizados = new Vector();
                while (tokenizer.hasMoreTokens()) {
                    camposAnalizados.add(tokenizer.nextToken());
                }
                m_Log.debug(camposAnalizados);
                for (int k = 0; k < tamEstExp; k++) {
                    String campo = camposDSExpediente[k];
                    for (int j = 0; j < camposAnalizados.size(); j++) {
                        if (campo.equals(camposAnalizados.get(j))) {
                            nombresAnalizados.add(nombresDSExpediente[k]);
                        }
                    }

                }
                for (int k = 0; k < tamEstTra; k++) {
                    String campo = camposDSTramites[k];
                    for (int j = 0; j < camposAnalizados.size(); j++) {
                        if (campo.equals(camposAnalizados.get(j))) {
                            nombresAnalizados.add(nombresDSTramites[k]);
                        }
                    }

                }

                    GeneralValueObject tramiteNoCumple = new GeneralValueObject();
                    // Almacenamos los datos del tramite que no se inicia.
                    tramiteNoCumple.setAtributo("codTramite", codTramite);
                    tramiteNoCumple.setAtributo("descTramite", nombreTramite);
                    tramiteNoCumple.setAtributo("numeroExpediente", numeroExpediente);
                    GeneralValueObject condicionNoCumplida = new GeneralValueObject();
                    condicionNoCumplida.setAtributo("tipoCondicion", "EXPRESION");
                    condicionNoCumplida.setAtributo("nombresCampos", nombresAnalizados);
                    Vector listaCondiciones = new Vector();
                    listaCondiciones.add(condicionNoCumplida);
                    tramiteNoCumple.setAtributo("listaCondiciones", listaCondiciones);
                    noCumplenCondicion.add(tramiteNoCumple);

                    m_Log.debug("No se ha podido iniciar el tramite: " + codTramite);
            }
        }
      } catch (Exception e){
          throw e;
        }
        m_Log.debug("El numero de tramites que no cumplen las condiciones de entrada de tipo expresion es " +
                noCumplenCondicion.size());
        return noCumplenCondicion;
    }

   

   

/******************************************************************************/
/*	BUSCAR TRAMITES PENDIENTES POR FINALIZAR								  */
/*  Busca en la tabla de Tramites Pendientes (E_TRP) aquellos tramites cuyo   */
/*  inicio depende del tramite que se le pasa como parametro de entrada       */
    /**************************************************************************/
   

   

    private Vector inicioDeTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO, Vector listaTramitesIniciar)
    throws Exception, TechnicalException {
        m_Log.debug("************ TramitacionExpedientesDAO.inicioDeTramite init");
        int resultado;
        String codUnidadTramitadoraAnterior = "";
        String iniciarTramitePendienteDespues = "no";
        Vector<GeneralValueObject> devolver = new Vector<GeneralValueObject>();

      

        //Compruebo los tramites q tienen como condicion de entrada una expresión, y no la cumplen.
        Vector tramitesExpresionCondEntradaNoOK = tratarTramCondEntradaExpresion(oad, con, teVO);       
        Vector listaCondicionesEntrada = getListaCondicionesEntrada(oad,con,teVO,listaTramitesIniciar);
        Vector listaCondicionesEntradaNoCumplidas = new Vector();
        
        String[] paramsBD = teVO.getParamsBD();
        if(listaCondicionesEntrada.size() > 0) {
            listaCondicionesEntradaNoCumplidas = comprobarCondicionesEntrada(con,teVO,listaCondicionesEntrada, null);
        }
        
        //insertarCondicionesEntrada(oad,con,listaCondicionesEntradaNoCumplidas);
        
        
        Vector tramitesFirmaDocCondEntradaNoOK = tratarTramCondEntradaFirmaDoc(con, teVO);
        
        if (listaTramitesIniciar != null) {
            m_Log.debug("SE QUIEREN INICIAR " + listaTramitesIniciar.size() + " TRAMITES A CONTINUACION");
            m_Log.debug("******************* RECORRIENDO LISTA DE TRAMITES A INICIAR *************** ");
            if (listaTramitesIniciar.size() > 0) {
                for (int i=0; i< listaTramitesIniciar.size(); i++) {
                    TramitacionExpedientesValueObject t = (TramitacionExpedientesValueObject) listaTramitesIniciar.elementAt(i);
                    m_Log.debug("****************** Código del tramite a iniciar: " + t.getCodTramite() + "-"
                            + t.getCodUnidadTramitadoraManual() + "-" + t.getCodUnidadTramitadoraUsu());

                    try {
                        rellenarDatosComunes(teVO,t);
                        // COMPROBAR CONDICIONES DE ENTRADA
                        String iniciar = "si";
                        boolean yaTratado = false;
                        m_Log.debug("TRAMITE A INICIAR en inicioDeTramite----------------------------------> " + t.getCodigoTramiteFlujoSalida());
                        for(int m=0; m<listaCondicionesEntradaNoCumplidas.size();m++)
                        {
                            GeneralValueObject g = (GeneralValueObject) listaCondicionesEntradaNoCumplidas.elementAt(m);
                            String codTramiteNoIniciar = (String) g.getAtributo("codTramite");
                            if(codTramiteNoIniciar.equals(t.getCodigoTramiteFlujoSalida())) {
                                for (int j=0;j<tramitesExpresionCondEntradaNoOK.size();j++) {
                                    GeneralValueObject gVO = (GeneralValueObject) tramitesExpresionCondEntradaNoOK.elementAt(j);
                                    if (gVO.getAtributo("codTramite").equals(codTramiteNoIniciar)) {
                                        g.setAtributo("expCondicionEntrada",gVO.getAtributo("expCondicionEntrada"));
                                        yaTratado = true;
                                    }
                                }
                                m_Log.debug("No cumple condiciones----------------------------------> " + t.getCodigoTramiteFlujoSalida());
                                iniciar = "no";
                                devolver.addElement(g);
                            }
                        }
                        for (int k=0;k<tramitesExpresionCondEntradaNoOK.size();k++) {
                            GeneralValueObject gVO = (GeneralValueObject) tramitesExpresionCondEntradaNoOK.elementAt(k);
                            String codTramiteNoIniciar = (String) gVO.getAtributo("codTramite");
                            if(codTramiteNoIniciar.equals(t.getCodigoTramiteFlujoSalida())) {
                                if (!yaTratado) {
                                    m_Log.debug("No cumple expresiones pero si tramites----------------------------------> " + t.getCodigoTramiteFlujoSalida());
                                    yaTratado = true;
                                    iniciar = "no";
                                    devolver.addElement(gVO);
                                }
                            }
                        }
                        for (int k=0;k<tramitesFirmaDocCondEntradaNoOK.size();k++) {
                            GeneralValueObject gVO = (GeneralValueObject) tramitesFirmaDocCondEntradaNoOK.elementAt(k);
                            String codTramiteNoIniciar = (String) gVO.getAtributo("codTramite");
                            if(codTramiteNoIniciar.equals(t.getCodigoTramiteFlujoSalida())) {
                                if (!yaTratado) {
                                    m_Log.debug("No cumple documentos ----------------------------------> " + t.getCodigoTramiteFlujoSalida());                                    
                                    iniciar = "no";                                    
                                    devolver.addElement(gVO);
                                }
                            }
                        }                        
                        // FIN DE COMPROBAR CONDICIONES DE ENTRADA
                        if(iniciar.equals("si")) {

                            m_Log.debug("*********** TramitacionExpedientesDAO iniciar=si");
                            

                            m_Log.debug("########## TramitacionExpedientesDAO.inicioDeTramite antes de iniciarTramite ");

                            t.setParamsBD(paramsBD);
                            t.setOcurrenciaTramite("1");
                            resultado=TramitesExpedienteDAO.getInstance().iniciarTramite(oad,con,t,codUnidadTramitadoraAnterior);
                            if(resultado >0) {
                                for(int j=0;j<t.getListaEMailsAlIniciar().size();j++) {
                                    teVO.getListaEMailsAlIniciar().addElement(t.getListaEMailsAlIniciar().elementAt(j));
                                }
                                teVO.setUnidadTramitadoraTramiteIniciado(t.getUnidadTramitadoraTramiteIniciado());
                               
                            }

                            
                        }
                        teVO.setOcurrenciaTramite(t.getOcurrenciaTramite());
                    }catch ( SQLException e){
                        e.printStackTrace();
                        throw new TramitacionException("Error.TramitacionExpedientesDAO.finalizarConTramites.iniciarTramite " + e.getMessage());
                    }catch ( BDException bde){
                        bde.printStackTrace();
                        throw new TramitacionException("Error.TramitacionExpedientesDAO.finalizarConTramites.iniciarTramite " + bde.getMessage());
                    }catch (Exception e){
                        throw new TramitacionException("Error.TramitacionExpedientesDAO.finalizarConTramites.iniciarTramite " + e.getMessage());
                    }
                }
                resultado =1;
            } else resultado = -1;
        } else resultado = -1;
        if(m_Log.isDebugEnabled()) m_Log.debug("antes de devolver el vector en inicioDeTramite la longitud del vector es : " + devolver.size());

        return devolver;
    }

   

    public Vector getListaInteresados(GeneralValueObject g,String[] params)
            throws TechnicalException,BDException{

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        Vector lista = new Vector();
        String codMunicipio = (String) g.getAtributo("codMunicipio");
        String codProcedimiento = (String) g.getAtributo("codProcedimiento");
        String numeroExpediente = (String) g.getAtributo("numeroExpediente");
        String ejercicio = (String) g.getAtributo("ejercicio");
        String codRol = (String) g.getAtributo("codRol");

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            
            String sql = null;
            ResultSet rs = null;

            String from = ext_ter + "," + ext_nvr + "," + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                    new String[]{hte_pa1, "' '", hte_ap1, "' '", hte_pa2, "' '", hte_ap2, "', '", hte_nom}) +
                    " AS titular," + ext_rol + "," + rol_des + "," + rol_pde + ",EXT_NOTIFICACION_ELECTRONICA ";
            String where = ext_mun + "=? AND " + ext_eje + "=?" +
                    " AND " + ext_num + "=? ";
            if(codRol != null && !codRol.equals("")) {
                where += " AND " + ext_rol + "=?" ;
            }
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
            String parametros[] = {"4","4"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, Integer.parseInt(codMunicipio));
            stmt.setInt(2, Integer.parseInt(ejercicio));
            stmt.setString(3,numeroExpediente);
            if(codRol != null && !codRol.equals(""))  stmt.setInt(4,Integer.parseInt(codRol));
            
            rs = stmt.executeQuery(); 
            while(rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();
                String codTercero = rs.getString(ext_ter);
                gVO.setAtributo("codTercero",codTercero);
                String versTercero = rs.getString(ext_nvr);
                gVO.setAtributo("versTercero",versTercero);
                String titular = rs.getString("titular");
                gVO.setAtributo("titular",titular);
                String codigoRol = rs.getString(ext_rol);
                gVO.setAtributo("codRol",codigoRol);
                String descRol = rs.getString(rol_des);
                gVO.setAtributo("descRol",descRol);
                String rolPorDefecto = rs.getString(rol_pde);
                gVO.setAtributo("rolPorDefecto",rolPorDefecto);
                gVO.setAtributo("codMunicipio",codMunicipio);
                gVO.setAtributo("codProcedimiento",codProcedimiento);
                gVO.setAtributo("numeroExpediente",numeroExpediente);
                gVO.setAtributo("ejercicio",ejercicio); 
                gVO.setAtributo("admiteNotif",rs.getString("EXT_NOTIFICACION_ELECTRONICA")==null?"0":rs.getString("EXT_NOTIFICACION_ELECTRONICA"));
                lista.addElement(gVO);
            }
            rs.close();
            stmt.close();

            oad.finTransaccion(con);

        } catch (SQLException ex) {
            oad.rollBack(con);
            ex.printStackTrace();
        } catch (BDException e) {
            m_Log.error("error al conectar en el metodo insert");
            e.printStackTrace();
        } finally {
            if (con != null)
                try{
                    oad.devolverConexion(con);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }

        return lista;
    }


    public Vector getListaInteresadosRelacion(GeneralValueObject g, String[] params)
            throws TechnicalException, BDException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Vector lista = new Vector();
        String codMunicipio = (String) g.getAtributo("codMunicipio");
        String codProcedimiento = (String) g.getAtributo("codProcedimiento");
        String numeroRelacion = (String) g.getAtributo("numeroRelacion");
        String ejercicio = (String) g.getAtributo("ejercicio");
        String ejerExp= numeroRelacion.substring(0,4);
        String codRol = (String) g.getAtributo("codRol");
        Vector listaExpedientes = new Vector();
        String numExp = "";

        // Primero tenemos que saber que expedientes pertenecen a la relación para que nos muestre los
        // interesados de cada uno de ellos.
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            Statement stmt = null;
            String sql = null;
            ResultSet rs = null;

            String consulta = "SELECT EXP_NUM FROM G_EXP WHERE REL_NUM ='"+numeroRelacion+"'";

            m_Log.debug("************ Consulta : \n"+consulta );
            stmt = con.createStatement();
            rs = stmt.executeQuery(consulta);

            while ( rs.next()){
                numExp = rs.getString(exp_rel_num);
                listaExpedientes.add(numExp);
            }

        } catch (SQLException ex) {
            oad.rollBack(con);
            ex.printStackTrace();
        } catch (BDException e) {
            m_Log.error("error al conectar en el metodo insert");
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    oad.devolverConexion(con);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage());
                }
        }

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            Statement stmt = null;
            String sql = null;
            ResultSet rs = null;

            for (int i=0;i< listaExpedientes.size();i++){

                String numeroExpe = (String)listaExpedientes.get(i);

                String from = ext_ter + "," + ext_nvr + "," + hte_pa1 + "||' '||"
                    + hte_ap1 + "||' '||" + hte_pa2 + "||' '||" + hte_ap2
                    + "||', '||" + hte_nom + " AS titular," + ext_rol + ","
                    + rol_des + "," + rol_pde;
                String where = ext_mun + "=" + codMunicipio + " AND " + ext_num + "='"
                        + numeroExpe + "'";
                if (codRol != null && !codRol.equals("")) {
                    where += " AND " + ext_rol + "=" + codRol;
                }
                String[] join = new String[8];
                join[0] = "E_EXT";
                join[1] = "INNER";
                join[2] = "e_rol";
                join[3] = "e_ext." + ext_mun + "=e_rol." + rol_mun + " AND "
                        + "e_ext." + ext_rol + "=e_rol." + rol_cod + " AND "
                        + "e_ext.ext_pro= " + "e_rol." + rol_pro;
                join[4] = "LEFT";
                join[5] = "t_hte";
                join[6] = "e_ext." + ext_ter + "=t_hte." + hte_ter + " AND "
                        + "e_ext." + ext_nvr + "=t_hte." + hte_nvr;
                join[7] = "false";

                sql = oad.join(from, where, join);
                String parametros[] = { "4", "4" };
                sql += oad.orderUnion(parametros);
                if (m_Log.isDebugEnabled())
                    m_Log.debug(sql);

                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    GeneralValueObject gVO = new GeneralValueObject();
                    String codTercero = rs.getString(ext_ter);
                    gVO.setAtributo("codTercero", codTercero);
                    String versTercero = rs.getString(ext_nvr);
                    gVO.setAtributo("versTercero", versTercero);
                    String titular = rs.getString("titular");
                    gVO.setAtributo("titular", titular);
                    String codigoRol = rs.getString(ext_rol);
                    gVO.setAtributo("codRol", codigoRol);
                    String descRol = rs.getString(rol_des);
                    gVO.setAtributo("descRol", descRol);
                    String rolPorDefecto = rs.getString(rol_pde);
                    gVO.setAtributo("rolPorDefecto", rolPorDefecto);
                    gVO.setAtributo("codMunicipio", codMunicipio);
                    gVO.setAtributo("codProcedimiento", codProcedimiento);
                    gVO.setAtributo("numeroExpediente", numExp);
                    gVO.setAtributo("ejercicio", ejercicio);
                    gVO.setAtributo("numeroRelacion",numeroRelacion);
                    lista.addElement(gVO);
                }
            }
            rs.close();
            stmt.close();

            oad.finTransaccion(con);

        } catch (SQLException ex) {
            oad.rollBack(con);
            ex.printStackTrace();
        } catch (BDException e) {
            m_Log.error("error al conectar en el metodo insert");
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    oad.devolverConexion(con);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage());
                }
        }

        return lista;
    }






    public Vector getListaExpedientesNoInteresadosRelacion(GeneralValueObject g,String[] params)
            throws TechnicalException,BDException{

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        Vector<TramitacionExpedientesValueObject> lista = new Vector<TramitacionExpedientesValueObject>();
        String codMunicipio = (String) g.getAtributo("codMunicipio");
        String codProcedimiento = (String) g.getAtributo("codProcedimiento");
        String numeroRelacion = (String) g.getAtributo("numeroRelacion");
        String ejercicio = (String) g.getAtributo("ejercicio");
        String codRol = (String) g.getAtributo("codRol");

        try {
            GeneralValueObject temp = new GeneralValueObject();
            temp.setAtributo("codMunicipio",codMunicipio);
            temp.setAtributo("codProcedimiento",codProcedimiento);
            temp.setAtributo("ejercicio",ejercicio);
            temp.setAtributo("numero",numeroRelacion);
            Vector expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(temp, params);


            con = oad.getConnection();
            oad.inicioTransaccion(con);
            Statement stmt = null;
            String sql = null;
            ResultSet rs = null;
            for (int i=0; i< expedientes.size(); i++) {
                GeneralValueObject exp = (GeneralValueObject) expedientes.get(i);
                String numExp = (String) exp.getAtributo("numExp");
                String ejeExp = (String) exp.getAtributo("ejeExp");
                String from = ext_ter + "," + ext_nvr + "," + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                        new String[]{hte_pa1, "' '", hte_ap1, "' '", hte_pa2, "' '", hte_ap2, "', '", hte_nom}) +
                        " AS titular," + ext_rol + "," + rol_des + "," + rol_pde;
                String where = ext_mun + "=" + codMunicipio + " AND " + ext_eje + "=" +
                        ejeExp + " AND " + ext_num + "='" + numExp + "'";
                if(codRol != null && !codRol.equals("")) {
                    where += " AND " + ext_rol + "=" + codRol;
                }
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
                String parametros[] = {"4","4"};
                sql += oad.orderUnion(parametros);
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                if(!rs.next()){
                    TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
                    teVO.setNumExpedienteNoInteresado(numExp);
                    lista.addElement(teVO);
                }
                rs.close();
                stmt.close();
            }

            oad.finTransaccion(con);

        } catch (SQLException ex) {
            oad.rollBack(con);
            ex.printStackTrace();
        } catch (BDException e) {
            m_Log.error("error al conectar en el metodo insert");
            e.printStackTrace();
        } finally {
            if (con != null)
                try{
                    oad.devolverConexion(con);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }

        return lista;
    }



    private void rellenarDatosComunes(TramitacionExpedientesValueObject tOrig, TramitacionExpedientesValueObject tDest){
        tDest.setCodMunicipio(tOrig.getCodMunicipio());
        tDest.setCodProcedimiento(tOrig.getCodProcedimiento());
        tDest.setEjercicio(tOrig.getEjercicio());
        tDest.setNumero(tOrig.getNumeroExpediente());
        tDest.setCodUsuario(tOrig.getCodUsuario());
        tDest.setCodOrganizacion(tOrig.getCodOrganizacion());
        tDest.setCodEntidad(tOrig.getCodEntidad());
        tDest.setCodUnidadOrganicaExp(tOrig.getCodUnidadOrganicaExp());
        tDest.setCodUnidadTramitadoraUsu(tOrig.getCodUnidadTramitadoraUsu());
        tDest.setCodUnidadTramitadoraManual(tOrig.getCodUnidadTramitadoraManual());
        tDest.setVectorCodInteresados(tOrig.getVectorCodInteresados());
        tDest.setListaEMailsAlIniciar(new Vector());
        tDest.setListaEMailsAlFinalizar(new Vector());
        tDest.setCodUsuario(tOrig.getCodUsuario());
        tDest.setBloqueo(tOrig.getBloqueo());
        tDest.setOrigenLlamada(tOrig.getOrigenLlamada());

    }

    public int getUsuarioFirma (TramitacionExpedientesValueObject tramExpVO, String[] params) throws Exception{
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql="";
        int usuario = -1;

        try {
            oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            st = con.createStatement();

            sql = "SELECT USU_COD FROM E_CRD_FIR WHERE CRD_MUN=" + tramExpVO.getCodMunicipio() + " AND CRD_PRO='" +
                    tramExpVO.getCodProcedimiento() + "' AND CRD_TRA=" + tramExpVO.getCodTramite() +
                    " AND CRD_NUD=" + tramExpVO.getCodDocumento() + " AND CRD_EJE=" + tramExpVO.getEjercicio() +
                    " AND CRD_NUM='" + tramExpVO.getNumeroExpediente() + "' AND CRD_OCU=" +
                    tramExpVO.getOcurrenciaTramite();

            if(m_Log.isDebugEnabled()) m_Log.debug("getFirmaDocumento: " + sql);
            rs = st.executeQuery(sql);
            if (rs.next()) {
                usuario = rs.getInt(1);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } finally {
            if (con != null)
                try{
                    oad.devolverConexion(con);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }
        return usuario;
    }

    public int getUsuarioFirmaRelacion (TramitacionExpedientesValueObject tramExpVO, String[] params) throws Exception{

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int usuario = -1;

        try {

            con = oad.getConnection();
            st = con.createStatement();

            String sql = "SELECT USU_COD FROM G_CRD_FIR " +
                    "WHERE CRD_MUN = " + tramExpVO.getCodMunicipio() + " AND CRD_PRO = '" + tramExpVO.getCodProcedimiento() + "' " +
                    "AND CRD_TRA = " + tramExpVO.getCodTramite() + " AND CRD_NUD = " + tramExpVO.getCodDocumento() + " AND " +
                    "CRD_EJE = " + tramExpVO.getEjercicio() + " AND CRD_NUM = '" + tramExpVO.getNumero() + "' " +
                    "AND CRD_OCU = " + tramExpVO.getOcurrenciaTramite();

            if(m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL USUARIO QUE DEBE FIRMAR UN DOCUMENTO DE UNA RELACION:");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            if (rs.next()) usuario = rs.getInt(1);

            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } finally {
            if (con != null)
                try{
                    oad.devolverConexion(con);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }
        return usuario;
    }

    public String getFirmaDocumento(TramitacionExpedientesValueObject tEVO, String[] params) throws Exception {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String firma = "";

        try {
            oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            st = con.createStatement();

            sql = "SELECT CRD_FIR_EST FROM E_CRD WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();

            if(m_Log.isDebugEnabled()) m_Log.debug("getFirmaDocumento: " + sql);
            rs = st.executeQuery(sql);
            if (rs.next()) {
                firma = rs.getString(1);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } finally {
            if (con != null)
                try{
                    oad.devolverConexion(con);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }
        return firma;
    }

    public String getFirmaDocumentoRelacion(TramitacionExpedientesValueObject tEVO, String[] params) throws Exception {

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st;
        ResultSet rs;
        String firma = "";

        try {

            con = oad.getConnection();
            st = con.createStatement();

            String sql = "SELECT CRD_FIR_EST FROM G_CRD " +
                    "WHERE CRD_MUN = " + tEVO.getCodMunicipio() + " AND CRD_PRO = '" + tEVO.getCodProcedimiento() + "' " +
                    "AND CRD_EJE = " + tEVO.getEjercicio() + " AND CRD_NUM = '" + tEVO.getNumero() + "' " +
                    "AND CRD_TRA = " + tEVO.getCodTramite() + " AND CRD_OCU = " + tEVO.getOcurrenciaTramite() + " " +
                    "AND CRD_NUD =" + tEVO.getCodDocumento();

            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL ESTADO DE LA FIRMA DEL DOCUMENTO DE UNA RELACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            if (rs.next()) firma = rs.getString(1);

            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw e;
        } finally {
            if (con != null)
                try {
                    oad.devolverConexion(con);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage());
                }
        }
        return firma;
    }


    public GeneralValueObject cargaValoresFicheros(TramitacionExpedientesValueObject tEVO,Vector eCs, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        GeneralValueObject lista = new GeneralValueObject();
        CamposFormulario cF = null;

        try{
        oad = new AdaptadorSQLBD(params);
        con = oad.getConnection();

        String codMunicipio = tEVO.getCodMunicipio();
        String ejercicio = tEVO.getEjercicio();
        String numero = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();

        for(int i=0;i<eCs.size();i++) {
          EstructuraCampo eC = new EstructuraCampo();
          eC = (EstructuraCampo) eCs.elementAt(i);
          String codTipoDato = eC.getCodTipoDato();
          String codCampo = eC.getCodCampo();
          m_Log.debug(".................T"+codTramite+"T...........Procesado FICHERO ... " + codCampo);
          byte[] fichero;
          if("5".equals(codTipoDato)) {
            if(codTramite == null || "".equals(codTramite)) {
                fichero=DatosSuplementariosDAO.getInstance().getFichero(codCampo,codMunicipio,ejercicio,numero,params);
                lista.setAtributo(codCampo,fichero);
            } else {
                fichero=DatosSuplementariosDAO.getInstance().getFicheroTramite(codCampo,codMunicipio,ejercicio,numero,params,codTramite);
                lista.setAtributo(codCampo+"_"+tEVO.getOcurrenciaTramite(),fichero);

            }
            lista.setAtributo(codCampo,fichero);
          }
        }

      }catch (Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }finally {
          try{
              oad.devolverConexion(con);
          }catch(BDException bde) {
              bde.printStackTrace();
              if(m_Log.isErrorEnabled()) m_Log.error(bde.getMessage());
          }
          return lista;
      }
    }

    public GeneralValueObject cargaNombresFicheros(TramitacionExpedientesValueObject tEVO, Vector eCs, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        GeneralValueObject lista = new GeneralValueObject();

        try{

            con = oad.getConnection();

            String codMunicipio = tEVO.getCodMunicipio();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();
                String ocurrencia = eC.getOcurrencia();

                String tipo;

                if("5".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        tipo=DatosSuplementariosDAO.getInstance().getNombreFichero(codCampo,codMunicipio,ejercicio,numero,params);
                    } else {
                        tipo=DatosSuplementariosDAO.getInstance().getNombreFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,params,codTramite);
                        codCampo=codCampo+"_"+ocurrencia;
                    }
                    lista.setAtributo(codCampo,tipo);
                }
            }

            return lista;

        } catch (Exception e){
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }



    public GeneralValueObject cargaTiposFicheros(TramitacionExpedientesValueObject tEVO,Vector eCs, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        GeneralValueObject lista = new GeneralValueObject();
        CamposFormulario cF = null;

        try{
        //oad = new AdaptadorSQLBD(params);
        //con = oad.getConnection();

            String codMunicipio = tEVO.getCodMunicipio();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocuTramite = tEVO.getOcurrenciaTramite();

        for(int i=0;i<eCs.size();i++) {
          EstructuraCampo eC = new EstructuraCampo();
          eC = (EstructuraCampo) eCs.elementAt(i);
          String codTipoDato = eC.getCodTipoDato();  
          String codCampo = eC.getCodCampo();
          String mascara = eC.getMascara();
          String tipo; 
          if("5".equals(codTipoDato)) {
            if(codTramite == null || "".equals(codTramite)) {
                tipo=DatosSuplementariosDAO.getInstance().getTipoFichero(codCampo,codMunicipio,ejercicio,numero,params);
            } else {
                tipo=DatosSuplementariosDAO.getInstance().getTipoFicheroTramite(codCampo,codMunicipio,ejercicio,numero,params, codTramite);
            }
            codCampo = eC.getCodCampo()+"_"+ocuTramite;
            lista.setAtributo(codCampo,tipo);
          }
        }

      }catch (Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }finally {
            /*
          try{
              oad.devolverConexion(con);
          }catch(BDException bde) {
              bde.printStackTrace();
              if(m_Log.isErrorEnabled()) m_Log.error(bde.getMessage());
          }*/
          return lista;
      }
    }

    public Vector cargarDatosSuplementariosExpediente(TramitacionExpedientesValueObject tEVO, AdaptadorSQL oad, Connection con) throws TechnicalException {
        GeneralValueObject gVO = new GeneralValueObject();
        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String usuario = tEVO.getCodUsuario();
        String codOrganizacion = tEVO.getCodOrganizacion();
        String codEntidad = tEVO.getCodEntidad();
        String desdeJsp = tEVO.getDesdeJsp();
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("codProcedimiento",codProcedimiento);
        gVO.setAtributo("ejercicio",ejercicio);
        gVO.setAtributo("numero",numeroExpediente);
        gVO.setAtributo("usuario",usuario);
        gVO.setAtributo("codOrganizacion",codOrganizacion);
        gVO.setAtributo("codEntidad",codEntidad);
        gVO.setAtributo("desdeJsp",desdeJsp);
        gVO.setAtributo("expHistorico","no");        
        if(tEVO.isExpHistorico()) gVO.setAtributo("expHistorico","si");
        
        m_Log.debug("INI PRUEBA ................................... ");
        

        // ini DATOS SUPLEMENTARIOS DE EXPEDIENTE para luego comprobar las expresiones de entrada
        Vector estructuraDSExpediente = FichaExpedienteDAO.getInstance().cargaEstructuraDatosSuplementarios(gVO, oad, con);
        m_Log.debug("ESTRUCTURA EXPEDIENTE ................................... "+estructuraDSExpediente);
        Vector valoresDSExpediente = FichaExpedienteDAO.getInstance().cargaValoresDatosSuplementarios(gVO, estructuraDSExpediente, oad, con);
        m_Log.debug("VALORES EXPEDIENTE    ................................... "+estructuraDSExpediente);
        // fin DATOS SUPLEMENTARIOS DE EXPEDIENTE para luego comprobar las expresiones de entrada

        // ini DATOS SUPLEMENTARIOS DE LOS TRÁMITES ABIERTOS para luego comprobar las expresiones de entrada
        Vector estructuraDSTramites = new Vector();
        Vector valoresDSTramites = new Vector();
        Vector estructuraDSTramiteActual = new Vector();
         Vector valoresDSTramiteActual = new Vector();
        Vector listaTramitesIniciados = FichaExpedienteDAO.getInstance().cargaTramites(gVO, oad, con);
        m_Log.debug("TRAMITES INICIADOS ................................... "+listaTramitesIniciados);
        for (int i=0; i< listaTramitesIniciados.size(); i++) {
            String codTramite = (String)((GeneralValueObject)listaTramitesIniciados.get(i)).getAtributo("codTramite");
            TramitacionExpedientesValueObject tEVOaux = new TramitacionExpedientesValueObject();
            tEVOaux.setCodMunicipio(codMunicipio);
            tEVOaux.setCodProcedimiento(codProcedimiento);
            tEVOaux.setEjercicio(ejercicio);
            tEVOaux.setOcurrenciaTramite((String) ((GeneralValueObject)listaTramitesIniciados.get(i)).getAtributo("ocurrenciaTramite"));
            tEVOaux.setNumeroExpediente(numeroExpediente);
            tEVOaux.setCodTramite(codTramite);
            tEVOaux.setUsuario(usuario);
            tEVOaux.setCodOrganizacion(codOrganizacion);
            tEVOaux.setCodEntidad(codEntidad);
            tEVOaux.setDesdeJsp(desdeJsp);
            tEVOaux.setExpHistorico(tEVO.isExpHistorico());            

            Vector estructuraDatosSuplementarios = cargaEstructuraDatosSuplementarios(tEVOaux, oad, con);
            Vector valoresSuplementarios = cargaValoresDatosSuplementarios(tEVOaux,estructuraDatosSuplementarios, oad, con);
            
            estructuraDSTramites.addAll(estructuraDatosSuplementarios);
            valoresDSTramites.addAll(valoresSuplementarios);
            
            if (codTramite.equals(tEVO.getCodTramite())){
                estructuraDSTramiteActual.addAll(estructuraDatosSuplementarios);
                valoresDSTramiteActual.addAll(valoresSuplementarios);
            }
            
        }
        // fin DATOS SUPLEMENTARIOS DE LOS TRÁMITES ABIERTOS para luego comprobar las expresiones de entrada

        m_Log.debug("ESTRUCTURA TRAMITES ................................... "+estructuraDSTramites);
        m_Log.debug("VALORES TRAMITES    ................................... "+valoresDSTramites);
        m_Log.debug("FIN PRUEBA ................................... ");
        Vector resultado = new Vector();
        resultado.add(estructuraDSExpediente);
        resultado.add(valoresDSExpediente);
        resultado.add(estructuraDSTramites);
        resultado.add(valoresDSTramites);
        resultado.add(estructuraDSTramiteActual);
        resultado.add(valoresDSTramiteActual);
        return resultado;
    }

    private void rollBackTransaction(AdaptadorSQLBD abd, Connection con) throws TechnicalException {
        try {
            if (con != null) abd.rollBack(con);
        } catch (BDException bde) {
            m_Log.error("ERROR AL HACER ROLLBACK DE  LA TRANSACCION EN LA CONEXION A LA BASE DE DATOS", bde);
            throw new TechnicalException(bde.getMensaje());
        }
    }

    private void devolverConexion(AdaptadorSQLBD abd, Connection con) throws TechnicalException {
        try {
            if (con != null) abd.devolverConexion(con);
        } catch (BDException bde) {
            m_Log.error("ERROR AL DEVOLVER LA CONEXION A LA BASE DE DATOS", bde);
            throw new TechnicalException(bde.getMensaje());
        }
    }

    private void finTransaccion(AdaptadorSQLBD abd, Connection con) throws TechnicalException {
        try {
            if (con != null) abd.finTransaccion(con);
        } catch (BDException bde) {
            m_Log.error("ERROR AL HACER COMMIT DE LA TRANSACCION DE LA CONEXION A LA BASE DE DATOS", bde);
            throw new TechnicalException(bde.getMensaje());
        }
    }


    /**
     * Para un determinado trámite comprueba si se cumplen sus condiciones de entrada de tipo trámite
     * @param oad
     * @param con
     * @param tEVO
     * @param tramitesAIniciar
     * @return
     */
    public Vector getListaCondicionesEntrada(AdaptadorSQL oad, Connection con,TramitacionExpedientesValueObject tEVO)
    {

        Statement stmt = null;
        String sql = "";
        String from = "";
        String where = "";
        ResultSet rs = null;
        Vector lista = new Vector();
        Vector listaResultado = new Vector();
        TramitacionExpedientesValueObject t = new TramitacionExpedientesValueObject();

        try
        {
            from = ent_cod + "," + ent_ctr + "," + ent_est + ",tra." + tra_cou + " AS codTramiteVisible,tml. " +
                    tml_valor + " AS nombreTramite,tra1." + tra_cou + " AS codTrCondEntVis,tml1." + tml_valor +
                    " AS nomTrCondEntVis";
            where = ent_mun + "=" + tEVO.getCodMunicipio() + " AND " + ent_pro + "='" +
                    tEVO.getCodProcedimiento() + "' AND " + ent_tra + "=" +
                    tEVO.getCodigoTramiteFlujoSalida();
            String[] join = new String[14];
            join[0] = "E_ENT";
            join[1] = "INNER";
            join[2] = "e_tra tra";
            join[3] = "e_ent." + ent_mun + "=tra." + tra_mun + " AND " +
                    "e_ent." + ent_pro + "=tra." + tra_pro + " AND " +
                    "e_ent." + ent_tra + "=tra." + tra_cod;
            join[4] = "INNER";
            join[5] = "e_tml tml";
            join[6] = "e_ent." + ent_mun + "=tml." + tml_mun + " AND " +
                    "e_ent." + ent_pro + "=tml." + tml_pro + " AND " +
                    "e_ent." + ent_tra + "=tml." + tml_tra + " AND " +
                    "tml." + tml_cmp + "='NOM'" + " AND " +
                    "tml." + tml_leng + "='"+idiomaDefecto+"'";
            join[7] = "INNER";
            join[8] = "e_tra tra1";
            join[9] = "e_ent." + ent_mun + "=tra1." + tra_mun + " AND " +
                    "e_ent." + ent_pro + "=tra1." + tra_pro + " AND " +
                    "e_ent." + ent_ctr + "=tra1." + tra_cod;
            join[10] = "INNER";
            join[11] = "e_tml tml1";
            join[12] = "e_ent." + ent_mun + "=tml1." + tml_mun + " AND " +
                    "e_ent." + ent_pro + "=tml1." + tml_pro + " AND " +
                    "e_ent." + ent_ctr + "=tml1." + tml_tra + " AND " +
                    "tml1." + tml_cmp + "='NOM'" + " AND " +
                    "tml1." + tml_leng + "='"+idiomaDefecto+"'";
            join[13] = "false";

            sql = oad.join(from,where,join);

                if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: Sentencia --> "+ sql);
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    GeneralValueObject g = new GeneralValueObject();
                    String codCondicionEntrada = rs.getString(ent_cod);
                    g.setAtributo("codCondicionEntrada",codCondicionEntrada);
                    String codTramiteCondicionEntrada = rs.getString(ent_ctr);
                    g.setAtributo("codTramiteCondicionEntrada",codTramiteCondicionEntrada);
                    String estadoCondicionEntrada = rs.getString(ent_est);
                    g.setAtributo("estadoCondicionEntrada",estadoCondicionEntrada);
                    g.setAtributo("codMunicipio",tEVO.getCodMunicipio());
                    g.setAtributo("codProcedimiento",tEVO.getCodProcedimiento());
                    g.setAtributo("codTramite",tEVO.getCodigoTramiteFlujoSalida());
                    String codTramiteVisible = rs.getString("codTramiteVisible");
                    g.setAtributo("codTramiteVisible",codTramiteVisible);
                    String nombreTramite = rs.getString("nombreTramite");
                    g.setAtributo("nombreTramite",nombreTramite);
                    String codTramiteCondicionEntradaVisible = rs.getString("codTrCondEntVis");
                    g.setAtributo("codTramiteCondicionEntradaVisible",codTramiteCondicionEntradaVisible);
                    String nombreTramiteCondicionEntrada = rs.getString("nomTrCondEntVis");
                    g.setAtributo("nombreTramiteCondicionEntrada",nombreTramiteCondicionEntrada);
                    lista.addElement(g);
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: Resultado --> numero condiciones entrada.Tramite "+t.getCodigoTramiteFlujoSalida()+": " + lista.size());
                rs.close();
                stmt.close();

                // SQLException,TechnicalException, BDException
            }
            catch (Exception e)
            {
                lista = null;
                e.printStackTrace();
            }

        if(m_Log.isDebugEnabled()) m_Log.debug("Para cada uno de los elementos de la lista, consultamos su unidad tramitadora:"+lista.size());
        for(int j=0;j<lista.size();j++)
        {
            try
            {
                GeneralValueObject g = new GeneralValueObject();
                g = (GeneralValueObject) lista.elementAt(j);
                sql = "SELECT " + tra_utr + " FROM E_TRA WHERE " + tra_mun + "=" +
                        tEVO.getCodMunicipio() + " AND " + tra_pro + "='" + tEVO.getCodProcedimiento() +
                        "' AND " + tra_cod + "=" + tEVO.getCodTramite();
                if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getListaCondicionesEntrada --> "+ sql);
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                String codUnidadTramitadoraAnterior = "";
                while(rs.next())
                {
                    codUnidadTramitadoraAnterior = rs.getString(tra_utr);
                    g.setAtributo("codUnidadTramitadoraAnterior",codUnidadTramitadoraAnterior);
                }
                rs.close();
                if(codUnidadTramitadoraAnterior ==null || "".equals(codUnidadTramitadoraAnterior))
                {
                    sql = "SELECT " + exp_uor + " FROM E_EXP WHERE " + exp_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                            exp_eje + "=" + tEVO.getEjercicio() + " AND " + exp_num + "='" + tEVO.getNumeroExpediente() + "'";
                    if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getListaCondicionesEntrada --> "+ sql);
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        String codUnidadControladoraAnterior = rs.getString(exp_uor);
                        g.setAtributo("codUnidadControladoraAnterior",codUnidadControladoraAnterior);
                    }
                }
                rs.close();
                listaResultado.addElement(g);
                stmt.close();
            }
            catch (Exception e)
            {
                lista = null;
                e.printStackTrace();
            }
        }
        return listaResultado;
    }


    /**
     * Recupera una lista de condiciones de entrada de tipo expresión para un trámite en concreto
     * @param oad
     * @param con
     * @param tEVO
     * @return
     */
     public Vector getListaCondicionesEntradaExpresion(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject tEVO) {

        Statement stmt;
        ResultSet rs;
        Vector<GeneralValueObject> lista = new Vector<GeneralValueObject>();

        m_Log.debug("*********** getListaCondicionesEntradaExpresion init **************");

            m_Log.debug("*********** getListaCondicionesEntradaExpresion  DATOS TRAMITES A INICIAR *************");
            m_Log.debug("*********** getListaCondicionesEntradaExpresion  t.getCodTramite(): " + tEVO.getCodTramite());
            m_Log.debug("*********** getListaCondicionesEntradaExpresion  t.getCodigoTramiteFlujoSalida(): " + tEVO.getCodigoTramiteFlujoSalida());
            m_Log.debug("*********** getListaCondicionesEntradaExpresion  t.getCodProcedimiento(): " + tEVO.getCodProcedimiento());
            try {
                String selectCondsEntr = ent_cod + ", " + ent_ctr + ", " + ent_tipo + ", " + ent_exp + ", " + tml_valor;
                String whereCondsEntr = ent_mun + " = " + tEVO.getCodMunicipio() + " " +
                        "AND " + ent_pro + " = '" + tEVO.getCodProcedimiento() + "' " +
                        "AND " + ent_tra + " = " + tEVO.getCodigoTramiteFlujoSalida() + " AND ENT_TIPO = 'E'";
                        //"AND " + ent_tra + " = " + t.getCodTramite() + " AND ENT_TIPO = 'E'";
                String[] joinCondsEntr = new String[5];
                joinCondsEntr[0] = "E_ENT";
                joinCondsEntr[1] = "INNER";
                joinCondsEntr[2] = "E_TML";
                joinCondsEntr[3] = "E_ENT." + ent_mun + " = E_TML." + tml_mun + " " +
                        "AND E_ENT." + ent_pro + " = E_TML." + tml_pro + " " +
                        "AND E_ENT." + ent_tra + " = E_TML." + tml_tra + " " +
                        "AND E_TML." + tml_cmp + " = 'NOM' AND E_TML." + tml_leng + " = '"+idiomaDefecto+"'";
                joinCondsEntr[4] = "false";
                String sql = oad.join(selectCondsEntr, whereCondsEntr, joinCondsEntr);

                if (m_Log.isDebugEnabled())
                    m_Log.debug("TramitacionExpedientesDAO: Sentencia en getListaCondicionesEntradaExpresion --> " + sql);
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    GeneralValueObject g = new GeneralValueObject();
                    String expCondicionEntrada = rs.getString(ent_exp);
                    g.setAtributo("expCondicionEntrada", expCondicionEntrada);
                    g.setAtributo("codTramiteSalida", tEVO.getCodigoTramiteFlujoSalida());
                    g.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                    g.setAtributo("nombreTramite", rs.getString(tml_valor));

                    lista.addElement(g);
                }

                if (m_Log.isDebugEnabled())
                    m_Log.debug("TramitacionDAO: Resultado --> numero condiciones entrada expresion.Tramite " +
                            tEVO.getCodigoTramiteFlujoSalida() + ": " + lista.size());
                rs.close();
                stmt.close();

            }
            catch (Exception e) {
                lista = null;
                e.printStackTrace();
            }

        return lista;
    }

     public Vector<ElementoListaValueObject> getUnidadesTramitadorasUsuario(Connection con, int codMunicipio, String codProcedimiento, int codTramite, int codUsuario) throws TechnicalException {

         String sqlQuery = "SELECT DISTINCT UOR_COD, UOR_NOM, UOR_COD_VIS " +
                 "FROM A_UOR " +
                 "JOIN E_CRO ON (CRO_UTR = UOR_COD)" +
                 "JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = UOR_COD) " +
                 "WHERE CRO_MUN = ? " +
                 "AND CRO_TRA = ? " +
                 "AND CRO_PRO = ? " +
                 "AND UOU_USU = ? " +
                 "AND UOU_ORG = ?";

         PreparedStatement ps = null;
         ResultSet rs = null;

         try {
             m_Log.debug(sqlQuery);
             m_Log.debug("Municipio: "+codMunicipio);
             m_Log.debug("Codigo Tramite: " + codTramite);
             m_Log.debug("Codigo Procedimiento: " + codProcedimiento);
             m_Log.debug("Codigo Usuario: " + codUsuario);
             m_Log.debug("Codigo Procedimiento: " + codProcedimiento);

             
             ps = con.prepareStatement(sqlQuery);

             ps.setInt(1, codMunicipio);
             ps.setInt(2, codTramite);
             ps.setString(3, codProcedimiento);
             ps.setInt(4, codUsuario);
             ps.setInt(5, codMunicipio);

             rs = ps.executeQuery();

             Vector<ElementoListaValueObject> utrs = new Vector<ElementoListaValueObject>();
             while (rs.next()) {
                 ElementoListaValueObject elemento = new ElementoListaValueObject();
                 elemento.setCodigo(rs.getString(1));
                 elemento.setDescripcion(rs.getString(2));
                 elemento.setIdentificador(rs.getString(3));
                 utrs.add(elemento);
             }

             return utrs;
         } catch (SQLException sqle) {
             throw new TechnicalException(sqle.getMessage(), sqle);
         } finally {
             SigpGeneralOperations.closeResultSet(rs);
             SigpGeneralOperations.closeStatement(ps);
         }
     }
    public int getCodRolPorDefecto (String codProcedimiento, Connection con) throws TechnicalException {
        int codRol = -1;

        PreparedStatement ps = null;
        String sql;
        ResultSet rs = null;
        try {
            sql = "SELECT ROL_COD FROM E_ROL WHERE ROL_PRO = ? AND ROL_PDE = 1";
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            if (rs.next()) codRol = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TechnicalException("ERROR AL RECUPERAR EL ROL POR DEFECTO");
        }finally {
             SigpGeneralOperations.closeResultSet(rs);
             SigpGeneralOperations.closeStatement(ps);
        }
        return codRol;
    }


    /************** oscar ************************/

    /**
     *
     * @param tramitesIniciar: Vector de objetos TramitacionExpedientesValueObject con los trámites a iniciar
     * @param codTramOrigen: Código del trámite de origen
     * @param ocuTramOrigen: Ocurrencia del trámite de origen
     * @param ejercicio: Ejercicio
     * @param codPro: Código del procedimiento
     * @param codMun: Código del municipio
     * @param numExp: Número del expediente
     * @param con: Conexión a la base de datos
     * @return True si todo bien y false en caso contrario.
     */
    public boolean guardarEnListaTramitesDeOrigen(Vector tramitesIniciar,TramitacionExpedientesValueObject dato,Connection con) throws SQLException,TechnicalException{

        boolean exito = false;
        PreparedStatement ps = null;

       // try{
            String sql = "INSERT INTO LIST_TRAM_ORIG(EJERCICIO,COD_PRO,COD_MUN,NUM_EXP,COD_TRA_ORIGEN,OCU_TRA_ORIGEN,COD_TRA_DESTINO,OCU_TRA_DESTINO) " +
                              "VALUES(?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);
            int contador = 0;
            for(int i=0;i<tramitesIniciar.size();i++){
                TramitacionExpedientesValueObject teVO = (TramitacionExpedientesValueObject)tramitesIniciar.get(i);

                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codMunicipio", dato.getCodMunicipio());
                gVO.setAtributo("codProcedimiento",dato.getCodProcedimiento());
                gVO.setAtributo("codTramite",teVO.getCodTramite());
                gVO.setAtributo("numero",dato.getNumeroExpediente());
                gVO.setAtributo("ejercicio",dato.getEjercicio());


                int ocurrencia = this.getOcurrenciaTramite(gVO, con);
                teVO.setOcurrenciaTramite(Integer.toString(ocurrencia));
                
                int j=1;

                if(Utilidades.isInteger(teVO.getCodTramite()) && Utilidades.isInteger(teVO.getOcurrenciaTramite())
                    && Utilidades.isInteger(dato.getEjercicio())  && Utilidades.isInteger(dato.getCodMunicipio()) && Utilidades.isInteger(teVO.getCodTramite()) && Utilidades.isInteger(teVO.getOcurrenciaTramite())){
                    
                    ps.setInt(j++,Integer.parseInt(dato.getEjercicio()));
                    ps.setString(j++,dato.getCodProcedimiento());
                    ps.setInt(j++,Integer.parseInt(dato.getCodMunicipio()));
                    ps.setString(j++,dato.getNumeroExpediente());
                    ps.setInt(j++,Integer.parseInt(dato.getCodTramite()));
                    ps.setInt(j++,Integer.parseInt(dato.getOcurrenciaTramite()));
                    ps.setInt(j++,Integer.parseInt(teVO.getCodTramite()));
                    ps.setInt(j++,Integer.parseInt(teVO.getOcurrenciaTramite()));

                    int rowsInserted = ps.executeUpdate();
                    m_Log.debug(this.getClass().getName() + ".guardarEnListaTramitesDeOrigen se ha insertado un trámite en la tabla de trámites de origen " + rowsInserted);
                    m_Log.debug(this.getClass().getName() + ".guardarEnListaTramitesDeOrigen codTramiteOrigen: " +dato.getCodTramite() + ", ocurrenciaTramiteOrigen: " + dato.getOcurrenciaTramite() + ", codTramDestino: " + teVO.getCodTramite() + ", ocurrenciaTramDestino: " +  teVO.getCodTramite() );
                    if(rowsInserted==1)
                        contador++;
                }                
            }// for

            if(contador==tramitesIniciar.size())
                exito = true;

         
        return exito;
    }


    /**
     * Comprueba si un trámite con su ocurrencia es origen de algún otro trámite. Se consulta la tabla LISTA_TRAM_ORIG
     * @param codTramOrigen: Código del trámite de origen
     * @param ocuTramOrigen: Ocurrencia del trámite de origen
     * @param ejercicio: Ejercicio
     * @param codPro: Código del procedimiento
     * @param codMun: Código del municipio
     * @param numExp: Número del expediente
     * @param con: Conexión a la base de datos
     * @return True si todo bien y false en caso contrario.
     */
    public boolean esOrigenDeAlgunTramite(int codTramOrigen, int ocuTramOrigen,int ejercicio,String codPro,int codMun,String numExp,Connection con) throws SQLException{

        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

      
        String sql = "SELECT COUNT(*) AS NUM FROM LIST_TRAM_ORIG " +
                         " WHERE COD_TRA_ORIGEN=? AND OCU_TRA_ORIGEN=? " +
                         " AND EJERCICIO =? AND COD_PRO	=? AND COD_MUN =? AND NUM_EXP=?";

        m_Log.debug(this.getClass().getName() + ".esOrigenDeAlgunTramite: " + sql);
        int i=1;
        ps = con.prepareStatement(sql);
        ps.setInt(i++,codTramOrigen);
        ps.setInt(i++,ocuTramOrigen);
        ps.setInt(i++,ejercicio);
        ps.setString(i++,codPro);
        ps.setInt(i++,codMun);
        ps.setString(i++,numExp);

        rs = ps.executeQuery();
        rs.next();

        int num = rs.getInt("NUM");
        if(num>=1)
            exito = true;

        if(ps!=null) ps.close();
        if(rs!=null) rs.close();

        return exito;
    }



    /**
     * Recupera los trámites de los que es origen un determinado trámite sin incluir un determinado trámite. Esta información se recupera de la tabla LISTA_TRAM_ORIG
     * @param tvo: Objeto TramitacionExpedientesValueObject con todos los datos del trámite de origen
     * @param codTramExcluir: Código del trámite de destino a excluir de la lista
     * @param ocuTramExcluir: Ocurrencia del trámite de destino a excluir de la lista
     * @param con: Conexión a la base de datos
     * @return True si todo bien y false en caso contrario.
     */
    public ArrayList<TramitacionExpedientesValueObject> getDestinoTramExcluido(TramitacionExpedientesValueObject tvo, int codTramExcluir,int ocuTramExcluir,Connection con) throws SQLException{

        ArrayList<TramitacionExpedientesValueObject> salida = new ArrayList<TramitacionExpedientesValueObject>();
        Statement st = null;
        ResultSet rs = null;

        String codTramiteOrigen = tvo.getCodTramite();
        String ocuTramiteOrigen = tvo.getOcurrenciaTramite();
        String ejercicio              = tvo.getEjercicio();
        String codProcedimiento = tvo.getCodProcedimiento();
        String codMunicipio        = tvo.getCodMunicipio();
        String numExpediente    = tvo.getNumeroExpediente();


        String sql = "SELECT COD_TRA_DESTINO,OCU_TRA_DESTINO,UOR_NOM,TML_VALOR " +
                          "FROM LIST_TRAM_ORIG,E_CRO, E_TML, A_UOR " +
                          "WHERE COD_TRA_ORIGEN=" + codTramiteOrigen + " AND OCU_TRA_ORIGEN=" + ocuTramiteOrigen + " AND EJERCICIO =" + ejercicio +
                          "AND COD_PRO ='" + codProcedimiento + "' AND COD_MUN =" + codMunicipio + " AND NUM_EXP='" + numExpediente + "' " +
                          "AND (COD_TRA_DESTINO<>" + codTramExcluir + " OR OCU_TRA_DESTINO<>" +ocuTramExcluir  + ")" +
                          "AND COD_PRO = CRO_PRO AND NUM_EXP = CRO_NUM " +
                          "AND COD_TRA_DESTINO = CRO_TRA AND OCU_TRA_DESTINO = CRO_OCU "+
                          "AND EJERCICIO = CRO_EJE AND CRO_UTR = UOR_COD " +
                          "AND CRO_MUN= TML_MUN AND CRO_PRO=TML_PRO AND CRO_TRA=TML_TRA ";

        m_Log.debug("  getDestinoTramExcluido sql: " + sql);
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while(rs.next()){
            TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
            teVO.setCodTramite(rs.getString("COD_TRA_DESTINO"));
            teVO.setOcurrenciaTramite(rs.getString("OCU_TRA_DESTINO"));
            teVO.setUnidadTramitadora(rs.getString("UOR_NOM"));
            teVO.setDescripcionTramiteFlujoSalida(rs.getString("TML_VALOR"));
            teVO.setTramite(tvo.getTramite());

            salida.add(teVO);
        }

        if(st!=null) st.close();
        if(rs!=null) rs.close();

        return salida;
    }





     /**
     * Recupera el trámite de origen de un trámite determinado. Se consulta la tabla LISTA_TRAM_ORIG
     * @param codTramDestino: Código del trámite de destino
     * @param ocuTramDestino: Ocurrencia del trámite de destino
     * @param con: Conexión a la base de datos
     * @return TramitacionExpedientesValueObject con el código del trámite y su ocurrencia o bien si todo bien y false en caso contrario.
     */
    public TramitacionExpedientesValueObject getTramiteOrigen(int codTramDestino, int ocuTramDestino,int ejercicio,String codPro,int codMun,String numExp,Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;

        TramitacionExpedientesValueObject  teVO = null;
            
         StringBuilder sql = new StringBuilder();
        sql.append(" SELECT lto.cod_tra_origen, ")
                .append("   lto.ocu_tra_origen, ")
                .append("   tml.tml_valor, ")
                .append("   cro.cro_fei ")
                .append(" FROM    e_tml tml, list_tram_orig lto ")
                .append("  left join ")
                .append("   e_cro cro on")
                 .append("   ( cro.cro_mun = lto.cod_mun ")
                .append("   AND cro.cro_pro = lto.cod_pro ")
                .append("   AND cro.cro_eje = lto.ejercicio ")
                .append("   AND cro.cro_num = lto.num_exp ")
                .append("   AND cro.cro_tra = lto.cod_tra_origen ")
                .append("   AND cro.cro_ocu = lto.ocu_tra_origen) ")
                .append("  WHERE  lto.cod_tra_destino = ? ")
                .append("   AND lto.ocu_tra_destino = ? ")
                .append("   AND lto.ejercicio = ? ")
                .append("   AND lto.cod_pro = ? ")
                .append("   AND lto.cod_mun = ? ")
                .append("   AND lto.num_exp = ? ")
                .append("   AND ( ( lto.cod_tra_origen = tml.tml_tra ")
                .append("            AND lto.cod_pro = tml.tml_pro ")
                .append("            AND lto.cod_mun = tml.tml_mun ) ")
                .append("            OR ( lto.cod_tra_origen =- 1 ")
                .append("                AND lto.ocu_tra_origen =- 1 ")
                .append("                AND lto.cod_tra_destino = tml.tml_tra ")
                .append("                AND lto.cod_pro = tml.tml_pro ")
                .append("                AND lto.cod_mun = tml.tml_mun ) ) ");
       

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql.toString()));
            }
        
            int i=1;
            ps = con.prepareStatement(sql.toString());
            ps.setInt(i++,codTramDestino);
            ps.setInt(i++,ocuTramDestino);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,codPro);
            ps.setInt(i++,codMun);
            ps.setString(i++,numExp);

            rs = ps.executeQuery();
            while(rs.next()){
                teVO = new TramitacionExpedientesValueObject();
                teVO.setCodTramite(rs.getString("COD_TRA_ORIGEN"));
                teVO.setOcurrenciaTramite(rs.getString("OCU_TRA_ORIGEN"));
                teVO.setCodProcedimiento(codPro);
                teVO.setEjercicio(Integer.toString(ejercicio));
                teVO.setCodMunicipio(Integer.toString(codMun));
                teVO.setNumeroExpediente(numExp);
                teVO.setNumero(teVO.getNumeroExpediente());
                teVO.setTramite(rs.getString("TML_VALOR"));
                teVO.setFechaInicio(DateOperations.timeStampToString(rs.getTimestamp("CRO_FEI"), DateOperations.LATIN_DATE_24HOUR_WITHOUT_ZERO_FORMAT));
                
            }

            return teVO;
    }



    /**
     * Recupera la ocurrencia que debe ter un determinado trámite. Será la siguiente a la última ocurrencia
     * que haya para ese trámite para un expediente de un determinado procedimiento     
     * @param gVO: GeneralValueObject
     * @param con: Conexión a la base de datos
     * @return int
     * @throws java.sql.SQLException
     * @throws es.altia.common.exception.TechnicalException
     */
    private int getOcurrenciaTramite(GeneralValueObject gVO,Connection con)
            throws SQLException,TechnicalException {
        Statement st = null;
        ResultSet rs = null;
        String sql = null;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String codTramite = (String) gVO.getAtributo("codTramite");
        int ocurrencia = 1;

        try{
            st = con.createStatement();
            sql = " SELECT MAX(CRO_OCU) AS OCURRENCIA " + 
                    " FROM E_CRO "
                    +" WHERE "+ cro_mun +"="+ codMunicipio 
                    +" AND "+ cro_pro+ "='" + codProcedimiento + "'"
                    +" AND "+ cro_eje + "=" + ejercicio
                    +" AND "+ cro_num + "='" + numero + "'"
                    +" AND "+ cro_tra + "=" + codTramite;

            m_Log.debug(" ******* codTramite: " + codTramite);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            if (rs.next() ) {
                ocurrencia = rs.getInt("OCURRENCIA");
                if (ocurrencia>=0) ocurrencia = ocurrencia+1;
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error(this.getClass().getName() + ".getOcurrenciaTramite error: " + e.getMessage());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception e){
                e.printStackTrace();
                m_Log.error(this.getClass().getName() + ".getOcurrenciaTramite error: " + e.getMessage());
            }
        }
        return ocurrencia;
    }
    
    
    
    /**
     * Recupera la ocurrencia que debe ter un determinado trámite. Será la siguiente a la última ocurrencia
     * que haya para ese trámite para un expediente de un determinado procedimiento     
     * @param gVO: GeneralValueObject
     * @param con: Conexión a la base de datos
     * @return int
     * @throws java.sql.SQLException
     * @throws es.altia.common.exception.TechnicalException
     */
    private int getOcurrenciaTramiteManual(GeneralValueObject gVO,Connection con)
            throws SQLException,TechnicalException {
        Statement st = null;
        ResultSet rs = null;
        String sql = null;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String codTramite = (String) gVO.getAtributo("codTramite");
        int ocurrencia = 1;

        try{
            st = con.createStatement();
            sql = " SELECT MAX(CRO_OCU) AS OCURRENCIA " + 
                    " FROM E_CRO "
                    +" WHERE "+ cro_mun +"="+ codMunicipio 
                    +" AND "+ cro_pro+ "='" + codProcedimiento + "'"
                    +" AND "+ cro_eje + "=" + ejercicio
                    +" AND "+ cro_num + "='" + numero + "'"
                    +" AND "+ cro_tra + "=" + codTramite;

            m_Log.debug(" ******* codTramite: " + codTramite);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            if (rs.next() ) {
                ocurrencia = rs.getInt("OCURRENCIA");
                if (ocurrencia==0) ocurrencia = ocurrencia+1;
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error(this.getClass().getName() + ".getOcurrenciaTramite error: " + e.getMessage());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception e){
                e.printStackTrace();
                m_Log.error(this.getClass().getName() + ".getOcurrenciaTramite error: " + e.getMessage());
            }
        }
        return ocurrencia;
    }



  /**
     * Comprueba si un trámite está finalizado
     * @param gVO: Instancia de la clase GeneralValueObject
     * @param con: Conexión a la base de datos
     * @return boolean
     */
    public boolean isFinalizadoTramite(GeneralValueObject gVO,Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;

        String sql = "SELECT COUNT(*) AS NUM FROM E_CRO " +
                          "WHERE CRO_MUN=? AND CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? " +
                          "AND CRO_TRA=? AND CRO_OCU=? AND CRO_FEF IS NOT NULL";

        m_Log.debug("isFinalizadoTramite: " + sql);
        int codMunicipio             = Integer.parseInt((String)gVO.getAtributo("codMunicipio"));
        int codTramite               = Integer.parseInt((String)gVO.getAtributo("codTramiteRetroceder"));
        int ocurrenciaTramite     = Integer.parseInt((String)gVO.getAtributo("ocurrenciaTramiteRetroceder"));
        int ejercicio                   = Integer.parseInt((String)gVO.getAtributo("ejercicio"));
        String numExpediente    = (String)gVO.getAtributo("numero");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

        int i=1;
        ps = con.prepareStatement(sql);
        ps.setInt(i++, codMunicipio);
        ps.setString(i++, codProcedimiento);
        ps.setInt(i++, ejercicio);
        ps.setString(i++, numExpediente);
        ps.setInt(i++,codTramite);
        ps.setInt(i++,ocurrenciaTramite);

        rs = ps.executeQuery();
        while(rs.next()){
            int numero = rs.getInt("NUM");
            if(numero==1) exito = true;
        }

        if(ps!=null) ps.close();
        if(rs!=null) rs.close();
        
        return exito;
    }


      /**
     *
     * Elimina un registro de la lista de trámites de origen
     * @param tramitesIniciar: Vector de objetos TramitacionExpedientesValueObject con los trámites a iniciar
     * @param codTramOrigen: Código del trámite de origen
     * @param ocuTramOrigen: Ocurrencia del trámite de origen
     * @param ejercicio: Ejercicio
     * @param codPro: Código del procedimiento
     * @param codMun: Código del municipio
     * @param numExp: Número del expediente
     * @param con: Conexión a la base de datos
     * @return True si todo bien y false en caso contrario.
     */
    public boolean eliminarRegistroListaTramitesOrigen(int codTramOrigen,int ocuTramOrigen,int codTramDestino, int ocuTramDestino,int ejercicio,String codPro,int codMun,String numExp,Connection con) throws SQLException{

        boolean exito = false;
        PreparedStatement ps = null;

        String sql = "DELETE FROM LIST_TRAM_ORIG WHERE EJERCICIO=? AND COD_PRO=? AND COD_MUN=? AND NUM_EXP=? AND COD_TRA_ORIGEN=? AND OCU_TRA_ORIGEN=? AND COD_TRA_DESTINO=? AND OCU_TRA_DESTINO=?";

        int j=1;
        ps = con.prepareStatement(sql);
        ps.setInt(j++,ejercicio);
        ps.setString(j++,codPro);
        ps.setInt(j++,codMun);
        ps.setString(j++,numExp);
        ps.setInt(j++,codTramOrigen);
        ps.setInt(j++,ocuTramDestino);
        ps.setInt(j++,codTramDestino);
        ps.setInt(j++,ocuTramDestino);

       int rowsInserted = ps.executeUpdate();
        m_Log.debug(this.getClass().getName() + ".guardarEnListaTramitesDeOrigen se ha insertado un trámite en la tabla de trámites de origen " + rowsInserted);
        if(rowsInserted==1) exito = true;
        if(ps!=null) ps.close();

        return exito;
    }



      /**
     *
     * Elimina un registro de la lista de trámites de origen
     * @param tramitesIniciar: Vector de objetos TramitacionExpedientesValueObject con los trámites a iniciar
     * @param codTramOrigen: Código del trámite de origen
     * @param ocuTramOrigen: Ocurrencia del trámite de origen
     * @param ejercicio: Ejercicio
     * @param codPro: Código del procedimiento
     * @param codMun: Código del municipio
     * @param numExp: Número del expediente
     * @param con: Conexión a la base de datos
     * @return True si todo bien y false en caso contrario.
     */
    public boolean isExpedienteFinalizado(String numExpediente,String codProcedimiento,int codMunicipio,int ejercicio,Connection con) throws SQLException{

        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(*) AS NUM FROM E_EXP " +
                         " WHERE EXP_FEF IS NOT NULL " +
                         " AND EXP_NUM=? AND EXP_PRO=? AND EXP_MUN=? AND EXP_EJE=? ";

        m_Log.debug(this.getClass().getName() + ".isExpedienteFinalizado sql: " + sql);
        int i=1;
        ps = con.prepareStatement(sql);
        ps.setString(i++,numExpediente);
        ps.setString(i++,codProcedimiento);
        ps.setInt(i++,codMunicipio);
        ps.setInt(i++,ejercicio);

        rs = ps.executeQuery();
        while(rs.next()){
            int num = rs.getInt("NUM");
            if(num==1)
                exito = true;
        }

        if(ps!=null) ps.close();
        if(rs!=null) rs.close();

        return exito;
    }


     /**
     * Recupera los trámites abiertos por un determinado. Esta información se recupera de la tabla LIST_TRAM_ORIG
     * @param tvo: Objeto TramitacionExpedientesValueObject con todos los datos del trámite de origen
     * @param codTramExcluir: Código del trámite de destino a excluir de la lista
     * @param ocuTramExcluir: Ocurrencia del trámite de destino a excluir de la lista
     * @param con: Conexión a la base de datos
     * @return True si todo bien y false en caso contrario.
     */
    public ArrayList<TramitacionExpedientesValueObject> getTramitesAbiertosPor(TramitacionExpedientesValueObject tvo,Connection con) throws SQLException{

        ArrayList<TramitacionExpedientesValueObject> salida = new ArrayList<TramitacionExpedientesValueObject>();
        Statement st = null;
        ResultSet rs = null;

        String codTramiteOrigen = tvo.getCodTramite();
        String ocuTramiteOrigen = tvo.getOcurrenciaTramite();
        String ejercicio              = tvo.getEjercicio();
        String codProcedimiento = tvo.getCodProcedimiento();
        String codMunicipio        = tvo.getCodMunicipio();
        String numExpediente    = tvo.getNumeroExpediente();

        String sql = "SELECT COD_TRA_DESTINO,OCU_TRA_DESTINO,CRO_UTR, UOR_NOM,TML_VALOR FROM LIST_TRAM_ORIG ,E_CRO,E_TML, A_UOR " +
                         " WHERE COD_TRA_ORIGEN=" + codTramiteOrigen + " AND OCU_TRA_ORIGEN=" + ocuTramiteOrigen +
                         " AND EJERCICIO =" + ejercicio +" AND COD_PRO	='" + codProcedimiento + "' AND COD_MUN =" + codMunicipio + " AND NUM_EXP='" + numExpediente + "' " +
                         " AND COD_TRA_DESTINO = CRO_TRA AND OCU_TRA_DESTINO= CRO_OCU AND EJERCICIO = CRO_EJE AND COD_PRO = CRO_PRO AND NUM_EXP = CRO_NUM "  +
                         " AND CRO_MUN= TML_MUN AND CRO_PRO=TML_PRO AND CRO_TRA=TML_TRA "  +
                         " AND CRO_UTR = UOR_COD";

        m_Log.debug(" getTramitesAbiertosPor: " + sql);
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while(rs.next()){
            TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
            teVO.setCodTramite(rs.getString("COD_TRA_DESTINO"));
            teVO.setOcurrenciaTramite(rs.getString("OCU_TRA_DESTINO"));
            teVO.setUnidadTramitadora(rs.getString("UOR_NOM")); // Se guarda la descripción de la unidad tramitadora para mostrarla al usaurio
            teVO.setDescripcionTramiteFlujoSalida(rs.getString("TML_VALOR"));
            salida.add(teVO);
        }

        if(st!=null) st.close();
        if(rs!=null) rs.close();

        return salida;
    }


   /**
     * Recupera la fecha de fin de un  trámite abiertos por un determinado. Esta información se recupera de la tabla LIST_TRAM_ORIG
     * @param gVO: Objeto GeneralValueObject con los datos del trámite
     * @param con: Conexión a la base de datos
     * @return Calendar con la fecha de fin o null si no la tiene
     */
    public Calendar getFechaFinTramite(GeneralValueObject gVO,Connection con) throws SQLException{        
        PreparedStatement st = null;
        ResultSet rs = null;
        Calendar fechaFin = null;

        String numExpediente      = (String)gVO.getAtributo("numero");
        String codTramite            = (String)gVO.getAtributo("codTramiteRetroceder");
        String ocurrenciaTramite  = (String)gVO.getAtributo("ocurrenciaTramiteRetroceder");
        String ejercicio                = (String)gVO.getAtributo("ejercicio");
        String codProcedimiento   = (String)gVO.getAtributo("codProcedimiento");
        String codMunicipio          = (String)gVO.getAtributo("codMunicipio");
                
        String sql = "SELECT CRO_FEF FROM E_CRO " +
                         " WHERE CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? AND CRO_TRA=? AND CRO_OCU=? AND CRO_MUN=? ";

        m_Log.debug(" getTramitesAbiertosPor: " + sql);
        int i = 1;
        st = con.prepareStatement(sql);
        st.setString(i++,codProcedimiento);
        st.setInt(i++,Integer.parseInt(ejercicio));
        st.setString(i++,numExpediente);
        st.setInt(i++,Integer.parseInt(codTramite));
        st.setInt(i++,Integer.parseInt(ocurrenciaTramite));
        st.setInt(i++,Integer.parseInt(codMunicipio));
        rs = st.executeQuery();

        rs.next();
        fechaFin = DateOperations.toCalendar(rs.getTimestamp("CRO_FEF"));

        if(st!=null) st.close();
        if(rs!=null) rs.close();

        return fechaFin;
    }


   /**
     * Guarda un trámite manual en la lista de trámites de origen. Como un trámite manual no tiene origen se indica que su el trámite de origen y la ocurrencia del mismo
    * es -1. Se guarda en la tabla LIST_TRAM_ORIG
     * @param gVO: Objeto GeneralValueObject con los datos del trámite
     * @param con: Conexión a la base de datos
     * @return Calendar con la fecha de fin o null si no la tiene
     */
    public void guardarTramiteManualEnListaTramitesOrigen(TramitacionExpedientesValueObject tEVO,Connection con) throws SQLException,TechnicalException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        

        String sql = "INSERT INTO LIST_TRAM_ORIG(EJERCICIO,COD_PRO,COD_MUN,NUM_EXP,COD_TRA_ORIGEN,OCU_TRA_ORIGEN,COD_TRA_DESTINO,OCU_TRA_DESTINO) " +
                          "VALUES(?,?,?,?,?,?,?,?)";

        m_Log.debug("guardarTramiteManualEnListaTramitesOrigen sql: " + sql);
        
        ps = con.prepareStatement(sql);
        String codMunicipio         = tEVO.getCodMunicipio();
        String codProcedimiento  = tEVO.getCodProcedimiento();
        String numExpediente     = tEVO.getNumeroExpediente();
        String ejercicio               = tEVO.getEjercicio();
       
        Vector tramitesIniciar      = tEVO.getListaTramitesIniciar();


        for(int i=0;i<tramitesIniciar.size();i++)
        {
            TramitacionExpedientesValueObject tramiteIniciar = (TramitacionExpedientesValueObject)tramitesIniciar.get(i);
            GeneralValueObject gVO = new GeneralValueObject();

            gVO.setAtributo("codMunicipio", codMunicipio);
            gVO.setAtributo("codProcedimiento", codProcedimiento);
            gVO.setAtributo("codTramite",tramiteIniciar.getCodTramite());  //==> Código del trámite de destino que viene en tramitesIniciar
            gVO.setAtributo("numero",numExpediente);
            gVO.setAtributo("ejercicio",ejercicio);
            int ocurrenciaDestino = this.getOcurrenciaTramiteManual(gVO, con);
            int codTramiteDestino = Integer.parseInt(tramiteIniciar.getCodTramite()); //==> Recuperar el código trámite destino

            int j=1;

            ps.setInt(j++,Integer.parseInt(ejercicio));
            ps.setString(j++,codProcedimiento);
            ps.setInt(j++,Integer.parseInt(codMunicipio));
            ps.setString(j++,numExpediente);
            ps.setInt(j++,-1);
            ps.setInt(j++,-1);
            ps.setInt(j++,codTramiteDestino);
            ps.setInt(j++,ocurrenciaDestino);

            int rowsInserted = ps.executeUpdate();
            m_Log.debug(this.getClass().getName() + ".guardarTramiteManualEnListaTramitesOrigen se ha insertado un trámite en la tabla de trámites de origen " + rowsInserted);
            m_Log.debug(this.getClass().getName() + ".guardarTramiteManualEnListaTramitesOrigen codTramiteOrigen: -1 y  ocurrenciaTramiteOrigen: -1 " + ", codTramDestino: " + codTramiteDestino + ", ocurrenciaTramDestino: " +  ocurrenciaDestino);
           
                
        }// for

        if(ps!=null) ps.close();
        if(rs!=null) rs.close();
            
    }



   /**
     * Comprueba si un determinado tramite tiene documentos firmados asociados
     * @param gVO: Objeto GeneralValueObject con los datos del trámite
     * @param con: Conexión a la base de datos
     * @return boolean
     */
    public boolean tieneDocumentosFirmados(GeneralValueObject gVO,Connection con) throws TechnicalException{
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{

            String codMunicipio         =(String)gVO.getAtributo("codMunicipio");
            String codProcedimiento  =(String)gVO.getAtributo("codProcedimiento");
            String ejercicio               =(String)gVO.getAtributo("ejercicio");            
            String numExpediente     =(String)gVO.getAtributo("numero");
            String codTramite           =(String)gVO.getAtributo("codTramiteRetroceder");
            String ocurrenciaTramite =(String)gVO.getAtributo("ocurrenciaTramiteRetroceder");
            

            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) AS NUM FROM E_CRD_FIR,E_CRD WHERE");
            sql.append(" E_CRD_FIR.CRD_MUN=?");
            sql.append(" AND E_CRD_FIR.CRD_PRO=?");
            sql.append(" AND E_CRD_FIR.CRD_EJE=?");
            sql.append(" AND E_CRD_FIR.CRD_NUM=?");
            sql.append(" AND E_CRD_FIR.CRD_TRA=?");
            sql.append(" AND E_CRD_FIR.CRD_OCU=?");            
            sql.append(" AND E_CRD_FIR.CRD_MUN=E_CRD.CRD_MUN");
            sql.append(" AND E_CRD_FIR.CRD_PRO=E_CRD.CRD_PRO");
            sql.append(" AND E_CRD_FIR.CRD_EJE=E_CRD.CRD_EJE");
            sql.append(" AND E_CRD_FIR.CRD_NUM=E_CRD.CRD_NUM");
            sql.append(" AND E_CRD_FIR.CRD_TRA=E_CRD.CRD_TRA");
            sql.append(" AND E_CRD_FIR.CRD_OCU=E_CRD.CRD_OCU");
            sql.append(" AND E_CRD_FIR.CRD_MUN=E_CRD.CRD_NUD");

            //REFERENCES E_CRD (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD) ON DELETE CASCADE
            int i=1;
            ps = con.prepareStatement(sql.toString());
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numExpediente);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(ocurrenciaTramite));

            rs = ps.executeQuery();
            while(rs.next()){
                int num = rs.getInt("NUM");
                if(num>=1) exito = true;
            }

        }catch(SQLException e){
            e.printStackTrace();
            m_Log.debug(this.getClass().getName() + ".tieneDocumentosFirmados:" + e.getMessage());
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                m_Log.debug(this.getClass().getName() + ".tieneDocumentosFirmados:" + e.getMessage());
            }
        }        

        return exito;
    }

    public Vector getListaCondicionesEntradaDocumento(TramitacionExpedientesValueObject tEVO,  Connection con, Vector tramitesAIniciar) throws SQLException {

        Vector lista = new Vector();        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT ent_doc,  ent_est,  ent_tra, TML_VALOR, DPML_VALOR FROM e_ent "
                + "LEFT JOIN E_TML ON (ENT_MUN=TML_MUN AND ENT_PRO=TML_PRO AND ENT_TRA=TML_TRA AND TML_CMP='NOM' AND TML_LENG=?) "
                + "LEFT JOIN E_DPML ON (ENT_MUN=DPML_MUN AND ENT_PRO=DPML_PRO AND ENT_DOC=DPML_DOP AND DPML_CMP='NOM' AND DPML_LENG=?) "
                + "WHERE ent_mun=? AND ent_pro=? AND ent_tra=? AND ent_ctr=0 AND ent_tipo='D'";

        for (Iterator it = tramitesAIniciar.iterator(); it.hasNext();) {
            TramitacionExpedientesValueObject t = (TramitacionExpedientesValueObject) it.next();
            ps = con.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,idiomaDefecto);
            ps.setString(i++,idiomaDefecto);
            ps.setInt(i++, Integer.parseInt(tEVO.getCodMunicipio()));
            ps.setString(i++, tEVO.getCodProcedimiento());
            ps.setInt(i++, Integer.parseInt(t.getCodTramite()));

            m_Log.debug("RECUPERANDO CONDICIONES DE ENTRADA TIPO FIRMA DE DOCUMENTO DEL TRAMITE DE COD INTERNO " + t.getCodTramite());
            m_Log.debug(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codDocumento", rs.getString("ent_doc"));
                gVO.setAtributo("estadoFirma", rs.getString("ent_est"));
                gVO.setAtributo("codTramite", rs.getString("ent_tra"));
                gVO.setAtributo("descTramite", rs.getString("TML_VALOR"));
                gVO.setAtributo("descDocumento", rs.getString("DPML_VALOR"));
                lista.add(gVO);        
            }
            try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(ps);
            } catch (TechnicalException ex) {
                Logger.getLogger(TramitacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new SQLException();
            }
            
        }

        return lista;

    }

    public Vector getListaCondicionesEntradaDocumento(TramitacionExpedientesValueObject tEVO, Connection con) throws SQLException {

        Vector lista = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT ent_doc,  ent_est,  ent_tra, TML_VALOR, DPML_VALOR FROM e_ent "
                + "LEFT JOIN E_TML ON (ENT_MUN=TML_MUN AND ENT_PRO=TML_PRO AND ENT_TRA=TML_TRA AND TML_CMP='NOM' AND TML_LENG=?) "
                + "LEFT JOIN E_DPML ON (ENT_MUN=DPML_MUN AND ENT_PRO=DPML_PRO AND ENT_DOC=DPML_DOP AND DPML_CMP='NOM' AND DPML_LENG=?) "
                + "WHERE ent_mun=? AND ent_pro=? AND ent_tra=? AND ent_ctr=0 AND ent_tipo='D'";



        ps = con.prepareStatement(sql);

        int i = 1;
        ps.setString(i++, idiomaDefecto);
        ps.setString(i++, idiomaDefecto);
        ps.setInt(i++, Integer.parseInt(tEVO.getCodMunicipio()));
        ps.setString(i++, tEVO.getCodProcedimiento());
        ps.setInt(i++, Integer.parseInt(tEVO.getCodTramite()));

        m_Log.debug("RECUPERANDO CONDICIONES DE ENTRADA TIPO FIRMA DE DOCUMENTO DEL TRAMITE DE COD INTERNO " + tEVO.getCodTramite());
        m_Log.debug(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codDocumento", rs.getString("ent_doc"));
            gVO.setAtributo("estadoFirma", rs.getString("ent_est"));
            gVO.setAtributo("codTramite", rs.getString("ent_tra"));
            gVO.setAtributo("descTramite", rs.getString("TML_VALOR"));
            gVO.setAtributo("descDocumento", rs.getString("DPML_VALOR"));
            lista.add(gVO);
        }
        try {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        } catch (TechnicalException ex) {
            Logger.getLogger(TramitacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException();
        }



        return lista;

    }

    public Vector tratarTramCondEntradaFirmaDoc(Connection con, TramitacionExpedientesValueObject tEVO) throws SQLException {
        Vector tramitesAIniciar = tEVO.getListaTramitesIniciar();
        Vector listaTramitesCondEntradaNoOk = new Vector();
        Vector listaCondicionesEntradaDocumento = getListaCondicionesEntradaDocumento(tEVO,con,tramitesAIniciar);
        if (!listaCondicionesEntradaDocumento.isEmpty()) {
            listaTramitesCondEntradaNoOk = comprobarCondicionesEntradaFirmaDoc(con, tEVO, listaCondicionesEntradaDocumento);
        }
        
        
        return listaTramitesCondEntradaNoOk;
    }

    public Vector comprobarCondicionesEntradaFirmaDoc(Connection con, TramitacionExpedientesValueObject tEVO, Vector listaCondicionesEntradaDocumento) throws SQLException {
        Vector noCumplenCondicion = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT doc_firma_estado FROM e_docs_firmas "
                + "LEFT JOIN e_docs_presentados ON (id_doc_presentado=presentado_cod)"
                + "WHERE presentado_mun = ? AND presentado_eje   = ? AND "
                + "presentado_num   = ? AND presentado_pro   =? and presentado_cod_doc = ?";
        
        for (Iterator it = listaCondicionesEntradaDocumento.iterator(); it.hasNext();) {
            GeneralValueObject condicion = (GeneralValueObject) it.next();
            ps = con.prepareStatement(sql);

            int i = 1;
            ps.setInt(i++, Integer.parseInt(tEVO.getCodMunicipio()));
            ps.setInt(i++, Integer.parseInt(tEVO.getEjercicio()));
            ps.setString(i++, tEVO.getNumeroExpediente());
            ps.setString(i++, tEVO.getCodProcedimiento());
            ps.setInt(i++, Integer.parseInt((String)condicion.getAtributo("codDocumento")));
            
            boolean documentoSubido = false;
            ArrayList<String> estadoFirmas = new ArrayList<String>();                        
            m_Log.debug(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                documentoSubido = true;              
                estadoFirmas.add(rs.getString("doc_firma_estado"));                
            }
            try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(ps);
            } catch (TechnicalException ex) {
                Logger.getLogger(TramitacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new SQLException();
            }
            String estadoFirmaDocumento = "";
            boolean condicionOK = false;
            if (documentoSubido) {
                estadoFirmaDocumento= FichaExpedienteDAO.getInstance().devolverEstadoFirma(estadoFirmas);
                if ("C".equals((String)condicion.getAtributo("estadoFirma"))) {
                    condicionOK= "F".equals(estadoFirmaDocumento)||"R".equals(estadoFirmaDocumento);
                } else {
                    condicionOK = estadoFirmaDocumento.equals((String)condicion.getAtributo("estadoFirma"));
                }
            } 
                       
            if (!condicionOK){
                String codTramite = (String)condicion.getAtributo("codTramite");
                String nombreTramite = (String)condicion.getAtributo("descTramite");
                GeneralValueObject tramiteNoCumple = new GeneralValueObject();
                // Almacenamos los datos del tramite que no se inicia.
                tramiteNoCumple.setAtributo("codTramite", codTramite);
                tramiteNoCumple.setAtributo("descTramite", nombreTramite);
                tramiteNoCumple.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
                GeneralValueObject condicionNoCumplida = new GeneralValueObject();
                condicionNoCumplida.setAtributo("tipoCondicion", "FIRMA");
                condicionNoCumplida.setAtributo("descDocumento", (String)condicion.getAtributo("descDocumento"));
                condicionNoCumplida.setAtributo("estadoFirma", (String)condicion.getAtributo("estadoFirma"));
                Vector listaCondiciones = new Vector();
                listaCondiciones.add(condicionNoCumplida);
                tramiteNoCumple.setAtributo("listaCondiciones", listaCondiciones);
                noCumplenCondicion.add(tramiteNoCumple);
            }
            
        }        
        return noCumplenCondicion;        
    }

    public boolean documentosPendientesDeFirma(TramitacionExpedientesValueObject tramExpVO, Connection con) throws TechnicalException{        
            boolean tienePendientes = true;
            String sql = "SELECT * FROM E_DOCS_FIRMAS LEFT JOIN E_DOCS_PRESENTADOS ON (ID_DOC_PRESENTADO=PRESENTADO_COD) "
                    + "WHERE DOC_FIRMA_ESTADO='O' AND PRESENTADO_EJE=? AND PRESENTADO_NUM = ?"
                    + "AND PRESENTADO_PRO = ? AND PRESENTADO_MUN=?";
            PreparedStatement ps= null;
            ResultSet rs = null;
          try {  
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(tramExpVO.getEjercicio()));
            ps.setString(i++, tramExpVO.getNumeroExpediente());
            ps.setString(i++, tramExpVO.getProcedimiento());        
            ps.setInt(i++, Integer.parseInt(tramExpVO.getCodMunicipio()));            
            rs = ps.executeQuery();            
            tienePendientes = rs.next();                        
            
        } catch (SQLException ex) {
            Logger.getLogger(TramitacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de base de datos comprobando si hay documentos presentados pendientes de firma", ex);
        } finally{
              try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
              }catch(SQLException e){
                  m_Log.error("Error al cerrar recursos asociados a la conexión a BBDD: " + e.getMessage());
              }
          } 
          return tienePendientes;
    }

    public GeneralValueObject expedientePermiteSubsanacion (String expediente, Connection con) throws TechnicalException {
        GeneralValueObject tramite = null;
        m_Log.debug(TramitacionExpedientesDAO.class.getName() + " --> expedientePermiteSubsanacion");
        String sql = "SELECT SAL_TAC, SAL_TAA, SAL_TAN, CRO_TRA, CRO_OCU, TRA_PRO, TRA_MUN, CRO_EJE, CRO_NUM "
                + "FROM E_SAL S INNER JOIN E_TRA T ON S.SAL_TRA = T.TRA_COD "
                + "AND S.SAL_PRO = T.TRA_PRO INNER JOIN E_CRO C ON T.TRA_COD = C.CRO_TRA "
                + "AND T.TRA_PRO = C.CRO_PRO AND C.CRO_NUM = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        m_Log.debug("espedientePermiteSubsanacion SQL: " + sql);
        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i, expediente);
            rs = ps.executeQuery();
            if (rs.next()) {
                if ("T".equals(rs.getString("SAL_TAA")) && "T".equals(rs.getString("SAL_TAN")) &&
                    ("R".equals(rs.getString("SAL_TAC")) || "P".equals(rs.getString("SAL_TAC")))) {
                    GeneralValueObject ocuTra = new GeneralValueObject();
                    ocuTra.setAtributo("codMunicipio", rs.getString("TRA_MUN"));
                    ocuTra.setAtributo("codProcedimiento", rs.getString("TRA_PRO"));
                    ocuTra.setAtributo("ejercicio", rs.getString("CRO_EJE"));
                    ocuTra.setAtributo("numeroExpediente", rs.getString("CRO_NUM"));
                    ocuTra.setAtributo("codTramite", rs.getString("CRO_TRA"));
                    ocuTra.setAtributo("ocurrenciaTramite", rs.getString("CRO_OCU"));
                    //if (!rs.next()) {
                        // Sólo se tiene un trámite, por lo que se puede subsanar.
                        tramite = ocuTra;
                    //}
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TramitacionExpedientesDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de base de datos comprobando si el expeciente permite subsanacion", ex);
        }
        m_Log.debug(TramitacionExpedientesDAO.class.getName() + "<-- expedientePermiteTramitacion");
        return tramite;
    }
    
    public Vector finalizarConSubsanacion (TramitacionExpedientesValueObject tEVO, String[] params, boolean desFavorable)
            throws TramitacionException, TechnicalException, WSException, EjecucionSWException {
        Vector devolver = new Vector();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int resultado = 0;
        m_Log.debug(TramitacionExpedientesDAO.class.getName() + "--> finalizarConSubsanacion");
        try {
            // Obtener la conexión e iniciar la transacción
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            //Finalizar trámite
            resultado = TramitesExpedienteDAO.getInstance().finalizarTramiteConSubsanacion(adapt, con, tEVO, desFavorable);
            if (resultado > 0) {
                devolver = finalizacionComunConTramites(tEVO,adapt, con, params);
            }
        } catch (SQLException e) {
           rollBackTransaction(adapt, con);
           throw new TechnicalException(e.getMessage());
        } catch (BDException e) {
           rollBackTransaction(adapt, con);
           throw new TechnicalException(e.getMessage());
        } catch (EjecucionSWException eswe) {
           rollBackTransaction(adapt, con);
           throw eswe;
        } catch (TramitacionException te) {
           rollBackTransaction(adapt, con);
           throw te;
        } finally {
           if (resultado >= 0) {
               finTransaccion(adapt, con);
           } else {
               rollBackTransaction(adapt, con);
           }
           devolverConexion(adapt, con);
        }
        m_Log.debug(TramitacionExpedientesDAO.class.getName() + "<-- finalizarConSubsanacion");
        return devolver;
    }
    public String tratarCamposExpresion(AdaptadorSQL oad,Connection con,
                                         TramitacionExpedientesValueObject tEVO)
            throws TechnicalException, TramitacionException, Exception {

        String tramitesnoOK = "";
        Vector listaCamposExpresion = new Vector();        


        listaCamposExpresion = getListaCamposExpresion(oad,con,tEVO);
        if (m_Log.isDebugEnabled()){
            m_Log.debug("Numero de Campos de Entrada a comprobar de tipo expresión: " + listaCamposExpresion.size());}

        // Si hay alguna condición de entrada de tipo expresión.
        if (listaCamposExpresion.size() > 0) {
            try {
                tramitesnoOK = comprobarCamposExpresion(tEVO.getEstructuraDatosSuplExpediente(), 
                        tEVO.getValoresDatosSuplExpediente(), tEVO.getEstructuraDatosSuplTramites(), 
                        tEVO.getValoresDatosSuplTramites(), listaCamposExpresion);
                
                if(tramitesnoOK!=null && "".equals(tramitesnoOK))
                    tramitesnoOK = "0"; // Se verifican los expresiones de validación de campos numéricos => TODO OK

            } catch (Exception e) {
                throw e;
            }
        }else // Como no hay campos numéricos con una expresión de validación, todo correcto. RETURN -1
            tramitesnoOK ="1"; // No hay expresiones => TODO OK
        
        return tramitesnoOK;
    }

    public Vector getListaCamposExpresion(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject tEVO) {

        Statement stmt=null;
        ResultSet rs = null;
        Vector<GeneralValueObject> lista = new Vector<GeneralValueObject>();
        String tramite = "";
        if ("".equals(tEVO.getCodTramite()))
            tramite = "0"; 
        else
            tramite = tEVO.getCodTramite();
                 
        try { 
            String sql = " SELECT COD_CAMPO,EXPRESION,'TRAM' AS ORIGEN,TCA_ROT AS ROTULO " +
                         " FROM expresion_campo_num_tram " +
                         " INNER JOIN E_TML " +
                         " on e_tml.tml_mun = expresion_campo_num_tram.cod_organizacion " +
                         " AND e_tml.tml_pro = expresion_campo_num_tram.cod_procedimiento " +
                         " AND e_tml.tml_tra = expresion_campo_num_tram.cod_tramite " +
                         " INNER JOIN E_TCA " +
                         " ON E_TML.TML_MUN = E_TCA.TCA_MUN " +
                         " AND E_TML.TML_PRO = E_TCA.TCA_PRO " +
                         " AND E_TML.TML_TRA = E_TCA.TCA_TRA " +
                         " AND E_TCA.TCA_COD = COD_CAMPO " +
                         " WHERE e_tml.tml_mun = " + tEVO.getCodMunicipio() +
                         " AND e_tml.tml_pro = '" + tEVO.getCodProcedimiento() + "' " +
                         " AND e_tml.tml_tra = " + tramite + 
                         " AND E_TCA.TCA_ACTIVO = 'SI' " +
                         " UNION " +
                         " SELECT COD_CAMPO,EXPRESION,'PROC' AS ORIGEN, PCA_ROT AS ROTULO  " +
                         " FROM expresion_campo_num_PROC " +                         
                         " INNER JOIN E_PCA " +
                         " ON COD_ORGANIZACION = E_PCA.PCA_MUN " +
                         " AND COD_PROCEDIMIENTO = E_PCA.PCA_PRO " +
                         " AND E_PCA.PCA_COD = COD_CAMPO " +
                         " AND expresion_campo_num_PROC.cod_campo = e_pca.pca_cod " +
                         " WHERE COD_ORGANIZACION = " + tEVO.getCodMunicipio() +
                         " AND E_PCA.PCA_ACTIVO = 'SI'" +
                         " AND COD_PROCEDIMIENTO = '" + tEVO.getCodProcedimiento() + "' ";            

            if (m_Log.isDebugEnabled()){
                m_Log.debug("TramitacionExpedientesDAO: Sentencia en getListaCondicionesEntradaExpresion --> " + sql);}
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject g = new GeneralValueObject();
                g.setAtributo("expresion", rs.getString("EXPRESION"));
                g.setAtributo("codTramite", tEVO.getCodTramite() );                    
                g.setAtributo("cod_campo",rs.getString("COD_CAMPO"));
                g.setAtributo("origen",rs.getString("ORIGEN"));
                g.setAtributo("rotulo",rs.getString("ROTULO"));

                lista.addElement(g);
            }

            if (m_Log.isDebugEnabled()){
                m_Log.debug("TramitacionDAO: Resultado --> numero condiciones entrada expresion.Tramite " + tEVO.getCodTramite() + ": " + lista.size());}
            rs.close();
            stmt.close();            
        }
        catch (Exception e) {
            lista = null;
            e.printStackTrace();
        }finally{
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
                
            }catch(SQLException e){
               m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }
    
 public String comprobarCamposExpresion(
            Vector estructuraDSExpediente, Vector valoresDSExpediente, Vector estructuraDSTramites,
            Vector valoresDSTramites, Vector listaExpresion)
            throws TechnicalException, Exception {
        
        
        int tamEstExp = estructuraDSExpediente.size();
        int tamEstTra = estructuraDSTramites.size();
        
        String[] camposDSExpediente = new String[tamEstExp];                
        String[] datosDSExpediente = new String[tamEstExp];
        String[] nombresDSExpediente = new String[tamEstExp];
        
        String[] camposDSTramites = new String[tamEstTra];
        String[] datosDSTramites = new String[tamEstTra];
        String[] nombresDSTramites = new String[tamEstTra];
        String[] tramitesCampos = new String[tamEstTra];
                
        boolean resultado = true;
        String salida = "";
        boolean valido = true;
       
        
        try 
        {
            //EXPEDIENTE
            for (int i=0; i < tamEstExp; i++){
                EstructuraCampo eC = (EstructuraCampo) estructuraDSExpediente.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valoresDSExpediente.get(i);
                                
                camposDSExpediente[i]  = eC.getCodCampo();                                
                nombresDSExpediente[i] = eC.getDescCampo();
                
                String valor = cF.getString(eC.getCodCampo());
                
                if(eC.getCodTipoDato()!=null && Integer.parseInt(eC.getCodTipoDato()) == ConstantesDatos.TIPO_CAMPO_NUMERICO && valor!=null && !"".equals(valor))
                    valor = valor.replace(".","");
            
                datosDSExpediente[i] = valor;
                
                m_Log.info("    EXPEDIENTE............................... ");
                m_Log.info("    NOMBRE ..... " + nombresDSExpediente[i]);
                m_Log.info("    CAMPO  ..... " +camposDSExpediente[i]);
                m_Log.info("    VALOR... " +datosDSExpediente[i]);
                m_Log.info("    ......................................... ");
            }   
            
            //TRAMITE            
            for (int i=0; i< tamEstTra; i++) 
            {
                EstructuraCampo eC = (EstructuraCampo) estructuraDSTramites.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valoresDSTramites.get(i);
                
                camposDSTramites[i] = eC.getCodCampo();                
                nombresDSTramites[i] = eC.getDescCampo();
                datosDSTramites[i] = cF.getString(eC.getCodCampo());   
                tramitesCampos[i] = eC.getCodTramite();
                             
                m_Log.info("    TRAMITE.................................. ");
                m_Log.info("    NOMBRE ..... " +nombresDSTramites[i]);
                m_Log.info("    CAMPO  ..... " +camposDSTramites[i] + "_T" + tramitesCampos[i]);
                m_Log.info("    VALOR  ..... " +datosDSTramites[i]);
                m_Log.info("    ......................................... ");                
            }

            GeneralValueObject g;

            for (int i=0; i< listaExpresion.size(); i++) 
            {
                g= (GeneralValueObject) listaExpresion.elementAt(i);
                valido = true;
                String expresion = (String) g.getAtributo("expresion");
                if (expresion != null && !"".equals(expresion.trim())){
                    String codTramite = (String) g.getAtributo("codTramite");
                    String origen = (String)g.getAtributo("origen");
                    String rotulo = (String)g.getAtributo("rotulo");

                    expresion = expresion.replace("/&lt;/g","<");
                    expresion = expresion.replace("/&gt;/g",">");
                    expresion = expresion.replace("&lt;","<");
                    expresion = expresion.replace("&gt;",">");
                    String auxiliar = expresion;

                    if (origen.equals("TRAM"))
                    {
                        for (int j=0; j< tamEstTra; j++) 
                        {
                            if (Campo_sin_valor(expresion,camposDSTramites[j]) && "".equals(datosDSTramites[j]))  
                                valido = false;

                            auxiliar = auxiliar.replace(" " + camposDSTramites[j] + "_T" + tramitesCampos[j] + " "," " + datosDSTramites[j] + " ");
                            auxiliar = auxiliar.replace("(" + camposDSTramites[j] + "_T" + tramitesCampos[j] + " ","(" + datosDSTramites[j] + " ");
                            auxiliar = auxiliar.replace(" " + camposDSTramites[j] + "_T" + tramitesCampos[j] + ")"," " + datosDSTramites[j] + ")");
                            auxiliar = auxiliar.replace("(" + camposDSTramites[j] + "_T" + tramitesCampos[j] + ")","(" + datosDSTramites[j] + ")");                        
                        }
                        for (int j=0; j< tamEstExp; j++) 
                        {
                            if (Campo_sin_valor(expresion,camposDSExpediente[j]) && "".equals(datosDSExpediente[j]))  
                                valido = false;

                            auxiliar = auxiliar.replace(" " + camposDSExpediente[j] + " "," " + datosDSExpediente[j] + " ");
                            auxiliar = auxiliar.replace("(" + camposDSExpediente[j] + " ","(" + datosDSExpediente[j] + " ");
                            auxiliar = auxiliar.replace(" " + camposDSExpediente[j] + ")"," " + datosDSExpediente[j] + ")");
                            auxiliar = auxiliar.replace("(" + camposDSExpediente[j] + ")","(" + datosDSExpediente[j] + ")");                        
                        }
                    }
                    else
                    {
                        for (int j=0; j< tamEstExp; j++) 
                        {
                            if (Campo_sin_valor(expresion,camposDSExpediente[j]) && "".equals(datosDSExpediente[j])) 
                                valido = false;

                            auxiliar = auxiliar.replace(" " + camposDSExpediente[j] + " "," " + datosDSExpediente[j] + " ");
                            auxiliar = auxiliar.replace("(" + camposDSExpediente[j] + " ","(" + datosDSExpediente[j] + " ");
                            auxiliar = auxiliar.replace(" " + camposDSExpediente[j] + ")"," " + datosDSExpediente[j] + ")");
                            auxiliar = auxiliar.replace("(" + camposDSExpediente[j] + ")","(" + datosDSExpediente[j] + ")");                        
                        }
                    }

                    m_Log.info("    VALIDACIONES  .....................");
                    m_Log.info("    TRAMITE       ... "+codTramite);
                    m_Log.info("    EXPRESION INI ... "+expresion);
                    m_Log.info("    EXPRESION FIN ... "+auxiliar);        
                    auxiliar = auxiliar.replace("x","*");
                    auxiliar = auxiliar.replace("X","*");
                    if (valido == false)
                    {
                        if( salida.length() >0)
                            salida = salida + "#" + rotulo;
                        else
                            salida = rotulo;
                    }
                    else
                    {
                        resultado = chequear_expresion(auxiliar);
                        if (resultado == false)
                        {
                            if( salida.length() >0)
                                salida = salida + "#" + rotulo;
                            else
                                salida = rotulo;
                        }
                    }  
                }
            }
        } catch (Exception e){
          throw e;
        }        
        return salida;
    }    
    private boolean chequear_expresion(String expresion) throws TechnicalException
    {            
        int PA= 0;
        int PO= 0;
        int contador = 1;
        int tamano = 0;
        String aux ="";        
        boolean terminado = false;                    
        Vector Operaciones = new Vector();    
        try
        {
            while (terminado == false)
            {
                PA = expresion.indexOf("AND");
                PO = expresion.indexOf("OR");
                aux = "";           

                if(PA > PO) 
                {
                    if (PO > 0)
                    {
                        tamano = PO;
                        aux = "OR";
                    }
                    else
                    {
                        tamano = PA;
                        aux = "AND";
                    }
                }
                else if (PO > PA)
                {              
                    if (PA > 0)
                    {
                        tamano = PA;                
                        aux= "AND";
                    }
                    else
                    {
                        tamano = PO;
                        aux = "OR";
                    }
                }            
                else
                {
                    tamano = expresion.length();
                    aux = "";
                }

                Operaciones.add(expresion.substring(0,tamano));            
                if (!aux.equals(""))
                        Operaciones.add(aux);
                expresion = expresion.substring(tamano+aux.length(),expresion.length());
                contador++;

                if (PA == PO)
                    terminado = true; 
            }
            terminado = calcula_expresion(Operaciones);
        }catch (Exception e){
            throw e;
        }finally{        
            return terminado;
        }
    }    
    private boolean calcula_expresion(Vector expresiones) throws TechnicalException
    {            
        int i = 0;
        int j = 0;
        int posicion = 0;

        String[] operadores = {"<>",">=","<=",">","<","="}; 
        String elemento_1 = "";
        String elemento_comparador = "";
        String elemento_2 = "";
        String elemento_final = "";
        String elemento_pr = "";
        String aux = "";     
        Vector cadena_final = new Vector();
        boolean comparacion = true;
        
        try
        {                
            for (i = 0;i<expresiones.size();i++) 
            {
                aux = expresiones.get(i).toString().replace("(", ""); 
                aux = aux.replace(")","");                
                m_Log.debug("    EXPRESION     ... "+aux);     

                if (aux.equals("AND") || aux.equals("OR"))
                    cadena_final.add(aux);

                if (comparacion = true && !aux.equals("AND") && !aux.equals("OR"))
                {                
                    elemento_comparador = "";
                    posicion= 0;
                    for (j = 0 ;j< 6;j++)
                    {
                        if (posicion < 1) 
                        {
                            if (aux.indexOf(operadores[j]) > 0);
                                elemento_comparador = operadores[j];                    
                            if (aux.indexOf(elemento_comparador)>0)
                                posicion = aux.indexOf(elemento_comparador);                                        
                        }
                    }
                    
                    if (elemento_comparador.length() == 1)
                        elemento_comparador = elemento_comparador.replace("=","==");                        
                    
                    elemento_comparador = elemento_comparador.replace("<>","!=");
                    
                    elemento_1 = aux.substring(0,posicion);
                    elemento_2 = aux.substring(posicion+elemento_comparador.length(),aux.length());
                                                  
                    elemento_final = evalua_cadena.evalua(elemento_1 +" "+ elemento_comparador +" "+ elemento_2);       
                    m_Log.debug("    RESULTADO     ... "+elemento_final);                     
                    cadena_final.add(elemento_final);
                }                               
            }
            comparacion = calcula_cadena_logica(cadena_final);
            return comparacion;
        }catch (Exception e){ 
            throw e;
        }finally{
            return comparacion;
        }                
    }
    private boolean calcula_cadena_logica(Vector cadena) throws TechnicalException
    {        
        int i= 0;
        String comparacion = "";
        boolean salida = false;        
        
        try
        {
            for(i = 0;i<cadena.size();i++)
            {
                comparacion = comparacion + " " + cadena.get(i).toString().trim();
            }
            m_Log.debug("    CADENA INI    ... "+comparacion);             
            for(i = 0;i<cadena.size();i++)
            {
                comparacion = comparacion.replaceFirst("true AND true","true");
                comparacion = comparacion.replaceFirst("false AND false","false");
                comparacion = comparacion.replaceFirst("true AND false","false");
                comparacion = comparacion.replaceFirst("false AND true","false");
                comparacion = comparacion.replaceFirst("true OR true","true");
                comparacion = comparacion.replaceFirst("false OR false","false");
                comparacion = comparacion.replaceFirst("true OR false","true");
                comparacion = comparacion.replaceFirst("false OR true","true");                
            }             
            m_Log.debug("    CADENA FIN    ... "+comparacion); 
            if (comparacion.trim().equals("true"))
                salida = true;
            else
                salida = false;
            
        }catch (Exception e){
            throw e;
        }finally{
            return salida;
        }    

    }
    public Vector cargaEstructuraDatosSuplementariosExped(TramitacionExpedientesValueObject tEVO, AdaptadorSQL oad, Connection con) throws TechnicalException {
  
       Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();

        String desdeJsp=tEVO.getDesdeJsp();

        try{
            st = con.createStatement();

            from = tca_cod + "," + tca_des + "," + tca_plt + "," + plt_url + "," + tca_tda + "," +
                    tca_tam + "," + tca_mas + "," + tca_obl + "," + tca_nor + "," + tca_rot + "," + tca_vis + "," + tca_activo+ ",TCA_BLOQ,TCA_TRA ";
            where = tca_mun + " = " + tEVO.getCodMunicipio() + " AND " + tca_pro + " = '" +
                    tEVO.getCodProcedimiento() + "'";            
            String[] join = new String[5];
            join[0] = "E_TCA";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join[4] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"9","9"};
            sql += oad.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(tca_cod);
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(tca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(tca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(tca_tda);
                eC.setCodTipoDato(codTipoDato);
                String tamano = rs.getString(tca_tam);
                eC.setTamano(tamano);
                String mascara = rs.getString(tca_mas);
                eC.setMascara(mascara);
                String obligatorio = rs.getString(tca_obl);
                eC.setObligatorio(obligatorio);
                String numeroOrden = rs.getString(tca_nor);
                eC.setNumeroOrden(numeroOrden);
                String rotulo = rs.getString(tca_rot);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                String bloqueado = rs.getString("TCA_BLOQ");
                eC.setBloqueado(bloqueado);
                eC.setRotulo(rotulo);
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                eC.setCodTramite(rs.getString("TCA_TRA"));
                eC.setOcurrencia(tEVO.getOcurrenciaTramite());
                lista.addElement(eC);
            }
            rs.close();
            st.close();
        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            return lista;
        }
    } 
    
    public Vector getListaCamposExpresionCalculada(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject tEVO) { 

        Statement stmt = null;
        ResultSet rs = null; 
        Vector<GeneralValueObject> lista = new Vector<GeneralValueObject>(); 
        String tramite = ""; 
        String sql = "";
        
        if (tEVO.getCodTramite() == null)  
            tramite = ""; 
        else 
            tramite = tEVO.getCodTramite();
                 
        try {  
            if ("".equals(tramite))
            {
                sql = " SELECT DISTINCT COD_CAMPO,EXPRESION,'PROC' AS ORIGEN  " +
                         " FROM expresion_campo_cal_PROC " +
                         " INNER JOIN E_PCA  ON Expresion_campo_cal_PROC.cod_procedimiento  = E_PCA.PCA_PRO " + 
                         " AND expresion_campo_cal_PROC.cod_campo = e_pca.pca_cod " +
                         " WHERE e_PCA.PCA_mun = " + tEVO.getCodMunicipio() +
                         //" AND E_PCA.PCA_ACTIVO = 'SI' " +
                         " AND E_PCA.PCA_ACTIVO = 'SI' AND PCA_OCULTO='NO' " +
                         " AND Expresion_campo_cal_PROC.cod_procedimiento = '" + tEVO.getCodProcedimiento() + "' ";                                                                  
            }
            else
            {
                sql = " SELECT DISTINCT COD_CAMPO,EXPRESION,'TRAM' AS ORIGEN " +
                         " FROM expresion_campo_cal_tram " +
                         " INNER JOIN E_TML " + 
                         " on e_tml.tml_mun = expresion_campo_cal_tram.cod_organizacion " +
                         " AND e_tml.tml_pro = expresion_campo_cal_tram.cod_procedimiento " +
                         " AND e_tml.tml_tra = expresion_campo_cal_tram.cod_tramite " +
                         " INNER JOIN E_TCA " +
                         " ON E_TML.TML_MUN = E_TCA.TCA_MUN " +
                         " AND E_TML.TML_PRO = E_TCA.TCA_PRO " +
                         " AND E_TML.TML_TRA = E_TCA.TCA_TRA " +
                         " WHERE e_tml.tml_mun = " + tEVO.getCodMunicipio() +
                         " AND e_tml.tml_pro = '" + tEVO.getCodProcedimiento() + "' " +
                         " AND e_tml.tml_tra = " + tramite + 
                         " AND E_TCA.TCA_ACTIVO = 'SI' AND E_TCA.TCA_OCULTO='NO'";
            }
            
            if (m_Log.isDebugEnabled()){ 
                m_Log.debug("TramitacionExpedientesDAO: Sentencia en getListaCamposExpresionCalculada --> " + sql);} 
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql); 

            while (rs.next()) { 
                GeneralValueObject g = new GeneralValueObject(); 
                g.setAtributo("expresion", rs.getString("EXPRESION")); 
                g.setAtributo("codTramite", tEVO.getCodTramite());                     
                g.setAtributo("cod_campo",rs.getString("COD_CAMPO")); 
                g.setAtributo("origen",rs.getString("ORIGEN"));                 

                lista.addElement(g); 
            } 

            if (m_Log.isDebugEnabled()){
                m_Log.debug("TramitacionDAO: Resultado --> numero condiciones entrada expresion calculada .Tramite " + tramite + ": " + lista.size());} 
            rs.close();
            stmt.close();
        } 
        catch (Exception e) {
            lista = null;
            e.printStackTrace(); 
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        } 
        return lista;
    } 
    private boolean Campo_sin_valor(String expresion, String campo)
    {        
        boolean vacio = false;
        if (expresion.indexOf(" " + campo + " ") >= 0)
            vacio = true;
        else if (expresion.indexOf("(" + campo + ")") >= 0)
            vacio = true;
        else if (expresion.indexOf("(" + campo + " ") >= 0)
            vacio = true;
        else if (expresion.indexOf(" " + campo + ")") >= 0)
            vacio = true;
        else if (expresion.indexOf(" " + campo + "_T") >= 0)
            vacio = true;
        else if (expresion.indexOf("(" + campo + "_T") >= 0)
            vacio = true;    
        return vacio;
    }
    public String recuperarExpresion(String[] params,GeneralValueObject gVO) {  
        
        AdaptadorSQLBD oad = null;
        Connection con = null;        
        Statement stmt = null;
        ResultSet rs = null;
        String retorno = "";
        try 
        {
                oad = new AdaptadorSQLBD(params);
                con = oad.getConnection(); 
 
                String codMunicipio = (String)gVO.getAtributo("codMunicipio");
                String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");       
                String campo = (String)gVO.getAtributo("campo");
                String tramite = (String)gVO.getAtributo("tramite");
                
                String sql = "";                 
                           
                sql = " SELECT EXPRESION FROM EXPRESION_CAMPO_CAL_TRAM " +                      
                      " WHERE COD_ORGANIZACION = " + codMunicipio +
                      " AND COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +                      
                      " AND COD_CAMPO = '" + campo +"'" + 
                      " AND COD_TRAMITE = " + tramite;               
            
            stmt = con.createStatement(); 
            rs = stmt.executeQuery(sql);       
            if (rs.next()) 
            {
                retorno = rs.getString("EXPRESION").trim();       
                retorno = retorno.replace("+",";SUMA;");
            }
            else 
                retorno = ""; 
        } 
        catch (Exception e) { 
            retorno = "";
            e.printStackTrace();  
            
        }finally{ 
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();             
                if(con!=null) con.close();
            }catch(SQLException e){ 
               e.printStackTrace();
           } 
        }
        return retorno;
    }
     public String recuperarCodigoExterno(Connection con,GeneralValueObject gVO) {  
    /*
     * @Descripcion --> Recupera el campos desplegable externo asociado a un campo suplementario
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */                
        Statement stmt = null; 
        ResultSet rs = null;       
        String retorno = "";        
        try 
        {            

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");        
            String campo = (String)gVO.getAtributo("campo");
            String ocurrencia = (String)gVO.getAtributo("codOcu");
            String tramite = (String)gVO.getAtributo("codTram");

            String sql = " SELECT TCA_DESPLEGABLE FROM E_TCA " +                      
                         " WHERE TCA_MUN = " + codMunicipio +
                         " AND TCA_PRO = '" + codProcedimiento + "'" +                      
                         " AND TCA_COD = '" + campo +"'" +
                         " AND TCA_TRA = " + tramite;               
            
            stmt = con.createStatement(); 
            m_Log.debug(sql);
            rs = stmt.executeQuery(sql);       
            if (rs.next()) 
                retorno = rs.getString("TCA_DESPLEGABLE").trim();                                                      
            else
                retorno = "";            
        } 
        catch (Exception e) { 
            retorno = "";
            e.printStackTrace();  
        }finally{ 
            try{
                rs.close();
                stmt.close();             
            }catch(SQLException e){ 
               e.printStackTrace();
           } 
        }
        return retorno; 
    }
    public String[] recuperarParametrosConexionExternos(Connection con,String campo_conexion) {  
    /*
     * @Descripcion --> Recupera los parametros para realizar la conexion a una base de datos externa.
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */          
        Statement stmt = null; 
        ResultSet rs = null;
        String[] retorno = new String[7]; 
        String[] parametros = new String[7];
        try 
        {                                                                                             
                String sql = " SELECT DRIVER_JDBC,URL_JDBC,USUARIO,PASSWORD,TABLA,CAMPO_CODIGO, CAMPO_VALOR " +
                             " FROM DESPLEGABLE_EXTERNO " +                      
                             " WHERE CODIGO = '" + campo_conexion +"'";          
            
            stmt = con.createStatement(); 
            m_Log.debug(sql);
            rs = stmt.executeQuery(sql);       
            if (rs.next()) 
            {                                                                                      
                parametros[0] = rs.getString("DRIVER_JDBC").trim(); 
                parametros[1] = rs.getString("URL_JDBC").trim(); 
                parametros[2] = rs.getString("USUARIO").trim(); 
                parametros[3] = rs.getString("PASSWORD").trim();                                      
                parametros[4] = rs.getString("TABLA").trim(); 
                parametros[5] = rs.getString("CAMPO_CODIGO").trim();  
                parametros[6] = rs.getString("CAMPO_VALOR").trim();  
            }
            retorno = parametros;     
        } 
        catch (Exception e) {           
            e.printStackTrace();  
        }finally{ 
            try{
                rs.close();
                stmt.close();             
            }catch(SQLException e){ 
               e.printStackTrace();
           } 
        }
        return retorno; 
    }
    public ArrayList<GeneralValueObject> recuperarDatosExternos(String[] parametros) {  
    /*
     * @Descripcion --> Recupera los datos del desplegable externo realizando la conexion contra la base de datos externa.
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     * @parametros          
        *** 0 - Driver jdbc
        *** 1 - url jdbc
        *** 2 - usuaio bbdd
        *** 3 - password
        *** 4 - tabla de consulta
        *** 5 - campo de consulta
     */
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String retorno = "";
        ArrayList<GeneralValueObject> resultados = new ArrayList<GeneralValueObject>();
        try
        {
            ConexionExterna oadExt = new ConexionExterna();
            con = oadExt.getConnection(parametros);
            String tabla_externa = parametros[4].toString();
            String campo_externo = parametros[5].toString();
            String valor_externo = parametros[6].toString();
            
            String sql = " SELECT " + campo_externo + ","+valor_externo+" FROM " + tabla_externa;
            
            stmt = con.createStatement();
            m_Log.debug(sql);
            rs = stmt.executeQuery(sql); 
            
            while(rs.next()) 
            {
                GeneralValueObject gVO=new GeneralValueObject();
                gVO.setAtributo("codigo", rs.getString(campo_externo) );
                gVO.setAtributo("valor", rs.getString(valor_externo) );
                /*if ("".equals(retorno))
                    retorno = rs.getString(campo_externo); 
                else
                    retorno = retorno + "||" + rs.getString(campo_externo); */
                resultados.add(gVO);
            }
            
           
        } 
        catch (Exception e) {   
            retorno = "";
            e.printStackTrace();    
        }finally{   
            try{ 
                rs.close();
                stmt.close();   
                if(con!=null) con.close();
            }catch(SQLException e){   
               e.printStackTrace();
           }   
        }
        return resultados;
    }  
    
    
    
    
    /**
     * Cuenta el número de interesados que tienen asignado un expediente
     * @param codOrganizacion: Código de la organización/municipio
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la BBDD
     * @return int
     */
    public int getNumInteresadosExpediente(int codOrganizacion,String numExpediente,Connection con){
        int numero = 0;
        ResultSet rs = null;
        Statement st = null;
        ResultSet rs1 = null;
        Statement st1 = null;
        
        try{
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];
            
            String sql = "SELECT COUNT(*) AS NUMERO FROM E_EXT WHERE EXT_NUM='" + numExpediente + "' AND EXT_EJE=" + ejercicio + " AND EXT_PRO='" + codProcedimiento + "' AND EXT_MUN=" + codOrganizacion;
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                numero = rs.getInt("NUMERO");
            }
            // Si el número es cero tal vez se esta consultando un expediente que está en histórico. Se consulta en la tabla de Historio de terceros
            if (numero == 0) {

                sql = "SELECT COUNT(*) AS NUMERO FROM HIST_E_EXT WHERE EXT_NUM='" + numExpediente + "' AND EXT_EJE=" + ejercicio + " AND EXT_PRO='" + codProcedimiento + "' AND EXT_MUN=" + codOrganizacion;
                m_Log.debug(sql);
                st1 = con.createStatement();
                rs1 = st1.executeQuery(sql);

                while (rs1.next()) {
                    numero = rs1.getInt("NUMERO");
                }
            }
            
        }catch(SQLException e){
            m_Log.error("Error al recuperar el número de interesados del expediente: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(rs1!=null) rs1.close();
                if(st1!=null) st1.close();
               
            }catch(SQLException e){
                m_Log.error("Error cerrando las conexiones: " + e.getMessage());
            }
            
        }        
           return numero;     
    }
 
    
    
    /*********** prueba *****************/
    
    /**
     * Recupera la estructura de campos suplementarios de un trámite de un procedimiento
     * @param tEVO: Objeto de tipo TramitacionExpedientesValueObject con loa datos del trámite y expediente
     * @param oad: Objeto de tipo AdaptadorSQLBD necesario para construir alguna de las consultas sql
     * @param con: Conexión a la BBDD
     * @return
     * @throws TechnicalException 
     */
    public Vector<EstructuraCampo> cargaEstructuraDatosSuplementarios(TramitacionExpedientesValueObject tEVO, AdaptadorSQLBD oad, Connection con) throws TechnicalException{

        m_Log.info("**************------------->cargaEstructuraDatosSuplementarios<----------------------*****************");
        
        Statement st = null, st2 = null, st3 = null;
        ResultSet rs = null, rs2 = null, rs3 = null;
        String sql3 = "";
        Vector<EstructuraCampo> lista = new Vector<EstructuraCampo>();
        String numero = tEVO.getNumeroExpediente();        
        String campoActivo = "";

        try{
        
            String desdeJsp=tEVO.getDesdeJsp();

            String from = tca_cod + ", " + tca_des + ", " + tca_plt + ", " + plt_url + ", " + tca_tda + ", " +
                    tca_tam + ", " + tca_mas + ", " + tca_obl + ", " + tca_nor + ", " + tca_rot + ", " +
                    tca_vis + ", " + tca_activo + ", " + tca_desplegable+ ", TCA_BLOQ, PLAZO_AVISO, PERIODO_PLAZO, PCA_GROUP, TCA_POS_X, TCA_POS_Y";
            String where = tca_mun + " = " + tEVO.getCodMunicipio() + " AND " + tca_pro + " = '" +
                    tEVO.getCodProcedimiento() + "' AND " + tca_tra + " = " + tEVO.getCodTramite() + " AND " +
                    tca_activo + " = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND TCA_OCULTO='NO'";
            String[] join = new String[5];
            join[0] = "E_TCA";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join[4] = "false";

            String sql = oad.join(from,where,join);
            String parametros[] = {"9","9"};
            sql += oad.orderUnion(parametros);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            Hashtable<String, ArrayList<String>>  CACHE_CAMPOS_DESPLEGABLES = new Hashtable<String,ArrayList<String>>();            
            
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(tca_cod);
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(tca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(tca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(tca_tda);
                eC.setCodTipoDato(codTipoDato);
                String tamano = rs.getString(tca_tam);
                eC.setTamano(tamano);
                String mascara = rs.getString(tca_mas);
                eC.setMascara(mascara);
                String obligatorio = rs.getString(tca_obl);
                eC.setObligatorio(obligatorio);
                String numeroOrden = rs.getString(tca_nor);
                eC.setNumeroOrden(numeroOrden);
                String rotulo = rs.getString(tca_rot);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                
                String bloqueado = rs.getString("TCA_BLOQ");
                eC.setBloqueado(bloqueado);
                //PLAZO_AVISO, PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                eC.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                eC.setCheckPlazoFecha(checkPlazoFecha);
                eC.setRotulo(rotulo);
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String desplegable = "";
                eC.setCodTramite(tEVO.getCodTramite());
                eC.setOcurrencia(tEVO.getOcurrenciaTramite());
                try {
                    //Campos de tipo fecha
                     if (("3").equals(codTipoDato)){
                         m_Log.debug("TIPO DE DATO FECHA...");
                         m_Log.debug("CodCampo::" + codCampo);
                         st3 = con.createStatement();
                         sql3 = "SELECT PLAZO_ACTIVADO FROM  E_TFET WHERE TFET_COD ='"+
                                codCampo + "' AND TFET_NUM='"+
                                numero + "'";
                         rs3 = st3.executeQuery(sql3);
                         if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
                         while (rs3.next()) {
                            campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                            m_Log.debug("campoActivo:::" + campoActivo);
                            eC.setCampoActivo(campoActivo);
                        }
                        SigpGeneralOperations.closeStatement(st3);
                        SigpGeneralOperations.closeResultSet(rs3);
                    }
                    if ( !rs.getString(tca_desplegable).equals(null)) {
                        desplegable = rs.getString(tca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        
                        
                            ArrayList<String> CAMPO_DESPLEGABLE = CACHE_CAMPOS_DESPLEGABLES.get(desplegable);
                            if(CAMPO_DESPLEGABLE!=null){                                
                                
                                for(int j=0;j<CAMPO_DESPLEGABLE.size();j++){                                    
                                    String dato = CAMPO_DESPLEGABLE.get(j);
                                    if(dato!=null){
                                        String[] datos = dato.split("-");
                                        listaCod.add("'" + datos[0] + "'");
                                        listaDesc.add("'" +datos[1] + "'");   
                                        listaEstado.add("'"+datos[2] +"'");
                                    }                                    
                                }
                                
                                eC.setListaCodDesplegable(listaCod);
                                eC.setListaDescDesplegable(listaDesc);      
                                eC.setListaEstadoValorDesplegable(listaEstado);
                            }else{ 
                                
                                //Vector listaCod = new Vector();
                                //Vector listaDesc = new Vector();
                                String sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", "+ des_val_estado +" FROM E_DES_VAL WHERE " +
                                       des_val_campo +"='"+ desplegable + "' ORDER BY " + des_val_desc + " ASC";

                                st2 = con.createStatement();
                                if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                                rs2 = st2.executeQuery(sql2);

                                ArrayList<String> valoresDesplegable = new ArrayList<String>();
                                while (rs2.next()) {
                                    String codigoValor = rs2.getString(des_val_cod);
                                    String descValor = rs2.getString(des_val_desc);
                                     String estadoValor=rs2.getString(des_val_estado);

                                    listaCod.addElement("'" + codigoValor + "'");
                                    listaDesc.addElement("'" + descValor + "'");
                                    listaEstado.addElement("'"+estadoValor+"'");
                                    

                                     valoresDesplegable.add(codigoValor + "-" + descValor+"-"+estadoValor);

                                }// while
                                CACHE_CAMPOS_DESPLEGABLES.put(desplegable,valoresDesplegable);

                                rs2.close();
                                st2.close();
                                eC.setListaCodDesplegable(listaCod);
                                eC.setListaDescDesplegable(listaDesc);     
                                eC.setListaEstadoValorDesplegable(listaEstado);
                            }
                       
                    }
                } catch (NullPointerException e){
                }
                String agrupacionCampo = rs.getString("PCA_GROUP");
                if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo)){
                    eC.setCodAgrupacion(agrupacionCampo);
                }else{
                    eC.setCodAgrupacion("DEF");
                }//if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo))
                if(codTipoDato != null && !"".equalsIgnoreCase(codTipoDato) && ("1".equalsIgnoreCase(codTipoDato) 
                                || "3".equalsIgnoreCase(codTipoDato)
                                || "8".equalsIgnoreCase(codTipoDato) 
                                || "9".equalsIgnoreCase(codTipoDato))){
                    eC.setTamanoVista("50");
                }else{
                    eC.setTamanoVista("100");
                }//
                String posX = String.valueOf(rs.getInt("TCA_POS_X"));
                String posY = String.valueOf(rs.getInt("TCA_POS_Y"));                
                if((posX != null) && (posY != null)){
                    eC.setPosicionar(true);
                }//if((posX != null) && (posY != null))
                eC.setPosX(posX);
                eC.setPosY(posY);
                lista.addElement(eC);
            }
            rs.close();
            st.close();
        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
            throw new TechnicalException("Error al recuperar la estructura de los campos suplementarios a nivel de trámite: " + e.getMessage());
        }finally {
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            try{
                if(rs!=null) rs.close();
                if(rs2!=null) rs2.close();
                if(rs3!=null) rs3.close();
                if(st!=null) st.close();
                if(st2!=null) st2.close();
                if(st3!=null) st3.close();
                
            }catch(SQLException e) {
                e.printStackTrace();
            }
            return lista;
        }
    }

    
    
    
    
    public Vector cargaValoresDatosSuplementarios(TramitacionExpedientesValueObject tEVO,Vector eCs, AdaptadorSQLBD oad, Connection con) throws TechnicalException {
                
        Vector lista = new Vector();
        CamposFormulario cF = null;
        
        
        CamposFormulario VALORES_CAMPOS_NUMERICOS = null;
        CamposFormulario VALORES_CAMPOS_TEXTO = null;
        CamposFormulario VALORES_CAMPOS_FECHA = null;
        CamposFormulario VALORES_CAMPOS_TEXTO_LARGO = null;
        CamposFormulario VALORES_CAMPOS_FICHERO = null;
        CamposFormulario VALORES_CAMPOS_NUMERICOS_CALCULADOS = null;
        CamposFormulario VALORES_CAMPOS_FECHA_CALCULADOS = null;
        CamposFormulario VALORES_CAMPOS_DESPLEGABLE = null;
        CamposFormulario VALORES_CAMPOS_DESPLEGABLE_EXTERNO = null;
        

        try{
            
            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                if(m_Log.isDebugEnabled()) m_Log.debug("COD TIPO DATO ....................." + codTipoDato);
                if("1".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_NUMERICOS!=null)
                        cF = VALORES_CAMPOS_NUMERICOS;
                    else{
                        VALORES_CAMPOS_NUMERICOS = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_NUMERICOS;
                    }
                    
                } else if("2".equals(codTipoDato)) {
                    if(VALORES_CAMPOS_TEXTO!=null)
                        cF = VALORES_CAMPOS_TEXTO;
                    else{
                        VALORES_CAMPOS_TEXTO = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_TEXTO;
                    }
                    
                } else if("3".equals(codTipoDato)) {
                    if(VALORES_CAMPOS_FECHA!=null)
                        cF = VALORES_CAMPOS_FECHA;
                    else{
                        VALORES_CAMPOS_FECHA = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad,con,tEVO,mascara);
                        cF = VALORES_CAMPOS_FECHA;
                    }
                } else if("4".equals(codTipoDato)) {
                     if(VALORES_CAMPOS_TEXTO_LARGO!=null)
                        cF = VALORES_CAMPOS_TEXTO_LARGO;
                    else{
                        VALORES_CAMPOS_TEXTO_LARGO = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_TEXTO_LARGO;
                     }
                } else if("5".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_FICHERO!=null)
                        cF = VALORES_CAMPOS_FICHERO;
                    else{
                        VALORES_CAMPOS_FICHERO = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_FICHERO;
                    }
                } else if("6".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_DESPLEGABLE!=null)
                        cF = VALORES_CAMPOS_DESPLEGABLE;
                    else{
                        VALORES_CAMPOS_DESPLEGABLE = DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_DESPLEGABLE;
                    }
                } else if("8".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_NUMERICOS_CALCULADOS!=null)
                        cF = VALORES_CAMPOS_NUMERICOS_CALCULADOS;
                    else{
                        VALORES_CAMPOS_NUMERICOS_CALCULADOS = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad,con,tEVO);
                        cF = VALORES_CAMPOS_NUMERICOS_CALCULADOS;
                    }
                } else if("9".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_FECHA_CALCULADOS!=null)
                        cF = VALORES_CAMPOS_FECHA_CALCULADOS;
                    else{
                        VALORES_CAMPOS_FECHA_CALCULADOS = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad,con,tEVO,mascara);
                        cF = VALORES_CAMPOS_FECHA_CALCULADOS;
                    }
                } else if("10".equals(codTipoDato)) {
                    
                    if(VALORES_CAMPOS_DESPLEGABLE_EXTERNO!=null)
                        cF = VALORES_CAMPOS_DESPLEGABLE_EXTERNO;
                    else{
                        VALORES_CAMPOS_DESPLEGABLE_EXTERNO = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad,con,tEVO);
                        cF = VALORES_CAMPOS_DESPLEGABLE_EXTERNO;
                    }
                }
                lista.addElement(cF);
            }

        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
            throw new TechnicalException("Error al recuperar los valores de los campos suplementarios a nivel de trámite: " + e.getMessage());
        }finally {           
            return lista;
        }
    }
    
    
    
   /**
    * Carga los datos de un trámite de un expediente que está en las tablas del histórico
    * @param tEVO: Objeto de la clase TramitacionExpedientesValueObject
    * @param params: Parámetros de conexión a la BD
    * @return TramitacionExpedientesValueObject con la información del trámite
    * @throws AnotacionRegistroException 
    */ 
    public TramitacionExpedientesValueObject cargarDatosHistorico(TramitacionExpedientesValueObject tEVO,String[] params)
            throws AnotacionRegistroException,TechnicalException {

        //Comprobamos en caso de que exista un portafirmas externo si los documentos de tramitacion estan firmados y 
        //Recuperamos la propiedad que nos indica si existe un portafirmas externo
        if(m_Log.isDebugEnabled()) m_Log.debug("Comprobamos si existe un portafirmas externo");
        Boolean existePortafirmasExterno = 
                PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(tEVO.getCodOrganizacion());
        if(m_Log.isDebugEnabled()) m_Log.debug("existe un portafirmas externo = " + existePortafirmasExterno);

        if(existePortafirmasExterno){
            PluginPortafirmasExternoCliente pluginPortafirmasExterno =
                    PluginPortafirmasExternoClienteFactoria.getImplClass(tEVO.getCodMunicipio());
            
            String resultado = pluginPortafirmasExterno.actualizarFirmasDocumentosTramitacion(tEVO, params);
        }//if(existePortafirmasExterno)
        
        m_Log.debug("TramitacionExpedientesDAO.cargarDatosHistorico");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector<TramitacionExpedientesValueObject> listaDocumentos = new Vector<TramitacionExpedientesValueObject>();
        Vector listaCodDocumentosTramite = new Vector();
        Vector<SiguienteTramiteTO> listaTramitesFavorables = new Vector<SiguienteTramiteTO>();
        Vector<SiguienteTramiteTO> listaTramitesNoFavorables = new Vector<SiguienteTramiteTO>();
        Vector listaEnlaces = new Vector();
        UsuarioDAO userDAO = UsuarioDAO.getInstance();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            // PESTAÑA DE DATOS GENERALES

            from = tml_valor + "," + oad.convertir(cro_fei, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + cro_fei + ", " + oad.convertir(cro_fef, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + cro_fef + "," + tra_uin + "," + cro_utr + "," + tra_plz + "," + tra_ins +
                    "," + tra_und + "," + oad.convertir(cro_fip, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + cro_fip +
                    "," + oad.convertir(cro_fli, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+ " AS " + cro_fli + "," +
                    oad.convertir(cro_ffp, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " + cro_ffp + "," +
                    cml_valor + "," + cro_obs + ",cro_usf," + tra_prr +", tra_fin, TRA_GENERARPLZ,TRA_NOTIFICACION_ELECTRONICA, TRA_NOTIF_ELECT_OBLIG, TRA_NOTIF_FIRMA_CERT_ORG, PRO_INTOBL ";
            where = tra_mun + "=" + tEVO.getCodMunicipio() + " AND " + tra_pro + "='" + tEVO.getCodProcedimiento() +
                    "' AND " + tra_cod + "=" + tEVO.getCodTramite() + " AND " + cro_num + "='" +
                    tEVO.getNumeroExpediente() + "' AND " + cro_eje + "=" + tEVO.getEjercicio() + " AND " +
                    cro_ocu + "=" + tEVO.getOcurrenciaTramite();
            String[] join = new String[14];
            join[0] = "E_TRA";
            join[1] = "INNER";
            join[2] = "hist_e_cro";
            join[3] = "e_tra." + tra_mun + "=hist_e_cro." + cro_mun + " AND " +
                    "e_tra." + tra_pro + "=hist_e_cro." + cro_pro + " AND " +
                    "e_tra." + tra_cod + "=hist_e_cro." + cro_tra;
            join[4] = "INNER";
            join[5] = "e_tml";
            join[6] = "e_tra." + tra_mun + "=e_tml." + tml_mun + " AND " +
                    "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                    "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                    "e_tml." + tml_cmp + "='NOM' AND " +
                    "e_tml." + tml_leng + "='"+idiomaDefecto+"'";
            join[7] = "INNER";
            join[8] = GlobalNames.ESQUEMA_GENERICO + "a_cml a_cml";
            join[9] = "e_tra." + tra_cls + "=a_cml." + cml_cod + " AND " +
                    "a_cml." + cml_cmp + "='NOM' AND " +
                    "a_cml." + cml_leng + "='"+idiomaDefecto+"'";
            join[10] = "INNER";
            join[11] = "e_pro";
            join[12] = "e_tra." + tra_mun + "=e_pro." + pro_mun + " AND " +
                    "e_tra." + tra_pro + "=e_pro." + pro_cod;
            join[13] = "false";
            sql = oad.join(from,where,join);
            if(m_Log.isDebugEnabled()) m_Log.debug("TramitecinExpedientesDAO.cararDatos: " + sql);

            rs = st.executeQuery(sql);
            while (rs.next()) {
                String descTramite = rs.getString(tml_valor);
                tEVO.setTramite(descTramite);
                String fechaInicio = rs.getString(cro_fei);
                tEVO.setFechaInicio(fechaInicio);
                String fechaFin = rs.getString(cro_fef);
                tEVO.setFechaFin(fechaFin);
                String codUnidadInicio = rs.getString(tra_uin);
                tEVO.setCodUnidadInicioTram(codUnidadInicio);
                String codUnidadTramitadoraTram = rs.getString(cro_utr);
                tEVO.setCodUnidadTramitadoraTram(codUnidadTramitadoraTram);
                String plazo = rs.getString(tra_plz);
                tEVO.setPlazo(plazo);
                String unidadPlazo = rs.getString(tra_und);
                tEVO.setTipoPlazo(unidadPlazo);
                String fechaInicioPlazo = rs.getString(cro_fip);
                tEVO.setFechaInicioPlazo(fechaInicioPlazo);
                String fechaLimite = rs.getString(cro_fli);
                tEVO.setFechaLimite(fechaLimite);
                String fechaFinPlazo = rs.getString(cro_ffp);
                tEVO.setFechaFinPlazo(fechaFinPlazo);
                String descClasificacionTramite = rs.getString(cml_valor);
                tEVO.setClasificacionTramite(descClasificacionTramite);
                String observaciones = rs.getString(cro_obs);
                if (observaciones != null) {
                   tEVO.setObservaciones(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.unescapeJava(rs.getString(cro_obs))));
                }
                String instrucciones = rs.getString(tra_ins);
                if (instrucciones != null) {
                    tEVO.setInstrucciones(AdaptadorSQLBD.js_escape(instrucciones));
                }
                String procedimientoAsociado = rs.getString(tra_prr);
                tEVO.setProcedimientoAsociado(procedimientoAsociado);

                // Se establece el código de usuario que finaliza el trámite en el caso
                // de que haya fecha de fin del mismo.
                if (fechaFin != null && fechaFin.length() >= 1) {
                    int codUsuarioFinTramite = rs.getInt("CRO_USF");
                    String nameUserFinTramite = userDAO.getNombreUsuario(params, codUsuarioFinTramite);
                    m_Log.debug("Nombre usuario fin tramite: " + nameUserFinTramite);
                    tEVO.setNombreUsuario(nameUserFinTramite);
                }

                m_Log.debug("Fecha fin del trámite: " + tEVO.getFechaFin());
                m_Log.debug("Nombre usuario finaliza tramite: " + tEVO.getNombreUsuario());
                
                int plazoCercaFin = rs.getInt("TRA_FIN");
                m_Log.debug("________________CERCA FIN PLAZO ______________");
                m_Log.debug("________________ CERCA FIN PLAZO" + plazoCercaFin);
                m_Log.debug("________________ fecha inicio plazo " + fechaInicioPlazo);
                if (fechaFin == null && fechaLimite != null && !"".equals(fechaLimite) && plazoCercaFin != 0) {
                    tEVO.setPlazoCercaFin(plazoCercaFin);

                } else {
                    tEVO.setPlazoCercaFin(plazoCercaFin);
                }
                tEVO.setBloquearPlazos(rs.getBoolean("TRA_GENERARPLZ"));
                tEVO.setAdmiteNotificacionElectronica(rs.getString("TRA_NOTIFICACION_ELECTRONICA"));
                
                //Inicio cogemos los nuevos valores
                Integer notifObligatoria = rs.getInt("TRA_NOTIF_ELECT_OBLIG");
                tEVO.setNotificacionObligatoria((notifObligatoria!=null)?notifObligatoria.toString():"0");
                
                Integer certificadoOrganismo = rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG");
                tEVO.setCertificadoOrganismo((certificadoOrganismo!=null)?certificadoOrganismo.toString():"0");
                
                String interesadoObligatorio = rs.getString("PRO_INTOBL");
                if(interesadoObligatorio!=null && "1".equals(interesadoObligatorio))
                    tEVO.setInteresadoObligatorio(true);
                else
                    tEVO.setInteresadoObligatorio(false);
                    
            }
            rs.close();
            st.close();

            if(tEVO.getCodUnidadInicioTram() == null || "".equals(tEVO.getCodUnidadInicioTram())) {
                tEVO.setCodUnidadInicioTram("");
                tEVO.setUnidadInicio("");
            } else {
                UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],tEVO.getCodUnidadInicioTram());

                if (uorDTO!=null)
                    tEVO.setUnidadInicio(uorDTO.getUor_nom());
            }
            if(tEVO.getCodUnidadTramitadoraTram() == null || "".equals(tEVO.getCodUnidadTramitadoraTram())) {
                tEVO.setCodUnidadTramitadoraTram("");
                tEVO.setUnidadTramitadora("");
            } else {
                UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],tEVO.getCodUnidadTramitadoraTram());

                if (uorDTO!=null)
                    tEVO.setUnidadTramitadora(uorDTO.getUor_nom());
            }

            // PESTAÑA DE DOCUMENTOS

            from = crd_nud + "," +  oad.convertir("CRD_FINF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FINF,"  + crd_des + "," + oad.convertir(crd_fal, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS " + crd_fal + "," + oad.convertir(crd_fmo, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS "
                    + crd_fmo + ",usu." + usu_nom + " AS usuarioCreacion,usu1." +
                    usu_nom + " AS usuarioModificacion," + plt_int + ",PLT_EDITOR_TEXTO,crd_id_metadato ";

            where = crd_mun + "=" + tEVO.getCodMunicipio() + " AND " + crd_pro + "='" + tEVO.getCodProcedimiento() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_num + "='" +
                    tEVO.getNumeroExpediente() + "' AND " + crd_ocu + " = " + tEVO.getOcurrenciaTramite() + " ";

            // Comprobamos si esta activado el servicio Web de firmadoc.
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                from += ", " + crd_fir_fd;
                where += "AND " + crd_exp_fd + " IS NOT NULL " +
                        "AND " + crd_doc_fd + " IS NOT NULL ";
            } else {
                from += ", " + crd_fir_est;
                where += "AND " + crd_exp_fd + " IS NULL " +
                        "AND " + crd_doc_fd + " IS NULL " +
                        "AND " + crd_fir_fd + " IS NULL";
            }
            String[] join1 = new String[14];
            join1[0] = "HIST_E_CRD";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu";
            join1[3] = "HIST_E_CRD." + crd_usc + "=usu." + usu_cod;
            join1[4] = "LEFT";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu1";
            join1[6] = "HIST_E_CRD." + crd_usm + "=usu1." + usu_cod;
            join1[7] = "LEFT";
            join1[8] = "e_dot";
            join1[9] = "HIST_E_CRD." + crd_pro + "=e_dot." + dot_pro + " AND " +
                    "HIST_E_CRD." + crd_tra + "=e_dot." + dot_tra + " AND " +
                    "HIST_E_CRD." + crd_dot + "=e_dot." + dot_cod;
            join1[10] = "LEFT";
            join1[11] = "a_plt";
            join1[12] = "e_dot." + dot_plt + "=a_plt." + plt_cod;
            join1[13] = "false";
            sql = oad.join(from,where,join1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(crd_nud);
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString(crd_des);
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString(crd_fal);
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString(crd_fmo);
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");

                String fechaInforme = rs.getString("CRD_FINF");
                if(fechaInforme!=null)
                    tramExpVO.setFechaInforme(fechaInforme);
                else
                    tramExpVO.setFechaInforme("");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString(plt_int);
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                 int codigoMetadato=(rs.getInt("crd_id_metadato"));
                if (codigoMetadato>0){
                     if(m_Log.isDebugEnabled()) m_Log.debug("El documento tiene metadato, el editor pasa a ser NO EDITABLE");
                    tramExpVO.setEditorTexto("");
                }
                String estadoFirma;
                if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                    estadoFirma = rs.getString(crd_fir_fd);
                } else {
                    estadoFirma = rs.getString(crd_fir_est);
                }
                tramExpVO.setEstadoFirma(estadoFirma);
                tramExpVO.setRelacion("");
                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }

           
            //DOCUMENTOS DE LAS RELACIONES A LOS QUE PERTENECEN
            from = "DISTINCT CRD_NUD, CRD_DES, " +
                    oad.convertir("CRD_FAL", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FAL, " +
                    oad.convertir("CRD_FMO", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS CRD_FMO, " +
                    "USU.USU_NOM AS usuarioCreacion, USU1.USU_NOM AS usuarioModificacion, PLT_INT,PLT_EDITOR_TEXTO,CRD_NUM";

            where = "CRD_MUN = " + tEVO.getCodMunicipio() + " AND CRD_PRO = '" + tEVO.getCodProcedimiento() + "' " +
                    "AND CRD_TRA = " + tEVO.getCodTramite() + " AND CRD_OCU = " + tEVO.getOcurrenciaTramite() + " ";

            // Comprobamos si esta activado el servicio Web de firmadoc.
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                from += ", CRD_FIR_FD";
                where += "AND CRD_EXP_FD IS NOT NULL AND CRD_DOC_FD IS NOT NULL ";
            } else {
                from += ", CRD_FIR_EST";
                where += "AND CRD_EXP_FD IS NULL AND CRD_DOC_FD IS NULL AND CRD_FIR_FD IS NULL";
            }
            String[] join2 = new String[17];
            join2[0] = "G_CRD";
            join2[1] = "INNER";
            join2[2] = GlobalNames.ESQUEMA_GENERICO + "A_USU USU";
            join2[3] = "G_CRD.CRD_USC = USU.USU_COD";
            join2[4] = "LEFT";
            join2[5] = GlobalNames.ESQUEMA_GENERICO + "A_USU USU1";
            join2[6] = "G_CRD.CRD_USM = USU1.USU_COD";
            join2[7] = "LEFT";
            join2[8] = "E_DOT";
            join2[9] = "G_CRD.CRD_PRO = E_DOT.DOT_PRO AND G_CRD.CRD_TRA = E_DOT.DOT_TRA " +
                    "AND G_CRD.CRD_DOT = E_DOT.DOT_COD";
            join2[10] = "LEFT";
            join2[11] = "A_PLT";
            join2[12] = "E_DOT.DOT_PLT = A_PLT.PLT_COD";
            join2[13] = "INNER";
            join2[14] = "G_EXP";
            join2[15] = "G_CRD.CRD_MUN = G_EXP.REL_MUN AND G_CRD.CRD_PRO = G_EXP.REL_PRO " +
                    "AND G_CRD.CRD_NUM = G_EXP.REL_NUM AND G_EXP.EXP_NUM = '" + tEVO.getNumeroExpediente() +"'";
            join2[16] = "false";
            sql = oad.join(from,where,join2);

            m_Log.debug("CONSULTA PARA RECUPERAR LOS DOCUMENTOS DE LAS RELACIONES A LAS QUE PERTENCE");
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while ( rs.next() ) {
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString("CRD_NUD");
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString("CRD_DES");
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString("CRD_FAL");
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString("CRD_FMO");
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString("PLT_INT");
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                String estadoFirma;
                if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                    estadoFirma = rs.getString("CRD_FIR_FD");
                } else {
                    estadoFirma = rs.getString("CRD_FIR_EST");
                }
                tramExpVO.setEstadoFirma(estadoFirma);
                tramExpVO.setRelacion(rs.getString("CRD_NUM"));

                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }
            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            rs.close();

            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            rs.close();
           


            sql = "SELECT " + dot_cod + " FROM E_DOT WHERE " +  dot_mun + "=" + tEVO.getCodMunicipio() +
                    " AND " + dot_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + dot_tra + "=" +
                    tEVO.getCodTramite()+" AND DOT_ACTIVO = 'SI'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 ="si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(dot_cod);
                tramExpVO.setCodDocumento(codDocumento);
                listaCodDocumentosTramite.addElement(tramExpVO);
                tEVO.setListaCodDocumentosTramite(listaCodDocumentosTramite);
            }
            if(entrar1.equals("no")) {
                tEVO.setListaCodDocumentosTramite(listaCodDocumentosTramite);
            }
            rs.close();

            // PESTAÑA DE ENLACES

            sql = "SELECT " + ten_des + "," + ten_url + "," + ten_est + " FROM E_TEN WHERE " + ten_mun + "=" +
                    tEVO.getCodMunicipio() + " AND " + ten_pro + "='" + tEVO.getCodProcedimiento() +
                    "' AND " + ten_tra + "=" + tEVO.getCodTramite();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            String entrar5 = "no";
            while(rs.next()) {
                entrar5 ="si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String descEnlace = rs.getString(ten_des);
                tramExpVO.setDescEnlace(descEnlace);
                String url = rs.getString(ten_url);
                tramExpVO.setUrl(url);
                String estado = rs.getString(ten_est);
                tramExpVO.setEstadoEnlace(estado);
                listaEnlaces.addElement(tramExpVO);
                tEVO.setListaEnlaces(listaEnlaces);
            }
            if(entrar5.equals("no")) {
                tEVO.setListaEnlaces(listaEnlaces);
            }
            rs.close();

            // CONDICIONES DE SALIDA DEL TRAMITE
            sql = "SELECT " + sal_tac + "," + sal_taa + "," + sal_tan + "," + sal_obl + "," + sal_obld +
                    " FROM E_SAL,E_TRA WHERE " + sal_mun + "=" + tEVO.getCodMunicipio() + " AND " + sal_pro + "='" +
                    tEVO.getCodProcedimiento() + "' AND " + sal_tra + "=" + tEVO.getCodTramite() +
                    " AND " + sal_mun + "=" + tra_mun + " AND " + sal_pro + "=" + tra_pro + " AND " +
                    sal_tra + "=" + tra_cod + " AND " + tra_fba + " IS NULL";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = st.executeQuery(sql);
            String entrar4 = "no";
            while ( rs.next() ) {
                entrar4 = "si";
                String accion = rs.getString(sal_tac);
                tEVO.setAccion(accion);
                String accionAfirmativa = rs.getString(sal_taa);
                tEVO.setAccionAfirmativa(accionAfirmativa);
                String accionNegativa = rs.getString(sal_tan);
                tEVO.setAccionNegativa(accionNegativa);
                String obligatorio = rs.getString(sal_obl);
                tEVO.setObligatorio(obligatorio);
                String obligatorioDesf = rs.getString(sal_obld);
                tEVO.setObligatorioDesf(obligatorioDesf);
            }
            rs.close();
            if(entrar4.equals("si")) {
                if("P".equals(tEVO.getAccion())) {
                    sql = "SELECT " + sml_valor + " FROM E_SML WHERE " + sml_mun + "=" + tEVO.getCodMunicipio() +
                            " AND " + sml_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + sml_tra + "=" +
                            tEVO.getCodTramite() + " AND " + sml_cmp + "='TXT' AND " + sml_leng + "='"+idiomaDefecto+"'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                    rs = st.executeQuery(sql);
                    while ( rs.next() ) {
                        String pregunta = rs.getString(sml_valor);
                        tEVO.setPregunta(pregunta);
                    }
                }
            }
            rs.close();

            // LISTAS DEL FLUJO DE SALIDA
            String codOrg = tEVO.getCodMunicipio();
            String codProc = tEVO.getCodProcedimiento();
            String codTram = tEVO.getCodTramite();

            listaTramitesFavorables =
                getFlujoSalida(con, codOrg, codProc, codTram, FLUJO_LISTA_TRAMITES);

            if (listaTramitesFavorables.size() > 0) {
                tEVO.setListaTramitesFavorables(listaTramitesFavorables);
                tEVO.setListaTramitesNoFavorables(new Vector());
            } else {
                listaTramitesFavorables =
                    getFlujoSalida(con, codOrg, codProc, codTram, FLUJO_LISTA_TRAMITES_FAVORABLE);
                listaTramitesNoFavorables =
                    getFlujoSalida(con, codOrg, codProc, codTram, FLUJO_LISTA_TRAMITES_NO_FAVORABLE);
                tEVO.setListaTramitesFavorables(listaTramitesFavorables);
                tEVO.setListaTramitesNoFavorables(listaTramitesNoFavorables);
            }

            // PERMISO USUARIO
            String codUsuario = tEVO.getCodUsuario();
            String codOrganizacion = tEVO.getCodOrganizacion();
            String codEntidad = tEVO.getCodEntidad();
            String permiso = "no";
            sql = "SELECT DISTINCT " + exp_uor + "," + cro_utr + " FROM HIST_E_EXP, HIST_E_CRO WHERE " +
                    exp_mun + "=" + cro_mun + " AND " + exp_pro + "=" + cro_pro + " AND " + exp_eje + "=" +
                    cro_eje + " AND " + exp_mun + "=" + cro_mun + " AND " + exp_mun + "=" + tEVO.getCodMunicipio() +
                    " AND " + exp_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + exp_eje + "=" + tEVO.getEjercicio() +
                    " AND " + exp_num + "='" + tEVO.getNumeroExpediente() + "' AND " + cro_tra + "=" + tEVO.getCodTramite()+
                    " AND " + cro_ocu + "=" + tEVO.getOcurrenciaTramite() + " AND (EXISTS ( SELECT DISTINCT " +
                    uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " + uou_usu + "=" +
                    codUsuario + " AND " + uou_org +
                    "=" + codOrganizacion + " AND " + uou_ent + "=" + codEntidad + " AND " + uou_uor +
                    "=" + exp_uor + ") OR EXISTS (SELECT DISTINCT " + uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO +
                    "A_UOU A_UOU WHERE " + uou_usu +
                    "=" + codUsuario + " AND " + uou_org + "=" + codOrganizacion + " AND " + uou_ent + "=" +
                    codEntidad + " AND " + uou_uor + "=" + cro_utr + "))" ;

            if(m_Log.isDebugEnabled()) m_Log.debug( sql);

            rs = st.executeQuery(sql);
            if (rs.next()){
                String unidadControladora = (String) rs.getString(exp_uor);
                String unidadTramitadora = (String) rs.getString(cro_utr);
                if ( (unidadControladora != null) && (unidadTramitadora != null) ) permiso = "si";
            }
            //if(m_Log.isDebugEnabled()) m_Log.debug("el permiso del usuario es : " + permiso);
            tEVO.setPermiso(permiso);
            rs.close();

            //inicio COMPROBAR BLOQUEO DE USUARIO en el primer expediente de la relacion (todos serán iguales)
            sql = "SELECT " + exp_rel_num + " FROM G_EXP WHERE " +
                    exp_exp_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    exp_exp_pro + "='" + tEVO.getCodProcedimiento() + "' AND " +
                    exp_exp_eje + "=" + tEVO.getEjercicio() + " AND " +
                    exp_exp_num + "='" + tEVO.getNumeroRelacion() + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(" PRIMER EXPEDIENTE : "+ sql);
            rs = st.executeQuery(sql);
            if (rs.next()){
                tEVO.setNumeroExpediente(rs.getString(exp_rel_num));  // Expediente de la relacion
            }
            rs.close();

               
            sql = "SELECT " + bloq_usu + " FROM E_EXP_BLOQ WHERE " +
                    bloq_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    bloq_pro + "='" + tEVO.getCodProcedimiento() + "' AND " +
                    bloq_eje + "=" + tEVO.getEjercicio() + " AND " +
                    bloq_num + "='" + tEVO.getNumeroExpediente() + "' AND " +
                    bloq_tra + "=" + tEVO.getCodTramite()+ " AND " +
                    bloq_ocu + "=" + tEVO.getOcurrenciaTramite();

            if(m_Log.isDebugEnabled()) m_Log.debug(" BLOQUEO RELACIONES : "+ sql);

            rs = st.executeQuery(sql);
            String bloqueo = ""; //Por defecto tramite no bloqueado
            if (rs.next()){
                bloqueo = rs.getString(bloq_usu);  // Usuario que bloquea el expediente en ese trémite
            }
            //if(m_Log.isDebugEnabled()) m_Log.debug("el permiso del usuario es : " + permiso);
            if(m_Log.isDebugEnabled()) m_Log.debug(" BLOQUEO RELACIONES : "+ bloqueo);
            tEVO.setBloqueo(bloqueo);
            rs.close();
            //fin COMPROBAR BLOQUEO DE USUARIO
           
            
           // tEVO.setBloqueo("");
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.cargarDatos", e);
        } finally {
            try {
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return tEVO;
    }
    
    
    
    
    
    
    
    /*******************************/
   
    public GeneralValueObject cargaTiposFicheros(TramitacionExpedientesValueObject tEVO,Vector eCs, boolean expedienteHistorico,String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        GeneralValueObject lista = new GeneralValueObject();
        CamposFormulario cF = null;

        try{
       
            String codMunicipio = tEVO.getCodMunicipio();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocuTramite = tEVO.getOcurrenciaTramite();

        for(int i=0;i<eCs.size();i++) {
          EstructuraCampo eC = new EstructuraCampo();
          eC = (EstructuraCampo) eCs.elementAt(i);
          String codTipoDato = eC.getCodTipoDato();  
          String codCampo = eC.getCodCampo();
          String mascara = eC.getMascara();
          String tipo; 
                    
         
            if("5".equals(codTipoDato)) {
              if(codTramite == null || "".equals(codTramite)) {
                  tipo=DatosSuplementariosDAO.getInstance().getTipoFichero(codCampo,codMunicipio,ejercicio,numero,expedienteHistorico,params);
              } else {
                  tipo=DatosSuplementariosDAO.getInstance().getTipoFicheroTramite(codCampo,codMunicipio,ejercicio,numero,params,codTramite,expedienteHistorico);
              }
              codCampo = eC.getCodCampo()+"_"+ocuTramite;
              lista.setAtributo(codCampo,tipo);
            }         
        }

      }catch (Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }finally {         
          return lista;
      }
    }
    
    /**
     * Recupera los tramites que en base de datos estan marcados con TRA_NOTIFICADO=1 y  sin registros en la tabla NOTIFICACION  
     * @param organizacion 
     * @param numExp
     * @param codProcedimiento 
     * @param ejercicio 
     * @param con Objeto con la conexion a la base de datos
     * @return
     */
    public ArrayList<TramitacionExpedientesValueObject> getTramitesNotificadosNoE(int organizacion, int ejercicio, String codProcedimiento, String numExp, boolean enHistorico, Connection con) throws TechnicalException{

        ArrayList<TramitacionExpedientesValueObject> tramites = new ArrayList<TramitacionExpedientesValueObject>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        String prefijo = "";

        try{
            if(enHistorico){
                prefijo = "HIST_";
            }
            if (!esExpedienteHistorico(numExp, con)) {
                sql = "SELECT CRO_FEF, CRO_FEI, TML_VALOR, CRO_OCU, TRA_COD FROM " + prefijo + "E_CRO "
                        + "JOIN E_TRA ON (TRA_MUN = CRO_MUN AND TRA_PRO = CRO_PRO AND TRA_COD = CRO_TRA) "
                        + "JOIN E_TML ON (TRA_MUN = TML_MUN AND TRA_PRO = TML_PRO AND TRA_COD = TML_TRA) "
                        + "LEFT JOIN " + prefijo + "NOTIFICACION ON (NUM_EXPEDIENTE = CRO_NUM AND COD_PROCEDIMIENTO = CRO_PRO AND EJERCICIO = CRO_EJE "
                        + "AND COD_MUNICIPIO = CRO_MUN AND COD_TRAMITE = CRO_TRA AND OCU_TRAMITE = CRO_OCU) "
                        + "WHERE CRO_MUN=? AND CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? AND TRA_NOTIFICADO=1 AND CODIGO_NOTIFICACION IS NULL";
            }
            else {
                sql = "SELECT CRO_FEF, CRO_FEI, TML_VALOR, CRO_OCU, TRA_COD FROM " + prefijo + "HIST_E_CRO "
                        + "JOIN E_TRA ON (TRA_MUN = CRO_MUN AND TRA_PRO = CRO_PRO AND TRA_COD = CRO_TRA) "
                        + "JOIN E_TML ON (TRA_MUN = TML_MUN AND TRA_PRO = TML_PRO AND TRA_COD = TML_TRA) "
                        + "LEFT JOIN " + prefijo + "NOTIFICACION ON (NUM_EXPEDIENTE = CRO_NUM AND COD_PROCEDIMIENTO = CRO_PRO AND EJERCICIO = CRO_EJE "
                        + "AND COD_MUNICIPIO = CRO_MUN AND COD_TRAMITE = CRO_TRA AND OCU_TRAMITE = CRO_OCU) "
                        + "WHERE CRO_MUN=? AND CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? AND TRA_NOTIFICADO=1 AND CODIGO_NOTIFICACION IS NULL";
            }
            
            m_Log.debug("La query: " + sql);
            m_Log.debug("Los valores pasados a la query: " +organizacion + "-" +codProcedimiento + "-" + ejercicio + "-" + numExp);

            ps = con.prepareStatement(sql);
            int contbd = 1;
            ps.setInt(contbd++,organizacion);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, numExp);
            
            rs = ps.executeQuery();
            while(rs.next()){
                TramitacionExpedientesValueObject tram = new TramitacionExpedientesValueObject();
                tram.setCodTramite(rs.getString("TRA_COD"));
                tram.setOcurrenciaTramite(rs.getString("CRO_OCU"));
                tram.setTramite(rs.getString("TML_VALOR"));
                tram.setFechaInicio(DateOperations.timeStampToString(rs.getTimestamp("CRO_FEI")));
                tram.setFechaFin(DateOperations.toString(rs.getDate("CRO_FEF"), "dd/MM/yyyy"));
                tram.setCodMunicipio(String.valueOf(organizacion));
                tram.setCodProcedimiento(codProcedimiento);
                tram.setNumeroExpediente(numExp);
                tram.setEjercicio(String.valueOf(ejercicio));
                tram.setDesdeJsp("si");
                tram.setExpHistorico(enHistorico);
                tramites.add(tram);

            }// while

        }catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException("Error: " + e.getMessage());
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return tramites;
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
     * Obteniene el id de los metadatos del documento de tramite
     * 
     * @param codDocumento
     * @param ejercicio
     * @param municipio
     * @param numeroExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param procedimiento
     * @param con
     * @return 
     * @throws es.altia.common.exception.TechnicalException 
     */
    public Long getDocumentoIdMetadato(Integer codDocumento, Integer ejercicio, 
            Integer municipio, String numeroExpediente, Integer codTramite,
            Integer ocurrenciaTramite, String procedimiento, Connection con)
            throws TechnicalException {
        
        Long idMetadato = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT CRD_ID_METADATO ")
               .append("FROM E_CRD ")
               .append("WHERE CRD_NUD = ? AND CRD_EJE = ? AND CRD_MUN = ? ")
               .append("AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? ")
               .append("AND CRD_PRO = ? ");
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: CRD_NUD = %d AND CRD_EJE = %d AND CRD_MUN = %d AND CRD_NUM = %s AND CRD_TRA = %d AND CRD_OCU = %d AND CRD_PRO = %s ",
                        codDocumento, ejercicio, municipio, numeroExpediente, codTramite, ocurrenciaTramite, procedimiento));
            }

            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    codDocumento,
                    ejercicio,
                    municipio,
                    numeroExpediente,
                    codTramite,
                    ocurrenciaTramite,
                    procedimiento);

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                idMetadato = JdbcOperations.getLongFromResultSet(resultSet, "CRD_ID_METADATO");
            }   
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al intentar obtener el campo CRD_ID_METADATO del documento de tramite", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(resultSet);
            SigpGeneralOperations.closeStatement(ps);
        }

        return idMetadato;
    }
    
    /**
     * Obteniene la fecha de modificacion del documento de tramite
     * 
     * @param codDocumento
     * @param ejercicio
     * @param municipio
     * @param numeroExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param procedimiento
     * @param con
     * @return 
     * @throws es.altia.common.exception.TechnicalException 
     */
    public Calendar getDocumentoFechaModificacion(Integer codDocumento, Integer ejercicio, 
            Integer municipio, String numeroExpediente, Integer codTramite,
            Integer ocurrenciaTramite, String procedimiento, Connection con)
            throws TechnicalException {
        
        Calendar fechaModificacion = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT CRD_FMO ")
               .append("FROM E_CRD ")
               .append("WHERE CRD_NUD = ? AND CRD_EJE = ? AND CRD_MUN = ? ")
               .append("AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? ")
               .append("AND CRD_PRO = ? ");
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: CRD_NUD = %d AND CRD_EJE = %d AND CRD_MUN = %d AND CRD_NUM = %s AND CRD_TRA = %d AND CRD_OCU = %d AND CRD_PRO = %s ",
                        codDocumento, ejercicio, municipio, numeroExpediente, codTramite, ocurrenciaTramite, procedimiento));
            }

            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    codDocumento,
                    ejercicio,
                    municipio,
                    numeroExpediente,
                    codTramite,
                    ocurrenciaTramite,
                    procedimiento);

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                fechaModificacion = DateOperations.toCalendar(resultSet.getTimestamp("CRD_FMO"));
            }   
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al intentar obtener el campo CRD_FMO del documento de tramite", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(resultSet);
            SigpGeneralOperations.closeStatement(ps);
        }

        return fechaModificacion;
    }
    
    /**
     * Recupera las posibles UORs de inicio manual que tendrá el trámite según su definición
     * @param tramite
     * @param opcionUnidadInicio
     * @param usuario
     * @param con
     * @return 
     * @throws java.sql.SQLException 
     */
    public List<Integer> recuperarListaUnidadesInicioManual(TramitacionExpedientesValueObject tramite, String opcionUnidadInicio, ArrayList<String> listaUnidadesUsuario, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        List<Integer> listaUnidadesTramite= null;
        int codOrg;
        int ejer;
        
        try {
            codOrg = Integer.parseInt(tramite.getCodOrganizacion());
            ejer = Integer.parseInt(tramite.getEjercicio());
            listaUnidadesTramite = new ArrayList<Integer>();
            if ("-99998".equals(opcionUnidadInicio)){
                for(String codigo : listaUnidadesUsuario){
                    listaUnidadesTramite.add(Integer.parseInt(codigo));
                }
            } else {
                int codUor;
                if("-99999".equals(opcionUnidadInicio)){
                    codUor = ExpedientesDAO.getInstance().getUorExpediente(codOrg, ejer, tramite.getNumeroExpediente(), con);
                } else {
                    codUor = Integer.parseInt(opcionUnidadInicio);
                }
                
                if(codUor != -1){
                    listaUnidadesTramite.add(codUor);                    
                }
            }
        } catch (Exception ex) {
            m_Log.error(String.format("Ha ocurrido un error al recuperar la lista de unidades de inicio del trámite manual que va a iniciarse (del expediente %s, con código %s", 
                    tramite.getNumeroExpediente(), tramite.getCodTramite()), ex);
            throw new SQLException(ex.getMessage());
        } finally {
            try{
                if (rs != null){
                    rs.close();
                }
                if (ps != null){
                    ps.close();
                }
            } catch (SQLException e){
                
            }
        }
        
        return listaUnidadesTramite;
    }

    /** 
     * Borra las tablas de flujo y circuito personalizadas de los documentos de trámite
     * 
     * @param tEVO
     * @param con 
     */
    private void eliminarDocumentoCRDFlujoFirmaPersonalizada(
            TramitacionExpedientesValueObject tEVO, Connection con)
            throws TechnicalException {

        PreparedStatement ps = null;
        
        try {
            FirmaFlujoDAO firmaFlujoDao = FirmaFlujoDAO.getInstance();

            FirmaDocumentoTramiteClave clave = new FirmaDocumentoTramiteClave();
            clave.setCodMunicipio(NumberUtils.createInteger(tEVO.getCodMunicipio()));
            clave.setCodProcedimiento(tEVO.getCodProcedimiento());
            clave.setEjercicio(NumberUtils.createInteger(tEVO.getEjercicio()));
            clave.setNumExpediente(tEVO.getNumeroExpediente());
            clave.setCodTramite(NumberUtils.createInteger(tEVO.getCodTramite()));
            clave.setCodOcurrencia(NumberUtils.createInteger(tEVO.getOcurrenciaTramite()));
            clave.setCodDocumento(NumberUtils.createInteger(tEVO.getCodDocumento()));
            
            // Tabla E_CRD_FIR_FLUJO
            firmaFlujoDao.eliminarFlujoTramitePersonalizado(clave, con);
            
            // Tabla E_CRD_FIR_FIRMANTES
            firmaFlujoDao.eliminarFirmantesTramitePersonalizado(clave, con);
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al intentar eliminar el registro asociado al documento de tramite de las tablas E_CRD_FIR_FLUJO y E_CRD_FIR_FIRMANTES", ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }
    
    /**
     * Obtiene el nombre del tramite de un procedimiento
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param codTramite
     * @param conexion
     * @return
     * @throws TechnicalException 
     */
    public String getNombreTramite(
            Integer codMunicipio, String codProcedimiento, Integer codTramite,
            Connection conexion)
            throws TechnicalException {
        
        m_Log.debug("getNombreTramite");

        String nombre = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT TML_VALOR ")
               .append("FROM E_TML ")
               .append("WHERE TML_MUN = ? ")
               .append(" AND TML_PRO = ? ")
               .append(" AND TML_TRA = ? ")
               .append(" AND TML_CMP = 'NOM' ")
               .append(" AND TML_LENG = ? ");
            
            String idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL Obtener id de flujo de firma: %s", sql.toString()));
                m_Log.debug("PARAMS:");
                m_Log.debug(String.format("TML_MUN = %s", codMunicipio));
                m_Log.debug(String.format("TML_PRO = %s", codProcedimiento));
                m_Log.debug(String.format("TML_TRA = %s", codTramite));
                m_Log.debug(String.format("TML_LENG = %s", idiomaDefecto));
            }

            ps = conexion.prepareStatement(sql.toString());

            int indexStart = 1;
            indexStart = JdbcOperations.setValues(ps, indexStart, 
                    codMunicipio,
                    codProcedimiento,
                    codTramite,
                    idiomaDefecto);

            rs = ps.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("TML_VALOR");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return nombre;
    }

    /***
     * Metodo que libera una Entrada de registro al finalizar un expediente de forma no covencional
     * @param numeroExpediente
     * @param ejercicioRegistro
     * @param numeroRegistro
     * @param con
     * @return Mapa con dos parametros resultado de la ejecucionde un procedure en BD : resultado, mensaje. resultado=OK - Proceso correcto KO en cualquier otro caso, detalles en el mensaje.
     * @throws Exception
     */
    public Map<String,String> liberarEntradaRegistroAlFinalizarExpteNoConvencional(String numeroExpediente, int ejercicioRegistro, int numeroRegistro, Connection con) throws Exception {
        m_Log.info(" liberarEntradaRegistroAlFinalizarExpteNoConvencional - Begin " + numeroExpediente + " :  " + ejercicioRegistro + "/" + numeroRegistro + " "
                + formatFechaLog.format(new Date()));
        CallableStatement st = null;
        ResultSet rs = null;
        Map<String,String> resultado = new Hashtable<String, String>();
        try {
            String query = "call desasociarexpregistro(?,?,?,?,?)";
            m_Log.info("sql = " + query);
            st = con.prepareCall(query);
            st.setInt("ejercicio", ejercicioRegistro);
            st.setInt("numregistro", numeroRegistro);
            st.setString("numexpediente", numeroExpediente);
            st.registerOutParameter("resultado", java.sql.Types.VARCHAR);
            st.registerOutParameter("mensaje", java.sql.Types.VARCHAR);
            st.executeUpdate();
            resultado.put("resultado",st.getString("resultado"));
            resultado.put("mensaje", st.getString("mensaje"));
        }catch (Exception ex){
            m_Log.error("Error al Liberar las etradas de registro asociadas al Expediente anulado de manera no cnovencional .. " + ex.getMessage());
            throw ex;
        }
        m_Log.info(" liberarEntradaRegistroAlFinalizarExpteNoConvencional - End " + resultado.toString()  + " "
                + formatFechaLog.format(new Date()));
        return resultado;
    }
}