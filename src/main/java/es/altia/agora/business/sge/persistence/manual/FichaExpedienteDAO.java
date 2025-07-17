package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.sge.FirmasDocumentoExpedienteVO;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.FicheroVO;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.agora.business.integracionsw.PeticionSWVO;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException; 
import es.altia.agora.business.integracionsw.exception.NoServicioWebDefinidoException;
import es.altia.agora.business.integracionsw.exception.FaltaDatoObligatorioException;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.integracionsw.procesos.GestorEjecucionTramitacion;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.sge.*;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos; 
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.business.expediente.vo.EnlaceVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionModuloException;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.ModuloIntegracionExternoDAO;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaBase;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaCargador;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.ConexionExterna; 
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;


public class FichaExpedienteDAO {

    //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
    //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
    //Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(FichaExpedienteDAO.class.getName());
    
    private static ResourceBundle properties = ResourceBundle.getBundle("Terceros");     

    protected static String des_val_campo;
    protected static String des_val_cod;
    protected static String des_val_desc;
    protected static String des_val_estado;

    protected static String pml_mun;
    protected static String pml_cod;
    protected static String pml_cmp;
    protected static String pml_leng;
    protected static String pml_valor;

    protected static String hte_ter;
    protected static String hte_nvr;
    protected static String hte_pa1;
    protected static String hte_ap1;
    protected static String hte_pa2;
    protected static String hte_ap2;
    protected static String hte_nom;

    protected static String exp_mun;
    protected static String exp_eje;
    protected static String exp_num;
    protected static String exp_pro;
    protected static String exp_fei;
    protected static String exp_fef;
    protected static String exp_est;
    protected static String exp_usu;
    protected static String exp_uor;
    protected static String exp_loc;
    protected static String exp_clo;
    protected static String exp_obs;
    protected static String exp_asu;
    protected static String exp_tra;
    protected static String exp_tocu;
    protected static String tra_ws_cod;
    protected static String tra_ws_ob;
    protected static String tra_ws_cod_retro;
    protected static String tra_ws_ob_retro;


    protected static String bloq_mun;
    protected static String bloq_eje;
    protected static String bloq_num;
    protected static String bloq_pro;
    protected static String bloq_tra;
    protected static String bloq_ocu;
    protected static String bloq_usu;
    
    protected static String ext_mun;
    protected static String ext_eje;
    protected static String ext_num;
    protected static String ext_pro;
    protected static String ext_ter;
    protected static String ext_nvr;
    protected static String ext_rol;
    protected static String ext_dot;

    protected static String pro_mun;
    protected static String pro_cod;
    protected static String pro_tri;
    protected static String pro_utr;
    protected static String pro_loc;

    protected static String tft_tra_mun;
    protected static String tft_tra_pro;
    protected static String tft_tra_eje;
    protected static String tft_tra_num;
    protected static String tft_tra_tra;
    protected static String tft_tra_ocu;
   
    protected static String ff_trafirma_mun;
    protected static String ff_trafirma_pro;
    protected static String ff_trafirma_eje;
    protected static String ff_trafirma_num;
    protected static String ff_trafirma_tra;
    protected static String ff_trafirma_ocu;
   
    protected static String txtt_mun;
    protected static String txtt_pro;
    protected static String txtt_eje;
    protected static String txtt_num;
    protected static String txtt_tra;
    protected static String txtt_ocu;
    
    protected static String ttlt_mun;
    protected static String ttlt_pro;
    protected static String ttlt_eje;
    protected static String ttlt_num;
    protected static String ttlt_tra;
    protected static String ttlt_ocu;
    
    protected static String tnut_mun;
    protected static String tnut_pro;
    protected static String tnut_eje;
    protected static String tnut_num;
    protected static String tnut_tra;
    protected static String tnut_ocu;
    
    protected static String tnuct_mun;
    protected static String tnuct_pro;
    protected static String tnuct_eje;
    protected static String tnuct_num;
    protected static String tnuct_tra;
    protected static String tnuct_ocu;
   
    protected static String tfit_mun;
    protected static String tfit_pro;
    protected static String tfit_eje;
    protected static String tfit_num;
    protected static String tfit_tra;
    protected static String tfit_ocu;
    protected static String tfit_cod;
    protected static String tfit_nomfich;
    protected static String tfit_mime;
    protected static String tfit_valorfich;
    
    protected static String tfet_mun;
    protected static String tfet_pro;
    protected static String tfet_eje;
    protected static String tfet_num;
    protected static String tfet_tra;
    protected static String tfet_ocu;
    
    protected static String tfect_mun;
    protected static String tfect_pro;
    protected static String tfect_eje;
    protected static String tfect_num;
    protected static String tfect_tra;
    protected static String tfect_ocu;
 
    protected static String tdet_mun;
    protected static String tdet_pro;
    protected static String tdet_eje;
    protected static String tdet_num;
    protected static String tdet_tra;
    protected static String tdet_ocu;
 
    //Fin variables anhadidas 
    
    protected static String cro_mun;
    protected static String cro_pro;
    protected static String cro_tra;
    protected static String cro_eje;
    protected static String cro_num;
    protected static String cro_usu;
    protected static String cro_fei;
    protected static String cro_fef;
    protected static String cro_utr;
    protected static String cro_ocu;
    protected static String cro_fli;
    protected static String cro_usf;
    protected static String cro_ffp;
     protected static String cro_fip;

    protected static String crd_mun;
    protected static String crd_pro;
    protected static String crd_eje;
    protected static String crd_num;
    protected static String crd_tra;
    protected static String crd_ocu;

    protected static String trp_mun;
    protected static String trp_pro;
    protected static String trp_eje;
    protected static String trp_num;
    protected static String trp_tra;
    protected static String trp_ent;
    protected static String trp_ctr;
    protected static String trp_fei;
    protected static String trp_fef;

    // Informacion de asientos de entrada relacionados con expedientes.
    protected static String sql_res_codDpto;
    protected static String sql_res_codUnid;
    protected static String sql_res_tipoReg;
    protected static String sql_res_ejer;
    protected static String sql_res_num;
    protected static String sql_res_fecAnot;
    protected static String sql_res_asunt;
    protected static String sql_res_estado;
    protected static String sql_res_codTerc;
    protected static String sql_res_domTerc;
    protected static String sql_res_modTerc;
    protected static String sql_res_tipoDestino;

    protected static String utml_cod;
    protected static String utml_cmp;
    protected static String utml_leng;
    protected static String utml_valor;

    protected static String sql_exr_mun;
    protected static String sql_exr_pro;
    protected static String sql_exr_eje;
    protected static String sql_exr_num;
    protected static String sql_exr_dep;
    protected static String sql_exr_uor;
    protected static String sql_exr_ejr;
    protected static String sql_exr_nre;
    protected static String sql_exr_tip;
    protected static String sql_exr_ori;
    protected static String sql_exr_top;


    protected static String cml_cod;
    protected static String cml_cmp;
    protected static String cml_leng;
    protected static String cml_valor;
    protected static String sql_dnn_dom; 
    protected static String sql_dpo_dom; 

    protected static String tml_mun;
    protected static String tml_pro;
    protected static String tml_tra;
    protected static String tml_cmp;
    protected static String tml_leng;
    protected static String tml_valor;
    protected static String sql_dnn_pai;
    protected static String sql_dnn_prv;
    protected static String sql_dnn_mun;
    protected static String sql_dnn_refCatastral;
    protected static String sql_mun_pai;
    protected static String sql_mun_prv;
    protected static String sql_mun_cod;

    protected static String usu_cod;
    protected static String usu_nom;
    protected static String sql_dpo_dirSuelo;
    protected static String sql_dsu_codDirSuelo;
    protected static String sql_dsu_pais;
    protected static String sql_dsu_prov;
    protected static String sql_dsu_mun;

    protected static String tra_mun;
    protected static String tra_pro;
    protected static String tra_cod;
    protected static String tra_utr;
    protected static String tra_cls;
    protected static String tra_ocu;
    protected static String tra_uin;
    protected static String tra_fba;
    protected static String tra_car;
    protected static String tra_cou;

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

    protected static String sql_doe_codMun;
    protected static String sql_doe_ejer;
    protected static String sql_doe_num;
    protected static String sql_doe_codProc;
    protected static String sql_doe_codDoc;
    protected static String sql_doe_fEntreg;

    protected static String uou_uor;
    protected static String uou_usu;
    protected static String uou_org;
    protected static String uou_ent;

    protected static String uor_cod;
    protected static String uor_nom;

    protected static String enp_mun;
    protected static String enp_pro;
    protected static String enp_cod;
    protected static String enp_des;
    protected static String enp_url;
    protected static String enp_est;

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
    protected	static String tca_desplegable;

    protected static String ter_cod;
    protected static String ter_doc;

    protected static String rol_mun;
    protected static String rol_pro;
    protected static String rol_cod;
    protected static String rol_des;
    protected static String rol_pde;

    
    protected static String rex_mun;
    protected static String rex_eje;
    protected static String rex_num;
    protected static String rex_munr;
    protected static String rex_ejer;
    protected static String rex_numr;
    
    protected static String sql_docsDepto;
    protected static String sql_docsUnid;
    protected static String sql_docsEjerc;
    protected static String sql_docsNum;
    protected static String sql_docsTipo;
    protected static String sql_docsNombreDoc;
    protected static String sql_docsTipoDoc;
    protected static String sql_docsDoc;
    protected static String idiomaDefecto;

    private static FichaExpedienteDAO instance = null;
    
    private Connection conexionRetroceso = null;
    protected GeneralValueObject listaTamFichero = new GeneralValueObject();

    public GeneralValueObject getListaTamFichero()
    {
        return listaTamFichero;
    }
    
    public void setListaTamFichero(GeneralValueObject _gvo)
    {
        listaTamFichero = _gvo;
    }
    
    protected FichaExpedienteDAO(Boolean cargarDatos) {
        super();
        if (cargarDatos==null) cargarDatos = true;

        if (cargarDatos){
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        des_val_campo = m_ConfigTechnical.getString("SQL.E_DES_VAL.campoValor");
        des_val_cod = m_ConfigTechnical.getString("SQL.E_DES_VAL.codigoValor");
        des_val_desc = m_ConfigTechnical.getString("SQL.E_DES_VAL.nombreValor");
        des_val_estado = m_ConfigTechnical.getString("SQL.E_DES_VAL.estadoValor");

        pml_mun = m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");
        pml_cod = m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
        pml_cmp = m_ConfigTechnical.getString("SQL.E_PML.codCampoML");
        pml_leng = m_ConfigTechnical.getString("SQL.E_PML.idioma");
        pml_valor = m_ConfigTechnical.getString("SQL.E_PML.valor");

        hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
        hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");
        hte_pa1 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido1");
        hte_ap1 = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
        hte_pa2 = m_ConfigTechnical.getString("SQL.T_HTE.partApellido2");
        hte_ap2 = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
        hte_nom = m_ConfigTechnical.getString("SQL.T_HTE.nombre");

        exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        exp_eje = m_ConfigTechnical.getString("SQL.E_EXP.ano");
        exp_num = m_ConfigTechnical.getString("SQL.E_EXP.numero");
        exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
        exp_fei = m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio");
        exp_fef = m_ConfigTechnical.getString("SQL.E_EXP.fechaFin");
        exp_est = m_ConfigTechnical.getString("SQL.E_EXP.estado");
        exp_usu = m_ConfigTechnical.getString( "SQL.E_EXP.usuario");
        exp_uor = m_ConfigTechnical.getString( "SQL.E_EXP.uor");
        exp_loc = m_ConfigTechnical.getString( "SQL.E_EXP.localizacion");
        exp_clo = m_ConfigTechnical.getString( "SQL.E_EXP.codLocalizacion");
        exp_obs = m_ConfigTechnical.getString( "SQL.E_EXP.observaciones");
        exp_asu = m_ConfigTechnical.getString( "SQL.E_EXP.asunto");
        exp_tra= m_ConfigTechnical.getString("SQL.E_EXP.codTramiteUltCerrado");
        exp_tocu= m_ConfigTechnical.getString("SQL.E_EXP.ocuTramiteUltCerrado");

        bloq_mun= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codMunicipio");
        bloq_eje= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ano");
        bloq_num= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.numero");
        bloq_pro= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codProcedimiento");
        bloq_tra= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.tramite");
        bloq_ocu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ocurrencia");
        bloq_usu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.usuario");

        ext_mun = m_ConfigTechnical.getString("SQL.E_EXT.codMunicipio");
        ext_eje = m_ConfigTechnical.getString("SQL.E_EXT.ano");
        ext_num = m_ConfigTechnical.getString("SQL.E_EXT.numero");
        ext_pro = m_ConfigTechnical.getString("SQL.E_EXT.codProcedimiento");
        ext_ter = m_ConfigTechnical.getString("SQL.E_EXT.codTercero");
        ext_nvr = m_ConfigTechnical.getString("SQL.E_EXT.verTercero");
        ext_rol = m_ConfigTechnical.getString("SQL.E_EXT.rolTercero");
        ext_dot = m_ConfigTechnical.getString("SQL.E_EXT.codDomicilio");

        pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");
        pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
        pro_tri = m_ConfigTechnical.getString("SQL.E_PRO.codTramiteInicio");
        pro_utr = m_ConfigTechnical.getString("SQL.E_PRO.unidad");
        pro_loc = m_ConfigTechnical.getString("SQL.E_PRO.poseeLocalizacion");

        
        //Variables añadidas
        tft_tra_mun = m_ConfigTechnical.getString("SQL.F_TRAFORM_TRA.codMunicipio");
        tft_tra_pro = m_ConfigTechnical.getString("SQL.F_TRAFORM_TRA.codProcedimiento");
        tft_tra_eje = m_ConfigTechnical.getString("SQL.F_TRAFORM_TRA.ano");
        tft_tra_num = m_ConfigTechnical.getString("SQL.F_TRAFORM_TRA.numero");
        tft_tra_tra = m_ConfigTechnical.getString("SQL.F_TRAFORM_TRA.codTramite");
        tft_tra_ocu = m_ConfigTechnical.getString("SQL.F_TRAFORM_TRA.ocurrencia");
        
        ff_trafirma_mun = m_ConfigTechnical.getString("SQL.F_FIRMA.codMunicipio");
        ff_trafirma_pro = m_ConfigTechnical.getString("SQL.F_FIRMA.codProcedimiento");
        ff_trafirma_eje = m_ConfigTechnical.getString("SQL.F_FIRMA.ano");
        ff_trafirma_num = m_ConfigTechnical.getString("SQL.F_FIRMA.numero");
        ff_trafirma_tra = m_ConfigTechnical.getString("SQL.F_FIRMA.codTramite");
        ff_trafirma_ocu = m_ConfigTechnical.getString("SQL.F_FIRMA.ocurrencia");
      
        txtt_mun = m_ConfigTechnical.getString("SQL.E_TXTT.codMunicipio");
        txtt_pro = m_ConfigTechnical.getString("SQL.E_TXTT.codProcedimiento");
        txtt_eje = m_ConfigTechnical.getString("SQL.E_TXTT.ejercicio");
        txtt_num = m_ConfigTechnical.getString("SQL.E_TXTT.numeroExpediente");
        txtt_tra = m_ConfigTechnical.getString("SQL.E_TXTT.codTramite");
        txtt_ocu = m_ConfigTechnical.getString("SQL.E_TXTT.ocurrencia");

        ttlt_mun = m_ConfigTechnical.getString("SQL.E_TTLT.codMunicipio");
        ttlt_pro = m_ConfigTechnical.getString("SQL.E_TTLT.codProcedimiento");
        ttlt_eje = m_ConfigTechnical.getString("SQL.E_TTLT.ejercicio");
        ttlt_num = m_ConfigTechnical.getString("SQL.E_TTLT.numeroExpediente");
        ttlt_tra = m_ConfigTechnical.getString("SQL.E_TTLT.codTramite");
        ttlt_ocu = m_ConfigTechnical.getString("SQL.E_TTLT.ocurrencia");
        
        tnut_mun = m_ConfigTechnical.getString("SQL.E_TNUT.codMunicipio");
        tnut_pro = m_ConfigTechnical.getString("SQL.E_TNUT.codProcedimiento");
        tnut_eje = m_ConfigTechnical.getString("SQL.E_TNUT.ejercicio");
        tnut_num = m_ConfigTechnical.getString("SQL.E_TNUT.numeroExpediente");
        tnut_tra = m_ConfigTechnical.getString("SQL.E_TNUT.codTramite");
        tnut_ocu = m_ConfigTechnical.getString("SQL.E_TNUT.ocurrencia");
        
        tnuct_mun = m_ConfigTechnical.getString("SQL.E_TNUCT.codMunicipio");
        tnuct_pro = m_ConfigTechnical.getString("SQL.E_TNUCT.codProcedimiento");
        tnuct_eje = m_ConfigTechnical.getString("SQL.E_TNUCT.ejercicio");
        tnuct_num = m_ConfigTechnical.getString("SQL.E_TNUCT.numeroExpediente");
        tnuct_tra = m_ConfigTechnical.getString("SQL.E_TNUCT.codTramite");
        tnuct_ocu = m_ConfigTechnical.getString("SQL.E_TNUCT.ocurrencia");
        
        tfit_mun = m_ConfigTechnical.getString("SQL.E_TFIT.codMunicipio");
        tfit_pro = m_ConfigTechnical.getString("SQL.E_TFIT.codProcedimiento");
        tfit_eje = m_ConfigTechnical.getString("SQL.E_TFIT.ejercicio");
        tfit_num = m_ConfigTechnical.getString("SQL.E_TFIT.numeroExpediente");
        tfit_tra = m_ConfigTechnical.getString("SQL.E_TFIT.codTramite");
        tfit_ocu = m_ConfigTechnical.getString("SQL.E_TFIT.ocurrencia");
        tfit_cod = m_ConfigTechnical.getString("SQL.E_TFIT.codCampo");
        tfit_nomfich   = m_ConfigTechnical.getString("SQL.E_TFIT.nombreFichero");
        tfit_mime      = m_ConfigTechnical.getString("SQL.E_TFIT.mime");
        tfit_valorfich = m_ConfigTechnical.getString("SQL.E_TFIT.valor");
        
        tfet_mun = m_ConfigTechnical.getString("SQL.E_TFET.codMunicipio");
        tfet_pro = m_ConfigTechnical.getString("SQL.E_TFET.codProcedimiento");
        tfet_eje = m_ConfigTechnical.getString("SQL.E_TFET.ejercicio");
        tfet_num = m_ConfigTechnical.getString("SQL.E_TFET.numeroExpediente");
        tfet_tra = m_ConfigTechnical.getString("SQL.E_TFET.codTramite");
        tfet_ocu = m_ConfigTechnical.getString("SQL.E_TFET.ocurrencia");
        
        tfect_mun = m_ConfigTechnical.getString("SQL.E_TFECT.codMunicipio");
        tfect_pro = m_ConfigTechnical.getString("SQL.E_TFECT.codProcedimiento");
        tfect_eje = m_ConfigTechnical.getString("SQL.E_TFECT.ejercicio");
        tfect_num = m_ConfigTechnical.getString("SQL.E_TFECT.numeroExpediente");
        tfect_tra = m_ConfigTechnical.getString("SQL.E_TFECT.codTramite");
        tfect_ocu = m_ConfigTechnical.getString("SQL.E_TFECT.ocurrencia");
        
        tdet_mun = m_ConfigTechnical.getString("SQL.E_TDET.codMunicipio");
        tdet_pro = m_ConfigTechnical.getString("SQL.E_TDET.codProcedimiento");
        tdet_eje = m_ConfigTechnical.getString("SQL.E_TDET.ejercicio");
        tdet_num = m_ConfigTechnical.getString("SQL.E_TDET.numeroExpediente");
        tdet_tra = m_ConfigTechnical.getString("SQL.E_TDET.codTramite");
        tdet_ocu = m_ConfigTechnical.getString("SQL.E_TDET.ocurrencia");
        
        //Fin variables añadidas
        
        cro_mun = m_ConfigTechnical.getString("SQL.E_CRO.codMunicipio");
        cro_pro = m_ConfigTechnical.getString("SQL.E_CRO.codProcedimiento");
        cro_tra = m_ConfigTechnical.getString("SQL.E_CRO.codTramite");
        cro_eje = m_ConfigTechnical.getString("SQL.E_CRO.ano");
        cro_num = m_ConfigTechnical.getString("SQL.E_CRO.numero");
        cro_usu = m_ConfigTechnical.getString("SQL.E_CRO.codUsuario");
        cro_fei = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicio");
        cro_fef = m_ConfigTechnical.getString("SQL.E_CRO.fechaFin");
        cro_utr = m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora");
        cro_ocu = m_ConfigTechnical.getString("SQL.E_CRO.ocurrencia");
        cro_fli = m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite");
        cro_usf = m_ConfigTechnical.getString("SQL.E_CRO.usuarioFinalizacion");
        cro_ffp = m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo");
        cro_fip = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicioPlazo");

       
        crd_mun = m_ConfigTechnical.getString("SQL.E_CRD.codMunicipio");
        crd_pro = m_ConfigTechnical.getString("SQL.E_CRD.codProcedimiento");
        crd_eje = m_ConfigTechnical.getString("SQL.E_CRD.ejercicio");
        crd_num = m_ConfigTechnical.getString("SQL.E_CRD.numeroExpediente");
        crd_tra = m_ConfigTechnical.getString("SQL.E_CRD.codTramite");
        crd_ocu = m_ConfigTechnical.getString("SQL.E_CRD.ocurrencia");

     

        utml_cod = m_ConfigTechnical.getString("SQL.A_UTML.codUnidadTramitadora");
        utml_cmp = m_ConfigTechnical.getString("SQL.A_UTML.codCampoML");
        utml_leng = m_ConfigTechnical.getString("SQL.A_UTML.idioma");
        utml_valor = m_ConfigTechnical.getString("SQL.A_UTML.valor");

        cml_cod = m_ConfigTechnical.getString("SQL.A_CML.codTramite");
        cml_cmp = m_ConfigTechnical.getString("SQL.A_CML.codCampoML");
        cml_leng = m_ConfigTechnical.getString("SQL.A_CML.idioma");
        cml_valor = m_ConfigTechnical.getString("SQL.A_CML.valor");
        // Tabla R_RES, E_EXR, T_DNN y T_MUN
        sql_res_codDpto= m_ConfigTechnical.getString("SQL.R_RES.codDpto");
        sql_res_codUnid= m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
        sql_res_tipoReg= m_ConfigTechnical.getString("SQL.R_RES.tipoReg");
        sql_res_ejer= m_ConfigTechnical.getString("SQL.R_RES.ejercicio");
        sql_res_num= m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacion");
        sql_res_fecAnot= m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");
        sql_res_asunt = m_ConfigTechnical.getString("SQL.R_RES.asunto");
        sql_res_estado = m_ConfigTechnical.getString("SQL.R_RES.estAnot");
        sql_res_codTerc= m_ConfigTechnical.getString("SQL.R_RES.codTercero");
        sql_res_domTerc= m_ConfigTechnical.getString("SQL.R_RES.domicTercero");
        sql_res_modTerc= m_ConfigTechnical.getString("SQL.R_RES.modifInteresado");
        sql_res_tipoDestino = m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino");

        tml_mun = m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
        tml_pro = m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
        tml_tra = m_ConfigTechnical.getString("SQL.E_TML.codTramite");
        tml_cmp = m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
        tml_leng = m_ConfigTechnical.getString("SQL.E_TML.idioma");
        tml_valor = m_ConfigTechnical.getString("SQL.E_TML.valor");

        sql_dnn_refCatastral = m_ConfigTechnical.getString("SQL.T_DNN.referenciaCatastral");
        sql_dnn_pai = m_ConfigTechnical.getString("SQL.T_DNN.idPaisD");
        sql_mun_pai = m_ConfigTechnical.getString("SQL.T_MUN.idPais");
        sql_dnn_prv = m_ConfigTechnical.getString("SQL.T_DNN.idProvinciaD");
        sql_mun_prv = m_ConfigTechnical.getString("SQL.T_MUN.idProvincia");
        sql_dnn_mun = m_ConfigTechnical.getString("SQL.T_DNN.idMunicipioD");
        sql_mun_cod = m_ConfigTechnical.getString("SQL.T_MUN.idMunicipio");

        usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
        usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");

        sql_dnn_dom = m_ConfigTechnical.getString("SQL.T_DNN.idDomicilio");
        sql_dpo_dom = m_ConfigTechnical.getString("SQL.T_DPO.domicilio"); // Domicilio tercero, normalizado
        sql_dpo_dirSuelo = m_ConfigTechnical.getString("SQL.T_DPO.suelo");
        sql_dsu_codDirSuelo = m_ConfigTechnical.getString("SQL.T_DSU.identificador");
        sql_dsu_pais= m_ConfigTechnical.getString("SQL.T_DSU.pais");
        sql_dsu_prov= m_ConfigTechnical.getString("SQL.T_DSU.provincia");
        sql_dsu_mun = m_ConfigTechnical.getString("SQL.T_DSU.municipio");

        tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
        tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
        tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
        tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");
        tra_cls = m_ConfigTechnical.getString("SQL.E_TRA.clasificacion");
        tra_ocu = m_ConfigTechnical.getString("SQL.E_TRA.ocurrencias");
        tra_uin = m_ConfigTechnical.getString("SQL.E_TRA.unidadInicio");
        tra_fba = m_ConfigTechnical.getString("SQL.E_TRA.fechaBaja");
        tra_car = m_ConfigTechnical.getString("SQL.E_TRA.cargo");
        tra_ws_cod = m_ConfigTechnical.getString("SQL.E_TRA.wsTramitar");
        tra_ws_ob = m_ConfigTechnical.getString("SQL.E_TRA.wsTramitarTipo");
        tra_ws_cod_retro = m_ConfigTechnical.getString("SQL.E_TRA.wsRetroceder");
        tra_ws_ob_retro = m_ConfigTechnical.getString("SQL.E_TRA.wsRetrocederTipo");
        tra_cou = m_ConfigTechnical.getString("SQL.E_TRA.codTramiteUsuario");

        sql_exr_mun= m_ConfigTechnical.getString("SQL.E_EXR.codMunicipio");
        sql_exr_pro= m_ConfigTechnical.getString("SQL.E_EXR.codProcedimiento");
        sql_exr_eje= m_ConfigTechnical.getString("SQL.E_EXR.ejercicio");
        sql_exr_num= m_ConfigTechnical.getString("SQL.E_EXR.numero");
        sql_exr_dep= m_ConfigTechnical.getString("SQL.E_EXR.departamentoAnotacion");
        sql_exr_uor= m_ConfigTechnical.getString("SQL.E_EXR.unidadAnotacion");
        sql_exr_ejr= m_ConfigTechnical.getString("SQL.E_EXR.ejercicioAnotacion");
        sql_exr_nre= m_ConfigTechnical.getString("SQL.E_EXR.numeroRegistro");
        sql_exr_tip= m_ConfigTechnical.getString("SQL.E_EXR.tipoRegistro");
        sql_exr_ori= m_ConfigTechnical.getString("SQL.E_EXR.origen");
        sql_exr_top= m_ConfigTechnical.getString("SQL.E_EXR.tipoOperacion");


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

        sql_doe_codMun = m_ConfigTechnical.getString("SQL.E_DOE.codMunicipio");
        sql_doe_ejer = m_ConfigTechnical.getString("SQL.E_DOE.ejercicio");
        sql_doe_num = m_ConfigTechnical.getString("SQL.E_DOE.numero");
        sql_doe_codProc = m_ConfigTechnical.getString("SQL.E_DOE.codProcedimiento");
        sql_doe_codDoc = m_ConfigTechnical.getString("SQL.E_DOE.codDocumento");
        sql_doe_fEntreg = m_ConfigTechnical.getString("SQL.E_DOE.fechaEntrega");

        uou_uor = m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        uou_usu = m_ConfigTechnical.getString("SQL.A_UOU.usuario");
        uou_org = m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
        uou_ent = m_ConfigTechnical.getString("SQL.A_UOU.entidad");

        uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        uor_nom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");

        enp_mun = m_ConfigTechnical.getString("SQL.E_ENP.codMunicipio");
        enp_pro = m_ConfigTechnical.getString("SQL.E_ENP.codProcedimiento");
        enp_cod = m_ConfigTechnical.getString("SQL.E_ENP.codEnlace");
        enp_des = m_ConfigTechnical.getString("SQL.E_ENP.descripcion");
        enp_url = m_ConfigTechnical.getString("SQL.E_ENP.url");
        enp_est = m_ConfigTechnical.getString("SQL.E_ENP.estado");

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
        tca_desplegable	= m_ConfigTechnical.getString("SQL.E_TCA.desplegable");

        
        rex_mun = m_ConfigTechnical.getString("SQL.E_REX.codMunicipio");
        rex_eje = m_ConfigTechnical.getString("SQL.E_REX.ejercicio");
        rex_num = m_ConfigTechnical.getString("SQL.E_REX.numeroExpediente");
        rex_munr = m_ConfigTechnical.getString("SQL.E_REX.codMunicipioRelacionado");
        rex_ejer = m_ConfigTechnical.getString("SQL.E_REX.ejercicioRelacionado");
        rex_numr = m_ConfigTechnical.getString("SQL.E_REX.numeroExpedienteRelacionado");
        
        
        
        ter_cod = m_ConfigTechnical.getString("SQL.T_TER.identificador");
        ter_doc = m_ConfigTechnical.getString("SQL.T_TER.documento");

        rol_mun = m_ConfigTechnical.getString("SQL.E_ROL.codMunicipio");
        rol_pro = m_ConfigTechnical.getString("SQL.E_ROL.codProcedimiento");
        rol_cod = m_ConfigTechnical.getString("SQL.E_ROL.codRol");
        rol_des = m_ConfigTechnical.getString("SQL.E_ROL.descripcion");
        rol_pde = m_ConfigTechnical.getString("SQL.E_ROL.porDefecto");
        
        // Documentos de un asiento
        sql_docsDepto     = m_ConfigTechnical.getString("SQL.R_RED.codDpto");
        sql_docsUnid      = m_ConfigTechnical.getString("SQL.R_RED.codUnidad");
        sql_docsEjerc     = m_ConfigTechnical.getString("SQL.R_RED.ejercicio");
        sql_docsNum       = m_ConfigTechnical.getString("SQL.R_RED.numeroAnotacion");
        sql_docsTipo      = m_ConfigTechnical.getString("SQL.R_RED.tipoReg");
        sql_docsNombreDoc = m_ConfigTechnical.getString("SQL.R_RED.nombreDoc");
        sql_docsTipoDoc   = m_ConfigTechnical.getString("SQL.R_RED.tipoDoc");
        sql_docsDoc       = m_ConfigTechnical.getString("SQL.R_RED.doc");
        idiomaDefecto       = m_ConfigTechnical.getString("idiomaDefecto");
        }
    }

    public static FichaExpedienteDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        synchronized (FichaExpedienteDAO.class) {
            if (instance == null) {
                instance = new FichaExpedienteDAO(true);
            }
        }

        return instance;
    }
    
    public static FichaExpedienteDAO getInstance(Boolean cargarDatos) {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        synchronized (FichaExpedienteDAO.class) {
            if (instance == null) {
                instance = new FichaExpedienteDAO(cargarDatos);
            }
        }

        return instance;
    }

    public GeneralValueObject cargaExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {
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
                    " WHERE " + pml_mun + " = " + codMunicipio + " AND " + pml_cod + " = '" + codProcedimiento + "' AND " + pml_cmp + " = 'NOM' AND " + pml_leng + 
                    " = " + idiomaDefecto;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                gVO.setAtributo("nombreProcedimiento",rs.getString(pml_valor));
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);                                

            sql = "SELECT " + pro_loc + ",PRO_INTOBL,PRO_PORCENTAJE,PRO_UND,PRO_PLZ " +
                    " FROM E_PRO" +
                    " WHERE " + pro_mun + " = " + codMunicipio + " AND " + pro_cod + " = '" + codProcedimiento + "'";

            DefinicionProcedimientosValueObject definicion = new DefinicionProcedimientosValueObject();
            definicion.setTxtCodigo(codProcedimiento);
            //definicion = DefinicionProcedimientosManager.getInstance().getPlazosFinalizacion(definicion, con);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                gVO.setAtributo("poseeLocalizacion",rs.getString(pro_loc));
                String interesadoObligatorio = rs.getString("PRO_INTOBL");
                
                gVO.setAtributo("interesadoObligatorio","NO");
                if(interesadoObligatorio!=null && "1".equals(interesadoObligatorio))
                    gVO.setAtributo("interesadoObligatorio","SI");
                
                definicion.setPorcentaje(rs.getString("PRO_PORCENTAJE"));
                definicion.setPlazo(rs.getString("PRO_PLZ"));
                definicion.setTipoPlazo(rs.getString("PRO_UND"));
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            st = con.createStatement();
            sql = "SELECT " + exp_loc + "," + exp_clo + "," + usu_nom + "," + exp_uor + "," + uor_nom + "," +
                    oad.convertir(exp_fei,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fInicio " + "," +
                    oad.convertir(exp_fef,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFin " + "," + exp_obs +
                    "," + exp_asu + "," + sql_dnn_refCatastral + "," + exp_est + " FROM A_UOR,  " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU,E_EXP" +
                    " left join T_DNN on (EXP_CLO=DNN_DOM) "+
                    " WHERE "+
                    exp_mun + " = " + codMunicipio + " AND " +
                    exp_eje + " = " + ejercicio + " AND " +
                    exp_num + "='" + numero + "' AND " + exp_usu + " = " + usu_cod + " AND " +
                    exp_uor + "=" + uor_cod ;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);


            while(rs.next()){
                gVO.setAtributo("localizacion",rs.getString(exp_loc));
                gVO.setAtributo("codLocalizacion",rs.getString(exp_clo));
                gVO.setAtributo("fechaInicioExpediente",rs.getString("fInicio"));
                gVO.setAtributo("fechaFinExpediente",rs.getString("fFin"));
                gVO.setAtributo("usuarioExpediente",rs.getString(usu_nom));
                gVO.setAtributo("codUnidadOrganicaExp",rs.getString(exp_uor));
                gVO.setAtributo("descUnidadOrganicaExp",rs.getString(uor_nom));
                gVO.setAtributo("refCatastral",rs.getString(sql_dnn_refCatastral));
                gVO.setAtributo("estado",rs.getString(exp_est));
                String observ = rs.getString(exp_obs);
                if (observ != null)
                    gVO.setAtributo("observaciones",AdaptadorSQLBD.js_escape(observ));
                else gVO.setAtributo("observaciones",observ);
                String asunto = rs.getString(exp_asu);
                if (asunto != null)
                    gVO.setAtributo("asunto",AdaptadorSQLBD.js_escape(asunto));
                else gVO.setAtributo("asunto",asunto);

                /****** CALCULO PLAZO FIN DE EXPEDIENTE ******/
                

                Calendar fechaInicio = DateOperations.toCalendar((String)gVO.getAtributo("fechaInicioExpediente"),"dd/MM/yyyy");
                fechaInicio.set(Calendar.HOUR,0);
                fechaInicio.set(Calendar.MINUTE,0);
                fechaInicio.set(Calendar.SECOND,0);
                if(definicion!=null && definicion.getPlazo()!=null && definicion.getTipoPlazo()!=null){
                    Calendar fechaFin = null;
                    ArrayList<Calendar> festivos = TramitacionManager.getInstance().getFestivos(fechaInicio.get(Calendar.YEAR), con);
                    
                    int porcentaje = 0;
                    if(definicion.getPorcentaje()!=null && definicion.getPorcentaje().length()>0)
                        porcentaje = Integer.parseInt(definicion.getPorcentaje());

                    // Se obtiene la fecha de fin del expediente
                    fechaFin = TramitacionManager.getInstance().calcularFechaFinExpediente(fechaInicio,Integer.parseInt(definicion.getPlazo()),Integer.parseInt(definicion.getTipoPlazo()),festivos);
                    m_Log.debug(">>>>>>> getExpedientesPendiente fechaInicio " + this.mostrarFecha(fechaInicio)); 
                    m_Log.debug(">>>>>>> getExpedientesPendiente fechaFin " + this.mostrarFecha(fechaFin)); 

                   // Se comprueba si el expediente está cercano a la fecha de fin o fuera de plazo
                    String resultado = TramitacionManager.getInstance().calculoExpedienteCercaFinPlazo(porcentaje, fechaInicio, fechaFin);
                    // Si la salida es un si es que el expediente está cerca de fin de plazo
                      // Si el resultado es nulo es que no se está ni cerca ni fuera de plazo
                    if(resultado==null){
                        gVO.setAtributo("cercaPlazoExpediente",false);
                        gVO.setAtributo("fueraPlazoExpediente",false);
                    }else{
                        if(resultado.equals("si")){
                            gVO.setAtributo("cercaPlazoExpediente",true);
                            gVO.setAtributo("fueraPlazoExpediente",false);
                        }
                        // Si el resultado es no es que no lo está
                        if(resultado.equals("no")){
                            gVO.setAtributo("cercaPlazoExpediente",false);
                            gVO.setAtributo("fueraPlazoExpediente",true);                            
                        }
                    }
                } else{
                    gVO.setAtributo("cercaPlazoExpediente",false);
                    gVO.setAtributo("fueraPlazoExpediente",false);
                }

                /******* CALCULO PLAZO FIN DE EXPEDIENTE *****/

            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            sql = "SELECT EXT_TER, EXT_NVR, HTE_PA1, HTE_AP1, HTE_PA2, HTE_AP2, HTE_NOM, HTE_DOC " +
                    "FROM E_EXT " +
                    " left join T_HTE on (EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR)"+
                    " WHERE EXT_MUN = " + codMunicipio + " AND EXT_EJE = " + ejercicio + " " +
                    "AND EXT_NUM = '" + numero + "' " + " AND MOSTRAR = 1 " ;
                   
            
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()){
                
                gVO.setAtributo("codigoTercero", rs.getString(ext_ter));
                gVO.setAtributo("versionTercero", rs.getString(ext_nvr));
                String tercero_p1a = rs.getString(hte_pa1);
                String tercero_a1a = rs.getString(hte_ap1);
                String tercero_p2a = rs.getString(hte_pa2);
                String tercero_a2a = rs.getString(hte_ap2);
                String tercero_nom = rs.getString(hte_nom);
                String documento=rs.getString("HTE_DOC");
                String titular = "";
                if (tercero_p1a != null) titular += tercero_p1a + " ";
                if (tercero_a1a != null) titular += tercero_a1a + " ";
                if (tercero_p2a != null) titular += tercero_p2a + " ";
                if (tercero_a2a != null) titular += tercero_a2a + " ";
                
                if (titular.trim().equals("")) titular = tercero_nom;
                else titular = titular.substring(0, titular.length()-1) + ", " + tercero_nom;
                gVO.setAtributo("titular",titular);    
                gVO.setAtributo("documentoTitular",documento);  
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            m_Log.debug("EJERCICIO: " + gVO.getAtributo("ejercicio"));
        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("");
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        return gVO;
    }
    
    
     
     
     
    public GeneralValueObject cargaExpediente(GeneralValueObject gVO,AdaptadorSQLBD oad, Connection con) throws TechnicalException {        
        ResultSet rs = null;
        PreparedStatement ps = null;
        
        try{
            
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            
            String sql = "SELECT PRO_LOC,PRO_INTOBL,PRO_PORCENTAJE,PRO_UND,PRO_PLZ,PML_VALOR " + 
                         "FROM E_PRO,E_PML " + 
                         "WHERE PML_MUN=? AND PML_COD=? AND PML_CMP='NOM' AND PML_LENG=? " + 
                         "AND PML_MUN = PRO_MUN AND PRO_COD=PML_COD ";
            

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setString(i++,idiomaDefecto);
            
            rs = ps.executeQuery();
            
            DefinicionProcedimientosValueObject definicion = new DefinicionProcedimientosValueObject();
            definicion.setTxtCodigo(codProcedimiento);
            
            while(rs.next()){
                gVO.setAtributo("nombreProcedimiento",rs.getString("PML_VALOR"));
                
                gVO.setAtributo("poseeLocalizacion",rs.getString("PRO_LOC"));
                String interesadoObligatorio = rs.getString("PRO_INTOBL");
                
                gVO.setAtributo("interesadoObligatorio","NO");
                if(interesadoObligatorio!=null && "1".equals(interesadoObligatorio))
                    gVO.setAtributo("interesadoObligatorio","SI");
                
                definicion.setPorcentaje(rs.getString("PRO_PORCENTAJE"));
                definicion.setPlazo(rs.getString("PRO_PLZ"));
                definicion.setTipoPlazo(rs.getString("PRO_UND"));
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);                                

            
             if (esExpedienteHistorico(numero, con))
                sql = "SELECT EXP_LOC,EXP_CLO,USU_NOM,EXP_UOR,UOR_NOM," +
                        oad.convertir("EXP_FEI",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fInicio," +
                        oad.convertir("EXP_FEF",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFin,EXP_OBS, " +
                        "EXP_ASU,DNN_RCA,EXP_EST,EXP_UBICACION_DOC FROM A_UOR,  " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU,HIST_E_EXP" +
                        " left join T_DNN on (EXP_CLO=DNN_DOM)"+
                        " WHERE "+
                        "EXP_MUN=? AND EXP_EJE=? AND EXP_NUM=? " +
                        "AND EXP_USU=USU_COD AND EXP_UOR=UOR_COD";
            else
                sql = "SELECT EXP_LOC,EXP_CLO,USU_NOM,EXP_UOR,UOR_NOM," +
                        oad.convertir("EXP_FEI",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fInicio," +
                        oad.convertir("EXP_FEF",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFin,EXP_OBS, " +
                        "EXP_ASU,DNN_RCA,EXP_EST,EXP_UBICACION_DOC FROM A_UOR,  " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU,E_EXP" +
                        " left join T_DNN on (EXP_CLO=DNN_DOM)"+
                        " WHERE "+
                        "EXP_MUN=? AND EXP_EJE=? AND EXP_NUM=? " +
                        "AND EXP_USU=USU_COD AND EXP_UOR=UOR_COD";
                        
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            i = 1;
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            while(rs.next()){
                gVO.setAtributo("localizacion",rs.getString("EXP_LOC"));
                gVO.setAtributo("codLocalizacion",rs.getString("EXP_CLO"));
                gVO.setAtributo("fechaInicioExpediente",rs.getString("fInicio"));
                gVO.setAtributo("fechaFinExpediente",rs.getString("fFin"));
                gVO.setAtributo("usuarioExpediente",rs.getString("USU_NOM"));
                gVO.setAtributo("codUnidadOrganicaExp",rs.getString("EXP_UOR"));
                gVO.setAtributo("descUnidadOrganicaExp",rs.getString("UOR_NOM"));
                gVO.setAtributo("refCatastral",rs.getString("DNN_RCA"));
                gVO.setAtributo("estado",rs.getString("EXP_EST"));
                String observ = rs.getString("EXP_OBS");
                if (observ != null)
                    gVO.setAtributo("observaciones",AdaptadorSQLBD.js_escape(observ));
                else gVO.setAtributo("observaciones",observ);
                String asunto = rs.getString("EXP_ASU");
                if (asunto != null)
                    gVO.setAtributo("asunto",AdaptadorSQLBD.js_escape(asunto));
                else gVO.setAtributo("asunto",asunto);
                // #253692: Recuperamos el valor de ubicación para mostrarlo en pantalla
                gVO.setAtributo("ubicacionDoc",rs.getString("EXP_UBICACION_DOC"));

                /****** CALCULO PLAZO FIN DE EXPEDIENTE ******/
                

                Calendar fechaInicio = DateOperations.toCalendar((String)gVO.getAtributo("fechaInicioExpediente"),"dd/MM/yyyy");
                fechaInicio.set(Calendar.HOUR,0);
                fechaInicio.set(Calendar.MINUTE,0);
                fechaInicio.set(Calendar.SECOND,0);
                if(definicion!=null && definicion.getPlazo()!=null && definicion.getTipoPlazo()!=null){
                    Calendar fechaFin = null;
                    ArrayList<Calendar> festivos = TramitacionManager.getInstance().getFestivos(fechaInicio.get(Calendar.YEAR), con);
                    
                    int porcentaje = 0;
                    if(definicion.getPorcentaje()!=null && definicion.getPorcentaje().length()>0)
                        porcentaje = Integer.parseInt(definicion.getPorcentaje());

                    // Se obtiene la fecha de fin del expediente
                    fechaFin = TramitacionManager.getInstance().calcularFechaFinExpediente(fechaInicio,Integer.parseInt(definicion.getPlazo()),Integer.parseInt(definicion.getTipoPlazo()),festivos);
                    m_Log.debug(">>>>>>> getExpedientesPendiente fechaInicio " + this.mostrarFecha(fechaInicio)); 
                    m_Log.debug(">>>>>>> getExpedientesPendiente fechaFin " + this.mostrarFecha(fechaFin)); 

                   // Se comprueba si el expediente está cercano a la fecha de fin o fuera de plazo
                    String resultado = TramitacionManager.getInstance().calculoExpedienteCercaFinPlazo(porcentaje, fechaInicio, fechaFin);
                    // Si la salida es un si es que el expediente está cerca de fin de plazo
                      // Si el resultado es nulo es que no se está ni cerca ni fuera de plazo
                    if(resultado==null){
                        gVO.setAtributo("cercaPlazoExpediente",false);
                        gVO.setAtributo("fueraPlazoExpediente",false);
                    }else{
                        if(resultado.equals("si")){
                            gVO.setAtributo("cercaPlazoExpediente",true);
                            gVO.setAtributo("fueraPlazoExpediente",false);
                        }
                        // Si el resultado es no es que no lo está
                        if(resultado.equals("no")){
                            gVO.setAtributo("cercaPlazoExpediente",false);
                            gVO.setAtributo("fueraPlazoExpediente",true);                            
                        }
                    }
                } else{
                    gVO.setAtributo("cercaPlazoExpediente",false);
                    gVO.setAtributo("fueraPlazoExpediente",false);
                }

                /******* CALCULO PLAZO FIN DE EXPEDIENTE *****/

            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            
            /******
             * SE COMENTA PORQUE NO SE UTILIZA EN LA FICHA DEL EXPEDIENTE           
            if ("true".equals(expHistorico))
                sql = "SELECT EXT_TER, EXT_NVR, HTE_PA1, HTE_AP1, HTE_PA2, HTE_AP2, HTE_NOM, HTE_DOC " +
                        "FROM HIST_E_EXT " +
                        " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)"+
                        "WHERE EXT_MUN =? AND EXT_EJE=? " +
                        "AND EXT_NUM =? AND MOSTRAR = 1 ";
            else
                sql = "SELECT EXT_TER, EXT_NVR, HTE_PA1, HTE_AP1, HTE_PA2, HTE_AP2, HTE_NOM, HTE_DOC " +
                        "FROM E_EXT " +
                        " left join T_HTE on (EXT_TER=HTE_TER and EXT_NVR=HTE_NVR)"+
                        "WHERE EXT_MUN =? AND EXT_EJE=? " +
                        "AND EXT_NUM =? AND MOSTRAR = 1 ";
            
            
            String parametros[] = {"1","1"};
            sql += oad.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            i = 1;
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            if (rs.next()){
                
                gVO.setAtributo("codigoTercero", rs.getString("ext_ter"));
                gVO.setAtributo("versionTercero", rs.getString("ext_nvr"));
                String tercero_p1a = rs.getString("hte_pa1");
                String tercero_a1a = rs.getString("hte_ap1");
                String tercero_p2a = rs.getString("hte_pa2");
                String tercero_a2a = rs.getString("hte_ap2");
                String tercero_nom = rs.getString("hte_nom");
                String documento=rs.getString("HTE_DOC");
                String titular = "";
                if (tercero_p1a != null) titular += tercero_p1a + " ";
                if (tercero_a1a != null) titular += tercero_a1a + " ";
                if (tercero_p2a != null) titular += tercero_p2a + " ";
                if (tercero_a2a != null) titular += tercero_a2a + " ";
                
                if (titular.trim().equals("")) titular = tercero_nom;
                else titular = titular.substring(0, titular.length()-1) + ", " + tercero_nom;
                gVO.setAtributo("titular",titular);    
                gVO.setAtributo("documentoTitular",documento);  
            } 
            */
            
            m_Log.debug("EJERCICIO: " + gVO.getAtributo("ejercicio"));
        }catch (Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
            //if(m_Log.isErrorEnabled()) m_Log.error("");
        } finally {            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return gVO;
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



        public Vector cargaTramites(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        Vector<GeneralValueObject> tramites = new Vector<GeneralValueObject>();
        Vector<GeneralValueObject> tramites1 = new Vector<GeneralValueObject>();
        Vector<GeneralValueObject> tramitesFinal = new Vector<GeneralValueObject>();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            String usuario = (String)gVO.getAtributo("usuario");
            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codEntidad = (String) gVO.getAtributo("codEntidad");

            String from = "CRO_OCU, TRA_COD, TML_VALOR, CRO_UTR"
                    + ", " + oad.convertir(cro_fei,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fInicio "
                    + ", " + oad.convertir(cro_fef,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFin "
                     + ", " + oad.convertir(cro_fip,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fIniPlazo "
                    + ", " + oad.convertir(cro_ffp,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFinPlazo "
                    + ", CRO_USU, " +  "A_USU.USU_NOM AS usu_nom, CRO_USF, CML_VALOR, PRO_TRI, " +
                    oad.convertir(cro_fli,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fLimite, " +                    
                    "A_USU_BLQ.USU_NOM AS usu_nom_blq, A_USU_BLQ.USU_COD AS usu_cod_blq, TRA_FIN, TRA_UND, TRA_PLZ,TRA_NOTIFICACION_ELECTRONICA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN , TRA_NOTIF_ELECT_OBLIG, TRA_NOTIF_FIRMA_CERT_ORG, " + 
                    "(SELECT COUNT(*) FROM TAREAS_PENDIENTES_INICIO WHERE NUM_EXPEDIENTE=CRO_NUM AND COD_MUNICIPIO=" + codMunicipio + 
                    " AND COD_TRAMITE=CRO_TRA AND OCU_TRAMITE=CRO_OCU) TAREAS";
           
            ArrayList<String> join = new ArrayList<String>();
            join.add("e_cro");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu");
            join.add("A_USU.USU_COD = CRO_USU");
            join.add("INNER");
            join.add("e_tra");
            join.add("TRA_MUN = CRO_MUN and TRA_PRO = CRO_PRO and TRA_COD = CRO_TRA");
            join.add("INNER");
            join.add("e_tml");
            join.add("TML_MUN = TRA_MUN and TML_PRO = TRA_PRO and TML_TRA = TRA_COD and TML_CMP = 'NOM' and TML_LENG = '"+idiomaDefecto+"'");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "a_cml a_cml");
            join.add("CML_COD = TRA_CLS and CML_CMP = 'NOM' and CML_LENG = '"+idiomaDefecto+"'");
            join.add("INNER");
            join.add("e_pro");
            join.add("TRA_MUN = PRO_MUN AND TRA_PRO = PRO_COD");
            join.add("LEFT");
            join.add("e_exp_bloq");
            join.add("CRO_MUN = BLQ_EXP_MUN and CRO_PRO = BLQ_EXP_PRO and CRO_EJE = BLQ_EXP_EJE and " +
                    "CRO_NUM = BLQ_EXP_NUM and CRO_TRA = BLQ_TRA_COD and CRO_OCU = BLQ_TRA_OCU");
            join.add("LEFT");
            join.add(GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu_blq");
            join.add("BLQ_USUARIO = A_USU_BLQ.USU_COD");

            join.add("false");

            String where = "CRO_MUN = " + codMunicipio + " and CRO_PRO = '" + codProcedimiento + "' " +
                    "and CRO_EJE = " + ejercicio + " and CRO_NUM = '" + numero + "'";

            String order = " order by CRO_FEI desc";
            String sql = oad.join(from, where, join.toArray(new String[]{})) + order;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            
            Vector resultadoTareasPendientes=new Vector();
            boolean tareasPendientesConsultadas=false;
              
            while(rs.next()){
                GeneralValueObject tramiteVO = new GeneralValueObject();
                String codTramite = rs.getString("TRA_COD");
                tramiteVO.setAtributo("codTramite",codTramite);
                String ocurrenciaTramite = rs.getString("CRO_OCU");
                tramiteVO.setAtributo("ocurrenciaTramite", ocurrenciaTramite);
                tramiteVO.setAtributo("tramite",rs.getString("TML_VALOR"));
                String fechaInicio=rs.getString("fInicio");
                tramiteVO.setAtributo("fehcaInicio",fechaInicio);
                String fechaFin = rs.getString("fFin");
                tramiteVO.setAtributo("fechaFin",fechaFin);
                
                tramiteVO.setAtributo("codUsuario",rs.getString("CRO_USU"));
                tramiteVO.setAtributo("usuario",rs.getString("USU_NOM"));
                tramiteVO.setAtributo("codUsuarioFinalizacion",rs.getString("CRO_USF"));
                tramiteVO.setAtributo("clasificacion",rs.getString("CML_VALOR"));
                tramiteVO.setAtributo("codUniTramTramite",rs.getString("CRO_UTR"));
                String codTramiteInicio = rs.getString("PRO_TRI");
                if(codTramiteInicio != null && codTramiteInicio.equals(codTramite)) {
                    tramiteVO.setAtributo("tramiteInicio","si");
                } else {
                    tramiteVO.setAtributo("tramiteInicio","no");
                }
                String fechaLimite = rs.getString("fLimite");
                String fechaFinPlazo = rs.getString("fFinPlazo");
                
                tramiteVO.setAtributo("notificacionObligatoria", Integer.valueOf(rs.getInt("TRA_NOTIF_ELECT_OBLIG")));
                tramiteVO.setAtributo("certificadoOrganismo", Integer.valueOf(rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG")));
                
                // Se calcula si el tramite esta fuera de plazo.
                m_Log.debug("LA FECHA LIMITE DE LA TRAMITACION ES: " + fechaLimite);
                if(fechaLimite != null && !"".equals(fechaLimite)) {
                    java.util.Date dateLimite = Fecha.obtenerDate(fechaLimite);
                    Calendar calFechaLimite = Calendar.getInstance();
                    calFechaLimite.setTime(dateLimite);
                    calFechaLimite.set(Calendar.HOUR_OF_DAY, 23);
                    calFechaLimite.set(Calendar.MINUTE, 59);
                    calFechaLimite.set(Calendar.SECOND, 59);
                    
                    Calendar fechaActual = Calendar.getInstance();
                    
                    if (fechaActual.after(calFechaLimite)) tramiteVO.setAtributo("fueraDePlazo","si");
                    else tramiteVO.setAtributo("fueraDePlazo","no");
                } else {
                    tramiteVO.setAtributo("fueraDePlazo","no");
                }
                tramiteVO.setAtributo("usuarioBloq", rs.getString("usu_nom_blq"));
                tramiteVO.setAtributo("codUsuarioBloq", rs.getString("usu_cod_blq"));
                tramiteVO.setAtributo("traNotificacionElectronica", rs.getString("TRA_NOTIFICACION_ELECTRONICA"));
                
                // Se calcula si el tramite esta cercano al fin de plazo.
                int plazoCercaFin = rs.getInt("TRA_FIN");
                String tipoPlazo = rs.getString("TRA_UND");
                int unidadesPlazo = rs.getInt("TRA_PLZ");
                
                if(fechaLimite != null && !"".equals(fechaLimite) && tipoPlazo!=null) {
                    java.util.Date dateLimite = Fecha.obtenerDate(fechaLimite);
                    Calendar calFechaLimite = Calendar.getInstance();
                    calFechaLimite.setTime(dateLimite);
                    calFechaLimite.set(Calendar.HOUR_OF_DAY, 23);
                    calFechaLimite.set(Calendar.MINUTE, 59);
                    calFechaLimite.set(Calendar.SECOND, 59);
                                        
                    String fuera = TramitacionDAO.getInstance().calculoCercaFinPlazo(plazoCercaFin, calFechaLimite, tipoPlazo, unidadesPlazo, con, oad);
                    tramiteVO.setAtributo("plazoCercaFin", fuera);
                } else {
                    tramiteVO.setAtributo("plazoCercaFin","no");
                }
                                               
                m_Log.debug("EL TRAMITE SE ENCUENTRA FUERA DE PLAZO: " + tramiteVO.getAtributo("fueraDePlazo"));
                m_Log.debug("EL TRAMITE SE ENCUENTRA CERCA DE FIN DE PLAZO: " + tramiteVO.getAtributo("plazoCercaFin"));

                /*** SE COMPRUEBA SI EL TRÁMITE TIENE TAREAS DE INICIO PENDIENTES DE EJECUTAR **/                
                tramiteVO.setAtributo("tieneTareasPendientesInicio","NO");
                if(rs.getInt("TAREAS")>=1){
                    tramiteVO.setAtributo("tieneTareasPendientesInicio","SI");                    
                }
                              
                String extPlugin = rs.getString("TRA_TRAM_EXT_PLUGIN");
                String urlExtPlugin = rs.getString("TRA_TRAM_EXT_URL");
                String implClass = rs.getString("TRA_TRAM_EXT_IMPLCLASS");
                String idEnlace  = rs.getString("TRA_TRAM_ID_ENLACE_PLUGIN");

                tramiteVO.setAtributo("extPlugin","");
                tramiteVO.setAtributo("extUrl","");
                tramiteVO.setAtributo("extImplClass","");
                tramiteVO.setAtributo("extIdEnlace","");
                tramiteVO.setAtributo("extBloqueoFinalizarTramite","NO");
                tramiteVO.setAtributo("extBloqueoRetrocesoTramite","NO");

                if(extPlugin!=null && urlExtPlugin!=null && implClass!=null && idEnlace!=null && !"".equals(extPlugin)
                        && !"".equals(urlExtPlugin) && !"".equals(implClass) && !"".equals(idEnlace)){
                    
                    TramitacionExternaBase plugin = TramitacionExternaCargador.getInstance().getPlugin(Integer.parseInt(codMunicipio),extPlugin,idEnlace,urlExtPlugin,implClass);
                    if(plugin!=null){
                        tramiteVO.setAtributo("extPlugin",extPlugin);
                        tramiteVO.setAtributo("extUrl",urlExtPlugin);
                        tramiteVO.setAtributo("extImplClass",implClass);
                        tramiteVO.setAtributo("extIdEnlace",implClass);
                        
                        if(plugin.isBloqueadoFinalizarTramite())
                            tramiteVO.setAtributo("extBloqueoFinalizarTramite","SI");
                        else
                            tramiteVO.setAtributo("extBloqueoFinalizarTramite","NO");
                                                
                        if(plugin.isBloqueadoRetrocesoTramite()){
                            tramiteVO.setAtributo("extBloqueoRetrocesoTramite","SI");
                        }else
                            tramiteVO.setAtributo("extBloqueoRetrocesoTramite","NO");
                        
                    }// if
                }// if
    
                /*** FIN:COMPROBACIÓN SI EL TRÁMITE TIENE TAREAS DE INICIO PENDIENTES DE EJECUTAR **/
                tramites.add(tramiteVO);
            }
            
           
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            for(int j=0;j<tramites.size();j++) {
                GeneralValueObject g = tramites.elementAt(j);
                String codUnidadTramitadoraTram = (String) g.getAtributo("codUniTramTramite");
                if(codUnidadTramitadoraTram == null || "".equals(codUnidadTramitadoraTram)) {
                    g.setAtributo("unidad","");
                } else {
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],codUnidadTramitadoraTram);

                    if (uorDTO!=null){
                        g.setAtributo("unidad", uorDTO.getUor_nom());
                    } 
                }
                tramites1.addElement(g);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            for(int i=0;i<tramites1.size();i++) {
                GeneralValueObject g = tramites1.elementAt(i);
                GeneralValueObject g1 = new GeneralValueObject();
                g1.setAtributo("codMunicipio",codMunicipio);
                g1.setAtributo("codProcedimiento",codProcedimiento);
                g1.setAtributo("ejercicio",ejercicio);
                g1.setAtributo("numero",numero);
                g1.setAtributo("usuario",usuario);
                g1.setAtributo("codOrganizacion",codOrganizacion);
                g1.setAtributo("codEntidad",codEntidad);
                g1.setAtributo("ocurrenciaTramite",g.getAtributo("ocurrenciaTramite"));
                g1.setAtributo("codTramite",g.getAtributo("codTramite"));

                int resultado = TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad,con,g1);

                String consultar = "no";
                if(resultado == 1) {
                    consultar = "si";
                }
                g.setAtributo("consultar",consultar);
                tramitesFinal.addElement(g);
            }

        } catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled())
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled())
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        return tramitesFinal;
    }

    
    
    
    
    
    
    public Vector cargaTramites(GeneralValueObject gVO, AdaptadorSQLBD oad, Connection con, boolean hora) throws TechnicalException {       
        PreparedStatement ps = null;
        ResultSet rs = null;

        Vector<GeneralValueObject> tramites = new Vector<GeneralValueObject>();
        Vector<GeneralValueObject> tramites1 = new Vector<GeneralValueObject>();
        Vector<GeneralValueObject> tramitesFinal = new Vector<GeneralValueObject>();

        try{            
            
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            String usuario = (String)gVO.getAtributo("usuario");
            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codEntidad = (String) gVO.getAtributo("codEntidad");

            
            /* ORIGINAL 
            String from = "CRO_OCU, TRA_COD, TML_VALOR, CRO_UTR"
                        + ", " + oad.convertir("CRO_FEI",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fInicio "
                        + ", " + oad.convertir("CRO_FEF",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFin "
                         + ", " + oad.convertir("CRO_FIP",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fIniPlazo "
                        + ", " + oad.convertir("CRO_FFP",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFinPlazo "
                        + ", CRO_USU, " +  "A_USU.USU_NOM AS usu_nom, CRO_USF, CML_VALOR, PRO_TRI, " +
                        oad.convertir("CRO_FLI",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fLimite, " +                    
                        "A_USU_BLQ.USU_NOM AS usu_nom_blq, A_USU_BLQ.USU_COD AS usu_cod_blq, TRA_FIN, TRA_UND, TRA_PLZ,TRA_NOTIFICACION_ELECTRONICA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN , TRA_NOTIF_ELECT_OBLIG, TRA_NOTIF_FIRMA_CERT_ORG, " + 
                        "(SELECT COUNT(*) FROM TAREAS_PENDIENTES_INICIO WHERE NUM_EXPEDIENTE=CRO_NUM AND COD_MUNICIPIO=" + codMunicipio + 
                        " AND COD_TRAMITE=CRO_TRA AND OCU_TRAMITE=CRO_OCU) TAREAS";
            */
            //INCLUIMOS LA POSIBILIDAD DE DEVOLVER FECHA INICIO Y FIN CON HORA
            String from = "CRO_OCU, TRA_COD, TML_VALOR, CRO_UTR";
            if(!hora){
                from += ", " + oad.convertir("CRO_FEI",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fInicio "
                        + ", " + oad.convertir("CRO_FEF",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFin ";
            } else {
                from += ", " + oad.convertir("CRO_FEI",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH:MM:SS")+" AS fInicio "
                        + ", " + oad.convertir("CRO_FEF",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH:MM:SS")+" AS fFin ";
            }
            
            from += ", " + oad.convertir("CRO_FIP",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fIniPlazo "
                        + ", " + oad.convertir("CRO_FFP",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFinPlazo "
                        + ", CRO_USU, " +  "A_USU.USU_NOM AS usu_nom, CRO_USF, CML_VALOR, PRO_TRI, " +
                        oad.convertir("CRO_FLI",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fLimite, " +                    
                        "A_USU_BLQ.USU_NOM AS usu_nom_blq, A_USU_BLQ.USU_COD AS usu_cod_blq, TRA_FIN, TRA_UND, TRA_PLZ,TRA_NOTIFICACION_ELECTRONICA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN , TRA_NOTIF_ELECT_OBLIG, TRA_NOTIF_FIRMA_CERT_ORG, " + 
                        "(SELECT COUNT(*) FROM TAREAS_PENDIENTES_INICIO WHERE NUM_EXPEDIENTE=CRO_NUM AND COD_MUNICIPIO=" + codMunicipio + 
                        " AND COD_TRAMITE=CRO_TRA AND OCU_TRAMITE=CRO_OCU) TAREAS";
            
            ArrayList<String> join = new ArrayList<String>();
            
            if (esExpedienteHistorico(numero, con))
                join.add("hist_e_cro");
            else 
                join.add("e_cro");
            
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu");
            join.add("A_USU.USU_COD = CRO_USU");
            join.add("INNER");
            join.add("e_tra");
            join.add("TRA_MUN = CRO_MUN and TRA_PRO = CRO_PRO and TRA_COD = CRO_TRA");
            join.add("INNER");
            join.add("e_tml");
            join.add("TML_MUN = TRA_MUN and TML_PRO = TRA_PRO and TML_TRA = TRA_COD and TML_CMP = 'NOM' and TML_LENG = '"+idiomaDefecto+"'");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "a_cml a_cml");
            join.add("CML_COD = TRA_CLS and CML_CMP = 'NOM' and CML_LENG = '"+idiomaDefecto+"'");
            join.add("INNER");
            join.add("e_pro");
            join.add("TRA_MUN = PRO_MUN AND TRA_PRO = PRO_COD");
            join.add("LEFT");
            join.add("e_exp_bloq");
            join.add("CRO_MUN = BLQ_EXP_MUN and CRO_PRO = BLQ_EXP_PRO and CRO_EJE = BLQ_EXP_EJE and " +
                    "CRO_NUM = BLQ_EXP_NUM and CRO_TRA = BLQ_TRA_COD and CRO_OCU = BLQ_TRA_OCU");
            join.add("LEFT");
            join.add(GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu_blq");
            join.add("BLQ_USUARIO = A_USU_BLQ.USU_COD");

            join.add("false");

            String where = "CRO_MUN=? AND CRO_PRO=? " +
                           "AND CRO_EJE=? AND CRO_NUM=?";
 
            String order = " order by CRO_FEI desc";
            String sql = oad.join(from, where, join.toArray(new String[]{})) + order;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            int z=1;
            ps = con.prepareStatement(sql);
            ps.setInt(z++,Integer.parseInt(codMunicipio));
            ps.setString(z++,codProcedimiento);
            ps.setInt(z++,Integer.parseInt(ejercicio));
            ps.setString(z++,numero);
            
            rs = ps.executeQuery();                          
            while(rs.next()){
                GeneralValueObject tramiteVO = new GeneralValueObject();
                String codTramite = rs.getString("TRA_COD");
                tramiteVO.setAtributo("codTramite",codTramite);
                String ocurrenciaTramite = rs.getString("CRO_OCU");
                tramiteVO.setAtributo("ocurrenciaTramite", ocurrenciaTramite);
                tramiteVO.setAtributo("tramite",rs.getString("TML_VALOR"));
                String fechaInicio=rs.getString("fInicio");
                tramiteVO.setAtributo("fehcaInicio",fechaInicio);
                String fechaFin = rs.getString("fFin");
                tramiteVO.setAtributo("fechaFin",fechaFin);
                
                tramiteVO.setAtributo("codUsuario",rs.getString("CRO_USU"));
                tramiteVO.setAtributo("usuario",rs.getString("USU_NOM"));
                tramiteVO.setAtributo("codUsuarioFinalizacion",rs.getString("CRO_USF"));
                tramiteVO.setAtributo("clasificacion",rs.getString("CML_VALOR"));
                tramiteVO.setAtributo("codUniTramTramite",rs.getString("CRO_UTR"));
                String codTramiteInicio = rs.getString("PRO_TRI");
                if(codTramiteInicio != null && codTramiteInicio.equals(codTramite)) {
                    tramiteVO.setAtributo("tramiteInicio","si");
                } else {
                    tramiteVO.setAtributo("tramiteInicio","no");
                }
                String fechaLimite = rs.getString("fLimite");
                String fechaFinPlazo = rs.getString("fFinPlazo");
                
                tramiteVO.setAtributo("notificacionObligatoria", Integer.valueOf(rs.getInt("TRA_NOTIF_ELECT_OBLIG")));
                tramiteVO.setAtributo("certificadoOrganismo", Integer.valueOf(rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG")));
                
                // Se calcula si el tramite esta fuera de plazo.
                m_Log.debug("LA FECHA LIMITE DE LA TRAMITACION ES: " + fechaLimite);
                if(fechaLimite != null && !"".equals(fechaLimite)) {
                    java.util.Date dateLimite = Fecha.obtenerDate(fechaLimite);
                    Calendar calFechaLimite = Calendar.getInstance();
                    calFechaLimite.setTime(dateLimite);
                    calFechaLimite.set(Calendar.HOUR_OF_DAY, 23);
                    calFechaLimite.set(Calendar.MINUTE, 59);
                    calFechaLimite.set(Calendar.SECOND, 59);
                    
                    Calendar fechaActual = Calendar.getInstance();
                    
                    if (fechaActual.after(calFechaLimite)) tramiteVO.setAtributo("fueraDePlazo","si");
                    else tramiteVO.setAtributo("fueraDePlazo","no");
                } else {
                    tramiteVO.setAtributo("fueraDePlazo","no");
                }
                tramiteVO.setAtributo("usuarioBloq", rs.getString("usu_nom_blq"));
                tramiteVO.setAtributo("codUsuarioBloq", rs.getString("usu_cod_blq"));
                tramiteVO.setAtributo("traNotificacionElectronica", rs.getString("TRA_NOTIFICACION_ELECTRONICA"));
                
                // Se calcula si el tramite esta cercano al fin de plazo.
                int plazoCercaFin = rs.getInt("TRA_FIN");
                String tipoPlazo = rs.getString("TRA_UND");
                int unidadesPlazo = rs.getInt("TRA_PLZ");
                
                if(fechaLimite != null && !"".equals(fechaLimite) && tipoPlazo!=null) {
                    java.util.Date dateLimite = Fecha.obtenerDate(fechaLimite);
                    Calendar calFechaLimite = Calendar.getInstance();
                    calFechaLimite.setTime(dateLimite);
                    calFechaLimite.set(Calendar.HOUR_OF_DAY, 23);
                    calFechaLimite.set(Calendar.MINUTE, 59);
                    calFechaLimite.set(Calendar.SECOND, 59);
                                        
                    String fuera = TramitacionDAO.getInstance().calculoCercaFinPlazo(plazoCercaFin, calFechaLimite, tipoPlazo, unidadesPlazo, con, oad);
                    tramiteVO.setAtributo("plazoCercaFin", fuera);
                } else {
                    tramiteVO.setAtributo("plazoCercaFin","no");
                }
                                               
                m_Log.debug("EL TRAMITE SE ENCUENTRA FUERA DE PLAZO: " + tramiteVO.getAtributo("fueraDePlazo"));
                m_Log.debug("EL TRAMITE SE ENCUENTRA CERCA DE FIN DE PLAZO: " + tramiteVO.getAtributo("plazoCercaFin"));

                /*** SE COMPRUEBA SI EL TRÁMITE TIENE TAREAS DE INICIO PENDIENTES DE EJECUTAR **/
                              
                 tramiteVO.setAtributo("tieneTareasPendientesInicio","NO");
                 if(rs.getInt("TAREAS")>=1){
                    tramiteVO.setAtributo("tieneTareasPendientesInicio","SI");                    
                 } 
                            
                
                String extPlugin = rs.getString("TRA_TRAM_EXT_PLUGIN");
                String urlExtPlugin = rs.getString("TRA_TRAM_EXT_URL");
                String implClass = rs.getString("TRA_TRAM_EXT_IMPLCLASS");
                String idEnlace  = rs.getString("TRA_TRAM_ID_ENLACE_PLUGIN");

                tramiteVO.setAtributo("extPlugin","");
                tramiteVO.setAtributo("extUrl","");
                tramiteVO.setAtributo("extImplClass","");
                tramiteVO.setAtributo("extIdEnlace","");
                tramiteVO.setAtributo("extBloqueoFinalizarTramite","NO");
                tramiteVO.setAtributo("extBloqueoRetrocesoTramite","NO");

                if(extPlugin!=null && urlExtPlugin!=null && implClass!=null && idEnlace!=null && !"".equals(extPlugin)
                        && !"".equals(urlExtPlugin) && !"".equals(implClass) && !"".equals(idEnlace)){
                    
                    TramitacionExternaBase plugin = TramitacionExternaCargador.getInstance().getPlugin(Integer.parseInt(codMunicipio),extPlugin,idEnlace,urlExtPlugin,implClass);
                    if(plugin!=null){
                        tramiteVO.setAtributo("extPlugin",extPlugin);
                        tramiteVO.setAtributo("extUrl",urlExtPlugin);
                        tramiteVO.setAtributo("extImplClass",implClass);
                        tramiteVO.setAtributo("extIdEnlace",implClass);
                        
                        if(plugin.isBloqueadoFinalizarTramite())
                            tramiteVO.setAtributo("extBloqueoFinalizarTramite","SI");
                        else
                            tramiteVO.setAtributo("extBloqueoFinalizarTramite","NO");
                                                
                        if(plugin.isBloqueadoRetrocesoTramite()){
                            tramiteVO.setAtributo("extBloqueoRetrocesoTramite","SI");
                        }else
                            tramiteVO.setAtributo("extBloqueoRetrocesoTramite","NO");
                        
                    }// if
                }// if
                                
                
                /****** SE OBTIENE EL NOMBRE DE LA UNIDAD TRAMITADORA DEL TRÁMITE **************/
                HashMap<String,String> CACHE_UORS = new HashMap<String, String>();
                String codUnidadTramitadoraTram = (String)tramiteVO.getAtributo("codUniTramTramite");
                if(codUnidadTramitadoraTram == null || "".equals(codUnidadTramitadoraTram)) {
                    tramiteVO.setAtributo("unidad","");
                } else {                    
                    String nombre = "";
                    tramiteVO.setAtributo("unidad","");
                    
                    if(CACHE_UORS.containsKey(codUnidadTramitadoraTram))
                        nombre = CACHE_UORS.get(codUnidadTramitadoraTram);
                    else { 
                        nombre = this.getNombreUnidadTramitadora(Integer.parseInt(codUnidadTramitadoraTram),con,oad);
                        CACHE_UORS.put(codUnidadTramitadoraTram,nombre);
                        tramiteVO.setAtributo("unidad",nombre);                        
                    }
                }// else
                /*********    FIN NOMBRE DE LA UNIDAD ORGANIZATIVA ***************/
                
                                
                
                /******* SE COMPRUEBA SI EL USUARIO TRAMITADOR TIENE PERMISO SOBRE LA UNIDAD TRAMITADORA DEL TRÁMITE *****/    
               /* GeneralValueObject g1 = new GeneralValueObject();
                g1.setAtributo("codMunicipio",codMunicipio);
                g1.setAtributo("codProcedimiento",codProcedimiento);
                g1.setAtributo("ejercicio",ejercicio);
                g1.setAtributo("numero",numero);
                g1.setAtributo("usuario",usuario);
                g1.setAtributo("codOrganizacion",codOrganizacion);
                g1.setAtributo("codEntidad",codEntidad);
                g1.setAtributo("ocurrenciaTramite",tramiteVO.getAtributo("ocurrenciaTramite"));
                g1.setAtributo("codTramite",tramiteVO.getAtributo("codTramite"));
                g1.setAtributo("expHistorico",expHistorico);

                String consultar = "no";
                int resultado = TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad,con,g1);
                if(resultado == 1) {
                    consultar = "si";
                }
                tramiteVO.setAtributo("consultar",consultar);  */              
                tramites.add(tramiteVO);
            }
            
            //SigpGeneralOperations.closeResultSet(rs);
            //SigpGeneralOperations.closeStatement(ps);
            
            /** ORIGINAL
            for(int j=0;j<tramites.size();j++) {
                GeneralValueObject g = tramites.elementAt(j);
                String codUnidadTramitadoraTram = (String) g.getAtributo("codUniTramTramite");
                if(codUnidadTramitadoraTram == null || "".equals(codUnidadTramitadoraTram)) {
                    g.setAtributo("unidad","");
                } else {
                                       
                    sql = "SELECT UOR_NOM FROM A_UOR WHERE UOR_COD=?";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps = con.prepareStatement(sql);
                    ps.setInt(1,Integer.parseInt(codUnidadTramitadoraTram));
                    rs = ps.executeQuery();
                    g.setAtributo("unidad", "");
                    while(rs.next()){
                        g.setAtributo("unidad",rs.getString(uor_nom));
                    }
                }
                tramites1.addElement(g);
            }
            **/
            
            //SigpGeneralOperations.closeResultSet(rs);
            //SigpGeneralOperations.closeStatement(ps);

            /** original
            for(int i=0;i<tramites.size();i++) {                
                GeneralValueObject g = tramites.elementAt(i);
                GeneralValueObject g1 = new GeneralValueObject();
                g1.setAtributo("codMunicipio",codMunicipio);
                g1.setAtributo("codProcedimiento",codProcedimiento);
                g1.setAtributo("ejercicio",ejercicio);
                g1.setAtributo("numero",numero);
                g1.setAtributo("usuario",usuario);
                g1.setAtributo("codOrganizacion",codOrganizacion);
                g1.setAtributo("codEntidad",codEntidad);
                g1.setAtributo("ocurrenciaTramite",g.getAtributo("ocurrenciaTramite"));
                g1.setAtributo("codTramite",g.getAtributo("codTramite"));
                g1.setAtributo("expHistorico",expHistorico);

                int resultado = TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad,con,g1);

                String consultar = "no";
                if(resultado == 1) {
                    consultar = "si";
                }
                g.setAtributo("consultar",consultar);
                tramitesFinal.addElement(g);
            }
            */

        } catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled())
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
            
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled())
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
            
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return tramites;
        //return tramitesFinal;
    }
    
    
    public String verificarPermisosTramite(GeneralValueObject gVO, AdaptadorSQLBD oad, Connection con) throws TechnicalException {

        m_Log.debug("****FichaExpedienteDAO verificarPermisosTRamite ");
        String retorno="NO";

        try {
            //consulta permisos trámites
           
            int resultado = TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO);
            if (resultado == 1) {
             
                m_Log.debug("resultado consulta permisos unidades: "+resultado);
                 
            //consulta permisos trámites por cargos
                //Si tiene permiso devuelve true
                
                String permisoCargo="no";
                permisoCargo=permisoPorTramiteYCargo( gVO, con);
                m_Log.debug("resultado consulta permisos cargos: "+permisoCargo);
                
                if((permisoCargo!=null)&&("si".equals(permisoCargo)))
                {
                    retorno= "SI";
                }

            }  

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
            }

        } finally {
            //SigpGeneralOperations.closeResultSet(rs);
            //SigpGeneralOperations.closeStatement(ps);
        }
        
        
        
        return retorno;
         

    }

    
    /**
     * Recupera el nombre de una unidad tramitadora
     * @param codUor: Código de la uor
     * @param con: Conexión a la BBDD
     * @return  String con el nombre de la unidad
     */
    private String getNombreUnidadTramitadora(int codUor,Connection con,AdaptadorSQLBD oad) {         
        String nombre = null;
        
        UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],String.valueOf(codUor));

        if (uorDTO!=null){
            nombre = uorDTO.getUor_nom();
        } 
        return nombre;        
    }
    
    
    
    
    public String calculoCercaFinPlazo(int plazoCercaFin, String fechaInicioPlazo, String fechaLimite) {
        String fuera = "";
        //calculo el dia de inicio de plazo
        int diaIni = 0;
        int horas;
        int diaP = 0;
        String[] dia = null;
        
        if (fechaInicioPlazo == null) return "no";
        
        dia = fechaInicioPlazo.split("/");
        diaIni = Integer.parseInt(dia[0]);
        m_Log.debug(" ********   diaIni : " + diaIni);
        //paso las fechas a calendar para trabajar.
        java.util.Date dateLimite = (Date) Fecha.obtenerDate(fechaLimite);
        m_Log.debug(" ********   dateLimite : " + dateLimite);
        java.util.Date dateInicioPlazo = (Date) Fecha.obtenerDate(fechaInicioPlazo);
        m_Log.debug(" ********   dateiNICIO : " + dateInicioPlazo);

        long time = dateLimite.getTime() - dateInicioPlazo.getTime();
        //Muestro el resultado en días
        m_Log.debug(" ********   dias de diferencia : " + time / (3600 * 24 * 1000));

        int d = (int) (time / (3600 * 24 * 1000));
        m_Log.debug("- dia ENTRE INICIO Y FIN : " + d);

        //CALCULO DE CUANTAS HORAS DEBEN PASAR PARA QUE LA APLICACION EMPIECE A AVISAR:
        //la formula seria: (24 horas * dia de inicio de plazo )/ 100 *Porcentaje de aviso(plazoCercaFin)= 
        //numero de horas que tienen que pasar para avisar

        horas = ((24 * d * plazoCercaFin) / 100);
        m_Log.debug(" ********   horas : " + horas);
        //calculo diap=cuantos dias antes tiene que empezar avisar
        diaP = horas / 24;
        m_Log.debug(" ********   cuantos dias antes tiene que empezar avisar : " + diaP);
        //si diap es = tengo que avisar el mismo dia que finaliza

        //calculo la fecha para ello covierto a calendar el string

        Calendar calendario = Calendar.getInstance();        
        java.util.Date date = calendario.getTime();
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(dateLimite.getTime());
        cal.add(Calendar.DATE, -diaP);
                
        // Obtengo la fecha de fin de plazo
        Calendar calFinPlazo = Calendar.getInstance();
        calFinPlazo.setTime(dateLimite);        
        
        if (calendario.after(cal) && calendario.before(calFinPlazo)) fuera = "si";
        else fuera = "no";
        
        // VALORES DE RETORNO POSIBLES.
        // fuera = "si" --> hay que avisar que el trámite está cercano a la finalización del plazo.
        // fuera = "no" --> no hay que avisar que el trámite está cercano a la finalización del plazo.        

        m_Log.debug(" ********   fuera : " + fuera);
        return fuera;
    }
    
    public Vector cargaTramites(GeneralValueObject gVO, AdaptadorSQL oad, Connection con) throws TechnicalException {
      PreparedStatement st = null;
      ResultSet rs = null;

      Vector tramites = new Vector();
      Vector tramites1 = new Vector();
      Vector tramitesFinal = new Vector();

      try{

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");
      String usuario = (String)gVO.getAtributo("usuario");
      String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
      String codEntidad = (String) gVO.getAtributo("codEntidad");

      String sql = "select " + cro_ocu + "," + tra_cod + "," + tml_valor + "," + cro_utr
      + ", " + oad.convertir(cro_fei,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fInicio "
      + ", " + oad.convertir(cro_fef,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFin "
      + ", " + oad.convertir(cro_ffp,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fFinPlazo "
      + ", " + cro_usu + "," +  usu_nom + "," + cro_usf + ", " + cml_valor + "," + pro_tri + "," +
      oad.convertir(cro_fli,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS fLimite " +
      " from e_cro, e_tra, e_tml, " + GlobalNames.ESQUEMA_GENERICO + "a_cml, "+ GlobalNames.ESQUEMA_GENERICO +
      "a_usu, e_pro " + " where " + cro_mun + " = " + codMunicipio + " and " + cro_pro + " = '" + codProcedimiento +
      "' and " + cro_eje + " = " + ejercicio + " and " + cro_num + " = '" + numero + "' and " +
      tra_mun + " = " + cro_mun + " and " + tra_pro + " = " + cro_pro + " and " + tra_cod + " = " + cro_tra + " and " +
      usu_cod + " = " + cro_usu + " and " +
      tml_mun + " = " + tra_mun + " and " + tml_pro + " = " + tra_pro + " and " + tml_tra + " = " + tra_cod + " and " + tml_cmp + " = 'NOM' and " + tml_leng + " = '"+idiomaDefecto+"' and " +
      cml_cod + " = " + tra_cls + " and " + cml_cmp + " = 'NOM' and " + cml_leng + " = '"+idiomaDefecto+"'" +
      " AND " + tra_mun + "=" + pro_mun + " AND " + tra_pro + "=" + pro_cod +
      " order by " + cro_fei + " desc";

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      st = con.prepareStatement(sql);
      rs = st.executeQuery();
      while(rs.next()){
        GeneralValueObject tramiteVO = new GeneralValueObject();
        String codTramite = rs.getString(tra_cod);
        tramiteVO.setAtributo("codTramite",codTramite);
        tramiteVO.setAtributo("ocurrenciaTramite", rs.getString(cro_ocu));
        tramiteVO.setAtributo("tramite",rs.getString(tml_valor));
        tramiteVO.setAtributo("fehcaInicio",rs.getString("fInicio"));
        String fechaFin = rs.getString("fFin");
        tramiteVO.setAtributo("fechaFin",fechaFin);
        tramiteVO.setAtributo("codUsuario",rs.getString(cro_usu));
        tramiteVO.setAtributo("usuario",rs.getString(usu_nom));
        tramiteVO.setAtributo("codUsuarioFinalizacion",rs.getString(cro_usf));
        tramiteVO.setAtributo("clasificacion",rs.getString(cml_valor));
        tramiteVO.setAtributo("codUniTramTramite",rs.getString(cro_utr));
        String codTramiteInicio = rs.getString(pro_tri);
        if(codTramiteInicio != null && codTramiteInicio.equals(codTramite)) {
            tramiteVO.setAtributo("tramiteInicio","si");
        } else {
          tramiteVO.setAtributo("tramiteInicio","no");
        }
        String fechaLimite = rs.getString("fLimite");
        String fechaFinPlazo = rs.getString("fFinPlazo");
        if(fechaFin ==null && fechaLimite != null && !"".equals(fechaLimite))
        {
          if (fechaFinPlazo == null || "".equals(fechaFinPlazo))
          {
              Calendar calendario = Calendar.getInstance();
              java.util.Date date = calendario.getTime();
              Fecha fecha = new Fecha();
              java.util.Date dateLimite = Fecha.obtenerDate(fechaLimite);
              if(date.compareTo(dateLimite) >=0) {
                tramiteVO.setAtributo("fueraDePlazo","si");
              } else {
                tramiteVO.setAtributo("fueraDePlazo","no");
              }
          }
          else{tramiteVO.setAtributo("fueraDePlazo","no");}

        } else {
          tramiteVO.setAtributo("fueraDePlazo","no");
        }
        tramites.add(tramiteVO);
      }
      SigpGeneralOperations.closeResultSet(rs);
      SigpGeneralOperations.closeStatement(st);
      
      for(int j=0;j<tramites.size();j++) {
        GeneralValueObject g = new GeneralValueObject();
        g = (GeneralValueObject) tramites.elementAt(j);
        String codUnidadTramitadoraTram = (String) g.getAtributo("codUniTramTramite");
        if(codUnidadTramitadoraTram == null || "".equals(codUnidadTramitadoraTram)) {
          g.setAtributo("unidad","");
        } else {
            UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],codUnidadTramitadoraTram);

            if (uorDTO!=null){
                g.setAtributo("unidad",uorDTO.getUor_nom());
            } 
        }
        tramites1.addElement(g);
      }
      SigpGeneralOperations.closeResultSet(rs);
      SigpGeneralOperations.closeStatement(st);

      for(int i=0;i<tramites1.size();i++) {
        GeneralValueObject g = new GeneralValueObject();
        g = (GeneralValueObject) tramites1.elementAt(i);
        GeneralValueObject g1 = new GeneralValueObject();
        g1.setAtributo("codMunicipio",codMunicipio);
        g1.setAtributo("codProcedimiento",codProcedimiento);
        g1.setAtributo("ejercicio",ejercicio);
        g1.setAtributo("numero",numero);
        g1.setAtributo("usuario",usuario);
        g1.setAtributo("codOrganizacion",codOrganizacion);
        g1.setAtributo("codEntidad",codEntidad);
        g1.setAtributo("ocurrenciaTramite",g.getAtributo("ocurrenciaTramite"));
        g1.setAtributo("codTramite",g.getAtributo("codTramite"));

        int resultado = TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad,con,g1);

        String consultar = "no";
        if(resultado == 1) {
          consultar = "si";
        }
        g.setAtributo("consultar",consultar);
        tramitesFinal.addElement(g);
      }

    }catch (SQLException e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
    }finally{
        SigpGeneralOperations.devolverConexion(null, con);
        return tramitesFinal;
    }
    }

       
    
    public Vector cargaPermisosTramites(GeneralValueObject gVO,Connection con) throws TechnicalException {                
        Statement st = null;
        ResultSet rs = null;

        Vector permisosTramites = new Vector();

        try{            
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");

            String sql = "select " + tra_car + ", " + cro_utr + " from e_cro, e_tra" +
                    " where " + cro_mun + " = " + codMunicipio + " and " + cro_pro + " = '" + codProcedimiento + "' and " +
                    cro_eje + " = " + ejercicio + " and " + cro_num + " = '" + numero + "' and " +
                    tra_mun + " = " + cro_mun + " and " + tra_pro + " = " + cro_pro + " and " +
                    tra_cod + " = " + cro_tra + " order by " + cro_fei + " desc";
            if(m_Log.isDebugEnabled()) m_Log.debug("cargaPermisosTramites: " + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                String tramitar = "si";
                if(m_Log.isDebugEnabled()) m_Log.debug("-*-*-*-*-* CARGO DEL TRÁMITE : " + rs.getString(tra_car));
                if (rs.getString(tra_car) != null) {
                    if (!rs.getString(tra_car).equals("") && !rs.getString(tra_car).equals("0")) {    
                        tramitar = TramitesExpedienteDAO.getInstance().permisoTramitacionUsuario(gVO, rs.getString(tra_car), rs.getString(cro_utr),con);
                    }
                }
                permisosTramites.add(tramitar);
            }

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);            
            return permisosTramites;
        }
    } 
    
    
    private String permisoPorTramiteYCargo(GeneralValueObject gVO,Connection con) throws TechnicalException {                
        Statement st = null;
        ResultSet rs = null;
         String tramitar = "no";
       

        try{            
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            String codTramite = (String)gVO.getAtributo("codTramite");

           
            
            String sql = "select " + tra_car + ", " + cro_utr + " from e_cro, e_tra" +
                    " where " + cro_mun + " = " + codMunicipio + " and " + cro_pro + " = '" + codProcedimiento + "' and " +
                    cro_eje + " = " + ejercicio + " and " + cro_num + " = '" + numero + "' and " + cro_tra+" = '" + codTramite + "' and " +
                    tra_mun + " = " + cro_mun + " and " + tra_pro + " = " + cro_pro + " and " +
                    tra_cod + " = " + cro_tra + " order by " + cro_fei + " desc";
            if(m_Log.isDebugEnabled()) m_Log.debug("cargaPermisosTramites: " + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                tramitar = "si";
                if(m_Log.isDebugEnabled()) m_Log.debug("-*-*-*-*-* CARGO DEL TRÁMITE : " + rs.getString(tra_car));
                if (rs.getString(tra_car) != null) {
                    if (!rs.getString(tra_car).equals("") && !rs.getString(tra_car).equals("0")) {    
                        tramitar = TramitesExpedienteDAO.getInstance().permisoTramitacionUsuario(gVO, rs.getString(tra_car), rs.getString(cro_utr),con);
                    }
                }
               
            }

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);            
            return tramitar;
        }
    } 
    

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExpediente(GeneralValueObject gVO, String idServicio, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        ArrayList<AsientoFichaExpedienteVO> listaAsientos = new ArrayList<AsientoFichaExpedienteVO>();

        boolean LLAMADA_DESDE_WSTRAMITACION = false;
        String NOMBRE_ESQUEMA_GENERICO = "";
        try{

            oad = new AdaptadorSQLBD(params);
                       
            String numero = (String)gVO.getAtributo("numero");
            Connection con_aux = (Connection)gVO.getAtributo("CONEXION_BBDD");
            String generico_aux = (String)gVO.getAtributo("ESQUEMA_GENERICO");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");

            m_Log.debug("*************** cargaListaAsientosExpediente conexion:: " + con_aux);
            m_Log.debug("*************** cargaListaAsientosExpediente generico:: " + generico_aux);
            m_Log.debug("*************** cargaListaAsientosExpediente numero:: " + numero);
            
            if(con_aux!=null && generico_aux!=null){
                con = con_aux;
                LLAMADA_DESDE_WSTRAMITACION = true;                
                NOMBRE_ESQUEMA_GENERICO = generico_aux;
            }else{
                NOMBRE_ESQUEMA_GENERICO = GlobalNames.ESQUEMA_GENERICO;
                con = oad.getConnection();
            }
            
            st = con.createStatement();

            String parte_select = "SELECT RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM, RES_OBS, " +
                    oad.convertir("RES_FEC",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS FECHA_ANOTACION, " +
                    "RES_ASU, RES_UOD, HTE_NOM, " +
                    oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1","' '","HTE_AP1"}) + " AS APELLIDO1, " +
                    oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2","' '","HTE_AP2"}) + " AS APELLIDO2, (SELECT COUNT (*) FROM  R_EXT WHERE res_uor=ext_uor AND res_dep=ext_dep AND res_tip=ext_tip AND res_eje=ext_eje AND res_num=ext_num )AS num_terceros";
            /* original
            String parteFrom1 = " FROM R_RES, E_EXR, T_HTE, T_DNN, " + GlobalNames.ESQUEMA_GENERICO + "T_MUN"; // Domicilio no normalizado
            String parteFrom2 = " FROM R_RES, E_EXR, T_HTE, T_DPO, T_DSU," + GlobalNames.ESQUEMA_GENERICO + "T_MUN"; // Domicilio normalizado            
            */
            String parteFrom1; // Domicilio no normalizado
            String parteFrom2; // Domicilio normalizado            

            if (esExpedienteHistorico(numero, con)) {
                parteFrom1 = " FROM R_RES, HIST_E_EXR, T_HTE, T_DNN, " + NOMBRE_ESQUEMA_GENERICO + "T_MUN"; 
                parteFrom2 = " FROM R_RES, HIST_E_EXR, T_HTE, T_DPO, T_DSU," + NOMBRE_ESQUEMA_GENERICO + "T_MUN";  
            } else {
                parteFrom1 = " FROM R_RES, E_EXR, T_HTE, T_DNN, " + NOMBRE_ESQUEMA_GENERICO + "T_MUN"; 
                parteFrom2 = " FROM R_RES, E_EXR, T_HTE, T_DPO, T_DSU," + NOMBRE_ESQUEMA_GENERICO + "T_MUN";  
            }
            String pWhere = " WHERE RES_DEP = EXR_DEP AND RES_UOR = EXR_UOR AND RES_TIP = EXR_TIP " +
                    "AND RES_EJE = EXR_EJR AND RES_NUM = EXR_NRE AND RES_TER = HTE_TER"; // Datos listado.

            // Domicilio no normalizado
            String pWhere1 = pWhere + " AND RES_DOM = DNN_DOM AND RES_TNV = HTE_NVR AND DNN_PAI = MUN_PAI " +
                    "AND DNN_PRV = MUN_PRV AND DNN_MUN = MUN_COD";

            // Domicilio normalizado
            String pWhere2 = pWhere + " AND RES_DOM = DPO_DOM AND RES_TNV = HTE_NVR AND DPO_DSU = DSU_COD " +
                    "AND DSU_PAI = MUN_PAI AND DSU_PRV = MUN_PRV AND DSU_MUN = MUN_COD";

            String p2Where = " AND EXR_NUM LIKE '" + numero + "'";

            String sql = parte_select + parteFrom1 + pWhere1 + p2Where + " UNION " + parte_select + parteFrom2 + pWhere2 + p2Where;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            
            while(rs.next()){

                AsientoFichaExpedienteVO asiento = new AsientoFichaExpedienteVO();                
                asiento.setCodigoDepartamento(rs.getInt("RES_DEP"));
                asiento.setCodigoUOR(rs.getInt("RES_UOR"));
                asiento.setTipoAsiento(rs.getString("RES_TIP"));
                asiento.setEjercicioAsiento(rs.getInt("RES_EJE"));
                asiento.setNumeroAsiento(rs.getLong("RES_NUM"));
                asiento.setFechaAsiento(rs.getString("FECHA_ANOTACION"));
                asiento.setAsuntoAsiento(AdaptadorSQLBD.js_escape(rs.getString("RES_ASU")));
                String observaciones = rs.getString("RES_OBS");
                if (observaciones == null || "".equals(observaciones.trim())) asiento.setObservaciones(false);
                else asiento.setObservaciones(true);
                asiento.setCodigoUOD(rs.getInt("RES_UOD"));
                asiento.setNombreInteresado(rs.getString("HTE_NOM"));
                asiento.setApellido1Interesado(rs.getString("APELLIDO1"));
                asiento.setApellido2Interesado(rs.getString("APELLIDO2"));
                int numeroTerceros = rs.getInt("num_terceros");
                if (numeroTerceros > 1) asiento.setMasInteresados(true);
                else asiento.setMasInteresados(false);
                asiento.setOrigenAsiento(idServicio);
                listaAsientos.add(asiento);
            }

        } catch (Exception e) {
            listaAsientos = new ArrayList<AsientoFichaExpedienteVO>();
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);        
            if(!LLAMADA_DESDE_WSTRAMITACION) SigpGeneralOperations.devolverConexion(oad, con);
        }
        return listaAsientos;
    }

    public Vector cargaListaDocumentosExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        Vector documentos = new Vector();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            // Si estamos iniciando un expediente desde un asiento, hemos de comprobar si ya hay
            // algun documento aportado y en ese caso marcarlo como aportado.
            boolean desdeAsiento = false;
            if (gVO.getAtributo("desdeAsiento") != null && !gVO.getAtributo("desdeAsiento").equals(""))
                desdeAsiento = true;
            
            // Cargamos los documentos del asiento en un HashMap, si tienen tipo es que ya estan aportados.
            HashMap<String,String> docsAsiento = new HashMap<String,String>();
            if (desdeAsiento) {
                if(m_Log.isDebugEnabled()) m_Log.debug("Leemos los documentos del asiento.");
                st = con.createStatement();
                String sql = "SELECT " + sql_docsNombreDoc + "," + sql_docsTipoDoc +
                            " FROM R_RED" +
                            " WHERE " + sql_docsDepto + "=" + gVO.getAtributo("codDepartamento") +
                            " AND " + sql_docsUnid + "=" + gVO.getAtributo("codUnidadRegistro") +
                            " AND " + sql_docsEjerc + "=" + gVO.getAtributo("ejercicioAsiento") +
                            " AND " + sql_docsNum + "=" + gVO.getAtributo("numeroAsiento") +
                            " AND " + sql_docsTipo + "='" + gVO.getAtributo("tipoAsiento") + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = st.executeQuery(sql);
                
                while (rs.next()) {
                    docsAsiento.put(rs.getString(sql_docsNombreDoc), rs.getString(sql_docsTipoDoc));
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("Documentos del asiento: " + docsAsiento);
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
            }
                                 
            
            
            st = con.createStatement();
            String from = sql_dop_codDoc +", dpml1."+sql_dpml_valor+" AS nombreDocumento,dpml2."+sql_dpml_valor+" AS condicion "
                    + ", " + oad.convertir(sql_doe_fEntreg, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fEntrega ";
            ArrayList join = new ArrayList();
            join.add("E_DOP");
            join.add("INNER");
            join.add("e_dpml dpml1");
            join.add(sql_dop_codMun +"="+codMunicipio
                     + " AND "+ sql_dop_codPro+"='"+codProcedimiento+"' "
                     + " AND e_dop."+sql_dop_codMun+"=dpml1."+sql_dpml_codMun
                     + " AND e_dop."+sql_dop_codPro+"=dpml1."+sql_dpml_codProc
                     + " AND e_dop."+sql_dop_codDoc+"=dpml1."+sql_dpml_codDoc);
            join.add("INNER");
            join.add("e_dpml dpml2");
            join.add("e_dop."+sql_dop_codMun+"=dpml2."+sql_dpml_codMun
                     + " AND e_dop."+sql_dop_codPro+"=dpml2."+sql_dpml_codProc
                     + " AND e_dop."+sql_dop_codDoc+"=dpml2."+sql_dpml_codDoc);
            join.add("LEFT");
            join.add("(SELECT * FROM E_DOE WHERE " + sql_doe_ejer + " ='" + ejercicio + "' AND " + sql_doe_num + " ='" + numero + "') docs");
            join.add( "e_dop." + sql_dop_codDoc + " = docs." + sql_doe_codDoc 
            		+ " AND  e_dop." + sql_dop_codPro + " =docs." +  sql_doe_codProc
            		+ " AND  e_dop." + sql_dop_codMun + " = docs." +  sql_doe_codMun);
            join.add("false");
            
            String where = "dpml1."+sql_dpml_campo+"='NOM'" + " AND " +
            		"dpml1."+sql_dpml_idioma+"='"+idiomaDefecto+"'" + " AND " +
            		"dpml2."+sql_dpml_campo+"='CON'" + " AND " +
            		"dpml2."+sql_dpml_idioma+"='"+idiomaDefecto+"'";

            String sql = oad.join(from, where, (String[]) join.toArray(new String[]{}));
            sql += " ORDER BY DOP_COD ";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);

            String nombreDoc = null;
            String tipoDoc = null;
            while(rs.next()){
                gVO = new GeneralValueObject();
                String codDocumento = rs.getString(sql_dop_codDoc);
                gVO.setAtributo("codigo",codDocumento);
                nombreDoc = rs.getString("nombreDocumento");
                gVO.setAtributo("nombre",nombreDoc);
                gVO.setAtributo("condicion",rs.getString("condicion"));
                String fEntrega = rs.getString("fEntrega");
                if (fEntrega == null || "".equals(fEntrega)) {
                    gVO.setAtributo("ENTREGADO", "NO");
                    // Comprobamos si viene en el asiento
                    tipoDoc = docsAsiento.get(nombreDoc);
                    if (tipoDoc!=null && !tipoDoc.equals("")) gVO.setAtributo("ENTREGADO", "SI");
                    
                } else gVO.setAtributo("ENTREGADO", "SI");
                gVO.setAtributo("FEntrega", fEntrega);
               
                GeneralValueObject adjunto = this.cargarDatosAdjuntoDocumentoExpediente(Integer.parseInt(codMunicipio),Integer.parseInt(ejercicio), numero,
                        codProcedimiento,Integer.parseInt(codDocumento),con);

                // Se guarda en gVO los datos del adjunto del documento de expediente si lo tuviese
                if(adjunto!=null){
                    gVO.setAtributo("codDocumentoAdjuntoExpediente",adjunto.getAtributo("codDocumentoAdjuntoExpediente"));
                    gVO.setAtributo("tipoMimeAdjuntoDocumentoExpediente",adjunto.getAtributo("tipoMimeAdjuntoDocumentoExpediente"));
                    gVO.setAtributo("nombreAdjuntoDocumentoExpediente",adjunto.getAtributo("nombreAdjuntoDocumentoExpediente"));
                    gVO.setAtributo("fechaAltaAdjuntoDocumentoExpediente",adjunto.getAtributo("fechaAltaAdjuntoDocumentoExpediente"));
                    gVO.setAtributo("estadoFirma", adjunto.getAtributo("estadoFirma"));
                    gVO.setAtributo("numeroFirmas",adjunto.getAtributo("numeroFirmas"));
                }else{
                    gVO.setAtributo("codDocumentoAdjuntoExpediente","");
                    gVO.setAtributo("tipoMimeAdjuntoDocumentoExpediente","");
                    gVO.setAtributo("nombreAdjuntoDocumentoExpediente","");
                    gVO.setAtributo("fechaAltaAdjuntoDocumentoExpediente","");
                    gVO.setAtributo("estadoFirma", "");
                    gVO.setAtributo("numeroFirmas",0);
                }

                documentos.addElement(gVO);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        }catch (Exception e){
            documentos = new Vector(); 
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return documentos;
    }

    
    
    
     public Vector cargaListaDocumentosExpediente(GeneralValueObject gVO,AdaptadorSQLBD oad,Connection con) throws TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        Vector documentos = new Vector();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");

        try{
            
            // Si estamos iniciando un expediente desde un asiento, hemos de comprobar si ya hay
            // algun documento aportado y en ese caso marcarlo como aportado.
            boolean desdeAsiento = false;
            if (gVO.getAtributo("desdeAsiento") != null && !gVO.getAtributo("desdeAsiento").equals(""))
                desdeAsiento = true;
            
            // Cargamos los documentos del asiento en un HashMap, si tienen tipo es que ya estan aportados.
            HashMap<String,String> docsAsiento = new HashMap<String,String>();
            if (desdeAsiento) {
                if(m_Log.isDebugEnabled()) m_Log.debug("Leemos los documentos del asiento.");
                          
                String sql = "SELECT " + sql_docsNombreDoc + "," + sql_docsTipoDoc +
                            " FROM R_RED" +
                            " WHERE " + sql_docsDepto + "=?" + 
                            " AND " + sql_docsUnid + "=?" +  
                            " AND " + sql_docsEjerc + "=?" +  
                            " AND " + sql_docsNum + "=?" + 
                            " AND " + sql_docsTipo + "=?";
                
                ps = con.prepareStatement(sql);
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                
                int i=1;
                ps.setInt(i++,Integer.parseInt((String)gVO.getAtributo("codDepartamento")));
                ps.setInt(i++,Integer.parseInt((String)gVO.getAtributo("codUnidadRegistro")));
                ps.setInt(i++,Integer.parseInt((String)gVO.getAtributo("ejercicioAsiento")));
                ps.setString(i++,(String)gVO.getAtributo("numeroAsiento"));
                ps.setString(i++,(String)gVO.getAtributo("tipoAsiento"));
                
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    docsAsiento.put(rs.getString(sql_docsNombreDoc), rs.getString(sql_docsTipoDoc));
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("Documentos del asiento: " + docsAsiento);
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(ps);
            }
                                 
            
            String from = sql_dop_codDoc +", dpml1."+sql_dpml_valor+" AS nombreDocumento,dpml2."+sql_dpml_valor+" AS condicion "
                    + ", " + oad.convertir(sql_doe_fEntreg, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fEntrega ";
            ArrayList join = new ArrayList();
            join.add("E_DOP");
            join.add("INNER");
            join.add("e_dpml dpml1");
            join.add(sql_dop_codMun +"="+codMunicipio
                     + " AND "+ sql_dop_codPro+"='"+codProcedimiento+"' "
                     + " AND e_dop."+sql_dop_codMun+"=dpml1."+sql_dpml_codMun
                     + " AND e_dop."+sql_dop_codPro+"=dpml1."+sql_dpml_codProc
                     + " AND e_dop."+sql_dop_codDoc+"=dpml1."+sql_dpml_codDoc);
            join.add("INNER");
            join.add("e_dpml dpml2");
            join.add("e_dop."+sql_dop_codMun+"=dpml2."+sql_dpml_codMun
                     + " AND e_dop."+sql_dop_codPro+"=dpml2."+sql_dpml_codProc
                     + " AND e_dop."+sql_dop_codDoc+"=dpml2."+sql_dpml_codDoc);
            join.add("LEFT");
            join.add("(SELECT * FROM E_DOE WHERE " + sql_doe_ejer + " ='" + ejercicio + "' AND " + sql_doe_num + " ='" + numero + "') docs");
            join.add( "e_dop." + sql_dop_codDoc + " = docs." + sql_doe_codDoc 
            		+ " AND  e_dop." + sql_dop_codPro + " =docs." +  sql_doe_codProc
            		+ " AND  e_dop." + sql_dop_codMun + " = docs." +  sql_doe_codMun);
            join.add("false");
            
            String where = "dpml1."+sql_dpml_campo+"='NOM'" + " AND " +
            		"dpml1."+sql_dpml_idioma+"='"+idiomaDefecto+"'" + " AND " +
            		"dpml2."+sql_dpml_campo+"='CON'" + " AND " +
            		"dpml2."+sql_dpml_idioma+"='"+idiomaDefecto+"'";

            String sql = oad.join(from, where, (String[]) join.toArray(new String[]{}));
            sql += " ORDER BY DOP_COD ";            
            
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            String nombreDoc = null;
            String tipoDoc = null;
            while(rs.next()){
                gVO = new GeneralValueObject();
                String codDocumento = rs.getString(sql_dop_codDoc);
                gVO.setAtributo("codigo",codDocumento);
                nombreDoc = rs.getString("nombreDocumento");
                gVO.setAtributo("nombre",nombreDoc);
                gVO.setAtributo("condicion",rs.getString("condicion"));
                String fEntrega = rs.getString("fEntrega");
                if (fEntrega == null || "".equals(fEntrega)) {
                    gVO.setAtributo("ENTREGADO", "NO");
                    // Comprobamos si viene en el asiento
                    tipoDoc = docsAsiento.get(nombreDoc);
                    if (tipoDoc!=null && !tipoDoc.equals("")) gVO.setAtributo("ENTREGADO", "SI");
                    
                } else gVO.setAtributo("ENTREGADO", "SI");
                gVO.setAtributo("FEntrega", fEntrega);
               
                GeneralValueObject adjunto = this.cargarDatosAdjuntoDocumentoExpediente(Integer.parseInt(codMunicipio),Integer.parseInt(ejercicio), numero,
                        codProcedimiento,Integer.parseInt(codDocumento),con);

                // Se guarda en gVO los datos del adjunto del documento de expediente si lo tuviese
                if(adjunto!=null){
                    gVO.setAtributo("codDocumentoAdjuntoExpediente",adjunto.getAtributo("codDocumentoAdjuntoExpediente"));
                    gVO.setAtributo("tipoMimeAdjuntoDocumentoExpediente",adjunto.getAtributo("tipoMimeAdjuntoDocumentoExpediente"));
                    gVO.setAtributo("nombreAdjuntoDocumentoExpediente",adjunto.getAtributo("nombreAdjuntoDocumentoExpediente"));
                    gVO.setAtributo("fechaAltaAdjuntoDocumentoExpediente",adjunto.getAtributo("fechaAltaAdjuntoDocumentoExpediente"));
                    gVO.setAtributo("estadoFirma", adjunto.getAtributo("estadoFirma"));
                    gVO.setAtributo("numeroFirmas",adjunto.getAtributo("numeroFirmas"));
                }else{
                    gVO.setAtributo("codDocumentoAdjuntoExpediente","");
                    gVO.setAtributo("tipoMimeAdjuntoDocumentoExpediente","");
                    gVO.setAtributo("nombreAdjuntoDocumentoExpediente","");
                    gVO.setAtributo("fechaAltaAdjuntoDocumentoExpediente","");
                    gVO.setAtributo("estadoFirma", "");
                    gVO.setAtributo("numeroFirmas",0);
                }

                documentos.addElement(gVO);
            }
         

        }catch (Exception e){
            documentos = new Vector(); 
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        return documentos;
    }
    
    
    

    public byte[] cargarFirmaDocumento(Integer codDocumento, String[] params ){
        if (m_Log.isDebugEnabled()){m_Log.debug("CargarFirmaDocumento : Inicio");}
        byte[] salida=null;
         
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{         
            
            //Cargamos la conexion
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            //Creamos la query
            StringBuffer sql = new StringBuffer();
            sql.append("Select FIRMA from E_DOCS_FIRMAS where ID_DOC_PRESENTADO = ?");
            if (m_Log.isDebugEnabled()){m_Log.debug("CargarFirmaDocumento() :Query  "+ sql);}        
            
            
            //Asignamos los parametros
            ps = con.prepareStatement(sql.toString() );
            ps.setInt(1, codDocumento);
            
            
            //Ejecutamos la query
            rs = ps.executeQuery();

            while (rs.next()) {
                salida = rs.getBytes("FIRMA");
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            
        }catch(Exception e){
            m_Log.error("CargarFirmaDocumento(): Error " + e.getMessage() );
        }
        
     
        
        if (m_Log.isDebugEnabled()){m_Log.debug("CargarFirmaDocumento : Fin");}
        return salida;
    }
    
    
    public Vector<FicheroVO> cargaDocumentosExpediente (GeneralValueObject gVO,boolean expedienteHistorico, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Vector<FicheroVO> documentos = new Vector<FicheroVO>();
        FicheroVO fich = new FicheroVO();

        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numero = (String) gVO.getAtributo("numero");

        try {
            
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String sql = "";
            
            
            if(!esExpedienteHistorico(numero, con)) {
                sql = "SELECT PRESENTADO_TIPO,PRESENTADO_NOMBRE, PRESENTADO_COD,PRESENTADO_COD_DOC,PRESENTADO_EXTENSION "
                    + "FROM E_DOCS_PRESENTADOS "
                    + "WHERE PRESENTADO_MUN=" + codMunicipio
                    + " AND PRESENTADO_EJE= " + ejercicio
                    + " AND PRESENTADO_NUM= '" + numero
                    + "' AND PRESENTADO_PRO='" + codProcedimiento + "' ORDER BY PRESENTADO_FECHA_ALTA DESC, PRESENTADO_NOMBRE ASC";
                
            } else { 
                sql = "SELECT PRESENTADO_TIPO,PRESENTADO_NOMBRE, PRESENTADO_COD,PRESENTADO_COD_DOC,PRESENTADO_EXTENSION "
                    + "FROM HIST_E_DOCS_PRESENTADOS "
                    + "WHERE PRESENTADO_MUN=" + codMunicipio
                    + " AND PRESENTADO_EJE= " + ejercicio
                    + " AND PRESENTADO_NUM= '" + numero
                    + "' AND PRESENTADO_PRO='" + codProcedimiento + "' ORDER BY PRESENTADO_FECHA_ALTA DESC, PRESENTADO_NOMBRE ASC";            
            }
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {            
                fich = new FicheroVO();
                fich.setNombre(rs.getString("PRESENTADO_NOMBRE"));
                fich.setCodigo(rs.getString("PRESENTADO_COD"));
                fich.setEjercicio(ejercicio);
                fich.setExpediente(numero);
                fich.setMunicipio(codMunicipio);
                fich.setTipo(rs.getString("PRESENTADO_TIPO"));
                fich.setNumero(numero);
                fich.setCodigoDocumentoPresentado(rs.getString("PRESENTADO_COD_DOC"));
                documentos.add(fich);
            }

           

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
            return documentos;
        }
        
    }
    
    

    /**
     * Comprueba si un documento de expediente tiene asociado un adjunto y en ese caso, recupera el nombre del adjunto, fecha de alta y su tipo mime
     * @param codMunicipio: Código de municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número de expediente
     * @param codProcedimiento: Código de procedimiento
     * @param codDocumento: Código de documento
     * @param con: Conexión a la base de datos
     * @return GeneralValueObject con el tipo mime, nombre del fichero y su fecha de alta
     */
    private GeneralValueObject cargarDatosAdjuntoDocumentoExpediente(int codMunicipio,int ejercicio,String numero,String codProcedimiento,int codDocumento, Connection con) throws TechnicalException{
        ResultSet rs = null;
        PreparedStatement ps  =null;
        GeneralValueObject gVO = null;
        try{

            String sql = "SELECT PRESENTADO_COD, PRESENTADO_TIPO,PRESENTADO_NOMBRE,PRESENTADO_FECHA_ALTA " +
                              "FROM E_DOCS_PRESENTADOS " +
                              "WHERE PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? " +
                              "AND PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++,codMunicipio);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numero);
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,codDocumento);

            rs = ps.executeQuery();
            while(rs.next()){
                int codDocumentoPresentado = rs.getInt("PRESENTADO_COD");
                String estadoFirma = calcularEstadoFirma(codDocumentoPresentado,con);
                Timestamp tFechaAlta = rs.getTimestamp("PRESENTADO_FECHA_ALTA");
                gVO = new GeneralValueObject();
                gVO.setAtributo("estadoFirma", estadoFirma);
                gVO.setAtributo("codDocumentoAdjuntoExpediente", codDocumentoPresentado+"");
                gVO.setAtributo("tipoMimeAdjuntoDocumentoExpediente",rs.getString("PRESENTADO_TIPO"));
                gVO.setAtributo("nombreAdjuntoDocumentoExpediente",rs.getString("PRESENTADO_NOMBRE"));
                
                
                //Cargamos el numero de firmas del documento
                Integer numFirmas =calcularNumeroFirmas(codDocumentoPresentado,con);
                gVO.setAtributo("numeroFirmas", numFirmas);
                
                if(tFechaAlta!=null)
                    gVO.setAtributo("fechaAltaAdjuntoDocumentoExpediente",DateOperations.extraerFechaTimeStamp(tFechaAlta));
                else
                    gVO.setAtributo("fechaAltaAdjuntoDocumentoExpediente","");
            }

        }catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException("ERROR DE BASE DE DATOS AL TRAER LOS DATOS DE LOS DOCUMENTOS", e);
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return gVO;
    }// cargarDatosAdjuntoDocumentoExpediente


    public String calcularEstadoFirma(int codDocumentoPresentado, Connection con) throws TechnicalException {
        String estadoDocumento = "";
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "SELECT DOC_FIRMA_ESTADO FROM E_DOCS_FIRMAS WHERE ID_DOC_PRESENTADO = ? ORDER BY DOC_FIRMA_ORDEN ASC";
        try {
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setInt(1, codDocumentoPresentado);
            rs = ps.executeQuery();
            ArrayList<String> estados = new ArrayList<String>();
            while (rs.next()) {
                estados.add(rs.getString("DOC_FIRMA_ESTADO"));
            }
            estadoDocumento = devolverEstadoFirma(estados); 
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TechnicalException("ERROR DE BASE DE DATOS AL TRAER EL ESTADO DE LA FIRMA DEL DOCUMENTO", e);
        }
        return estadoDocumento;
    }
    
    
    public Integer calcularNumeroFirmas(int codDocumentoPresentado, Connection con) throws TechnicalException {
        Integer numeroFirmas =0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "SELECT DOC_FIRMA_ESTADO FROM E_DOCS_FIRMAS WHERE ID_DOC_PRESENTADO = ? ORDER BY DOC_FIRMA_ORDEN ASC";
        try {
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setInt(1, codDocumentoPresentado);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                numeroFirmas++;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TechnicalException("ERROR DE BASE DE DATOS AL TRAER EL ESTADO DE LA FIRMA DEL DOCUMENTO", e);
        }
        return numeroFirmas;
    }
    

    public Vector cargaListaEnlaces(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;

        Vector enlaces = new Vector();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio"); 
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql ="SELECT " + enp_des + "," + enp_url + "," + enp_est + " FROM E_ENP WHERE " + enp_mun + "=" + codMunicipio +
                    " AND " + enp_pro + "='" + codProcedimiento + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);

            while(rs.next()){
                gVO = new GeneralValueObject();
                gVO.setAtributo("descEnlace",rs.getString(enp_des));
                gVO.setAtributo("url",rs.getString(enp_url));
                gVO.setAtributo("estadoEnlace",rs.getString(enp_est));
                enlaces.addElement(gVO);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        }catch (Exception e){
            enlaces = new Vector(); // Vacío
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return enlaces;
    }
    
    
    
    
    public Vector cargaListaEnlaces(GeneralValueObject gVO,Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;

        Vector enlaces = new Vector();

        String codMunicipio     = (String)gVO.getAtributo("codMunicipio"); 
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

        try{
                      
            sql ="SELECT ENP_DES,ENP_URL,ENP_EST FROM E_ENP WHERE ENP_MUN=?" +
                 " AND ENP_PRO=?";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            rs = ps.executeQuery();

            while(rs.next()){
                gVO = new GeneralValueObject();
                gVO.setAtributo("descEnlace",rs.getString(enp_des));
                gVO.setAtributo("url",rs.getString(enp_url));
                gVO.setAtributo("estadoEnlace",rs.getString(enp_est));
                enlaces.addElement(gVO);
            }
            

        }catch (Exception e){
            enlaces = new Vector(); // Vacío
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        return enlaces;
    }
    
   
    public int actualizaListaDocumentosExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {

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
                    sql ="DELETE FROM e_doe "
                            + " WHERE e_doe."+sql_doe_codProc+" = '" + codProcedimiento + "' AND  e_doe."+sql_doe_codMun+" = " + codMunicipio
                            + " AND e_doe."+sql_doe_ejer+" ='"+ ejercicio +"'" + " AND e_doe."+sql_doe_num+" ='"+ numero+"'";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    resultado1 = st.executeUpdate(sql);
                    SigpGeneralOperations.closeStatement(st);

                    // Insertamos.
                    sql = "INSERT INTO E_DOE ( " + sql_doe_codMun + "," + sql_doe_ejer + ", " + sql_doe_num
                            + ", " + sql_doe_codProc + "," + sql_doe_fEntreg + "," + sql_doe_codDoc
                            + ") VALUES ( " + codMunicipio + "," + ejercicio + ",'" + numero +"'"
                            + ",'" + codProcedimiento+"'"
                            + ", " + oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ";

                    String entrar = "no";
                    for (int i=0; i<documentos.size(); i++) {
                        documento = (GeneralValueObject) documentos.elementAt(i);
                        String entregado = (String) documento.getAtributo("ENTREGADO");
                        if ("SI".equals(entregado)) {
                            entrar = "si";
                            String sql1 =  sql + (String) documento.getAtributo("codigo") + ")";
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql1);
                            resultado = st.executeUpdate(sql1);
                            SigpGeneralOperations.closeStatement(st);
                        }
                    } 
                    if("no".equals(entrar)) {
                        resultado = 1;
                    }
                    if (resultado >= 0)
                        SigpGeneralOperations.commit(oad, con);
                    else
                        SigpGeneralOperations.rollBack(oad, con);
                }catch (Exception e){
                    SigpGeneralOperations.rollBack(oad, con);
                    resultado=-1;
                    e.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }finally{
                    SigpGeneralOperations.devolverConexion(oad, con);
                }
            } else {
                resultado = 1;
            }
        }
        return resultado;
    }

    public int grabarExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = null;
         Vector terceros = null;
        TercerosValueObject terAntiguo = null; 
        TercerosValueObject terNuevo = null;         
        TercerosValueObject tercero = null;
        DomicilioSimpleValueObject domicilioA = null;       
        DomicilioSimpleValueObject domicilioN = null;       
        InteresadoExpedienteVO interesadoExpedienteVO = null;
        boolean existe = false;
        boolean cambioVersion = false;
        boolean cambioRol = false;
        boolean cambioDomicilio = false;
        boolean cambioNotifElec = false; 
        boolean cambioDatos = false;        
        int j = 0;
        int resultado=0;
        int resultado1 = 0;
        int resultado2 = 0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numeroExpediente = (String)gVO.getAtributo("numero");
        String localizacion = (String)gVO.getAtributo("localizacion");
        String codLocalizacion = (String)gVO.getAtributo("codLocalizacion");
        String observaciones = (String)gVO.getAtributo("observaciones");
        String asunto = (String)gVO.getAtributo("asunto");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String usuario = (String) gVO.getAtributo("usuario");        
        String nomUsuario = (String) gVO.getAtributo("nomUsuario");        
        
        // #253692
        String ubicacionDoc = (String)gVO.getAtributo("ubicacionDoc");

        Vector listaCodTercero = (Vector) gVO.getAtributo("listaCodTercero");
        Vector listaVersionTercero = (Vector) gVO.getAtributo("listaVersionTercero");
        Vector listaCodDomicilio = (Vector) gVO.getAtributo("listaCodDomicilio");
        Vector listaRol = (Vector) gVO.getAtributo("listaRol");
        Vector listaMostrar = (Vector) gVO.getAtributo("listaMostrar");
        Vector listaNotificacionesElectronica = (Vector)gVO.getAtributo("listaNotificacionesElectronica");

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            //Recupera la lista de interesados asociados al expediente
            Vector interesados = InteresadosDAO.getInstance().getListaInteresados(gVO,oad,con);


            sql = "UPDATE E_EXP SET EXP_OBS = ?, EXP_ASU = ?, EXP_LOC = ?, EXP_CLO = ?, EXP_UBICACION_DOC = ? WHERE EXP_MUN = ? AND EXP_EJE = ? AND EXP_NUM = ?";
            m_Log.debug("grabarExpediente sql:  " + sql);
            int contbd =1;
            ps = con.prepareStatement(sql);
            ps.setString(contbd++, observaciones);
            ps.setString(contbd++, asunto);
            ps.setString(contbd++, localizacion);
            if (codLocalizacion!= null &&!"".equals(codLocalizacion)) {
                ps.setInt(contbd++, Integer.parseInt(codLocalizacion));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            // #253692
            ps.setString(contbd++, ubicacionDoc);
            //
            if (codMunicipio!= null&&!"".equals(codMunicipio)) {
                ps.setInt(contbd++, Integer.parseInt(codMunicipio));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            if (ejercicio!= null && !"".equals(ejercicio)) {
                ps.setInt(contbd++, Integer.parseInt(ejercicio));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            ps.setString(contbd++, numeroExpediente);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            m_Log.debug("update exp param 1: " + observaciones);
            m_Log.debug("update exp param 2: " + asunto);
            m_Log.debug("update exp param 3: " + localizacion);
            m_Log.debug("update exp param 4: " + codLocalizacion);
            m_Log.debug("update exp param 5: " + ubicacionDoc);
            m_Log.debug("update exp param 6: " + codMunicipio);
            m_Log.debug("update exp param 7: " + ejercicio);
            m_Log.debug("update exp param 8: " + numeroExpediente);

            resultado = ps.executeUpdate();
            SigpGeneralOperations.closeStatement(ps);

            sql = "DELETE FROM E_EXT WHERE EXT_MUN = ? AND EXT_EJE = ? AND EXT_NUM = ?";
            ps = con.prepareStatement(sql);
            contbd = 1;
            if (codMunicipio!= null&&!"".equals(codMunicipio)) {
                ps.setInt(contbd++, Integer.parseInt(codMunicipio));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            if (ejercicio!= null && !"".equals(ejercicio)) {
                ps.setInt(contbd++, Integer.parseInt(ejercicio));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            ps.setString(contbd++, numeroExpediente);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado1 = ps.executeUpdate();
            SigpGeneralOperations.closeStatement(ps);
            
              
               
            if(listaCodTercero.size() >0) {
                for (int i=0; i<listaCodTercero.size(); i++) {
                    
                    int mostrar;
                    if (Boolean.parseBoolean((String)listaMostrar.elementAt(i))) mostrar = 1;
                    else mostrar = 0;
                    

                    sql = "INSERT INTO E_EXT (EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, EXT_ROL, EXT_PRO, MOSTRAR, EXT_NOTIFICACION_ELECTRONICA ) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)";
                    m_Log.debug("grabarExpediente sql: " + sql);
                     m_Log.debug("grabarExpediente listaNotificacionesElectronica: " + listaNotificacionesElectronica);
                    
                    ps = con.prepareStatement(sql);
                    contbd = 1;
                    if (codMunicipio!= null&&!"".equals(codMunicipio)) {
                        ps.setInt(contbd++, Integer.parseInt(codMunicipio));
                    } else ps.setNull(contbd++, java.sql.Types.INTEGER);
                    if (ejercicio!= null && !"".equals(ejercicio)) {
                        ps.setInt(contbd++, Integer.parseInt(ejercicio));
                    } else ps.setNull(contbd++, java.sql.Types.INTEGER);
                    ps.setString(contbd++, numeroExpediente);
                    if (listaCodTercero.elementAt(i)!= null && !"".equals((String)listaCodTercero.elementAt(i))) {
                        ps.setInt(contbd++, Integer.parseInt((String)listaCodTercero.elementAt(i)));
                    } else ps.setNull(contbd++, java.sql.Types.INTEGER);
                    if (listaVersionTercero.elementAt(i)!= null && !"".equals((String)listaVersionTercero.elementAt(i))) {
                        ps.setInt(contbd++, Integer.parseInt((String)listaVersionTercero.elementAt(i)));
                    } else ps.setNull(contbd++, java.sql.Types.INTEGER);
                    if (listaCodDomicilio.elementAt(i)!= null && !"".equals((String)listaCodDomicilio.elementAt(i))) {
                        ps.setInt(contbd++, Integer.parseInt((String)listaCodDomicilio.elementAt(i)));
                    } else ps.setNull(contbd++, java.sql.Types.INTEGER);
                    if (listaRol.elementAt(i)!= null && !"".equals((String)listaRol.elementAt(i))) {
                        ps.setInt(contbd++, Integer.parseInt((String)listaRol.elementAt(i)));
                    } else ps.setNull(contbd++, java.sql.Types.INTEGER);
                    ps.setString(contbd++, codProcedimiento);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps.setInt(contbd++, mostrar);
                    
                    if (listaNotificacionesElectronica !=null && listaNotificacionesElectronica.size()>0 &&
                            listaNotificacionesElectronica.elementAt(i)!= null
                            && !"".equals((String)listaNotificacionesElectronica.elementAt(i))) {
                        ps.setInt(contbd++, Integer.parseInt((String)listaNotificacionesElectronica.elementAt(i)));
                    } else ps.setNull(contbd++, java.sql.Types.INTEGER);
                    
                    
                    resultado2 = ps.executeUpdate();
                    SigpGeneralOperations.closeStatement(ps);
                }
            }
             //Comprueba si ha sido eliminado algun interesado
            for (int i=0; i<interesados.size(); i++) {
                interesadoExpedienteVO = (InteresadoExpedienteVO)interesados.elementAt(i);
                existe = false;                                
                j = 0;
                while (j<listaCodTercero.size() && !existe){
                    String codTer = (String)listaCodTercero.elementAt(j);
                    if (interesadoExpedienteVO.getCodTercero() == Integer.valueOf(codTer).intValue()){
                        existe = true;
                    }
                    j++;
                }
                if (!existe){
                    tercero = new TercerosValueObject();
                    tercero.setIdentificador(String.valueOf(interesadoExpedienteVO.getCodTercero()));
                    tercero.setVersion(String.valueOf(interesadoExpedienteVO.getNumVersion()));
                    tercero.setDocumento(interesadoExpedienteVO.getTxtDoc());
                    tercero.setNombreCompleto(interesadoExpedienteVO.getNombreCompleto());
                    
                    OperacionesExpedienteManager.getInstance().registrarEliminacionInteresado(Integer.valueOf(codMunicipio).intValue(),
                            numeroExpediente, Integer.valueOf(usuario).intValue(), nomUsuario, tercero, con);  
                }
            }
            
            //Comprueba si se ha dado de alta alg\FAn interesado
            for (int i=0; i<listaCodTercero.size(); i++) {
                String codTer = (String)listaCodTercero.elementAt(i);
                existe = false;
                j = 0;
                while ( j<interesados.size() && !existe){
                   interesadoExpedienteVO = (InteresadoExpedienteVO)interesados.elementAt(j);
                    if (Integer.valueOf(codTer).intValue() == interesadoExpedienteVO.getCodTercero()){
                        existe = true;
                    }
                    j++;
                }
                if (!existe){
                    tercero = new TercerosValueObject();
                    tercero.setIdentificador((String)listaCodTercero.elementAt(i));
                    tercero.setVersion((String)listaVersionTercero.elementAt(i));
                    tercero.setIdDomicilio((String)listaCodDomicilio.elementAt(i));
                    terceros = TercerosDAO.getInstance().getByHistorico(tercero, con, params);
                    tercero = (TercerosValueObject) terceros.elementAt(0);
                    tercero.setCodRol(Integer.valueOf((String)listaRol.elementAt(i)));
                    String notifElecTer = null;
                    if (listaNotificacionesElectronica !=null && listaNotificacionesElectronica.size()>0 &&
                                listaNotificacionesElectronica.get(i)!= null
                                && !"".equals((String)listaNotificacionesElectronica.get(i))) {
                            notifElecTer = String.valueOf(listaNotificacionesElectronica.get(i)); 
                    }  
                    tercero.setNotificacionElectronica(notifElecTer);
                                        
                    OperacionesExpedienteManager.getInstance().registrarAltaInteresado(Integer.valueOf(codMunicipio).intValue(),
                            numeroExpediente, Integer.valueOf(usuario).intValue(), nomUsuario, tercero, con);  
                }
            }
            
            //Comprobamos si ha modificado alg\FAn interesado
            for (int i=0; i<listaCodTercero.size(); i++) {
                String codTer = (String)listaCodTercero.elementAt(i);
                String versionTer = (String)listaVersionTercero.elementAt(i);
                String rolTer = (String)listaRol.elementAt(i);
                String domTer = (String)listaCodDomicilio.elementAt(i);
                String notifElecTer = null;
                if (listaNotificacionesElectronica !=null && listaNotificacionesElectronica.size()>0 &&
                            listaNotificacionesElectronica.get(i)!= null
                            && !"".equals((String)listaNotificacionesElectronica.get(i))) {
                        notifElecTer = String.valueOf(listaNotificacionesElectronica.get(i)); 
                } 
                existe = false;
                cambioVersion = false;
                cambioRol = false;
                cambioDomicilio = false;
                cambioNotifElec = false;
                cambioDatos = false;
                j = 0;
                while (j < interesados.size() && !existe){
                    interesadoExpedienteVO = (InteresadoExpedienteVO)interesados.elementAt(j);
                    if (Integer.valueOf(codTer).intValue() == interesadoExpedienteVO.getCodTercero()){
                        existe = true;
                        //Recuperamos la informaci\F3n del domicilio antiguo                           
                        tercero = new TercerosValueObject();
                        tercero.setIdentificador(String.valueOf(interesadoExpedienteVO.getCodTercero()));
                        tercero.setVersion(String.valueOf(interesadoExpedienteVO.getNumVersion()));
                        tercero.setIdDomicilio(String.valueOf(interesadoExpedienteVO.getCodDomicilio()));
                        terceros = TercerosDAO.getInstance().getByHistorico(tercero, con, params);
                        terAntiguo = (TercerosValueObject) terceros.elementAt(0);

                        //Recuperamos la informaci\F3n del nuevo domicilio
                        tercero = new TercerosValueObject();
                        tercero.setIdentificador(codTer);
                        tercero.setVersion(versionTer);
                        tercero.setIdDomicilio(domTer);
                        terceros = TercerosDAO.getInstance().getByHistorico(tercero, con, params);
                        terNuevo = (TercerosValueObject) terceros.elementAt(0);

                        GeneralValueObject gVOModif = new GeneralValueObject();
                        gVOModif.setAtributo("codMunicipio", codMunicipio);
                        gVOModif.setAtributo("ejercicio", ejercicio);
                        gVOModif.setAtributo("numero", numeroExpediente);
                        gVOModif.setAtributo("usuario", usuario);
                        gVOModif.setAtributo("nomUsuario", nomUsuario);
                        gVOModif.setAtributo("Codigo", codTer);

                        //Comprueba si se ha modificado la versi\F3n del tercero
                        if (Integer.valueOf(versionTer).intValue() != interesadoExpedienteVO.getNumVersion()){
                                gVOModif.setAtributo("VersionA", String.valueOf(interesadoExpedienteVO.getNumVersion()));                            
                                gVOModif.setAtributo("VersionN", versionTer);                               
                                cambioVersion = true;                                
                        }
                        //Comprueba si se ha modificado el rol del tercero
                        if (Integer.valueOf(rolTer).intValue()  != interesadoExpedienteVO.getCodigoRol()){
                                String descRolAntiguo = RolDAO.getInstance().getDescRolPorCodigo(Integer.valueOf(codMunicipio).intValue(), codProcedimiento, interesadoExpedienteVO.getCodigoRol(), con);
                                String descRolNuevo = RolDAO.getInstance().getDescRolPorCodigo(Integer.valueOf(codMunicipio).intValue(), codProcedimiento, Integer.valueOf(rolTer).intValue(), con);
                                gVOModif.setAtributo("RolA", String.valueOf(interesadoExpedienteVO.getCodigoRol()));
                                gVOModif.setAtributo("DescRolA", descRolAntiguo);                                
                                gVOModif.setAtributo("RolN", rolTer);
                                gVOModif.setAtributo("DescRolN", descRolNuevo);                                
                                cambioRol = true;                                
                            }

                        //Recupera el domicilio antiguo del tercero
                        if (terAntiguo.getDomicilios() != null && terAntiguo.getDomicilios().size()>0){
                            domicilioA = (DomicilioSimpleValueObject) terAntiguo.getDomicilios().elementAt(0);
                        }  
                        //Recupera el domicilio nuevo del tercero
                        if (terNuevo.getDomicilios() != null && terNuevo.getDomicilios().size()>0){
                            domicilioN = (DomicilioSimpleValueObject) terNuevo.getDomicilios().elementAt(0);
                        }                                
                        
                        //Comprueba si se ha modificado el domicilio del tercero
                        if (Integer.valueOf(domTer).intValue() != interesadoExpedienteVO.getCodDomicilio() || 
                            (domicilioA == null && domicilioN != null) || (domicilioA != null && domicilioN == null) ||
                            (domicilioA != null && domicilioN != null && !domicilioN.equals(domicilioA))){
                            cambioDomicilio = true;
                            gVOModif.setAtributo("CodDomicilioA", String.valueOf(interesadoExpedienteVO.getCodDomicilio()));
                            gVOModif.setAtributo("CodDomicilioN",domTer); 

                            if (domicilioA != null){
                                gVOModif.setAtributo("TipoViaA", domicilioA.getTipoVia());
                                gVOModif.setAtributo("DescViaA", domicilioA.getDescVia());
                                gVOModif.setAtributo("EmplazamientoA", domicilioA.getDomicilio());
                                gVOModif.setAtributo("PortalA", domicilioA.getPortal());
                                gVOModif.setAtributo("PlantaA", domicilioA.getPlanta());
                                gVOModif.setAtributo("PuertaA", domicilioA.getPuerta());
                                gVOModif.setAtributo("EscaleraA", domicilioA.getEscalera());                            
                                gVOModif.setAtributo("BloqueA", domicilioA.getBloque());
                                gVOModif.setAtributo("LetraDesdeA", domicilioA.getLetraDesde());
                                gVOModif.setAtributo("LetraHastaA", domicilioA.getLetraHasta());
                                gVOModif.setAtributo("NumDesdeA", domicilioA.getNumDesde());                            
                                gVOModif.setAtributo("NumHastaA", domicilioA.getNumHasta());
                                gVOModif.setAtributo("CodigoPostalA", domicilioA.getCodigoPostal());                            
                                gVOModif.setAtributo("MunicipioA", domicilioA.getMunicipio());
                                gVOModif.setAtributo("ProvinciaA", domicilioA.getProvincia());
                            }
                            
                            if (domicilioN != null){
                                gVOModif.setAtributo("TipoViaN", domicilioN.getTipoVia());
                                gVOModif.setAtributo("DescViaN", domicilioN.getDescVia());
                                gVOModif.setAtributo("EmplazamientoN", domicilioN.getDomicilio());
                                gVOModif.setAtributo("PortalN", domicilioN.getPortal());
                                gVOModif.setAtributo("PlantaN", domicilioN.getPlanta());
                                gVOModif.setAtributo("PuertaN", domicilioN.getPuerta());
                                gVOModif.setAtributo("EscaleraN", domicilioN.getEscalera());                            
                                gVOModif.setAtributo("BloqueN", domicilioN.getBloque());
                                gVOModif.setAtributo("LetraDesdeN", domicilioN.getLetraDesde());
                                gVOModif.setAtributo("LetraHastaN", domicilioN.getLetraHasta());
                                gVOModif.setAtributo("NumDesdeN", domicilioN.getNumDesde());                            
                                gVOModif.setAtributo("NumHastaN", domicilioN.getNumHasta());
                                gVOModif.setAtributo("CodigoPostalN", domicilioN.getCodigoPostal());                            
                                gVOModif.setAtributo("MunicipioN", domicilioN.getMunicipio());
                                gVOModif.setAtributo("ProvinciaN", domicilioN.getProvincia());
                            }
                        }
                                
                        //Comprueba si se ha modificado la notificacion electronica
                        if (notifElecTer != null && !notifElecTer.equals(interesadoExpedienteVO.getAdmiteNotificacion())){
                                gVOModif.setAtributo("NotifElectA",interesadoExpedienteVO.getAdmiteNotificacion());
                                gVOModif.setAtributo("NotifElectN",notifElecTer);                                  
                                cambioNotifElec = true;                               
                        }
                        //Comprueba los datos del tercero
                        if (!terAntiguo.getNombre().equals(terNuevo.getNombre()) || !terAntiguo.getApellido1().equals(terNuevo.getApellido1()) ||
                            !terAntiguo.getApellido2().equals(terNuevo.getApellido2()) || !terAntiguo.getTipoDocumento().equals(terNuevo.getTipoDocumento()) ||
                            !terAntiguo.getDocumento().equals(terNuevo.getDocumento()) || !terAntiguo.getTelefono().equals(terNuevo.getTelefono()) ||
                            !terAntiguo.getEmail().equals(terNuevo.getEmail())){
                                gVOModif.setAtributo("NombreA", terAntiguo.getNombre());
                                gVOModif.setAtributo("NombreN", terNuevo.getNombre());                                    
                                gVOModif.setAtributo("Apellido1A", terAntiguo.getApellido1());                                    
                                gVOModif.setAtributo("Apellido1N", terNuevo.getApellido1());                                    
                                gVOModif.setAtributo("Apellido2A", terAntiguo.getApellido2());
                                gVOModif.setAtributo("Apellido2N", terNuevo.getApellido2());                                    
                                gVOModif.setAtributo("TipoDocumentoA", terAntiguo.getTipoDocumento());
                                gVOModif.setAtributo("TipoDocumentoN", terNuevo.getTipoDocumento());                                    
                                gVOModif.setAtributo("DocumentoA", terAntiguo.getDocumento());
                                gVOModif.setAtributo("DocumentoN", terNuevo.getDocumento());                                    
                                gVOModif.setAtributo("TelefonoA", terAntiguo.getTelefono());
                                gVOModif.setAtributo("TelefonoN", terNuevo.getTelefono());                                    
                                gVOModif.setAtributo("EmailA", terAntiguo.getEmail());
                                gVOModif.setAtributo("EmailN", terNuevo.getEmail());                                    
                                cambioDatos = true;
                        }

                        if (cambioVersion || cambioRol || cambioDomicilio || cambioNotifElec || cambioDatos){
                            OperacionesExpedienteManager.getInstance().registrarModificacionInteresado(gVOModif,
                                    cambioVersion,cambioRol,cambioDomicilio,cambioNotifElec,cambioDatos, con);  
                        }                           
                    }
                    j++;
                }
            }
            if (resultado >= 0)
                SigpGeneralOperations.commit(oad, con);
            else
                SigpGeneralOperations.rollBack(oad, con);

        }catch (Exception e){
            SigpGeneralOperations.rollBack(oad, con);
            resultado=-1;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return resultado;
    }
    
    public int grabarExpedienteObservaciones(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = null;
        int resultado=0;
        int resultado1 = 0;
        int resultado2 = 0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numeroExpediente = (String)gVO.getAtributo("numero");
        String observaciones = (String)gVO.getAtributo("observaciones");


        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            sql = "UPDATE E_EXP SET EXP_OBS = ? WHERE EXP_MUN = ? AND EXP_EJE = ? AND EXP_NUM = ?";

            int contbd =1;
            ps = con.prepareStatement(sql);
            ps.setString(contbd++, observaciones);

            if (codMunicipio!= null&&!"".equals(codMunicipio)) {
                ps.setInt(contbd++, Integer.parseInt(codMunicipio));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            if (ejercicio!= null && !"".equals(ejercicio)) {
                ps.setInt(contbd++, Integer.parseInt(ejercicio));
            } else ps.setNull(contbd++, java.sql.Types.INTEGER);
            ps.setString(contbd++, numeroExpediente);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            m_Log.debug("update exp param 1: " + observaciones);

            m_Log.debug("update exp param 5: " + codMunicipio);
            m_Log.debug("update exp param 6: " + ejercicio);
            m_Log.debug("update exp param 7: " + numeroExpediente);

            resultado = ps.executeUpdate();
            ps.close();

          
            if (resultado >= 0)
                oad.finTransaccion(con);
            else
                oad.rollBack(con);

        }catch (Exception e){
            try{
                oad.rollBack(con);
            }catch(BDException bde){
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
            resultado=-1;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try{
                if (ps!=null) ps.close();
                oad.devolverConexion(con);
            }catch(Exception bde) {
                resultado=-1;
                bde.printStackTrace();
                bde.getMessage();
            }
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
            oad.inicioTransaccion(con) ; 
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            String codUsuario = (String) gVO.getAtributo("usuario");
            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codEntidad = (String) gVO.getAtributo("codEntidad"); 
 
            String sql = "SELECT DISTINCT " + tra_cod + "," + tml_valor + "," + cml_valor +
                    "," + tra_uin + " FROM E_EXP,E_TRA,E_TML," + GlobalNames.ESQUEMA_GENERICO + "A_CML A_CML WHERE " +
                    exp_mun + "=" + tra_mun + " AND " +
                    exp_pro + "=" + tra_pro + " AND " + tml_mun + "=" + tra_mun + " AND " + tml_pro + "=" +
                    tra_pro + " AND " + tml_tra + "=" + tra_cod + " AND " + tml_cmp + "='NOM' AND " +
                    tml_leng + "='"+idiomaDefecto+"' AND " + cml_cod + "=" + tra_cls + " AND " + cml_cmp + "='NOM' AND " +
                    cml_leng + "='"+idiomaDefecto+"' AND " + tra_mun + "=" + codMunicipio + " AND " + tra_pro + "='" +
                    codProcedimiento + "' AND " +exp_num+"='"+numero+"' AND "+ tra_fba + " IS NULL AND ((EXISTS ( SELECT DISTINCT " +
                    uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " + uou_usu + "=" +
                    codUsuario + " AND " + uou_org + "=" +
                    codOrganizacion + " AND " + uou_ent + "=" + codEntidad + " AND " + uou_uor + "=" +
                    exp_uor + ") AND " + tra_uin + " IS NOT NULL) OR EXISTS (SELECT DISTINCT " + uou_uor +
                    " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " + uou_usu + "=" + codUsuario + " AND " + uou_org + "=" +
                    codOrganizacion + " AND " + uou_ent + "=" + codEntidad + " AND " + uou_uor + "=" + tra_uin + ") OR " + tra_uin + "=-99999OR " + tra_uin + "=-99998) AND ( " +
                    tra_ocu + " > ( select count(*) from e_cro where " + cro_mun + "=" +
                    codMunicipio + " and " + cro_pro +"='"+codProcedimiento+"'"+ " and " + cro_eje + " = " + ejercicio + " and " +
                    cro_num + " = '" + numero + "' " + " and " + cro_tra + " = " + tra_cod + " ) "+
                    " or "  + tra_ocu + " is null )" + " order by "+ cml_valor + "," + tml_valor ;
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
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.commit(oad, con);

        }catch (BDException e){
            SigpGeneralOperations.rollBack(oad, con);
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

    public Vector getListaUnidadesUsuario(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        Vector listaUnidadesUsuario = new Vector();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String codUsuario = (String) gVO.getAtributo("usuario");
            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codEntidad = (String) gVO.getAtributo("codEntidad");

            String sql = "SELECT " + uor_cod + "," + uor_nom + " FROM A_UOR," + GlobalNames.ESQUEMA_GENERICO +
                    "A_UOU A_UOU WHERE " +
                    uou_org + "=" + codOrganizacion + " AND " + uou_ent + "=" +
                    codEntidad + " AND " + uou_usu + "=" + codUsuario + " AND " +
                    uou_uor + "=" + uor_cod + " ORDER BY " + uor_nom;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject tramiteVO = new GeneralValueObject();
                tramiteVO.setAtributo("codUnidadTramitadora",rs.getString(uor_cod));
                tramiteVO.setAtributo("descUnidadTramitadora",rs.getString(uor_nom));
                listaUnidadesUsuario.add(tramiteVO);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);            

        }catch (SQLException e){            
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            listaUnidadesUsuario = new Vector();
        }catch (BDException e){            
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            listaUnidadesUsuario = new Vector();
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);   
            SigpGeneralOperations.devolverConexion(oad, con);
            
        }
        return listaUnidadesUsuario;
    }

    public int iniciarExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {
        return ExpedientesDAO.getInstance().iniciarExpediente(gVO,params);
    }

    public int iniciarExpedienteAsiento(AdaptadorSQLBD oad, GeneralValueObject gVO, Connection con, String[] params) throws TechnicalException {

        int resultado=0;
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

            sql = "SELECT " + rol_cod + " FROM E_ROL WHERE "  + rol_mun + "=" + codMunicipio + " AND " +
                    rol_pro + "='" + codProcedimiento + "' AND " + rol_pde + " = 1";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                String codRol = rs.getString(rol_cod);
                gVO.setAtributo("codRol",codRol);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);   
            
            // "desdeAsiento" se usa en ExpedientesDAO.insertarExpediente para 
            // saber que se ha llamado desde aqui. Tambien pasamos los parametros
            // de conexion por si hace falta mapear los roles.
            gVO.setAtributo("desdeAsiento", "si");
            gVO.setAtributo("params",params);

            resultado = ExpedientesDAO.getInstance().insertarExpediente(oad, con, gVO);
            if (resultado > 0) {
                resultado = insertarExpedienteAsiento(oad, con, gVO);
                if (resultado > 0)
                    resultado = TramitesExpedienteDAO.getInstance().iniciarTramiteInicio(oad,con,gVO);
            }

            // Insercion en el histórico de movimientos de anotaciones
            HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
            hvo.setCodigoUsuario(Integer.parseInt((String) gVO.getAtributo("usuario")));
            hvo.setTipoEntidad(ConstantesDatos.HIST_ENTIDAD_ANOTACION);
            String claveAnotacion = ((String) gVO.getAtributo("codDepartamento")) + "/" +
                                    ((String) gVO.getAtributo("codUnidadRegistro")) + "/" +
                                    ((String) gVO.getAtributo("tipoAsiento")) + "/" +
                                    ((String) gVO.getAtributo("ejercicioAsiento")) + "/" +
                                    ((String) gVO.getAtributo("numeroAsiento"));
            hvo.setCodigoEntidad(claveAnotacion);
            hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_INICIAR);
            hvo.setDetallesMovimiento(
                    HistoricoAnotacionHelper.crearXMLIniciar((String)gVO.getAtributo("numero")));
            HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, con, params);
            
            if (resultado < 0) throw new TechnicalException("FICHAEXPEDIENTE. ERROR");

        }catch (Exception e){
            resultado=-1;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
        }
        return resultado;
    }


    private int insertarExpedienteAsiento(AdaptadorSQL oad, Connection con, GeneralValueObject gVO) throws SQLException, TechnicalException {

        Statement st = null;
        String sql = null;
        int resultado=0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String ejercicioAsiento = (String)gVO.getAtributo("ejercicioAsiento");
        String numeroAsiento = (String)gVO.getAtributo("numeroAsiento");
        String dpto = (String)gVO.getAtributo("codDepartamento");
        String unid = (String)gVO.getAtributo("codUnidadRegistro");
        String tipoAsiento = (String)gVO.getAtributo("tipoAsiento");
        String ori ="1";
        String top ="0";
        String estado = "1";
        String dejarAnotacionBuzonEntrada = (String) gVO.getAtributo("valorOpcionPermanencia");

        /**
         * Dado que la anotación puede tener alguna asociación a expediente grabada desde el registro de E/S,
         * lo primero que haremos será borrarla, ya que una anotación no puede estar relacionada a más de una
         * entrada.
         */
        /*
        sql = "DELETE FROM E_EXR WHERE EXR_DEP=" + dpto + " AND EXR_UOR=" + unid + " AND EXR_TIP='" + tipoAsiento +
                "' AND EXR_EJR= " + ejercicio + " AND EXR_NRE=" + numeroAsiento;

        if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Eliminar relacion expediente " + sql);
                }
        st = con.createStatement();
                resultado = st.executeUpdate(sql); 
        */  
        // #253742: Añadir valor de fecha actual al insertar
        java.util.Date fechoraActual = new java.util.Date();
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(fechoraActual.getTime());  
        sql = " INSERT INTO E_EXR (" + sql_exr_eje + "," + sql_exr_num + "," + sql_exr_dep + "," +
                sql_exr_uor + "," + sql_exr_tip + "," + sql_exr_ejr + "," + sql_exr_nre + "," +
                sql_exr_ori + "," + sql_exr_top + "," + sql_exr_mun + "," + sql_exr_pro + ",EXR_FECHAINSMOD) VALUES (" +
                ejercicio + ",'" + numero + "'," + dpto + "," + unid + ",'" + tipoAsiento + "'," + ejercicioAsiento +
                "," + numeroAsiento + "," + ori + ",'" + top + "'," + codMunicipio + ",'" + codProcedimiento + "'," + oad.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) +")";

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        st = con.createStatement();
        resultado = st.executeUpdate(sql);
        SigpGeneralOperations.closeStatement(st);

        if (resultado > 0)  {
            if(dejarAnotacionBuzonEntrada == null || dejarAnotacionBuzonEntrada.equalsIgnoreCase("") 
                    || !dejarAnotacionBuzonEntrada.equalsIgnoreCase("true")){
                st = con.createStatement();
                sql = "UPDATE R_RES SET " +  sql_res_estado + "=" + estado + " WHERE " +
                        sql_res_codDpto + "=" + dpto + " AND " + sql_res_codUnid + "=" +
                        unid + " AND " + sql_res_tipoReg + "='" + tipoAsiento +
                        "' AND " + sql_res_ejer + "=" + ejercicioAsiento + " AND " +
                        sql_res_num + "=" + numeroAsiento;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                resultado = st.executeUpdate(sql);
                SigpGeneralOperations.closeStatement(st);
            }/*if(dejarAnotacionBuzonEntrada == null || dejarAnotacionBuzonEntrada.equalsIgnoreCase("") 
                    || dejarAnotacionBuzonEntrada.equalsIgnoreCase(pro_tri))*/
        } else resultado=-1;
        return resultado;
    }
    
    public int retrocederExpedienteCH(GeneralValueObject gVO, String[] params) throws TechnicalException, EjecucionSWException {
    	 
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int res = -1;        
        String codigotramite=null;
        
        m_Log.debug(" funcion retrocederExpedienteCH");
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; // Inicio transacción

            this.conexionRetroceso = con;
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            String codTramiteRetroceder = (String) gVO.getAtributo("codTramiteRetroceder");
            String ocurrenciaTramiteRetroceder = (String) gVO.getAtributo("ocurrenciaTramiteRetroceder");
            
            m_Log.debug("RETROCEDEMOS EL TRAMITE: " + codTramiteRetroceder + " OCURRENCIA " + ocurrenciaTramiteRetroceder);
            
            // Recupero los tramites abiertos del expediente.
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ").append(oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS FEI, ").append(oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS FEF, CRO_TRA, CRO_PRO, CRO_NUM, CRO_OCU");
            sql.append(" FROM e_cro");
            sql.append(" WHERE ");
            sql.append("CRO_MUN").append(" = ").append(codMunicipio);
            sql.append(" AND ").append("CRO_PRO").append(" = '").append(codProcedimiento).append("'");
            sql.append(" AND ").append("CRO_EJE").append(" = '").append(ejercicio).append("'");
            sql.append(" AND ").append("CRO_NUM").append(" = '").append(numero).append("'");
            sql.append(" AND ").append("CRO_FEF").append(" is null");
            sql.append(" ORDER BY CRO_FEI DESC");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tramites abiertos del expediente: " + sql.toString());
            }
            
            st = con.prepareStatement(sql.toString());
            rs = st.executeQuery();

            Vector<String[]> tramitesAbiertos = new Vector<String[]>();
            while (rs.next()) {
                String[] tramiteAbierto = new String[3];
                tramiteAbierto[0] = rs.getString("CRO_TRA");
                tramiteAbierto[1] = rs.getString("CRO_OCU");
                tramiteAbierto[2] = rs.getString("FEI");
                tramitesAbiertos.addElement(tramiteAbierto);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            
            // Recupero los tramites cerrados del expediente.
            sql = new StringBuffer();
            sql.append("SELECT CRO_TRA, CRO_OCU, ").append(oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS FEI");
            sql.append(" FROM e_cro");
            sql.append(" WHERE ");
            sql.append("CRO_MUN").append(" = ").append(codMunicipio);
            sql.append(" AND ").append("CRO_PRO").append(" = '").append(codProcedimiento).append("'");
            sql.append(" AND ").append("CRO_EJE").append(" = '").append(ejercicio).append("'");
            sql.append(" AND ").append("CRO_NUM").append(" = '").append(numero).append("'");
            sql.append(" AND ").append("CRO_FEF").append(" is NOT null");
            sql.append(" ORDER BY CRO_FEI DESC");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tramites cerrados del expediente: " + sql.toString());
            }
            
            st = con.prepareStatement(sql.toString());
            rs = st.executeQuery();

            Vector<String[]> tramitesCerrados = new Vector<String[]>();
            while (rs.next()) {
                String[] tramiteCerrado = new String[3];
                tramiteCerrado[0] = rs.getString("CRO_TRA");
                tramiteCerrado[1] = rs.getString("CRO_OCU");
                tramiteCerrado[2] = rs.getString("FEI");
                tramitesCerrados.addElement(tramiteCerrado);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            
            // Ahora tengo que comprobar en cual de los dos grupos se encuentra el 
            // tramite que se quiere retroceder.
            boolean estaEnAbiertos = false;
            String fechaInicio = null;
            for (String[] tramiteAbierto: tramitesAbiertos) {
                if (tramiteAbierto[0].equals(codTramiteRetroceder) && tramiteAbierto[1].equals(ocurrenciaTramiteRetroceder)) {
                    estaEnAbiertos = true;
                    fechaInicio = tramiteAbierto[2];
                }
                
            }
            
            if (estaEnAbiertos) {
                // Comprobamos que posible tramite puede haber iniciado el seleccionado.
                sql = new StringBuffer();
                sql.append("SELECT ORIGEN.CRO_TRA AS COD_TRAM_ORI, ORIGEN.CRO_OCU AS OCU_TRAM_ORI ");
                sql.append("FROM E_CRO SELECCIONADO ");
                sql.append("JOIN E_FLS ON (FLS_MUN = SELECCIONADO.CRO_MUN AND FLS_PRO = SELECCIONADO.CRO_PRO AND FLS_CTS = SELECCIONADO.CRO_TRA) ");
                sql.append("JOIN E_CRO ORIGEN ON (FLS_MUN = ORIGEN.CRO_MUN AND FLS_PRO = ORIGEN.CRO_PRO AND FLS_TRA = ORIGEN.CRO_TRA) ");
                sql.append("WHERE SELECCIONADO.CRO_TRA = ? AND SELECCIONADO.CRO_OCU = ? AND SELECCIONADO.CRO_PRO = ? AND SELECCIONADO.CRO_MUN = ? AND SELECCIONADO.CRO_NUM = ? ");
                sql.append("AND ORIGEN.CRO_PRO = ? AND ORIGEN.CRO_MUN = ? AND ORIGEN.CRO_NUM = ? ");
                sql.append("AND (").append(oad.convertir("ORIGEN.CRO_FEF",AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH24:MI:SS")).append(") = (")
                        .append(oad.convertir("ORIGEN.CRO_FEF",AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH24:MI:SS")).append(")");
                
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Comprobación de que tramite ha iniciado el seleccionado: " + sql.toString());
                }
                
                st = con.prepareStatement(sql.toString());
                st.setInt(1, Integer.parseInt(codTramiteRetroceder));
                st.setInt(2, Integer.parseInt(ocurrenciaTramiteRetroceder));
                st.setString(3, codProcedimiento);
                st.setInt(4, Integer.parseInt(codMunicipio));
                st.setString(5, numero);
                st.setString(6, codProcedimiento);
                st.setInt(7, Integer.parseInt(codMunicipio));
                st.setString(8, numero);
                
                rs = st.executeQuery();
                
                String codTramInicio = null, ocuTramInicio = null;
                if (rs.next()) {
                    codTramInicio = rs.getString("COD_TRAM_ORI");
                    ocuTramInicio = rs.getString("OCU_TRAM_ORI");
                }
                
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
                
                m_Log.debug("EL TRAMITE A RETROCEDER FUE INICIADO POR EL TRAMITE: " + codTramInicio + ", OCURRENCIA: " + ocuTramInicio);
                boolean reabrirTramOrigen = true;
                if (codTramInicio != null) {
                    // Comprobamos si hay tramites que han podido ser iniciados por el tramite de origen
                    // aparte del seleccionado para retroceder.
                    sql = new StringBuffer();
                    sql.append("SELECT SELECCIONADO.CRO_TRA AS COD_TRAM_DEST, ORIGEN.CRO_OCU AS OCU_TRAM_DEST ");
                    sql.append("FROM E_CRO SELECCIONADO ");
                    sql.append("JOIN E_FLS ON (FLS_MUN = SELECCIONADO.CRO_MUN AND FLS_PRO = SELECCIONADO.CRO_PRO AND FLS_CTS = SELECCIONADO.CRO_TRA) ");
                    sql.append("JOIN E_CRO ORIGEN ON (FLS_MUN = ORIGEN.CRO_MUN AND FLS_PRO = ORIGEN.CRO_PRO AND FLS_TRA = ORIGEN.CRO_TRA) ");
                    sql.append("WHERE ORIGEN.CRO_TRA = ? AND ORIGEN.CRO_OCU = ? AND SELECCIONADO.CRO_PRO = ? AND SELECCIONADO.CRO_MUN = ? AND SELECCIONADO.CRO_NUM = ? ");
                    sql.append("AND ORIGEN.CRO_PRO = ? AND ORIGEN.CRO_MUN = ? AND ORIGEN.CRO_NUM = ? ");
                    sql.append("AND (").append(oad.convertir("ORIGEN.CRO_FEF", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS")).append(") = (").append(oad.convertir("ORIGEN.CRO_FEF", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS")).append(")");

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Se comprueba si aparte del seleccionado hay otros trámites que han podido ser abiertos: " + sql.toString());
                    }

                    st = con.prepareStatement(sql.toString());
                    st.setInt(1, Integer.parseInt(codTramInicio));
                    st.setInt(2, Integer.parseInt(ocuTramInicio));
                    st.setString(3, codProcedimiento);
                    st.setInt(4, Integer.parseInt(codMunicipio));
                    st.setString(5, numero);
                    st.setString(6, codProcedimiento);
                    st.setInt(7, Integer.parseInt(codMunicipio));
                    st.setString(8, numero);
                    m_Log.debug("param 1: " + codTramInicio);
                    m_Log.debug("param 2: " + ocuTramInicio);
                    m_Log.debug("param 3: " + codProcedimiento);
                    m_Log.debug("param 4: " + codMunicipio);
                    m_Log.debug("param 5: " + numero);
                    m_Log.debug("param 6: " + codProcedimiento);
                    m_Log.debug("param 7: " + codMunicipio);
                    m_Log.debug("param 8: " + numero);
                    rs = st.executeQuery();
                                        
                    while (rs.next()) {
                        String codTramDest = rs.getString("COD_TRAM_DEST");
                        String ocuTramDest = rs.getString("OCU_TRAM_DEST");
                        m_Log.debug("codTramDest: " + codTramDest);
                        m_Log.debug("ocuTramDest: " + ocuTramDest);

                        if (codTramDest.equals(codTramiteRetroceder) && ocuTramDest.equals(ocurrenciaTramiteRetroceder)) reabrirTramOrigen=true; else reabrirTramOrigen = false;
                    }
                    
                    SigpGeneralOperations.closeResultSet(rs);
                    SigpGeneralOperations.closeStatement(st);
                    
                    m_Log.debug("¿HAY QUE REABRIR EL TRAMITE DE ORIGEN?: " + reabrirTramOrigen);
                }
                 
                m_Log.debug("retrocederExpediente entrando");
                retrocederExpediente(gVO, params, con, codTramiteRetroceder, ocurrenciaTramiteRetroceder);
                
                if (reabrirTramOrigen){
                     m_Log.debug("retrocederExpediente reabrimos el trámite de origen por: " + reabrirTramOrigen);
                    retrocederExpediente(gVO, params, con, codTramInicio, ocuTramInicio);
                }

            } else {
                m_Log.debug("retrocederExpediente el trámite no está entre los abiertos");
                retrocederExpediente(gVO, params, con, codTramiteRetroceder, ocurrenciaTramiteRetroceder);
            }
            
            SigpGeneralOperations.commit(oad, con);
            
        } catch (BDException bde) {
            res = -1;
            SigpGeneralOperations.rollBack(oad, con);
            SigpGeneralOperations.devolverConexion(oad, con);
            throw new TechnicalException(bde.getMessage(), bde);
        } catch (SQLException e) {
            SigpGeneralOperations.rollBack(oad, con);
            e.printStackTrace();
            res = -1;
            SigpGeneralOperations.devolverConexion(oad, con);
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        //envio el codigo del tramite a donde tengo que retroceder

        if (codigotramite == null) return -1;
        else return Integer.parseInt(codigotramite.trim());        
    }

    
    private void retrocederExpediente(GeneralValueObject gVO, String[] params, Connection con, String codTramiteRetroceder, String ocuTramRetro) throws TechnicalException, EjecucionSWException {
   
       AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
       Statement st = null;
       ResultSet rs = null;
       StringBuffer sql = new StringBuffer();
       m_Log.debug("retrocederExpediente para codTramiteRetroceder: " + codTramiteRetroceder + " ocurrencia: " + ocuTramRetro);
       try{
           oad.inicioTransaccion(con);
           String codMunicipio = (String)gVO.getAtributo("codMunicipio");
           String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
           String ejercicio = (String)gVO.getAtributo("ejercicio");
           String numero = (String)gVO.getAtributo("numero");
           String codigoIdiomaUsuario = (String)gVO.getAtributo("codigoIdiomaUsuario");
           
           m_Log.debug(" datos que recojo del formulario");
           m_Log.debug(" [codMunicipio]"+codMunicipio);
           m_Log.debug(" [codProcedimiento]"+codProcedimiento);
           m_Log.debug(" [ejercicio]"+ejercicio);
           m_Log.debug(" [numero]"+numero);
           m_Log.debug("[codTramiteRetroceder]"+codTramiteRetroceder);
           m_Log.debug(" [ocurrenciaTramiteRetroceder]"+ocuTramRetro);
           
           if (codProcedimiento==null||"".equals(codProcedimiento)) throw new TechnicalException("EL codigo de procedimiento NO PUEDE SER NULO");
           if (codMunicipio==null||"".equals(codMunicipio)) throw new TechnicalException("EL codMunicipio NO PUEDE SER NULO");
           if (ejercicio==null||"".equals(ejercicio)) throw new TechnicalException("EL EJERCICIO NO PUEDE SER NULO");
           if (numero==null||"".equals(numero)) throw new TechnicalException("EL numero de expediente NO PUEDE SER NULO");
           if (codTramiteRetroceder==null||"".equals(codTramiteRetroceder)) throw new TechnicalException("EL codTramiteRetroceder NO PUEDE SER NULO");
           if (ocuTramRetro==null||"".equals(ocuTramRetro)) throw new TechnicalException("EL ocuTramRetro NO PUEDE SER NULO");
           
           sql.append ("SELECT CRO_FEI AS FEI, CRO_FEF AS FEF");
           sql.append (" FROM e_cro");
           sql.append (" WHERE ");
           sql.append (cro_mun).append(" = ").append(codMunicipio);
           sql.append (" AND ").append(cro_pro).append(" = '").append(codProcedimiento).append("'");
           sql.append (" AND ").append(cro_eje).append(" = '").append(ejercicio).append("'");
           sql.append (" AND ").append(cro_num).append(" = '").append(numero).append("'");
           sql.append (" AND ").append(cro_tra).append(" = ").append(codTramiteRetroceder);
           sql.append (" AND ").append(cro_ocu).append(" = ").append(ocuTramRetro);
           sql.append (" ORDER BY " + cro_fei + " DESC");

           if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
           st = con.createStatement();
           rs = st.executeQuery(sql.toString());

           Timestamp fecha_inicio = null;
           Timestamp fecha_fin = null;
           while(rs.next()){
               fecha_inicio = rs.getTimestamp("FEI");
               fecha_fin = rs.getTimestamp("FEF");
           }
           gVO.setAtributo("fechaInicio", fecha_inicio);
           SigpGeneralOperations.closeResultSet(rs);
           SigpGeneralOperations.closeStatement(st);           
           m_Log.debug("FECHA INICIO RECUPERADA: " + fecha_inicio);               
         
           if(fecha_fin==null && fecha_inicio!= null) {
           	 if(m_Log.isDebugEnabled()) m_Log.debug("--> Dentro de if  de retroceder expediente");
                   
           	 //Borramos de F_TRAFORM_TRA
           	 sql = new StringBuffer();
                sql.append ("DELETE FROM f_traform_tra");
                sql.append (" WHERE ");
                sql.append (tft_tra_mun).append(" = ").append(codMunicipio);
                sql.append (" AND ").append(tft_tra_pro).append(" = '").append(codProcedimiento).append("'");
                sql.append (" AND ").append(tft_tra_eje).append(" = ").append(ejercicio);
                sql.append (" AND ").append(tft_tra_num).append(" = '").append(numero).append("'");
                sql.append (" AND ").append(tft_tra_tra).append(" = ").append(codTramiteRetroceder);
                sql.append (" AND ").append(tft_tra_ocu).append(" = ").append(ocuTramRetro);

                if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en F_TRAFORM_TRA :" + sql.toString());
                st = con.createStatement();
                st.executeUpdate(sql.toString());
                SigpGeneralOperations.closeStatement(st);
                
                //Borramos de F_FIRMA
                sql = new StringBuffer();
                sql.append ("DELETE FROM f_firma");
                sql.append (" WHERE ");
                sql.append (ff_trafirma_mun).append(" = ").append(codMunicipio);
                sql.append (" AND ").append(ff_trafirma_pro).append(" = '").append(codProcedimiento).append("'");
                sql.append (" AND ").append(ff_trafirma_eje).append(" = ").append(ejercicio);
                sql.append (" AND ").append(ff_trafirma_num).append(" = '").append(numero).append("'");
                sql.append (" AND ").append(ff_trafirma_tra).append(" = ").append(codTramiteRetroceder);
                sql.append (" AND ").append(ff_trafirma_ocu).append(" = ").append(ocuTramRetro);

                if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en F_FIRMA :" + sql.toString());
                st = con.createStatement();
                st.executeUpdate(sql.toString());
                SigpGeneralOperations.closeStatement(st);
                
                //Se unifican todos los borrados de datos suplementarios en un método
                elimarRegistrosCamposSuplementarios(codMunicipio, codProcedimiento, ejercicio, numero, codTramiteRetroceder, ocuTramRetro, con);
                        
                AlarmasExpedienteDAO.getInstance().borrarAlarmasOcurrenciaTramite(Integer.parseInt(codMunicipio),
                        Integer.parseInt(ejercicio),numero,Integer.parseInt(codTramiteRetroceder),
                        Integer.parseInt(ocuTramRetro), con);
                
           	//Bloqueos
               sql = new StringBuffer();
               sql.append ("DELETE FROM e_exp_bloq");
               sql.append (" WHERE ");
               sql.append (bloq_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(bloq_pro).append(" = '").append(codProcedimiento).append("'");
               sql.append (" AND ").append(bloq_eje).append(" = ").append(ejercicio);
               sql.append (" AND ").append(bloq_num).append(" = '").append(numero).append("'");
               sql.append (" AND ").append(bloq_tra).append(" = ").append(codTramiteRetroceder);
               sql.append (" AND ").append(bloq_ocu).append(" = ").append(ocuTramRetro);

               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               st.executeUpdate(sql.toString());
               SigpGeneralOperations.closeStatement(st);
           	            	
               sql = new StringBuffer();
               sql.append ("DELETE FROM e_cro");
               sql.append (" WHERE ");
               sql.append (cro_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(cro_pro).append(" = '").append(codProcedimiento).append("'");
               sql.append (" AND ").append(cro_eje).append(" = ").append(ejercicio);
               sql.append (" AND ").append(cro_num).append(" = '").append(numero).append("'");
               sql.append (" AND ").append(cro_tra).append(" = ").append(codTramiteRetroceder);
               sql.append (" AND ").append(cro_ocu).append(" = ").append(ocuTramRetro);

               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               st.executeUpdate(sql.toString());
               SigpGeneralOperations.closeStatement(st);
               
               /****************************************************************/
               //Se extrae todo el contenido para eliminar los datos relacionados a ese tramite
               try {
                eliminarTodosDocumentosAsociadoTramite (codMunicipio, codProcedimiento, ejercicio, numero, codTramiteRetroceder, ocuTramRetro, con);
               } catch (TechnicalException e) {
                throw new TechnicalException(e.getMessage());
               }
               /****************************************************************/
               
               /**** SI EL TRÁMITE TIENE ASOCIADAS TAREAS PENDIENTES DE INICIO, SE ELIMINAN YA QUE SE ELIMINA LA OCURRENCIA DEL MISMO
                **** PARA REABRIR EL TRÁMITE DE ORIGEN */
               boolean tareasPendientesEliminadas = ModuloIntegracionExternoDAO.getInstance().eliminarTareasPendienteInicio(Integer.parseInt(codMunicipio), Integer.parseInt(codTramiteRetroceder), Integer.parseInt(ocuTramRetro), numero, con);
               if(tareasPendientesEliminadas){
                    m_Log.debug("********** LAS TAREAS PENDIENTES DEL TRÁMITE HAN SIDO ELIMINADAS ******************");
               }else
                   m_Log.debug("********** LAS TAREAS PENDIENTES DEL TRÁMITE NO HAN SIDO ELIMINADAS ******************");

               /***************************/

           } else {
           	//entra si la fecha fei=null
               gVO.setAtributo("jndi", params[6]);
               m_Log.debug(".....ENTRO EN SINO....-->"+params[6]);
               try {
                   // Llamar al Servicio Web para retroceder.
                   int intCodMunicipio = Integer.parseInt(codMunicipio);
                   int intCodTramite = Integer.parseInt(codTramiteRetroceder);
                   m_Log.debug(".....TRAMITE A RETROCEDER....-->"+codTramiteRetroceder);
                   int intOcurrencia = Integer.parseInt(ocuTramRetro);
                   int intEjercicio = Integer.parseInt(ejercicio);
                   m_Log.debug(".....EJERCICIO A RETROCEDER....-->"+ejercicio);
                   PeticionSWVO peticionSW = new PeticionSWVO(intCodMunicipio, codProcedimiento, intCodTramite, false,true,false,
                           numero, intOcurrencia, intEjercicio,true, params);            
                    peticionSW.setOrigenLlamada((String) gVO.getAtributoONulo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO));
                   GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                   launcher.ejecutarSWTramitacion(peticionSW,oad,con);
               } catch (NoServicioWebDefinidoException e) {
                   m_Log.debug("NO HAY NINGUN SERVICIO WEB DEFINIDO AL RETROCEDER ESTE TRAMITE");
               } catch (FaltaDatoObligatorioException fdoe) {
                   throw new EjecucionSWException(fdoe, "EL VALOR DEL CAMPO " + fdoe.getNombreCampo() + " ES OBLIGATORIO PARA LLAMAR AL SERVICIO WEB");
               }                
               catch(EjecucionOperacionModuloIntegracionException e){
                    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                    traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
                    traductor.setIdi_cod(Integer.parseInt(codigoIdiomaUsuario));

                    StringBuffer mensaje = new StringBuffer();
                    if(e.getNombreModulo()!=null && !"".equals(e.getCodigoErrorOperacion()) && e.getCodigoErrorOperacion()!=null && !"".equals(e.getCodigoErrorOperacion())){
                        try{
                            ResourceBundle  resource = ResourceBundle.getBundle(e.getNombreModulo());
                            if(resource!=null){
                                // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                                String nombreMensaje = resource.getString(codMunicipio + ConstantesDatos.MODULO_INTEGRACION + e.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + e.getCodigoErrorOperacion());
                                mensaje.append(resource.getString(nombreMensaje + ConstantesDatos.GUION_BAJO + codigoIdiomaUsuario));
                            }
                        }catch(Exception ex){
                            m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + e.getNombreModulo() + " para la operación "
                                    + e.getOperacion() + ". Se muestra un mensaje de error genérico.");

                            // SE MUESTRA ENTONCES UN MENSAJE GENÉRICO
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

                    throw new EjecucionSWException(e,mensaje.toString());             
               }
               
               m_Log.debug("FECHA INICIO RECUPERADA: " + fecha_inicio);
               sql = new StringBuffer();
               sql.append ("UPDATE e_cro SET ");
               sql.append (cro_fef).append(" = NULL,");
               sql.append (cro_usf).append(" = NULL ");
               sql.append (" WHERE ");
               sql.append (cro_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(cro_pro).append(" = '").append(codProcedimiento).append("'");
               sql.append (" AND ").append(cro_eje).append(" = '").append(ejercicio).append("'");
               sql.append (" AND ").append(cro_num).append(" = '").append(numero).append("'");
               sql.append (" AND ").append(cro_tra).append(" = ").append(codTramiteRetroceder);
               sql.append (" AND ").append(cro_ocu).append(" = ").append(ocuTramRetro);
               
               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               int rowsUpdated = st.executeUpdate(sql.toString());
               m_Log.debug("Num filas afectadas último update: " + rowsUpdated);
               SigpGeneralOperations.closeStatement(st);
             
               
               // ACTUALIZAR LOS CAMPOS EXP_TRA Y EXP_TOCU CON EL ULTIMO TRAMITE FINALIZADO
               String codTramiteUltCerrado = "";
               String ocuTramiteUltCerrado = "";
               sql = new StringBuffer();
               sql.append ("SELECT ").append(cro_tra + "," + cro_ocu);
               sql.append (" FROM e_cro");
               sql.append (" WHERE ");
               sql.append (cro_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(cro_pro).append(" = '").append(codProcedimiento).append("'");
               sql.append (" AND ").append(cro_eje).append(" = '").append(ejercicio).append("'");
               sql.append (" AND ").append(cro_num).append(" = '").append(numero).append("'");
               sql.append (" AND ").append(cro_fef).append(" IS NOT NULL");
               sql.append (" ORDER BY " + cro_fef + " DESC");
               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               rs = st.executeQuery(sql.toString());
                             
               
               if(rs.next()) {
                   codTramiteUltCerrado = rs.getString(cro_tra);
                   ocuTramiteUltCerrado = rs.getString(cro_ocu);
               }
               SigpGeneralOperations.closeStatement(st);
               SigpGeneralOperations.closeResultSet(rs);
               
               sql = new StringBuffer();
               sql.append ("UPDATE e_exp SET ");
               if(codTramiteUltCerrado != null && !"".equals(codTramiteUltCerrado)) {
                   sql.append (exp_tra).append(" = ").append(codTramiteUltCerrado).append(",");
                   sql.append (exp_tocu).append(" = ").append(ocuTramiteUltCerrado);
               } else {
                   sql.append (exp_tra).append(" = ").append("NULL").append(",");
                   sql.append (exp_tocu).append(" = ").append("NULL");
               }
               sql.append (" WHERE ");
               sql.append (exp_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(exp_eje).append(" = '").append(ejercicio).append("'");
               sql.append (" AND ").append(exp_num).append(" = '").append(numero).append("'");

               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               st.executeUpdate(sql.toString());
               SigpGeneralOperations.closeStatement(st);
           }
           
           SigpGeneralOperations.commit(oad, con);
           
       } catch (SQLException sqle) {
           SigpGeneralOperations.rollBack(oad, con);
           sqle.printStackTrace();     
           throw new TechnicalException(sqle.getMessage(), sqle);
       } catch (EjecucionSWException eswe) {
           SigpGeneralOperations.rollBack(oad, con);
           //eswe.printStackTrace();     
           throw eswe;
       } catch (BDException e) {
           SigpGeneralOperations.rollBack(oad, con);
           e.printStackTrace();           
           throw new TechnicalException(e.getMessage(), e);
       } finally {
           SigpGeneralOperations.closeResultSet(rs);
           SigpGeneralOperations.closeStatement(st);            
           SigpGeneralOperations.devolverConexion(oad, con);
       }
   }

     private int borrarFirmantesDocumentos(String codMunicipio,String codProcedimiento,String ejercicio,String numero,String tra,String ocu,String[] params) throws TechnicalException{
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        int res = -1;
        StringBuffer sql = new StringBuffer();
   
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();


            st = con.createStatement();
            sql.append ("DELETE FROM E_CRD_FIR_FIRMANTES");
            sql.append (" WHERE ");
            sql.append (" COD_MUNICIPIO ").append(" = ").append(codMunicipio);
            sql.append (" AND ").append("COD_PROCEDIMIENTO").append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append("EJERCICIO").append(" = '").append(ejercicio).append("'");
            sql.append (" AND ").append("NUM_EXPEDIENTE").append(" = '").append(numero).append("'");
            sql.append (" AND ").append("COD_TRAMITE").append(" = ").append(tra);
            sql.append (" AND ").append("COD_OCURRENCIA").append(" = ").append(ocu);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            res = st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);

        }catch (BDException e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.devolverConexion(oad, con);
            return res;
        }
    }

    public Boolean cargarVista(GeneralValueObject gVO,Connection con) throws TechnicalException{
        if(m_Log.isDebugEnabled()) m_Log.debug("cargarVista() : BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        Boolean cargar = false;
        
        try{           
            
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");            
            sql = "SELECT PCA_COD FROM E_PCA WHERE PCA_PRO =? AND PCA_ACTIVO = ? AND PCA_OCULTO <> ? AND PCA_POS_X IS NULL";
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,codProcedimiento);
            ps.setString(i++,"SI");
            ps.setString(i++,"SI");
            
            if(m_Log.isDebugEnabled()) m_Log.debug("sql =" + sql);
            rs = ps.executeQuery();
            
            if(!rs.next()){
                cargar = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeResultSet(rs);                        
            return cargar;
        }//try-catch
    }//cargarVista
    
    public Vector cargaEstructuraDatosSuplementarios(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null, st2 = null, st3 = null;
        ResultSet rs = null, rs2 = null, rs3 = null;
        String sql = "", sql2 = "", sql3 = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();
        String campoActivo ="";
        String codigoCampo ="";
        String tempCodTipoDato ="";
        String tempCodCampo="";
        GeneralValueObject plazoActivo=new GeneralValueObject();
        
        m_Log.info("-------------------------FichaExpedienteDAO: cargaEstructuraDatosSuplementarios() BEGIN:" );
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String codigoTramite = (String)gVO.getAtributo("codTramite");
            String numero = (String)gVO.getAtributo("numero");
            

            String desdeJsp=(String)gVO.getAtributo("desdeJsp");
            String consultaCampos=(String)gVO.getAtributo("consultaCampos");
            
            
            st3 = con.createStatement();
            sql3 = "SELECT TFE_COD,PLAZO_ACTIVADO FROM  E_TFE WHERE TFE_NUM='"
                    + numero + "'";
            rs3 = st3.executeQuery(sql3);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
            }
            while (rs3.next()) {
                codigoCampo = String.valueOf(rs3.getString("TFE_COD"));
                campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                m_Log.debug("campoActivo:::" + campoActivo);
                plazoActivo.setAtributo(codigoCampo, campoActivo);

            }
            
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs3);


            from = pca_cod + "," + pca_des + "," + pca_plt + "," + plt_url + "," + pca_tda + "," +
                   pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," + pca_rot + "," +
                   "CAMPO."+pca_activo + "," + pca_desplegable+",PCA_BLOQ, PLAZO_AVISO,PERIODO_PLAZO, PCA_GROUP,  PCA_POS_X, PCA_POS_Y, GRUPO.PCA_ORDER_GROUP";
            where = pca_mun + " = " + codMunicipio + " AND CAMPO." + pca_pro + " = '" + codProcedimiento + "' AND " +
                    "CAMPO."+pca_activo + " = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND PCA_OCULTO='NO'";
            String[] join = new String[8];
            join[0] = "E_PCA campo";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] =  pca_plt + "=e_plt." + plt_cod;
            join[4] = "LEFT";
            join[5] = "E_PCA_GROUP GRUPO";
            join[6] = "CAMPO.PCA_PRO=GRUPO.PCA_PRO  and PCA_GROUP=PCA_ID_GROUP";
            join[7] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"18","18","17","17","9","9"};
            if (params[0] != null && (params[0].equalsIgnoreCase("oracle"))) {
                sql += " ORDER BY GRUPO.PCA_order_group NULLS FIRST,18, 17, 9";
            } else {
                sql += " ORDER BY GRUPO.PCA_order_group,18, 17, 9";
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            
            Hashtable<String,ArrayList<EstructuraCampo>> contenedor=new Hashtable<String,ArrayList<EstructuraCampo>>();
            
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(pca_cod);
                tempCodCampo = codCampo;
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(pca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(pca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(pca_tda);
                tempCodTipoDato = codTipoDato;
                m_Log.debug("Tipo de dato::" + tempCodTipoDato);
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
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String bloqueado = rs.getString("PCA_BLOQ");                
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
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
                }//if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo))
                if(codTipoDato != null && !"".equalsIgnoreCase(codTipoDato) && ("1".equalsIgnoreCase(codTipoDato) 
                                || "3".equalsIgnoreCase(codTipoDato)
                                || "8".equalsIgnoreCase(codTipoDato) 
                                || "9".equalsIgnoreCase(codTipoDato))){
                    eC.setTamanoVista("50");
                }else{
                    eC.setTamanoVista("100");
                }//
                String posX = String.valueOf(rs.getInt("PCA_POS_X"));
                String posY = String.valueOf(rs.getInt("PCA_POS_Y"));
                //if((posX != null && !posX.equalsIgnoreCase("0")) && (posY != null && !posY.equalsIgnoreCase("0"))){
                if((posX != null) && (posY != null)){
                    eC.setPosicionar(true);
                }//if((posX != null && !posX.equalsIgnoreCase("0")) && (posY != null && !posY.equalsIgnoreCase("0")))
                eC.setPosX(posX);
                eC.setPosY(posY);
                String desplegable = "";
                try {
                     /*if (("3").equals(tempCodTipoDato)){
                         m_Log.info("TIPO DE DATO FECHA...");
                         m_Log.info("CodCampo::" + tempCodCampo);
                         st3 = con.createStatement();
                         sql3 = "SELECT PLAZO_ACTIVADO FROM  E_TFE WHERE TFE_COD ='"+
                                tempCodCampo + "' AND TFE_NUM='"+
                                numero + "'";
                         rs3 = st3.executeQuery(sql3);
                         if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
                         while (rs3.next()) {
                            campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                            m_Log.info("campoActivo:::" + campoActivo);
                            eC.setCampoActivo(campoActivo);
                        }
                        SigpGeneralOperations.closeStatement(st2);
                        SigpGeneralOperations.closeResultSet(rs2);
                     }*/
                    if (("3").equals(tempCodTipoDato)){
                        m_Log.debug("campoActivo:::" + (String)plazoActivo.getAtributo(tempCodCampo));
                        eC.setCampoActivo((String)plazoActivo.getAtributo(tempCodCampo));
                    }
                    
                        
                    if ( !rs.getString(pca_desplegable).equals(null)) {
                        desplegable = rs.getString(pca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        

                        ArrayList<EstructuraCampo> valoresDesplegables=contenedor.get(desplegable);
                        if(valoresDesplegables!=null)
                        {
                            
                            if(m_Log.isDebugEnabled()) m_Log.debug("ya tenemos guardados los valores para el desplegable: "+ desplegable);
                          
                           for(int i=0;i<valoresDesplegables.size();i++) 
                           {
                               EstructuraCampo estructura=valoresDesplegables.get(i);
                               listaCod.addElement(estructura.getCodCampo());
                               listaDesc.addElement(estructura.getDescCampo());
                               listaEstado.addElement(estructura.getEstadoValorCampo());
                           }
                            
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                            eC.setListaEstadoValorDesplegable(listaEstado);
                        }
                        else{ //No hay valores guardados para ese desplegable
                            st2 = con.createStatement();
                           sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", " + des_val_estado + " FROM E_DES_VAL WHERE " +
                                   des_val_campo +"='"+ desplegable + "' ORDER BY " + des_val_desc  + " ASC"; 
                            rs2 = st2.executeQuery(sql2);
                            if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios sal: " + sql2);
                            ArrayList<EstructuraCampo> valoresDevueltos=new ArrayList<EstructuraCampo>();
                            while (rs2.next()) {
                                
                                String desvalcod="'"+rs2.getString(des_val_cod)+"'";
                                String desvaldesc="'"+rs2.getString(des_val_desc)+"'";
                                String desvalestado="'"+rs2.getString(des_val_estado)+"'";
                                
                                listaCod.addElement(desvalcod);
                                listaDesc.addElement(desvaldesc);
                                listaEstado.addElement(desvalestado);
                              
                                 EstructuraCampo estructura=new EstructuraCampo();
                                 estructura.setCodCampo(desvalcod);
                                 estructura.setDescCampo(desvaldesc);
                                 valoresDevueltos.add(estructura);
                            }
                            contenedor.put(desplegable, valoresDevueltos);
                            
                            SigpGeneralOperations.closeStatement(st2);
                            SigpGeneralOperations.closeResultSet(rs2);
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                            eC.setListaEstadoValorDesplegable(listaEstado);
                        }
                    }
                } catch (NullPointerException e){
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("PLANTILLA : " + eC.getCodPlantilla());
                lista.addElement(eC);
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);

           
       
            from = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", oad.convertir("E_TCA.TCA_TRA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null), oad.convertir("E_TCA.TCA_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)}) + " AS " + tca_cod + ",E_TCA.TCA_DES,E_TCA.TCA_PLT," + plt_url + ",E_TCA.TCA_TDA,E_TCA.TCA_ROT" +
                    ",E_TCA.TCA_VIS,E_TCA.TCA_TRA,E_TCA.TCA_ACTIVO," + tml_valor + "," + cro_ocu + "," + tca_desplegable+",TCA_OCULTO,TCA_BLOQ,"+ oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{"E_TCA_GROUP.TCA_ORDER_GROUP", "-1"})+" as ordenGrupo";
            where = "e_tca.tca_mun = " + codMunicipio + " AND e_tca.tca_pro   = '" + codProcedimiento + "' AND e_tca.tca_activo='SI'";
            String[] join1 = new String[17];
            join1[0] = "e_tml";
            join1[1] = "INNER";
            join1[2] = "e_tca";
            join1[3] = "e_tca.tca_mun =e_tml.tml_mun AND " +
                    "e_tca.tca_pro=e_tml.tml_pro and " +
                    "e_tca.tca_tra= e_tml.tml_tra and " +
                    "e_tca.tca_vis = 'S' ";
            if (codigoTramite != null) join1[3] += " AND E_TCA.TCA_TRA <> " + codigoTramite;

            join1[4] = "INNER";
            join1[5] = "E_CRO";

            //String numero = (String)gVO.getAtributo("numero");

            join1[6] = "e_tca.tca_mun= e_cro.CRO_MUN AND " +
                       "e_tca.TCA_PRO  = e_cro.CRO_PRO AND " +
                       "e_tca.tca_TRA = e_cro.CRO_TRA AND " +
                       "e_cro.cro_num = '" + numero +"'";
            join1[7] = "INNER";
            join1[8] = "e_plt";
            join1[9] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join1[10] = "INNER";
            join1[11] = "e_tra";
            join1[12] = "e_tca." + tca_mun + "=e_tra." + tra_mun + " AND " +
                    "e_tca." + tca_pro + "=e_tra." + tra_pro + " AND " +
                    "e_tca." + tca_tra + "=e_tra." + tra_cod + " AND " +
                    "e_tra." + tra_fba + " IS NULL";

          
            join1[13]= "LEFT";
            join1[14]= " E_TCA_GROUP";
            join1[15]= " E_TCA.TCA_PRO=E_TCA_GROUP.TCA_PRO  "
                    + "and E_TCA.TCA_TRA=E_TCA_GROUP.TCA_TRA and "
                    + "E_TCA.PCA_GROUP=E_TCA_GROUP.TCA_ID_GROUP";
              
            // Para quitar la descripción del tramite
            join1[16] = "false";
         
            sql = oad.join(from,where,join1);

            sql = sql + " ORDER BY  E_TRA.TRA_COU,ordenGrupo,e_tca.TCA_POS_Y,e_tca.TCA_POS_X, e_tca.TCA_NOR, e_cro.CRO_OCU";
            // Fin consulta modificada
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
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
                eC.setTamano("");
                eC.setMascara("");
                eC.setObligatorio("0");
                eC.setNumeroOrden("0");
                String rotulo = rs.getString(tca_rot);
                eC.setRotulo(rotulo);
                String soloLectura = "true";
                eC.setSoloLectura(soloLectura);
                String codTramite = rs.getString(tca_tra);
                eC.setCodTramite(codTramite);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                String descripcionTramite = rs.getString(tml_valor);
                eC.setDescripcionTramite(descripcionTramite);
                String ocurrencia = rs.getString(cro_ocu);
                eC.setOcurrencia(ocurrencia);
                String bloqueado = rs.getString("TCA_BLOQ");               
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
                String desplegable = "";
                try {
                    if ( !rs.getString(tca_desplegable).equals(null)) {
                        desplegable = rs.getString(tca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        st2 = con.createStatement();
                        sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", "+des_val_estado +" FROM E_DES_VAL WHERE " +
                               des_val_campo +"='"+ desplegable + "'";
                        rs2 = st2.executeQuery(sql2);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                        while (rs2.next()) {
                            listaCod.addElement("'"+rs2.getString(des_val_cod)+"'");
                            listaDesc.addElement("'"+rs2.getString(des_val_desc)+"'");
                            listaEstado.addElement("'"+rs2.getString(des_val_estado)+"'");
                        }
                        SigpGeneralOperations.closeStatement(st2);
                        SigpGeneralOperations.closeResultSet(rs2);
                        eC.setListaCodDesplegable(listaCod);
                        eC.setListaDescDesplegable(listaDesc);
                        eC.setListaEstadoValorDesplegable(listaEstado);
                    }
                } catch (NullPointerException e){
                }
                eC.setCodAgrupacion("DVT");
                String visibleExpediente = rs.getString(tca_vis);
                if("S".equals(visibleExpediente)) {
                    String oculto = rs.getString("TCA_OCULTO");
                    if(("si".equals(desdeJsp))&& ("SI".equals(oculto)))
                    {
                      //Si viene de jsp y el campo esta oculto no se inserta el campo en la lista
                    }
                    else lista.addElement(eC);
                }
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);

        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return lista;
        }
    }
    
    
    
    
    
    public Vector cargaEstructuraDatosSuplementarios(GeneralValueObject gVO,AdaptadorSQLBD oad,Connection con,String[] params) throws TechnicalException {

        Statement st = null, st2 = null, st3 = null;
        ResultSet rs = null, rs2 = null, rs3 = null;
        String sql = "", sql2 = "", sql3 = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();
        String campoActivo ="";
        String codigoCampo ="";
        String tempCodTipoDato ="";
        String tempCodCampo="";
        GeneralValueObject plazoActivo=new GeneralValueObject();
        
        m_Log.info("-------------------------FichaExpedienteDAO: cargaEstructuraDatosSuplementarios() BEGIN:" );
        try{
           
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String codigoTramite = (String)gVO.getAtributo("codTramite");
            String numero = (String)gVO.getAtributo("numero");
            
            String desdeJsp=(String)gVO.getAtributo("desdeJsp");
            String consultaCampos=(String)gVO.getAtributo("consultaCampos");
            st3 = con.createStatement();
            sql3 = "SELECT TFE_COD,PLAZO_ACTIVADO FROM  E_TFE WHERE TFE_NUM='"
                    + numero + "'";
            rs3 = st3.executeQuery(sql3);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
            }
            while (rs3.next()) {
                codigoCampo = String.valueOf(rs3.getString("TFE_COD"));
                campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                m_Log.debug("campoActivo:::" + campoActivo);
                plazoActivo.setAtributo(codigoCampo, campoActivo);
            }
            
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs3);


            from = pca_cod + "," + pca_des + "," + pca_plt + "," + plt_url + "," + pca_tda + "," +
                   pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," + pca_rot + "," +
                   "CAMPO."+pca_activo + "," + pca_desplegable+",PCA_BLOQ, PLAZO_AVISO,PERIODO_PLAZO, PCA_GROUP,  PCA_POS_X, PCA_POS_Y, GRUPO.PCA_ORDER_GROUP";
            where = pca_mun + " = " + codMunicipio + " AND CAMPO." + pca_pro + " = '" + codProcedimiento + "' AND " +
                    "CAMPO."+pca_activo + " = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND PCA_OCULTO='NO'";
            String[] join = new String[8];
            join[0] = "E_PCA campo";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] =  pca_plt + "=e_plt." + plt_cod;
            join[4] = "LEFT";
            join[5] = "E_PCA_GROUP GRUPO";
            join[6] = "CAMPO.PCA_PRO=GRUPO.PCA_PRO  and PCA_GROUP=PCA_ID_GROUP";
            join[7] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"18","18","17","17","9","9"};
            if (params[0] != null && (params[0].equalsIgnoreCase("oracle"))) {
                sql += " ORDER BY GRUPO.PCA_order_group NULLS FIRST,18, 17, 9";
            } else {
                sql += " ORDER BY GRUPO.PCA_order_group,18, 17, 9";
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            
            Hashtable<String,ArrayList<EstructuraCampo>> contenedor=new Hashtable<String,ArrayList<EstructuraCampo>>();
            
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(pca_cod);
                tempCodCampo = codCampo;
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(pca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(pca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(pca_tda);
                tempCodTipoDato = codTipoDato;                
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
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String bloqueado = rs.getString("PCA_BLOQ");                
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
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
                }//if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo))
                if(codTipoDato != null && !"".equalsIgnoreCase(codTipoDato) && ("1".equalsIgnoreCase(codTipoDato) 
                                || "3".equalsIgnoreCase(codTipoDato)
                                || "8".equalsIgnoreCase(codTipoDato) 
                                || "9".equalsIgnoreCase(codTipoDato))){
                    eC.setTamanoVista("50");
                }else{
                    eC.setTamanoVista("100");
                }//
                String posX = String.valueOf(rs.getInt("PCA_POS_X"));
                String posY = String.valueOf(rs.getInt("PCA_POS_Y"));
                //if((posX != null && !posX.equalsIgnoreCase("0")) && (posY != null && !posY.equalsIgnoreCase("0"))){
                if((posX != null) && (posY != null)){
                    eC.setPosicionar(true);
                }//if((posX != null && !posX.equalsIgnoreCase("0")) && (posY != null && !posY.equalsIgnoreCase("0")))
                eC.setPosX(posX);
                eC.setPosY(posY);
                String desplegable = "";
                try {
                     
                    if (("3").equals(tempCodTipoDato)){
                        m_Log.debug("campoActivo:::" + (String)plazoActivo.getAtributo(tempCodCampo));
                        eC.setCampoActivo((String)plazoActivo.getAtributo(tempCodCampo));
                    }
                    
                        
                    if ( !rs.getString(pca_desplegable).equals(null)) {
                        desplegable = rs.getString(pca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                         Vector listaEstado = new Vector();
                        

                        ArrayList<EstructuraCampo> valoresDesplegables=contenedor.get(desplegable);
                        if(valoresDesplegables!=null)
                        {
                            
                            if(m_Log.isDebugEnabled()) m_Log.debug("ya tenemos guardados los valores para el desplegable: "+ desplegable);
                          
                           for(int i=0;i<valoresDesplegables.size();i++) 
                           {
                               EstructuraCampo estructura=valoresDesplegables.get(i);
                               listaCod.addElement(estructura.getCodCampo());
                               listaDesc.addElement(estructura.getDescCampo());
                               listaEstado.addElement(estructura.getEstadoValorCampo());
                           }
                            
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                            eC.setListaEstadoValorDesplegable(listaEstado);
                        }
                        else{ //No hay valores guardados para ese desplegable
                            st2 = con.createStatement();
                             sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", " + des_val_estado +" FROM E_DES_VAL WHERE " +
                                   des_val_campo +"='"+ desplegable + "' ORDER BY " + des_val_desc  + " ASC"; 
                            rs2 = st2.executeQuery(sql2);
                            if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios sal: " + sql2);
                            ArrayList<EstructuraCampo> valoresDevueltos=new ArrayList<EstructuraCampo>();
                            while (rs2.next()) {
                                
                                String desvalcod="'"+rs2.getString(des_val_cod)+"'";
                                String desvaldesc="'"+rs2.getString(des_val_desc)+"'";
                                String desvalestado="'"+rs2.getString(des_val_estado)+"'";
                                
                                listaCod.addElement(desvalcod);
                                listaDesc.addElement(desvaldesc);
                                listaEstado.addElement(desvalestado);
                              
                                 EstructuraCampo estructura=new EstructuraCampo();
                                 estructura.setCodCampo(desvalcod);
                                 estructura.setDescCampo(desvaldesc);
                                 estructura.setEstadoValorCampo(desvalestado);
                                 valoresDevueltos.add(estructura);
                            }
                            contenedor.put(desplegable, valoresDevueltos);
                            
                            SigpGeneralOperations.closeStatement(st2);
                            SigpGeneralOperations.closeResultSet(rs2);
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                            eC.setListaEstadoValorDesplegable(listaEstado);
                        }
                    }
                } catch (NullPointerException e){
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("PLANTILLA : " + eC.getCodPlantilla());
                lista.addElement(eC);
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);

           
       
            from = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", oad.convertir("E_TCA.TCA_TRA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null), oad.convertir("E_TCA.TCA_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)}) + " AS " + tca_cod + ",E_TCA.TCA_DES,E_TCA.TCA_PLT," + plt_url + ",E_TCA.TCA_TDA,E_TCA.TCA_ROT" +
                    ",E_TCA.TCA_VIS,E_TCA.TCA_TRA,E_TCA.TCA_ACTIVO," + tml_valor + "," + cro_ocu + "," + tca_desplegable+",TCA_OCULTO,TCA_BLOQ,"+ oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{"E_TCA_GROUP.TCA_ORDER_GROUP", "-1"})+" as ordenGrupo";
            where = "e_tca.tca_mun = " + codMunicipio + " AND e_tca.tca_pro   = '" + codProcedimiento + "' AND e_tca.tca_activo='SI'";
            String[] join1 = new String[17];
            join1[0] = "e_tml";
            join1[1] = "INNER";
            join1[2] = "e_tca";
            join1[3] = "e_tca.tca_mun =e_tml.tml_mun AND " +
                    "e_tca.tca_pro=e_tml.tml_pro and " +
                    "e_tca.tca_tra= e_tml.tml_tra and " +
                    "e_tca.tca_vis = 'S' ";
            if (codigoTramite != null) join[3] += " AND E_TCA.TCA_TRA <> " + codigoTramite;

            join1[4] = "INNER";
            join1[5] = "E_CRO";

            //String numero = (String)gVO.getAtributo("numero");

            join1[6] = "e_tca.tca_mun= e_cro.CRO_MUN AND " +
                       "e_tca.TCA_PRO  = e_cro.CRO_PRO AND " +
                       "e_tca.tca_TRA = e_cro.CRO_TRA AND " +
                       "e_cro.cro_num = '" + numero +"'";
            join1[7] = "INNER";
            join1[8] = "e_plt";
            join1[9] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join1[10] = "INNER";
            join1[11] = "e_tra";
            join1[12] = "e_tca." + tca_mun + "=e_tra." + tra_mun + " AND " +
                    "e_tca." + tca_pro + "=e_tra." + tra_pro + " AND " +
                    "e_tca." + tca_tra + "=e_tra." + tra_cod + " AND " +
                    "e_tra." + tra_fba + " IS NULL";

          
            join1[13]= "LEFT";
            join1[14]= " E_TCA_GROUP";
            join1[15]= " E_TCA.TCA_PRO=E_TCA_GROUP.TCA_PRO  "
                    + "and E_TCA.TCA_TRA=E_TCA_GROUP.TCA_TRA and "
                    + "E_TCA.PCA_GROUP=E_TCA_GROUP.TCA_ID_GROUP";
              
            // Para quitar la descripción del tramite
            join1[16] = "false";
         
            sql = oad.join(from,where,join1);

            sql = sql + " ORDER BY CRO_FEI,E_TRA.TRA_COU,ordenGrupo,e_tca.TCA_POS_Y,e_tca.TCA_POS_X, e_tca.TCA_NOR, e_cro.CRO_OCU";
            // Fin consulta modificada
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
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
                eC.setTamano("");
                eC.setMascara("");
                eC.setObligatorio("0");
                eC.setNumeroOrden("0");
                String rotulo = rs.getString(tca_rot);
                eC.setRotulo(rotulo);
                String soloLectura = "true";
                eC.setSoloLectura(soloLectura);
                String codTramite = rs.getString(tca_tra);
                eC.setCodTramite(codTramite);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                String descripcionTramite = rs.getString(tml_valor);
                eC.setDescripcionTramite(descripcionTramite);
                String ocurrencia = rs.getString(cro_ocu);
                eC.setOcurrencia(ocurrencia);
                String bloqueado = rs.getString("TCA_BLOQ");               
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
                String desplegable = "";
                try {
                    if ( !rs.getString(tca_desplegable).equals(null)) {
                        desplegable = rs.getString(tca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        st2 = con.createStatement();
                        sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", "+ des_val_estado +" FROM E_DES_VAL WHERE " +
                               des_val_campo +"='"+ desplegable + "'";
                        rs2 = st2.executeQuery(sql2);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                        while (rs2.next()) {
                            listaCod.addElement("'"+rs2.getString(des_val_cod)+"'");
                            listaDesc.addElement("'"+rs2.getString(des_val_desc)+"'");
                            listaEstado.addElement("'"+rs2.getString(des_val_estado)+"'");
                        }
                        SigpGeneralOperations.closeStatement(st2);
                        SigpGeneralOperations.closeResultSet(rs2);
                        eC.setListaCodDesplegable(listaCod);
                        eC.setListaDescDesplegable(listaDesc);
                         eC.setListaEstadoValorDesplegable(listaEstado);
                    }
                } catch (NullPointerException e){
                }
                eC.setCodAgrupacion("DVT");
                String visibleExpediente = rs.getString(tca_vis);
                if("S".equals(visibleExpediente)) {
                    String oculto = rs.getString("TCA_OCULTO");
                    if(("si".equals(desdeJsp))&& ("SI".equals(oculto)))
                    {
                      //Si viene de jsp y el campo esta oculto no se inserta el campo en la lista
                    }
                    else lista.addElement(eC);
                }
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);

        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        finally {
            
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeStatement(st2);
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeResultSet(rs2);
            SigpGeneralOperations.closeResultSet(rs3);
            return lista;
        }
    }
    
       
    
    public Vector cargaEstructuraAgrupacionCampos(GeneralValueObject gVO, AdaptadorSQL oad, Connection con) throws TechnicalException{
        
        if(m_Log.isDebugEnabled()) m_Log.debug("cargaEstructuraAgrupacionCampos() : BEGIN");
        Vector listaAgrupacionesCampo = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = new String();
        try{
            //Creamos el grupo por defecto en la lista de agrupaciones
            DefinicionAgrupacionCamposValueObject dacdfVO = new DefinicionAgrupacionCamposValueObject();
                dacdfVO.setCodAgrupacion("DEF");
                dacdfVO.setDescAgrupacion("");
                dacdfVO.setOrdenAgrupacion(0);
            listaAgrupacionesCampo.add(dacdfVO);
            
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");            
            /*
            sql += "Select PCA_ID_GROUP, PCA_DESC_GROUP, PCA_ORDER_GROUP From E_PCA_GROUP where PCA_PRO = '" + codProcedimiento + "'"
                    + " and PCA_ACTIVE = 'SI' "
                    + " order by PCA_ORDER_GROUP ASC";
            */
            
            sql += "Select PCA_ID_GROUP, PCA_DESC_GROUP, PCA_ORDER_GROUP From E_PCA_GROUP where PCA_PRO =?"
                 + " and PCA_ACTIVE = 'SI' "
                 + " order by PCA_ORDER_GROUP ASC";
            
            ps = con.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.setString(1,codProcedimiento);
            rs = ps.executeQuery();
            
            while(rs.next()){
                DefinicionAgrupacionCamposValueObject dacVO = new DefinicionAgrupacionCamposValueObject();
                dacVO.setCodAgrupacion(rs.getString("PCA_ID_GROUP"));
                dacVO.setDescAgrupacion(rs.getString("PCA_DESC_GROUP"));
                dacVO.setOrdenAgrupacion(rs.getInt("PCA_ORDER_GROUP"));
                listaAgrupacionesCampo.add(dacVO);
            }
                        
            Boolean cargarGrupoVisiblesTramite = cargarGrupoCamposTramiteVisiblesExpediente(codProcedimiento, con);
            if(cargarGrupoVisiblesTramite){
                DefinicionAgrupacionCamposValueObject dcvtVO = new DefinicionAgrupacionCamposValueObject();
                dcvtVO.setCodAgrupacion("DVT");
                dcvtVO.setDescAgrupacion("");
                dcvtVO.setOrdenAgrupacion(listaAgrupacionesCampo.size()+1);
                listaAgrupacionesCampo.add(dcvtVO);
            }
        }catch (SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
            
            if(m_Log.isDebugEnabled()) m_Log.debug("cargaEstructuraAgrupacionCampos() : END");
            
            return listaAgrupacionesCampo;
            
        } //try-catch--finally
    }//cargaEstructuraAgrupacionCampos
    
    
    private Boolean cargarGrupoCamposTramiteVisiblesExpediente(String codProcedimiento,Connection con) throws TechnicalException{
        if(m_Log.isDebugEnabled()) m_Log.debug("cargarGrupoCamposTramiteVisiblesExpediente() : BEGIN");
        Boolean cargar = false;
        PreparedStatement ps = null;
        ResultSet rs = null;        
        String sql = new String();
        try{            
            /*
            sql += "Select TCA_DES from E_TCA where TCA_PRO = '" + codProcedimiento + "' and TCA_ACTIVO='SI'"
                    + " and e_tca.tca_vis  = 'S'";
            */            
            sql += "Select TCA_DES from E_TCA where TCA_PRO =? and TCA_ACTIVO=?"
                 + " and e_tca.tca_vis  = ?";
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            int i = 1;
            ps = con.prepareStatement(sql);            
            ps.setString(i++,codProcedimiento);
            ps.setString(i++,"SI");
            ps.setString(i++,"S");
            
            rs = ps.executeQuery();
            while(rs.next()){
                cargar = true;
            }
        }catch (SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("cargarGrupoCamposTramiteVisiblesExpediente() : END");            
        } 
        return cargar;
    }//cargarGrupoPorDefecto

    public Vector cargaEstructuraDatosSuplementarios(GeneralValueObject gVO,  AdaptadorSQL oad, Connection con) throws TechnicalException {
        
      Statement st = null, st3 = null;
      ResultSet rs = null, rs3 = null;
      String sql = "", sql3 = "";
      String from = "";
      String where = "";
      Vector lista = new Vector();
      String campoActivo = "";

      m_Log.info("**************------------->cargaEstructuraDatosSuplementarios<----------------------*****************");
      
      try{
      st = con.createStatement();

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String desdeJsp=(String)gVO.getAtributo("desdeJsp");
      String numero = (String)gVO.getAtributo("numero");
      boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            

      from = pca_cod + "," + pca_des + "," + pca_plt + "," + plt_url + "," + pca_tda + "," +
            pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," + pca_rot + "," + pca_activo+",PCA_OCULTO,PCA_BLOQ, PLAZO_AVISO, PERIODO_PLAZO";
      where = pca_mun + " = " + codMunicipio + " AND " + pca_pro + " = '" + codProcedimiento + "'";
      String[] join = new String[5];
      join[0] = "E_PCA";
      join[1] = "INNER";
      join[2] = "e_plt";
      join[3] = "e_pca." + pca_plt + "=e_plt." + plt_cod;
      join[4] = "false";

      sql = oad.join(from,where,join);
      String parametros[] = {"9","9"};
      sql += oad.orderUnion(parametros);

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = st.executeQuery(sql);
      while(rs.next()){
        EstructuraCampo eC = new EstructuraCampo();
        String codCampo = rs.getString(pca_cod);
        eC.setCodCampo(codCampo);
        String descCampo = rs.getString(pca_des);
        eC.setDescCampo(descCampo);
        String codPlantilla = rs.getString(pca_plt);
        eC.setCodPlantilla(codPlantilla);
        String urlPlantilla = rs.getString(plt_url);
        eC.setURLPlantilla(urlPlantilla);
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
        String soloLectura = "false";
        eC.setSoloLectura(soloLectura);

        String oculto = rs.getString("PCA_OCULTO");

        if(("si".equals(desdeJsp))&&("SI".equals(oculto)))
        {

        }else lista.addElement(eC);
        //CASO TIPO FECHA
        if (("3").equals(codTipoDato)){
             m_Log.debug("TIPO DE DATO FECHA...");
             m_Log.debug("CodCampo::" + codCampo);
             st3 = con.createStatement();
             if (expHistorico)
                 sql3 = "SELECT PLAZO_ACTIVADO FROM  HIST_E_TFE WHERE TFE_COD ='";
             else
                 sql3 = "SELECT PLAZO_ACTIVADO FROM  E_TFE WHERE TFE_COD ='";
                 
             sql3 += codCampo + "' AND TFE_NUM='"+
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
        
        
      }
      SigpGeneralOperations.closeStatement(st);
      SigpGeneralOperations.closeResultSet(rs);

      from = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[] {"'T'", oad.convertir(
             tca_tra, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,""), tca_cod }) +
             " AS " + tca_cod + "," + tca_des + "," + tca_plt + "," + plt_url + "," + tca_tda + "," + tca_rot +
             "," + tca_vis + "," + tca_tra + "," + tca_activo + "," + tml_valor + ",CRO_OCU ";
      where = tca_mun + " = " + codMunicipio + " AND " + tca_pro + " = '" + codProcedimiento + "'";
      String[] join1 = new String[8];
      if (expHistorico)
          join1[0] = "e_tml, hist_e_cro, E_TCA";
      else
          join1[0] = "e_tml, e_cro, E_TCA";
      
      
      join1[1] = "INNER";
      join1[2] = "e_plt";
      join1[3] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
      join1[4] = "INNER";
      join1[5] = "e_tra";
      join1[6] = "e_tca." + tca_mun + "=e_tra." + tra_mun + " AND " +
                           "e_tca." + tca_pro + "=e_tra." + tra_pro + " AND " +
                           "e_tca." + tca_tra + "=e_tra." + tra_cod + " AND " +
                           "e_tra." + tra_fba + " IS NULL";
      
      join1[7] = "false";
      sql = oad.join(from,where,join1);

          sql = sql + " and e_tca.tca_mun =e_tml.tml_mun AND e_tca.tca_pro=e_tml.tml_pro and e_tca.tca_tra= e_tml.tml_tra";
          //String numero = (String)gVO.getAtributo("numero");
          sql = sql + " AND e_tca.tca_vis = 'S'";
          sql = sql + " AND e_tca.tca_mun= CRO_MUN AND e_tca.TCA_PRO  = CRO_PRO AND e_tca.tca_TRA = CRO_TRA";
          sql = sql + " AND cro_num = '" + numero + "' ORDER BY  e_tca.TCA_TRA, e_tca.TCA_POS_Y,e_tca.TCA_POS_X,e_tca.TCA_NOR, CRO_OCU";

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      st = con.createStatement();
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
        eC.setTamano("");
        eC.setMascara("");
        eC.setObligatorio("0");
        eC.setNumeroOrden("0");
        String rotulo = rs.getString(tca_rot);
        eC.setRotulo(rotulo);
        String soloLectura = "true";
        eC.setSoloLectura(soloLectura);
        String codTramite = rs.getString(tca_tra);
        eC.setCodTramite(codTramite);
        String activo = rs.getString(tca_activo);
        eC.setActivo(activo);
        String descripcionTramite = rs.getString(tml_valor);
        eC.setDescripcionTramite(descripcionTramite);
        String ocurrencia = rs.getString("CRO_OCU");
        eC.setOcurrencia(ocurrencia);
        String visibleExpediente = rs.getString(tca_vis);
        if("S".equals(visibleExpediente)) {
          lista.addElement(eC);
        }
      }
      SigpGeneralOperations.closeResultSet(rs);
      SigpGeneralOperations.closeStatement(st);

    }catch (BDException e){
      e.printStackTrace();
      if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }catch (SQLException e){
      e.printStackTrace();
      if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
          
        try{
          if(rs!=null) rs.close();
          if(st!=null) st.close();
          if(rs3!=null) rs3.close();
          if(st3!=null) st3.close();
          
        }catch(SQLException e){
            m_Log.error("Error al cerrar recursos asociados a la conexión de la BBDD: " + e.getMessage());
        }
    }
    return lista;
  }

    public Vector cargaEstructuraInteresados(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();

        try{
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

        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            return lista;
        }
    }

     public Vector cargaValoresDatosSuplementarios(GeneralValueObject gVO,Vector eCs, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Vector lista = new Vector();
          
        CamposFormulario cF1 = null;
        CamposFormulario cF2 = null;
        CamposFormulario cF3 = null;
        CamposFormulario cF4 = null;
        CamposFormulario cF5 = null;
        CamposFormulario cF6 = null;
        CamposFormulario cF7 = null;
        CamposFormulario cF8 = null;
        CamposFormulario cF9 = null;
        CamposFormulario cF10 = null;
        
       

        try{ 
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection(); 

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                String campoActivo = eC.getCampoActivo();
                m_Log.debug("campoActivo:"  + campoActivo);
                if ("1".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF1 == null) {
                            cF1 = DatosSuplementariosDAO.getInstance().getValoresNumericos(oad, con, gVO);
                        }

                    } else {

                        cF1 = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                    lista.addElement(cF1);

                } else if ("2".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF2 == null) {
                            cF2 = DatosSuplementariosDAO.getInstance().getValoresTexto(oad, con, gVO);
                        }

                    } else {

                        cF2 = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad, con, gVO, codTramite, ocurrencia);

                    }
                    lista.addElement(cF2);

                } else if ("3".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF3 == null) {
                            cF3 = DatosSuplementariosDAO.getInstance().getValoresFecha(oad, con, gVO, mascara);
                        }

                    } else {

                        cF3 = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad, con, gVO, mascara, codTramite, ocurrencia);

                    }
                    lista.addElement(cF3);

                } else if ("4".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF4 == null) {
                            cF4 = DatosSuplementariosDAO.getInstance().getValoresTextoLargo(oad, con, gVO);
                        }

                    } else {

                        cF4 = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                    lista.addElement(cF4);

                } else if ("5".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF5 == null) {
                            cF5 = DatosSuplementariosDAO.getInstance().getValoresFichero(oad, con, gVO);
                        }

                    } else {

                        cF5 = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad, con, gVO, codTramite, ocurrencia);
                       
                    }
                    lista.addElement(cF5);

                } else if ("6".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF6 == null) {
                            cF6 = DatosSuplementariosDAO.getInstance().getValoresDesplegable(oad, con, gVO);
                        }

                    } else {

                        cF6 = DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                    lista.addElement(cF6);

                } else if ("8".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF8 == null) {
                            cF8 = DatosSuplementariosDAO.getInstance().getValoresNumericosCal(oad, con, gVO);
                        }

                    } else {

                        cF8 = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad, con, gVO, codTramite, ocurrencia);

                    }
                    lista.addElement(cF8);

                } else if ("9".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF9 == null) {
                            cF9 = DatosSuplementariosDAO.getInstance().getValoresFechaCal(oad, con, gVO, mascara);
                        }


                    } else {

                        cF9 = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad, con, gVO, mascara, codTramite, ocurrencia);
                    }

                    lista.addElement(cF9);

                } else if ("10".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF10 == null) {
                            cF10 = DatosSuplementariosDAO.getInstance().getValoresDesplegableExt(oad, con, gVO);
                        }


                    } else {

                        cF10 = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                     lista.addElement(cF10);
                }
               

            }

        }catch (BDException e){ 
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.devolverConexion(oad, con);                                
        }
        return lista; 
    }

     
     
    public Vector cargaValoresDatosSuplementarios(GeneralValueObject gVO,Vector eCs,AdaptadorSQLBD oad,Connection con) throws TechnicalException {        
        Vector lista = new Vector();          
        CamposFormulario cF1 = null;
        CamposFormulario cF2 = null;
        CamposFormulario cF3 = null;
        CamposFormulario cF4 = null;
        CamposFormulario cF5 = null;
        CamposFormulario cF6 = null;
        CamposFormulario cF7 = null;
        CamposFormulario cF8 = null;
        CamposFormulario cF9 = null;
        CamposFormulario cF10 = null;

        try{ 

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                String campoActivo = eC.getCampoActivo();                
                if ("1".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF1 == null) {
                            cF1 = DatosSuplementariosDAO.getInstance().getValoresNumericos(oad, con, gVO);
                        }

                    } else {

                        cF1 = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                    lista.addElement(cF1);

                } else if ("2".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF2 == null) {
                            cF2 = DatosSuplementariosDAO.getInstance().getValoresTexto(oad, con, gVO);
                        }

                    } else {

                        cF2 = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad, con, gVO, codTramite, ocurrencia);

                    }
                    lista.addElement(cF2);

                } else if ("3".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF3 == null) {
                            cF3 = DatosSuplementariosDAO.getInstance().getValoresFecha(oad, con, gVO, mascara);
                        }

                    } else {

                        cF3 = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad, con, gVO, mascara, codTramite, ocurrencia);

                    }
                    lista.addElement(cF3);

                } else if ("4".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF4 == null) {
                            cF4 = DatosSuplementariosDAO.getInstance().getValoresTextoLargo(oad, con, gVO);
                        }

                    } else {

                        cF4 = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                    lista.addElement(cF4);

                } else if ("5".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF5 == null) {
                            cF5 = DatosSuplementariosDAO.getInstance().getValoresFichero(oad, con, gVO);
                        }

                    } else {

                        cF5 = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad, con, gVO, codTramite, ocurrencia);

                    }
                    lista.addElement(cF5);

                } else if ("6".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF6 == null) {
                            cF6 = DatosSuplementariosDAO.getInstance().getValoresDesplegable(oad, con, gVO);
                        }

                    } else {

                        cF6 = DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                    lista.addElement(cF6);

                } else if ("8".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF8 == null) {
                            cF8 = DatosSuplementariosDAO.getInstance().getValoresNumericosCal(oad, con, gVO);
                        }

                    } else {

                        cF8 = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad, con, gVO, codTramite, ocurrencia);

                    }
                    lista.addElement(cF8);

                } else if ("9".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF9 == null) {
                            cF9 = DatosSuplementariosDAO.getInstance().getValoresFechaCal(oad, con, gVO, mascara);
                        }


                    } else {

                        cF9 = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad, con, gVO, mascara, codTramite, ocurrencia);
                    }

                    lista.addElement(cF9);

                } else if ("10".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF10 == null) {
                            cF10 = DatosSuplementariosDAO.getInstance().getValoresDesplegableExt(oad, con, gVO);
                        }


                    } else {

                        cF10 = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad, con, gVO, codTramite, ocurrencia);
                    }
                     lista.addElement(cF10);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lista; 
    }

     
     
     
     
     
     
   

    public Vector cargaValoresDatosSuplEtiquetas(GeneralValueObject gVO,Vector eCs, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Vector lista = new Vector();
        CamposFormulario cF = null;

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                if("1".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresNumericos(oad,con,gVO);
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad,con,gVO,codTramite,ocurrencia);
                    }
                } else if("2".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresTexto(oad,con,gVO);
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad,con,gVO,codTramite,ocurrencia);
                    }
                } else if("3".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresFecha(oad,con,gVO,mascara);
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad,con,gVO,mascara,codTramite,ocurrencia);
                    }
                } else if("4".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresTextoLargo(oad,con,gVO);
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad,con,gVO,codTramite,ocurrencia);
                    }
                } else if("5".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresFichero(oad,con,gVO);
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad,con,gVO,codTramite,ocurrencia);
                    }
                } else if("6".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValsDespEtiquetas(oad,con,gVO);
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValsDespTramiteEtiquetas(oad,con,gVO,codTramite,ocurrencia);
                    }
                } else if("8".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresNumericosCal(oad,con,gVO); 
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad,con,gVO,codTramite,ocurrencia); 
                    }
                } else if("9".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresFechaCal(oad,con,gVO,mascara); 
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad,con,gVO,mascara,codTramite,ocurrencia); 
                    }
                } else if("10".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        cF = DatosSuplementariosDAO.getInstance().getValoresDesplegableExt(oad,con,gVO); 
                    } else {
                        cF = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad,con,gVO,codTramite,ocurrencia);
                    }
                }
                lista.addElement(cF);
            }

        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return lista;
        }
    }

    
    public Vector cargaValoresDatosSuplementarios(GeneralValueObject gVO, Vector eCs, AdaptadorSQL oad, Connection con)
            throws TechnicalException {

        Vector lista = new Vector();
        CamposFormulario cF = null;
        AdaptadorSQLBD dbAdapter = (AdaptadorSQLBD)oad;
        DatosSuplementariosDAO dao = DatosSuplementariosDAO.getInstance();
        
        CamposFormulario CAMPOS_NUMERICOS_EXPEDIENTE = null;
        CamposFormulario CAMPOS_TEXTO_EXPEDIENTE = null;
        CamposFormulario CAMPOS_FECHA_EXPEDIENTE = null;
        CamposFormulario CAMPOS_TEXTO_LARGO_EXPEDIENTE = null;
        CamposFormulario CAMPOS_FICHERO_EXPEDIENTE = null;
        CamposFormulario CAMPOS_DESPLEGABLE_EXPEDIENTE = null;
        CamposFormulario CAMPOS_NUMERICOS_CALCULADOS_EXPEDIENTE = null;
        CamposFormulario CAMPOS_FECHA_CALCULADOS_EXPEDIENTE = null;
        CamposFormulario CAMPOS_DESPLEGABLE_EXTERNO_EXPEDIENTE = null;
        
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
                EstructuraCampo eC = (EstructuraCampo) eCs.elementAt(i);
                String strCodTipoDato = eC.getCodTipoDato();
          String mascara = eC.getMascara();
          String codTramite = eC.getCodTramite();
          String ocurrencia = eC.getOcurrencia();
                int codTipoDato = Integer.parseInt(strCodTipoDato);

            if(codTramite == null || "".equals(codTramite)) {
                    switch (codTipoDato) {
                        case 1:
                            if(CAMPOS_NUMERICOS_EXPEDIENTE==null){
                                CAMPOS_NUMERICOS_EXPEDIENTE = dao.getValoresNumericos(dbAdapter, con, gVO);
                                cF = CAMPOS_NUMERICOS_EXPEDIENTE;
                            }else
                                cF = CAMPOS_NUMERICOS_EXPEDIENTE;
                            break;
                        case 2:
                            if(CAMPOS_TEXTO_EXPEDIENTE==null){
                                CAMPOS_TEXTO_EXPEDIENTE = dao.getValoresTexto(dbAdapter, con, gVO);
                                cF = CAMPOS_TEXTO_EXPEDIENTE;
                            }else
                                cF = CAMPOS_TEXTO_EXPEDIENTE;
                            
                            break;
                        case 3:
                            if(CAMPOS_FECHA_EXPEDIENTE==null){
                                CAMPOS_FECHA_EXPEDIENTE = dao.getValoresFecha(dbAdapter, con, gVO, mascara);
                                cF = CAMPOS_FECHA_EXPEDIENTE;
                            }else
                                cF = CAMPOS_FECHA_EXPEDIENTE;
                            break;
                        case 4:
                            
                            if(CAMPOS_TEXTO_LARGO_EXPEDIENTE==null){
                                CAMPOS_TEXTO_LARGO_EXPEDIENTE = dao.getValoresTextoLargo(dbAdapter, con, gVO);
                                cF = CAMPOS_TEXTO_LARGO_EXPEDIENTE;
                            }else
                                cF = CAMPOS_TEXTO_LARGO_EXPEDIENTE;
                            
                            break;
                        case 5:
                            
                            if(CAMPOS_FICHERO_EXPEDIENTE==null){
                                CAMPOS_FICHERO_EXPEDIENTE = dao.getValoresFichero(dbAdapter, con, gVO);
                                cF = CAMPOS_FICHERO_EXPEDIENTE;
                            }else
                                cF = CAMPOS_FICHERO_EXPEDIENTE;
                            
                            break;
                        case 6:
                            if(CAMPOS_DESPLEGABLE_EXPEDIENTE==null){
                                CAMPOS_DESPLEGABLE_EXPEDIENTE = dao.getValoresDesplegable(dbAdapter, con, gVO);
                                cF = CAMPOS_DESPLEGABLE_EXPEDIENTE;
                            }else
                                cF = CAMPOS_DESPLEGABLE_EXPEDIENTE;
                            break;
                        case 8:
                            if(CAMPOS_NUMERICOS_CALCULADOS_EXPEDIENTE==null){
                                CAMPOS_NUMERICOS_CALCULADOS_EXPEDIENTE = dao.getValoresNumericosCal(dbAdapter, con, gVO);
                                cF = CAMPOS_NUMERICOS_CALCULADOS_EXPEDIENTE;
                            }else
                                cF = CAMPOS_NUMERICOS_CALCULADOS_EXPEDIENTE;
                            
                            break;
                        case 9:
                            if(CAMPOS_FECHA_CALCULADOS_EXPEDIENTE==null){
                                CAMPOS_FECHA_CALCULADOS_EXPEDIENTE = dao.getValoresFechaCal(dbAdapter, con, gVO, mascara);
                                cF = CAMPOS_FECHA_CALCULADOS_EXPEDIENTE;
                            }else
                                cF = CAMPOS_FECHA_CALCULADOS_EXPEDIENTE;
                            
                            break;
                        case 10:
                            if(CAMPOS_DESPLEGABLE_EXTERNO_EXPEDIENTE==null){
                                CAMPOS_DESPLEGABLE_EXTERNO_EXPEDIENTE = dao.getValoresDesplegableExt(dbAdapter, con, gVO);
                                cF = CAMPOS_DESPLEGABLE_EXTERNO_EXPEDIENTE;
                            }else
                                cF = CAMPOS_DESPLEGABLE_EXTERNO_EXPEDIENTE;                            
                            break;
            }
            } else {
                    switch (codTipoDato) {
                        case 1:
                            if(CAMPOS_NUMERICOS_TRAMITE==null){
                                CAMPOS_NUMERICOS_TRAMITE = dao.getValoresNumericosTramite(dbAdapter, con, gVO, codTramite, ocurrencia);
                                cF = CAMPOS_NUMERICOS_TRAMITE;
                            }else
                                cF = CAMPOS_NUMERICOS_TRAMITE;
                            break;
                        case 2:
                            if(CAMPOS_TEXTO_TRAMITE==null){                                
                                CAMPOS_TEXTO_TRAMITE = dao.getValoresTextoTramite(dbAdapter, con, gVO, codTramite, ocurrencia);
                                cF = CAMPOS_TEXTO_TRAMITE;
                            }else
                                cF = CAMPOS_TEXTO_TRAMITE;
                            break;
                        case 3:
                            if(CAMPOS_FECHA_TRAMITE==null){
                                CAMPOS_FECHA_TRAMITE = dao.getValoresFechaTramite(dbAdapter, con, gVO, mascara, codTramite, ocurrencia);
                                cF = CAMPOS_FECHA_TRAMITE;
                            }else
                                cF = CAMPOS_FECHA_TRAMITE;
                            
                            break;
                        case 4:
                            if(CAMPOS_TEXTO_LARGO_TRAMITE==null){
                                CAMPOS_TEXTO_LARGO_TRAMITE = dao.getValoresTextoLargoTramite(dbAdapter, con, gVO, codTramite, ocurrencia);
                                cF = CAMPOS_TEXTO_LARGO_TRAMITE;
                            }else
                                cF = CAMPOS_TEXTO_LARGO_TRAMITE;
                            break;
                        case 5:
                            if(CAMPOS_FICHERO_TRAMITE==null){
                                CAMPOS_FICHERO_TRAMITE = dao.getValoresFicheroTramite(dbAdapter, con, gVO, codTramite, ocurrencia);
                                cF = CAMPOS_FICHERO_TRAMITE;
                            }else
                                cF = CAMPOS_FICHERO_TRAMITE;
                            
                            break;
                        case 6:
                            if(CAMPOS_DESPLEGABLE_TRAMITE==null){
                                CAMPOS_DESPLEGABLE_TRAMITE = dao.getValoresDesplegableTramite(dbAdapter, con, gVO, codTramite, ocurrencia);
                                cF = CAMPOS_DESPLEGABLE_TRAMITE;
                            }else
                                cF = CAMPOS_DESPLEGABLE_TRAMITE;
                            
                            break;
                        case 8:
                            if(CAMPOS_NUMERICOS_CALCULADOS_TRAMITE==null){
                                CAMPOS_NUMERICOS_CALCULADOS_TRAMITE = dao.getValoresNumericosTramiteCal(dbAdapter, con, gVO, codTramite, ocurrencia);
                                cF= CAMPOS_NUMERICOS_CALCULADOS_TRAMITE;
                            }else
                                cF= CAMPOS_NUMERICOS_CALCULADOS_TRAMITE;
                            break;
                        case 9:
                            if(CAMPOS_FECHA_CALCULADOS_TRAMITE==null){
                                CAMPOS_FECHA_CALCULADOS_TRAMITE = dao.getValoresFechaTramiteCal(dbAdapter, con, gVO, mascara, codTramite, ocurrencia);
                                cF = CAMPOS_FECHA_CALCULADOS_TRAMITE;
                            }else
                                cF = CAMPOS_FECHA_CALCULADOS_TRAMITE;
                            break;
                        case 10:
                            if(CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE==null){
                                CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE = dao.getValoresDesplegableExtTramite(dbAdapter, con, gVO, codTramite, ocurrencia);
                                cF = CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE;
                            }else
                                cF = CAMPOS_DESPLEGABLE_EXTERNO_TRAMITE;
                            
                            break;
                }
          }

          lista.addElement(cF);
        }

      }catch (Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
          return lista;
      }
    
    


    public GeneralValueObject cargaValoresFicheros(GeneralValueObject gVO,Vector eCs, String[] params) throws TechnicalException {

        GeneralValueObject lista = new GeneralValueObject();

        m_Log.debug("******* cargaValoresFicheros **********");                 

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");

            for(int i=0;i<eCs.size();i++) 
            {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                m_Log.debug("cargaValoresFicheros.codcampo original: " + codCampo);
                m_Log.debug("cargaValoresFicheros.ocurrencia original: " + ocurrencia);
                
                byte[] fichero;
                if("5".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        fichero=DatosSuplementariosDAO.getInstance().getFichero(codCampo,codMunicipio,ejercicio,numero,params);
                    } else {
                        fichero=DatosSuplementariosDAO.getInstance().getFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,params,codTramite);
                        codCampo=codCampo+"_"+ocurrencia;
                    }
                    m_Log.debug(".................T"+ codTramite +"T...........Procesado FICHERO ... " + fichero);
                    m_Log.debug("---- Añadiendo campo con código: " + codCampo);                    
                    // Se guarda el fichero
                    lista.setAtributo(codCampo,fichero);
                }
            }
 
            return lista;        
    }
    
    
    
    
    public GeneralValueObject cargaValoresFicheros(GeneralValueObject gVO,Vector eCs,Connection con) throws TechnicalException {

        GeneralValueObject lista = new GeneralValueObject();

        m_Log.debug("******* cargaValoresFicheros **********");                 

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");

            for(int i=0;i<eCs.size();i++) 
            {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                m_Log.debug("cargaValoresFicheros.codcampo original: " + codCampo);
                m_Log.debug("cargaValoresFicheros.ocurrencia original: " + ocurrencia);
                
                byte[] fichero;
                if("5".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        fichero=DatosSuplementariosDAO.getInstance().getFichero(codCampo,codMunicipio,ejercicio,numero,con);
                    } else {
                        fichero=DatosSuplementariosDAO.getInstance().getFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,con,codTramite);
                        codCampo=codCampo+"_"+ocurrencia;
                    }
                    m_Log.debug(".................T"+ codTramite +"T...........Procesado FICHERO ... " + fichero);
                    m_Log.debug("---- Añadiendo campo con código: " + codCampo);                    
                    // Se guarda el fichero
                    lista.setAtributo(codCampo,fichero);
                }
            }
 
            return lista;        
    }
    
    
    

    public GeneralValueObject cargaTiposFicheros(GeneralValueObject gVO,Vector eCs, String[] params) throws TechnicalException {

        GeneralValueObject lista = new GeneralValueObject();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();                
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                m_Log.debug("----------------------------O"+ocurrencia);
                String tipo;
                m_Log.debug("................."+codCampo+"...........Coidog campo ... ");
                if("5".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        tipo=DatosSuplementariosDAO.getInstance().getTipoFichero(codCampo,codMunicipio,ejercicio,numero,params);
                    } else {
                        //codCampo="T"+codTramite+codCampo;
                        tipo=DatosSuplementariosDAO.getInstance().getTipoFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,params,codTramite);
                        codCampo=codCampo+"_"+ocurrencia;
                    }
                    lista.setAtributo(codCampo,tipo);
                }
            }
            
            return lista;
        
    }
     
    
    
    
    public GeneralValueObject cargaTiposFicheros(GeneralValueObject gVO,Vector eCs, Connection con) throws TechnicalException {

        GeneralValueObject lista = new GeneralValueObject();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();                
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                m_Log.debug("----------------------------O"+ocurrencia);
                String tipo;
                m_Log.debug("................."+codCampo+"...........Coidog campo ... ");
                if("5".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        tipo=DatosSuplementariosDAO.getInstance().getTipoFichero(codCampo,codMunicipio,ejercicio,numero,con);
                    } else {                        
                        tipo=DatosSuplementariosDAO.getInstance().getTipoFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,con,codTramite);
                        codCampo=codCampo+"_"+ocurrencia;
                    }
                    lista.setAtributo(codCampo,tipo);
                }
            }
            
            return lista;
        
    }
    
    
    
     public GeneralValueObject cargaNombreFicheros(GeneralValueObject gVO,Vector eCs, String[] params) throws TechnicalException {

        GeneralValueObject lista = new GeneralValueObject();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();
                String mascara = eC.getMascara();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                m_Log.debug("----------------------------O"+ocurrencia);
                String tipo;
                m_Log.debug("................."+codCampo+"...........Coidog campo ... ");
                if("5".equals(codTipoDato)) {
                    if(codTramite == null || "".equals(codTramite)) {
                        tipo=DatosSuplementariosDAO.getInstance().getNombreFichero(codCampo,codMunicipio,ejercicio,numero,params);
                    } else {
                        //codCampo="T"+codTramite+codCampo;
                        tipo=DatosSuplementariosDAO.getInstance().getNombreFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,params,codTramite);
                        codCampo=codCampo+"_"+ocurrencia;
                    }
                    lista.setAtributo(codCampo,tipo);
                }
            }
            return lista;
    }
     

     
    /**
     * Recupera el nombre y la longitud de los ficheros correspondientes
     * @param gVO: Objeto de tipo GeneralValueObject
     * @param eCs: Estructura de los campos suplementarios del expediente
     * @param params: Parámetros de conexión a la BBDD
     * @return Se retorna un GeneralValueObject con el nombre y longitud del fichero
     * @throws TechnicalException RE
     */ 
        public Hashtable<String,GeneralValueObject> cargaNombreLongitudFicheros(GeneralValueObject gVO,Vector eCs, Connection con) throws TechnicalException {
        
        Hashtable<String,GeneralValueObject> salida = new Hashtable<String, GeneralValueObject>();        
        GeneralValueObject nombres    = new GeneralValueObject();
        GeneralValueObject longitudes = new GeneralValueObject();
        GeneralValueObject tipos = new GeneralValueObject();
        
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");

        try{
            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();
                String mascara = eC.getMascara();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();
                m_Log.debug("----------------------------Código trámite: "+ codTramite);
                m_Log.debug("----------------------------Ocurrencia trámite: "+ocurrencia);
                String tipo;
                m_Log.debug("................."+codCampo+"...........Codigo campo ... ");

                GeneralValueObject datos = null;
                String nombre = null;
                int longitud = 0;

                CampoSuplementarioFicheroVO campo = null;

                if("5".equals(codTipoDato)) {

                    if(codTramite == null || "".equals(codTramite)) {
                        campo  = DatosSuplementariosDAO.getInstance().getNombreLongitudFichero(codCampo,codMunicipio,ejercicio,numero,con);                    
                    } else {
                        campo    = DatosSuplementariosDAO.getInstance().getNombreLongitudFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,codTramite,con);
                        codCampo = codCampo+ "_" + ocurrencia;
                    }

                    nombres.setAtributo(codCampo,campo.getNombreFichero());
                    longitudes.setAtributo(codCampo,(Integer)campo.getLongitudFichero());                
                    tipos.setAtributo(codCampo,(String)campo.getTipoMime());
                }
           }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        salida.put("NOMBRES",nombres);
        salida.put("LONGITUDES",longitudes);
        salida.put("TIPOS",tipos);
                
       return salida;
    }     
        
    public boolean tieneExpedientesPendientes(String codMunicipio, String ejercicio, String numero, String [] params)
    throws AnotacionRegistroException, TechnicalException,TramitacionException {
    	
    	m_Log.debug("Dentro de FichaExpedienteDAO : tieneExpedientesRelacionados");
    	
    	boolean resultado = false; 
    	Connection con = null;
    	Statement st = null;
    	ResultSet rs = null;
    	String sql = "";
    	int cont = 0;
        AdaptadorSQLBD oad = null;
    	
    	try{
    		oad = new AdaptadorSQLBD(params);
    		con = oad.getConnection();
    		st = con.createStatement();

                
    		sql = "SELECT " + rex_munr + "," + rex_ejer + "," + rex_numr + " FROM E_REX WHERE " +
    		rex_mun + "=" + codMunicipio + " AND " + rex_eje + "=" + ejercicio +
    		" AND " + rex_num + "='" + numero + "'";
                
    		if (m_Log.isDebugEnabled()) m_Log.debug("getExpedientesRelacionados : " + sql);
    		rs = st.executeQuery(sql);
                
            while(rs.next()){
            	cont++;            	
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            if (m_Log.isDebugEnabled()) m_Log.debug("Numero de filas obtenidas en select de tieneExpedientesRelacionados son : " + cont);            
    	
        }catch (Exception e) {
    		     resultado = false;
    		     e.printStackTrace();
    		     m_Log.error(e.getMessage());
    		     throw new AnotacionRegistroException(m_ConfigError.getString("Error.FichaExpedienteDAO.tieneExpedientesRelacionados"), e);
    	}finally {
            SigpGeneralOperations.devolverConexion(oad, con);
    	}
    	if (cont > 0)
    		return resultado = true;
    	else
    		return resultado = false;
    }
    
    
    
    

    /**
     * Comprueba si un expediente tiene expedientes relacionados
     * @param codMunicipio: Código del municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número del expediente
     * @param con: Conexión
     * @return Un boolean
     * @throws AnotacionRegistroException
     * @throws TechnicalException
     * @throws TramitacionException Cor
     */
    public boolean tieneExpedientesRelacionados(String codMunicipio, String ejercicio, String numero, Connection con) throws AnotacionRegistroException, TechnicalException,TramitacionException {    	    	
    	boolean resultado = false;     	
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	String sql = "";
    	int cont = 0;        
    	
    	try{
                             
            sql = "SELECT REX_NUMR " +
                  "FROM E_REX " +
                  "WHERE REX_MUN=? AND REX_EJE=? AND REX_NUM=?";
            
            if (m_Log.isDebugEnabled()) m_Log.debug("tieneExpedientesRelacionados : " + sql);
            int i=1;
            ps = con.prepareStatement(sql);    		
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
            	cont++;            	
            }
            
            resultado = false;
            if (cont > 0) resultado = true;
            
    	
        }catch (Exception e) {
            resultado = false;
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.FichaExpedienteDAO.tieneExpedientesRelacionados"), e);
            
    	}finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
    	}
        
        return resultado;
    	
    }
    
    
    
    
    
    public boolean tieneTramitesAbiertos(GeneralValueObject gVO, String[] params) throws TechnicalException {

        Vector tramites = cargaTramites(gVO, params);
        for (Object objTramite: tramites) {
            GeneralValueObject gvoTramite = (GeneralValueObject)objTramite;
            String fechaFin = (String)gvoTramite.getAtributo("fechaFin");

            if (fechaFin == null || "".equals(fechaFin)) return true;
        }

        return false;
    }
    
    /**
     * Carga el listado de ficheros aportados a registro del tipo indicado para los asientos relacionados con 
     * el expediente indicado. Carga los datos necesarios para la posterior recuperacion de su contenido (no 
     * carga el array de bytes que forma el contenido del fichero). El estado de la anotacion debe ser 1
     * (aceptada).
     *
     * @return Un vector de ficheroVO.
     */
    public Vector<FicheroVO> cargarFicherosRegistro(String tipoRegistro, String numExpediente, String codMunicipio, String[] params) 
        throws TechnicalException {
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector<FicheroVO> lista = new Vector<FicheroVO>();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT " + sql_docsDepto + ", " + sql_docsUnid + ", " + sql_docsEjerc + ", " 
                            + sql_docsNum + ", " + sql_docsNombreDoc + ", " + sql_docsTipoDoc +
                  " FROM R_RED INNER JOIN E_EXR ON (" + sql_docsDepto + "=" + sql_exr_dep + " AND "
                                                      + sql_docsUnid +  "=" + sql_exr_uor + " AND "
                                                      + sql_docsEjerc + "=" + sql_exr_ejr + " AND "
                                                      + sql_docsNum +   "=" + sql_exr_nre + " AND "
                                                      + sql_docsTipo +  "=" + sql_exr_tip + ")" +
                  " INNER JOIN R_RES ON (" + sql_docsDepto + "=" + sql_res_codDpto + " AND "
                                           + sql_docsUnid +  "=" + sql_res_codUnid + " AND "
                                           + sql_docsEjerc + "=" + sql_res_ejer + " AND "
                                           + sql_docsNum +   "=" + sql_res_num + " AND "
                                           + sql_docsTipo +  "=" + sql_res_tipoReg + ")" +
                  " WHERE " + sql_exr_num + "= '" + numExpediente + "'" +
                  " AND " + sql_exr_mun + "=" + codMunicipio +
                  " AND " + sql_exr_tip + "='" + tipoRegistro + "'" +
                  //" AND " + sql_docsDoc + " IS NOT NULL" +
                  " AND " + sql_res_estado + "=1" +
                  " ORDER BY " + sql_exr_ejr + ", " + sql_exr_nre;            
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                FicheroVO fichero = new FicheroVO();
                fichero.setTipo(tipoRegistro);
                fichero.setDep(rs.getString(sql_docsDepto));
                fichero.setUor(rs.getString(sql_docsUnid));
                fichero.setEjercicio(rs.getString(sql_docsEjerc));
                fichero.setNumero(rs.getString(sql_docsNum));
                fichero.setNombre(rs.getString(sql_docsNombreDoc));
                fichero.setTipoContenido(rs.getString(sql_docsTipoDoc));
                lista.add(fichero);
            }

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);                      
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return lista;
    }
    
    /**
     * Carga el listado de ficheros existentes en los trámites del expediente indicado. Carga los datos necesarios 
     * para la posterior recuperacion de su contenido (no carga el array de bytes que forma el contenido del fichero). 
     * Se tiene en cuenta si el usuario tiene permisos para los trámites.
     *
     * @return Un vector de ficheroVO.
     */
    public Vector<FicheroVO> cargarFicherosTramites(UsuarioValueObject usuarioVO,
        String numExpediente, String codMunicipio, boolean expHistorico, String[] params)throws TechnicalException {
      
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector<FicheroVO> lista = new Vector<FicheroVO>();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            if (esExpedienteHistorico(numExpediente, con))
                sql = "SELECT TFIT_MUN, TFIT_NUM, TFIT_TRA, TFIT_OCU, TFIT_COD, TFIT_NOMFICH, TFIT_MIME, TML_VALOR, CRO_FEI, CRO_TRA " + // tml_valor = nombre del trámite
                        "FROM HIST_E_TFIT " +
                        "INNER JOIN E_TML ON (TFIT_MUN = TML_MUN AND TFIT_PRO = TML_PRO AND " +
                            "TFIT_TRA = TML_TRA AND TML_CMP = 'NOM' AND TML_LENG = '"+idiomaDefecto+"') " +
                        "INNER JOIN HIST_E_CRO ON (TFIT_MUN = CRO_MUN AND TFIT_NUM = CRO_NUM AND " +
                            "TFIT_TRA = CRO_TRA AND TFIT_OCU = CRO_OCU) " +
                            "INNER JOIN HIST_E_EXP ON (TFIT_MUN = EXP_MUN AND TFIT_NUM = EXP_NUM) "+
                        "WHERE TFIT_MUN = '" + codMunicipio + "' " +
                        "AND TFIT_NUM = '" + numExpediente + "' " +
                      " AND ((EXISTS (" +
                            "SELECT DISTINCT UOU_UOR " +
                             " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU = " + usuarioVO.getIdUsuario() + " " +
                            "AND UOU_ORG = " + usuarioVO.getOrgCod() + " " +
                            "AND UOU_ENT = " + usuarioVO.getEntCod() + " " +
                            "AND (UOU_UOR = CRO_UTR OR UOU_UOR=EXP_UOR))) OR CRO_FEF IS NOT NULL) " +
                        "UNION " +
                        "SELECT CRD_MUN, CRD_NUM, CRD_TRA, CRD_OCU, " + oad.convertir("CRD_NUD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) +", CRD_DES, 'none', TML_VALOR, CRO_FEI, CRO_TRA " + // tml_valor = nombre del trámite
                        "FROM HIST_E_CRD " +
                        "INNER JOIN E_TML ON (CRD_MUN = TML_MUN AND CRD_PRO = TML_PRO AND " +
                            "CRD_TRA = TML_TRA AND TML_CMP = 'NOM' AND TML_LENG = '"+idiomaDefecto+"') " +
                        "INNER JOIN HIST_E_CRO ON (CRD_MUN = CRO_MUN AND CRD_NUM = CRO_NUM AND " +
                            "CRD_TRA = CRO_TRA AND CRD_OCU = CRO_OCU) " +
                        "INNER JOIN HIST_E_EXP ON (CRD_MUN = EXP_MUN AND CRD_NUM = EXP_NUM ) " +
                        "WHERE CRD_MUN = '" + codMunicipio + "' " +
                        "AND CRD_NUM = '" + numExpediente + "' " +
                        "AND ((EXISTS ( " +
                            "SELECT DISTINCT UOU_UOR " +
                            "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE  UOU_USU = " + usuarioVO.getIdUsuario() + " " +
                            "AND UOU_ORG = " + usuarioVO.getOrgCod() + " " +
                            "AND UOU_ENT = " + usuarioVO.getEntCod() + " " +
                            "AND (UOU_UOR = CRO_UTR OR UOU_UOR=EXP_UOR)))OR CRO_FEF IS NOT NULL) " +
                        "ORDER BY CRO_FEI, CRO_TRA";
            else
                sql = "SELECT TFIT_MUN, TFIT_NUM, TFIT_TRA, TFIT_OCU, TFIT_COD, TFIT_NOMFICH, TFIT_MIME, TML_VALOR, CRO_FEI, CRO_TRA " + // tml_valor = nombre del trámite
                        "FROM E_TFIT " +
                        "INNER JOIN E_TML ON (TFIT_MUN = TML_MUN AND TFIT_PRO = TML_PRO AND " +
                            "TFIT_TRA = TML_TRA AND TML_CMP = 'NOM' AND TML_LENG = '"+idiomaDefecto+"') " +
                        "INNER JOIN E_CRO ON (TFIT_MUN = CRO_MUN AND TFIT_NUM = CRO_NUM AND " +
                            "TFIT_TRA = CRO_TRA AND TFIT_OCU = CRO_OCU) " +
                            "INNER JOIN E_EXP ON (TFIT_MUN = EXP_MUN AND TFIT_NUM = EXP_NUM) "+
                        "WHERE TFIT_MUN = '" + codMunicipio + "' " +
                        "AND TFIT_NUM = '" + numExpediente + "' " +
                      " AND ((EXISTS (" +
                            "SELECT DISTINCT UOU_UOR " +
                             " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU = " + usuarioVO.getIdUsuario() + " " +
                            "AND UOU_ORG = " + usuarioVO.getOrgCod() + " " +
                            "AND UOU_ENT = " + usuarioVO.getEntCod() + " " +
                            "AND (UOU_UOR = CRO_UTR OR UOU_UOR=EXP_UOR))) OR CRO_FEF IS NOT NULL) " +
                        "UNION " +
                        "SELECT CRD_MUN, CRD_NUM, CRD_TRA, CRD_OCU, " + oad.convertir("CRD_NUD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) +", CRD_DES, 'none', TML_VALOR, CRO_FEI, CRO_TRA " + // tml_valor = nombre del trámite
                        "FROM E_CRD " +
                        "INNER JOIN E_TML ON (CRD_MUN = TML_MUN AND CRD_PRO = TML_PRO AND " +
                            "CRD_TRA = TML_TRA AND TML_CMP = 'NOM' AND TML_LENG = '"+idiomaDefecto+"') " +
                        "INNER JOIN E_CRO ON (CRD_MUN = CRO_MUN AND CRD_NUM = CRO_NUM AND " +
                            "CRD_TRA = CRO_TRA AND CRD_OCU = CRO_OCU) " +
                        "INNER JOIN E_EXP ON (CRD_MUN = EXP_MUN AND CRD_NUM = EXP_NUM ) " +
                        "WHERE CRD_MUN = '" + codMunicipio + "' " +
                        "AND CRD_NUM = '" + numExpediente + "' " +
                        "AND ((EXISTS ( " +
                            "SELECT DISTINCT UOU_UOR " +
                            "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE  UOU_USU = " + usuarioVO.getIdUsuario() + " " +
                            "AND UOU_ORG = " + usuarioVO.getOrgCod() + " " +
                            "AND UOU_ENT = " + usuarioVO.getEntCod() + " " +
                            "AND (UOU_UOR = CRO_UTR OR UOU_UOR=EXP_UOR)))OR CRO_FEF IS NOT NULL) " +
                        "ORDER BY CRO_FEI, CRO_TRA";
                                                     
                                                     
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                FicheroVO fichero = new FicheroVO();                
                fichero.setMunicipio(rs.getString(tfit_mun));
                fichero.setExpediente(rs.getString(tfit_num));
                fichero.setTramite(rs.getString(tfit_tra));
                fichero.setOcurrencia(rs.getString(tfit_ocu));
                fichero.setCodigoFicheroTramite(rs.getString(tfit_cod));
                fichero.setNombre(rs.getString(tfit_nomfich));                
                fichero.setTipoContenido(rs.getString(tfit_mime));
                fichero.setNombreTramite(rs.getString(tml_valor));
                lista.add(fichero);
            }

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);                                
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return lista;   
    }
    
    /**
     * Devuelve el contenido binario de un fichero guardado en la BD. El FicheroVO pasado
     * puede corresponder tanto a un fichero de un asiento como a un fichero de un trámite.
     * 
     * @param fichero  FicheroVO que contiene los datos para recuperar el fichero de BD.
     * 
     * @return  Contenido del fichero en forma de array de bytes.
     */
    public byte[] cargarValorFichero(FicheroVO fichero, String[] params) throws TechnicalException {
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        byte[] valor = new byte[0];

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();            

            if (fichero.getNombreAsiento() != null) {  // Se trata de un asiento
                
                sql = "SELECT " + sql_docsDoc + 
                     " FROM R_RED" + 
                     " WHERE " + sql_docsDepto + "=" + fichero.getDep() +
                     " AND " + sql_docsUnid + "=" + fichero.getUor() +
                     " AND " + sql_docsEjerc + "=" + fichero.getEjercicio() +
                     " AND " + sql_docsNum + "=" + fichero.getNumero() +
                     " AND " + sql_docsTipo + "='" + fichero.getTipo() + "'" +
                     " AND " + sql_docsNombreDoc + "='" + fichero.getNombre() + "'";
                
            } else if (fichero.getNombreTramite() != null) { // Se trata de un trámite
                
                if (fichero.getCodigo().startsWith("ANEXO")) {
                sql = "SELECT " + tfit_valorfich + 
                     " FROM E_TFIT" +
                     " WHERE " + tfit_mun + "=" + fichero.getMunicipio() +
                     " AND " + tfit_num + "='" + fichero.getExpediente() + "'" +
                     " AND " + tfit_tra + "=" + fichero.getTramite() +
                     " AND " + tfit_ocu + "=" + fichero.getOcurrencia() +
                     " AND " + tfit_cod + "='" + fichero.getCodigoFicheroTramite() + "'";
                } else {
                    m_Log.debug("CARGAMOS UN FICHERO DE TRAMITACION");
                    sql = "SELECT CRD_FIL " + 
                            "FROM E_CRD " +
                            "WHERE CRD_MUN = " + fichero.getMunicipio() + " " +
                            "AND CRD_NUM = '" + fichero.getExpediente() + "' " +
                            "AND CRD_TRA = " + fichero.getTramite() + " " +
                            "AND CRD_OCU = " + fichero.getOcurrencia() + " " +
                            "AND CRD_NUD = " + fichero.getCodigoFicheroTramite();
                }
            } else {
                m_Log.error("FicheroVO invalido");                
            }
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            if(rs.next()){
                valor = rs.getBytes(1);
            }

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);                      
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return valor;
    }
    
    public boolean existeDocumentoExpediente(String[] params, int codOrganizacion, String codProcedimiento, int numDocumento) throws TechnicalException, TramitacionException {
        String sqlQuery = "SELECT * FROM E_DOE WHERE DOE_MUN = ? AND DOE_PRO = ? AND DOE_COD = ?";
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = abd.getConnection();
            ps = con.prepareStatement(sqlQuery);
            int i = 1;
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i, numDocumento);
            
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new TramitacionException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }

      /**
     * Guardar las observaciones de un procedimiento
     * @param codProcedimiento: Código del procedimineto
     * @param observaciones: Observaciones
     * @param params: Paránmetros de conexión a la bd
     * @throws es.altia.common.exception.TechnicalException si ocurre algún error grave
     */
     public void guardarObservaciones(String codProcedimiento,String observaciones, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        m_Log.debug("guardarObservaciones codProcedimiento: " + codProcedimiento);
        m_Log.debug("guardarObservaciones observaciones: " + observaciones);

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String sql = "SELECT EXP_OBS FROM E_EXP WHERE EXP_NUM='" + codProcedimiento + "'";
            m_Log.debug("FichaExpedienteDAO.guardarObservaciones: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            String oldObser = rs.getString("EXP_OBS");
            m_Log.debug("oldObser: " + oldObser);
            if(oldObser==null || oldObser.length()==0)
                oldObser = "";

            StringBuffer oldOb = new StringBuffer();
            if("".equals(oldObser))
                oldOb.append(observaciones);
            else{
                oldOb.append(oldObser);
                oldOb.append("\r\n");
                oldOb.append(observaciones);
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);
            
            st = con.createStatement();
            m_Log.debug("FichaExpedienteDAO.guardarObservaciones nuevas observaciones: " + oldOb);
            sql = "UPDATE E_EXP SET EXP_OBS='" + oldOb.toString() + "' WHERE EXP_NUM='" + codProcedimiento + "'";
            m_Log.debug("FichaExpedienteDAO.guardarObservaciones: " + sql);
            int rowsUpdated = st.executeUpdate(sql);
            m_Log.debug("Num filas actualizadas " + rowsUpdated);
            SigpGeneralOperations.closeStatement(st);

        }catch (BDException e){
            e.printStackTrace();
            m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        }catch (SQLException e){
            e.printStackTrace();
            m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        }finally{
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }


    /**
     * Para un expediente determinado, se recupera los datos del registro de entrada/salida al que está asociado
     * @param gVO
     * @param params
     * @throws es.altia.common.exception.TechnicalException
     */
     public void getRegistroRelacionado(GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList registros = new ArrayList();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String numeroExpediente = (String)gVO.getAtributo("numero");

            String sql = "SELECT EXREXT_EJR,EXREXT_NRE,EXREXT_UOR FROM E_EXREXT " +
                         "WHERE EXREXT_NUM='" + numeroExpediente + "'";

            m_Log.debug(" ******************** getRegistroRelacionado: " + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                String ejercicio = rs.getString("EXREXT_EJR");
                String uor       = rs.getString("EXREXT_UOR");
                String nre       = rs.getString("EXREXT_NRE");
                m_Log.debug(" ******************** getRegistroRelacionado ejercicio: " + ejercicio);
                m_Log.debug(" ******************** getRegistroRelacionado uor: " + uor);
                m_Log.debug(" ******************** getRegistroRelacionado nre: " + nre);
                gVO.setAtributo("codUnidadRegistro",uor);
                gVO.setAtributo("numeroAsiento",nre);
                gVO.setAtributo("ejercicioAsiento",ejercicio);
                
                GeneralValueObject obj = new GeneralValueObject();
                obj.setAtributo("codUnidadRegistro",uor);
                obj.setAtributo("numeroAsiento",nre);
                obj.setAtributo("ejercicioAsiento",ejercicio);
                registros.add(obj);
            }
            gVO.setAtributo("registrosExternos",registros);
            
        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());           
        }catch (SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }

     
     
     
     
    public void getRegistroRelacionado(GeneralValueObject gVO,Connection con) throws TechnicalException {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList registros = new ArrayList();

        try{
            
            String sql = "SELECT EXREXT_EJR,EXREXT_NRE,EXREXT_UOR FROM E_EXREXT " +
                         "WHERE EXREXT_NUM=?";
            int i=1;
            ps = con.prepareStatement(sql); 
            ps.setString(i++,(String)gVO.getAtributo("numero"));
            
            rs = ps.executeQuery();
            while(rs.next()){
                String ejercicio = rs.getString("EXREXT_EJR");
                String uor       = rs.getString("EXREXT_UOR");
                String nre       = rs.getString("EXREXT_NRE");                
                gVO.setAtributo("codUnidadRegistro",uor);
                gVO.setAtributo("numeroAsiento",nre);
                gVO.setAtributo("ejercicioAsiento",ejercicio);
                
                GeneralValueObject obj = new GeneralValueObject();
                obj.setAtributo("codUnidadRegistro",uor);
                obj.setAtributo("numeroAsiento",nre);
                obj.setAtributo("ejercicioAsiento",ejercicio);
                registros.add(obj);
            }
            gVO.setAtributo("registrosExternos",registros);
            
        }catch (SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);            
        }
    }
     
     
     
     
     
     
     
     
     
     private String mostrarFecha(Calendar c){
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        return sf.format(c.getTime());
    }

     /**
      * Operación de retroceso de expediente que es llamada cuando se utiliza el nuevo método de vuelta atrás
      * @param gVO: GeneralValueObject con los datos del trámite a retroceder
      * @param params: Parámetros de conexión a la base de datos
      * @return RespuestaRetrocesoTramiteVO
      * @throws es.altia.common.exception.TechnicalException
      * @throws es.altia.agora.business.integracionsw.exception.EjecucionSWException
      */
    public RespuestaRetrocesoTramiteVO retrocederExpedienteCHMetodoNuevo(GeneralValueObject gVO, String[] params) throws TechnicalException, EjecucionSWException, EjecucionModuloException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        RespuestaRetrocesoTramiteVO resultado = null;

        m_Log.debug(" funcion  retrocederExpedienteCHMetodoNuevo");

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; 

            this.conexionRetroceso = con;
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            String codTramiteRetroceder = (String) gVO.getAtributo("codTramiteRetroceder");
            String ocurrenciaTramiteRetroceder = (String) gVO.getAtributo("ocurrenciaTramiteRetroceder");

            m_Log.debug("RETROCEDEMOS EL TRAMITE: " + codTramiteRetroceder + " OCURRENCIA " + ocurrenciaTramiteRetroceder);

            // Recupero los tramites abiertos del expediente.
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ").append(oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS FEI, ").append(oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS FEF, CRO_TRA, CRO_PRO, CRO_NUM, CRO_OCU");
            sql.append(" FROM e_cro");
            sql.append(" WHERE ");
            sql.append("CRO_MUN").append(" = ").append(codMunicipio);
            sql.append(" AND ").append("CRO_PRO").append(" = '").append(codProcedimiento).append("'");
            sql.append(" AND ").append("CRO_EJE").append(" = '").append(ejercicio).append("'");
            sql.append(" AND ").append("CRO_NUM").append(" = '").append(numero).append("'");
            sql.append(" AND ").append("CRO_FEF").append(" is null");
            sql.append(" ORDER BY CRO_FEI DESC");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tramites abiertos del expediente: " + sql.toString());
            }

            st = con.prepareStatement(sql.toString());
            rs = st.executeQuery();

            Vector<String[]> tramitesAbiertos = new Vector<String[]>();
            while (rs.next()) {
                String[] tramiteAbierto = new String[3];
                tramiteAbierto[0] = rs.getString("CRO_TRA");
                tramiteAbierto[1] = rs.getString("CRO_OCU");
                tramiteAbierto[2] = rs.getString("FEI");
                tramitesAbiertos.addElement(tramiteAbierto);
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            // Recupero los tramites cerrados del expediente.
            sql = new StringBuffer();
            sql.append("SELECT CRO_TRA, CRO_OCU, ").append(oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS FEI");
            sql.append(" FROM e_cro");
            sql.append(" WHERE ");
            sql.append("CRO_MUN").append(" = ").append(codMunicipio);
            sql.append(" AND ").append("CRO_PRO").append(" = '").append(codProcedimiento).append("'");
            sql.append(" AND ").append("CRO_EJE").append(" = '").append(ejercicio).append("'");
            sql.append(" AND ").append("CRO_NUM").append(" = '").append(numero).append("'");
            sql.append(" AND ").append("CRO_FEF").append(" is NOT null");
            sql.append(" ORDER BY CRO_FEI DESC");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tramites cerrados del expediente: " + sql.toString());
            }

            st = con.prepareStatement(sql.toString());
            rs = st.executeQuery();

            Vector<String[]> tramitesCerrados = new Vector<String[]>();
            while (rs.next()) {
                String[] tramiteCerrado = new String[3];
                tramiteCerrado[0] = rs.getString("CRO_TRA");
                tramiteCerrado[1] = rs.getString("CRO_OCU");
                tramiteCerrado[2] = rs.getString("FEI");
                tramitesCerrados.addElement(tramiteCerrado);
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            // Ahora tengo que comprobar en cual de los dos grupos se encuentra el
            // tramite que se quiere retroceder.
            boolean estaEnAbiertos = false;
            String fechaInicio = null;
            for (String[] tramiteAbierto: tramitesAbiertos) {
                if (tramiteAbierto[0].equals(codTramiteRetroceder) && tramiteAbierto[1].equals(ocurrenciaTramiteRetroceder)) {
                    estaEnAbiertos = true;
                    fechaInicio = tramiteAbierto[2];
                }

            }

            if (estaEnAbiertos) {
                // Comprobamos que posible tramite puede haber iniciado el seleccionado.
                sql = new StringBuffer();
                TramitacionExpedientesDAO teDAO = TramitacionExpedientesDAO.getInstance();

                //(int codTramDestino, int ocuTramDestino,int ejercicio,String codPro,int codMun,String numExp,Connection con) throws SQLException{
                TramitacionExpedientesValueObject tramiteOrigen = teDAO.getTramiteOrigen(Integer.parseInt(codTramiteRetroceder),Integer.parseInt(ocurrenciaTramiteRetroceder),Integer.parseInt(ejercicio),codProcedimiento,Integer.parseInt(codMunicipio),numero,con);
                m_Log.debug(" **** Tramite origen del que se retrocede es: " + tramiteOrigen.getCodTramite() + " y ocurrencia: " + tramiteOrigen.getOcurrenciaTramite());

                String codTramInicio = tramiteOrigen.getCodTramite();
                String ocuTramInicio = tramiteOrigen.getOcurrenciaTramite();
                String nomTramInicio = tramiteOrigen.getTramite();
                String fecIniTramInicio = tramiteOrigen.getFechaInicio();
             

                m_Log.debug("EL TRAMITE A RETROCEDER FUE INICIADO POR EL TRAMITE: " + codTramInicio + ", OCURRENCIA: " + ocuTramInicio);
                boolean reabrirTramOrigen = true;
                if (codTramInicio != null && Utilidades.isInteger(codTramInicio) && Utilidades.isInteger(ocurrenciaTramiteRetroceder) && Integer.parseInt(codTramInicio)!=-1 && Integer.parseInt(ocurrenciaTramiteRetroceder)!=-1) {
                    
                    /** SE RECUPERAN LOS TRÁMITES QUE HA ABIERTO EL TRÁMITE DE ORIGEN DEL SELECCIONADO PARA DETERMINADA SI SE TIENE QUE ABRIR O NO EL TRÁMITE DE ORIGEN*/
                    ArrayList<TramitacionExpedientesValueObject> tramsAbiertosPorOrigen = teDAO.getDestinoTramExcluido(tramiteOrigen,Integer.parseInt(codTramiteRetroceder),Integer.parseInt(ocurrenciaTramiteRetroceder), con);
                    // Si hay tramites abiertos por el trámite de origen, excluido el trámite que se retrocede => No se permite reabrir el trámite de origen.
                    if(tramsAbiertosPorOrigen!=null && tramsAbiertosPorOrigen.size()>0){
                      resultado = new RespuestaRetrocesoTramiteVO();
                      resultado.setTramitesDestino(tramsAbiertosPorOrigen);                      
                      resultado.setTipoRespuesta(ConstantesDatos.TRAMITE_ORIGEN_CON_TRAMITES_ABIERTOS);
                      reabrirTramOrigen = false;
                    }

                    m_Log.debug("¿HAY QUE REABRIR EL TRAMITE DE ORIGEN?: " + reabrirTramOrigen);
                }

                m_Log.debug("retrocederExpediente entrando");
                retrocederExpedienteMetodoNuevo(gVO, params, con, codTramiteRetroceder, ocurrenciaTramiteRetroceder);

                if (reabrirTramOrigen){ // Sólo se reabre el trámite de origen al de retroceso, si el de origen no ha abiertos otros trámites.
                     m_Log.debug("retrocederExpediente reabrimos el trámite de origen por: " + reabrirTramOrigen);
                     resultado = new RespuestaRetrocesoTramiteVO();
                     resultado.setReabrirTramiteOrigen(true);
                     resultado.setCodTramiteOrigenReabrir(Integer.parseInt(codTramInicio));
                     resultado.setOcurrenciaTramiteOrigenReabrir(Integer.parseInt(ocuTramInicio));
                     resultado.setNomTramiteOrigenReabrir(nomTramInicio);
                     resultado.setFecIniTramiteOrigenReabrir(fecIniTramInicio);
                     
                     
                     //Tenemos que añadir aqui una consulta para saber si el tramite esta abierto o cerrado.
                     //Si esta abierto no se llama a retroceder. No deberia en condicioens normales
                     boolean esTramiteAbierto=false;
                     esTramiteAbierto=estaTramiteAbierto(gVO,codTramInicio,ocuTramInicio,con);
                     m_Log.debug("retrocederExpediente el trámite origen esta abierto? "+esTramiteAbierto);
                     if(!esTramiteAbierto){
                         m_Log.debug("retrocederExpediente el trámite origen esta cerrado");
                         retrocederExpedienteMetodoNuevo(gVO, params, con, codTramInicio, ocuTramInicio);
                         resultado = new RespuestaRetrocesoTramiteVO();
                         resultado.setTipoRespuesta(ConstantesDatos.TRAMITE_RETROCESO_DE_ESTADO_FINALIZADO);      
                     }
                    
                }

            } else {
                m_Log.debug("retrocederExpediente el trámite no está entre los abiertos");
                retrocederExpedienteMetodoNuevo(gVO, params, con, codTramiteRetroceder, ocurrenciaTramiteRetroceder);
                 resultado = new RespuestaRetrocesoTramiteVO();
                 resultado.setTipoRespuesta(ConstantesDatos.TRAMITE_REABRIR);
            }

            SigpGeneralOperations.commit(oad, con);

        } catch (BDException bde) {
            resultado = null;
            SigpGeneralOperations.rollBack(oad, con);
            SigpGeneralOperations.devolverConexion(oad, con);
            throw new TechnicalException(bde.getMessage(), bde);
        }
        /********* manejar el mensaje de error en la llamada a la operación de un servicio web ****************/
        catch (EjecucionSWException e) {
            //e.printStackTrace();
            resultado = null;
            SigpGeneralOperations.rollBack(oad, con);
            SigpGeneralOperations.devolverConexion(oad, con);
            /** original
             * throw new TechnicalException(e.getMensaje(), e);
             */
            throw e;
        }
        /********* manejar el mensaje de error en la llamada a la operación de un servicio web ****************/

        /********* manejar el mensaje de error en la llamada a la operación de módulo externo ****************/
        catch (EjecucionModuloException e) {
            e.printStackTrace();
            resultado = null;
            SigpGeneralOperations.rollBack(oad, con);
            SigpGeneralOperations.devolverConexion(oad, con);
            /** original
             * throw new TechnicalException(e.getMensaje(), e);
             */
            throw e;
        }
        /********* manejar el mensaje de error en la llamada a la operación de un módulo externo ****************/
        catch (Exception e) {
            e.printStackTrace();
            resultado = null;
            SigpGeneralOperations.rollBack(oad, con);
            SigpGeneralOperations.devolverConexion(oad, con);
            throw new TechnicalException(e.getMessage(), e);
        }
        
        finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        
        return resultado;
    }



    /**
     * Método de retroceso del expediente
     * @param gVO
     * @param params
     * @param con
     * @param codTramiteRetroceder
     * @param ocuTramRetro
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.integracionsw.exception.EjecucionSWException
     */
     private void retrocederExpedienteMetodoNuevo(GeneralValueObject gVO, String[] params, Connection con, String codTramiteRetroceder, String ocuTramRetro) throws TechnicalException, EjecucionSWException, EjecucionModuloException {
   	
       AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
       Statement st = null;
       ResultSet rs = null;
       StringBuffer sql = new StringBuffer();
       m_Log.debug("retrocederExpediente para codTramiteRetroceder: " + codTramiteRetroceder + " ocurrencia: " + ocuTramRetro);
       try{

           String codMunicipio = (String)gVO.getAtributo("codMunicipio");
           String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
           String ejercicio = (String)gVO.getAtributo("ejercicio");
           String numero = (String)gVO.getAtributo("numero");
           String codigoIdiomaUsuario = (String)gVO.getAtributo("codigoIdiomaUsuario");

           m_Log.debug(" datos que recojo del formulario");
           m_Log.debug(" [codMunicipio]"+codMunicipio);
           m_Log.debug(" [codProcedimiento]"+codProcedimiento);
           m_Log.debug(" [ejercicio]"+ejercicio);
           m_Log.debug(" [numero]"+numero);
           m_Log.debug("[codTramiteRetroceder]"+codTramiteRetroceder);
           m_Log.debug(" [ocurrenciaTramiteRetroceder]"+ocuTramRetro);
           
           if (codProcedimiento==null||"".equals(codProcedimiento)) throw new TechnicalException("EL codigo de procedimiento NO PUEDE SER NULO");
           if (codMunicipio==null||"".equals(codMunicipio)) throw new TechnicalException("EL codMunicipio NO PUEDE SER NULO");
           if (ejercicio==null||"".equals(ejercicio)) throw new TechnicalException("EL EJERCICIO NO PUEDE SER NULO");
           if (numero==null||"".equals(numero)) throw new TechnicalException("EL numero de expediente NO PUEDE SER NULO");
           if (codTramiteRetroceder==null||"".equals(codTramiteRetroceder)) throw new TechnicalException("EL codTramiteRetroceder NO PUEDE SER NULO");
           if (ocuTramRetro==null||"".equals(ocuTramRetro)) throw new TechnicalException("EL ocuTramRetro NO PUEDE SER NULO");

           sql.append ("SELECT CRO_FEI AS FEI, CRO_FEF AS FEF");
           sql.append (" FROM e_cro");
           sql.append (" WHERE ");
           sql.append (cro_mun).append(" = ").append(codMunicipio);
           sql.append (" AND ").append(cro_pro).append(" = '").append(codProcedimiento).append("'");
           sql.append (" AND ").append(cro_eje).append(" = '").append(ejercicio).append("'");
           sql.append (" AND ").append(cro_num).append(" = '").append(numero).append("'");
           sql.append (" AND ").append(cro_tra).append(" = ").append(codTramiteRetroceder);
           sql.append (" AND ").append(cro_ocu).append(" = ").append(ocuTramRetro);
           sql.append (" ORDER BY " + cro_fei + " DESC");

           if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
           st = con.createStatement();
           rs = st.executeQuery(sql.toString());

           Timestamp fecha_inicio = null;
           Timestamp fecha_fin = null;
           while(rs.next()){
               fecha_inicio = rs.getTimestamp("FEI");
               fecha_fin = rs.getTimestamp("FEF");
           }
           gVO.setAtributo("fechaInicio", fecha_inicio); 
           SigpGeneralOperations.closeResultSet(rs);
           SigpGeneralOperations.closeStatement(st);
           
           m_Log.debug("FECHA INICIO RECUPERADA: " + fecha_inicio);

           if(fecha_fin==null && fecha_inicio!= null) {
           	 if(m_Log.isDebugEnabled()) m_Log.debug("--> Dentro de if  de retroceder expediente");

             String sqlStr = "DELETE /*+ INDEX (LIST_TRAM_ORIG IX_LIST_TRAM_ORIG) */ FROM LIST_TRAM_ORIG WHERE EJERCICIO=? AND COD_PRO=? AND COD_MUN=? "
                     + "AND NUM_EXP=? AND COD_TRA_DESTINO=? AND OCU_TRA_DESTINO=?";
             PreparedStatement pst = con.prepareStatement(sqlStr);
             pst.setInt(1, Integer.parseInt(ejercicio));
             pst.setString(2, codProcedimiento);
             pst.setInt(3, Integer.parseInt(codMunicipio));
             pst.setString(4, numero);
             pst.setInt(5, Integer.parseInt(codTramiteRetroceder));
             pst.setInt(6, Integer.parseInt(ocuTramRetro));


             if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en LIST_TRA_ORIG :" + sqlStr + "\nEJERCICIO: " + ejercicio 
                     + "\nCOD_PRO: " + codProcedimiento 
                     + "\nCOD_MUN: " + codMunicipio 
                     + "\nNUM_EXP: " + numero 
                     + "\nCOD_TRA_DESTINO: " + codTramiteRetroceder 
                     + "\nOCU_TRA_DESTINO: " + ocuTramRetro 
                     );
             pst.executeUpdate();             
             SigpGeneralOperations.closeStatement(pst);

           	 //Borramos de F_TRAFORM_TRA
           	 sql = new StringBuffer();
                sql.append ("DELETE FROM f_traform_tra");
                sql.append (" WHERE ");
                sql.append (tft_tra_mun).append(" = ").append(codMunicipio);
                sql.append (" AND ").append(tft_tra_pro).append(" = '").append(codProcedimiento).append("'");
                sql.append (" AND ").append(tft_tra_eje).append(" = ").append(ejercicio);
                sql.append (" AND ").append(tft_tra_num).append(" = '").append(numero).append("'");
                sql.append (" AND ").append(tft_tra_tra).append(" = ").append(codTramiteRetroceder);
                sql.append (" AND ").append(tft_tra_ocu).append(" = ").append(ocuTramRetro);

                if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en F_TRAFORM_TRA :" + sql.toString());
                st = con.createStatement();
                st.executeUpdate(sql.toString());
                SigpGeneralOperations.closeStatement(st);

                //Borramos de F_FIRMA
                sql = new StringBuffer();
                sql.append ("DELETE FROM f_firma");
                sql.append (" WHERE ");
                sql.append (ff_trafirma_mun).append(" = ").append(codMunicipio);
                sql.append (" AND ").append(ff_trafirma_pro).append(" = '").append(codProcedimiento).append("'");
                sql.append (" AND ").append(ff_trafirma_eje).append(" = ").append(ejercicio);
                sql.append (" AND ").append(ff_trafirma_num).append(" = '").append(numero).append("'");
                sql.append (" AND ").append(ff_trafirma_tra).append(" = ").append(codTramiteRetroceder);
                sql.append (" AND ").append(ff_trafirma_ocu).append(" = ").append(ocuTramRetro);

                if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en F_FIRMA :" + sql.toString());
                st = con.createStatement();
                st.executeUpdate(sql.toString());
                SigpGeneralOperations.closeStatement(st);

               //Se unifican todos los borrados de datos suplementarios en un método
               elimarRegistrosCamposSuplementarios(codMunicipio, codProcedimiento, ejercicio, numero, codTramiteRetroceder, ocuTramRetro, con);
              
               AlarmasExpedienteDAO.getInstance().borrarAlarmasOcurrenciaTramite(Integer.parseInt(codMunicipio),Integer.parseInt(ejercicio),numero,Integer.parseInt(codTramiteRetroceder),Integer.parseInt(ocuTramRetro), con); 

                //Bloqueos
               sql = new StringBuffer();
               sql.append ("DELETE FROM e_exp_bloq");
               sql.append (" WHERE ");
               sql.append (bloq_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(bloq_pro).append(" = '").append(codProcedimiento).append("'");
               sql.append (" AND ").append(bloq_eje).append(" = ").append(ejercicio);
               sql.append (" AND ").append(bloq_num).append(" = '").append(numero).append("'");
               sql.append (" AND ").append(bloq_tra).append(" = ").append(codTramiteRetroceder);
               sql.append (" AND ").append(bloq_ocu).append(" = ").append(ocuTramRetro);
               
               /****************************************************************/
               //Se extrae todo el contenido para eliminar los datos relacionados a ese tramite
               try {
                eliminarTodosDocumentosAsociadoTramite (codMunicipio, codProcedimiento, ejercicio, numero, codTramiteRetroceder, ocuTramRetro, con);
               } catch (TechnicalException e) {
                throw new TechnicalException(e.getMessage());
               }
                
               /****************************************************************/

               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               st.executeUpdate(sql.toString());
               SigpGeneralOperations.closeStatement(st);

               sqlStr="DELETE FROM E_CRO WHERE CRO_MUN=? AND CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? AND CRO_TRA=? AND CRO_OCU=?";
               
               PreparedStatement pst2 = con.prepareStatement(sqlStr);
               pst2.setInt(1, Integer.parseInt(codMunicipio));
               pst2.setString(2, codProcedimiento);
               pst2.setInt(3, Integer.parseInt(ejercicio));
               pst2.setString(4, numero);
               pst2.setInt(5, Integer.parseInt(codTramiteRetroceder));
               pst2.setInt(6, Integer.parseInt(ocuTramRetro));
               
               if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en E_CRO :" + sqlStr + "\nEJERCICIO: " + ejercicio 
                     + "\nCOD_PRO: " + codProcedimiento 
                     + "\nCOD_MUN: " + codMunicipio 
                     + "\nNUM_EXP: " + numero 
                     + "\nCOD_TRA_DESTINO: " + codTramiteRetroceder 
                     + "\nOCU_TRA_DESTINO: " + ocuTramRetro 
                     );
             pst2.executeUpdate();             
             SigpGeneralOperations.closeStatement(pst2);
              

              borrarFirmantesDocumentos(codMunicipio,codProcedimiento,ejercicio,numero,codTramiteRetroceder,ocuTramRetro,params);

               /**** SI EL TRÁMITE TIENE ASOCIADAS TAREAS PENDIENTES DE INICIO, SE ELIMINAN YA QUE SE ELIMINA LA OCURRENCIA DEL MISMO
                **** PARA REABRIR EL TRÁMITE DE ORIGEN */
               boolean tareasPendientesEliminadas = ModuloIntegracionExternoDAO.getInstance().eliminarTareasPendienteInicio(Integer.parseInt(codMunicipio), Integer.parseInt(codTramiteRetroceder), Integer.parseInt(ocuTramRetro), numero, con);
               if(tareasPendientesEliminadas){
                    m_Log.debug("********** LAS TAREAS PENDIENTES DEL TRÁMITE HAN SIDO ELIMINADAS ******************");
               }else
                   m_Log.debug("********** LAS TAREAS PENDIENTES DEL TRÁMITE NO HAN SIDO ELIMINADAS ******************");
               
               /***************************/
               
               
               
               
               /****** prueba *******/
                gVO.setAtributo("jndi", params[6]);
               m_Log.debug(".....ENTRO EN SINO PARTE SI....-->"+params[6]);
               try {
                   // Llamar al Servicio Web para retroceder.
                   int intCodMunicipio = Integer.parseInt(codMunicipio);
                   int intCodTramite = Integer.parseInt(codTramiteRetroceder);
                   m_Log.debug(".....TRAMITE A RETROCEDER....-->"+codTramiteRetroceder);
                   int intOcurrencia = Integer.parseInt(ocuTramRetro);
                   int intEjercicio = Integer.parseInt(ejercicio);
                   m_Log.debug(".....EJERCICIO A RETROCEDER....-->"+ejercicio);
                   PeticionSWVO peticionSW = new PeticionSWVO(intCodMunicipio, codProcedimiento, intCodTramite, false,true,false,
                           numero, intOcurrencia, intEjercicio, false, params);     
                   peticionSW.setOrigenLlamada((String) gVO.getAtributoONulo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO));
                   peticionSW.setUsuario(UsuarioDAO.getInstance().getLoginUsuario(Integer.parseInt((String) gVO.getAtributo("usuario")), con));
                   GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                   launcher.ejecutarSWTramitacion(peticionSW,oad,con);
               } catch (NoServicioWebDefinidoException e) {
                   m_Log.debug("NO HAY NINGUN SERVICIO WEB DEFINIDO AL RETROCEDER ESTE TRAMITE");
               } catch (FaltaDatoObligatorioException fdoe) {
                   throw new EjecucionSWException(fdoe, "EL VALOR DEL CAMPO " + fdoe.getNombreCampo() + " ES OBLIGATORIO PARA LLAMAR AL SERVICIO WEB");
               } catch (EjecucionOperacionModuloIntegracionException e){
                    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                    traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
                    traductor.setIdi_cod(Integer.parseInt(codigoIdiomaUsuario));

                    StringBuffer mensaje = new StringBuffer();
                    if(e.getNombreModulo()!=null && !"".equals(e.getCodigoErrorOperacion()) && e.getCodigoErrorOperacion()!=null && !"".equals(e.getCodigoErrorOperacion())){
                        try{
                            ResourceBundle  resource = ResourceBundle.getBundle(e.getNombreModulo());
                            if(resource!=null){
                                // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                                String nombreMensaje = resource.getString(codMunicipio + ConstantesDatos.MODULO_INTEGRACION + e.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + e.getCodigoErrorOperacion());
                                mensaje.append(resource.getString(nombreMensaje + ConstantesDatos.GUION_BAJO + codigoIdiomaUsuario));
                            }
                        }catch(Exception ex){
                            m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + e.getNombreModulo() + " para la operación "
                                    + e.getOperacion() + ". Se muestra un mensaje de error genérico.");

                            // SE MUESTRA ENTONCES UN MENSAJE GENÉRICO
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

                    throw new EjecucionModuloException(e,mensaje.toString());
                    
               }
               
               /****** prueba *******/

           } else {
               
               gVO.setAtributo("jndi", params[6]);
               m_Log.debug(".....ENTRO EN SINO PARTE NO....-->"+params[6]);
               try {
                   // Llamar al Servicio Web para retroceder.
                   int intCodMunicipio = Integer.parseInt(codMunicipio);
                   int intCodTramite = Integer.parseInt(codTramiteRetroceder);
                   m_Log.debug(".....TRAMITE A RETROCEDER....-->"+codTramiteRetroceder);
                   int intOcurrencia = Integer.parseInt(ocuTramRetro);
                   int intEjercicio = Integer.parseInt(ejercicio);
                   m_Log.debug(".....EJERCICIO A RETROCEDER....-->"+ejercicio);
                   PeticionSWVO peticionSW = new PeticionSWVO(intCodMunicipio, codProcedimiento, intCodTramite, false,true,false,
                           numero, intOcurrencia, intEjercicio,true, params);       
                   peticionSW.setOrigenLlamada((String) gVO.getAtributoONulo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO));
                   peticionSW.setUsuario(UsuarioDAO.getInstance().getLoginUsuario(Integer.parseInt((String) gVO.getAtributo("usuario")), con));
                   GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                   launcher.ejecutarSWTramitacion(peticionSW,oad,con);
               } catch (NoServicioWebDefinidoException e) {
                   m_Log.debug("NO HAY NINGUN SERVICIO WEB DEFINIDO AL RETROCEDER ESTE TRAMITE");
               } catch (FaltaDatoObligatorioException fdoe) {
                   throw new EjecucionSWException(fdoe, "EL VALOR DEL CAMPO " + fdoe.getNombreCampo() + " ES OBLIGATORIO PARA LLAMAR AL SERVICIO WEB");
               } catch (EjecucionOperacionModuloIntegracionException e){
                    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                    traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
                    traductor.setIdi_cod(Integer.parseInt(codigoIdiomaUsuario));

                    StringBuffer mensaje = new StringBuffer();
                    if(e.getNombreModulo()!=null && !"".equals(e.getCodigoErrorOperacion()) && e.getCodigoErrorOperacion()!=null && !"".equals(e.getCodigoErrorOperacion())){
                        try{
                            ResourceBundle  resource = ResourceBundle.getBundle(e.getNombreModulo());
                            if(resource!=null){
                                // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                                String nombreMensaje = resource.getString(codMunicipio + ConstantesDatos.MODULO_INTEGRACION + e.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + e.getCodigoErrorOperacion());
                                mensaje.append(resource.getString(nombreMensaje + ConstantesDatos.GUION_BAJO + codigoIdiomaUsuario));
                            }
                        }catch(Exception ex){
                            m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + e.getNombreModulo() + " para la operación "
                                    + e.getOperacion() + ". Se muestra un mensaje de error genérico.");

                            // SE MUESTRA ENTONCES UN MENSAJE GENÉRICO
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

                    throw new EjecucionModuloException(e,mensaje.toString());
                    
               }
               m_Log.debug("FECHA INICIO RECUPERADA: " + fecha_inicio);
               sql = new StringBuffer();
             sql.append ("UPDATE e_cro SET ");
               sql.append (cro_fef).append(" = NULL,");
               sql.append (cro_usf).append(" = NULL ");
               sql.append (" WHERE ");
               sql.append (cro_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(cro_pro).append(" = '").append(codProcedimiento).append("'");
               sql.append (" AND ").append(cro_eje).append(" = '").append(ejercicio).append("'");
               sql.append (" AND ").append(cro_num).append(" = '").append(numero).append("'");
               sql.append (" AND ").append(cro_tra).append(" = ").append(codTramiteRetroceder);
               sql.append (" AND ").append(cro_ocu).append(" = ").append(ocuTramRetro);               
               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               int rowsUpdated = st.executeUpdate(sql.toString());
               m_Log.debug("Num filas afectadas último update: " + rowsUpdated);
               SigpGeneralOperations.closeStatement(st);
               
               
               
               // ACTUALIZAR LOS CAMPOS EXP_TRA Y EXP_TOCU CON EL ULTIMO TRAMITE FINALIZADO
               String codTramiteUltCerrado = "";
               String ocuTramiteUltCerrado = "";
               sql = new StringBuffer();
               sql.append ("SELECT ").append(cro_tra + "," + cro_ocu);
               sql.append (" FROM e_cro");
               sql.append (" WHERE ");
               sql.append (cro_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(cro_pro).append(" = '").append(codProcedimiento).append("'");
               sql.append (" AND ").append(cro_eje).append(" = '").append(ejercicio).append("'");
               sql.append (" AND ").append(cro_num).append(" = '").append(numero).append("'");
               sql.append (" AND ").append(cro_fef).append(" IS NOT NULL");
               sql.append (" ORDER BY " + cro_fef + " DESC");
               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               rs = st.executeQuery(sql.toString());

               if(rs.next()) {
                   codTramiteUltCerrado = rs.getString(cro_tra);
                   ocuTramiteUltCerrado = rs.getString(cro_ocu);
               }
               SigpGeneralOperations.closeStatement(st);
               SigpGeneralOperations.closeResultSet(rs);
               
               sql = new StringBuffer();
               sql.append ("UPDATE e_exp SET ");
               if(codTramiteUltCerrado != null && !"".equals(codTramiteUltCerrado)) {
                   sql.append (exp_tra).append(" = ").append(codTramiteUltCerrado).append(",");
                   sql.append (exp_tocu).append(" = ").append(ocuTramiteUltCerrado);
               } else {
                   sql.append (exp_tra).append(" = ").append("NULL").append(",");
                   sql.append (exp_tocu).append(" = ").append("NULL");
               }
               sql.append (" WHERE ");
               sql.append (exp_mun).append(" = ").append(codMunicipio);
               sql.append (" AND ").append(exp_eje).append(" = '").append(ejercicio).append("'");
               sql.append (" AND ").append(exp_num).append(" = '").append(numero).append("'");

               if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
               st = con.createStatement();
               st.executeUpdate(sql.toString());
               SigpGeneralOperations.closeStatement(st);
           }

       } catch (SQLException sqle) {
           sqle.printStackTrace();
           throw new TechnicalException(sqle.getMessage(), sqle);
       } catch (EjecucionSWException eswe) {
           //eswe.printStackTrace();
           throw eswe;
       } finally {
           SigpGeneralOperations.closeResultSet(rs);
           SigpGeneralOperations.closeStatement(st);
       }
   }

     
    
    public Vector cargaPermisosTramites(Vector tramites, GeneralValueObject gVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Vector permisosTramites = new Vector();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String sql = "select " + tra_car + ", " + cro_utr + " from e_cro, e_tra"
                    + " where " + cro_mun + " = ? and " + cro_pro + " = ? and "
                    + cro_eje + " = ? and " + cro_num + " = ? and "
                    + tra_mun + " = " + cro_mun + " and " + tra_pro + " = " + cro_pro + " and "
                    + tra_cod + " = " + cro_tra + " and cro_tra = ? and cro_ocu = ?";
            ps = con.prepareStatement(sql);
            m_Log.debug("Permiso de tramitación basado en cargos:" + sql);
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            String tramitar = "si";

            for (int i = 0; i < tramites.size(); i++) {
                tramitar="si";
                GeneralValueObject tramiteVO = (GeneralValueObject) tramites.get(i);
                ps.setInt(1, Integer.valueOf(codMunicipio));
                ps.setString(2, codProcedimiento);
                ps.setInt(3, Integer.valueOf(ejercicio));
                ps.setString(4, numero);
                ps.setInt(5, Integer.valueOf((String) tramiteVO.getAtributo("codTramite")));
                ps.setInt(6, Integer.valueOf((String) tramiteVO.getAtributo("ocurrenciaTramite")));
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getString(tra_car) != null) {
                        if (!rs.getString(tra_car).equals("") && !rs.getString(tra_car).equals("0")) {
                            tramitar = TramitesExpedienteDAO.getInstance().permisoTramitacionUsuario(gVO, params, rs.getString(tra_car), rs.getString(cro_utr));
                        }
                    }
                    permisosTramites.add(tramitar);
                }
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            
        } catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
            }
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return permisosTramites;
        }
    }
   
     
     
     
     
     
    public Vector cargaPermisosTramites(Vector tramites, GeneralValueObject gVO, Connection con) throws TechnicalException {               
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector permisosTramites = new Vector();

        try {
            
            String expHistorico = (String) gVO.getAtributo("expHistorico");
            
            String sql = "select tra_car, cro_utr ";
            
            if ("true".equals(expHistorico)) 
                sql += " from HIST_E_CRO, e_tra";
            else
                sql += " from e_cro, e_tra";
            
            sql += " where cro_mun = ? and cro_pro = ? and "
                    + "cro_eje = ? and cro_num = ? and "
                    + "tra_mun = cro_mun and tra_pro = cro_pro and "
                    + "tra_cod = cro_tra and cro_tra = ? and cro_ocu = ?";
            
            ps = con.prepareStatement(sql);            
            m_Log.debug("Permiso de tramitación basado en cargos:" + sql);
            
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            String tramitar = "si";

            for (int i = 0; i < tramites.size(); i++) {
                tramitar="si";
                GeneralValueObject tramiteVO = (GeneralValueObject) tramites.get(i);
                ps.setInt(1, Integer.valueOf(codMunicipio));
                ps.setString(2, codProcedimiento);
                ps.setInt(3, Integer.valueOf(ejercicio));
                ps.setString(4, numero);
                ps.setInt(5, Integer.valueOf((String) tramiteVO.getAtributo("codTramite")));
                ps.setInt(6, Integer.valueOf((String) tramiteVO.getAtributo("ocurrenciaTramite")));
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getString("tra_car") != null) {
                        if (!rs.getString("tra_car").equals("") && !rs.getString("tra_car").equals("0")) {
                            tramitar = TramitesExpedienteDAO.getInstance().permisoTramitacionUsuario(gVO,rs.getString("tra_car"), rs.getString("cro_utr"),con);
                                                                           
                        }
                    } 
                    permisosTramites.add(tramitar);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
            }
            
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return permisosTramites;
        }
    }
     
     
     

     /**
     * Recupera la fecha de inicio de un determinado trámite y ocurrencia
     * @param gVO
     * @return String con la fecha en formato dd/MM/yyyy
     */
    public String getFechaInicio(GeneralValueObject gVO,Connection con){
        String fechaInicio = "";
        ResultSet rs = null;
        Statement st = null;
        try{
             String codMunicipio = (String)gVO.getAtributo("codMunicipio");
             String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
             String numero = (String)gVO.getAtributo("numero");
             String codTramite = (String)gVO.getAtributo("codTramiteRetroceder");
             String ocurrenciaTramite = (String)gVO.getAtributo("ocurrenciaTramiteRetroceder");

             String sql = "SELECT CRO_FEI FROM E_CRO WHERE " +
                           "CRO_PRO='" + codProcedimiento + "' AND " +
                           "CRO_TRA=" + codTramite + " AND " +
                           "CRO_OCU=" + ocurrenciaTramite + " AND " +
                           "CRO_NUM='" + numero + "' AND " +
                           "CRO_MUN=" + codMunicipio;

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                Timestamp tFei = rs.getTimestamp("CRO_FEI");
                if(tFei!=null){
                    Calendar c = Calendar.getInstance();
                    c.clear();
                    c.setTimeInMillis(tFei.getTime());
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaInicio = sf.format(c.getTime());
                }
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return fechaInicio;
    }


    /**
     * Método viejo de retroceso, es igual a retrocederExpedienteCH con la única salvedad que en vez de devolver un
     * int devuelve un objeto RespuestaRetrocesoTramiteVO con los datos de trámite de origen que se ha reabierto, en cacaso de que sea necesario
     * @param gVO
     * @param params
     * @return
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.integracionsw.exception.EjecucionSWException
     */
     public RespuestaRetrocesoTramiteVO retrocederExpedienteCHViejoRedefinido(GeneralValueObject gVO, String[] params) throws TechnicalException, EjecucionSWException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int res = -1;

        RespuestaRetrocesoTramiteVO salida = null;
        m_Log.debug(" funcion retrocederExpedienteCHMetodoNuevo");
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con) ; // Inicio transacción

            this.conexionRetroceso = con;
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String numero = (String)gVO.getAtributo("numero");
            String codTramiteRetroceder = (String) gVO.getAtributo("codTramiteRetroceder");
            String ocurrenciaTramiteRetroceder = (String) gVO.getAtributo("ocurrenciaTramiteRetroceder");

            m_Log.debug("RETROCEDEMOS EL TRAMITE: " + codTramiteRetroceder + " OCURRENCIA " + ocurrenciaTramiteRetroceder);

            // Recupero los tramites abiertos del expediente.
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ").append(oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS FEI, ").append(oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS FEF, CRO_TRA, CRO_PRO, CRO_NUM, CRO_OCU");
            sql.append(" FROM e_cro");
            sql.append(" WHERE ");
            sql.append("CRO_MUN").append(" = ").append(codMunicipio);
            sql.append(" AND ").append("CRO_PRO").append(" = '").append(codProcedimiento).append("'");
            sql.append(" AND ").append("CRO_EJE").append(" = '").append(ejercicio).append("'");
            sql.append(" AND ").append("CRO_NUM").append(" = '").append(numero).append("'");
            sql.append(" AND ").append("CRO_FEF").append(" is null");
            sql.append(" ORDER BY CRO_FEI DESC");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tramites abiertos del expediente: " + sql.toString());
            }

            st = con.prepareStatement(sql.toString());
            rs = st.executeQuery();

            Vector<String[]> tramitesAbiertos = new Vector<String[]>();
            while (rs.next()) {
                String[] tramiteAbierto = new String[3];
                tramiteAbierto[0] = rs.getString("CRO_TRA");
                tramiteAbierto[1] = rs.getString("CRO_OCU");
                tramiteAbierto[2] = rs.getString("FEI");
                tramitesAbiertos.addElement(tramiteAbierto);
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            // Recupero los tramites cerrados del expediente.
            sql = new StringBuffer();
            sql.append("SELECT CRO_TRA, CRO_OCU, ").append(oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS FEI");
            sql.append(" FROM e_cro");
            sql.append(" WHERE ");
            sql.append("CRO_MUN").append(" = ").append(codMunicipio);
            sql.append(" AND ").append("CRO_PRO").append(" = '").append(codProcedimiento).append("'");
            sql.append(" AND ").append("CRO_EJE").append(" = '").append(ejercicio).append("'");
            sql.append(" AND ").append("CRO_NUM").append(" = '").append(numero).append("'");
            sql.append(" AND ").append("CRO_FEF").append(" is NOT null");
            sql.append(" ORDER BY CRO_FEI DESC");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tramites cerrados del expediente: " + sql.toString());
            }

            st = con.prepareStatement(sql.toString());
            rs = st.executeQuery();

            Vector<String[]> tramitesCerrados = new Vector<String[]>();
            while (rs.next()) {
                String[] tramiteCerrado = new String[3];
                tramiteCerrado[0] = rs.getString("CRO_TRA");
                tramiteCerrado[1] = rs.getString("CRO_OCU");
                tramiteCerrado[2] = rs.getString("FEI");
                tramitesCerrados.addElement(tramiteCerrado);
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

            // Ahora tengo que comprobar en cual de los dos grupos se encuentra el
            // tramite que se quiere retroceder.
            boolean estaEnAbiertos = false;
            String fechaInicio = null;
            for (String[] tramiteAbierto: tramitesAbiertos) {
                if (tramiteAbierto[0].equals(codTramiteRetroceder) && tramiteAbierto[1].equals(ocurrenciaTramiteRetroceder)) {
                    estaEnAbiertos = true;
                    fechaInicio = tramiteAbierto[2];
                }

            }

            if (estaEnAbiertos) {
                // Comprobamos que posible tramite puede haber iniciado el seleccionado.
                sql = new StringBuffer();
                sql.append("SELECT ORIGEN.CRO_TRA AS COD_TRAM_ORI, ORIGEN.CRO_OCU AS OCU_TRAM_ORI ");
                sql.append("FROM E_CRO SELECCIONADO ");
                sql.append("JOIN E_FLS ON (FLS_MUN = SELECCIONADO.CRO_MUN AND FLS_PRO = SELECCIONADO.CRO_PRO AND FLS_CTS = SELECCIONADO.CRO_TRA) ");
                sql.append("JOIN E_CRO ORIGEN ON (FLS_MUN = ORIGEN.CRO_MUN AND FLS_PRO = ORIGEN.CRO_PRO AND FLS_TRA = ORIGEN.CRO_TRA) ");
                sql.append("WHERE SELECCIONADO.CRO_TRA = ? AND SELECCIONADO.CRO_OCU = ? AND SELECCIONADO.CRO_PRO = ? AND SELECCIONADO.CRO_MUN = ? AND SELECCIONADO.CRO_NUM = ? ");
                sql.append("AND ORIGEN.CRO_PRO = ? AND ORIGEN.CRO_MUN = ? AND ORIGEN.CRO_NUM = ? ");
                sql.append("AND (").append(oad.convertir("ORIGEN.CRO_FEF",AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH24:MI:SS")).append(") = (")
                        .append(oad.convertir("ORIGEN.CRO_FEF",AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH24:MI:SS")).append(")");

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Comprobación de que tramite ha iniciado el seleccionado: " + sql.toString());
                }

                st = con.prepareStatement(sql.toString());
                st.setInt(1, Integer.parseInt(codTramiteRetroceder));
                st.setInt(2, Integer.parseInt(ocurrenciaTramiteRetroceder));
                st.setString(3, codProcedimiento);
                st.setInt(4, Integer.parseInt(codMunicipio));
                st.setString(5, numero);
                st.setString(6, codProcedimiento);
                st.setInt(7, Integer.parseInt(codMunicipio));
                st.setString(8, numero);

                rs = st.executeQuery();

                String codTramInicio = null, ocuTramInicio = null;
                if (rs.next()) {
                    codTramInicio = rs.getString("COD_TRAM_ORI");
                    ocuTramInicio = rs.getString("OCU_TRAM_ORI");
                }

                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);

                m_Log.debug("EL TRAMITE A RETROCEDER FUE INICIADO POR EL TRAMITE: " + codTramInicio + ", OCURRENCIA: " + ocuTramInicio);
                boolean reabrirTramOrigen = true;
                if (codTramInicio != null) {
                    // Comprobamos si hay tramites que han podido ser iniciados por el tramite de origen
                    // aparte del seleccionado para retroceder.
                    sql = new StringBuffer();
                    sql.append("SELECT SELECCIONADO.CRO_TRA AS COD_TRAM_DEST, ORIGEN.CRO_OCU AS OCU_TRAM_DEST ");
                    sql.append("FROM E_CRO SELECCIONADO ");
                    sql.append("JOIN E_FLS ON (FLS_MUN = SELECCIONADO.CRO_MUN AND FLS_PRO = SELECCIONADO.CRO_PRO AND FLS_CTS = SELECCIONADO.CRO_TRA) ");
                    sql.append("JOIN E_CRO ORIGEN ON (FLS_MUN = ORIGEN.CRO_MUN AND FLS_PRO = ORIGEN.CRO_PRO AND FLS_TRA = ORIGEN.CRO_TRA) ");
                    sql.append("WHERE ORIGEN.CRO_TRA = ? AND ORIGEN.CRO_OCU = ? AND SELECCIONADO.CRO_PRO = ? AND SELECCIONADO.CRO_MUN = ? AND SELECCIONADO.CRO_NUM = ? ");
                    sql.append("AND ORIGEN.CRO_PRO = ? AND ORIGEN.CRO_MUN = ? AND ORIGEN.CRO_NUM = ? ");
                    sql.append("AND (").append(oad.convertir("ORIGEN.CRO_FEF", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS")).append(") = (").append(oad.convertir("ORIGEN.CRO_FEF", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS")).append(")");

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Se comprueba si aparte del seleccionado hay otros trámites que han podido ser abiertos: " + sql.toString());
                    }

                    st = con.prepareStatement(sql.toString());
                    st.setInt(1, Integer.parseInt(codTramInicio));
                    st.setInt(2, Integer.parseInt(ocuTramInicio));
                    st.setString(3, codProcedimiento);
                    st.setInt(4, Integer.parseInt(codMunicipio));
                    st.setString(5, numero);
                    st.setString(6, codProcedimiento);
                    st.setInt(7, Integer.parseInt(codMunicipio));
                    st.setString(8, numero);
                    m_Log.debug("param 1: " + codTramInicio);
                    m_Log.debug("param 2: " + ocuTramInicio);
                    m_Log.debug("param 3: " + codProcedimiento);
                    m_Log.debug("param 4: " + codMunicipio);
                    m_Log.debug("param 5: " + numero);
                    m_Log.debug("param 6: " + codProcedimiento);
                    m_Log.debug("param 7: " + codMunicipio);
                    m_Log.debug("param 8: " + numero);
                    rs = st.executeQuery();

                    while (rs.next()) {
                        String codTramDest = rs.getString("COD_TRAM_DEST");
                        String ocuTramDest = rs.getString("OCU_TRAM_DEST");
                        m_Log.debug("codTramDest: " + codTramDest);
                        m_Log.debug("ocuTramDest: " + ocuTramDest);

                        //if (!(codTramDest.equals(codTramiteRetroceder) && ocuTramDest.equals(ocurrenciaTramiteRetroceder))) reabrirTramOrigen = false;
                        if (codTramDest.equals(codTramiteRetroceder) && ocuTramDest.equals(ocurrenciaTramiteRetroceder)) reabrirTramOrigen=true; else reabrirTramOrigen = false;
                    }

                    SigpGeneralOperations.closeResultSet(rs);
                    SigpGeneralOperations.closeStatement(st);

                    m_Log.debug("¿HAY QUE REABRIR EL TRAMITE DE ORIGEN?: " + reabrirTramOrigen);
                }

                m_Log.debug("retrocederExpediente entrando");
                retrocederExpediente(gVO, params, con, codTramiteRetroceder, ocurrenciaTramiteRetroceder);

                if (reabrirTramOrigen){
                    salida = new RespuestaRetrocesoTramiteVO();
                    salida.setReabrirTramiteOrigen(true);
                    salida.setCodTramiteOrigenReabrir(Integer.parseInt(codTramInicio));
                    salida.setOcurrenciaTramiteOrigenReabrir(Integer.parseInt(ocuTramInicio));
                     m_Log.debug("retrocederExpediente reabrimos el trámite de origen por: " + reabrirTramOrigen);
                    retrocederExpediente(gVO, params, con, codTramInicio, ocuTramInicio);
                }

            } else {
                m_Log.debug("retrocederExpediente el trámite no está entre los abiertos");
                retrocederExpediente(gVO, params, con, codTramiteRetroceder, ocurrenciaTramiteRetroceder);
            }

            SigpGeneralOperations.commit(oad, con);

        } catch (BDException bde) {
            res = -1;
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(bde.getMessage(), bde);
        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        //envio el codigo del tramite a donde tengo que retroceder

        return salida;
      
    }


 /**
         * Carga la estructura de los datos suplementarios de procedimiento
         * @param gVO: Datos del expediente
         * @param params: Parámetros de conexión a la BBDD
         * @return
         * @throws es.altia.common.exception.TechnicalException
         */
      public Vector cargaEstructuraDatosSuplementariosProcedimiento(GeneralValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null, st2 = null;
        ResultSet rs = null, rs2 = null;
        String sql = "", sql2 = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String desdeJsp=(String)gVO.getAtributo("desdeJsp");

 

            from = pca_cod + "," + pca_des + "," + pca_plt + "," + plt_url + "," + pca_tda + "," +
                   pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," + pca_rot + "," +
                   pca_activo + "," + pca_desplegable+ " ,PCA_BLOQ, PLAZO_AVISO, PERIODO_PLAZO";
            where = pca_mun + " = " + codMunicipio + " AND " + pca_pro + " = '" + codProcedimiento + "' AND " +
                    pca_activo + " = 'SI'";

            if("si".equals(desdeJsp)) where=where+"AND PCA_OCULTO='NO'";

            String[] join = new String[5];
            join[0] = "E_PCA";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] = "e_pca." + pca_plt + "=e_plt." + plt_cod;
            join[4] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"9","9"};
            sql += oad.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(pca_cod);
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(pca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(pca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
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
                eC.setActivo(bloqueado);
                //PLAZO_AVISO, PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                eC.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                eC.setCheckPlazoFecha(checkPlazoFecha);
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String desplegable = "";
                try {
                    if ( !rs.getString(pca_desplegable).equals(null)) {
                        desplegable = rs.getString(pca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        st2 = con.createStatement();
                        sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", " + des_val_estado+" FROM E_DES_VAL WHERE " +
                               des_val_campo +"='"+ desplegable + "'";
                        rs2 = st2.executeQuery(sql2);
                        if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios sal: " + sql2);
                        while (rs2.next()) {
                            listaCod.addElement("'"+rs2.getString(des_val_cod)+"'");
                            listaDesc.addElement("'"+rs2.getString(des_val_desc)+"'");
                            listaEstado.addElement("'"+rs2.getString(des_val_estado)+"'");
                        }
                        SigpGeneralOperations.closeResultSet(rs2);
                        SigpGeneralOperations.closeStatement(st2);
                        eC.setListaCodDesplegable(listaCod);
                        eC.setListaDescDesplegable(listaDesc);
                        eC.setListaEstadoValorDesplegable(listaEstado);
                    }
                } catch (NullPointerException e){
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("PLANTILLA : " + eC.getCodPlantilla());
                lista.addElement(eC);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return lista;
        }
    }
 /**
         * Copia los interesados de un expediente a otro
         * @param gVO: Datos del expediente
         * @param params: Parámetros de conexión a la BBDD
         * @return
         * @throws es.altia.common.exception.TechnicalException
         */
       public boolean copiarInteresados(GeneralValueObject gVO,  String[] params) throws TechnicalException {

        boolean resultado=false;
       
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int res=0;
        Vector<GeneralValueObject> interesados = new Vector<GeneralValueObject>();
        try{

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String numExpedienteOriginal = (String)gVO.getAtributo("numExpedienteOriginal");
            String numExpedienteNuevo = (String)gVO.getAtributo("numExpedienteNuevo");

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String sql = "SELECT EXT_TER,EXT_NVR,EXT_DOT,EXT_ROL,EXT_PRO,MOSTRAR,EXT_NOTIFICACION_ELECTRONICA FROM E_EXT WHERE " +
                           "EXT_PRO='" + codProcedimiento + "' AND " +
                           "EXT_NUM='" + numExpedienteOriginal + "' AND " +
                           "EXT_MUN=" + codMunicipio;
            
            if(m_Log.isDebugEnabled()) m_Log.debug("sql : " + sql);
            rs = st.executeQuery(sql);

            while(rs.next()){
                GeneralValueObject interesadosVO=new GeneralValueObject();
                interesadosVO.setAtributo("codTercero",rs.getString("EXT_TER"));
                interesadosVO.setAtributo("numVersion",rs.getString("EXT_NVR"));
                interesadosVO.setAtributo("codDomicilio",rs.getString("EXT_DOT"));
                interesadosVO.setAtributo("rol",rs.getString("EXT_ROL"));
                interesadosVO.setAtributo("codProcedimiento",rs.getString("EXT_PRO"));
                interesadosVO.setAtributo("mostrar",rs.getString("MOSTRAR"));
                String notificacionElect=rs.getString("EXT_NOTIFICACION_ELECTRONICA");
                if("1".equals(notificacionElect)) interesadosVO.setAtributo("notificacionElect","1");
                else interesadosVO.setAtributo("notificacionElect","0");
                

                interesados.addElement(interesadosVO);

            }
            if(interesados.size()==0) resultado=true;
            for (int i=0;i<interesados.size();i++)
            {
                GeneralValueObject interesadosVO=interesados.get(i);

               sql = "INSERT INTO E_EXT (EXT_MUN,EXT_EJE,EXT_NUM,EXT_TER,EXT_NVR,EXT_DOT,EXT_ROL,EXT_PRO,MOSTRAR,EXT_NOTIFICACION_ELECTRONICA) VALUES ("+
                       codMunicipio+","+ejercicio+",'"+numExpedienteNuevo+"',"+interesadosVO.getAtributo("codTercero")+","+interesadosVO.getAtributo("numVersion")+
                       ","+interesadosVO.getAtributo("codDomicilio")+","+interesadosVO.getAtributo("rol")+",'"+interesadosVO.getAtributo("codProcedimiento")+"',"+interesadosVO.getAtributo("mostrar")+
                       ","+interesadosVO.getAtributo("notificacionElect")
               +")";

            if(m_Log.isDebugEnabled()) m_Log.debug("sql : " + sql);
            res= st.executeUpdate(sql);
            TercerosValueObject tercero = new TercerosValueObject();
                tercero.setIdentificador((String) interesadosVO.getAtributo("codTercero"));
                tercero.setVersion((String) interesadosVO.getAtributo("numVersion"));
                tercero.setIdDomicilio((String) interesadosVO.getAtributo("codDomicilio"));
                Vector <TercerosValueObject> terceros = TercerosDAO.getInstance().getByHistorico(tercero, con, params);
                tercero = (TercerosValueObject) terceros.elementAt(0);
                tercero.setCodRol(Integer.valueOf((String)interesadosVO.getAtributo("rol")));
                tercero.setNotificacionElectronica((String) interesadosVO.getAtributo("notificacionElect")); 
                
                OperacionesExpedienteManager.getInstance().registrarAltaInteresado(Integer.valueOf(codMunicipio).intValue(),
                        numExpedienteNuevo, Integer.valueOf((String)gVO.getAtributo("usuario")).intValue(), (String)gVO.getAtributo("nomUsuario"), tercero, con);  
           

            }

            if (res>0)
            resultado=true;


        }catch (Exception e){
            resultado=false;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        
        return resultado;
    }

       /**
         * Copia la localizacion de un expediente a otro
         * @param gVO: Datos del expediente
         * @param params: Parámetros de conexión a la BBDD
         * @return
         * @throws es.altia.common.exception.TechnicalException
         */
       public boolean copiarLocalizacion(GeneralValueObject gVO,  String[] params) throws TechnicalException {

        boolean resultado=false;

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int res=0;
        Vector<GeneralValueObject> exp = new Vector<GeneralValueObject>();
        try{

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String numExpedienteOriginal = (String)gVO.getAtributo("numExpedienteOriginal");
            String numExpedienteNuevo = (String)gVO.getAtributo("numExpedienteNuevo");
            String localizacion="";


            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            String sql = "SELECT EXP_LOC,EXP_CLO FROM E_EXP WHERE " +
                           "EXP_PRO='" + codProcedimiento + "' AND " +
                           "EXP_NUM='" + numExpedienteOriginal + "' AND " +
                           "EXP_MUN=" + codMunicipio;

            if(m_Log.isDebugEnabled()) m_Log.debug("sql : " + sql);
            rs = st.executeQuery(sql);

            while(rs.next()){
                GeneralValueObject gVO1=new GeneralValueObject();
                gVO1.setAtributo("localizacion",rs.getString("EXP_LOC"));
                gVO1.setAtributo("codLocalizacion",rs.getString("EXP_CLO"));
                localizacion=rs.getString("EXP_CLO");


                exp.addElement(gVO1);

            }
            if((("").equals(localizacion))||(localizacion==null)) resultado=true;
            else{
                for (int i=0;i<exp.size();i++)
                {
                    GeneralValueObject gVO1=exp.get(i);

                   sql = "UPDATE E_EXP SET EXP_LOC='"+gVO1.getAtributo("localizacion")+"',EXP_CLO="+gVO1.getAtributo("codLocalizacion")+" WHERE " +
                               "EXP_PRO='" + codProcedimiento + "' AND " +
                               "EXP_NUM='" + numExpedienteNuevo + "' AND " +
                               "EXP_MUN=" + codMunicipio;


                if(m_Log.isDebugEnabled()) m_Log.debug("sql : " + sql);
                res= st.executeUpdate(sql);

                }

                if (res>0)
                resultado=true;
            }

        }catch (Exception e){
            resultado=false;            
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try{
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
                if(con!=null) con.close();
            }catch(SQLException e){
                
            }
        }
        
        return resultado;
    }

    /**
     * Copia los campos suplementarios a nivel procedimiento de un expediente a otro
     * @param gVO: Datos del expediente
     * @param params: Parámetros de conexión a la BBDD
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
   public boolean copiarDatosSuplementarios(GeneralValueObject gVO,  String[] params) throws TechnicalException {

        boolean resultado=false;

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int res=0;
        Vector<GeneralValueObject> interesados = new Vector<GeneralValueObject>();
        DatosSuplementariosDAO dao = DatosSuplementariosDAO.getInstance();

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            boolean camposNumericosCopiados =dao.copyValoresNumericos(oad,con,gVO);            
            boolean camposFechaCopiados = dao.copyValoresFecha(oad,con,gVO);                        
            boolean camposTextoLargoCopiados = dao.copyValoresTextoLargo(oad,con,gVO);                        
            boolean camposFicheroCopiados = dao.copyValoresFichero(oad,con,gVO,params);           
            
            boolean camposDesplegablesCopiados =  dao.copyValoresDesplegable(oad,con,gVO);            
            boolean camposTextoCopiados = dao.copyValoresTexto(oad,con,gVO);
            boolean camposNumericosCalculadosCopiados = dao.copyValoresNumericosCalculados(oad, con, gVO);
            boolean camposFecCalculadaCopiados = dao.copyValoresFechaCalculada(oad, con, gVO);
            
            if(camposNumericosCopiados && camposFechaCopiados && camposTextoLargoCopiados && camposFicheroCopiados
                    && camposDesplegablesCopiados && camposTextoCopiados && camposNumericosCalculadosCopiados && camposFecCalculadaCopiados)
                resultado = true;
            else
                resultado = false;

        }catch (Exception e){
            resultado=false;            
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("Error al recuperar recursos asociados a la conexión a la BBDD:: " + e.getMessage());
            }
        }
        return resultado;
    }



   public boolean estaExpedienteEnUnaRelacion(String codMunicipio,String codProcedimiento,String ejercicio,String numExpediente,Connection con){
       PreparedStatement ps = null;
       ResultSet rs = null;
       boolean exito = false;

       try{           
           /*
           String sql = "SELECT COUNT(*) AS NUM FROM G_EXP WHERE EXP_MUN=" + codMunicipio + " AND EXP_PRO='" + codProcedimiento + "' AND EXP_EJE=" + ejercicio
                       + " AND REL_NUM='" + numExpediente + "'";
                           */
           
           String sql = "SELECT COUNT(*) AS NUM FROM G_EXP WHERE EXP_MUN=? AND EXP_PRO=? " + 
                        "AND EXP_EJE=? AND REL_NUM=?";
           
            m_Log.debug(sql);
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            int num = -1;
            while(rs.next())
                num = rs.getInt("NUM");

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
    private int getMaxCodFirma(Connection con) throws SQLException, TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlSelect = "SELECT MAX(ID_DOC_FIRMA) FROM E_DOCS_FIRMAS";
        m_Log.debug(sqlSelect);
        ps = con.prepareStatement(sqlSelect);
        rs = ps.executeQuery();
        int maxCod = -1;
        if (rs.next()) {
            maxCod = rs.getInt(1) + 1;
        }
        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(ps);
        return maxCod;
    }
    public void iniciarCircuitoFirmas(ArrayList<FirmasDocumentoProcedimientoVO> firmas,int codDocPresentado, int codUnidadOrganicaExp, Connection con) throws TechnicalException{
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO E_DOCS_FIRMAS(ID_DOC_FIRMA, DOC_FIRMA_ESTADO, DOC_FIRMA_ORDEN, "
                    + "DOC_FIRMA_UOR, DOC_FIRMA_CARGO, DOC_FIRMA_USUARIO, DOC_FECHA_ENVIO, ID_DOC_PRESENTADO)"
                    + "VALUES(?,?,?,?,?,?,?,?)";
            for (Iterator it= firmas.iterator();it.hasNext();) {
                FirmasDocumentoProcedimientoVO firmaVO = (FirmasDocumentoProcedimientoVO)it.next();
                ps = con.prepareStatement(sql);
                int i = 1;
                ps.setInt(i++, getMaxCodFirma(con));
                ps.setString(i++, "O");
                ps.setInt(i++, Integer.parseInt(firmaVO.getOrden()));
                if ("-999".equals(firmaVO.getUor())) {
                    ps.setInt(i++, codUnidadOrganicaExp);
                } else if (firmaVO.getUor() == null) {
                    ps.setNull(i++, Types.INTEGER);
                } else {
                    ps.setInt(i++, Integer.parseInt(firmaVO.getUor()));
                }                
                if (firmaVO.getCargo()==null) {
                    ps.setNull(i++, Types.INTEGER);
                } else {
                    ps.setInt(i++, Integer.parseInt(firmaVO.getCargo()));
                }
                if (firmaVO.getUsuario()==null) {
                    ps.setNull(i++, Types.INTEGER);
                } else {
                    ps.setInt(i++, Integer.parseInt(firmaVO.getUsuario()));
                }
                if ("1".equals(firmaVO.getOrden())) ps.setTimestamp(i++, DateOperations.toSQLTimestamp(Calendar.getInstance()));
                else ps.setNull(i++, Types.TIMESTAMP);
                ps.setInt(i++, codDocPresentado);
                m_Log.debug(sql);
                ps.executeUpdate();
                SigpGeneralOperations.closeStatement(ps);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FichaExpedienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("ERROR DE BASE DE DATOS AL CREAR EL CIRCUITO DE FIRMAS",ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }

    }

    public ArrayList<FirmasDocumentoExpedienteVO> getFirmasDocumento(int codDocumentoAdjunto, Connection con) throws TechnicalException {
        ArrayList<FirmasDocumentoExpedienteVO> firmasDocumentoExpediente = 
                new ArrayList<FirmasDocumentoExpedienteVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT DISTINCT(ID_DOC_FIRMA) AS CODIGO, DOC_FIRMA_ORDEN AS ORDEN, DOC_FIRMA_ESTADO AS ESTADO, UOR_NOM AS UOR, USU_NOM AS USUARIO, CAR_NOM AS CARGO "
                + "FROM E_DOCS_FIRMAS "
                + "LEFT JOIN A_UOR ON DOC_FIRMA_UOR=UOR_COD "
                + "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU ON DOC_FIRMA_USUARIO=USU_COD "
                + "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON "
                    + "(UOU_CAR=E_DOCS_FIRMAS.DOC_FIRMA_CARGO AND UOU_USU=E_DOCS_FIRMAS.DOC_FIRMA_USUARIO) "
                + "LEFT JOIN A_CAR ON DOC_FIRMA_CARGO=CAR_COD "
                + "WHERE ID_DOC_PRESENTADO=? "
                + "ORDER BY DOC_FIRMA_ORDEN ASC";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, codDocumentoAdjunto);
            m_Log.debug(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                FirmasDocumentoExpedienteVO firmaVO = new FirmasDocumentoExpedienteVO();
                firmaVO.setOrden(rs.getInt("ORDEN"));
                firmaVO.setEstado(rs.getString("ESTADO"));
                firmaVO.setNombreUnidad(rs.getString("UOR"));
                firmaVO.setCodigo(rs.getInt("CODIGO"));
                String cargo = rs.getString("CARGO");
                String usuario = rs.getString("USUARIO");
                if (cargo==null) {
                    firmaVO.setNombreCargo("--");                    
                } else {
                    firmaVO.setNombreCargo(cargo);
                }
                if (usuario==null) {
                    firmaVO.setNombreUsuario("--");                    
                } else {
                    firmaVO.setNombreUsuario(usuario);
                }
                firmasDocumentoExpediente.add(firmaVO);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FichaExpedienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("ERROR DE BASE DE DATOS AL RECUPERAR LAS FIRMAS DE UN DOCUMENTO DE EXPEDIENTE", ex);
        } finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la BBDD: " + e.getMessage());
            }
            
        }
        
        return firmasDocumentoExpediente;
    }   

    public String devolverEstadoFirma(ArrayList<String> estados) {
        String estadoDocumento = "";
        if (!estados.isEmpty()) {
            //Hay que tener en cuenta que las firmas estan ordenadas.
            //Si una esta pendiente, las que vengan a continuacion tambien lo estaran

            //ESTO SE PUEDE CAMBIAR SEGUN SEA LA FILOSOFIA DEL CIRCUITO DE APROBACION
            //SEGUN LA ACTUAL, ESTA PENDIENTE SI TODAS LAS FIRMAS LO ESTAN
            //                 ESTA RECHAZADO SI ALGUNA LO ESTA
            //                 ESTA FIRMADO SI TODAS LAS FIRMAS LO ESTAN
            
            
            //FALTA MIRAR SI ES NO FIRMABLE
            for (Iterator it = estados.iterator(); it.hasNext();) {
                String estado_i = (String) it.next();
                // original
                //if ("O".equals(estado_i) || "R".equals(estado_i)) {
                if ("O".equals(estado_i) || "R".equals(estado_i) || "S".equals(estado_i)) {
                    estadoDocumento = estado_i;
                    break;
                } else if ("F".equals(estado_i)) {
                    estadoDocumento = "F";
                }
            }
        } else {
            estadoDocumento = "N";
        }
        return estadoDocumento;
    }

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExternosExpediente(GeneralValueObject gVO, String idServicio, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        ArrayList<AsientoFichaExpedienteVO> listaAsientos = new ArrayList<AsientoFichaExpedienteVO>();

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
           

            String numero = (String)gVO.getAtributo("numero");
            String query = "SELECT DISTINCT EXREXT_TIP, EXREXT_FECALTA, EXREXT_NRE FROM E_EXREXT WHERE EXREXT_NUM = ?";
            
            st = con.prepareStatement(query);
            if(m_Log.isDebugEnabled()) m_Log.debug(query);
            st.setString(1, numero);
            
            rs = st.executeQuery();
            
            while(rs.next()){

                AsientoFichaExpedienteVO asiento = new AsientoFichaExpedienteVO();       
                asiento.setTipoAsiento(rs.getString("EXREXT_TIP"));
                Date fecha = rs.getDate("EXREXT_FECALTA");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                asiento.setFechaAsiento(sdf.format(fecha));
                asiento.setNumeroAsiento(new Long(rs.getLong("EXREXT_NRE")));
                             
                listaAsientos.add(asiento);
            }

        } catch (Exception e) {
            listaAsientos = new ArrayList<AsientoFichaExpedienteVO>();
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);                                
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return listaAsientos;
    }
    
     public AsientoFichaExpedienteVO cargaAsientoExterno(RegistroValueObject gVO, String[] params) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        AsientoFichaExpedienteVO asiento = new AsientoFichaExpedienteVO();       

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
           

            Long numeroRegistro = gVO.getNumReg();
            String procedimiento =  gVO.getCodProcedimiento();
            Integer ejercicio = gVO.getAnoReg();
            
            String query = "SELECT DISTINCT EXREXT_TIP, EXREXT_FECALTA, EXREXT_NRE FROM E_EXREXT WHERE EXREXT_NRE = ? AND EXREXT_TIP=?";
            Integer i=1;
            if (gVO.getCodProcedimiento()!=null){
                query += " AND EXREXT_PRO = ?";
            }
            if (ejercicio!=0){
                 query += " AND EXREXT_EJR = ?";
            }
            
            st = con.prepareStatement(query);
            if(m_Log.isDebugEnabled()) m_Log.debug(query);
            st.setLong(i, numeroRegistro);
            i++;
            st.setString(i,gVO.getTipoReg());
            i++;
            if (gVO.getCodProcedimiento()!=null){
                st.setString(i,procedimiento);
                i++;
            }
            if (ejercicio!=0){
                st.setInt(i, ejercicio);
                i++;
            }
            
            
            
            rs = st.executeQuery();
            
            while(rs.next()){

                
                asiento.setTipoAsiento(rs.getString("EXREXT_TIP"));
                Date fecha = rs.getDate("EXREXT_FECALTA");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                asiento.setFechaAsiento(sdf.format(fecha));
                asiento.setNumeroAsiento(rs.getLong("EXREXT_NRE"));
                             
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);                                
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return asiento;
    }
     
     
     
     
    private String getCodigoCargoTodos(Connection con){
        Statement st = null;
        ResultSet rs = null;
        String codigoCargoTodos = "";
        
        try{
            String sql = "SELECT CAR_COD FROM A_CAR WHERE CAR_COD_VIS='TD'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery(sql);
            
            while(rs.next()) {
                codigoCargoTodos = rs.getString("CAR_COD");
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
        
        return codigoCargoTodos;
    } 
     
     
    public boolean verificarSubsanacionDocumento(String numExpediente,String codProcedimiento,String numDocumento,UsuarioValueObject usuario,Connection con){    
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            m_Log.debug("verificar codProcedimiento: " + codProcedimiento + ",numDocumento: " + numDocumento);
            
            String codigoCargoTodos = getCodigoCargoTodos(con);
            
            
            String sqlPendientes = "SELECT PRESENTADO_COD_DOC,ID_DOC_PRESENTADO,doc_firma_orden,pml_cod,PML_VALOR,PRESENTADO_NUM,DOC_FIRMA_FECHA,DOC_FECHA_ENVIO,ID_DOC_FIRMA,DOC_FIRMA_USUARIO,DOC_FIRMA_ESTADO,"
                    + "PRESENTADO_NOMBRE, FIRMA_FIN_REC,FIRMA_TRA_SUB FROM E_DOCS_FIRMAS firmas LEFT JOIN E_DOCS_PRESENTADOS "
                    + "ON (PRESENTADO_COD       =ID_DOC_PRESENTADO) "
                    + "LEFT JOIN E_PML ON (PRESENTADO_PRO=PML_COD) "
                    +" LEFT JOIN E_DEF_FIRMA DF ON (FIRMA_PROC =PRESENTADO_PRO AND DOC_FIRMA_ORDEN = DF.FIRMA_ORDEN AND PRESENTADO_COD_DOC = DF.FIRMA_COD_DOC)"
                    //+ " WHERE ( ";
                    + " WHERE E_DOCS_PRESENTADOS.PRESENTADO_PRO='" + codProcedimiento + "' AND E_DOCS_PRESENTADOS.PRESENTADO_NUM='" + numExpediente + "' AND PRESENTADO_COD_DOC=" + numDocumento 
                    + " AND (";
            
            sqlPendientes += " doc_firma_usuario IN (" + usuario.getIdUsuario() + ")";
            
            sqlPendientes += " OR (EXISTS "
                    + "(SELECT uor_cod FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "a_uou ON (usu_cod=uou_usu "
                    + "AND uou_org=" + usuario.getOrgCod()
                    + ") LEFT JOIN a_uor ON (uou_uor  =uor_cod) WHERE ";
                        
            sqlPendientes += " usu_cod IN (" + usuario.getIdUsuario() + ")";
            
            sqlPendientes += "AND uor_cod  =firmas.doc_firma_uor) AND (doc_firma_cargo IS NULL ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlPendientes += " OR doc_firma_cargo    ='" + codigoCargoTodos + "'";
            }
            sqlPendientes += ") and doc_firma_usuario is null ) "
                    + "OR (EXISTS (SELECT uor_cod, car_cod FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "a_uou "
                    + "ON (usu_cod=uou_usu AND uou_org=" + usuario.getOrgCod() + ") LEFT JOIN a_uor ON (uou_uor=uor_cod) LEFT JOIN a_car "
                    + "ON (car_cod  =uou_car) WHERE ";
            
            
            sqlPendientes += " usu_cod IN (" + usuario.getIdUsuario() + ")";
            
            sqlPendientes += "AND uor_cod  =firmas.doc_firma_uor AND ";
            sqlPendientes += "(car_cod  =firmas.doc_firma_cargo ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlPendientes += " OR CAR_COD= " + codigoCargoTodos;
            }
            sqlPendientes += ")) AND (doc_firma_cargo IS NOT NULL ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlPendientes += "AND doc_firma_cargo  <>'" + codigoCargoTodos + "'";
            }
            sqlPendientes += ") and doc_firma_usuario is null ) ) AND doc_firma_estado  ='O' AND NOT EXISTS (SELECT * FROM e_docs_firmas aux WHERE "
                    + "firmas.id_doc_presentado=aux.id_doc_presentado AND firmas.doc_firma_orden    >aux.doc_firma_orden) ";
                    //+ "AND aux.doc_firma_estado='O' )";
                        

            m_Log.debug(sqlPendientes);
            
            st = con.createStatement();
            rs = st.executeQuery(sqlPendientes);
            
            String subsanacion = null;
            while(rs.next()){
               subsanacion = rs.getString("FIRMA_TRA_SUB");                
               break;
            }
            
            if(subsanacion!=null && "1".equalsIgnoreCase(subsanacion))
                exito = true;            
            
        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error(" Error ");
        }
        catch(Exception e){
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
    
    public int obtenerEstadoExpediente(String numExpediente, Connection con) throws SQLException, Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        int estado = -1;
        
        try{
            sql = "SELECT EXP_EST FROM E_EXP WHERE EXP_NUM = ?";
                        
            m_Log.debug("Query: " + sql);
            m_Log.debug("Parámetros de la query: " + numExpediente);
            
            ps = con.prepareStatement(sql);
            ps.setString(1, numExpediente);
            rs = ps.executeQuery();
            
            if(rs.next()){
               estado = rs.getInt("EXP_EST");
            } 
        }catch(SQLException e){
            throw e;
        } catch(Exception e){
            throw e;
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                m_Log.error("Ha ocurrido un error al liberar recursos de base de datos: " + e.getMessage(), e);
                throw e;
            }
        }
        
        return estado;
    }     
    
       public String validarCamposCal(String[] params,GeneralValueObject gVO) {  
        
        AdaptadorSQLBD oad = null;
        Connection con = null;        
        Statement stmt = null;
        ResultSet rs = null;
        String retorno = "0";
        try
        {
                oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();

                String codMunicipio = (String)gVO.getAtributo("codMunicipio");
                String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");       
                String numero = (String)gVO.getAtributo("numero");
                String ocurrencia = (String)gVO.getAtributo("ocurrencia");
                String tramite = (String)gVO.getAtributo("tramite");        
                String sql = "";                 
                String valor = "";              
                
                //sqlserver ISNULL(CONVERT(varchar, NUM.TNUCT_VALOR) ,'-')  
                
               
                sql = " SELECT EXP.COD_CAMPO AS CAMPO, "+oad.convertir("NUM.TNUCT_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)+" AS VALOR" +
                      " FROM EXPRESION_CAMPO_CAL_TRAM EXP" +
                      " LEFT JOIN E_TNUCT NUM" +
                      " ON NUM.TNUCT_PRO = EXP.COD_PROCEDIMIENTO" +
                      " AND NUM.TNUCT_TRA = EXP.COD_TRAMITE" +
                      " AND NUM.TNUCT_MUN = EXP.COD_ORGANIZACION" +
                      " AND NUM.TNUCT_COD = EXP.COD_CAMPO" + 
                      " AND NUM.TNUCT_NUM = '" + numero +"'"+
                      " AND NUM.TNUCT_OCU = " + ocurrencia + 
                      " WHERE EXP.COD_ORGANIZACION = " + codMunicipio +
                      " AND EXP.COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +                      
                      " AND EXP.COD_TRAMITE = " + tramite +
                      " AND EXP.TIPO_DATO = 8 ";
                sql = sql + " UNION ALL ";
                sql = sql + " SELECT EXP.COD_CAMPO AS CAMPO,"+oad.convertir("NUM.TFECT_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS VALOR" +
                      " FROM EXPRESION_CAMPO_CAL_TRAM EXP" +
                      " LEFT JOIN E_TFECT NUM" +
                      " ON NUM.TFECT_PRO = EXP.COD_PROCEDIMIENTO" +
                      " AND NUM.TFECT_TRA = EXP.COD_TRAMITE" +
                      " AND NUM.TFECT_MUN = EXP.COD_ORGANIZACION" +
                      " AND NUM.TFECT_COD = EXP.COD_CAMPO" + 
                      " AND NUM.TFECT_NUM = '" + numero +"'"+
                      " AND NUM.TFECT_OCU = " + ocurrencia + 
                      " WHERE EXP.COD_ORGANIZACION = " + codMunicipio +
                      " AND EXP.COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +                      
                      " AND EXP.COD_TRAMITE = " + tramite + 
                      " AND EXP.TIPO_DATO = 9 ";
                sql = sql + " UNION ALL "; 
                sql = sql + " SELECT EXP.COD_CAMPO AS CAMPO, "+oad.convertir("NUM.TFEC_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+"AS VALOR " +
                      " FROM EXPRESION_CAMPO_CAL_PROC EXP" +
                      " LEFT JOIN E_TFEC NUM" +
                      " ON NUM.TFEC_MUN = EXP.COD_ORGANIZACION" +
                      " AND NUM.TFEC_COD = EXP.COD_CAMPO" + 
                      " AND NUM.TFEC_NUM = '" + numero +"'"+
                      " WHERE EXP.COD_ORGANIZACION = " + codMunicipio +         
                      " AND EXP.COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +     
                      " AND EXP.COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +                      
                      " AND EXP.TIPO_DATO = 9 ";   
                sql = sql + " UNION ALL ";
                sql = sql + " SELECT EXP.COD_CAMPO AS CAMPO,"+oad.convertir("NUM.TNUC_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)+"  AS VALOR" +
                      " FROM EXPRESION_CAMPO_CAL_PROC EXP" +
                      " LEFT JOIN E_TNUC NUM" +
                      " ON NUM.TNUC_MUN = EXP.COD_ORGANIZACION" +
                      " AND NUM.TNUC_COD = EXP.COD_CAMPO" + 
                      " AND NUM.TNUC_NUM = '" + numero +"'"+
                      " WHERE EXP.COD_ORGANIZACION = " + codMunicipio +             
                      " AND EXP.COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +     
                      " AND EXP.COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +                      
                      " AND EXP.TIPO_DATO = 8 ";
            m_Log.debug(sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql); 

            while (rs.next()) { 
                
                valor = rs.getString("VALOR");
                if ("-".equals(valor))
                    retorno = "1";                
                if ("null".equals(valor)) retorno = "1";
                if (valor==null) 
                {                   
                    retorno = "1";
                }
                                    
            }             
        } 
        catch (Exception e) {
            retorno = "1";
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
                
                String sql = "";                 
                           
                sql = " SELECT EXPRESION FROM EXPRESION_CAMPO_CAL_PROC " +                      
                      " WHERE COD_ORGANIZACION = " + codMunicipio +
                      " AND COD_PROCEDIMIENTO = '" + codProcedimiento + "'" +                      
                      " AND COD_CAMPO = '" + campo +"'";               
            
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
                     
        Statement stmt = null; 
        ResultSet rs = null;       
        String retorno = "";        
        try 
        {            

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");        
            String campo = (String)gVO.getAtributo("campo");

            String sql = " SELECT PCA_DESPLEGABLE FROM E_PCA " +                      
                         " WHERE PCA_MUN = " + codMunicipio +
                         " AND PCA_PRO = '" + codProcedimiento + "'" +                      
                         " AND PCA_COD = '" + campo +"'";               
            
            stmt = con.createStatement(); 
            m_Log.debug(sql);
            rs = stmt.executeQuery(sql);       
            if (rs.next()) 
                retorno = rs.getString("PCA_DESPLEGABLE").trim();                                                      
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
             
        Statement stmt = null; 
        ResultSet rs = null;
        String[] retorno = new String[7]; 
        String[] parametros = new String[7];
        try 
        {                                                                                             
                String sql = " SELECT DRIVER_JDBC,URL_JDBC,USUARIO,PASSWORD,TABLA,CAMPO_CODIGO,CAMPO_VALOR " +
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
        //*****parametros*******//
        //*** 0 - Driver jdbc
        //*** 1 - url jdbc
        //*** 2 - usuaio bbdd
        //*** 3 - password
        //*** 4 - tabla de consulta
        //*** 5 - campo de consulta

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
            String valor_externo = parametros[6].toString(); //Recuperaremos los valores
            String sql = " SELECT " + campo_externo + ","+valor_externo+" FROM " + tabla_externa +" order by "+valor_externo;
            
            stmt = con.createStatement();
            m_Log.debug(sql);
            rs = stmt.executeQuery(sql);                     
            
            while(rs.next()) 
            {
                GeneralValueObject gVO=new GeneralValueObject();
                gVO.setAtributo("codigo", rs.getString(campo_externo) );
                gVO.setAtributo("valor", rs.getString(valor_externo) );                
                resultados.add(gVO);
            }
            
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
        return resultados;
    }
    public String interesadoObligatorio(Connection con,GeneralValueObject gVO) {  
                     
        Statement stmt = null; 
        ResultSet rs = null;       
        String retorno = "";        
        try 
        {            

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");        

            String sql = " SELECT PRO_INTOBL FROM E_PRO " +                      
                         " WHERE PRO_MUN = " + codMunicipio +
                         " AND PRO_COD = '" + codProcedimiento + "'" ;
            
            stmt = con.createStatement(); 
            rs = stmt.executeQuery(sql);       
            if (rs.next()) 
                retorno = rs.getString("PRO_INTOBL").trim();                                                      
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
            }catch(SQLException e){ 
               e.printStackTrace();
           } 
        }
        return retorno;
    }
    
     /**
     * Comprueba la existencia de un interesado asociado a una anotacion.
     * @param gVO GeneralValueObject con la información de la anotación y el interesado
     * @param con Conexion
     * @return true si existe el asiento, false en caso contrario
     * @throws AnotacionRegistroException
     * @throws TechnicalException
     */
    public TercerosValueObject existeInteresadoRegistro(GeneralValueObject gVO, Connection con) throws AnotacionRegistroException,TechnicalException{

        m_Log.debug("AnotacionRegistroDAO.existeInteresadoRegistro(): BEGIN");
        
        TercerosValueObject tercero = null;
        
        String codUOR = (String) gVO.getAtributo("codUnidadRegistro");
        String tipoAsiento = (String) gVO.getAtributo("tipoAsiento");
        String ejercicio = (String) gVO.getAtributo("ejercicioAsiento");
        String numeroAsiento = (String) gVO.getAtributo("numeroAsiento");
        String codDepartamento = (String) gVO.getAtributo("codDepartamento");
        String codTercero = (String) gVO.getAtributo("codTercero");
        String codRol = (String) gVO.getAtributo("codRol");

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT EXT_TER, EXT_NVR" +
                         " FROM R_EXT" +
                         " WHERE EXT_UOR = ? AND EXT_TIP = ? AND EXT_EJE = ? AND EXT_NUM = ?" +
                         " AND EXT_DEP = ? AND EXT_TER = ? AND EXT_ROL = ?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            ps = con.prepareStatement(sql);
            int i = 1;
            if (codUOR !=null){ps.setInt(i++, Integer.valueOf(codUOR).intValue());}
            ps.setString(i++, tipoAsiento);
            if (ejercicio != null){ps.setInt(i++, Integer.valueOf(ejercicio).intValue());}
            if (numeroAsiento != null){ps.setInt(i++, Integer.valueOf(numeroAsiento).intValue());}
            if (codDepartamento != null){ps.setInt(i++, Integer.valueOf(codDepartamento).intValue());}
            if (codTercero != null){ps.setInt(i++, Integer.valueOf(codTercero).intValue());}
            if (codRol != null){ps.setInt(i++, Integer.valueOf(codRol).intValue());}            
                         
            rs = ps.executeQuery();

            if (rs.next()) {
                tercero = new TercerosValueObject();                
                tercero.setCodTerceroOrigen(String.valueOf(rs.getInt("EXT_TER")));  
                tercero.setVersion(String.valueOf(rs.getInt("EXT_NVR")));
            }
            
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) {m_Log.error("Error al comprobar si un interesado esta asociado a una anotación");}
            sqle.printStackTrace();            
            throw new AnotacionRegistroException("Error al comprobar si un interesado esta asociado a una anotación", sqle);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        
        m_Log.debug("AnotacionRegistroDAO.existeInteresadoRegistro(): END");
        
        return tercero;
        
    }
    
    
    
     /**
     * Actuliza la notificación electronica del interesado del expediente
     * @param gVO GeneralValueObject con la información del asiento
     * @param tercero Información del tercero
     * @param con Conexion
     * @return true si existe el asiento, false en caso contrario
     * @throws AnotacionRegistroException
     * @throws TechnicalException
     */
     public int actualizarInteresadoExpediente(GeneralValueObject gVO, TercerosValueObject tercero, Connection con) throws TechnicalException {
        
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codTercero = tercero.getCodTerceroOrigen();
        String version = tercero.getVersion();        
        
        String notificacionElectronica = ((String) gVO.getAtributo("notifElect")).trim(); 
        String codRol = ((String) gVO.getAtributo("codRol")).trim(); 

        PreparedStatement ps = null;

        try {
            
            String sql = "UPDATE E_EXT SET EXT_NOTIFICACION_ELECTRONICA = ?"
            + " WHERE EXT_MUN  = ? AND EXT_EJE = ? AND EXT_NUM = ? AND EXT_TER = ? AND EXT_NVR = ? AND EXT_ROL = ?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }            
            
            ps = con.prepareStatement(sql);

            int i = 1;
            ps.setString(i++, notificacionElectronica);
            if (codMunicipio !=null){ps.setInt(i++, Integer.valueOf(codMunicipio).intValue());}            
            if (ejercicio !=null){ps.setInt(i++, Integer.valueOf(ejercicio).intValue());}                        
            ps.setString(i++, numeroExpediente);
            if (codTercero !=null){ps.setInt(i++, Integer.valueOf(codTercero).intValue());} 
            if (version !=null){ps.setInt(i++, Integer.valueOf(version).intValue());}             
            if (codRol !=null){ps.setInt(i++, Integer.valueOf(codRol).intValue());}                

            int updatedRows = ps.executeUpdate();

            return updatedRows;

        } catch(SQLException sqle) {
            if (m_Log.isErrorEnabled()) {m_Log.error("Error al actualizar el campo notificacion electronica del interesado asociado al expediente");}
            sqle.printStackTrace();            
            throw new TechnicalException("Error al actualizar el campo notificacion electronica del interesado asociado al expediente", sqle);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }        
        
    }

    
     
    /**
     * Recupera un único interesado que tenga el rol por defecto en un expedinete
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la BBDDF
     * @return Objeto TercerosValueObject con la info del interesado
     * @throws TechnicalException si ocurre algún error
     */
     public TercerosValueObject getInteresadoRolPorDefecto(String codOrganizacion , String numExpediente,Connection con) throws TechnicalException{
       TercerosValueObject tercero = null;
       ResultSet rs = null;
       Statement st = null;
       try{
           String[] datos = numExpediente.split("/");
           String ejercicio = datos[0];
           String codProcedimiento = datos[1];
           
           String sql = "SELECT HTE_TER,HTE_NVR,HTE_TID,HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2,HTE_DCE,HTE_TLF FROM E_EXT,E_ROL,T_HTE WHERE EXT_NUM='" + numExpediente + "' AND EXT_EJE=" + ejercicio  + 
                       " AND EXT_MUN=" + codOrganizacion + " AND EXT_MUN=ROL_MUN AND EXT_ROL=ROL_COD AND ROL_PCW=1 AND ROL_PRO='"  + codProcedimiento + "'" +
                       " AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR";
           
           m_Log.debug("sql: " + sql);
           
           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next()){
               tercero = new TercerosValueObject();
               tercero.setNombre(rs.getString("HTE_NOM"));
               tercero.setApellido1(rs.getString("HTE_AP1"));
               tercero.setApellido2(rs.getString("HTE_AP2"));
               tercero.setVersion(rs.getString("HTE_NVR"));
               tercero.setIdentificador(rs.getString("HTE_TER"));
               tercero.setCodTerceroOrigen(rs.getString("HTE_TER"));
               tercero.setDocumento(rs.getString("HTE_DOC"));
               tercero.setTipoDocumento(rs.getString("HTE_TID"));
               tercero.setEmail(rs.getString("HTE_DCE"));
               tercero.setTelefono(rs.getString("HTE_TLF"));
               
               
               break;                       
           }           
           
       }catch(SQLException e){
           e.printStackTrace();
           throw new TechnicalException("Error al recuperar el interesado",e);
       }finally{
           try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }
              
       return tercero;       
   }
     
     
     
     
     /**
      * Comprueba si existe un determinado expediente
      * @param codOrganizacion: Código de la organización
      * @param numExpediente: Número del expediente
      * @param con: Conexión a la BBDD
      * @return True si existe o false en caso contrario
      * @throws TechnicalException si ocurre algún error
      */
     public boolean existeExpediente(int codOrganizacion,String numExpediente,Connection con) throws TechnicalException{
         boolean exito = false;
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try{
             String[] datos   = numExpediente.split("/");
             String ejercicio = datos[0];
             
             String sql = "SELECT COUNT(*) AS NUM FROM E_EXP WHERE EXP_NUM=? AND EXP_MUN=? AND EXP_EJE=?";
             int i = 1;
             ps = con.prepareStatement(sql);
             ps.setString(i++, numExpediente);
             ps.setInt(i++,codOrganizacion);
             ps.setInt(i++,Integer.parseInt(ejercicio));
             
             int numero = 0;
             rs = ps.executeQuery();
             while(rs.next()){
                 numero = rs.getInt("NUM");
             }
             
             if(numero==1) exito = true;
             
         }catch(Exception e){
             e.printStackTrace();
             throw new TechnicalException(e.getMessage(),e);
         }finally{
             try{
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();
             }catch(Exception e){
                 e.printStackTrace();
             }
         }
         
         return exito;
     }
     
     
     
     
     public boolean estaTramiteAbierto(GeneralValueObject gVO,String codTramite,String ocurrenciaTramite, Connection con) throws TechnicalException{
         boolean exito = false;
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try{
             
             String codMunicipio = (String)gVO.getAtributo("codMunicipio");
             String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
             String ejercicio = (String)gVO.getAtributo("ejercicio");
             String numExpediente = (String)gVO.getAtributo("numero");
             
             String sql = "SELECT COUNT(*) AS NUM FROM E_CRO WHERE CRO_PRO=? AND CRO_MUN=?  AND CRO_EJE=? AND CRO_NUM=? AND CRO_TRA=? AND CRO_OCU=? and cro_fef is null";
             int i = 1;
             ps = con.prepareStatement(sql);
             ps.setString(i++,codProcedimiento);
             ps.setInt(i++,Integer.parseInt(codMunicipio));
             ps.setInt(i++,Integer.parseInt(ejercicio));
             ps.setString(i++, numExpediente);
             ps.setInt(i++,Integer.parseInt(codTramite));
             ps.setInt(i++,Integer.parseInt(ocurrenciaTramite));
             
             int numero = 0;
             rs = ps.executeQuery();
             while(rs.next()){
                 numero = rs.getInt("NUM");
             }
             
             if(numero==1) exito = true;
             
         }catch(Exception e){
             e.printStackTrace();
             throw new TechnicalException(e.getMessage(),e);
         }finally{
             try{
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();
             }catch(Exception e){
                 e.printStackTrace();
             }
         }
         
         return exito;
     }
     
     
     public boolean tieneTramitesAbiertos(GeneralValueObject gVO, Connection con) throws TechnicalException{
         boolean exito = false;
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try{
             
             String codMunicipio = (String)gVO.getAtributo("codMunicipio");
             String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
             String ejercicio = (String)gVO.getAtributo("ejercicio");
             String numExpediente = (String)gVO.getAtributo("numero");
             
             String sql = "SELECT COUNT(*) AS NUM FROM E_CRO WHERE CRO_PRO=? AND CRO_MUN=?  AND CRO_EJE=? AND CRO_NUM=? and cro_fef is null";
             m_Log.debug("sql: " + sql);
             int i = 1;
             ps = con.prepareStatement(sql);
             ps.setString(i++,codProcedimiento);
             ps.setInt(i++,Integer.parseInt(codMunicipio));
             ps.setInt(i++,Integer.parseInt(ejercicio));
             ps.setString(i++, numExpediente);
     
             
             int numero = 0;
             rs = ps.executeQuery();
             while(rs.next()){
                 numero = rs.getInt("NUM");
             }
             
             if(numero>0) exito = true;
             
         }catch(Exception e){
             e.printStackTrace();
             throw new TechnicalException(e.getMessage(),e);
         }finally{
             try{
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();
             }catch(Exception e){
                 e.printStackTrace();
             }
         }
         
         return exito;
     }
     
     
     
     /**
      * Método que permite relacionar 2 expedientes entre si. La relación tiene que ser bidireccional, se
      * relaciona el expediente del parámetro numExpedienteOriginal con el del parámetro numExpedienteRelacionado y 
      * viceversa
      * @param codOrganizacion: Código de la organización
      * @param numExpedienteOriginal: Número del expediente 
      * @param numExpedienteRelacionado: Número del expediente con el que se relaciona.
      * @param con: Conexión a la BBDD
      * @return
      * @throws TechnicalException REcu
      */
      public boolean relacionarExpediente(int codOrganizacion,String numExpedienteOriginal,String numExpedienteRelacionado,Connection con) throws TechnicalException{
         boolean exito = false;
         PreparedStatement ps = null;
                  
         try{
             String[] datosOriginal   = numExpedienteOriginal.split("/");
             String ejercicioOriginal = datosOriginal[0];
                          
             String[] datosRelacionado = numExpedienteRelacionado.split("/");
             String ejercicioRelacionado = datosRelacionado[0];             
             
             String sql = "INSERT INTO E_REX(REX_MUN,REX_EJE,REX_NUM,REX_MUNR,REX_EJER,REX_NUMR) VALUES(?,?,?,?,?,?)";
             int i = 1;
             ps = con.prepareStatement(sql);
             ps.setInt(i++,codOrganizacion);
             ps.setInt(i++,Integer.parseInt(ejercicioOriginal));             
             ps.setString(i++, numExpedienteOriginal);
             
             ps.setInt(i++,codOrganizacion);
             ps.setInt(i++,Integer.parseInt(ejercicioRelacionado));             
             ps.setString(i++, numExpedienteRelacionado);
             
             int rowsUpdated = ps.executeUpdate();
             if(rowsUpdated==1){
                ps.close();
                 
                i = 1;
                ps = con.prepareStatement(sql);
                
                ps.setInt(i++,codOrganizacion);
                ps.setInt(i++,Integer.parseInt(ejercicioRelacionado));             
                ps.setString(i++, numExpedienteRelacionado);
                
                ps.setInt(i++,codOrganizacion);
                ps.setInt(i++,Integer.parseInt(ejercicioOriginal));             
                ps.setString(i++, numExpedienteOriginal);
                
                int rowsUpdated2 = ps.executeUpdate();
                if(rowsUpdated2==1) exito = true;
                 
             }
             
         }catch(Exception e){
             e.printStackTrace();
             throw new TechnicalException(e.getMessage(),e);
         }finally{
             try{
                 if(ps!=null) ps.close();         
             }catch(Exception e){
                 e.printStackTrace();
             }
         }
         
         return exito;
     }
     
      
      
      
    /****************** NUEVO ***************************/
    
    
     /**
     * Recupera los enlaces asignados a un determinado procedimiento
     * @param codMunicipio: Código del municipio
     * @param codProcedimiento: Código del procedimiento
     * @return ArrayList<EnlaceVO>
     */
    public ArrayList<EnlaceVO> cargaListaEnlacesActivos(int codMunicipio,String codProcedimiento,Connection con) throws TechnicalException {
        ArrayList<EnlaceVO> salida = new ArrayList<EnlaceVO>();
        PreparedStatement ps = null;        
        ResultSet rs = null;
        String sql = null;

        try{
                      
            sql ="SELECT ENP_DES,ENP_URL FROM E_ENP WHERE ENP_MUN=? AND ENP_PRO=? AND ENP_EST=1";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,codProcedimiento);
            
            rs = ps.executeQuery();

            while(rs.next()){
                EnlaceVO enlace = new EnlaceVO();
                enlace.setDescripcion(rs.getString("ENP_DES"));
                enlace.setUrl(rs.getString("ENP_URL"));
                enlace.setEstado("1");
                salida.add(enlace);
            }
            
        }catch (Exception e){            
            if(m_Log.isErrorEnabled()) m_Log.error("Error al recuperar los enlaces asociados al procedimiento: " + e.getMessage());
            e.printStackTrace();
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }        
        return salida;
    }

    
    
    /**************************/

    public ArrayList<DocumentoInicioExpedienteVO> cargaListaDocumentosExpediente(DocumentoInicioExpedienteVO doc,AdaptadorSQLBD oad,Connection con) throws TechnicalException {
        ArrayList<DocumentoInicioExpedienteVO> documentos = new ArrayList<DocumentoInicioExpedienteVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        int codMunicipio = doc.getCodOrganizacion();
        String codProcedimiento = doc.getCodProcedimiento();
        String ejercicio = doc.getEjercicio();
        String numero    = doc.getNumExpediente();
       
        try{
                       
            String from = sql_dop_codDoc +", dpml1."+sql_dpml_valor+" AS nombreDocumento,dpml2."+sql_dpml_valor+" AS condicion "
                    + ", " + oad.convertir(sql_doe_fEntreg, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fEntrega ";
            ArrayList join = new ArrayList();
            join.add("E_DOP");
            join.add("INNER");
            join.add("e_dpml dpml1");
            join.add(sql_dop_codMun +"="+codMunicipio
                     + " AND "+ sql_dop_codPro+"='"+codProcedimiento+"' "
                     + " AND e_dop."+sql_dop_codMun+"=dpml1."+sql_dpml_codMun
                     + " AND e_dop."+sql_dop_codPro+"=dpml1."+sql_dpml_codProc
                     + " AND e_dop."+sql_dop_codDoc+"=dpml1."+sql_dpml_codDoc);
            join.add("INNER");
            join.add("e_dpml dpml2");
            join.add("e_dop."+sql_dop_codMun+"=dpml2."+sql_dpml_codMun
                     + " AND e_dop."+sql_dop_codPro+"=dpml2."+sql_dpml_codProc
                     + " AND e_dop."+sql_dop_codDoc+"=dpml2."+sql_dpml_codDoc);
            join.add("LEFT");
            join.add("(SELECT * FROM E_DOE WHERE " + sql_doe_ejer + " ='" + ejercicio + "' AND " + sql_doe_num + " ='" + numero + "') docs");
            join.add( "e_dop." + sql_dop_codDoc + " = docs." + sql_doe_codDoc 
            		+ " AND  e_dop." + sql_dop_codPro + " =docs." +  sql_doe_codProc
            		+ " AND  e_dop." + sql_dop_codMun + " = docs." +  sql_doe_codMun);
            join.add("false");
            
            String where = "dpml1."+sql_dpml_campo+"='NOM'" + " AND " +
            		"dpml1."+sql_dpml_idioma+"='"+idiomaDefecto+"'" + " AND " +
            		"dpml2."+sql_dpml_campo+"='CON'" + " AND " +
            		"dpml2."+sql_dpml_idioma+"='"+idiomaDefecto+"'";

            String sql = oad.join(from, where, (String[]) join.toArray(new String[]{}));
            sql += " ORDER BY fEntrega DESC, nombreDocumento ASC";            
            
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            String nombreDoc = null;
            String tipoDoc = null;
            
            while(rs.next()){
                DocumentoInicioExpedienteVO aux = new DocumentoInicioExpedienteVO();
                
                aux.setCodigo(rs.getInt("DOP_COD"));
                aux.setNombreDocumento(rs.getString("nombreDocumento"));
                aux.setCondicion(rs.getString("condicion"));
                
                String fechaEntrega = rs.getString("fEntrega");
                aux.setFechaEntrega(fechaEntrega);
                aux.setEntregado(false);
                if(fechaEntrega!=null)
                    aux.setEntregado(true);
                
                aux.setCodOrganizacion(codMunicipio);
                aux.setEjercicio(ejercicio);
                aux.setNumExpediente(numero);
                aux.setCodProcedimiento(codProcedimiento);
                aux.setExpedienteHistorico(doc.isExpedienteHistorico());
                
                aux = this.cargarDatosAdjuntoDocumentoExpediente(aux,con);                
                documentos.add(aux);
            }
         

        }catch (Exception e){
            documentos = new ArrayList<DocumentoInicioExpedienteVO>(); 
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        return documentos;
    }
    
    
    /**
     * Comprueba si el expediente se ha iniciado en MiCarpeta
     * @param params datos de la conexión
     * @return 1 si no está en MiCarpeta, 2 si está en MiCarpeta
     */
    public String expedienteEnMiCarpeta(String[] params,String numExpediente) {  
        
        AdaptadorSQLBD oad = null;
        Connection con = null;        
        Statement stmt = null;
        ResultSet rs = null;
        String retorno = "1";
        try
        {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            String sql = "select EXP_NUM,TER_TID,TER_DOC from MELANBIDE43_INTEGMISGEST where EXP_NUM='" + numExpediente + "' and TIPO_OPERACION='I' and RESULTADO_PROCESO='1'";
                
            m_Log.debug(sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql); 

            while (rs.next()) { 
                retorno = "2";                 
            }             
        } 
        catch (Exception e) {
            retorno = "0";
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
    
    
    /**
     * Comprueba si el expediente no tiene roles repetidos y sólo tiene 1 rol 1 (debe devolver 1 fila CANT=1 EXT_ROL=1)
     * @param params datos de la conexión
     * @return 3 roles incorrectos, 6 roles correctos
     */
    public String rolesCorrectos(String[] params,String numExpediente) {  
        
        AdaptadorSQLBD oad = null;
        Connection con = null;        
        Statement stmt = null;
        ResultSet rs = null;
        String retorno = "3";
        int filasRol1 = 0;
        int filas = 0;
        try
        {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            String sql = "select count(*) CANT, EXT_ROL from E_EXT where EXT_NUM='" + numExpediente + "' group by EXT_ROL having count(*)>1\n" +
                            "union all\n" +
                            "select count(*) CANT, EXT_ROL from E_EXT where EXT_NUM='" + numExpediente + "' and EXT_ROL=1 group by EXT_ROL having count(*)=1";
                
            m_Log.debug(sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql); 

            while (rs.next()) { 
                int cant = rs.getInt("CANT");
                int rol = rs.getInt("EXT_ROL");
                if (cant == 1 && rol == 1){
                    filasRol1++;
                } 
                filas++;
            }   
            if (filasRol1 == 1 && filas == 1){
                retorno = "6";
            }
        } 
        catch (Exception e) {
            retorno = "0";
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
 
    
    /**
     * Actualiza la marca de notificación electrónica (en caso de marcar, del representante (ext_rol=2) y si no, del interesado (ext_rol=1))
     * @param params datos de la conexión
     * @return 5 si ha marcado, 4 si ha desmarcado
     * @throws TechnicalException
     */ 
    public String actualizarMarcaNotificacion(String[] params, String numExpediente, String checkMarca) throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = null;
        String retorno="0";
        int result = 0;

        try{

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            if (checkMarca.equals("1")) {
                //marcar
                sql = "update e_ext\n" +
                        "set ext_notificacion_electronica = \n" +
                        "(case \n" +
                        "    when ((ext_rol=2 or (ext_rol=1 and (select count(*) from e_ext where ext_num='" + numExpediente + "' and ext_rol=2)=0))) then 1\n" +
                        "    when (ext_rol<>2 and ((select count(*) from e_ext where ext_num='" + numExpediente + "' and ext_rol=2) > 0)) then 0\n" +
                        "end)\n" +
                        "where ext_num='" + numExpediente + "'";
                retorno = "5";
            } else if (checkMarca.equals("0")) {
                //desmarcar
                sql = "update e_ext \n" +
                        "set ext_notificacion_electronica=0\n" +
                        "where ext_num='" + numExpediente + "'";
                retorno = "4";
            }
            
            m_Log.debug("sql actualizarMarcaNotificacion :" + sql);
            ps = con.prepareStatement(sql);
            result = ps.executeUpdate();
            ps.close();

            if (result >= 0)
                oad.finTransaccion(con);
            else
                oad.rollBack(con);

        }catch (Exception e){
            try{
                oad.rollBack(con);
            }catch(BDException bde){
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
            result=-1;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try{
                if (ps!=null) ps.close();
                oad.devolverConexion(con);
            }catch(Exception bde) {
                result=-1;
                bde.printStackTrace();
                bde.getMessage();
            }
        }
        return retorno;
    }
     

    /**
     * Comprueba si un documento de expediente tiene asociado un adjunto y en ese caso, recupera el nombre del adjunto, fecha de alta y su tipo mime
     * @param codMunicipio: Código de municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número de expediente
     * @param codProcedimiento: Código de procedimiento
     * @param codDocumento: Código de documento
     * @param con: Conexión a la base de datos
     * @return GeneralValueObject con el tipo mime, nombre del fichero y su fecha de alta
     */
    private DocumentoInicioExpedienteVO cargarDatosAdjuntoDocumentoExpediente(DocumentoInicioExpedienteVO doc, Connection con) throws TechnicalException{
        ResultSet rs = null;
        PreparedStatement ps  =null;
        GeneralValueObject gVO = null;
        try{

            String sql = "";
            
            if(!doc.isExpedienteHistorico()) { 
                sql = "SELECT PRESENTADO_COD, PRESENTADO_TIPO,PRESENTADO_NOMBRE,PRESENTADO_FECHA_ALTA " +
                      "FROM E_DOCS_PRESENTADOS " +
                      "WHERE PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? " +
                      "AND PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";            
            }else {
                sql = "SELECT PRESENTADO_COD, PRESENTADO_TIPO,PRESENTADO_NOMBRE,PRESENTADO_FECHA_ALTA " +
                      "FROM HIST_E_DOCS_PRESENTADOS " +
                      "WHERE PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? " +
                      "AND PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";            
            }
                
                
            m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            
            int i=1;
            ps.setInt(i++,doc.getCodOrganizacion());
            ps.setInt(i++,Integer.parseInt(doc.getEjercicio()));
            ps.setString(i++,doc.getNumExpediente());
            ps.setString(i++,doc.getCodProcedimiento());
            ps.setInt(i++,doc.getCodigo());
            rs = ps.executeQuery();
            
            while(rs.next()){
                int codDocumentoPresentado = rs.getInt("PRESENTADO_COD");
                String estadoFirma = calcularEstadoFirma(codDocumentoPresentado,con);
                Timestamp tFechaAlta = rs.getTimestamp("PRESENTADO_FECHA_ALTA");
                
                doc.setCodDocumentoAdjunto(codDocumentoPresentado>0?codDocumentoPresentado:-1);
                doc.setTipoMimeDocumentoAdjunto(rs.getString("PRESENTADO_TIPO"));
                doc.setNombreDocumentoAdjunto(rs.getString("PRESENTADO_NOMBRE"));                
                doc.setFechaAltaDocumentoAdjunto((tFechaAlta!=null)?DateOperations.extraerFechaTimeStamp(tFechaAlta):"");
                doc.setEstadoFirmaDocumentoAdjunto(estadoFirma);
                Integer numFirmas =calcularNumeroFirmas(codDocumentoPresentado,con);
                doc.setNumeroFirmas((numFirmas!=null && numFirmas>0)?numFirmas:0);                                   
            }

        }catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException("ERROR DE BASE DE DATOS AL TRAER LOS DATOS DE LOS DOCUMENTOS", e);
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return doc;
    }

    
    
    
    /******************* NUEVO **********************************/
    
    /**
     * Recupera la estructura de los campos suplementarios para un determinado expedinete
     * @param gVO: Objeto de la clase GeneralValueObject que contiene lso datos del expediente para el que
     * se desea recuperar sus datos suplementarios y los valores de los mismos.
     * @param oad: Objeto de la clase AdaptadorSQLBD
     * @param con: Conexión a la BBDD
     * @param params: Parámetros de conexión a la BBDD
     * @return ArrayList<EstructuraCampo>
     * @throws TechnicalException 
     */
    public ArrayList<EstructuraCampo> cargaEstructuraDatosSuplementarios(DatosExpedienteVO dato,AdaptadorSQLBD oad,Connection con,String[] params) throws TechnicalException {

        Statement st = null, st2 = null, st3 = null;
        ResultSet rs = null, rs2 = null, rs3 = null;
        String sql = "", sql2 = "", sql3 = "";
        String from = "";
        String where = "";
        ArrayList<EstructuraCampo> lista = new  ArrayList<EstructuraCampo>();
        String campoActivo ="";
        String codigoCampo ="";
        String tempCodTipoDato ="";
        String tempCodCampo="";
        GeneralValueObject plazoActivo=new GeneralValueObject();
        
        m_Log.info("-------------------------FichaExpedienteDAO: cargaEstructuraDatosSuplementarios:" );
        try{
           
            st = con.createStatement();

            /*
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String codigoTramite = (String)gVO.getAtributo("codTramite");
            String numero = (String)gVO.getAtributo("numero");
            */
            
            int codMunicipio = dato.getCodOrganizacion();
            String codProcedimiento = dato.getCodProcedimiento();
            Integer codigoTramite = dato.getCodTramite();
            String numero = dato.getNumExpediente();
            String desdeJsp= dato.getDesdeJsp();
            String consultaCampos=dato.getConsultaCampos();
                        
            /*            
            String desdeJsp=(String)gVO.getAtributo("desdeJsp");
            String consultaCampos=(String)gVO.getAtributo("consultaCampos");
            */
            
            st3 = con.createStatement();
            sql3 = "SELECT TFE_COD,PLAZO_ACTIVADO FROM  E_TFE WHERE TFE_NUM='"
                    + numero + "'";
            rs3 = st3.executeQuery(sql3);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
            }
            while (rs3.next()) {
                codigoCampo = String.valueOf(rs3.getString("TFE_COD"));
                campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                m_Log.debug("campoActivo:::" + campoActivo);
                plazoActivo.setAtributo(codigoCampo, campoActivo);
            }
            
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs3);


            from = pca_cod + "," + pca_des + "," + pca_plt + "," + plt_url + "," + pca_tda + "," +
                   pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," + pca_rot + "," +
                   "CAMPO."+pca_activo + "," + pca_desplegable+",PCA_BLOQ, PLAZO_AVISO,PERIODO_PLAZO, PCA_GROUP,  PCA_POS_X, PCA_POS_Y, GRUPO.PCA_ORDER_GROUP";
            where = pca_mun + " = " + codMunicipio + " AND CAMPO." + pca_pro + " = '" + codProcedimiento + "' AND " +
                    "CAMPO."+pca_activo + " = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND PCA_OCULTO='NO'";
            String[] join = new String[8];
            join[0] = "E_PCA campo";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] =  pca_plt + "=e_plt." + plt_cod;
            join[4] = "LEFT";
            join[5] = "E_PCA_GROUP GRUPO";
            join[6] = "CAMPO.PCA_PRO=GRUPO.PCA_PRO  and PCA_GROUP=PCA_ID_GROUP";
            join[7] = "false";

            sql = oad.join(from,where,join);
            String parametros[] = {"18","18","17","17","9","9"};
            if (params[0] != null && (params[0].equalsIgnoreCase("oracle"))) {
                sql += " ORDER BY GRUPO.PCA_order_group NULLS FIRST,18, 17, 9";
            } else {
                sql += " ORDER BY GRUPO.PCA_order_group,18, 17, 9";
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            
            Hashtable<String,ArrayList<EstructuraCampo>> contenedor=new Hashtable<String,ArrayList<EstructuraCampo>>();
            
            while(rs.next()){
                EstructuraCampo eC = new EstructuraCampo();
                String codCampo = rs.getString(pca_cod);
                tempCodCampo = codCampo;
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(pca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(pca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(pca_tda);
                tempCodTipoDato = codTipoDato;                
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
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String bloqueado = rs.getString("PCA_BLOQ");                
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
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
                }//if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo))
                if(codTipoDato != null && !"".equalsIgnoreCase(codTipoDato) && ("1".equalsIgnoreCase(codTipoDato) 
                                || "3".equalsIgnoreCase(codTipoDato)
                                || "8".equalsIgnoreCase(codTipoDato) 
                                || "9".equalsIgnoreCase(codTipoDato))){
                    eC.setTamanoVista("50");
                }else{
                    eC.setTamanoVista("100");
                }//
                String posX = String.valueOf(rs.getInt("PCA_POS_X"));
                String posY = String.valueOf(rs.getInt("PCA_POS_Y"));
                //if((posX != null && !posX.equalsIgnoreCase("0")) && (posY != null && !posY.equalsIgnoreCase("0"))){
                if((posX != null) && (posY != null)){
                    eC.setPosicionar(true);
                }//if((posX != null && !posX.equalsIgnoreCase("0")) && (posY != null && !posY.equalsIgnoreCase("0")))
                eC.setPosX(posX);
                eC.setPosY(posY);
                String desplegable = "";
                try {
                     
                    if (("3").equals(tempCodTipoDato)){
                        m_Log.debug("campoActivo:::" + (String)plazoActivo.getAtributo(tempCodCampo));
                        eC.setCampoActivo((String)plazoActivo.getAtributo(tempCodCampo));
                    }
                    
                        
                    if ( !rs.getString(pca_desplegable).equals(null)) {
                        desplegable = rs.getString(pca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        

                        ArrayList<EstructuraCampo> valoresDesplegables=contenedor.get(desplegable);
                        if(valoresDesplegables!=null)
                        {
                            
                            if(m_Log.isDebugEnabled()) m_Log.debug("ya tenemos guardados los valores para el desplegable: "+ desplegable);
                          
                           for(int i=0;i<valoresDesplegables.size();i++) 
                           {
                               EstructuraCampo estructura=valoresDesplegables.get(i);
                               listaCod.addElement(estructura.getCodCampo());
                               listaDesc.addElement(estructura.getDescCampo());
                               listaEstado.addElement(estructura.getEstadoValorCampo());
                           }
                            
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                        }
                        else{ //No hay valores guardados para ese desplegable
                            st2 = con.createStatement();
                            sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", "+ des_val_estado +" FROM E_DES_VAL WHERE " +
                                   des_val_campo +"='"+ desplegable + "' ORDER BY " + des_val_desc  + " ASC"; 
                            rs2 = st2.executeQuery(sql2);
                            if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios sal: " + sql2);
                            ArrayList<EstructuraCampo> valoresDevueltos=new ArrayList<EstructuraCampo>();
                            while (rs2.next()) {
                                
                                String desvalcod="'"+rs2.getString(des_val_cod)+"'";
                                String desvaldesc="'"+rs2.getString(des_val_desc)+"'";
                                String desvalestado="'"+rs2.getString(des_val_estado)+"'";
                                
                                listaCod.addElement(desvalcod);
                                listaDesc.addElement(desvaldesc);
                                listaEstado.addElement(desvalestado);
                              
                                 EstructuraCampo estructura=new EstructuraCampo();
                                 estructura.setCodCampo(desvalcod);
                                 estructura.setDescCampo(desvaldesc);
                                 estructura.setEstadoValorCampo(desvalestado);
                                 valoresDevueltos.add(estructura);
                            }
                            contenedor.put(desplegable, valoresDevueltos);
                            
                            SigpGeneralOperations.closeStatement(st2);
                            SigpGeneralOperations.closeResultSet(rs2);
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                            eC.setListaEstadoValorDesplegable(listaEstado);
                        }
                    }
                } catch (NullPointerException e){
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("PLANTILLA : " + eC.getCodPlantilla());
                lista.add(eC);
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);

           
       
            from = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", oad.convertir("E_TCA.TCA_TRA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null), oad.convertir("E_TCA.TCA_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)}) + " AS " + tca_cod + ",E_TCA.TCA_DES,E_TCA.TCA_PLT," + plt_url + ",E_TCA.TCA_TDA,E_TCA.TCA_ROT" +
                    ",E_TCA.TCA_VIS,E_TCA.TCA_TRA,E_TCA.TCA_ACTIVO," + tml_valor + "," + cro_ocu + "," + tca_desplegable+",TCA_OCULTO,TCA_BLOQ,"+ oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{"E_TCA_GROUP.TCA_ORDER_GROUP", "-1"})+" as ordenGrupo";
            where = "e_tca.tca_mun = " + codMunicipio + " AND e_tca.tca_pro   = '" + codProcedimiento + "' AND e_tca.tca_activo='SI'";
            String[] join1 = new String[17];
            join1[0] = "e_tml";
            join1[1] = "INNER";
            join1[2] = "e_tca";
            join1[3] = "e_tca.tca_mun =e_tml.tml_mun AND " +
                    "e_tca.tca_pro=e_tml.tml_pro and " +
                    "e_tca.tca_tra= e_tml.tml_tra and " +
                    "e_tca.tca_vis = 'S' ";
            if (codigoTramite != null) join[3] += " AND E_TCA.TCA_TRA <> " + codigoTramite;

            join1[4] = "INNER";
            join1[5] = "E_CRO";

            //String numero = (String)gVO.getAtributo("numero");

            join1[6] = "e_tca.tca_mun= e_cro.CRO_MUN AND " +
                       "e_tca.TCA_PRO  = e_cro.CRO_PRO AND " +
                       "e_tca.tca_TRA = e_cro.CRO_TRA AND " +
                       "e_cro.cro_num = '" + numero +"'";
            join1[7] = "INNER";
            join1[8] = "e_plt";
            join1[9] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join1[10] = "INNER";
            join1[11] = "e_tra";
            join1[12] = "e_tca." + tca_mun + "=e_tra." + tra_mun + " AND " +
                    "e_tca." + tca_pro + "=e_tra." + tra_pro + " AND " +
                    "e_tca." + tca_tra + "=e_tra." + tra_cod + " AND " +
                    "e_tra." + tra_fba + " IS NULL";

          
            join1[13]= "LEFT";
            join1[14]= " E_TCA_GROUP";
            join1[15]= " E_TCA.TCA_PRO=E_TCA_GROUP.TCA_PRO  "
                    + "and E_TCA.TCA_TRA=E_TCA_GROUP.TCA_TRA and "
                    + "E_TCA.PCA_GROUP=E_TCA_GROUP.TCA_ID_GROUP";
              
            // Para quitar la descripción del tramite
            join1[16] = "false";
         
            sql = oad.join(from,where,join1);

            sql = sql + " ORDER BY CRO_FEI,E_TRA.TRA_COU,ordenGrupo,e_tca.TCA_POS_Y,e_tca.TCA_POS_X, e_tca.TCA_NOR, e_cro.CRO_OCU";
            // Fin consulta modificada
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
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
                eC.setTamano("");
                eC.setMascara("");
                eC.setObligatorio("0");
                eC.setNumeroOrden("0");
                String rotulo = rs.getString(tca_rot);
                eC.setRotulo(rotulo);
                String soloLectura = "true";
                eC.setSoloLectura(soloLectura);
                String codTramite = rs.getString(tca_tra);
                eC.setCodTramite(codTramite);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                String descripcionTramite = rs.getString(tml_valor);
                eC.setDescripcionTramite(descripcionTramite);
                String ocurrencia = rs.getString(cro_ocu);
                eC.setOcurrencia(ocurrencia);
                String bloqueado = rs.getString("TCA_BLOQ");               
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
                String desplegable = "";
                try {
                    if ( !rs.getString(tca_desplegable).equals(null)) {
                        desplegable = rs.getString(tca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        st2 = con.createStatement();
                        sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", " + des_val_estado +" FROM E_DES_VAL WHERE " +
                               des_val_campo +"='"+ desplegable + "'";
                        rs2 = st2.executeQuery(sql2);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                        while (rs2.next()) {
                            listaCod.addElement("'"+rs2.getString(des_val_cod)+"'");
                            listaDesc.addElement("'"+rs2.getString(des_val_desc)+"'");
                            listaEstado.addElement("'"+rs2.getString(des_val_estado)+"'");
                        }
                        SigpGeneralOperations.closeStatement(st2);
                        SigpGeneralOperations.closeResultSet(rs2);
                        eC.setListaCodDesplegable(listaCod);
                        eC.setListaDescDesplegable(listaDesc);
                        eC.setListaEstadoValorDesplegable(listaEstado);
                    }
                } catch (NullPointerException e){
                }
                eC.setCodAgrupacion("DVT");
                String visibleExpediente = rs.getString(tca_vis);
                if("S".equals(visibleExpediente)) {
                    String oculto = rs.getString("TCA_OCULTO");
                    if(("si".equalsIgnoreCase(desdeJsp))&& ("SI".equalsIgnoreCase(oculto)))
                    {
                      //Si viene de jsp y el campo esta oculto no se inserta el campo en la lista
                    }
                    else lista.add(eC);
                }
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);

        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        finally {
            
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeStatement(st2);
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeResultSet(rs2);
            SigpGeneralOperations.closeResultSet(rs3);
            return lista;
        }
    }
    
    
    
    
     public ArrayList<EstructuraCampoAgrupado> cargaEstructuraDatosSuplementariosExpediente(DatosExpedienteVO dato,String codigoAgrupacion,AdaptadorSQLBD oad,Connection con,String[] params) throws TechnicalException {

        Statement st = null, st2 = null, st3 = null;
        ResultSet rs = null, rs2 = null, rs3 = null;
        String sql = "", sql2 = "", sql3 = "";
        String from = "";
        String where = "";
        ArrayList<EstructuraCampoAgrupado> lista = new  ArrayList<EstructuraCampoAgrupado>();
        String campoActivo ="";
        String codigoCampo ="";
        String tempCodTipoDato ="";
        String tempCodCampo="";
        GeneralValueObject plazoActivo=new GeneralValueObject();                
        
        
        m_Log.info("-------------------------FichaExpedienteDAO: cargaEstructuraDatosSuplementariosExpediente:" );
        try{           
            st = con.createStatement();            
            int codMunicipio = dato.getCodOrganizacion();
            String codProcedimiento = dato.getCodProcedimiento();
            Integer codigoTramite = dato.getCodTramite();
            String numero = dato.getNumExpediente();
            String desdeJsp= dato.getDesdeJsp();
            String consultaCampos=dato.getConsultaCampos();
            
            st3 = con.createStatement();
            if (esExpedienteHistorico(numero, con))
                sql3 = "SELECT TFE_COD,PLAZO_ACTIVADO FROM  HIST_E_TFE WHERE TFE_NUM='"
                        + numero + "'";
            else
                sql3 = "SELECT TFE_COD,PLAZO_ACTIVADO FROM  E_TFE WHERE TFE_NUM='"
                        + numero + "'";
            rs3 = st3.executeQuery(sql3);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("cargarEstructuraDatosSuplementarios(PlazoActivo fecha) sal: " + sql3);
            }
            while (rs3.next()) {
                codigoCampo = String.valueOf(rs3.getString("TFE_COD"));
                campoActivo = String.valueOf(rs3.getInt("PLAZO_ACTIVADO"));
                m_Log.debug("campoActivo:::" + campoActivo);
                plazoActivo.setAtributo(codigoCampo, campoActivo);
            }
            
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs3);


            from = pca_cod + "," + pca_des + "," + pca_plt + "," + plt_url + "," + pca_tda + "," +
                   pca_tam + "," + pca_mas + "," + pca_obl + "," + pca_nor + "," + pca_rot + "," +
                   "CAMPO."+pca_activo + "," + pca_desplegable+",PCA_BLOQ, PLAZO_AVISO,PERIODO_PLAZO, PCA_GROUP,  PCA_POS_X, PCA_POS_Y, GRUPO.PCA_ORDER_GROUP";
            where = pca_mun + " = " + codMunicipio + " AND CAMPO." + pca_pro + " = '" + codProcedimiento + "' AND " +
                    "CAMPO."+pca_activo + " = 'SI'";
            if("si".equals(desdeJsp)) where=where+" AND PCA_OCULTO='NO'";
            String[] join = new String[8];
            join[0] = "E_PCA campo";
            join[1] = "INNER";
            join[2] = "e_plt";
            join[3] =  pca_plt + "=e_plt." + plt_cod;
            join[4] = "LEFT";
            join[5] = "E_PCA_GROUP GRUPO";
            join[6] = "CAMPO.PCA_PRO=GRUPO.PCA_PRO  and PCA_GROUP=PCA_ID_GROUP";
            join[7] = "false";

            sql = oad.join(from,where,join);
            
            if(codigoAgrupacion.equalsIgnoreCase("DEF")){
                sql = sql + " AND (PCA_GROUP='DEF' OR PCA_GROUP IS NULL)";                
            }else
                sql = sql + " AND PCA_GROUP='" + codigoAgrupacion + "'";
            
            String parametros[] = {"18","18","17","17","9","9"};
            if (params[0] != null && (params[0].equalsIgnoreCase("oracle"))) {
                sql += " ORDER BY GRUPO.PCA_order_group NULLS FIRST,18, 17, 9";
            } else {
                sql += " ORDER BY GRUPO.PCA_order_group,18, 17, 9";
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            
            Hashtable<String,ArrayList<EstructuraCampo>> contenedor=new Hashtable<String,ArrayList<EstructuraCampo>>();
            
            while(rs.next()){
                EstructuraCampoAgrupado eC = new EstructuraCampoAgrupado();
                String codCampo = rs.getString(pca_cod);
                tempCodCampo = codCampo;
                eC.setCodCampo(codCampo);
                String descCampo = rs.getString(pca_des);
                eC.setDescCampo(descCampo);
                String codPlantilla = rs.getString(pca_plt);
                eC.setCodPlantilla(codPlantilla);
                String urlPlantilla = rs.getString(plt_url);
                eC.setURLPlantilla(urlPlantilla);
                String codTipoDato = rs.getString(pca_tda);
                tempCodTipoDato = codTipoDato;                
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
                String soloLectura = "false";
                eC.setSoloLectura(soloLectura);
                String bloqueado = rs.getString("PCA_BLOQ");                
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
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
                }//if(agrupacionCampo != null && !"".equalsIgnoreCase(agrupacionCampo))
                if(codTipoDato != null && !"".equalsIgnoreCase(codTipoDato) && ("1".equalsIgnoreCase(codTipoDato) 
                                || "3".equalsIgnoreCase(codTipoDato)
                                || "8".equalsIgnoreCase(codTipoDato) 
                                || "9".equalsIgnoreCase(codTipoDato))){
                    eC.setTamanoVista("50");
                }else{
                    eC.setTamanoVista("100");
                }//
                
                /** original
                String posX = String.valueOf(rs.getInt("PCA_POS_X"));
                String posY = String.valueOf(rs.getInt("PCA_POS_Y"));
                */
                String posX = rs.getString("PCA_POS_X");
                String posY = rs.getString("PCA_POS_Y");
                
                if((posX != null) && (posY != null)){
                    eC.setPosicionar(true);
                }
                eC.setPosX(posX);
                eC.setPosY(posY);
                String desplegable = "";
                try {
                     
                    if (("3").equals(tempCodTipoDato)){
                        m_Log.debug("campoActivo:::" + (String)plazoActivo.getAtributo(tempCodCampo));
                        eC.setCampoActivo((String)plazoActivo.getAtributo(tempCodCampo));
                    }
                    
                        
                    if ( !rs.getString(pca_desplegable).equals(null)) {
                        desplegable = rs.getString(pca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        

                        ArrayList<EstructuraCampo> valoresDesplegables=contenedor.get(desplegable);
                        if(valoresDesplegables!=null)
                        {
                            
                            if(m_Log.isDebugEnabled()) m_Log.debug("ya tenemos guardados los valores para el desplegable: "+ desplegable);
                          
                           for(int i=0;i<valoresDesplegables.size();i++) 
                           {
                               EstructuraCampo estructura=valoresDesplegables.get(i);
                               listaCod.addElement(estructura.getCodCampo());
                               listaDesc.addElement(estructura.getDescCampo());
                               listaEstado.addElement(estructura.getEstadoValorCampo());
                           }
                            
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                            eC.setListaEstadoValorDesplegable(listaEstado);
                        }
                        else{ //No hay valores guardados para ese desplegable
                            st2 = con.createStatement();
                            sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", "+ des_val_estado + " FROM E_DES_VAL WHERE " +
                                   des_val_campo +"='"+ desplegable + "' ORDER BY " + des_val_desc  + " ASC"; 
                            rs2 = st2.executeQuery(sql2);
                            if(m_Log.isDebugEnabled()) m_Log.debug("cargarEstructuraDatosSuplementarios sal: " + sql2);
                            ArrayList<EstructuraCampo> valoresDevueltos=new ArrayList<EstructuraCampo>();
                            while (rs2.next()) {
                                
                                String desvalcod="'"+rs2.getString(des_val_cod)+"'";
                                String desvaldesc="'"+rs2.getString(des_val_desc)+"'";
                                String desvalestado="'"+rs2.getString(des_val_estado)+"'";
                                
                                listaCod.addElement(desvalcod);
                                listaDesc.addElement(desvaldesc);
                                listaEstado.addElement(desvalestado);
                              
                                 EstructuraCampo estructura=new EstructuraCampo();
                                 estructura.setCodCampo(desvalcod);
                                 estructura.setDescCampo(desvaldesc);
                                 estructura.setEstadoValorCampo(desvalestado);
                                 valoresDevueltos.add(estructura);
                            }
                            contenedor.put(desplegable, valoresDevueltos);
                            
                            SigpGeneralOperations.closeStatement(st2);
                            SigpGeneralOperations.closeResultSet(rs2);
                            eC.setListaCodDesplegable(listaCod);
                            eC.setListaDescDesplegable(listaDesc);
                            eC.setListaEstadoValorDesplegable(listaEstado);
                        }
                    }
                } catch (NullPointerException e){
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("PLANTILLA : " + eC.getCodPlantilla());
                
                
                // Se recupera el valor del campo
                /***************************************************************/                
                ValorCampoSuplementarioVO valor = null;
                if ("1".equals(eC.getCodTipoDato())) {                     
                    valor = DatosSuplementariosDAO.getInstance().getValoresNumericos(oad, con, dato,eC.getCodCampo());                    

                }else if ("2".equals(eC.getCodTipoDato())) {
                    valor = DatosSuplementariosDAO.getInstance().getValoresTexto(oad, con,dato,eC.getCodCampo());                     

                }else if ("3".equals(eC.getCodTipoDato())) {
                    //valor = DatosSuplementariosDAO.getInstance().getValoresFecha(oad, con, dato, mascara,eC.getCodCampo());
                    valor = DatosSuplementariosDAO.getInstance().getValoresFecha(oad,con,dato,mascara,eC);
                        
                } else if ("4".equals(eC.getCodTipoDato())) {
                    valor = DatosSuplementariosDAO.getInstance().getValoresTextoLargo(oad, con, dato,eC.getCodCampo());
                        
                } else if ("5".equals(codTipoDato)) {
                    valor = DatosSuplementariosDAO.getInstance().getValoresFichero(oad, con, dato,eC.getCodCampo());                    
                    
                } else if ("6".equals(codTipoDato)) {
                    valor = DatosSuplementariosDAO.getInstance().getValoresDesplegable(oad, con, dato,eC.getCodCampo());
                        
                } else if ("8".equals(codTipoDato)) {                    
                    valor = DatosSuplementariosDAO.getInstance().getValoresNumericosCal(oad, con, dato,eC.getCodCampo());                     

                } else if ("9".equals(codTipoDato)) {
                    valor = DatosSuplementariosDAO.getInstance().getValoresFechaCal(oad, con, dato, mascara,eC.getCodCampo());                        

                } else if ("10".equals(codTipoDato)) {                    
                    valor = DatosSuplementariosDAO.getInstance().getValoresDesplegableExt(oad, con, dato,eC.getCodCampo());                        
                }                
                
                eC.setValorCampo(valor);                                
                /****************************************************************/                
                lista.add(eC);
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);
            
        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        finally {
            
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeStatement(st2);
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeResultSet(rs2);
            SigpGeneralOperations.closeResultSet(rs3);
            return lista;
        }
    }
    
    
    
    
    
    /**
     * Recupera los valores de los diferentes campos suplementarios de un expediente
     * @param gVO: Objeto de la clase GeneralValueObject que contiene los datos del expediente
     * para los que se recuperan los valores de sus campos
     * @param eCs: 
     * @param oad
     * @param con
     * @return
     * @throws TechnicalException 
     *
    public ArrayList<CamposFormulario> cargaValoresDatosSuplementariosExpediente(DatosExpedienteVO dato,ArrayList<EstructuraCampo> estructura,AdaptadorSQLBD oad,Connection con) throws TechnicalException {        
        ArrayList<CamposFormulario> lista = new ArrayList<CamposFormulario>();          
        CamposFormulario cF1 = null;
        CamposFormulario cF2 = null;
        CamposFormulario cF3 = null;
        CamposFormulario cF4 = null;
        CamposFormulario cF5 = null;
        CamposFormulario cF6 = null;        
        CamposFormulario cF8 = null;
        CamposFormulario cF9 = null;
        CamposFormulario cF10 = null;

        try{ 

            for(int i=0;i<estructura.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) estructura.get(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                String codTramite = eC.getCodTramite();
                String ocurrencia = eC.getOcurrencia();                
                
                if ("1".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF1 == null) {
                            cF1 = DatosSuplementariosDAO.getInstance().getValoresNumericos(oad, con, dato);
                        }
                    } else {
                        cF1 = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad, con, dato,Integer.parseInt(codTramite),Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF1);

                } else if ("2".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF2 == null) {
                            cF2 = DatosSuplementariosDAO.getInstance().getValoresTexto(oad, con,dato);
                        }
                    } else {
                        cF2 = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad, con, dato,Integer.parseInt(codTramite), Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF2);

                } else if ("3".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF3 == null) {
                            cF3 = DatosSuplementariosDAO.getInstance().getValoresFecha(oad, con, dato, mascara);
                        }
                    } else {
                        cF3 = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad, con, dato, mascara, Integer.parseInt(codTramite), Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF3);

                } else if ("4".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF4 == null) {
                            cF4 = DatosSuplementariosDAO.getInstance().getValoresTextoLargo(oad, con, dato);
                        }
                    } else {
                        cF4 = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad, con, dato, Integer.parseInt(codTramite), Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF4);

                } else if ("5".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF5 == null) {
                            cF5 = DatosSuplementariosDAO.getInstance().getValoresFichero(oad, con, dato);
                        }
                    } else {
                        cF5 = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad, con, dato, Integer.parseInt(codTramite), Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF5);

                } else if ("6".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF6 == null) {
                            cF6 = DatosSuplementariosDAO.getInstance().getValoresDesplegable(oad, con, dato);
                        }

                    } else {
                        cF6 = DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite(oad, con, dato, Integer.parseInt(codTramite), Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF6);

                } else if ("8".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF8 == null) {
                            cF8 = DatosSuplementariosDAO.getInstance().getValoresNumericosCal(oad, con, dato);
                        }

                    } else {
                        cF8 = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad, con, dato, Integer.parseInt(codTramite), Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF8);

                } else if ("9".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF9 == null) {
                            cF9 = DatosSuplementariosDAO.getInstance().getValoresFechaCal(oad, con, dato, mascara);
                        }

                    } else {
                        cF9 = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad, con, dato, mascara, Integer.parseInt(codTramite), Integer.parseInt(ocurrencia));
                    }

                    lista.add(cF9);

                } else if ("10".equals(codTipoDato)) {

                    if (codTramite == null || "".equals(codTramite)) {
                        if (cF10 == null) {
                            cF10 = DatosSuplementariosDAO.getInstance().getValoresDesplegableExt(oad, con, dato);
                        }
                    } else {
                        cF10 = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad, con, dato,Integer.parseInt(codTramite),Integer.parseInt(ocurrencia));
                    }
                    lista.add(cF10);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lista; 
    }
    */

    
    
    

     /**
      * Recupera las agrupaciones existentes para organizar los campos suplementarios a nivel de expediente
      * @param codProcedimiento: String con el código del procedimiento
      * @param oad: AdaptadorSQLBD
      * @param con: Conexión a la BBDD
      * @return ArrayList<DefinicionAgrupacionCamposValueObject>
      * @throws TechnicalException si ocurre algún error
      */
     public ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> cargaEstructuraAgrupacionCampos(DatosExpedienteVO dato,String codProcedimiento, AdaptadorSQLBD oad, Connection con,String[] params) throws TechnicalException{
        
        if(m_Log.isDebugEnabled()) m_Log.debug("cargaEstructuraAgrupacionCampos() : BEGIN");
        ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> listaAgrupacionesCampo = new ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
            FichaExpedienteDAO fichaDAO = FichaExpedienteDAO.getInstance();
            //Creamos el grupo por defecto en la lista de agrupaciones
            DefinicionAgrupacionCamposAgrupadosValueObject dacdfVO = new DefinicionAgrupacionCamposAgrupadosValueObject();
            dacdfVO.setCodAgrupacion("DEF");
            dacdfVO.setDescAgrupacion("");
            dacdfVO.setOrdenAgrupacion(0);
            
            // Se recupera la estructura de los campos suplementarios asociados al a agrupación por defecto
            
            ArrayList<EstructuraCampoAgrupado> estructura = fichaDAO.cargaEstructuraDatosSuplementariosExpediente(dato,"DEF",oad, con, params);
            dacdfVO.setEstructura(estructura);
            listaAgrupacionesCampo.add(dacdfVO);
           
            
            sql = "SELECT PCA_ID_GROUP, PCA_DESC_GROUP, PCA_ORDER_GROUP FROM E_PCA_GROUP WHERE PCA_PRO =?"
                + " AND PCA_ACTIVE = 'SI' ORDER BY PCA_ORDER_GROUP ASC";
            
            ps = con.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.setString(1,codProcedimiento);
            rs = ps.executeQuery();
            
            while(rs.next()){
                DefinicionAgrupacionCamposAgrupadosValueObject dacVO = new DefinicionAgrupacionCamposAgrupadosValueObject();
                dacVO.setCodAgrupacion(rs.getString("PCA_ID_GROUP"));
                dacVO.setDescAgrupacion(rs.getString("PCA_DESC_GROUP"));
                dacVO.setOrdenAgrupacion(rs.getInt("PCA_ORDER_GROUP"));
                dacVO.setEstructura(fichaDAO.cargaEstructuraDatosSuplementariosExpediente(dato,dacVO.getCodAgrupacion(),oad, con, params));
                
                listaAgrupacionesCampo.add(dacVO);
            }               
            
            DefinicionAgrupacionCamposAgrupadosValueObject dcvtVO = new DefinicionAgrupacionCamposAgrupadosValueObject();
            dcvtVO.setCodAgrupacion("DVT");
            dcvtVO.setDescAgrupacion("");
            dcvtVO.setOrdenAgrupacion(listaAgrupacionesCampo.size()+1);             
            ArrayList<EstructuraCampoAgrupado> estructuraCamposTramite = fichaDAO.cargaEstructuraDatosSuplementariosTramiteVisiblesEnExpediente(dato,oad, con, params);
            dcvtVO.setEstructura(estructuraCamposTramite);
            listaAgrupacionesCampo.add(dcvtVO);      
            
            /* ORIGINAL
            if(cargarGrupoVisiblesTramite){
                DefinicionAgrupacionCamposAgrupadosValueObject dcvtVO = new DefinicionAgrupacionCamposAgrupadosValueObject();
                dcvtVO.setCodAgrupacion("DVT");
                dcvtVO.setDescAgrupacion("");
                dcvtVO.setOrdenAgrupacion(listaAgrupacionesCampo.size()+1);
                listaAgrupacionesCampo.add(dcvtVO);                
            } */
            
        }catch (SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }            
            return listaAgrupacionesCampo;
            
        } 
    }

     
     
    /************************************************/
     
    public ArrayList<EstructuraCampoAgrupado> cargaEstructuraDatosSuplementariosTramiteVisiblesEnExpediente(DatosExpedienteVO dato,AdaptadorSQLBD oad,Connection con,String[] params) throws TechnicalException {

        Statement st = null, st2 = null, st3 = null;
        ResultSet rs = null, rs2 = null, rs3 = null;
        String sql = "", sql2 = "", sql3 = "";
        String from = "";
        String where = "";
        ArrayList<EstructuraCampoAgrupado> lista = new  ArrayList<EstructuraCampoAgrupado>();
       
        
        m_Log.info("-------------------------FichaExpedienteDAO: cargaEstructuraDatosSuplementariosTramiteVisiblesEnExpediente:" );
        try{            
            st = con.createStatement();            
            int codMunicipio = dato.getCodOrganizacion();
            String codProcedimiento = dato.getCodProcedimiento();
            Integer codigoTramite = dato.getCodTramite();
            String numero = dato.getNumExpediente();
            String desdeJsp= dato.getDesdeJsp();
            String consultaCampos=dato.getConsultaCampos();
            String[] join = new String[8];
           
            from = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", oad.convertir("E_TCA.TCA_TRA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null), oad.convertir("E_TCA.TCA_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)}) + " AS " + tca_cod + ",E_TCA.TCA_DES,E_TCA.TCA_PLT," + plt_url + ",E_TCA.TCA_TDA,E_TCA.TCA_ROT" +
                    ",E_TCA.TCA_VIS,E_TCA.TCA_TRA,E_TCA.TCA_ACTIVO," + tml_valor + ",CRO_OCU,TCA_DESPLEGABLE,TCA_OCULTO,TCA_BLOQ,"+ oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{"E_TCA_GROUP.TCA_ORDER_GROUP", "-1"})+" as ordenGrupo";
            where = "e_tca.tca_mun = " + codMunicipio + " AND e_tca.tca_pro   = '" + codProcedimiento + "' AND e_tca.tca_activo='SI'";
            String[] join1 = new String[17];
            join1[0] = "e_tml";
            join1[1] = "INNER";
            join1[2] = "e_tca";
            join1[3] = "e_tca.tca_mun =e_tml.tml_mun AND " +
                    "e_tca.tca_pro=e_tml.tml_pro and " +
                    "e_tca.tca_tra= e_tml.tml_tra and " +
                    "e_tca.tca_vis = 'S' ";
            if (codigoTramite != null) join[3] += " AND E_TCA.TCA_TRA <> " + codigoTramite;

            join1[4] = "INNER";
            if (esExpedienteHistorico(numero, con))
                join1[5] = "HIST_E_CRO";
            else
                join1[5] = "E_CRO";

            //String numero = (String)gVO.getAtributo("numero");

            join1[6] = "e_tca.tca_mun= CRO_MUN AND " +
                       "e_tca.TCA_PRO  = CRO_PRO AND " +
                       "e_tca.tca_TRA = CRO_TRA AND " +
                       "cro_num = '" + numero +"'";
            join1[7] = "INNER";
            join1[8] = "e_plt";
            join1[9] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join1[10] = "INNER";
            join1[11] = "e_tra";
            join1[12] = "e_tca." + tca_mun + "=e_tra." + tra_mun + " AND " +
                    "e_tca." + tca_pro + "=e_tra." + tra_pro + " AND " +
                    "e_tca." + tca_tra + "=e_tra." + tra_cod + " AND " +
                    "e_tra." + tra_fba + " IS NULL";

          
            join1[13]= "LEFT";
            join1[14]= " E_TCA_GROUP";
            join1[15]= " E_TCA.TCA_PRO=E_TCA_GROUP.TCA_PRO  "
                    + "and E_TCA.TCA_TRA=E_TCA_GROUP.TCA_TRA and "
                    + "E_TCA.PCA_GROUP=E_TCA_GROUP.TCA_ID_GROUP";
              
            // Para quitar la descripción del tramite
            join1[16] = "false";
         
            sql = oad.join(from,where,join1);

            sql = sql + " ORDER BY CRO_FEI,E_TRA.TRA_COU,ordenGrupo,e_tca.TCA_POS_Y,e_tca.TCA_POS_X, e_tca.TCA_NOR, CRO_OCU";
            // Fin consulta modificada
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                EstructuraCampoAgrupado eC = new EstructuraCampoAgrupado();
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
                eC.setTamano("");
                eC.setMascara("");
                eC.setObligatorio("0");
                eC.setNumeroOrden("0");
                String rotulo = rs.getString(tca_rot);
                eC.setRotulo(rotulo);
                String soloLectura = "true";
                eC.setSoloLectura(soloLectura);
                String codTramite = rs.getString(tca_tra);
                eC.setCodTramite(codTramite);
                String activo = rs.getString(tca_activo);
                eC.setActivo(activo);
                String descripcionTramite = rs.getString(tml_valor);
                eC.setDescripcionTramite(descripcionTramite);
                String ocurrencia = rs.getString(cro_ocu);
                eC.setOcurrencia(ocurrencia);
                String bloqueado = rs.getString("TCA_BLOQ");               
                if("si".equals(consultaCampos))eC.setBloqueado("NO");
                else eC.setBloqueado(bloqueado);
                String desplegable = "";
                try {
                    if ( !rs.getString(tca_desplegable).equals(null)) {
                        desplegable = rs.getString(tca_desplegable);
                        eC.setDesplegable(desplegable);
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug("DESPLEGABLE : " + desplegable);
                    if (!desplegable.equals("")) {
                        Vector listaCod = new Vector();
                        Vector listaDesc = new Vector();
                        Vector listaEstado = new Vector();
                        st2 = con.createStatement();
                         sql2 = "SELECT " + des_val_cod + "," + des_val_desc + ", "+ des_val_estado + " FROM E_DES_VAL WHERE " +
                               des_val_campo +"='"+ desplegable + "'";
                        rs2 = st2.executeQuery(sql2);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                        while (rs2.next()) {
                            listaCod.addElement("'"+rs2.getString(des_val_cod)+"'");
                            listaDesc.addElement("'"+rs2.getString(des_val_desc)+"'");
                            listaEstado.addElement("'"+rs2.getString(des_val_estado)+"'");
                        }
                        SigpGeneralOperations.closeStatement(st2);
                        SigpGeneralOperations.closeResultSet(rs2);
                        eC.setListaCodDesplegable(listaCod);
                        eC.setListaDescDesplegable(listaDesc);
                        eC.setListaEstadoValorDesplegable(listaEstado);
                    }
                } catch (NullPointerException e){
                }
                eC.setCodAgrupacion("DVT");
                String visibleExpediente = rs.getString(tca_vis);
                if("S".equals(visibleExpediente)) {
                    String oculto = rs.getString("TCA_OCULTO");
                    if(("si".equalsIgnoreCase(desdeJsp))&& ("SI".equalsIgnoreCase(oculto)))
                    {
                      //Si viene de jsp y el campo esta oculto no se inserta el campo en la lista
                    }
                    else{                                                
                        String codCampoAuxiliar = eC.getCodCampo().replaceFirst("T" + eC.getCodTramite(),"").trim();                        
                        // Se recupera el valor del campo
                        /***************************************************************/                
                        ValorCampoSuplementarioVO valor = null;
                        if ("1".equals(eC.getCodTipoDato())) {                    
                            //valor = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());                    
                            valor = DatosSuplementariosDAO.getInstance().getValoresNumericosTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);                    

                        }else if ("2".equals(eC.getCodTipoDato())) {
                            //valor = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad, con,dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());                     
                            valor = DatosSuplementariosDAO.getInstance().getValoresTextoTramite(oad, con,dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);                     

                        }else if ("3".equals(eC.getCodTipoDato())) {
                            //valor = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad,con,dato,eC.getMascara(),Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());
                            valor = DatosSuplementariosDAO.getInstance().getValoresFechaTramite(oad,con,dato,eC.getMascara(),Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);

                        } else if ("4".equals(eC.getCodTipoDato())) {
                            //valor = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());
                            valor = DatosSuplementariosDAO.getInstance().getValoresTextoLargoTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);

                        } else if ("5".equals(codTipoDato)) {
                            //valor = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());                                                
                            valor = DatosSuplementariosDAO.getInstance().getValoresFicheroTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);                                                
                            
                        } else if ("6".equals(codTipoDato)) {
                            //valor = DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite(oad,con,dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());
                            valor = DatosSuplementariosDAO.getInstance().getValoresDesplegableTramite(oad,con,dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);

                        } else if ("8".equals(codTipoDato)) {                    
                            //valor = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());                     
                            valor = DatosSuplementariosDAO.getInstance().getValoresNumericosTramiteCal(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);                     

                        } else if ("9".equals(codTipoDato)) {
                            //valor = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad,con,dato,eC.getMascara(),Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());                        
                            valor = DatosSuplementariosDAO.getInstance().getValoresFechaTramiteCal(oad,con,dato,eC.getMascara(),Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);                        

                        } else if ("10".equals(codTipoDato)) {                    
                            //valor = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),eC.getCodCampo());                        
                            valor = DatosSuplementariosDAO.getInstance().getValoresDesplegableExtTramite(oad, con, dato,Integer.parseInt(eC.getCodTramite()),Integer.parseInt(eC.getOcurrencia()),codCampoAuxiliar);                        
                        }                

                        eC.setValorCampo(valor);                                                  
                        lista.add(eC);
                    }
                }
            }
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);

        }catch (BDException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch(SQLException e){
            e.printStackTrace();            
        }
        finally {
            
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeStatement(st2);
            SigpGeneralOperations.closeStatement(st3);
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeResultSet(rs2);
            SigpGeneralOperations.closeResultSet(rs3);
            return lista;
        }
    } 
     
    /**
     * Carga el listado de fichero aportados anteriormente por el ciudadano
     * (sólo el listado no se obtiene el cotenido del fichero)
     *
     * @return Un vector de ficheroVO
     */
    public ArrayList<FicheroVO> cargarListaFicherosAportadosAnterior(String tipo, String numExpediente, String[] params)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        ArrayList<FicheroVO> lista = new ArrayList<FicheroVO>();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            if (!esExpedienteHistorico(numExpediente, con)) {
                sql = "SELECT R_DOC_APORTADOS_EJE, R_DOC_APORTADOS_NUM, R_DOC_APORTADOS_NOM_DOC, R_DOC_APORTADOS_ORGANO,"
                        + " R_DOC_APORTADOS_TIP_DOC, R_DOC_APORTADOS_FEC_DOC FROM R_DOC_APORTADOS_ANTERIOR"
                        + " INNER JOIN E_EXR"
                        + " ON E_EXR.EXR_NRE=R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_NUM AND "
                        + " E_EXR.EXR_EJR=R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_EJE AND "
                        + " E_EXR.EXR_TIP=R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_TIP "
                        + " WHERE EXR_NUM='" + numExpediente + "' AND EXR_TIP = '" + tipo + "' ORDER BY R_DOC_APORTADOS_FEC_DOC DESC, R_DOC_APORTADOS_NOM_DOC ASC";
            }
            else {
                sql = "SELECT R_DOC_APORTADOS_EJE, R_DOC_APORTADOS_NUM, R_DOC_APORTADOS_NOM_DOC, R_DOC_APORTADOS_ORGANO,"
                        + " R_DOC_APORTADOS_TIP_DOC, R_DOC_APORTADOS_FEC_DOC FROM R_DOC_APORTADOS_ANTERIOR"
                        + " INNER JOIN HIST_E_EXR"
                        + " ON HIST_E_EXR.EXR_NRE=R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_NUM AND "
                        + " HIST_E_EXR.EXR_EJR=R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_EJE AND "
                        + " HIST_E_EXR.EXR_TIP=R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_TIP "
                        + " WHERE EXR_NUM='" + numExpediente + "' AND EXR_TIP = '" + tipo + "' ORDER BY R_DOC_APORTADOS_FEC_DOC DESC, R_DOC_APORTADOS_NOM_DOC ASC";
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            while (rs.next()) {
                FicheroVO fichero = new FicheroVO();
                fichero.setEjercicio(rs.getString("R_DOC_APORTADOS_EJE"));
                fichero.setNumero(rs.getString("R_DOC_APORTADOS_NUM"));
                fichero.setNombre(rs.getString("R_DOC_APORTADOS_NOM_DOC"));
                fichero.setTipoContenido(rs.getString("R_DOC_APORTADOS_TIP_DOC"));
                fichero.setOrgano(rs.getString("R_DOC_APORTADOS_ORGANO"));
                Timestamp fecha=rs.getTimestamp("R_DOC_APORTADOS_FEC_DOC");
                fichero.setFecha(DateOperations.extraerFechaTimeStamp(fecha));
                lista.add(fichero);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return lista;
    }
    
    /**
     * Comprueba si es necesario posicionar los campos suplementarios de expediente, en la ficha del expediente,
     * según las posiciones (X,Y) definidas para dichos campos en la definición del procedimiento.
     * Si hay algún campo que tenga las posiciones (X,Y) a null, entonces no se posiciona, y en ese caso devolverá
     * false. Si todos los campos del procedimiento tiene posiciones X e Y definidas, entonces devolverá true
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return booleann 
     * @throws TechnicalException si ha ocurrido algún error
     */
    public Boolean posicionarVistaCamposSuplementariosExpediente(String codProcedimiento,Connection con) throws TechnicalException{        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        Boolean exito = new Boolean(true);
        
        try{           
            sql = "SELECT COUNT(*) AS NUM FROM E_PCA WHERE PCA_PRO =? AND PCA_ACTIVO = ? AND PCA_OCULTO <> ? AND PCA_POS_X IS NULL";
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,codProcedimiento);
            ps.setString(i++,ConstantesDatos.SI);
            ps.setString(i++,ConstantesDatos.SI);            
            rs = ps.executeQuery();
            
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }
            
            if(num>0) exito = new Boolean(false);
            
        }catch(SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeResultSet(rs);                        
            return exito;
        }
    }

    
    
    /***************************/
     /**
     * Recupera Carga el listado de ficheros existentes en los trámites del expediente indicado. Carga los datos necesarios 
     * para la posterior recuperacion de su contenido (no carga el array de bytes que forma el contenido del fichero). 
     * Se tiene en cuenta si el usuario tiene permisos para los trámites.
     *
     * @return Un vector de ficheroVO.
     */
    public Vector<FicheroVO> cargarFicherosExpediente(String numExpediente, int codMunicipio, boolean expHistorico, String[] params) throws TechnicalException {
      
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        Vector<FicheroVO> lista = new Vector<FicheroVO>();

        try{            
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            
            String[] datos = numExpediente.split("/");
            int ejercicio = Integer.parseInt(datos[0]);
            
            if (esExpedienteHistorico(numExpediente, con)) {
                sql = "SELECT TFI_COD AS CODIGO,TFI_NOMFICH AS NOMBREFICHERO " + 
                      "FROM HIST_E_TFI WHERE TFI_NUM=? AND TFI_MUN=? AND TFI_EJE=? ORDER BY TFI_NOMFICH ASC";
            }            
            else {
                sql = "SELECT TFI_COD  AS CODIGO,TFI_NOMFICH AS NOMBREFICHERO " + 
                      "FROM E_TFI WHERE TFI_NUM=? AND TFI_MUN=? AND TFI_EJE=? ORDER BY TFI_NOMFICH ASC";                
            }
             
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setString(i++,numExpediente);
            ps.setInt(i++,codMunicipio);
            ps.setInt(i++,ejercicio);
            rs = ps.executeQuery();
            
            while(rs.next()){
                FicheroVO fichero = new FicheroVO();                
                fichero.setCodigo(rs.getString("CODIGO"));
                fichero.setNombre(rs.getString("NOMBREFICHERO"));
                lista.add(fichero);
            }

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);                                
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return lista;   
    }
    
    
    
    /**
     * Carga el listado de ficheros aportados a registro del tipo indicado para los asientos relacionados con 
     * el expediente indicado. Carga los datos necesarios para la posterior recuperacion de su contenido (no 
     * carga el array de bytes que forma el contenido del fichero). El estado de la anotacion debe ser 1
     * (aceptada).
     *
     * @return Un vector de ficheroVO.
     */
    public Vector<FicheroVO> cargarFicherosRegistro(String tipoRegistro, String numExpediente, String codMunicipio,boolean expedienteHistorico, String[] params) 
        throws TechnicalException {
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector<FicheroVO> lista = new Vector<FicheroVO>();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            if(!esExpedienteHistorico(numExpediente, con)) {
                sql = "SELECT " + sql_docsDepto + ", " + sql_docsUnid + ", " + sql_docsEjerc + ", " 
                                + sql_docsNum + ", " + sql_docsNombreDoc + ", " + sql_docsTipoDoc + ", "
                                + sql_res_fecAnot +
                      " FROM R_RED INNER JOIN E_EXR ON (" + sql_docsDepto + "=" + sql_exr_dep + " AND "
                                                          + sql_docsUnid +  "=" + sql_exr_uor + " AND "
                                                          + sql_docsEjerc + "=" + sql_exr_ejr + " AND "
                                                          + sql_docsNum +   "=" + sql_exr_nre + " AND "
                                                          + sql_docsTipo +  "=" + sql_exr_tip + ")" +
                      " INNER JOIN R_RES ON (" + sql_docsDepto + "=" + sql_res_codDpto + " AND "
                                               + sql_docsUnid +  "=" + sql_res_codUnid + " AND "
                                               + sql_docsEjerc + "=" + sql_res_ejer + " AND "
                                               + sql_docsNum +   "=" + sql_res_num + " AND "
                                               + sql_docsTipo +  "=" + sql_res_tipoReg + ")" +
                      " WHERE " + sql_exr_num + "= '" + numExpediente + "'" +
                      " AND " + sql_exr_mun + "=" + codMunicipio +
                      " AND " + sql_exr_tip + "='" + tipoRegistro + "'" +                      
                      " AND " + sql_res_estado + "=1" +
                      " ORDER BY " + sql_exr_ejr + ", " + sql_exr_nre;            
            } else { 
                sql = "SELECT " + sql_docsDepto + ", " + sql_docsUnid + ", " + sql_docsEjerc + ", " 
                                + sql_docsNum + ", " + sql_docsNombreDoc + ", " + sql_docsTipoDoc + ", "
                                + sql_res_fecAnot +
                      " FROM R_RED INNER JOIN HIST_E_EXR ON (" + sql_docsDepto + "=" + sql_exr_dep + " AND "
                                                          + sql_docsUnid +  "=" + sql_exr_uor + " AND "
                                                          + sql_docsEjerc + "=" + sql_exr_ejr + " AND "
                                                          + sql_docsNum +   "=" + sql_exr_nre + " AND "
                                                          + sql_docsTipo +  "=" + sql_exr_tip + ")" +
                      " INNER JOIN R_RES ON (" + sql_docsDepto + "=" + sql_res_codDpto + " AND "
                                               + sql_docsUnid +  "=" + sql_res_codUnid + " AND "
                                               + sql_docsEjerc + "=" + sql_res_ejer + " AND "
                                               + sql_docsNum +   "=" + sql_res_num + " AND "
                                               + sql_docsTipo +  "=" + sql_res_tipoReg + ")" +
                      " WHERE " + sql_exr_num + "= '" + numExpediente + "'" +
                      " AND " + sql_exr_mun + "=" + codMunicipio +
                      " AND " + sql_exr_tip + "='" + tipoRegistro + "'" +                      
                      " AND " + sql_res_estado + "=1" +
                      " ORDER BY " + sql_exr_ejr + ", " + sql_exr_nre;                            
            }
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                FicheroVO fichero = new FicheroVO();
                fichero.setTipo(tipoRegistro);
                fichero.setDep(rs.getString(sql_docsDepto));
                fichero.setUor(rs.getString(sql_docsUnid));
                fichero.setEjercicio(rs.getString(sql_docsEjerc));
                fichero.setNumero(rs.getString(sql_docsNum));
                fichero.setNombre(rs.getString(sql_docsNombreDoc));
                fichero.setTipoContenido(rs.getString(sql_docsTipoDoc));
                
               java.sql.Timestamp fAnotacion = rs.getTimestamp(sql_res_fecAnot);

                if(fAnotacion!=null){                    
                    Calendar cFAnotacion = DateOperations.toCalendar(fAnotacion);
                    fichero.setFechaAsiento(DateOperations.toString(cFAnotacion,"dd/MM/yyyy"));

                }
                
                lista.add(fichero);
            }

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);                      
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return lista;
    }
    
    
   /**
     * Comprueba si un expediente esta un expediente activo, o bien, un expediente que está en el histórico
     * @param ejercicio: Ejercicio del expediente
     * @param numExpediente: Número del expedinete
     * @param con: Conexión a la BBDD
     * @return int que puede tomar los siguientes valores:
     *          0 --> Expediente activo
     *          1 --> Expediente historico
     *          2 --> Expediente no existe
     */
    public int estaExpedienteHistorico(int ejercicio,String numExpediente,Connection con) {
         int salida =2;
         PreparedStatement ps = null;
         ResultSet rs = null;
         String origen = null;
         int numero = 0;
         
         try{
             String sql = "SELECT COUNT(*) AS NUM,'ACTIVO' ORIGEN FROM E_EXP WHERE EXP_EJE=? AND EXP_NUM=? " + 
                          "UNION " + 
                          "SELECT COUNT(*) AS NUM,'HISTORICO' ORIGEN FROM HIST_E_EXP WHERE EXP_EJE=? AND EXP_NUM=?";
             m_Log.debug(sql);
             ps = con.prepareStatement(sql);
             int i=1;
             ps.setInt(i++,ejercicio);
             ps.setString(i++,numExpediente);
             ps.setInt(i++,ejercicio);
             ps.setString(i++,numExpediente);
             
             rs = ps.executeQuery();             
             while(rs.next()) { 
                 numero = rs.getInt("NUM");
                 origen = rs.getString("ORIGEN");                 
                 
             }// while
             
             if(origen==null) { 
                 salida = 2;
             }else
             if(origen!=null && origen.equals(ConstantesDatos.ACTIVO)){
                 salida = 0;                 
             } else
             if(origen!=null && origen.equals(ConstantesDatos.HISTORICO)){
                 salida = 1;
             }
             
         }catch(Exception e){             
             e.printStackTrace();
             
         } finally{
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
     * Recupera la lista de documentos de un trámite
     * No se obtiene el contenido binario del documento que reside en el campo E_CRD.CRD_FIL.
     * 
     * @param tEVO TramitacionExpedientesValueObject
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocumentoTramitacionVO> getListaDocumentosTramite(TramitacionExpedientesValueObject tEVO, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            
            String query = "SELECT CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU,"
                    + " CRD_NUD, CRD_FAL, CRD_FMO, CRD_USC, CRD_USM, CRD_DES, CRD_DOT,"
                    + " CRD_FIR_EST, CRD_EXP_FD, CRD_DOC_FD, CRD_FIR_FD, CRD_FINF"
                    + " FROM E_CRD "
                    + " WHERE CRD_MUN = "+tEVO.getCodMunicipio()
                    + " AND CRD_EJE = "+tEVO.getEjercicio()
                    + " AND  CRD_PRO ='"+tEVO.getCodProcedimiento()+"'"
                    + " AND CRD_NUM ='"+tEVO.getNumeroExpediente()+"'"
                    + " AND CRD_TRA ="+tEVO.getCodTramite()
                    + " AND CRD_OCU ="+tEVO.getOcurrenciaTramite();
          
            m_Log.debug(" (TRAZA DE CONTROL) SQL: " + query); 
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<DocumentoTramitacionVO> retList = new ArrayList<DocumentoTramitacionVO>();
            DocumentoTramitacionVO d = null;
            
            
            while(rs.next()){
                d = new DocumentoTramitacionVO();
                d.setCodDocumento(rs.getInt("CRD_DOT"));
                d.setCodMunicipio(rs.getInt("CRD_MUN"));
                d.setCodProcedimiento(rs.getString("CRD_PRO"));
                d.setCodTramite(rs.getInt("CRD_TRA"));
                d.setCodUsuarioAlta(rs.getInt("CRD_USC"));
                
                d.setDocFd(rs.getString("CRD_DOC_FD"));
                d.setEjercicio(rs.getInt("CRD_EJE"));
                d.setEstadoFirma(rs.getString("CRD_FIR_EST"));
                d.setFd(rs.getString("CRD_EXP_FD"));
                
                d.setNombreDocumento(rs.getString("CRD_DES"));
                d.setNumDocumento(rs.getInt("CRD_NUD"));
                d.setNumExpediente(rs.getString("CRD_NUM"));
                d.setOcurrenciaTramite(rs.getInt("CRD_OCU"));
               
                retList.add(d);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            m_Log.error("Se ha producido un error en FichaExpedienteDAO.getListaDocumentosTramite: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }

    public boolean expProcNumeracionAnhoAnotacion(int codOrg, String codProc, Connection con) throws SQLException{
        m_Log.info("FichaExpedienteDAO.expProcNumeracionAnhoAnotacion()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        boolean numeracionAnotacion = false;
        
        try {
            query = "SELECT PRO_EXPNUMANOT FROM E_PRO WHERE PRO_MUN=? AND PRO_COD=?";
            m_Log.debug("query = " + query);
            m_Log.debug("Parámetros pasados a la query = " + codOrg + "-" + codProc);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd, codProc);
            rs = ps.executeQuery();
            
            if(rs.next()){
                numeracionAnotacion = rs.getBoolean("PRO_EXPNUMANOT");
            }
        } catch (SQLException sqlex){
            m_Log.error("Ha ocurrido un error al recuperar la opción de numeración de expedientes desde registro para el procedimiento seleccionado.");
            sqlex.printStackTrace();
            throw sqlex;
        } finally {
            try  {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch (SQLException ex){
                m_Log.error("Ha ocurrido un error al liberar recursos de base de datos.");
            }
        } 
        
        m_Log.info("FichaExpedienteDAO.expProcNumeracionAnhoAnotacion()::END");
        return numeracionAnotacion;
    }
    
    public FicheroVO cargarTipoDocumentalFicherosRegistro(FicheroVO fichero,  Connection con) throws TechnicalException{
        m_Log.debug("cargarTipoDocumentalFicherosRegistro(): BEGIN");
        
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
         
         try{
             sql = "SELECT DISTINCT TIPDOC_LANBIDE_ES, TIPDOC_LANBIDE_EU FROM MELANBIDE68_TIPDOC_LANBIDE tip"
                     + " LEFT JOIN R_RED"
                     + " ON tip.TIPDOC_ID = R_RED.RED_TIPODOC_ID"
                     + " WHERE R_RED.RED_DEP = ? AND R_RED.RED_UOR = ? AND R_RED.RED_EJE = ? AND R_RED.RED_NUM = ?"
                     + " AND R_RED.RED_TIP = ? AND R_RED.RED_NOM_DOC = ?";     
             if(m_Log.isDebugEnabled()) {
                 m_Log.debug("Query = " + sql);
                 m_Log.debug("Parámetros de la query = " + fichero.getDep() + " - " + fichero.getUor() + " - " + fichero.getEjercicio() + 
                         " - " + fichero.getNumero() + " - " + fichero.getTipo() + " - " + fichero.getNombre());
             }
             
             st = con.prepareStatement(sql);
             int contbd = 1;
             st.setInt(contbd++, Integer.parseInt(fichero.getDep()));
             st.setInt(contbd++, Integer.parseInt(fichero.getUor()));
             st.setInt(contbd++, Integer.parseInt(fichero.getEjercicio()));
             st.setLong(contbd++, Long.parseLong(fichero.getNumero()));
             st.setString(contbd++, fichero.getTipo());
             st.setString(contbd++, fichero.getNombre());
             rs = st.executeQuery();
             while(rs.next()){
                 String tipDocumental = rs.getString("TIPDOC_LANBIDE_ES");
                 if(tipDocumental!=null){
                    fichero.setTipoDocumental(rs.getString("TIPDOC_LANBIDE_ES"));
                 } else {
                     fichero.setTipoDocumental("");
                 }
             }
         }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);                      
            SigpGeneralOperations.closeStatement(st);
        }
         
        return fichero;
    }
    
    
    /**
     * Metodo creado para eliminar todos los documentos de datos suplementarios del tramite
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numero
     * @param codTramiteRetroceder
     * @param ocuTramRetro
     * @param con
     * @throws TechnicalException 
     */
    private boolean eliminarTodosDocumentosAsociadoTramite (String codMunicipio, String codProcedimiento, String ejercicio, String numero,
            String codTramiteRetroceder, String ocuTramRetro, Connection con) throws TechnicalException {
        
        m_Log.debug("eliminarTodosDocumentosAsociadoTramite(): BEGIN");

        boolean exito = false;
        //Obtiene la implementacion del plugin correspondiente               
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);               
        int tipoDocumento = -1;

        if(!almacen.isPluginGestor()){
            //Plugin BBDDD
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        }else{
            //Plugin Gestor Alfresco
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
        }

        try{
            m_Log.debug("eliminarTodosDocumentosAsociadoTramite(): Se obtienen los datos para obtener el documento");
            Hashtable<String,Object> datos = obtenerDatosDoc(codMunicipio, codProcedimiento, ejercicio, numero, codTramiteRetroceder, ocuTramRetro, almacen, con);
            
            if (datos == null) {
                m_Log.debug("eliminarTodosDocumentosAsociadoTramite(): END ERROR datos se encuentra vacion");
                return exito;
            }
            
            Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
            if(doc!=null){
                m_Log.debug("doc es distinto a nulo y se eliminaran todos los documentos datos suplementarios");
                exito = almacen.eliminarTodosDocumentosTramite(doc, con);
            }                

        }catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            m_Log.error("eliminarTodosDocumentosAsociadoTramite(): END ERROR AlmacenDocumentoTramitacionException");
            throw new TechnicalException(e.getMessage());
        }
        catch(Exception e){ 
            e.printStackTrace();
            m_Log.error("eliminarTodosDocumentosAsociadoTramite(): END ERROR Exception");
            throw new TechnicalException(e.getMessage());            
        }
        
         m_Log.debug("eliminarTodosDocumentosAsociadoTramite(): END SIN ERRORES exito = "+exito);
         return exito;
    }
    
    
    /**
     * Metodo que elimina todos los documentos de los datos suplementarios de un tramite
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numero
     * @param codTramiteRetroceder
     * @param ocuTramRetro
     * @param con
     * @throws TechnicalException 
     */
    private boolean eliminarTodosDocumentosDatosSuplementariosTramite (String codMunicipio, String codProcedimiento, String ejercicio, String numero,
            String codTramiteRetroceder, String ocuTramRetro, Connection con) throws TechnicalException {
        m_Log.debug("eliminarTodosDocumentosDatosSuplementariosTramite(): BEGIN");

        //Obtiene la implementacion del plugin correspondiente               
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);               
        int tipoDocumento = -1;
        boolean exito = false;

        if(!almacen.isPluginGestor()){
            //Plugin BBDDD
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        }else{
            //Plugin Gestor Alfresco
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
        }

        try{
            m_Log.debug("eliminarTodosDocumentosDatosSuplementariosTramite(): Se obtienen los datos para obtener el documento");
            Hashtable<String,Object> datos = obtenerDatosDoc(codMunicipio, codProcedimiento, ejercicio, numero, codTramiteRetroceder, ocuTramRetro, almacen, con);
            
            if (datos == null) {
                m_Log.debug("eliminarTodosDocumentosDatosSuplementariosTramite(): END ERROR datos se encuentra vacion");
                return exito;
            }
            
            Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
            if(doc!=null){
                m_Log.debug("doc es distinto a nulo y se eliminaran todos los documentos datos suplementarios");
                exito = almacen.eliminarTodosDocumentosDatosSuplementariosTramite(doc, con);
            }                

        }catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            m_Log.error("eliminarTodosDocumentosDatosSuplementariosTramite(): END ERROR AlmacenDocumentoTramitacionException");
            throw new TechnicalException(e.getMessage());
        }
        catch(Exception e){ 
            e.printStackTrace();
            m_Log.error("eliminarTodosDocumentosDatosSuplementariosTramite(): END ERROR Exception");
            throw new TechnicalException(e.getMessage());            
        }
        m_Log.debug("eliminarTodosDocumentosDatosSuplementariosTramite(): END SIN ERRORES exito = "+exito);
        return exito;
    }
    
    
    /**
     * Metodo que devuelve un hash con los datos para obtener un documento
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numero
     * @param codTramiteRetroceder
     * @param ocuTramRetro
     * @param almacen
     * @param con
     * @return
     * @throws TechnicalException 
     */
    private Hashtable<String,Object> obtenerDatosDoc(String codMunicipio, String codProcedimiento, String ejercicio, String numero,
            String codTramiteRetroceder, String ocuTramRetro, AlmacenDocumento almacen, Connection con) throws TechnicalException {
        
        m_Log.debug(" obtenerDatosDoc: BEGIN ");
        
        m_Log.debug(" datos que recojo del formulario");
        m_Log.debug(" [codMunicipio]"+codMunicipio);
        m_Log.debug(" [codProcedimiento]"+codProcedimiento);
        m_Log.debug(" [ejercicio]"+ejercicio);
        m_Log.debug(" [numero]"+numero);
        m_Log.debug("[codTramiteRetroceder]"+codTramiteRetroceder);
        m_Log.debug(" [ocurrenciaTramiteRetroceder]"+ocuTramRetro);
        
        String descripcionOrganizacion = null;
        Hashtable<String,Object> datos = null;
        
        try{
            datos = new Hashtable<String,Object>();                            
            datos.put("codMunicipio",codMunicipio);
            datos.put("codOrganizacion",codMunicipio);                   
            datos.put("ejercicio",ejercicio);
            datos.put("numeroExpediente",numero);                   
            datos.put("codTramite",codTramiteRetroceder);
            datos.put("ocurrenciaTramite",ocuTramRetro);
            datos.put("codProcedimiento",codProcedimiento);


            if(almacen.isPluginGestor()){
                //  Si se trata de un plugin de un gestor documental, se pasa la información
                // extra necesaria              
                m_Log.debug("Se trata de un plugin de un gestor documental");
                ResourceBundle config = ResourceBundle.getBundle("documentos");                  
                String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codMunicipio),con);
                String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);

                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                listaCarpetas.add(numero.replaceAll("/","-"));
                datos.put("listaCarpetas",listaCarpetas);
            }

        }catch(Exception e){ 
            e.printStackTrace();
            m_Log.error("obtenerDatosDoc(): END ERROR Exception");
            throw new TechnicalException(e.getMessage());            
        }
        
        m_Log.debug(" obtenerDatosDoc: END SIN ERRORES ");
        return datos;
    }
    
    
    /**
     * Metodo donde se eliminan todos los datos suplementarios de un tramite
     * 
     * E_TXTT: campo suplementario de tipo texto
     * E_TDEXT: campo suplementario de tipo desplegable externo
     * E_TTLT: campo suplementario de tipo texto largo
     * E_TNUT: campo suplementario de tipo numérico
     * E_TNUCT: campo suplementario de tipo numérico compuesto
     * E_TFIT: campo suplementario de tipo fichero. SOLO ESTA GUARDA DOCUMENTOS
     * E_TFET: campo suplementario de tipo fecha
     * E_TFECT: campo suplementario de tipo fecha compuesta
     * E_TDET: campo suplementario de tipo desplegable
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numero
     * @param codTramiteRetroceder
     * @param ocuTramRetro
     * @param con
     * @throws SQLException
     * @throws TechnicalException 
     */
    private void elimarRegistrosCamposSuplementarios (String codMunicipio, String codProcedimiento, String ejercicio, String numero,
            String codTramiteRetroceder, String ocuTramRetro, Connection con) throws SQLException, TechnicalException {
        
            Statement st = null;
        try {
            StringBuilder sql = new StringBuilder();
            //Borramos de E_TXTT
            sql.append ("DELETE FROM e_txtt");
            sql.append (" WHERE ");
            sql.append (txtt_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(txtt_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(txtt_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(txtt_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(txtt_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(txtt_ocu).append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en TXTT :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);
            
            
            //Borramos de E_TDEXT
            sql = new StringBuilder();
            sql.append ("DELETE FROM E_TDEXT");
            sql.append (" WHERE ");
            sql.append ("TDEXT_MUN").append(" = ").append(codMunicipio);
            sql.append (" AND ").append("TDEXT_PRO").append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append("TDEXT_EJE").append(" = ").append(ejercicio);
            sql.append (" AND ").append("TDEXT_NUM").append(" = '").append(numero).append("'");
            sql.append (" AND ").append("TDEXT_TRA").append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append("TDEXT_OCU").append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en TDEXT :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st); 
            
            //Borramos de E_TTLT
            sql = new StringBuilder();
            sql.append ("DELETE FROM e_ttlt");
            sql.append (" WHERE ");
            sql.append (ttlt_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(ttlt_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(ttlt_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(ttlt_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(ttlt_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(ttlt_ocu).append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en TTLT :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);
            
            //Borramos de E_TNUT
            sql = new StringBuilder();
            sql.append ("DELETE FROM e_tnut");
            sql.append (" WHERE ");
            sql.append (tnut_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(tnut_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(tnut_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(tnut_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(tnut_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(tnut_ocu).append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en E_TNUT :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);
            
            //Borramos de E_TNUCT
            sql = new StringBuilder();
            sql.append ("DELETE FROM e_tnuct");
            sql.append (" WHERE ");
            sql.append (tnuct_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(tnuct_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(tnuct_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(tnuct_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(tnuct_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(tnuct_ocu).append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en E_TNUCT :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);
                
            //Borramos de E_TFIT
            eliminarTodosDocumentosDatosSuplementariosTramite(codMunicipio, codProcedimiento, ejercicio, numero, codTramiteRetroceder, ocuTramRetro, con);
               
            //Borramos de E_TFET
            sql = new StringBuilder();
            sql.append ("DELETE FROM e_tfet");
            sql.append (" WHERE ");
            sql.append (tfet_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(tfet_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(tfet_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(tfet_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(tfet_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(tfet_ocu).append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en E_TFET :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);
            
            //Borramos de E_TFECT
            sql = new StringBuilder();
            sql.append ("DELETE FROM e_tfect");
            sql.append (" WHERE ");
            sql.append (tfect_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(tfect_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(tfect_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(tfect_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(tfect_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(tfect_ocu).append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en E_TFECT :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);
            
            //Borramos de E_TDET
            sql = new StringBuilder();
            sql.append ("DELETE FROM e_tdet");
            sql.append (" WHERE ");
            sql.append (tdet_mun).append(" = ").append(codMunicipio);
            sql.append (" AND ").append(tdet_pro).append(" = '").append(codProcedimiento).append("'");
            sql.append (" AND ").append(tdet_eje).append(" = ").append(ejercicio);
            sql.append (" AND ").append(tdet_num).append(" = '").append(numero).append("'");
            sql.append (" AND ").append(tdet_tra).append(" = ").append(codTramiteRetroceder);
            sql.append (" AND ").append(tdet_ocu).append(" = ").append(ocuTramRetro);

            if(m_Log.isDebugEnabled()) m_Log.debug("-->Borramos en E_TDET :" + sql.toString());
            st = con.createStatement();
            st.executeUpdate(sql.toString());
            SigpGeneralOperations.closeStatement(st);
            
            
        } catch (SQLException ex) {
            throw ex;
        } catch (TechnicalException ex) {
            throw ex;
        } finally {
           SigpGeneralOperations.closeStatement(st);
        }
        
    }

    public String obtenerDocumentoTercero(String codTercero, String[] params) throws Exception {
        String documento = "nulo";
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            m_Log.debug("obtenerDocumentoTercero - codigo del tercero: " + codTercero);
            sql = "SELECT HTE_DOC FROM T_HTE WHERE HTE_TER = '" + codTercero + "' AND HTE_FOP IS NOT NULL AND HTE_DOC IS NOT NULL AND ROWNUM = 1 ORDER BY HTE_FOP DESC";
            m_Log.debug("obtenerDocumentoTercero - query SQL: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
				documento = rs.getString("HTE_DOC");
            }
            m_Log.debug("obtenerDocumentoTercero - Documento del tercero: " + documento);
        } catch (BDException e) {
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
			throw new Exception(e);
		} finally {
			try {
				SigpGeneralOperations.closeResultSet(rs);
				SigpGeneralOperations.closeStatement(st);
				SigpGeneralOperations.devolverConexion(oad, con);
			} catch (Exception e){
				m_Log.error("obtenerDocumentoTercero - Error al liberar recursos de base de datos");
			}
		}
		return documento;
	}
}
