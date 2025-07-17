package es.altia.agora.business.sge.persistence.manual;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.administracion.exception.GestionException;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.persistence.manual.CalendarioGeneralDAO;
import es.altia.agora.business.integracionsw.PeticionSWVO;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.integracionsw.exception.FaltaDatoObligatorioException;
import es.altia.agora.business.integracionsw.exception.NoServicioWebDefinidoException;
import es.altia.agora.business.integracionsw.procesos.GestorEjecucionTramitacion;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class TramitesExpedienteDAO {

    private static final String TIPO_PLAZO_DIAS_HABILES = "H";
    private static final String TIPO_PLAZO_DIAS_NATURALES = "N";
    private static final String TIPO_PLAZO_MESES = "M";
    // Tabla e_cro

    protected static Config m_ConfigTechnical;   //Para el fichero de configuracion tecnico.

    // protected static Config m_ConfigError; //Para los mensajes de error localizados.
    // protected static Log m_Log;   //Para informacion de logs.

    protected static Log m_Log =
            LogFactory.getLog(TramitesExpedienteDAO.class.getName());

    protected static String idiomaDefecto;
    protected static String url;
    protected static String usuario;
    protected static String password;
    protected static String driverClass;


    protected static String cro_mun;
    protected static String cro_pro;
    protected static String cro_tra;
    protected static String cro_eje;
    protected static String cro_num;
    protected static String cro_usu;
    protected static String cro_fei;
    protected static String cro_fef;
    protected static String cro_fip;
    protected static String cro_ffp;
    protected static String cro_flim;
    protected static String cro_utr;
    protected static String cro_uin;
    protected static String cro_ocu;
    protected static String cro_res;
    protected static String cro_obs;
    protected static String cro_usf;

  protected static String cro_rel_mun;
  protected static String cro_rel_pro;
  protected static String cro_rel_tra;
  protected static String cro_rel_eje;
  protected static String cro_rel_num;
  protected static String cro_rel_usu;
  protected static String cro_rel_fei;
  protected static String cro_rel_fef;
  protected static String cro_rel_fip;
  protected static String cro_rel_ffp;
  protected static String cro_rel_flim;
  protected static String cro_rel_utr;
  protected static String cro_rel_ocu;
  protected static String cro_rel_res;
  protected static String cro_rel_obs;
  protected static String cro_rel_usf;

    protected static String pro_mun;
    protected static String pro_cod;
    protected static String pro_tri;
    protected static String pro_utr;

    protected static String tra_mun;
    protected static String tra_pro;
    protected static String tra_cod;
    protected static String tra_utr;
    protected static String tra_uin;
    protected static String tra_pre;
    protected static String tra_uti;
    protected static String tra_utf;
    protected static String tra_usi;
    protected static String tra_usf;
    protected static String tra_ini;
    protected static String tra_inf;
  protected static String tra_ws_ob;
  protected static String tra_ws_cod;

    protected static String uor_cod;
    protected static String uor_padre;
    protected static String uor_nombre;
    protected static String uor_tipo;
    protected static String uor_fechaAlta;
    protected static String uor_fechaBaja;
    protected static String uor_estado;
    protected static String uor_codigoVisible;
    protected static String uor_email;

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
    protected static String usu_nif;
    protected static String usu_firmante;
    protected static String usu_email;

    protected static String ter_cod;
    protected static String ter_tid;
    protected static String ter_doc;
    protected static String ter_nom;
    protected static String ter_ap1;
    protected static String ter_pa1;
    protected static String ter_ap2;
    protected static String ter_pa2;
    protected static String ter_noc;
    protected static String ter_nml;
    protected static String ter_tlf;
    protected static String ter_dce;
    protected static String ter_sit;
    protected static String ter_nve;
    protected static String ter_fal;
    protected static String ter_ual;
    protected static String ter_apl;
    protected static String ter_fbj;
    protected static String ter_ubj;

    protected static String sql_uou_uor;
    protected static String sql_uou_usu;
    protected static String sql_uou_org;
    protected static String sql_uou_ent;
  protected static String sql_uou_car;

    protected static String ent_mun;
    protected static String ent_pro;
    protected static String ent_tra;
    protected static String ent_cod;
    protected static String ent_ctr;
    protected static String ent_est;

    protected static String exp_mun;
    protected static String exp_pro;
    protected static String exp_eje;
    protected static String exp_num;
    protected static String exp_uor;
    protected static String exp_tra;
    protected static String exp_tocu;

    protected static String bloq_mun;
    protected static String bloq_pro;
    protected static String bloq_eje;
    protected static String bloq_num;
    protected static String bloq_usu;
    protected static String bloq_tra;
    protected static String bloq_ocu;

  protected static String rel_mun;
  protected static String rel_pro;
  protected static String rel_eje;
  protected static String rel_num;
  protected static String rel_uor;
  protected static String rel_tra;
  protected static String rel_tocu;

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

    protected static String tml_mun;
    protected static String tml_pro;
    protected static String tml_tra;
    protected static String tml_cmp;
    protected static String tml_leng;
    protected static String tml_valor;

    private static TramitesExpedienteDAO instance = null;

    protected TramitesExpedienteDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

        idiomaDefecto =m_ConfigTechnical.getString("idiomaDefecto");
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
        cro_fip = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicioPlazo");
        cro_ffp = m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo");
        cro_flim = m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite");
        cro_res = m_ConfigTechnical.getString("SQL.E_CRO.resolucion");
        cro_obs = m_ConfigTechnical.getString("SQL.E_CRO.observaciones");
        cro_usf = m_ConfigTechnical.getString("SQL.E_CRO.usuarioFinalizacion");

    cro_rel_mun = m_ConfigTechnical.getString("SQL.G_CRO.codMunicipio");
    cro_rel_pro = m_ConfigTechnical.getString("SQL.G_CRO.codProcedimiento");
    cro_rel_tra = m_ConfigTechnical.getString("SQL.G_CRO.codTramite");
    cro_rel_eje = m_ConfigTechnical.getString("SQL.G_CRO.ano");
    cro_rel_num = m_ConfigTechnical.getString("SQL.G_CRO.numero");
    cro_rel_usu = m_ConfigTechnical.getString("SQL.G_CRO.codUsuario");
    cro_rel_fei = m_ConfigTechnical.getString("SQL.G_CRO.fechaInicio");
    cro_rel_fef = m_ConfigTechnical.getString("SQL.G_CRO.fechaFin");
    cro_rel_utr = m_ConfigTechnical.getString("SQL.G_CRO.codUnidadTramitadora");
    cro_rel_ocu = m_ConfigTechnical.getString("SQL.G_CRO.ocurrencia");
    cro_rel_fip = m_ConfigTechnical.getString("SQL.G_CRO.fechaInicioPlazo");
    cro_rel_ffp = m_ConfigTechnical.getString("SQL.G_CRO.fechaFinPlazo");
    cro_rel_flim = m_ConfigTechnical.getString("SQL.G_CRO.fechaLimite");
    cro_rel_res = m_ConfigTechnical.getString("SQL.G_CRO.resolucion");
    cro_rel_obs = m_ConfigTechnical.getString("SQL.G_CRO.observaciones");
    cro_rel_usf = m_ConfigTechnical.getString("SQL.G_CRO.usuarioFinalizacion");

        pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");
        pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
        pro_tri = m_ConfigTechnical.getString("SQL.E_PRO.codTramiteInicio");
        pro_utr = m_ConfigTechnical.getString("SQL.E_PRO.unidad");

        tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
        tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
        tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
        tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");
        tra_uin = m_ConfigTechnical.getString("SQL.E_TRA.unidadInicio");
        tra_pre = m_ConfigTechnical.getString("SQL.E_TRA.tramitePregunta");
        tra_uti = m_ConfigTechnical.getString("SQL.E_TRA.notUnidadTramitIni");
        tra_utf = m_ConfigTechnical.getString("SQL.E_TRA.notUnidadTramitFin");
        tra_usi = m_ConfigTechnical.getString("SQL.E_TRA.notUsuUnidadTramitIni");
        tra_usf = m_ConfigTechnical.getString("SQL.E_TRA.notUsuUnidadTramitFin");
        tra_ini = m_ConfigTechnical.getString("SQL.E_TRA.notInteresadosIni");
        tra_inf = m_ConfigTechnical.getString("SQL.E_TRA.notInteresadosFin");
    tra_ws_ob = m_ConfigTechnical.getString("SQL.E_TRA.wsTramitarTipo");
    tra_ws_cod = m_ConfigTechnical.getString("SQL.E_TRA.wsTramitar");

        uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        uor_padre = m_ConfigTechnical.getString("SQL.A_UOR.padre");
        uor_nombre = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
        uor_tipo = m_ConfigTechnical.getString("SQL.A_UOR.tipo");
        uor_fechaAlta = m_ConfigTechnical.getString("SQL.A_UOR.fechaAlta");
        uor_fechaBaja = m_ConfigTechnical.getString("SQL.A_UOR.fechaBaja");
        uor_estado = m_ConfigTechnical.getString("SQL.A_UOR.estado");
        uor_codigoVisible = m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible");
        uor_email = m_ConfigTechnical.getString("SQL.A_UOR.email");

        usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
        usu_idi = m_ConfigTechnical.getString("SQL.A_USU.idioma");
        usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");
        usu_log = m_ConfigTechnical.getString("SQL.A_USU.login");
        usu_pas = m_ConfigTechnical.getString("SQL.A_USU.password");
        usu_act = m_ConfigTechnical.getString("SQL.A_USU.puedeActualizar");
        usu_con = m_ConfigTechnical.getString("SQL.A_USU.puedeConsultar");
        usu_lis = m_ConfigTechnical.getString("SQL.A_USU.puedeListar");
        usu_teb = m_ConfigTechnical.getString("SQL.A_USU.tiempoMaxBloqueos");
        usu_tdm = m_ConfigTechnical.getString("SQL.A_USU.tipoMenu");
        usu_ico = m_ConfigTechnical.getString("SQL.A_USU.iconos");
        usu_mpr = m_ConfigTechnical.getString("SQL.A_USU.mostrarProcesosRest");
        usu_nif = m_ConfigTechnical.getString("SQL.A_USU.nif");
        usu_firmante = m_ConfigTechnical.getString("SQL.A_USU.firmante");
        usu_email = m_ConfigTechnical.getString("SQL.A_USU.email");

        ter_cod = m_ConfigTechnical.getString("SQL.T_TER.identificador");
        ter_tid = m_ConfigTechnical.getString("SQL.T_TER.tipoDocumento");
        ter_doc = m_ConfigTechnical.getString("SQL.T_TER.documento");
        ter_nom = m_ConfigTechnical.getString("SQL.T_TER.nombre");
        ter_ap1 = m_ConfigTechnical.getString("SQL.T_TER.apellido1");
        ter_pa1 = m_ConfigTechnical.getString("SQL.T_TER.partApellido1");
        ter_ap2 = m_ConfigTechnical.getString("SQL.T_TER.apellido2");
        ter_pa2 = m_ConfigTechnical.getString("SQL.T_TER.partApellido2");
        ter_noc = m_ConfigTechnical.getString("SQL.T_TER.nombreCompleto");
        ter_nml = m_ConfigTechnical.getString("SQL.T_TER.normalizado");
        ter_tlf = m_ConfigTechnical.getString("SQL.T_TER.telefono");
        ter_dce = m_ConfigTechnical.getString("SQL.T_TER.email");
        ter_sit = m_ConfigTechnical.getString("SQL.T_TER.situacion");
        ter_nve = m_ConfigTechnical.getString("SQL.T_TER.version");
        ter_fal = m_ConfigTechnical.getString("SQL.T_TER.fechaAlta");
        ter_ual = m_ConfigTechnical.getString("SQL.T_TER.usuarioAlta");
        ter_apl = m_ConfigTechnical.getString("SQL.T_TER.moduloAlta");
        ter_fbj = m_ConfigTechnical.getString("SQL.T_TER.fechaBaja");
        ter_ubj = m_ConfigTechnical.getString("SQL.T_TER.usuarioBaja");

        sql_uou_uor= m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        sql_uou_usu= m_ConfigTechnical.getString("SQL.A_UOU.usuario");
        sql_uou_org= m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
        sql_uou_ent= m_ConfigTechnical.getString("SQL.A_UOU.entidad");
    sql_uou_car= m_ConfigTechnical.getString("SQL.A_UOU.cargo");

        ent_mun= m_ConfigTechnical.getString("SQL.E_ENT.codMunicipio");
        ent_pro= m_ConfigTechnical.getString("SQL.E_ENT.codProcedimiento");
        ent_tra= m_ConfigTechnical.getString("SQL.E_ENT.codTramite");
        ent_cod= m_ConfigTechnical.getString("SQL.E_ENT.codCondicion");
        ent_ctr= m_ConfigTechnical.getString("SQL.E_ENT.codTramiteCond");
        ent_est= m_ConfigTechnical.getString("SQL.E_ENT.estadoTramiteCond");

        exp_mun= m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        exp_pro= m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
        exp_eje= m_ConfigTechnical.getString("SQL.E_EXP.ano");
        exp_num= m_ConfigTechnical.getString("SQL.E_EXP.numero");
        exp_uor= m_ConfigTechnical.getString("SQL.E_EXP.uor");
        exp_tra= m_ConfigTechnical.getString("SQL.E_EXP.codTramiteUltCerrado");
        exp_tocu= m_ConfigTechnical.getString("SQL.E_EXP.ocuTramiteUltCerrado");

        bloq_mun= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codMunicipio");
        bloq_pro= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.codProcedimiento");
        bloq_eje= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ano");
        bloq_num= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.numero");
        bloq_tra= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.tramite");
        bloq_ocu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.ocurrencia");
        bloq_usu= m_ConfigTechnical.getString("SQL.E_EXP_BLOQ.usuario");

    rel_mun= m_ConfigTechnical.getString("SQL.G_REL.codMunicipio");
    rel_pro= m_ConfigTechnical.getString("SQL.G_REL.codProcedimiento");
    rel_eje= m_ConfigTechnical.getString("SQL.G_REL.ano");
    rel_num= m_ConfigTechnical.getString("SQL.G_REL.numero");
    rel_uor= m_ConfigTechnical.getString("SQL.G_REL.uorInicio");
    rel_tra= m_ConfigTechnical.getString("SQL.G_REL.codTramiteActual");

     

        tml_mun	= m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
        tml_pro	= m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
        tml_tra	= m_ConfigTechnical.getString("SQL.E_TML.codTramite");
        tml_cmp	= m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
        tml_leng 	= m_ConfigTechnical.getString("SQL.E_TML.idioma");
        tml_valor 	= m_ConfigTechnical.getString("SQL.E_TML.valor");

    }

    public static TramitesExpedienteDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(TramitesExpedienteDAO.class){
                if (instance == null)
                    instance = new TramitesExpedienteDAO();
            }
        }
        return instance;
    }


   

    public int finalizarTramiteNoConvencional(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException,TechnicalException, BDException  {
            int result = actualizarTramiteNoConvencional(oad, con, gVO, true);

            return result;
    }

   

    public int actualizarTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        if (TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0)
            return actualizarTramite(oad, con, gVO,false);
        else return -1;
    }
   public int guardarSitucionTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        int resultado;
        resultado = guardarSitucionTramite(oad, con, gVO);
        return resultado;

    }
    public int guardarSitucionExpediente(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        int resultado;

            resultado = guardarSitucionExpediente(oad, con, gVO, teVO );
            return resultado;

    }
    public int finalizarTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        int resultado;
        if (TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0) {
             resultado = actualizarTramite(oad, con, gVO, true);
           if (resultado>0) {
               // ORIGINAL
               EstructuraNotificacion estructura = getMailsUsuariosAlFinalizar(oad,con,gVO);
               Vector notificacion = new Vector();
               if(estructura!=null) notificacion.add(estructura);
               teVO.setListaEMailsAlFinalizar(notificacion);
                //teVO.getListaEMailsAlFinalizar().addElement(getMailsUsuariosAlFinalizar(oad,con,gVO));
            }
            return resultado;
        } else return -1;
    }

    public int finalizarTramiteNoConvencional(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        int resultado;
        //Como es finalización no convencional, no se tienen en cuenta los permisos de tramitación.
            resultado = finalizarTramiteNoConvencional(oad, con, gVO);
           if (resultado>0) {
                teVO.getListaEMailsAlFinalizar().addElement(getMailsUsuariosAlFinalizar(oad,con,gVO));
            }
            return resultado;
    }
    
     public int cambiarEstadoFirmaDocumentoFinalizado(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        int resultado;
        if (TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0) {
            resultado = cambiarEstadoFirmaDocumentoFin(oad, con, teVO);
            return resultado;
        } else return -1;
    }

    public int finalizarTramiteConResolucion(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO, boolean desFavorable)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        int resultado;
        if (TramitesExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0) {
            gVO.setAtributo("resolucion",(desFavorable?"0":"1"));
            resultado = actualizarTramite(oad, con, gVO, true);
            if (resultado>0) {
                teVO.getListaEMailsAlFinalizar().addElement(getMailsUsuariosAlFinalizar(oad,con,gVO));
            }
            return resultado;
        }
        else return -1;
    }


    public int iniciarTramiteInicio(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException, TechnicalException, BDException{

        PreparedStatement st = null;
        int resultado=0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String codTramite = "";
     

        String sql = " SELECT PRO_TRI, TRA_UTR FROM E_PRO JOIN E_TRA ON (PRO_MUN = TRA_MUN AND PRO_COD = TRA_PRO AND PRO_TRI = TRA_COD) "
                +" WHERE PRO_MUN = ? AND  PRO_COD = ?";


        st = con.prepareStatement(sql);
        st.setInt(1, Integer.parseInt(codMunicipio));
        st.setString(2, codProcedimiento);

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        ResultSet rs = st.executeQuery();
        if (rs.next() ) {
            codTramite = rs.getString("PRO_TRI");
          

            if (codTramite!=null) resultado = 1;
            else resultado=0;
        }
        rs.close();
        st.close();

        if (resultado >0) {           

            gVO.setAtributo("ocurrenciaTramite", Integer.toString(1));
            gVO.setAtributo("codTramite", codTramite);
            resultado = insertarTramite(oad,con,gVO);
            gVO.setAtributo("mailsUsuariosAlIniciar",getMailsUsuariosAlIniciar(oad,con,gVO));

        }
        else resultado = 0;
        return resultado;
    }//iniciarTramiteInicio
    
    public void ejecutarOperacionesAlIniciarTramiteInicio (GeneralValueObject gVO, String[] params) throws TechnicalException{
        if(m_Log.isDebugEnabled()) m_Log.debug("ejecutarOperacionesAlIniciarTramiteInicio() : BEGIN");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement st = null;
        int resultado = 0;
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String codTramite = "";

            String sql = " SELECT PRO_TRI, TRA_UTR FROM E_PRO JOIN E_TRA ON (PRO_MUN = TRA_MUN AND PRO_COD = TRA_PRO AND PRO_TRI = TRA_COD) "
                    +" WHERE PRO_MUN = ? AND  PRO_COD = ?";

            st = con.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(codMunicipio));
            st.setString(2, codProcedimiento);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next() ) {
                codTramite = rs.getString("PRO_TRI");
                if (codTramite!=null) resultado = 1;
                else resultado=0;
            }//if (rs.next() ) 
            rs.close();
            st.close();

            if (resultado >0) {
                /**** SE EJECUTAN LAS OPERACIONES QUE ESTÉN ASOCIADOS AL INICIO DEL TRÁMITE.
                **** SI FALLAN, ENTONCES SE GUARDAN COMO TAREAS DE INICIO PENDIENTES DE EJECUCIÓN Y NO SE PODRÁ FINALIZAR EL TRÁMITE
                **** MIENTRAS NO SE HAYAN EJECUTADO.
                ****/
                int ejercicio   = Integer.parseInt((String)gVO.getAtributo("ejercicio"));
                String numExpediente = (String)gVO.getAtributo("numero");
                int codMun = Integer.parseInt((String)gVO.getAtributo("codMunicipio"));
                int ocurrencia = 1;

                try {
                    AdaptadorSQLBD abd = new AdaptadorSQLBD(params);

                    PeticionSWVO peticionSW = new PeticionSWVO(codMun, codProcedimiento,
                            Integer.parseInt(codTramite), false, false,true, numExpediente, ocurrencia, ejercicio, false,params);
                    peticionSW.setOrigenLlamada((String) gVO.getAtributoONulo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO));
                    GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                    launcher.ejecutarSWTramitacionOperacionesInicio(peticionSW,abd, con);
                }catch (NoServicioWebDefinidoException e) {
                    m_Log.error("NO HAY NINGUN SERVICIO WEB DEFINIDO PARA LANZAR AL FINALIZAR ESTE TRAMITE");
                }catch (EjecucionSWException fdoe) { 
                    m_Log.error("ERROR EN LA EJECUCIÓN DE UNA OPERACION DE UN WEB SERVICE AL INICIAR: " + fdoe.getMensaje());
                }catch (FaltaDatoObligatorioException fdoe) {
                    m_Log.error("EL VALOR DEL CAMPO " + fdoe.getNombreCampo() + " ES OBLIGATORIO PARA LLAMAR AL SERVICIO WEB");
                }catch (EjecucionOperacionModuloIntegracionException eoe){
                    m_Log.error("FALLO EN LA EJECUCIÓN DE LA OPERACIÓN " + eoe.getOperacion() + " DEL MÓDULO " + eoe.getNombreModulo());
                }
                /*************** FIN EJECUCION TAREAS INICIO  ***********************/
                SigpGeneralOperations.commit(oad, con);
            }//if (resultado >0)
        }catch(BDException exception){
            m_Log.error("Se ha producido una excepcion ejecutando las operaciones de inicio del tramite " + exception.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
        }catch(SQLException exception){
            m_Log.error("Se ha producido una excepcion ejecutando las operaciones de inicio del tramite " + exception.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
        }finally{
            if(m_Log.isDebugEnabled()) m_Log.debug("Devolvemos la conexion");
            SigpGeneralOperations.devolverConexion(oad, con);   
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("ejecutarOperacionesAlIniciarTramiteInicio() : END");
    }//ejecutarOperacionesAlIniciarTramiteInicio

    public int iniciarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException, TechnicalException, BDException {

        String ocurrencia ="";
    int resultado = 0;

        m_Log.debug("************ TramitesExpedienteDAO.iniciarTramite init");
        m_Log.debug("************ Llamando a getOcurrenciaTramite codTramite: " + gVO.getAtributo("codTramite"));
        m_Log.debug("************ Llamando a getOcurrenciaTramite codTramite: " + gVO.getAtributo("codTramite"));
        ocurrencia = getOcurrenciaTramite(oad, con , gVO);

        if (!"0".equals(ocurrencia)) {
            gVO.setAtributo("ocurrenciaTramite", ocurrencia);
            resultado = insertarTramite(oad,con,gVO);
            if (resultado > 0) {
                gVO.setAtributo("mailsUsuariosAlIniciar",getMailsUsuariosAlIniciar(oad,con,gVO));
            }
        }
        else resultado = 0;

        return resultado;

    }






    public int iniciarTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO,String codUnidadTramitadoraAnterior)
            throws SQLException,TechnicalException, BDException  {

        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        gVO.setAtributo("codUnidadTramitadoraAnterior",codUnidadTramitadoraAnterior);

        // SE COMPRUEBA SI HAY OPERACIONES QUE SE TIENEN QUE EJECUTAR AL INICIO DEL TRÁMITE
        String[] paramsBD = teVO.getParamsBD();
        AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        PeticionSWVO peticionSW = null;
         try {
                int codMunicipio = Integer.parseInt(teVO.getCodMunicipio());
                int codTramite = Integer.parseInt(teVO.getCodTramite());
                int ocurrencia = Integer.parseInt(teVO.getOcurrenciaTramite());
                int ejercicio = Integer.parseInt(teVO.getEjercicio());
                peticionSW = new PeticionSWVO(codMunicipio, teVO.getCodProcedimiento(),
                        codTramite, false,false,true, teVO.getNumero(), ocurrencia, ejercicio, false, paramsBD);       
                peticionSW.setOrigenLlamada(teVO.getOrigenLlamada());
                GestorEjecucionTramitacion launcher = new GestorEjecucionTramitacion();
                //launcher.ejecutarSWTramitacion(peticionSW,abd, con);
                launcher.ejecutarSWTramitacionOperacionesInicio(peticionSW,abd, con);
                
        } catch (NoServicioWebDefinidoException e) {
            m_Log.error("NO HAY NINGUN SERVICIO WEB DEFINIDO PARA LANZAR AL FINALIZAR ESTE TRAMITE");            
        } catch(EjecucionSWException e){
            e.printStackTrace();
            m_Log.error("ERROR DURANTE LA EJECUCIÓN DE LA OPERACION. SE GUARDA LA TAREA COMO PENDIENTE DE INICIO Y SE DEJA INICIAR EL TRÁMITE");            
        } catch(FaltaDatoObligatorioException e){
            e.printStackTrace();
            m_Log.error("ERROR DURANTE LA EJECUCIÓN DE LA OPERACION: FALTA DATO OBLIGATORIO.SE GUARDA LA TAREA COMO PENDIENTE DE INICIO Y SE DEJA INICIAR EL TRÁMITE");            
        } catch(EjecucionOperacionModuloIntegracionException e){
            e.printStackTrace();
            m_Log.error("ERROR DURANTE LA EJECUCIÓN DE LA OPERACION " + e.getOperacion() + " DE UN MÓDULO EXTERNO " + e.getNombreModulo() + ".SE GUARDA LA TAREA COMO PENDIENTE DE INICIO Y SE DEJA INICIAR EL TRÁMITE");            
        }

        int resultado = iniciarTramite(oad, con, gVO);
        teVO.setOcurrenciaTramite((String) gVO.getAtributo("ocurrenciaTramite"));
        teVO.getListaEMailsAlIniciar().addElement(gVO.getAtributo("mailsUsuariosAlIniciar"));
        teVO.setUnidadTramitadoraTramiteIniciado((String) gVO.getAtributo("nombreUORTramiteIniciado"));
        return resultado;
    }

    /******  ORIGINAL
    public int iniciarTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO,String codUnidadTramitadoraAnterior)
            throws SQLException,TechnicalException, BDException  {

        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        gVO.setAtributo("codUnidadTramitadoraAnterior",codUnidadTramitadoraAnterior);
        int resultado = iniciarTramite(oad, con, gVO);
        teVO.setOcurrenciaTramite((String) gVO.getAtributo("ocurrenciaTramite"));

        teVO.getListaEMailsAlIniciar().addElement(gVO.getAtributo("mailsUsuariosAlIniciar"));
        teVO.setUnidadTramitadoraTramiteIniciado((String) gVO.getAtributo("nombreUORTramiteIniciado"));
        return resultado;
    } *****/

    public int iniciarTramiteManual(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO, String codUnidadInicio)
            throws SQLException,TechnicalException, BDException  {

        m_Log.debug("**************** TramitesExpedienteDAO.iniciarTramiteManual_1 init");
        m_Log.debug("**************** TramitesExpedienteDAO.iniciarTramiteManual_1 codTramite: " + teVO.getCodTramite());
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        m_Log.debug("**************** TramitesExpedienteDAO.iniciarTramiteManual_1 codTramite tras cambio: " + gVO.getAtributo("codTramite"));
        gVO.setAtributo ("codUnidadTramitadoraAnterior", codUnidadInicio);
        int resultado = iniciarTramiteManual(oad, con, gVO);
        teVO.setOcurrenciaTramite((String) gVO.getAtributo("ocurrenciaTramite"));
        teVO.setUnidadTramitadoraTramiteIniciado((String) gVO.getAtributo("nombreUORTramiteIniciado"));
        return resultado;
    }

    public int iniciarTramiteManual(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException, TechnicalException, BDException {

        String ocurrencia ="";
        int resultado=0;

        m_Log.debug("************ TramitesExpedienteDAO.iniciarTramiteManual_2 init");
        m_Log.debug("************ Llamando a getOcurrenciaTramite codTramite: " + gVO.getAtributo("codTramite"));
        ocurrencia = getOcurrenciaTramite(oad, con , gVO);

        if (!"0".equals(ocurrencia)) {
            gVO.setAtributo("ocurrenciaTramite", ocurrencia);
            resultado = insertarTramiteManual(oad,con,gVO);
        }
        else resultado = 0;

        return resultado;

    }

    private int insertarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException, TechnicalException {


        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        int resultado=0;
        String unidadTramiteInicioSeleccionada = (String)gVO.getAtributo("unidadTramiteInicioSeleccionada");
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String ocurrencia = (String) gVO.getAtributo("ocurrenciaTramite");
        String usuario = (String)gVO.getAtributo("usuario");
        String uor = "";
        String codTramite = (String) gVO.getAtributo("codTramite");
        String codUnidadOrganicaExp = (String) gVO.getAtributo("codUnidadOrganicaExp");
        String codUnidadTramitadoraTram = (String) gVO.getAtributo("codUnidadTramitadoraTram");
        if ("_".equals(codUnidadTramitadoraTram)) codUnidadTramitadoraTram = "";
        // Si esta variable contiene un si => Entonces se inserta el código de la unidad tramitadora que se indica
        // en la variable codUnidadTramitadoraTram como unidad de inicio del trámite
        String insertarCodUnidadTramitadoraTram = (String) gVO.getAtributo("insertarCodUnidadTramitadoraTram");
        //if(m_Log.isDebugEnabled()) m_Log.debug("Se inserta el código de la unidad tramitadora : " + insertarCodUnidadTramitadoraTram);

        //if(m_Log.isDebugEnabled()) m_Log.debug("El codigo del trámite : " + codTramite);
        //if(m_Log.isDebugEnabled()) m_Log.debug("El codigo de la unidad organica del expediente al inicio de insertarTramite es : " + codUnidadOrganicaExp);
        //if(m_Log.isDebugEnabled()) m_Log.debug("El codigo de la unidad tramitadora del tramite al inicio de insertarTramite es : " + codUnidadTramitadoraTram);
        String codUnidadTramitadoraAnterior = (String) gVO.getAtributo("codUnidadTramitadoraAnterior");
        //if(m_Log.isDebugEnabled()) m_Log.debug("El codigo de la unidad organica anterior del tramite al inicio de insertarTramite es : " + codUnidadTramitadoraAnterior);
        String codEntidad = (String) gVO.getAtributo("codEntidad");
        String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
        String codUnidadTramitadoraUsu = (String) gVO.getAtributo("codUnidadTramitadoraUsu");
        String bloqueo = (String) gVO.getAtributo("bloqueo");
        String[] infoPlazos = DefinicionTramitesDAO.getInstance().getInfoPlazosTramite(
                con, Integer.parseInt(codMunicipio), codProcedimiento, Integer.parseInt(codTramite));


        String tipoPlazo = null, unidadesPlazo = null;
        Calendar fechaInicioPlazo = null, fechaLimitePlazo = null;
        if (infoPlazos != null) {
            if (infoPlazos.length == 3) {
                if ("true".equals(infoPlazos[0])) {
                    tipoPlazo = infoPlazos[1];
                    unidadesPlazo = infoPlazos[2];
                    fechaInicioPlazo = Calendar.getInstance();
                    fechaLimitePlazo = getFechaLimitePlazo(fechaInicioPlazo, tipoPlazo, Integer.parseInt(unidadesPlazo), con, oad);
                }
            }
        }

        //Primero comprobamos que el trámite es manual.
        String codUnidadTramitadoraManual = (String) gVO.getAtributo("codUnidadTramitadoraManual");
        m_Log.debug("TRAMITE MANUAL (si esta unidad no es vacía el trámite se inicia de modo manual): " + codUnidadTramitadoraManual);
        if(codUnidadTramitadoraManual != null && !"".equals(codUnidadTramitadoraManual)) {

            /*
             * En caso de trámite manual recordar que la unidad de inicio (TRA_UIN) tiene los siguientes valores:
             * Sen unidade = null
             * Calquera = -99998
             * La del expediente = -99999
             * Otra unidad = Código de esa unidad
             */

            if (("-99999".equals(codUnidadTramitadoraManual))) {
                if (codUnidadOrganicaExp != null && !"".equals(codUnidadOrganicaExp)) {
                    uor = codUnidadOrganicaExp;
                } else {
                    sql = "SELECT " + exp_uor + " FROM E_EXP WHERE " + exp_mun + "=" + codMunicipio + " AND " +
                            exp_eje + "=" + ejercicio + " AND " + exp_num + "='" + numero + "'";
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Unidad manual de inicio=la del expediente=" + sql);
                    }
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()) {
                        uor = rs.getString(exp_uor);
                }
                rs.close();
                st.close();
                        }
            } else if (("-99998".equals(codUnidadTramitadoraManual))) {
                        if(codUnidadTramitadoraUsu != null && !"".equals(codUnidadTramitadoraUsu)) {
                            uor = codUnidadTramitadoraUsu;
                        } else {
                            sql = "SELECT " + sql_uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " +
                                    sql_uou_usu + "=" +
                                    usuario + " AND " + sql_uou_org + "=" + codOrganizacion + " AND " +
                                    sql_uou_ent + "=" + codEntidad;
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Unidad manual de inicio=Cualquiera" + sql);
                    }
                            st = con.createStatement();
                            rs = st.executeQuery(sql);
                            if(rs.next()) {
                                uor = rs.getString(sql_uou_uor);
                            }
                            rs.close();
                            st.close();
                        }

            } else {
                uor = codUnidadTramitadoraManual;
            }
        } else { //ENTRA EN ESTE "ELSE" SI EL TRÁMITE NO SE INICIA DE MODO MANUAL
            /*
             * En caso de tramitación normal recordar que la unidad de inicio (TRA_UTR) tiene los siguientes valores:
             * La del trámite anterior = 2
             * La que lo inicia = 3
             * La del expediente = 4
             * Otras unidades = 0
             * Cualquiera = 1
             */
        if(codUnidadTramitadoraTram != null && !"".equals(codUnidadTramitadoraTram)) {
            /*if("2".equals(codUnidadTramitadoraTram)) {
                sql = "SELECT " + trp_uor + " FROM E_TRP WHERE " + trp_mun + "=" + codMunicipio +
                        " AND " + trp_eje + "=" + ejercicio + " AND " + trp_exp + "='" + numero +
                        "' AND " + trp_pro + "='" + codProcedimiento + "' AND " + trp_tra + "=" + codTramite;
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Unidad de inicio de la tramitación normal=la del trámite anterior" + sql);
                    }
                st = con.createStatement();
                rs = st.executeQuery(sql);
                if(rs.next()){
                    uor = rs.getString(trp_uor);
                }
                rs.close();
                st.close();
                if((uor == null) || ("".equals(uor))) {
                    uor = codUnidadTramitadoraAnterior;
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
                        }
                }
            } else if("3".equals(codUnidadTramitadoraTram)) {
                uor = codUnidadTramitadoraTram;
            } else {*/
                  uor = codUnidadTramitadoraTram;
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
                    }
            //}
        } else {
            sql = "SELECT " + tra_utr + " FROM E_TRA WHERE " + tra_mun + "=" + codMunicipio + " AND " +
                    tra_pro + "='" + codProcedimiento + "' AND " + tra_cod + "=" + codTramite;
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Unidad de trmaitación = otra" + sql);
                }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                codUnidadTramitadoraTram = rs.getString(tra_utr);
            }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el codigo de la unidad tramitadora del tramite es ::::::::::::::::::::::::: : " + codUnidadTramitadoraTram);
                }
            rs.close();
            st.close();
            if(codUnidadTramitadoraTram != null && !"".equals(codUnidadTramitadoraTram)) {
                if("2".equals(codUnidadTramitadoraTram)) {
                    sql = "SELECT " + trp_uor + " FROM E_TRP WHERE " + trp_mun + "=" + codMunicipio +
                            " AND " + trp_eje + "=" + ejercicio + " AND " + trp_exp + "='" + numero +
                            "' AND " + trp_pro + "='" + codProcedimiento + "' AND " + trp_tra + "=" + codTramite;
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("Unidad de tramitaicón = la del trámite anterior" + sql);
                        }
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    if(rs.next()){
                        uor = rs.getString(trp_uor);
                    }
                    rs.close();
                    st.close();
                    if((uor == null) || ("".equals(uor))) {
                        uor = codUnidadTramitadoraAnterior;
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("el codigo de la unidad organica de la cronologia es (linea 722):::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
                            }
                    }
                } else if("3".equals(codUnidadTramitadoraTram)) {
                    if(codUnidadTramitadoraUsu != null && !"".equals(codUnidadTramitadoraUsu)) {
                        uor = codUnidadTramitadoraUsu;
                    } else {
                        sql = "SELECT " + sql_uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " +
                                sql_uou_usu + "=" +
                                usuario + " AND " + sql_uou_org + "=" + codOrganizacion + " AND " +
                                sql_uou_ent + "=" + codEntidad;
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("Unidad de tramitación = la que lo inicia" + sql);
                            }
                        st = con.createStatement();
                        rs = st.executeQuery(sql);
                        if(rs.next()) {
                            uor = rs.getString(sql_uou_uor);
                        }
                        rs.close();
                        st.close();
                    }
                }
                else 
                if("4".equals(codUnidadTramitadoraTram)){
                    m_Log.debug("Se va a recuperar el código de la unidad tramitodora");
                    m_Log.debug("Recuperando unidad de inicio del expediente");

                    sql = "SELECT " + exp_uor + " FROM E_EXP WHERE " + exp_mun + "=" + codMunicipio + " AND " +
                           exp_eje + "=" + ejercicio + " AND " + exp_num + "='" + numero + "'";

                    if (m_Log.isDebugEnabled()) {
                          m_Log.debug("*********** Unidad de inicio del expediente" + sql);
                    }

                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    while(rs.next()) {
                        uor = rs.getString(exp_uor);
                    }
                    rs.close();
                    st.close();

                }// if
                else  // si el trámite tiene como unidad de inicio una unidad orgánica seleccionada por el usuario que puede
                      // venir, bien porque en la definición del trámite está marcado como otras o bien como cualquiera
                if("0".equals(codUnidadTramitadoraTram) || "1".equals(codUnidadTramitadoraTram)){
                  m_Log.debug("el codigo de la unidad organica tramitoradora del tramite de inicio es :::::::: : " + codUnidadTramitadoraTram);
                  uor = unidadTramiteInicioSeleccionada;
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("el codigo de la unidad organica de la cronologia es ::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
                  }
                }
            } else {
                if(codUnidadOrganicaExp !=null && !"".equals(codUnidadOrganicaExp)) {
                    uor = codUnidadOrganicaExp;
                } else {
                    sql = "SELECT " + exp_uor + " FROM E_EXP WHERE " + exp_mun + "=" + codMunicipio + " AND " +
                            exp_eje + "=" + ejercicio + " AND " + exp_num + "='" + numero + "'";
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("Unidad de inicio del expediente" + sql);
                        }
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    while(rs.next()) {
                        uor = rs.getString(exp_uor);
                    }
                    rs.close();
                    st.close();
                }
            }
        }
        }

        m_Log.debug(" ********************* TramitesExpedienteDAO.insertarTramite *********************************");
        m_Log.debug(" ********************* codUnidadTramitadoraUsu: " + codUnidadTramitadoraUsu);

        sql = "SELECT " + cro_tra + " FROM E_CRO WHERE " + cro_mun + "=" + codMunicipio +
                " AND " + cro_pro + "='" + codProcedimiento + "' AND " + cro_eje + "=" + ejercicio + " AND " +
                cro_num + "='" + numero + "' AND " + cro_tra + "=" + codTramite + " AND " + cro_ocu + "=" +
                ocurrencia;
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(sql);
        }
        st = con.createStatement();
        rs = st.executeQuery(sql);
        String yaExiste = "no";
        while(rs.next()) {
            yaExiste = "si";
        }
        rs.close();
        st.close();

        if("si".equals(insertarCodUnidadTramitadoraTram))
            uor = codUnidadTramitadoraTram;

        m_Log.debug("******** TramitesExpedienteDAO.insertarTramite yaExiste: " + yaExiste);
        m_Log.debug("******** TramitesExpedienteDAO.insertarTramite antes de insertar en E_CRO uor: " + uor);
        if("no".equals(yaExiste)) {
            String sqlInsert = "INSERT INTO E_CRO (CRO_MUN, CRO_PRO, CRO_EJE, CRO_NUM, CRO_TRA, CRO_OCU, CRO_FEI, CRO_FEF, CRO_USU, CRO_UTR";
            if (fechaInicioPlazo != null) {
                sqlInsert += ", CRO_FIP, CRO_FLI";
            }
            sqlInsert += ") VALUES (" + codMunicipio + ", '" + codProcedimiento + "', " + ejercicio + ", '" + numero + "'"
                      + ", " + codTramite + ", " + ocurrencia + ", ?, NULL, " + usuario + ", " + uor;
            if (fechaInicioPlazo != null) {
                sqlInsert += ", ?, ?";
            }
            sqlInsert += ")";

            if (m_Log.isDebugEnabled()) m_Log.debug("INSERTAR TRAMITE: " + sqlInsert);
            ps = con.prepareStatement(sqlInsert);
            ps.setTimestamp(1, new Timestamp((new java.util.Date()).getTime()));
            if (fechaInicioPlazo != null) {
                ps.setTimestamp(2, new Timestamp(fechaInicioPlazo.getTime().getTime()));
                ps.setTimestamp(3, new Timestamp(fechaLimitePlazo.getTime().getTime()));
            }

            resultado = ps.executeUpdate();
            gVO.setAtributo("codUORTramiteIniciado",uor);

            ps.close();

            UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],uor);	
            if (uorDTO!=null)	
            gVO.setAtributo("nombreUORTramiteIniciado",uorDTO.getUor_nom());
            st.close();

            //Si el trámite anterior estaba bloqueado, controlar si se mantiene el mismo.
            //Si la UOR que abre el trámite pertenece al usuario --> BLOQUEADO
            //Si la UOR que abre el trámite NO pertenece al usuario --> SE PIERDE EL BLOQUEO
            m_Log.debug("------------------------------BLOQUEO : " + bloqueo);
            if (bloqueo != null) {
                if (bloqueo.equals("1")) {
                    boolean ok = false;
                    sql = "SELECT " + sql_uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " +
                            sql_uou_usu + "=" +
                            usuario + " AND " + sql_uou_org + "=" + codOrganizacion + " AND " +
                            sql_uou_ent + "=" + codEntidad + " AND " + sql_uou_uor + "=" + uor;
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(sql);
                    }
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    if(rs.next()) {
                        ok = true;
                    }
                    st.close();

                    if (ok == true) { // Si la UOR pertenece al usuario
                        st = con.createStatement();
                        sql = "INSERT INTO E_EXP_BLOQ  ("
                                + bloq_mun + ", " + bloq_pro + "," + bloq_eje + "," + bloq_num + "," + bloq_tra + "," + bloq_ocu + "," + bloq_usu + ") VALUES ("
                                + codMunicipio + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numero + "'" + "," + codTramite + "," + ocurrencia + ", " + usuario
                                +")";

                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        resultado = st.executeUpdate(sql);
                        st.close();
                    }
                }
            }
        } else {
            resultado = 1;
        }
        return resultado;
    }

    private Calendar getFechaLimitePlazo(Calendar fechaInicioPlazo, String tipoPlazo, int unidadesPlazo, Connection con, AdaptadorSQL oad) throws TechnicalException {

        try {
            Calendar fechaLimitePlazo = Calendar.getInstance();
            fechaLimitePlazo.setTimeInMillis(fechaInicioPlazo.getTimeInMillis());
            if (tipoPlazo.equals(TIPO_PLAZO_DIAS_NATURALES)) {
                fechaLimitePlazo.add(Calendar.DATE, unidadesPlazo);
            } else if (tipoPlazo.equals(TIPO_PLAZO_MESES)) {
                fechaLimitePlazo.add(Calendar.MONTH, unidadesPlazo);
            } else if (tipoPlazo.equals(TIPO_PLAZO_DIAS_HABILES)) {
                Vector diasFestivos = CalendarioGeneralDAO.getInstance().obtenerFestivosPorAno(
                        Integer.toString(fechaLimitePlazo.get(Calendar.YEAR)), con, oad);
                int contadorDias = unidadesPlazo;
                while (contadorDias > 0) {
                    fechaLimitePlazo.add(Calendar.DATE, 1);
                    boolean incremento = true;
                    if (fechaLimitePlazo.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        incremento = false;
                    } else {
                        for (Object objDiaFestivo: diasFestivos) {
                            String diaFestivo = (String)objDiaFestivo;
                            java.util.Date dateFestivo = Fecha.obtenerDate(diaFestivo);
                            if (dateFestivo.equals(fechaLimitePlazo.getTime())) {
                                incremento = false;
                            }
                        }
                    }
                    if (incremento) contadorDias--;
                }
            }
            return fechaLimitePlazo;
        } catch (GestionException ge) {
            throw new TechnicalException(ge.getMessage(), ge);
        }
    }

    private EstructuraNotificacion getMailsUsuariosAlIniciar(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
    {

        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;
        int resultado=0;
        EstructuraNotificacion eNotif = new EstructuraNotificacion();
        try {
            if(m_Log.isDebugEnabled()) m_Log.debug("entra en getMailsUsuariosAlIniciar");

            String codOrganizacion = (String)gVO.getAtributo("codOrganizacion");
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String codTramite = (String) gVO.getAtributo("codTramite");
            String codUORTramiteIniciado = (String) gVO.getAtributo("codUORTramiteIniciado");
            Vector codInteresados = new Vector();
            String codUsuario = (String) gVO.getAtributo("usuario");
            if(m_Log.isDebugEnabled()) m_Log.debug("UOR DEL TRAMITE QUE SE INICIA: " + codUORTramiteIniciado);
            if(m_Log.isDebugEnabled()) m_Log.debug("COD INTERESADOS : " + codInteresados);
            if(m_Log.isDebugEnabled()) m_Log.debug("COD USUARIO : " + codUsuario);
            String uti = "";
            String usi = "";
            String ini = "";
            String uiti = "";
            String uiei = "";
            String uor_mail = "";
            String uor_usu = "";
            String usu_mail = "";
            String int_mail = "";
            String usu_mail_tramite;
            String usu_mail_exped;
            Vector mailsUOR = new Vector();
            Vector mailsUsusUOR = new Vector();
            Vector mailsInteresados = new Vector();
            String mailUsuInicioTramite ="";
            String mailUsuInicioExpediente = "";
            Vector usuarios = new Vector();
            int procedimientoRestringido = 0;

            sql = "SELECT " + tra_uti + "," + tra_usi + "," + tra_ini +  ",TRA_NOTIF_UITI, TRA_NOTIF_UIEI, pro_restringido " +
                    " FROM e_tra, e_pro WHERE " + tra_mun + "=" + codMunicipio + " AND " + tra_pro + "=" + oad.addString(codProcedimiento) +
                    " AND " + tra_cod + "=" +codTramite+" and tra_pro=pro_cod and tra_mun=pro_mun";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()) {
                uti = rs.getString(tra_uti);
                usi = rs.getString(tra_usi);
                ini = rs.getString(tra_ini);
                uiti = rs.getString("TRA_NOTIF_UITI");
                uiei = rs.getString("TRA_NOTIF_UIEI");
                procedimientoRestringido = rs.getInt("pro_restringido");
                resultado = 1;
            }
            rs.close();
            st.close();
            if (resultado > 0) {

                sql = "SELECT " + tml_valor + ",TRA_PLZ, TRA_UND "+
                        " FROM e_tml, e_tra WHERE " + tml_mun + "=" + codMunicipio + " AND " + tml_pro + "=" + oad.addString(codProcedimiento) +
                        " AND " + tml_tra + "=" +codTramite+
                        " AND TRA_PRO=TML_PRO AND TRA_COD=TML_TRA";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()) { 
                    eNotif.setNombreTramite(rs.getString(tml_valor));
                    eNotif.setPlazoTramite(rs.getString("TRA_PLZ"));
                    eNotif.setUnidadPlazo(rs.getString("TRA_UND"));
                }
                rs.close();
                st.close();
                if (uti.equals("S")) {
                    //coger mail de a_uor buscando por uor
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],codUORTramiteIniciado);

                    if (uorDTO!=null && uorDTO.getUor_email()!=null && !uorDTO.getUor_email().equals("")) {
                            uor_mail=uorDTO.getUor_email();
                            if(m_Log.isDebugEnabled()) m_Log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);
                            mailsUOR.addElement(uor_mail);
                       
                    }
                   
                }
                if (usi.equals("S")) {
                    if(procedimientoRestringido==0){
                    //coger usuarios de a_uou buscando por uor
                    sql = "SELECT " + sql_uou_usu + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou WHERE " +
                            sql_uou_uor + "=" + codUORTramiteIniciado + " AND "
                            + sql_uou_org + "=" + codOrganizacion + " AND " + sql_uou_usu + "<>" + codUsuario;
                    }else
                    {
                        // El procedimiento está restringido, entonces se notifica a los usuarios con permisos sobre la 
                        // unidad organizativa, que además, tenga permiso sobre el procedimiento restringido. De este 
                        // modo se evita que le lleguen avisos a un usuario con permiso sobre la uor pero que no tiene
                        // permiso sobre el procedimiento restringido en cuestión 
                         sql = "SELECT " + sql_uou_usu + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou,  " +
                                  GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE " +
                            sql_uou_uor + "=" + codUORTramiteIniciado + " AND "
                            + sql_uou_org + "=" + codOrganizacion + " AND " + sql_uou_usu + "<>" + codUsuario +
                        " AND USUARIO_PROC_RESTRINGIDO.PRO_COD='" + codProcedimiento + "' AND USUARIO_PROC_RESTRINGIDO.ORG_COD=" + codOrganizacion + 
                               " AND USUARIO_PROC_RESTRINGIDO.USU_COD=A_UOU.UOU_USU" ;
                    }
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql); 
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while(rs.next()) {
                        uor_usu = rs.getString(sql_uou_usu);
                        if (!(uor_usu==null) && !(uor_usu.equals(""))) {
                            if(m_Log.isDebugEnabled()) m_Log.debug("CODIGO USUARIO: "+uor_usu);
                            usuarios.addElement(uor_usu);
                        }
                    }
                    rs.close();
                    st.close();
                    //coger mail de a_usu buscando por cod_usu
                    for (int i=0;i<usuarios.size();i++) {
                        sql = "SELECT " + usu_email + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu WHERE " +
                                usu_cod + "=" + usuarios.elementAt(i);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        st = con.prepareStatement(sql);
                        rs = st.executeQuery();
                        while(rs.next()) {
                            usu_mail = rs.getString(usu_email);
                            if (!(usu_mail==null) && !(usu_mail.equals(""))) {
                                if(m_Log.isDebugEnabled()) m_Log.debug("USUARIO MAIL: "+usu_mail);
                                mailsUsusUOR.addElement(usu_mail);
                            }
                        }
                        rs.close();
                        st.close();
                    }
                }
                if (ini.equals("S")) {
                    sql = "SELECT EXT_TER, EXT_NVR FROM E_EXT WHERE EXT_NUM=? AND EXT_MUN=? AND EXT_EJE=?";
                    st = con.prepareStatement(sql);
                    st.setString(1, (String)gVO.getAtributo("numero"));
                    st.setString(2, codMunicipio);
                    st.setString(3, (String)gVO.getAtributo("ejercicio"));

                    m_Log.debug("Consulta de terceros del expediente: " + sql);
                    rs = st.executeQuery();



                    while (rs.next()) {
                        GeneralValueObject res = new GeneralValueObject();
                        res.setAtributo("codigo", rs.getString("EXT_TER"));
                        res.setAtributo("version", rs.getString("EXT_NVR"));
                        codInteresados.add(res);
                    }

                    //coger mail de t_ter buscando por codInteresados
                    if (codInteresados!=null) {
                        for (int i=0;i<codInteresados.size();i++) {
                            GeneralValueObject resVO = (GeneralValueObject) codInteresados.elementAt(i);
                            sql = "SELECT " + ter_dce + " FROM t_ter WHERE " + ter_cod +
                                    "=" + (String) resVO.getAtributo("codigo")+ "AND " + ter_nve +"="+
                                    (String) resVO.getAtributo("version");
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            st = con.prepareStatement(sql);
                            rs = st.executeQuery();
                            while(rs.next()) {
                                int_mail = rs.getString(ter_dce);
                                if (!(int_mail==null) && !(int_mail.equals(""))) {
                                    if(m_Log.isDebugEnabled()) m_Log.debug("INTERESADO MAIL: "+int_mail);
                                    mailsInteresados.addElement(int_mail);
                                }
                            }
                            rs.close();
                            st.close();
                        }
                    }
                }
                if (uiti.equals("S")) {
                    //coger el mail E_CRO
                    sql = "SELECT " + usu_email + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " INNER JOIN E_CRO"
                            + " ON (A_USU.USU_COD = E_CRO.CRO_USU )"
                            + " WHERE E_CRO.CRO_NUM = ? "
                            + " AND E_CRO.CRO_TRA = ?";
                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));
                    st.setString(2, codTramite);
                    m_Log.debug("Consulta Mail Usuario Inicio Tramite" + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_tramite = rs.getString(usu_email);
                        if (!(usu_mail_tramite == null) && !(usu_mail_tramite.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_tramite);
                            }
                            mailUsuInicioTramite = usu_mail_tramite;
                        }
                    }
                    rs.close();
                    st.close();

                }
                if (uiei.equals("S")) {
                    //coger el mail de usuario inicio expediente 
                    sql = "SELECT " + usu_email + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + "INNER JOIN E_EXP ON (A_USU.USU_COD= E_EXP.EXP_USU )"
                            + "WHERE E_EXP.EXP_NUM = ?";

                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));

                    m_Log.debug("Consulta Mail Usuario Inicio Expediente: " + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_exped = rs.getString(usu_email);
                        if (!(usu_mail_exped == null) && !(usu_mail_exped.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_exped);
                            }
                            mailUsuInicioExpediente = usu_mail_exped;
                        }
                    }
                    rs.close();
                    st.close();

                }
                eNotif.setListaEMailsUOR(mailsUOR);
                eNotif.setListaEMailsUsusUOR(mailsUsusUOR);
                eNotif.setListaEMailsInteresados(mailsInteresados);
                eNotif.setListaEmailsUsuInicioTramite(mailUsuInicioTramite);
                eNotif.setListaEmailsUsuInicioExped(mailUsuInicioExpediente);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("sale de getMailsUsuariosAlIniciar y devuelve: "+eNotif);
        } catch (Exception e) {
            e.printStackTrace(); 
            if(m_Log.isErrorEnabled()) m_Log.error("Exception: "+e.getMessage());
        }
        return eNotif;
    }

    private EstructuraNotificacion getMailsUsuariosAlFinalizar(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException {

        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;
        int resultado=0;

        EstructuraNotificacion eNotif = new EstructuraNotificacion();
        try {
            if(m_Log.isDebugEnabled()) m_Log.debug("ENTRA EN getMailsUsuariosAlFinalizar");

            String codOrganizacion = (String)gVO.getAtributo("codOrganizacion");
            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String codTramite = (String) gVO.getAtributo("codTramite");
            String uor = (String) gVO.getAtributo("codUnidadTramitadoraTram");
            Vector codInteresados = new Vector();   
            String codUsuario = (String) gVO.getAtributo("usuario");
            if(m_Log.isDebugEnabled()) m_Log.debug("UOR DEL TRAMITE QUE FINALIZA : " + uor);
            if(m_Log.isDebugEnabled()) m_Log.debug("COD INTERESADOS : " + codInteresados);
            if(m_Log.isDebugEnabled()) m_Log.debug("COD USUARIO : " + codUsuario);
            String utf = "";
            String usf = "";
            String inf = "";
            String uitf = "";
            String uief = "";
            String uor_mail = "";
            String uor_usu = "";
            String usu_mail = "";
            String int_mail = "";
            String usu_mail_tramite;
            String usu_mail_exped;
            Vector mailsUOR = new Vector();
            Vector mailsUsusUOR = new Vector();
            Vector mailsInteresados = new Vector();
            String mailUsuInicioTramite = "";
            String mailUsuInicioExpediente = "";
            Vector usuarios = new Vector();
            int procedimientoRestringido = 0;

            /*** PRUEBA ***/
            String ocuTramite = (String)gVO.getAtributo("ocurrenciaTramite");
            String numExpediente = (String)gVO.getAtributo("numero");

            m_Log.debug( "******************** ocurrenciaTramite:  " + ocuTramite);
            m_Log.debug( "******************** numExpediente:  " + numExpediente);

            // Se recupera la unidad tramitadora del trámite
            sql = "SELECT CRO_UTR FROM E_CRO WHERE CRO_NUM='" + numExpediente + "' AND CRO_MUN=" + codOrganizacion + " AND CRO_TRA=" + codTramite +
                  " AND CRO_OCU=" + ocuTramite;
            m_Log.debug(sql);

            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()) {
                uor = rs.getString("CRO_UTR");
            }

            m_Log.debug(" ******** UOR TRAMITADORA RECUPERADA: " + uor);
            st.close();
            rs.close();
            /*** PRUEBA ***/


            /** original
            sql = "SELECT " + tra_utf + "," + tra_usf + "," + tra_inf +
                    " FROM e_tra WHERE " + tra_mun + "=" + codMunicipio + " AND " + tra_pro + "=" + oad.addString(codProcedimiento) +
                    " AND " + tra_cod + "=" +codTramite;
            */
            sql = "SELECT " + tra_utf + "," + tra_usf + "," + tra_inf + ", TRA_NOTIF_UITF, TRA_NOTIF_UIEF, pro_restringido " +
                    " FROM e_tra,e_pro WHERE " + tra_mun + "=" + codMunicipio + " AND " + tra_pro + "=" + oad.addString(codProcedimiento) +
                    " AND " + tra_cod + "=" +codTramite + " and tra_pro=pro_cod and tra_mun=pro_mun";
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()) {
                utf = rs.getString(tra_utf);
                usf = rs.getString(tra_usf);
                inf = rs.getString(tra_inf);
                uitf = rs.getString("TRA_NOTIF_UITF");
                uief = rs.getString("TRA_NOTIF_UIEF");
                procedimientoRestringido = rs.getInt("pro_restringido");
                resultado = 1;
            }
            rs.close();
            st.close();
            if (resultado > 0) {

                sql = "SELECT " + tml_valor +
                        " FROM e_tml WHERE " + tml_mun + "=" + codMunicipio + " AND " + tml_pro + "=" + oad.addString(codProcedimiento) +
                        " AND " + tml_tra + "=" +codTramite;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()) {
                    eNotif.setNombreTramite(rs.getString(tml_valor));
                }
                rs.close();
                st.close();

                if (utf.equals("S")) {
                    //coger mail de a_uor buscando por uor
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],uor);
	
                    if (uorDTO!=null)	
                        if (uorDTO.getUor_email()!=null && !uorDTO.getUor_email().equals("")) {                            
                            uor_mail=uorDTO.getUor_email();
                            if(m_Log.isDebugEnabled()) m_Log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);
                            mailsUOR.addElement(uor_mail);
                        }
                }
                if (usf.equals("S")) {
                    //coger usuarios de a_uou buscando por uor
                    /** original
                    sql = "SELECT " + sql_uou_usu + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou WHERE " +
                            sql_uou_uor + "=" + uor + " AND " + sql_uou_org +
                            "=" + codOrganizacion + " AND " + sql_uou_usu + "<>" + codUsuario;
                    **/
                    if(procedimientoRestringido==0){
                       sql = "SELECT " + sql_uou_usu + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou WHERE " +
                            sql_uou_uor + "=" + uor + " AND " + sql_uou_org +
                            "=" + codOrganizacion + " AND " + sql_uou_usu + "<>" + codUsuario;
                    }else{
                        // El procedimiento está restringido, entonces se notifica a los usuarios con permisos sobre la 
                        // unidad organizativa, que además, tenga permiso sobre el procedimiento restringido. De este 
                        // modo se evita que le lleguen avisos a un usuario con permiso sobre la uor pero que no tiene
                        // permiso sobre el procedimiento restringido en cuestión
                        sql = "SELECT " + sql_uou_usu + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou, " + 
                               GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE " +
                               sql_uou_uor + "=" + uor + " AND " + sql_uou_org +
                               "=" + codOrganizacion + " AND " + sql_uou_usu + "<>" + codUsuario +
                               " AND USUARIO_PROC_RESTRINGIDO.PRO_COD='" + codProcedimiento + "' AND USUARIO_PROC_RESTRINGIDO.ORG_COD=" + codOrganizacion + 
                               " AND USUARIO_PROC_RESTRINGIDO.USU_COD=A_UOU.UOU_USU" ;
                    }
                        
                    
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while(rs.next()) {
                        uor_usu = rs.getString(sql_uou_usu);
                        if (!(uor_usu==null) && !(uor_usu.equals(""))) {
                            if(m_Log.isDebugEnabled()) m_Log.debug("CODIGO USUARIO: "+uor_usu);
                            usuarios.addElement(uor_usu);
                        }
                    }
                    rs.close();
                    st.close();
                    //coger mail de a_usu buscando por cod_usu
                    for (int i=0;i<usuarios.size();i++) {
                        sql = "SELECT " + usu_email + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu WHERE " +
                                usu_cod + "=" + usuarios.elementAt(i);
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        st = con.prepareStatement(sql);
                        rs = st.executeQuery();
                        while(rs.next()) {
                            usu_mail = rs.getString(usu_email);
                            if (!(usu_mail==null) && !(usu_mail.equals(""))) {
                                if(m_Log.isDebugEnabled()) m_Log.debug("USUARIO MAIL: "+usu_mail);
                                mailsUsusUOR.addElement(usu_mail);
                            }
                        }
                        rs.close();
                        st.close();
                    }
                }
                if (inf.equals("S")) {
                    sql = "SELECT EXT_TER, EXT_NVR FROM E_EXT WHERE EXT_NUM=? AND EXT_MUN=? AND EXT_EJE=?";
                    st = con.prepareStatement(sql);
                    st.setString(1, (String)gVO.getAtributo("numero"));
                    st.setString(2, codMunicipio);
                    st.setString(3, (String)gVO.getAtributo("ejercicio"));

                    m_Log.debug("Consulta de terceros del expediente: " + sql);
                    rs = st.executeQuery();



                    while (rs.next()) {
                        GeneralValueObject res = new GeneralValueObject();
                        res.setAtributo("codigo", rs.getString("EXT_TER"));
                        res.setAtributo("version", rs.getString("EXT_NVR"));
                        codInteresados.add(res);
                    }


                    //coger mail de t_ter buscando por codInteresados
                    if (codInteresados!=null) {
                        for (int i=0;i<codInteresados.size();i++) {
                            GeneralValueObject resVO = (GeneralValueObject) codInteresados.elementAt(i);
                            sql = "SELECT " + ter_dce + " FROM t_ter WHERE " + ter_cod +
                                    "=" + (String) resVO.getAtributo("codigo")+ "AND " + ter_nve +"="+
                                    (String) resVO.getAtributo("version");
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                            st = con.prepareStatement(sql);
                            rs = st.executeQuery();
                            while(rs.next()) {
                                int_mail = rs.getString(ter_dce);
                                if (!(int_mail==null) && !(int_mail.equals(""))) {
                                    if(m_Log.isDebugEnabled()) m_Log.debug("INTERESADO MAIL: "+int_mail);
                                    mailsInteresados.addElement(int_mail);
                                }
                            }
                            rs.close();
                            st.close();
                        }
                    }
                }
                if (uitf.equals("S")) {
                    //coger el mail usuario inicio tramite
                    sql = "SELECT " + usu_email + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " INNER JOIN E_CRO "
                            + " ON (A_USU.USU_COD= E_CRO.CRO_USU )"
                            + " WHERE E_CRO.CRO_NUM = ?"
                            + " AND E_CRO.CRO_TRA = ?";
                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));
                    st.setString(2, codTramite);
                    m_Log.debug("Consulta Mail Usuario Inicio Tramite" + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_tramite = rs.getString(usu_email);
                        if (!(usu_mail_tramite == null) && !(usu_mail_tramite.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_tramite);
                            }
                            mailUsuInicioTramite = usu_mail_tramite;
                        }
                    }
                    rs.close();
                    st.close();

                }
                if (uief.equals("S")) {
                    //coger el mail de usuario inicio expediente 
                    sql = "SELECT " + usu_email + " FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " INNER JOIN E_EXP ON (A_USU.USU_COD= E_EXP.EXP_USU )"
                            + " WHERE E_EXP.EXP_NUM = ?";

                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));

                    m_Log.debug("Consulta Mail Usuario Inicio Expediente: " + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_exped = rs.getString(usu_email);
                        if (!(usu_mail_exped == null) && !(usu_mail_exped.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_exped);
                            }
                            mailUsuInicioExpediente = usu_mail_exped;
                        }
                    }
                    rs.close();
                    st.close();

                }
                eNotif.setListaEMailsUOR(mailsUOR);
                eNotif.setListaEMailsUsusUOR(mailsUsusUOR);
                eNotif.setListaEMailsInteresados(mailsInteresados);
                eNotif.setListaEmailsUsuInicioTramite(mailUsuInicioTramite);
                eNotif.setListaEmailsUsuInicioExped(mailUsuInicioExpediente);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("sale de getMailsUsuariosAlFinalizar");
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error("Exception: "+e.getMessage());
            e.printStackTrace();
        }
        return eNotif;
    }

    private int insertarTramiteManual(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException, TechnicalException {

        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        int resultado=0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String ocurrencia = (String) gVO.getAtributo("ocurrenciaTramite");
        String usuario = (String)gVO.getAtributo("usuario");
        String uor = "";//(String)gVO.getAtributo("codUOR");
        String codTramite = (String) gVO.getAtributo("codTramite");
        String codUnidadOrganicaExp = (String) gVO.getAtributo("codUnidadOrganicaExp");
        String codUnidadTramitadoraTram = (String) gVO.getAtributo("codUnidadTramitadoraTram");
        if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica del expediente es : " + codUnidadOrganicaExp);
        if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad tramitadora del tramite es : " + codUnidadTramitadoraTram);
        String codUnidadTramitadoraAnterior = (String) gVO.getAtributo("codUnidadTramitadoraAnterior");
        if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica anterior del tramite es : " + codUnidadTramitadoraAnterior);
        String codEntidad = (String) gVO.getAtributo("codEntidad");
        String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
        String codUnidadTramitadoraUsu = (String) gVO.getAtributo("codUnidadTramitadoraUsu");

        String[] infoPlazos = DefinicionTramitesDAO.getInstance().getInfoPlazosTramite(
                con, Integer.parseInt(codMunicipio), codProcedimiento, Integer.parseInt(codTramite));

        String tipoPlazo = null, unidadesPlazo = null;
        Calendar fechaInicioPlazo = null, fechaLimitePlazo = null;
        if (infoPlazos != null) {
            if (infoPlazos.length == 3) {
                if ("true".equals(infoPlazos[0])) {
                    tipoPlazo = infoPlazos[1];
                    unidadesPlazo = infoPlazos[2];
                    fechaInicioPlazo = Calendar.getInstance();
                    fechaLimitePlazo = getFechaLimitePlazo(fechaInicioPlazo, tipoPlazo, Integer.parseInt(unidadesPlazo), con, oad);
                }
            }
        }

        if(codUnidadTramitadoraTram != null && !"".equals(codUnidadTramitadoraTram)) {
            if("99".equals(codUnidadTramitadoraTram)) {               
                    //uor = codUnidadTramitadoraAnterior;
                    uor = codUnidadOrganicaExp;
                    if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
                
            } else if("98".equals(codUnidadTramitadoraTram)) {
                if(codUnidadTramitadoraUsu != null && !"".equals(codUnidadTramitadoraUsu)) {
                    uor = codUnidadTramitadoraUsu;
                } else {
                    sql = "SELECT " + sql_uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " +
                            sql_uou_usu + "=" +
                            usuario + " AND " + sql_uou_org + "=" + codOrganizacion + " AND " +
                            sql_uou_ent + "=" + codEntidad;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    if(rs.next()) {
                        uor = rs.getString(sql_uou_uor);
                    }
                    rs.close();
                    st.close();
                }
            } else {
		uor = codUnidadTramitadoraTram;
            if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
            }
        } else {
            sql = "SELECT " + tra_uin + " FROM E_TRA WHERE " + tra_mun + "=" + codMunicipio + " AND " +
                    tra_pro + "='" + codProcedimiento + "' AND " + tra_cod + "=" + codTramite;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                codUnidadTramitadoraTram = rs.getString(tra_uin);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad tramitadora del tramite es ::::::::::::::::::::::::: : " + codUnidadTramitadoraTram);
            rs.close();
            st.close();
            if(codUnidadTramitadoraTram != null && !"".equals(codUnidadTramitadoraTram)) {
                if("99".equals(codUnidadTramitadoraTram)) {
                    
                        //uor = codUnidadTramitadoraAnterior;
                    	uor = codUnidadOrganicaExp;
            if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
                    
                } else if("98".equals(codUnidadTramitadoraTram)) {
                    if(codUnidadTramitadoraUsu != null && !"".equals(codUnidadTramitadoraUsu)) {
                        uor = codUnidadTramitadoraUsu;
                    } else {
                        sql = "SELECT " + sql_uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " +
                                sql_uou_usu + "=" +
                                usuario + " AND " + sql_uou_org + "=" + codOrganizacion + " AND " +
                                sql_uou_ent + "=" + codEntidad;
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        st = con.createStatement();
                        rs = st.executeQuery(sql);
                        if(rs.next()) {
                            uor = rs.getString(sql_uou_uor);
                        }
                        rs.close();
                        st.close();
                    }
                } else {
                      uor = codUnidadTramitadoraTram;
                      if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es 5:::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
                }
        } else {
            if(codUnidadOrganicaExp !=null && !"".equals(codUnidadOrganicaExp)) {
                uor = codUnidadOrganicaExp;
            } else {
                sql = "SELECT " + exp_uor + " FROM E_EXP WHERE " + exp_mun + "=" + codMunicipio + " AND " +
                        exp_eje + "=" + ejercicio + " AND " + exp_num + "='" + numero + "'";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()) {
                    uor = rs.getString(exp_uor);
                }
                rs.close();
                st.close();
            }
        }
        }

        sql = "SELECT " + cro_tra + " FROM E_CRO WHERE " + cro_mun + "=" + codMunicipio +
                " AND " + cro_pro + "='" + codProcedimiento + "' AND " + cro_eje + "=" + ejercicio + " AND " +
                cro_num + "='" + numero + "' AND " + cro_tra + "=" + codTramite + " AND " + cro_ocu + "=" +
                ocurrencia;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        st = con.createStatement();
        rs = st.executeQuery(sql);
        String yaExiste = "no";
        while(rs.next()) {
            yaExiste = "si";
        }
        rs.close();
        st.close();

        if("no".equals(yaExiste)) {
            String sqlInsert = "INSERT INTO E_CRO (CRO_MUN, CRO_PRO, CRO_EJE, CRO_NUM, CRO_TRA, CRO_OCU, CRO_FEI, CRO_FEF, CRO_USU, CRO_UTR";
            if (fechaInicioPlazo != null) {
                sqlInsert += ", CRO_FIP, CRO_FLI";
            }
            sqlInsert += ") VALUES (" + codMunicipio + ", '" + codProcedimiento + "', " + ejercicio + ", '" + numero + "'"
                    + ", " + codTramite + ", " + ocurrencia + ", ?, NULL, " + usuario + ", " + uor;
            if (fechaInicioPlazo != null) {
                sqlInsert += ", ?, ?";
            }
            sqlInsert += ")";

            if (m_Log.isDebugEnabled()) m_Log.debug("INSERTAR TRAMITE: " + sqlInsert);
            ps = con.prepareStatement(sqlInsert);
            ps.setTimestamp(1, new Timestamp((new java.util.Date()).getTime()));

            if (fechaInicioPlazo != null) {
                ps.setTimestamp(2, new Timestamp(fechaInicioPlazo.getTime().getTime()));
                ps.setTimestamp(3, new Timestamp(fechaLimitePlazo.getTime().getTime()));
            }
            resultado = ps.executeUpdate();
            gVO.setAtributo("codUORTramiteIniciado",uor);

            ps.close();
            UORDTO uorDTO = (UORDTO) CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6], uor);
            if (uorDTO != null) {
                gVO.setAtributo("nombreUORTramiteIniciado", uorDTO.getUor_nom());
            }
        } else {
            resultado = 1;
        }
        return resultado;
    }
    private int guardarSitucionExpediente(AdaptadorSQL oad, Connection con, GeneralValueObject gVO, TramitacionExpedientesValueObject teVO)
            throws SQLException,TechnicalException, BDException  {
        AdaptadorSQLBD abd = null;
        String sql = null;
       
        int res=0;
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
       
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String codUsuario = (String) gVO.getAtributo("usuario");

        String justificacion=teVO.getJustificacion();
        String personaAutoriza=teVO.getPersonaAutoriza();

        m_Log.debug("justificacion  "+justificacion);

        //INSERTAMOS EN LA SITUACION DEL expediente UN 1 QUE INDICA QUE FUE FINALIZADO DE FORMA NO CONVENCIONAL
        try {
            sql = "INSERT INTO E_EXPSIT (EXPSIT_NUM,EXPSIT_MUN,EXPSIT_EJE,EXPSIT_JUST,EXPSIT_USUARIO,EXPSIT_AUTORIZA) VALUES ('"+
            numero+"',"+codMunicipio +","+ejercicio+",'"+justificacion+"',"+codUsuario+",'"+personaAutoriza+"')";


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = con.prepareStatement(sql);
            res = ps.executeUpdate();
            m_Log.debug("las filas afectadas en el insert son : " + res);
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
        try{
        //con.close();
            } catch(Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
            }
        }
        return res;
    }

   private int guardarSitucionTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException,TechnicalException, BDException  {
        AdaptadorSQLBD abd = null;
        String sql = null;
      
        int res=0;
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String codTramite = (String) gVO.getAtributo("codTramite");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");

        //INSERTAMOS EN LA SITUACION DEL TRAMITE UN 1 QUE INDICA QUE FUE FINALIZADO DE FORMA NO CONVENCIONAL
        try {
            sql = "INSERT INTO E_TRASIT (TRASIT_COD,TRASIT_PRO,TRASIT_EJE,TRASIT_SITUACION,TRASIT_MUN,TRASIT_NUM) VALUES ("
            +codTramite+",'"+codProcedimiento +"',"+ejercicio+",1,"+codMunicipio+",'"+numero +"')";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = con.prepareStatement(sql);
            res = ps.executeUpdate();
            m_Log.debug("las filas afectadas en el insert son : " + res);
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
            //con.close();
            } catch(Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
            }
        }
        return res;
    }

      private int actualizarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO, boolean finalizarTramite)
            throws SQLException,TechnicalException, BDException  {

        PreparedStatement st = null;
        String sql = null;
        Statement st2 = null;
        String sql2 = null;
        ResultSet rs2 = null;
        int resultado=0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String ocurrencia = (String) gVO.getAtributo("ocurrenciaTramite");
        String usuario = (String)gVO.getAtributo("usuario");
        //String uor = (String)gVO.getAtributo("codUOR");
        String codTramite = (String) gVO.getAtributo("codTramite");
        String fechaFin = (String) gVO.getAtributo("fechaFinTramite");
        String fechaInicioPlazo = (String) gVO.getAtributo("fechaInicioPlazo");
        String fechaFinPlazo = (String) gVO.getAtributo("fechaFinPlazo");
        String fechaLimite = (String) gVO.getAtributo("fechaLimite");
        String resolucion = (String) gVO.getAtributo("resolucion");
        String observaciones = null;
        if(gVO.getAtributo("observaciones")!=null)
            observaciones = (String) gVO.getAtributo("observaciones");
        String codUsuario = (String) gVO.getAtributo("usuario");
        String bloqueo = (String) gVO.getAtributo("bloqueo");


        //Primero vamos a comprobar que todavía no ha sido actualizado
        //debido a problemas de concurrencia
        sql2 ="SELECT " + cro_fef + " FROM E_CRO";
        sql2 += " WHERE " + cro_mun + "=" + codMunicipio
                +" AND  "+ cro_pro + "='" + codProcedimiento + "'"
                +" AND "+ cro_eje + "=" + ejercicio
                +" AND "+ cro_num + "='" + numero +"'"
                +" AND "+ cro_tra + "=" + codTramite
                +" AND "+ cro_ocu + "=" + ocurrencia;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
        boolean ok =true;
        try {
            st2 = con.createStatement();
            rs2 = st2.executeQuery(sql2);
            if (rs2.next()) {
                if (rs2.getString(cro_fef) != null) {
                    ok = false;
                }
            }
            rs2.close();
            st2.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (!ok) {  //Si ya ha sido actualizada previamente
            return -999;
        } else {
        sql = "UPDATE E_CRO  SET ";

        sql += cro_fef +"=";
        if (finalizarTramite )
            //sql += oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");
            sql += oad.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + cro_usf + "=" + codUsuario;
            // En otro caso no se actualiza.
        else {
            if (fechaFin != null)
                if (!"".equals(fechaFin.trim())) sql += oad.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + "," + cro_usf + "=" + codUsuario;
                else sql += "null";
            else sql += "null";
        }

        sql += ","+cro_fip +"=";
        if (fechaInicioPlazo != null)
            if (!"".equals(fechaInicioPlazo.trim())) sql += oad.convertir("'" + fechaInicioPlazo + "'",AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");
            else sql += "null";
        else sql += "null";
        sql += ","+cro_ffp +"=";
        if (fechaFinPlazo != null)
            if (!"".equals(fechaFinPlazo.trim())) sql += oad.convertir("'" + fechaFinPlazo + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");
            else sql += "null";
        else sql += "null";
        sql += ","+cro_flim +"=";
        if (fechaLimite != null)
      if (!"".equals(fechaLimite.trim())) sql += oad.convertir("'" + fechaLimite + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");
            else sql += "null";
        else sql += "null";

        if (resolucion != null) sql +=","+cro_res +"=" + resolucion;
        // original
        //if (observaciones != null && observaciones.length()>0) sql +=", CRO_OBS = ?";
        if (observaciones != null && observaciones.length()>0) 
            sql +=", CRO_OBS = ?";
        else
            sql +=", CRO_OBS = null";


        sql += " WHERE " + cro_mun + "=" + codMunicipio
                +" AND  "+ cro_pro + "='" + codProcedimiento + "'"
                +" AND "+ cro_eje + "=" + ejercicio
                +" AND "+ cro_num + "='" + numero +"'"
                +" AND "+ cro_tra + "=" + codTramite
                +" AND "+ cro_ocu + "=" + ocurrencia;

        st = con.prepareStatement(sql);
        if (observaciones != null  && observaciones.length()>0) st.setString(1, observaciones);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        resultado = st.executeUpdate();
        st.close();

            if (bloqueo != null) {
                if (!bloqueo.equals("no")) {
                    // ELIMINA EL BLOQUEO SI LO TIENE
                    sql = "DELETE FROM E_EXP_BLOQ WHERE " + bloq_mun + "=" + codMunicipio + " AND " +
                                                            bloq_eje + "=" + ejercicio + " AND " +
                                                            bloq_num + "='" + numero + "' AND " +
                                                            bloq_pro + "='" + codProcedimiento + "' AND " +
                                                            bloq_tra + "=" + codTramite + " AND " +
                                                            bloq_ocu + "=" + ocurrencia + " AND " +
                                                            bloq_usu + "=" + usuario;
                    st = con.prepareStatement(sql);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    resultado = st.executeUpdate();
                    st.close();
                    // ELIMINA EL BLOQUEO SI LO TIENE
                }
            }

        // ACTUALIZAR LOS VALORES EXP_TRA Y EXP_TOCU
        if(finalizarTramite || fechaFin != null || !"".equals(fechaFin.trim()) ) {
            sql = "UPDATE E_EXP SET " + exp_tra + "=" + codTramite + "," + exp_tocu + "=" +
                    ocurrencia + " WHERE " + exp_mun + "=" + codMunicipio + " AND " + exp_eje +
                    "=" + ejercicio + " AND " + exp_num + "='" + numero + "'";
            st = con.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate();
            st.close();
        }

        return resultado;
    }
    }


      
      
       private int actualizarTramiteNoConvencional(AdaptadorSQL oad, Connection con, GeneralValueObject gVO, boolean finalizarTramite)
            throws SQLException,TechnicalException, BDException  {

        PreparedStatement st = null;
        String sql = null;
        Statement st2 = null;
        String sql2 = null;
        ResultSet rs2 = null;
        int resultado=0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String ocurrencia = (String) gVO.getAtributo("ocurrenciaTramite");
        String usuario = (String)gVO.getAtributo("usuario");
        //String uor = (String)gVO.getAtributo("codUOR");
        String codTramite = (String) gVO.getAtributo("codTramite");
        String fechaFin = (String) gVO.getAtributo("fechaFinTramite");
        String fechaInicioPlazo = (String) gVO.getAtributo("fechaInicioPlazo");
        String fechaFinPlazo = (String) gVO.getAtributo("fechaFinPlazo");
        String fechaLimite = (String) gVO.getAtributo("fechaLimite");
        String resolucion = (String) gVO.getAtributo("resolucion");
        String observaciones = null;
        if(gVO.getAtributo("observaciones")!=null)
            observaciones = (String) gVO.getAtributo("observaciones");
        String codUsuario = (String) gVO.getAtributo("usuario");
        String bloqueo = (String) gVO.getAtributo("bloqueo");


        //Primero vamos a comprobar que todavía no ha sido actualizado
        //debido a problemas de concurrencia 
        sql2 ="SELECT " + cro_fef + " FROM E_CRO";  
        sql2 += " WHERE " + cro_mun + "=" + codMunicipio
                +" AND  "+ cro_pro + "='" + codProcedimiento + "'"
                +" AND "+ cro_eje + "=" + ejercicio
                +" AND "+ cro_num + "='" + numero +"'"
                +" AND "+ cro_tra + "=" + codTramite
                +" AND "+ cro_ocu + "=" + ocurrencia;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
        boolean ok =true;
        try {
            st2 = con.createStatement();
            rs2 = st2.executeQuery(sql2);
            if (rs2.next()) {
                if (rs2.getString(cro_fef) != null) {
                    ok = false;
                }
            }
            rs2.close();
            st2.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (!ok) {  //Si ya ha sido actualizada previamente
            return -999;
        } else {
        sql = "UPDATE E_CRO  SET ";

        sql += cro_fef +"=";
        if (finalizarTramite )
            //sql += oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");
            sql += oad.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + cro_usf + "=" + codUsuario;
            // En otro caso no se actualiza.
        else {
            if (fechaFin != null)
                if (!"".equals(fechaFin.trim())) sql += oad.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + "," + cro_usf + "=" + codUsuario;
                else sql += "null";
            else sql += "null";
        }

        


        sql += " WHERE " + cro_mun + "=" + codMunicipio
                +" AND  "+ cro_pro + "='" + codProcedimiento + "'"
                +" AND "+ cro_eje + "=" + ejercicio
                +" AND "+ cro_num + "='" + numero +"'"
                +" AND "+ cro_tra + "=" + codTramite
                +" AND "+ cro_ocu + "=" + ocurrencia;

        st = con.prepareStatement(sql);
        if (observaciones != null  && observaciones.length()>0) st.setString(1, observaciones);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        resultado = st.executeUpdate();
        st.close();

            if (bloqueo != null) {
                if (!bloqueo.equals("no")) {
                    // ELIMINA EL BLOQUEO SI LO TIENE
                    sql = "DELETE FROM E_EXP_BLOQ WHERE " + bloq_mun + "=" + codMunicipio + " AND " +
                                                            bloq_eje + "=" + ejercicio + " AND " +
                                                            bloq_num + "='" + numero + "' AND " +
                                                            bloq_pro + "='" + codProcedimiento + "' AND " +
                                                            bloq_tra + "=" + codTramite + " AND " +
                                                            bloq_ocu + "=" + ocurrencia + " AND " +
                                                            bloq_usu + "=" + usuario;
                    st = con.prepareStatement(sql);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    resultado = st.executeUpdate();
                    st.close();
                    // ELIMINA EL BLOQUEO SI LO TIENE
                }
            }

        // ACTUALIZAR LOS VALORES EXP_TRA Y EXP_TOCU
        if(finalizarTramite || fechaFin != null || !"".equals(fechaFin.trim()) ) {
            sql = "UPDATE E_EXP SET " + exp_tra + "=" + codTramite + "," + exp_tocu + "=" +
                    ocurrencia + " WHERE " + exp_mun + "=" + codMunicipio + " AND " + exp_eje +
                    "=" + ejercicio + " AND " + exp_num + "='" + numero + "'";
            st = con.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate();
            st.close();
        }

        return resultado;
    }
    }


    private int cambiarEstadoFirmaDocumentoFin(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject tEVO)
            throws SQLException,TechnicalException, BDException  {


        /*Si el documento se envio a firmar hay que sacarlo del portafirmas. y si está pendiente de firmar por el
         tramitador, se elimina.*/

        Statement st2,st = null;
        String sql2,sql = null;
        ResultSet rs2,rs = null;
        int resultado=0;
        String codDocu;
       try {
         sql = "SELECT CRD_NUD from E_CRD WHERE  CRD_MUN =" + tEVO.getCodMunicipio()+
                 "AND CRD_PRO ='" + tEVO.getCodProcedimiento() + "' AND CRD_EJE=" +tEVO.getEjercicio() +
                 " AND CRD_NUM='" + tEVO.getNumeroExpediente() +"' AND CRD_TRA=" + tEVO.getCodTramite() +
                 " AND CRD_OCU=" + tEVO.getOcurrenciaTramite() + " AND CRD_FIR_EST='O'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              st = con.createStatement();
              rs = st.executeQuery(sql);
              while (rs.next()) {
                  codDocu = rs.getString("CRD_NUD");
                   sql2 = "UPDATE E_CRD SET CRD_FIR_EST='E' WHERE  CRD_MUN =" + tEVO.getCodMunicipio()+
                 "AND CRD_PRO ='" + tEVO.getCodProcedimiento() + "' AND CRD_EJE=" +tEVO.getEjercicio() +
                 " AND CRD_NUM='" + tEVO.getNumeroExpediente() +"' AND CRD_TRA=" + tEVO.getCodTramite() +
                 " AND CRD_OCU=" + tEVO.getOcurrenciaTramite() + " AND CRD_NUD=" +codDocu;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                   st2 = con.createStatement();
                   resultado = st2.executeUpdate(sql2);
                   st2.close();
                  sql2 = "DELETE FROM E_CRD_FIR WHERE  CRD_MUN =" + tEVO.getCodMunicipio()+
                 "AND CRD_PRO ='" + tEVO.getCodProcedimiento() + "' AND CRD_EJE=" +tEVO.getEjercicio() +
                 " AND CRD_NUM='" + tEVO.getNumeroExpediente() +"' AND CRD_TRA=" + tEVO.getCodTramite() +
                 " AND CRD_OCU=" + tEVO.getOcurrenciaTramite() + " AND CRD_NUD=" +codDocu;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
                   st2 = con.createStatement();
                   resultado = st2.executeUpdate(sql2);
                   st2.close();

              }

             sql = "SELECT CRD_NUD from E_CRD WHERE  CRD_MUN =" + tEVO.getCodMunicipio() +
                   "AND CRD_PRO ='" + tEVO.getCodProcedimiento() + "' AND CRD_EJE=" + tEVO.getEjercicio() +
                   " AND CRD_NUM='" + tEVO.getNumeroExpediente() + "' AND CRD_TRA=" + tEVO.getCodTramite() +
                   " AND CRD_OCU=" + tEVO.getOcurrenciaTramite() + " AND CRD_FIR_EST='T'";
           if (m_Log.isDebugEnabled()) {
               m_Log.debug(sql);
           }
           st = con.createStatement();
           rs = st.executeQuery(sql);
           while (rs.next()) {
               codDocu = rs.getString("CRD_NUD");
               sql2 = "DELETE FROM E_CRD WHERE CRD_MUN=" + tEVO.getCodMunicipio() + " AND CRD_PRO" + "='" + tEVO.getCodProcedimiento() + "' AND CRD_EJE=" +
                       tEVO.getEjercicio() + " AND CRD_NUM='" + tEVO.getNumeroExpediente() +
                       "' AND CRD_TRA=" + tEVO.getCodTramite() + " AND CRD_OCU=" +
                       tEVO.getOcurrenciaTramite() + " AND CRD_NUD=" + codDocu;
               if (m_Log.isDebugEnabled()) {
                   m_Log.debug(sql2);
               }
               st2 = con.createStatement();
               resultado = st2.executeUpdate(sql2);
               st2.close();
           }

              st.close();
              rs.close();

        } catch(Exception e) {
            e.printStackTrace();
        }


        return resultado;

    }

    public int actualizarTramiteRelacion(TramitacionExpedientesValueObject teVO, String[] params, boolean finalizarTramite) {
      GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      PreparedStatement ps= null;
      String sql = null;
      int resultado;
      ResultSet rs=null;

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");
      String ocurrencia = (String) gVO.getAtributo("ocurrenciaTramite");
      String usuario = (String)gVO.getAtributo("usuario");
      String codTramite = (String) gVO.getAtributo("codTramite");
      String fechaFin = (String) gVO.getAtributo("fechaFinTramite");
      String fechaInicioPlazo = (String) gVO.getAtributo("fechaInicioPlazo");
      String fechaFinPlazo = (String) gVO.getAtributo("fechaFinPlazo");
      String fechaLimite = (String) gVO.getAtributo("fechaLimite");
      String resolucion = (String) gVO.getAtributo("resolucion");
      String observaciones = (String) gVO.getAtributo("observaciones");
      String codUsuario = (String) gVO.getAtributo("usuario");
      String numeroRelacion = (String) gVO.getAtributo("numeroRelacion");
      if (!ejercicio.equals(numeroRelacion.substring(0, 4))) ejercicio = numeroRelacion.substring(0, 4);

      try {
          /*Determinar la ocurrencia del trámite que se está cerrando, ya que se está pasando la ocurrentica de uno de los
           expedientes y no siempre es correcto. Puede darse que dos expedientes tengan ocurrencias diferentes del trámite.*/

      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();


      sql ="SELECT CRO_OCU FROM G_CRO WHERE CRO_MUN="+codMunicipio+" AND CRO_PRO='"+
              codProcedimiento+"' AND CRO_NUM='"+numeroRelacion+"' AND CRO_TRA="+codTramite+" AND CRO_FEF IS NULL";
            st = con.createStatement();
            m_Log.debug (sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                ocurrencia = rs.getString("CRO_OCU");
            }
            m_Log.debug (ocurrencia);

      sql = "UPDATE G_CRO  SET " + cro_rel_fef +"=";
      if (finalizarTramite )
        //sql += oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");
        sql += oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null) + "," + cro_rel_usf + "=" + codUsuario;
      // En otro caso no se actualiza.
      else {
        if (fechaFin != null)
          if (!"".equals(fechaFin.trim())) sql += oad.fechaHora(fechaFin, oad.FECHAHORA_CAMPO_FECHA) + "," + cro_rel_usf + "=" + codUsuario;
          else sql += "null";
        else sql += "null";
      }

      sql += ","+cro_rel_fip +"=";
      if (fechaInicioPlazo != null)
        if (!"".equals(fechaInicioPlazo.trim())) sql += oad.fechaHora(fechaInicioPlazo, oad.FECHAHORA_CAMPO_FECHA);
        else sql += "null";
      else sql += "null";
      sql += ","+cro_rel_ffp +"=";
      if (fechaFinPlazo != null)
        if (!"".equals(fechaFinPlazo.trim())) sql += oad.fechaHora(fechaFinPlazo, oad.FECHAHORA_CAMPO_FECHA);
        else sql += "null";
      else sql += "null";
      sql += ","+cro_rel_flim +"=";
      if (fechaLimite != null)
        if (!"".equals(fechaLimite.trim())) sql += oad.fechaHora(fechaLimite, oad.FECHAHORA_CAMPO_FECHA);
        else sql += "null";
      else sql += "null";

      if (resolucion != null) sql +=","+cro_rel_res +"=" + resolucion;
      if (observaciones != null) sql +=","+cro_rel_obs +"='" + observaciones + "'";


      sql += " WHERE " + cro_rel_mun + "=" + codMunicipio
      +" AND  "+ cro_rel_pro + "='" + codProcedimiento + "'"
      +" AND "+ cro_rel_eje + "=" + ejercicio
      +" AND "+ cro_rel_num + "='" + numeroRelacion +"'"
      +" AND "+ cro_rel_tra + "=" + codTramite
      +" AND "+ cro_rel_ocu + "=" + ocurrencia;

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      st = con.createStatement();
      resultado = st.executeUpdate(sql);

      // ACTUALIZAR LOS VALORES EXP_TRA
      if(finalizarTramite || fechaFin != null || !"".equals(fechaFin.trim()) ) {
          sql = "UPDATE G_REL SET " + rel_tra + "=" + codTramite + " WHERE " + rel_mun + "=" + codMunicipio + " AND " +
                rel_eje + "=" + ejercicio + " AND " + rel_num + "='" + numeroRelacion + "'";
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          resultado = st.executeUpdate(sql);
      }
        st.close();
     } catch (Exception e) {
       resultado =0;
       e.printStackTrace();
     } finally {
       try{
         con.close();
       } catch(SQLException sqle) {
         sqle.printStackTrace();
         m_Log.error("SQLException del finally: " +	sqle.getMessage());
       }
     }
      return resultado;
    }


    public int insertarTramiteRelacion(TramitacionExpedientesValueObject teVO, String[] params, boolean finalizarTramite) {
      GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
      AdaptadorSQLBD oad = null;
      Connection con = null;
      Statement st = null;
      String sql = null;
      ResultSet rs = null;
      int resultado=0;

      String codMunicipio = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

      /*El ejercicio nos supone un problema la relaicón se inicio en un ejercicio y los expedientes que la forman en otro.
       Para más datos consultar Dédalo #11208.*/
      String ejercicio = (String)gVO.getAtributo("ejercicio");
      String numero = (String)gVO.getAtributo("numero");
      String ejercicioExp = numero.substring(0, 4);
      String usuario = (String)gVO.getAtributo("usuario");
      String numeroRelacion = (String)gVO.getAtributo("numeroRelacion");
      String uor = "";
      try {
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();

      Vector tramitesAIniciar = (Vector) teVO.getListaTramitesIniciar();
      if (tramitesAIniciar.size() > 0) {
         for (int i=0; i< tramitesAIniciar.size(); i++)
          {
              TramitacionExpedientesValueObject t = (TramitacionExpedientesValueObject) tramitesAIniciar.elementAt(i);
              String codTramite = t.getCodTramite();
              String ocurrencia = "1";
              sql = " SELECT " + oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{cro_ocu}) +
                      "+1 AS OCURRENCIA FROM G_CRO "
              +" WHERE "+ cro_mun +"="+ codMunicipio
              +" AND "+ cro_pro+ "='" + codProcedimiento +"'"
              +" AND "+ cro_eje + "=" + ejercicioExp
              +" AND "+ cro_num + "='" + numero +"'"
              +" AND "+ cro_tra + "=" + codTramite;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              st = con.createStatement();
              rs = st.executeQuery(sql);
              if (rs.next()) {
                  if (rs.getString("OCURRENCIA") != null) ocurrencia = rs.getString("OCURRENCIA");
              }

              sql = "SELECT " + cro_utr + " FROM E_CRO WHERE " + cro_mun + "=" + codMunicipio + " AND " + cro_tra + "=" + codTramite +
                    " AND " + cro_pro + "='" + codProcedimiento + "' AND " + cro_eje + "=" + ejercicioExp + " AND " +
                    cro_num + "='" + numero + "' ORDER BY "+cro_fei+" DESC";
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              st = con.createStatement();
              rs = st.executeQuery(sql);
              if (rs.next()) {
                  uor = rs.getString(cro_utr);
              }

              sql = "INSERT INTO G_CRO ("
                  + cro_rel_mun +", "+ cro_rel_pro +","+ cro_rel_eje+","+ cro_rel_num+","+ cro_rel_tra+","
                  + cro_rel_ocu+","+cro_rel_fei+","+cro_rel_fef+","+cro_rel_usu+","+cro_rel_utr + ") VALUES ("
                  + codMunicipio + ",'"+ codProcedimiento + "'," + ejercicio + ",'" + numeroRelacion +"'"
                  + "," + codTramite + "," + ocurrencia + ","+oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null)
                  + ", null" + ", " + usuario + "," + uor + ")";

              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              st = con.createStatement();
              resultado = st.executeUpdate(sql);
              st.close();
          }
      }
     } catch (Exception e) {
       resultado =0;
       e.printStackTrace();
     } finally {
       try{
         con.close();
       } catch(SQLException sqle) {
         sqle.printStackTrace();
         m_Log.error("SQLException del finally: " +	sqle.getMessage());
       }
     }
      return resultado;
    }

    private String getOcurrenciaTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException,TechnicalException {
        Statement st = null;
        String sql = null;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String codTramite = (String) gVO.getAtributo("codTramite");
        String ocurrencia ="";

        st = con.createStatement();
        sql = " SELECT " + oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{cro_ocu}) +
                "+1 AS OCURRENCIA FROM E_CRO "
                +" WHERE "+ cro_mun +"="+ codMunicipio
                +" AND "+ cro_pro+ "='" + codProcedimiento +"'"
                +" AND "+ cro_eje + "=" + ejercicio
                +" AND "+ cro_num + "='" + numero +"'"
                +" AND "+ cro_tra + "=" + codTramite
                ;

        m_Log.debug(" ******* codTramite: " + codTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        ResultSet rs = st.executeQuery(sql);
        if (rs.next() ) {
            ocurrencia = rs.getString("OCURRENCIA");
            if (ocurrencia==null) ocurrencia = "1";

        }
        rs.close();
        st.close();
        return ocurrencia;
    }



    public int permisoModificacionTramiteUsuario(AdaptadorSQL oad, Connection con, GeneralValueObject gVO) {

        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String codTramite = (String) gVO.getAtributo("codTramite");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numeroExpediente = (String) gVO.getAtributo("numero");
        String ocurrenciaTramite = (String) gVO.getAtributo("ocurrenciaTramite");
        String codUsuario = (String) gVO.getAtributo("usuario");
        String codEntidad = (String) gVO.getAtributo("codEntidad");
        String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
        String expHistorico = (String) gVO.getAtributo("expHistorico");
        
        int permiso = 0;

        try {
           
           
           if ("true".equals(expHistorico)) 
                sql = "SELECT DISTINCT EXP_UOR, CRO_UTR, TRA_PRE" +
                         " FROM HIST_E_EXP, HIST_E_CRO, E_TRA WHERE exp_mun = cro_mun  AND " +
                         "exp_pro = cro_pro  AND  exp_eje = cro_eje AND " +
                         "exp_num = cro_num AND exp_mun = ? AND " +
                         "exp_pro = ? AND exp_eje = ?" +
                         " AND exp_num =? AND cro_tra = ?" +
                         " AND cro_ocu = ? AND cro_mun = " + 
                         "tra_mun  AND  cro_pro = tra_pro AND " +
                         "tra_cod = ? AND ((EXISTS ( SELECT DISTINCT " +
                         "uou_uor  FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE uou_usu =? AND uou_org = ? " + 
                         "AND uou_ent = ? AND uou_uor = exp_uor) AND tra_pre = 0) OR EXISTS (SELECT DISTINCT uou_uor FROM " +
                         GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE uou_usu =? " + 
                         "AND uou_org = ? AND uou_ent = ? AND uou_uor = cro_utr ))" ;
           else
                sql = "SELECT DISTINCT EXP_UOR, CRO_UTR, TRA_PRE" +
                         " FROM E_EXP, E_CRO, E_TRA WHERE exp_mun = cro_mun  AND " +
                         "exp_pro = cro_pro  AND  exp_eje = cro_eje AND " +
                         "exp_num = cro_num AND exp_mun = ? AND " +
                         "exp_pro = ? AND exp_eje = ?" +
                         " AND exp_num =? AND cro_tra = ?" +
                         " AND cro_ocu = ? AND cro_mun = " + 
                         "tra_mun  AND  cro_pro = tra_pro AND " +
                         "tra_cod = ? AND ((EXISTS ( SELECT DISTINCT " +
                         "uou_uor  FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE uou_usu =? AND uou_org = ? " + 
                         "AND uou_ent = ? AND uou_uor = exp_uor) AND tra_pre = 0) OR EXISTS (SELECT DISTINCT uou_uor FROM " +
                         GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE uou_usu =? " + 
                         "AND uou_org = ? AND uou_ent = ? AND uou_uor = cro_utr ))" ;
            
            if(m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: permisoModificacionTramiteUsuario --> "+ sql);
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numeroExpediente);
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(ocurrenciaTramite));
            ps.setInt(i++,Integer.parseInt(codTramite));
            ps.setInt(i++,Integer.parseInt(codUsuario));
            ps.setInt(i++,Integer.parseInt(codOrganizacion));
            ps.setInt(i++,Integer.parseInt(codEntidad));
            ps.setInt(i++,Integer.parseInt(codUsuario));
            ps.setInt(i++,Integer.parseInt(codOrganizacion));
            ps.setInt(i++,Integer.parseInt(codEntidad));
            
            rs = ps.executeQuery();
            if (rs.next()){
                 String unidadControladora = (String) rs.getString("exp_uor");
                String unidadTramitadora = (String) rs.getString("cro_utr");
                if ( (unidadControladora != null) && (unidadTramitadora != null) ) permiso = 1;
            }

        } catch (Exception e) {
            permiso =0;
            e.printStackTrace();
        }finally{
            try{
                if (ps!=null) ps.close();
                if (rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
         if(m_Log.isDebugEnabled()) m_Log.debug("Permiso: "+permiso);
        return permiso;
    }

    public String permisoTramitacionUsuario(GeneralValueObject gVO, String[] params, String cargo, String uor) {
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String permiso = "no";

        String usuario = (String)gVO.getAtributo("usuario");
        String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
        String codEntidad = (String) gVO.getAtributo("codEntidad");

        try {
          con = oad.getConnection();
          st = con.createStatement();
          String sql = "SELECT " + sql_uou_car + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " +
                       sql_uou_ent + " = " + codEntidad + " AND " +
                       sql_uou_org + " = " + codOrganizacion + " AND " + sql_uou_usu + " = " + usuario + " AND " +
                       sql_uou_uor + " = " + uor;

          if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: permisoTramitacionUsuario --> "+ sql);
          st = con.createStatement();
          rs = st.executeQuery(sql);
          if (rs.next()){
            if (cargo.equals(rs.getString(sql_uou_car)) || "0".equals(rs.getString(sql_uou_car))) permiso = "si";
          }

      } catch (Exception e) {
          permiso = "no";
          e.printStackTrace();
      }finally{
          try{
            rs.close();
            st.close();
                oad.devolverConexion(con);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    return permiso;
  }
   
    
    public String permisoTramitacionUsuario(GeneralValueObject gVO, String cargo, String uor,Connection con) {
        Statement st = null;
        ResultSet rs = null;
        String permiso = "no";

        String usuario = (String)gVO.getAtributo("usuario");
        String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
        String codEntidad = (String) gVO.getAtributo("codEntidad");

        try {
          
          st = con.createStatement();
          String sql = "SELECT " + sql_uou_car + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " +
                       sql_uou_ent + " = " + codEntidad + " AND " +
                       sql_uou_org + " = " + codOrganizacion + " AND " + sql_uou_usu + " = " + usuario + " AND " +
                       sql_uou_uor + " = " + uor;

          if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: permisoTramitacionUsuario --> "+ sql);
          st = con.createStatement();
          rs = st.executeQuery(sql);
          if (rs.next()){
            if (cargo.equals(rs.getString(sql_uou_car)) || "0".equals(rs.getString(sql_uou_car))) permiso = "si";
          }

      } catch (Exception e) {
          permiso = "no";
          e.printStackTrace();
      }finally{
          try{
            if(rs!=null) rs.close();
            if(st!=null) st.close();
                
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    return permiso;
  } 
    
    
    public List<String> getCargosUsuario(UsuarioValueObject gVO, String[] params ) {
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        List<String> idCargos= new ArrayList<String>();
        
        String usuario = (String)gVO.getNombreUsu();
        String codOrganizacion = String.valueOf( gVO.getOrgCod());
        String codEntidad = (String) gVO.getEnt();

        try {
          con = oad.getConnection();
          st = con.createStatement();
            
          String sql="SELECT "+sql_uou_car+" FROM "+GlobalNames.ESQUEMA_GENERICO+ "A_USU "+
                "LEFT JOIN  "+GlobalNames.ESQUEMA_GENERICO+"A_UOU ON " +usu_cod+"="+sql_uou_usu+
                " WHERE "+usu_cod+"="+gVO.getIdUsuario()+
                " GROUP BY "+ sql_uou_car;

          if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: permisoTramitacionUsuario --> "+ sql);
          st = con.createStatement();
          rs = st.executeQuery(sql);
          
          while (rs.next()){
            idCargos.add(rs.getString(sql_uou_car));
          }

      } catch (Exception e) {
          e.printStackTrace();
      }finally{
          try{
            rs.close();
            st.close();
                oad.devolverConexion(con);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    return idCargos;
  }
    

    public Vector getTramitesExpedienteSinFinalizar(TramitacionExpedientesValueObject teVO,String[] params)
    //throws SQLException,TechnicalException, BDException
    {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        return getTramitesExpedienteSinFinalizar(gVO,params);
    }

    private Vector getTramitesExpedienteSinFinalizar(GeneralValueObject gVO,String[] params)
    //throws SQLException,TechnicalException, BDException
    {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String sql = null;
        ResultSet rs = null;

        Vector lista = new Vector();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String codTramite = (String) gVO.getAtributo("codTramite");

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            sql = "SELECT " + cro_tra + "," + tml_valor + "," + cro_ocu + " FROM E_CRO,E_TML "
                    + " WHERE " + cro_mun + "=" + codMunicipio
                    + " AND " + cro_pro + "='" + codProcedimiento +"'"
                    + " AND " + cro_eje + "=" + ejercicio
                    + " AND " + cro_num + "='" + numero+"'"
                    + " AND " + cro_fef + " IS NULL AND " + cro_tra + "<>" + codTramite + " AND " +
                    cro_mun + "=" + tml_mun + " AND " + cro_pro + "=" + tml_pro + " AND " +
                    cro_tra + "=" + tml_tra + " AND " + tml_cmp + "='NOM' AND " + tml_leng + "="+idiomaDefecto;

            if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getTramitesSinFinalizar --> "+ sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String cT = rs.getString(cro_tra);
                g.setAtributo("codTramite",cT);
                String nombreTramite = rs.getString(tml_valor);
                g.setAtributo("nombreTramite",nombreTramite);
                String ocurrenciaTramite = rs.getString(cro_ocu);
                g.setAtributo("ocurrenciaTramite",ocurrenciaTramite);
                lista.addElement(g);
            }
            rs.close();
            st.close();

            // SQLException,TechnicalException, BDException
        } catch (Exception e) {
            lista =null;
            e.printStackTrace();
        } finally {
            try{
                oad.devolverConexion(con);
            } catch(BDException sqle) {
                sqle.printStackTrace();
                m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("el resultado de la cuenta es : " + lista.size());
        return lista;
    }
   public Vector getTramitesExpedienteSinFinalizarTotal(TramitacionExpedientesValueObject teVO,String[] params)
    //throws SQLException,TechnicalException, BDException
    {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        return getTramitesExpedienteSinFinalizarTotal(gVO,params);
    }

  private Vector getTramitesExpedienteSinFinalizarTotal(GeneralValueObject gVO,String[] params)
    //throws SQLException,TechnicalException, BDException
    {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String sql = null;
        ResultSet rs = null;

        Vector lista = new Vector();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            sql = "SELECT " + cro_tra + "," + tml_valor + "," + cro_ocu + " FROM E_CRO,E_TML "
                    + " WHERE " + cro_mun + "=" + codMunicipio
                    + " AND " + cro_pro + "='" + codProcedimiento +"'"
                    + " AND " + cro_eje + "=" + ejercicio
                    + " AND " + cro_num + "='" + numero+"'"
                    + " AND " + cro_fef + " IS NULL AND " +
                    cro_mun + "=" + tml_mun + " AND " + cro_pro + "=" + tml_pro + " AND " +
                    cro_tra + "=" + tml_tra + " AND " + tml_cmp + "='NOM' AND " + tml_leng + "="+idiomaDefecto;

            if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getTramitesSinFinalizar --> "+ sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String cT = rs.getString(cro_tra);
                g.setAtributo("codTramite",cT);
                String nombreTramite = rs.getString(tml_valor);
                g.setAtributo("nombreTramite",nombreTramite);
                String ocurrenciaTramite = rs.getString(cro_ocu);
                g.setAtributo("ocurrenciaTramite",ocurrenciaTramite);
                lista.addElement(g);
            }
            rs.close();
            st.close();

            // SQLException,TechnicalException, BDException
        } catch (Exception e) {
            lista =null;
            e.printStackTrace();
        } finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("el resultado de la cuenta es : " + lista.size());
        return lista;
    }

    public Vector getTramitesRelacionSinFinalizar(TramitacionExpedientesValueObject teVO,String[] params)
    //throws SQLException,TechnicalException, BDException
    {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        return getTramitesRelacionSinFinalizar(gVO,params);
    }
    private Vector getTramitesRelacionSinFinalizar(GeneralValueObject gVO,String[] params)
    //throws SQLException,TechnicalException, BDException
    {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String sql = null;
        ResultSet rs = null;

        Vector lista = new Vector();

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String codTramite = (String) gVO.getAtributo("codTramite");

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            sql = "SELECT " + cro_tra + "," + tml_valor + "," + cro_ocu + " FROM G_CRO,E_TML "
                    + " WHERE " + cro_mun + "=" + codMunicipio
                    + " AND " + cro_pro + "='" + codProcedimiento +"'"
                    + " AND " + cro_eje + "=" + ejercicio
                    + " AND " + cro_num + "='" + numero+"'"
                    + " AND " + cro_fef + " IS NULL AND " + cro_tra + "<>" + codTramite + " AND " +
                    cro_mun + "=" + tml_mun + " AND " + cro_pro + "=" + tml_pro + " AND " +
                    cro_tra + "=" + tml_tra + " AND " + tml_cmp + "='NOM' AND " + tml_leng + "="+idiomaDefecto;

            if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getTramitesRelacionSinFinalizar --> "+ sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String cT = rs.getString(cro_tra);
                g.setAtributo("codTramite",cT);
                String nombreTramite = rs.getString(tml_valor);
                g.setAtributo("nombreTramite",nombreTramite);
                String ocurrenciaTramite = rs.getString(cro_ocu);
                g.setAtributo("ocurrenciaTramite",ocurrenciaTramite);
                lista.addElement(g);
            }
            rs.close();
            st.close();

            // SQLException,TechnicalException, BDException
        } catch (Exception e) {
            lista =null;
            e.printStackTrace();
        } finally {
            try{
                if (con!=null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                m_Log.error("SQLException del finally: " +	sqle.getMessage());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("el resultado de la cuenta es : " + lista.size());
        return lista;
    }

  

    //Abrimos el trámite con la fecha fin más alta del municipio, procedimiento, ejercicio y número
    public int reabrirUltimoTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException,TechnicalException, BDException  {

        Statement st = null;
        String sql = null;
        int resultado=0;

        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");

        sql = " UPDATE E_CRO SET cro_fef = null";
        sql += " WHERE " + cro_mun + "=" + codMunicipio
                +" AND  "+ cro_pro + "='" + codProcedimiento + "'"
                +" AND "+ cro_eje + "=" + ejercicio
                +" AND "+ cro_num + "='" + numero +"'"
                +" AND "+ cro_fef + " in (select " + oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{cro_fef})
                +" from E_CRO where " + cro_mun + "=" + codMunicipio
                +" AND  "+ cro_pro + "='" + codProcedimiento + "'"
                +" AND "+ cro_eje + "=" + ejercicio
                +" AND "+ cro_num + "='" + numero +"')";

        st = con.createStatement(); 
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);

        resultado = st.executeUpdate(sql);
        st.close();

        return resultado;
    }

      //Reabrimos todos los tramites del expediente que esten cerrados de forma no convencional
    public int reabrirTramites(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
            throws SQLException,TechnicalException, BDException  {

        Statement st = null;
        String sql = null;
        int resultado=0;
        String codTramite;
        Vector tramites=new Vector();
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String numero = (String)gVO.getAtributo("numero");


       //CONSULTA DE TRAMITES QUE DEBEMOS ABRIR

          sql = "SELECT * FROM E_TRASIT WHERE TRASIT_MUN="+
                  codMunicipio+" AND TRASIT_EJE="+ejercicio+" AND TRASIT_NUM='"+numero+"' AND TRASIT_PRO='"+codProcedimiento+"' AND TRASIT_SITUACION=1";
           if(m_Log.isDebugEnabled()) m_Log.debug(sql);
         st = con.createStatement();
          ResultSet rs = st.executeQuery(sql);
         while(rs.next()) {
            codTramite = rs.getString("TRASIT_COD");
             tramites.addElement(codTramite);
        }
           st.close();
           rs.close();
         for(int i=0;i<tramites.size();i++){
            //CAMBIAMOS LA FECHA DEL TRAMITE A VACIO
            sql = " UPDATE E_CRO SET cro_fef = null";
            sql += " WHERE " + cro_mun + "=" + codMunicipio
                    +" AND  "+ cro_pro + "='" + codProcedimiento + "'"
                    +" AND "+ cro_eje + "=" + ejercicio
                    +" AND "+ cro_num + "='" + numero +"'"
                    +" AND "+ cro_tra + "=" + tramites.elementAt(i) ;

            st = con.createStatement();
             if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = st.executeUpdate(sql);
            st.close();
            //BORRAMOS EL TRAMITE DE LA TABLA TRASIT

             sql ="DELETE FROM E_TRASIT WHERE TRASIT_MUN="+
                  codMunicipio+" AND TRASIT_COD="+tramites.elementAt(i)+"  AND TRASIT_EJE="+ejercicio+" AND TRASIT_NUM='"+numero+"' AND TRASIT_PRO='"+codProcedimiento+"' AND TRASIT_SITUACION=1";

             st = con.createStatement();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    resultado = st.executeUpdate(sql);
                    st.close();
         }
          sql ="DELETE FROM E_EXPSIT WHERE EXPSIT_MUN="+codMunicipio+" AND EXPSIT_EJE="+ejercicio+" AND EXPSIT_NUM='"+numero+"'";

          st = con.createStatement();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    resultado = st.executeUpdate(sql);
                    st.close();

        return resultado;
    }

    private GeneralValueObject tramitacionExpedientesVO (TramitacionExpedientesValueObject teVO) {
        GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("numeroRelacion", teVO.getNumeroRelacion());
        gVO.setAtributo("codMunicipio", teVO.getCodMunicipio());
        gVO.setAtributo("codProcedimiento",teVO.getCodProcedimiento());
        gVO.setAtributo("ejercicio", teVO.getEjercicio());
        gVO.setAtributo("numero", teVO.getNumero());
        gVO.setAtributo("ocurrenciaTramite", teVO.getOcurrenciaTramite());
        gVO.setAtributo("usuario", teVO.getCodUsuario());
        gVO.setAtributo("codEntidad",teVO.getCodEntidad());
        gVO.setAtributo("codOrganizacion",teVO.getCodOrganizacion());
        gVO.setAtributo("codTramite", teVO.getCodTramite());
        gVO.setAtributo("fechaInicio",teVO.getFechaInicio());
        gVO.setAtributo("fechaInicioPlazo", teVO.getFechaInicioPlazo());
        gVO.setAtributo("fechaFinPlazo", teVO.getFechaFinPlazo());
        gVO.setAtributo("fechaLimite", teVO.getFechaLimite());
        gVO.setAtributo("fechaFinTramite", teVO.getFechaFin());
        gVO.setAtributo("observaciones",teVO.getObservaciones());
        gVO.setAtributo("codUnidadOrganicaExp",teVO.getCodUnidadOrganicaExp());
        gVO.setAtributo("codUnidadTramitadoraTram",teVO.getCodUnidadTramitadoraTram());
        gVO.setAtributo("codUnidadTramitadoraManual", teVO.getCodUnidadTramitadoraManual());
        gVO.setAtributo("codUnidadTramitadoraUsu",teVO.getCodUnidadTramitadoraUsu());
        gVO.setAtributo("codUnidadOrganica",teVO.getCodUOR());
        gVO.setAtributo("codInteresados",teVO.getVectorCodInteresados());
        gVO.setAtributo("usuario",teVO.getCodUsuario());
        gVO.setAtributo("bloqueo",teVO.getBloqueo());
        return gVO;
    }


    /**
     * Recupera la descripción del trámite de inicio de un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return String
     * @throws es.altia.common.exception.TechnicalException
     */
    public String getDescripcionTramInicio(String codProcedimiento,Connection con) throws TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        String descripcion = "";

        try{
            sql = "SELECT PRO_TRI,TML_VALOR FROM E_PRO,E_TML WHERE PRO_COD=? AND PRO_COD=TML_PRO AND PRO_TRI=TML_TRA";
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setString(i++,codProcedimiento);
            rs = ps.executeQuery();
            while(rs.next()){
                descripcion = rs.getString("TML_VALOR");
            }


        }catch (Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }
            catch(Exception e){}
        }

         return descripcion;
    }

    public String getOcurrenciaTramiteAbierto(Connection con, TramitacionExpedientesValueObject tramExpVO){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        String descripcion = "";

        try{
            sql = "SELECT CRO_OCU FROM E_CRO WHERE CRO_NUM=? AND CRO_FEF IS NULL AND CRO_TRA=?";
            ps = con.prepareStatement(sql);
            m_Log.debug (sql);
            int i=1;
            ps.setString(i++,tramExpVO.getNumeroExpediente());
            ps.setString(i++,tramExpVO.getCodTramite());
            rs = ps.executeQuery();
            while(rs.next()){
                descripcion = rs.getString("CRO_OCU");
            }
            m_Log.debug (descripcion);


        }catch (Exception e){
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }
            catch(Exception e){}
        }

         return descripcion;

    }

    public String getUnidadTramitadoraTramite(String[] params, int municipio, String procedimiento, String numero,
            int ocu, int ejercicio, int tramite) {
        String sql = "SELECT UOR_COD_VIS FROM E_CRO, A_UOR WHERE CRO_PRO=? AND "
                + "CRO_NUM=? AND CRO_OCU=? "
                + "AND CRO_EJE=? AND CRO_TRA=? AND CRO_UTR=UOR_COD";
        m_Log.debug(sql);
        String resultado = "";
        try {
            AdaptadorSQLBD bd = new AdaptadorSQLBD(params);
            Connection con = bd.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, procedimiento);
            ps.setString(2, numero);
            ps.setInt(3, ocu);
            ps.setInt(4, ejercicio);
            ps.setInt(5, tramite);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resultado = rs.getString("UOR_COD_VIS");
            }
            con.close();
            rs.close();
        } catch (BDException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return resultado;
        }
    }



    /**
     * Recupera las diferentes ocurrencias de los trámites de un determinada expediente, tanto las pendientes como las cerradas
     * @param gvo: Datos del expediente
     * @param params: Parámetros de conexión a la base de datos
     * @return ArrayList<GeneralValueObject>
     */
    public ArrayList<GeneralValueObject> getOcurrenciaTramiteExpediente(GeneralValueObject gvo,Connection con) {

        ArrayList<GeneralValueObject> tramites = new ArrayList<GeneralValueObject>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT CRO_TRA,CRO_OCU  " +
                          " FROM E_CRO WHERE CRO_PRO=? AND " +
                          " CRO_EJE=? AND CRO_NUM=? AND " +
                           "CRO_MUN=? ";
        m_Log.debug(sql);

        try {
            
            ps = con.prepareStatement(sql);

            String codMunicipio = (String)gvo.getAtributo("codMunicipio");
            String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
            String ejercicio = (String)gvo.getAtributo("ejercicio");
            String numeroExpediente = (String)gvo.getAtributo("numeroExpediente");

            int i=1;
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setString(i++, numeroExpediente);
            ps.setInt(i++, Integer.parseInt(codMunicipio));
            
            rs = ps.executeQuery();
            while (rs.next()) {
                int codTra = rs.getInt("CRO_TRA");
                int ocuTra = rs.getInt("CRO_OCU");
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codTramite", codTra);
                gVO.setAtributo("ocurrenciaTramite", ocuTra);
                gVO.setAtributo("codMunicipio", codMunicipio);
                gVO.setAtributo("codProcedimiento", codProcedimiento);
                gVO.setAtributo("ejercicio", ejercicio);
                gVO.setAtributo("numeroExpediente", numeroExpediente);

                tramites.add(gVO);
            }
            
        } catch (SQLException ex) {
             ex.printStackTrace();
        } finally {
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return tramites;
    }
    
    /**
     * NOTA: En este método no se va a comprobar si el usuario tiene permisos o no para modificar el trámite.
     *      Se da por supuesto que sí que tiene permisos, porque sólo se accede desde el portafirmas.
     */
    public int finalizarTramiteConSubsanacion(AdaptadorSQL adapt, Connection con, TramitacionExpedientesValueObject teVO, boolean desFavorable)
            throws SQLException,TechnicalException, BDException  {
        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        int resultado;
        m_Log.debug(TramitesExpedienteDAO.class.getName() + "--> finalizarTramiteConSubsanacion");
        resultado = finalizarTramiteConSubsanacion(adapt, con, gVO, desFavorable);
        if (resultado > 0) {
            teVO.getListaEMailsAlFinalizar().addElement(getMailsUsuariosAlFinalizar(adapt,con,gVO));
        }
        m_Log.debug(TramitesExpedienteDAO.class.getName() + "<-- finalizarTramiteConSubsanacion");
        return resultado;
    }

    /**
     * NOTA: En este método no se va a comprobar si el usuario tiene permisos o no para modificar el trámite.
     *      Se da por supuesto que sí que tiene permisos, porque sólo se accede desde el portafirmas.
     */
    public int finalizarTramiteConSubsanacion(AdaptadorSQL adapt, Connection con, GeneralValueObject gVO, boolean desFavorable)
            throws SQLException,TechnicalException, BDException  {
        gVO.setAtributo("resolucion",(desFavorable?"0":"1"));        
        int result = actualizarTramite(adapt, con, gVO, true);
        return result;
    }
}