package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import java.util.Vector;
import java.sql.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.util.cache.CacheDatosFactoria;

public class TramitesRelacionExpedienteDAO {
   // Tabla e_cro

  protected static Config m_ConfigTechnical;   //Para el fichero de configuracion tecnico.

   // protected static Config m_ConfigError; //Para los mensajes de error localizados.
   // protected static Log m_Log;   //Para informacion de logs.

  protected static Log m_Log =
		  LogFactory.getLog(TramitesRelacionExpedienteDAO.class.getName());

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
  protected static String cro_ocu;
  protected static String cro_res;
  protected static String cro_obs;
  protected static String cro_usf;

  protected static String pro_mun;
  protected static String pro_cod;
  protected static String pro_tri;
  protected static String pro_utr;

  protected static String tra_mun;
  protected static String tra_pro;
  protected static String tra_cod;
  protected static String tra_utr;
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
  protected static String rel_tri;
  protected static String rel_trai;

  protected static String trp_mun;
  protected static String trp_eje;
  protected static String trp_rel;
  protected static String trp_pro;
  protected static String trp_tra;
  protected static String trp_ent;
  protected static String trp_ctr;
  protected static String trp_est;
  protected static String trp_fei;
  protected static String trp_fef;
  protected static String trp_uor;

  protected	static String tml_mun;
  protected	static String tml_pro;
  protected	static String tml_tra;
  protected	static String tml_cmp;
  protected	static String tml_leng;
  protected	static String tml_valor;


	protected	static String w_class;
	protected	static String w_url;
	protected static String w_cod;

	protected static String r_sw_municipio;
	protected static String r_sw_procedimiento;
	protected static String r_sw_ejercicio;
	protected static String r_sw_numero;
	protected static String r_sw_tramite;
	protected static String r_sw_ocurrencia;
	protected static String r_sw_sw;
	protected static String r_sw_valor;
    protected static String r_sw_importe;
    protected static String r_sw_anulado;
    protected static String r_sw_fecha;
    protected static String idiomaDefecto;



  private static TramitesRelacionExpedienteDAO instance = null;

  protected TramitesRelacionExpedienteDAO() {
	super();
	// Queremos usar el fichero de configuracion techserver
	m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
	//Queremos tener acceso a los mensajes de error localizados
	//m_ConfigError = ConfigServiceHelper.getConfig("error");
	//Queremos usar el servicio de log

        idiomaDefecto =m_ConfigTechnical.getString("idiomaDefecto");
	cro_mun = m_ConfigTechnical.getString("SQL.G_CRO.codMunicipio");
	cro_pro = m_ConfigTechnical.getString("SQL.G_CRO.codProcedimiento");
	cro_tra = m_ConfigTechnical.getString("SQL.G_CRO.codTramite");
	cro_eje = m_ConfigTechnical.getString("SQL.G_CRO.ano");
	cro_num = m_ConfigTechnical.getString("SQL.G_CRO.numero");
	cro_usu = m_ConfigTechnical.getString("SQL.G_CRO.codUsuario");
	cro_fei = m_ConfigTechnical.getString("SQL.G_CRO.fechaInicio");
	cro_fef = m_ConfigTechnical.getString("SQL.G_CRO.fechaFin");
	cro_utr = m_ConfigTechnical.getString("SQL.G_CRO.codUnidadTramitadora");
	cro_ocu = m_ConfigTechnical.getString("SQL.G_CRO.ocurrencia");
	cro_fip = m_ConfigTechnical.getString("SQL.G_CRO.fechaInicioPlazo");
	cro_ffp = m_ConfigTechnical.getString("SQL.G_CRO.fechaFinPlazo");
	cro_flim = m_ConfigTechnical.getString("SQL.G_CRO.fechaLimite");
	cro_res = m_ConfigTechnical.getString("SQL.G_CRO.resolucion");
	cro_obs = m_ConfigTechnical.getString("SQL.G_CRO.observaciones");
	cro_usf = m_ConfigTechnical.getString("SQL.G_CRO.usuarioFinalizacion");

	pro_mun = m_ConfigTechnical.getString("SQL.E_PRO.municipio");
	pro_cod = m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
	pro_tri = m_ConfigTechnical.getString("SQL.E_PRO.codTramiteInicio");
	pro_utr = m_ConfigTechnical.getString("SQL.E_PRO.unidad");

	tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
	tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
	tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
	tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");
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

    rel_mun= m_ConfigTechnical.getString("SQL.G_REL.codMunicipio");
    rel_pro= m_ConfigTechnical.getString("SQL.G_REL.codProcedimiento");
    rel_eje= m_ConfigTechnical.getString("SQL.G_REL.ano");
    rel_num= m_ConfigTechnical.getString("SQL.G_REL.numero");
    rel_uor= m_ConfigTechnical.getString("SQL.G_REL.uorInicio");
    rel_tri= m_ConfigTechnical.getString("SQL.G_REL.codTramiteInicio");
    rel_trai= m_ConfigTechnical.getString("SQL.G_REL.codTramiteActual");

	tml_mun	= m_ConfigTechnical.getString("SQL.E_TML.codMunicipio");
	tml_pro	= m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento");
	tml_tra	= m_ConfigTechnical.getString("SQL.E_TML.codTramite");
	tml_cmp	= m_ConfigTechnical.getString("SQL.E_TML.codCampoML");
	tml_leng 	= m_ConfigTechnical.getString("SQL.E_TML.idioma");
	tml_valor 	= m_ConfigTechnical.getString("SQL.E_TML.valor");

	w_class = m_ConfigTechnical.getString("SQL.WEB_SERVICES.clase");
	w_url = m_ConfigTechnical.getString("SQL.WEB_SERVICES.url");
	w_cod = m_ConfigTechnical.getString("SQL.WEB_SERVICES.codigo");

	r_sw_municipio = m_ConfigTechnical.getString("SQL.R_SW.MUNICIPIO");
	r_sw_procedimiento = m_ConfigTechnical.getString("SQL.R_SW.PROCEDIMIENTO");
	r_sw_ejercicio = m_ConfigTechnical.getString("SQL.R_SW.EJERCICIO");
	r_sw_numero = m_ConfigTechnical.getString("SQL.R_SW.NUMERO");
	r_sw_tramite= m_ConfigTechnical.getString("SQL.R_SW.TRAMITE");
	r_sw_ocurrencia = m_ConfigTechnical.getString("SQL.R_SW.OCURRENCIA");
	r_sw_sw = m_ConfigTechnical.getString("SQL.R_SW.SW");
	r_sw_valor = m_ConfigTechnical.getString("SQL.R_SW.VALOR");
    r_sw_importe = m_ConfigTechnical.getString("SQL.R_SW.IMPORTE");
    r_sw_anulado = m_ConfigTechnical.getString("SQL.R_SW.ANULADO");
    r_sw_fecha = m_ConfigTechnical.getString("SQL.R_SW.FECHA");

  }

  public static TramitesRelacionExpedienteDAO getInstance() {
	//si no hay ninguna instancia de esta clase tenemos que crear una.
	if (instance == null) {
	  // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
	  synchronized(TramitesRelacionExpedienteDAO.class){
		if (instance == null)
		  instance = new TramitesRelacionExpedienteDAO();
	  }
	}
	return instance;
  }


  public int actualizarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws SQLException,TechnicalException, BDException  {
	if (TramitesRelacionExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0)
	  return actualizarTramite(oad, con, gVO, false);
	return -1;
  }

  public int finalizarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws SQLException,TechnicalException, BDException  {
		if (permisoModificacionTramiteUsuario(oad, con, gVO) > 0) {
			int result = actualizarTramite(oad, con, gVO, true);

			return result;
	}
	return -1;
  }

  public int finalizarTramiteConResolucion(AdaptadorSQL oad, Connection con, GeneralValueObject gVO, boolean desFavorable)
  throws SQLException,TechnicalException, BDException  {
	gVO.setAtributo("resolucion",(desFavorable?"0":"1"));
		if (TramitesRelacionExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) > 0) {
			int result = actualizarTramite(oad, con, gVO, true);

			return result;
		} else
			return -1;
  }

  public int actualizarTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
  throws SQLException,TechnicalException, BDException  {
	GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
	if (TramitesRelacionExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0)
	  return actualizarTramite(oad, con, gVO);
	else return -1;
  }

  public int finalizarTramite(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
  throws SQLException,TechnicalException, BDException  {
	GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
	int resultado;
	if (TramitesRelacionExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0) {
		resultado = finalizarTramite(oad, con, gVO);
		if (resultado>0) {
			teVO.getListaEMailsAlFinalizar().addElement(getMailsUsuariosAlFinalizar(oad,con,gVO));
		}
		return resultado;
	} else return -1;
  }

  public int finalizarTramiteConResolucion(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO, boolean desFavorable)
  throws SQLException,TechnicalException, BDException  {
	GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
	int resultado;
	if (TramitesRelacionExpedienteDAO.getInstance().permisoModificacionTramiteUsuario(oad, con, gVO) >0) {
	  resultado = finalizarTramiteConResolucion(oad, con, gVO, desFavorable);
		if (resultado>0) {
			teVO.getListaEMailsAlFinalizar().addElement(getMailsUsuariosAlFinalizar(oad,con,gVO));
		}
		return resultado;
	}
	else return -1;
  }


  public int iniciarTramiteInicio(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws SQLException, TechnicalException, BDException {

	Statement st = null;
	String sql = null;
	int resultado=0;

	String codMunicipio = (String)gVO.getAtributo("codMunicipio");
	String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
	//String uorTramite = (String)gVO.getAtributo("codUOR");
	String codTramite = "";


	st = con.createStatement();
	sql = " SELECT " + pro_tri  + " from e_pro  WHERE "+ pro_mun +"="+ codMunicipio
            +" AND "+ pro_cod+ "='" + codProcedimiento +"'";

	if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	ResultSet rs = st.executeQuery(sql);
	if (rs.next() ) {
	  codTramite = rs.getString(pro_tri);

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

  }

  public int iniciarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws SQLException, TechnicalException, BDException {

	String ocurrencia ="";
	int resultado = 0;

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
	int resultado = iniciarTramite(oad, con, gVO);
	teVO.setOcurrenciaTramite((String) gVO.getAtributo("ocurrenciaTramite"));

	teVO.getListaEMailsAlIniciar().addElement(gVO.getAtributo("mailsUsuariosAlIniciar"));
	teVO.setUnidadTramitadoraTramiteIniciado((String) gVO.getAtributo("nombreUORTramiteIniciado"));
	return resultado;
  }

  public int iniciarTramiteManual(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
  throws SQLException,TechnicalException, BDException  {
	GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
	int resultado = iniciarTramiteManual(oad, con, gVO);
	  teVO.setOcurrenciaTramite((String) gVO.getAtributo("ocurrenciaTramite"));
	teVO.setUnidadTramitadoraTramiteIniciado((String) gVO.getAtributo("nombreUORTramiteIniciado"));
	return resultado;
  }

  public int iniciarTramiteManual(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws SQLException, TechnicalException, BDException {

	String ocurrencia ="";
	int resultado=0;

	ocurrencia = getOcurrenciaTramite(oad, con , gVO);

	if (!"0".equals(ocurrencia)) {
	  gVO.setAtributo("ocurrenciaTramite", ocurrencia);
	  resultado = insertarTramiteManual(oad,con,gVO);
	}
	else resultado = 0;

	return resultado;

  }

  private int insertarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws SQLException {

	Statement st = null;
	ResultSet rs = null;
	String sql = null;
	int resultado=0;

	String codMunicipio = (String)gVO.getAtributo("codMunicipio");
	String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
	String ejercicio = (String)gVO.getAtributo("ejercicio");
	String numero = (String)gVO.getAtributo("numeroRelacion");
	String ocurrencia = (String) gVO.getAtributo("ocurrenciaTramite");
	String usuario = (String)gVO.getAtributo("usuarioIni");
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
    String bloqueo = (String) gVO.getAtributo("bloqueo");
	if(codUnidadTramitadoraTram != null && !"".equals(codUnidadTramitadoraTram)) {
	  if("99".equals(codUnidadTramitadoraTram)) {
		sql = "SELECT " + trp_uor + " FROM G_TRP WHERE " + trp_mun + "=" + codMunicipio +
		  " AND " + trp_eje + "=" + ejercicio + " AND " + trp_rel + "='" + numero +
		  "' AND " + trp_pro + "='" + codProcedimiento + "' AND " + trp_tra + "=" + codTramite;
		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		st = con.createStatement();
		rs = st.executeQuery(sql);
		if(rs.next()){
		  uor = rs.getString(trp_uor);
		}
		rs.close();
		st.close();
		if((uor == null) || ("".equals(uor))) {
		  uor = codUnidadTramitadoraAnterior;
		  if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
		}
	  } else if("98".equals(codUnidadTramitadoraTram)) {
		  if(codUnidadTramitadoraUsu != null && !"".equals(codUnidadTramitadoraUsu)) {
			  uor = codUnidadTramitadoraUsu;
		  } else {
			  sql = "SELECT " + sql_uou_uor + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU WHERE " + sql_uou_usu + "=" +
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
	  sql = "SELECT " + tra_utr + " FROM E_TRA WHERE " + tra_mun + "=" + codMunicipio + " AND " +
			tra_pro + "='" + codProcedimiento + "' AND " + tra_cod + "=" + codTramite;
	  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	  st = con.createStatement();
	  rs = st.executeQuery(sql);
	  while(rs.next()) {
		codUnidadTramitadoraTram = rs.getString(tra_utr);
	  }
	  if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad tramitadora del tramite es ::::::::::::::::::::::::: : " + codUnidadTramitadoraTram);
	  rs.close();
	  st.close();
	  if(codUnidadTramitadoraTram != null && !"".equals(codUnidadTramitadoraTram)) {
		if("99".equals(codUnidadTramitadoraTram)) {
		  sql = "SELECT " + trp_uor + " FROM G_TRP WHERE " + trp_mun + "=" + codMunicipio +
				" AND " + trp_eje + "=" + ejercicio + " AND " + trp_rel + "='" + numero +
				"' AND " + trp_pro + "='" + codProcedimiento + "' AND " + trp_tra + "=" + codTramite;
		  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		  st = con.createStatement();
		  rs = st.executeQuery(sql);
		  if(rs.next()){
			uor = rs.getString(trp_uor);
		  }
		  rs.close();
		  st.close();
		  if((uor == null) || ("".equals(uor))) {
			uor = codUnidadTramitadoraAnterior;
			if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
		  }
		} else if("98".equals(codUnidadTramitadoraTram)) {
			if(codUnidadTramitadoraUsu != null && !"".equals(codUnidadTramitadoraUsu)) {
				  uor = codUnidadTramitadoraUsu;
			  } else {
				  sql = "SELECT " + sql_uou_uor + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU WHERE " + sql_uou_usu + "=" +
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

	sql = "SELECT " + cro_tra + " FROM G_CRO WHERE " + cro_mun + "=" + codMunicipio +
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
	  st = con.createStatement();
	  sql = "INSERT INTO G_CRO  ("
		  + cro_mun +", "+ cro_pro +","+ cro_eje+","+ cro_num+","+ cro_tra+","
		  + cro_ocu+","+cro_fei+","+cro_fef+","+cro_usu+","+cro_utr
		  + ") VALUES ("
		  + codMunicipio + ",'"+ codProcedimiento + "'," + ejercicio + ",'" + numero +"'"
		  + "," + codTramite + "," + ocurrencia
	  //+ ","+oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH:MM")
		  + ","+oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null)
		  + ", null"
		  + ", " + usuario + "," + uor
		  +")";

	  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	  resultado = st.executeUpdate(sql);
	  gVO.setAtributo("codUORTramiteIniciado",uor);
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],uor);

                    if (uorDTO!=null)
                        gVO.setAtributo("nombreUORTramiteIniciado",uorDTO.getUor_nom());
	} else {
	  resultado = 1;
	}
	return resultado;
  }

	private EstructuraNotificacion getMailsUsuariosAlIniciar(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
	{

	  Statement st = null;
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
	  Vector codInteresados = (Vector) gVO.getAtributo("codInteresados");
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

	  sql = "SELECT " + tra_uti + "," + tra_usi + "," + tra_ini +  "TRA_NOTIF_UITI, TRA_NOTIF_UIEI " +
			" FROM e_tra WHERE " + tra_mun + "=" + codMunicipio + " AND " + tra_pro + "=" + oad.addString(codProcedimiento) +
			" AND " + tra_cod + "=" +codTramite;
	  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	  st = con.createStatement();
	  rs = st.executeQuery(sql);
	  while(rs.next()) {
		uti = rs.getString(tra_uti);
		usi = rs.getString(tra_usi);
		ini = rs.getString(tra_ini);
        uiti = rs.getString("TRA_NOTIF_UITI");
        uiei = rs.getString("TRA_NOTIF_UIEI");
		resultado = 1;
	  }
      rs.close();
      st.close();
	  if (resultado > 0) {

		  sql = "SELECT " + tml_valor +
				" FROM e_tml WHERE " + tml_mun + "=" + codMunicipio + " AND " + tml_pro + "=" + oad.addString(codProcedimiento) +
				" AND " + tml_tra + "=" +codTramite;
		  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		  st = con.createStatement();
		  rs = st.executeQuery(sql);
		  while(rs.next()) {
			eNotif.setNombreTramite(rs.getString(tml_valor));
		  }
          rs.close();
          st.close();

		  if (uti.equals("S")) {
			//coger mail de a_uor buscando por uor
                                                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],codUORTramiteIniciado);

                                                    if (uorDTO!=null)
                                                        if (uorDTO.getUor_email()!=null && !uorDTO.getUor_email().equals("")) {
                                                            if(m_Log.isDebugEnabled()) m_Log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);
                                                            mailsUOR.addElement(uor_mail);
                                                        }
		  }
		  if (usi.equals("S")) {
			//coger usuarios de a_uou buscando por uor
			sql = "SELECT " + sql_uou_usu + " FROM "+GlobalNames.ESQUEMA_GENERICO+"a_uou WHERE " + sql_uou_uor + "=" + codUORTramiteIniciado + " AND "
				+ sql_uou_org + "=" + codOrganizacion + " AND " + sql_uou_usu + "<>" + codUsuario;
			if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
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
				sql = "SELECT " + usu_email + " FROM "+GlobalNames.ESQUEMA_GENERICO+"a_usu WHERE " + usu_cod + "=" + usuarios.elementAt(i);
				if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				st = con.createStatement();
				rs = st.executeQuery(sql);
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
			//coger mail de t_ter buscando por codInteresados
			if (codInteresados!=null) {
			for (int i=0;i<codInteresados.size();i++) {
				sql = "SELECT " + ter_dce + " FROM t_ter WHERE " + ter_cod + "=" + codInteresados.elementAt(i);
				if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				st = con.createStatement();
				rs = st.executeQuery(sql);
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
                      + " A_USU.USU_COD= E_CRO.CRO_USU )"
                      + " WHERE E_CRO.CRO_NUM = '" + gVO.getAtributo("numero") + "'"
                      + " AND E_CRO.CRO_TRA = '" + codTramite + "'";
              st = con.createStatement();
              m_Log.debug("Consulta Mail Usuario Inicio Tramite" + sql);

              rs = st.executeQuery(sql);

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
                      + " INNER JOIN E_EXP ON (A_USU.USU_COD= E_EXP.EXP_USU )"
                      + " WHERE E_EXP.EXP_NUM = '" + gVO.getAtributo("numero") + "'";

              st = con.createStatement();

              m_Log.debug("Consulta Mail Usuario Inicio Expediente: " + sql);

              rs = st.executeQuery(sql);

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
		  if(m_Log.isErrorEnabled()) m_Log.error("Exception: "+e.getMessage());
	  }
	  return eNotif;
	}

	private EstructuraNotificacion getMailsUsuariosAlFinalizar(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
	throws SQLException {

	  Statement st = null;
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
	  Vector codInteresados = (Vector) gVO.getAtributo("codInteresados");
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

	  sql = "SELECT " + tra_utf + "," + tra_usf + "," + tra_inf + "TRA_NOTIF_UITF, TRA_NOTIF_UIEF " +
			" FROM e_tra WHERE " + tra_mun + "=" + codMunicipio + " AND " + tra_pro + "=" + oad.addString(codProcedimiento) +
			" AND " + tra_cod + "=" +codTramite;
	  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	  st = con.createStatement();
	  rs = st.executeQuery(sql);
	  while(rs.next()) {
		utf = rs.getString(tra_utf);
		usf = rs.getString(tra_usf);
		inf = rs.getString(tra_inf);
        uitf = rs.getString("TRA_NOTIF_UITF");
        uief = rs.getString("TRA_NOTIF_UIEF");
		resultado = 1;
	  }
      rs.close();
      st.close();
	  if (resultado > 0) {

		  sql = "SELECT " + tml_valor +
				" FROM e_tml WHERE " + tml_mun + "=" + codMunicipio + " AND " + tml_pro + "=" + oad.addString(codProcedimiento) +
				" AND " + tml_tra + "=" +codTramite;
		  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		  st = con.createStatement();
		  rs = st.executeQuery(sql);
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
                                                if(m_Log.isDebugEnabled()) m_Log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);
                                                mailsUOR.addElement(uor_mail);
                                            }
		  }
		  if (usf.equals("S")) {
			//coger usuarios de a_uou buscando por uor
			sql = "SELECT " + sql_uou_usu + " FROM "+GlobalNames.ESQUEMA_GENERICO+"a_uou WHERE " + sql_uou_uor + "=" + uor + " AND " + sql_uou_org +
				"=" + codOrganizacion + " AND " + sql_uou_usu + "<>" + codUsuario;
			if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			st = con.createStatement();
			rs = st.executeQuery(sql);
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
				sql = "SELECT " + usu_email + " FROM "+GlobalNames.ESQUEMA_GENERICO+"a_usu WHERE " + usu_cod + "=" + usuarios.elementAt(i);
				if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				st = con.createStatement();
				rs = st.executeQuery(sql);
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
			//coger mail de t_ter buscando por codInteresados
			if (codInteresados!=null) {
			for (int i=0;i<codInteresados.size();i++) {
				sql = "SELECT " + ter_dce + " FROM t_ter WHERE " + ter_cod + "=" + codInteresados.elementAt(i);
				if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				st = con.createStatement();
				rs = st.executeQuery(sql);
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
                      + " A_USU.USU_COD= E_CRO.CRO_USU )"
                      + " WHERE E_CRO.CRO_NUM = '" + gVO.getAtributo("numero") + "'"
                      + " AND E_CRO.CRO_TRA = '" + codTramite + "'";
              st = con.createStatement();
              m_Log.debug("Consulta Mail Usuario Inicio Tramite" + sql);

              rs = st.executeQuery(sql);

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
                      + " WHERE E_EXP.EXP_NUM = '" + gVO.getAtributo("numero") + "'";

              st = con.createStatement();

              m_Log.debug("Consulta Mail Usuario Inicio Expediente: " + sql);

              rs = st.executeQuery(sql);

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
	}
	return eNotif;
	}

  private int insertarTramiteManual(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws SQLException {

	PreparedStatement st = null;
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
	if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad tramitadora anterior del tramite es : " + codUnidadTramitadoraAnterior);
	if(codUnidadTramitadoraTram != null && !"".equals(codUnidadTramitadoraTram)) {
//      sql = "SELECT " + sql_uot_uor + " FROM A_UOT WHERE " + sql_uot_utr + "=" + codUnidadTramitadoraTram;
//      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
//      st = con.createStatement();
//      rs = st.executeQuery(sql);
//      while(rs.next()) {
//        uor = rs.getString(sql_uot_uor);
//      }
//      if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
//      rs.close();
//      st.close();
		uor = codUnidadTramitadoraTram;
		if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica de la cronologia es :::::::::::::::::::::::::::::::::::::::::::::::::::: : " + uor);
	} else {
	  if(codUnidadOrganicaExp !=null && !"".equals(codUnidadOrganicaExp)) {
		uor = codUnidadOrganicaExp;
	  } else {
		sql = "SELECT EXP_UOR FROM E_EXP WHERE EXP_MUN = ? AND EXP_EJE = ? " +
                                            " AND EXP_NUM = ?";
		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		st = con.prepareStatement(sql);
                                    int i = 1;
                                    st.setInt(i++, Integer.valueOf(codMunicipio));
                                    st.setInt(i++, Integer.valueOf(ejercicio));
                                    st.setString(i++, numero);
                                    
		rs = st.executeQuery();
		while(rs.next()) {
		  uor = rs.getString(exp_uor);
		}
		rs.close();
		st.close();
	  }
	}

	sql = "SELECT CRO_TRA FROM E_CRO WHERE CRO_MUN = ?" + 
                                    " AND CRO_PRO = ? AND CRO_EJE = ? AND CRO_NUM = ? AND CRO_TRA = ?" + 
                                    " AND CRO_OCU = ?";
	if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                  st = con.prepareStatement(sql);
                  int i = 1;
                  st.setInt(i++, Integer.valueOf(codMunicipio));
                  st.setString(i++, codProcedimiento);
                  st.setInt(i++, Integer.valueOf(ejercicio));
                  st.setString(i++, numero);
                  st.setInt(i++, Integer.valueOf(codTramite));
                  st.setInt(i++, Integer.valueOf(ocurrencia));

	rs = st.executeQuery();
	String yaExiste = "no";
	while(rs.next()) {
	  yaExiste = "si";
	}
	rs.close();
	st.close();

	if("no".equals(yaExiste)) {
	  sql = "INSERT INTO E_CRO  (CRO_MUN,CRO_PRO,CRO_EJE,CRO_NUM,CRO_TRA,CRO_OCU," + 
                            "CRO_FEI,CRO_FEF,CRO_USU,CRO_UTR,) VALUES (?,?,?,?,?,?,?,null,?,?)";

	  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	  st = con.prepareStatement(sql);
                    i = 1;
                    st.setInt(i++, Integer.valueOf(codMunicipio));
                    st.setString(i++, codProcedimiento);
                    st.setInt(i++, Integer.valueOf(ejercicio));
                    st.setString(i++, numero);
                    st.setInt(i++, Integer.valueOf(codTramite));
                    st.setInt(i++, Integer.valueOf(ocurrencia));
                    st.setTimestamp(i++, new Timestamp((new java.util.Date()).getTime()));
                    st.setInt(i++, Integer.valueOf(usuario));
                    st.setInt(i++, Integer.valueOf(uor));

	  resultado = st.executeUpdate();
	  st.close();
	} else {
	  resultado = 1;
	}
	gVO.setAtributo("codUORTramiteIniciado",uor);
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],uor);

                    if (uorDTO!=null)
                        gVO.setAtributo("nombreUORTramiteIniciado",uorDTO.getUor_nom());
                    
	return resultado;
  }

  private int actualizarTramite(AdaptadorSQL oad, Connection con, GeneralValueObject gVO, boolean finalizarTramite)
  throws SQLException,TechnicalException, BDException  {

	Statement st = null;
	String sql = null;
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
	String observaciones = (String) gVO.getAtributo("observaciones");
	String codUsuario = (String) gVO.getAtributo("usuario");
    String bloqueo = (String) gVO.getAtributo("bloqueo");

	st = con.createStatement();
	sql = "UPDATE E_CRO  SET "
	/* Clave primaria
		+ cro_mun +"="+ codMunicipio
		+", "+ cro_pro +"="+  "'"+ codProcedimiento + "'"
		+","+ cro_eje +"="+ ejercicio
		+","+ cro_num +"="+ "'" + numero +"'"
		+","+ cro_tra +"="+ codTramite
		+","+ cro_ocu +"="+ ocurrencia
		*/
	/* No se actualizan
		+","+cro_fei +"="+oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
		+","+cro_usu +"="+usuario
		*/
	;

	sql += cro_fef +"=";
	if (finalizarTramite )
	  //sql += oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY");
	  sql += oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null) + "," + cro_usf + "=" + codUsuario;
	// En otro caso no se actualiza.
	else {
	  if (fechaFin != null)
		if (!"".equals(fechaFin.trim())) sql += oad.fechaHora(fechaFin, oad.FECHAHORA_CAMPO_FECHA) + "," + cro_usf + "=" + codUsuario;
		else sql += "null";
	  else sql += "null";
	}

	sql += ","+cro_fip +"=";
	if (fechaInicioPlazo != null)
	  if (!"".equals(fechaInicioPlazo.trim())) sql += oad.fechaHora(fechaInicioPlazo, oad.FECHAHORA_CAMPO_FECHA);
	  else sql += "null";
	else sql += "null";
	sql += ","+cro_ffp +"=";
	if (fechaFinPlazo != null)
	  if (!"".equals(fechaFinPlazo.trim())) sql += oad.fechaHora(fechaFinPlazo, oad.FECHAHORA_CAMPO_FECHA);
	  else sql += "null";
	else sql += "null";
	sql += ","+cro_flim +"=";
	if (fechaLimite != null)
	  if (!"".equals(fechaLimite.trim())) sql += oad.fechaHora(fechaLimite, oad.FECHAHORA_CAMPO_FECHA);
	  else sql += "null";
	else sql += "null";

	if (resolucion != null) sql +=","+cro_res +"=" + resolucion;
	if (observaciones != null) sql +=","+cro_obs +"='" + observaciones + "'";


	sql += " WHERE " + cro_mun + "=" + codMunicipio
	+" AND  "+ cro_pro + "='" + codProcedimiento + "'"
	+" AND "+ cro_eje + "=" + ejercicio
	+" AND "+ cro_num + "='" + numero +"'"
	+" AND "+ cro_tra + "=" + codTramite
	+" AND "+ cro_ocu + "=" + ocurrencia;

	if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	resultado = st.executeUpdate(sql);
    st.close();

	// ACTUALIZAR LOS VALORES EXP_TRA Y EXP_TOCU
	if(finalizarTramite || fechaFin != null || !"".equals(fechaFin.trim()) ) {
        st = con.createStatement();
		sql = "UPDATE E_EXP SET " + exp_tra + "=" + codTramite + "," + exp_tocu + "=" +
					ocurrencia + " WHERE " + exp_mun + "=" + codMunicipio + " AND " + exp_eje +
					"=" + ejercicio + " AND " + exp_num + "='" + numero + "'";
		if(m_Log.isDebugEnabled()) m_Log.debug(sql);
  	    resultado = st.executeUpdate(sql);
        st.close();
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

	public int permisoModificacionTramiteUsuario(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
	//throws SQLException,TechnicalException, BDException
	{
	  GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
	  return permisoModificacionTramiteUsuario(oad, con, gVO);
	}

  public int permisoModificacionTramiteUsuario(AdaptadorSQL oad, Connection con, GeneralValueObject gVO) {

	Statement stmt = null;
	String sql = null;
	ResultSet rs = null;
	String codMunicipio = (String)gVO.getAtributo("codMunicipio");
	String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
	String codTramite = (String) gVO.getAtributo("codTramite");
	String ejercicio = (String) gVO.getAtributo("ejercicio");
	String numeroRelacion = (String) gVO.getAtributo("numero");
	String ocurrenciaTramite = (String) gVO.getAtributo("ocurrenciaTramite");
	String codUsuario = (String) gVO.getAtributo("usuario");
	String codEntidad = (String) gVO.getAtributo("codEntidad");
	String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");

	int permiso = 0;

	try {

	  sql = "SELECT DISTINCT " + rel_uor + "," + cro_utr + "," + tra_pre +
				  " FROM G_REL, G_CRO, E_TRA WHERE " + rel_mun + "=" + cro_mun + " AND " +
				  rel_pro + "=" + cro_pro + " AND " + rel_eje + "=" + cro_eje + " AND " +
				  rel_num + "=" + cro_num + " AND " + rel_mun + "=" + codMunicipio + " AND " +
				  rel_pro + "='" + codProcedimiento + "' AND " + rel_eje + "=" + ejercicio +
				  " AND " + rel_num + "='" + numeroRelacion + "' AND " + cro_tra + "=" +
				  codTramite + " AND " + cro_ocu + "=" + ocurrenciaTramite + " AND " + cro_mun +
				  "=" + tra_mun + " AND " + cro_pro + "=" + tra_pro + " AND " +
				  tra_cod + "=" + codTramite + " AND ((EXISTS ( SELECT DISTINCT " +
					sql_uou_uor + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " + sql_uou_usu + "=" + codUsuario + " AND " + sql_uou_org +
			"=" + codOrganizacion + " AND " + sql_uou_ent + "=" + codEntidad + " AND " + sql_uou_uor +
					"=" + rel_uor + ") AND " + tra_pre + "=0) OR EXISTS (SELECT DISTINCT " + sql_uou_uor + " FROM " +
					GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU WHERE " + sql_uou_usu +
			"=" + codUsuario + " AND " + sql_uou_org + "=" + codOrganizacion + " AND " + sql_uou_ent + "=" +
			codEntidad + " AND " + sql_uou_uor + "=" + cro_utr + "))" ;

	  if(m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: permisoModificacionTramiteUsuario --> "+ sql);
	  stmt = con.createStatement();
	  rs = stmt.executeQuery(sql);
	  if (rs.next()){
		String unidadControladora = (String) rs.getString(rel_uor);
		String unidadTramitadora = (String) rs.getString(cro_utr);
		if ( (unidadControladora != null) && (unidadTramitadora != null) ) permiso = 1;
	  }

	} catch (Exception e) {
		permiso =0;
		e.printStackTrace();
	}finally{
		try{
	  rs.close();
	  stmt.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
  return permiso;
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
			cro_tra + "=" + tml_tra + " AND " + tml_cmp + "='NOM' AND " + tml_leng + "=" + idiomaDefecto;

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
		con.close();
	  } catch(SQLException sqle) {
		sqle.printStackTrace();
		m_Log.error("SQLException del finally: " +	sqle.getMessage());
	  }
	}
	  if(m_Log.isDebugEnabled()) m_Log.debug("el resultado de la cuenta es : " + lista.size());
	return lista;
  }


  public Vector getListaUnidadesTramitadorasUsuario(GeneralValueObject gVO,String[] params)
  {
	Connection con = null;
	Statement stmt = null;
	String sql = null;
	ResultSet rs = null;
	AdaptadorSQLBD oad = null;
	String usuario = (String)gVO.getAtributo("usuario");
	Vector lista = new Vector();

	try {
	  oad = new AdaptadorSQLBD(params);
	  con = oad.getConnection();
	  sql = "SELECT "+ sql_uou_uor + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU WHERE " + sql_uou_usu + "="+ usuario;

	  if(m_Log.isDebugEnabled()) m_Log.debug("TramitesExpedienteDAO: getListaUnidadesTramitadoras --> "+ sql);
	  stmt = con.createStatement();
	  rs = stmt.executeQuery(sql);
	  while(rs.next()){
		String codUOT = (String) rs.getString(sql_uou_uor);
		lista.addElement(codUOT);
	  }
	  if(m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: getListaUnidadesTramitadoras --> numero unidades tramitadoras: " + lista.size());
	  rs.close();
	  stmt.close();

	// SQLException,TechnicalException, BDException
	} catch (Exception e) {
	  lista = null;
	  e.printStackTrace();
	} finally {
	  try {
		oad.devolverConexion(con);
	  } catch (BDException be) {
		be.printStackTrace();
	  }
	}
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


	sql = " UPDATE E_CRO SET cro_fef = ''";
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

	resultado = st.executeUpdate(sql);
	st.close();

	return resultado;
  }

  private GeneralValueObject tramitacionExpedientesVO (TramitacionExpedientesValueObject teVO) {
	GeneralValueObject gVO = new GeneralValueObject();
	gVO.setAtributo("codMunicipio", teVO.getCodMunicipio());
	gVO.setAtributo("codProcedimiento",teVO.getCodProcedimiento());
	gVO.setAtributo("ejercicio", teVO.getEjercicio());
	gVO.setAtributo("numero", teVO.getNumero());
	gVO.setAtributo("ocurrenciaTramite", teVO.getOcurrenciaTramite());
	gVO.setAtributo("usuario", teVO.getCodUsuario());
	gVO.setAtributo("codEntidad",teVO.getCodEntidad());
	gVO.setAtributo("codOrganizacion",teVO.getCodOrganizacion());
	//gVO.setAtributo("codUOR", teVO.getCodUOR());
	gVO.setAtributo("codTramite", teVO.getCodTramite());
	gVO.setAtributo("fechaInicio",teVO.getFechaInicio());
	gVO.setAtributo("fechaInicioPlazo", teVO.getFechaInicioPlazo());
	gVO.setAtributo("fechaFinPlazo", teVO.getFechaFinPlazo());
	gVO.setAtributo("fechaLimite", teVO.getFechaLimite());
	gVO.setAtributo("fechaFinTramite", teVO.getFechaFin());
	gVO.setAtributo("observaciones",teVO.getObservaciones());
	gVO.setAtributo("codUnidadOrganicaExp",teVO.getCodUnidadOrganicaExp());
	gVO.setAtributo("codUnidadTramitadoraTram",teVO.getCodUnidadTramitadoraTram());
	if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad organica del expediente en el cambio a general : " + teVO.getCodUnidadOrganicaExp());
	if(m_Log.isDebugEnabled()) m_Log.debug("el codigo de la unidad tramitadora del tramite en el cambio a general : " + teVO.getCodUnidadTramitadoraTram());
	gVO.setAtributo("codUnidadTramitadoraUsu",teVO.getCodUnidadTramitadoraUsu());
	gVO.setAtributo("codUnidadOrganica",teVO.getCodUOR());
	gVO.setAtributo("codInteresados",teVO.getVectorCodInteresados());
	gVO.setAtributo("usuario",teVO.getCodUsuario());
    gVO.setAtributo("bloqueo",teVO.getBloqueo());


	return gVO;
  }

}