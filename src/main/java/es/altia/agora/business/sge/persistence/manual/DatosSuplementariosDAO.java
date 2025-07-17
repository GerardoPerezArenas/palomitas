package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.sge.AlarmaExpedienteVO;
import es.altia.agora.business.sge.CampoSuplementarioFicheroVO;
import es.altia.agora.business.sge.DefinicionCampoValueObject;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoBBDD;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.CampoSuplementarioFechaVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.JdbcOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class DatosSuplementariosDAO {

    //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
    //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
    //Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(DatosSuplementariosDAO.class.getName());
    protected static String tnu_mun;
    protected static String tnu_eje;
    protected static String tnu_num;
    protected static String tnu_cod;
    protected static String tnu_valor;
    protected static String tnuc_mun;
    protected static String tnuc_eje;
    protected static String tnuc_num;
    protected static String tnuc_cod;
    protected static String tnuc_valor;
    protected static String txt_mun;
    protected static String txt_eje;
    protected static String txt_num;
    protected static String txt_cod;
    protected static String txt_valor;
    protected static String tfe_mun;
    protected static String tfe_eje;
    protected static String tfe_num;
    protected static String tfe_cod;
    protected static String tfe_valor;
    protected static String tfec_mun;
    protected static String tfec_eje;
    protected static String tfec_num;
    protected static String tfec_cod;
    protected static String tfec_valor;
    protected static String tfi_mun;
    protected static String tfi_eje;
    protected static String tfi_num;
    protected static String tfi_cod;
    protected static String tfi_valor;
    protected static String tfi_mime;
    protected static String tfi_nom_fich;
    protected static String ttl_mun;
    protected static String ttl_eje;
    protected static String ttl_num;
    protected static String ttl_cod;
    protected static String ttl_valor;
    protected static String tda_cod;
    protected static String tda_des;
    protected static String tda_tab;
    protected static String tde_mun;
    protected static String tde_eje;
    protected static String tde_num;
    protected static String tde_cod;
    protected static String tde_valor;
    protected static String tnut_mun;
    protected static String tnut_pro;
    protected static String tnut_eje;
    protected static String tnut_num;
    protected static String tnut_tra;
    protected static String tnut_ocu;
    protected static String tnut_cod;
    protected static String tnut_valor;
    protected static String tnuct_mun;
    protected static String tnuct_pro;
    protected static String tnuct_eje;
    protected static String tnuct_num;
    protected static String tnuct_tra;
    protected static String tnuct_ocu;
    protected static String tnuct_cod;
    protected static String tnuct_valor;
    protected static String txtt_mun;
    protected static String txtt_pro;
    protected static String txtt_eje;
    protected static String txtt_num;
    protected static String txtt_tra;
    protected static String txtt_ocu;
    protected static String txtt_cod;
    protected static String txtt_valor;
    protected static String tfet_mun;
    protected static String tfet_pro;
    protected static String tfet_eje;
    protected static String tfet_num;
    protected static String tfet_tra;
    protected static String tfet_ocu;
    protected static String tfet_cod;
    protected static String tfet_valor;
    protected static String tfet_fechaVencimiento;
    protected static String tfet_plazoActivado;
    protected static String tfect_mun;
    protected static String tfect_pro;
    protected static String tfect_eje;
    protected static String tfect_num;
    protected static String tfect_tra;
    protected static String tfect_ocu;
    protected static String tfect_cod;
    protected static String tfect_valor;
    protected static String tfit_mun;
    protected static String tfit_pro;
    protected static String tfit_eje;
    protected static String tfit_num;
    protected static String tfit_tra;
    protected static String tfit_ocu;
    protected static String tfit_cod;
    protected static String tfit_valor;
    protected static String tfit_mime;
    protected static String tfit_nom_fich;
    protected static String ttlt_mun;
    protected static String ttlt_pro;
    protected static String ttlt_eje;
    protected static String ttlt_num;
    protected static String ttlt_tra;
    protected static String ttlt_ocu;
    protected static String ttlt_cod;
    protected static String ttlt_valor;
    protected static String tdet_mun;
    protected static String tdet_pro;
    protected static String tdet_eje;
    protected static String tdet_num;
    protected static String tdet_tra;
    protected static String tdet_ocu;
    protected static String tdet_cod;
    protected static String tdet_valor;
    protected static String tca_mun;
    protected static String tca_pro;
    protected static String tca_tra;
    protected static String tca_cod;
    protected static String tca_des;
    protected static String des_cod;
    protected static String desv_cod;
    protected static String desv_val_cod;
    protected static String desv_nom;
    protected static String exp_mun;
    protected static String exp_num;
    protected static String exp_pro;
    protected static String pca_cod;
    protected static String pca_pro;
    protected static String pca_mun;
    protected static String pca_des;
    private static DatosSuplementariosDAO instance = null;

    protected DatosSuplementariosDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");


        //Conexion

        tnu_mun = m_ConfigTechnical.getString("SQL.E_TNU.codMunicipio");
        tnu_eje = m_ConfigTechnical.getString("SQL.E_TNU.ejercicio");
        tnu_num = m_ConfigTechnical.getString("SQL.E_TNU.numeroExpediente");
        tnu_cod = m_ConfigTechnical.getString("SQL.E_TNU.codCampo");
        tnu_valor = m_ConfigTechnical.getString("SQL.E_TNU.valor");

        tnuc_mun = m_ConfigTechnical.getString("SQL.E_TNUC.codMunicipio");
        tnuc_eje = m_ConfigTechnical.getString("SQL.E_TNUC.ejercicio");
        tnuc_num = m_ConfigTechnical.getString("SQL.E_TNUC.numeroExpediente");
        tnuc_cod = m_ConfigTechnical.getString("SQL.E_TNUC.codCampo");
        tnuc_valor = m_ConfigTechnical.getString("SQL.E_TNUC.valor");

        txt_mun = m_ConfigTechnical.getString("SQL.E_TXT.codMunicipio");
        txt_eje = m_ConfigTechnical.getString("SQL.E_TXT.ejercicio");
        txt_num = m_ConfigTechnical.getString("SQL.E_TXT.numeroExpediente");
        txt_cod = m_ConfigTechnical.getString("SQL.E_TXT.codCampo");
        txt_valor = m_ConfigTechnical.getString("SQL.E_TXT.valor");

        tfe_mun = m_ConfigTechnical.getString("SQL.E_TFE.codMunicipio");
        tfe_eje = m_ConfigTechnical.getString("SQL.E_TFE.ejercicio");
        tfe_num = m_ConfigTechnical.getString("SQL.E_TFE.numeroExpediente");
        tfe_cod = m_ConfigTechnical.getString("SQL.E_TFE.codCampo");
        tfe_valor = m_ConfigTechnical.getString("SQL.E_TFE.valor");


        tfec_mun = m_ConfigTechnical.getString("SQL.E_TFEC.codMunicipio");
        tfec_eje = m_ConfigTechnical.getString("SQL.E_TFEC.ejercicio");
        tfec_num = m_ConfigTechnical.getString("SQL.E_TFEC.numeroExpediente");
        tfec_cod = m_ConfigTechnical.getString("SQL.E_TFEC.codCampo");
        tfec_valor = m_ConfigTechnical.getString("SQL.E_TFEC.valor");

        tfi_mun = m_ConfigTechnical.getString("SQL.E_TFI.codMunicipio");
        tfi_eje = m_ConfigTechnical.getString("SQL.E_TFI.ejercicio");
        tfi_num = m_ConfigTechnical.getString("SQL.E_TFI.numeroExpediente");
        tfi_cod = m_ConfigTechnical.getString("SQL.E_TFI.codCampo");
        tfi_valor = m_ConfigTechnical.getString("SQL.E_TFI.valor");
        tfi_mime = m_ConfigTechnical.getString("SQL.E_TFI.mime");
        tfi_nom_fich = m_ConfigTechnical.getString("SQL.E_TFI.nombreFichero");

        tfit_mun = m_ConfigTechnical.getString("SQL.E_TFIT.codMunicipio");
        tfit_pro = m_ConfigTechnical.getString("SQL.E_TFIT.codProcedimiento");
        tfit_eje = m_ConfigTechnical.getString("SQL.E_TFIT.ejercicio");
        tfit_num = m_ConfigTechnical.getString("SQL.E_TFIT.numeroExpediente");
        tfit_cod = m_ConfigTechnical.getString("SQL.E_TFIT.codCampo");
        tfit_valor = m_ConfigTechnical.getString("SQL.E_TFIT.valor");
        tfit_mime = m_ConfigTechnical.getString("SQL.E_TFIT.mime");
        tfit_nom_fich = m_ConfigTechnical.getString("SQL.E_TFIT.nombreFichero");
        tfit_tra = m_ConfigTechnical.getString("SQL.E_TFIT.codTramite");
        tfit_ocu = m_ConfigTechnical.getString("SQL.E_TFIT.ocurrencia");

        ttl_mun = m_ConfigTechnical.getString("SQL.E_TTL.codMunicipio");
        ttl_eje = m_ConfigTechnical.getString("SQL.E_TTL.ejercicio");
        ttl_num = m_ConfigTechnical.getString("SQL.E_TTL.numeroExpediente");
        ttl_cod = m_ConfigTechnical.getString("SQL.E_TTL.codCampo");
        ttl_valor = m_ConfigTechnical.getString("SQL.E_TTL.valor");

        tda_cod = m_ConfigTechnical.getString("SQL.E_TDA.codDato");
        tda_des = m_ConfigTechnical.getString("SQL.E_TDA.descripcion");
        tda_tab = m_ConfigTechnical.getString("SQL.E_TDA.tabla");

        tde_mun = m_ConfigTechnical.getString("SQL.E_TDE.codMunicipio");
        tde_eje = m_ConfigTechnical.getString("SQL.E_TDE.ejercicio");
        tde_num = m_ConfigTechnical.getString("SQL.E_TDE.numeroExpediente");
        tde_cod = m_ConfigTechnical.getString("SQL.E_TDE.codCampo");
        tde_valor = m_ConfigTechnical.getString("SQL.E_TDE.valor");

        tnut_mun = m_ConfigTechnical.getString("SQL.E_TNUT.codMunicipio");
        tnut_pro = m_ConfigTechnical.getString("SQL.E_TNUT.codProcedimiento");
        tnut_eje = m_ConfigTechnical.getString("SQL.E_TNUT.ejercicio");
        tnut_num = m_ConfigTechnical.getString("SQL.E_TNUT.numeroExpediente");
        tnut_tra = m_ConfigTechnical.getString("SQL.E_TNUT.codTramite");
        tnut_ocu = m_ConfigTechnical.getString("SQL.E_TNUT.ocurrencia");
        tnut_cod = m_ConfigTechnical.getString("SQL.E_TNUT.codCampo");
        tnut_valor = m_ConfigTechnical.getString("SQL.E_TNUT.valor");

        tnuct_mun = m_ConfigTechnical.getString("SQL.E_TNUCT.codMunicipio");
        tnuct_pro = m_ConfigTechnical.getString("SQL.E_TNUCT.codProcedimiento");
        tnuct_eje = m_ConfigTechnical.getString("SQL.E_TNUCT.ejercicio");
        tnuct_num = m_ConfigTechnical.getString("SQL.E_TNUCT.numeroExpediente");
        tnuct_tra = m_ConfigTechnical.getString("SQL.E_TNUCT.codTramite");
        tnuct_ocu = m_ConfigTechnical.getString("SQL.E_TNUCT.ocurrencia");
        tnuct_cod = m_ConfigTechnical.getString("SQL.E_TNUCT.codCampo");
        tnuct_valor = m_ConfigTechnical.getString("SQL.E_TNUCT.valor");

        txtt_mun = m_ConfigTechnical.getString("SQL.E_TXTT.codMunicipio");
        txtt_pro = m_ConfigTechnical.getString("SQL.E_TXTT.codProcedimiento");
        txtt_eje = m_ConfigTechnical.getString("SQL.E_TXTT.ejercicio");
        txtt_num = m_ConfigTechnical.getString("SQL.E_TXTT.numeroExpediente");
        txtt_tra = m_ConfigTechnical.getString("SQL.E_TXTT.codTramite");
        txtt_ocu = m_ConfigTechnical.getString("SQL.E_TXTT.ocurrencia");
        txtt_cod = m_ConfigTechnical.getString("SQL.E_TXTT.codCampo");
        txtt_valor = m_ConfigTechnical.getString("SQL.E_TXTT.valor");

        tfet_mun = m_ConfigTechnical.getString("SQL.E_TFET.codMunicipio");
        tfet_pro = m_ConfigTechnical.getString("SQL.E_TFET.codProcedimiento");
        tfet_eje = m_ConfigTechnical.getString("SQL.E_TFET.ejercicio");
        tfet_num = m_ConfigTechnical.getString("SQL.E_TFET.numeroExpediente");
        tfet_tra = m_ConfigTechnical.getString("SQL.E_TFET.codTramite");
        tfet_ocu = m_ConfigTechnical.getString("SQL.E_TFET.ocurrencia");
        tfet_cod = m_ConfigTechnical.getString("SQL.E_TFET.codCampo");
        tfet_valor = m_ConfigTechnical.getString("SQL.E_TFET.valor");

        tfect_mun = m_ConfigTechnical.getString("SQL.E_TFECT.codMunicipio");
        tfect_pro = m_ConfigTechnical.getString("SQL.E_TFECT.codProcedimiento");
        tfect_eje = m_ConfigTechnical.getString("SQL.E_TFECT.ejercicio");
        tfect_num = m_ConfigTechnical.getString("SQL.E_TFECT.numeroExpediente");
        tfect_tra = m_ConfigTechnical.getString("SQL.E_TFECT.codTramite");
        tfect_ocu = m_ConfigTechnical.getString("SQL.E_TFECT.ocurrencia");
        tfect_cod = m_ConfigTechnical.getString("SQL.E_TFECT.codCampo");
        tfect_valor = m_ConfigTechnical.getString("SQL.E_TFECT.valor");

        tfit_mun = m_ConfigTechnical.getString("SQL.E_TFIT.codMunicipio");
        tfit_pro = m_ConfigTechnical.getString("SQL.E_TFIT.codProcedimiento");
        tfit_eje = m_ConfigTechnical.getString("SQL.E_TFIT.ejercicio");
        tfit_num = m_ConfigTechnical.getString("SQL.E_TFIT.numeroExpediente");
        tfit_tra = m_ConfigTechnical.getString("SQL.E_TFIT.codTramite");
        tfit_ocu = m_ConfigTechnical.getString("SQL.E_TFIT.ocurrencia");
        tfit_cod = m_ConfigTechnical.getString("SQL.E_TFIT.codCampo");
        tfit_valor = m_ConfigTechnical.getString("SQL.E_TFIT.valor");

        ttlt_mun = m_ConfigTechnical.getString("SQL.E_TTLT.codMunicipio");
        ttlt_pro = m_ConfigTechnical.getString("SQL.E_TTLT.codProcedimiento");
        ttlt_eje = m_ConfigTechnical.getString("SQL.E_TTLT.ejercicio");
        ttlt_num = m_ConfigTechnical.getString("SQL.E_TTLT.numeroExpediente");
        ttlt_tra = m_ConfigTechnical.getString("SQL.E_TTLT.codTramite");
        ttlt_ocu = m_ConfigTechnical.getString("SQL.E_TTLT.ocurrencia");
        ttlt_cod = m_ConfigTechnical.getString("SQL.E_TTLT.codCampo");
        ttlt_valor = m_ConfigTechnical.getString("SQL.E_TTLT.valor");

        tdet_mun = m_ConfigTechnical.getString("SQL.E_TDET.codMunicipio");
        tdet_pro = m_ConfigTechnical.getString("SQL.E_TDET.codProcedimiento");
        tdet_eje = m_ConfigTechnical.getString("SQL.E_TDET.ejercicio");
        tdet_num = m_ConfigTechnical.getString("SQL.E_TDET.numeroExpediente");
        tdet_tra = m_ConfigTechnical.getString("SQL.E_TDET.codTramite");
        tdet_ocu = m_ConfigTechnical.getString("SQL.E_TDET.ocurrencia");
        tdet_cod = m_ConfigTechnical.getString("SQL.E_TDET.codCampo");
        tdet_valor = m_ConfigTechnical.getString("SQL.E_TDET.valor");

        tca_mun = m_ConfigTechnical.getString("SQL.E_TCA.codMunicipio");
        tca_pro = m_ConfigTechnical.getString("SQL.E_TCA.codProcedimiento");
        tca_tra = m_ConfigTechnical.getString("SQL.E_TCA.codTramite");
        tca_cod = m_ConfigTechnical.getString("SQL.E_TCA.codCampo");
        tca_des = m_ConfigTechnical.getString("SQL.E_TCA.desplegable");

        des_cod = m_ConfigTechnical.getString("SQL.E_DES.codigo");

        desv_cod = m_ConfigTechnical.getString("SQL.E_DES_VAL.campoValor");
        desv_val_cod = m_ConfigTechnical.getString("SQL.E_DES_VAL.codigoValor");
        desv_nom = m_ConfigTechnical.getString("SQL.E_DES_VAL.nombreValor");

        exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        exp_num = m_ConfigTechnical.getString("SQL.E_EXP.numero");
        exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");

        pca_mun = m_ConfigTechnical.getString("SQL.E_PCA.codMunicipio"); 
        pca_des = m_ConfigTechnical.getString("SQL.E_PCA.desplegable");
        pca_cod = m_ConfigTechnical.getString("SQL.E_PCA.codCampo");
        pca_pro = m_ConfigTechnical.getString("SQL.E_PCA.codProcedimiento");

    }

    public static DatosSuplementariosDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized (DatosSuplementariosDAO.class) {
                if (instance == null) {
                    instance = new DatosSuplementariosDAO();
                }
            }
        }
        return instance;
    }

    public CamposFormulario getValoresTexto(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try {
            

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            String sql;
            if (expHistorico)
                sql = "SELECT TXT_COD,TXT_VALOR FROM HIST_E_TXT WHERE TXT_MUN = ?"
                    + " AND TXT_EJE = ? AND TXT_NUM = ?";
            else
                sql = "SELECT TXT_COD,TXT_VALOR FROM E_TXT WHERE TXT_MUN = ?"
                    + " AND TXT_EJE = ? AND TXT_NUM = ?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i= 1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            String entrar = "no";

            HashMap<String, String> campos = new HashMap<String, String>();
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TXT_COD");
                String valorCampo = rs.getString("TXT_VALOR");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }

    public CamposFormulario getValoresTextoTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {
        Statement st = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            String sql = "";

            if(!tEVO.isExpHistorico()) {
                sql = "SELECT " + txtt_cod + "," + txtt_valor + " FROM E_TXTT WHERE " + txtt_mun + " = "
                    + codMunicipio + " AND " + txtt_pro + "='" + codProcedimiento + "' AND " + txtt_eje
                    + " = " + ejercicio + " AND " + txtt_num + " ='" + numero + "' AND " + txtt_tra + "="
                    + codTramite + " AND " + txtt_ocu + "=" + ocurrencia;
            } else {
                sql = "SELECT " + txtt_cod + "," + txtt_valor + " FROM HIST_E_TXTT WHERE " + txtt_mun + " = "
                    + codMunicipio + " AND " + txtt_pro + "='" + codProcedimiento + "' AND " + txtt_eje
                    + " = " + ejercicio + " AND " + txtt_num + " ='" + numero + "' AND " + txtt_tra + "="
                    + codTramite + " AND " + txtt_ocu + "=" + ocurrencia;
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);

            HashMap<String, String> campos = new HashMap<String, String>();
            while (rs.next()) {
                String codCampo = rs.getString(txtt_cod);
                String valorCampo = rs.getString(txtt_valor);
                campos.put(codCampo, valorCampo);
                campos.put("T" + codTramite + codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);


        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresTextoTramite(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String codTramite,
            String ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

           boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql = "SELECT " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TXTT_COD"}) + 
                    " AS TXTT_COD,TXTT_VALOR ";
            if (expHistorico)
                sql += "FROM HIST_E_TXTT ";
            else
                sql += "FROM E_TXTT ";
            
            sql += "WHERE TXTT_MUN  = ? AND txtt_pro  =? AND " + 
                    "txtt_eje =? AND txtt_num =? AND txtt_tra =? AND txtt_ocu =?";
            
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(ocurrencia));
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TXTT_COD");
                String valorCampo = rs.getString("TXTT_VALOR");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps!= null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }

    public CamposFormulario getValoresNumericos(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql = "SELECT TNU_COD," + oad.convertir("TNU_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNU_VALOR";
            if (expHistorico)
                sql += " FROM HIST_E_TNU WHERE TNU_MUN =? AND TNU_EJE =? AND TNU_NUM =?";
            else
                sql += " FROM E_TNU WHERE TNU_MUN =? AND TNU_EJE =? AND TNU_NUM =?";
            
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);           
            rs = ps.executeQuery();
            
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tnu_cod);
                String valorCampo = rs.getString(tnu_valor);
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresNumericosCal(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql = "SELECT tnuc_cod," + oad.convertir("tnuc_valor", oad.CONVERTIR_COLUMNA_TEXTO, null) + 
                    " AS tnuc_valor ";
            
            if (expHistorico)
                sql += "FROM HIST_E_TNUC ";
            else
                sql += "FROM E_TNUC ";

            sql += "WHERE tnuc_mun  = " + codMunicipio + " AND tnuc_eje = " + ejercicio + " AND tnuc_num ='" + numero + "'";

            

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tnuc_cod");
                String valorCampo = rs.getString("tnuc_valor");
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }     
    
    
    
    public CamposFormulario getValoresDesplegableExt(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {
    /*
     * @Descripcion --> Recuperacion de valores asignados a un procedimiento para campos suplementarios de tipo desplegables externos
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */
        Statement st = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

           boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql;
            
            if (expHistorico) 
                sql = "SELECT TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM HIST_E_TDEX WHERE TDEX_MUN = " + codMunicipio
                    + " AND TDEX_EJE = " + ejercicio + " AND TDEX_NUM ='" + numero + "'";
            else
                sql = "SELECT TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM E_TDEX WHERE TDEX_MUN = " + codMunicipio
                    + " AND TDEX_EJE = " + ejercicio + " AND TDEX_NUM ='" + numero + "'";


            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            String entrar = "no";

            HashMap<String, String> campos = new HashMap<String, String>();
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TDEX_COD");
                String valorCampo = rs.getString("TDEX_VALOR");
                String codValorCampo = rs.getString("TDEX_CODSEL");
                campos.put(codCampo, valorCampo);
                campos.put(codCampo+"_CODSEL", codValorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    
    public CamposFormulario getValoresDesplegableExtTramite(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String codTramite,String ocurrencia) {
    /*
     * @Descripcion --> Recuperacion de valores asignados para campos suplementario de tipo de desplegables externos a nivel tramite.
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));

            String sql = "SELECT ";
            sql = sql + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TDEXT_COD" });
            sql = sql + " AS TDEXT_COD , TDEXT_VALOR, TDEXT_CODSEL ";
            
            if (expHistorico)
                sql = sql + "FROM HIST_E_TDEXT ";
            else
                sql = sql + "FROM E_TDEXT ";

            sql = sql + "WHERE TDEXT_MUN = " + codMunicipio + " AND TDEXT_PRO ='" + codProcedimiento;
            sql = sql + "' AND TDEXT_EJE = " + ejercicio + " AND TDEXT_NUM ='" + numero + "' AND TDEXT_TRA = ";
            sql = sql + codTramite + " AND TDEXT_OCU =" + ocurrencia;

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TDEXT_COD");
                String valorCampo = rs.getString("TDEXT_VALOR");
                String codValorCampo = rs.getString("TDEXT_CODSEL");
                campos.put(codCampo, valorCampo);
                campos.put(codCampo+"_CODSEL", codValorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    public CamposFormulario getValoresDesplegableExtTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {
        Statement st = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            String sql = "";

            if(!tEVO.isExpHistorico()) {
                sql = "SELECT TDEXT_COD,TDEXT_VALOR, TDEXT_CODSEL FROM E_TDEXT WHERE TDEXT_MUN = "
                    + codMunicipio + " AND TDEXT_PRO='" + codProcedimiento + "' AND TDEXT_EJE "
                    + " = " + ejercicio + " AND TDEXT_NUM ='" + numero + "' AND TDEXT_TRA ="
                    + codTramite + " AND TDEXT_OCU =" + ocurrencia;                
            } else {
                sql = "SELECT TDEXT_COD,TDEXT_VALOR, TDEXT_CODSEL FROM HIST_E_TDEXT WHERE TDEXT_MUN = "
                    + codMunicipio + " AND TDEXT_PRO='" + codProcedimiento + "' AND TDEXT_EJE "
                    + " = " + ejercicio + " AND TDEXT_NUM ='" + numero + "' AND TDEXT_TRA ="
                    + codTramite + " AND TDEXT_OCU =" + ocurrencia;                
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);

            HashMap<String, String> campos = new HashMap<String, String>();
            while (rs.next()) {
                String codCampo = rs.getString("TDEXT_COD");
                String valorCampo = rs.getString("TDEXT_VALOR");
                String codValorCampo = rs.getString("TDEXT_CODSEL");
                campos.put(codCampo, valorCampo);
                campos.put("T" + codTramite + codCampo, valorCampo);
                campos.put(codCampo+"_CODSEL", codValorCampo);
                campos.put("T" + codTramite + codCampo + "_CODSEL", codValorCampo);

            }

            cF = new CamposFormulario(campos);


        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }
    
    public CamposFormulario getValoresNumericosTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {
        return getValoresNumericosTramite(oad, con, tEVO, false);
    }
    
    public CamposFormulario getValoresNumericosTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO, boolean campoCalculado) {
        Statement st = null;
        ResultSet rs = null;
        HashMap<String, String> campos = new HashMap<String, String>();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            String sql = "";
            String tabla = "E_TNUT";
            
            if(tEVO.isExpHistorico() && !campoCalculado) {
               tabla = "HIST_E_TNUT" ;
            } else if(tEVO.isExpHistorico()){
                tabla = "HIST_E_TNUCT";
            } else if(campoCalculado){
                tabla = "E_TNUCT";
            }
            
            if(!campoCalculado) {
                sql = "SELECT " + tnut_cod + "," + oad.convertir(tnut_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " AS " + tnut_valor + " FROM " + tabla + " WHERE " + tnut_mun + " = "
                    + codMunicipio + " AND " + tnut_pro + "='" + codProcedimiento + "' AND " + tnut_eje
                    + " = " + ejercicio + " AND " + tnut_num + " ='" + numero + "' AND " + tnut_tra + "="
                    + codTramite + " AND " + tnut_ocu + "=" + ocurrencia;
            } else {
                sql = "SELECT TNUCT_COD AS " + tnut_cod + "," + oad.convertir("TNUCT_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNUT_VALOR FROM " + tabla + " WHERE TNUCT_MUN = "
                    + codMunicipio + " AND TNUCT_PRO='" + codProcedimiento + "' AND TNUCT_EJE = " 
                    + ejercicio + " AND TNUCT_NUM ='" + numero + "' AND TNUCT_TRA="
                    + codTramite + " AND TNUCT_OCU=" + ocurrencia;                
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String codCampo = rs.getString(tnut_cod);
                String valorCampo = rs.getString(tnut_valor);
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }
                campos.put(codCampo, valorCampo);
                campos.put("T" + codTramite + codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cF;
    }

    public CamposFormulario getValoresNumericosTramiteCal(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {
        Statement st = null;
        ResultSet rs = null;
        HashMap<String, String> campos = new HashMap<String, String>();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            String sql = "";

            if(!tEVO.isExpHistorico()) {
                sql = "SELECT " + tnuct_cod + "," + oad.convertir(tnuct_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " AS " + tnuct_valor + " FROM E_TNUCT WHERE " + tnuct_mun + " = "
                    + codMunicipio + " AND " + tnuct_pro + "='" + codProcedimiento + "' AND " + tnuct_eje
                    + " = " + ejercicio + " AND " + tnuct_num + " ='" + numero + "' AND " + tnuct_tra + "="
                    + codTramite + " AND " + tnuct_ocu + "=" + ocurrencia;
            } else {
                sql = "SELECT " + tnuct_cod + "," + oad.convertir(tnuct_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " AS " + tnuct_valor + " FROM HIST_E_TNUCT WHERE " + tnuct_mun + " = "
                    + codMunicipio + " AND " + tnuct_pro + "='" + codProcedimiento + "' AND " + tnuct_eje
                    + " = " + ejercicio + " AND " + tnuct_num + " ='" + numero + "' AND " + tnuct_tra + "="
                    + codTramite + " AND " + tnuct_ocu + "=" + ocurrencia;                
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String codCampo = rs.getString(tnuct_cod);
                String valorCampo = rs.getString(tnuct_valor);
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }
                campos.put(codCampo, valorCampo);
                campos.put("T" + codTramite + codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cF;
    }

    public CamposFormulario getValoresNumericosTramite(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String codTramite,
            String ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

             boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));

            
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "TNUT_COD"}) + " AS TNUT_COD,"
                    + oad.convertir("TNUT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null)
                    + " AS TNUT_VALOR ";
            
            if (expHistorico)
                sql += "FROM HIST_E_TNUT ";
            else
                sql += "FROM E_TNUT ";
                
            sql +=  "WHERE TNUT_MUN =? AND TNUT_PRO =? " + 
                    "AND TNUT_EJE =? AND TNUT_NUM =? AND TNUT_TRA =?" + 
                    " AND TNUT_OCU =?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(ocurrencia));
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TNUT_COD");
                String valorCampo = rs.getString("TNUT_VALOR");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresNumericosTramiteCal(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String codTramite, String ocurrencia) {
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

             boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));

            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "tnuct_cod"}) + " AS tnuct_cod,"
                    + oad.convertir("tnuct_valor", oad.CONVERTIR_COLUMNA_TEXTO, null)
                    + " AS tnuct_valor ";
            
            if (expHistorico)
                sql += "FROM HIST_E_TNUCT ";
            else
                sql += "FROM E_TNUCT ";
            
            sql += "WHERE tnuct_mun = " + codMunicipio + " AND tnuct_pro ='" + codProcedimiento + 
                    "' AND tnuct_eje = " + ejercicio + " AND tnuct_num ='" + numero + "' AND tnuct_tra =" + 
                    codTramite + " AND tnuct_ocu =" + ocurrencia;

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tnuct_cod");
                String valorCampo = rs.getString("tnuct_valor");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }

    public CamposFormulario getValoresFichero(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

             boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql;
            
            if (expHistorico)
                sql = "SELECT tfi_cod,tfi_valor,tfi_mime,tfi_nomfich FROM HIST_E_TFI WHERE tfi_mun =? AND tfi_eje =? AND tfi_num =?";
            else
                sql = "SELECT tfi_cod,tfi_nomfich FROM E_TFI WHERE tfi_mun =? AND tfi_eje =? AND tfi_num =?";

            

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);            
            rs = ps.executeQuery();
            
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tfi_cod");
                String valorCampo = rs.getString("tfi_nomfich");
                 if (expHistorico) {
                    byte [] fichero = rs.getBytes("tfi_valor");
                    
                    campos.put(codCampo, fichero);
                    
                    String mime = rs.getString("tfi_mime");
                    campos.put(codCampo + "_TIPO", mime);
                    campos.put(codCampo + "_NOMBRE", valorCampo);
                } else {
                    campos.put(codCampo, valorCampo);
                }
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresFicheroTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            
            String sql = "";
            if(!tEVO.isExpHistorico()) {
                sql = "SELECT " + tfit_cod + "," + tfit_nom_fich + " FROM E_TFIT WHERE " + tfit_mun + " = "
                     + codMunicipio + " AND " + tfit_pro + "='" + codProcedimiento + "' AND " + tfit_eje
                     + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                     + codTramite + " AND " + tfit_ocu + "=" + ocurrencia;
            } else {
                sql = "SELECT " + tfit_cod + "," + tfit_nom_fich + " FROM HIST_E_TFIT WHERE " + tfit_mun + " = "
                     + codMunicipio + " AND " + tfit_pro + "='" + codProcedimiento + "' AND " + tfit_eje
                     + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                     + codTramite + " AND " + tfit_ocu + "=" + ocurrencia;                
            }
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tfit_cod);
                String valorCampo = rs.getString(tfit_nom_fich);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cF;
    }

    public CamposFormulario getValoresFicheroTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,int codTramite,int ocurrencia) {        
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
                
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", tfit_cod}) + " AS " + tfit_cod + "," + tfit_nom_fich
                    + " FROM E_TFIT WHERE " + tfit_mun + "=? AND " + tfit_pro + "=? AND " + 
                    tfit_eje + "=? AND " + tfit_num + "=? AND " + tfit_tra + "=? AND " + tfit_ocu + "=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tfit_cod);
                String valorCampo = rs.getString(tfit_nom_fich);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    
    
    public ValorCampoSuplementarioVO getValoresFicheroTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,int codTramite,int ocurrencia,String codCampo) {        
        PreparedStatement ps = null;
        ResultSet rs = null;                
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        
        try {                
            String sql;
            if (dato.isExpHistorico())
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "TFIT_COD"}) + " AS TFIT_COD,TFIT_NOMFICH"
                    + ",TFIT_TAMANHO,TFIT_MIME FROM HIST_E_TFIT WHERE TFIT_MUN = ? AND TFIT_PRO = ? AND " + 
                   "TFIT_EJE = ? AND TFIT_NUM = ? AND TFIT_TRA = ? AND TFIT_OCU = ? AND TFIT_COD=?";
            else
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "TFIT_COD"}) + " AS TFIT_COD,TFIT_NOMFICH"
                    + ",TFIT_TAMANHO,TFIT_MIME FROM E_TFIT WHERE TFIT_MUN = ? AND TFIT_PRO = ? AND " + 
                   "TFIT_EJE = ? AND TFIT_NUM = ? AND TFIT_TRA = ? AND TFIT_OCU = ? AND TFIT_COD=?";
            
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            ps.setString(i++,codCampo);            
            rs = ps.executeQuery();
            
            while (rs.next()) {            
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TFIT_NOMFICH"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_FICHERO);
                valor.setTipoMimeFichero(rs.getString("TFIT_MIME"));
                valor.setLongitudFichero(rs.getInt("TFIT_TAMANHO"));
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    
    

    public CamposFormulario getValoresTextoLargo(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = new CamposFormulario(campos);

        try {
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
           
            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql;
            
            if (expHistorico)
                sql = "SELECT ttl_cod,ttl_valor FROM HIST_E_TTL WHERE ttl_mun = ? AND ttl_eje =? AND ttl_num =?";
            else
                sql = "SELECT ttl_cod,ttl_valor FROM E_TTL WHERE ttl_mun = ? AND ttl_eje =? AND ttl_num =?";
            
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            pst = con.prepareStatement(sql);
            pst.setInt(i++,Integer.parseInt(codMunicipio));
            pst.setInt(i++,Integer.parseInt(ejercicio));
            pst.setString(i++,numero);
            
            rs = pst.executeQuery();
            String codCampo = null;
            String valorCampo = null;
            while (rs.next()) {
                valorCampo = new String("");
                codCampo = rs.getString("ttl_cod");
                java.io.Reader cr = rs.getCharacterStream("ttl_valor");
                if (cr == null) {
                    valorCampo = null;
                } else {
                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    valorCampo = ot.toString();
                    ot.close();
                    cr.close();
                }
                if ("".equals(codCampo) || "".equals(valorCampo)) {
                    cF = new CamposFormulario(campos);
                } else {
                    valorCampo=valorCampo.trim();
                    valorCampo = valorCampo.replace("\n", "");
                    campos.put(codCampo, valorCampo);
                    cF = new CamposFormulario(campos);
                }
            }

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresTextoLargoTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {

        PreparedStatement pst = null;
        ResultSet rs = null;
        HashMap<String, String> campos = new HashMap<String, String>();
        CamposFormulario cF = new CamposFormulario(campos);

        try {
            String sql = "";
            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("------------getValoresTextoLargoTramite----------");
            }
            
            if(!tEVO.isExpHistorico()) {
                sql = "SELECT " + ttlt_cod + "," + ttlt_valor + " FROM E_TTLT WHERE " + ttlt_mun + "=?"
                    + " AND " + ttlt_pro + "=? AND " + ttlt_eje + " =? AND " + 
                    ttlt_num + "=? AND " + ttlt_tra + "=? AND " + ttlt_ocu + "=?";
            } else {
                sql = "SELECT " + ttlt_cod + "," + ttlt_valor + " FROM HIST_E_TTLT WHERE " + ttlt_mun + "=?"
                    + " AND " + ttlt_pro + "=? AND " + ttlt_eje + " =? AND " + 
                    ttlt_num + "=? AND " + ttlt_tra + "=? AND " + ttlt_ocu + "=?";                
            }                
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i = 1;
            pst = con.prepareStatement(sql);
            pst.setInt(i++,Integer.parseInt(codMunicipio));            
            pst.setString(i++,codProcedimiento);
            pst.setInt(i++,Integer.parseInt(ejercicio));            
            pst.setString(i++,numero);
            pst.setInt(i++,Integer.parseInt(codTramite));            
            pst.setInt(i++,Integer.parseInt(ocurrencia));            
            
            rs = pst.executeQuery();

            while (rs.next()) {
                String codCampo = rs.getString(ttlt_cod);
                Reader reader = rs.getCharacterStream(ttlt_valor);
                char[] cbuf = new char[65536];
                StringBuffer stringbuf = new StringBuffer();
                while (reader.read(cbuf, 0, 65536) != -1) {
                    stringbuf.append(cbuf);
                }
                String valorCampo = stringbuf.toString();
                if (!"".equals(codCampo) && !"".equals(valorCampo)) {
                    valorCampo=valorCampo.trim();
                    valorCampo = valorCampo.replace("\n", "");
                    campos.put(codCampo, valorCampo);
                    campos.put("T" + codTramite + codCampo, valorCampo);
                }
            }

            cF = new CamposFormulario(campos);

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cF;
    }

    public CamposFormulario getValoresTextoLargoTramite(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String codTramite,
            String ocurrencia) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = new CamposFormulario(campos);

        try {

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("--------------METODO getValoresTextoLargoTramite");
            }
            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("--------------METODO getValoresTextoLargoTramite");
            }
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "ttlt_cod"}) + " AS ttlt_cod, ttlt_valor"; 
            
            if (expHistorico)
               sql += " FROM HIST_E_TTLT";
            else
               sql += " FROM E_TTLT";
            
            sql += " WHERE ttlt_mun = " + codMunicipio + " AND ttlt_pro ='" + codProcedimiento + "' AND ttlt_eje = " + 
                    ejercicio + " AND ttlt_num ='" + numero + "' AND ttlt_tra=" + codTramite + " AND ttlt_ocu =" + ocurrencia;
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            String codCampo = null;
            String valorCampo = null;
            while (rs.next()) {
                codCampo = new String("");
                valorCampo = new String("");
            
                codCampo = rs.getString("ttlt_cod");
                Reader reader = rs.getCharacterStream("ttlt_valor");
                char[] cbuf = new char[65536];
                StringBuffer stringbuf = new StringBuffer();
                while (reader.read(cbuf, 0, 65536) != -1) {
                    stringbuf.append(cbuf);
                }//end while
                valorCampo = stringbuf.toString();
//                 codCampo = rs.getString(ttlt_cod);
                if ("".equals(codCampo) || "".equals(valorCampo)) {
                    cF = new CamposFormulario(campos);
                } else {
                    valorCampo=valorCampo.trim();
                    valorCampo = valorCampo.replace("\n", "");
                    campos.put(codCampo, valorCampo);
                    cF = new CamposFormulario(campos);
                }
            }

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }

    public CamposFormulario getValoresFecha(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String mascara) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";


        try {
            

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            if (mascara == null || "".equals(mascara)) {
                sql = "SELECT tfe_cod," + oad.convertir("tfe_valor", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");
            } else {
                sql = "SELECT tfe_cod," + oad.convertir("tfe_valor", oad.CONVERTIR_COLUMNA_TEXTO, mascara);
            }

            sql += " AS tfe_valor ," + oad.convertir("TFE_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + 
                    " AS TFE_FEC_VENCIMIENTO " + ",PLAZO_ACTIVADO ";
            
            if (expHistorico)
                sql += " FROM HIST_E_TFE";
            else
                sql += " FROM E_TFE";
    
            sql += " WHERE tfe_mun =? AND tfe_eje =? AND tfe_num =?";

            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tfe_cod");
                String valorCampo = rs.getString("tfe_valor");
                campos.put(codCampo, valorCampo);
                //fecha Vencimiento
                String codFechVencimiento = "fechaVencimiento" + codCampo;
                String valorFechaVencimiento = rs.getString("TFE_FEC_VENCIMIENTO");
                campos.put(codFechVencimiento, valorFechaVencimiento);
                //plazo activo
                String codPlazoActivo = "activar" + codCampo;
                String valorPlazoActivo = rs.getString(String.valueOf("PLAZO_ACTIVADO"));
                campos.put(codPlazoActivo, valorPlazoActivo);

                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresFechaCal(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String mascara) {
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";

        try {
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));

            if (mascara == null || "".equals(mascara)) {
                sql = "SELECT tfec_cod," + oad.convertir("tfec_valor", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");
            } else {
                sql = "SELECT tfec_cod," + oad.convertir("tfec_valor", oad.CONVERTIR_COLUMNA_TEXTO, mascara);
            }

            sql += " AS tfec_valor ";
            sql += "FROM E_TFEC WHERE tfec_mun = " + codMunicipio + " AND tfec_eje = "
                        + ejercicio + " AND tfec_num ='" + numero + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tfec_cod");
                String valorCampo = rs.getString("tfec_valor");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresFechaTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO, String mascara) {
        return getValoresFechaTramite(oad, con, tEVO, mascara, false);
    }
    
    public CamposFormulario getValoresFechaTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO, String mascara, boolean campoCalculado) {
        Statement st = null;
        ResultSet rs = null;
        HashMap<String, String> campos = new HashMap<String, String>();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            String prefijoCampo = "T_" + tEVO.getCodTramite() + "_" + tEVO.getOcurrenciaTramite() + "_";

            String sql;
            String tabla = "E_TFET";
            
            if(tEVO.isExpHistorico() && !campoCalculado) {
               tabla = "HIST_E_TFET" ;
            } else if(tEVO.isExpHistorico()){
                tabla = "HIST_E_TFECT";
            } else if(campoCalculado){
                tabla = "E_TFECT";
            }
            
            if (mascara == null || "".equals(mascara)) {
                mascara = "DD/MM/YYYY";
            }
            
            if(!campoCalculado) {
                sql = "SELECT " + tfet_cod + "," + oad.convertir(tfet_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, mascara)
                        + " AS " + tfet_valor + "," + oad.convertir("TFET_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO "
                        + " FROM " + tabla + " WHERE " + tfet_mun + " = " + codMunicipio + " AND " + tfet_pro + "='"
                        + codProcedimiento + "' AND " + tfet_eje + " = " + ejercicio + " AND " + tfet_num + " ='"
                        + numero + "' AND " + tfet_tra + "=" + codTramite + " AND " + tfet_ocu + "=" + ocurrencia;
            } else {
                sql = "SELECT tfect_cod AS " + tfet_cod + "," + oad.convertir("tfect_valor", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, mascara)
                        + " AS " + tfet_valor + "," + oad.convertir("TFECT_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO "
                        + " FROM " + tabla + " WHERE TFECT_MUN = " + codMunicipio + " AND TFECT_PRO='"
                        + codProcedimiento + "' AND TFECT_EJE = " + ejercicio + " AND TFECT_NUM ='"
                        + numero + "' AND TFECT_TRA=" + codTramite + " AND TFECT_OCU=" + ocurrencia;
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String codCampo = rs.getString(tfet_cod);
                String valorCampo = rs.getString(tfet_valor);
                campos.put(codCampo, valorCampo);
                campos.put("T" + codTramite + codCampo, valorCampo);
                campos.put(prefijoCampo + codCampo, valorCampo);
                //fecha Vencimiento
                String codFechVencimiento = "fechaVencimiento" + codCampo;
                String valorFechaVencimiento = rs.getString("TFET_FEC_VENCIMIENTO");
                campos.put(codFechVencimiento, valorFechaVencimiento);
                //plazo activo
                String codPlazoActivo = "activar" + prefijoCampo + codCampo;
                String valorPlazoActivo = rs.getString(String.valueOf("PLAZO_ACTIVADO"));
                campos.put(codPlazoActivo, valorPlazoActivo);
            }

            cF = new CamposFormulario(campos);

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cF;
    }

    public CamposFormulario getValoresFechaTramiteCal(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO, String mascara) {
        Statement st = null;
        ResultSet rs = null;
        HashMap<String, String> campos = new HashMap<String, String>();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            String sql = "";
            
            if(!tEVO.isExpHistorico()) {
                if (mascara == null || "".equals(mascara)) {
                    sql = "SELECT " + tfect_cod + "," + oad.convertir(tfect_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
                            + " AS " + tfect_valor
                            + " FROM E_TFECT WHERE " + tfect_mun + " = " + codMunicipio + " AND " + tfect_pro + "='"
                            + codProcedimiento + "' AND " + tfect_eje + " = " + ejercicio + " AND " + tfect_num + " ='"
                            + numero + "' AND " + tfect_tra + "=" + codTramite + " AND " + tfect_ocu + "=" + ocurrencia;
                } else {
                    sql = "SELECT " + tfect_cod + "," + oad.convertir(tfect_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, mascara)
                            + " AS " + tfect_valor
                            + " FROM E_TFECT WHERE " + tfect_mun + " = " + codMunicipio + " AND " + tfect_pro + "='"
                            + codProcedimiento + "' AND " + tfect_eje + " = " + ejercicio + " AND " + tfect_num + " ='"
                            + numero + "' AND " + tfect_tra + "=" + codTramite + " AND " + tfect_ocu + "=" + ocurrencia;
                }
            } else {
                if (mascara == null || "".equals(mascara)) {
                    sql = "SELECT " + tfect_cod + "," + oad.convertir(tfect_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
                            + " AS " + tfect_valor
                            + " FROM HIST_E_TFECT WHERE " + tfect_mun + " = " + codMunicipio + " AND " + tfect_pro + "='"
                            + codProcedimiento + "' AND " + tfect_eje + " = " + ejercicio + " AND " + tfect_num + " ='"
                            + numero + "' AND " + tfect_tra + "=" + codTramite + " AND " + tfect_ocu + "=" + ocurrencia;
                } else {
                    sql = "SELECT " + tfect_cod + "," + oad.convertir(tfect_valor, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, mascara)
                            + " AS " + tfect_valor
                            + " FROM HIST_E_TFECT WHERE " + tfect_mun + " = " + codMunicipio + " AND " + tfect_pro + "='"
                            + codProcedimiento + "' AND " + tfect_eje + " = " + ejercicio + " AND " + tfect_num + " ='"
                            + numero + "' AND " + tfect_tra + "=" + codTramite + " AND " + tfect_ocu + "=" + ocurrencia;
                }
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String codCampo = rs.getString(tfect_cod);
                String valorCampo = rs.getString(tfect_valor);
                campos.put(codCampo, valorCampo);
                campos.put("T" + codTramite + codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cF;
    }

     public CamposFormulario getValoresFechaTramite(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String mascara,
            String codTramite, String ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";
        String prefijoCampo = "T_" + codTramite + "_" + ocurrencia + "_";

        try {
            
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                new String[]{"'T'", "'" + codTramite + "'", "TFET_COD"}) + " AS TFET_COD, ";
            
            if (mascara == null || "".equals(mascara)) 
                 sql += oad.convertir("TFET_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_VALOR";
            else {
                 sql += "," + oad.convertir("TFET_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS TFET_VALOR";
            }

            sql += ", " + oad.convertir("TFET_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + 
                    " AS TFET_FEC_VENCIMIENTO,PLAZO_ACTIVADO ";
            
            if (expHistorico)
                sql += " FROM HIST_E_TFET";
            else
                sql += " FROM E_TFET";
            
            sql += " WHERE tfet_mun =? AND tfet_pro =? AND tfet_eje=? AND tfet_num =? AND tfet_tra=? AND " + 
                    "tfet_ocu =?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i =1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(ocurrencia));
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TFET_COD");
                String valorCampo = rs.getString("TFET_VALOR");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
                //fecha Vencimiento
                String codFechVencimiento = "fechaVencimiento" + codCampo;
                String valorFechaVencimiento = rs.getString("TFET_FEC_VENCIMIENTO");
                campos.put(codFechVencimiento, valorFechaVencimiento);
                //plazo activo
                String codPlazoActivo = "activar" + prefijoCampo + codCampo;
                String valorPlazoActivo = rs.getString(String.valueOf("PLAZO_ACTIVADO"));
                campos.put(codPlazoActivo, valorPlazoActivo);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValoresFechaTramiteCal(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String mascara,
            String codTramite, String ocurrencia) {
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";

        try {
            st = con.createStatement();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", "tfect_cod"}) + " AS tfect_cod, ";
            
            if (mascara == null || "".equals(mascara)) {
                sql += oad.convertir("tfect_valor", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS tfect_valor ";
            } else {
                sql += oad.convertir("tfect_valor", oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS tfect_valor ";
            }

            if (expHistorico)
                sql += "FROM HIST_E_TFECT ";
            else
                sql += "FROM E_TFECT ";
                    
            sql += " WHERE tfect_mun = " + codMunicipio + " AND tfect_pro='" + codProcedimiento + 
                    "' AND tfect_eje = " + ejercicio + " AND tfect_num ='" + numero + "' AND tfect_tra =" +
                    codTramite + " AND tfect_ocu =" + ocurrencia;

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tfect_cod");
                String valorCampo = rs.getString("tfect_valor");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }


    public CamposFormulario getValoresDesplegable(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

           boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql;
            
            if (expHistorico)
                sql = "SELECT tde_cod,tde_valor FROM HIST_E_TDE WHERE tde_mun=? AND tde_eje =? AND tde_num =?";
            else
                sql = "SELECT tde_cod,tde_valor FROM E_TDE WHERE tde_mun=? AND tde_eje =? AND tde_num =?";
            
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tde_cod");
                String valorCampo = rs.getString("tde_valor");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    public CamposFormulario getValsDespEtiquetas(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO) {

        PreparedStatement ps;
        ResultSet rs;
        HashMap campos = new HashMap();
        CamposFormulario cF;

        String sqlQuery = "SELECT " + tde_cod + ", VALOR." + desv_nom + " "
                + "FROM E_TDE, E_PCA, E_EXP, E_DES CAMPO, E_DES_VAL VALOR "
                + "WHERE " + tde_mun + " = " + exp_mun + " AND " + tde_num + " = " + exp_num + " " + // Condiciones de JOIN.
                "AND " + exp_mun + " = " + pca_mun + " AND " + exp_pro + " = " + pca_pro + " "
                + "AND " + tde_cod + " = " + pca_cod + " AND " + pca_des + " = CAMPO." + des_cod + " "
                + "AND CAMPO." + des_cod + " = VALOR." + desv_cod + " AND " + tde_valor + " = VALOR." + desv_val_cod + " "
                + "AND " + tde_eje + " = ? AND " + tde_num + " = ? AND " + tde_mun + " = ?";


        try {
            ps = con.prepareStatement(sqlQuery);
            int i = 1;
            ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("ejercicio")));
            ps.setString(i++, (String) gVO.getAtributo("numero"));
            ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("CONSULTA PARA OBTENER LOS VALORES DE LOS CAMPOS DESPLEGABLES "
                        + "PARA LAS ETIQUETAS DE LOS DOCUMENTOS");
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sqlQuery);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                String codCampo = rs.getString(tde_cod);
                String valorCampo = rs.getString(desv_nom);
                campos.put(codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);
            rs.close();
            ps.close();

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return cF;
    }

    public CamposFormulario getValoresDesplegableTramite(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {
        Statement st;
        ResultSet rs;
        HashMap<String, String> campos = new HashMap<String, String>();
        CamposFormulario cF = null;

        try {
            st = con.createStatement();

            String codMunicipio = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrencia = tEVO.getOcurrenciaTramite();
            String sql = "";
            
            if(!tEVO.isExpHistorico()) {
                sql = "SELECT " + tdet_cod + "," + tdet_valor + " FROM E_TDET WHERE " + tdet_mun + " = "
                    + codMunicipio + " AND " + tdet_pro + "='" + codProcedimiento + "' AND " + tdet_eje
                    + " = " + ejercicio + " AND " + tdet_num + " ='" + numero + "' AND " + tdet_tra + "="
                    + codTramite + " AND " + tdet_ocu + "=" + ocurrencia;
            } else {
                sql = "SELECT " + tdet_cod + "," + tdet_valor + " FROM HIST_E_TDET WHERE " + tdet_mun + " = "
                    + codMunicipio + " AND " + tdet_pro + "='" + codProcedimiento + "' AND " + tdet_eje
                    + " = " + ejercicio + " AND " + tdet_num + " ='" + numero + "' AND " + tdet_tra + "="
                    + codTramite + " AND " + tdet_ocu + "=" + ocurrencia;                
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String codCampo = rs.getString(tdet_cod);
                String valorCampo = rs.getString(tdet_valor);
                campos.put(codCampo, valorCampo);
                campos.put("T" + codTramite + codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);

            rs.close();
            st.close();
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        }
        return cF;
    }

    public CamposFormulario getValoresDesplegableTramite(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String codTramite,
            String ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

             boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "tdet_cod"}) + 
                    " AS tdet_cod, tdet_valor ";
            
            if (expHistorico)
                sql += "FROM HIST_E_TDET ";
            else
                sql += "FROM E_TDET ";
            
            sql += " WHERE tdet_mun =? AND tdet_pro =? AND tdet_eje =? AND tdet_num =? AND tdet_tra =? AND tdet_ocu =?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(ocurrencia));
            
            rs = ps.executeQuery();
                        
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tdet_cod");
                String valorCampo = rs.getString("tdet_valor");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }

    public CamposFormulario getValsDespTramiteEtiquetas(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject tEVO) {

        PreparedStatement ps;
        ResultSet rs;
        HashMap<String, String> campos = new HashMap<String, String>();
        CamposFormulario cF;

        String sqlQuery = "SELECT " + tdet_cod + ", VALOR." + desv_nom + " "
                + "FROM E_TDET, E_TCA, E_DES CAMPO, E_DES_VAL VALOR "
                + "WHERE " + tdet_mun + " = " + tca_mun + " AND " + tdet_pro + " = " + tca_pro + " " + // Condiciones de JOIN.
                "AND " + tdet_tra + " = " + tca_tra + " AND " + tdet_cod + " = " + tca_cod + " "
                + "AND " + tca_des + " = CAMPO." + des_cod + " AND CAMPO." + des_cod + " = VALOR." + desv_cod + " "
                + "AND " + tdet_valor + " = VALOR." + desv_val_cod + " "
                + "AND " + tdet_pro + " = ? AND " + tdet_eje + " = ? AND " + tdet_num + " = ? "
                + "AND " + tdet_tra + " = ? AND " + tdet_ocu + " = ? AND " + tdet_mun + " = ?";

        try {
            ps = con.prepareStatement(sqlQuery);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("CONSULTA PARA OBTENER LOS VALORES DE LOS CAMPOS DESPLEGABLES "
                        + "PARA LAS ETIQUETAS DE LOS DOCUMENTOS");
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sqlQuery);
            }

            int i = 1;
            ps.setString(i++, tEVO.getCodProcedimiento());
            ps.setInt(i++, Integer.parseInt(tEVO.getEjercicio()));
            ps.setString(i++, tEVO.getNumeroExpediente());
            ps.setInt(i++, Integer.parseInt(tEVO.getCodTramite()));
            ps.setInt(i++, Integer.parseInt(tEVO.getOcurrenciaTramite()));
            ps.setInt(i, Integer.parseInt(tEVO.getCodMunicipio()));

            rs = ps.executeQuery();
            while (rs.next()) {
                String codCampo = rs.getString(tdet_cod);
                String valorCampo = rs.getString(desv_nom);
                campos.put(codCampo, valorCampo);
                campos.put("T" + tEVO.getCodTramite() + codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);
            rs.close();
            ps.close();
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        }

        return cF;
    }

    public CamposFormulario getValsDespTramiteEtiquetas(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO,
            String codTramite, String ocurrencia) {
        PreparedStatement ps=null;
        ResultSet rs=null;
        HashMap campos = new HashMap();
        CamposFormulario cF;

        String codigoDesplegable = oad.funcionCadena(
                AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                new String[]{"'T'", "'" + codTramite + "'", tdet_cod}) + " AS " + tdet_cod;

        String sqlQuery = "SELECT " + codigoDesplegable + ", VALOR." + desv_nom + " "
                + "FROM E_TDET, E_TCA, E_DES CAMPO, E_DES_VAL VALOR "
                + "WHERE " + tdet_mun + " = " + tca_mun + " AND " + tdet_pro + " = " + tca_pro + " " + // Condiciones de JOIN.
                "AND " + tdet_tra + " = " + tca_tra + " AND " + tdet_cod + " = " + tca_cod + " "
                + "AND " + tca_des + " = CAMPO." + des_cod + " AND CAMPO." + des_cod + " = VALOR." + desv_cod + " "
                + "AND " + tdet_valor + " = VALOR." + desv_val_cod + " "
                + "AND " + tdet_pro + " = ? AND " + tdet_eje + " = ? AND " + tdet_num + " = ? "
                + "AND " + tdet_tra + " = ? AND " + tdet_ocu + " = ? AND " + tdet_mun + " = ?";

        try {
            ps = con.prepareStatement(sqlQuery);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("CONSULTA PARA OBTENER LOS VALORES DE LOS CAMPOS DESPLEGABLES "
                        + "PARA LAS ETIQUETAS DE LOS DOCUMENTOS");
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sqlQuery);
            }

            int i = 1;
            ps.setString(i++, (String) gVO.getAtributo("codProcedimiento"));
            ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("ejercicio")));
            ps.setString(i++, (String) gVO.getAtributo("numero"));
            ps.setInt(i++, Integer.parseInt(codTramite));
            ps.setInt(i++, Integer.parseInt(ocurrencia));
            ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));

            rs = ps.executeQuery();
            while (rs.next()) {
                String codCampo = rs.getString(tdet_cod);
                String valorCampo = rs.getString(desv_nom);
                campos.put(codCampo, valorCampo);
            }

            cF = new CamposFormulario(campos);
            rs.close();
            ps.close();
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
        } finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
                
            }catch(SQLException e){}
        }

        return cF;
    }
    
    public int grabarDatosSuplementarios(Vector estructuraDatosSuplementarios, Vector valoresDatosSuplementarios, String[] params)
            throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        String sql = null;
        int res = 0;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            res = grabarDatosSuplementariosConsultas(estructuraDatosSuplementarios, valoresDatosSuplementarios, params, oad, con);

        } catch (Exception e) {
            try {
                oad.rollBack(con);
            } catch (BDException ex) {
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("BDException haciendo rollback.");
                }
            }
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                oad.finTransaccion(con);
                oad.devolverConexion(con);
            } catch (BDException bde) {
                bde.getMensaje();
            }
            return res;
        }
    }

    public int grabarDatosSuplementariosConsultas(Vector estructuraDatosSuplementarios, Vector valoresDatosSuplementarios, String[] params, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        String sql = null;
        int res = 0;

        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("LG DATOS SUPLEMENTARIOS ..................... " + estructuraDatosSuplementarios.size());
            }
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                m_Log.debug("I ... " + i);
                res = 0;
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
                String codTramite = eC.getCodTramite();
                String codTipoDato = eC.getCodTipoDato();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);
                if ("1".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoNumerico(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("2".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoTexto(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("3".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoFecha(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("4".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoTextoLargo(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("5".equals(codTipoDato)) {
                    res=1;
                    
                } else if ("6".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                       
                        res = setDatoDesplegable(oad, con, eC, gVO);
                       
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("8".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoNumericoCal(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("9".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoFechaCal(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("10".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoDesplegableExt(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                }
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
            }
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } finally{
           return res;
        }

    }

    
    
      public int grabarDatosSuplementariosConsultasFichero(Vector estructuraDatosSuplementarios, Vector valoresDatosSuplementarios, String[] params, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        String sql = null;
        int res = 0;

        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("LG DATOS SUPLEMENTARIOS ..................... " + estructuraDatosSuplementarios.size());
            }
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                m_Log.debug("I ... " + i);
                res = 0;
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
                String codTramite = eC.getCodTramite();
                String codTipoDato = eC.getCodTipoDato();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);
               if ("5".equals(codTipoDato)) {
                    
                    
                    if (codTramite == null || "".equals(codTramite)) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(".....ha llegado el fichero");
                        }
                                                
                        
                        try{
                            String estado = (String)gVO.getAtributo(eC.getCodCampo() + "_ESTADO");
                            m_Log.debug("Estado documento: " + estado);
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){
                            
                                if(!setDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(),gVO,con)){
                                    break;
                                }else
                                    res = 1;

                                //res = setDatoFichero(oad, con, eC, gVO);
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug(".....ha pasado el fichero");
                                }
                            }//else res=1;
                            else
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO){
                                if(!eliminarDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(), gVO, con)){
                                    break;
                                }else
                                    res = 1;
                                
                            }else res=1;
                       
                        }catch(AlmacenDocumentoTramitacionException e){                           
                            throw e;                            
                        }
                    }else res=1;
                    
                } else  res =1;
                
            }
            
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } finally{
           return res;
        }

    }
      
    public int grabarDatosSuplementariosTramite(Vector estructuraDatosSuplementarios, GeneralValueObject gVO,
            String[] params, TramitacionExpedientesValueObject tEVO, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        int res = 0;

        try {

            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                res = 0;
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                m_Log.debug("******** grabarDatosSuplementariosTramite");
                m_Log.debug("GRABAMOS EL DATO DE TIPO: " + codTipoDato + " CON CODIGO: " + eC.getCodCampo());

                if ("1".equals(codTipoDato)) {
                    res = setDatoNumericoTramite(oad, con, eC, gVO, tEVO);
                    m_Log.debug("******** grabarDatosSuplementariosTramite después de insertar datos numérico: " + con);
                    if (res < 1) {
                        break;
                    }
                } else if ("2".equals(codTipoDato)) {
                    res = setDatoTextoTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("3".equals(codTipoDato)) {
                    res = setDatoFechaTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("4".equals(codTipoDato)) {
                    res = setDatoTextoLargoTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("5".equals(codTipoDato)) {                   
                  
                    //Los campos tipo fichero se guardan en grabarDatosSuplementariosFicheroTramite
                    res=1;
                  
                } else if ("6".equals(codTipoDato)) {
                    res = setDatoDesplegableTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("7".equals(codTipoDato)) {
                    res = setDatoDesplegableTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("8".equals(codTipoDato)) {
                    res = setDatoNumericoTramiteCal(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("9".equals(codTipoDato)) {
                    res = setDatoFechaTramiteCal(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                }else if ("10".equals(codTipoDato)) {
                    res = setDatoDesplegableExtTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        }finally{
           return res;        
        }

    }
    
    
    public int grabarDatosSuplementariosFichero(Vector estructuraDatosSuplementarios, Vector valoresDatosSuplementarios,
            String[] params, TramitacionExpedientesValueObject tEVO, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        int res = 1;

        try {
             for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                m_Log.debug("I ... " + i);
                res = 0;
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
                String codTramite = eC.getCodTramite();
                String codTipoDato = eC.getCodTipoDato();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);
                
                if ("5".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(".....ha llegado el fichero");
                        }
                                                
                        
                        try{
                            String estado = (String)gVO.getAtributo(eC.getCodCampo() + "_ESTADO");
                            m_Log.debug("Estado documento: " + estado);
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){
                            
                                if(!setDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(),gVO,con,params)){
                                    break;
                                }else
                                    res = 1;

                                //res = setDatoFichero(oad, con, eC, gVO);
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug(".....ha pasado el fichero");
                                }
                            }//else res=1;
                            else
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO){
                                if(!eliminarDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(), gVO, con)){
                                    break;
                                }else
                                    res = 1;
                                
                            }else res=1;
                                 
                          
                        }catch(AlmacenDocumentoTramitacionException e){                           
                            throw e;                            
                        }
                    }else res=1;                  
                } else
                    res = 1;
            }
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        }finally{
           return res;        
        }

    }
    
    
     public int grabarDatosSuplementariosFicheroTramite(Vector estructuraDatosSuplementarios, Vector valoresDatosSuplementarios,
            String[] params, TramitacionExpedientesValueObject tEVO, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        int res = 1;

        try {
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                res = 0;
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                GeneralValueObject gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);
                        
                String codTipoDato = eC.getCodTipoDato();
                m_Log.debug("******** grabarDatosSuplementariosFicheroTramite");
                m_Log.debug("GRABAMOS EL DATO DE TIPO: " + codTipoDato + " CON CODIGO: " + eC.getCodCampo());
                
                if ("5".equals(codTipoDato)) {
                    try{
                        Integer estado = (Integer)gVO.getAtributo(eC.getCodCampo() + "_ESTADO");
                        m_Log.debug("Estado documento: " + estado);
                        if(estado!=null && estado.intValue()==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){
                            if(!setDatoFicheroCampoSuplementarioTramitePluginDocumentos(eC.getCodCampo(),gVO,tEVO,con, params))
                                break;
                            else
                                res = 1;                                

                            if (m_Log.isDebugEnabled()) 
                                m_Log.debug(".....ha pasado el fichero");
                        } else if (estado != null && estado.intValue() == ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO){                         
                            if(!eliminarDatoFicheroCampoSuplementarioTramitePluginDocumentos(eC.getCodCampo(), gVO,tEVO, con))
                                break;
                            else
                                res = 1;
                        }else 
                            res=1;
                        
                    }catch(AlmacenDocumentoTramitacionException e){                           
                        throw e;                            
                    }                    
                } else
                    res = 1;
            }
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        }finally{
           return res;        
        }

    }

    /**
     * Este método hace lo mismo que grabarDatosSuplementariosTramite pero con
     * la diferencia de que no inicia una transacción para grabar los datos
     * suplementarios del trámite. Se debe utilizar cuando se desee guardar los
     * datos suplementarios del trámite desde dentro de otra operación que ya es
     * la que inicia la transacción. El uso de transacciones anidadas en SQL
     * SERVER produce fallos.
     *
     * @param estructuraDatosSuplementarios: Estructura con los datos
     * suplementarios
     * @param valoresDatosSuplementarios: Valores de los datos suplementarios
     * @param con: Conexión a la base de datos
     * @param tEVO:
     * @param oad
     * @return
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.util.conexion.BDException
     */
    public int grabarDatosSuplementariosTramite2(Vector estructuraDatosSuplementarios, GeneralValueObject gVO,
            Connection con, TramitacionExpedientesValueObject tEVO, AdaptadorSQLBD oad)
            throws TechnicalException, BDException {

        int res = 0;

        try {
            m_Log.debug("**** grabarDatosSuplementariosTramite2");
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                res = 0;
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                m_Log.debug("GRABAMOS EL DATO DE TIPO: " + codTipoDato + " CON CODIGO: " + eC.getCodCampo() + " Y EL VALOR: " + gVO.getAtributo(eC.getCodCampo()));
                if ("1".equals(codTipoDato)) {
                    res = setDatoNumericoTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("2".equals(codTipoDato)) {
                    res = setDatoTextoTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("3".equals(codTipoDato)) {
                    res = setDatoFechaTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("4".equals(codTipoDato)) {
                    res = setDatoTextoLargoTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("5".equals(codTipoDato)) {
                    //Si el dato suplementario es de tipo fichero devolvemos un resultado correcto 
                    //porque los ficheros se insertan desde un método del manager.           
                    
                    
                    /********************************************/
                     //Si el dato suplementario es de tipo fichero devolvemos un resultado correcto 
                    //porque los ficheros se insertan desde un método del manager.                    
                    
                    try{
                        Integer estado = (Integer)gVO.getAtributo(eC.getCodCampo() + "_ESTADO");
                        m_Log.debug("Estado documento: " + estado);
                        if(estado!=null && estado.intValue()==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){

                            if(!setDatoFicheroCampoSuplementarioTramitePluginDocumentos(eC.getCodCampo(),gVO,tEVO,con)){
                                break;
                            }else{
                                res = 1;                                
                            }

                            //res = setDatoFichero(oad, con, eC, gVO);
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug(".....ha pasado el fichero");
                            }
                        }
                        else
                        if(estado!=null && estado.intValue()==ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO){                            
                            if(!eliminarDatoFicheroCampoSuplementarioTramitePluginDocumentos(eC.getCodCampo(), gVO,tEVO, con)){
                                break;
                            }else
                                res = 1;

                        }else res=1;


                    }catch(AlmacenDocumentoTramitacionException e){                           
                        throw e;                            
                    }                                        
                    
                    /*********************************************/
                    //res = 1;
                    
                } else if ("6".equals(codTipoDato)) {
                    res = setDatoDesplegableTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }                
                } else if ("8".equals(codTipoDato)) {
                    res = setDatoNumericoTramiteCal(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                } else if ("9".equals(codTipoDato)) {
                    res = setDatoFechaTramiteCal(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                }else if ("10".equals(codTipoDato)) {
                    res = setDatoDesplegableExtTramite(oad, con, eC, gVO, tEVO);
                    if (res < 1) {
                        break;
                    }
                }
            }
        } catch (SQLException sqle) { 
            res = 0;
            sqle.printStackTrace();            
            throw new BDException(sqle.getMessage());
        }finally{
            return res;
        }
    }

       public int grabarDocumentosDatosSuplementarios(Connection con, Vector docs, String[] params) throws TechnicalException{

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 1;

        try {
        
            for (int i=0;i<docs.size();i++){
                DocumentoBBDD doc = (DocumentoBBDD) docs.get(i);
                
                String codMunicipio = String.valueOf(doc.getCodMunicipio());
                String ejercicio = String.valueOf(doc.getEjercicio());
                String numeroExpediente = doc.getNumeroExpediente();
                byte[] valorDato = doc.getFichero();
                String nombre = doc.getNombreDocumento();
                String mime = doc.getTipoMimeContenido();
                String codTipoDato = doc.getCodTipoDato();

		        sql = "DELETE FROM E_TFI WHERE " + tfi_mun + "=" + codMunicipio + " AND " + tfi_eje + "="
                        + ejercicio + " AND " + tfi_num + "='" + numeroExpediente + "' AND " + tfi_cod + "='" + codTipoDato + "'";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }

                stmt = con.createStatement();
                stmt.executeUpdate(sql);
                if (valorDato != null) {
                    if (!nombre.equals("")) {
                        sql = "INSERT INTO E_TFI ( " + tfi_mun + "," + tfi_eje + "," + tfi_num + "," + tfi_cod + ","
                                + tfi_valor + "," + tfi_mime + "," + tfi_nom_fich + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" 
                                + numeroExpediente + "','" + codTipoDato  + "',?,'" + mime + "','" + nombre + "')";
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        stmt.close();
                        PreparedStatement ps = con.prepareStatement(sql);
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(valorDato);
                        }

                        InputStream st = new ByteArrayInputStream(valorDato);
                        ps.setBinaryStream(1, st, valorDato.length);
                        resultadoInsertar = ps.executeUpdate();
                        ps.close();
                    } else {
                        resultadoInsertar = 1;
                    }
                }
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
       
       
       
    
     
    
    public boolean grabarDocumentoDatoSuplementarioTramite(Documento doc,Connection con) throws TechnicalException{

        boolean exito = false;
        Statement stmt = null;
        String sql = null;     
        PreparedStatement ps = null;
        DocumentoDAO documentoDAO = DocumentoDAO.getInstance();
        
        
        try {
            
            String codMunicipio = String.valueOf(doc.getCodMunicipio());
            String codProcedimiento = doc.getCodProcedimiento();
            String ejercicio = String.valueOf(doc.getEjercicio());
            String numeroExpediente = doc.getNumeroExpediente();
            String codTramite = String.valueOf(doc.getCodTramite());
            String ocurrencia = String.valueOf(doc.getOcurrenciaTramite());
            String codDato = doc.getCodTipoDato();
            String nombre = doc.getNombreDocumento();
            String mime = doc.getTipoMimeContenido();
            int longitudDocumento = doc.getLongitudDocumento();
            byte[] valorDato = doc.getFichero();                

            String[] params = doc.getParams();
            // Metadatos del documento
            MetadatosDocumentoVO metadatos = null;
            Long idMetadatosNuevo = null;
            Boolean insertarMetadatoEnBBDD = doc.isInsertarMetadatosEnBBDD();
            
            // Nos quedamos con el id de los metadatos para poder borrarlo
            Long idMetadatoAntiguo = getDocumentoIdMetadatoTramite(
                    codDato, NumberUtils.createInteger(ejercicio), NumberUtils.createInteger(codMunicipio),
                    numeroExpediente, NumberUtils.createInteger(codTramite),
                    NumberUtils.createInteger(ocurrencia), con);
            

            sql = "DELETE FROM E_TFIT WHERE " + tfit_mun + "=" + codMunicipio + " AND " + tfit_pro + "='"
                    + codProcedimiento + "' AND " + tfit_eje + "=" + ejercicio + " AND " + tfit_num + "='"
                    + numeroExpediente + "' AND " + tfit_tra + "=" + codTramite + " AND " + tfit_ocu + "="
                    + ocurrencia + " AND " + tfit_cod + "='" + codDato + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

             // Borrar metadatos
            if (idMetadatoAntiguo != null) {
                documentoDAO.eliminarMetadato(idMetadatoAntiguo, con);
            }

            

            if (!nombre.equals("")) {
                // Insertamos los metadatos
                if (Boolean.TRUE.equals(insertarMetadatoEnBBDD)) {
                    metadatos = new MetadatosDocumentoVO();
                    metadatos.setCsv(doc.getMetadatoDocumentoCsv());
                    metadatos.setCsvAplicacion(doc.getMetadatoDocumentoCsvAplicacion());
                    metadatos.setCsvUri(doc.getMetadatoDocumentoCsvUri());
                    idMetadatosNuevo = documentoDAO.insertarMetadatoCSV(metadatos, con, params);
                }
                
                sql = "INSERT INTO E_TFIT ( " + tfit_mun + "," + tfit_pro + "," + tfit_eje + "," + tfit_num + ","
                        + tfit_tra + "," + tfit_ocu + "," + tfit_cod + "," + tfit_valor + "," + tfit_mime + "," + tfit_nom_fich + ",TFIT_TAMANHO, TFIT_ID_METADATO) VALUES (" + codMunicipio
                        + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                        + "," + ocurrencia + ",'" + codDato + "',?,'" + mime + "','" + nombre + "'," + longitudDocumento + ", " + idMetadatosNuevo + ")";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                
                ps = con.prepareStatement(sql);
                if(valorDato!=null && valorDato.length>0){
                    // Creamos un stream de lectura a partir del buffer
                    InputStream st = new ByteArrayInputStream(valorDato);
                    ps.setBinaryStream(1, st, valorDato.length);
                   
                }else
                    ps.setNull(1,java.sql.Types.BINARY);

                int rowsInserted = ps.executeUpdate();
                if(rowsInserted==1) exito = true;
                ps.close();

            } else {
                exito = true;
            }
            
           
        } catch (Exception e) {
            exito = false;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}
        } finally {
            try {
                
                if(stmt != null) stmt.close();
                if(ps!=null) ps.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exito;
    }       
    
       
    
   public int setDatoFecha(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String plazoFecha = eC.getPlazoFecha();
        String checkFechaPlazo = eC.getCheckPlazoFecha();
        String fechaVencimiento = null;
        String campoActivo = (String) gVO.getAtributo("activar" + eC.getCodCampo());
        String mascara = eC.getMascara();
        if (mascara == null || "".equals(mascara))
            mascara = "DD/MM/YYYY";
        
        try {
            sql = "DELETE FROM E_TFE WHERE " + tfe_mun + "=" + codMunicipio + " AND " + tfe_eje + "="
                    + ejercicio + " AND " + tfe_num + "='" + numeroExpediente + "' AND " + tfe_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if (valorDato != null && !"".equals(valorDato)) {
                //Si el campo de tipo fecha tiene un plazo definido
                if (plazoFecha != null) {
                    if (campoActivo.equals("desactivada")) {
                        sql = "INSERT INTO E_TFE ( " + tfe_mun + "," + tfe_eje + "," + tfe_num + "," + tfe_cod + ","
                                + tfe_valor + ",TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO) VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                                + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + "," + null + "," + 0 + ")";
                    } else {
                        //Se calcula la fecha de vencimiento
                        fechaVencimiento = calcularFechaVencimiento(valorDato, plazoFecha, checkFechaPlazo);
                        sql = "INSERT INTO E_TFE ( " + tfe_mun + "," + tfe_eje + "," + tfe_num + "," + tfe_cod + ","
                                + tfe_valor + ",TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO) VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                                + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + "," + oad.convertir("'" + fechaVencimiento + "'", oad.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "," + 1 + ")";
                    }
                } else { //no tiene definido plazo para fecha vencimiento
                    sql = "INSERT INTO E_TFE ( " + tfe_mun + "," + tfe_eje + "," + tfe_num + "," + tfe_cod + ","
                            + tfe_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                            + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + ")";
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                
            } else {
                resultadoInsertar = 1;
            }
            if (plazoFecha != null) {
                AlarmaExpedienteVO alarma = new AlarmaExpedienteVO(Integer.parseInt(codMunicipio),
                        Integer.parseInt(ejercicio),numeroExpediente,0,0,codDato,
                        fechaVencimiento!=null?DateOperations.toCalendar(fechaVencimiento, "dd/MM/yyyy"):null);
                AlarmasExpedienteDAO.getInstance().grabarAlarmaExpediente(alarma,con);
            }
        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }

    public int setDatoFechaCal(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String mascara = eC.getMascara();

        try {
            sql = "DELETE FROM E_TFEC WHERE " + tfec_mun + "=" + codMunicipio + " AND " + tfec_eje + "="
                    + ejercicio + " AND " + tfec_num + "='" + numeroExpediente + "' AND " + tfec_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if (valorDato != null && !"".equals(valorDato)) {
                if (mascara == null || "".equals(mascara)) {
                    sql = "INSERT INTO E_TFEC ( " + tfec_mun + "," + tfec_eje + "," + tfec_num + "," + tfec_cod + ","
                            + tfec_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                            + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")";
                } else {
                    sql = "INSERT INTO E_TFEC ( " + tfec_mun + "," + tfec_eje + "," + tfec_num + "," + tfec_cod + ","
                            + tfec_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                            + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + ")";
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }
        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }

    
    
    public int setDatoFichero(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        PreparedStatement ps = null;
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 1;
        byte[] valorDato = null;
        String mime = "";
        String nombre = "";
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        if (!gVO.getAtributo(eC.getCodCampo()).equals("")) {
            valorDato = (byte[]) gVO.getAtributo(eC.getCodCampo());
            nombre = (String) gVO.getAtributo(eC.getCodCampo() + "_NOMBRE");
            mime = (String) gVO.getAtributo(eC.getCodCampo() + "_TIPO");
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("NOMBRE FICHERO ....." + nombre);
            }
        }
        //String mascara = eC.getMascara();

        try {
            sql = "DELETE FROM E_TFI WHERE " + tfi_mun + "=" + codMunicipio + " AND " + tfi_eje + "="
                    + ejercicio + " AND " + tfi_num + "='" + numeroExpediente + "' AND " + tfi_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            if (valorDato != null) {
                if (!nombre.equals("")) {
                    sql = "INSERT INTO E_TFI ( " + tfi_mun + "," + tfi_eje + "," + tfi_num + "," + tfi_cod + ","
                            + tfi_valor + "," + tfi_mime + "," + tfi_nom_fich + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                            + codDato + "',?,'" + mime + "','" + nombre + "')";
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(sql);
                    }
                    stmt.close();
                    
                    ps = con.prepareStatement(sql);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(valorDato);
                    }

                    InputStream st = new ByteArrayInputStream(valorDato);
                    ps.setBinaryStream(1, st, valorDato.length);
                    resultadoInsertar = ps.executeUpdate();
                    ps.close();
                } else {
                    resultadoInsertar = 1;
                }

            }
        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                
                if (stmt!= null) stmt.close();
                if(ps!=null) ps.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
    
        /**
     *  Esta operaci\F3n es llamada para almacenar un archivo en un campo suplementario de tipo fichero a nivel
     * de expediente     
     * @param codCampo: Codigo del campo suplementario
     * @param gVO: Objeto de tipo GeneralValueObject con los datos del fichero a dar de alta
     * @param con: Conexi\F3n a la BBDD
     * @return 
     * @throws es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException 
     */
    public boolean setDatoFicheroCampoSuplementarioExpedientePluginDocumentos(String codCampo, GeneralValueObject gVO,Connection con)
            throws AlmacenDocumentoTramitacionException{
        return setDatoFicheroCampoSuplementarioExpedientePluginDocumentos(codCampo, gVO, con, null);
    }    
    
    
    
    /**
     *  Esta operación es llamada para almacenar un archivo en un campo suplementario de tipo fichero a nivel
     * de expediente     
     * @param codCampo: Codigo del campo suplementario
     * @param gVO: Objeto de tipo GeneralValueObject con los datos del fichero a dar de alta
     * @param con: Conexión a la BBDD
     * @return 
     */
    public boolean setDatoFicheroCampoSuplementarioExpedientePluginDocumentos(String codCampo, GeneralValueObject gVO,Connection con, String[] params) throws AlmacenDocumentoTramitacionException{

        boolean exito = false;        
        byte[] valorDato = null;
        String mime = "";
        String nombre = "";
        String codOrganizacion = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        
        
        String[] datosExp       = numeroExpediente.split("/");
        String codProcedimiento = datosExp[1];
        
        nombre = (String) gVO.getAtributo(codCampo + "_NOMBRE");
        mime = (String) gVO.getAtributo(codCampo + "_TIPO");            
        String estadoFichero = (String)gVO.getAtributo(codCampo + "_ESTADO");
        String rutaFichero = (String)gVO.getAtributo(codCampo + "_RUTA");
        MetadatosDocumentoVO metadatos = (MetadatosDocumentoVO)gVO.getAtributoONulo(codCampo + "_METADATOS");
               
        //Obtiene la implementacion del plugin correspondiente
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(String.valueOf(codOrganizacion));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
        ArrayList<Documento> docsReg = new ArrayList<Documento>();
        int tipoDocumento = -1;
        String descripcionOrganizacion = null;
        
        if(!almacen.isPluginGestor()){
            //Plugin BBDDD
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        }else{
            //Plugin Gestor Alfresco. Solo inserta los docuemntos en la BD de Flexia
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
        }
        
                
        try{
            if(estadoFichero!=null && Integer.parseInt(estadoFichero)==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){                 
            // Si se trata de un nuevo elemento, se almacena en la lista de documentos
            // a dar de alta, y se lee su contenido 
            
                byte[] fichero = null;                    
                try{
                    File f = new File(rutaFichero);
                    fichero = FileOperations.readFile(f);

                }catch(Exception e){
                    e.printStackTrace();
                }

                Hashtable<String,Object> datos = new Hashtable<String,Object>();                
                if (params != null) {
                    datos.put("params", params);
                }
                datos.put("fichero",fichero);
                datos.put("codMunicipio",codOrganizacion);
                datos.put("codOrganizacion",codOrganizacion);
                datos.put("nombreDocumento",nombre);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numeroExpediente);
                datos.put("tipoMime",mime);
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(mime));
                datos.put("longitudDocumento",fichero.length);
                datos.put("codTipoDato",codCampo);
                 putMetadatosDocumentoEnParametros(datos, metadatos);

                if(almacen.isPluginGestor()){
                    //  Si se trata de un plugin de un gestor documental, se pasa la información
                    // extra necesaria                                    
                    ResourceBundle config = ResourceBundle.getBundle("documentos");
                    String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);                    

                    String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
                    descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion), con);
                    
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                    listaCarpetas.add(numeroExpediente.replaceAll("/","-"));
                    datos.put("listaCarpetas",listaCarpetas);
                }

                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
                
                if(doc!=null){
                  
                    exito = almacen.setDocumentoDatoSuplementarioExpediente(doc,con);
                                        
                    // Se procede a eliminar los ficheros nuevos de la anotación que residen en el disco del servidor
                    // ya que se trata de un documento con estado NUEVO. 
                    if(rutaFichero!=null && !"".equals(rutaFichero)){
                        try{
                            FileOperations.deleteFile(rutaFichero);

                        }catch(Exception e){                        
                            m_Log.error("Error al borrar el documento situado en la ruta: " + rutaFichero + " correspondiente a un campo suplementario fichero " + codCampo + " del expediente: " + numeroExpediente);
                            e.printStackTrace();
                        }
                    }
                }                
            }        
        }catch(TechnicalException e){
            throw new AlmacenDocumentoTramitacionException(0,"Error al recuperar la descripción del procedimiento: " + e.getMessage());
        }
        catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            throw e;            
        }
        return exito;
    }
    
    
    
    
    public boolean setDatoFicheroCampoExpedientePlugin(String codOrganizacion, String ejercicio, String numeroExpediente,
            String codCampo, String valorDato, String nombre, String mime, Connection con) throws AlmacenDocumentoTramitacionException{

        boolean exito = false;        
      
       
        byte[] fichero = Base64.decode(valorDato);
        
       

        
        
        String[] datosExp       = numeroExpediente.split("/");
        String codProcedimiento = datosExp[1];
        
      
        //Obtiene la implementacion del plugin correspondiente
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(String.valueOf(codOrganizacion));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
        ArrayList<Documento> docsReg = new ArrayList<Documento>();
        int tipoDocumento = -1;
        String descripcionOrganizacion = null;
        
        if(!almacen.isPluginGestor()){
            //Plugin BBDDD
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        }else{
            //Plugin Gestor Alfresco. Solo inserta los docuemntos en la BD de Flexia
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
        }
        
                
        try{
                          
            // Si se trata de un nuevo elemento, se almacena en la lista de documentos
            // a dar de alta, y se lee su contenido 


                Hashtable<String,Object> datos = new Hashtable<String,Object>();                
                datos.put("fichero",fichero);
                datos.put("codMunicipio",codOrganizacion);
                datos.put("codOrganizacion",codOrganizacion);
                datos.put("nombreDocumento",nombre);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numeroExpediente);
                datos.put("tipoMime",mime);
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(mime));
                datos.put("longitudDocumento",fichero.length);
                datos.put("codTipoDato",codCampo);

                if(almacen.isPluginGestor()){
                    //  Si se trata de un plugin de un gestor documental, se pasa la información
                    // extra necesaria                                    
                    ResourceBundle config = ResourceBundle.getBundle("documentos");
                    String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);                    

                    String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
                    descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion), con);
                    
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                    listaCarpetas.add(numeroExpediente.replaceAll("/","-"));
                    datos.put("listaCarpetas",listaCarpetas);
                }

                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
                
                if(doc!=null){
                  
                    exito = almacen.setDocumentoDatoSuplementarioExpediente(doc,con);
                                        
                    
                }                
                   
        }catch(TechnicalException e){
            throw new AlmacenDocumentoTramitacionException(0,"Error al recuperar la descripción del procedimiento: " + e.getMessage());
        }
        catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            throw e;            
        }
        return exito;
    }
    /**
     *  Esta operación es llamada para eliminar un documento almacenado en un campo suplementario de tipo fichero
     * definido a nivel de expediente
     * de expediente     
     * @param codCampo: Codigo del campo suplementario
     * @param gVO: Objeto de tipo GeneralValueObject con los datos del fichero a eliminar
     * @param con: Conexión a la BBDD
     * @return 
     */
    public boolean eliminarDatoFicheroCampoSuplementarioExpedientePluginDocumentos(String codCampo, GeneralValueObject gVO, Connection con) throws AlmacenDocumentoTramitacionException{

        boolean exito = false;
        Statement stmt = null;                        
        String mime = "";
        String nombre = "";
        String codOrganizacion = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        
        
        String[] datosExp       = numeroExpediente.split("/");
        String codProcedimiento = datosExp[1];
        
        nombre = (String) gVO.getAtributo(codCampo + "_NOMBRE");
        mime = (String) gVO.getAtributo(codCampo + "_TIPO");            
        String estadoFichero = (String)gVO.getAtributo(codCampo + "_ESTADO");
        String rutaFichero = (String)gVO.getAtributo(codCampo + "_RUTA");
        
               
        //Obtiene la implementacion del plugin correspondiente
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(String.valueOf(codOrganizacion));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
        ArrayList<Documento> docsReg = new ArrayList<Documento>();
        int tipoDocumento = -1;
        String descripcionOrganizacion = null;
        
        if(!almacen.isPluginGestor()){
            //Plugin BBDDD
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        }else{
            //Plugin Gestor Alfresco
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
        }
        
                
        try{
            
            Hashtable<String,Object> datos = new Hashtable<String,Object>();                            
            datos.put("codMunicipio",codOrganizacion);
            datos.put("codOrganizacion",codOrganizacion);
            datos.put("nombreDocumento",nombre);
            datos.put("ejercicio",ejercicio);
            datos.put("numeroExpediente",numeroExpediente);
            datos.put("tipoMime",mime);
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(mime));            
            datos.put("codTipoDato",codCampo);
            datos.put("numeroDocumento",codCampo);
            datos.put("rutaDocumento",rutaFichero);

            if(almacen.isPluginGestor()){
                //  Si se trata de un plugin de un gestor documental, se pasa la información
                // extra necesaria                                    
                ResourceBundle config = ResourceBundle.getBundle("documentos");                
                String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);                    

                descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion),con);
                String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
                
                String nombreFichero = DatosSuplementariosDAO.getInstance().getNombreCampoSuplementarioFicheroExpediente(codOrganizacion,ejercicio,numeroExpediente,codCampo,con);                
                datos.put("nombreDocumento", codCampo + "_" + nombreFichero);
                datos.put("nombreFicheroCompleto",codCampo + "_" + nombreFichero);
                
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                listaCarpetas.add(numeroExpediente.replaceAll("/","-"));
                datos.put("listaCarpetas",listaCarpetas);
            }

            Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
                
            if(doc!=null){

                exito = almacen.eliminarDocumentoDatosSuplementarios(doc, con);
                if(exito){
                    
                    if(rutaFichero!=null && !"".equals(rutaFichero)){
                        try{
                            // Si el fichero ha sido subido con estado NUEVO y no ha sido eliminado de disco, se procede
                            // a su borrado
                            FileOperations.deleteFile(rutaFichero);

                        }catch(Exception e){
                            e.printStackTrace();
                            throw new AlmacenDocumentoTramitacionException(4,e.getMessage());
                        }
                    }
                }
            }                
                 
        }catch(TechnicalException e){
            throw new AlmacenDocumentoTramitacionException(0,"Error al recuperar la descripción del procedimiento: " + e.getMessage());
        }
        catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            throw e;            
        }
       
        return exito;
    }
    
    
    
  
    

    public int setDatoFicheroTramite(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO)
            throws SQLException {

        byte[] valorDato = null;
        String mime = "";
        String nombre = "";

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String codDato = eC.getCodCampo();
        if (!gVO.getAtributo(eC.getCodCampo()).equals("")) {
            valorDato = (byte[]) gVO.getAtributo(eC.getCodCampo());
            nombre = (String) gVO.getAtributo(eC.getCodCampo() + "_NOMBRE");
            mime = (String) gVO.getAtributo(eC.getCodCampo() + "_TIPO");
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("NOMBRE FICHERO ....." + nombre);
            }
        }

        String sql = "DELETE FROM E_TFIT WHERE " + tfit_mun + "=" + codMunicipio + " AND " + tfit_pro + "='"
                + codProcedimiento + "' AND " + tfit_eje + "=" + ejercicio + " AND " + tfit_num + "='"
                + numeroExpediente + "' AND " + tfit_tra + "=" + codTramite + " AND " + tfit_ocu + "="
                + ocurrencia + " AND " + tfit_cod + "='" + codDato + "'";

        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.executeUpdate();
        stmt.close();
        int resultadoInsertar = 0;
        if (valorDato != null) {

            if (!nombre.equals("")) {
                sql = "INSERT INTO E_TFIT ( " + tfit_mun + "," + tfit_pro + "," + tfit_eje + "," + tfit_num + ","
                        + tfit_tra + "," + tfit_ocu + "," + tfit_cod + "," + tfit_valor + "," + tfit_mime + "," + tfit_nom_fich + ") VALUES (" + codMunicipio
                        + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                        + "," + ocurrencia + ",'" + codDato + "',?,'" + mime + "','" + nombre + "')";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                PreparedStatement ps = con.prepareStatement(sql);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(valorDato);
                }

                // Creamos un stream de lectura a partir del buffer
                InputStream st = new ByteArrayInputStream(valorDato);
                ps.setBinaryStream(1, st, valorDato.length);
                resultadoInsertar = ps.executeUpdate();
                ps.close();
            } else {
                resultadoInsertar = 1;
            }
        } else {
            resultadoInsertar = 1;
        }
        return resultadoInsertar;
    }

   public int setDatoFechaTramite(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO) {

        Statement stmt = null;
        String sql = "";
        int resultadoInsertar = 0;
        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String prefijoCampo = "T_" + tEVO.getCodTramite() + "_" + tEVO.getOcurrenciaTramite() + "_";
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String plazoFecha = eC.getPlazoFecha();
        String checkFechaPlazo = eC.getCheckPlazoFecha();
        String fechaVencimiento = null;
        String campoActivo = (String) gVO.getAtributo("activar" + prefijoCampo + eC.getCodCampo());
        String mascara = eC.getMascara();
        if (mascara == null || "".equals(mascara))
            mascara = "DD/MM/YYYY";

        try {
            sql = "DELETE FROM E_TFET WHERE " + tfet_mun + "=" + codMunicipio + " AND " + tfet_pro + "='"
                    + codProcedimiento + "' AND " + tfet_eje + "=" + ejercicio + " AND " + tfet_num + "='"
                    + numeroExpediente + "' AND " + tfet_tra + "=" + codTramite + " AND " + tfet_ocu + "="
                    + ocurrencia + " AND " + tfet_cod + "='" + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            stmt.close();
            if (valorDato != null && !"".equals(valorDato)) {
                //Si el campo de tipo fecha tiene un plazo definido
                if (plazoFecha != null) {
                    if ("desactivada".equals(campoActivo)) {
                        sql = "INSERT INTO E_TFET ( " + tfet_mun + "," + tfet_pro + "," + tfet_eje + "," + tfet_num + ","
                                + tfet_tra + "," + tfet_ocu + "," + tfet_cod + "," + tfet_valor + ",TFET_FEC_VENCIMIENTO,PLAZO_ACTIVADO) VALUES (" + codMunicipio
                                + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                                + "," + ocurrencia + ",'" + codDato + "',"
                                + oad.convertir("'" + valorDato + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, mascara) + "," + null + "," + 0 + ")";

                    } else {
                        //se calcula la fecha de Vencimiento
                        fechaVencimiento = calcularFechaVencimiento(valorDato, plazoFecha, checkFechaPlazo);
                        if ("activada".equals(campoActivo)) {
                            sql = "INSERT INTO E_TFET ( " + tfet_mun + "," + tfet_pro + "," + tfet_eje + "," + tfet_num + ","
                                    + tfet_tra + "," + tfet_ocu + "," + tfet_cod + "," + tfet_valor + ",TFET_FEC_VENCIMIENTO,PLAZO_ACTIVADO) VALUES (" + codMunicipio
                                    + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                                    + "," + ocurrencia + ",'" + codDato + "',"
                                    + oad.convertir("'" + valorDato + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, mascara) + ","
                                    + oad.convertir("'" + fechaVencimiento + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "," + 1 + ")";
                        }
                    }
                } else { //no tiene definido plazo para fecha vencimiento
                    sql = "INSERT INTO E_TFET ( " + tfet_mun + "," + tfet_pro + "," + tfet_eje + "," + tfet_num + ","
                            + tfet_tra + "," + tfet_ocu + "," + tfet_cod + "," + tfet_valor + ") VALUES (" + codMunicipio
                            + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                            + "," + ocurrencia + ",'" + codDato + "',"
                            + oad.convertir("'" + valorDato + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, mascara) + ")";
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                stmt = con.createStatement();
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }
            if (plazoFecha!=null){
                AlarmaExpedienteVO alarma = new AlarmaExpedienteVO(Integer.parseInt(codMunicipio),
                        Integer.parseInt(ejercicio),numeroExpediente,Integer.parseInt(codTramite),
                        Integer.parseInt(ocurrencia),codDato,
                        fechaVencimiento!=null?DateOperations.toCalendar(fechaVencimiento, "dd/MM/yyyy"):null);
                AlarmasExpedienteDAO.getInstance().grabarAlarmaExpediente(alarma,con);
            }
        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return resultadoInsertar;
    }

    public int setDatoFechaTramiteCal(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO) throws SQLException {

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String mascara = eC.getMascara();

        String sql = "DELETE FROM E_TFECT WHERE " + tfect_mun + "=" + codMunicipio + " AND " + tfect_pro + "='"
                + codProcedimiento + "' AND " + tfect_eje + "=" + ejercicio + " AND " + tfect_num + "='"
                + numeroExpediente + "' AND " + tfect_tra + "=" + codTramite + " AND " + tfect_ocu + "="
                + ocurrencia + " AND " + tfect_cod + "='" + codDato + "'";
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.executeUpdate();
        stmt.close();

        int resultadoInsertar;
        if (valorDato != null && !"".equals(valorDato)) {
            if (mascara == null || "".equals(mascara)) {
                sql = "INSERT INTO E_TFECT ( " + tfect_mun + "," + tfect_pro + "," + tfect_eje + "," + tfect_num + ","
                        + tfect_tra + "," + tfect_ocu + "," + tfect_cod + "," + tfect_valor + ") VALUES (" + codMunicipio
                        + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                        + "," + ocurrencia + ",'" + codDato + "',"
                        + oad.convertir("'" + valorDato + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")";
            } else {
                sql = "INSERT INTO E_TFECT ( " + tfect_mun + "," + tfect_pro + "," + tfect_eje + "," + tfect_num + ","
                        + tfect_tra + "," + tfect_ocu + "," + tfect_cod + "," + tfect_valor + ") VALUES (" + codMunicipio
                        + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                        + "," + ocurrencia + ",'" + codDato + "',"
                        + oad.convertir("'" + valorDato + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, mascara) + ")";
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.prepareStatement(sql);
            resultadoInsertar = stmt.executeUpdate();
            stmt.close();
        } else {
            resultadoInsertar = 1;
        }

        return resultadoInsertar;
    }

    public int setDatoTexto(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        Statement stmt = null;
        PreparedStatement st = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        try {
            String sql = "DELETE FROM E_TXT WHERE TXT_MUN = " + codMunicipio + " AND TXT_EJE = " + ejercicio  
                    + " AND TXT_NUM = '" + numeroExpediente + "' AND TXT_COD = '" + codDato + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();


            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TXT (TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR) VALUES  (?, ?, ?, ?, ?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }

                st = con.prepareStatement(sql);
                // Asignar los valores de la consulta al preparedStatement.
                int i = 1;
                st.setInt(i++, Integer.parseInt(codMunicipio));

                st.setInt(i++, Integer.parseInt(ejercicio));
                st.setString(i++, numeroExpediente);
                st.setString(i++, codDato);
                st.setString(i, valorDato);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = st.executeUpdate();
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                
                if(st!=null) st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }

    public int setDatoTextoTramite(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO)
            throws SQLException {

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        String sql = "DELETE FROM E_TXTT WHERE TXTT_MUN = ? AND TXTT_PRO = ? AND TXTT_EJE = ? AND TXTT_NUM = ? AND "
                + "TXTT_TRA = ? AND TXTT_OCU = ? AND TXTT_COD = ?";
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }

        PreparedStatement stmt = con.prepareStatement(sql);
        int i = 1;
        stmt.setInt(i++, Integer.parseInt(codMunicipio));
        stmt.setString(i++, codProcedimiento);
        stmt.setInt(i++, Integer.parseInt(ejercicio));
        stmt.setString(i++, numeroExpediente);
        stmt.setInt(i++, Integer.parseInt(codTramite));
        stmt.setInt(i++, Integer.parseInt(ocurrencia));
        stmt.setString(i, codDato);

        stmt.executeUpdate();
        stmt.close();

        int resultadoInsertar;
        if (valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TXTT (TXTT_MUN, TXTT_PRO, TXTT_EJE, TXTT_NUM, TXTT_TRA, TXTT_OCU, TXTT_COD, "
                    + "TXTT_VALOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            stmt = con.prepareStatement(sql);

            // Asignar los valores de la consulta al preparedStatement.
            i = 1;
            stmt.setInt(i++, Integer.parseInt(codMunicipio));
            stmt.setString(i++, codProcedimiento);
            stmt.setInt(i++, Integer.parseInt(ejercicio));
            stmt.setString(i++, numeroExpediente);
            stmt.setInt(i++, Integer.parseInt(codTramite));
            stmt.setInt(i++, Integer.parseInt(ocurrencia));
            stmt.setString(i++, codDato);
            stmt.setString(i, valorDato);

            // ejecutar consulta
            resultadoInsertar = stmt.executeUpdate();
        } else {
            resultadoInsertar = 1;
        }

        return resultadoInsertar;
    }
    
    
    
    public int setDatoDesplegableExt(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {
    /*
     * @Descripcion --> Insercion de valores de campos suplementarios de tipo desplegable externo para un procedimiento.
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */
        Statement stmt = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String campoDExtCodigo = eC.getCodCampo();
        String campoDExtComponente = eC.getDesplegable();
        String codDato = (String) gVO.getAtributo(eC.getCodCampo());
        String valorDato = null;

        try {
            String sql = "DELETE FROM E_TDEX WHERE TDEX_MUN = " + codMunicipio + " AND TDEX_EJE = " + ejercicio
                    + " AND TDEX_NUM = '" + numeroExpediente + "' AND TDEX_COD = '" + campoDExtCodigo + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();


            if (codDato != null && !"".equals(codDato)) {
                /*
                Establecer conexion externa definida para el desplegable externo (codDato), para comprobar si el codigo pasado (valorDato) grabando:
                Si no existe --> TDEX_CODSEL=valorDato y TDEX_VALOR=texto 'codigo no existe'
                Si existe --> TDEX_CODSEL=valorDato y TDEX_VALOR=descripcion de valorDato
                */
                valorDato = this.recuperarValorDesplegableExt(campoDExtComponente, codDato, con);
                if (valorDato == null){
                    valorDato = "El código enviado (" + codDato + ") no existe.";
                }
                
                sql = "INSERT INTO E_TDEX (TDEX_MUN, TDEX_EJE, TDEX_NUM, TDEX_COD, TDEX_VALOR, TDEX_CODSEL) VALUES  (?, ?, ?, ?, ?, ?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }

                PreparedStatement st = con.prepareStatement(sql);

                // Asignar los valores de la consulta al preparedStatement.
                int i = 1;
                st.setInt(i++, Integer.parseInt(codMunicipio));

                st.setInt(i++, Integer.parseInt(ejercicio));
                st.setString(i++, numeroExpediente);
                st.setString(i++, campoDExtCodigo);
                st.setString(i++, valorDato);
                st.setString(i, codDato);
                resultadoInsertar = st.executeUpdate();
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }

    public int setDatoDesplegableExtTramite(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO, TramitacionExpedientesValueObject tEVO) 
            throws SQLException {
    /*
     * @Descripcion --> Insercion de valores de campos suplementarios de tipo desplegable externo para un tramite.
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String campoDExtCodigo = eC.getCodCampo();
        String campoDExtComponente = eC.getDesplegable();
        String valorDato = null;
        String codDato = (String) gVO.getAtributo(eC.getCodCampo()+"_CODSEL");
        if(codDato != null) {
            valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        } else {
            codDato = (String) gVO.getAtributo(eC.getCodCampo());
            
            if (codDato != null && !"".equals(codDato)) {
                /*
                Establecer conexion externa definida para el desplegable externo, para comprobar si el codigo pasado existe, grabando:
                Si no existe --> TDEX_CODSEL=valorDato y TDEX_VALOR=texto 'codigo no existe'
                Si existe --> TDEX_CODSEL=valorDato y TDEX_VALOR=descripcion de valorDato
                */
                valorDato = this.recuperarValorDesplegableExt(campoDExtComponente, codDato, con);
                if (valorDato == null){
                    valorDato = "El código enviado (" + codDato + ") no existe.";
                }
            }
        }

        String sql = "DELETE FROM E_TDEXT WHERE TDEXT_MUN = ? AND TDEXT_PRO = ? AND TDEXT_EJE = ? AND TDEXT_NUM = ? AND "
                + "TDEXT_TRA = ? AND TDEXT_OCU = ? AND TDEXT_COD = ?";
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }

        PreparedStatement stmt = con.prepareStatement(sql);
        int i = 1;
        stmt.setInt(i++, Integer.parseInt(codMunicipio));
        stmt.setString(i++, codProcedimiento);
        stmt.setInt(i++, Integer.parseInt(ejercicio));
        stmt.setString(i++, numeroExpediente);
        stmt.setInt(i++, Integer.parseInt(codTramite));
        stmt.setInt(i++, Integer.parseInt(ocurrencia));
        stmt.setString(i, campoDExtCodigo);

        stmt.executeUpdate();
        stmt.close();

        int resultadoInsertar;
        if (valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TDEXT (TDEXT_MUN, TDEXT_PRO, TDEXT_EJE, TDEXT_NUM, TDEXT_TRA, TDEXT_OCU, TDEXT_COD, "
                    + "TDEXT_VALOR, TDEXT_CODSEL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            stmt = con.prepareStatement(sql);

            // Asignar los valores de la consulta al preparedStatement.
            i = 1;
            stmt.setInt(i++, Integer.parseInt(codMunicipio));
            stmt.setString(i++, codProcedimiento);
            stmt.setInt(i++, Integer.parseInt(ejercicio));
            stmt.setString(i++, numeroExpediente);
            stmt.setInt(i++, Integer.parseInt(codTramite));
            stmt.setInt(i++, Integer.parseInt(ocurrencia));
            stmt.setString(i++, campoDExtCodigo);
            stmt.setString(i++, valorDato);
            stmt.setString(i, codDato);

            // ejecutar consulta
            resultadoInsertar = stmt.executeUpdate();
            stmt.close();
        } else {
            resultadoInsertar = 1;
        }

        return resultadoInsertar;
    }
    
    public int setDatoNumerico(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());


        try {
            sql = "DELETE FROM E_TNU WHERE " + tnu_mun + "=" + codMunicipio + " AND " + tnu_eje + "="
                    + ejercicio + " AND " + tnu_num + "='" + numeroExpediente + "' AND " + tnu_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            stmt = null;
            stmt = con.createStatement();
            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TNU ( " + tnu_mun + "," + tnu_eje + "," + tnu_num + "," + tnu_cod + ","
                        + tnu_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "', "
                        + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }

    public int setDatoNumericoCal(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());


        try {
            sql = "DELETE FROM E_TNUC WHERE " + tnuc_mun + "=" + codMunicipio + " AND " + tnuc_eje + "="
                    + ejercicio + " AND " + tnuc_num + "='" + numeroExpediente + "' AND " + tnuc_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            stmt = null;
            stmt = con.createStatement();
            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TNUC ( " + tnuc_mun + "," + tnuc_eje + "," + tnuc_num + "," + tnuc_cod + ","
                        + tnuc_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "', "
                        + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }

    public int setDatoNumericoTramite(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO) throws SQLException {

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        m_Log.debug("ENTRANDO EN setDatoNumericoTramite");
        String sql = "DELETE FROM E_TNUT "
                + "WHERE TNUT_MUN = " + codMunicipio + " AND TNUT_PRO = '" + codProcedimiento + "' "
                + "AND TNUT_EJE = " + ejercicio + " AND TNUT_NUM = '" + numeroExpediente + "' "
                + "AND TNUT_TRA = " + codTramite + " AND TNUT_OCU = " + ocurrencia + " "
                + "AND TNUT_COD = '" + codDato + "'";
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }

        //PreparedStatement stmt = con.prepareStatement(sql);
        //int rowsDeleted = stmt.executeUpdate();
        Statement stmt = con.createStatement();
        int rowsDeleted = stmt.executeUpdate(sql);

        m_Log.debug("setDatoNumericoTramite SE HA BORRADO: " + rowsDeleted);
        try {
            stmt.close();
        } catch (SQLException e) {
            m_Log.debug("ERRRRRROOOOOORRRRRR: " + e.getMessage());
            e.printStackTrace();
        }

        int resultadoInsertar;
        if (valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TNUT (TNUT_MUN, TNUT_PRO, TNUT_EJE, TNUT_NUM, TNUT_TRA, TNUT_OCU, TNUT_COD, TNUT_VALOR) "
                    + "VALUES (" + codMunicipio + ", '" + codProcedimiento + "', " + ejercicio + ", "
                    + "'" + numeroExpediente + "', " + codTramite + ", " + ocurrencia + ", '" + codDato + "', "
                    + oad.convertir("'" + valorDato + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            Statement stmt2 = con.createStatement();
            resultadoInsertar = stmt2.executeUpdate(sql);
            //stmt = con.prepareStatement(sql);
            //resultadoInsertar = stmt.executeUpdate();
            m_Log.debug("setDatoNumericoTramite SE HA INSERTADO: " + resultadoInsertar);
            stmt2.close();
        } else {
            resultadoInsertar = 1;
        }
        return resultadoInsertar;
    }

    public int setDatoNumericoTramiteCal(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO) throws SQLException {

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        m_Log.debug("ENTRANDO EN setDatoNumericoTramiteCalculado");
        String sql = "DELETE FROM E_TNUCT "
                + "WHERE TNUCT_MUN = " + codMunicipio + " AND TNUCT_PRO = '" + codProcedimiento + "' "
                + "AND TNUCT_EJE = " + ejercicio + " AND TNUCT_NUM = '" + numeroExpediente + "' "
                + "AND TNUCT_TRA = " + codTramite + " AND TNUCT_OCU = " + ocurrencia + " "
                + "AND TNUCT_COD = '" + codDato + "'";
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }

        //PreparedStatement stmt = con.prepareStatement(sql);
        //int rowsDeleted = stmt.executeUpdate();
        Statement stmt = con.createStatement();
        int rowsDeleted = stmt.executeUpdate(sql);

        m_Log.debug("setDatoNumericoTramiteCalculado SE HA BORRADO: " + rowsDeleted);
        try {
            stmt.close();
        } catch (SQLException e) {
            m_Log.debug("ERRRRRROOOOOORRRRRR: " + e.getMessage());
            e.printStackTrace();
        }

        int resultadoInsertar;
        if (valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TNUCT (TNUCT_MUN, TNUCT_PRO, TNUCT_EJE, TNUCT_NUM, TNUCT_TRA, TNUCT_OCU, TNUCT_COD, TNUCT_VALOR) "
                    + "VALUES (" + codMunicipio + ", '" + codProcedimiento + "', " + ejercicio + ", "
                    + "'" + numeroExpediente + "', " + codTramite + ", " + ocurrencia + ", '" + codDato + "', "
                    + oad.convertir("'" + valorDato + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            Statement stmt2 = con.createStatement();
            resultadoInsertar = stmt2.executeUpdate(sql);
            //stmt = con.prepareStatement(sql);
            //resultadoInsertar = stmt.executeUpdate();
            m_Log.debug("setDatoNumericoTramiteCalculado SE HA INSERTADO: " + resultadoInsertar);
            stmt2.close();
        } else {
            resultadoInsertar = 1;
        }
        return resultadoInsertar;
    }

    public int setDatoTextoLargo(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        PreparedStatement pstmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());


        try {
            sql = "DELETE FROM E_TTL WHERE " + ttl_mun + "=" + codMunicipio + " AND " + ttl_eje + "="
                    + ejercicio + " AND " + ttl_num + "='" + numeroExpediente + "' AND " + ttl_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();

            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TTL ( " + ttl_mun + "," + ttl_eje + "," + ttl_num + "," + ttl_cod + ","
                        + ttl_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "',?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                pstmt.close();
                pstmt = con.prepareStatement(sql);
                java.io.StringReader cr = new java.io.StringReader(valorDato);
                pstmt.setCharacterStream(1, cr, valorDato.length());
                resultadoInsertar = pstmt.executeUpdate();
                cr.close();
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }

    public int setDatoTextoLargoTramite(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO)
            throws SQLException {

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        String sql = "DELETE FROM E_TTLT WHERE " + ttlt_mun + "=" + codMunicipio + " AND " + ttlt_pro + "='"
                + codProcedimiento + "' AND " + ttlt_eje + "=" + ejercicio + " AND " + ttlt_num + "='"
                + numeroExpediente + "' AND " + ttlt_tra + "=" + codTramite + " AND " + ttlt_ocu + "="
                + ocurrencia + " AND " + ttlt_cod + "='" + codDato + "'";
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();

        int resultadoInsertar;
        if (valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TTLT ( " + ttlt_mun + "," + ttlt_pro + "," + ttlt_eje + "," + ttlt_num + ","
                    + ttlt_tra + "," + ttlt_ocu + "," + ttlt_cod + "," + ttlt_valor + ") VALUES ("
                    + codMunicipio + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente
                    + "'," + codTramite + "," + ocurrencia + ",'" + codDato + "',?)";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            pstmt = con.prepareStatement(sql);
            java.io.StringReader cr = new java.io.StringReader(valorDato);
            pstmt.setCharacterStream(1, cr, valorDato.length());
            resultadoInsertar = pstmt.executeUpdate();
            cr.close();
            pstmt.close();
        } else {
            resultadoInsertar = 1;
        }

        return resultadoInsertar;
    }

    public int setDatoDesplegable(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO) {

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());


        try {
            sql = "DELETE FROM E_TDE WHERE " + tde_mun + "=" + codMunicipio + " AND " + tde_eje + "="
                    + ejercicio + " AND " + tde_num + "='" + numeroExpediente + "' AND " + tde_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TDE ( " + tde_mun + "," + tde_eje + "," + tde_num + "," + tde_cod + ","
                        + tde_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "','" + valorDato + "')";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                stmt = con.createStatement();
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException e){
                e.printStackTrace();
            }            
        }
        return resultadoInsertar;
    }

    public int setDatoDesplegableTramite(AdaptadorSQL oad, Connection con, EstructuraCampo eC, GeneralValueObject gVO,
            TramitacionExpedientesValueObject tEVO)
            throws SQLException {

        String codMunicipio = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrencia = tEVO.getOcurrenciaTramite();

        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());


        String sql = "DELETE FROM E_TDET WHERE " + tdet_mun + "=" + codMunicipio + " AND " + tdet_pro + "='"
                + codProcedimiento + "' AND " + tdet_eje + "=" + ejercicio + " AND " + tdet_num + "='"
                + numeroExpediente + "' AND " + tdet_tra + "=" + codTramite + " AND " + tdet_ocu + "="
                + ocurrencia + " AND " + tdet_cod + "='" + codDato + "'";
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.executeUpdate();
        stmt.close();

        int resultadoInsertar;
        if (valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TDET ( " + tdet_mun + "," + tdet_pro + "," + tdet_eje + "," + tdet_num + ","
                    + tdet_tra + "," + tdet_ocu + "," + tdet_cod + "," + tdet_valor + ") VALUES (" + codMunicipio
                    + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," + codTramite
                    + "," + ocurrencia + ",'" + codDato + "','" + valorDato + "')";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.prepareStatement(sql);
            resultadoInsertar = stmt.executeUpdate();
            stmt.close();
        } else {
            resultadoInsertar = 1;
        }
        return resultadoInsertar;
    }

    public byte[] getFichero(String codigo, String municipio, String ejercicio, String numero, String[] params) throws TechnicalException {
        byte[] fichero = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            //stmt = conexion.createStatement();
            sql = "SELECT " + tfi_valor + " FROM E_TFI WHERE " + tfi_mun + "=" + municipio + " AND " + tfi_eje + "="
                    + ejercicio + " AND " + tfi_num + "='" + numero + "' AND " + tfi_cod + "='"
                    + codigo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                InputStream st = rs.getBinaryStream(tfi_valor);
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read()) != -1) {
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
            }
        } catch (Exception e) {
            m_Log.error("Excepcion: " + e.getMessage());
            e.printStackTrace();
            rollBackTransaction(abd, conexion);
        } finally {
            try {                
                
                if(rs!=null) rs.close();                
                if (stmt!= null) stmt.close();                
                commitAndCloseTransaction(abd, conexion);
            }catch (Exception e) {
                m_Log.error("Excepcion: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return fichero;
    }
    
    
    
    public byte[] getFichero(String codigo, String municipio, String ejercicio, String numero,Connection con) throws TechnicalException {
        byte[] fichero = null;        
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try {        
        
            sql = "SELECT " + tfi_valor + " FROM E_TFI WHERE " + tfi_mun + "=" + municipio + " AND " + tfi_eje + "="
                    + ejercicio + " AND " + tfi_num + "='" + numero + "' AND " + tfi_cod + "='"
                    + codigo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                InputStream st = rs.getBinaryStream(tfi_valor);
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read()) != -1) {
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {                
                
                if(rs!=null) rs.close();                
                if (stmt!= null) stmt.close();                                
                
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fichero;
    }
    
    

    /**
     * Recupera el tamaño del fichero
     *
     * @param codigo
     * @param municipio
     * @param ejercicio
     * @param numero
     * @param params
     * @return tamaño del fichero
     * @throws es.altia.common.exception.TechnicalException
     *
     * public long getTamFichero(String codigo,String municipio, String
     * ejercicio, String numero, String[] params) throws TechnicalException {
     * byte[] fichero = null; AdaptadorSQLBD abd = null; Connection conexion =
     * null; Statement stmt = null; ResultSet rs = null; String sql = ""; try{
     * //m_Log.debug("A por el OAD"); abd = new AdaptadorSQLBD(params);
     * //m_Log.debug("A por la conexion"); conexion = abd.getConnection();
     * abd.inicioTransaccion(conexion); //stmt = conexion.createStatement(); sql
     * = "SELECT "+tfi_valor+" FROM E_TFI WHERE " + tfi_mun + "=" + municipio +
     * " AND " + tfi_eje + "=" + ejercicio + " AND " + tfi_num + "='" + numero +
     * "' AND " + tfi_cod + "='" + codigo + "'"; if(m_Log.isDebugEnabled())
     * m_Log.debug(sql); stmt = conexion.createStatement(); rs =
     * stmt.executeQuery(sql); while (rs.next()){ InputStream st =
     * rs.getBinaryStream(tfi_valor); ByteArrayOutputStream ot = new
     * ByteArrayOutputStream(); int c; while ((c = st.read())!= -1){
     * ot.write(c); } ot.flush(); fichero = ot.toByteArray(); } }catch
     * (Exception e){ rollBackTransaction(abd,conexion); }finally{ try { if
     * (stmt!=null) { stmt.close(); } commitTransaction(abd,conexion); } catch
     * (Exception e) { e.printStackTrace(); } } return fichero; }
     */
    public byte[] getFicheroTramite(String codigo, String municipio, String ejercicio, String numero, String[] params, String codTramite) throws TechnicalException {
        byte[] fichero = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            //stmt = conexion.createStatement();

            sql = "SELECT " + tfit_valor + " FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                InputStream st = rs.getBinaryStream(tfit_valor);
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read()) != -1) {
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
            }
        } catch (Exception e) {
            rollBackTransaction(abd, conexion);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                commitAndCloseTransaction(abd, conexion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fichero;
    }

    public byte[] getFicheroTramiteExpediente(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, Connection con, String codTramite) throws TechnicalException {
        byte[] fichero = null;        
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
                        
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            sql = "SELECT " + tfit_valor + " FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                InputStream st = rs.getBinaryStream(tfit_valor);
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read()) != -1) {
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
            }
        }catch(IOException e) {
            m_Log.error(" Error al recuperar una conexión a la BBDD: " + e.getMessage());            
        }
        catch(SQLException e){
             m_Log.error(" Error al recuperar el contenido de un campo suplementario de tipo fichero: " + e.getMessage());
        }   
        finally {
            
            try {
                
                if (stmt!= null) stmt.close();
                if(rs!=null) rs.close();                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fichero;
    }
    
    
    
     public byte[] getFicheroTramiteExpediente(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, String[] params, String codTramite) throws TechnicalException {
        byte[] fichero = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();
            
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            sql = "SELECT " + tfit_valor + " FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                InputStream st = rs.getBinaryStream(tfit_valor);
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read()) != -1) {
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
            }
        }catch(BDException e) {
            m_Log.error(" Error al recuperar una conexión a la BBDD: " + e.getMessage());
            
        }catch(IOException e) {
            m_Log.error(" Error al recuperar una conexión a la BBDD: " + e.getMessage());            
        }
        catch(SQLException e){
             m_Log.error(" Error al recuperar el contenido de un campo suplementario de tipo fichero: " + e.getMessage());
        }   
        finally {
            try {
                if (stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if(conexion!=null) conexion.close();                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fichero;
    }
    
    

    public String getTipoFichero(String codigo, String municipio, String ejercicio, String numero, String[] params) throws TechnicalException {
        String tipo = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();            
            sql = "SELECT " + tfi_mime + " FROM E_TFI WHERE " + tfi_mun + "=" + municipio + " AND " + tfi_eje + "="
                    + ejercicio + " AND " + tfi_num + "='" + numero + "' AND " + tfi_cod + "='"
                    + codigo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString(tfi_mime);
            }
            
        } catch (BDException e) {
            m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            
        }catch (SQLException e) {
            m_Log.error("Error al recuperar el tipo MIME de un campo suplementario de tipo fichero con código: " + codigo + " del expediente : " + numero + ": " + e.getMessage());
            
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if(conexion!=null) conexion.close();                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }
    
    
    
     public String getTipoFichero(String codigo, String municipio, String ejercicio, String numero, Connection con) throws TechnicalException {
        String tipo = "";        
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
        
            sql = "SELECT " + tfi_mime + " FROM E_TFI WHERE " + tfi_mun + "=" + municipio + " AND " + tfi_eje + "="
                    + ejercicio + " AND " + tfi_num + "='" + numero + "' AND " + tfi_cod + "='"
                    + codigo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString(tfi_mime);
            }
            
        } catch (SQLException e) {
            m_Log.error("Error al recuperar el tipo MIME de un campo suplementario de tipo fichero con código: " + codigo + " del expediente : " + numero + ": " + e.getMessage());
            
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs!=null) rs.close();
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }
    
    

    public String getTipoFicheroTramite(String codigo, String municipio, String ejercicio, String numero, String[] params, String codTramite) throws TechnicalException {
        String tipo = "";
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try {            
            conexion = abd.getConnection();            
            sql = "SELECT " + tfit_mime + " FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString(tfit_mime);
            }            
            
        } catch (BDException e) {
            m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            
        }catch (SQLException e) {
            m_Log.error("Error al recuperar el tipo MIME de un campo suplementario de tipo fichero con código: " + codigo + " del expediente : " + numero + " y del trámite: " +  codTramite + ":" + e.getMessage());
            
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if(conexion!=null) conexion.close();                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }

    public String getTipoFicheroTramiteExpediente(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, String[] params, String codTramite) throws TechnicalException {
        String tipo = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            
            sql = "SELECT " + tfit_mime + " FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString(tfit_mime);
            }            
                        
        } catch (BDException e) {            
            m_Log.error(" Error al recuperar una conexión a la BBDD: " + e.getMessage());                        
        } catch (SQLException e) {            
            m_Log.error(" Error al recuperar el tipo MIME de un campo suplementario de tipo fichero: " + e.getMessage());                        
        } 
        finally {
            try {
                if(stmt!= null) stmt.close();
                if(rs!=null) rs.close();
                if(conexion!=null) conexion.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }
    
    
    
    
    public String getTipoFicheroTramiteExpediente(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, Connection con, String codTramite) throws TechnicalException {
        String tipo = "";    
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            
            sql = "SELECT " + tfit_mime + " FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString(tfit_mime);
            }            
                        
        } catch (SQLException e) {            
            m_Log.error(" Error al recuperar el tipo MIME de un campo suplementario de tipo fichero: " + e.getMessage());                        
        } 
        finally {
            try {
                if(stmt!= null) stmt.close();
                if(rs!=null) rs.close();          
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }
    
    

    private void rollBackTransaction(AdaptadorSQLBD bd, Connection con) throws TechnicalException {
        try {
            if (con != null) {
                bd.rollBack(con);
            }
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje());
        }
    }

    private void commitAndCloseTransaction(AdaptadorSQLBD bd, Connection con) {
        try {
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage());
        }
    }

   
    /**
     * Recupera el nombre de un fichero
     *
     * @param codigo
     * @param municipio
     * @param ejercicio
     * @param numero
     * @param params
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public String getNombreFichero(String codigo, String municipio, String ejercicio, String numero, String[] params) throws TechnicalException {
        String tipo = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();
            
            sql = "SELECT TFI_NOMFICH FROM E_TFI WHERE " + tfi_mun + "=" + municipio + " AND " + tfi_eje + "="
                    + ejercicio + " AND " + tfi_num + "='" + numero + "' AND " + tfi_cod + "='"
                    + codigo + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("getNombreFichero.E_TFI: " + sql);
            }
            
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString("TFI_NOMFICH");
            }
        }catch (BDException e) {
            m_Log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());
            
        } catch (SQLException e) {
            m_Log.error("Error al recuperar el nombre del campo suplementario de tipo fichero: " + codigo + " del expediente " + numero + " por: " +  e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if(conexion!=null) conexion.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }
    
    
    
    
   /**
     * Recupera el nombre y la longitud de un fichero de un campo suplementario definido a nivel de expediente
     * @param codigo: Código del campo
     * @param municipio: Código de la organización 
     * @param ejercicio: Ejercicio del expediente
     * @param numero: Número del expediente
     * @param params: Parámetros de conexión a la BBDD
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public CampoSuplementarioFicheroVO getNombreLongitudFichero(String codigo, String municipio, String ejercicio, String numero, Connection conexion) throws TechnicalException {
        String nombre = "";
        int longitud = 0;
        
        CampoSuplementarioFicheroVO salida = new CampoSuplementarioFicheroVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {
                /*        
            sql = "SELECT TFI_NOMFICH,TFI_TAMANHO,TFI_MIME FROM E_TFI WHERE TFI_MUN=" + municipio + " AND TFI_EJE="
                    + ejercicio + " AND TFI_NUM='" + numero + "' AND TFI_COD='" + codigo + "'";
                    * */
            
            sql = "SELECT TFI_NOMFICH,TFI_TAMANHO,TFI_MIME FROM E_TFI WHERE TFI_MUN=? AND TFI_EJE=?" + 
                  " AND TFI_NUM=? AND TFI_COD=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("getNombreLongitudFichero.E_TFI: " + sql);
            }
            
            int i=1;
            ps = conexion.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(municipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            ps.setString(i++,codigo);
            
            rs = ps.executeQuery();            
            while (rs.next()) {
                salida.setNombreFichero(rs.getString("TFI_NOMFICH"));                 
                salida.setLongitudFichero(rs.getInt("TFI_TAMANHO"));
                salida.setTipoMime(rs.getString("TFI_MIME"));
            }
            
        } catch (SQLException e) {
            m_Log.error("Error al recuperar el nombre del campo suplementario de tipo fichero: " + codigo + " del expediente " + numero + " por: " +  e.getMessage());
        } finally {
            
            try {
                if(ps!= null) ps.close();
                if(rs!=null) rs.close();            
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return salida;
    }
    
    
    

    /**
     * Recupera el nombre de un fichero
     *
     * @param codigo
     * @param municipio
     * @param ejercicio
     * @param numero
     * @param ocurrencia
     * @param params
     * @param codTramite
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public String getNombreFicheroTramiteExpediente(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, String[] params, String codTramite) throws TechnicalException {
        String tipo = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();
            
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            
            sql = "SELECT TFIT_NOMFICH FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString("TFIT_NOMFICH");
            }

        }catch (BDException e) {
            m_Log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());            
        }catch (SQLException e) {
            m_Log.error("Error al recuperar el nombre del campo suplementario de tipo fichero: " + codigo + " del expediente " + numero + " por: " +  e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(rs!=null) rs.close();                
                if(conexion!=null) conexion.close();
            } catch (Exception e) {
                m_Log.error("Error al cerrar recursos asociados a una conexión a la BBDD: " + e.getMessage());
            }
        }
        return tipo;
    }
    
    
    
    
   /**
     * Recupera el nombre y longitud de un campo suplementario de tipo fichero definido a nivel de expediente     
     * @param codigo: Código del campo suplementario
     * @param municipio: Código del municipio
     * @param ejercicio: Ejercicio del expediente
     * @param numero: Número del expediente
     * @param ocurrencia: Ocurrencia del trámite
     * @param codTramite: Código del trámite
     * @param params: Parámetros de conexión a la BBDD
     * @return Objeto de tipo CampoSuplementarioFicheroVO
     * @throws es.altia.common.exception.TechnicalException: Si ocurre algún error
     */
    public CampoSuplementarioFicheroVO getNombreLongitudFicheroTramiteExpediente(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, String codTramite, Connection conexion) throws TechnicalException {
        CampoSuplementarioFicheroVO salida = new CampoSuplementarioFicheroVO();
        String tipo = "";    
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try {            
            
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            
            /*
            sql = "SELECT TFIT_NOMFICH,TFIT_TAMANHO FROM E_TFIT WHERE TFIT_MUN="
                    + municipio + " AND TFIT_EJE=" + ejercicio + " AND TFIT_NUM='" + numero + "' AND TFIT_TRA="
                    + codTramite + " AND TFIT_COD='" + codigo + "'" + " AND TFIT_OCU=" + ocurrencia;
            */
            
            sql = "SELECT TFIT_NOMFICH,TFIT_TAMANHO FROM E_TFIT WHERE TFIT_MUN=?" + 
                  " AND TFIT_EJE=? AND TFIT_NUM=? AND TFIT_TRA=?" + 
                  " AND TFIT_COD=? AND TFIT_OCU=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
         
            int i=1;
            ps = conexion.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(municipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setString(i++,codigo);
            ps.setInt(i++,Integer.parseInt(ocurrencia));
            
            rs = ps.executeQuery();
                        
            while (rs.next()) {                
                salida.setNombreFichero(rs.getString("TFIT_NOMFICH"));
                salida.setLongitudFichero(rs.getInt("TFIT_TAMANHO"));
                salida.setCodigoCampo(codigo);
                salida.setCodMunicipio(municipio);
                salida.setCodTramite(Integer.parseInt(codTramite));
                salida.setOcurrenciaTramite(Integer.parseInt(ocurrencia));
                salida.setNumExpediente(numero);                
            }

        }catch (SQLException e) {
            m_Log.error("Error al recuperar el nombre del campo suplementario de tipo fichero: " + codigo + " del expediente " + numero + " por: " +  e.getMessage());
        } finally {
            try {
                if (ps!= null) ps.close();
                if(rs!=null) rs.close();                
                
            } catch (Exception e) {
                m_Log.error("Error al cerrar recursos asociados a una conexión a la BBDD: " + e.getMessage());
            }
        }
        return salida;
    }
    
    
    
    
    /**
     * Recupera el nombre de un fichero y su longitud que se encuentra asociado a un campo suplementario
     * de tipo fichero, definido a nivel de trámite
     * @param codigo: Código del campo suplementario
     * @param municipio: Código del municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número del expediente
     * @param ocurrencia: Ocurrencia del trámite
     * @param params: Parámetros de conexión a la BBDD
     * @param codTramite: Código del trámite
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public CampoSuplementarioFicheroVO getNombreLongitudFicheroTramite(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, String codTramite,String[] params) throws TechnicalException {
        CampoSuplementarioFicheroVO campo = new CampoSuplementarioFicheroVO();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try {
            
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();
            
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            
            sql = "SELECT TFIT_NOMFICH,TFIT_TAMANHO,TFIT_MIME FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {                
                campo.setCodigoCampo(codigo);
                campo.setNombreFichero(rs.getString("TFIT_NOMFICH"));
                campo.setLongitudFichero(rs.getInt("TFIT_TAMANHO"));
                campo.setCodTramite(Integer.parseInt(codTramite));
                campo.setOcurrenciaTramite(Integer.parseInt(ocurrencia));
                campo.setNumExpediente(numero);
                campo.setTipoMime(rs.getString("TFIT_MIME"));
            }

        }catch (BDException e) {
            m_Log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());            
        }catch (SQLException e) {
            m_Log.error("Error al recuperar el nombre del campo suplementario de tipo fichero: " + codigo + " del expediente " + numero + " por: " +  e.getMessage());
        } finally {
            try {
                
                if(stmt != null) stmt.close();
                if(rs!=null) rs.close();                
                if(conexion!=null) conexion.close();
                
            } catch (Exception e) {
                m_Log.error("Error al cerrar recursos asociados a una conexión a la BBDD: " + e.getMessage());
            }
        }        
        return campo;
    }
    

    public boolean copyValoresNumericos(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int camposCopiados = 0;
        
        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TNU_COD, TNU_VALOR FROM E_TNU WHERE TNU_MUN = " + codMunicipio
                    + " AND TNU_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();
                camposVO.setAtributo("codigoCampo", rs.getString("TNU_COD"));
                camposVO.setAtributo("valorCampo", rs.getString("TNU_VALOR"));

                camposSupl.addElement(camposVO);
            }

            
            /** original            
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            }
            **/
            numeroCamposCopiar = camposSupl.size();
            camposCopiados = 0;
            
            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);
                sql = "INSERT INTO E_TNU VALUES ( " + codMunicipio + "," + ejercicio + ",'" + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") + "'," + camposVO.getAtributo("valorCampo") + ")";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                if(resultadoInsertar==1) camposCopiados++;
            }


        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(stmt!= null) stmt.close();
                if(rs!=null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(numeroCamposCopiar==camposCopiados)
            return true;
        else
            return false;
    }

    public boolean copyValoresNumericosCalculados(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;

        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TNUC_COD, TNUC_VALOR FROM E_TNUC WHERE TNUC_MUN = " + codMunicipio
                    + " AND TNUC_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();
                camposVO.setAtributo("codigoCampo", rs.getString("TNUC_COD"));
                camposVO.setAtributo("valorCampo", rs.getString("TNUC_VALOR"));

                camposSupl.addElement(camposVO);
            }

            numeroCamposCopiar = camposSupl.size();
            /**
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            }*/

            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);
                sql = "INSERT INTO E_TNUC VALUES ( " + codMunicipio + "," + ejercicio + ",'" + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") + "'," + camposVO.getAtributo("valorCampo") + ")";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                if(resultadoInsertar==1) numeroCamposCopiados++;
            }


        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(numeroCamposCopiar==numeroCamposCopiados)
            return true;
        else
            return false;
    }

    public boolean copyValoresFecha(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {
        Statement stmt = null;
        PreparedStatement ps = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;

        //Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TFE_COD,TFE_VALOR,TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO FROM E_TFE WHERE TFE_MUN = " + codMunicipio
                    + " AND TFE_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();

            rs = stmt.executeQuery(sql);

            ArrayList<CampoSuplementarioFechaVO> camposSupl = new ArrayList<CampoSuplementarioFechaVO>();
            while (rs.next()) {
                //GeneralValueObject camposVO = new GeneralValueObject();
                CampoSuplementarioFechaVO campo = new CampoSuplementarioFechaVO();
                
                campo.setCodigoCampo(rs.getString("TFE_COD"));
                campo.setCodMunicipio(Integer.parseInt(codMunicipio));
                campo.setPlazoActivado(rs.getInt("PLAZO_ACTIVADO"));
                java.sql.Timestamp tFechaValor = rs.getTimestamp("TFE_VALOR");
                if(tFechaValor!=null)
                    campo.setValorfecha(DateOperations.toCalendar(rs.getTimestamp("TFE_VALOR")));
                else
                    campo.setValorfecha(null);
                
                java.sql.Timestamp tFechaVencimiento = rs.getTimestamp("TFE_FEC_VENCIMIENTO");
                if(tFechaVencimiento!=null)
                    campo.setFechaVencimiento(DateOperations.toCalendar(tFechaVencimiento));
                else
                    campo.setFechaVencimiento(null);
                                
                int plazo = rs.getInt("PLAZO_ACTIVADO");
                campo.setPlazoActivado(plazo);                
                
                camposSupl.add(campo);
            }

            /** original
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            } */
            numeroCamposCopiar = camposSupl.size();
            
            stmt.close();
            rs.close();

            for (int i = 0; i < camposSupl.size(); i++) {
                //GeneralValueObject camposVO = camposSupl.get(i);
                CampoSuplementarioFechaVO camposVO = camposSupl.get(i);
                
                sql = "INSERT INTO E_TFE(TFE_MUN,TFE_EJE,TFE_NUM,TFE_COD,TFE_VALOR,TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO) VALUES (?,?,?,?,?,?,?)";
                m_Log.debug(sql);
                ps = con.prepareStatement(sql);
                int indice=1;
                ps.setInt(indice++,Integer.parseInt(codMunicipio));
                ps.setInt(indice++,Integer.parseInt(ejercicio));
                ps.setString(indice++,numExpedienteNuevo);
                ps.setString(indice++,camposVO.getCodigoCampo());
                                
                if(camposVO.getValorfecha()==null)
                    ps.setNull(indice++,java.sql.Types.TIMESTAMP);
                else
                    ps.setTimestamp(indice++,DateOperations.toTimestamp(camposVO.getValorfecha()));
                
                if(camposVO.getFechaVencimiento()==null)
                    ps.setNull(indice++,java.sql.Types.TIMESTAMP);
                else
                    ps.setTimestamp(indice++,DateOperations.toTimestamp(camposVO.getFechaVencimiento()));
                
                ps.setInt(indice++,camposVO.getPlazoActivado());                
                resultadoInsertar = ps.executeUpdate();
                if(resultadoInsertar==1) numeroCamposCopiados++;
                    
                ps.close();
                
                if (camposVO.getFechaVencimiento()!=null && camposVO.getPlazoActivado()==1){
                    AlarmaExpedienteVO alarma = new AlarmaExpedienteVO(Integer.parseInt(codMunicipio),
                            Integer.parseInt(ejercicio),numExpedienteNuevo,0,0,camposVO.getCodigoCampo(),
                            camposVO.getFechaVencimiento());
                    AlarmasExpedienteDAO.getInstance().insertarAlarmaExpediente(alarma,con);
                }
            }// for

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt!= null) stmt.close();
                if (rs!= null) rs.close();
                if (ps!= null) ps.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(numeroCamposCopiados==numeroCamposCopiar)
            return true;
        else
            return false;        
        //return resultadoInsertar;
    }

    public boolean copyValoresFechaCalculada(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;

        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TFEC_COD FROM E_TFEC WHERE TFEC_MUN = " + codMunicipio
                    + " AND TFEC_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();
                camposVO.setAtributo("codigoCampo", rs.getString("TFEC_COD"));
                camposSupl.addElement(camposVO);
            }

            numeroCamposCopiar = camposSupl.size();
            /**
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            }*/

            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);

                sql = "INSERT INTO E_TFEC SELECT " + codMunicipio + "," + ejercicio + ",'" + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") + "',TFEC_VALOR FROM E_TFEC WHERE TFEC_NUM='" + numExpedienteOriginal + "' AND TFEC_COD='" + camposVO.getAtributo("codigoCampo") + "'";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                if(resultadoInsertar==1) numeroCamposCopiados++;
            }


        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(stmt!= null) stmt.close();
                if(rs!= null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(numeroCamposCopiados==numeroCamposCopiar)
            return true;
        else
            return false;      
    }

    public boolean copyValoresTextoLargo(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;

        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TTL_COD FROM E_TTL WHERE TTL_MUN = " + codMunicipio
                    + " AND TTL_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();
                camposVO.setAtributo("codigoCampo", rs.getString("TTL_COD"));
                camposSupl.addElement(camposVO);
            }

            /** original
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            } **/
            
            numeroCamposCopiar = camposSupl.size();

            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);

                sql = "INSERT INTO E_TTL SELECT " + codMunicipio + "," + ejercicio + ",'" + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") + "',TTL_VALOR FROM E_TTL WHERE TTL_NUM='" + numExpedienteOriginal + "' AND TTL_COD='" + camposVO.getAtributo("codigoCampo") + "'";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                if(resultadoInsertar==1) numeroCamposCopiados++;

            }


        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt!= null) stmt.close();
                if (rs!= null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(numeroCamposCopiar==numeroCamposCopiados)
            return true;
        else
            return false;
    }

    public boolean copyValoresFichero(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO, String[] params) throws TechnicalException {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        byte[] fichero = null;
        byte[] valorDato = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;
        
        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TFI_COD, TFI_VALOR,TFI_MIME,TFI_NOMFICH FROM E_TFI WHERE TFI_MUN = " + codMunicipio
                    + " AND TFI_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();
                camposVO.setAtributo("codigoCampo", rs.getString("TFI_COD"));

                camposVO.setAtributo("tipoMime", rs.getString("TFI_MIME"));
                camposVO.setAtributo("nomFichero", rs.getString("TFI_NOMFICH"));
                camposSupl.addElement(camposVO);
            }

            /**
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            }**/
            numeroCamposCopiar = camposSupl.size();

            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);

                sql = "INSERT INTO E_TFI VALUES(" + codMunicipio + "," + ejercicio + ",'" + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") + "',?,'" + camposVO.getAtributo("tipoMime") + "','" + camposVO.getAtributo("nomFichero") + "')";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }

                String[] partesExp = null;
                partesExp = numExpedienteOriginal.split("/");
                String ejercicioOrigial = partesExp[0];

                valorDato = getFichero((String) camposVO.getAtributo("codigoCampo"), codMunicipio, ejercicioOrigial, numExpedienteOriginal, params);

                PreparedStatement ps = con.prepareStatement(sql);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(valorDato);
                }

                InputStream st = new ByteArrayInputStream(valorDato);
                ps.setBinaryStream(1, st, valorDato.length);
                resultadoInsertar = ps.executeUpdate();
                if(resultadoInsertar==1) numeroCamposCopiados++;
                st.close();
                ps.close();

            }


        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(numeroCamposCopiar==numeroCamposCopiados)
            return true;
        else
            return false;
    }

    public boolean copyValoresDesplegable(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;

        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TDE_COD,TDE_VALOR FROM E_TDE WHERE TDE_MUN = " + codMunicipio
                    + " AND TDE_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();

                camposVO.setAtributo("codigoCampo", rs.getString("TDE_COD"));
                camposVO.setAtributo("valorCampo", rs.getString("TDE_VALOR"));

                camposSupl.addElement(camposVO);
            }

            numeroCamposCopiar = camposSupl.size();
            /**
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            }**/
            
            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);

                sql = "INSERT INTO E_TDE VALUES ( " + codMunicipio + "," + ejercicio + ",'" + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") + "','" + camposVO.getAtributo("valorCampo") + "')";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                if(resultadoInsertar==1) numeroCamposCopiados++;
            }


        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt!= null) stmt.close();
                if (rs!= null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         
        if(numeroCamposCopiar==numeroCamposCopiados)
            return true;
        else
            return false;
    }

    public boolean copyValoresTexto(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;

        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TXT_COD,TXT_VALOR FROM E_TXT WHERE TXT_MUN = " + codMunicipio
                    + " AND TXT_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();
                camposVO.setAtributo("codigoCampo", rs.getString("TXT_COD"));
                camposVO.setAtributo("valorCampo", rs.getString("TXT_VALOR"));

                camposSupl.addElement(camposVO);
            }

            numeroCamposCopiar = camposSupl.size();
            /**
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            }**/

            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);


                sql = "INSERT INTO E_TXT VALUES ( " + codMunicipio + "," + ejercicio + ",'" + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") + "','" + camposVO.getAtributo("valorCampo") + "')";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                if(resultadoInsertar==1) numeroCamposCopiados++;
            }
        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt!= null) stmt.close();
                if (rs!= null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(numeroCamposCopiar==numeroCamposCopiados)
            return true;
        else
            return false;
    }
    
    
    
    public boolean copyValoresDesplegableExt(AdaptadorSQLBD bd, Connection con, GeneralValueObject gVO) throws TechnicalException {  
    /*
     * @Descripcion --> Copia a nivel de procedimiento de campos suplementarios de tipo desplegable externo.
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */      
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numExpedienteOriginal = (String) gVO.getAtributo("numExpedienteOriginal");
        String numExpedienteNuevo = (String) gVO.getAtributo("numExpedienteNuevo");
        ResultSet rs = null;
        int numeroCamposCopiar = 0;
        int numeroCamposCopiados = 0;

        Vector<GeneralValueObject> camposSupl = new Vector<GeneralValueObject>();

        try {
            sql = "SELECT TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM E_TDEX WHERE TDEX_NUM = " + codMunicipio
                    + " AND TDEX_NUM ='" + numExpedienteOriginal + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GeneralValueObject camposVO = new GeneralValueObject();
                camposVO.setAtributo("codigoCampo", rs.getString("TDEX_COD"));
                camposVO.setAtributo("valorCampo", rs.getString("TDEX_VALOR"));
                camposVO.setAtributo("codigoCampoSel", rs.getString("TDEX_CODSEL"));

                camposSupl.addElement(camposVO);
            }

            numeroCamposCopiar = camposSupl.size();
            
            /**
            if (camposSupl.size() == 0) {
                resultadoInsertar = 1;
            }**/

            for (int i = 0; i < camposSupl.size(); i++) {
                GeneralValueObject camposVO = camposSupl.get(i);


                sql = "INSERT INTO E_TDEX (TDEXT_MUN, TDEXT_EJE,TDEXT_NUM,TDEXT_COD, TDEXT_VALOR, TDEX_CODSEL) "
                        + "VALUES ( " + codMunicipio + "," + ejercicio + ",'" 
                        + numExpedienteNuevo + "','" + camposVO.getAtributo("codigoCampo") 
                        + "','" + camposVO.getAtributo("valorCampo") 
                        + "','" + camposVO.getAtributo("codCampoSel") + "')";
                
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
                if(resultadoInsertar==1) numeroCamposCopiados++;

            }


        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt!= null) stmt.close();
                if (rs!= null) rs.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(numeroCamposCopiar==numeroCamposCopiados)
            return true;
        else
            return false;
    }
    public String calcularFechaVencimiento(String fecha, String plazo, String periodoPlazo) {

        String fechaVencimiento = null;
        //Vamos a calcular a fecha de vencimiento

        String[] dataTemp = fecha.split("/");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1]) - 1, Integer.parseInt(dataTemp[0]));
        if (periodoPlazo.equals("D")) {
            c.add(Calendar.DAY_OF_YEAR, Integer.parseInt(plazo));
        } else {
            if (periodoPlazo.equals("M")) {
                c.add(Calendar.MONTH, Integer.parseInt(plazo));
            } else {
                c.add(Calendar.YEAR, Integer.parseInt(plazo));
            }
        }
        fechaVencimiento = sdf.format(c.getTime());
        return fechaVencimiento;
    }

    public Vector CargarCamposVer(String[] params, GeneralValueObject gVO) {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Vector listaCampos = new Vector();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            String tipo_campo = (String) gVO.getAtributo("tipo_campo");
            String opcion_pr = (String) gVO.getAtributo("opcion_pr");
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            
            String tramite = "";
            String sql = "";
            String nombre_campo = "";

            if (tipo_campo.equals("8")) {
                tipo_campo = "1";
            } else if (tipo_campo.equals("9")) {
                tipo_campo = "3";
            }


            if ("TRAM".equals(opcion_pr)) {
                tramite = (String) gVO.getAtributo("tramite");
            }


            if ("PROC".equals(opcion_pr) || "".equals(tramite)) {
                sql = " SELECT PCA_COD AS CAMPO, '-' AS TRAMITE  "
                        + " FROM E_PCA "
                        + " WHERE PCA_MUN = " + codMunicipio
                        + " AND PCA_PRO = '" + codProcedimiento + "'"
                        + " AND PCA_TDA = " + tipo_campo
                        + " AND PCA_ACTIVO = 'SI' ";
            } else {
                sql = " SELECT PCA_COD AS CAMPO, '-' AS TRAMITE  "
                        + " FROM E_PCA "
                        + " WHERE PCA_MUN = " + codMunicipio
                        + " AND PCA_PRO = '" + codProcedimiento + "'"
                        + " AND PCA_TDA = " + tipo_campo
                        + " AND PCA_ACTIVO = 'SI' "
                        + " UNION ALL "
                        + " SELECT TCA_COD AS CAMPO ,'" + tramite + "' AS TRAMITE "
                        + " FROM E_TCA "
                        + " WHERE TCA_MUN = " + codMunicipio
                        + " AND TCA_PRO = '" + codProcedimiento + "'"
                        + " AND TCA_TDA = " + tipo_campo
                        + " AND TCA_TRA = " + tramite
                        + " AND TCA_ACTIVO = 'SI'";
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                nombre_campo = rs.getString("CAMPO").trim();
                DefinicionCampoValueObject campos = new DefinicionCampoValueObject();
                campos.setCodCampo(nombre_campo);
                campos.setCodTipoDato(tipo_campo);
                campos.setCodTramite(rs.getString("TRAMITE").trim());

                listaCampos.addElement(campos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
                if(con!=null) con.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaCampos;
    }

    public String CargarCamposExt(String[] params) {
    /*
     * @Descripcion --> Recuperacion de valores de los posibles combos externos definidos en la aplicacion. 
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String listaExt = new String(); 
        String codigo = new String();
        String descrip = new String();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            String sql = "";

            sql = " SELECT CODIGO, DESCRIPCION "
                    + " FROM DESPLEGABLE_EXTERNO ";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                codigo = rs.getString("CODIGO").trim();
                descrip = rs.getString("DESCRIPCION").trim();     
                descrip = descrip.replace("||"," ");
                if ("".equals(listaExt)) {
                    listaExt = codigo + "||" + descrip;
                } else {
                    listaExt = listaExt + "#" + codigo + "||" + descrip;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();                
                if (con!=null) con.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaExt;
    }
    
    
    
    
    /**
     * Permite recuperar un determinado campo suplementario de tipo fichero definido a nivel de expediente
     * @param codigo: Código del campo suplementario
     * @param codMunicipio: Código de la organización/Municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número del expediente
     * @param connection: Conexión a la BBDD
     * @return
     * @throws TechnicalException 
     */    
    public CampoSuplementarioFicheroVO getCampoSuplementarioFicheroExpediente(String codigo, String codMunicipio, 
             String ejercicio, String numero, boolean expHistorico, Connection con) throws TechnicalException {
        CampoSuplementarioFicheroVO campo = null;
        byte[] fichero = null;                
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            if (expHistorico)
                sql = "SELECT TFI_VALOR,TFI_MIME,TFI_NOMFICH FROM HIST_E_TFI WHERE TFI_MUN=" + codMunicipio + 
                       " AND TFI_EJE=" + ejercicio + " AND TFI_NUM='" + numero + "' AND TFI_COD='" + codigo + "'";
            else
                sql = "SELECT TFI_VALOR,TFI_MIME,TFI_NOMFICH FROM E_TFI WHERE TFI_MUN=" + codMunicipio + 
                       " AND TFI_EJE=" + ejercicio + " AND TFI_NUM='" + numero + "' AND TFI_COD='" + codigo + "'";
                
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);            
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                InputStream st = rs.getBinaryStream("TFI_VALOR");
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read()) != -1) {
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
                
                ot.close();
                st.close();
                
                campo = new CampoSuplementarioFicheroVO();
                campo.setTipoMime(rs.getString("TFI_MIME"));
                campo.setNombreFichero(rs.getString("TFI_NOMFICH"));
                campo.setContenido(fichero);
                campo.setNumExpediente(numero);
                campo.setCampoDeExpediente(true);
                campo.setCodMunicipio(codMunicipio);
                campo.setCodigoCampo(codigo);
                campo.setEjercicio(ejercicio);
                
            }
        } catch (SQLException e) {
            m_Log.error("getCampoSuplementarioFicheroExpediente(). ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            throw new TechnicalException("ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            
        } catch (Exception e) {
            m_Log.error("getCampoSuplementarioFicheroExpediente(). ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            throw new TechnicalException("ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            
        }
        finally {
            try {                
                
                if(rs!=null) rs.close();                
                if (stmt!= null) stmt.close();                                
                
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return campo;
    }
     
     
     
     /**
     * Permite recuperar un determinado campo suplementario de tipo fichero definido a nivel de expediente
     * @param codigo: Código del campo suplementario
     * @param codMunicipio: Código de la organización/Municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número del expediente
     * @param connection: Conexión a la BBDD
     * @return
     * @throws TechnicalException 
     */    
     public CampoSuplementarioFicheroVO getCampoSuplementarioFicheroTramite(String codigo, 
             String codMunicipio, String ejercicio, String numero,int codTramite,int ocurrenciaTramite, 
             boolean expHistorico, Connection con) throws TechnicalException {
        CampoSuplementarioFicheroVO campo = null;
        byte[] fichero = null;                
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            if (expHistorico) 
                sql = "SELECT TFIT_VALOR,TFIT_MIME,TFIT_NOMFICH FROM HIST_E_TFIT WHERE TFIT_MUN=" + codMunicipio;
            else
                sql = "SELECT TFIT_VALOR,TFIT_MIME,TFIT_NOMFICH FROM E_TFIT WHERE TFIT_MUN=" + codMunicipio;
             
            sql = sql + " AND TFIT_EJE=" + ejercicio + " AND TFIT_NUM='" + numero + "' AND TFIT_COD='" + codigo + "' " + 
                   " AND TFIT_TRA=" + codTramite + " AND TFIT_OCU=" + ocurrenciaTramite;
            
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);            
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                InputStream st = rs.getBinaryStream("TFIT_VALOR");
                ByteArrayOutputStream ot = new ByteArrayOutputStream();
                int c;
                while ((c = st.read()) != -1) {
                    ot.write(c);
                }
                ot.flush();
                fichero = ot.toByteArray();
                
                ot.close();
                st.close();
                
                campo = new CampoSuplementarioFicheroVO();
                campo.setTipoMime(rs.getString("TFIT_MIME"));
                campo.setNombreFichero(rs.getString("TFIT_NOMFICH"));
                campo.setContenido(fichero);
                campo.setNumExpediente(numero);
                campo.setCampoDeExpediente(true);
                campo.setCodMunicipio(codMunicipio);
                campo.setCodigoCampo(codigo);
                campo.setEjercicio(ejercicio);
                campo.setCodTramite(codTramite);
                campo.setOcurrenciaTramite(ocurrenciaTramite);
                campo.setCampoDeExpediente(false);
                
            }// while
        } catch (SQLException e) {
            m_Log.error("getCampoSuplementarioFicheroTramite(). ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            throw new TechnicalException("ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            
        } catch (Exception e) {
            m_Log.error("getCampoSuplementarioFicheroTramite(). ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            throw new TechnicalException("ERROR al recuperar el valor de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());
            
        }
        finally {
            try {                
                
                if(rs!=null) rs.close();                
                if (stmt!= null) stmt.close();                                
                
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return campo;
    } 
    
    
     
     /**
     * Permite eliminar un documento de un determinado campo suplementario de tipo fichero definido a nivel de expediente
     * @param doc: Documento
     * @param con: Conexion con BD
     * @return boolean: Indicando si la eliminación se realizó correctamente
     * @throws TechnicalException 
     */    
     public boolean eliminarDocumentoDatosSuplementarios(Documento doc,Connection con)throws TechnicalException{
        PreparedStatement ps = null;
        boolean exito = false;

        try{          

            Long idMetadatos = getDocumentoIdMetadatoExpediente(
                    doc.getNumeroDocumento(), String.valueOf(doc.getEjercicio()), String.valueOf(doc.getCodMunicipio()), doc.getNumeroExpediente(), con);

            String sql = "DELETE FROM E_TFI WHERE TFI_MUN=? AND TFI_EJE=? AND TFI_NUM=? AND TFI_COD=?";

            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setString(i++, doc.getNumeroDocumento());

            int rowsDeleted = ps.executeUpdate();
            if(rowsDeleted==1) {
                if (idMetadatos != null) {
                    DocumentoDAO.getInstance().eliminarMetadato(idMetadatos, con);
                } 
                exito = true;
            }

        }catch(SQLException e){
            if (m_Log.isErrorEnabled()) {m_Log.error("eliminarDocumentoDatosSuplementarios(). ERROR al eliminar el documento de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());}
            e.printStackTrace();            
            throw new TechnicalException("ERROR al eliminar el documento de un campo suplementario de tipo fichero definido a nivel de expediente: " + e.getMessage());            
        }finally{
            try{
                if(ps!=null) {ps.close();}
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }

     
   /**
     * Permite eliminar un documento de un determinado campo suplementario de tipo fichero definido a nivel de tramite
     * @param doc: Documento
     * @param con: Conexion con BD
     * @return boolean: Indicando si la eliminación se realizó correctamente
     * @throws TechnicalException 
     */    
     public boolean eliminarDocumentoDatosSuplementariosTramite(Documento doc,Connection con)throws TechnicalException{
        PreparedStatement ps = null;
        boolean exito = false;

        try{          
            Long idMetadatos = getDocumentoIdMetadatoTramite(
                    doc.getNumeroDocumento(), doc.getEjercicio(), doc.getCodMunicipio(),
                    doc.getNumeroExpediente(), doc.getCodTramite(), doc.getOcurrenciaTramite(), con);
            

            String sql = "DELETE FROM E_TFIT WHERE TFIT_MUN=? AND TFIT_EJE=? AND TFIT_NUM=? AND TFIT_COD=? AND TFIT_TRA=? AND TFIT_OCU=?";

            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setString(i++, doc.getNumeroDocumento());
            ps.setInt(i++,doc.getCodTramite());
            ps.setInt(i++, doc.getOcurrenciaTramite());

            int rowsDeleted = ps.executeUpdate();
             if(rowsDeleted==1) {
                if (idMetadatos != null) {
                    DocumentoDAO.getInstance().eliminarMetadato(idMetadatos, con);
                }
                exito = true;
            }

        }catch(SQLException e){
            if (m_Log.isErrorEnabled()) {m_Log.error("eliminarDocumentoDatosSuplementariosTramite(). ERROR al eliminar el documento de un campo suplementario de tipo fichero definido a nivel de tramite: " + e.getMessage());}
            e.printStackTrace();            
            throw new TechnicalException("ERROR al eliminar el documento de un campo suplementario de tipo fichero definido a nivel de tramite: " + e.getMessage());            
        }finally{
            try{
                if(ps!=null) {ps.close();}
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }

     
     
    /**
     * Graba un dato suplementario de tipo fichero definido a nivel de expediente. Se almacena en la tabla E_TFI
     * @param doc: Datos del documento
     * @param con: Conexión a la base de datos
     * @return
     * @throws TechnicalException Grabar 
     */
    public boolean grabarDocumentoDatosSuplementarioFicheroExpediente(Documento doc,Connection con) throws TechnicalException{

        boolean exito = false;
        Statement stmt = null;
        PreparedStatement ps = null;
        String sql = null;
        DocumentoDAO documentoDAO = DocumentoDAO.getInstance();
        
        
        try {
                        
            String codMunicipio = String.valueOf(doc.getCodMunicipio());
            String ejercicio = String.valueOf(doc.getEjercicio());
            String numeroExpediente = doc.getNumeroExpediente();
            byte[] valorDato = doc.getFichero();
            String nombre = doc.getNombreDocumento();
            String mime = doc.getTipoMimeContenido();
            String codTipoDato = doc.getCodTipoDato();
            int longitud = doc.getLongitudDocumento();

             String[] params = doc.getParams();
            
            // Metadatos del documento
            MetadatosDocumentoVO metadatos = null;
            Long idMetadatosNuevo = null;
            Boolean insertarMetadatoEnBBDD = doc.isInsertarMetadatosEnBBDD();
            
            // Nos quedamos con el id de los metadatos para poder borrarlo
            Long idMetadatoAntiguo = getDocumentoIdMetadatoExpediente(
                    codTipoDato, ejercicio, codMunicipio, numeroExpediente, con); 
            

            sql = "DELETE FROM E_TFI WHERE " + tfi_mun + "=" + codMunicipio + " AND " + tfi_eje + "="
                  + ejercicio + " AND " + tfi_num + "='" + numeroExpediente + "' AND " + tfi_cod + "='" + codTipoDato + "'";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            stmt = con.createStatement();
            int rowsDeleted = stmt.executeUpdate(sql);
            
            stmt.close();

            
                 // Borrar metadatos
            if (idMetadatoAntiguo != null) {
                documentoDAO.eliminarMetadato(idMetadatoAntiguo, con);
            }

                if (!nombre.equals("")) {
                    // Insertamos los metadatos
                    if (Boolean.TRUE.equals(insertarMetadatoEnBBDD)) {
                        metadatos = new MetadatosDocumentoVO();
                        metadatos.setCsv(doc.getMetadatoDocumentoCsv());
                        metadatos.setCsvAplicacion(doc.getMetadatoDocumentoCsvAplicacion());
                        metadatos.setCsvUri(doc.getMetadatoDocumentoCsvUri());
                        idMetadatosNuevo = documentoDAO.insertarMetadatoCSV(metadatos, con, params);
                    }
                                    
                    sql = "INSERT INTO E_TFI ( " + tfi_mun + "," + tfi_eje + "," + tfi_num + "," + tfi_cod + ","
                            + tfi_valor + "," + tfi_mime + "," + tfi_nom_fich + ",TFI_TAMANHO, TFI_ID_METADATO) VALUES (" + codMunicipio + "," + ejercicio + ",'" 
                            + numeroExpediente + "','" + codTipoDato  + "',?,'" + mime + "','" + nombre + "'," + longitud + "," + idMetadatosNuevo + ")";
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(sql);
                    }

                    ps = con.prepareStatement(sql);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(valorDato);
                    }

                    
                    if (valorDato != null && valorDato.length>0) {
                       InputStream st = new ByteArrayInputStream(valorDato);                        
                       ps.setBinaryStream(1, st, valorDato.length);
                    }else
                       ps.setNull(1,java.sql.Types.BINARY); 
                    
                    int rowsInserted = ps.executeUpdate();                    
                    if(rowsInserted==1) exito = true;                                        
                
              } else {
                    exito = true;
               }
            

        } catch (Exception e) {
            exito = false;
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}
            e.printStackTrace();
            
        } finally {
            try {
                if(stmt!=null) stmt.close();
                if(ps!=null) ps.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return exito;
    }
    
    
  public GeneralValueObject getInfoCampoSuplementarioFicheroExpediente(int codOrganizacion,
            String codCampo,String numExpediente, boolean expHistorico, Connection con){

        GeneralValueObject salida = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];
            
            String sql; 
            if (expHistorico)        
                sql = "SELECT TFI_NOMFICH,TFI_MIME,PCA_PRO,ORG_DES,PRO_DES FROM HIST_E_TFI,E_PCA,E_PRO, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG ";
            else
                sql = "SELECT TFI_NOMFICH,TFI_MIME,PCA_PRO,ORG_DES,PRO_DES FROM E_TFI,E_PCA,E_PRO, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG ";
                
            sql = sql + "WHERE TFI_MUN=" + codOrganizacion + " AND TFI_COD='" + codCampo + "' AND TFI_NUM='" + numExpediente + "' " +
                             "AND TFI_EJE=" + ejercicio + " AND TFI_COD=PCA_COD AND PCA_MUN=TFI_MUN AND TFI_MUN=A_ORG.ORG_COD AND PCA_MUN = A_ORG.ORG_COD " + 
                             "AND PCA_PRO=PRO_COD AND PCA_MUN=PRO_MUN AND PCA_PRO='" + codProcedimiento + "'";

            
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                String nombreFichero = rs.getString("TFI_NOMFICH");
                String tipoMime      = rs.getString("TFI_MIME");                
                String descOrganizacion   = rs.getString("ORG_DES");
                String descProcedimiento  = rs.getString("PRO_DES");
                
                salida = new GeneralValueObject();
                salida.setAtributo("numeroExpediente",numExpediente);
                salida.setAtributo("ejercicio",ejercicio);
                salida.setAtributo("codProcedimiento",codProcedimiento);
                salida.setAtributo("codOrganizacion",codOrganizacion);
                salida.setAtributo("codCampo",codCampo);
                salida.setAtributo("nombreFichero",nombreFichero);
                salida.setAtributo("tipoMime",tipoMime);
                salida.setAtributo("descOrganizacion",descOrganizacion);
                salida.setAtributo("descProcedimiento",descProcedimiento);
                
            }
            
        }catch(SQLException e){
            m_Log.error("Error al recuperar la información de un campo suplementario de tipo fichero a nivel de expediente: " + e.getMessage());
        }finally{
            
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return salida;
    }
    
    
    /**
     * Recupera el nombre de un campo suplementario de tipo fichero definido a nivel de expediente
     * @param codOrganizacion: Código de la organización/municipio
     * @param ejercicio: Ejercicio
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo
     * @param con: Conexión a la BBDD
     * @return String
     */
    public String getNombreCampoSuplementarioFicheroExpediente(String codOrganizacion,String ejercicio,String numExpediente,String codCampo,Connection con){

        String salida = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            
            String sql = "SELECT TFI_NOMFICH FROM E_TFI " +
                         "WHERE TFI_MUN=" + codOrganizacion + " AND TFI_COD='" + codCampo + "' AND TFI_NUM='" + numExpediente + "' " +
                         "AND TFI_EJE=" + ejercicio;
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                salida = rs.getString("TFI_NOMFICH");               
            }
            
        }catch(SQLException e){
            m_Log.error("Error al recuperar la información de un campo suplementario de tipo fichero a nivel de expediente: " + e.getMessage());
        }finally{
            
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return salida;
    }
    
    
    
    /* Recupera el nombre de un campo suplementario de tipo fichero definido a nivel de trámite
     * @param codOrganizacion: Código de la organización/municipio
     * @param ejercicio: Ejercicio
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo
     * @param con: Conexión a la BBDD
     * @return String
     */
    public String getNombreCampoSuplementarioFicheroTramite(int codOrganizacion,int codTramite,int ocurrenciaTramite,int ejercicio,String numExpediente,String codCampo,Connection con){

        String salida = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            
            String sql = "SELECT TFIT_NOMFICH FROM E_TFIT " +
                         "WHERE TFIT_MUN=" + codOrganizacion + " AND TFIT_COD='" + codCampo + "' AND TFIT_NUM='" + numExpediente + "' " +
                         "AND TFIT_EJE=" + ejercicio + " AND TFIT_TRA=" + codTramite + " AND TFIT_OCU=" + ocurrenciaTramite;
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                salida = rs.getString("TFIT_NOMFICH");               
            }
            
        }catch(SQLException e){
            m_Log.error("Error al recuperar la información de un campo suplementario de tipo fichero a nivel de expediente: " + e.getMessage());
        }finally{
            
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return salida;
    }


  /**
     * Esta operaci\F3n es llamada para almacenar un archivo en un campo suplementario de tipo fichero a nivel
     * de tr\E1mite
     * @param codCampo: Codigo del campo suplementario
     * @param gVO: Objeto de tipo GeneralValueObject con los datos del fichero a dar de alta
     * @param tEVO: Objeto de tipo TramitacionExpedientesValueObject que contiene la info del tr\E1mite sobre
     * el que se est\E1n grabando los campos suplementarios de tipo fichero     
     * @param con: Conexi\F3n a la BBDD
     * @return 
     * @throws es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException 
     */
    public boolean setDatoFicheroCampoSuplementarioTramitePluginDocumentos(String codCampo,
            GeneralValueObject gVO, TramitacionExpedientesValueObject tEVO, Connection con)
            throws AlmacenDocumentoTramitacionException, Exception {
    
        return setDatoFicheroCampoSuplementarioTramitePluginDocumentos(codCampo, gVO, tEVO, con, null);
    }   
    
    
    
   /**
     * Esta operación es llamada para almacenar un archivo en un campo suplementario de tipo fichero a nivel
     * de trámite
     * @param codCampo: Codigo del campo suplementario
     * @param gVO: Objeto de tipo GeneralValueObject con los datos del fichero a dar de alta
     * @param tEVO: Objeto de tipo TramitacionExpedientesValueObject que contiene la info del trámite sobre
     * el que se están grabando los campos suplementarios de tipo fichero     
     * @param con: Conexión a la BBDD
     * @return 
     */
     public boolean setDatoFicheroCampoSuplementarioTramitePluginDocumentos(String codCampo, 
            GeneralValueObject gVO,TramitacionExpedientesValueObject tEVO,Connection con, String[] params) 
            throws AlmacenDocumentoTramitacionException, Exception {

        boolean exito = false;
        
        String mime = "";
        String nombre = "";
        
        String codOrganizacion = tEVO.getCodMunicipio();
        String codProcedimiento = tEVO.getCodProcedimiento();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrenciaTramite = tEVO.getOcurrenciaTramite();
        
        nombre = (String) gVO.getAtributo(codCampo + "_NOMBRE");
        mime = (String) gVO.getAtributo(codCampo + "_TIPO");            
        Integer estadoFichero = (Integer)gVO.getAtributo(codCampo + "_ESTADO");
        String rutaFichero = (String)gVO.getAtributo(codCampo + "_RUTA");
        MetadatosDocumentoVO metadatos = (MetadatosDocumentoVO)gVO.getAtributoONulo(codCampo + "_METADATOS");
               
        //Obtiene la implementacion del plugin correspondiente
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(String.valueOf(codOrganizacion));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
       
        int tipoDocumento = -1;
        String descripcionOrganizacion = null;
        
        if(!almacen.isPluginGestor()){
            //Plugin BBDDD
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        }else{
            //Plugin Gestor Alfresco. Solo inserta los docuemntos en la BD de Flexia
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
        }
        
                
        try{
            if(estadoFichero!=null && estadoFichero.intValue()==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){                 
            // Si se trata de un nuevo elemento, se almacena en la lista de documentos
            // a dar de alta, y se lee su contenido 
            
                byte[] fichero = null;                    
                try{
                    File f = new File(rutaFichero);
                    fichero = FileOperations.readFile(f);

                }catch(Exception e){
                    e.printStackTrace();
                }

                Hashtable<String,Object> datos = new Hashtable<String,Object>();                
                if (params != null) {
                    datos.put("params", params);
                }
                datos.put("fichero",fichero);
                datos.put("codMunicipio",codOrganizacion);
                datos.put("codOrganizacion",codOrganizacion);
                datos.put("nombreDocumento",nombre);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numeroExpediente);
                datos.put("tipoMime",mime);
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(mime));
                datos.put("longitudDocumento",fichero.length);
                datos.put("codTipoDato",codCampo);
                datos.put("codTramite",codTramite);
                datos.put("ocurrenciaTramite",ocurrenciaTramite);
                datos.put("codProcedimiento",codProcedimiento);
                datos.put("nombreFicheroCompleto",codCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" + nombre);
                putMetadatosDocumentoEnParametros(datos, metadatos);

                if(almacen.isPluginGestor()){
                    //  Si se trata de un plugin de un gestor documental, se pasa la información
                    // extra necesaria                                    
                    ResourceBundle config = ResourceBundle.getBundle("documentos");                   
                    String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);                    

                    String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
                    descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion), con);
                    
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                    listaCarpetas.add(numeroExpediente.replaceAll("/","-"));
                    datos.put("listaCarpetas",listaCarpetas);
                }

                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
                
                if(doc!=null){
                    
                    exito = almacen.setDocumentoDatoSuplementarioTramite(doc,con);
                                        
                    // Se procede a eliminar los ficheros nuevos de la anotación que residen en el disco del servidor
                    // ya que se trata de un documento con estado NUEVO. 
                    if(rutaFichero!=null && !"".equals(rutaFichero)){
                        try{
                            FileOperations.deleteFile(rutaFichero);

                        }catch(Exception e){                        
                            m_Log.error("Error al borrar el documento situado en la ruta: " + rutaFichero + " correspondiente a un campo suplementario fichero " + codCampo + " del expediente: " + numeroExpediente);
                            e.printStackTrace();
                        }
                    }
                }                
            }        
        } catch(TechnicalException e){
            exito =false;
            throw new AlmacenDocumentoTramitacionException(0,"Error al recuperar la descripción del procedimiento: " + e.getMessage());
        } catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            throw e;            
        } catch(Exception e){
            exito =false;
            throw new AlmacenDocumentoTramitacionException(0,"Error al recuperar la descripción del procedimiento: " + e.getMessage());
        }
        return exito;
    }
    
    
    
   /**
     *  Esta operación es llamada para eliminar un documento almacenado en un campo suplementario de tipo fichero
     * definido a nivel de trámite
     * de expediente     
     * @param codCampo: Codigo del campo suplementario
     * @param gVO: Objeto de tipo GeneralValueObject con los datos del fichero a eliminar
     * @param con: Conexión a la BBDD
     * @return 
     */
    public boolean eliminarDatoFicheroCampoSuplementarioTramitePluginDocumentos(String codCampo, GeneralValueObject gVO,TramitacionExpedientesValueObject tEVO,Connection con) throws AlmacenDocumentoTramitacionException{

        boolean exito = false;
        Statement stmt = null;                        
        String mime = "";
        String nombre = "";
        
        String codOrganizacion = tEVO.getCodMunicipio();
        String ejercicio = tEVO.getEjercicio();
        String numeroExpediente = tEVO.getNumeroExpediente();
        String codTramite = tEVO.getCodTramite();
        String ocurrenciaTramite = tEVO.getOcurrenciaTramite();        
        String codProcedimiento = tEVO.getCodProcedimiento();
        
        nombre = (String) gVO.getAtributo(codCampo + "_NOMBRE");
        mime = (String) gVO.getAtributo(codCampo + "_TIPO");                    
        String rutaFichero = (String)gVO.getAtributo(codCampo + "_RUTA");
        
               
        //Obtiene la implementacion del plugin correspondiente
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(String.valueOf(codOrganizacion));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
        ArrayList<Documento> docsReg = new ArrayList<Documento>();
        int tipoDocumento = -1;
        String descripcionOrganizacion = null;
        
        if(!almacen.isPluginGestor()){
            //Plugin BBDDD
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        }else{
            //Plugin Gestor Alfresco
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
        }
        
                
        try{
            
            Hashtable<String,Object> datos = new Hashtable<String,Object>();                            
            datos.put("codMunicipio",codOrganizacion);
            datos.put("codOrganizacion",codOrganizacion);
            datos.put("nombreDocumento",nombre);
            datos.put("ejercicio",ejercicio);
            datos.put("numeroExpediente",numeroExpediente);
            datos.put("tipoMime",mime);
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(mime));            
            datos.put("codTipoDato",codCampo);
            datos.put("numeroDocumento",codCampo);
            datos.put("rutaDocumento",rutaFichero);
            datos.put("codTramite",codTramite);
            datos.put("ocurrenciaTramite",ocurrenciaTramite);
            

            if(almacen.isPluginGestor()){
                //  Si se trata de un plugin de un gestor documental, se pasa la información
                // extra necesaria                                    
                ResourceBundle config = ResourceBundle.getBundle("documentos");            
                String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                
                descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion),con);
                String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
                
                String nombreFichero = DatosSuplementariosDAO.getInstance().getNombreCampoSuplementarioFicheroTramite(Integer.parseInt(codOrganizacion),Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),Integer.parseInt(ejercicio),numeroExpediente,codCampo,con);
                datos.put("nombreDocumento", codCampo + "_" + nombreFichero);                
                datos.put("nombreFicheroCompleto",codCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" + nombreFichero);
                
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                listaCarpetas.add(numeroExpediente.replaceAll("/","-"));
                datos.put("listaCarpetas",listaCarpetas);
            }

            Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
                
            if(doc!=null){

                exito = almacen.eliminarDocumentoDatosSuplementariosTramite(doc, con);
                if(exito){
                    
                    if(rutaFichero!=null && !"".equals(rutaFichero)){
                        try{
                            // Si el fichero ha sido subido con estado NUEVO y no ha sido eliminado de disco, se procede
                            // a su borrado
                            FileOperations.deleteFile(rutaFichero);

                        }catch(Exception e){
                            e.printStackTrace();
                            throw new AlmacenDocumentoTramitacionException(4,e.getMessage());
                        }
                    }
                }
            }                
                 
        }catch(TechnicalException e){
            throw new AlmacenDocumentoTramitacionException(0,"Error al recuperar la descripción del procedimiento: " + e.getMessage());
        }
        catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            throw e;            
        }
       
        return exito;
    }

    
    
     /**
     * Graba un dato suplementario de tipo fichero definido a nivel de trámite. Se almacena en la tabla E_TFIT
     * @param doc: Datos del documento
     * @param con: Conexión a la base de datos
     * @return
     * @throws TechnicalException Grabar 
     */
    public boolean grabarDocumentoDatosSuplementarioFicheroTramite(Documento doc,Connection con) throws TechnicalException{

        boolean exito = false;
        Statement stmt = null;
        PreparedStatement ps = null;
        String sql = null;
        
        try {
                        
            String codMunicipio = String.valueOf(doc.getCodMunicipio());
            String ejercicio = String.valueOf(doc.getEjercicio());
            String numeroExpediente = doc.getNumeroExpediente();
            byte[] valorDato = doc.getFichero();
            String nombre = doc.getNombreDocumento();
            String mime = doc.getTipoMimeContenido();
            String codTipoDato = doc.getCodTipoDato();

            sql = "DELETE FROM E_TFIT WHERE TFIT_MUN=" + tfi_mun + "=" + codMunicipio + " AND " + tfi_eje + "="
                  + ejercicio + " AND " + tfi_num + "='" + numeroExpediente + "' AND " + tfi_cod + "='" + codTipoDato + "'";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            stmt = con.createStatement();
            int rowsDeleted = stmt.executeUpdate(sql);
            
            stmt.close();

            
                if (!nombre.equals("")) {
                    sql = "INSERT INTO E_TFI ( " + tfi_mun + "," + tfi_eje + "," + tfi_num + "," + tfi_cod + ","
                            + tfi_valor + "," + tfi_mime + "," + tfi_nom_fich + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" 
                            + numeroExpediente + "','" + codTipoDato  + "',?,'" + mime + "','" + nombre + "')";
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(sql);
                    }

                    ps = con.prepareStatement(sql);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(valorDato);
                    }

                    
                    if (valorDato != null && valorDato.length>0) {
                       InputStream st = new ByteArrayInputStream(valorDato);                        
                       ps.setBinaryStream(1, st, valorDato.length);
                    }else
                       ps.setNull(1,java.sql.Types.BINARY); 
                    
                    int rowsInserted = ps.executeUpdate();                    
                    if(rowsInserted==1) exito = true;                                        
                
               }
            

        } catch (Exception e) {
            exito = false;
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}
            e.printStackTrace();
            
        } finally {
            try {
                if(stmt!=null) stmt.close();
                if(ps!=null) ps.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return exito;
    }
    
    
    
    
     public GeneralValueObject getInfoCampoSuplementarioFicheroTramite(int codOrganizacion,int codTramite,
             int ocurrenciaTramite,String codCampo,String numExpediente,boolean expHistorico,Connection con){

        GeneralValueObject salida = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];
            String sql;
            
            if (expHistorico)
                sql = "SELECT TFIT_NOMFICH,TFIT_MIME,TCA_PRO,ORG_DES,PRO_DES FROM HIST_E_TFIT,E_TCA,E_PRO, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG ";
            else
                sql = "SELECT TFIT_NOMFICH,TFIT_MIME,TCA_PRO,ORG_DES,PRO_DES FROM E_TFIT,E_TCA,E_PRO, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG ";

            sql = sql + "WHERE TFIT_MUN=" + codOrganizacion + " AND TFIT_COD='" + codCampo + "' AND TFIT_NUM='" + numExpediente + "' " +
                         "AND TFIT_TRA=" + codTramite + " AND TFIT_OCU=" + ocurrenciaTramite + " " + 
                         "AND TFIT_EJE=" + ejercicio + " AND TFIT_COD=TCA_COD AND TCA_MUN=TFIT_MUN AND TFIT_MUN=A_ORG.ORG_COD AND TCA_MUN = A_ORG.ORG_COD " + 
                         "AND TCA_PRO=PRO_COD AND TCA_MUN=PRO_MUN AND TCA_PRO='" + codProcedimiento + "'";
                        
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                String nombreFichero = rs.getString("TFIT_NOMFICH");
                String tipoMime      = rs.getString("TFIT_MIME");                
                String descOrganizacion   = rs.getString("ORG_DES");
                String descProcedimiento  = rs.getString("PRO_DES");
                
                salida = new GeneralValueObject();
                salida.setAtributo("numeroExpediente",numExpediente);
                salida.setAtributo("ejercicio",ejercicio);
                salida.setAtributo("codProcedimiento",codProcedimiento);
                salida.setAtributo("codOrganizacion",codOrganizacion);
                salida.setAtributo("codCampo",codCampo);
                salida.setAtributo("nombreFichero",nombreFichero);
                salida.setAtributo("tipoMime",tipoMime);
                salida.setAtributo("descOrganizacion",descOrganizacion);
                salida.setAtributo("descProcedimiento",descProcedimiento);
                
            }
            
        }catch(SQLException e){
            m_Log.error("Error al recuperar la información de un campo suplementario de tipo fichero a nivel de expediente: " + e.getMessage());
        }finally{
            
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return salida;
    }
     
     
     

   /**
     * Permite eliminar todos y cada uno de los archivos almacenados en campos suplementarios de fichero
     * para un determinado trámite, de un determinado expediente
     * @param doc: Documento
     * @param con: Conexion con BD
     * @return boolean: Indicando si la eliminación se realizó correctamente
     * @throws TechnicalException 
     */    
     public boolean eliminarTodosDocumentoDatosSuplementariosTramite(Documento doc,Connection con) throws TechnicalException{
        PreparedStatement ps = null;
        boolean exito = false;

        try{          
            // Obtenemos los ids de los metadatos a borrar
            ArrayList<Long> listaIdMetadatos = getDocumentosIdMetadatoTramite(doc.getCodProcedimiento(),
                    doc.getEjercicio(), doc.getCodMunicipio(), doc.getNumeroExpediente(),
                    doc.getCodTramite(), doc.getOcurrenciaTramite(), con);
                     

            String sql = "DELETE FROM E_TFIT WHERE TFIT_MUN=? AND TFIT_EJE=? AND TFIT_NUM=? AND TFIT_TRA=? AND TFIT_OCU=? AND TFIT_PRO=?";
            m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setInt(i++,doc.getCodTramite());
            ps.setInt(i++, doc.getOcurrenciaTramite());
            ps.setString(i++, doc.getCodProcedimiento());

            int rowsDeleted = ps.executeUpdate();
            m_Log.debug("Número de filas eliminadas: " + rowsDeleted);
            // Quitamos los ids nulos antes de borrar los metadatos
            listaIdMetadatos.removeAll(Collections.singleton(null));
            DocumentoDAO.getInstance().eliminarMetadato(listaIdMetadatos, con);
            
            exito = true;

        }catch(SQLException e){
            if (m_Log.isErrorEnabled()) {m_Log.error("eliminarTodosDocumentoDatosSuplementariosTramite(). ERROR al eliminar el documento de un campo suplementario de tipo fichero definido a nivel de tramite: " + e.getMessage());}
            e.printStackTrace();            
            throw new TechnicalException("ERROR los ficheros almacenados en todos los campo suplementarios a nivel de trámite " + doc.getCodTramite() + ", ocurrencia: " + doc.getOcurrenciaTramite() + " del expediente: "  + doc.getNumeroExpediente() + ": " + e.getMessage());            
        }finally{
            
            try{
                if(ps!=null) {ps.close();}
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }
     
     
     
     /**
      * Recupera códigos de campos suplementarios de tipo fichero que han sido dados de alta 
      * en un trámite y de un expediente determinado
      * @param doc: Objeto de tipo Documento con la información del trámite y expediente para el cual se recuperan
      * los códigos de los campos de tipo fichero
      * @param con: Conexión a la BBDD
      * @return ArrayList<CampoSuplementarioFicheroVO>
      */
     public ArrayList<CampoSuplementarioFicheroVO> getCodigosCamposSuplementarioFicheroTramite(Documento doc,Connection con) throws TechnicalException{
         ArrayList<CampoSuplementarioFicheroVO> salida = new ArrayList<CampoSuplementarioFicheroVO>();
         Statement st = null;
         ResultSet rs = null;
         
         try{
             
             String sql = "SELECT TFIT_COD,TFIT_NOMFICH FROM E_TFIT WHERE TFIT_MUN=" + doc.getCodMunicipio() + " AND TFIT_PRO='" + doc.getCodProcedimiento() + "' " + 
                          "AND TFIT_EJE=" + doc.getEjercicio() + " AND TFIT_NUM='" + doc.getNumeroExpediente() + "' AND TFIT_TRA=" + doc.getCodTramite() + " " + 
                          "AND TFIT_OCU=" + doc.getOcurrenciaTramite();
             
             st = con.createStatement();
             rs = st.executeQuery(sql);
             
             while(rs.next()){
                 CampoSuplementarioFicheroVO campo = new CampoSuplementarioFicheroVO();
                 campo.setCodigoCampo(rs.getString("TFIT_COD"));
                 campo.setNombreFichero(rs.getString("TFIT_NOMFICH"));
                 campo.setCodMunicipio(Integer.toString(doc.getCodMunicipio()));
                 campo.setCodTramite(doc.getCodTramite());
                 campo.setOcurrenciaTramite(doc.getOcurrenciaTramite());
                 campo.setNumExpediente(doc.getNumeroExpediente());
                 campo.setEjercicio(Integer.toString(doc.getEjercicio()));                 
                 salida.add(campo);
             }
             
         }catch(SQLException e){
             e.printStackTrace();
             throw new TechnicalException("Error al recuparar los códigos correspondientes a campos de tipo fichero de trámite: " + e.getMessage());
             
         }finally{
             try{
                 if(st!=null) st.close();
                 if(rs!=null) rs.close();
                 
             }catch(SQLException e){
                 m_Log.error("Error al cerrar recursos asociados a la conexión de base de datos: " + e.getMessage());
             }
         }         
         return salida;
     }

     
     
      public CamposFormulario getValoresFicheroTramite(AdaptadorSQLBD oad, Connection con, GeneralValueObject gVO, String codTramite,
            String ocurrencia) {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            
            boolean expHistorico = "si".equals((String)gVO.getAtributo("expHistorico"));
            
            String sql;
            
            if (expHistorico)
                sql = "SELECT tfit_cod,tfit_valor,tfit_mime,tfit_nomfich FROM HIST_E_TFIT ";
            else
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "tfit_cod"}) + " AS tfit_cod,tfit_nomfich FROM E_TFIT ";
            
            sql += "WHERE tfit_mun =? AND tfit_pro =? AND tfit_eje =? AND tfit_num =? AND tfit_tra =? AND tfit_ocu =?";
            
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(ocurrencia));
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tfit_cod");
                String valorCampo = rs.getString("tfit_nomfich");
                
                if (expHistorico) {
                    byte [] fichero = rs.getBytes("tfit_valor");
                    
                    campos.put(codCampo, fichero);
                    
                    String mime = rs.getString("tfit_mime");
                    campos.put(codCampo + "_TIPO", mime);
                    campos.put(codCampo + "_NOMBRE", valorCampo);
                } else {
                    campos.put(codCampo, valorCampo);
                }
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
     
     
     
     /****************** NUEVO ****************************/
     public CamposFormulario getValoresNumericos(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
           
            String sql = "SELECT " + tnu_cod + "," + oad.convertir(tnu_valor, oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS " + tnu_valor + 
                        " FROM E_TNU WHERE " + tnu_mun + "=? AND " + tnu_eje + "=? AND " + tnu_num + "=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());           
            rs = ps.executeQuery();
            
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tnu_cod);
                String valorCampo = rs.getString(tnu_valor);
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }
     
     
    
    public ValorCampoSuplementarioVO getValoresNumericos(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {
           
             String sql;
           if (dato.isExpHistorico())
                sql = "SELECT TNU_COD," + oad.convertir("TNU_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNU_VALOR" + 
                             " FROM HIST_E_TNU WHERE TNU_MUN = ? AND TNU_EJE = ? AND TNU_NUM=? AND TNU_COD=?";
           else
                sql = "SELECT " + tnu_cod + "," + oad.convertir(tnu_valor, oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNU_VALOR"  + 
                             " FROM E_TNU WHERE TNU_MUN = ? AND TNU_EJE = ? AND TNU_NUM=? AND TNU_COD=?";
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());           
            ps.setString(i++,codCampo);             
            rs = ps.executeQuery();
            
            
            while (rs.next()) {
                
                valor.setCodDatoSuplementario(codCampo);
                String valorCampo = rs.getString("tnu_valor");
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }                
                valor.setValorDatoSuplementario(valorCampo);      
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_NUMERICO);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
     
     
     
     
     
    public CamposFormulario getValoresNumericosTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,int codTramite,
            int ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
             
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", tnut_cod}) + " AS " + tnut_cod + ","
                    + oad.convertir(tnut_valor, oad.CONVERTIR_COLUMNA_TEXTO, null)
                    + " AS " + tnut_valor + " FROM E_TNUT WHERE " + tnut_mun + "=? AND " + tnut_pro + "=? " + 
                    "AND " + tnut_eje + "=? AND " + tnut_num + "=? AND " + tnut_tra + "=?" + 
                    " AND " + tnut_ocu + "=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);            
            rs = ps.executeQuery();
            
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tnut_cod);
                String valorCampo = rs.getString(tnut_valor);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    } 
    
    
    
    
    public ValorCampoSuplementarioVO getValoresNumericosTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,int codTramite,int ocurrencia,String codCampo) {        
        PreparedStatement ps = null;
        ResultSet rs = null;        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        
        try {
             
           String sql;
            if (dato.isExpHistorico())
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                new String[]{"'T'", "'" + codTramite + "'", "TNUT_COD"}) + " AS TNUT_COD,"
                + oad.convertir("TNUT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null)
                + " AS TNUT_VALOR FROM HIST_E_TNUT WHERE TNUT_MUN = ? AND TNUT_PRO = ? " + 
                "AND TNUT_EJE = ? AND TNUT_NUM = ? AND TNUT_TRA = ?" + 
                " AND TNUT_OCU = ? AND TNUT_COD=?";
            else
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                new String[]{"'T'", "'" + codTramite + "'", "TNUT_COD"}) + " AS TNUT_COD,"
                + oad.convertir("TNUT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null)
                + " AS TNUT_VALOR FROM E_TNUT WHERE TNUT_MUN = ? AND TNUT_PRO = ? " + 
                "AND TNUT_EJE = ? AND TNUT_NUM = ? AND TNUT_TRA = ?" + 
                " AND TNUT_OCU = ? AND TNUT_COD=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);     
            ps.setString(i++,codCampo);
            rs = ps.executeQuery();
            
            while (rs.next()) {        
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TNUT_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_NUMERICO);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    } 
        
    
    
    public CamposFormulario getValoresTexto(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try {
            
            String sql = "SELECT " + txt_cod + "," + txt_valor + " FROM E_TXT WHERE " + txt_mun + "=?"
                    + " AND " + txt_eje + "=?" + " AND " + txt_num + "=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i= 1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            
            rs = ps.executeQuery();
            String entrar = "no";

            HashMap<String, String> campos = new HashMap<String, String>();
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(txt_cod);
                String valorCampo = rs.getString(txt_valor);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }
    
    
    
    public ValorCampoSuplementarioVO getValoresTexto(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {
            
           String sql;
            
            if (dato.isExpHistorico())
                sql = "SELECT TXT_COD,TXT_VALOR FROM HIST_E_TXT WHERE TXT_MUN=?"
                       + " AND TXT_EJE=? AND TXT_NUM=? AND TXT_COD=?";
            else
                sql = "SELECT TXT_COD,TXT_VALOR FROM E_TXT WHERE TXT_MUN=?"
                       + " AND TXT_EJE=? AND TXT_NUM=? AND TXT_COD=?";
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            int i= 1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setString(i++,codCampo);
            
            rs = ps.executeQuery();
            
            
            while (rs.next()) {
                
                valor.setCodDatoSuplementario(rs.getString("TXT_COD"));
                valor.setValorDatoSuplementario(rs.getString("TXT_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_TEXTO);
                
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    
    
    
    
     public ValorCampoSuplementarioVO getValoresTextoTramite(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato,int codTramite,int ocurrencia,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        
        try{            
            
             String sql;
            if (dato.isExpHistorico())
                sql = "SELECT " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TXTT_COD"}) + " AS TXTT_COD,"
                    + "TXTT_VALOR FROM HIST_E_TXTT WHERE TXTT_MUN = ? AND TXTT_PRO = ? AND " + 
                    "TXTT_EJE = ? AND TXTT_NUM = ? AND TXTT_TRA = ? AND TXTT_OCU = ? AND TXTT_COD=?";
            else
                sql = "SELECT " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TXTT_COD"}) + " AS TXTT_COD,"
                    + "TXTT_VALOR FROM E_TXTT WHERE TXTT_MUN = ? AND TXTT_PRO = ? AND " + 
                    "TXTT_EJE = ? AND TXTT_NUM = ? AND TXTT_TRA = ? AND TXTT_OCU = ? AND TXTT_COD=?";
                
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            ps.setString(i++,codCampo);
            
            rs = ps.executeQuery();            
            while (rs.next()) {
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TXTT_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_TEXTO_LARGO);
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
                if (ps!= null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
     
    
     
    public CamposFormulario getValoresFecha(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";

        try {

            if (mascara == null || "".equals(mascara)) {                               
                sql = "SELECT " + tfe_cod + "," + oad.convertir(tfe_valor, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
                        + " AS " + tfe_valor + "," + oad.convertir("TFE_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFE_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO "
                        + " FROM E_TFE WHERE " + tfe_mun + "=? AND " + tfe_eje + "=? AND " + tfe_num + "=?";
                
            } else {               
                sql = "SELECT " + tfe_cod + "," + oad.convertir(tfe_valor, oad.CONVERTIR_COLUMNA_TEXTO, mascara)
                        + " AS " + tfe_valor + "," + oad.convertir("TFE_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFE_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO "
                        + " FROM E_TFE WHERE " + tfe_mun + "=? AND " + tfe_eje + "=?"
                        + " AND " + tfe_num + " =?";                
            }

            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tfe_cod);
                String valorCampo = rs.getString(tfe_valor);
                campos.put(codCampo, valorCampo);
                //fecha Vencimiento
                String codFechVencimiento = "fechaVencimiento" + codCampo;
                String valorFechaVencimiento = rs.getString("TFE_FEC_VENCIMIENTO");
                campos.put(codFechVencimiento, valorFechaVencimiento);
                //plazo activo
                String codPlazoActivo = "activar" + codCampo;
                String valorPlazoActivo = rs.getString(String.valueOf("PLAZO_ACTIVADO"));
                campos.put(codPlazoActivo, valorPlazoActivo);

                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    } 
     
    
    public ValorCampoSuplementarioVO getValoresFecha(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara,EstructuraCampoAgrupado campo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        String sql = "";

        try {
             if (mascara == null || "".equals(mascara)) {                               
                sql = "SELECT TFE_COD," + oad.convertir("TFE_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
                        + " AS TFE_VALOR," + oad.convertir("TFE_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFE_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO ";
            } else {               
                sql = "SELECT TFE_COD," + oad.convertir("TFE_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara)
                        + " AS TFE_VALOR," + oad.convertir("TFE_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFE_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO ";
            }
            if (dato.isExpHistorico())
                sql = sql + " FROM HIST_E_TFE WHERE TFE_MUN = ? AND TFE_EJE = ? AND TFE_NUM = ? AND TFE_COD=?";
            else
                sql = sql + " FROM E_TFE WHERE TFE_MUN = ? AND TFE_EJE = ? AND TFE_NUM = ? AND TFE_COD=?";


            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setString(i++,campo.getCodCampo());
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            rs = ps.executeQuery();
            while (rs.next()) {                              
                String valorCampo = rs.getString("TFE_VALOR");
                campos.put(campo.getCodCampo(), valorCampo);
                //fecha Vencimiento
                String codFechVencimiento = "fechaVencimiento" + campo.getCodCampo();
                String valorFechaVencimiento = rs.getString("TFE_FEC_VENCIMIENTO");
                                
                //plazo activo
                String codPlazoActivo = "activar" + campo.getCodCampo();
                String valorPlazoActivo = rs.getString(String.valueOf("PLAZO_ACTIVADO"));
                valor.setCodDatoSuplementario(codFechVencimiento);
                valor.setValorDatoSuplementario(valorCampo);                
                valor.setCodFechaVencimiento(codFechVencimiento);
                valor.setValorFechaVencimiento(valorFechaVencimiento);
                valor.setCodPlazoActivo(codPlazoActivo);
                valor.setValorPlazoActivo(valorPlazoActivo);
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_FECHA);
                
                
                if(valorFechaVencimiento!=null && !"".equals(valorFechaVencimiento)){                                        
                    valorFechaVencimiento += " 23:59:59";                    
                    Calendar actual = Calendar.getInstance();                                
                    Calendar vencimiento = DateOperations.toCalendar(valorFechaVencimiento,"dd/MM/yyyy HH:mm:ss");
                    valor.setAlarmaVencida(false);
                    if(actual.getTimeInMillis()>vencimiento.getTimeInMillis())                    
                        valor.setAlarmaVencida(true);                    
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }  
    
    
     
    public CamposFormulario getValoresFechaTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara,
            int codTramite, int ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";
        String prefijoCampo = "T_" + codTramite + "_" + ocurrencia + "_";

        try {
          
            if (mascara == null || "".equals(mascara)) {
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", tfet_cod}) + " AS " + tfet_cod
                        + ", " + oad.convertir("TFET_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " + tfet_valor
                        + ", " + oad.convertir("TFET_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO "
                        + " FROM E_TFET WHERE " + tfet_mun + "=? AND " + tfet_pro + "=? AND " + tfet_eje  + "=?"
                        + " AND " + tfet_num + "=? AND " + tfet_tra + "=? AND " + tfet_ocu + "=?";
                
            } else {               
                 sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", tfet_cod}) + " AS " + tfet_cod
                        + "," + oad.convertir(tfet_valor, oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS " + tfet_valor
                        + "," + oad.convertir("TFET_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_FEC_VENCIMIENTO "
                        + ",PLAZO_ACTIVADO "
                        + " FROM E_TFET WHERE " + tfet_mun + "=? AND " + tfet_pro + "=? AND " + tfet_eje + "=?" 
                        + " AND " + tfet_num + "=? AND " + tfet_tra + "=? AND " + tfet_ocu + "=?";                
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i =1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tfet_cod);
                String valorCampo = rs.getString(tfet_valor);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
                //fecha Vencimiento
                String codFechVencimiento = "fechaVencimiento" + codCampo;
                String valorFechaVencimiento = rs.getString("TFET_FEC_VENCIMIENTO");
                campos.put(codFechVencimiento, valorFechaVencimiento);
                //plazo activo
                String codPlazoActivo = "activar" + prefijoCampo + codCampo;
                String valorPlazoActivo = rs.getString(String.valueOf("PLAZO_ACTIVADO"));
                campos.put(codPlazoActivo, valorPlazoActivo);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    } 
    
    
    
    
    public ValorCampoSuplementarioVO getValoresFechaTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara,int codTramite, int ocurrencia,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;        
        String sql = "";
        String prefijoCampo = "T_" + codTramite + "_" + ocurrencia + "_";        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {          
            if (mascara == null || "".equals(mascara)) 
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", "TFET_COD"}) + " AS TFET_COD" 
                        + ", " + oad.convertir("TFET_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_VALOR";
            else                
                 sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", "TFET_COD"}) + " AS TFET_COD" 
                        + "," + oad.convertir("TFET_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS TFET_VALOR";
            
            sql = sql + "," + oad.convertir("TFET_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_FEC_VENCIMIENTO "
                + ",PLAZO_ACTIVADO ";
            if (dato.isExpHistorico())
                sql = sql + " FROM HIST_E_TFET WHERE TFET_MUN = ? AND TFET_PRO = ? AND TFET_EJE = ?";
            else
                sql = sql + " FROM E_TFET WHERE TFET_MUN = ? AND TFET_PRO = ? AND TFET_EJE = ?";
            
            sql = sql + " AND TFET_NUM = ? AND TFET_TRA = ? AND TFET_OCU = ? AND TFET_COD = ?";                

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            int i=1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            ps.setString(i++,codCampo);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                
                String valorCampo = rs.getString(tfet_valor);                                
                //fecha Vencimiento
                String codFechVencimiento = "fechaVencimiento" + codCampo;
                String valorFechaVencimiento = rs.getString("TFET_FEC_VENCIMIENTO");                
                //plazo activo
                String codPlazoActivo = "activar" + prefijoCampo + codCampo;
                String valorPlazoActivo = rs.getString(String.valueOf("PLAZO_ACTIVADO"));
                
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(valorCampo);
                valor.setCodFechaVencimiento(codFechVencimiento);
                valor.setValorFechaVencimiento(valorFechaVencimiento);
                valor.setCodPlazoActivo(codPlazoActivo);
                valor.setValorPlazoActivo(valorPlazoActivo);    
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_FECHA);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    } 
    
    
    
    
     public CamposFormulario getValoresTextoLargo(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = new CamposFormulario(campos);

        try {
            
            String sql = "SELECT " + ttl_cod + "," + ttl_valor + " FROM E_TTL WHERE " + ttl_mun + "=? AND " + 
                    ttl_eje + "=? AND " + ttl_num + "=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            pst = con.prepareStatement(sql);
            pst.setInt(i++,dato.getCodOrganizacion());
            pst.setInt(i++,dato.getEjercicio());
            pst.setString(i++,dato.getNumExpediente());
            
            rs = pst.executeQuery();
            String codCampo = null;
            String valorCampo = null;
            while (rs.next()) {
                valorCampo = new String("");
                codCampo = rs.getString(ttl_cod);
                java.io.Reader cr = rs.getCharacterStream(ttl_valor);
                if (cr == null) {
                    valorCampo = null;
                } else {
                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    valorCampo = ot.toString();
                    ot.close();
                    cr.close();
                }
                if ("".equals(codCampo) || "".equals(valorCampo)) {
                    cF = new CamposFormulario(campos);
                } else {
                    valorCampo=valorCampo.trim();
                    valorCampo = valorCampo.replace("\n", ""); 
                    campos.put(codCampo, valorCampo);
                    cF = new CamposFormulario(campos);
                }
            }

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }
     
    
     
     public ValorCampoSuplementarioVO getValoresTextoLargo(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,String codCampo) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        
        try {
            
          String sql;
            if (dato.isExpHistorico())
                sql = "SELECT TTL_COD,TTL_VALOR FROM HIST_E_TTL " + 
                         "WHERE TTL_MUN=? AND TTL_EJE=? AND TTL_NUM=? AND TTL_COD=?";            
            else
                sql = "SELECT TTL_COD,TTL_VALOR FROM E_TTL " + 
                         "WHERE TTL_MUN=? AND TTL_EJE=? AND TTL_NUM=? AND TTL_COD=?";            
            
           if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            int i=1;
            pst = con.prepareStatement(sql);
            pst.setInt(i++,dato.getCodOrganizacion());
            pst.setInt(i++,dato.getEjercicio());
            pst.setString(i++,dato.getNumExpediente());
            pst.setString(i++,codCampo);            
            rs = pst.executeQuery();
            
            String valorCampo = null;
            while (rs.next()) {
                valorCampo = new String("");
                java.io.Reader cr = rs.getCharacterStream("TTL_VALOR");
                if (cr == null) {
                    valorCampo = null;
                } else {
                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    valorCampo = ot.toString();
                    ot.close();
                    cr.close();
                }
                valorCampo = valorCampo.replace("\n", "");
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(valorCampo);                
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
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    } 
     
     
     
     
     
     
    public CamposFormulario getValoresTextoLargoTramite(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato, int codTramite,
            int ocurrencia) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = new CamposFormulario(campos);

        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("--------------METODO getValoresTextoLargoTramite");
            }
            /*
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", ttlt_cod}) + " AS " + ttlt_cod + ","
                    + ttlt_valor + " FROM E_TTLT WHERE " + ttlt_mun + " = "
                    + codMunicipio + " AND " + ttlt_pro + "='" + codProcedimiento + "' AND " + ttlt_eje
                    + " = " + ejercicio + " AND " + ttlt_num + " ='" + numero + "' AND " + ttlt_tra
                    + "=" + codTramite + " AND " + ttlt_ocu + "=" + ocurrencia; 
                    */
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", ttlt_cod}) + " AS " + ttlt_cod + ","
                    + ttlt_valor + " FROM E_TTLT WHERE " + ttlt_mun + " = "
                    + dato.getCodOrganizacion() + " AND " + ttlt_pro + "='" + dato.getCodProcedimiento() + "' AND " + ttlt_eje
                    + "= " + dato.getEjercicio() + " AND " + ttlt_num + " ='" + dato.getNumExpediente() + "' AND " + ttlt_tra
                    + "=" + codTramite + " AND " + ttlt_ocu + "=" + ocurrencia; 
            
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            String codCampo = null;
            String valorCampo = null;
            while (rs.next()) {
                codCampo = new String("");
                valorCampo = new String("");
            
                codCampo = rs.getString(ttlt_cod);
                Reader reader = rs.getCharacterStream(ttlt_valor);
                char[] cbuf = new char[65536];
                StringBuffer stringbuf = new StringBuffer();
                while (reader.read(cbuf, 0, 65536) != -1) {
                    stringbuf.append(cbuf);
                }//end while
                valorCampo = stringbuf.toString();
                valorCampo=valorCampo.trim();                
//                 codCampo = rs.getString(ttlt_cod);
                if ("".equals(codCampo) || "".equals(valorCampo)) {
                    cF = new CamposFormulario(campos);
                } else {
                    campos.put(codCampo, valorCampo);
                    cF = new CamposFormulario(campos);
                }
            }

        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    } 
    
    
    
    public ValorCampoSuplementarioVO getValoresTextoLargoTramite(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato, int codTramite,int ocurrencia,String codCampo) {
        PreparedStatement pst = null;
        ResultSet rs = null;                
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        
        try {
 
           String sql;
            if (dato.isExpHistorico())
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "TTLT_COD"}) + " AS TTLT_COD,TTLT_VALOR " +
                    " FROM HIST_E_TTLT WHERE TTLT_MUN=? AND TTLT_PRO=? AND TTLT_EJE=? "
                    + "AND TTLT_NUM=? AND TTLT_TRA=? AND TTLT_OCU=? AND TTLT_COD=?";
            else
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", "TTLT_COD"}) + " AS TTLT_COD,TTLT_VALOR " +
                    " FROM E_TTLT WHERE TTLT_MUN=? AND TTLT_PRO=? AND TTLT_EJE=? "
                    + "AND TTLT_NUM=? AND TTLT_TRA=? AND TTLT_OCU=? AND TTLT_COD=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            pst = con.prepareStatement(sql);
                    
            pst.setInt(i++,dato.getCodOrganizacion());
            pst.setString(i++,dato.getCodProcedimiento());
            pst.setInt(i++,dato.getEjercicio());
            pst.setString(i++,dato.getNumExpediente());
            pst.setInt(i++,codTramite);
            pst.setInt(i++,ocurrencia);
            pst.setString(i++,codCampo);
            rs = pst.executeQuery();    
            
            String valorCampo = null;
            while (rs.next()) {
                valorCampo = new String("");
                Reader reader = rs.getCharacterStream("TTLT_VALOR");
                char[] cbuf = new char[65536];
                StringBuffer stringbuf = new StringBuffer();
                while (reader.read(cbuf, 0, 65536) != -1) {
                    stringbuf.append(cbuf);
                }//end while
                valorCampo = stringbuf.toString();
                valorCampo=valorCampo.trim();
                valorCampo = valorCampo.replace("\n", "");
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(valorCampo);
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_TEXTO_LARGO);
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
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return valor;
    } 
    
    
    public CamposFormulario getValoresFichero(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            
            String sql = "SELECT " + tfi_cod + "," + tfi_nom_fich + " FROM E_TFI WHERE " + tfi_mun + "=?"
                       + " AND " + tfi_eje + "=? AND " + tfi_num +  "=?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());            
            rs = ps.executeQuery();
            
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tfi_cod);
                String valorCampo = rs.getString(tfi_nom_fich);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }
    
    
    
     public ValorCampoSuplementarioVO getValoresFichero(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {
            
           String sql;
            if (dato.isExpHistorico())
                sql = "SELECT TFI_COD,TFI_NOMFICH,TFI_TAMANHO,TFI_MIME FROM HIST_E_TFI" + 
                         " WHERE TFI_MUN=? AND TFI_EJE=? AND TFI_NUM=? AND TFI_COD=?";            
            else
                sql = "SELECT TFI_COD,TFI_NOMFICH,TFI_TAMANHO,TFI_MIME FROM E_TFI" + 
                         " WHERE TFI_MUN=? AND TFI_EJE=? AND TFI_NUM=? AND TFI_COD=?";            

                       
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());      
            ps.setString(i++,codCampo);      
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString(tfi_nom_fich));
                valor.setLongitudFichero(rs.getInt("TFI_TAMANHO"));
                valor.setTipoMimeFichero(rs.getString("TFI_MIME"));
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    public CamposFormulario getValoresDesplegable(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            
            String sql = "SELECT " + tde_cod + "," + tde_valor + " FROM E_TDE WHERE " + tde_mun + "=?"
                    + " AND " + tde_eje + "=? AND " + tde_num + "=?";            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());            
            rs = ps.executeQuery();
            
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tde_cod);
                String valorCampo = rs.getString(tde_valor);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }
    
    
    
    public ValorCampoSuplementarioVO getValoresDesplegable(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {
            
            String sql;
            if (dato.isExpHistorico())
                sql = "SELECT TDE_COD,TDE_VALOR FROM HIST_E_TDE WHERE TDE_MUN = ?"
                        + " AND TDE_EJE = ? AND TDE_NUM = ? AND TDE_COD = ?";            
            else
                sql = "SELECT TDE_COD,TDE_VALOR FROM E_TDE WHERE TDE_MUN = ?"
                        + " AND TDE_EJE = ? AND TDE_NUM = ? AND TDE_COD = ?";            
       
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());            
            ps.setString(i++,codCampo);            
            rs = ps.executeQuery();
            
            while (rs.next()) {                                
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString(tde_valor));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_NUMERICO_CALCULADO);
            }

        } catch (Exception e) {            
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    
    
    
    public CamposFormulario getValoresDesplegableTramite(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato,int codTramite,
            int ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
                        
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", tdet_cod}) + " AS " + tdet_cod + ","
                    + tdet_valor + " FROM E_TDET WHERE " + tdet_mun + "=? AND " + tdet_pro + "=? AND " + 
                    tdet_eje + " =? AND " + tdet_num + "=? AND " + tdet_tra + "=? AND " + tdet_ocu + "=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            
            rs = ps.executeQuery();
                        
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tdet_cod);
                String valorCampo = rs.getString(tdet_valor);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    
    public ValorCampoSuplementarioVO getValoresDesplegableTramite(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato,int codTramite,int ocurrencia,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        
        try {
                        
            String sql;       
            if (dato.isExpHistorico())
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TDET_COD"}) + " AS TDET_COD,"
                        + " TDET_VALOR FROM HIST_E_TDET WHERE TDET_MUN = ? AND TDET_PRO = ? AND " + 
                        "TDET_EJE =? AND TDET_NUM = ? AND TDET_TRA = ? AND TDET_OCU = ? AND TDET_COD=?";
            else
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TDET_COD"}) + " AS TDET_COD,"
                        + " TDET_VALOR FROM E_TDET WHERE TDET_MUN = ? AND TDET_PRO = ? AND " + 
                        "TDET_EJE =? AND TDET_NUM = ? AND TDET_TRA = ? AND TDET_OCU = ? AND TDET_COD=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            ps.setString(i++,codCampo);
            
            rs = ps.executeQuery();
            while (rs.next()) {                            
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TDET_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_DESPLEGABLE);
            }
            
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return valor;
    }
    
    
    public CamposFormulario getValoresNumericosCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {            
            String sql = "SELECT TNUC_COD" + "," + oad.convertir("TNUC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNUC_VALOR" + 
                         " FROM E_TNUC WHERE TNUC_MUN=? AND TNUC_EJE=? AND TNUC_NUM=?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            int i=1;
            ps = con.prepareStatement(sql);
            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tnuc_cod);
                String valorCampo = rs.getString(tnuc_valor);
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }     
    
    
    
    
    public ValorCampoSuplementarioVO getValoresNumericosCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {            
           String sql;
            if (dato.isExpHistorico())
                sql = "SELECT TNUC_COD" + "," + oad.convertir("TNUC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNUC_VALOR" + 
                         " FROM HIST_E_TNUC WHERE TNUC_MUN=? AND TNUC_EJE=? AND TNUC_NUM=? AND TNUC_COD=?";
            else
                sql = "SELECT TNUC_COD" + "," + oad.convertir("TNUC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNUC_VALOR" + 
                         " FROM E_TNUC WHERE TNUC_MUN=? AND TNUC_EJE=? AND TNUC_NUM=? AND TNUC_COD=?";


            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            int i=1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setString(i++,codCampo);            
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                
                String valorCampo = rs.getString("TNUC_VALOR");
                if (valorCampo.endsWith(",00")) {
                    valorCampo = valorCampo.substring(0, valorCampo.length() - 3);
                }
                
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(valorCampo);
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_NUMERICO_CALCULADO);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }     
    
    
    
    
    public CamposFormulario getValoresNumericosTramiteCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,int codTramite,int ocurrencia){
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {            
            /*
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");
            */            
            String sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                    new String[]{"'T'", "'" + codTramite + "'", tnuct_cod}) + " AS TNUCT_COD,"
                    + oad.convertir("TNUCT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null)
                    + " AS TNUCT_VALOR FROM E_TNUCT WHERE TNUCT_NUM=? AND TNUCT_PRO=? " + 
                    " AND TNUCT_EJE=? AND TNUCT_TRA=? AND TNUCT_OCU=?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,dato.getNumExpediente());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString(tnuct_cod);
                String valorCampo = rs.getString(tnuct_valor);
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    
    
    public ValorCampoSuplementarioVO getValoresNumericosTramiteCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato,int codTramite,int ocurrencia,String codCampo){
        PreparedStatement ps = null;
        ResultSet rs = null;        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {            
            
            String sql;
            if (dato.isExpHistorico())
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", "TNUCT_COD"}) + " AS TNUCT_COD,"
                        + oad.convertir("TNUCT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null)
                        + " AS TNUCT_VALOR FROM E_TNUCT WHERE TNUCT_NUM=? AND TNUCT_PRO=? " + 
                        " AND TNUCT_EJE=? AND TNUCT_TRA=? AND TNUCT_OCU=? AND TNUCT_COD=?";
            else
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", "TNUCT_COD"}) + " AS TNUCT_COD,"
                        + oad.convertir("TNUCT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null)
                        + " AS TNUCT_VALOR FROM E_TNUCT WHERE TNUCT_NUM=? AND TNUCT_PRO=? " + 
                        " AND TNUCT_EJE=? AND TNUCT_TRA=? AND TNUCT_OCU=? AND TNUCT_COD=?";


            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,dato.getNumExpediente());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            ps.setString(i++,codCampo);
            
            rs = ps.executeQuery();            
            while (rs.next()) {
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TNUCT_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_NUMERICO_CALCULADO);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    

    
    
    
    
    public CamposFormulario getValoresFechaCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";

        try {
         
            if (mascara == null || "".equals(mascara)) {                                
                sql = "SELECT TFEC_COD," + oad.convertir("TFEC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
                    + " AS TFEC_VALOR " 
                    + " FROM E_TFEC WHERE TFEC_MUN=? AND TFEC_EJE=?"
                    + " AND TFEC_NUM=?";
                
                
            } else {               
                sql = "SELECT TFEC_COD," + oad.convertir("TFEC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara)
                        + " AS TFEC_VALOR" 
                        + " FROM E_TFEC WHERE TFEC_MUN=? AND TFEC_EJE=? "
                        + " AND TFEC_NUM=?";
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
                    
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TFEC_COD");
                String valorCampo = rs.getString("TFEC_VALOR");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs!= null) rs.close();                
                if (ps!= null) ps.close();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return cF;
    }

    
    
    
    public ValorCampoSuplementarioVO getValoresFechaCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        String sql = "";

        try {
         
            if (mascara == null || "".equals(mascara))                              
                sql = "SELECT TFEC_COD," + oad.convertir("TFEC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
                    + " AS TFEC_VALOR ";
            else
                sql = "SELECT TFEC_COD," + oad.convertir("TFEC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara)
                        + " AS TFEC_VALOR"; 
                
            if (dato.isExpHistorico())
                sql = sql + " FROM HIST_E_TFEC WHERE TFEC_MUN=? AND TFEC_EJE=?";
            else
                sql = sql + " FROM E_TFEC WHERE TFEC_MUN=? AND TFEC_EJE=?";

            sql = sql + " AND TFEC_NUM=? AND TFEC_COD=?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setString(i++,codCampo);
                    
            rs = ps.executeQuery();
            
            while (rs.next()) {                                                       
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TFEC_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_FECHA_CALCULADO);
            }
            

        } catch (Exception e) {
            
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (rs!= null) rs.close();                
                if (ps!= null) ps.close();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    
    
    
    public CamposFormulario getValoresFechaTramiteCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara,
            int codTramite, int ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";

        try {
 
            if (mascara == null || "".equals(mascara)) {
                /*
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", tfect_cod}) + " AS " + tfect_cod
                        + ", " + oad.convertir(tfect_valor, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " + tfect_valor
                        + " FROM E_TFECT WHERE " + tfect_mun + " = " + codMunicipio + " AND " + tfect_pro + "='"
                        + codProcedimiento + "' AND " + tfect_eje + " = " + ejercicio + " AND " + tfect_num + " ='"
                        + numero + "' AND " + tfect_tra + "=" + codTramite + " AND " + tfect_ocu + "=" + ocurrencia;
                */                
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", tfect_cod}) + " AS TFECT_COD"
                        + ", " + oad.convertir("TFECT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " + tfect_valor
                        + " FROM E_TFECT WHERE TFECT_MUN=? AND TFECT_PRO=? AND TFECT_EJE=? AND TFECT_NUM=?"
                        + " AND TFECT_TRA=? AND TFECT_OCU=?";                
                
            } else {
                /**
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", tfect_cod}) + " AS " + tfect_cod
                        + "," + oad.convertir(tfect_valor, oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS " + tfect_valor
                        + " FROM E_TFECT WHERE " + tfect_mun + " = " + codMunicipio + " AND " + tfect_pro + "='"
                        + codProcedimiento + "' AND " + tfect_eje + " = " + ejercicio + " AND " + tfect_num + " ='"
                        + numero + "' AND " + tfect_tra + "=" + codTramite + " AND " + tfect_ocu + "=" + ocurrencia;
                        */ 
                sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", tfect_cod}) + " AS TFECT_COD" 
                        + "," + oad.convertir("TFECT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS " + tfect_valor
                        + " FROM E_TFECT WHERE TFECT_MUN=? AND TFECT_PRO=? AND TFECT_EJE=? AND TFECT_NUM=? " 
                        + "AND TFECT_TRA=? AND TFECT_OCU=?";                                
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TFECT_COD");
                String valorCampo = rs.getString("TFECT_VALOR");
                campos.put(codCampo, valorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    
    
    public ValorCampoSuplementarioVO getValoresFechaTramiteCal(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, String mascara,int codTramite, int ocurrencia,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;        
        String sql = "";
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
        
        try {
            
            sql = "SELECT " + oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                        new String[]{"'T'", "'" + codTramite + "'", "TFECT_COD"}) + " AS TFECT_COD";
            
            if (mascara == null || "".equals(mascara))                         
                sql = sql + ", " + oad.convertir("TFECT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFECT_VALOR"; 
             else                 
                sql = sql + "," + oad.convertir("TFECT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS TFECT_VALOR"; 
                
            if (dato.isExpHistorico())
                sql = sql + " FROM HIST_E_TFECT WHERE TFECT_MUN=? AND TFECT_PRO=? AND TFECT_EJE=? AND TFECT_NUM=? ";
            else
                sql = sql + " FROM E_TFECT WHERE TFECT_MUN=? AND TFECT_PRO=? AND TFECT_EJE=? AND TFECT_NUM=? ";
                
            sql = sql + "AND TFECT_TRA=? AND TFECT_OCU=? AND TFECT_COD=?"; 

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            ps.setString(i++,codCampo);            
            rs = ps.executeQuery();
    
            while (rs.next()) {    
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TFECT_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_FECHA_CALCULADO);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    
    
    public CamposFormulario getValoresDesplegableExt(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try {
          
            String sql = "SELECT TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM E_TDEX WHERE TDEX_MUN=?"
                    + " AND TDEX_EJE=? AND TDEX_NUM=?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            
            rs = ps.executeQuery();
            String entrar = "no";

            HashMap<String, String> campos = new HashMap<String, String>();
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TDEX_COD");
                String valorCampo = rs.getString("TDEX_VALOR");
                String codValorCampo = rs.getString("TDEX_CODSEL");
                campos.put(codCampo, valorCampo);
                campos.put(codCampo+"_CODSEL", codValorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    
    public ValorCampoSuplementarioVO getValoresDesplegableExt(AdaptadorSQLBD oad, Connection con,DatosExpedienteVO dato,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {          
           String sql;
            if (dato.isExpHistorico())
                sql = "SELECT TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM HIST_E_TDEX WHERE TDEX_MUN=?"
                    + " AND TDEX_EJE=? AND TDEX_NUM=? AND TDEX_COD=?";
            else
                sql = "SELECT TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM E_TDEX WHERE TDEX_MUN=?"
                    + " AND TDEX_EJE=? AND TDEX_NUM=? AND TDEX_COD=?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i=1;
            ps = con.prepareStatement(sql);
            
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setString(i++,codCampo);            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TDEX_VALOR"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_DESPLEGABLE_EXTERNO);
                valor.setCodValorDatoSuplementario(rs.getString("TDEX_CODSEL"));
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    
    
    
    public CamposFormulario getValoresDesplegableExtTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, int codTramite,int ocurrencia) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
           
            String sql = "SELECT ";
            sql = sql + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TDEXT_COD" });
            sql = sql + " AS TDEXT_COD,";
            sql = sql + " TDEXT_VALOR, TDEXT_CODSEL FROM E_TDEXT WHERE TDEXT_MUN=? AND TDEXT_PRO =? AND TDEXT_EJE=? ";
            sql = sql + " AND TDEXT_NUM =? AND TDEXT_TRA=? ";
            sql = sql + " AND TDEXT_OCU =?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            
            rs = ps.executeQuery();
            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("TDEXT_COD");
                String valorCampo = rs.getString("TDEXT_VALOR");
                String codValorCampo = rs.getString("TDEXT_CODSEL");
                campos.put(codCampo, valorCampo);
                campos.put(codCampo+"_CODSEL", codValorCampo);
                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }

        return cF;
    }
    
    
    
    public ValorCampoSuplementarioVO getValoresDesplegableExtTramite(AdaptadorSQLBD oad, Connection con, DatosExpedienteVO dato, int codTramite,int ocurrencia,String codCampo) {
        PreparedStatement ps = null;
        ResultSet rs = null;        
        ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();

        try {
           
           String sql = "SELECT ";
            sql = sql + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T'", "'" + codTramite + "'", "TDEXT_COD" });
            sql = sql + " AS TDEXT_COD, TDEXT_CODSEL,";
            
            if (dato.isExpHistorico())
                sql = sql + " TDEXT_VALOR FROM HIST_E_TDEXT WHERE TDEXT_MUN=? AND TDEXT_PRO =? AND TDEXT_EJE=? ";
            else
                sql = sql + " TDEXT_VALOR FROM E_TDEXT WHERE TDEXT_MUN=? AND TDEXT_PRO =? AND TDEXT_EJE=? ";
            
            sql = sql + " AND TDEXT_NUM =? AND TDEXT_TRA=? ";
            sql = sql + " AND TDEXT_OCU =? AND TDEXT_COD=?";
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);           
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,dato.getCodOrganizacion());
            ps.setString(i++,dato.getCodProcedimiento());
            ps.setInt(i++,dato.getEjercicio());
            ps.setString(i++,dato.getNumExpediente());
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrencia);
            ps.setString(i++,codCampo);            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                valor.setCodDatoSuplementario(codCampo);
                valor.setValorDatoSuplementario(rs.getString("TDEXT_VALOR"));
                valor.setCodValorDatoSuplementario(rs.getString("TDEXT_CODSEL"));
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_DESPLEGABLE_EXTERNO);
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error("Excepcion capturada en: " + getClass().getName());
                }
            }
        }
        return valor;
    }
    
    
    
    
    public int grabarDatosSuplementariosConsultas(ArrayList<EstructuraCampo> estructuraDatosSuplementarios, Vector valoresDatosSuplementarios, String[] params, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        String sql = null;
        int res = 0;

        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("LG DATOS SUPLEMENTARIOS ..................... " + estructuraDatosSuplementarios.size());
            }
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                m_Log.debug("I ... " + i);
                res = 0;
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo)estructuraDatosSuplementarios.get(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
                String codTramite = eC.getCodTramite();
                String codTipoDato = eC.getCodTipoDato();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);
                if ("1".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoNumerico(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("2".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoTexto(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("3".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoFecha(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("4".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoTextoLargo(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("5".equals(codTipoDato)) {
                    res=1;
                    
                } else if ("6".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                       
                        res = setDatoDesplegable(oad, con, eC, gVO);
                       
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("8".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoNumericoCal(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("9".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoFechaCal(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                } else if ("10".equals(codTipoDato)) {
                    if (codTramite == null || "".equals(codTramite)) {
                        res = setDatoDesplegableExt(oad, con, eC, gVO);
                        if (res < 1) {
                            break;
                        }
                    }else res =1;
                }
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
            }
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } finally{
           return res;
        }

    }
    
    
      
    
      public int grabarDatosSuplementariosConsultasFichero(ArrayList<EstructuraCampo> estructuraDatosSuplementarios, Vector valoresDatosSuplementarios, String[] params, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        String sql = null;
        int res = 0;

        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("LG DATOS SUPLEMENTARIOS ..................... " + estructuraDatosSuplementarios.size());
            }
            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                m_Log.debug("I ... " + i);
                res = 0;
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo)estructuraDatosSuplementarios.get(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
                String codTramite = eC.getCodTramite();
                String codTipoDato = eC.getCodTipoDato();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);
                if ("5".equals(codTipoDato)) {
                    
                    if (codTramite == null || "".equals(codTramite)) {
                    if (m_Log.isDebugEnabled()) {
                                    m_Log.debug(".....ha llegado el fichero");
                    }

                        try{
                            
                            String estado = (String)gVO.getAtributo(eC.getCodCampo() + "_ESTADO");
                            m_Log.debug("Estado documento: " + estado);
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){
    
                                if(!setDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(),gVO,con)){
                                    break;
                                }else
                                    res = 1;

                                //res = setDatoFichero(oad, con, eC, gVO);
                                if (m_Log.isDebugEnabled()) {
                                                        m_Log.debug(".....ha pasado el fichero");
                                }
                            }//else res=1;
                            else
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO){
                                if(!eliminarDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(), gVO, con)){
                            break;
                                }else
                                    res = 1;
                                
                            }else res=1;
                                 
                      
                        }catch(AlmacenDocumentoTramitacionException e){                           
                            throw e;                            
                        }
                    }else res=1;
                    
                } else res =1;
                
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
            }
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } finally{
           return res;
        }

    }
    
        
    /**
     * 
     * @param estructuraDatosSuplementarios: Colección con la estructura de los campos suplementarios 
     * @param gVO: Los valores de los datos suplementarios
     * @param params: Parámetros de conexión a la BBDD
     * @param oad: Objeto de tipo AdaptadorSQLBD
     * @param con: Conexión a la BBDD
     * @return int 
     * @throws TechnicalException Permite grabar los valores de los datos suplementarios recuperados de la 
     */
    public int grabarDatosSuplementariosConsultas(Vector<EstructuraCampoAgrupado> estructuraDatosSuplementarios, GeneralValueObject gVO, String[] params, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        int res = 0;

        try {

            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                m_Log.debug("I ... " + i);
                res = 0;
                EstructuraCampoAgrupado eC = estructuraDatosSuplementarios.get(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
                String codTramite = eC.getCodTramite();
                String codTipoDato = eC.getCodTipoDato();
                if (codTramite == null || "".equals(codTramite)) {
                    if ("1".equals(codTipoDato)) {
                        res = setDatoNumerico(oad, con, eC, gVO);
                    } else if ("2".equals(codTipoDato)) {
                        res = setDatoTexto(oad, con, eC, gVO);
                    } else if ("3".equals(codTipoDato)) {
                        res = setDatoFecha(oad, con, eC, gVO);
                    } else if ("4".equals(codTipoDato)) {
                        res = setDatoTextoLargo(oad, con, eC, gVO);
                    } else if ("5".equals(codTipoDato)) {
                        res = 1;
                    } else if ("6".equals(codTipoDato)) {
                        res = setDatoDesplegable(oad, con, eC, gVO);
                    } else if ("8".equals(codTipoDato)) {
                        res = setDatoNumericoCal(oad, con, eC, gVO);
                    } else if ("9".equals(codTipoDato)) {
                        res = setDatoFechaCal(oad, con, eC, gVO);
                    } else if ("10".equals(codTipoDato)) {
                        res = setDatoDesplegableExt(oad, con, eC, gVO);
                    }
                    
                    if (res < 1) {
                        break;
                    }
                } else {
                    res = 1;
                }
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
            }
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } finally {
            return res;
        }

    }

    
    
    
     public int grabarDatosSuplementariosConsultasFichero(ArrayList<EstructuraCampoAgrupado> estructuraDatosSuplementarios, ArrayList<GeneralValueObject> valoresDatosSuplementarios, String[] params, AdaptadorSQLBD oad, Connection con)
            throws TechnicalException {

        String sql = null;
        int res = 0;

        try {

            for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
                m_Log.debug("I ... " + i);
                res = 0;
                EstructuraCampoAgrupado eC = new EstructuraCampoAgrupado();
                eC = (EstructuraCampoAgrupado) estructuraDatosSuplementarios.get(i);
                m_Log.debug("CODIGO ... " + eC.getCodCampo());
                m_Log.debug("NOMBRE ... " + eC.getDescCampo());
                m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
                String codTramite = eC.getCodTramite();
                String codTipoDato = eC.getCodTipoDato();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) valoresDatosSuplementarios.get(i);
               if ("5".equals(codTipoDato)) {
                     
                    
                    if (codTramite == null || "".equals(codTramite)) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(".....ha llegado el fichero");
                        }
                                                
                        
                        try{
                            String estado = (String)gVO.getAtributo(eC.getCodCampo() + "_ESTADO");
                            m_Log.debug("Estado documento: " + estado);
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO){
                            
                                if(!setDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(),gVO,con,params)){
                                    break;
                                }else
                                    res = 1;

                                //res = setDatoFichero(oad, con, eC, gVO);
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug(".....ha pasado el fichero");
                                }
                            }//else res=1;
                            else
                            if(estado!=null && Integer.parseInt(estado)==ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO){
                                if(!eliminarDatoFicheroCampoSuplementarioExpedientePluginDocumentos(eC.getCodCampo(), gVO, con)){
                                    break;
                                }else
                                    res = 1;
                                
                            }else res=1;
                                                             
                        }catch(AlmacenDocumentoTramitacionException e){                           
                            throw e;                            
                        }
                    }else res=1;

               }else res=1;
            }
        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } finally{
           return res;
        }

    }
    
    
    
    public int setDatoNumerico(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());


        try {
            sql = "DELETE FROM E_TNU WHERE " + tnu_mun + "=" + codMunicipio + " AND " + tnu_eje + "="
                    + ejercicio + " AND " + tnu_num + "='" + numeroExpediente + "' AND " + tnu_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            stmt = null;
            stmt = con.createStatement();
            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TNU ( " + tnu_mun + "," + tnu_eje + "," + tnu_num + "," + tnu_cod + ","
                        + tnu_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "', "
                        + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
    
    
    
     public int setDatoTexto(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {

        Statement stmt = null;
        PreparedStatement st = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        try {
            String sql = "DELETE FROM E_TXT WHERE TXT_MUN = " + codMunicipio + " AND TXT_EJE = " + ejercicio  
                    + " AND TXT_NUM = '" + numeroExpediente + "' AND TXT_COD = '" + codDato + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TXT (TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR) VALUES  (?, ?, ?, ?, ?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }

                st = con.prepareStatement(sql);
                // Asignar los valores de la consulta al preparedStatement.
                int i = 1;
                st.setInt(i++, Integer.parseInt(codMunicipio));

                st.setInt(i++, Integer.parseInt(ejercicio));
                st.setString(i++, numeroExpediente);
                st.setString(i++, codDato);
                st.setString(i, valorDato);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = st.executeUpdate();
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                
                if(st!=null) st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
     
     
     
    public int setDatoFecha(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String plazoFecha = eC.getPlazoFecha();
        String checkFechaPlazo = eC.getCheckPlazoFecha();
        String fechaVencimiento = null;
        String campoActivo = (String) gVO.getAtributo("activar" + eC.getCodCampo());
        String mascara = eC.getMascara();
        if (mascara == null || "".equals(mascara))
            mascara = "DD/MM/YYYY";

        try {
            sql = "DELETE FROM E_TFE WHERE " + tfe_mun + "=" + codMunicipio + " AND " + tfe_eje + "="
                    + ejercicio + " AND " + tfe_num + "='" + numeroExpediente + "' AND " + tfe_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if (valorDato != null && !"".equals(valorDato)) {
                //Si el campo de tipo fecha tiene un plazo definido
                if (plazoFecha != null) {
                    if (campoActivo.equals("desactivada")) {
                        sql = "INSERT INTO E_TFE ( " + tfe_mun + "," + tfe_eje + "," + tfe_num + "," + tfe_cod + ","
                                + tfe_valor + ",TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO) VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                                + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + "," + null + "," + 0 + ")";
                    } else {
                        //Se calcula la fecha de vencimiento
                        fechaVencimiento = calcularFechaVencimiento(valorDato, plazoFecha, checkFechaPlazo);
                        sql = "INSERT INTO E_TFE ( " + tfe_mun + "," + tfe_eje + "," + tfe_num + "," + tfe_cod + ","
                                + tfe_valor + ",TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO) VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                                + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + "," + oad.convertir("'" + fechaVencimiento + "'", oad.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "," + 1 + ")";
                    }
                } else { //no tiene definido plazo para fecha vencimiento
                    sql = "INSERT INTO E_TFE ( " + tfe_mun + "," + tfe_eje + "," + tfe_num + "," + tfe_cod + ","
                            + tfe_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                            + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + ")";
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

            if (plazoFecha != null){
                AlarmaExpedienteVO alarma = new AlarmaExpedienteVO(Integer.parseInt(codMunicipio),
                        Integer.parseInt(ejercicio),numeroExpediente,0,0,codDato,
                        fechaVencimiento!=null?DateOperations.toCalendar(fechaVencimiento, "dd/MM/yyyy"):null);
                AlarmasExpedienteDAO.getInstance().grabarAlarmaExpediente(alarma,con);
            }
        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    } 
    
    
    public int setDatoTextoLargo(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {
        PreparedStatement pstmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        try {
            sql = "DELETE FROM E_TTL WHERE " + ttl_mun + "=" + codMunicipio + " AND " + ttl_eje + "="
                    + ejercicio + " AND " + ttl_num + "='" + numeroExpediente + "' AND " + ttl_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();

            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TTL ( " + ttl_mun + "," + ttl_eje + "," + ttl_num + "," + ttl_cod + ","
                        + ttl_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "',?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                pstmt.close();
                pstmt = con.prepareStatement(sql);
                java.io.StringReader cr = new java.io.StringReader(valorDato);
                pstmt.setCharacterStream(1, cr, valorDato.length());
                resultadoInsertar = pstmt.executeUpdate();
                cr.close();
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
    
    
    public int setDatoDesplegable(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        
        if(eC.getBloqueado().equals("NO")){
        try {
            sql = "DELETE FROM E_TDE WHERE " + tde_mun + "=" + codMunicipio + " AND " + tde_eje + "="
                    + ejercicio + " AND " + tde_num + "='" + numeroExpediente + "' AND " + tde_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TDE ( " + tde_mun + "," + tde_eje + "," + tde_num + "," + tde_cod + ","
                        + tde_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "','" + valorDato + "')";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                stmt = con.createStatement();
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException e){
                e.printStackTrace();
            }            
        }
        }else{	
            resultadoInsertar = 1;	
        }
        return resultadoInsertar;
    }
    
    
    
    public int setDatoNumericoCal(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {
        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());


        try {
            sql = "DELETE FROM E_TNUC WHERE " + tnuc_mun + "=" + codMunicipio + " AND " + tnuc_eje + "="
                    + ejercicio + " AND " + tnuc_num + "='" + numeroExpediente + "' AND " + tnuc_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            stmt = null;
            stmt = con.createStatement();
            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TNUC ( " + tnuc_mun + "," + tnuc_eje + "," + tnuc_num + "," + tnuc_cod + ","
                        + tnuc_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "', "
                        + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
    
    
    public int setDatoDesplegableExt(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {    
        Statement stmt = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String codValorDato = (String) gVO.getAtributo(eC.getCodCampo()+"_CODSEL");

        try {
            String sql = "DELETE FROM E_TDEX WHERE TDEX_MUN = " + codMunicipio + " AND TDEX_EJE = " + ejercicio
                    + " AND TDEX_NUM = '" + numeroExpediente + "' AND TDEX_COD = '" + codDato + "'";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();


            if (valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TDEX (TDEX_MUN, TDEX_EJE, TDEX_NUM, TDEX_COD, TDEX_VALOR, TDEX_CODSEL) VALUES  (?, ?, ?, ?, ?, ?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }

                PreparedStatement st = con.prepareStatement(sql);

                // Asignar los valores de la consulta al preparedStatement.
                int i = 1;
                st.setInt(i++, Integer.parseInt(codMunicipio));

                st.setInt(i++, Integer.parseInt(ejercicio));
                st.setString(i++, numeroExpediente);
                st.setString(i++, codDato);
                st.setString(i++, valorDato);
                st.setString(i, codValorDato);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = st.executeUpdate();
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
    
    
    public int setDatoFechaCal(AdaptadorSQL oad, Connection con, EstructuraCampoAgrupado eC, GeneralValueObject gVO) {

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String mascara = eC.getMascara();

        try {
            sql = "DELETE FROM E_TFEC WHERE " + tfec_mun + "=" + codMunicipio + " AND " + tfec_eje + "="
                    + ejercicio + " AND " + tfec_num + "='" + numeroExpediente + "' AND " + tfec_cod + "='"
                    + codDato + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if (valorDato != null && !"".equals(valorDato)) {
                if (mascara == null || "".equals(mascara)) {
                    sql = "INSERT INTO E_TFEC ( " + tfec_mun + "," + tfec_eje + "," + tfec_num + "," + tfec_cod + ","
                            + tfec_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                            + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")";
                } else {
                    sql = "INSERT INTO E_TFEC ( " + tfec_mun + "," + tfec_eje + "," + tfec_num + "," + tfec_cod + ","
                            + tfec_valor + ") VALUES (" + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                            + codDato + "'," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + ")";
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }
        } catch (Exception e) {
            resultadoInsertar = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }
    
    
    
     /**
     * Recupera los valores de los campos suplementarios que tiene que actualizar en la ficha del 
     * expediente, tras grabar el mismo. Concretamente son los valores de los campos de tipo numérico
     * y los de tipo fecha co
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la base de datos
     * @return ArrayList<ValorSuplementarioVO>
     */
    public ArrayList<ValorCampoSuplementarioVO> getCamposExpedienteActualizarEnFicha(int codOrganizacion,String numExpediente,Connection con){
        ArrayList<ValorCampoSuplementarioVO> salida = new ArrayList<ValorCampoSuplementarioVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];
            
            String sql = "SELECT TNU_COD,TNU_VALOR FROM E_PCA,E_TNU WHERE PCA_PRO=? AND PCA_TDA=1 AND PCA_ACTIVO='SI' AND PCA_OCULTO<>'SI' " + 
                         "AND PCA_COD=TNU_COD AND TNU_NUM=? AND TNU_EJE=? AND TNU_MUN=?";
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,codProcedimiento);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setInt(i++,codOrganizacion);            
            rs = ps.executeQuery();
            
            while(rs.next()){
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_NUMERICO);
                valor.setCodDatoSuplementario(rs.getString("TNU_COD"));
                valor.setValorDatoSuplementario(rs.getString("TNU_VALOR"));
                salida.add(valor);
            }
            
            ps.close();
            rs.close();
            
            
            // Se recupera los campos suplementarios del expediente de tipo fecha
            sql = "SELECT TFE_COD,TFE_VALOR,TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO FROM E_PCA,E_TFE WHERE PCA_PRO=? AND PCA_TDA=3 AND PCA_ACTIVO='SI' AND PCA_OCULTO<>'SI' " + 
                  "AND PCA_COD=TFE_COD AND TFE_NUM=? AND TFE_EJE=? AND TFE_MUN=?";
            
            i = 1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,codProcedimiento);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setInt(i++,codOrganizacion);            
            rs = ps.executeQuery();
            
            while(rs.next()){
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                //String valorFechaVencimiento = rs.getString("TFE_FEC_VENCIMIENTO");
                java.sql.Timestamp tFechaVencimiento = rs.getTimestamp("TFE_FEC_VENCIMIENTO");                
                valor.setValorPlazoActivo(rs.getString("PLAZO_ACTIVADO"));                
                
                if(tFechaVencimiento!=null){                                        
                    Calendar actual = Calendar.getInstance();                                
                    Calendar vencimiento = DateOperations.toCalendar(tFechaVencimiento);
                    
                    valor.setAlarmaVencida(false);                                                            
                    if(actual.getTimeInMillis()>vencimiento.getTimeInMillis())                    
                        valor.setAlarmaVencida(true);                    
                }           
                
                valor.setTipoDatoSuplementario(ConstantesDatos.TIPO_CAMPO_FECHA);
                valor.setCodDatoSuplementario(rs.getString("TFE_COD"));
                valor.setValorDatoSuplementario(rs.getString("TFE_VALOR"));
                salida.add(valor);
                
           }// while
                    
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

    
    
    /****************************************************************/
   
    /**
     * Recupera el nombre de un fichero y su longitud que se encuentra asociado a un campo suplementario
     * de tipo fichero, definido a nivel de trámite
     * @param codigo: Código del campo suplementario
     * @param municipio: Código del municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número del expediente
     * @param ocurrencia: Ocurrencia del trámite
     * @param params: Parámetros de conexión a la BBDD
     * @param codTramite: Código del trámite
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
    public CampoSuplementarioFicheroVO getNombreLongitudFicheroTramite(String codigo, String municipio, String ejercicio, String numero, String ocurrencia, String codTramite,boolean expedienteFinalizado,String[] params) throws TechnicalException {
        CampoSuplementarioFicheroVO campo = new CampoSuplementarioFicheroVO();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try {
            
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();
            
            int lg = codTramite.length();
            codigo = codigo.substring(lg + 1);
            
            if(!expedienteFinalizado) {
                sql = "SELECT TFIT_NOMFICH,TFIT_TAMANHO,TFIT_MIME FROM E_TFIT WHERE " + tfit_mun + " = "
                        + municipio + " AND " + tfit_eje
                        + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                        + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;
            } else {
                sql = "SELECT TFIT_NOMFICH,TFIT_TAMANHO,TFIT_MIME FROM HIST_E_TFIT WHERE " + tfit_mun + " = "
                        + municipio + " AND " + tfit_eje
                        + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                        + codTramite + " AND " + tfit_cod + "='" + codigo + "'" + " AND " + tfit_ocu + "=" + ocurrencia;                
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {                
                campo.setCodigoCampo(codigo);
                campo.setNombreFichero(rs.getString("TFIT_NOMFICH"));
                campo.setLongitudFichero(rs.getInt("TFIT_TAMANHO"));
                campo.setCodTramite(Integer.parseInt(codTramite));
                campo.setOcurrenciaTramite(Integer.parseInt(ocurrencia));
                campo.setNumExpediente(numero);
                campo.setTipoMime(rs.getString("TFIT_MIME"));
            }

        }catch (BDException e) {
            m_Log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());            
        }catch (SQLException e) {
            m_Log.error("Error al recuperar el nombre del campo suplementario de tipo fichero: " + codigo + " del expediente " + numero + " por: " +  e.getMessage());
        } finally {
            try {
                
                if(stmt != null) stmt.close();
                if(rs!=null) rs.close();                
                if(conexion!=null) conexion.close();
                
            } catch (Exception e) {
                m_Log.error("Error al cerrar recursos asociados a una conexión a la BBDD: " + e.getMessage());
            }
        }        
        return campo;
    }
    
    
    
    public String getTipoFichero(String codigo, String municipio, String ejercicio, String numero,boolean expedienteHistorico,String[] params) throws TechnicalException {
        String tipo = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);            
            conexion = abd.getConnection();            
            
            if(!expedienteHistorico) {
                sql = "SELECT " + tfi_mime + " FROM E_TFI WHERE " + tfi_mun + "=" + municipio + " AND " + tfi_eje + "="
                        + ejercicio + " AND " + tfi_num + "='" + numero + "' AND " + tfi_cod + "='"
                        + codigo + "'";
            }else {
                sql = "SELECT " + tfi_mime + " FROM HIST_E_TFI WHERE " + tfi_mun + "=" + municipio + " AND " + tfi_eje + "="
                        + ejercicio + " AND " + tfi_num + "='" + numero + "' AND " + tfi_cod + "='"
                        + codigo + "'";                
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString(tfi_mime);
            }
            
        } catch (BDException e) {
            m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            
        }catch (SQLException e) {
            m_Log.error("Error al recuperar el tipo MIME de un campo suplementario de tipo fichero con código: " + codigo + " del expediente : " + numero + ": " + e.getMessage());
            
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if(conexion!=null) conexion.close();                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }

    
    public String getTipoFicheroTramite(String codigo, String municipio, String ejercicio, String numero, String[] params, String codTramite,boolean expedienteHistorico) throws TechnicalException {
        String tipo = "";
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try {            
            conexion = abd.getConnection();     
            
            if(!expedienteHistorico) {
                sql = "SELECT " + tfit_mime + " FROM E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'";
            } else {
                    sql = "SELECT " + tfit_mime + " FROM HIST_E_TFIT WHERE " + tfit_mun + " = "
                    + municipio + " AND " + tfit_eje
                    + " = " + ejercicio + " AND " + tfit_num + " ='" + numero + "' AND " + tfit_tra + "="
                    + codTramite + " AND " + tfit_cod + "='" + codigo + "'";                
            }
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo = rs.getString(tfit_mime);
            }            
            
        } catch (BDException e) {
            m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            
        }catch (SQLException e) {
            m_Log.error("Error al recuperar el tipo MIME de un campo suplementario de tipo fichero con código: " + codigo + " del expediente : " + numero + " y del trámite: " +  codTramite + ":" + e.getMessage());
            
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if(conexion!=null) conexion.close();                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tipo;
    }
    
    
    /**
     * Obteniene el id de los metadatos del documento de tipo
     * fichero del expediente
     * 
     * @param codCampo
     * @param ejercicio
     * @param municipio
     * @param numeroExpediente
     * @param con
     * @return 
     * @throws es.altia.common.exception.TechnicalException 
     */
    public Long getDocumentoIdMetadatoExpediente(String codCampo, String ejercicio, 
            String municipio, String numeroExpediente, Connection con)
            throws TechnicalException {
        Long idMetadato = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT TFI_ID_METADATO ")
               .append("FROM E_TFI ")
               .append("WHERE TFI_COD = ? AND TFI_EJE = ? AND TFI_MUN = ? AND TFI_NUM = ?");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: TFI_COD = %s, TFI_EJE = %s, TFI_MUN = %s, TFI_NUM = %s ",
                        codCampo, ejercicio, municipio, numeroExpediente));
            }

            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    codCampo,
                    ejercicio,
                    municipio,
                    numeroExpediente);

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                idMetadato = JdbcOperations.getLongFromResultSet(resultSet, "TFI_ID_METADATO");
            }   
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al intentar obtener el campo TFI_ID_METADATO del documento de tipo fichero del expediente", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(resultSet);
            SigpGeneralOperations.closeStatement(ps);
        }

        return idMetadato;
    }
    
    /**
     * Obteniene el id de los metadatos del documento de tipo
     * fichero del tramite
     * 
     * @param codCampo
     * @param ejercicio
     * @param municipio
     * @param numeroExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param con
     * @return 
     * @throws es.altia.common.exception.TechnicalException 
     */
    public Long getDocumentoIdMetadatoTramite(String codCampo, Integer ejercicio, 
            Integer municipio, String numeroExpediente, Integer codTramite,
            Integer ocurrenciaTramite, Connection con)
            throws TechnicalException {
        Long idMetadato = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT TFIT_ID_METADATO ")
               .append("FROM E_TFIT ")
               .append("WHERE TFIT_COD = ? AND TFIT_EJE = ? AND TFIT_MUN = ?")
               .append("  AND TFIT_NUM = ? AND TFIT_TRA=? AND TFIT_OCU=?");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: TFIT_COD = %s, TFIT_EJE = %d, TFIT_MUN = %d, TFIT_NUM = %s, TFIT_TRA = %d, TFIT_OCU = %d ",
                        codCampo, ejercicio, municipio, numeroExpediente, codTramite, ocurrenciaTramite));
            }

            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    codCampo,
                    ejercicio,
                    municipio,
                    numeroExpediente,
                    codTramite,
                    ocurrenciaTramite);

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                idMetadato = JdbcOperations.getLongFromResultSet(resultSet, "TFIT_ID_METADATO");
            }   
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al intentar obtener el campo TFIT_ID_METADATO del documento de tipo fichero del tramite", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(resultSet);
            SigpGeneralOperations.closeStatement(ps);
        }

        return idMetadato;
    }
    
    /**
     * Obteniene todos los ids de los metadatos del documento de tipo
     * fichero del tramite
     * 
     * @param codProcedimiento
     * @param codCampo
     * @param ejercicio
     * @param municipio
     * @param numeroExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param con
     * @return 
     * @throws es.altia.common.exception.TechnicalException 
     */
    public ArrayList<Long> getDocumentosIdMetadatoTramite(String codProcedimiento, Integer ejercicio, 
            Integer municipio, String numeroExpediente, Integer codTramite,
            Integer ocurrenciaTramite, Connection con)
            throws TechnicalException {
        
        ArrayList<Long> idMetadatos = new ArrayList<Long>();
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT TFIT_ID_METADATO ")
               .append("FROM E_TFIT ")
               .append("WHERE TFIT_PRO = ? AND TFIT_EJE = ? AND TFIT_MUN = ?")
               .append("  AND TFIT_NUM = ? AND TFIT_TRA=? AND TFIT_OCU=?");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql));
                m_Log.debug(String.format("Param: TFIT_PRO = %s, TFIT_EJE = %d, TFIT_MUN = %d, TFIT_NUM = %s, TFIT_TRA = %d, TFIT_OCU = %d ",
                        codProcedimiento, ejercicio, municipio, numeroExpediente, codTramite, ocurrenciaTramite));
            }

            ps = con.prepareStatement(sql.toString());
            JdbcOperations.setValues(ps, 1,
                    codProcedimiento,
                    ejercicio,
                    municipio,
                    numeroExpediente,
                    codTramite,
                    ocurrenciaTramite);

            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                idMetadatos.add(JdbcOperations.getLongFromResultSet(resultSet, "TFIT_ID_METADATO"));
            }   
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage(), ex);
            throw new TechnicalException("Error al intentar obtener los campos TFIT_ID_METADATO de los documentos de tipo fichero del tramite", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(resultSet);
            SigpGeneralOperations.closeStatement(ps);
        }

        return idMetadatos;
    }

    private void putMetadatosDocumentoEnParametros(Hashtable<String, Object> datos, MetadatosDocumentoVO metadatos) {
        Boolean insertarMetadatoEnBBDD = Boolean.FALSE;
        
        if (metadatos != null) {
            // CSV
            if (StringUtils.isNotEmpty(metadatos.getCsv())) {
                datos.put("metadatoDocumentoCsv", metadatos.getCsv());
                datos.put("metadatoDocumentoCsvAplicacion", metadatos.getCsvAplicacion());
                datos.put("metadatoDocumentoCsvUri", metadatos.getCsvUri());
                insertarMetadatoEnBBDD = Boolean.TRUE;
            }
        }

        datos.put("insertarMetadatosEnBBDD", insertarMetadatoEnBBDD);
    }
    
    private String recuperarValorDesplegableExt(String codDesplegable, String codigo, Connection con) {
        String valor = null;
        ArrayList<GeneralValueObject> todosValores = null;
        
        try {
            String[] parametros = FichaExpedienteDAO.getInstance().recuperarParametrosConexionExternos(con, codDesplegable);
            todosValores = FichaExpedienteDAO.getInstance().recuperarDatosExternos(parametros);
            
            for(GeneralValueObject gVO : todosValores){
                if(codigo.equals((String) gVO.getAtributo("codigo"))) {
                    valor = (String) gVO.getAtributo("valor");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return valor;
    }
}
