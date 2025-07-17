// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence.manual;

// PAQUETES	IMPORTADOS
  
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.*;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config; 
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.eni.conversoreni.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.apache.commons.codec.binary.Base64;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionTramitesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Jorge Hombre	Tuías
 * @version	1.0
 */

public class DefinicionTramitesDAO {

    private static DefinicionTramitesDAO instance	= null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected	static Config m_ConfigTechnical; //	Para el fichero de configuracion t+cnico
    protected	static Config m_ConfigError; // Para los mensajes de error localizados
    protected	static Log m_Log =
            LogFactory.getLog(DefinicionTramitesDAO.class.getName());


    protected static String idiomaDefecto;
    protected	static String uor_nom;
    protected	static String uor_cod;
    protected	static String uor_cod_vis;
    //protected	static String uor_dep;

    protected	static String pro_mun;
    protected	static String pro_cod;
    protected	static String pro_tri;

    protected	static String tra_cod;
    protected	static String tra_cou;
    protected	static String tra_mun;
    protected	static String tra_pro;
    protected	static String tra_vis;
    protected	static String tra_uin;
    protected	static String tra_utr;
    protected	static String tra_plz;
    protected	static String tra_und;
    protected	static String tra_are;
    protected	static String tra_ocu;
    protected	static String tra_cls;
    protected	static String tra_fba;
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

    protected	static String utr_cod;

    protected	static String cls_cod;

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

    protected	static String cml_cod;
    protected	static String cml_cmp;
    protected	static String cml_leng;
    protected	static String cml_valor;

    protected	static String tml_mun;
    protected	static String tml_pro;
    protected	static String tml_tra;
    protected	static String tml_cmp;
    protected	static String tml_leng;
    protected	static String tml_valor;

    protected	static String utml_cod;
    protected	static String utml_cmp;
    protected	static String utml_leng;
    protected	static String utml_valor;

    protected	static String dtml_mun;
    protected	static String dtml_pro;
    protected	static String dtml_tra;
    protected	static String dtml_dot;
    protected	static String dtml_cmp;
    protected	static String dtml_leng;
    protected	static String dtml_valor;

    protected	static String tdml_cod;
    protected	static String tdml_cmp;
    protected	static String tdml_leng;
    protected	static String tdml_valor;

    protected	static String sml_mun;
    protected	static String sml_tra;
    protected	static String sml_pro;
    protected	static String sml_cmp;
    protected	static String sml_leng;
    protected	static String sml_valor;

    protected	static String aml_cod;
    protected	static String aml_cmp;
    protected	static String aml_leng;
    protected	static String aml_valor;

    protected	static String pml_mun;
    protected	static String pml_cod;
    protected	static String pml_cmp;
    protected	static String pml_leng;
    protected	static String pml_valor;

    protected	static String pui_mun;
    protected	static String pui_pro;
    protected	static String pui_cod;

    protected	static String ten_mun;
    protected	static String ten_pro;
    protected	static String ten_tra;
    protected	static String ten_cod;
    protected	static String ten_des;
    protected	static String ten_url;
    protected	static String ten_est;

    protected	static String tca_mun;
    protected	static String tca_pro;
    protected	static String tca_tra;
    protected	static String tca_cod;
    protected	static String tca_des;
    protected	static String tca_plt;
    protected	static String tca_tda;
    protected	static String tca_tam;
    protected	static String tca_mas;
    protected	static String tca_obl;
    protected	static String tca_nor;
    protected	static String tca_rot;
    protected	static String tca_vis;
    protected static String tca_activo;
    protected   static String tca_desplegable;

    protected static String tda_cod;
    protected static String tda_des;
    protected static String tda_tab;

    protected static String plt_cod;
    protected static String plt_des;
    protected static String plt_url;

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

    protected	DefinicionTramitesDAO()	{
        super();
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
        // Queremos usar el	fichero de configuracion techserver
        m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError	= ConfigServiceHelper.getConfig("error");

        idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
        uor_nom	= m_ConfigTechnical.getString("SQL.A_UOR.nombre");
        uor_cod	= m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        uor_cod_vis	= m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible");

        pro_mun	= m_ConfigTechnical.getString("SQL.E_PRO.municipio");
        pro_cod	= m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
        pro_tri	= m_ConfigTechnical.getString("SQL.E_PRO.codTramiteInicio");

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
    }

    public static DefinicionTramitesDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        synchronized(DefinicionTramitesDAO.class)	{
                if (instance ==	null)	{
                    instance = new DefinicionTramitesDAO();
                }
            
        }
        return instance;
    }

    public Vector getListaClasifTramites(String[]	params) throws AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando	este metod es ejecutado
        m_Log.debug("getListaClasifTramites");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        String from =	"";
        String where = "";
        Vector list =	new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            from = cls_cod + "," + cml_valor;
            String[] join = new String[5];
            join[0] = GlobalNames.ESQUEMA_GENERICO + "A_CLS";
            join[1] = "INNER";
            join[2] = GlobalNames.ESQUEMA_GENERICO + "a_cml";
            join[3] = GlobalNames.ESQUEMA_GENERICO + "a_cls." + cls_cod + "=" + GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_cod +
                    " AND " + GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_cmp	+ "='NOM' AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_leng + " ='"+idiomaDefecto+"' ";
            join[4] = "false";

            sql =	oad.join(from,where,join);
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaClasifTramites: Sentencia SQL:" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(cls_cod),rs.getString(cml_valor),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaClasifTramites: Lista clasificación tramites cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaClasifTramites: Tamaío lista:" +	list.size());
            rs.close();
            st.close();
        }catch (Exception	e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaClasifTramites"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaClasifTramites");
        return list;
    }

    public Vector getListaTramites(DefinicionTramitesValueObject dTVO,String[] params) throws AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando	este metod es ejecutado
        m_Log.debug("getListaTramites");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        String from =	"";
        String where = "";
        Vector list =	new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            from = tra_cod + "," + tra_cou + "," + tml_valor;
            where	= tra_mun +	"=" +	dTVO.getCodMunicipio() + " AND " +
                    tra_pro +	"='" + dTVO.getTxtCodigo() + "' AND	" + tra_fba	+ " IS null";
            if(!dTVO.getNumeroTramite().equals(""))
                where += " AND " + tra_cod + "<>"	+ dTVO.getCodigoTramite();
            String[] join = new String[5];
            join[0] = "E_TRA";
            join[1] = "INNER";
            join[2] = "e_tml";
            join[3] = "e_tra." + tra_mun + "=e_tml." + tml_mun + " AND " +
                    "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                    "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                    "e_tml." + tml_cmp + "='NOM'" +	" AND	" +
                    "e_tml." + tml_leng	+ " ='"+idiomaDefecto+"' ";
            join[4] = "false";
            sql =	oad.join(from,where,join);
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTramites: Sentencia SQL:" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(tra_cod),rs.getString(tra_cou),rs.getString(tml_valor),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaTramites: Lista	tramites cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTramites: Tamaío lista:" +	list.size());
            rs.close();
            st.close();
        }catch (Exception	e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaTramites"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaTramites");
        return list;
    }

    public Vector getListaTramitesFlujoSalidaTodos(TablasIntercambiadorasValueObject tIVO,String[] params) throws AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando	este metod es ejecutado
        m_Log.debug("getListaTramitesFlujoSalidaTodos");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        String from =	"";
        String where = "";
        Vector lista = new Vector();
        Vector list =	new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            if(tIVO.getCodTramite()	!= null && !tIVO.getCodTramite().equals("")) {
                sql	= "SELECT "	+ fls_cts +	" FROM E_FLS WHERE " + fls_pro + "='" + tIVO.getCodProcedimiento() +
                        "' AND " + fls_tra + "=" + tIVO.getCodTramite() + "	AND "	+ fls_mun +	"=" +
                        tIVO.getCodMunicipio() + " AND " + fls_nuc + "=" + tIVO.getNumeroCondicionSalida();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()){
                    String codigo	= rs.getString(fls_cts);
                    lista.addElement(codigo);
                }
                st.close();

                from = tra_cod + "," + tra_cou + "," + tml_valor;
                where = tra_mun	+ "="	+ tIVO.getCodMunicipio() + " AND " + tra_pro + "='" +	tIVO.getCodProcedimiento() + "' AND	" +
                        tra_fba	+ " IS null";
                for(int m=0; m <lista.size(); m++) {
                    where += " AND " + tra_cod + "<>" + lista.elementAt(m);
                }
                String[] join =	new String[5];
                join[0] =	"E_TRA";
                join[1] =	"INNER";
                join[2] =	"e_tml";
                join[3] =	"e_tra." + tra_mun + "=e_tml." + tml_mun + " AND " +
                        "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                        "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                        "e_tml." + tml_cmp + "='NOM'"	+ " AND " +
                        "e_tml." + tml_leng + "	='"+idiomaDefecto+"' ";
                join[4] =	"false";
                sql	= oad.join(from,where,join);
            } else {
                from = tra_cod + "," + tra_cou + "," + tml_valor;
                where = tra_mun	+ "="	+ tIVO.getCodMunicipio() + " AND " + tra_pro + "='" +	tIVO.getCodProcedimiento() + "' AND	" +
                        tra_fba	+ " IS null";
                String[] join =	new String[5];
                join[0] =	"E_TRA";
                join[1] =	"INNER";
                join[2] =	"e_tml";
                join[3] =	"e_tra." + tra_mun + "=e_tml." + tml_mun + " AND " +
                        "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                        "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                        "e_tml." + tml_cmp + "='NOM'"	+ " AND " +
                        "e_tml." + tml_leng + "	='"+idiomaDefecto+"' ";
                join[4] =	"false";
                sql	= oad.join(from,where,join);
            }

            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getListaTramitesFlujoSalidaTodos: Sentencia SQL:" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(tra_cod),rs.getString(tra_cou),rs.getString(tml_valor),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionProcedimientosDAO, getListaTramites: Lista	tramites cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionProcedimientosDAO, getListaTramites: Tamaío lista:" +	list.size());
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.getListaTramites"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaTramites");
        return list; 
    }

    public TablasIntercambiadorasValueObject getListaTramitesFlujoSalidaSeleccionada(TablasIntercambiadorasValueObject tIVO,String[] params) throws	AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTramitesFlujoSalida");

        Connection con	= null;
        PreparedStatement st =	null;
        ResultSet rs =	null;
        String sql = "";
        String from = "";
        String where =	"";
        Vector listaTramitesSeleccion = new Vector();
        int orden = 0;

        try{
            AdaptadorSQLBD oad	= new	AdaptadorSQLBD(params);
            con = oad.getConnection();

            if(tIVO.getCodTramite() != null) {
                if(!tIVO.getCodTramite().equals("")) {
                    if(tIVO.getNumeroCondicionSalida().equals("2")) {
                        from =	fls_nus + "," + tra_cou	+ ","	+ fls_cts +	"," +	tml_valor +	"," +	sal_obld;
                    } else {
                        from =	fls_nus + "," + tra_cou	+ ","	+ fls_cts +	"," +	tml_valor +	"," +	sal_obl;
                    }
                    where = fls_pro + "='" + tIVO.getCodProcedimiento() + "'	AND "	+ fls_tra +	"=" +	tIVO.getCodTramite() + " AND " +
                            fls_mun + "=" + tIVO.getCodMunicipio() + "	AND "	+ fls_nuc +	"=" +	tIVO.getNumeroCondicionSalida() +
                            " AND " + tra_fba + " IS	null";

                    String join[] = new String[11];
                    join[0] = "E_FLS";
                    join[1] = "INNER";
                    join[2] = "e_tra";
                    join[3] = "e_fls." +	fls_mun + "=e_tra." + tra_mun	+ " AND e_fls." +	fls_pro + "=e_tra." + tra_pro	+
                            " AND e_fls." + fls_cts + "=e_tra." + tra_cod;
                    join[4] = "INNER";
                    join[5] = "e_tml";
                    join[6] = "e_fls." +	fls_mun + "=e_tml." + tml_mun	+ " AND e_fls." +	fls_pro + "=e_tml." + tml_pro	+
                            " AND e_fls." + fls_cts + "=e_tml." + tml_tra +
                            " AND e_tml." + tml_cmp + "='NOM'"	+
                            " AND e_tml." + tml_leng + "='"+idiomaDefecto+"'";
                    join[7] = "INNER";
                    join[8] = "e_sal";
                    join[9] = "e_fls." +	fls_mun + "=e_sal." + sal_mun	+ " AND e_fls." +	fls_pro + "=e_sal." + sal_pro	+
                            " AND e_fls." + fls_tra + "=e_sal." + sal_tra;
                    join[10]	= "false";

                    sql = oad.join(from,where,join);

                    String parametros[] = {"1","1"};
                    sql += oad.orderUnion(parametros);

                    if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getListaTramitesFlujoSalidaSeleccionada: Sentencia SQL:" + sql);
                    st = con.prepareStatement(sql);
                    rs	= st.executeQuery();
                    String entrar = "no";
                    while(rs.next()){
                        entrar	= "si";
                        DefinicionTramitesValueObject defTramVO = new DefinicionTramitesValueObject();
                        String	numeroSecuencia =	rs.getString(fls_nus);
                        defTramVO.setNumeroSecuencia(numeroSecuencia);
                        String	codTramiteFlujoSalida =	rs.getString(tra_cou);
                        defTramVO.setCodTramiteFlujoSalida(codTramiteFlujoSalida);
                        String	idTramiteFlujoSalida = rs.getString(fls_cts);
                        defTramVO.setIdTramiteFlujoSalida(idTramiteFlujoSalida);
                        String	nombreTramiteFlujoSalida = rs.getString(tml_valor);
                        defTramVO.setNombreTramiteFlujoSalida(nombreTramiteFlujoSalida);
                        if(m_Log.isDebugEnabled()) m_Log.debug(numeroSecuencia +	"|" +	codTramiteFlujoSalida +	"|" +	nombreTramiteFlujoSalida);
                        listaTramitesSeleccion.addElement(defTramVO);
                        tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
                        String	obligatorio	= "";                        
                        if(tIVO.getNumeroCondicionSalida().equals("2")) {
                            obligatorio = rs.getString(sal_obld);
                        } else	{
                            obligatorio = rs.getString(sal_obl);
                        }
                        tIVO.setObligatorio(obligatorio);
                    }
                    if(entrar.equals("no")) {
                        tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
                    }
                    rs.close();
                    st.close();
                } else {
                    tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
                }
            } else	{
                tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
            }
            m_Log.debug("DefinicionTramitesDAO, getListaTramitesFlujoSalida: Lista  tramites cargada");
        }catch	(Exception e) {
            tIVO	= null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionTramitesDAO.getListaTramitesFlujoSalida"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException	del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de	cuando este	metodo ha finalizado
        m_Log.debug("getListaTramitesFlujoSalida");
        return	tIVO;
    }

    public TablasIntercambiadorasValueObject	getListaTramitesFlujoSalida(TablasIntercambiadorasValueObject tIVO,String[] params) throws	AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTramitesFlujoSalida");

        Connection con	= null;
        PreparedStatement st =	null;
        ResultSet rs =	null;
        String sql = "";
        String from = "";
        String where =	"";
        Vector listaTramitesSeleccion = new Vector();
        int orden = 0;

        try{
            AdaptadorSQLBD oad	= new	AdaptadorSQLBD(params);
            con = oad.getConnection();

            if(tIVO.getCodTramite() != null) {
                if(!tIVO.getCodTramite().equals("")) {
                    from = fls_nus	+ ","	+ fls_nuc +	"," +	tra_cod + "," + tra_cou	+ ","	+ fls_cts +	"," +
                            tml_valor + "," + tra_cls;
                    if(tIVO.getNumeroCondicionSalida().equals("2") || tIVO.getNumeroCondicionSalida().equals("1")) {
                        where = fls_pro + "='" +	tIVO.getCodProcedimiento() + "' AND	" + fls_tra	+ "="	+ tIVO.getCodTramite() + " AND " +
                                fls_mun + "=" + tIVO.getCodMunicipio() +	" AND	(" + fls_nuc + "=1" + "	OR " + fls_nuc + "=2)";
                    } else {
                        where = fls_pro + "='" +	tIVO.getCodProcedimiento() + "' AND	" + fls_tra	+ "="	+ tIVO.getCodTramite() + " AND " +
                                fls_mun + "=" + tIVO.getCodMunicipio() + "	AND "	+ fls_nuc +	"=0";
                    }

                    String join[] = new String[11];
                    join[0] = "E_FLS";
                    join[1] = "INNER";
                    join[2] = "e_tra";
                    join[3] = "e_fls." +	fls_mun + "=e_tra." + tra_mun	+ " AND e_fls." +	fls_pro + "=e_tra." + tra_pro	+
                            " AND e_fls." + fls_cts + "=e_tra." + tra_cod;
                    join[4] = "INNER";
                    join[5] = "e_tml";
                    join[6] = "e_fls." +	fls_mun + "=e_tml." + tml_mun	+ " AND e_fls." +	fls_pro + "=e_tml." + tml_pro	+
                            " AND e_fls." + fls_cts + "=e_tml." + tml_tra +
                            " AND e_tml." + tml_cmp + "='NOM'"	+
                            " AND e_tml." + tml_leng + "='"+idiomaDefecto+"'";
                    join[7] = "INNER";
                    join[8] = "e_sal";
                    join[9] = "e_fls." +	fls_mun + "=e_sal." + sal_mun	+ " AND e_fls." +	fls_pro + "=e_sal." + sal_pro	+
                            " AND e_fls." + fls_tra + "=e_sal." + sal_tra;
                    join[10]	= "false";

                    sql = oad.join(from,where,join);

                    String parametros[] = {"1","1"};
                    sql += oad.orderUnion(parametros);

                    if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getListaTramitesFlujoSalida: Sentencia SQL:" + sql);
                    st = con.prepareStatement(sql);
                    rs	= st.executeQuery();
                    String entrar = "no";
                    while(rs.next()){
                        entrar	= "si";
                        DefinicionTramitesValueObject defTramVO = new DefinicionTramitesValueObject();
                        String	numeroSecuencia =	rs.getString(fls_nus);
                        defTramVO.setNumeroSecuencia(numeroSecuencia);
                        String	numeroCondSalida = rs.getString(fls_nuc);
                        defTramVO.setNumeroCondicionSalida(numeroCondSalida);
                        String	cTramiteFlujoSalida = rs.getString(tra_cod);
                        defTramVO.setTxtCodigo(cTramiteFlujoSalida);
                        String	codTramiteFlujoSalida =	rs.getString(tra_cou);
                        defTramVO.setCodTramiteFlujoSalida(codTramiteFlujoSalida);
                        String	idTramiteFlujoSalida = rs.getString(fls_cts);
                        defTramVO.setIdTramiteFlujoSalida(idTramiteFlujoSalida);
                        String	nombreTramiteFlujoSalida = rs.getString(tml_valor);
                        defTramVO.setNombreTramiteFlujoSalida(nombreTramiteFlujoSalida);
                        String	codClasificacionTramite	= rs.getString(tra_cls);
                        defTramVO.setCodClasifTramite(codClasificacionTramite);
                        listaTramitesSeleccion.addElement(defTramVO);
                        tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
                    }
                    if(entrar.equals("no")) {
                        tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
                    }
                    rs.close();
                    st.close();
                } else {
                    tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
                }
            } else	{
                tIVO.setListaTramitesSeleccion(listaTramitesSeleccion);
            }
            m_Log.debug("DefinicionTramitesDAO, getListaTramitesFlujoSalida: Lista  tramites cargada");
        }catch	(Exception e) {
            tIVO	= null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionTramitesDAO.getListaTramitesFlujoSalida"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException	del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de	cuando este	metodo ha finalizado
        m_Log.debug("getListaTramitesFlujoSalida");
        return	tIVO;
    }

    public int	grabarFlujoSalida(TablasIntercambiadorasValueObject tabInterVO,String[]	params) throws TechnicalException,BDException,AnotacionRegistroException{
        String	sql= "";
        String	sqlBorrar =	"";
        int res = 0;
        int res1 = 0;
        int resBorrar =0;
        AdaptadorSQLBD abd	= null;
        Connection conexion = null;
        m_Log.debug("Entra en grabarFlujoSalida del	DAO");

        try{
            m_Log.debug("A por el	OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la	conexion");

            conexion =	abd.getConnection();
            abd.inicioTransaccion(conexion);

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
            if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
            PreparedStatement ps =	conexion.prepareStatement(sqlBorrar);
            resBorrar = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_FLS son ::::::::::::::	: " +	resBorrar);
            if(m_Log.isDebugEnabled()) m_Log.debug("el	tamaío de la lista de tramites seleccionados es	: " +	listaCodTramitesFlujoSalida.size());

            for(int l=0;l<listaCodTramitesFlujoSalida.size();l++) {
                res = 0;
                sql = "INSERT INTO E_FLS (" + fls_mun + "," + fls_pro + "," + fls_tra + "," + fls_nuc +
                        "," + fls_nus + "," + fls_cts + ") VALUES(" + tabInterVO.getCodMunicipio()	+ ",'" +
                        tabInterVO.getCodProcedimiento() +	"'," + tabInterVO.getCodTramite() +	"," +
                        tabInterVO.getNumeroCondicionSalida() + "," + listaNumerosSecuenciaFlujoSalida.elementAt(l) + "," +
                        listaCodTramitesFlujoSalida.elementAt(l)	+ ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps	= conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de	E_FLS	son :::::::::::::: : " + res);
            }

            if(tabInterVO.getNumeroCondicionSalida().equals("2") ) {
                sql	= "UPDATE E_SAL SET " +	sal_obld + "=" + tabInterVO.getObligatorio() +	" WHERE " +	 sal_mun + "=" + tabInterVO.getCodMunicipio() +
                        " AND	" + sal_pro	+ "='" + tabInterVO.getCodProcedimiento()	+
                        "' AND " + sal_tra + "=" + tabInterVO.getCodTramite();
            } else if(tabInterVO.getNumeroCondicionSalida().equals("0") || tabInterVO.getNumeroCondicionSalida().equals("1") ){
                sql	= "UPDATE E_SAL SET " +	sal_obl + "=" + tabInterVO.getObligatorio() + "," +	sal_obld + "=null" +
                        " WHERE " +	sal_mun + "=" + tabInterVO.getCodMunicipio() +
                        " AND	" + sal_pro	+ "='" + tabInterVO.getCodProcedimiento()	+
                        "' AND " + sal_tra + "=" + tabInterVO.getCodTramite();
            } else {
                sql	= "UPDATE E_SAL SET " +	sal_obl + "=null" + "," +	sal_obld + "=null" +
                        " WHERE " +	sal_mun + "=" + tabInterVO.getCodMunicipio() +
                        " AND	" + sal_pro	+ "='" + tabInterVO.getCodProcedimiento()	+
                        "' AND " + sal_tra + "=" + tabInterVO.getCodTramite();
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el update de E_SAL son	:::::::::::::: : " + res1);

            abd.finTransaccion(conexion);

        } catch (Exception ex) {
            abd.rollBack(conexion);
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        } finally {
            abd.devolverConexion(conexion);
        }
        return	res;
    }


    public Vector getListaCodTramites(DefinicionTramitesValueObject	defTramVO,String codMunicipio,String[] params) throws AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando	este metod es ejecutado
        m_Log.debug("getListaCodTramites");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list =	new Vector();
        String codProcedimiento =	defTramVO.getTxtCodigo();
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            sql =	"SELECT " +	tra_cod + "," + tra_cou	+  " FROM  E_TRA WHERE " +
                    tra_mun + "=" + codMunicipio + " AND " + tra_pro + "='" + codProcedimiento + "' AND	" +
                    tra_fba + "	IS null";
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getListaCodTramites: Sentencia SQL:" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                String codTramite = rs.getString(tra_cod);
                list.addElement(codTramite);
            }
            m_Log.debug("DefinicionTramitesDAO, getListaCodTramites: Lista codigos tramites cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getListaCodTramites: Tamaío lista:" + list.size());
            rs.close();
            st.close();
        }catch (Exception	e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionTramitesDAO.getListaCodTramites"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando	este metodo	ha finalizado
        m_Log.debug("getListaCodTramites");
        return list;
    }

    public DefinicionTramitesValueObject getTramite(DefinicionTramitesValueObject defTramVO,String codMunicipio,Connection con, String[] params) throws AnotacionRegistroException, TechnicalException{
        return getTramite(defTramVO, codMunicipio, con, params, false);
    }

    public DefinicionTramitesValueObject getTramite(DefinicionTramitesValueObject defTramVO,String codMunicipio,Connection con, String[] params, boolean devolverDatosFlujo) throws AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando	este metod es ejecutado
        m_Log.debug("entra	en getTramite en el DAO");

        PreparedStatement st = null,st2 = null;
        ResultSet rs = null,rs2 = null;
        String sql = "",sql2 = "";
        String from ="";
        String where = "";
        String codProcedemento = defTramVO.getTxtCodigo();
        String codTramite =	defTramVO.getCodigoTramite();
        Vector listasCondEntrada = new Vector();
        Vector listaDocumentos = new Vector();
        Vector listaDocusErroneos = new Vector();
        Vector listasCampos = new Vector();
        Vector listaAgrupaciones = new Vector();

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);

            // PESTAÑA DATOS

            /* original
            from = "TRA_MUN," + tra_cod + "," + tra_cou + "," + tml_valor + ","  + tra_vis	+ ","	+ tra_uin +
                    "," +	tra_utr + "," + tra_plz	+ ","	+ tra_und +",TRA_FIN," +tra_ocu + "," + tra_ins + "," +
                    tra_cls + "," + cml_valor + "," + pro_tri + "," + tra_pre + ",e_pml." + pml_valor + " AS " + pml_valor +
                    ",pml2." + pml_valor + " AS " + pml_valor + "2" +
                    "," + tra_uti + "," + tra_utf + "," + tra_usi  + "," + tra_usf  + "," + tra_ini  + "," + tra_inf  +
                    "," + tra_prr + "," + tra_car + "," + car_cod_vis +", TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP,"+
                    "TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA";
            */
            from = "TRA_MUN," + tra_cod + "," + tra_cou + "," + tml_valor + ","  + tra_vis	+ ","	+ tra_uin +
                    "," +	tra_utr + "," + tra_plz	+ ","	+ tra_und +",TRA_FIN," +tra_ocu + "," + tra_ins + "," +
                    tra_cls + "," + cml_valor + "," + pro_tri + "," + tra_pre + ",e_pml." + pml_valor + " AS " + pml_valor +
                    ",pml2." + pml_valor + " AS " + pml_valor + "2" +
                    "," + tra_uti + "," + tra_utf + "," + tra_usi  + "," + tra_usf  + "," + tra_ini  + "," + tra_inf  +
                    "," + tra_prr + "," + tra_car + "," + car_cod_vis +", TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP,"+
                    "TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,"
                    + "TRA_TRAM_EXT_IMPLCLASS, TRA_NOTIF_ELECT_OBLIG, TRA_NOTIF_FIRMA_CERT_ORG,COD_DEPTO_NOTIFICACION, TRA_NOTIF_UITI, TRA_NOTIF_UITF, TRA_NOTIF_UIEI, TRA_NOTIF_UIEF, TRA_NOTIFICADO , TRA_NOTIF_USUTRA_FINPLAZO," +
                    "TRA_NOTIF_USUEXP_FINPLAZO, TRA_NOTIF_UOR_FINPLAZO";
            where	= tra_pro +	"='" + codProcedemento + "' AND " +	tra_cod + "=" + codTramite + " AND " +
                    tra_mun + "=" + codMunicipio + " AND " + tra_fba + " IS null";

            String join[] = new String[20];
            join[0] = "E_TRA";
            join[1] = "INNER";
            join[2] = "e_tml";
            join[3] = "e_tra." + tra_mun + "=e_tml." + tml_mun + " AND " +
                    "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                    "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                    "e_tml." + tml_cmp + "='NOM'" +	" AND	" +
                    "e_tml." + tml_leng	+ "='"+idiomaDefecto+"'";
            join[4] = "INNER";
            join[5] = GlobalNames.ESQUEMA_GENERICO + "a_cml";
            join[6] = "e_tra." + tra_cls + "=" + GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_cod + " AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_cmp + "='NOM'" +	" AND	" +
                    GlobalNames.ESQUEMA_GENERICO + "a_cml." + cml_leng	+ "='"+idiomaDefecto+"'";
            join[7] = "LEFT";
            join[8] = "e_pro";
            join[9] = "e_tra." + tra_mun + "=e_pro." + pro_mun + " AND " +
                    "e_tra." + tra_pro + "=e_pro." + pro_cod;
            join[10] = "INNER";
            join[11] = "e_pml";
            join[12] = "e_tra." + tra_mun	+ "=e_pml."	+ pml_mun +	" AND	" +
                    "e_tra." + tra_pro	+ "=e_pml."	+ pml_cod +	" AND	" +
                    "e_pml." + pml_cmp	+ "='NOM'" + " AND " +
                    "e_pml." + pml_leng + "='"+idiomaDefecto+"'";
            join[13] = "LEFT";
            join[14] = "e_pml pml2";
            join[15] = "e_tra." + tra_mun	+ "=pml2."	+ pml_mun +	" AND	" +
                    "e_tra." + tra_prr	+ "=pml2."	+ pml_cod +	" AND	" +
                    "'NOM'=" + "pml2." + pml_cmp	+ " AND " +
                    "'"+idiomaDefecto+"'=" + "pml2." + pml_leng;
            join[16] = "LEFT";
            join[17] = "a_car";
            join[18] = "e_tra." + tra_car + "=a_car."	+ car_cod;
            join[19] = "false";

            sql =	oad.join(from,where,join);

            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getTramite: Sentencia SQL:" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                defTramVO.setCodMunicipio(rs.getString("TRA_MUN"));
                String codigoTramite = rs.getString(tra_cod);
                defTramVO.setCodigoTramite(codigoTramite);
                defTramVO.setCodigoInternoTramite(codigoTramite);
                String numeroTramite = rs.getString(tra_cou);
                defTramVO.setNumeroTramite(numeroTramite);
                String nombreTramite = rs.getString(tml_valor);
                defTramVO.setNombreTramite(nombreTramite);
                String disponible = rs.getString(tra_vis);
                defTramVO.setDisponible(disponible);
                String codUnidadInicio = rs.getString(tra_uin);
                defTramVO.setCodUnidadInicio(codUnidadInicio);
                String codUnidadTramite = rs.getString(tra_utr);
                defTramVO.setCodUnidadTramite(codUnidadTramite);
                String plazo = rs.getString(tra_plz);
                defTramVO.setPlazo(plazo);
                String unidadesPlazo = rs.getString(tra_und);
                int plazoFin = rs.getInt("TRA_FIN");
                defTramVO.setPlazoFin(plazoFin);
                defTramVO.setUnidadesPlazo(unidadesPlazo);
                String ocurrencias = rs.getString(tra_ocu);
                defTramVO.setOcurrencias(ocurrencias);
                String codClasificacionTramite = rs.getString(tra_cls);
                defTramVO.setCodClasifTramite(codClasificacionTramite);
                String descClasificacionTramite =	rs.getString(cml_valor);
                defTramVO.setDescClasifTramite(descClasificacionTramite);
                String codTramiteInicio = rs.getString(pro_tri);
                if(codTramiteInicio != null	&& codTramiteInicio.equals(codTramite))
                    defTramVO.setTramiteInicio("1");
                else
                    defTramVO.setTramiteInicio("0");
                String tramitePregunta = rs.getString(tra_pre);
                defTramVO.setTramitePregunta(tramitePregunta);
                String descProcedimiento = rs.getString(pml_valor);
                defTramVO.setTxtDescripcion(descProcedimiento);
                String instrucc = rs.getString(tra_ins);
                if (instrucc != null)
                    defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(instrucc));
                else defTramVO.setInstrucciones(null);

                String notUnidadTramitIni = rs.getString(tra_uti);
                if (!notUnidadTramitIni.equals("N"))
                    defTramVO.setNotUnidadTramitIni("1");
                else
                    defTramVO.setNotUnidadTramitIni("0");

                String notUnidadTramitFin = rs.getString(tra_utf);
                if (!notUnidadTramitFin.equals("N"))
                    defTramVO.setNotUnidadTramitFin("1");
                else
                    defTramVO.setNotUnidadTramitFin("0");

                String notUsuUnidadTramitIni = rs.getString(tra_usi);
                if (!notUsuUnidadTramitIni.equals("N"))
                    defTramVO.setNotUsuUnidadTramitIni("1");
                else
                    defTramVO.setNotUsuUnidadTramitIni("0");

                String notUsuUnidadTramitFin = rs.getString(tra_usf);
                if (!notUsuUnidadTramitFin.equals("N"))
                    defTramVO.setNotUsuUnidadTramitFin("1");
                else
                    defTramVO.setNotUsuUnidadTramitFin("0");

                String notInteresadosIni = rs.getString(tra_ini);
                if (!notInteresadosIni.equals("N"))
                    defTramVO.setNotInteresadosIni("1");
                else
                    defTramVO.setNotInteresadosIni("0");

                String notInteresadosFin = rs.getString(tra_inf);
                if (!notInteresadosFin.equals("N"))
                    defTramVO.setNotInteresadosFin("1");
                else
                    defTramVO.setNotInteresadosFin("0");

                String notUsuInicioTramiteIni = rs.getString("TRA_NOTIF_UITI");
                if(!notUsuInicioTramiteIni.equals("N")){
                    defTramVO.setNotUsuInicioTramiteIni("1");
                }else{
                    defTramVO.setNotUsuInicioTramiteIni("0");
                }
                
                String notUsuInicioTramiteFin = rs.getString("TRA_NOTIF_UITF");
                if(!notUsuInicioTramiteFin.equals("N")){
                    defTramVO.setNotUsuInicioTramiteFin("1");
                }else{
                    defTramVO.setNotUsuInicioTramiteFin("0");
                }
                
                String notUsuInicioExpedIni = rs.getString("TRA_NOTIF_UIEI");
                if(!notUsuInicioExpedIni.equals("N")){
                    defTramVO.setNotUsuInicioExpedIni("1");
                }else{
                    defTramVO.setNotUsuInicioExpedIni("0");
                }
                
                String notUsuInicioExpedFin = rs.getString("TRA_NOTIF_UIEF");
                if(!notUsuInicioExpedFin.equals("N")){
                    defTramVO.setNotUsuInicioExpedFin("1");
                }else{
                    defTramVO.setNotUsuInicioExpedFin("0");
                }
                
                String notUsuTraFinPlazo = rs.getString("TRA_NOTIF_USUTRA_FINPLAZO");	
                if(!notUsuTraFinPlazo.equals("N")){	
                    defTramVO.setNotUsuTraFinPlazo("1");	
                }else{	
                    defTramVO.setNotUsuTraFinPlazo("0");	
                }	
                String notUsuExpFinPlazo = rs.getString("TRA_NOTIF_USUEXP_FINPLAZO");	
                if(!notUsuExpFinPlazo.equals("N")){	
                    defTramVO.setNotUsuExpFinPlazo("1");	
                }else{	
                    defTramVO.setNotUsuExpFinPlazo("0");	
                }	
                String notUORFinPlazo = rs.getString("TRA_NOTIF_UOR_FINPLAZO");	
                if(!notUORFinPlazo.equals("N")){
	
                    defTramVO.setNotUORFinPlazo("1");	
                }else{	
                    defTramVO.setNotUORFinPlazo("0");	
                }
                
                defTramVO.setCodExpRel(rs.getString(tra_prr));
                defTramVO.setDescExpRel(rs.getString(pml_valor + "2"));
                defTramVO.setCodCargo(rs.getString(tra_car));
                defTramVO.setCodVisibleCargo(rs.getString(car_cod_vis));
                defTramVO.setGenerarPlazos(rs.getBoolean("TRA_GENERARPLZ"));
                defTramVO.setNotificarCercaFinPlazo(rs.getBoolean("TRA_NOTCERCAFP"));
                defTramVO.setNotificarFueraDePlazo(rs.getBoolean("TRA_NOTFUERADP"));
                defTramVO.setTipoNotCercaFinPlazo(rs.getInt("TRA_TIPNOTCFP"));
                defTramVO.setTipoNotFueraDePlazo(rs.getInt("TRA_TIPNOTFDP"));

                
                
                defTramVO. setTipoUsuarioFirma(rs.getString("TRA_TIPO_USUARIO_FIRMA"));
                defTramVO. setCodigoOtroUsuarioFirma(rs.getString("TRA_OTRO_COD_USUARIO_FIRMA"));
                defTramVO.setCodPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_PLUGIN"));
                defTramVO.setImplClassPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_IMPLCLASS"));
                defTramVO.setUrlPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_URL"));
                
                //INICIO #76248 Asignamos los valores de los campos nuevos
                 defTramVO.setTramiteNotificado(rs.getInt("TRA_NOTIFICADO")==1);
                 
                defTramVO.setAdmiteNotificacionElectronica(rs.getString("TRA_NOTIFICACION_ELECTRONICA") != null && rs.getString("TRA_NOTIFICACION_ELECTRONICA").equals("1") ? "1" : "0");
                defTramVO.setCodigoTipoNotificacionElectronica(rs.getString("TRA_COD_TIPO_NOTIFICACION"));
                 defTramVO.setCodDepartamentoNotificacion(rs.getString("COD_DEPTO_NOTIFICACION"));
                DepartamentoNotificacionSneVO departamento = this.getDepartamentoNotificacionSNEPorCodigo(con, defTramVO.getCodDepartamentoNotificacion());
                defTramVO.setDescripcionDepartamentoNotificacion(departamento.getDescripcion());
                
                
                defTramVO.setNotificacionElectronicaObligatoria(rs.getInt("TRA_NOTIF_ELECT_OBLIG")==1);
                defTramVO.setCertificadoOrganismoFirmaNotificacion(rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG")==1);
               

            }
            rs.close();
            st.close();

            if(defTramVO.getCodUnidadInicio() == null	|| defTramVO.getCodUnidadInicio().equals("")) {
                defTramVO.setCodUnidadInicio("");
                defTramVO.setCodVisibleUnidadInicio("");
                defTramVO.setDescUnidadInicio("");
            } else {
              defTramVO.setCodVisibleUnidadInicio("");
              defTramVO.setDescUnidadInicio("");
              
              UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],defTramVO.getCodUnidadInicio());

              if (uorDTO!=null){
                  defTramVO.setDescUnidadInicio(uorDTO.getUor_nom());
                  defTramVO.setCodVisibleUnidadInicio(uorDTO.getUor_cod_vis());
              }             
            }

            if(defTramVO.getCodUnidadTramite() == null || defTramVO.getCodUnidadTramite().equals(""))	{
                defTramVO.setCodUnidadTramite("");
                defTramVO.setDescUnidadTramite("");
            } else {
                defTramVO.setDescUnidadTramite("");
                
                UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],defTramVO.getCodUnidadTramite());

                if (uorDTO!=null){
                    defTramVO.setDescUnidadInicio(uorDTO.getUor_nom());
                    defTramVO.setCodVisibleUnidadInicio(uorDTO.getUor_cod_vis());
                } 
            }

            //m_Log.info("DefinicionTramitesDAO, getTramite ");

            // PESTAÑA DOCUMENTOS

            st = null;
			listaDocumentos = obtenerDocumentos(defTramVO, con, st, rs);
			
			if (devolverDatosFlujo) {
				for(Object obj : listaDocumentos){
					DefinicionTramitesValueObject docVO = (DefinicionTramitesValueObject) obj;
					// El valor en E_DOT (DOT_FRM) puede ser:
					// - id de usuario: el documento tiene firma de usuario
					// - -1: el documento tiene una firma de flujo. Tablas FIRMA_FLUJO y FIRMA_CIRCUITO
					// - null o vacio: el documento no tiene firma, ni de usuario ni de flujo					
					if(!docVO.getFirma().isEmpty()) { 
						obtenerDatosFirmasDocumento(docVO, con);
					} else {
						docVO.setFirmaDocumentoIdUsuario(null);
						docVO.setFirmaDocumentoUsuarioLog(null);
						docVO.setFirmaFlujo(null);
						docVO.setListaFirmasCircuito(null);
					}
				}
			} 
			
			defTramVO.setListaDocumentos(listaDocumentos);
			if (st != null) {
				st.close();
            }
            rs.close();

            from = dot_cod + "," + dot_plt;
            where	= dot_mun +	"=" +	defTramVO.getCodMunicipio() +	" AND " + dot_pro + "='" + defTramVO.getTxtCodigo() +	"' AND " +
                    dot_tra +	"=" +	defTramVO.getCodigoTramite();
            String[] join3 = new String[5];
            join3[0] = "E_DOT";
            join3[1] = "INNER";
            join3[2] = "a_plt";
            join3[3] = "e_dot." + dot_plt	+ "=a_plt." + aplt_cod + " AND " +
                    "e_dot." + dot_pro	+ "<>a_plt." + aplt_pro + " AND " +
                    "e_dot." + dot_tra	+ "=a_plt." + aplt_tra;
            join3[4] = "false";

            sql =	oad.join(from,where,join3);

            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_DOT es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 = "si";
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                String codDocumento =	rs.getString(dot_cod);
                dTVO.setCodigoDoc(codDocumento);
                dTVO.setCodigoTramite(codTramite);
                String codPlantilla =	rs.getString(dot_plt);
                dTVO.setCodPlantilla(codPlantilla);
                listaDocusErroneos.addElement(dTVO);
                defTramVO.setListaDocusErroneos(listaDocusErroneos);
            }
            if(entrar1.equals("no"))	{
                defTramVO.setListaDocusErroneos(listaDocusErroneos);
            }
            rs.close();
            st.close();

            // PESTAÑA CONDICIONES DE ENTRADA

            sql = "select " + ent_cod + "," + ent_tipo + "," + ent_exp + "," + ent_ctr + "," + ent_est + ", ENT_DOC from e_ent " +
                    " where " + ent_mun + "=" +	defTramVO.getCodMunicipio() + " AND	" + ent_pro	+ "='" + defTramVO.getTxtCodigo() +	"' AND " +
                    ent_tra  + "=" + defTramVO.getCodigoTramite();

            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_ENT es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar2 = "no";
            while(rs.next()) {
                entrar2 =	"si";
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                String tipo = rs.getString(ent_tipo);
                if ("E".equals(tipo)) {
                    dTVO.setTipoCondEntrada("EXPRESION");
                    String exp = rs.getString(ent_exp);
                    dTVO.setExpresionCondEntrada(exp);
                } else if ("T".equals(tipo)) {
                    from = ent_mun + "," + ent_pro + "," + ent_tra + "," + ent_cod + "," + ent_ctr + "," + tra_cou + "," +
                            tml_valor + "," +	ent_est	+ "," +	ent_tipo + "," + ent_exp;
                    where	= ent_mun +	"=" +	defTramVO.getCodMunicipio() +	" AND	" + ent_pro	+ "='" + defTramVO.getTxtCodigo() +	"' AND " +
                            ent_tra +	"=" +	defTramVO.getCodigoTramite() + " AND " + tra_fba + " IS NULL" +	" AND " + ent_ctr +	"=" + rs.getString(ent_ctr) ;
                    String[] join1 = new String[8];
                    join1[0] = "E_ENT";
                    join1[1] = "INNER";
                    join1[2] = "e_tra";
                    join1[3] = "e_ent." + ent_mun	+ "=e_tra."	+ tra_mun +	" AND	" +
                            "e_ent." + ent_pro	+ "=e_tra."	+ tra_pro +	" AND	" +
                            "e_ent." + ent_ctr	+ "=e_tra."	+ tra_cod;
                    join1[4] = "INNER";
                    join1[5] = "e_tml";
                    join1[6] = "e_ent." + ent_mun	+ "=e_tml."	+ tml_mun +	" AND	" +
                            "e_ent." + ent_pro + "=e_tml." + tml_pro + " AND " +
                            "e_ent." + ent_ctr + "=e_tml." + tml_tra + " AND " +
                            "e_tml." + tml_cmp + "='NOM'" +	" AND	" +
                            "e_tml." + tml_leng	+ "='"+idiomaDefecto+"'";
                    join1[7] = "false";

                    sql2 =	oad.join(from,where,join1);
                    if(m_Log.isDebugEnabled()) m_Log.debug("la subconsulta de E_ENT es : "	+ sql2);
                    st2 = con.prepareStatement(sql2);
                    rs2 = st2.executeQuery();
                    while(rs2.next()) {
                        String idTra = rs2.getString(ent_ctr);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Id Tramite : " + idTra);
                        dTVO.setIdTramiteCondEntrada(idTra);
                        String codTra =	rs2.getString(tra_cou);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Cod Tramite : " + codTra);
                        dTVO.setCodTramiteCondEntrada(codTra);
                        String descTramite = rs2.getString(tml_valor);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Desc Tramite : " + descTramite);
                        dTVO.setDescTramiteCondEntrada(descTramite);
                        String estadoTramite = rs2.getString(ent_est);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Estado Tramite : " + estadoTramite);
                        if("F".equals(estadoTramite))
                            dTVO.setEstadoTramiteCondEntrada("FINALIZADO");
                        else if("I".equals(estadoTramite))
                            dTVO.setEstadoTramiteCondEntrada("INICIADO");
                        else if("O".equals(estadoTramite))
                            dTVO.setEstadoTramiteCondEntrada("NO INICIADO");
                        else if("S".equals(estadoTramite))
                            dTVO.setEstadoTramiteCondEntrada("FAVORABLE");
                        else if("N".equals(estadoTramite))
                            dTVO.setEstadoTramiteCondEntrada("DESFAVORABLE");
                        else if("C".equals(estadoTramite))
                            dTVO.setEstadoTramiteCondEntrada("NO INICIADO O FINALIZADO");
                        dTVO.setTipoCondEntrada("TRÁMITE");
                    }
                    rs2.close();
                    st2.close();
                } else if ("D".equals(tipo)) {
                    dTVO.setTipoCondEntrada("DOCUMENTO");
                    dTVO.setIdTramiteCondEntrada("");
                    String estadoDocumento = rs.getString(ent_est);
                    if ("F".equals(estadoDocumento)) {
                        dTVO.setEstadoTramiteCondEntrada("FIRMADO");
                    } else if ("O".equals(estadoDocumento)) {
                        dTVO.setEstadoTramiteCondEntrada("PENDIENTE");
                    } else if ("R".equals(estadoDocumento)) {
                        dTVO.setEstadoTramiteCondEntrada("RECHAZADO");
                    } else if ("C".equals(estadoDocumento)) {
                        dTVO.setEstadoTramiteCondEntrada("CIRCUITO FINALIZADO");
                    } else if ("S".equals(estadoDocumento)) {
                        dTVO.setEstadoTramiteCondEntrada("SUBSANADO");
                    } 
                    String codDoc = rs.getString("ENT_DOC");
                    dTVO.setCodigoDoc(codDoc);
                    sql2 = "SELECT DPML_VALOR FROM E_DPML WHERE DPML_MUN = " + defTramVO.getCodMunicipio()+ 
                            " AND DPML_PRO = '" + defTramVO.getTxtCodigo() + "' AND DPML_DOP = " + codDoc +
                            " AND DPML_CMP='NOM' AND DPML_LENG= " + idiomaDefecto;
                    if(m_Log.isDebugEnabled()) m_Log.debug("la subconsulta de E_ENT es : "	+ sql2);
                    st2 = con.prepareStatement(sql2);
                    rs2 = st2.executeQuery();
                    if (rs2.next()) {
                        dTVO.setDescTramiteCondEntrada(rs2.getString("DPML_VALOR"));
                    }
                    rs2.close();
                    st2.close();
                }
                String codCondicion =	rs.getString(ent_cod);
                dTVO.setCodCondEntrada(codCondicion);
                listasCondEntrada.addElement(dTVO);
                defTramVO.setListasCondEntrada(listasCondEntrada);
            }
            if(entrar2.equals("no")) {
                defTramVO.setListasCondEntrada(listasCondEntrada);
            }
            rs.close();
            st.close();

            //PESTAÑA CONDICIONES DE SALIDA

            from = sal_tac + "," + sal_taa + "," + sal_tan + "," + sml_valor;
            where	= sal_mun +	"=" +	defTramVO.getCodMunicipio() +
                    " AND " +	sal_pro + "='" + defTramVO.getTxtCodigo()	+ "' AND " + sal_tra + "=" +
                    defTramVO.getCodigoTramite();
            String[] join4 = new String[5];
            join4[0] = "E_SAL";
            join4[1] = "INNER";
            join4[2] = "e_sml";
            join4[3] = "e_sal." + sal_mun	+ "=e_sml."	+ sml_mun +	" AND	" +
                    "e_sal." + sal_pro	+ "=e_sml."	+ sml_pro +	" AND	" +
                    "e_sal." + sal_tra	+ "=e_sml."	+ sml_tra +	" AND	" +
                    "e_sml." + sml_cmp	+ "='TXT'" + " AND " +
                    "e_sml." + sml_leng + "='"+idiomaDefecto+"'";
            join4[4] = "false";
            sql =	oad.join(from,where,join4);

            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_SAL es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar3 = "no";
            while(rs.next()) {
                entrar3 =	"si";
                String tipoCondicion = rs.getString(sal_tac);
                if(m_Log.isDebugEnabled()) m_Log.debug("el tipo	de condicion es : " + tipoCondicion);
                if("T".equals(tipoCondicion)) {
                    defTramVO.setTipoCondicion("Tramite");
                    defTramVO.setTexto("");
                } else if("F".equals(tipoCondicion)) {
                    defTramVO.setTipoCondicion("Finalizacion");
                    defTramVO.setTexto("");
                } else if("R".equals(tipoCondicion)) {
                    defTramVO.setTipoCondicion("Resolucion");
                    defTramVO.setTexto("");
                } else if("P".equals(tipoCondicion)) {
                    defTramVO.setTipoCondicion("Pregunta");
                    String texto = rs.getString(sml_valor);
                    defTramVO.setTexto(texto);
                } else {
                    defTramVO.setTipoCondicion("");
                    defTramVO.setTexto("");
                }
                String tipoCondicionFav = rs.getString(sal_taa);
                if("T".equals(tipoCondicionFav))
                    defTramVO.setTipoFavorableSI("TramiteSI");
                else if("F".equals(tipoCondicionFav))
                    defTramVO.setTipoFavorableSI("FinalizacionSI");
                else
                    defTramVO.setTipoFavorableSI("");
                String tipoCondicionDesf = rs.getString(sal_tan);
                if("T".equals(tipoCondicionDesf))
                    defTramVO.setTipoFavorableNO("TramiteNO");
                else if("F".equals(tipoCondicionDesf))
                    defTramVO.setTipoFavorableNO("FinalizacionNO");
                else
                    defTramVO.setTipoFavorableNO("");
            }
            rs.close();
            st.close();
            if("no".equals(entrar3)) {
                sql	= "SELECT "	+ sal_tac +	"," +	sal_taa + "," + sal_tan	+ " FROM E_SAL WHERE " +
                        sal_mun +	"=" +	defTramVO.getCodMunicipio() +	" AND	" + sal_pro	+ "='" +
                        defTramVO.getTxtCodigo() + "' AND	" + sal_tra	+ "="	+ defTramVO.getCodigoTramite();
                if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_SAL es :	" + sql);
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()) {
                    String tipoCondicion = rs.getString(sal_tac);
                    if("T".equals(tipoCondicion))
                        defTramVO.setTipoCondicion("Tramite");
                    else if("F".equals(tipoCondicion))
                        defTramVO.setTipoCondicion("Finalizacion");
                    else if("R".equals(tipoCondicion))
                        defTramVO.setTipoCondicion("Resolucion");
                    else if("P".equals(tipoCondicion))
                        defTramVO.setTipoCondicion("Pregunta");
                    else
                        defTramVO.setTipoCondicion("");
                    String tipoCondicionFav =	rs.getString(sal_taa);
                    if("T".equals(tipoCondicionFav))
                        defTramVO.setTipoFavorableSI("TramiteSI");
                    else if("F".equals(tipoCondicionFav))
                        defTramVO.setTipoFavorableSI("FinalizacionSI");
                    else
                        defTramVO.setTipoFavorableSI("");
                    String tipoCondicionDesf = rs.getString(sal_tan);
                    if("T".equals(tipoCondicionDesf))
                        defTramVO.setTipoFavorableNO("TramiteNO");
                    else if("F".equals(tipoCondicionDesf))
                        defTramVO.setTipoFavorableNO("FinalizacionNO");
                    else
                        defTramVO.setTipoFavorableNO("");
                    defTramVO.setTexto("");
                }
                rs.close();
                st.close();
            }

            // PESTAÑA CAMPOS
            
            //Agrupaciones de campos
            sql = "Select TCA_ID_GROUP, TCA_DESC_GROUP, TCA_ORDER_GROUP, TCA_ACTIVE "
                    + " From E_TCA_GROUP "
                    + " Where TCA_PRO = '" + defTramVO.getTxtCodigo() + "'"
                    + " And TCA_TRA = " + defTramVO.getCodigoTramite() 
                    + " order by TCA_ORDER_GROUP ASC ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while ( rs.next() ) {
                DefinicionAgrupacionCamposValueObject dacVO = new DefinicionAgrupacionCamposValueObject();
                dacVO.setCodAgrupacion(rs.getString("TCA_ID_GROUP"));
                dacVO.setDescAgrupacion(rs.getString("TCA_DESC_GROUP"));
                dacVO.setOrdenAgrupacion(rs.getInt("TCA_ORDER_GROUP"));
                dacVO.setAgrupacionActiva(rs.getString("TCA_ACTIVE"));
                listaAgrupaciones.add(dacVO);
            }//while ( rs.next() )
            defTramVO.setListaAgrupaciones(listaAgrupaciones);

            from = tca_cod + "," + tca_des + "," + tca_plt + "," + tca_tda + "," + tca_tam + "," + tca_mas +
             "," + tca_obl + "," + tca_nor + "," + tda_des + "," + plt_des + "," + tca_rot + "," + tca_vis +
             "," + tca_activo + "," + tca_desplegable+",TCA_OCULTO,TCA_BLOQ,PLAZO_AVISO, PERIODO_PLAZO" + 
			 ",EXPRESION_CAMPO_NUM_TRAM.EXPRESION AS EXPRESION ," +
             "EXPRESION_CAMPO_CAL_TRAM.EXPRESION AS EXPRESION_CAL , " + tca_tra + ",PCA_GROUP, TCA_POS_X, TCA_POS_Y";
            where = tca_mun + "=" + codMunicipio + " AND " + tca_pro + "='" + defTramVO.getTxtCodigo() +
                    "' AND " + tca_tra + "=" + defTramVO.getCodigoTramite();
            String[] join5 = new String[14];
            join5[0] = "E_TCA";
            join5[1] = "INNER";
            join5[2] = "e_tda";
            join5[3] = "e_tca." + tca_tda + "=e_tda." + tda_cod;
            join5[4] = "INNER";
            join5[5] = "e_plt";
            join5[6] = "e_tca." + tca_plt + "=e_plt." + plt_cod;
            join5[7] = "LEFT";
            join5[8] = "EXPRESION_CAMPO_NUM_TRAM";
            join5[9] = "e_tca." + tca_mun + " = EXPRESION_CAMPO_NUM_TRAM.COD_ORGANIZACION " +
                       " AND E_tCA." + tca_pro +" = EXPRESION_CAMPO_NUM_TRAM.COD_PROCEDIMIENTO "  +
                       " AND E_tCA." + tca_cod +" = EXPRESION_CAMPO_NUM_TRAM.COD_CAMPO " + 
                       " AND E_TCA." + tca_tra +" = EXPRESION_CAMPO_NUM_TRAM.COD_TRAMITE";
            join5[10] = "LEFT";
            join5[11] = "EXPRESION_CAMPO_CAL_TRAM";
            join5[12] = "E_tCA." + tca_mun + " = EXPRESION_CAMPO_CAL_TRAM.COD_ORGANIZACION " +
                       " AND E_tCA." + tca_pro +" = EXPRESION_CAMPO_CAL_TRAM.COD_PROCEDIMIENTO "  +
                       " AND E_tCA." + tca_cod +" = EXPRESION_CAMPO_CAL_TRAM.COD_CAMPO "+
                       " AND E_TCA." + tca_tra +" = EXPRESION_CAMPO_CAL_TRAM.COD_TRAMITE";
            join5[13] = "false";            


            sql = oad.join(from,where,join5);
            String parametros1[] = {"8","8"};
            sql += oad.orderUnion(parametros1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar4 = "no";
            while ( rs.next() ) {
                entrar4 = "si";
                DefinicionCampoValueObject dCVO = new DefinicionCampoValueObject();
                String codCampo = rs.getString(tca_cod);
                dCVO.setCodCampo(codCampo);
                String descCampo = rs.getString(tca_des);
                dCVO.setDescCampo(descCampo);
                String codTipoDato = rs.getString(tca_tda);
                dCVO.setCodTipoDato(codTipoDato);
                String codCampoDesplegable = "";
                if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                    codCampoDesplegable = rs.getString(tca_desplegable);
                    dCVO.setCodPlantilla(codCampoDesplegable);
                }else if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))){ //desplegables externos
                    codCampoDesplegable = rs.getString(tca_desplegable);
                    dCVO.setCodPlantilla(codCampoDesplegable);
                } else {
                    String codPlantilla = rs.getString(tca_plt);
                    dCVO.setCodPlantilla(codPlantilla);
                }
                String tamano = rs.getString(tca_tam);
                dCVO.setTamano(tamano);
                String mascara = rs.getString(tca_mas);
                dCVO.setDescMascara(mascara);
                String obligatorio = rs.getString(tca_obl);
                dCVO.setObligat(obligatorio);
                String numeroOrden = rs.getString(tca_nor);
                dCVO.setOrden(numeroOrden);
                String descPlantilla = rs.getString(plt_des);
                dCVO.setDescPlantilla(descPlantilla);
                String descTipoDato = rs.getString(tda_des);
                dCVO.setDescTipoDato(descTipoDato);
                String rotulo = rs.getString(tca_rot);
                dCVO.setRotulo(rotulo);
                String visible = rs.getString(tca_vis);
                dCVO.setVisible(visible);
                String activo = rs.getString(tca_activo);
                dCVO.setActivo(activo);
                String oculto = rs.getString("TCA_OCULTO");
                dCVO.setOculto(oculto);
                String bloqueado = rs.getString("TCA_BLOQ");
                dCVO.setBloqueado(bloqueado);
                String plazoFecha = rs.getString("PLAZO_AVISO");
                dCVO.setPlazoFecha(plazoFecha);
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                dCVO.setCheckPlazoFecha(checkPlazoFecha);
                String validacion = rs.getString("EXPRESION");
                dCVO.setValidacion(validacion);
                String operacion = rs.getString("EXPRESION_CAL");
                dCVO.setOperacion(operacion);
                String codtra = rs.getString(tca_tra);
                dCVO.setCodTramite(codtra);
                String codAgrupacion = rs.getString("PCA_GROUP");
                if(codAgrupacion != null){
                    dCVO.setCodAgrupacion(codAgrupacion);
                }else{
                    dCVO.setCodAgrupacion("DEF");
                }//if(codAgrupacion != null)
                String posX = rs.getString("TCA_POS_X");
                dCVO.setPosX(posX);
                String posY = rs.getString("TCA_POS_Y");
                dCVO.setPosY(posY);
                listasCampos.addElement(dCVO); 
                defTramVO.setListaCampos(listasCampos);
            }
            if(entrar4.equals("no")) {
                defTramVO.setListaCampos(listasCampos);
            }

            rs.close();
            st.close();
        }catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionTramitesDAO.getTramite"), e);
        }finally {
            try{
                if(rs != null) rs.close();
                if(rs2 != null) rs2.close();
                if(st != null) st.close();
                if(st2 != null) st2.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getTramite");
        return defTramVO;
    }
    public int insert(DefinicionTramitesValueObject defTramVO,Connection conexion, String[] params) throws TechnicalException,BDException,AnotacionRegistroException{
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
        String yaExiste = "no";
        Vector codTramiteV = new Vector();
        AdaptadorSQLBD abd = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("Entra	en el	insert del DAO");

        try{

            if(m_Log.isDebugEnabled()) m_Log.debug("A por el OAD");
            abd =	new AdaptadorSQLBD(params);

            // PESTAÑA DE DATOS

            sql1 = "SELECT " + tra_cod + " FROM	E_TRA	WHERE	" + tra_mun	+ "="	+ defTramVO.getCodMunicipio()	+
                    " AND " + tra_pro + "='" + defTramVO.getTxtCodigo() + "'";
            String parametros[] = {"1","1"};
            sql1 += abd.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql1);

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
            defTramVO.setCodigoInternoTramite(codTramite);

            sql =	"INSERT INTO E_TRA (" +	tra_mun + "," + tra_pro	+ ","	+ tra_cou +	"," +	tra_cod + "," +
                    tra_vis + "," + tra_uin	+ ","	+ tra_utr +	"," +	tra_plz + "," + tra_und	+ ","	+ tra_ins + "," +
                    tra_ocu + "," + tra_cls	+ ","	+ tra_pre +	","+ tra_uti +	","+ tra_utf +	","+ tra_usi +	","+
                    tra_usf +	","+ tra_ini +	","+ tra_inf + "," + tra_prr + "," + tra_car +
                    //",TRA_FIN, TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP,TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA) VALUES(" + defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() + "'," + defTramVO.getNumeroTramite() +
                    ",TRA_FIN, TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP,TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,"
                    + "TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN, TRA_NOTIF_ELECT_OBLIG, "
                    + "TRA_NOTIF_FIRMA_CERT_ORG, TRA_NOTIF_UITI, TRA_NOTIF_UITF, TRA_NOTIF_UIEI, TRA_NOTIF_UIEF, TRA_NOTIFICADO, TRA_NOTIF_USUTRA_FINPLAZO, TRA_NOTIF_USUEXP_FINPLAZO, TRA_NOTIF_UOR_FINPLAZO ) "
                    + "VALUES(" + defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() + "'," + defTramVO.getNumeroTramite() +
                    "," +	codTramite + "," + defTramVO.getDisponible();
            if(defTramVO.getCodUnidadInicio().equals("") ||	defTramVO.getCodUnidadInicio() == null)
                sql	+= ",null";
            else
                sql	+= "," + defTramVO.getCodUnidadInicio();
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

            if(defTramVO.getCodExpRel() ==	null || defTramVO.getCodExpRel().equals(""))
                sql	+= ",null";
            else
                sql	+= ",'" + defTramVO.getCodExpRel() + "'";
            if(defTramVO.getCodCargo().equals("") || defTramVO.getCodCargo() ==	null)
                sql	+= ",null";
            else
                sql	+= "," + defTramVO.getCodCargo();
            
             sql	+= "," + defTramVO.getPlazoFin();
             sql +=",?,?,?,?,?";

               if(defTramVO.getAdmiteNotificacionElectronica().equals("") ||defTramVO.getAdmiteNotificacionElectronica() == null)
                sql	+= ",null";
                else
                sql	+= ",'" + defTramVO.getAdmiteNotificacionElectronica() + "'";

                if(defTramVO.getCodigoTipoNotificacionElectronica().equals("") ||	defTramVO.getCodigoTipoNotificacionElectronica() == null)
                sql	+= ",null";
                else
                sql	+= ",'" + defTramVO.getCodigoTipoNotificacionElectronica() + "'";

                if(defTramVO.getTipoUsuarioFirma().equals("") ||	defTramVO.getTipoUsuarioFirma() == null)
                sql	+= ",null";
                else
                sql	+= ",'" + defTramVO.getTipoUsuarioFirma() + "'";

                if(defTramVO.getCodigoOtroUsuarioFirma().equals("") ||	defTramVO.getCodigoOtroUsuarioFirma() == null)
                sql	+= ",null";
                else
                sql	+= ",'" + defTramVO.getCodigoOtroUsuarioFirma() + "'";

                if(defTramVO.getCodPluginPantallaTramitacionExterna().equals("") ||	defTramVO.getCodPluginPantallaTramitacionExterna() == null)
                    sql	+= ",null";
                else
                    sql	+= ",'" + defTramVO.getCodPluginPantallaTramitacionExterna() + "'";

                if(defTramVO.getUrlPluginPantallaTramitacionExterna().equals("") ||	defTramVO.getUrlPluginPantallaTramitacionExterna() == null)
                    sql	+= ",null";
                else
                    sql	+= ",'" + defTramVO.getUrlPluginPantallaTramitacionExterna() + "'";

                if(defTramVO.getImplClassPluginPantallaTramitacionExterna().equals("") ||	defTramVO.getImplClassPluginPantallaTramitacionExterna() == null)
                    sql	+= ",null";
                else
                    sql	+= ",'" + defTramVO.getImplClassPluginPantallaTramitacionExterna() + "'";

                if(defTramVO.getCodUrlPluginPantallaTramitacionExterna().equals("") ||	defTramVO.getCodUrlPluginPantallaTramitacionExterna() == null)
                    sql	+= ",null";
                else
                    sql	+= ",'" + defTramVO.getCodUrlPluginPantallaTramitacionExterna() + "'";
                
            
                 
                if (defTramVO.getNotificacionElectronicaObligatoria()){
                    sql += ",1";
                }else{
                    sql += ",0";
                }
                   
                if (defTramVO.getCertificadoOrganismoFirmaNotificacion()){
                    sql += ",1";
                }else{
                    sql += ",0";
                }

            sql += ",'" + defTramVO.getNotUsuInicioTramiteIni() + "','" + defTramVO.getNotUsuInicioTramiteFin()+"','"+defTramVO.getNotUsuInicioExpedIni()+"','"+defTramVO.getNotUsuInicioExpedFin()+"'";
            sql += ",?,'" + defTramVO.getNotUsuTraFinPlazo() +"', '" + defTramVO.getNotUsuExpFinPlazo() + "', '" + defTramVO.getNotUORFinPlazo()+"'";
            sql += ")";
           

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement	ps = conexion.prepareStatement(sql);

             int i = 1;
              ps.setBoolean(i++, defTramVO.isGenerarPlazos());
             ps.setBoolean(i++, defTramVO.getNotificarCercaFinPlazo());
             ps.setBoolean(i++, defTramVO.getNotificarFueraDePlazo());
             ps.setInt(i++, defTramVO.getTipoNotCercaFinPlazo());
             ps.setInt(i++, defTramVO.getTipoNotFueraDePlazo());
             ps.setInt(i++, (defTramVO.isTramiteNotificado()?1:0));

            res = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el insert E_TRA son ::::::::::::::	: " +	res);

            sql =	"INSERT INTO E_TML VALUES(" +	defTramVO.getCodMunicipio() +
                    ",'" + defTramVO.getTxtCodigo() + "'," + codTramite +	",'NOM','"+
                    m_ConfigTechnical.getString("idiomaDefecto")+"','" +
                    defTramVO.getNombreTramite() + "')";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el insert E_TML son ::::::::::::::	: " +	res1);

            if(defTramVO.getTramiteInicio().equals("1")) {
                sql	= "UPDATE E_PRO SET " +	pro_tri + "=" + codTramite + " WHERE " +	pro_mun + "=" +
                        defTramVO.getCodMunicipio()	+ " AND " +	pro_cod + "='" + defTramVO.getTxtCodigo()	+ "'";

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res3 = ps.executeUpdate();
                ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas	en el	update de E_PRO son :::::::::::::: : " + res3);
            }

            if(res !=0)	{

                // PESTAÑA DE DOCUMENTOS

                Vector listaCodigosDoc = defTramVO.getListaCodigosDoc();
                Vector listaNombresDoc = defTramVO.getListaNombresDoc();
                Vector listaVisibleDoc = defTramVO.getListaVisibleDoc();
                Vector listaPlantillaDoc = defTramVO.getListaPlantillaDoc();
                Vector listaCodPlantilla = defTramVO.getListaCodPlantilla();
                Vector listaDocActivos = defTramVO.getListaDocActivos();
                if(m_Log.isDebugEnabled()) m_Log.debug("el tamaío	de las listas de documentos es : " +
                        listaCodigosDoc.size()	+ "|"	+ listaNombresDoc.size() +
                        "|" + listaVisibleDoc.size()	+ "|"	+ listaPlantillaDoc.size() );
                for(int l=0;l<listaCodigosDoc.size();l++){
                    sql	= "INSERT INTO E_DOT ("	+ dot_mun +	"," +	dot_pro + "," + dot_tra	+ ","	+ dot_cod +
                            ","	+ dot_vis +	"," +	dot_plt + "," + dot_activo + ") VALUES(" +	defTramVO.getCodMunicipio() +	",'" +
                            defTramVO.getTxtCodigo() + "'," + codTramite + "," +
                            listaCodigosDoc.elementAt(l) + ",'"	+ listaVisibleDoc.elementAt(l) + "',";
                    if(listaCodPlantilla.elementAt(l)	== null || "".equals(listaCodPlantilla.elementAt(l)))	{
                        sql += "null,";
                    } else {
                        sql += "'" + listaCodPlantilla.elementAt(l)	+ "',";
                    }
                    sql += listaDocActivos.elementAt(l) + ")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps = conexion.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                }

                // PESTAÑA DE CONDICIONES DE ENTRADA

                Vector listaTramitesTablaEntrada = defTramVO.getListaTramitesTabla();
                Vector listaCodTramitesTablaEntrada =	defTramVO.getListaCodTramitesTabla();
                Vector listaEstadoTablaEntrada = defTramVO.getListaEstadosTabla();
                Vector listaTipoTablaEntrada = defTramVO.getListaTiposTabla();
                Vector listaExpresionesTablaEntrada = defTramVO.getListaExpresionesTabla();
                for(int	j=0;j<listaTramitesTablaEntrada.size();j++){
                    if(listaTipoTablaEntrada.elementAt(j).equals("TRÁMITE")) {
                    sql =	"INSERT INTO E_ENT (" +	ent_mun + "," + ent_pro	+ ","	+ ent_tra +	"," +	ent_cod +
                    "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + ") VALUES("	+ defTramVO.getCodMunicipio() + ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
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
                    } else if(listaTipoTablaEntrada.elementAt(j).equals("EXPRESION")) {
                    sql = "INSERT	INTO E_ENT (" + ent_mun	+ ","	+ ent_pro +	"," +	ent_tra + "," + ent_cod	+
                        "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + "," + ent_exp + ") VALUES("+ defTramVO.getCodMunicipio()	+ ",'" +
                    defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," + (j+1);
                    String aux =(String) listaExpresionesTablaEntrada.elementAt(j);
                    aux = aux.replaceAll("&lt;","<");
                    aux = aux.replaceAll("&gt;",">");
                    aux = aux.replaceAll("&#39;","'");

                    sql += ", 0 , ' ' ,'E'," + abd.addString(aux) + ")";
                    } else {
                        sql = "INSERT	INTO E_ENT (" + ent_mun	+ ","	+ ent_pro +	"," +	ent_tra + "," + ent_cod	+
                                "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + ",ENT_DOC) VALUES("	+ defTramVO.getCodMunicipio()	+ ",'" +
                                defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
                                (j+1) + ", 0 ";
                        if(listaEstadoTablaEntrada.elementAt(j).equals("PENDIENTE")) {
                            sql += ",'P'";
                        } else if(listaEstadoTablaEntrada.elementAt(j).equals("FIRMADO")) {
                            sql += ",'F'";
                        } else if(listaEstadoTablaEntrada.elementAt(j).equals("RECHAZADO")) {
                            sql += ",'R'";
                        } else sql += ",'C'";
                        sql += ",'D',"+listaTramitesTablaEntrada.elementAt(j)+")";
                    }

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                    ps = conexion.prepareStatement(sql);
                    resCondEnt = ps.executeUpdate();
                    ps.close();
                }
                if(listaTramitesTablaEntrada.size() == 0 ) {
                    resCondEnt = 1;
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_ENT son :::::::::::::: : "	+ resCondEnt);

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
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_TEN son :::::::::::::: : "	+ resEnl);

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
                            codTramite	+ ",'TXT','"+idiomaDefecto+"','" + defTramVO.getTexto() +	"')";
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

                if(m_Log.isDebugEnabled()) m_Log.debug(sqlCond);
                ps = conexion.prepareStatement(sqlCond);
                res2 = ps.executeUpdate();
                ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el INSERT de	E_SAL	son :::::::::::::: : " + res2);

                if(!sqlCondSML.equals("")) {
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlCondSML);
                    ps = conexion.prepareStatement(sqlCondSML);
                    resSML = ps.executeUpdate();
                    ps.close();
                    if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el INSERT de E_SML son	:::::::::::::: : " + resSML);
                }

                // PESTAÑA DE CAMPOS
                
                 //Agrupaciones de campos
                Vector listaCodsAgrupacion = defTramVO.getListaCodAgrupacion();
                Vector listaDescsAgrupacion = defTramVO.getListaDescAgrupacion();
                Vector listaOrdenAgrupacion = defTramVO.getListaOrdenAgrupacion();
                Vector listaAgrupacionesActivas = defTramVO.getListaAgrupacionActiva();
                
                for(int j=0;j<listaCodsAgrupacion.size();j++) {
                    String sqlInsertar = "Insert into E_TCA_GROUP (TCA_ID_GROUP, TCA_DESC_GROUP, TCA_ORDER_GROUP, TCA_PRO, TCA_ACTIVE, TCA_TRA) "
                            + " values ('" + listaCodsAgrupacion.get(j) + "',"
                            + " '" + listaDescsAgrupacion.get(j) + "',"
                            + " " + listaOrdenAgrupacion.get(j) + ", "
                            + " '" + defTramVO.getTxtCodigo() + "', "
                            + " '" + listaAgrupacionesActivas.get(j) + "',"
                            + defTramVO.getCodigoTramite() + ")";

                    ps = conexion.prepareStatement(sqlInsertar);
                    ps.executeUpdate();
                }//for(int j=0;j<listaCodsAgrupacion.size();j++) 

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
                Vector listaOcultos = defTramVO.getListaOcultos();
                Vector listaBloqueados = defTramVO.getListaBloqueados();
                //PLAZO_AVISO, PERIODO_PLAZO
                Vector listaPlazoFecha = defTramVO.getListaPlazoFecha();
                Vector listaCheckPlazoFecha = defTramVO.getListaCheckPlazoFecha();
                Vector listaValidacion = defTramVO.getListaValidacion();
                Vector listaOperacion = defTramVO.getListaOperacion();
                Vector listaCodAgrupacionCampo = defTramVO.getListaCodAgrupacionCampo();
                Vector listaPosX = defTramVO.getListaPosX();
                Vector listaPosY = defTramVO.getListaPosY();
                String valida = "";                
                
                for(int j=0;j<listaCodCampos.size();j++){
                    if ("SI".equalsIgnoreCase((String)listaActivo.elementAt(j))) {
                        String plantilla="", desplegable="";
                        if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                            plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                            desplegable=(String)listaCodPlantill.elementAt(j);
                        }else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))){ //desplegables externos
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                            desplegable = (String)listaCodPlantill.elementAt(j);    
                        } else {
                            plantilla=(String)listaCodPlantill.elementAt(j);
                            desplegable="";
                        }
                        sql = "INSERT INTO E_TCA (" + tca_mun + "," + tca_pro + "," + tca_tra + ","+ tca_cod + "," +
                                tca_des + "," + tca_plt + "," + tca_tda + "," + tca_tam + "," + tca_mas + "," + tca_obl +
                        "," + tca_nor + "," + tca_rot + "," + tca_vis + "," + tca_activo + "," + tca_desplegable +",TCA_OCULTO,TCA_BLOQ,"
                            + "PLAZO_AVISO, PERIODO_PLAZO,PCA_GROUP,TCA_POS_X,TCA_POS_Y"+
                        ") VALUES (" + defTramVO.getCodMunicipio() + ",'" + defTramVO.getTxtCodigo() + "',"+ codTramite +
                        ",'" + listaCodCampos.elementAt(j) + "','" + listaDescCampos.elementAt(j) + "'," +
                        plantilla + ",'" + listaCodTipoDato.elementAt(j) + "'," + listaTamano.elementAt(j);
                        if(listaMascara.elementAt(j) == null || "".equals(listaMascara.elementAt(j)) || " ".equals(listaMascara.elementAt(j))) {
                            sql += ",null,";
                        } else {
                            sql += ",'" +  listaMascara.elementAt(j) + "',";
                        }
                        sql += listaObligatorio.elementAt(j) + "," + listaOrden.elementAt(j) + ",'" +
                        listaRotulo.elementAt(j) + "','"+ listaVisible.elementAt(j) + "','" +
                        listaActivo.elementAt(j)+ "','" + desplegable+ "','" + listaOcultos.elementAt(j) +"','" + listaBloqueados.elementAt(j) + "',";
						//insertamos el campo PLAZO_AVISO Y pERIODO_PLAZO
                        if(listaPlazoFecha.elementAt(j) == null || "".equals(listaPlazoFecha.elementAt(j)) || " ".equals(listaPlazoFecha.elementAt(j))) {
                        //if ((!listaCodTipoDato.elementAt(j).equals("3")) || (!"3".equals(listaCodTipoDato.elementAt(j)))){    
                            sql += "null, null";
                        } else {
                            sql += "'" +  listaPlazoFecha.elementAt(j) + "','" + listaCheckPlazoFecha.elementAt(j) +"'";
                        }
                        
                        if(listaCodAgrupacionCampo.elementAt(j) == null || "".equals(listaCodAgrupacionCampo.elementAt(j)) || 
                                " ".equals(listaCodAgrupacionCampo.elementAt(j))) {
                            sql += ",null";
                        }else{
                            sql +=",'" + listaCodAgrupacionCampo.elementAt(j) + "'";
                        }
                        
                        if(listaPosX.elementAt(j) == null 
                            || "".equals(listaPosX.elementAt(j))
                            || " ".equals(listaPosX.elementAt(j))
                            || "undefined".equals(listaPosX.elementAt(j))){
                            sql +=",null";
                        }else{
                                sql +="," + listaPosX.elementAt(j);
                        }

                        if(listaPosY.elementAt(j) == null 
                                || "".equals(listaPosY.elementAt(j))
                                || " ".equals(listaPosY.elementAt(j))
                                || "undefined".equals(listaPosY.elementAt(j))){
                                sql +=",null)";
                        }else{
                                sql +="," + listaPosY.elementAt(j) + ")";
                        }
                        
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();
                        
                        String tip_dat = (String) listaCodTipoDato.elementAt(j);
                        
                        valida = listaValidacion.elementAt(j).toString();
                        if (tip_dat.trim().equals("1") && !"".equals(valida))
                        {   
                            //valida = listaValidacion.elementAt(contador_expresion).toString();
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
                                m_Log.debug("grabacion de nueva expresion de calculo = " +listaOperacion.elementAt(j));  
                            }
                        }   
                    }
                }

            } else {
                if(m_Log.isDebugEnabled()) m_Log.debug("entra	en yaExise igual a si");
                resTotal = -1;
            }
            if(res != 0	&& res2 !=0	&& resCondEnt !=0	&& resEnl !=0){
                resTotal = 1;
            } else {
                if(m_Log.isErrorEnabled()){
                    m_Log.error("INSERT NO HECHO EN " + getClass().getName());
                    m_Log.debug("res VALE: " + res + " res2 VALE: " + res2 + " resCondEnt VALE: " + resCondEnt  +
                            " resEnl VALE: " + resEnl);
                }
            }

        }catch(Exception ex)	{
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        }finally {
            try {
                if (rs!=null) rs.close();
                if (st!=null) st.close();
            } catch(Exception ex) {
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("el entero que devuelve el insert es	: " +	resTotal);
        return resTotal;
    }

    public int modify(DefinicionTramitesValueObject defTramVO,Connection conexion, String[] params) throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        String sqlCond = "";
        String sqlCondSML =	"";
        String sqlBorrar = "";
        PreparedStatement ps = null;
        int res	= 0;
        int res1 = 0;
        int res2 = 0;
        int res3 = 0;
        int res4 = 0;
        int res5 = 0;
        int resDelete	= 0;
        int resTotal = 0;
        int resBorrar	=0;
        int resCondEnt = 0;
        int resEnl = 0;
        int resBorrarDoc = 0;
        int resBorrarPlt = 0;
        int resSML = 0;
        AdaptadorSQLBD abd = null;
        ResultSet rs = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("Entra	en el	modify del DAO");

        try{
            abd =	new AdaptadorSQLBD(params);

            if(m_Log.isDebugEnabled()) m_Log.debug("entra en el modify");

            // PESTAÑA DE DATOS

            sql =	"UPDATE E_TRA SET	" + tra_cou	+ "="	+ defTramVO.getNumeroTramite() + "," +
                    tra_vis + "=" + defTramVO.getDisponible();
            if(defTramVO.getCodUnidadInicio().equals("") ||	defTramVO.getCodUnidadInicio() == null)
                sql	+= "," + tra_uin + "=null";
            else
                sql	+= "," + tra_uin + "=" + defTramVO.getCodUnidadInicio();
            if(defTramVO.getCodUnidadTramite().equals("") || defTramVO.getCodUnidadTramite() ==	null)
                sql	+= "," + tra_utr + "=null";
            else
                sql	+= "," + tra_utr + "=" + defTramVO.getCodUnidadTramite();
            if(defTramVO.getPlazo().equals("") || defTramVO.getPlazo() == null)
                sql	+= "," + tra_plz + "=null" + "," + tra_und + "=null";
            else
                sql	+= "," + tra_plz + "=" + defTramVO.getPlazo() +
                        "," + tra_und +  "='" + defTramVO.getUnidadesPlazo() + "'";
            if(defTramVO.getOcurrencias().equals("") || defTramVO.getOcurrencias()==null)
                sql	+= "," + tra_ocu + "=null";
            else
                sql	+= "," + tra_ocu + "=" + defTramVO.getOcurrencias();
            if (defTramVO.getInstrucciones()!= null){
                sql += "," + tra_ins + "='" + defTramVO.getInstrucciones()+"' ";
                defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(defTramVO.getInstrucciones()));
            }
            if(defTramVO.getCodExpRel().equals("") ||	defTramVO.getCodExpRel() == null)
                sql	+= "," + tra_prr + "=null";
            else
                sql	+= "," + tra_prr + "='" + defTramVO.getCodExpRel() + "'";
            if(defTramVO.getCodCargo().equals("") ||	defTramVO.getCodCargo() == null)
                sql	+= "," + tra_car + "=null";
            else
                sql	+= "," + tra_car + "='" + defTramVO.getCodCargo() + "'";
            sql += "," + tra_cls + "=" + defTramVO.getCodClasifTramite() + "," +
                    tra_pre +  "=" + defTramVO.getTramitePregunta();

            // NOTIFICACIONES
            sql += "," + tra_uti + "='" + defTramVO.getNotUnidadTramitIni() + "'";
            sql += "," + tra_utf + "='" + defTramVO.getNotUnidadTramitFin() + "'";
            sql += "," + tra_usi + "='" + defTramVO.getNotUsuUnidadTramitIni() + "'";
            sql += "," + tra_usf + "='" + defTramVO.getNotUsuUnidadTramitFin() + "'";
            sql += "," + tra_ini + "='" + defTramVO.getNotInteresadosIni() + "'";
            sql += "," + tra_inf + "='" + defTramVO.getNotInteresadosFin() + "'";
            sql += ", TRA_NOTIF_UITI ='" + defTramVO.getNotUsuInicioTramiteIni() +"'";
            sql += ", TRA_NOTIF_UITF ='" + defTramVO.getNotUsuInicioTramiteFin() +"'";
            sql += ", TRA_NOTIF_UIEI ='" + defTramVO.getNotUsuInicioExpedIni() +"'";
            sql += ", TRA_NOTIF_UIEF ='" + defTramVO.getNotUsuInicioExpedFin()+"'";
             sql += ", TRA_NOTIF_USUTRA_FINPLAZO = '" + defTramVO.getNotUsuTraFinPlazo()+"'";	
            sql += ", TRA_NOTIF_USUEXP_FINPLAZO = '" + defTramVO.getNotUsuExpFinPlazo()+"'";	
            sql += ", TRA_NOTIF_UOR_FINPLAZO = '" + defTramVO.getNotUORFinPlazo()+"'";
            sql += ", TRA_FIN =" + defTramVO.getPlazoFin();
            sql += ", TRA_NOTCERCAFP =?";
            sql += ", TRA_NOTFUERADP =?";
            sql += ", TRA_TIPNOTCFP =?";
            sql += ", TRA_TIPNOTFDP =?";
             sql += ", TRA_GENERARPLZ = ?";

             if(defTramVO.getAdmiteNotificacionElectronica().equals("") ||defTramVO.getAdmiteNotificacionElectronica() == null)
                sql	+= ",TRA_NOTIFICACION_ELECTRONICA=null";
            else
                sql	+= ",TRA_NOTIFICACION_ELECTRONICA='" + defTramVO.getAdmiteNotificacionElectronica() + "'";

             if(defTramVO.getCodigoTipoNotificacionElectronica().equals("") ||	defTramVO.getCodigoTipoNotificacionElectronica() == null)
                sql	+= ",TRA_COD_TIPO_NOTIFICACION=null";
            else
                sql	+= ",TRA_COD_TIPO_NOTIFICACION='" + defTramVO.getCodigoTipoNotificacionElectronica() + "'";

             if(defTramVO.getTipoUsuarioFirma().equals("") ||	defTramVO.getTipoUsuarioFirma() == null)
                sql	+= ",TRA_TIPO_USUARIO_FIRMA=null";
            else
                sql	+= ",TRA_TIPO_USUARIO_FIRMA='" + defTramVO.getTipoUsuarioFirma() + "'";

             if(defTramVO.getCodigoOtroUsuarioFirma().equals("") ||	defTramVO.getCodigoOtroUsuarioFirma() == null)
                sql	+= ",TRA_OTRO_COD_USUARIO_FIRMA=null";
            else
                sql	+= ",TRA_OTRO_COD_USUARIO_FIRMA='" + defTramVO.getCodigoOtroUsuarioFirma() + "'";

            if(defTramVO.getCodPluginPantallaTramitacionExterna()!=null && !"".equals(defTramVO.getCodPluginPantallaTramitacionExterna())){
                sql += ",TRA_TRAM_EXT_PLUGIN='" + defTramVO.getCodPluginPantallaTramitacionExterna() + "'";
            }else
                sql += ",TRA_TRAM_EXT_PLUGIN=null";

            if(defTramVO.getUrlPluginPantallaTramitacionExterna()!=null && !"".equals(defTramVO.getUrlPluginPantallaTramitacionExterna())){
                sql += ",TRA_TRAM_EXT_URL='" + defTramVO.getUrlPluginPantallaTramitacionExterna() + "'";
            }else
                sql += ",TRA_TRAM_EXT_URL=null";

            if(defTramVO.getImplClassPluginPantallaTramitacionExterna()!=null && !"".equals(defTramVO.getImplClassPluginPantallaTramitacionExterna())){
                sql += ",TRA_TRAM_EXT_IMPLCLASS='" + defTramVO.getImplClassPluginPantallaTramitacionExterna() + "'";
            }else
                sql += ",TRA_TRAM_EXT_IMPLCLASS=null";


            if(defTramVO.getCodUrlPluginPantallaTramitacionExterna()!=null && !"".equals(defTramVO.getCodUrlPluginPantallaTramitacionExterna())){
                sql += ",TRA_TRAM_ID_ENLACE_PLUGIN='" + defTramVO.getCodUrlPluginPantallaTramitacionExterna() + "'";
            }else
                sql += ",TRA_TRAM_ID_ENLACE_PLUGIN=null";
            
            //Peticion #76248 Metemos el valor de los campos de notificacion obligatoria y de certificado de organismo
            sql += ", TRA_NOTIF_ELECT_OBLIG = ?";
            sql += ", TRA_NOTIF_FIRMA_CERT_ORG = ?";
            sql += ",COD_DEPTO_NOTIFICACION=?"; 
            sql += ",TRA_NOTIFICADO=?"; 

            // INFORMACIÓN SERVICIOS WEB
            sql += " WHERE " +	tra_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+
                    tra_pro + "='" + defTramVO.getTxtCodigo()	+ "' AND " + tra_cod + "=" + defTramVO.getCodigoTramite();

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);

            int i=1;
            ps.setBoolean(i++, defTramVO.getNotificarCercaFinPlazo());
            ps.setBoolean(i++,defTramVO.getNotificarFueraDePlazo());
            ps.setInt(i++, defTramVO.getTipoNotCercaFinPlazo());
            ps.setInt(i++, defTramVO.getTipoNotFueraDePlazo());
            ps.setBoolean(i++, defTramVO.isGenerarPlazos());
          
            //Peticion #76248 Metemos el valor de los campos de notificacion obligatoria y de certificado de organismo
            ps.setInt(i++, (defTramVO.getNotificacionElectronicaObligatoria())?1:0);
            ps.setInt(i++, (defTramVO.getCertificadoOrganismoFirmaNotificacion())?1:0);
            if(defTramVO.getCodDepartamentoNotificacion()==null)
                ps.setNull(i++,java.sql.Types.VARCHAR);
            else
                ps.setString(i++,defTramVO.getCodDepartamentoNotificacion());
            ps.setInt(i++, (defTramVO.isTramiteNotificado()?1:0));
            
            res = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el modify de E_TRA son	:::::::::::::: : " + res);

            sql =	"UPDATE E_TML SET	" + tml_valor + "='" + defTramVO.getNombreTramite() +	"'" +
                    " WHERE " +	tml_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+
                    tml_pro + "='" + defTramVO.getTxtCodigo()	+ "' AND " + tml_tra + "=" + defTramVO.getCodigoTramite() +
                    " AND	" + tml_cmp	+ "='NOM' AND " +	tml_leng + "='"+idiomaDefecto+"'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res3 = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el modify de E_TML son	:::::::::::::: : " + res3);

            sql =	"SELECT " +	pro_tri + "	FROM E_PRO WHERE " + pro_mun + "=" + defTramVO.getCodMunicipio() +
                    " AND	" + pro_cod	+ "='" + defTramVO.getTxtCodigo() +	"'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                res5 = ps.executeUpdate();
                ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas	en el	update de E_PRO son :::::::::::::: : " + res5);
            }

            if(codTramiteInicio != null) {
                if(defTramVO.getTramiteInicio().equals("0") && codTramiteInicio.equals(defTramVO.getCodigoTramite())) {
                    sql = "UPDATE	E_PRO	SET "	+ pro_tri +	"=null" + "	WHERE	" +  pro_mun + "=" +
                            defTramVO.getCodMunicipio() + "	AND "	+ pro_cod +	"='" + defTramVO.getTxtCodigo() + "'";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps = conexion.prepareStatement(sql);
                    res5 = ps.executeUpdate();
                    ps.close();
                    if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el update de	E_PRO	son :::::::::::::: : " + res5);
                }
            }

            if(res !=0)	{

                // PESTAÑA DE DOCUMENTOS
                Vector listaCodigosDoc = defTramVO.getListaCodigosDoc();
                Vector listaNombresDoc = defTramVO.getListaNombresDoc();
                Vector listaVisibleDoc = defTramVO.getListaVisibleDoc();
                Vector listaPlantillaDoc = defTramVO.getListaPlantillaDoc();
                Vector listaCodPlantilla = defTramVO.getListaCodPlantilla();
                Vector listaFirmaPlantilla = defTramVO.getListaFirmaDoc();
                Vector listaDocActivos = defTramVO.getListaDocActivos();

                /* Realizamos la comprobación de que campos pueden ser borrados y cuales no */
                HashMap insertar = new HashMap();
                boolean inserta = true;
                PreparedStatement statExiste = null;
                ResultSet rsExiste = null;
                for(int j=0;j<listaCodigosDoc.size();j++){
                    inserta = true;
                    /* Comprobamos si el codigo del documento existe en otras tablas */
                    if(m_Log.isDebugEnabled()) m_Log.debug("CAMPO ACTIVO = " + (String)listaDocActivos.elementAt(j));
                    if ("NO".equalsIgnoreCase((String)listaDocActivos.elementAt(j))) {
                        String sqlExiste = "SELECT COUNT (*)  FROM e_dot WHERE e_dot.dot_cod = '" + listaCodigosDoc.elementAt(j)  + "'";
                        sqlExiste += " AND e_dot.dot_pro = '" + defTramVO.getTxtCodigo() + "'";
                        sqlExiste += " AND e_dot.dot_tra = " + defTramVO.getCodigoTramite();
                        sqlExiste += " AND e_dot.dot_mun = '" + defTramVO.getCodMunicipio() + "'";
                        sqlExiste += " AND e_dot.dot_cod IN (SELECT crd_dot FROM e_crd WHERE crd_pro = dot_pro AND crd_tra = dot_tra AND crd_mun = dot_mun)";
                        if(m_Log.isDebugEnabled()) m_Log.debug("CONSULTA = " + sqlExiste);
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
                    if(m_Log.isDebugEnabled()) m_Log.debug("EL RESULTADO DE LA CONSULTA, se inserta documento ? = " + insertar);
                }
                /* Fin de la comprobación de los campos que pueden ser borrados y cuales no*/

                Boolean seInserta;
                
                if(m_Log.isDebugEnabled()) m_Log.debug("el tamaío de la lista de codigos de documento es : " + listaCodigosDoc.size());
                for(int l=0;l<listaCodigosDoc.size();l++) {
                    sqlBorrar = "DELETE FROM E_DOT WHERE dot_mun =" + defTramVO.getCodMunicipio() +	
                            " AND dot_pro = '" + defTramVO.getTxtCodigo() + 
                            "' AND dot_tra =" + defTramVO.getCodigoTramite() + " AND dot_cod =" + listaCodigosDoc.elementAt(l);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                    ps = conexion.prepareStatement(sqlBorrar);
                    resBorrarDoc = ps.executeUpdate();
                    ps.close();
                    if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas	en el eliminar de E_DOT	son :::::::::::::: : " + resBorrarDoc);

                    if(m_Log.isDebugEnabled()) m_Log.debug("en el	for");
                    seInserta = (Boolean)insertar.get((String)listaCodigosDoc.elementAt(l));
                    if (seInserta.booleanValue()) {
                        sql = "INSERT INTO E_DOT (dot_mun,dot_pro,dot_tra,dot_cod,dot_vis,dot_frm,dot_plt,dot_activo) "+
                                "VALUES(" + defTramVO.getCodMunicipio() +	",'" + defTramVO.getTxtCodigo() + "'," + 
                                defTramVO.getCodigoTramite() + "," + listaCodigosDoc.elementAt(l) + ",'" + 
                                listaVisibleDoc.elementAt(l) + "',";
                        if (listaFirmaPlantilla.elementAt(l)!=null) sql +="'"+listaFirmaPlantilla.elementAt(l)+"',";
                        else sql +="NULL,";
                        if(listaCodPlantilla.elementAt(l) == null || "".equals(listaCodPlantilla.elementAt(l))) {
                            sql += "null,";
                        }	else {
                            sql += listaCodPlantilla.elementAt(l) + ",";
                        }
                        sql += "'" + listaDocActivos.elementAt(l) + "')";
                        if(m_Log.isDebugEnabled()) m_Log.debug("el Insert en E_DOT es :	" + sql);
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();
                        ps.close();
                    } else { //los q solo se borran y no se vuelven a insertar en e_dot,se borran tambien de a_plt
                        sqlBorrar = "DELETE FROM A_PLT WHERE plt_cod = '" +	listaCodPlantilla.elementAt(l) + "'";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                        ps = conexion.prepareStatement(sqlBorrar);
                        resBorrarPlt = ps.executeUpdate();
                        ps.close();
                        if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas	en el eliminar de A_PLT	son :::::::::::::: : " + resBorrarPlt);
                    }
                } 

                // PESTAÑA DE CONDICIONES DE ENTRADA
                Vector listaTramitesTablaEntrada = defTramVO.getListaTramitesTabla();
                Vector listaCodTramitesTablaEntrada = defTramVO.getListaCodTramitesTabla();
                Vector listaEstadoTablaEntrada = defTramVO.getListaEstadosTabla();
                Vector listaTipoTablaEntrada = defTramVO.getListaTiposTabla();
                Vector listaExpresionesTablaEntrada = defTramVO.getListaExpresionesTabla();
                Vector listaDocumentosTablaEntrada = defTramVO.getListaCodigosDocTabla();
            
                 
                if(m_Log.isDebugEnabled()) m_Log.debug("LISTA TRAMITES TABLA ENTRADA     -> "+listaTramitesTablaEntrada);
                if(m_Log.isDebugEnabled()) m_Log.debug("LISTA COD TRAMITES TABLA ENTRADA -> "+listaCodTramitesTablaEntrada);
                if(m_Log.isDebugEnabled()) m_Log.debug("LISTA ESTADO   TABLA ENTRADA     -> "+listaEstadoTablaEntrada);
                if(m_Log.isDebugEnabled()) m_Log.debug("LISTA TIPO TABLA ENTRADA         -> "+listaTipoTablaEntrada);
                if(m_Log.isDebugEnabled()) m_Log.debug("LISTA EXPRESIONES TABLA ENTRADA         -> "+listaExpresionesTablaEntrada);
                if(m_Log.isDebugEnabled()) m_Log.debug("LISTA DOCS TABLA ENTRADA         -> "+listaDocumentosTablaEntrada);

                sqlBorrar	= "DELETE FROM E_ENT WHERE " + ent_mun + "=" + defTramVO.getCodMunicipio() + " AND " + ent_pro + "='"	+
                        defTramVO.getTxtCodigo() + "' AND " +	ent_tra + "=" + defTramVO.getCodigoTramite();
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);                 
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas	en el	eliminar de	E_ENT	son :::::::::::::: : " + resBorrar);

                for(int j=0;j<listaTramitesTablaEntrada.size();j++)	{
                    if(listaTipoTablaEntrada.elementAt(j).equals("TRÁMITE")) {
                        sql = "INSERT	INTO E_ENT (" + ent_mun	+ ","	+ ent_pro +	"," +	ent_tra + "," + ent_cod	+
                                "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + ") VALUES("	+ defTramVO.getCodMunicipio()	+ ",'" +
                                defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
                               (j+1) + "," + listaCodTramitesTablaEntrada.elementAt(j);
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
                    } else if(listaTipoTablaEntrada.elementAt(j).equals("EXPRESION")) {
                        sql = "INSERT	INTO E_ENT (" + ent_mun	+ ","	+ ent_pro +	"," +	ent_tra + "," + ent_cod	+
                                "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + "," + ent_exp + ") VALUES("	+ defTramVO.getCodMunicipio()	+ ",'" +
                                defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," + (j+1);
                        String aux =(String) listaExpresionesTablaEntrada.elementAt(j);
                        aux = aux.replaceAll("&lt;","<");
                        aux = aux.replaceAll("&gt;",">");
                        aux = aux.replaceAll("&#39;","'");

                        sql += ", 0 , ' ' ,'E'," + abd.addString(aux) + ")";
                    } else {
                        sql = "INSERT	INTO E_ENT (" + ent_mun	+ ","	+ ent_pro +	"," +	ent_tra + "," + ent_cod	+
                                "," + ent_ctr	+ ","	+ ent_est +	"," + ent_tipo + ",ENT_DOC) VALUES("	+ defTramVO.getCodMunicipio()	+ ",'" +
                                defTramVO.getTxtCodigo() + "'," +	defTramVO.getCodigoTramite() + "," +
                                (j+1) + ", 0 ";
                        if(listaEstadoTablaEntrada.elementAt(j).equals("PENDIENTE")) {
                            // DOCUMENTO PENDIENTE
                            sql += ",'P'";
                        } else if(listaEstadoTablaEntrada.elementAt(j).equals("FIRMADO")) {
                            // DOCUMENTO FINALIZADO
                            sql += ",'F'";
                        } else if(listaEstadoTablaEntrada.elementAt(j).equals("RECHAZADO")){
                            // DOCUMENTO RECHAZADO
                            sql += ",'R'";
                        } else if(listaEstadoTablaEntrada.elementAt(j).equals("SUBSANADO")){
                            // DOCUMENTO SUBSANADO
                            sql += ",'S'";
                        } else {  // CIRCUITO FINALIZADO
                            sql += ",'C'";
                        }
                        sql += ",'D',"+listaDocumentosTablaEntrada.elementAt(j)+")";
                    }

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);               

                    ps = conexion.prepareStatement(sql);
                    resCondEnt += ps.executeUpdate();
                    ps.close();
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_ENT son resCondEnt:::::::::::::: : "	+ resCondEnt);
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
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);
                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas	en el	eliminar de	E_TEN	son :::::::::::::: : " + resBorrar);

                for(int j=0;j<listaCodigoEnlaces.size();j++) {
                    sql =	"INSERT INTO E_TEN (" +	ten_mun + "," + ten_pro	+ ","	+ ten_tra +	"," +	ten_cod +
                            ","	+ ten_des +	"," +	ten_url + "," +	ten_est + ") VALUES(" +	defTramVO.getCodMunicipio() +	",'" +
                            defTramVO.getTxtCodigo() + "'," + defTramVO.getCodigoTramite() + "," +
                            listaCodigoEnlaces.elementAt(j) + ",'" + listaDescripcionEnlaces.elementAt(j) + "','" +
                            listaUrlEnlaces.elementAt(j) + "'," + listaEstadoEnlaces.elementAt(j)+")";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                    ps = conexion.prepareStatement(sql);
                    resEnl += ps.executeUpdate();
                    ps.close();
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de E_TEN son resEnl:::::::::::::: : "	+ resEnl);
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
                            sml_leng + "='"+idiomaDefecto+"'";
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

                if(m_Log.isDebugEnabled()) m_Log.debug(sqlCond);
                ps = conexion.prepareStatement(sqlCond);
                res2 = ps.executeUpdate();
                ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas	en el	UPDATE de E_SAL son :::::::::::::: : " + res2);
                if(res2 !=0) {
                    if(!"".equals(sqlCondSML)) {
                        if(m_Log.isDebugEnabled()) m_Log.debug(sqlCondSML);
                        ps = conexion.prepareStatement(sqlCondSML);
                        res4 = ps.executeUpdate();
                        ps.close();
                        if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el UPDATE de E_SML son	:::::::::::::: : " + res4);
                    }
                    if(res4	==0 && "Pregunta".equals(tipoCondicion) )	{
                        sqlCondSML = "INSERT INTO E_SML (" + sml_mun + "," + sml_pro + "," + sml_tra + "," +
                                sml_cmp + "," + sml_leng + "," + sml_valor + ") VALUES(" +
                                defTramVO.getCodMunicipio() + ",'"	+ defTramVO.getTxtCodigo() + "'," +
                                defTramVO.getCodigoTramite()	+ ",'TXT','"+idiomaDefecto+"','" + defTramVO.getTexto() +	"')";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sqlCondSML);
                        ps = conexion.prepareStatement(sqlCondSML);
                        res4 = ps.executeUpdate();
                        ps.close();
                        if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el INSERT de E_SML son	:::::::::::::: : " + res4);
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
                                defTramVO.getCodigoTramite()	+ ",'TXT','"+idiomaDefecto+"','" + defTramVO.getTexto() +	"')";
                    }

                    if("sinCondicion".equals(tipoCondicion)) {
                        sqlCond = "INSERT	INTO E_SAL (" + sal_mun	+ ","	+ sal_pro +	"," +	sal_tra + "," +
                                sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + ") VALUES( " + defTramVO.getCodMunicipio() + ",'"	+
                                defTramVO.getTxtCodigo() + "',"	+ defTramVO.getCodigoTramite() + "," +
                                "'','','')";
                    }

                    if(m_Log.isDebugEnabled()) m_Log.debug(sqlCond);
                    ps = conexion.prepareStatement(sqlCond);
                    res2 = ps.executeUpdate();
                    ps.close();
                    if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el INSERT de	E_SAL	son :::::::::::::: : " + res2);

                    if(!sqlCondSML.equals("")) {
                        if(m_Log.isDebugEnabled()) m_Log.debug(sqlCondSML);
                        ps = conexion.prepareStatement(sqlCondSML);
                        resSML = ps.executeUpdate();
                        ps.close();
                        if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el INSERT de E_SML son	:::::::::::::: : " + resSML);
                    }
                }

                // PESTAÑA DE CAMPOS
                
                //Agrupaciones de campos
                Vector listaCodsAgrupacion = defTramVO.getListaCodAgrupacion();
                Vector listaDescsAgrupacion = defTramVO.getListaDescAgrupacion();
                Vector listaOrdenAgrupacion = defTramVO.getListaOrdenAgrupacion();
                Vector listaAgrupacionesActivas = defTramVO.getListaAgrupacionActiva();
                
                for(int w=0; w<listaCodsAgrupacion.size(); w++){
                    Boolean existe = false;
                    //Comprobamos si existe ya una agrupacion con este codigo para el mismo procedimiento
                    sql = "Select TCA_ID_GROUP from E_TCA_GROUP "
                            + " where TCA_ID_GROUP = '" + listaCodsAgrupacion.elementAt(w) + "' "
                            + " and TCA_PRO = '" + defTramVO.getTxtCodigo() + "' "
                            + " and TCA_TRA = " + defTramVO.getCodigoTramite();
                    ps = conexion.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while(rs.next()){
                        existe = true;
                    }//while(rs.next())
                    if(existe){
                        String sqlUpdate = "Update E_TCA_GROUP set TCA_DESC_GROUP = '" + listaDescsAgrupacion.elementAt(w) + "',"
                            + " TCA_ORDER_GROUP = " + listaOrdenAgrupacion.elementAt(w) + ", "
                            + " TCA_ACTIVE = '" + listaAgrupacionesActivas.elementAt(w) + "' "
                            + " where TCA_ID_GROUP = '" + listaCodsAgrupacion.elementAt(w) + "'"
                            + " and TCA_PRO = '" + defTramVO.getTxtCodigo() + "'"
                            + " and TCA_TRA = " + defTramVO.getCodigoTramite();
                        if(m_Log.isDebugEnabled()) m_Log.debug(sqlUpdate);
                        ps = conexion.prepareStatement(sqlUpdate);
                        ps.executeUpdate();
                        ps.close();
                    }else{
                        String sqlInsertar = "Insert into E_TCA_GROUP (TCA_ID_GROUP, TCA_DESC_GROUP, TCA_ORDER_GROUP, TCA_PRO, TCA_ACTIVE, TCA_TRA) "
                            + " values ('" + listaCodsAgrupacion.get(w) + "',"
                            + " '" + listaDescsAgrupacion.get(w) + "',"
                            + " " + listaOrdenAgrupacion.get(w) + ", "
                            + " '" + defTramVO.getTxtCodigo() + "', "
                            + " '" + listaAgrupacionesActivas.get(w) + "',"
                            + defTramVO.getCodigoTramite() +")";
                        if(m_Log.isDebugEnabled()) m_Log.debug(sqlInsertar);
                        ps = conexion.prepareStatement(sqlInsertar);
                        ps.executeUpdate();
                        ps.close();
                    }//if(existe)
                }//for(int w=0; w<listaCodAgrupaciones.size(); w++)

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
                Vector listaOcultos = defTramVO.getListaOcultos();
                Vector listaBloqueados = defTramVO.getListaBloqueados();
                Vector listaPlazoFecha = defTramVO.getListaPlazoFecha();
                Vector listaCheckPlazoFecha = defTramVO.getListaCheckPlazoFecha();
                Vector listaValidacion = defTramVO.getListaValidacion(); 
                Vector listaOperacion = defTramVO.getListaOperacion();
                Vector listaCodAgrupacionCampo = defTramVO.getListaCodAgrupacionCampo();
                Vector listaPosX = defTramVO.getListaPosX();
                Vector listaPosY = defTramVO.getListaPosY();
                String valida= "";

                /* Realizamos la comprobación de que campos pueden ser borrados y cuales no */
                insertar = new HashMap();
                inserta = true;
                statExiste = null;
                rsExiste = null;                                                
                for(int j=0;j<listaCodCampos.size();j++) {
                    inserta = true;
                    /* Comprobamos si el codigo del campo existe en otras tablas */
                    if(m_Log.isDebugEnabled()) m_Log.debug("CAMPO ACTIVO = " + (String)listaActivo.elementAt(j));
                    if ("NO".equalsIgnoreCase((String)listaActivo.elementAt(j))) {
                        String sqlExiste = "SELECT COUNT (*)  FROM e_tca WHERE e_tca.tca_cod =?";
                        sqlExiste += " AND e_tca.tca_pro = ?";
                        sqlExiste += " AND e_tca.tca_tra = ?";
                        sqlExiste += " AND e_tca.tca_mun = ?";
                        sqlExiste += " AND (e_tca.tca_cod IN (SELECT tnut_cod FROM e_tnut WHERE TNUT_PRO=?) " +
                                "OR e_tca.tca_cod IN (SELECT tfet_cod FROM e_tfet WHERE TFET_PRO=?) OR " +
                                "e_tca.tca_cod IN (SELECT ttlt_cod FROM e_ttlt WHERE TTLT_PRO=?) OR e_tca.tca_cod " +
                                "IN (SELECT txtt_cod FROM e_txtt  WHERE TXTT_PRO=?) OR e_tca.tca_cod IN (SELECT " +
                                "TDET_COD FROM E_TDET WHERE TDET_PRO=?))";
                        
                        statExiste = conexion.prepareStatement(sqlExiste);
                        i=1;
                        statExiste.setString(i++,(String)listaCodCampos.elementAt(j));
                        statExiste.setString(i++,defTramVO.getTxtCodigo());
                        statExiste.setInt(i++,Integer.parseInt(defTramVO.getCodigoTramite()));
                        statExiste.setInt(i++,Integer.parseInt(defTramVO.getCodMunicipio()));
                        statExiste.setString(i++,defTramVO.getTxtCodigo());
                        statExiste.setString(i++,defTramVO.getTxtCodigo());
                        statExiste.setString(i++,defTramVO.getTxtCodigo());
                        statExiste.setString(i++,defTramVO.getTxtCodigo());
                        statExiste.setString(i++,defTramVO.getTxtCodigo());

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
                    if(m_Log.isDebugEnabled()) m_Log.debug("EL RESULTADO DE LA CONSULTA, se inserta campo ? = " + insertar);
                }
                /* Fin de la comprobación de los campos que pueden ser borrados y cuales no*/

                sqlBorrar = "DELETE FROM E_TCA WHERE " + tca_mun + "=" + defTramVO.getCodMunicipio() + " AND " +
                        tca_pro + "='" + defTramVO.getTxtCodigo() + "' AND " + tca_tra + "=" +
                        defTramVO.getCodigoTramite();
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlBorrar);

                ps = conexion.prepareStatement(sqlBorrar);
                resBorrar = ps.executeUpdate();
                if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el eliminar de E_TCA son :::::::::::::: : " + resBorrar);
                ps.close();                
                sqlBorrar =" DELETE FROM EXPRESION_CAMPO_NUM_TRAM WHERE COD_ORGANIZACION = " + defTramVO.getCodMunicipio() +
                           " AND COD_PROCEDIMIENTO = '" + defTramVO.getTxtCodigo() +"'" + 
                           " AND COD_TRAMITE = " + defTramVO.getCodigoTramite();
                ps = conexion.prepareStatement(sqlBorrar);
                ps.executeUpdate();
                ps.close();                
                
                 sqlBorrar =" DELETE FROM EXPRESION_CAMPO_CAL_TRAM WHERE COD_ORGANIZACION = " + defTramVO.getCodMunicipio() +
                           " AND COD_PROCEDIMIENTO = '" + defTramVO.getTxtCodigo() +"'" + 
                           " AND COD_TRAMITE = " + defTramVO.getCodigoTramite();
                ps = conexion.prepareStatement(sqlBorrar);
                ps.executeUpdate();
                ps.close();                
                
                seInserta = new Boolean(true);                                
                
                for(int j=0;j<listaCodCampos.size();j++) {
                    seInserta = (Boolean)insertar.get((String)listaCodCampos.elementAt(j));
                    if (seInserta.booleanValue()) {
                        String plantilla = "", desplegable = "";
                        if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                            plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                            desplegable=(String)listaCodPlantill.elementAt(j);
                        }
                        else if (listaCodTipoDato.elementAt(j).equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt")))
                        {
                            plantilla=m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                            desplegable=(String)listaCodPlantill.elementAt(j);
                        } else {
                            plantilla=(String)listaCodPlantill.elementAt(j);
                            desplegable="";
                        }
                        sql = "INSERT INTO E_TCA (" + tca_mun + "," + tca_pro + "," + tca_tra + ","+ tca_cod + "," +
                                tca_des + "," + tca_plt + "," + tca_tda + "," + tca_tam + "," + tca_mas + "," + tca_obl +
                        "," + tca_nor + "," + tca_rot + "," + tca_vis + "," + tca_activo + "," + tca_desplegable + ",TCA_OCULTO,TCA_BLOQ,"
                            + "PLAZO_AVISO, PERIODO_PLAZO, PCA_GROUP, TCA_POS_X, TCA_POS_Y) VALUES(" + defTramVO.getCodMunicipio() + ",'" +
                                defTramVO.getTxtCodigo() + "',"+ defTramVO.getCodigoTramite() + ",'" + listaCodCampos.elementAt(j) + "','" +
                        listaDescCampos.elementAt(j) + "'," + plantilla + ",'" + listaCodTipoDato.elementAt(j) + "'," + listaTamano.elementAt(j);
                        if(listaMascara.elementAt(j) == null || "".equals(listaMascara.elementAt(j)) || " ".equals(listaMascara.elementAt(j))) {
                            sql += ",null,";
                        } else {
                            sql += ",'" +  listaMascara.elementAt(j) + "',";
                        }
                        sql += listaObligatorio.elementAt(j) + "," + listaOrden.elementAt(j) + ",'" +
                        listaRotulo.elementAt(j) + "','"+ listaVisible.elementAt(j) + "','" +
                        listaActivo.elementAt(j)+ "','" + desplegable+ "','" +  listaOcultos.elementAt(j) +"','" +  listaBloqueados.elementAt(j) + "',";
	

                        //insertamos el campo PLAZO_AVISO Y pERIODO_PLAZO
                        if(listaPlazoFecha.elementAt(j) == null || "".equals(listaPlazoFecha.elementAt(j)) || " ".equals(listaPlazoFecha.elementAt(j))) {
                            sql += "null, null";
                        } else {
                            sql += "'" +  listaPlazoFecha.elementAt(j) + "','" + listaCheckPlazoFecha.elementAt(j) +"'";
                        }
                        
                        if(listaCodAgrupacionCampo.size() == 0 ||
                                listaCodAgrupacionCampo.elementAt(j) == null || "".equals(listaCodAgrupacionCampo.elementAt(j)) || 
                                " ".equals(listaCodAgrupacionCampo.elementAt(j))) {
                            sql += ",null";
                        }else{
                            sql +=",'" + listaCodAgrupacionCampo.elementAt(j) + "'";
                        }
                        
                        if(listaPosX.size() == 0
                            || listaPosX.elementAt(j) == null 
                            || "".equals(listaPosX.elementAt(j))
                            || " ".equals(listaPosX.elementAt(j))
                            || "undefined".equals(listaPosX.elementAt(j))){
                            sql +=",null";
                        }else{
                                sql +="," + listaPosX.elementAt(j);
                        }

                        if(listaPosY.size() == 0
                                || listaPosY.elementAt(j) == null 
                                || "".equals(listaPosY.elementAt(j))
                                || " ".equals(listaPosY.elementAt(j))
                                || "undefined".equals(listaPosY.elementAt(j))){
                                sql +=",null)";
                        }else{
                                sql +="," + listaPosY.elementAt(j) + ")";
                        }
                        
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();                                                                        
                        
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
                                m_Log.debug("grabacion de nueva expresion de calculo = " +listaOperacion.elementAt(j));  
                            }
                        }   
                    }
                }
            }

            if(res != 0	&& res2 !=0	&& resCondEnt !=0	&& resEnl !=0){
                resTotal = 1;
            } else {
                if(m_Log.isErrorEnabled()){
                    m_Log.error("MODIFICACION NO HECHA EN " + getClass().getName());
                    m_Log.debug("res VALE: " + res + " res2 VALE: " + res2 + " resCondEnt VALE: " + resCondEnt  +
                            " resEnl VALE: " + resEnl);
                }
            }

        } catch(Exception ex)	{
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        } finally {
            try{
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
            } catch(Exception ex)	{
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
            }
        }
        return resTotal;
    }

    public int eliminar(DefinicionTramitesValueObject defTramVO,String[]	params) throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        int res	= 0;
        int res1 = 0;

        AdaptadorSQLBD abd = null;
        Connection conexion	= null;
        String codMunicipio	= defTramVO.getCodMunicipio();
        String codProcedimiento =	defTramVO.getTxtCodigo();
        String codTramite =	defTramVO.getCodigoTramite();
        if(m_Log.isDebugEnabled()) m_Log.debug("Entra	en el	eliminar del DAO");

        try{

            if(m_Log.isDebugEnabled()) m_Log.debug("A por el OAD");
            abd =	new AdaptadorSQLBD(params);
            if(m_Log.isDebugEnabled()) m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql =	"UPDATE E_TRA SET	" + tra_fba	+ "=" + abd.convertir(abd.convertir(abd.funcionFecha(
                    AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                    AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " WHERE " +
                    tra_mun + "=" + codMunicipio + " AND " + tra_pro + "='" + codProcedimiento + "' AND	" +
                    tra_cod + "=" + codTramite;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement	ps = conexion.prepareStatement(sql);
            res =	ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el UPDATE de UPDATE de E_TRA son	:::::::::::::: : " + res);

            sql =	"UPDATE E_PRO SET	" + pro_tri	+ "=null" +	" WHERE " +	 pro_mun + "=" +
                    defTramVO.getCodMunicipio() +	" AND	" + pro_cod	+ "='" + defTramVO.getTxtCodigo() +	"' AND " +
                    pro_tri + "=" + codTramite;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el update de E_PRO son	:::::::::::::: : " + res1);

            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el UPDATE de UPDATE de E_TRA son	:::::::::::::: : " + res);

            sql =	"UPDATE E_TCA SET TCA_ACTIVO='NO' WHERE TCA_MUN=" +
                    defTramVO.getCodMunicipio() + " AND TCA_PRO='" + defTramVO.getTxtCodigo()
                    + "' AND TCA_TRA=" + codTramite;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res1 = ps.executeUpdate();
            ps.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("las	filas	afectadas en el update de E_PCA son	:::::::::::::: : " + res1);


            abd.finTransaccion(conexion);

        }catch (Exception ex)	{
            abd.rollBack(conexion);
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        }finally {
            try{
                abd.devolverConexion(conexion);
            } catch(Exception e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception: "	+ e.getMessage());
            }
        }
        return res;
    }


    public DefinicionTramitesValueObject getTramiteImportar(DefinicionTramitesValueObject defTramVO,Connection con,String[] params) throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando	este metod es ejecutado
        m_Log.debug("getTramiteImportar");
        if(m_Log.isDebugEnabled()) m_Log.debug("entra en getTramiteImportar en el DAO");

        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        String from =	"";
        String where = "";
        String codProcedemento = defTramVO.getTxtCodigo();
        String codMunicipio	= defTramVO.getCodMunicipio();
        Vector listaTramitesImportar = new Vector();
        Vector listaDocumentosImportar = new Vector();
        Vector listaCondEntradaImportar	= new	Vector();
        Vector listaCondSalidaImportar = new Vector();
        Vector listaPreguntaCondSalida = new Vector();
        Vector listaFlujoSalida =	new Vector();
        Vector listaTramitesNulos	= new	Vector();
        Vector listaEnlaces = new Vector();
        Vector listaCampos = new Vector();
        Vector listaPlantillas = new Vector();
        Vector listaAgrupaciones = new Vector();

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);

            sql =	"SELECT " +	tra_cod + "	FROM E_TRA WHERE " + tra_pro + "='"	+ codProcedemento	+
                    "' AND " + tra_mun + "=" + codMunicipio +	" AND	" + tra_fba	+ " IS NOT null";
            if(m_Log.isDebugEnabled()) m_Log.debug("Sentencia	SQL de tramites nulos :" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                String codTramiteNulo	= rs.getString(tra_cod);
                listaTramitesNulos.addElement(codTramiteNulo);
            }
            rs.close();
            st.close();

            // PESTAÑA DATOS

            from = "TRA_PRO,TRA_MUN, " + tra_cod + "," + tra_cou + ","+ tml_valor	+ ","	 + tra_vis + "," + tra_uin + "," + tra_utr +
                    "," + tra_ins + "," + tra_plz + "," + tra_und	+ ","	+ tra_are +	"," +	tra_ocu + "," +
                    tra_cls	+ "," + tra_pre + "," + tra_uti + "," + tra_utf + "," + tra_usi  + "," + tra_usf  + "," +
                    tra_ini  + "," + tra_inf + "," + tra_car + ", TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,"
                    + "TRA_TIPNOTFDP,TRA_FIN,TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_NOTIF_ELECT_OBLIG,COD_DEPTO_NOTIFICACION,"
                    + "TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN,TRA_TIPO_USUARIO_FIRMA,"
                    + "TRA_OTRO_COD_USUARIO_FIRMA,TRA_NOTIFICADO ";
            where	= tra_pro +	"='" + codProcedemento + "' AND " +	tra_mun +
                    "=" +	codMunicipio + " AND " + tra_fba + " IS null";
            String join[] = new String[5];
            join[0] = "E_TRA";
            join[1] = "INNER";
            join[2] = "e_tml";
            join[3] = "e_tra." + tra_mun + "=e_tml." + tml_mun + " AND " +
                    "e_tra." + tra_pro + "=e_tml." + tml_pro + " AND " +
                    "e_tra." + tra_cod + "=e_tml." + tml_tra + " AND " +
                    "e_tml." + tml_cmp + "='NOM'" +	" AND	" +
                    "e_tml." + tml_leng	+ "='"+idiomaDefecto+"'";
            join[4] = "false";
            sql =	oad.join(from,where,join);

            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getTramiteImportar: Sentencia SQL:" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar = "no";

            String codTramiteImportar = "";
            String codMunTramiteImportar = "";
            String codProcedimientoTramiteImportar = "";

            while(rs.next()){
                entrar = "si";
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                String codigoTramite = rs.getString(tra_cod);
                dTVO.setCodigoTramite(codigoTramite);
                String numeroTramite = rs.getString(tra_cou);
                dTVO.setNumeroTramite(numeroTramite);
                String nombreTramite = rs.getString(tml_valor);
                dTVO.setNombreTramite(nombreTramite);
                String disponible = rs.getString(tra_vis);
                dTVO.setDisponible(disponible);
                String codUnidadInicio = rs.getString(tra_uin);
                dTVO.setCodUnidadInicio(codUnidadInicio);
                String codUnidadTramite = rs.getString(tra_utr);
                dTVO.setCodUnidadTramite(codUnidadTramite);
                String plazo = rs.getString(tra_plz);
                dTVO.setPlazo(plazo);
                String unidadesPlazo = rs.getString(tra_und);
                dTVO.setUnidadesPlazo(unidadesPlazo);
                String codArea = rs.getString(tra_are);
                dTVO.setCodAreaTra(codArea);
                String ocurrencias = rs.getString(tra_ocu);
                dTVO.setOcurrencias(ocurrencias);
                String codClasificacionTramite = rs.getString(tra_cls);
                dTVO.setCodClasifTramite(codClasificacionTramite);
                String instrucciones = rs.getString(tra_ins);
                dTVO.setInstrucciones(instrucciones);
                String tramitePregunta = rs.getString(tra_pre);
                dTVO.setTramitePregunta(tramitePregunta);
                dTVO.setNotUnidadTramitIni(rs.getString(tra_uti));
                 String codigoCargo = rs.getString(tra_car);
                if(m_Log.isDebugEnabled()) m_Log.debug("............ notUnidadTramitIni: " + dTVO.getNotUnidadTramitIni());
                dTVO.setNotUnidadTramitFin(rs.getString(tra_utf));
                dTVO.setNotUsuUnidadTramitIni(rs.getString(tra_usi));
                dTVO.setNotUsuUnidadTramitFin(rs.getString(tra_usf));
                dTVO.setNotInteresadosIni(rs.getString(tra_ini));
                dTVO.setNotInteresadosFin(rs.getString(tra_inf));
                dTVO.setGenerarPlazos(rs.getBoolean("TRA_GENERARPLZ"));
                dTVO.setPlazoFin(rs.getInt("TRA_FIN"));
                dTVO.setNotificarCercaFinPlazo(rs.getBoolean("TRA_NOTCERCAFP"));
                dTVO.setNotificarFueraDePlazo(rs.getBoolean("TRA_NOTFUERADP"));
                dTVO.setTipoNotCercaFinPlazo(rs.getInt("TRA_TIPNOTCFP"));
                dTVO.setTipoNotFueraDePlazo(rs.getInt("TRA_TIPNOTFDP"));
                dTVO.setAdmiteNotificacionElectronica(rs.getString("TRA_NOTIFICACION_ELECTRONICA"));
                dTVO.setCodigoTipoNotificacionElectronica(rs.getString("TRA_COD_TIPO_NOTIFICACION"));
                dTVO.setCodDepartamentoNotificacion(rs.getString("COD_DEPTO_NOTIFICACION"));
                dTVO.setNotificacionElectronicaObligatoria(rs.getInt("TRA_NOTIF_ELECT_OBLIG") == 1);
                dTVO.setTipoUsuarioFirma(rs.getString("TRA_TIPO_USUARIO_FIRMA"));
                dTVO.setCodigoOtroUsuarioFirma(rs.getString("TRA_OTRO_COD_USUARIO_FIRMA"));
                dTVO.setTramiteNotificado(rs.getInt("TRA_NOTIFICADO")==1);
                dTVO.setCodPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_PLUGIN"));
                dTVO.setUrlPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_URL"));
                dTVO.setImplClassPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_IMPLCLASS"));
                dTVO.setCodUrlPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_ID_ENLACE_PLUGIN"));
                listaTramitesImportar.addElement(dTVO);
                defTramVO.setListaTramitesImportar(listaTramitesImportar);
                
                defTramVO.setNotificarCercaFinPlazo(rs.getBoolean("TRA_NOTCERCAFP"));
                defTramVO.setNotificarFueraDePlazo(rs.getBoolean("TRA_NOTFUERADP"));
                defTramVO.setTipoNotCercaFinPlazo(rs.getInt("TRA_TIPNOTCFP"));
                defTramVO.setTipoNotFueraDePlazo(rs.getInt("TRA_TIPNOTFDP"));
                

            }
            if(entrar.equals("no"))	{
                defTramVO.setListaTramitesImportar(listaTramitesImportar);
            }
            rs.close();
            st.close();

            // PESTAÑA DOCUMENTOS

            from =  dot_tra +	"," + dot_cod + ",a_plt." + aplt_des + " AS	nombreDocumento,"	+ dot_tdo +
                    ","	+ dot_vis +	",a_plt." + aplt_des + " AS plantilla," + dot_frm + "," + dot_plt + "," + dot_activo;
            where	= dot_mun +	"=" +
                    defTramVO.getCodMunicipio()	+ " AND " +	dot_pro + "='" + defTramVO.getTxtCodigo()	+ "'";
            for(int n=0; n<listaTramitesNulos.size();	n++) {
                String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                where += " AND " + dot_tra + "<>"	+ codTramiteNulo;
            }
            String[] join1 = new String[5];
            join1[0] = "E_DOT";
            join1[1] = "INNER";
            join1[2] = "a_plt";
            join1[3] = "e_dot." + dot_plt	+ "=a_plt." + aplt_cod + " AND " +
                    "e_dot." + dot_pro	+ "=a_plt." + aplt_pro + " AND " +
                    "e_dot." + dot_tra	+ "=a_plt." + aplt_tra;
            join1[4] = "false";
            sql =	oad.join(from,where,join1);

            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_DOT es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 =	"si";
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                String codTramite = rs.getString(dot_tra);
                dTVO.setNumeroTramite(codTramite);
                String codDocumento =	rs.getString(dot_cod);
                dTVO.setCodigoDoc(codDocumento);
                String nombreDocumento = rs.getString("nombreDocumento");
                dTVO.setNombreDoc(nombreDocumento);
                String codTipoDocumento = rs.getString(dot_tdo);
                dTVO.setCodTipoDoc(codTipoDocumento);
                String visible = rs.getString(dot_vis);
                dTVO.setVisibleInternet(visible);
                String plantilla = rs.getString("plantilla");
                dTVO.setPlantilla(plantilla);
                String firma = rs.getString(dot_frm);
                dTVO.setFirma(firma);
                String codPlantilla =	rs.getString(dot_plt);
                dTVO.setCodPlantilla(codPlantilla);
                String docActivo = rs.getString(dot_activo);
                dTVO.setDocActivo(docActivo);
                listaDocumentosImportar.addElement(dTVO);
                defTramVO.setListaDocumentosImportar(listaDocumentosImportar);
            }
            if(entrar1.equals("no")) {
                defTramVO.setListaDocumentosImportar(listaDocumentosImportar);
            }
            rs.close();
            st.close();
            
            //DATOS DE FIRMA DE DOCUMENTOS
            comprobarFirmaDocumentos(defTramVO, con);

            // PESTAÑA CONDICIONES DE ENTRADA
      sql =	"SELECT " +	ent_tra + "," + ent_cod	+ ","	+ ent_ctr +	"," +	ent_est  +	"," +	ent_tipo  +	"," +	ent_exp + ",ENT_DOC	FROM E_ENT WHERE " +
                    ent_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+ ent_pro +	"='" + defTramVO.getTxtCodigo() + "'";
            for(int n=0; n<listaTramitesNulos.size();	n++) {
                String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                sql	+= " AND " + ent_tra + "<>" +	codTramiteNulo;
            }

            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_ENT es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar2 = "no";
            while(rs.next()) {
                entrar2 =	"si";
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                String codTramiteInicial = rs.getString(ent_tra);
                dTVO.setNumeroTramite(codTramiteInicial);
                String codTramite = rs.getString(ent_ctr);
                dTVO.setCodTramiteCondEntrada(codTramite);
                String estadoTramite = rs.getString(ent_est);
                dTVO.setEstadoTramiteCondEntrada(estadoTramite);
                String codCondicion =	rs.getString(ent_cod);
                dTVO.setCodCondEntrada(codCondicion);
                String tipo = rs.getString(ent_tipo);
                dTVO.setTipoCondEntrada(tipo);
                String exp = rs.getString(ent_exp);
                dTVO.setExpresionCondEntrada(exp);
                String doc = rs.getString("ENT_DOC");
                dTVO.setCodDocumentoCondEntrada(doc);

                listaCondEntradaImportar.addElement(dTVO);
                defTramVO.setListaCondEntradaImportar(listaCondEntradaImportar);
            }
            if(entrar2.equals("no")) {
                defTramVO.setListaCondEntradaImportar(listaCondEntradaImportar);
            }                                 
            rs.close();
            st.close();

            //PESTAÑA CONDICIONES DE SALIDA

            sql =	"SELECT " +	sal_tra + "," + sal_tac	+ ","	+ sal_taa +	"," +	sal_tan + "," + sal_obl	+
                    "," +	sal_obld + " FROM	E_SAL	WHERE	" +  sal_mun + "=" + defTramVO.getCodMunicipio() +
                    " AND	" + sal_pro	+ "='" + defTramVO.getTxtCodigo() +	"'";
            for(int n=0; n<listaTramitesNulos.size();	n++) {
                String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                sql += " AND " + sal_tra + "<>"	+ codTramiteNulo;
            }

            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_SAL es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar3 = "no";
            while(rs.next()) {
                entrar3 =	"si";
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                String codTramite = rs.getString(sal_tra);
                if(m_Log.isDebugEnabled()) m_Log.debug("el codigo del	tramite es : " + codTramite);
                dTVO.setNumeroTramite(codTramite);
                String tipoCondicion = rs.getString(sal_tac);
                dTVO.setTipoCondicion(tipoCondicion);
                String tipoCondicionFav = rs.getString(sal_taa);
                dTVO.setTipoFavorableSI(tipoCondicionFav);
                String tipoCondicionDesf = rs.getString(sal_tan);
                dTVO.setTipoFavorableNO(tipoCondicionDesf);
                String obligatorio = rs.getString(sal_obl);
                dTVO.setObligatorio(obligatorio);
                String obligatorioDesf = rs.getString(sal_obld);
                dTVO.setObligatorioDesf(obligatorioDesf);
                listaCondSalidaImportar.addElement(dTVO);
                defTramVO.setListaCondSalidaImportar(listaCondSalidaImportar);
            }
            rs.close();
            st.close();

            String entrar5 = "no";
            if("si".equals(entrar3)) {
                sql	= "SELECT "	+ sml_tra +	"," +	sml_valor +	" FROM E_SML WHERE " + sml_mun + "=" + defTramVO.getCodMunicipio() +
                        " AND " +	sml_pro + "='" + defTramVO.getTxtCodigo()	+ "' AND " + sml_cmp + "='TXT' AND " +
                        sml_leng + "='"+idiomaDefecto+"'";
                for(int n=0; n<listaTramitesNulos.size();	n++) {
                    String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                    sql += " AND " + sml_tra + "<>"	+ codTramiteNulo;
                }
                if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_SML es :	" + sql);
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()) {
                    entrar5	= "si";
                    DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                    String codTramite =	rs.getString(sml_tra);
                    dTVO.setNumeroTramite(codTramite);
                    String texto = rs.getString(sml_valor);
                    dTVO.setTexto(texto);
                    listaPreguntaCondSalida.addElement(dTVO);
                    defTramVO.setListaPreguntaCondSalida(listaPreguntaCondSalida);
                }
                rs.close();
                st.close();

            }
            if(entrar3.equals("no")) {
                defTramVO.setListaCondSalidaImportar(listaCondSalidaImportar);
            }
            if(entrar5.equals("no")) {
                defTramVO.setListaPreguntaCondSalida(listaPreguntaCondSalida);
            }

            sql =	"SELECT " +	fls_tra + "," + fls_nuc	+ ","	+ fls_nus +	"," +	fls_cts + "	FROM E_FLS WHERE " +
                    fls_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+ fls_pro +	"='" +
                    defTramVO.getTxtCodigo() + "'";
            for(int n=0; n<listaTramitesNulos.size();	n++) {
                String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                sql	+= " AND " + fls_tra + "<>" +	codTramiteNulo;
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_FLS es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar4 = "no";
            while(rs.next()) {
                entrar4 =	"si";
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                String codTramiteInicialFlujoSalida = rs.getString(fls_tra);
                dTVO.setCodTramiteInicialFlujoSalida(codTramiteInicialFlujoSalida);
                String numeroCondSalida = rs.getString(fls_nuc);
                dTVO.setNumeroCondicionSalida(numeroCondSalida);
                String numeroSecuencia = rs.getString(fls_nus);
                dTVO.setNumeroSecuencia(numeroSecuencia);
                String codTramiteFlujoSalida = rs.getString(fls_cts);
                dTVO.setCodTramiteFlujoSalida(codTramiteFlujoSalida);
                listaFlujoSalida.addElement(dTVO);
                defTramVO.setListaFlujoSalida(listaFlujoSalida);
            }
            if(entrar4.equals("no")) {
                defTramVO.setListaFlujoSalida(listaFlujoSalida);
            }
            rs.close();
            st.close();

            // PESTAÑA DE ENLACES

            sql = "SELECT " + ten_tra + "," + ten_cod + "," + ten_des + "," + ten_url + "," + ten_est +
                    " FROM E_TEN WHERE " + ten_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+ ten_pro +	"='" +
                    defTramVO.getTxtCodigo() + "'";
            for(int n=0; n<listaTramitesNulos.size();	n++) {
                    String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                    sql += " AND " + ten_tra + "<>"	+ codTramiteNulo;
                }
            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_TEN es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar6 = "no";
            while(rs.next()) {
                entrar6 =	"si";
                GeneralValueObject gVO = new GeneralValueObject();
                String codTramite = rs.getString(ten_tra);
                gVO.setAtributo("codTramite",codTramite);
                String codEnlace = rs.getString(ten_cod);
                gVO.setAtributo("codEnlace",codEnlace);
                String descEnlace = rs.getString(ten_des);
                gVO.setAtributo("descEnlace",descEnlace);
                String url = rs.getString(ten_url);
                gVO.setAtributo("url",url);
                String estadoEnlace = rs.getString(ten_est);
                gVO.setAtributo("estadoEnlace",estadoEnlace);
                listaEnlaces.addElement(gVO);
                defTramVO.setListaEnlaces(listaEnlaces);
            }
            if(entrar6.equals("no")) {
                defTramVO.setListaEnlaces(listaEnlaces);
            }
            rs.close();
            st.close();

            // PESTAÑA DE CAMPOS
            
             sql = "Select TCA_ID_GROUP, TCA_DESC_GROUP, TCA_ORDER_GROUP, TCA_PRO, TCA_ACTIVE, TCA_TRA "
                    + " from E_TCA_GROUP "
                    + " where TCA_PRO = '" + defTramVO.getTxtCodigo() + "'";
            
            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_TCA_GROUP es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()) {
                GeneralValueObject gVO = new GeneralValueObject();
                String idGrupo = rs.getString("TCA_ID_GROUP");
                gVO.setAtributo("idGrupo",idGrupo);
                String descGrupo = rs.getString("TCA_DESC_GROUP");
                gVO.setAtributo("descGrupo",descGrupo);
                Integer ordenGrupo = rs.getInt("TCA_ORDER_GROUP");
                gVO.setAtributo("ordenGrupo",ordenGrupo);
                String procGrupo = rs.getString("TCA_PRO");
                gVO.setAtributo("procGrupo",procGrupo);
                String grupoActivo = rs.getString("TCA_ACTIVE");
                gVO.setAtributo("grupoActivo",grupoActivo);
                Integer traGrupo = rs.getInt("TCA_TRA");
                gVO.setAtributo("traGrupo",traGrupo);
                listaAgrupaciones.add(gVO);
                defTramVO.setListaAgrupaciones(listaAgrupaciones);
            }//while(rs.next())

            sql = "SELECT " + tca_tra + "," + tca_cod + "," + tca_des + "," + tca_plt + "," +
                    tca_tda + "," + tca_tam + "," + tca_mas + "," + tca_obl + "," + tca_nor + "," +
                    tca_rot + "," + tca_vis + "," + tca_activo + "," + tca_desplegable +",TCA_OCULTO,TCA_BLOQ,PLAZO_AVISO, PERIODO_PLAZO" +
                    ",PCA_GROUP, TCA_POS_X, TCA_POS_Y,EXPRESION_CAMPO_NUM_TRAM.EXPRESION AS EXPRESION ," +
                    " EXPRESION_CAMPO_CAL_TRAM.EXPRESION AS EXPRESION_CAL " +
                    " FROM E_TCA " + 
                    " LEFT JOIN EXPRESION_CAMPO_NUM_TRAM ON e_tca." + tca_mun + " = EXPRESION_CAMPO_NUM_TRAM.COD_ORGANIZACION " + 
                    " AND E_tCA." + tca_pro +" = EXPRESION_CAMPO_NUM_TRAM.COD_PROCEDIMIENTO "  +
                    " AND E_tCA." + tca_cod +" = EXPRESION_CAMPO_NUM_TRAM.COD_CAMPO " + 
                    " AND E_TCA." + tca_tra +" = EXPRESION_CAMPO_NUM_TRAM.COD_TRAMITE" +
                    " LEFT JOIN EXPRESION_CAMPO_CAL_TRAM ON e_tca." + tca_mun + " = EXPRESION_CAMPO_CAL_TRAM.COD_ORGANIZACION " +
                    " AND E_tCA." + tca_pro +" = EXPRESION_CAMPO_CAL_TRAM.COD_PROCEDIMIENTO "  +
                    " AND E_tCA." + tca_cod +" = EXPRESION_CAMPO_CAL_TRAM.COD_CAMPO "+
                    " AND E_TCA." + tca_tra +" = EXPRESION_CAMPO_CAL_TRAM.COD_TRAMITE" +
                    " WHERE " + tca_mun + "=" + 
                    defTramVO.getCodMunicipio() + "	AND "	+ tca_pro +	"='" + 
                    defTramVO.getTxtCodigo() + "'";
             for(int n=0; n<listaTramitesNulos.size();	n++) {
                    String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                    sql += " AND " + tca_tra + "<>"	+ codTramiteNulo;
                }
            if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_TCA es : "	+ sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String entrar7 = "no";
            while(rs.next()) {
                entrar7 =	"si";
                GeneralValueObject gVO = new GeneralValueObject();
                String codTramite = rs.getString(tca_tra);
                gVO.setAtributo("codTramite",codTramite);
                String codCampo = rs.getString(tca_cod);
                gVO.setAtributo("codCampo",codCampo);
                String descCampo = rs.getString(tca_des);
                gVO.setAtributo("descCampo",descCampo);
                String codTipoDato = rs.getString(tca_tda);
                gVO.setAtributo("codTipoDato",codTipoDato);
                if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable")) || codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
                    String codCampoDesplegable = rs.getString(tca_desplegable);
                    gVO.setAtributo("codPlantilla",codCampoDesplegable);
                } else {
                    String codPlantilla = rs.getString(tca_plt);
                    gVO.setAtributo("codPlantilla",codPlantilla);
                }
                String tamano = rs.getString(tca_tam);
                gVO.setAtributo("tamano",tamano);
                String mascara = rs.getString(tca_mas);
                gVO.setAtributo("mascara",mascara);
                String obligatorio = rs.getString(tca_obl);
                gVO.setAtributo("obligatorio",obligatorio);
                String numeroOrden = rs.getString(tca_nor);
                gVO.setAtributo("numeroOrden",numeroOrden);
                String rotulo = rs.getString(tca_rot);
                gVO.setAtributo("rotulo",rotulo);
                String visibleExp = rs.getString(tca_vis);
                gVO.setAtributo("visibleExp",visibleExp);
                String activo = rs.getString(tca_activo);
                gVO.setAtributo("activo",activo);
                String oculto = rs.getString("TCA_OCULTO");
                gVO.setAtributo("oculto",oculto);
                String bloqueado = rs.getString("TCA_BLOQ");
                gVO.setAtributo("bloqueado",bloqueado);
                //PLAZO_AVISO, PERIODO_PLAZO
                String plazoFecha = rs.getString("PLAZO_AVISO");
                gVO.setAtributo("plazoFecha",plazoFecha); 
                String checkPlazoFecha = rs.getString("PERIODO_PLAZO");
                gVO.setAtributo("checkPlazoFecha",checkPlazoFecha);
                String codAgrupacion = rs.getString("PCA_GROUP");
                gVO.setAtributo("grupoCampo",codAgrupacion);
                Integer posX = rs.getInt("TCA_POS_X");
                if (rs.wasNull()) {
                    gVO.setAtributo("posX","");
                }else  gVO.setAtributo("posX",String.valueOf(posX));
               
                Integer posY = rs.getInt("TCA_POS_Y");
                if (rs.wasNull()) {
                    gVO.setAtributo("posY","");
                }else  gVO.setAtributo("posY",String.valueOf(posY));
               
                String validacion = rs.getString("EXPRESION");
                gVO.setAtributo("validacion",validacion);
                String operacion = rs.getString("EXPRESION_CAL");
                gVO.setAtributo("operacion",operacion);                
                
                listaCampos.addElement(gVO);
                defTramVO.setListaCampos(listaCampos);
            }
            if(entrar7.equals("no")) {
                defTramVO.setListaCampos(listaCampos);
            }
            rs.close();
            st.close(); 

            // Plantillas de documentos
            sql = "SELECT " + aplt_cod + "," + aplt_des + "," + aplt_tra + "," + aplt_int + ", PLT_REL, PLT_EDITOR_TEXTO, " + aplt_doc +
                    " FROM A_PLT WHERE " + aplt_apl + "=4 AND " + aplt_pro + "='" +
                    defTramVO.getTxtCodigo() + "'";
             for(int n=0; n<listaTramitesNulos.size();	n++) {
                    String codTramiteNulo	= (String) listaTramitesNulos.elementAt(n);
                    sql += " AND " + aplt_tra + "<>"	+ codTramiteNulo;
                }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            String entrar8 = "no";
            while(rs.next()) {
                entrar8 = "si";
                GeneralValueObject g = new GeneralValueObject();
                String codDocumento = rs.getString(aplt_cod);
                g.setAtributo("codDocumento",codDocumento);
                String descDocumento = rs.getString(aplt_des);
                g.setAtributo("descDocumento",descDocumento);
                String codTramite = rs.getString(aplt_tra);
                g.setAtributo("codTramite",codTramite);
                String interesado = rs.getString(aplt_int);
                g.setAtributo("interesado",interesado);
                String relacion = rs.getString("PLT_REL");
                g.setAtributo("relacion",relacion);
                String editotTexto = rs.getString("PLT_EDITOR_TEXTO");
                g.setAtributo("editotTexto",editotTexto);

                byte[] r = null;
                java.io.InputStream ist = rs.getBinaryStream(aplt_doc);
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                while ((c = ist.read())!= -1){
                    ot.write(c);
                }
                ot.flush();
                r = ot.toByteArray();
                ot.close();
                ist.close();

                g.setAtributo("documento",r);
                listaPlantillas.addElement(g);
                defTramVO.setListaPlantillas(listaPlantillas);
            }
            if(entrar8.equals("no")) {
                defTramVO.setListaPlantillas(listaPlantillas);
            }
            rs.close();
            stmt.close();
        }catch (Exception	e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionTramitesDAO.getTramiteImportar"), e);
        }finally {
            try{
                if (rs!=null) rs.close();
                if (st!=null) st.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getTramiteImportar");
        return defTramVO;
    }

    public int insertImportar(Connection conexion, DefinicionTramitesValueObject dTVO, String[] params)
            throws TechnicalException, BDException, AnotacionRegistroException {
        String sql;
        String sqlCondSML;
        int res = 0;
        int res1;
        int res2 = 0;
        int resDuplicarPlantilla = 1;
        int resCondEnt = 0;
        int resTotal = 0;
        int res3 = 0;
        int resSML = 0;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        PreparedStatement ps;
        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("params[0]: " + params[0]);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("params[6]: " + params[6]);
            }

            // PESTAÑA DE DATOS
            Vector listaTramitesImportar = dTVO.getListaTramitesImportar();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("NUMERO DE TRAMITES A IMPORTAR:	" + listaTramitesImportar.size());
            }
            for (int i = 0; i < listaTramitesImportar.size(); i++) {
                DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) listaTramitesImportar.elementAt(i);
                sql = "INSERT INTO E_TRA (" + tra_mun + "," + tra_pro + "," + tra_cod + "," + tra_cou + ","
                        + tra_vis + "," + tra_uin + "," + tra_utr + "," + tra_plz + "," + tra_und + ","
                        + tra_ins + "," + tra_are + "," + tra_ocu + "," + tra_cls + "," + tra_pre + ","
                        + tra_uti + "," + tra_utf + "," + tra_usi + "," + tra_usf + "," + tra_ini + ","
                        + tra_inf + "," + tra_car
                        + ", TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP, TRA_FIN,"
                        + "TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_NOTIF_ELECT_OBLIG,COD_DEPTO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,"
                        + "TRA_OTRO_COD_USUARIO_FIRMA,TRA_NOTIFICADO,"
                        + "TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN) "
                        + "VALUES (" + dTVO.getCodMunicipio() + ",'" + dTVO.getTxtCodigo() + "',"
                        + defTramVO.getCodigoTramite() + "," + defTramVO.getNumeroTramite() + ","
                        + defTramVO.getDisponible();
                if (defTramVO.getCodUnidadInicio() == null || defTramVO.getCodUnidadInicio().equals("")) {
                    sql += ",null";
                } else {
                    sql += "," + defTramVO.getCodUnidadInicio();
                }
                if (defTramVO.getCodUnidadTramite() == null || defTramVO.getCodUnidadTramite().equals("")) {
                    sql += ",null";
                } else {
                    sql += "," + defTramVO.getCodUnidadTramite();
                }
                if (defTramVO.getPlazo() != null) {
                    if (!defTramVO.getPlazo().equals("")) {
                        sql += "," + defTramVO.getPlazo();
                    } else {
                        sql += ", null";
                    }
                } else {
                    sql += ", null";
                }
                if (defTramVO.getUnidadesPlazo() != null) {
                    if (!defTramVO.getUnidadesPlazo().equals("")) {
                        sql += ",'" + defTramVO.getUnidadesPlazo() + "'";
                    } else {
                        sql += ", null";
                    }
                } else {
                    sql += ", null";
                }
                if (defTramVO.getInstrucciones() != null) {
                    if (!"".equals(defTramVO.getInstrucciones())) {
                        sql += ",'" + defTramVO.getInstrucciones() + "'";
                        defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(defTramVO.getInstrucciones()));
                    } else {
                        sql += ",null";
                    }
                } else {
                    sql += ",null";
                }

                sql += "," + defTramVO.getCodAreaTra();
                if (defTramVO.getOcurrencias() != null) {
                    if (!defTramVO.getOcurrencias().equals("")) {
                        sql += "," + defTramVO.getOcurrencias();
                    } else {
                        sql += ", null";
                    }
                } else {
                    sql += ", null";
                }

                sql += "," + defTramVO.getCodClasifTramite() + "," + defTramVO.getTramitePregunta();

                if ((defTramVO.getNotUnidadTramitIni() != null) && !(defTramVO.getNotUnidadTramitIni().equals(""))) {
                    sql += ",'" + defTramVO.getNotUnidadTramitIni() + "'";
                } else {
                    sql += ", null";
                }
                if ((defTramVO.getNotUnidadTramitFin() != null) && !(defTramVO.getNotUnidadTramitFin().equals(""))) {
                    sql += ",'" + defTramVO.getNotUnidadTramitFin() + "'";
                } else {
                    sql += ", null";
                }
                if ((defTramVO.getNotUsuUnidadTramitIni() != null) && !(defTramVO.getNotUsuUnidadTramitIni().equals(""))) {
                    sql += ",'" + defTramVO.getNotUsuUnidadTramitIni() + "'";
                } else {
                    sql += ", null";
                }
                if ((defTramVO.getNotUsuUnidadTramitFin() != null) && !(defTramVO.getNotUsuUnidadTramitFin().equals(""))) {
                    sql += ",'" + defTramVO.getNotUsuUnidadTramitFin() + "'";
                } else {
                    sql += ", null";
                }
                if ((defTramVO.getNotInteresadosIni() != null) && !(defTramVO.getNotInteresadosIni().equals(""))) {
                    sql += ",'" + defTramVO.getNotInteresadosIni() + "'";
                } else {
                    sql += ", null";
                }
                if ((defTramVO.getNotInteresadosFin() != null) && !(defTramVO.getNotInteresadosFin().equals(""))) {
                    sql += ",'" + defTramVO.getNotInteresadosFin() + "'";
                } else {
                    sql += ", null";

                }

                if (defTramVO.getCodCargo() != null) {
                    if (!defTramVO.getCodCargo().equals("")) {
                        sql += "," + defTramVO.getCodCargo();
                    } else {
                        sql += ", null";
                    }
                } else {
                    sql += ", null";
                }

                sql += ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                ps = conexion.prepareStatement(sql);

                int j = 1;
                ps.setBoolean(j++, defTramVO.isGenerarPlazos());
                ps.setBoolean(j++, defTramVO.getNotificarCercaFinPlazo());
                ps.setBoolean(j++, defTramVO.getNotificarFueraDePlazo());
                ps.setInt(j++, defTramVO.getTipoNotCercaFinPlazo());
                ps.setInt(j++, defTramVO.getTipoNotFueraDePlazo());
                ps.setInt(j++, defTramVO.getPlazoFin()); // Porcentaje de aviso

                if (defTramVO.getAdmiteNotificacionElectronica() == null || defTramVO.getAdmiteNotificacionElectronica().equals("")) {
                    ps.setNull(j++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(j++, defTramVO.getAdmiteNotificacionElectronica());
                }
                if (defTramVO.getCodigoTipoNotificacionElectronica() == null || defTramVO.getCodigoTipoNotificacionElectronica().equals("")) {
                    ps.setNull(j++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(j++, Integer.parseInt(defTramVO.getCodigoTipoNotificacionElectronica()));
                }

                sql += ",'" + defTramVO.getCodigoTipoNotificacionElectronica() + "'";

                ps.setInt(j++, (defTramVO.getNotificacionElectronicaObligatoria()) ? 1 : 0);
                if (defTramVO.getCodDepartamentoNotificacion() == null || defTramVO.getCodDepartamentoNotificacion().equals("")) {
                    ps.setNull(j++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(j++, defTramVO.getCodDepartamentoNotificacion());
                }
                if (defTramVO.getTipoUsuarioFirma() == null || defTramVO.getTipoUsuarioFirma().equals("")) {
                    ps.setNull(j++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(j++, defTramVO.getTipoUsuarioFirma());
                }
                if (defTramVO.getCodigoOtroUsuarioFirma() == null || defTramVO.getCodigoOtroUsuarioFirma().equals("")) {
                    ps.setNull(j++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(j++, Integer.parseInt(defTramVO.getCodigoOtroUsuarioFirma()));
                }
                ps.setInt(j++, (defTramVO.isTramiteNotificado() ? 1 : 0));
                if (defTramVO.getCodPluginPantallaTramitacionExterna() == null || defTramVO.getCodPluginPantallaTramitacionExterna().equals("")) {
                    ps.setNull(j++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(j++, defTramVO.getCodPluginPantallaTramitacionExterna());
                }
                if (defTramVO.getUrlPluginPantallaTramitacionExterna() == null || defTramVO.getUrlPluginPantallaTramitacionExterna().equals("")) {
                    ps.setNull(j++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(j++, defTramVO.getUrlPluginPantallaTramitacionExterna());
                }
                if (defTramVO.getImplClassPluginPantallaTramitacionExterna() == null || defTramVO.getImplClassPluginPantallaTramitacionExterna().equals("")) {
                    ps.setNull(j++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(j++, defTramVO.getImplClassPluginPantallaTramitacionExterna());
                }
                if (defTramVO.getCodUrlPluginPantallaTramitacionExterna() == null || defTramVO.getCodUrlPluginPantallaTramitacionExterna().equals("")) {
                    ps.setNull(j++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(j++, defTramVO.getCodUrlPluginPantallaTramitacionExterna());
                }

                res = ps.executeUpdate();
                ps.close();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("las filas afectadas en el insert E_TRA son :::::::::::::: :	" + res);
                }

                sql = "INSERT INTO E_TML VALUES(" + dTVO.getCodMunicipio()
                        + ",'" + dTVO.getTxtCodigo() + "'," + defTramVO.getCodigoTramite() + ",'NOM','"
                        + m_ConfigTechnical.getString("idiomaDefecto") + "','"
                        + defTramVO.getNombreTramite() + "')";

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                ps = conexion.prepareStatement(sql);
                res1 = ps.executeUpdate();
                ps.close();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("las filas afectadas en el insert E_TML son :::::::::::::: :	" + res1);
                }

            }

            if (res != 0) {
                // PESTAÑA DE	DOCUMENTOS
                Vector listaDocumentosImportar = dTVO.getListaDocumentosImportar();
                for (int i = 0; i < listaDocumentosImportar.size(); i++) {
                    DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) listaDocumentosImportar.elementAt(i);
                    String codA_PLT = duplicarPlantilla(abd, conexion, defTramVO, dTVO.getTxtCodigo(), dTVO.getListaPlantillas());

                    if (codA_PLT != null && !"".equals(codA_PLT)) {
                        sql = "INSERT INTO E_DOT (" + dot_mun + "," + dot_pro + "," + dot_tra + "," + dot_cod
                                + "," + dot_tdo + "," + dot_vis + "," + dot_frm + "," + dot_plt + "," + dot_activo + ")	VALUES("
                                + dTVO.getCodMunicipio() + ",'" + dTVO.getTxtCodigo() + "'," + defTramVO.getNumeroTramite() + ","
                                + defTramVO.getCodigoDoc() + ",null,'" + defTramVO.getVisibleInternet() + "',";
                        if(defTramVO.getFirma() != null){
                            sql +=  "'" + defTramVO.getFirma() + "',";
                        } else {
                            sql += "null,";
                        }
                        sql += codA_PLT + ", '" + defTramVO.getDocActivo() + "')";
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        ps = conexion.prepareStatement(sql);
                        ps.executeUpdate();
                        ps.close();
                    } else {
                        resDuplicarPlantilla = 0;
                        i = listaDocumentosImportar.size();
                    }
                }
                if (resDuplicarPlantilla != 0) {
                    //DATOS DE FIRMA DE DOCUMENTOS
                    insertarInfoFirmaDocumentos(dTVO, conexion);
                    
                    // PESTAÑA DE	CONDICIONES	DE ENTRADA

                    Vector listaCondEntradaImportar = dTVO.getListaCondEntradaImportar();
                    for (int i = 0; i < listaCondEntradaImportar.size(); i++) {
                        DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) listaCondEntradaImportar.elementAt(i);
                        sql = "INSERT INTO E_ENT (" + ent_mun + "," + ent_pro + "," + ent_tra + "," + ent_cod + ","
                                + ent_ctr + "," + ent_est + "," + ent_tipo + "," + ent_exp + ",ENT_DOC) VALUES(" + dTVO.getCodMunicipio() + ",'"
                                + dTVO.getTxtCodigo() + "'," + defTramVO.getNumeroTramite() + ","
                                + defTramVO.getCodCondEntrada() + "," + defTramVO.getCodTramiteCondEntrada()
                                + ",'" + defTramVO.getEstadoTramiteCondEntrada() + "','" + defTramVO.getTipoCondEntrada()
                                + "',";
                        String aux = "";
                        if (defTramVO.getExpresionCondEntrada() != null) {
                            aux = defTramVO.getExpresionCondEntrada().replaceAll("&lt;", "<");
                            aux = aux.replaceAll("&gt;", ">");
                            aux = aux.replaceAll("&#39;", "'");
                        }

                        sql += abd.addString(aux) + "," + defTramVO.getCodDocumentoCondEntrada() + ")";

                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        ps = conexion.prepareStatement(sql);
                        resCondEnt += ps.executeUpdate();
                        ps.close();
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("las	filas	afectadas en el insert	de E_ENT son resCondEnt:::::::::::::: :	" + resCondEnt);
                    }
                    if (listaCondEntradaImportar.size() == 0) {
                        resCondEnt = 1;
                    }

                    // PESTAÑA DE	CONDICIONES	DE SALIDA
                    Vector listaCondSalidaImportar = dTVO.getListaCondSalidaImportar();
                    for (int i = 0; i < listaCondSalidaImportar.size(); i++) {
                        DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) listaCondSalidaImportar.elementAt(i);
                        sql = "INSERT INTO E_SAL (" + sal_mun + "," + sal_pro + "," + sal_tra + ","
                                + sal_tac + "," + sal_taa + "," + sal_tan + "," + sal_obl + "," + sal_obld
                                + ") VALUES( " + dTVO.getCodMunicipio() + ",'"
                                + dTVO.getTxtCodigo() + "'," + defTramVO.getNumeroTramite() + ",";
                        if (defTramVO.getTipoCondicion() == null) {
                            sql += "null,";
                        } else {
                            sql += "'" + defTramVO.getTipoCondicion() + "',";
                        }
                        if (defTramVO.getTipoFavorableSI() == null) {
                            sql += "null,";
                        } else {
                            sql += "'" + defTramVO.getTipoFavorableSI() + "',";
                        }
                        if (defTramVO.getTipoFavorableNO() == null) {
                            sql += "null,";
                        } else {
                            sql += "'" + defTramVO.getTipoFavorableNO() + "',";
                        }
                        if (defTramVO.getObligatorio() == null) {
                            sql += "null,";
                        } else {
                            sql += defTramVO.getObligatorio() + ",";
                        }
                        if (defTramVO.getObligatorioDesf() == null) {
                            sql += "null)";
                        } else {
                            sql += defTramVO.getObligatorioDesf() + ")";
                        }

                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        ps = conexion.prepareStatement(sql);
                        res2 += ps.executeUpdate();
                        ps.close();
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("las	filas	afectadas en el INSERT de E_SAL son	res2:::::::::::::: : " + res2);
                    }

                    Vector listaPreguntaCondSalida = dTVO.getListaPreguntaCondSalida();
                    for (int i = 0; i < listaPreguntaCondSalida.size(); i++) {
                        DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) listaPreguntaCondSalida.elementAt(i);
                        sqlCondSML = "INSERT INTO E_SML (" + sml_mun + "," + sml_pro + "," + sml_tra + ","
                                + sml_cmp + "," + sml_leng + "," + sml_valor + ") VALUES("
                                + dTVO.getCodMunicipio() + ",'" + dTVO.getTxtCodigo() + "',"
                                + defTramVO.getNumeroTramite() + ",'TXT','" + idiomaDefecto + "','" + defTramVO.getTexto() + "')";
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sqlCondSML);
                        }
                        ps = conexion.prepareStatement(sqlCondSML);
                        resSML += ps.executeUpdate();
                        ps.close();
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("las	filas	afectadas en el INSERT de E_SML son	resSML:::::::::::::: : " + resSML);
                    }

                    Vector listaFlujoSalida = dTVO.getListaFlujoSalida();
                    for (int i = 0; i < listaFlujoSalida.size(); i++) {
                        DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) listaFlujoSalida.elementAt(i);
                        sql = "INSERT INTO E_FLS (" + fls_mun + "," + fls_pro + "," + fls_tra + ","
                                + fls_nuc + "," + fls_nus + "," + fls_cts + ") VALUES( " + dTVO.getCodMunicipio() + ",'"
                                + dTVO.getTxtCodigo() + "'," + defTramVO.getCodTramiteInicialFlujoSalida() + ","
                                + defTramVO.getNumeroCondicionSalida() + "," + defTramVO.getNumeroSecuencia() + ","
                                + defTramVO.getCodTramiteFlujoSalida() + ")";
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        ps = conexion.prepareStatement(sql);
                        res3 += ps.executeUpdate();
                        ps.close();
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("las	filas	afectadas en el INSERT de E_FLS son	res3:::::::::::::: : " + res3);
                    }

                    // PESTAÑA DE ENLACES
                    Vector listaEnlaces = dTVO.getListaEnlaces();
                    for (int i = 0; i < listaEnlaces.size(); i++) {
                        GeneralValueObject gVO = (GeneralValueObject) listaEnlaces.elementAt(i);
                        sql = "INSERT INTO E_TEN (" + ten_mun + "," + ten_pro + "," + ten_tra + ","
                                + ten_cod + "," + ten_des + "," + ten_url + "," + ten_est + ") VALUES("
                                + dTVO.getCodMunicipio() + ",'" + dTVO.getTxtCodigo() + "',"
                                + gVO.getAtributo("codTramite") + "," + gVO.getAtributo("codEnlace")
                                + ",'" + gVO.getAtributo("descEnlace") + "','"
                                + gVO.getAtributo("url") + "'," + gVO.getAtributo("estadoEnlace") + ")";
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        ps = conexion.prepareStatement(sql);
                        res += ps.executeUpdate();
                        ps.close();
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("las	filas	afectadas en el insert	de E_TEN son res:::::::::::::: :	" + res);
                    }

                    // PESTAÑA DE CAMPOS
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Insertamos las agrupaciones de tramite");
                    }
                    Vector listaAgrupaciones = dTVO.getListaAgrupaciones();
                    if (listaAgrupaciones != null) {
                        for (int w = 0; w < listaAgrupaciones.size(); w++) {
                            GeneralValueObject gVO = (GeneralValueObject) listaAgrupaciones.elementAt(w);
                            // Si la agrupacion esta activa se guarda para el nuevo procedimiento.
                            if ("SI".equals(gVO.getAtributo("grupoActivo")) || "si".equals(gVO.getAtributo("grupoActivo"))) {
                                String idGrupo = (String) gVO.getAtributo("idGrupo");
                                String descGrupo = (String) gVO.getAtributo("descGrupo");
                                Integer ordenGrupo = (Integer) gVO.getAtributo("ordenGrupo");
                                String procGrupo = (String) gVO.getAtributo("procGrupo");
                                String grupoActivo = (String) gVO.getAtributo("grupoActivo");
                                Integer traGrupo = (Integer) gVO.getAtributo("traGrupo");

                                sql = "Insert into E_TCA_GROUP (TCA_ID_GROUP, TCA_DESC_GROUP, TCA_ORDER_GROUP, TCA_PRO, TCA_ACTIVE, TCA_TRA) "
                                        + " values('" + idGrupo + "','" + descGrupo + "'," + ordenGrupo + ",'" + dTVO.getTxtCodigo() + "',"
                                        + "'" + grupoActivo + "'," + traGrupo + ")";

                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug(sql);
                                }
                                ps = conexion.prepareStatement(sql);
                                res += ps.executeUpdate();
                                ps.close();
                            }//if("SI".equals(gVO.getAtributo("grupoActivo")) || "si".equals(gVO.getAtributo("grupoActivo")))
                        }//for(int w=0;w<listaAgrupaciones.size();w++) 
                    }
                    Vector listaCampos = dTVO.getListaCampos();
                    for (int i = 0; i < listaCampos.size(); i++) {
                        GeneralValueObject gVO = (GeneralValueObject) listaCampos.elementAt(i);
                        String plantilla, desplegable = "";
                        if (gVO.getAtributo("codTipoDato").equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable"))) {
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable");
                            desplegable = (String) gVO.getAtributo("codPlantilla");
                        } else if (gVO.getAtributo("codTipoDato").equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))){ //desplegables externos
                            plantilla = m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt");
                            desplegable = (String) gVO.getAtributo("codPlantilla");
                        } else {
                            plantilla = (String) gVO.getAtributo("codPlantilla");
                            desplegable = "";
                        }

                        // Si el campo suplementario está activo se guarda para el nuevo procedimiento.
                        if ("SI".equals(gVO.getAtributo("activo")) || "si".equals(gVO.getAtributo("activo"))) {
                            sql = "INSERT INTO E_TCA (" + tca_mun + "," + tca_pro + "," + tca_tra + ","
                                    + tca_cod + "," + tca_des + "," + tca_plt + "," + tca_tda + "," + tca_tam + ","
                                    + tca_mas + "," + tca_obl + "," + tca_nor + "," + tca_rot + "," + tca_vis + ","
                                    + tca_activo + "," + tca_desplegable + ",TCA_OCULTO,TCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO, PCA_GROUP, TCA_POS_X,"
                                    + "TCA_POS_Y) VALUES ("
                                    + dTVO.getCodMunicipio() + ",'" + dTVO.getTxtCodigo() + "'," + gVO.getAtributo("codTramite") + ",'"
                                    + gVO.getAtributo("codCampo") + "','" + gVO.getAtributo("descCampo") + "',"
                                    + plantilla + "," + gVO.getAtributo("codTipoDato") + "," + gVO.getAtributo("tamano") + ",'"
                                    + gVO.getAtributo("mascara") + "'," + gVO.getAtributo("obligatorio") + ","
                                    + gVO.getAtributo("numeroOrden") + ",'" + gVO.getAtributo("rotulo") + "','"
                                    + gVO.getAtributo("visibleExp") + "','" + gVO.getAtributo("activo") + "','"
                                    + desplegable + "','" + gVO.getAtributo("oculto") + "','" + gVO.getAtributo("bloqueado") + "',";
                            //insertamos el campo PLAZO_AVISO Y pERIODO_PLAZO
                            if (gVO.getAtributo("plazoFecha") == null || "".equals(gVO.getAtributo("plazoFecha")) || " ".equals(gVO.getAtributo("plazoFecha"))) {
                                sql += "null, null";
                            } else {
                                sql += "'" + gVO.getAtributo("plazoFecha") + "','" + gVO.getAtributo("checkPlazoFecha") + "'";
                            }

                            if (gVO.getAtributo("grupoCampo") == null || "".equals(gVO.getAtributo("grupoCampo")) || " ".equals(gVO.getAtributo("grupoCampo"))) {
                                sql += ",null";
                            } else {
                                sql += ",'" + gVO.getAtributo("grupoCampo") + "'";
                            }

                            if (gVO.getAtributo("posX") == null
                                    || "".equals(gVO.getAtributo("posX"))
                                    || " ".equals(gVO.getAtributo("posX"))
                                    || "undefined".equals(gVO.getAtributo("posX"))) {
                                sql += ",null";
                            } else {
                                sql += "," + gVO.getAtributo("posX");
                            }

                            if (gVO.getAtributo("posY") == null
                                    || "".equals(gVO.getAtributo("posY"))
                                    || " ".equals(gVO.getAtributo("posY"))
                                    || "undefined".equals(gVO.getAtributo("posY"))) {
                                sql += ",null)";
                            } else {
                                sql += "," + gVO.getAtributo("posY") + ")";
                            }

                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug(sql);
                            }
                            ps = conexion.prepareStatement(sql);
                            res += ps.executeUpdate();
                            ps.close();
                        }

                        String tip_dat = (String) gVO.getAtributo("codTipoDato");
                        String valida = (String) gVO.getAtributo("validacion");
                        String CodCampo = (String) gVO.getAtributo("codCampo");

                        if (tip_dat.trim().equals("1") && !"".equals(valida)) {
                            //valida = listaValidacion.elementAt(contador_expresion).toString();
                            valida = valida.replace("/&lt;/g", "<");
                            valida = valida.replace("/&gt;/g", ">");
                            valida = valida.replace("&lt;", "<");
                            valida = valida.replace("&gt;", ">");

                            sql = " INSERT INTO EXPRESION_CAMPO_NUM_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,EXPRESION) VALUES "
                                    + " (" + dTVO.getCodMunicipio() + ",'" + dTVO.getTxtCodigo() + "'," + gVO.getAtributo("codTramite") + ",'" + CodCampo + "','" + valida
                                    + "')";
                            ps = conexion.prepareStatement(sql);
                            ps.executeUpdate();
                            ps.close();
                        }
                        if (tip_dat.trim().equals("8") || tip_dat.trim().equals("9")) {
                            valida = (String) gVO.getAtributo("operacion");
                            if (!"".equals(valida)) {
                                sql = " INSERT INTO EXPRESION_CAMPO_CAL_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,TIPO_DATO,EXPRESION) VALUES "
                                        + " (" + dTVO.getCodMunicipio() + ",'" + dTVO.getTxtCodigo() + "'," + gVO.getAtributo("codTramite") + ",'" + CodCampo + "'," + tip_dat + ",'" + valida
                                        + "')";
                                ps = conexion.prepareStatement(sql);
                                ps.executeUpdate();
                                ps.close();
                                m_Log.debug("grabacion de nueva expresion de calculo = " + valida);
                            }
                        }
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("las	filas	afectadas en el insert	de E_TCA son res:::::::::::::: :	" + res);
                    }
                }
            }

            if (listaTramitesImportar.size() == 0) {
                resTotal = 1;
            }

            if (res != 0 && res2 != 0 && resCondEnt != 0 && resDuplicarPlantilla != 0) {
                resTotal = 1;
            } else {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("res VALE: " + res + " resDuplicarPlantilla VALE: " + resDuplicarPlantilla + " res2 VALE: " + res2 + " resCondEnt VALE: " + resCondEnt);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("el entero que devuelve el insert es	: " + resTotal);
        }
        return resTotal;
    }

    public int insertPlantillasDocumentos(DefinicionTramitesValueObject dTVO,String[] params) throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        int res	= 0;
        String codMunic = "";
        String codProc = "";
        AdaptadorSQLBD abd = null;
        Connection conexion	= null;
        ResultSet rs = null;
        PreparedStatement ps = null;


        Vector listaPlantillas = new Vector();
        listaPlantillas = dTVO.getListaPlantillas();
        if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de plantillas es : " + listaPlantillas.size());
        for(int i=0;i<listaPlantillas.size();i++) {

            try{

                if(m_Log.isDebugEnabled()) m_Log.debug("A por el OAD");
                abd =	new AdaptadorSQLBD(params);
                if(m_Log.isDebugEnabled()) m_Log.debug("A por la conexion");
                conexion = abd.getConnection();
                abd.inicioTransaccion(conexion);

                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) listaPlantillas.elementAt(i);
                byte[] documento = (byte[])gVO.getAtributo("documento");
                sql = "INSERT INTO A_PLT (" + aplt_cod + "," + aplt_des + "," + aplt_apl +
                        "," + aplt_pro + "," + aplt_tra + "," + aplt_int + "," + aplt_doc + ",PLT_REL) VALUES ("+
                        (String)gVO.getAtributo("codDocumento") + ",'" + (String)gVO.getAtributo("descDocumento") +
                        "',4,'" + dTVO.getTxtCodigo() + "'," + (String)gVO.getAtributo("codTramite") + ",'" +
                        (String)gVO.getAtributo("interesado") + "',?,'"+ (String)gVO.getAtributo("relacion") +"')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                java.io.InputStream ist = new java.io.ByteArrayInputStream(documento);
                ps.setBinaryStream(1,ist,documento.length);
                res = ps.executeUpdate();
                ist.close();
                ps.close();

                abd.finTransaccion(conexion);

            }catch (Exception ex)	{
                abd.rollBack(conexion);
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            }finally {
                try{
                    abd.devolverConexion(conexion);
                } catch(Exception ex)	{
                    ex.printStackTrace();
                    if(m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                }
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("el entero que devuelve el insert es	: " +	res);
        return res;
    }

    private GeneralValueObject buscarDocumentoPlantilla(String codPLT, Vector plantillasProcedimiento){
        GeneralValueObject res = null;
        boolean encontrado=false;
        int i=0;
        if (m_Log.isDebugEnabled()) m_Log.debug("buscarDocumentoPlantilla: plantilla que se va a copiar: " + codPLT + ".");
        if (plantillasProcedimiento != null){
            while (!encontrado && i<plantillasProcedimiento.size()){
                GeneralValueObject  gVO = (GeneralValueObject) plantillasProcedimiento.elementAt(i);
                String codPLT_gVO = (String) gVO.getAtributo("codDocumento");
                if (codPLT_gVO!= null ){
                    if (codPLT_gVO.equals(codPLT) ){
                        encontrado = true;
                        res = gVO;
                    } else i++;
                } else i++;
            }
        } else if (m_Log.isDebugEnabled()) m_Log.debug("buscarDocumentoPlantilla: listaPlantillas nulo.");

        return res;
    }


    public String duplicarPlantilla(AdaptadorSQLBD oad, Connection c, DefinicionTramitesValueObject dTVO, String codigoNuevoProc, Vector plantillasProcedimiento) {
        ResultSet rsCodPLT = null;
        ResultSet rsGetPLT = null;
        PreparedStatement pstmGetPLT = null;
        PreparedStatement pstmInsPLT = null;
        String sqlCodPLT = "select " + oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{
                oad.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[]{aplt_cod}) + "+1", "1"}) +
                " as codigo FROM A_PLT ";
        String sqlInsPLT = "INSERT INTO A_PLT("+aplt_doc+","+aplt_cod+","+aplt_des+", "+aplt_apl+", "+aplt_pro+", "+aplt_tra+", "+aplt_int+",PLT_REL,PLT_EDITOR_TEXTO) VALUES (?,?,?,?,?,?,?,?,?)";

        String codPLT ="";

        try {


            int codPLTModelo = Integer.parseInt(dTVO.getCodPlantilla());
            if (m_Log.isDebugEnabled()) m_Log.debug("duplicarPlantilla: plantilla que se quiere copiar: " + codPLTModelo);

            GeneralValueObject plantillaVO = (GeneralValueObject)  buscarDocumentoPlantilla(dTVO.getCodPlantilla(),plantillasProcedimiento);
            if (plantillaVO != null){
                // datos
                byte[] bytesDocumento = (byte[])plantillaVO.getAtributo("documento");
                String codigo = dTVO.getCodPlantilla();
                String nombre = (String) plantillaVO.getAtributo("descDocumento");
                String aplicacion = "4";
                String tramite = (String) plantillaVO.getAtributo("codTramite");
                String interesado = (String) plantillaVO.getAtributo("interesado");
                String relacion = (String) plantillaVO.getAtributo("relacion");
                m_Log.debug("relacion -->"+relacion);
                String editotTexto = (String) plantillaVO.getAtributo("editotTexto");

                //if(m_Log.isDebugEnabled()) m_Log.debug("DefinicioTramitesDAO.duplicarPlantilla: plantilla " + codPLTModelo+ " encontrada.");

                pstmGetPLT = c.prepareStatement(sqlCodPLT);
                if(m_Log.isDebugEnabled()) m_Log.debug(sqlCodPLT);
                rsCodPLT = pstmGetPLT.executeQuery();
                if (rsCodPLT.next()){

                    codPLT = rsCodPLT.getString("codigo");

                    //if(m_Log.isDebugEnabled()) m_Log.debug("DefinicioTramitesDAO.duplicarPlantilla: codigo nueva plantilla " + codPLT);
                    if (m_Log.isDebugEnabled()) m_Log.debug("duplicarPlantilla: codigo nueva plantilla " + codPLT);
                    pstmGetPLT.close();
                    pstmInsPLT = c.prepareStatement(sqlInsPLT);
                      if(m_Log.isDebugEnabled()) m_Log.debug(sqlInsPLT);
                    java.io.InputStream plantilla = new java.io.ByteArrayInputStream(bytesDocumento);
                    pstmInsPLT.setBinaryStream(1,plantilla,bytesDocumento.length);
                    pstmInsPLT.setString(2,codPLT);
                    pstmInsPLT.setString(3,nombre);
                    pstmInsPLT.setString(4,"4");
                    pstmInsPLT.setString(5,codigoNuevoProc);
                    pstmInsPLT.setString(6,tramite);
                    pstmInsPLT.setString(7,interesado);
                    pstmInsPLT.setString(8,relacion);
                    pstmInsPLT.setString(9,editotTexto);

                    int resultado = pstmInsPLT.executeUpdate();
                    pstmInsPLT.close();
                    if (resultado == 1){
                        if (m_Log.isDebugEnabled()) m_Log.debug("duplicarPlantilla: plantilla " + nombre+ " duplicada.");
                    }
                    else {
                        if (m_Log.isDebugEnabled()) m_Log.debug("duplicarPlantilla: plantilla " + nombre+ " no duplicada.");
                        codPLT = null;
                    }

                } else {
                    if (m_Log.isDebugEnabled()) m_Log.debug("duplicarPlantilla: maximo codigo de plantilla no encontrado.");
                    pstmGetPLT.close();
                    codPLT = null;
                }
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("duplicarPlantilla: plantilla " + codPLTModelo+ " no encontrada.");
                codPLT = null;
            }
            rsCodPLT.close();
            rsGetPLT.close();
        }catch (SQLException e){
            if (m_Log.isDebugEnabled()) m_Log.debug(e.getMessage());
            codPLT = null;
        } finally {
            return codPLT;
        }
    }

/* Se prodria eliminar este metodo si */
    public int insertPlantillasDocumentosDuplicar(DefinicionTramitesValueObject dTVO,String[] params) throws TechnicalException,BDException,AnotacionRegistroException{
        String sql= "";
        int res	= 0;
        String codMunic = "";
        String codProc = "";
        AdaptadorSQLBD abd = null;
        Connection conexion	= null;
        ResultSet rs = null;
        PreparedStatement ps = null;


        Vector listaPlantillas = new Vector();
        listaPlantillas = dTVO.getListaPlantillas();
        if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de plantillas es : " + listaPlantillas.size());
        for(int i=0;i<listaPlantillas.size();i++) {

            try{

                if(m_Log.isDebugEnabled()) m_Log.debug("A por el OAD");
                abd =	new AdaptadorSQLBD(params);
                if(m_Log.isDebugEnabled()) m_Log.debug("A por la conexion");
                conexion = abd.getConnection();
                abd.inicioTransaccion(conexion);

                GeneralValueObject gVO = new GeneralValueObject();
                gVO = (GeneralValueObject) listaPlantillas.elementAt(i);
                byte[] documento = (byte[])gVO.getAtributo("documento");
                sql = "INSERT INTO A_PLT (" + aplt_cod + "," + aplt_des + "," + aplt_apl +
                        "," + aplt_pro + "," + aplt_tra + "," + aplt_int + "," + aplt_doc + ") VALUES ("+
                        "(select " + abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{
                abd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[]{"PLT_COD"}) + "+1", "1"}) +
                        " FROM A_PLT)" + ",'" + (String)gVO.getAtributo("descDocumento") +
                        "',4,'" + dTVO.getTxtCodigo() + "'," + (String)gVO.getAtributo("codTramite") + ",'" +
                        (String)gVO.getAtributo("interesado") + "',?)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                java.io.InputStream ist = new java.io.ByteArrayInputStream(documento);
                ps.setBinaryStream(1,ist,documento.length);
                res = ps.executeUpdate();
                ist.close();
                ps.close();


                abd.finTransaccion(conexion);
            }catch (Exception ex)	{
                abd.rollBack(conexion);
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            }finally {
                if (conexion !=	null)
                    try{
                        abd.devolverConexion(conexion);
                    } catch(Exception ex)	{
                        ex.printStackTrace();
                        if(m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                    }
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("el entero que devuelve el insert es	: " +	res);
        return res;
    }


    public Vector getListaPlantillas(DefinicionTramitesValueObject dTVO, String[] params) throws AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaPlantillas");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector list =	new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            sql = "	SELECT " + m_ConfigTechnical.getString("SQL.A_PLT.codigo") 
                    +	", " + m_ConfigTechnical.getString("SQL.A_PLT.descripcion")	
                    +	" FROM A_PLT WHERE "
                    +	m_ConfigTechnical.getString("SQL.A_PLT.codigoApli") +	"=" +	dTVO.getCodAplicacion()	
                    +	" AND	" + m_ConfigTechnical.getString("SQL.A_PLT.procedimiento") +"='" +	dTVO.getTxtCodigo()+"' " 
                    +	" AND	" + m_ConfigTechnical.getString("SQL.A_PLT.tramite") +"=" +	dTVO.getCodigoTramite() 
                    +	" ORDER BY	" + m_ConfigTechnical.getString("SQL.A_PLT.descripcion"); 

            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getListaPlantillas: Sentencia SQL:" + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                elemListVO = new ElementoListaValueObject(rs.getString(m_ConfigTechnical.getString("SQL.A_PLT.codigo")),rs.getString(m_ConfigTechnical.getString("SQL.A_PLT.descripcion")),orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("DefinicionTramitesDAO, getListaPlantillas:  Lista plantillas cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("DefinicionTramitesDAO, getListaPlantillas: Tamaío lista:"	+ list.size());
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException("Error.DefinicionTramitesDAO.GetListaPlantillas", e);
        }finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                list = null;
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaPlantillas");
        return list;
    }

    /*	Borra	todos	los tramites asociados a un expediente físicamente */
    public int eliminarFisicamente(DefinicionTramitesValueObject defTramVO, Connection conexion, String[]	params ){

        AdaptadorSQL abd = null;
        String sql= "";
        int res	= 0;
        int resTEN = 0;
        int resTCA = 0;
        int resTCAGROUP = 0;
        PreparedStatement ps = null;

        String codMunicipio	= defTramVO.getCodMunicipio();
        String codProcedimiento =	defTramVO.getTxtCodigo();

        try{
            abd =	new AdaptadorSQLBD(params);
            // PLANTILLA DE DOCUMENTOS
            sql = "DELETE FROM A_PLT WHERE " + aplt_pro + "='" + defTramVO.getTxtCodigo() + "'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de A_PLT:	" + res);
            ps.close();
            sql =	"DELETE FROM E_DOT WHERE " +	dot_mun + "=" + defTramVO.getCodMunicipio() + "	AND "	+ dot_pro +	"='" +
                    defTramVO.getTxtCodigo() + "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res =	ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_DOT: " + res);
            ps.close();

            // CONDICIONES DE	ENTRADA
            sql =	"DELETE FROM E_ENT WHERE " + ent_mun + "=" + defTramVO.getCodMunicipio() + " AND " + ent_pro + "='" +
                    defTramVO.getTxtCodigo() + "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res =	ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_ENT:"	+ res);
            ps.close();

            // CONDICIONES DE	ENLACES
            sql =	"DELETE FROM E_TEN WHERE " + ten_mun + "=" + defTramVO.getCodMunicipio() + " AND " + ten_pro +
                    "='" + defTramVO.getTxtCodigo() + "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            resTEN =	ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_TEN:"	+ resTEN);
            ps.close();

            // CONDICIONES DE	CAMPOS
            sql =	"DELETE FROM E_TCA WHERE " + tca_mun + "=" + defTramVO.getCodMunicipio() + " AND " + tca_pro +
                    "='" + defTramVO.getTxtCodigo() + "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            resTCA = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_TCA:"	+ resTCA);
            ps.close();
            
            sql = "DELETE FROM E_TCA_GROUP WHERE TCA_PRO='" + defTramVO.getTxtCodigo() + "' ";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            ps = conexion.prepareStatement(sql);
            resTCAGROUP = ps.executeUpdate();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Filas eliminadas de E_TCA_GROUP : " + resTCAGROUP);
            }
            ps.close();
  
            // VALIDACIONES Y OPERACIONES DE CAMPOS CALCULADOS
            sql ="DELETE FROM EXPRESION_CAMPO_NUM_TRAM WHERE COD_ORGANIZACION =" + defTramVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '"+ 
                    defTramVO.getTxtCodigo() + "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de EXPRESION_CAMPO_NUM_TRAM:"+ resTCA);
            ps.close();
            
            sql ="DELETE FROM EXPRESION_CAMPO_CAL_TRAM WHERE COD_ORGANIZACION =" + defTramVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '"+ 
                    defTramVO.getTxtCodigo() + "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de EXPRESION_CAMPO_CAL_TRAM:"	+ resTCA);
            ps.close();
            // PESTAÑA DE CONDICIONES DE SALIDA
            sql =	"DELETE FROM E_SML WHERE " + sml_mun + "=" + codMunicipio +	" AND	" +
                    sml_pro +	"='" + codProcedimiento	+ "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_SAL: " + res);
            ps.close();

            sql =	"DELETE FROM E_FLS WHERE " + fls_mun + "=" + codMunicipio +	" AND	" +
                    fls_pro +	"='" + codProcedimiento	+ "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_FLS: " + res);
            ps.close();

            sql =	"DELETE FROM E_SAL WHERE " + sal_mun + "=" + codMunicipio +	" AND	" +
                    sal_pro +	"='" + codProcedimiento	+ "' ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_SAL: " + res);
            ps.close();

            //Datos generales
            sql =	"DELETE FROM E_TML WHERE " + tml_mun + "=" + codMunicipio +
                    " AND " +	tml_pro + "='" + codProcedimiento +	"'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res =	ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas de E_TML son: " + res);
            ps.close();

            m_Log.debug("DefinicionTramitesDAO.eliminarFisicamente antes de eliminar en E_TRA_UTR");
            // Se eliminan las unidades tramitadoras asociadas a los trámites de un determinado procedimiento
            UnidadesTramitacionDAO.getInstance().deleteProcedimientoUTRByCodProc(Integer.parseInt(defTramVO.getCodMunicipio()),defTramVO.getTxtCodigo(),conexion);
            m_Log.debug("DefinicionTramitesDAO.eliminarFisicamente después de eliminar en E_TRA_UTR");

            sql =	"DELETE FROM E_TRA WHERE " + tra_mun + "=" + codMunicipio +
                    " AND " +	tra_pro + "='" + codProcedimiento +	"'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("Filas eliminadas en E_TRA: " + res);
            ps.close();

        }catch(Exception ex)	{
            res=-1;
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());

        }finally {
            try{
                if (ps!=null) ps.close();
            } catch(SQLException ex) {
                res =	-1;
                ex.printStackTrace();
                if(m_Log.isErrorEnabled())m_Log.error("BDException: "	+ ex.getMessage()) ;
            }
        }
        return res;
    }

    public int comprobarListasLlenas(DefinicionTramitesValueObject dTVO,String[] params) throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("comprobarListasLlenas");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        String from =	"";
        String where = "";
        int resultado	= 0;
        String tipoCondicion = dTVO.getTipoCondicion();
        String tipoFavorableSI = dTVO.getTipoFavorableSI();
        String tipoFavorableNO = dTVO.getTipoFavorableNO();
        String tC = "";
        String tFSI ="";
        String tFNO =	"";
        if(tipoCondicion !=	null && !"".equals(tipoCondicion))
            tC = tipoCondicion.substring(0,1);
        if(tipoFavorableSI != null && !"".equals(tipoFavorableSI))
            tFSI = tipoFavorableSI.substring(0,1);
        if(tipoFavorableNO != null && !"".equals(tipoFavorableNO))
            tFNO = tipoFavorableNO.substring(0,1);

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();
            from = tra_cou;
            if("T".equals(tC)) {
                where = fls_pro	+ "='" + dTVO.getTxtCodigo() + "' AND " +	fls_tra + "=" +
                        dTVO.getCodigoTramite() +	" AND	" + fls_mun	+ "="	+ dTVO.getCodMunicipio() + " AND " +
                        fls_nuc	+ "=0" + " AND " + tra_fba + " IS NULL";
            }
            if("T".equals(tFSI)) {
                where = fls_pro	+ "='" + dTVO.getTxtCodigo() + "' AND " +	fls_tra + "=" +
                        dTVO.getCodigoTramite() +	" AND	" + fls_mun	+ "="	+ dTVO.getCodMunicipio() + " AND " +
                        fls_nuc	+ "=1" + " AND " + tra_fba + " IS NULL";
            }
            if("T".equals(tFNO)) {
                where = fls_pro	+ "='" + dTVO.getTxtCodigo() + "' AND " +	fls_tra + "=" +
                        dTVO.getCodigoTramite() +	" AND	" + fls_mun	+ "="	+ dTVO.getCodMunicipio() + " AND " +
                        fls_nuc	+ "=2" + " AND " + tra_fba + " IS NULL";
            }

            String	join[] = new String[8];
            join[0] = "E_FLS";
            join[1] = "INNER";
            join[2] = "e_tra";
            join[3] = "e_fls."	+ fls_mun +	"=e_tra." +	tra_mun + "	AND e_fls."	+ fls_pro +	"=e_tra." +	tra_pro +
                    " AND e_fls." + fls_cts + "=e_tra." + tra_cod;
            join[4] = "INNER";
            join[5] = "e_tml";
            join[6] = "e_fls."	+ fls_mun +	"=e_tml." +	tml_mun + "	AND e_fls."	+ fls_pro +	"=e_tml." +	tml_pro +
                    " AND e_fls." + fls_cts + "=e_tml." + tml_tra +
                    " AND e_tml." + tml_cmp + "='NOM'" +
                    " AND e_tml." + tml_leng +	"='"+idiomaDefecto+"'";
            join[7] = "false";

            sql = oad.join(from,where,join);
            if(m_Log.isDebugEnabled()) m_Log.debug("comprobarListasLlenas " + sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()) {
                resultado ++;
            }

            rs.close();
            st.close();
        }catch (Exception	e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.comprobarListasLlenas"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("comprobarListasLlenas");
        return resultado;
    }

    public Vector getListaEnlaces(DefinicionTramitesValueObject	dTVO, String[] params) throws AnotacionRegistroException, TechnicalException{
        //Queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getListaEnlaces");

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();
        String codProcedemento = dTVO.getTxtCodigo();
        String codTramite = dTVO.getCodigoTramite();
        String codMunicipio= dTVO.getCodMunicipio();

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con =	oad.getConnection();

            sql = "SELECT " + ten_cod + ", " + ten_des + ", " + ten_url + ", " + ten_est +
            " FROM E_TEN WHERE " + ten_mun + " = " + codMunicipio + " AND " + ten_pro + " = '" + codProcedemento +
            "' AND " + ten_tra + " = " + codTramite;
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if(m_Log.isDebugEnabled()) m_Log.debug("SQL_ENLACES: " + sql);
            while(rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codigo",rs.getString(ten_cod));
                gVO.setAtributo("descripcion",rs.getString(ten_des));
                gVO.setAtributo("url",rs.getString(ten_url));
                gVO.setAtributo("estado",rs.getString(ten_est));
                lista.addElement(gVO);
            }

            rs.close();
            st.close();
        }catch (Exception	e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new	AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionTramitesDAO.getListaEnlaces"), e);
        }finally {
            try{
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaEnlaces");
        return lista;
    }
    
    public String[] getInfoPlazosTramite(Connection con, int codMunicipio, String codProcedimiento, int codTramite) throws TechnicalException {
        
        String sqlQuery = "SELECT TRA_GENERARPLZ, TRA_UND, TRA_PLZ FROM E_TRA WHERE TRA_MUN = ? AND TRA_PRO = ? AND TRA_COD = ?";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement(sqlQuery);
            
            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i, codTramite);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                i = 1;
                String[] infoPlazos = new String[3];
                infoPlazos[0] = Boolean.toString(rs.getBoolean(i++));
                infoPlazos[1] = rs.getString(i++);
                infoPlazos[2] = Integer.toString(rs.getInt(i));
                
                return infoPlazos;
            }
            
            return null;
            
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);            
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }



      public String getCodigoVisibleTramite(String codMunicipio,String codProcedimiento,String codTramite, Connection con) throws SQLException {

          String codigo ="";
          Statement st = null;
          ResultSet rs = null;

          String sqlQuery = "SELECT TRA_COU FROM E_TRA,E_TML " +
                                    "WHERE TRA_PRO= TML_PRO " +
                                    "AND TRA_COD=TML_TRA " +
                                    "AND TRA_MUN = TML_MUN " +
                                    "AND TRA_COD=" + codTramite + " AND TRA_PRO='" + codProcedimiento + "' AND TRA_MUN=" + codMunicipio;
        try {

            st  = con.createStatement();
            rs  = st.executeQuery(sqlQuery);
            while(rs.next()){
                codigo = rs.getString("TRA_COU");
            }
          
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return codigo;
    }



    /**
     * Recupera la definición de flujo de salida de un trámite de un determinado procedimiento
     * @param codTramite: Código trámite
     * @param codProcedimiento: Código procedimiento
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la base de datos
     * @return ArrayList<FlujoSalidaTramiteVO>
     */
    public ArrayList<FlujoSalidaTramiteVO> getFlujoSalidaTramiteImportacion(int codTramite,String codProcedimiento,int codMunicipio,Connection con) {
         ArrayList<FlujoSalidaTramiteVO> salida = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         try{
            String sql = "SELECT FLS_NUC,FLS_NUS,FLS_CTS,FLS_TRA FROM E_FLS WHERE FLS_MUN=? AND FLS_PRO=? AND FLS_TRA=?";
            int i=1;

            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,codTramite);

            rs = ps.executeQuery();
            while(rs.next()){
                if(salida==null) salida = new ArrayList<FlujoSalidaTramiteVO>();
                FlujoSalidaTramiteVO flujo = new FlujoSalidaTramiteVO();
                flujo.setCodMunicipio(Integer.toString(codMunicipio));
                flujo.setCodProcedimiento(codProcedimiento);
                flujo.setCodigoTramiteOrigen(Integer.toString(codTramite));
                flujo.setNumeroCondicionSalida(rs.getString("FLS_NUC"));
                flujo.setNumeroSecuencia(rs.getString("FLS_NUS"));
                flujo.setCodigoTramiteDestino(rs.getString("FLS_CTS"));
                salida.add(flujo);
            }

         }catch(SQLException e){
             e.printStackTrace();
         }finally{
             try{
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();
             }catch(Exception e){
                 e.printStackTrace();
             }
         }

         return salida;
    }



    /**
     * REcupera la información de una pantalla externa de trámite si la tiene
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return DefinicionTramitesValueObject o null si el trámite no tiene asociada un plugin de pantalla de tramitación externa
     */
    public DefinicionTramitesValueObject getInfoPantallaExternaTramite(String codOrganizacion,String codTramite,String codProcedimiento,Connection con){
        Statement st = null;
        ResultSet rs = null;
        DefinicionTramitesValueObject dfvo = null;

        try{
            String sql = "SELECT TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN " +
                         "FROM E_TRA WHERE TRA_COD=" + codTramite + " AND TRA_PRO='" + codProcedimiento + "' AND TRA_MUN=" + codOrganizacion;
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            String NOMBRE_PLUGIN = null;
            String URL_PLUGIN    = null;
            String IMPL_CLASS    =  null;
            String ID_ENLACE_PLUGIN =  null;

            while(rs.next()){
                NOMBRE_PLUGIN    = rs.getString("TRA_TRAM_EXT_PLUGIN");
                URL_PLUGIN       = rs.getString("TRA_TRAM_EXT_URL");
                IMPL_CLASS       =  rs.getString("TRA_TRAM_EXT_IMPLCLASS");
                ID_ENLACE_PLUGIN =  rs.getString("TRA_TRAM_ID_ENLACE_PLUGIN");
                m_Log.debug("NOMBRE_PLUGIN: " + NOMBRE_PLUGIN + ",URL_PLUGIN: " + URL_PLUGIN + ",IMPL_CLASS: " + IMPL_CLASS + ",ID_ENLACE_PLUGIN: " + ID_ENLACE_PLUGIN);

                if(NOMBRE_PLUGIN!=null && !"".equals(NOMBRE_PLUGIN) && URL_PLUGIN!=null && !"".equals(URL_PLUGIN) && IMPL_CLASS!=null && !"".equals(IMPL_CLASS) && ID_ENLACE_PLUGIN!=null && !"".equals(ID_ENLACE_PLUGIN)){
                    dfvo = new DefinicionTramitesValueObject();
                    dfvo.setCodPluginPantallaTramitacionExterna(NOMBRE_PLUGIN);
                    dfvo.setUrlPluginPantallaTramitacionExterna(URL_PLUGIN);
                    dfvo.setImplClassPluginPantallaTramitacionExterna(IMPL_CLASS);
                    dfvo.setCodUrlPluginPantallaTramitacionExterna(ID_ENLACE_PLUGIN);
                }
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

        return dfvo;

    }// getInfoPantallaExternaTramite

    
    
        
    public DefinicionTramitesValueObject getInfoNotificacionElectronicaTramite(int codTramite,String codProcedimiento,String codMunicipio,Connection con){
        
        DefinicionTramitesValueObject defVO = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT TRA_NOTIFICACION_ELECTRONICA,TRA_NOTIF_FIRMA_CERT_ORG,TRA_NOTIF_ELECT_OBLIG,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA " + 
                          "FROM E_TRA WHERE TRA_PRO='" + codProcedimiento + "' AND TRA_COD=" + codTramite + " AND TRA_MUN=" + codMunicipio;
            
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                defVO = new DefinicionTramitesValueObject();
                
                String admiteNotif = rs.getString("TRA_NOTIFICACION_ELECTRONICA");
                defVO.setAdmiteNotificacionElectronica("NO");
                if(admiteNotif!=null && "1".equals(admiteNotif))
                    defVO.setAdmiteNotificacionElectronica("SI");
                
                int certOrganismo = rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG");
                defVO.setCertificadoOrganismoFirmaNotificacion(false);
                if(certOrganismo==1)
                    defVO.setCertificadoOrganismoFirmaNotificacion(true);    
                
                int certificadoOrganismo = rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG");
                defVO.setCertificadoOrganismoFirmaNotificacion(false);
                if(certificadoOrganismo==1)
                    defVO.setCertificadoOrganismoFirmaNotificacion(true);
                                    
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
        
        return defVO;
    }



    /**
     * Recupera los departamentos utilizados para dar de alta una notificación en sistema de notificación electrónica     
     * @param con
     * @return
     */
    public ArrayList<DepartamentoNotificacionSneVO> getDepartamentosNotificacionSNE(Connection con){

        ArrayList<DepartamentoNotificacionSneVO> departs = new ArrayList<DepartamentoNotificacionSneVO>();
        Statement st = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT CODIGO,DESCRIPCION FROM DEPTO_NOTIFICACION_SNE";
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                DepartamentoNotificacionSneVO depto = new DepartamentoNotificacionSneVO();
                depto.setCodigo(rs.getString("CODIGO"));
                depto.setDescripcion(rs.getString("DESCRIPCION"));
                departs.add(depto);

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

        return departs;
    }
    
    
     public void comprobarErrorDepartamentoNotificacion(
        DefinicionTramitesValueObject tramite, Connection conexion) throws SQLException {

            if (tramite.isTramiteNotificado() && StringUtils.isNotNullOrEmpty(tramite.getCodDepartamentoNotificacion())) {
                DepartamentoNotificacionSneVO departamento = getDepartamentoNotificacionSNEPorCodigo(conexion, tramite.getCodDepartamentoNotificacion());
                    if (!StringUtils.isNotNullOrEmpty(departamento.getCodigo()) && !StringUtils.isNotNullOrEmpty(departamento.getDescripcion())) {
                        this.crearDepartamentoNotificacion(tramite.getCodDepartamentoNotificacion(), tramite.getDescripcionDepartamentoNotificacion(), conexion);
                        tramite.setExisteDepartamento(Boolean.FALSE);
                        m_Log.debug(String.format("Se ha insertado el departamento de notificación con código %s y descripción %s", tramite.getCodDepartamentoNotificacion(), tramite.getDescripcionDepartamentoNotificacion()));
                    } else {
                        // en caso de que la descripción no coincida seteamos en el trámite la descripción del sistema en el que estamos importando.
                    tramite.setDescripcionDepartamentoNotificacion(departamento.getDescripcion());
                    }
            }
         }
     
        public void crearDepartamentoNotificacion (String codigo, String descripcion, Connection conexion) throws SQLException {
        
            int res3 = 0;
            String sql = "INSERT INTO DEPTO_NOTIFICACION_SNE (CODIGO, DESCRIPCION ) VALUES ('"+ codigo + "','" + descripcion + "')";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps = conexion.prepareStatement(sql);
            res3 = ps.executeUpdate();
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el insert  de DEPTO_NOTIFICACION_SNE son res3: " + res3);
        }
     
    
    
    
       public DepartamentoNotificacionSneVO getDepartamentoNotificacionSNEPorCodigo(Connection con, String codigo){

        DepartamentoNotificacionSneVO depto = new DepartamentoNotificacionSneVO();
        Statement st = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT CODIGO,DESCRIPCION FROM DEPTO_NOTIFICACION_SNE WHERE CODIGO = '"+codigo+"'";
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                
                depto.setCodigo(rs.getString("CODIGO"));
                depto.setDescripcion(rs.getString("DESCRIPCION"));
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

        return depto;
    }
	
	/**
	 * Obtiene la lista de documentos del trámite
	 * @param defTramVO Objeto con la definición del tramite
	 * @param con Objeto con la conexión a la base de datos
	 * @param st Objeto PreparedStatement
	 * @param rs Objeto ResultSet
	 * @return 
	 * @throws es.altia.util.conexion.BDException 
	 * @throws java.io.IOException 
	 * @throws java.sql.SQLException 
	 */
	public Vector obtenerDocumentos(DefinicionTramitesValueObject defTramVO, Connection con, 
			PreparedStatement st, ResultSet rs) throws BDException, SQLException, IOException { 
		Vector listaDocumentos = null;
		
		rs = ejecutarSqlObtenerDocumentos(defTramVO, con, st);
		listaDocumentos = parsearResultSetObtenerDocumentos(defTramVO, rs);
		
		
		return listaDocumentos;
	}
	
    /**
     * Se recuperan los datos necesarios sobre el procedimiento de la base de
     * datos acorde a los criterios imputados.
     * @param oad
     * @param defTramVO
     * @param con
     * @param st
     * @return
     * @throws BDException
     * @throws SQLException 
     */
    private ResultSet ejecutarSqlObtenerDocumentos(DefinicionTramitesValueObject defTramVO, Connection con,
            PreparedStatement st) throws BDException, SQLException {
        String E_DOT = "e_dot";
        SqlBuilder sql = new SqlBuilder();
        // SELECT
        sql.select(SqlBuilder.columnaConTabla(E_DOT, dot_cod), SqlBuilder.columnaConAlias(plt_des, "nombreDocumento"),
                SqlBuilder.columnaConTabla(E_DOT, dot_tdo), SqlBuilder.columnaConTabla(E_DOT, dot_vis),
                SqlBuilder.columnaConTabla(E_DOT, dot_frm), SqlBuilder.columnaConAlias(plt_des, "plantilla"),
                SqlBuilder.columnaConTabla(E_DOT, dot_plt), aplt_int, SqlBuilder.columnaConTabla(E_DOT, dot_activo),
                aplt_rel, aplt_doc, "PLT_EDITOR_TEXTO", "PLT_VIS_EXT")
                // FROM AND JOIN
                .from(E_DOT).innerEquiJoin("A_PLT", dot_plt, aplt_cod)
                .andEquals(dot_pro, aplt_pro)
                .andEquals(dot_tra, aplt_tra)
                // WHERE
                .whereEqualsParametrizado(SqlBuilder.columnaConTabla(E_DOT, dot_mun))
                .andEqualsParametrizado(SqlBuilder.columnaConTabla(E_DOT, dot_pro))
                .andEqualsParametrizado(SqlBuilder.columnaConTabla(E_DOT, dot_tra));
      
        st = con.prepareStatement(sql.toString());
        
        JdbcOperations.setValues(st, 1, 
                defTramVO.getCodMunicipio(), defTramVO.getTxtCodigo(),
                defTramVO.getCodigoTramite());
        
        if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_DOT es : " + sql);
        
        return st.executeQuery();
    }
	
	/**
     * Parsea en varios elementos de tipo DefinicionTramitesValueObject los 
     * resultados del ResultSet rs que viene como parï¿½mentro.
     * @param rs
     * @return
     * @throws SQLException
     * @throws IOException 
     */
    private Vector parsearResultSetObtenerDocumentos(DefinicionTramitesValueObject dTVO, ResultSet rs) throws SQLException, IOException {
        Vector listaDocumentos = new Vector<DefinicionTramitesValueObject>();
        // Recuperar e introducir los datos en listaDocumentos mientras hay
        // resultados
        while(rs.next()) {
            DefinicionTramitesValueObject docVO = new DefinicionTramitesValueObject();
			// Establecemos los datos de municipio, procecimiento y trámite en el vo del documento
			docVO.setCodMunicipio(dTVO.getCodMunicipio());
			docVO.setTxtCodigo(dTVO.getTxtCodigo());
			docVO.setCodigoTramite(dTVO.getCodigoTramite());
			// Establecemos en el vo del documento los datos en el ResultSet
            setDatosDocumento(docVO, rs);
            listaDocumentos.addElement(docVO);
        }
        return listaDocumentos;
    }
	
    /**
     * Se recupera de base de datos la informaciï¿½n de las firmas un documento definido en el trámite
     * (tablas E_DOT_FIR y FIRMA_FLUJO)
     * @param oad
     * @param defTramVO
     * @param con
     * @param st
     * @return
     * @throws BDException
     * @throws SQLException 
     */	
    private void obtenerDatosFirmasDocumento(DefinicionTramitesValueObject docVO, Connection con) 
			throws BDException, SQLException, IOException {
        PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			rs = ejecutarSqlObtenerDatosFirmas(docVO, con, ps);
			parsearResultSetObtenerDatosFirmas(docVO, con, rs);
		} catch (SQLException sqle){
			m_Log.error("Se ha producido un error al recuperar los datos de firma del documento. " + sqle.getMessage());
			throw sqle;
		} catch (NumberFormatException nfe){
			m_Log.error("Se ha producido un error al formatear los datos de la firma del documento. " + nfe.getMessage());
		} catch (IOException ioe){
			throw ioe;
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
			} catch(SQLException e){
				m_Log.error("Se ha producido un error al liberar recursos de base de datos");
			}
		}
    }
    
	
    /**
     * Se recupera de base de datos la informaciï¿½n del procedimiento necesaria
     * acorde a los criterios imputados.
     * En este caso se incluye la informaciï¿½n de los flujos de firmas de los 
     * documentos de los trï¿½mites. (tablas E_DOT_FIR, FIRMA_FLUJO y FIRMA_CIRCUITO)
     * @param oad
     * @param defTramVO
     * @param con
     * @param st
     * @return
     * @throws BDException
     * @throws SQLException 
     */
    private ResultSet ejecutarSqlObtenerDatosFirmas(DefinicionTramitesValueObject docVO, Connection con, PreparedStatement st) throws BDException, SQLException {
        SqlBuilder sql = new SqlBuilder();
		
		String aliasUsuFirmaDoc = SqlBuilder.tablaConAlias(SqlBuilder.columnaConTabla(GlobalNames.ESQUEMA_GENERICO, "A_USU"), 
				"USUFIRMADOC");
        // SELECT
        sql.select(SqlBuilder.columnaConTabla("E_DOT_FIR", "DOT_FLUJO"), 
				SqlBuilder.columnaConAlias(SqlBuilder.columnaConTabla("E_DOT_FIR", "USU_COD"), "USUFIRMADOC_ID"), 
				SqlBuilder.columnaConAlias(SqlBuilder.columnaConTabla("USUFIRMADOC", "USU_LOG"), "USUFIRMADOC_LOG"),
                                SqlBuilder.columnaConAlias(SqlBuilder.columnaConTabla("USUFIRMADOC", "USU_NIF"), "USUFIRMADOC_NIF"),
                SqlBuilder.columnaConTabla("FIRMA_FLUJO", "ID"), SqlBuilder.columnaConTabla("FIRMA_FLUJO", "NOMBRE"),
                SqlBuilder.columnaConTabla("FIRMA_FLUJO", "ID_TIPO"), SqlBuilder.columnaConTabla("FIRMA_FLUJO", "ACTIVO"))
				// FROM
				.from("E_DOT_FIR")
				// LEFT JOIN
				.leftEquiJoin("FIRMA_FLUJO", SqlBuilder.columnaConTabla("E_DOT_FIR", "DOT_FLUJO"), 
						SqlBuilder.columnaConTabla("FIRMA_FLUJO", "ID"))
                // LEFT JOIN
                .leftEquiJoin(aliasUsuFirmaDoc, SqlBuilder.columnaConTabla("E_DOT_FIR", "USU_COD"),
                        SqlBuilder.columnaConTabla("USUFIRMADOC", "USU_COD"))
                // WHERE
                .whereEqualsParametrizado(SqlBuilder.columnaConTabla("E_DOT_FIR", dot_mun))
                .andEqualsParametrizado(SqlBuilder.columnaConTabla("E_DOT_FIR", dot_pro))
                .andEqualsParametrizado(SqlBuilder.columnaConTabla("E_DOT_FIR", dot_tra))
                .andEqualsParametrizado(SqlBuilder.columnaConTabla("E_DOT_FIR", dot_cod));
        st = con.prepareStatement(sql.toString());
        
        JdbcOperations.setValues(st, 1,                 
				docVO.getCodMunicipio(), docVO.getTxtCodigo(),
                docVO.getCodigoTramite(), docVO.getCodigoDoc());
        if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de E_DOT_FIR es : " + sql);
        
        return st.executeQuery();
    }
    
    /**
     * Parsea en varios elementos de tipo DefinicionTramitesValueObjects los 
     * resultados del ResultSet rs que viene como parï¿½mentro.
     * Se recogen tambiï¿½n datos que tienen que ver con los flujos de firmas de 
     * los documentos de los trï¿½mites.
     * @param rs
     * @return
     * @throws SQLException
     * @throws IOException 
     */
    private void parsearResultSetObtenerDatosFirmas(DefinicionTramitesValueObject docVO, Connection con, ResultSet rs) 
			throws SQLException, NumberFormatException, IOException {
		FirmaFlujoVO ffVO = null;
		List<FirmaCircuitoVO> listaFirmantes = null;
		int firmaDocumentoIdUsuario = -1;
		String ffIdDotFlujo = null;
		String ffIdFirmaFlujo = null;
		
        while(rs.next()) {
			// USU_COD es -1 en E_DOT_FIR cuando hay un flujo de firmas (DOT_FLUJO)
			firmaDocumentoIdUsuario = rs.getInt("USUFIRMADOC_ID");
			if (firmaDocumentoIdUsuario == -1) {
				ffVO = new FirmaFlujoVO();
				ffIdDotFlujo = rs.getString("DOT_FLUJO");
				ffIdFirmaFlujo = rs.getString("ID");
				if(ffIdFirmaFlujo == null) {// Si la firma tiene asociado un flujo en E_DOT_FIR pero este no existe en FIRMA_FLUJO, 
											// se establece el id a -1 y se considerará que el flujo no es válido
					ffVO.setId(Integer.parseInt(ffIdDotFlujo));
					ffVO.setNombre("-1");		
				} else {
					ffVO.setId(Integer.parseInt(ffIdFirmaFlujo));
					ffVO.setNombre(getClaveString(rs, "NOMBRE"));
					ffVO.setIdTipoFirma(rs.getInt("ID_TIPO"));
					ffVO.setActivo(rs.getBoolean("ACTIVO"));
					
					listaFirmantes = obtenerDatosCircuitosFirmas(ffVO.getId(), con);
					ffVO.setListaFirmasCircuito(listaFirmantes);
				}
			} 
			
			docVO.setFirmaDocumentoIdUsuario(firmaDocumentoIdUsuario);
			docVO.setFirmaDocumentoUsuarioLog(getClaveString(rs, "USUFIRMADOC_LOG"));
                        docVO.setFirmadocumentoDniUsuario(getClaveString(rs, "USUFIRMADOC_NIF"));
			docVO.setFirmaFlujo(ffVO);
		}
    }
	
	/**
     * Se recupera de base de datos la informaciï¿½n de las firmas de un documento definido en el trámite
     * (tabla FIRMA_CIRCUITO)
     * @param oad
     * @param defTramVO
     * @param con
     * @param st
     * @return
     * @throws BDException
     * @throws SQLException 
     */	
    private List<FirmaCircuitoVO> obtenerDatosCircuitosFirmas(Integer flujoId, Connection con) 
			throws SQLException, IOException {
        PreparedStatement ps = null;
		ResultSet rs = null;
		List<FirmaCircuitoVO> listaCircuito = null;
		SqlBuilder sql = new SqlBuilder();
		String aliasUsuFirmante = SqlBuilder.tablaConAlias(SqlBuilder.columnaConTabla(GlobalNames.ESQUEMA_GENERICO, "A_USU"), 
				"USUFIRMANTE");
		
		try {
			// Ejecutar consulta para recuperar los circuitos de firmas
			sql.select(SqlBuilder.columnaConAlias(SqlBuilder.columnaConTabla("FIRMA_CIRCUITO", "ID_USUARIO"), "USUFIRMANTE_ID"),
				SqlBuilder.columnaConAlias(SqlBuilder.columnaConTabla("USUFIRMANTE", "USU_LOG"), "USUFIRMANTE_LOG"),
                                SqlBuilder.columnaConAlias(SqlBuilder.columnaConTabla("USUFIRMANTE", "USU_NIF"), "USUFIRMANTE_NIF"),
                SqlBuilder.columnaConTabla("FIRMA_CIRCUITO", "ORDEN"),SqlBuilder.columnaConTabla("FIRMA_FLUJO", "NOMBRE"))
                // FROM AND JOIN
                .from("FIRMA_CIRCUITO")
                // INNER JOIN
                .innerEquiJoin(aliasUsuFirmante, SqlBuilder.columnaConTabla("FIRMA_CIRCUITO", "ID_USUARIO"),
                        SqlBuilder.columnaConTabla("USUFIRMANTE", "USU_COD"))
                .innerEquiJoin("FIRMA_FLUJO", SqlBuilder.columnaConTabla("FIRMA_CIRCUITO", "ID_FIRMA_FLUJO"),
                        SqlBuilder.columnaConTabla("FIRMA_FLUJO", "ID"))
			     // WHERE
                .whereEqualsParametrizado(SqlBuilder.columnaConTabla("FIRMA_CIRCUITO", "ID_FIRMA_FLUJO"));
			
			ps = con.prepareStatement(sql.toString());

			JdbcOperations.setValues(ps, 1, flujoId);
			if(m_Log.isDebugEnabled()) m_Log.debug("la consulta de FIRMA_CIRCUITO es : " + sql);
		
			// Parsear ResultSet con los resultados de circuitos de firmas
			rs = ps.executeQuery();
			listaCircuito = new ArrayList<FirmaCircuitoVO>();
			while(rs.next()) {
				FirmaCircuitoVO fcVO = new FirmaCircuitoVO();
				fcVO.setIdFlujoFirma(flujoId);
				fcVO.setNombreFlujoFirma(getClaveString(rs, "NOMBRE"));
				fcVO.setIdUsuario(rs.getInt("USUFIRMANTE_ID"));
				fcVO.setLogUsuario(getClaveString(rs, "USUFIRMANTE_LOG"));
                                fcVO.setDniUsuario(getClaveString(rs, "USUFIRMANTE_NIF"));
				fcVO.setOrden(rs.getInt("ORDEN"));
				listaCircuito.add(fcVO);
			}
		} catch (SQLException sqle){
			m_Log.error("Se ha producido un error al recuperar los datos de circuito de firma de los flujos de firma del documento. " + sqle.getMessage());
			throw sqle;
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
			} catch(SQLException e){
				m_Log.error("Se ha producido un error al liberar recursos de base de datos");
			}
		}
		return listaCircuito;
    }

    private String getClaveString(ResultSet rs, String clave) throws SQLException {
        String resultado = rs.getString(clave);
        return (resultado != null) ? resultado : "";
    }
    
    /**
     * Introduce los datos del documento no pertinentes al flujo de firmas en el
     * dTVO a partir del ResultSet rs introducido.
     * @param dTVO
     * @param rs
     * @throws SQLException
     * @throws IOException 
     */
    private void setDatosDocumento(DefinicionTramitesValueObject dTVO, ResultSet rs) 
            throws SQLException, IOException {
        dTVO.setCodigoDoc(getClaveString(rs, dot_cod));
        dTVO.setNombreDoc(getClaveString(rs, "nombreDocumento"));
        dTVO.setCodTipoDoc(getClaveString(rs, dot_tdo));
        dTVO.setVisibleInternet(getClaveString(rs, dot_vis));
        dTVO.setFirma(getClaveString(rs, dot_frm));
        dTVO.setPlantilla(getClaveString(rs, "plantilla"));
        dTVO.setCodPlantilla(getClaveString(rs, dot_plt));

        java.io.InputStream ist = rs.getBinaryStream(aplt_doc);
        if(ist!=null){
            java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
            int c;
            while ((c = ist.read())!= -1){
                ot.write(c);
            }
            ot.flush();
            byte[] r = Base64.encodeBase64(ot.toByteArray());
            ot.close();
            ist.close();
            dTVO.setContidoPlantilla(new String(r, "UTF-8"));
        }
        dTVO.setInteresado(getClaveString(rs, aplt_int));
        dTVO.setDocActivo(getClaveString(rs, dot_activo));
        dTVO.setRelacion(getClaveString(rs, aplt_rel));
        dTVO.setEditorTexto(getClaveString(rs, "PLT_EDITOR_TEXTO"));
        dTVO.setVisibleExt(getClaveString(rs, "PLT_VIS_EXT"));
        
    }
    
    private void comprobarFirmaDocumentos(DefinicionTramitesValueObject defTramVO, Connection con) throws BDException, SQLException, IOException{
        List<DefinicionTramitesValueObject> listaDocumentos = defTramVO.getListaDocumentosImportar();
        for(DefinicionTramitesValueObject documento : listaDocumentos){
            // El valor en E_DOT (DOT_FRM) puede ser:
            // - id de usuario: el documento tiene firma de usuario
            // - -1: el documento tiene una firma de flujo. Tablas FIRMA_FLUJO y FIRMA_CIRCUITO
            // - null o vacio: el documento no tiene firma, ni de usuario ni de flujo					
            if(documento.getFirma() != null && !documento.getFirma().isEmpty()) { 
                documento.setCodMunicipio(defTramVO.getCodMunicipio());
                documento.setTxtCodigo(defTramVO.getTxtCodigo());
                documento.setCodigoTramite(documento.getNumeroTramite());
                
                obtenerDatosFirmasDocumento(documento, con);
            } else {
                documento.setFirmaDocumentoIdUsuario(null);
                documento.setFirmaDocumentoUsuarioLog(null);
                documento.setFirmaFlujo(null);
                documento.setListaFirmasCircuito(null);
            }
        }
    }
    
    private void insertarInfoFirmaDocumentos(DefinicionTramitesValueObject defTramite, Connection con) throws SQLException{
        PreparedStatement ps = null;
        String query = null;
        
        try{
            for(DefinicionTramitesValueObject documento : (List<DefinicionTramitesValueObject>)defTramite.getListaDocumentosImportar()){
                if(documento.getFirma() != null && !documento.getFirma().isEmpty()){
                    query = "INSERT INTO E_DOT_FIR(DOT_MUN, DOT_PRO, DOT_TRA, DOT_COD, USU_COD, DOT_FLUJO) VALUES (?, ?, ?, ?, ?, ?)"; 
                    m_Log.debug("Query de inserción en E_DOT_FIR: " + query);
                    m_Log.debug(String.format("Parámetros de la query: %s - %s - %s - %s - %d - %s",
                            defTramite.getCodMunicipio(), defTramite.getTxtCodigo(), documento.getNumeroTramite(), documento.getCodigoDoc(), documento.getFirmaDocumentoIdUsuario(), 
                            documento.getFirmaFlujo() != null ? String.valueOf(documento.getFirmaFlujo().getId()) : "NULL"));
                    
                    ps = con.prepareStatement(query);
                    
                    int contbd = 1;
                    ps.setString(contbd++, defTramite.getCodMunicipio());
                    ps.setString(contbd++, defTramite.getTxtCodigo());
                    ps.setString(contbd++, documento.getNumeroTramite());
                    ps.setString(contbd++, documento.getCodigoDoc());
                    ps.setInt(contbd++, documento.getFirmaDocumentoIdUsuario());
                    if(documento.getFirmaFlujo() != null) {
                        ps.setInt(contbd, documento.getFirmaFlujo().getId());
                    } else {
                        ps.setNull(contbd, java.sql.Types.NUMERIC);
                    }
                    
                    ps.executeUpdate();                    
                }
            }
        } catch(SQLException sqle){ 
            m_Log.error("Ha ocurrido un error al insertar datos de firma de un documento.");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try{
                if(ps != null){
                    ps.close();
                }
            } catch(Exception e){
                m_Log.error("Ha ocurrido un error al cerrar recursos de base de datos.");
            }
        }
        
        
    }
}